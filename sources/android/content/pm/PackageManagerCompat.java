package android.content.pm;

import android.content.pm.PackageManager.NameNotFoundException;
import java.util.List;

public class PackageManagerCompat {
    public static int getPackageUidAsUser(PackageManager pm, String packageName, int userId) throws NameNotFoundException {
        return pm.getPackageUidAsUser(packageName, 8192, userId);
    }

    public static List<PackageInfo> getInstalledPackagesAsUser(PackageManager pm, int flags, int userId) {
        return pm.getInstalledPackagesAsUser(flags, userId);
    }

    public static String getDefaultBrowserPackageNameAsUser(PackageManager pm, int userId) {
        return pm.getDefaultBrowserPackageNameAsUser(userId);
    }

    public static void setDefaultBrowserPackageNameAsUser(PackageManager pm, String packageName, int userId) {
        pm.setDefaultBrowserPackageNameAsUser(packageName, userId);
    }
}
