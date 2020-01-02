package android.graphics.drawable;

import android.annotation.UnsupportedAppUsage;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ImageDecoder;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import com.android.internal.R;
import java.io.IOException;
import java.io.InputStream;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class BitmapDrawable extends Drawable {
    private static final int DEFAULT_PAINT_FLAGS = 6;
    private static final int TILE_MODE_CLAMP = 0;
    private static final int TILE_MODE_DISABLED = -1;
    private static final int TILE_MODE_MIRROR = 2;
    private static final int TILE_MODE_REPEAT = 1;
    private static final int TILE_MODE_UNDEFINED = -2;
    private int mBitmapHeight;
    @UnsupportedAppUsage
    private BitmapState mBitmapState;
    private int mBitmapWidth;
    private BlendModeColorFilter mBlendModeFilter;
    private final Rect mDstRect;
    private boolean mDstRectAndInsetsDirty;
    private Matrix mMirrorMatrix;
    private boolean mMutated;
    private Insets mOpticalInsets;
    @UnsupportedAppUsage
    private int mTargetDensity;

    static final class BitmapState extends ConstantState {
        boolean mAutoMirrored = false;
        float mBaseAlpha = 1.0f;
        Bitmap mBitmap = null;
        BlendMode mBlendMode = Drawable.DEFAULT_BLEND_MODE;
        int mChangingConfigurations;
        int mGravity = 119;
        final Paint mPaint;
        boolean mRebuildShader;
        int mSrcDensityOverride = 0;
        int mTargetDensity = 160;
        int[] mThemeAttrs = null;
        TileMode mTileModeX = null;
        TileMode mTileModeY = null;
        ColorStateList mTint = null;

        BitmapState(Bitmap bitmap) {
            this.mBitmap = bitmap;
            this.mPaint = new Paint(6);
        }

        BitmapState(BitmapState bitmapState) {
            this.mBitmap = bitmapState.mBitmap;
            this.mTint = bitmapState.mTint;
            this.mBlendMode = bitmapState.mBlendMode;
            this.mThemeAttrs = bitmapState.mThemeAttrs;
            this.mChangingConfigurations = bitmapState.mChangingConfigurations;
            this.mGravity = bitmapState.mGravity;
            this.mTileModeX = bitmapState.mTileModeX;
            this.mTileModeY = bitmapState.mTileModeY;
            this.mSrcDensityOverride = bitmapState.mSrcDensityOverride;
            this.mTargetDensity = bitmapState.mTargetDensity;
            this.mBaseAlpha = bitmapState.mBaseAlpha;
            this.mPaint = new Paint(bitmapState.mPaint);
            this.mRebuildShader = bitmapState.mRebuildShader;
            this.mAutoMirrored = bitmapState.mAutoMirrored;
        }

        public boolean canApplyTheme() {
            if (this.mThemeAttrs == null) {
                ColorStateList colorStateList = this.mTint;
                if (colorStateList == null || !colorStateList.canApplyTheme()) {
                    return false;
                }
            }
            return true;
        }

        public Drawable newDrawable() {
            return new BitmapDrawable(this, null);
        }

        public Drawable newDrawable(Resources res) {
            return new BitmapDrawable(this, res);
        }

        public int getChangingConfigurations() {
            int i = this.mChangingConfigurations;
            ColorStateList colorStateList = this.mTint;
            return i | (colorStateList != null ? colorStateList.getChangingConfigurations() : 0);
        }
    }

    @Deprecated
    public BitmapDrawable() {
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        init(new BitmapState((Bitmap) null), null);
    }

    @Deprecated
    public BitmapDrawable(Resources res) {
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        init(new BitmapState((Bitmap) null), res);
    }

    @Deprecated
    public BitmapDrawable(Bitmap bitmap) {
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        init(new BitmapState(bitmap), null);
    }

    public BitmapDrawable(Resources res, Bitmap bitmap) {
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        init(new BitmapState(bitmap), res);
    }

    @Deprecated
    public BitmapDrawable(String filepath) {
        this(null, filepath);
    }

    /* JADX WARNING: Missing block: B:18:?, code skipped:
            $closeResource(r4, r3);
     */
    public BitmapDrawable(android.content.res.Resources r8, java.lang.String r9) {
        /*
        r7 = this;
        r0 = "BitmapDrawable cannot decode ";
        r1 = "BitmapDrawable";
        r7.<init>();
        r2 = new android.graphics.Rect;
        r2.<init>();
        r7.mDstRect = r2;
        r2 = 160; // 0xa0 float:2.24E-43 double:7.9E-322;
        r7.mTargetDensity = r2;
        r2 = 1;
        r7.mDstRectAndInsetsDirty = r2;
        r2 = android.graphics.Insets.NONE;
        r7.mOpticalInsets = r2;
        r2 = 0;
        r3 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x0078, all -> 0x0056 }
        r3.<init>(r9);	 Catch:{ Exception -> 0x0078, all -> 0x0056 }
        r4 = 0;
        r5 = android.graphics.ImageDecoder.createSource(r8, r3);	 Catch:{ all -> 0x004f }
        r6 = android.graphics.drawable.-$$Lambda$BitmapDrawable$23eAuhdkgEf5MIRJC-rMNbn4Pyg.INSTANCE;	 Catch:{ all -> 0x004f }
        r5 = android.graphics.ImageDecoder.decodeBitmap(r5, r6);	 Catch:{ all -> 0x004f }
        r2 = r5;
        $closeResource(r4, r3);	 Catch:{ Exception -> 0x0078, all -> 0x0056 }
        r3 = new android.graphics.drawable.BitmapDrawable$BitmapState;
        r3.<init>(r2);
        r7.init(r3, r8);
        r3 = r7.mBitmapState;
        r3 = r3.mBitmap;
        if (r3 != 0) goto L_0x008d;
    L_0x003c:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
    L_0x0041:
        r3.append(r0);
        r3.append(r9);
        r0 = r3.toString();
        android.util.Log.w(r1, r0);
        goto L_0x008d;
    L_0x004f:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x0051 }
    L_0x0051:
        r5 = move-exception;
        $closeResource(r4, r3);	 Catch:{ Exception -> 0x0078, all -> 0x0056 }
        throw r5;	 Catch:{ Exception -> 0x0078, all -> 0x0056 }
    L_0x0056:
        r3 = move-exception;
        r4 = new android.graphics.drawable.BitmapDrawable$BitmapState;
        r4.<init>(r2);
        r7.init(r4, r8);
        r4 = r7.mBitmapState;
        r4 = r4.mBitmap;
        if (r4 != 0) goto L_0x0077;
    L_0x0065:
        r4 = new java.lang.StringBuilder;
        r4.<init>();
        r4.append(r0);
        r4.append(r9);
        r0 = r4.toString();
        android.util.Log.w(r1, r0);
    L_0x0077:
        throw r3;
    L_0x0078:
        r3 = move-exception;
        r3 = new android.graphics.drawable.BitmapDrawable$BitmapState;
        r3.<init>(r2);
        r7.init(r3, r8);
        r3 = r7.mBitmapState;
        r3 = r3.mBitmap;
        if (r3 != 0) goto L_0x008d;
    L_0x0087:
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        goto L_0x0041;
    L_0x008d:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.BitmapDrawable.<init>(android.content.res.Resources, java.lang.String):void");
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

    @Deprecated
    public BitmapDrawable(InputStream is) {
        this(null, is);
    }

    public BitmapDrawable(Resources res, InputStream is) {
        String str = "BitmapDrawable cannot decode ";
        String str2 = "BitmapDrawable";
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        StringBuilder stringBuilder;
        try {
            init(new BitmapState(ImageDecoder.decodeBitmap(ImageDecoder.createSource(res, is), -$$Lambda$BitmapDrawable$T1BUUqQwU4Z6Ve8DJHFuQvYohkY.INSTANCE)), res);
            if (this.mBitmapState.mBitmap == null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(is);
                Log.w(str2, stringBuilder.toString());
            }
        } catch (Exception e) {
            init(new BitmapState(null), res);
            if (this.mBitmapState.mBitmap == null) {
                stringBuilder = new StringBuilder();
            }
        } catch (Throwable th) {
            init(new BitmapState(null), res);
            if (this.mBitmapState.mBitmap == null) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(is);
                Log.w(str2, stringBuilder2.toString());
            }
        }
    }

    public final Paint getPaint() {
        return this.mBitmapState.mPaint;
    }

    public final Bitmap getBitmap() {
        return this.mBitmapState.mBitmap;
    }

    private void computeBitmapSize() {
        Bitmap bitmap = this.mBitmapState.mBitmap;
        if (bitmap != null) {
            this.mBitmapWidth = bitmap.getScaledWidth(this.mTargetDensity);
            this.mBitmapHeight = bitmap.getScaledHeight(this.mTargetDensity);
            return;
        }
        this.mBitmapHeight = -1;
        this.mBitmapWidth = -1;
    }

    @UnsupportedAppUsage
    public void setBitmap(Bitmap bitmap) {
        if (this.mBitmapState.mBitmap != bitmap) {
            this.mBitmapState.mBitmap = bitmap;
            computeBitmapSize();
            invalidateSelf();
        }
    }

    public void setTargetDensity(Canvas canvas) {
        setTargetDensity(canvas.getDensity());
    }

    public void setTargetDensity(DisplayMetrics metrics) {
        setTargetDensity(metrics.densityDpi);
    }

    public void setTargetDensity(int density) {
        if (this.mTargetDensity != density) {
            this.mTargetDensity = density == 0 ? 160 : density;
            if (this.mBitmapState.mBitmap != null) {
                computeBitmapSize();
            }
            invalidateSelf();
        }
    }

    public int getGravity() {
        return this.mBitmapState.mGravity;
    }

    public void setGravity(int gravity) {
        if (this.mBitmapState.mGravity != gravity) {
            this.mBitmapState.mGravity = gravity;
            this.mDstRectAndInsetsDirty = true;
            invalidateSelf();
        }
    }

    public void setMipMap(boolean mipMap) {
        if (this.mBitmapState.mBitmap != null) {
            this.mBitmapState.mBitmap.setHasMipMap(mipMap);
            invalidateSelf();
        }
    }

    public boolean hasMipMap() {
        return this.mBitmapState.mBitmap != null && this.mBitmapState.mBitmap.hasMipMap();
    }

    public void setAntiAlias(boolean aa) {
        this.mBitmapState.mPaint.setAntiAlias(aa);
        invalidateSelf();
    }

    public boolean hasAntiAlias() {
        return this.mBitmapState.mPaint.isAntiAlias();
    }

    public void setFilterBitmap(boolean filter) {
        this.mBitmapState.mPaint.setFilterBitmap(filter);
        invalidateSelf();
    }

    public boolean isFilterBitmap() {
        return this.mBitmapState.mPaint.isFilterBitmap();
    }

    public void setDither(boolean dither) {
        this.mBitmapState.mPaint.setDither(dither);
        invalidateSelf();
    }

    public TileMode getTileModeX() {
        return this.mBitmapState.mTileModeX;
    }

    public TileMode getTileModeY() {
        return this.mBitmapState.mTileModeY;
    }

    public void setTileModeX(TileMode mode) {
        setTileModeXY(mode, this.mBitmapState.mTileModeY);
    }

    public final void setTileModeY(TileMode mode) {
        setTileModeXY(this.mBitmapState.mTileModeX, mode);
    }

    public void setTileModeXY(TileMode xmode, TileMode ymode) {
        BitmapState state = this.mBitmapState;
        if (state.mTileModeX != xmode || state.mTileModeY != ymode) {
            state.mTileModeX = xmode;
            state.mTileModeY = ymode;
            state.mRebuildShader = true;
            this.mDstRectAndInsetsDirty = true;
            invalidateSelf();
        }
    }

    public void setAutoMirrored(boolean mirrored) {
        if (this.mBitmapState.mAutoMirrored != mirrored) {
            this.mBitmapState.mAutoMirrored = mirrored;
            invalidateSelf();
        }
    }

    public final boolean isAutoMirrored() {
        return this.mBitmapState.mAutoMirrored;
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mBitmapState.getChangingConfigurations();
    }

    private boolean needMirroring() {
        return isAutoMirrored() && getLayoutDirection() == 1;
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect bounds) {
        this.mDstRectAndInsetsDirty = true;
        Bitmap bitmap = this.mBitmapState.mBitmap;
        Shader shader = this.mBitmapState.mPaint.getShader();
        if (bitmap != null && shader != null) {
            updateShaderMatrix(bitmap, this.mBitmapState.mPaint, shader, needMirroring());
        }
    }

    public void draw(Canvas canvas) {
        Bitmap bitmap = this.mBitmapState.mBitmap;
        if (bitmap != null) {
            int restoreAlpha;
            boolean clearColorFilter;
            BitmapState state = this.mBitmapState;
            Paint paint = state.mPaint;
            if (state.mRebuildShader) {
                TileMode tmx = state.mTileModeX;
                TileMode tmy = state.mTileModeY;
                if (tmx == null && tmy == null) {
                    paint.setShader(null);
                } else {
                    paint.setShader(new BitmapShader(bitmap, tmx == null ? TileMode.CLAMP : tmx, tmy == null ? TileMode.CLAMP : tmy));
                }
                state.mRebuildShader = false;
            }
            if (state.mBaseAlpha != 1.0f) {
                Paint p = getPaint();
                restoreAlpha = p.getAlpha();
                p.setAlpha((int) ((((float) restoreAlpha) * state.mBaseAlpha) + 0.5f));
            } else {
                restoreAlpha = -1;
            }
            if (this.mBlendModeFilter == null || paint.getColorFilter() != null) {
                clearColorFilter = false;
            } else {
                paint.setColorFilter(this.mBlendModeFilter);
                clearColorFilter = true;
            }
            updateDstRectAndInsetsIfDirty();
            Shader shader = paint.getShader();
            boolean needMirroring = needMirroring();
            if (shader == null) {
                if (needMirroring) {
                    canvas.save();
                    canvas.translate((float) (this.mDstRect.right - this.mDstRect.left), 0.0f);
                    canvas.scale(-1.0f, 1.0f);
                }
                canvas.drawBitmap(bitmap, null, this.mDstRect, paint);
                if (needMirroring) {
                    canvas.restore();
                }
            } else {
                updateShaderMatrix(bitmap, paint, shader, needMirroring);
                canvas.drawRect(this.mDstRect, paint);
            }
            if (clearColorFilter) {
                paint.setColorFilter(null);
            }
            if (restoreAlpha >= 0) {
                paint.setAlpha(restoreAlpha);
            }
        }
    }

    private void updateShaderMatrix(Bitmap bitmap, Paint paint, Shader shader, boolean needMirroring) {
        int sourceDensity = bitmap.getDensity();
        int targetDensity = this.mTargetDensity;
        boolean needScaling = (sourceDensity == 0 || sourceDensity == targetDensity) ? false : true;
        if (needScaling || needMirroring) {
            Matrix matrix = getOrCreateMirrorMatrix();
            matrix.reset();
            if (needMirroring) {
                matrix.setTranslate((float) (this.mDstRect.right - this.mDstRect.left), 0.0f);
                matrix.setScale(-1.0f, 1.0f);
            }
            if (needScaling) {
                float densityScale = ((float) targetDensity) / ((float) sourceDensity);
                matrix.postScale(densityScale, densityScale);
            }
            shader.setLocalMatrix(matrix);
        } else {
            this.mMirrorMatrix = null;
            shader.setLocalMatrix(Matrix.IDENTITY_MATRIX);
        }
        paint.setShader(shader);
    }

    private Matrix getOrCreateMirrorMatrix() {
        if (this.mMirrorMatrix == null) {
            this.mMirrorMatrix = new Matrix();
        }
        return this.mMirrorMatrix;
    }

    private void updateDstRectAndInsetsIfDirty() {
        if (this.mDstRectAndInsetsDirty) {
            if (this.mBitmapState.mTileModeX == null && this.mBitmapState.mTileModeY == null) {
                Rect bounds = getBounds();
                Rect rect = bounds;
                Gravity.apply(this.mBitmapState.mGravity, this.mBitmapWidth, this.mBitmapHeight, rect, this.mDstRect, getLayoutDirection());
                this.mOpticalInsets = Insets.of(this.mDstRect.left - bounds.left, this.mDstRect.top - bounds.top, bounds.right - this.mDstRect.right, bounds.bottom - this.mDstRect.bottom);
            } else {
                copyBounds(this.mDstRect);
                this.mOpticalInsets = Insets.NONE;
            }
        }
        this.mDstRectAndInsetsDirty = false;
    }

    public Insets getOpticalInsets() {
        updateDstRectAndInsetsIfDirty();
        return this.mOpticalInsets;
    }

    public void getOutline(Outline outline) {
        updateDstRectAndInsetsIfDirty();
        outline.setRect(this.mDstRect);
        boolean opaqueOverShape = (this.mBitmapState.mBitmap == null || this.mBitmapState.mBitmap.hasAlpha()) ? false : true;
        outline.setAlpha(opaqueOverShape ? ((float) getAlpha()) / 255.0f : 0.0f);
    }

    public void setAlpha(int alpha) {
        if (alpha != this.mBitmapState.mPaint.getAlpha()) {
            this.mBitmapState.mPaint.setAlpha(alpha);
            invalidateSelf();
        }
    }

    public int getAlpha() {
        return this.mBitmapState.mPaint.getAlpha();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mBitmapState.mPaint.setColorFilter(colorFilter);
        invalidateSelf();
    }

    public ColorFilter getColorFilter() {
        return this.mBitmapState.mPaint.getColorFilter();
    }

    public void setTintList(ColorStateList tint) {
        BitmapState state = this.mBitmapState;
        if (state.mTint != tint) {
            state.mTint = tint;
            this.mBlendModeFilter = updateBlendModeFilter(this.mBlendModeFilter, tint, this.mBitmapState.mBlendMode);
            invalidateSelf();
        }
    }

    public void setTintBlendMode(BlendMode blendMode) {
        BitmapState state = this.mBitmapState;
        if (state.mBlendMode != blendMode) {
            state.mBlendMode = blendMode;
            this.mBlendModeFilter = updateBlendModeFilter(this.mBlendModeFilter, this.mBitmapState.mTint, blendMode);
            invalidateSelf();
        }
    }

    @UnsupportedAppUsage
    public ColorStateList getTint() {
        return this.mBitmapState.mTint;
    }

    @UnsupportedAppUsage
    public Mode getTintMode() {
        return BlendMode.blendModeToPorterDuffMode(this.mBitmapState.mBlendMode);
    }

    public void setXfermode(Xfermode xfermode) {
        this.mBitmapState.mPaint.setXfermode(xfermode);
        invalidateSelf();
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mBitmapState = new BitmapState(this.mBitmapState);
            this.mMutated = true;
        }
        return this;
    }

    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    /* Access modifiers changed, original: protected */
    public boolean onStateChange(int[] stateSet) {
        BitmapState state = this.mBitmapState;
        if (state.mTint == null || state.mBlendMode == null) {
            return false;
        }
        this.mBlendModeFilter = updateBlendModeFilter(this.mBlendModeFilter, state.mTint, state.mBlendMode);
        return true;
    }

    public boolean isStateful() {
        return (this.mBitmapState.mTint != null && this.mBitmapState.mTint.isStateful()) || super.isStateful();
    }

    public boolean hasFocusStateSpecified() {
        return this.mBitmapState.mTint != null && this.mBitmapState.mTint.hasFocusStateSpecified();
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.BitmapDrawable);
        updateStateFromTypedArray(a, this.mSrcDensityOverride);
        verifyRequiredAttributes(a);
        a.recycle();
        updateLocalState(r);
    }

    private void verifyRequiredAttributes(TypedArray a) throws XmlPullParserException {
        BitmapState state = this.mBitmapState;
        if (state.mBitmap != null) {
            return;
        }
        if (state.mThemeAttrs == null || state.mThemeAttrs[1] == 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(a.getPositionDescription());
            stringBuilder.append(": <bitmap> requires a valid 'src' attribute");
            throw new XmlPullParserException(stringBuilder.toString());
        }
    }

    /* JADX WARNING: Missing block: B:32:0x007b, code skipped:
            if (r7 != null) goto L_0x007d;
     */
    /* JADX WARNING: Missing block: B:34:?, code skipped:
            $closeResource(r8, r7);
     */
    private void updateStateFromTypedArray(android.content.res.TypedArray r12, int r13) throws org.xmlpull.v1.XmlPullParserException {
        /*
        r11 = this;
        r0 = r12.getResources();
        r1 = r11.mBitmapState;
        r2 = r1.mChangingConfigurations;
        r3 = r12.getChangingConfigurations();
        r2 = r2 | r3;
        r1.mChangingConfigurations = r2;
        r2 = r12.extractThemeAttrs();
        r1.mThemeAttrs = r2;
        r1.mSrcDensityOverride = r13;
        r2 = 0;
        r3 = android.graphics.drawable.Drawable.resolveDensity(r0, r2);
        r1.mTargetDensity = r3;
        r3 = 1;
        r4 = r12.getResourceId(r3, r2);
        if (r4 == 0) goto L_0x00a2;
    L_0x0025:
        r5 = new android.util.TypedValue;
        r5.<init>();
        r0.getValueForDensity(r4, r13, r5, r3);
        r3 = 65535; // 0xffff float:9.1834E-41 double:3.23786E-319;
        if (r13 <= 0) goto L_0x0053;
    L_0x0032:
        r6 = r5.density;
        if (r6 <= 0) goto L_0x0053;
    L_0x0036:
        r6 = r5.density;
        if (r6 == r3) goto L_0x0053;
    L_0x003a:
        r6 = r5.density;
        if (r6 != r13) goto L_0x0047;
    L_0x003e:
        r6 = r0.getDisplayMetrics();
        r6 = r6.densityDpi;
        r5.density = r6;
        goto L_0x0053;
    L_0x0047:
        r6 = r5.density;
        r7 = r0.getDisplayMetrics();
        r7 = r7.densityDpi;
        r6 = r6 * r7;
        r6 = r6 / r13;
        r5.density = r6;
    L_0x0053:
        r6 = 0;
        r7 = r5.density;
        if (r7 != 0) goto L_0x005b;
    L_0x0058:
        r6 = 160; // 0xa0 float:2.24E-43 double:7.9E-322;
        goto L_0x0061;
    L_0x005b:
        r7 = r5.density;
        if (r7 == r3) goto L_0x0061;
    L_0x005f:
        r6 = r5.density;
    L_0x0061:
        r3 = 0;
        r7 = r0.openRawResource(r4, r5);	 Catch:{ Exception -> 0x0081 }
        r8 = 0;
        r9 = android.graphics.ImageDecoder.createSource(r0, r7, r6);	 Catch:{ all -> 0x0078 }
        r10 = android.graphics.drawable.-$$Lambda$BitmapDrawable$LMqt8JvxZ4giSOIRAtlCKDg39Jw.INSTANCE;	 Catch:{ all -> 0x0078 }
        r10 = android.graphics.ImageDecoder.decodeBitmap(r9, r10);	 Catch:{ all -> 0x0078 }
        r3 = r10;
        if (r7 == 0) goto L_0x0077;
    L_0x0074:
        $closeResource(r8, r7);	 Catch:{ Exception -> 0x0081 }
    L_0x0077:
        goto L_0x0082;
    L_0x0078:
        r8 = move-exception;
        throw r8;	 Catch:{ all -> 0x007a }
    L_0x007a:
        r9 = move-exception;
        if (r7 == 0) goto L_0x0080;
    L_0x007d:
        $closeResource(r8, r7);	 Catch:{ Exception -> 0x0081 }
    L_0x0080:
        throw r9;	 Catch:{ Exception -> 0x0081 }
    L_0x0081:
        r7 = move-exception;
    L_0x0082:
        if (r3 == 0) goto L_0x0087;
    L_0x0084:
        r1.mBitmap = r3;
        goto L_0x00a2;
    L_0x0087:
        r2 = new org.xmlpull.v1.XmlPullParserException;
        r7 = new java.lang.StringBuilder;
        r7.<init>();
        r8 = r12.getPositionDescription();
        r7.append(r8);
        r8 = ": <bitmap> requires a valid 'src' attribute";
        r7.append(r8);
        r7 = r7.toString();
        r2.<init>(r7);
        throw r2;
    L_0x00a2:
        r3 = r1.mBitmap;
        if (r3 == 0) goto L_0x00ad;
    L_0x00a6:
        r3 = r1.mBitmap;
        r3 = r3.hasMipMap();
        goto L_0x00ae;
    L_0x00ad:
        r3 = r2;
    L_0x00ae:
        r5 = 8;
        r5 = r12.getBoolean(r5, r3);
        r11.setMipMap(r5);
        r5 = 9;
        r6 = r1.mAutoMirrored;
        r5 = r12.getBoolean(r5, r6);
        r1.mAutoMirrored = r5;
        r5 = 7;
        r6 = r1.mBaseAlpha;
        r5 = r12.getFloat(r5, r6);
        r1.mBaseAlpha = r5;
        r5 = 10;
        r6 = -1;
        r5 = r12.getInt(r5, r6);
        if (r5 == r6) goto L_0x00db;
    L_0x00d3:
        r6 = android.graphics.BlendMode.SRC_IN;
        r6 = android.graphics.drawable.Drawable.parseBlendMode(r5, r6);
        r1.mBlendMode = r6;
    L_0x00db:
        r6 = 5;
        r6 = r12.getColorStateList(r6);
        if (r6 == 0) goto L_0x00e4;
    L_0x00e2:
        r1.mTint = r6;
    L_0x00e4:
        r7 = r11.mBitmapState;
        r7 = r7.mPaint;
        r8 = 2;
        r9 = r7.isAntiAlias();
        r8 = r12.getBoolean(r8, r9);
        r7.setAntiAlias(r8);
        r8 = 3;
        r9 = r7.isFilterBitmap();
        r8 = r12.getBoolean(r8, r9);
        r7.setFilterBitmap(r8);
        r8 = 4;
        r9 = r7.isDither();
        r8 = r12.getBoolean(r8, r9);
        r7.setDither(r8);
        r8 = r1.mGravity;
        r2 = r12.getInt(r2, r8);
        r11.setGravity(r2);
        r2 = 6;
        r8 = -2;
        r2 = r12.getInt(r2, r8);
        if (r2 == r8) goto L_0x0124;
    L_0x011d:
        r9 = parseTileMode(r2);
        r11.setTileModeXY(r9, r9);
    L_0x0124:
        r9 = 11;
        r9 = r12.getInt(r9, r8);
        if (r9 == r8) goto L_0x0133;
    L_0x012c:
        r10 = parseTileMode(r9);
        r11.setTileModeX(r10);
    L_0x0133:
        r10 = 12;
        r10 = r12.getInt(r10, r8);
        if (r10 == r8) goto L_0x0142;
    L_0x013b:
        r8 = parseTileMode(r10);
        r11.setTileModeY(r8);
    L_0x0142:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.BitmapDrawable.updateStateFromTypedArray(android.content.res.TypedArray, int):void");
    }

    public void applyTheme(Theme t) {
        super.applyTheme(t);
        BitmapState state = this.mBitmapState;
        if (state != null) {
            if (state.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.BitmapDrawable);
                try {
                    updateStateFromTypedArray(a, state.mSrcDensityOverride);
                } catch (XmlPullParserException e) {
                    Drawable.rethrowAsRuntimeException(e);
                } catch (Throwable th) {
                    a.recycle();
                }
                a.recycle();
            }
            if (state.mTint != null && state.mTint.canApplyTheme()) {
                state.mTint = state.mTint.obtainForTheme(t);
            }
            updateLocalState(t.getResources());
        }
    }

    private static TileMode parseTileMode(int tileMode) {
        if (tileMode == 0) {
            return TileMode.CLAMP;
        }
        if (tileMode == 1) {
            return TileMode.REPEAT;
        }
        if (tileMode != 2) {
            return null;
        }
        return TileMode.MIRROR;
    }

    public boolean canApplyTheme() {
        BitmapState bitmapState = this.mBitmapState;
        return bitmapState != null && bitmapState.canApplyTheme();
    }

    public int getIntrinsicWidth() {
        return this.mBitmapWidth;
    }

    public int getIntrinsicHeight() {
        return this.mBitmapHeight;
    }

    public int getOpacity() {
        int i = -3;
        if (this.mBitmapState.mGravity != 119) {
            return -3;
        }
        Bitmap bitmap = this.mBitmapState.mBitmap;
        if (!(bitmap == null || bitmap.hasAlpha() || this.mBitmapState.mPaint.getAlpha() < 255)) {
            i = -1;
        }
        return i;
    }

    public final ConstantState getConstantState() {
        BitmapState bitmapState = this.mBitmapState;
        bitmapState.mChangingConfigurations |= getChangingConfigurations();
        return this.mBitmapState;
    }

    private BitmapDrawable(BitmapState state, Resources res) {
        this.mDstRect = new Rect();
        this.mTargetDensity = 160;
        this.mDstRectAndInsetsDirty = true;
        this.mOpticalInsets = Insets.NONE;
        init(state, res);
    }

    private void init(BitmapState state, Resources res) {
        this.mBitmapState = state;
        updateLocalState(res);
        BitmapState bitmapState = this.mBitmapState;
        if (bitmapState != null && res != null) {
            bitmapState.mTargetDensity = this.mTargetDensity;
        }
    }

    private void updateLocalState(Resources res) {
        this.mTargetDensity = Drawable.resolveDensity(res, this.mBitmapState.mTargetDensity);
        this.mBlendModeFilter = updateBlendModeFilter(this.mBlendModeFilter, this.mBitmapState.mTint, this.mBitmapState.mBlendMode);
        computeBitmapSize();
    }
}
