package miui.os;

import android.content.res.Resources;
import android.miui.R;
import android.os.Build.VERSION;
import android.os.SystemProperties;
import miui.util.FeatureParser;

public class DeviceFeature {
    public static final int BACKLIGHT_BIT = SYSTEM_RESOURCES.getInteger(R.integer.config_backlightBit);
    public static final boolean PERSIST_SCREEN_EFFECT = SystemProperties.getBoolean("sys.persist_screen_effect", false);
    public static final boolean SCREEN_EFFECT_CONFLICT;
    public static final boolean SUPPORT_3D_GESTURE;
    public static final boolean SUPPORT_AUTO_BRIGHTNESS_OPTIMIZE;
    public static final boolean SUPPORT_CAMERA_ANIMATION;
    public static final boolean SUPPORT_DISPLAYFEATURE_CALLBACK = SYSTEM_RESOURCES.getBoolean(R.bool.config_displayFeatureCallbackSupported);
    public static final boolean SUPPORT_DISPLAYFEATURE_HIDL = SystemProperties.getBoolean("sys.displayfeature_hidl", false);
    public static final boolean SUPPORT_GAME_MODE = FeatureParser.getBoolean("support_touchfeature_gamemode", false);
    public static final boolean SUPPORT_LAB_GESTURE;
    public static final boolean SUPPORT_NIGHT_LIGHT = SYSTEM_RESOURCES.getBoolean(R.bool.config_nightLightSupported);
    public static final boolean SUPPORT_NIGHT_LIGHT_ADJ = SYSTEM_RESOURCES.getBoolean(R.bool.config_nightLightAdjSupported);
    public static final boolean SUPPORT_PAPERMODE_ANIMATION = FeatureParser.getBoolean("support_papermode_animation", false);
    public static final Resources SYSTEM_RESOURCES = Resources.getSystem();

    static {
        boolean z = false;
        boolean z2 = (FeatureParser.getBoolean("support_autobrightness_optimize", false) && VERSION.SDK_INT > 23) || SystemProperties.getBoolean("sys.autobrightness_optimize", false);
        SUPPORT_AUTO_BRIGHTNESS_OPTIMIZE = z2;
        z2 = SystemProperties.getInt("ro.df.effect.conflict", 0) == 1 || SystemProperties.getInt("ro.vendor.df.effect.conflict", 0) == 1;
        SCREEN_EFFECT_CONFLICT = z2;
        z2 = "sagit".equals(Build.DEVICE) && !Build.IS_STABLE_VERSION;
        SUPPORT_LAB_GESTURE = z2;
        if (!"pyxis".equals(Build.DEVICE)) {
            if (!"vela".equals(Build.DEVICE)) {
                z2 = false;
                SUPPORT_CAMERA_ANIMATION = z2;
                if ("cepheus".equals(Build.DEVICE) && !Build.IS_INTERNATIONAL_BUILD) {
                    z = true;
                }
                SUPPORT_3D_GESTURE = z;
            }
        }
        z2 = true;
        SUPPORT_CAMERA_ANIMATION = z2;
        z = true;
        SUPPORT_3D_GESTURE = z;
    }

    public static final boolean hasMirihiSupport() {
        if (!"perseus".equals(Build.DEVICE)) {
            if (!"andromeda".equals(Build.DEVICE)) {
                return false;
            }
        }
        return true;
    }

    public static final boolean hasPopupCameraSupport() {
        if (!"raphael".equals(Build.DEVICE)) {
            if (!"davinci".equals(Build.DEVICE)) {
                if (!"raphaelin".equals(Build.DEVICE)) {
                    if (!"davinciin".equals(Build.DEVICE)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static final boolean hasFlipSupport() {
        return "draco".equals(Build.DEVICE);
    }

    public static final boolean hasSupportAudioPromity() {
        if (SystemProperties.getBoolean("ro.vendor.audio.us.proximity", false) || SystemProperties.getBoolean("ro.audio.us.proximity", false)) {
            return true;
        }
        return false;
    }
}
