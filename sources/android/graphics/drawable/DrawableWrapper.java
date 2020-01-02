package android.graphics.drawable;

import android.annotation.UnsupportedAppUsage;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Insets;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.AttributeSet;
import com.android.internal.R;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public abstract class DrawableWrapper extends Drawable implements Callback {
    private Drawable mDrawable;
    private boolean mMutated;
    @UnsupportedAppUsage
    private DrawableWrapperState mState;

    static abstract class DrawableWrapperState extends ConstantState {
        int mChangingConfigurations;
        int mDensity = 160;
        ConstantState mDrawableState;
        int mSrcDensityOverride = 0;
        private int[] mThemeAttrs;

        public abstract Drawable newDrawable(Resources resources);

        DrawableWrapperState(DrawableWrapperState orig, Resources res) {
            int density;
            int i = 160;
            if (orig != null) {
                this.mThemeAttrs = orig.mThemeAttrs;
                this.mChangingConfigurations = orig.mChangingConfigurations;
                this.mDrawableState = orig.mDrawableState;
                this.mSrcDensityOverride = orig.mSrcDensityOverride;
            }
            if (res != null) {
                density = res.getDisplayMetrics().densityDpi;
            } else if (orig != null) {
                density = orig.mDensity;
            } else {
                density = 0;
            }
            if (density != 0) {
                i = density;
            }
            this.mDensity = i;
        }

        public final void setDensity(int targetDensity) {
            if (this.mDensity != targetDensity) {
                int sourceDensity = this.mDensity;
                this.mDensity = targetDensity;
                onDensityChanged(sourceDensity, targetDensity);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void onDensityChanged(int sourceDensity, int targetDensity) {
        }

        public boolean canApplyTheme() {
            if (this.mThemeAttrs == null) {
                ConstantState constantState = this.mDrawableState;
                if ((constantState == null || !constantState.canApplyTheme()) && !super.canApplyTheme()) {
                    return false;
                }
            }
            return true;
        }

        public Drawable newDrawable() {
            return newDrawable(null);
        }

        public int getChangingConfigurations() {
            int i = this.mChangingConfigurations;
            ConstantState constantState = this.mDrawableState;
            return i | (constantState != null ? constantState.getChangingConfigurations() : 0);
        }

        public boolean canConstantState() {
            return this.mDrawableState != null;
        }
    }

    DrawableWrapper(DrawableWrapperState state, Resources res) {
        this.mState = state;
        updateLocalState(res);
    }

    public DrawableWrapper(Drawable dr) {
        this.mState = null;
        setDrawable(dr);
    }

    private void updateLocalState(Resources res) {
        DrawableWrapperState drawableWrapperState = this.mState;
        if (drawableWrapperState != null && drawableWrapperState.mDrawableState != null) {
            setDrawable(this.mState.mDrawableState.newDrawable(res));
        }
    }

    public void setXfermode(Xfermode mode) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setXfermode(mode);
        }
    }

    public void setDrawable(Drawable dr) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setCallback(null);
        }
        this.mDrawable = dr;
        if (dr != null) {
            dr.setCallback(this);
            dr.setVisible(isVisible(), true);
            dr.setState(getState());
            dr.setLevel(getLevel());
            dr.setBounds(getBounds());
            dr.setLayoutDirection(getLayoutDirection());
            DrawableWrapperState drawableWrapperState = this.mState;
            if (drawableWrapperState != null) {
                drawableWrapperState.mDrawableState = dr.getConstantState();
            }
        }
        invalidateSelf();
    }

    public Drawable getDrawable() {
        return this.mDrawable;
    }

    public void inflate(Resources r, XmlPullParser parser, AttributeSet attrs, Theme theme) throws XmlPullParserException, IOException {
        super.inflate(r, parser, attrs, theme);
        DrawableWrapperState state = this.mState;
        if (state != null) {
            int densityDpi = r.getDisplayMetrics().densityDpi;
            state.setDensity(densityDpi == 0 ? 160 : densityDpi);
            state.mSrcDensityOverride = this.mSrcDensityOverride;
            TypedArray a = Drawable.obtainAttributes(r, theme, attrs, R.styleable.DrawableWrapper);
            updateStateFromTypedArray(a);
            a.recycle();
            inflateChildDrawable(r, parser, attrs, theme);
        }
    }

    public void applyTheme(Theme t) {
        super.applyTheme(t);
        Drawable drawable = this.mDrawable;
        if (drawable != null && drawable.canApplyTheme()) {
            this.mDrawable.applyTheme(t);
        }
        DrawableWrapperState state = this.mState;
        if (state != null) {
            int densityDpi = t.getResources().getDisplayMetrics().densityDpi;
            state.setDensity(densityDpi == 0 ? 160 : densityDpi);
            if (state.mThemeAttrs != null) {
                TypedArray a = t.resolveAttributes(state.mThemeAttrs, R.styleable.DrawableWrapper);
                updateStateFromTypedArray(a);
                a.recycle();
            }
        }
    }

    private void updateStateFromTypedArray(TypedArray a) {
        DrawableWrapperState state = this.mState;
        if (state != null) {
            state.mChangingConfigurations |= a.getChangingConfigurations();
            state.mThemeAttrs = a.extractThemeAttrs();
            if (a.hasValueOrEmpty(0)) {
                setDrawable(a.getDrawable(0));
            }
        }
    }

    public boolean canApplyTheme() {
        DrawableWrapperState drawableWrapperState = this.mState;
        return (drawableWrapperState != null && drawableWrapperState.canApplyTheme()) || super.canApplyTheme();
    }

    public void invalidateDrawable(Drawable who) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.invalidateDrawable(this);
        }
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.scheduleDrawable(this, what, when);
        }
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        Callback callback = getCallback();
        if (callback != null) {
            callback.unscheduleDrawable(this, what);
        }
    }

    public void draw(Canvas canvas) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    public int getChangingConfigurations() {
        int changingConfigurations = super.getChangingConfigurations();
        DrawableWrapperState drawableWrapperState = this.mState;
        return (changingConfigurations | (drawableWrapperState != null ? drawableWrapperState.getChangingConfigurations() : 0)) | this.mDrawable.getChangingConfigurations();
    }

    public boolean getPadding(Rect padding) {
        Drawable drawable = this.mDrawable;
        return drawable != null && drawable.getPadding(padding);
    }

    public Insets getOpticalInsets() {
        Drawable drawable = this.mDrawable;
        return drawable != null ? drawable.getOpticalInsets() : Insets.NONE;
    }

    public void setHotspot(float x, float y) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
    }

    public void setHotspotBounds(int left, int top, int right, int bottom) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setHotspotBounds(left, top, right, bottom);
        }
    }

    public void getHotspotBounds(Rect outRect) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.getHotspotBounds(outRect);
        } else {
            outRect.set(getBounds());
        }
    }

    public boolean setVisible(boolean visible, boolean restart) {
        boolean superChanged = super.setVisible(visible, restart);
        Drawable drawable = this.mDrawable;
        boolean changed = drawable != null && drawable.setVisible(visible, restart);
        return superChanged | changed;
    }

    public void setAlpha(int alpha) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
    }

    public int getAlpha() {
        Drawable drawable = this.mDrawable;
        return drawable != null ? drawable.getAlpha() : 255;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
        }
    }

    public ColorFilter getColorFilter() {
        Drawable drawable = getDrawable();
        if (drawable != null) {
            return drawable.getColorFilter();
        }
        return super.getColorFilter();
    }

    public void setTintList(ColorStateList tint) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setTintList(tint);
        }
    }

    public void setTintBlendMode(BlendMode blendMode) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setTintBlendMode(blendMode);
        }
    }

    public boolean onLayoutDirectionChanged(int layoutDirection) {
        Drawable drawable = this.mDrawable;
        return drawable != null && drawable.setLayoutDirection(layoutDirection);
    }

    public int getOpacity() {
        Drawable drawable = this.mDrawable;
        return drawable != null ? drawable.getOpacity() : -2;
    }

    public boolean isStateful() {
        Drawable drawable = this.mDrawable;
        return drawable != null && drawable.isStateful();
    }

    public boolean hasFocusStateSpecified() {
        Drawable drawable = this.mDrawable;
        return drawable != null && drawable.hasFocusStateSpecified();
    }

    /* Access modifiers changed, original: protected */
    public boolean onStateChange(int[] state) {
        Drawable drawable = this.mDrawable;
        if (drawable == null || !drawable.isStateful()) {
            return false;
        }
        boolean changed = this.mDrawable.setState(state);
        if (changed) {
            onBoundsChange(getBounds());
        }
        return changed;
    }

    /* Access modifiers changed, original: protected */
    public boolean onLevelChange(int level) {
        Drawable drawable = this.mDrawable;
        return drawable != null && drawable.setLevel(level);
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect bounds) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.setBounds(bounds);
        }
    }

    public int getIntrinsicWidth() {
        Drawable drawable = this.mDrawable;
        return drawable != null ? drawable.getIntrinsicWidth() : -1;
    }

    public int getIntrinsicHeight() {
        Drawable drawable = this.mDrawable;
        return drawable != null ? drawable.getIntrinsicHeight() : -1;
    }

    public void getOutline(Outline outline) {
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.getOutline(outline);
        } else {
            super.getOutline(outline);
        }
    }

    public ConstantState getConstantState() {
        DrawableWrapperState drawableWrapperState = this.mState;
        if (drawableWrapperState == null || !drawableWrapperState.canConstantState()) {
            return null;
        }
        this.mState.mChangingConfigurations = getChangingConfigurations();
        return this.mState;
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState = mutateConstantState();
            Drawable drawable = this.mDrawable;
            if (drawable != null) {
                drawable.mutate();
            }
            DrawableWrapperState drawableWrapperState = this.mState;
            if (drawableWrapperState != null) {
                Drawable drawable2 = this.mDrawable;
                drawableWrapperState.mDrawableState = drawable2 != null ? drawable2.getConstantState() : null;
            }
            this.mMutated = true;
        }
        return this;
    }

    /* Access modifiers changed, original: 0000 */
    public DrawableWrapperState mutateConstantState() {
        return this.mState;
    }

    public void clearMutated() {
        super.clearMutated();
        Drawable drawable = this.mDrawable;
        if (drawable != null) {
            drawable.clearMutated();
        }
        this.mMutated = false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0024  */
    private void inflateChildDrawable(android.content.res.Resources r6, org.xmlpull.v1.XmlPullParser r7, android.util.AttributeSet r8, android.content.res.Resources.Theme r9) throws org.xmlpull.v1.XmlPullParserException, java.io.IOException {
        /*
        r5 = this;
        r0 = 0;
        r1 = r7.getDepth();
    L_0x0005:
        r2 = r7.next();
        r3 = r2;
        r4 = 1;
        if (r2 == r4) goto L_0x0022;
    L_0x000d:
        r2 = 3;
        if (r3 != r2) goto L_0x0016;
    L_0x0010:
        r2 = r7.getDepth();
        if (r2 <= r1) goto L_0x0022;
    L_0x0016:
        r2 = 2;
        if (r3 != r2) goto L_0x0005;
    L_0x0019:
        r2 = r5.mState;
        r2 = r2.mSrcDensityOverride;
        r0 = android.graphics.drawable.Drawable.createFromXmlInnerForDensity(r6, r7, r8, r2, r9);
        goto L_0x0005;
    L_0x0022:
        if (r0 == 0) goto L_0x0027;
    L_0x0024:
        r5.setDrawable(r0);
    L_0x0027:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.graphics.drawable.DrawableWrapper.inflateChildDrawable(android.content.res.Resources, org.xmlpull.v1.XmlPullParser, android.util.AttributeSet, android.content.res.Resources$Theme):void");
    }
}
