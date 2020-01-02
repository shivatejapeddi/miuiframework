package miui.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityThread;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioSystem;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.PowerManager;
import android.provider.MiuiSettings.AntiSpam;
import android.provider.MiuiSettings.SilenceMode;
import android.telephony.TelephonyManager;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import miui.os.Build;

public class QuietUtils {
    private static ArrayList<String> AUTHORIZE_PACKAGE = new ArrayList(Arrays.asList(new String[]{"android", "com.android.deskclock", "com.android.providers.telephony"}));
    private static ArrayList<String> PHONE_AND_SMS_PACKAGE = new ArrayList(Arrays.asList(new String[]{TelephonyManager.PHONE_PROCESS_NAME, "com.android.incallui", "com.android.server.telecom", "com.miui.voip", "com.android.mms"}));
    private static final String TAG = "QuietUtils";
    public static final int TYPE_AUDIO_MANAGER = 8;
    public static final int TYPE_MEDIA_PLAYER = 7;
    public static final int TYPE_NOTIFICATION = 5;
    public static final int TYPE_POWER_MANAGER = 1;
    public static final int TYPE_POWER_MANAGER_SERVICE = 3;
    public static final int TYPE_POWER_MANAGER_WAKEUP = 2;
    public static final int TYPE_SOUND_POOL = 6;
    public static final int TYPE_VIBRATOR = 4;
    public static final String ZENMODE_TYPE_ALLW_FROM = "5";
    public static final String ZENMODE_TYPE_CALL_STATUS = "3";
    public static final String ZENMODE_TYPE_EVENT_STATUS = "4";
    public static final String ZENMODE_TYPE_MESSAGE_STATUS = "2";
    public static final String ZENMODE_TYPE_STATUS = "1";

    public static boolean checkQuiet(int type, int flags, String pkg, CharSequence targetPkg) {
        if (SilenceMode.isSupported) {
            return checkNewQuiet(type, flags, pkg, targetPkg);
        }
        Context context = ActivityThread.currentApplication();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        String cPkg = context.getPackageName();
        String str = "com.android.deskclock";
        String str2 = "android";
        String str3 = TAG;
        StringBuilder stringBuilder;
        switch (type) {
            case 1:
                if (!(((268435456 & flags) == 0 && flags != 26 && flags != 10 && flags != 6 && flags != 1) || str2.equals(cPkg) || str.equals(cPkg) || "com.google.android.talk".equals(cPkg) || !checkZenmod(context, cPkg))) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("POWER_MANAGER pkg:");
                    stringBuilder.append(cPkg);
                    Log.w(str3, stringBuilder.toString());
                    return true;
                }
            case 2:
                if (!(!checkZenmod(context, cPkg) || str2.equals(cPkg) || str.equals(cPkg))) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("POWER_MANAGER_WAKEUP pkg:");
                    stringBuilder.append(cPkg);
                    Log.w(str3, stringBuilder.toString());
                    return true;
                }
            case 4:
                if (!(!checkZenmod(context, cPkg) || str.equals(cPkg) || str2.equals(cPkg) || ("com.android.cellbroadcastreceiver".equals(cPkg) && Build.checkRegion("CL")))) {
                    str = "VIBRATOR pkg:";
                    if (!pm.isScreenOn()) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(cPkg);
                        Log.w(str3, stringBuilder.toString());
                        return true;
                    } else if (!isTopActivity(context, cPkg)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(cPkg);
                        Log.w(str3, stringBuilder.toString());
                        return true;
                    }
                }
                break;
            case 5:
                String charSequence = targetPkg != null ? targetPkg.toString() : pkg != null ? pkg : "";
                if (checkZenmod(context, charSequence)) {
                    str = " targetPkg:";
                    str2 = "NOTIFICATION pkg:";
                    if (!pm.isScreenOn()) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str2);
                        stringBuilder.append(pkg);
                        stringBuilder.append(str);
                        stringBuilder.append(targetPkg);
                        Log.w(str3, stringBuilder.toString());
                        return true;
                    } else if (targetPkg != null) {
                        if (!isTopActivity(context, targetPkg.toString())) {
                            stringBuilder = new StringBuilder();
                            stringBuilder.append(str2);
                            stringBuilder.append(pkg);
                            stringBuilder.append(str);
                            stringBuilder.append(targetPkg);
                            Log.w(str3, stringBuilder.toString());
                            return true;
                        }
                    } else if (!(pkg == null || isTopActivity(context, pkg.toString()))) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str2);
                        stringBuilder.append(pkg);
                        stringBuilder.append(str);
                        stringBuilder.append(targetPkg);
                        Log.w(str3, stringBuilder.toString());
                        return true;
                    }
                }
                break;
            case 6:
                if (checkZenmod(context, cPkg)) {
                    str = "SOUND_POOL pkg:";
                    if (!pm.isScreenOn()) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(cPkg);
                        Log.w(str3, stringBuilder.toString());
                        return true;
                    } else if (!checkAuthorizePackage(context, cPkg, true)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(cPkg);
                        Log.w(str3, stringBuilder.toString());
                        return true;
                    }
                }
                break;
            case 7:
                if (flags == 2 && checkZenmod(context, cPkg)) {
                    str = "MEDIA_PLAYER pkg:";
                    if (!pm.isScreenOn()) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(cPkg);
                        Log.w(str3, stringBuilder.toString());
                        return true;
                    } else if (!checkAuthorizePackage(context, cPkg, true)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(cPkg);
                        Log.w(str3, stringBuilder.toString());
                        return true;
                    }
                }
                break;
            case 8:
                if ((flags == 5 || flags == 2) && !str2.equals(cPkg) && !"com.android.systemui".equals(cPkg) && checkZenmod(context, cPkg)) {
                    str = "AUDIO_MANAGER pkg:";
                    if (!pm.isScreenOn()) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(cPkg);
                        Log.w(str3, stringBuilder.toString());
                        return true;
                    } else if (!checkAuthorizePackage(context, cPkg, true)) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(str);
                        stringBuilder.append(cPkg);
                        Log.w(str3, stringBuilder.toString());
                        return true;
                    }
                }
                break;
        }
        return false;
    }

    public static boolean checkNewQuiet(int type, int flags, String pkg, CharSequence targetPkg) {
        if (VERSION.SDK_INT < 21) {
            return false;
        }
        Context context = ActivityThread.currentApplication();
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        String cPkg = context.getPackageName();
        int zenMode = SilenceMode.getZenMode(context);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("type:");
        stringBuilder.append(type);
        stringBuilder.append(", flags:");
        stringBuilder.append(flags);
        stringBuilder.append(", cpkg:");
        stringBuilder.append(cPkg);
        String stringBuilder2 = stringBuilder.toString();
        String str = TAG;
        Log.i(str, stringBuilder2);
        stringBuilder2 = "android";
        if (type != 2) {
            if (type != 4) {
                if (type != 6) {
                    if (type == 8 && ((flags == 5 || flags == 2) && checkNewZenModeEnable(context, cPkg) && !TelephonyManager.PHONE_PROCESS_NAME.equals(cPkg) && !"com.miui.voip".equals(cPkg) && !stringBuilder2.equals(cPkg) && AudioSystem.getDevicesForStream(2) == 2)) {
                        Log.d(str, "speaker volume is 0");
                        return true;
                    }
                } else if (checkNewZenModeEnable(context, cPkg)) {
                    return true;
                }
            } else if (checkNewZenModeEnable(context, cPkg) && zenMode == 3) {
                return true;
            } else {
                return false;
            }
        } else if (!(!checkNewZenModeEnable(context, cPkg) || stringBuilder2.equals(cPkg) || "com.android.deskclock".equals(cPkg))) {
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("POWER_MANAGER_WAKEUP pkg:");
            stringBuilder3.append(cPkg);
            Log.w(str, stringBuilder3.toString());
            return true;
        }
        return false;
    }

    private static boolean checkAuthorizePackage(Context context, String pkg, boolean isCheckTopActivity) {
        if (AUTHORIZE_PACKAGE.contains(pkg)) {
            return true;
        }
        if (isCheckTopActivity && isTopActivity(context, pkg)) {
            return true;
        }
        return false;
    }

    private static boolean checkZenmod(Context context, String pkg) {
        boolean z = true;
        if (VERSION.SDK_INT < 21) {
            if (!AntiSpam.isQuietModeEnable(context) || TelephonyManager.PHONE_PROCESS_NAME.equals(pkg) || "com.miui.voip".equals(pkg)) {
                z = false;
            }
            return z;
        }
        if (!AntiSpam.isQuietModeEnable(context) || PHONE_AND_SMS_PACKAGE.contains(pkg) || isZenmode(context, "4")) {
            z = false;
        }
        return z;
    }

    private static boolean checkNewZenModeEnable(Context context, String pkg) {
        return SilenceMode.isSilenceModeEnable(context);
    }

    private static boolean isZenmode(Context context, String type) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(Uri.withAppendedPath(Uri.parse("content://antispamCommon/zenmode"), type), null, null, null, null);
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e) {
                }
                return true;
            }
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e2) {
                }
            }
            return false;
        } catch (Exception e3) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("exception when checkZenmodConfig :");
            stringBuilder.append(e3.toString());
            Log.e(str, stringBuilder.toString());
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                try {
                    cursor.close();
                } catch (Exception e4) {
                }
            }
        }
    }

    private static boolean isTopActivity(Context context, String pkgName) {
        try {
            for (RunningAppProcessInfo appProcess : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningAppProcesses()) {
                if (appProcess.importance == 100) {
                    for (String pkg : appProcess.pkgList) {
                        if (pkg.equals(pkgName)) {
                            return true;
                        }
                    }
                    continue;
                }
            }
        } catch (SecurityException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Fail to get RunningProcessInfo:");
            stringBuilder.append(e.toString());
            Log.i(TAG, stringBuilder.toString());
        }
        return false;
    }
}
