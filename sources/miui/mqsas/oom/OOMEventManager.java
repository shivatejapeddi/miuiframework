package miui.mqsas.oom;

import android.os.AnrMonitor;
import android.os.Debug;
import android.os.SystemProperties;
import android.util.Slog;
import java.io.File;
import java.io.IOException;
import miui.mqsas.sdk.MQSEventManagerDelegate;
import miui.mqsas.sdk.event.JavaExceptionEvent;

public class OOMEventManager {
    public static final String FILE_DIR_DATA_APP = "/data/data/";
    public static final String FILE_DIR_DATA_MQSAS_HPROF = "/data/mqsas/hprof/";
    public static final String FILE_HPROF_SUFFIX = ".hprof";
    public static final String JAVA_EXCEPTION_OOM_CLASS = "java.lang.OutOfMemoryError";
    public static final int OOM_EVENT_OTHER_APP_OOM_DEBUG = 3;
    public static final int OOM_EVENT_SYSTEM_PROC_DEBUG = 2;
    public static final int OOM_EVENT_SYSTEM_PROC_MARTCHED = 1;
    public static final int OOM_EVENT_TOO_NOISY = -1;
    private static final String TAG = "OOMEventManagerFK";

    public static boolean isInterestedOOMEvent(JavaExceptionEvent event) {
        if (JAVA_EXCEPTION_OOM_CLASS.equals(event.getExceptionClassName())) {
            return true;
        }
        return false;
    }

    public static int checkEventAndDumpheap(JavaExceptionEvent event, String pkgName) {
        int shouldDumpHeap = MQSEventManagerDelegate.getInstance().checkIfNeedDumpheap(event);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("checkEventAndDumpheap shouldDumpHeap : ");
        stringBuilder.append(shouldDumpHeap);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Slog.d(str, stringBuilder2);
        stringBuilder2 = "";
        String str2 = FILE_HPROF_SUFFIX;
        StringBuilder stringBuilder3;
        if (shouldDumpHeap == 3) {
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(FILE_DIR_DATA_APP);
            stringBuilder3.append(pkgName);
            stringBuilder3.append("/");
            stringBuilder3.append(pkgName);
            stringBuilder3.append(str2);
            doDumpheap(stringBuilder3.toString());
        } else if (shouldDumpHeap == 1) {
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(FILE_DIR_DATA_MQSAS_HPROF);
            stringBuilder3.append(pkgName);
            stringBuilder3.append(str2);
            doDumpheap(stringBuilder3.toString());
        } else if (shouldDumpHeap == 2) {
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append("/data/mqsas/hprof/debug/");
            stringBuilder3.append(pkgName);
            stringBuilder3.append(str2);
            doDumpheap(stringBuilder3.toString());
        } else if (isMtbfTest() && event.isSystem()) {
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(AnrMonitor.ANR_DIRECTORY);
            stringBuilder4.append(pkgName);
            stringBuilder4.append(str2);
            stringBuilder2 = stringBuilder4.toString();
            StringBuilder stringBuilder5 = new StringBuilder();
            stringBuilder5.append("checkEventAndDumpheap dump for mtbf test : ");
            stringBuilder5.append(stringBuilder2);
            Slog.d(str, stringBuilder5.toString());
            doDumpheap(stringBuilder2);
        }
        return shouldDumpHeap;
    }

    private static boolean isMtbfTest() {
        if (SystemProperties.getInt("ro.debuggable", 0) == 1 && SystemProperties.getInt("ro.miui.mtbftest", 0) == 1) {
            return true;
        }
        return false;
    }

    private static synchronized void doDumpheap(String outPutFileName) {
        synchronized (OOMEventManager.class) {
            if (outPutFileName != null) {
                if (outPutFileName.length() != 0) {
                    if (new File(outPutFileName).exists()) {
                        Slog.e(TAG, "dumpheap failed, outPutFileName already exists");
                        return;
                    }
                    try {
                        Debug.dumpHprofData(outPutFileName);
                        String str = TAG;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("dumpheap success : ");
                        stringBuilder.append(outPutFileName);
                        Slog.d(str, stringBuilder.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Slog.e(TAG, "dumpheap failed, outPutFileName is invalid!");
            return;
        }
    }
}
