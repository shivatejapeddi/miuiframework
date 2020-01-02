package android.webkit;

import android.app.ActivityManagerInternal;
import android.app.ActivityThread;
import android.app.LoadedApk;
import android.content.pm.PackageInfo;
import android.net.TrafficStats;
import android.os.Build;
import android.os.RemoteException;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.server.LocalServices;
import dalvik.system.VMRuntime;
import java.util.Arrays;

@VisibleForTesting
public class WebViewLibraryLoader {
    private static final String CHROMIUM_WEBVIEW_NATIVE_RELRO_32 = "/data/misc/shared_relro/libwebviewchromium32.relro";
    private static final String CHROMIUM_WEBVIEW_NATIVE_RELRO_64 = "/data/misc/shared_relro/libwebviewchromium64.relro";
    private static final boolean DEBUG = false;
    private static final String LOGTAG = WebViewLibraryLoader.class.getSimpleName();
    private static boolean sAddressSpaceReserved = false;

    private static class RelroFileCreator {
        private RelroFileCreator() {
        }

        public static void main(String[] args) {
            String str = "failed to create relro file";
            String str2 = "error notifying update service";
            boolean is64Bit = VMRuntime.getRuntime().is64Bit();
            try {
                String libraryFileName;
                if (args.length == 2 && args[0] != null) {
                    if (args[1] != null) {
                        String packageName = args[0];
                        libraryFileName = args[1];
                        String access$000 = WebViewLibraryLoader.LOGTAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("RelroFileCreator (64bit = ");
                        stringBuilder.append(is64Bit);
                        stringBuilder.append("), package: ");
                        stringBuilder.append(packageName);
                        stringBuilder.append(" library: ");
                        stringBuilder.append(libraryFileName);
                        Log.v(access$000, stringBuilder.toString());
                        if (WebViewLibraryLoader.sAddressSpaceReserved) {
                            String str3;
                            LoadedApk apk = ActivityThread.currentActivityThread().getPackageInfo(packageName, null, 3);
                            if (is64Bit) {
                                str3 = WebViewLibraryLoader.CHROMIUM_WEBVIEW_NATIVE_RELRO_64;
                            } else {
                                str3 = WebViewLibraryLoader.CHROMIUM_WEBVIEW_NATIVE_RELRO_32;
                            }
                            boolean result = WebViewLibraryLoader.nativeCreateRelroFile(libraryFileName, str3, apk.getClassLoader());
                            try {
                                WebViewFactory.getUpdateServiceUnchecked().notifyRelroCreationCompleted();
                            } catch (RemoteException e) {
                                Log.e(WebViewLibraryLoader.LOGTAG, str2, e);
                            }
                            if (!result) {
                                Log.e(WebViewLibraryLoader.LOGTAG, str);
                            }
                            System.exit(0);
                            return;
                        }
                        Log.e(WebViewLibraryLoader.LOGTAG, "can't create relro file; address space not reserved");
                        try {
                            WebViewFactory.getUpdateServiceUnchecked().notifyRelroCreationCompleted();
                        } catch (RemoteException e2) {
                            Log.e(WebViewLibraryLoader.LOGTAG, str2, e2);
                        }
                        if (null == null) {
                            Log.e(WebViewLibraryLoader.LOGTAG, str);
                        }
                        System.exit(0);
                        return;
                    }
                }
                libraryFileName = WebViewLibraryLoader.LOGTAG;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Invalid RelroFileCreator args: ");
                stringBuilder2.append(Arrays.toString(args));
                Log.e(libraryFileName, stringBuilder2.toString());
            } finally {
                try {
                    WebViewFactory.getUpdateServiceUnchecked().notifyRelroCreationCompleted();
                } catch (RemoteException e3) {
                    Log.e(WebViewLibraryLoader.LOGTAG, str2, e3);
                }
                if (null == null) {
                    Log.e(WebViewLibraryLoader.LOGTAG, str);
                }
                System.exit(0);
            }
        }
    }

    static native boolean nativeCreateRelroFile(String str, String str2, ClassLoader classLoader);

    static native int nativeLoadWithRelroFile(String str, String str2, ClassLoader classLoader);

    static native boolean nativeReserveAddressSpace(long j);

    static void createRelroFile(boolean is64Bit, String packageName, String libraryFileName) {
        final String abi = is64Bit ? Build.SUPPORTED_64_BIT_ABIS[0] : Build.SUPPORTED_32_BIT_ABIS[0];
        Runnable crashHandler = new Runnable() {
            public void run() {
                try {
                    String access$000 = WebViewLibraryLoader.LOGTAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("relro file creator for ");
                    stringBuilder.append(abi);
                    stringBuilder.append(" crashed. Proceeding without");
                    Log.e(access$000, stringBuilder.toString());
                    WebViewFactory.getUpdateService().notifyRelroCreationCompleted();
                } catch (RemoteException e) {
                    String access$0002 = WebViewLibraryLoader.LOGTAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Cannot reach WebViewUpdateService. ");
                    stringBuilder2.append(e.getMessage());
                    Log.e(access$0002, stringBuilder2.toString());
                }
            }
        };
        try {
            ActivityManagerInternal activityManagerInternal = (ActivityManagerInternal) LocalServices.getService(ActivityManagerInternal.class);
            String name = RelroFileCreator.class.getName();
            String[] strArr = new String[]{packageName, libraryFileName};
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("WebViewLoader-");
            stringBuilder.append(abi);
            if (!activityManagerInternal.startIsolatedProcess(name, strArr, stringBuilder.toString(), abi, 1037, crashHandler)) {
                throw new Exception("Failed to start the relro file creator process");
            }
        } catch (Throwable t) {
            String str = LOGTAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("error starting relro file creator for abi ");
            stringBuilder2.append(abi);
            Log.e(str, stringBuilder2.toString(), t);
            crashHandler.run();
        }
    }

    static int prepareNativeLibraries(PackageInfo webViewPackageInfo) {
        String libraryFileName = WebViewFactory.getWebViewLibrary(webViewPackageInfo.applicationInfo);
        if (libraryFileName == null) {
            return 0;
        }
        return createRelros(webViewPackageInfo.packageName, libraryFileName);
    }

    private static int createRelros(String packageName, String libraryFileName) {
        int numRelros = 0;
        if (Build.SUPPORTED_32_BIT_ABIS.length > 0) {
            createRelroFile(false, packageName, libraryFileName);
            numRelros = 0 + 1;
        }
        if (Build.SUPPORTED_64_BIT_ABIS.length <= 0) {
            return numRelros;
        }
        createRelroFile(true, packageName, libraryFileName);
        return numRelros + 1;
    }

    static void reserveAddressSpaceInZygote() {
        System.loadLibrary("webviewchromium_loader");
        long addressSpaceToReserve = VMRuntime.getRuntime().is64Bit() ? TrafficStats.GB_IN_BYTES : 136314880;
        sAddressSpaceReserved = nativeReserveAddressSpace(addressSpaceToReserve);
        if (!sAddressSpaceReserved) {
            String str = LOGTAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("reserving ");
            stringBuilder.append(addressSpaceToReserve);
            stringBuilder.append(" bytes of address space failed");
            Log.e(str, stringBuilder.toString());
        }
    }

    public static int loadNativeLibrary(ClassLoader clazzLoader, String libraryFileName) {
        if (sAddressSpaceReserved) {
            String relroPath;
            if (VMRuntime.getRuntime().is64Bit()) {
                relroPath = CHROMIUM_WEBVIEW_NATIVE_RELRO_64;
            } else {
                relroPath = CHROMIUM_WEBVIEW_NATIVE_RELRO_32;
            }
            int result = nativeLoadWithRelroFile(libraryFileName, relroPath, clazzLoader);
            if (result != 0) {
                Log.w(LOGTAG, "failed to load with relro file, proceeding without");
            }
            return result;
        }
        Log.e(LOGTAG, "can't load with relro file; address space not reserved");
        return 2;
    }
}
