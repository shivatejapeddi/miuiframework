package com.android.internal.os;

import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.BatteryStats;
import android.os.BatteryStats.Uid;
import android.util.SparseArray;
import java.util.List;

public class SensorPowerCalculator extends PowerCalculator {
    private final double mGpsPower;
    private final List<Sensor> mSensors;

    public SensorPowerCalculator(PowerProfile profile, SensorManager sensorManager, BatteryStats stats, long rawRealtimeUs, int statsType) {
        this.mSensors = sensorManager.getSensorList(-1);
        this.mGpsPower = getAverageGpsPower(profile, stats, rawRealtimeUs, statsType);
    }

    public void calculateApp(BatterySipper app, Uid u, long rawRealtimeUs, long rawUptimeUs, int statsType) {
        BatterySipper batterySipper = app;
        SparseArray<? extends Uid.Sensor> sensorStats = u.getSensorStats();
        int NSE = sensorStats.size();
        int ise = 0;
        while (ise < NSE) {
            SparseArray<? extends Uid.Sensor> sensorStats2;
            int NSE2;
            Uid.Sensor sensor = (Uid.Sensor) sensorStats.valueAt(ise);
            int sensorHandle = sensorStats.keyAt(ise);
            long sensorTime = sensor.getSensorTime().getTotalTimeLocked(rawRealtimeUs, statsType) / 1000;
            Uid.Sensor sensor2;
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
                batterySipper.gpsPowerMah = (((double) batterySipper.gpsTimeMs) * this.mGpsPower) / 3600000.0d;
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
