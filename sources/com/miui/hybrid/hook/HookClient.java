package com.miui.hybrid.hook;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.util.Slog;

public class HookClient {
    private static final String TAG = "HookClient";

    public static PackageInfo hookPkgInfo(PackageInfo packageInfo, String pkgName, int flags) {
        PackageInfo hookPkgInfo = packageInfo;
        try {
            return PkgInfoHook.getInstance().hook(packageInfo, pkgName, flags);
        } catch (Throwable e) {
            e.printStackTrace();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception happened when hookPackageInfo for ");
            stringBuilder.append(pkgName);
            Slog.e(TAG, stringBuilder.toString());
            return hookPkgInfo;
        }
    }

    public static Intent redirectStartActivity(Intent intent, String callingPackage) {
        Intent redirectIntent = intent;
        try {
            return IntentHook.getInstance().redirect(intent, callingPackage);
        } catch (Throwable e) {
            e.printStackTrace();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception happened when redirect intent for ");
            stringBuilder.append(callingPackage);
            Slog.e(TAG, stringBuilder.toString());
            return redirectIntent;
        }
    }

    public static String hookGetCallingPkg(String hostApp, String originCallingPkg) {
        String hookCallingPKg = originCallingPkg;
        try {
            return CallingPkgHook.getInstance().getHookCallingPkg(hostApp, originCallingPkg);
        } catch (Throwable e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception happened when hook getCallingPkg for hostApp:");
            stringBuilder.append(hostApp);
            stringBuilder.append(", originCallingPkg:");
            stringBuilder.append(originCallingPkg);
            Slog.e(TAG, stringBuilder.toString(), e);
            return hookCallingPKg;
        }
    }
}
