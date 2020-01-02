package miui.securityspace;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.IUserSwitchObserver;
import android.app.MiuiThemeHelper;
import android.content.ActivityNotFoundException;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.UserInfo;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.MiuiSettings;
import android.provider.Settings.Secure;
import android.util.Log;

public class CrossUserUtilsCompat {
    public static final int FLAG_XSPACE_PROFILE = 8388608;
    public static final int OWNER_SHARED_USER_GID = UserHandle.getUserGid(0);
    private static final String TAG = "CrossUserUtilsCompat";
    public static final int XSPACE_SHARED_USER_GID = UserHandle.getUserGid(999);

    public static Uri addUserIdForUri(Uri uri, int userId) {
        return userId == -1 ? uri : ContentProvider.maybeAddUserId(uri, userId);
    }

    public static Uri addUserIdForUri(Uri uri, Context context, String packageName, Intent intent) {
        int userId = intent.getIntExtra(CrossUserUtils.EXTRA_PICKED_USER_ID, -1);
        if (userId == -1 || !checkUidPermission(context, packageName)) {
            return uri;
        }
        return addUserIdForUri(uri, userId);
    }

    public static boolean checkUidPermission(Context context, String packageName) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo == null || UserHandle.getAppId(applicationInfo.uid) > 1000) {
                return false;
            }
            return true;
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (NameNotFoundException e2) {
            e2.printStackTrace();
        }
        return false;
    }

    public static Drawable getXSpaceIcon(Context context, Drawable icon, UserHandle userHandle) {
        PackageManager packageManager = context.getPackageManager();
        if (icon instanceof BitmapDrawable) {
            icon = CrossUserUtils.createDrawableWithCache(context, ((BitmapDrawable) icon).getBitmap());
        }
        return packageManager.getUserBadgedIcon(icon, userHandle);
    }

    public static void startActivityAsCaller(Activity activity, Intent intent, Bundle options, boolean ignoreTargetSecurity, int userId) {
        StrictMode.disableDeathOnFileUriExposure();
        try {
            activity.startActivityAsCaller(intent, options, null, ignoreTargetSecurity, userId);
        } catch (ActivityNotFoundException exception) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Can not find Activity.");
            stringBuilder.append(exception);
            Log.e(str, stringBuilder.toString());
        } catch (Throwable th) {
            StrictMode.enableDeathOnFileUriExposure();
        }
        StrictMode.enableDeathOnFileUriExposure();
    }

    public static Drawable getOriginalAppIcon(Context context, String pkgName) {
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(pkgName, null);
            Drawable drawable = MiuiThemeHelper.getDrawable(pm, pkgName, appinfo.name, appinfo.icon, appinfo);
            if (drawable == null) {
                drawable = pm.loadUnbadgedItemIcon(appinfo, appinfo);
            }
            return drawable;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    static boolean hasSecondSpace(Context context) {
        return Secure.getInt(context.getContentResolver(), MiuiSettings.Secure.SECOND_USER_ID, -10000) != -10000;
    }

    static boolean hasXSpaceUser(Context context) {
        for (UserInfo profile : ((UserManager) context.getSystemService("user")).getProfiles(0)) {
            if (XSpaceUserHandle.isXSpaceUser(profile)) {
                return true;
            }
        }
        return false;
    }

    public static void registerUserSwitchObserver(IUserSwitchObserver observer, String name) throws RemoteException {
        ActivityManager.getService().registerUserSwitchObserver(observer, name);
    }
}
