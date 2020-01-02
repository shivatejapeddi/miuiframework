package com.android.internal.os;

import android.util.ArrayMap;
import android.util.Slog;
import java.util.Map;

public final class RailStats {
    private static final String CELLULAR_SUBSYSTEM = "cellular";
    private static final String TAG = "RailStats";
    private static final String WIFI_SUBSYSTEM = "wifi";
    private long mCellularTotalEnergyUseduWs = 0;
    private Map<Long, RailInfoData> mRailInfoData = new ArrayMap();
    private boolean mRailStatsAvailability = true;
    private long mWifiTotalEnergyUseduWs = 0;

    public static class RailInfoData {
        private static final String TAG = "RailInfoData";
        public long energyUsedSinceBootuWs;
        public long index;
        public String railName;
        public String subSystemName;
        public long timestampSinceBootMs;

        private RailInfoData(long index, String railName, String subSystemName, long timestampSinceBootMs, long energyUsedSinceBoot) {
            this.index = index;
            this.railName = railName;
            this.subSystemName = subSystemName;
            this.timestampSinceBootMs = timestampSinceBootMs;
            this.energyUsedSinceBootuWs = energyUsedSinceBoot;
        }

        public void printData() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Index = ");
            stringBuilder.append(this.index);
            String stringBuilder2 = stringBuilder.toString();
            String str = TAG;
            Slog.d(str, stringBuilder2);
            stringBuilder = new StringBuilder();
            stringBuilder.append("RailName = ");
            stringBuilder.append(this.railName);
            Slog.d(str, stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("SubSystemName = ");
            stringBuilder.append(this.subSystemName);
            Slog.d(str, stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("TimestampSinceBootMs = ");
            stringBuilder.append(this.timestampSinceBootMs);
            Slog.d(str, stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append("EnergyUsedSinceBootuWs = ");
            stringBuilder.append(this.energyUsedSinceBootuWs);
            Slog.d(str, stringBuilder.toString());
        }
    }

    public void updateRailData(long index, String railName, String subSystemName, long timestampSinceBootMs, long energyUsedSinceBootuWs) {
        String str = subSystemName;
        long j = timestampSinceBootMs;
        long j2 = energyUsedSinceBootuWs;
        String str2 = "wifi";
        boolean equals = str.equals(str2);
        String str3 = CELLULAR_SUBSYSTEM;
        if (equals || str.equals(str3)) {
            RailInfoData node = (RailInfoData) this.mRailInfoData.get(Long.valueOf(index));
            RailInfoData railInfoData;
            String str4;
            String str5;
            if (node == null) {
                RailInfoData railInfoData2 = railInfoData;
                Map map = this.mRailInfoData;
                Long valueOf = Long.valueOf(index);
                str4 = str3;
                str5 = str2;
                railInfoData = new RailInfoData(index, railName, subSystemName, timestampSinceBootMs, energyUsedSinceBootuWs);
                map.put(valueOf, railInfoData2);
                if (str.equals(str5)) {
                    this.mWifiTotalEnergyUseduWs += j2;
                    return;
                }
                if (str.equals(str4)) {
                    this.mCellularTotalEnergyUseduWs += j2;
                }
                return;
            }
            str4 = str3;
            str5 = str2;
            railInfoData = node;
            long j3 = timestampSinceBootMs;
            long energyUsedSinceLastLoguWs = j2 - railInfoData.energyUsedSinceBootuWs;
            if (j3 - railInfoData.timestampSinceBootMs < 0 || energyUsedSinceLastLoguWs < 0) {
                energyUsedSinceLastLoguWs = railInfoData.energyUsedSinceBootuWs;
            }
            railInfoData.timestampSinceBootMs = j3;
            railInfoData.energyUsedSinceBootuWs = j2;
            if (str.equals(str5)) {
                this.mWifiTotalEnergyUseduWs += energyUsedSinceLastLoguWs;
                return;
            }
            if (str.equals(str4)) {
                this.mCellularTotalEnergyUseduWs += energyUsedSinceLastLoguWs;
            }
        }
    }

    public void resetCellularTotalEnergyUsed() {
        this.mCellularTotalEnergyUseduWs = 0;
    }

    public void resetWifiTotalEnergyUsed() {
        this.mWifiTotalEnergyUseduWs = 0;
    }

    public long getCellularTotalEnergyUseduWs() {
        return this.mCellularTotalEnergyUseduWs;
    }

    public long getWifiTotalEnergyUseduWs() {
        return this.mWifiTotalEnergyUseduWs;
    }

    public void reset() {
        this.mCellularTotalEnergyUseduWs = 0;
        this.mWifiTotalEnergyUseduWs = 0;
    }

    public RailStats getRailStats() {
        return this;
    }

    public void setRailStatsAvailability(boolean railStatsAvailability) {
        this.mRailStatsAvailability = railStatsAvailability;
    }

    public boolean isRailStatsAvailable() {
        return this.mRailStatsAvailability;
    }
}
