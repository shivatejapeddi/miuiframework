package com.android.internal.os;

import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.KernelCpuThreadReader.ProcessCpuUsage;
import com.android.internal.os.KernelCpuThreadReader.ThreadCpuUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class KernelCpuThreadReaderDiff {
    private static final int OTHER_THREADS_ID = -1;
    private static final String OTHER_THREADS_NAME = "__OTHER_THREADS";
    private static final String TAG = "KernelCpuThreadReaderDiff";
    private int mMinimumTotalCpuUsageMillis;
    private Map<ThreadKey, int[]> mPreviousCpuUsage = null;
    private final KernelCpuThreadReader mReader;

    private static class ThreadKey {
        private final int mProcessId;
        private final int mProcessNameHash;
        private final int mThreadId;
        private final int mThreadNameHash;

        ThreadKey(int processId, int threadId, String processName, String threadName) {
            this.mProcessId = processId;
            this.mThreadId = threadId;
            this.mProcessNameHash = Objects.hash(new Object[]{processName});
            this.mThreadNameHash = Objects.hash(new Object[]{threadName});
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Integer.valueOf(this.mProcessId), Integer.valueOf(this.mThreadId), Integer.valueOf(this.mProcessNameHash), Integer.valueOf(this.mThreadNameHash)});
        }

        public boolean equals(Object obj) {
            boolean z = false;
            if (!(obj instanceof ThreadKey)) {
                return false;
            }
            ThreadKey other = (ThreadKey) obj;
            if (this.mProcessId == other.mProcessId && this.mThreadId == other.mThreadId && this.mProcessNameHash == other.mProcessNameHash && this.mThreadNameHash == other.mThreadNameHash) {
                z = true;
            }
            return z;
        }
    }

    @VisibleForTesting
    public KernelCpuThreadReaderDiff(KernelCpuThreadReader reader, int minimumTotalCpuUsageMillis) {
        this.mReader = reader;
        this.mMinimumTotalCpuUsageMillis = minimumTotalCpuUsageMillis;
    }

    public ArrayList<ProcessCpuUsage> getProcessCpuUsageDiffed() {
        Map<ThreadKey, int[]> newCpuUsage = null;
        try {
            ArrayList<ProcessCpuUsage> processCpuUsages = this.mReader.getProcessCpuUsage();
            newCpuUsage = createCpuUsageMap(processCpuUsages);
            ArrayList<ProcessCpuUsage> i;
            if (this.mPreviousCpuUsage == null) {
                i = null;
                return i;
            }
            i = null;
            while (i < processCpuUsages.size()) {
                ProcessCpuUsage processCpuUsage = (ProcessCpuUsage) processCpuUsages.get(i);
                changeToDiffs(this.mPreviousCpuUsage, processCpuUsage);
                applyThresholding(processCpuUsage);
                i++;
            }
            this.mPreviousCpuUsage = newCpuUsage;
            return processCpuUsages;
        } finally {
            this.mPreviousCpuUsage = newCpuUsage;
        }
    }

    public int[] getCpuFrequenciesKhz() {
        return this.mReader.getCpuFrequenciesKhz();
    }

    /* Access modifiers changed, original: 0000 */
    public void setMinimumTotalCpuUsageMillis(int minimumTotalCpuUsageMillis) {
        if (minimumTotalCpuUsageMillis < 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Negative minimumTotalCpuUsageMillis: ");
            stringBuilder.append(minimumTotalCpuUsageMillis);
            Slog.w(TAG, stringBuilder.toString());
            return;
        }
        this.mMinimumTotalCpuUsageMillis = minimumTotalCpuUsageMillis;
    }

    private static Map<ThreadKey, int[]> createCpuUsageMap(List<ProcessCpuUsage> processCpuUsages) {
        Map<ThreadKey, int[]> cpuUsageMap = new ArrayMap();
        for (int i = 0; i < processCpuUsages.size(); i++) {
            ProcessCpuUsage processCpuUsage = (ProcessCpuUsage) processCpuUsages.get(i);
            for (int j = 0; j < processCpuUsage.threadCpuUsages.size(); j++) {
                ThreadCpuUsage threadCpuUsage = (ThreadCpuUsage) processCpuUsage.threadCpuUsages.get(j);
                cpuUsageMap.put(new ThreadKey(processCpuUsage.processId, threadCpuUsage.threadId, processCpuUsage.processName, threadCpuUsage.threadName), threadCpuUsage.usageTimesMillis);
            }
        }
        return cpuUsageMap;
    }

    private static void changeToDiffs(Map<ThreadKey, int[]> previousCpuUsage, ProcessCpuUsage processCpuUsage) {
        for (int i = 0; i < processCpuUsage.threadCpuUsages.size(); i++) {
            ThreadCpuUsage threadCpuUsage = (ThreadCpuUsage) processCpuUsage.threadCpuUsages.get(i);
            int[] previous = (int[]) previousCpuUsage.get(new ThreadKey(processCpuUsage.processId, threadCpuUsage.threadId, processCpuUsage.processName, threadCpuUsage.threadName));
            if (previous == null) {
                previous = new int[threadCpuUsage.usageTimesMillis.length];
            }
            threadCpuUsage.usageTimesMillis = cpuTimeDiff(threadCpuUsage.usageTimesMillis, previous);
        }
    }

    private void applyThresholding(ProcessCpuUsage processCpuUsage) {
        int[] filteredThreadsCpuUsage = null;
        ArrayList<ThreadCpuUsage> thresholded = new ArrayList();
        for (int i = 0; i < processCpuUsage.threadCpuUsages.size(); i++) {
            ThreadCpuUsage threadCpuUsage = (ThreadCpuUsage) processCpuUsage.threadCpuUsages.get(i);
            if (this.mMinimumTotalCpuUsageMillis > totalCpuUsage(threadCpuUsage.usageTimesMillis)) {
                if (filteredThreadsCpuUsage == null) {
                    filteredThreadsCpuUsage = new int[threadCpuUsage.usageTimesMillis.length];
                }
                addToCpuUsage(filteredThreadsCpuUsage, threadCpuUsage.usageTimesMillis);
            } else {
                thresholded.add(threadCpuUsage);
            }
        }
        if (filteredThreadsCpuUsage != null) {
            thresholded.add(new ThreadCpuUsage(-1, OTHER_THREADS_NAME, filteredThreadsCpuUsage));
        }
        processCpuUsage.threadCpuUsages = thresholded;
    }

    private static int totalCpuUsage(int[] cpuUsage) {
        int total = 0;
        for (int i : cpuUsage) {
            total += i;
        }
        return total;
    }

    private static void addToCpuUsage(int[] a, int[] b) {
        for (int i = 0; i < a.length; i++) {
            a[i] = a[i] + b[i];
        }
    }

    private static int[] cpuTimeDiff(int[] a, int[] b) {
        int[] difference = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            difference[i] = a[i] - b[i];
        }
        return difference;
    }
}
