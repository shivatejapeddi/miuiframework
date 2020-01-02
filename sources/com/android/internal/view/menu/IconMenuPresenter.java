package com.android.internal.view.menu;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.R;
import com.android.internal.view.menu.MenuPresenter.Callback;
import com.android.internal.view.menu.MenuView.ItemView;

public class IconMenuPresenter extends BaseMenuPresenter {
    private static final String OPEN_SUBMENU_KEY = "android:menu:icon:submenu";
    private static final String VIEWS_TAG = "android:menu:icon";
    private int mMaxItems = -1;
    private IconMenuItemView mMoreView;
    MenuDialogHelper mOpenSubMenu;
    int mOpenSubMenuId;
    SubMenuPresenterCallback mSubMenuPresenterCallback = new SubMenuPresenterCallback();

    class SubMenuPresenterCallback implements Callback {
        SubMenuPresenterCallback() {
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            IconMenuPresenter iconMenuPresenter = IconMenuPresenter.this;
            iconMenuPresenter.mOpenSubMenuId = 0;
            if (iconMenuPresenter.mOpenSubMenu != null) {
                IconMenuPresenter.this.mOpenSubMenu.dismiss();
                IconMenuPresenter.this.mOpenSubMenu = null;
            }
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            if (subMenu != null) {
                IconMenuPresenter.this.mOpenSubMenuId = ((SubMenuBuilder) subMenu).getItem().getItemId();
            }
            return false;
        }
    }

    public IconMenuPresenter(Context context) {
        super(new ContextThemeWrapper(context, (int) R.style.Theme_IconMenu), R.layout.icon_menu_layout, R.layout.icon_menu_item_layout);
    }

    public void initForMenu(Context context, MenuBuilder menu) {
        super.initForMenu(context, menu);
        this.mMaxItems = -1;
    }

    public void bindItemView(MenuItemImpl item, ItemView itemView) {
        IconMenuItemView view = (IconMenuItemView) itemView;
        view.setItemData(item);
        view.initialize(item.getTitleForItemView(view), item.getIcon());
        view.setVisibility(item.isVisible() ? 0 : 8);
        view.setEnabled(view.isEnabled());
        view.setLayoutParams(view.getTextAppropriateLayoutParams());
    }

    public boolean shouldIncludeItem(int childIndex, MenuItemImpl item) {
        int size = this.mMenu.getNonActionItems().size();
        int i = this.mMaxItems;
        boolean fits = (size == i && childIndex < i) || childIndex < this.mMaxItems - 1;
        if (!fits || item.isActionButton()) {
            return false;
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void addItemView(View itemView, int childIndex) {
        IconMenuItemView v = (IconMenuItemView) itemView;
        IconMenuView parent = this.mMenuView;
        v.setIconMenuView(parent);
        v.setItemInvoker(parent);
        v.setBackgroundDrawable(parent.getItemBackgroundDrawable());
        super.addItemView(itemView, childIndex);
    }

    public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
        if (!subMenu.hasVisibleItems()) {
            return false;
        }
        MenuDialogHelper helper = new MenuDialogHelper(subMenu);
        helper.setPresenterCallback(this.mSubMenuPresenterCallback);
        helper.show(null);
        this.mOpenSubMenu = helper;
        this.mOpenSubMenuId = subMenu.getItem().getItemId();
        super.onSubMenuSelected(subMenu);
        return true;
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x0057  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0053  */
    public void updateMenuView(boolean r7) {
        /*
        r6 = this;
        r0 = r6.mMenuView;
        r0 = (com.android.internal.view.menu.IconMenuView) r0;
        r1 = r6.mMaxItems;
        if (r1 >= 0) goto L_0x000e;
    L_0x0008:
        r1 = r0.getMaxItems();
        r6.mMaxItems = r1;
    L_0x000e:
        r1 = r6.mMenu;
        r1 = r1.getNonActionItems();
        r2 = r1.size();
        r3 = r6.mMaxItems;
        r4 = 1;
        if (r2 <= r3) goto L_0x001f;
    L_0x001d:
        r2 = r4;
        goto L_0x0020;
    L_0x001f:
        r2 = 0;
    L_0x0020:
        super.updateMenuView(r7);
        if (r2 == 0) goto L_0x0048;
    L_0x0025:
        r3 = r6.mMoreView;
        if (r3 == 0) goto L_0x002f;
    L_0x0029:
        r3 = r3.getParent();
        if (r3 == r0) goto L_0x0048;
    L_0x002f:
        r3 = r6.mMoreView;
        if (r3 != 0) goto L_0x0042;
    L_0x0033:
        r3 = r0.createMoreItemView();
        r6.mMoreView = r3;
        r3 = r6.mMoreView;
        r5 = r0.getItemBackgroundDrawable();
        r3.setBackgroundDrawable(r5);
    L_0x0042:
        r3 = r6.mMoreView;
        r0.addView(r3);
        goto L_0x0051;
    L_0x0048:
        if (r2 != 0) goto L_0x0051;
    L_0x004a:
        r3 = r6.mMoreView;
        if (r3 == 0) goto L_0x0051;
    L_0x004e:
        r0.removeView(r3);
    L_0x0051:
        if (r2 == 0) goto L_0x0057;
    L_0x0053:
        r3 = r6.mMaxItems;
        r3 = r3 - r4;
        goto L_0x005b;
    L_0x0057:
        r3 = r1.size();
    L_0x005b:
        r0.setNumActualItemsShown(r3);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.view.menu.IconMenuPresenter.updateMenuView(boolean):void");
    }

    /* Access modifiers changed, original: protected */
    public boolean filterLeftoverView(ViewGroup parent, int childIndex) {
        if (parent.getChildAt(childIndex) != this.mMoreView) {
            return super.filterLeftoverView(parent, childIndex);
        }
        return false;
    }

    public int getNumActualItemsShown() {
        return ((IconMenuView) this.mMenuView).getNumActualItemsShown();
    }

    public void saveHierarchyState(Bundle outState) {
        SparseArray<Parcelable> viewStates = new SparseArray();
        if (this.mMenuView != null) {
            ((View) this.mMenuView).saveHierarchyState(viewStates);
        }
        outState.putSparseParcelableArray(VIEWS_TAG, viewStates);
    }

    public void restoreHierarchyState(Bundle inState) {
        SparseArray<Parcelable> viewStates = inState.getSparseParcelableArray(VIEWS_TAG);
        if (viewStates != null) {
            ((View) this.mMenuView).restoreHierarchyState(viewStates);
        }
        int subMenuId = inState.getInt(OPEN_SUBMENU_KEY, 0);
        if (subMenuId > 0 && this.mMenu != null) {
            MenuItem item = this.mMenu.findItem(subMenuId);
            if (item != null) {
                onSubMenuSelected((SubMenuBuilder) item.getSubMenu());
            }
        }
    }

    public Parcelable onSaveInstanceState() {
        if (this.mMenuView == null) {
            return null;
        }
        Bundle state = new Bundle();
        saveHierarchyState(state);
        int i = this.mOpenSubMenuId;
        if (i > 0) {
            state.putInt(OPEN_SUBMENU_KEY, i);
        }
        return state;
    }

    public void onRestoreInstanceState(Parcelable state) {
        restoreHierarchyState((Bundle) state);
    }
}
