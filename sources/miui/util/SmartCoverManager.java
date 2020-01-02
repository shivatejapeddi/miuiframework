package miui.util;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.provider.MiuiSettings;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import android.util.Slog;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SmartCoverManager {
    private static final String ACTION_SMART_COVER_GUIDE = "miui.intent.action.SMART_COVER_GUIDE";
    private static final String EXCEPTION = "exception";
    private static final boolean IS_D4;
    private static final int LID_CLOSE_SCREEN_OFF_TIMEOUT_VALUE = 15000;
    private static final String LID_SWITCH_OPEN = "lid switch open";
    private static final boolean MULTI = FeatureParser.getBoolean(SUPPORT_MULTIPLE_SMALL_WIN_COVER, false);
    private static final String POWER = "power";
    private static final String SETTINGS_PKG = "com.android.settings";
    private static final String SMART_COVER_GUIDE_ACTIVITY = "com.android.settings.MiuiSmartCoverGuideActivity";
    private static final String SMART_COVER_LID_OPEN = "mSmartCoverLidOpen=";
    private static final String SMART_COVER_MODE = "mSmartCoverMode=";
    private static final String SUPPORT_MULTIPLE_SMALL_WIN_COVER = "support_multiple_small_win_cover";
    private static final String TAG = "SmartCoverManager";
    private ContentResolver mContentResolver;
    private Context mContext;
    private int mCurrentUserId;
    private DisplayManager mDisplayManager;
    private boolean mNeedResetTimeout = false;
    private PowerManagerWrapper mPowerManagerWrapper;
    private boolean mSmartCoverLidOpen = true;
    private int mSmartCoverMode;

    private class PowerManagerWrapper {
        final String GO_TO_SLEEP = "goToSleep";
        final int GO_TO_SLEEP_FLAG_NO_DOZE = 1;
        final int GO_TO_SLEEP_REASON_LID_SWITCH = 3;
        final String WAKE_UP = "wakeUp";
        PowerManager mPowerManager;

        PowerManagerWrapper(PowerManager pm) {
            this.mPowerManager = pm;
        }

        /* Access modifiers changed, original: 0000 */
        public void goToSleep() {
            if (!callMethod("goToSleep", new Object[]{Long.valueOf(SystemClock.uptimeMillis()), Integer.valueOf(3), Integer.valueOf(1)}, Long.TYPE, Integer.TYPE, Integer.TYPE)) {
                this.mPowerManager.goToSleep(SystemClock.uptimeMillis());
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void wakeUp() {
            restoreScreenBrightnessByLid();
            if (!callMethod("wakeUp", new Object[]{Long.valueOf(SystemClock.uptimeMillis()), SmartCoverManager.LID_SWITCH_OPEN}, Long.TYPE, String.class)) {
                this.mPowerManager.wakeUp(SystemClock.uptimeMillis());
            }
        }

        private boolean callMethod(String name, Object[] params, Class<?>... paramsType) {
            Method method = null;
            try {
                method = this.mPowerManager.getClass().getMethod(name, paramsType);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (method == null) {
                return false;
            }
            method.setAccessible(true);
            try {
                method.invoke(this.mPowerManager, params);
                return true;
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
                return false;
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
                return false;
            }
        }

        private void restoreScreenBrightnessByLid() {
            try {
                if (isAutoBrightnessMode()) {
                    SmartCoverManager.this.mDisplayManager.setTemporaryAutoBrightnessAdjustment(System.getFloatForUser(SmartCoverManager.this.mContentResolver, System.SCREEN_AUTO_BRIGHTNESS_ADJ, Float.NaN, SmartCoverManager.this.mCurrentUserId));
                    return;
                }
                SmartCoverManager.this.mDisplayManager.setTemporaryBrightness(System.getIntForUser(SmartCoverManager.this.mContentResolver, System.SCREEN_BRIGHTNESS, this.mPowerManager.getDefaultScreenBrightnessSetting(), SmartCoverManager.this.mCurrentUserId));
            } catch (Exception e) {
                Slog.e(SmartCoverManager.TAG, "exception", e);
            }
        }

        private boolean isAutoBrightnessMode() {
            return 1 == System.getIntForUser(SmartCoverManager.this.mContentResolver, System.SCREEN_BRIGHTNESS_MODE, 0, SmartCoverManager.this.mCurrentUserId);
        }
    }

    static {
        boolean z = false;
        String str = "oxygen";
        if (str.equals(Build.DEVICE) || str.equals(Build.PRODUCT)) {
            z = true;
        }
        IS_D4 = z;
    }

    public void init(Context context, PowerManager powerManager) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mPowerManagerWrapper = new PowerManagerWrapper(powerManager);
        checkSmartCoverEnable();
        this.mCurrentUserId = 0;
        this.mDisplayManager = (DisplayManager) context.getSystemService(DisplayManager.class);
    }

    public boolean enableLidAfterBoot(int lidState) {
        checkSmartCoverEnable();
        boolean z = true;
        if (-1 == this.mSmartCoverMode) {
            MiuiSettings.System.setSmartCoverMode(MULTI ^ 1);
        }
        boolean inSmallWinMode = false;
        boolean setSmallWinMode = 2 <= this.mSmartCoverMode;
        if (setSmallWinMode) {
            if (lidState != 0) {
                z = false;
            }
            inSmallWinMode = z;
        }
        enableInSmallWinMode(inSmallWinMode);
        ContentResolver contentResolver = this.mContentResolver;
        String str = MiuiSettings.System.NEED_RESET_SCREEN_OFF_TIMEOUT;
        if (MiuiSettings.System.getBooleanForUser(contentResolver, str, false, -2)) {
            System.putInt(this.mContentResolver, System.SCREEN_OFF_TIMEOUT, Integer.MAX_VALUE);
            MiuiSettings.System.putBooleanForUser(this.mContentResolver, str, false, -2);
        }
        return setSmallWinMode;
    }

    public boolean notifyLidSwitchChanged(boolean lidOpen, boolean systemBooted) {
        boolean smartCoverEnable = checkSmartCoverEnable();
        guideSmartCoverSettingIfNeeded(smartCoverEnable);
        if (!smartCoverEnable) {
            return false;
        }
        handleLidSwitchChanged(lidOpen, systemBooted);
        return true;
    }

    private void guideSmartCoverSettingIfNeeded(boolean smartCoverEnable) {
        if (!smartCoverEnable && MULTI) {
            boolean first = this.mContentResolver;
            String str = MiuiSettings.System.SMARTCOVER_GUIDE_KEY;
            if (MiuiSettings.System.getBooleanForUser(first, str, true, 0) && isDeviceProvisioned(this.mContext)) {
                MiuiSettings.System.putBooleanForUser(this.mContentResolver, str, false, 0);
                this.mContext.startActivity(new Intent(ACTION_SMART_COVER_GUIDE).setComponent(new ComponentName("com.android.settings", SMART_COVER_GUIDE_ACTIVITY)).setFlags(268435456));
            }
        }
    }

    private static boolean isDeviceProvisioned(Context context) {
        return Global.getInt(context.getContentResolver(), "device_provisioned", 0) != 0;
    }

    public boolean notifyScreenTurningOn() {
        if (this.mSmartCoverLidOpen) {
            setScreenOffByLid(false);
        }
        if (this.mSmartCoverLidOpen) {
            return false;
        }
        Context context = this.mContext;
        if (context == null || !MiuiSettings.System.isInSmallWindowMode(context)) {
            return false;
        }
        return true;
    }

    public int getSmartCoverMode() {
        return this.mSmartCoverMode;
    }

    public boolean getSmartCoverLidOpen() {
        return this.mSmartCoverLidOpen;
    }

    public void onUserSwitch(int newUserId) {
        if (this.mCurrentUserId != newUserId) {
            this.mCurrentUserId = newUserId;
            enableInSmallWinMode(this.mSmartCoverLidOpen ^ 1);
        }
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print(SMART_COVER_LID_OPEN);
        pw.println(this.mSmartCoverLidOpen);
        pw.print(prefix);
        pw.print(SMART_COVER_MODE);
        pw.println(this.mSmartCoverMode);
    }

    private boolean enableInSmallWinMode(boolean enable) {
        boolean z = true;
        boolean supportSmallWinMode = 2 <= this.mSmartCoverMode;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        if (!(enable && supportSmallWinMode)) {
            z = false;
        }
        MiuiSettings.System.putBooleanForUser(contentResolver, MiuiSettings.System.KEY_IN_SMALL_WINDOW_MODE, z, this.mCurrentUserId);
        return supportSmallWinMode;
    }

    private boolean checkSmartCoverEnable() {
        this.mSmartCoverMode = SystemProperties.getInt(MiuiSettings.System.SMARTCOVER_MODE_KEY, -1);
        if (this.mSmartCoverMode != 0) {
            return true;
        }
        this.mSmartCoverLidOpen = true;
        return false;
    }

    private void handleLidSwitchChanged(boolean lidOpen, boolean systemBooted) {
        this.mSmartCoverLidOpen = lidOpen;
        if (!lidOpen) {
            setScreenOffByLid(true);
        }
        if (systemBooted) {
            this.mContext.sendBroadcastAsUser(new Intent("miui.intent.action.SMART_COVER").putExtra("is_smart_cover_open", lidOpen), UserHandle.CURRENT);
        }
        if (enableInSmallWinMode(lidOpen ^ 1)) {
            if (lidOpen) {
                this.mPowerManagerWrapper.wakeUp();
            } else {
                this.mPowerManagerWrapper.goToSleep();
            }
        }
        updateScreenOffTimeoutIfNeeded(lidOpen);
    }

    private void updateScreenOffTimeoutIfNeeded(boolean lidOpen) {
        if ((2147483647L == System.getLong(this.mContentResolver, System.SCREEN_OFF_TIMEOUT, 15000)) && !lidOpen) {
            triggerScreenOffTimeout(true);
        } else if (lidOpen && this.mNeedResetTimeout) {
            triggerScreenOffTimeout(false);
        }
    }

    private void triggerScreenOffTimeout(boolean change) {
        System.putInt(this.mContentResolver, System.SCREEN_OFF_TIMEOUT, change ? 15000 : Integer.MAX_VALUE);
        this.mNeedResetTimeout = change;
        MiuiSettings.System.putBooleanForUser(this.mContentResolver, MiuiSettings.System.NEED_RESET_SCREEN_OFF_TIMEOUT, this.mNeedResetTimeout, -2);
    }

    private void setScreenOffByLid(boolean byLid) {
        try {
            SystemProperties.set(MiuiSettings.System.SCREEN_OFF_BY_LID_PROPERTY_STRING, byLid ? "true" : "false");
        } catch (RuntimeException ex) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Set screen off by lid:");
            stringBuilder.append(ex);
            Slog.e(TAG, stringBuilder.toString());
        }
    }

    public static boolean deviceDisableKeysWhenLidClose() {
        if (!IS_D4) {
            return true;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Device: ");
        stringBuilder.append(Build.DEVICE);
        stringBuilder.append(" not disable keys when LidClose.");
        Slog.i(TAG, stringBuilder.toString());
        return false;
    }
}
