package android.provider;

import miui.os.Build;

public final class SettingsInjector {
    public static final String ELDER_RINGTONE_ALT = "elder-ringtone";
    public static final String RINGTONE = "ringtone";

    public static String elderAltSettingName(String name) {
        if (Build.getUserMode() == 1 && "ringtone".equals(name)) {
            return ELDER_RINGTONE_ALT;
        }
        return name;
    }
}
