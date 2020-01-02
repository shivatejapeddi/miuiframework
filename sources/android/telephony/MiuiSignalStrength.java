package android.telephony;

public class MiuiSignalStrength {
    public static final int MIUI_NUM_SIGNAL_STRENGTH_BINS = 6;
    public static final String[] MIUI_SIGNAL_STRENGTH_NAMES = new String[]{"none", "poor", "moderate", "good", "great", "excellent"};
    public static final int SIGNAL_STRENGTH_EXCELLENT = 5;

    public static boolean shouldOptimizeSignalStrength() {
        for (int i = 0; i < TelephonyManager.getDefault().getPhoneCount(); i++) {
            if (TelephonyManager.getDefault().getSimCountryIsoForPhone(i).equalsIgnoreCase("IN")) {
                return true;
            }
        }
        return false;
    }
}
