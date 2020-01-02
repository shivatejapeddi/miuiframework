package miui.securitycenter.powercenter;

import android.os.UserHandle;

class UidUtils {
    UidUtils() {
    }

    static int getRealUid(int uid) {
        if (isSharedGid(uid)) {
            return UserHandle.getUid(0, UserHandle.getAppIdFromSharedAppGid(uid));
        }
        return uid;
    }

    private static boolean isSharedGid(int uid) {
        return UserHandle.getAppIdFromSharedAppGid(uid) > 0;
    }
}
