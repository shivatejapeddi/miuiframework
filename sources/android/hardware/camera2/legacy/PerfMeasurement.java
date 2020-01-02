package android.hardware.camera2.legacy;

import android.os.SystemClock;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

class PerfMeasurement {
    public static final int DEFAULT_MAX_QUERIES = 3;
    private static final long FAILED_TIMING = -2;
    private static final long NO_DURATION_YET = -1;
    private static final String TAG = "PerfMeasurement";
    private ArrayList<Long> mCollectedCpuDurations;
    private ArrayList<Long> mCollectedGpuDurations;
    private ArrayList<Long> mCollectedTimestamps;
    private int mCompletedQueryCount;
    private Queue<Long> mCpuDurationsQueue;
    private final long mNativeContext;
    private long mStartTimeNs;
    private Queue<Long> mTimestampQueue;

    private static native long nativeCreateContext(int i);

    private static native void nativeDeleteContext(long j);

    protected static native long nativeGetNextGlDuration(long j);

    private static native boolean nativeQuerySupport();

    protected static native void nativeStartGlTimer(long j);

    protected static native void nativeStopGlTimer(long j);

    public PerfMeasurement() {
        this.mCompletedQueryCount = 0;
        this.mCollectedGpuDurations = new ArrayList();
        this.mCollectedCpuDurations = new ArrayList();
        this.mCollectedTimestamps = new ArrayList();
        this.mTimestampQueue = new LinkedList();
        this.mCpuDurationsQueue = new LinkedList();
        this.mNativeContext = nativeCreateContext(3);
    }

    public PerfMeasurement(int maxQueries) {
        this.mCompletedQueryCount = 0;
        this.mCollectedGpuDurations = new ArrayList();
        this.mCollectedCpuDurations = new ArrayList();
        this.mCollectedTimestamps = new ArrayList();
        this.mTimestampQueue = new LinkedList();
        this.mCpuDurationsQueue = new LinkedList();
        if (maxQueries >= 1) {
            this.mNativeContext = nativeCreateContext(maxQueries);
            return;
        }
        throw new IllegalArgumentException("maxQueries is less than 1");
    }

    public static boolean isGlTimingSupported() {
        return nativeQuerySupport();
    }

    /* JADX WARNING: Missing block: B:15:?, code skipped:
            r0.close();
     */
    public void dumpPerformanceData(java.lang.String r8) {
        /*
        r7 = this;
        r0 = new java.io.BufferedWriter;	 Catch:{ IOException -> 0x0062 }
        r1 = new java.io.FileWriter;	 Catch:{ IOException -> 0x0062 }
        r1.<init>(r8);	 Catch:{ IOException -> 0x0062 }
        r0.<init>(r1);	 Catch:{ IOException -> 0x0062 }
        r1 = "timestamp gpu_duration cpu_duration\n";
        r0.write(r1);	 Catch:{ all -> 0x0056 }
        r1 = 0;
        r2 = r1;
    L_0x0012:
        r3 = r7.mCollectedGpuDurations;	 Catch:{ all -> 0x0056 }
        r3 = r3.size();	 Catch:{ all -> 0x0056 }
        if (r2 >= r3) goto L_0x0043;
    L_0x001a:
        r3 = "%d %d %d\n";
        r4 = 3;
        r4 = new java.lang.Object[r4];	 Catch:{ all -> 0x0056 }
        r5 = r7.mCollectedTimestamps;	 Catch:{ all -> 0x0056 }
        r5 = r5.get(r2);	 Catch:{ all -> 0x0056 }
        r4[r1] = r5;	 Catch:{ all -> 0x0056 }
        r5 = r7.mCollectedGpuDurations;	 Catch:{ all -> 0x0056 }
        r5 = r5.get(r2);	 Catch:{ all -> 0x0056 }
        r6 = 1;
        r4[r6] = r5;	 Catch:{ all -> 0x0056 }
        r5 = 2;
        r6 = r7.mCollectedCpuDurations;	 Catch:{ all -> 0x0056 }
        r6 = r6.get(r2);	 Catch:{ all -> 0x0056 }
        r4[r5] = r6;	 Catch:{ all -> 0x0056 }
        r3 = java.lang.String.format(r3, r4);	 Catch:{ all -> 0x0056 }
        r0.write(r3);	 Catch:{ all -> 0x0056 }
        r2 = r2 + 1;
        goto L_0x0012;
    L_0x0043:
        r1 = r7.mCollectedTimestamps;	 Catch:{ all -> 0x0056 }
        r1.clear();	 Catch:{ all -> 0x0056 }
        r1 = r7.mCollectedGpuDurations;	 Catch:{ all -> 0x0056 }
        r1.clear();	 Catch:{ all -> 0x0056 }
        r1 = r7.mCollectedCpuDurations;	 Catch:{ all -> 0x0056 }
        r1.clear();	 Catch:{ all -> 0x0056 }
        r0.close();	 Catch:{ IOException -> 0x0062 }
        goto L_0x0081;
    L_0x0056:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0058 }
    L_0x0058:
        r2 = move-exception;
        r0.close();	 Catch:{ all -> 0x005d }
        goto L_0x0061;
    L_0x005d:
        r3 = move-exception;
        r1.addSuppressed(r3);	 Catch:{ IOException -> 0x0062 }
    L_0x0061:
        throw r2;	 Catch:{ IOException -> 0x0062 }
    L_0x0062:
        r0 = move-exception;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Error writing data dump to ";
        r1.append(r2);
        r1.append(r8);
        r2 = ":";
        r1.append(r2);
        r1.append(r0);
        r1 = r1.toString();
        r2 = "PerfMeasurement";
        android.util.Log.e(r2, r1);
    L_0x0081:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.hardware.camera2.legacy.PerfMeasurement.dumpPerformanceData(java.lang.String):void");
    }

    public void startTimer() {
        nativeStartGlTimer(this.mNativeContext);
        this.mStartTimeNs = SystemClock.elapsedRealtimeNanos();
    }

    public void stopTimer() {
        this.mCpuDurationsQueue.add(Long.valueOf(SystemClock.elapsedRealtimeNanos() - this.mStartTimeNs));
        nativeStopGlTimer(this.mNativeContext);
        long duration = getNextGlDuration();
        if (duration > 0) {
            this.mCollectedGpuDurations.add(Long.valueOf(duration));
            long j = -1;
            this.mCollectedTimestamps.add(Long.valueOf(this.mTimestampQueue.isEmpty() ? -1 : ((Long) this.mTimestampQueue.poll()).longValue()));
            ArrayList arrayList = this.mCollectedCpuDurations;
            if (!this.mCpuDurationsQueue.isEmpty()) {
                j = ((Long) this.mCpuDurationsQueue.poll()).longValue();
            }
            arrayList.add(Long.valueOf(j));
        }
        if (duration == -2) {
            if (!this.mTimestampQueue.isEmpty()) {
                this.mTimestampQueue.poll();
            }
            if (!this.mCpuDurationsQueue.isEmpty()) {
                this.mCpuDurationsQueue.poll();
            }
        }
    }

    public void addTimestamp(long timestamp) {
        this.mTimestampQueue.add(Long.valueOf(timestamp));
    }

    private long getNextGlDuration() {
        long duration = nativeGetNextGlDuration(this.mNativeContext);
        if (duration > 0) {
            this.mCompletedQueryCount++;
        }
        return duration;
    }

    public int getCompletedQueryCount() {
        return this.mCompletedQueryCount;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() {
        nativeDeleteContext(this.mNativeContext);
    }
}
