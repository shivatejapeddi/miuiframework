package android.telephony;

import android.content.pm.PackageManager;
import android.os.PersistableBundle;

public final class MiuiCellSignalStrengthLte {
    private static final boolean DBG = false;
    private static final String LOG_TAG = "MiuiCellSignalStrengthLte";
    private static final int MAX_LTE_RSRP = -44;
    private static final int MIN_LTE_RSRP = -140;

    public static int updateMiuiLevel(PersistableBundle cc, ServiceState ss, int rsrp, int rssi) {
        boolean rsrpOnly;
        int rsrpIconLevel;
        int[] thresholds = MiuiCellSignalStrength.getCustomedRsrpThresholds();
        if (cc == null) {
            rsrpOnly = false;
        } else {
            rsrpOnly = cc.getBoolean(CarrierConfigManager.KEY_USE_ONLY_RSRP_FOR_LTE_SIGNAL_BAR_BOOL, false);
        }
        int rsrpBoost = 0;
        if (ss != null) {
            rsrpBoost = ss.getLteEarfcnRsrpBoost();
        }
        int rsrpVal = rsrp + rsrpBoost;
        if (rsrpVal < MIN_LTE_RSRP || rsrpVal > -44) {
            rsrpIconLevel = -1;
        } else {
            rsrpIconLevel = thresholds.length;
            while (rsrpIconLevel > 0 && rsrpVal < thresholds[rsrpIconLevel - 1]) {
                rsrpIconLevel--;
            }
        }
        if (rsrpOnly && rsrpIconLevel != -1) {
            return rsrpIconLevel;
        }
        if (-1 != -1 && rsrpIconLevel != -1) {
            return rsrpIconLevel < -1 ? rsrpIconLevel : -1;
        } else if (-1 != -1) {
            return -1;
        } else {
            if (rsrpIconLevel != -1) {
                return rsrpIconLevel;
            }
            int rssiIconLevel;
            if (rssi > -51) {
                rssiIconLevel = 0;
            } else if (rssi >= -89) {
                rssiIconLevel = 5;
            } else if (rssi >= -95) {
                rssiIconLevel = 4;
            } else if (rssi >= -101) {
                rssiIconLevel = 3;
            } else if (rssi >= PackageManager.INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID) {
                rssiIconLevel = 2;
            } else if (rssi >= -113) {
                rssiIconLevel = 1;
            } else {
                rssiIconLevel = 0;
            }
            return rssiIconLevel;
        }
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
