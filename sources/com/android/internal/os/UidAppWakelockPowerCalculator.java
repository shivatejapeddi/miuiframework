package com.android.internal.os;

import android.content.Context;
import android.os.BatteryStats.Timer;
import android.os.Build.VERSION;
import android.util.ArrayMap;
import com.android.internal.os.UidAppBatteryStatsImpl.UidPackage;
import com.android.internal.os.UidAppBatteryStatsImpl.UidPackage.Wakelock;

public class UidAppWakelockPowerCalculator {
    private static final boolean DEBUG = false;
    private static final String TAG = "UidAppWakelockPowerCalculator";
    private final Context mContext;

    public UidAppWakelockPowerCalculator(Context context) {
        this.mContext = context;
    }

    public void calculateUidApp(BatterySipper app, UidPackage p, long rawRealtimeUs, int statsType) {
        long j;
        int i;
        BatterySipper batterySipper = app;
        PowerProfile pp = new PowerProfile(this.mContext);
        String powerOfAwake = "cpu.awake";
        if (VERSION.SDK_INT >= 28) {
            powerOfAwake = PowerProfile.POWER_CPU_IDLE;
        }
        double powerWakelock = pp.getAveragePower(powerOfAwake);
        long wakeLockTimeUs = 0;
        ArrayMap<String, ? extends Wakelock> wakelockStats = p.getWakelockStats();
        int wakelockStatsCount = wakelockStats.size();
        for (int i2 = 0; i2 < wakelockStatsCount; i2++) {
            Timer timer = ((Wakelock) wakelockStats.valueAt(i2)).getWakeTime();
            if (timer != null) {
                wakeLockTimeUs += timer.getTotalTimeLocked(rawRealtimeUs, statsType);
            } else {
                j = rawRealtimeUs;
                i = statsType;
            }
        }
        j = rawRealtimeUs;
        i = statsType;
        batterySipper.wakeLockTimeMs = wakeLockTimeUs / 1000;
        batterySipper.wakeLockPowerMah = (((double) batterySipper.wakeLockTimeMs) * powerWakelock) / 3600000.0d;
    }
}
