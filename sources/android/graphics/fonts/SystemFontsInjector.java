package android.graphics.fonts;

import java.io.File;

public class SystemFontsInjector {
    private static final String ANDROID_ROOT = "/system";
    private static final String MIUI_FONT_ROOT = "/data/system/theme";

    public static String replaceMiuiFontPath(String fontPath) {
        String path = ANDROID_ROOT;
        if (!fontPath.startsWith(path)) {
            return fontPath;
        }
        path = fontPath.replace(path, MIUI_FONT_ROOT);
        if (new File(path).exists()) {
            return path;
        }
        return fontPath;
    }
}
