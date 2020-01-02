package android.app;

import android.app.backup.FullBackup;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.MiuiConfiguration;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.ExtraGLES20Canvas;
import java.io.File;
import miui.content.res.IconCustomizer;
import miui.content.res.ThemeFontChangeHelper;
import miui.os.Build;
import miui.os.SystemProperties;
import org.apache.miui.commons.lang3.ClassUtils;

public class MiuiThemeHelper {
    private static final String ICON_THEME = "/data/system/theme/icons";
    public static final String MIUI_RES_PATH = "/system/framework/framework-miui-res.apk";
    private static final String MIUI_SCREENSHOT_MODE_RES_PATH = "/data/system/themeScreenshotMode";

    private MiuiThemeHelper() {
    }

    public static boolean isInternationalBuildWithDefaultTheme() {
        return Build.IS_INTERNATIONAL_BUILD && !new File(ICON_THEME).exists();
    }

    public static Drawable getDrawable(PackageManager pm, String packageName, String packageItemName, int resid, ApplicationInfo appInfo) {
        boolean isPackageIcon = true;
        Drawable className = null;
        if ((VERSION.SDK_INT > 28 && !SystemProperties.getBoolean("persist.sys.miui_optimization", true)) || TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            String className2;
            ApplicationPackageManager apm = (ApplicationPackageManager) pm;
            if (VERSION.SDK_INT <= 24) {
                if (packageItemName != null) {
                    isPackageIcon = false;
                }
            } else if (packageItemName != null && (appInfo == null || !packageItemName.equals(appInfo.name))) {
                isPackageIcon = false;
            }
            if (!isPackageIcon) {
                className2 = packageItemName;
            }
            Drawable dr = IconCustomizer.getCustomizedIconFromCache(packageName, className2);
            if (dr == null && (pm instanceof ApplicationPackageManager)) {
                dr = IconCustomizer.getCustomizedIcon(((ApplicationPackageManager) pm).getContext(), packageName, className2, resid, appInfo, isPackageIcon);
            }
            return dr;
        } catch (ClassCastException e) {
            if (resid != 0) {
                className = pm.getDrawable(packageName, resid, appInfo);
            }
            return className;
        }
    }

    public static void addExtraAssetPaths(AssetManager am) {
        am.addAssetPath(MIUI_RES_PATH);
    }

    public static void copyExtraConfigurations(Configuration srcConfig, Configuration desConfig) {
        desConfig.extraConfig.themeChanged = srcConfig.extraConfig.themeChanged;
    }

    public static void handleExtraConfigurationChanges(int changes, Configuration newConfig) {
        if ((Integer.MIN_VALUE & changes) != 0) {
            long flag = newConfig.extraConfig.themeChangedFlags;
            Canvas.freeCaches();
            if ((16 & flag) != 0) {
                ThemeFontChangeHelper.quitProcessIfNeed(newConfig);
                Canvas.freeTextLayoutCaches();
                ExtraGLES20Canvas.freeCaches();
            }
            if ((8 & flag) != 0) {
                IconCustomizer.clearCache();
            }
        }
    }

    public static void handleExtraConfigurationChangesForSystem(int changes, Configuration newConfig) {
        if ((Integer.MIN_VALUE & changes) != 0) {
            handleExtraConfigurationChanges(changes, newConfig);
        }
    }

    public static boolean canKeepActivityAlive(String packageName, int changes, Configuration oldConfig, Configuration newConfig) {
        if (changes == Integer.MIN_VALUE) {
            MiuiConfiguration oldThemeConfig = oldConfig.extraConfig;
            MiuiConfiguration newThemeConfig = newConfig.extraConfig;
            if (newThemeConfig.themeChanged - oldThemeConfig.themeChanged == 1) {
                return MiuiConfiguration.needRestartActivity(packageName, newThemeConfig.themeChangedFlags) ^ 1;
            }
        }
        return false;
    }

    public static Integer parseDimension(String value) {
        String str = value;
        int intPos = -4;
        int dotPos = -3;
        int fractionPos = -2;
        int unitPos = -1;
        for (int i = 0; i < value.length(); i++) {
            char c = str.charAt(i);
            if (intPos == -4 && c >= '0' && c <= '9') {
                intPos = i;
            }
            if (dotPos == -3 && c == ClassUtils.PACKAGE_SEPARATOR_CHAR) {
                dotPos = i;
            }
            if (dotPos != -3 && c >= '0' && c <= '9') {
                fractionPos = i;
            }
            if (-1 == -1 && c >= DateFormat.AM_PM && c <= DateFormat.TIME_ZONE) {
                unitPos = i;
                i = intPos;
                break;
            }
        }
        int fraction = 0;
        int mantissaShift = 0;
        if (unitPos == -1 || dotPos >= fractionPos || fractionPos >= unitPos) {
            return null;
        }
        try {
            int fracStart;
            float f = Float.parseFloat(str.substring(0, unitPos));
            if (!(dotPos == -3 || fractionPos == -2)) {
                fracStart = dotPos + 1;
                try {
                    StringBuilder tmp = new StringBuilder(str.substring(fracStart, Math.min(4, fractionPos - dotPos) + fracStart));
                    while (tmp.length() < 4) {
                        tmp.append('0');
                    }
                    fraction = Integer.parseInt(tmp.toString());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            if (fraction < 256) {
                f *= 256.0f;
            } else if (fraction < 32768) {
                f *= 32768.0f;
                mantissaShift = 1;
            } else if (fraction < 8388608) {
                f *= 8388608.0f;
                mantissaShift = 2;
            } else if (((long) fraction) < 2147483648L) {
                f *= 2.14748365E9f;
                mantissaShift = 3;
            }
            String unit = str.substring(unitPos);
            if (unit.equals("px")) {
                fracStart = 0;
            } else if (unit.equals("dp") || unit.equals("dip")) {
                fracStart = 1;
            } else if (unit.equals(FullBackup.SHAREDPREFS_TREE_TOKEN)) {
                fracStart = 2;
            } else if (unit.equals("pt")) {
                fracStart = 3;
            } else if (unit.equals("in")) {
                fracStart = 4;
            } else if (!unit.equals("mm")) {
                return null;
            } else {
                fracStart = 5;
            }
            return Integer.valueOf(Integer.valueOf(Integer.valueOf(Integer.valueOf((int) f).intValue() & -256).intValue() | (mantissaShift << 4)).intValue() | fracStart);
        } catch (NumberFormatException e2) {
            return null;
        }
    }

    public static boolean isScreenshotMode() {
        return new File(MIUI_SCREENSHOT_MODE_RES_PATH).exists();
    }
}
