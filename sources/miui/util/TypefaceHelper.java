package miui.util;

import android.graphics.Typeface;
import android.graphics.fonts.FontVariationAxis;
import java.util.Arrays;

public class TypefaceHelper {
    private TypefaceHelper() {
    }

    public static Typeface createVarFont(Typeface baseFont, int scaleWght) {
        FontVariationAxis[] fontVariationAxisArr = new FontVariationAxis[1];
        fontVariationAxisArr[0] = new FontVariationAxis("wght", (float) scaleWght);
        return Typeface.createFromTypefaceWithVariation(baseFont, Arrays.asList(fontVariationAxisArr));
    }
}
