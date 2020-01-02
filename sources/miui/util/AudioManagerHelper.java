package miui.util;

import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;
import android.provider.MiuiSettings.SilenceMode;
import android.provider.MiuiSettings.System;
import android.provider.Settings;

public class AudioManagerHelper {
    public static final int FLAG_ONLY_SET_VOLUME = 1048576;
    public static final int FLAG_SHOW_UI_WARNINGS = 1024;

    public static void toggleVibrateSetting(Context context) {
        toggleVibrateSettingForUser(context, -3);
    }

    public static void toggleVibrateSettingForUser(Context context, int userHandle) {
        setVibrateSettingForUser(context, isVibrateEnabledForUser(context, userHandle) ^ 1, isSilentEnabled(context), userHandle);
    }

    public static void setVibrateSetting(Context context, boolean enable, boolean forSilient) {
        setVibrateSettingForUser(context, enable, forSilient, -3);
    }

    public static void setVibrateSettingForUser(Context context, boolean enable, boolean forSilient, int userHandle) {
        String str;
        ContentResolver contentResolver = context.getContentResolver();
        if (forSilient) {
            str = "vibrate_in_silent";
        } else {
            str = System.VIBRATE_IN_NORMAL;
        }
        Settings.System.putIntForUser(contentResolver, str, enable, userHandle);
        if (SilenceMode.isSupported) {
            validateRingerMode(context, enable, forSilient);
        } else {
            validateRingerMode(context, userHandle);
        }
    }

    public static boolean isSilentEnabled(Context context) {
        if (SilenceMode.isSupported) {
            return isNewSilentEnabled(context);
        }
        return ((AudioManager) context.getSystemService("audio")).getRingerMode() != 2;
    }

    public static boolean isNewSilentEnabled(Context context) {
        return SilenceMode.getZenMode(context) != 0;
    }

    public static boolean isVibrateEnabled(Context context) {
        if (SilenceMode.isSupported) {
            return isNewVibrateEnabled(context);
        }
        return isVibrateEnabled(context, ((AudioManager) context.getSystemService("audio")).getRingerMode());
    }

    public static boolean isNewVibrateEnabled(Context context) {
        return isVibrateEnabled(context, SilenceMode.getZenMode(context));
    }

    public static boolean isVibrateEnabled(Context context, int mode) {
        return isVibrateEnabledForUser(context, mode, -3);
    }

    public static boolean isVibrateEnabledForUser(Context context, int userHandle) {
        if (SilenceMode.isSupported) {
            return isNewVibrateEnabledForUser(context, userHandle);
        }
        return isVibrateEnabledForUser(context, ((AudioManager) context.getSystemService("audio")).getRingerMode(), userHandle);
    }

    public static boolean isNewVibrateEnabledForUser(Context context, int userHandle) {
        return isVibrateEnabledForUser(context, SilenceMode.getZenMode(context), userHandle);
    }

    public static boolean isVibrateEnabledForUser(Context context, int mode, int userHandle) {
        boolean hasVibrator = ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).hasVibrator();
        boolean z = false;
        if ((SilenceMode.isSupported ? 0 : 2) != mode) {
            if (hasVibrator && Settings.System.getIntForUser(context.getContentResolver(), "vibrate_in_silent", 1, userHandle) == 1) {
                z = true;
            }
            return z;
        }
        if (hasVibrator) {
            if (Settings.System.getIntForUser(context.getContentResolver(), System.VIBRATE_IN_NORMAL, System.VIBRATE_IN_NORMAL_DEFAULT, userHandle) == 1) {
                z = true;
            }
        }
        return z;
    }

    private static void validateRingerMode(Context context, int userHandle) {
        AudioManager am = (AudioManager) context.getSystemService("audio");
        int mode = am.getRingerMode();
        int newMode = getValidatedRingerModeForUser(context, mode, userHandle);
        if (mode != newMode) {
            am.setRingerMode(newMode);
        }
    }

    private static void validateRingerMode(Context context, boolean shouldVibrate, boolean forSilent) {
        if (forSilent && SilenceMode.getZenMode(context) == 4) {
            ((AudioManager) context.getSystemService("audio")).setRingerMode(shouldVibrate);
        }
    }

    public static int getValidatedRingerMode(Context context, int mode) {
        return getValidatedRingerModeForUser(context, mode, -3);
    }

    public static int getValidatedRingerModeForUser(Context context, int mode, int userHandle) {
        if (SilenceMode.isSupported) {
            return getNewValidatedRingerModeForUser(context, mode, userHandle);
        }
        boolean vibrate = isVibrateEnabledForUser(context, mode, userHandle);
        if (mode == 0) {
            if (vibrate) {
                return 1;
            }
        } else if (1 == mode && !vibrate) {
            return 0;
        }
        return mode;
    }

    public static int getNewValidatedRingerModeForUser(Context context, int mode, int userHandle) {
        if (mode == 1) {
            return 0;
        }
        return mode;
    }

    public static void toggleSilent(Context context, int flag) {
        toggleSilentForUser(context, flag, -3);
    }

    public static void toggleSilentForUser(Context context, int flag, int userHandle) {
        if (SilenceMode.isSupported) {
            newToggleSilentForUser(context, flag, userHandle);
            return;
        }
        int newMode;
        AudioManager am = (AudioManager) context.getSystemService("audio");
        if (2 != am.getRingerMode()) {
            newMode = 2;
        } else if (isVibrateEnabledForUser(context, 0, userHandle)) {
            newMode = 1;
        } else {
            newMode = 0;
        }
        am.setRingerMode(newMode);
        if (flag != 0) {
            am.adjustStreamVolume(2, 0, flag);
        }
    }

    public static void newToggleSilentForUser(Context context, int flag, int userHandle) {
        SilenceMode.setSilenceMode(context, isSilentEnabled(context) ? 0 : SilenceMode.getLastestQuietMode(context), null);
    }

    public static boolean isHiFiMode(Context context) {
        AudioManager am = (AudioManager) context.getSystemService("audio");
        return am.isWiredHeadsetOn() && am.getParameters("hifi_mode").contains("true");
    }

    public static int getHiFiVolume(Context context) {
        try {
            return Integer.valueOf(((AudioManager) context.getSystemService("audio")).getParameters("hifi_volume").replace("hifi_volume=", "")).intValue();
        } catch (Exception e) {
            return 0;
        }
    }

    public static void setHiFiVolume(Context context, int hifiVolume) {
        AudioManager am = (AudioManager) context.getSystemService("audio");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("hifi_volume=");
        stringBuilder.append(hifiVolume);
        am.setParameters(stringBuilder.toString());
    }
}
