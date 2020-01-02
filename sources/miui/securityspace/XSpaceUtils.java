package miui.securityspace;

import android.app.AppGlobals;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageInfo;
import android.content.pm.ParceledListSlice;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.MiuiSettings.SettingsCloudData;
import android.text.TextUtils;
import android.util.Log;
import com.miui.enterprise.ApplicationHelper;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;

public class XSpaceUtils {
    public static final String EXTRA_XSPACE_ACTUAL_USERID = "userId";
    private static final String TAG = "XSpaceUtils";
    private static final String XSPACE_BLACK_APPS_KEY = "pkgName";
    private static final String XSPACE_CLOUD_CONTROL_MODULE_NAME = "XSpace";
    private static final String XSPACE_WHITE_APPS_KEY = "whiteList";

    public static ArrayList<String> getXSpaceBlackApps(Context context) {
        String str = TAG;
        ArrayList<String> XSpaceBlackApps = new ArrayList();
        try {
            String data = SettingsCloudData.getCloudDataString(context.getContentResolver(), XSPACE_CLOUD_CONTROL_MODULE_NAME, XSPACE_BLACK_APPS_KEY, null);
            if (TextUtils.isEmpty(data)) {
                XSpaceBlackApps = XSpaceConstant.XSPACE_DEFAULT_BLACK_LIST;
            } else {
                JSONArray apps = new JSONArray(data);
                for (int i = 0; i < apps.length(); i++) {
                    XSpaceBlackApps.add(apps.getString(i));
                }
            }
            for (String pkg : ApplicationHelper.getXSpaceBlackApps(context)) {
                if (!XSpaceBlackApps.contains(pkg)) {
                    XSpaceBlackApps.add(pkg);
                }
            }
        } catch (JSONException e) {
            Log.e(str, "JSONException when get XSpaceBlackApps :", e);
        } catch (Exception e2) {
            Log.e(str, "Exception when get XSpaceBlackApps :", e2);
        }
        return XSpaceBlackApps;
    }

    public static ArrayList<String> getXSpaceWhiteApps(Context context) {
        String str = TAG;
        ArrayList<String> whiteListApps = new ArrayList();
        try {
            String data = SettingsCloudData.getCloudDataString(context.getContentResolver(), XSPACE_CLOUD_CONTROL_MODULE_NAME, XSPACE_WHITE_APPS_KEY, null);
            if (TextUtils.isEmpty(data)) {
                whiteListApps = XSpaceConstant.XSPACE_WHITELIST;
            } else {
                JSONArray apps = new JSONArray(data);
                for (int i = 0; i < apps.length(); i++) {
                    whiteListApps.add(apps.getString(i));
                }
            }
            whiteListApps.removeAll(ApplicationHelper.getXSpaceBlackApps(context));
        } catch (JSONException e) {
            Log.e(str, "JSONException when get XSpaceWhiteApps :", e);
        } catch (Exception e2) {
            Log.e(str, "Exception when get XSpaceWhiteApps :", e2);
        }
        return whiteListApps;
    }

    public static ArrayList<String> getXSpaceSupportPackages(Context context) {
        ArrayList<String> XSpaceSupportApps = new ArrayList();
        ArrayList<String> XSpaceBlackApps = getXSpaceBlackApps(context);
        ParceledListSlice<PackageInfo> slice = null;
        try {
            slice = AppGlobals.getPackageManager().getInstalledPackages(0, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (slice != null) {
            for (PackageInfo pkgInfo : slice.getList()) {
                if (XSpaceBlackApps == null || !XSpaceBlackApps.contains(pkgInfo.packageName)) {
                    if (!isSystemApp(pkgInfo.applicationInfo) && pkgHasIcon(context, pkgInfo.packageName)) {
                        XSpaceSupportApps.add(pkgInfo.packageName);
                    }
                }
            }
        }
        return XSpaceSupportApps;
    }

    public static boolean isSystemApp(ApplicationInfo info) {
        return (info.flags & 1) > 0 || info.uid < 10000;
    }

    public static boolean pkgHasIcon(Context context, String pkgName) {
        return ((LauncherApps) context.getSystemService(Context.LAUNCHER_APPS_SERVICE)).getActivityList(pkgName, UserHandle.OWNER).isEmpty() ^ 1;
    }

    public static boolean isAppInXSpaceWhiltList(String pkgName) {
        return XSpaceConstant.XSPACE_WHITELIST.contains(pkgName);
    }

    public static boolean isAppInXSpaceWhiltList(Context context, String pkgName) {
        ArrayList<String> whiteListApps = getXSpaceWhiteApps(context);
        if (whiteListApps == null || whiteListApps.isEmpty() || pkgName == null) {
            return false;
        }
        return whiteListApps.contains(pkgName);
    }

    public static boolean isAppInXSpaceSupportList(Context context, String pkgName) {
        if (context == null || pkgName == null) {
            return false;
        }
        ArrayList<String> xSpaceSupportPackages = getXSpaceSupportPackages(context);
        if (xSpaceSupportPackages != null) {
            return xSpaceSupportPackages.contains(pkgName);
        }
        return false;
    }
}
