package com.android.internal.os;

import android.os.BatteryStats;
import android.os.BatteryStats.Timer;
import android.os.BatteryStats.Uid;
import android.util.LongSparseArray;

public class MemoryPowerCalculator extends PowerCalculator {
    private static final boolean DEBUG = false;
    public static final String TAG = "MemoryPowerCalculator";
    private final double[] powerAverages;

    public MemoryPowerCalculator(PowerProfile profile) {
        String str = PowerProfile.POWER_MEMORY;
        int numBuckets = profile.getNumElements(str);
        this.powerAverages = new double[numBuckets];
        for (int i = 0; i < numBuckets; i++) {
            this.powerAverages[i] = profile.getAveragePower(str, i);
            double d = this.powerAverages[i];
        }
    }

    public void calculateApp(BatterySipper app, Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
    }

    public void calculateRemaining(BatterySipper app, BatteryStats stats, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        BatterySipper batterySipper = app;
        double totalMah = 0.0d;
        long totalTimeMs = 0;
        LongSparseArray<? extends Timer> timers = stats.getKernelMemoryStats();
        for (int i = 0; i < timers.size(); i++) {
            double mAatRail = this.powerAverages;
            if (i >= mAatRail.length) {
                break;
            }
            mAatRail = mAatRail[(int) timers.keyAt(i)];
            long timeMs = ((Timer) timers.valueAt(i)).getTotalTimeLocked(rawRealtimeUs, statsType);
            totalMah += ((((double) timeMs) * mAatRail) / 60000.0d) / 60.0d;
            totalTimeMs += timeMs;
        }
        int i2 = statsType;
        batterySipper.usagePowerMah = totalMah;
        batterySipper.usageTimeMs = totalTimeMs;
    }
}
