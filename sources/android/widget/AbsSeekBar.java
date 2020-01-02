package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Insets;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.Region.Op;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import com.android.internal.R;
import com.android.internal.util.Preconditions;
import com.miui.internal.variable.api.v29.Android_Widget_AbsSeekBar.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_AbsSeekBar.Interface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbsSeekBar extends ProgressBar {
    private static final int NO_ALPHA = 255;
    @UnsupportedAppUsage
    private float mDisabledAlpha;
    private final List<Rect> mGestureExclusionRects;
    private boolean mHasThumbBlendMode;
    private boolean mHasThumbTint;
    private boolean mHasTickMarkBlendMode;
    private boolean mHasTickMarkTint;
    @UnsupportedAppUsage
    private boolean mIsDragging;
    @UnsupportedAppUsage
    boolean mIsUserSeekable;
    private int mKeyProgressIncrement;
    private int mScaledTouchSlop;
    @UnsupportedAppUsage
    private boolean mSplitTrack;
    private final Rect mTempRect;
    @UnsupportedAppUsage
    private Drawable mThumb;
    private BlendMode mThumbBlendMode;
    private int mThumbOffset;
    private final Rect mThumbRect;
    private ColorStateList mThumbTintList;
    private Drawable mTickMark;
    private BlendMode mTickMarkBlendMode;
    private ColorStateList mTickMarkTintList;
    private float mTouchDownX;
    @UnsupportedAppUsage
    float mTouchProgressOffset;
    private List<Rect> mUserGestureExclusionRects;

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<AbsSeekBar> {
        private boolean mPropertiesMapped = false;
        private int mThumbTintId;
        private int mThumbTintModeId;
        private int mTickMarkTintBlendModeId;
        private int mTickMarkTintId;
        private int mTickMarkTintModeId;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mThumbTintId = propertyMapper.mapObject("thumbTint", 16843889);
            this.mThumbTintModeId = propertyMapper.mapObject("thumbTintMode", 16843890);
            this.mTickMarkTintId = propertyMapper.mapObject("tickMarkTint", 16844043);
            this.mTickMarkTintBlendModeId = propertyMapper.mapObject("tickMarkTintBlendMode", 7);
            this.mTickMarkTintModeId = propertyMapper.mapObject("tickMarkTintMode", 16844044);
            this.mPropertiesMapped = true;
        }

        public void readProperties(AbsSeekBar node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readObject(this.mThumbTintId, node.getThumbTintList());
                propertyReader.readObject(this.mThumbTintModeId, node.getThumbTintMode());
                propertyReader.readObject(this.mTickMarkTintId, node.getTickMarkTintList());
                propertyReader.readObject(this.mTickMarkTintBlendModeId, node.getTickMarkTintBlendMode());
                propertyReader.readObject(this.mTickMarkTintModeId, node.getTickMarkTintMode());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public AbsSeekBar(Context context) {
        super(context);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbBlendMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbBlendMode = false;
        this.mTickMarkTintList = null;
        this.mTickMarkBlendMode = null;
        this.mHasTickMarkTint = false;
        this.mHasTickMarkBlendMode = false;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mUserGestureExclusionRects = Collections.emptyList();
        this.mGestureExclusionRects = new ArrayList();
        this.mThumbRect = new Rect();
    }

    public AbsSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbBlendMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbBlendMode = false;
        this.mTickMarkTintList = null;
        this.mTickMarkBlendMode = null;
        this.mHasTickMarkTint = false;
        this.mHasTickMarkBlendMode = false;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mUserGestureExclusionRects = Collections.emptyList();
        this.mGestureExclusionRects = new ArrayList();
        this.mThumbRect = new Rect();
    }

    public AbsSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AbsSeekBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTempRect = new Rect();
        this.mThumbTintList = null;
        this.mThumbBlendMode = null;
        this.mHasThumbTint = false;
        this.mHasThumbBlendMode = false;
        this.mTickMarkTintList = null;
        this.mTickMarkBlendMode = null;
        this.mHasTickMarkTint = false;
        this.mHasTickMarkBlendMode = false;
        this.mIsUserSeekable = true;
        this.mKeyProgressIncrement = 1;
        this.mUserGestureExclusionRects = Collections.emptyList();
        this.mGestureExclusionRects = new ArrayList();
        this.mThumbRect = new Rect();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekBar, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.SeekBar, attrs, a, defStyleAttr, defStyleRes);
        setThumb(a.getDrawable(0));
        if (a.hasValue(4)) {
            this.mThumbBlendMode = Drawable.parseBlendMode(a.getInt(4, -1), this.mThumbBlendMode);
            this.mHasThumbBlendMode = true;
        }
        if (a.hasValue(3)) {
            this.mThumbTintList = a.getColorStateList(3);
            this.mHasThumbTint = true;
        }
        setTickMark(a.getDrawable(5));
        if (a.hasValue(7)) {
            this.mTickMarkBlendMode = Drawable.parseBlendMode(a.getInt(7, -1), this.mTickMarkBlendMode);
            this.mHasTickMarkBlendMode = true;
        }
        if (a.hasValue(6)) {
            this.mTickMarkTintList = a.getColorStateList(6);
            this.mHasTickMarkTint = true;
        }
        this.mSplitTrack = a.getBoolean(2, false);
        setThumbOffset(a.getDimensionPixelOffset(1, getThumbOffset()));
        boolean useDisabledAlpha = a.getBoolean(8, true);
        a.recycle();
        if (useDisabledAlpha) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Theme, 0, 0);
            this.mDisabledAlpha = ta.getFloat(3, 0.5f);
            ta.recycle();
        } else {
            this.mDisabledAlpha = 1.0f;
        }
        applyThumbTint();
        applyTickMarkTint();
        this.mScaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public void setThumb(Drawable thumb) {
        boolean needUpdate;
        Drawable drawable = this.mThumb;
        if (drawable == null || thumb == drawable) {
            needUpdate = false;
        } else {
            drawable.setCallback(null);
            needUpdate = true;
        }
        if (thumb != null) {
            thumb.setCallback(this);
            if (canResolveLayoutDirection()) {
                thumb.setLayoutDirection(getLayoutDirection());
            }
            this.mThumbOffset = thumb.getIntrinsicWidth() / 2;
            if (needUpdate && !(thumb.getIntrinsicWidth() == this.mThumb.getIntrinsicWidth() && thumb.getIntrinsicHeight() == this.mThumb.getIntrinsicHeight())) {
                requestLayout();
            }
        }
        this.mThumb = thumb;
        applyThumbTint();
        invalidate();
        if (needUpdate) {
            updateThumbAndTrackPos(getWidth(), getHeight());
            if (thumb != null && thumb.isStateful()) {
                thumb.setState(getDrawableState());
            }
        }
    }

    public Drawable getThumb() {
        return this.mThumb;
    }

    public void setThumbTintList(ColorStateList tint) {
        this.mThumbTintList = tint;
        this.mHasThumbTint = true;
        applyThumbTint();
    }

    public ColorStateList getThumbTintList() {
        return this.mThumbTintList;
    }

    public void setThumbTintMode(Mode tintMode) {
        BlendMode fromValue;
        if (tintMode != null) {
            fromValue = BlendMode.fromValue(tintMode.nativeInt);
        } else {
            fromValue = null;
        }
        setThumbTintBlendMode(fromValue);
    }

    public void setThumbTintBlendMode(BlendMode blendMode) {
        this.mThumbBlendMode = blendMode;
        this.mHasThumbBlendMode = true;
        applyThumbTint();
    }

    public Mode getThumbTintMode() {
        BlendMode blendMode = this.mThumbBlendMode;
        return blendMode != null ? BlendMode.blendModeToPorterDuffMode(blendMode) : null;
    }

    public BlendMode getThumbTintBlendMode() {
        return this.mThumbBlendMode;
    }

    private void applyThumbTint() {
        if (this.mThumb == null) {
            return;
        }
        if (this.mHasThumbTint || this.mHasThumbBlendMode) {
            this.mThumb = this.mThumb.mutate();
            if (this.mHasThumbTint) {
                this.mThumb.setTintList(this.mThumbTintList);
            }
            if (this.mHasThumbBlendMode) {
                this.mThumb.setTintBlendMode(this.mThumbBlendMode);
            }
            if (this.mThumb.isStateful()) {
                this.mThumb.setState(getDrawableState());
            }
        }
    }

    public int getThumbOffset() {
        return this.mThumbOffset;
    }

    public void setThumbOffset(int thumbOffset) {
        this.mThumbOffset = thumbOffset;
        invalidate();
    }

    public void setSplitTrack(boolean splitTrack) {
        this.mSplitTrack = splitTrack;
        invalidate();
    }

    public boolean getSplitTrack() {
        return this.mSplitTrack;
    }

    public void setTickMark(Drawable tickMark) {
        Drawable drawable = this.mTickMark;
        if (drawable != null) {
            drawable.setCallback(null);
        }
        this.mTickMark = tickMark;
        if (tickMark != null) {
            tickMark.setCallback(this);
            tickMark.setLayoutDirection(getLayoutDirection());
            if (tickMark.isStateful()) {
                tickMark.setState(getDrawableState());
            }
            applyTickMarkTint();
        }
        invalidate();
    }

    public Drawable getTickMark() {
        return this.mTickMark;
    }

    public void setTickMarkTintList(ColorStateList tint) {
        this.mTickMarkTintList = tint;
        this.mHasTickMarkTint = true;
        applyTickMarkTint();
    }

    public ColorStateList getTickMarkTintList() {
        return this.mTickMarkTintList;
    }

    public void setTickMarkTintMode(Mode tintMode) {
        setTickMarkTintBlendMode(tintMode != null ? BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    public void setTickMarkTintBlendMode(BlendMode blendMode) {
        this.mTickMarkBlendMode = blendMode;
        this.mHasTickMarkBlendMode = true;
        applyTickMarkTint();
    }

    public Mode getTickMarkTintMode() {
        BlendMode blendMode = this.mTickMarkBlendMode;
        return blendMode != null ? BlendMode.blendModeToPorterDuffMode(blendMode) : null;
    }

    public BlendMode getTickMarkTintBlendMode() {
        return this.mTickMarkBlendMode;
    }

    private void applyTickMarkTint() {
        if (this.mTickMark == null) {
            return;
        }
        if (this.mHasTickMarkTint || this.mHasTickMarkBlendMode) {
            this.mTickMark = this.mTickMark.mutate();
            if (this.mHasTickMarkTint) {
                this.mTickMark.setTintList(this.mTickMarkTintList);
            }
            if (this.mHasTickMarkBlendMode) {
                this.mTickMark.setTintBlendMode(this.mTickMarkBlendMode);
            }
            if (this.mTickMark.isStateful()) {
                this.mTickMark.setState(getDrawableState());
            }
        }
    }

    public void setKeyProgressIncrement(int increment) {
        this.mKeyProgressIncrement = increment < 0 ? -increment : increment;
    }

    public int getKeyProgressIncrement() {
        return this.mKeyProgressIncrement;
    }

    public synchronized void setMin(int min) {
        super.setMin(min);
        int range = getMax() - getMin();
        if (this.mKeyProgressIncrement == 0 || range / this.mKeyProgressIncrement > 20) {
            setKeyProgressIncrement(Math.max(1, Math.round(((float) range) / 20.0f)));
        }
    }

    public synchronized void setMax(int max) {
        super.setMax(max);
        int range = getMax() - getMin();
        if (this.mKeyProgressIncrement == 0 || range / this.mKeyProgressIncrement > 20) {
            setKeyProgressIncrement(Math.max(1, Math.round(((float) range) / 20.0f)));
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean verifyDrawable(Drawable who) {
        return who == this.mThumb || who == this.mTickMark || super.verifyDrawable(who);
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mThumb;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        drawable = this.mTickMark;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable != null && this.mDisabledAlpha < 1.0f) {
            progressDrawable.setAlpha(isEnabled() ? 255 : (int) (this.mDisabledAlpha * 255.0f));
        }
        Drawable thumb = this.mThumb;
        if (thumb != null && thumb.isStateful() && thumb.setState(getDrawableState())) {
            invalidateDrawable(thumb);
        }
        Drawable tickMark = this.mTickMark;
        if (tickMark != null && tickMark.isStateful() && tickMark.setState(getDrawableState())) {
            invalidateDrawable(tickMark);
        }
    }

    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        Drawable drawable = this.mThumb;
        if (drawable != null) {
            drawable.setHotspot(x, y);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onVisualProgressChanged(int id, float scale) {
        super.onVisualProgressChanged(id, scale);
        if (id == 16908301) {
            Drawable thumb = this.mThumb;
            if (thumb != null) {
                setThumbPos(getWidth(), thumb, scale, Integer.MIN_VALUE);
                invalidate();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateThumbAndTrackPos(w, h);
    }

    private void updateThumbAndTrackPos(int w, int h) {
        int thumbOffset;
        int trackOffset;
        int paddedHeight = (h - this.mPaddingTop) - this.mPaddingBottom;
        Drawable track = getCurrentDrawable();
        Drawable thumb = this.mThumb;
        int trackHeight = Math.min(this.mMaxHeight, paddedHeight);
        int thumbHeight = thumb == null ? 0 : thumb.getIntrinsicHeight();
        if (thumbHeight > trackHeight) {
            thumbOffset = (paddedHeight - thumbHeight) / 2;
            trackOffset = ((thumbHeight - trackHeight) / 2) + thumbOffset;
        } else {
            thumbOffset = (paddedHeight - trackHeight) / 2;
            trackOffset = thumbOffset;
            thumbOffset += (trackHeight - thumbHeight) / 2;
        }
        if (track != null) {
            track.setBounds(0, trackOffset, (w - this.mPaddingRight) - this.mPaddingLeft, trackOffset + trackHeight);
        }
        if (thumb != null) {
            setThumbPos(w, thumb, getScale(), thumbOffset);
        }
    }

    private float getScale() {
        int min = getMin();
        int range = getMax() - min;
        return range > 0 ? ((float) (getProgress() - min)) / ((float) range) : 0.0f;
    }

    private void setThumbPos(int w, Drawable thumb, float scale, int offset) {
        int bottom;
        int top;
        int i = offset;
        int available = (w - this.mPaddingLeft) - this.mPaddingRight;
        int thumbWidth = thumb.getIntrinsicWidth();
        int thumbHeight = thumb.getIntrinsicHeight();
        available = (available - thumbWidth) + (this.mThumbOffset * 2);
        int thumbPos = (int) ((((float) available) * scale) + 1056964608);
        if (i == Integer.MIN_VALUE) {
            bottom = thumb.getBounds();
            top = bottom.top;
            bottom = bottom.bottom;
        } else {
            top = offset;
            bottom = i + thumbHeight;
        }
        int left = (isLayoutRtl() && this.mMirrorForRtl) ? available - thumbPos : thumbPos;
        int right = left + thumbWidth;
        Drawable background = getBackground();
        if (background != null) {
            int offsetX = this.mPaddingLeft - this.mThumbOffset;
            int offsetY = this.mPaddingTop;
            background.setHotspotBounds(left + offsetX, top + offsetY, right + offsetX, bottom + offsetY);
        }
        thumb.setBounds(left, top, right, bottom);
        updateGestureExclusionRects();
    }

    public void setSystemGestureExclusionRects(List<Rect> rects) {
        Preconditions.checkNotNull(rects, "rects must not be null");
        this.mUserGestureExclusionRects = rects;
        updateGestureExclusionRects();
    }

    private void updateGestureExclusionRects() {
        Drawable thumb = this.mThumb;
        if (thumb == null) {
            super.setSystemGestureExclusionRects(this.mUserGestureExclusionRects);
            return;
        }
        this.mGestureExclusionRects.clear();
        thumb.copyBounds(this.mThumbRect);
        this.mGestureExclusionRects.add(this.mThumbRect);
        this.mGestureExclusionRects.addAll(this.mUserGestureExclusionRects);
        super.setSystemGestureExclusionRects(this.mGestureExclusionRects);
    }

    public void onResolveDrawables(int layoutDirection) {
        super.onResolveDrawables(layoutDirection);
        Drawable drawable = this.mThumb;
        if (drawable != null) {
            drawable.setLayoutDirection(layoutDirection);
        }
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawThumb(canvas);
    }

    /* Access modifiers changed, original: 0000 */
    public void drawTrack(Canvas canvas) {
        Drawable thumbDrawable = this.mThumb;
        if (thumbDrawable == null || !this.mSplitTrack) {
            super.drawTrack(canvas);
            drawTickMarks(canvas);
            return;
        }
        Insets insets = thumbDrawable.getOpticalInsets();
        Rect tempRect = this.mTempRect;
        thumbDrawable.copyBounds(tempRect);
        tempRect.offset(this.mPaddingLeft - this.mThumbOffset, this.mPaddingTop);
        tempRect.left += insets.left;
        tempRect.right -= insets.right;
        int saveCount = canvas.save();
        canvas.clipRect(tempRect, Op.DIFFERENCE);
        super.drawTrack(canvas);
        drawTickMarks(canvas);
        canvas.restoreToCount(saveCount);
    }

    /* Access modifiers changed, original: protected */
    public void drawTickMarks(Canvas canvas) {
        if (this.mTickMark != null) {
            int count = getMax() - getMin();
            int halfH = 1;
            if (count > 1) {
                int w = this.mTickMark.getIntrinsicWidth();
                int h = this.mTickMark.getIntrinsicHeight();
                int halfW = w >= 0 ? w / 2 : 1;
                if (h >= 0) {
                    halfH = h / 2;
                }
                this.mTickMark.setBounds(-halfW, -halfH, halfW, halfH);
                float spacing = ((float) ((getWidth() - this.mPaddingLeft) - this.mPaddingRight)) / ((float) count);
                int saveCount = canvas.save();
                canvas.translate((float) this.mPaddingLeft, (float) (getHeight() / 2));
                for (int i = 0; i <= count; i++) {
                    this.mTickMark.draw(canvas);
                    canvas.translate(spacing, 0.0f);
                }
                canvas.restoreToCount(saveCount);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void drawThumb(Canvas canvas) {
        if (this.mThumb != null) {
            int saveCount = canvas.save();
            canvas.translate((float) (this.mPaddingLeft - this.mThumbOffset), (float) this.mPaddingTop);
            this.mThumb.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    /* Access modifiers changed, original: protected|declared_synchronized */
    public synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getCurrentDrawable();
        int thumbHeight = this.mThumb == null ? 0 : this.mThumb.getIntrinsicHeight();
        int dw = 0;
        int dh = 0;
        if (d != null) {
            dw = Math.max(this.mMinWidth, Math.min(this.mMaxWidth, d.getIntrinsicWidth()));
            dh = Math.max(thumbHeight, Math.max(this.mMinHeight, Math.min(this.mMaxHeight, d.getIntrinsicHeight())));
        }
        setMeasuredDimension(View.resolveSizeAndState(dw + (this.mPaddingLeft + this.mPaddingRight), widthMeasureSpec, 0), View.resolveSizeAndState(dh + (this.mPaddingTop + this.mPaddingBottom), heightMeasureSpec, 0));
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (Extension.get().getExtension() != null) {
            return ((Interface) Extension.get().getExtension().asInterface()).onTouchEvent(this, event);
        }
        return originalOnTouchEvent(event);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean originalOnTouchEvent(MotionEvent event) {
        if (!this.mIsUserSeekable || !isEnabled()) {
            return false;
        }
        int action = event.getAction();
        if (action != 0) {
            if (action == 1) {
                if (this.mIsDragging) {
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                    setPressed(false);
                } else {
                    onStartTrackingTouch();
                    trackTouchEvent(event);
                    onStopTrackingTouch();
                }
                invalidate();
            } else if (action != 2) {
                if (action == 3) {
                    if (this.mIsDragging) {
                        onStopTrackingTouch();
                        setPressed(false);
                    }
                    invalidate();
                }
            } else if (this.mIsDragging) {
                trackTouchEvent(event);
            } else if (Math.abs(event.getX() - this.mTouchDownX) > ((float) this.mScaledTouchSlop)) {
                startDrag(event);
            }
        } else if (isInScrollingContainer()) {
            this.mTouchDownX = event.getX();
        } else {
            startDrag(event);
        }
        return true;
    }

    private void startDrag(MotionEvent event) {
        setPressed(true);
        Drawable drawable = this.mThumb;
        if (drawable != null) {
            invalidate(drawable.getBounds());
        }
        onStartTrackingTouch();
        trackTouchEvent(event);
        attemptClaimDrag();
    }

    private void setHotspot(float x, float y) {
        Drawable bg = getBackground();
        if (bg != null) {
            bg.setHotspot(x, y);
        }
    }

    @UnsupportedAppUsage
    private void trackTouchEvent(MotionEvent event) {
        float scale;
        int x = Math.round(event.getX());
        int y = Math.round(event.getY());
        int width = getWidth();
        int availableWidth = (width - this.mPaddingLeft) - this.mPaddingRight;
        float progress = 0.0f;
        if (isLayoutRtl() && this.mMirrorForRtl) {
            if (x > width - this.mPaddingRight) {
                scale = 0.0f;
            } else if (x < this.mPaddingLeft) {
                scale = 1.0f;
            } else {
                scale = ((float) ((availableWidth - x) + this.mPaddingLeft)) / ((float) availableWidth);
                progress = this.mTouchProgressOffset;
            }
        } else if (x < this.mPaddingLeft) {
            scale = 0.0f;
        } else if (x > width - this.mPaddingRight) {
            scale = 1.0f;
        } else {
            scale = ((float) (x - this.mPaddingLeft)) / ((float) availableWidth);
            progress = this.mTouchProgressOffset;
        }
        progress += (((float) (getMax() - getMin())) * scale) + ((float) getMin());
        setHotspot((float) x, (float) y);
        setProgressInternal(Math.round(progress), true, false);
    }

    private void attemptClaimDrag() {
        if (this.mParent != null) {
            this.mParent.requestDisallowInterceptTouchEvent(true);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onStartTrackingTouch() {
        this.mIsDragging = true;
    }

    /* Access modifiers changed, original: 0000 */
    public void onStopTrackingTouch() {
        this.mIsDragging = false;
    }

    /* Access modifiers changed, original: 0000 */
    public void onKeyChange() {
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x0026  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0024  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0034  */
    /* JADX WARNING: Missing block: B:11:0x001a, code skipped:
            if (r4 != 81) goto L_0x0038;
     */
    public boolean onKeyDown(int r4, android.view.KeyEvent r5) {
        /*
        r3 = this;
        r0 = r3.isEnabled();
        if (r0 == 0) goto L_0x0038;
    L_0x0006:
        r0 = r3.mKeyProgressIncrement;
        r1 = 21;
        if (r4 == r1) goto L_0x001d;
    L_0x000c:
        r1 = 22;
        if (r4 == r1) goto L_0x001e;
    L_0x0010:
        r1 = 69;
        if (r4 == r1) goto L_0x001d;
    L_0x0014:
        r1 = 70;
        if (r4 == r1) goto L_0x001e;
    L_0x0018:
        r1 = 81;
        if (r4 == r1) goto L_0x001e;
    L_0x001c:
        goto L_0x0038;
    L_0x001d:
        r0 = -r0;
    L_0x001e:
        r1 = r3.isLayoutRtl();
        if (r1 == 0) goto L_0x0026;
    L_0x0024:
        r1 = -r0;
        goto L_0x0027;
    L_0x0026:
        r1 = r0;
    L_0x0027:
        r0 = r1;
        r1 = r3.getProgress();
        r1 = r1 + r0;
        r2 = 1;
        r1 = r3.setProgressInternal(r1, r2, r2);
        if (r1 == 0) goto L_0x0038;
    L_0x0034:
        r3.onKeyChange();
        return r2;
    L_0x0038:
        r0 = super.onKeyDown(r4, r5);
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.AbsSeekBar.onKeyDown(int, android.view.KeyEvent):boolean");
    }

    public CharSequence getAccessibilityClassName() {
        return AbsSeekBar.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (isEnabled()) {
            int progress = getProgress();
            if (progress > getMin()) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_BACKWARD);
            }
            if (progress < getMax()) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_FORWARD);
            }
        }
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        if (!isEnabled()) {
            return false;
        }
        if (action != 4096 && action != 8192) {
            if (action == 16908349 && canUserSetProgress() && arguments != null) {
                float value = AccessibilityNodeInfo.ACTION_ARGUMENT_PROGRESS_VALUE;
                if (arguments.containsKey(value)) {
                    return setProgressInternal((int) arguments.getFloat(value), true, true);
                }
            }
            return false;
        } else if (!canUserSetProgress()) {
            return false;
        } else {
            int increment = Math.max(1, Math.round(((float) (getMax() - getMin())) / 20.0f));
            if (action == 8192) {
                increment = -increment;
            }
            if (!setProgressInternal(getProgress() + increment, true, true)) {
                return false;
            }
            onKeyChange();
            return true;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean canUserSetProgress() {
        return !isIndeterminate() && isEnabled();
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        Drawable thumb = this.mThumb;
        if (thumb != null) {
            setThumbPos(getWidth(), thumb, getScale(), Integer.MIN_VALUE);
            invalidate();
        }
    }

    static {
        Extension.get().bindOriginal(new Interface() {
            public boolean onTouchEvent(AbsSeekBar absSeekBar, MotionEvent motionEvent) {
                return absSeekBar.originalOnTouchEvent(motionEvent);
            }
        });
    }
}
