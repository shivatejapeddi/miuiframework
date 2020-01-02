package android.telephony;

import android.content.pm.PackageManager;
import miui.os.Build;

public abstract class MiuiCellSignalStrength {
    public static final int MIUI_NUM_SIGNAL_STRENGTH_BINS = 6;
    protected static final int MIUI_NUM_SIGNAL_STRENGTH_THRESHOLDS = 5;
    public static final String[] MIUI_SIGNAL_STRENGTH_NAMES = new String[]{"none", "poor", "moderate", "good", "great", "excellent"};
    private static final int[] RSRP_THRESH_CU_TEST = new int[]{PackageManager.INSTALL_FAILED_MULTIPACKAGE_INCONSISTENCY, PackageManager.INSTALL_FAILED_BAD_SIGNATURE, PackageManager.NO_NATIVE_LIBRARIES, -110, PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING};
    private static final int[] RSRP_THRESH_INDIA = new int[]{-128, PackageManager.INSTALL_FAILED_ABORTED, -110, PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING, -97};
    private static final int[] RSRP_THRESH_LENIENT = new int[]{-140, -125, PackageManager.INSTALL_FAILED_ABORTED, -110, PackageManager.INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION};
    private static final int[] RSRP_THRESH_STRICT = new int[]{PackageManager.INSTALL_FAILED_MULTIPACKAGE_INCONSISTENCY, PackageManager.INSTALL_FAILED_ABORTED, -110, PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING, -97};
    public static final int SIGNAL_STRENGTH_EXCELLENT = 5;

    public abstract int getMiuiLevel();

    protected static final int[] getCustomedRsrpThresholds() {
        if (Build.IS_CU_CUSTOMIZATION_TEST) {
            return RSRP_THRESH_CU_TEST;
        }
        if (!Build.IS_CM_CUSTOMIZATION && !Build.IS_CT_CUSTOMIZATION && !Build.IS_INTERNATIONAL_BUILD) {
            return RSRP_THRESH_LENIENT;
        }
        int[] iArr;
        if (MiuiSignalStrength.shouldOptimizeSignalStrength()) {
            iArr = RSRP_THRESH_INDIA;
        } else {
            iArr = RSRP_THRESH_STRICT;
        }
        return RSRP_THRESH_STRICT;
    }
}
