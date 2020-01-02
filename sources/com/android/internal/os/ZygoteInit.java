package com.android.internal.os;

import android.annotation.UnsupportedAppUsage;
import android.app.ApplicationLoaders;
import android.content.pm.SharedLibraryInfo;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Environment;
import android.os.IInstalld;
import android.os.IInstalld.Stub;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceSpecificException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.os.ZygoteProcess;
import android.os.storage.StorageManager;
import android.security.keystore.AndroidKeyStoreProvider;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructCapUserData;
import android.system.StructCapUserHeader;
import android.text.Hyphenator;
import android.util.EventLog;
import android.util.Log;
import android.util.TimingsTraceLog;
import android.webkit.WebViewFactory;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.util.Preconditions;
import dalvik.system.DexFile;
import dalvik.system.VMRuntime;
import dalvik.system.VMRuntime.HiddenApiUsageLogger;
import dalvik.system.ZygoteHooks;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Provider;
import java.security.Security;
import miui.security.SecurityManager;

public class ZygoteInit {
    private static final String ABI_LIST_ARG = "--abi-list=";
    public static long BOOT_START_TIME = 0;
    private static final int LOG_BOOT_PROGRESS_PRELOAD_END = 3030;
    private static final int LOG_BOOT_PROGRESS_PRELOAD_START = 3020;
    private static final String PRELOADED_CLASSES = "/system/etc/preloaded-classes";
    private static final int PRELOAD_GC_THRESHOLD = 50000;
    public static final boolean PRELOAD_RESOURCES = true;
    private static final String PROPERTY_DISABLE_GRAPHICS_DRIVER_PRELOADING = "ro.zygote.disable_gl_preload";
    private static final int ROOT_GID = 0;
    private static final int ROOT_UID = 0;
    private static final String SOCKET_NAME_ARG = "--socket-name=";
    private static final String TAG = "Zygote";
    private static final int UNPRIVILEGED_GID = 9999;
    private static final int UNPRIVILEGED_UID = 9999;
    @UnsupportedAppUsage
    private static Resources mResources;
    private static ClassLoader sCachedSystemServerClassLoader = null;
    private static boolean sPreloadComplete;

    private static native void nativePreloadAppProcessHALs();

    static native void nativePreloadGraphicsDriver();

    private static final native void nativeZygoteInit();

    static void preload(TimingsTraceLog bootTimingsTraceLog) {
        String str = TAG;
        Log.d(str, "begin preload");
        bootTimingsTraceLog.traceBegin("BeginPreload");
        beginPreload();
        bootTimingsTraceLog.traceEnd();
        bootTimingsTraceLog.traceBegin("PreloadClasses");
        preloadClasses();
        bootTimingsTraceLog.traceEnd();
        bootTimingsTraceLog.traceBegin("CacheNonBootClasspathClassLoaders");
        cacheNonBootClasspathClassLoaders();
        bootTimingsTraceLog.traceEnd();
        bootTimingsTraceLog.traceBegin("PreloadResources");
        preloadResources();
        bootTimingsTraceLog.traceEnd();
        Trace.traceBegin(16384, "PreloadAppProcessHALs");
        nativePreloadAppProcessHALs();
        Trace.traceEnd(16384);
        Trace.traceBegin(16384, "PreloadGraphicsDriver");
        maybePreloadGraphicsDriver();
        Trace.traceEnd(16384);
        preloadSharedLibraries();
        preloadTextResources();
        WebViewFactory.prepareWebViewInZygote();
        endPreload();
        warmUpJcaProviders();
        Log.d(str, "end preload");
        sPreloadComplete = true;
    }

    public static void lazyPreload() {
        Preconditions.checkState(sPreloadComplete ^ 1);
        Log.i(TAG, "Lazily preloading resources.");
        preload(new TimingsTraceLog("ZygoteInitTiming_lazy", 16384));
    }

    private static void beginPreload() {
        Log.i(TAG, "Calling ZygoteHooks.beginPreload()");
        ZygoteHooks.onBeginPreload();
    }

    private static void endPreload() {
        ZygoteHooks.onEndPreload();
        Log.i(TAG, "Called ZygoteHooks.endPreload()");
    }

    private static void preloadSharedLibraries() {
        String str = TAG;
        Log.i(str, "Preloading shared libraries...");
        System.loadLibrary("android");
        System.loadLibrary("compiler_rt");
        System.loadLibrary("jnigraphics");
        try {
            System.loadLibrary("qti_performance");
        } catch (UnsatisfiedLinkError e) {
            Log.e(str, "Couldn't load qti_performance");
        }
    }

    private static void maybePreloadGraphicsDriver() {
        if (!SystemProperties.getBoolean(PROPERTY_DISABLE_GRAPHICS_DRIVER_PRELOADING, false)) {
            nativePreloadGraphicsDriver();
        }
    }

    private static void preloadTextResources() {
        Hyphenator.init();
        TextView.preloadFontCache();
    }

    private static void warmUpJcaProviders() {
        long startTime = SystemClock.uptimeMillis();
        Trace.traceBegin(16384, "Starting installation of AndroidKeyStoreProvider");
        AndroidKeyStoreProvider.install();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Installed AndroidKeyStoreProvider in ");
        stringBuilder.append(SystemClock.uptimeMillis() - startTime);
        String str = "ms.";
        stringBuilder.append(str);
        String stringBuilder2 = stringBuilder.toString();
        String str2 = TAG;
        Log.i(str2, stringBuilder2);
        Trace.traceEnd(16384);
        startTime = SystemClock.uptimeMillis();
        Trace.traceBegin(16384, "Starting warm up of JCA providers");
        for (Provider p : Security.getProviders()) {
            p.warmUpServiceProvision();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Warmed up JCA providers in ");
        stringBuilder.append(SystemClock.uptimeMillis() - startTime);
        stringBuilder.append(str);
        Log.i(str2, stringBuilder.toString());
        Trace.traceEnd(16384);
    }

    /* JADX WARNING: Removed duplicated region for block: B:92:0x01ab  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0187  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x01ab  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0187  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x01ab  */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0187  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x01ab  */
    private static void preloadClasses() {
        /*
        r1 = "Failed to restore root";
        r2 = "PreloadDexCaches";
        r3 = "Zygote";
        r4 = dalvik.system.VMRuntime.getRuntime();
        r0 = new java.io.FileInputStream;	 Catch:{ FileNotFoundException -> 0x01bb }
        r5 = "/system/etc/preloaded-classes";
        r0.<init>(r5);	 Catch:{ FileNotFoundException -> 0x01bb }
        r5 = r0;
        r0 = "Preloading classes...";
        android.util.Log.i(r3, r0);
        r6 = android.os.SystemClock.uptimeMillis();
        r8 = android.system.Os.getuid();
        r9 = android.system.Os.getgid();
        r10 = 0;
        r11 = 0;
        if (r8 != 0) goto L_0x003e;
    L_0x0028:
        if (r9 != 0) goto L_0x003e;
    L_0x002a:
        r0 = 9999; // 0x270f float:1.4012E-41 double:4.94E-320;
        android.system.Os.setregid(r11, r0);	 Catch:{ ErrnoException -> 0x0035 }
        android.system.Os.setreuid(r11, r0);	 Catch:{ ErrnoException -> 0x0035 }
        r10 = 1;
        goto L_0x003e;
    L_0x0035:
        r0 = move-exception;
        r1 = new java.lang.RuntimeException;
        r2 = "Failed to drop root";
        r1.<init>(r2, r0);
        throw r1;
    L_0x003e:
        r12 = r4.getTargetHeapUtilization();
        r0 = 1061997773; // 0x3f4ccccd float:0.8 double:5.246966156E-315;
        r4.setTargetHeapUtilization(r0);
        r0 = new java.io.BufferedReader;	 Catch:{ IOException -> 0x016c, all -> 0x0169 }
        r15 = new java.io.InputStreamReader;	 Catch:{ IOException -> 0x016c, all -> 0x0169 }
        r15.<init>(r5);	 Catch:{ IOException -> 0x016c, all -> 0x0169 }
        r13 = 256; // 0x100 float:3.59E-43 double:1.265E-321;
        r0.<init>(r15, r13);	 Catch:{ IOException -> 0x016c, all -> 0x0169 }
        r13 = r0;
        r0 = r11;
        r14 = r0;
    L_0x0057:
        r0 = r13.readLine();	 Catch:{ IOException -> 0x016c, all -> 0x0169 }
        r15 = r0;
        if (r0 == 0) goto L_0x0113;
    L_0x005e:
        r0 = r15.trim();	 Catch:{ IOException -> 0x0110, all -> 0x010c }
        r15 = r0;
        r0 = "#";
        r0 = r15.startsWith(r0);	 Catch:{ IOException -> 0x0110, all -> 0x010c }
        if (r0 != 0) goto L_0x0105;
    L_0x006b:
        r0 = "";
        r0 = r15.equals(r0);	 Catch:{ IOException -> 0x0110, all -> 0x010c }
        if (r0 == 0) goto L_0x0077;
    L_0x0073:
        r16 = r12;
        goto L_0x0107;
    L_0x0077:
        r16 = r12;
        r11 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.os.Trace.traceBegin(r11, r15);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r0 = 0;
        r11 = 1;
        java.lang.Class.forName(r15, r11, r0);	 Catch:{ ClassNotFoundException -> 0x00d9, UnsatisfiedLinkError -> 0x00b8, all -> 0x0086 }
        r14 = r14 + 1;
        goto L_0x00f1;
    L_0x0086:
        r0 = move-exception;
        r11 = r0;
        r0 = r11;
        r11 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11.<init>();	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r12 = "Error preloading ";
        r11.append(r12);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11.append(r15);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r12 = ".";
        r11.append(r12);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11 = r11.toString();	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        android.util.Log.e(r3, r11, r0);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11 = r0 instanceof java.lang.Error;	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        if (r11 != 0) goto L_0x00b4;
    L_0x00a6:
        r11 = r0 instanceof java.lang.RuntimeException;	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        if (r11 == 0) goto L_0x00ae;
    L_0x00aa:
        r11 = r0;
        r11 = (java.lang.RuntimeException) r11;	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        throw r11;	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
    L_0x00ae:
        r11 = new java.lang.RuntimeException;	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11.<init>(r0);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        throw r11;	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
    L_0x00b4:
        r11 = r0;
        r11 = (java.lang.Error) r11;	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        throw r11;	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
    L_0x00b8:
        r0 = move-exception;
        r11 = r0;
        r0 = r11;
        r11 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11.<init>();	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r12 = "Problem preloading ";
        r11.append(r12);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11.append(r15);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r12 = ": ";
        r11.append(r12);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11.append(r0);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11 = r11.toString();	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        android.util.Log.w(r3, r11);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        goto L_0x00f1;
    L_0x00d9:
        r0 = move-exception;
        r11 = r0;
        r0 = r11;
        r11 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11.<init>();	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r12 = "Class not found for preloading: ";
        r11.append(r12);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11.append(r15);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r11 = r11.toString();	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        android.util.Log.w(r3, r11);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
    L_0x00f1:
        r11 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.os.Trace.traceEnd(r11);	 Catch:{ IOException -> 0x0100, all -> 0x00fb }
        r12 = r16;
        r11 = 0;
        goto L_0x0057;
    L_0x00fb:
        r0 = move-exception;
        r11 = r16;
        goto L_0x0198;
    L_0x0100:
        r0 = move-exception;
        r11 = r16;
        goto L_0x016e;
    L_0x0105:
        r16 = r12;
    L_0x0107:
        r12 = r16;
        r11 = 0;
        goto L_0x0057;
    L_0x010c:
        r0 = move-exception;
        r11 = r12;
        goto L_0x0198;
    L_0x0110:
        r0 = move-exception;
        r11 = r12;
        goto L_0x016e;
    L_0x0113:
        r16 = r12;
        r0 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0165, all -> 0x0161 }
        r0.<init>();	 Catch:{ IOException -> 0x0165, all -> 0x0161 }
        r11 = "...preloaded ";
        r0.append(r11);	 Catch:{ IOException -> 0x0165, all -> 0x0161 }
        r0.append(r14);	 Catch:{ IOException -> 0x0165, all -> 0x0161 }
        r11 = " classes in ";
        r0.append(r11);	 Catch:{ IOException -> 0x0165, all -> 0x0161 }
        r11 = android.os.SystemClock.uptimeMillis();	 Catch:{ IOException -> 0x0165, all -> 0x0161 }
        r11 = r11 - r6;
        r0.append(r11);	 Catch:{ IOException -> 0x0165, all -> 0x0161 }
        r11 = "ms.";
        r0.append(r11);	 Catch:{ IOException -> 0x0165, all -> 0x0161 }
        r0 = r0.toString();	 Catch:{ IOException -> 0x0165, all -> 0x0161 }
        android.util.Log.i(r3, r0);	 Catch:{ IOException -> 0x0165, all -> 0x0161 }
        libcore.io.IoUtils.closeQuietly(r5);
        r11 = r16;
        r4.setTargetHeapUtilization(r11);
        r12 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.os.Trace.traceBegin(r12, r2);
        r4.preloadDexCaches();
        android.os.Trace.traceEnd(r12);
        if (r10 == 0) goto L_0x0196;
    L_0x0152:
        r2 = 0;
        android.system.Os.setreuid(r2, r2);	 Catch:{ ErrnoException -> 0x015a }
        android.system.Os.setregid(r2, r2);	 Catch:{ ErrnoException -> 0x015a }
        goto L_0x018e;
    L_0x015a:
        r0 = move-exception;
        r2 = new java.lang.RuntimeException;
        r2.<init>(r1, r0);
        throw r2;
    L_0x0161:
        r0 = move-exception;
        r11 = r16;
        goto L_0x0198;
    L_0x0165:
        r0 = move-exception;
        r11 = r16;
        goto L_0x016e;
    L_0x0169:
        r0 = move-exception;
        r11 = r12;
        goto L_0x0198;
    L_0x016c:
        r0 = move-exception;
        r11 = r12;
    L_0x016e:
        r12 = "Error reading /system/etc/preloaded-classes.";
        android.util.Log.e(r3, r12, r0);	 Catch:{ all -> 0x0197 }
        libcore.io.IoUtils.closeQuietly(r5);
        r4.setTargetHeapUtilization(r11);
        r12 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.os.Trace.traceBegin(r12, r2);
        r4.preloadDexCaches();
        android.os.Trace.traceEnd(r12);
        if (r10 == 0) goto L_0x0196;
    L_0x0187:
        r2 = 0;
        android.system.Os.setreuid(r2, r2);	 Catch:{ ErrnoException -> 0x018f }
        android.system.Os.setregid(r2, r2);	 Catch:{ ErrnoException -> 0x018f }
    L_0x018e:
        goto L_0x0196;
    L_0x018f:
        r0 = move-exception;
        r2 = new java.lang.RuntimeException;
        r2.<init>(r1, r0);
        throw r2;
    L_0x0196:
        return;
    L_0x0197:
        r0 = move-exception;
    L_0x0198:
        libcore.io.IoUtils.closeQuietly(r5);
        r4.setTargetHeapUtilization(r11);
        r12 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        android.os.Trace.traceBegin(r12, r2);
        r4.preloadDexCaches();
        android.os.Trace.traceEnd(r12);
        if (r10 == 0) goto L_0x01ba;
    L_0x01ab:
        r2 = 0;
        android.system.Os.setreuid(r2, r2);	 Catch:{ ErrnoException -> 0x01b3 }
        android.system.Os.setregid(r2, r2);	 Catch:{ ErrnoException -> 0x01b3 }
        goto L_0x01ba;
    L_0x01b3:
        r0 = move-exception;
        r2 = new java.lang.RuntimeException;
        r2.<init>(r1, r0);
        throw r2;
    L_0x01ba:
        throw r0;
    L_0x01bb:
        r0 = move-exception;
        r1 = "Couldn't find /system/etc/preloaded-classes.";
        android.util.Log.e(r3, r1);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.ZygoteInit.preloadClasses():void");
    }

    private static void cacheNonBootClasspathClassLoaders() {
        SharedLibraryInfo hidlBase = new SharedLibraryInfo("/system/framework/android.hidl.base-V1.0-java.jar", null, null, null, 0, 0, null, null, null);
        new SharedLibraryInfo("/system/framework/android.hidl.manager-V1.0-java.jar", null, null, null, 0, 0, null, null, null).addDependency(hidlBase);
        ApplicationLoaders.getDefault().createAndCacheNonBootclasspathSystemClassLoaders(new SharedLibraryInfo[]{hidlBase, hidlManager});
    }

    private static void preloadResources() {
        String str = " resources in ";
        String str2 = "ms.";
        String str3 = "...preloaded ";
        String str4 = TAG;
        VMRuntime runtime = VMRuntime.getRuntime();
        try {
            mResources = Resources.getSystem();
            mResources.startPreloading();
            Log.i(str4, "Preloading resources...");
            long startTime = SystemClock.uptimeMillis();
            TypedArray ar = mResources.obtainTypedArray(R.array.preloaded_drawables);
            int N = preloadDrawables(ar);
            ar.recycle();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str3);
            stringBuilder.append(N);
            stringBuilder.append(str);
            stringBuilder.append(SystemClock.uptimeMillis() - startTime);
            stringBuilder.append(str2);
            Log.i(str4, stringBuilder.toString());
            startTime = SystemClock.uptimeMillis();
            ar = mResources.obtainTypedArray(R.array.preloaded_color_state_lists);
            N = preloadColorStateLists(ar);
            ar.recycle();
            stringBuilder = new StringBuilder();
            stringBuilder.append(str3);
            stringBuilder.append(N);
            stringBuilder.append(str);
            stringBuilder.append(SystemClock.uptimeMillis() - startTime);
            stringBuilder.append(str2);
            Log.i(str4, stringBuilder.toString());
            ZygoteInitInjector.preloadMiuiResources(mResources);
            if (mResources.getBoolean(R.bool.config_freeformWindowManagement)) {
                startTime = SystemClock.uptimeMillis();
                TypedArray ar2 = mResources.obtainTypedArray(R.array.preloaded_freeform_multi_window_drawables);
                int N2 = preloadDrawables(ar2);
                ar2.recycle();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str3);
                stringBuilder2.append(N2);
                stringBuilder2.append(" resource in ");
                stringBuilder2.append(SystemClock.uptimeMillis() - startTime);
                stringBuilder2.append(str2);
                Log.i(str4, stringBuilder2.toString());
            }
            mResources.finishPreloading();
        } catch (RuntimeException e) {
            Log.w(str4, "Failure preloading resources", e);
        }
    }

    private static int preloadColorStateLists(TypedArray ar) {
        int N = ar.length();
        int i = 0;
        while (i < N) {
            int id = ar.getResourceId(i, 0);
            if (id == 0 || mResources.getColorStateList(id, null) != null) {
                i++;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to find preloaded color resource #0x");
                stringBuilder.append(Integer.toHexString(id));
                stringBuilder.append(" (");
                stringBuilder.append(ar.getString(i));
                stringBuilder.append(")");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        return N;
    }

    private static int preloadDrawables(TypedArray ar) {
        int N = ar.length();
        int i = 0;
        while (i < N) {
            int id = ar.getResourceId(i, 0);
            if (id == 0 || mResources.getDrawable(id, null) != null) {
                i++;
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to find preloaded drawable resource #0x");
                stringBuilder.append(Integer.toHexString(id));
                stringBuilder.append(" (");
                stringBuilder.append(ar.getString(i));
                stringBuilder.append(")");
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }
        return N;
    }

    private static void gcAndFinalize() {
        ZygoteHooks.gcAndFinalize();
    }

    private static Runnable handleSystemServerProcess(ZygoteArguments parsedArgs) {
        Os.umask(OsConstants.S_IRWXG | OsConstants.S_IRWXO);
        if (parsedArgs.mNiceName != null) {
            Process.setArgV0(parsedArgs.mNiceName);
        }
        String systemServerClasspath = Os.getenv("SYSTEMSERVERCLASSPATH");
        if (systemServerClasspath != null) {
            if (performSystemServerDexOpt(systemServerClasspath)) {
                sCachedSystemServerClassLoader = null;
            }
            if (SystemProperties.getBoolean("dalvik.vm.profilesystemserver", false) && (Build.IS_USERDEBUG || Build.IS_ENG)) {
                try {
                    prepareSystemServerProfile(systemServerClasspath);
                } catch (Exception e) {
                    Log.wtf(TAG, "Failed to set up system server profile", e);
                }
            }
        }
        if (parsedArgs.mInvokeWith != null) {
            String[] args = parsedArgs.mRemainingArgs;
            if (systemServerClasspath != null) {
                String[] amendedArgs = new String[(args.length + 2)];
                amendedArgs[0] = "-cp";
                amendedArgs[1] = systemServerClasspath;
                System.arraycopy(args, 0, amendedArgs, 2, args.length);
                args = amendedArgs;
            }
            WrapperInit.execApplication(parsedArgs.mInvokeWith, parsedArgs.mNiceName, parsedArgs.mTargetSdkVersion, VMRuntime.getCurrentInstructionSet(), null, args);
            throw new IllegalStateException("Unexpected return from WrapperInit.execApplication");
        }
        createSystemServerClassLoader();
        ClassLoader cl = sCachedSystemServerClassLoader;
        if (cl != null) {
            Thread.currentThread().setContextClassLoader(cl);
        }
        return zygoteInit(parsedArgs.mTargetSdkVersion, parsedArgs.mRemainingArgs, cl);
    }

    private static void createSystemServerClassLoader() {
        if (sCachedSystemServerClassLoader == null) {
            String systemServerClasspath = Os.getenv("SYSTEMSERVERCLASSPATH");
            if (systemServerClasspath != null) {
                sCachedSystemServerClassLoader = createPathClassLoader(systemServerClasspath, 10000);
            }
        }
    }

    private static void prepareSystemServerProfile(String systemServerClasspath) throws RemoteException {
        if (!systemServerClasspath.isEmpty()) {
            String[] codePaths = systemServerClasspath.split(":");
            String systemServerPackageName = "android";
            String systemServerProfileName = "primary.prof";
            Stub.asInterface(ServiceManager.getService("installd")).prepareAppProfile(systemServerPackageName, 0, UserHandle.getAppId(1000), systemServerProfileName, codePaths[0], null);
            VMRuntime.registerAppInfo(new File(Environment.getDataProfilesDePackageDirectory(0, systemServerPackageName), systemServerProfileName).getAbsolutePath(), codePaths);
        }
    }

    public static void setApiBlacklistExemptions(String[] exemptions) {
        VMRuntime.getRuntime().setHiddenApiExemptions(exemptions);
    }

    public static void setHiddenApiAccessLogSampleRate(int percent) {
        VMRuntime.getRuntime().setHiddenApiAccessLogSamplingRate(percent);
    }

    public static void setHiddenApiUsageLogger(HiddenApiUsageLogger logger) {
        VMRuntime.getRuntime();
        VMRuntime.setHiddenApiUsageLogger(logger);
    }

    static ClassLoader createPathClassLoader(String classPath, int targetSdkVersion) {
        String libraryPath = System.getProperty("java.library.path");
        return ClassLoaderFactory.createClassLoader(classPath, libraryPath, libraryPath, ClassLoader.getSystemClassLoader().getParent(), targetSdkVersion, true, null);
    }

    private static boolean performSystemServerDexOpt(String classPath) {
        String classPathElement;
        int i;
        int i2;
        StringBuilder stringBuilder;
        String classPathForElement;
        Exception e;
        String str = TAG;
        String[] classPathElements = classPath.split(":");
        IInstalld installd = Stub.asInterface(ServiceManager.getService("installd"));
        String instructionSet = VMRuntime.getRuntime().vmInstructionSet();
        int length = classPathElements.length;
        String classPathForElement2 = "";
        boolean compiledSomething = false;
        int i3 = 0;
        while (i3 < length) {
            int dexoptNeeded;
            String classPathElement2 = classPathElements[i3];
            String systemServerFilter = SystemProperties.get("dalvik.vm.systemservercompilerfilter", "speed");
            try {
                dexoptNeeded = DexFile.getDexOptNeeded(classPathElement2, instructionSet, systemServerFilter, null, false, false);
            } catch (FileNotFoundException ignored) {
                classPathElement = classPathElement2;
                String classPathForElement3 = classPathForElement2;
                i = i3;
                i2 = length;
                FileNotFoundException ignored2 = ignored2;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Missing classpath element for system server: ");
                stringBuilder2.append(classPathElement);
                Log.w(str, stringBuilder2.toString());
                classPathForElement2 = classPathForElement3;
            } catch (IOException e2) {
                IOException e22 = e22;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Error checking classpath element for system server: ");
                stringBuilder.append(classPathElement2);
                Log.w(str, stringBuilder.toString(), e22);
                dexoptNeeded = null;
            }
            if (dexoptNeeded != 0) {
                String packageName = "*";
                String classPathElement3;
                try {
                    classPathElement3 = classPathElement2;
                    classPathForElement = classPathForElement2;
                    i = i3;
                    i2 = length;
                    try {
                        installd.dexopt(classPathElement2, 1000, "*", instructionSet, dexoptNeeded, null, 0, systemServerFilter, StorageManager.UUID_PRIVATE_INTERNAL, getSystemServerClassLoaderContext(classPathForElement2), null, false, 0, null, null, "server-dexopt");
                        compiledSomething = true;
                        classPathElement = classPathElement3;
                    } catch (RemoteException | ServiceSpecificException e3) {
                        e = e3;
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Failed compiling classpath element for system server: ");
                        classPathElement = classPathElement3;
                        stringBuilder.append(classPathElement);
                        Log.w(str, stringBuilder.toString(), e);
                        classPathForElement2 = encodeSystemServerClassPath(classPathForElement, classPathElement);
                        i3 = i + 1;
                        length = i2;
                    }
                } catch (RemoteException | ServiceSpecificException e4) {
                    e = e4;
                    classPathElement3 = classPathElement2;
                    classPathForElement = classPathForElement2;
                    i = i3;
                    i2 = length;
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed compiling classpath element for system server: ");
                    classPathElement = classPathElement3;
                    stringBuilder.append(classPathElement);
                    Log.w(str, stringBuilder.toString(), e);
                    classPathForElement2 = encodeSystemServerClassPath(classPathForElement, classPathElement);
                    i3 = i + 1;
                    length = i2;
                }
            } else {
                classPathElement = classPathElement2;
                classPathForElement = classPathForElement2;
                i = i3;
                i2 = length;
            }
            classPathForElement2 = encodeSystemServerClassPath(classPathForElement, classPathElement);
            i3 = i + 1;
            length = i2;
        }
        return compiledSomething;
    }

    private static String getSystemServerClassLoaderContext(String classPath) {
        if (classPath == null) {
            return "PCL[]";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PCL[");
        stringBuilder.append(classPath);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private static String encodeSystemServerClassPath(String classPath, String newElement) {
        if (classPath == null || classPath.isEmpty()) {
            return newElement;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(classPath);
        stringBuilder.append(":");
        stringBuilder.append(newElement);
        return stringBuilder.toString();
    }

    private static Runnable forkSystemServer(String abiList, String socketName, ZygoteServer zygoteServer) {
        long capabilities = posixCapabilitiesAsBits(OsConstants.CAP_IPC_LOCK, OsConstants.CAP_KILL, OsConstants.CAP_NET_ADMIN, OsConstants.CAP_NET_BIND_SERVICE, OsConstants.CAP_NET_BROADCAST, OsConstants.CAP_NET_RAW, OsConstants.CAP_SYS_MODULE, OsConstants.CAP_SYS_NICE, OsConstants.CAP_SYS_RESOURCE, OsConstants.CAP_SYS_PTRACE, OsConstants.CAP_SYS_TIME, OsConstants.CAP_SYS_TTY_CONFIG, OsConstants.CAP_WAKE_ALARM, OsConstants.CAP_BLOCK_SUSPEND);
        try {
            StructCapUserData[] data = Os.capget(new StructCapUserHeader(OsConstants._LINUX_CAPABILITY_VERSION_3, 0));
            long capabilities2 = ((((long) data[1].effective) << 32) | ((long) data[0].effective)) & capabilities;
            String[] strArr = new String[8];
            strArr[0] = "--setuid=1000";
            strArr[1] = "--setgid=1000";
            strArr[2] = "--setgroups=1001,1002,1003,1004,1005,1006,1007,1008,1009,1010,1018,1021,1023,1024,1032,1065,3001,3002,3003,3006,3007,3009,3010,9801";
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("--capabilities=");
            stringBuilder.append(capabilities2);
            stringBuilder.append(",");
            stringBuilder.append(capabilities2);
            strArr[3] = stringBuilder.toString();
            strArr[4] = "--nice-name=system_server";
            strArr[5] = "--runtime-args";
            strArr[6] = "--target-sdk-version=10000";
            strArr[7] = "com.android.server.SystemServer";
            try {
                ZygoteArguments parsedArgs = new ZygoteArguments(strArr);
                Zygote.applyDebuggerSystemProperty(parsedArgs);
                Zygote.applyInvokeWithSystemProperty(parsedArgs);
                if (SystemProperties.getBoolean("dalvik.vm.profilesystemserver", false)) {
                    parsedArgs.mRuntimeFlags |= 16384;
                }
                if (Zygote.forkSystemServer(parsedArgs.mUid, parsedArgs.mGid, parsedArgs.mGids, parsedArgs.mRuntimeFlags, null, parsedArgs.mPermittedCapabilities, parsedArgs.mEffectiveCapabilities) != 0) {
                    return null;
                }
                if (hasSecondZygote(abiList)) {
                    waitForSecondaryZygote(socketName);
                }
                zygoteServer.closeServerSocket();
                return handleSystemServerProcess(parsedArgs);
            } catch (IllegalArgumentException ex) {
                throw new RuntimeException(ex);
            }
        } catch (ErrnoException e) {
            throw new RuntimeException("Failed to capget()", e);
        }
    }

    private static long posixCapabilitiesAsBits(int... capabilities) {
        long result = 0;
        for (int capability : capabilities) {
            if (capability < 0 || capability > OsConstants.CAP_LAST_CAP) {
                throw new IllegalArgumentException(String.valueOf(capability));
            }
            result |= 1 << capability;
        }
        return result;
    }

    @UnsupportedAppUsage
    public static void main(String[] argv) {
        String str = SOCKET_NAME_ARG;
        String str2 = "--abi-list=";
        String str3 = TAG;
        ZygoteServer zygoteServer = null;
        ZygoteHooks.startZygoteNoThreadCreation();
        try {
            Os.setpgid(0, 0);
            if (!"1".equals(SystemProperties.get("sys.boot_completed"))) {
                MetricsLogger.histogram(null, "boot_zygote_init", (int) SystemClock.elapsedRealtime());
            }
            TimingsTraceLog bootTimingsTraceLog = new TimingsTraceLog(Process.is64Bit() ? "Zygote64Timing" : "Zygote32Timing", 16384);
            bootTimingsTraceLog.traceBegin("ZygoteInit");
            RuntimeInit.enableDdms();
            SecurityManager.init();
            boolean startSystemServer = false;
            String str4 = Zygote.PRIMARY_SOCKET_NAME;
            String zygoteSocketName = str4;
            String abiList = null;
            boolean enableLazyPreload = false;
            int i = 1;
            while (i < argv.length) {
                try {
                    if ("start-system-server".equals(argv[i])) {
                        startSystemServer = true;
                    } else if ("--enable-lazy-preload".equals(argv[i])) {
                        enableLazyPreload = true;
                    } else if (argv[i].startsWith(str2)) {
                        abiList = argv[i].substring(str2.length());
                    } else if (argv[i].startsWith(str)) {
                        zygoteSocketName = argv[i].substring(str.length());
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Unknown command line argument: ");
                        stringBuilder.append(argv[i]);
                        throw new RuntimeException(stringBuilder.toString());
                    }
                    i++;
                } catch (Throwable th) {
                    if (zygoteServer != null) {
                        zygoteServer.closeServerSocket();
                    }
                }
            }
            boolean isPrimaryZygote = zygoteSocketName.equals(str4);
            if (abiList != null) {
                if (enableLazyPreload) {
                    Zygote.resetNicePriority();
                } else {
                    bootTimingsTraceLog.traceBegin("ZygotePreload");
                    EventLog.writeEvent((int) LOG_BOOT_PROGRESS_PRELOAD_START, SystemClock.uptimeMillis());
                    preload(bootTimingsTraceLog);
                    EventLog.writeEvent((int) LOG_BOOT_PROGRESS_PRELOAD_END, SystemClock.uptimeMillis());
                    bootTimingsTraceLog.traceEnd();
                }
                bootTimingsTraceLog.traceBegin("PostZygoteInitGC");
                gcAndFinalize();
                bootTimingsTraceLog.traceEnd();
                bootTimingsTraceLog.traceEnd();
                Trace.setTracingEnabled(false, 0);
                Zygote.initNativeState(isPrimaryZygote);
                ZygoteHooks.stopZygoteNoThreadCreation();
                zygoteServer = new ZygoteServer(isPrimaryZygote);
                if (startSystemServer) {
                    Runnable r = forkSystemServer(abiList, zygoteSocketName, zygoteServer);
                    if (r != null) {
                        r.run();
                        zygoteServer.closeServerSocket();
                        return;
                    }
                }
                Log.i(str3, "Accepting command socket connections");
                isPrimaryZygote = zygoteServer.runSelectLoop(abiList);
                zygoteServer.closeServerSocket();
                if (isPrimaryZygote) {
                    isPrimaryZygote.run();
                }
                return;
            }
            throw new RuntimeException("No ABI list supplied.");
        } catch (ErrnoException ex) {
            throw new RuntimeException("Failed to setpgid(0,0)", ex);
        }
    }

    private static boolean hasSecondZygote(String abiList) {
        return SystemProperties.get("ro.product.cpu.abilist").equals(abiList) ^ 1;
    }

    private static void waitForSecondaryZygote(String socketName) {
        String otherZygoteName = Zygote.PRIMARY_SOCKET_NAME;
        if (otherZygoteName.equals(socketName)) {
            otherZygoteName = Zygote.SECONDARY_SOCKET_NAME;
        }
        ZygoteProcess.waitForConnectionToZygote(otherZygoteName);
    }

    static boolean isPreloadComplete() {
        return sPreloadComplete;
    }

    private ZygoteInit() {
    }

    public static final Runnable zygoteInit(int targetSdkVersion, String[] argv, ClassLoader classLoader) {
        Trace.traceBegin(64, "ZygoteInit");
        RuntimeInit.redirectLogStreams();
        RuntimeInit.commonInit();
        nativeZygoteInit();
        return RuntimeInit.applicationInit(targetSdkVersion, argv, classLoader);
    }

    static final Runnable childZygoteInit(int targetSdkVersion, String[] argv, ClassLoader classLoader) {
        Arguments args = new Arguments(argv);
        return RuntimeInit.findStaticMain(args.startClass, args.startArgs, classLoader);
    }
}
