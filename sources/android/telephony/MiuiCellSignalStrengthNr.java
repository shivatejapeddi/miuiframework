package android.telephony;

import android.os.PersistableBundle;

public final class MiuiCellSignalStrengthNr {
    public static int updateLevel(PersistableBundle cc, ServiceState ss, int csiRsrp) {
        int[] thresholds = MiuiCellSignalStrength.getCustomedRsrpThresholds();
        if (csiRsrp == Integer.MAX_VALUE) {
            return 0;
        }
        if (csiRsrp >= thresholds[4]) {
            return 5;
        }
        if (csiRsrp >= thresholds[3]) {
            return 4;
        }
        if (csiRsrp >= thresholds[2]) {
            return 3;
        }
        if (csiRsrp >= thresholds[1]) {
            return 2;
        }
        if (csiRsrp >= thresholds[0]) {
            return 1;
        }
        return 0;
    }
}
