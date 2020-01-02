package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.app.slice.Slice;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.RemotableViewMethod;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewDebug.FlagToString;
import android.view.ViewDebug.IntToString;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@RemoteView
public class LinearLayout extends ViewGroup {
    public static final int HORIZONTAL = 0;
    @UnsupportedAppUsage
    private static final int INDEX_BOTTOM = 2;
    private static final int INDEX_CENTER_VERTICAL = 0;
    private static final int INDEX_FILL = 3;
    @UnsupportedAppUsage
    private static final int INDEX_TOP = 1;
    public static final int SHOW_DIVIDER_BEGINNING = 1;
    public static final int SHOW_DIVIDER_END = 4;
    public static final int SHOW_DIVIDER_MIDDLE = 2;
    public static final int SHOW_DIVIDER_NONE = 0;
    public static final int VERTICAL = 1;
    private static final int VERTICAL_GRAVITY_COUNT = 4;
    private static boolean sCompatibilityDone = false;
    private static boolean sRemeasureWeightedChildren = true;
    private final boolean mAllowInconsistentMeasurement;
    @ExportedProperty(category = "layout")
    private boolean mBaselineAligned;
    @ExportedProperty(category = "layout")
    private int mBaselineAlignedChildIndex;
    @ExportedProperty(category = "measurement")
    private int mBaselineChildTop;
    @UnsupportedAppUsage
    private Drawable mDivider;
    private int mDividerHeight;
    private int mDividerPadding;
    private int mDividerWidth;
    @ExportedProperty(category = "measurement", flagMapping = {@FlagToString(equals = -1, mask = -1, name = "NONE"), @FlagToString(equals = 0, mask = 0, name = "NONE"), @FlagToString(equals = 48, mask = 48, name = "TOP"), @FlagToString(equals = 80, mask = 80, name = "BOTTOM"), @FlagToString(equals = 3, mask = 3, name = "LEFT"), @FlagToString(equals = 5, mask = 5, name = "RIGHT"), @FlagToString(equals = 8388611, mask = 8388611, name = "START"), @FlagToString(equals = 8388613, mask = 8388613, name = "END"), @FlagToString(equals = 16, mask = 16, name = "CENTER_VERTICAL"), @FlagToString(equals = 112, mask = 112, name = "FILL_VERTICAL"), @FlagToString(equals = 1, mask = 1, name = "CENTER_HORIZONTAL"), @FlagToString(equals = 7, mask = 7, name = "FILL_HORIZONTAL"), @FlagToString(equals = 17, mask = 17, name = "CENTER"), @FlagToString(equals = 119, mask = 119, name = "FILL"), @FlagToString(equals = 8388608, mask = 8388608, name = "RELATIVE")}, formatToHexString = true)
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mGravity;
    private int mLayoutDirection;
    @UnsupportedAppUsage
    private int[] mMaxAscent;
    @UnsupportedAppUsage
    private int[] mMaxDescent;
    @ExportedProperty(category = "measurement")
    private int mOrientation;
    private int mShowDividers;
    @ExportedProperty(category = "measurement")
    @UnsupportedAppUsage
    private int mTotalLength;
    @ExportedProperty(category = "layout")
    @UnsupportedAppUsage
    private boolean mUseLargestChild;
    @ExportedProperty(category = "layout")
    private float mWeightSum;

    public static class LayoutParams extends MarginLayoutParams {
        @ExportedProperty(category = "layout", mapping = {@IntToString(from = -1, to = "NONE"), @IntToString(from = 0, to = "NONE"), @IntToString(from = 48, to = "TOP"), @IntToString(from = 80, to = "BOTTOM"), @IntToString(from = 3, to = "LEFT"), @IntToString(from = 5, to = "RIGHT"), @IntToString(from = 8388611, to = "START"), @IntToString(from = 8388613, to = "END"), @IntToString(from = 16, to = "CENTER_VERTICAL"), @IntToString(from = 112, to = "FILL_VERTICAL"), @IntToString(from = 1, to = "CENTER_HORIZONTAL"), @IntToString(from = 7, to = "FILL_HORIZONTAL"), @IntToString(from = 17, to = "CENTER"), @IntToString(from = 119, to = "FILL")})
        public int gravity;
        @ExportedProperty(category = "layout")
        public float weight;

        public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<LayoutParams> {
            private int mLayout_gravityId;
            private int mLayout_weightId;
            private boolean mPropertiesMapped = false;

            public void mapProperties(PropertyMapper propertyMapper) {
                this.mLayout_gravityId = propertyMapper.mapGravity("layout_gravity", 16842931);
                this.mLayout_weightId = propertyMapper.mapFloat("layout_weight", 16843137);
                this.mPropertiesMapped = true;
            }

            public void readProperties(LayoutParams node, PropertyReader propertyReader) {
                if (this.mPropertiesMapped) {
                    propertyReader.readGravity(this.mLayout_gravityId, node.gravity);
                    propertyReader.readFloat(this.mLayout_weightId, node.weight);
                    return;
                }
                throw new UninitializedPropertyMapException();
            }
        }

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.gravity = -1;
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.LinearLayout_Layout);
            this.weight = a.getFloat(3, 0.0f);
            this.gravity = a.getInt(0, -1);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.gravity = -1;
            this.weight = 0.0f;
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height);
            this.gravity = -1;
            this.weight = weight;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
            this.gravity = -1;
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
            this.gravity = -1;
        }

        public LayoutParams(LayoutParams source) {
            super((MarginLayoutParams) source);
            this.gravity = -1;
            this.weight = source.weight;
            this.gravity = source.gravity;
        }

        public String debug(String output) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(output);
            stringBuilder.append("LinearLayout.LayoutParams={width=");
            stringBuilder.append(android.view.ViewGroup.LayoutParams.sizeToString(this.width));
            stringBuilder.append(", height=");
            stringBuilder.append(android.view.ViewGroup.LayoutParams.sizeToString(this.height));
            stringBuilder.append(" weight=");
            stringBuilder.append(this.weight);
            stringBuilder.append("}");
            return stringBuilder.toString();
        }

        /* Access modifiers changed, original: protected */
        @UnsupportedAppUsage
        public void encodeProperties(ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("layout:weight", this.weight);
            encoder.addProperty("layout:gravity", this.gravity);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DividerMode {
    }

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<LinearLayout> {
        private int mBaselineAlignedChildIndexId;
        private int mBaselineAlignedId;
        private int mDividerId;
        private int mGravityId;
        private int mMeasureWithLargestChildId;
        private int mOrientationId;
        private boolean mPropertiesMapped = false;
        private int mWeightSumId;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mBaselineAlignedId = propertyMapper.mapBoolean("baselineAligned", 16843046);
            this.mBaselineAlignedChildIndexId = propertyMapper.mapInt("baselineAlignedChildIndex", 16843047);
            this.mDividerId = propertyMapper.mapObject("divider", 16843049);
            this.mGravityId = propertyMapper.mapGravity("gravity", 16842927);
            this.mMeasureWithLargestChildId = propertyMapper.mapBoolean("measureWithLargestChild", 16843476);
            SparseArray<String> orientationEnumMapping = new SparseArray();
            orientationEnumMapping.put(0, Slice.HINT_HORIZONTAL);
            orientationEnumMapping.put(1, "vertical");
            Objects.requireNonNull(orientationEnumMapping);
            this.mOrientationId = propertyMapper.mapIntEnum("orientation", 16842948, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(orientationEnumMapping));
            this.mWeightSumId = propertyMapper.mapFloat("weightSum", 16843048);
            this.mPropertiesMapped = true;
        }

        public void readProperties(LinearLayout node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readBoolean(this.mBaselineAlignedId, node.isBaselineAligned());
                propertyReader.readInt(this.mBaselineAlignedChildIndexId, node.getBaselineAlignedChildIndex());
                propertyReader.readObject(this.mDividerId, node.getDividerDrawable());
                propertyReader.readGravity(this.mGravityId, node.getGravity());
                propertyReader.readBoolean(this.mMeasureWithLargestChildId, node.isMeasureWithLargestChildEnabled());
                propertyReader.readIntEnum(this.mOrientationId, node.getOrientation());
                propertyReader.readFloat(this.mWeightSumId, node.getWeightSum());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode {
    }

    public LinearLayout(Context context) {
        this(context, null);
    }

    public LinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        boolean z = true;
        this.mBaselineAligned = true;
        this.mBaselineAlignedChildIndex = -1;
        this.mBaselineChildTop = 0;
        this.mGravity = 8388659;
        this.mLayoutDirection = -1;
        if (!(sCompatibilityDone || context == null)) {
            sRemeasureWeightedChildren = context.getApplicationInfo().targetSdkVersion >= 28;
            sCompatibilityDone = true;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LinearLayout, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.LinearLayout, attrs, a, defStyleAttr, defStyleRes);
        int index = a.getInt(1, -1);
        if (index >= 0) {
            setOrientation(index);
        }
        index = a.getInt(0, -1);
        if (index >= 0) {
            setGravity(index);
        }
        boolean baselineAligned = a.getBoolean(true, true);
        if (!baselineAligned) {
            setBaselineAligned(baselineAligned);
        }
        this.mWeightSum = a.getFloat(4, -1.0f);
        this.mBaselineAlignedChildIndex = a.getInt(3, -1);
        this.mUseLargestChild = a.getBoolean(6, false);
        this.mShowDividers = a.getInt(7, 0);
        this.mDividerPadding = a.getDimensionPixelSize(8, 0);
        setDividerDrawable(a.getDrawable(5));
        if (context.getApplicationInfo().targetSdkVersion > 23) {
            z = false;
        }
        this.mAllowInconsistentMeasurement = z;
        a.recycle();
    }

    private boolean isShowingDividers() {
        return (this.mShowDividers == 0 || this.mDivider == null) ? false : true;
    }

    public void setShowDividers(int showDividers) {
        if (showDividers != this.mShowDividers) {
            this.mShowDividers = showDividers;
            setWillNotDraw(isShowingDividers() ^ 1);
            requestLayout();
        }
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public int getShowDividers() {
        return this.mShowDividers;
    }

    public Drawable getDividerDrawable() {
        return this.mDivider;
    }

    public void setDividerDrawable(Drawable divider) {
        if (divider != this.mDivider) {
            this.mDivider = divider;
            if (divider != null) {
                this.mDividerWidth = divider.getIntrinsicWidth();
                this.mDividerHeight = divider.getIntrinsicHeight();
            } else {
                this.mDividerWidth = 0;
                this.mDividerHeight = 0;
            }
            setWillNotDraw(isShowingDividers() ^ 1);
            requestLayout();
        }
    }

    public void setDividerPadding(int padding) {
        if (padding != this.mDividerPadding) {
            this.mDividerPadding = padding;
            if (isShowingDividers()) {
                requestLayout();
                invalidate();
            }
        }
    }

    public int getDividerPadding() {
        return this.mDividerPadding;
    }

    public int getDividerWidth() {
        return this.mDividerWidth;
    }

    /* Access modifiers changed, original: protected */
    public void onDraw(Canvas canvas) {
        if (this.mDivider != null) {
            if (this.mOrientation == 1) {
                drawDividersVertical(canvas);
            } else {
                drawDividersHorizontal(canvas);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void drawDividersVertical(Canvas canvas) {
        int count = getVirtualChildCount();
        int i = 0;
        while (i < count) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8 || !hasDividerBeforeChildAt(i))) {
                drawHorizontalDivider(canvas, (child.getTop() - ((LayoutParams) child.getLayoutParams()).topMargin) - this.mDividerHeight);
            }
            i++;
        }
        if (hasDividerBeforeChildAt(count)) {
            int bottom;
            View child2 = getLastNonGoneChild();
            if (child2 == null) {
                bottom = (getHeight() - getPaddingBottom()) - this.mDividerHeight;
            } else {
                bottom = child2.getBottom() + ((LayoutParams) child2.getLayoutParams()).bottomMargin;
            }
            drawHorizontalDivider(canvas, bottom);
        }
    }

    private View getLastNonGoneChild() {
        for (int i = getVirtualChildCount() - 1; i >= 0; i--) {
            View child = getVirtualChildAt(i);
            if (child != null && child.getVisibility() != 8) {
                return child;
            }
        }
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public void drawDividersHorizontal(Canvas canvas) {
        int count = getVirtualChildCount();
        boolean isLayoutRtl = isLayoutRtl();
        int i = 0;
        while (i < count) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8 || !hasDividerBeforeChildAt(i))) {
                int position;
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (isLayoutRtl) {
                    position = child.getRight() + lp.rightMargin;
                } else {
                    position = (child.getLeft() - lp.leftMargin) - this.mDividerWidth;
                }
                drawVerticalDivider(canvas, position);
            }
            i++;
        }
        if (hasDividerBeforeChildAt(count)) {
            int position2;
            View child2 = getLastNonGoneChild();
            if (child2 != null) {
                LayoutParams lp2 = (LayoutParams) child2.getLayoutParams();
                if (isLayoutRtl) {
                    position2 = (child2.getLeft() - lp2.leftMargin) - this.mDividerWidth;
                } else {
                    position2 = child2.getRight() + lp2.rightMargin;
                }
            } else if (isLayoutRtl) {
                position2 = getPaddingLeft();
            } else {
                position2 = (getWidth() - getPaddingRight()) - this.mDividerWidth;
            }
            drawVerticalDivider(canvas, position2);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void drawHorizontalDivider(Canvas canvas, int top) {
        this.mDivider.setBounds(getPaddingLeft() + this.mDividerPadding, top, (getWidth() - getPaddingRight()) - this.mDividerPadding, this.mDividerHeight + top);
        this.mDivider.draw(canvas);
    }

    /* Access modifiers changed, original: 0000 */
    public void drawVerticalDivider(Canvas canvas, int left) {
        this.mDivider.setBounds(left, getPaddingTop() + this.mDividerPadding, this.mDividerWidth + left, (getHeight() - getPaddingBottom()) - this.mDividerPadding);
        this.mDivider.draw(canvas);
    }

    public boolean isBaselineAligned() {
        return this.mBaselineAligned;
    }

    @RemotableViewMethod
    public void setBaselineAligned(boolean baselineAligned) {
        this.mBaselineAligned = baselineAligned;
    }

    public boolean isMeasureWithLargestChildEnabled() {
        return this.mUseLargestChild;
    }

    @RemotableViewMethod
    public void setMeasureWithLargestChildEnabled(boolean enabled) {
        this.mUseLargestChild = enabled;
    }

    public int getBaseline() {
        if (this.mBaselineAlignedChildIndex < 0) {
            return super.getBaseline();
        }
        int childCount = getChildCount();
        int i = this.mBaselineAlignedChildIndex;
        if (childCount > i) {
            View child = getChildAt(i);
            i = child.getBaseline();
            if (i != -1) {
                int childTop = this.mBaselineChildTop;
                if (this.mOrientation == 1) {
                    int majorGravity = this.mGravity & 112;
                    if (majorGravity != 48) {
                        if (majorGravity == 16) {
                            childTop += ((((this.mBottom - this.mTop) - this.mPaddingTop) - this.mPaddingBottom) - this.mTotalLength) / 2;
                        } else if (majorGravity == 80) {
                            childTop = ((this.mBottom - this.mTop) - this.mPaddingBottom) - this.mTotalLength;
                        }
                    }
                }
                return (((LayoutParams) child.getLayoutParams()).topMargin + childTop) + i;
            } else if (this.mBaselineAlignedChildIndex == 0) {
                return -1;
            } else {
                throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout points to a View that doesn't know how to get its baseline.");
            }
        }
        throw new RuntimeException("mBaselineAlignedChildIndex of LinearLayout set to an index that is out of bounds.");
    }

    public int getBaselineAlignedChildIndex() {
        return this.mBaselineAlignedChildIndex;
    }

    @RemotableViewMethod
    public void setBaselineAlignedChildIndex(int i) {
        if (i < 0 || i >= getChildCount()) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("base aligned child index out of range (0, ");
            stringBuilder.append(getChildCount());
            stringBuilder.append(")");
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        this.mBaselineAlignedChildIndex = i;
    }

    /* Access modifiers changed, original: 0000 */
    public View getVirtualChildAt(int index) {
        return getChildAt(index);
    }

    /* Access modifiers changed, original: 0000 */
    public int getVirtualChildCount() {
        return getChildCount();
    }

    public float getWeightSum() {
        return this.mWeightSum;
    }

    @RemotableViewMethod
    public void setWeightSum(float weightSum) {
        this.mWeightSum = Math.max(0.0f, weightSum);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (this.mOrientation == 1) {
            measureVertical(widthMeasureSpec, heightMeasureSpec);
        } else {
            measureHorizontal(widthMeasureSpec, heightMeasureSpec);
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean hasDividerBeforeChildAt(int childIndex) {
        boolean z = false;
        if (childIndex == getVirtualChildCount()) {
            if ((this.mShowDividers & 4) != 0) {
                z = true;
            }
            return z;
        } else if (allViewsAreGoneBefore(childIndex)) {
            if ((this.mShowDividers & 1) != 0) {
                z = true;
            }
            return z;
        } else {
            if ((this.mShowDividers & 2) != 0) {
                z = true;
            }
            return z;
        }
    }

    private boolean allViewsAreGoneBefore(int childIndex) {
        for (int i = childIndex - 1; i >= 0; i--) {
            View child = getVirtualChildAt(i);
            if (child != null && child.getVisibility() != 8) {
                return false;
            }
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:162:0x03ca  */
    /* JADX WARNING: Removed duplicated region for block: B:161:0x03c8  */
    /* JADX WARNING: Removed duplicated region for block: B:168:0x03dc  */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x03d2  */
    /* JADX WARNING: Removed duplicated region for block: B:181:0x0459  */
    /* JADX WARNING: Removed duplicated region for block: B:180:0x0453  */
    public void measureVertical(int r40, int r41) {
        /*
        r39 = this;
        r7 = r39;
        r8 = r40;
        r9 = r41;
        r10 = 0;
        r7.mTotalLength = r10;
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r3 = 0;
        r4 = 1;
        r5 = 0;
        r11 = r39.getVirtualChildCount();
        r12 = android.view.View.MeasureSpec.getMode(r40);
        r13 = android.view.View.MeasureSpec.getMode(r41);
        r6 = 0;
        r14 = 0;
        r15 = r7.mBaselineAlignedChildIndex;
        r10 = r7.mUseLargestChild;
        r17 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r18 = 0;
        r19 = 0;
        r20 = 0;
        r21 = r6;
        r37 = r1;
        r1 = r0;
        r0 = r37;
        r38 = r4;
        r4 = r2;
        r2 = r17;
        r17 = r14;
        r14 = r20;
        r20 = r38;
    L_0x003b:
        r6 = 8;
        r23 = 0;
        r24 = r4;
        r25 = 1;
        if (r14 >= r11) goto L_0x01b0;
    L_0x0045:
        r4 = r7.getVirtualChildAt(r14);
        if (r4 != 0) goto L_0x005d;
    L_0x004b:
        r6 = r7.mTotalLength;
        r22 = r7.measureNullChild(r14);
        r6 = r6 + r22;
        r7.mTotalLength = r6;
        r29 = r11;
        r26 = r13;
        r4 = r24;
        goto L_0x01a4;
    L_0x005d:
        r26 = r0;
        r0 = r4.getVisibility();
        if (r0 != r6) goto L_0x0074;
    L_0x0065:
        r0 = r7.getChildrenSkipCount(r4, r14);
        r14 = r14 + r0;
        r29 = r11;
        r4 = r24;
        r0 = r26;
        r26 = r13;
        goto L_0x01a4;
    L_0x0074:
        r19 = r19 + 1;
        r0 = r7.hasDividerBeforeChildAt(r14);
        if (r0 == 0) goto L_0x0083;
    L_0x007c:
        r0 = r7.mTotalLength;
        r6 = r7.mDividerHeight;
        r0 = r0 + r6;
        r7.mTotalLength = r0;
    L_0x0083:
        r0 = r4.getLayoutParams();
        r0 = (android.widget.LinearLayout.LayoutParams) r0;
        r6 = r0.weight;
        r27 = r5 + r6;
        r5 = r0.height;
        if (r5 != 0) goto L_0x009a;
    L_0x0091:
        r5 = r0.weight;
        r5 = (r5 > r23 ? 1 : (r5 == r23 ? 0 : -1));
        if (r5 <= 0) goto L_0x009a;
    L_0x0097:
        r5 = r25;
        goto L_0x009b;
    L_0x009a:
        r5 = 0;
    L_0x009b:
        r28 = r5;
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r13 != r5) goto L_0x00c7;
    L_0x00a1:
        if (r28 == 0) goto L_0x00c7;
    L_0x00a3:
        r6 = r7.mTotalLength;
        r5 = r0.topMargin;
        r5 = r5 + r6;
        r29 = r1;
        r1 = r0.bottomMargin;
        r5 = r5 + r1;
        r1 = java.lang.Math.max(r6, r5);
        r7.mTotalLength = r1;
        r17 = 1;
        r9 = r0;
        r32 = r24;
        r8 = r26;
        r24 = r3;
        r3 = r4;
        r26 = r13;
        r13 = r29;
        r29 = r11;
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        goto L_0x0129;
    L_0x00c7:
        r29 = r1;
        if (r28 == 0) goto L_0x00ce;
    L_0x00cb:
        r1 = -2;
        r0.height = r1;
    L_0x00ce:
        r1 = (r27 > r23 ? 1 : (r27 == r23 ? 0 : -1));
        if (r1 != 0) goto L_0x00d6;
    L_0x00d2:
        r1 = r7.mTotalLength;
        r6 = r1;
        goto L_0x00d7;
    L_0x00d6:
        r6 = 0;
    L_0x00d7:
        r5 = -1;
        r22 = 0;
        r1 = r0;
        r8 = r26;
        r0 = r39;
        r9 = r1;
        r26 = r13;
        r13 = r29;
        r1 = r4;
        r30 = r2;
        r2 = r14;
        r29 = r11;
        r11 = r3;
        r3 = r40;
        r31 = r4;
        r32 = r24;
        r24 = r11;
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = r22;
        r5 = r41;
        r0.measureChildBeforeLayout(r1, r2, r3, r4, r5, r6);
        r0 = r31.getMeasuredHeight();
        if (r28 == 0) goto L_0x0107;
    L_0x0102:
        r1 = 0;
        r9.height = r1;
        r18 = r18 + r0;
    L_0x0107:
        r1 = r7.mTotalLength;
        r2 = r1 + r0;
        r3 = r9.topMargin;
        r2 = r2 + r3;
        r3 = r9.bottomMargin;
        r2 = r2 + r3;
        r3 = r31;
        r4 = r7.getNextLocationOffset(r3);
        r2 = r2 + r4;
        r2 = java.lang.Math.max(r1, r2);
        r7.mTotalLength = r2;
        if (r10 == 0) goto L_0x0127;
    L_0x0120:
        r2 = r30;
        r2 = java.lang.Math.max(r0, r2);
        goto L_0x0129;
    L_0x0127:
        r2 = r30;
    L_0x0129:
        if (r15 < 0) goto L_0x0133;
    L_0x012b:
        r0 = r14 + 1;
        if (r15 != r0) goto L_0x0133;
    L_0x012f:
        r0 = r7.mTotalLength;
        r7.mBaselineChildTop = r0;
    L_0x0133:
        if (r14 >= r15) goto L_0x0144;
    L_0x0135:
        r0 = r9.weight;
        r0 = (r0 > r23 ? 1 : (r0 == r23 ? 0 : -1));
        if (r0 > 0) goto L_0x013c;
    L_0x013b:
        goto L_0x0144;
    L_0x013c:
        r0 = new java.lang.RuntimeException;
        r1 = "A child of LinearLayout with index less than mBaselineAlignedChildIndex has weight > 0, which won't work.  Either remove the weight, or don't set mBaselineAlignedChildIndex.";
        r0.<init>(r1);
        throw r0;
    L_0x0144:
        r0 = 0;
        if (r12 == r11) goto L_0x0150;
    L_0x0147:
        r1 = r9.width;
        r4 = -1;
        if (r1 != r4) goto L_0x0151;
    L_0x014c:
        r21 = 1;
        r0 = 1;
        goto L_0x0151;
    L_0x0150:
        r4 = -1;
    L_0x0151:
        r1 = r9.leftMargin;
        r5 = r9.rightMargin;
        r1 = r1 + r5;
        r5 = r3.getMeasuredWidth();
        r5 = r5 + r1;
        r6 = java.lang.Math.max(r13, r5);
        r11 = r3.getMeasuredState();
        r8 = android.view.View.combineMeasuredStates(r8, r11);
        if (r20 == 0) goto L_0x0170;
    L_0x0169:
        r11 = r9.width;
        if (r11 != r4) goto L_0x0170;
    L_0x016d:
        r4 = r25;
        goto L_0x0171;
    L_0x0170:
        r4 = 0;
    L_0x0171:
        r11 = r9.weight;
        r11 = (r11 > r23 ? 1 : (r11 == r23 ? 0 : -1));
        if (r11 <= 0) goto L_0x0187;
        if (r0 == 0) goto L_0x017c;
    L_0x017a:
        r11 = r1;
        goto L_0x017d;
    L_0x017c:
        r11 = r5;
    L_0x017d:
        r13 = r24;
        r11 = java.lang.Math.max(r13, r11);
        r20 = r4;
        r13 = r11;
        goto L_0x0198;
    L_0x0187:
        r13 = r24;
        if (r0 == 0) goto L_0x018d;
    L_0x018b:
        r11 = r1;
        goto L_0x018e;
    L_0x018d:
        r11 = r5;
    L_0x018e:
        r20 = r4;
        r4 = r32;
        r4 = java.lang.Math.max(r4, r11);
        r32 = r4;
    L_0x0198:
        r4 = r7.getChildrenSkipCount(r3, r14);
        r14 = r14 + r4;
        r1 = r6;
        r0 = r8;
        r3 = r13;
        r5 = r27;
        r4 = r32;
    L_0x01a4:
        r14 = r14 + 1;
        r8 = r40;
        r9 = r41;
        r13 = r26;
        r11 = r29;
        goto L_0x003b;
    L_0x01b0:
        r8 = r0;
        r29 = r11;
        r26 = r13;
        r4 = r24;
        r0 = -1;
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r13 = r1;
        if (r19 <= 0) goto L_0x01cd;
    L_0x01bd:
        r1 = r29;
        r9 = r7.hasDividerBeforeChildAt(r1);
        if (r9 == 0) goto L_0x01cf;
    L_0x01c5:
        r9 = r7.mTotalLength;
        r14 = r7.mDividerHeight;
        r9 = r9 + r14;
        r7.mTotalLength = r9;
        goto L_0x01cf;
    L_0x01cd:
        r1 = r29;
    L_0x01cf:
        if (r10 == 0) goto L_0x0232;
    L_0x01d1:
        r9 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r14 = r26;
        if (r14 == r9) goto L_0x01dd;
    L_0x01d7:
        if (r14 != 0) goto L_0x01da;
    L_0x01d9:
        goto L_0x01dd;
    L_0x01da:
        r27 = r8;
        goto L_0x0236;
    L_0x01dd:
        r9 = 0;
        r7.mTotalLength = r9;
        r9 = 0;
    L_0x01e1:
        if (r9 >= r1) goto L_0x022f;
    L_0x01e3:
        r0 = r7.getVirtualChildAt(r9);
        if (r0 != 0) goto L_0x01f6;
    L_0x01e9:
        r11 = r7.mTotalLength;
        r24 = r7.measureNullChild(r9);
        r11 = r11 + r24;
        r7.mTotalLength = r11;
        r27 = r8;
        goto L_0x0225;
    L_0x01f6:
        r11 = r0.getVisibility();
        if (r11 != r6) goto L_0x0204;
    L_0x01fc:
        r11 = r7.getChildrenSkipCount(r0, r9);
        r9 = r9 + r11;
        r27 = r8;
        goto L_0x0225;
        r11 = r0.getLayoutParams();
        r11 = (android.widget.LinearLayout.LayoutParams) r11;
        r6 = r7.mTotalLength;
        r26 = r6 + r2;
        r27 = r8;
        r8 = r11.topMargin;
        r26 = r26 + r8;
        r8 = r11.bottomMargin;
        r26 = r26 + r8;
        r8 = r7.getNextLocationOffset(r0);
        r8 = r26 + r8;
        r8 = java.lang.Math.max(r6, r8);
        r7.mTotalLength = r8;
    L_0x0225:
        r9 = r9 + 1;
        r8 = r27;
        r0 = -1;
        r6 = 8;
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        goto L_0x01e1;
    L_0x022f:
        r27 = r8;
        goto L_0x0236;
    L_0x0232:
        r27 = r8;
        r14 = r26;
    L_0x0236:
        r0 = r7.mTotalLength;
        r6 = r7.mPaddingTop;
        r8 = r7.mPaddingBottom;
        r6 = r6 + r8;
        r0 = r0 + r6;
        r7.mTotalLength = r0;
        r0 = r7.mTotalLength;
        r6 = r39.getSuggestedMinimumHeight();
        r0 = java.lang.Math.max(r0, r6);
        r6 = r41;
        r8 = 0;
        r9 = android.view.View.resolveSizeAndState(r0, r6, r8);
        r8 = 16777215; // 0xffffff float:2.3509886E-38 double:8.2890456E-317;
        r0 = r9 & r8;
        r8 = r7.mTotalLength;
        r8 = r0 - r8;
        r11 = r7.mAllowInconsistentMeasurement;
        if (r11 == 0) goto L_0x0260;
    L_0x025e:
        r11 = 0;
        goto L_0x0262;
    L_0x0260:
        r11 = r18;
    L_0x0262:
        r8 = r8 + r11;
        if (r17 != 0) goto L_0x02fb;
    L_0x0265:
        r11 = sRemeasureWeightedChildren;
        if (r11 != 0) goto L_0x026b;
    L_0x0269:
        if (r8 == 0) goto L_0x0277;
    L_0x026b:
        r11 = (r5 > r23 ? 1 : (r5 == r23 ? 0 : -1));
        if (r11 <= 0) goto L_0x0277;
    L_0x026f:
        r26 = r0;
        r28 = r3;
        r29 = r5;
        goto L_0x0301;
    L_0x0277:
        r4 = java.lang.Math.max(r4, r3);
        if (r10 == 0) goto L_0x02e2;
    L_0x027d:
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r14 == r11) goto L_0x02e2;
    L_0x0281:
        r11 = 0;
    L_0x0282:
        if (r11 >= r1) goto L_0x02d9;
    L_0x0284:
        r26 = r0;
        r0 = r7.getVirtualChildAt(r11);
        if (r0 == 0) goto L_0x02c8;
    L_0x028c:
        r28 = r3;
        r3 = r0.getVisibility();
        r16 = r4;
        r4 = 8;
        if (r3 != r4) goto L_0x029b;
    L_0x0298:
        r29 = r5;
        goto L_0x02ce;
        r3 = r0.getLayoutParams();
        r3 = (android.widget.LinearLayout.LayoutParams) r3;
        r4 = r3.weight;
        r22 = (r4 > r23 ? 1 : (r4 == r23 ? 0 : -1));
        if (r22 <= 0) goto L_0x02c1;
        r22 = r3;
        r3 = r0.getMeasuredWidth();
        r25 = r4;
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r3 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r4);
        r29 = r5;
        r5 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r4);
        r0.measure(r3, r5);
        goto L_0x02ce;
    L_0x02c1:
        r22 = r3;
        r25 = r4;
        r29 = r5;
        goto L_0x02ce;
    L_0x02c8:
        r28 = r3;
        r16 = r4;
        r29 = r5;
    L_0x02ce:
        r11 = r11 + 1;
        r4 = r16;
        r0 = r26;
        r3 = r28;
        r5 = r29;
        goto L_0x0282;
    L_0x02d9:
        r26 = r0;
        r28 = r3;
        r16 = r4;
        r29 = r5;
        goto L_0x02ea;
    L_0x02e2:
        r26 = r0;
        r28 = r3;
        r16 = r4;
        r29 = r5;
    L_0x02ea:
        r31 = r1;
        r1 = r8;
        r30 = r15;
        r5 = r16;
        r4 = r27;
        r8 = r40;
        r16 = r2;
        r27 = r10;
        goto L_0x0435;
    L_0x02fb:
        r26 = r0;
        r28 = r3;
        r29 = r5;
    L_0x0301:
        r5 = r7.mWeightSum;
        r0 = (r5 > r23 ? 1 : (r5 == r23 ? 0 : -1));
        if (r0 <= 0) goto L_0x0308;
    L_0x0307:
        goto L_0x030a;
    L_0x0308:
        r5 = r29;
    L_0x030a:
        r0 = r5;
        r3 = 0;
        r7.mTotalLength = r3;
        r3 = 0;
        r5 = r4;
        r4 = r27;
    L_0x0312:
        if (r3 >= r1) goto L_0x041f;
    L_0x0314:
        r11 = r7.getVirtualChildAt(r3);
        if (r11 == 0) goto L_0x0407;
    L_0x031a:
        r27 = r10;
        r10 = r11.getVisibility();
        r30 = r15;
        r15 = 8;
        if (r10 != r15) goto L_0x032f;
    L_0x0326:
        r31 = r1;
        r16 = r2;
        r1 = r8;
        r8 = r40;
        goto L_0x0412;
    L_0x032f:
        r10 = r11.getLayoutParams();
        r10 = (android.widget.LinearLayout.LayoutParams) r10;
        r15 = r10.weight;
        r31 = (r15 > r23 ? 1 : (r15 == r23 ? 0 : -1));
        if (r31 <= 0) goto L_0x039b;
    L_0x033b:
        r31 = r1;
        r1 = (float) r8;
        r1 = r1 * r15;
        r1 = r1 / r0;
        r1 = (int) r1;
        r8 = r8 - r1;
        r0 = r0 - r15;
        r32 = r0;
        r0 = r7.mUseLargestChild;
        if (r0 == 0) goto L_0x034f;
    L_0x0349:
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r14 == r0) goto L_0x034f;
    L_0x034d:
        r0 = r2;
        goto L_0x0362;
    L_0x034f:
        r0 = r10.height;
        if (r0 != 0) goto L_0x035d;
    L_0x0353:
        r0 = r7.mAllowInconsistentMeasurement;
        if (r0 == 0) goto L_0x035b;
    L_0x0357:
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r14 != r0) goto L_0x035d;
    L_0x035b:
        r0 = r1;
        goto L_0x0362;
    L_0x035d:
        r0 = r11.getMeasuredHeight();
        r0 = r0 + r1;
        r33 = r1;
        r16 = r2;
        r1 = 0;
        r2 = java.lang.Math.max(r1, r0);
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r2 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r1);
        r1 = r7.mPaddingLeft;
        r34 = r0;
        r0 = r7.mPaddingRight;
        r1 = r1 + r0;
        r0 = r10.leftMargin;
        r1 = r1 + r0;
        r0 = r10.rightMargin;
        r1 = r1 + r0;
        r0 = r10.width;
        r35 = r8;
        r8 = r40;
        r0 = android.view.ViewGroup.getChildMeasureSpec(r8, r1, r0);
        r11.measure(r0, r2);
        r1 = r11.getMeasuredState();
        r1 = r1 & -256;
        r4 = android.view.View.combineMeasuredStates(r4, r1);
        r0 = r32;
        r1 = r35;
        goto L_0x03a2;
    L_0x039b:
        r31 = r1;
        r16 = r2;
        r1 = r8;
        r8 = r40;
    L_0x03a2:
        r2 = r10.leftMargin;
        r32 = r0;
        r0 = r10.rightMargin;
        r2 = r2 + r0;
        r0 = r11.getMeasuredWidth();
        r0 = r0 + r2;
        r13 = java.lang.Math.max(r13, r0);
        r33 = r0;
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r12 == r0) goto L_0x03c2;
    L_0x03b8:
        r0 = r10.width;
        r34 = r1;
        r1 = -1;
        if (r0 != r1) goto L_0x03c4;
    L_0x03bf:
        r0 = r25;
        goto L_0x03c5;
    L_0x03c2:
        r34 = r1;
    L_0x03c4:
        r0 = 0;
        if (r0 == 0) goto L_0x03ca;
    L_0x03c8:
        r1 = r2;
        goto L_0x03cc;
    L_0x03ca:
        r1 = r33;
    L_0x03cc:
        r1 = java.lang.Math.max(r5, r1);
        if (r20 == 0) goto L_0x03dc;
    L_0x03d2:
        r5 = r10.width;
        r35 = r0;
        r0 = -1;
        if (r5 != r0) goto L_0x03df;
    L_0x03d9:
        r5 = r25;
        goto L_0x03e0;
    L_0x03dc:
        r35 = r0;
        r0 = -1;
    L_0x03df:
        r5 = 0;
    L_0x03e0:
        r0 = r7.mTotalLength;
        r20 = r11.getMeasuredHeight();
        r20 = r0 + r20;
        r36 = r1;
        r1 = r10.topMargin;
        r20 = r20 + r1;
        r1 = r10.bottomMargin;
        r20 = r20 + r1;
        r1 = r7.getNextLocationOffset(r11);
        r1 = r20 + r1;
        r1 = java.lang.Math.max(r0, r1);
        r7.mTotalLength = r1;
        r20 = r5;
        r0 = r32;
        r1 = r34;
        r5 = r36;
        goto L_0x0412;
    L_0x0407:
        r31 = r1;
        r16 = r2;
        r1 = r8;
        r27 = r10;
        r30 = r15;
        r8 = r40;
    L_0x0412:
        r3 = r3 + 1;
        r8 = r1;
        r2 = r16;
        r10 = r27;
        r15 = r30;
        r1 = r31;
        goto L_0x0312;
    L_0x041f:
        r31 = r1;
        r16 = r2;
        r1 = r8;
        r27 = r10;
        r30 = r15;
        r8 = r40;
        r2 = r7.mTotalLength;
        r3 = r7.mPaddingTop;
        r10 = r7.mPaddingBottom;
        r3 = r3 + r10;
        r2 = r2 + r3;
        r7.mTotalLength = r2;
    L_0x0435:
        if (r20 != 0) goto L_0x043c;
    L_0x0437:
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r12 == r0) goto L_0x043c;
    L_0x043b:
        r13 = r5;
    L_0x043c:
        r0 = r7.mPaddingLeft;
        r2 = r7.mPaddingRight;
        r0 = r0 + r2;
        r13 = r13 + r0;
        r0 = r39.getSuggestedMinimumWidth();
        r0 = java.lang.Math.max(r13, r0);
        r2 = android.view.View.resolveSizeAndState(r0, r8, r4);
        r7.setMeasuredDimension(r2, r9);
        if (r21 == 0) goto L_0x0459;
    L_0x0453:
        r2 = r31;
        r7.forceUniformWidth(r2, r6);
        goto L_0x045b;
    L_0x0459:
        r2 = r31;
    L_0x045b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.LinearLayout.measureVertical(int, int):void");
    }

    private void forceUniformWidth(int count, int heightMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8)) {
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

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:212:0x057e  */
    /* JADX WARNING: Removed duplicated region for block: B:204:0x0546  */
    /* JADX WARNING: Removed duplicated region for block: B:233:0x062e  */
    /* JADX WARNING: Removed duplicated region for block: B:232:0x0626  */
    public void measureHorizontal(int r49, int r50) {
        /*
        r48 = this;
        r7 = r48;
        r8 = r49;
        r9 = r50;
        r10 = 0;
        r7.mTotalLength = r10;
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r3 = 0;
        r4 = 1;
        r5 = 0;
        r11 = r48.getVirtualChildCount();
        r12 = android.view.View.MeasureSpec.getMode(r49);
        r13 = android.view.View.MeasureSpec.getMode(r50);
        r6 = 0;
        r14 = 0;
        r15 = r7.mMaxAscent;
        if (r15 == 0) goto L_0x0025;
    L_0x0021:
        r15 = r7.mMaxDescent;
        if (r15 != 0) goto L_0x002e;
    L_0x0025:
        r15 = 4;
        r10 = new int[r15];
        r7.mMaxAscent = r10;
        r10 = new int[r15];
        r7.mMaxDescent = r10;
    L_0x002e:
        r10 = r7.mMaxAscent;
        r15 = r7.mMaxDescent;
        r17 = 3;
        r18 = r6;
        r6 = -1;
        r10[r17] = r6;
        r19 = 2;
        r10[r19] = r6;
        r20 = 1;
        r10[r20] = r6;
        r16 = 0;
        r10[r16] = r6;
        r15[r17] = r6;
        r15[r19] = r6;
        r15[r20] = r6;
        r15[r16] = r6;
        r6 = r7.mBaselineAligned;
        r22 = r14;
        r14 = r7.mUseLargestChild;
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r12 != r9) goto L_0x005a;
    L_0x0057:
        r23 = r20;
        goto L_0x005c;
    L_0x005a:
        r23 = 0;
    L_0x005c:
        r24 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r25 = 0;
        r26 = 0;
        r27 = 0;
        r28 = r1;
        r1 = r3;
        r3 = r0;
        r0 = r5;
        r5 = r27;
        r47 = r18;
        r18 = r4;
        r4 = r24;
        r24 = r47;
    L_0x0073:
        r30 = 0;
        if (r5 >= r11) goto L_0x025f;
    L_0x0077:
        r9 = r7.getVirtualChildAt(r5);
        if (r9 != 0) goto L_0x0093;
    L_0x007d:
        r32 = r1;
        r1 = r7.mTotalLength;
        r29 = r7.measureNullChild(r5);
        r1 = r1 + r29;
        r7.mTotalLength = r1;
        r21 = r6;
        r6 = r28;
        r1 = r32;
        r32 = r12;
        goto L_0x0251;
    L_0x0093:
        r32 = r1;
        r1 = r9.getVisibility();
        r33 = r2;
        r2 = 8;
        if (r1 != r2) goto L_0x00b0;
    L_0x009f:
        r1 = r7.getChildrenSkipCount(r9, r5);
        r5 = r5 + r1;
        r21 = r6;
        r6 = r28;
        r1 = r32;
        r2 = r33;
        r32 = r12;
        goto L_0x0251;
    L_0x00b0:
        r26 = r26 + 1;
        r1 = r7.hasDividerBeforeChildAt(r5);
        if (r1 == 0) goto L_0x00bf;
    L_0x00b8:
        r1 = r7.mTotalLength;
        r2 = r7.mDividerWidth;
        r1 = r1 + r2;
        r7.mTotalLength = r1;
    L_0x00bf:
        r1 = r9.getLayoutParams();
        r2 = r1;
        r2 = (android.widget.LinearLayout.LayoutParams) r2;
        r1 = r2.weight;
        r31 = r0 + r1;
        r0 = r2.width;
        if (r0 != 0) goto L_0x00d7;
    L_0x00ce:
        r0 = r2.weight;
        r0 = (r0 > r30 ? 1 : (r0 == r30 ? 0 : -1));
        if (r0 <= 0) goto L_0x00d7;
    L_0x00d4:
        r0 = r20;
        goto L_0x00d8;
    L_0x00d7:
        r0 = 0;
    L_0x00d8:
        r34 = r0;
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r12 != r0) goto L_0x0139;
    L_0x00de:
        if (r34 == 0) goto L_0x0139;
    L_0x00e0:
        if (r23 == 0) goto L_0x00ef;
    L_0x00e2:
        r0 = r7.mTotalLength;
        r1 = r2.leftMargin;
        r35 = r3;
        r3 = r2.rightMargin;
        r1 = r1 + r3;
        r0 = r0 + r1;
        r7.mTotalLength = r0;
        goto L_0x00ff;
    L_0x00ef:
        r35 = r3;
        r0 = r7.mTotalLength;
        r1 = r2.leftMargin;
        r1 = r1 + r0;
        r3 = r2.rightMargin;
        r1 = r1 + r3;
        r1 = java.lang.Math.max(r0, r1);
        r7.mTotalLength = r1;
    L_0x00ff:
        if (r6 == 0) goto L_0x0127;
        r0 = android.view.View.MeasureSpec.getSize(r49);
        r1 = 0;
        r0 = android.view.View.MeasureSpec.makeSafeMeasureSpec(r0, r1);
        r3 = android.view.View.MeasureSpec.getSize(r50);
        r3 = android.view.View.MeasureSpec.makeSafeMeasureSpec(r3, r1);
        r9.measure(r0, r3);
        r1 = r2;
        r2 = r4;
        r8 = r5;
        r21 = r6;
        r37 = r32;
        r39 = r33;
        r40 = r35;
        r32 = r12;
        r12 = -1;
        goto L_0x01ac;
    L_0x0127:
        r22 = 1;
        r1 = r2;
        r2 = r4;
        r8 = r5;
        r21 = r6;
        r37 = r32;
        r39 = r33;
        r40 = r35;
        r32 = r12;
        r12 = -1;
        goto L_0x01ac;
    L_0x0139:
        r35 = r3;
        if (r34 == 0) goto L_0x0140;
    L_0x013d:
        r0 = -2;
        r2.width = r0;
    L_0x0140:
        r0 = (r31 > r30 ? 1 : (r31 == r30 ? 0 : -1));
        if (r0 != 0) goto L_0x0147;
    L_0x0144:
        r0 = r7.mTotalLength;
        goto L_0x0148;
    L_0x0147:
        r0 = 0;
    L_0x0148:
        r3 = r4;
        r4 = r0;
        r36 = 0;
        r0 = r48;
        r37 = r32;
        r1 = r9;
        r38 = r2;
        r39 = r33;
        r2 = r5;
        r41 = r3;
        r40 = r35;
        r3 = r49;
        r8 = r5;
        r5 = r50;
        r21 = r6;
        r32 = r12;
        r12 = -1;
        r6 = r36;
        r0.measureChildBeforeLayout(r1, r2, r3, r4, r5, r6);
        r0 = r9.getMeasuredWidth();
        if (r34 == 0) goto L_0x0177;
    L_0x016f:
        r1 = r38;
        r2 = 0;
        r1.width = r2;
        r25 = r25 + r0;
        goto L_0x0179;
    L_0x0177:
        r1 = r38;
    L_0x0179:
        if (r23 == 0) goto L_0x018c;
    L_0x017b:
        r2 = r7.mTotalLength;
        r3 = r1.leftMargin;
        r3 = r3 + r0;
        r5 = r1.rightMargin;
        r3 = r3 + r5;
        r5 = r7.getNextLocationOffset(r9);
        r3 = r3 + r5;
        r2 = r2 + r3;
        r7.mTotalLength = r2;
        goto L_0x01a1;
    L_0x018c:
        r2 = r7.mTotalLength;
        r3 = r2 + r0;
        r5 = r1.leftMargin;
        r3 = r3 + r5;
        r5 = r1.rightMargin;
        r3 = r3 + r5;
        r5 = r7.getNextLocationOffset(r9);
        r3 = r3 + r5;
        r3 = java.lang.Math.max(r2, r3);
        r7.mTotalLength = r3;
    L_0x01a1:
        if (r14 == 0) goto L_0x01aa;
    L_0x01a3:
        r2 = r41;
        r2 = java.lang.Math.max(r0, r2);
        goto L_0x01ac;
    L_0x01aa:
        r2 = r41;
    L_0x01ac:
        r0 = 0;
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r13 == r3) goto L_0x01b8;
    L_0x01b1:
        r3 = r1.height;
        if (r3 != r12) goto L_0x01b8;
    L_0x01b5:
        r24 = 1;
        r0 = 1;
    L_0x01b8:
        r3 = r1.topMargin;
        r4 = r1.bottomMargin;
        r3 = r3 + r4;
        r4 = r9.getMeasuredHeight();
        r4 = r4 + r3;
        r5 = r9.getMeasuredState();
        r6 = r28;
        r5 = android.view.View.combineMeasuredStates(r6, r5);
        if (r21 == 0) goto L_0x0203;
    L_0x01ce:
        r6 = r9.getBaseline();
        if (r6 == r12) goto L_0x01fe;
    L_0x01d4:
        r12 = r1.gravity;
        if (r12 >= 0) goto L_0x01db;
    L_0x01d8:
        r12 = r7.mGravity;
        goto L_0x01dd;
    L_0x01db:
        r12 = r1.gravity;
    L_0x01dd:
        r12 = r12 & 112;
        r33 = r12 >> 4;
        r29 = -2;
        r29 = r33 & -2;
        r29 = r29 >> 1;
        r33 = r2;
        r2 = r10[r29];
        r2 = java.lang.Math.max(r2, r6);
        r10[r29] = r2;
        r2 = r15[r29];
        r35 = r3;
        r3 = r4 - r6;
        r2 = java.lang.Math.max(r2, r3);
        r15[r29] = r2;
        goto L_0x0207;
    L_0x01fe:
        r33 = r2;
        r35 = r3;
        goto L_0x0207;
    L_0x0203:
        r33 = r2;
        r35 = r3;
    L_0x0207:
        r3 = r40;
        r2 = java.lang.Math.max(r3, r4);
        if (r18 == 0) goto L_0x0217;
    L_0x020f:
        r3 = r1.height;
        r6 = -1;
        if (r3 != r6) goto L_0x0217;
    L_0x0214:
        r3 = r20;
        goto L_0x0218;
    L_0x0217:
        r3 = 0;
    L_0x0218:
        r6 = r1.weight;
        r6 = (r6 > r30 ? 1 : (r6 == r30 ? 0 : -1));
        if (r6 <= 0) goto L_0x0230;
        if (r0 == 0) goto L_0x0224;
    L_0x0221:
        r6 = r35;
        goto L_0x0225;
    L_0x0224:
        r6 = r4;
    L_0x0225:
        r12 = r37;
        r6 = java.lang.Math.max(r12, r6);
        r38 = r1;
        r1 = r39;
        goto L_0x0241;
    L_0x0230:
        r12 = r37;
        if (r0 == 0) goto L_0x0237;
    L_0x0234:
        r6 = r35;
        goto L_0x0238;
    L_0x0237:
        r6 = r4;
    L_0x0238:
        r38 = r1;
        r1 = r39;
        r1 = java.lang.Math.max(r1, r6);
        r6 = r12;
    L_0x0241:
        r12 = r7.getChildrenSkipCount(r9, r8);
        r8 = r8 + r12;
        r18 = r3;
        r0 = r31;
        r4 = r33;
        r3 = r2;
        r2 = r1;
        r1 = r6;
        r6 = r5;
        r5 = r8;
    L_0x0251:
        r5 = r5 + 1;
        r8 = r49;
        r28 = r6;
        r6 = r21;
        r12 = r32;
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        goto L_0x0073;
    L_0x025f:
        r8 = r5;
        r21 = r6;
        r32 = r12;
        r6 = r28;
        r12 = r1;
        r1 = r2;
        r2 = r4;
        if (r26 <= 0) goto L_0x0278;
    L_0x026b:
        r4 = r7.hasDividerBeforeChildAt(r11);
        if (r4 == 0) goto L_0x0278;
    L_0x0271:
        r4 = r7.mTotalLength;
        r5 = r7.mDividerWidth;
        r4 = r4 + r5;
        r7.mTotalLength = r4;
    L_0x0278:
        r4 = r10[r20];
        r5 = -1;
        if (r4 != r5) goto L_0x028e;
    L_0x027d:
        r4 = 0;
        r8 = r10[r4];
        if (r8 != r5) goto L_0x028e;
    L_0x0282:
        r4 = r10[r19];
        if (r4 != r5) goto L_0x028e;
    L_0x0286:
        r4 = r10[r17];
        if (r4 == r5) goto L_0x028b;
    L_0x028a:
        goto L_0x028e;
    L_0x028b:
        r33 = r6;
        goto L_0x02c0;
    L_0x028e:
        r4 = r10[r17];
        r5 = 0;
        r8 = r10[r5];
        r9 = r10[r20];
        r5 = r10[r19];
        r5 = java.lang.Math.max(r9, r5);
        r5 = java.lang.Math.max(r8, r5);
        r4 = java.lang.Math.max(r4, r5);
        r5 = r15[r17];
        r8 = 0;
        r9 = r15[r8];
        r8 = r15[r20];
        r33 = r6;
        r6 = r15[r19];
        r6 = java.lang.Math.max(r8, r6);
        r6 = java.lang.Math.max(r9, r6);
        r5 = java.lang.Math.max(r5, r6);
        r6 = r4 + r5;
        r3 = java.lang.Math.max(r3, r6);
    L_0x02c0:
        if (r14 == 0) goto L_0x033c;
    L_0x02c2:
        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r5 = r32;
        if (r5 == r4) goto L_0x02cf;
    L_0x02c8:
        if (r5 != 0) goto L_0x02cb;
    L_0x02ca:
        goto L_0x02cf;
    L_0x02cb:
        r32 = r3;
        goto L_0x0340;
    L_0x02cf:
        r4 = 0;
        r7.mTotalLength = r4;
        r4 = 0;
    L_0x02d3:
        if (r4 >= r11) goto L_0x0337;
    L_0x02d5:
        r6 = r7.getVirtualChildAt(r4);
        if (r6 != 0) goto L_0x02e9;
    L_0x02db:
        r8 = r7.mTotalLength;
        r9 = r7.measureNullChild(r4);
        r8 = r8 + r9;
        r7.mTotalLength = r8;
        r32 = r3;
        r34 = r4;
        goto L_0x0332;
    L_0x02e9:
        r8 = r6.getVisibility();
        r9 = 8;
        if (r8 != r9) goto L_0x02fb;
    L_0x02f1:
        r8 = r7.getChildrenSkipCount(r6, r4);
        r4 = r4 + r8;
        r32 = r3;
        r34 = r4;
        goto L_0x0332;
        r8 = r6.getLayoutParams();
        r8 = (android.widget.LinearLayout.LayoutParams) r8;
        if (r23 == 0) goto L_0x0319;
    L_0x0304:
        r9 = r7.mTotalLength;
        r32 = r3;
        r3 = r8.leftMargin;
        r3 = r3 + r2;
        r34 = r4;
        r4 = r8.rightMargin;
        r3 = r3 + r4;
        r4 = r7.getNextLocationOffset(r6);
        r3 = r3 + r4;
        r9 = r9 + r3;
        r7.mTotalLength = r9;
        goto L_0x0332;
    L_0x0319:
        r32 = r3;
        r34 = r4;
        r3 = r7.mTotalLength;
        r4 = r3 + r2;
        r9 = r8.leftMargin;
        r4 = r4 + r9;
        r9 = r8.rightMargin;
        r4 = r4 + r9;
        r9 = r7.getNextLocationOffset(r6);
        r4 = r4 + r9;
        r4 = java.lang.Math.max(r3, r4);
        r7.mTotalLength = r4;
    L_0x0332:
        r4 = r34 + 1;
        r3 = r32;
        goto L_0x02d3;
    L_0x0337:
        r32 = r3;
        r34 = r4;
        goto L_0x0340;
    L_0x033c:
        r5 = r32;
        r32 = r3;
    L_0x0340:
        r3 = r7.mTotalLength;
        r4 = r7.mPaddingLeft;
        r6 = r7.mPaddingRight;
        r4 = r4 + r6;
        r3 = r3 + r4;
        r7.mTotalLength = r3;
        r3 = r7.mTotalLength;
        r4 = r48.getSuggestedMinimumWidth();
        r3 = java.lang.Math.max(r3, r4);
        r4 = r49;
        r6 = 0;
        r8 = android.view.View.resolveSizeAndState(r3, r4, r6);
        r6 = 16777215; // 0xffffff float:2.3509886E-38 double:8.2890456E-317;
        r3 = r8 & r6;
        r6 = r7.mTotalLength;
        r6 = r3 - r6;
        r9 = r7.mAllowInconsistentMeasurement;
        if (r9 == 0) goto L_0x036a;
    L_0x0368:
        r9 = 0;
        goto L_0x036c;
    L_0x036a:
        r9 = r25;
    L_0x036c:
        r6 = r6 + r9;
        if (r22 != 0) goto L_0x040a;
    L_0x036f:
        r34 = sRemeasureWeightedChildren;
        if (r34 != 0) goto L_0x0375;
    L_0x0373:
        if (r6 == 0) goto L_0x0381;
    L_0x0375:
        r34 = (r0 > r30 ? 1 : (r0 == r30 ? 0 : -1));
        if (r34 <= 0) goto L_0x0381;
    L_0x0379:
        r35 = r0;
        r36 = r3;
        r37 = r6;
        goto L_0x0410;
    L_0x0381:
        r1 = java.lang.Math.max(r1, r12);
        if (r14 == 0) goto L_0x03ec;
    L_0x0387:
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r5 == r9) goto L_0x03ec;
    L_0x038b:
        r9 = 0;
    L_0x038c:
        if (r9 >= r11) goto L_0x03e3;
    L_0x038e:
        r35 = r0;
        r0 = r7.getVirtualChildAt(r9);
        if (r0 == 0) goto L_0x03d2;
    L_0x0396:
        r16 = r1;
        r1 = r0.getVisibility();
        r36 = r3;
        r3 = 8;
        if (r1 != r3) goto L_0x03a5;
    L_0x03a2:
        r37 = r6;
        goto L_0x03d8;
        r1 = r0.getLayoutParams();
        r1 = (android.widget.LinearLayout.LayoutParams) r1;
        r3 = r1.weight;
        r17 = (r3 > r30 ? 1 : (r3 == r30 ? 0 : -1));
        if (r17 <= 0) goto L_0x03cb;
        r17 = r1;
        r19 = r3;
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r3 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r1);
        r37 = r6;
        r6 = r0.getMeasuredHeight();
        r6 = android.view.View.MeasureSpec.makeMeasureSpec(r6, r1);
        r0.measure(r3, r6);
        goto L_0x03d8;
    L_0x03cb:
        r17 = r1;
        r19 = r3;
        r37 = r6;
        goto L_0x03d8;
    L_0x03d2:
        r16 = r1;
        r36 = r3;
        r37 = r6;
    L_0x03d8:
        r9 = r9 + 1;
        r1 = r16;
        r0 = r35;
        r3 = r36;
        r6 = r37;
        goto L_0x038c;
    L_0x03e3:
        r35 = r0;
        r16 = r1;
        r36 = r3;
        r37 = r6;
        goto L_0x03f4;
    L_0x03ec:
        r35 = r0;
        r16 = r1;
        r36 = r3;
        r37 = r6;
    L_0x03f4:
        r41 = r2;
        r43 = r5;
        r38 = r8;
        r3 = r32;
        r9 = r33;
        r0 = r37;
        r5 = r50;
        r33 = r11;
        r37 = r12;
        r32 = r14;
        goto L_0x0600;
    L_0x040a:
        r35 = r0;
        r36 = r3;
        r37 = r6;
    L_0x0410:
        r0 = r7.mWeightSum;
        r3 = (r0 > r30 ? 1 : (r0 == r30 ? 0 : -1));
        if (r3 <= 0) goto L_0x0417;
    L_0x0416:
        goto L_0x0419;
    L_0x0417:
        r0 = r35;
    L_0x0419:
        r3 = -1;
        r10[r17] = r3;
        r10[r19] = r3;
        r10[r20] = r3;
        r6 = 0;
        r10[r6] = r3;
        r15[r17] = r3;
        r15[r19] = r3;
        r15[r20] = r3;
        r15[r6] = r3;
        r3 = -1;
        r7.mTotalLength = r6;
        r6 = 0;
        r9 = r33;
        r47 = r1;
        r1 = r0;
        r0 = r37;
        r37 = r12;
        r12 = r47;
    L_0x043a:
        if (r6 >= r11) goto L_0x05a7;
    L_0x043c:
        r32 = r14;
        r14 = r7.getVirtualChildAt(r6);
        if (r14 == 0) goto L_0x058b;
    L_0x0444:
        r4 = r14.getVisibility();
        r33 = r11;
        r11 = 8;
        if (r4 != r11) goto L_0x045a;
    L_0x044e:
        r41 = r2;
        r43 = r5;
        r38 = r8;
        r29 = -2;
        r5 = r50;
        goto L_0x0597;
    L_0x045a:
        r4 = r14.getLayoutParams();
        r4 = (android.widget.LinearLayout.LayoutParams) r4;
        r11 = r4.weight;
        r38 = (r11 > r30 ? 1 : (r11 == r30 ? 0 : -1));
        if (r38 <= 0) goto L_0x04cd;
    L_0x0466:
        r38 = r8;
        r8 = (float) r0;
        r8 = r8 * r11;
        r8 = r8 / r1;
        r8 = (int) r8;
        r0 = r0 - r8;
        r1 = r1 - r11;
        r39 = r0;
        r0 = r7.mUseLargestChild;
        if (r0 == 0) goto L_0x047a;
    L_0x0474:
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r5 == r0) goto L_0x047a;
    L_0x0478:
        r0 = r2;
        goto L_0x048d;
    L_0x047a:
        r0 = r4.width;
        if (r0 != 0) goto L_0x0488;
    L_0x047e:
        r0 = r7.mAllowInconsistentMeasurement;
        if (r0 == 0) goto L_0x0486;
    L_0x0482:
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r5 != r0) goto L_0x0488;
    L_0x0486:
        r0 = r8;
        goto L_0x048d;
    L_0x0488:
        r0 = r14.getMeasuredWidth();
        r0 = r0 + r8;
        r40 = r1;
        r41 = r2;
        r1 = 0;
        r2 = java.lang.Math.max(r1, r0);
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r2 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r1);
        r1 = r7.mPaddingTop;
        r42 = r0;
        r0 = r7.mPaddingBottom;
        r1 = r1 + r0;
        r0 = r4.topMargin;
        r1 = r1 + r0;
        r0 = r4.bottomMargin;
        r1 = r1 + r0;
        r0 = r4.height;
        r43 = r5;
        r27 = r8;
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = r50;
        r0 = android.view.ViewGroup.getChildMeasureSpec(r5, r1, r0);
        r14.measure(r2, r0);
        r1 = r14.getMeasuredState();
        r34 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r1 = r1 & r34;
        r9 = android.view.View.combineMeasuredStates(r9, r1);
        r0 = r39;
        r1 = r40;
        goto L_0x04d7;
    L_0x04cd:
        r41 = r2;
        r43 = r5;
        r38 = r8;
        r8 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = r50;
    L_0x04d7:
        if (r23 == 0) goto L_0x04f4;
    L_0x04d9:
        r2 = r7.mTotalLength;
        r27 = r14.getMeasuredWidth();
        r8 = r4.leftMargin;
        r27 = r27 + r8;
        r8 = r4.rightMargin;
        r27 = r27 + r8;
        r8 = r7.getNextLocationOffset(r14);
        r27 = r27 + r8;
        r2 = r2 + r27;
        r7.mTotalLength = r2;
        r27 = r0;
        goto L_0x050e;
    L_0x04f4:
        r2 = r7.mTotalLength;
        r8 = r14.getMeasuredWidth();
        r8 = r8 + r2;
        r27 = r0;
        r0 = r4.leftMargin;
        r8 = r8 + r0;
        r0 = r4.rightMargin;
        r8 = r8 + r0;
        r0 = r7.getNextLocationOffset(r14);
        r8 = r8 + r0;
        r0 = java.lang.Math.max(r2, r8);
        r7.mTotalLength = r0;
    L_0x050e:
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r13 == r0) goto L_0x051a;
    L_0x0512:
        r0 = r4.height;
        r2 = -1;
        if (r0 != r2) goto L_0x051a;
    L_0x0517:
        r0 = r20;
        goto L_0x051b;
    L_0x051a:
        r0 = 0;
    L_0x051b:
        r2 = r4.topMargin;
        r8 = r4.bottomMargin;
        r2 = r2 + r8;
        r8 = r14.getMeasuredHeight();
        r8 = r8 + r2;
        r3 = java.lang.Math.max(r3, r8);
        r40 = r0;
        if (r0 == 0) goto L_0x0530;
    L_0x052e:
        r0 = r2;
        goto L_0x0531;
    L_0x0530:
        r0 = r8;
    L_0x0531:
        r0 = java.lang.Math.max(r12, r0);
        if (r18 == 0) goto L_0x0541;
    L_0x0537:
        r12 = r4.height;
        r42 = r0;
        r0 = -1;
        if (r12 != r0) goto L_0x0543;
    L_0x053e:
        r0 = r20;
        goto L_0x0544;
    L_0x0541:
        r42 = r0;
    L_0x0543:
        r0 = 0;
    L_0x0544:
        if (r21 == 0) goto L_0x057e;
    L_0x0546:
        r12 = r14.getBaseline();
        r18 = r0;
        r0 = -1;
        if (r12 == r0) goto L_0x0579;
    L_0x054f:
        r0 = r4.gravity;
        if (r0 >= 0) goto L_0x0556;
    L_0x0553:
        r0 = r7.mGravity;
        goto L_0x0558;
    L_0x0556:
        r0 = r4.gravity;
    L_0x0558:
        r0 = r0 & 112;
        r44 = r0 >> 4;
        r29 = -2;
        r44 = r44 & -2;
        r44 = r44 >> 1;
        r45 = r0;
        r0 = r10[r44];
        r0 = java.lang.Math.max(r0, r12);
        r10[r44] = r0;
        r0 = r15[r44];
        r46 = r1;
        r1 = r8 - r12;
        r0 = java.lang.Math.max(r0, r1);
        r15[r44] = r0;
        goto L_0x0584;
    L_0x0579:
        r46 = r1;
        r29 = -2;
        goto L_0x0584;
    L_0x057e:
        r18 = r0;
        r46 = r1;
        r29 = -2;
    L_0x0584:
        r0 = r27;
        r12 = r42;
        r1 = r46;
        goto L_0x0597;
    L_0x058b:
        r41 = r2;
        r43 = r5;
        r38 = r8;
        r33 = r11;
        r29 = -2;
        r5 = r50;
    L_0x0597:
        r6 = r6 + 1;
        r4 = r49;
        r14 = r32;
        r11 = r33;
        r8 = r38;
        r2 = r41;
        r5 = r43;
        goto L_0x043a;
    L_0x05a7:
        r41 = r2;
        r43 = r5;
        r38 = r8;
        r33 = r11;
        r32 = r14;
        r5 = r50;
        r2 = r7.mTotalLength;
        r4 = r7.mPaddingLeft;
        r6 = r7.mPaddingRight;
        r4 = r4 + r6;
        r2 = r2 + r4;
        r7.mTotalLength = r2;
        r2 = r10[r20];
        r4 = -1;
        if (r2 != r4) goto L_0x05cf;
    L_0x05c2:
        r2 = 0;
        r6 = r10[r2];
        if (r6 != r4) goto L_0x05cf;
    L_0x05c7:
        r2 = r10[r19];
        if (r2 != r4) goto L_0x05cf;
    L_0x05cb:
        r2 = r10[r17];
        if (r2 == r4) goto L_0x05fe;
    L_0x05cf:
        r2 = r10[r17];
        r4 = 0;
        r6 = r10[r4];
        r8 = r10[r20];
        r11 = r10[r19];
        r8 = java.lang.Math.max(r8, r11);
        r6 = java.lang.Math.max(r6, r8);
        r2 = java.lang.Math.max(r2, r6);
        r6 = r15[r17];
        r4 = r15[r4];
        r8 = r15[r20];
        r11 = r15[r19];
        r8 = java.lang.Math.max(r8, r11);
        r4 = java.lang.Math.max(r4, r8);
        r4 = java.lang.Math.max(r6, r4);
        r6 = r2 + r4;
        r3 = java.lang.Math.max(r3, r6);
    L_0x05fe:
        r16 = r12;
    L_0x0600:
        if (r18 != 0) goto L_0x0608;
    L_0x0602:
        r1 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r13 == r1) goto L_0x0608;
    L_0x0606:
        r3 = r16;
    L_0x0608:
        r1 = r7.mPaddingTop;
        r2 = r7.mPaddingBottom;
        r1 = r1 + r2;
        r3 = r3 + r1;
        r1 = r48.getSuggestedMinimumHeight();
        r1 = java.lang.Math.max(r3, r1);
        r2 = -16777216; // 0xffffffffff000000 float:-1.7014118E38 double:NaN;
        r2 = r2 & r9;
        r2 = r38 | r2;
        r3 = r9 << 16;
        r3 = android.view.View.resolveSizeAndState(r1, r5, r3);
        r7.setMeasuredDimension(r2, r3);
        if (r24 == 0) goto L_0x062e;
    L_0x0626:
        r2 = r49;
        r3 = r33;
        r7.forceUniformHeight(r3, r2);
        goto L_0x0632;
    L_0x062e:
        r2 = r49;
        r3 = r33;
    L_0x0632:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.LinearLayout.measureHorizontal(int, int):void");
    }

    private void forceUniformHeight(int count, int widthMeasureSpec) {
        int uniformMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (int i = 0; i < count; i++) {
            View child = getVirtualChildAt(i);
            if (!(child == null || child.getVisibility() == 8)) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                if (lp.height == -1) {
                    int oldWidth = lp.width;
                    lp.width = child.getMeasuredWidth();
                    measureChildWithMargins(child, widthMeasureSpec, 0, uniformMeasureSpec, 0);
                    lp.width = oldWidth;
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getChildrenSkipCount(View child, int index) {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public int measureNullChild(int childIndex) {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public void measureChildBeforeLayout(View child, int childIndex, int widthMeasureSpec, int totalWidth, int heightMeasureSpec, int totalHeight) {
        measureChildWithMargins(child, widthMeasureSpec, totalWidth, heightMeasureSpec, totalHeight);
    }

    /* Access modifiers changed, original: 0000 */
    public int getLocationOffset(View child) {
        return 0;
    }

    /* Access modifiers changed, original: 0000 */
    public int getNextLocationOffset(View child) {
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int l, int t, int r, int b) {
        if (this.mOrientation == 1) {
            layoutVertical(l, t, r, b);
        } else {
            layoutHorizontal(l, t, r, b);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void layoutVertical(int left, int top, int right, int bottom) {
        int paddingLeft = this.mPaddingLeft;
        int width = right - left;
        int childRight = width - this.mPaddingRight;
        int childSpace = (width - paddingLeft) - this.mPaddingRight;
        int count = getVirtualChildCount();
        int i = this.mGravity;
        int majorGravity = i & 112;
        int minorGravity = i & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        if (majorGravity == 16) {
            i = this.mPaddingTop + (((bottom - top) - this.mTotalLength) / 2);
        } else if (majorGravity != 80) {
            i = this.mPaddingTop;
        } else {
            i = ((this.mPaddingTop + bottom) - top) - this.mTotalLength;
        }
        int i2 = 0;
        while (i2 < count) {
            int paddingLeft2;
            View child = getVirtualChildAt(i2);
            int i3;
            if (child == null) {
                i += measureNullChild(i2);
                i3 = 1;
                paddingLeft2 = paddingLeft;
            } else if (child.getVisibility() != 8) {
                int gravity;
                int childLeft;
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int gravity2 = lp.gravity;
                if (gravity2 < 0) {
                    gravity = minorGravity;
                } else {
                    gravity = gravity2;
                }
                int layoutDirection = getLayoutDirection();
                gravity2 = Gravity.getAbsoluteGravity(gravity, layoutDirection) & 7;
                if (gravity2 == 1) {
                    childLeft = ((((childSpace - childWidth) / 2) + paddingLeft) + lp.leftMargin) - lp.rightMargin;
                } else if (gravity2 != 5) {
                    childLeft = lp.leftMargin + paddingLeft;
                } else {
                    childLeft = (childRight - childWidth) - lp.rightMargin;
                }
                if (hasDividerBeforeChildAt(i2)) {
                    i += this.mDividerHeight;
                }
                int childTop = i + lp.topMargin;
                gravity = childTop + getLocationOffset(child);
                paddingLeft2 = paddingLeft;
                paddingLeft = lp;
                setChildFrame(child, childLeft, gravity, childWidth, childHeight);
                i2 += getChildrenSkipCount(child, i2);
                i = childTop + ((childHeight + paddingLeft.bottomMargin) + getNextLocationOffset(child));
            } else {
                i3 = 1;
                paddingLeft2 = paddingLeft;
            }
            i2++;
            paddingLeft = paddingLeft2;
        }
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        if (layoutDirection != this.mLayoutDirection) {
            this.mLayoutDirection = layoutDirection;
            if (this.mOrientation == 0) {
                requestLayout();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00bd  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x00b9  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00c7  */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x010e  */
    public void layoutHorizontal(int r34, int r35, int r36, int r37) {
        /*
        r33 = this;
        r6 = r33;
        r7 = r33.isLayoutRtl();
        r8 = r6.mPaddingTop;
        r9 = r37 - r35;
        r0 = r6.mPaddingBottom;
        r10 = r9 - r0;
        r0 = r9 - r8;
        r1 = r6.mPaddingBottom;
        r11 = r0 - r1;
        r12 = r33.getVirtualChildCount();
        r0 = r6.mGravity;
        r1 = 8388615; // 0x800007 float:1.1754953E-38 double:4.1445265E-317;
        r13 = r0 & r1;
        r14 = r0 & 112;
        r15 = r6.mBaselineAligned;
        r5 = r6.mMaxAscent;
        r4 = r6.mMaxDescent;
        r3 = r33.getLayoutDirection();
        r0 = android.view.Gravity.getAbsoluteGravity(r13, r3);
        r16 = 2;
        r2 = 1;
        if (r0 == r2) goto L_0x0044;
    L_0x0034:
        r1 = 5;
        if (r0 == r1) goto L_0x003a;
    L_0x0037:
        r0 = r6.mPaddingLeft;
        goto L_0x004f;
    L_0x003a:
        r0 = r6.mPaddingLeft;
        r0 = r0 + r36;
        r0 = r0 - r34;
        r1 = r6.mTotalLength;
        r0 = r0 - r1;
        goto L_0x004f;
    L_0x0044:
        r0 = r6.mPaddingLeft;
        r1 = r36 - r34;
        r2 = r6.mTotalLength;
        r1 = r1 - r2;
        r1 = r1 / 2;
        r0 = r0 + r1;
    L_0x004f:
        r1 = 0;
        r2 = 1;
        if (r7 == 0) goto L_0x005b;
    L_0x0053:
        r1 = r12 + -1;
        r2 = -1;
        r18 = r1;
        r19 = r2;
        goto L_0x005f;
    L_0x005b:
        r18 = r1;
        r19 = r2;
    L_0x005f:
        r1 = 0;
        r2 = r1;
    L_0x0061:
        if (r2 >= r12) goto L_0x016b;
    L_0x0063:
        r1 = r19 * r2;
        r1 = r18 + r1;
        r20 = r7;
        r7 = r6.getVirtualChildAt(r1);
        if (r7 != 0) goto L_0x0085;
    L_0x006f:
        r21 = r6.measureNullChild(r1);
        r0 = r0 + r21;
        r22 = r3;
        r31 = r4;
        r32 = r5;
        r29 = r8;
        r26 = r9;
        r28 = r12;
        r21 = 1;
        goto L_0x0159;
    L_0x0085:
        r21 = r2;
        r2 = r7.getVisibility();
        r22 = r3;
        r3 = 8;
        if (r2 == r3) goto L_0x0148;
    L_0x0091:
        r23 = r7.getMeasuredWidth();
        r24 = r7.getMeasuredHeight();
        r2 = -1;
        r3 = r7.getLayoutParams();
        r3 = (android.widget.LinearLayout.LayoutParams) r3;
        r25 = r2;
        r2 = -1;
        if (r15 == 0) goto L_0x00b1;
    L_0x00a6:
        r26 = r9;
        r9 = r3.height;
        if (r9 == r2) goto L_0x00b3;
    L_0x00ac:
        r9 = r7.getBaseline();
        goto L_0x00b5;
    L_0x00b1:
        r26 = r9;
    L_0x00b3:
        r9 = r25;
    L_0x00b5:
        r2 = r3.gravity;
        if (r2 >= 0) goto L_0x00bd;
    L_0x00b9:
        r2 = r14;
        r27 = r2;
        goto L_0x00bf;
    L_0x00bd:
        r27 = r2;
    L_0x00bf:
        r2 = r27 & 112;
        r28 = r12;
        r12 = 16;
        if (r2 == r12) goto L_0x00fb;
    L_0x00c7:
        r12 = 48;
        if (r2 == r12) goto L_0x00e9;
    L_0x00cb:
        r12 = 80;
        if (r2 == r12) goto L_0x00d2;
    L_0x00cf:
        r2 = r8;
        r12 = r2;
        goto L_0x0108;
    L_0x00d2:
        r2 = r10 - r24;
        r12 = r3.bottomMargin;
        r2 = r2 - r12;
        r12 = -1;
        if (r9 == r12) goto L_0x00e7;
    L_0x00da:
        r12 = r7.getMeasuredHeight();
        r12 = r12 - r9;
        r25 = r4[r16];
        r25 = r25 - r12;
        r2 = r2 - r25;
        r12 = r2;
        goto L_0x0108;
    L_0x00e7:
        r12 = r2;
        goto L_0x0108;
    L_0x00e9:
        r2 = r3.topMargin;
        r2 = r2 + r8;
        r12 = -1;
        if (r9 == r12) goto L_0x00f8;
    L_0x00ef:
        r12 = 1;
        r17 = r5[r12];
        r17 = r17 - r9;
        r2 = r2 + r17;
        r12 = r2;
        goto L_0x0108;
    L_0x00f8:
        r12 = 1;
        r12 = r2;
        goto L_0x0108;
    L_0x00fb:
        r12 = 1;
        r2 = r11 - r24;
        r2 = r2 / 2;
        r2 = r2 + r8;
        r12 = r3.topMargin;
        r2 = r2 + r12;
        r12 = r3.bottomMargin;
        r2 = r2 - r12;
        r12 = r2;
    L_0x0108:
        r2 = r6.hasDividerBeforeChildAt(r1);
        if (r2 == 0) goto L_0x0111;
    L_0x010e:
        r2 = r6.mDividerWidth;
        r0 = r0 + r2;
    L_0x0111:
        r2 = r3.leftMargin;
        r25 = r0 + r2;
        r0 = r6.getLocationOffset(r7);
        r2 = r25 + r0;
        r0 = r33;
        r29 = r8;
        r8 = r1;
        r1 = r7;
        r17 = r21;
        r21 = 1;
        r30 = r9;
        r9 = r3;
        r3 = r12;
        r31 = r4;
        r4 = r23;
        r32 = r5;
        r5 = r24;
        r0.setChildFrame(r1, r2, r3, r4, r5);
        r0 = r9.rightMargin;
        r0 = r23 + r0;
        r1 = r6.getNextLocationOffset(r7);
        r0 = r0 + r1;
        r25 = r25 + r0;
        r0 = r6.getChildrenSkipCount(r7, r8);
        r2 = r17 + r0;
        r0 = r25;
        goto L_0x0159;
    L_0x0148:
        r31 = r4;
        r32 = r5;
        r29 = r8;
        r26 = r9;
        r28 = r12;
        r17 = r21;
        r21 = 1;
        r8 = r1;
        r2 = r17;
    L_0x0159:
        r2 = r2 + 1;
        r7 = r20;
        r3 = r22;
        r9 = r26;
        r12 = r28;
        r8 = r29;
        r4 = r31;
        r5 = r32;
        goto L_0x0061;
    L_0x016b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.LinearLayout.layoutHorizontal(int, int, int, int):void");
    }

    private void setChildFrame(View child, int left, int top, int width, int height) {
        child.layout(left, top, left + width, top + height);
    }

    public void setOrientation(int orientation) {
        if (this.mOrientation != orientation) {
            this.mOrientation = orientation;
            requestLayout();
        }
    }

    public int getOrientation() {
        return this.mOrientation;
    }

    @RemotableViewMethod
    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            if ((Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK & gravity) == 0) {
                gravity |= Gravity.START;
            }
            if ((gravity & 112) == 0) {
                gravity |= 48;
            }
            this.mGravity = gravity;
            requestLayout();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    @RemotableViewMethod
    public void setHorizontalGravity(int horizontalGravity) {
        int gravity = horizontalGravity & Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK;
        int i = this.mGravity;
        if ((Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK & i) != gravity) {
            this.mGravity = (-8388616 & i) | gravity;
            requestLayout();
        }
    }

    @RemotableViewMethod
    public void setVerticalGravity(int verticalGravity) {
        int gravity = verticalGravity & 112;
        int i = this.mGravity;
        if ((i & 112) != gravity) {
            this.mGravity = (i & -113) | gravity;
            requestLayout();
        }
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        int i = this.mOrientation;
        if (i == 0) {
            return new LayoutParams(-2, -2);
        }
        if (i == 1) {
            return new LayoutParams(-1, -2);
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams lp) {
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

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public CharSequence getAccessibilityClassName() {
        return LinearLayout.class.getName();
    }

    /* Access modifiers changed, original: protected */
    public void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("layout:baselineAligned", this.mBaselineAligned);
        encoder.addProperty("layout:baselineAlignedChildIndex", this.mBaselineAlignedChildIndex);
        encoder.addProperty("measurement:baselineChildTop", this.mBaselineChildTop);
        encoder.addProperty("measurement:orientation", this.mOrientation);
        encoder.addProperty("measurement:gravity", this.mGravity);
        encoder.addProperty("measurement:totalLength", this.mTotalLength);
        encoder.addProperty("layout:totalLength", this.mTotalLength);
        encoder.addProperty("layout:useLargestChild", this.mUseLargestChild);
    }
}
