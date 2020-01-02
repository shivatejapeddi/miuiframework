package com.android.internal.widget;

import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.os.UserManager;

public class LockPatternUtilsInjector {
    private LockPatternUtilsInjector() {
    }

    public static boolean isUserAllowed(UserManager userManager) {
        UserInfo userInfo = userManager.getUserInfo(UserHandle.myUserId());
        return false;
    }
}
