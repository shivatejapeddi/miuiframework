package android.graphics.fonts;

import android.graphics.FontListParser;
import android.graphics.fonts.FontCustomizationParser.Result;
import android.graphics.fonts.FontFamily.Builder;
import android.text.FontConfig;
import android.text.FontConfig.Alias;
import android.text.FontConfig.Family;
import android.text.FontConfig.Font;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

public final class SystemFonts {
    private static final String DEFAULT_FAMILY = "sans-serif";
    private static final String TAG = "SystemFonts";
    private static final Alias[] sAliases;
    private static final List<Font> sAvailableFonts;
    private static final Map<String, FontFamily[]> sSystemFallbackMap;

    private SystemFonts() {
    }

    public static Set<Font> getAvailableFonts() {
        HashSet<Font> set = new HashSet();
        set.addAll(sAvailableFonts);
        return set;
    }

    public static FontFamily[] getSystemFallback(String familyName) {
        FontFamily[] families = (FontFamily[]) sSystemFallbackMap.get(familyName);
        return families == null ? (FontFamily[]) sSystemFallbackMap.get(DEFAULT_FAMILY) : families;
    }

    public static Map<String, FontFamily[]> getRawSystemFallbackMap() {
        return sSystemFallbackMap;
    }

    public static Alias[] getAliases() {
        return sAliases;
    }

    /* JADX WARNING: Missing block: B:13:?, code skipped:
            $closeResource(r2, r1);
     */
    private static java.nio.ByteBuffer mmap(java.lang.String r8) {
        /*
        r8 = android.graphics.fonts.SystemFontsInjector.replaceMiuiFontPath(r8);
        r0 = 0;
        r1 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0025 }
        r1.<init>(r8);	 Catch:{ IOException -> 0x0025 }
        r2 = r1.getChannel();	 Catch:{ all -> 0x001e }
        r6 = r2.size();	 Catch:{ all -> 0x001e }
        r3 = java.nio.channels.FileChannel.MapMode.READ_ONLY;	 Catch:{ all -> 0x001e }
        r4 = 0;
        r3 = r2.map(r3, r4, r6);	 Catch:{ all -> 0x001e }
        $closeResource(r0, r1);	 Catch:{ IOException -> 0x0025 }
        return r3;
    L_0x001e:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0020 }
    L_0x0020:
        r3 = move-exception;
        $closeResource(r2, r1);	 Catch:{ IOException -> 0x0025 }
        throw r3;	 Catch:{ IOException -> 0x0025 }
    L_0x0025:
        r1 = move-exception;
        r2 = new java.lang.StringBuilder;
        r2.<init>();
        r3 = "Error mapping font file ";
        r2.append(r3);
        r2.append(r8);
        r2 = r2.toString();
        r3 = "SystemFonts";
        android.util.Log.e(r3, r2);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.fonts.SystemFonts.mmap(java.lang.String):java.nio.ByteBuffer");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    private static void pushFamilyToFallback(Family xmlFamily, ArrayMap<String, ArrayList<FontFamily>> fallbackMap, Map<String, ByteBuffer> cache, ArrayList<Font> availableFonts) {
        ArrayMap<String, ArrayList<FontFamily>> arrayMap = fallbackMap;
        String languageTags = xmlFamily.getLanguages();
        int variant = xmlFamily.getVariant();
        ArrayList<Font> defaultFonts = new ArrayList();
        ArrayMap<String, ArrayList<Font>> specificFallbackFonts = new ArrayMap();
        for (Font font : xmlFamily.getFonts()) {
            String fallbackName = font.getFallbackFor();
            if (fallbackName == null) {
                defaultFonts.add(font);
            } else {
                ArrayList<Font> fallback = (ArrayList) specificFallbackFonts.get(fallbackName);
                if (fallback == null) {
                    fallback = new ArrayList();
                    specificFallbackFonts.put(fallbackName, fallback);
                }
                fallback.add(font);
            }
        }
        FontFamily defaultFamily = defaultFonts.isEmpty() ? null : createFontFamily(xmlFamily.getName(), defaultFonts, languageTags, variant, cache, availableFonts);
        for (int i = 0; i < fallbackMap.size(); i++) {
            ArrayList<Font> fallback2 = (ArrayList) specificFallbackFonts.get(fallbackMap.keyAt(i));
            if (fallback2 != null) {
                FontFamily family = createFontFamily(xmlFamily.getName(), fallback2, languageTags, variant, cache, availableFonts);
                if (family != null) {
                    ((ArrayList) fallbackMap.valueAt(i)).add(family);
                } else if (defaultFamily != null) {
                    ((ArrayList) fallbackMap.valueAt(i)).add(defaultFamily);
                }
            } else if (defaultFamily != null) {
                ((ArrayList) fallbackMap.valueAt(i)).add(defaultFamily);
            }
        }
    }

    private static FontFamily createFontFamily(String familyName, List<Font> fonts, String languageTags, int variant, Map<String, ByteBuffer> cache, ArrayList<Font> availableFonts) {
        FontFamily fontFamily = null;
        if (fonts.size() == 0) {
            return null;
        }
        Builder b = null;
        int i = 0;
        while (true) {
            int i2 = 0;
            if (i >= fonts.size()) {
                break;
            }
            Font fontConfig = (Font) fonts.get(i);
            String fullPath = fontConfig.getFontName();
            ByteBuffer buffer = (ByteBuffer) cache.get(fullPath);
            if (buffer == null) {
                if (!cache.containsKey(fullPath)) {
                    buffer = mmap(fullPath);
                    cache.put(fullPath, buffer);
                    if (buffer == null) {
                    }
                }
                i++;
            }
            try {
                Font.Builder weight = new Font.Builder(buffer, new File(fullPath), languageTags).setWeight(fontConfig.getWeight());
                if (fontConfig.isItalic()) {
                    i2 = 1;
                }
                Font font = weight.setSlant(i2).setTtcIndex(fontConfig.getTtcIndex()).setFontVariationSettings(fontConfig.getAxes()).build();
                availableFonts.add(font);
                if (b == null) {
                    b = new Builder(font);
                } else {
                    b.addFont(font);
                }
                i++;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (b != null) {
            fontFamily = b.build(languageTags, variant, false);
        }
        return fontFamily;
    }

    private static void appendNamedFamily(Family xmlFamily, HashMap<String, ByteBuffer> bufferCache, ArrayMap<String, ArrayList<FontFamily>> fallbackListMap, ArrayList<Font> availableFonts) {
        String familyName = xmlFamily.getName();
        FontFamily family = createFontFamily(familyName, Arrays.asList(xmlFamily.getFonts()), xmlFamily.getLanguages(), xmlFamily.getVariant(), bufferCache, availableFonts);
        if (family != null) {
            ArrayList<FontFamily> fallback = new ArrayList();
            fallback.add(family);
            fallbackListMap.put(familyName, fallback);
        }
    }

    @VisibleForTesting
    public static Alias[] buildSystemFallback(String xmlPath, String fontDir, Result oemCustomization, ArrayMap<String, FontFamily[]> fallbackMap, ArrayList<Font> availableFonts) {
        try {
            int i;
            FontConfig fontConfig = FontListParser.parse(new FileInputStream(xmlPath), fontDir);
            HashMap<String, ByteBuffer> bufferCache = new HashMap();
            Family[] xmlFamilies = fontConfig.getFamilies();
            ArrayMap<String, ArrayList<FontFamily>> fallbackListMap = new ArrayMap();
            for (Family xmlFamily : xmlFamilies) {
                if (xmlFamily.getName() != null) {
                    appendNamedFamily(xmlFamily, bufferCache, fallbackListMap, availableFonts);
                }
            }
            for (i = 0; i < oemCustomization.mAdditionalNamedFamilies.size(); i++) {
                appendNamedFamily((Family) oemCustomization.mAdditionalNamedFamilies.get(i), bufferCache, fallbackListMap, availableFonts);
            }
            for (i = 0; i < xmlFamilies.length; i++) {
                Family xmlFamily2 = xmlFamilies[i];
                if (i == 0 || xmlFamily2.getName() == null) {
                    pushFamilyToFallback(xmlFamily2, fallbackListMap, bufferCache, availableFonts);
                }
            }
            for (i = 0; i < fallbackListMap.size(); i++) {
                List<FontFamily> familyList = (List) fallbackListMap.valueAt(i);
                fallbackMap.put((String) fallbackListMap.keyAt(i), (FontFamily[]) familyList.toArray(new FontFamily[familyList.size()]));
            }
            ArrayList<Alias> list = new ArrayList();
            list.addAll(Arrays.asList(fontConfig.getAliases()));
            list.addAll(oemCustomization.mAdditionalAliases);
            return (Alias[]) list.toArray(new Alias[list.size()]);
        } catch (IOException | XmlPullParserException e) {
            Log.e(TAG, "Failed initialize system fallbacks.", e);
            return (Alias[]) ArrayUtils.emptyArray(Alias.class);
        }
    }

    /* JADX WARNING: Missing block: B:13:?, code skipped:
            $closeResource(r1, r0);
     */
    private static android.graphics.fonts.FontCustomizationParser.Result readFontCustomization(java.lang.String r3, java.lang.String r4) {
        /*
        r0 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x0023, XmlPullParserException -> 0x0015 }
        r0.<init>(r3);	 Catch:{ IOException -> 0x0023, XmlPullParserException -> 0x0015 }
        r1 = 0;
        r2 = android.graphics.fonts.FontCustomizationParser.parse(r0, r4);	 Catch:{ all -> 0x000e }
        $closeResource(r1, r0);	 Catch:{ IOException -> 0x0023, XmlPullParserException -> 0x0015 }
        return r2;
    L_0x000e:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0010 }
    L_0x0010:
        r2 = move-exception;
        $closeResource(r1, r0);	 Catch:{ IOException -> 0x0023, XmlPullParserException -> 0x0015 }
        throw r2;	 Catch:{ IOException -> 0x0023, XmlPullParserException -> 0x0015 }
    L_0x0015:
        r0 = move-exception;
        r1 = "SystemFonts";
        r2 = "Failed to parse font customization XML";
        android.util.Log.e(r1, r2, r0);
        r1 = new android.graphics.fonts.FontCustomizationParser$Result;
        r1.<init>();
        return r1;
    L_0x0023:
        r0 = move-exception;
        r1 = new android.graphics.fonts.FontCustomizationParser$Result;
        r1.<init>();
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.fonts.SystemFonts.readFontCustomization(java.lang.String, java.lang.String):android.graphics.fonts.FontCustomizationParser$Result");
    }

    static {
        ArrayMap<String, FontFamily[]> systemFallbackMap = new ArrayMap();
        ArrayList<Font> availableFonts = new ArrayList();
        sAliases = buildSystemFallback("/system/etc/fonts.xml", "/system/fonts/", readFontCustomization("/product/etc/fonts_customization.xml", "/product/fonts/"), systemFallbackMap, availableFonts);
        sSystemFallbackMap = Collections.unmodifiableMap(systemFallbackMap);
        sAvailableFonts = Collections.unmodifiableList(availableFonts);
    }
}
