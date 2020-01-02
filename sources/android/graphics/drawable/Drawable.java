package android.graphics.drawable;

import android.annotation.UnsupportedAppUsage;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.DecodeException;
import android.graphics.ImageDecoder.ImageInfo;
import android.graphics.ImageDecoder.Source;
import android.graphics.Insets;
import android.graphics.NinePatch;
import android.graphics.Outline;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Xfermode;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.util.TypedValue;
import com.android.internal.R;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class Drawable {
    static final BlendMode DEFAULT_BLEND_MODE = BlendMode.SRC_IN;
    static final Mode DEFAULT_TINT_MODE = Mode.SRC_IN;
    private static final Rect ZERO_BOUNDS_RECT = new Rect();
    private Rect mBounds = ZERO_BOUNDS_RECT;
    @UnsupportedAppUsage
    private WeakReference<Callback> mCallback = null;
    private int mChangingConfigurations = 0;
    private int mLayoutDirection;
    private int mLevel = 0;
    private boolean mSetBlendModeInvoked = false;
    private boolean mSetTintModeInvoked = false;
    @UnsupportedAppUsage
    protected int mSrcDensityOverride = 0;
    private int[] mStateSet = StateSet.WILD_CARD;
    private boolean mVisible = true;

    public static abstract class ConstantState {
        public abstract int getChangingConfigurations();

        public abstract Drawable newDrawable();

        public Drawable newDrawable(Resources res) {
            return newDrawable();
        }

        public Drawable newDrawable(Resources res, Theme theme) {
            return newDrawable(res);
        }

        public boolean canApplyTheme() {
            return false;
        }
    }

    public interface Callback {
        void invalidateDrawable(Drawable drawable);

        void scheduleDrawable(Drawable drawable, Runnable runnable, long j);

        void unscheduleDrawable(Drawable drawable, Runnable runnable);
    }

    public abstract void draw(Canvas canvas);

    @Deprecated
    public abstract int getOpacity();

    public abstract void setAlpha(int i);

    public abstract void setColorFilter(ColorFilter colorFilter);

    public void setBounds(int left, int top, int right, int bottom) {
        Rect oldBounds = this.mBounds;
        if (oldBounds == ZERO_BOUNDS_RECT) {
            Rect rect = new Rect();
            this.mBounds = rect;
            oldBounds = rect;
        }
        if (oldBounds.left != left || oldBounds.top != top || oldBounds.right != right || oldBounds.bottom != bottom) {
            if (!oldBounds.isEmpty()) {
                invalidateSelf();
            }
            this.mBounds.set(left, top, right, bottom);
            onBoundsChange(this.mBounds);
        }
    }

    public void setBounds(Rect bounds) {
        setBounds(bounds.left, bounds.top, bounds.right, bounds.bottom);
    }

    public final void copyBounds(Rect bounds) {
        bounds.set(this.mBounds);
    }

    public final Rect copyBounds() {
        return new Rect(this.mBounds);
    }

    public final Rect getBounds() {
        if (this.mBounds == ZERO_BOUNDS_RECT) {
            this.mBounds = new Rect();
        }
        return this.mBounds;
    }

    public Rect getDirtyBounds() {
        return getBounds();
    }

    public void setChangingConfigurations(int configs) {
        this.mChangingConfigurations = configs;
    }

    public int getChangingConfigurations() {
        return this.mChangingConfigurations;
    }

    @Deprecated
    public void setDither(boolean dither) {
    }

    public void setFilterBitmap(boolean filter) {
    }

    public boolean isFilterBitmap() {
        return false;
    }

    public final void setCallback(Callback cb) {
        this.mCallback = cb != null ? new WeakReference(cb) : null;
    }

    public Callback getCallback() {
        WeakReference weakReference = this.mCallback;
        return weakReference != null ? (Callback) weakReference.get() : null;
    }

    public void invalidateSelf() {
        Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    public void scheduleSelf(Runnable what, long when) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    public void unscheduleSelf(Runnable what) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    public int getLayoutDirection() {
        return this.mLayoutDirection;
    }

    public final boolean setLayoutDirection(int layoutDirection) {
        if (this.mLayoutDirection == layoutDirection) {
            return false;
        }
        this.mLayoutDirection = layoutDirection;
        return onLayoutDirectionChanged(layoutDirection);
    }

    public boolean onLayoutDirectionChanged(int layoutDirection) {
        return false;
    }

    public int getAlpha() {
        return 255;
    }

    public void setXfermode(Xfermode mode) {
    }

    @Deprecated
    public void setColorFilter(int color, Mode mode) {
        if (getColorFilter() instanceof PorterDuffColorFilter) {
            PorterDuffColorFilter existing = (PorterDuffColorFilter) getColorFilter();
            if (existing.getColor() == color && existing.getMode() == mode) {
                return;
            }
        }
        setColorFilter(new PorterDuffColorFilter(color, mode));
    }

    public void setTint(int tintColor) {
        setTintList(ColorStateList.valueOf(tintColor));
    }

    public void setTintList(ColorStateList tint) {
    }

    public void setTintMode(Mode tintMode) {
        if (!this.mSetTintModeInvoked) {
            this.mSetTintModeInvoked = true;
            BlendMode mode = tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null;
            setTintBlendMode(mode != null ? mode : DEFAULT_BLEND_MODE);
            this.mSetTintModeInvoked = false;
        }
    }

    public void setTintBlendMode(BlendMode blendMode) {
        if (!this.mSetBlendModeInvoked) {
            this.mSetBlendModeInvoked = true;
            Mode mode = BlendMode.blendModeToPorterDuffMode(blendMode);
            setTintMode(mode != null ? mode : DEFAULT_TINT_MODE);
            this.mSetBlendModeInvoked = false;
        }
    }

    public ColorFilter getColorFilter() {
        return null;
    }

    public void clearColorFilter() {
        setColorFilter(null);
    }

    public void setHotspot(float x, float y) {
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
    }

    public void getHotspotBounds(Rect outRect) {
        outRect.set(getBounds());
    }

    public boolean isProjected() {
        return false;
    }

    public boolean isStateful() {
        return false;
    }

    public boolean hasFocusStateSpecified() {
        return false;
    }

    public boolean setState(int[] stateSet) {
        if (Arrays.equals(this.mStateSet, stateSet)) {
            return false;
        }
        this.mStateSet = stateSet;
        return onStateChange(stateSet);
    }

    public int[] getState() {
        return this.mStateSet;
    }

    public void jumpToCurrentState() {
    }

    public Drawable getCurrent() {
        return this;
    }

    public final boolean setLevel(int level) {
        if (this.mLevel == level) {
            return false;
        }
        this.mLevel = level;
        return onLevelChange(level);
    }

    public final int getLevel() {
        return this.mLevel;
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean changed = this.mVisible != visible;
        if (changed) {
            this.mVisible = visible;
            invalidateSelf();
        }
        return changed;
    }

    public final boolean isVisible() {
        return this.mVisible;
    }

    public void setAutoMirrored(boolean mirrored) {
    }

    public boolean isAutoMirrored() {
        return false;
    }

    public void applyTheme(Theme t) {
    }

    public boolean canApplyTheme() {
        return false;
    }

    public static int resolveOpacity(int op1, int op2) {
        if (op1 == op2) {
            return op1;
        }
        if (op1 == 0 || op2 == 0) {
            return 0;
        }
        if (op1 == -3 || op2 == -3) {
            return -3;
        }
        if (op1 == -2 || op2 == -2) {
            return -2;
        }
        return -1;
    }

    public Region getTransparentRegion() {
        return null;
    }

    /* Access modifiers changed, original: protected */
    public boolean onStateChange(int[] state) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean onLevelChange(int level) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect bounds) {
    }

    public int getIntrinsicWidth() {
        return -1;
    }

    public int getIntrinsicHeight() {
        return -1;
    }

    public int getMinimumWidth() {
        int intrinsicWidth = getIntrinsicWidth();
        return intrinsicWidth > 0 ? intrinsicWidth : 0;
    }

    public int getMinimumHeight() {
        int intrinsicHeight = getIntrinsicHeight();
        return intrinsicHeight > 0 ? intrinsicHeight : 0;
    }

    public boolean getPadding(Rect padding) {
        padding.set(0, 0, 0, 0);
        return false;
    }

    public Insets getOpticalInsets() {
        return Insets.NONE;
    }

    public void getOutline(Outline outline) {
        outline.setRect(getBounds());
        outline.setAlpha(0.0f);
    }

    public Drawable mutate() {
        return this;
    }

    public void clearMutated() {
    }

    public static Drawable createFromStream(InputStream is, String srcName) {
        Trace.traceBegin(8192, srcName != null ? srcName : "Unknown drawable");
        try {
            Drawable createFromResourceStream = createFromResourceStream(null, null, is, srcName);
            return createFromResourceStream;
        } finally {
            Trace.traceEnd(8192);
        }
    }

    public static Drawable createFromResourceStream(Resources res, TypedValue value, InputStream is, String srcName) {
        Trace.traceBegin(8192, srcName != null ? srcName : "Unknown drawable");
        try {
            Drawable createFromResourceStream = createFromResourceStream(res, value, is, srcName, null);
            return createFromResourceStream;
        } finally {
            Trace.traceEnd(8192);
        }
    }

    public static Drawable createFromResourceStream(Resources res, TypedValue value, InputStream is, String srcName, Options opts) {
        if (is == null) {
            return null;
        }
        if (opts == null) {
            return getBitmapDrawable(res, value, is);
        }
        Rect pad = new Rect();
        opts.inScreenDensity = resolveDensity(res, 0);
        Bitmap bm = BitmapFactory.decodeResourceStream(res, value, is, pad, opts);
        if (bm == null) {
            return null;
        }
        byte[] np = bm.getNinePatchChunk();
        if (np == null || !NinePatch.isNinePatchChunk(np)) {
            np = null;
            pad = null;
        }
        Rect opticalInsets = new Rect();
        bm.getOpticalInsets(opticalInsets);
        return drawableFromBitmap(res, bm, np, pad, opticalInsets, srcName);
    }

    private static Drawable getBitmapDrawable(Resources res, TypedValue value, InputStream is) {
        Source source;
        if (value != null) {
            int density = 0;
            try {
                if (value.density == 0) {
                    density = 160;
                } else if (value.density != 65535) {
                    density = value.density;
                }
                source = ImageDecoder.createSource(res, is, density);
            } catch (IOException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to decode stream: ");
                stringBuilder.append(e);
                Log.e("Drawable", stringBuilder.toString());
                return null;
            }
        }
        source = ImageDecoder.createSource(res, is);
        return ImageDecoder.decodeDrawable(source, -$$Lambda$Drawable$bbJz2VgQAwkXlE27mR8nPMYacEw.INSTANCE);
    }

    static /* synthetic */ void lambda$getBitmapDrawable$1(ImageDecoder decoder, ImageInfo info, Source src) {
        decoder.setAllocator(1);
        decoder.setOnPartialImageListener(-$$Lambda$Drawable$KZt6g0-IxKV2yrq1V3HrWrb1kXg.INSTANCE);
    }

    static /* synthetic */ boolean lambda$getBitmapDrawable$0(DecodeException e) {
        return e.getError() == 2;
    }

    public static Drawable createFromXml(Resources r, XmlPullParser parser) throws XmlPullParserException, IOException {
        return createFromXml(r, parser, null);
    }

    public static Drawable createFromXml(Resources r, XmlPullParser parser, Theme theme) throws XmlPullParserException, IOException {
        return createFromXmlForDensity(r, parser, 0, theme);
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0034  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0012  */
    public static android.graphics.drawable.Drawable createFromXmlForDensity(android.content.res.Resources r6, org.xmlpull.v1.XmlPullParser r7, int r8, android.content.res.Resources.Theme r9) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r0 = android.util.Xml.asAttributeSet(r7);
    L_0x0004:
        r1 = r7.next();
        r2 = r1;
        r3 = 2;
        if (r1 == r3) goto L_0x0010;
    L_0x000c:
        r1 = 1;
        if (r2 == r1) goto L_0x0010;
    L_0x000f:
        goto L_0x0004;
    L_0x0010:
        if (r2 != r3) goto L_0x0034;
    L_0x0012:
        r1 = createFromXmlInnerForDensity(r6, r7, r0, r8, r9);
        if (r1 == 0) goto L_0x0019;
    L_0x0018:
        return r1;
    L_0x0019:
        r3 = new java.lang.RuntimeException;
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r5 = "Unknown initial tag: ";
        r4.append(r5);
        r5 = r7.getName();
        r4.append(r5);
        r4 = r4.toString();
        r3.<init>(r4);
        throw r3;
    L_0x0034:
        r1 = new org.xmlpull.v1.XmlPullParserException;
        r3 = "No start tag found";
        r1.<init>(r3);
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.Drawable.createFromXmlForDensity(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, int, android.content.res.Resources$Theme):android.graphics.drawable.Drawable");
    }

    public static Drawable createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        return createFromXmlInner(r, parser, attrs, null);
    }

    public static Drawable createFromXmlInner(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        return createFromXmlInnerForDensity(r, parser, attrs, 0, theme);
    }

    static Drawable createFromXmlInnerForDensity(Resources r, XmlPullParser parser, AttributeSet attrs, int density, Theme theme) throws XmlPullParserException, IOException {
        return r.getDrawableInflater().inflateFromXmlForDensity(parser.getName(), parser, attrs, density, theme);
    }

    /* JADX WARNING: Missing block: B:17:?, code skipped:
            r3.close();
     */
    public static android.graphics.drawable.Drawable createFromPath(java.lang.String r7) {
        /*
        r0 = 0;
        if (r7 != 0) goto L_0x0004;
    L_0x0003:
        return r0;
    L_0x0004:
        r1 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        android.os.Trace.traceBegin(r1, r7);
        r3 = new java.io.FileInputStream;	 Catch:{ IOException -> 0x002a, all -> 0x0025 }
        r3.<init>(r7);	 Catch:{ IOException -> 0x002a, all -> 0x0025 }
        r4 = getBitmapDrawable(r0, r0, r3);	 Catch:{ all -> 0x0019 }
        r3.close();	 Catch:{ IOException -> 0x002a, all -> 0x0025 }
        android.os.Trace.traceEnd(r1);
        return r4;
    L_0x0019:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x001b }
    L_0x001b:
        r5 = move-exception;
        r3.close();	 Catch:{ all -> 0x0020 }
        goto L_0x0024;
    L_0x0020:
        r6 = move-exception;
        r4.addSuppressed(r6);	 Catch:{ IOException -> 0x002a, all -> 0x0025 }
    L_0x0024:
        throw r5;	 Catch:{ IOException -> 0x002a, all -> 0x0025 }
    L_0x0025:
        r0 = move-exception;
        android.os.Trace.traceEnd(r1);
        throw r0;
    L_0x002a:
        r3 = move-exception;
        android.os.Trace.traceEnd(r1);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.Drawable.createFromPath(java.lang.String):android.graphics.drawable.Drawable");
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs) throws XmlPullParserException, IOException {
        inflate(r, parser, attrs, null);
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        TypedArray a = obtainAttributes(r, theme, attrs, R.styleable.Drawable);
        this.mVisible = a.getBoolean(0, this.mVisible);
        a.recycle();
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void inflateWithAttributes(Resources r, XmlPullParser parser, TypedArray attrs, int visibleAttr) throws XmlPullParserException, IOException {
        this.mVisible = attrs.getBoolean(visibleAttr, this.mVisible);
    }

    /* Access modifiers changed, original: final */
    public final void setSrcDensityOverride(int density) {
        this.mSrcDensityOverride = density;
    }

    public ConstantState getConstantState() {
        return null;
    }

    private static Drawable drawableFromBitmap(Resources res, Bitmap bm, byte[] np, Rect pad, Rect layoutBounds, String srcName) {
        if (np != null) {
            return new NinePatchDrawable(res, bm, np, pad, layoutBounds, srcName);
        }
        return new BitmapDrawable(res, bm);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public PorterDuffColorFilter updateTintFilter(PorterDuffColorFilter tintFilter, ColorStateList tint, Mode tintMode) {
        if (tint == null || tintMode == null) {
            return null;
        }
        int color = tint.getColorForState(getState(), 0);
        if (tintFilter != null && tintFilter.getColor() == color && tintFilter.getMode() == tintMode) {
            return tintFilter;
        }
        return new PorterDuffColorFilter(color, tintMode);
    }

    /* Access modifiers changed, original: 0000 */
    public BlendModeColorFilter updateBlendModeFilter(BlendModeColorFilter blendFilter, ColorStateList tint, BlendMode blendMode) {
        if (tint == null || blendMode == null) {
            return null;
        }
        int color = tint.getColorForState(getState(), 0);
        if (blendFilter != null && blendFilter.getColor() == color && blendFilter.getMode() == blendMode) {
            return blendFilter;
        }
        return new BlendModeColorFilter(color, blendMode);
    }

    protected static TypedArray obtainAttributes(Resources res, Theme theme, AttributeSet set, int[] attrs) {
        if (theme == null) {
            return res.obtainAttributes(set, attrs);
        }
        return theme.obtainStyledAttributes(set, attrs, 0, 0);
    }

    static float scaleFromDensity(float pixels, int sourceDensity, int targetDensity) {
        return (((float) targetDensity) * pixels) / ((float) sourceDensity);
    }

    static int scaleFromDensity(int pixels, int sourceDensity, int targetDensity, boolean isSize) {
        if (pixels == 0 || sourceDensity == targetDensity) {
            return pixels;
        }
        float result = ((float) (pixels * targetDensity)) / ((float) sourceDensity);
        if (!isSize) {
            return (int) result;
        }
        int rounded = Math.round(result);
        if (rounded != 0) {
            return rounded;
        }
        if (pixels > 0) {
            return 1;
        }
        return -1;
    }

    static int resolveDensity(Resources r, int parentDensity) {
        int densityDpi = r == null ? parentDensity : r.getDisplayMetrics().densityDpi;
        return densityDpi == 0 ? 160 : densityDpi;
    }

    static void rethrowAsRuntimeException(Exception cause) throws RuntimeException {
        RuntimeException e = new RuntimeException(cause);
        e.setStackTrace(new StackTraceElement[0]);
        throw e;
    }

    @UnsupportedAppUsage
    public static Mode parseTintMode(int value, Mode defaultMode) {
        if (value == 3) {
            return Mode.SRC_OVER;
        }
        if (value == 5) {
            return Mode.SRC_IN;
        }
        if (value == 9) {
            return Mode.SRC_ATOP;
        }
        switch (value) {
            case 14:
                return Mode.MULTIPLY;
            case 15:
                return Mode.SCREEN;
            case 16:
                return Mode.ADD;
            default:
                return defaultMode;
        }
    }

    @UnsupportedAppUsage
    public static BlendMode parseBlendMode(int value, BlendMode defaultMode) {
        if (value == 3) {
            return BlendMode.SRC_OVER;
        }
        if (value == 5) {
            return BlendMode.SRC_IN;
        }
        if (value == 9) {
            return BlendMode.SRC_ATOP;
        }
        switch (value) {
            case 14:
                return BlendMode.MODULATE;
            case 15:
                return BlendMode.SCREEN;
            case 16:
                return BlendMode.PLUS;
            default:
                return defaultMode;
        }
    }
}
