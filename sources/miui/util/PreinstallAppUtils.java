package miui.util;

import miui.os.MiuiInit;

public class PreinstallAppUtils {
    private PreinstallAppUtils() {
    }

    public static boolean isPreinstalledPackage(String pkg) {
        return MiuiInit.isPreinstalledPackage(pkg);
    }

    public static boolean supportSignVerifyInCust() {
        return FeatureParser.getBoolean("support_sign_verify_in_cust", false);
    }
}
