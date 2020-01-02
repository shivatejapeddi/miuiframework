package com.android.internal.os;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.ApplicationErrorReport.ParcelableCrashInfo;
import android.ddm.DdmRegister;
import android.miui.Shell;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.DeadObjectException;
import android.os.Debug;
import android.os.Environment;
import android.os.FileUtils;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.Slog;
import com.android.internal.logging.AndroidConfig;
import com.android.server.NetworkManagementSocketTagger;
import dalvik.system.RuntimeHooks;
import dalvik.system.VMRuntime;
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.LogManager;
import miui.mqsas.fdmonitor.FdInfoManager;

public class RuntimeInit {
    static final boolean DEBUG = false;
    static final String TAG = "AndroidRuntime";
    private static boolean initialized;
    private static IBinder mApplicationObject;
    private static volatile boolean mCrashing = false;

    static class Arguments {
        String[] startArgs;
        String startClass;

        Arguments(String[] args) throws IllegalArgumentException {
            parseArgs(args);
        }

        private void parseArgs(String[] args) throws IllegalArgumentException {
            int curArg = 0;
            while (curArg < args.length) {
                String arg = args[curArg];
                String str = "--";
                if (arg.equals(str)) {
                    curArg++;
                    break;
                } else if (!arg.startsWith(str)) {
                    break;
                } else {
                    curArg++;
                }
            }
            if (curArg != args.length) {
                int curArg2 = curArg + 1;
                this.startClass = args[curArg];
                this.startArgs = new String[(args.length - curArg2)];
                String[] strArr = this.startArgs;
                System.arraycopy(args, curArg2, strArr, 0, strArr.length);
                return;
            }
            throw new IllegalArgumentException("Missing classname argument to RuntimeInit!");
        }
    }

    private static class KillApplicationHandler implements UncaughtExceptionHandler {
        private KillApplicationHandler() {
        }

        public void uncaughtException(Thread t, Throwable e) {
            try {
                if (RuntimeInit.mCrashing) {
                    Process.killProcess(Process.myPid());
                    System.exit(10);
                    return;
                }
                RuntimeInit.mCrashing = true;
                if (ActivityThread.currentActivityThread() != null) {
                    ActivityThread.currentActivityThread().stopProfiling();
                }
                ActivityManager.getService().handleApplicationCrash(RuntimeInit.mApplicationObject, new ParcelableCrashInfo(e));
                Process.killProcess(Process.myPid());
                System.exit(10);
            } catch (Throwable th) {
            }
        }
    }

    private static class LoggingHandler implements UncaughtExceptionHandler {
        private LoggingHandler() {
        }

        public void uncaughtException(Thread t, Throwable e) {
            if (!RuntimeInit.mCrashing) {
                IBinder access$100 = RuntimeInit.mApplicationObject;
                String str = "interested fdleak event，need raise rlimit temporarily!";
                String str2 = RuntimeInit.TAG;
                StringBuilder stringBuilder;
                String str3;
                if (access$100 == null && 1000 == Process.myUid()) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("*** FATAL EXCEPTION IN SYSTEM PROCESS: ");
                    stringBuilder.append(t.getName());
                    RuntimeInit.Clog_e(str2, stringBuilder.toString(), e);
                    if (SystemProperties.getInt("sys.is_mem_low_level", 0) == 3) {
                        str3 = "sys.is_mem_low_retried";
                        String str4 = "true";
                        if (str4.equals(SystemProperties.get(str3, "false"))) {
                            Slog.wtf(str2, "*** NO SPACE FOR SYSTEM, THE SYSTEM IS DEAD, LAST TRY.");
                            RuntimeInit.removeFileForLowMem();
                        } else {
                            Slog.wtf(str2, "*** NO SPACE FOR SYSTEM, AUTO CLEAN SOME FILES, FIRST TRY.");
                            RuntimeInit.removeFileForLowMem();
                            try {
                                SystemProperties.set(str3, str4);
                            } catch (Exception propException) {
                                StringBuilder stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("SystemProperties set, Exception is ");
                                stringBuilder2.append(propException);
                                Slog.e(str2, stringBuilder2.toString());
                            }
                        }
                    }
                    if (FdInfoManager.isInterestedFdLeakEvent(null, e)) {
                        Slog.d(str2, str);
                        RuntimeInit.raiseRlimit();
                    }
                    RuntimeInitInjector.onJE(Process.myPid(), "system_server", "system_server", t.getName(), e, "*** FATAL EXCEPTION IN SYSTEM PROCESS: ", true);
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("FATAL EXCEPTION: ");
                    stringBuilder.append(t.getName());
                    stringBuilder.append("\n");
                    str3 = ActivityThread.currentProcessName();
                    if (str3 != null) {
                        stringBuilder.append("Process: ");
                        stringBuilder.append(str3);
                        stringBuilder.append(", ");
                    }
                    stringBuilder.append("PID: ");
                    stringBuilder.append(Process.myPid());
                    RuntimeInit.Clog_e(str2, stringBuilder.toString(), e);
                    if (FdInfoManager.isInterestedFdLeakEvent(null, e)) {
                        Slog.d(str2, str);
                        RuntimeInit.raiseRlimit();
                    }
                    RuntimeInitInjector.onJE(Process.myPid(), ActivityThread.currentProcessName(), ActivityThread.currentPackageName(), t.getName(), e, "FATAL EXCEPTION: ", false);
                }
            }
        }
    }

    static class MethodAndArgsCaller implements Runnable {
        private final String[] mArgs;
        private final Method mMethod;

        public MethodAndArgsCaller(Method method, String[] args) {
            this.mMethod = method;
            this.mArgs = args;
        }

        public void run() {
            try {
                this.mMethod.invoke(null, new Object[]{this.mArgs});
            } catch (IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (InvocationTargetException ex2) {
                Throwable cause = ex2.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                } else if (cause instanceof Error) {
                    throw ((Error) cause);
                } else {
                    throw new RuntimeException(ex2);
                }
            }
        }
    }

    private static final native void nativeFinishInit();

    private static final native void nativeRaiseRlimit();

    private static final native void nativeSetExitWithoutCleanup(boolean z);

    public static final void removeFileForLowMem() {
        String str = "removeFileForLowMem: ";
        String str2 = TAG;
        try {
            String dataDir = Environment.getDataDirectory().getAbsolutePath();
            String extDir = Environment.getExternalStorageDirectory().getAbsolutePath();
            extRemoveList = new String[17];
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(extDir);
            stringBuilder.append("/downloaded_rom/");
            int i = 0;
            extRemoveList[0] = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(extDir);
            stringBuilder.append("/ramdump/");
            extRemoveList[1] = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(extDir);
            stringBuilder.append("/MIUI/debug_log/");
            extRemoveList[2] = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(extDir);
            stringBuilder.append("/miliao/");
            extRemoveList[3] = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(extDir);
            stringBuilder.append("/.xiaomi/");
            extRemoveList[4] = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(extDir);
            stringBuilder.append("/step_log/");
            extRemoveList[5] = stringBuilder.toString();
            stringBuilder = new StringBuilder();
            stringBuilder.append(extDir);
            stringBuilder.append("/Android/data/com.miui.cleanmaster/cache/");
            extRemoveList[6] = stringBuilder.toString();
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(extDir);
            stringBuilder2.append("/MIUI//music/album/");
            extRemoveList[7] = stringBuilder2.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(extDir);
            stringBuilder2.append("/MIUI/music/avatar/");
            extRemoveList[8] = stringBuilder2.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(extDir);
            stringBuilder2.append("/MIUI/music/lyric/");
            extRemoveList[9] = stringBuilder2.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(extDir);
            stringBuilder2.append("/MIUI/.cache/resource/");
            extRemoveList[10] = stringBuilder2.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(extDir);
            stringBuilder2.append("/MIUI/Gallery/cloud/.cache/");
            extRemoveList[11] = stringBuilder2.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(extDir);
            stringBuilder2.append("/MIUI/Gallery/cloud/.microthumbnailFile/");
            extRemoveList[12] = stringBuilder2.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(extDir);
            stringBuilder2.append("/MIUI/assistant/");
            extRemoveList[13] = stringBuilder2.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(extDir);
            stringBuilder2.append("/DuoKan/Cache/");
            extRemoveList[14] = stringBuilder2.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(extDir);
            stringBuilder2.append("/DuoKan/Downloads/Covers/");
            extRemoveList[15] = stringBuilder2.toString();
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(extDir);
            stringBuilder2.append("/browser/MediaCache/");
            extRemoveList[16] = stringBuilder2.toString();
            String[] dataRemoveList = new String[6];
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append(dataDir);
            stringBuilder3.append("/anr/");
            dataRemoveList[0] = stringBuilder3.toString();
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append(dataDir);
            stringBuilder3.append("/tombstones/");
            dataRemoveList[1] = stringBuilder3.toString();
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append(dataDir);
            stringBuilder4.append("/system/dropbox/");
            dataRemoveList[2] = stringBuilder4.toString();
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append(dataDir);
            stringBuilder4.append("/system/app_screenshot/");
            dataRemoveList[3] = stringBuilder4.toString();
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append(dataDir);
            stringBuilder4.append("/system/nativedebug/");
            dataRemoveList[4] = stringBuilder4.toString();
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append(dataDir);
            stringBuilder4.append("/mqsas/");
            dataRemoveList[5] = stringBuilder4.toString();
            stringBuilder4 = new StringBuilder();
            stringBuilder4.append(str);
            stringBuilder4.append(dataDir);
            stringBuilder4.append(", ");
            stringBuilder4.append(extDir);
            Slog.d(str2, stringBuilder4.toString());
            for (String extRemoveFile : extRemoveList) {
                StringBuilder stringBuilder5 = new StringBuilder();
                stringBuilder5.append(str);
                stringBuilder5.append(extRemoveFile);
                Slog.d(str2, stringBuilder5.toString());
                Shell.remove(extRemoveFile);
            }
            int length = dataRemoveList.length;
            while (i < length) {
                String dataRemoveFile = dataRemoveList[i];
                StringBuilder stringBuilder6 = new StringBuilder();
                stringBuilder6.append(str);
                stringBuilder6.append(dataRemoveFile);
                Slog.d(str2, stringBuilder6.toString());
                FileUtils.deleteContents(new File(dataRemoveFile));
                i++;
            }
        } catch (Throwable th) {
        }
    }

    private static int Clog_e(String tag, String msg, Throwable tr) {
        return Log.printlns(4, 6, tag, msg, tr);
    }

    protected static final void commonInit() {
        RuntimeHooks.setUncaughtExceptionPreHandler(new LoggingHandler());
        Thread.setDefaultUncaughtExceptionHandler(new KillApplicationHandler());
        RuntimeHooks.setTimeZoneIdSupplier(-$$Lambda$RuntimeInit$ep4ioD9YINkHI5Q1wZ0N_7VFAOg.INSTANCE);
        LogManager.getLogManager().reset();
        AndroidConfig androidConfig = new AndroidConfig();
        System.setProperty("http.agent", RuntimeInitInjector.getDefaultUserAgent());
        NetworkManagementSocketTagger.install();
        if (SystemProperties.get("ro.kernel.android.tracing").equals("1")) {
            Slog.i(TAG, "NOTE: emulator trace profiling enabled");
            Debug.enableEmulatorTraceOutput();
        }
        initialized = true;
    }

    private static String getDefaultUserAgent() {
        String model;
        StringBuilder result = new StringBuilder(64);
        result.append("Dalvik/");
        result.append(System.getProperty("java.vm.version"));
        result.append(" (Linux; U; Android ");
        String version = VERSION.RELEASE;
        result.append(version.length() > 0 ? version : "1.0");
        if ("REL".equals(VERSION.CODENAME)) {
            model = Build.MODEL;
            if (model.length() > 0) {
                result.append("; ");
                result.append(model);
            }
        }
        model = Build.ID;
        if (model.length() > 0) {
            result.append(" Build/");
            result.append(model);
        }
        result.append(")");
        return result.toString();
    }

    protected static Runnable findStaticMain(String className, String[] argv, ClassLoader classLoader) {
        StringBuilder stringBuilder;
        try {
            try {
                Method m = Class.forName(className, true, classLoader).getMethod("main", new Class[]{String[].class});
                int modifiers = m.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isPublic(modifiers)) {
                    return new MethodAndArgsCaller(m, argv);
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Main method is not public and static on ");
                stringBuilder2.append(className);
                throw new RuntimeException(stringBuilder2.toString());
            } catch (NoSuchMethodException ex) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Missing static main on ");
                stringBuilder.append(className);
                throw new RuntimeException(stringBuilder.toString(), ex);
            } catch (SecurityException ex2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Problem getting static main on ");
                stringBuilder.append(className);
                throw new RuntimeException(stringBuilder.toString(), ex2);
            }
        } catch (ClassNotFoundException ex3) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Missing class when invoking static main ");
            stringBuilder3.append(className);
            throw new RuntimeException(stringBuilder3.toString(), ex3);
        }
    }

    public static final void main(String[] argv) {
        enableDdms();
        if (argv.length == 2 && argv[1].equals("application")) {
            redirectLogStreams();
        }
        commonInit();
        nativeFinishInit();
    }

    protected static Runnable applicationInit(int targetSdkVersion, String[] argv, ClassLoader classLoader) {
        nativeSetExitWithoutCleanup(true);
        VMRuntime.getRuntime().setTargetHeapUtilization(0.75f);
        VMRuntime.getRuntime().setTargetSdkVersion(targetSdkVersion);
        Arguments args = new Arguments(argv);
        Trace.traceEnd(64);
        return findStaticMain(args.startClass, args.startArgs, classLoader);
    }

    public static void redirectLogStreams() {
        System.out.close();
        System.setOut(new AndroidPrintStream(4, "System.out"));
        System.err.close();
        System.setErr(new AndroidPrintStream(5, "System.err"));
    }

    public static void wtf(String tag, Throwable t, boolean system) {
        try {
            if (ActivityManager.getService().handleApplicationWtf(mApplicationObject, tag, system, new ParcelableCrashInfo(t))) {
                Process.killProcess(Process.myPid());
                System.exit(10);
            }
        } catch (Throwable t2) {
            if (!(t2 instanceof DeadObjectException)) {
                String str = TAG;
                Slog.e(str, "Error reporting WTF", t2);
                Slog.e(str, "Original WTF:", t);
            }
        }
    }

    public static final void setApplicationObject(IBinder app) {
        mApplicationObject = app;
    }

    public static final void raiseRlimit() {
        nativeRaiseRlimit();
    }

    public static final IBinder getApplicationObject() {
        return mApplicationObject;
    }

    static final void enableDdms() {
        DdmRegister.registerHandlers();
    }
}
