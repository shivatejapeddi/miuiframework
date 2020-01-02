package com.android.internal.os;

import android.util.ArrayMap;

public class KernelSysAppCpuTimeReader {
    static final boolean DEBUG = false;
    private static final long DEFAULT_THROTTLE_INTERVAL = 10000;
    private static final String PROC_FILE = "/proc/uid_cputime/show_sys_app_stat";
    private static final String TAG = "KernelSysAppCpuTimeReader";
    private ArrayMap<String, Long> mLastPackageSystemTimeUs = new ArrayMap();
    private ArrayMap<String, Long> mLastPackageUserTimeUs = new ArrayMap();
    private long mLastTimeReadMs = Long.MIN_VALUE;
    private long mLastTimeReadUs = 0;
    private long mThrottleInterval = 10000;

    public interface Callback {
        void onPackageCpuTime(String str, long j, long j2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:43:0x0182 A:{Catch:{ all -> 0x0193 }} */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x0182 A:{Catch:{ all -> 0x0193 }} */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:48:0x019c, B:52:0x01a4, B:56:0x01a7] */
    /* JADX WARNING: Missing block: B:54:0x01a5, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:55:0x01a6, code skipped:
            r4 = r0;
     */
    /* JADX WARNING: Missing block: B:57:?, code skipped:
            r8.close();
     */
    /* JADX WARNING: Missing block: B:58:0x01ab, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:61:?, code skipped:
            r3.addSuppressed(r0);
     */
    /* JADX WARNING: Missing block: B:63:0x01b1, code skipped:
            r0 = th;
     */
    /* JADX WARNING: Missing block: B:64:0x01b2, code skipped:
            r3 = r28;
     */
    /* JADX WARNING: Missing block: B:65:0x01b5, code skipped:
            r0 = e;
     */
    public void readDelta(com.android.internal.os.KernelSysAppCpuTimeReader.Callback r33) {
        /*
        r32 = this;
        r1 = r32;
        r2 = "KernelSysAppCpuTimeReader";
        r0 = " s=";
        r3 = android.os.SystemClock.elapsedRealtime();
        r5 = r1.mLastTimeReadMs;
        r7 = r1.mThrottleInterval;
        r5 = r5 + r7;
        r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1));
        if (r3 >= 0) goto L_0x0014;
    L_0x0013:
        return;
    L_0x0014:
        r3 = android.os.SystemClock.elapsedRealtime();
        r5 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r3 = r3 * r5;
        r7 = android.os.StrictMode.allowThreadDiskReadsMask();
        r8 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x01b9, all -> 0x01b7 }
        r9 = new java.io.FileReader;	 Catch:{ IOException -> 0x01b9, all -> 0x01b7 }
        r10 = "/proc/uid_cputime/show_sys_app_stat";
        r9.<init>(r10);	 Catch:{ IOException -> 0x01b9, all -> 0x01b7 }
        r8.<init>(r9);	 Catch:{ IOException -> 0x01b9, all -> 0x01b7 }
        r9 = new android.text.TextUtils$SimpleStringSplitter;	 Catch:{ all -> 0x01a0 }
        r10 = 32;
        r9.<init>(r10);	 Catch:{ all -> 0x01a0 }
    L_0x0032:
        r10 = r8.readLine();	 Catch:{ all -> 0x01a0 }
        r11 = r10;
        if (r10 == 0) goto L_0x0196;
    L_0x0039:
        r10 = "total";
        r10 = r11.startsWith(r10);	 Catch:{ all -> 0x01a0 }
        if (r10 == 0) goto L_0x0043;
    L_0x0042:
        goto L_0x0032;
    L_0x0043:
        r9.setString(r11);	 Catch:{ all -> 0x01a0 }
        r10 = r9.next();	 Catch:{ all -> 0x01a0 }
        r12 = r9.next();	 Catch:{ all -> 0x01a0 }
        r13 = 10;
        r14 = java.lang.Long.parseLong(r12, r13);	 Catch:{ all -> 0x01a0 }
        r18 = r14;
        r12 = r9.next();	 Catch:{ all -> 0x01a0 }
        r12 = java.lang.Long.parseLong(r12, r13);	 Catch:{ all -> 0x01a0 }
        r20 = r12;
        r12 = 0;
        r13 = r18;
        r15 = r20;
        if (r33 == 0) goto L_0x015f;
    L_0x0067:
        r5 = r1.mLastTimeReadUs;	 Catch:{ all -> 0x01a0 }
        r24 = 0;
        r5 = (r5 > r24 ? 1 : (r5 == r24 ? 0 : -1));
        if (r5 == 0) goto L_0x0154;
    L_0x006f:
        r5 = r1.mLastPackageUserTimeUs;	 Catch:{ all -> 0x01a0 }
        r5 = r5.indexOfKey(r10);	 Catch:{ all -> 0x01a0 }
        if (r5 < 0) goto L_0x0137;
    L_0x0077:
        r6 = r1.mLastPackageUserTimeUs;	 Catch:{ all -> 0x01a0 }
        r6 = r6.valueAt(r5);	 Catch:{ all -> 0x01a0 }
        r6 = (java.lang.Long) r6;	 Catch:{ all -> 0x01a0 }
        r26 = r6.longValue();	 Catch:{ all -> 0x01a0 }
        r13 = r13 - r26;
        r6 = r1.mLastPackageSystemTimeUs;	 Catch:{ all -> 0x01a0 }
        r6 = r6.valueAt(r5);	 Catch:{ all -> 0x01a0 }
        r6 = (java.lang.Long) r6;	 Catch:{ all -> 0x01a0 }
        r26 = r6.longValue();	 Catch:{ all -> 0x01a0 }
        r15 = r15 - r26;
        r6 = r11;
        r17 = r12;
        r11 = r1.mLastTimeReadUs;	 Catch:{ all -> 0x01a0 }
        r11 = r3 - r11;
        r26 = (r13 > r24 ? 1 : (r13 == r24 ? 0 : -1));
        if (r26 < 0) goto L_0x00ad;
    L_0x009e:
        r26 = (r15 > r24 ? 1 : (r15 == r24 ? 0 : -1));
        if (r26 >= 0) goto L_0x00a3;
    L_0x00a2:
        goto L_0x00ad;
    L_0x00a3:
        r28 = r3;
        r26 = r6;
        r27 = r9;
        r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        goto L_0x0141;
    L_0x00ad:
        r26 = r6;
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01a0 }
        r27 = r9;
        r9 = "Malformed cpu data for Package=";
        r6.<init>(r9);	 Catch:{ all -> 0x01a0 }
        r6.append(r10);	 Catch:{ all -> 0x01a0 }
        r9 = "!\n";
        r6.append(r9);	 Catch:{ all -> 0x01a0 }
        r9 = "Time between reads: ";
        r6.append(r9);	 Catch:{ all -> 0x01a0 }
        r28 = r3;
        r22 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r3 = r11 / r22;
        android.util.TimeUtils.formatDuration(r3, r6);	 Catch:{ all -> 0x0193 }
        r3 = "\n";
        r6.append(r3);	 Catch:{ all -> 0x0193 }
        r3 = "Previous times: u=";
        r6.append(r3);	 Catch:{ all -> 0x0193 }
        r3 = r1.mLastPackageUserTimeUs;	 Catch:{ all -> 0x0193 }
        r3 = r3.valueAt(r5);	 Catch:{ all -> 0x0193 }
        r3 = (java.lang.Long) r3;	 Catch:{ all -> 0x0193 }
        r3 = r3.longValue();	 Catch:{ all -> 0x0193 }
        r22 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r3 = r3 / r22;
        android.util.TimeUtils.formatDuration(r3, r6);	 Catch:{ all -> 0x0193 }
        r6.append(r0);	 Catch:{ all -> 0x0193 }
        r3 = r1.mLastPackageSystemTimeUs;	 Catch:{ all -> 0x0193 }
        r3 = r3.valueAt(r5);	 Catch:{ all -> 0x0193 }
        r3 = (java.lang.Long) r3;	 Catch:{ all -> 0x0193 }
        r3 = r3.longValue();	 Catch:{ all -> 0x0193 }
        r22 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r3 = r3 / r22;
        android.util.TimeUtils.formatDuration(r3, r6);	 Catch:{ all -> 0x0193 }
        r3 = "\nCurrent times: u=";
        r6.append(r3);	 Catch:{ all -> 0x0193 }
        r30 = r11;
        r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r11 = r18 / r3;
        android.util.TimeUtils.formatDuration(r11, r6);	 Catch:{ all -> 0x0193 }
        r6.append(r0);	 Catch:{ all -> 0x0193 }
        r11 = r20 / r3;
        android.util.TimeUtils.formatDuration(r11, r6);	 Catch:{ all -> 0x0193 }
        r3 = "\nDelta: u=";
        r6.append(r3);	 Catch:{ all -> 0x0193 }
        r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        r11 = r13 / r3;
        android.util.TimeUtils.formatDuration(r11, r6);	 Catch:{ all -> 0x0193 }
        r6.append(r0);	 Catch:{ all -> 0x0193 }
        r11 = r15 / r3;
        android.util.TimeUtils.formatDuration(r11, r6);	 Catch:{ all -> 0x0193 }
        r9 = r6.toString();	 Catch:{ all -> 0x0193 }
        android.util.Slog.e(r2, r9);	 Catch:{ all -> 0x0193 }
        r13 = 0;
        r15 = 0;
        goto L_0x0141;
    L_0x0137:
        r28 = r3;
        r27 = r9;
        r26 = r11;
        r17 = r12;
        r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
    L_0x0141:
        r6 = (r13 > r24 ? 1 : (r13 == r24 ? 0 : -1));
        if (r6 != 0) goto L_0x014c;
    L_0x0145:
        r6 = (r15 > r24 ? 1 : (r15 == r24 ? 0 : -1));
        if (r6 == 0) goto L_0x014a;
    L_0x0149:
        goto L_0x014c;
    L_0x014a:
        r6 = 0;
        goto L_0x014d;
    L_0x014c:
        r6 = 1;
    L_0x014d:
        r12 = r6;
        r5 = r12;
        r22 = r13;
        r24 = r15;
        goto L_0x016e;
    L_0x0154:
        r28 = r3;
        r27 = r9;
        r26 = r11;
        r17 = r12;
        r3 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        goto L_0x0168;
    L_0x015f:
        r28 = r3;
        r3 = r5;
        r27 = r9;
        r26 = r11;
        r17 = r12;
    L_0x0168:
        r22 = r13;
        r24 = r15;
        r5 = r17;
    L_0x016e:
        r6 = r1.mLastPackageUserTimeUs;	 Catch:{ all -> 0x0193 }
        r9 = java.lang.Long.valueOf(r18);	 Catch:{ all -> 0x0193 }
        r6.put(r10, r9);	 Catch:{ all -> 0x0193 }
        r6 = r1.mLastPackageSystemTimeUs;	 Catch:{ all -> 0x0193 }
        r9 = java.lang.Long.valueOf(r20);	 Catch:{ all -> 0x0193 }
        r6.put(r10, r9);	 Catch:{ all -> 0x0193 }
        if (r5 == 0) goto L_0x018c;
    L_0x0182:
        r12 = r33;
        r13 = r10;
        r14 = r22;
        r16 = r24;
        r12.onPackageCpuTime(r13, r14, r16);	 Catch:{ all -> 0x0193 }
    L_0x018c:
        r5 = r3;
        r9 = r27;
        r3 = r28;
        goto L_0x0032;
    L_0x0193:
        r0 = move-exception;
        r3 = r0;
        goto L_0x01a4;
    L_0x0196:
        r28 = r3;
        r27 = r9;
        r26 = r11;
        r8.close();	 Catch:{ IOException -> 0x01b5, all -> 0x01b1 }
        goto L_0x01d5;
    L_0x01a0:
        r0 = move-exception;
        r28 = r3;
        r3 = r0;
    L_0x01a4:
        throw r3;	 Catch:{ all -> 0x01a5 }
    L_0x01a5:
        r0 = move-exception;
        r4 = r0;
        r8.close();	 Catch:{ all -> 0x01ab }
        goto L_0x01b0;
    L_0x01ab:
        r0 = move-exception;
        r5 = r0;
        r3.addSuppressed(r5);	 Catch:{ IOException -> 0x01b5, all -> 0x01b1 }
    L_0x01b0:
        throw r4;	 Catch:{ IOException -> 0x01b5, all -> 0x01b1 }
    L_0x01b1:
        r0 = move-exception;
        r3 = r28;
        goto L_0x01e1;
    L_0x01b5:
        r0 = move-exception;
        goto L_0x01bc;
    L_0x01b7:
        r0 = move-exception;
        goto L_0x01e1;
    L_0x01b9:
        r0 = move-exception;
        r28 = r3;
    L_0x01bc:
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x01de }
        r3.<init>();	 Catch:{ all -> 0x01de }
        r4 = "Failed to read uid_cputime: ";
        r3.append(r4);	 Catch:{ all -> 0x01de }
        r4 = r0.getMessage();	 Catch:{ all -> 0x01de }
        r3.append(r4);	 Catch:{ all -> 0x01de }
        r3 = r3.toString();	 Catch:{ all -> 0x01de }
        android.util.Slog.e(r2, r3);	 Catch:{ all -> 0x01de }
    L_0x01d5:
        android.os.StrictMode.setThreadPolicyMask(r7);
        r3 = r28;
        r1.mLastTimeReadUs = r3;
        return;
    L_0x01de:
        r0 = move-exception;
        r3 = r28;
    L_0x01e1:
        android.os.StrictMode.setThreadPolicyMask(r7);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelSysAppCpuTimeReader.readDelta(com.android.internal.os.KernelSysAppCpuTimeReader$Callback):void");
    }
}
