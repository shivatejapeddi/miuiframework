package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.metrics.LogMaker;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver.OnTouchModeChangeListener;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.OverScroller;
import android.widget.ScrollView;
import com.android.internal.R;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.nano.MetricsProto.MetricsEvent;

public class ResolverDrawerLayout extends ViewGroup {
    private static final String TAG = "ResolverDrawerLayout";
    private int mActivePointerId;
    private int mAlwaysShowHeight;
    private float mCollapseOffset;
    private int mCollapsibleHeight;
    private int mCollapsibleHeightReserved;
    private boolean mDismissLocked;
    private boolean mDismissOnScrollerFinished;
    private float mDragRemainder;
    private float mInitialTouchX;
    private float mInitialTouchY;
    private boolean mIsDragging;
    private float mLastTouchY;
    private int mMaxCollapsedHeight;
    private int mMaxCollapsedHeightSmall;
    private int mMaxWidth;
    private MetricsLogger mMetricsLogger;
    private final float mMinFlingVelocity;
    private AbsListView mNestedScrollingChild;
    private OnCollapsedChangedListener mOnCollapsedChangedListener;
    private OnDismissedListener mOnDismissedListener;
    private boolean mOpenOnClick;
    private boolean mOpenOnLayout;
    private RunOnDismissedListener mRunOnDismissedListener;
    private Drawable mScrollIndicatorDrawable;
    private final OverScroller mScroller;
    private boolean mShowAtTop;
    private boolean mSmallCollapsed;
    private final Rect mTempRect;
    private int mTopOffset;
    private final OnTouchModeChangeListener mTouchModeChangeListener;
    private final int mTouchSlop;
    private int mUncollapsibleHeight;
    private final VelocityTracker mVelocityTracker;

    public interface OnDismissedListener {
        void onDismissed();
    }

    public static class LayoutParams extends MarginLayoutParams {
        public boolean alwaysShow;
        public boolean hasNestedScrollIndicator;
        public boolean ignoreOffset;
        public int maxHeight;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.ResolverDrawerLayout_LayoutParams);
            this.alwaysShow = a.getBoolean(1, false);
            this.ignoreOffset = a.getBoolean(3, false);
            this.hasNestedScrollIndicator = a.getBoolean(2, false);
            this.maxHeight = a.getDimensionPixelSize(4, -1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(LayoutParams source) {
            super((MarginLayoutParams) source);
            this.alwaysShow = source.alwaysShow;
            this.ignoreOffset = source.ignoreOffset;
            this.hasNestedScrollIndicator = source.hasNestedScrollIndicator;
            this.maxHeight = source.maxHeight;
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }
    }

    public interface OnCollapsedChangedListener {
        void onCollapsedChanged(boolean z);
    }

    private class RunOnDismissedListener implements Runnable {
        private RunOnDismissedListener() {
        }

        /* synthetic */ RunOnDismissedListener(ResolverDrawerLayout x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            ResolverDrawerLayout.this.dispatchOnDismissed();
        }
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        boolean open;

        /* synthetic */ SavedState(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.open = in.readInt() != 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.open);
        }
    }

    public ResolverDrawerLayout(Context context) {
        this(context, null);
    }

    public ResolverDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ResolverDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mDragRemainder = 0.0f;
        this.mActivePointerId = -1;
        this.mTempRect = new Rect();
        this.mTouchModeChangeListener = new OnTouchModeChangeListener() {
            public void onTouchModeChanged(boolean isInTouchMode) {
                if (!isInTouchMode && ResolverDrawerLayout.this.hasFocus()) {
                    ResolverDrawerLayout resolverDrawerLayout = ResolverDrawerLayout.this;
                    if (resolverDrawerLayout.isDescendantClipped(resolverDrawerLayout.getFocusedChild())) {
                        ResolverDrawerLayout.this.smoothScrollTo(0, 0.0f);
                    }
                }
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ResolverDrawerLayout, defStyleAttr, 0);
        this.mMaxWidth = a.getDimensionPixelSize(0, -1);
        this.mMaxCollapsedHeight = a.getDimensionPixelSize(1, 0);
        this.mMaxCollapsedHeightSmall = a.getDimensionPixelSize(2, this.mMaxCollapsedHeight);
        this.mShowAtTop = a.getBoolean(3, false);
        a.recycle();
        this.mScrollIndicatorDrawable = this.mContext.getDrawable(R.drawable.scroll_indicator_material);
        this.mScroller = new OverScroller(context, AnimationUtils.loadInterpolator(context, 17563653));
        this.mVelocityTracker = VelocityTracker.obtain();
        ViewConfiguration vc = ViewConfiguration.get(context);
        this.mTouchSlop = vc.getScaledTouchSlop();
        this.mMinFlingVelocity = (float) vc.getScaledMinimumFlingVelocity();
        setImportantForAccessibility(1);
    }

    public void setSmallCollapsed(boolean smallCollapsed) {
        this.mSmallCollapsed = smallCollapsed;
        requestLayout();
    }

    public boolean isSmallCollapsed() {
        return this.mSmallCollapsed;
    }

    public boolean isCollapsed() {
        return this.mCollapseOffset > 0.0f;
    }

    public void setShowAtTop(boolean showOnTop) {
        this.mShowAtTop = showOnTop;
        invalidate();
        requestLayout();
    }

    public boolean getShowAtTop() {
        return this.mShowAtTop;
    }

    public void setCollapsed(boolean collapsed) {
        if (isLaidOut()) {
            smoothScrollTo(collapsed ? this.mCollapsibleHeight : 0, 0.0f);
        } else {
            this.mOpenOnLayout = collapsed;
        }
    }

    public void setCollapsibleHeightReserved(int heightPixels) {
        int oldReserved = this.mCollapsibleHeightReserved;
        this.mCollapsibleHeightReserved = heightPixels;
        int dReserved = this.mCollapsibleHeightReserved - oldReserved;
        if (dReserved != 0 && this.mIsDragging) {
            this.mLastTouchY -= (float) dReserved;
        }
        int oldCollapsibleHeight = this.mCollapsibleHeight;
        this.mCollapsibleHeight = Math.max(this.mCollapsibleHeight, getMaxCollapsedHeight());
        if (!updateCollapseOffset(oldCollapsibleHeight, isDragging() ^ 1)) {
            invalidate();
        }
    }

    public void setDismissLocked(boolean locked) {
        this.mDismissLocked = locked;
    }

    private boolean isMoving() {
        return this.mIsDragging || !this.mScroller.isFinished();
    }

    private boolean isDragging() {
        return this.mIsDragging || getNestedScrollAxes() == 2;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0045  */
    private boolean updateCollapseOffset(int r8, boolean r9) {
        /*
        r7 = this;
        r0 = r7.mCollapsibleHeight;
        r1 = 0;
        if (r8 != r0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = r7.getShowAtTop();
        r2 = 0;
        if (r0 == 0) goto L_0x0010;
    L_0x000d:
        r7.mCollapseOffset = r2;
        return r1;
    L_0x0010:
        r0 = r7.isLaidOut();
        r3 = 1;
        if (r0 == 0) goto L_0x0049;
    L_0x0017:
        r0 = r7.mCollapseOffset;
        r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        if (r0 == 0) goto L_0x001f;
    L_0x001d:
        r0 = r3;
        goto L_0x0020;
    L_0x001f:
        r0 = r1;
    L_0x0020:
        if (r9 == 0) goto L_0x0031;
    L_0x0022:
        r4 = r7.mCollapsibleHeight;
        if (r8 >= r4) goto L_0x0031;
    L_0x0026:
        r5 = r7.mCollapseOffset;
        r6 = (float) r8;
        r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1));
        if (r5 != 0) goto L_0x0031;
    L_0x002d:
        r4 = (float) r4;
        r7.mCollapseOffset = r4;
        goto L_0x003c;
    L_0x0031:
        r4 = r7.mCollapseOffset;
        r5 = r7.mCollapsibleHeight;
        r5 = (float) r5;
        r4 = java.lang.Math.min(r4, r5);
        r7.mCollapseOffset = r4;
    L_0x003c:
        r4 = r7.mCollapseOffset;
        r2 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1));
        if (r2 == 0) goto L_0x0043;
    L_0x0042:
        r1 = r3;
    L_0x0043:
        if (r0 == r1) goto L_0x0048;
    L_0x0045:
        r7.onCollapsedChanged(r1);
    L_0x0048:
        goto L_0x0053;
    L_0x0049:
        r0 = r7.mOpenOnLayout;
        if (r0 == 0) goto L_0x004e;
    L_0x004d:
        goto L_0x0051;
    L_0x004e:
        r0 = r7.mCollapsibleHeight;
        r2 = (float) r0;
    L_0x0051:
        r7.mCollapseOffset = r2;
    L_0x0053:
        return r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.ResolverDrawerLayout.updateCollapseOffset(int, boolean):boolean");
    }

    private int getMaxCollapsedHeight() {
        return (isSmallCollapsed() ? this.mMaxCollapsedHeightSmall : this.mMaxCollapsedHeight) + this.mCollapsibleHeightReserved;
    }

    public void setOnDismissedListener(OnDismissedListener listener) {
        this.mOnDismissedListener = listener;
    }

    private boolean isDismissable() {
        return (this.mOnDismissedListener == null || this.mDismissLocked) ? false : true;
    }

    public void setOnCollapsedChangedListener(OnCollapsedChangedListener listener) {
        this.mOnCollapsedChangedListener = listener;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == 0) {
            this.mVelocityTracker.clear();
        }
        this.mVelocityTracker.addMovement(ev);
        float x;
        float f;
        if (action != 0) {
            if (action != 1) {
                if (action == 2) {
                    x = ev.getX();
                    float y = ev.getY();
                    float dy = y - this.mInitialTouchY;
                    if (Math.abs(dy) > ((float) this.mTouchSlop) && findChildUnder(x, y) != null && (2 & getNestedScrollAxes()) == 0) {
                        this.mActivePointerId = ev.getPointerId(0);
                        this.mIsDragging = true;
                        f = this.mLastTouchY;
                        int i = this.mTouchSlop;
                        this.mLastTouchY = Math.max(f - ((float) i), Math.min(f + dy, f + ((float) i)));
                    }
                } else if (action != 3) {
                    if (action == 6) {
                        onSecondaryPointerUp(ev);
                    }
                }
            }
            resetTouch();
        } else {
            f = ev.getX();
            x = ev.getY();
            this.mInitialTouchX = f;
            this.mLastTouchY = x;
            this.mInitialTouchY = x;
            boolean z = isListChildUnderClipped(f, x) && this.mCollapseOffset > 0.0f;
            this.mOpenOnClick = z;
        }
        if (this.mIsDragging) {
            abortAnimation();
        }
        if (this.mIsDragging || this.mOpenOnClick) {
            return true;
        }
        return false;
    }

    private boolean isNestedChildScrolled() {
        AbsListView absListView = this.mNestedScrollingChild;
        if (absListView == null || absListView.getChildCount() <= 0) {
            return false;
        }
        if (this.mNestedScrollingChild.getFirstVisiblePosition() > 0 || this.mNestedScrollingChild.getChildAt(0).getTop() < 0) {
            return true;
        }
        return false;
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        this.mVelocityTracker.addMovement(ev);
        boolean handled = false;
        boolean z = true;
        int i = 0;
        float x;
        float y;
        float yvel;
        float f;
        float f2;
        if (action == 0) {
            x = ev.getX();
            y = ev.getY();
            this.mInitialTouchX = x;
            this.mLastTouchY = y;
            this.mInitialTouchY = y;
            this.mActivePointerId = ev.getPointerId(0);
            boolean hitView = findChildUnder(this.mInitialTouchX, this.mInitialTouchY) != null;
            boolean z2 = isDismissable() || this.mCollapsibleHeight > 0;
            handled = z2;
            if (!(hitView && handled)) {
                z = false;
            }
            this.mIsDragging = z;
            abortAnimation();
        } else if (action == 1) {
            boolean wasDragging = this.mIsDragging;
            this.mIsDragging = false;
            if (!wasDragging && findChildUnder(this.mInitialTouchX, this.mInitialTouchY) == null && findChildUnder(ev.getX(), ev.getY()) == null && isDismissable()) {
                dispatchOnDismissed();
                resetTouch();
                return true;
            } else if (!this.mOpenOnClick || Math.abs(ev.getX() - this.mInitialTouchX) >= ((float) this.mTouchSlop) || Math.abs(ev.getY() - this.mInitialTouchY) >= ((float) this.mTouchSlop)) {
                this.mVelocityTracker.computeCurrentVelocity(1000);
                yvel = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
                if (Math.abs(yvel) <= this.mMinFlingVelocity) {
                    f = this.mCollapseOffset;
                    int i2 = this.mCollapsibleHeight;
                    if (f >= ((float) (i2 / 2))) {
                        i = i2;
                    }
                    smoothScrollTo(i, 0.0f);
                } else if (!getShowAtTop()) {
                    if (isDismissable() && yvel > 0.0f) {
                        f2 = this.mCollapseOffset;
                        int i3 = this.mCollapsibleHeight;
                        if (f2 > ((float) i3)) {
                            smoothScrollTo(i3 + this.mUncollapsibleHeight, yvel);
                            this.mDismissOnScrollerFinished = true;
                        }
                    }
                    if (isNestedChildScrolled()) {
                        this.mNestedScrollingChild.smoothScrollToPosition(0);
                    }
                    if (yvel >= 0.0f) {
                        i = this.mCollapsibleHeight;
                    }
                    smoothScrollTo(i, yvel);
                } else if (!isDismissable() || yvel >= 0.0f) {
                    if (yvel >= 0.0f) {
                        i = this.mCollapsibleHeight;
                    }
                    smoothScrollTo(i, yvel);
                } else {
                    abortAnimation();
                    dismiss();
                }
                resetTouch();
            } else {
                smoothScrollTo(0, 0.0f);
                return true;
            }
        } else if (action == 2) {
            int index = ev.findPointerIndex(this.mActivePointerId);
            if (index < 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Bad pointer id ");
                stringBuilder.append(this.mActivePointerId);
                stringBuilder.append(", resetting");
                Log.e(TAG, stringBuilder.toString());
                index = 0;
                this.mActivePointerId = ev.getPointerId(0);
                this.mInitialTouchX = ev.getX();
                yvel = ev.getY();
                this.mLastTouchY = yvel;
                this.mInitialTouchY = yvel;
            }
            yvel = ev.getX(index);
            f2 = ev.getY(index);
            if (!this.mIsDragging) {
                float dy = f2 - this.mInitialTouchY;
                if (Math.abs(dy) > ((float) this.mTouchSlop) && findChildUnder(yvel, f2) != null) {
                    this.mIsDragging = true;
                    handled = true;
                    f = this.mLastTouchY;
                    int i4 = this.mTouchSlop;
                    this.mLastTouchY = Math.max(f - ((float) i4), Math.min(f + dy, f + ((float) i4)));
                }
            }
            if (this.mIsDragging) {
                f = f2 - this.mLastTouchY;
                if (f <= 0.0f || !isNestedChildScrolled()) {
                    performDrag(f);
                } else {
                    this.mNestedScrollingChild.smoothScrollBy((int) (-f), 0);
                }
            }
            this.mLastTouchY = f2;
        } else if (action == 3) {
            if (this.mIsDragging) {
                y = this.mCollapseOffset;
                int i5 = this.mCollapsibleHeight;
                if (y >= ((float) (i5 / 2))) {
                    i = i5;
                }
                smoothScrollTo(i, 0.0f);
            }
            resetTouch();
            return true;
        } else if (action == 5) {
            int pointerIndex = ev.getActionIndex();
            this.mActivePointerId = ev.getPointerId(pointerIndex);
            this.mInitialTouchX = ev.getX(pointerIndex);
            x = ev.getY(pointerIndex);
            this.mLastTouchY = x;
            this.mInitialTouchY = x;
        } else if (action == 6) {
            onSecondaryPointerUp(ev);
        }
        return handled;
    }

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = ev.getActionIndex();
        if (ev.getPointerId(pointerIndex) == this.mActivePointerId) {
            int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.mInitialTouchX = ev.getX(newPointerIndex);
            float y = ev.getY(newPointerIndex);
            this.mLastTouchY = y;
            this.mInitialTouchY = y;
            this.mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private void resetTouch() {
        this.mActivePointerId = -1;
        this.mIsDragging = false;
        this.mOpenOnClick = false;
        this.mLastTouchY = 0.0f;
        this.mInitialTouchY = 0.0f;
        this.mInitialTouchX = 0.0f;
        this.mVelocityTracker.clear();
    }

    private void dismiss() {
        this.mRunOnDismissedListener = new RunOnDismissedListener(this, null);
        post(this.mRunOnDismissedListener);
    }

    public void computeScroll() {
        super.computeScroll();
        if (this.mScroller.computeScrollOffset()) {
            boolean keepGoing = this.mScroller.isFinished() ^ 1;
            performDrag(((float) this.mScroller.getCurrY()) - this.mCollapseOffset);
            if (keepGoing) {
                postInvalidateOnAnimation();
            } else if (this.mDismissOnScrollerFinished && this.mOnDismissedListener != null) {
                dismiss();
            }
        }
    }

    private void abortAnimation() {
        this.mScroller.abortAnimation();
        this.mRunOnDismissedListener = null;
        this.mDismissOnScrollerFinished = false;
    }

    private float performDrag(float dy) {
        if (getShowAtTop()) {
            return 0.0f;
        }
        float newPos = Math.max(0.0f, Math.min(this.mCollapseOffset + dy, (float) (this.mCollapsibleHeight + this.mUncollapsibleHeight)));
        float f = this.mCollapseOffset;
        if (newPos == f) {
            return 0.0f;
        }
        dy = newPos - f;
        this.mDragRemainder += dy - ((float) ((int) dy));
        f = this.mDragRemainder;
        if (f >= 1.0f) {
            this.mDragRemainder = f - 1.0f;
            dy += 1.0f;
        } else if (f <= -1.0f) {
            this.mDragRemainder = f + 1.0f;
            dy -= 1.0f;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (!((LayoutParams) child.getLayoutParams()).ignoreOffset) {
                child.offsetTopAndBottom((int) dy);
            }
        }
        int i2 = 1;
        boolean isCollapsedOld = this.mCollapseOffset != 0.0f;
        this.mCollapseOffset = newPos;
        this.mTopOffset = (int) (((float) this.mTopOffset) + dy);
        boolean isCollapsedNew = newPos != 0.0f;
        if (isCollapsedOld != isCollapsedNew) {
            onCollapsedChanged(isCollapsedNew);
            MetricsLogger metricsLogger = getMetricsLogger();
            LogMaker logMaker = new LogMaker((int) MetricsEvent.ACTION_SHARESHEET_COLLAPSED_CHANGED);
            if (!isCollapsedNew) {
                i2 = 0;
            }
            metricsLogger.write(logMaker.setSubtype(i2));
        }
        onScrollChanged(0, (int) newPos, 0, (int) (newPos - dy));
        postInvalidateOnAnimation();
        return dy;
    }

    private void onCollapsedChanged(boolean isCollapsed) {
        notifyViewAccessibilityStateChangedIfNeeded(0);
        if (this.mScrollIndicatorDrawable != null) {
            setWillNotDraw(isCollapsed ^ 1);
        }
        OnCollapsedChangedListener onCollapsedChangedListener = this.mOnCollapsedChangedListener;
        if (onCollapsedChangedListener != null) {
            onCollapsedChangedListener.onCollapsedChanged(isCollapsed);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnDismissed() {
        OnDismissedListener onDismissedListener = this.mOnDismissedListener;
        if (onDismissedListener != null) {
            onDismissedListener.onDismissed();
        }
        RunOnDismissedListener runOnDismissedListener = this.mRunOnDismissedListener;
        if (runOnDismissedListener != null) {
            removeCallbacks(runOnDismissedListener);
            this.mRunOnDismissedListener = null;
        }
    }

    private void smoothScrollTo(int yOffset, float velocity) {
        abortAnimation();
        int sy = (int) this.mCollapseOffset;
        int dy = yOffset - sy;
        if (dy != 0) {
            int duration;
            int height = getHeight();
            int halfHeight = height / 2;
            float distance = ((float) halfHeight) + (((float) halfHeight) * distanceInfluenceForSnapDuration(Math.min(1.0f, (((float) Math.abs(dy)) * 1.0f) / ((float) height))));
            velocity = Math.abs(velocity);
            if (velocity > 0.0f) {
                duration = Math.round(Math.abs(distance / velocity) * 1000.0f) * 4;
            } else {
                duration = (int) ((1.0f + (((float) Math.abs(dy)) / ((float) height))) * 100.0f);
            }
            this.mScroller.startScroll(0, sy, 0, dy, Math.min(duration, 300));
            postInvalidateOnAnimation();
        }
    }

    private float distanceInfluenceForSnapDuration(float f) {
        return (float) Math.sin((double) ((float) (((double) (f - 0.5f)) * 0.4712389167638204d)));
    }

    private View findChildUnder(float x, float y) {
        return findChildUnder(this, x, y);
    }

    private static View findChildUnder(ViewGroup parent, float x, float y) {
        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            View child = parent.getChildAt(i);
            if (isChildUnder(child, x, y)) {
                return child;
            }
        }
        return null;
    }

    private View findListChildUnder(float x, float y) {
        View v = findChildUnder(x, y);
        while (v != null) {
            x -= v.getX();
            y -= v.getY();
            if (v instanceof AbsListView) {
                return findChildUnder((ViewGroup) v, x, y);
            }
            v = v instanceof ViewGroup ? findChildUnder((ViewGroup) v, x, y) : null;
        }
        return v;
    }

    private boolean isListChildUnderClipped(float x, float y) {
        View listChild = findListChildUnder(x, y);
        return listChild != null && isDescendantClipped(listChild);
    }

    private boolean isDescendantClipped(View child) {
        View directChild;
        this.mTempRect.set(0, 0, child.getWidth(), child.getHeight());
        offsetDescendantRectToMyCoords(child, this.mTempRect);
        if (child.getParent() == this) {
            directChild = child;
        } else {
            directChild = child;
            View p = child.getParent();
            while (p != this) {
                directChild = p;
                p = directChild.getParent();
            }
        }
        int clipEdge = getHeight() - getPaddingBottom();
        int childCount = getChildCount();
        for (int i = indexOfChild(directChild) + 1; i < childCount; i++) {
            View nextChild = getChildAt(i);
            if (nextChild.getVisibility() != 8) {
                clipEdge = Math.min(clipEdge, nextChild.getTop());
            }
        }
        if (this.mTempRect.bottom > clipEdge) {
            return true;
        }
        return false;
    }

    private static boolean isChildUnder(View child, float x, float y) {
        float left = child.getX();
        float top = child.getY();
        return x >= left && y >= top && x < ((float) child.getWidth()) + left && y < ((float) child.getHeight()) + top;
    }

    public void requestChildFocus(View child, View focused) {
        super.requestChildFocus(child, focused);
        if (!isInTouchMode() && isDescendantClipped(focused)) {
            smoothScrollTo(0, 0.0f);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnTouchModeChangeListener(this.mTouchModeChangeListener);
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnTouchModeChangeListener(this.mTouchModeChangeListener);
        abortAnimation();
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        if ((nestedScrollAxes & 2) == 0) {
            return false;
        }
        if (child instanceof AbsListView) {
            this.mNestedScrollingChild = (AbsListView) child;
        }
        return true;
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
    }

    public void onStopNestedScroll(View child) {
        super.onStopNestedScroll(child);
        if (this.mScroller.isFinished()) {
            float f = this.mCollapseOffset;
            int i = this.mCollapsibleHeight;
            if (f < ((float) (i / 2))) {
                i = 0;
            }
            smoothScrollTo(i, 0.0f);
        }
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyUnconsumed < 0) {
            performDrag((float) (-dyUnconsumed));
        }
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        if (dy > 0) {
            consumed[1] = (int) (-performDrag((float) (-dy)));
        }
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        if (getShowAtTop() || velocityY <= this.mMinFlingVelocity || this.mCollapseOffset == 0.0f) {
            return false;
        }
        smoothScrollTo(0, velocityY);
        return true;
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        int i = 0;
        if (consumed || Math.abs(velocityY) <= this.mMinFlingVelocity) {
            return false;
        }
        if (!getShowAtTop()) {
            if (isDismissable() && velocityY < 0.0f) {
                float f = this.mCollapseOffset;
                int i2 = this.mCollapsibleHeight;
                if (f > ((float) i2)) {
                    smoothScrollTo(i2 + this.mUncollapsibleHeight, velocityY);
                    this.mDismissOnScrollerFinished = true;
                }
            }
            if (velocityY <= 0.0f) {
                i = this.mCollapsibleHeight;
            }
            smoothScrollTo(i, velocityY);
        } else if (!isDismissable() || velocityY <= 0.0f) {
            if (velocityY < 0.0f) {
                i = this.mCollapsibleHeight;
            }
            smoothScrollTo(i, velocityY);
        } else {
            abortAnimation();
            dismiss();
        }
        return true;
    }

    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        if (super.onNestedPrePerformAccessibilityAction(target, action, args)) {
            return true;
        }
        if (action != 4096 || this.mCollapseOffset == 0.0f) {
            return false;
        }
        smoothScrollTo(0, 0.0f);
        return true;
    }

    public CharSequence getAccessibilityClassName() {
        return ScrollView.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        if (isEnabled() && this.mCollapseOffset != 0.0f) {
            info.addAction(4096);
            info.setScrollable(true);
        }
        info.removeAction(AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS);
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (action == AccessibilityAction.ACTION_ACCESSIBILITY_FOCUS.getId()) {
            return false;
        }
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        if (action != 4096 || this.mCollapseOffset == 0.0f) {
            return false;
        }
        smoothScrollTo(0, 0.0f);
        return true;
    }

    public void onDrawForeground(Canvas canvas) {
        Drawable drawable = this.mScrollIndicatorDrawable;
        if (drawable != null) {
            drawable.draw(canvas);
        }
        super.onDrawForeground(canvas);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize;
        int i;
        int i2;
        int i3;
        int i4;
        View child;
        LayoutParams lp;
        int sourceWidth = MeasureSpec.getSize(widthMeasureSpec);
        int widthSize2 = sourceWidth;
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int i5 = this.mMaxWidth;
        if (i5 >= 0) {
            widthSize = Math.min(widthSize2, i5);
        } else {
            widthSize = widthSize2;
        }
        int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, 1073741824);
        int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, 1073741824);
        int childCount = getChildCount();
        int heightUsed = 0;
        int i6 = 0;
        while (true) {
            i = Integer.MIN_VALUE;
            i2 = -1;
            i3 = 8;
            i4 = 0;
            if (i6 >= childCount) {
                break;
            }
            child = getChildAt(i6);
            lp = (LayoutParams) child.getLayoutParams();
            if (!lp.alwaysShow || child.getVisibility() == 8) {
            } else {
                if (lp.maxHeight != -1) {
                    i2 = heightSize - heightUsed;
                    measureChildWithMargins(child, widthSpec, 0, MeasureSpec.makeMeasureSpec(lp.maxHeight, Integer.MIN_VALUE), lp.maxHeight > i2 ? lp.maxHeight - i2 : 0);
                } else {
                    measureChildWithMargins(child, widthSpec, 0, heightSpec, heightUsed);
                }
                heightUsed += child.getMeasuredHeight();
            }
            i6++;
        }
        this.mAlwaysShowHeight = heightUsed;
        i6 = 0;
        while (i6 < childCount) {
            int i7;
            int i8;
            child = getChildAt(i6);
            lp = (LayoutParams) child.getLayoutParams();
            if (lp.alwaysShow || child.getVisibility() == i3) {
                i = i4;
                i7 = i3;
                i8 = i2;
            } else {
                if (lp.maxHeight != i2) {
                    i5 = heightSize - heightUsed;
                    i8 = MeasureSpec.makeMeasureSpec(lp.maxHeight, i);
                    i = i4;
                    i7 = i3;
                    i3 = i8;
                    i8 = i2;
                    measureChildWithMargins(child, widthSpec, 0, i3, lp.maxHeight > i5 ? lp.maxHeight - i5 : i4);
                } else {
                    i = i4;
                    i7 = i3;
                    i8 = i2;
                    measureChildWithMargins(child, widthSpec, 0, heightSpec, heightUsed);
                }
                heightUsed += child.getMeasuredHeight();
            }
            i6++;
            i4 = i;
            i3 = i7;
            i2 = i8;
            i = Integer.MIN_VALUE;
        }
        i = i4;
        widthSize2 = this.mCollapsibleHeight;
        this.mCollapsibleHeight = Math.max(i, (heightUsed - this.mAlwaysShowHeight) - getMaxCollapsedHeight());
        this.mUncollapsibleHeight = heightUsed - this.mCollapsibleHeight;
        updateCollapseOffset(widthSize2, isDragging() ^ 1);
        if (getShowAtTop()) {
            this.mTopOffset = i;
        } else {
            this.mTopOffset = Math.max(i, heightSize - heightUsed) + ((int) this.mCollapseOffset);
        }
        setMeasuredDimension(sourceWidth, heightSize);
    }

    public int getAlwaysShowHeight() {
        return this.mAlwaysShowHeight;
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        View indicatorHost = null;
        int ypos = this.mTopOffset;
        int leftEdge = getPaddingLeft();
        int rightEdge = width - getPaddingRight();
        int childCount = getChildCount();
        int i = 0;
        while (i < childCount) {
            int width2;
            View child = getChildAt(i);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp.hasNestedScrollIndicator) {
                indicatorHost = child;
            }
            if (child.getVisibility() == 8) {
                width2 = width;
            } else {
                int top = lp.topMargin + ypos;
                if (lp.ignoreOffset) {
                    top = (int) (((float) top) - this.mCollapseOffset);
                }
                int bottom = child.getMeasuredHeight() + top;
                int childWidth = child.getMeasuredWidth();
                int left = (((rightEdge - leftEdge) - childWidth) / 2) + leftEdge;
                child.layout(left, top, left + childWidth, bottom);
                width2 = width;
                ypos = lp.bottomMargin + bottom;
            }
            i++;
            width = width2;
        }
        if (this.mScrollIndicatorDrawable == null) {
            return;
        }
        if (indicatorHost != null) {
            i = indicatorHost.getLeft();
            int right = indicatorHost.getRight();
            int bottom2 = indicatorHost.getTop();
            this.mScrollIndicatorDrawable.setBounds(i, bottom2 - this.mScrollIndicatorDrawable.getIntrinsicHeight(), right, bottom2);
            setWillNotDraw(1 ^ isCollapsed());
            return;
        }
        this.mScrollIndicatorDrawable = null;
        setWillNotDraw(true);
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) p);
        }
        if (p instanceof MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams) p);
        }
        return new LayoutParams(p);
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        SavedState ss = new SavedState(super.onSaveInstanceState());
        boolean z = this.mCollapsibleHeight > 0 && this.mCollapseOffset == 0.0f;
        ss.open = z;
        return ss;
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        this.mOpenOnLayout = ss.open;
    }

    private MetricsLogger getMetricsLogger() {
        if (this.mMetricsLogger == null) {
            this.mMetricsLogger = new MetricsLogger();
        }
        return this.mMetricsLogger;
    }
}
