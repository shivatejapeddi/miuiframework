package com.android.internal.telephony;

import android.provider.MiuiSettings.AntiSpam;
import android.text.TextUtils;
import android.util.Slog;
import java.util.ArrayList;
import java.util.List;
import miui.os.Build;

public class TelephonyPermissionsInjector {
    private static final String TAG = TelephonyPermissionsInjector.class.getSimpleName();
    private static List<String> sAllowedList = new ArrayList();

    static {
        sAllowedList.add("android");
        sAllowedList.add("com.miui.analytics");
        if (!Build.IS_INTERNATIONAL_BUILD) {
            sAllowedList.add(AntiSpam.ANTISPAM_PKG);
        }
    }

    public static boolean isAllowedAccessDeviceIdentifiers(String callingPackage) {
        if (TextUtils.isEmpty(callingPackage) || sAllowedList.contains(callingPackage)) {
            return true;
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MIUILOG- Permission Denied Get Telephony identifier :  pkg : ");
        stringBuilder.append(callingPackage);
        Slog.d(str, stringBuilder.toString());
        return false;
    }
}
