package android.content.res;

import android.animation.Animator;
import android.animation.StateListAnimator;
import android.annotation.UnsupportedAppUsage;
import android.content.res.ResourcesImpl.ThemeImpl;
import android.graphics.Movie;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.graphics.drawable.DrawableInflater;
import android.miui.ResourcesManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Pools.SynchronizedPool;
import android.util.TypedValue;
import android.view.DisplayAdjustments;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewHierarchyEncoder;
import com.android.internal.R;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.GrowingArrayUtils;
import com.android.internal.util.XmlUtils;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParserException;

public class Resources {
    public static final int ID_NULL = 0;
    private static final int MIN_THEME_REFS_FLUSH_SIZE = 32;
    static final String TAG = "Resources";
    @UnsupportedAppUsage
    static Resources mSystem = null;
    private static final Object sSync = new Object();
    @UnsupportedAppUsage
    final ClassLoader mClassLoader;
    @UnsupportedAppUsage
    private DrawableInflater mDrawableInflater;
    @UnsupportedAppUsage
    private ResourcesImpl mResourcesImpl;
    private final ArrayList<WeakReference<Theme>> mThemeRefs;
    private int mThemeRefsNextFlushSize;
    @UnsupportedAppUsage
    private TypedValue mTmpValue;
    private final Object mTmpValueLock;
    @UnsupportedAppUsage
    final SynchronizedPool<TypedArray> mTypedArrayPool;

    public static class NotFoundException extends RuntimeException {
        public NotFoundException(String name) {
            super(name);
        }

        public NotFoundException(String name, Exception cause) {
            super(name, cause);
        }
    }

    public final class Theme {
        @UnsupportedAppUsage
        private ThemeImpl mThemeImpl;

        private Theme() {
        }

        /* Access modifiers changed, original: 0000 */
        public void setImpl(ThemeImpl impl) {
            this.mThemeImpl = impl;
        }

        public void applyStyle(int resId, boolean force) {
            this.mThemeImpl.applyStyle(resId, force);
        }

        public void setTo(Theme other) {
            this.mThemeImpl.setTo(other.mThemeImpl);
        }

        public TypedArray obtainStyledAttributes(int[] attrs) {
            return Resources.this.loadOverlayTypedArray(this.mThemeImpl.obtainStyledAttributes(this, null, attrs, 0, 0));
        }

        public TypedArray obtainStyledAttributes(int resId, int[] attrs) throws NotFoundException {
            return Resources.this.loadOverlayTypedArray(this.mThemeImpl.obtainStyledAttributes(this, null, attrs, 0, resId));
        }

        public TypedArray obtainStyledAttributes(AttributeSet set, int[] attrs, int defStyleAttr, int defStyleRes) {
            return Resources.this.loadOverlayTypedArray(this.mThemeImpl.obtainStyledAttributes(this, set, attrs, defStyleAttr, defStyleRes));
        }

        @UnsupportedAppUsage
        public TypedArray resolveAttributes(int[] values, int[] attrs) {
            return this.mThemeImpl.resolveAttributes(this, values, attrs);
        }

        public boolean resolveAttribute(int resid, TypedValue outValue, boolean resolveRefs) {
            boolean got = this.mThemeImpl.resolveAttribute(resid, outValue, resolveRefs);
            if (got) {
                Resources.this.loadOverlayValue(outValue, resid);
            }
            return got;
        }

        public int[] getAllAttributes() {
            return this.mThemeImpl.getAllAttributes();
        }

        public Resources getResources() {
            return Resources.this;
        }

        public Drawable getDrawable(int id) throws NotFoundException {
            return Resources.this.getDrawable(id, this);
        }

        public int getChangingConfigurations() {
            return this.mThemeImpl.getChangingConfigurations();
        }

        public void dump(int priority, String tag, String prefix) {
            this.mThemeImpl.dump(priority, tag, prefix);
        }

        /* Access modifiers changed, original: 0000 */
        public long getNativeTheme() {
            return this.mThemeImpl.getNativeTheme();
        }

        /* Access modifiers changed, original: 0000 */
        public int getAppliedStyleResId() {
            return this.mThemeImpl.getAppliedStyleResId();
        }

        public ThemeKey getKey() {
            return this.mThemeImpl.getKey();
        }

        private String getResourceNameFromHexString(String hexString) {
            return Resources.this.getResourceName(Integer.parseInt(hexString, 16));
        }

        @ExportedProperty(category = "theme", hasAdjacentMapping = true)
        public String[] getTheme() {
            return this.mThemeImpl.getTheme();
        }

        public void encode(ViewHierarchyEncoder encoder) {
            encoder.beginObject(this);
            String[] properties = getTheme();
            for (int i = 0; i < properties.length; i += 2) {
                encoder.addProperty(properties[i], properties[i + 1]);
            }
            encoder.endObject();
        }

        public void rebase() {
            this.mThemeImpl.rebase();
        }

        public int getExplicitStyle(AttributeSet set) {
            if (set == null) {
                return 0;
            }
            int styleAttr = set.getStyleAttribute();
            if (styleAttr == 0) {
                return 0;
            }
            String styleAttrType = getResources().getResourceTypeName(styleAttr);
            if ("attr".equals(styleAttrType)) {
                TypedValue explicitStyle = new TypedValue();
                if (resolveAttribute(styleAttr, explicitStyle, true)) {
                    return explicitStyle.resourceId;
                }
            } else if (TtmlUtils.TAG_STYLE.equals(styleAttrType)) {
                return styleAttr;
            }
            return 0;
        }

        public int[] getAttributeResolutionStack(int defStyleAttr, int defStyleRes, int explicitStyleRes) {
            int[] stack = this.mThemeImpl.getAttributeResolutionStack(defStyleAttr, defStyleRes, explicitStyleRes);
            if (stack == null) {
                return new int[0];
            }
            return stack;
        }
    }

    static class ThemeKey implements Cloneable {
        int mCount;
        boolean[] mForce;
        private int mHashCode = 0;
        int[] mResId;

        ThemeKey() {
        }

        public void append(int resId, boolean force) {
            if (this.mResId == null) {
                this.mResId = new int[4];
            }
            if (this.mForce == null) {
                this.mForce = new boolean[4];
            }
            this.mResId = GrowingArrayUtils.append(this.mResId, this.mCount, resId);
            this.mForce = GrowingArrayUtils.append(this.mForce, this.mCount, force);
            this.mCount++;
            this.mHashCode = (((this.mHashCode * 31) + resId) * 31) + force;
        }

        public void setTo(ThemeKey other) {
            int[] iArr = other.mResId;
            boolean[] zArr = null;
            this.mResId = iArr == null ? null : (int[]) iArr.clone();
            boolean[] zArr2 = other.mForce;
            if (zArr2 != null) {
                zArr = (boolean[]) zArr2.clone();
            }
            this.mForce = zArr;
            this.mCount = other.mCount;
        }

        public int hashCode() {
            return this.mHashCode;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass() || hashCode() != o.hashCode()) {
                return false;
            }
            ThemeKey t = (ThemeKey) o;
            if (this.mCount != t.mCount) {
                return false;
            }
            int N = this.mCount;
            int i = 0;
            while (i < N) {
                if (this.mResId[i] != t.mResId[i] || this.mForce[i] != t.mForce[i]) {
                    return false;
                }
                i++;
            }
            return true;
        }

        public ThemeKey clone() {
            ThemeKey other = new ThemeKey();
            other.mResId = this.mResId;
            other.mForce = this.mForce;
            other.mCount = this.mCount;
            other.mHashCode = this.mHashCode;
            return other;
        }
    }

    @UnsupportedAppUsage
    public static int selectDefaultTheme(int curTheme, int targetSdkVersion) {
        return selectSystemTheme(curTheme, targetSdkVersion, 16973829, 16973931, 16974120, 16974143);
    }

    public static int selectSystemTheme(int curTheme, int targetSdkVersion, int orig, int holo, int dark, int deviceDefault) {
        if (curTheme != 0) {
            return curTheme;
        }
        if (targetSdkVersion < 11) {
            return orig;
        }
        if (targetSdkVersion < 14) {
            return holo;
        }
        if (targetSdkVersion < 24) {
            return dark;
        }
        return deviceDefault;
    }

    public static Resources getSystem() {
        Resources ret;
        synchronized (sSync) {
            ret = mSystem;
            if (ret == null) {
                ret = new MiuiResources();
                mSystem = ret;
                ResourcesManager.initMiuiResource(ret, null);
            }
        }
        return ret;
    }

    @Deprecated
    public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
        this(null);
        this.mResourcesImpl = new MiuiResourcesImpl(assets, metrics, config, new DisplayAdjustments());
    }

    @UnsupportedAppUsage
    public Resources(ClassLoader classLoader) {
        this.mTypedArrayPool = new SynchronizedPool(5);
        this.mTmpValueLock = new Object();
        this.mTmpValue = new TypedValue();
        this.mThemeRefs = new ArrayList();
        this.mThemeRefsNextFlushSize = 32;
        this.mClassLoader = classLoader == null ? ClassLoader.getSystemClassLoader() : classLoader;
    }

    @UnsupportedAppUsage
    Resources() {
        this(null);
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.setToDefaults();
        Configuration config = new Configuration();
        config.setToDefaults();
        this.mResourcesImpl = new MiuiResourcesImpl(AssetManager.getSystem(), metrics, config, new DisplayAdjustments());
    }

    @UnsupportedAppUsage
    public void setImpl(ResourcesImpl impl) {
        if (impl != this.mResourcesImpl) {
            this.mResourcesImpl = impl;
            synchronized (this.mThemeRefs) {
                int count = this.mThemeRefs.size();
                for (int i = 0; i < count; i++) {
                    WeakReference<Theme> weakThemeRef = (WeakReference) this.mThemeRefs.get(i);
                    Theme theme = weakThemeRef != null ? (Theme) weakThemeRef.get() : null;
                    if (theme != null) {
                        theme.setImpl(this.mResourcesImpl.newThemeImpl(theme.getKey()));
                    }
                }
            }
        }
    }

    @UnsupportedAppUsage
    public ResourcesImpl getImpl() {
        return this.mResourcesImpl;
    }

    public ClassLoader getClassLoader() {
        return this.mClassLoader;
    }

    @UnsupportedAppUsage
    public final DrawableInflater getDrawableInflater() {
        if (this.mDrawableInflater == null) {
            this.mDrawableInflater = new DrawableInflater(this, this.mClassLoader);
        }
        return this.mDrawableInflater;
    }

    public ConfigurationBoundResourceCache<Animator> getAnimatorCache() {
        return this.mResourcesImpl.getAnimatorCache();
    }

    public ConfigurationBoundResourceCache<StateListAnimator> getStateListAnimatorCache() {
        return this.mResourcesImpl.getStateListAnimatorCache();
    }

    public CharSequence getText(int id) throws NotFoundException {
        CharSequence res = this.mResourcesImpl.getAssets().getResourceText(id);
        if (res != null) {
            return res;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("String resource ID #0x");
        stringBuilder.append(Integer.toHexString(id));
        throw new NotFoundException(stringBuilder.toString());
    }

    public Typeface getFont(int id) throws NotFoundException {
        TypedValue value = obtainTempTypedValue();
        try {
            ResourcesImpl impl = this.mResourcesImpl;
            impl.getValue(id, value, true);
            Typeface typeface = impl.loadFont(this, value, id);
            if (typeface != null) {
                return typeface;
            }
            releaseTempTypedValue(value);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Font resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            throw new NotFoundException(stringBuilder.toString());
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public Typeface getFont(TypedValue value, int id) throws NotFoundException {
        return this.mResourcesImpl.loadFont(this, value, id);
    }

    public void preloadFonts(int id) {
        TypedArray array = obtainTypedArray(id);
        try {
            int size = array.length();
            for (int i = 0; i < size; i++) {
                array.getFont(i);
            }
        } finally {
            array.recycle();
        }
    }

    public CharSequence getQuantityText(int id, int quantity) throws NotFoundException {
        return this.mResourcesImpl.getQuantityText(id, quantity);
    }

    public String getString(int id) throws NotFoundException {
        return getText(id).toString();
    }

    public String getString(int id, Object... formatArgs) throws NotFoundException {
        return String.format(this.mResourcesImpl.getConfiguration().getLocales().get(0), getString(id), formatArgs);
    }

    public String getQuantityString(int id, int quantity, Object... formatArgs) throws NotFoundException {
        return String.format(this.mResourcesImpl.getConfiguration().getLocales().get(0), getQuantityText(id, quantity).toString(), formatArgs);
    }

    public String getQuantityString(int id, int quantity) throws NotFoundException {
        return getQuantityText(id, quantity).toString();
    }

    public CharSequence getText(int id, CharSequence def) {
        CharSequence res = id != 0 ? this.mResourcesImpl.getAssets().getResourceText(id) : null;
        return res != null ? res : def;
    }

    public CharSequence[] getTextArray(int id) throws NotFoundException {
        CharSequence[] res = this.mResourcesImpl.getAssets().getResourceTextArray(id);
        if (res != null) {
            return res;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Text array resource ID #0x");
        stringBuilder.append(Integer.toHexString(id));
        throw new NotFoundException(stringBuilder.toString());
    }

    public String[] getStringArray(int id) throws NotFoundException {
        String[] res = this.mResourcesImpl.getAssets().getResourceStringArray(id);
        if (res != null) {
            return res;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("String array resource ID #0x");
        stringBuilder.append(Integer.toHexString(id));
        throw new NotFoundException(stringBuilder.toString());
    }

    public int[] getIntArray(int id) throws NotFoundException {
        int[] res = this.mResourcesImpl.getAssets().getResourceIntArray(id);
        if (res != null) {
            return res;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Int array resource ID #0x");
        stringBuilder.append(Integer.toHexString(id));
        throw new NotFoundException(stringBuilder.toString());
    }

    public TypedArray obtainTypedArray(int id) throws NotFoundException {
        ResourcesImpl impl = this.mResourcesImpl;
        int len = impl.getAssets().getResourceArraySize(id);
        if (len >= 0) {
            TypedArray array = TypedArray.obtain(this, len);
            array.mLength = impl.getAssets().getResourceArray(id, array.mData);
            array.mIndices[0] = 0;
            return loadOverlayTypedArray(array);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Array resource ID #0x");
        stringBuilder.append(Integer.toHexString(id));
        throw new NotFoundException(stringBuilder.toString());
    }

    public float getDimension(int id) throws NotFoundException {
        TypedValue value = obtainTempTypedValue();
        try {
            ResourcesImpl impl = this.mResourcesImpl;
            impl.getValue(id, value, true);
            float f = value.type;
            if (f == 5) {
                f = TypedValue.complexToDimension(value.data, impl.getDisplayMetrics());
                return f;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            stringBuilder.append(" type #0x");
            stringBuilder.append(Integer.toHexString(value.type));
            stringBuilder.append(" is not valid");
            throw new NotFoundException(stringBuilder.toString());
        } finally {
            releaseTempTypedValue(value);
        }
    }

    public int getDimensionPixelOffset(int id) throws NotFoundException {
        TypedValue value = obtainTempTypedValue();
        try {
            ResourcesImpl impl = this.mResourcesImpl;
            impl.getValue(id, value, true);
            int i = value.type;
            if (i == 5) {
                i = TypedValue.complexToDimensionPixelOffset(value.data, impl.getDisplayMetrics());
                return i;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            stringBuilder.append(" type #0x");
            stringBuilder.append(Integer.toHexString(value.type));
            stringBuilder.append(" is not valid");
            throw new NotFoundException(stringBuilder.toString());
        } finally {
            releaseTempTypedValue(value);
        }
    }

    public int getDimensionPixelSize(int id) throws NotFoundException {
        TypedValue value = obtainTempTypedValue();
        try {
            ResourcesImpl impl = this.mResourcesImpl;
            impl.getValue(id, value, true);
            int i = value.type;
            if (i == 5) {
                i = TypedValue.complexToDimensionPixelSize(value.data, impl.getDisplayMetrics());
                return i;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            stringBuilder.append(" type #0x");
            stringBuilder.append(Integer.toHexString(value.type));
            stringBuilder.append(" is not valid");
            throw new NotFoundException(stringBuilder.toString());
        } finally {
            releaseTempTypedValue(value);
        }
    }

    public float getFraction(int id, int base, int pbase) {
        TypedValue value = obtainTempTypedValue();
        try {
            this.mResourcesImpl.getValue(id, value, true);
            if (value.type == 6) {
                float complexToFraction = TypedValue.complexToFraction(value.data, (float) base, (float) pbase);
                return complexToFraction;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            stringBuilder.append(" type #0x");
            stringBuilder.append(Integer.toHexString(value.type));
            stringBuilder.append(" is not valid");
            throw new NotFoundException(stringBuilder.toString());
        } finally {
            releaseTempTypedValue(value);
        }
    }

    @Deprecated
    public Drawable getDrawable(int id) throws NotFoundException {
        Drawable d = getDrawable(id, null);
        if (d != null && d.canApplyTheme()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Drawable ");
            stringBuilder.append(getResourceName(id));
            stringBuilder.append(" has unresolved theme attributes! Consider using Resources.getDrawable(int, Theme) or Context.getDrawable(int).");
            Log.w(TAG, stringBuilder.toString(), new RuntimeException());
        }
        return d;
    }

    public Drawable getDrawable(int id, Theme theme) throws NotFoundException {
        return getDrawableForDensity(id, 0, theme);
    }

    @Deprecated
    public Drawable getDrawableForDensity(int id, int density) throws NotFoundException {
        return getDrawableForDensity(id, density, null);
    }

    public Drawable getDrawableForDensity(int id, int density, Theme theme) {
        TypedValue value = obtainTempTypedValue();
        try {
            ResourcesImpl impl = this.mResourcesImpl;
            impl.getValueForDensity(id, density, value, true);
            Drawable loadDrawable = impl.loadDrawable(this, value, id, density, theme);
            return loadDrawable;
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public Drawable loadDrawable(TypedValue value, int id, int density, Theme theme) throws NotFoundException {
        return this.mResourcesImpl.loadDrawable(this, value, id, density, theme);
    }

    @Deprecated
    public Movie getMovie(int id) throws NotFoundException {
        InputStream is = openRawResource(id);
        Movie movie = Movie.decodeStream(is);
        try {
            is.close();
        } catch (IOException e) {
        }
        return movie;
    }

    @Deprecated
    public int getColor(int id) throws NotFoundException {
        return getColor(id, null);
    }

    public int getColor(int id, Theme theme) throws NotFoundException {
        TypedValue value = obtainTempTypedValue();
        try {
            ResourcesImpl impl = this.mResourcesImpl;
            impl.getValue(id, value, true);
            int i = value.type;
            if (i >= 16) {
                i = value.type;
                if (i <= 31) {
                    i = value.data;
                    return i;
                }
            }
            i = value.type;
            if (i == 3) {
                int defaultColor = impl.loadColorStateList(this, value, id, theme).getDefaultColor();
                releaseTempTypedValue(value);
                return defaultColor;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            stringBuilder.append(" type #0x");
            stringBuilder.append(Integer.toHexString(value.type));
            stringBuilder.append(" is not valid");
            throw new NotFoundException(stringBuilder.toString());
        } finally {
            releaseTempTypedValue(value);
        }
    }

    @Deprecated
    public ColorStateList getColorStateList(int id) throws NotFoundException {
        ColorStateList csl = getColorStateList(id, null);
        if (csl != null && csl.canApplyTheme()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ColorStateList ");
            stringBuilder.append(getResourceName(id));
            stringBuilder.append(" has unresolved theme attributes! Consider using Resources.getColorStateList(int, Theme) or Context.getColorStateList(int).");
            Log.w(TAG, stringBuilder.toString(), new RuntimeException());
        }
        return csl;
    }

    public ColorStateList getColorStateList(int id, Theme theme) throws NotFoundException {
        TypedValue value = obtainTempTypedValue();
        try {
            ResourcesImpl impl = this.mResourcesImpl;
            impl.getValue(id, value, true);
            ColorStateList loadColorStateList = impl.loadColorStateList(this, value, id, theme);
            return loadColorStateList;
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ColorStateList loadColorStateList(TypedValue value, int id, Theme theme) throws NotFoundException {
        return this.mResourcesImpl.loadColorStateList(this, value, id, theme);
    }

    public ComplexColor loadComplexColor(TypedValue value, int id, Theme theme) {
        return this.mResourcesImpl.loadComplexColor(this, value, id, theme);
    }

    public boolean getBoolean(int id) throws NotFoundException {
        TypedValue value = obtainTempTypedValue();
        try {
            boolean z = true;
            this.mResourcesImpl.getValue(id, value, true);
            if (value.type < 16 || value.type > 31) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Resource ID #0x");
                stringBuilder.append(Integer.toHexString(id));
                stringBuilder.append(" type #0x");
                stringBuilder.append(Integer.toHexString(value.type));
                stringBuilder.append(" is not valid");
                throw new NotFoundException(stringBuilder.toString());
            }
            if (value.data == 0) {
                z = false;
            }
            releaseTempTypedValue(value);
            return z;
        } catch (Throwable th) {
            releaseTempTypedValue(value);
        }
    }

    public int getInteger(int id) throws NotFoundException {
        TypedValue value = obtainTempTypedValue();
        try {
            this.mResourcesImpl.getValue(id, value, true);
            if (value.type < 16 || value.type > 31) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Resource ID #0x");
                stringBuilder.append(Integer.toHexString(id));
                stringBuilder.append(" type #0x");
                stringBuilder.append(Integer.toHexString(value.type));
                stringBuilder.append(" is not valid");
                throw new NotFoundException(stringBuilder.toString());
            }
            int i = value.data;
            return i;
        } finally {
            releaseTempTypedValue(value);
        }
    }

    public float getFloat(int id) {
        TypedValue value = obtainTempTypedValue();
        try {
            this.mResourcesImpl.getValue(id, value, true);
            if (value.type == 4) {
                float f = value.getFloat();
                return f;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            stringBuilder.append(" type #0x");
            stringBuilder.append(Integer.toHexString(value.type));
            stringBuilder.append(" is not valid");
            throw new NotFoundException(stringBuilder.toString());
        } finally {
            releaseTempTypedValue(value);
        }
    }

    public XmlResourceParser getLayout(int id) throws NotFoundException {
        return loadXmlResourceParser(id, TtmlUtils.TAG_LAYOUT);
    }

    public XmlResourceParser getAnimation(int id) throws NotFoundException {
        return loadXmlResourceParser(id, "anim");
    }

    public XmlResourceParser getXml(int id) throws NotFoundException {
        return loadXmlResourceParser(id, "xml");
    }

    public InputStream openRawResource(int id) throws NotFoundException {
        TypedValue value = obtainTempTypedValue();
        try {
            InputStream openRawResource = openRawResource(id, value);
            return openRawResource;
        } finally {
            releaseTempTypedValue(value);
        }
    }

    private TypedValue obtainTempTypedValue() {
        TypedValue tmpValue = null;
        synchronized (this.mTmpValueLock) {
            if (this.mTmpValue != null) {
                tmpValue = this.mTmpValue;
                this.mTmpValue = null;
            }
        }
        if (tmpValue == null) {
            return new TypedValue();
        }
        return tmpValue;
    }

    private void releaseTempTypedValue(TypedValue value) {
        synchronized (this.mTmpValueLock) {
            if (this.mTmpValue == null) {
                this.mTmpValue = value;
            }
        }
    }

    public InputStream openRawResource(int id, TypedValue value) throws NotFoundException {
        return this.mResourcesImpl.openRawResource(id, value);
    }

    public AssetFileDescriptor openRawResourceFd(int id) throws NotFoundException {
        TypedValue value = obtainTempTypedValue();
        try {
            AssetFileDescriptor openRawResourceFd = this.mResourcesImpl.openRawResourceFd(id, value);
            return openRawResourceFd;
        } finally {
            releaseTempTypedValue(value);
        }
    }

    public void getValue(int id, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        this.mResourcesImpl.getValue(id, outValue, resolveRefs);
    }

    public void getValueForDensity(int id, int density, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        this.mResourcesImpl.getValueForDensity(id, density, outValue, resolveRefs);
    }

    public void getValue(String name, TypedValue outValue, boolean resolveRefs) throws NotFoundException {
        this.mResourcesImpl.getValue(name, outValue, resolveRefs);
    }

    public static int getAttributeSetSourceResId(AttributeSet set) {
        return ResourcesImpl.getAttributeSetSourceResId(set);
    }

    public final Theme newTheme() {
        Theme theme = new Theme();
        theme.setImpl(this.mResourcesImpl.newThemeImpl());
        synchronized (this.mThemeRefs) {
            this.mThemeRefs.add(new WeakReference(theme));
            if (this.mThemeRefs.size() > this.mThemeRefsNextFlushSize) {
                this.mThemeRefs.removeIf(-$$Lambda$Resources$4msWUw7LKsgLexLZjIfWa4oguq4.INSTANCE);
                this.mThemeRefsNextFlushSize = Math.max(32, this.mThemeRefs.size() * 2);
            }
        }
        return theme;
    }

    static /* synthetic */ boolean lambda$newTheme$0(WeakReference ref) {
        return ref.get() == null;
    }

    public TypedArray obtainAttributes(AttributeSet set, int[] attrs) {
        TypedArray array = TypedArray.obtain(this, attrs.length);
        Parser parser = (Parser) set;
        this.mResourcesImpl.getAssets().retrieveAttributes(parser, attrs, array.mData, array.mIndices);
        array.mXml = parser;
        return loadOverlayTypedArray(array);
    }

    @Deprecated
    public void updateConfiguration(Configuration config, DisplayMetrics metrics) {
        updateConfiguration(config, metrics, null);
    }

    public void updateConfiguration(Configuration config, DisplayMetrics metrics, CompatibilityInfo compat) {
        this.mResourcesImpl.updateConfiguration(config, metrics, compat);
    }

    @UnsupportedAppUsage
    public static void updateSystemConfiguration(Configuration config, DisplayMetrics metrics, CompatibilityInfo compat) {
        Resources resources = mSystem;
        if (resources != null) {
            resources.updateConfiguration(config, metrics, compat);
        }
    }

    public DisplayMetrics getDisplayMetrics() {
        return this.mResourcesImpl.getDisplayMetrics();
    }

    @UnsupportedAppUsage
    public DisplayAdjustments getDisplayAdjustments() {
        return this.mResourcesImpl.getDisplayAdjustments();
    }

    public Configuration getConfiguration() {
        return this.mResourcesImpl.getConfiguration();
    }

    public Configuration[] getSizeConfigurations() {
        return this.mResourcesImpl.getSizeConfigurations();
    }

    @UnsupportedAppUsage
    public CompatibilityInfo getCompatibilityInfo() {
        return this.mResourcesImpl.getCompatibilityInfo();
    }

    @VisibleForTesting
    @UnsupportedAppUsage
    public void setCompatibilityInfo(CompatibilityInfo ci) {
        if (ci != null) {
            this.mResourcesImpl.updateConfiguration(null, null, ci);
        }
    }

    public int getIdentifier(String name, String defType, String defPackage) {
        return this.mResourcesImpl.getIdentifier(name, defType, defPackage);
    }

    public static boolean resourceHasPackage(int resid) {
        return (resid >>> 24) != 0;
    }

    public String getResourceName(int resid) throws NotFoundException {
        return this.mResourcesImpl.getResourceName(resid);
    }

    public String getResourcePackageName(int resid) throws NotFoundException {
        return this.mResourcesImpl.getResourcePackageName(resid);
    }

    public String getResourceTypeName(int resid) throws NotFoundException {
        return this.mResourcesImpl.getResourceTypeName(resid);
    }

    public String getResourceEntryName(int resid) throws NotFoundException {
        return this.mResourcesImpl.getResourceEntryName(resid);
    }

    public String getLastResourceResolution() throws NotFoundException {
        return this.mResourcesImpl.getLastResourceResolution();
    }

    public void parseBundleExtras(XmlResourceParser parser, Bundle outBundle) throws XmlPullParserException, IOException {
        int outerDepth = parser.getDepth();
        while (true) {
            int next = parser.next();
            int type = next;
            if (next == 1) {
                return;
            }
            if (type == 3 && parser.getDepth() <= outerDepth) {
                return;
            }
            if (type != 3) {
                if (type != 4) {
                    String str = "extra";
                    if (parser.getName().equals(str)) {
                        parseBundleExtra(str, parser, outBundle);
                        XmlUtils.skipCurrentTag(parser);
                    } else {
                        XmlUtils.skipCurrentTag(parser);
                    }
                }
            }
        }
    }

    public void parseBundleExtra(String tagName, AttributeSet attrs, Bundle outBundle) throws XmlPullParserException {
        TypedArray sa = obtainAttributes(attrs, R.styleable.Extra);
        boolean z = false;
        String name = sa.getString(0);
        String str = "<";
        StringBuilder stringBuilder;
        if (name != null) {
            TypedValue v = sa.peekValue(1);
            if (v != null) {
                if (v.type == 3) {
                    outBundle.putCharSequence(name, v.coerceToString());
                } else if (v.type == 18) {
                    if (v.data != 0) {
                        z = true;
                    }
                    outBundle.putBoolean(name, z);
                } else if (v.type >= 16 && v.type <= 31) {
                    outBundle.putInt(name, v.data);
                } else if (v.type == 4) {
                    outBundle.putFloat(name, v.getFloat());
                } else {
                    sa.recycle();
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(tagName);
                    stringBuilder.append("> only supports string, integer, float, color, and boolean at ");
                    stringBuilder.append(attrs.getPositionDescription());
                    throw new XmlPullParserException(stringBuilder.toString());
                }
                sa.recycle();
                return;
            }
            sa.recycle();
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(tagName);
            stringBuilder.append("> requires an android:value or android:resource attribute at ");
            stringBuilder.append(attrs.getPositionDescription());
            throw new XmlPullParserException(stringBuilder.toString());
        }
        sa.recycle();
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(tagName);
        stringBuilder.append("> requires an android:name attribute at ");
        stringBuilder.append(attrs.getPositionDescription());
        throw new XmlPullParserException(stringBuilder.toString());
    }

    public final AssetManager getAssets() {
        return this.mResourcesImpl.getAssets();
    }

    public final void flushLayoutCache() {
        this.mResourcesImpl.flushLayoutCache();
    }

    public final void startPreloading() {
        this.mResourcesImpl.startPreloading();
    }

    public final void finishPreloading() {
        this.mResourcesImpl.finishPreloading();
    }

    @UnsupportedAppUsage
    public LongSparseArray<ConstantState> getPreloadedDrawables() {
        return this.mResourcesImpl.getPreloadedDrawables();
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public XmlResourceParser loadXmlResourceParser(int id, String type) throws NotFoundException {
        TypedValue value = obtainTempTypedValue();
        try {
            ResourcesImpl impl = this.mResourcesImpl;
            impl.getValue(id, value, true);
            XmlResourceParser xmlResourceParser = value.type;
            if (xmlResourceParser == 3) {
                xmlResourceParser = impl.loadXmlResourceParser(value.string.toString(), id, value.assetCookie, type);
                return xmlResourceParser;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Resource ID #0x");
            stringBuilder.append(Integer.toHexString(id));
            stringBuilder.append(" type #0x");
            stringBuilder.append(Integer.toHexString(value.type));
            stringBuilder.append(" is not valid");
            throw new NotFoundException(stringBuilder.toString());
        } finally {
            releaseTempTypedValue(value);
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public XmlResourceParser loadXmlResourceParser(String file, int id, int assetCookie, String type) throws NotFoundException {
        return this.mResourcesImpl.loadXmlResourceParser(file, id, assetCookie, type);
    }

    @VisibleForTesting
    public int calcConfigChanges(Configuration config) {
        return this.mResourcesImpl.calcConfigChanges(config);
    }

    public static TypedArray obtainAttributes(Resources res, Theme theme, AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    /* Access modifiers changed, original: 0000 */
    public Drawable loadOverlayDrawable(TypedValue value, int id) {
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public void loadOverlayValue(TypedValue outValue, int id) {
    }

    /* Access modifiers changed, original: 0000 */
    public TypedArray loadOverlayTypedArray(TypedArray array) {
        return array;
    }

    static void clearPreloadedCache() {
        ResourcesImpl.clearPreloadedCache();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isPreloading() {
        return this.mResourcesImpl.isPreloading();
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isPreloadOverlayed(int id) {
        return false;
    }
}
