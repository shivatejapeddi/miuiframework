package android.os.statistics;

public class PerfSuperviser {
    public static final boolean DEBUGGING = false;
    public static int MY_PID = -1;
    public static final String TAG = "MiuiPerfSuperviser";
    public static String packageName;
    public static String processName;
    private static volatile boolean sInitialized = false;
    private static volatile boolean sStarted = false;

    private static native void nativeInit(boolean z, boolean z2, int i, int i2, int i3, int i4, int i5);

    private static native void nativeStart(boolean z, boolean z2, int i);

    private static native void nativeUpdateProcessInfo(String str, String str2);

    public static native void setThreadPerfSupervisionOn(boolean z);

    static {
        String str = "";
        processName = str;
        packageName = str;
    }

    /* JADX WARNING: Missing block: B:13:0x0025, code skipped:
            return;
     */
    public static synchronized void init(boolean r10, boolean r11) {
        /*
        r0 = android.os.statistics.PerfSuperviser.class;
        monitor-enter(r0);
        r1 = android.os.Process.myPpid();	 Catch:{ all -> 0x0026 }
        r2 = 1;
        if (r1 == r2) goto L_0x000c;
    L_0x000a:
        monitor-exit(r0);
        return;
    L_0x000c:
        r1 = sInitialized;	 Catch:{ all -> 0x0026 }
        if (r1 != 0) goto L_0x0024;
    L_0x0010:
        android.os.statistics.PerfSupervisionSettings.init();	 Catch:{ all -> 0x0026 }
        r5 = android.os.statistics.PerfSupervisionSettings.sPerfSupervisionSoftThreshold;	 Catch:{ all -> 0x0026 }
        r6 = android.os.statistics.PerfSupervisionSettings.sPerfSupervisionHardThreshold;	 Catch:{ all -> 0x0026 }
        r7 = 10;
        r8 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r9 = android.os.statistics.PerfSupervisionSettings.sPerfSupervisionDivisionRatio;	 Catch:{ all -> 0x0026 }
        r3 = r10;
        r4 = r11;
        nativeInit(r3, r4, r5, r6, r7, r8, r9);	 Catch:{ all -> 0x0026 }
        sInitialized = r2;	 Catch:{ all -> 0x0026 }
    L_0x0024:
        monitor-exit(r0);
        return;
    L_0x0026:
        r10 = move-exception;
        monitor-exit(r0);
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.statistics.PerfSuperviser.init(boolean, boolean):void");
    }

    public static synchronized void start(boolean isSystemServer) {
        synchronized (PerfSuperviser.class) {
            start(isSystemServer, "");
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0054  */
    /* JADX WARNING: Missing block: B:40:0x0077, code skipped:
            return;
     */
    public static synchronized void start(boolean r4, java.lang.String r5) {
        /*
        r0 = android.os.statistics.PerfSuperviser.class;
        monitor-enter(r0);
        r1 = sInitialized;	 Catch:{ all -> 0x0078 }
        if (r1 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);
        return;
    L_0x0009:
        r1 = android.os.Process.myPpid();	 Catch:{ all -> 0x0078 }
        r2 = 1;
        if (r1 != r2) goto L_0x0012;
    L_0x0010:
        monitor-exit(r0);
        return;
    L_0x0012:
        r1 = sStarted;	 Catch:{ all -> 0x0078 }
        if (r1 != 0) goto L_0x0076;
    L_0x0016:
        r1 = android.os.Process.myPid();	 Catch:{ all -> 0x0078 }
        MY_PID = r1;	 Catch:{ all -> 0x0078 }
        if (r4 == 0) goto L_0x0028;
    L_0x001e:
        r1 = android.text.TextUtils.isEmpty(r5);	 Catch:{ all -> 0x0078 }
        if (r1 == 0) goto L_0x0028;
    L_0x0024:
        r1 = "system_server";
        goto L_0x002e;
    L_0x0028:
        if (r5 != 0) goto L_0x002d;
    L_0x002a:
        r1 = "";
        goto L_0x002e;
    L_0x002d:
        r1 = r5;
    L_0x002e:
        processName = r1;	 Catch:{ all -> 0x0078 }
        if (r4 != 0) goto L_0x0043;
    L_0x0032:
        r1 = android.text.TextUtils.isEmpty(r5);	 Catch:{ all -> 0x0078 }
        if (r1 == 0) goto L_0x0039;
    L_0x0038:
        goto L_0x0043;
    L_0x0039:
        r1 = ":";
        r1 = r5.split(r1);	 Catch:{ all -> 0x0078 }
        r3 = 0;
        r1 = r1[r3];	 Catch:{ all -> 0x0078 }
        goto L_0x0045;
    L_0x0043:
        r1 = "";
    L_0x0045:
        packageName = r1;	 Catch:{ all -> 0x0078 }
        r1 = processName;	 Catch:{ all -> 0x0078 }
        r3 = packageName;	 Catch:{ all -> 0x0078 }
        nativeUpdateProcessInfo(r1, r3);	 Catch:{ all -> 0x0078 }
        r1 = android.os.statistics.OsUtils.isIsolatedProcess();	 Catch:{ all -> 0x0078 }
        if (r1 != 0) goto L_0x0074;
    L_0x0054:
        r1 = "com.miui.daemon";
        r3 = processName;	 Catch:{ all -> 0x0078 }
        r1 = android.text.TextUtils.equals(r1, r3);	 Catch:{ all -> 0x0078 }
        android.os.statistics.PerfSupervisionSettings.notifySupervisionReady(r4, r1);	 Catch:{ all -> 0x0078 }
        r3 = android.os.statistics.PerfSupervisionSettings.getSupervisionLevel();	 Catch:{ all -> 0x0078 }
        nativeStart(r4, r1, r3);	 Catch:{ all -> 0x0078 }
        r3 = android.os.statistics.PerfSupervisionSettings.isPerfEventReportable();	 Catch:{ all -> 0x0078 }
        if (r3 == 0) goto L_0x0074;
    L_0x006c:
        android.os.statistics.PerfEventReporter.start();	 Catch:{ all -> 0x0078 }
        if (r4 != 0) goto L_0x0074;
    L_0x0071:
        android.os.statistics.LooperCheckPointDetector.start();	 Catch:{ all -> 0x0078 }
    L_0x0074:
        sStarted = r2;	 Catch:{ all -> 0x0078 }
    L_0x0076:
        monitor-exit(r0);
        return;
    L_0x0078:
        r4 = move-exception;
        monitor-exit(r0);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.statistics.PerfSuperviser.start(boolean, java.lang.String):void");
    }

    public static void updateProcessInfo(String _processName, String _packageName) {
        if (sStarted) {
            processName = _processName;
            packageName = _packageName;
            nativeUpdateProcessInfo(processName, packageName);
        }
    }
}
