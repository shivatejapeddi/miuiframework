package com.android.internal.os;

import android.os.BatteryStats.Uid;
import android.os.BatteryStats.Uid.Proc;
import android.util.ArrayMap;
import android.util.Log;

public class CpuPowerCalculator extends PowerCalculator {
    private static final boolean DEBUG = false;
    private static final long MICROSEC_IN_HR = 3600000000L;
    private static final String TAG = "CpuPowerCalculator";
    private final PowerProfile mProfile;

    public CpuPowerCalculator(PowerProfile profile) {
        this.mProfile = profile;
    }

    public void calculateApp(BatterySipper app, Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        int speed;
        long[] cpuClusterTimes;
        double cpuPowerMaUs;
        BatterySipper batterySipper = app;
        Uid uid = u;
        int i = statsType;
        batterySipper.cpuTimeMs = (uid.getUserCpuTimeUs(i) + uid.getSystemCpuTimeUs(i)) / 1000;
        int numClusters = this.mProfile.getNumCpuClusters();
        double cpuPowerMaUs2 = 0.0d;
        for (int cluster = 0; cluster < numClusters; cluster++) {
            for (speed = 0; speed < this.mProfile.getNumSpeedStepsInCpuCluster(cluster); speed++) {
                cpuPowerMaUs2 += ((double) uid.getTimeAtCpuSpeed(cluster, speed, i)) * this.mProfile.getAveragePowerForCpuCore(cluster, speed);
            }
        }
        cpuPowerMaUs2 += ((double) (u.getCpuActiveTime() * 1000)) * this.mProfile.getAveragePower(PowerProfile.POWER_CPU_ACTIVE);
        long[] cpuClusterTimes2 = u.getCpuClusterTimes();
        if (cpuClusterTimes2 != null) {
            if (cpuClusterTimes2.length == numClusters) {
                for (int i2 = 0; i2 < numClusters; i2++) {
                    cpuPowerMaUs2 += ((double) (cpuClusterTimes2[i2] * 1000)) * this.mProfile.getAveragePowerForCpuCluster(i2);
                }
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("UID ");
                stringBuilder.append(u.getUid());
                stringBuilder.append(" CPU cluster # mismatch: Power Profile # ");
                stringBuilder.append(numClusters);
                stringBuilder.append(" actual # ");
                stringBuilder.append(cpuClusterTimes2.length);
                Log.w(TAG, stringBuilder.toString());
            }
        }
        batterySipper.cpuPowerMah = cpuPowerMaUs2 / 3.6E9d;
        double highestDrain = 0.0d;
        batterySipper.cpuFgTimeMs = 0;
        ArrayMap<String, ? extends Proc> processStats = u.getProcessStats();
        speed = processStats.size();
        int i3 = 0;
        while (i3 < speed) {
            Proc ps = (Proc) processStats.valueAt(i3);
            String processName = (String) processStats.keyAt(i3);
            int numClusters2 = numClusters;
            cpuClusterTimes = cpuClusterTimes2;
            batterySipper.cpuFgTimeMs += ps.getForegroundTime(i);
            numClusters = (ps.getUserTime(i) + ps.getSystemTime(i)) + ps.getForegroundTime(i);
            if (batterySipper.packageWithHighestDrain != null) {
                String str = "*";
                if (batterySipper.packageWithHighestDrain.startsWith(str)) {
                    cpuPowerMaUs = cpuPowerMaUs2;
                } else {
                    cpuPowerMaUs = cpuPowerMaUs2;
                    if (highestDrain < ((double) numClusters) && !processName.startsWith(str)) {
                        highestDrain = (double) numClusters;
                        batterySipper.packageWithHighestDrain = processName;
                    }
                    i3++;
                    uid = u;
                    numClusters = numClusters2;
                    cpuClusterTimes2 = cpuClusterTimes;
                    cpuPowerMaUs2 = cpuPowerMaUs;
                }
            } else {
                cpuPowerMaUs = cpuPowerMaUs2;
            }
            highestDrain = (double) numClusters;
            batterySipper.packageWithHighestDrain = processName;
            i3++;
            uid = u;
            numClusters = numClusters2;
            cpuClusterTimes2 = cpuClusterTimes;
            cpuPowerMaUs2 = cpuPowerMaUs;
        }
        cpuClusterTimes = cpuClusterTimes2;
        cpuPowerMaUs = cpuPowerMaUs2;
        if (batterySipper.cpuFgTimeMs > batterySipper.cpuTimeMs) {
            batterySipper.cpuTimeMs = batterySipper.cpuFgTimeMs;
        }
    }
}
