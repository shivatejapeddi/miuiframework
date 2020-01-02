package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.pm.IPackageManager;
import miui.os.DeviceFeature;
import miui.slide.AppSlideConfig;

public class AppGlobals {
    public static AppSlideConfig mAppSlideConfig;

    @UnsupportedAppUsage
    public static Application getInitialApplication() {
        return ActivityThread.currentApplication();
    }

    @UnsupportedAppUsage
    public static String getInitialPackage() {
        return ActivityThread.currentPackageName();
    }

    @UnsupportedAppUsage
    public static IPackageManager getPackageManager() {
        return ActivityThread.getPackageManager();
    }

    public static int getIntCoreSetting(String key, int defaultValue) {
        ActivityThread currentActivityThread = ActivityThread.currentActivityThread();
        if (currentActivityThread != null) {
            return currentActivityThread.getIntCoreSetting(key, defaultValue);
        }
        return defaultValue;
    }

    static {
        mAppSlideConfig = null;
        if (DeviceFeature.hasMirihiSupport()) {
            mAppSlideConfig = new AppSlideConfig();
        }
    }
}
