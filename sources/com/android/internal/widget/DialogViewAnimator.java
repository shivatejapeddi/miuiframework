package com.android.internal.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ViewAnimator;
import java.util.ArrayList;

public class DialogViewAnimator extends ViewAnimator {
    private final ArrayList<View> mMatchParentChildren = new ArrayList(1);

    public DialogViewAnimator(Context context) {
        super(context);
    }

    public DialogViewAnimator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int childState;
        int i2 = widthMeasureSpec;
        int i3 = heightMeasureSpec;
        boolean z = (MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && MeasureSpec.getMode(heightMeasureSpec) == 1073741824) ? false : true;
        boolean measureMatchParentChildren = z;
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int childState2 = 0;
        int i4 = 0;
        while (true) {
            i = -1;
            if (i4 >= count) {
                break;
            }
            View child = getChildAt(i4);
            if (getMeasureAllChildren() || child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                boolean matchWidth = lp.width == -1;
                boolean matchHeight = lp.height == -1;
                if (measureMatchParentChildren && (matchWidth || matchHeight)) {
                    this.mMatchParentChildren.add(child);
                }
                LayoutParams lp2 = lp;
                View child2 = child;
                childState = childState2;
                int maxHeight2 = maxHeight;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                i = 0;
                if (measureMatchParentChildren && !matchWidth) {
                    maxWidth = Math.max(maxWidth, (child2.getMeasuredWidth() + lp2.leftMargin) + lp2.rightMargin);
                    i = 0 | (child2.getMeasuredWidthAndState() & -16777216);
                }
                if (!measureMatchParentChildren || matchHeight) {
                    maxHeight = maxHeight2;
                } else {
                    maxHeight = Math.max(maxHeight2, (child2.getMeasuredHeight() + lp2.topMargin) + lp2.bottomMargin);
                    i |= (child2.getMeasuredHeightAndState() >> 16) & -256;
                }
                childState2 = View.combineMeasuredStates(childState, i);
            }
            i4++;
        }
        childState = childState2;
        maxWidth += getPaddingLeft() + getPaddingRight();
        int maxHeight3 = Math.max(maxHeight + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight());
        int maxWidth2 = Math.max(maxWidth, getSuggestedMinimumWidth());
        Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight3 = Math.max(maxHeight3, drawable.getMinimumHeight());
            maxWidth2 = Math.max(maxWidth2, drawable.getMinimumWidth());
        }
        setMeasuredDimension(View.resolveSizeAndState(maxWidth2, i2, childState), View.resolveSizeAndState(maxHeight3, i3, childState << 16));
        childState2 = this.mMatchParentChildren.size();
        maxHeight = 0;
        while (maxHeight < childState2) {
            View child3 = (View) this.mMatchParentChildren.get(maxHeight);
            MarginLayoutParams lp3 = (MarginLayoutParams) child3.getLayoutParams();
            if (lp3.width == i) {
                i4 = MeasureSpec.makeMeasureSpec((((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight()) - lp3.leftMargin) - lp3.rightMargin, 1073741824);
            } else {
                i4 = ViewGroup.getChildMeasureSpec(i2, ((getPaddingLeft() + getPaddingRight()) + lp3.leftMargin) + lp3.rightMargin, lp3.width);
            }
            if (lp3.height == i) {
                maxWidth = MeasureSpec.makeMeasureSpec((((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom()) - lp3.topMargin) - lp3.bottomMargin, 1073741824);
            } else {
                maxWidth = ViewGroup.getChildMeasureSpec(i3, ((getPaddingTop() + getPaddingBottom()) + lp3.topMargin) + lp3.bottomMargin, lp3.height);
            }
            child3.measure(i4, maxWidth);
            maxHeight++;
            i = -1;
        }
        this.mMatchParentChildren.clear();
    }
}
