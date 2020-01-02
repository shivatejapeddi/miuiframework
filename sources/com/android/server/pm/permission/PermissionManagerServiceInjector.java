package com.android.server.pm.permission;

import android.miui.AppOpsUtils;
import miui.os.Build;

public class PermissionManagerServiceInjector {
    public static boolean isPermissionReviewDisabled() {
        return (Build.IS_INTERNATIONAL_BUILD || AppOpsUtils.isXOptMode()) ? false : true;
    }
}
