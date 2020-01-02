package android.renderscript;

import android.annotation.UnsupportedAppUsage;
import android.content.res.AssetManager.AssetInputStream;
import android.content.res.Resources;
import android.os.Environment;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class Font extends BaseObj {
    private static Map<String, FontFamily> sFontFamilyMap;
    private static final String[] sMonoNames = new String[]{"monospace", "courier", "courier new", "monaco"};
    private static final String[] sSansNames = new String[]{"sans-serif", "arial", "helvetica", "tahoma", "verdana"};
    private static final String[] sSerifNames = new String[]{"serif", "times", "times new roman", "palatino", "georgia", "baskerville", "goudy", "fantasy", "cursive", "ITC Stone Serif"};

    /* renamed from: android.renderscript.Font$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$renderscript$Font$Style = new int[Style.values().length];

        static {
            try {
                $SwitchMap$android$renderscript$Font$Style[Style.NORMAL.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$renderscript$Font$Style[Style.BOLD.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$renderscript$Font$Style[Style.ITALIC.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$renderscript$Font$Style[Style.BOLD_ITALIC.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
        }
    }

    private static class FontFamily {
        String mBoldFileName;
        String mBoldItalicFileName;
        String mItalicFileName;
        String[] mNames;
        String mNormalFileName;

        private FontFamily() {
        }

        /* synthetic */ FontFamily(AnonymousClass1 x0) {
            this();
        }
    }

    public enum Style {
        NORMAL,
        BOLD,
        ITALIC,
        BOLD_ITALIC
    }

    static {
        initFontFamilyMap();
    }

    private static void addFamilyToMap(FontFamily family) {
        for (Object put : family.mNames) {
            sFontFamilyMap.put(put, family);
        }
    }

    private static void initFontFamilyMap() {
        sFontFamilyMap = new HashMap();
        FontFamily sansFamily = new FontFamily();
        sansFamily.mNames = sSansNames;
        sansFamily.mNormalFileName = "Roboto-Regular.ttf";
        sansFamily.mBoldFileName = "Roboto-Bold.ttf";
        sansFamily.mItalicFileName = "Roboto-Italic.ttf";
        sansFamily.mBoldItalicFileName = "Roboto-BoldItalic.ttf";
        addFamilyToMap(sansFamily);
        FontFamily serifFamily = new FontFamily();
        serifFamily.mNames = sSerifNames;
        serifFamily.mNormalFileName = "NotoSerif-Regular.ttf";
        serifFamily.mBoldFileName = "NotoSerif-Bold.ttf";
        serifFamily.mItalicFileName = "NotoSerif-Italic.ttf";
        serifFamily.mBoldItalicFileName = "NotoSerif-BoldItalic.ttf";
        addFamilyToMap(serifFamily);
        FontFamily monoFamily = new FontFamily();
        monoFamily.mNames = sMonoNames;
        String str = "DroidSansMono.ttf";
        monoFamily.mNormalFileName = str;
        monoFamily.mBoldFileName = str;
        monoFamily.mItalicFileName = str;
        monoFamily.mBoldItalicFileName = str;
        addFamilyToMap(monoFamily);
    }

    static String getFontFileName(String familyName, Style style) {
        FontFamily family = (FontFamily) sFontFamilyMap.get(familyName);
        if (family != null) {
            int i = AnonymousClass1.$SwitchMap$android$renderscript$Font$Style[style.ordinal()];
            if (i == 1) {
                return family.mNormalFileName;
            }
            if (i == 2) {
                return family.mBoldFileName;
            }
            if (i == 3) {
                return family.mItalicFileName;
            }
            if (i == 4) {
                return family.mBoldItalicFileName;
            }
        }
        return "DroidSans.ttf";
    }

    Font(long id, RenderScript rs) {
        super(id, rs);
        this.guard.open("destroy");
    }

    public static Font createFromFile(RenderScript rs, Resources res, String path, float pointSize) {
        rs.validate();
        long fontId = rs.nFontCreateFromFile(path, pointSize, res.getDisplayMetrics().densityDpi);
        if (fontId != 0) {
            return new Font(fontId, rs);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to create font from file ");
        stringBuilder.append(path);
        throw new RSRuntimeException(stringBuilder.toString());
    }

    public static Font createFromFile(RenderScript rs, Resources res, File path, float pointSize) {
        return createFromFile(rs, res, path.getAbsolutePath(), pointSize);
    }

    public static Font createFromAsset(RenderScript rs, Resources res, String path, float pointSize) {
        rs.validate();
        long fontId = rs.nFontCreateFromAsset(res.getAssets(), path, pointSize, res.getDisplayMetrics().densityDpi);
        if (fontId != 0) {
            return new Font(fontId, rs);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to create font from asset ");
        stringBuilder.append(path);
        throw new RSRuntimeException(stringBuilder.toString());
    }

    public static Font createFromResource(RenderScript rs, Resources res, int id, float pointSize) {
        int i = id;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("R.");
        stringBuilder.append(Integer.toString(id));
        String name = stringBuilder.toString();
        rs.validate();
        RenderScript renderScript;
        try {
            InputStream is = res.openRawResource(id);
            int dpi = res.getDisplayMetrics().densityDpi;
            if (is instanceof AssetInputStream) {
                long fontId = rs.nFontCreateFromAssetStream(name, pointSize, dpi, ((AssetInputStream) is).getNativeAsset());
                if (fontId != 0) {
                    renderScript = rs;
                    return new Font(fontId, rs);
                }
                renderScript = rs;
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Unable to create font from resource ");
                stringBuilder2.append(i);
                throw new RSRuntimeException(stringBuilder2.toString());
            }
            renderScript = rs;
            throw new RSRuntimeException("Unsupported asset stream created");
        } catch (Exception e) {
            renderScript = rs;
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("Unable to open resource ");
            stringBuilder3.append(i);
            throw new RSRuntimeException(stringBuilder3.toString());
        }
    }

    @UnsupportedAppUsage
    public static Font create(RenderScript rs, Resources res, String familyName, Style fontStyle, float pointSize) {
        String fileName = getFontFileName(familyName, fontStyle);
        String fontPath = Environment.getRootDirectory().getAbsolutePath();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(fontPath);
        stringBuilder.append("/fonts/");
        stringBuilder.append(fileName);
        return createFromFile(rs, res, stringBuilder.toString(), pointSize);
    }
}
