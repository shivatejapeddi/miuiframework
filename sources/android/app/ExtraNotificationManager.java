package android.app;

import android.app.NotificationManager.Policy;
import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.provider.MiuiSettings.AntiSpam;
import android.provider.MiuiSettings.SilenceMode;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.service.notification.Condition;
import android.service.notification.ZenModeConfig;
import android.util.Log;
import miui.securityspace.XSpaceUserHandle;

public class ExtraNotificationManager {
    public static int getZenMode(Context context) {
        return NotificationManager.from(context).getZenMode();
    }

    public static void setZenMode(Context context, int mode) {
        NotificationManager.from(context).setZenMode(mode, null, null);
    }

    public static void setZenMode(Context context, int mode, Uri conditionId) {
        if (SilenceMode.isSupported) {
            boolean isVibrateInSilent = System.getIntForUser(context.getContentResolver(), "vibrate_in_silent", 1, -2) == 0;
            AudioManager manager = (AudioManager) context.getSystemService("audio");
            if (manager.getLastAudibleStreamVolume(2) == 0 && manager.getLastAudibleStreamVolume(4) == 0 && manager.getLastAudibleStreamVolume(3) == 0 && mode == 1 && isVibrateInSilent) {
                mode = 2;
            }
            if (conditionId == null) {
                conditionId = new Condition(Condition.newId(context).appendPath("forever").build(), "", "", "", 0, 1, 0).id;
            }
            if (context.getPackageName().contains("settings")) {
                SilenceMode.reportRingerModeInfo(SilenceMode.MISTAT_SILENCE_DND, SilenceMode.MISTAT_RINGERMODE_LIST[mode], "0", System.currentTimeMillis());
            }
            NotificationManager.from(context).setZenMode(mode, conditionId, "miui_manual");
            return;
        }
        NotificationManager.from(context).setZenMode(mode, null, null);
    }

    public static ZenModeConfig getZenModeConfig(Context context) {
        return NotificationManager.from(context).getZenModeConfig();
    }

    public static boolean setZenModeConfig(Context context, ZenModeConfig config) {
        Policy policy = NotificationManager.from(context).getNotificationPolicy();
        int priorityCategories = policy.priorityCategories;
        if (config.allowEvents) {
            priorityCategories |= 2;
        } else {
            priorityCategories &= -3;
        }
        if (config.allowCalls) {
            priorityCategories |= 8;
        } else {
            priorityCategories &= -9;
        }
        if (config.allowMedia) {
            priorityCategories |= 64;
        } else {
            priorityCategories &= -65;
        }
        if (config.allowSystem) {
            priorityCategories |= 128;
        } else {
            priorityCategories &= -129;
        }
        if (config.allowAlarms) {
            priorityCategories |= 32;
        } else {
            priorityCategories &= -33;
        }
        if (config.allowMessages) {
            priorityCategories |= 4;
        } else {
            priorityCategories &= -5;
        }
        if (config.allowRepeatCallers) {
            priorityCategories |= 16;
        } else {
            priorityCategories &= -17;
        }
        NotificationManager.from(context).setNotificationPolicy(new Policy(priorityCategories, config.allowCallsFrom, config.allowMessagesFrom, policy.suppressedVisualEffects));
        return true;
    }

    public static void setQuietMode(Context context, boolean enable, int userId) {
        int preMode = getZenMode(context);
        String str = AntiSpam.QUIET_MODE_ENABLE;
        if (enable) {
            if (preMode == 0) {
                setZenMode(context, 1, null);
            }
            Secure.putIntForUser(context.getContentResolver(), str, 1, userId);
            Secure.putIntForUser(context.getContentResolver(), str, 1, 999);
            return;
        }
        if (preMode == 1) {
            setZenMode(context, 0, null);
        }
        Secure.putIntForUser(context.getContentResolver(), str, 0, userId);
        Secure.putIntForUser(context.getContentResolver(), str, 0, 999);
    }

    public static boolean isQuietModeEnable(Context context, int userId) {
        boolean z = false;
        if (SilenceMode.isSupported) {
            return false;
        }
        if (Secure.getIntForUser(context.getContentResolver(), AntiSpam.QUIET_MODE_ENABLE, 0, XSpaceUserHandle.isXSpaceUserCalling() ? 999 : userId) == 1) {
            z = true;
        }
        return z;
    }

    public static boolean isRepeatedCallEnable(Context context) {
        return getZenModeConfig(context).allowRepeatCallers;
    }

    public static void setRepeatedCallEnable(Context context, boolean enable) {
        ZenModeConfig newConfig = getZenModeConfig(context).copy();
        newConfig.allowRepeatCallers = enable;
        setZenModeConfig(context, newConfig);
    }

    public static void enableVIPMode(Context context, boolean enable, int userId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("enableVIPMode:");
        stringBuilder.append(enable);
        Log.i("SilenceMode", stringBuilder.toString());
        ZenModeConfig config = getZenModeConfig(context);
        int mode = getZenMode(context);
        config.allowCalls = enable;
        config.allowMessages = enable;
        config.allowEvents = false;
        config.allowMedia = true;
        config.allowSystem = false;
        config.allowAlarms = true;
        setZenModeConfig(context, config);
    }

    public static void setSilenceMode(Context context, int mode, Uri conditionId) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("setSilenceMode mode:");
        stringBuilder.append(mode);
        Log.i("SilenceMode", stringBuilder.toString());
        setZenMode(context, mode, conditionId);
    }

    public static boolean isVIPModeEnable(Context context) {
        return getZenModeConfig(context).allowCalls;
    }

    public static boolean isSilenceModeEnable(Context context, int userId) {
        if (!XSpaceUserHandle.isXSpaceUserCalling()) {
            int userHandle = userId;
        }
        return getZenMode(context) != 0;
    }

    public static Uri getConditionId(Context context) {
        ZenModeConfig config = getZenModeConfig(context);
        return config.manualRule == null ? null : config.manualRule.conditionId;
    }

    public static long getRemainTime(Context context) {
        long countDownTime = ZenModeConfig.tryParseCountdownConditionId(getConditionId(context));
        return countDownTime == 0 ? 0 : countDownTime - System.currentTimeMillis();
    }

    public static void startCountDownSilenceMode(Context context, int mode, int time) {
        if (time == 0) {
            setZenMode(context, mode, null);
        } else {
            setZenMode(context, mode, ZenModeConfig.toTimeCondition(context, time, ActivityManager.getCurrentUser()).id);
        }
    }

    public static void updateRestriction(Context context) {
        if (SilenceMode.isSupported) {
            boolean allowNotification;
            boolean allowRingtone;
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            String[] exceptionPackages = "com.android.cellbroadcastreceiver";
            String[] defaultException = new String[]{exceptionPackages};
            exceptionPackages = new String[]{"com.android.systemui", "android", exceptionPackages, "com.android.server.telecom"};
            int mode = getZenMode(context);
            ZenModeConfig config = getZenModeConfig(context);
            boolean hasException = false;
            boolean z = true;
            if (mode != 1) {
                allowNotification = true;
                allowRingtone = true;
            } else {
                allowRingtone = false;
                allowNotification = false;
                if (!(config.allowCalls || config.allowRepeatCallers)) {
                    z = false;
                }
                hasException = z;
            }
            applyRestriction(allowRingtone, 6, appOps, hasException ? exceptionPackages : defaultException);
            applyRestriction(allowNotification, 5, appOps, hasException ? exceptionPackages : defaultException);
        }
    }

    private static void applyRestriction(boolean allow, int usage, AppOpsManager appOps, String[] exception) {
        appOps.setRestriction(28, usage, allow ^ 1, exception);
        appOps.setRestriction(3, usage, allow ^ 1, exception);
    }
}
