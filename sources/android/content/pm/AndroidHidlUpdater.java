package android.content.pm;

import android.content.pm.PackageParser.Package;
import com.android.internal.annotations.VisibleForTesting;

@VisibleForTesting
public class AndroidHidlUpdater extends PackageSharedLibraryUpdater {
    public void updatePackage(Package pkg) {
        ApplicationInfo info = pkg.applicationInfo;
        boolean isSystem = true;
        boolean isLegacy = info.targetSdkVersion <= 28;
        if (!(info.isSystemApp() || info.isUpdatedSystemApp())) {
            isSystem = false;
        }
        String str = "android.hidl.manager-V1.0-java";
        String str2 = "android.hidl.base-V1.0-java";
        if (isLegacy && isSystem) {
            prefixRequiredLibrary(pkg, str2);
            prefixRequiredLibrary(pkg, str);
            return;
        }
        PackageSharedLibraryUpdater.removeLibrary(pkg, str2);
        PackageSharedLibraryUpdater.removeLibrary(pkg, str);
    }
}
