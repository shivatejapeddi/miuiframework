package com.android.internal.os;

import android.os.Process;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

public class KernelCpuThreadReader {
    private static final String CPU_STATISTICS_FILENAME = "time_in_state";
    private static final boolean DEBUG = false;
    private static final Path DEFAULT_INITIAL_TIME_IN_STATE_PATH = DEFAULT_PROC_PATH.resolve("self/time_in_state");
    private static final String DEFAULT_PROCESS_NAME = "unknown_process";
    private static final Path DEFAULT_PROC_PATH = Paths.get("/proc", new String[0]);
    private static final String DEFAULT_THREAD_NAME = "unknown_thread";
    private static final int ID_ERROR = -1;
    private static final String PROCESS_DIRECTORY_FILTER = "[0-9]*";
    private static final String PROCESS_NAME_FILENAME = "cmdline";
    private static final String TAG = "KernelCpuThreadReader";
    private static final String THREAD_NAME_FILENAME = "comm";
    private int[] mFrequenciesKhz;
    private FrequencyBucketCreator mFrequencyBucketCreator;
    private final Injector mInjector;
    private final Path mProcPath;
    private final ProcTimeInStateReader mProcTimeInStateReader;
    private Predicate<Integer> mUidPredicate;

    @VisibleForTesting
    public static class FrequencyBucketCreator {
        private final int[] mBucketStartIndices;
        private final int mNumBuckets = this.mBucketStartIndices.length;
        private final int mNumFrequencies;

        @VisibleForTesting
        public FrequencyBucketCreator(long[] frequencies, int targetNumBuckets) {
            this.mNumFrequencies = frequencies.length;
            this.mBucketStartIndices = getBucketStartIndices(getClusterStartIndices(frequencies), targetNumBuckets, this.mNumFrequencies);
        }

        @VisibleForTesting
        public int[] bucketValues(long[] values) {
            Preconditions.checkArgument(values.length == this.mNumFrequencies);
            int[] buckets = new int[this.mNumBuckets];
            for (int bucketIdx = 0; bucketIdx < this.mNumBuckets; bucketIdx++) {
                int bucketStartIdx = getLowerBound(bucketIdx, this.mBucketStartIndices);
                int bucketEndIdx = getUpperBound(bucketIdx, this.mBucketStartIndices, values.length);
                for (int valuesIdx = bucketStartIdx; valuesIdx < bucketEndIdx; valuesIdx++) {
                    buckets[bucketIdx] = (int) (((long) buckets[bucketIdx]) + values[valuesIdx]);
                }
            }
            return buckets;
        }

        @VisibleForTesting
        public int[] bucketFrequencies(long[] frequencies) {
            Preconditions.checkArgument(frequencies.length == this.mNumFrequencies);
            int[] buckets = new int[this.mNumBuckets];
            for (int i = 0; i < buckets.length; i++) {
                buckets[i] = (int) frequencies[this.mBucketStartIndices[i]];
            }
            return buckets;
        }

        private static int[] getClusterStartIndices(long[] frequencies) {
            ArrayList<Integer> indices = new ArrayList();
            indices.add(Integer.valueOf(0));
            for (int i = 0; i < frequencies.length - 1; i++) {
                if (frequencies[i] >= frequencies[i + 1]) {
                    indices.add(Integer.valueOf(i + 1));
                }
            }
            return ArrayUtils.convertToIntArray(indices);
        }

        private static int[] getBucketStartIndices(int[] clusterStartIndices, int targetNumBuckets, int numFrequencies) {
            int numClusters = clusterStartIndices.length;
            if (numClusters > targetNumBuckets) {
                return Arrays.copyOfRange(clusterStartIndices, 0, targetNumBuckets);
            }
            ArrayList<Integer> bucketStartIndices = new ArrayList();
            for (int clusterIdx = 0; clusterIdx < numClusters; clusterIdx++) {
                int numBucketsInCluster;
                int clusterStartIdx = getLowerBound(clusterIdx, clusterStartIndices);
                int clusterEndIdx = getUpperBound(clusterIdx, clusterStartIndices, numFrequencies);
                if (clusterIdx != numClusters - 1) {
                    numBucketsInCluster = targetNumBuckets / numClusters;
                } else {
                    numBucketsInCluster = targetNumBuckets - ((numClusters - 1) * (targetNumBuckets / numClusters));
                }
                int numFrequenciesInBucket = Math.max(1, (clusterEndIdx - clusterStartIdx) / numBucketsInCluster);
                for (int bucketIdx = 0; bucketIdx < numBucketsInCluster; bucketIdx++) {
                    int bucketStartIdx = (bucketIdx * numFrequenciesInBucket) + clusterStartIdx;
                    if (bucketStartIdx >= clusterEndIdx) {
                        break;
                    }
                    bucketStartIndices.add(Integer.valueOf(bucketStartIdx));
                }
            }
            return ArrayUtils.convertToIntArray(bucketStartIndices);
        }

        private static int getLowerBound(int index, int[] startIndices) {
            return startIndices[index];
        }

        private static int getUpperBound(int index, int[] startIndices, int max) {
            if (index != startIndices.length - 1) {
                return startIndices[index + 1];
            }
            return max;
        }
    }

    @VisibleForTesting
    public static class Injector {
        public int getUidForPid(int pid) {
            return Process.getUidForPid(pid);
        }
    }

    public static class ProcessCpuUsage {
        public final int processId;
        public final String processName;
        public ArrayList<ThreadCpuUsage> threadCpuUsages;
        public final int uid;

        @VisibleForTesting
        public ProcessCpuUsage(int processId, String processName, int uid, ArrayList<ThreadCpuUsage> threadCpuUsages) {
            this.processId = processId;
            this.processName = processName;
            this.uid = uid;
            this.threadCpuUsages = threadCpuUsages;
        }
    }

    public static class ThreadCpuUsage {
        public final int threadId;
        public final String threadName;
        public int[] usageTimesMillis;

        @VisibleForTesting
        public ThreadCpuUsage(int threadId, String threadName, int[] usageTimesMillis) {
            this.threadId = threadId;
            this.threadName = threadName;
            this.usageTimesMillis = usageTimesMillis;
        }
    }

    @VisibleForTesting
    public KernelCpuThreadReader(int numBuckets, Predicate<Integer> uidPredicate, Path procPath, Path initialTimeInStatePath, Injector injector) throws IOException {
        this.mUidPredicate = uidPredicate;
        this.mProcPath = procPath;
        this.mProcTimeInStateReader = new ProcTimeInStateReader(initialTimeInStatePath);
        this.mInjector = injector;
        setNumBuckets(numBuckets);
    }

    public static KernelCpuThreadReader create(int numBuckets, Predicate<Integer> uidPredicate) {
        try {
            return new KernelCpuThreadReader(numBuckets, uidPredicate, DEFAULT_PROC_PATH, DEFAULT_INITIAL_TIME_IN_STATE_PATH, new Injector());
        } catch (IOException e) {
            Slog.e(TAG, "Failed to initialize KernelCpuThreadReader", e);
            return null;
        }
    }

    /* JADX WARNING: Missing block: B:29:0x005d, code skipped:
            if (r3 != null) goto L_0x005f;
     */
    /* JADX WARNING: Missing block: B:31:?, code skipped:
            $closeResource(r4, r3);
     */
    public java.util.ArrayList<com.android.internal.os.KernelCpuThreadReader.ProcessCpuUsage> getProcessCpuUsage() {
        /*
        r10 = this;
        r0 = "KernelCpuThreadReader";
        r1 = new java.util.ArrayList;
        r1.<init>();
        r2 = 0;
        r3 = r10.mProcPath;	 Catch:{ IOException -> 0x0063 }
        r4 = "[0-9]*";
        r3 = java.nio.file.Files.newDirectoryStream(r3, r4);	 Catch:{ IOException -> 0x0063 }
        r4 = r3.iterator();	 Catch:{ all -> 0x005a }
    L_0x0015:
        r5 = r4.hasNext();	 Catch:{ all -> 0x005a }
        if (r5 == 0) goto L_0x0049;
    L_0x001b:
        r5 = r4.next();	 Catch:{ all -> 0x005a }
        r5 = (java.nio.file.Path) r5;	 Catch:{ all -> 0x005a }
        r6 = r10.getProcessId(r5);	 Catch:{ all -> 0x005a }
        r7 = r10.mInjector;	 Catch:{ all -> 0x005a }
        r7 = r7.getUidForPid(r6);	 Catch:{ all -> 0x005a }
        r8 = -1;
        if (r7 == r8) goto L_0x0015;
    L_0x002e:
        if (r6 != r8) goto L_0x0031;
    L_0x0030:
        goto L_0x0015;
    L_0x0031:
        r8 = r10.mUidPredicate;	 Catch:{ all -> 0x005a }
        r9 = java.lang.Integer.valueOf(r7);	 Catch:{ all -> 0x005a }
        r8 = r8.test(r9);	 Catch:{ all -> 0x005a }
        if (r8 != 0) goto L_0x003e;
    L_0x003d:
        goto L_0x0015;
        r8 = r10.getProcessCpuUsage(r5, r6, r7);	 Catch:{ all -> 0x005a }
        if (r8 == 0) goto L_0x0048;
    L_0x0045:
        r1.add(r8);	 Catch:{ all -> 0x005a }
    L_0x0048:
        goto L_0x0015;
    L_0x0049:
        $closeResource(r2, r3);	 Catch:{ IOException -> 0x0063 }
        r3 = r1.isEmpty();
        if (r3 == 0) goto L_0x0059;
    L_0x0053:
        r3 = "Didn't successfully get any process CPU information for UIDs specified";
        android.util.Slog.w(r0, r3);
        return r2;
    L_0x0059:
        return r1;
    L_0x005a:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x005c }
    L_0x005c:
        r5 = move-exception;
        if (r3 == 0) goto L_0x0062;
    L_0x005f:
        $closeResource(r4, r3);	 Catch:{ IOException -> 0x0063 }
    L_0x0062:
        throw r5;	 Catch:{ IOException -> 0x0063 }
    L_0x0063:
        r3 = move-exception;
        r4 = "Failed to iterate over process paths";
        android.util.Slog.w(r0, r4, r3);
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuThreadReader.getProcessCpuUsage():java.util.ArrayList");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    public int[] getCpuFrequenciesKhz() {
        return this.mFrequenciesKhz;
    }

    /* Access modifiers changed, original: 0000 */
    public void setNumBuckets(int numBuckets) {
        if (numBuckets < 1) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Number of buckets must be at least 1, but was ");
            stringBuilder.append(numBuckets);
            Slog.w(TAG, stringBuilder.toString());
            return;
        }
        int[] iArr = this.mFrequenciesKhz;
        if (iArr == null || iArr.length != numBuckets) {
            this.mFrequencyBucketCreator = new FrequencyBucketCreator(this.mProcTimeInStateReader.getFrequenciesKhz(), numBuckets);
            this.mFrequenciesKhz = this.mFrequencyBucketCreator.bucketFrequencies(this.mProcTimeInStateReader.getFrequenciesKhz());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setUidPredicate(Predicate<Integer> uidPredicate) {
        this.mUidPredicate = uidPredicate;
    }

    /* JADX WARNING: Missing block: B:23:0x0045, code skipped:
            if (r3 != null) goto L_0x0047;
     */
    /* JADX WARNING: Missing block: B:25:?, code skipped:
            $closeResource(r4, r3);
     */
    private com.android.internal.os.KernelCpuThreadReader.ProcessCpuUsage getProcessCpuUsage(java.nio.file.Path r8, int r9, int r10) {
        /*
        r7 = this;
        r0 = "task";
        r0 = r8.resolve(r0);
        r1 = new java.util.ArrayList;
        r1.<init>();
        r2 = 0;
        r3 = java.nio.file.Files.newDirectoryStream(r0);	 Catch:{ IOException -> 0x004b }
        r4 = r3.iterator();	 Catch:{ all -> 0x0042 }
    L_0x0015:
        r5 = r4.hasNext();	 Catch:{ all -> 0x0042 }
        if (r5 == 0) goto L_0x002d;
    L_0x001b:
        r5 = r4.next();	 Catch:{ all -> 0x0042 }
        r5 = (java.nio.file.Path) r5;	 Catch:{ all -> 0x0042 }
        r6 = r7.getThreadCpuUsage(r5);	 Catch:{ all -> 0x0042 }
        if (r6 != 0) goto L_0x0028;
    L_0x0027:
        goto L_0x0015;
    L_0x0028:
        r1.add(r6);	 Catch:{ all -> 0x0042 }
        goto L_0x0015;
    L_0x002d:
        $closeResource(r2, r3);	 Catch:{ IOException -> 0x004b }
        r3 = r1.isEmpty();
        if (r3 == 0) goto L_0x0038;
    L_0x0037:
        return r2;
    L_0x0038:
        r2 = new com.android.internal.os.KernelCpuThreadReader$ProcessCpuUsage;
        r3 = r7.getProcessName(r8);
        r2.<init>(r9, r3, r10, r1);
        return r2;
    L_0x0042:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x0044 }
    L_0x0044:
        r5 = move-exception;
        if (r3 == 0) goto L_0x004a;
    L_0x0047:
        $closeResource(r4, r3);	 Catch:{ IOException -> 0x004b }
    L_0x004a:
        throw r5;	 Catch:{ IOException -> 0x004b }
    L_0x004b:
        r3 = move-exception;
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuThreadReader.getProcessCpuUsage(java.nio.file.Path, int, int):com.android.internal.os.KernelCpuThreadReader$ProcessCpuUsage");
    }

    private ThreadCpuUsage getThreadCpuUsage(Path threadDirectory) {
        try {
            int threadId = Integer.parseInt(threadDirectory.getFileName().toString());
            String threadName = getThreadName(threadDirectory);
            long[] cpuUsagesLong = this.mProcTimeInStateReader.getUsageTimesMillis(threadDirectory.resolve(CPU_STATISTICS_FILENAME));
            if (cpuUsagesLong == null) {
                return null;
            }
            return new ThreadCpuUsage(threadId, threadName, this.mFrequencyBucketCreator.bucketValues(cpuUsagesLong));
        } catch (NumberFormatException e) {
            Slog.w(TAG, "Failed to parse thread ID when iterating over /proc/*/task", e);
            return null;
        }
    }

    private String getProcessName(Path processPath) {
        String processName = ProcStatsUtil.readSingleLineProcFile(processPath.resolve(PROCESS_NAME_FILENAME).toString());
        if (processName != null) {
            return processName;
        }
        return DEFAULT_PROCESS_NAME;
    }

    private String getThreadName(Path threadPath) {
        String threadName = ProcStatsUtil.readNullSeparatedFile(threadPath.resolve(THREAD_NAME_FILENAME).toString());
        if (threadName == null) {
            return DEFAULT_THREAD_NAME;
        }
        return threadName;
    }

    private int getProcessId(Path processPath) {
        String fileName = processPath.getFileName().toString();
        try {
            return Integer.parseInt(fileName);
        } catch (NumberFormatException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to parse ");
            stringBuilder.append(fileName);
            stringBuilder.append(" as process ID");
            Slog.w(TAG, stringBuilder.toString(), e);
            return -1;
        }
    }
}
