package com.android.internal.os;

import android.annotation.UnsupportedAppUsage;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Process;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Slog;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.os.BatteryStatsImpl.Uid.Proc;
import com.android.internal.util.FastPrintWriter;
import com.miui.mishare.app.connect.MiShareConnectivity;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.miui.commons.lang3.ClassUtils;

public class ProcessCpuTracker {
    private static final boolean DEBUG = false;
    private static final int[] LOAD_AVERAGE_FORMAT = new int[]{16416, 16416, 16416};
    private static final int[] PROCESS_FULL_STATS_FORMAT = new int[]{32, 4640, 32, 32, 32, 32, 32, 32, 32, 8224, 32, 8224, 32, 8224, 8224, 32, 32, 32, 32, 32, 32, 32, 8224};
    static final int PROCESS_FULL_STAT_MAJOR_FAULTS = 2;
    static final int PROCESS_FULL_STAT_MINOR_FAULTS = 1;
    static final int PROCESS_FULL_STAT_STIME = 4;
    static final int PROCESS_FULL_STAT_UTIME = 3;
    static final int PROCESS_FULL_STAT_VSIZE = 5;
    private static final int[] PROCESS_STATS_FORMAT = new int[]{32, MetricsEvent.DIALOG_WIFI_SKIP, 32, 32, 32, 32, 32, 32, 32, 8224, 32, 8224, 32, 8224, 8224};
    static final int PROCESS_STAT_MAJOR_FAULTS = 1;
    static final int PROCESS_STAT_MINOR_FAULTS = 0;
    static final int PROCESS_STAT_STIME = 3;
    static final int PROCESS_STAT_UTIME = 2;
    private static final int[] SYSTEM_CPU_FORMAT = new int[]{288, 8224, 8224, 8224, 8224, 8224, 8224, 8224};
    private static final String TAG = "ProcessCpuTracker";
    private static final boolean localLOGV = false;
    private static final Comparator<Stats> sLoadComparator = new Comparator<Stats>() {
        public final int compare(Stats sta, Stats stb) {
            int ta = sta.rel_utime + sta.rel_stime;
            int tb = stb.rel_utime + stb.rel_stime;
            int i = -1;
            if (ta != tb) {
                if (ta <= tb) {
                    i = 1;
                }
                return i;
            } else if (sta.added != stb.added) {
                if (!sta.added) {
                    i = 1;
                }
                return i;
            } else if (sta.removed == stb.removed) {
                return 0;
            } else {
                if (!sta.added) {
                    i = 1;
                }
                return i;
            }
        }
    };
    private long mBaseIdleTime;
    private long mBaseIoWaitTime;
    private long mBaseIrqTime;
    private long mBaseSoftIrqTime;
    private long mBaseSystemTime;
    private long mBaseUserTime;
    private int[] mCurPids;
    private int[] mCurThreadPids;
    private long mCurrentSampleRealTime;
    private long mCurrentSampleTime;
    private long mCurrentSampleWallTime;
    private boolean mFirst = true;
    private final boolean mIncludeThreads;
    private final long mJiffyMillis;
    private long mLastSampleRealTime;
    private long mLastSampleTime;
    private long mLastSampleWallTime;
    private float mLoad1 = 0.0f;
    private float mLoad15 = 0.0f;
    private float mLoad5 = 0.0f;
    private final float[] mLoadAverageData = new float[3];
    private final ArrayList<Stats> mProcStats = new ArrayList();
    private final long[] mProcessFullStatsData = new long[6];
    private final String[] mProcessFullStatsStringData = new String[6];
    private final long[] mProcessStatsData = new long[4];
    private int mRelIdleTime;
    private int mRelIoWaitTime;
    private int mRelIrqTime;
    private int mRelSoftIrqTime;
    private boolean mRelStatsAreGood;
    private int mRelSystemTime;
    private int mRelUserTime;
    private final long[] mSinglePidStatsData = new long[4];
    private final long[] mSystemCpuData = new long[7];
    private final ArrayList<Stats> mWorkingProcs = new ArrayList();
    private boolean mWorkingProcsSorted;

    public interface FilterStats {
        boolean needed(Stats stats);
    }

    public static class Stats {
        public boolean active;
        public boolean added;
        public String baseName;
        public long base_majfaults;
        public long base_minfaults;
        public long base_stime;
        public long base_uptime;
        public long base_utime;
        public Proc batteryStats;
        final String cmdlineFile;
        public boolean interesting;
        @UnsupportedAppUsage
        public String name;
        public int nameWidth;
        public final int pid;
        public int rel_majfaults;
        public int rel_minfaults;
        @UnsupportedAppUsage
        public int rel_stime;
        @UnsupportedAppUsage
        public long rel_uptime;
        @UnsupportedAppUsage
        public int rel_utime;
        public boolean removed;
        final String statFile;
        final ArrayList<Stats> threadStats;
        final String threadsDir;
        public final int uid;
        public long vsize;
        public boolean working;
        final ArrayList<Stats> workingThreads;

        Stats(int _pid, int parentPid, boolean includeThreads) {
            this.pid = _pid;
            String str = "stat";
            String str2 = MiShareConnectivity.EXTRA_TASK;
            String str3 = "/proc";
            if (parentPid < 0) {
                File procDir = new File(str3, Integer.toString(this.pid));
                this.uid = getUid(procDir.toString());
                this.statFile = new File(procDir, str).toString();
                this.cmdlineFile = new File(procDir, "cmdline").toString();
                this.threadsDir = new File(procDir, str2).toString();
                if (includeThreads) {
                    this.threadStats = new ArrayList();
                    this.workingThreads = new ArrayList();
                    return;
                }
                this.threadStats = null;
                this.workingThreads = null;
                return;
            }
            File taskDir = new File(new File(new File(str3, Integer.toString(parentPid)), str2), Integer.toString(this.pid));
            this.uid = getUid(taskDir.toString());
            this.statFile = new File(taskDir, str).toString();
            this.cmdlineFile = null;
            this.threadsDir = null;
            this.threadStats = null;
            this.workingThreads = null;
        }

        private static int getUid(String path) {
            try {
                return Os.stat(path).st_uid;
            } catch (ErrnoException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to stat(");
                stringBuilder.append(path);
                stringBuilder.append("): ");
                stringBuilder.append(e);
                Slog.w(ProcessCpuTracker.TAG, stringBuilder.toString());
                return -1;
            }
        }
    }

    @UnsupportedAppUsage
    public ProcessCpuTracker(boolean includeThreads) {
        this.mIncludeThreads = includeThreads;
        this.mJiffyMillis = 1000 / Os.sysconf(OsConstants._SC_CLK_TCK);
    }

    public void onLoadChanged(float load1, float load5, float load15) {
    }

    public int onMeasureProcessName(String name) {
        return 0;
    }

    public void init() {
        this.mFirst = true;
        update();
    }

    @UnsupportedAppUsage
    public void update() {
        long nowWallTime;
        long nowRealtime;
        long nowUptime;
        int sysCpu;
        long nowUptime2 = SystemClock.uptimeMillis();
        long nowRealtime2 = SystemClock.elapsedRealtime();
        long nowWallTime2 = System.currentTimeMillis();
        long[] sysCpu2 = this.mSystemCpuData;
        if (Process.readProcFile("/proc/stat", SYSTEM_CPU_FORMAT, null, sysCpu2, null)) {
            long usertime = sysCpu2[0] + sysCpu2[1];
            long softirqtime = this.mJiffyMillis;
            usertime *= softirqtime;
            long systemtime = sysCpu2[2] * softirqtime;
            nowWallTime = nowWallTime2;
            nowWallTime2 = sysCpu2[3] * softirqtime;
            nowRealtime = nowRealtime2;
            nowRealtime2 = sysCpu2[4] * softirqtime;
            nowUptime = nowUptime2;
            nowUptime2 = sysCpu2[5] * softirqtime;
            softirqtime *= sysCpu2[6];
            this.mRelUserTime = (int) (usertime - this.mBaseUserTime);
            this.mRelSystemTime = (int) (systemtime - this.mBaseSystemTime);
            this.mRelIoWaitTime = (int) (nowRealtime2 - this.mBaseIoWaitTime);
            this.mRelIrqTime = (int) (nowUptime2 - this.mBaseIrqTime);
            this.mRelSoftIrqTime = (int) (softirqtime - this.mBaseSoftIrqTime);
            this.mRelIdleTime = (int) (nowWallTime2 - this.mBaseIdleTime);
            sysCpu = 1;
            this.mRelStatsAreGood = true;
            this.mBaseUserTime = usertime;
            this.mBaseSystemTime = systemtime;
            this.mBaseIoWaitTime = nowRealtime2;
            this.mBaseIrqTime = nowUptime2;
            this.mBaseSoftIrqTime = softirqtime;
            this.mBaseIdleTime = nowWallTime2;
        } else {
            nowUptime = nowUptime2;
            nowRealtime = nowRealtime2;
            nowWallTime = nowWallTime2;
            long[] jArr = sysCpu2;
            sysCpu = 1;
        }
        this.mLastSampleTime = this.mCurrentSampleTime;
        this.mCurrentSampleTime = nowUptime;
        this.mLastSampleRealTime = this.mCurrentSampleRealTime;
        this.mCurrentSampleRealTime = nowRealtime;
        this.mLastSampleWallTime = this.mCurrentSampleWallTime;
        this.mCurrentSampleWallTime = nowWallTime;
        ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        try {
            this.mCurPids = collectStats("/proc", -1, this.mFirst, this.mCurPids, this.mProcStats);
            float[] loadAverages = this.mLoadAverageData;
            if (Process.readProcFile("/proc/loadavg", LOAD_AVERAGE_FORMAT, null, null, loadAverages)) {
                float load1 = loadAverages[0];
                float load5 = loadAverages[sysCpu];
                float load15 = loadAverages[2];
                if (!(load1 == this.mLoad1 && load5 == this.mLoad5 && load15 == this.mLoad15)) {
                    this.mLoad1 = load1;
                    this.mLoad5 = load5;
                    this.mLoad15 = load15;
                    onLoadChanged(load1, load5, load15);
                }
            }
            this.mWorkingProcsSorted = false;
            this.mFirst = false;
        } finally {
            StrictMode.setThreadPolicy(savedPolicy);
        }
    }

    private int[] collectStats(String statsFile, int parentPid, boolean first, int[] curPids, ArrayList<Stats> allProcs) {
        int i;
        int[] pids;
        boolean pids2;
        int NP;
        int i2;
        int NS;
        int i3 = parentPid;
        ArrayList arrayList = allProcs;
        int[] pids3 = Process.getPids(statsFile, curPids);
        boolean z = false;
        int NP2 = pids3 == null ? 0 : pids3.length;
        int curStatsIndex = 0;
        int NS2 = allProcs.size();
        int i4 = 0;
        while (i4 < NP2) {
            int pid = pids3[i4];
            if (pid < 0) {
                NP2 = pid;
                i = i3;
                pids = pids3;
                pids2 = true;
                break;
            }
            Stats st = curStatsIndex < NS2 ? (Stats) arrayList.get(curStatsIndex) : null;
            int pid2;
            long j;
            if (st == null || st.pid != pid) {
                pid2 = pid;
                pids = pids3;
                NP = NP2;
                i2 = i4;
                NS = NS2;
                Stats st2 = st;
                if (st2 != null) {
                    i3 = pid2;
                    if (st2.pid > i3) {
                        arrayList = allProcs;
                    } else {
                        st2.rel_utime = 0;
                        st2.rel_stime = 0;
                        st2.rel_minfaults = 0;
                        st2.rel_majfaults = 0;
                        st2.removed = true;
                        st2.working = true;
                        arrayList = allProcs;
                        arrayList.remove(curStatsIndex);
                        i = parentPid;
                        NS2 = NS - 1;
                        i2--;
                    }
                } else {
                    arrayList = allProcs;
                    i3 = pid2;
                }
                i = parentPid;
                Stats st3 = new Stats(i3, i, this.mIncludeThreads);
                arrayList.add(curStatsIndex, st3);
                int curStatsIndex2 = curStatsIndex + 1;
                NS2 = NS + 1;
                String[] procStatsString = this.mProcessFullStatsStringData;
                long[] procStats = this.mProcessFullStatsData;
                st3.base_uptime = SystemClock.uptimeMillis();
                String path = st3.statFile.toString();
                if (Process.readProcFile(path, PROCESS_FULL_STATS_FORMAT, procStatsString, procStats, null)) {
                    st3.vsize = procStats[5];
                    st3.interesting = true;
                    st3.baseName = procStatsString[0];
                    st3.base_minfaults = procStats[1];
                    st3.base_majfaults = procStats[2];
                    j = procStats[3];
                    long j2 = this.mJiffyMillis;
                    st3.base_utime = j * j2;
                    st3.base_stime = procStats[4] * j2;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Skipping unknown process pid ");
                    stringBuilder.append(i3);
                    Slog.w(TAG, stringBuilder.toString());
                    st3.baseName = MediaStore.UNKNOWN_STRING;
                    st3.base_stime = 0;
                    st3.base_utime = 0;
                    st3.base_majfaults = 0;
                    st3.base_minfaults = 0;
                }
                if (i < 0) {
                    getName(st3, st3.cmdlineFile);
                    if (st3.threadStats != null) {
                        this.mCurThreadPids = collectStats(st3.threadsDir, i3, true, this.mCurThreadPids, st3.threadStats);
                    }
                } else {
                    if (st3.interesting) {
                        st3.name = st3.baseName;
                        st3.nameWidth = onMeasureProcessName(st3.name);
                    }
                }
                st3.rel_utime = 0;
                st3.rel_stime = 0;
                st3.rel_minfaults = 0;
                st3.rel_majfaults = 0;
                st3.added = true;
                if (!first && st3.interesting) {
                    st3.working = true;
                }
                curStatsIndex = curStatsIndex2;
            } else {
                st.added = z;
                st.working = z;
                int curStatsIndex3 = curStatsIndex + 1;
                if (st.interesting) {
                    long uptime = SystemClock.uptimeMillis();
                    long[] procStats2 = this.mProcessStatsData;
                    long uptime2 = uptime;
                    if (Process.readProcFile(st.statFile.toString(), PROCESS_STATS_FORMAT, null, procStats2, null)) {
                        uptime = procStats2[0];
                        long majfaults = procStats2[1];
                        long j3 = procStats2[2];
                        long minfaults = uptime;
                        j = this.mJiffyMillis;
                        NP = NP2;
                        long utime = j3 * j;
                        uptime = procStats2[3] * j;
                        i2 = i4;
                        NS = NS2;
                        if (utime == st.base_utime && uptime == st.base_stime) {
                            st.rel_utime = 0;
                            st.rel_stime = 0;
                            st.rel_minfaults = 0;
                            st.rel_majfaults = 0;
                            if (st.active) {
                                st.active = false;
                                pids = pids3;
                            } else {
                                pids = pids3;
                            }
                        } else {
                            boolean z2;
                            long uptime3;
                            if (st.active) {
                                z2 = true;
                            } else {
                                z2 = true;
                                st.active = true;
                            }
                            long[] jArr;
                            if (i3 < 0) {
                                getName(st, st.cmdlineFile);
                                if (st.threadStats != null) {
                                    uptime3 = uptime2;
                                    uptime2 = majfaults;
                                    majfaults = uptime;
                                    NS2 = st;
                                    pids = pids3;
                                    pids2 = true;
                                    this.mCurThreadPids = collectStats(st.threadsDir, pid, false, this.mCurThreadPids, st.threadStats);
                                } else {
                                    NS2 = st;
                                    pid2 = pid;
                                    jArr = procStats2;
                                    pids = pids3;
                                    pids2 = z2;
                                    uptime3 = uptime2;
                                    uptime2 = majfaults;
                                    majfaults = uptime;
                                }
                            } else {
                                NS2 = st;
                                pid2 = pid;
                                jArr = procStats2;
                                pids = pids3;
                                pids2 = z2;
                                uptime3 = uptime2;
                                uptime2 = majfaults;
                                majfaults = uptime;
                            }
                            NS2.rel_uptime = uptime3 - NS2.base_uptime;
                            NS2.base_uptime = uptime3;
                            NS2.rel_utime = (int) (utime - NS2.base_utime);
                            NS2.rel_stime = (int) (majfaults - NS2.base_stime);
                            NS2.base_utime = utime;
                            NS2.base_stime = majfaults;
                            NS2.rel_minfaults = (int) (minfaults - NS2.base_minfaults);
                            NS2.rel_majfaults = (int) (uptime2 - NS2.base_majfaults);
                            NS2.base_minfaults = minfaults;
                            NS2.base_majfaults = uptime2;
                            NS2.working = pids2;
                        }
                    } else {
                        pids = pids3;
                        NP = NP2;
                        i2 = i4;
                        NS = NS2;
                    }
                } else {
                    pids = pids3;
                    NP = NP2;
                    i2 = i4;
                    NS = NS2;
                    NS2 = st;
                }
                i = parentPid;
                arrayList = allProcs;
                curStatsIndex = curStatsIndex3;
                NS2 = NS;
            }
            i4 = i2 + 1;
            int[] iArr = curPids;
            i3 = i;
            pids3 = pids;
            NP2 = NP;
            z = false;
            String str = statsFile;
        }
        i = i3;
        pids = pids3;
        NP = NP2;
        i2 = i4;
        NS = NS2;
        pids2 = true;
        while (curStatsIndex < NS2) {
            Stats st4 = (Stats) arrayList.get(curStatsIndex);
            st4.rel_utime = 0;
            st4.rel_stime = 0;
            st4.rel_minfaults = 0;
            st4.rel_majfaults = 0;
            st4.removed = pids2;
            st4.working = pids2;
            arrayList.remove(curStatsIndex);
            NS2--;
        }
        return pids;
    }

    public long getCpuTimeForPid(int pid) {
        synchronized (this.mSinglePidStatsData) {
            String statFile = new StringBuilder();
            statFile.append("/proc/");
            statFile.append(pid);
            statFile.append("/stat");
            statFile = statFile.toString();
            long[] statsData = this.mSinglePidStatsData;
            if (Process.readProcFile(statFile, PROCESS_STATS_FORMAT, null, statsData, null)) {
                long j = this.mJiffyMillis * (statsData[2] + statsData[3]);
                return j;
            }
            return 0;
        }
    }

    public final int getLastUserTime() {
        return this.mRelUserTime;
    }

    public final int getLastSystemTime() {
        return this.mRelSystemTime;
    }

    public final int getLastIoWaitTime() {
        return this.mRelIoWaitTime;
    }

    public final int getLastIrqTime() {
        return this.mRelIrqTime;
    }

    public final int getLastSoftIrqTime() {
        return this.mRelSoftIrqTime;
    }

    public final int getLastIdleTime() {
        return this.mRelIdleTime;
    }

    public final boolean hasGoodLastStats() {
        return this.mRelStatsAreGood;
    }

    public final float getTotalCpuPercent() {
        int i = this.mRelUserTime;
        int i2 = this.mRelSystemTime;
        int i3 = i + i2;
        int i4 = this.mRelIrqTime;
        i3 = (i3 + i4) + this.mRelIdleTime;
        if (i3 <= 0) {
            return 0.0f;
        }
        return (((float) ((i + i2) + i4)) * 100.0f) / ((float) i3);
    }

    /* Access modifiers changed, original: final */
    public final void buildWorkingProcs() {
        if (!this.mWorkingProcsSorted) {
            this.mWorkingProcs.clear();
            int N = this.mProcStats.size();
            for (int i = 0; i < N; i++) {
                Stats stats = (Stats) this.mProcStats.get(i);
                if (stats.working) {
                    this.mWorkingProcs.add(stats);
                    if (stats.threadStats != null && stats.threadStats.size() > 1) {
                        stats.workingThreads.clear();
                        int M = stats.threadStats.size();
                        for (int j = 0; j < M; j++) {
                            Stats tstats = (Stats) stats.threadStats.get(j);
                            if (tstats.working) {
                                stats.workingThreads.add(tstats);
                            }
                        }
                        Collections.sort(stats.workingThreads, sLoadComparator);
                    }
                }
            }
            Collections.sort(this.mWorkingProcs, sLoadComparator);
            this.mWorkingProcsSorted = true;
        }
    }

    public final int countStats() {
        return this.mProcStats.size();
    }

    public final Stats getStats(int index) {
        return (Stats) this.mProcStats.get(index);
    }

    public final List<Stats> getStats(FilterStats filter) {
        ArrayList<Stats> statses = new ArrayList(this.mProcStats.size());
        int N = this.mProcStats.size();
        for (int p = 0; p < N; p++) {
            Stats stats = (Stats) this.mProcStats.get(p);
            if (filter.needed(stats)) {
                statses.add(stats);
            }
        }
        return statses;
    }

    @UnsupportedAppUsage
    public final int countWorkingStats() {
        buildWorkingProcs();
        return this.mWorkingProcs.size();
    }

    @UnsupportedAppUsage
    public final Stats getWorkingStats(int index) {
        return (Stats) this.mWorkingProcs.get(index);
    }

    public final String printCurrentLoad() {
        Writer sw = new StringWriter();
        PrintWriter pw = new FastPrintWriter(sw, false, 128);
        pw.print("Load: ");
        pw.print(this.mLoad1);
        String str = " / ";
        pw.print(str);
        pw.print(this.mLoad5);
        pw.print(str);
        pw.println(this.mLoad15);
        pw.flush();
        return sw.toString();
    }

    public final String printCurrentState(long now) {
        long percAwake;
        int i;
        int N;
        PrintWriter pw;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        buildWorkingProcs();
        Writer sw = new StringWriter();
        PrintWriter pw2 = new FastPrintWriter(sw, false, 1024);
        pw2.print("CPU usage from ");
        long j = this.mLastSampleTime;
        String str = "ms to ";
        if (now > j) {
            pw2.print(now - j);
            pw2.print(str);
            pw2.print(now - this.mCurrentSampleTime);
            pw2.print("ms ago");
        } else {
            pw2.print(j - now);
            pw2.print(str);
            pw2.print(this.mCurrentSampleTime - now);
            pw2.print("ms later");
        }
        pw2.print(" (");
        pw2.print(sdf.format(new Date(this.mLastSampleWallTime)));
        pw2.print(" to ");
        pw2.print(sdf.format(new Date(this.mCurrentSampleWallTime)));
        pw2.print(")");
        long sampleTime = this.mCurrentSampleTime - this.mLastSampleTime;
        long sampleRealTime = this.mCurrentSampleRealTime - this.mLastSampleRealTime;
        j = 0;
        if (sampleRealTime > 0) {
            j = (sampleTime * 100) / sampleRealTime;
        }
        long percAwake2 = j;
        if (percAwake2 != 100) {
            pw2.print(" with ");
            pw2.print(percAwake2);
            pw2.print("% awake");
        }
        pw2.println(":");
        int totalTime = ((((this.mRelUserTime + this.mRelSystemTime) + this.mRelIoWaitTime) + this.mRelIrqTime) + this.mRelSoftIrqTime) + this.mRelIdleTime;
        int N2 = this.mWorkingProcs.size();
        int i2 = 0;
        while (i2 < N2) {
            Stats st = (Stats) this.mWorkingProcs.get(i2);
            String str2 = st.added ? " +" : st.removed ? " -" : "  ";
            percAwake = percAwake2;
            Stats st2 = st;
            i = i2;
            N = N2;
            pw = pw2;
            printProcessCPU(pw2, str2, st.pid, st.name, (int) st.rel_uptime, st.rel_utime, st.rel_stime, 0, 0, 0, st.rel_minfaults, st.rel_majfaults);
            Stats st3 = st2;
            if (!(st3.removed || st3.workingThreads == null)) {
                int j2;
                int M;
                int M2 = st3.workingThreads.size();
                int j3 = 0;
                while (j3 < M2) {
                    Stats tst = (Stats) st3.workingThreads.get(j3);
                    str2 = tst.added ? "   +" : tst.removed ? "   -" : "    ";
                    j2 = j3;
                    M = M2;
                    Stats st4 = st3;
                    printProcessCPU(pw, str2, tst.pid, tst.name, (int) st3.rel_uptime, tst.rel_utime, tst.rel_stime, 0, 0, 0, 0, 0);
                    j3 = j2 + 1;
                    M2 = M;
                    st3 = st4;
                }
                j2 = j3;
                M = M2;
            }
            i2 = i + 1;
            N2 = N;
            pw2 = pw;
            percAwake2 = percAwake;
        }
        i = i2;
        N = N2;
        percAwake = percAwake2;
        pw = pw2;
        printProcessCPU(pw, "", -1, "TOTAL", totalTime, this.mRelUserTime, this.mRelSystemTime, this.mRelIoWaitTime, this.mRelIrqTime, this.mRelSoftIrqTime, 0, 0);
        pw.flush();
        return sw.toString();
    }

    private void printRatio(PrintWriter pw, long numerator, long denominator) {
        long thousands = (1000 * numerator) / denominator;
        long hundreds = thousands / 10;
        pw.print(hundreds);
        if (hundreds < 10) {
            long remainder = thousands - (10 * hundreds);
            if (remainder != 0) {
                pw.print(ClassUtils.PACKAGE_SEPARATOR_CHAR);
                pw.print(remainder);
            }
        }
    }

    private void printProcessCPU(PrintWriter pw, String prefix, int pid, String label, int totalTime, int user, int system, int iowait, int irq, int softIrq, int minFaults, int majFaults) {
        String str;
        PrintWriter printWriter = pw;
        int i = pid;
        int i2 = user;
        int i3 = system;
        int i4 = iowait;
        int i5 = irq;
        int i6 = softIrq;
        int i7 = minFaults;
        int i8 = majFaults;
        pw.print(prefix);
        int totalTime2 = totalTime == 0 ? 1 : totalTime;
        printRatio(pw, (long) ((((i2 + i3) + i4) + i5) + i6), (long) totalTime2);
        printWriter.print("% ");
        if (i >= 0) {
            printWriter.print(i);
            printWriter.print("/");
        }
        printWriter.print(label);
        printWriter.print(": ");
        PrintWriter printWriter2 = pw;
        printRatio(printWriter2, (long) i2, (long) totalTime2);
        printWriter.print("% user + ");
        printRatio(printWriter2, (long) i3, (long) totalTime2);
        printWriter.print("% kernel");
        String str2 = " + ";
        if (i4 > 0) {
            printWriter.print(str2);
            str = str2;
            printRatio(pw, (long) i4, (long) totalTime2);
            printWriter.print("% iowait");
        } else {
            str = str2;
        }
        if (i5 > 0) {
            printWriter.print(str);
            printRatio(pw, (long) i5, (long) totalTime2);
            printWriter.print("% irq");
        }
        if (i6 > 0) {
            printWriter.print(str);
            printRatio(pw, (long) i6, (long) totalTime2);
            printWriter.print("% softirq");
        }
        if (i7 > 0 || i8 > 0) {
            printWriter.print(" / faults:");
            String str3 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            if (i7 > 0) {
                printWriter.print(str3);
                printWriter.print(i7);
                printWriter.print(" minor");
            }
            if (i8 > 0) {
                printWriter.print(str3);
                printWriter.print(i8);
                printWriter.print(" major");
            }
        }
        pw.println();
    }

    private void getName(Stats st, String cmdlineFile) {
        String newName = st.name;
        if (st.name == null || st.name.equals("app_process") || st.name.equals("<pre-initialized>")) {
            String cmdName = ProcStatsUtil.readTerminatedProcFile(cmdlineFile, null);
            if (cmdName != null && cmdName.length() > 1) {
                newName = cmdName;
                int i = newName.lastIndexOf("/");
                if (i > 0 && i < newName.length() - 1) {
                    newName = newName.substring(i + 1);
                }
            }
            if (newName == null) {
                newName = st.baseName;
            }
        }
        if (st.name == null || !newName.equals(st.name)) {
            st.name = newName;
            st.nameWidth = onMeasureProcessName(st.name);
        }
    }
}
