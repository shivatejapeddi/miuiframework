package miui.util;

import android.content.Context;
import android.os.SystemProperties;
import android.provider.MiuiSettings.System;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.view.WindowManager;
import miui.os.Build;

public class HandyModeUtils {
    public static final boolean DEFAULT_IS_ENTER_DIRECT = false;
    static boolean SUPPORTED = SystemProperties.getBoolean("ro.miui.has_handy_mode_sf", false);
    private static volatile HandyModeUtils sInstance;
    private Context mContext;
    private float mScreenSize;

    public static HandyModeUtils getInstance(Context context) {
        if (sInstance == null) {
            synchronized (HandyModeUtils.class) {
                if (sInstance == null) {
                    sInstance = new HandyModeUtils(context);
                }
            }
        }
        return sInstance;
    }

    private HandyModeUtils(Context context) {
        this.mContext = context.getApplicationContext();
        if (this.mContext == null) {
            this.mContext = context;
        }
        this.mScreenSize = getScreenSize();
    }

    public static boolean isFeatureVisible() {
        return SUPPORTED && !Build.IS_TABLET;
    }

    private float getScreenSize() {
        WindowManager w = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        w.getDefaultDisplay().getMetrics(metrics);
        float widthInch = ((float) metrics.widthPixels) / metrics.xdpi;
        float heightInch = ((float) metrics.heightPixels) / metrics.ydpi;
        return FloatMath.sqrt((widthInch * widthInch) + (heightInch * heightInch));
    }

    private float calcScreenSizeToScale(float size) {
        return size / this.mScreenSize;
    }

    private float getDefaultScreenSize() {
        return getScreenSize() > 4.5f ? 4.0f : 3.5f;
    }

    public boolean isEnable() {
        return isFeatureVisible() && isHandyModeEnabled();
    }

    public void setHandyModeStateToSettings(boolean enabled) {
        System.putBooleanForUser(this.mContext.getContentResolver(), System.HANDY_MODE_STATE, enabled, 0);
    }

    public boolean isHandyModeEnabled() {
        return Settings.System.getIntForUser(this.mContext.getContentResolver(), System.HANDY_MODE_STATE, 0, 0) != 0;
    }

    public boolean isEnterDirect() {
        return true;
    }

    public void setEnterDirect(boolean value) {
        System.putBoolean(this.mContext.getContentResolver(), System.HANDY_MODE_ENTER_DIRECT, value);
    }

    public boolean hasShowed() {
        return true;
    }

    public void setSize(float size) {
        Settings.System.putFloatForUser(this.mContext.getContentResolver(), System.HANDY_MODE_SIZE, size, 0);
    }

    public float getSize() {
        float defaultScreenSize = getDefaultScreenSize();
        float size = Settings.System.getFloatForUser(this.mContext.getContentResolver(), System.HANDY_MODE_SIZE, defaultScreenSize, 0);
        if (isValidSize(size)) {
            return size;
        }
        return defaultScreenSize;
    }

    public float getScale() {
        return calcScreenSizeToScale(getSize());
    }

    public boolean isValidSize(float size) {
        return calcScreenSizeToScale(size) < 0.88f;
    }
}
