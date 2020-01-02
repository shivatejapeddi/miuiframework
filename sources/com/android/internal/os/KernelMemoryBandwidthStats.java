package com.android.internal.os;

import android.text.TextUtils.SimpleStringSplitter;
import android.util.LongSparseLongArray;
import android.util.TimeUtils;
import com.android.internal.annotations.VisibleForTesting;
import java.io.BufferedReader;
import java.io.IOException;

public class KernelMemoryBandwidthStats {
    private static final boolean DEBUG = false;
    private static final String TAG = "KernelMemoryBandwidthStats";
    private static final String mSysfsFile = "/sys/kernel/memory_state_time/show_stat";
    protected final LongSparseLongArray mBandwidthEntries = new LongSparseLongArray();
    private boolean mStatsDoNotExist = false;

    /* JADX WARNING: Missing block: B:15:?, code skipped:
            r4.close();
     */
    public void updateStats() {
        /*
        r8 = this;
        r0 = "KernelMemoryBandwidthStats";
        r1 = r8.mStatsDoNotExist;
        if (r1 == 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r1 = android.os.SystemClock.uptimeMillis();
        r3 = android.os.StrictMode.allowThreadDiskReads();
        r4 = new java.io.BufferedReader;	 Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x0030 }
        r5 = new java.io.FileReader;	 Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x0030 }
        r6 = "/sys/kernel/memory_state_time/show_stat";
        r5.<init>(r6);	 Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x0030 }
        r4.<init>(r5);	 Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x0030 }
        r8.parseStats(r4);	 Catch:{ all -> 0x0022 }
        r4.close();	 Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x0030 }
        goto L_0x005d;
    L_0x0022:
        r5 = move-exception;
        throw r5;	 Catch:{ all -> 0x0024 }
    L_0x0024:
        r6 = move-exception;
        r4.close();	 Catch:{ all -> 0x0029 }
        goto L_0x002d;
    L_0x0029:
        r7 = move-exception;
        r5.addSuppressed(r7);	 Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x0030 }
    L_0x002d:
        throw r6;	 Catch:{ FileNotFoundException -> 0x004f, IOException -> 0x0030 }
    L_0x002e:
        r0 = move-exception;
        goto L_0x0087;
    L_0x0030:
        r4 = move-exception;
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x002e }
        r5.<init>();	 Catch:{ all -> 0x002e }
        r6 = "Failed to read memory bandwidth: ";
        r5.append(r6);	 Catch:{ all -> 0x002e }
        r6 = r4.getMessage();	 Catch:{ all -> 0x002e }
        r5.append(r6);	 Catch:{ all -> 0x002e }
        r5 = r5.toString();	 Catch:{ all -> 0x002e }
        android.util.Slog.e(r0, r5);	 Catch:{ all -> 0x002e }
        r5 = r8.mBandwidthEntries;	 Catch:{ all -> 0x002e }
        r5.clear();	 Catch:{ all -> 0x002e }
        goto L_0x005d;
    L_0x004f:
        r4 = move-exception;
        r5 = "No kernel memory bandwidth stats available";
        android.util.Slog.w(r0, r5);	 Catch:{ all -> 0x002e }
        r5 = r8.mBandwidthEntries;	 Catch:{ all -> 0x002e }
        r5.clear();	 Catch:{ all -> 0x002e }
        r5 = 1;
        r8.mStatsDoNotExist = r5;	 Catch:{ all -> 0x002e }
    L_0x005d:
        android.os.StrictMode.setThreadPolicy(r3);
        r4 = android.os.SystemClock.uptimeMillis();
        r4 = r4 - r1;
        r6 = 100;
        r6 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r6 <= 0) goto L_0x0086;
    L_0x006c:
        r6 = new java.lang.StringBuilder;
        r6.<init>();
        r7 = "Reading memory bandwidth file took ";
        r6.append(r7);
        r6.append(r4);
        r7 = "ms";
        r6.append(r7);
        r6 = r6.toString();
        android.util.Slog.w(r0, r6);
    L_0x0086:
        return;
    L_0x0087:
        android.os.StrictMode.setThreadPolicy(r3);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelMemoryBandwidthStats.updateStats():void");
    }

    @VisibleForTesting
    public void parseStats(BufferedReader reader) throws IOException {
        SimpleStringSplitter splitter = new SimpleStringSplitter(' ');
        this.mBandwidthEntries.clear();
        while (true) {
            String readLine = reader.readLine();
            String line = readLine;
            if (readLine != null) {
                splitter.setString(line);
                splitter.next();
                int bandwidth = 0;
                do {
                    int indexOfKey = this.mBandwidthEntries.indexOfKey((long) bandwidth);
                    int index = indexOfKey;
                    if (indexOfKey >= 0) {
                        LongSparseLongArray longSparseLongArray = this.mBandwidthEntries;
                        longSparseLongArray.put((long) bandwidth, longSparseLongArray.valueAt(index) + (Long.parseLong(splitter.next()) / TimeUtils.NANOS_PER_MS));
                    } else {
                        this.mBandwidthEntries.put((long) bandwidth, Long.parseLong(splitter.next()) / TimeUtils.NANOS_PER_MS);
                    }
                    bandwidth++;
                } while (splitter.hasNext());
            } else {
                return;
            }
        }
    }

    public LongSparseLongArray getBandwidthEntries() {
        return this.mBandwidthEntries;
    }
}
