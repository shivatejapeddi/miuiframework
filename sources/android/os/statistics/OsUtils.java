package android.os.statistics;

import android.os.Process;
import android.view.ThreadedRenderer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import miui.util.ReflectionUtils;

public class OsUtils {
    public static final int SCHED_GROUP_UNKNOWN = Integer.MAX_VALUE;
    private static boolean sDoneFindIsRenderThreadMethod = false;
    private static Method sIsRenderThreadMethod;

    public static native long getCoarseUptimeMillisFast();

    public static native long getDeltaToUptimeMillis();

    public static native long getRunningTimeMs();

    public static native void getThreadSchedStat(long[] jArr);

    private static final native int getThreadScheduler(int i) throws IllegalArgumentException;

    public static native boolean isIsolatedProcess();

    public static native void setThreadPriorityUnconditonally(int i, int i2);

    public static native String translateKernelAddress(long j);

    public static int decodeThreadSchedulePolicy(int scheduler) {
        return scheduler < 0 ? scheduler : -1073741825 & scheduler;
    }

    public static int getThreadSchedulePolicy(int tid) {
        try {
            return decodeThreadSchedulePolicy(getThreadScheduler(tid));
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean isHighPriority(int policy, int priority) {
        boolean z = false;
        if (policy == -1) {
            return false;
        }
        if (policy == 2 || policy == 1) {
            return true;
        }
        if (policy != 0) {
            return false;
        }
        if (priority <= -2) {
            z = true;
        }
        return z;
    }

    public static boolean isLowPriority(int policy, int priority) {
        boolean z = false;
        if (policy == -1 || policy == 2 || policy == 1) {
            return false;
        }
        if (policy != 0) {
            return true;
        }
        if (priority >= 10) {
            z = true;
        }
        return z;
    }

    public static int getSchedGroup(int pid) {
        try {
            return Process.getProcessGroup(pid);
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }

    public static boolean isHighSchedGroup(int group) {
        return group == -1 || group == 5 || group == 1 || group == 9 || group == 10;
    }

    public static boolean isRenderThread(int threadId) {
        if (!sDoneFindIsRenderThreadMethod) {
            sIsRenderThreadMethod = ReflectionUtils.tryFindMethodBestMatch(ThreadedRenderer.class, "isRenderThread", Integer.class);
            sDoneFindIsRenderThreadMethod = true;
        }
        Method method = sIsRenderThreadMethod;
        if (method == null) {
            return false;
        }
        try {
            return ((Boolean) method.invoke(null, new Object[]{Integer.valueOf(threadId)})).booleanValue();
        } catch (InvocationTargetException ex) {
            ex.printStackTrace();
            return false;
        } catch (IllegalAccessException ex2) {
            ex2.printStackTrace();
            return false;
        }
    }
}
