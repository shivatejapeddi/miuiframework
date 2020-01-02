package android.widget;

import android.R;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.AnimationUtils;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import java.util.List;

public class HorizontalScrollView extends FrameLayout {
    private static final int ANIMATED_SCROLL_GAP = 250;
    private static final int INVALID_POINTER = -1;
    private static final float MAX_SCROLL_FACTOR = 0.5f;
    private static final String TAG = "HorizontalScrollView";
    private int mActivePointerId;
    @UnsupportedAppUsage
    private View mChildToScrollTo;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124053130)
    private EdgeEffect mEdgeGlowLeft;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124052619)
    private EdgeEffect mEdgeGlowRight;
    @ExportedProperty(category = "layout")
    private boolean mFillViewport;
    private float mHorizontalScrollFactor;
    @UnsupportedAppUsage
    private boolean mIsBeingDragged;
    private boolean mIsLayoutDirty;
    @UnsupportedAppUsage
    private int mLastMotionX;
    private long mLastScroll;
    private int mMaximumVelocity;
    private int mMinimumVelocity;
    @UnsupportedAppUsage
    private int mOverflingDistance;
    @UnsupportedAppUsage
    private int mOverscrollDistance;
    private SavedState mSavedState;
    @UnsupportedAppUsage
    private OverScroller mScroller;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private int mTouchSlop;
    @UnsupportedAppUsage
    private VelocityTracker mVelocityTracker;

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<HorizontalScrollView> {
        private int mFillViewportId;
        private boolean mPropertiesMapped = false;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mFillViewportId = propertyMapper.mapBoolean("fillViewport", 16843130);
            this.mPropertiesMapped = true;
        }

        public void readProperties(HorizontalScrollView node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readBoolean(this.mFillViewportId, node.isFillViewport());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        public int scrollOffsetFromStart;

        SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            this.scrollOffsetFromStart = source.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.scrollOffsetFromStart);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("HorizontalScrollView.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" scrollPosition=");
            stringBuilder.append(this.scrollOffsetFromStart);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public HorizontalScrollView(Context context) {
        this(context, null);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 16843603);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public HorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTempRect = new Rect();
        this.mEdgeGlowLeft = new EdgeEffect(getContext());
        this.mEdgeGlowRight = new EdgeEffect(getContext());
        this.mIsLayoutDirty = true;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        this.mSmoothScrollingEnabled = true;
        this.mActivePointerId = -1;
        initScrollView();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.HorizontalScrollView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.HorizontalScrollView, attrs, a, defStyleAttr, defStyleRes);
        setFillViewport(a.getBoolean(0, false));
        a.recycle();
        if (context.getResources().getConfiguration().uiMode == 6) {
            setRevealOnFocusHint(false);
        }
    }

    /* Access modifiers changed, original: protected */
    public float getLeftFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getHorizontalFadingEdgeLength();
        if (this.mScrollX < length) {
            return ((float) this.mScrollX) / ((float) length);
        }
        return 1.0f;
    }

    /* Access modifiers changed, original: protected */
    public float getRightFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getHorizontalFadingEdgeLength();
        int span = (getChildAt(0).getRight() - this.mScrollX) - (getWidth() - this.mPaddingRight);
        if (span < length) {
            return ((float) span) / ((float) length);
        }
        return 1.0f;
    }

    public void setEdgeEffectColor(int color) {
        setLeftEdgeEffectColor(color);
        setRightEdgeEffectColor(color);
    }

    public void setRightEdgeEffectColor(int color) {
        this.mEdgeGlowRight.setColor(color);
    }

    public void setLeftEdgeEffectColor(int color) {
        this.mEdgeGlowLeft.setColor(color);
    }

    public int getLeftEdgeEffectColor() {
        return this.mEdgeGlowLeft.getColor();
    }

    public int getRightEdgeEffectColor() {
        return this.mEdgeGlowRight.getColor();
    }

    public int getMaxScrollAmount() {
        return (int) (((float) (this.mRight - this.mLeft)) * 0.5f);
    }

    private void initScrollView() {
        this.mScroller = new OverScroller(getContext());
        setFocusable(true);
        setDescendantFocusability(262144);
        setWillNotDraw(false);
        ViewConfiguration configuration = ViewConfiguration.get(this.mContext);
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mOverscrollDistance = configuration.getScaledOverscrollDistance();
        this.mOverflingDistance = configuration.getScaledOverflingDistance();
        this.mHorizontalScrollFactor = configuration.getScaledHorizontalScrollFactor();
    }

    public void addView(View child) {
        if (getChildCount() <= 0) {
            super.addView(child);
            return;
        }
        throw new IllegalStateException("HorizontalScrollView can host only one direct child");
    }

    public void addView(View child, int index) {
        if (getChildCount() <= 0) {
            super.addView(child, index);
            return;
        }
        throw new IllegalStateException("HorizontalScrollView can host only one direct child");
    }

    public void addView(View child, LayoutParams params) {
        if (getChildCount() <= 0) {
            super.addView(child, params);
            return;
        }
        throw new IllegalStateException("HorizontalScrollView can host only one direct child");
    }

    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() <= 0) {
            super.addView(child, index, params);
            return;
        }
        throw new IllegalStateException("HorizontalScrollView can host only one direct child");
    }

    private boolean canScroll() {
        boolean z = false;
        View child = getChildAt(0);
        if (child == null) {
            return false;
        }
        if (getWidth() < (this.mPaddingLeft + child.getWidth()) + this.mPaddingRight) {
            z = true;
        }
        return z;
    }

    public boolean isFillViewport() {
        return this.mFillViewport;
    }

    public void setFillViewport(boolean fillViewport) {
        if (fillViewport != this.mFillViewport) {
            this.mFillViewport = fillViewport;
            requestLayout();
        }
    }

    public boolean isSmoothScrollingEnabled() {
        return this.mSmoothScrollingEnabled;
    }

    public void setSmoothScrollingEnabled(boolean smoothScrollingEnabled) {
        this.mSmoothScrollingEnabled = smoothScrollingEnabled;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mFillViewport && MeasureSpec.getMode(widthMeasureSpec) != 0 && getChildCount() > 0) {
            int widthPadding;
            int heightPadding;
            View child = getChildAt(null);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child.getLayoutParams();
            if (getContext().getApplicationInfo().targetSdkVersion >= 23) {
                widthPadding = ((this.mPaddingLeft + this.mPaddingRight) + lp.leftMargin) + lp.rightMargin;
                heightPadding = ((this.mPaddingTop + this.mPaddingBottom) + lp.topMargin) + lp.bottomMargin;
            } else {
                widthPadding = this.mPaddingLeft + this.mPaddingRight;
                heightPadding = this.mPaddingTop + this.mPaddingBottom;
            }
            int desiredWidth = getMeasuredWidth() - widthPadding;
            if (child.getMeasuredWidth() < desiredWidth) {
                child.measure(MeasureSpec.makeMeasureSpec(desiredWidth, 1073741824), ViewGroup.getChildMeasureSpec(heightMeasureSpec, heightPadding, lp.height));
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        this.mTempRect.setEmpty();
        int i = 66;
        if (canScroll()) {
            boolean handled = false;
            if (event.getAction() == 0) {
                int keyCode = event.getKeyCode();
                if (keyCode == 21) {
                    handled = !event.isAltPressed() ? arrowScroll(17) : fullScroll(17);
                } else if (keyCode == 22) {
                    handled = !event.isAltPressed() ? arrowScroll(66) : fullScroll(66);
                } else if (keyCode == 62) {
                    if (event.isShiftPressed()) {
                        i = 17;
                    }
                    pageScroll(i);
                }
            }
            return handled;
        }
        boolean z = false;
        if (!isFocused()) {
            return false;
        }
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, 66);
        if (!(nextFocused == null || nextFocused == this || !nextFocused.requestFocus(66))) {
            z = true;
        }
        return z;
    }

    private boolean inChild(int x, int y) {
        boolean z = false;
        if (getChildCount() <= 0) {
            return false;
        }
        int scrollX = this.mScrollX;
        View child = getChildAt(0);
        if (y >= child.getTop() && y < child.getBottom() && x >= child.getLeft() - scrollX && x < child.getRight() - scrollX) {
            z = true;
        }
        return z;
    }

    private void initOrResetVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        } else {
            velocityTracker.clear();
        }
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
    }

    @UnsupportedAppUsage
    private void recycleVelocityTracker() {
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (disallowIntercept) {
            recycleVelocityTracker();
        }
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if ((action == 2 && this.mIsBeingDragged) || super.onInterceptTouchEvent(ev)) {
            return true;
        }
        int i = action & 255;
        int activePointerId;
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    activePointerId = this.mActivePointerId;
                    if (activePointerId != -1) {
                        i = ev.findPointerIndex(activePointerId);
                        if (i == -1) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Invalid pointerId=");
                            stringBuilder.append(activePointerId);
                            stringBuilder.append(" in onInterceptTouchEvent");
                            Log.e(TAG, stringBuilder.toString());
                        } else {
                            int x = (int) ev.getX(i);
                            if (Math.abs(x - this.mLastMotionX) > this.mTouchSlop) {
                                this.mIsBeingDragged = true;
                                this.mLastMotionX = x;
                                initVelocityTrackerIfNotExists();
                                this.mVelocityTracker.addMovement(ev);
                                if (this.mParent != null) {
                                    this.mParent.requestDisallowInterceptTouchEvent(true);
                                }
                            }
                        }
                    }
                } else if (i != 3) {
                    if (i == 5) {
                        activePointerId = ev.getActionIndex();
                        this.mLastMotionX = (int) ev.getX(activePointerId);
                        this.mActivePointerId = ev.getPointerId(activePointerId);
                    } else if (i == 6) {
                        onSecondaryPointerUp(ev);
                        this.mLastMotionX = (int) ev.getX(ev.findPointerIndex(this.mActivePointerId));
                    }
                }
            }
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, getScrollRange(), 0, 0)) {
                postInvalidateOnAnimation();
            }
        } else {
            activePointerId = (int) ev.getX();
            if (inChild(activePointerId, (int) ev.getY())) {
                this.mLastMotionX = activePointerId;
                this.mActivePointerId = ev.getPointerId(0);
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(ev);
                this.mIsBeingDragged = 1 ^ this.mScroller.isFinished();
            } else {
                this.mIsBeingDragged = false;
                recycleVelocityTracker();
            }
        }
        return this.mIsBeingDragged;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        MotionEvent motionEvent = ev;
        initVelocityTrackerIfNotExists();
        this.mVelocityTracker.addMovement(motionEvent);
        int action = ev.getAction();
        int i = action & 255;
        boolean z = false;
        int i2;
        int i3;
        if (i == 0) {
            if (getChildCount() == 0) {
                return false;
            }
            i = this.mScroller.isFinished() ^ 1;
            this.mIsBeingDragged = i;
            if (i != 0) {
                ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
            }
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
            }
            this.mLastMotionX = (int) ev.getX();
            this.mActivePointerId = motionEvent.getPointerId(0);
        } else if (i == 1) {
            if (this.mIsBeingDragged) {
                VelocityTracker velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                int initialVelocity = (int) velocityTracker.getXVelocity(this.mActivePointerId);
                if (getChildCount() > 0) {
                    if (Math.abs(initialVelocity) > this.mMinimumVelocity) {
                        fling(-initialVelocity);
                    } else {
                        OverScroller overScroller = this.mScroller;
                        i2 = this.mScrollX;
                        if (overScroller.springBack(i2, this.mScrollY, 0, getScrollRange(), 0, 0)) {
                            postInvalidateOnAnimation();
                        }
                    }
                }
                this.mActivePointerId = -1;
                this.mIsBeingDragged = false;
                recycleVelocityTracker();
                if (shouldDisplayEdgeEffects()) {
                    this.mEdgeGlowLeft.onRelease();
                    this.mEdgeGlowRight.onRelease();
                }
            }
        } else if (i == 2) {
            int activePointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
            if (activePointerIndex == -1) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid pointerId=");
                stringBuilder.append(this.mActivePointerId);
                stringBuilder.append(" in onTouchEvent");
                Log.e(TAG, stringBuilder.toString());
                i3 = action;
            } else {
                int deltaX;
                int x = (int) motionEvent.getX(activePointerIndex);
                i = this.mLastMotionX - x;
                if (this.mIsBeingDragged || Math.abs(i) <= this.mTouchSlop) {
                    deltaX = i;
                } else {
                    ViewParent parent2 = getParent();
                    if (parent2 != null) {
                        parent2.requestDisallowInterceptTouchEvent(true);
                    }
                    this.mIsBeingDragged = true;
                    if (i > 0) {
                        deltaX = i - this.mTouchSlop;
                    } else {
                        deltaX = i + this.mTouchSlop;
                    }
                }
                if (this.mIsBeingDragged) {
                    this.mLastMotionX = x;
                    int oldX = this.mScrollX;
                    int oldY = this.mScrollY;
                    int range = getScrollRange();
                    i2 = getOverScrollMode();
                    if (i2 == 0 || (i2 == 1 && range > 0)) {
                        z = true;
                    }
                    boolean canOverscroll = z;
                    int range2 = range;
                    int oldX2 = oldX;
                    action = deltaX;
                    if (overScrollBy(deltaX, 0, this.mScrollX, 0, range, 0, this.mOverscrollDistance, 0, true)) {
                        this.mVelocityTracker.clear();
                    }
                    if (canOverscroll) {
                        oldX = oldX2 + action;
                        if (oldX < 0) {
                            this.mEdgeGlowLeft.onPull(((float) action) / ((float) getWidth()), 1.0f - (motionEvent.getY(activePointerIndex) / ((float) getHeight())));
                            if (!this.mEdgeGlowRight.isFinished()) {
                                this.mEdgeGlowRight.onRelease();
                            }
                        } else if (oldX > range2) {
                            this.mEdgeGlowRight.onPull(((float) action) / ((float) getWidth()), motionEvent.getY(activePointerIndex) / ((float) getHeight()));
                            if (!this.mEdgeGlowLeft.isFinished()) {
                                this.mEdgeGlowLeft.onRelease();
                            }
                        }
                        if (shouldDisplayEdgeEffects() && !(this.mEdgeGlowLeft.isFinished() && this.mEdgeGlowRight.isFinished())) {
                            postInvalidateOnAnimation();
                        }
                    }
                } else {
                    action = deltaX;
                }
            }
        } else if (i != 3) {
            if (i != 6) {
                i3 = action;
            } else {
                onSecondaryPointerUp(ev);
                i3 = action;
            }
        } else if (!this.mIsBeingDragged || getChildCount() <= 0) {
            i3 = action;
        } else {
            if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, getScrollRange(), 0, 0)) {
                postInvalidateOnAnimation();
            }
            this.mActivePointerId = -1;
            this.mIsBeingDragged = false;
            recycleVelocityTracker();
            if (shouldDisplayEdgeEffects()) {
                this.mEdgeGlowLeft.onRelease();
                this.mEdgeGlowRight.onRelease();
                i3 = action;
            } else {
                i3 = action;
            }
        }
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & 65280) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionX = (int) ev.getX(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if (event.getAction() == 8 && !this.mIsBeingDragged) {
            float axisValue;
            if (event.isFromSource(2)) {
                if ((event.getMetaState() & 1) != 0) {
                    axisValue = -event.getAxisValue(9);
                } else {
                    axisValue = event.getAxisValue(10);
                }
            } else if (event.isFromSource(4194304)) {
                axisValue = event.getAxisValue(26);
            } else {
                axisValue = 0.0f;
            }
            int delta = Math.round(this.mHorizontalScrollFactor * axisValue);
            if (delta != 0) {
                int range = getScrollRange();
                int oldScrollX = this.mScrollX;
                int newScrollX = oldScrollX + delta;
                if (newScrollX < 0) {
                    newScrollX = 0;
                } else if (newScrollX > range) {
                    newScrollX = range;
                }
                if (newScrollX != oldScrollX) {
                    super.scrollTo(newScrollX, this.mScrollY);
                    return true;
                }
            }
        }
        return super.onGenericMotionEvent(event);
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        if (this.mScroller.isFinished()) {
            super.scrollTo(scrollX, scrollY);
        } else {
            int oldX = this.mScrollX;
            int oldY = this.mScrollY;
            this.mScrollX = scrollX;
            this.mScrollY = scrollY;
            invalidateParentIfNeeded();
            onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
            if (clampedX) {
                this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, getScrollRange(), 0, 0);
            }
        }
        awakenScrollBars();
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        int targetScrollX;
        if (action != 4096) {
            if (action == 8192 || action == 16908345) {
                if (!isEnabled()) {
                    return false;
                }
                targetScrollX = Math.max(0, this.mScrollX - ((getWidth() - this.mPaddingLeft) - this.mPaddingRight));
                if (targetScrollX == this.mScrollX) {
                    return false;
                }
                smoothScrollTo(targetScrollX, 0);
                return true;
            } else if (action != 16908347) {
                return false;
            }
        }
        if (!isEnabled()) {
            return false;
        }
        targetScrollX = Math.min(this.mScrollX + ((getWidth() - this.mPaddingLeft) - this.mPaddingRight), getScrollRange());
        if (targetScrollX == this.mScrollX) {
            return false;
        }
        smoothScrollTo(targetScrollX, 0);
        return true;
    }

    public CharSequence getAccessibilityClassName() {
        return HorizontalScrollView.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        int scrollRange = getScrollRange();
        if (scrollRange > 0) {
            info.setScrollable(true);
            if (isEnabled() && this.mScrollX > 0) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_BACKWARD);
                info.addAction(AccessibilityAction.ACTION_SCROLL_LEFT);
            }
            if (isEnabled() && this.mScrollX < scrollRange) {
                info.addAction(AccessibilityAction.ACTION_SCROLL_FORWARD);
                info.addAction(AccessibilityAction.ACTION_SCROLL_RIGHT);
            }
        }
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setScrollable(getScrollRange() > 0);
        event.setScrollX(this.mScrollX);
        event.setScrollY(this.mScrollY);
        event.setMaxScrollX(getScrollRange());
        event.setMaxScrollY(this.mScrollY);
    }

    private int getScrollRange() {
        if (getChildCount() > 0) {
            return Math.max(0, getChildAt(0).getWidth() - ((getWidth() - this.mPaddingLeft) - this.mPaddingRight));
        }
        return 0;
    }

    private View findFocusableViewInMyBounds(boolean leftFocus, int left, View preferredFocusable) {
        int fadingEdgeLength = getHorizontalFadingEdgeLength() / 2;
        int leftWithoutFadingEdge = left + fadingEdgeLength;
        int rightWithoutFadingEdge = (getWidth() + left) - fadingEdgeLength;
        if (preferredFocusable == null || preferredFocusable.getLeft() >= rightWithoutFadingEdge || preferredFocusable.getRight() <= leftWithoutFadingEdge) {
            return findFocusableViewInBounds(leftFocus, leftWithoutFadingEdge, rightWithoutFadingEdge);
        }
        return preferredFocusable;
    }

    private View findFocusableViewInBounds(boolean leftFocus, int left, int right) {
        List<View> focusables = getFocusables(2);
        View focusCandidate = null;
        boolean foundFullyContainedFocusable = false;
        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            View view = (View) focusables.get(i);
            int viewLeft = view.getLeft();
            int viewRight = view.getRight();
            if (left < viewRight && viewLeft < right) {
                boolean viewIsCloserToBoundary = false;
                boolean viewIsFullyContained = left < viewLeft && viewRight < right;
                if (focusCandidate == null) {
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    if ((leftFocus && viewLeft < focusCandidate.getLeft()) || (!leftFocus && viewRight > focusCandidate.getRight())) {
                        viewIsCloserToBoundary = true;
                    }
                    if (foundFullyContainedFocusable) {
                        if (viewIsFullyContained && viewIsCloserToBoundary) {
                            focusCandidate = view;
                        }
                    } else if (viewIsFullyContained) {
                        focusCandidate = view;
                        foundFullyContainedFocusable = true;
                    } else if (viewIsCloserToBoundary) {
                        focusCandidate = view;
                    }
                }
            }
        }
        return focusCandidate;
    }

    public boolean pageScroll(int direction) {
        boolean right = direction == 66;
        int width = getWidth();
        if (right) {
            this.mTempRect.left = getScrollX() + width;
            if (getChildCount() > 0) {
                View view = getChildAt(0);
                if (this.mTempRect.left + width > view.getRight()) {
                    this.mTempRect.left = view.getRight() - width;
                }
            }
        } else {
            this.mTempRect.left = getScrollX() - width;
            if (this.mTempRect.left < 0) {
                this.mTempRect.left = 0;
            }
        }
        Rect rect = this.mTempRect;
        rect.right = rect.left + width;
        return scrollAndFocus(direction, this.mTempRect.left, this.mTempRect.right);
    }

    public boolean fullScroll(int direction) {
        boolean right = direction == 66;
        int width = getWidth();
        Rect rect = this.mTempRect;
        rect.left = 0;
        rect.right = width;
        if (right && getChildCount() > 0) {
            this.mTempRect.right = getChildAt(0).getRight();
            Rect rect2 = this.mTempRect;
            rect2.left = rect2.right - width;
        }
        return scrollAndFocus(direction, this.mTempRect.left, this.mTempRect.right);
    }

    private boolean scrollAndFocus(int direction, int left, int right) {
        boolean handled = true;
        int width = getWidth();
        int containerLeft = getScrollX();
        int containerRight = containerLeft + width;
        boolean goLeft = direction == 17;
        View newFocused = findFocusableViewInBounds(goLeft, left, right);
        if (newFocused == null) {
            newFocused = this;
        }
        if (left < containerLeft || right > containerRight) {
            doScrollX(goLeft ? left - containerLeft : right - containerRight);
        } else {
            handled = false;
        }
        if (newFocused != findFocus()) {
            newFocused.requestFocus(direction);
        }
        return handled;
    }

    public boolean arrowScroll(int direction) {
        int scrollDelta;
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, direction);
        int maxJump = getMaxScrollAmount();
        if (nextFocused == null || !isWithinDeltaOfScreen(nextFocused, maxJump)) {
            scrollDelta = maxJump;
            if (direction == 17 && getScrollX() < scrollDelta) {
                scrollDelta = getScrollX();
            } else if (direction == 66 && getChildCount() > 0) {
                int daRight = getChildAt(0).getRight();
                int screenRight = getScrollX() + getWidth();
                if (daRight - screenRight < maxJump) {
                    scrollDelta = daRight - screenRight;
                }
            }
            if (scrollDelta == 0) {
                return false;
            }
            doScrollX(direction == 66 ? scrollDelta : -scrollDelta);
        } else {
            nextFocused.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(nextFocused, this.mTempRect);
            doScrollX(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
            nextFocused.requestFocus(direction);
        }
        if (currentFocused != null && currentFocused.isFocused() && isOffScreen(currentFocused)) {
            scrollDelta = getDescendantFocusability();
            setDescendantFocusability(131072);
            requestFocus();
            setDescendantFocusability(scrollDelta);
        }
        return true;
    }

    private boolean isOffScreen(View descendant) {
        return isWithinDeltaOfScreen(descendant, 0) ^ 1;
    }

    private boolean isWithinDeltaOfScreen(View descendant, int delta) {
        descendant.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        return this.mTempRect.right + delta >= getScrollX() && this.mTempRect.left - delta <= getScrollX() + getWidth();
    }

    private void doScrollX(int delta) {
        if (delta == 0) {
            return;
        }
        if (this.mSmoothScrollingEnabled) {
            smoothScrollBy(delta, 0);
        } else {
            scrollBy(delta, 0);
        }
    }

    public final void smoothScrollBy(int dx, int dy) {
        if (getChildCount() != 0) {
            if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250) {
                int maxX = Math.max(0, getChildAt(0).getWidth() - ((getWidth() - this.mPaddingRight) - this.mPaddingLeft));
                int scrollX = this.mScrollX;
                this.mScroller.startScroll(scrollX, this.mScrollY, Math.max(0, Math.min(scrollX + dx, maxX)) - scrollX, 0);
                postInvalidateOnAnimation();
            } else {
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                }
                scrollBy(dx, dy);
            }
            this.mLastScroll = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public final void smoothScrollTo(int x, int y) {
        smoothScrollBy(x - this.mScrollX, y - this.mScrollY);
    }

    /* Access modifiers changed, original: protected */
    public int computeHorizontalScrollRange() {
        int contentWidth = (getWidth() - this.mPaddingLeft) - this.mPaddingRight;
        if (getChildCount() == 0) {
            return contentWidth;
        }
        int scrollRange = getChildAt(0).getRight();
        int scrollX = this.mScrollX;
        int overscrollRight = Math.max(0, scrollRange - contentWidth);
        if (scrollX < 0) {
            scrollRange -= scrollX;
        } else if (scrollX > overscrollRight) {
            scrollRange += scrollX - overscrollRight;
        }
        return scrollRange;
    }

    /* Access modifiers changed, original: protected */
    public int computeHorizontalScrollOffset() {
        return Math.max(0, super.computeHorizontalScrollOffset());
    }

    /* Access modifiers changed, original: protected */
    public void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        int horizontalPadding = this.mPaddingLeft + this.mPaddingRight;
        child.measure(MeasureSpec.makeSafeMeasureSpec(Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - horizontalPadding), 0), ViewGroup.getChildMeasureSpec(parentHeightMeasureSpec, this.mPaddingTop + this.mPaddingBottom, child.getLayoutParams().height));
    }

    /* Access modifiers changed, original: protected */
    public void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        child.measure(MeasureSpec.makeSafeMeasureSpec(Math.max(0, MeasureSpec.getSize(parentWidthMeasureSpec) - ((((this.mPaddingLeft + this.mPaddingRight) + lp.leftMargin) + lp.rightMargin) + widthUsed)), 0), ViewGroup.getChildMeasureSpec(parentHeightMeasureSpec, (((this.mPaddingTop + this.mPaddingBottom) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height));
    }

    public void computeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            int oldX = this.mScrollX;
            int oldY = this.mScrollY;
            int x = this.mScroller.getCurrX();
            int y = this.mScroller.getCurrY();
            if (!(oldX == x && oldY == y)) {
                int range = getScrollRange();
                int overscrollMode = getOverScrollMode();
                boolean z = true;
                if (overscrollMode != 0 && (overscrollMode != 1 || range <= 0)) {
                    z = false;
                }
                boolean canOverscroll = z;
                overScrollBy(x - oldX, y - oldY, oldX, oldY, range, 0, this.mOverflingDistance, 0, false);
                onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
                if (canOverscroll) {
                    if (x < 0 && oldX >= 0) {
                        this.mEdgeGlowLeft.onAbsorb((int) this.mScroller.getCurrVelocity());
                    } else if (x > range && oldX <= range) {
                        this.mEdgeGlowRight.onAbsorb((int) this.mScroller.getCurrVelocity());
                    }
                }
            }
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
            }
        }
    }

    private void scrollToChild(View child) {
        child.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(child, this.mTempRect);
        int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (scrollDelta != 0) {
            scrollBy(scrollDelta, 0);
        }
    }

    private boolean scrollToChildRect(Rect rect, boolean immediate) {
        int delta = computeScrollDeltaToGetChildRectOnScreen(rect);
        boolean scroll = delta != 0;
        if (scroll) {
            if (immediate) {
                scrollBy(delta, 0);
            } else {
                smoothScrollBy(delta, 0);
            }
        }
        return scroll;
    }

    /* Access modifiers changed, original: protected */
    public int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) {
            return 0;
        }
        int width = getWidth();
        int screenLeft = getScrollX();
        int screenRight = screenLeft + width;
        int fadingEdge = getHorizontalFadingEdgeLength();
        if (rect.left > 0) {
            screenLeft += fadingEdge;
        }
        if (rect.right < getChildAt(0).getWidth()) {
            screenRight -= fadingEdge;
        }
        int scrollXDelta = 0;
        if (rect.right > screenRight && rect.left > screenLeft) {
            if (rect.width() > width) {
                scrollXDelta = 0 + (rect.left - screenLeft);
            } else {
                scrollXDelta = 0 + (rect.right - screenRight);
            }
            scrollXDelta = Math.min(scrollXDelta, getChildAt(0).getRight() - screenRight);
        } else if (rect.left < screenLeft && rect.right < screenRight) {
            if (rect.width() > width) {
                scrollXDelta = 0 - (screenRight - rect.right);
            } else {
                scrollXDelta = 0 - (screenLeft - rect.left);
            }
            scrollXDelta = Math.max(scrollXDelta, -getScrollX());
        }
        return scrollXDelta;
    }

    public void requestChildFocus(View child, View focused) {
        if (focused != null && focused.getRevealOnFocusHint()) {
            if (this.mIsLayoutDirty) {
                this.mChildToScrollTo = focused;
            } else {
                scrollToChild(focused);
            }
        }
        super.requestChildFocus(child, focused);
    }

    /* Access modifiers changed, original: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        View nextFocus;
        if (direction == 2) {
            direction = 66;
        } else if (direction == 1) {
            direction = 17;
        }
        if (previouslyFocusedRect == null) {
            nextFocus = FocusFinder.getInstance().findNextFocus(this, null, direction);
        } else {
            nextFocus = FocusFinder.getInstance().findNextFocusFromRect(this, previouslyFocusedRect, direction);
        }
        if (nextFocus == null || isOffScreen(nextFocus)) {
            return false;
        }
        return nextFocus.requestFocus(direction, previouslyFocusedRect);
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
        return scrollToChildRect(rectangle, immediate);
    }

    public void requestLayout() {
        this.mIsLayoutDirty = true;
        super.requestLayout();
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int childWidth;
        int childMargins;
        if (getChildCount() > 0) {
            FrameLayout.LayoutParams childParams = (FrameLayout.LayoutParams) getChildAt(0).getLayoutParams();
            childWidth = getChildAt(0).getMeasuredWidth();
            childMargins = childParams.leftMargin + childParams.rightMargin;
        } else {
            childWidth = 0;
            childMargins = 0;
        }
        layoutChildren(l, t, r, b, childWidth > (((r - l) - getPaddingLeftWithForeground()) - getPaddingRightWithForeground()) - childMargins);
        this.mIsLayoutDirty = false;
        View view = this.mChildToScrollTo;
        if (view != null && isViewDescendantOf(view, this)) {
            scrollToChild(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!isLaidOut()) {
            int scrollRange = Math.max(0, childWidth - (((r - l) - this.mPaddingLeft) - this.mPaddingRight));
            if (this.mSavedState != null) {
                int i;
                if (isLayoutRtl()) {
                    i = scrollRange - this.mSavedState.scrollOffsetFromStart;
                } else {
                    i = this.mSavedState.scrollOffsetFromStart;
                }
                this.mScrollX = i;
                this.mSavedState = null;
            } else if (isLayoutRtl()) {
                this.mScrollX = scrollRange - this.mScrollX;
            }
            if (this.mScrollX > scrollRange) {
                this.mScrollX = scrollRange;
            } else if (this.mScrollX < 0) {
                this.mScrollX = 0;
            }
        }
        scrollTo(this.mScrollX, this.mScrollY);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:7:0x002b, code skipped:
            return;
     */
    public void onSizeChanged(int r4, int r5, int r6, int r7) {
        /*
        r3 = this;
        super.onSizeChanged(r4, r5, r6, r7);
        r0 = r3.findFocus();
        if (r0 == 0) goto L_0x002b;
    L_0x0009:
        if (r3 != r0) goto L_0x000c;
    L_0x000b:
        goto L_0x002b;
    L_0x000c:
        r1 = r3.mRight;
        r2 = r3.mLeft;
        r1 = r1 - r2;
        r2 = r3.isWithinDeltaOfScreen(r0, r1);
        if (r2 == 0) goto L_0x002a;
    L_0x0017:
        r2 = r3.mTempRect;
        r0.getDrawingRect(r2);
        r2 = r3.mTempRect;
        r3.offsetDescendantRectToMyCoords(r0, r2);
        r2 = r3.mTempRect;
        r2 = r3.computeScrollDeltaToGetChildRectOnScreen(r2);
        r3.doScrollX(r2);
    L_0x002a:
        return;
    L_0x002b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.HorizontalScrollView.onSizeChanged(int, int, int, int):void");
    }

    private static boolean isViewDescendantOf(View child, View parent) {
        boolean z = true;
        if (child == parent) {
            return true;
        }
        ViewParent theParent = child.getParent();
        if (!((theParent instanceof ViewGroup) && isViewDescendantOf((View) theParent, parent))) {
            z = false;
        }
        return z;
    }

    public void fling(int velocityX) {
        if (getChildCount() > 0) {
            int width = (getWidth() - this.mPaddingRight) - this.mPaddingLeft;
            boolean movingRight = false;
            this.mScroller.fling(this.mScrollX, this.mScrollY, velocityX, 0, 0, Math.max(0, getChildAt(0).getWidth() - width), 0, 0, width / 2, 0);
            if (velocityX > 0) {
                movingRight = true;
            }
            View currentFocused = findFocus();
            View newFocused = findFocusableViewInMyBounds(movingRight, this.mScroller.getFinalX(), currentFocused);
            if (newFocused == null) {
                newFocused = this;
            }
            if (newFocused != currentFocused) {
                newFocused.requestFocus(movingRight ? 66 : 17);
            }
            postInvalidateOnAnimation();
        }
    }

    public void scrollTo(int x, int y) {
        if (getChildCount() > 0) {
            View child = getChildAt(null);
            x = clamp(x, (getWidth() - this.mPaddingRight) - this.mPaddingLeft, child.getWidth());
            y = clamp(y, (getHeight() - this.mPaddingBottom) - this.mPaddingTop, child.getHeight());
            if (x != this.mScrollX || y != this.mScrollY) {
                super.scrollTo(x, y);
            }
        }
    }

    private boolean shouldDisplayEdgeEffects() {
        return getOverScrollMode() != 2;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (shouldDisplayEdgeEffects()) {
            int restoreCount;
            int height;
            int scrollX = this.mScrollX;
            if (!this.mEdgeGlowLeft.isFinished()) {
                restoreCount = canvas.save();
                height = (getHeight() - this.mPaddingTop) - this.mPaddingBottom;
                canvas.rotate(270.0f);
                canvas.translate((float) ((-height) + this.mPaddingTop), (float) Math.min(0, scrollX));
                this.mEdgeGlowLeft.setSize(height, getWidth());
                if (this.mEdgeGlowLeft.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowRight.isFinished()) {
                restoreCount = canvas.save();
                height = getWidth();
                int height2 = (getHeight() - this.mPaddingTop) - this.mPaddingBottom;
                canvas.rotate(90.0f);
                canvas.translate((float) (-this.mPaddingTop), (float) (-(Math.max(getScrollRange(), scrollX) + height)));
                this.mEdgeGlowRight.setSize(height2, height);
                if (this.mEdgeGlowRight.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
        }
    }

    private static int clamp(int n, int my, int child) {
        if (my >= child || n < 0) {
            return 0;
        }
        if (my + n > child) {
            return child - my;
        }
        return n;
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable state) {
        if (this.mContext.getApplicationInfo().targetSdkVersion <= 18) {
            super.onRestoreInstanceState(state);
            return;
        }
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mSavedState = ss;
        requestLayout();
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        if (this.mContext.getApplicationInfo().targetSdkVersion <= 18) {
            return super.onSaveInstanceState();
        }
        SavedState ss = new SavedState(super.onSaveInstanceState());
        ss.scrollOffsetFromStart = isLayoutRtl() ? -this.mScrollX : this.mScrollX;
        return ss;
    }

    /* Access modifiers changed, original: protected */
    public void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("layout:fillViewPort", this.mFillViewport);
    }
}
