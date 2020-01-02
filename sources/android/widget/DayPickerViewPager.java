package android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import com.android.internal.widget.ViewPager;
import com.android.internal.widget.ViewPager.LayoutParams;
import java.util.ArrayList;
import java.util.function.Predicate;

class DayPickerViewPager extends ViewPager {
    private final ArrayList<View> mMatchParentChildren;

    public DayPickerViewPager(Context context) {
        this(context, null);
    }

    public DayPickerViewPager(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DayPickerViewPager(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public DayPickerViewPager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mMatchParentChildren = new ArrayList(1);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        View child;
        LayoutParams lp;
        int i = widthMeasureSpec;
        int i2 = heightMeasureSpec;
        populate();
        int count = getChildCount();
        int i3 = 1073741824;
        boolean measureMatchParentChildren = (MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && MeasureSpec.getMode(heightMeasureSpec) == 1073741824) ? false : true;
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        for (int i4 = 0; i4 < count; i4++) {
            child = getChildAt(i4);
            if (child.getVisibility() != 8) {
                measureChild(child, i, i2);
                lp = (LayoutParams) child.getLayoutParams();
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
                childState = View.combineMeasuredStates(childState, child.getMeasuredState());
                if (measureMatchParentChildren && (lp.width == -1 || lp.height == -1)) {
                    this.mMatchParentChildren.add(child);
                }
            }
        }
        maxWidth += getPaddingLeft() + getPaddingRight();
        maxHeight = Math.max(maxHeight + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight = Math.max(maxHeight, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }
        setMeasuredDimension(View.resolveSizeAndState(maxWidth, i, childState), View.resolveSizeAndState(maxHeight, i2, childState << 16));
        count = this.mMatchParentChildren.size();
        if (count > 1) {
            int i5 = 0;
            while (i5 < count) {
                int childWidthMeasureSpec;
                int childHeightMeasureSpec;
                child = (View) this.mMatchParentChildren.get(i5);
                lp = (LayoutParams) child.getLayoutParams();
                if (lp.width == -1) {
                    childWidthMeasureSpec = MeasureSpec.makeMeasureSpec((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), i3);
                } else {
                    childWidthMeasureSpec = ViewGroup.getChildMeasureSpec(i, getPaddingLeft() + getPaddingRight(), lp.width);
                }
                if (lp.height == -1) {
                    childHeightMeasureSpec = MeasureSpec.makeMeasureSpec((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), i3);
                } else {
                    childHeightMeasureSpec = ViewGroup.getChildMeasureSpec(i2, getPaddingTop() + getPaddingBottom(), lp.height);
                }
                child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
                i5++;
                i3 = 1073741824;
            }
        }
        this.mMatchParentChildren.clear();
    }

    /* Access modifiers changed, original: protected */
    public <T extends View> T findViewByPredicateTraversal(Predicate<View> predicate, View childToSkip) {
        if (predicate.test(this)) {
            return this;
        }
        View current = ((DayPickerPagerAdapter) getAdapter()).getView(getCurrent());
        if (!(current == childToSkip || current == null)) {
            View v = current.findViewByPredicate(predicate);
            if (v != null) {
                return v;
            }
        }
        int len = getChildCount();
        for (int i = 0; i < len; i++) {
            View child = getChildAt(i);
            if (!(child == childToSkip || child == current)) {
                View v2 = child.findViewByPredicate(predicate);
                if (v2 != null) {
                    return v2;
                }
            }
        }
        return null;
    }
}
