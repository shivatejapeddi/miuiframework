package com.android.internal.os;

import android.app.AppGlobals;
import android.content.pm.PackageInfo;
import android.os.RemoteException;
import android.os.UserHandle;
import com.android.internal.annotations.VisibleForTesting;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AppIdToPackageMap {
    private final Map<Integer, String> mAppIdToPackageMap;

    @VisibleForTesting
    public AppIdToPackageMap(Map<Integer, String> appIdToPackageMap) {
        this.mAppIdToPackageMap = appIdToPackageMap;
    }

    public static AppIdToPackageMap getSnapshot() {
        try {
            List<PackageInfo> packages = AppGlobals.getPackageManager().getInstalledPackages(794624, 0).getList();
            Map<Integer, String> map = new HashMap();
            for (PackageInfo pkg : packages) {
                int uid = pkg.applicationInfo.uid;
                if (pkg.sharedUserId == null || !map.containsKey(Integer.valueOf(uid))) {
                    map.put(Integer.valueOf(uid), pkg.packageName);
                } else {
                    Integer valueOf = Integer.valueOf(uid);
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("shared:");
                    stringBuilder.append(pkg.sharedUserId);
                    map.put(valueOf, stringBuilder.toString());
                }
            }
            return new AppIdToPackageMap(map);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String mapAppId(int appId) {
        String pkgName = (String) this.mAppIdToPackageMap.get(Integer.valueOf(appId));
        return pkgName == null ? String.valueOf(appId) : pkgName;
    }

    public String mapUid(int uid) {
        String pkgName = (String) this.mAppIdToPackageMap.get(Integer.valueOf(UserHandle.getAppId(uid)));
        String uidStr = UserHandle.formatUid(uid);
        if (pkgName == null) {
            return uidStr;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(pkgName);
        stringBuilder.append('/');
        stringBuilder.append(uidStr);
        return stringBuilder.toString();
    }
}
