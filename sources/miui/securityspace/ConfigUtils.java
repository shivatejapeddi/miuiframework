package miui.securityspace;

import android.os.Build.VERSION;
import miui.util.FeatureParser;

public class ConfigUtils {
    public static boolean isSupportXSpace() {
        return isKitKat() ^ 1;
    }

    public static boolean isSupportSecuritySpace() {
        return (isPad() || isKitKat()) ? false : true;
    }

    private static boolean isPad() {
        return FeatureParser.getBoolean("is_pad", false);
    }

    private static boolean isKitKat() {
        return VERSION.SDK_INT == 19;
    }
}
