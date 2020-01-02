package android.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.os.StrictMode.VmPolicy;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.os.statistics.PerfSuperviser;
import android.util.EventLog;
import com.miui.whetstone.Event.EventLogTags;
import miui.log.SystemLogSwitchesConfigManager;
import miui.os.Build;

public class ActivityThreadInjector {
    private static final int AM_LIFECYCLE_SAMPLE_THRESHOLD = SystemProperties.getInt("persist.sys.handler.threshold", EventLogTags.BOOT_PROGRESS_START);
    private static final int LOG_AM_LIFECYCLE_SAMPLE = 30100;
    private static final boolean mEnableLifecycleSample = (AM_LIFECYCLE_SAMPLE_THRESHOLD > 0);

    public static void enableStrictMode() {
        if (!Build.IS_STABLE_VERSION) {
            boolean isPenaltyDeath = SystemProperties.getBoolean(StrictMode.VISUAL_PROPERTY, false);
            Builder threadPolicy = new Builder().detectNetwork().penaltyLog();
            VmPolicy.Builder vmPolicy = new VmPolicy.Builder().detectLeakedClosableObjects().detectLeakedRegistrationObjects().detectLeakedSqlLiteObjects().penaltyLog();
            if (isPenaltyDeath) {
                threadPolicy.penaltyDeath();
                vmPolicy.penaltyDeath();
            }
            StrictMode.setThreadPolicy(threadPolicy.build());
            StrictMode.setVmPolicy(vmPolicy.build());
        }
    }

    static void enableAppLogSwitch() {
        SystemLogSwitchesConfigManager.enableLogSwitch(false);
    }

    static void updatePackageInfoForLogSwitch(AppBindData data) {
        SystemLogSwitchesConfigManager.updatePackageName(data.appInfo.packageName);
        SystemLogSwitchesConfigManager.updateProgramName(data.processName);
    }

    static void raiseThreadPriority() {
    }

    static void preloadSubActivity(ActivityThread thiz, int theme, LoadedApk info) {
    }

    static void clearCachedDrawables() {
    }

    static void checkHandleMessageTime(long startTime, Message msg) {
        checkHandleMessageTime(startTime, msg != null ? msg.what : -1);
    }

    static void checkHandleMessageTime(long startTime, int MsgWhat) {
        if (mEnableLifecycleSample) {
            if (SystemClock.uptimeMillis() - startTime >= ((long) AM_LIFECYCLE_SAMPLE_THRESHOLD)) {
                EventLog.writeEvent((int) LOG_AM_LIFECYCLE_SAMPLE, Integer.valueOf(UserHandle.myUserId()), ActivityThread.currentProcessName(), Integer.valueOf(MsgWhat), Long.valueOf(SystemClock.uptimeMillis() - startTime));
            }
        }
    }

    static void bindApplicationInjector(Context context, ApplicationInfo info) {
    }

    public static boolean isPersistent(AppBindData data) {
        return (data == null || (data.appInfo.flags & 8) == 0) ? false : true;
    }

    public static boolean isSystemThread() {
        ActivityThread currentActivityThread = ActivityThread.currentActivityThread();
        return currentActivityThread != null ? currentActivityThread.mSystemThread : false;
    }

    public static Handler getHandler() {
        ActivityThread currentActivityThread = ActivityThread.currentActivityThread();
        return currentActivityThread == null ? null : currentActivityThread.getHandler();
    }

    public static void updateProcessInfo(String processName, String packageName) {
        PerfSuperviser.updateProcessInfo(processName, packageName);
    }
}
