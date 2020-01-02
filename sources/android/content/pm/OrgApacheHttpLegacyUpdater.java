package android.content.pm;

import android.content.pm.PackageParser.Package;
import com.android.internal.annotations.VisibleForTesting;

@VisibleForTesting
public class OrgApacheHttpLegacyUpdater extends PackageSharedLibraryUpdater {
    private static boolean apkTargetsApiLevelLessThanOrEqualToOMR1(Package pkg) {
        return pkg.applicationInfo.targetSdkVersion < 28;
    }

    public void updatePackage(Package pkg) {
        if (apkTargetsApiLevelLessThanOrEqualToOMR1(pkg)) {
            prefixRequiredLibrary(pkg, SharedLibraryNames.ORG_APACHE_HTTP_LEGACY);
        }
    }
}
