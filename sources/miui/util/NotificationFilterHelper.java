package miui.util;

import android.app.INotificationManager;
import android.app.INotificationManager.Stub;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.miui.AppOpsUtils;
import android.miui.R;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.provider.MiuiSettings.AntiSpam;
import android.provider.Settings.Global;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import miui.os.Build;
import miui.push.PushConstants;
import miui.securityspace.XSpaceUserHandle;

public class NotificationFilterHelper {
    private static final String APP_NOTIFICATION = "app_notification";
    public static final int BACKUP_CHANNEL_IMPORTANCE_DEFAULT = 3;
    private static final String CHANNEL_FLAG = "_channel_flag";
    private static final String CHANNEL_IMPORTANCE_BACKUP = "_channel_importance_backup";
    public static final int DISABLE_ALL = 3;
    public static final int DISABLE_FLOATING = 1;
    public static final int ENABLE = 2;
    public static final String IMPORTANCE = "_importance";
    public static final int IMPORTANCE_DEFAULT = 0;
    public static final int IMPORTANCE_HIGH = 1;
    public static final int IMPORTANCE_LOW = -1;
    public static final String KEYGUARD = "_keyguard";
    private static final String KEY_FLOAT_VERSION = "sysui_float_version";
    public static final String KEY_FLOAT_WHITELIST = "float_whitelist";
    private static final String KEY_KEYGUARD_VERSION = "sysui_keyguard_version";
    public static final String KEY_KEYGUARD_WHITELIST = "keyguard_whitelist";
    public static final String LED = "_led";
    public static final String MESSAGE = "_message";
    public static final int NONE = 0;
    public static final String SOUND = "_sound";
    private static final String SYSTEMUI_PACKAGE_NAME = "com.android.systemui";
    private static final String TAG = "NotificationFilterHelper";
    public static final String VIBRATE = "_vibrate";
    private static INotificationManager nm = Stub.asInterface(ServiceManager.getService("notification"));
    private static long sFloatVersionCode = -1;
    private static ArrayList<String> sFloatWhiteList = new ArrayList();
    private static HashMap<String, Boolean> sIsSystemApp = new HashMap();
    private static long sKeyguardVersionCode = -1;
    private static ArrayList<String> sKeyguardWhiteList = new ArrayList();
    private static HashSet<String> sNotificationCanBeBlockedList = new HashSet();
    private static HashSet<String> sNotificationCannotSetImportanceList = new HashSet();
    private static HashSet<String> sNotificationForcedEnabledAllChannelList = new HashSet();
    private static HashMap<String, HashSet<String>> sNotificationForcedEnabledChannelList = new HashMap();
    private static HashSet<String> sNotificationForcedEnabledPkgList = new HashSet();

    static {
        sNotificationCanBeBlockedList.add("com.miui.fm");
        sNotificationCanBeBlockedList.add("com.miui.antispam");
        HashSet hashSet = sNotificationCanBeBlockedList;
        String str = AntiSpam.ANTISPAM_PKG;
        hashSet.add(str);
        sNotificationForcedEnabledPkgList.add("android");
        String str2 = "com.android.incallui";
        sNotificationForcedEnabledPkgList.add(str2);
        sNotificationForcedEnabledPkgList.add("com.android.deskclock");
        sNotificationForcedEnabledPkgList.add("com.android.mms");
        sNotificationForcedEnabledPkgList.add("com.android.bluetooth");
        sNotificationForcedEnabledPkgList.add("com.android.updater");
        sNotificationForcedEnabledPkgList.add("com.android.providers.downloads");
        sNotificationForcedEnabledPkgList.add("com.miui.hybrid");
        sNotificationForcedEnabledAllChannelList.add(PushConstants.PUSH_SERVICE_PACKAGE_NAME);
        HashSet<String> channelList = new HashSet();
        channelList.add(str);
        sNotificationForcedEnabledChannelList.put(str, channelList);
        sNotificationCannotSetImportanceList.add(str2);
    }

    public static SharedPreferences getSharedPreferences(Context context) {
        boolean isXSpaceUserId = XSpaceUserHandle.isXSpaceUserId(context.getUserId());
        String str = SYSTEMUI_PACKAGE_NAME;
        if (isXSpaceUserId) {
            try {
                context = context.createPackageContextAsUser(str, 2, UserHandle.OWNER);
            } catch (NameNotFoundException e) {
                Log.e(TAG, "Can't find pkg: com.android.systemui", e);
            }
        }
        if (!context.getPackageName().equals(str)) {
            try {
                context = context.createPackageContext(str, 2);
            } catch (NameNotFoundException e2) {
                e2.printStackTrace();
            }
        }
        return context.getSharedPreferences(APP_NOTIFICATION, 4);
    }

    public static boolean canSendNotifications(Context context, String pkg) {
        return getAppFlag(context, pkg, true) != 3;
    }

    public static int getAppFlag(Context context, String pkg, boolean checkSystemSetting) {
        return getAppFlag(context, pkg, getAppUid(context, pkg), checkSystemSetting);
    }

    public static int getAppFlag(Context context, String pkg, int uid, boolean checkSystemSetting) {
        if (!(checkSystemSetting ? areNotificationsEnabled(context, pkg, uid) : true)) {
            return 3;
        }
        int flag = getSharedPreferences(context).getInt(pkg, 0);
        if (flag == 0) {
            flag = getDefaultFlag(context, pkg, false);
        }
        if (flag == 0) {
            return 1;
        }
        return flag;
    }

    public static int getChannelFlag(Context context, String pkg, String channel, int uid, boolean checkSystemSetting) {
        if (!(checkSystemSetting ? areNotificationsEnabled(context, pkg, uid) : true)) {
            return 3;
        }
        int flag = getSharedPreferences(context).getInt(String.format("%s_%s%s", new Object[]{pkg, channel, CHANNEL_FLAG}), 0);
        if (flag == 0) {
            flag = getDefaultFlag(context, pkg, false);
        }
        if (flag == 0) {
            return 1;
        }
        return flag;
    }

    private static int getDefaultFlag(Context context, String pkg, boolean isInKeyguardMode) {
        initFilterList(context);
        int i = 2;
        if (isInKeyguardMode) {
            if (!sKeyguardWhiteList.contains(pkg)) {
                i = 0;
            }
            return i;
        }
        if (!sFloatWhiteList.contains(pkg)) {
            i = 0;
        }
        return i;
    }

    private static void initFilterList(Context context) {
        long latestVersion = Global.getLong(context.getContentResolver(), KEY_FLOAT_VERSION, -1);
        if (sFloatWhiteList.isEmpty() || latestVersion != sFloatVersionCode) {
            sFloatVersionCode = latestVersion;
            ArrayList<String> floatWhiteList = new ArrayList();
            Set<String> floatSet = getWhiteListFromCache(context, KEY_FLOAT_WHITELIST);
            if (floatSet == null || floatSet.isEmpty()) {
                floatWhiteList.addAll(Arrays.asList(context.getResources().getStringArray(R.array.float_notification_whitelist)));
            } else {
                floatWhiteList.addAll(floatSet);
            }
            sFloatWhiteList = floatWhiteList;
        }
        long latestVersion2 = Global.getLong(context.getContentResolver(), KEY_KEYGUARD_VERSION, -1);
        if (sKeyguardWhiteList.isEmpty() || latestVersion2 != sKeyguardVersionCode) {
            sKeyguardVersionCode = latestVersion2;
            ArrayList<String> keyguardWhiteList = new ArrayList();
            Set<String> keyguardSet = getWhiteListFromCache(context, KEY_KEYGUARD_WHITELIST);
            if (keyguardSet == null || keyguardSet.isEmpty()) {
                keyguardWhiteList.addAll(Arrays.asList(context.getResources().getStringArray(R.array.keyguard_whitelist)));
            } else {
                keyguardWhiteList.addAll(keyguardSet);
            }
            sKeyguardWhiteList = keyguardWhiteList;
        }
    }

    private static Set<String> getWhiteListFromCache(Context context, String key) {
        try {
            return getSharedPreferences(context).getStringSet(key, null);
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("error get whiteList: ");
            stringBuilder.append(key);
            Log.e(TAG, stringBuilder.toString());
            return null;
        }
    }

    public static void updateFloatWhiteList(Context context, List<String> pkgList) {
        Global.putLong(context.getContentResolver(), KEY_FLOAT_VERSION, System.currentTimeMillis());
        getSharedPreferences(context).edit().putStringSet(KEY_FLOAT_WHITELIST, new HashSet(pkgList)).apply();
    }

    public static void updateKeyguardWhitelist(Context context, List<String> pkgList) {
        Global.putLong(context.getContentResolver(), KEY_KEYGUARD_VERSION, System.currentTimeMillis());
        getSharedPreferences(context).edit().putStringSet(KEY_KEYGUARD_WHITELIST, new HashSet(pkgList)).apply();
    }

    private static boolean areNotificationsEnabled(Context context, String pkg) {
        return areNotificationsEnabled(context, pkg, getAppUid(context, pkg));
    }

    private static boolean areNotificationsEnabled(Context context, String pkg, int appUid) {
        try {
            return nm.areNotificationsEnabledForPackage(pkg, appUid);
        } catch (RemoteException e) {
            return true;
        }
    }

    public static void enableNotifications(Context context, String pkg, boolean enabled) {
        enableNotifications(context, pkg, getAppUid(context, pkg), enabled);
    }

    public static void enableNotifications(Context context, String pkg, int appUid, boolean enabled) {
        if (enabled || !isNotificationForcedFor(context, pkg)) {
            try {
                if (XSpaceUserHandle.isUidBelongtoXSpace(appUid)) {
                    resolveAssociatedUid(context, UserHandle.OWNER, pkg, enabled);
                } else if (context.getUserId() == 0 && XSpaceUserHandle.isAppInXSpace(context, pkg)) {
                    resolveAssociatedUid(context, new UserHandle(999), pkg, enabled);
                }
                nm.setNotificationsEnabledForPackage(pkg, appUid, enabled);
                try {
                    if (!Build.IS_TABLET || VERSION.SDK_INT >= 26) {
                        int i = 0;
                        ApplicationInfo info = context.getPackageManager().getApplicationInfo(pkg, 0);
                        if (info != null && (info.flags & 1) == 0) {
                            if (!enabled) {
                                i = 1;
                            }
                            AppOpsUtils.setMode(context, 11, pkg, i);
                        }
                    }
                } catch (NameNotFoundException e) {
                }
            } catch (RemoteException e2) {
            }
        }
    }

    private static void resolveAssociatedUid(Context context, UserHandle user, String pkg, boolean enabled) {
        String str = TAG;
        try {
            nm.setNotificationsEnabledForPackage(pkg, getAppUid(context.createPackageContextAsUser(pkg, 2, user), pkg), enabled);
        } catch (RemoteException e) {
            Log.e(str, "Can't talk to NotificationManagerService", e);
        } catch (NameNotFoundException e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can't find pkg: ");
            stringBuilder.append(pkg);
            Log.e(str, stringBuilder.toString(), e2);
        }
    }

    public static void enableStatusIcon(Context context, String pkg, boolean enable) {
        enableStatusIcon(context, pkg, enable ? 2 : 1);
    }

    public static void enableStatusIcon(Context context, String pkg, String channelId, boolean enable) {
        r0 = new Object[3];
        int i = 1;
        r0[1] = channelId;
        r0[2] = CHANNEL_FLAG;
        String format = String.format("%s_%s%s", r0);
        if (enable) {
            i = 2;
        }
        enableStatusIcon(context, format, i);
    }

    private static void enableStatusIcon(Context context, String key, int value) {
        getSharedPreferences(context).edit().putInt(key, value).apply();
    }

    public static boolean isSystemApp(String pkg, PackageManager pm) {
        Boolean isSystemApp = (Boolean) sIsSystemApp.get(pkg);
        if (isSystemApp == null) {
            ApplicationInfo info = null;
            boolean z = false;
            try {
                info = pm.getApplicationInfo(pkg, 0);
            } catch (NameNotFoundException e) {
            }
            if (!(info == null || (info.flags & 1) == 0)) {
                z = true;
            }
            isSystemApp = Boolean.valueOf(z);
            sIsSystemApp.put(pkg, isSystemApp);
        }
        return isSystemApp.booleanValue();
    }

    public static boolean isNotificationForcedFor(Context context, String pkg) {
        return isNotificationForcedEnabled(context, pkg) || containNonBlockableChannel(pkg) || sNotificationForcedEnabledAllChannelList.contains(pkg);
    }

    private static boolean isNotificationForcedEnabled(Context context, String pkg) {
        if (sNotificationForcedEnabledPkgList.contains(pkg)) {
            return true;
        }
        if (canSystemNotificationBeBlocked(pkg)) {
            return false;
        }
        int appId = UserHandle.getAppId(getAppUid(context, pkg));
        if (appId == 1000 || appId == 1001 || appId == 0) {
            return true;
        }
        return false;
    }

    private static boolean containNonBlockableChannel(String pkg) {
        return sNotificationForcedEnabledChannelList.containsKey(pkg);
    }

    public static boolean isNotificationForcedEnabled(Context context, String pkg, String channelId) {
        return isNotificationForcedEnabled(context, pkg) || (sNotificationForcedEnabledChannelList.get(pkg) != null && ((HashSet) sNotificationForcedEnabledChannelList.get(pkg)).contains(channelId));
    }

    public static boolean canSystemNotificationBeBlocked(String pkg) {
        return sNotificationCanBeBlockedList.contains(pkg);
    }

    public static HashSet<String> getNotificationForcedEnabledList() {
        return sNotificationForcedEnabledPkgList;
    }

    public static boolean canNotificationSetImportance(String pkg) {
        return sNotificationCannotSetImportanceList.contains(pkg) ^ 1;
    }

    private static int getAppUid(Context context, String pkg) {
        try {
            return context.getPackageManager().getApplicationInfo(pkg, 0).uid;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    public static Uri getCustomSoundUri(Context context, Uri soundUri, StatusBarNotification sbn) {
        return soundUri;
    }

    private static String getRealPackageName(StatusBarNotification sbn) {
        CharSequence targetPkg;
        String str = "miui.targetPkg";
        if (sbn.getNotification().extras.containsKey(str)) {
            targetPkg = sbn.getNotification().extras.getCharSequence(str);
        } else {
            targetPkg = sbn.getNotification().extraNotification.getTargetPkg();
        }
        return TextUtils.isEmpty(targetPkg) ? sbn.getPackageName() : targetPkg.toString();
    }

    public static boolean isAllowed(Context context, StatusBarNotification sbn, String type) {
        return isAllowed(context, getRealPackageName(sbn), type);
    }

    public static boolean isAllowed(Context context, String pkg, String type) {
        SharedPreferences sharedPreferences;
        StringBuilder stringBuilder;
        boolean z = false;
        if ("_keyguard".equals(type) || "_sound".equals(type)) {
            sharedPreferences = getSharedPreferences(context);
            stringBuilder = new StringBuilder();
            stringBuilder.append(pkg);
            stringBuilder.append(type);
            if (!sharedPreferences.contains(stringBuilder.toString())) {
                if (getDefaultFlag(context, pkg, true) == 2) {
                    z = true;
                }
                return z;
            }
        }
        if ("_led".equals(type)) {
            sharedPreferences = getSharedPreferences(context);
            stringBuilder = new StringBuilder();
            stringBuilder.append(pkg);
            stringBuilder.append(type);
            if (!sharedPreferences.contains(stringBuilder.toString())) {
                if (getDefaultFlag(context, pkg, false) == 2) {
                    z = true;
                }
                return z;
            }
        }
        sharedPreferences = getSharedPreferences(context);
        String key = new StringBuilder();
        key.append(pkg);
        key.append(type);
        key = key.toString();
        if (!sharedPreferences.contains(key) || (sharedPreferences.getAll().get(key) instanceof Boolean)) {
            return sharedPreferences.getBoolean(key, true);
        }
        Log.e(TAG, "got non boolean values, return true");
        return true;
    }

    public static boolean isAllowedWithChannel(Context context, String pkg, String channelId, String type) {
        boolean z = false;
        if (("_keyguard".equals(type) || "_sound".equals(type)) && !getSharedPreferences(context).contains(getChannelKey(pkg, channelId, type))) {
            if (getDefaultFlag(context, pkg, true) == 2) {
                z = true;
            }
            return z;
        } else if (!"_led".equals(type) || getSharedPreferences(context).contains(getChannelKey(pkg, channelId, type))) {
            return getSharedPreferences(context).getBoolean(getChannelKey(pkg, channelId, type), true);
        } else {
            if (getDefaultFlag(context, pkg, false) == 2) {
                z = true;
            }
            return z;
        }
    }

    public static void setAllow(Context context, String pkg, String type, boolean allow) {
        Editor edit = getSharedPreferences(context).edit();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pkg);
        stringBuilder.append(type);
        edit.putBoolean(stringBuilder.toString(), allow).apply();
    }

    public static void setImportance(Context context, String pkg, int importance) {
        Editor edit = getSharedPreferences(context).edit();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pkg);
        stringBuilder.append(IMPORTANCE);
        edit.putInt(stringBuilder.toString(), importance).apply();
    }

    public static int getImportance(Context context, String pkg) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pkg);
        stringBuilder.append(IMPORTANCE);
        return sharedPreferences.getInt(stringBuilder.toString(), 0);
    }

    public static void setAllow(Context context, String pkg, String channelId, String type, boolean allow) {
        getSharedPreferences(context).edit().putBoolean(getChannelKey(pkg, channelId, type), allow).apply();
    }

    public static int getBackupChannelImportance(Context context, String pkg, String channelId) {
        int backupImportance = getSharedPreferences(context).getInt(getChannelKey(pkg, channelId, CHANNEL_IMPORTANCE_BACKUP), 3);
        if (backupImportance == 0) {
            return 3;
        }
        return backupImportance;
    }

    public static void saveBackupChannelImportance(Context context, String pkg, String channelId, int importance) {
        getSharedPreferences(context).edit().putInt(getChannelKey(pkg, channelId, CHANNEL_IMPORTANCE_BACKUP), importance).apply();
    }

    private static String getChannelKey(String pkg, String channelId, String key) {
        return String.format("%s_%s%s", new Object[]{pkg, channelId, key});
    }
}
