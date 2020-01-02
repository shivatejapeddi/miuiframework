package android.content.res;

import android.animation.Animator;
import android.animation.StateListAnimator;
import android.annotation.UnsupportedAppUsage;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager.AssetInputStream;
import android.content.res.FontResourcesParser.FamilyResourceEntry;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.AssetInputStreamSource;
import android.graphics.Typeface;
import android.graphics.Typeface.Builder;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.ColorStateListDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableContainer;
import android.icu.text.PluralRules;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Build.VERSION;
import android.os.LocaleList;
import android.os.Process;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.TypedValue;
import android.view.DisplayAdjustments;
import com.android.internal.util.GrowingArrayUtils;
import com.miui.internal.variable.api.v29.Android_Content_Res_ResourcesImpl.Extension;
import com.miui.internal.variable.api.v29.Android_Content_Res_ResourcesImpl.Interface;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import miui.R;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class ResourcesImpl {
    private static final boolean DEBUG_CONFIG = false;
    private static final boolean DEBUG_LOAD = false;
    private static final int ID_OTHER = 16777220;
    static final String TAG = "Resources";
    static final String TAG_PRELOAD = "Resources.preload";
    public static final boolean TRACE_FOR_DETAILED_PRELOAD = SystemProperties.getBoolean("debug.trace_resource_preload", false);
    @UnsupportedAppUsage
    private static final boolean TRACE_FOR_MISS_PRELOAD = false;
    @UnsupportedAppUsage
    private static final boolean TRACE_FOR_PRELOAD = false;
    private static final int XML_BLOCK_CACHE_SIZE = 4;
    private static int sPreloadTracingNumLoadedDrawables;
    private static boolean sPreloaded;
    @UnsupportedAppUsage
    private static final LongSparseArray<ConstantState> sPreloadedColorDrawables = new LongSparseArray();
    @UnsupportedAppUsage
    private static final LongSparseArray<ConstantState<ComplexColor>> sPreloadedComplexColors = new LongSparseArray();
    @UnsupportedAppUsage
    private static final LongSparseArray<ConstantState>[] sPreloadedDrawables = new LongSparseArray[2];
    private static final Object sSync = new Object();
    @UnsupportedAppUsage
    private final Object mAccessLock = new Object();
    @UnsupportedAppUsage
    private final ConfigurationBoundResourceCache<Animator> mAnimatorCache = new ConfigurationBoundResourceCache();
    @UnsupportedAppUsage
    final AssetManager mAssets;
    private final int[] mCachedXmlBlockCookies = new int[4];
    private final String[] mCachedXmlBlockFiles = new String[4];
    private final XmlBlock[] mCachedXmlBlocks = new XmlBlock[4];
    @UnsupportedAppUsage
    private final DrawableCache mColorDrawableCache = new DrawableCache();
    private final ConfigurationBoundResourceCache<ComplexColor> mComplexColorCache = new ConfigurationBoundResourceCache();
    @UnsupportedAppUsage
    private final Configuration mConfiguration = new Configuration();
    private final DisplayAdjustments mDisplayAdjustments;
    @UnsupportedAppUsage
    private final DrawableCache mDrawableCache = new DrawableCache();
    private int mLastCachedXmlBlockIndex = -1;
    private final ThreadLocal<LookupStack> mLookupStack = ThreadLocal.withInitial(-$$Lambda$ResourcesImpl$h3PTRX185BeQl8SVC2_w9arp5Og.INSTANCE);
    private final DisplayMetrics mMetrics = new DisplayMetrics();
    private PluralRules mPluralRule;
    private long mPreloadTracingPreloadStartTime;
    private long mPreloadTracingStartBitmapCount;
    private long mPreloadTracingStartBitmapSize;
    @UnsupportedAppUsage
    private boolean mPreloading;
    @UnsupportedAppUsage
    private final ConfigurationBoundResourceCache<StateListAnimator> mStateListAnimatorCache = new ConfigurationBoundResourceCache();
    private final Configuration mTmpConfig = new Configuration();

    private static class Impl implements Interface {
        private Impl() {
        }

        public Drawable loadDrawable(ResourcesImpl resources, Resources resources1, TypedValue typedValue, int i, int i1, Theme theme) {
            return resources.originalLoadDrawable(resources1, typedValue, i, i1, theme);
        }
    }

    private static class LookupStack {
        private int[] mIds;
        private int mSize;

        private LookupStack() {
            this.mIds = new int[4];
            this.mSize = 0;
        }

        public void push(int id) {
            this.mIds = GrowingArrayUtils.append(this.mIds, this.mSize, id);
            this.mSize++;
        }

        public boolean contains(int id) {
            for (int i = 0; i < this.mSize; i++) {
                if (this.mIds[i] == id) {
                    return true;
                }
            }
            return false;
        }

        public void pop() {
            this.mSize--;
        }
    }

    public class ThemeImpl {
        private final AssetManager mAssets;
        private final ThemeKey mKey = new ThemeKey();
        private final long mTheme;
        private int mThemeResId = 0;

        ThemeImpl() {
            this.mAssets = ResourcesImpl.this.mAssets;
            this.mTheme = this.mAssets.createTheme();
        }

        /* Access modifiers changed, original: protected */
        public void finalize() throws Throwable {
            super.finalize();
            this.mAssets.releaseTheme(this.mTheme);
        }

        /* Access modifiers changed, original: 0000 */
        public ThemeKey getKey() {
            return this.mKey;
        }

        /* Access modifiers changed, original: 0000 */
        public long getNativeTheme() {
            return this.mTheme;
        }

        /* Access modifiers changed, original: 0000 */
        public int getAppliedStyleResId() {
            return this.mThemeResId;
        }

        /* Access modifiers changed, original: 0000 */
        public void applyStyle(int resId, boolean force) {
            synchronized (this.mKey) {
                this.mAssets.applyStyleToTheme(this.mTheme, resId, force);
                this.mThemeResId = resId;
                this.mKey.append(resId, force);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void setTo(ThemeImpl other) {
            synchronized (this.mKey) {
                synchronized (other.mKey) {
                    this.mAssets.setThemeTo(this.mTheme, other.mAssets, other.mTheme);
                    this.mThemeResId = other.mThemeResId;
                    this.mKey.setTo(other.getKey());
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public TypedArray obtainStyledAttributes(Theme wrapper, AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
            Throwable th;
            synchronized (this.mKey) {
                try {
                    int len = attrs.length;
                    TypedArray array = TypedArray.obtain(wrapper.getResources(), len);
                    Parser parser = (Parser) set;
                    Parser parser2 = parser;
                    this.mAssets.applyStyle(this.mTheme, defStyleAttr, defStyleRes, parser, attrs, array.mDataAddress, array.mIndicesAddress);
                    array.mTheme = wrapper;
                    array.mXml = parser2;
                    return array;
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public TypedArray resolveAttributes(Theme wrapper, int[] values, int[] attrs) {
            TypedArray array;
            synchronized (this.mKey) {
                int len = attrs.length;
                if (values == null || len != values.length) {
                    throw new IllegalArgumentException("Base attribute values must the same length as attrs");
                }
                array = TypedArray.obtain(wrapper.getResources(), len);
                this.mAssets.resolveAttrs(this.mTheme, 0, 0, values, attrs, array.mData, array.mIndices);
                array.mTheme = wrapper;
                array.mXml = null;
            }
            return array;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean resolveAttribute(int resid, TypedValue outValue, boolean resolveRefs) {
            boolean themeValue;
            synchronized (this.mKey) {
                themeValue = this.mAssets.getThemeValue(this.mTheme, resid, outValue, resolveRefs);
            }
            return themeValue;
        }

        /* Access modifiers changed, original: 0000 */
        public int[] getAllAttributes() {
            return this.mAssets.getStyleAttributes(getAppliedStyleResId());
        }

        /* Access modifiers changed, original: 0000 */
        public int getChangingConfigurations() {
            int activityInfoConfigNativeToJava;
            synchronized (this.mKey) {
                activityInfoConfigNativeToJava = ActivityInfo.activityInfoConfigNativeToJava(AssetManager.nativeThemeGetChangingConfigurations(this.mTheme));
            }
            return activityInfoConfigNativeToJava;
        }

        public void dump(int priority, String tag, String prefix) {
            synchronized (this.mKey) {
                this.mAssets.dumpTheme(this.mTheme, priority, tag, prefix);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public String[] getTheme() {
            String[] themes;
            synchronized (this.mKey) {
                int N = this.mKey.mCount;
                themes = new String[(N * 2)];
                int i = 0;
                int j = N - 1;
                while (i < themes.length) {
                    int resId = this.mKey.mResId[j];
                    boolean forced = this.mKey.mForce[j];
                    try {
                        themes[i] = ResourcesImpl.this.getResourceName(resId);
                    } catch (NotFoundException e) {
                        themes[i] = Integer.toHexString(i);
                    }
                    themes[i + 1] = forced ? "forced" : "not forced";
                    i += 2;
                    j--;
                }
            }
            return themes;
        }

        /* Access modifiers changed, original: 0000 */
        public void rebase() {
            synchronized (this.mKey) {
                AssetManager.nativeThemeClear(this.mTheme);
                for (int i = 0; i < this.mKey.mCount; i++) {
                    this.mAssets.applyStyleToTheme(this.mTheme, this.mKey.mResId[i], this.mKey.mForce[i]);
                }
            }
        }

        public int[] getAttributeResolutionStack(int defStyleAttr, int defStyleRes, int explicitStyleRes) {
            int[] attributeResolutionStack;
            synchronized (this.mKey) {
                attributeResolutionStack = this.mAssets.getAttributeResolutionStack(this.mTheme, defStyleAttr, defStyleRes, explicitStyleRes);
            }
            return attributeResolutionStack;
        }
    }

    static {
        sPreloadedDrawables[0] = new LongSparseArray();
        sPreloadedDrawables[1] = new LongSparseArray();
        Extension.get().bindOriginal(new Impl());
    }

    @UnsupportedAppUsage
    public ResourcesImpl(AssetManager assets, DisplayMetrics metrics, Configuration config, DisplayAdjustments displayAdjustments) {
        this.mAssets = assets;
        this.mMetrics.setToDefaults();
        this.mDisplayAdjustments = displayAdjustments;
        this.mConfiguration.setToDefaults();
        updateConfiguration(config, metrics, displayAdjustments.getCompatibilityInfo());
    }

    public DisplayAdjustments getDisplayAdjustments() {
        return this.mDisplayAdjustments;
    }

    @UnsupportedAppUsage
    public AssetManager getAssets() {
        return this.mAssets;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public DisplayMetrics getDisplayMetrics() {
        return this.mMetrics;
    }

    /* Access modifiers changed, original: 0000 */
    public Configuration getConfiguration() {
        return this.mConfiguration;
    }

    /* Access modifiers changed, original: 0000 */
    public Configuration[] getSizeConfigurations() {
        return this.mAssets.getSizeConfigurations();
    }

    /* Access modifiers changed, original: 0000 */
    public CompatibilityInfo getCompatibilityInfo() {
        return this.mDisplayAdjustments.getCompatibilityInfo();
    }

    private PluralRules getPluralRule() {
        PluralRules pluralRules;
        synchronized (sSync) {
            if (this.mPluralRule == null) {
                this.mPluralRule = PluralRules.forLocale(this.mConfiguration.getLocales().get(0));
            }
            pluralRules = this.mPluralRule;
        }
        return pluralRules;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void getValue(int id, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        if (!this.mAssets.getResourceValue(id, 0, outValue, resolveRefs)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            throw new NotFoundException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void getValueForDensity(int id, int density, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        if (!this.mAssets.getResourceValue(id, density, outValue, resolveRefs)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            throw new NotFoundException(stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void getValue(String name, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        int id = getIdentifier(name, "string", null);
        if (id != 0) {
            getValue(id, outValue, resolveRefs);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("String resource name ");
        stringBuilder.append(name);
        throw new NotFoundException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public int getIdentifier(String name, String defType, String defPackage) {
        if (name != null) {
            try {
                return Integer.parseInt(name);
            } catch (Exception e) {
                return this.mAssets.getResourceIdentifier(name, defType, defPackage);
            }
        }
        throw new NullPointerException("name is null");
    }

    /* Access modifiers changed, original: 0000 */
    public String getResourceName(int resid) throws NotFoundException {
        String str = this.mAssets.getResourceName(resid);
        if (str != null) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to find resource ID #0x");
        stringBuilder.append(Integer.toHexString(resid));
        throw new NotFoundException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public String getResourcePackageName(int resid) throws NotFoundException {
        String str = this.mAssets.getResourcePackageName(resid);
        if (str != null) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to find resource ID #0x");
        stringBuilder.append(Integer.toHexString(resid));
        throw new NotFoundException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public String getResourceTypeName(int resid) throws NotFoundException {
        String str = this.mAssets.getResourceTypeName(resid);
        if (str != null) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to find resource ID #0x");
        stringBuilder.append(Integer.toHexString(resid));
        throw new NotFoundException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public String getResourceEntryName(int resid) throws NotFoundException {
        String str = this.mAssets.getResourceEntryName(resid);
        if (str != null) {
            return str;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unable to find resource ID #0x");
        stringBuilder.append(Integer.toHexString(resid));
        throw new NotFoundException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public String getLastResourceResolution() throws NotFoundException {
        String str = this.mAssets.getLastResourceResolution();
        if (str != null) {
            return str;
        }
        throw new NotFoundException("Associated AssetManager hasn't resolved a resource");
    }

    /* Access modifiers changed, original: 0000 */
    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        PluralRules rule = getPluralRule();
        CharSequence res = this.mAssets.getResourceBagText(id, attrForQuantityCode(rule.select((double) quantity)));
        if (res != null) {
            return res;
        }
        res = this.mAssets.getResourceBagText(id, ID_OTHER);
        if (res != null) {
            return res;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Plural resource ID #0x");
        stringBuilder.append(Integer.toHexString(id));
        stringBuilder.append(" quantity=");
        stringBuilder.append(quantity);
        stringBuilder.append(" item=");
        stringBuilder.append(rule.select((double) quantity));
        throw new NotFoundException(stringBuilder.toString());
    }

    private static int attrForQuantityCode(java.lang.String r5) {
        /*
        r0 = r5.hashCode();
        r1 = 4;
        r2 = 3;
        r3 = 2;
        r4 = 1;
        switch(r0) {
            case 101272: goto L_0x0037;
            case 110182: goto L_0x002c;
            case 115276: goto L_0x0021;
            case 3343967: goto L_0x0017;
            case 3735208: goto L_0x000c;
            default: goto L_0x000b;
        };
    L_0x000b:
        goto L_0x0041;
    L_0x000c:
        r0 = "zero";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x000b;
    L_0x0015:
        r0 = 0;
        goto L_0x0042;
    L_0x0017:
        r0 = "many";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x000b;
    L_0x001f:
        r0 = r1;
        goto L_0x0042;
    L_0x0021:
        r0 = "two";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x000b;
    L_0x002a:
        r0 = r3;
        goto L_0x0042;
    L_0x002c:
        r0 = "one";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x000b;
    L_0x0035:
        r0 = r4;
        goto L_0x0042;
    L_0x0037:
        r0 = "few";
        r0 = r5.equals(r0);
        if (r0 == 0) goto L_0x000b;
    L_0x003f:
        r0 = r2;
        goto L_0x0042;
    L_0x0041:
        r0 = -1;
    L_0x0042:
        if (r0 == 0) goto L_0x0060;
    L_0x0044:
        if (r0 == r4) goto L_0x005c;
    L_0x0046:
        if (r0 == r3) goto L_0x0058;
    L_0x0048:
        if (r0 == r2) goto L_0x0054;
    L_0x004a:
        if (r0 == r1) goto L_0x0050;
    L_0x004c:
        r0 = 16777220; // 0x1000004 float:2.3509898E-38 double:8.289048E-317;
        return r0;
    L_0x0050:
        r0 = 16777225; // 0x1000009 float:2.3509912E-38 double:8.2890505E-317;
        return r0;
    L_0x0054:
        r0 = 16777224; // 0x1000008 float:2.350991E-38 double:8.28905E-317;
        return r0;
    L_0x0058:
        r0 = 16777223; // 0x1000007 float:2.3509907E-38 double:8.2890495E-317;
        return r0;
    L_0x005c:
        r0 = 16777222; // 0x1000006 float:2.3509904E-38 double:8.289049E-317;
        return r0;
    L_0x0060:
        r0 = 16777221; // 0x1000005 float:2.35099E-38 double:8.2890485E-317;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.ResourcesImpl.attrForQuantityCode(java.lang.String):int");
    }

    /* Access modifiers changed, original: 0000 */
    public AssetFileDescriptor openRawResourceFd(int id, TypedValue tempValue) throws NotFoundException {
        getValue(id, tempValue, true);
        try {
            return this.mAssets.openNonAssetFd(tempValue.assetCookie, tempValue.string.toString());
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("File ");
            stringBuilder.append(tempValue.string.toString());
            stringBuilder.append(" from drawable resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            throw new NotFoundException(stringBuilder.toString(), e);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public InputStream openRawResource(int id, TypedValue value) throws NotFoundException {
        getValue(id, value, true);
        try {
            return this.mAssets.openNonAsset(value.assetCookie, value.string.toString(), 2);
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("File ");
            stringBuilder.append(value.string == null ? "(null)" : value.string.toString());
            stringBuilder.append(" from drawable resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            NotFoundException rnf = new NotFoundException(stringBuilder.toString());
            rnf.initCause(e);
            throw rnf;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ConfigurationBoundResourceCache<Animator> getAnimatorCache() {
        return this.mAnimatorCache;
    }

    /* Access modifiers changed, original: 0000 */
    public ConfigurationBoundResourceCache<StateListAnimator> getStateListAnimatorCache() {
        return this.mStateListAnimatorCache;
    }

    public void updateConfiguration(Configuration config, DisplayMetrics metrics, CompatibilityInfo compat) {
        DisplayMetrics displayMetrics = metrics;
        CompatibilityInfo compatibilityInfo = compat;
        Trace.traceBegin(8192, "ResourcesImpl#updateConfiguration");
        try {
            synchronized (this.mAccessLock) {
                int width;
                int height;
                int keyboardHidden;
                if (compatibilityInfo != null) {
                    this.mDisplayAdjustments.setCompatibilityInfo(compatibilityInfo);
                }
                if (displayMetrics != null) {
                    this.mMetrics.setTo(displayMetrics);
                }
                this.mDisplayAdjustments.getCompatibilityInfo().applyToDisplayMetrics(this.mMetrics);
                int configChanges = calcConfigChanges(config);
                LocaleList locales = this.mConfiguration.getLocales();
                if (locales.isEmpty()) {
                    locales = LocaleList.getDefault();
                    this.mConfiguration.setLocales(locales);
                }
                if ((configChanges & 4) != 0 && locales.size() > 1) {
                    String[] availableLocales = this.mAssets.getNonSystemLocales();
                    if (LocaleList.isPseudoLocalesOnly(availableLocales)) {
                        availableLocales = this.mAssets.getLocales();
                        if (LocaleList.isPseudoLocalesOnly(availableLocales)) {
                            availableLocales = null;
                        }
                    }
                    if (availableLocales != null) {
                        Locale bestLocale = locales.getFirstMatchWithEnglishSupported(availableLocales);
                        if (!(bestLocale == null || bestLocale == locales.get(0))) {
                            this.mConfiguration.setLocales(new LocaleList(bestLocale, locales));
                        }
                    }
                }
                if (this.mConfiguration.densityDpi != 0) {
                    this.mMetrics.densityDpi = this.mConfiguration.densityDpi;
                    this.mMetrics.density = ((float) this.mConfiguration.densityDpi) * 0.00625f;
                }
                this.mMetrics.scaledDensity = this.mMetrics.density * (this.mConfiguration.fontScale != 0.0f ? this.mConfiguration.fontScale : 1.0f);
                if (this.mMetrics.widthPixels >= this.mMetrics.heightPixels) {
                    width = this.mMetrics.widthPixels;
                    height = this.mMetrics.heightPixels;
                } else {
                    width = this.mMetrics.heightPixels;
                    height = this.mMetrics.widthPixels;
                }
                if (this.mConfiguration.keyboardHidden == 1 && this.mConfiguration.hardKeyboardHidden == 2) {
                    keyboardHidden = 3;
                } else {
                    keyboardHidden = this.mConfiguration.keyboardHidden;
                }
                AssetManager assetManager = this.mAssets;
                int i = this.mConfiguration.mcc;
                int i2 = this.mConfiguration.mnc;
                String adjustLanguageTag = adjustLanguageTag(this.mConfiguration.getLocales().get(0).toLanguageTag());
                int i3 = this.mConfiguration.orientation;
                int i4 = this.mConfiguration.touchscreen;
                int i5 = this.mConfiguration.densityDpi;
                int i6 = this.mConfiguration.keyboard;
                int i7 = this.mConfiguration.navigation;
                int configChanges2 = configChanges;
                assetManager.setConfiguration(i, i2, adjustLanguageTag, i3, i4, i5, i6, keyboardHidden, i7, width, height, this.mConfiguration.smallestScreenWidthDp, this.mConfiguration.screenWidthDp, this.mConfiguration.screenHeightDp, this.mConfiguration.screenLayout, this.mConfiguration.uiMode, this.mConfiguration.colorMode, VERSION.RESOURCES_SDK_INT);
                i6 = configChanges2;
                this.mDrawableCache.onConfigurationChange(i6);
                this.mColorDrawableCache.onConfigurationChange(i6);
                this.mComplexColorCache.onConfigurationChange(i6);
                this.mAnimatorCache.onConfigurationChange(i6);
                this.mStateListAnimatorCache.onConfigurationChange(i6);
                flushLayoutCache();
            }
            synchronized (sSync) {
                if (this.mPluralRule != null) {
                    this.mPluralRule = PluralRules.forLocale(this.mConfiguration.getLocales().get(0));
                }
            }
            Trace.traceEnd(8192);
        } catch (Throwable th) {
            Trace.traceEnd(8192);
        }
    }

    public int calcConfigChanges(Configuration config) {
        if (config == null) {
            return -1;
        }
        this.mTmpConfig.setTo(config);
        int density = config.densityDpi;
        if (density == 0) {
            density = this.mMetrics.noncompatDensityDpi;
        }
        this.mDisplayAdjustments.getCompatibilityInfo().applyToConfiguration(density, this.mTmpConfig);
        if (this.mTmpConfig.getLocales().isEmpty()) {
            this.mTmpConfig.setLocales(LocaleList.getDefault());
        }
        return this.mConfiguration.updateFrom(this.mTmpConfig);
    }

    private static String adjustLanguageTag(String languageTag) {
        String language;
        String remainder;
        int separator = languageTag.indexOf(45);
        if (separator == -1) {
            language = languageTag;
            remainder = "";
        } else {
            language = languageTag.substring(0, separator);
            remainder = languageTag.substring(separator);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Locale.adjustLanguageCode(language));
        stringBuilder.append(remainder);
        return stringBuilder.toString();
    }

    public void flushLayoutCache() {
        synchronized (this.mCachedXmlBlocks) {
            Arrays.fill(this.mCachedXmlBlockCookies, 0);
            Arrays.fill(this.mCachedXmlBlockFiles, null);
            XmlBlock[] cachedXmlBlocks = this.mCachedXmlBlocks;
            for (int i = 0; i < 4; i++) {
                XmlBlock oldBlock = cachedXmlBlocks[i];
                if (oldBlock != null) {
                    oldBlock.close();
                }
            }
            Arrays.fill(cachedXmlBlocks, null);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Drawable loadDrawable(Resources wrapper, TypedValue value, int id, int density, Theme theme) throws NotFoundException {
        if (Extension.get().getExtension() != null) {
            return ((Interface) Extension.get().getExtension().asInterface()).loadDrawable(this, wrapper, value, id, density, theme);
        }
        return originalLoadDrawable(wrapper, value, id, density, theme);
    }

    /* Access modifiers changed, original: 0000 */
    public Drawable originalLoadDrawable(Resources wrapper, TypedValue value, int id, int density, Theme theme) throws NotFoundException {
        int i;
        Exception e;
        Exception e2;
        String name;
        StringBuilder stringBuilder;
        NotFoundException nfe;
        Resources resources = wrapper;
        TypedValue typedValue = value;
        int i2 = id;
        int i3 = density;
        Theme theme2 = theme;
        boolean z = i3 == 0 || typedValue.density == this.mMetrics.densityDpi;
        boolean useCache = z;
        if (i3 > 0 && typedValue.density > 0 && typedValue.density != 65535) {
            if (typedValue.density == i3) {
                typedValue.density = this.mMetrics.densityDpi;
            } else {
                typedValue.density = (typedValue.density * this.mMetrics.densityDpi) / i3;
            }
        }
        try {
            boolean isColorDrawable;
            DrawableCache caches;
            long key;
            ConstantState cs;
            Drawable dr;
            boolean needsNewDrawableAfterCache;
            if (typedValue.type < 28 || typedValue.type > 31) {
                isColorDrawable = false;
                caches = this.mDrawableCache;
                key = (((long) typedValue.assetCookie) << 32) | ((long) typedValue.data);
            } else {
                isColorDrawable = true;
                caches = this.mColorDrawableCache;
                key = (long) typedValue.data;
            }
            if (!this.mPreloading && useCache) {
                Drawable cachedDrawable = caches.getInstance(key, resources, theme2);
                if (cachedDrawable != null) {
                    cachedDrawable.setChangingConfigurations(typedValue.changingConfigurations);
                    return cachedDrawable;
                }
            }
            if (isColorDrawable) {
                cs = (ConstantState) sPreloadedColorDrawables.get(key);
            } else {
                cs = getPreloadedDrawable(resources, key, i2);
            }
            if (cs != null) {
                if (TRACE_FOR_DETAILED_PRELOAD && (i2 >>> 24) == 1 && Process.myUid() != 0) {
                    String name2 = getResourceName(i2);
                    if (name2 != null) {
                        String str = TAG_PRELOAD;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("Hit preloaded FW drawable #");
                        stringBuilder2.append(Integer.toHexString(id));
                        stringBuilder2.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        stringBuilder2.append(name2);
                        Log.d(str, stringBuilder2.toString());
                    }
                }
                dr = cs.newDrawable(resources);
            } else if (isColorDrawable) {
                dr = new ColorDrawable(typedValue.data);
            } else {
                dr = loadDrawableForCookie(wrapper, value, id, density);
            }
            if (dr instanceof DrawableContainer) {
                needsNewDrawableAfterCache = true;
            } else {
                needsNewDrawableAfterCache = false;
            }
            boolean canApplyTheme = dr != null && dr.canApplyTheme();
            if (canApplyTheme && theme2 != null) {
                dr = dr.mutate();
                dr.applyTheme(theme2);
                dr.clearMutated();
            }
            DrawableCache drawableCache;
            ConstantState constantState;
            if (dr != null) {
                dr.setChangingConfigurations(typedValue.changingConfigurations);
                if (useCache) {
                    i = 0;
                    try {
                        cacheDrawable(value, isColorDrawable, caches, theme, canApplyTheme, key, dr);
                        if (needsNewDrawableAfterCache) {
                            ConstantState state = dr.getConstantState();
                            if (state != null) {
                                dr = state.newDrawable(resources);
                            }
                        }
                    } catch (Exception e3) {
                        e = e3;
                        e2 = e;
                        try {
                            name = getResourceName(i2);
                        } catch (NotFoundException notFoundException) {
                            NotFoundException notFoundException2 = notFoundException2;
                            name = "(missing name)";
                        }
                        stringBuilder = new StringBuilder();
                        stringBuilder.append("Drawable ");
                        stringBuilder.append(name);
                        stringBuilder.append(" with resource ID #0x");
                        stringBuilder.append(Integer.toHexString(id));
                        nfe = new NotFoundException(stringBuilder.toString(), e2);
                        nfe.setStackTrace(new StackTraceElement[i]);
                        throw nfe;
                    }
                }
                long j = key;
                drawableCache = caches;
                constantState = cs;
            } else {
                drawableCache = caches;
                constantState = cs;
            }
            return dr;
        } catch (Exception e4) {
            e = e4;
            i = 0;
            e2 = e;
            name = getResourceName(i2);
            stringBuilder = new StringBuilder();
            stringBuilder.append("Drawable ");
            stringBuilder.append(name);
            stringBuilder.append(" with resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            nfe = new NotFoundException(stringBuilder.toString(), e2);
            nfe.setStackTrace(new StackTraceElement[i]);
            throw nfe;
        }
    }

    private void cacheDrawable(TypedValue value, boolean isColorDrawable, DrawableCache caches, Theme theme, boolean usesTheme, long key, Drawable dr) {
        TypedValue typedValue = value;
        long j = key;
        ConstantState cs = dr.getConstantState();
        if (cs != null) {
            if (this.mPreloading) {
                int changingConfigs = cs.getChangingConfigurations();
                if (isColorDrawable) {
                    if (verifyPreloadConfig(changingConfigs, 0, typedValue.resourceId, "drawable")) {
                        sPreloadedColorDrawables.put(j, cs);
                    }
                } else if (verifyPreloadConfig(changingConfigs, 8192, typedValue.resourceId, "drawable")) {
                    if ((changingConfigs & 8192) == 0) {
                        sPreloadedDrawables[0].put(j, cs);
                        sPreloadedDrawables[1].put(j, cs);
                    } else {
                        sPreloadedDrawables[this.mConfiguration.getLayoutDirection()].put(j, cs);
                    }
                }
            } else {
                synchronized (this.mAccessLock) {
                    caches.put(key, theme, cs, usesTheme);
                }
            }
        }
    }

    private boolean verifyPreloadConfig(int changingConfigurations, int allowVarying, int resourceId, String name) {
        if (((-1073745921 & changingConfigurations) & (~allowVarying)) == 0) {
            return true;
        }
        String resName;
        try {
            resName = getResourceName(resourceId);
        } catch (NotFoundException e) {
            resName = "?";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Preloaded ");
        stringBuilder.append(name);
        stringBuilder.append(" resource #0x");
        stringBuilder.append(Integer.toHexString(resourceId));
        stringBuilder.append(" (");
        stringBuilder.append(resName);
        stringBuilder.append(") that varies with configuration!!");
        Log.w(TAG, stringBuilder.toString());
        return false;
    }

    private Drawable decodeImageDrawable(AssetInputStream ais, Resources wrapper, TypedValue value) {
        try {
            return ImageDecoder.decodeDrawable(new AssetInputStreamSource(ais, wrapper, value), -$$Lambda$ResourcesImpl$99dm2ENnzo9b0SIUjUj2Kl3pi90.INSTANCE);
        } catch (IOException e) {
            return null;
        }
    }

    private Drawable loadDrawableForCookie(Resources wrapper, TypedValue value, int id, int density) {
        LookupStack lookupStack;
        long j;
        Throwable th;
        StringBuilder stringBuilder;
        NotFoundException rnf;
        TypedValue typedValue = value;
        int i = id;
        if (typedValue.string != null) {
            long startTime;
            int startBitmapCount;
            long startBitmapSize;
            int startDrawableCount;
            String file = typedValue.string.toString();
            if (TRACE_FOR_DETAILED_PRELOAD) {
                long startTime2 = System.nanoTime();
                int startBitmapCount2 = Bitmap.sPreloadTracingNumInstantiatedBitmaps;
                startTime = startTime2;
                startBitmapCount = startBitmapCount2;
                startBitmapSize = Bitmap.sPreloadTracingTotalBitmapsSize;
                startDrawableCount = sPreloadTracingNumLoadedDrawables;
            } else {
                startTime = 0;
                startBitmapCount = 0;
                startBitmapSize = 0;
                startDrawableCount = 0;
            }
            Trace.traceBegin(8192, file);
            LookupStack stack = (LookupStack) this.mLookupStack.get();
            int i2;
            try {
                if (stack.contains(i)) {
                    lookupStack = stack;
                    j = 8192;
                    i2 = startDrawableCount;
                    throw new Exception("Recursive reference in drawable");
                }
                Drawable dr;
                stack.push(i);
                try {
                    if (file.endsWith(".xml")) {
                        try {
                            Resources resources;
                            if (file.startsWith("res/color/")) {
                                lookupStack = stack;
                                j = 8192;
                                try {
                                    dr = loadColorOrXmlDrawable(wrapper, value, id, density, file);
                                    resources = wrapper;
                                } catch (Throwable th2) {
                                    th = th2;
                                    i2 = startDrawableCount;
                                    try {
                                        lookupStack.pop();
                                        throw th;
                                    } catch (Exception | StackOverflowError e) {
                                        th = e;
                                        Trace.traceEnd(j);
                                        stringBuilder = new StringBuilder();
                                        stringBuilder.append("File ");
                                        stringBuilder.append(file);
                                        stringBuilder.append(" from drawable resource ID #0x");
                                        stringBuilder.append(Integer.toHexString(id));
                                        rnf = new NotFoundException(stringBuilder.toString());
                                        rnf.initCause(th);
                                        throw rnf;
                                    }
                                }
                            }
                            lookupStack = stack;
                            j = 8192;
                            dr = loadXmlDrawable(wrapper, value, id, density, file);
                            resources = wrapper;
                        } catch (Throwable th3) {
                            th = th3;
                            lookupStack = stack;
                            j = 8192;
                            i2 = startDrawableCount;
                            lookupStack.pop();
                            throw th;
                        }
                    }
                    lookupStack = stack;
                    j = 8192;
                    try {
                        dr = createFromResourceStream(wrapper, typedValue, this.mAssets.openNonAsset(typedValue.assetCookie, file, 2), i);
                    } catch (Throwable th4) {
                        th = th4;
                        i2 = startDrawableCount;
                        lookupStack.pop();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    lookupStack = stack;
                    j = 8192;
                    i2 = startDrawableCount;
                    lookupStack.pop();
                    throw th;
                }
                try {
                    lookupStack.pop();
                    Trace.traceEnd(j);
                    if (!TRACE_FOR_DETAILED_PRELOAD) {
                    } else if ((i >>> 24) == 1) {
                        String name = getResourceName(i);
                        if (name != null) {
                            int loadedBitmapCount = Bitmap.sPreloadTracingNumInstantiatedBitmaps - startBitmapCount;
                            long time = System.nanoTime() - startTime;
                            long loadedBitmapSize = Bitmap.sPreloadTracingTotalBitmapsSize - startBitmapSize;
                            int i3 = sPreloadTracingNumLoadedDrawables;
                            int loadedDrawables = i3 - startDrawableCount;
                            boolean z = true;
                            sPreloadTracingNumLoadedDrawables = i3 + 1;
                            if (Process.myUid() != 0) {
                                z = false;
                            }
                            boolean isRoot = z;
                            StringBuilder stringBuilder2 = new StringBuilder();
                            if (isRoot) {
                                boolean z2 = isRoot;
                                isRoot = "Preloaded FW drawable #";
                            } else {
                                isRoot = "Loaded non-preloaded FW drawable #";
                            }
                            stringBuilder2.append(isRoot);
                            stringBuilder2.append(Integer.toHexString(id));
                            String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
                            stringBuilder2.append(str);
                            stringBuilder2.append(name);
                            stringBuilder2.append(str);
                            stringBuilder2.append(file);
                            stringBuilder2.append(str);
                            stringBuilder2.append(dr.getClass().getCanonicalName());
                            stringBuilder2.append(" #nested_drawables= ");
                            stringBuilder2.append(loadedDrawables);
                            stringBuilder2.append(" #bitmaps= ");
                            stringBuilder2.append(loadedBitmapCount);
                            stringBuilder2.append(" total_bitmap_size= ");
                            stringBuilder2.append(loadedBitmapSize);
                            stringBuilder2.append(" in[us] ");
                            stringBuilder2.append(time / 1000);
                            Log.d(TAG_PRELOAD, stringBuilder2.toString());
                        } else {
                            i2 = startDrawableCount;
                        }
                    }
                    return dr;
                } catch (Exception | StackOverflowError e2) {
                    th = e2;
                    i2 = startDrawableCount;
                    Trace.traceEnd(j);
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("File ");
                    stringBuilder.append(file);
                    stringBuilder.append(" from drawable resource ID #0x");
                    stringBuilder.append(Integer.toHexString(id));
                    rnf = new NotFoundException(stringBuilder.toString());
                    rnf.initCause(th);
                    throw rnf;
                }
            } catch (Exception | StackOverflowError e3) {
                th = e3;
                lookupStack = stack;
                j = 8192;
                i2 = startDrawableCount;
                Trace.traceEnd(j);
                stringBuilder = new StringBuilder();
                stringBuilder.append("File ");
                stringBuilder.append(file);
                stringBuilder.append(" from drawable resource ID #0x");
                stringBuilder.append(Integer.toHexString(id));
                rnf = new NotFoundException(stringBuilder.toString());
                rnf.initCause(th);
                throw rnf;
            }
        }
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append("Resource \"");
        stringBuilder3.append(getResourceName(i));
        stringBuilder3.append("\" (");
        stringBuilder3.append(Integer.toHexString(id));
        stringBuilder3.append(") is not a Drawable (color or path): ");
        stringBuilder3.append(typedValue);
        throw new NotFoundException(stringBuilder3.toString());
    }

    private Drawable loadColorOrXmlDrawable(Resources wrapper, TypedValue value, int id, int density, String file) {
        try {
            return new ColorStateListDrawable(loadColorStateList(wrapper, value, id, null));
        } catch (NotFoundException originalException) {
            try {
                return loadXmlDrawable(wrapper, value, id, density, file);
            } catch (Exception e) {
                throw originalException;
            }
        }
    }

    /* JADX WARNING: Missing block: B:10:0x001c, code skipped:
            if (r0 != null) goto L_0x001e;
     */
    /* JADX WARNING: Missing block: B:12:?, code skipped:
            r0.close();
     */
    /* JADX WARNING: Missing block: B:13:0x0022, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:14:0x0023, code skipped:
            r1.addSuppressed(r3);
     */
    private android.graphics.drawable.Drawable loadXmlDrawable(android.content.res.Resources r9, android.util.TypedValue r10, int r11, int r12, java.lang.String r13) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        /*
        r8 = this;
        r0 = r10.assetCookie;
        r1 = "drawable";
        r0 = r8.loadXmlResourceParser(r13, r11, r0, r1);
        r5 = 0;
        r2 = r9;
        r3 = r0;
        r4 = r12;
        r6 = r10;
        r7 = r11;
        r1 = createFromXmlForDensity(r2, r3, r4, r5, r6, r7);	 Catch:{ all -> 0x0019 }
        if (r0 == 0) goto L_0x0018;
    L_0x0015:
        r0.close();
    L_0x0018:
        return r1;
    L_0x0019:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x001b }
    L_0x001b:
        r2 = move-exception;
        if (r0 == 0) goto L_0x0026;
    L_0x001e:
        r0.close();	 Catch:{ all -> 0x0022 }
        goto L_0x0026;
    L_0x0022:
        r3 = move-exception;
        r1.addSuppressed(r3);
    L_0x0026:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.ResourcesImpl.loadXmlDrawable(android.content.res.Resources, android.util.TypedValue, int, int, java.lang.String):android.graphics.drawable.Drawable");
    }

    public Typeface loadFont(Resources wrapper, TypedValue value, int id) {
        StringBuilder stringBuilder;
        String str = TAG;
        if (value.string != null) {
            String file = value.string.toString();
            if (!file.startsWith("res/")) {
                return null;
            }
            Typeface cached = Typeface.findFromCache(this.mAssets, file);
            if (cached != null) {
                return cached;
            }
            Trace.traceBegin(8192, file);
            try {
                if (file.endsWith("xml")) {
                    FamilyResourceEntry familyEntry = FontResourcesParser.parse(loadXmlResourceParser(file, id, value.assetCookie, "font"), wrapper);
                    if (familyEntry == null) {
                        Trace.traceEnd(8192);
                        return null;
                    }
                    str = Typeface.createFromResources(familyEntry, this.mAssets, file);
                    Trace.traceEnd(8192);
                    return str;
                }
                str = new Builder(this.mAssets, file, false, value.assetCookie).build();
                Trace.traceEnd(8192);
                return str;
            } catch (XmlPullParserException e) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to parse xml resource ");
                stringBuilder.append(file);
                Log.e(str, stringBuilder.toString(), e);
            } catch (IOException e2) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to read xml resource ");
                stringBuilder.append(file);
                Log.e(str, stringBuilder.toString(), e2);
            } catch (Throwable th) {
                Trace.traceEnd(8192);
            }
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Resource \"");
            stringBuilder2.append(getResourceName(id));
            stringBuilder2.append("\" (");
            stringBuilder2.append(Integer.toHexString(id));
            stringBuilder2.append(") is not a Font: ");
            stringBuilder2.append(value);
            throw new NotFoundException(stringBuilder2.toString());
        }
        Trace.traceEnd(8192);
        return null;
    }

    private ComplexColor loadComplexColorFromName(Resources wrapper, Theme theme, TypedValue value, int id) {
        long key = obtainColorKey(id, value);
        ConfigurationBoundResourceCache<ComplexColor> cache = this.mComplexColorCache;
        ComplexColor complexColor = (ComplexColor) cache.getInstance(key, wrapper, theme);
        if (complexColor != null) {
            return complexColor;
        }
        ConstantState<ComplexColor> factory = (ConstantState) sPreloadedComplexColors.get(key);
        if (factory != null) {
            complexColor = (ComplexColor) factory.newInstance(wrapper, theme);
        }
        if (complexColor == null) {
            complexColor = loadComplexColorForCookie(wrapper, value, id, theme);
        }
        if (complexColor != null) {
            complexColor.setBaseChangingConfigurations(value.changingConfigurations);
            if (!this.mPreloading) {
                cache.put(key, theme, complexColor.getConstantState());
            } else if (verifyPreloadConfig(complexColor.getChangingConfigurations(), 0, value.resourceId, "color")) {
                sPreloadedComplexColors.put(key, complexColor.getConstantState());
            }
        }
        return complexColor;
    }

    /* Access modifiers changed, original: 0000 */
    public ComplexColor loadComplexColor(Resources wrapper, TypedValue value, int id, Theme theme) {
        long key = obtainColorKey(id, value);
        if (value.type >= 28 && value.type <= 31) {
            return getColorStateListFromInt(value, key);
        }
        String file = value.string.toString();
        String str = "File ";
        if (file.endsWith(".xml")) {
            try {
                return loadComplexColorFromName(wrapper, theme, value, id);
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(file);
                stringBuilder.append(" from complex color resource ID #0x");
                stringBuilder.append(Integer.toHexString(id));
                NotFoundException rnf = new NotFoundException(stringBuilder.toString());
                rnf.initCause(e);
                throw rnf;
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(file);
        stringBuilder2.append(" from drawable resource ID #0x");
        stringBuilder2.append(Integer.toHexString(id));
        stringBuilder2.append(": .xml extension required");
        throw new NotFoundException(stringBuilder2.toString());
    }

    /* Access modifiers changed, original: 0000 */
    public ColorStateList loadColorStateList(Resources wrapper, TypedValue value, int id, Theme theme) throws NotFoundException {
        long key = obtainColorKey(id, value);
        if (value.type >= 28 && value.type <= 31) {
            return getColorStateListFromInt(value, key);
        }
        ComplexColor complexColor = loadComplexColorFromName(wrapper, theme, value, id);
        if (complexColor != null && (complexColor instanceof ColorStateList)) {
            return (ColorStateList) complexColor;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Can't find ColorStateList from drawable resource ID #0x");
        stringBuilder.append(Integer.toHexString(id));
        throw new NotFoundException(stringBuilder.toString());
    }

    private long obtainColorKey(int id, TypedValue value) {
        if ((id >>> 24) == (R.attr.miui_version >>> 24)) {
            return ((((long) value.type) << 56) | (((long) value.assetCookie) << 32)) | ((long) value.data);
        }
        return (((long) value.assetCookie) << 32) | ((long) value.data);
    }

    private ColorStateList getColorStateListFromInt(TypedValue value, long key) {
        ConstantState<ComplexColor> factory = (ConstantState) sPreloadedComplexColors.get(key);
        if (factory != null) {
            return (ColorStateList) factory.newInstance();
        }
        ColorStateList csl = ColorStateList.valueOf(value.data);
        if (this.mPreloading && verifyPreloadConfig(value.changingConfigurations, 0, value.resourceId, "color")) {
            sPreloadedComplexColors.put(key, csl.getConstantState());
        }
        return csl;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x005c A:{SYNTHETIC, Splitter:B:21:0x005c} */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0034 A:{Catch:{ Exception -> 0x0064 }} */
    private android.content.res.ComplexColor loadComplexColorForCookie(android.content.res.Resources r11, android.util.TypedValue r12, int r13, android.content.res.Resources.Theme r14) {
        /*
        r10 = this;
        r0 = r12.string;
        if (r0 == 0) goto L_0x00b5;
    L_0x0004:
        r0 = r12.string;
        r0 = r0.toString();
        r1 = 0;
        r2 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        android.os.Trace.traceBegin(r2, r0);
        r4 = ".xml";
        r4 = r0.endsWith(r4);
        r5 = "File ";
        if (r4 == 0) goto L_0x008c;
    L_0x001a:
        r4 = r12.assetCookie;	 Catch:{ Exception -> 0x0064 }
        r6 = "ComplexColor";
        r4 = r10.loadXmlResourceParser(r0, r13, r4, r6);	 Catch:{ Exception -> 0x0064 }
        r6 = android.util.Xml.asAttributeSet(r4);	 Catch:{ Exception -> 0x0064 }
    L_0x0026:
        r7 = r4.next();	 Catch:{ Exception -> 0x0064 }
        r8 = r7;
        r9 = 2;
        if (r7 == r9) goto L_0x0032;
    L_0x002e:
        r7 = 1;
        if (r8 == r7) goto L_0x0032;
    L_0x0031:
        goto L_0x0026;
    L_0x0032:
        if (r8 != r9) goto L_0x005c;
    L_0x0034:
        r7 = r4.getName();	 Catch:{ Exception -> 0x0064 }
        r9 = "gradient";
        r9 = r7.equals(r9);	 Catch:{ Exception -> 0x0064 }
        if (r9 == 0) goto L_0x0046;
    L_0x0040:
        r9 = android.content.res.GradientColor.createFromXmlInner(r11, r4, r6, r14);	 Catch:{ Exception -> 0x0064 }
        r1 = r9;
        goto L_0x0054;
    L_0x0046:
        r9 = "selector";
        r9 = r7.equals(r9);	 Catch:{ Exception -> 0x0064 }
        if (r9 == 0) goto L_0x0054;
    L_0x004f:
        r9 = android.content.res.ColorStateList.createFromXmlInner(r11, r4, r6, r14);	 Catch:{ Exception -> 0x0064 }
        r1 = r9;
    L_0x0054:
        r4.close();	 Catch:{ Exception -> 0x0064 }
        android.os.Trace.traceEnd(r2);
        return r1;
    L_0x005c:
        r7 = new org.xmlpull.v1.XmlPullParserException;	 Catch:{ Exception -> 0x0064 }
        r9 = "No start tag found";
        r7.<init>(r9);	 Catch:{ Exception -> 0x0064 }
        throw r7;	 Catch:{ Exception -> 0x0064 }
    L_0x0064:
        r4 = move-exception;
        android.os.Trace.traceEnd(r2);
        r2 = new android.content.res.Resources$NotFoundException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r5);
        r3.append(r0);
        r5 = " from ComplexColor resource ID #0x";
        r3.append(r5);
        r5 = java.lang.Integer.toHexString(r13);
        r3.append(r5);
        r3 = r3.toString();
        r2.<init>(r3);
        r2.initCause(r4);
        throw r2;
    L_0x008c:
        android.os.Trace.traceEnd(r2);
        r2 = new android.content.res.Resources$NotFoundException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r3.append(r5);
        r3.append(r0);
        r4 = " from drawable resource ID #0x";
        r3.append(r4);
        r4 = java.lang.Integer.toHexString(r13);
        r3.append(r4);
        r4 = ": .xml extension required";
        r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x00b5:
        r0 = new java.lang.UnsupportedOperationException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r2 = "Can't convert to ComplexColor: type=0x";
        r1.append(r2);
        r2 = r12.type;
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.content.res.ResourcesImpl.loadComplexColorForCookie(android.content.res.Resources, android.util.TypedValue, int, android.content.res.Resources$Theme):android.content.res.ComplexColor");
    }

    /* Access modifiers changed, original: 0000 */
    public XmlResourceParser loadXmlResourceParser(String file, int id, int assetCookie, String type) throws NotFoundException {
        if (id != 0) {
            try {
                synchronized (this.mCachedXmlBlocks) {
                    int[] cachedXmlBlockCookies = this.mCachedXmlBlockCookies;
                    String[] cachedXmlBlockFiles = this.mCachedXmlBlockFiles;
                    XmlBlock[] cachedXmlBlocks = this.mCachedXmlBlocks;
                    int num = cachedXmlBlockFiles.length;
                    int i = 0;
                    while (i < num) {
                        if (cachedXmlBlockCookies[i] == assetCookie && cachedXmlBlockFiles[i] != null && cachedXmlBlockFiles[i].equals(file)) {
                            XmlResourceParser newParser = cachedXmlBlocks[i].newParser(id);
                            return newParser;
                        }
                        i++;
                    }
                    XmlBlock block = this.mAssets.openXmlBlockAsset(assetCookie, file);
                    if (block != null) {
                        int pos = (this.mLastCachedXmlBlockIndex + 1) % num;
                        this.mLastCachedXmlBlockIndex = pos;
                        XmlBlock oldBlock = cachedXmlBlocks[pos];
                        if (oldBlock != null) {
                            oldBlock.close();
                        }
                        cachedXmlBlockCookies[pos] = assetCookie;
                        cachedXmlBlockFiles[pos] = file;
                        cachedXmlBlocks[pos] = block;
                        XmlResourceParser newParser2 = block.newParser(id);
                        return newParser2;
                    }
                }
            } catch (Exception e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("File ");
                stringBuilder.append(file);
                stringBuilder.append(" from xml type ");
                stringBuilder.append(type);
                stringBuilder.append(" resource ID #0x");
                stringBuilder.append(Integer.toHexString(id));
                NotFoundException rnf = new NotFoundException(stringBuilder.toString());
                rnf.initCause(e);
                throw rnf;
            }
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("File ");
        stringBuilder2.append(file);
        stringBuilder2.append(" from xml type ");
        stringBuilder2.append(type);
        stringBuilder2.append(" resource ID #0x");
        stringBuilder2.append(Integer.toHexString(id));
        throw new NotFoundException(stringBuilder2.toString());
    }

    public final void startPreloading() {
        synchronized (sSync) {
            if (sPreloaded) {
                throw new IllegalStateException("Resources already preloaded");
            }
            sPreloaded = true;
            this.mPreloading = true;
            this.mConfiguration.densityDpi = DisplayMetrics.DENSITY_DEVICE;
            updateConfiguration(null, null, null);
            if (TRACE_FOR_DETAILED_PRELOAD) {
                this.mPreloadTracingPreloadStartTime = SystemClock.uptimeMillis();
                this.mPreloadTracingStartBitmapSize = Bitmap.sPreloadTracingTotalBitmapsSize;
                this.mPreloadTracingStartBitmapCount = (long) Bitmap.sPreloadTracingNumInstantiatedBitmaps;
                Log.d(TAG_PRELOAD, "Preload starting");
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void finishPreloading() {
        if (this.mPreloading) {
            if (TRACE_FOR_DETAILED_PRELOAD) {
                long time = SystemClock.uptimeMillis() - this.mPreloadTracingPreloadStartTime;
                long size = Bitmap.sPreloadTracingTotalBitmapsSize - this.mPreloadTracingStartBitmapSize;
                long count = ((long) Bitmap.sPreloadTracingNumInstantiatedBitmaps) - this.mPreloadTracingStartBitmapCount;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Preload finished, ");
                stringBuilder.append(count);
                stringBuilder.append(" bitmaps of ");
                stringBuilder.append(size);
                stringBuilder.append(" bytes in ");
                stringBuilder.append(time);
                stringBuilder.append(" ms");
                Log.d(TAG_PRELOAD, stringBuilder.toString());
            }
            this.mPreloading = false;
            flushLayoutCache();
        }
    }

    static int getAttributeSetSourceResId(AttributeSet set) {
        if (set == null || !(set instanceof Parser)) {
            return 0;
        }
        return ((Parser) set).getSourceResId();
    }

    /* Access modifiers changed, original: 0000 */
    public LongSparseArray<ConstantState> getPreloadedDrawables() {
        return sPreloadedDrawables[0];
    }

    /* Access modifiers changed, original: 0000 */
    public ThemeImpl newThemeImpl() {
        return new ThemeImpl();
    }

    /* Access modifiers changed, original: 0000 */
    public ThemeImpl newThemeImpl(ThemeKey key) {
        ThemeImpl impl = new ThemeImpl();
        impl.mKey.setTo(key);
        impl.rebase();
        return impl;
    }

    static void clearPreloadedCache() {
        sPreloadedDrawables[0].clear();
        sPreloadedDrawables[1].clear();
        sPreloadedColorDrawables.clear();
    }

    /* Access modifiers changed, original: 0000 */
    public ConstantState getPreloadedDrawable(Resources wrapper, long key, int id) {
        ConstantState cs = (ConstantState) sPreloadedDrawables[this.mConfiguration.getLayoutDirection()].get(key);
        if (cs == null || !wrapper.isPreloadOverlayed(id)) {
            return cs;
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public Drawable createFromResourceStream(Resources res, TypedValue value, InputStream is, int id) throws IOException {
        Drawable dr = res.loadOverlayDrawable(value, id);
        if (dr == null) {
            return decodeImageDrawable((AssetInputStream) is, res, value);
        }
        is.close();
        return dr;
    }

    protected static Drawable createFromXmlForDensity(Resources res, XmlPullParser parser, int density, Theme theme, TypedValue value, int id) throws XmlPullParserException, IOException {
        Drawable dr = res.loadOverlayDrawable(value, id);
        if (dr == null) {
            return Drawable.createFromXmlForDensity(res, parser, density, theme);
        }
        return dr;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isPreloading() {
        return this.mPreloading;
    }
}
