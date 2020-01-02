package android.content.res;

import android.miui.ResourcesManager;
import android.os.Build.VERSION;
import android.util.Log;
import java.io.IOException;
import java.util.ArrayList;

class AssetManagerInjector {
    private static String TAG = "AssetManagerInjector";

    private static native String[] nativeCreateIdmapsForStaticOverlaysTargetingMiui();

    AssetManagerInjector() {
    }

    static void addMiuiAssets(ArrayList<ApkAssets> apkAssets) throws IOException {
        apkAssets.add(ApkAssets.loadFromPath(ResourcesManager.FRAMEWORK_EXT_RES_PATH, true));
        apkAssets.add(ApkAssets.loadFromPath(ResourcesManager.MIUI_FRAMEWORK_RES_PATH, true));
        apkAssets.add(ApkAssets.loadFromPath(ResourcesManager.MIUI_SDK_RES_PATH, true));
        if (VERSION.SDK_INT > 28) {
            String[] systemIdmapPaths = nativeCreateIdmapsForStaticOverlaysTargetingMiui();
            if (systemIdmapPaths != null) {
                for (String idmapPath : systemIdmapPaths) {
                    apkAssets.add(ApkAssets.loadOverlayFromPath(idmapPath, true));
                }
                return;
            }
            Log.w(TAG, "'idmap2 --scan' failed: no static=\"true\" overlays targeting \"miui\" will be loaded");
        }
    }
}
