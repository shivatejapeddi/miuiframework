package android.telephony;

import android.os.PersistableBundle;

public final class MiuiCellSignalStrengthWcdma {
    private static final boolean DBG = false;
    private static final String LOG_TAG = "MiuiCellSignalStrengthWcdma";
    private static final int WCDMA_RSSI_EXCELLENT = (shouldOptimize ? -93 : -89);
    private static final int WCDMA_RSSI_GOOD = -101;
    private static final int WCDMA_RSSI_GREAT = (shouldOptimize ? -97 : -95);
    private static final int WCDMA_RSSI_MAX = -51;
    private static final int WCDMA_RSSI_MIN = -113;
    private static final int WCDMA_RSSI_MODERATE = -107;
    private static final int WCDMA_RSSI_POOR = -113;
    private static final boolean shouldOptimize = MiuiSignalStrength.shouldOptimizeSignalStrength();

    public static int updateLevel(PersistableBundle cc, ServiceState ss, int rssi) {
        if (rssi < -113 || rssi > -51) {
            return 0;
        }
        if (rssi >= WCDMA_RSSI_EXCELLENT) {
            return 5;
        }
        if (rssi >= WCDMA_RSSI_GREAT) {
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
