package android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.ViewHierarchyEncoder;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import com.android.internal.R;

public class TableRow extends LinearLayout {
    private ChildrenTracker mChildrenTracker;
    private SparseIntArray mColumnToChildIndex;
    private int[] mColumnWidths;
    private int[] mConstrainedColumnWidths;
    private int mNumColumns = 0;

    private class ChildrenTracker implements OnHierarchyChangeListener {
        private OnHierarchyChangeListener listener;

        private ChildrenTracker() {
        }

        private void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
            this.listener = listener;
        }

        public void onChildViewAdded(View parent, View child) {
            TableRow.this.mColumnToChildIndex = null;
            OnHierarchyChangeListener onHierarchyChangeListener = this.listener;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        public void onChildViewRemoved(View parent, View child) {
            TableRow.this.mColumnToChildIndex = null;
            OnHierarchyChangeListener onHierarchyChangeListener = this.listener;
            if (onHierarchyChangeListener != null) {
                onHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
        private static final int LOCATION = 0;
        private static final int LOCATION_NEXT = 1;
        @ExportedProperty(category = "layout")
        public int column;
        private int[] mOffset;
        @ExportedProperty(category = "layout")
        public int span;

        public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<LayoutParams> {
            private int mLayout_columnId;
            private int mLayout_spanId;
            private boolean mPropertiesMapped = false;

            public void mapProperties(PropertyMapper propertyMapper) {
                this.mLayout_columnId = propertyMapper.mapInt("layout_column", 16843084);
                this.mLayout_spanId = propertyMapper.mapInt("layout_span", 16843085);
                this.mPropertiesMapped = true;
            }

            public void readProperties(LayoutParams node, PropertyReader propertyReader) {
                if (this.mPropertiesMapped) {
                    propertyReader.readInt(this.mLayout_columnId, node.column);
                    propertyReader.readInt(this.mLayout_spanId, node.span);
                    return;
                }
                throw new UninitializedPropertyMapException();
            }
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mOffset = new int[2];
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.TableRow_Cell);
            this.column = a.getInt(0, -1);
            this.span = a.getInt(1, 1);
            if (this.span <= 1) {
                this.span = 1;
            }
            a.recycle();
        }

        public LayoutParams(int w, int h) {
            super(w, h);
            this.mOffset = new int[2];
            this.column = -1;
            this.span = 1;
        }

        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
            this.mOffset = new int[2];
            this.column = -1;
            this.span = 1;
        }

        public LayoutParams() {
            super(-1, -2);
            this.mOffset = new int[2];
            this.column = -1;
            this.span = 1;
        }

        public LayoutParams(int column) {
            this();
            this.column = column;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
            this.mOffset = new int[2];
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
            this.mOffset = new int[2];
        }

        /* Access modifiers changed, original: protected */
        public void setBaseAttributes(TypedArray a, int widthAttr, int heightAttr) {
            if (a.hasValue(widthAttr)) {
                this.width = a.getLayoutDimension(widthAttr, "layout_width");
            } else {
                this.width = -1;
            }
            if (a.hasValue(heightAttr)) {
                this.height = a.getLayoutDimension(heightAttr, "layout_height");
            } else {
                this.height = -2;
            }
        }

        /* Access modifiers changed, original: protected */
        public void encodeProperties(ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("layout:column", this.column);
            encoder.addProperty("layout:span", this.span);
        }
    }

    public TableRow(Context context) {
        super(context);
        initTableRow();
    }

    public TableRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTableRow();
    }

    private void initTableRow() {
        OnHierarchyChangeListener oldListener = this.mOnHierarchyChangeListener;
        this.mChildrenTracker = new ChildrenTracker();
        if (oldListener != null) {
            this.mChildrenTracker.setOnHierarchyChangeListener(oldListener);
        }
        super.setOnHierarchyChangeListener(this.mChildrenTracker);
    }

    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        this.mChildrenTracker.setOnHierarchyChangeListener(listener);
    }

    /* Access modifiers changed, original: 0000 */
    public void setColumnCollapsed(int columnIndex, boolean collapsed) {
        View child = getVirtualChildAt(columnIndex);
        if (child != null) {
            child.setVisibility(collapsed ? 8 : 0);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureHorizontal(widthMeasureSpec, heightMeasureSpec);
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutHorizontal(l, t, r, b);
    }

    public View getVirtualChildAt(int i) {
        if (this.mColumnToChildIndex == null) {
            mapIndexAndColumns();
        }
        int deflectedIndex = this.mColumnToChildIndex.get(i, -1);
        if (deflectedIndex != -1) {
            return getChildAt(deflectedIndex);
        }
        return null;
    }

    public int getVirtualChildCount() {
        if (this.mColumnToChildIndex == null) {
            mapIndexAndColumns();
        }
        return this.mNumColumns;
    }

    private void mapIndexAndColumns() {
        if (this.mColumnToChildIndex == null) {
            int virtualCount = 0;
            int count = getChildCount();
            this.mColumnToChildIndex = new SparseIntArray();
            SparseIntArray columnToChild = this.mColumnToChildIndex;
            for (int i = 0; i < count; i++) {
                LayoutParams layoutParams = (LayoutParams) getChildAt(i).getLayoutParams();
                if (layoutParams.column >= virtualCount) {
                    virtualCount = layoutParams.column;
                }
                int j = 0;
                while (j < layoutParams.span) {
                    int virtualCount2 = virtualCount + 1;
                    columnToChild.put(virtualCount, i);
                    j++;
                    virtualCount = virtualCount2;
                }
            }
            this.mNumColumns = virtualCount;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int measureNullChild(int childIndex) {
        return this.mConstrainedColumnWidths[childIndex];
    }

    /* Access modifiers changed, original: 0000 */
    public void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        if (this.mConstrainedColumnWidths != null) {
            int i;
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            int measureMode = 1073741824;
            int columnWidth = 0;
            int span = lp.span;
            int[] constrainedColumnWidths = this.mConstrainedColumnWidths;
            for (i = 0; i < span; i++) {
                columnWidth += constrainedColumnWidths[childIndex + i];
            }
            i = lp.gravity;
            boolean isHorizontalGravity = Gravity.isHorizontal(i);
            if (isHorizontalGravity) {
                measureMode = Integer.MIN_VALUE;
            }
            child.measure(MeasureSpec.makeMeasureSpec(Math.max(0, (columnWidth - lp.leftMargin) - lp.rightMargin), measureMode), ViewGroup.getChildMeasureSpec(heightMeasureSpec, (((this.mPaddingTop + this.mPaddingBottom) + lp.topMargin) + lp.bottomMargin) + totalHeight, lp.height));
            if (isHorizontalGravity) {
                lp.mOffset[1] = columnWidth - child.getMeasuredWidth();
                int absoluteGravity = Gravity.getAbsoluteGravity(i, getLayoutDirection()) & 7;
                if (absoluteGravity == 1) {
                    lp.mOffset[0] = lp.mOffset[1] / 2;
                    return;
                } else if (absoluteGravity != 3 && absoluteGravity == 5) {
                    lp.mOffset[0] = lp.mOffset[1];
                    return;
                } else {
                    return;
                }
            }
            int i2 = 0;
            int i3 = 1;
            int[] access$200 = lp.mOffset;
            lp.mOffset[i3] = i2;
            access$200[i2] = i2;
            return;
        }
        View view = child;
        int i4 = heightMeasureSpec;
        super.measureChildBeforeLayout(child, childIndex, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    /* Access modifiers changed, original: 0000 */
    public int getChildrenSkipCount(View child, int index) {
        return ((LayoutParams) child.getLayoutParams()).span - 1;
    }

    /* Access modifiers changed, original: 0000 */
    public int getLocationOffset(View child) {
        return ((LayoutParams) child.getLayoutParams()).mOffset[0];
    }

    /* Access modifiers changed, original: 0000 */
    public int getNextLocationOffset(View child) {
        return ((LayoutParams) child.getLayoutParams()).mOffset[1];
    }

    /* Access modifiers changed, original: 0000 */
    public int[] getColumnsWidths(int widthMeasureSpec, int heightMeasureSpec) {
        int numColumns = getVirtualChildCount();
        int[] iArr = this.mColumnWidths;
        if (iArr == null || numColumns != iArr.length) {
            this.mColumnWidths = new int[numColumns];
        }
        iArr = this.mColumnWidths;
        for (int i = 0; i < numColumns; i++) {
            View child = getVirtualChildAt(i);
            if (child == null || child.getVisibility() == 8) {
                iArr[i] = 0;
            } else {
                LayoutParams layoutParams = (LayoutParams) child.getLayoutParams();
                if (layoutParams.span == 1) {
                    int spec;
                    int i2 = layoutParams.width;
                    if (i2 == -2) {
                        spec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, 0, -2);
                    } else if (i2 != -1) {
                        spec = MeasureSpec.makeMeasureSpec(layoutParams.width, 1073741824);
                    } else {
                        spec = MeasureSpec.makeSafeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), 0);
                    }
                    child.measure(spec, spec);
                    iArr[i] = (child.getMeasuredWidth() + layoutParams.leftMargin) + layoutParams.rightMargin;
                } else {
                    iArr[i] = 0;
                }
            }
        }
        return iArr;
    }

    /* Access modifiers changed, original: 0000 */
    public void setColumnsWidthConstraints(int[] columnWidths) {
        if (columnWidths == null || columnWidths.length < getVirtualChildCount()) {
            throw new IllegalArgumentException("columnWidths should be >= getVirtualChildCount()");
        }
        this.mConstrainedColumnWidths = columnWidths;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public android.widget.LinearLayout.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams();
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    /* Access modifiers changed, original: protected */
    public android.widget.LinearLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public CharSequence getAccessibilityClassName() {
        return TableRow.class.getName();
    }
}
