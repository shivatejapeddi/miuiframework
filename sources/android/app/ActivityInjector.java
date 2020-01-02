package android.app;

import android.content.Context;
import miui.security.SecurityManager;

class ActivityInjector {
    ActivityInjector() {
    }

    static void checkAccessControl(Activity activity) {
        SecurityManager securityManager = (SecurityManager) activity.getSystemService(Context.SECURITY_SERVICE);
        if (securityManager != null) {
            securityManager.checkAccessControl(activity, activity.getUserId());
        }
    }
}
