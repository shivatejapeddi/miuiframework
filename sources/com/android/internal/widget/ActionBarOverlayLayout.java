package com.android.internal.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.IntProperty;
import android.util.Log;
import android.util.Property;
import android.util.SparseArray;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewPropertyAnimator;
import android.view.Window.Callback;
import android.view.WindowInsets;
import android.widget.OverScroller;
import android.widget.Toolbar;
import com.android.internal.R;
import com.android.internal.view.menu.MenuPresenter;

public class ActionBarOverlayLayout extends ViewGroup implements DecorContentParent {
    public static final Property<ActionBarOverlayLayout, Integer> ACTION_BAR_HIDE_OFFSET = new IntProperty<ActionBarOverlayLayout>("actionBarHideOffset") {
        public void setValue(ActionBarOverlayLayout object, int value) {
            object.setActionBarHideOffset(value);
        }

        public Integer get(ActionBarOverlayLayout object) {
            return Integer.valueOf(object.getActionBarHideOffset());
        }
    };
    static final int[] ATTRS = new int[]{16843499, 16842841};
    private static final String TAG = "ActionBarOverlayLayout";
    private final int ACTION_BAR_ANIMATE_DELAY = 600;
    private ActionBarContainer mActionBarBottom;
    private int mActionBarHeight;
    private ActionBarContainer mActionBarTop;
    private ActionBarVisibilityCallback mActionBarVisibilityCallback;
    private final Runnable mAddActionBarHideOffset = new Runnable() {
        public void run() {
            ActionBarOverlayLayout.this.haltActionBarHideOffsetAnimations();
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.mCurrentActionBarTopAnimator = actionBarOverlayLayout.mActionBarTop.animate().translationY((float) (-ActionBarOverlayLayout.this.mActionBarTop.getHeight())).setListener(ActionBarOverlayLayout.this.mTopAnimatorListener);
            if (ActionBarOverlayLayout.this.mActionBarBottom != null && ActionBarOverlayLayout.this.mActionBarBottom.getVisibility() != 8) {
                actionBarOverlayLayout = ActionBarOverlayLayout.this;
                actionBarOverlayLayout.mCurrentActionBarBottomAnimator = actionBarOverlayLayout.mActionBarBottom.animate().translationY((float) ActionBarOverlayLayout.this.mActionBarBottom.getHeight()).setListener(ActionBarOverlayLayout.this.mBottomAnimatorListener);
            }
        }
    };
    private boolean mAnimatingForFling;
    private final Rect mBaseContentInsets = new Rect();
    private WindowInsets mBaseInnerInsets = WindowInsets.CONSUMED;
    private final AnimatorListener mBottomAnimatorListener = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animation) {
            ActionBarOverlayLayout.this.mCurrentActionBarBottomAnimator = null;
            ActionBarOverlayLayout.this.mAnimatingForFling = false;
        }

        public void onAnimationCancel(Animator animation) {
            ActionBarOverlayLayout.this.mCurrentActionBarBottomAnimator = null;
            ActionBarOverlayLayout.this.mAnimatingForFling = false;
        }
    };
    private View mContent;
    private final Rect mContentInsets = new Rect();
    private ViewPropertyAnimator mCurrentActionBarBottomAnimator;
    private ViewPropertyAnimator mCurrentActionBarTopAnimator;
    private DecorToolbar mDecorToolbar;
    private OverScroller mFlingEstimator;
    private boolean mHasNonEmbeddedTabs;
    private boolean mHideOnContentScroll;
    private int mHideOnContentScrollReference;
    private boolean mIgnoreWindowContentOverlay;
    private WindowInsets mInnerInsets = WindowInsets.CONSUMED;
    private final Rect mLastBaseContentInsets = new Rect();
    private WindowInsets mLastBaseInnerInsets = WindowInsets.CONSUMED;
    private WindowInsets mLastInnerInsets = WindowInsets.CONSUMED;
    private int mLastSystemUiVisibility;
    private boolean mOverlayMode;
    private final Runnable mRemoveActionBarHideOffset = new Runnable() {
        public void run() {
            ActionBarOverlayLayout.this.haltActionBarHideOffsetAnimations();
            ActionBarOverlayLayout actionBarOverlayLayout = ActionBarOverlayLayout.this;
            actionBarOverlayLayout.mCurrentActionBarTopAnimator = actionBarOverlayLayout.mActionBarTop.animate().translationY(0.0f).setListener(ActionBarOverlayLayout.this.mTopAnimatorListener);
            if (ActionBarOverlayLayout.this.mActionBarBottom != null && ActionBarOverlayLayout.this.mActionBarBottom.getVisibility() != 8) {
                actionBarOverlayLayout = ActionBarOverlayLayout.this;
                actionBarOverlayLayout.mCurrentActionBarBottomAnimator = actionBarOverlayLayout.mActionBarBottom.animate().translationY(0.0f).setListener(ActionBarOverlayLayout.this.mBottomAnimatorListener);
            }
        }
    };
    private final AnimatorListener mTopAnimatorListener = new AnimatorListenerAdapter() {
        public void onAnimationEnd(Animator animation) {
            ActionBarOverlayLayout.this.mCurrentActionBarTopAnimator = null;
            ActionBarOverlayLayout.this.mAnimatingForFling = false;
        }

        public void onAnimationCancel(Animator animation) {
            ActionBarOverlayLayout.this.mCurrentActionBarTopAnimator = null;
            ActionBarOverlayLayout.this.mAnimatingForFling = false;
        }
    };
    private Drawable mWindowContentOverlay;
    private int mWindowVisibility = 0;

    public interface ActionBarVisibilityCallback {
        void enableContentAnimations(boolean z);

        void hideForSystem();

        void onContentScrollStarted();

        void onContentScrollStopped();

        void onWindowVisibilityChanged(int i);

        void showForSystem();
    }

    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }
    }

    public ActionBarOverlayLayout(Context context) {
        super(context);
        init(context);
    }

    @UnsupportedAppUsage
    public ActionBarOverlayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(ATTRS);
        boolean z = false;
        this.mActionBarHeight = ta.getDimensionPixelSize(0, 0);
        this.mWindowContentOverlay = ta.getDrawable(1);
        setWillNotDraw(this.mWindowContentOverlay == null);
        ta.recycle();
        if (context.getApplicationInfo().targetSdkVersion < 19) {
            z = true;
        }
        this.mIgnoreWindowContentOverlay = z;
        this.mFlingEstimator = new OverScroller(context);
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        haltActionBarHideOffsetAnimations();
    }

    public void setActionBarVisibilityCallback(ActionBarVisibilityCallback cb) {
        this.mActionBarVisibilityCallback = cb;
        if (getWindowToken() != null) {
            this.mActionBarVisibilityCallback.onWindowVisibilityChanged(this.mWindowVisibility);
            if (this.mLastSystemUiVisibility != 0) {
                onWindowSystemUiVisibilityChanged(this.mLastSystemUiVisibility);
                requestApplyInsets();
            }
        }
    }

    public void setOverlayMode(boolean overlayMode) {
        this.mOverlayMode = overlayMode;
        boolean z = overlayMode && getContext().getApplicationInfo().targetSdkVersion < 19;
        this.mIgnoreWindowContentOverlay = z;
    }

    public boolean isInOverlayMode() {
        return this.mOverlayMode;
    }

    public void setHasNonEmbeddedTabs(boolean hasNonEmbeddedTabs) {
        this.mHasNonEmbeddedTabs = hasNonEmbeddedTabs;
    }

    public void setShowingForActionMode(boolean showing) {
        if (!showing) {
            setDisabledSystemUiVisibility(0);
        } else if ((getWindowSystemUiVisibility() & 1280) == 1280) {
            setDisabledSystemUiVisibility(4);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        init(getContext());
        requestApplyInsets();
    }

    public void onWindowSystemUiVisibilityChanged(int visible) {
        super.onWindowSystemUiVisibilityChanged(visible);
        pullChildren();
        int diff = this.mLastSystemUiVisibility ^ visible;
        this.mLastSystemUiVisibility = visible;
        boolean z = true;
        boolean barVisible = (visible & 4) == 0;
        boolean stable = (visible & 256) != 0;
        ActionBarVisibilityCallback actionBarVisibilityCallback = this.mActionBarVisibilityCallback;
        if (actionBarVisibilityCallback != null) {
            if (stable) {
                z = false;
            }
            actionBarVisibilityCallback.enableContentAnimations(z);
            if (barVisible || !stable) {
                this.mActionBarVisibilityCallback.showForSystem();
            } else {
                this.mActionBarVisibilityCallback.hideForSystem();
            }
        }
        if ((diff & 256) != 0 && this.mActionBarVisibilityCallback != null) {
            requestApplyInsets();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mWindowVisibility = visibility;
        ActionBarVisibilityCallback actionBarVisibilityCallback = this.mActionBarVisibilityCallback;
        if (actionBarVisibilityCallback != null) {
            actionBarVisibilityCallback.onWindowVisibilityChanged(visibility);
        }
    }

    private boolean applyInsets(View view, Rect insets, boolean left, boolean top, boolean bottom, boolean right) {
        boolean changed = false;
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        if (left && lp.leftMargin != insets.left) {
            changed = true;
            lp.leftMargin = insets.left;
        }
        if (top && lp.topMargin != insets.top) {
            changed = true;
            lp.topMargin = insets.top;
        }
        if (right && lp.rightMargin != insets.right) {
            changed = true;
            lp.rightMargin = insets.right;
        }
        if (!bottom || lp.bottomMargin == insets.bottom) {
            return changed;
        }
        lp.bottomMargin = insets.bottom;
        return true;
    }

    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        pullChildren();
        int vis = getWindowSystemUiVisibility();
        Rect systemInsets = insets.getSystemWindowInsetsAsRect();
        boolean changed = applyInsets(this.mActionBarTop, systemInsets, true, true, false, true);
        ActionBarContainer actionBarContainer = this.mActionBarBottom;
        if (actionBarContainer != null) {
            changed |= applyInsets(actionBarContainer, systemInsets, true, false, true, true);
        }
        computeSystemWindowInsets(insets, this.mBaseContentInsets);
        this.mBaseInnerInsets = insets.inset(this.mBaseContentInsets);
        if (!this.mLastBaseInnerInsets.equals(this.mBaseInnerInsets)) {
            changed = true;
            this.mLastBaseInnerInsets = this.mBaseInnerInsets;
        }
        if (!this.mLastBaseContentInsets.equals(this.mBaseContentInsets)) {
            changed = true;
            this.mLastBaseContentInsets.set(this.mBaseContentInsets);
        }
        if (changed) {
            requestLayout();
        }
        return WindowInsets.CONSUMED;
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int maxHeight;
        WindowInsets windowInsets;
        pullChildren();
        int topInset = 0;
        int bottomInset = 0;
        measureChildWithMargins(this.mActionBarTop, widthMeasureSpec, 0, heightMeasureSpec, 0);
        LayoutParams lp = (LayoutParams) this.mActionBarTop.getLayoutParams();
        int maxWidth = Math.max(0, (this.mActionBarTop.getMeasuredWidth() + lp.leftMargin) + lp.rightMargin);
        int maxHeight2 = Math.max(0, (this.mActionBarTop.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin);
        int childState = View.combineMeasuredStates(0, this.mActionBarTop.getMeasuredState());
        ActionBarContainer actionBarContainer = this.mActionBarBottom;
        if (actionBarContainer != null) {
            measureChildWithMargins(actionBarContainer, widthMeasureSpec, 0, heightMeasureSpec, 0);
            lp = (LayoutParams) this.mActionBarBottom.getLayoutParams();
            maxWidth = Math.max(maxWidth, (this.mActionBarBottom.getMeasuredWidth() + lp.leftMargin) + lp.rightMargin);
            maxHeight2 = Math.max(maxHeight2, (this.mActionBarBottom.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin);
            childState = View.combineMeasuredStates(childState, this.mActionBarBottom.getMeasuredState());
            maxHeight = maxHeight2;
        } else {
            maxHeight = maxHeight2;
        }
        boolean stable = (getWindowSystemUiVisibility() & 256) != 0;
        if (stable) {
            topInset = this.mActionBarHeight;
            if (this.mHasNonEmbeddedTabs && this.mActionBarTop.getTabContainer() != null) {
                topInset += this.mActionBarHeight;
            }
        } else if (this.mActionBarTop.getVisibility() != 8) {
            topInset = this.mActionBarTop.getMeasuredHeight();
        }
        if (this.mDecorToolbar.isSplit()) {
            ActionBarContainer actionBarContainer2 = this.mActionBarBottom;
            if (actionBarContainer2 != null) {
                bottomInset = stable ? this.mActionBarHeight : actionBarContainer2.getMeasuredHeight();
            }
        }
        this.mContentInsets.set(this.mBaseContentInsets);
        this.mInnerInsets = this.mBaseInnerInsets;
        if (this.mOverlayMode || stable) {
            windowInsets = this.mInnerInsets;
            this.mInnerInsets = windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), this.mInnerInsets.getSystemWindowInsetTop() + topInset, this.mInnerInsets.getSystemWindowInsetRight(), this.mInnerInsets.getSystemWindowInsetBottom() + bottomInset);
        } else {
            Rect rect = this.mContentInsets;
            rect.top += topInset;
            rect = this.mContentInsets;
            rect.bottom += bottomInset;
            this.mInnerInsets = this.mInnerInsets.inset(0, topInset, 0, bottomInset);
        }
        applyInsets(this.mContent, this.mContentInsets, true, true, true, true);
        if (!this.mLastInnerInsets.equals(this.mInnerInsets)) {
            windowInsets = this.mInnerInsets;
            this.mLastInnerInsets = windowInsets;
            this.mContent.dispatchApplyWindowInsets(windowInsets);
        }
        measureChildWithMargins(this.mContent, widthMeasureSpec, 0, heightMeasureSpec, 0);
        LayoutParams lp2 = (LayoutParams) this.mContent.getLayoutParams();
        int maxWidth2 = Math.max(maxWidth, (this.mContent.getMeasuredWidth() + lp2.leftMargin) + lp2.rightMargin);
        int maxHeight3 = Math.max(maxHeight, (this.mContent.getMeasuredHeight() + lp2.topMargin) + lp2.bottomMargin);
        int childState2 = View.combineMeasuredStates(childState, this.mContent.getMeasuredState());
        setMeasuredDimension(View.resolveSizeAndState(Math.max(maxWidth2 + (getPaddingLeft() + getPaddingRight()), getSuggestedMinimumWidth()), widthMeasureSpec, childState2), View.resolveSizeAndState(Math.max(maxHeight3 + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight()), heightMeasureSpec, childState2 << 16));
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        int parentLeft = getPaddingLeft();
        int parentRight = (right - left) - getPaddingRight();
        int parentTop = getPaddingTop();
        int parentBottom = (bottom - top) - getPaddingBottom();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                int childTop;
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int childLeft = lp.leftMargin + parentLeft;
                if (child == this.mActionBarBottom) {
                    childTop = (parentBottom - height) - lp.bottomMargin;
                } else {
                    childTop = lp.topMargin + parentTop;
                }
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
        }
    }

    public void draw(Canvas c) {
        super.draw(c);
        if (this.mWindowContentOverlay != null && !this.mIgnoreWindowContentOverlay) {
            int top = this.mActionBarTop.getVisibility() == 0 ? (int) ((((float) this.mActionBarTop.getBottom()) + this.mActionBarTop.getTranslationY()) + 0.5f) : 0;
            this.mWindowContentOverlay.setBounds(0, top, getWidth(), this.mWindowContentOverlay.getIntrinsicHeight() + top);
            this.mWindowContentOverlay.draw(c);
        }
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public boolean onStartNestedScroll(View child, View target, int axes) {
        if ((axes & 2) == 0 || this.mActionBarTop.getVisibility() != 0) {
            return false;
        }
        return this.mHideOnContentScroll;
    }

    public void onNestedScrollAccepted(View child, View target, int axes) {
        super.onNestedScrollAccepted(child, target, axes);
        this.mHideOnContentScrollReference = getActionBarHideOffset();
        haltActionBarHideOffsetAnimations();
        ActionBarVisibilityCallback actionBarVisibilityCallback = this.mActionBarVisibilityCallback;
        if (actionBarVisibilityCallback != null) {
            actionBarVisibilityCallback.onContentScrollStarted();
        }
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        this.mHideOnContentScrollReference += dyConsumed;
        setActionBarHideOffset(this.mHideOnContentScrollReference);
    }

    public void onStopNestedScroll(View target) {
        super.onStopNestedScroll(target);
        if (this.mHideOnContentScroll && !this.mAnimatingForFling) {
            if (this.mHideOnContentScrollReference <= this.mActionBarTop.getHeight()) {
                postRemoveActionBarHideOffset();
            } else {
                postAddActionBarHideOffset();
            }
        }
        ActionBarVisibilityCallback actionBarVisibilityCallback = this.mActionBarVisibilityCallback;
        if (actionBarVisibilityCallback != null) {
            actionBarVisibilityCallback.onContentScrollStopped();
        }
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        if (!this.mHideOnContentScroll || !consumed) {
            return false;
        }
        if (shouldHideActionBarOnFling(velocityX, velocityY)) {
            addActionBarHideOffset();
        } else {
            removeActionBarHideOffset();
        }
        this.mAnimatingForFling = true;
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void pullChildren() {
        if (this.mContent == null) {
            this.mContent = findViewById(16908290);
            this.mActionBarTop = (ActionBarContainer) findViewById(R.id.action_bar_container);
            this.mDecorToolbar = getDecorToolbar(findViewById(R.id.action_bar));
            this.mActionBarBottom = (ActionBarContainer) findViewById(R.id.split_action_bar);
        }
    }

    private DecorToolbar getDecorToolbar(View view) {
        if (view instanceof DecorToolbar) {
            return (DecorToolbar) view;
        }
        if (view instanceof Toolbar) {
            return ((Toolbar) view).getWrapper();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Can't make a decor toolbar out of ");
        stringBuilder.append(view.getClass().getSimpleName());
        throw new IllegalStateException(stringBuilder.toString());
    }

    public void setHideOnContentScrollEnabled(boolean hideOnContentScroll) {
        if (hideOnContentScroll != this.mHideOnContentScroll) {
            this.mHideOnContentScroll = hideOnContentScroll;
            if (!hideOnContentScroll) {
                stopNestedScroll();
                haltActionBarHideOffsetAnimations();
                setActionBarHideOffset(0);
            }
        }
    }

    public boolean isHideOnContentScrollEnabled() {
        return this.mHideOnContentScroll;
    }

    public int getActionBarHideOffset() {
        ActionBarContainer actionBarContainer = this.mActionBarTop;
        return actionBarContainer != null ? -((int) actionBarContainer.getTranslationY()) : 0;
    }

    public void setActionBarHideOffset(int offset) {
        haltActionBarHideOffsetAnimations();
        int topHeight = this.mActionBarTop.getHeight();
        offset = Math.max(0, Math.min(offset, topHeight));
        this.mActionBarTop.setTranslationY((float) (-offset));
        ActionBarContainer actionBarContainer = this.mActionBarBottom;
        if (actionBarContainer != null && actionBarContainer.getVisibility() != 8) {
            this.mActionBarBottom.setTranslationY((float) ((int) (((float) this.mActionBarBottom.getHeight()) * (((float) offset) / ((float) topHeight)))));
        }
    }

    private void haltActionBarHideOffsetAnimations() {
        removeCallbacks(this.mRemoveActionBarHideOffset);
        removeCallbacks(this.mAddActionBarHideOffset);
        ViewPropertyAnimator viewPropertyAnimator = this.mCurrentActionBarTopAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        viewPropertyAnimator = this.mCurrentActionBarBottomAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
    }

    private void postRemoveActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        postDelayed(this.mRemoveActionBarHideOffset, 600);
    }

    private void postAddActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        postDelayed(this.mAddActionBarHideOffset, 600);
    }

    private void removeActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        this.mRemoveActionBarHideOffset.run();
    }

    private void addActionBarHideOffset() {
        haltActionBarHideOffsetAnimations();
        this.mAddActionBarHideOffset.run();
    }

    private boolean shouldHideActionBarOnFling(float velocityX, float velocityY) {
        this.mFlingEstimator.fling(0, 0, 0, (int) velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        return this.mFlingEstimator.getFinalY() > this.mActionBarTop.getHeight();
    }

    @UnsupportedAppUsage
    public void setWindowCallback(Callback cb) {
        pullChildren();
        this.mDecorToolbar.setWindowCallback(cb);
    }

    public void setWindowTitle(CharSequence title) {
        pullChildren();
        this.mDecorToolbar.setWindowTitle(title);
    }

    public CharSequence getTitle() {
        pullChildren();
        return this.mDecorToolbar.getTitle();
    }

    public void initFeature(int windowFeature) {
        pullChildren();
        if (windowFeature == 2) {
            this.mDecorToolbar.initProgress();
        } else if (windowFeature == 5) {
            this.mDecorToolbar.initIndeterminateProgress();
        } else if (windowFeature == 9) {
            setOverlayMode(true);
        }
    }

    public void setUiOptions(int uiOptions) {
        boolean splitActionBar = false;
        boolean splitWhenNarrow = (uiOptions & 1) != 0;
        if (splitWhenNarrow) {
            splitActionBar = getContext().getResources().getBoolean(R.bool.split_action_bar_is_narrow);
        }
        if (splitActionBar) {
            pullChildren();
            if (this.mActionBarBottom != null && this.mDecorToolbar.canSplit()) {
                this.mDecorToolbar.setSplitView(this.mActionBarBottom);
                this.mDecorToolbar.setSplitToolbar(splitActionBar);
                this.mDecorToolbar.setSplitWhenNarrow(splitWhenNarrow);
                ActionBarContextView cab = (ActionBarContextView) findViewById(R.id.action_context_bar);
                cab.setSplitView(this.mActionBarBottom);
                cab.setSplitToolbar(splitActionBar);
                cab.setSplitWhenNarrow(splitWhenNarrow);
            } else if (splitActionBar) {
                Log.e(TAG, "Requested split action bar with incompatible window decor! Ignoring request.");
            }
        }
    }

    public boolean hasIcon() {
        pullChildren();
        return this.mDecorToolbar.hasIcon();
    }

    public boolean hasLogo() {
        pullChildren();
        return this.mDecorToolbar.hasLogo();
    }

    public void setIcon(int resId) {
        pullChildren();
        this.mDecorToolbar.setIcon(resId);
    }

    public void setIcon(Drawable d) {
        pullChildren();
        this.mDecorToolbar.setIcon(d);
    }

    public void setLogo(int resId) {
        pullChildren();
        this.mDecorToolbar.setLogo(resId);
    }

    public boolean canShowOverflowMenu() {
        pullChildren();
        return this.mDecorToolbar.canShowOverflowMenu();
    }

    public boolean isOverflowMenuShowing() {
        pullChildren();
        return this.mDecorToolbar.isOverflowMenuShowing();
    }

    public boolean isOverflowMenuShowPending() {
        pullChildren();
        return this.mDecorToolbar.isOverflowMenuShowPending();
    }

    public boolean showOverflowMenu() {
        pullChildren();
        return this.mDecorToolbar.showOverflowMenu();
    }

    public boolean hideOverflowMenu() {
        pullChildren();
        return this.mDecorToolbar.hideOverflowMenu();
    }

    public void setMenuPrepared() {
        pullChildren();
        this.mDecorToolbar.setMenuPrepared();
    }

    public void setMenu(Menu menu, MenuPresenter.Callback cb) {
        pullChildren();
        this.mDecorToolbar.setMenu(menu, cb);
    }

    public void saveToolbarHierarchyState(SparseArray<Parcelable> toolbarStates) {
        pullChildren();
        this.mDecorToolbar.saveHierarchyState(toolbarStates);
    }

    public void restoreToolbarHierarchyState(SparseArray<Parcelable> toolbarStates) {
        pullChildren();
        this.mDecorToolbar.restoreHierarchyState(toolbarStates);
    }

    public void dismissPopups() {
        pullChildren();
        this.mDecorToolbar.dismissPopupMenus();
    }
}
