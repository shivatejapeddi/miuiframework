package miui.securitycenter.utils;

import android.app.ActivityManager;
import android.app.ActivityManagerNative;
import android.app.AppGlobals;
import android.app.StatusBarManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.content.res.XmlResourceParser;
import android.os.Build.VERSION;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.R;

public class SecurityCenterHelper {
    private SecurityCenterHelper() {
    }

    public static UserHandle getUserAll() {
        return UserHandle.ALL;
    }

    public static int getUserId(int uid) {
        return UserHandle.getUserId(uid);
    }

    public static boolean packageHasActiveAdmins(DevicePolicyManager dpm, String packageName) {
        return dpm.packageHasActiveAdmins(packageName);
    }

    public static XmlResourceParser getApnsXml(Context context) {
        try {
            return context.getResources().getXml(R.xml.apns);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void forceStopPackage(ActivityManager am, String pkgName) {
        am.forceStopPackage(pkgName);
        try {
            if (UserHandle.myUserId() == 0 && VERSION.SDK_INT >= 20 && AppGlobals.getPackageManager().getApplicationInfo(pkgName, null, 999) != null) {
                ActivityManagerNative.getDefault().forceStopPackage(pkgName, 999);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void collapseStatusPanels(Context context) {
        ((StatusBarManager) context.getSystemService(Context.STATUS_BAR_SERVICE)).collapsePanels();
    }

    public static long[] getProcessPss(int[] pids) {
        try {
            return ActivityManagerNative.getDefault().getProcessPss(pids);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getLabel(Context context, String filePath, ApplicationInfo info) {
        Resources pRes = context.getResources();
        AssetManager assmgr = null;
        CharSequence label = null;
        try {
            assmgr = new AssetManager();
            assmgr.addAssetPath(filePath);
            Resources res = new Resources(assmgr, pRes.getDisplayMetrics(), pRes.getConfiguration());
            if (info.labelRes != 0) {
                try {
                    label = res.getText(info.labelRes);
                } catch (NotFoundException e) {
                }
            }
            if (label == null) {
                label = info.nonLocalizedLabel != null ? info.nonLocalizedLabel : info.packageName;
            }
            String charSequence = label.toString();
            assmgr.close();
            return charSequence;
        } catch (Exception e2) {
            Log.e("getLabel", "getLabel error");
            return "";
        } finally {
            if (assmgr != null) {
                assmgr.close();
            }
        }
    }

    public static int getBrightnessDimInt(Context context) {
        return context.getResources().getInteger(android.miui.R.integer.android_config_screenBrightnessDim);
    }

    public static boolean isAutomaticBrightnessAvailable(Context context) {
        return context.getResources().getBoolean(android.miui.R.bool.android_config_automatic_brightness_available);
    }
}
