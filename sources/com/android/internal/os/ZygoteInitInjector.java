package com.android.internal.os;

import android.content.res.Resources;
import android.os.SystemClock;
import android.util.Log;
import miui.R;
import miui.util.ObjectReference;
import miui.util.ReflectionUtils;

class ZygoteInitInjector {
    private static final String PRELOADED_MIUI_CLASSES = "preloaded-miui-classes";
    private static final String PRELOADED_MIUI_CLASSES_FILE = "/system/etc/preloaded-miui-classes";
    private static final int ROOT_GID = 0;
    private static final int ROOT_UID = 0;
    private static final String SECOND_ZYGOTE_NAME = "zygote_secondary";
    private static final String TAG = "ZygoteInitInjector";
    private static final int UNPRIVILEGED_GID = 9999;
    private static final int UNPRIVILEGED_UID = 9999;
    private static String processName;

    ZygoteInitInjector() {
    }

    static void preloadMiuiResources(Resources resources) {
        boolean equals = "zygote_secondary".equals(processName);
        String str = TAG;
        if (equals) {
            Log.i(str, "skip the second zygote 32 preload miui resource");
            return;
        }
        long startTime = SystemClock.uptimeMillis();
        String str2 = "preloadDrawables";
        ObjectReference<Integer> result = ReflectionUtils.tryCallStaticMethod(ZygoteInit.class, str2, Integer.class, resources.obtainTypedArray(R.array.preloaded_drawables));
        int N = 0;
        if (result != null) {
            N = ((Integer) result.get()).intValue();
        }
        ar.recycle();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("...preloaded ");
        stringBuilder.append(N);
        stringBuilder.append(" miui sdk resources in ");
        stringBuilder.append(SystemClock.uptimeMillis() - startTime);
        stringBuilder.append("ms.");
        Log.i(str, stringBuilder.toString());
        preloadMiuiClasses();
    }

    /* JADX WARNING: Removed duplicated region for block: B:69:0x0187  */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x01ab  */
    private static void preloadMiuiClasses() {
        /*
        r1 = "PreloadDexCaches";
        r2 = "ZygoteInitInjector";
        r3 = dalvik.system.VMRuntime.getRuntime();
        r0 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x01bb }
        r4 = "/system/etc/preloaded-miui-classes";
        r0.<init>(r4);	 Catch:{ FileNotFoundException -> 0x01bb }
        r4 = r0;
        r0 = "Preloading classes...";
        android.util.Log.i(r2, r0);
        r5 = android.os.SystemClock.uptimeMillis();
        r7 = android.system.Os.getuid();
        r8 = android.system.Os.getgid();
        r9 = 0;
        r10 = "Failed to drop root";
        r11 = 0;
        if (r7 != 0) goto L_0x003c;
    L_0x0028:
        if (r8 != 0) goto L_0x003c;
    L_0x002a:
        r0 = 9999; // 0x270f float:1.4012E-41 double:4.94E-320;
        android.system.Os.setregid(r11, r0);	 Catch:{ ErrnoException -> 0x0035 }
        android.system.Os.setreuid(r11, r0);	 Catch:{ ErrnoException -> 0x0035 }
        r9 = 1;
        goto L_0x003c;
    L_0x0035:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r1.<init>(r10, r0);
        throw r1;
    L_0x003c:
        r12 = r3.getTargetHeapUtilization();
        r0 = 1061997773; // 0x3f4ccccd float:0.8 double:5.246966156E-315;
        r3.setTargetHeapUtilization(r0);
        r0 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        r15 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        r15.<init>(r4);	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        r13 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r0.<init>(r15, r13);	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        r13 = r0;
        r0 = r11;
        r14 = r0;
    L_0x0055:
        r0 = r13.readLine();	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        r15 = r0;
        if (r0 == 0) goto L_0x0116;
    L_0x005c:
        r0 = r15.trim();	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        r15 = r0;
        r0 = "#";
        r0 = r15.startsWith(r0);	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        if (r0 != 0) goto L_0x010d;
    L_0x0069:
        r0 = "";
        r0 = r15.equals(r0);	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        if (r0 == 0) goto L_0x0076;
    L_0x0071:
        r11 = r7;
        r16 = r8;
        goto L_0x0110;
    L_0x0076:
        r0 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        r0.<init>();	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        r11 = "PreloadClass ";
        r0.append(r11);	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        r0.append(r15);	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        r0 = r0.toString();	 Catch:{ IOException -> 0x016a, all -> 0x0165 }
        r11 = r7;
        r16 = r8;
        r7 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.os.Trace.traceBegin(r7, r0);	 Catch:{ IOException -> 0x0163 }
        r0 = 0;
        r7 = 1;
        java.lang.Class.forName(r15, r7, r0);	 Catch:{ ClassNotFoundException -> 0x00ea, UnsatisfiedLinkError -> 0x00c9, all -> 0x0097 }
        r14 = r14 + 1;
        goto L_0x0102;
    L_0x0097:
        r0 = move-exception;
        r7 = r0;
        r0 = r7;
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0163 }
        r7.<init>();	 Catch:{ IOException -> 0x0163 }
        r8 = "Error preloading ";
        r7.append(r8);	 Catch:{ IOException -> 0x0163 }
        r7.append(r15);	 Catch:{ IOException -> 0x0163 }
        r8 = ".";
        r7.append(r8);	 Catch:{ IOException -> 0x0163 }
        r7 = r7.toString();	 Catch:{ IOException -> 0x0163 }
        android.util.Log.e(r2, r7, r0);	 Catch:{ IOException -> 0x0163 }
        r7 = r0 instanceof java.lang.Error;	 Catch:{ IOException -> 0x0163 }
        if (r7 != 0) goto L_0x00c5;
    L_0x00b7:
        r7 = r0 instanceof java.lang.RuntimeException;	 Catch:{ IOException -> 0x0163 }
        if (r7 == 0) goto L_0x00bf;
    L_0x00bb:
        r7 = r0;
        r7 = (java.lang.RuntimeException) r7;	 Catch:{ IOException -> 0x0163 }
        throw r7;	 Catch:{ IOException -> 0x0163 }
    L_0x00bf:
        r7 = new java.lang.RuntimeException;	 Catch:{ IOException -> 0x0163 }
        r7.<init>(r0);	 Catch:{ IOException -> 0x0163 }
        throw r7;	 Catch:{ IOException -> 0x0163 }
    L_0x00c5:
        r7 = r0;
        r7 = (java.lang.Error) r7;	 Catch:{ IOException -> 0x0163 }
        throw r7;	 Catch:{ IOException -> 0x0163 }
    L_0x00c9:
        r0 = move-exception;
        r7 = r0;
        r0 = r7;
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0163 }
        r7.<init>();	 Catch:{ IOException -> 0x0163 }
        r8 = "Problem preloading ";
        r7.append(r8);	 Catch:{ IOException -> 0x0163 }
        r7.append(r15);	 Catch:{ IOException -> 0x0163 }
        r8 = ": ";
        r7.append(r8);	 Catch:{ IOException -> 0x0163 }
        r7.append(r0);	 Catch:{ IOException -> 0x0163 }
        r7 = r7.toString();	 Catch:{ IOException -> 0x0163 }
        android.util.Log.w(r2, r7);	 Catch:{ IOException -> 0x0163 }
        goto L_0x0102;
    L_0x00ea:
        r0 = move-exception;
        r7 = r0;
        r0 = r7;
        r7 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0163 }
        r7.<init>();	 Catch:{ IOException -> 0x0163 }
        r8 = "Class not found for preloading: ";
        r7.append(r8);	 Catch:{ IOException -> 0x0163 }
        r7.append(r15);	 Catch:{ IOException -> 0x0163 }
        r7 = r7.toString();	 Catch:{ IOException -> 0x0163 }
        android.util.Log.w(r2, r7);	 Catch:{ IOException -> 0x0163 }
    L_0x0102:
        r7 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.os.Trace.traceEnd(r7);	 Catch:{ IOException -> 0x0163 }
        r7 = r11;
        r8 = r16;
        r11 = 0;
        goto L_0x0055;
    L_0x010d:
        r11 = r7;
        r16 = r8;
    L_0x0110:
        r7 = r11;
        r8 = r16;
        r11 = 0;
        goto L_0x0055;
    L_0x0116:
        r11 = r7;
        r16 = r8;
        r0 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0163 }
        r0.<init>();	 Catch:{ IOException -> 0x0163 }
        r7 = "...preloaded ";
        r0.append(r7);	 Catch:{ IOException -> 0x0163 }
        r0.append(r14);	 Catch:{ IOException -> 0x0163 }
        r7 = " classes in ";
        r0.append(r7);	 Catch:{ IOException -> 0x0163 }
        r7 = android.os.SystemClock.uptimeMillis();	 Catch:{ IOException -> 0x0163 }
        r7 = r7 - r5;
        r0.append(r7);	 Catch:{ IOException -> 0x0163 }
        r7 = "ms.";
        r0.append(r7);	 Catch:{ IOException -> 0x0163 }
        r0 = r0.toString();	 Catch:{ IOException -> 0x0163 }
        android.util.Log.i(r2, r0);	 Catch:{ IOException -> 0x0163 }
        libcore.io.IoUtils.closeQuietly(r4);
        r3.setTargetHeapUtilization(r12);
        r7 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.os.Trace.traceBegin(r7, r1);
        r3.preloadDexCaches();
        android.os.Trace.traceEnd(r7);
        if (r9 == 0) goto L_0x0196;
    L_0x0154:
        r1 = 0;
        android.system.Os.setreuid(r1, r1);	 Catch:{ ErrnoException -> 0x015c }
        android.system.Os.setregid(r1, r1);	 Catch:{ ErrnoException -> 0x015c }
        goto L_0x018e;
    L_0x015c:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r1.<init>(r10, r0);
        throw r1;
    L_0x0163:
        r0 = move-exception;
        goto L_0x016e;
    L_0x0165:
        r0 = move-exception;
        r11 = r7;
        r16 = r8;
        goto L_0x0198;
    L_0x016a:
        r0 = move-exception;
        r11 = r7;
        r16 = r8;
    L_0x016e:
        r7 = "Error reading /system/etc/preloaded-miui-classes.";
        android.util.Log.e(r2, r7, r0);	 Catch:{ all -> 0x0197 }
        libcore.io.IoUtils.closeQuietly(r4);
        r3.setTargetHeapUtilization(r12);
        r7 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.os.Trace.traceBegin(r7, r1);
        r3.preloadDexCaches();
        android.os.Trace.traceEnd(r7);
        if (r9 == 0) goto L_0x0196;
    L_0x0187:
        r1 = 0;
        android.system.Os.setreuid(r1, r1);	 Catch:{ ErrnoException -> 0x018f }
        android.system.Os.setregid(r1, r1);	 Catch:{ ErrnoException -> 0x018f }
    L_0x018e:
        goto L_0x0196;
    L_0x018f:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r1.<init>(r10, r0);
        throw r1;
    L_0x0196:
        return;
    L_0x0197:
        r0 = move-exception;
    L_0x0198:
        libcore.io.IoUtils.closeQuietly(r4);
        r3.setTargetHeapUtilization(r12);
        r7 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.os.Trace.traceBegin(r7, r1);
        r3.preloadDexCaches();
        android.os.Trace.traceEnd(r7);
        if (r9 == 0) goto L_0x01ba;
    L_0x01ab:
        r1 = 0;
        android.system.Os.setreuid(r1, r1);	 Catch:{ ErrnoException -> 0x01b3 }
        android.system.Os.setregid(r1, r1);	 Catch:{ ErrnoException -> 0x01b3 }
        goto L_0x01ba;
    L_0x01b3:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r1.<init>(r10, r0);
        throw r1;
    L_0x01ba:
        throw r0;
    L_0x01bb:
        r0 = move-exception;
        r1 = "Couldn't find /system/etc/preloaded-miui-classes.";
        android.util.Log.e(r2, r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteInitInjector.preloadMiuiClasses():void");
    }
}
