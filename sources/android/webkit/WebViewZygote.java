package android.webkit;

import android.content.pm.PackageInfo;
import android.os.Build;
import android.os.ChildZygoteProcess;
import android.os.Process;
import android.os.ZygoteProcess;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;

public class WebViewZygote {
    private static final String LOGTAG = "WebViewZygote";
    private static final String[] WEBVIEW_ZYGOTE_PROCESS_NAME = new String[]{"webview_zygote"};
    private static final Object sLock = new Object();
    @GuardedBy({"sLock"})
    private static boolean sMultiprocessEnabled = false;
    @GuardedBy({"sLock"})
    private static PackageInfo sPackage;
    @GuardedBy({"sLock"})
    private static ChildZygoteProcess sZygote;

    public static ZygoteProcess getProcess() {
        synchronized (sLock) {
            ChildZygoteProcess childZygoteProcess;
            if (sZygote == null || !isWebViewZygoteAlive()) {
                if (sZygote != null) {
                    Log.e(LOGTAG, "webview_zygote is gone, need to be restarted");
                    sZygote.close();
                    sZygote = null;
                }
                connectToZygoteIfNeededLocked();
                childZygoteProcess = sZygote;
                return childZygoteProcess;
            }
            childZygoteProcess = sZygote;
            return childZygoteProcess;
        }
    }

    private static boolean isWebViewZygoteAlive() {
        int[] nativePids = Process.getPidsForCommands(WEBVIEW_ZYGOTE_PROCESS_NAME);
        return nativePids != null && nativePids.length >= 1;
    }

    public static String getPackageName() {
        String str;
        synchronized (sLock) {
            str = sPackage.packageName;
        }
        return str;
    }

    public static boolean isMultiprocessEnabled() {
        boolean z;
        synchronized (sLock) {
            z = sMultiprocessEnabled && sPackage != null;
        }
        return z;
    }

    public static void setMultiprocessEnabled(boolean enabled) {
        synchronized (sLock) {
            sMultiprocessEnabled = enabled;
            if (!enabled) {
                stopZygoteLocked();
            }
        }
    }

    static void onWebViewProviderChanged(PackageInfo packageInfo) {
        synchronized (sLock) {
            sPackage = packageInfo;
            if (sMultiprocessEnabled) {
                stopZygoteLocked();
                return;
            }
        }
    }

    @GuardedBy({"sLock"})
    private static void stopZygoteLocked() {
        ChildZygoteProcess childZygoteProcess = sZygote;
        if (childZygoteProcess != null) {
            childZygoteProcess.close();
            Process.killProcess(sZygote.getPid());
            sZygote = null;
        }
    }

    @GuardedBy({"sLock"})
    private static void connectToZygoteIfNeededLocked() {
        if (sZygote == null) {
            PackageInfo packageInfo = sPackage;
            String str = LOGTAG;
            if (packageInfo == null) {
                Log.e(str, "Cannot connect to zygote, no package specified");
                return;
            }
            try {
                String abi = packageInfo.applicationInfo.primaryCpuAbi;
                sZygote = Process.ZYGOTE_PROCESS.startChildZygote("com.android.internal.os.WebViewZygoteInit", "webview_zygote", 1053, 1053, null, 0, "webview_zygote", abi, TextUtils.join((CharSequence) ",", Build.SUPPORTED_ABIS), null, Process.FIRST_ISOLATED_UID, Integer.MAX_VALUE);
                ZygoteProcess.waitForConnectionToZygote(sZygote.getPrimarySocketAddress());
                sZygote.preloadApp(sPackage.applicationInfo, abi);
            } catch (Exception e) {
                Log.e(str, "Error connecting to webview zygote", e);
                stopZygoteLocked();
            }
        }
    }
}
