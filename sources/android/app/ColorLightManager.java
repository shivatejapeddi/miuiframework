package android.app;

import android.Manifest.permission;
import android.app.INotificationManager.Stub;
import android.content.Context;
import android.os.IBinder;
import android.os.Process;
import android.os.ServiceManager;
import android.provider.MiuiSettings;
import android.provider.Settings.Secure;
import miui.lights.ILightsManager;

public class ColorLightManager {
    public static final int LIGHTSTYLE_ALARM = 4;
    public static final int LIGHTSTYLE_DEFAULT = 0;
    public static final int LIGHTSTYLE_EXPAND = 5;
    public static final int LIGHTSTYLE_GAME = 2;
    public static final int LIGHTSTYLE_LUCKYMONEY = 6;
    public static final int LIGHTSTYLE_MUSIC = 3;
    public static final int LIGHTSTYLE_PHONE = 1;
    public static final int LIGHTSTYLE_SRC_NOTFOUND = -100;
    public static final int LIGHTSTYLE_TURNOFF = -1;
    private static long ONE_HOUR = (ONE_MINUTE * 60);
    private static long ONE_MINUTE = 60000;
    private static ILightsManager sService;
    private Context mContext;

    public ColorLightManager(Context mContext) {
        this.mContext = mContext;
    }

    public static ILightsManager getServices() {
        if (sService == null) {
            try {
                INotificationManager mService = Stub.asInterface(ServiceManager.getService("notification"));
                sService = ILightsManager.Stub.asInterface((IBinder) mService.getClass().getMethod("getColorLightManager", new Class[0]).invoke(mService, new Object[0]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sService;
    }

    public void setColorfulLight(int styleType) {
        try {
            getServices().setColorfulLight(this.mContext.getPackageName(), styleType, Process.myUserHandle().getIdentifier());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setColorfulLightStartEnableTime(long startHour, long startMin) {
        this.mContext.enforceCallingOrSelfPermission(permission.WRITE_SECURE_SETTINGS, null);
        Secure.putLong(this.mContext.getContentResolver(), MiuiSettings.Secure.LIGHT_TURN_ON_STARTTIME, (ONE_HOUR * startHour) + (ONE_MINUTE * startMin));
    }

    public void setColorfulLightEndEnableTime(long endHour, long endMin) {
        this.mContext.enforceCallingOrSelfPermission(permission.WRITE_SECURE_SETTINGS, null);
        Secure.putLong(this.mContext.getContentResolver(), MiuiSettings.Secure.LIGHT_TURN_ON_ENDTIME, (ONE_HOUR * endHour) + (ONE_MINUTE * endMin));
    }

    public void enableLight(boolean enable) {
        this.mContext.enforceCallingOrSelfPermission(permission.WRITE_SECURE_SETTINGS, null);
        Secure.putInt(this.mContext.getContentResolver(), MiuiSettings.Secure.LIGHT_TURN_ON, enable);
    }

    public void enableNotificationLight(boolean enable) {
        this.mContext.enforceCallingOrSelfPermission(permission.WRITE_SECURE_SETTINGS, null);
        Secure.putInt(this.mContext.getContentResolver(), MiuiSettings.Secure.NOTIFICATION_LIGHT_TURN_ON, enable);
    }

    public void enableBatteryLight(boolean enable) {
        this.mContext.enforceCallingOrSelfPermission(permission.WRITE_SECURE_SETTINGS, null);
        Secure.putInt(this.mContext.getContentResolver(), MiuiSettings.Secure.BATTERY_LIGHT_TURN_ON, enable);
    }

    public void enableMusiclLight(boolean enable) {
        this.mContext.enforceCallingOrSelfPermission(permission.WRITE_SECURE_SETTINGS, null);
        Secure.putInt(this.mContext.getContentResolver(), MiuiSettings.Secure.MUSIC_LIGHT_TURN_ON, enable);
    }

    public boolean isLightEnable() {
        return Secure.getIntForUser(this.mContext.getContentResolver(), MiuiSettings.Secure.LIGHT_TURN_ON, 1, -2) == 1;
    }

    public boolean isNotificationLightEnable() {
        return Secure.getIntForUser(this.mContext.getContentResolver(), MiuiSettings.Secure.NOTIFICATION_LIGHT_TURN_ON, 1, -2) == 1;
    }

    public boolean isBatteryLightEnable() {
        return Secure.getIntForUser(this.mContext.getContentResolver(), MiuiSettings.Secure.BATTERY_LIGHT_TURN_ON, 1, -2) == 1;
    }

    public boolean isMusicLightEnable() {
        return Secure.getIntForUser(this.mContext.getContentResolver(), MiuiSettings.Secure.MUSIC_LIGHT_TURN_ON, 1, -2) == 1;
    }
}
