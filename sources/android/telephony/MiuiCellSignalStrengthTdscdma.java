package android.telephony;

import android.os.PersistableBundle;

public final class MiuiCellSignalStrengthTdscdma {
    private static final boolean DBG = false;
    private static final String LOG_TAG = "MiuiCellSignalStrengthTdscdma";
    private static final int TDSCDMA_RSCP_EXCELLENT = -88;
    private static final int TDSCDMA_RSCP_GOOD = -102;
    private static final int TDSCDMA_RSCP_GREAT = -95;
    private static final int TDSCDMA_RSCP_MAX = -24;
    private static final int TDSCDMA_RSCP_MODERATE = -109;
    private static final int TDSCDMA_RSCP_POOR = -120;
    private int mBitErrorRate;
    private int mRscp;
    private int mRssi;

    public static int updateLevel(PersistableBundle cc, ServiceState ss, int rssi, int rscp) {
        if (rscp > -24 || rscp == Integer.MAX_VALUE) {
            return 0;
        }
        if (rscp >= TDSCDMA_RSCP_EXCELLENT) {
            return 5;
        }
        if (rscp >= -95) {
            return 4;
        }
        if (rscp >= -102) {
            return 3;
        }
        if (rscp >= -109) {
            return 2;
        }
        if (rscp >= -120) {
            return 1;
        }
        return 0;
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
