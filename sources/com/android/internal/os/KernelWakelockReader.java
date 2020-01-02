package com.android.internal.os;

import android.os.Process;
import android.os.StrictMode;
import android.os.SystemClock;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.KernelWakelockStats.Entry;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;

public class KernelWakelockReader {
    private static final int[] PROC_WAKELOCKS_FORMAT = new int[]{5129, 8201, 9, 9, 9, 8201};
    private static final String TAG = "KernelWakelockReader";
    private static final int[] WAKEUP_SOURCES_FORMAT = new int[]{4105, 8457, 265, 265, 265, 265, 8457};
    private static int sKernelWakelockUpdateVersion = 0;
    private static final String sWakelockFile = "/proc/wakelocks";
    private static final String sWakeupSourceFile = "/d/wakeup_sources";
    private final long[] mProcWakelocksData = new long[3];
    private final String[] mProcWakelocksName = new String[3];

    public final KernelWakelockStats readKernelWakelockStats(KernelWakelockStats staleStats) {
        IOException e;
        boolean wakeup_sources;
        StringBuilder stringBuilder;
        String str = TAG;
        byte[] buffer = new byte[32768];
        int len = 0;
        long startTime = SystemClock.uptimeMillis();
        int oldMask = StrictMode.allowThreadDiskReadsMask();
        try {
            e = new FileInputStream(sWakelockFile);
            wakeup_sources = null;
        } catch (FileNotFoundException e2) {
            try {
                e = new FileInputStream(sWakeupSourceFile);
                wakeup_sources = true;
            } catch (FileNotFoundException e3) {
                wakeup_sources = e3;
                Slog.wtf(str, "neither /proc/wakelocks nor /d/wakeup_sources exists");
                StrictMode.setThreadPolicyMask(oldMask);
                return null;
            }
        }
        while (true) {
            try {
                int read = e.read(buffer, len, buffer.length - len);
                int cnt = read;
                if (read <= 0) {
                    break;
                }
                len += cnt;
            } catch (IOException e4) {
                wakeup_sources = "failed to read kernel wakelocks";
                Slog.wtf(str, wakeup_sources, e4);
                return null;
            } finally {
                StrictMode.setThreadPolicyMask(oldMask);
            }
        }
        e4.close();
        long readTime = SystemClock.uptimeMillis() - startTime;
        if (readTime > 100) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Reading wakelock stats took ");
            stringBuilder.append(readTime);
            stringBuilder.append("ms");
            Slog.w(str, stringBuilder.toString());
        }
        if (len > 0) {
            if (len >= buffer.length) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Kernel wake locks exceeded buffer size ");
                stringBuilder.append(buffer.length);
                Slog.wtf(str, stringBuilder.toString());
            }
            for (int i = 0; i < len; i++) {
                if (buffer[i] == (byte) 0) {
                    len = i;
                    break;
                }
            }
        }
        return parseProcWakelocks(buffer, len, wakeup_sources, staleStats);
    }

    @VisibleForTesting
    public KernelWakelockStats parseProcWakelocks(byte[] wlBuffer, int len, boolean wakeup_sources, KernelWakelockStats staleStats) {
        byte b;
        int i;
        int endIndex;
        int startIndex;
        Throwable th;
        byte[] bArr = wlBuffer;
        int i2 = len;
        KernelWakelockStats kernelWakelockStats = staleStats;
        int i3 = 0;
        while (true) {
            b = (byte) 10;
            if (i3 >= i2 || bArr[i3] == (byte) 10 || bArr[i3] == (byte) 0) {
                i = i3 + 1;
                endIndex = i;
                startIndex = i;
            } else {
                i3++;
            }
        }
        i = i3 + 1;
        endIndex = i;
        startIndex = i;
        synchronized (this) {
            try {
                sKernelWakelockUpdateVersion++;
                int startIndex2 = startIndex;
                while (endIndex < i2) {
                    int endIndex2 = startIndex2;
                    while (endIndex2 < i2) {
                        try {
                            if (bArr[endIndex2] == b || bArr[endIndex2] == (byte) 0) {
                                break;
                            }
                            endIndex2++;
                        } catch (Throwable th2) {
                            th = th2;
                            endIndex = endIndex2;
                            startIndex = startIndex2;
                            throw th;
                        }
                    }
                    if (endIndex2 > i2 - 1) {
                        endIndex = endIndex2;
                        break;
                    }
                    try {
                        int[] iArr;
                        String[] nameStringArray = this.mProcWakelocksName;
                        long[] wlData = this.mProcWakelocksData;
                        for (i = startIndex2; i < endIndex2; i++) {
                            if ((bArr[i] & 128) != 0) {
                                bArr[i] = (byte) 63;
                            }
                        }
                        if (wakeup_sources) {
                            iArr = WAKEUP_SOURCES_FORMAT;
                        } else {
                            iArr = PROC_WAKELOCKS_FORMAT;
                        }
                        int endIndex3 = endIndex2;
                        try {
                            long totalTime;
                            boolean parsed = Process.parseProcLine(wlBuffer, startIndex2, endIndex2, iArr, nameStringArray, wlData, null);
                            String name = nameStringArray[0].trim();
                            int count = (int) wlData[1];
                            if (wakeup_sources) {
                                totalTime = wlData[2] * 1000;
                            } else {
                                totalTime = (wlData[2] + 500) / 1000;
                            }
                            if (!parsed || name.length() <= 0) {
                                if (!parsed) {
                                    String str = TAG;
                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("Failed to parse proc line: ");
                                    stringBuilder.append(new String(bArr, startIndex2, endIndex3 - startIndex2));
                                    Slog.wtf(str, stringBuilder.toString());
                                }
                            } else if (kernelWakelockStats.containsKey(name)) {
                                Entry kwlStats = (Entry) kernelWakelockStats.get(name);
                                if (kwlStats.mVersion == sKernelWakelockUpdateVersion) {
                                    kwlStats.mCount += count;
                                    kwlStats.mTotalTime += totalTime;
                                } else {
                                    kwlStats.mCount = count;
                                    kwlStats.mTotalTime = totalTime;
                                    kwlStats.mVersion = sKernelWakelockUpdateVersion;
                                }
                            } else {
                                kernelWakelockStats.put(name, new Entry(count, totalTime, sKernelWakelockUpdateVersion));
                            }
                        } catch (Exception e) {
                            Slog.wtf(TAG, "Failed to parse proc line!");
                        } catch (Throwable th3) {
                            th = th3;
                            startIndex = startIndex2;
                            endIndex = endIndex3;
                        }
                        startIndex2 = endIndex3 + 1;
                        endIndex = endIndex3;
                        b = (byte) 10;
                    } catch (Throwable th4) {
                        th = th4;
                        startIndex = startIndex2;
                        throw th;
                    }
                }
                try {
                    Iterator<Entry> itr = staleStats.values().iterator();
                    while (itr.hasNext()) {
                        if (((Entry) itr.next()).mVersion != sKernelWakelockUpdateVersion) {
                            itr.remove();
                        }
                    }
                    kernelWakelockStats.kernelWakelockVersion = sKernelWakelockUpdateVersion;
                    return kernelWakelockStats;
                } catch (Throwable th5) {
                    th = th5;
                    throw th;
                }
            } catch (Throwable th6) {
                th = th6;
                throw th;
            }
        }
    }
}
