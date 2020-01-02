package miui.securitycenter.powercenter;

import android.os.BatteryStats;
import android.os.BatteryStats.HistoryItem;
import android.os.SystemClock;

public class BatteryHistoryHelper {
    private BatteryStats mStats;
    private HistoryItem rec = new HistoryItem();

    public void refreshHistory() {
        this.mStats = BatteryStatsUtils.getBatteryStats();
    }

    public long getBatteryUsageRealtime() {
        BatteryStats batteryStats = this.mStats;
        if (batteryStats == null) {
            return 0;
        }
        return batteryStats.computeBatteryRealtime(SystemClock.elapsedRealtime() * 1000, 0);
    }

    public long getScreenOnTime() {
        if (this.mStats == null) {
            return 0;
        }
        return this.mStats.getScreenOnTime(this.mStats.getBatteryRealtime(SystemClock.elapsedRealtime() * 1000), 0);
    }

    public boolean startIterate() {
        BatteryStats batteryStats = this.mStats;
        if (batteryStats == null) {
            return false;
        }
        return batteryStats.startIteratingHistoryLocked();
    }

    public void finishIterate() {
        BatteryStats batteryStats = this.mStats;
        if (batteryStats != null) {
            batteryStats.finishIteratingHistoryLocked();
        }
    }

    public boolean getNextHistoryItem(HistoryItemWrapper item) {
        boolean ret = this.mStats;
        boolean z = false;
        if (!ret || !ret.getNextHistoryLocked(this.rec)) {
            return false;
        }
        int bin;
        item.cmd = this.rec.cmd;
        item.time = this.rec.time;
        item.batteryLevel = this.rec.batteryLevel;
        item.batteryStatus = this.rec.batteryStatus;
        item.batteryHealth = this.rec.batteryHealth;
        item.batteryPlugType = this.rec.batteryPlugType;
        item.batteryTemperature = this.rec.batteryTemperature;
        item.batteryVoltage = this.rec.batteryVoltage;
        item.wifiOn = (this.rec.states2 & 536870912) != 0;
        item.gpsOn = (this.rec.states & 536870912) != 0;
        item.charging = (this.rec.states & 524288) != 0;
        item.screenOn = (this.rec.states & 1048576) != 0;
        item.wakelockOn = (this.rec.states & 1073741824) != 0;
        if ((this.rec.states & Integer.MIN_VALUE) != 0) {
            z = true;
        }
        item.cpuRunning = z;
        if (((this.rec.states & 448) >> 6) == 3) {
            bin = 0;
        } else if ((this.rec.states & 2097152) != 0) {
            bin = 1;
        } else {
            bin = ((this.rec.states & 56) >> 3) + 2;
        }
        item.phoneSignalStrength = bin;
        return true;
    }
}
