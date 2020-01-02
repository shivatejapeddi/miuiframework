package com.android.internal.telephony;

import android.provider.MiuiSettings.AntiSpam;
import com.android.internal.util.ArrayUtils;
import miui.provider.MiProfile;
import miui.push.PushConstants;

public class SmsApplicationInjector {
    private static final String[] IGNORE_PACKAGE_NAMES = new String[]{PushConstants.PUSH_SERVICE_PACKAGE_NAME, MiProfile.PACKAGE_SCOPE_CLOUDSERVICE, "com.miui.networkassistant", "com.miui.yellowpage", AntiSpam.ANTISPAM_PKG, "com.xiaomi.simactivate.service", "com.android.mms", "com.xiaomi.finddevice"};

    static boolean isIgnoreSmsStorageApplication(String packageName) {
        return ArrayUtils.contains(IGNORE_PACKAGE_NAMES, (Object) packageName);
    }
}
