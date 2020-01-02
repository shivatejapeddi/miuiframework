package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;

@RemoteView
public class MessagingLinearLayout extends ViewGroup {
    private int mMaxDisplayedLines = Integer.MAX_VALUE;
    private MessagingLayout mMessagingLayout;
    private int mSpacing;

    public interface MessagingChild {
        public static final int MEASURED_NORMAL = 0;
        public static final int MEASURED_SHORTENED = 1;
        public static final int MEASURED_TOO_SMALL = 2;

        int getConsumedLines();

        int getMeasuredType();

        void hideAnimated();

        boolean isHidingAnimated();

        void setMaxDisplayedLines(int i);

        int getExtraSpacing() {
            return 0;
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        public boolean hide = false;
        public int lastVisibleHeight;
        public boolean visibleBefore = false;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }

    public MessagingLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MessagingLinearLayout, 0, 0);
        int N = a.getIndexCount();
        for (int i = 0; i < N; i++) {
            if (a.getIndex(i) == 0) {
                this.mSpacing = a.getDimensionPixelSize(i, 0);
            }
        }
        a.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int targetHeight;
        int i;
        int targetHeight2 = MeasureSpec.getSize(heightMeasureSpec);
        if (MeasureSpec.getMode(heightMeasureSpec) != 0) {
            targetHeight = targetHeight2;
        } else {
            targetHeight = Integer.MAX_VALUE;
        }
        targetHeight2 = this.mPaddingLeft + this.mPaddingRight;
        int count = getChildCount();
        for (i = 0; i < count; i++) {
            ((LayoutParams) getChildAt(i).getLayoutParams()).hide = true;
        }
        int measuredWidth = targetHeight2;
        int totalHeight = this.mPaddingTop + this.mPaddingBottom;
        boolean first = true;
        int linesRemaining = this.mMaxDisplayedLines;
        for (int i2 = count - 1; i2 >= 0 && totalHeight < targetHeight; i2--) {
            if (getChildAt(i2).getVisibility() != 8) {
                MessagingChild messagingChild;
                View child = getChildAt(i2);
                LayoutParams lp = (LayoutParams) getChildAt(i2).getLayoutParams();
                i = this.mSpacing;
                if (child instanceof MessagingChild) {
                    MessagingChild messagingChild2 = (MessagingChild) child;
                    messagingChild2.setMaxDisplayedLines(linesRemaining);
                    i += messagingChild2.getExtraSpacing();
                    messagingChild = messagingChild2;
                } else {
                    messagingChild = null;
                }
                int spacing = first ? 0 : i;
                LayoutParams lp2 = lp;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, ((totalHeight - this.mPaddingTop) - this.mPaddingBottom) + spacing);
                i = Math.max(totalHeight, (((totalHeight + child.getMeasuredHeight()) + lp2.topMargin) + lp2.bottomMargin) + spacing);
                int measureType = 0;
                if (messagingChild != null) {
                    measureType = messagingChild.getMeasuredType();
                    linesRemaining -= messagingChild.getConsumedLines();
                }
                boolean isTooSmall = measureType == 2 && !first;
                boolean isShortened = measureType == 1 || (measureType == 2 && first);
                if (i > targetHeight || isTooSmall) {
                    break;
                }
                totalHeight = i;
                measuredWidth = Math.max(measuredWidth, (((child.getMeasuredWidth() + lp2.leftMargin) + lp2.rightMargin) + this.mPaddingLeft) + this.mPaddingRight);
                lp2.hide = false;
                if (isShortened || linesRemaining <= 0) {
                    break;
                }
                first = false;
            }
        }
        setMeasuredDimension(View.resolveSize(Math.max(getSuggestedMinimumWidth(), measuredWidth), widthMeasureSpec), Math.max(getSuggestedMinimumHeight(), totalHeight));
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int paddingLeft = this.mPaddingLeft;
        int width = right - left;
        int childRight = width - this.mPaddingRight;
        int layoutDirection = getLayoutDirection();
        int count = getChildCount();
        int childTop = this.mPaddingTop;
        boolean first = true;
        boolean shown = isShown();
        int i = 0;
        while (i < count) {
            int paddingLeft2;
            int width2;
            View child = getChildAt(i);
            if (child.getVisibility() == 8) {
                paddingLeft2 = paddingLeft;
                width2 = width;
            } else {
                int childLeft;
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                MessagingChild messagingChild = (MessagingChild) child;
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                if (layoutDirection == 1) {
                    childLeft = (childRight - childWidth) - lp.rightMargin;
                } else {
                    childLeft = paddingLeft + lp.leftMargin;
                }
                paddingLeft2 = paddingLeft;
                if (lp.hide) {
                    if (shown && lp.visibleBefore) {
                        width2 = width;
                        child.layout(childLeft, childTop, childLeft + childWidth, lp.lastVisibleHeight + childTop);
                        messagingChild.hideAnimated();
                    } else {
                        width2 = width;
                    }
                    lp.visibleBefore = false;
                } else {
                    width2 = width;
                    lp.visibleBefore = true;
                    lp.lastVisibleHeight = childHeight;
                    if (!first) {
                        childTop += this.mSpacing;
                    }
                    childTop += lp.topMargin;
                    child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                    childTop += lp.bottomMargin + childHeight;
                    first = false;
                }
            }
            i++;
            paddingLeft = paddingLeft2;
            width = width2;
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        if (!((LayoutParams) child.getLayoutParams()).hide || ((MessagingChild) child).isHidingAnimated()) {
            return super.drawChild(canvas, child, drawingTime);
        }
        return true;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(this.mContext, attrs);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -2);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
        LayoutParams copy = new LayoutParams(lp.width, lp.height);
        if (lp instanceof MarginLayoutParams) {
            copy.copyMarginsFrom((MarginLayoutParams) lp);
        }
        return copy;
    }

    public static boolean isGone(View view) {
        if (view.getVisibility() == 8) {
            return true;
        }
        android.view.ViewGroup.LayoutParams lp = view.getLayoutParams();
        if ((lp instanceof LayoutParams) && ((LayoutParams) lp).hide) {
            return true;
        }
        return false;
    }

    @RemotableViewMethod
    public void setMaxDisplayedLines(int numberLines) {
        this.mMaxDisplayedLines = numberLines;
    }

    public void setMessagingLayout(MessagingLayout layout) {
        this.mMessagingLayout = layout;
    }

    public MessagingLayout getMessagingLayout() {
        return this.mMessagingLayout;
    }
}
