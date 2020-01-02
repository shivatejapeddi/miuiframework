package com.android.internal.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;
import com.android.internal.R;
import java.util.ArrayList;
import java.util.Comparator;

@RemoteView
public class NotificationActionListLayout extends LinearLayout {
    public static final Comparator<Pair<Integer, TextView>> MEASURE_ORDER_COMPARATOR = -$$Lambda$NotificationActionListLayout$uFZFEmIEBpI3kn6c3tNvvgmMSv8.INSTANCE;
    private int mDefaultPaddingBottom;
    private int mDefaultPaddingTop;
    private int mEmphasizedHeight;
    private boolean mEmphasizedMode;
    private final int mGravity;
    private ArrayList<View> mMeasureOrderOther;
    private ArrayList<Pair<Integer, TextView>> mMeasureOrderTextViews;
    private int mRegularHeight;
    private int mTotalWidth;

    public NotificationActionListLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NotificationActionListLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NotificationActionListLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mTotalWidth = 0;
        this.mMeasureOrderTextViews = new ArrayList();
        this.mMeasureOrderOther = new ArrayList();
        TypedArray ta = context.obtainStyledAttributes(attrs, new int[]{16842927}, defStyleAttr, defStyleRes);
        this.mGravity = ta.getInt(0, 0);
        ta.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mEmphasizedMode) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        int i;
        boolean needRebuild;
        int N = getChildCount();
        int i2 = 0;
        int textViews = 0;
        int otherViews = 0;
        int notGoneChildren = 0;
        while (true) {
            i = 8;
            if (i2 >= N) {
                break;
            }
            View c = getChildAt(i2);
            if (c instanceof TextView) {
                textViews++;
            } else {
                otherViews++;
            }
            if (c.getVisibility() != 8) {
                notGoneChildren++;
            }
            i2++;
        }
        boolean needRebuild2 = false;
        if (!(textViews == this.mMeasureOrderTextViews.size() && otherViews == this.mMeasureOrderOther.size())) {
            needRebuild2 = true;
        }
        if (needRebuild2) {
            needRebuild = needRebuild2;
        } else {
            int size = this.mMeasureOrderTextViews.size();
            for (int i3 = 0; i3 < size; i3++) {
                Pair<Integer, TextView> pair = (Pair) this.mMeasureOrderTextViews.get(i3);
                if (((Integer) pair.first).intValue() != ((TextView) pair.second).getText().length()) {
                    needRebuild2 = true;
                }
            }
            needRebuild = needRebuild2;
        }
        if (needRebuild) {
            rebuildMeasureOrder(textViews, otherViews);
        }
        boolean constrained = MeasureSpec.getMode(widthMeasureSpec) != 0;
        int innerWidth = (MeasureSpec.getSize(widthMeasureSpec) - this.mPaddingLeft) - this.mPaddingRight;
        int otherSize = this.mMeasureOrderOther.size();
        int usedWidth = 0;
        int measuredChildren = 0;
        int i4 = 0;
        while (i4 < N) {
            View c2;
            int i5;
            if (i4 < otherSize) {
                c2 = (View) this.mMeasureOrderOther.get(i4);
            } else {
                c2 = (View) ((Pair) this.mMeasureOrderTextViews.get(i4 - otherSize)).second;
            }
            if (c2.getVisibility() == i) {
                i5 = i4;
            } else {
                int usedWidthForChild;
                MarginLayoutParams lp = (MarginLayoutParams) c2.getLayoutParams();
                int usedWidthForChild2 = usedWidth;
                if (constrained) {
                    usedWidthForChild = innerWidth - ((innerWidth - usedWidth) / (notGoneChildren - measuredChildren));
                } else {
                    usedWidthForChild = usedWidthForChild2;
                }
                MarginLayoutParams lp2 = lp;
                i5 = i4;
                measureChildWithMargins(c2, widthMeasureSpec, usedWidthForChild, heightMeasureSpec, 0);
                usedWidth += (c2.getMeasuredWidth() + lp2.rightMargin) + lp2.leftMargin;
                measuredChildren++;
            }
            i4 = i5 + 1;
            i = 8;
        }
        this.mTotalWidth = (usedWidth + this.mPaddingRight) + this.mPaddingLeft;
        setMeasuredDimension(View.resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec), View.resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    private void rebuildMeasureOrder(int capacityText, int capacityOther) {
        clearMeasureOrder();
        this.mMeasureOrderTextViews.ensureCapacity(capacityText);
        this.mMeasureOrderOther.ensureCapacity(capacityOther);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View c = getChildAt(i);
            if (!(c instanceof TextView) || ((TextView) c).getText().length() <= 0) {
                this.mMeasureOrderOther.add(c);
            } else {
                this.mMeasureOrderTextViews.add(Pair.create(Integer.valueOf(((TextView) c).getText().length()), (TextView) c));
            }
        }
        this.mMeasureOrderTextViews.sort(MEASURE_ORDER_COMPARATOR);
    }

    private void clearMeasureOrder() {
        this.mMeasureOrderOther.clear();
        this.mMeasureOrderTextViews.clear();
    }

    public void onViewAdded(View child) {
        super.onViewAdded(child);
        clearMeasureOrder();
        if (child.getBackground() instanceof RippleDrawable) {
            ((RippleDrawable) child.getBackground()).setForceSoftware(true);
        }
    }

    public void onViewRemoved(View child) {
        super.onViewRemoved(child);
        clearMeasureOrder();
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        NotificationActionListLayout notificationActionListLayout = this;
        if (notificationActionListLayout.mEmphasizedMode) {
            super.onLayout(changed, left, top, right, bottom);
            return;
        }
        int childLeft;
        boolean isLayoutRtl = isLayoutRtl();
        int paddingTop = notificationActionListLayout.mPaddingTop;
        boolean z = true;
        if ((notificationActionListLayout.mGravity & 1) == 0) {
            z = false;
        }
        if (z) {
            childLeft = ((notificationActionListLayout.mPaddingLeft + left) + ((right - left) / 2)) - (notificationActionListLayout.mTotalWidth / 2);
        } else {
            childLeft = notificationActionListLayout.mPaddingLeft;
            if (Gravity.getAbsoluteGravity(Gravity.START, getLayoutDirection()) == 5) {
                childLeft += (right - left) - notificationActionListLayout.mTotalWidth;
            }
        }
        int innerHeight = ((bottom - top) - paddingTop) - notificationActionListLayout.mPaddingBottom;
        int count = getChildCount();
        int start = 0;
        int dir = 1;
        if (isLayoutRtl) {
            start = count - 1;
            dir = -1;
        }
        int i = 0;
        while (i < count) {
            boolean isLayoutRtl2;
            int paddingTop2;
            View child = notificationActionListLayout.getChildAt((dir * i) + start);
            if (child.getVisibility() != 8) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
                int childTop = ((paddingTop + ((innerHeight - childHeight) / 2)) + lp.topMargin) - lp.bottomMargin;
                isLayoutRtl2 = isLayoutRtl;
                childLeft += lp.leftMargin;
                paddingTop2 = paddingTop;
                child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                childLeft += lp.rightMargin + childWidth;
            } else {
                isLayoutRtl2 = isLayoutRtl;
                paddingTop2 = paddingTop;
            }
            i++;
            notificationActionListLayout = this;
            isLayoutRtl = isLayoutRtl2;
            paddingTop = paddingTop2;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mDefaultPaddingBottom = getPaddingBottom();
        this.mDefaultPaddingTop = getPaddingTop();
        updateHeights();
    }

    private void updateHeights() {
        this.mEmphasizedHeight = (getResources().getDimensionPixelSize(R.dimen.notification_content_margin_end) + getResources().getDimensionPixelSize(R.dimen.notification_content_margin)) + getResources().getDimensionPixelSize(R.dimen.notification_action_emphasized_height);
        this.mRegularHeight = getResources().getDimensionPixelSize(R.dimen.notification_action_list_height);
    }

    @RemotableViewMethod
    public void setEmphasizedMode(boolean emphasizedMode) {
        int height;
        this.mEmphasizedMode = emphasizedMode;
        if (emphasizedMode) {
            int paddingTop = getResources().getDimensionPixelSize(R.dimen.notification_content_margin);
            int paddingBottom = getResources().getDimensionPixelSize(R.dimen.notification_content_margin_end);
            height = this.mEmphasizedHeight;
            int buttonPaddingInternal = getResources().getDimensionPixelSize(R.dimen.button_inset_vertical_material);
            setPaddingRelative(getPaddingStart(), paddingTop - buttonPaddingInternal, getPaddingEnd(), paddingBottom - buttonPaddingInternal);
        } else {
            setPaddingRelative(getPaddingStart(), this.mDefaultPaddingTop, getPaddingEnd(), this.mDefaultPaddingBottom);
            height = this.mRegularHeight;
        }
        LayoutParams layoutParams = getLayoutParams();
        layoutParams.height = height;
        setLayoutParams(layoutParams);
    }

    public int getExtraMeasureHeight() {
        if (this.mEmphasizedMode) {
            return this.mEmphasizedHeight - this.mRegularHeight;
        }
        return 0;
    }
}
