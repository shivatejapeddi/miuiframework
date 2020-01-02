package miui.securityspace;

import android.content.Intent;

public class XSpaceIntentCompat {
    public static void prepareToLeaveUser(Intent intent, int userId) {
        intent.prepareToLeaveUser(userId);
    }
}
