package miui.security.appcompatibility;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import java.util.List;
import miui.security.SecurityManager;

public class AppCompatibilityManager {
    public static final String ACTION_BROADCAST_APPCOMPATIBILITY_UPDATE = "com.miui.action.appcompatibility.update";
    public static final String DEVICE_NAME = Build.DEVICE;
    private static final String INTENT_ACTION_SHOW_DIALOG_FOR_APPSTORE = "com.miui.appcompatibility.LaunchDialog.appstore";
    private static final String INTENT_ACTION_SHOW_DIALOG_FOR_LAUNCHER = "com.miui.appcompatibility.LaunchDialog.launcher";
    private static final String INTENT_EXTRA_APPNAME = "app_name";

    private static SecurityManager getSecurityManager(Context context) {
        return (SecurityManager) context.getSystemService(Context.SECURITY_SERVICE);
    }

    public static void setIncompatibleAppList(Context context, List<String> list) {
        if (list != null) {
            getSecurityManager(context).setIncompatibleAppList(list);
        }
    }

    public static List<String> getIncompatibleAppList(Context context) {
        return getSecurityManager(context).getIncompatibleAppList();
    }

    public static boolean isAppCompatible(Context context, String pkgName) {
        for (String pkgname : getSecurityManager(context).getIncompatibleAppList()) {
            if (pkgname.equals(pkgName)) {
                return false;
            }
        }
        return true;
    }

    public static Intent getAppErrorTipsDialogIntentForLauncher(String appname) {
        Intent intent = new Intent(INTENT_ACTION_SHOW_DIALOG_FOR_LAUNCHER);
        if (!TextUtils.isEmpty(appname)) {
            intent.putExtra(INTENT_EXTRA_APPNAME, appname);
        }
        return intent;
    }

    public static Intent getAppErrorTipsDialogIntentForApptore(String appname) {
        Intent intent = new Intent(INTENT_ACTION_SHOW_DIALOG_FOR_APPSTORE);
        if (!TextUtils.isEmpty(appname)) {
            intent.putExtra(INTENT_EXTRA_APPNAME, appname);
        }
        return intent;
    }
}
