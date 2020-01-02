package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.content.res.AssetManager;
import android.content.res.FontResourcesParser.FamilyResourceEntry;
import android.content.res.FontResourcesParser.FontFamilyFilesResourceEntry;
import android.content.res.FontResourcesParser.FontFileResourceEntry;
import android.content.res.FontResourcesParser.ProviderResourceEntry;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.graphics.fonts.FontVariationAxis;
import android.graphics.fonts.SystemFonts;
import android.media.tv.TvContract.PreviewPrograms;
import android.os.ParcelFileDescriptor;
import android.provider.FontRequest;
import android.provider.FontsContract;
import android.text.FontConfig.Alias;
import android.util.Base64;
import android.util.LongSparseArray;
import android.util.LruCache;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.Preconditions;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import libcore.util.NativeAllocationRegistry;

public class Typeface {
    public static final int BOLD = 1;
    public static final int BOLD_ITALIC = 3;
    public static final Typeface DEFAULT;
    public static final Typeface DEFAULT_BOLD;
    private static final String DEFAULT_FAMILY = "sans-serif";
    private static final int[] EMPTY_AXES = new int[0];
    public static final int ITALIC = 2;
    public static final Typeface MONOSPACE = create("monospace", 0);
    public static final int NORMAL = 0;
    public static final int RESOLVE_BY_FONT_TABLE = -1;
    public static final Typeface SANS_SERIF;
    public static final Typeface SERIF = create("serif", 0);
    private static final int STYLE_ITALIC = 1;
    public static final int STYLE_MASK = 3;
    private static final int STYLE_NORMAL = 0;
    private static String TAG = "Typeface";
    static Typeface sDefaultTypeface;
    @UnsupportedAppUsage(trackingBug = 123769446)
    static Typeface[] sDefaults;
    private static final Object sDynamicCacheLock = new Object();
    @GuardedBy({"sDynamicCacheLock"})
    private static final LruCache<String, Typeface> sDynamicTypefaceCache = new LruCache(16);
    private static final NativeAllocationRegistry sRegistry = NativeAllocationRegistry.createMalloced(Typeface.class.getClassLoader(), nativeGetReleaseFunc());
    private static final Object sStyledCacheLock = new Object();
    @GuardedBy({"sStyledCacheLock"})
    private static final LongSparseArray<SparseArray<Typeface>> sStyledTypefaceCache = new LongSparseArray(3);
    @Deprecated
    @UnsupportedAppUsage(trackingBug = 123768928)
    static Map<String, FontFamily[]> sSystemFallbackMap = Collections.emptyMap();
    @UnsupportedAppUsage(trackingBug = 123769347)
    static Map<String, Typeface> sSystemFontMap;
    private static final Object sWeightCacheLock = new Object();
    @GuardedBy({"sWeightCacheLock"})
    private static final LongSparseArray<SparseArray<Typeface>> sWeightTypefaceCache = new LongSparseArray(3);
    public String[] familyName;
    @UnsupportedAppUsage
    private int mStyle;
    private int[] mSupportedAxes;
    private int mWeight;
    @UnsupportedAppUsage
    public long native_instance;

    public static final class Builder {
        public static final int BOLD_WEIGHT = 700;
        public static final int NORMAL_WEIGHT = 400;
        private final AssetManager mAssetManager;
        private String mFallbackFamilyName;
        private final android.graphics.fonts.Font.Builder mFontBuilder;
        private int mItalic;
        private final String mPath;
        private int mWeight;

        public Builder(File path) {
            this.mWeight = -1;
            this.mItalic = -1;
            this.mFontBuilder = new android.graphics.fonts.Font.Builder(path);
            this.mAssetManager = null;
            this.mPath = null;
        }

        public Builder(FileDescriptor fd) {
            android.graphics.fonts.Font.Builder builder;
            this.mWeight = -1;
            this.mItalic = -1;
            try {
                builder = new android.graphics.fonts.Font.Builder(ParcelFileDescriptor.dup(fd));
            } catch (IOException e) {
                builder = null;
            }
            this.mFontBuilder = builder;
            this.mAssetManager = null;
            this.mPath = null;
        }

        public Builder(String path) {
            this.mWeight = -1;
            this.mItalic = -1;
            this.mFontBuilder = new android.graphics.fonts.Font.Builder(new File(path));
            this.mAssetManager = null;
            this.mPath = null;
        }

        public Builder(AssetManager assetManager, String path) {
            this(assetManager, path, true, 0);
        }

        public Builder(AssetManager assetManager, String path, boolean isAsset, int cookie) {
            this.mWeight = -1;
            this.mItalic = -1;
            this.mFontBuilder = new android.graphics.fonts.Font.Builder(assetManager, path, isAsset, cookie);
            this.mAssetManager = assetManager;
            this.mPath = path;
        }

        public Builder setWeight(int weight) {
            this.mWeight = weight;
            this.mFontBuilder.setWeight(weight);
            return this;
        }

        public Builder setItalic(boolean italic) {
            this.mItalic = italic;
            this.mFontBuilder.setSlant(this.mItalic);
            return this;
        }

        public Builder setTtcIndex(int ttcIndex) {
            this.mFontBuilder.setTtcIndex(ttcIndex);
            return this;
        }

        public Builder setFontVariationSettings(String variationSettings) {
            this.mFontBuilder.setFontVariationSettings(variationSettings);
            return this;
        }

        public Builder setFontVariationSettings(FontVariationAxis[] axes) {
            this.mFontBuilder.setFontVariationSettings(axes);
            return this;
        }

        public Builder setFallback(String familyName) {
            this.mFallbackFamilyName = familyName;
            return this;
        }

        private static String createAssetUid(AssetManager mgr, String path, int ttcIndex, FontVariationAxis[] axes, int weight, int italic, String fallback) {
            String str;
            SparseArray<String> pkgs = mgr.getAssignedPackageIdentifiers();
            StringBuilder builder = new StringBuilder();
            int size = pkgs.size();
            int i = 0;
            while (true) {
                str = "-";
                if (i >= size) {
                    break;
                }
                builder.append((String) pkgs.valueAt(i));
                builder.append(str);
                i++;
            }
            builder.append(path);
            builder.append(str);
            builder.append(Integer.toString(ttcIndex));
            builder.append(str);
            builder.append(Integer.toString(weight));
            builder.append(str);
            builder.append(Integer.toString(italic));
            String str2 = "--";
            builder.append(str2);
            builder.append(fallback);
            builder.append(str2);
            if (axes != null) {
                for (FontVariationAxis axis : axes) {
                    builder.append(axis.getTag());
                    builder.append(str);
                    builder.append(Float.toString(axis.getStyleValue()));
                }
            }
            return builder.toString();
        }

        private Typeface resolveFallbackTypeface() {
            Typeface base = this.mFallbackFamilyName;
            if (base == null) {
                return null;
            }
            base = Typeface.getSystemDefaultTypeface(base);
            if (this.mWeight == -1 && this.mItalic == -1) {
                return base;
            }
            int weight = this.mWeight;
            if (weight == -1) {
                weight = base.mWeight;
            }
            int i = this.mItalic;
            boolean z = false;
            if (i != -1 ? i != 1 : (base.mStyle & 2) == 0) {
                z = true;
            }
            return Typeface.createWeightStyle(base, weight, z);
        }

        public Typeface build() {
            Font font = this.mFontBuilder;
            if (font == null) {
                return resolveFallbackTypeface();
            }
            try {
                String key;
                font = font.build();
                if (this.mAssetManager == null) {
                    key = null;
                } else {
                    key = createAssetUid(this.mAssetManager, this.mPath, font.getTtcIndex(), font.getAxes(), this.mWeight, this.mItalic, this.mFallbackFamilyName == null ? Typeface.DEFAULT_FAMILY : this.mFallbackFamilyName);
                }
                if (key != null) {
                    synchronized (Typeface.sDynamicCacheLock) {
                        Typeface typeface = (Typeface) Typeface.sDynamicTypefaceCache.get(key);
                        if (typeface != null) {
                            return typeface;
                        }
                    }
                }
                CustomFallbackBuilder builder = new CustomFallbackBuilder(new android.graphics.fonts.FontFamily.Builder(font).build()).setStyle(new FontStyle(this.mWeight == -1 ? font.getStyle().getWeight() : this.mWeight, this.mItalic == -1 ? font.getStyle().getSlant() : this.mItalic));
                if (this.mFallbackFamilyName != null) {
                    builder.setSystemFallback(this.mFallbackFamilyName);
                }
                Typeface typeface2 = builder.build();
                if (key != null) {
                    synchronized (Typeface.sDynamicCacheLock) {
                        Typeface.sDynamicTypefaceCache.put(key, typeface2);
                    }
                }
                return typeface2;
            } catch (IOException | IllegalArgumentException e) {
                return resolveFallbackTypeface();
            }
        }
    }

    public static final class CustomFallbackBuilder {
        private static final int MAX_CUSTOM_FALLBACK = 64;
        private String mFallbackName = null;
        private final ArrayList<FontFamily> mFamilies = new ArrayList();
        private FontStyle mStyle;

        public static int getMaxCustomFallbackCount() {
            return 64;
        }

        public CustomFallbackBuilder(FontFamily family) {
            Preconditions.checkNotNull(family);
            this.mFamilies.add(family);
        }

        public CustomFallbackBuilder setSystemFallback(String familyName) {
            Preconditions.checkNotNull(familyName);
            this.mFallbackName = familyName;
            return this;
        }

        public CustomFallbackBuilder setStyle(FontStyle style) {
            this.mStyle = style;
            return this;
        }

        public CustomFallbackBuilder addCustomFallback(FontFamily family) {
            Preconditions.checkNotNull(family);
            boolean z = this.mFamilies.size() < getMaxCustomFallbackCount();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Custom fallback limit exceeded(");
            stringBuilder.append(getMaxCustomFallbackCount());
            stringBuilder.append(")");
            Preconditions.checkArgument(z, stringBuilder.toString());
            this.mFamilies.add(family);
            return this;
        }

        public Typeface build() {
            int i;
            int userFallbackSize = this.mFamilies.size();
            FontFamily[] fallback = SystemFonts.getSystemFallback(this.mFallbackName);
            long[] ptrArray = new long[(fallback.length + userFallbackSize)];
            for (i = 0; i < userFallbackSize; i++) {
                ptrArray[i] = ((FontFamily) this.mFamilies.get(i)).getNativePtr();
            }
            for (i = 0; i < fallback.length; i++) {
                ptrArray[i + userFallbackSize] = fallback[i].getNativePtr();
            }
            i = this.mStyle;
            i = i == 0 ? 400 : i.getWeight();
            FontStyle fontStyle = this.mStyle;
            int italic = (fontStyle == null || fontStyle.getSlant() == 0) ? 0 : 1;
            return new Typeface(Typeface.nativeCreateFromArray(ptrArray, i, italic));
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Style {
    }

    @UnsupportedAppUsage
    private static native long nativeCreateFromArray(long[] jArr, int i, int i2);

    private static native long nativeCreateFromTypeface(long j, int i);

    private static native long nativeCreateFromTypefaceWithExactStyle(long j, int i, boolean z);

    private static native long nativeCreateFromTypefaceWithVariation(long j, List<FontVariationAxis> list);

    @UnsupportedAppUsage
    private static native long nativeCreateWeightAlias(long j, int i);

    private static native long nativeGetReleaseFunc();

    private static native int nativeGetStyle(long j);

    private static native int[] nativeGetSupportedAxes(long j);

    private static native int nativeGetWeight(long j);

    private static native void nativeRegisterGenericFamily(String str, long j);

    private static native void nativeSetDefault(long j);

    static {
        int i = 0;
        HashMap<String, Typeface> systemFontMap = new HashMap();
        initSystemDefaultTypefaces(systemFontMap, SystemFonts.getRawSystemFallbackMap(), SystemFonts.getAliases());
        sSystemFontMap = Collections.unmodifiableMap(systemFontMap);
        Map map = sSystemFontMap;
        String str = DEFAULT_FAMILY;
        if (map.containsKey(str)) {
            setDefault((Typeface) sSystemFontMap.get(str));
        }
        String str2 = (String) null;
        DEFAULT = create(str2, 0);
        DEFAULT_BOLD = create(str2, 1);
        SANS_SERIF = create(str, 0);
        sDefaults = new Typeface[]{DEFAULT, DEFAULT_BOLD, create(str2, 2), create(str2, 3)};
        String[] genericFamilies = new String[]{"serif", DEFAULT_FAMILY, "cursive", "fantasy", "monospace", "system-ui"};
        int length = genericFamilies.length;
        while (i < length) {
            str = genericFamilies[i];
            registerGenericFamilyNative(str, (Typeface) systemFontMap.get(str));
            i++;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static void setDefault(Typeface t) {
        sDefaultTypeface = t;
        nativeSetDefault(t.native_instance);
    }

    public int getWeight() {
        return this.mWeight;
    }

    public int getStyle() {
        return this.mStyle;
    }

    public final boolean isBold() {
        return (this.mStyle & 1) != 0;
    }

    public final boolean isItalic() {
        return (this.mStyle & 2) != 0;
    }

    public static Typeface createFromResources(FamilyResourceEntry entry, AssetManager mgr, String path) {
        int j;
        if (entry instanceof ProviderResourceEntry) {
            ProviderResourceEntry providerEntry = (ProviderResourceEntry) entry;
            List<List<String>> givenCerts = providerEntry.getCerts();
            List<List<byte[]>> certs = new ArrayList();
            if (givenCerts != null) {
                for (int i = 0; i < givenCerts.size(); i++) {
                    List<String> certSet = (List) givenCerts.get(i);
                    List<byte[]> byteArraySet = new ArrayList();
                    for (j = 0; j < certSet.size(); j++) {
                        byteArraySet.add(Base64.decode((String) certSet.get(j), 0));
                    }
                    certs.add(byteArraySet);
                }
            }
            Typeface typeface = FontsContract.getFontSync(new FontRequest(providerEntry.getAuthority(), providerEntry.getPackage(), providerEntry.getQuery(), certs));
            return typeface == null ? DEFAULT : typeface;
        }
        Typeface typeface2 = findFromCache(mgr, path);
        if (typeface2 != null) {
            return typeface2;
        }
        Typeface typeface3;
        try {
            FontFileResourceEntry[] entries = ((FontFamilyFilesResourceEntry) entry).getEntries();
            int length = entries.length;
            android.graphics.fonts.FontFamily.Builder familyBuilder = null;
            int familyBuilder2 = 0;
            while (true) {
                j = 1;
                if (familyBuilder2 >= length) {
                    break;
                }
                FontFileResourceEntry fontFile = entries[familyBuilder2];
                android.graphics.fonts.Font.Builder fontBuilder = new android.graphics.fonts.Font.Builder(mgr, fontFile.getFileName(), false, 0).setTtcIndex(fontFile.getTtcIndex()).setFontVariationSettings(fontFile.getVariationSettings());
                if (fontFile.getWeight() != -1) {
                    fontBuilder.setWeight(fontFile.getWeight());
                }
                if (fontFile.getItalic() != -1) {
                    if (fontFile.getItalic() != 1) {
                        j = 0;
                    }
                    fontBuilder.setSlant(j);
                }
                if (familyBuilder == null) {
                    familyBuilder = new android.graphics.fonts.FontFamily.Builder(fontBuilder.build());
                } else {
                    familyBuilder.addFont(fontBuilder.build());
                }
                familyBuilder2++;
            }
            if (familyBuilder == null) {
                return DEFAULT;
            }
            FontFamily family = familyBuilder.build();
            FontStyle normal = new FontStyle(400, 0);
            Font bestFont = family.getFont(0);
            length = normal.getMatchScore(bestFont.getStyle());
            while (j < family.getSize()) {
                Font candidate = family.getFont(j);
                int score = normal.getMatchScore(candidate.getStyle());
                if (score < length) {
                    bestFont = candidate;
                    length = score;
                }
                j++;
            }
            typeface3 = new CustomFallbackBuilder(family).setStyle(bestFont.getStyle()).build();
            synchronized (sDynamicCacheLock) {
                sDynamicTypefaceCache.put(Builder.createAssetUid(mgr, path, 0, null, -1, -1, DEFAULT_FAMILY), typeface3);
            }
            return typeface3;
        } catch (IllegalArgumentException e) {
            return null;
        } catch (IOException e2) {
            typeface3 = DEFAULT;
        }
    }

    public static Typeface findFromCache(AssetManager mgr, String path) {
        synchronized (sDynamicCacheLock) {
            Typeface typeface = (Typeface) sDynamicTypefaceCache.get(Builder.createAssetUid(mgr, path, 0, null, -1, -1, DEFAULT_FAMILY));
            if (typeface != null) {
                return typeface;
            }
            return null;
        }
    }

    public static Typeface create(String familyName, int style) {
        return create(getSystemDefaultTypeface(familyName), style);
    }

    public static Typeface create(Typeface family, int style) {
        if ((style & -4) != 0) {
            style = 0;
        }
        if (family == null) {
            family = sDefaultTypeface;
        }
        if (family.mStyle == style) {
            return family;
        }
        long ni = family.native_instance;
        synchronized (sStyledCacheLock) {
            Typeface typeface;
            SparseArray<Typeface> styles = (SparseArray) sStyledTypefaceCache.get(ni);
            if (styles == null) {
                styles = new SparseArray(4);
                sStyledTypefaceCache.put(ni, styles);
            } else {
                typeface = (Typeface) styles.get(style);
                if (typeface != null) {
                    return typeface;
                }
            }
            typeface = new Typeface(nativeCreateFromTypeface(ni, style));
            typeface.familyName = family.familyName;
            styles.put(style, typeface);
            return typeface;
        }
    }

    public static Typeface create(Typeface family, int weight, boolean italic) {
        Preconditions.checkArgumentInRange(weight, 0, 1000, PreviewPrograms.COLUMN_WEIGHT);
        if (family == null) {
            family = sDefaultTypeface;
        }
        return createWeightStyle(family, weight, italic);
    }

    private static Typeface createWeightStyle(Typeface base, int weight, boolean italic) {
        int key = (weight << 1) | italic;
        synchronized (sWeightCacheLock) {
            Typeface typeface;
            SparseArray<Typeface> innerCache = (SparseArray) sWeightTypefaceCache.get(base.native_instance);
            if (innerCache == null) {
                innerCache = new SparseArray(4);
                sWeightTypefaceCache.put(base.native_instance, innerCache);
            } else {
                typeface = (Typeface) innerCache.get(key);
                if (typeface != null) {
                    return typeface;
                }
            }
            typeface = new Typeface(nativeCreateFromTypefaceWithExactStyle(base.native_instance, weight, italic));
            typeface.familyName = base.familyName;
            innerCache.put(key, typeface);
            return typeface;
        }
    }

    public static Typeface createFromTypefaceWithVariation(Typeface family, List<FontVariationAxis> axes) {
        return new Typeface(nativeCreateFromTypefaceWithVariation((family == null ? DEFAULT : family).native_instance, axes));
    }

    public static Typeface defaultFromStyle(int style) {
        return sDefaults[style];
    }

    public static Typeface createFromAsset(AssetManager mgr, String path) {
        Preconditions.checkNotNull(path);
        Preconditions.checkNotNull(mgr);
        Typeface typeface = new Builder(mgr, path).build();
        if (typeface != null) {
            return typeface;
        }
        try {
            InputStream inputStream = mgr.open(path);
            if (inputStream != null) {
                inputStream.close();
            }
            return DEFAULT;
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Font asset not found ");
            stringBuilder.append(path);
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    private static String createProviderUid(String authority, String query) {
        StringBuilder builder = new StringBuilder();
        builder.append("provider:");
        builder.append(authority);
        builder.append("-");
        builder.append(query);
        return builder.toString();
    }

    public static Typeface createFromFile(File file) {
        Typeface typeface = new Builder(file).build();
        if (typeface != null) {
            return typeface;
        }
        if (file.exists()) {
            return DEFAULT;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Font asset not found ");
        stringBuilder.append(file.getAbsolutePath());
        throw new RuntimeException(stringBuilder.toString());
    }

    public static Typeface createFromFile(String path) {
        Preconditions.checkNotNull(path);
        return createFromFile(new File(path));
    }

    @Deprecated
    @UnsupportedAppUsage(trackingBug = 123768928)
    private static Typeface createFromFamilies(FontFamily[] families) {
        long[] ptrArray = new long[families.length];
        for (int i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].mNativePtr;
        }
        return new Typeface(nativeCreateFromArray(ptrArray, -1, -1));
    }

    private static Typeface createFromFamilies(FontFamily[] families) {
        long[] ptrArray = new long[families.length];
        for (int i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].getNativePtr();
        }
        return new Typeface(nativeCreateFromArray(ptrArray, -1, -1));
    }

    @Deprecated
    @UnsupportedAppUsage(trackingBug = 123768395)
    private static Typeface createFromFamiliesWithDefault(FontFamily[] families, int weight, int italic) {
        return createFromFamiliesWithDefault(families, DEFAULT_FAMILY, weight, italic);
    }

    @Deprecated
    @UnsupportedAppUsage(trackingBug = 123768928)
    private static Typeface createFromFamiliesWithDefault(FontFamily[] families, String fallbackName, int weight, int italic) {
        int i;
        FontFamily[] fallback = SystemFonts.getSystemFallback(fallbackName);
        long[] ptrArray = new long[(families.length + fallback.length)];
        for (i = 0; i < families.length; i++) {
            ptrArray[i] = families[i].mNativePtr;
        }
        for (i = 0; i < fallback.length; i++) {
            ptrArray[families.length + i] = fallback[i].getNativePtr();
        }
        return new Typeface(nativeCreateFromArray(ptrArray, weight, italic));
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Typeface(long ni) {
        this.mStyle = 0;
        this.mWeight = 0;
        if (ni != 0) {
            this.native_instance = ni;
            sRegistry.registerNativeAllocation(this, this.native_instance);
            this.mStyle = nativeGetStyle(ni);
            this.mWeight = nativeGetWeight(ni);
            return;
        }
        throw new RuntimeException("native typeface cannot be made");
    }

    private static Typeface getSystemDefaultTypeface(String familyName) {
        Typeface tf = (Typeface) sSystemFontMap.get(familyName);
        return tf == null ? DEFAULT : tf;
    }

    @VisibleForTesting
    public static void initSystemDefaultTypefaces(Map<String, Typeface> systemFontMap, Map<String, FontFamily[]> fallbacks, Alias[] aliases) {
        for (Entry<String, FontFamily[]> entry : fallbacks.entrySet()) {
            Typeface tf = createFromFamilies((FontFamily[]) entry.getValue());
            tf.familyName = new String[]{(String) entry.getKey()};
            systemFontMap.put((String) entry.getKey(), tf);
        }
        for (Alias alias : aliases) {
            if (!systemFontMap.containsKey(alias.getName())) {
                Typeface base = (Typeface) systemFontMap.get(alias.getToName());
                if (base != null) {
                    Typeface newFace;
                    int weight = alias.getWeight();
                    if (weight == 400) {
                        newFace = base;
                    } else {
                        newFace = new Typeface(nativeCreateWeightAlias(base.native_instance, weight));
                    }
                    newFace.familyName = new String[]{alias.getName()};
                    systemFontMap.put(alias.getName(), newFace);
                }
            }
        }
    }

    private static void registerGenericFamilyNative(String familyName, Typeface typeface) {
        if (typeface != null) {
            nativeRegisterGenericFamily(familyName, typeface.native_instance);
        }
    }

    public static void init() {
        HashMap<String, Typeface> systemFontMap = new HashMap();
        initSystemDefaultTypefaces(systemFontMap, SystemFonts.getRawSystemFallbackMap(), SystemFonts.getAliases());
        sSystemFontMap = Collections.unmodifiableMap(systemFontMap);
        Map map = sSystemFontMap;
        String str = DEFAULT_FAMILY;
        if (map.containsKey(str)) {
            setDefault((Typeface) sSystemFontMap.get(str));
        }
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Typeface typeface = (Typeface) o;
        if (!(this.mStyle == typeface.mStyle && this.native_instance == typeface.native_instance)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result = 17 * 31;
        long j = this.native_instance;
        return ((result + ((int) (j ^ (j >>> 32)))) * 31) + this.mStyle;
    }

    public boolean isSupportedAxes(int axis) {
        if (this.mSupportedAxes == null) {
            synchronized (this) {
                if (this.mSupportedAxes == null) {
                    this.mSupportedAxes = nativeGetSupportedAxes(this.native_instance);
                    if (this.mSupportedAxes == null) {
                        this.mSupportedAxes = EMPTY_AXES;
                    }
                }
            }
        }
        return Arrays.binarySearch(this.mSupportedAxes, axis) >= 0;
    }
}
