package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Outline;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import com.android.internal.R;

public class ActionBarContainer extends FrameLayout {
    private View mActionBarView;
    private View mActionContextView;
    private Drawable mBackground;
    private int mHeight;
    private boolean mIsSplit;
    private boolean mIsStacked;
    private boolean mIsTransitioning;
    private Drawable mSplitBackground;
    private Drawable mStackedBackground;
    private View mTabContainer;

    private class ActionBarBackgroundDrawable extends Drawable {
        private ActionBarBackgroundDrawable() {
        }

        public void draw(Canvas canvas) {
            if (!ActionBarContainer.this.mIsSplit) {
                if (ActionBarContainer.this.mBackground != null) {
                    ActionBarContainer.this.mBackground.draw(canvas);
                }
                if (ActionBarContainer.this.mStackedBackground != null && ActionBarContainer.this.mIsStacked) {
                    ActionBarContainer.this.mStackedBackground.draw(canvas);
                }
            } else if (ActionBarContainer.this.mSplitBackground != null) {
                ActionBarContainer.this.mSplitBackground.draw(canvas);
            }
        }

        public void getOutline(Outline outline) {
            if (ActionBarContainer.this.mIsSplit) {
                if (ActionBarContainer.this.mSplitBackground != null) {
                    ActionBarContainer.this.mSplitBackground.getOutline(outline);
                }
            } else if (ActionBarContainer.this.mBackground != null) {
                ActionBarContainer.this.mBackground.getOutline(outline);
            }
        }

        public void setAlpha(int alpha) {
        }

        public void setColorFilter(ColorFilter colorFilter) {
        }

        /* JADX WARNING: Missing block: B:13:0x003b, code skipped:
            return 0;
     */
        public int getOpacity() {
            /*
            r3 = this;
            r0 = com.android.internal.widget.ActionBarContainer.this;
            r0 = r0.mIsSplit;
            r1 = 0;
            r2 = -1;
            if (r0 == 0) goto L_0x001f;
        L_0x000a:
            r0 = com.android.internal.widget.ActionBarContainer.this;
            r0 = r0.mSplitBackground;
            if (r0 == 0) goto L_0x005d;
        L_0x0012:
            r0 = com.android.internal.widget.ActionBarContainer.this;
            r0 = r0.mSplitBackground;
            r0 = r0.getOpacity();
            if (r0 != r2) goto L_0x005d;
        L_0x001e:
            return r2;
        L_0x001f:
            r0 = com.android.internal.widget.ActionBarContainer.this;
            r0 = r0.mIsStacked;
            if (r0 == 0) goto L_0x003c;
        L_0x0027:
            r0 = com.android.internal.widget.ActionBarContainer.this;
            r0 = r0.mStackedBackground;
            if (r0 == 0) goto L_0x003b;
        L_0x002f:
            r0 = com.android.internal.widget.ActionBarContainer.this;
            r0 = r0.mStackedBackground;
            r0 = r0.getOpacity();
            if (r0 == r2) goto L_0x003c;
        L_0x003b:
            return r1;
        L_0x003c:
            r0 = com.android.internal.widget.ActionBarContainer.this;
            r0 = r0.mActionBarView;
            r0 = com.android.internal.widget.ActionBarContainer.isCollapsed(r0);
            if (r0 != 0) goto L_0x005d;
        L_0x0048:
            r0 = com.android.internal.widget.ActionBarContainer.this;
            r0 = r0.mBackground;
            if (r0 == 0) goto L_0x005d;
        L_0x0050:
            r0 = com.android.internal.widget.ActionBarContainer.this;
            r0 = r0.mBackground;
            r0 = r0.getOpacity();
            if (r0 != r2) goto L_0x005d;
        L_0x005c:
            return r2;
        L_0x005d:
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.ActionBarContainer$ActionBarBackgroundDrawable.getOpacity():int");
        }
    }

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    public ActionBarContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackground(new ActionBarBackgroundDrawable());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionBar);
        this.mBackground = a.getDrawable(2);
        this.mStackedBackground = a.getDrawable(18);
        this.mHeight = a.getDimensionPixelSize(4, -1);
        boolean z = true;
        if (getId() == R.id.split_action_bar) {
            this.mIsSplit = true;
            this.mSplitBackground = a.getDrawable(19);
        }
        a.recycle();
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                z = false;
            }
        } else if (!(this.mBackground == null && this.mStackedBackground == null)) {
            z = false;
        }
        setWillNotDraw(z);
    }

    public void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarView = findViewById(R.id.action_bar);
        this.mActionContextView = findViewById(R.id.action_context_bar);
    }

    public void setPrimaryBackground(Drawable bg) {
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setCallback(null);
            unscheduleDrawable(this.mBackground);
        }
        this.mBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            View view = this.mActionBarView;
            if (view != null) {
                this.mBackground.setBounds(view.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
            }
        }
        boolean z = true;
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                z = false;
            }
        } else if (!(this.mBackground == null && this.mStackedBackground == null)) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setStackedBackground(Drawable bg) {
        Drawable drawable = this.mStackedBackground;
        if (drawable != null) {
            drawable.setCallback(null);
            unscheduleDrawable(this.mStackedBackground);
        }
        this.mStackedBackground = bg;
        if (bg != null) {
            bg.setCallback(this);
            if (this.mIsStacked) {
                drawable = this.mStackedBackground;
                if (drawable != null) {
                    drawable.setBounds(this.mTabContainer.getLeft(), this.mTabContainer.getTop(), this.mTabContainer.getRight(), this.mTabContainer.getBottom());
                }
            }
        }
        boolean z = true;
        if (this.mIsSplit) {
            if (this.mSplitBackground != null) {
                z = false;
            }
        } else if (!(this.mBackground == null && this.mStackedBackground == null)) {
            z = false;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setSplitBackground(Drawable bg) {
        Drawable drawable = this.mSplitBackground;
        if (drawable != null) {
            drawable.setCallback(null);
            unscheduleDrawable(this.mSplitBackground);
        }
        this.mSplitBackground = bg;
        boolean z = false;
        if (bg != null) {
            bg.setCallback(this);
            if (this.mIsSplit) {
                Drawable drawable2 = this.mSplitBackground;
                if (drawable2 != null) {
                    drawable2.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                }
            }
        }
        if (this.mIsSplit) {
            if (this.mSplitBackground == null) {
                z = true;
            }
        } else if (this.mBackground == null && this.mStackedBackground == null) {
            z = true;
        }
        setWillNotDraw(z);
        invalidate();
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        boolean isVisible = visibility == 0;
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setVisible(isVisible, false);
        }
        drawable = this.mStackedBackground;
        if (drawable != null) {
            drawable.setVisible(isVisible, false);
        }
        drawable = this.mSplitBackground;
        if (drawable != null) {
            drawable.setVisible(isVisible, false);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean verifyDrawable(Drawable who) {
        return (who == this.mBackground && !this.mIsSplit) || ((who == this.mStackedBackground && this.mIsStacked) || ((who == this.mSplitBackground && this.mIsSplit) || super.verifyDrawable(who)));
    }

    /* Access modifiers changed, original: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        int[] state = getDrawableState();
        boolean changed = false;
        Drawable background = this.mBackground;
        if (background != null && background.isStateful()) {
            changed = false | background.setState(state);
        }
        Drawable stackedBackground = this.mStackedBackground;
        if (stackedBackground != null && stackedBackground.isStateful()) {
            changed |= stackedBackground.setState(state);
        }
        Drawable splitBackground = this.mSplitBackground;
        if (splitBackground != null && splitBackground.isStateful()) {
            changed |= splitBackground.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        drawable = this.mStackedBackground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
        drawable = this.mSplitBackground;
        if (drawable != null) {
            drawable.jumpToCurrentState();
        }
    }

    public void onResolveDrawables(int layoutDirection) {
        super.onResolveDrawables(layoutDirection);
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setLayoutDirection(layoutDirection);
        }
        drawable = this.mStackedBackground;
        if (drawable != null) {
            drawable.setLayoutDirection(layoutDirection);
        }
        drawable = this.mSplitBackground;
        if (drawable != null) {
            drawable.setLayoutDirection(layoutDirection);
        }
    }

    public void setTransitioning(boolean isTransitioning) {
        int i;
        this.mIsTransitioning = isTransitioning;
        if (isTransitioning) {
            i = 393216;
        } else {
            i = 262144;
        }
        setDescendantFocusability(i);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.mIsTransitioning || super.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        super.onTouchEvent(ev);
        return true;
    }

    public boolean onHoverEvent(MotionEvent ev) {
        super.onHoverEvent(ev);
        return true;
    }

    public void setTabContainer(ScrollingTabContainerView tabView) {
        View view = this.mTabContainer;
        if (view != null) {
            removeView(view);
        }
        this.mTabContainer = tabView;
        if (tabView != null) {
            addView(tabView);
            LayoutParams lp = tabView.getLayoutParams();
            lp.width = -1;
            lp.height = -2;
            tabView.setAllowCollapse(false);
        }
    }

    public View getTabContainer() {
        return this.mTabContainer;
    }

    public ActionMode startActionModeForChild(View child, Callback callback, int type) {
        if (type != 0) {
            return super.startActionModeForChild(child, callback, type);
        }
        return null;
    }

    private static boolean isCollapsed(View view) {
        return view == null || view.getVisibility() == 8 || view.getMeasuredHeight() == 0;
    }

    private int getMeasuredHeightWithMargins(View view) {
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
        return (view.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin;
    }

    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        if (this.mActionBarView == null && MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE) {
            i = this.mHeight;
            if (i >= 0) {
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(Math.min(i, MeasureSpec.getSize(heightMeasureSpec)), Integer.MIN_VALUE);
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (this.mActionBarView != null) {
            View view = this.mTabContainer;
            if (!(view == null || view.getVisibility() == 8)) {
                i = 0;
                int childCount = getChildCount();
                for (int i2 = 0; i2 < childCount; i2++) {
                    View child = getChildAt(i2);
                    if (child != this.mTabContainer) {
                        int i3;
                        if (isCollapsed(child)) {
                            i3 = 0;
                        } else {
                            i3 = getMeasuredHeightWithMargins(child);
                        }
                        i = Math.max(i, i3);
                    }
                }
                setMeasuredDimension(getMeasuredWidth(), Math.min(getMeasuredHeightWithMargins(this.mTabContainer) + i, MeasureSpec.getMode(heightMeasureSpec) == Integer.MIN_VALUE ? MeasureSpec.getSize(heightMeasureSpec) : Integer.MAX_VALUE));
            }
        }
    }

    public void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        View tabContainer = this.mTabContainer;
        boolean hasTabs = (tabContainer == null || tabContainer.getVisibility() == 8) ? false : true;
        if (!(tabContainer == null || tabContainer.getVisibility() == 8)) {
            int containerHeight = getMeasuredHeight();
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) tabContainer.getLayoutParams();
            tabContainer.layout(l, (containerHeight - tabContainer.getMeasuredHeight()) - lp.bottomMargin, r, containerHeight - lp.bottomMargin);
        }
        boolean needsInvalidate = false;
        if (this.mIsSplit) {
            Drawable drawable = this.mSplitBackground;
            if (drawable != null) {
                drawable.setBounds(0, 0, getMeasuredWidth(), getMeasuredHeight());
                needsInvalidate = true;
            }
        } else {
            if (this.mBackground != null) {
                if (this.mActionBarView.getVisibility() == 0) {
                    this.mBackground.setBounds(this.mActionBarView.getLeft(), this.mActionBarView.getTop(), this.mActionBarView.getRight(), this.mActionBarView.getBottom());
                } else {
                    View view = this.mActionContextView;
                    if (view == null || view.getVisibility() != 0) {
                        this.mBackground.setBounds(0, 0, 0, 0);
                    } else {
                        this.mBackground.setBounds(this.mActionContextView.getLeft(), this.mActionContextView.getTop(), this.mActionContextView.getRight(), this.mActionContextView.getBottom());
                    }
                }
                needsInvalidate = true;
            }
            this.mIsStacked = hasTabs;
            if (hasTabs) {
                Drawable drawable2 = this.mStackedBackground;
                if (drawable2 != null) {
                    drawable2.setBounds(tabContainer.getLeft(), tabContainer.getTop(), tabContainer.getRight(), tabContainer.getBottom());
                    needsInvalidate = true;
                }
            }
        }
        if (needsInvalidate) {
            invalidate();
        }
    }
}
