package android.os.statistics;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.util.EventLog;
import android.util.Slog;
import com.android.internal.app.IPerfShielder;
import com.miui.daemon.performance.PerfShielderManager;
import java.util.HashMap;
import java.util.LinkedList;

public class BinderServerMonitor {
    private static final boolean DEBUG = true;
    private static final int MAX_RECORDS_QUEUE_CAPACITY = 12;
    public static final String PERFSHIELDER_SERVICE_NAME = "perfshielder";
    private static final String TAG = "BinderServerMonitor";
    private static boolean isEnabled = SystemProperties.getBoolean("persist.sys.excessive_cpu_usage_enabled", true);
    private static HashMap<Integer, Long> mBinderClientCpuUsages = new HashMap();
    private static LinkedList<Bundle> mExcessiveCpuUsageRecords = new LinkedList();
    private static int mExcessiveCpuUsageThershold = SystemProperties.getInt("persist.sys.excessive_cpu_usage", 20);
    private static IPerfShielder mPerfService;

    static class CpuUsageInfo {
        public long binderCpuTime;
        public double cpuUsage;
        public long currentTimeMills;
        public long duration;
        public int pid;
        public String procName;
        public int procState;
        public long runningTime;
        public int uid;

        CpuUsageInfo() {
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(128);
            sb.append("name:");
            sb.append(this.procName);
            sb.append(" procState:");
            sb.append(this.procState);
            sb.append(String.format(" cputime:%s(%s:%s)", new Object[]{Long.valueOf(this.runningTime), Long.valueOf(this.runningTime - this.binderCpuTime), Long.valueOf(this.binderCpuTime)}));
            sb.append(" cpuUsage:");
            sb.append(this.cpuUsage);
            sb.append("%");
            return sb.toString();
        }

        public Bundle toBundle() {
            Bundle bundle = new Bundle();
            bundle.putString("procName", this.procName);
            bundle.putInt("procState", this.procState);
            bundle.putLong("duration", this.duration);
            bundle.putLong(PerfEventConstants.FIELD_RUNNING_TIME, this.runningTime);
            bundle.putLong("binderCpuTime", this.binderCpuTime);
            bundle.putDouble("cpuUsage", this.cpuUsage);
            return bundle;
        }
    }

    public static native long clearBinderClientCpuTimeUsed(int i);

    static native void dumpBinderClientCpuTimeUsed(HashMap hashMap);

    static native void nativeStart();

    public static void start() {
        if (isEnabled) {
            nativeStart();
            mPerfService = PerfShielderManager.getService();
        }
    }

    public static int getExcessiveCpuUsageThreshold() {
        return mExcessiveCpuUsageThershold;
    }

    public static void updateBinderClientCpuUsages() {
        mBinderClientCpuUsages.clear();
        dumpBinderClientCpuTimeUsed(mBinderClientCpuUsages);
    }

    public static HashMap<Integer, Long> getBinderClientCpuUsages() {
        return mBinderClientCpuUsages;
    }

    public static void recordExcessiveCpuUsage(String procName, int procState, int pid, int uid, long uptimeSince, long procCputime, long binderCpuTime, boolean reportToDaemon) {
        CpuUsageInfo cpuUsageInfo = new CpuUsageInfo();
        cpuUsageInfo.pid = pid;
        cpuUsageInfo.uid = uid;
        cpuUsageInfo.procName = procName;
        cpuUsageInfo.procState = procState;
        cpuUsageInfo.duration = uptimeSince;
        cpuUsageInfo.runningTime = procCputime + binderCpuTime;
        cpuUsageInfo.binderCpuTime = binderCpuTime;
        cpuUsageInfo.cpuUsage = (double) computeCpuUsage(uptimeSince, cpuUsageInfo.runningTime);
        cpuUsageInfo.currentTimeMills = System.currentTimeMillis();
        String logStr = cpuUsageInfo.toString();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("excessive process ");
        stringBuilder.append(logStr);
        Slog.w(TAG, stringBuilder.toString());
        EventLog.writeEvent(30093, logStr);
        if (reportToDaemon) {
            mExcessiveCpuUsageRecords.add(cpuUsageInfo.toBundle());
            if (mExcessiveCpuUsageRecords.size() >= 12) {
                IPerfShielder iPerfShielder = mPerfService;
                if (iPerfShielder != null) {
                    try {
                        iPerfShielder.reportExcessiveCpuUsageRecords(mExcessiveCpuUsageRecords);
                    } catch (RemoteException e) {
                    }
                }
                mExcessiveCpuUsageRecords.clear();
            }
        }
    }

    public static int computeCpuUsage(long upCputime, long cputimeUsed) {
        return upCputime != 0 ? (int) ((100 * cputimeUsed) / upCputime) : 0;
    }
}
