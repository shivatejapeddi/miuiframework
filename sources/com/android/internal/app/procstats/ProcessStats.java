package com.android.internal.app.procstats;

import android.content.ComponentName;
import android.os.Debug;
import android.os.Debug.MemoryInfo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.DebugUtils;
import android.util.LongSparseArray;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.app.ProcessMap;
import com.android.internal.app.procstats.AssociationState.SourceState;
import dalvik.system.VMRuntime;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ProcessStats implements Parcelable {
    public static final int ADD_PSS_EXTERNAL = 3;
    public static final int ADD_PSS_EXTERNAL_SLOW = 4;
    public static final int ADD_PSS_INTERNAL_ALL_MEM = 1;
    public static final int ADD_PSS_INTERNAL_ALL_POLL = 2;
    public static final int ADD_PSS_INTERNAL_SINGLE = 0;
    public static final int ADJ_COUNT = 8;
    public static final int ADJ_MEM_FACTOR_COUNT = 4;
    public static final int ADJ_MEM_FACTOR_CRITICAL = 3;
    public static final int ADJ_MEM_FACTOR_LOW = 2;
    public static final int ADJ_MEM_FACTOR_MODERATE = 1;
    public static final int ADJ_MEM_FACTOR_NORMAL = 0;
    public static final int ADJ_NOTHING = -1;
    public static final int ADJ_SCREEN_MOD = 4;
    public static final int ADJ_SCREEN_OFF = 0;
    public static final int ADJ_SCREEN_ON = 4;
    public static final int[] ALL_MEM_ADJ = new int[]{0, 1, 2, 3};
    public static final int[] ALL_PROC_STATES = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};
    public static final int[] ALL_SCREEN_ADJ = new int[]{0, 4};
    public static final int[] BACKGROUND_PROC_STATES = new int[]{2, 3, 4, 8, 5, 6, 7};
    static final int[] BAD_TABLE = new int[0];
    public static long COMMIT_PERIOD = 10800000;
    public static long COMMIT_UPTIME_PERIOD = 3600000;
    public static final Creator<ProcessStats> CREATOR = new Creator<ProcessStats>() {
        public ProcessStats createFromParcel(Parcel in) {
            return new ProcessStats(in);
        }

        public ProcessStats[] newArray(int size) {
            return new ProcessStats[size];
        }
    };
    static final boolean DEBUG = false;
    static final boolean DEBUG_PARCEL = false;
    public static final int FLAG_COMPLETE = 1;
    public static final int FLAG_SHUTDOWN = 2;
    public static final int FLAG_SYSPROPS = 4;
    private static final long INVERSE_PROC_STATE_WARNING_MIN_INTERVAL_MS = 10000;
    private static final int MAGIC = 1347638356;
    public static final int[] NON_CACHED_PROC_STATES = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    public static final int[] OPTIONS = new int[]{1, 2, 4, 8, 14, 15};
    public static final String[] OPTIONS_STR = new String[]{"proc", "pkg-proc", "pkg-svc", "pkg-asc", "pkg-all", "all"};
    private static final int PARCEL_VERSION = 36;
    public static final int PSS_AVERAGE = 2;
    public static final int PSS_COUNT = 10;
    public static final int PSS_MAXIMUM = 3;
    public static final int PSS_MINIMUM = 1;
    public static final int PSS_RSS_AVERAGE = 8;
    public static final int PSS_RSS_MAXIMUM = 9;
    public static final int PSS_RSS_MINIMUM = 7;
    public static final int PSS_SAMPLE_COUNT = 0;
    public static final int PSS_USS_AVERAGE = 5;
    public static final int PSS_USS_MAXIMUM = 6;
    public static final int PSS_USS_MINIMUM = 4;
    public static final int REPORT_ALL = 15;
    public static final int REPORT_PKG_ASC_STATS = 8;
    public static final int REPORT_PKG_PROC_STATS = 2;
    public static final int REPORT_PKG_STATS = 14;
    public static final int REPORT_PKG_SVC_STATS = 4;
    public static final int REPORT_PROC_STATS = 1;
    public static final String SERVICE_NAME = "procstats";
    public static final int STATE_BACKUP = 4;
    public static final int STATE_CACHED_ACTIVITY = 11;
    public static final int STATE_CACHED_ACTIVITY_CLIENT = 12;
    public static final int STATE_CACHED_EMPTY = 13;
    public static final int STATE_COUNT = 14;
    public static final int STATE_HEAVY_WEIGHT = 8;
    public static final int STATE_HOME = 9;
    public static final int STATE_IMPORTANT_BACKGROUND = 3;
    public static final int STATE_IMPORTANT_FOREGROUND = 2;
    public static final int STATE_LAST_ACTIVITY = 10;
    public static final int STATE_NOTHING = -1;
    public static final int STATE_PERSISTENT = 0;
    public static final int STATE_RECEIVER = 7;
    public static final int STATE_SERVICE = 5;
    public static final int STATE_SERVICE_RESTARTING = 6;
    public static final int STATE_TOP = 1;
    public static final int SYS_MEM_USAGE_CACHED_AVERAGE = 2;
    public static final int SYS_MEM_USAGE_CACHED_MAXIMUM = 3;
    public static final int SYS_MEM_USAGE_CACHED_MINIMUM = 1;
    public static final int SYS_MEM_USAGE_COUNT = 16;
    public static final int SYS_MEM_USAGE_FREE_AVERAGE = 5;
    public static final int SYS_MEM_USAGE_FREE_MAXIMUM = 6;
    public static final int SYS_MEM_USAGE_FREE_MINIMUM = 4;
    public static final int SYS_MEM_USAGE_KERNEL_AVERAGE = 11;
    public static final int SYS_MEM_USAGE_KERNEL_MAXIMUM = 12;
    public static final int SYS_MEM_USAGE_KERNEL_MINIMUM = 10;
    public static final int SYS_MEM_USAGE_NATIVE_AVERAGE = 14;
    public static final int SYS_MEM_USAGE_NATIVE_MAXIMUM = 15;
    public static final int SYS_MEM_USAGE_NATIVE_MINIMUM = 13;
    public static final int SYS_MEM_USAGE_SAMPLE_COUNT = 0;
    public static final int SYS_MEM_USAGE_ZRAM_AVERAGE = 8;
    public static final int SYS_MEM_USAGE_ZRAM_MAXIMUM = 9;
    public static final int SYS_MEM_USAGE_ZRAM_MINIMUM = 7;
    public static final String TAG = "ProcessStats";
    private static final Pattern sPageTypeRegex = Pattern.compile("^Node\\s+(\\d+),.* zone\\s+(\\w+),.* type\\s+(\\w+)\\s+([\\s\\d]+?)\\s*$");
    ArrayMap<String, Integer> mCommonStringToIndex;
    public long mExternalPssCount;
    public long mExternalPssTime;
    public long mExternalSlowPssCount;
    public long mExternalSlowPssTime;
    public int mFlags;
    boolean mHasSwappedOutPss;
    ArrayList<String> mIndexToCommonString;
    public long mInternalAllMemPssCount;
    public long mInternalAllMemPssTime;
    public long mInternalAllPollPssCount;
    public long mInternalAllPollPssTime;
    public long mInternalSinglePssCount;
    public long mInternalSinglePssTime;
    public int mMemFactor;
    public final long[] mMemFactorDurations;
    private long mNextInverseProcStateWarningUptime;
    public final ProcessMap<LongSparseArray<PackageState>> mPackages;
    private final ArrayList<String> mPageTypeLabels;
    private final ArrayList<Integer> mPageTypeNodes;
    private final ArrayList<int[]> mPageTypeSizes;
    private final ArrayList<String> mPageTypeZones;
    public final ProcessMap<ProcessState> mProcesses;
    public String mReadError;
    boolean mRunning;
    String mRuntime;
    private int mSkippedInverseProcStateWarningCount;
    public long mStartTime;
    public final SysMemUsageTable mSysMemUsage;
    public final long[] mSysMemUsageArgs;
    public final SparseMappingTable mTableData;
    public long mTimePeriodEndRealtime;
    public long mTimePeriodEndUptime;
    public long mTimePeriodStartClock;
    public String mTimePeriodStartClockStr;
    public long mTimePeriodStartRealtime;
    public long mTimePeriodStartUptime;
    public final ArrayList<SourceState> mTrackingAssociations;

    public static final class PackageState {
        public final ArrayMap<String, AssociationState> mAssociations = new ArrayMap();
        public final String mPackageName;
        public final ProcessStats mProcessStats;
        public final ArrayMap<String, ProcessState> mProcesses = new ArrayMap();
        public final ArrayMap<String, ServiceState> mServices = new ArrayMap();
        public final int mUid;
        public final long mVersionCode;

        public PackageState(ProcessStats procStats, String packageName, int uid, long versionCode) {
            this.mProcessStats = procStats;
            this.mUid = uid;
            this.mPackageName = packageName;
            this.mVersionCode = versionCode;
        }

        public AssociationState getAssociationStateLocked(ProcessState proc, String className) {
            AssociationState as = (AssociationState) this.mAssociations.get(className);
            if (as != null) {
                if (proc != null) {
                    as.setProcess(proc);
                }
                return as;
            }
            as = new AssociationState(this.mProcessStats, this, className, proc.getName(), proc);
            this.mAssociations.put(className, as);
            return as;
        }

        public void writeToProto(ProtoOutputStream proto, long fieldId, long now, int section) {
            int is;
            ProtoOutputStream protoOutputStream = proto;
            long token = proto.start(fieldId);
            protoOutputStream.write(1138166333441L, this.mPackageName);
            protoOutputStream.write(1120986464258L, this.mUid);
            protoOutputStream.write(1112396529667L, this.mVersionCode);
            if ((section & 2) != 0) {
                for (int ip = 0; ip < this.mProcesses.size(); ip++) {
                    ((ProcessState) this.mProcesses.valueAt(ip)).writeToProto(proto, 2246267895812L, (String) this.mProcesses.keyAt(ip), this.mUid, now);
                }
            }
            if ((section & 4) != 0) {
                for (is = 0; is < this.mServices.size(); is++) {
                    ((ServiceState) this.mServices.valueAt(is)).writeToProto(proto, 2246267895813L, now);
                }
            }
            if ((section & 8) != 0) {
                for (is = 0; is < this.mAssociations.size(); is++) {
                    ((AssociationState) this.mAssociations.valueAt(is)).writeToProto(proto, 2246267895814L, now);
                }
            }
            protoOutputStream.end(token);
        }
    }

    public static final class ProcessDataCollection {
        public long avgPss;
        public long avgRss;
        public long avgUss;
        public long maxPss;
        public long maxRss;
        public long maxUss;
        final int[] memStates;
        public long minPss;
        public long minRss;
        public long minUss;
        public long numPss;
        final int[] procStates;
        final int[] screenStates;
        public long totalTime;

        public ProcessDataCollection(int[] _screenStates, int[] _memStates, int[] _procStates) {
            this.screenStates = _screenStates;
            this.memStates = _memStates;
            this.procStates = _procStates;
        }

        /* Access modifiers changed, original: 0000 */
        public void print(PrintWriter pw, long overallTime, boolean full) {
            if (this.totalTime > overallTime) {
                pw.print("*");
            }
            DumpUtils.printPercent(pw, ((double) this.totalTime) / ((double) overallTime));
            if (this.numPss > 0) {
                pw.print(" (");
                DebugUtils.printSizeValue(pw, this.minPss * 1024);
                String str = "-";
                pw.print(str);
                DebugUtils.printSizeValue(pw, this.avgPss * 1024);
                pw.print(str);
                DebugUtils.printSizeValue(pw, this.maxPss * 1024);
                String str2 = "/";
                pw.print(str2);
                DebugUtils.printSizeValue(pw, this.minUss * 1024);
                pw.print(str);
                DebugUtils.printSizeValue(pw, this.avgUss * 1024);
                pw.print(str);
                DebugUtils.printSizeValue(pw, this.maxUss * 1024);
                pw.print(str2);
                DebugUtils.printSizeValue(pw, this.minRss * 1024);
                pw.print(str);
                DebugUtils.printSizeValue(pw, this.avgRss * 1024);
                pw.print(str);
                DebugUtils.printSizeValue(pw, this.maxRss * 1024);
                if (full) {
                    pw.print(" over ");
                    pw.print(this.numPss);
                }
                pw.print(")");
            }
        }
    }

    public static final class ProcessStateHolder {
        public final long appVersion;
        public PackageState pkg;
        public ProcessState state;

        public ProcessStateHolder(long _appVersion) {
            this.appVersion = _appVersion;
        }
    }

    public static class TotalMemoryUseCollection {
        public boolean hasSwappedOutPss;
        final int[] memStates;
        public long[] processStatePss = new long[14];
        public int[] processStateSamples = new int[14];
        public long[] processStateTime = new long[14];
        public double[] processStateWeight = new double[14];
        final int[] screenStates;
        public double sysMemCachedWeight;
        public double sysMemFreeWeight;
        public double sysMemKernelWeight;
        public double sysMemNativeWeight;
        public int sysMemSamples;
        public long[] sysMemUsage = new long[16];
        public double sysMemZRamWeight;
        public long totalTime;

        public TotalMemoryUseCollection(int[] _screenStates, int[] _memStates) {
            this.screenStates = _screenStates;
            this.memStates = _memStates;
        }
    }

    public ProcessStats(boolean running) {
        this.mPackages = new ProcessMap();
        this.mProcesses = new ProcessMap();
        this.mTrackingAssociations = new ArrayList();
        this.mMemFactorDurations = new long[8];
        this.mMemFactor = -1;
        this.mTableData = new SparseMappingTable();
        this.mSysMemUsageArgs = new long[16];
        this.mSysMemUsage = new SysMemUsageTable(this.mTableData);
        this.mPageTypeNodes = new ArrayList();
        this.mPageTypeZones = new ArrayList();
        this.mPageTypeLabels = new ArrayList();
        this.mPageTypeSizes = new ArrayList();
        this.mRunning = running;
        reset();
        if (running) {
            MemoryInfo info = new MemoryInfo();
            Debug.getMemoryInfo(Process.myPid(), info);
            this.mHasSwappedOutPss = info.hasSwappedOutPss();
        }
    }

    public ProcessStats(Parcel in) {
        this.mPackages = new ProcessMap();
        this.mProcesses = new ProcessMap();
        this.mTrackingAssociations = new ArrayList();
        this.mMemFactorDurations = new long[8];
        this.mMemFactor = -1;
        this.mTableData = new SparseMappingTable();
        this.mSysMemUsageArgs = new long[16];
        this.mSysMemUsage = new SysMemUsageTable(this.mTableData);
        this.mPageTypeNodes = new ArrayList();
        this.mPageTypeZones = new ArrayList();
        this.mPageTypeLabels = new ArrayList();
        this.mPageTypeSizes = new ArrayList();
        reset();
        readFromParcel(in);
    }

    public void add(ProcessStats other) {
        int iu;
        int NASCS;
        ProcessState otherProc;
        int NSRVS;
        ProcessStats otherState;
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = other.mPackages.getMap();
        int ip = 0;
        while (ip < pkgMap.size()) {
            SparseArray<LongSparseArray<PackageState>> uids;
            String pkgName = (String) pkgMap.keyAt(ip);
            SparseArray<LongSparseArray<PackageState>> uids2 = (SparseArray) pkgMap.valueAt(ip);
            iu = 0;
            while (iu < uids2.size()) {
                int iv;
                ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap2;
                LongSparseArray<PackageState> versions;
                int uid = uids2.keyAt(iu);
                LongSparseArray<PackageState> versions2 = (LongSparseArray) uids2.valueAt(iu);
                int iv2 = 0;
                while (iv2 < versions2.size()) {
                    int NASCS2;
                    long vers;
                    PackageState otherState2;
                    int NSRVS2;
                    long vers2 = versions2.keyAt(iv2);
                    PackageState otherState3 = (PackageState) versions2.valueAt(iv2);
                    int NPROCS = otherState3.mProcesses.size();
                    int NSRVS3 = otherState3.mServices.size();
                    NASCS = otherState3.mAssociations.size();
                    iv = iv2;
                    iv2 = 0;
                    while (iv2 < NPROCS) {
                        int NPROCS2;
                        int NASCS3 = NASCS;
                        otherProc = (ProcessState) otherState3.mProcesses.valueAt(iv2);
                        int NSRVS4 = NSRVS3;
                        ProcessState pkgMap3;
                        if (otherProc.getCommonProcess() != otherProc) {
                            NSRVS3 = NASCS3;
                            pkgMap2 = pkgMap;
                            pkgMap3 = otherProc;
                            uids = uids2;
                            NSRVS = NSRVS4;
                            versions = versions2;
                            NASCS2 = NSRVS3;
                            NPROCS2 = NPROCS;
                            vers = vers2;
                            otherState2 = otherState3;
                            otherProc = getProcessStateLocked(pkgName, uid, vers2, otherProc.getName());
                            if (otherProc.getCommonProcess() == otherProc) {
                                otherProc.setMultiPackage(true);
                                long now = SystemClock.uptimeMillis();
                                vers2 = vers;
                                otherState3 = getPackageStateLocked(pkgName, uid, vers2);
                                otherProc = otherProc.clone(now);
                                vers = now;
                                otherState3.mProcesses.put(otherProc.getName(), otherProc);
                            } else {
                                vers2 = vers;
                            }
                            otherProc.add(pkgMap3);
                        } else {
                            NPROCS2 = NPROCS;
                            otherState2 = otherState3;
                            uids = uids2;
                            NSRVS = NSRVS4;
                            versions = versions2;
                            NASCS2 = NASCS3;
                            pkgMap2 = pkgMap;
                            pkgMap3 = otherProc;
                        }
                        iv2++;
                        otherState3 = otherState2;
                        NSRVS3 = NSRVS;
                        NASCS = NASCS2;
                        pkgMap = pkgMap2;
                        versions2 = versions;
                        uids2 = uids;
                        NPROCS = NPROCS2;
                        otherState = other;
                    }
                    otherState2 = otherState3;
                    pkgMap2 = pkgMap;
                    uids = uids2;
                    versions = versions2;
                    NASCS2 = NASCS;
                    NSRVS = NSRVS3;
                    int isvc = 0;
                    while (isvc < NSRVS) {
                        ServiceState otherSvc = (ServiceState) otherState2.mServices.valueAt(isvc);
                        vers = vers2;
                        NSRVS2 = NSRVS;
                        ServiceState otherSvc2 = otherSvc;
                        getServiceStateLocked(pkgName, uid, vers2, otherSvc.getProcessName(), otherSvc.getName()).add(otherSvc2);
                        isvc++;
                        NSRVS = NSRVS2;
                    }
                    vers = vers2;
                    NSRVS2 = NSRVS;
                    for (isvc = 0; isvc < NASCS2; isvc++) {
                        AssociationState otherAsc = (AssociationState) otherState2.mAssociations.valueAt(isvc);
                        getAssociationStateLocked(pkgName, uid, vers, otherAsc.getProcessName(), otherAsc.getName()).add(otherAsc);
                    }
                    iv2 = iv + 1;
                    otherState = other;
                    pkgMap = pkgMap2;
                    versions2 = versions;
                    uids2 = uids;
                }
                iv = iv2;
                pkgMap2 = pkgMap;
                uids = uids2;
                versions = versions2;
                iu++;
                otherState = other;
            }
            uids = uids2;
            ip++;
            otherState = other;
        }
        otherState = other;
        ArrayMap<String, SparseArray<ProcessState>> procMap = otherState.mProcesses.getMap();
        for (ip = 0; ip < procMap.size(); ip++) {
            SparseArray<ProcessState> uids3 = (SparseArray) procMap.valueAt(ip);
            NSRVS = 0;
            while (NSRVS < uids3.size()) {
                ArrayMap<String, SparseArray<ProcessState>> procMap2;
                iu = uids3.keyAt(NSRVS);
                ProcessState otherProc2 = (ProcessState) uids3.valueAt(NSRVS);
                String name = otherProc2.getName();
                String pkg = otherProc2.getPackage();
                long vers3 = otherProc2.getVersion();
                ProcessState thisProc = (ProcessState) this.mProcesses.get(name, iu);
                String procMap3;
                if (thisProc == null) {
                    long vers4 = vers3;
                    procMap2 = procMap;
                    procMap3 = pkg;
                    otherProc = new ProcessState(this, pkg, iu, vers3, name);
                    this.mProcesses.put(name, iu, otherProc);
                    PackageState thisState = getPackageStateLocked(procMap3, iu, vers4);
                    if (!thisState.mProcesses.containsKey(name)) {
                        thisState.mProcesses.put(name, otherProc);
                    }
                } else {
                    procMap2 = procMap;
                    procMap3 = pkg;
                    otherProc = thisProc;
                }
                otherProc.add(otherProc2);
                NSRVS++;
                procMap = procMap2;
            }
        }
        for (NASCS = 0; NASCS < 8; NASCS++) {
            long[] jArr = this.mMemFactorDurations;
            jArr[NASCS] = jArr[NASCS] + otherState.mMemFactorDurations[NASCS];
        }
        this.mSysMemUsage.mergeStats(otherState.mSysMemUsage);
        long j = otherState.mTimePeriodStartClock;
        if (j < this.mTimePeriodStartClock) {
            this.mTimePeriodStartClock = j;
            this.mTimePeriodStartClockStr = otherState.mTimePeriodStartClockStr;
        }
        this.mTimePeriodEndRealtime += otherState.mTimePeriodEndRealtime - otherState.mTimePeriodStartRealtime;
        this.mTimePeriodEndUptime += otherState.mTimePeriodEndUptime - otherState.mTimePeriodStartUptime;
        this.mInternalSinglePssCount += otherState.mInternalSinglePssCount;
        this.mInternalSinglePssTime += otherState.mInternalSinglePssTime;
        this.mInternalAllMemPssCount += otherState.mInternalAllMemPssCount;
        this.mInternalAllMemPssTime += otherState.mInternalAllMemPssTime;
        this.mInternalAllPollPssCount += otherState.mInternalAllPollPssCount;
        this.mInternalAllPollPssTime += otherState.mInternalAllPollPssTime;
        this.mExternalPssCount += otherState.mExternalPssCount;
        this.mExternalPssTime += otherState.mExternalPssTime;
        this.mExternalSlowPssCount += otherState.mExternalSlowPssCount;
        this.mExternalSlowPssTime += otherState.mExternalSlowPssTime;
        this.mHasSwappedOutPss |= otherState.mHasSwappedOutPss;
    }

    public void addSysMemUsage(long cachedMem, long freeMem, long zramMem, long kernelMem, long nativeMem) {
        int state = this.mMemFactor;
        if (state != -1) {
            state *= 14;
            this.mSysMemUsageArgs[0] = 1;
            for (int i = 0; i < 3; i++) {
                long[] jArr = this.mSysMemUsageArgs;
                jArr[i + 1] = cachedMem;
                jArr[i + 4] = freeMem;
                jArr[i + 7] = zramMem;
                jArr[i + 10] = kernelMem;
                jArr[i + 13] = nativeMem;
            }
            this.mSysMemUsage.mergeStats(state, this.mSysMemUsageArgs, 0);
        }
    }

    public void computeTotalMemoryUse(TotalMemoryUseCollection data, long now) {
        int i;
        int is;
        long j;
        TotalMemoryUseCollection totalMemoryUseCollection = data;
        long j2 = now;
        totalMemoryUseCollection.totalTime = 0;
        int i2 = 0;
        while (true) {
            i = 0;
            if (i2 >= 14) {
                break;
            }
            totalMemoryUseCollection.processStateWeight[i2] = 0.0d;
            totalMemoryUseCollection.processStatePss[i2] = 0;
            totalMemoryUseCollection.processStateTime[i2] = 0;
            totalMemoryUseCollection.processStateSamples[i2] = 0;
            i2++;
        }
        for (i2 = 0; i2 < 16; i2++) {
            totalMemoryUseCollection.sysMemUsage[i2] = 0;
        }
        totalMemoryUseCollection.sysMemCachedWeight = 0.0d;
        totalMemoryUseCollection.sysMemFreeWeight = 0.0d;
        totalMemoryUseCollection.sysMemZRamWeight = 0.0d;
        totalMemoryUseCollection.sysMemKernelWeight = 0.0d;
        totalMemoryUseCollection.sysMemNativeWeight = 0.0d;
        totalMemoryUseCollection.sysMemSamples = 0;
        long[] totalMemUsage = this.mSysMemUsage.getTotalMemUsage();
        int is2 = 0;
        while (is2 < totalMemoryUseCollection.screenStates.length) {
            i2 = 0;
            while (i2 < totalMemoryUseCollection.memStates.length) {
                long[] totalMemUsage2;
                int memBucket = totalMemoryUseCollection.screenStates[is2] + totalMemoryUseCollection.memStates[i2];
                int stateBucket = memBucket * 14;
                long memTime = this.mMemFactorDurations[memBucket];
                if (this.mMemFactor == memBucket) {
                    memTime += j2 - this.mStartTime;
                }
                totalMemoryUseCollection.totalTime += memTime;
                int sysKey = this.mSysMemUsage.getKey((byte) stateBucket);
                long[] longs = totalMemUsage;
                int idx = 0;
                if (sysKey != -1) {
                    long[] tmpLongs = this.mSysMemUsage.getArrayForKey(sysKey);
                    int tmpIndex = SparseMappingTable.getIndexFromKey(sysKey);
                    if (tmpLongs[tmpIndex + 0] >= 3) {
                        totalMemUsage2 = totalMemUsage;
                        SysMemUsageTable.mergeSysMemUsage(totalMemoryUseCollection.sysMemUsage, i, longs, 0);
                        longs = tmpLongs;
                        idx = tmpIndex;
                    } else {
                        totalMemUsage2 = totalMemUsage;
                    }
                } else {
                    totalMemUsage2 = totalMemUsage;
                }
                is = is2;
                totalMemoryUseCollection.sysMemCachedWeight += ((double) longs[idx + 2]) * ((double) memTime);
                totalMemoryUseCollection.sysMemFreeWeight += ((double) longs[idx + 5]) * ((double) memTime);
                totalMemoryUseCollection.sysMemZRamWeight += ((double) longs[idx + 8]) * ((double) memTime);
                totalMemoryUseCollection.sysMemKernelWeight += ((double) longs[idx + 11]) * ((double) memTime);
                totalMemoryUseCollection.sysMemNativeWeight += ((double) longs[idx + 14]) * ((double) memTime);
                totalMemoryUseCollection.sysMemSamples = (int) (((long) totalMemoryUseCollection.sysMemSamples) + longs[idx + 0]);
                i2++;
                j2 = now;
                totalMemUsage = totalMemUsage2;
                is2 = is;
                i = 0;
            }
            is2++;
            j2 = now;
            i = 0;
        }
        is = is2;
        totalMemoryUseCollection.hasSwappedOutPss = this.mHasSwappedOutPss;
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        for (int iproc = 0; iproc < procMap.size(); iproc++) {
            SparseArray<ProcessState> uids = (SparseArray) procMap.valueAt(iproc);
            for (is2 = 0; is2 < uids.size(); is2++) {
                ((ProcessState) uids.valueAt(is2)).aggregatePss(totalMemoryUseCollection, now);
            }
            j = now;
        }
        j = now;
    }

    public void reset() {
        resetCommon();
        this.mPackages.getMap().clear();
        this.mProcesses.getMap().clear();
        this.mMemFactor = -1;
        this.mStartTime = 0;
    }

    public void resetSafely() {
        int ip;
        int iu;
        resetCommon();
        long now = SystemClock.uptimeMillis();
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        for (int ip2 = procMap.size() - 1; ip2 >= 0; ip2--) {
            SparseArray<ProcessState> uids = (SparseArray) procMap.valueAt(ip2);
            for (int iu2 = uids.size() - 1; iu2 >= 0; iu2--) {
                ((ProcessState) uids.valueAt(iu2)).tmpNumInUse = 0;
            }
        }
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
        for (ip = pkgMap.size() - 1; ip >= 0; ip--) {
            SparseArray<LongSparseArray<PackageState>> uids2 = (SparseArray) pkgMap.valueAt(ip);
            for (iu = uids2.size() - 1; iu >= 0; iu--) {
                LongSparseArray<PackageState> vpkgs = (LongSparseArray) uids2.valueAt(iu);
                for (int iv = vpkgs.size() - 1; iv >= 0; iv--) {
                    int iproc;
                    PackageState pkgState = (PackageState) vpkgs.valueAt(iv);
                    for (iproc = pkgState.mProcesses.size() - 1; iproc >= 0; iproc--) {
                        ProcessState ps = (ProcessState) pkgState.mProcesses.valueAt(iproc);
                        if (ps.isInUse()) {
                            ps.resetSafely(now);
                            ProcessState commonProcess = ps.getCommonProcess();
                            commonProcess.tmpNumInUse++;
                            ps.getCommonProcess().tmpFoundSubProc = ps;
                        } else {
                            ((ProcessState) pkgState.mProcesses.valueAt(iproc)).makeDead();
                            pkgState.mProcesses.removeAt(iproc);
                        }
                    }
                    for (iproc = pkgState.mServices.size() - 1; iproc >= 0; iproc--) {
                        ServiceState ss = (ServiceState) pkgState.mServices.valueAt(iproc);
                        if (ss.isInUse()) {
                            ss.resetSafely(now);
                        } else {
                            pkgState.mServices.removeAt(iproc);
                        }
                    }
                    for (iproc = pkgState.mAssociations.size() - 1; iproc >= 0; iproc--) {
                        AssociationState as = (AssociationState) pkgState.mAssociations.valueAt(iproc);
                        if (as.isInUse()) {
                            as.resetSafely(now);
                        } else {
                            pkgState.mAssociations.removeAt(iproc);
                        }
                    }
                    if (pkgState.mProcesses.size() <= 0 && pkgState.mServices.size() <= 0 && pkgState.mAssociations.size() <= 0) {
                        vpkgs.removeAt(iv);
                    }
                }
                if (vpkgs.size() <= 0) {
                    uids2.removeAt(iu);
                }
            }
            if (uids2.size() <= 0) {
                pkgMap.removeAt(ip);
            }
        }
        for (ip = procMap.size() - 1; ip >= 0; ip--) {
            SparseArray<ProcessState> uids3 = (SparseArray) procMap.valueAt(ip);
            for (iu = uids3.size() - 1; iu >= 0; iu--) {
                ProcessState ps2 = (ProcessState) uids3.valueAt(iu);
                if (!ps2.isInUse() && ps2.tmpNumInUse <= 0) {
                    ps2.makeDead();
                    uids3.removeAt(iu);
                } else if (!ps2.isActive() && ps2.isMultiPackage() && ps2.tmpNumInUse == 1) {
                    ps2 = ps2.tmpFoundSubProc;
                    ps2.makeStandalone();
                    uids3.setValueAt(iu, ps2);
                } else {
                    ps2.resetSafely(now);
                }
            }
            if (uids3.size() <= 0) {
                procMap.removeAt(ip);
            }
        }
        this.mStartTime = now;
    }

    private void resetCommon() {
        this.mTimePeriodStartClock = System.currentTimeMillis();
        buildTimePeriodStartClockStr();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        this.mTimePeriodEndRealtime = elapsedRealtime;
        this.mTimePeriodStartRealtime = elapsedRealtime;
        elapsedRealtime = SystemClock.uptimeMillis();
        this.mTimePeriodEndUptime = elapsedRealtime;
        this.mTimePeriodStartUptime = elapsedRealtime;
        this.mInternalSinglePssCount = 0;
        this.mInternalSinglePssTime = 0;
        this.mInternalAllMemPssCount = 0;
        this.mInternalAllMemPssTime = 0;
        this.mInternalAllPollPssCount = 0;
        this.mInternalAllPollPssTime = 0;
        this.mExternalPssCount = 0;
        this.mExternalPssTime = 0;
        this.mExternalSlowPssCount = 0;
        this.mExternalSlowPssTime = 0;
        this.mTableData.reset();
        Arrays.fill(this.mMemFactorDurations, 0);
        this.mSysMemUsage.resetTable();
        this.mStartTime = 0;
        this.mReadError = null;
        this.mFlags = 0;
        evaluateSystemProperties(true);
        updateFragmentation();
    }

    public boolean evaluateSystemProperties(boolean update) {
        boolean changed = false;
        String runtime = SystemProperties.get("persist.sys.dalvik.vm.lib.2", VMRuntime.getRuntime().vmLibrary());
        if (!Objects.equals(runtime, this.mRuntime)) {
            changed = true;
            if (update) {
                this.mRuntime = runtime;
            }
        }
        return changed;
    }

    private void buildTimePeriodStartClockStr() {
        this.mTimePeriodStartClockStr = DateFormat.format((CharSequence) "yyyy-MM-dd-HH-mm-ss", this.mTimePeriodStartClock).toString();
    }

    public void updateFragmentation() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/pagetypeinfo"));
            Matcher matcher = sPageTypeRegex.matcher("");
            this.mPageTypeNodes.clear();
            this.mPageTypeZones.clear();
            this.mPageTypeLabels.clear();
            this.mPageTypeSizes.clear();
            while (true) {
                String line = reader.readLine();
                if (line == null) {
                    try {
                        break;
                    } catch (IOException e) {
                    }
                } else {
                    matcher.reset(line);
                    if (matcher.matches()) {
                        Integer node = Integer.valueOf(matcher.group(1), 10);
                        if (node != null) {
                            this.mPageTypeNodes.add(node);
                            this.mPageTypeZones.add(matcher.group(2));
                            this.mPageTypeLabels.add(matcher.group(3));
                            this.mPageTypeSizes.add(splitAndParseNumbers(matcher.group(4)));
                        }
                    }
                }
            }
            reader.close();
        } catch (IOException e2) {
            this.mPageTypeNodes.clear();
            this.mPageTypeZones.clear();
            this.mPageTypeLabels.clear();
            this.mPageTypeSizes.clear();
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e3) {
                }
            }
        } catch (Throwable th) {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e4) {
                }
            }
        }
    }

    private static int[] splitAndParseNumbers(String s) {
        boolean digit = false;
        int count = 0;
        int N = s.length();
        for (int i = 0; i < N; i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9') {
                digit = false;
            } else if (!digit) {
                digit = true;
                count++;
            }
        }
        int[] result = new int[count];
        int p = 0;
        int val = 0;
        for (int i2 = 0; i2 < N; i2++) {
            char c2 = s.charAt(i2);
            if (c2 < '0' || c2 > '9') {
                if (digit) {
                    digit = false;
                    int p2 = p + 1;
                    result[p] = val;
                    p = p2;
                }
            } else if (digit) {
                val = (val * 10) + (c2 - 48);
            } else {
                digit = true;
                val = c2 - 48;
            }
        }
        if (count > 0) {
            result[count - 1] = val;
        }
        return result;
    }

    private void writeCompactedLongArray(Parcel out, long[] array, int num) {
        for (int i = 0; i < num; i++) {
            long val = array[i];
            if (val < 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Time val negative: ");
                stringBuilder.append(val);
                Slog.w(TAG, stringBuilder.toString());
                val = 0;
            }
            if (val <= 2147483647L) {
                out.writeInt((int) val);
            } else {
                int bottom = (int) (4294967295L & val);
                out.writeInt(~((int) (2147483647L & (val >> 32))));
                out.writeInt(bottom);
            }
        }
    }

    private void readCompactedLongArray(Parcel in, int version, long[] array, int num) {
        if (version <= 10) {
            in.readLongArray(array);
            return;
        }
        int alen = array.length;
        if (num <= alen) {
            int i = 0;
            while (i < num) {
                int val = in.readInt();
                if (val >= 0) {
                    array[i] = (long) val;
                } else {
                    array[i] = (((long) (~val)) << 32) | ((long) in.readInt());
                }
                i++;
            }
            while (i < alen) {
                array[i] = 0;
                i++;
            }
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("bad array lengths: got ");
        stringBuilder.append(num);
        stringBuilder.append(" array is ");
        stringBuilder.append(alen);
        throw new RuntimeException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public void writeCommonString(Parcel out, String name) {
        Integer index = (Integer) this.mCommonStringToIndex.get(name);
        if (index != null) {
            out.writeInt(index.intValue());
            return;
        }
        index = Integer.valueOf(this.mCommonStringToIndex.size());
        this.mCommonStringToIndex.put(name, index);
        out.writeInt(~index.intValue());
        out.writeString(name);
    }

    /* Access modifiers changed, original: 0000 */
    public String readCommonString(Parcel in, int version) {
        if (version <= 9) {
            return in.readString();
        }
        int index = in.readInt();
        if (index >= 0) {
            return (String) this.mIndexToCommonString.get(index);
        }
        index = ~index;
        String name = in.readString();
        while (this.mIndexToCommonString.size() <= index) {
            this.mIndexToCommonString.add(null);
        }
        this.mIndexToCommonString.set(index, name);
        return name;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        writeToParcel(out, SystemClock.uptimeMillis(), flags);
    }

    public void writeToParcel(Parcel out, long now, int flags) {
        int ip;
        int NUID;
        SparseArray<LongSparseArray<PackageState>> uids;
        int NUID2;
        int iu;
        LongSparseArray<PackageState> vpkgs;
        int NVERS;
        int iv;
        int NPROCS;
        int NSRVS;
        int NPROC;
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap;
        int NPKG;
        Parcel parcel = out;
        long j = now;
        parcel.writeInt(MAGIC);
        parcel.writeInt(36);
        parcel.writeInt(14);
        parcel.writeInt(8);
        parcel.writeInt(10);
        parcel.writeInt(16);
        parcel.writeInt(4096);
        this.mCommonStringToIndex = new ArrayMap(this.mProcesses.size());
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        int NPROC2 = procMap.size();
        for (ip = 0; ip < NPROC2; ip++) {
            SparseArray<ProcessState> uids2 = (SparseArray) procMap.valueAt(ip);
            NUID = uids2.size();
            for (int iu2 = 0; iu2 < NUID; iu2++) {
                ((ProcessState) uids2.valueAt(iu2)).commitStateTime(j);
            }
        }
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap2 = this.mPackages.getMap();
        int NPKG2 = pkgMap2.size();
        for (NUID = 0; NUID < NPKG2; NUID++) {
            int NUID3;
            uids = (SparseArray) pkgMap2.valueAt(NUID);
            NUID2 = uids.size();
            for (iu = 0; iu < NUID2; iu++) {
                LongSparseArray<PackageState> vpkgs2;
                vpkgs = (LongSparseArray) uids.valueAt(iu);
                NVERS = vpkgs.size();
                iv = 0;
                while (iv < NVERS) {
                    PackageState pkgState = (PackageState) vpkgs.valueAt(iv);
                    SparseArray<LongSparseArray<PackageState>> uids3 = uids;
                    uids = pkgState.mProcesses.size();
                    NUID3 = NUID2;
                    NUID2 = 0;
                    while (NUID2 < uids) {
                        NPROCS = uids;
                        ProcessState uids4 = (ProcessState) pkgState.mProcesses.valueAt(NUID2);
                        vpkgs2 = vpkgs;
                        if (uids4.getCommonProcess() != uids4) {
                            uids4.commitStateTime(j);
                        }
                        NUID2++;
                        uids = NPROCS;
                        vpkgs = vpkgs2;
                    }
                    NPROCS = uids;
                    vpkgs2 = vpkgs;
                    uids = pkgState.mServices.size();
                    for (NUID2 = 0; NUID2 < uids; NUID2++) {
                        ((ServiceState) pkgState.mServices.valueAt(NUID2)).commitStateTime(j);
                    }
                    NUID2 = pkgState.mAssociations.size();
                    int iasc = 0;
                    while (iasc < NUID2) {
                        NSRVS = uids;
                        ((AssociationState) pkgState.mAssociations.valueAt(iasc)).commitStateTime(j);
                        iasc++;
                        uids = NSRVS;
                    }
                    NSRVS = uids;
                    iv++;
                    uids = uids3;
                    NUID2 = NUID3;
                    vpkgs = vpkgs2;
                }
                NUID3 = NUID2;
                vpkgs2 = vpkgs;
            }
            NUID3 = NUID2;
        }
        parcel.writeLong(this.mTimePeriodStartClock);
        parcel.writeLong(this.mTimePeriodStartRealtime);
        parcel.writeLong(this.mTimePeriodEndRealtime);
        parcel.writeLong(this.mTimePeriodStartUptime);
        parcel.writeLong(this.mTimePeriodEndUptime);
        parcel.writeLong(this.mInternalSinglePssCount);
        parcel.writeLong(this.mInternalSinglePssTime);
        parcel.writeLong(this.mInternalAllMemPssCount);
        parcel.writeLong(this.mInternalAllMemPssTime);
        parcel.writeLong(this.mInternalAllPollPssCount);
        parcel.writeLong(this.mInternalAllPollPssTime);
        parcel.writeLong(this.mExternalPssCount);
        parcel.writeLong(this.mExternalPssTime);
        parcel.writeLong(this.mExternalSlowPssCount);
        parcel.writeLong(this.mExternalSlowPssTime);
        parcel.writeString(this.mRuntime);
        parcel.writeInt(this.mHasSwappedOutPss);
        parcel.writeInt(this.mFlags);
        this.mTableData.writeToParcel(parcel);
        NUID = this.mMemFactor;
        if (NUID != -1) {
            long[] jArr = this.mMemFactorDurations;
            jArr[NUID] = jArr[NUID] + (j - this.mStartTime);
            this.mStartTime = j;
        }
        long[] jArr2 = this.mMemFactorDurations;
        writeCompactedLongArray(parcel, jArr2, jArr2.length);
        this.mSysMemUsage.writeToParcel(parcel);
        parcel.writeInt(NPROC2);
        for (NUID = 0; NUID < NPROC2; NUID++) {
            writeCommonString(parcel, (String) procMap.keyAt(NUID));
            SparseArray<ProcessState> uids5 = (SparseArray) procMap.valueAt(NUID);
            NUID2 = uids5.size();
            parcel.writeInt(NUID2);
            for (iu = 0; iu < NUID2; iu++) {
                parcel.writeInt(uids5.keyAt(iu));
                ProcessState proc = (ProcessState) uids5.valueAt(iu);
                writeCommonString(parcel, proc.getPackage());
                parcel.writeLong(proc.getVersion());
                proc.writeToParcel(parcel, j);
            }
        }
        parcel.writeInt(NPKG2);
        for (NUID = 0; NUID < NPKG2; NUID++) {
            writeCommonString(parcel, (String) pkgMap2.keyAt(NUID));
            uids = (SparseArray) pkgMap2.valueAt(NUID);
            NUID2 = uids.size();
            parcel.writeInt(NUID2);
            for (iu = 0; iu < NUID2; iu++) {
                parcel.writeInt(uids.keyAt(iu));
                vpkgs = (LongSparseArray) uids.valueAt(iu);
                NVERS = vpkgs.size();
                parcel.writeInt(NVERS);
                iv = 0;
                while (iv < NVERS) {
                    ArrayMap<String, SparseArray<ProcessState>> pkgState2;
                    ArrayMap<String, SparseArray<ProcessState>> procMap2 = procMap;
                    NPROC = NPROC2;
                    parcel.writeLong(vpkgs.keyAt(iv));
                    procMap = (PackageState) vpkgs.valueAt(iv);
                    NPROC2 = procMap.mProcesses.size();
                    parcel.writeInt(NPROC2);
                    pkgMap = pkgMap2;
                    ip = 0;
                    while (ip < NPROC2) {
                        NPROCS = NPROC2;
                        writeCommonString(parcel, (String) procMap.mProcesses.keyAt(ip));
                        ProcessState NPROCS2 = (ProcessState) procMap.mProcesses.valueAt(ip);
                        NPKG = NPKG2;
                        if (NPROCS2.getCommonProcess() == NPROCS2) {
                            parcel.writeInt(0);
                        } else {
                            parcel.writeInt(1);
                            NPROCS2.writeToParcel(parcel, j);
                        }
                        ip++;
                        NPROC2 = NPROCS;
                        NPKG2 = NPKG;
                    }
                    NPKG = NPKG2;
                    NPROC2 = procMap.mServices.size();
                    parcel.writeInt(NPROC2);
                    ip = 0;
                    while (ip < NPROC2) {
                        parcel.writeString((String) procMap.mServices.keyAt(ip));
                        ServiceState svc = (ServiceState) procMap.mServices.valueAt(ip);
                        NSRVS = NPROC2;
                        writeCommonString(parcel, svc.getProcessName());
                        svc.writeToParcel(parcel, j);
                        ip++;
                        NPROC2 = NSRVS;
                    }
                    NPROC2 = procMap.mAssociations.size();
                    parcel.writeInt(NPROC2);
                    ip = 0;
                    while (ip < NPROC2) {
                        writeCommonString(parcel, (String) procMap.mAssociations.keyAt(ip));
                        AssociationState asc = (AssociationState) procMap.mAssociations.valueAt(ip);
                        pkgState2 = procMap;
                        writeCommonString(parcel, asc.getProcessName());
                        asc.writeToParcel(this, parcel, j);
                        ip++;
                        procMap = pkgState2;
                    }
                    pkgState2 = procMap;
                    iv++;
                    procMap = procMap2;
                    NPROC2 = NPROC;
                    pkgMap2 = pkgMap;
                    NPKG2 = NPKG;
                }
                NPROC = NPROC2;
                pkgMap = pkgMap2;
                NPKG = NPKG2;
            }
            NPROC = NPROC2;
            pkgMap = pkgMap2;
            NPKG = NPKG2;
        }
        NPROC = NPROC2;
        pkgMap = pkgMap2;
        NPKG = NPKG2;
        int NPAGETYPES = this.mPageTypeLabels.size();
        parcel.writeInt(NPAGETYPES);
        for (NPROC2 = 0; NPROC2 < NPAGETYPES; NPROC2++) {
            parcel.writeInt(((Integer) this.mPageTypeNodes.get(NPROC2)).intValue());
            parcel.writeString((String) this.mPageTypeZones.get(NPROC2));
            parcel.writeString((String) this.mPageTypeLabels.get(NPROC2));
            parcel.writeIntArray((int[]) this.mPageTypeSizes.get(NPROC2));
        }
        this.mCommonStringToIndex = null;
    }

    private boolean readCheckedInt(Parcel in, int val, String what) {
        int readInt = in.readInt();
        int got = readInt;
        if (readInt == val) {
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("bad ");
        stringBuilder.append(what);
        stringBuilder.append(": ");
        stringBuilder.append(got);
        this.mReadError = stringBuilder.toString();
        return false;
    }

    static byte[] readFully(InputStream stream, int[] outLen) throws IOException {
        int pos = 0;
        int initialAvail = stream.available();
        byte[] data = new byte[(initialAvail > 0 ? initialAvail + 1 : 16384)];
        while (true) {
            int amt = stream.read(data, pos, data.length - pos);
            if (amt < 0) {
                outLen[0] = pos;
                return data;
            }
            pos += amt;
            if (pos >= data.length) {
                byte[] newData = new byte[(pos + 16384)];
                System.arraycopy(data, 0, newData, 0, pos);
                data = newData;
            }
        }
    }

    public void read(InputStream stream) {
        try {
            int[] len = new int[1];
            byte[] raw = readFully(stream, len);
            Parcel in = Parcel.obtain();
            in.unmarshall(raw, 0, len[0]);
            in.setDataPosition(0);
            stream.close();
            readFromParcel(in);
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("caught exception: ");
            stringBuilder.append(e);
            this.mReadError = stringBuilder.toString();
        }
    }

    public void readFromParcel(Parcel in) {
        Parcel parcel = in;
        boolean z = this.mPackages.getMap().size() > 0 || this.mProcesses.getMap().size() > 0;
        boolean hadData = z;
        if (hadData) {
            resetSafely();
        }
        if (readCheckedInt(parcel, MAGIC, "magic number")) {
            int version = in.readInt();
            StringBuilder stringBuilder;
            if (version != 36) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("bad version: ");
                stringBuilder.append(version);
                this.mReadError = stringBuilder.toString();
            } else if (readCheckedInt(parcel, 14, "state count") && readCheckedInt(parcel, 8, "adj count") && readCheckedInt(parcel, 10, "pss count") && readCheckedInt(parcel, 16, "sys mem usage count") && readCheckedInt(parcel, 4096, "longs size")) {
                this.mIndexToCommonString = new ArrayList();
                this.mTimePeriodStartClock = in.readLong();
                buildTimePeriodStartClockStr();
                this.mTimePeriodStartRealtime = in.readLong();
                this.mTimePeriodEndRealtime = in.readLong();
                this.mTimePeriodStartUptime = in.readLong();
                this.mTimePeriodEndUptime = in.readLong();
                this.mInternalSinglePssCount = in.readLong();
                this.mInternalSinglePssTime = in.readLong();
                this.mInternalAllMemPssCount = in.readLong();
                this.mInternalAllMemPssTime = in.readLong();
                this.mInternalAllPollPssCount = in.readLong();
                this.mInternalAllPollPssTime = in.readLong();
                this.mExternalPssCount = in.readLong();
                this.mExternalPssTime = in.readLong();
                this.mExternalSlowPssCount = in.readLong();
                this.mExternalSlowPssTime = in.readLong();
                this.mRuntime = in.readString();
                this.mHasSwappedOutPss = in.readInt() != 0;
                this.mFlags = in.readInt();
                this.mTableData.readFromParcel(parcel);
                long[] jArr = this.mMemFactorDurations;
                readCompactedLongArray(parcel, version, jArr, jArr.length);
                if (this.mSysMemUsage.readFromParcel(parcel)) {
                    int NPROC = in.readInt();
                    StringBuilder stringBuilder2;
                    if (NPROC < 0) {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("bad process count: ");
                        stringBuilder2.append(NPROC);
                        this.mReadError = stringBuilder2.toString();
                        return;
                    }
                    int NPROC2 = NPROC;
                    while (true) {
                        String str = "bad uid: ";
                        String str2 = "bad uid count: ";
                        int NPROC3;
                        int uid;
                        String procName;
                        if (NPROC2 > 0) {
                            NPROC3 = NPROC2 - 1;
                            String procName2 = readCommonString(parcel, version);
                            if (procName2 == null) {
                                this.mReadError = "bad process name";
                                return;
                            }
                            NPROC = in.readInt();
                            if (NPROC < 0) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append(str2);
                                stringBuilder2.append(NPROC);
                                this.mReadError = stringBuilder2.toString();
                                return;
                            }
                            while (NPROC > 0) {
                                int NUID = NPROC - 1;
                                int uid2 = in.readInt();
                                if (uid2 < 0) {
                                    stringBuilder = new StringBuilder();
                                    stringBuilder.append(str);
                                    stringBuilder.append(uid2);
                                    this.mReadError = stringBuilder.toString();
                                    return;
                                }
                                String pkgName = readCommonString(parcel, version);
                                if (pkgName == null) {
                                    this.mReadError = "bad process package name";
                                    return;
                                }
                                String procName3;
                                long vers = in.readLong();
                                ProcessState proc = hadData ? (ProcessState) this.mProcesses.get(procName2, uid2) : null;
                                if (proc == null) {
                                    uid = uid2;
                                    procName3 = procName2;
                                    proc = new ProcessState(this, pkgName, uid2, vers, procName2);
                                    if (!proc.readFromParcel(parcel, true)) {
                                        return;
                                    }
                                } else if (proc.readFromParcel(parcel, false)) {
                                    uid = uid2;
                                    procName3 = procName2;
                                } else {
                                    return;
                                }
                                procName = procName3;
                                this.mProcesses.put(procName, uid, proc);
                                procName2 = procName;
                                NPROC = NUID;
                            }
                            NPROC2 = NPROC3;
                        } else {
                            NPROC = in.readInt();
                            if (NPROC < 0) {
                                stringBuilder2 = new StringBuilder();
                                stringBuilder2.append("bad package count: ");
                                stringBuilder2.append(NPROC);
                                this.mReadError = stringBuilder2.toString();
                                return;
                            }
                            while (NPROC > 0) {
                                uid = NPROC - 1;
                                String pkgName2 = readCommonString(parcel, version);
                                if (pkgName2 == null) {
                                    this.mReadError = "bad package name";
                                    return;
                                }
                                NPROC = in.readInt();
                                if (NPROC < 0) {
                                    stringBuilder2 = new StringBuilder();
                                    stringBuilder2.append(str2);
                                    stringBuilder2.append(NPROC);
                                    this.mReadError = stringBuilder2.toString();
                                    return;
                                }
                                int NPROC4;
                                while (NPROC > 0) {
                                    NPROC3 = NPROC - 1;
                                    int uid3 = in.readInt();
                                    if (uid3 < 0) {
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append(str);
                                        stringBuilder.append(uid3);
                                        this.mReadError = stringBuilder.toString();
                                        return;
                                    }
                                    NPROC = in.readInt();
                                    if (NPROC < 0) {
                                        stringBuilder2 = new StringBuilder();
                                        stringBuilder2.append("bad versions count: ");
                                        stringBuilder2.append(NPROC);
                                        this.mReadError = stringBuilder2.toString();
                                        return;
                                    }
                                    int uid4;
                                    while (NPROC > 0) {
                                        LongSparseArray<PackageState> vpkg;
                                        int NVERS = NPROC - 1;
                                        long vers2 = in.readLong();
                                        String pkgName3 = pkgName2;
                                        int uid5 = uid3;
                                        PackageState pkgState = new PackageState(this, pkgName2, uid3, vers2);
                                        LongSparseArray<PackageState> vpkg2 = (LongSparseArray) this.mPackages.get(pkgName3, uid5);
                                        if (vpkg2 == null) {
                                            vpkg2 = new LongSparseArray();
                                            this.mPackages.put(pkgName3, uid5, vpkg2);
                                            vpkg = vpkg2;
                                        } else {
                                            vpkg = vpkg2;
                                        }
                                        long vers3 = vers2;
                                        vpkg.put(vers3, pkgState);
                                        NPROC = in.readInt();
                                        if (NPROC < 0) {
                                            pkgName2 = new StringBuilder();
                                            pkgName2.append("bad package process count: ");
                                            pkgName2.append(NPROC);
                                            this.mReadError = pkgName2.toString();
                                            return;
                                        }
                                        int hasProc;
                                        int NPROCS = NPROC;
                                        while (NPROCS > 0) {
                                            NPROCS--;
                                            String procName4 = readCommonString(parcel, version);
                                            if (procName4 == null) {
                                                this.mReadError = "bad package process name";
                                                return;
                                            }
                                            pkgName2 = in.readInt();
                                            LongSparseArray<PackageState> vpkg3 = vpkg;
                                            ProcessState vpkg4 = (ProcessState) this.mProcesses.get(procName4, uid5);
                                            if (vpkg4 == null) {
                                                StringBuilder stringBuilder3 = new StringBuilder();
                                                stringBuilder3.append("no common proc: ");
                                                stringBuilder3.append(procName4);
                                                this.mReadError = stringBuilder3.toString();
                                                return;
                                            }
                                            if (pkgName2 != null) {
                                                if (hadData) {
                                                    hasProc = pkgName2;
                                                    pkgName2 = (ProcessState) pkgState.mProcesses.get(procName4);
                                                } else {
                                                    hasProc = pkgName2;
                                                    pkgName2 = null;
                                                }
                                                if (pkgName2 != null) {
                                                    NPROC4 = NPROC2;
                                                    if (!pkgName2.readFromParcel(parcel, 0)) {
                                                        return;
                                                    }
                                                } else {
                                                    NPROC4 = NPROC2;
                                                    pkgName2 = new ProcessState(vpkg4, pkgName3, uid5, vers3, procName4, 0);
                                                    if (!pkgName2.readFromParcel(parcel, true)) {
                                                        return;
                                                    }
                                                }
                                                pkgState.mProcesses.put(procName4, pkgName2);
                                            } else {
                                                hasProc = pkgName2;
                                                NPROC4 = NPROC2;
                                                pkgState.mProcesses.put(procName4, vpkg4);
                                            }
                                            vpkg = vpkg3;
                                            NPROC2 = NPROC4;
                                        }
                                        NPROC4 = NPROC2;
                                        NPROC = in.readInt();
                                        if (NPROC < 0) {
                                            StringBuilder stringBuilder4 = new StringBuilder();
                                            stringBuilder4.append("bad package service count: ");
                                            stringBuilder4.append(NPROC);
                                            this.mReadError = stringBuilder4.toString();
                                            return;
                                        }
                                        int NSRVS;
                                        PackageState pkgState2;
                                        NPROC2 = NPROC;
                                        while (NPROC2 > 0) {
                                            NPROC2--;
                                            String serviceName = in.readString();
                                            if (serviceName == null) {
                                                this.mReadError = "bad package service name";
                                                return;
                                            }
                                            long vers4;
                                            String serviceName2;
                                            ServiceState serv;
                                            pkgName2 = version > 9 ? readCommonString(parcel, version) : null;
                                            ServiceState serv2 = hadData ? (ServiceState) pkgState.mServices.get(serviceName) : null;
                                            if (serv2 == null) {
                                                vers4 = vers3;
                                                serviceName2 = serviceName;
                                                NSRVS = NPROC2;
                                                pkgState2 = pkgState;
                                                serv = new ServiceState(this, pkgName3, serviceName, pkgName2, null);
                                            } else {
                                                vers4 = vers3;
                                                serviceName2 = serviceName;
                                                NSRVS = NPROC2;
                                                pkgState2 = pkgState;
                                                serv = serv2;
                                            }
                                            if (serv.readFromParcel(parcel)) {
                                                pkgState2.mServices.put(serviceName2, serv);
                                                pkgState = pkgState2;
                                                vers3 = vers4;
                                                NPROC2 = NSRVS;
                                            } else {
                                                return;
                                            }
                                        }
                                        NSRVS = NPROC2;
                                        pkgState2 = pkgState;
                                        NPROC = in.readInt();
                                        if (NPROC < 0) {
                                            stringBuilder2 = new StringBuilder();
                                            stringBuilder2.append("bad package association count: ");
                                            stringBuilder2.append(NPROC);
                                            this.mReadError = stringBuilder2.toString();
                                            return;
                                        }
                                        while (NPROC > 0) {
                                            hasProc = NPROC - 1;
                                            String associationName = readCommonString(parcel, version);
                                            if (associationName == null) {
                                                this.mReadError = "bad package association name";
                                                return;
                                            }
                                            AssociationState asc;
                                            String processName = readCommonString(parcel, version);
                                            AssociationState asc2 = hadData ? (AssociationState) pkgState2.mAssociations.get(associationName) : null;
                                            if (asc2 == null) {
                                                uid4 = uid5;
                                                uid5 = associationName;
                                                asc = new AssociationState(this, pkgState2, associationName, processName, null);
                                            } else {
                                                uid4 = uid5;
                                                uid5 = associationName;
                                                asc = asc2;
                                            }
                                            procName = asc.readFromParcel(this, parcel, version);
                                            if (procName != null) {
                                                this.mReadError = procName;
                                                return;
                                            }
                                            pkgState2.mAssociations.put(uid5, asc);
                                            NPROC = hasProc;
                                            uid5 = uid4;
                                        }
                                        pkgName2 = pkgName3;
                                        NPROC = NVERS;
                                        uid3 = uid5;
                                        NPROC2 = NPROC4;
                                    }
                                    uid4 = uid3;
                                    NPROC4 = NPROC2;
                                    NPROC = NPROC3;
                                }
                                NPROC4 = NPROC2;
                                NPROC = uid;
                            }
                            int NPAGETYPES = in.readInt();
                            this.mPageTypeNodes.clear();
                            this.mPageTypeNodes.ensureCapacity(NPAGETYPES);
                            this.mPageTypeZones.clear();
                            this.mPageTypeZones.ensureCapacity(NPAGETYPES);
                            this.mPageTypeLabels.clear();
                            this.mPageTypeLabels.ensureCapacity(NPAGETYPES);
                            this.mPageTypeSizes.clear();
                            this.mPageTypeSizes.ensureCapacity(NPAGETYPES);
                            for (int i = 0; i < NPAGETYPES; i++) {
                                this.mPageTypeNodes.add(Integer.valueOf(in.readInt()));
                                this.mPageTypeZones.add(in.readString());
                                this.mPageTypeLabels.add(in.readString());
                                this.mPageTypeSizes.add(in.createIntArray());
                            }
                            this.mIndexToCommonString = null;
                            return;
                        }
                    }
                }
            }
        }
    }

    public PackageState getPackageStateLocked(String packageName, int uid, long vers) {
        LongSparseArray<PackageState> vpkg = (LongSparseArray) this.mPackages.get(packageName, uid);
        if (vpkg == null) {
            vpkg = new LongSparseArray();
            this.mPackages.put(packageName, uid, vpkg);
        }
        PackageState as = (PackageState) vpkg.get(vers);
        if (as != null) {
            return as;
        }
        as = new PackageState(this, packageName, uid, vers);
        vpkg.put(vers, as);
        return as;
    }

    public ProcessState getProcessStateLocked(String packageName, int uid, long vers, String processName) {
        return getProcessStateLocked(getPackageStateLocked(packageName, uid, vers), processName);
    }

    public ProcessState getProcessStateLocked(PackageState pkgState, String processName) {
        PackageState packageState = pkgState;
        String str = processName;
        ProcessState ps = (ProcessState) packageState.mProcesses.get(str);
        if (ps != null) {
            return ps;
        }
        ProcessState commonProc;
        Object obj;
        ProcessState ps2;
        ProcessState commonProc2 = (ProcessState) this.mProcesses.get(str, packageState.mUid);
        if (commonProc2 == null) {
            commonProc = new ProcessState(this, packageState.mPackageName, packageState.mUid, packageState.mVersionCode, processName);
            this.mProcesses.put(str, packageState.mUid, commonProc);
        } else {
            commonProc = commonProc2;
        }
        if (commonProc.isMultiPackage()) {
            obj = str;
            commonProc2 = new ProcessState(commonProc, packageState.mPackageName, packageState.mUid, packageState.mVersionCode, processName, SystemClock.uptimeMillis());
        } else if (packageState.mPackageName.equals(commonProc.getPackage()) && packageState.mVersionCode == commonProc.getVersion()) {
            ps2 = commonProc;
            obj = str;
        } else {
            commonProc.setMultiPackage(true);
            long now = SystemClock.uptimeMillis();
            PackageState commonPkgState = getPackageStateLocked(commonProc.getPackage(), packageState.mUid, commonProc.getVersion());
            if (commonPkgState != null) {
                int i;
                ProcessState cloned = commonProc.clone(now);
                commonPkgState.mProcesses.put(commonProc.getName(), cloned);
                for (i = commonPkgState.mServices.size() - 1; i >= 0; i--) {
                    ServiceState ss = (ServiceState) commonPkgState.mServices.valueAt(i);
                    if (ss.getProcess() == commonProc) {
                        ss.setProcess(cloned);
                    }
                }
                for (i = commonPkgState.mAssociations.size() - 1; i >= 0; i--) {
                    AssociationState as = (AssociationState) commonPkgState.mAssociations.valueAt(i);
                    if (as.getProcess() == commonProc) {
                        as.setProcess(cloned);
                    }
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Cloning proc state: no package state ");
                stringBuilder.append(commonProc.getPackage());
                stringBuilder.append("/");
                stringBuilder.append(packageState.mUid);
                stringBuilder.append(" for proc ");
                stringBuilder.append(commonProc.getName());
                Slog.w(TAG, stringBuilder.toString());
            }
            obj = str;
            commonProc2 = new ProcessState(commonProc, packageState.mPackageName, packageState.mUid, packageState.mVersionCode, processName, now);
        }
        packageState.mProcesses.put(obj, ps2);
        return ps2;
    }

    public ServiceState getServiceStateLocked(String packageName, int uid, long vers, String processName, String className) {
        PackageState as = getPackageStateLocked(packageName, uid, vers);
        ServiceState ss = (ServiceState) as.mServices.get(className);
        if (ss != null) {
            return ss;
        }
        ss = new ServiceState(this, packageName, className, processName, processName != null ? getProcessStateLocked(packageName, uid, vers, processName) : null);
        as.mServices.put(className, ss);
        return ss;
    }

    public AssociationState getAssociationStateLocked(String packageName, int uid, long vers, String processName, String className) {
        PackageState pkgs = getPackageStateLocked(packageName, uid, vers);
        AssociationState as = (AssociationState) pkgs.mAssociations.get(className);
        if (as != null) {
            return as;
        }
        AssociationState as2 = new AssociationState(this, pkgs, className, processName, processName != null ? getProcessStateLocked(packageName, uid, vers, processName) : null);
        pkgs.mAssociations.put(className, as2);
        return as2;
    }

    public void updateTrackingAssociationsLocked(int curSeq, long now) {
        for (int i = this.mTrackingAssociations.size() - 1; i >= 0; i--) {
            SourceState act = (SourceState) this.mTrackingAssociations.get(i);
            if (act.mProcStateSeq != curSeq || act.mProcState >= 9) {
                act.stopActive(now);
                act.mInTrackingList = false;
                act.mProcState = -1;
                this.mTrackingAssociations.remove(i);
            } else {
                ProcessState proc = act.getAssociationState().getProcess();
                String str = TAG;
                if (proc != null) {
                    int procState = proc.getCombinedState() % 14;
                    if (act.mProcState == procState) {
                        act.startActive(now);
                    } else {
                        act.stopActive(now);
                        if (act.mProcState < procState) {
                            long nowUptime = SystemClock.uptimeMillis();
                            if (this.mNextInverseProcStateWarningUptime > nowUptime) {
                                this.mSkippedInverseProcStateWarningCount++;
                            } else {
                                StringBuilder stringBuilder = new StringBuilder();
                                stringBuilder.append("Tracking association ");
                                stringBuilder.append(act);
                                stringBuilder.append(" whose proc state ");
                                stringBuilder.append(act.mProcState);
                                stringBuilder.append(" is better than process ");
                                stringBuilder.append(proc);
                                stringBuilder.append(" proc state ");
                                stringBuilder.append(procState);
                                stringBuilder.append(" (");
                                stringBuilder.append(this.mSkippedInverseProcStateWarningCount);
                                stringBuilder.append(" skipped)");
                                Slog.w(str, stringBuilder.toString());
                                this.mSkippedInverseProcStateWarningCount = 0;
                                this.mNextInverseProcStateWarningUptime = 10000 + nowUptime;
                            }
                        }
                    }
                } else {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Tracking association without process: ");
                    stringBuilder2.append(act);
                    stringBuilder2.append(" in ");
                    stringBuilder2.append(act.getAssociationState());
                    Slog.wtf(str, stringBuilder2.toString());
                }
            }
        }
    }

    public void dumpLocked(PrintWriter pw, String reqPackage, long now, boolean dumpSummary, boolean dumpDetails, boolean dumpAll, boolean activeOnly, int section) {
        boolean sepNeeded;
        boolean printedHeader;
        String pkgName;
        int uid;
        String str;
        boolean z;
        String NPROCS;
        String str2;
        String str3;
        String str4;
        String str5;
        PrintWriter printWriter;
        int NSRVS;
        String str6;
        ProcessStats processStats;
        String str7;
        PrintWriter printWriter2;
        PrintWriter printWriter3 = pw;
        String str8 = reqPackage;
        long j = now;
        boolean z2 = dumpAll;
        long totalTime = DumpUtils.dumpSingleTime(null, null, this.mMemFactorDurations, this.mMemFactor, this.mStartTime, now);
        boolean sepNeeded2 = false;
        if (this.mSysMemUsage.getKeyCount() > 0) {
            printWriter3.println("System memory usage:");
            this.mSysMemUsage.dump(printWriter3, "  ", ALL_SCREEN_ADJ, ALL_MEM_ADJ);
            sepNeeded2 = true;
        }
        boolean printedHeader2 = false;
        String str9 = "      (Not active: ";
        String str10 = "        ";
        String str11 = " entries)";
        String str12 = " / ";
        String str13 = "  * ";
        String str14 = ")";
        String str15 = ":";
        boolean iu;
        if ((section & 14) != 0) {
            boolean ip;
            ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap;
            ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap2 = this.mPackages.getMap();
            sepNeeded = sepNeeded2;
            sepNeeded2 = false;
            while (true) {
                printedHeader = printedHeader2;
                if (sepNeeded2 >= pkgMap2.size()) {
                    break;
                }
                ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap3;
                SparseArray<LongSparseArray<PackageState>> uids;
                int iu2;
                String pkgName2;
                pkgName = (String) pkgMap2.keyAt(sepNeeded2);
                SparseArray<LongSparseArray<PackageState>> uids2 = (SparseArray) pkgMap2.valueAt(sepNeeded2);
                int iu3 = 0;
                while (true) {
                    pkgMap3 = pkgMap2;
                    if (iu3 >= uids2.size()) {
                        break;
                    }
                    String str16;
                    LongSparseArray<PackageState> vpkgs;
                    int iv;
                    int uid2;
                    uid = uids2.keyAt(iu3);
                    uids = uids2;
                    LongSparseArray<PackageState> vpkgs2 = (LongSparseArray) uids2.valueAt(iu3);
                    int iv2 = 0;
                    while (true) {
                        str16 = str14;
                        if (iv2 >= vpkgs2.size()) {
                            break;
                        }
                        int NASCS;
                        boolean sepNeeded3;
                        boolean printedHeader3;
                        int NSRVS2;
                        String printedHeader4;
                        PackageState pkgState;
                        PackageState pkgState2;
                        int iproc;
                        String str17 = str9;
                        String str18 = str10;
                        long vers = vpkgs2.keyAt(iv2);
                        PackageState pkgState3 = (PackageState) vpkgs2.valueAt(iv2);
                        vpkgs = vpkgs2;
                        int NPROCS2 = pkgState3.mProcesses.size();
                        str = str11;
                        int NSRVS3 = pkgState3.mServices.size();
                        ip = sepNeeded2;
                        sepNeeded2 = pkgState3.mAssociations.size();
                        boolean pkgMatch = str8 == null || str8.equals(pkgName);
                        boolean onlyAssociations = false;
                        if (pkgMatch) {
                            iu2 = iu3;
                            iv = iv2;
                        } else {
                            boolean procMatch = false;
                            iu2 = iu3;
                            iu3 = 0;
                            while (iu3 < NPROCS2) {
                                iv = iv2;
                                if (str8.equals(((ProcessState) pkgState3.mProcesses.valueAt(iu3)).getName())) {
                                    procMatch = true;
                                    break;
                                }
                                iu3++;
                                z2 = dumpAll;
                                iv2 = iv;
                            }
                            iv = iv2;
                            if (!procMatch) {
                                for (iu = false; iu < sepNeeded2; iu++) {
                                    if (((AssociationState) pkgState3.mAssociations.valueAt(iu)).hasProcessOrPackage(str8)) {
                                        onlyAssociations = true;
                                        break;
                                    }
                                }
                                if (!onlyAssociations) {
                                    z = dumpAll;
                                    pkgName2 = pkgName;
                                    uid2 = uid;
                                    NPROCS = str15;
                                    str2 = str13;
                                    pkgMap = pkgMap3;
                                    sepNeeded2 = str16;
                                    str3 = str17;
                                    str4 = str18;
                                    str5 = str;
                                    str = str12;
                                    str14 = sepNeeded2;
                                    str15 = NPROCS;
                                    z2 = z;
                                    vpkgs2 = vpkgs;
                                    str12 = str;
                                    sepNeeded2 = ip;
                                    pkgName = pkgName2;
                                    pkgMap3 = pkgMap;
                                    uid = uid2;
                                    str13 = str2;
                                    str11 = str5;
                                    str10 = str4;
                                    str9 = str3;
                                    iv2 = iv + 1;
                                    iu3 = iu2;
                                }
                            }
                        }
                        if (NPROCS2 > 0 || NSRVS3 > 0 || sepNeeded2 <= false) {
                            if (!printedHeader) {
                                if (sepNeeded) {
                                    pw.println();
                                }
                                printWriter3.println("Per-Package Stats:");
                                printedHeader = true;
                                sepNeeded = true;
                            }
                            printWriter3.print(str13);
                            printWriter3.print(pkgName);
                            printWriter3.print(str12);
                            UserHandle.formatUid(printWriter3, uid);
                            printWriter3.print(" / v");
                            printWriter3.print(vers);
                            printWriter3.println(str15);
                            iu = sepNeeded;
                            z2 = printedHeader;
                        } else {
                            iu = sepNeeded;
                            z2 = printedHeader;
                        }
                        int i;
                        long j2;
                        if ((section & 2) == 0 || onlyAssociations) {
                            z = dumpAll;
                            NASCS = sepNeeded2;
                            sepNeeded3 = iu;
                            printedHeader3 = z2;
                            pkgName2 = pkgName;
                            uid2 = uid;
                            str2 = str13;
                            NSRVS2 = NSRVS3;
                            pkgMap = pkgMap3;
                            sepNeeded2 = str16;
                            printedHeader4 = str17;
                            pkgState = str;
                            i = NPROCS2;
                            NPROCS = str15;
                            str = str12;
                            str12 = str18;
                            j2 = vers;
                            pkgState2 = pkgState3;
                        } else {
                            PackageState pkgState4;
                            String str19;
                            if (!dumpSummary) {
                                NASCS = sepNeeded2;
                                sepNeeded3 = iu;
                                printedHeader3 = z2;
                                pkgName2 = pkgName;
                                uid2 = uid;
                                pkgState4 = pkgState3;
                                str2 = str13;
                                NSRVS2 = NSRVS3;
                                pkgMap = pkgMap3;
                                sepNeeded2 = str16;
                                printedHeader4 = str17;
                                str5 = str18;
                                pkgState = str;
                                i = str15;
                                str = str12;
                                j2 = vers;
                            } else if (dumpAll) {
                                NASCS = sepNeeded2;
                                sepNeeded3 = iu;
                                printedHeader3 = z2;
                                pkgName2 = pkgName;
                                uid2 = uid;
                                pkgState4 = pkgState3;
                                str2 = str13;
                                NSRVS2 = NSRVS3;
                                pkgMap = pkgMap3;
                                sepNeeded2 = str16;
                                printedHeader4 = str17;
                                str5 = str18;
                                pkgState = str;
                                i = str15;
                                str = str12;
                                j2 = vers;
                            } else {
                                ArrayList<ProcessState> procs = new ArrayList();
                                sepNeeded3 = iu;
                                iu3 = 0;
                                while (iu3 < NPROCS2) {
                                    ArrayList<ProcessState> procs2;
                                    printedHeader3 = z2;
                                    ProcessState printedHeader5 = (ProcessState) pkgState3.mProcesses.valueAt(iu3);
                                    if (pkgMatch) {
                                        pkgName2 = pkgName;
                                    } else {
                                        pkgName2 = pkgName;
                                        if (!str8.equals(printedHeader5.getName())) {
                                            procs2 = procs;
                                            iu3++;
                                            procs = procs2;
                                            z2 = printedHeader3;
                                            pkgName = pkgName2;
                                        }
                                    }
                                    if (!activeOnly || printedHeader5.isInUse()) {
                                        procs2 = procs;
                                        procs2.add(printedHeader5);
                                        iu3++;
                                        procs = procs2;
                                        z2 = printedHeader3;
                                        pkgName = pkgName2;
                                    } else {
                                        procs2 = procs;
                                        iu3++;
                                        procs = procs2;
                                        z2 = printedHeader3;
                                        pkgName = pkgName2;
                                    }
                                }
                                printedHeader3 = z2;
                                pkgName2 = pkgName;
                                uid2 = uid;
                                pkgMap = pkgMap3;
                                NASCS = sepNeeded2;
                                pkgState4 = pkgState3;
                                str19 = str16;
                                str16 = str15;
                                sepNeeded2 = str19;
                                str2 = str13;
                                str19 = str;
                                str = str12;
                                pkgState = str19;
                                NSRVS2 = NSRVS3;
                                printedHeader4 = str17;
                                str5 = str18;
                                DumpUtils.dumpProcessSummaryLocked(pw, "      ", "Prc ", procs, ALL_SCREEN_ADJ, ALL_MEM_ADJ, NON_CACHED_PROC_STATES, now, totalTime);
                                pkgState2 = pkgState4;
                                str12 = str5;
                                z = dumpAll;
                                str19 = str16;
                                i = NPROCS2;
                                NPROCS = str19;
                            }
                            iproc = 0;
                            while (iproc < NPROCS2) {
                                ProcessState proc = (ProcessState) pkgState4.mProcesses.valueAt(iproc);
                                if (!pkgMatch && !str8.equals(proc.getName())) {
                                    pkgState2 = pkgState4;
                                    str12 = str5;
                                    pkgState4 = dumpAll;
                                    str19 = i;
                                    i = NPROCS2;
                                    NPROCS = str19;
                                } else if (!activeOnly || proc.isInUse()) {
                                    printWriter3.print("      Process ");
                                    printWriter3.print((String) pkgState4.mProcesses.keyAt(iproc));
                                    if (proc.getCommonProcess().isMultiPackage()) {
                                        printWriter3.print(" (multi, ");
                                    } else {
                                        printWriter3.print(" (unique, ");
                                    }
                                    printWriter3.print(proc.getDurationsBucketCount());
                                    printWriter3.print(pkgState);
                                    str9 = i;
                                    printWriter3.println(str9);
                                    ProcessState processState = proc;
                                    printWriter = pw;
                                    i = NPROCS2;
                                    ProcessState proc2 = proc;
                                    NPROCS = str9;
                                    NSRVS3 = now;
                                    processState.dumpProcessState(printWriter, "        ", ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES, NSRVS3);
                                    processState = proc2;
                                    processState.dumpPss(printWriter, "        ", ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES, NSRVS3);
                                    pkgState2 = pkgState4;
                                    str12 = str5;
                                    processState.dumpInternalLocked(printWriter3, str12, dumpAll);
                                } else {
                                    printWriter3.print(printedHeader4);
                                    printWriter3.print((String) pkgState4.mProcesses.keyAt(iproc));
                                    printWriter3.println(sepNeeded2);
                                    pkgState2 = pkgState4;
                                    str12 = str5;
                                    pkgState4 = dumpAll;
                                    str19 = i;
                                    i = NPROCS2;
                                    NPROCS = str19;
                                }
                                iproc++;
                                str5 = str12;
                                pkgState4 = pkgState2;
                                int i2 = i;
                                i = NPROCS;
                                NPROCS2 = i2;
                            }
                            pkgState2 = pkgState4;
                            str12 = str5;
                            z = dumpAll;
                            str19 = i;
                            i = NPROCS2;
                            NPROCS = str19;
                        }
                        str10 = "        Process: ";
                        if ((section & 4) == 0 || onlyAssociations) {
                            str5 = pkgState;
                            str3 = printedHeader4;
                            str4 = str12;
                            pkgState = pkgState2;
                            printedHeader4 = str10;
                        } else {
                            int isvc;
                            NSRVS3 = 0;
                            while (true) {
                                NSRVS = NSRVS2;
                                if (NSRVS3 >= NSRVS) {
                                    break;
                                }
                                PackageState packageState;
                                ServiceState svc = (ServiceState) pkgState2.mServices.valueAt(NSRVS3);
                                if (!pkgMatch && !str8.equals(svc.getProcessName())) {
                                    packageState = pkgState;
                                    str3 = printedHeader4;
                                    NSRVS2 = NSRVS;
                                    str4 = str12;
                                    isvc = NSRVS3;
                                    pkgState = pkgState2;
                                    printedHeader4 = str10;
                                } else if (!activeOnly || svc.isInUse()) {
                                    if (z) {
                                        printWriter3.print("      Service ");
                                    } else {
                                        printWriter3.print("      * Svc ");
                                    }
                                    printWriter3.print((String) pkgState2.mServices.keyAt(NSRVS3));
                                    printWriter3.println(NPROCS);
                                    printWriter3.print(str10);
                                    printWriter3.println(svc.getProcessName());
                                    NSRVS2 = NSRVS;
                                    str4 = str12;
                                    isvc = NSRVS3;
                                    packageState = pkgState;
                                    str3 = printedHeader4;
                                    pkgState = pkgState2;
                                    printedHeader4 = str10;
                                    svc.dumpStats(pw, "        ", "          ", "    ", now, totalTime, dumpSummary, dumpAll);
                                } else {
                                    printWriter3.print("      (Not active service: ");
                                    printWriter3.print((String) pkgState2.mServices.keyAt(NSRVS3));
                                    printWriter3.println(sepNeeded2);
                                    packageState = pkgState;
                                    str3 = printedHeader4;
                                    NSRVS2 = NSRVS;
                                    str4 = str12;
                                    isvc = NSRVS3;
                                    pkgState = pkgState2;
                                    printedHeader4 = str10;
                                }
                                NSRVS3 = isvc + 1;
                                pkgState2 = pkgState;
                                str10 = printedHeader4;
                                pkgState = packageState;
                                str12 = str4;
                                printedHeader4 = str3;
                            }
                            str5 = pkgState;
                            str3 = printedHeader4;
                            NSRVS2 = NSRVS;
                            str4 = str12;
                            isvc = NSRVS3;
                            pkgState = pkgState2;
                            printedHeader4 = str10;
                        }
                        if ((section & 8) != 0) {
                            int iasc;
                            int iasc2 = 0;
                            while (true) {
                                iproc = NASCS;
                                if (iasc2 >= iproc) {
                                    break;
                                }
                                AssociationState asc = (AssociationState) pkgState.mAssociations.valueAt(iasc2);
                                if (!(pkgMatch || str8.equals(asc.getProcessName()))) {
                                    if (!onlyAssociations) {
                                        iasc = iasc2;
                                        NASCS = iproc;
                                    } else if (!asc.hasProcessOrPackage(str8)) {
                                        iasc = iasc2;
                                        NASCS = iproc;
                                    }
                                    iasc2 = iasc + 1;
                                }
                                if (!activeOnly || asc.isInUse()) {
                                    if (z) {
                                        printWriter3.print("      Association ");
                                    } else {
                                        printWriter3.print("      * Asc ");
                                    }
                                    printWriter3.print((String) pkgState.mAssociations.keyAt(iasc2));
                                    printWriter3.println(NPROCS);
                                    printWriter3.print(printedHeader4);
                                    printWriter3.println(asc.getProcessName());
                                    iasc = iasc2;
                                    NASCS = iproc;
                                    asc.dumpStats(pw, "        ", "          ", "    ", now, totalTime, onlyAssociations ? str8 : null, dumpDetails, dumpAll);
                                    iasc2 = iasc + 1;
                                } else {
                                    printWriter3.print("      (Not active association: ");
                                    printWriter3.print((String) pkgState.mAssociations.keyAt(iasc2));
                                    printWriter3.println(sepNeeded2);
                                    iasc = iasc2;
                                    NASCS = iproc;
                                    iasc2 = iasc + 1;
                                }
                            }
                            iasc = iasc2;
                            NASCS = iproc;
                        }
                        sepNeeded = sepNeeded3;
                        printedHeader = printedHeader3;
                        str14 = sepNeeded2;
                        str15 = NPROCS;
                        z2 = z;
                        vpkgs2 = vpkgs;
                        str12 = str;
                        sepNeeded2 = ip;
                        pkgName = pkgName2;
                        pkgMap3 = pkgMap;
                        uid = uid2;
                        str13 = str2;
                        str11 = str5;
                        str10 = str4;
                        str9 = str3;
                        iv2 = iv + 1;
                        iu3 = iu2;
                    }
                    vpkgs = vpkgs2;
                    iv = iv2;
                    pkgName2 = pkgName;
                    uid2 = uid;
                    NPROCS = str15;
                    str2 = str13;
                    str = str12;
                    str5 = str11;
                    str3 = str9;
                    str4 = str10;
                    z = z2;
                    iu3++;
                    str14 = str16;
                    uids2 = uids;
                    sepNeeded2 = sepNeeded2;
                    pkgMap2 = pkgMap3;
                }
                ip = sepNeeded2;
                uids = uids2;
                iu2 = iu3;
                z = z2;
                pkgName2 = pkgName;
                NPROCS = str15;
                sepNeeded2 = str14;
                str2 = str13;
                str = str12;
                str5 = str11;
                str3 = str9;
                str4 = str10;
                sepNeeded2 = ip + 1;
                printedHeader2 = printedHeader;
                pkgMap2 = pkgMap3;
                j = now;
            }
            ip = sepNeeded2;
            z = z2;
            pkgMap = pkgMap2;
            NPROCS = str15;
            str6 = str14;
            str2 = str13;
            str = str12;
            str5 = str11;
            str3 = str9;
            str4 = str10;
            printedHeader2 = printedHeader;
        } else {
            iu = sepNeeded2;
            z = z2;
            NPROCS = str15;
            str6 = str14;
            str2 = str13;
            str = str12;
            str5 = str11;
            str3 = str9;
            str4 = str10;
            sepNeeded = iu;
        }
        if ((section & 1) != 0) {
            processStats = this;
            ArrayMap<String, SparseArray<ProcessState>> procMap = processStats.mProcesses.getMap();
            printedHeader2 = false;
            uid = 0;
            boolean printedHeader6 = false;
            int ip2 = 0;
            while (ip2 < procMap.size()) {
                String str20;
                String str21;
                String str22;
                String str23;
                int iu4;
                String procName;
                SparseArray<ProcessState> uids3;
                str11 = (String) procMap.keyAt(ip2);
                SparseArray<ProcessState> uids4 = (SparseArray) procMap.valueAt(ip2);
                int iu5 = 0;
                while (iu5 < uids4.size()) {
                    NSRVS = uids4.keyAt(iu5);
                    int numTotalProcs = uid + 1;
                    ProcessState proc3 = (ProcessState) uids4.valueAt(iu5);
                    if (proc3.hasAnyData() && proc3.isMultiPackage() && (str8 == null || str8.equals(str11) || str8.equals(proc3.getPackage()))) {
                        boolean printedHeader7;
                        boolean numShownProcs = printedHeader2 + 1;
                        if (sepNeeded) {
                            pw.println();
                        }
                        sepNeeded = true;
                        if (printedHeader6) {
                            printedHeader7 = printedHeader6;
                        } else {
                            printWriter3.println("Multi-Package Common Processes:");
                            printedHeader7 = true;
                        }
                        if (!activeOnly || proc3.isInUse()) {
                            str15 = str3;
                            str7 = str2;
                            printWriter3.print(str7);
                            printWriter3.print(str11);
                            printedHeader2 = str;
                            printWriter3.print(printedHeader2);
                            UserHandle.formatUid(printWriter3, NSRVS);
                            str20 = str6;
                            printWriter3.print(" (");
                            printWriter3.print(proc3.getDurationsBucketCount());
                            str6 = str5;
                            printWriter3.print(str6);
                            printWriter3.println(NPROCS);
                            str21 = printedHeader2;
                            str22 = str7;
                            printWriter = pw;
                            str23 = str15;
                            ProcessState proc4 = proc3;
                            iu4 = iu5;
                            procName = str11;
                            uids3 = uids4;
                            long j3 = now;
                            proc3.dumpProcessState(printWriter, "        ", ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES, j3);
                            printedHeader2 = proc4;
                            printedHeader2.dumpPss(printWriter, "        ", ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES, j3);
                            str14 = str4;
                            printedHeader2.dumpInternalLocked(printWriter3, str14, z);
                        } else {
                            str15 = str3;
                            printWriter3.print(str15);
                            printWriter3.print(str11);
                            printWriter3.println(str6);
                            str20 = str6;
                            str23 = str15;
                            iu4 = iu5;
                            procName = str11;
                            uids3 = uids4;
                            str21 = str;
                            str22 = str2;
                            str6 = str5;
                            str14 = str4;
                        }
                        printedHeader2 = numShownProcs;
                        printedHeader6 = printedHeader7;
                    } else {
                        str20 = str6;
                        iu4 = iu5;
                        procName = str11;
                        uids3 = uids4;
                        str21 = str;
                        str22 = str2;
                        str6 = str5;
                        str14 = str4;
                        str23 = str3;
                    }
                    iu5 = iu4 + 1;
                    str5 = str6;
                    str4 = str14;
                    uid = numTotalProcs;
                    str6 = str20;
                    str11 = procName;
                    str = str21;
                    str2 = str22;
                    str3 = str23;
                    uids4 = uids3;
                }
                str20 = str6;
                iu4 = iu5;
                procName = str11;
                uids3 = uids4;
                str21 = str;
                str22 = str2;
                str6 = str5;
                str14 = str4;
                str23 = str3;
                ip2++;
                str6 = str20;
            }
            printWriter3.print("  Total procs: ");
            printWriter3.print(printedHeader2);
            printWriter3.print(" shown of ");
            printWriter3.print(uid);
            printWriter3.println(" total");
            printedHeader = printedHeader6;
        } else {
            processStats = this;
        }
        if (z) {
            if (sepNeeded) {
                pw.println();
            }
            sepNeeded = true;
            if (processStats.mTrackingAssociations.size() > 0) {
                pw.println();
                printWriter3.println("Tracking associations:");
                for (int i3 = 0; i3 < processStats.mTrackingAssociations.size(); i3++) {
                    SourceState src = (SourceState) processStats.mTrackingAssociations.get(i3);
                    AssociationState asc2 = src.getAssociationState();
                    printWriter3.print("  #");
                    printWriter3.print(i3);
                    pkgName = ": ";
                    printWriter3.print(pkgName);
                    printWriter3.print(asc2.getProcessName());
                    str7 = "/";
                    printWriter3.print(str7);
                    UserHandle.formatUid(printWriter3, asc2.getUid());
                    printWriter3.print(" <- ");
                    printWriter3.print(src.getProcessName());
                    printWriter3.print(str7);
                    UserHandle.formatUid(printWriter3, src.getUid());
                    printWriter3.println(NPROCS);
                    printWriter3.print("    Tracking for: ");
                    TimeUtils.formatDuration(now - src.mTrackingUptime, printWriter3);
                    pw.println();
                    printWriter3.print("    Component: ");
                    printWriter3.print(new ComponentName(asc2.getPackage(), asc2.getName()).flattenToShortString());
                    pw.println();
                    printWriter3.print("    Proc state: ");
                    if (src.mProcState != -1) {
                        printWriter3.print(DumpUtils.STATE_NAMES[src.mProcState]);
                    } else {
                        printWriter3.print("--");
                    }
                    printWriter3.print(" #");
                    printWriter3.println(src.mProcStateSeq);
                    printWriter3.print("    Process: ");
                    printWriter3.println(asc2.getProcess());
                    if (src.mActiveCount > 0) {
                        printWriter3.print("    Active count ");
                        printWriter3.print(src.mActiveCount);
                        printWriter3.print(pkgName);
                        asc2.dumpActiveDurationSummary(pw, src, totalTime, now, dumpAll);
                        pw.println();
                    }
                }
            }
        }
        if (sepNeeded) {
            pw.println();
        }
        if (dumpSummary) {
            printWriter3.println("Process summary:");
            long j4 = now;
            printWriter2 = printWriter3;
            dumpSummaryLocked(pw, reqPackage, now, activeOnly);
        } else {
            printWriter2 = printWriter3;
            processStats.dumpTotalsLocked(printWriter2, now);
        }
        if (dumpAll) {
            pw.println();
            printWriter2.println("Internal state:");
            printWriter2.print("  mRunning=");
            printWriter2.println(processStats.mRunning);
        }
        if (reqPackage == null) {
            dumpFragmentationLocked(pw);
        }
    }

    public void dumpSummaryLocked(PrintWriter pw, String reqPackage, long now, boolean activeOnly) {
        String str = "  ";
        PrintWriter printWriter = pw;
        dumpFilteredSummaryLocked(printWriter, null, str, null, ALL_SCREEN_ADJ, ALL_MEM_ADJ, ALL_PROC_STATES, NON_CACHED_PROC_STATES, now, DumpUtils.dumpSingleTime(null, null, this.mMemFactorDurations, this.mMemFactor, this.mStartTime, now), reqPackage, activeOnly);
        pw.println();
        dumpTotalsLocked(pw, now);
    }

    private void dumpFragmentationLocked(PrintWriter pw) {
        pw.println();
        pw.println("Available pages by page size:");
        int NPAGETYPES = this.mPageTypeLabels.size();
        for (int i = 0; i < NPAGETYPES; i++) {
            pw.format("Node %3d Zone %7s  %14s ", new Object[]{this.mPageTypeNodes.get(i), this.mPageTypeZones.get(i), this.mPageTypeLabels.get(i)});
            int[] sizes = (int[]) this.mPageTypeSizes.get(i);
            int N = sizes == null ? 0 : sizes.length;
            for (int j = 0; j < N; j++) {
                pw.format("%6d", new Object[]{Integer.valueOf(sizes[j])});
            }
            pw.println();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public long printMemoryCategory(PrintWriter pw, String prefix, String label, double memWeight, long totalTime, long curTotalMem, int samples) {
        if (memWeight == 0.0d) {
            return curTotalMem;
        }
        long mem = (long) ((1024.0d * memWeight) / ((double) totalTime));
        pw.print(prefix);
        pw.print(label);
        pw.print(": ");
        DebugUtils.printSizeValue(pw, mem);
        pw.print(" (");
        pw.print(samples);
        pw.print(" samples)");
        pw.println();
        return curTotalMem + mem;
    }

    /* Access modifiers changed, original: 0000 */
    public void dumpTotalsLocked(PrintWriter pw, long now) {
        int i;
        PrintWriter printWriter = pw;
        printWriter.println("Run time Stats:");
        DumpUtils.dumpSingleTime(pw, "  ", this.mMemFactorDurations, this.mMemFactor, this.mStartTime, now);
        pw.println();
        printWriter.println("Memory usage:");
        TotalMemoryUseCollection totalMem = new TotalMemoryUseCollection(ALL_SCREEN_ADJ, ALL_MEM_ADJ);
        computeTotalMemoryUse(totalMem, now);
        PrintWriter printWriter2 = pw;
        long totalPss = printMemoryCategory(printWriter2, "  ", "Native ", totalMem.sysMemNativeWeight, totalMem.totalTime, printMemoryCategory(printWriter2, "  ", "Kernel ", totalMem.sysMemKernelWeight, totalMem.totalTime, 0, totalMem.sysMemSamples), totalMem.sysMemSamples);
        int i2 = 0;
        while (i2 < 14) {
            if (i2 != 6) {
                i = i2;
                totalPss = printMemoryCategory(pw, "  ", DumpUtils.STATE_NAMES[i2], totalMem.processStateWeight[i2], totalMem.totalTime, totalPss, totalMem.processStateSamples[i2]);
            } else {
                i = i2;
            }
            i2 = i + 1;
        }
        i = i2;
        printWriter2 = pw;
        int i3 = 6;
        long totalPss2 = printMemoryCategory(printWriter2, "  ", "Z-Ram  ", totalMem.sysMemZRamWeight, totalMem.totalTime, printMemoryCategory(printWriter2, "  ", "Free   ", totalMem.sysMemFreeWeight, totalMem.totalTime, printMemoryCategory(printWriter2, "  ", "Cached ", totalMem.sysMemCachedWeight, totalMem.totalTime, totalPss, totalMem.sysMemSamples), totalMem.sysMemSamples), totalMem.sysMemSamples);
        printWriter.print("  TOTAL  : ");
        DebugUtils.printSizeValue(printWriter, totalPss2);
        pw.println();
        totalPss = totalPss2;
        printMemoryCategory(printWriter2, "  ", DumpUtils.STATE_NAMES[i3], totalMem.processStateWeight[i3], totalMem.totalTime, totalPss2, totalMem.processStateSamples[i3]);
        pw.println();
        printWriter.println("PSS collection stats:");
        printWriter.print("  Internal Single: ");
        printWriter.print(this.mInternalSinglePssCount);
        String str = "x over ";
        printWriter.print(str);
        TimeUtils.formatDuration(this.mInternalSinglePssTime, printWriter);
        pw.println();
        printWriter.print("  Internal All Procs (Memory Change): ");
        printWriter.print(this.mInternalAllMemPssCount);
        printWriter.print(str);
        TimeUtils.formatDuration(this.mInternalAllMemPssTime, printWriter);
        pw.println();
        printWriter.print("  Internal All Procs (Polling): ");
        printWriter.print(this.mInternalAllPollPssCount);
        printWriter.print(str);
        TimeUtils.formatDuration(this.mInternalAllPollPssTime, printWriter);
        pw.println();
        printWriter.print("  External: ");
        printWriter.print(this.mExternalPssCount);
        printWriter.print(str);
        TimeUtils.formatDuration(this.mExternalPssTime, printWriter);
        pw.println();
        printWriter.print("  External Slow: ");
        printWriter.print(this.mExternalSlowPssCount);
        printWriter.print(str);
        TimeUtils.formatDuration(this.mExternalSlowPssTime, printWriter);
        pw.println();
        pw.println();
        printWriter.print("          Start time: ");
        printWriter.print(DateFormat.format((CharSequence) "yyyy-MM-dd HH:mm:ss", this.mTimePeriodStartClock));
        pw.println();
        printWriter.print("        Total uptime: ");
        TimeUtils.formatDuration((this.mRunning ? SystemClock.uptimeMillis() : this.mTimePeriodEndUptime) - this.mTimePeriodStartUptime, printWriter);
        pw.println();
        printWriter.print("  Total elapsed time: ");
        TimeUtils.formatDuration((this.mRunning ? SystemClock.elapsedRealtime() : this.mTimePeriodEndRealtime) - this.mTimePeriodStartRealtime, printWriter);
        boolean partial = true;
        if ((this.mFlags & 2) != 0) {
            printWriter.print(" (shutdown)");
            partial = false;
        }
        if ((this.mFlags & 4) != 0) {
            printWriter.print(" (sysprops)");
            partial = false;
        }
        if ((this.mFlags & 1) != 0) {
            printWriter.print(" (complete)");
            partial = false;
        }
        if (partial) {
            printWriter.print(" (partial)");
        }
        if (this.mHasSwappedOutPss) {
            printWriter.print(" (swapped-out-pss)");
        }
        printWriter.print(' ');
        printWriter.print(this.mRuntime);
        pw.println();
    }

    /* Access modifiers changed, original: 0000 */
    public void dumpFilteredSummaryLocked(PrintWriter pw, String header, String prefix, String prcLabel, int[] screenStates, int[] memStates, int[] procStates, int[] sortProcStates, long now, long totalTime, String reqPackage, boolean activeOnly) {
        ArrayList<ProcessState> procs = collectProcessesLocked(screenStates, memStates, procStates, sortProcStates, now, reqPackage, activeOnly);
        if (procs.size() > 0) {
            if (header != null) {
                pw.println();
                pw.println(header);
            }
            DumpUtils.dumpProcessSummaryLocked(pw, prefix, prcLabel, procs, screenStates, memStates, sortProcStates, now, totalTime);
        }
    }

    public ArrayList<ProcessState> collectProcessesLocked(int[] screenStates, int[] memStates, int[] procStates, int[] sortProcStates, long now, String reqPackage, boolean activeOnly) {
        int ip;
        int[] iArr;
        int[] iArr2;
        String str = reqPackage;
        ArraySet<ProcessState> foundProcs = new ArraySet();
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
        for (ip = 0; ip < pkgMap.size(); ip++) {
            String pkgName = (String) pkgMap.keyAt(ip);
            SparseArray<LongSparseArray<PackageState>> procs = (SparseArray) pkgMap.valueAt(ip);
            for (int iu = 0; iu < procs.size(); iu++) {
                LongSparseArray<PackageState> vpkgs = (LongSparseArray) procs.valueAt(iu);
                int NVERS = vpkgs.size();
                for (int iv = 0; iv < NVERS; iv++) {
                    PackageState state = (PackageState) vpkgs.valueAt(iv);
                    int NPROCS = state.mProcesses.size();
                    boolean pkgMatch = str == null || str.equals(pkgName);
                    for (int iproc = 0; iproc < NPROCS; iproc++) {
                        ProcessState proc = (ProcessState) state.mProcesses.valueAt(iproc);
                        if ((pkgMatch || str.equals(proc.getName())) && (!activeOnly || proc.isInUse())) {
                            foundProcs.add(proc.getCommonProcess());
                        }
                    }
                }
            }
        }
        ArrayList<ProcessState> outProcs = new ArrayList(foundProcs.size());
        for (ip = 0; ip < foundProcs.size(); ip++) {
            ProcessState proc2 = (ProcessState) foundProcs.valueAt(ip);
            if (proc2.computeProcessTimeLocked(screenStates, memStates, procStates, now) > 0) {
                outProcs.add(proc2);
                if (procStates != sortProcStates) {
                    proc2.computeProcessTimeLocked(screenStates, memStates, sortProcStates, now);
                }
            } else {
                iArr = procStates;
                iArr2 = sortProcStates;
            }
        }
        iArr = procStates;
        iArr2 = sortProcStates;
        Collections.sort(outProcs, ProcessState.COMPARATOR);
        return outProcs;
    }

    public void dumpCheckinLocked(PrintWriter pw, String reqPackage, int section) {
        ProcessStats thisR;
        boolean partial;
        int iv;
        int NPROCS;
        int NSRVS;
        int iproc;
        String str;
        int key;
        String str2;
        int j;
        PrintWriter printWriter = pw;
        String str3 = reqPackage;
        long now = SystemClock.uptimeMillis();
        ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
        printWriter.println("vers,5");
        printWriter.print("period,");
        printWriter.print(this.mTimePeriodStartClockStr);
        String NASCS = ",";
        printWriter.print(NASCS);
        printWriter.print(this.mTimePeriodStartRealtime);
        printWriter.print(NASCS);
        printWriter.print(this.mRunning ? SystemClock.elapsedRealtime() : this.mTimePeriodEndRealtime);
        boolean partial2 = true;
        if ((this.mFlags & 2) != 0) {
            printWriter.print(",shutdown");
            partial2 = false;
        }
        if ((this.mFlags & 4) != 0) {
            printWriter.print(",sysprops");
            partial2 = false;
        }
        if ((this.mFlags & 1) != 0) {
            printWriter.print(",complete");
            partial = false;
        } else {
            partial = partial2;
        }
        if (partial) {
            printWriter.print(",partial");
        }
        if (this.mHasSwappedOutPss) {
            printWriter.print(",swapped-out-pss");
        }
        pw.println();
        printWriter.print("config,");
        printWriter.println(this.mRuntime);
        if ((section & 14) != 0) {
            ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap2;
            int ip;
            int ip2 = 0;
            while (ip2 < pkgMap.size()) {
                String pkgName = (String) pkgMap.keyAt(ip2);
                if (str3 == null || str3.equals(pkgName)) {
                    int iu;
                    String pkgName2;
                    SparseArray<LongSparseArray<PackageState>> uids = (SparseArray) pkgMap.valueAt(ip2);
                    int iu2 = 0;
                    while (iu2 < uids.size()) {
                        int iv2;
                        LongSparseArray<PackageState> vpkgs;
                        SparseArray<LongSparseArray<PackageState>> uids2;
                        int uid = uids.keyAt(iu2);
                        LongSparseArray<PackageState> vpkgs2 = (LongSparseArray) uids.valueAt(iu2);
                        iv = 0;
                        while (iv < vpkgs2.size()) {
                            PackageState pkgState;
                            int NASCS2;
                            int NSRVS2;
                            long vers = vpkgs2.keyAt(iv);
                            PackageState pkgState2 = (PackageState) vpkgs2.valueAt(iv);
                            NPROCS = pkgState2.mProcesses.size();
                            NSRVS = pkgState2.mServices.size();
                            iv2 = iv;
                            iv = pkgState2.mAssociations.size();
                            if ((section & 2) != 0) {
                                iproc = 0;
                                while (iproc < NPROCS) {
                                    int NPROCS2 = NPROCS;
                                    pkgMap2 = pkgMap;
                                    pkgState = pkgState2;
                                    str = NASCS;
                                    NASCS2 = iv;
                                    vpkgs = vpkgs2;
                                    uids2 = uids;
                                    iu = iu2;
                                    pkgName2 = pkgName;
                                    NSRVS2 = NSRVS;
                                    ip = ip2;
                                    ((ProcessState) pkgState2.mProcesses.valueAt(iproc)).dumpPackageProcCheckin(pw, pkgName, uid, vers, (String) pkgState2.mProcesses.keyAt(iproc), now);
                                    iproc++;
                                    NSRVS = NSRVS2;
                                    pkgName = pkgName2;
                                    pkgState2 = pkgState;
                                    iv = NASCS2;
                                    ip2 = ip;
                                    NPROCS = NPROCS2;
                                    pkgMap = pkgMap2;
                                    NASCS = str;
                                    vpkgs2 = vpkgs;
                                    uids = uids2;
                                    iu2 = iu;
                                    printWriter = pw;
                                }
                                vpkgs = vpkgs2;
                                uids2 = uids;
                                iu = iu2;
                                pkgName2 = pkgName;
                                NSRVS2 = NSRVS;
                                ip = ip2;
                                pkgMap2 = pkgMap;
                                str = NASCS;
                                pkgState = pkgState2;
                                NASCS2 = iv;
                            } else {
                                vpkgs = vpkgs2;
                                uids2 = uids;
                                iu = iu2;
                                pkgName2 = pkgName;
                                NSRVS2 = NSRVS;
                                ip = ip2;
                                pkgMap2 = pkgMap;
                                str = NASCS;
                                pkgState = pkgState2;
                                NASCS2 = iv;
                            }
                            if ((section & 4) != 0) {
                                for (iproc = 0; iproc < NSRVS2; iproc++) {
                                    ((ServiceState) pkgState.mServices.valueAt(iproc)).dumpTimesCheckin(pw, pkgName2, uid, vers, DumpUtils.collapseString(pkgName2, (String) pkgState.mServices.keyAt(iproc)), now);
                                }
                            }
                            if ((section & 8) != 0) {
                                for (iproc = 0; iproc < NASCS2; iproc++) {
                                    ((AssociationState) pkgState.mAssociations.valueAt(iproc)).dumpTimesCheckin(pw, pkgName2, uid, vers, DumpUtils.collapseString(pkgName2, (String) pkgState.mAssociations.keyAt(iproc)), now);
                                }
                            }
                            iv = iv2 + 1;
                            str3 = reqPackage;
                            pkgName = pkgName2;
                            ip2 = ip;
                            pkgMap = pkgMap2;
                            NASCS = str;
                            vpkgs2 = vpkgs;
                            uids = uids2;
                            iu2 = iu;
                            printWriter = pw;
                        }
                        iv2 = iv;
                        vpkgs = vpkgs2;
                        uids2 = uids;
                        pkgName2 = pkgName;
                        ip = ip2;
                        pkgMap2 = pkgMap;
                        str = NASCS;
                        iu2++;
                        str3 = reqPackage;
                        printWriter = pw;
                    }
                    iu = iu2;
                    pkgName2 = pkgName;
                }
                ip2++;
                printWriter = pw;
                str3 = reqPackage;
                pkgMap = pkgMap;
                NASCS = NASCS;
            }
            ip = ip2;
            pkgMap2 = pkgMap;
            str = NASCS;
        } else {
            str = NASCS;
        }
        if ((section & 1) != 0) {
            thisR = this;
            ArrayMap<String, SparseArray<ProcessState>> procMap = thisR.mProcesses.getMap();
            for (NSRVS = 0; NSRVS < procMap.size(); NSRVS++) {
                String procName = (String) procMap.keyAt(NSRVS);
                SparseArray<ProcessState> uids3 = (SparseArray) procMap.valueAt(NSRVS);
                for (iproc = 0; iproc < uids3.size(); iproc++) {
                    ((ProcessState) uids3.valueAt(iproc)).dumpProcCheckin(pw, procName, uids3.keyAt(iproc), now);
                }
            }
        } else {
            thisR = this;
        }
        PrintWriter printWriter2 = pw;
        printWriter2.print("total");
        DumpUtils.dumpAdjTimesCheckin(pw, ",", thisR.mMemFactorDurations, thisR.mMemFactor, thisR.mStartTime, now);
        pw.println();
        NPROCS = thisR.mSysMemUsage.getKeyCount();
        String str4 = ":";
        if (NPROCS > 0) {
            printWriter2.print("sysmemusage");
            iv = 0;
            while (iv < NPROCS) {
                key = thisR.mSysMemUsage.getKeyAt(iv);
                int type = SparseMappingTable.getIdFromKey(key);
                str2 = str;
                printWriter2.print(str2);
                DumpUtils.printProcStateTag(printWriter2, type);
                for (j = 0; j < 16; j++) {
                    if (j > 1) {
                        printWriter2.print(str4);
                    }
                    printWriter2.print(thisR.mSysMemUsage.getValue(key, j));
                }
                iv++;
                str = str2;
            }
            str2 = str;
        } else {
            str2 = str;
        }
        pw.println();
        TotalMemoryUseCollection totalMem = new TotalMemoryUseCollection(ALL_SCREEN_ADJ, ALL_MEM_ADJ);
        thisR.computeTotalMemoryUse(totalMem, now);
        printWriter2.print("weights,");
        printWriter2.print(totalMem.totalTime);
        printWriter2.print(str2);
        printWriter2.print(totalMem.sysMemCachedWeight);
        printWriter2.print(str4);
        printWriter2.print(totalMem.sysMemSamples);
        printWriter2.print(str2);
        printWriter2.print(totalMem.sysMemFreeWeight);
        printWriter2.print(str4);
        printWriter2.print(totalMem.sysMemSamples);
        printWriter2.print(str2);
        printWriter2.print(totalMem.sysMemZRamWeight);
        printWriter2.print(str4);
        printWriter2.print(totalMem.sysMemSamples);
        printWriter2.print(str2);
        printWriter2.print(totalMem.sysMemKernelWeight);
        printWriter2.print(str4);
        printWriter2.print(totalMem.sysMemSamples);
        printWriter2.print(str2);
        printWriter2.print(totalMem.sysMemNativeWeight);
        printWriter2.print(str4);
        printWriter2.print(totalMem.sysMemSamples);
        for (key = 0; key < 14; key++) {
            printWriter2.print(str2);
            printWriter2.print(totalMem.processStateWeight[key]);
            printWriter2.print(str4);
            printWriter2.print(totalMem.processStateSamples[key]);
        }
        pw.println();
        int NPAGETYPES = thisR.mPageTypeLabels.size();
        for (key = 0; key < NPAGETYPES; key++) {
            printWriter2.print("availablepages,");
            printWriter2.print((String) thisR.mPageTypeLabels.get(key));
            printWriter2.print(str2);
            printWriter2.print((String) thisR.mPageTypeZones.get(key));
            printWriter2.print(str2);
            int[] sizes = (int[]) thisR.mPageTypeSizes.get(key);
            j = sizes == null ? 0 : sizes.length;
            for (NSRVS = 0; NSRVS < j; NSRVS++) {
                if (NSRVS != 0) {
                    printWriter2.print(str2);
                }
                printWriter2.print(sizes[NSRVS]);
            }
            pw.println();
        }
    }

    public void writeToProto(ProtoOutputStream proto, long now, int section) {
        boolean partial;
        int N;
        int ip;
        ProtoOutputStream protoOutputStream = proto;
        protoOutputStream.write(1112396529665L, this.mTimePeriodStartRealtime);
        protoOutputStream.write(1112396529666L, this.mRunning ? SystemClock.elapsedRealtime() : this.mTimePeriodEndRealtime);
        protoOutputStream.write(1112396529667L, this.mTimePeriodStartUptime);
        protoOutputStream.write(1112396529668L, this.mTimePeriodEndUptime);
        protoOutputStream.write(1138166333445L, this.mRuntime);
        protoOutputStream.write(1133871366150L, this.mHasSwappedOutPss);
        boolean partial2 = true;
        if ((this.mFlags & 2) != 0) {
            protoOutputStream.write(2259152797703L, 3);
            partial2 = false;
        }
        if ((this.mFlags & 4) != 0) {
            protoOutputStream.write(2259152797703L, 4);
            partial2 = false;
        }
        if ((this.mFlags & 1) != 0) {
            protoOutputStream.write(2259152797703L, 1);
            partial = false;
        } else {
            partial = partial2;
        }
        if (partial) {
            protoOutputStream.write(2259152797703L, 2);
        }
        int NPAGETYPES = this.mPageTypeLabels.size();
        for (int i = 0; i < NPAGETYPES; i++) {
            long token = protoOutputStream.start(2246267895818L);
            protoOutputStream.write(1120986464257L, ((Integer) this.mPageTypeNodes.get(i)).intValue());
            protoOutputStream.write(1138166333442L, (String) this.mPageTypeZones.get(i));
            protoOutputStream.write(1138166333443L, (String) this.mPageTypeLabels.get(i));
            int[] sizes = (int[]) this.mPageTypeSizes.get(i);
            N = sizes == null ? 0 : sizes.length;
            for (int j = 0; j < N; j++) {
                protoOutputStream.write(2220498092036L, sizes[j]);
            }
            protoOutputStream.end(token);
        }
        ArrayMap<String, SparseArray<ProcessState>> procMap = this.mProcesses.getMap();
        if ((section & 1) != 0) {
            for (ip = 0; ip < procMap.size(); ip++) {
                int iu;
                String procName = (String) procMap.keyAt(ip);
                SparseArray<ProcessState> uids = (SparseArray) procMap.valueAt(ip);
                int iu2 = 0;
                while (iu2 < uids.size()) {
                    iu = iu2;
                    ((ProcessState) uids.valueAt(iu2)).writeToProto(proto, 2246267895816L, procName, uids.keyAt(iu2), now);
                    iu2 = iu + 1;
                }
                iu = iu2;
            }
        }
        if ((section & 14) != 0) {
            ArrayMap<String, SparseArray<LongSparseArray<PackageState>>> pkgMap = this.mPackages.getMap();
            for (ip = 0; ip < pkgMap.size(); ip++) {
                SparseArray<LongSparseArray<PackageState>> uids2 = (SparseArray) pkgMap.valueAt(ip);
                for (int iu3 = 0; iu3 < uids2.size(); iu3++) {
                    int iv;
                    LongSparseArray<PackageState> vers;
                    LongSparseArray<PackageState> vers2 = (LongSparseArray) uids2.valueAt(iu3);
                    N = 0;
                    while (N < vers2.size()) {
                        iv = N;
                        vers = vers2;
                        ((PackageState) vers2.valueAt(N)).writeToProto(proto, 2246267895817L, now, section);
                        N = iv + 1;
                        vers2 = vers;
                    }
                    iv = N;
                    vers = vers2;
                }
            }
        }
    }
}
