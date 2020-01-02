package com.android.internal.os;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.BatteryStats;
import android.util.SparseArray;
import com.android.internal.os.UidAppBatteryStatsImpl.UidPackage;
import java.util.List;

public class UidAppSensorPowerCalculator {
    private static final boolean DEBUG = false;
    private static final String TAG = "UidAppSensorPowerCalculator";
    private final Context mContext;
    private final double mGpsPowerOn;
    private final List<Sensor> mSensors = ((SensorManager) this.mContext.getSystemService(Context.SENSOR_SERVICE)).getSensorList(-1);

    public UidAppSensorPowerCalculator(Context context, BatteryStats stats, long rawRealtimeUs, int statsType) {
        this.mContext = context;
        this.mGpsPowerOn = getAverageGpsPower(new PowerProfile(this.mContext), stats, rawRealtimeUs, statsType);
    }

    public void calculateUidApp(BatterySipper app, UidPackage p, long rawRealtimeUs, int statsType) {
        BatterySipper batterySipper = app;
        SparseArray<UidPackage.Sensor> sensorStats = p.getSensorStats();
        int NSE = sensorStats.size();
        int ise = 0;
        while (ise < NSE) {
            SparseArray<UidPackage.Sensor> sensorStats2;
            int NSE2;
            UidPackage.Sensor sensor = (UidPackage.Sensor) sensorStats.valueAt(ise);
            int sensorHandle = sensorStats.keyAt(ise);
            long sensorTime = sensor.getSensorTime().getTotalTimeLocked(rawRealtimeUs, statsType) / 1000;
            UidPackage.Sensor sensor2;
            int i;
            if (sensorHandle != -10000) {
                int sensorsCount = this.mSensors.size();
                int i2 = 0;
                while (i2 < sensorsCount) {
                    Sensor s = (Sensor) this.mSensors.get(i2);
                    sensorStats2 = sensorStats;
                    if (s.getHandle() == sensorHandle) {
                        NSE2 = NSE;
                        batterySipper.sensorPowerMah += (double) ((((float) sensorTime) * s.getPower()) / 3600000.0f);
                        break;
                    }
                    sensor2 = sensor;
                    i = sensorHandle;
                    i2++;
                    sensorStats = sensorStats2;
                }
                sensorStats2 = sensorStats;
                NSE2 = NSE;
                sensor2 = sensor;
                i = sensorHandle;
            } else {
                sensorStats2 = sensorStats;
                NSE2 = NSE;
                sensor2 = sensor;
                i = sensorHandle;
                batterySipper.gpsTimeMs = sensorTime;
                batterySipper.gpsPowerMah = (((double) batterySipper.gpsTimeMs) * this.mGpsPowerOn) / 3600000.0d;
            }
            ise++;
            sensorStats = sensorStats2;
            NSE = NSE2;
        }
    }

    private double getAverageGpsPower(PowerProfile profile, BatteryStats stats, long rawRealtimeUs, int statsType) {
        PowerProfile powerProfile = profile;
        double averagePower = powerProfile.getAveragePowerOrDefault(PowerProfile.POWER_GPS_ON, -1.0d);
        if (averagePower != -1.0d) {
            return averagePower;
        }
        double averagePower2 = 0.0d;
        long totalTime = 0;
        double totalPower = 0.0d;
        int i = 0;
        while (i < 2) {
            long timePerLevel = stats.getGpsSignalQualityTime(i, rawRealtimeUs, statsType);
            totalTime += timePerLevel;
            totalPower += powerProfile.getAveragePower(PowerProfile.POWER_GPS_SIGNAL_QUALITY_BASED, i) * ((double) timePerLevel);
            i++;
            powerProfile = profile;
            averagePower2 = averagePower2;
        }
        BatteryStats batteryStats = stats;
        long j = rawRealtimeUs;
        int i2 = statsType;
        double averagePower3 = averagePower2;
        if (totalTime != 0) {
            averagePower3 = totalPower / ((double) totalTime);
        }
        return averagePower3;
    }
}
