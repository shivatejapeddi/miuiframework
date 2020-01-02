package android.preference;

import android.content.Intent;

class RingtonePreferenceInjector {
    RingtonePreferenceInjector() {
    }

    static void specifyRingtonePickIntentActivity(Intent ringtonePickerIntent) {
        ringtonePickerIntent.setClassName("com.android.thememanager", "com.android.thememanager.activity.ThemeTabActivity");
    }
}
