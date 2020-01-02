package com.android.internal.os;

import android.content.Context;
import com.android.internal.os.UidAppBatteryStatsImpl.UidPackage;

public class UidAppCpuPowerCalculator {
    private static final boolean DEBUG = false;
    private static final long MICROSEC_IN_HR = 3600000000L;
    private static final String TAG = "UidAppCpuPowerCalculator";
    private final Context mContext;

    public UidAppCpuPowerCalculator(Context context) {
        this.mContext = context;
    }

    public void calculateUidApp(BatterySipper app, UidPackage p, int statsType) {
        UidPackage uidPackage;
        int i;
        BatterySipper batterySipper = app;
        PowerProfile profile = new PowerProfile(this.mContext);
        batterySipper.cpuTimeMs = (p.getUserCpuTimeUs(statsType) + p.getSystemCpuTimeUs(statsType)) / 1000;
        int numClusters = profile.getNumCpuClusters();
        double cpuPowerMaUs = 0.0d;
        for (int cluster = 0; cluster < numClusters; cluster++) {
            for (int speed = 0; speed < profile.getNumSpeedStepsInCpuCluster(cluster); speed++) {
                cpuPowerMaUs += ((double) p.getTimeAtCpuSpeed(cluster, speed, statsType)) * profile.getAveragePowerForCpuCore(cluster, speed);
            }
            uidPackage = p;
            i = statsType;
        }
        uidPackage = p;
        i = statsType;
        batterySipper.cpuPowerMah = cpuPowerMaUs / 3.6E9d;
        if (batterySipper.cpuFgTimeMs > batterySipper.cpuTimeMs) {
            batterySipper.cpuTimeMs = batterySipper.cpuFgTimeMs;
        }
    }
}
