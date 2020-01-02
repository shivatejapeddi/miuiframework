package android.graphics;

import java.io.File;

public class TypefaceInjector {
    private static final String THEME_FONT_DIR = "/data/system/theme/fonts/";
    private static Boolean sIsUsingThemeFont;

    private TypefaceInjector() {
    }

    public static boolean isUsingThemeFont() {
        if (sIsUsingThemeFont == null) {
            File dir = new File(THEME_FONT_DIR);
            boolean z = false;
            if (dir.exists()) {
                String[] files = dir.list();
                if (files != null && files.length > 0) {
                    z = true;
                }
                sIsUsingThemeFont = Boolean.valueOf(z);
            } else {
                sIsUsingThemeFont = Boolean.valueOf(false);
            }
        }
        return sIsUsingThemeFont.booleanValue();
    }
}
