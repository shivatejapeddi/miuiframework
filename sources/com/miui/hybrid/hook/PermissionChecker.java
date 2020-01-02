package com.miui.hybrid.hook;

import android.content.Context;

public class PermissionChecker {
    private static final String PERMISSION = "com.miui.hybrid.hook.WRITE_PERMISSION";

    public static boolean check(Context context) {
        return context.checkCallingPermission(PERMISSION) == 0;
    }
}
