package android.content.pm;

import android.content.pm.PackageManagerInternal.PackageListObserver;
import com.android.server.LocalServices;
import java.util.List;

public class PackageList implements PackageListObserver, AutoCloseable {
    private final List<String> mPackageNames;
    private final PackageListObserver mWrappedObserver;

    public PackageList(List<String> packageNames, PackageListObserver observer) {
        this.mPackageNames = packageNames;
        this.mWrappedObserver = observer;
    }

    public void onPackageAdded(String packageName, int uid) {
        PackageListObserver packageListObserver = this.mWrappedObserver;
        if (packageListObserver != null) {
            packageListObserver.onPackageAdded(packageName, uid);
        }
    }

    public void onPackageChanged(String packageName, int uid) {
        PackageListObserver packageListObserver = this.mWrappedObserver;
        if (packageListObserver != null) {
            packageListObserver.onPackageChanged(packageName, uid);
        }
    }

    public void onPackageRemoved(String packageName, int uid) {
        PackageListObserver packageListObserver = this.mWrappedObserver;
        if (packageListObserver != null) {
            packageListObserver.onPackageRemoved(packageName, uid);
        }
    }

    public void close() throws Exception {
        ((PackageManagerInternal) LocalServices.getService(PackageManagerInternal.class)).removePackageListObserver(this);
    }

    public List<String> getPackageNames() {
        return this.mPackageNames;
    }
}
