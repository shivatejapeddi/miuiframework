package miui.theme;

import android.content.Context;
import android.os.FileUtils;
import android.os.SystemProperties;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import java.io.File;
import miui.util.FeatureParser;

public class DeviceCustomized {
    private static boolean isDeviceIsProvisioned(Context context) {
        return Secure.getInt(context.getContentResolver(), "device_provisioned", 0) != 0;
    }

    private static void createThemeRuntimeFolder() {
        String str = "/data/system/theme/compatibility-v11/";
        new File(str).mkdirs();
        FileUtils.setPermissions("/data/system/theme/", 493, -1, -1);
        FileUtils.setPermissions(str, 493, -1, -1);
    }

    public static void setCustomizedWallpaper(Context context, File userWallpaperFile) {
        if (context != null && userWallpaperFile != null) {
            String devicePanelColor = SystemProperties.get("sys.panel.color", "");
            if (!TextUtils.isEmpty(devicePanelColor) && !isDeviceIsProvisioned(context)) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("customized_wallpaper_");
                stringBuilder.append(devicePanelColor);
                String customizedWallpaper = FeatureParser.getString(stringBuilder.toString());
                if (!TextUtils.isEmpty(customizedWallpaper) && new File(customizedWallpaper).exists()) {
                    FileUtils.copyFile(new File(customizedWallpaper), userWallpaperFile);
                }
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("customized_lockscreen_");
                stringBuilder2.append(devicePanelColor);
                String customizedLockscreen = FeatureParser.getString(stringBuilder2.toString());
                if (!TextUtils.isEmpty(customizedLockscreen) && new File(customizedLockscreen).exists()) {
                    createThemeRuntimeFolder();
                    FileUtils.copyFile(new File(customizedLockscreen), new File("/data/system/theme/lock_wallpaper"));
                }
            }
        }
    }
}
