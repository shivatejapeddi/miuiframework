package android.graphics;

import java.io.File;
import miui.util.FeatureParser;

public class MiuiTypeface {
    private static final String DROID_SANS = "/data/system/theme/fonts/Roboto-Regular.ttf";
    private static final String DROID_SANS_BOLD = "/data/system/theme/fonts/Roboto-Bold.ttf";
    private static final String DROID_SANS_FALLBACK = "/data/system/theme/fonts/DroidSansFallback.ttf";
    private static String[] DROID_SANS_FONTS = new String[]{DROID_SANS_FALLBACK, DROID_SANS};
    private static String[] DROID_SANS_FONTS_BOLD = new String[]{DROID_SANS_BOLD};
    public static Typeface FLIPFONT = null;
    public static Typeface FLIPFONT_BOLD = null;
    public static Typeface FLIPFONT_BOLD_ITALIC = null;
    public static Typeface FLIPFONT_ITALIC = null;
    private static final String FONTS_FOLDER = "/data/system/theme/fonts/";
    public static final int MONOSPACE_INDEX = 3;
    public static final int SANS_INDEX = 1;
    public static final int SERIF_INDEX = 2;
    private static long mLastModified;

    private static Typeface getTypefaceFlipFont(int typefaceIndex, int style) {
        if (!FeatureParser.getBoolean("is_patchrom", false) || typefaceIndex != 1 || style < 0 || style > 3) {
            return null;
        }
        setTypefaceFlipFont();
        return Typeface.sDefaults[style];
    }

    public static Typeface getChangedTypeface(Typeface oldtf, int typefaceIndex, int style) {
        Typeface newtf = getTypefaceFlipFont(typefaceIndex, style);
        if (newtf != null) {
            return newtf;
        }
        return oldtf;
    }

    public static Typeface getDefaultTypeface(Typeface tf) {
        if (tf == null) {
            return getTypefaceFlipFont(1, 0);
        }
        return tf;
    }

    private static String getFlipFontPath(int typefaceIndex, int styleIndex) {
        String str = "";
        if (typefaceIndex != 1) {
            return str;
        }
        String[] fonts = null;
        if (styleIndex == 0) {
            fonts = DROID_SANS_FONTS;
        } else if (styleIndex == 1) {
            fonts = DROID_SANS_FONTS_BOLD;
        }
        for (String font : fonts) {
            if (new File(font).exists()) {
                return font;
            }
        }
        return str;
    }

    private static void setTypefaceFlipFont() {
        String fontPath = getFlipFontPath(1, 0);
        String fontBoldPath = getFlipFontPath(1, 1);
        File fontFile = new File(fontPath);
        if (fontPath.isEmpty()) {
            FLIPFONT = Typeface.DEFAULT;
            FLIPFONT_BOLD = Typeface.DEFAULT_BOLD;
            String str = (String) null;
            FLIPFONT_ITALIC = Typeface.create(str, 2);
            FLIPFONT_BOLD_ITALIC = Typeface.create(str, 3);
        } else if (mLastModified != fontFile.lastModified()) {
            try {
                FLIPFONT = Typeface.createFromFile(fontPath);
                FLIPFONT_BOLD = Typeface.createFromFile(fontBoldPath);
            } catch (RuntimeException e) {
                FLIPFONT_BOLD = Typeface.create(FLIPFONT, 1);
            }
            FLIPFONT_ITALIC = Typeface.create(FLIPFONT, 2);
            FLIPFONT_BOLD_ITALIC = Typeface.create(FLIPFONT_BOLD, 3);
            mLastModified = fontFile.lastModified();
        }
        Typeface.sDefaults[0] = FLIPFONT;
        Typeface.sDefaults[1] = FLIPFONT_BOLD;
        Typeface.sDefaults[2] = FLIPFONT_ITALIC;
        Typeface.sDefaults[3] = FLIPFONT_BOLD_ITALIC;
    }
}
