package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;

@RemoteView
@Deprecated
public class AbsoluteLayout extends ViewGroup {

    public static class LayoutParams extends android.view.ViewGroup.LayoutParams {
        public int x;
        public int y;

        public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<LayoutParams> {
            private int mLayout_xId;
            private int mLayout_yId;
            private boolean mPropertiesMapped = false;

            public void mapProperties(PropertyMapper propertyMapper) {
                this.mLayout_xId = propertyMapper.mapInt("layout_x", 16843135);
                this.mLayout_yId = propertyMapper.mapInt("layout_y", 16843136);
                this.mPropertiesMapped = true;
            }

            public void readProperties(LayoutParams node, PropertyReader propertyReader) {
                if (this.mPropertiesMapped) {
                    propertyReader.readInt(this.mLayout_xId, node.x);
                    propertyReader.readInt(this.mLayout_yId, node.y);
                    return;
                }
                throw new UninitializedPropertyMapException();
            }
        }

        public LayoutParams(int width, int height, int x, int y) {
            super(width, height);
            this.x = x;
            this.y = y;
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.AbsoluteLayout_Layout);
            this.x = a.getDimensionPixelOffset(0, 0);
            this.y = a.getDimensionPixelOffset(1, 0);
            a.recycle();
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public String debug(String output) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("Absolute.LayoutParams={width=");
            stringBuilder.append(android.view.ViewGroup.LayoutParams.sizeToString(this.width));
            stringBuilder.append(", height=");
            stringBuilder.append(android.view.ViewGroup.LayoutParams.sizeToString(this.height));
            stringBuilder.append(" x=");
            stringBuilder.append(this.x);
            stringBuilder.append(" y=");
            stringBuilder.append(this.y);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }
    }

    public AbsoluteLayout(Context context) {
        this(context, null);
    }

    public AbsoluteLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbsoluteLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public AbsoluteLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childBottom = lp.y + child.getMeasuredHeight();
                maxWidth = Math.max(maxWidth, lp.x + child.getMeasuredWidth());
                maxHeight = Math.max(maxHeight, childBottom);
            }
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max(maxWidth + (this.mPaddingLeft + this.mPaddingRight), getSuggestedMinimumWidth()), widthMeasureSpec, 0), View.resolveSizeAndState(Math.max(maxHeight + (this.mPaddingTop + this.mPaddingBottom), getSuggestedMinimumHeight()), heightMeasureSpec, 0));
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2, 0, 0);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int childLeft = this.mPaddingLeft + lp.x;
                int childTop = this.mPaddingTop + lp.y;
                child.layout(childLeft, childTop, child.getMeasuredWidth() + childLeft, child.getMeasuredHeight() + childTop);
            }
        }
    }

    public android.view.ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* Access modifiers changed, original: protected */
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }
}
