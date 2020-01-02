package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Trace;
import android.util.AttributeSet;
import android.util.Log;
import android.util.MathUtils;
import android.util.SparseBooleanArray;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.RemotableViewMethod;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewHierarchyEncoder;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeInfo.CollectionInfo;
import android.view.accessibility.AccessibilityNodeInfo.CollectionItemInfo;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import android.widget.RemoteViews.RemoteView;
import com.android.internal.R;
import com.google.android.collect.Lists;
import com.miui.internal.variable.api.v29.Android_Widget_ListView.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_ListView.Interface;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@RemoteView
public class ListView extends AbsListView {
    private static final float MAX_SCROLL_FACTOR = 0.33f;
    private static final int MIN_SCROLL_PREVIEW_PIXELS = 2;
    static final int NO_POSITION = -1;
    static final String TAG = "ListView";
    @UnsupportedAppUsage
    private boolean mAreAllItemsSelectable;
    private final ArrowScrollFocusResult mArrowScrollFocusResult;
    @UnsupportedAppUsage
    Drawable mDivider;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    int mDividerHeight;
    private boolean mDividerIsOpaque;
    private Paint mDividerPaint;
    private FocusSelector mFocusSelector;
    private boolean mFooterDividersEnabled;
    @UnsupportedAppUsage
    ArrayList<FixedViewInfo> mFooterViewInfos;
    private boolean mHeaderDividersEnabled;
    @UnsupportedAppUsage
    ArrayList<FixedViewInfo> mHeaderViewInfos;
    private boolean mIsCacheColorOpaque;
    private boolean mItemsCanFocus;
    Drawable mOverScrollFooter;
    Drawable mOverScrollHeader;
    private final Rect mTempRect;

    private static class ArrowScrollFocusResult {
        private int mAmountToScroll;
        private int mSelectedPosition;

        private ArrowScrollFocusResult() {
        }

        /* synthetic */ ArrowScrollFocusResult(AnonymousClass1 x0) {
            this();
        }

        /* Access modifiers changed, original: 0000 */
        public void populate(int selectedPosition, int amountToScroll) {
            this.mSelectedPosition = selectedPosition;
            this.mAmountToScroll = amountToScroll;
        }

        public int getSelectedPosition() {
            return this.mSelectedPosition;
        }

        public int getAmountToScroll() {
            return this.mAmountToScroll;
        }
    }

    public class FixedViewInfo {
        public Object data;
        public boolean isSelectable;
        public View view;
    }

    private class FocusSelector implements Runnable {
        private static final int STATE_REQUEST_FOCUS = 3;
        private static final int STATE_SET_SELECTION = 1;
        private static final int STATE_WAIT_FOR_LAYOUT = 2;
        private int mAction;
        private int mPosition;
        private int mPositionTop;

        private FocusSelector() {
        }

        /* synthetic */ FocusSelector(ListView x0, AnonymousClass1 x1) {
            this();
        }

        /* Access modifiers changed, original: 0000 */
        public FocusSelector setupForSetSelection(int position, int top) {
            this.mPosition = position;
            this.mPositionTop = top;
            this.mAction = 1;
            return this;
        }

        public void run() {
            int i = this.mAction;
            if (i == 1) {
                ListView.this.setSelectionFromTop(this.mPosition, this.mPositionTop);
                this.mAction = 2;
            } else if (i == 3) {
                View child = ListView.this.getChildAt(this.mPosition - ListView.this.mFirstPosition);
                if (child != null) {
                    child.requestFocus();
                }
                this.mAction = -1;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public Runnable setupFocusIfValid(int position) {
            if (this.mAction != 2 || position != this.mPosition) {
                return null;
            }
            this.mAction = 3;
            return this;
        }

        /* Access modifiers changed, original: 0000 */
        public void onLayoutComplete() {
            if (this.mAction == 2) {
                this.mAction = -1;
            }
        }
    }

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<ListView> {
        private int mDividerHeightId;
        private int mDividerId;
        private int mFooterDividersEnabledId;
        private int mHeaderDividersEnabledId;
        private boolean mPropertiesMapped = false;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mDividerId = propertyMapper.mapObject("divider", 16843049);
            this.mDividerHeightId = propertyMapper.mapInt("dividerHeight", 16843050);
            this.mFooterDividersEnabledId = propertyMapper.mapBoolean("footerDividersEnabled", 16843311);
            this.mHeaderDividersEnabledId = propertyMapper.mapBoolean("headerDividersEnabled", 16843310);
            this.mPropertiesMapped = true;
        }

        public void readProperties(ListView node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readObject(this.mDividerId, node.getDivider());
                propertyReader.readInt(this.mDividerHeightId, node.getDividerHeight());
                propertyReader.readBoolean(this.mFooterDividersEnabledId, node.areFooterDividersEnabled());
                propertyReader.readBoolean(this.mHeaderDividersEnabledId, node.areHeaderDividersEnabled());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public ListView(Context context) {
        this(context, null);
    }

    public ListView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842868);
    }

    public ListView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        Context context2 = context;
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mHeaderViewInfos = Lists.newArrayList();
        this.mFooterViewInfos = Lists.newArrayList();
        this.mAreAllItemsSelectable = true;
        this.mItemsCanFocus = false;
        this.mTempRect = new Rect();
        this.mArrowScrollFocusResult = new ArrowScrollFocusResult();
        TypedArray a = context2.obtainStyledAttributes(attrs, R.styleable.ListView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.ListView, attrs, a, defStyleAttr, defStyleRes);
        Object[] entries = a.getTextArray(0);
        if (entries != null) {
            setAdapter(new ArrayAdapter(context2, 17367043, entries));
        }
        Drawable d = a.getDrawable(1);
        if (d != null) {
            setDivider(d);
        }
        Drawable osHeader = a.getDrawable(5);
        if (osHeader != null) {
            setOverscrollHeader(osHeader);
        }
        Drawable osFooter = a.getDrawable(6);
        if (osFooter != null) {
            setOverscrollFooter(osFooter);
        }
        if (a.hasValueOrEmpty(2)) {
            int dividerHeight = a.getDimensionPixelSize(2, 0);
            if (dividerHeight != 0) {
                setDividerHeight(dividerHeight);
            }
        }
        this.mHeaderDividersEnabled = a.getBoolean(3, true);
        this.mFooterDividersEnabled = a.getBoolean(4, true);
        a.recycle();
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).init(this, context, attrs, defStyleAttr, defStyleRes);
            return;
        }
        Drawable drawable = osHeader;
    }

    public int getMaxScrollAmount() {
        return (int) (((float) (this.mBottom - this.mTop)) * MAX_SCROLL_FACTOR);
    }

    private void adjustViewsUpOrDown() {
        int childCount = getChildCount();
        if (childCount > 0) {
            int delta;
            if (this.mStackFromBottom) {
                delta = getChildAt(childCount - 1).getBottom() - (getHeight() - this.mListPadding.bottom);
                if (this.mFirstPosition + childCount < this.mItemCount) {
                    delta += this.mDividerHeight;
                }
                if (delta > 0) {
                    delta = 0;
                }
            } else {
                delta = getChildAt(null).getTop() - this.mListPadding.top;
                if (this.mFirstPosition != 0) {
                    delta -= this.mDividerHeight;
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

    public void addHeaderView(View v, Object data, boolean isSelectable) {
        if (!(v.getParent() == null || v.getParent() == this)) {
            String str = TAG;
            if (Log.isLoggable(str, 5)) {
                Log.w(str, "The specified child already has a parent. You must call removeView() on the child's parent first.");
            }
        }
        FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        this.mHeaderViewInfos.add(info);
        this.mAreAllItemsSelectable &= isSelectable;
        if (this.mAdapter != null) {
            if (!(this.mAdapter instanceof HeaderViewListAdapter)) {
                wrapHeaderListAdapterInternal();
            }
            if (this.mDataSetObserver != null) {
                this.mDataSetObserver.onChanged();
            }
        }
    }

    public void addHeaderView(View v) {
        addHeaderView(v, null, true);
    }

    public int getHeaderViewsCount() {
        return this.mHeaderViewInfos.size();
    }

    public boolean removeHeaderView(View v) {
        if (this.mHeaderViewInfos.size() <= 0) {
            return false;
        }
        boolean result = false;
        if (this.mAdapter != null && ((HeaderViewListAdapter) this.mAdapter).removeHeader(v)) {
            if (this.mDataSetObserver != null) {
                this.mDataSetObserver.onChanged();
            }
            result = true;
        }
        removeFixedViewInfo(v, this.mHeaderViewInfos);
        return result;
    }

    private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; i++) {
            if (((FixedViewInfo) where.get(i)).view == v) {
                where.remove(i);
                return;
            }
        }
    }

    public void addFooterView(View v, Object data, boolean isSelectable) {
        if (!(v.getParent() == null || v.getParent() == this)) {
            String str = TAG;
            if (Log.isLoggable(str, 5)) {
                Log.w(str, "The specified child already has a parent. You must call removeView() on the child's parent first.");
            }
        }
        FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        this.mFooterViewInfos.add(info);
        this.mAreAllItemsSelectable &= isSelectable;
        if (this.mAdapter != null) {
            if (!(this.mAdapter instanceof HeaderViewListAdapter)) {
                wrapHeaderListAdapterInternal();
            }
            if (this.mDataSetObserver != null) {
                this.mDataSetObserver.onChanged();
            }
        }
    }

    public void addFooterView(View v) {
        addFooterView(v, null, true);
    }

    public int getFooterViewsCount() {
        return this.mFooterViewInfos.size();
    }

    public boolean removeFooterView(View v) {
        if (this.mFooterViewInfos.size() <= 0) {
            return false;
        }
        boolean result = false;
        if (this.mAdapter != null && ((HeaderViewListAdapter) this.mAdapter).removeFooter(v)) {
            if (this.mDataSetObserver != null) {
                this.mDataSetObserver.onChanged();
            }
            result = true;
        }
        removeFixedViewInfo(v, this.mFooterViewInfos);
        return result;
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
        if (this.mHeaderViewInfos.size() > 0 || this.mFooterViewInfos.size() > 0) {
            this.mAdapter = wrapHeaderListAdapterInternal(this.mHeaderViewInfos, this.mFooterViewInfos, adapter);
        } else {
            this.mAdapter = adapter;
        }
        this.mOldSelectedPosition = -1;
        this.mOldSelectedRowId = Long.MIN_VALUE;
        super.setAdapter(adapter);
        if (this.mAdapter != null) {
            int position;
            this.mAreAllItemsSelectable = this.mAdapter.areAllItemsEnabled();
            this.mOldItemCount = this.mItemCount;
            this.mItemCount = this.mAdapter.getCount();
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
            if (this.mItemCount == 0) {
                checkSelectionChanged();
            }
        } else {
            this.mAreAllItemsSelectable = true;
            checkFocus();
            checkSelectionChanged();
        }
        requestLayout();
    }

    /* Access modifiers changed, original: 0000 */
    public void resetList() {
        clearRecycledState(this.mHeaderViewInfos);
        clearRecycledState(this.mFooterViewInfos);
        super.resetList();
        this.mLayoutMode = 0;
    }

    private void clearRecycledState(ArrayList<FixedViewInfo> infos) {
        if (infos != null) {
            int count = infos.size();
            for (int i = 0; i < count; i++) {
                LayoutParams params = ((FixedViewInfo) infos.get(i)).view.getLayoutParams();
                if (checkLayoutParams(params)) {
                    ((AbsListView.LayoutParams) params).recycledHeaderFooter = false;
                }
            }
        }
    }

    private boolean showingTopFadingEdge() {
        return this.mFirstPosition > 0 || getChildAt(0).getTop() > this.mScrollY + this.mListPadding.top;
    }

    private boolean showingBottomFadingEdge() {
        int childCount = getChildCount();
        int bottomOfBottomChild = getChildAt(childCount - 1).getBottom();
        int listBottom = (this.mScrollY + getHeight()) - this.mListPadding.bottom;
        if ((this.mFirstPosition + childCount) - 1 < this.mItemCount - 1 || bottomOfBottomChild < listBottom) {
            return true;
        }
        return false;
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {
        int rectTopWithinChild = rect.top;
        rect.offset(child.getLeft(), child.getTop());
        rect.offset(-child.getScrollX(), -child.getScrollY());
        int height = getHeight();
        int listUnfadedTop = getScrollY();
        int listUnfadedBottom = listUnfadedTop + height;
        int fadingEdge = getVerticalFadingEdgeLength();
        if (showingTopFadingEdge() && (this.mSelectedPosition > 0 || rectTopWithinChild > fadingEdge)) {
            listUnfadedTop += fadingEdge;
        }
        int bottomOfBottomChild = getChildAt(getChildCount() - 1).getBottom();
        boolean scroll = true;
        if (showingBottomFadingEdge() && (this.mSelectedPosition < this.mItemCount - 1 || rect.bottom < bottomOfBottomChild - fadingEdge)) {
            listUnfadedBottom -= fadingEdge;
        }
        int scrollYDelta = 0;
        if (rect.bottom > listUnfadedBottom && rect.top > listUnfadedTop) {
            if (rect.height() > height) {
                scrollYDelta = 0 + (rect.top - listUnfadedTop);
            } else {
                scrollYDelta = 0 + (rect.bottom - listUnfadedBottom);
            }
            scrollYDelta = Math.min(scrollYDelta, bottomOfBottomChild - listUnfadedBottom);
        } else if (rect.top < listUnfadedTop && rect.bottom < listUnfadedBottom) {
            if (rect.height() > height) {
                scrollYDelta = 0 - (listUnfadedBottom - rect.bottom);
            } else {
                scrollYDelta = 0 - (listUnfadedTop - rect.top);
            }
            scrollYDelta = Math.max(scrollYDelta, getChildAt(0).getTop() - listUnfadedTop);
        }
        if (scrollYDelta == 0) {
            scroll = false;
        }
        if (scroll) {
            scrollListItemsBy(-scrollYDelta);
            positionSelector(-1, child);
            this.mSelectedTop = child.getTop();
            invalidate();
        }
        return scroll;
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
        int count = getChildCount();
        int paddingTop;
        int startOffset;
        if (down) {
            paddingTop = 0;
            if ((this.mGroupFlags & 34) == 34) {
                paddingTop = getListPaddingTop();
            }
            if (count > 0) {
                startOffset = getChildAt(count - 1).getBottom() + this.mDividerHeight;
            } else {
                startOffset = paddingTop;
            }
            fillDown(this.mFirstPosition + count, startOffset);
            correctTooHigh(getChildCount());
            return;
        }
        paddingTop = 0;
        if ((this.mGroupFlags & 34) == 34) {
            paddingTop = getListPaddingBottom();
        }
        if (count > 0) {
            startOffset = getChildAt(0).getTop() - this.mDividerHeight;
        } else {
            startOffset = getHeight() - paddingTop;
        }
        fillUp(this.mFirstPosition - 1, startOffset);
        correctTooLow(getChildCount());
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private View fillDown(int pos, int nextTop) {
        View selectedView = null;
        int end = this.mBottom - this.mTop;
        if ((this.mGroupFlags & 34) == 34) {
            end -= this.mListPadding.bottom;
        }
        while (true) {
            boolean z = true;
            if (nextTop >= end || pos >= this.mItemCount) {
                setVisibleRangeHint(this.mFirstPosition, (this.mFirstPosition + getChildCount()) - 1);
            } else {
                if (pos != this.mSelectedPosition) {
                    z = false;
                }
                boolean selected = z;
                View child = makeAndAddView(pos, nextTop, true, this.mListPadding.left, selected);
                nextTop = child.getBottom() + this.mDividerHeight;
                if (selected) {
                    selectedView = child;
                }
                pos++;
            }
        }
        setVisibleRangeHint(this.mFirstPosition, (this.mFirstPosition + getChildCount()) - 1);
        return selectedView;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private View fillUp(int pos, int nextBottom) {
        View selectedView = null;
        int end = 0;
        if ((this.mGroupFlags & 34) == 34) {
            end = this.mListPadding.top;
        }
        while (true) {
            boolean z = true;
            if (nextBottom <= end || pos < 0) {
                this.mFirstPosition = pos + 1;
                setVisibleRangeHint(this.mFirstPosition, (this.mFirstPosition + getChildCount()) - 1);
            } else {
                if (pos != this.mSelectedPosition) {
                    z = false;
                }
                boolean selected = z;
                View child = makeAndAddView(pos, nextBottom, false, this.mListPadding.left, selected);
                nextBottom = child.getTop() - this.mDividerHeight;
                if (selected) {
                    selectedView = child;
                }
                pos--;
            }
        }
        this.mFirstPosition = pos + 1;
        setVisibleRangeHint(this.mFirstPosition, (this.mFirstPosition + getChildCount()) - 1);
        return selectedView;
    }

    private View fillFromTop(int nextTop) {
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mSelectedPosition);
        this.mFirstPosition = Math.min(this.mFirstPosition, this.mItemCount - 1);
        if (this.mFirstPosition < 0) {
            this.mFirstPosition = 0;
        }
        return fillDown(this.mFirstPosition, nextTop);
    }

    private View fillFromMiddle(int childrenTop, int childrenBottom) {
        int height = childrenBottom - childrenTop;
        int position = reconcileSelectedPosition();
        View sel = makeAndAddView(position, childrenTop, true, this.mListPadding.left, true);
        this.mFirstPosition = position;
        int selHeight = sel.getMeasuredHeight();
        if (selHeight <= height) {
            sel.offsetTopAndBottom((height - selHeight) / 2);
        }
        fillAboveAndBelow(sel, position);
        if (this.mStackFromBottom) {
            correctTooLow(getChildCount());
        } else {
            correctTooHigh(getChildCount());
        }
        return sel;
    }

    private void fillAboveAndBelow(View sel, int position) {
        int dividerHeight = this.mDividerHeight;
        if (this.mStackFromBottom) {
            fillDown(position + 1, sel.getBottom() + dividerHeight);
            adjustViewsUpOrDown();
            fillUp(position - 1, sel.getTop() - dividerHeight);
            return;
        }
        fillUp(position - 1, sel.getTop() - dividerHeight);
        adjustViewsUpOrDown();
        fillDown(position + 1, sel.getBottom() + dividerHeight);
    }

    private View fillFromSelection(int selectedTop, int childrenTop, int childrenBottom) {
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int topSelectionPixel = getTopSelectionPixel(childrenTop, fadingEdgeLength, selectedPosition);
        int bottomSelectionPixel = getBottomSelectionPixel(childrenBottom, fadingEdgeLength, selectedPosition);
        View sel = makeAndAddView(selectedPosition, selectedTop, true, this.mListPadding.left, true);
        if (sel.getBottom() > bottomSelectionPixel) {
            sel.offsetTopAndBottom(-Math.min(sel.getTop() - topSelectionPixel, sel.getBottom() - bottomSelectionPixel));
        } else if (sel.getTop() < topSelectionPixel) {
            sel.offsetTopAndBottom(Math.min(topSelectionPixel - sel.getTop(), bottomSelectionPixel - sel.getBottom()));
        }
        fillAboveAndBelow(sel, selectedPosition);
        if (this.mStackFromBottom) {
            correctTooLow(getChildCount());
        } else {
            correctTooHigh(getChildCount());
        }
        return sel;
    }

    private int getBottomSelectionPixel(int childrenBottom, int fadingEdgeLength, int selectedPosition) {
        int bottomSelectionPixel = childrenBottom;
        if (selectedPosition != this.mItemCount - 1) {
            return bottomSelectionPixel - fadingEdgeLength;
        }
        return bottomSelectionPixel;
    }

    private int getTopSelectionPixel(int childrenTop, int fadingEdgeLength, int selectedPosition) {
        int topSelectionPixel = childrenTop;
        if (selectedPosition > 0) {
            return topSelectionPixel + fadingEdgeLength;
        }
        return topSelectionPixel;
    }

    @RemotableViewMethod
    public void smoothScrollToPosition(int position) {
        super.smoothScrollToPosition(position);
    }

    @RemotableViewMethod
    public void smoothScrollByOffset(int offset) {
        super.smoothScrollByOffset(offset);
    }

    private View moveSelection(View oldSel, View newSel, int delta, int childrenTop, int childrenBottom) {
        View sel;
        int i = childrenTop;
        int fadingEdgeLength = getVerticalFadingEdgeLength();
        int selectedPosition = this.mSelectedPosition;
        int topSelectionPixel = getTopSelectionPixel(i, fadingEdgeLength, selectedPosition);
        int bottomSelectionPixel = getBottomSelectionPixel(i, fadingEdgeLength, selectedPosition);
        View oldSel2;
        if (delta > 0) {
            oldSel2 = makeAndAddView(selectedPosition - 1, oldSel.getTop(), true, this.mListPadding.left, false);
            int dividerHeight = this.mDividerHeight;
            sel = makeAndAddView(selectedPosition, oldSel2.getBottom() + dividerHeight, true, this.mListPadding.left, true);
            if (sel.getBottom() > bottomSelectionPixel) {
                int offset = Math.min(Math.min(sel.getTop() - topSelectionPixel, sel.getBottom() - bottomSelectionPixel), (childrenBottom - i) / 2);
                oldSel2.offsetTopAndBottom(-offset);
                sel.offsetTopAndBottom(-offset);
            }
            if (this.mStackFromBottom) {
                fillDown(this.mSelectedPosition + 1, sel.getBottom() + dividerHeight);
                adjustViewsUpOrDown();
                fillUp(this.mSelectedPosition - 2, sel.getTop() - dividerHeight);
            } else {
                fillUp(this.mSelectedPosition - 2, sel.getTop() - dividerHeight);
                adjustViewsUpOrDown();
                fillDown(this.mSelectedPosition + 1, sel.getBottom() + dividerHeight);
            }
        } else if (delta < 0) {
            if (newSel != null) {
                sel = makeAndAddView(selectedPosition, newSel.getTop(), true, this.mListPadding.left, true);
            } else {
                sel = makeAndAddView(selectedPosition, oldSel.getTop(), false, this.mListPadding.left, true);
            }
            if (sel.getTop() < topSelectionPixel) {
                sel.offsetTopAndBottom(Math.min(Math.min(topSelectionPixel - sel.getTop(), bottomSelectionPixel - sel.getBottom()), (childrenBottom - i) / 2));
            }
            fillAboveAndBelow(sel, selectedPosition);
            oldSel2 = oldSel;
        } else {
            int oldTop = oldSel.getTop();
            sel = makeAndAddView(selectedPosition, oldTop, true, this.mListPadding.left, true);
            if (oldTop < i && sel.getBottom() < i + 20) {
                sel.offsetTopAndBottom(i - sel.getTop());
            }
            fillAboveAndBelow(sel, selectedPosition);
        }
        return sel;
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        FocusSelector focusSelector = this.mFocusSelector;
        if (focusSelector != null) {
            removeCallbacks(focusSelector);
            this.mFocusSelector = null;
        }
        super.onDetachedFromWindow();
    }

    /* Access modifiers changed, original: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (getChildCount() > 0) {
            View focusedChild = getFocusedChild();
            if (focusedChild != null) {
                int childPosition = this.mFirstPosition + indexOfChild(focusedChild);
                int top = focusedChild.getTop() - Math.max(0, focusedChild.getBottom() - (h - this.mPaddingTop));
                if (this.mFocusSelector == null) {
                    this.mFocusSelector = new FocusSelector(this, null);
                }
                post(this.mFocusSelector.setupForSetSelection(childPosition, top));
            }
        }
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize;
        int heightSize;
        int i = widthMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize2 = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize2 = MeasureSpec.getSize(heightMeasureSpec);
        int childWidth = 0;
        int childHeight = 0;
        int childState = 0;
        this.mItemCount = this.mAdapter == null ? 0 : this.mAdapter.getCount();
        if (this.mItemCount > 0 && (widthMode == 0 || heightMode == 0)) {
            View child = obtainView(0, this.mIsScrap);
            measureScrapChild(child, 0, i, heightSize2);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            childState = View.combineMeasuredStates(0, child.getMeasuredState());
            if (recycleOnMeasure() && this.mRecycler.shouldRecycleViewType(((AbsListView.LayoutParams) child.getLayoutParams()).viewType)) {
                this.mRecycler.addScrapView(child, 0);
            }
        }
        int childWidth2 = childWidth;
        int childHeight2 = childHeight;
        int childState2 = childState;
        if (widthMode == 0) {
            widthSize = ((this.mListPadding.left + this.mListPadding.right) + childWidth2) + getVerticalScrollbarWidth();
        } else {
            widthSize = (-16777216 & childState2) | widthSize2;
        }
        if (heightMode == 0) {
            heightSize = ((this.mListPadding.top + this.mListPadding.bottom) + childHeight2) + (getVerticalFadingEdgeLength() * 2);
        } else {
            heightSize = heightSize2;
        }
        if (heightMode == Integer.MIN_VALUE) {
            heightSize = measureHeightOfChildren(widthMeasureSpec, 0, -1, heightSize, -1);
        }
        setMeasuredDimension(widthSize, heightSize);
        this.mWidthMeasureSpec = i;
    }

    private void measureScrapChild(View child, int position, int widthMeasureSpec, int heightHint) {
        int childHeightSpec;
        AbsListView.LayoutParams p = (AbsListView.LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (AbsListView.LayoutParams) generateDefaultLayoutParams();
            child.setLayoutParams(p);
        }
        p.viewType = this.mAdapter.getItemViewType(position);
        p.isEnabled = this.mAdapter.isEnabled(position);
        p.forceAdd = true;
        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p.width);
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
        } else {
            childHeightSpec = MeasureSpec.makeSafeMeasureSpec(heightHint, 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
        child.forceLayout();
    }

    /* Access modifiers changed, original: protected */
    @ExportedProperty(category = "list")
    public boolean recycleOnMeasure() {
        return true;
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final int measureHeightOfChildren(int widthMeasureSpec, int startPosition, int endPosition, int maxHeight, int disallowPartialChildPosition) {
        int i = maxHeight;
        int i2 = disallowPartialChildPosition;
        ListAdapter adapter = this.mAdapter;
        if (adapter == null) {
            return this.mListPadding.top + this.mListPadding.bottom;
        }
        int returnedHeight = this.mListPadding.top + this.mListPadding.bottom;
        int dividerHeight = this.mDividerHeight;
        int prevHeightWithoutPartialChild = 0;
        int i3 = endPosition;
        i3 = i3 == -1 ? adapter.getCount() - 1 : i3;
        RecycleBin recycleBin = this.mRecycler;
        boolean recyle = recycleOnMeasure();
        boolean[] isScrap = this.mIsScrap;
        int i4 = startPosition;
        while (i4 <= i3) {
            View child = obtainView(i4, isScrap);
            measureScrapChild(child, i4, widthMeasureSpec, i);
            if (i4 > 0) {
                returnedHeight += dividerHeight;
            }
            if (recyle && recycleBin.shouldRecycleViewType(((AbsListView.LayoutParams) child.getLayoutParams()).viewType)) {
                recycleBin.addScrapView(child, -1);
            }
            returnedHeight += child.getMeasuredHeight();
            if (returnedHeight >= i) {
                int i5;
                if (i2 < 0 || i4 <= i2 || prevHeightWithoutPartialChild <= 0 || returnedHeight == i) {
                    i5 = i;
                } else {
                    i5 = prevHeightWithoutPartialChild;
                }
                return i5;
            }
            if (i2 >= 0 && i4 >= i2) {
                prevHeightWithoutPartialChild = returnedHeight;
            }
            i4++;
        }
        int i6 = widthMeasureSpec;
        return returnedHeight;
    }

    /* Access modifiers changed, original: 0000 */
    public int findMotionRow(int y) {
        int childCount = getChildCount();
        if (childCount > 0) {
            int i;
            if (this.mStackFromBottom) {
                for (i = childCount - 1; i >= 0; i--) {
                    if (y >= getChildAt(i).getTop()) {
                        return this.mFirstPosition + i;
                    }
                }
            } else {
                for (i = 0; i < childCount; i++) {
                    if (y <= getChildAt(i).getBottom()) {
                        return this.mFirstPosition + i;
                    }
                }
            }
        }
        return -1;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private View fillSpecific(int position, int top) {
        View below;
        View above;
        boolean tempIsSelected = position == this.mSelectedPosition;
        View temp = makeAndAddView(position, top, true, this.mListPadding.left, tempIsSelected);
        this.mFirstPosition = position;
        int dividerHeight = this.mDividerHeight;
        int childCount;
        if (this.mStackFromBottom) {
            below = fillDown(position + 1, temp.getBottom() + dividerHeight);
            adjustViewsUpOrDown();
            above = fillUp(position - 1, temp.getTop() - dividerHeight);
            childCount = getChildCount();
            if (childCount > 0) {
                correctTooLow(childCount);
            }
        } else {
            above = fillUp(position - 1, temp.getTop() - dividerHeight);
            adjustViewsUpOrDown();
            below = fillDown(position + 1, temp.getBottom() + dividerHeight);
            childCount = getChildCount();
            if (childCount > 0) {
                correctTooHigh(childCount);
            }
        }
        if (tempIsSelected) {
            return temp;
        }
        if (above != null) {
            return above;
        }
        return below;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private void correctTooHigh(int childCount) {
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
                    fillUp(this.mFirstPosition - 1, firstChild.getTop() - this.mDividerHeight);
                    adjustViewsUpOrDown();
                }
            }
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private void correctTooLow(int childCount) {
        if (this.mFirstPosition == 0 && childCount > 0) {
            int end = (this.mBottom - this.mTop) - this.mListPadding.bottom;
            int topOffset = getChildAt(null).getTop() - this.mListPadding.top;
            View lastChild = getChildAt(childCount - 1);
            int lastBottom = lastChild.getBottom();
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
                    fillDown(lastPosition + 1, lastChild.getBottom() + this.mDividerHeight);
                    adjustViewsUpOrDown();
                }
            } else if (lastPosition == this.mItemCount - 1) {
                adjustViewsUpOrDown();
            }
        }
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
    /* JADX WARNING: Removed duplicated region for block: B:169:0x02e0 A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:165:0x02cf A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:82:0x0142 A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x011e A:{SKIP, Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x015f A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x014d A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x016d A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:110:0x01f8 A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:106:0x01de A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:105:0x01ca A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:97:0x01a4 A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:96:0x0192 A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x017b A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:157:0x02be A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x0274 A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:199:0x0358 A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x030b A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:206:0x036e A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:209:0x0385 A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00ae A:{Catch:{ all -> 0x03d2 }} */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x00c7 A:{SYNTHETIC, Splitter:B:50:0x00c7} */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x00b5 A:{Catch:{ all -> 0x03d2 }} */
    public void originalLayoutChildren() {
        /*
        r30 = this;
        r7 = r30;
        r8 = r7.mBlockLayoutRequests;
        if (r8 == 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r0 = 1;
        r7.mBlockLayoutRequests = r0;
        r9 = 0;
        super.layoutChildren();	 Catch:{ all -> 0x03d2 }
        r30.invalidate();	 Catch:{ all -> 0x03d2 }
        r1 = r7.mAdapter;	 Catch:{ all -> 0x03d2 }
        if (r1 != 0) goto L_0x0027;
    L_0x0015:
        r30.resetList();	 Catch:{ all -> 0x03d2 }
        r30.invokeOnItemScrollListener();	 Catch:{ all -> 0x03d2 }
        r0 = r7.mFocusSelector;
        if (r0 == 0) goto L_0x0022;
    L_0x001f:
        r0.onLayoutComplete();
    L_0x0022:
        if (r8 != 0) goto L_0x0026;
    L_0x0024:
        r7.mBlockLayoutRequests = r9;
    L_0x0026:
        return;
    L_0x0027:
        r1 = r7.mListPadding;	 Catch:{ all -> 0x03d2 }
        r1 = r1.top;	 Catch:{ all -> 0x03d2 }
        r10 = r1;
        r1 = r7.mBottom;	 Catch:{ all -> 0x03d2 }
        r2 = r7.mTop;	 Catch:{ all -> 0x03d2 }
        r1 = r1 - r2;
        r2 = r7.mListPadding;	 Catch:{ all -> 0x03d2 }
        r2 = r2.bottom;	 Catch:{ all -> 0x03d2 }
        r11 = r1 - r2;
        r1 = r30.getChildCount();	 Catch:{ all -> 0x03d2 }
        r12 = r1;
        r1 = 0;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = r7.mLayoutMode;	 Catch:{ all -> 0x03d2 }
        r13 = 2;
        if (r6 == r0) goto L_0x00a0;
    L_0x0046:
        if (r6 == r13) goto L_0x007f;
    L_0x0048:
        r14 = 3;
        if (r6 == r14) goto L_0x00a0;
    L_0x004b:
        r14 = 4;
        if (r6 == r14) goto L_0x00a0;
    L_0x004e:
        r14 = 5;
        if (r6 == r14) goto L_0x00a0;
    L_0x0051:
        r6 = r7.mSelectedPosition;	 Catch:{ all -> 0x03d2 }
        r14 = r7.mFirstPosition;	 Catch:{ all -> 0x03d2 }
        r1 = r6 - r14;
        if (r1 < 0) goto L_0x0060;
    L_0x0059:
        if (r1 >= r12) goto L_0x0060;
    L_0x005b:
        r6 = r7.getChildAt(r1);	 Catch:{ all -> 0x03d2 }
        r3 = r6;
    L_0x0060:
        r6 = r7.getChildAt(r9);	 Catch:{ all -> 0x03d2 }
        r4 = r6;
        r6 = r7.mNextSelectedPosition;	 Catch:{ all -> 0x03d2 }
        if (r6 < 0) goto L_0x006f;
    L_0x0069:
        r6 = r7.mNextSelectedPosition;	 Catch:{ all -> 0x03d2 }
        r14 = r7.mSelectedPosition;	 Catch:{ all -> 0x03d2 }
        r2 = r6 - r14;
    L_0x006f:
        r6 = r1 + r2;
        r6 = r7.getChildAt(r6);	 Catch:{ all -> 0x03d2 }
        r5 = r6;
        r17 = r1;
        r18 = r2;
        r14 = r3;
        r15 = r4;
        r16 = r5;
        goto L_0x00a8;
    L_0x007f:
        r6 = r7.mNextSelectedPosition;	 Catch:{ all -> 0x03d2 }
        r14 = r7.mFirstPosition;	 Catch:{ all -> 0x03d2 }
        r1 = r6 - r14;
        if (r1 < 0) goto L_0x0097;
    L_0x0087:
        if (r1 >= r12) goto L_0x0097;
    L_0x0089:
        r6 = r7.getChildAt(r1);	 Catch:{ all -> 0x03d2 }
        r5 = r6;
        r17 = r1;
        r18 = r2;
        r14 = r3;
        r15 = r4;
        r16 = r5;
        goto L_0x00a8;
    L_0x0097:
        r17 = r1;
        r18 = r2;
        r14 = r3;
        r15 = r4;
        r16 = r5;
        goto L_0x00a8;
    L_0x00a0:
        r17 = r1;
        r18 = r2;
        r14 = r3;
        r15 = r4;
        r16 = r5;
    L_0x00a8:
        r1 = r7.mDataChanged;	 Catch:{ all -> 0x03d2 }
        r19 = r1;
        if (r19 == 0) goto L_0x00b1;
    L_0x00ae:
        r30.handleDataChanged();	 Catch:{ all -> 0x03d2 }
    L_0x00b1:
        r1 = r7.mItemCount;	 Catch:{ all -> 0x03d2 }
        if (r1 != 0) goto L_0x00c7;
    L_0x00b5:
        r30.resetList();	 Catch:{ all -> 0x03d2 }
        r30.invokeOnItemScrollListener();	 Catch:{ all -> 0x03d2 }
        r0 = r7.mFocusSelector;
        if (r0 == 0) goto L_0x00c2;
    L_0x00bf:
        r0.onLayoutComplete();
    L_0x00c2:
        if (r8 != 0) goto L_0x00c6;
    L_0x00c4:
        r7.mBlockLayoutRequests = r9;
    L_0x00c6:
        return;
    L_0x00c7:
        r1 = r7.mItemCount;	 Catch:{ all -> 0x03d2 }
        r2 = r7.mAdapter;	 Catch:{ all -> 0x03d2 }
        r2 = r2.getCount();	 Catch:{ all -> 0x03d2 }
        if (r1 != r2) goto L_0x0398;
    L_0x00d1:
        r1 = r7.mNextSelectedPosition;	 Catch:{ all -> 0x03d2 }
        r7.setSelectedPositionInt(r1);	 Catch:{ all -> 0x03d2 }
        r1 = 0;
        r2 = 0;
        r3 = -1;
        r4 = r30.getViewRootImpl();	 Catch:{ all -> 0x03d2 }
        r20 = r4;
        if (r20 == 0) goto L_0x0110;
    L_0x00e1:
        r4 = r20.getAccessibilityFocusedHost();	 Catch:{ all -> 0x03d2 }
        if (r4 == 0) goto L_0x0110;
    L_0x00e7:
        r5 = r7.getAccessibilityFocusedChild(r4);	 Catch:{ all -> 0x03d2 }
        if (r5 == 0) goto L_0x0110;
    L_0x00ed:
        if (r19 == 0) goto L_0x00ff;
    L_0x00ef:
        r6 = r7.isDirectChildHeaderOrFooter(r5);	 Catch:{ all -> 0x03d2 }
        if (r6 != 0) goto L_0x00ff;
    L_0x00f5:
        r6 = r5.hasTransientState();	 Catch:{ all -> 0x03d2 }
        if (r6 == 0) goto L_0x0106;
    L_0x00fb:
        r6 = r7.mAdapterHasStableIds;	 Catch:{ all -> 0x03d2 }
        if (r6 == 0) goto L_0x0106;
    L_0x00ff:
        r2 = r4;
        r6 = r20.getAccessibilityFocusedVirtualView();	 Catch:{ all -> 0x03d2 }
        r1 = r6;
    L_0x0106:
        r6 = r7.getPositionForView(r5);	 Catch:{ all -> 0x03d2 }
        r3 = r6;
        r22 = r1;
        r21 = r2;
        goto L_0x0115;
    L_0x0110:
        r22 = r1;
        r21 = r2;
        r6 = r3;
    L_0x0115:
        r1 = 0;
        r2 = 0;
        r3 = r30.getFocusedChild();	 Catch:{ all -> 0x03d2 }
        r5 = r3;
        if (r5 == 0) goto L_0x0142;
    L_0x011e:
        if (r19 == 0) goto L_0x0130;
    L_0x0120:
        r3 = r7.isDirectChildHeaderOrFooter(r5);	 Catch:{ all -> 0x03d2 }
        if (r3 != 0) goto L_0x0130;
    L_0x0126:
        r3 = r5.hasTransientState();	 Catch:{ all -> 0x03d2 }
        if (r3 != 0) goto L_0x0130;
    L_0x012c:
        r3 = r7.mAdapterHasStableIds;	 Catch:{ all -> 0x03d2 }
        if (r3 == 0) goto L_0x013b;
    L_0x0130:
        r1 = r5;
        r3 = r30.findFocus();	 Catch:{ all -> 0x03d2 }
        r2 = r3;
        if (r2 == 0) goto L_0x013b;
    L_0x0138:
        r2.dispatchStartTemporaryDetach();	 Catch:{ all -> 0x03d2 }
    L_0x013b:
        r30.requestFocus();	 Catch:{ all -> 0x03d2 }
        r4 = r1;
        r23 = r2;
        goto L_0x0145;
    L_0x0142:
        r4 = r1;
        r23 = r2;
    L_0x0145:
        r1 = r7.mFirstPosition;	 Catch:{ all -> 0x03d2 }
        r3 = r1;
        r1 = r7.mRecycler;	 Catch:{ all -> 0x03d2 }
        r2 = r1;
        if (r19 == 0) goto L_0x015f;
    L_0x014d:
        r1 = 0;
    L_0x014e:
        if (r1 >= r12) goto L_0x015e;
    L_0x0150:
        r13 = r7.getChildAt(r1);	 Catch:{ all -> 0x03d2 }
        r9 = r3 + r1;
        r2.addScrapView(r13, r9);	 Catch:{ all -> 0x03d2 }
        r1 = r1 + 1;
        r9 = 0;
        r13 = 2;
        goto L_0x014e;
    L_0x015e:
        goto L_0x0162;
    L_0x015f:
        r2.fillActiveViews(r12, r3);	 Catch:{ all -> 0x03d2 }
    L_0x0162:
        r30.detachAllViewsFromParent();	 Catch:{ all -> 0x03d2 }
        r2.removeSkippedScrap();	 Catch:{ all -> 0x03d2 }
        r1 = r7.mLayoutMode;	 Catch:{ all -> 0x03d2 }
        switch(r1) {
            case 1: goto L_0x01f8;
            case 2: goto L_0x01de;
            case 3: goto L_0x01ca;
            case 4: goto L_0x01a4;
            case 5: goto L_0x0192;
            case 6: goto L_0x017b;
            default: goto L_0x016d;
        };	 Catch:{ all -> 0x03d2 }
    L_0x016d:
        r9 = r2;
        r13 = r3;
        r25 = r4;
        r26 = r5;
        r27 = r6;
        if (r12 != 0) goto L_0x0230;
    L_0x0177:
        r1 = r7.mStackFromBottom;	 Catch:{ all -> 0x03d2 }
        goto L_0x020b;
    L_0x017b:
        r1 = r30;
        r9 = r2;
        r2 = r14;
        r13 = r3;
        r3 = r16;
        r25 = r4;
        r4 = r18;
        r26 = r5;
        r5 = r10;
        r27 = r6;
        r6 = r11;
        r1 = r1.moveSelection(r2, r3, r4, r5, r6);	 Catch:{ all -> 0x03d2 }
        goto L_0x0264;
    L_0x0192:
        r9 = r2;
        r13 = r3;
        r25 = r4;
        r26 = r5;
        r27 = r6;
        r1 = r7.mSyncPosition;	 Catch:{ all -> 0x03d2 }
        r2 = r7.mSpecificTop;	 Catch:{ all -> 0x03d2 }
        r1 = r7.fillSpecific(r1, r2);	 Catch:{ all -> 0x03d2 }
        goto L_0x0264;
    L_0x01a4:
        r9 = r2;
        r13 = r3;
        r25 = r4;
        r26 = r5;
        r27 = r6;
        r1 = r30.reconcileSelectedPosition();	 Catch:{ all -> 0x03d2 }
        r2 = r7.mSpecificTop;	 Catch:{ all -> 0x03d2 }
        r2 = r7.fillSpecific(r1, r2);	 Catch:{ all -> 0x03d2 }
        if (r2 != 0) goto L_0x01c7;
    L_0x01b8:
        r3 = r7.mFocusSelector;	 Catch:{ all -> 0x03d2 }
        if (r3 == 0) goto L_0x01c7;
    L_0x01bc:
        r3 = r7.mFocusSelector;	 Catch:{ all -> 0x03d2 }
        r3 = r3.setupFocusIfValid(r1);	 Catch:{ all -> 0x03d2 }
        if (r3 == 0) goto L_0x01c7;
    L_0x01c4:
        r7.post(r3);	 Catch:{ all -> 0x03d2 }
    L_0x01c7:
        r1 = r2;
        goto L_0x0264;
    L_0x01ca:
        r9 = r2;
        r13 = r3;
        r25 = r4;
        r26 = r5;
        r27 = r6;
        r1 = r7.mItemCount;	 Catch:{ all -> 0x03d2 }
        r1 = r1 - r0;
        r1 = r7.fillUp(r1, r11);	 Catch:{ all -> 0x03d2 }
        r30.adjustViewsUpOrDown();	 Catch:{ all -> 0x03d2 }
        goto L_0x0264;
    L_0x01de:
        r9 = r2;
        r13 = r3;
        r25 = r4;
        r26 = r5;
        r27 = r6;
        if (r16 == 0) goto L_0x01f2;
    L_0x01e8:
        r1 = r16.getTop();	 Catch:{ all -> 0x03d2 }
        r1 = r7.fillFromSelection(r1, r10, r11);	 Catch:{ all -> 0x03d2 }
        goto L_0x0264;
    L_0x01f2:
        r1 = r7.fillFromMiddle(r10, r11);	 Catch:{ all -> 0x03d2 }
        goto L_0x0264;
    L_0x01f8:
        r9 = r2;
        r13 = r3;
        r25 = r4;
        r26 = r5;
        r27 = r6;
        r1 = 0;
        r7.mFirstPosition = r1;	 Catch:{ all -> 0x03d2 }
        r1 = r7.fillFromTop(r10);	 Catch:{ all -> 0x03d2 }
        r30.adjustViewsUpOrDown();	 Catch:{ all -> 0x03d2 }
        goto L_0x0264;
    L_0x020b:
        if (r1 != 0) goto L_0x021c;
    L_0x020d:
        r1 = 0;
        r2 = r7.lookForSelectablePosition(r1, r0);	 Catch:{ all -> 0x03d2 }
        r1 = r2;
        r7.setSelectedPositionInt(r1);	 Catch:{ all -> 0x03d2 }
        r2 = r7.fillFromTop(r10);	 Catch:{ all -> 0x03d2 }
        r1 = r2;
        goto L_0x0264;
    L_0x021c:
        r1 = r7.mItemCount;	 Catch:{ all -> 0x03d2 }
        r1 = r1 - r0;
        r2 = 0;
        r1 = r7.lookForSelectablePosition(r1, r2);	 Catch:{ all -> 0x03d2 }
        r7.setSelectedPositionInt(r1);	 Catch:{ all -> 0x03d2 }
        r2 = r7.mItemCount;	 Catch:{ all -> 0x03d2 }
        r2 = r2 - r0;
        r2 = r7.fillUp(r2, r11);	 Catch:{ all -> 0x03d2 }
        r1 = r2;
        goto L_0x0264;
    L_0x0230:
        r1 = r7.mSelectedPosition;	 Catch:{ all -> 0x03d2 }
        if (r1 < 0) goto L_0x0249;
    L_0x0234:
        r1 = r7.mSelectedPosition;	 Catch:{ all -> 0x03d2 }
        r2 = r7.mItemCount;	 Catch:{ all -> 0x03d2 }
        if (r1 >= r2) goto L_0x0249;
    L_0x023a:
        r1 = r7.mSelectedPosition;	 Catch:{ all -> 0x03d2 }
        if (r14 != 0) goto L_0x0240;
    L_0x023e:
        r2 = r10;
        goto L_0x0244;
    L_0x0240:
        r2 = r14.getTop();	 Catch:{ all -> 0x03d2 }
    L_0x0244:
        r1 = r7.fillSpecific(r1, r2);	 Catch:{ all -> 0x03d2 }
        goto L_0x0264;
    L_0x0249:
        r1 = r7.mFirstPosition;	 Catch:{ all -> 0x03d2 }
        r2 = r7.mItemCount;	 Catch:{ all -> 0x03d2 }
        if (r1 >= r2) goto L_0x025e;
    L_0x024f:
        r1 = r7.mFirstPosition;	 Catch:{ all -> 0x03d2 }
        if (r15 != 0) goto L_0x0255;
    L_0x0253:
        r2 = r10;
        goto L_0x0259;
    L_0x0255:
        r2 = r15.getTop();	 Catch:{ all -> 0x03d2 }
    L_0x0259:
        r1 = r7.fillSpecific(r1, r2);	 Catch:{ all -> 0x03d2 }
        goto L_0x0264;
    L_0x025e:
        r1 = 0;
        r2 = r7.fillSpecific(r1, r10);	 Catch:{ all -> 0x03d2 }
        r1 = r2;
    L_0x0264:
        r9.scrapActiveViews();	 Catch:{ all -> 0x03d2 }
        r2 = r7.mHeaderViewInfos;	 Catch:{ all -> 0x03d2 }
        r7.removeUnusedFixedViews(r2);	 Catch:{ all -> 0x03d2 }
        r2 = r7.mFooterViewInfos;	 Catch:{ all -> 0x03d2 }
        r7.removeUnusedFixedViews(r2);	 Catch:{ all -> 0x03d2 }
        r2 = -1;
        if (r1 == 0) goto L_0x02be;
    L_0x0274:
        r3 = r7.mItemsCanFocus;	 Catch:{ all -> 0x03d2 }
        if (r3 == 0) goto L_0x02b2;
    L_0x0278:
        r3 = r30.hasFocus();	 Catch:{ all -> 0x03d2 }
        if (r3 == 0) goto L_0x02b2;
    L_0x027e:
        r3 = r1.hasFocus();	 Catch:{ all -> 0x03d2 }
        if (r3 != 0) goto L_0x02b2;
    L_0x0284:
        r3 = r25;
        if (r1 != r3) goto L_0x0290;
    L_0x0288:
        if (r23 == 0) goto L_0x0290;
    L_0x028a:
        r4 = r23.requestFocus();	 Catch:{ all -> 0x03d2 }
        if (r4 != 0) goto L_0x0296;
    L_0x0290:
        r4 = r1.requestFocus();	 Catch:{ all -> 0x03d2 }
        if (r4 == 0) goto L_0x0298;
    L_0x0296:
        r4 = r0;
        goto L_0x0299;
    L_0x0298:
        r4 = 0;
    L_0x0299:
        if (r4 != 0) goto L_0x02a8;
    L_0x029b:
        r5 = r30.getFocusedChild();	 Catch:{ all -> 0x03d2 }
        if (r5 == 0) goto L_0x02a4;
    L_0x02a1:
        r5.clearFocus();	 Catch:{ all -> 0x03d2 }
    L_0x02a4:
        r7.positionSelector(r2, r1);	 Catch:{ all -> 0x03d2 }
        goto L_0x02b1;
    L_0x02a8:
        r5 = 0;
        r1.setSelected(r5);	 Catch:{ all -> 0x03d2 }
        r5 = r7.mSelectorRect;	 Catch:{ all -> 0x03d2 }
        r5.setEmpty();	 Catch:{ all -> 0x03d2 }
    L_0x02b1:
        goto L_0x02b7;
    L_0x02b2:
        r3 = r25;
        r7.positionSelector(r2, r1);	 Catch:{ all -> 0x03d2 }
    L_0x02b7:
        r4 = r1.getTop();	 Catch:{ all -> 0x03d2 }
        r7.mSelectedTop = r4;	 Catch:{ all -> 0x03d2 }
        goto L_0x0308;
    L_0x02be:
        r3 = r25;
        r4 = r7.mTouchMode;	 Catch:{ all -> 0x03d2 }
        if (r4 == r0) goto L_0x02cc;
    L_0x02c4:
        r4 = r7.mTouchMode;	 Catch:{ all -> 0x03d2 }
        r5 = 2;
        if (r4 != r5) goto L_0x02ca;
    L_0x02c9:
        goto L_0x02cc;
    L_0x02ca:
        r4 = 0;
        goto L_0x02cd;
    L_0x02cc:
        r4 = r0;
    L_0x02cd:
        if (r4 == 0) goto L_0x02e0;
    L_0x02cf:
        r5 = r7.mMotionPosition;	 Catch:{ all -> 0x03d2 }
        r6 = r7.mFirstPosition;	 Catch:{ all -> 0x03d2 }
        r5 = r5 - r6;
        r5 = r7.getChildAt(r5);	 Catch:{ all -> 0x03d2 }
        if (r5 == 0) goto L_0x02df;
    L_0x02da:
        r6 = r7.mMotionPosition;	 Catch:{ all -> 0x03d2 }
        r7.positionSelector(r6, r5);	 Catch:{ all -> 0x03d2 }
    L_0x02df:
        goto L_0x02fd;
    L_0x02e0:
        r5 = r7.mSelectorPosition;	 Catch:{ all -> 0x03d2 }
        if (r5 == r2) goto L_0x02f5;
    L_0x02e4:
        r5 = r7.mSelectorPosition;	 Catch:{ all -> 0x03d2 }
        r6 = r7.mFirstPosition;	 Catch:{ all -> 0x03d2 }
        r5 = r5 - r6;
        r5 = r7.getChildAt(r5);	 Catch:{ all -> 0x03d2 }
        if (r5 == 0) goto L_0x02f4;
    L_0x02ef:
        r6 = r7.mSelectorPosition;	 Catch:{ all -> 0x03d2 }
        r7.positionSelector(r6, r5);	 Catch:{ all -> 0x03d2 }
    L_0x02f4:
        goto L_0x02fd;
    L_0x02f5:
        r5 = 0;
        r7.mSelectedTop = r5;	 Catch:{ all -> 0x03d2 }
        r5 = r7.mSelectorRect;	 Catch:{ all -> 0x03d2 }
        r5.setEmpty();	 Catch:{ all -> 0x03d2 }
    L_0x02fd:
        r5 = r30.hasFocus();	 Catch:{ all -> 0x03d2 }
        if (r5 == 0) goto L_0x0308;
    L_0x0303:
        if (r23 == 0) goto L_0x0308;
    L_0x0305:
        r23.requestFocus();	 Catch:{ all -> 0x03d2 }
    L_0x0308:
        r4 = 0;
        if (r20 == 0) goto L_0x0358;
    L_0x030b:
        r5 = r20.getAccessibilityFocusedHost();	 Catch:{ all -> 0x03d2 }
        if (r5 != 0) goto L_0x0355;
    L_0x0311:
        if (r21 == 0) goto L_0x0338;
    L_0x0313:
        r6 = r21.isAttachedToWindow();	 Catch:{ all -> 0x03d2 }
        if (r6 == 0) goto L_0x0338;
        r0 = r21.getAccessibilityNodeProvider();	 Catch:{ all -> 0x03d2 }
        if (r22 == 0) goto L_0x0332;
    L_0x0320:
        if (r0 == 0) goto L_0x0332;
        r28 = r22.getSourceNodeId();	 Catch:{ all -> 0x03d2 }
        r2 = android.view.accessibility.AccessibilityNodeInfo.getVirtualDescendantId(r28);	 Catch:{ all -> 0x03d2 }
        r6 = 64;
        r0.performAction(r2, r6, r4);	 Catch:{ all -> 0x03d2 }
        goto L_0x0335;
    L_0x0332:
        r21.requestAccessibilityFocus();	 Catch:{ all -> 0x03d2 }
    L_0x0335:
        r6 = r27;
        goto L_0x035a;
    L_0x0338:
        r6 = r27;
        if (r6 == r2) goto L_0x035a;
    L_0x033c:
        r2 = r7.mFirstPosition;	 Catch:{ all -> 0x03d2 }
        r2 = r6 - r2;
        r24 = r30.getChildCount();	 Catch:{ all -> 0x03d2 }
        r0 = r24 + -1;
        r4 = 0;
        r0 = android.util.MathUtils.constrain(r2, r4, r0);	 Catch:{ all -> 0x03d2 }
        r2 = r7.getChildAt(r0);	 Catch:{ all -> 0x03d2 }
        if (r2 == 0) goto L_0x035a;
    L_0x0351:
        r2.requestAccessibilityFocus();	 Catch:{ all -> 0x03d2 }
        goto L_0x035a;
    L_0x0355:
        r6 = r27;
        goto L_0x035a;
    L_0x0358:
        r6 = r27;
    L_0x035a:
        if (r23 == 0) goto L_0x0365;
    L_0x035c:
        r0 = r23.getWindowToken();	 Catch:{ all -> 0x03d2 }
        if (r0 == 0) goto L_0x0365;
    L_0x0362:
        r23.dispatchFinishTemporaryDetach();	 Catch:{ all -> 0x03d2 }
    L_0x0365:
        r2 = 0;
        r7.mLayoutMode = r2;	 Catch:{ all -> 0x03d2 }
        r7.mDataChanged = r2;	 Catch:{ all -> 0x03d2 }
        r0 = r7.mPositionScrollAfterLayout;	 Catch:{ all -> 0x03d2 }
        if (r0 == 0) goto L_0x0376;
    L_0x036e:
        r0 = r7.mPositionScrollAfterLayout;	 Catch:{ all -> 0x03d2 }
        r7.post(r0);	 Catch:{ all -> 0x03d2 }
        r0 = 0;
        r7.mPositionScrollAfterLayout = r0;	 Catch:{ all -> 0x03d2 }
    L_0x0376:
        r2 = 0;
        r7.mNeedSync = r2;	 Catch:{ all -> 0x03d2 }
        r0 = r7.mSelectedPosition;	 Catch:{ all -> 0x03d2 }
        r7.setNextSelectedPositionInt(r0);	 Catch:{ all -> 0x03d2 }
        r30.updateScrollIndicators();	 Catch:{ all -> 0x03d2 }
        r0 = r7.mItemCount;	 Catch:{ all -> 0x03d2 }
        if (r0 <= 0) goto L_0x0388;
    L_0x0385:
        r30.checkSelectionChanged();	 Catch:{ all -> 0x03d2 }
    L_0x0388:
        r30.invokeOnItemScrollListener();	 Catch:{ all -> 0x03d2 }
        r0 = r7.mFocusSelector;
        if (r0 == 0) goto L_0x0392;
    L_0x038f:
        r0.onLayoutComplete();
    L_0x0392:
        if (r8 != 0) goto L_0x0397;
    L_0x0394:
        r1 = 0;
        r7.mBlockLayoutRequests = r1;
    L_0x0397:
        return;
    L_0x0398:
        r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x03d2 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03d2 }
        r1.<init>();	 Catch:{ all -> 0x03d2 }
        r2 = "The content of the adapter has changed but ListView did not receive a notification. Make sure the content of your adapter is not modified from a background thread, but only from the UI thread. Make sure your adapter calls notifyDataSetChanged() when its content changes. [in ListView(";
        r1.append(r2);	 Catch:{ all -> 0x03d2 }
        r2 = r30.getId();	 Catch:{ all -> 0x03d2 }
        r1.append(r2);	 Catch:{ all -> 0x03d2 }
        r2 = ", ";
        r1.append(r2);	 Catch:{ all -> 0x03d2 }
        r2 = r30.getClass();	 Catch:{ all -> 0x03d2 }
        r1.append(r2);	 Catch:{ all -> 0x03d2 }
        r2 = ") with Adapter(";
        r1.append(r2);	 Catch:{ all -> 0x03d2 }
        r2 = r7.mAdapter;	 Catch:{ all -> 0x03d2 }
        r2 = r2.getClass();	 Catch:{ all -> 0x03d2 }
        r1.append(r2);	 Catch:{ all -> 0x03d2 }
        r2 = ")]";
        r1.append(r2);	 Catch:{ all -> 0x03d2 }
        r1 = r1.toString();	 Catch:{ all -> 0x03d2 }
        r0.<init>(r1);	 Catch:{ all -> 0x03d2 }
        throw r0;	 Catch:{ all -> 0x03d2 }
    L_0x03d2:
        r0 = move-exception;
        r1 = r7.mFocusSelector;
        if (r1 == 0) goto L_0x03da;
    L_0x03d7:
        r1.onLayoutComplete();
    L_0x03da:
        if (r8 != 0) goto L_0x03df;
    L_0x03dc:
        r1 = 0;
        r7.mBlockLayoutRequests = r1;
    L_0x03df:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ListView.originalLayoutChildren():void");
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean trackMotionScroll(int deltaY, int incrementalDeltaY) {
        boolean result = super.trackMotionScroll(deltaY, incrementalDeltaY);
        removeUnusedFixedViews(this.mHeaderViewInfos);
        removeUnusedFixedViews(this.mFooterViewInfos);
        return result;
    }

    private void removeUnusedFixedViews(List<FixedViewInfo> infoList) {
        if (infoList != null) {
            for (int i = infoList.size() - 1; i >= 0; i--) {
                View view = ((FixedViewInfo) infoList.get(i)).view;
                AbsListView.LayoutParams lp = (AbsListView.LayoutParams) view.getLayoutParams();
                if (view.getParent() == null && lp != null && lp.recycledHeaderFooter) {
                    removeDetachedView(view, false);
                    lp.recycledHeaderFooter = false;
                }
            }
        }
    }

    @UnsupportedAppUsage
    private boolean isDirectChildHeaderOrFooter(View child) {
        ArrayList<FixedViewInfo> headers = this.mHeaderViewInfos;
        int numHeaders = headers.size();
        for (int i = 0; i < numHeaders; i++) {
            if (child == ((FixedViewInfo) headers.get(i)).view) {
                return true;
            }
        }
        ArrayList<FixedViewInfo> footers = this.mFooterViewInfos;
        int numFooters = footers.size();
        for (int i2 = 0; i2 < numFooters; i2++) {
            if (child == ((FixedViewInfo) footers.get(i2)).view) {
                return true;
            }
        }
        return false;
    }

    @UnsupportedAppUsage
    private View makeAndAddView(int position, int y, boolean flow, int childrenLeft, boolean selected) {
        View activeView;
        if (!this.mDataChanged) {
            activeView = this.mRecycler.getActiveView(position);
            if (activeView != null) {
                setupChild(activeView, position, y, flow, childrenLeft, selected, true);
                return activeView;
            }
        }
        activeView = obtainView(position, this.mIsScrap);
        setupChild(activeView, position, y, flow, childrenLeft, selected, this.mIsScrap[0]);
        return activeView;
    }

    private void setupChild(View child, int position, int y, boolean flowDown, int childrenLeft, boolean selected, boolean isAttachedToWindow) {
        int lpHeight;
        int childHeightSpec;
        View view = child;
        int i = position;
        int i2 = childrenLeft;
        Trace.traceBegin(8, "setupListItem");
        boolean isSelected = selected && shouldShowSelector();
        boolean updateChildSelected = isSelected != child.isSelected();
        int mode = this.mTouchMode;
        boolean isPressed = mode > 0 && mode < 3 && this.mMotionPosition == i;
        boolean updateChildPressed = isPressed != child.isPressed();
        boolean needToMeasure = !isAttachedToWindow || updateChildSelected || child.isLayoutRequested();
        AbsListView.LayoutParams p = (AbsListView.LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = (AbsListView.LayoutParams) generateDefaultLayoutParams();
        }
        p.viewType = this.mAdapter.getItemViewType(i);
        p.isEnabled = this.mAdapter.isEnabled(i);
        if (updateChildSelected) {
            view.setSelected(isSelected);
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
        int i3 = -1;
        if ((!isAttachedToWindow || p.forceAdd) && !(p.recycledHeaderFooter && p.viewType == -2)) {
            p.forceAdd = false;
            if (p.viewType == -2) {
                p.recycledHeaderFooter = true;
            }
            if (!flowDown) {
                i3 = 0;
            }
            addViewInLayout(view, i3, p, true);
            child.resolveRtlPropertiesIfNeeded();
        } else {
            if (!flowDown) {
                i3 = 0;
            }
            attachViewToParent(view, i3, p);
            if (isAttachedToWindow && ((AbsListView.LayoutParams) child.getLayoutParams()).scrappedFromPosition != i) {
                child.jumpDrawablesToCurrentState();
            }
        }
        if (needToMeasure) {
            i3 = ViewGroup.getChildMeasureSpec(this.mWidthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p.width);
            lpHeight = p.height;
            if (lpHeight > 0) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
            } else {
                childHeightSpec = MeasureSpec.makeSafeMeasureSpec(getMeasuredHeight(), 0);
            }
            view.measure(i3, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }
        i3 = child.getMeasuredWidth();
        lpHeight = child.getMeasuredHeight();
        childHeightSpec = flowDown ? y : y - lpHeight;
        if (needToMeasure) {
            view.layout(i2, childHeightSpec, i2 + i3, childHeightSpec + lpHeight);
        } else {
            view.offsetLeftAndRight(i2 - child.getLeft());
            view.offsetTopAndBottom(childHeightSpec - child.getTop());
        }
        if (this.mCachingStarted && !child.isDrawingCacheEnabled()) {
            view.setDrawingCacheEnabled(true);
        }
        Trace.traceEnd(8);
    }

    /* Access modifiers changed, original: protected */
    public boolean canAnimate() {
        return super.canAnimate() && this.mItemCount > 0;
    }

    public void setSelection(int position) {
        setSelectionFromTop(position, 0);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void setSelectionInt(int position) {
        setNextSelectedPositionInt(position);
        boolean awakeScrollbars = false;
        int selectedPosition = this.mSelectedPosition;
        if (selectedPosition >= 0) {
            if (position == selectedPosition - 1) {
                awakeScrollbars = true;
            } else if (position == selectedPosition + 1) {
                awakeScrollbars = true;
            }
        }
        if (this.mPositionScroller != null) {
            this.mPositionScroller.stop();
        }
        layoutChildren();
        if (awakeScrollbars) {
            awakenScrollBars();
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public int lookForSelectablePosition(int position, boolean lookDown) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null || isInTouchMode()) {
            return -1;
        }
        int count = adapter.getCount();
        if (!this.mAreAllItemsSelectable) {
            if (lookDown) {
                position = Math.max(0, position);
                while (position < count && !adapter.isEnabled(position)) {
                    position++;
                }
            } else {
                position = Math.min(position, count - 1);
                while (position >= 0 && !adapter.isEnabled(position)) {
                    position--;
                }
            }
        }
        if (position < 0 || position >= count) {
            return -1;
        }
        return position;
    }

    /* Access modifiers changed, original: 0000 */
    public int lookForSelectablePositionAfter(int current, int position, boolean lookDown) {
        ListAdapter adapter = this.mAdapter;
        if (adapter == null || isInTouchMode()) {
            return -1;
        }
        int after = lookForSelectablePosition(position, lookDown);
        if (after != -1) {
            return after;
        }
        int count = adapter.getCount();
        current = MathUtils.constrain(current, -1, count - 1);
        if (lookDown) {
            position = Math.min(position - 1, count - 1);
            while (position > current && !adapter.isEnabled(position)) {
                position--;
            }
            if (position <= current) {
                return -1;
            }
        }
        position = Math.max(0, position + 1);
        while (position < current && !adapter.isEnabled(position)) {
            position++;
        }
        if (position >= current) {
            return -1;
        }
        return position;
    }

    public void setSelectionAfterHeaderView() {
        int count = getHeaderViewsCount();
        if (count > 0) {
            this.mNextSelectedPosition = 0;
            return;
        }
        if (this.mAdapter != null) {
            setSelection(count);
        } else {
            this.mNextSelectedPosition = count;
            this.mLayoutMode = 2;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean handled = super.dispatchKeyEvent(event);
        if (handled || getFocusedChild() == null || event.getAction() != 0) {
            return handled;
        }
        return onKeyDown(event.getKeyCode(), event);
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

    /* JADX WARNING: Missing block: B:66:0x00c7, code skipped:
            r10 = r5;
     */
    private boolean commonKey(int r9, int r10, android.view.KeyEvent r11) {
        /*
        r8 = this;
        r0 = r8.mAdapter;
        r1 = 0;
        if (r0 == 0) goto L_0x01c6;
    L_0x0005:
        r0 = r8.isAttachedToWindow();
        if (r0 != 0) goto L_0x000d;
    L_0x000b:
        goto L_0x01c6;
    L_0x000d:
        r0 = r8.mDataChanged;
        if (r0 == 0) goto L_0x0014;
    L_0x0011:
        r8.layoutChildren();
    L_0x0014:
        r0 = 0;
        r2 = r11.getAction();
        r3 = android.view.KeyEvent.isConfirmKey(r9);
        r4 = 1;
        if (r3 == 0) goto L_0x003e;
    L_0x0020:
        r3 = r11.hasNoModifiers();
        if (r3 == 0) goto L_0x003e;
    L_0x0026:
        if (r2 == r4) goto L_0x003e;
    L_0x0028:
        r0 = r8.resurrectSelectionIfNeeded();
        if (r0 != 0) goto L_0x003e;
    L_0x002e:
        r3 = r11.getRepeatCount();
        if (r3 != 0) goto L_0x003e;
    L_0x0034:
        r3 = r8.getChildCount();
        if (r3 <= 0) goto L_0x003e;
    L_0x003a:
        r8.keyPressed();
        r0 = 1;
    L_0x003e:
        r3 = 2;
        if (r0 != 0) goto L_0x01a6;
    L_0x0041:
        if (r2 == r4) goto L_0x01a6;
    L_0x0043:
        r5 = 61;
        r6 = 33;
        r7 = 130; // 0x82 float:1.82E-43 double:6.4E-322;
        if (r9 == r5) goto L_0x0177;
    L_0x004b:
        r5 = 92;
        if (r9 == r5) goto L_0x0147;
    L_0x004f:
        r5 = 93;
        if (r9 == r5) goto L_0x0115;
    L_0x0053:
        r5 = 122; // 0x7a float:1.71E-43 double:6.03E-322;
        if (r9 == r5) goto L_0x00fc;
    L_0x0057:
        r5 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        if (r9 == r5) goto L_0x00e3;
    L_0x005b:
        switch(r9) {
            case 19: goto L_0x00ae;
            case 20: goto L_0x007c;
            case 21: goto L_0x006e;
            case 22: goto L_0x0060;
            default: goto L_0x005e;
        };
    L_0x005e:
        goto L_0x01a6;
    L_0x0060:
        r5 = r11.hasNoModifiers();
        if (r5 == 0) goto L_0x01a6;
    L_0x0066:
        r5 = 66;
        r0 = r8.handleHorizontalFocusWithinListItem(r5);
        goto L_0x01a6;
    L_0x006e:
        r5 = r11.hasNoModifiers();
        if (r5 == 0) goto L_0x01a6;
    L_0x0074:
        r5 = 17;
        r0 = r8.handleHorizontalFocusWithinListItem(r5);
        goto L_0x01a6;
    L_0x007c:
        r5 = r11.hasNoModifiers();
        if (r5 == 0) goto L_0x0095;
    L_0x0082:
        r0 = r8.resurrectSelectionIfNeeded();
        if (r0 != 0) goto L_0x01a6;
    L_0x0088:
        r5 = r10 + -1;
        if (r10 <= 0) goto L_0x00c7;
    L_0x008c:
        r10 = r8.arrowScroll(r7);
        if (r10 == 0) goto L_0x00c7;
    L_0x0092:
        r0 = 1;
        r10 = r5;
        goto L_0x0088;
    L_0x0095:
        r5 = r11.hasModifiers(r3);
        if (r5 == 0) goto L_0x01a6;
    L_0x009b:
        r5 = r8.resurrectSelectionIfNeeded();
        if (r5 != 0) goto L_0x00aa;
    L_0x00a1:
        r5 = r8.fullScroll(r7);
        if (r5 == 0) goto L_0x00a8;
    L_0x00a7:
        goto L_0x00aa;
    L_0x00a8:
        r5 = r1;
        goto L_0x00ab;
    L_0x00aa:
        r5 = r4;
    L_0x00ab:
        r0 = r5;
        goto L_0x01a6;
    L_0x00ae:
        r5 = r11.hasNoModifiers();
        if (r5 == 0) goto L_0x00ca;
    L_0x00b4:
        r0 = r8.resurrectSelectionIfNeeded();
        if (r0 != 0) goto L_0x01a6;
    L_0x00ba:
        r5 = r10 + -1;
        if (r10 <= 0) goto L_0x00c7;
    L_0x00be:
        r10 = r8.arrowScroll(r6);
        if (r10 == 0) goto L_0x00c7;
    L_0x00c4:
        r0 = 1;
        r10 = r5;
        goto L_0x00ba;
    L_0x00c7:
        r10 = r5;
        goto L_0x01a6;
    L_0x00ca:
        r5 = r11.hasModifiers(r3);
        if (r5 == 0) goto L_0x01a6;
    L_0x00d0:
        r5 = r8.resurrectSelectionIfNeeded();
        if (r5 != 0) goto L_0x00df;
    L_0x00d6:
        r5 = r8.fullScroll(r6);
        if (r5 == 0) goto L_0x00dd;
    L_0x00dc:
        goto L_0x00df;
    L_0x00dd:
        r5 = r1;
        goto L_0x00e0;
    L_0x00df:
        r5 = r4;
    L_0x00e0:
        r0 = r5;
        goto L_0x01a6;
    L_0x00e3:
        r5 = r11.hasNoModifiers();
        if (r5 == 0) goto L_0x01a6;
    L_0x00e9:
        r5 = r8.resurrectSelectionIfNeeded();
        if (r5 != 0) goto L_0x00f8;
    L_0x00ef:
        r5 = r8.fullScroll(r7);
        if (r5 == 0) goto L_0x00f6;
    L_0x00f5:
        goto L_0x00f8;
    L_0x00f6:
        r5 = r1;
        goto L_0x00f9;
    L_0x00f8:
        r5 = r4;
    L_0x00f9:
        r0 = r5;
        goto L_0x01a6;
    L_0x00fc:
        r5 = r11.hasNoModifiers();
        if (r5 == 0) goto L_0x01a6;
    L_0x0102:
        r5 = r8.resurrectSelectionIfNeeded();
        if (r5 != 0) goto L_0x0111;
    L_0x0108:
        r5 = r8.fullScroll(r6);
        if (r5 == 0) goto L_0x010f;
    L_0x010e:
        goto L_0x0111;
    L_0x010f:
        r5 = r1;
        goto L_0x0112;
    L_0x0111:
        r5 = r4;
    L_0x0112:
        r0 = r5;
        goto L_0x01a6;
    L_0x0115:
        r5 = r11.hasNoModifiers();
        if (r5 == 0) goto L_0x012e;
    L_0x011b:
        r5 = r8.resurrectSelectionIfNeeded();
        if (r5 != 0) goto L_0x012a;
    L_0x0121:
        r5 = r8.pageScroll(r7);
        if (r5 == 0) goto L_0x0128;
    L_0x0127:
        goto L_0x012a;
    L_0x0128:
        r5 = r1;
        goto L_0x012b;
    L_0x012a:
        r5 = r4;
    L_0x012b:
        r0 = r5;
        goto L_0x01a6;
    L_0x012e:
        r5 = r11.hasModifiers(r3);
        if (r5 == 0) goto L_0x01a6;
    L_0x0134:
        r5 = r8.resurrectSelectionIfNeeded();
        if (r5 != 0) goto L_0x0143;
    L_0x013a:
        r5 = r8.fullScroll(r7);
        if (r5 == 0) goto L_0x0141;
    L_0x0140:
        goto L_0x0143;
    L_0x0141:
        r5 = r1;
        goto L_0x0144;
    L_0x0143:
        r5 = r4;
    L_0x0144:
        r0 = r5;
        goto L_0x01a6;
    L_0x0147:
        r5 = r11.hasNoModifiers();
        if (r5 == 0) goto L_0x015f;
    L_0x014d:
        r5 = r8.resurrectSelectionIfNeeded();
        if (r5 != 0) goto L_0x015c;
    L_0x0153:
        r5 = r8.pageScroll(r6);
        if (r5 == 0) goto L_0x015a;
    L_0x0159:
        goto L_0x015c;
    L_0x015a:
        r5 = r1;
        goto L_0x015d;
    L_0x015c:
        r5 = r4;
    L_0x015d:
        r0 = r5;
        goto L_0x01a6;
    L_0x015f:
        r5 = r11.hasModifiers(r3);
        if (r5 == 0) goto L_0x01a6;
    L_0x0165:
        r5 = r8.resurrectSelectionIfNeeded();
        if (r5 != 0) goto L_0x0174;
    L_0x016b:
        r5 = r8.fullScroll(r6);
        if (r5 == 0) goto L_0x0172;
    L_0x0171:
        goto L_0x0174;
    L_0x0172:
        r5 = r1;
        goto L_0x0175;
    L_0x0174:
        r5 = r4;
    L_0x0175:
        r0 = r5;
        goto L_0x01a6;
    L_0x0177:
        r5 = r11.hasNoModifiers();
        if (r5 == 0) goto L_0x018f;
    L_0x017d:
        r5 = r8.resurrectSelectionIfNeeded();
        if (r5 != 0) goto L_0x018c;
    L_0x0183:
        r5 = r8.arrowScroll(r7);
        if (r5 == 0) goto L_0x018a;
    L_0x0189:
        goto L_0x018c;
    L_0x018a:
        r5 = r1;
        goto L_0x018d;
    L_0x018c:
        r5 = r4;
    L_0x018d:
        r0 = r5;
        goto L_0x01a6;
    L_0x018f:
        r5 = r11.hasModifiers(r4);
        if (r5 == 0) goto L_0x01a6;
    L_0x0195:
        r5 = r8.resurrectSelectionIfNeeded();
        if (r5 != 0) goto L_0x01a4;
    L_0x019b:
        r5 = r8.arrowScroll(r6);
        if (r5 == 0) goto L_0x01a2;
    L_0x01a1:
        goto L_0x01a4;
    L_0x01a2:
        r5 = r1;
        goto L_0x01a5;
    L_0x01a4:
        r5 = r4;
    L_0x01a5:
        r0 = r5;
    L_0x01a6:
        if (r0 == 0) goto L_0x01a9;
    L_0x01a8:
        return r4;
    L_0x01a9:
        r5 = r8.sendToTextFilter(r9, r10, r11);
        if (r5 == 0) goto L_0x01b0;
    L_0x01af:
        return r4;
    L_0x01b0:
        if (r2 == 0) goto L_0x01c1;
    L_0x01b2:
        if (r2 == r4) goto L_0x01bc;
    L_0x01b4:
        if (r2 == r3) goto L_0x01b7;
    L_0x01b6:
        return r1;
    L_0x01b7:
        r1 = super.onKeyMultiple(r9, r10, r11);
        return r1;
    L_0x01bc:
        r1 = super.onKeyUp(r9, r11);
        return r1;
    L_0x01c1:
        r1 = super.onKeyDown(r9, r11);
        return r1;
    L_0x01c6:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.ListView.commonKey(int, int, android.view.KeyEvent):boolean");
    }

    /* Access modifiers changed, original: 0000 */
    public boolean pageScroll(int direction) {
        int nextPage;
        boolean down;
        if (direction == 33) {
            nextPage = Math.max(0, (this.mSelectedPosition - getChildCount()) - 1);
            down = false;
        } else if (direction != 130) {
            return false;
        } else {
            nextPage = Math.min(this.mItemCount - 1, (this.mSelectedPosition + getChildCount()) - 1);
            down = true;
        }
        if (nextPage >= 0) {
            int position = lookForSelectablePositionAfter(this.mSelectedPosition, nextPage, down);
            if (position >= 0) {
                this.mLayoutMode = 4;
                this.mSpecificTop = this.mPaddingTop + getVerticalFadingEdgeLength();
                if (down && position > this.mItemCount - getChildCount()) {
                    this.mLayoutMode = 3;
                }
                if (!down && position < getChildCount()) {
                    this.mLayoutMode = 1;
                }
                setSelectionInt(position);
                invokeOnItemScrollListener();
                if (!awakenScrollBars()) {
                    invalidate();
                }
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean fullScroll(int direction) {
        boolean moved = false;
        int position;
        if (direction == 33) {
            if (this.mSelectedPosition != 0) {
                position = lookForSelectablePositionAfter(this.mSelectedPosition, 0, true);
                if (position >= 0) {
                    this.mLayoutMode = 1;
                    setSelectionInt(position);
                    invokeOnItemScrollListener();
                }
                moved = true;
            }
        } else if (direction == 130) {
            int lastItem = this.mItemCount - 1;
            if (this.mSelectedPosition < lastItem) {
                position = lookForSelectablePositionAfter(this.mSelectedPosition, lastItem, false);
                if (position >= 0) {
                    this.mLayoutMode = 3;
                    setSelectionInt(position);
                    invokeOnItemScrollListener();
                }
                moved = true;
            }
        }
        if (moved && !awakenScrollBars()) {
            awakenScrollBars();
            invalidate();
        }
        return moved;
    }

    private boolean handleHorizontalFocusWithinListItem(int direction) {
        if (direction == 17 || direction == 66) {
            int numChildren = getChildCount();
            if (this.mItemsCanFocus && numChildren > 0 && this.mSelectedPosition != -1) {
                View selectedView = getSelectedView();
                if (selectedView != null && selectedView.hasFocus() && (selectedView instanceof ViewGroup)) {
                    View currentFocus = selectedView.findFocus();
                    View nextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup) selectedView, currentFocus, direction);
                    if (nextFocus != null) {
                        Rect focusedRect = this.mTempRect;
                        if (currentFocus != null) {
                            currentFocus.getFocusedRect(focusedRect);
                            offsetDescendantRectToMyCoords(currentFocus, focusedRect);
                            offsetRectIntoDescendantCoords(nextFocus, focusedRect);
                        } else {
                            focusedRect = null;
                        }
                        if (nextFocus.requestFocus(direction, focusedRect)) {
                            return true;
                        }
                    }
                    View globalNextFocus = FocusFinder.getInstance().findNextFocus((ViewGroup) getRootView(), currentFocus, direction);
                    if (globalNextFocus != null) {
                        return isViewAncestorOf(globalNextFocus, this);
                    }
                }
            }
            return false;
        }
        throw new IllegalArgumentException("direction must be one of {View.FOCUS_LEFT, View.FOCUS_RIGHT}");
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean arrowScroll(int direction) {
        try {
            this.mInLayout = true;
            boolean handled = arrowScrollImpl(direction);
            if (handled) {
                playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            }
            this.mInLayout = false;
            return handled;
        } catch (Throwable th) {
            this.mInLayout = false;
        }
    }

    private final int nextSelectedPositionForDirection(View selectedView, int selectedPos, int direction) {
        int nextSelected;
        boolean z = true;
        int listBottom;
        if (direction == 130) {
            listBottom = getHeight() - this.mListPadding.bottom;
            if (selectedView == null || selectedView.getBottom() > listBottom) {
                return -1;
            }
            if (selectedPos == -1 || selectedPos < this.mFirstPosition) {
                nextSelected = this.mFirstPosition;
            } else {
                nextSelected = selectedPos + 1;
            }
        } else {
            listBottom = this.mListPadding.top;
            if (selectedView == null || selectedView.getTop() < listBottom) {
                return -1;
            }
            int i;
            nextSelected = (this.mFirstPosition + getChildCount()) - 1;
            if (selectedPos == -1 || selectedPos > nextSelected) {
                i = nextSelected;
            } else {
                i = selectedPos - 1;
            }
            nextSelected = i;
        }
        if (nextSelected < 0 || nextSelected >= this.mAdapter.getCount()) {
            return -1;
        }
        if (direction != 130) {
            z = false;
        }
        return lookForSelectablePosition(nextSelected, z);
    }

    private boolean arrowScrollImpl(int direction) {
        if (getChildCount() <= 0) {
            return false;
        }
        View focused;
        View selectedView = getSelectedView();
        int selectedPos = this.mSelectedPosition;
        int nextSelectedPosition = nextSelectedPositionForDirection(selectedView, selectedPos, direction);
        int amountToScroll = amountToScroll(direction, nextSelectedPosition);
        ArrowScrollFocusResult focusResult = this.mItemsCanFocus ? arrowScrollFocused(direction) : null;
        if (focusResult != null) {
            nextSelectedPosition = focusResult.getSelectedPosition();
            amountToScroll = focusResult.getAmountToScroll();
        }
        boolean needToRedraw = focusResult != null;
        if (nextSelectedPosition != -1) {
            handleNewSelectionChange(selectedView, direction, nextSelectedPosition, focusResult != null);
            setSelectedPositionInt(nextSelectedPosition);
            setNextSelectedPositionInt(nextSelectedPosition);
            selectedView = getSelectedView();
            selectedPos = nextSelectedPosition;
            if (this.mItemsCanFocus && focusResult == null) {
                focused = getFocusedChild();
                if (focused != null) {
                    focused.clearFocus();
                }
            }
            needToRedraw = true;
            checkSelectionChanged();
        }
        if (amountToScroll > 0) {
            scrollListItemsBy(direction == 33 ? amountToScroll : -amountToScroll);
            needToRedraw = true;
        }
        if (this.mItemsCanFocus && focusResult == null && selectedView != null && selectedView.hasFocus()) {
            focused = selectedView.findFocus();
            if (focused != null && (!isViewAncestorOf(focused, this) || distanceToView(focused) > 0)) {
                focused.clearFocus();
            }
        }
        if (!(nextSelectedPosition != -1 || selectedView == null || isViewAncestorOf(selectedView, this))) {
            selectedView = null;
            hideSelector();
            this.mResurrectToPosition = -1;
        }
        if (!needToRedraw) {
            return false;
        }
        if (selectedView != null) {
            positionSelectorLikeFocus(selectedPos, selectedView);
            this.mSelectedTop = selectedView.getTop();
        }
        if (!awakenScrollBars()) {
            invalidate();
        }
        invokeOnItemScrollListener();
        return true;
    }

    private void handleNewSelectionChange(View selectedView, int direction, int newSelectedPosition, boolean newFocusAssigned) {
        if (newSelectedPosition != -1) {
            int topViewIndex;
            int bottomViewIndex;
            View topView;
            View bottomView;
            boolean topSelected = false;
            int selectedIndex = this.mSelectedPosition - this.mFirstPosition;
            int nextSelectedIndex = newSelectedPosition - this.mFirstPosition;
            if (direction == 33) {
                topViewIndex = nextSelectedIndex;
                bottomViewIndex = selectedIndex;
                topView = getChildAt(topViewIndex);
                bottomView = selectedView;
                topSelected = true;
            } else {
                topViewIndex = selectedIndex;
                bottomViewIndex = nextSelectedIndex;
                topView = selectedView;
                bottomView = getChildAt(bottomViewIndex);
            }
            int numChildren = getChildCount();
            boolean z = true;
            if (topView != null) {
                boolean z2 = !newFocusAssigned && topSelected;
                topView.setSelected(z2);
                measureAndAdjustDown(topView, topViewIndex, numChildren);
            }
            if (bottomView != null) {
                if (newFocusAssigned || topSelected) {
                    z = false;
                }
                bottomView.setSelected(z);
                measureAndAdjustDown(bottomView, bottomViewIndex, numChildren);
                return;
            }
            return;
        }
        throw new IllegalArgumentException("newSelectedPosition needs to be valid");
    }

    private void measureAndAdjustDown(View child, int childIndex, int numChildren) {
        int oldHeight = child.getHeight();
        measureItem(child);
        if (child.getMeasuredHeight() != oldHeight) {
            relayoutMeasuredItem(child);
            int heightDelta = child.getMeasuredHeight() - oldHeight;
            for (int i = childIndex + 1; i < numChildren; i++) {
                getChildAt(i).offsetTopAndBottom(heightDelta);
            }
        }
    }

    private void measureItem(View child) {
        int childHeightSpec;
        LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new LayoutParams(-1, -2);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(this.mWidthMeasureSpec, this.mListPadding.left + this.mListPadding.right, p.width);
        int lpHeight = p.height;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, 1073741824);
        } else {
            childHeightSpec = MeasureSpec.makeSafeMeasureSpec(getMeasuredHeight(), 0);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private void relayoutMeasuredItem(View child) {
        int w = child.getMeasuredWidth();
        int h = child.getMeasuredHeight();
        int childLeft = this.mListPadding.left;
        int childRight = childLeft + w;
        int childTop = child.getTop();
        child.layout(childLeft, childTop, childRight, childTop + h);
    }

    private int getArrowScrollPreviewLength() {
        return Math.max(2, getVerticalFadingEdgeLength());
    }

    private int amountToScroll(int direction, int nextSelectedPosition) {
        int listBottom = getHeight() - this.mListPadding.bottom;
        int listTop = this.mListPadding.top;
        int numChildren = getChildCount();
        int indexToMakeVisible;
        int positionToMakeVisible;
        View viewToMakeVisible;
        int goalBottom;
        int amountToScroll;
        if (direction == 130) {
            indexToMakeVisible = numChildren - 1;
            if (nextSelectedPosition != -1) {
                indexToMakeVisible = nextSelectedPosition - this.mFirstPosition;
            }
            while (numChildren <= indexToMakeVisible) {
                addViewBelow(getChildAt(numChildren - 1), (this.mFirstPosition + numChildren) - 1);
                numChildren++;
            }
            positionToMakeVisible = this.mFirstPosition + indexToMakeVisible;
            viewToMakeVisible = getChildAt(indexToMakeVisible);
            goalBottom = listBottom;
            if (positionToMakeVisible < this.mItemCount - 1) {
                goalBottom -= getArrowScrollPreviewLength();
            }
            if (viewToMakeVisible.getBottom() <= goalBottom) {
                return 0;
            }
            if (nextSelectedPosition != -1 && goalBottom - viewToMakeVisible.getTop() >= getMaxScrollAmount()) {
                return 0;
            }
            amountToScroll = viewToMakeVisible.getBottom() - goalBottom;
            if (this.mFirstPosition + numChildren == this.mItemCount) {
                amountToScroll = Math.min(amountToScroll, getChildAt(numChildren - 1).getBottom() - listBottom);
            }
            return Math.min(amountToScroll, getMaxScrollAmount());
        }
        indexToMakeVisible = 0;
        if (nextSelectedPosition != -1) {
            indexToMakeVisible = nextSelectedPosition - this.mFirstPosition;
        }
        while (indexToMakeVisible < 0) {
            addViewAbove(getChildAt(0), this.mFirstPosition);
            this.mFirstPosition--;
            indexToMakeVisible = nextSelectedPosition - this.mFirstPosition;
        }
        positionToMakeVisible = this.mFirstPosition + indexToMakeVisible;
        viewToMakeVisible = getChildAt(indexToMakeVisible);
        goalBottom = listTop;
        if (positionToMakeVisible > 0) {
            goalBottom += getArrowScrollPreviewLength();
        }
        if (viewToMakeVisible.getTop() >= goalBottom) {
            return 0;
        }
        if (nextSelectedPosition != -1 && viewToMakeVisible.getBottom() - goalBottom >= getMaxScrollAmount()) {
            return 0;
        }
        amountToScroll = goalBottom - viewToMakeVisible.getTop();
        if (this.mFirstPosition == 0) {
            amountToScroll = Math.min(amountToScroll, listTop - getChildAt(0).getTop());
        }
        return Math.min(amountToScroll, getMaxScrollAmount());
    }

    private int lookForSelectablePositionOnScreen(int direction) {
        int firstPosition = this.mFirstPosition;
        int startPos;
        int lastVisiblePos;
        ListAdapter adapter;
        int pos;
        if (direction == 130) {
            if (this.mSelectedPosition != -1) {
                startPos = this.mSelectedPosition + 1;
            } else {
                startPos = firstPosition;
            }
            if (startPos >= this.mAdapter.getCount()) {
                return -1;
            }
            if (startPos < firstPosition) {
                startPos = firstPosition;
            }
            lastVisiblePos = getLastVisiblePosition();
            adapter = getAdapter();
            pos = startPos;
            while (pos <= lastVisiblePos) {
                if (adapter.isEnabled(pos) && getChildAt(pos - firstPosition).getVisibility() == 0) {
                    return pos;
                }
                pos++;
            }
        } else {
            startPos = (getChildCount() + firstPosition) - 1;
            if (this.mSelectedPosition != -1) {
                lastVisiblePos = this.mSelectedPosition - 1;
            } else {
                lastVisiblePos = (getChildCount() + firstPosition) - 1;
            }
            if (lastVisiblePos < 0 || lastVisiblePos >= this.mAdapter.getCount()) {
                return -1;
            }
            if (lastVisiblePos > startPos) {
                lastVisiblePos = startPos;
            }
            adapter = getAdapter();
            pos = lastVisiblePos;
            while (pos >= firstPosition) {
                if (adapter.isEnabled(pos) && getChildAt(pos - firstPosition).getVisibility() == 0) {
                    return pos;
                }
                pos--;
            }
        }
        return -1;
    }

    private ArrowScrollFocusResult arrowScrollFocused(int direction) {
        int listTop;
        int ySearchPoint;
        View newFocus;
        View selectedView = getSelectedView();
        if (selectedView == null || !selectedView.hasFocus()) {
            boolean topFadingEdgeShowing = true;
            if (direction == 130) {
                if (this.mFirstPosition <= 0) {
                    topFadingEdgeShowing = false;
                }
                listTop = this.mListPadding.top + (topFadingEdgeShowing ? getArrowScrollPreviewLength() : 0);
                if (selectedView == null || selectedView.getTop() <= listTop) {
                    ySearchPoint = listTop;
                } else {
                    ySearchPoint = selectedView.getTop();
                }
                this.mTempRect.set(0, ySearchPoint, 0, ySearchPoint);
            } else {
                if ((this.mFirstPosition + getChildCount()) - 1 >= this.mItemCount) {
                    topFadingEdgeShowing = false;
                }
                listTop = (getHeight() - this.mListPadding.bottom) - (topFadingEdgeShowing ? getArrowScrollPreviewLength() : 0);
                if (selectedView == null || selectedView.getBottom() >= listTop) {
                    ySearchPoint = listTop;
                } else {
                    ySearchPoint = selectedView.getBottom();
                }
                this.mTempRect.set(0, ySearchPoint, 0, ySearchPoint);
            }
            newFocus = FocusFinder.getInstance().findNextFocusFromRect(this, this.mTempRect, direction);
        } else {
            newFocus = FocusFinder.getInstance().findNextFocus(this, selectedView.findFocus(), direction);
        }
        if (newFocus != null) {
            listTop = positionOfNewFocus(newFocus);
            if (!(this.mSelectedPosition == -1 || listTop == this.mSelectedPosition)) {
                ySearchPoint = lookForSelectablePositionOnScreen(direction);
                if (ySearchPoint != -1 && ((direction == 130 && ySearchPoint < listTop) || (direction == 33 && ySearchPoint > listTop))) {
                    return null;
                }
            }
            int focusScroll = amountToScrollToNewFocus(direction, newFocus, listTop);
            ySearchPoint = getMaxScrollAmount();
            if (focusScroll < ySearchPoint) {
                newFocus.requestFocus(direction);
                this.mArrowScrollFocusResult.populate(listTop, focusScroll);
                return this.mArrowScrollFocusResult;
            } else if (distanceToView(newFocus) < ySearchPoint) {
                newFocus.requestFocus(direction);
                this.mArrowScrollFocusResult.populate(listTop, ySearchPoint);
                return this.mArrowScrollFocusResult;
            }
        }
        return null;
    }

    private int positionOfNewFocus(View newFocus) {
        int numChildren = getChildCount();
        for (int i = 0; i < numChildren; i++) {
            if (isViewAncestorOf(newFocus, getChildAt(i))) {
                return this.mFirstPosition + i;
            }
        }
        throw new IllegalArgumentException("newFocus is not a child of any of the children of the list!");
    }

    private boolean isViewAncestorOf(View child, View parent) {
        boolean z = true;
        if (child == parent) {
            return true;
        }
        ViewParent theParent = child.getParent();
        if (!((theParent instanceof ViewGroup) && isViewAncestorOf((View) theParent, parent))) {
            z = false;
        }
        return z;
    }

    private int amountToScrollToNewFocus(int direction, View newFocus, int positionOfNewFocus) {
        newFocus.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(newFocus, this.mTempRect);
        int amountToScroll;
        if (direction != 33) {
            int listBottom = getHeight() - this.mListPadding.bottom;
            if (this.mTempRect.bottom <= listBottom) {
                return 0;
            }
            amountToScroll = this.mTempRect.bottom - listBottom;
            if (positionOfNewFocus < this.mItemCount - 1) {
                return amountToScroll + getArrowScrollPreviewLength();
            }
            return amountToScroll;
        } else if (this.mTempRect.top >= this.mListPadding.top) {
            return 0;
        } else {
            amountToScroll = this.mListPadding.top - this.mTempRect.top;
            if (positionOfNewFocus > 0) {
                return amountToScroll + getArrowScrollPreviewLength();
            }
            return amountToScroll;
        }
    }

    private int distanceToView(View descendant) {
        descendant.getDrawingRect(this.mTempRect);
        offsetDescendantRectToMyCoords(descendant, this.mTempRect);
        int listBottom = (this.mBottom - this.mTop) - this.mListPadding.bottom;
        if (this.mTempRect.bottom < this.mListPadding.top) {
            return this.mListPadding.top - this.mTempRect.bottom;
        }
        if (this.mTempRect.top > listBottom) {
            return this.mTempRect.top - listBottom;
        }
        return 0;
    }

    @UnsupportedAppUsage
    private void scrollListItemsBy(int amount) {
        offsetChildrenTopAndBottom(amount);
        int listBottom = getHeight() - this.mListPadding.bottom;
        int listTop = this.mListPadding.top;
        RecycleBin recycleBin = this.mRecycler;
        int numChildren;
        View last;
        if (amount < 0) {
            numChildren = getChildCount();
            last = getChildAt(numChildren - 1);
            while (last.getBottom() < listBottom) {
                int lastVisiblePosition = (this.mFirstPosition + numChildren) - 1;
                if (lastVisiblePosition >= this.mItemCount - 1) {
                    break;
                }
                last = addViewBelow(last, lastVisiblePosition);
                numChildren++;
            }
            if (last.getBottom() < listBottom) {
                offsetChildrenTopAndBottom(listBottom - last.getBottom());
            }
            View first = getChildAt(0);
            while (first.getBottom() < listTop) {
                if (recycleBin.shouldRecycleViewType(((AbsListView.LayoutParams) first.getLayoutParams()).viewType)) {
                    recycleBin.addScrapView(first, this.mFirstPosition);
                }
                detachViewFromParent(first);
                first = getChildAt(0);
                this.mFirstPosition++;
            }
        } else {
            View first2 = getChildAt(0);
            while (first2.getTop() > listTop && this.mFirstPosition > 0) {
                first2 = addViewAbove(first2, this.mFirstPosition);
                this.mFirstPosition--;
            }
            if (first2.getTop() > listTop) {
                offsetChildrenTopAndBottom(listTop - first2.getTop());
            }
            numChildren = getChildCount() - 1;
            last = getChildAt(numChildren);
            while (last.getTop() > listBottom) {
                if (recycleBin.shouldRecycleViewType(((AbsListView.LayoutParams) last.getLayoutParams()).viewType)) {
                    recycleBin.addScrapView(last, this.mFirstPosition + numChildren);
                }
                detachViewFromParent(last);
                numChildren--;
                last = getChildAt(numChildren);
            }
        }
        recycleBin.fullyDetachScrapViews();
        removeUnusedFixedViews(this.mHeaderViewInfos);
        removeUnusedFixedViews(this.mFooterViewInfos);
    }

    private View addViewAbove(View theView, int position) {
        int abovePosition = position - 1;
        View view = obtainView(abovePosition, this.mIsScrap);
        setupChild(view, abovePosition, theView.getTop() - this.mDividerHeight, false, this.mListPadding.left, false, this.mIsScrap[0]);
        return view;
    }

    private View addViewBelow(View theView, int position) {
        int belowPosition = position + 1;
        View view = obtainView(belowPosition, this.mIsScrap);
        setupChild(view, belowPosition, theView.getBottom() + this.mDividerHeight, true, this.mListPadding.left, false, this.mIsScrap[0]);
        return view;
    }

    public void setItemsCanFocus(boolean itemsCanFocus) {
        this.mItemsCanFocus = itemsCanFocus;
        if (!itemsCanFocus) {
            setDescendantFocusability(393216);
        }
    }

    public boolean getItemsCanFocus() {
        return this.mItemsCanFocus;
    }

    public boolean isOpaque() {
        boolean retValue = (this.mCachingActive && this.mIsCacheColorOpaque && this.mDividerIsOpaque && hasOpaqueScrollbars()) || super.isOpaque();
        if (retValue) {
            int listTop = this.mListPadding != null ? this.mListPadding.top : this.mPaddingTop;
            View first = getChildAt(0);
            if (first == null || first.getTop() > listTop) {
                return false;
            }
            int listBottom = getHeight() - (this.mListPadding != null ? this.mListPadding.bottom : this.mPaddingBottom);
            View last = getChildAt(getChildCount() - 1);
            if (last == null || last.getBottom() < listBottom) {
                return false;
            }
        }
        return retValue;
    }

    public void setCacheColorHint(int color) {
        boolean opaque = (color >>> 24) == 255;
        this.mIsCacheColorOpaque = opaque;
        if (opaque) {
            if (this.mDividerPaint == null) {
                this.mDividerPaint = new Paint();
            }
            this.mDividerPaint.setColor(color);
        }
        super.setCacheColorHint(color);
    }

    /* Access modifiers changed, original: 0000 */
    public void drawOverscrollHeader(Canvas canvas, Drawable drawable, Rect bounds) {
        int height = drawable.getMinimumHeight();
        canvas.save();
        canvas.clipRect(bounds);
        if (bounds.bottom - bounds.top < height) {
            bounds.top = bounds.bottom - height;
        }
        drawable.setBounds(bounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    /* Access modifiers changed, original: 0000 */
    public void drawOverscrollFooter(Canvas canvas, Drawable drawable, Rect bounds) {
        int height = drawable.getMinimumHeight();
        canvas.save();
        canvas.clipRect(bounds);
        if (bounds.bottom - bounds.top < height) {
            bounds.bottom = bounds.top + height;
        }
        drawable.setBounds(bounds);
        drawable.draw(canvas);
        canvas.restore();
    }

    /* Access modifiers changed, original: protected */
    public void dispatchDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        if (this.mCachingStarted) {
            this.mCachingActive = true;
        }
        int dividerHeight = this.mDividerHeight;
        Drawable overscrollHeader = this.mOverScrollHeader;
        Drawable overscrollFooter = this.mOverScrollFooter;
        boolean drawOverscrollHeader = overscrollHeader != null;
        boolean drawOverscrollFooter = overscrollFooter != null;
        boolean drawDividers = dividerHeight > 0 && this.mDivider != null;
        boolean drawOverscrollFooter2;
        Drawable overscrollHeader2;
        boolean drawOverscrollHeader2;
        boolean drawDividers2;
        if (drawDividers || drawOverscrollHeader || drawOverscrollFooter) {
            int itemCount;
            ListAdapter adapter;
            int effectivePaddingBottom;
            Rect bounds = this.mTempRect;
            bounds.left = this.mPaddingLeft;
            bounds.right = (this.mRight - this.mLeft) - this.mPaddingRight;
            int count = getChildCount();
            int headerCount = getHeaderViewsCount();
            int itemCount2 = this.mItemCount;
            int footerLimit = itemCount2 - this.mFooterViewInfos.size();
            boolean headerDividers = this.mHeaderDividersEnabled;
            boolean footerDividers = this.mFooterDividersEnabled;
            int first = this.mFirstPosition;
            Drawable overscrollFooter2 = overscrollFooter;
            boolean areAllItemsSelectable = this.mAreAllItemsSelectable;
            ListAdapter adapter2 = this.mAdapter;
            boolean fillForMissingDividers = isOpaque() && !super.isOpaque();
            if (fillForMissingDividers) {
                itemCount = itemCount2;
                if (this.mDividerPaint == null && this.mIsCacheColorOpaque) {
                    this.mDividerPaint = new Paint();
                    adapter = adapter2;
                    this.mDividerPaint.setColor(getCacheColorHint());
                } else {
                    adapter = adapter2;
                }
            } else {
                adapter = adapter2;
                itemCount = itemCount2;
            }
            int effectivePaddingTop = 0;
            Paint paint = this.mDividerPaint;
            if ((this.mGroupFlags & 34) == 34) {
                itemCount2 = this.mListPadding.top;
                effectivePaddingBottom = this.mListPadding.bottom;
            } else {
                effectivePaddingBottom = 0;
                itemCount2 = effectivePaddingTop;
            }
            int effectivePaddingTop2 = itemCount2;
            drawOverscrollFooter2 = drawOverscrollFooter;
            boolean listBottom = ((this.mBottom - this.mTop) - effectivePaddingBottom) + this.mScrollY;
            boolean listBottom2;
            Drawable overscrollFooter3;
            ListAdapter adapter3;
            Paint paint2;
            if (this.mStackFromBottom) {
                Drawable overscrollHeader3;
                int first2;
                Drawable overscrollFooter4;
                int start;
                overscrollHeader2 = overscrollHeader;
                drawOverscrollHeader2 = drawOverscrollHeader;
                drawDividers2 = drawDividers;
                listBottom2 = listBottom;
                overscrollFooter3 = overscrollFooter2;
                itemCount2 = itemCount;
                adapter3 = adapter;
                paint2 = paint;
                effectivePaddingBottom = this.mScrollY;
                if (count <= 0 || !drawOverscrollHeader2) {
                    overscrollHeader3 = overscrollHeader2;
                } else {
                    bounds.top = effectivePaddingBottom;
                    bounds.bottom = getChildAt(0).getTop();
                    overscrollHeader3 = overscrollHeader2;
                    drawOverscrollHeader(canvas2, overscrollHeader3, bounds);
                }
                int i = drawOverscrollHeader2 ? 1 : 0;
                itemCount = i;
                overscrollHeader2 = overscrollHeader3;
                int i2 = i;
                while (i2 < count) {
                    boolean footerDividers2;
                    i = itemCount2;
                    itemCount2 = first + i2;
                    boolean isHeader = itemCount2 < headerCount;
                    boolean isFooter = itemCount2 >= footerLimit;
                    if ((headerDividers || !isHeader) && (footerDividers || !isFooter)) {
                        first2 = first;
                        first = getChildAt(i2).getTop();
                        if (drawDividers2) {
                            overscrollFooter4 = overscrollFooter3;
                            overscrollFooter3 = effectivePaddingTop2;
                            if (first > overscrollFooter3) {
                                effectivePaddingTop2 = overscrollFooter3;
                                overscrollFooter3 = itemCount;
                                itemCount = i2 == overscrollFooter3 ? 1 : 0;
                                start = overscrollFooter3;
                                overscrollFooter3 = itemCount2 - 1;
                                if (!adapter3.isEnabled(itemCount2)) {
                                    footerDividers2 = footerDividers;
                                } else if (!headerDividers && (isHeader || overscrollFooter3 < headerCount)) {
                                    footerDividers2 = footerDividers;
                                } else if (itemCount != 0 || (adapter3.isEnabled(overscrollFooter3) && (footerDividers || (!isFooter && overscrollFooter3 < footerLimit)))) {
                                    footerDividers2 = footerDividers;
                                    bounds.top = first - dividerHeight;
                                    bounds.bottom = first;
                                    drawDivider(canvas2, bounds, i2 - 1);
                                } else {
                                    footerDividers2 = footerDividers;
                                }
                                if (fillForMissingDividers) {
                                    bounds.top = first - dividerHeight;
                                    bounds.bottom = first;
                                    canvas2.drawRect(bounds, paint2);
                                }
                            } else {
                                footerDividers2 = footerDividers;
                                effectivePaddingTop2 = overscrollFooter3;
                                start = itemCount;
                            }
                        } else {
                            footerDividers2 = footerDividers;
                            overscrollFooter4 = overscrollFooter3;
                            start = itemCount;
                        }
                    } else {
                        footerDividers2 = footerDividers;
                        first2 = first;
                        overscrollFooter4 = overscrollFooter3;
                        start = itemCount;
                    }
                    i2++;
                    itemCount2 = i;
                    first = first2;
                    overscrollFooter3 = overscrollFooter4;
                    itemCount = start;
                    footerDividers = footerDividers2;
                }
                first2 = first;
                overscrollFooter4 = overscrollFooter3;
                i = itemCount2;
                start = itemCount;
                if (count <= 0 || effectivePaddingBottom <= 0) {
                    listBottom = listBottom2;
                } else if (drawOverscrollFooter2) {
                    int absListBottom = this.mBottom;
                    bounds.top = absListBottom;
                    bounds.bottom = absListBottom + effectivePaddingBottom;
                    drawOverscrollFooter(canvas2, overscrollFooter4, bounds);
                } else {
                    if (drawDividers2) {
                        listBottom = listBottom2;
                        bounds.top = listBottom;
                        bounds.bottom = listBottom + dividerHeight;
                        drawDivider(canvas2, bounds, -1);
                    }
                }
            } else {
                boolean bottom;
                effectivePaddingBottom = this.mScrollY;
                if (count <= 0 || effectivePaddingBottom >= 0) {
                    bottom = false;
                } else if (drawOverscrollHeader) {
                    bottom = false;
                    bounds.bottom = 0;
                    bounds.top = effectivePaddingBottom;
                    drawOverscrollHeader(canvas2, overscrollHeader, bounds);
                } else {
                    bottom = false;
                    if (drawDividers) {
                        bounds.bottom = 0;
                        bounds.top = -dividerHeight;
                        drawDivider(canvas2, bounds, -1);
                    }
                }
                int i3 = 0;
                boolean bottom2 = bottom;
                while (i3 < count) {
                    overscrollHeader2 = overscrollHeader;
                    overscrollHeader = first + i3;
                    boolean isHeader2 = overscrollHeader < headerCount;
                    boolean isFooter2 = overscrollHeader >= footerLimit;
                    if ((headerDividers || !isHeader2) && (footerDividers || !isFooter2)) {
                        bottom2 = getChildAt(i3).getBottom();
                        drawOverscrollHeader2 = drawOverscrollHeader;
                        drawOverscrollHeader = i3 == count + -1;
                        Drawable adapter4;
                        if (!drawDividers || bottom2 >= listBottom) {
                            drawDividers2 = drawDividers;
                            listBottom2 = listBottom;
                            adapter3 = adapter;
                            adapter4 = overscrollHeader;
                            overscrollHeader = paint;
                        } else if (drawOverscrollFooter2 && drawOverscrollHeader) {
                            drawDividers2 = drawDividers;
                            listBottom2 = listBottom;
                            adapter3 = adapter;
                            overscrollHeader = paint;
                        } else {
                            listBottom2 = listBottom;
                            itemCount2 = overscrollHeader + 1;
                            drawDividers2 = drawDividers;
                            adapter3 = adapter;
                            if (!adapter3.isEnabled(overscrollHeader)) {
                                adapter4 = overscrollHeader;
                            } else if (!headerDividers && (isHeader2 || itemCount2 < headerCount)) {
                                adapter4 = overscrollHeader;
                            } else if (drawOverscrollHeader || (adapter3.isEnabled(itemCount2) && (footerDividers || (!isFooter2 && itemCount2 < footerLimit)))) {
                                bounds.top = bottom2;
                                int itemIndex = overscrollHeader;
                                bounds.bottom = bottom2 + dividerHeight;
                                drawDivider(canvas2, bounds, i3);
                                overscrollHeader = paint;
                            } else {
                                adapter4 = overscrollHeader;
                            }
                            if (fillForMissingDividers) {
                                bounds.top = bottom2;
                                bounds.bottom = bottom2 + dividerHeight;
                                overscrollHeader = paint;
                                canvas2.drawRect(bounds, (Paint) overscrollHeader);
                            } else {
                                overscrollHeader = paint;
                            }
                        }
                    } else {
                        drawOverscrollHeader2 = drawOverscrollHeader;
                        drawDividers2 = drawDividers;
                        listBottom2 = listBottom;
                        adapter3 = adapter;
                        overscrollHeader = paint;
                    }
                    i3++;
                    Object paint3 = overscrollHeader;
                    adapter = adapter3;
                    overscrollHeader = overscrollHeader2;
                    drawOverscrollHeader = drawOverscrollHeader2;
                    listBottom = listBottom2;
                    drawDividers = drawDividers2;
                }
                drawOverscrollHeader2 = drawOverscrollHeader;
                drawDividers2 = drawDividers;
                listBottom2 = listBottom;
                drawDividers = adapter;
                paint2 = paint3;
                drawOverscrollHeader = this.mBottom + this.mScrollY;
                if (!drawOverscrollFooter2) {
                    overscrollFooter3 = overscrollFooter2;
                    itemCount2 = itemCount;
                } else if (first + count != itemCount || drawOverscrollHeader <= bottom2) {
                    overscrollFooter3 = overscrollFooter2;
                } else {
                    bounds.top = bottom2;
                    bounds.bottom = drawOverscrollHeader;
                    overscrollFooter3 = overscrollFooter2;
                    drawOverscrollFooter(canvas2, overscrollFooter3, bounds);
                }
                first = overscrollFooter3;
            }
        } else {
            overscrollHeader2 = overscrollHeader;
            Drawable drawable = overscrollFooter;
            drawOverscrollHeader2 = drawOverscrollHeader;
            drawOverscrollFooter2 = drawOverscrollFooter;
            drawDividers2 = drawDividers;
        }
        super.dispatchDraw(canvas);
    }

    /* Access modifiers changed, original: protected */
    public boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean more = super.drawChild(canvas, child, drawingTime);
        if (this.mCachingActive && child.mCachingFailed) {
            this.mCachingActive = false;
        }
        return more;
    }

    /* Access modifiers changed, original: 0000 */
    public void drawDivider(Canvas canvas, Rect bounds, int childIndex) {
        Drawable divider = this.mDivider;
        divider.setBounds(bounds);
        divider.draw(canvas);
    }

    public Drawable getDivider() {
        return this.mDivider;
    }

    public void setDivider(Drawable divider) {
        boolean z = false;
        if (divider != null) {
            this.mDividerHeight = divider.getIntrinsicHeight();
        } else {
            this.mDividerHeight = 0;
        }
        this.mDivider = divider;
        if (divider == null || divider.getOpacity() == -1) {
            z = true;
        }
        this.mDividerIsOpaque = z;
        requestLayout();
        invalidate();
    }

    public int getDividerHeight() {
        return this.mDividerHeight;
    }

    public void setDividerHeight(int height) {
        this.mDividerHeight = height;
        requestLayout();
        invalidate();
    }

    public void setHeaderDividersEnabled(boolean headerDividersEnabled) {
        this.mHeaderDividersEnabled = headerDividersEnabled;
        invalidate();
    }

    public boolean areHeaderDividersEnabled() {
        return this.mHeaderDividersEnabled;
    }

    public void setFooterDividersEnabled(boolean footerDividersEnabled) {
        this.mFooterDividersEnabled = footerDividersEnabled;
        invalidate();
    }

    public boolean areFooterDividersEnabled() {
        return this.mFooterDividersEnabled;
    }

    public void setOverscrollHeader(Drawable header) {
        this.mOverScrollHeader = header;
        if (this.mScrollY < 0) {
            invalidate();
        }
    }

    public Drawable getOverscrollHeader() {
        return this.mOverScrollHeader;
    }

    public void setOverscrollFooter(Drawable footer) {
        this.mOverScrollFooter = footer;
        invalidate();
    }

    public Drawable getOverscrollFooter() {
        return this.mOverScrollFooter;
    }

    /* Access modifiers changed, original: protected */
    public void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        ListAdapter adapter = this.mAdapter;
        int closetChildIndex = -1;
        int closestChildTop = 0;
        if (!(adapter == null || !gainFocus || previouslyFocusedRect == null)) {
            previouslyFocusedRect.offset(this.mScrollX, this.mScrollY);
            if (adapter.getCount() < getChildCount() + this.mFirstPosition) {
                this.mLayoutMode = 0;
                layoutChildren();
            }
            Rect otherRect = this.mTempRect;
            int minDistance = Integer.MAX_VALUE;
            int childCount = getChildCount();
            int firstPosition = this.mFirstPosition;
            for (int i = 0; i < childCount; i++) {
                if (adapter.isEnabled(firstPosition + i)) {
                    View other = getChildAt(i);
                    other.getDrawingRect(otherRect);
                    offsetDescendantRectToMyCoords(other, otherRect);
                    int distance = AbsListView.getDistance(previouslyFocusedRect, otherRect, direction);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closetChildIndex = i;
                        closestChildTop = other.getTop();
                    }
                }
            }
        }
        if (closetChildIndex >= 0) {
            setSelectionFromTop(this.mFirstPosition + closetChildIndex, closestChildTop);
        } else {
            requestLayout();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                addHeaderView(getChildAt(i));
            }
            removeAllViews();
        }
    }

    /* Access modifiers changed, original: protected */
    public <T extends View> T findViewTraversal(int id) {
        View v = super.findViewTraversal(id);
        if (v == null) {
            v = findViewInHeadersOrFooters(this.mHeaderViewInfos, id);
            if (v != null) {
                return v;
            }
            v = findViewInHeadersOrFooters(this.mFooterViewInfos, id);
            if (v != null) {
                return v;
            }
        }
        return v;
    }

    /* Access modifiers changed, original: 0000 */
    public View findViewInHeadersOrFooters(ArrayList<FixedViewInfo> where, int id) {
        if (where != null) {
            int len = where.size();
            for (int i = 0; i < len; i++) {
                View v = ((FixedViewInfo) where.get(i)).view;
                if (!v.isRootNamespace()) {
                    v = v.findViewById(id);
                    if (v != null) {
                        return v;
                    }
                }
            }
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public <T extends View> T findViewWithTagTraversal(Object tag) {
        View v = super.findViewWithTagTraversal(tag);
        if (v == null) {
            v = findViewWithTagInHeadersOrFooters(this.mHeaderViewInfos, tag);
            if (v != null) {
                return v;
            }
            v = findViewWithTagInHeadersOrFooters(this.mFooterViewInfos, tag);
            if (v != null) {
                return v;
            }
        }
        return v;
    }

    /* Access modifiers changed, original: 0000 */
    public View findViewWithTagInHeadersOrFooters(ArrayList<FixedViewInfo> where, Object tag) {
        if (where != null) {
            int len = where.size();
            for (int i = 0; i < len; i++) {
                View v = ((FixedViewInfo) where.get(i)).view;
                if (!v.isRootNamespace()) {
                    v = v.findViewWithTag(tag);
                    if (v != null) {
                        return v;
                    }
                }
            }
        }
        return null;
    }

    /* Access modifiers changed, original: protected */
    public <T extends View> T findViewByPredicateTraversal(Predicate<View> predicate, View childToSkip) {
        View v = super.findViewByPredicateTraversal(predicate, childToSkip);
        if (v == null) {
            v = findViewByPredicateInHeadersOrFooters(this.mHeaderViewInfos, predicate, childToSkip);
            if (v != null) {
                return v;
            }
            v = findViewByPredicateInHeadersOrFooters(this.mFooterViewInfos, predicate, childToSkip);
            if (v != null) {
                return v;
            }
        }
        return v;
    }

    /* Access modifiers changed, original: 0000 */
    public View findViewByPredicateInHeadersOrFooters(ArrayList<FixedViewInfo> where, Predicate<View> predicate, View childToSkip) {
        if (where != null) {
            int len = where.size();
            for (int i = 0; i < len; i++) {
                View v = ((FixedViewInfo) where.get(i)).view;
                if (!(v == childToSkip || v.isRootNamespace())) {
                    v = v.findViewByPredicate(predicate);
                    if (v != null) {
                        return v;
                    }
                }
            }
        }
        return null;
    }

    @Deprecated
    public long[] getCheckItemIds() {
        if (this.mAdapter != null && this.mAdapter.hasStableIds()) {
            return getCheckedItemIds();
        }
        if (this.mChoiceMode == 0 || this.mCheckStates == null || this.mAdapter == null) {
            return new long[0];
        }
        SparseBooleanArray states = this.mCheckStates;
        int count = states.size();
        long[] ids = new long[count];
        ListAdapter adapter = this.mAdapter;
        int checkedCount = 0;
        for (int i = 0; i < count; i++) {
            if (states.valueAt(i)) {
                int checkedCount2 = checkedCount + 1;
                ids[checkedCount] = adapter.getItemId(states.keyAt(i));
                checkedCount = checkedCount2;
            }
        }
        if (checkedCount == count) {
            return ids;
        }
        long[] result = new long[checkedCount];
        System.arraycopy(ids, 0, result, 0, checkedCount);
        return result;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public int getHeightForPosition(int position) {
        int height = super.getHeightForPosition(position);
        if (shouldAdjustHeightForDivider(position)) {
            return this.mDividerHeight + height;
        }
        return height;
    }

    private boolean shouldAdjustHeightForDivider(int itemIndex) {
        int i = itemIndex;
        int dividerHeight = this.mDividerHeight;
        Drawable overscrollHeader = this.mOverScrollHeader;
        Drawable overscrollFooter = this.mOverScrollFooter;
        boolean drawOverscrollHeader = overscrollHeader != null;
        boolean drawOverscrollFooter = overscrollFooter != null;
        boolean drawDividers = dividerHeight > 0 && this.mDivider != null;
        Drawable drawable;
        if (drawDividers) {
            boolean fillForMissingDividers = isOpaque() && !super.isOpaque();
            int itemCount = this.mItemCount;
            int headerCount = getHeaderViewsCount();
            int footerLimit = itemCount - this.mFooterViewInfos.size();
            boolean isHeader = i < headerCount;
            boolean isFooter = i >= footerLimit;
            boolean headerDividers = this.mHeaderDividersEnabled;
            boolean footerDividers = this.mFooterDividersEnabled;
            if ((headerDividers || !isHeader) && (footerDividers || !isFooter)) {
                dividerHeight = this.mAdapter;
                int start;
                boolean z;
                if (this.mStackFromBottom) {
                    start = drawOverscrollHeader ? 1 : 0;
                    overscrollHeader = i == start ? true : null;
                    if (overscrollHeader == null) {
                        start = i - 1;
                        if (!dividerHeight.isEnabled(i)) {
                            z = true;
                        } else if (!headerDividers && (isHeader || start < headerCount)) {
                            z = true;
                        } else if (overscrollHeader != null || (dividerHeight.isEnabled(start) && (footerDividers || (!isFooter && start < footerLimit)))) {
                            return true;
                        } else {
                            z = true;
                        }
                        if (fillForMissingDividers) {
                            return z;
                        }
                    }
                } else {
                    boolean isLastItem = i == itemCount + -1;
                    if (!(drawOverscrollFooter && isLastItem)) {
                        start = i + 1;
                        if (!dividerHeight.isEnabled(i)) {
                            z = true;
                        } else if (!headerDividers && (isHeader || start < headerCount)) {
                            z = true;
                        } else if (isLastItem || (dividerHeight.isEnabled(start) && (footerDividers || (!isFooter && start < footerLimit)))) {
                            return true;
                        } else {
                            z = true;
                        }
                        if (fillForMissingDividers) {
                            return z;
                        }
                    }
                }
            }
            int i2 = dividerHeight;
            drawable = overscrollHeader;
        } else {
            drawable = overscrollHeader;
        }
        return false;
    }

    public CharSequence getAccessibilityClassName() {
        return ListView.class.getName();
    }

    public void onInitializeAccessibilityNodeInfoInternal(AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        int rowsCount = getCount();
        info.setCollectionInfo(CollectionInfo.obtain(rowsCount, 1, false, getSelectionModeForAccessibility()));
        if (rowsCount > 0) {
            info.addAction(AccessibilityAction.ACTION_SCROLL_TO_POSITION);
        }
    }

    public boolean performAccessibilityActionInternal(int action, Bundle arguments) {
        if (super.performAccessibilityActionInternal(action, arguments)) {
            return true;
        }
        if (action == 16908343) {
            int row = arguments.getInt(AccessibilityNodeInfo.ACTION_ARGUMENT_ROW_INT, -1);
            int position = Math.min(row, getCount() - 1);
            if (row >= 0) {
                smoothScrollToPosition(position);
                return true;
            }
        }
        return false;
    }

    public void onInitializeAccessibilityNodeInfoForItem(View view, int position, AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoForItem(view, position, info);
        AbsListView.LayoutParams lp = (AbsListView.LayoutParams) view.getLayoutParams();
        boolean z = lp != null && lp.viewType == -2;
        info.setCollectionItemInfo(CollectionItemInfo.obtain(position, 1, 0, 1, z, isItemChecked(position)));
    }

    /* Access modifiers changed, original: protected */
    public void encodeProperties(ViewHierarchyEncoder encoder) {
        super.encodeProperties(encoder);
        encoder.addProperty("recycleOnMeasure", recycleOnMeasure());
    }

    /* Access modifiers changed, original: protected */
    public HeaderViewListAdapter wrapHeaderListAdapterInternal(ArrayList<FixedViewInfo> headerViewInfos, ArrayList<FixedViewInfo> footerViewInfos, ListAdapter adapter) {
        return new HeaderViewListAdapter(headerViewInfos, footerViewInfos, adapter);
    }

    /* Access modifiers changed, original: protected */
    public void wrapHeaderListAdapterInternal() {
        this.mAdapter = wrapHeaderListAdapterInternal(this.mHeaderViewInfos, this.mFooterViewInfos, this.mAdapter);
    }

    /* Access modifiers changed, original: protected */
    public void dispatchDataSetObserverOnChangedInternal() {
        if (this.mDataSetObserver != null) {
            this.mDataSetObserver.onChanged();
        }
    }

    static {
        Extension.get().bindOriginal(new Interface() {
            public void layoutChildren(ListView listView) {
                listView.originalLayoutChildren();
            }

            public void fillGap(ListView listView, boolean b) {
                listView.originalFillGap(b);
            }

            public void init(ListView listView, Context context, AttributeSet attributeSet, int i, int i1) {
            }
        });
    }
}
