package android.content.pm;

import android.Manifest.permission;
import android.app.AppGlobals;
import android.content.Intent;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.ArraySet;
import android.view.inputmethod.InputMethod;
import com.android.internal.annotations.VisibleForTesting;
import java.util.ArrayList;
import java.util.List;

public class AppsQueryHelper {
    public static int GET_APPS_WITH_INTERACT_ACROSS_USERS_PERM = 2;
    public static int GET_IMES = 4;
    public static int GET_NON_LAUNCHABLE_APPS = 1;
    public static int GET_REQUIRED_FOR_SYSTEM_USER = 8;
    private List<ApplicationInfo> mAllApps;
    private final IPackageManager mPackageManager;

    public AppsQueryHelper(IPackageManager packageManager) {
        this.mPackageManager = packageManager;
    }

    public AppsQueryHelper() {
        this(AppGlobals.getPackageManager());
    }

    public List<String> queryApps(int flags, boolean systemAppsOnly, UserHandle user) {
        boolean requiredForSystemUser = false;
        boolean nonLaunchableApps = (flags & GET_NON_LAUNCHABLE_APPS) > 0;
        boolean interactAcrossUsers = (flags & GET_APPS_WITH_INTERACT_ACROSS_USERS_PERM) > 0;
        boolean imes = (flags & GET_IMES) > 0;
        if ((flags & GET_REQUIRED_FOR_SYSTEM_USER) > 0) {
            requiredForSystemUser = true;
        }
        if (this.mAllApps == null) {
            this.mAllApps = getAllApps(user.getIdentifier());
        }
        List<String> result = new ArrayList();
        int allAppsSize;
        int i;
        ApplicationInfo appInfo;
        if (flags == 0) {
            allAppsSize = this.mAllApps.size();
            for (i = 0; i < allAppsSize; i++) {
                appInfo = (ApplicationInfo) this.mAllApps.get(i);
                if (!systemAppsOnly || appInfo.isSystemApp()) {
                    result.add(appInfo.packageName);
                }
            }
            return result;
        }
        int i2;
        if (nonLaunchableApps) {
            int i3;
            List<ResolveInfo> resolveInfos = queryIntentActivitiesAsUser(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER), user.getIdentifier());
            ArraySet<String> appsWithLaunchers = new ArraySet();
            int resolveInfosSize = resolveInfos.size();
            for (i3 = 0; i3 < resolveInfosSize; i3++) {
                appsWithLaunchers.add(((ResolveInfo) resolveInfos.get(i3)).activityInfo.packageName);
            }
            i3 = this.mAllApps.size();
            for (int i4 = 0; i4 < i3; i4++) {
                ApplicationInfo appInfo2 = (ApplicationInfo) this.mAllApps.get(i4);
                if (!systemAppsOnly || appInfo2.isSystemApp()) {
                    String packageName = appInfo2.packageName;
                    if (!appsWithLaunchers.contains(packageName)) {
                        result.add(packageName);
                    }
                }
            }
        }
        if (interactAcrossUsers) {
            List<PackageInfo> packagesHoldingPermissions = getPackagesHoldingPermission(permission.INTERACT_ACROSS_USERS, user.getIdentifier());
            i = packagesHoldingPermissions.size();
            for (i2 = 0; i2 < i; i2++) {
                PackageInfo packageInfo = (PackageInfo) packagesHoldingPermissions.get(i2);
                if ((!systemAppsOnly || packageInfo.applicationInfo.isSystemApp()) && !result.contains(packageInfo.packageName)) {
                    result.add(packageInfo.packageName);
                }
            }
        }
        if (imes) {
            List<ResolveInfo> resolveInfos2 = queryIntentServicesAsUser(new Intent(InputMethod.SERVICE_INTERFACE), user.getIdentifier());
            i = resolveInfos2.size();
            for (i2 = 0; i2 < i; i2++) {
                ServiceInfo serviceInfo = ((ResolveInfo) resolveInfos2.get(i2)).serviceInfo;
                if ((!systemAppsOnly || serviceInfo.applicationInfo.isSystemApp()) && !result.contains(serviceInfo.packageName)) {
                    result.add(serviceInfo.packageName);
                }
            }
        }
        if (requiredForSystemUser) {
            allAppsSize = this.mAllApps.size();
            for (i = 0; i < allAppsSize; i++) {
                appInfo = (ApplicationInfo) this.mAllApps.get(i);
                if ((!systemAppsOnly || appInfo.isSystemApp()) && appInfo.isRequiredForSystemUser()) {
                    result.add(appInfo.packageName);
                }
            }
        }
        return result;
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public List<ApplicationInfo> getAllApps(int userId) {
        try {
            return this.mPackageManager.getInstalledApplications(8704, userId).getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public List<ResolveInfo> queryIntentActivitiesAsUser(Intent intent, int userId) {
        try {
            return this.mPackageManager.queryIntentActivities(intent, null, 795136, userId).getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public List<ResolveInfo> queryIntentServicesAsUser(Intent intent, int userId) {
        try {
            return this.mPackageManager.queryIntentServices(intent, null, 819328, userId).getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /* Access modifiers changed, original: protected */
    @VisibleForTesting
    public List<PackageInfo> getPackagesHoldingPermission(String perm, int userId) {
        try {
            return this.mPackageManager.getPackagesHoldingPermissions(new String[]{perm}, 0, userId).getList();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}
