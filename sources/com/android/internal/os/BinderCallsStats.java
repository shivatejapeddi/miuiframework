package com.android.internal.os;

import android.os.Binder;
import android.os.Process;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.util.ArrayMap;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.os.BinderInternal.CallSession;
import com.android.internal.os.BinderInternal.Observer;
import com.android.internal.os.CachedDeviceState.Readonly;
import com.android.internal.os.CachedDeviceState.TimeInStateStopwatch;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.ToDoubleFunction;

public class BinderCallsStats implements Observer {
    private static final int CALL_SESSIONS_POOL_SIZE = 100;
    private static final String DEBUG_ENTRY_PREFIX = "__DEBUG_";
    public static final boolean DEFAULT_TRACK_DIRECT_CALLING_UID = true;
    public static final boolean DEFAULT_TRACK_SCREEN_INTERACTIVE = false;
    public static final boolean DETAILED_TRACKING_DEFAULT = true;
    public static final boolean ENABLED_DEFAULT = true;
    private static final String EXCEPTION_COUNT_OVERFLOW_NAME = "overflow";
    public static final int MAX_BINDER_CALL_STATS_COUNT_DEFAULT = 1500;
    private static final int MAX_EXCEPTION_COUNT_SIZE = 50;
    private static final Class<? extends Binder> OVERFLOW_BINDER = OverflowBinder.class;
    private static final int OVERFLOW_DIRECT_CALLING_UID = -1;
    private static final boolean OVERFLOW_SCREEN_INTERACTIVE = false;
    private static final int OVERFLOW_TRANSACTION_CODE = -1;
    public static final int PERIODIC_SAMPLING_INTERVAL_DEFAULT = 1000;
    private static final String TAG = "BinderCallsStats";
    private boolean mAddDebugEntries = false;
    private TimeInStateStopwatch mBatteryStopwatch;
    private final Queue<CallSession> mCallSessionsPool = new ConcurrentLinkedQueue();
    private long mCallStatsCount = 0;
    private boolean mDetailedTracking = true;
    private Readonly mDeviceState;
    @GuardedBy({"mLock"})
    private final ArrayMap<String, Integer> mExceptionCounts = new ArrayMap();
    private final Object mLock = new Object();
    private int mMaxBinderCallStatsCount = 1500;
    private int mPeriodicSamplingInterval = 1000;
    private final Random mRandom;
    private long mStartCurrentTime = System.currentTimeMillis();
    private long mStartElapsedTime = SystemClock.elapsedRealtime();
    private boolean mTrackDirectCallingUid = true;
    private boolean mTrackScreenInteractive = false;
    @GuardedBy({"mLock"})
    private final SparseArray<UidEntry> mUidEntries = new SparseArray();

    @VisibleForTesting
    public static class CallStat {
        public final Class<? extends Binder> binderClass;
        public long callCount;
        public final int callingUid;
        public long cpuTimeMicros;
        public long exceptionCount;
        public long latencyMicros;
        public long maxCpuTimeMicros;
        public long maxLatencyMicros;
        public long maxReplySizeBytes;
        public long maxRequestSizeBytes;
        public long recordedCallCount;
        public final boolean screenInteractive;
        public final int transactionCode;

        CallStat(int callingUid, Class<? extends Binder> binderClass, int transactionCode, boolean screenInteractive) {
            this.callingUid = callingUid;
            this.binderClass = binderClass;
            this.transactionCode = transactionCode;
            this.screenInteractive = screenInteractive;
        }
    }

    public static class CallStatKey {
        public Class<? extends Binder> binderClass;
        public int callingUid;
        private boolean screenInteractive;
        public int transactionCode;

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            CallStatKey key = (CallStatKey) o;
            if (!(this.callingUid == key.callingUid && this.transactionCode == key.transactionCode && this.screenInteractive == key.screenInteractive && this.binderClass.equals(key.binderClass))) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return (((((this.binderClass.hashCode() * 31) + this.transactionCode) * 31) + this.callingUid) * 31) + (this.screenInteractive ? MetricsEvent.AUTOFILL_SERVICE_DISABLED_APP : MetricsEvent.ANOMALY_TYPE_UNOPTIMIZED_BT);
        }
    }

    public static class ExportedCallStat {
        Class<? extends Binder> binderClass;
        public long callCount;
        public int callingUid;
        public String className;
        public long cpuTimeMicros;
        public long exceptionCount;
        public long latencyMicros;
        public long maxCpuTimeMicros;
        public long maxLatencyMicros;
        public long maxReplySizeBytes;
        public long maxRequestSizeBytes;
        public String methodName;
        public long recordedCallCount;
        public boolean screenInteractive;
        int transactionCode;
        public int workSourceUid;
    }

    public static class Injector {
        public Random getRandomGenerator() {
            return new Random();
        }
    }

    private static class OverflowBinder extends Binder {
        private OverflowBinder() {
        }
    }

    @VisibleForTesting
    public static class UidEntry {
        public long callCount;
        public long cpuTimeMicros;
        private Map<CallStatKey, CallStat> mCallStats = new ArrayMap();
        private CallStatKey mTempKey = new CallStatKey();
        public long recordedCallCount;
        public int workSourceUid;

        UidEntry(int uid) {
            this.workSourceUid = uid;
        }

        /* Access modifiers changed, original: 0000 */
        public CallStat get(int callingUid, Class<? extends Binder> binderClass, int transactionCode, boolean screenInteractive) {
            CallStatKey callStatKey = this.mTempKey;
            callStatKey.callingUid = callingUid;
            callStatKey.binderClass = binderClass;
            callStatKey.transactionCode = transactionCode;
            callStatKey.screenInteractive = screenInteractive;
            return (CallStat) this.mCallStats.get(this.mTempKey);
        }

        /* Access modifiers changed, original: 0000 */
        public CallStat getOrCreate(int callingUid, Class<? extends Binder> binderClass, int transactionCode, boolean screenInteractive, boolean maxCallStatsReached) {
            CallStat mapCallStat = get(callingUid, binderClass, transactionCode, screenInteractive);
            if (mapCallStat == null) {
                if (maxCallStatsReached) {
                    mapCallStat = get(-1, BinderCallsStats.OVERFLOW_BINDER, -1, false);
                    if (mapCallStat != null) {
                        return mapCallStat;
                    }
                    callingUid = -1;
                    binderClass = BinderCallsStats.OVERFLOW_BINDER;
                    transactionCode = -1;
                    screenInteractive = false;
                }
                mapCallStat = new CallStat(callingUid, binderClass, transactionCode, screenInteractive);
                CallStatKey key = new CallStatKey();
                key.callingUid = callingUid;
                key.binderClass = binderClass;
                key.transactionCode = transactionCode;
                key.screenInteractive = screenInteractive;
                this.mCallStats.put(key, mapCallStat);
            }
            return mapCallStat;
        }

        public Collection<CallStat> getCallStatsList() {
            return this.mCallStats.values();
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("UidEntry{cpuTimeMicros=");
            stringBuilder.append(this.cpuTimeMicros);
            stringBuilder.append(", callCount=");
            stringBuilder.append(this.callCount);
            stringBuilder.append(", mCallStats=");
            stringBuilder.append(this.mCallStats);
            stringBuilder.append('}');
            return stringBuilder.toString();
        }

        public boolean equals(Object o) {
            boolean z = true;
            if (this == o) {
                return true;
            }
            if (this.workSourceUid != ((UidEntry) o).workSourceUid) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return this.workSourceUid;
        }
    }

    public BinderCallsStats(Injector injector) {
        this.mRandom = injector.getRandomGenerator();
    }

    public void setDeviceState(Readonly deviceState) {
        TimeInStateStopwatch timeInStateStopwatch = this.mBatteryStopwatch;
        if (timeInStateStopwatch != null) {
            timeInStateStopwatch.close();
        }
        this.mDeviceState = deviceState;
        this.mBatteryStopwatch = deviceState.createTimeOnBatteryStopwatch();
    }

    public CallSession callStarted(Binder binder, int code, int workSourceUid) {
        Readonly readonly = this.mDeviceState;
        if (readonly == null || readonly.isCharging()) {
            return null;
        }
        CallSession s = obtainCallSession();
        s.binderClass = binder.getClass();
        s.transactionCode = code;
        s.exceptionThrown = false;
        s.cpuTimeStarted = -1;
        s.timeStarted = -1;
        if (shouldRecordDetailedData()) {
            s.cpuTimeStarted = getThreadTimeMicro();
            s.timeStarted = getElapsedRealtimeMicro();
        }
        return s;
    }

    private CallSession obtainCallSession() {
        CallSession s = (CallSession) this.mCallSessionsPool.poll();
        return s == null ? new CallSession() : s;
    }

    public void callEnded(CallSession s, int parcelRequestSize, int parcelReplySize, int workSourceUid) {
        if (s != null) {
            processCallEnded(s, parcelRequestSize, parcelReplySize, workSourceUid);
            if (this.mCallSessionsPool.size() < 100) {
                this.mCallSessionsPool.add(s);
            }
        }
    }

    private void processCallEnded(CallSession s, int parcelRequestSize, int parcelReplySize, int workSourceUid) {
        long duration;
        long latencyDuration;
        boolean isScreenInteractive;
        int callingUid;
        Throwable th;
        long j;
        CallSession callSession = s;
        long j2 = 0;
        boolean recordCall = callSession.cpuTimeStarted >= 0;
        if (recordCall) {
            duration = getThreadTimeMicro() - callSession.cpuTimeStarted;
            latencyDuration = getElapsedRealtimeMicro() - callSession.timeStarted;
        } else {
            duration = 0;
            latencyDuration = 0;
        }
        if (this.mTrackScreenInteractive) {
            isScreenInteractive = this.mDeviceState.isScreenInteractive();
        } else {
            isScreenInteractive = false;
        }
        boolean screenInteractive = isScreenInteractive;
        if (this.mTrackDirectCallingUid) {
            callingUid = getCallingUid();
        } else {
            callingUid = -1;
        }
        int callingUid2 = callingUid;
        Object obj = this.mLock;
        synchronized (obj) {
            int i;
            boolean z;
            long j3;
            int i2;
            boolean z2;
            try {
                Object obj2;
                if (this.mDeviceState == null) {
                    i = parcelReplySize;
                    z = recordCall;
                    j3 = latencyDuration;
                    obj2 = obj;
                    i2 = callingUid2;
                    z2 = screenInteractive;
                    callingUid2 = parcelRequestSize;
                    screenInteractive = duration;
                } else if (this.mDeviceState.isCharging()) {
                    i = parcelReplySize;
                    z = recordCall;
                    j3 = latencyDuration;
                    obj2 = obj;
                    i2 = callingUid2;
                    z2 = screenInteractive;
                    callingUid2 = parcelRequestSize;
                    screenInteractive = duration;
                } else {
                    UidEntry uidEntry = getUidEntry(workSourceUid);
                    long j4 = 1;
                    uidEntry.callCount++;
                    CallStat callStat;
                    if (recordCall) {
                        uidEntry.cpuTimeMicros += duration;
                        uidEntry.recordedCallCount++;
                        try {
                            j2 = obj;
                            try {
                                callStat = uidEntry.getOrCreate(callingUid2, callSession.binderClass, callSession.transactionCode, screenInteractive, this.mCallStatsCount >= ((long) this.mMaxBinderCallStatsCount));
                                recordCall = callStat.callCount == 0;
                                if (recordCall) {
                                    try {
                                        this.mCallStatsCount++;
                                    } catch (Throwable th2) {
                                        th = th2;
                                        callingUid2 = parcelRequestSize;
                                        i = parcelReplySize;
                                        j = duration;
                                        j3 = latencyDuration;
                                    }
                                }
                                callStat.callCount++;
                                callStat.recordedCallCount++;
                                callStat.cpuTimeMicros += duration;
                                callStat.maxCpuTimeMicros = Math.max(callStat.maxCpuTimeMicros, duration);
                                callStat.latencyMicros += latencyDuration;
                                callStat.maxLatencyMicros = Math.max(callStat.maxLatencyMicros, latencyDuration);
                                if (this.mDetailedTracking) {
                                    long j5 = callStat.exceptionCount;
                                    if (!callSession.exceptionThrown) {
                                        j4 = 0;
                                    }
                                    callStat.exceptionCount = j5 + j4;
                                    boolean isNewCallStat = recordCall;
                                    try {
                                        callStat.maxRequestSizeBytes = Math.max(callStat.maxRequestSizeBytes, (long) parcelRequestSize);
                                        callStat.maxReplySizeBytes = Math.max(callStat.maxReplySizeBytes, (long) parcelReplySize);
                                    } catch (Throwable th3) {
                                        th = th3;
                                        throw th;
                                    }
                                }
                                callingUid2 = parcelRequestSize;
                                i = parcelReplySize;
                                j = duration;
                                j3 = latencyDuration;
                                boolean z3 = recordCall;
                            } catch (Throwable th4) {
                                th = th4;
                                callingUid2 = parcelRequestSize;
                                i = parcelReplySize;
                                screenInteractive = duration;
                                j3 = latencyDuration;
                                throw th;
                            }
                        } catch (Throwable th5) {
                            th = th5;
                            i = parcelReplySize;
                            j3 = latencyDuration;
                            j2 = obj;
                            i2 = callingUid2;
                            z2 = screenInteractive;
                            callingUid2 = parcelRequestSize;
                            screenInteractive = duration;
                            throw th;
                        }
                    }
                    i = parcelReplySize;
                    z = recordCall;
                    j3 = latencyDuration;
                    j2 = obj;
                    i2 = callingUid2;
                    z2 = screenInteractive;
                    callingUid2 = parcelRequestSize;
                    screenInteractive = duration;
                    callStat = uidEntry.get(i2, callSession.binderClass, callSession.transactionCode, z2);
                    if (callStat != null) {
                        callStat.callCount++;
                    }
                    return;
                }
            } catch (Throwable th6) {
                th = th6;
                i = parcelReplySize;
                z = recordCall;
                j3 = latencyDuration;
                j2 = obj;
                i2 = callingUid2;
                z2 = screenInteractive;
                callingUid2 = parcelRequestSize;
                throw th;
            }
        }
    }

    private UidEntry getUidEntry(int uid) {
        UidEntry uidEntry = (UidEntry) this.mUidEntries.get(uid);
        if (uidEntry != null) {
            return uidEntry;
        }
        uidEntry = new UidEntry(uid);
        this.mUidEntries.put(uid, uidEntry);
        return uidEntry;
    }

    public void callThrewException(CallSession s, Exception exception) {
        if (s != null) {
            int i = 1;
            s.exceptionThrown = true;
            try {
                String className = exception.getClass().getName();
                synchronized (this.mLock) {
                    if (this.mExceptionCounts.size() >= 50) {
                        className = EXCEPTION_COUNT_OVERFLOW_NAME;
                    }
                    Integer count = (Integer) this.mExceptionCounts.get(className);
                    ArrayMap arrayMap = this.mExceptionCounts;
                    if (count != null) {
                        i = 1 + count.intValue();
                    }
                    arrayMap.put(className, Integer.valueOf(i));
                }
            } catch (RuntimeException e) {
                Slog.wtf(TAG, "Unexpected exception while updating mExceptionCounts");
            }
        }
    }

    private Method getDefaultTransactionNameMethod(Class<? extends Binder> binder) {
        try {
            return binder.getMethod("getDefaultTransactionName", new Class[]{Integer.TYPE});
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private String resolveTransactionCode(Method getDefaultTransactionName, int transactionCode) {
        if (getDefaultTransactionName == null) {
            return null;
        }
        try {
            return (String) getDefaultTransactionName.invoke(null, new Object[]{Integer.valueOf(transactionCode)});
        } catch (ClassCastException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<ExportedCallStat> getExportedCallStats() {
        if (!this.mDetailedTracking) {
            return new ArrayList();
        }
        ArrayList<ExportedCallStat> resultCallStats = new ArrayList();
        synchronized (this.mLock) {
            int uidEntriesSize = this.mUidEntries.size();
            for (int entryIdx = 0; entryIdx < uidEntriesSize; entryIdx++) {
                UidEntry entry = (UidEntry) this.mUidEntries.valueAt(entryIdx);
                for (CallStat stat : entry.getCallStatsList()) {
                    ExportedCallStat exported = new ExportedCallStat();
                    exported.workSourceUid = entry.workSourceUid;
                    exported.callingUid = stat.callingUid;
                    exported.className = stat.binderClass.getName();
                    exported.binderClass = stat.binderClass;
                    exported.transactionCode = stat.transactionCode;
                    exported.screenInteractive = stat.screenInteractive;
                    exported.cpuTimeMicros = stat.cpuTimeMicros;
                    exported.maxCpuTimeMicros = stat.maxCpuTimeMicros;
                    exported.latencyMicros = stat.latencyMicros;
                    exported.maxLatencyMicros = stat.maxLatencyMicros;
                    exported.recordedCallCount = stat.recordedCallCount;
                    exported.callCount = stat.callCount;
                    exported.maxRequestSizeBytes = stat.maxRequestSizeBytes;
                    exported.maxReplySizeBytes = stat.maxReplySizeBytes;
                    exported.exceptionCount = stat.exceptionCount;
                    resultCallStats.add(exported);
                }
            }
        }
        Method getDefaultTransactionName = null;
        String previousMethodName = null;
        resultCallStats.sort(-$$Lambda$BinderCallsStats$sqXweH5BoxhmZvI188ctqYiACRk.INSTANCE);
        Iterator it = resultCallStats.iterator();
        while (it.hasNext()) {
            String resolvedCode;
            ExportedCallStat exported2 = (ExportedCallStat) it.next();
            boolean isCodeDifferent = false;
            boolean isClassDifferent = null == null || !null.className.equals(exported2.className);
            if (isClassDifferent) {
                getDefaultTransactionName = getDefaultTransactionNameMethod(exported2.binderClass);
            }
            if (null == null || null.transactionCode != exported2.transactionCode) {
                isCodeDifferent = true;
            }
            if (isClassDifferent || isCodeDifferent) {
                String valueOf;
                resolvedCode = resolveTransactionCode(getDefaultTransactionName, exported2.transactionCode);
                if (resolvedCode == null) {
                    valueOf = String.valueOf(exported2.transactionCode);
                } else {
                    valueOf = resolvedCode;
                }
                resolvedCode = valueOf;
            } else {
                resolvedCode = previousMethodName;
            }
            previousMethodName = resolvedCode;
            exported2.methodName = resolvedCode;
        }
        if (this.mAddDebugEntries && this.mBatteryStopwatch != null) {
            resultCallStats.add(createDebugEntry("start_time_millis", this.mStartElapsedTime));
            resultCallStats.add(createDebugEntry("end_time_millis", SystemClock.elapsedRealtime()));
            resultCallStats.add(createDebugEntry("battery_time_millis", this.mBatteryStopwatch.getMillis()));
            resultCallStats.add(createDebugEntry("sampling_interval", (long) this.mPeriodicSamplingInterval));
        }
        return resultCallStats;
    }

    private ExportedCallStat createDebugEntry(String variableName, long value) {
        int uid = Process.myUid();
        ExportedCallStat callStat = new ExportedCallStat();
        callStat.className = "";
        callStat.workSourceUid = uid;
        callStat.callingUid = uid;
        callStat.recordedCallCount = 1;
        callStat.callCount = 1;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("__DEBUG_");
        stringBuilder.append(variableName);
        callStat.methodName = stringBuilder.toString();
        callStat.latencyMicros = value;
        return callStat;
    }

    public ArrayMap<String, Integer> getExportedExceptionStats() {
        ArrayMap arrayMap;
        synchronized (this.mLock) {
            arrayMap = new ArrayMap(this.mExceptionCounts);
        }
        return arrayMap;
    }

    public void dump(PrintWriter pw, AppIdToPackageMap packageMap, boolean verbose) {
        synchronized (this.mLock) {
            dumpLocked(pw, packageMap, verbose);
        }
    }

    private void dumpLocked(PrintWriter pw, AppIdToPackageMap packageMap, boolean verbose) {
        List<UidEntry> summaryEntries;
        String datasetSizeDesc;
        StringBuilder sb;
        String str;
        PrintWriter printWriter = pw;
        AppIdToPackageMap appIdToPackageMap = packageMap;
        long totalCallsCount = 0;
        long totalRecordedCallsCount = 0;
        long totalCpuTime = 0;
        printWriter.print("Start time: ");
        printWriter.println(DateFormat.format((CharSequence) "yyyy-MM-dd HH:mm:ss", this.mStartCurrentTime));
        printWriter.print("On battery time (ms): ");
        TimeInStateStopwatch timeInStateStopwatch = this.mBatteryStopwatch;
        printWriter.println(timeInStateStopwatch != null ? timeInStateStopwatch.getMillis() : 0);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Sampling interval period: ");
        stringBuilder.append(this.mPeriodicSamplingInterval);
        printWriter.println(stringBuilder.toString());
        List<UidEntry> entries = new ArrayList();
        int uidEntriesSize = this.mUidEntries.size();
        for (int i = 0; i < uidEntriesSize; i++) {
            UidEntry e = (UidEntry) this.mUidEntries.valueAt(i);
            entries.add(e);
            totalCpuTime += e.cpuTimeMicros;
            totalRecordedCallsCount += e.recordedCallCount;
            totalCallsCount += e.callCount;
        }
        entries.sort(Comparator.comparingDouble(-$$Lambda$BinderCallsStats$iPOmTqbqUiHzgsAugINuZgf9tls.INSTANCE).reversed());
        String str2 = "";
        String datasetSizeDesc2 = verbose ? str2 : "(top 90% by cpu time) ";
        StringBuilder sb2 = new StringBuilder();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Per-UID raw data ");
        stringBuilder2.append(datasetSizeDesc2);
        stringBuilder2.append("(package/uid, worksource, call_desc, screen_interactive, cpu_time_micros, max_cpu_time_micros, latency_time_micros, max_latency_time_micros, exception_count, max_request_size_bytes, max_reply_size_bytes, recorded_call_count, call_count):");
        printWriter.println(stringBuilder2.toString());
        List<ExportedCallStat> exportedCallStats = getExportedCallStats();
        exportedCallStats.sort(-$$Lambda$BinderCallsStats$233x_Qux4c_AiqShYaWwvFplEXs.INSTANCE);
        Iterator it = exportedCallStats.iterator();
        while (true) {
            int uidEntriesSize2 = uidEntriesSize;
            if (!it.hasNext()) {
                break;
            }
            ExportedCallStat e2 = (ExportedCallStat) it.next();
            List<ExportedCallStat> exportedCallStats2 = exportedCallStats;
            Iterator it2 = it;
            if (e2.methodName.startsWith("__DEBUG_")) {
                exportedCallStats = exportedCallStats2;
                uidEntriesSize = uidEntriesSize2;
                it = it2;
            } else {
                sb2.setLength(0);
                sb2.append("    ");
                sb2.append(appIdToPackageMap.mapUid(e2.callingUid));
                sb2.append(',');
                sb2.append(appIdToPackageMap.mapUid(e2.workSourceUid));
                sb2.append(',');
                sb2.append(e2.className);
                sb2.append('#');
                sb2.append(e2.methodName);
                sb2.append(',');
                sb2.append(e2.screenInteractive);
                sb2.append(',');
                sb2.append(e2.cpuTimeMicros);
                sb2.append(',');
                sb2.append(e2.maxCpuTimeMicros);
                sb2.append(',');
                sb2.append(e2.latencyMicros);
                sb2.append(',');
                sb2.append(e2.maxLatencyMicros);
                sb2.append(',');
                sb2.append(this.mDetailedTracking ? e2.exceptionCount : 95);
                sb2.append(',');
                sb2.append(this.mDetailedTracking ? e2.maxRequestSizeBytes : 95);
                sb2.append(',');
                sb2.append(this.mDetailedTracking ? e2.maxReplySizeBytes : 95);
                sb2.append(',');
                sb2.append(e2.recordedCallCount);
                sb2.append(',');
                sb2.append(e2.callCount);
                printWriter.println(sb2);
                exportedCallStats = exportedCallStats2;
                uidEntriesSize = uidEntriesSize2;
                it = it2;
            }
        }
        pw.println();
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("Per-UID Summary ");
        stringBuilder3.append(datasetSizeDesc2);
        stringBuilder3.append("(cpu_time, % of total cpu_time, recorded_call_count, call_count, package/uid):");
        printWriter.println(stringBuilder3.toString());
        if (verbose) {
            summaryEntries = entries;
        } else {
            summaryEntries = getHighestValues(entries, -$$Lambda$BinderCallsStats$xI0E0RpviGYsokEB7ojNx8LEbUc.INSTANCE, 0.9d);
        }
        Iterator it3 = summaryEntries.iterator();
        while (true) {
            List<UidEntry> entries2 = entries;
            if (!it3.hasNext()) {
                break;
            }
            UidEntry entry = (UidEntry) it3.next();
            String uidStr = appIdToPackageMap.mapUid(entry.workSourceUid);
            Object[] objArr = new Object[5];
            datasetSizeDesc = datasetSizeDesc2;
            sb = sb2;
            objArr[0] = Long.valueOf(entry.cpuTimeMicros);
            List<UidEntry> summaryEntries2 = summaryEntries;
            str = str2;
            objArr[1] = Double.valueOf((((double) entry.cpuTimeMicros) * 100.0d) / ((double) totalCpuTime));
            objArr[2] = Long.valueOf(entry.recordedCallCount);
            objArr[3] = Long.valueOf(entry.callCount);
            objArr[4] = uidStr;
            printWriter.println(String.format("  %10d %3.0f%% %8d %8d %s", objArr));
            appIdToPackageMap = packageMap;
            entries = entries2;
            datasetSizeDesc2 = datasetSizeDesc;
            sb2 = sb;
            str2 = str;
            summaryEntries = summaryEntries2;
        }
        str = str2;
        datasetSizeDesc = datasetSizeDesc2;
        sb = sb2;
        pw.println();
        printWriter.println(String.format("  Summary: total_cpu_time=%d, calls_count=%d, avg_call_cpu_time=%.0f", new Object[]{Long.valueOf(totalCpuTime), Long.valueOf(totalCallsCount), Double.valueOf(((double) totalCpuTime) / ((double) totalRecordedCallsCount))}));
        pw.println();
        printWriter.println("Exceptions thrown (exception_count, class_name):");
        List<Pair<String, Integer>> exceptionEntries = new ArrayList();
        this.mExceptionCounts.entrySet().iterator().forEachRemaining(new -$$Lambda$BinderCallsStats$Vota0PqfoPWckjXH35wE48myGdk(exceptionEntries));
        exceptionEntries.sort(-$$Lambda$BinderCallsStats$-YP-7pwoNn8TN0iTmo5Q1r2lQz0.INSTANCE);
        for (Pair<String, Integer> entry2 : exceptionEntries) {
            printWriter.println(String.format("  %6d %s", new Object[]{entry2.second, entry2.first}));
        }
        if (this.mPeriodicSamplingInterval != 1) {
            printWriter.println(str);
            printWriter.println("/!\\ Displayed data is sampled. See sampling interval at the top.");
        }
    }

    /* Access modifiers changed, original: protected */
    public long getThreadTimeMicro() {
        return SystemClock.currentThreadTimeMicro();
    }

    /* Access modifiers changed, original: protected */
    public int getCallingUid() {
        return Binder.getCallingUid();
    }

    /* Access modifiers changed, original: protected */
    public long getElapsedRealtimeMicro() {
        return SystemClock.elapsedRealtimeNanos() / 1000;
    }

    /* Access modifiers changed, original: protected */
    public boolean shouldRecordDetailedData() {
        return this.mRandom.nextInt() % this.mPeriodicSamplingInterval == 0;
    }

    public void setDetailedTracking(boolean enabled) {
        synchronized (this.mLock) {
            if (enabled != this.mDetailedTracking) {
                this.mDetailedTracking = enabled;
                reset();
            }
        }
    }

    public void setTrackScreenInteractive(boolean enabled) {
        synchronized (this.mLock) {
            if (enabled != this.mTrackScreenInteractive) {
                this.mTrackScreenInteractive = enabled;
                reset();
            }
        }
    }

    public void setTrackDirectCallerUid(boolean enabled) {
        synchronized (this.mLock) {
            if (enabled != this.mTrackDirectCallingUid) {
                this.mTrackDirectCallingUid = enabled;
                reset();
            }
        }
    }

    public void setAddDebugEntries(boolean addDebugEntries) {
        this.mAddDebugEntries = addDebugEntries;
    }

    public void setMaxBinderCallStats(int maxKeys) {
        if (maxKeys <= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Ignored invalid max value (value must be positive): ");
            stringBuilder.append(maxKeys);
            Slog.w(TAG, stringBuilder.toString());
            return;
        }
        synchronized (this.mLock) {
            if (maxKeys != this.mMaxBinderCallStatsCount) {
                this.mMaxBinderCallStatsCount = maxKeys;
                reset();
            }
        }
    }

    public void setSamplingInterval(int samplingInterval) {
        if (samplingInterval <= 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Ignored invalid sampling interval (value must be positive): ");
            stringBuilder.append(samplingInterval);
            Slog.w(TAG, stringBuilder.toString());
            return;
        }
        synchronized (this.mLock) {
            if (samplingInterval != this.mPeriodicSamplingInterval) {
                this.mPeriodicSamplingInterval = samplingInterval;
                reset();
            }
        }
    }

    public void reset() {
        synchronized (this.mLock) {
            this.mCallStatsCount = 0;
            this.mUidEntries.clear();
            this.mExceptionCounts.clear();
            this.mStartCurrentTime = System.currentTimeMillis();
            this.mStartElapsedTime = SystemClock.elapsedRealtime();
            if (this.mBatteryStopwatch != null) {
                this.mBatteryStopwatch.reset();
            }
        }
    }

    @VisibleForTesting
    public SparseArray<UidEntry> getUidEntries() {
        return this.mUidEntries;
    }

    @VisibleForTesting
    public ArrayMap<String, Integer> getExceptionCounts() {
        return this.mExceptionCounts;
    }

    @VisibleForTesting
    public static <T> List<T> getHighestValues(List<T> list, ToDoubleFunction<T> toDouble, double percentile) {
        List<T> sortedList = new ArrayList(list);
        sortedList.sort(Comparator.comparingDouble(toDouble).reversed());
        double total = 0.0d;
        for (T item : list) {
            total += toDouble.applyAsDouble(item);
        }
        List<T> result = new ArrayList();
        double runningSum = 0.0d;
        for (T item2 : sortedList) {
            if (runningSum > percentile * total) {
                break;
            }
            result.add(item2);
            runningSum += toDouble.applyAsDouble(item2);
        }
        return result;
    }

    private static int compareByCpuDesc(ExportedCallStat a, ExportedCallStat b) {
        return Long.compare(b.cpuTimeMicros, a.cpuTimeMicros);
    }

    private static int compareByBinderClassAndCode(ExportedCallStat a, ExportedCallStat b) {
        int result = a.className.compareTo(b.className);
        if (result != 0) {
            return result;
        }
        return Integer.compare(a.transactionCode, b.transactionCode);
    }
}
