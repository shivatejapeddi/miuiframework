package android.net;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import miui.os.Build;

class CaptivePortalTrackerInjector {
    private static final String CN_CAPTIVE_PORTAL_SERVER = "http://connect.rom.miui.com/generate_204";
    private static final String CN_OPERATOR = "460";

    CaptivePortalTrackerInjector() {
    }

    static final String getCaptivePortalServer(Context context, String server) {
        String networkOperator = ((TelephonyManager) context.getSystemService("phone")).getNetworkOperator();
        if ((TextUtils.isEmpty(networkOperator) && Build.checkRegion("CN")) || isCnFromOperator(networkOperator)) {
            return CN_CAPTIVE_PORTAL_SERVER;
        }
        return server;
    }

    static boolean isCnFromOperator(String operator) {
        String mcc = "";
        if (!TextUtils.isEmpty(operator) && operator.length() >= 3) {
            mcc = operator.substring(0, 3);
        }
        return CN_OPERATOR.equals(mcc);
    }
}
