package android.util;

import android.content.Context;
import android.content.Intent;
import android.os.SystemProperties;
import miui.os.Build;

public final class VirtualSim {
    public static boolean isSupportVirtualSim(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.miui.virtualsim", "com.miui.virtualsim.ui.MainActivity");
        if (isActivityExist(context, intent)) {
            return true;
        }
        return false;
    }

    public static boolean isSupportMiSim(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.miui.virtualsim", "com.miui.mimobile.ui.MmRouterActivity");
        if (isActivityExist(context, intent)) {
            if (!("LTE-CMCC".equals(SystemProperties.get("persist.radio.modem")) || Build.IS_INTERNATIONAL_BUILD)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isActivityExist(Context context, Intent intent) {
        if (context.getPackageManager().resolveActivity(intent, 0) == null) {
            return false;
        }
        return true;
    }
}
