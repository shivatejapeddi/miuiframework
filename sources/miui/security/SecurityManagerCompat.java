package miui.security;

import android.app.ActivityManagerNative;
import android.app.AlarmManager;
import android.app.IApplicationThread;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PackageParser.Package;
import android.content.pm.PackageParser.PackageParserException;
import android.miui.Shell;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import java.io.File;
import java.lang.reflect.Method;

public class SecurityManagerCompat {
    private static final String ACTION_CANCEL_POWEROFF_ALARM = "org.codeaurora.poweroffalarm.action.CANCEL_ALARM";
    private static final String ACTION_SET_POWEROFF_ALARM = "org.codeaurora.poweroffalarm.action.SET_ALARM";
    public static final String LEADCORE = "leadcore";
    public static final String MTK = "mediatek";
    private static final String PINECONE = "pinecone";
    private static final String POWER_OFF_ALARM_PACKAGE = "com.qualcomm.qti.poweroffalarm";
    private static final int PRE_SCHEDULE_POWER_OFF_ALARM = 7;
    private static final int RTC_POWEROFF_WAKEUP_MTK = 8;
    private static final int RTC_POWEROFF_WAKEUP_QCOM_M = 5;
    private static final String TAG = "SecurityManagerCompat";
    private static final String TIME = "time";
    public static final String WAKEALARM_PATH_OF_LEADCORE = "/sys/comip/rtc_alarm";
    private static final String WAKEALARM_PATH_OF_PINECONE = "/sys/class/rtc/rtc1/wakealarm";
    public static final String WAKEALARM_PATH_OF_QCOM = "/sys/class/rtc/rtc0/wakealarm";

    public static void startActvity(Context context, IApplicationThread thread, IBinder token, String id, Intent intent) {
        try {
            ActivityManagerNative.getDefault().startActivity(thread, null, intent, intent.resolveTypeIfNeeded(context.getContentResolver()), token, id, -1, 0, null, null);
        } catch (RemoteException e) {
        }
    }

    public static void startActvityAsUser(Context context, IApplicationThread thread, IBinder token, String id, Intent intent, int userId) {
        try {
            try {
                ActivityManagerNative.getDefault().startActivityAsUser(thread, null, intent, intent.resolveTypeIfNeeded(context.getContentResolver()), token, id, -1, 0, null, null, userId);
            } catch (RemoteException e) {
            }
        } catch (RemoteException e2) {
            Intent intent2 = intent;
        }
    }

    public static void writeBootTime(Context context, String vendor, long wakeTime) {
        if (vendor.equals(MTK)) {
            writeMTKBootTime(context, wakeTime);
        } else if (vendor.equals(LEADCORE)) {
            Shell.write(WAKEALARM_PATH_OF_LEADCORE, String.valueOf(wakeTime));
        } else if (vendor.equals("pinecone")) {
            String valueOf = String.valueOf(0);
            String str = WAKEALARM_PATH_OF_PINECONE;
            Shell.write(str, valueOf);
            Shell.write(str, String.valueOf(wakeTime));
        } else {
            writeQcomBootTime(context, wakeTime);
        }
    }

    private static void writeMTKBootTime(Context context, long wakeTime) {
        AlarmManager am = (AlarmManager) context.getSystemService("alarm");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, new Intent(), 0);
        if (wakeTime == 0) {
            try {
                Method declaredMethod = am.getClass().getDeclaredMethod("cancelPoweroffAlarm", new Class[]{String.class});
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(am, new Object[]{"android"});
                return;
            } catch (Exception e) {
                Log.e(TAG, "cancelPoweroffAlarm: ", e);
                return;
            }
        }
        am.set(VERSION.SDK_INT > 26 ? 7 : 8, 1000 * wakeTime, pi);
    }

    private static void writeQcomBootTime(Context context, long wakeTime) {
        String str = WAKEALARM_PATH_OF_QCOM;
        boolean exists = new File(str).exists();
        String str2 = TAG;
        if (exists) {
            Shell.write(str, String.valueOf(wakeTime));
            Log.d(str2, "Wake up time updated to wakealarm");
        } else if (VERSION.SDK_INT < 26) {
            try {
                ((AlarmManager) context.getSystemService("alarm")).setExact(5, 1000 * wakeTime, PendingIntent.getBroadcast(context, null, new Intent(), 134217728));
            } catch (Exception e) {
                Log.e(str2, "alarm type 5 not supported", e);
            }
        } else {
            sendCancelBootAlarm(context, wakeTime);
            sendSetBootAlarm(context, wakeTime);
            if (VERSION.SDK_INT > 27 && wakeTime == 0) {
                sendCancelBootAlarm(context, wakeTime);
            }
        }
    }

    public static PackageParser createPackageParser(String sourceDir) {
        return new PackageParser();
    }

    public static Package parsePackage(PackageParser pp, String sourceDir) {
        try {
            return pp.parsePackage(new File(sourceDir), 0);
        } catch (PackageParserException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void checkAppHidden(PackageManager pm, String packageName, UserHandle userHandle) {
        if (pm.getApplicationHiddenSettingAsUser(packageName, userHandle)) {
            pm.setApplicationHiddenSettingAsUser(packageName, false, userHandle);
        }
    }

    private static void sendCancelBootAlarm(Context context, long wakeTime) {
        Intent intent = new Intent(ACTION_CANCEL_POWEROFF_ALARM);
        intent.addFlags(268435456);
        intent.setPackage(POWER_OFF_ALARM_PACKAGE);
        intent.putExtra("time", 1000 * wakeTime);
        context.sendBroadcast(intent);
        Log.d(TAG, "Send cancel poweroff alarm broadcast");
    }

    private static void sendSetBootAlarm(Context context, long wakeTime) {
        Intent intent = new Intent(ACTION_SET_POWEROFF_ALARM);
        intent.addFlags(268435456);
        intent.setPackage(POWER_OFF_ALARM_PACKAGE);
        intent.putExtra("time", 1000 * wakeTime);
        context.sendBroadcast(intent);
        Log.d(TAG, "Send set poweroff alarm broadcast");
    }
}
