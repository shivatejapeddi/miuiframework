package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;
import java.util.ArrayList;

@RemoteView
public class FrameLayout extends ViewGroup {
    private static final int DEFAULT_CHILD_GRAVITY = 8388659;
    @ExportedProperty(category = "padding")
    @UnsupportedAppUsage
    private int mForegroundPaddingBottom;
    @ExportedProperty(category = "padding")
    @UnsupportedAppUsage
    private int mForegroundPaddingLeft;
    @ExportedProperty(category = "padding")
    @UnsupportedAppUsage
    private int mForegroundPaddingRight;
    @ExportedProperty(category = "padding")
    @UnsupportedAppUsage
    private int mForegroundPaddingTop;
    private final ArrayList<View> mMatchParentChildren;
    @ExportedProperty(category = "measurement")
    @UnsupportedAppUsage
    boolean mMeasureAllChildren;

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<FrameLayout> {
        private int mMeasureAllChildrenId;
        private boolean mPropertiesMapped = false;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mMeasureAllChildrenId = propertyMapper.mapBoolean("measureAllChildren", 16843018);
            this.mPropertiesMapped = true;
        }

        public void readProperties(FrameLayout node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readBoolean(this.mMeasureAllChildrenId, node.getMeasureAllChildren());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public static class LayoutParams extends MarginLayoutParams {
        public static final int UNSPECIFIED_GRAVITY = -1;
        public int gravity = -1;

        public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<LayoutParams> {
            private int mLayout_gravityId;
            private boolean mPropertiesMapped = false;

            public void mapProperties(PropertyMapper propertyMapper) {
                this.mLayout_gravityId = propertyMapper.mapGravity("layout_gravity", 16842931);
                this.mPropertiesMapped = true;
            }

            public void readProperties(LayoutParams node, PropertyReader propertyReader) {
                if (this.mPropertiesMapped) {
                    propertyReader.readGravity(this.mLayout_gravityId, node.gravity);
                    return;
                }
                throw new UninitializedPropertyMapException();
            }
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FrameLayout_Layout);
            this.gravity = a.getInt(0, -1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LayoutParams source) {
            super((MarginLayoutParams) source);
            this.gravity = source.gravity;
        }
    }

    public FrameLayout(Context context) {
        super(context);
        this.mMeasureAllChildren = false;
        this.mForegroundPaddingLeft = 0;
        this.mForegroundPaddingTop = 0;
        this.mForegroundPaddingRight = 0;
        this.mForegroundPaddingBottom = 0;
        this.mMatchParentChildren = new ArrayList(1);
    }

    public FrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FrameLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mMeasureAllChildren = false;
        this.mForegroundPaddingLeft = 0;
        this.mForegroundPaddingTop = 0;
        this.mForegroundPaddingRight = 0;
        this.mForegroundPaddingBottom = 0;
        this.mMatchParentChildren = new ArrayList(1);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.FrameLayout, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.FrameLayout, attrs, a, defStyleAttr, defStyleRes);
        if (a.getBoolean(0, false)) {
            setMeasureAllChildren(true);
        }
        a.recycle();
    }

    @RemotableViewMethod
    public void setForegroundGravity(int foregroundGravity) {
        if (getForegroundGravity() != foregroundGravity) {
            super.setForegroundGravity(foregroundGravity);
            Drawable foreground = getForeground();
            if (getForegroundGravity() != 119 || foreground == null) {
                this.mForegroundPaddingLeft = 0;
                this.mForegroundPaddingTop = 0;
                this.mForegroundPaddingRight = 0;
                this.mForegroundPaddingBottom = 0;
            } else {
                Rect padding = new Rect();
                if (foreground.getPadding(padding)) {
                    this.mForegroundPaddingLeft = padding.left;
                    this.mForegroundPaddingTop = padding.top;
                    this.mForegroundPaddingRight = padding.right;
                    this.mForegroundPaddingBottom = padding.bottom;
                }
            }
            requestLayout();
        }
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-1, -1);
    }

    /* Access modifiers changed, original: 0000 */
    public int getPaddingLeftWithForeground() {
        if (isForegroundInsidePadding()) {
            return Math.max(this.mPaddingLeft, this.mForegroundPaddingLeft);
        }
        return this.mPaddingLeft + this.mForegroundPaddingLeft;
    }

    /* Access modifiers changed, original: 0000 */
    public int getPaddingRightWithForeground() {
        if (isForegroundInsidePadding()) {
            return Math.max(this.mPaddingRight, this.mForegroundPaddingRight);
        }
        return this.mPaddingRight + this.mForegroundPaddingRight;
    }

    private int getPaddingTopWithForeground() {
        if (isForegroundInsidePadding()) {
            return Math.max(this.mPaddingTop, this.mForegroundPaddingTop);
        }
        return this.mPaddingTop + this.mForegroundPaddingTop;
    }

    private int getPaddingBottomWithForeground() {
        if (isForegroundInsidePadding()) {
            return Math.max(this.mPaddingBottom, this.mForegroundPaddingBottom);
        }
        return this.mPaddingBottom + this.mForegroundPaddingBottom;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int i;
        int i2;
        int childState;
        int maxHeight;
        int maxWidth;
        int childState2;
        int i3 = widthMeasureSpec;
        int i4 = heightMeasureSpec;
        int count = getChildCount();
        boolean z = (MeasureSpec.getMode(widthMeasureSpec) == 1073741824 && MeasureSpec.getMode(heightMeasureSpec) == 1073741824) ? false : true;
        boolean measureMatchParentChildren = z;
        this.mMatchParentChildren.clear();
        int maxHeight2 = 0;
        int maxWidth2 = 0;
        int childState3 = 0;
        for (i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (this.mMeasureAllChildren || child.getVisibility() != 8) {
                View child2 = child;
                i2 = -1;
                childState = childState3;
                maxHeight = maxHeight2;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                LayoutParams lp = (LayoutParams) child2.getLayoutParams();
                maxWidth = Math.max(maxWidth2, (child2.getMeasuredWidth() + lp.leftMargin) + lp.rightMargin);
                int maxHeight3 = Math.max(maxHeight, (child2.getMeasuredHeight() + lp.topMargin) + lp.bottomMargin);
                childState2 = View.combineMeasuredStates(childState, child2.getMeasuredState());
                if (!measureMatchParentChildren) {
                } else if (lp.width == i2 || lp.height == i2) {
                    this.mMatchParentChildren.add(child2);
                }
                maxWidth2 = maxWidth;
                maxHeight2 = maxHeight3;
                childState3 = childState2;
            }
        }
        i2 = -1;
        childState = childState3;
        maxWidth2 += getPaddingLeftWithForeground() + getPaddingRightWithForeground();
        int maxHeight4 = Math.max(maxHeight2 + (getPaddingTopWithForeground() + getPaddingBottomWithForeground()), getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth2, getSuggestedMinimumWidth());
        Drawable drawable = getForeground();
        if (drawable != null) {
            maxHeight4 = Math.max(maxHeight4, drawable.getMinimumHeight());
            maxWidth = Math.max(maxWidth, drawable.getMinimumWidth());
        }
        setMeasuredDimension(View.resolveSizeAndState(maxWidth, i3, childState), View.resolveSizeAndState(maxHeight4, i4, childState << 16));
        childState2 = this.mMatchParentChildren.size();
        if (childState2 > 1) {
            childState3 = 0;
            while (childState3 < childState2) {
                View child3 = (View) this.mMatchParentChildren.get(childState3);
                MarginLayoutParams lp2 = (MarginLayoutParams) child3.getLayoutParams();
                if (lp2.width == i2) {
                    maxHeight = MeasureSpec.makeMeasureSpec(Math.max(0, (((getMeasuredWidth() - getPaddingLeftWithForeground()) - getPaddingRightWithForeground()) - lp2.leftMargin) - lp2.rightMargin), 1073741824);
                } else {
                    maxHeight = ViewGroup.getChildMeasureSpec(i3, ((getPaddingLeftWithForeground() + getPaddingRightWithForeground()) + lp2.leftMargin) + lp2.rightMargin, lp2.width);
                }
                if (lp2.height == i2) {
                    i = MeasureSpec.makeMeasureSpec(Math.max(0, (((getMeasuredHeight() - getPaddingTopWithForeground()) - getPaddingBottomWithForeground()) - lp2.topMargin) - lp2.bottomMargin), 1073741824);
                } else {
                    i = ViewGroup.getChildMeasureSpec(i4, ((getPaddingTopWithForeground() + getPaddingBottomWithForeground()) + lp2.topMargin) + lp2.bottomMargin, lp2.height);
                }
                child3.measure(maxHeight, i);
                childState3++;
                i2 = -1;
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        layoutChildren(left, top, right, bottom, false);
    }

    /* Access modifiers changed, original: 0000 */
    public void layoutChildren(int left, int top, int right, int bottom, boolean forceLeftGravity) {
        int count = getChildCount();
        int parentLeft = getPaddingLeftWithForeground();
        int parentRight = (right - left) - getPaddingRightWithForeground();
        int parentTop = getPaddingTopWithForeground();
        int parentBottom = (bottom - top) - getPaddingBottomWithForeground();
        int i = 0;
        while (i < count) {
            int count2;
            int parentLeft2;
            int parentRight2;
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int gravity = lp.gravity;
                if (gravity == -1) {
                    gravity = DEFAULT_CHILD_GRAVITY;
                }
                int verticalGravity = gravity & 112;
                int absoluteGravity = Gravity.getAbsoluteGravity(gravity, getLayoutDirection()) & 7;
                count2 = count;
                if (absoluteGravity == 1) {
                    count = (((((parentRight - parentLeft) - width) / 2) + parentLeft) + lp.leftMargin) - lp.rightMargin;
                } else if (absoluteGravity == 5 && !forceLeftGravity) {
                    count = (parentRight - width) - lp.rightMargin;
                } else {
                    count = lp.leftMargin + parentLeft;
                }
                if (verticalGravity == 16) {
                    parentLeft2 = parentLeft;
                    absoluteGravity = (((((parentBottom - parentTop) - height) / 2) + parentTop) + lp.topMargin) - lp.bottomMargin;
                } else if (verticalGravity == 48) {
                    parentLeft2 = parentLeft;
                    absoluteGravity = parentTop + lp.topMargin;
                } else if (verticalGravity != 80) {
                    absoluteGravity = lp.topMargin + parentTop;
                    parentLeft2 = parentLeft;
                } else {
                    parentLeft2 = parentLeft;
                    absoluteGravity = (parentBottom - height) - lp.bottomMargin;
                }
                parentRight2 = parentRight;
                child.layout(count, absoluteGravity, count + width, absoluteGravity + height);
            } else {
                count2 = count;
                parentLeft2 = parentLeft;
                parentRight2 = parentRight;
            }
            i++;
            count = count2;
            parentLeft = parentLeft2;
            parentRight = parentRight2;
        }
    }

    @RemotableViewMethod
    public void setMeasureAllChildren(boolean measureAll) {
        this.mMeasureAllChildren = measureAll;
    }

    @Deprecated
    public boolean getConsiderGoneChildrenWhenMeasuring() {
        return getMeasureAllChildren();
    }

    public boolean getMeasureAllChildren() {
        return this.mMeasureAllChildren;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
        if (sPreserveMarginParamsInLayoutParamConversion) {
            if (lp instanceof LayoutParams) {
                return new LayoutParams((LayoutParams) lp);
            }
            if (lp instanceof MarginLayoutParams) {
                return new LayoutParams((MarginLayoutParams) lp);
            }
        }
        return new LayoutParams(lp);
    }

    public CharSequence getAccessibilityClassName() {
        return FrameLayout.class.getName();
    }

    /* Access modifiers changed, original: protected */
    public void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("measurement:measureAllChildren", this.mMeasureAllChildren);
        encoder.addProperty("padding:foregroundPaddingLeft", this.mForegroundPaddingLeft);
        encoder.addProperty("padding:foregroundPaddingTop", this.mForegroundPaddingTop);
        encoder.addProperty("padding:foregroundPaddingRight", this.mForegroundPaddingRight);
        encoder.addProperty("padding:foregroundPaddingBottom", this.mForegroundPaddingBottom);
    }
}
