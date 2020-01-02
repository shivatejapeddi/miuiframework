package miui.theme;

import android.miui.Shell;
import android.os.MiuiProcess;
import android.os.SELinux;
import android.util.Log;
import java.io.File;
import miui.content.res.ThemeResources;

public class ThemePermissionUtils {
    private static final String THEME_FILE_CONTEXT = "u:object_r:theme_data_file:s0";

    public static boolean updateFilePermissionWithThemeContext(String path) {
        return updateFilePermissionWithThemeContext(path, true);
    }

    public static boolean updateFilePermissionWithThemeContext(String path, boolean systemReady) {
        boolean z = false;
        if (path == null) {
            return false;
        }
        File themeRoot = null;
        int mode = -1;
        String str = "/data/system/theme/";
        if (path.startsWith(str)) {
            themeRoot = new File(str);
            mode = 509;
        } else if (path.startsWith(ThemeResources.THEME_MAGIC_PATH)) {
            themeRoot = new File(ThemeResources.THEME_MAGIC_PATH);
            mode = 509;
        }
        File destFile = new File(path);
        if (themeRoot == null || !destFile.exists() || destFile.getAbsolutePath().equals(themeRoot.getAbsolutePath())) {
            return false;
        }
        boolean ret = false;
        String str2 = "Theme";
        String str3 = THEME_FILE_CONTEXT;
        if (systemReady) {
            boolean z2 = Shell.chmod(path, mode) && Shell.chown(path, MiuiProcess.THEME_UID, MiuiProcess.THEME_UID);
            ret = z2;
            if (Shell.setfilecon(path, str3) && ret) {
                z = true;
            }
            ret = z;
        } else {
            try {
                if (SELinux.setFileContext(path, str3) && null != null) {
                    z = true;
                }
                ret = z;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("system user update theme file: ");
                stringBuilder.append(path);
                stringBuilder.append("  ");
                stringBuilder.append(ret);
                Log.i(str2, stringBuilder.toString());
            } catch (Exception e) {
                e.printStackTrace();
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("occur exception when updating theme file: ");
                stringBuilder2.append(path);
                Log.i(str2, stringBuilder2.toString());
            }
        }
        return ret;
    }
}
