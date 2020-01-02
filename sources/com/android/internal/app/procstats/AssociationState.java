package com.android.internal.app.procstats;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Slog;
import android.util.TimeUtils;
import android.util.proto.ProtoOutputStream;
import com.android.internal.app.procstats.ProcessStats.PackageState;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;

public final class AssociationState {
    private static final boolean DEBUG = false;
    private static final String TAG = "ProcessStats";
    private final String mName;
    private int mNumActive;
    private final PackageState mPackageState;
    private ProcessState mProc;
    private final String mProcessName;
    private final ProcessStats mProcessStats;
    private final ArrayMap<SourceKey, SourceState> mSources = new ArrayMap();
    private final SourceKey mTmpSourceKey = new SourceKey(0, null, null);

    private static final class SourceKey {
        String mPackage;
        String mProcess;
        int mUid;

        SourceKey(int uid, String process, String pkg) {
            this.mUid = uid;
            this.mProcess = process;
            this.mPackage = pkg;
        }

        public boolean equals(Object o) {
            boolean z = false;
            if (!(o instanceof SourceKey)) {
                return false;
            }
            SourceKey s = (SourceKey) o;
            if (s.mUid == this.mUid && Objects.equals(s.mProcess, this.mProcess) && Objects.equals(s.mPackage, this.mPackage)) {
                z = true;
            }
            return z;
        }

        public int hashCode() {
            int hashCode = Integer.hashCode(this.mUid);
            String str = this.mProcess;
            int i = 0;
            hashCode ^= str == null ? 0 : str.hashCode();
            str = this.mPackage;
            if (str != null) {
                i = str.hashCode() * 33;
            }
            return hashCode ^ i;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(64);
            sb.append("SourceKey{");
            UserHandle.formatUid(sb, this.mUid);
            sb.append(' ');
            sb.append(this.mProcess);
            sb.append(' ');
            sb.append(this.mPackage);
            sb.append('}');
            return sb.toString();
        }
    }

    public final class SourceState {
        int mActiveCount;
        long mActiveDuration;
        int mActiveProcState = -1;
        long mActiveStartUptime;
        int mCount;
        long mDuration;
        DurationsTable mDurations;
        boolean mInTrackingList;
        final SourceKey mKey;
        int mNesting;
        int mProcState = -1;
        int mProcStateSeq = -1;
        long mStartUptime;
        long mTrackingUptime;

        SourceState(SourceKey key) {
            this.mKey = key;
        }

        public AssociationState getAssociationState() {
            return AssociationState.this;
        }

        public String getProcessName() {
            return this.mKey.mProcess;
        }

        public int getUid() {
            return this.mKey.mUid;
        }

        public void trackProcState(int procState, int seq, long now) {
            procState = ProcessState.PROCESS_STATE_TO_STATE[procState];
            if (seq != this.mProcStateSeq) {
                this.mProcStateSeq = seq;
                this.mProcState = procState;
            } else if (procState < this.mProcState) {
                this.mProcState = procState;
            }
            if (procState < 9 && !this.mInTrackingList) {
                this.mInTrackingList = true;
                this.mTrackingUptime = now;
                AssociationState.this.mProcessStats.mTrackingAssociations.add(this);
            }
        }

        public void stop() {
            this.mNesting--;
            if (this.mNesting == 0) {
                this.mDuration += SystemClock.uptimeMillis() - this.mStartUptime;
                AssociationState.this.mNumActive = AssociationState.this.mNumActive - 1;
                stopTracking(SystemClock.uptimeMillis());
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void startActive(long now) {
            if (this.mInTrackingList) {
                if (this.mActiveStartUptime == 0) {
                    this.mActiveStartUptime = now;
                    this.mActiveCount++;
                }
                int i = this.mActiveProcState;
                if (i != this.mProcState) {
                    if (i != -1) {
                        long duration = (this.mActiveDuration + now) - this.mActiveStartUptime;
                        if (duration != 0) {
                            if (this.mDurations == null) {
                                makeDurations();
                            }
                            this.mDurations.addDuration(this.mActiveProcState, duration);
                            this.mActiveDuration = 0;
                        }
                        this.mActiveStartUptime = now;
                    }
                    this.mActiveProcState = this.mProcState;
                    return;
                }
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("startActive while not tracking: ");
            stringBuilder.append(this);
            Slog.wtf("ProcessStats", stringBuilder.toString());
        }

        /* Access modifiers changed, original: 0000 */
        public void stopActive(long now) {
            if (this.mActiveStartUptime != 0) {
                if (!this.mInTrackingList) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("stopActive while not tracking: ");
                    stringBuilder.append(this);
                    Slog.wtf("ProcessStats", stringBuilder.toString());
                }
                long duration = (this.mActiveDuration + now) - this.mActiveStartUptime;
                DurationsTable durationsTable = this.mDurations;
                if (durationsTable != null) {
                    durationsTable.addDuration(this.mActiveProcState, duration);
                } else {
                    this.mActiveDuration = duration;
                }
                this.mActiveStartUptime = 0;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void makeDurations() {
            this.mDurations = new DurationsTable(AssociationState.this.mProcessStats.mTableData);
        }

        /* Access modifiers changed, original: 0000 */
        public void stopTracking(long now) {
            stopActive(now);
            if (this.mInTrackingList) {
                this.mInTrackingList = false;
                this.mProcState = -1;
                ArrayList<SourceState> list = AssociationState.this.mProcessStats.mTrackingAssociations;
                for (int i = list.size() - 1; i >= 0; i--) {
                    if (list.get(i) == this) {
                        list.remove(i);
                        return;
                    }
                }
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Stop tracking didn't find in tracking list: ");
                stringBuilder.append(this);
                Slog.wtf("ProcessStats", stringBuilder.toString());
            }
        }

        public String toString() {
            StringBuilder sb = new StringBuilder(64);
            sb.append("SourceState{");
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            sb.append(str);
            sb.append(this.mKey.mProcess);
            sb.append("/");
            sb.append(this.mKey.mUid);
            if (this.mProcState != -1) {
                sb.append(str);
                sb.append(DumpUtils.STATE_NAMES[this.mProcState]);
                sb.append(" #");
                sb.append(this.mProcStateSeq);
            }
            sb.append("}");
            return sb.toString();
        }
    }

    public AssociationState(ProcessStats processStats, PackageState packageState, String name, String processName, ProcessState proc) {
        this.mProcessStats = processStats;
        this.mPackageState = packageState;
        this.mName = name;
        this.mProcessName = processName;
        this.mProc = proc;
    }

    public int getUid() {
        return this.mPackageState.mUid;
    }

    public String getPackage() {
        return this.mPackageState.mPackageName;
    }

    public String getProcessName() {
        return this.mProcessName;
    }

    public String getName() {
        return this.mName;
    }

    public ProcessState getProcess() {
        return this.mProc;
    }

    public void setProcess(ProcessState proc) {
        this.mProc = proc;
    }

    public SourceState startSource(int uid, String processName, String packageName) {
        SourceKey sourceKey = this.mTmpSourceKey;
        sourceKey.mUid = uid;
        sourceKey.mProcess = processName;
        sourceKey.mPackage = packageName;
        SourceState src = (SourceState) this.mSources.get(sourceKey);
        if (src == null) {
            SourceKey key = new SourceKey(uid, processName, packageName);
            src = new SourceState(key);
            this.mSources.put(key, src);
        }
        src.mNesting++;
        if (src.mNesting == 1) {
            src.mCount++;
            src.mStartUptime = SystemClock.uptimeMillis();
            this.mNumActive++;
        }
        return src;
    }

    public void add(AssociationState other) {
        for (int isrc = other.mSources.size() - 1; isrc >= 0; isrc--) {
            SourceKey key = (SourceKey) other.mSources.keyAt(isrc);
            SourceState otherSrc = (SourceState) other.mSources.valueAt(isrc);
            SourceState mySrc = (SourceState) this.mSources.get(key);
            if (mySrc == null) {
                mySrc = new SourceState(key);
                this.mSources.put(key, mySrc);
            }
            mySrc.mCount += otherSrc.mCount;
            mySrc.mDuration += otherSrc.mDuration;
            mySrc.mActiveCount += otherSrc.mActiveCount;
            if (otherSrc.mActiveDuration != 0 || otherSrc.mDurations != null) {
                if (mySrc.mDurations != null) {
                    if (otherSrc.mDurations != null) {
                        mySrc.mDurations.addDurations(otherSrc.mDurations);
                    } else {
                        mySrc.mDurations.addDuration(otherSrc.mActiveProcState, otherSrc.mActiveDuration);
                    }
                } else if (otherSrc.mDurations != null) {
                    mySrc.makeDurations();
                    mySrc.mDurations.addDurations(otherSrc.mDurations);
                    if (mySrc.mActiveDuration != 0) {
                        mySrc.mDurations.addDuration(mySrc.mActiveProcState, mySrc.mActiveDuration);
                        mySrc.mActiveDuration = 0;
                        mySrc.mActiveProcState = -1;
                    }
                } else if (mySrc.mActiveDuration == 0) {
                    mySrc.mActiveProcState = otherSrc.mActiveProcState;
                    mySrc.mActiveDuration = otherSrc.mActiveDuration;
                } else if (mySrc.mActiveProcState == otherSrc.mActiveProcState) {
                    mySrc.mDuration += otherSrc.mDuration;
                } else {
                    mySrc.makeDurations();
                    mySrc.mDurations.addDuration(mySrc.mActiveProcState, mySrc.mActiveDuration);
                    mySrc.mDurations.addDuration(otherSrc.mActiveProcState, otherSrc.mActiveDuration);
                    mySrc.mActiveDuration = 0;
                    mySrc.mActiveProcState = -1;
                }
            }
        }
    }

    public boolean isInUse() {
        return this.mNumActive > 0;
    }

    public void resetSafely(long now) {
        if (isInUse()) {
            for (int isrc = this.mSources.size() - 1; isrc >= 0; isrc--) {
                SourceState src = (SourceState) this.mSources.valueAt(isrc);
                if (src.mNesting > 0) {
                    src.mCount = 1;
                    src.mStartUptime = now;
                    src.mDuration = 0;
                    if (src.mActiveStartUptime > 0) {
                        src.mActiveCount = 1;
                        src.mActiveStartUptime = now;
                    } else {
                        src.mActiveCount = 0;
                    }
                    src.mActiveDuration = 0;
                    src.mDurations = null;
                } else {
                    this.mSources.removeAt(isrc);
                }
            }
            return;
        }
        this.mSources.clear();
    }

    public void writeToParcel(ProcessStats stats, Parcel out, long nowUptime) {
        int NSRC = this.mSources.size();
        out.writeInt(NSRC);
        for (int isrc = 0; isrc < NSRC; isrc++) {
            SourceKey key = (SourceKey) this.mSources.keyAt(isrc);
            SourceState src = (SourceState) this.mSources.valueAt(isrc);
            out.writeInt(key.mUid);
            stats.writeCommonString(out, key.mProcess);
            stats.writeCommonString(out, key.mPackage);
            out.writeInt(src.mCount);
            out.writeLong(src.mDuration);
            out.writeInt(src.mActiveCount);
            if (src.mDurations != null) {
                out.writeInt(1);
                src.mDurations.writeToParcel(out);
            } else {
                out.writeInt(0);
                out.writeInt(src.mActiveProcState);
                out.writeLong(src.mActiveDuration);
            }
        }
    }

    public String readFromParcel(ProcessStats stats, Parcel in, int parcelVersion) {
        int NSRC = in.readInt();
        if (NSRC < 0 || NSRC > UserHandle.PER_USER_RANGE) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Association with bad src count: ");
            stringBuilder.append(NSRC);
            return stringBuilder.toString();
        }
        for (int isrc = 0; isrc < NSRC; isrc++) {
            SourceKey key = new SourceKey(in.readInt(), stats.readCommonString(in, parcelVersion), stats.readCommonString(in, parcelVersion));
            SourceState src = new SourceState(key);
            src.mCount = in.readInt();
            src.mDuration = in.readLong();
            src.mActiveCount = in.readInt();
            if (in.readInt() != 0) {
                src.makeDurations();
                if (!src.mDurations.readFromParcel(in)) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Duration table corrupt: ");
                    stringBuilder2.append(key);
                    stringBuilder2.append(" <- ");
                    stringBuilder2.append(src);
                    return stringBuilder2.toString();
                }
            } else {
                src.mActiveProcState = in.readInt();
                src.mActiveDuration = in.readLong();
            }
            this.mSources.put(key, src);
        }
        return null;
    }

    public void commitStateTime(long nowUptime) {
        if (isInUse()) {
            for (int isrc = this.mSources.size() - 1; isrc >= 0; isrc--) {
                SourceState src = (SourceState) this.mSources.valueAt(isrc);
                if (src.mNesting > 0) {
                    src.mDuration += nowUptime - src.mStartUptime;
                    src.mStartUptime = nowUptime;
                }
                if (src.mActiveStartUptime > 0) {
                    long duration = (src.mActiveDuration + nowUptime) - src.mActiveStartUptime;
                    if (src.mDurations != null) {
                        src.mDurations.addDuration(src.mActiveProcState, duration);
                    } else {
                        src.mActiveDuration = duration;
                    }
                    src.mActiveStartUptime = nowUptime;
                }
            }
        }
    }

    public boolean hasProcessOrPackage(String procName) {
        int NSRC = this.mSources.size();
        for (int isrc = 0; isrc < NSRC; isrc++) {
            SourceKey key = (SourceKey) this.mSources.keyAt(isrc);
            if (procName.equals(key.mProcess) || procName.equals(key.mPackage)) {
                return true;
            }
        }
        return false;
    }

    public void dumpStats(PrintWriter pw, String prefix, String prefixInner, String headerPrefix, long now, long totalTime, String reqPackage, boolean dumpDetails, boolean dumpAll) {
        AssociationState associationState = this;
        PrintWriter printWriter = pw;
        String str = prefixInner;
        String str2 = reqPackage;
        if (dumpAll) {
            pw.print(prefix);
            printWriter.print("mNumActive=");
            printWriter.println(associationState.mNumActive);
        }
        int NSRC = associationState.mSources.size();
        int isrc = 0;
        while (isrc < NSRC) {
            int NSRC2;
            SourceKey key = (SourceKey) associationState.mSources.keyAt(isrc);
            SourceState src = (SourceState) associationState.mSources.valueAt(isrc);
            if (str2 == null || str2.equals(key.mProcess) || str2.equals(key.mPackage)) {
                long duration;
                int i;
                SourceState src2;
                printWriter.print(str);
                printWriter.print("<- ");
                printWriter.print(key.mProcess);
                printWriter.print("/");
                UserHandle.formatUid(printWriter, key.mUid);
                String str3 = ")";
                if (key.mPackage != null) {
                    printWriter.print(" (");
                    printWriter.print(key.mPackage);
                    printWriter.print(str3);
                }
                String str4 = ":";
                printWriter.println(str4);
                printWriter.print(str);
                printWriter.print("   Total count ");
                printWriter.print(src.mCount);
                long duration2 = src.mDuration;
                if (src.mNesting > 0) {
                    duration = duration2 + (now - src.mStartUptime);
                } else {
                    duration = duration2;
                }
                String str5 = " / ";
                if (dumpAll) {
                    printWriter.print(": Duration ");
                    TimeUtils.formatDuration(duration, printWriter);
                    printWriter.print(str5);
                } else {
                    printWriter.print(": time ");
                }
                NSRC2 = NSRC;
                DumpUtils.printPercent(printWriter, ((double) duration) / ((double) totalTime));
                if (src.mNesting > 0) {
                    printWriter.print(" (running");
                    if (src.mProcState != -1) {
                        printWriter.print(str5);
                        printWriter.print(DumpUtils.STATE_NAMES[src.mProcState]);
                        printWriter.print(" #");
                        printWriter.print(src.mProcStateSeq);
                    }
                    printWriter.print(str3);
                }
                pw.println();
                SourceKey sourceKey;
                if (src.mActiveCount <= 0 && src.mDurations == null && src.mActiveDuration == 0 && src.mActiveStartUptime == 0) {
                    i = -1;
                    src2 = src;
                    sourceKey = key;
                } else {
                    printWriter.print(str);
                    printWriter.print("   Active count ");
                    printWriter.print(src.mActiveCount);
                    if (dumpDetails) {
                        if (dumpAll) {
                            printWriter.print(src.mDurations != null ? " (multi-field)" : " (inline)");
                        }
                        printWriter.println(str4);
                        i = -1;
                        src2 = src;
                        dumpTime(pw, prefixInner, src, totalTime, now, dumpDetails, dumpAll);
                    } else {
                        i = -1;
                        src2 = src;
                        sourceKey = key;
                        printWriter.print(": ");
                        dumpActiveDurationSummary(pw, src2, totalTime, now, dumpAll);
                        pw.println();
                    }
                }
                if (dumpAll) {
                    SourceState src3 = src2;
                    if (src3.mInTrackingList) {
                        printWriter.print(str);
                        printWriter.print("   mInTrackingList=");
                        printWriter.println(src3.mInTrackingList);
                    }
                    if (src3.mProcState != i) {
                        printWriter.print(str);
                        printWriter.print("   mProcState=");
                        printWriter.print(DumpUtils.STATE_NAMES[src3.mProcState]);
                        printWriter.print(" mProcStateSeq=");
                        printWriter.println(src3.mProcStateSeq);
                    }
                }
            } else {
                NSRC2 = NSRC;
                long j = totalTime;
            }
            isrc++;
            associationState = this;
            str2 = reqPackage;
            NSRC = NSRC2;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dumpActiveDurationSummary(PrintWriter pw, SourceState src, long totalTime, long now, boolean dumpAll) {
        PrintWriter printWriter = pw;
        long duration = dumpTime(null, null, src, totalTime, now, false, false);
        if (duration < 0) {
            duration = -duration;
        }
        if (dumpAll) {
            pw.print("Duration ");
            TimeUtils.formatDuration(duration, pw);
            pw.print(" / ");
        } else {
            pw.print("time ");
        }
        DumpUtils.printPercent(pw, ((double) duration) / ((double) totalTime));
        if (src.mActiveStartUptime > 0) {
            pw.print(" (running)");
        }
        pw.println();
    }

    /* Access modifiers changed, original: 0000 */
    public long dumpTime(PrintWriter pw, String prefix, SourceState src, long overallTime, long now, boolean dumpDetails, boolean dumpAll) {
        String str;
        String str2;
        String str3;
        String str4;
        String str5;
        long totalTime;
        PrintWriter printWriter = pw;
        SourceState sourceState = src;
        long j = overallTime;
        long totalTime2 = 0;
        boolean isRunning = false;
        int iprocstate = 0;
        while (true) {
            str = " / ";
            str2 = "Duration ";
            str3 = "time ";
            str4 = ": ";
            str5 = "  ";
            if (iprocstate >= 14) {
                break;
            }
            String running;
            long time = sourceState.mDurations != null ? sourceState.mDurations.getValueForId((byte) iprocstate) : sourceState.mActiveProcState == iprocstate ? sourceState.mDuration : 0;
            totalTime = totalTime2;
            if (sourceState.mActiveStartUptime == 0 || sourceState.mActiveProcState != iprocstate) {
                running = null;
            } else {
                running = " (running)";
                isRunning = true;
                time += now - sourceState.mActiveStartUptime;
            }
            if (time != 0) {
                if (printWriter != null) {
                    pw.print(prefix);
                    printWriter.print(str5);
                    printWriter.print(DumpUtils.STATE_LABELS[iprocstate]);
                    printWriter.print(str4);
                    if (dumpAll) {
                        printWriter.print(str2);
                        TimeUtils.formatDuration(time, printWriter);
                        printWriter.print(str);
                    } else {
                        printWriter.print(str3);
                    }
                    DumpUtils.printPercent(printWriter, ((double) time) / ((double) j));
                    if (running != null) {
                        printWriter.print(running);
                    }
                    pw.println();
                }
                totalTime2 = totalTime + time;
            } else {
                totalTime2 = totalTime;
            }
            iprocstate++;
        }
        totalTime = totalTime2;
        if (totalTime == 0 || printWriter == null) {
            totalTime2 = totalTime;
        } else {
            pw.print(prefix);
            printWriter.print(str5);
            printWriter.print(DumpUtils.STATE_LABEL_TOTAL);
            printWriter.print(str4);
            if (dumpAll) {
                printWriter.print(str2);
                totalTime2 = totalTime;
                TimeUtils.formatDuration(totalTime2, printWriter);
                printWriter.print(str);
            } else {
                totalTime2 = totalTime;
                printWriter.print(str3);
            }
            DumpUtils.printPercent(printWriter, ((double) totalTime2) / ((double) j));
            pw.println();
        }
        return isRunning ? -totalTime2 : totalTime2;
    }

    public void dumpTimesCheckin(PrintWriter pw, String pkgName, int uid, long vers, String associationName, long now) {
        AssociationState associationState = this;
        PrintWriter printWriter = pw;
        int NSRC = associationState.mSources.size();
        int isrc = 0;
        while (isrc < NSRC) {
            int NSRC2;
            SourceKey key = (SourceKey) associationState.mSources.keyAt(isrc);
            SourceState src = (SourceState) associationState.mSources.valueAt(isrc);
            printWriter.print("pkgasc");
            String str = ",";
            printWriter.print(str);
            pw.print(pkgName);
            printWriter.print(str);
            printWriter.print(uid);
            printWriter.print(str);
            printWriter.print(vers);
            printWriter.print(str);
            printWriter.print(associationName);
            printWriter.print(str);
            printWriter.print(key.mProcess);
            printWriter.print(str);
            printWriter.print(key.mUid);
            printWriter.print(str);
            printWriter.print(src.mCount);
            long duration = src.mDuration;
            if (src.mNesting > 0) {
                duration += now - src.mStartUptime;
            }
            printWriter.print(str);
            printWriter.print(duration);
            printWriter.print(str);
            printWriter.print(src.mActiveCount);
            long timeNow = src.mActiveStartUptime != 0 ? now - src.mActiveStartUptime : 0;
            long duration2;
            if (src.mDurations != null) {
                int N = src.mDurations.getKeyCount();
                long j = duration;
                duration = null;
                long duration3 = j;
                while (duration < N) {
                    int dkey = src.mDurations.getKeyAt(duration);
                    duration3 = src.mDurations.getValue(dkey);
                    if (dkey == src.mActiveProcState) {
                        duration3 += timeNow;
                    }
                    duration2 = duration3;
                    int procState = SparseMappingTable.getIdFromKey(dkey);
                    printWriter.print(str);
                    NSRC2 = NSRC;
                    DumpUtils.printArrayEntry(printWriter, DumpUtils.STATE_TAGS, procState, 1);
                    printWriter.print(':');
                    printWriter.print(duration2);
                    duration++;
                    duration3 = duration2;
                    NSRC = NSRC2;
                    duration2 = uid;
                    long j2 = vers;
                }
                NSRC2 = NSRC;
                duration2 = duration3;
            } else {
                NSRC2 = NSRC;
                duration2 = src.mActiveDuration + timeNow;
                if (duration2 != 0) {
                    printWriter.print(str);
                    DumpUtils.printArrayEntry(printWriter, DumpUtils.STATE_TAGS, src.mActiveProcState, 1);
                    printWriter.print(':');
                    printWriter.print(duration2);
                }
            }
            pw.println();
            isrc++;
            associationState = this;
            NSRC = NSRC2;
        }
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId, long now) {
        AssociationState associationState = this;
        ProtoOutputStream protoOutputStream = proto;
        long token = proto.start(fieldId);
        protoOutputStream.write(1138166333441L, associationState.mName);
        int NSRC = associationState.mSources.size();
        int isrc = 0;
        while (isrc < NSRC) {
            long token2;
            int NSRC2;
            ProtoOutputStream protoOutputStream2;
            SourceKey key = (SourceKey) associationState.mSources.keyAt(isrc);
            SourceState src = (SourceState) associationState.mSources.valueAt(isrc);
            long sourceToken = protoOutputStream.start(2246267895810L);
            protoOutputStream.write(1138166333442L, key.mProcess);
            protoOutputStream.write(1138166333447L, key.mPackage);
            protoOutputStream.write(1120986464257L, key.mUid);
            protoOutputStream.write(1120986464259L, src.mCount);
            long duration = src.mDuration;
            if (src.mNesting > 0) {
                duration += now - src.mStartUptime;
            }
            protoOutputStream.write(1112396529668L, duration);
            if (src.mActiveCount != 0) {
                protoOutputStream.write(1120986464261L, src.mActiveCount);
            }
            long timeNow = src.mActiveStartUptime != 0 ? now - src.mActiveStartUptime : 0;
            long stateToken;
            long duration2;
            ProtoOutputStream protoOutputStream3;
            if (src.mDurations != null) {
                int i;
                int N;
                int N2 = src.mDurations.getKeyCount();
                int i2 = 0;
                while (i2 < N2) {
                    int dkey = src.mDurations.getKeyAt(i2);
                    long duration3 = src.mDurations.getValue(dkey);
                    if (dkey == src.mActiveProcState) {
                        duration3 += timeNow;
                    }
                    int procState = SparseMappingTable.getIdFromKey(dkey);
                    long duration4 = duration3;
                    long j = 2246267895814L;
                    i = i2;
                    N = N2;
                    token2 = token;
                    stateToken = proto.start(2246267895814L);
                    long j2 = duration4;
                    NSRC2 = NSRC;
                    duration2 = j2;
                    DumpUtils.printProto(proto, 1159641169921L, DumpUtils.STATE_PROTO_ENUMS, procState, 1);
                    protoOutputStream3 = proto;
                    protoOutputStream3.write(1112396529666L, duration2);
                    protoOutputStream3.end(stateToken);
                    i2 = i + 1;
                    N2 = N;
                    NSRC = NSRC2;
                    token = token2;
                }
                i = i2;
                N = N2;
                token2 = token;
                NSRC2 = NSRC;
                protoOutputStream2 = proto;
            } else {
                protoOutputStream3 = proto;
                token2 = token;
                NSRC2 = NSRC;
                stateToken = src.mActiveDuration + timeNow;
                if (stateToken != 0) {
                    duration2 = protoOutputStream3.start(2246267895814L);
                    protoOutputStream2 = protoOutputStream3;
                    DumpUtils.printProto(proto, 1159641169921L, DumpUtils.STATE_PROTO_ENUMS, src.mActiveProcState, 1);
                    protoOutputStream2.write(1112396529666L, stateToken);
                    protoOutputStream2.end(duration2);
                } else {
                    protoOutputStream2 = protoOutputStream3;
                }
                long j3 = stateToken;
            }
            protoOutputStream2.end(sourceToken);
            isrc++;
            protoOutputStream = protoOutputStream2;
            NSRC = NSRC2;
            token = token2;
            associationState = this;
        }
        protoOutputStream.end(token);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AssociationState{");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
        stringBuilder.append(this.mName);
        stringBuilder.append(" pkg=");
        stringBuilder.append(this.mPackageState.mPackageName);
        stringBuilder.append(" proc=");
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this.mProc)));
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
