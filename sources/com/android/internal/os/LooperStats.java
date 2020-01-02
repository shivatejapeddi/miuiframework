package com.android.internal.os;

import android.os.Handler;
import android.os.Looper.Observer;
import android.os.Message;
import android.os.SystemClock;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;
import com.android.internal.os.CachedDeviceState.Readonly;
import com.android.internal.os.CachedDeviceState.TimeInStateStopwatch;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

public class LooperStats implements Observer {
    public static final String DEBUG_ENTRY_PREFIX = "__DEBUG_";
    private static final boolean DISABLED_SCREEN_STATE_TRACKING_VALUE = false;
    private static final int SESSION_POOL_SIZE = 50;
    private boolean mAddDebugEntries = false;
    private TimeInStateStopwatch mBatteryStopwatch;
    private Readonly mDeviceState;
    @GuardedBy({"mLock"})
    private final SparseArray<Entry> mEntries = new SparseArray(512);
    private final int mEntriesSizeCap;
    private final Entry mHashCollisionEntry = new Entry("HASH_COLLISION");
    private final Object mLock = new Object();
    private final Entry mOverflowEntry = new Entry("OVERFLOW");
    private int mSamplingInterval;
    private final ConcurrentLinkedQueue<DispatchSession> mSessionPool = new ConcurrentLinkedQueue();
    private long mStartCurrentTime = System.currentTimeMillis();
    private long mStartElapsedTime = SystemClock.elapsedRealtime();
    private boolean mTrackScreenInteractive = false;

    private static class DispatchSession {
        static final DispatchSession NOT_SAMPLED = new DispatchSession();
        public long cpuStartMicro;
        public long startTimeMicro;
        public long systemUptimeMillis;

        private DispatchSession() {
        }
    }

    private static class Entry {
        public long cpuUsageMicro;
        public long delayMillis;
        public long exceptionCount;
        public final Handler handler;
        public final boolean isInteractive;
        public long maxCpuUsageMicro;
        public long maxDelayMillis;
        public long maxLatencyMicro;
        public long messageCount;
        public final String messageName;
        public long recordedDelayMessageCount;
        public long recordedMessageCount;
        public long totalLatencyMicro;
        public final int workSourceUid;

        Entry(Message msg, boolean isInteractive) {
            this.workSourceUid = msg.workSourceUid;
            this.handler = msg.getTarget();
            this.messageName = this.handler.getMessageName(msg);
            this.isInteractive = isInteractive;
        }

        Entry(String specialEntryName) {
            this.workSourceUid = -1;
            this.messageName = specialEntryName;
            this.handler = null;
            this.isInteractive = false;
        }

        /* Access modifiers changed, original: 0000 */
        public void reset() {
            this.messageCount = 0;
            this.recordedMessageCount = 0;
            this.exceptionCount = 0;
            this.totalLatencyMicro = 0;
            this.maxLatencyMicro = 0;
            this.cpuUsageMicro = 0;
            this.maxCpuUsageMicro = 0;
            this.delayMillis = 0;
            this.maxDelayMillis = 0;
            this.recordedDelayMessageCount = 0;
        }

        static int idFor(Message msg, boolean isInteractive) {
            int result = (((((((7 * 31) + msg.workSourceUid) * 31) + msg.getTarget().getLooper().getThread().hashCode()) * 31) + msg.getTarget().getClass().hashCode()) * 31) + (isInteractive ? MetricsEvent.AUTOFILL_SERVICE_DISABLED_APP : MetricsEvent.ANOMALY_TYPE_UNOPTIMIZED_BT);
            if (msg.getCallback() != null) {
                return (result * 31) + msg.getCallback().getClass().hashCode();
            }
            return (result * 31) + msg.what;
        }
    }

    public static class ExportedEntry {
        public final long cpuUsageMicros;
        public final long delayMillis;
        public final long exceptionCount;
        public final String handlerClassName;
        public final boolean isInteractive;
        public final long maxCpuUsageMicros;
        public final long maxDelayMillis;
        public final long maxLatencyMicros;
        public final long messageCount;
        public final String messageName;
        public final long recordedDelayMessageCount;
        public final long recordedMessageCount;
        public final String threadName;
        public final long totalLatencyMicros;
        public final int workSourceUid;

        ExportedEntry(Entry entry) {
            this.workSourceUid = entry.workSourceUid;
            if (entry.handler != null) {
                this.handlerClassName = entry.handler.getClass().getName();
                this.threadName = entry.handler.getLooper().getThread().getName();
            } else {
                String str = "";
                this.handlerClassName = str;
                this.threadName = str;
            }
            this.isInteractive = entry.isInteractive;
            this.messageName = entry.messageName;
            this.messageCount = entry.messageCount;
            this.recordedMessageCount = entry.recordedMessageCount;
            this.exceptionCount = entry.exceptionCount;
            this.totalLatencyMicros = entry.totalLatencyMicro;
            this.maxLatencyMicros = entry.maxLatencyMicro;
            this.cpuUsageMicros = entry.cpuUsageMicro;
            this.maxCpuUsageMicros = entry.maxCpuUsageMicro;
            this.delayMillis = entry.delayMillis;
            this.maxDelayMillis = entry.maxDelayMillis;
            this.recordedDelayMessageCount = entry.recordedDelayMessageCount;
        }
    }

    public LooperStats(int samplingInterval, int entriesSizeCap) {
        this.mSamplingInterval = samplingInterval;
        this.mEntriesSizeCap = entriesSizeCap;
    }

    public void setDeviceState(Readonly deviceState) {
        TimeInStateStopwatch timeInStateStopwatch = this.mBatteryStopwatch;
        if (timeInStateStopwatch != null) {
            timeInStateStopwatch.close();
        }
        this.mDeviceState = deviceState;
        this.mBatteryStopwatch = deviceState.createTimeOnBatteryStopwatch();
    }

    public void setAddDebugEntries(boolean addDebugEntries) {
        this.mAddDebugEntries = addDebugEntries;
    }

    public Object messageDispatchStarting() {
        if (!deviceStateAllowsCollection() || !shouldCollectDetailedData()) {
            return DispatchSession.NOT_SAMPLED;
        }
        DispatchSession session = (DispatchSession) this.mSessionPool.poll();
        session = session == null ? new DispatchSession() : session;
        session.startTimeMicro = getElapsedRealtimeMicro();
        session.cpuStartMicro = getThreadTimeMicro();
        session.systemUptimeMillis = getSystemUptimeMillis();
        return session;
    }

    public void messageDispatched(Object token, Message msg) {
        if (deviceStateAllowsCollection()) {
            DispatchSession session = (DispatchSession) token;
            Entry entry = findEntry(msg, session != DispatchSession.NOT_SAMPLED);
            if (entry != null) {
                synchronized (entry) {
                    entry.messageCount++;
                    if (session != DispatchSession.NOT_SAMPLED) {
                        entry.recordedMessageCount++;
                        long latency = getElapsedRealtimeMicro() - session.startTimeMicro;
                        long cpuUsage = getThreadTimeMicro() - session.cpuStartMicro;
                        entry.totalLatencyMicro += latency;
                        entry.maxLatencyMicro = Math.max(entry.maxLatencyMicro, latency);
                        entry.cpuUsageMicro += cpuUsage;
                        entry.maxCpuUsageMicro = Math.max(entry.maxCpuUsageMicro, cpuUsage);
                        if (msg.getWhen() > 0) {
                            long delay = Math.max(0, session.systemUptimeMillis - msg.getWhen());
                            entry.delayMillis += delay;
                            entry.maxDelayMillis = Math.max(entry.maxDelayMillis, delay);
                            entry.recordedDelayMessageCount++;
                        }
                    }
                }
            }
            recycleSession(session);
        }
    }

    public void dispatchingThrewException(Object token, Message msg, Exception exception) {
        if (deviceStateAllowsCollection()) {
            DispatchSession session = (DispatchSession) token;
            Entry entry = findEntry(msg, session != DispatchSession.NOT_SAMPLED ? true : null);
            if (entry != null) {
                synchronized (entry) {
                    entry.exceptionCount++;
                }
            }
            recycleSession(session);
        }
    }

    private boolean deviceStateAllowsCollection() {
        Readonly readonly = this.mDeviceState;
        return (readonly == null || readonly.isCharging()) ? false : true;
    }

    public List<ExportedEntry> getEntries() {
        ArrayList<ExportedEntry> exportedEntries;
        synchronized (this.mLock) {
            int size = this.mEntries.size();
            exportedEntries = new ArrayList(size);
            for (int i = 0; i < size; i++) {
                Entry entry = (Entry) this.mEntries.valueAt(i);
                synchronized (entry) {
                    exportedEntries.add(new ExportedEntry(entry));
                }
            }
        }
        maybeAddSpecialEntry(exportedEntries, this.mOverflowEntry);
        maybeAddSpecialEntry(exportedEntries, this.mHashCollisionEntry);
        if (this.mAddDebugEntries && this.mBatteryStopwatch != null) {
            exportedEntries.add(createDebugEntry("start_time_millis", this.mStartElapsedTime));
            exportedEntries.add(createDebugEntry("end_time_millis", SystemClock.elapsedRealtime()));
            exportedEntries.add(createDebugEntry("battery_time_millis", this.mBatteryStopwatch.getMillis()));
            exportedEntries.add(createDebugEntry("sampling_interval", (long) this.mSamplingInterval));
        }
        return exportedEntries;
    }

    private ExportedEntry createDebugEntry(String variableName, long value) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(DEBUG_ENTRY_PREFIX);
        stringBuilder.append(variableName);
        Entry entry = new Entry(stringBuilder.toString());
        entry.messageCount = 1;
        entry.recordedMessageCount = 1;
        entry.totalLatencyMicro = value;
        return new ExportedEntry(entry);
    }

    public long getStartTimeMillis() {
        return this.mStartCurrentTime;
    }

    public long getStartElapsedTimeMillis() {
        return this.mStartElapsedTime;
    }

    public long getBatteryTimeMillis() {
        TimeInStateStopwatch timeInStateStopwatch = this.mBatteryStopwatch;
        return timeInStateStopwatch != null ? timeInStateStopwatch.getMillis() : 0;
    }

    private void maybeAddSpecialEntry(List<ExportedEntry> exportedEntries, Entry specialEntry) {
        synchronized (specialEntry) {
            if (specialEntry.messageCount > 0 || specialEntry.exceptionCount > 0) {
                exportedEntries.add(new ExportedEntry(specialEntry));
            }
        }
    }

    public void reset() {
        synchronized (this.mLock) {
            this.mEntries.clear();
        }
        synchronized (this.mHashCollisionEntry) {
            this.mHashCollisionEntry.reset();
        }
        synchronized (this.mOverflowEntry) {
            this.mOverflowEntry.reset();
        }
        this.mStartCurrentTime = System.currentTimeMillis();
        this.mStartElapsedTime = SystemClock.elapsedRealtime();
        TimeInStateStopwatch timeInStateStopwatch = this.mBatteryStopwatch;
        if (timeInStateStopwatch != null) {
            timeInStateStopwatch.reset();
        }
    }

    public void setSamplingInterval(int samplingInterval) {
        this.mSamplingInterval = samplingInterval;
    }

    public void setTrackScreenInteractive(boolean enabled) {
        this.mTrackScreenInteractive = enabled;
    }

    /* JADX WARNING: Missing block: B:21:0x0041, code skipped:
            if (r3.workSourceUid != r7.workSourceUid) goto L_0x0071;
     */
    /* JADX WARNING: Missing block: B:23:0x0051, code skipped:
            if (r3.handler.getClass() != r7.getTarget().getClass()) goto L_0x0071;
     */
    /* JADX WARNING: Missing block: B:25:0x0069, code skipped:
            if (r3.handler.getLooper().getThread() != r7.getTarget().getLooper().getThread()) goto L_0x0071;
     */
    /* JADX WARNING: Missing block: B:27:0x006d, code skipped:
            if (r3.isInteractive == r0) goto L_0x0070;
     */
    /* JADX WARNING: Missing block: B:28:0x0070, code skipped:
            return r3;
     */
    /* JADX WARNING: Missing block: B:30:0x0073, code skipped:
            return r6.mHashCollisionEntry;
     */
    private com.android.internal.os.LooperStats.Entry findEntry(android.os.Message r7, boolean r8) {
        /*
        r6 = this;
        r0 = r6.mTrackScreenInteractive;
        if (r0 == 0) goto L_0x000b;
    L_0x0004:
        r0 = r6.mDeviceState;
        r0 = r0.isScreenInteractive();
        goto L_0x000c;
    L_0x000b:
        r0 = 0;
        r1 = com.android.internal.os.LooperStats.Entry.idFor(r7, r0);
        r2 = r6.mLock;
        monitor-enter(r2);
        r3 = r6.mEntries;	 Catch:{ all -> 0x0074 }
        r3 = r3.get(r1);	 Catch:{ all -> 0x0074 }
        r3 = (com.android.internal.os.LooperStats.Entry) r3;	 Catch:{ all -> 0x0074 }
        if (r3 != 0) goto L_0x003c;
    L_0x001e:
        if (r8 != 0) goto L_0x0023;
    L_0x0020:
        r4 = 0;
        monitor-exit(r2);	 Catch:{ all -> 0x0074 }
        return r4;
    L_0x0023:
        r4 = r6.mEntries;	 Catch:{ all -> 0x0074 }
        r4 = r4.size();	 Catch:{ all -> 0x0074 }
        r5 = r6.mEntriesSizeCap;	 Catch:{ all -> 0x0074 }
        if (r4 < r5) goto L_0x0031;
    L_0x002d:
        r4 = r6.mOverflowEntry;	 Catch:{ all -> 0x0074 }
        monitor-exit(r2);	 Catch:{ all -> 0x0074 }
        return r4;
    L_0x0031:
        r4 = new com.android.internal.os.LooperStats$Entry;	 Catch:{ all -> 0x0074 }
        r4.<init>(r7, r0);	 Catch:{ all -> 0x0074 }
        r3 = r4;
        r4 = r6.mEntries;	 Catch:{ all -> 0x0074 }
        r4.put(r1, r3);	 Catch:{ all -> 0x0074 }
    L_0x003c:
        monitor-exit(r2);	 Catch:{ all -> 0x0074 }
        r2 = r3.workSourceUid;
        r4 = r7.workSourceUid;
        if (r2 != r4) goto L_0x0071;
    L_0x0043:
        r2 = r3.handler;
        r2 = r2.getClass();
        r4 = r7.getTarget();
        r4 = r4.getClass();
        if (r2 != r4) goto L_0x0071;
    L_0x0053:
        r2 = r3.handler;
        r2 = r2.getLooper();
        r2 = r2.getThread();
        r4 = r7.getTarget();
        r4 = r4.getLooper();
        r4 = r4.getThread();
        if (r2 != r4) goto L_0x0071;
    L_0x006b:
        r2 = r3.isInteractive;
        if (r2 == r0) goto L_0x0070;
    L_0x006f:
        goto L_0x0071;
    L_0x0070:
        return r3;
    L_0x0071:
        r2 = r6.mHashCollisionEntry;
        return r2;
    L_0x0074:
        r3 = move-exception;
        monitor-exit(r2);	 Catch:{ all -> 0x0074 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.LooperStats.findEntry(android.os.Message, boolean):com.android.internal.os.LooperStats$Entry");
    }

    private void recycleSession(DispatchSession session) {
        if (session != DispatchSession.NOT_SAMPLED && this.mSessionPool.size() < 50) {
            this.mSessionPool.add(session);
        }
    }

    /* Access modifiers changed, original: protected */
    public long getThreadTimeMicro() {
        return SystemClock.currentThreadTimeMicro();
    }

    /* Access modifiers changed, original: protected */
    public long getElapsedRealtimeMicro() {
        return SystemClock.elapsedRealtimeNanos() / 1000;
    }

    /* Access modifiers changed, original: protected */
    public long getSystemUptimeMillis() {
        return SystemClock.uptimeMillis();
    }

    /* Access modifiers changed, original: protected */
    public boolean shouldCollectDetailedData() {
        return ThreadLocalRandom.current().nextInt() % this.mSamplingInterval == 0;
    }
}
