package com.android.internal.app;

import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.media.AudioSystem;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.UserHandle;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import miui.os.Build;
import miui.securityspace.CrossUserUtils;
import miui.securityspace.CrossUserUtilsCompat;

public class ResolverActivityInjector {
    private static final String ACTION_TYPE = "image/";
    private static final String PKG_NAME = "com.tencent.mm";
    private static final String SEND_ACTION = "android.intent.action.SEND";
    private static final String SHARE_IMG_UI = "com.tencent.mm.ui.tools.ShareImgUI";
    private static final String SHARE_TO_TIME_LINE = "com.tencent.mm.ui.tools.ShareToTimeLineUI";
    private static final String TAG = "RAInjector";
    private static final String XMAN_CLOUD_DISABLE = "xman_cloud_disable";
    private static final String XMAN_INTENT_ACTION = "miui.intent.action.XMAN_SHARE_MANAGER";
    private static final String XMAN_SHARE_DEFAULT_PKG = "com.miui.xman";
    private static final String XMAN_SHARE_ENABLE = "xman_share_enable";
    private static final String XMAN_SHARE_IGNORE = "xman_share_ignore";
    private static List<String> sWhiteList = new ArrayList();

    static {
        sWhiteList.add("com.android.fileexplorer");
        sWhiteList.add("com.miui.weather2");
    }

    public static boolean checkMMShare(Activity activity, Intent intent, Bundle options, int userId) {
        String str = TAG;
        if (activity == null || intent == null) {
            Log.w(str, "param is illegal.");
            return false;
        } else if (Build.IS_INTERNATIONAL_BUILD || !isXmanCloudEnable(activity.getApplicationContext()) || CrossUserUtils.getCurrentUserId() != 0) {
            return false;
        } else {
            if ((Build.IS_STABLE_VERSION || isIgnoreXmanShare(activity.getApplicationContext())) && !isXmanShareEnable(activity.getApplicationContext())) {
                return false;
            }
            try {
                str = isXmanShareFilter(activity, intent, options, userId);
                return str;
            } catch (Exception e) {
                Log.e(str, "error ", e);
                return false;
            }
        }
    }

    public static boolean isSystemApp(Context context, String packageName) {
        ApplicationInfo appinfo = null;
        try {
            appinfo = context.getPackageManager().getApplicationInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        if (appinfo == null || !isSystemApp(appinfo)) {
            return false;
        }
        return true;
    }

    public static boolean isSystemApp(ApplicationInfo info) {
        return (info.flags & 1) > 0 || UserHandle.getAppId(info.uid) < 10000;
    }

    private static String reflectGetReferrer(Activity activity) {
        try {
            Field refererField = Class.forName("android.app.Activity").getDeclaredField("mReferrer");
            refererField.setAccessible(true);
            return (String) refererField.get(activity);
        } catch (Exception e) {
            Log.e(TAG, "error ", e);
            return "";
        }
    }

    private static boolean isXmanShareFilter(Activity activity, Intent intent, Bundle options, int userId) {
        String action = intent.getAction();
        boolean equals = "android.intent.action.SEND".equals(action);
        String str = TAG;
        if (equals) {
            String type = intent.getType();
            if (TextUtils.isEmpty(type) || !type.startsWith("image/")) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("type= ");
                stringBuilder.append(type);
                Log.d(str, stringBuilder.toString());
                return false;
            }
            ComponentName cpm = intent.getComponent();
            String packageName = cpm.getPackageName();
            if ("com.tencent.mm".equals(packageName)) {
                String cmpName = cpm.getClassName();
                if (cmpName.equals("com.tencent.mm.ui.tools.ShareImgUI") || cmpName.equals("com.tencent.mm.ui.tools.ShareToTimeLineUI")) {
                    String pkg = reflectGetReferrer(activity);
                    StringBuilder stringBuilder2 = new StringBuilder();
                    String str2 = "pkg= ";
                    stringBuilder2.append(str2);
                    stringBuilder2.append(pkg);
                    Log.d(str, stringBuilder2.toString());
                    if (sWhiteList.contains(pkg) || isSystemApp(activity.getApplicationContext(), pkg)) {
                        Intent xmanIntent = new Intent(XMAN_INTENT_ACTION);
                        xmanIntent.setFlags(268435456);
                        if (!isXmanShareSupport(activity.getApplicationContext(), xmanIntent)) {
                            return false;
                        }
                        ClipData data = intent.getClipData();
                        if (data != null) {
                            xmanIntent.setClipData(data);
                            xmanIntent.addFlags(AudioSystem.DEVICE_IN_COMMUNICATION);
                        }
                        xmanIntent.putExtra("shareSource", "shareFilter");
                        xmanIntent.setPackage(XMAN_SHARE_DEFAULT_PKG);
                        xmanIntent.putExtra(Intent.EXTRA_INTENT, (Parcelable) intent);
                        CrossUserUtilsCompat.startActivityAsCaller(activity, xmanIntent, options, false, userId);
                        return true;
                    }
                    stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(str2);
                    stringBuilder2.append(pkg);
                    Log.e(str, stringBuilder2.toString());
                    return false;
                }
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("cmpName= ");
                stringBuilder3.append(cmpName);
                Log.d(str, stringBuilder3.toString());
                return false;
            }
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append("packageName= ");
            stringBuilder4.append(packageName);
            Log.d(str, stringBuilder4.toString());
            return false;
        }
        StringBuilder stringBuilder5 = new StringBuilder();
        stringBuilder5.append("action= ");
        stringBuilder5.append(action);
        Log.d(str, stringBuilder5.toString());
        return false;
    }

    private static boolean isXmanShareSupport(Context context, Intent intent) {
        boolean z = false;
        try {
            List<ResolveInfo> resolveInfoList = context.getPackageManager().queryIntentActivities(intent, 32);
            if (resolveInfoList != null && resolveInfoList.size() > 0) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            Log.i(TAG, "xman don't support", e);
            return false;
        }
    }

    private static boolean isIgnoreXmanShare(Context context) {
        return Secure.getInt(context.getContentResolver(), XMAN_SHARE_IGNORE, 0) == 1;
    }

    private static boolean isXmanShareEnable(Context context) {
        return Secure.getInt(context.getContentResolver(), XMAN_SHARE_ENABLE, 0) == 1;
    }

    private static boolean isXmanCloudEnable(Context context) {
        return Secure.getInt(context.getContentResolver(), XMAN_CLOUD_DISABLE, 1) == 0;
    }
}
