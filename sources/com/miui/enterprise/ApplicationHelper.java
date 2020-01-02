package com.miui.enterprise;

import android.content.Context;
import android.text.TextUtils;
import com.miui.enterprise.settings.EnterpriseSettings;
import com.miui.enterprise.settings.EnterpriseSettings.Application;
import java.util.List;

public class ApplicationHelper {
    private static final String ENTERPRISE_PACKAGE_PREFIX = "pkg_";

    private ApplicationHelper() {
    }

    public static String buildPackageSettingKey(String packageName) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ENTERPRISE_PACKAGE_PREFIX);
        stringBuilder.append(packageName.hashCode());
        return stringBuilder.toString();
    }

    public static boolean shouldKeeAlive(Context context, String pkgName) {
        boolean z = false;
        if (!EnterpriseSettings.isBootComplete() || !EnterpriseSettings.ENTERPRISE_ACTIVATED || TextUtils.isEmpty(pkgName)) {
            return false;
        }
        if ((getPackageFlag(context, pkgName) & 1) != 0) {
            z = true;
        }
        return z;
    }

    public static boolean shouldKeeAlive(Context context, String pkgName, int userId) {
        boolean z = false;
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED || TextUtils.isEmpty(pkgName)) {
            return false;
        }
        if ((getPackageFlag(context, pkgName, userId) & 1) != 0) {
            z = true;
        }
        return z;
    }

    public static boolean protectedFromDelete(Context context, String pkgName) {
        boolean z = false;
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED || TextUtils.isEmpty(pkgName)) {
            return false;
        }
        if ((getPackageFlag(context, pkgName) & 4) != 0) {
            z = true;
        }
        return z;
    }

    public static boolean protectedFromDelete(Context context, String pkgName, int userId) {
        boolean z = false;
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED || TextUtils.isEmpty(pkgName)) {
            return false;
        }
        if ((getPackageFlag(context, pkgName, userId) & 4) != 0) {
            z = true;
        }
        return z;
    }

    public static boolean shouldGrantPermission(Context context, String pkgName) {
        boolean z = false;
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED || TextUtils.isEmpty(pkgName)) {
            return false;
        }
        if ((getPackageFlag(context, pkgName) & 16) != 0) {
            z = true;
        }
        return z;
    }

    public static boolean shouldGrantPermission(Context context, String pkgName, int userId) {
        boolean z = false;
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED || TextUtils.isEmpty(pkgName)) {
            return false;
        }
        if ((getPackageFlag(context, pkgName, userId) & 16) != 0) {
            z = true;
        }
        return z;
    }

    public static boolean allowAutoStart(Context context, String pkgName) {
        boolean z = false;
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED || TextUtils.isEmpty(pkgName)) {
            return false;
        }
        if ((getPackageFlag(context, pkgName) & 8) != 0) {
            z = true;
        }
        return z;
    }

    public static boolean allowAutoStart(Context context, String pkgName, int userId) {
        boolean z = false;
        if (!EnterpriseSettings.ENTERPRISE_ACTIVATED || TextUtils.isEmpty(pkgName)) {
            return false;
        }
        if ((getPackageFlag(context, pkgName, userId) & 8) != 0) {
            z = true;
        }
        return z;
    }

    private static int getPackageFlag(Context context, String pkgName, int userId) {
        return EnterpriseSettings.getInt(context, buildPackageSettingKey(pkgName), 0, userId < 0 ? 0 : userId);
    }

    private static int getPackageFlag(Context context, String pkgName) {
        return EnterpriseSettings.getInt(context, buildPackageSettingKey(pkgName), 0, EnterpriseSettings.getUserId());
    }

    public static List<String> getTrustedAppStores(Context context, int userId) {
        return EnterpriseSettings.parseListSettings(EnterpriseSettings.getString(context, Application.TRUSTED_APP_STORES, userId));
    }

    public static List<String> getTrustedAppStores(Context context) {
        return EnterpriseSettings.parseListSettings(EnterpriseSettings.getString(context, Application.TRUSTED_APP_STORES, EnterpriseSettings.getUserId()));
    }

    public static boolean isTrustedAppStoresEnabled(Context context, int userId) {
        return EnterpriseSettings.getInt(context, Application.TRUSTED_APP_STORE_ENABLED, 0, userId) == 1;
    }

    public static boolean isTrustedAppStoresEnabled(Context context) {
        return EnterpriseSettings.getInt(context, Application.TRUSTED_APP_STORE_ENABLED, 0, EnterpriseSettings.getUserId()) == 1;
    }

    public static boolean checkEnterprisePackageRestriction(Context context, String pkg) {
        int restrictionMode = EnterpriseSettings.getInt(context, Application.APPLICATION_RESTRICTION_MODE, 0);
        if (restrictionMode == 0) {
            return false;
        }
        if (restrictionMode == 1) {
            return 1 ^ EnterpriseSettings.parseListSettings(EnterpriseSettings.getString(context, Application.APPLICATION_WHITE_LIST)).contains(pkg);
        }
        if (restrictionMode != 2) {
            return false;
        }
        return EnterpriseSettings.parseListSettings(EnterpriseSettings.getString(context, Application.APPLICATION_BLACK_LIST)).contains(pkg);
    }

    public static List<String> getXSpaceBlackApps(Context context) {
        return EnterpriseSettings.parseListSettings(EnterpriseSettings.getString(context, Application.APPLICATION_BLACK_XSAPCE));
    }
}
