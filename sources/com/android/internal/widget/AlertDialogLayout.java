package com.android.internal.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.android.internal.R;

public class AlertDialogLayout extends LinearLayout {
    public AlertDialogLayout(Context context) {
        super(context);
    }

    @UnsupportedAppUsage
    public AlertDialogLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlertDialogLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public AlertDialogLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (!tryOnMeasure(widthMeasureSpec, heightMeasureSpec)) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    private boolean tryOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int heightToGive;
        int heightToGive2;
        View middlePanel;
        int i2 = widthMeasureSpec;
        int i3 = heightMeasureSpec;
        View topPanel = null;
        View buttonPanel = null;
        View middlePanel2 = null;
        int count = getChildCount();
        for (i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                switch (child.getId()) {
                    case R.id.buttonPanel /*16908785*/:
                        buttonPanel = child;
                        break;
                    case R.id.contentPanel /*16908837*/:
                    case R.id.customPanel /*16908862*/:
                        if (middlePanel2 == null) {
                            middlePanel2 = child;
                            break;
                        }
                        return false;
                    case R.id.topPanel /*16909518*/:
                        topPanel = child;
                        break;
                    default:
                        return false;
                }
            }
        }
        i = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int childState = 0;
        int usedHeight = getPaddingTop() + getPaddingBottom();
        if (topPanel != null) {
            topPanel.measure(i2, 0);
            usedHeight += topPanel.getMeasuredHeight();
            childState = View.combineMeasuredStates(0, topPanel.getMeasuredState());
        }
        int buttonHeight = 0;
        int buttonWantsHeight = 0;
        if (buttonPanel != null) {
            buttonPanel.measure(i2, 0);
            buttonHeight = resolveMinimumHeight(buttonPanel);
            buttonWantsHeight = buttonPanel.getMeasuredHeight() - buttonHeight;
            usedHeight += buttonHeight;
            childState = View.combineMeasuredStates(childState, buttonPanel.getMeasuredState());
        }
        int middleHeight = 0;
        if (middlePanel2 != null) {
            if (i == 0) {
                int childHeightSpec = topPanel;
                topPanel = null;
            } else {
                topPanel = MeasureSpec.makeMeasureSpec(Math.max(0, heightSize - usedHeight), i);
            }
            middlePanel2.measure(i2, topPanel);
            middleHeight = middlePanel2.getMeasuredHeight();
            usedHeight += middleHeight;
            childState = View.combineMeasuredStates(childState, middlePanel2.getMeasuredState());
        }
        int remainingHeight = heightSize - usedHeight;
        if (buttonPanel != null) {
            usedHeight -= buttonHeight;
            heightToGive = Math.min(remainingHeight, buttonWantsHeight);
            if (heightToGive > 0) {
                remainingHeight -= heightToGive;
                buttonHeight += heightToGive;
            }
            int remainingHeight2 = remainingHeight;
            buttonPanel.measure(i2, MeasureSpec.makeMeasureSpec(buttonHeight, 1073741824));
            usedHeight += buttonPanel.getMeasuredHeight();
            childState = View.combineMeasuredStates(childState, buttonPanel.getMeasuredState());
            remainingHeight = remainingHeight2;
        }
        if (middlePanel2 != null && remainingHeight > 0) {
            usedHeight -= middleHeight;
            heightToGive2 = remainingHeight;
            heightToGive = remainingHeight - heightToGive2;
            remainingHeight = MeasureSpec.makeMeasureSpec(middleHeight + heightToGive2, i);
            middlePanel2.measure(i2, remainingHeight);
            usedHeight += middlePanel2.getMeasuredHeight();
            childState = View.combineMeasuredStates(childState, middlePanel2.getMeasuredState());
            remainingHeight = heightToGive;
        }
        heightToGive = remainingHeight;
        remainingHeight = 0;
        heightToGive2 = 0;
        while (heightToGive2 < count) {
            View child2 = getChildAt(heightToGive2);
            View buttonPanel2 = buttonPanel;
            middlePanel = middlePanel2;
            if (child2.getVisibility() != 8) {
                remainingHeight = Math.max(remainingHeight, child2.getMeasuredWidth());
            }
            heightToGive2++;
            buttonPanel = buttonPanel2;
            middlePanel2 = middlePanel;
        }
        middlePanel = middlePanel2;
        setMeasuredDimension(View.resolveSizeAndState(remainingHeight + (getPaddingLeft() + getPaddingRight()), i2, childState), View.resolveSizeAndState(usedHeight, i3, 0));
        if (widthMode != 1073741824) {
            forceUniformWidth(count, i3);
        }
        return true;
    }

    private void forceUniformWidth(int count, int heightMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.width == -1) {
                    int oldHeight = lp.height;
                    lp.height = child.getMeasuredHeight();
                    measureChildWithMargins(child, uniformMeasureSpec, 0, heightMeasureSpec, 0);
                    lp.height = oldHeight;
                }
            }
        }
    }

    private int resolveMinimumHeight(View v) {
        int minHeight = v.getMinimumHeight();
        if (minHeight > 0) {
            return minHeight;
        }
        if (v instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) v;
            if (vg.getChildCount() == 1) {
                return resolveMinimumHeight(vg.getChildAt(0));
            }
        }
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childTop;
        AlertDialogLayout alertDialogLayout = this;
        int paddingLeft = alertDialogLayout.mPaddingLeft;
        int width = right - left;
        int childRight = width - alertDialogLayout.mPaddingRight;
        int childSpace = (width - paddingLeft) - alertDialogLayout.mPaddingRight;
        int totalLength = getMeasuredHeight();
        int count = getChildCount();
        int gravity = getGravity();
        int majorGravity = gravity & 112;
        int minorGravity = gravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (majorGravity == 16) {
            childTop = alertDialogLayout.mPaddingTop + (((bottom - top) - totalLength) / 2);
        } else if (majorGravity != 80) {
            childTop = alertDialogLayout.mPaddingTop;
        } else {
            childTop = ((alertDialogLayout.mPaddingTop + bottom) - top) - totalLength;
        }
        Drawable dividerDrawable = getDividerDrawable();
        int dividerHeight = dividerDrawable == null ? 0 : dividerDrawable.getIntrinsicHeight();
        int i = 0;
        while (i < count) {
            int i2;
            View child = alertDialogLayout.getChildAt(i);
            if (child == null || child.getVisibility() == 8) {
                i2 = i;
            } else {
                int layoutGravity;
                int childLeft;
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int layoutGravity2 = lp.gravity;
                if (layoutGravity2 < 0) {
                    layoutGravity = minorGravity;
                } else {
                    layoutGravity = layoutGravity2;
                }
                int layoutDirection = getLayoutDirection();
                layoutGravity2 = Gravity.getAbsoluteGravity(layoutGravity, layoutDirection) & 7;
                if (layoutGravity2 == 1) {
                    childLeft = ((((childSpace - childWidth) / 2) + paddingLeft) + lp.leftMargin) - lp.rightMargin;
                } else if (layoutGravity2 != 5) {
                    childLeft = lp.leftMargin + paddingLeft;
                } else {
                    childLeft = (childRight - childWidth) - lp.rightMargin;
                }
                if (alertDialogLayout.hasDividerBeforeChildAt(i)) {
                    childTop += dividerHeight;
                }
                int childTop2 = childTop + lp.topMargin;
                LayoutParams lp2 = lp;
                i2 = i;
                setChildFrame(child, childLeft, childTop2, childWidth, childHeight);
                childTop = childTop2 + (childHeight + lp2.bottomMargin);
            }
            i = i2 + 1;
            alertDialogLayout = this;
        }
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }
}
