package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.StrictMode;
import android.os.StrictMode.Span;
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
import com.android.internal.R;
import java.util.List;

public class ScrollView extends FrameLayout {
    static final int ANIMATED_SCROLL_GAP = 250;
    private static final int INVALID_POINTER = -1;
    static final float MAX_SCROLL_FACTOR = 0.5f;
    private static final String TAG = "ScrollView";
    private int mActivePointerId;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769715)
    private View mChildToScrollTo;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769386)
    private EdgeEffect mEdgeGlowBottom;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123768600)
    private EdgeEffect mEdgeGlowTop;
    @ExportedProperty(category = "layout")
    private boolean mFillViewport;
    @UnsupportedAppUsage
    private Span mFlingStrictSpan;
    @UnsupportedAppUsage
    private boolean mIsBeingDragged;
    private boolean mIsLayoutDirty;
    @UnsupportedAppUsage
    private int mLastMotionY;
    @UnsupportedAppUsage
    private long mLastScroll;
    private int mMaximumVelocity;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124051125)
    private int mMinimumVelocity;
    private int mNestedYOffset;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050903)
    private int mOverflingDistance;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 124050903)
    private int mOverscrollDistance;
    private SavedState mSavedState;
    private final int[] mScrollConsumed;
    private final int[] mScrollOffset;
    private Span mScrollStrictSpan;
    @UnsupportedAppUsage
    private OverScroller mScroller;
    private boolean mSmoothScrollingEnabled;
    private final Rect mTempRect;
    private int mTouchSlop;
    @UnsupportedAppUsage
    private VelocityTracker mVelocityTracker;
    private float mVerticalScrollFactor;

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<ScrollView> {
        private int mFillViewportId;
        private boolean mPropertiesMapped = false;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mFillViewportId = propertyMapper.mapBoolean("fillViewport", 16843130);
            this.mPropertiesMapped = true;
        }

        public void readProperties(ScrollView node, PropertyReader propertyReader) {
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
        public int scrollPosition;

        SavedState(Parcelable superState) {
            super(superState);
        }

        public SavedState(Parcel source) {
            super(source);
            this.scrollPosition = source.readInt();
        }

        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.scrollPosition);
        }

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ScrollView.SavedState{");
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(" scrollPosition=");
            stringBuilder.append(this.scrollPosition);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public ScrollView(Context context) {
        this(context, null);
    }

    public ScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842880);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTempRect = new Rect();
        this.mEdgeGlowTop = new EdgeEffect(getContext());
        this.mEdgeGlowBottom = new EdgeEffect(getContext());
        this.mIsLayoutDirty = true;
        this.mChildToScrollTo = null;
        this.mIsBeingDragged = false;
        this.mSmoothScrollingEnabled = true;
        this.mActivePointerId = -1;
        this.mScrollOffset = new int[2];
        this.mScrollConsumed = new int[2];
        this.mScrollStrictSpan = null;
        this.mFlingStrictSpan = null;
        initScrollView();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.ScrollView, attrs, a, defStyleAttr, defStyleRes);
        setFillViewport(a.getBoolean(0, false));
        a.recycle();
        if (context.getResources().getConfiguration().uiMode == 6) {
            setRevealOnFocusHint(false);
        }
    }

    public boolean shouldDelayChildPressedState() {
        return true;
    }

    /* Access modifiers changed, original: protected */
    public float getTopFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getVerticalFadingEdgeLength();
        if (this.mScrollY < length) {
            return ((float) this.mScrollY) / ((float) length);
        }
        return 1.0f;
    }

    /* Access modifiers changed, original: protected */
    public float getBottomFadingEdgeStrength() {
        if (getChildCount() == 0) {
            return 0.0f;
        }
        int length = getVerticalFadingEdgeLength();
        int span = (getChildAt(0).getBottom() - this.mScrollY) - (getHeight() - this.mPaddingBottom);
        if (span < length) {
            return ((float) span) / ((float) length);
        }
        return 1.0f;
    }

    public void setEdgeEffectColor(int color) {
        setTopEdgeEffectColor(color);
        setBottomEdgeEffectColor(color);
    }

    public void setBottomEdgeEffectColor(int color) {
        this.mEdgeGlowBottom.setColor(color);
    }

    public void setTopEdgeEffectColor(int color) {
        this.mEdgeGlowTop.setColor(color);
    }

    public int getTopEdgeEffectColor() {
        return this.mEdgeGlowTop.getColor();
    }

    public int getBottomEdgeEffectColor() {
        return this.mEdgeGlowBottom.getColor();
    }

    public int getMaxScrollAmount() {
        return (int) (((float) (this.mBottom - this.mTop)) * 0.5f);
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
        this.mVerticalScrollFactor = configuration.getScaledVerticalScrollFactor();
    }

    public void addView(View child) {
        if (getChildCount() <= 0) {
            super.addView(child);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public void addView(View child, int index) {
        if (getChildCount() <= 0) {
            super.addView(child, index);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public void addView(View child, LayoutParams params) {
        if (getChildCount() <= 0) {
            super.addView(child, params);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    public void addView(View child, int index, LayoutParams params) {
        if (getChildCount() <= 0) {
            super.addView(child, index, params);
            return;
        }
        throw new IllegalStateException("ScrollView can host only one direct child");
    }

    @UnsupportedAppUsage
    private boolean canScroll() {
        boolean z = false;
        View child = getChildAt(0);
        if (child == null) {
            return false;
        }
        if (getHeight() < (this.mPaddingTop + child.getHeight()) + this.mPaddingBottom) {
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
        if (this.mFillViewport && MeasureSpec.getMode(heightMeasureSpec) != 0 && getChildCount() > 0) {
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
            int desiredHeight = getMeasuredHeight() - heightPadding;
            if (child.getMeasuredHeight() < desiredHeight) {
                child.measure(ViewGroup.getChildMeasureSpec(widthMeasureSpec, widthPadding, lp.width), MeasureSpec.makeMeasureSpec(desiredHeight, 1073741824));
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        return super.dispatchKeyEvent(event) || executeKeyEvent(event);
    }

    public boolean executeKeyEvent(KeyEvent event) {
        this.mTempRect.setEmpty();
        int i = 130;
        if (canScroll()) {
            boolean handled = false;
            if (event.getAction() == 0) {
                int keyCode = event.getKeyCode();
                if (keyCode == 19) {
                    handled = !event.isAltPressed() ? arrowScroll(33) : fullScroll(33);
                } else if (keyCode == 20) {
                    handled = !event.isAltPressed() ? arrowScroll(130) : fullScroll(130);
                } else if (keyCode == 62) {
                    if (event.isShiftPressed()) {
                        i = 33;
                    }
                    pageScroll(i);
                }
            }
            return handled;
        }
        boolean z = false;
        if (!isFocused() || event.getKeyCode() == 4) {
            return false;
        }
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        }
        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused, 130);
        if (!(nextFocused == null || nextFocused == this || !nextFocused.requestFocus(130))) {
            z = true;
        }
        return z;
    }

    private boolean inChild(int x, int y) {
        boolean z = false;
        if (getChildCount() <= 0) {
            return false;
        }
        int scrollY = this.mScrollY;
        View child = getChildAt(0);
        if (y >= child.getTop() - scrollY && y < child.getBottom() - scrollY && x >= child.getLeft() && x < child.getRight()) {
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
        MotionEvent motionEvent = ev;
        int action = ev.getAction();
        if ((action == 2 && this.mIsBeingDragged) || super.onInterceptTouchEvent(ev)) {
            return true;
        }
        if (getScrollY() == 0 && !canScrollVertically(1)) {
            return false;
        }
        int i = action & 255;
        String str = "ScrollView-scroll";
        if (i != 0) {
            if (i != 1) {
                if (i == 2) {
                    i = this.mActivePointerId;
                    if (i != -1) {
                        int pointerIndex = motionEvent.findPointerIndex(i);
                        if (pointerIndex == -1) {
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("Invalid pointerId=");
                            stringBuilder.append(i);
                            stringBuilder.append(" in onInterceptTouchEvent");
                            Log.e(TAG, stringBuilder.toString());
                        } else {
                            int y = (int) motionEvent.getY(pointerIndex);
                            if (Math.abs(y - this.mLastMotionY) > this.mTouchSlop && (2 & getNestedScrollAxes()) == 0) {
                                this.mIsBeingDragged = true;
                                this.mLastMotionY = y;
                                initVelocityTrackerIfNotExists();
                                this.mVelocityTracker.addMovement(motionEvent);
                                this.mNestedYOffset = 0;
                                if (this.mScrollStrictSpan == null) {
                                    this.mScrollStrictSpan = StrictMode.enterCriticalSpan(str);
                                }
                                ViewParent parent = getParent();
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                            }
                        }
                    }
                } else if (i != 3) {
                    if (i == 6) {
                        onSecondaryPointerUp(ev);
                    }
                }
            }
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
            recycleVelocityTracker();
            if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, 0, 0, getScrollRange())) {
                postInvalidateOnAnimation();
            }
            stopNestedScroll();
        } else {
            i = (int) ev.getY();
            if (inChild((int) ev.getX(), i)) {
                this.mLastMotionY = i;
                this.mActivePointerId = motionEvent.getPointerId(0);
                initOrResetVelocityTracker();
                this.mVelocityTracker.addMovement(motionEvent);
                this.mScroller.computeScrollOffset();
                this.mIsBeingDragged = 1 ^ this.mScroller.isFinished();
                if (this.mIsBeingDragged && this.mScrollStrictSpan == null) {
                    this.mScrollStrictSpan = StrictMode.enterCriticalSpan(str);
                }
                startNestedScroll(2);
            } else {
                this.mIsBeingDragged = false;
                recycleVelocityTracker();
            }
        }
        return this.mIsBeingDragged;
    }

    private boolean shouldDisplayEdgeEffects() {
        return getOverScrollMode() != 2;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        VelocityTracker velocityTracker;
        MotionEvent motionEvent = ev;
        initVelocityTrackerIfNotExists();
        MotionEvent vtev = MotionEvent.obtain(ev);
        int actionMasked = ev.getActionMasked();
        boolean z = false;
        if (actionMasked == 0) {
            this.mNestedYOffset = 0;
        }
        vtev.offsetLocation(0.0f, (float) this.mNestedYOffset);
        ViewParent parent;
        int i;
        if (actionMasked != 0) {
            int deltaY;
            int overscrollMode;
            if (actionMasked != 1) {
                int i2;
                if (actionMasked == 2) {
                    int activePointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
                    if (activePointerIndex == -1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Invalid pointerId=");
                        stringBuilder.append(this.mActivePointerId);
                        stringBuilder.append(" in onTouchEvent");
                        Log.e(TAG, stringBuilder.toString());
                    } else {
                        int deltaY2;
                        int y = (int) motionEvent.getY(activePointerIndex);
                        deltaY = this.mLastMotionY - y;
                        if (dispatchNestedPreScroll(0, deltaY, this.mScrollConsumed, this.mScrollOffset)) {
                            deltaY -= this.mScrollConsumed[1];
                            vtev.offsetLocation(0.0f, (float) this.mScrollOffset[1]);
                            this.mNestedYOffset += this.mScrollOffset[1];
                        }
                        if (this.mIsBeingDragged || Math.abs(deltaY) <= this.mTouchSlop) {
                            deltaY2 = deltaY;
                        } else {
                            parent = getParent();
                            if (parent != null) {
                                parent.requestDisallowInterceptTouchEvent(true);
                            }
                            this.mIsBeingDragged = true;
                            if (deltaY > 0) {
                                deltaY2 = deltaY - this.mTouchSlop;
                            } else {
                                deltaY2 = deltaY + this.mTouchSlop;
                            }
                        }
                        int i3;
                        if (this.mIsBeingDragged) {
                            this.mLastMotionY = y - this.mScrollOffset[1];
                            int oldY = this.mScrollY;
                            int range = getScrollRange();
                            overscrollMode = getOverScrollMode();
                            if (overscrollMode == 0 || (overscrollMode == 1 && range > 0)) {
                                z = true;
                            }
                            boolean canOverscroll = z;
                            i = this.mScrollY;
                            int range2 = range;
                            int oldY2 = oldY;
                            int deltaY3 = deltaY2;
                            int activePointerIndex2 = activePointerIndex;
                            if (overScrollBy(0, deltaY2, 0, i, 0, range2, 0, this.mOverscrollDistance, true) && !hasNestedScrollingParent()) {
                                this.mVelocityTracker.clear();
                            }
                            oldY = this.mScrollY - oldY2;
                            if (dispatchNestedScroll(0, oldY, 0, deltaY3 - oldY, this.mScrollOffset)) {
                                i2 = this.mLastMotionY;
                                int[] iArr = this.mScrollOffset;
                                this.mLastMotionY = i2 - iArr[1];
                                vtev.offsetLocation(0.0f, (float) iArr[1]);
                                this.mNestedYOffset += this.mScrollOffset[1];
                                i3 = activePointerIndex2;
                            } else if (canOverscroll) {
                                i2 = oldY2 + deltaY3;
                                if (i2 < 0) {
                                    this.mEdgeGlowTop.onPull(((float) deltaY3) / ((float) getHeight()), motionEvent.getX(activePointerIndex2) / ((float) getWidth()));
                                    if (this.mEdgeGlowBottom.isFinished()) {
                                        deltaY = range2;
                                    } else {
                                        this.mEdgeGlowBottom.onRelease();
                                        deltaY = range2;
                                    }
                                } else {
                                    i3 = activePointerIndex2;
                                    if (i2 > range2) {
                                        this.mEdgeGlowBottom.onPull(((float) deltaY3) / ((float) getHeight()), 1.0f - (motionEvent.getX(i3) / ((float) getWidth())));
                                        if (!this.mEdgeGlowTop.isFinished()) {
                                            this.mEdgeGlowTop.onRelease();
                                        }
                                    }
                                }
                                if (shouldDisplayEdgeEffects() && !(this.mEdgeGlowTop.isFinished() && this.mEdgeGlowBottom.isFinished())) {
                                    postInvalidateOnAnimation();
                                }
                            } else {
                                i3 = activePointerIndex2;
                            }
                        } else {
                            int i4 = y;
                            i3 = activePointerIndex;
                        }
                    }
                } else if (actionMasked != 3) {
                    if (actionMasked == 5) {
                        i2 = ev.getActionIndex();
                        this.mLastMotionY = (int) motionEvent.getY(i2);
                        this.mActivePointerId = motionEvent.getPointerId(i2);
                    } else if (actionMasked == 6) {
                        onSecondaryPointerUp(ev);
                        this.mLastMotionY = (int) motionEvent.getY(motionEvent.findPointerIndex(this.mActivePointerId));
                    }
                } else if (this.mIsBeingDragged && getChildCount() > 0) {
                    if (this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, 0, 0, getScrollRange())) {
                        postInvalidateOnAnimation();
                    }
                    this.mActivePointerId = -1;
                    endDrag();
                }
            } else if (this.mIsBeingDragged) {
                velocityTracker = this.mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000, (float) this.mMaximumVelocity);
                deltaY = (int) velocityTracker.getYVelocity(this.mActivePointerId);
                if (Math.abs(deltaY) > this.mMinimumVelocity) {
                    flingWithNestedDispatch(-deltaY);
                } else {
                    OverScroller overScroller = this.mScroller;
                    overscrollMode = this.mScrollX;
                    if (overScroller.springBack(overscrollMode, this.mScrollY, 0, 0, 0, getScrollRange())) {
                        postInvalidateOnAnimation();
                    }
                }
                this.mActivePointerId = -1;
                endDrag();
            }
        } else if (getChildCount() == 0) {
            return false;
        } else {
            i = this.mScroller.isFinished() ^ 1;
            this.mIsBeingDragged = i;
            if (i != 0) {
                parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
            }
            if (!this.mScroller.isFinished()) {
                this.mScroller.abortAnimation();
                Span span = this.mFlingStrictSpan;
                if (span != null) {
                    span.finish();
                    this.mFlingStrictSpan = null;
                }
            }
            this.mLastMotionY = (int) ev.getY();
            this.mActivePointerId = motionEvent.getPointerId(0);
            startNestedScroll(2);
        }
        velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.addMovement(vtev);
        }
        vtev.recycle();
        return true;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (ev.getAction() & 65280) >> 8;
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mLastMotionY = (int) ev.getY(newPointerIndex);
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
            VelocityTracker velocityTracker = this.mVelocityTracker;
            if (velocityTracker != null) {
                velocityTracker.clear();
            }
        }
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        if (event.getAction() == 8) {
            float axisValue;
            if (event.isFromSource(2)) {
                axisValue = event.getAxisValue(1.3E-44f);
            } else if (event.isFromSource(4194304)) {
                axisValue = event.getAxisValue(26);
            } else {
                axisValue = 0.0f;
            }
            int delta = Math.round(this.mVerticalScrollFactor * axisValue);
            if (delta != 0) {
                int range = getScrollRange();
                int oldScrollY = this.mScrollY;
                int newScrollY = oldScrollY - delta;
                if (newScrollY < 0) {
                    newScrollY = 0;
                } else if (newScrollY > range) {
                    newScrollY = range;
                }
                if (newScrollY != oldScrollY) {
                    super.scrollTo(this.mScrollX, newScrollY);
                    return true;
                }
            }
        }
        return super.onGenericMotionEvent(event);
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
            if (clampedY) {
                this.mScroller.springBack(this.mScrollX, this.mScrollY, 0, 0, 0, getScrollRange());
            }
        }
        awakenScrollBars();
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        if (!isEnabled()) {
            return false;
        }
        int targetScrollY;
        if (action != 4096) {
            if (action == 8192 || action == 16908344) {
                targetScrollY = Math.max(this.mScrollY - ((getHeight() - this.mPaddingBottom) - this.mPaddingTop), 0);
                if (targetScrollY == this.mScrollY) {
                    return false;
                }
                smoothScrollTo(0, targetScrollY);
                return true;
            } else if (action != 16908346) {
                return false;
            }
        }
        targetScrollY = Math.min(this.mScrollY + ((getHeight() - this.mPaddingBottom) - this.mPaddingTop), getScrollRange());
        if (targetScrollY == this.mScrollY) {
            return false;
        }
        smoothScrollTo(0, targetScrollY);
        return true;
    }

    public CharSequence getAccessibilityClassName() {
        return ScrollView.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (isEnabled()) {
            int scrollRange = getScrollRange();
            if (scrollRange > 0) {
                info.setScrollable(true);
                if (this.mScrollY > 0) {
                    info.addAction(AccessibilityAction.ACTION_SCROLL_BACKWARD);
                    info.addAction(AccessibilityAction.ACTION_SCROLL_UP);
                }
                if (this.mScrollY < scrollRange) {
                    info.addAction(AccessibilityAction.ACTION_SCROLL_FORWARD);
                    info.addAction(AccessibilityAction.ACTION_SCROLL_DOWN);
                }
            }
        }
    }

    public void onInitializeAccessibilityEventInternal(AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        event.setScrollable(getScrollRange() > 0);
        event.setScrollX(this.mScrollX);
        event.setScrollY(this.mScrollY);
        event.setMaxScrollX(this.mScrollX);
        event.setMaxScrollY(getScrollRange());
    }

    private int getScrollRange() {
        if (getChildCount() > 0) {
            return Math.max(0, getChildAt(0).getHeight() - ((getHeight() - this.mPaddingBottom) - this.mPaddingTop));
        }
        return 0;
    }

    private View findFocusableViewInBounds(boolean topFocus, int top, int bottom) {
        List<View> focusables = getFocusables(2);
        View focusCandidate = null;
        boolean foundFullyContainedFocusable = false;
        int count = focusables.size();
        for (int i = 0; i < count; i++) {
            View view = (View) focusables.get(i);
            int viewTop = view.getTop();
            int viewBottom = view.getBottom();
            if (top < viewBottom && viewTop < bottom) {
                boolean viewIsCloserToBoundary = false;
                boolean viewIsFullyContained = top < viewTop && viewBottom < bottom;
                if (focusCandidate == null) {
                    focusCandidate = view;
                    foundFullyContainedFocusable = viewIsFullyContained;
                } else {
                    if ((topFocus && viewTop < focusCandidate.getTop()) || (!topFocus && viewBottom > focusCandidate.getBottom())) {
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
        boolean down = direction == 130;
        int height = getHeight();
        if (down) {
            this.mTempRect.top = getScrollY() + height;
            int count = getChildCount();
            if (count > 0) {
                View view = getChildAt(count - 1);
                if (this.mTempRect.top + height > view.getBottom()) {
                    this.mTempRect.top = view.getBottom() - height;
                }
            }
        } else {
            this.mTempRect.top = getScrollY() - height;
            if (this.mTempRect.top < 0) {
                this.mTempRect.top = 0;
            }
        }
        Rect rect = this.mTempRect;
        rect.bottom = rect.top + height;
        return scrollAndFocus(direction, this.mTempRect.top, this.mTempRect.bottom);
    }

    public boolean fullScroll(int direction) {
        boolean down = direction == 130;
        int height = getHeight();
        Rect rect = this.mTempRect;
        rect.top = 0;
        rect.bottom = height;
        if (down) {
            int count = getChildCount();
            if (count > 0) {
                this.mTempRect.bottom = getChildAt(count - 1).getBottom() + this.mPaddingBottom;
                Rect rect2 = this.mTempRect;
                rect2.top = rect2.bottom - height;
            }
        }
        return scrollAndFocus(direction, this.mTempRect.top, this.mTempRect.bottom);
    }

    private boolean scrollAndFocus(int direction, int top, int bottom) {
        boolean handled = true;
        int height = getHeight();
        int containerTop = getScrollY();
        int containerBottom = containerTop + height;
        boolean up = direction == 33;
        View newFocused = findFocusableViewInBounds(up, top, bottom);
        if (newFocused == null) {
            newFocused = this;
        }
        if (top < containerTop || bottom > containerBottom) {
            doScrollY(up ? top - containerTop : bottom - containerBottom);
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
        if (nextFocused == null || !isWithinDeltaOfScreen(nextFocused, maxJump, getHeight())) {
            scrollDelta = maxJump;
            if (direction == 33 && getScrollY() < scrollDelta) {
                scrollDelta = getScrollY();
            } else if (direction == 130 && getChildCount() > 0) {
                int daBottom = getChildAt(0).getBottom();
                int screenBottom = (getScrollY() + getHeight()) - this.mPaddingBottom;
                if (daBottom - screenBottom < maxJump) {
                    scrollDelta = daBottom - screenBottom;
                }
            }
            if (scrollDelta == 0) {
                return false;
            }
            doScrollY(direction == 130 ? scrollDelta : -scrollDelta);
        } else {
            nextFocused.getDrawingRect(this.mTempRect);
            offsetDescendantRectToMyCoords(nextFocused, this.mTempRect);
            doScrollY(computeScrollDeltaToGetChildRectOnScreen(this.mTempRect));
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
        return isWithinDeltaOfScreen(descendant, 0, getHeight()) ^ 1;
    }

    private boolean isWithinDeltaOfScreen(View descendant, int delta, int height) {
        descendant.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        return this.mTempRect.bottom + delta >= getScrollY() && this.mTempRect.top - delta <= getScrollY() + height;
    }

    private void doScrollY(int delta) {
        if (delta == 0) {
            return;
        }
        if (this.mSmoothScrollingEnabled) {
            smoothScrollBy(0, delta);
        } else {
            scrollBy(0, delta);
        }
    }

    public final void smoothScrollBy(int dx, int dy) {
        if (getChildCount() != 0) {
            if (AnimationUtils.currentAnimationTimeMillis() - this.mLastScroll > 250) {
                int maxY = Math.max(0, getChildAt(0).getHeight() - ((getHeight() - this.mPaddingBottom) - this.mPaddingTop));
                int scrollY = this.mScrollY;
                this.mScroller.startScroll(this.mScrollX, scrollY, 0, Math.max(0, Math.min(scrollY + dy, maxY)) - scrollY);
                postInvalidateOnAnimation();
            } else {
                if (!this.mScroller.isFinished()) {
                    this.mScroller.abortAnimation();
                    Span span = this.mFlingStrictSpan;
                    if (span != null) {
                        span.finish();
                        this.mFlingStrictSpan = null;
                    }
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
    public int computeVerticalScrollRange() {
        int contentHeight = (getHeight() - this.mPaddingBottom) - this.mPaddingTop;
        if (getChildCount() == 0) {
            return contentHeight;
        }
        int scrollRange = getChildAt(0).getBottom();
        int scrollY = this.mScrollY;
        int overscrollBottom = Math.max(0, scrollRange - contentHeight);
        if (scrollY < 0) {
            scrollRange -= scrollY;
        } else if (scrollY > overscrollBottom) {
            scrollRange += scrollY - overscrollBottom;
        }
        return scrollRange;
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollOffset() {
        return Math.max(0, super.computeVerticalScrollOffset());
    }

    /* Access modifiers changed, original: protected */
    public void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec) {
        child.measure(ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, this.mPaddingLeft + this.mPaddingRight, child.getLayoutParams().width), MeasureSpec.makeSafeMeasureSpec(Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - (this.mPaddingTop + this.mPaddingBottom)), 0));
    }

    /* Access modifiers changed, original: protected */
    public void measureChildWithMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        child.measure(ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, (((this.mPaddingLeft + this.mPaddingRight) + lp.leftMargin) + lp.rightMargin) + widthUsed, lp.width), MeasureSpec.makeSafeMeasureSpec(Math.max(0, MeasureSpec.getSize(parentHeightMeasureSpec) - ((((this.mPaddingTop + this.mPaddingBottom) + lp.topMargin) + lp.bottomMargin) + heightUsed)), 0));
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
                overScrollBy(x - oldX, y - oldY, oldX, oldY, 0, range, 0, this.mOverflingDistance, false);
                onScrollChanged(this.mScrollX, this.mScrollY, oldX, oldY);
                if (canOverscroll) {
                    if (y < 0 && oldY >= 0) {
                        this.mEdgeGlowTop.onAbsorb((int) this.mScroller.getCurrVelocity());
                    } else if (y > range && oldY <= range) {
                        this.mEdgeGlowBottom.onAbsorb((int) this.mScroller.getCurrVelocity());
                    }
                }
            }
            if (!awakenScrollBars()) {
                postInvalidateOnAnimation();
                return;
            }
            return;
        }
        Span span = this.mFlingStrictSpan;
        if (span != null) {
            span.finish();
            this.mFlingStrictSpan = null;
        }
    }

    public void scrollToDescendant(View child) {
        if (this.mIsLayoutDirty) {
            this.mChildToScrollTo = child;
            return;
        }
        child.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(child, this.mTempRect);
        int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(this.mTempRect);
        if (scrollDelta != 0) {
            scrollBy(0, scrollDelta);
        }
    }

    private boolean scrollToChildRect(Rect rect, boolean immediate) {
        int delta = computeScrollDeltaToGetChildRectOnScreen(rect);
        boolean scroll = delta != 0;
        if (scroll) {
            if (immediate) {
                scrollBy(0, delta);
            } else {
                smoothScrollBy(0, delta);
            }
        }
        return scroll;
    }

    /* Access modifiers changed, original: protected */
    public int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
        if (getChildCount() == 0) {
            return 0;
        }
        int height = getHeight();
        int screenTop = getScrollY();
        int screenBottom = screenTop + height;
        int fadingEdge = getVerticalFadingEdgeLength();
        if (rect.top > 0) {
            screenTop += fadingEdge;
        }
        if (rect.bottom < getChildAt(0).getHeight()) {
            screenBottom -= fadingEdge;
        }
        int scrollYDelta = 0;
        if (rect.bottom > screenBottom && rect.top > screenTop) {
            if (rect.height() > height) {
                scrollYDelta = 0 + (rect.top - screenTop);
            } else {
                scrollYDelta = 0 + (rect.bottom - screenBottom);
            }
            scrollYDelta = Math.min(scrollYDelta, getChildAt(0).getBottom() - screenBottom);
        } else if (rect.top < screenTop && rect.bottom < screenBottom) {
            if (rect.height() > height) {
                scrollYDelta = 0 - (screenBottom - rect.bottom);
            } else {
                scrollYDelta = 0 - (screenTop - rect.top);
            }
            scrollYDelta = Math.max(scrollYDelta, -getScrollY());
        }
        return scrollYDelta;
    }

    public void requestChildFocus(View child, View focused) {
        if (focused != null && focused.getRevealOnFocusHint()) {
            if (this.mIsLayoutDirty) {
                this.mChildToScrollTo = focused;
            } else {
                scrollToDescendant(focused);
            }
        }
        super.requestChildFocus(child, focused);
    }

    /* Access modifiers changed, original: protected */
    public boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        View nextFocus;
        if (direction == 2) {
            direction = 130;
        } else if (direction == 1) {
            direction = 33;
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
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Span span = this.mScrollStrictSpan;
        if (span != null) {
            span.finish();
            this.mScrollStrictSpan = null;
        }
        span = this.mFlingStrictSpan;
        if (span != null) {
            span.finish();
            this.mFlingStrictSpan = null;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mIsLayoutDirty = false;
        View view = this.mChildToScrollTo;
        if (view != null && isViewDescendantOf(view, this)) {
            scrollToDescendant(this.mChildToScrollTo);
        }
        this.mChildToScrollTo = null;
        if (!isLaidOut()) {
            SavedState savedState = this.mSavedState;
            if (savedState != null) {
                this.mScrollY = savedState.scrollPosition;
                this.mSavedState = null;
            }
            int scrollRange = Math.max(0, (getChildCount() > 0 ? getChildAt(0).getMeasuredHeight() : 0) - (((b - t) - this.mPaddingBottom) - this.mPaddingTop));
            if (this.mScrollY > scrollRange) {
                this.mScrollY = scrollRange;
            } else if (this.mScrollY < 0) {
                this.mScrollY = 0;
            }
        }
        scrollTo(this.mScrollX, this.mScrollY);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Missing block: B:7:0x0027, code skipped:
            return;
     */
    public void onSizeChanged(int r3, int r4, int r5, int r6) {
        /*
        r2 = this;
        super.onSizeChanged(r3, r4, r5, r6);
        r0 = r2.findFocus();
        if (r0 == 0) goto L_0x0027;
    L_0x0009:
        if (r2 != r0) goto L_0x000c;
    L_0x000b:
        goto L_0x0027;
    L_0x000c:
        r1 = 0;
        r1 = r2.isWithinDeltaOfScreen(r0, r1, r6);
        if (r1 == 0) goto L_0x0026;
    L_0x0013:
        r1 = r2.mTempRect;
        r0.getDrawingRect(r1);
        r1 = r2.mTempRect;
        r2.offsetDescendantRectToMyCoords(r0, r1);
        r1 = r2.mTempRect;
        r1 = r2.computeScrollDeltaToGetChildRectOnScreen(r1);
        r2.doScrollY(r1);
    L_0x0026:
        return;
    L_0x0027:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ScrollView.onSizeChanged(int, int, int, int):void");
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

    public void fling(int velocityY) {
        if (getChildCount() > 0) {
            int height = (getHeight() - this.mPaddingBottom) - this.mPaddingTop;
            int i = velocityY;
            this.mScroller.fling(this.mScrollX, this.mScrollY, 0, i, 0, 0, 0, Math.max(0, getChildAt(0).getHeight() - height), 0, height / 2);
            if (this.mFlingStrictSpan == null) {
                this.mFlingStrictSpan = StrictMode.enterCriticalSpan("ScrollView-fling");
            }
            postInvalidateOnAnimation();
        }
    }

    private void flingWithNestedDispatch(int velocityY) {
        boolean canFling = (this.mScrollY > 0 || velocityY > 0) && (this.mScrollY < getScrollRange() || velocityY < 0);
        if (!dispatchNestedPreFling(0.0f, (float) velocityY)) {
            dispatchNestedFling(0.0f, (float) velocityY, canFling);
            if (canFling) {
                fling(velocityY);
            }
        }
    }

    @UnsupportedAppUsage
    private void endDrag() {
        this.mIsBeingDragged = false;
        recycleVelocityTracker();
        if (shouldDisplayEdgeEffects()) {
            this.mEdgeGlowTop.onRelease();
            this.mEdgeGlowBottom.onRelease();
        }
        Span span = this.mScrollStrictSpan;
        if (span != null) {
            span.finish();
            this.mScrollStrictSpan = null;
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

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return (nestedScrollAxes & 2) != 0;
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(2);
    }

    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        int oldScrollY = this.mScrollY;
        scrollBy(0, dyUnconsumed);
        int myConsumed = this.mScrollY - oldScrollY;
        dispatchNestedScroll(0, myConsumed, 0, dyUnconsumed - myConsumed, null);
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (consumed) {
            return false;
        }
        flingWithNestedDispatch((int) velocityY);
        return true;
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (shouldDisplayEdgeEffects()) {
            int restoreCount;
            int width;
            int height;
            float translateX;
            float translateY;
            int scrollY = this.mScrollY;
            boolean clipToPadding = getClipToPadding();
            if (!this.mEdgeGlowTop.isFinished()) {
                restoreCount = canvas.save();
                if (clipToPadding) {
                    width = (getWidth() - this.mPaddingLeft) - this.mPaddingRight;
                    height = (getHeight() - this.mPaddingTop) - this.mPaddingBottom;
                    translateX = (float) this.mPaddingLeft;
                    translateY = (float) this.mPaddingTop;
                } else {
                    width = getWidth();
                    height = getHeight();
                    translateX = 0.0f;
                    translateY = 0.0f;
                }
                canvas.translate(translateX, ((float) Math.min(0, scrollY)) + translateY);
                this.mEdgeGlowTop.setSize(width, height);
                if (this.mEdgeGlowTop.draw(canvas)) {
                    postInvalidateOnAnimation();
                }
                canvas.restoreToCount(restoreCount);
            }
            if (!this.mEdgeGlowBottom.isFinished()) {
                restoreCount = canvas.save();
                if (clipToPadding) {
                    width = (getWidth() - this.mPaddingLeft) - this.mPaddingRight;
                    height = (getHeight() - this.mPaddingTop) - this.mPaddingBottom;
                    translateX = (float) this.mPaddingLeft;
                    translateY = (float) this.mPaddingTop;
                } else {
                    width = getWidth();
                    height = getHeight();
                    translateX = 0.0f;
                    translateY = 0.0f;
                }
                canvas.translate(((float) (-width)) + translateX, ((float) (Math.max(getScrollRange(), scrollY) + height)) + translateY);
                canvas.rotate(180.0f, (float) width, 0.0f);
                this.mEdgeGlowBottom.setSize(width, height);
                if (this.mEdgeGlowBottom.draw(canvas)) {
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
        ss.scrollPosition = this.mScrollY;
        return ss;
    }

    /* Access modifiers changed, original: protected */
    public void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("fillViewport", this.mFillViewport);
    }
}
