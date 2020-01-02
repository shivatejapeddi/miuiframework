package com.miui.hybrid.hook;

import android.content.pm.PackageInfo;
import android.text.TextUtils;
import android.util.Slog;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PkgInfoHook {
    public static final String TAG = "PkgInfoHook";
    private static volatile PkgInfoHook sInstance;
    private Map<String, PackageInfo> fakeData = new ConcurrentHashMap();

    public static PkgInfoHook getInstance() {
        if (sInstance == null) {
            synchronized (PkgInfoHook.class) {
                if (sInstance == null) {
                    sInstance = new PkgInfoHook();
                }
            }
        }
        return sInstance;
    }

    private PkgInfoHook() {
    }

    public boolean insert(PackageInfo packageInfo) {
        String str = TAG;
        if (packageInfo == null || TextUtils.isEmpty(packageInfo.packageName)) {
            Slog.e(str, "PkgInfoHook.insert(PackageInfo) failed.");
            return false;
        }
        this.fakeData.put(packageInfo.packageName, packageInfo);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("insert ");
        stringBuilder.append(packageInfo.packageName);
        Slog.v(str, stringBuilder.toString());
        return true;
    }

    public PackageInfo hook(PackageInfo pkgInfo, String pkgName, int flags) {
        if (!TextUtils.isEmpty(pkgName)) {
            PackageInfo newPkgInfo = (PackageInfo) this.fakeData.get(pkgName);
            if (newPkgInfo != null) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("hook ");
                stringBuilder.append(pkgName);
                Slog.d(TAG, stringBuilder.toString());
                return newPkgInfo;
            }
        }
        return pkgInfo;
    }

    public PackageInfo delete(String pkgName) {
        boolean isEmpty = TextUtils.isEmpty(pkgName);
        String str = TAG;
        if (isEmpty) {
            Slog.e(str, "Expect non-null pkgName.");
            return null;
        }
        PackageInfo pInfo = (PackageInfo) this.fakeData.remove(pkgName);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("remove ");
        stringBuilder.append(pkgName);
        stringBuilder.append(" pInfo:");
        stringBuilder.append(pInfo);
        Slog.v(str, stringBuilder.toString());
        return pInfo;
    }
}
