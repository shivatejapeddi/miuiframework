package com.android.internal.os;

import android.content.Context;
import com.android.internal.os.UidAppBatteryStatsImpl.UidPackage;

public class UidAppWifiPowerCalculator {
    private static final boolean DEBUG = false;
    private static final String TAG = "UidAppWifiPowerCalculator";
    private final Context mContext;
    private final double mWifiPowerPerPacket;
    private final double mWifiPowerScan;

    public UidAppWifiPowerCalculator(Context context) {
        this.mContext = context;
        PowerProfile profile = new PowerProfile(this.mContext);
        this.mWifiPowerPerPacket = getWifiPowerPerPacket(profile);
        this.mWifiPowerScan = profile.getAveragePower(PowerProfile.POWER_WIFI_SCAN);
    }

    private static double getWifiPowerPerPacket(PowerProfile profile) {
        return (profile.getAveragePower(PowerProfile.POWER_WIFI_ACTIVE) / 3600.0d) / 61.03515625d;
    }

    public void calculateUidApp(BatterySipper app, UidPackage p, long rawRealtimeUs, int statsType) {
        app.wifiRxPackets = p.getNetworkActivityPackets(2, statsType);
        app.wifiTxPackets = p.getNetworkActivityPackets(3, statsType);
        app.wifiRxBytes = p.getNetworkActivityBytes(2, statsType);
        app.wifiTxBytes = p.getNetworkActivityBytes(3, statsType);
        app.wifiPowerMah = (((double) (app.wifiRxPackets + app.wifiTxPackets)) * this.mWifiPowerPerPacket) + ((((double) (p.getWifiScanTime(rawRealtimeUs, statsType) / 1000)) * this.mWifiPowerScan) / 3600000.0d);
    }
}
