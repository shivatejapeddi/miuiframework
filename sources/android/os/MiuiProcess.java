package android.os;

import android.util.Log;

public class MiuiProcess {
    public static final int BACKUP_UID = 9800;
    public static final int FINDDEVICE_UID = 9810;
    public static final int SCHED_RESET_ON_FORK = 1073741824;
    private static final String TAG = "LockBoost";
    public static final int THEME_UID = 9801;
    public static final int THREAD_GROUP_DEFAULT = -1;
    public static final int THREAD_GROUP_FG_LIMITED = 10;
    public static final int THREAD_GROUP_FG_SERVICE = 9;
    public static final int THREAD_GROUP_FOREGROUND = 1;
    public static final int THREAD_GROUP_TOP_APP = 5;
    public static final int UPDATER_UID = 9802;
    static ThreadLocal<PriorityState> sThreadPriorityState = new ThreadLocal<PriorityState>() {
        /* Access modifiers changed, original: protected */
        public PriorityState initialValue() {
            return new PriorityState();
        }
    };

    private static final class PriorityState {
        private int prevPriority;
        private int regionCounter;

        private PriorityState() {
            this.regionCounter = 0;
            this.prevPriority = Integer.MIN_VALUE;
        }

        /* synthetic */ PriorityState(AnonymousClass1 x0) {
            this();
        }
    }

    public static void boostPriorityForLockedSection() {
        int tid = Process.myTid();
        int prevPriority = Process.getThreadPriority(tid);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("tid=");
        stringBuilder.append(tid);
        stringBuilder.append(", prevPriority=");
        stringBuilder.append(prevPriority);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.d(str, stringBuilder2);
        PriorityState state = (PriorityState) sThreadPriorityState.get();
        if (state.regionCounter == 0 && prevPriority > -2) {
            state.prevPriority = prevPriority;
            Process.setThreadPriority(tid, -2);
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("thread tid=");
            stringBuilder3.append(tid);
            stringBuilder3.append(" priority is boosted to -2");
            Log.d(str, stringBuilder3.toString());
        }
        state.regionCounter = state.regionCounter + 1;
    }

    public static void resetPriorityAfterLockedSection() {
        PriorityState state = (PriorityState) sThreadPriorityState.get();
        state.regionCounter = state.regionCounter - 1;
        if (state.regionCounter == 0 && state.prevPriority > -2) {
            Process.setThreadPriority(Process.myTid(), state.prevPriority);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("thread tid=");
            stringBuilder.append(Process.myTid());
            stringBuilder.append(" priority is reset to ");
            stringBuilder.append(state.prevPriority);
            Log.d(TAG, stringBuilder.toString());
        }
    }

    public static void setThreadPriority(int tid, int priority, String tag) {
        String str = "thread tid=";
        if (tag == null) {
            tag = "MiuiProcess";
        }
        try {
            Process.setThreadPriority(tid, priority);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(tid);
            stringBuilder.append(", priority is set to ");
            stringBuilder.append(priority);
            Log.d(tag, stringBuilder.toString());
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str);
            stringBuilder2.append(tid);
            stringBuilder2.append(", set priority error");
            Log.e(tag, stringBuilder2.toString());
            e.printStackTrace();
        }
    }

    public static void setThreadPriority(int priority, String tag) {
        setThreadPriority(Process.myTid(), priority, tag);
    }
}
