package android.telephony;

import android.content.pm.PackageManager;
import android.os.PersistableBundle;

public final class MiuiCellSignalStrengthCdma {
    private static final boolean DBG = false;
    private static final String LOG_TAG = "MiuiCellSignalStrengthCdma";

    public static int updateLevel(PersistableBundle cc, ServiceState ss, int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr) {
        int cdmaLevel = getCdmaLevel(cdmaDbm, cdmaEcio);
        int evdoLevel = getEvdoLevel(evdoDbm, evdoEcio, evdoSnr);
        if (evdoLevel == 0) {
            return getCdmaLevel(cdmaDbm, cdmaEcio);
        }
        if (cdmaLevel == 0) {
            return getEvdoLevel(evdoDbm, evdoEcio, evdoSnr);
        }
        return cdmaLevel < evdoLevel ? cdmaLevel : evdoLevel;
    }

    public static int getCdmaLevel(int cdmaDbm, int cdmaEcio) {
        if (cdmaDbm == Integer.MAX_VALUE) {
            return 0;
        }
        if (cdmaDbm >= -90) {
            return 5;
        }
        if (cdmaDbm >= -93) {
            return 4;
        }
        if (cdmaDbm >= -97) {
            return 3;
        }
        if (cdmaDbm >= -101) {
            return 2;
        }
        if (cdmaDbm >= PackageManager.INSTALL_PARSE_FAILED_MANIFEST_EMPTY) {
            return 1;
        }
        return 0;
    }

    public static int getEvdoLevel(int evdoDbm, int evdoEcio, int evdoSnr) {
        if (evdoDbm == Integer.MAX_VALUE) {
            return 0;
        }
        if (evdoDbm >= -90) {
            return 5;
        }
        if (evdoDbm >= -93) {
            return 4;
        }
        if (evdoDbm >= -97) {
            return 3;
        }
        if (evdoDbm >= -101) {
            return 2;
        }
        if (evdoDbm >= PackageManager.INSTALL_PARSE_FAILED_MANIFEST_EMPTY) {
            return 1;
        }
        return 0;
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
