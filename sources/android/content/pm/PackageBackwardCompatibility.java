package android.content.pm;

import android.content.pm.PackageParser.Package;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@VisibleForTesting
public class PackageBackwardCompatibility extends PackageSharedLibraryUpdater {
    private static final PackageBackwardCompatibility INSTANCE;
    private static final String TAG = PackageBackwardCompatibility.class.getSimpleName();
    private final boolean mBootClassPathContainsATB;
    private final PackageSharedLibraryUpdater[] mPackageUpdaters;

    @VisibleForTesting
    public static class AndroidTestRunnerSplitUpdater extends PackageSharedLibraryUpdater {
        public void updatePackage(Package pkg) {
            prefixImplicitDependency(pkg, "android.test.runner", "android.test.mock");
        }
    }

    @VisibleForTesting
    public static class RemoveUnnecessaryAndroidTestBaseLibrary extends PackageSharedLibraryUpdater {
        public void updatePackage(Package pkg) {
            PackageSharedLibraryUpdater.removeLibrary(pkg, "android.test.base");
        }
    }

    @VisibleForTesting
    public static class RemoveUnnecessaryOrgApacheHttpLegacyLibrary extends PackageSharedLibraryUpdater {
        public void updatePackage(Package pkg) {
            PackageSharedLibraryUpdater.removeLibrary(pkg, SharedLibraryNames.ORG_APACHE_HTTP_LEGACY);
        }
    }

    static {
        List<PackageSharedLibraryUpdater> packageUpdaters = new ArrayList();
        packageUpdaters.add(new OrgApacheHttpLegacyUpdater());
        packageUpdaters.add(new AndroidHidlUpdater());
        packageUpdaters.add(new AndroidTestRunnerSplitUpdater());
        INSTANCE = new PackageBackwardCompatibility(addOptionalUpdater(packageUpdaters, "android.content.pm.AndroidTestBaseUpdater", -$$Lambda$jpya2qgMDDEok2GAoKRDqPM5lIE.INSTANCE) ^ 1, (PackageSharedLibraryUpdater[]) packageUpdaters.toArray(new PackageSharedLibraryUpdater[0]));
    }

    private static boolean addOptionalUpdater(List<PackageSharedLibraryUpdater> packageUpdaters, String className, Supplier<PackageSharedLibraryUpdater> defaultUpdater) {
        Class<? extends PackageSharedLibraryUpdater> clazz;
        PackageSharedLibraryUpdater updater;
        String str;
        StringBuilder stringBuilder;
        try {
            clazz = PackageBackwardCompatibility.class.getClassLoader().loadClass(className).asSubclass(PackageSharedLibraryUpdater.class);
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Loaded ");
            stringBuilder.append(className);
            Log.i(str, stringBuilder.toString());
        } catch (ClassNotFoundException e) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Could not find ");
            stringBuilder.append(className);
            stringBuilder.append(", ignoring");
            Log.i(str, stringBuilder.toString());
            clazz = null;
        }
        boolean usedOptional = false;
        if (clazz == null) {
            updater = (PackageSharedLibraryUpdater) defaultUpdater.get();
        } else {
            try {
                updater = (PackageSharedLibraryUpdater) clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
                usedOptional = true;
            } catch (ReflectiveOperationException e2) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Could not create instance of ");
                stringBuilder2.append(className);
                throw new IllegalStateException(stringBuilder2.toString(), e2);
            }
        }
        packageUpdaters.add(updater);
        return usedOptional;
    }

    @VisibleForTesting
    public static PackageSharedLibraryUpdater getInstance() {
        return INSTANCE;
    }

    private PackageBackwardCompatibility(boolean bootClassPathContainsATB, PackageSharedLibraryUpdater[] packageUpdaters) {
        this.mBootClassPathContainsATB = bootClassPathContainsATB;
        this.mPackageUpdaters = packageUpdaters;
    }

    @VisibleForTesting
    public static void modifySharedLibraries(Package pkg) {
        INSTANCE.updatePackage(pkg);
    }

    public void updatePackage(Package pkg) {
        for (PackageSharedLibraryUpdater packageUpdater : this.mPackageUpdaters) {
            packageUpdater.updatePackage(pkg);
        }
    }

    @VisibleForTesting
    public static boolean bootClassPathContainsATB() {
        return INSTANCE.mBootClassPathContainsATB;
    }
}
