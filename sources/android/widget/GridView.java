package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.RemotableViewMethod;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo.CollectionInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;
import android.view.animation.GridLayoutAnimationController.AnimationParameters;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.AbsListView.LayoutParams;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;
import com.miui.internal.variable.api.v29.Android_Widget_GridView.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_GridView.Interface;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@RemoteView
public class GridView extends AbsListView {
    public static final int AUTO_FIT = -1;
    public static final int NO_STRETCH = 0;
    public static final int STRETCH_COLUMN_WIDTH = 2;
    public static final int STRETCH_SPACING = 1;
    public static final int STRETCH_SPACING_UNIFORM = 3;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 117521079)
    private int mColumnWidth;
    private int mGravity;
    @UnsupportedAppUsage
    private int mHorizontalSpacing;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 117521080)
    private int mNumColumns;
    private View mReferenceView;
    private View mReferenceViewInSelectedRow;
    @UnsupportedAppUsage
    private int mRequestedColumnWidth;
    @UnsupportedAppUsage
    private int mRequestedHorizontalSpacing;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769395)
    private int mRequestedNumColumns;
    private int mStretchMode;
    private final Rect mTempRect;
    @UnsupportedAppUsage
    private int mVerticalSpacing;

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<GridView> {
        private int mColumnWidthId;
        private int mGravityId;
        private int mHorizontalSpacingId;
        private int mNumColumnsId;
        private boolean mPropertiesMapped = false;
        private int mStretchModeId;
        private int mVerticalSpacingId;

        public void mapProperties(PropertyMapper propertyMapper) {
            String str = "columnWidth";
            this.mColumnWidthId = propertyMapper.mapInt(str, 16843031);
            this.mGravityId = propertyMapper.mapGravity("gravity", 16842927);
            this.mHorizontalSpacingId = propertyMapper.mapInt("horizontalSpacing", 16843028);
            this.mNumColumnsId = propertyMapper.mapInt("numColumns", 16843032);
            SparseArray<String> stretchModeEnumMapping = new SparseArray();
            stretchModeEnumMapping.put(0, "none");
            stretchModeEnumMapping.put(1, "spacingWidth");
            stretchModeEnumMapping.put(2, str);
            stretchModeEnumMapping.put(3, "spacingWidthUniform");
            Objects.requireNonNull(stretchModeEnumMapping);
            this.mStretchModeId = propertyMapper.mapIntEnum("stretchMode", 16843030, new -$$Lambda$QY3N4tzLteuFdjRnyJFCbR1ajSI(stretchModeEnumMapping));
            this.mVerticalSpacingId = propertyMapper.mapInt("verticalSpacing", 16843029);
            this.mPropertiesMapped = true;
        }

        public void readProperties(GridView node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readInt(this.mColumnWidthId, node.getColumnWidth());
                propertyReader.readGravity(this.mGravityId, node.getGravity());
                propertyReader.readInt(this.mHorizontalSpacingId, node.getHorizontalSpacing());
                propertyReader.readInt(this.mNumColumnsId, node.getNumColumns());
                propertyReader.readIntEnum(this.mStretchModeId, node.getStretchMode());
                propertyReader.readInt(this.mVerticalSpacingId, node.getVerticalSpacing());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface StretchMode {
    }

    public GridView(Context context) {
        this(context, null);
    }

    public GridView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842865);
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public GridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mNumColumns = -1;
        this.mHorizontalSpacing = 0;
        this.mVerticalSpacing = 0;
        this.mStretchMode = 2;
        this.mReferenceView = null;
        this.mReferenceViewInSelectedRow = null;
        this.mGravity = Gravity.START;
        this.mTempRect = new Rect();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GridView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.GridView, attrs, a, defStyleAttr, defStyleRes);
        setHorizontalSpacing(a.getDimensionPixelOffset(1, 0));
        setVerticalSpacing(a.getDimensionPixelOffset(2, 0));
        int index = a.getInt(3, 2);
        if (index >= 0) {
            setStretchMode(index);
        }
        int columnWidth = a.getDimensionPixelOffset(4, -1);
        if (columnWidth > 0) {
            setColumnWidth(columnWidth);
        }
        setNumColumns(a.getInt(5, 1));
        int index2 = a.getInt(0, -1);
        if (index2 >= 0) {
            setGravity(index2);
        }
        a.recycle();
    }

    public ListAdapter getAdapter() {
        return this.mAdapter;
    }

    @RemotableViewMethod(asyncImpl = "setRemoteViewsAdapterAsync")
    public void setRemoteViewsAdapter(Intent intent) {
        super.setRemoteViewsAdapter(intent);
    }

    public void setAdapter(ListAdapter adapter) {
        if (!(this.mAdapter == null || this.mDataSetObserver == null)) {
            this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        }
        resetList();
        this.mRecycler.clear();
        this.mAdapter = adapter;
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        super.setAdapter(adapter);
        if (this.mAdapter != null) {
            int position;
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
            this.mDataChanged = true;
            checkFocus();
            this.mDataSetObserver = new AdapterDataSetObserver();
            this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
            this.mRecycler.setViewTypeCount(this.mAdapter.getViewTypeCount());
            if (this.mStackFromBottom) {
                position = lookForSelectablePosition(this.mItemCount - 1, false);
            } else {
                position = lookForSelectablePosition(0, true);
            }
            setSelectedPositionInt(position);
            setNextSelectedPositionInt(position);
            checkSelectionChanged();
        } else {
            checkFocus();
            checkSelectionChanged();
        }
        requestLayout();
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:9:0x0015, code skipped:
            return -1;
     */
    public int lookForSelectablePosition(int r4, boolean r5) {
        /*
        r3 = this;
        r0 = r3.mAdapter;
        r1 = -1;
        if (r0 == 0) goto L_0x0015;
    L_0x0005:
        r2 = r3.isInTouchMode();
        if (r2 == 0) goto L_0x000c;
    L_0x000b:
        goto L_0x0015;
    L_0x000c:
        if (r4 < 0) goto L_0x0014;
    L_0x000e:
        r2 = r3.mItemCount;
        if (r4 < r2) goto L_0x0013;
    L_0x0012:
        goto L_0x0014;
    L_0x0013:
        return r4;
    L_0x0014:
        return r1;
    L_0x0015:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.GridView.lookForSelectablePosition(int, boolean):int");
    }

    /* Access modifiers changed, original: 0000 */
    public void fillGap(boolean down) {
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).fillGap(this, down);
        } else {
            originalFillGap(down);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void originalFillGap(boolean down) {
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int count = getChildCount();
        int paddingTop;
        int startOffset;
        int position;
        if (down) {
            paddingTop = 0;
            if ((this.mGroupFlags & 34) == 34) {
                paddingTop = getListPaddingTop();
            }
            startOffset = count > 0 ? getChildAt(count - 1).getBottom() + verticalSpacing : paddingTop;
            position = this.mFirstPosition + count;
            if (this.mStackFromBottom) {
                position += numColumns - 1;
            }
            fillDown(position, startOffset);
            correctTooHigh(numColumns, verticalSpacing, getChildCount());
            return;
        }
        paddingTop = 0;
        if ((this.mGroupFlags & 34) == 34) {
            paddingTop = getListPaddingBottom();
        }
        startOffset = count > 0 ? getChildAt(0).getTop() - verticalSpacing : getHeight() - paddingTop;
        position = this.mFirstPosition;
        if (this.mStackFromBottom) {
            position--;
        } else {
            position -= numColumns;
        }
        fillUp(position, startOffset);
        correctTooLow(numColumns, verticalSpacing, getChildCount());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private View fillDown(int pos, int nextTop) {
        View selectedView = null;
        int end = this.mBottom - this.mTop;
        if ((this.mGroupFlags & 34) == 34) {
            end -= this.mListPadding.bottom;
        }
        while (nextTop < end && pos < this.mItemCount) {
            View temp = makeRow(pos, nextTop, true);
            if (temp != null) {
                selectedView = temp;
            }
            nextTop = this.mReferenceView.getBottom() + this.mVerticalSpacing;
            pos += this.mNumColumns;
        }
        setVisibleRangeHint(this.mFirstPosition, (this.mFirstPosition + getChildCount()) - 1);
        return selectedView;
    }

    private View makeRow(int startPos, int y, boolean flow) {
        int nextLeft;
        int i;
        int last;
        int startPos2;
        int pos;
        int selectedPosition;
        int columnWidth = this.mColumnWidth;
        int horizontalSpacing = this.mHorizontalSpacing;
        boolean isLayoutRtl = isLayoutRtl();
        boolean z = false;
        if (isLayoutRtl) {
            nextLeft = ((getWidth() - this.mListPadding.right) - columnWidth) - (this.mStretchMode == 3 ? horizontalSpacing : 0);
        } else {
            nextLeft = this.mListPadding.left + (this.mStretchMode == 3 ? horizontalSpacing : 0);
        }
        if (this.mStackFromBottom) {
            int last2 = startPos + 1;
            int startPos3 = Math.max(0, (startPos - this.mNumColumns) + 1);
            int i2 = last2 - startPos3;
            i = this.mNumColumns;
            if (i2 < i) {
                nextLeft += (isLayoutRtl ? -1 : 1) * ((i - (last2 - startPos3)) * (columnWidth + horizontalSpacing));
                last = last2;
                startPos2 = startPos3;
            } else {
                last = last2;
                startPos2 = startPos3;
            }
        } else {
            startPos2 = startPos;
            last = Math.min(startPos + this.mNumColumns, this.mItemCount);
        }
        boolean hasFocus = shouldShowSelector();
        boolean inClick = touchModeDrawsInPressedState();
        int selectedPosition2 = this.mSelectedPosition;
        int nextChildDir = isLayoutRtl ? -1 : 1;
        View selectedView = null;
        int nextLeft2 = nextLeft;
        i = startPos2;
        View child = null;
        while (i < last) {
            boolean selected = i == selectedPosition2 ? true : z;
            pos = i;
            selectedPosition = selectedPosition2;
            child = makeAndAddView(i, y, flow, nextLeft2, selected, flow ? -1 : i - startPos2);
            nextLeft2 += nextChildDir * columnWidth;
            if (pos < last - 1) {
                nextLeft2 += nextChildDir * horizontalSpacing;
            }
            if (selected && (hasFocus || inClick)) {
                selectedView = child;
            }
            i = pos + 1;
            selectedPosition2 = selectedPosition;
            z = false;
        }
        pos = i;
        selectedPosition = selectedPosition2;
        this.mReferenceView = child;
        if (selectedView != null) {
            this.mReferenceViewInSelectedRow = this.mReferenceView;
        }
        return selectedView;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private View fillUp(int pos, int nextBottom) {
        View selectedView = null;
        int end = 0;
        if ((this.mGroupFlags & 34) == 34) {
            end = this.mListPadding.top;
        }
        while (nextBottom > end && pos >= 0) {
            View temp = makeRow(pos, nextBottom, false);
            if (temp != null) {
                selectedView = temp;
            }
            nextBottom = this.mReferenceView.getTop() - this.mVerticalSpacing;
            this.mFirstPosition = pos;
            pos -= this.mNumColumns;
        }
        if (this.mStackFromBottom) {
            this.mFirstPosition = Math.max(0, pos + 1);
        }
        setVisibleRangeHint(this.mFirstPosition, (this.mFirstPosition + getChildCount()) - 1);
        return selectedView;
    }

    private View fillFromTop(int nextTop) {
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mSelectedPosition);
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mItemCount - 1);
        if (this.mFirstPosition < 0) {
            this.mFirstPosition = 0;
        }
        this.mFirstPosition -= this.mFirstPosition % this.mNumColumns;
        return fillDown(this.mFirstPosition, nextTop);
    }

    private View fillFromBottom(int lastPosition, int nextBottom) {
        int invertedPosition = (this.mItemCount - 1) - Math.min(Math.max(lastPosition, this.mSelectedPosition), this.mItemCount - 1);
        return fillUp((this.mItemCount - 1) - (invertedPosition - (invertedPosition % this.mNumColumns)), nextBottom);
    }

    private View fillSelection(int childrenTop, int childrenBottom) {
        int invertedSelection;
        int selectedPosition = reconcileSelectedPosition();
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int rowEnd = -1;
        if (this.mStackFromBottom) {
            invertedSelection = (this.mItemCount - 1) - selectedPosition;
            rowEnd = (this.mItemCount - 1) - (invertedSelection - (invertedSelection % numColumns));
            invertedSelection = Math.max(0, (rowEnd - numColumns) + 1);
        } else {
            invertedSelection = selectedPosition - (selectedPosition % numColumns);
        }
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        View sel = makeRow(this.mStackFromBottom ? rowEnd : invertedSelection, getTopSelectionPixel(childrenTop, fadingEdgeLength, invertedSelection), true);
        this.mFirstPosition = invertedSelection;
        View referenceView = this.mReferenceView;
        if (this.mStackFromBottom) {
            offsetChildrenTopAndBottom(getBottomSelectionPixel(childrenBottom, fadingEdgeLength, numColumns, invertedSelection) - referenceView.getBottom());
            fillUp(invertedSelection - 1, referenceView.getTop() - verticalSpacing);
            pinToTop(childrenTop);
            fillDown(rowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            adjustViewsUpOrDown();
        } else {
            fillDown(invertedSelection + numColumns, referenceView.getBottom() + verticalSpacing);
            pinToBottom(childrenBottom);
            fillUp(invertedSelection - numColumns, referenceView.getTop() - verticalSpacing);
            adjustViewsUpOrDown();
        }
        return sel;
    }

    private void pinToTop(int childrenTop) {
        if (this.mFirstPosition == 0) {
            int offset = childrenTop - getChildAt(0).getTop();
            if (offset < 0) {
                offsetChildrenTopAndBottom(offset);
            }
        }
    }

    private void pinToBottom(int childrenBottom) {
        int count = getChildCount();
        if (this.mFirstPosition + count == this.mItemCount) {
            int offset = childrenBottom - getChildAt(count - 1).getBottom();
            if (offset > 0) {
                offsetChildrenTopAndBottom(offset);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int findMotionRow(int y) {
        int childCount = getChildCount();
        if (childCount > 0) {
            int numColumns = this.mNumColumns;
            int i;
            if (this.mStackFromBottom) {
                for (i = childCount - 1; i >= 0; i -= numColumns) {
                    if (y >= getChildAt(i).getTop()) {
                        return this.mFirstPosition + i;
                    }
                }
            } else {
                for (i = 0; i < childCount; i += numColumns) {
                    if (y <= getChildAt(i).getBottom()) {
                        return this.mFirstPosition + i;
                    }
                }
            }
        }
        return -1;
    }

    private View fillSpecific(int position, int top) {
        int invertedSelection;
        int numColumns = this.mNumColumns;
        int motionRowEnd = -1;
        if (this.mStackFromBottom) {
            invertedSelection = (this.mItemCount - 1) - position;
            motionRowEnd = (this.mItemCount - 1) - (invertedSelection - (invertedSelection % numColumns));
            invertedSelection = Math.max(0, (motionRowEnd - numColumns) + 1);
        } else {
            invertedSelection = position - (position % numColumns);
        }
        View temp = makeRow(this.mStackFromBottom ? motionRowEnd : invertedSelection, top, true);
        this.mFirstPosition = invertedSelection;
        View referenceView = this.mReferenceView;
        if (referenceView == null) {
            return null;
        }
        View below;
        View above;
        int verticalSpacing = this.mVerticalSpacing;
        int childCount;
        if (this.mStackFromBottom) {
            below = fillDown(motionRowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            adjustViewsUpOrDown();
            above = fillUp(invertedSelection - 1, referenceView.getTop() - verticalSpacing);
            childCount = getChildCount();
            if (childCount > 0) {
                correctTooLow(numColumns, verticalSpacing, childCount);
            }
        } else {
            above = fillUp(invertedSelection - numColumns, referenceView.getTop() - verticalSpacing);
            adjustViewsUpOrDown();
            below = fillDown(invertedSelection + numColumns, referenceView.getBottom() + verticalSpacing);
            childCount = getChildCount();
            if (childCount > 0) {
                correctTooHigh(numColumns, verticalSpacing, childCount);
            }
        }
        if (temp != null) {
            return temp;
        }
        if (above != null) {
            return above;
        }
        return below;
    }

    private void correctTooHigh(int numColumns, int verticalSpacing, int childCount) {
        int i = 1;
        if ((this.mFirstPosition + childCount) - 1 == this.mItemCount - 1 && childCount > 0) {
            int bottomOffset = ((this.mBottom - this.mTop) - this.mListPadding.bottom) - getChildAt(childCount - 1).getBottom();
            View firstChild = getChildAt(null);
            int firstTop = firstChild.getTop();
            if (bottomOffset <= 0) {
                return;
            }
            if (this.mFirstPosition > 0 || firstTop < this.mListPadding.top) {
                if (this.mFirstPosition == 0) {
                    bottomOffset = Math.min(bottomOffset, this.mListPadding.top - firstTop);
                }
                offsetChildrenTopAndBottom(bottomOffset);
                if (this.mFirstPosition > 0) {
                    int i2 = this.mFirstPosition;
                    if (!this.mStackFromBottom) {
                        i = numColumns;
                    }
                    fillUp(i2 - i, firstChild.getTop() - verticalSpacing);
                    adjustViewsUpOrDown();
                }
            }
        }
    }

    private void correctTooLow(int numColumns, int verticalSpacing, int childCount) {
        if (this.mFirstPosition == 0 && childCount > 0) {
            int end = (this.mBottom - this.mTop) - this.mListPadding.bottom;
            int topOffset = getChildAt(null).getTop() - this.mListPadding.top;
            View lastChild = getChildAt(childCount - 1);
            int lastBottom = lastChild.getBottom();
            int i = 1;
            int lastPosition = (this.mFirstPosition + childCount) - 1;
            if (topOffset <= 0) {
                return;
            }
            if (lastPosition < this.mItemCount - 1 || lastBottom > end) {
                if (lastPosition == this.mItemCount - 1) {
                    topOffset = Math.min(topOffset, lastBottom - end);
                }
                offsetChildrenTopAndBottom(-topOffset);
                if (lastPosition < this.mItemCount - 1) {
                    if (this.mStackFromBottom) {
                        i = numColumns;
                    }
                    fillDown(i + lastPosition, lastChild.getBottom() + verticalSpacing);
                    adjustViewsUpOrDown();
                }
            }
        }
    }

    private View fillFromSelection(int selectedTop, int childrenTop, int childrenBottom) {
        int invertedSelection;
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int rowEnd = -1;
        if (this.mStackFromBottom) {
            invertedSelection = (this.mItemCount - 1) - selectedPosition;
            rowEnd = (this.mItemCount - 1) - (invertedSelection - (invertedSelection % numColumns));
            invertedSelection = Math.max(0, (rowEnd - numColumns) + 1);
        } else {
            invertedSelection = selectedPosition - (selectedPosition % numColumns);
        }
        int topSelectionPixel = getTopSelectionPixel(childrenTop, fadingEdgeLength, invertedSelection);
        int bottomSelectionPixel = getBottomSelectionPixel(childrenBottom, fadingEdgeLength, numColumns, invertedSelection);
        View sel = makeRow(this.mStackFromBottom ? rowEnd : invertedSelection, selectedTop, true);
        this.mFirstPosition = invertedSelection;
        View referenceView = this.mReferenceView;
        adjustForTopFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
        adjustForBottomFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
        if (this.mStackFromBottom) {
            fillDown(rowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            adjustViewsUpOrDown();
            fillUp(invertedSelection - 1, referenceView.getTop() - verticalSpacing);
        } else {
            fillUp(invertedSelection - numColumns, referenceView.getTop() - verticalSpacing);
            adjustViewsUpOrDown();
            fillDown(invertedSelection + numColumns, referenceView.getBottom() + verticalSpacing);
        }
        return sel;
    }

    private int getBottomSelectionPixel(int childrenBottom, int fadingEdgeLength, int numColumns, int rowStart) {
        int bottomSelectionPixel = childrenBottom;
        if ((rowStart + numColumns) - 1 < this.mItemCount - 1) {
            return bottomSelectionPixel - fadingEdgeLength;
        }
        return bottomSelectionPixel;
    }

    private int getTopSelectionPixel(int childrenTop, int fadingEdgeLength, int rowStart) {
        int topSelectionPixel = childrenTop;
        if (rowStart > 0) {
            return topSelectionPixel + fadingEdgeLength;
        }
        return topSelectionPixel;
    }

    private void adjustForBottomFadingEdge(View childInSelectedRow, int topSelectionPixel, int bottomSelectionPixel) {
        if (childInSelectedRow.getBottom() > bottomSelectionPixel) {
            offsetChildrenTopAndBottom(-Math.min(childInSelectedRow.getTop() - topSelectionPixel, childInSelectedRow.getBottom() - bottomSelectionPixel));
        }
    }

    private void adjustForTopFadingEdge(View childInSelectedRow, int topSelectionPixel, int bottomSelectionPixel) {
        if (childInSelectedRow.getTop() < topSelectionPixel) {
            offsetChildrenTopAndBottom(Math.min(topSelectionPixel - childInSelectedRow.getTop(), bottomSelectionPixel - childInSelectedRow.getBottom()));
        }
    }

    @RemotableViewMethod
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
    }

    @RemotableViewMethod
    public void smoothScrollByOffset(int offset) {
        super.smoothScrollByOffset(offset);
    }

    private View moveSelection(int delta, int childrenTop, int childrenBottom) {
        int invertedSelection;
        int rowStart;
        int invertedSelection2;
        View referenceView;
        View sel;
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        int verticalSpacing = this.mVerticalSpacing;
        int rowEnd = -1;
        int oldBottom = 0;
        if (this.mStackFromBottom) {
            invertedSelection = (this.mItemCount - 1) - selectedPosition;
            rowEnd = (this.mItemCount - 1) - (invertedSelection - (invertedSelection % numColumns));
            rowStart = Math.max(0, (rowEnd - numColumns) + 1);
            invertedSelection2 = (this.mItemCount - 1) - (selectedPosition - delta);
            invertedSelection = Math.max(0, (((this.mItemCount - 1) - (invertedSelection2 - (invertedSelection2 % numColumns))) - numColumns) + 1);
        } else {
            invertedSelection = (selectedPosition - delta) - ((selectedPosition - delta) % numColumns);
            rowStart = selectedPosition - (selectedPosition % numColumns);
        }
        invertedSelection2 = rowStart - invertedSelection;
        int topSelectionPixel = getTopSelectionPixel(childrenTop, fadingEdgeLength, rowStart);
        int bottomSelectionPixel = getBottomSelectionPixel(childrenBottom, fadingEdgeLength, numColumns, rowStart);
        this.mFirstPosition = rowStart;
        if (invertedSelection2 > 0) {
            View view = this.mReferenceViewInSelectedRow;
            if (view != null) {
                oldBottom = view.getBottom();
            }
            fadingEdgeLength = makeRow(this.mStackFromBottom ? rowEnd : rowStart, oldBottom + verticalSpacing, true);
            referenceView = this.mReferenceView;
            adjustForBottomFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
            sel = fadingEdgeLength;
        } else {
            if (invertedSelection2 < 0) {
                fadingEdgeLength = this.mReferenceViewInSelectedRow;
                sel = makeRow(this.mStackFromBottom ? rowEnd : rowStart, (fadingEdgeLength == 0 ? 0 : fadingEdgeLength.getTop()) - verticalSpacing, false);
                referenceView = this.mReferenceView;
                adjustForTopFadingEdge(referenceView, topSelectionPixel, bottomSelectionPixel);
            } else {
                View view2 = this.mReferenceViewInSelectedRow;
                if (view2 != null) {
                    oldBottom = view2.getTop();
                }
                sel = makeRow(this.mStackFromBottom ? rowEnd : rowStart, oldBottom, true);
                referenceView = this.mReferenceView;
            }
        }
        if (this.mStackFromBottom) {
            fillDown(rowEnd + numColumns, referenceView.getBottom() + verticalSpacing);
            adjustViewsUpOrDown();
            fillUp(rowStart - 1, referenceView.getTop() - verticalSpacing);
        } else {
            fillUp(rowStart - numColumns, referenceView.getTop() - verticalSpacing);
            adjustViewsUpOrDown();
            fillDown(rowStart + numColumns, referenceView.getBottom() + verticalSpacing);
        }
        return sel;
    }

    @UnsupportedAppUsage
    private boolean determineColumns(int availableSpace) {
        int requestedHorizontalSpacing = this.mRequestedHorizontalSpacing;
        int stretchMode = this.mStretchMode;
        int requestedColumnWidth = this.mRequestedColumnWidth;
        boolean didNotInitiallyFit = false;
        int i = this.mRequestedNumColumns;
        if (i != -1) {
            this.mNumColumns = i;
        } else if (requestedColumnWidth > 0) {
            this.mNumColumns = (availableSpace + requestedHorizontalSpacing) / (requestedColumnWidth + requestedHorizontalSpacing);
        } else {
            this.mNumColumns = 2;
        }
        if (this.mNumColumns <= 0) {
            this.mNumColumns = 1;
        }
        if (stretchMode != 0) {
            i = this.mNumColumns;
            int spaceLeftOver = (availableSpace - (i * requestedColumnWidth)) - ((i - 1) * requestedHorizontalSpacing);
            if (spaceLeftOver < 0) {
                didNotInitiallyFit = true;
            }
            if (stretchMode == 1) {
                this.mColumnWidth = requestedColumnWidth;
                i = this.mNumColumns;
                if (i > 1) {
                    this.mHorizontalSpacing = (spaceLeftOver / (i - 1)) + requestedHorizontalSpacing;
                } else {
                    this.mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver;
                }
            } else if (stretchMode == 2) {
                this.mColumnWidth = (spaceLeftOver / this.mNumColumns) + requestedColumnWidth;
                this.mHorizontalSpacing = requestedHorizontalSpacing;
            } else if (stretchMode == 3) {
                this.mColumnWidth = requestedColumnWidth;
                i = this.mNumColumns;
                if (i > 1) {
                    this.mHorizontalSpacing = (spaceLeftOver / (i + 1)) + requestedHorizontalSpacing;
                } else {
                    this.mHorizontalSpacing = requestedHorizontalSpacing + spaceLeftOver;
                }
            }
        } else {
            this.mColumnWidth = requestedColumnWidth;
            this.mHorizontalSpacing = requestedHorizontalSpacing;
        }
        return didNotInitiallyFit;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == 0) {
            int i = this.mColumnWidth;
            if (i > 0) {
                i = (i + this.mListPadding.left) + this.mListPadding.right;
            } else {
                i = this.mListPadding.left + this.mListPadding.right;
            }
            widthSize = getVerticalScrollbarWidth() + i;
        }
        boolean didNotInitiallyFit = determineColumns((widthSize - this.mListPadding.left) - this.mListPadding.right);
        int childHeight = 0;
        this.mItemCount = this.mAdapter == null ? 0 : this.mAdapter.getCount();
        int count = this.mItemCount;
        if (count > 0) {
            View child = obtainView(0, this.mIsScrap);
            LayoutParams p = (LayoutParams) child.getLayoutParams();
            if (p == null) {
                p = (LayoutParams) generateDefaultLayoutParams();
                child.setLayoutParams(p);
            }
            p.viewType = this.mAdapter.getItemViewType(0);
            p.isEnabled = this.mAdapter.isEnabled(0);
            p.forceAdd = true;
            child.measure(ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(this.mColumnWidth, 1073741824), 0, p.width), ViewGroup.getChildMeasureSpec(MeasureSpec.makeSafeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), 0), 0, p.height));
            childHeight = child.getMeasuredHeight();
            int childState = View.combineMeasuredStates(0, child.getMeasuredState());
            if (this.mRecycler.shouldRecycleViewType(p.viewType)) {
                this.mRecycler.addScrapView(child, -1);
            }
        }
        if (heightMode == 0) {
            heightSize = ((this.mListPadding.top + this.mListPadding.bottom) + childHeight) + (getVerticalFadingEdgeLength() * 2);
        }
        if (heightMode == Integer.MIN_VALUE) {
            int ourSize = this.mListPadding.top + this.mListPadding.bottom;
            int numColumns = this.mNumColumns;
            for (int i2 = 0; i2 < count; i2 += numColumns) {
                ourSize += childHeight;
                if (i2 + numColumns < count) {
                    ourSize += this.mVerticalSpacing;
                }
                if (ourSize >= heightSize) {
                    ourSize = heightSize;
                    break;
                }
            }
            heightSize = ourSize;
        }
        if (widthMode == Integer.MIN_VALUE) {
            int i3 = this.mRequestedNumColumns;
            if (i3 != -1 && ((((this.mColumnWidth * i3) + ((i3 - 1) * this.mHorizontalSpacing)) + this.mListPadding.left) + this.mListPadding.right > widthSize || didNotInitiallyFit)) {
                widthSize |= 16777216;
            }
        }
        setMeasuredDimension(widthSize, heightSize);
        this.mWidthMeasureSpec = widthMeasureSpec;
    }

    /* Access modifiers changed, original: protected */
    public void attachLayoutAnimationParameters(View child, ViewGroup.LayoutParams params, int index, int count) {
        AnimationParameters animationParams = params.layoutAnimationParameters;
        if (animationParams == null) {
            animationParams = new AnimationParameters();
            params.layoutAnimationParameters = animationParams;
        }
        animationParams.count = count;
        animationParams.index = index;
        int i = this.mNumColumns;
        animationParams.columnsCount = i;
        animationParams.rowsCount = count / i;
        if (this.mStackFromBottom) {
            i = (count - 1) - index;
            int i2 = this.mNumColumns;
            animationParams.column = (i2 - 1) - (i % i2);
            animationParams.row = (animationParams.rowsCount - 1) - (i / this.mNumColumns);
            return;
        }
        i = this.mNumColumns;
        animationParams.column = index % i;
        animationParams.row = index / i;
    }

    /* Access modifiers changed, original: protected */
    public void layoutChildren() {
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).layoutChildren(this);
        } else {
            originalLayoutChildren();
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x02a4  */
    /* JADX WARNING: Removed duplicated region for block: B:185:0x02a4  */
    public void originalLayoutChildren() {
        /*
        r25 = this;
        r1 = r25;
        r2 = r1.mBlockLayoutRequests;
        r0 = 1;
        if (r2 != 0) goto L_0x0009;
    L_0x0007:
        r1.mBlockLayoutRequests = r0;
    L_0x0009:
        r3 = 0;
        super.layoutChildren();	 Catch:{ all -> 0x029f }
        r25.invalidate();	 Catch:{ all -> 0x029f }
        r4 = r1.mAdapter;	 Catch:{ all -> 0x029f }
        if (r4 != 0) goto L_0x0024;
    L_0x0014:
        r25.resetList();	 Catch:{ all -> 0x001f }
        r25.invokeOnItemScrollListener();	 Catch:{ all -> 0x001f }
        if (r2 != 0) goto L_0x001e;
    L_0x001c:
        r1.mBlockLayoutRequests = r3;
    L_0x001e:
        return;
    L_0x001f:
        r0 = move-exception;
        r20 = r2;
        goto L_0x02a2;
    L_0x0024:
        r4 = r1.mListPadding;	 Catch:{ all -> 0x029f }
        r4 = r4.top;	 Catch:{ all -> 0x029f }
        r5 = r1.mBottom;	 Catch:{ all -> 0x029f }
        r6 = r1.mTop;	 Catch:{ all -> 0x029f }
        r5 = r5 - r6;
        r6 = r1.mListPadding;	 Catch:{ all -> 0x029f }
        r6 = r6.bottom;	 Catch:{ all -> 0x029f }
        r5 = r5 - r6;
        r6 = r25.getChildCount();	 Catch:{ all -> 0x029f }
        r7 = 0;
        r8 = 0;
        r9 = 0;
        r10 = 0;
        r11 = r1.mLayoutMode;	 Catch:{ all -> 0x029f }
        switch(r11) {
            case 1: goto L_0x005c;
            case 2: goto L_0x004d;
            case 3: goto L_0x005c;
            case 4: goto L_0x005c;
            case 5: goto L_0x005c;
            case 6: goto L_0x0042;
            default: goto L_0x003f;
        };	 Catch:{ all -> 0x029f }
    L_0x003f:
        r11 = r1.mSelectedPosition;	 Catch:{ all -> 0x029f }
        goto L_0x005d;
    L_0x0042:
        r11 = r1.mNextSelectedPosition;	 Catch:{ all -> 0x001f }
        if (r11 < 0) goto L_0x006e;
    L_0x0046:
        r11 = r1.mNextSelectedPosition;	 Catch:{ all -> 0x001f }
        r12 = r1.mSelectedPosition;	 Catch:{ all -> 0x001f }
        r7 = r11 - r12;
        goto L_0x006e;
    L_0x004d:
        r11 = r1.mNextSelectedPosition;	 Catch:{ all -> 0x001f }
        r12 = r1.mFirstPosition;	 Catch:{ all -> 0x001f }
        r11 = r11 - r12;
        if (r11 < 0) goto L_0x006e;
    L_0x0054:
        if (r11 >= r6) goto L_0x006e;
    L_0x0056:
        r12 = r1.getChildAt(r11);	 Catch:{ all -> 0x001f }
        r10 = r12;
        goto L_0x006e;
    L_0x005c:
        goto L_0x006e;
    L_0x005d:
        r12 = r1.mFirstPosition;	 Catch:{ all -> 0x029f }
        r11 = r11 - r12;
        if (r11 < 0) goto L_0x0069;
    L_0x0062:
        if (r11 >= r6) goto L_0x0069;
    L_0x0064:
        r12 = r1.getChildAt(r11);	 Catch:{ all -> 0x001f }
        r8 = r12;
    L_0x0069:
        r12 = r1.getChildAt(r3);	 Catch:{ all -> 0x029f }
        r9 = r12;
    L_0x006e:
        r11 = r1.mDataChanged;	 Catch:{ all -> 0x029f }
        if (r11 == 0) goto L_0x0075;
    L_0x0072:
        r25.handleDataChanged();	 Catch:{ all -> 0x001f }
    L_0x0075:
        r12 = r1.mItemCount;	 Catch:{ all -> 0x029f }
        if (r12 != 0) goto L_0x0084;
    L_0x0079:
        r25.resetList();	 Catch:{ all -> 0x001f }
        r25.invokeOnItemScrollListener();	 Catch:{ all -> 0x001f }
        if (r2 != 0) goto L_0x0083;
    L_0x0081:
        r1.mBlockLayoutRequests = r3;
    L_0x0083:
        return;
    L_0x0084:
        r12 = r1.mNextSelectedPosition;	 Catch:{ all -> 0x029f }
        r1.setSelectedPositionInt(r12);	 Catch:{ all -> 0x029f }
        r12 = 0;
        r13 = 0;
        r14 = -1;
        r15 = r25.getViewRootImpl();	 Catch:{ all -> 0x029f }
        if (r15 == 0) goto L_0x00cf;
    L_0x0092:
        r16 = r15.getAccessibilityFocusedHost();	 Catch:{ all -> 0x001f }
        r17 = r16;
        r3 = r17;
        if (r3 == 0) goto L_0x00cc;
    L_0x009c:
        r17 = r1.getAccessibilityFocusedChild(r3);	 Catch:{ all -> 0x001f }
        r18 = r17;
        r0 = r18;
        if (r0 == 0) goto L_0x00c9;
    L_0x00a6:
        if (r11 == 0) goto L_0x00b8;
    L_0x00a8:
        r18 = r0.hasTransientState();	 Catch:{ all -> 0x001f }
        if (r18 != 0) goto L_0x00b8;
    L_0x00ae:
        r18 = r12;
        r12 = r1.mAdapterHasStableIds;	 Catch:{ all -> 0x001f }
        if (r12 == 0) goto L_0x00b5;
    L_0x00b4:
        goto L_0x00ba;
    L_0x00b5:
        r12 = r18;
        goto L_0x00c0;
    L_0x00b8:
        r18 = r12;
    L_0x00ba:
        r13 = r3;
        r12 = r15.getAccessibilityFocusedVirtualView();	 Catch:{ all -> 0x001f }
    L_0x00c0:
        r18 = r1.getPositionForView(r0);	 Catch:{ all -> 0x001f }
        r14 = r18;
        r18 = r12;
        goto L_0x00d1;
    L_0x00c9:
        r18 = r12;
        goto L_0x00d1;
    L_0x00cc:
        r18 = r12;
        goto L_0x00d1;
    L_0x00cf:
        r18 = r12;
    L_0x00d1:
        r0 = r1.mFirstPosition;	 Catch:{ all -> 0x029f }
        r3 = r1.mRecycler;	 Catch:{ all -> 0x029f }
        if (r11 == 0) goto L_0x00f3;
    L_0x00d7:
        r12 = 0;
    L_0x00d8:
        if (r12 >= r6) goto L_0x00ee;
    L_0x00da:
        r19 = r11;
        r11 = r1.getChildAt(r12);	 Catch:{ all -> 0x029f }
        r20 = r2;
        r2 = r0 + r12;
        r3.addScrapView(r11, r2);	 Catch:{ all -> 0x029d }
        r12 = r12 + 1;
        r11 = r19;
        r2 = r20;
        goto L_0x00d8;
    L_0x00ee:
        r20 = r2;
        r19 = r11;
        goto L_0x00fa;
    L_0x00f3:
        r20 = r2;
        r19 = r11;
        r3.fillActiveViews(r6, r0);	 Catch:{ all -> 0x029d }
    L_0x00fa:
        r25.detachAllViewsFromParent();	 Catch:{ all -> 0x029d }
        r3.removeSkippedScrap();	 Catch:{ all -> 0x029d }
        r2 = r1.mLayoutMode;	 Catch:{ all -> 0x029d }
        r11 = -1;
        switch(r2) {
            case 1: goto L_0x0144;
            case 2: goto L_0x0132;
            case 3: goto L_0x0125;
            case 4: goto L_0x011b;
            case 5: goto L_0x0111;
            case 6: goto L_0x010b;
            default: goto L_0x0106;
        };	 Catch:{ all -> 0x029d }
    L_0x0106:
        if (r6 != 0) goto L_0x0183;
    L_0x0108:
        r2 = r1.mStackFromBottom;	 Catch:{ all -> 0x029d }
        goto L_0x0150;
    L_0x010b:
        r2 = r1.moveSelection(r7, r4, r5);	 Catch:{ all -> 0x029d }
        goto L_0x01b7;
    L_0x0111:
        r2 = r1.mSyncPosition;	 Catch:{ all -> 0x029d }
        r12 = r1.mSpecificTop;	 Catch:{ all -> 0x029d }
        r2 = r1.fillSpecific(r2, r12);	 Catch:{ all -> 0x029d }
        goto L_0x01b7;
    L_0x011b:
        r2 = r1.mSelectedPosition;	 Catch:{ all -> 0x029d }
        r12 = r1.mSpecificTop;	 Catch:{ all -> 0x029d }
        r2 = r1.fillSpecific(r2, r12);	 Catch:{ all -> 0x029d }
        goto L_0x01b7;
    L_0x0125:
        r2 = r1.mItemCount;	 Catch:{ all -> 0x029d }
        r12 = 1;
        r2 = r2 - r12;
        r2 = r1.fillUp(r2, r5);	 Catch:{ all -> 0x029d }
        r25.adjustViewsUpOrDown();	 Catch:{ all -> 0x029d }
        goto L_0x01b7;
    L_0x0132:
        if (r10 == 0) goto L_0x013e;
    L_0x0134:
        r2 = r10.getTop();	 Catch:{ all -> 0x029d }
        r2 = r1.fillFromSelection(r2, r4, r5);	 Catch:{ all -> 0x029d }
        goto L_0x01b7;
    L_0x013e:
        r2 = r1.fillSelection(r4, r5);	 Catch:{ all -> 0x029d }
        goto L_0x01b7;
    L_0x0144:
        r2 = 0;
        r1.mFirstPosition = r2;	 Catch:{ all -> 0x029d }
        r2 = r1.fillFromTop(r4);	 Catch:{ all -> 0x029d }
        r25.adjustViewsUpOrDown();	 Catch:{ all -> 0x029d }
        goto L_0x01b7;
    L_0x0150:
        if (r2 != 0) goto L_0x0168;
    L_0x0152:
        r2 = r1.mAdapter;	 Catch:{ all -> 0x029d }
        if (r2 == 0) goto L_0x015f;
    L_0x0156:
        r2 = r25.isInTouchMode();	 Catch:{ all -> 0x029d }
        if (r2 == 0) goto L_0x015d;
    L_0x015c:
        goto L_0x015f;
    L_0x015d:
        r2 = 0;
        goto L_0x0160;
    L_0x015f:
        r2 = r11;
    L_0x0160:
        r1.setSelectedPositionInt(r2);	 Catch:{ all -> 0x029d }
        r2 = r1.fillFromTop(r4);	 Catch:{ all -> 0x029d }
        goto L_0x01b7;
    L_0x0168:
        r2 = r1.mItemCount;	 Catch:{ all -> 0x029d }
        r12 = 1;
        r2 = r2 - r12;
        r12 = r1.mAdapter;	 Catch:{ all -> 0x029d }
        if (r12 == 0) goto L_0x0179;
    L_0x0170:
        r12 = r25.isInTouchMode();	 Catch:{ all -> 0x029d }
        if (r12 == 0) goto L_0x0177;
    L_0x0176:
        goto L_0x0179;
    L_0x0177:
        r12 = r2;
        goto L_0x017a;
    L_0x0179:
        r12 = r11;
    L_0x017a:
        r1.setSelectedPositionInt(r12);	 Catch:{ all -> 0x029d }
        r12 = r1.fillFromBottom(r2, r5);	 Catch:{ all -> 0x029d }
        r2 = r12;
        goto L_0x01b7;
    L_0x0183:
        r2 = r1.mSelectedPosition;	 Catch:{ all -> 0x029d }
        if (r2 < 0) goto L_0x019c;
    L_0x0187:
        r2 = r1.mSelectedPosition;	 Catch:{ all -> 0x029d }
        r12 = r1.mItemCount;	 Catch:{ all -> 0x029d }
        if (r2 >= r12) goto L_0x019c;
    L_0x018d:
        r2 = r1.mSelectedPosition;	 Catch:{ all -> 0x029d }
        if (r8 != 0) goto L_0x0193;
    L_0x0191:
        r12 = r4;
        goto L_0x0197;
    L_0x0193:
        r12 = r8.getTop();	 Catch:{ all -> 0x029d }
    L_0x0197:
        r2 = r1.fillSpecific(r2, r12);	 Catch:{ all -> 0x029d }
        goto L_0x01b7;
    L_0x019c:
        r2 = r1.mFirstPosition;	 Catch:{ all -> 0x029d }
        r12 = r1.mItemCount;	 Catch:{ all -> 0x029d }
        if (r2 >= r12) goto L_0x01b1;
    L_0x01a2:
        r2 = r1.mFirstPosition;	 Catch:{ all -> 0x029d }
        if (r9 != 0) goto L_0x01a8;
    L_0x01a6:
        r12 = r4;
        goto L_0x01ac;
    L_0x01a8:
        r12 = r9.getTop();	 Catch:{ all -> 0x029d }
    L_0x01ac:
        r2 = r1.fillSpecific(r2, r12);	 Catch:{ all -> 0x029d }
        goto L_0x01b7;
    L_0x01b1:
        r2 = 0;
        r12 = r1.fillSpecific(r2, r4);	 Catch:{ all -> 0x029d }
        r2 = r12;
    L_0x01b7:
        r3.scrapActiveViews();	 Catch:{ all -> 0x029d }
        if (r2 == 0) goto L_0x01c8;
    L_0x01bc:
        r1.positionSelector(r11, r2);	 Catch:{ all -> 0x029d }
        r12 = r2.getTop();	 Catch:{ all -> 0x029d }
        r1.mSelectedTop = r12;	 Catch:{ all -> 0x029d }
        r22 = r0;
        goto L_0x0209;
    L_0x01c8:
        r12 = r1.mTouchMode;	 Catch:{ all -> 0x029d }
        if (r12 <= 0) goto L_0x01d3;
    L_0x01cc:
        r12 = r1.mTouchMode;	 Catch:{ all -> 0x029d }
        r11 = 3;
        if (r12 >= r11) goto L_0x01d3;
    L_0x01d1:
        r11 = 1;
        goto L_0x01d4;
    L_0x01d3:
        r11 = 0;
    L_0x01d4:
        if (r11 == 0) goto L_0x01e9;
    L_0x01d6:
        r12 = r1.mMotionPosition;	 Catch:{ all -> 0x029d }
        r22 = r0;
        r0 = r1.mFirstPosition;	 Catch:{ all -> 0x029d }
        r12 = r12 - r0;
        r0 = r1.getChildAt(r12);	 Catch:{ all -> 0x029d }
        if (r0 == 0) goto L_0x01e8;
    L_0x01e3:
        r12 = r1.mMotionPosition;	 Catch:{ all -> 0x029d }
        r1.positionSelector(r12, r0);	 Catch:{ all -> 0x029d }
    L_0x01e8:
        goto L_0x0209;
    L_0x01e9:
        r22 = r0;
        r0 = r1.mSelectedPosition;	 Catch:{ all -> 0x029d }
        r12 = -1;
        if (r0 == r12) goto L_0x0201;
    L_0x01f0:
        r0 = r1.mSelectorPosition;	 Catch:{ all -> 0x029d }
        r12 = r1.mFirstPosition;	 Catch:{ all -> 0x029d }
        r0 = r0 - r12;
        r0 = r1.getChildAt(r0);	 Catch:{ all -> 0x029d }
        if (r0 == 0) goto L_0x0200;
    L_0x01fb:
        r12 = r1.mSelectorPosition;	 Catch:{ all -> 0x029d }
        r1.positionSelector(r12, r0);	 Catch:{ all -> 0x029d }
    L_0x0200:
        goto L_0x0209;
    L_0x0201:
        r12 = 0;
        r1.mSelectedTop = r12;	 Catch:{ all -> 0x029d }
        r0 = r1.mSelectorRect;	 Catch:{ all -> 0x029d }
        r0.setEmpty();	 Catch:{ all -> 0x029d }
    L_0x0209:
        r0 = 0;
        if (r15 == 0) goto L_0x026e;
    L_0x020c:
        r11 = r15.getAccessibilityFocusedHost();	 Catch:{ all -> 0x029d }
        if (r11 != 0) goto L_0x0269;
    L_0x0212:
        if (r13 == 0) goto L_0x0248;
    L_0x0214:
        r12 = r13.isAttachedToWindow();	 Catch:{ all -> 0x029d }
        if (r12 == 0) goto L_0x0243;
        r12 = r13.getAccessibilityNodeProvider();	 Catch:{ all -> 0x029d }
        if (r18 == 0) goto L_0x023b;
    L_0x0221:
        if (r12 == 0) goto L_0x023b;
        r23 = r18.getSourceNodeId();	 Catch:{ all -> 0x029d }
        r17 = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(r23);	 Catch:{ all -> 0x029d }
        r21 = r17;
        r23 = r2;
        r2 = 64;
        r24 = r3;
        r3 = r21;
        r12.performAction(r3, r2, r0);	 Catch:{ all -> 0x029d }
        goto L_0x0268;
    L_0x023b:
        r23 = r2;
        r24 = r3;
        r13.requestAccessibilityFocus();	 Catch:{ all -> 0x029d }
        goto L_0x0268;
    L_0x0243:
        r23 = r2;
        r24 = r3;
        goto L_0x024c;
    L_0x0248:
        r23 = r2;
        r24 = r3;
    L_0x024c:
        r2 = -1;
        if (r14 == r2) goto L_0x0268;
    L_0x024f:
        r2 = r1.mFirstPosition;	 Catch:{ all -> 0x029d }
        r2 = r14 - r2;
        r3 = r25.getChildCount();	 Catch:{ all -> 0x029d }
        r12 = 1;
        r3 = r3 - r12;
        r12 = 0;
        r2 = android.util.MathUtils.constrain(r2, r12, r3);	 Catch:{ all -> 0x029d }
        r3 = r1.getChildAt(r2);	 Catch:{ all -> 0x029d }
        if (r3 == 0) goto L_0x0272;
    L_0x0264:
        r3.requestAccessibilityFocus();	 Catch:{ all -> 0x029d }
        goto L_0x0272;
    L_0x0268:
        goto L_0x0272;
    L_0x0269:
        r23 = r2;
        r24 = r3;
        goto L_0x0272;
    L_0x026e:
        r23 = r2;
        r24 = r3;
    L_0x0272:
        r2 = 0;
        r1.mLayoutMode = r2;	 Catch:{ all -> 0x029d }
        r1.mDataChanged = r2;	 Catch:{ all -> 0x029d }
        r2 = r1.mPositionScrollAfterLayout;	 Catch:{ all -> 0x029d }
        if (r2 == 0) goto L_0x0282;
    L_0x027b:
        r2 = r1.mPositionScrollAfterLayout;	 Catch:{ all -> 0x029d }
        r1.post(r2);	 Catch:{ all -> 0x029d }
        r1.mPositionScrollAfterLayout = r0;	 Catch:{ all -> 0x029d }
    L_0x0282:
        r2 = 0;
        r1.mNeedSync = r2;	 Catch:{ all -> 0x029d }
        r0 = r1.mSelectedPosition;	 Catch:{ all -> 0x029d }
        r1.setNextSelectedPositionInt(r0);	 Catch:{ all -> 0x029d }
        r25.updateScrollIndicators();	 Catch:{ all -> 0x029d }
        r0 = r1.mItemCount;	 Catch:{ all -> 0x029d }
        if (r0 <= 0) goto L_0x0294;
    L_0x0291:
        r25.checkSelectionChanged();	 Catch:{ all -> 0x029d }
    L_0x0294:
        r25.invokeOnItemScrollListener();	 Catch:{ all -> 0x029d }
        if (r20 != 0) goto L_0x029c;
    L_0x0299:
        r2 = 0;
        r1.mBlockLayoutRequests = r2;
    L_0x029c:
        return;
    L_0x029d:
        r0 = move-exception;
        goto L_0x02a2;
    L_0x029f:
        r0 = move-exception;
        r20 = r2;
    L_0x02a2:
        if (r20 != 0) goto L_0x02a7;
    L_0x02a4:
        r2 = 0;
        r1.mBlockLayoutRequests = r2;
    L_0x02a7:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.GridView.originalLayoutChildren():void");
    }

    private View makeAndAddView(int position, int y, boolean flow, int childrenLeft, boolean selected, int where) {
        View activeView;
        int i = position;
        if (!this.mDataChanged) {
            activeView = this.mRecycler.getActiveView(position);
            if (activeView != null) {
                setupChild(activeView, position, y, flow, childrenLeft, selected, true, where);
                return activeView;
            }
        }
        activeView = obtainView(position, this.mIsScrap);
        setupChild(activeView, position, y, flow, childrenLeft, selected, this.mIsScrap[0], where);
        return activeView;
    }

    private void setupChild(View child, int position, int y, boolean flowDown, int childrenLeft, boolean selected, boolean isAttachedToWindow, int where) {
        View view = child;
        int i = position;
        int i2 = where;
        Trace.traceBegin(8, "setupGridItem");
        boolean isSelected = selected && shouldShowSelector();
        boolean updateChildSelected = isSelected != child.isSelected();
        int mode = this.mTouchMode;
        boolean isPressed = mode > 0 && mode < 3 && this.mMotionPosition == i;
        boolean updateChildPressed = isPressed != child.isPressed();
        boolean needToMeasure = !isAttachedToWindow || updateChildSelected || child.isLayoutRequested();
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (LayoutParams) generateDefaultLayoutParams();
        }
        p.viewType = this.mAdapter.getItemViewType(i);
        p.isEnabled = this.mAdapter.isEnabled(i);
        if (updateChildSelected) {
            view.setSelected(isSelected);
            if (isSelected) {
                requestFocus();
            }
        }
        if (updateChildPressed) {
            view.setPressed(isPressed);
        }
        if (!(this.mChoiceMode == 0 || this.mCheckStates == null)) {
            if (view instanceof Checkable) {
                ((Checkable) view).setChecked(this.mCheckStates.get(i));
            } else if (getContext().getApplicationInfo().targetSdkVersion >= 11) {
                view.setActivated(this.mCheckStates.get(i));
            }
        }
        if (!isAttachedToWindow || p.forceAdd) {
            p.forceAdd = false;
            addViewInLayout(view, i2, p, true);
        } else {
            attachViewToParent(view, i2, p);
            if (!(isAttachedToWindow && ((LayoutParams) child.getLayoutParams()).scrappedFromPosition == i)) {
                child.jumpDrawablesToCurrentState();
            }
        }
        if (needToMeasure) {
            view.measure(ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(this.mColumnWidth, 1073741824), 0, p.width), ViewGroup.getChildMeasureSpec(MeasureSpec.makeMeasureSpec(0, 0), 0, p.height));
        } else {
            cleanupLayoutState(child);
        }
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childTop = flowDown ? y : y - h;
        i = Gravity.getAbsoluteGravity(this.mGravity, getLayoutDirection()) & 7;
        if (i == 1) {
            i = childrenLeft + ((this.mColumnWidth - w) / 2);
        } else if (i == 3) {
            i = childrenLeft;
        } else if (i != 5) {
            i = childrenLeft;
        } else {
            i = (childrenLeft + this.mColumnWidth) - w;
        }
        if (needToMeasure) {
            view.layout(i, childTop, i + w, childTop + h);
        } else {
            view.offsetLeftAndRight(i - child.getLeft());
            view.offsetTopAndBottom(childTop - child.getTop());
        }
        if (this.mCachingStarted && !child.isDrawingCacheEnabled()) {
            view.setDrawingCacheEnabled(true);
        }
        Trace.traceEnd(8);
    }

    public void setSelection(int position) {
        if (isInTouchMode()) {
            this.mResurrectToPosition = position;
        } else {
            setNextSelectedPositionInt(position);
        }
        this.mLayoutMode = 2;
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        requestLayout();
    }

    /* Access modifiers changed, original: 0000 */
    public void setSelectionInt(int position) {
        int next;
        int previousSelectedPosition = this.mNextSelectedPosition;
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        setNextSelectedPositionInt(position);
        layoutChildren();
        if (this.mStackFromBottom) {
            next = (this.mItemCount - 1) - this.mNextSelectedPosition;
        } else {
            next = this.mNextSelectedPosition;
        }
        int previous = this.mStackFromBottom ? (this.mItemCount - 1) - previousSelectedPosition : previousSelectedPosition;
        int previousRow = this.mNumColumns;
        if (next / previousRow != previous / previousRow) {
            awakenScrollBars();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return commonKey(keyCode, 1, event);
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return commonKey(keyCode, repeatCount, event);
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return commonKey(keyCode, 1, event);
    }

    private boolean commonKey(int keyCode, int count, KeyEvent event) {
        if (this.mAdapter == null) {
            return false;
        }
        if (this.mDataChanged) {
            layoutChildren();
        }
        boolean handled = false;
        int action = event.getAction();
        if (KeyEvent.isConfirmKey(keyCode) && event.hasNoModifiers() && action != 1) {
            handled = resurrectSelectionIfNeeded();
            if (!handled && event.getRepeatCount() == 0 && getChildCount() > 0) {
                keyPressed();
                handled = true;
            }
        }
        if (!(handled || action == 1)) {
            boolean z;
            if (keyCode != 61) {
                if (keyCode != 92) {
                    if (keyCode != 93) {
                        if (keyCode != 122) {
                            if (keyCode != 123) {
                                switch (keyCode) {
                                    case 19:
                                        if (!event.hasNoModifiers()) {
                                            if (event.hasModifiers(2)) {
                                                z = resurrectSelectionIfNeeded() || fullScroll(33);
                                                handled = z;
                                                break;
                                            }
                                        }
                                        z = resurrectSelectionIfNeeded() || arrowScroll(33);
                                        handled = z;
                                        break;
                                        break;
                                    case 20:
                                        if (!event.hasNoModifiers()) {
                                            if (event.hasModifiers(2)) {
                                                z = resurrectSelectionIfNeeded() || fullScroll(130);
                                                handled = z;
                                                break;
                                            }
                                        }
                                        z = resurrectSelectionIfNeeded() || arrowScroll(130);
                                        handled = z;
                                        break;
                                        break;
                                    case 21:
                                        if (event.hasNoModifiers()) {
                                            z = resurrectSelectionIfNeeded() || arrowScroll(17);
                                            handled = z;
                                            break;
                                        }
                                        break;
                                    case 22:
                                        if (event.hasNoModifiers()) {
                                            z = resurrectSelectionIfNeeded() || arrowScroll(66);
                                            handled = z;
                                            break;
                                        }
                                        break;
                                }
                            } else if (event.hasNoModifiers()) {
                                z = resurrectSelectionIfNeeded() || fullScroll(130);
                                handled = z;
                            }
                        } else if (event.hasNoModifiers()) {
                            z = resurrectSelectionIfNeeded() || fullScroll(33);
                            handled = z;
                        }
                    } else if (event.hasNoModifiers()) {
                        z = resurrectSelectionIfNeeded() || pageScroll(130);
                        handled = z;
                    } else if (event.hasModifiers(2)) {
                        z = resurrectSelectionIfNeeded() || fullScroll(130);
                        handled = z;
                    }
                } else if (event.hasNoModifiers()) {
                    z = resurrectSelectionIfNeeded() || pageScroll(33);
                    handled = z;
                } else if (event.hasModifiers(2)) {
                    z = resurrectSelectionIfNeeded() || fullScroll(33);
                    handled = z;
                }
            } else if (event.hasNoModifiers()) {
                z = resurrectSelectionIfNeeded() || sequenceScroll(2);
                handled = z;
            } else if (event.hasModifiers(1)) {
                z = resurrectSelectionIfNeeded() || sequenceScroll(1);
                handled = z;
            }
        }
        if (handled || sendToTextFilter(keyCode, count, event)) {
            return true;
        }
        if (action == 0) {
            return super.onKeyDown(keyCode, event);
        }
        if (action == 1) {
            return super.onKeyUp(keyCode, event);
        }
        if (action != 2) {
            return false;
        }
        return super.onKeyMultiple(keyCode, count, event);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean pageScroll(int direction) {
        int nextPage = -1;
        if (direction == 33) {
            nextPage = Math.max(0, this.mSelectedPosition - getChildCount());
        } else if (direction == 130) {
            nextPage = Math.min(this.mItemCount - 1, this.mSelectedPosition + getChildCount());
        }
        if (nextPage < 0) {
            return false;
        }
        setSelectionInt(nextPage);
        invokeOnItemScrollListener();
        awakenScrollBars();
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean fullScroll(int direction) {
        boolean moved = false;
        if (direction == 33) {
            this.mLayoutMode = 2;
            setSelectionInt(0);
            invokeOnItemScrollListener();
            moved = true;
        } else if (direction == 130) {
            this.mLayoutMode = 2;
            setSelectionInt(this.mItemCount - 1);
            invokeOnItemScrollListener();
            moved = true;
        }
        if (moved) {
            awakenScrollBars();
        }
        return moved;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean arrowScroll(int direction) {
        int endOfRowPos;
        int startOfRowPos;
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        boolean moved = false;
        if (this.mStackFromBottom) {
            endOfRowPos = (this.mItemCount - 1) - ((((this.mItemCount - 1) - selectedPosition) / numColumns) * numColumns);
            startOfRowPos = Math.max(0, (endOfRowPos - numColumns) + 1);
        } else {
            startOfRowPos = (selectedPosition / numColumns) * numColumns;
            endOfRowPos = Math.min((startOfRowPos + numColumns) - 1, this.mItemCount - 1);
        }
        if (direction != 33) {
            if (direction == 130 && endOfRowPos < this.mItemCount - 1) {
                this.mLayoutMode = 6;
                setSelectionInt(Math.min(selectedPosition + numColumns, this.mItemCount - 1));
                moved = true;
            }
        } else if (startOfRowPos > 0) {
            this.mLayoutMode = 6;
            setSelectionInt(Math.max(0, selectedPosition - numColumns));
            moved = true;
        }
        boolean isLayoutRtl = isLayoutRtl();
        if (selectedPosition > startOfRowPos && ((direction == 17 && !isLayoutRtl) || (direction == 66 && isLayoutRtl))) {
            this.mLayoutMode = 6;
            setSelectionInt(Math.max(0, selectedPosition - 1));
            moved = true;
        } else if (selectedPosition < endOfRowPos && ((direction == 17 && isLayoutRtl) || (direction == 66 && !isLayoutRtl))) {
            this.mLayoutMode = 6;
            setSelectionInt(Math.min(selectedPosition + 1, this.mItemCount - 1));
            moved = true;
        }
        if (moved) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            invokeOnItemScrollListener();
        }
        if (moved) {
            awakenScrollBars();
        }
        return moved;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean sequenceScroll(int direction) {
        int endOfRow;
        int startOfRow;
        int selectedPosition = this.mSelectedPosition;
        int numColumns = this.mNumColumns;
        int count = this.mItemCount;
        boolean z = false;
        if (this.mStackFromBottom) {
            endOfRow = (count - 1) - ((((count - 1) - selectedPosition) / numColumns) * numColumns);
            startOfRow = Math.max(0, (endOfRow - numColumns) + 1);
        } else {
            startOfRow = (selectedPosition / numColumns) * numColumns;
            endOfRow = Math.min((startOfRow + numColumns) - 1, count - 1);
        }
        boolean moved = false;
        boolean showScroll = false;
        if (direction != 1) {
            if (direction == 2 && selectedPosition < count - 1) {
                this.mLayoutMode = 6;
                setSelectionInt(selectedPosition + 1);
                moved = true;
                if (selectedPosition == endOfRow) {
                    z = true;
                }
                showScroll = z;
            }
        } else if (selectedPosition > 0) {
            this.mLayoutMode = 6;
            setSelectionInt(selectedPosition - 1);
            moved = true;
            if (selectedPosition == startOfRow) {
                z = true;
            }
            showScroll = z;
        }
        if (moved) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            invokeOnItemScrollListener();
        }
        if (showScroll) {
            awakenScrollBars();
        }
        return moved;
    }

    /* Access modifiers changed, original: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        int closestChildIndex = -1;
        if (gainFocus && previouslyFocusedRect != null) {
            previouslyFocusedRect.offset(this.mScrollX, this.mScrollY);
            Rect otherRect = this.mTempRect;
            int minDistance = Integer.MAX_VALUE;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (isCandidateSelection(i, direction)) {
                    View other = getChildAt(i);
                    other.getDrawingRect(otherRect);
                    offsetDescendantRectToMyCoords(other, otherRect);
                    int distance = AbsListView.getDistance(previouslyFocusedRect, otherRect, direction);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestChildIndex = i;
                    }
                }
            }
        }
        if (closestChildIndex >= 0) {
            setSelection(this.mFirstPosition + closestChildIndex);
        } else {
            requestLayout();
        }
    }

    private boolean isCandidateSelection(int childIndex, int direction) {
        int rowEnd;
        int i;
        int count = getChildCount();
        int invertedIndex = (count - 1) - childIndex;
        boolean z = false;
        if (this.mStackFromBottom) {
            rowEnd = count - 1;
            i = this.mNumColumns;
            rowEnd -= invertedIndex - (invertedIndex % i);
            i = Math.max(0, (rowEnd - i) + 1);
        } else {
            rowEnd = this.mNumColumns;
            i = childIndex - (childIndex % rowEnd);
            rowEnd = Math.min((rowEnd + i) - 1, count);
        }
        if (direction == 1) {
            if (childIndex == rowEnd && rowEnd == count - 1) {
                z = true;
            }
            return z;
        } else if (direction == 2) {
            if (childIndex == i && i == 0) {
                z = true;
            }
            return z;
        } else if (direction == 17) {
            if (childIndex == rowEnd) {
                z = true;
            }
            return z;
        } else if (direction == 33) {
            if (rowEnd == count - 1) {
                z = true;
            }
            return z;
        } else if (direction == 66) {
            if (childIndex == i) {
                z = true;
            }
            return z;
        } else if (direction == 130) {
            if (i == 0) {
                z = true;
            }
            return z;
        } else {
            throw new IllegalArgumentException("direction must be one of {FOCUS_UP, FOCUS_DOWN, FOCUS_LEFT, FOCUS_RIGHT, FOCUS_FORWARD, FOCUS_BACKWARD}.");
        }
    }

    public void setGravity(int gravity) {
        if (this.mGravity != gravity) {
            this.mGravity = gravity;
            requestLayoutIfNecessary();
        }
    }

    public int getGravity() {
        return this.mGravity;
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        if (horizontalSpacing != this.mRequestedHorizontalSpacing) {
            this.mRequestedHorizontalSpacing = horizontalSpacing;
            requestLayoutIfNecessary();
        }
    }

    public int getHorizontalSpacing() {
        return this.mHorizontalSpacing;
    }

    public int getRequestedHorizontalSpacing() {
        return this.mRequestedHorizontalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        if (verticalSpacing != this.mVerticalSpacing) {
            this.mVerticalSpacing = verticalSpacing;
            requestLayoutIfNecessary();
        }
    }

    public int getVerticalSpacing() {
        return this.mVerticalSpacing;
    }

    public void setStretchMode(int stretchMode) {
        if (stretchMode != this.mStretchMode) {
            this.mStretchMode = stretchMode;
            requestLayoutIfNecessary();
        }
    }

    public int getStretchMode() {
        return this.mStretchMode;
    }

    public void setColumnWidth(int columnWidth) {
        if (columnWidth != this.mRequestedColumnWidth) {
            this.mRequestedColumnWidth = columnWidth;
            requestLayoutIfNecessary();
        }
    }

    public int getColumnWidth() {
        return this.mColumnWidth;
    }

    public int getRequestedColumnWidth() {
        return this.mRequestedColumnWidth;
    }

    public void setNumColumns(int numColumns) {
        if (numColumns != this.mRequestedNumColumns) {
            this.mRequestedNumColumns = numColumns;
            requestLayoutIfNecessary();
        }
    }

    @ExportedProperty
    public int getNumColumns() {
        return this.mNumColumns;
    }

    private void adjustViewsUpOrDown() {
        int childCount = getChildCount();
        if (childCount > 0) {
            int delta;
            if (this.mStackFromBottom) {
                delta = getChildAt(childCount - 1).getBottom() - (getHeight() - this.mListPadding.bottom);
                if (this.mFirstPosition + childCount < this.mItemCount) {
                    delta += this.mVerticalSpacing;
                }
                if (delta > 0) {
                    delta = 0;
                }
            } else {
                delta = getChildAt(null).getTop() - this.mListPadding.top;
                if (this.mFirstPosition != 0) {
                    delta -= this.mVerticalSpacing;
                }
                if (delta < 0) {
                    delta = 0;
                }
            }
            if (delta != 0) {
                offsetChildrenTopAndBottom(-delta);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollExtent() {
        int count = getChildCount();
        if (count <= 0) {
            return 0;
        }
        int numColumns = this.mNumColumns;
        int extent = (((count + numColumns) - 1) / numColumns) * 100;
        View view = getChildAt(0);
        int top = view.getTop();
        int height = view.getHeight();
        if (height > 0) {
            extent += (top * 100) / height;
        }
        view = getChildAt(count - 1);
        int bottom = view.getBottom();
        height = view.getHeight();
        if (height > 0) {
            extent -= ((bottom - getHeight()) * 100) / height;
        }
        return extent;
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollOffset() {
        if (this.mFirstPosition >= 0 && getChildCount() > 0) {
            View view = getChildAt(0);
            int top = view.getTop();
            int height = view.getHeight();
            if (height > 0) {
                int oddItemsOnFirstRow;
                int numColumns = this.mNumColumns;
                int rowCount = ((this.mItemCount + numColumns) - 1) / numColumns;
                if (isStackFromBottom()) {
                    oddItemsOnFirstRow = (rowCount * numColumns) - this.mItemCount;
                } else {
                    oddItemsOnFirstRow = 0;
                }
                return Math.max(((((this.mFirstPosition + oddItemsOnFirstRow) / numColumns) * 100) - ((top * 100) / height)) + ((int) (((((float) this.mScrollY) / ((float) getHeight())) * ((float) rowCount)) * 100.0f)), 0);
            }
        }
        return 0;
    }

    /* Access modifiers changed, original: protected */
    public int computeVerticalScrollRange() {
        int numColumns = this.mNumColumns;
        int rowCount = ((this.mItemCount + numColumns) - 1) / numColumns;
        int result = Math.max(rowCount * 100, 0);
        if (this.mScrollY != 0) {
            return result + Math.abs((int) (((((float) this.mScrollY) / ((float) getHeight())) * ((float) rowCount)) * 100.0f));
        }
        return result;
    }

    public CharSequence getAccessibilityClassName() {
        return GridView.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        int columnsCount = getNumColumns();
        int rowsCount = getCount() / columnsCount;
        info.setCollectionInfo(CollectionInfo.obtain(rowsCount, columnsCount, null, getSelectionModeForAccessibility()));
        if (columnsCount > 0 || rowsCount > 0) {
            info.addAction(AccessibilityAction.ACTION_SCROLL_TO_POSITION);
        }
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        if (action == 16908343) {
            int numColumns = getNumColumns();
            int row = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_ROW_INT, -1);
            int position = Math.min(row * numColumns, getCount() - 1);
            if (row >= 0) {
                smoothScrollToPosition(position);
                return true;
            }
        }
        return false;
    }

    public void onInitializeAccessibilityNodeInfoForItem(View view, int position, AccessibilityNodeInfo info) {
        int invertedIndex;
        int row;
        int i = position;
        super.onInitializeAccessibilityNodeInfoForItem(view, position, info);
        int count = getCount();
        int columnsCount = getNumColumns();
        int rowsCount = count / columnsCount;
        if (this.mStackFromBottom) {
            invertedIndex = (count - 1) - i;
            row = (rowsCount - 1) - (invertedIndex / columnsCount);
            invertedIndex = (columnsCount - 1) - (invertedIndex % columnsCount);
        } else {
            invertedIndex = i % columnsCount;
            row = i / columnsCount;
        }
        LayoutParams lp = (LayoutParams) view.getLayoutParams();
        boolean z = lp != null && lp.viewType == -2;
        info.setCollectionItemInfo(CollectionItemInfo.obtain(row, 1, invertedIndex, 1, z, isItemChecked(i)));
    }

    /* Access modifiers changed, original: protected */
    public void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("numColumns", getNumColumns());
    }

    static {
        Extension.get().bindOriginal(new Interface() {
            public void layoutChildren(GridView gridView) {
                gridView.originalLayoutChildren();
            }

            public void fillGap(GridView gridView, boolean b) {
                gridView.originalFillGap(b);
            }
        });
    }
}
