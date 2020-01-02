package com.android.internal.net;

import android.net.NetworkStatsSysApp;
import android.net.NetworkStatsSysApp.Entry;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.SystemClock;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.ProcFileReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ProtocolException;
import libcore.io.IoUtils;

public class NetworkStatsFactorySysApp {
    private static final String CLATD_INTERFACE_PREFIX = "v4-";
    private static final int IPV4V6_HEADER_DELTA = 20;
    private final File mStatsXtSysApp = new File("/proc/", "net/xt_qtaguid/system_app_stats");
    private boolean mUseBpfStats = new File("/sys/fs/bpf/traffic_uid_stats_map").exists();

    public static native int nativeReadNSDForSysApp(NetworkStatsSysApp networkStatsSysApp, String[] strArr);

    public NetworkStatsSysApp readNSDForSystemApp(String[] limitIfaces, NetworkStatsSysApp lastStats) throws IOException {
        NetworkStatsSysApp stats;
        if (lastStats != null) {
            stats = lastStats;
            stats.setElapsedRealtime(SystemClock.elapsedRealtime());
        } else {
            stats = new NetworkStatsSysApp(SystemClock.elapsedRealtime(), -1);
        }
        if (this.mUseBpfStats) {
            nativeReadNSDForSysApp(stats, limitIfaces);
        } else {
            readNetworkStatsDetail(this.mStatsXtSysApp, stats, limitIfaces);
        }
        Entry entry = null;
        for (int i = 0; i < stats.size(); i++) {
            entry = stats.getValues(i, entry);
            if (entry.iface != null && entry.iface.startsWith(CLATD_INTERFACE_PREFIX)) {
                entry.rxBytes = entry.rxPackets * 20;
                entry.txBytes = entry.txPackets * 20;
                entry.rxPackets = 0;
                entry.txPackets = 0;
                stats.combineValues(entry);
            }
        }
        return stats;
    }

    public static void readNetworkStatsDetail(File detailPath, NetworkStatsSysApp stats, String[] limitIfaces) throws IOException {
        ThreadPolicy savedPolicy = StrictMode.allowThreadDiskReads();
        Entry entry = new Entry();
        int lastIdx = 1;
        try {
            ProcFileReader reader = new ProcFileReader(new FileInputStream(detailPath));
            reader.finishLine();
            while (reader.hasMoreData()) {
                int idx = reader.nextInt();
                if (idx == lastIdx + 1) {
                    lastIdx = idx;
                    entry.iface = reader.nextString();
                    if (limitIfaces == null || ArrayUtils.contains((Object[]) limitIfaces, entry.iface)) {
                        entry.nameOrHash = reader.nextString();
                        entry.rxBytes = reader.nextLong();
                        entry.rxPackets = reader.nextLong();
                        entry.txBytes = reader.nextLong();
                        entry.txPackets = reader.nextLong();
                        stats.combineValues(entry);
                        reader.finishLine();
                    } else {
                        reader.finishLine();
                    }
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("inconsistent idx=");
                    stringBuilder.append(idx);
                    stringBuilder.append(" after lastIdx=");
                    stringBuilder.append(lastIdx);
                    throw new ProtocolException(stringBuilder.toString());
                }
            }
            IoUtils.closeQuietly(reader);
            StrictMode.setThreadPolicy(savedPolicy);
        } catch (NullPointerException | NumberFormatException e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("problem parsing idx ");
            stringBuilder2.append(1);
            throw new ProtocolException(stringBuilder2.toString());
        } catch (Throwable th) {
            IoUtils.closeQuietly(null);
            StrictMode.setThreadPolicy(savedPolicy);
        }
    }
}
