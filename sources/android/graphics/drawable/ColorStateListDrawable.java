package android.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources.Theme;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.Drawable.ConstantState;
import android.util.MathUtils;

public class ColorStateListDrawable extends Drawable implements Callback {
    private ColorDrawable mColorDrawable;
    private boolean mMutated;
    private ColorStateListDrawableState mState;

    static final class ColorStateListDrawableState extends ConstantState {
        int mAlpha = -1;
        BlendMode mBlendMode = Drawable.DEFAULT_BLEND_MODE;
        int mChangingConfigurations = 0;
        ColorStateList mColor = null;
        ColorStateList mTint = null;

        ColorStateListDrawableState() {
        }

        ColorStateListDrawableState(ColorStateListDrawableState state) {
            this.mColor = state.mColor;
            this.mTint = state.mTint;
            this.mAlpha = state.mAlpha;
            this.mBlendMode = state.mBlendMode;
            this.mChangingConfigurations = state.mChangingConfigurations;
        }

        public Drawable newDrawable() {
            return new ColorStateListDrawable(this);
        }

        public int getChangingConfigurations() {
            int i = this.mChangingConfigurations;
            ColorStateList colorStateList = this.mColor;
            int i2 = 0;
            i |= colorStateList != null ? colorStateList.getChangingConfigurations() : 0;
            colorStateList = this.mTint;
            if (colorStateList != null) {
                i2 = colorStateList.getChangingConfigurations();
            }
            return i | i2;
        }

        public boolean isStateful() {
            ColorStateList colorStateList = this.mColor;
            if (colorStateList == null || !colorStateList.isStateful()) {
                colorStateList = this.mTint;
                if (colorStateList == null || !colorStateList.isStateful()) {
                    return false;
                }
            }
            return true;
        }

        public boolean hasFocusStateSpecified() {
            ColorStateList colorStateList = this.mColor;
            if (colorStateList == null || !colorStateList.hasFocusStateSpecified()) {
                colorStateList = this.mTint;
                if (colorStateList == null || !colorStateList.hasFocusStateSpecified()) {
                    return false;
                }
            }
            return true;
        }

        public boolean canApplyTheme() {
            ColorStateList colorStateList = this.mColor;
            if (colorStateList == null || !colorStateList.canApplyTheme()) {
                colorStateList = this.mTint;
                if (colorStateList == null || !colorStateList.canApplyTheme()) {
                    return false;
                }
            }
            return true;
        }
    }

    public ColorStateListDrawable() {
        this.mMutated = false;
        this.mState = new ColorStateListDrawableState();
        initializeColorDrawable();
    }

    public ColorStateListDrawable(ColorStateList colorStateList) {
        this.mMutated = false;
        this.mState = new ColorStateListDrawableState();
        initializeColorDrawable();
        setColorStateList(colorStateList);
    }

    public void draw(Canvas canvas) {
        this.mColorDrawable.draw(canvas);
    }

    public int getAlpha() {
        return this.mColorDrawable.getAlpha();
    }

    public boolean isStateful() {
        return this.mState.isStateful();
    }

    public boolean hasFocusStateSpecified() {
        return this.mState.hasFocusStateSpecified();
    }

    public Drawable getCurrent() {
        return this.mColorDrawable;
    }

    public void applyTheme(Theme t) {
        super.applyTheme(t);
        if (this.mState.mColor != null) {
            setColorStateList(this.mState.mColor.obtainForTheme(t));
        }
        if (this.mState.mTint != null) {
            setTintList(this.mState.mTint.obtainForTheme(t));
        }
    }

    public boolean canApplyTheme() {
        return super.canApplyTheme() || this.mState.canApplyTheme();
    }

    public void setAlpha(int alpha) {
        this.mState.mAlpha = alpha;
        onStateChange(getState());
    }

    public void clearAlpha() {
        this.mState.mAlpha = -1;
        onStateChange(getState());
    }

    public void setTintList(ColorStateList tint) {
        this.mState.mTint = tint;
        this.mColorDrawable.setTintList(tint);
        onStateChange(getState());
    }

    public void setTintBlendMode(BlendMode blendMode) {
        this.mState.mBlendMode = blendMode;
        this.mColorDrawable.setTintBlendMode(blendMode);
        onStateChange(getState());
    }

    public ColorFilter getColorFilter() {
        return this.mColorDrawable.getColorFilter();
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorDrawable.setColorFilter(colorFilter);
    }

    public int getOpacity() {
        return this.mColorDrawable.getOpacity();
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mColorDrawable.setBounds(bounds);
    }

    /* Access modifiers changed, original: protected */
    public boolean onStateChange(int[] state) {
        if (this.mState.mColor == null) {
            return false;
        }
        int color = this.mState.mColor.getColorForState(state, this.mState.mColor.getDefaultColor());
        if (this.mState.mAlpha != -1) {
            color = (16777215 & color) | (MathUtils.constrain(this.mState.mAlpha, 0, 255) << 24);
        }
        if (color == this.mColorDrawable.getColor()) {
            return this.mColorDrawable.setState(state);
        }
        this.mColorDrawable.setColor(color);
        this.mColorDrawable.setState(state);
        return true;
    }

    public void invalidateDrawable(Drawable who) {
        if (who == this.mColorDrawable && getCallback() != null) {
            getCallback().invalidateDrawable(this);
        }
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        if (who == this.mColorDrawable && getCallback() != null) {
            getCallback().scheduleDrawable(this, what, when);
        }
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        if (who == this.mColorDrawable && getCallback() != null) {
            getCallback().unscheduleDrawable(this, what);
        }
    }

    public ConstantState getConstantState() {
        ColorStateListDrawableState colorStateListDrawableState = this.mState;
        colorStateListDrawableState.mChangingConfigurations |= getChangingConfigurations() & (~this.mState.getChangingConfigurations());
        return this.mState;
    }

    public ColorStateList getColorStateList() {
        if (this.mState.mColor == null) {
            return ColorStateList.valueOf(this.mColorDrawable.getColor());
        }
        return this.mState.mColor;
    }

    public int getChangingConfigurations() {
        return super.getChangingConfigurations() | this.mState.getChangingConfigurations();
    }

    public Drawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            this.mState = new ColorStateListDrawableState(this.mState);
            this.mMutated = true;
        }
        return this;
    }

    public void clearMutated() {
        super.clearMutated();
        this.mMutated = false;
    }

    public void setColorStateList(ColorStateList colorStateList) {
        this.mState.mColor = colorStateList;
        onStateChange(getState());
    }

    private ColorStateListDrawable(ColorStateListDrawableState state) {
        this.mMutated = false;
        this.mState = state;
        initializeColorDrawable();
    }

    private void initializeColorDrawable() {
        this.mColorDrawable = new ColorDrawable();
        this.mColorDrawable.setCallback(this);
        if (this.mState.mTint != null) {
            this.mColorDrawable.setTintList(this.mState.mTint);
        }
        if (this.mState.mBlendMode != DEFAULT_BLEND_MODE) {
            this.mColorDrawable.setTintBlendMode(this.mState.mBlendMode);
        }
    }
}
