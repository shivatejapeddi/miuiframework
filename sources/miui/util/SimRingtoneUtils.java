package miui.util;

import android.content.Context;
import android.media.ExtraRingtoneManager;
import android.net.Uri;
import android.provider.MiuiSettings;
import android.provider.Settings.System;
import miui.telephony.SubscriptionManager;
import miui.telephony.TelephonyManager;

public class SimRingtoneUtils {
    public static Uri getDefaultRingtoneUri(Context context, int simSlotId) {
        return getDefaultSoundUriBySlot(context, 1, simSlotId);
    }

    public static Uri getDefaultSmsReceivedUri(Context context, int simSlotId) {
        return getDefaultSoundUriBySlot(context, 16, simSlotId);
    }

    public static Uri getDefaultSmsDeliveredUri(Context context, int simSlotId) {
        return getDefaultSoundUriBySlot(context, 8, simSlotId);
    }

    private static Uri getDefaultSoundUriBySlot(Context context, int type, int simSlotId) {
        if (!(simSlotId == SubscriptionManager.INVALID_SLOT_ID || isDefaultSoundUniform(context, type))) {
            type = getExtraRingtoneTypeBySlot(type, simSlotId);
        }
        return ExtraRingtoneManager.getDefaultSoundActualUri(context, type);
    }

    public static Uri getDefaultSoundUri(Context context, int extraRingtoneType) {
        if (extraRingtoneType == 1) {
            return System.DEFAULT_RINGTONE_URI;
        }
        if (extraRingtoneType == 2) {
            return System.DEFAULT_NOTIFICATION_URI;
        }
        if (extraRingtoneType == 4) {
            return System.DEFAULT_ALARM_ALERT_URI;
        }
        if (extraRingtoneType == 8) {
            return MiuiSettings.System.DEFAULT_SMS_DELIVERED_RINGTONE_URI;
        }
        if (extraRingtoneType == 16) {
            return MiuiSettings.System.DEFAULT_SMS_RECEIVED_RINGTONE_URI;
        }
        if (extraRingtoneType == 32) {
            return null;
        }
        if (extraRingtoneType == 64) {
            return MiuiSettings.System.DEFAULT_RINGTONE_URI_SLOT_1;
        }
        if (extraRingtoneType == 128) {
            return MiuiSettings.System.DEFAULT_RINGTONE_URI_SLOT_2;
        }
        if (extraRingtoneType == 256) {
            return MiuiSettings.System.DEFAULT_SMS_DELIVERED_SOUND_URI_SLOT_1;
        }
        if (extraRingtoneType == 512) {
            return MiuiSettings.System.DEFAULT_SMS_DELIVERED_SOUND_URI_SLOT_2;
        }
        if (extraRingtoneType == 1024) {
            return MiuiSettings.System.DEFAULT_SMS_RECEIVED_SOUND_URI_SLOT_1;
        }
        if (extraRingtoneType != 2048) {
            return null;
        }
        return MiuiSettings.System.DEFAULT_SMS_RECEIVED_SOUND_URI_SLOT_2;
    }

    public static int getExtraRingtoneTypeBySlot(int ringtoneType, int simSlotId) {
        if (simSlotId < 0 || simSlotId >= TelephonyManager.getDefault().getPhoneCount()) {
            return ringtoneType;
        }
        int i;
        if (ringtoneType == 1) {
            if (simSlotId == 0) {
                i = 64;
            } else {
                i = 128;
            }
            return i;
        } else if (ringtoneType == 8) {
            if (simSlotId == 0) {
                i = 256;
            } else {
                i = 512;
            }
            return i;
        } else if (ringtoneType != 16) {
            return ringtoneType;
        } else {
            if (simSlotId == 0) {
                i = 1024;
            } else {
                i = 2048;
            }
            return i;
        }
    }

    public static boolean isDefaultSoundUniform(Context context, int ringtoneType) {
        boolean z = true;
        if (!canSlotSettingRingtoneType(ringtoneType)) {
            return true;
        }
        if (System.getInt(context.getContentResolver(), getSoundUniformSettingName(ringtoneType), 1) != 1) {
            z = false;
        }
        return z;
    }

    public static void setDefaultSoundUniform(Context context, int ringtoneType, boolean useUniform) {
        if (canSlotSettingRingtoneType(ringtoneType)) {
            System.putInt(context.getContentResolver(), getSoundUniformSettingName(ringtoneType), useUniform);
        }
    }

    private static boolean canSlotSettingRingtoneType(int ringtoneType) {
        return ringtoneType == 1 || ringtoneType == 8 || ringtoneType == 16;
    }

    private static String getSoundUniformSettingName(int ringtoneType) {
        if (ringtoneType == 1) {
            return MiuiSettings.System.RINGTONE_SOUND_USE_UNIFORM;
        }
        if (ringtoneType == 8) {
            return MiuiSettings.System.SMS_DELIVERED_SOUND_USE_UNIFORM;
        }
        if (ringtoneType == 16) {
            return MiuiSettings.System.SMS_RECEIVED_SOUND_USE_UNIFORM;
        }
        return null;
    }
}
