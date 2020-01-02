package android.content.pm;

import android.app.ActivityThread;
import android.app.Application;
import android.os.Build.VERSION;
import java.util.HashSet;
import java.util.Set;
import miui.os.Build;

public class PackageManagerInjector {
    private static Set<String> sCtsPackage = new HashSet();

    static {
        sCtsPackage.add("com.android.cts.usepermission");
        sCtsPackage.add("com.android.cts.permissionapp");
        sCtsPackage.add("android.permission.cts.appthatrequestpermission");
    }

    public static String getPermissionControllerPackageName() {
        try {
            Application application = ActivityThread.currentApplication();
            String currentPackageName = ActivityThread.currentPackageName();
            if (!Build.IS_INTERNATIONAL_BUILD && (!Build.IS_TABLET || VERSION.SDK_INT >= 26)) {
                if (!sCtsPackage.contains(currentPackageName)) {
                    return "com.lbe.security.miui";
                }
            }
            return application.getPackageManager().getPermissionControllerPackageName();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
