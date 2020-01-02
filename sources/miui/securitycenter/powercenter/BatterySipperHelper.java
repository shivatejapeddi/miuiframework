package miui.securitycenter.powercenter;

import android.content.Context;
import android.text.TextUtils;
import com.android.internal.os.BatterySipper;

class BatterySipperHelper {
    BatterySipperHelper() {
    }

    static BatterySipper makeBatterySipper(Context context, int drainType, BatterySipper osSipper) {
        if (osSipper == null) {
            return new BatterySipper(context, drainType, -1, 0.0d);
        }
        int uid = -1;
        if (osSipper.uidObj != null) {
            uid = UidUtils.getRealUid(osSipper.uidObj.getUid());
        }
        BatterySipper sipper = new BatterySipper(context, drainType, uid, osSipper.totalPowerMah);
        sipper.usageTime = osSipper.usageTimeMs;
        sipper.cpuTime = osSipper.cpuTimeMs;
        sipper.gpsTime = osSipper.gpsTimeMs;
        sipper.wifiRunningTime = osSipper.wifiRunningTimeMs;
        sipper.cpuFgTime = osSipper.cpuFgTimeMs;
        sipper.wakeLockTime = osSipper.wakeLockTimeMs;
        sipper.noCoveragePercent = osSipper.noCoveragePercent;
        sipper.mobileRxBytes = osSipper.mobileRxBytes;
        sipper.mobileTxBytes = osSipper.mobileTxBytes;
        sipper.wifiRxBytes = osSipper.wifiRxBytes;
        sipper.wifiTxBytes = osSipper.wifiTxBytes;
        if (TextUtils.isEmpty(sipper.name)) {
            sipper.name = osSipper.packageWithHighestDrain;
        }
        return sipper;
    }

    static BatterySipper makeBatterySipperForSystemApp(Context context, BatterySipper osSipper) {
        if (osSipper == null) {
            return null;
        }
        BatterySipper sipper = new BatterySipper(context, osSipper.packageWithHighestDrain, osSipper.totalPowerMah);
        sipper.usageTime = osSipper.usageTimeMs;
        sipper.cpuTime = osSipper.cpuTimeMs;
        sipper.gpsTime = osSipper.gpsTimeMs;
        sipper.wifiRunningTime = osSipper.wifiRunningTimeMs;
        sipper.cpuFgTime = osSipper.cpuFgTimeMs;
        sipper.wakeLockTime = osSipper.wakeLockTimeMs;
        sipper.noCoveragePercent = osSipper.noCoveragePercent;
        sipper.mobileRxBytes = osSipper.mobileRxBytes;
        sipper.mobileTxBytes = osSipper.mobileTxBytes;
        sipper.wifiRxBytes = osSipper.wifiRxBytes;
        sipper.wifiTxBytes = osSipper.wifiTxBytes;
        return sipper;
    }

    static BatterySipper addBatterySipper(BatterySipper sipper, BatterySipper osSipper) {
        sipper.usageTime += osSipper.usageTimeMs;
        sipper.value += osSipper.totalPowerMah;
        return sipper;
    }
}
