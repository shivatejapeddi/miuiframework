package miui.mms;

import android.os.SystemProperties;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import miui.os.Build;
import miui.telephony.TelephonyManager;

public class MixinUtils {
    private static final Set<String> EU = new HashSet(Arrays.asList(new String[]{"AT", "BE", "BG", "CY", "CZ", "DE", "DK", "EE", "ES", "FI", "FR", "GB", "GR", "HR", "HU", "IE", "IT", "LT", "LU", "LV", "MT", "NL", "PL", "PT", "RO", "SE", "SI", "SK"}));
    private static final String PK_MCC = "410";

    private static boolean isInEURegion() {
        return EU.contains(SystemProperties.get("ro.miui.region", "unknown"));
    }

    private static boolean matchImsi(int slotId, String mcc) {
        String imsi = TelephonyManager.getDefault().getNetworkOperatorForSlot(slotId);
        return imsi != null && imsi.startsWith(mcc);
    }

    public static boolean isMxSupported(int slotId) {
        if (Build.IS_CM_CUSTOMIZATION || Build.IS_TABLET) {
            return false;
        }
        if (!Build.IS_INTERNATIONAL_BUILD) {
            return true;
        }
        if (isInEURegion() || matchImsi(slotId, PK_MCC)) {
            return false;
        }
        return true;
    }

    public static boolean isMxSupported() {
        if (Build.IS_CM_CUSTOMIZATION || Build.IS_TABLET) {
            return false;
        }
        if (!Build.IS_INTERNATIONAL_BUILD) {
            return true;
        }
        if (isInEURegion()) {
            return false;
        }
        int simCount = TelephonyManager.getDefault().getPhoneCount();
        int i = 0;
        while (i < simCount) {
            if (TelephonyManager.getDefault().hasIccCard(i) && matchImsi(i, PK_MCC)) {
                return false;
            }
            i++;
        }
        return true;
    }
}
