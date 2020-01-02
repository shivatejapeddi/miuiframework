package miui.log;

import android.os.FileObserver;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public final class LogSwitchesConfigMonitor {
    protected static final String TAG = "LogSwitchesConfigMonitor";
    private final LogSwitchesConfigApplier applier;
    private HashMap<String, AppLogSwitches> currentLogSwitchesConfig = new HashMap();
    private boolean isWatchingSwitches = false;
    private final String logSwitchesFileName;
    private final String logSwitchesFilePath;
    private final String logSwitchesFolder;
    private FileObserver logSwitchsObserver;

    public LogSwitchesConfigMonitor(String logSwitchesFolder, String logSwitchesFileName) {
        this.logSwitchesFolder = logSwitchesFolder;
        this.logSwitchesFileName = logSwitchesFileName;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(logSwitchesFolder);
        stringBuilder.append("/");
        stringBuilder.append(logSwitchesFileName);
        this.logSwitchesFilePath = stringBuilder.toString();
        this.applier = new LogSwitchesConfigApplier();
    }

    public synchronized void updatePackageName(String packageName) {
        this.applier.updatePackageName(packageName);
    }

    public synchronized void updateProgramName(String programName) {
        this.applier.updateProgramName(programName);
    }

    public synchronized void updateProgramName() {
        updateProgramName(getCurrentProgramName());
    }

    /* JADX WARNING: Missing block: B:17:0x0046, code skipped:
            return;
     */
    public synchronized void startMonitoring(boolean r4, boolean r5) {
        /*
        r3 = this;
        monitor-enter(r3);
        r0 = r3.isWatchingSwitches;	 Catch:{ all -> 0x0047 }
        if (r0 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r3);
        return;
    L_0x0007:
        if (r4 == 0) goto L_0x0010;
    L_0x0009:
        r0 = r3.logSwitchesFolder;	 Catch:{ all -> 0x0047 }
        r1 = r3.logSwitchesFilePath;	 Catch:{ all -> 0x0047 }
        miui.log.Utils.createLogSwitchesFileIfNotExisted(r0, r1);	 Catch:{ all -> 0x0047 }
    L_0x0010:
        r0 = r3.logSwitchsObserver;	 Catch:{ all -> 0x0047 }
        if (r0 != 0) goto L_0x001f;
    L_0x0014:
        r0 = new miui.log.LogSwitchesConfigMonitor$1;	 Catch:{ all -> 0x0047 }
        r1 = r3.logSwitchesFolder;	 Catch:{ all -> 0x0047 }
        r2 = 520; // 0x208 float:7.29E-43 double:2.57E-321;
        r0.<init>(r1, r2);	 Catch:{ all -> 0x0047 }
        r3.logSwitchsObserver = r0;	 Catch:{ all -> 0x0047 }
    L_0x001f:
        r0 = r3.logSwitchsObserver;	 Catch:{ all -> 0x0047 }
        r0.startWatching();	 Catch:{ all -> 0x0047 }
        r0 = 1;
        r3.isWatchingSwitches = r0;	 Catch:{ all -> 0x0047 }
        if (r5 == 0) goto L_0x0038;
    L_0x0029:
        r0 = "LogSwitchesConfigMonitor";
        r1 = "Read log switches for config file synchronously";
        android.util.Log.w(r0, r1);	 Catch:{ all -> 0x0047 }
        r0 = r3.applier;	 Catch:{ all -> 0x0047 }
        r1 = r3.logSwitchesFilePath;	 Catch:{ all -> 0x0047 }
        r0.apply(r1);	 Catch:{ all -> 0x0047 }
        goto L_0x0045;
    L_0x0038:
        r0 = new java.lang.Thread;	 Catch:{ all -> 0x0047 }
        r1 = new miui.log.LogSwitchesConfigMonitor$2;	 Catch:{ all -> 0x0047 }
        r1.<init>();	 Catch:{ all -> 0x0047 }
        r0.<init>(r1);	 Catch:{ all -> 0x0047 }
        r0.start();	 Catch:{ all -> 0x0047 }
    L_0x0045:
        monitor-exit(r3);
        return;
    L_0x0047:
        r4 = move-exception;
        monitor-exit(r3);
        throw r4;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.log.LogSwitchesConfigMonitor.startMonitoring(boolean, boolean):void");
    }

    public synchronized void stopMonitoring() {
        if (this.logSwitchsObserver != null) {
            if (this.isWatchingSwitches) {
                this.logSwitchsObserver.stopWatching();
                this.isWatchingSwitches = false;
            }
        }
    }

    public synchronized boolean isWatching() {
        return this.isWatchingSwitches;
    }

    public synchronized HashMap<String, AppLogSwitches> retrieveCurrentLogSwitches() {
        HashMap<String, AppLogSwitches> result;
        result = new HashMap();
        for (AppLogSwitches value : this.currentLogSwitchesConfig.values()) {
            AppLogSwitches clonedValue = (AppLogSwitches) value.clone();
            result.put(clonedValue.uniqueName, clonedValue);
        }
        return result;
    }

    private String getCurrentProgramName() {
        String str = TAG;
        String str2 = "";
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/self/cmdline"));
            String cmdline = reader.readLine();
            if (TextUtils.isEmpty(cmdline)) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
                return str2;
            }
            str = cmdline.split("\\u0000")[0];
            try {
                reader.close();
            } catch (IOException e2) {
            }
            return str;
        } catch (FileNotFoundException ex) {
            Log.e(str, "cannot found /proc/self/cmdline", ex);
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                }
            }
            return str2;
        } catch (IOException ex2) {
            Log.e(str, "failed to read /proc/self/cmdline", ex2);
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e4) {
                }
            }
            return str2;
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e5) {
                }
            }
        }
    }
}
