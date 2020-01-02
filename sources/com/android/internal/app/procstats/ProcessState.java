package com.android.internal.app.procstats;

import android.app.job.JobInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.DebugUtils;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Slog;
import android.util.SparseLongArray;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import android.util.proto.ProtoUtils;
import com.android.internal.app.procstats.ProcessStats.PackageState;
import com.android.internal.app.procstats.ProcessStats.ProcessDataCollection;
import com.android.internal.app.procstats.ProcessStats.ProcessStateHolder;
import com.android.internal.app.procstats.ProcessStats.TotalMemoryUseCollection;
import java.io.PrintWriter;
import java.util.Comparator;

public final class ProcessState {
    public static final Comparator<ProcessState> COMPARATOR = new Comparator<ProcessState>() {
        public int compare(ProcessState lhs, ProcessState rhs) {
            if (lhs.mTmpTotalTime < rhs.mTmpTotalTime) {
                return -1;
            }
            if (lhs.mTmpTotalTime > rhs.mTmpTotalTime) {
                return 1;
            }
            return 0;
        }
    };
    private static final boolean DEBUG = false;
    private static final boolean DEBUG_PARCEL = false;
    static final int[] PROCESS_STATE_TO_STATE = new int[]{0, 0, 1, 2, 2, 2, 2, 2, 3, 3, 4, 5, 7, 1, 8, 9, 10, 11, 12, 11, 13};
    private static final String TAG = "ProcessStats";
    private boolean mActive;
    private long mAvgCachedKillPss;
    private ProcessState mCommonProcess;
    private int mCurCombinedState;
    private boolean mDead;
    private final DurationsTable mDurations;
    private int mLastPssState;
    private long mLastPssTime;
    private long mMaxCachedKillPss;
    private long mMinCachedKillPss;
    private boolean mMultiPackage;
    private final String mName;
    private int mNumActiveServices;
    private int mNumCachedKill;
    private int mNumExcessiveCpu;
    private int mNumStartedServices;
    private final String mPackage;
    private final PssTable mPssTable;
    private long mStartTime;
    private final ProcessStats mStats;
    private long mTmpTotalTime;
    private long mTotalRunningDuration;
    private final long[] mTotalRunningPss;
    private long mTotalRunningStartTime;
    private final int mUid;
    private final long mVersion;
    public ProcessState tmpFoundSubProc;
    public int tmpNumInUse;

    static class PssAggr {
        long pss = 0;
        long samples = 0;

        PssAggr() {
        }

        /* Access modifiers changed, original: 0000 */
        public void add(long newPss, long newSamples) {
            double d = (double) this.pss;
            long j = this.samples;
            this.pss = ((long) ((d * ((double) j)) + (((double) newPss) * ((double) newSamples)))) / (j + newSamples);
            this.samples = j + newSamples;
        }
    }

    public ProcessState(ProcessStats processStats, String pkg, int uid, long vers, String name) {
        this.mTotalRunningPss = new long[10];
        this.mCurCombinedState = -1;
        this.mLastPssState = -1;
        this.mStats = processStats;
        this.mName = name;
        this.mCommonProcess = this;
        this.mPackage = pkg;
        this.mUid = uid;
        this.mVersion = vers;
        this.mDurations = new DurationsTable(processStats.mTableData);
        this.mPssTable = new PssTable(processStats.mTableData);
    }

    public ProcessState(ProcessState commonProcess, String pkg, int uid, long vers, String name, long now) {
        this.mTotalRunningPss = new long[10];
        this.mCurCombinedState = -1;
        this.mLastPssState = -1;
        this.mStats = commonProcess.mStats;
        this.mName = name;
        this.mCommonProcess = commonProcess;
        this.mPackage = pkg;
        this.mUid = uid;
        this.mVersion = vers;
        this.mCurCombinedState = commonProcess.mCurCombinedState;
        this.mStartTime = now;
        if (this.mCurCombinedState != -1) {
            this.mTotalRunningStartTime = now;
        }
        this.mDurations = new DurationsTable(commonProcess.mStats.mTableData);
        this.mPssTable = new PssTable(commonProcess.mStats.mTableData);
    }

    public ProcessState clone(long now) {
        ProcessState pnew = new ProcessState(this, this.mPackage, this.mUid, this.mVersion, this.mName, now);
        pnew.mDurations.addDurations(this.mDurations);
        pnew.mPssTable.copyFrom(this.mPssTable, 10);
        System.arraycopy(this.mTotalRunningPss, 0, pnew.mTotalRunningPss, 0, 10);
        pnew.mTotalRunningDuration = getTotalRunningDuration(now);
        pnew.mNumExcessiveCpu = this.mNumExcessiveCpu;
        pnew.mNumCachedKill = this.mNumCachedKill;
        pnew.mMinCachedKillPss = this.mMinCachedKillPss;
        pnew.mAvgCachedKillPss = this.mAvgCachedKillPss;
        pnew.mMaxCachedKillPss = this.mMaxCachedKillPss;
        pnew.mActive = this.mActive;
        pnew.mNumActiveServices = this.mNumActiveServices;
        pnew.mNumStartedServices = this.mNumStartedServices;
        return pnew;
    }

    public String getName() {
        return this.mName;
    }

    public ProcessState getCommonProcess() {
        return this.mCommonProcess;
    }

    public void makeStandalone() {
        this.mCommonProcess = this;
    }

    public String getPackage() {
        return this.mPackage;
    }

    public int getUid() {
        return this.mUid;
    }

    public long getVersion() {
        return this.mVersion;
    }

    public boolean isMultiPackage() {
        return this.mMultiPackage;
    }

    public void setMultiPackage(boolean val) {
        this.mMultiPackage = val;
    }

    public int getDurationsBucketCount() {
        return this.mDurations.getKeyCount();
    }

    public void add(ProcessState other) {
        this.mDurations.addDurations(other.mDurations);
        this.mPssTable.mergeStats(other.mPssTable);
        this.mNumExcessiveCpu += other.mNumExcessiveCpu;
        int i = other.mNumCachedKill;
        if (i > 0) {
            addCachedKill(i, other.mMinCachedKillPss, other.mAvgCachedKillPss, other.mMaxCachedKillPss);
        }
    }

    public void resetSafely(long now) {
        this.mDurations.resetTable();
        this.mPssTable.resetTable();
        this.mStartTime = now;
        this.mLastPssState = -1;
        this.mLastPssTime = 0;
        this.mNumExcessiveCpu = 0;
        this.mNumCachedKill = 0;
        this.mMaxCachedKillPss = 0;
        this.mAvgCachedKillPss = 0;
        this.mMinCachedKillPss = 0;
    }

    public void makeDead() {
        this.mDead = true;
    }

    private void ensureNotDead() {
        if (this.mDead) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ProcessState dead: name=");
            stringBuilder.append(this.mName);
            stringBuilder.append(" pkg=");
            stringBuilder.append(this.mPackage);
            stringBuilder.append(" uid=");
            stringBuilder.append(this.mUid);
            stringBuilder.append(" common.name=");
            stringBuilder.append(this.mCommonProcess.mName);
            Slog.w("ProcessStats", stringBuilder.toString());
        }
    }

    public void writeToParcel(Parcel out, long now) {
        out.writeInt(this.mMultiPackage);
        this.mDurations.writeToParcel(out);
        this.mPssTable.writeToParcel(out);
        for (int i = 0; i < 10; i++) {
            out.writeLong(this.mTotalRunningPss[i]);
        }
        out.writeLong(getTotalRunningDuration(now));
        out.writeInt(0);
        out.writeInt(this.mNumExcessiveCpu);
        out.writeInt(this.mNumCachedKill);
        if (this.mNumCachedKill > 0) {
            out.writeLong(this.mMinCachedKillPss);
            out.writeLong(this.mAvgCachedKillPss);
            out.writeLong(this.mMaxCachedKillPss);
        }
    }

    public boolean readFromParcel(Parcel in, boolean fully) {
        boolean multiPackage = in.readInt() != 0;
        if (fully) {
            this.mMultiPackage = multiPackage;
        }
        if (!this.mDurations.readFromParcel(in) || !this.mPssTable.readFromParcel(in)) {
            return false;
        }
        for (int i = 0; i < 10; i++) {
            this.mTotalRunningPss[i] = in.readLong();
        }
        this.mTotalRunningDuration = in.readLong();
        in.readInt();
        this.mNumExcessiveCpu = in.readInt();
        this.mNumCachedKill = in.readInt();
        if (this.mNumCachedKill > 0) {
            this.mMinCachedKillPss = in.readLong();
            this.mAvgCachedKillPss = in.readLong();
            this.mMaxCachedKillPss = in.readLong();
        } else {
            this.mMaxCachedKillPss = 0;
            this.mAvgCachedKillPss = 0;
            this.mMinCachedKillPss = 0;
        }
        return true;
    }

    public void makeActive() {
        ensureNotDead();
        this.mActive = true;
    }

    public void makeInactive() {
        this.mActive = false;
    }

    public boolean isInUse() {
        return this.mActive || this.mNumActiveServices > 0 || this.mNumStartedServices > 0 || this.mCurCombinedState != -1;
    }

    public boolean isActive() {
        return this.mActive;
    }

    public boolean hasAnyData() {
        if (this.mDurations.getKeyCount() == 0 && this.mCurCombinedState == -1 && this.mPssTable.getKeyCount() == 0 && this.mTotalRunningPss[0] == 0) {
            return false;
        }
        return true;
    }

    public void setState(int state, int memFactor, long now, ArrayMap<String, ProcessStateHolder> pkgList) {
        if (state < 0) {
            state = this.mNumStartedServices > 0 ? (memFactor * 14) + 6 : -1;
        } else {
            state = PROCESS_STATE_TO_STATE[state] + (memFactor * 14);
        }
        this.mCommonProcess.setCombinedState(state, now);
        if (this.mCommonProcess.mMultiPackage && pkgList != null) {
            for (int ip = pkgList.size() - 1; ip >= 0; ip--) {
                pullFixedProc(pkgList, ip).setCombinedState(state, now);
            }
        }
    }

    public void setCombinedState(int state, long now) {
        ensureNotDead();
        if (!this.mDead && this.mCurCombinedState != state) {
            commitStateTime(now);
            if (state == -1) {
                this.mTotalRunningDuration += now - this.mTotalRunningStartTime;
                this.mTotalRunningStartTime = 0;
            } else if (this.mCurCombinedState == -1) {
                this.mTotalRunningDuration = 0;
                this.mTotalRunningStartTime = now;
                for (int i = 9; i >= 0; i--) {
                    this.mTotalRunningPss[i] = 0;
                }
            }
            this.mCurCombinedState = state;
        }
    }

    public int getCombinedState() {
        return this.mCurCombinedState;
    }

    public void commitStateTime(long now) {
        int i = this.mCurCombinedState;
        if (i != -1) {
            long dur = now - this.mStartTime;
            if (dur > 0) {
                this.mDurations.addDuration(i, dur);
            }
            this.mTotalRunningDuration += now - this.mTotalRunningStartTime;
            this.mTotalRunningStartTime = now;
        }
        this.mStartTime = now;
    }

    public void incActiveServices(String serviceName) {
        ProcessState processState = this.mCommonProcess;
        if (processState != this) {
            processState.incActiveServices(serviceName);
        }
        this.mNumActiveServices++;
    }

    public void decActiveServices(String serviceName) {
        ProcessState processState = this.mCommonProcess;
        if (processState != this) {
            processState.decActiveServices(serviceName);
        }
        this.mNumActiveServices--;
        if (this.mNumActiveServices < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Proc active services underrun: pkg=");
            stringBuilder.append(this.mPackage);
            stringBuilder.append(" uid=");
            stringBuilder.append(this.mUid);
            stringBuilder.append(" proc=");
            stringBuilder.append(this.mName);
            stringBuilder.append(" service=");
            stringBuilder.append(serviceName);
            Slog.wtfStack("ProcessStats", stringBuilder.toString());
            this.mNumActiveServices = 0;
        }
    }

    public void incStartedServices(int memFactor, long now, String serviceName) {
        ProcessState processState = this.mCommonProcess;
        if (processState != this) {
            processState.incStartedServices(memFactor, now, serviceName);
        }
        this.mNumStartedServices++;
        if (this.mNumStartedServices == 1 && this.mCurCombinedState == -1) {
            setCombinedState((memFactor * 14) + 6, now);
        }
    }

    public void decStartedServices(int memFactor, long now, String serviceName) {
        ProcessState processState = this.mCommonProcess;
        if (processState != this) {
            processState.decStartedServices(memFactor, now, serviceName);
        }
        this.mNumStartedServices--;
        if (this.mNumStartedServices == 0 && this.mCurCombinedState % 14 == 6) {
            setCombinedState(-1, now);
        } else if (this.mNumStartedServices < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Proc started services underrun: pkg=");
            stringBuilder.append(this.mPackage);
            stringBuilder.append(" uid=");
            stringBuilder.append(this.mUid);
            stringBuilder.append(" name=");
            stringBuilder.append(this.mName);
            Slog.wtfStack("ProcessStats", stringBuilder.toString());
            this.mNumStartedServices = 0;
        }
    }

    public void addPss(long pss, long uss, long rss, boolean always, int type, long duration, ArrayMap<String, ProcessStateHolder> pkgList) {
        int i = type;
        ArrayMap<String, ProcessStateHolder> arrayMap = pkgList;
        ensureNotDead();
        ProcessStats processStats;
        ProcessStats processStats2;
        if (i == 0) {
            processStats = this.mStats;
            processStats.mInternalSinglePssCount++;
            processStats2 = this.mStats;
            processStats2.mInternalSinglePssTime += duration;
        } else if (i == 1) {
            processStats = this.mStats;
            processStats.mInternalAllMemPssCount++;
            processStats2 = this.mStats;
            processStats2.mInternalAllMemPssTime += duration;
        } else if (i == 2) {
            processStats = this.mStats;
            processStats.mInternalAllPollPssCount++;
            processStats2 = this.mStats;
            processStats2.mInternalAllPollPssTime += duration;
        } else if (i == 3) {
            processStats = this.mStats;
            processStats.mExternalPssCount++;
            processStats2 = this.mStats;
            processStats2.mExternalPssTime += duration;
        } else if (i == 4) {
            processStats = this.mStats;
            processStats.mExternalSlowPssCount++;
            processStats2 = this.mStats;
            processStats2.mExternalSlowPssTime += duration;
        }
        if (always || this.mLastPssState != this.mCurCombinedState || SystemClock.uptimeMillis() >= this.mLastPssTime + JobInfo.DEFAULT_INITIAL_BACKOFF_MILLIS) {
            this.mLastPssState = this.mCurCombinedState;
            this.mLastPssTime = SystemClock.uptimeMillis();
            int i2 = this.mCurCombinedState;
            if (i2 != -1) {
                this.mCommonProcess.mPssTable.mergeStats(i2, 1, pss, pss, pss, uss, uss, uss, rss, rss, rss);
                PssTable.mergeStats(this.mCommonProcess.mTotalRunningPss, 0, 1, pss, pss, pss, uss, uss, uss, rss, rss, rss);
                if (this.mCommonProcess.mMultiPackage && arrayMap != null) {
                    for (int ip = pkgList.size() - 1; ip >= 0; ip--) {
                        ProcessState fixedProc = pullFixedProc(arrayMap, ip);
                        fixedProc.mPssTable.mergeStats(this.mCurCombinedState, 1, pss, pss, pss, uss, uss, uss, rss, rss, rss);
                        PssTable.mergeStats(fixedProc.mTotalRunningPss, 0, 1, pss, pss, pss, uss, uss, uss, rss, rss, rss);
                    }
                }
            }
        }
    }

    public void reportExcessiveCpu(ArrayMap<String, ProcessStateHolder> pkgList) {
        ensureNotDead();
        ProcessState processState = this.mCommonProcess;
        processState.mNumExcessiveCpu++;
        if (processState.mMultiPackage) {
            for (int ip = pkgList.size() - 1; ip >= 0; ip--) {
                ProcessState pullFixedProc = pullFixedProc(pkgList, ip);
                pullFixedProc.mNumExcessiveCpu++;
            }
        }
    }

    private void addCachedKill(int num, long minPss, long avgPss, long maxPss) {
        if (this.mNumCachedKill <= 0) {
            this.mNumCachedKill = num;
            this.mMinCachedKillPss = minPss;
            this.mAvgCachedKillPss = avgPss;
            this.mMaxCachedKillPss = maxPss;
            return;
        }
        if (minPss < this.mMinCachedKillPss) {
            this.mMinCachedKillPss = minPss;
        }
        if (maxPss > this.mMaxCachedKillPss) {
            this.mMaxCachedKillPss = maxPss;
        }
        double d = (double) this.mAvgCachedKillPss;
        int i = this.mNumCachedKill;
        this.mAvgCachedKillPss = (long) (((d * ((double) i)) + ((double) avgPss)) / ((double) (i + num)));
        this.mNumCachedKill = i + num;
    }

    public void reportCachedKill(ArrayMap<String, ProcessStateHolder> pkgList, long pss) {
        ensureNotDead();
        this.mCommonProcess.addCachedKill(1, pss, pss, pss);
        if (this.mCommonProcess.mMultiPackage) {
            for (int ip = pkgList.size() - 1; ip >= 0; ip--) {
                pullFixedProc(pkgList, ip).addCachedKill(1, pss, pss, pss);
            }
        }
    }

    public ProcessState pullFixedProc(String pkgName) {
        if (!this.mMultiPackage) {
            return this;
        }
        LongSparseArray<PackageState> vpkg = (LongSparseArray) this.mStats.mPackages.get(pkgName, this.mUid);
        String str = "Didn't find package ";
        String str2 = " / ";
        if (vpkg != null) {
            PackageState pkg = (PackageState) vpkg.get(this.mVersion);
            String str3 = " vers ";
            StringBuilder stringBuilder;
            if (pkg != null) {
                ProcessState proc = (ProcessState) pkg.mProcesses.get(this.mName);
                if (proc != null) {
                    return proc;
                }
                stringBuilder = new StringBuilder();
                stringBuilder.append("Didn't create per-package process ");
                stringBuilder.append(this.mName);
                stringBuilder.append(" in pkg ");
                stringBuilder.append(pkgName);
                stringBuilder.append(str2);
                stringBuilder.append(this.mUid);
                stringBuilder.append(str3);
                stringBuilder.append(this.mVersion);
                throw new IllegalStateException(stringBuilder.toString());
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(pkgName);
            stringBuilder.append(str2);
            stringBuilder.append(this.mUid);
            stringBuilder.append(str3);
            stringBuilder.append(this.mVersion);
            throw new IllegalStateException(stringBuilder.toString());
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(pkgName);
        stringBuilder2.append(str2);
        stringBuilder2.append(this.mUid);
        throw new IllegalStateException(stringBuilder2.toString());
    }

    private ProcessState pullFixedProc(ArrayMap<String, ProcessStateHolder> pkgList, int index) {
        ProcessStateHolder holder = (ProcessStateHolder) pkgList.valueAt(index);
        ProcessState proc = holder.state;
        if (this.mDead && proc.mCommonProcess != proc) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Pulling dead proc: name=");
            stringBuilder.append(this.mName);
            stringBuilder.append(" pkg=");
            stringBuilder.append(this.mPackage);
            stringBuilder.append(" uid=");
            stringBuilder.append(this.mUid);
            stringBuilder.append(" common.name=");
            stringBuilder.append(this.mCommonProcess.mName);
            Log.wtf("ProcessStats", stringBuilder.toString());
            proc = this.mStats.getProcessStateLocked(proc.mPackage, proc.mUid, proc.mVersion, proc.mName);
        }
        if (proc.mMultiPackage) {
            LongSparseArray<PackageState> vpkg = (LongSparseArray) this.mStats.mPackages.get((String) pkgList.keyAt(index), proc.mUid);
            String str = " for multi-proc ";
            String str2 = "No existing package ";
            String str3 = "/";
            StringBuilder stringBuilder2;
            if (vpkg != null) {
                PackageState expkg = (PackageState) vpkg.get(proc.mVersion);
                if (expkg != null) {
                    str = proc.mName;
                    proc = (ProcessState) expkg.mProcesses.get(proc.mName);
                    if (proc != null) {
                        holder.state = proc;
                    } else {
                        stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Didn't create per-package process ");
                        stringBuilder2.append(str);
                        stringBuilder2.append(" in pkg ");
                        stringBuilder2.append(expkg.mPackageName);
                        stringBuilder2.append(str3);
                        stringBuilder2.append(expkg.mUid);
                        throw new IllegalStateException(stringBuilder2.toString());
                    }
                }
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(str2);
                stringBuilder3.append((String) pkgList.keyAt(index));
                stringBuilder3.append(str3);
                stringBuilder3.append(proc.mUid);
                stringBuilder3.append(str);
                stringBuilder3.append(proc.mName);
                stringBuilder3.append(" version ");
                stringBuilder3.append(proc.mVersion);
                throw new IllegalStateException(stringBuilder3.toString());
            }
            stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append((String) pkgList.keyAt(index));
            stringBuilder2.append(str3);
            stringBuilder2.append(proc.mUid);
            stringBuilder2.append(str);
            stringBuilder2.append(proc.mName);
            throw new IllegalStateException(stringBuilder2.toString());
        }
        return proc;
    }

    public long getTotalRunningDuration(long now) {
        long j = this.mTotalRunningDuration;
        long j2 = this.mTotalRunningStartTime;
        long j3 = 0;
        if (j2 != 0) {
            j3 = now - j2;
        }
        return j + j3;
    }

    public long getDuration(int state, long now) {
        long time = this.mDurations.getValueForId((byte) state);
        if (this.mCurCombinedState == state) {
            return time + (now - this.mStartTime);
        }
        return time;
    }

    public long getPssSampleCount(int state) {
        return this.mPssTable.getValueForId((byte) state, 0);
    }

    public long getPssMinimum(int state) {
        return this.mPssTable.getValueForId((byte) state, 1);
    }

    public long getPssAverage(int state) {
        return this.mPssTable.getValueForId((byte) state, 2);
    }

    public long getPssMaximum(int state) {
        return this.mPssTable.getValueForId((byte) state, 3);
    }

    public long getPssUssMinimum(int state) {
        return this.mPssTable.getValueForId((byte) state, 4);
    }

    public long getPssUssAverage(int state) {
        return this.mPssTable.getValueForId((byte) state, 5);
    }

    public long getPssUssMaximum(int state) {
        return this.mPssTable.getValueForId((byte) state, 6);
    }

    public long getPssRssMinimum(int state) {
        return this.mPssTable.getValueForId((byte) state, 7);
    }

    public long getPssRssAverage(int state) {
        return this.mPssTable.getValueForId((byte) state, 8);
    }

    public long getPssRssMaximum(int state) {
        return this.mPssTable.getValueForId((byte) state, 9);
    }

    public void aggregatePss(TotalMemoryUseCollection data, long now) {
        int procState;
        long samples;
        long avg;
        ProcessState processState = this;
        TotalMemoryUseCollection totalMemoryUseCollection = data;
        PssAggr fgPss = new PssAggr();
        PssAggr bgPss = new PssAggr();
        PssAggr cachedPss = new PssAggr();
        boolean havePss = false;
        for (int i = 0; i < processState.mDurations.getKeyCount(); i++) {
            int type = SparseMappingTable.getIdFromKey(processState.mDurations.getKeyAt(i));
            procState = type % 14;
            samples = processState.getPssSampleCount(type);
            if (samples > 0) {
                avg = processState.getPssAverage(type);
                havePss = true;
                if (procState <= 2) {
                    fgPss.add(avg, samples);
                } else if (procState <= 7) {
                    bgPss.add(avg, samples);
                } else {
                    cachedPss.add(avg, samples);
                }
            }
        }
        if (havePss) {
            boolean fgHasBg = false;
            boolean fgHasCached = false;
            boolean bgHasCached = false;
            if (fgPss.samples < 3 && bgPss.samples > 0) {
                fgHasBg = true;
                fgPss.add(bgPss.pss, bgPss.samples);
            }
            if (fgPss.samples < 3 && cachedPss.samples > 0) {
                fgHasCached = true;
                fgPss.add(cachedPss.pss, cachedPss.samples);
            }
            if (bgPss.samples < 3 && cachedPss.samples > 0) {
                bgHasCached = true;
                bgPss.add(cachedPss.pss, cachedPss.samples);
            }
            if (bgPss.samples < 3 && !fgHasBg && fgPss.samples > 0) {
                bgPss.add(fgPss.pss, fgPss.samples);
            }
            if (cachedPss.samples < 3 && !bgHasCached && bgPss.samples > 0) {
                cachedPss.add(bgPss.pss, bgPss.samples);
            }
            if (cachedPss.samples < 3 && !fgHasCached && fgPss.samples > 0) {
                cachedPss.add(fgPss.pss, fgPss.samples);
            }
            int i2 = 0;
            while (i2 < processState.mDurations.getKeyCount()) {
                boolean bgHasCached2;
                long samples2;
                boolean havePss2;
                boolean fgHasBg2;
                long avg2;
                int key = processState.mDurations.getKeyAt(i2);
                procState = SparseMappingTable.getIdFromKey(key);
                samples = processState.mDurations.getValue(key);
                if (processState.mCurCombinedState == procState) {
                    samples += now - processState.mStartTime;
                }
                int procState2 = procState % 14;
                long[] jArr = totalMemoryUseCollection.processStateTime;
                jArr[procState2] = jArr[procState2] + samples;
                long samples3 = processState.getPssSampleCount(procState);
                if (samples3 > 0) {
                    bgHasCached2 = bgHasCached;
                    samples2 = samples3;
                    havePss2 = havePss;
                    fgHasBg2 = fgHasBg;
                    avg2 = processState.getPssAverage(procState);
                } else if (procState2 <= 2) {
                    bgHasCached2 = bgHasCached;
                    samples2 = fgPss.samples;
                    havePss2 = havePss;
                    fgHasBg2 = fgHasBg;
                    avg2 = fgPss.pss;
                } else {
                    havePss2 = havePss;
                    fgHasBg2 = fgHasBg;
                    bgHasCached2 = bgHasCached;
                    if (procState2 <= 7) {
                        samples2 = bgPss.samples;
                        avg2 = bgPss.pss;
                    } else {
                        long j = cachedPss.samples;
                        avg2 = cachedPss.pss;
                        samples2 = j;
                    }
                }
                PssAggr fgPss2 = fgPss;
                PssAggr bgPss2 = bgPss;
                boolean fgHasCached2 = fgHasCached;
                int i3 = i2;
                int type2 = procState;
                long time = samples;
                double newAvg = ((((double) totalMemoryUseCollection.processStatePss[procState2]) * ((double) totalMemoryUseCollection.processStateSamples[procState2])) + (((double) avg2) * ((double) samples2))) / ((double) (((long) totalMemoryUseCollection.processStateSamples[procState2]) + samples2));
                totalMemoryUseCollection.processStatePss[procState2] = (long) newAvg;
                int[] iArr = totalMemoryUseCollection.processStateSamples;
                iArr[procState2] = (int) (((long) iArr[procState2]) + samples2);
                double[] dArr = totalMemoryUseCollection.processStateWeight;
                PssAggr cachedPss2 = cachedPss;
                long time2 = time;
                dArr[procState2] = dArr[procState2] + (((double) avg2) * ((double) time2));
                i2 = i3 + 1;
                processState = this;
                totalMemoryUseCollection = data;
                cachedPss = cachedPss2;
                bgHasCached = bgHasCached2;
                avg = 0;
                fgPss = fgPss2;
                bgPss = bgPss2;
                havePss = havePss2;
                fgHasBg = fgHasBg2;
                fgHasCached = fgHasCached2;
            }
        }
    }

    public long computeProcessTimeLocked(int[] screenStates, int[] memStates, int[] procStates, long now) {
        long totalTime = 0;
        for (int i : screenStates) {
            for (int i2 : memStates) {
                for (int i22 : procStates) {
                    totalTime += getDuration(((i + i22) * 14) + i22, now);
                }
            }
        }
        this.mTmpTotalTime = totalTime;
        return totalTime;
    }

    public void dumpSummary(PrintWriter pw, String prefix, String header, int[] screenStates, int[] memStates, int[] procStates, long now, long totalTime) {
        PrintWriter printWriter = pw;
        String str = header;
        pw.print(prefix);
        printWriter.print("* ");
        if (str != null) {
            printWriter.print(str);
        }
        printWriter.print(this.mName);
        printWriter.print(" / ");
        UserHandle.formatUid(printWriter, this.mUid);
        printWriter.print(" / v");
        printWriter.print(this.mVersion);
        printWriter.println(":");
        PrintWriter printWriter2 = pw;
        String str2 = prefix;
        int[] iArr = screenStates;
        int[] iArr2 = memStates;
        long j = now;
        long j2 = totalTime;
        dumpProcessSummaryDetails(printWriter2, str2, DumpUtils.STATE_LABEL_TOTAL, iArr, iArr2, procStates, j, j2, true);
        int i = 1;
        dumpProcessSummaryDetails(printWriter2, str2, DumpUtils.STATE_LABELS[0], iArr, iArr2, new int[]{0}, j, j2, true);
        String str3 = DumpUtils.STATE_LABELS[i];
        int[] iArr3 = new int[i];
        iArr3[0] = i;
        dumpProcessSummaryDetails(printWriter2, str2, str3, iArr, iArr2, iArr3, j, j2, true);
        str3 = DumpUtils.STATE_LABELS[2];
        iArr3 = new int[i];
        iArr3[0] = 2;
        printWriter2 = pw;
        dumpProcessSummaryDetails(printWriter2, str2, str3, iArr, iArr2, iArr3, j, j2, true);
        str3 = DumpUtils.STATE_LABELS[3];
        iArr3 = new int[i];
        iArr3[0] = 3;
        dumpProcessSummaryDetails(printWriter2, str2, str3, iArr, iArr2, iArr3, j, j2, true);
        str3 = DumpUtils.STATE_LABELS[4];
        iArr3 = new int[i];
        iArr3[0] = 4;
        dumpProcessSummaryDetails(pw, str2, str3, iArr, iArr2, iArr3, j, j2, true);
        str3 = DumpUtils.STATE_LABELS[5];
        iArr3 = new int[i];
        iArr3[0] = 5;
        dumpProcessSummaryDetails(pw, str2, str3, iArr, iArr2, iArr3, j, j2, true);
        str3 = DumpUtils.STATE_LABELS[6];
        iArr3 = new int[i];
        iArr3[0] = 6;
        dumpProcessSummaryDetails(pw, str2, str3, iArr, iArr2, iArr3, j, j2, true);
        str3 = DumpUtils.STATE_LABELS[7];
        iArr3 = new int[i];
        iArr3[0] = 7;
        dumpProcessSummaryDetails(pw, str2, str3, iArr, iArr2, iArr3, j, j2, true);
        str3 = DumpUtils.STATE_LABELS[8];
        iArr3 = new int[i];
        iArr3[0] = 8;
        dumpProcessSummaryDetails(pw, str2, str3, iArr, iArr2, iArr3, j, j2, true);
        str3 = DumpUtils.STATE_LABELS[9];
        iArr3 = new int[i];
        iArr3[0] = 9;
        dumpProcessSummaryDetails(pw, str2, str3, iArr, iArr2, iArr3, j, j2, true);
        str3 = DumpUtils.STATE_LABELS[10];
        iArr3 = new int[i];
        iArr3[0] = 10;
        printWriter2 = pw;
        dumpProcessSummaryDetails(printWriter2, str2, str3, iArr, iArr2, iArr3, j, j2, true);
        dumpProcessSummaryDetails(printWriter2, str2, DumpUtils.STATE_LABEL_CACHED, iArr, iArr2, new int[]{11, 12, 13}, j, j2, true);
    }

    public void dumpProcessState(PrintWriter pw, String prefix, int[] screenStates, int[] memStates, int[] procStates, long now) {
        String str;
        int is;
        ProcessState processState = this;
        PrintWriter printWriter = pw;
        int[] iArr = screenStates;
        int[] iArr2 = memStates;
        int[] iArr3 = procStates;
        long totalTime = 0;
        int printedScreen = -1;
        int is2 = 0;
        while (true) {
            str = ": ";
            if (is2 >= iArr.length) {
                break;
            }
            int im;
            int printedMem = -1;
            int printedScreen2 = printedScreen;
            long totalTime2 = totalTime;
            int im2 = 0;
            while (im2 < iArr2.length) {
                long totalTime3;
                int ip = 0;
                int printedScreen3 = printedScreen2;
                while (ip < iArr3.length) {
                    String running;
                    int iscreen = iArr[is2];
                    int imem = iArr2[im2];
                    int bucket = ((iscreen + imem) * 14) + iArr3[ip];
                    is = is2;
                    im = im2;
                    long time = processState.mDurations.getValueForId((byte) bucket);
                    im2 = "";
                    if (processState.mCurCombinedState == bucket) {
                        totalTime3 = totalTime2;
                        running = " (running)";
                        totalTime = time + (now - processState.mStartTime);
                    } else {
                        totalTime3 = totalTime2;
                        running = im2;
                        totalTime = time;
                    }
                    if (totalTime != 0) {
                        pw.print(prefix);
                        if (iArr.length > 1) {
                            DumpUtils.printScreenLabel(printWriter, printedScreen3 != iscreen ? iscreen : -1);
                            printedScreen3 = iscreen;
                        }
                        if (iArr2.length > 1) {
                            DumpUtils.printMemLabel(printWriter, printedMem != imem ? imem : -1, '/');
                            printedMem = imem;
                        }
                        printWriter.print(DumpUtils.STATE_LABELS[iArr3[ip]]);
                        printWriter.print(str);
                        TimeUtils.formatDuration(totalTime, printWriter);
                        printWriter.println(running);
                        totalTime2 = totalTime3 + totalTime;
                    } else {
                        totalTime2 = totalTime3;
                    }
                    ip++;
                    processState = this;
                    is2 = is;
                    im2 = im;
                }
                totalTime3 = totalTime2;
                is = is2;
                im2++;
                processState = this;
                printedScreen2 = printedScreen3;
            }
            im = im2;
            is2++;
            processState = this;
            totalTime = totalTime2;
            printedScreen = printedScreen2;
        }
        is = is2;
        if (totalTime != 0) {
            int i;
            pw.print(prefix);
            if (iArr.length > 1) {
                i = -1;
                DumpUtils.printScreenLabel(printWriter, -1);
            } else {
                i = -1;
            }
            if (iArr2.length > 1) {
                DumpUtils.printMemLabel(printWriter, i, '/');
            }
            printWriter.print(DumpUtils.STATE_LABEL_TOTAL);
            printWriter.print(str);
            TimeUtils.formatDuration(totalTime, printWriter);
            pw.println();
        }
    }

    public void dumpPss(PrintWriter pw, String prefix, int[] screenStates, int[] memStates, int[] procStates, long now) {
        String str;
        int is;
        PrintWriter printWriter = pw;
        int[] iArr = screenStates;
        int[] iArr2 = memStates;
        int[] iArr3 = procStates;
        boolean printedHeader = false;
        int printedScreen = -1;
        int is2 = 0;
        while (true) {
            str = ": ";
            if (is2 >= iArr.length) {
                break;
            }
            int printedMem = -1;
            int im = 0;
            while (im < iArr2.length) {
                boolean printedHeader2;
                int ip = 0;
                while (ip < iArr3.length) {
                    int iscreen = iArr[is2];
                    int imem = iArr2[im];
                    int bucket = ((iscreen + imem) * 14) + iArr3[ip];
                    is = is2;
                    is2 = this.mPssTable.getKey((byte) bucket);
                    if (is2 != -1) {
                        long[] table = this.mPssTable.getArrayForKey(is2);
                        bucket = SparseMappingTable.getIndexFromKey(is2);
                        if (printedHeader) {
                            printedHeader2 = printedHeader;
                        } else {
                            pw.print(prefix);
                            printWriter.print("PSS/USS (");
                            printWriter.print(this.mPssTable.getKeyCount());
                            printWriter.println(" entries):");
                            printedHeader2 = true;
                        }
                        pw.print(prefix);
                        printWriter.print("  ");
                        if (iArr.length > 1) {
                            DumpUtils.printScreenLabel(printWriter, printedScreen != iscreen ? iscreen : -1);
                            printedScreen = iscreen;
                        }
                        if (iArr2.length > 1) {
                            DumpUtils.printMemLabel(printWriter, printedMem != imem ? imem : -1, '/');
                            printedMem = imem;
                        }
                        printWriter.print(DumpUtils.STATE_LABELS[iArr3[ip]]);
                        printWriter.print(str);
                        dumpPssSamples(printWriter, table, bucket);
                        pw.println();
                        printedHeader = printedHeader2;
                    }
                    ip++;
                    iArr = screenStates;
                    is2 = is;
                }
                printedHeader2 = printedHeader;
                is = is2;
                im++;
                iArr = screenStates;
            }
            is2++;
            iArr = screenStates;
        }
        is = is2;
        long totalRunningDuration = getTotalRunningDuration(now);
        if (totalRunningDuration != 0) {
            pw.print(prefix);
            printWriter.print("Cur time ");
            TimeUtils.formatDuration(totalRunningDuration, printWriter);
            if (this.mTotalRunningStartTime != 0) {
                printWriter.print(" (running)");
            }
            if (this.mTotalRunningPss[0] != 0) {
                printWriter.print(str);
                dumpPssSamples(printWriter, this.mTotalRunningPss, 0);
            }
            pw.println();
        }
        if (this.mNumExcessiveCpu != 0) {
            pw.print(prefix);
            printWriter.print("Killed for excessive CPU use: ");
            printWriter.print(this.mNumExcessiveCpu);
            printWriter.println(" times");
        }
        if (this.mNumCachedKill != 0) {
            pw.print(prefix);
            printWriter.print("Killed from cached state: ");
            printWriter.print(this.mNumCachedKill);
            printWriter.print(" times from pss ");
            DebugUtils.printSizeValue(printWriter, this.mMinCachedKillPss * 1024);
            String str2 = "-";
            printWriter.print(str2);
            DebugUtils.printSizeValue(printWriter, this.mAvgCachedKillPss * 1024);
            printWriter.print(str2);
            DebugUtils.printSizeValue(printWriter, this.mMaxCachedKillPss * 1024);
            pw.println();
        }
    }

    public static void dumpPssSamples(PrintWriter pw, long[] table, int offset) {
        DebugUtils.printSizeValue(pw, table[offset + 1] * 1024);
        String str = "-";
        pw.print(str);
        DebugUtils.printSizeValue(pw, table[offset + 2] * 1024);
        pw.print(str);
        DebugUtils.printSizeValue(pw, table[offset + 3] * 1024);
        String str2 = "/";
        pw.print(str2);
        DebugUtils.printSizeValue(pw, table[offset + 4] * 1024);
        pw.print(str);
        DebugUtils.printSizeValue(pw, table[offset + 5] * 1024);
        pw.print(str);
        DebugUtils.printSizeValue(pw, table[offset + 6] * 1024);
        pw.print(str2);
        DebugUtils.printSizeValue(pw, table[offset + 7] * 1024);
        pw.print(str);
        DebugUtils.printSizeValue(pw, table[offset + 8] * 1024);
        pw.print(str);
        DebugUtils.printSizeValue(pw, table[offset + 9] * 1024);
        pw.print(" over ");
        pw.print(table[offset + 0]);
    }

    private void dumpProcessSummaryDetails(PrintWriter pw, String prefix, String label, int[] screenStates, int[] memStates, int[] procStates, long now, long totalTime, boolean full) {
        PrintWriter printWriter = pw;
        String str = label;
        long j = totalTime;
        ProcessDataCollection totals = new ProcessDataCollection(screenStates, memStates, procStates);
        computeProcessData(totals, now);
        if ((((double) totals.totalTime) / ((double) j)) * 100.0d >= 0.005d || totals.numPss != 0) {
            if (prefix != null) {
                pw.print(prefix);
            }
            if (str != null) {
                printWriter.print("  ");
                printWriter.print(str);
                printWriter.print(": ");
            }
            totals.print(printWriter, j, full);
            if (prefix != null) {
                pw.println();
                return;
            }
            return;
        }
        boolean z = full;
    }

    public void dumpInternalLocked(PrintWriter pw, String prefix, boolean dumpAll) {
        if (dumpAll) {
            pw.print(prefix);
            pw.print("myID=");
            pw.print(Integer.toHexString(System.identityHashCode(this)));
            pw.print(" mCommonProcess=");
            pw.print(Integer.toHexString(System.identityHashCode(this.mCommonProcess)));
            pw.print(" mPackage=");
            pw.println(this.mPackage);
            if (this.mMultiPackage) {
                pw.print(prefix);
                pw.print("mMultiPackage=");
                pw.println(this.mMultiPackage);
            }
            if (this != this.mCommonProcess) {
                pw.print(prefix);
                pw.print("Common Proc: ");
                pw.print(this.mCommonProcess.mName);
                pw.print("/");
                pw.print(this.mCommonProcess.mUid);
                pw.print(" pkg=");
                pw.println(this.mCommonProcess.mPackage);
            }
        }
        if (this.mActive) {
            pw.print(prefix);
            pw.print("mActive=");
            pw.println(this.mActive);
        }
        if (this.mDead) {
            pw.print(prefix);
            pw.print("mDead=");
            pw.println(this.mDead);
        }
        if (this.mNumActiveServices != 0 || this.mNumStartedServices != 0) {
            pw.print(prefix);
            pw.print("mNumActiveServices=");
            pw.print(this.mNumActiveServices);
            pw.print(" mNumStartedServices=");
            pw.println(this.mNumStartedServices);
        }
    }

    public void computeProcessData(ProcessDataCollection data, long now) {
        ProcessDataCollection processDataCollection = data;
        long j = 0;
        processDataCollection.totalTime = 0;
        processDataCollection.maxRss = 0;
        processDataCollection.avgRss = 0;
        processDataCollection.minRss = 0;
        processDataCollection.maxUss = 0;
        processDataCollection.avgUss = 0;
        processDataCollection.minUss = 0;
        processDataCollection.maxPss = 0;
        processDataCollection.avgPss = 0;
        processDataCollection.minPss = 0;
        processDataCollection.numPss = 0;
        int is = 0;
        while (is < processDataCollection.screenStates.length) {
            int im;
            long j2;
            int im2 = 0;
            while (im2 < processDataCollection.memStates.length) {
                int is2;
                int ip;
                int ip2 = 0;
                while (ip2 < processDataCollection.procStates.length) {
                    int bucket = ((processDataCollection.screenStates[is] + processDataCollection.memStates[im2]) * 14) + processDataCollection.procStates[ip2];
                    processDataCollection.totalTime += getDuration(bucket, now);
                    long samples = getPssSampleCount(bucket);
                    long avgPss;
                    if (samples > j) {
                        long minPss = getPssMinimum(bucket);
                        avgPss = getPssAverage(bucket);
                        j = getPssMaximum(bucket);
                        long minUss = getPssUssMinimum(bucket);
                        is2 = is;
                        im = im2;
                        long avgUss = getPssUssAverage(bucket);
                        long samples2 = samples;
                        long maxUss = getPssUssMaximum(bucket);
                        long minRss = getPssRssMinimum(bucket);
                        long avgRss = getPssRssAverage(bucket);
                        samples = getPssRssMaximum(bucket);
                        ip = ip2;
                        j2 = 0;
                        long avgRss2;
                        if (processDataCollection.numPss == 0) {
                            processDataCollection.minPss = minPss;
                            processDataCollection.avgPss = avgPss;
                            processDataCollection.maxPss = j;
                            processDataCollection.minUss = minUss;
                            processDataCollection.avgUss = avgUss;
                            processDataCollection.maxUss = maxUss;
                            processDataCollection.minRss = minRss;
                            avgRss2 = avgRss;
                            processDataCollection.avgRss = avgRss2;
                            processDataCollection.maxRss = samples;
                            long j3 = minPss;
                            long j4 = avgPss;
                            avgPss = samples2;
                            samples2 = avgUss;
                            long j5 = samples;
                            samples = minRss;
                            minRss = avgRss2;
                            avgRss2 = maxUss;
                            maxUss = j;
                            j = j5;
                        } else {
                            avgRss2 = avgRss;
                            avgRss = samples;
                            if (minPss < processDataCollection.minPss) {
                                processDataCollection.minPss = minPss;
                            }
                            double d = (double) avgPss;
                            avgPss = samples2;
                            samples2 = avgRss2;
                            processDataCollection.avgPss = (long) (((((double) processDataCollection.avgPss) * ((double) processDataCollection.numPss)) + (d * ((double) avgPss))) / ((double) (processDataCollection.numPss + avgPss)));
                            if (j > processDataCollection.maxPss) {
                                processDataCollection.maxPss = j;
                            }
                            if (minUss < processDataCollection.minUss) {
                                processDataCollection.minUss = minUss;
                            }
                            processDataCollection.avgUss = (long) (((((double) processDataCollection.avgUss) * ((double) processDataCollection.numPss)) + (((double) avgUss) * ((double) avgPss))) / ((double) (processDataCollection.numPss + avgPss)));
                            if (maxUss > processDataCollection.maxUss) {
                                processDataCollection.maxUss = maxUss;
                            } else {
                                ip2 = maxUss;
                            }
                            if (minRss < processDataCollection.minRss) {
                                processDataCollection.minRss = minRss;
                            } else {
                                samples = minRss;
                            }
                            j = samples2;
                            processDataCollection.avgRss = (long) (((((double) processDataCollection.avgRss) * ((double) processDataCollection.numPss)) + (((double) j) * ((double) avgPss))) / ((double) (processDataCollection.numPss + avgPss)));
                            if (avgRss > processDataCollection.maxRss) {
                                processDataCollection.maxRss = avgRss;
                            }
                        }
                        processDataCollection.numPss += avgPss;
                    } else {
                        j2 = j;
                        is2 = is;
                        im = im2;
                        ip = ip2;
                        int i = bucket;
                        avgPss = samples;
                    }
                    ip2 = ip + 1;
                    j = j2;
                    is = is2;
                    im2 = im;
                }
                j2 = j;
                is2 = is;
                ip = ip2;
                im2++;
            }
            j2 = j;
            im = im2;
            is++;
        }
    }

    public void dumpCsv(PrintWriter pw, boolean sepScreenStates, int[] screenStates, boolean sepMemStates, int[] memStates, boolean sepProcStates, int[] procStates, long now) {
        PrintWriter printWriter = pw;
        int[] iArr = screenStates;
        int[] iArr2 = memStates;
        int[] iArr3 = procStates;
        int NSS = sepScreenStates ? iArr.length : 1;
        int NMS = sepMemStates ? iArr2.length : 1;
        int NPS = sepProcStates ? iArr3.length : 1;
        int iss = 0;
        while (iss < NSS) {
            int NSS2;
            int NMS2;
            int NPS2;
            long j;
            int ims = 0;
            while (ims < NMS) {
                int ips = 0;
                while (ips < NPS) {
                    int vsscreen = sepScreenStates ? iArr[iss] : 0;
                    int vsmem = sepMemStates ? iArr2[ims] : 0;
                    int vsproc = sepProcStates ? iArr3[ips] : 0;
                    int NSA = sepScreenStates ? 1 : iArr.length;
                    int NMA = sepMemStates ? 1 : iArr2.length;
                    int NPA = sepProcStates ? 1 : iArr3.length;
                    NSS2 = NSS;
                    NSS = 0;
                    NMS2 = NMS;
                    NPS2 = NPS;
                    long totalTime = 0;
                    while (NSS < NSA) {
                        long totalTime2 = totalTime;
                        NMS = 0;
                        while (NMS < NMA) {
                            NPS = 0;
                            while (NPS < NPA) {
                                int vascreen = sepScreenStates ? 0 : iArr[NSS];
                                totalTime2 += getDuration((((((vsscreen + vascreen) + vsmem) + (sepMemStates ? 0 : iArr2[NMS])) * 14) + vsproc) + (sepProcStates ? 0 : iArr3[NPS]), now);
                                NPS++;
                                iArr = screenStates;
                                iArr2 = memStates;
                                iArr3 = procStates;
                                NMA = NMA;
                            }
                            j = now;
                            NMS++;
                            iArr = screenStates;
                            iArr2 = memStates;
                            iArr3 = procStates;
                            NMA = NMA;
                        }
                        j = now;
                        NSS++;
                        iArr = screenStates;
                        iArr2 = memStates;
                        iArr3 = procStates;
                        totalTime = totalTime2;
                        NMA = NMA;
                    }
                    int i = NMA;
                    j = now;
                    printWriter.print("\t");
                    printWriter.print(totalTime);
                    ips++;
                    iArr = screenStates;
                    iArr2 = memStates;
                    iArr3 = procStates;
                    NMS = NMS2;
                    NPS = NPS2;
                    NSS = NSS2;
                }
                j = now;
                NSS2 = NSS;
                NMS2 = NMS;
                NPS2 = NPS;
                ims++;
                iArr = screenStates;
                iArr2 = memStates;
                iArr3 = procStates;
            }
            j = now;
            NSS2 = NSS;
            NMS2 = NMS;
            NPS2 = NPS;
            iss++;
            iArr = screenStates;
            iArr2 = memStates;
            iArr3 = procStates;
        }
    }

    public void dumpPackageProcCheckin(PrintWriter pw, String pkgName, int uid, long vers, String itemName, long now) {
        pw.print("pkgproc,");
        pw.print(pkgName);
        String str = ",";
        pw.print(str);
        pw.print(uid);
        pw.print(str);
        pw.print(vers);
        pw.print(str);
        pw.print(DumpUtils.collapseString(pkgName, itemName));
        dumpAllStateCheckin(pw, now);
        pw.println();
        if (this.mPssTable.getKeyCount() > 0) {
            pw.print("pkgpss,");
            pw.print(pkgName);
            pw.print(str);
            pw.print(uid);
            pw.print(str);
            pw.print(vers);
            pw.print(str);
            pw.print(DumpUtils.collapseString(pkgName, itemName));
            dumpAllPssCheckin(pw);
            pw.println();
        }
        if (this.mTotalRunningPss[0] != 0) {
            pw.print("pkgrun,");
            pw.print(pkgName);
            pw.print(str);
            pw.print(uid);
            pw.print(str);
            pw.print(vers);
            pw.print(str);
            pw.print(DumpUtils.collapseString(pkgName, itemName));
            pw.print(str);
            pw.print(getTotalRunningDuration(now));
            pw.print(str);
            dumpPssSamplesCheckin(pw, this.mTotalRunningPss, 0);
            pw.println();
        }
        if (this.mNumExcessiveCpu > 0 || this.mNumCachedKill > 0) {
            pw.print("pkgkills,");
            pw.print(pkgName);
            pw.print(str);
            pw.print(uid);
            pw.print(str);
            pw.print(vers);
            pw.print(str);
            pw.print(DumpUtils.collapseString(pkgName, itemName));
            pw.print(str);
            pw.print("0");
            pw.print(str);
            pw.print(this.mNumExcessiveCpu);
            pw.print(str);
            pw.print(this.mNumCachedKill);
            pw.print(str);
            pw.print(this.mMinCachedKillPss);
            str = ":";
            pw.print(str);
            pw.print(this.mAvgCachedKillPss);
            pw.print(str);
            pw.print(this.mMaxCachedKillPss);
            pw.println();
        }
    }

    public void dumpProcCheckin(PrintWriter pw, String procName, int uid, long now) {
        String str = ",";
        if (this.mDurations.getKeyCount() > 0) {
            pw.print("proc,");
            pw.print(procName);
            pw.print(str);
            pw.print(uid);
            dumpAllStateCheckin(pw, now);
            pw.println();
        }
        if (this.mPssTable.getKeyCount() > 0) {
            pw.print("pss,");
            pw.print(procName);
            pw.print(str);
            pw.print(uid);
            dumpAllPssCheckin(pw);
            pw.println();
        }
        if (this.mTotalRunningPss[0] != 0) {
            pw.print("procrun,");
            pw.print(procName);
            pw.print(str);
            pw.print(uid);
            pw.print(str);
            pw.print(getTotalRunningDuration(now));
            pw.print(str);
            dumpPssSamplesCheckin(pw, this.mTotalRunningPss, 0);
            pw.println();
        }
        if (this.mNumExcessiveCpu > 0 || this.mNumCachedKill > 0) {
            pw.print("kills,");
            pw.print(procName);
            pw.print(str);
            pw.print(uid);
            pw.print(str);
            pw.print("0");
            pw.print(str);
            pw.print(this.mNumExcessiveCpu);
            pw.print(str);
            pw.print(this.mNumCachedKill);
            pw.print(str);
            pw.print(this.mMinCachedKillPss);
            String str2 = ":";
            pw.print(str2);
            pw.print(this.mAvgCachedKillPss);
            pw.print(str2);
            pw.print(this.mMaxCachedKillPss);
            pw.println();
        }
    }

    public void dumpAllStateCheckin(PrintWriter pw, long now) {
        int i;
        boolean didCurState = false;
        for (i = 0; i < this.mDurations.getKeyCount(); i++) {
            int key = this.mDurations.getKeyAt(i);
            int type = SparseMappingTable.getIdFromKey(key);
            long time = this.mDurations.getValue(key);
            if (this.mCurCombinedState == type) {
                didCurState = true;
                time += now - this.mStartTime;
            }
            DumpUtils.printProcStateTagAndValue(pw, type, time);
        }
        if (!didCurState) {
            i = this.mCurCombinedState;
            if (i != -1) {
                DumpUtils.printProcStateTagAndValue(pw, i, now - this.mStartTime);
            }
        }
    }

    public void dumpAllPssCheckin(PrintWriter pw) {
        int N = this.mPssTable.getKeyCount();
        for (int i = 0; i < N; i++) {
            int key = this.mPssTable.getKeyAt(i);
            int type = SparseMappingTable.getIdFromKey(key);
            pw.print(',');
            DumpUtils.printProcStateTag(pw, type);
            pw.print(':');
            dumpPssSamplesCheckin(pw, this.mPssTable.getArrayForKey(key), SparseMappingTable.getIndexFromKey(key));
        }
    }

    public static void dumpPssSamplesCheckin(PrintWriter pw, long[] table, int offset) {
        pw.print(table[offset + 0]);
        pw.print(':');
        pw.print(table[offset + 1]);
        pw.print(':');
        pw.print(table[offset + 2]);
        pw.print(':');
        pw.print(table[offset + 3]);
        pw.print(':');
        pw.print(table[offset + 4]);
        pw.print(':');
        pw.print(table[offset + 5]);
        pw.print(':');
        pw.print(table[offset + 6]);
        pw.print(':');
        pw.print(table[offset + 7]);
        pw.print(':');
        pw.print(table[offset + 8]);
        pw.print(':');
        pw.print(table[offset + 9]);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(128);
        sb.append("ProcessState{");
        sb.append(Integer.toHexString(System.identityHashCode(this)));
        sb.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        sb.append(this.mName);
        sb.append("/");
        sb.append(this.mUid);
        sb.append(" pkg=");
        sb.append(this.mPackage);
        if (this.mMultiPackage) {
            sb.append(" (multi)");
        }
        if (this.mCommonProcess != this) {
            sb.append(" (sub)");
        }
        sb.append("}");
        return sb.toString();
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId, String procName, int uid, long now) {
        long killToken;
        int i;
        int key;
        int type;
        long time;
        int i2;
        int i3;
        ProtoOutputStream protoOutputStream = proto;
        long j = now;
        long token = proto.start(fieldId);
        protoOutputStream.write(1138166333441L, procName);
        protoOutputStream.write(1120986464258L, uid);
        if (this.mNumExcessiveCpu > 0 || this.mNumCachedKill > 0) {
            killToken = protoOutputStream.start(1146756268035L);
            protoOutputStream.write(1120986464257L, this.mNumExcessiveCpu);
            protoOutputStream.write(1120986464258L, this.mNumCachedKill);
            ProtoOutputStream protoOutputStream2 = proto;
            long killToken2 = killToken;
            ProtoUtils.toAggStatsProto(protoOutputStream2, 1146756268035L, this.mMinCachedKillPss, this.mAvgCachedKillPss, this.mMaxCachedKillPss);
            protoOutputStream.end(killToken2);
        }
        SparseLongArray durationByState = new SparseLongArray();
        boolean didCurState = false;
        for (i = 0; i < this.mDurations.getKeyCount(); i++) {
            key = this.mDurations.getKeyAt(i);
            type = SparseMappingTable.getIdFromKey(key);
            time = this.mDurations.getValue(key);
            if (this.mCurCombinedState == type) {
                time += j - this.mStartTime;
                didCurState = true;
            }
            durationByState.put(type, time);
        }
        if (!didCurState) {
            key = this.mCurCombinedState;
            if (key != -1) {
                durationByState.put(key, j - this.mStartTime);
            }
        }
        int i4 = 0;
        while (true) {
            killToken = 2246267895813L;
            time = 1112396529668L;
            if (i4 >= this.mPssTable.getKeyCount()) {
                break;
            }
            i = this.mPssTable.getKeyAt(i4);
            type = SparseMappingTable.getIdFromKey(i);
            if (durationByState.indexOfKey(type) < 0) {
                i2 = i4;
            } else {
                int key2 = i;
                int type2 = type;
                long stateToken = protoOutputStream.start(2246267895813L);
                i2 = i4;
                DumpUtils.printProcStateTagProto(proto, 1159641169921L, 1159641169922L, 1159641169923L, type2);
                key = type2;
                long duration = durationByState.get(key);
                durationByState.delete(key);
                protoOutputStream.write(1112396529668L, duration);
                this.mPssTable.writeStatsToProtoForKey(protoOutputStream, key2);
                protoOutputStream.end(stateToken);
            }
            i4 = i2 + 1;
        }
        i2 = i4;
        i4 = 0;
        while (i4 < durationByState.size()) {
            long stateToken2 = protoOutputStream.start(killToken);
            long j2 = killToken;
            i3 = i4;
            DumpUtils.printProcStateTagProto(proto, 1159641169921L, 1159641169922L, 1159641169923L, durationByState.keyAt(i4));
            protoOutputStream.write(1112396529668L, durationByState.valueAt(i3));
            protoOutputStream.end(stateToken2);
            i4 = i3 + 1;
            i3 = procName;
            time = 1112396529668L;
            killToken = j2;
        }
        long j3 = time;
        i3 = i4;
        long totalRunningDuration = getTotalRunningDuration(j);
        if (totalRunningDuration > 0) {
            long stateToken3 = protoOutputStream.start(1146756268038L);
            protoOutputStream.write(j3, totalRunningDuration);
            long[] jArr = this.mTotalRunningPss;
            if (jArr[0] != 0) {
                PssTable.writeStatsToProto(protoOutputStream, jArr, 0);
            }
            protoOutputStream.end(stateToken3);
        }
        protoOutputStream.end(token);
    }
}
