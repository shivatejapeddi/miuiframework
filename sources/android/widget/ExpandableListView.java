package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.BaseSavedState;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListConnector.PositionMetadata;
import com.android.internal.R;
import java.util.ArrayList;

public class ExpandableListView extends ListView {
    public static final int CHILD_INDICATOR_INHERIT = -1;
    private static final int[] CHILD_LAST_STATE_SET = new int[]{16842918};
    private static final int[] EMPTY_STATE_SET = new int[0];
    private static final int[] GROUP_EMPTY_STATE_SET = new int[]{16842921};
    private static final int[] GROUP_EXPANDED_EMPTY_STATE_SET = new int[]{16842920, 16842921};
    private static final int[] GROUP_EXPANDED_STATE_SET = new int[]{16842920};
    @UnsupportedAppUsage
    private static final int[][] GROUP_STATE_SETS = new int[][]{EMPTY_STATE_SET, GROUP_EXPANDED_STATE_SET, GROUP_EMPTY_STATE_SET, GROUP_EXPANDED_EMPTY_STATE_SET};
    private static final int INDICATOR_UNDEFINED = -2;
    private static final long PACKED_POSITION_INT_MASK_CHILD = -1;
    private static final long PACKED_POSITION_INT_MASK_GROUP = 2147483647L;
    private static final long PACKED_POSITION_MASK_CHILD = 4294967295L;
    private static final long PACKED_POSITION_MASK_GROUP = 9223372032559808512L;
    private static final long PACKED_POSITION_MASK_TYPE = Long.MIN_VALUE;
    private static final long PACKED_POSITION_SHIFT_GROUP = 32;
    private static final long PACKED_POSITION_SHIFT_TYPE = 63;
    public static final int PACKED_POSITION_TYPE_CHILD = 1;
    public static final int PACKED_POSITION_TYPE_GROUP = 0;
    public static final int PACKED_POSITION_TYPE_NULL = 2;
    public static final long PACKED_POSITION_VALUE_NULL = 4294967295L;
    private ExpandableListAdapter mAdapter;
    @UnsupportedAppUsage
    private Drawable mChildDivider;
    private Drawable mChildIndicator;
    private int mChildIndicatorEnd;
    private int mChildIndicatorLeft;
    private int mChildIndicatorRight;
    private int mChildIndicatorStart;
    @UnsupportedAppUsage
    private ExpandableListConnector mConnector;
    @UnsupportedAppUsage
    private Drawable mGroupIndicator;
    private int mIndicatorEnd;
    @UnsupportedAppUsage
    private int mIndicatorLeft;
    private final Rect mIndicatorRect;
    @UnsupportedAppUsage
    private int mIndicatorRight;
    private int mIndicatorStart;
    @UnsupportedAppUsage
    private OnChildClickListener mOnChildClickListener;
    @UnsupportedAppUsage
    private OnGroupClickListener mOnGroupClickListener;
    @UnsupportedAppUsage
    private OnGroupCollapseListener mOnGroupCollapseListener;
    @UnsupportedAppUsage
    private OnGroupExpandListener mOnGroupExpandListener;

    public static class ExpandableListContextMenuInfo implements ContextMenuInfo {
        public long id;
        public long packedPosition;
        public View targetView;

        public ExpandableListContextMenuInfo(View targetView, long packedPosition, long id) {
            this.targetView = targetView;
            this.packedPosition = packedPosition;
            this.id = id;
        }
    }

    public interface OnChildClickListener {
        boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i2, long j);
    }

    public interface OnGroupClickListener {
        boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long j);
    }

    public interface OnGroupCollapseListener {
        void onGroupCollapse(int i);
    }

    public interface OnGroupExpandListener {
        void onGroupExpand(int i);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in, null);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        ArrayList<GroupMetadata> expandedGroupMetadataList;

        SavedState(Parcelable superState, ArrayList<GroupMetadata> expandedGroupMetadataList) {
            super(superState);
            this.expandedGroupMetadataList = expandedGroupMetadataList;
        }

        private SavedState(Parcel in) {
            super(in);
            this.expandedGroupMetadataList = new ArrayList();
            in.readList(this.expandedGroupMetadataList, ExpandableListConnector.class.getClassLoader());
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeList(this.expandedGroupMetadataList);
        }
    }

    public ExpandableListView(Context context) {
        this(context, null);
    }

    public ExpandableListView(Context context, AttributeSet attrs) {
        this(context, attrs, 16842863);
    }

    public ExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ExpandableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mIndicatorRect = new Rect();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ExpandableListView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.ExpandableListView, attrs, a, defStyleAttr, defStyleRes);
        this.mGroupIndicator = a.getDrawable(0);
        this.mChildIndicator = a.getDrawable(1);
        this.mIndicatorLeft = a.getDimensionPixelSize(2, 0);
        this.mIndicatorRight = a.getDimensionPixelSize(3, 0);
        if (this.mIndicatorRight == 0) {
            Drawable drawable = this.mGroupIndicator;
            if (drawable != null) {
                this.mIndicatorRight = this.mIndicatorLeft + drawable.getIntrinsicWidth();
            }
        }
        this.mChildIndicatorLeft = a.getDimensionPixelSize(4, -1);
        this.mChildIndicatorRight = a.getDimensionPixelSize(5, -1);
        this.mChildDivider = a.getDrawable(6);
        if (!isRtlCompatibilityMode()) {
            this.mIndicatorStart = a.getDimensionPixelSize(7, -2);
            this.mIndicatorEnd = a.getDimensionPixelSize(8, -2);
            this.mChildIndicatorStart = a.getDimensionPixelSize(9, -1);
            this.mChildIndicatorEnd = a.getDimensionPixelSize(10, -1);
        }
        a.recycle();
    }

    private boolean isRtlCompatibilityMode() {
        return this.mContext.getApplicationInfo().targetSdkVersion < 17 || !hasRtlSupport();
    }

    private boolean hasRtlSupport() {
        return this.mContext.getApplicationInfo().hasRtlSupport();
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        resolveIndicator();
        resolveChildIndicator();
    }

    private void resolveIndicator() {
        int i;
        if (isLayoutRtl()) {
            i = this.mIndicatorStart;
            if (i >= 0) {
                this.mIndicatorRight = i;
            }
            i = this.mIndicatorEnd;
            if (i >= 0) {
                this.mIndicatorLeft = i;
            }
        } else {
            i = this.mIndicatorStart;
            if (i >= 0) {
                this.mIndicatorLeft = i;
            }
            i = this.mIndicatorEnd;
            if (i >= 0) {
                this.mIndicatorRight = i;
            }
        }
        if (this.mIndicatorRight == 0) {
            Drawable drawable = this.mGroupIndicator;
            if (drawable != null) {
                this.mIndicatorRight = this.mIndicatorLeft + drawable.getIntrinsicWidth();
            }
        }
    }

    private void resolveChildIndicator() {
        int i;
        if (isLayoutRtl()) {
            i = this.mChildIndicatorStart;
            if (i >= -1) {
                this.mChildIndicatorRight = i;
            }
            i = this.mChildIndicatorEnd;
            if (i >= -1) {
                this.mChildIndicatorLeft = i;
                return;
            }
            return;
        }
        i = this.mChildIndicatorStart;
        if (i >= -1) {
            this.mChildIndicatorLeft = i;
        }
        i = this.mChildIndicatorEnd;
        if (i >= -1) {
            this.mChildIndicatorRight = i;
        }
    }

    /* Access modifiers changed, original: protected */
    public void dispatchDraw(Canvas canvas) {
        Canvas canvas2 = canvas;
        super.dispatchDraw(canvas);
        if (this.mChildIndicator != null || this.mGroupIndicator != null) {
            int scrollX;
            int scrollY;
            int lastChildFlPos;
            int saveCount = 0;
            boolean clipToPadding = (this.mGroupFlags & 34) == 34;
            if (clipToPadding) {
                saveCount = canvas.save();
                scrollX = this.mScrollX;
                scrollY = this.mScrollY;
                canvas2.clipRect(this.mPaddingLeft + scrollX, this.mPaddingTop + scrollY, ((this.mRight + scrollX) - this.mLeft) - this.mPaddingRight, ((this.mBottom + scrollY) - this.mTop) - this.mPaddingBottom);
            }
            scrollX = getHeaderViewsCount();
            scrollY = ((this.mItemCount - getFooterViewsCount()) - scrollX) - 1;
            int myB = this.mBottom;
            int lastItemType = -4;
            Rect indicatorRect = this.mIndicatorRect;
            int childCount = getChildCount();
            int i = 0;
            int childFlPos = this.mFirstPosition - scrollX;
            while (i < childCount) {
                int headerViewsCount;
                if (childFlPos < 0) {
                    headerViewsCount = scrollX;
                    lastChildFlPos = scrollY;
                } else if (childFlPos > scrollY) {
                    headerViewsCount = scrollX;
                    lastChildFlPos = scrollY;
                    break;
                } else {
                    View item = getChildAt(i);
                    int t = item.getTop();
                    int b = item.getBottom();
                    if (b < 0) {
                        headerViewsCount = scrollX;
                        lastChildFlPos = scrollY;
                    } else if (t > myB) {
                        headerViewsCount = scrollX;
                        lastChildFlPos = scrollY;
                    } else {
                        PositionMetadata pos = this.mConnector.getUnflattenedPos(childFlPos);
                        boolean isLayoutRtl = isLayoutRtl();
                        int width = getWidth();
                        headerViewsCount = scrollX;
                        if (pos.position.type != lastItemType) {
                            lastChildFlPos = scrollY;
                            if (pos.position.type == 1) {
                                scrollX = this.mChildIndicatorLeft;
                                if (scrollX == -1) {
                                    scrollX = this.mIndicatorLeft;
                                }
                                indicatorRect.left = scrollX;
                                scrollX = this.mChildIndicatorRight;
                                if (scrollX == -1) {
                                    scrollX = this.mIndicatorRight;
                                }
                                indicatorRect.right = scrollX;
                            } else {
                                indicatorRect.left = this.mIndicatorLeft;
                                indicatorRect.right = this.mIndicatorRight;
                            }
                            if (isLayoutRtl) {
                                scrollX = indicatorRect.left;
                                indicatorRect.left = width - indicatorRect.right;
                                indicatorRect.right = width - scrollX;
                                int temp = scrollX;
                                indicatorRect.left -= this.mPaddingRight;
                                indicatorRect.right -= this.mPaddingRight;
                            } else {
                                indicatorRect.left += this.mPaddingLeft;
                                indicatorRect.right += this.mPaddingLeft;
                            }
                            lastItemType = pos.position.type;
                        } else {
                            lastChildFlPos = scrollY;
                        }
                        if (indicatorRect.left != indicatorRect.right) {
                            if (this.mStackFromBottom != 0) {
                                indicatorRect.top = t;
                                indicatorRect.bottom = b;
                            } else {
                                indicatorRect.top = t;
                                indicatorRect.bottom = b;
                            }
                            scrollX = getIndicator(pos);
                            if (scrollX != 0) {
                                scrollX.setBounds(indicatorRect);
                                scrollX.draw(canvas2);
                            }
                        }
                        pos.recycle();
                    }
                }
                i++;
                childFlPos++;
                scrollX = headerViewsCount;
                scrollY = lastChildFlPos;
            }
            lastChildFlPos = scrollY;
            if (clipToPadding) {
                canvas2.restoreToCount(saveCount);
            }
        }
    }

    private Drawable getIndicator(PositionMetadata pos) {
        Drawable indicator;
        int stateSetIndex = 2;
        if (pos.position.type == 2) {
            indicator = this.mGroupIndicator;
            if (indicator != null && indicator.isStateful()) {
                boolean isEmpty = pos.groupMetadata == null || pos.groupMetadata.lastChildFlPos == pos.groupMetadata.flPos;
                boolean isExpanded = pos.isExpanded();
                if (!isEmpty) {
                    stateSetIndex = 0;
                }
                indicator.setState(GROUP_STATE_SETS[stateSetIndex | isExpanded]);
            }
        } else {
            indicator = this.mChildIndicator;
            if (indicator != null && indicator.isStateful()) {
                int[] stateSet;
                if (pos.position.flatListPos == pos.groupMetadata.lastChildFlPos) {
                    stateSet = CHILD_LAST_STATE_SET;
                } else {
                    stateSet = EMPTY_STATE_SET;
                }
                indicator.setState(stateSet);
            }
        }
        return indicator;
    }

    public void setChildDivider(Drawable childDivider) {
        this.mChildDivider = childDivider;
    }

    /* Access modifiers changed, original: 0000 */
    public void drawDivider(Canvas canvas, Rect bounds, int childIndex) {
        int flatListPosition = this.mFirstPosition + childIndex;
        if (flatListPosition >= 0) {
            PositionMetadata pos = this.mConnector.getUnflattenedPos(getFlatPositionForConnector(flatListPosition));
            if (pos.position.type == 1 || (pos.isExpanded() && pos.groupMetadata.lastChildFlPos != pos.groupMetadata.flPos)) {
                Drawable divider = this.mChildDivider;
                divider.setBounds(bounds);
                divider.draw(canvas);
                pos.recycle();
                return;
            }
            pos.recycle();
        }
        super.drawDivider(canvas, bounds, flatListPosition);
    }

    public void setAdapter(ListAdapter adapter) {
        throw new RuntimeException("For ExpandableListView, use setAdapter(ExpandableListAdapter) instead of setAdapter(ListAdapter)");
    }

    public ListAdapter getAdapter() {
        return super.getAdapter();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        super.setOnItemClickListener(l);
    }

    public void setAdapter(ExpandableListAdapter adapter) {
        this.mAdapter = adapter;
        if (adapter != null) {
            this.mConnector = new ExpandableListConnector(adapter);
        } else {
            this.mConnector = null;
        }
        super.setAdapter(this.mConnector);
    }

    public ExpandableListAdapter getExpandableListAdapter() {
        return this.mAdapter;
    }

    private boolean isHeaderOrFooterPosition(int position) {
        return position < getHeaderViewsCount() || position >= this.mItemCount - getFooterViewsCount();
    }

    private int getFlatPositionForConnector(int flatListPosition) {
        return flatListPosition - getHeaderViewsCount();
    }

    private int getAbsoluteFlatPosition(int flatListPosition) {
        return getHeaderViewsCount() + flatListPosition;
    }

    public boolean performItemClick(View v, int position, long id) {
        if (isHeaderOrFooterPosition(position)) {
            return super.performItemClick(v, position, id);
        }
        return handleItemClick(v, getFlatPositionForConnector(position), id);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean handleItemClick(View v, int position, long id) {
        boolean returnValue;
        PositionMetadata posMetadata = this.mConnector.getUnflattenedPos(position);
        id = getChildOrGroupId(posMetadata.position);
        if (posMetadata.position.type == 2) {
            OnGroupClickListener onGroupClickListener = this.mOnGroupClickListener;
            if (onGroupClickListener != null) {
                if (onGroupClickListener.onGroupClick(this, v, posMetadata.position.groupPos, id)) {
                    posMetadata.recycle();
                    return true;
                }
            }
            if (posMetadata.isExpanded()) {
                this.mConnector.collapseGroup(posMetadata);
                playSoundEffect(0);
                OnGroupCollapseListener onGroupCollapseListener = this.mOnGroupCollapseListener;
                if (onGroupCollapseListener != null) {
                    onGroupCollapseListener.onGroupCollapse(posMetadata.position.groupPos);
                }
            } else {
                this.mConnector.expandGroup(posMetadata);
                playSoundEffect(0);
                OnGroupExpandListener onGroupExpandListener = this.mOnGroupExpandListener;
                if (onGroupExpandListener != null) {
                    onGroupExpandListener.onGroupExpand(posMetadata.position.groupPos);
                }
                int groupPos = posMetadata.position.groupPos;
                int shiftedGroupPosition = getHeaderViewsCount() + posMetadata.position.flatListPos;
                smoothScrollToPosition(this.mAdapter.getChildrenCount(groupPos) + shiftedGroupPosition, shiftedGroupPosition);
            }
            returnValue = true;
        } else if (this.mOnChildClickListener != null) {
            playSoundEffect(0);
            return this.mOnChildClickListener.onChildClick(this, v, posMetadata.position.groupPos, posMetadata.position.childPos, id);
        } else {
            returnValue = false;
        }
        posMetadata.recycle();
        return returnValue;
    }

    public boolean expandGroup(int groupPos) {
        return expandGroup(groupPos, false);
    }

    public boolean expandGroup(int groupPos, boolean animate) {
        ExpandableListPosition elGroupPos = ExpandableListPosition.obtain(2, groupPos, -1, -1);
        PositionMetadata pm = this.mConnector.getFlattenedPos(elGroupPos);
        elGroupPos.recycle();
        boolean retValue = this.mConnector.expandGroup(pm);
        OnGroupExpandListener onGroupExpandListener = this.mOnGroupExpandListener;
        if (onGroupExpandListener != null) {
            onGroupExpandListener.onGroupExpand(groupPos);
        }
        if (animate) {
            int shiftedGroupPosition = getHeaderViewsCount() + pm.position.flatListPos;
            smoothScrollToPosition(this.mAdapter.getChildrenCount(groupPos) + shiftedGroupPosition, shiftedGroupPosition);
        }
        pm.recycle();
        return retValue;
    }

    public boolean collapseGroup(int groupPos) {
        boolean retValue = this.mConnector.collapseGroup(groupPos);
        OnGroupCollapseListener onGroupCollapseListener = this.mOnGroupCollapseListener;
        if (onGroupCollapseListener != null) {
            onGroupCollapseListener.onGroupCollapse(groupPos);
        }
        return retValue;
    }

    public void setOnGroupCollapseListener(OnGroupCollapseListener onGroupCollapseListener) {
        this.mOnGroupCollapseListener = onGroupCollapseListener;
    }

    public void setOnGroupExpandListener(OnGroupExpandListener onGroupExpandListener) {
        this.mOnGroupExpandListener = onGroupExpandListener;
    }

    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener) {
        this.mOnGroupClickListener = onGroupClickListener;
    }

    public void setOnChildClickListener(OnChildClickListener onChildClickListener) {
        this.mOnChildClickListener = onChildClickListener;
    }

    public long getExpandableListPosition(int flatListPosition) {
        if (isHeaderOrFooterPosition(flatListPosition)) {
            return 4294967295L;
        }
        PositionMetadata pm = this.mConnector.getUnflattenedPos(getFlatPositionForConnector(flatListPosition));
        long packedPos = pm.position.getPackedPosition();
        pm.recycle();
        return packedPos;
    }

    public int getFlatListPosition(long packedPosition) {
        ExpandableListPosition elPackedPos = ExpandableListPosition.obtainPosition(packedPosition);
        PositionMetadata pm = this.mConnector.getFlattenedPos(elPackedPos);
        elPackedPos.recycle();
        int flatListPosition = pm.position.flatListPos;
        pm.recycle();
        return getAbsoluteFlatPosition(flatListPosition);
    }

    public long getSelectedPosition() {
        return getExpandableListPosition(getSelectedItemPosition());
    }

    public long getSelectedId() {
        long packedPos = getSelectedPosition();
        if (packedPos == 4294967295L) {
            return -1;
        }
        int groupPos = getPackedPositionGroup(packedPos);
        if (getPackedPositionType(packedPos) == 0) {
            return this.mAdapter.getGroupId(groupPos);
        }
        return this.mAdapter.getChildId(groupPos, getPackedPositionChild(packedPos));
    }

    public void setSelectedGroup(int groupPosition) {
        ExpandableListPosition elGroupPos = ExpandableListPosition.obtainGroupPosition(groupPosition);
        PositionMetadata pm = this.mConnector.getFlattenedPos(elGroupPos);
        elGroupPos.recycle();
        super.setSelection(getAbsoluteFlatPosition(pm.position.flatListPos));
        pm.recycle();
    }

    public boolean setSelectedChild(int groupPosition, int childPosition, boolean shouldExpandGroup) {
        ExpandableListPosition elChildPos = ExpandableListPosition.obtainChildPosition(groupPosition, childPosition);
        PositionMetadata flatChildPos = this.mConnector.getFlattenedPos(elChildPos);
        if (flatChildPos == null) {
            if (!shouldExpandGroup) {
                return false;
            }
            expandGroup(groupPosition);
            flatChildPos = this.mConnector.getFlattenedPos(elChildPos);
            if (flatChildPos == null) {
                throw new IllegalStateException("Could not find child");
            }
        }
        super.setSelection(getAbsoluteFlatPosition(flatChildPos.position.flatListPos));
        elChildPos.recycle();
        flatChildPos.recycle();
        return true;
    }

    public boolean isGroupExpanded(int groupPosition) {
        return this.mConnector.isGroupExpanded(groupPosition);
    }

    public static int getPackedPositionType(long packedPosition) {
        if (packedPosition == 4294967295L) {
            return 2;
        }
        int i;
        if ((packedPosition & Long.MIN_VALUE) == Long.MIN_VALUE) {
            i = 1;
        } else {
            i = 0;
        }
        return i;
    }

    public static int getPackedPositionGroup(long packedPosition) {
        if (packedPosition == 4294967295L) {
            return -1;
        }
        return (int) ((PACKED_POSITION_MASK_GROUP & packedPosition) >> 32);
    }

    public static int getPackedPositionChild(long packedPosition) {
        if (packedPosition != 4294967295L && (packedPosition & Long.MIN_VALUE) == Long.MIN_VALUE) {
            return (int) (4294967295L & packedPosition);
        }
        return -1;
    }

    public static long getPackedPositionForChild(int groupPosition, int childPosition) {
        return (((((long) groupPosition) & PACKED_POSITION_INT_MASK_GROUP) << 32) | Long.MIN_VALUE) | (((long) childPosition) & -1);
    }

    public static long getPackedPositionForGroup(int groupPosition) {
        return (((long) groupPosition) & PACKED_POSITION_INT_MASK_GROUP) << 32;
    }

    /* Access modifiers changed, original: 0000 */
    public ContextMenuInfo createContextMenuInfo(View view, int flatListPosition, long id) {
        int i = flatListPosition;
        if (isHeaderOrFooterPosition(i)) {
            return new AdapterContextMenuInfo(view, i, id);
        }
        View view2 = view;
        long j = id;
        PositionMetadata pm = this.mConnector.getUnflattenedPos(getFlatPositionForConnector(i));
        ExpandableListPosition pos = pm.position;
        long id2 = getChildOrGroupId(pos);
        long packedPosition = pos.getPackedPosition();
        pm.recycle();
        return new ExpandableListContextMenuInfo(view, packedPosition, id2);
    }

    private long getChildOrGroupId(ExpandableListPosition position) {
        if (position.type == 1) {
            return this.mAdapter.getChildId(position.groupPos, position.childPos);
        }
        return this.mAdapter.getGroupId(position.groupPos);
    }

    public void setChildIndicator(Drawable childIndicator) {
        this.mChildIndicator = childIndicator;
    }

    public void setChildIndicatorBounds(int left, int right) {
        this.mChildIndicatorLeft = left;
        this.mChildIndicatorRight = right;
        resolveChildIndicator();
    }

    public void setChildIndicatorBoundsRelative(int start, int end) {
        this.mChildIndicatorStart = start;
        this.mChildIndicatorEnd = end;
        resolveChildIndicator();
    }

    public void setGroupIndicator(Drawable groupIndicator) {
        this.mGroupIndicator = groupIndicator;
        if (this.mIndicatorRight == 0) {
            Drawable drawable = this.mGroupIndicator;
            if (drawable != null) {
                this.mIndicatorRight = this.mIndicatorLeft + drawable.getIntrinsicWidth();
            }
        }
    }

    public void setIndicatorBounds(int left, int right) {
        this.mIndicatorLeft = left;
        this.mIndicatorRight = right;
        resolveIndicator();
    }

    public void setIndicatorBoundsRelative(int start, int end) {
        this.mIndicatorStart = start;
        this.mIndicatorEnd = end;
        resolveIndicator();
    }

    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        ExpandableListConnector expandableListConnector = this.mConnector;
        return new SavedState(superState, expandableListConnector != null ? expandableListConnector.getExpandedGroupMetadataList() : null);
    }

    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState ss = (SavedState) state;
            super.onRestoreInstanceState(ss.getSuperState());
            if (!(this.mConnector == null || ss.expandedGroupMetadataList == null)) {
                this.mConnector.setExpandedGroupMetadataList(ss.expandedGroupMetadataList);
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }

    public CharSequence getAccessibilityClassName() {
        return ExpandableListView.class.getName();
    }
}
