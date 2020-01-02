package android.miui;

import android.content.res.AssetManager;
import android.content.res.MiuiResources;
import android.content.res.Resources;
import android.os.Build.VERSION;
import android.os.Process;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import java.io.File;
import miui.util.ReflectionUtils;

public class ResourcesManager {
    public static final String FRAMEWORK_EXT_RES_PATH;
    public static final String FRAMEWORK_RES_PATH = "/system/framework/framework-res.apk";
    public static final String MIUI_FRAMEWORK_RES_DATA_PATH_1 = "/data/app/com.miui.system-1.apk";
    public static final String MIUI_FRAMEWORK_RES_DATA_PATH_2 = "/data/app/com.miui.system-2.apk";
    public static final String MIUI_FRAMEWORK_RES_PATH;
    public static final String MIUI_SDK_RES_DATA_PATH_1 = "/data/app/com.miui.core-1.apk";
    public static final String MIUI_SDK_RES_DATA_PATH_2 = "/data/app/com.miui.core-2.apk";
    public static final String MIUI_SDK_RES_PATH;
    private static final boolean VERSION_ABOVE_5 = (VERSION.SDK_INT > 19);

    static {
        String str;
        if (VERSION_ABOVE_5) {
            str = "/system/app/miui/miui.apk";
        } else {
            str = "/system/app/miui.apk";
        }
        MIUI_SDK_RES_PATH = str;
        if (VERSION_ABOVE_5) {
            str = "/system/app/miuisystem/miuisystem.apk";
        } else {
            str = "/system/app/miuisystem.apk";
        }
        MIUI_FRAMEWORK_RES_PATH = str;
        if (VERSION_ABOVE_5) {
            str = "/system/framework/framework-ext-res/framework-ext-res.apk";
        } else {
            str = "/system/framework/framework-ext-res.apk";
        }
        FRAMEWORK_EXT_RES_PATH = str;
    }

    public static void addSystemAssets(AssetManager am) {
        ThreadPolicy oldPolicy = null;
        int myUid = Process.myUid();
        if (myUid != 0) {
            oldPolicy = StrictMode.allowThreadDiskReads();
        }
        String str = "getResourceName";
        ReflectionUtils.tryCallMethod(am, str, String.class, Integer.valueOf(0));
        String str2 = MIUI_SDK_RES_DATA_PATH_1;
        if (new File(str2).exists()) {
            am.addAssetPath(str2);
        } else {
            str2 = MIUI_SDK_RES_DATA_PATH_2;
            if (new File(str2).exists()) {
                am.addAssetPath(str2);
            } else {
                am.addAssetPath(MIUI_SDK_RES_PATH);
            }
        }
        str2 = MIUI_FRAMEWORK_RES_DATA_PATH_1;
        if (new File(str2).exists()) {
            am.addAssetPath(str2);
        } else {
            str2 = MIUI_FRAMEWORK_RES_DATA_PATH_2;
            if (new File(str2).exists()) {
                am.addAssetPath(str2);
            } else {
                am.addAssetPath(MIUI_FRAMEWORK_RES_PATH);
            }
        }
        am.addAssetPath(FRAMEWORK_EXT_RES_PATH);
        if (myUid != 0) {
            StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    public static void initMiuiResource(Resources res, String packageName) {
        if (res instanceof MiuiResources) {
            ((MiuiResources) res).init(packageName);
        }
    }

    public static boolean isMiuiExtFrameworkPath(String path) {
        return FRAMEWORK_EXT_RES_PATH.equals(path);
    }

    public static boolean isMiuiSystemSdkPath(String path) {
        return MIUI_FRAMEWORK_RES_PATH.equals(path) || MIUI_FRAMEWORK_RES_DATA_PATH_1.equals(path) || MIUI_FRAMEWORK_RES_DATA_PATH_2.equals(path);
    }

    public static boolean isMiuiSdkPath(String path) {
        return MIUI_SDK_RES_PATH.equals(path) || MIUI_SDK_RES_DATA_PATH_1.equals(path) || MIUI_SDK_RES_DATA_PATH_2.equals(path);
    }

    public static boolean belongToMiuiFrameworkThemePath(String path) {
        return isMiuiExtFrameworkPath(path) || isMiuiSystemSdkPath(path) || isMiuiSdkPath(path);
    }
}
