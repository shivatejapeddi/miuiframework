package miui.provider;

import android.content.Context;
import android.database.sqlite.SQLiteStatement;
import android.media.ExtraRingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MiuiSettings;
import android.provider.Settings.System;
import android.text.TextUtils;
import java.io.File;
import miui.system.R;

public class SettingProviderHelper {
    public static void loadDefaultRingtoneSettings(SQLiteStatement stmt, Context context) {
        String DEFAULT_RINGTONE = "ringtone_default";
        loadRingtoneSetting(stmt, context, "ringtone_default", R.string.def_ringtone);
        loadRingtoneSetting(stmt, context, "ringtone", R.string.def_ringtone);
        loadRingtoneSetting(stmt, context, System.ALARM_ALERT, R.string.def_alarm_alert);
        loadRingtoneSetting(stmt, context, System.NOTIFICATION_SOUND, R.string.def_notification_sound);
        loadRingtoneSetting(stmt, context, MiuiSettings.System.CALENDAR_ALERT, R.string.def_notification_sound);
        loadRingtoneSetting(stmt, context, MiuiSettings.System.SMS_DELIVERED_SOUND, R.string.def_sms_delivered_sound);
        loadRingtoneSetting(stmt, context, MiuiSettings.System.SMS_RECEIVED_SOUND, R.string.def_sms_received_sound);
        loadRingtoneSetting(stmt, context, MiuiSettings.System.RINGTONE_SOUND_SLOT_1, R.string.def_ringtone_slot_1);
        loadRingtoneSetting(stmt, context, MiuiSettings.System.RINGTONE_SOUND_SLOT_2, R.string.def_ringtone_slot_2);
        loadRingtoneSetting(stmt, context, MiuiSettings.System.SMS_DELIVERED_SOUND_SLOT_1, R.string.def_sms_delivered_sound_slot_1);
        loadRingtoneSetting(stmt, context, MiuiSettings.System.SMS_DELIVERED_SOUND_SLOT_2, R.string.def_sms_delivered_sound_slot_2);
        loadRingtoneSetting(stmt, context, MiuiSettings.System.SMS_RECEIVED_SOUND_SLOT_1, R.string.def_sms_received_sound_slot_1);
        loadRingtoneSetting(stmt, context, MiuiSettings.System.SMS_RECEIVED_SOUND_SLOT_2, R.string.def_sms_received_sound_slot_2);
    }

    private static void loadRingtoneSetting(SQLiteStatement stmt, Context context, String key, int resid) {
        String path = context.getString(resid);
        if (!TextUtils.isEmpty(path) && new File(path).exists()) {
            stmt.bindString(1, key);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("file://");
            stringBuilder.append(path);
            stmt.bindString(2, stringBuilder.toString());
            stmt.execute();
        }
    }

    public static String getRingtoneAuthority(Uri soundUri) {
        String authority = soundUri.getAuthority();
        if (authority == null) {
            authority = "";
        }
        String str = MediaStore.AUTHORITY;
        if (authority.equals(str) || ExtraRingtoneManager.isExtraCases(soundUri)) {
            return str;
        }
        return authority;
    }

    public static Uri getRingtoneUriForExtraCases(Uri soundUri, int ringtoneType) {
        if (MediaStore.AUTHORITY.equals(getRingtoneAuthority(soundUri))) {
            return ExtraRingtoneManager.getUriForExtraCases(soundUri, ringtoneType);
        }
        return soundUri;
    }
}
