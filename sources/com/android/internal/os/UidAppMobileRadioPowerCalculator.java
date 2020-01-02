package com.android.internal.os;

import android.content.Context;
import android.os.BatteryStats;
import android.util.Log;
import com.android.internal.os.UidAppBatteryStatsImpl.UidPackage;

public class UidAppMobileRadioPowerCalculator {
    private static final boolean DEBUG = false;
    private static final String TAG = "UidAppMobileRadioPowerCalculator";
    private final Context mContext;
    private final double[] mPowerBins = new double[5];
    private final double mPowerRadioOn;
    private BatteryStats mStats;

    private double getMobilePowerPerPacket(long rawRealtimeUs, int statsType) {
        double mobilePps;
        int i = statsType;
        double MOBILE_POWER = this.mPowerRadioOn / 3600.0d;
        long mobileData = this.mStats.getNetworkActivityPackets(0, i) + this.mStats.getNetworkActivityPackets(1, i);
        long radioDataUptimeMs = this.mStats.getMobileRadioActiveTime(rawRealtimeUs, i) / 1000;
        long MOBILE_BPS;
        if (mobileData == 0 || radioDataUptimeMs == 0) {
            MOBILE_BPS = 200000;
            mobilePps = 12.20703125d;
        } else {
            MOBILE_BPS = 200000;
            mobilePps = ((double) mobileData) / ((double) radioDataUptimeMs);
        }
        return (MOBILE_POWER / mobilePps) / 3600.0d;
    }

    public UidAppMobileRadioPowerCalculator(Context context, BatteryStats stats) {
        this.mContext = context;
        this.mStats = stats;
        PowerProfile profile = new PowerProfile(this.mContext);
        double temp = profile.getAveragePowerOrDefault(PowerProfile.POWER_RADIO_ACTIVE, -1.0d);
        if (temp != -1.0d) {
            this.mPowerRadioOn = temp;
            return;
        }
        double sum = 0.0d + profile.getAveragePower(PowerProfile.POWER_MODEM_CONTROLLER_RX);
        int i = 0;
        while (true) {
            double[] dArr = this.mPowerBins;
            if (i < dArr.length) {
                sum += profile.getAveragePower(PowerProfile.POWER_MODEM_CONTROLLER_TX, i);
                i++;
            } else {
                this.mPowerRadioOn = sum / ((double) (dArr.length + 1));
                return;
            }
        }
    }

    public void calculateUidApp(BatterySipper app, UidPackage p, long rawRealtimeUs, int statsType) {
        app.mobileRxPackets = p.getNetworkActivityPackets(0, statsType);
        app.mobileTxPackets = p.getNetworkActivityPackets(1, statsType);
        app.mobileActive = p.getMobileRadioActiveTime(statsType) / 1000;
        app.mobileActiveCount = p.getMobileRadioActiveCount(statsType);
        app.mobileRxBytes = p.getNetworkActivityBytes(0, statsType);
        app.mobileTxBytes = p.getNetworkActivityBytes(1, statsType);
        int i = (app.mobileActive > 0 ? 1 : (app.mobileActive == 0 ? 0 : -1));
        String str = TAG;
        StringBuilder stringBuilder;
        if (i > 0) {
            app.mobileRadioPowerMah = (((double) app.mobileActive) * this.mPowerRadioOn) / 3600000.0d;
            stringBuilder = new StringBuilder();
            stringBuilder.append("mobile active power: ");
            stringBuilder.append(app.mobileRadioPowerMah);
            Log.d(str, stringBuilder.toString());
            return;
        }
        app.mobileRadioPowerMah = ((double) (app.mobileRxPackets + app.mobileTxPackets)) * getMobilePowerPerPacket(rawRealtimeUs, statsType);
        stringBuilder = new StringBuilder();
        stringBuilder.append("mobile not active power: ");
        stringBuilder.append(app.mobileRadioPowerMah);
        Log.d(str, stringBuilder.toString());
    }
}
