package android.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Process;
import android.os.UserHandle;
import android.util.Log;
import miui.maml.MamlDrawable;
import miui.os.Build;
import miui.os.MiuiInit;

public class ApplicationPackageManagerInjector {
    private static final String TAG = "ApplicationPackageManagerInjector";
    private static final String sNetflixPackage = "com.netflix.mediaclient";

    public static boolean hookGetBadgedDrawable(Drawable drawable, Drawable badgeDrawable, Rect badgeLocation) {
        if (!(drawable instanceof MamlDrawable)) {
            return false;
        }
        ((MamlDrawable) drawable).setBadgeInfo(badgeDrawable, badgeLocation);
        return true;
    }

    public static PackageInfo hookGetPackageInfo(Context context, PackageInfo pi, int flags) {
        if (Build.IS_INTERNATIONAL_BUILD && pi != null) {
            if (!sNetflixPackage.equals(pi.packageName) || !MiuiInit.isPreinstalledPackage(pi.packageName)) {
                return pi;
            }
            int appId = UserHandle.getAppId(Process.myUid());
            if (appId != UserHandle.getAppId(pi.applicationInfo.uid)) {
                return pi;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("HookPackageInfo for ");
            stringBuilder.append(pi.packageName);
            stringBuilder.append(" with version ");
            stringBuilder.append(pi.versionCode);
            stringBuilder.append(" from appId ");
            stringBuilder.append(appId);
            Log.i(TAG, stringBuilder.toString());
            ApplicationInfo applicationInfo = pi.applicationInfo;
            applicationInfo.flags |= 1;
            if (MiuiInit.getPreinstalledAppVersion(pi.packageName) < pi.versionCode) {
                applicationInfo = pi.applicationInfo;
                applicationInfo.flags |= 128;
            }
            return pi;
        }
        return pi;
    }
}
