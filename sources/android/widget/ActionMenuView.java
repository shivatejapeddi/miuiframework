package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewDebug.ExportedProperty;
import android.view.ViewGroup;
import android.view.ViewHierarchyEncoder;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.view.menu.ActionMenuItemView;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuBuilder.ItemInvoker;
import com.android.internal.view.menu.MenuItemImpl;
import com.android.internal.view.menu.MenuPresenter.Callback;
import com.android.internal.view.menu.MenuView;

public class ActionMenuView extends LinearLayout implements ItemInvoker, MenuView {
    static final int GENERATED_ITEM_PADDING = 4;
    static final int MIN_CELL_SIZE = 56;
    private static final String TAG = "ActionMenuView";
    private Callback mActionMenuPresenterCallback;
    private boolean mFormatItems;
    private int mFormatItemsWidth;
    private int mGeneratedItemPadding;
    private MenuBuilder mMenu;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private int mMinCellSize;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private Context mPopupContext;
    private int mPopupTheme;
    private ActionMenuPresenter mPresenter;
    private boolean mReserveOverflow;

    public interface ActionMenuChildView {
        boolean needsDividerAfter();

        @UnsupportedAppUsage
        boolean needsDividerBefore();
    }

    private class ActionMenuPresenterCallback implements Callback {
        private ActionMenuPresenterCallback() {
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            return false;
        }
    }

    public static class LayoutParams extends android.widget.LinearLayout.LayoutParams {
        @ExportedProperty(category = "layout")
        @UnsupportedAppUsage
        public int cellsUsed;
        @ExportedProperty(category = "layout")
        @UnsupportedAppUsage
        public boolean expandable;
        @UnsupportedAppUsage
        public boolean expanded;
        @ExportedProperty(category = "layout")
        @UnsupportedAppUsage
        public int extraPixels;
        @ExportedProperty(category = "layout")
        @UnsupportedAppUsage
        public boolean isOverflowButton;
        @ExportedProperty(category = "layout")
        @UnsupportedAppUsage
        public boolean preventEdgeOffset;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams other) {
            super(other);
        }

        public LayoutParams(LayoutParams other) {
            super((android.widget.LinearLayout.LayoutParams) other);
            this.isOverflowButton = other.isOverflowButton;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.isOverflowButton = false;
        }

        public LayoutParams(int width, int height, boolean isOverflowButton) {
            super(width, height);
            this.isOverflowButton = isOverflowButton;
        }

        /* Access modifiers changed, original: protected */
        public void encodeProperties(ViewHierarchyEncoder encoder) {
            super.encodeProperties(encoder);
            encoder.addProperty("layout:overFlowButton", this.isOverflowButton);
            encoder.addProperty("layout:cellsUsed", this.cellsUsed);
            encoder.addProperty("layout:extraPixels", this.extraPixels);
            encoder.addProperty("layout:expandable", this.expandable);
            encoder.addProperty("layout:preventEdgeOffset", this.preventEdgeOffset);
        }
    }

    private class MenuBuilderCallback implements MenuBuilder.Callback {
        private MenuBuilderCallback() {
        }

        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
            return ActionMenuView.this.mOnMenuItemClickListener != null && ActionMenuView.this.mOnMenuItemClickListener.onMenuItemClick(item);
        }

        public void onMenuModeChange(MenuBuilder menu) {
            if (ActionMenuView.this.mMenuBuilderCallback != null) {
                ActionMenuView.this.mMenuBuilderCallback.onMenuModeChange(menu);
            }
        }
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBaselineAligned(false);
        float density = context.getResources().getDisplayMetrics().density;
        this.mMinCellSize = (int) (56.0f * density);
        this.mGeneratedItemPadding = (int) (4.0f * density);
        this.mPopupContext = context;
        this.mPopupTheme = 0;
    }

    public void setPopupTheme(int resId) {
        if (this.mPopupTheme != resId) {
            this.mPopupTheme = resId;
            if (resId == 0) {
                this.mPopupContext = this.mContext;
            } else {
                this.mPopupContext = new ContextThemeWrapper(this.mContext, resId);
            }
        }
    }

    public int getPopupTheme() {
        return this.mPopupTheme;
    }

    public void setPresenter(ActionMenuPresenter presenter) {
        this.mPresenter = presenter;
        this.mPresenter.setMenuView(this);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.updateMenuView(false);
            if (this.mPresenter.isOverflowMenuShowing()) {
                this.mPresenter.hideOverflowMenu();
                this.mPresenter.showOverflowMenu();
            }
        }
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        boolean wasFormatted = this.mFormatItems;
        this.mFormatItems = MeasureSpec.getMode(widthMeasureSpec) == 1073741824;
        if (wasFormatted != this.mFormatItems) {
            this.mFormatItemsWidth = 0;
        }
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        if (this.mFormatItems) {
            MenuBuilder menuBuilder = this.mMenu;
            if (!(menuBuilder == null || widthSize == this.mFormatItemsWidth)) {
                this.mFormatItemsWidth = widthSize;
                menuBuilder.onItemsChanged(true);
            }
        }
        int childCount = getChildCount();
        if (!this.mFormatItems || childCount <= 0) {
            for (int i = 0; i < childCount; i++) {
                LayoutParams lp = (LayoutParams) getChildAt(i).getLayoutParams();
                lp.rightMargin = 0;
                lp.leftMargin = 0;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        onMeasureExactFormat(widthMeasureSpec, heightMeasureSpec);
    }

    private void onMeasureExactFormat(int widthMeasureSpec, int heightMeasureSpec) {
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthPadding = getPaddingLeft() + getPaddingRight();
        int heightPadding = getPaddingTop() + getPaddingBottom();
        int itemHeightSpec = ViewGroup.getChildMeasureSpec(heightMeasureSpec, heightPadding, -2);
        widthSize -= widthPadding;
        int cellSize = this.mMinCellSize;
        int cellCount = widthSize / cellSize;
        int cellSizeRemaining = widthSize % cellSize;
        if (cellCount == 0) {
            setMeasuredDimension(widthSize, 0);
            return;
        }
        int cellCount2;
        int cellSizeRemaining2;
        boolean isGeneratedItem;
        LayoutParams lp;
        int cellsUsed;
        boolean needsExpansion;
        int childCount;
        int maxChildHeight;
        cellSize += cellSizeRemaining / cellCount;
        int cellsRemaining = cellCount;
        boolean hasOverflow = false;
        int childCount2 = getChildCount();
        int heightSize2 = heightSize;
        heightSize = 0;
        int visibleItemCount = 0;
        int expandableItemCount = 0;
        int maxCellsUsed = 0;
        int cellsRemaining2 = cellsRemaining;
        cellsRemaining = 0;
        long smallestItemsAt = 0;
        while (true) {
            int widthPadding2 = widthPadding;
            if (cellsRemaining >= childCount2) {
                break;
            }
            int heightPadding2;
            View child = getChildAt(cellsRemaining);
            cellCount2 = cellCount;
            if (child.getVisibility() == 8) {
                heightPadding2 = heightPadding;
                cellSizeRemaining2 = cellSizeRemaining;
            } else {
                boolean cellSizeRemaining3;
                isGeneratedItem = child instanceof ActionMenuItemView;
                visibleItemCount++;
                if (isGeneratedItem) {
                    cellCount = this.mGeneratedItemPadding;
                    cellSizeRemaining2 = cellSizeRemaining;
                    cellSizeRemaining3 = false;
                    child.setPadding(cellCount, 0, cellCount, 0);
                } else {
                    cellSizeRemaining2 = cellSizeRemaining;
                    cellSizeRemaining3 = false;
                }
                lp = (LayoutParams) child.getLayoutParams();
                lp.expanded = cellSizeRemaining3;
                lp.extraPixels = cellSizeRemaining3;
                lp.cellsUsed = cellSizeRemaining3;
                lp.expandable = cellSizeRemaining3;
                lp.leftMargin = cellSizeRemaining3;
                lp.rightMargin = cellSizeRemaining3;
                cellSizeRemaining3 = isGeneratedItem && ((ActionMenuItemView) child).hasText();
                lp.preventEdgeOffset = cellSizeRemaining3;
                cellsUsed = measureChildForCells(child, cellSize, lp.isOverflowButton ? 1 : cellsRemaining2, itemHeightSpec, heightPadding);
                maxCellsUsed = Math.max(maxCellsUsed, cellsUsed);
                heightPadding2 = heightPadding;
                if (lp.expandable) {
                    expandableItemCount++;
                }
                if (lp.isOverflowButton) {
                    hasOverflow = true;
                }
                cellsRemaining2 -= cellsUsed;
                heightSize = Math.max(heightSize, child.getMeasuredHeight());
                if (cellsUsed == 1) {
                    smallestItemsAt |= (long) (1 << cellsRemaining);
                    heightSize = heightSize;
                } else {
                    View view = child;
                }
            }
            cellsRemaining++;
            cellsUsed = heightMeasureSpec;
            widthPadding = widthPadding2;
            cellCount = cellCount2;
            cellSizeRemaining = cellSizeRemaining2;
            heightPadding = heightPadding2;
        }
        cellCount2 = cellCount;
        cellSizeRemaining2 = cellSizeRemaining;
        boolean centerSingleExpandedItem = hasOverflow && visibleItemCount == 2;
        isGeneratedItem = false;
        while (expandableItemCount > 0 && cellsRemaining2 > 0) {
            long minCellsAt = 0;
            cellCount = Integer.MAX_VALUE;
            widthPadding = 0;
            cellsRemaining = 0;
            while (cellsRemaining < childCount2) {
                View child2 = getChildAt(cellsRemaining);
                needsExpansion = isGeneratedItem;
                LayoutParams needsExpansion2 = (LayoutParams) child2.getLayoutParams();
                if (!needsExpansion2.expandable) {
                    childCount = childCount2;
                } else if (needsExpansion2.cellsUsed < cellCount) {
                    childCount = childCount2;
                    widthPadding = 1;
                    minCellsAt = (long) (1 << cellsRemaining);
                    cellCount = needsExpansion2.cellsUsed;
                } else {
                    childCount = childCount2;
                    if (needsExpansion2.cellsUsed == cellCount) {
                        widthPadding++;
                        minCellsAt |= (long) (1 << cellsRemaining);
                    }
                }
                cellsRemaining++;
                isGeneratedItem = needsExpansion;
                childCount2 = childCount;
            }
            needsExpansion = isGeneratedItem;
            childCount = childCount2;
            smallestItemsAt |= minCellsAt;
            if (widthPadding > cellsRemaining2) {
                maxChildHeight = heightSize;
                cellSizeRemaining = childCount;
                childCount = widthSize;
                break;
            }
            int minCellsItemCount;
            cellCount++;
            cellsUsed = 0;
            while (true) {
                cellSizeRemaining = childCount;
                if (cellsUsed >= cellSizeRemaining) {
                    break;
                }
                View child3 = getChildAt(cellsUsed);
                LayoutParams lp2 = (LayoutParams) child3.getLayoutParams();
                minCellsItemCount = widthPadding;
                childCount = widthSize;
                maxChildHeight = heightSize;
                if ((minCellsAt & ((long) (1 << cellsUsed))) != 0) {
                    if (centerSingleExpandedItem && lp2.preventEdgeOffset && cellsRemaining2 == 1) {
                        widthSize = this.mGeneratedItemPadding;
                        child3.setPadding(widthSize + cellSize, 0, widthSize, 0);
                    }
                    lp2.cellsUsed++;
                    lp2.expanded = true;
                    cellsRemaining2--;
                } else if (lp2.cellsUsed == cellCount) {
                    smallestItemsAt |= (long) (1 << cellsUsed);
                }
                cellsUsed++;
                widthPadding = minCellsItemCount;
                widthSize = childCount;
                heightSize = maxChildHeight;
                childCount = cellSizeRemaining;
            }
            maxChildHeight = heightSize;
            minCellsItemCount = widthPadding;
            isGeneratedItem = true;
            childCount2 = cellSizeRemaining;
        }
        childCount = widthSize;
        maxChildHeight = heightSize;
        needsExpansion = isGeneratedItem;
        cellSizeRemaining = childCount2;
        boolean singleItem = !hasOverflow && visibleItemCount == 1;
        if (cellsRemaining2 > 0 && smallestItemsAt != 0 && (cellsRemaining2 < visibleItemCount - 1 || singleItem || maxCellsUsed > 1)) {
            float expandCount = (float) Long.bitCount(smallestItemsAt);
            if (singleItem) {
                widthPadding = 0;
            } else {
                if ((smallestItemsAt & 1) != 0) {
                    widthPadding = 0;
                    if (!((LayoutParams) getChildAt(0).getLayoutParams()).preventEdgeOffset) {
                        expandCount -= 0.5f;
                    }
                } else {
                    widthPadding = 0;
                }
                if (!((smallestItemsAt & ((long) (1 << (cellSizeRemaining - 1)))) == 0 || ((LayoutParams) getChildAt(cellSizeRemaining - 1).getLayoutParams()).preventEdgeOffset)) {
                    expandCount -= 0.5f;
                }
            }
            if (expandCount > 0.0f) {
                widthPadding = (int) (((float) (cellsRemaining2 * cellSize)) / expandCount);
            }
            for (cellsUsed = 0; cellsUsed < cellSizeRemaining; cellsUsed++) {
                if ((smallestItemsAt & ((long) (1 << cellsUsed))) != 0) {
                    View child4 = getChildAt(cellsUsed);
                    LayoutParams lp3 = (LayoutParams) child4.getLayoutParams();
                    if (child4 instanceof ActionMenuItemView) {
                        lp3.extraPixels = widthPadding;
                        lp3.expanded = true;
                        if (cellsUsed == 0 && !lp3.preventEdgeOffset) {
                            lp3.leftMargin = (-widthPadding) / 2;
                        }
                        needsExpansion = true;
                    } else if (lp3.isOverflowButton) {
                        lp3.extraPixels = widthPadding;
                        lp3.expanded = true;
                        lp3.rightMargin = (-widthPadding) / 2;
                        needsExpansion = true;
                    } else {
                        if (cellsUsed != 0) {
                            lp3.leftMargin = widthPadding / 2;
                        }
                        if (cellsUsed != cellSizeRemaining - 1) {
                            lp3.rightMargin = widthPadding / 2;
                        }
                    }
                }
            }
        }
        if (needsExpansion) {
            for (widthPadding = 0; widthPadding < cellSizeRemaining; widthPadding++) {
                View child5 = getChildAt(widthPadding);
                lp = (LayoutParams) child5.getLayoutParams();
                if (lp.expanded) {
                    child5.measure(MeasureSpec.makeMeasureSpec((lp.cellsUsed * cellSize) + lp.extraPixels, 1073741824), itemHeightSpec);
                }
            }
        }
        if (heightMode != 1073741824) {
            heightSize = maxChildHeight;
        } else {
            heightSize = heightSize2;
        }
        setMeasuredDimension(childCount, heightSize);
    }

    static int measureChildForCells(View child, int cellSize, int cellsRemaining, int parentHeightMeasureSpec, int parentHeightPadding) {
        View view = child;
        int i = cellsRemaining;
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int childHeightSpec = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(parentHeightMeasureSpec) - parentHeightPadding, MeasureSpec.getMode(parentHeightMeasureSpec));
        ActionMenuItemView itemView = view instanceof ActionMenuItemView ? (ActionMenuItemView) view : null;
        boolean expandable = false;
        boolean hasText = itemView != null && itemView.hasText();
        int cellsUsed = 0;
        if (i > 0 && (!hasText || i >= 2)) {
            child.measure(MeasureSpec.makeMeasureSpec(cellSize * i, Integer.MIN_VALUE), childHeightSpec);
            int measuredWidth = child.getMeasuredWidth();
            cellsUsed = measuredWidth / cellSize;
            if (measuredWidth % cellSize != 0) {
                cellsUsed++;
            }
            if (hasText && cellsUsed < 2) {
                cellsUsed = 2;
            }
        }
        if (!lp.isOverflowButton && hasText) {
            expandable = true;
        }
        lp.expandable = expandable;
        lp.cellsUsed = cellsUsed;
        child.measure(MeasureSpec.makeMeasureSpec(cellsUsed * cellSize, 1073741824), childHeightSpec);
        return cellsUsed;
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        ActionMenuView actionMenuView = this;
        if (actionMenuView.mFormatItems) {
            int i;
            int midVertical;
            boolean isLayoutRtl;
            LayoutParams p;
            int height;
            int l;
            int r;
            int childCount = getChildCount();
            int midVertical2 = (bottom - top) / 2;
            int dividerWidth = getDividerWidth();
            int overflowWidth = 0;
            int nonOverflowWidth = 0;
            int nonOverflowCount = 0;
            int widthRemaining = ((right - left) - getPaddingRight()) - getPaddingLeft();
            boolean hasOverflow = false;
            boolean isLayoutRtl2 = isLayoutRtl();
            int i2 = 0;
            while (true) {
                i = 8;
                if (i2 >= childCount) {
                    break;
                }
                View v = actionMenuView.getChildAt(i2);
                if (v.getVisibility() == 8) {
                    midVertical = midVertical2;
                    isLayoutRtl = isLayoutRtl2;
                } else {
                    p = (LayoutParams) v.getLayoutParams();
                    if (p.isOverflowButton) {
                        overflowWidth = v.getMeasuredWidth();
                        if (actionMenuView.hasDividerBeforeChildAt(i2)) {
                            overflowWidth += dividerWidth;
                        }
                        height = v.getMeasuredHeight();
                        if (isLayoutRtl2) {
                            l = getPaddingLeft() + p.leftMargin;
                            r = l + overflowWidth;
                        } else {
                            r = (getWidth() - getPaddingRight()) - p.rightMargin;
                            l = r - overflowWidth;
                        }
                        isLayoutRtl = isLayoutRtl2;
                        isLayoutRtl2 = midVertical2 - (height / 2);
                        midVertical = midVertical2;
                        v.layout(l, isLayoutRtl2, r, isLayoutRtl2 + height);
                        widthRemaining -= overflowWidth;
                        hasOverflow = true;
                    } else {
                        midVertical = midVertical2;
                        isLayoutRtl = isLayoutRtl2;
                        midVertical2 = (v.getMeasuredWidth() + p.leftMargin) + p.rightMargin;
                        nonOverflowWidth += midVertical2;
                        widthRemaining -= midVertical2;
                        if (actionMenuView.hasDividerBeforeChildAt(i2)) {
                            nonOverflowWidth += dividerWidth;
                        }
                        nonOverflowCount++;
                    }
                }
                i2++;
                midVertical2 = midVertical;
                isLayoutRtl2 = isLayoutRtl;
            }
            midVertical = midVertical2;
            isLayoutRtl = isLayoutRtl2;
            int spacerCount = 1;
            int i3;
            if (childCount != 1 || hasOverflow) {
                if (hasOverflow) {
                    spacerCount = 0;
                }
                spacerCount = nonOverflowCount - spacerCount;
                midVertical2 = Math.max(0, spacerCount > 0 ? widthRemaining / spacerCount : 0);
                int overflowWidth2;
                if (isLayoutRtl) {
                    i2 = getWidth() - getPaddingRight();
                    i3 = 0;
                    while (i3 < childCount) {
                        int dividerWidth2;
                        View v2 = actionMenuView.getChildAt(i3);
                        LayoutParams lp = (LayoutParams) v2.getLayoutParams();
                        if (v2.getVisibility() == i) {
                            dividerWidth2 = dividerWidth;
                            overflowWidth2 = overflowWidth;
                        } else if (lp.isOverflowButton) {
                            dividerWidth2 = dividerWidth;
                            overflowWidth2 = overflowWidth;
                        } else {
                            i2 -= lp.rightMargin;
                            r = v2.getMeasuredWidth();
                            int height2 = v2.getMeasuredHeight();
                            i = midVertical - (height2 / 2);
                            dividerWidth2 = dividerWidth;
                            overflowWidth2 = overflowWidth;
                            v2.layout(i2 - r, i, i2, i + height2);
                            i2 -= (lp.leftMargin + r) + midVertical2;
                        }
                        i3++;
                        dividerWidth = dividerWidth2;
                        overflowWidth = overflowWidth2;
                        i = 8;
                    }
                    overflowWidth2 = overflowWidth;
                } else {
                    overflowWidth2 = overflowWidth;
                    dividerWidth = getPaddingLeft();
                    overflowWidth = 0;
                    while (overflowWidth < childCount) {
                        View v3 = actionMenuView.getChildAt(overflowWidth);
                        p = (LayoutParams) v3.getLayoutParams();
                        if (!(v3.getVisibility() == 8 || p.isOverflowButton)) {
                            dividerWidth += p.leftMargin;
                            i3 = v3.getMeasuredWidth();
                            l = v3.getMeasuredHeight();
                            r = midVertical - (l / 2);
                            v3.layout(dividerWidth, r, dividerWidth + i3, r + l);
                            dividerWidth += (p.rightMargin + i3) + midVertical2;
                        }
                        overflowWidth++;
                        actionMenuView = this;
                    }
                }
                return;
            }
            View v4 = actionMenuView.getChildAt(0);
            spacerCount = v4.getMeasuredWidth();
            i2 = v4.getMeasuredHeight();
            i3 = ((right - left) / 2) - (spacerCount / 2);
            height = midVertical - (i2 / 2);
            v4.layout(i3, height, i3 + spacerCount, height + i2);
            return;
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        dismissPopupMenus();
    }

    public void setOverflowIcon(Drawable icon) {
        getMenu();
        this.mPresenter.setOverflowIcon(icon);
    }

    public Drawable getOverflowIcon() {
        getMenu();
        return this.mPresenter.getOverflowIcon();
    }

    @UnsupportedAppUsage
    public boolean isOverflowReserved() {
        return this.mReserveOverflow;
    }

    public void setOverflowReserved(boolean reserveOverflow) {
        this.mReserveOverflow = reserveOverflow;
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        LayoutParams params = new LayoutParams(-2, -2);
        params.gravity = 16;
        return params;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p == null) {
            return generateDefaultLayoutParams();
        }
        LayoutParams result;
        if (p instanceof LayoutParams) {
            result = new LayoutParams((LayoutParams) p);
        } else {
            result = new LayoutParams(p);
        }
        if (result.gravity <= 0) {
            result.gravity = 16;
        }
        return result;
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p != null && (p instanceof LayoutParams);
    }

    public LayoutParams generateOverflowButtonLayoutParams() {
        LayoutParams result = generateDefaultLayoutParams();
        result.isOverflowButton = true;
        return result;
    }

    public boolean invokeItem(MenuItemImpl item) {
        return this.mMenu.performItemAction(item, 0);
    }

    public int getWindowAnimations() {
        return 0;
    }

    public void initialize(MenuBuilder menu) {
        this.mMenu = menu;
    }

    public Menu getMenu() {
        if (this.mMenu == null) {
            Context context = getContext();
            this.mMenu = new MenuBuilder(context);
            this.mMenu.setCallback(new MenuBuilderCallback());
            this.mPresenter = new ActionMenuPresenter(context);
            this.mPresenter.setReserveOverflow(true);
            ActionMenuPresenter actionMenuPresenter = this.mPresenter;
            Callback callback = this.mActionMenuPresenterCallback;
            if (callback == null) {
                callback = new ActionMenuPresenterCallback();
            }
            actionMenuPresenter.setCallback(callback);
            this.mMenu.addMenuPresenter(this.mPresenter, this.mPopupContext);
            this.mPresenter.setMenuView(this);
        }
        return this.mMenu;
    }

    @UnsupportedAppUsage
    public void setMenuCallbacks(Callback pcb, MenuBuilder.Callback mcb) {
        this.mActionMenuPresenterCallback = pcb;
        this.mMenuBuilderCallback = mcb;
    }

    @UnsupportedAppUsage
    public MenuBuilder peekMenu() {
        return this.mMenu;
    }

    public boolean showOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.showOverflowMenu();
    }

    public boolean hideOverflowMenu() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.hideOverflowMenu();
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowing();
    }

    @UnsupportedAppUsage
    public boolean isOverflowMenuShowPending() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        return actionMenuPresenter != null && actionMenuPresenter.isOverflowMenuShowPending();
    }

    public void dismissPopupMenus() {
        ActionMenuPresenter actionMenuPresenter = this.mPresenter;
        if (actionMenuPresenter != null) {
            actionMenuPresenter.dismissPopupMenus();
        }
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public boolean hasDividerBeforeChildAt(int childIndex) {
        if (childIndex == 0) {
            return false;
        }
        View childBefore = getChildAt(childIndex - 1);
        View child = getChildAt(childIndex);
        boolean result = false;
        if (childIndex < getChildCount() && (childBefore instanceof ActionMenuChildView)) {
            result = false | ((ActionMenuChildView) childBefore).needsDividerAfter();
        }
        if (childIndex > 0 && (child instanceof ActionMenuChildView)) {
            result |= ((ActionMenuChildView) child).needsDividerBefore();
        }
        return result;
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        return false;
    }

    @UnsupportedAppUsage
    public void setExpandedActionViewsExclusive(boolean exclusive) {
        this.mPresenter.setExpandedActionViewsExclusive(exclusive);
    }
}
