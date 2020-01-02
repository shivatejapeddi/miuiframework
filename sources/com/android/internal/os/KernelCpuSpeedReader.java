package com.android.internal.os;

import android.system.Os;
import android.system.OsConstants;

public class KernelCpuSpeedReader {
    private static final String TAG = "KernelCpuSpeedReader";
    private final long[] mDeltaSpeedTimesMs;
    private final long mJiffyMillis = (1000 / Os.sysconf(OsConstants._SC_CLK_TCK));
    private final long[] mLastSpeedTimesMs;
    private final int mNumSpeedSteps;
    private final String mProcFile;

    public KernelCpuSpeedReader(int cpuNumber, int numSpeedSteps) {
        this.mProcFile = String.format("/sys/devices/system/cpu/cpu%d/cpufreq/stats/time_in_state", new Object[]{Integer.valueOf(cpuNumber)});
        this.mNumSpeedSteps = numSpeedSteps;
        this.mLastSpeedTimesMs = new long[numSpeedSteps];
        this.mDeltaSpeedTimesMs = new long[numSpeedSteps];
    }

    /* JADX WARNING: Missing block: B:23:?, code skipped:
            $closeResource(r2, r1);
     */
    public long[] readDelta() {
        /*
        r11 = this;
        r0 = android.os.StrictMode.allowThreadDiskReads();
        r1 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x0062 }
        r2 = new java.io.FileReader;	 Catch:{ IOException -> 0x0062 }
        r3 = r11.mProcFile;	 Catch:{ IOException -> 0x0062 }
        r2.<init>(r3);	 Catch:{ IOException -> 0x0062 }
        r1.<init>(r2);	 Catch:{ IOException -> 0x0062 }
        r2 = 0;
        r3 = new android.text.TextUtils$SimpleStringSplitter;	 Catch:{ all -> 0x0059 }
        r4 = 32;
        r3.<init>(r4);	 Catch:{ all -> 0x0059 }
        r4 = 0;
    L_0x0019:
        r5 = r11.mLastSpeedTimesMs;	 Catch:{ all -> 0x0059 }
        r5 = r5.length;	 Catch:{ all -> 0x0059 }
        if (r4 >= r5) goto L_0x0055;
    L_0x001e:
        r5 = r1.readLine();	 Catch:{ all -> 0x0059 }
        r6 = r5;
        if (r5 == 0) goto L_0x0055;
    L_0x0025:
        r3.setString(r6);	 Catch:{ all -> 0x0059 }
        r3.next();	 Catch:{ all -> 0x0059 }
        r5 = r3.next();	 Catch:{ all -> 0x0059 }
        r7 = java.lang.Long.parseLong(r5);	 Catch:{ all -> 0x0059 }
        r9 = r11.mJiffyMillis;	 Catch:{ all -> 0x0059 }
        r7 = r7 * r9;
        r5 = r11.mLastSpeedTimesMs;	 Catch:{ all -> 0x0059 }
        r9 = r5[r4];	 Catch:{ all -> 0x0059 }
        r5 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r5 >= 0) goto L_0x0043;
    L_0x003e:
        r5 = r11.mDeltaSpeedTimesMs;	 Catch:{ all -> 0x0059 }
        r5[r4] = r7;	 Catch:{ all -> 0x0059 }
        goto L_0x004d;
    L_0x0043:
        r5 = r11.mDeltaSpeedTimesMs;	 Catch:{ all -> 0x0059 }
        r9 = r11.mLastSpeedTimesMs;	 Catch:{ all -> 0x0059 }
        r9 = r9[r4];	 Catch:{ all -> 0x0059 }
        r9 = r7 - r9;
        r5[r4] = r9;	 Catch:{ all -> 0x0059 }
    L_0x004d:
        r5 = r11.mLastSpeedTimesMs;	 Catch:{ all -> 0x0059 }
        r5[r4] = r7;	 Catch:{ all -> 0x0059 }
        r4 = r4 + 1;
        goto L_0x0019;
    L_0x0055:
        $closeResource(r2, r1);	 Catch:{ IOException -> 0x0062 }
        goto L_0x0084;
    L_0x0059:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x005b }
    L_0x005b:
        r3 = move-exception;
        $closeResource(r2, r1);	 Catch:{ IOException -> 0x0062 }
        throw r3;	 Catch:{ IOException -> 0x0062 }
    L_0x0060:
        r1 = move-exception;
        goto L_0x008b;
    L_0x0062:
        r1 = move-exception;
        r2 = "KernelCpuSpeedReader";
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0060 }
        r3.<init>();	 Catch:{ all -> 0x0060 }
        r4 = "Failed to read cpu-freq: ";
        r3.append(r4);	 Catch:{ all -> 0x0060 }
        r4 = r1.getMessage();	 Catch:{ all -> 0x0060 }
        r3.append(r4);	 Catch:{ all -> 0x0060 }
        r3 = r3.toString();	 Catch:{ all -> 0x0060 }
        android.util.Slog.e(r2, r3);	 Catch:{ all -> 0x0060 }
        r2 = r11.mDeltaSpeedTimesMs;	 Catch:{ all -> 0x0060 }
        r3 = 0;
        java.util.Arrays.fill(r2, r3);	 Catch:{ all -> 0x0060 }
    L_0x0084:
        android.os.StrictMode.setThreadPolicy(r0);
        r1 = r11.mDeltaSpeedTimesMs;
        return r1;
    L_0x008b:
        android.os.StrictMode.setThreadPolicy(r0);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuSpeedReader.readDelta():long[]");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    /* JADX WARNING: Missing block: B:19:?, code skipped:
            $closeResource(r3, r2);
     */
    public long[] readAbsolute() {
        /*
        r12 = this;
        r0 = android.os.StrictMode.allowThreadDiskReads();
        r1 = r12.mNumSpeedSteps;
        r1 = new long[r1];
        r2 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x004c }
        r3 = new java.io.FileReader;	 Catch:{ IOException -> 0x004c }
        r4 = r12.mProcFile;	 Catch:{ IOException -> 0x004c }
        r3.<init>(r4);	 Catch:{ IOException -> 0x004c }
        r2.<init>(r3);	 Catch:{ IOException -> 0x004c }
        r3 = 0;
        r4 = new android.text.TextUtils$SimpleStringSplitter;	 Catch:{ all -> 0x0043 }
        r5 = 32;
        r4.<init>(r5);	 Catch:{ all -> 0x0043 }
        r5 = 0;
    L_0x001d:
        r6 = r12.mNumSpeedSteps;	 Catch:{ all -> 0x0043 }
        if (r5 >= r6) goto L_0x003f;
    L_0x0021:
        r6 = r2.readLine();	 Catch:{ all -> 0x0043 }
        r7 = r6;
        if (r6 == 0) goto L_0x003f;
    L_0x0028:
        r4.setString(r7);	 Catch:{ all -> 0x0043 }
        r4.next();	 Catch:{ all -> 0x0043 }
        r6 = r4.next();	 Catch:{ all -> 0x0043 }
        r8 = java.lang.Long.parseLong(r6);	 Catch:{ all -> 0x0043 }
        r10 = r12.mJiffyMillis;	 Catch:{ all -> 0x0043 }
        r8 = r8 * r10;
        r1[r5] = r8;	 Catch:{ all -> 0x0043 }
        r5 = r5 + 1;
        goto L_0x001d;
    L_0x003f:
        $closeResource(r3, r2);	 Catch:{ IOException -> 0x004c }
        goto L_0x006c;
    L_0x0043:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0045 }
    L_0x0045:
        r4 = move-exception;
        $closeResource(r3, r2);	 Catch:{ IOException -> 0x004c }
        throw r4;	 Catch:{ IOException -> 0x004c }
    L_0x004a:
        r2 = move-exception;
        goto L_0x0071;
    L_0x004c:
        r2 = move-exception;
        r3 = "KernelCpuSpeedReader";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x004a }
        r4.<init>();	 Catch:{ all -> 0x004a }
        r5 = "Failed to read cpu-freq: ";
        r4.append(r5);	 Catch:{ all -> 0x004a }
        r5 = r2.getMessage();	 Catch:{ all -> 0x004a }
        r4.append(r5);	 Catch:{ all -> 0x004a }
        r4 = r4.toString();	 Catch:{ all -> 0x004a }
        android.util.Slog.e(r3, r4);	 Catch:{ all -> 0x004a }
        r3 = 0;
        java.util.Arrays.fill(r1, r3);	 Catch:{ all -> 0x004a }
    L_0x006c:
        android.os.StrictMode.setThreadPolicy(r0);
        return r1;
    L_0x0071:
        android.os.StrictMode.setThreadPolicy(r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuSpeedReader.readAbsolute():long[]");
    }
}
