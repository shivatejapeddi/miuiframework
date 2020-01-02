package android.telephony;

import android.os.PersistableBundle;

public final class MiuiCellSignalStrengthGsm {
    private static final boolean DBG = false;
    private static final int GSM_RSSI_EXCELLENT = (shouldOptimize ? -93 : -89);
    private static final int GSM_RSSI_GOOD = -101;
    private static final int GSM_RSSI_GREAT = (shouldOptimize ? -97 : -95);
    private static final int GSM_RSSI_MAX = -51;
    private static final int GSM_RSSI_MIN = -113;
    private static final int GSM_RSSI_MODERATE = -107;
    private static final int GSM_RSSI_POOR = -113;
    private static final String LOG_TAG = "MiuiCellSignalStrengthGsm";
    private static final int[] sRssiThresholds = new int[]{-113, -107, -101, GSM_RSSI_GREAT, GSM_RSSI_EXCELLENT};
    private static final boolean shouldOptimize = MiuiSignalStrength.shouldOptimizeSignalStrength();

    public static int updateLevel(PersistableBundle cc, ServiceState ss, int rssi) {
        if (rssi < -113 || rssi > -51) {
            return 0;
        }
        if (rssi >= GSM_RSSI_EXCELLENT) {
            return 5;
        }
        if (rssi >= GSM_RSSI_GREAT) {
            return 4;
        }
        if (rssi >= -101) {
            return 3;
        }
        if (rssi >= -107) {
            return 2;
        }
        if (rssi >= -113) {
            return 1;
        }
        return 0;
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
