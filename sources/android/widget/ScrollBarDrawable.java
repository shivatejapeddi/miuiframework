package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.graphics.Canvas;
import android.graphics.Canvas.EdgeType;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import com.android.internal.widget.ScrollBarUtils;

public class ScrollBarDrawable extends Drawable implements Callback {
    private int mAlpha = 255;
    private boolean mAlwaysDrawHorizontalTrack;
    private boolean mAlwaysDrawVerticalTrack;
    private boolean mBoundsChanged;
    private ColorFilter mColorFilter;
    private int mExtent;
    private boolean mHasSetAlpha;
    private boolean mHasSetColorFilter;
    private Drawable mHorizontalThumb;
    private Drawable mHorizontalTrack;
    private boolean mMutated;
    private int mOffset;
    private int mRange;
    private boolean mRangeChanged;
    private boolean mVertical;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768422)
    private Drawable mVerticalThumb;
    private Drawable mVerticalTrack;

    public void setAlwaysDrawHorizontalTrack(boolean alwaysDrawTrack) {
        this.mAlwaysDrawHorizontalTrack = alwaysDrawTrack;
    }

    public void setAlwaysDrawVerticalTrack(boolean alwaysDrawTrack) {
        this.mAlwaysDrawVerticalTrack = alwaysDrawTrack;
    }

    public boolean getAlwaysDrawVerticalTrack() {
        return this.mAlwaysDrawVerticalTrack;
    }

    public boolean getAlwaysDrawHorizontalTrack() {
        return this.mAlwaysDrawHorizontalTrack;
    }

    public void setParameters(int range, int offset, int extent, boolean vertical) {
        if (this.mVertical != vertical) {
            this.mVertical = vertical;
            this.mBoundsChanged = true;
        }
        if (this.mRange != range || this.mOffset != offset || this.mExtent != extent) {
            this.mRange = range;
            this.mOffset = offset;
            this.mExtent = extent;
            this.mRangeChanged = true;
        }
    }

    public void draw(Canvas canvas) {
        boolean drawTrack;
        boolean drawThumb;
        boolean vertical = this.mVertical;
        int extent = this.mExtent;
        int range = this.mRange;
        if (extent <= 0 || range <= extent) {
            drawTrack = vertical ? this.mAlwaysDrawVerticalTrack : this.mAlwaysDrawHorizontalTrack;
            drawThumb = false;
        } else {
            drawTrack = true;
            drawThumb = true;
        }
        Rect r = getBounds();
        if (!canvas.quickReject((float) r.left, (float) r.top, (float) r.right, (float) r.bottom, EdgeType.AA)) {
            if (drawTrack) {
                drawTrack(canvas, r, vertical);
            } else {
                Canvas canvas2 = canvas;
            }
            if (drawThumb) {
                int scrollBarLength = vertical ? r.height() : r.width();
                int thumbLength = ScrollBarUtils.getThumbLength(scrollBarLength, vertical ? r.width() : r.height(), extent, range);
                drawThumb(canvas, r, ScrollBarUtils.getThumbOffset(scrollBarLength, thumbLength, extent, range, this.mOffset), thumbLength, vertical);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        this.mBoundsChanged = true;
    }

    public boolean isStateful() {
        Drawable drawable = this.mVerticalTrack;
        if (drawable == null || !drawable.isStateful()) {
            drawable = this.mVerticalThumb;
            if (drawable == null || !drawable.isStateful()) {
                drawable = this.mHorizontalTrack;
                if (drawable == null || !drawable.isStateful()) {
                    drawable = this.mHorizontalThumb;
                    if ((drawable == null || !drawable.isStateful()) && !super.isStateful()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean onStateChange(int[] state) {
        boolean changed = super.onStateChange(state);
        Drawable drawable = this.mVerticalTrack;
        if (drawable != null) {
            changed |= drawable.setState(state);
        }
        drawable = this.mVerticalThumb;
        if (drawable != null) {
            changed |= drawable.setState(state);
        }
        drawable = this.mHorizontalTrack;
        if (drawable != null) {
            changed |= drawable.setState(state);
        }
        drawable = this.mHorizontalThumb;
        if (drawable != null) {
            return changed | drawable.setState(state);
        }
        return changed;
    }

    private void drawTrack(Canvas canvas, Rect bounds, boolean vertical) {
        Drawable track;
        if (vertical) {
            track = this.mVerticalTrack;
        } else {
            track = this.mHorizontalTrack;
        }
        if (track != null) {
            if (this.mBoundsChanged) {
                track.setBounds(bounds);
            }
            track.draw(canvas);
        }
    }

    private void drawThumb(Canvas canvas, Rect bounds, int offset, int length, boolean vertical) {
        boolean changed = this.mRangeChanged || this.mBoundsChanged;
        Drawable thumb;
        if (vertical) {
            if (this.mVerticalThumb != null) {
                thumb = this.mVerticalThumb;
                if (changed) {
                    thumb.setBounds(bounds.left, bounds.top + offset, bounds.right, (bounds.top + offset) + length);
                }
                thumb.draw(canvas);
            }
        } else if (this.mHorizontalThumb != null) {
            thumb = this.mHorizontalThumb;
            if (changed) {
                thumb.setBounds(bounds.left + offset, bounds.top, (bounds.left + offset) + length, bounds.bottom);
            }
            thumb.draw(canvas);
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public void setVerticalThumbDrawable(Drawable thumb) {
        Drawable drawable = this.mVerticalThumb;
        if (drawable != null) {
            drawable.setCallback(null);
        }
        propagateCurrentState(thumb);
        this.mVerticalThumb = thumb;
    }

    public Drawable getVerticalTrackDrawable() {
        return this.mVerticalTrack;
    }

    public Drawable getVerticalThumbDrawable() {
        return this.mVerticalThumb;
    }

    public Drawable getHorizontalTrackDrawable() {
        return this.mHorizontalTrack;
    }

    public Drawable getHorizontalThumbDrawable() {
        return this.mHorizontalThumb;
    }

    public void setVerticalTrackDrawable(Drawable track) {
        Drawable drawable = this.mVerticalTrack;
        if (drawable != null) {
            drawable.setCallback(null);
        }
        propagateCurrentState(track);
        this.mVerticalTrack = track;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public void setHorizontalThumbDrawable(Drawable thumb) {
        Drawable drawable = this.mHorizontalThumb;
        if (drawable != null) {
            drawable.setCallback(null);
        }
        propagateCurrentState(thumb);
        this.mHorizontalThumb = thumb;
    }

    public void setHorizontalTrackDrawable(Drawable track) {
        Drawable drawable = this.mHorizontalTrack;
        if (drawable != null) {
            drawable.setCallback(null);
        }
        propagateCurrentState(track);
        this.mHorizontalTrack = track;
    }

    private void propagateCurrentState(Drawable d) {
        if (d != null) {
            if (this.mMutated) {
                d.mutate();
            }
            d.setState(getState());
            d.setCallback(this);
            if (this.mHasSetAlpha) {
                d.setAlpha(this.mAlpha);
            }
            if (this.mHasSetColorFilter) {
                d.setColorFilter(this.mColorFilter);
            }
        }
    }

    public int getSize(boolean vertical) {
        int i = 0;
        Drawable drawable;
        if (vertical) {
            drawable = this.mVerticalTrack;
            if (drawable != null) {
                i = drawable.getIntrinsicWidth();
            } else {
                drawable = this.mVerticalThumb;
                if (drawable != null) {
                    i = drawable.getIntrinsicWidth();
                }
            }
            return i;
        }
        drawable = this.mHorizontalTrack;
        if (drawable != null) {
            i = drawable.getIntrinsicHeight();
        } else {
            drawable = this.mHorizontalThumb;
            if (drawable != null) {
                i = drawable.getIntrinsicHeight();
            }
        }
        return i;
    }

    public ScrollBarDrawable mutate() {
        if (!this.mMutated && super.mutate() == this) {
            Drawable drawable = this.mVerticalTrack;
            if (drawable != null) {
                drawable.mutate();
            }
            drawable = this.mVerticalThumb;
            if (drawable != null) {
                drawable.mutate();
            }
            drawable = this.mHorizontalTrack;
            if (drawable != null) {
                drawable.mutate();
            }
            drawable = this.mHorizontalThumb;
            if (drawable != null) {
                drawable.mutate();
            }
            this.mMutated = true;
        }
        return this;
    }

    public void setAlpha(int alpha) {
        this.mAlpha = alpha;
        this.mHasSetAlpha = true;
        Drawable drawable = this.mVerticalTrack;
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
        drawable = this.mVerticalThumb;
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
        drawable = this.mHorizontalTrack;
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
        drawable = this.mHorizontalThumb;
        if (drawable != null) {
            drawable.setAlpha(alpha);
        }
    }

    public int getAlpha() {
        return this.mAlpha;
    }

    public void setColorFilter(ColorFilter colorFilter) {
        this.mColorFilter = colorFilter;
        this.mHasSetColorFilter = true;
        Drawable drawable = this.mVerticalTrack;
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
        }
        drawable = this.mVerticalThumb;
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
        }
        drawable = this.mHorizontalTrack;
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
        }
        drawable = this.mHorizontalThumb;
        if (drawable != null) {
            drawable.setColorFilter(colorFilter);
        }
    }

    public ColorFilter getColorFilter() {
        return this.mColorFilter;
    }

    public int getOpacity() {
        return -3;
    }

    public void invalidateDrawable(Drawable who) {
        invalidateSelf();
    }

    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        scheduleSelf(what, when);
    }

    public void unscheduleDrawable(Drawable who, Runnable what) {
        unscheduleSelf(what);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ScrollBarDrawable: range=");
        stringBuilder.append(this.mRange);
        stringBuilder.append(" offset=");
        stringBuilder.append(this.mOffset);
        stringBuilder.append(" extent=");
        stringBuilder.append(this.mExtent);
        stringBuilder.append(this.mVertical ? " V" : " H");
        return stringBuilder.toString();
    }
}
