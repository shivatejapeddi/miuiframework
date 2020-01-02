package miui.securityspace;

import android.content.Context;
import android.content.pm.IPackageManager.Stub;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;

public class XSpaceUserHandle {
    public static final String EXTRA_AUTH_CALL_XSPACE = "android.intent.extra.auth_to_call_xspace";
    public static final int FLAG_XSPACE_PROFILE = 8388608;
    public static final int OWNER_SHARED_USER_GID = CrossUserUtilsCompat.OWNER_SHARED_USER_GID;
    public static final int USER_XSPACE = 999;
    public static final int XSPACE_ICON_MASK_ID = 285671584;
    public static final int XSPACE_SHARED_USER_GID = CrossUserUtilsCompat.XSPACE_SHARED_USER_GID;

    public static int checkAndGetXSpaceUserId(int flags, int defUserId) {
        if (isXSpaceUserFlag(flags)) {
            return 999;
        }
        if (isXSpaceUserId(defUserId)) {
            return defUserId + 1;
        }
        return defUserId;
    }

    public static boolean isXSpaceUserId(int userId) {
        return userId == 999;
    }

    public static boolean isXSpaceUserCalling() {
        return isXSpaceUserId(UserHandle.getCallingUserId());
    }

    public static boolean isSelfXSpaceUser() {
        return isXSpaceUserId(UserHandle.getUserId(Process.myUid()));
    }

    public static boolean isXSpaceUser(UserInfo userinfo) {
        return userinfo != null ? isXSpaceUserFlag(userinfo.flags) : false;
    }

    public static boolean isXSpaceUser(UserHandle userHandle) {
        return userHandle != null ? isXSpaceUserId(userHandle.getIdentifier()) : false;
    }

    public static boolean isUidBelongtoXSpace(int uid) {
        return isXSpaceUserId(UserHandle.getUserId(uid));
    }

    public static boolean isXSpaceUserFlag(int flags) {
        return (flags & 8388608) == 8388608;
    }

    public static Drawable getXSpaceIcon(Context context, Drawable icon, UserHandle userHandle) {
        if (isXSpaceUser(userHandle)) {
            return CrossUserUtilsCompat.getXSpaceIcon(context, icon, userHandle);
        }
        return icon;
    }

    public static Drawable getXSpaceIcon(Context context, Drawable icon, int uid) {
        return getXSpaceIcon(context, icon, new UserHandle(UserHandle.getUserId(uid)));
    }

    public static Drawable getXSpaceIcon(Context context, Drawable icon) {
        return getXSpaceIcon(context, icon, new UserHandle(999));
    }

    public static boolean isAppInXSpace(Context context, String pkgName) {
        boolean z = false;
        if (context == null || pkgName == null || context.getApplicationContext() == null || context.getApplicationContext().getContentResolver() == null) {
            return false;
        }
        try {
            if (Stub.asInterface(ServiceManager.getService("package")).getPackageInfo(pkgName, 0, 999) != null) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            return false;
        }
    }
}
