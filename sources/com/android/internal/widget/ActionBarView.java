package com.android.internal.widget;

import android.animation.LayoutTransition;
import android.app.ActionBar;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Layout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.CollapsibleActionView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.Window.Callback;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ActionMenuPresenter;
import android.widget.ActionMenuView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.view.menu.ActionMenuItem;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuItemImpl;
import com.android.internal.view.menu.MenuPresenter;
import com.android.internal.view.menu.MenuView;
import com.android.internal.view.menu.SubMenuBuilder;

public class ActionBarView extends AbsActionBarView implements DecorToolbar {
    private static final int DEFAULT_CUSTOM_GRAVITY = 8388627;
    public static final int DISPLAY_DEFAULT = 0;
    private static final int DISPLAY_RELAYOUT_MASK = 63;
    private static final String TAG = "ActionBarView";
    private ActionBarContextView mContextView;
    private View mCustomNavView;
    private int mDefaultUpDescription = R.string.action_bar_up_description;
    private int mDisplayOptions = -1;
    View mExpandedActionView;
    private final OnClickListener mExpandedActionViewUpListener = new OnClickListener() {
        public void onClick(View v) {
            MenuItemImpl item = ActionBarView.this.mExpandedMenuPresenter.mCurrentExpandedItem;
            if (item != null) {
                item.collapseActionView();
            }
        }
    };
    private HomeView mExpandedHomeLayout;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private CharSequence mHomeDescription;
    private int mHomeDescriptionRes;
    private HomeView mHomeLayout;
    private Drawable mIcon;
    private boolean mIncludeTabs;
    private final int mIndeterminateProgressStyle;
    private ProgressBar mIndeterminateProgressView;
    private boolean mIsCollapsible;
    private int mItemPadding;
    private LinearLayout mListNavLayout;
    private Drawable mLogo;
    private ActionMenuItem mLogoNavItem;
    private boolean mMenuPrepared;
    private OnItemSelectedListener mNavItemSelectedListener;
    private int mNavigationMode;
    private MenuBuilder mOptionsMenu;
    private int mProgressBarPadding;
    private final int mProgressStyle;
    private ProgressBar mProgressView;
    private Spinner mSpinner;
    private SpinnerAdapter mSpinnerAdapter;
    private CharSequence mSubtitle;
    private final int mSubtitleStyleRes;
    private TextView mSubtitleView;
    private ScrollingTabContainerView mTabScrollView;
    private Runnable mTabSelector;
    private CharSequence mTitle;
    private LinearLayout mTitleLayout;
    private final int mTitleStyleRes;
    private TextView mTitleView;
    private final OnClickListener mUpClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (ActionBarView.this.mMenuPrepared) {
                ActionBarView.this.mWindowCallback.onMenuItemSelected(0, ActionBarView.this.mLogoNavItem);
            }
        }
    };
    private ViewGroup mUpGoerFive;
    private boolean mUserTitle;
    private boolean mWasHomeEnabled;
    Callback mWindowCallback;

    private class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        private ExpandedActionViewMenuPresenter() {
        }

        /* synthetic */ ExpandedActionViewMenuPresenter(ActionBarView x0, AnonymousClass1 x1) {
            this();
        }

        public void initForMenu(Context context, MenuBuilder menu) {
            MenuBuilder menuBuilder = this.mMenu;
            if (menuBuilder != null) {
                MenuItemImpl menuItemImpl = this.mCurrentExpandedItem;
                if (menuItemImpl != null) {
                    menuBuilder.collapseItemActionView(menuItemImpl);
                }
            }
            this.mMenu = menu;
        }

        public MenuView getMenuView(ViewGroup root) {
            return null;
        }

        public void updateMenuView(boolean cleared) {
            if (this.mCurrentExpandedItem != null) {
                boolean found = false;
                int count = this.mMenu;
                if (count != 0) {
                    count = count.size();
                    for (int i = 0; i < count; i++) {
                        if (this.mMenu.getItem(i) == this.mCurrentExpandedItem) {
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    collapseItemActionView(this.mMenu, this.mCurrentExpandedItem);
                }
            }
        }

        public void setCallback(MenuPresenter.Callback cb) {
        }

        public boolean onSubMenuSelected(SubMenuBuilder subMenu) {
            return false;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
        }

        public boolean flagActionItems() {
            return false;
        }

        public boolean expandItemActionView(MenuBuilder menu, MenuItemImpl item) {
            ActionBarView.this.mExpandedActionView = item.getActionView();
            ActionBarView.this.mExpandedHomeLayout.setIcon(ActionBarView.this.mIcon.getConstantState().newDrawable(ActionBarView.this.getResources()));
            this.mCurrentExpandedItem = item;
            ViewParent parent = ActionBarView.this.mExpandedActionView.getParent();
            ViewParent viewParent = ActionBarView.this;
            if (parent != viewParent) {
                viewParent.addView(viewParent.mExpandedActionView);
            }
            if (ActionBarView.this.mExpandedHomeLayout.getParent() != ActionBarView.this.mUpGoerFive) {
                ActionBarView.this.mUpGoerFive.addView(ActionBarView.this.mExpandedHomeLayout);
            }
            ActionBarView.this.mHomeLayout.setVisibility(8);
            if (ActionBarView.this.mTitleLayout != null) {
                ActionBarView.this.mTitleLayout.setVisibility(8);
            }
            if (ActionBarView.this.mTabScrollView != null) {
                ActionBarView.this.mTabScrollView.setVisibility(8);
            }
            if (ActionBarView.this.mSpinner != null) {
                ActionBarView.this.mSpinner.setVisibility(8);
            }
            if (ActionBarView.this.mCustomNavView != null) {
                ActionBarView.this.mCustomNavView.setVisibility(8);
            }
            ActionBarView.this.setHomeButtonEnabled(false, false);
            ActionBarView.this.requestLayout();
            item.setActionViewExpanded(true);
            if (ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) ActionBarView.this.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }

        public boolean collapseItemActionView(MenuBuilder menu, MenuItemImpl item) {
            if (ActionBarView.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) ActionBarView.this.mExpandedActionView).onActionViewCollapsed();
            }
            ActionBarView actionBarView = ActionBarView.this;
            actionBarView.removeView(actionBarView.mExpandedActionView);
            ActionBarView.this.mUpGoerFive.removeView(ActionBarView.this.mExpandedHomeLayout);
            actionBarView = ActionBarView.this;
            actionBarView.mExpandedActionView = null;
            if ((actionBarView.mDisplayOptions & 2) != 0) {
                ActionBarView.this.mHomeLayout.setVisibility(0);
            }
            if ((ActionBarView.this.mDisplayOptions & 8) != 0) {
                if (ActionBarView.this.mTitleLayout == null) {
                    ActionBarView.this.initTitle();
                } else {
                    ActionBarView.this.mTitleLayout.setVisibility(0);
                }
            }
            if (ActionBarView.this.mTabScrollView != null) {
                ActionBarView.this.mTabScrollView.setVisibility(0);
            }
            if (ActionBarView.this.mSpinner != null) {
                ActionBarView.this.mSpinner.setVisibility(0);
            }
            if (ActionBarView.this.mCustomNavView != null) {
                ActionBarView.this.mCustomNavView.setVisibility(0);
            }
            ActionBarView.this.mExpandedHomeLayout.setIcon(null);
            this.mCurrentExpandedItem = null;
            actionBarView = ActionBarView.this;
            actionBarView.setHomeButtonEnabled(actionBarView.mWasHomeEnabled);
            ActionBarView.this.requestLayout();
            item.setActionViewExpanded(false);
            return true;
        }

        public int getId() {
            return 0;
        }

        public Parcelable onSaveInstanceState() {
            return null;
        }

        public void onRestoreInstanceState(Parcelable state) {
        }
    }

    private static class HomeView extends FrameLayout {
        private static final long DEFAULT_TRANSITION_DURATION = 150;
        private Drawable mDefaultUpIndicator;
        private ImageView mIconView;
        private int mStartOffset;
        private Drawable mUpIndicator;
        private int mUpIndicatorRes;
        private ImageView mUpView;
        private int mUpWidth;

        public HomeView(Context context) {
            this(context, null);
        }

        public HomeView(Context context, AttributeSet attrs) {
            super(context, attrs);
            LayoutTransition t = getLayoutTransition();
            if (t != null) {
                t.setDuration(DEFAULT_TRANSITION_DURATION);
            }
        }

        public void setShowUp(boolean isUp) {
            this.mUpView.setVisibility(isUp ? 0 : 8);
        }

        public void setShowIcon(boolean showIcon) {
            this.mIconView.setVisibility(showIcon ? 0 : 8);
        }

        public void setIcon(Drawable icon) {
            this.mIconView.setImageDrawable(icon);
        }

        public void setUpIndicator(Drawable d) {
            this.mUpIndicator = d;
            this.mUpIndicatorRes = 0;
            updateUpIndicator();
        }

        public void setDefaultUpIndicator(Drawable d) {
            this.mDefaultUpIndicator = d;
            updateUpIndicator();
        }

        public void setUpIndicator(int resId) {
            this.mUpIndicatorRes = resId;
            this.mUpIndicator = null;
            updateUpIndicator();
        }

        private void updateUpIndicator() {
            Drawable drawable = this.mUpIndicator;
            if (drawable != null) {
                this.mUpView.setImageDrawable(drawable);
            } else if (this.mUpIndicatorRes != 0) {
                this.mUpView.setImageDrawable(getContext().getDrawable(this.mUpIndicatorRes));
            } else {
                this.mUpView.setImageDrawable(this.mDefaultUpIndicator);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onConfigurationChanged(Configuration newConfig) {
            super.onConfigurationChanged(newConfig);
            if (this.mUpIndicatorRes != 0) {
                updateUpIndicator();
            }
        }

        public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
            onPopulateAccessibilityEvent(event);
            return true;
        }

        public void onPopulateAccessibilityEventInternal(AccessibilityEvent event) {
            super.onPopulateAccessibilityEventInternal(event);
            CharSequence cdesc = getContentDescription();
            if (!TextUtils.isEmpty(cdesc)) {
                event.getText().add(cdesc);
            }
        }

        public boolean dispatchHoverEvent(MotionEvent event) {
            return onHoverEvent(event);
        }

        /* Access modifiers changed, original: protected */
        public void onFinishInflate() {
            this.mUpView = (ImageView) findViewById(R.id.up);
            this.mIconView = (ImageView) findViewById(16908332);
            this.mDefaultUpIndicator = this.mUpView.getDrawable();
        }

        public int getStartOffset() {
            return this.mUpView.getVisibility() == 8 ? this.mStartOffset : 0;
        }

        public int getUpWidth() {
            return this.mUpWidth;
        }

        /* Access modifiers changed, original: protected */
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            measureChildWithMargins(this.mUpView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            LayoutParams upLp = (LayoutParams) this.mUpView.getLayoutParams();
            int upMargins = upLp.leftMargin + upLp.rightMargin;
            this.mUpWidth = this.mUpView.getMeasuredWidth();
            this.mStartOffset = this.mUpWidth + upMargins;
            int width = this.mUpView.getVisibility() == 8 ? 0 : this.mStartOffset;
            int height = (upLp.topMargin + this.mUpView.getMeasuredHeight()) + upLp.bottomMargin;
            if (this.mIconView.getVisibility() != 8) {
                measureChildWithMargins(this.mIconView, widthMeasureSpec, width, heightMeasureSpec, 0);
                LayoutParams iconLp = (LayoutParams) this.mIconView.getLayoutParams();
                width += (iconLp.leftMargin + this.mIconView.getMeasuredWidth()) + iconLp.rightMargin;
                height = Math.max(height, (iconLp.topMargin + this.mIconView.getMeasuredHeight()) + iconLp.bottomMargin);
            } else if (upMargins < 0) {
                width -= upMargins;
            }
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int widthSize = MeasureSpec.getSize(widthMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (widthMode == Integer.MIN_VALUE) {
                width = Math.min(width, widthSize);
            } else if (widthMode == 1073741824) {
                width = widthSize;
            }
            if (heightMode == Integer.MIN_VALUE) {
                height = Math.min(height, heightSize);
            } else if (heightMode == 1073741824) {
                height = heightSize;
            }
            setMeasuredDimension(width, height);
        }

        /* Access modifiers changed, original: protected */
        public void onLayout(boolean changed, int l, int t, int r, int b) {
            LayoutParams upLp;
            int upHeight;
            int upWidth;
            int upTop;
            int upBottom;
            int upRight;
            int r2;
            int l2;
            int iconRight;
            int vCenter = (b - t) / 2;
            boolean isLayoutRtl = isLayoutRtl();
            int width = getWidth();
            int upOffset = 0;
            if (this.mUpView.getVisibility() != 8) {
                int upLeft;
                upLp = (LayoutParams) this.mUpView.getLayoutParams();
                upHeight = this.mUpView.getMeasuredHeight();
                upWidth = this.mUpView.getMeasuredWidth();
                upOffset = (upLp.leftMargin + upWidth) + upLp.rightMargin;
                upTop = vCenter - (upHeight / 2);
                upBottom = upTop + upHeight;
                if (isLayoutRtl) {
                    upRight = width;
                    upLeft = upRight - upWidth;
                    r2 = r - upOffset;
                    l2 = l;
                } else {
                    upRight = upWidth;
                    upLeft = 0;
                    l2 = l + upOffset;
                    r2 = r;
                }
                this.mUpView.layout(upLeft, upTop, upRight, upBottom);
            } else {
                l2 = l;
                r2 = r;
            }
            upLp = (LayoutParams) this.mIconView.getLayoutParams();
            upHeight = this.mIconView.getMeasuredHeight();
            upWidth = this.mIconView.getMeasuredWidth();
            upTop = (r2 - l2) / 2;
            upBottom = Math.max(upLp.topMargin, vCenter - (upHeight / 2));
            upRight = upBottom + upHeight;
            int delta = Math.max(upLp.getMarginStart(), upTop - (upWidth / 2));
            int iconLeft;
            if (isLayoutRtl) {
                iconRight = (width - upOffset) - delta;
                iconLeft = vCenter;
                vCenter = iconRight;
                iconRight -= upWidth;
            } else {
                iconLeft = upOffset + delta;
                iconRight = iconLeft + upWidth;
                int i = iconLeft;
                vCenter = iconRight;
                iconRight = i;
            }
            this.mIconView.layout(iconRight, upBottom, vCenter, upRight);
        }
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
        int expandedMenuItemId;
        boolean isOverflowOpen;

        /* synthetic */ SavedState(Parcel x0, AnonymousClass1 x1) {
            this(x0);
        }

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            this.expandedMenuItemId = in.readInt();
            this.isOverflowOpen = in.readInt() != 0;
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.expandedMenuItemId);
            out.writeInt(this.isOverflowOpen);
        }
    }

    public ActionBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundResource(0);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionBar, 16843470, 0);
        this.mNavigationMode = a.getInt(7, 0);
        this.mTitle = a.getText(5);
        this.mSubtitle = a.getText(9);
        this.mLogo = a.getDrawable(6);
        this.mIcon = a.getDrawable(0);
        LayoutInflater inflater = LayoutInflater.from(context);
        int homeResId = a.getResourceId(16, R.layout.action_bar_home);
        this.mUpGoerFive = (ViewGroup) inflater.inflate((int) R.layout.action_bar_up_container, this, false);
        this.mHomeLayout = (HomeView) inflater.inflate(homeResId, this.mUpGoerFive, false);
        this.mExpandedHomeLayout = (HomeView) inflater.inflate(homeResId, this.mUpGoerFive, false);
        this.mExpandedHomeLayout.setShowUp(true);
        this.mExpandedHomeLayout.setOnClickListener(this.mExpandedActionViewUpListener);
        this.mExpandedHomeLayout.setContentDescription(getResources().getText(this.mDefaultUpDescription));
        Drawable upBackground = this.mUpGoerFive.getBackground();
        if (upBackground != null) {
            this.mExpandedHomeLayout.setBackground(upBackground.getConstantState().newDrawable());
        }
        this.mExpandedHomeLayout.setEnabled(true);
        this.mExpandedHomeLayout.setFocusable(true);
        this.mTitleStyleRes = a.getResourceId(11, 0);
        this.mSubtitleStyleRes = a.getResourceId(12, 0);
        this.mProgressStyle = a.getResourceId(1, 0);
        this.mIndeterminateProgressStyle = a.getResourceId(14, 0);
        this.mProgressBarPadding = a.getDimensionPixelOffset(15, 0);
        this.mItemPadding = a.getDimensionPixelOffset(17, 0);
        setDisplayOptions(a.getInt(8, 0));
        int customNavId = a.getResourceId(10, 0);
        if (customNavId != 0) {
            this.mCustomNavView = inflater.inflate(customNavId, this, false);
            this.mNavigationMode = 0;
            setDisplayOptions(16 | this.mDisplayOptions);
        }
        this.mContentHeight = a.getLayoutDimension(4, 0);
        a.recycle();
        this.mLogoNavItem = new ActionMenuItem(context, 0, 16908332, 0, 0, this.mTitle);
        this.mUpGoerFive.setOnClickListener(this.mUpClickListener);
        this.mUpGoerFive.setClickable(true);
        this.mUpGoerFive.setFocusable(true);
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.mTitleView = null;
        this.mSubtitleView = null;
        LinearLayout linearLayout = this.mTitleLayout;
        if (linearLayout != null) {
            ViewParent parent = linearLayout.getParent();
            ViewParent viewParent = this.mUpGoerFive;
            if (parent == viewParent) {
                viewParent.removeView(this.mTitleLayout);
            }
        }
        this.mTitleLayout = null;
        if ((this.mDisplayOptions & 8) != 0) {
            initTitle();
        }
        int i = this.mHomeDescriptionRes;
        if (i != 0) {
            setNavigationContentDescription(i);
        }
        ViewGroup.LayoutParams lp = this.mTabScrollView;
        if (lp != null && this.mIncludeTabs) {
            lp = lp.getLayoutParams();
            if (lp != null) {
                lp.width = -2;
                lp.height = -1;
            }
            this.mTabScrollView.setAllowCollapse(true);
        }
    }

    public void setWindowCallback(Callback cb) {
        this.mWindowCallback = cb;
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mTabSelector);
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.hideOverflowMenu();
            this.mActionMenuPresenter.hideSubMenus();
        }
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public void initProgress() {
        this.mProgressView = new ProgressBar(this.mContext, null, 0, this.mProgressStyle);
        this.mProgressView.setId(R.id.progress_horizontal);
        this.mProgressView.setMax(10000);
        this.mProgressView.setVisibility(8);
        addView(this.mProgressView);
    }

    public void initIndeterminateProgress() {
        this.mIndeterminateProgressView = new ProgressBar(this.mContext, null, 0, this.mIndeterminateProgressStyle);
        this.mIndeterminateProgressView.setId(R.id.progress_circular);
        this.mIndeterminateProgressView.setVisibility(8);
        addView(this.mIndeterminateProgressView);
    }

    public void setSplitToolbar(boolean splitActionBar) {
        if (this.mSplitActionBar != splitActionBar) {
            if (this.mMenuView != null) {
                ViewGroup oldParent = (ViewGroup) this.mMenuView.getParent();
                if (oldParent != null) {
                    oldParent.removeView(this.mMenuView);
                }
                if (splitActionBar) {
                    if (this.mSplitView != null) {
                        this.mSplitView.addView(this.mMenuView);
                    }
                    this.mMenuView.getLayoutParams().width = -1;
                } else {
                    addView(this.mMenuView);
                    this.mMenuView.getLayoutParams().width = -2;
                }
                this.mMenuView.requestLayout();
            }
            if (this.mSplitView != null) {
                this.mSplitView.setVisibility(splitActionBar ? 0 : 8);
            }
            if (this.mActionMenuPresenter != null) {
                if (splitActionBar) {
                    this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
                    this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
                    this.mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
                } else {
                    this.mActionMenuPresenter.setExpandedActionViewsExclusive(getResources().getBoolean(R.bool.action_bar_expanded_action_views_exclusive));
                }
            }
            super.setSplitToolbar(splitActionBar);
        }
    }

    public boolean isSplit() {
        return this.mSplitActionBar;
    }

    public boolean canSplit() {
        return true;
    }

    public boolean hasEmbeddedTabs() {
        return this.mIncludeTabs;
    }

    public void setEmbeddedTabView(ScrollingTabContainerView tabs) {
        ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
        if (scrollingTabContainerView != null) {
            removeView(scrollingTabContainerView);
        }
        this.mTabScrollView = tabs;
        this.mIncludeTabs = tabs != null;
        if (this.mIncludeTabs && this.mNavigationMode == 2) {
            addView(this.mTabScrollView);
            ViewGroup.LayoutParams lp = this.mTabScrollView.getLayoutParams();
            lp.width = -2;
            lp.height = -1;
            tabs.setAllowCollapse(true);
        }
    }

    public void setMenuPrepared() {
        this.mMenuPrepared = true;
    }

    public void setMenu(Menu menu, MenuPresenter.Callback cb) {
        Menu menu2 = this.mOptionsMenu;
        if (menu != menu2) {
            ActionMenuView menuView;
            if (menu2 != null) {
                menu2.removeMenuPresenter(this.mActionMenuPresenter);
                this.mOptionsMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
            }
            MenuBuilder builder = (MenuBuilder) menu;
            this.mOptionsMenu = builder;
            if (this.mMenuView != null) {
                ViewGroup oldParent = (ViewGroup) this.mMenuView.getParent();
                if (oldParent != null) {
                    oldParent.removeView(this.mMenuView);
                }
            }
            if (this.mActionMenuPresenter == null) {
                this.mActionMenuPresenter = new ActionMenuPresenter(this.mContext);
                this.mActionMenuPresenter.setCallback(cb);
                this.mActionMenuPresenter.setId(R.id.action_menu_presenter);
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(this, null);
            }
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(-2, -1);
            ViewGroup oldParent2;
            if (this.mSplitActionBar) {
                this.mActionMenuPresenter.setExpandedActionViewsExclusive(false);
                this.mActionMenuPresenter.setWidthLimit(getContext().getResources().getDisplayMetrics().widthPixels, true);
                this.mActionMenuPresenter.setItemLimit(Integer.MAX_VALUE);
                layoutParams.width = -1;
                layoutParams.height = -2;
                configPresenters(builder);
                menuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                if (this.mSplitView != null) {
                    oldParent2 = (ViewGroup) menuView.getParent();
                    if (!(oldParent2 == null || oldParent2 == this.mSplitView)) {
                        oldParent2.removeView(menuView);
                    }
                    menuView.setVisibility(getAnimatedVisibility());
                    this.mSplitView.addView((View) menuView, layoutParams);
                } else {
                    menuView.setLayoutParams(layoutParams);
                }
            } else {
                this.mActionMenuPresenter.setExpandedActionViewsExclusive(getResources().getBoolean(R.bool.action_bar_expanded_action_views_exclusive));
                configPresenters(builder);
                menuView = (ActionMenuView) this.mActionMenuPresenter.getMenuView(this);
                oldParent2 = (ViewGroup) menuView.getParent();
                if (!(oldParent2 == null || oldParent2 == this)) {
                    oldParent2.removeView(menuView);
                }
                addView((View) menuView, layoutParams);
            }
            this.mMenuView = menuView;
        }
    }

    private void configPresenters(MenuBuilder builder) {
        if (builder != null) {
            builder.addMenuPresenter(this.mActionMenuPresenter, this.mPopupContext);
            builder.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
            return;
        }
        this.mActionMenuPresenter.initForMenu(this.mPopupContext, null);
        this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
        this.mActionMenuPresenter.updateMenuView(true);
        this.mExpandedMenuPresenter.updateMenuView(true);
    }

    public boolean hasExpandedActionView() {
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        return (expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null) ? false : true;
    }

    public void collapseActionView() {
        MenuItemImpl item;
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (expandedActionViewMenuPresenter == null) {
            item = null;
        } else {
            item = expandedActionViewMenuPresenter.mCurrentExpandedItem;
        }
        if (item != null) {
            item.collapseActionView();
        }
    }

    public void setCustomView(View view) {
        boolean showCustom = (this.mDisplayOptions & 16) != 0;
        View view2 = this.mCustomNavView;
        if (view2 != null && showCustom) {
            removeView(view2);
        }
        this.mCustomNavView = view;
        view2 = this.mCustomNavView;
        if (view2 != null && showCustom) {
            addView(view2);
        }
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public void setTitle(CharSequence title) {
        this.mUserTitle = true;
        setTitleImpl(title);
    }

    public void setWindowTitle(CharSequence title) {
        if (!this.mUserTitle) {
            setTitleImpl(title);
        }
    }

    private void setTitleImpl(CharSequence title) {
        this.mTitle = title;
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(title);
            int i = 8;
            boolean visible = (this.mExpandedActionView != null || (this.mDisplayOptions & 8) == 0 || (TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mSubtitle))) ? false : true;
            LinearLayout linearLayout = this.mTitleLayout;
            if (visible) {
                i = 0;
            }
            linearLayout.setVisibility(i);
        }
        ActionMenuItem actionMenuItem = this.mLogoNavItem;
        if (actionMenuItem != null) {
            actionMenuItem.setTitle(title);
        }
        updateHomeAccessibility(this.mUpGoerFive.isEnabled());
    }

    public CharSequence getSubtitle() {
        return this.mSubtitle;
    }

    public void setSubtitle(CharSequence subtitle) {
        this.mSubtitle = subtitle;
        TextView textView = this.mSubtitleView;
        if (textView != null) {
            textView.setText(subtitle);
            int i = 0;
            this.mSubtitleView.setVisibility(subtitle != null ? 0 : 8);
            boolean visible = (this.mExpandedActionView != null || (this.mDisplayOptions & 8) == 0 || (TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mSubtitle))) ? false : true;
            LinearLayout linearLayout = this.mTitleLayout;
            if (!visible) {
                i = 8;
            }
            linearLayout.setVisibility(i);
        }
        updateHomeAccessibility(this.mUpGoerFive.isEnabled());
    }

    public void setHomeButtonEnabled(boolean enable) {
        setHomeButtonEnabled(enable, true);
    }

    private void setHomeButtonEnabled(boolean enable, boolean recordState) {
        if (recordState) {
            this.mWasHomeEnabled = enable;
        }
        if (this.mExpandedActionView == null) {
            this.mUpGoerFive.setEnabled(enable);
            this.mUpGoerFive.setFocusable(enable);
            updateHomeAccessibility(enable);
        }
    }

    private void updateHomeAccessibility(boolean homeEnabled) {
        if (homeEnabled) {
            this.mUpGoerFive.setImportantForAccessibility(0);
            this.mUpGoerFive.setContentDescription(buildHomeContentDescription());
            return;
        }
        this.mUpGoerFive.setContentDescription(null);
        this.mUpGoerFive.setImportantForAccessibility(2);
    }

    private CharSequence buildHomeContentDescription() {
        CharSequence homeDesc;
        if (this.mHomeDescription != null) {
            homeDesc = this.mHomeDescription;
        } else if ((this.mDisplayOptions & 4) != 0) {
            homeDesc = this.mContext.getResources().getText(this.mDefaultUpDescription);
        } else {
            homeDesc = this.mContext.getResources().getText(R.string.action_bar_home_description);
        }
        CharSequence title = getTitle();
        CharSequence subtitle = getSubtitle();
        if (TextUtils.isEmpty(title)) {
            return homeDesc;
        }
        String result;
        if (TextUtils.isEmpty(subtitle)) {
            result = getResources().getString(R.string.action_bar_home_description_format, title, homeDesc);
        } else {
            result = getResources().getString(R.string.action_bar_home_subtitle_description_format, title, subtitle, homeDesc);
        }
        return result;
    }

    public void setDisplayOptions(int options) {
        int i = this.mDisplayOptions;
        int i2 = -1;
        if (i != -1) {
            i2 = options ^ i;
        }
        i = i2;
        this.mDisplayOptions = options;
        if ((i & 63) != 0) {
            boolean setUp;
            if ((i & 4) != 0) {
                setUp = (options & 4) != 0;
                this.mHomeLayout.setShowUp(setUp);
                if (setUp) {
                    setHomeButtonEnabled(true);
                }
            }
            if ((i & 1) != 0) {
                setUp = (this.mLogo == null || (options & 1) == 0) ? false : true;
                this.mHomeLayout.setIcon(setUp ? this.mLogo : this.mIcon);
            }
            if ((i & 8) != 0) {
                if ((options & 8) != 0) {
                    initTitle();
                } else {
                    this.mUpGoerFive.removeView(this.mTitleLayout);
                }
            }
            setUp = (options & 2) != 0;
            boolean titleUp = !setUp && ((this.mDisplayOptions & 4) != 0);
            this.mHomeLayout.setShowIcon(setUp);
            int homeVis = ((setUp || titleUp) && this.mExpandedActionView == null) ? 0 : 8;
            this.mHomeLayout.setVisibility(homeVis);
            if ((i & 16) != 0) {
                View view = this.mCustomNavView;
                if (view != null) {
                    if ((options & 16) != 0) {
                        addView(view);
                    } else {
                        removeView(view);
                    }
                }
            }
            if (!(this.mTitleLayout == null || (i & 32) == 0)) {
                if ((options & 32) != 0) {
                    this.mTitleView.setSingleLine(false);
                    this.mTitleView.setMaxLines(2);
                } else {
                    this.mTitleView.setMaxLines(1);
                    this.mTitleView.setSingleLine(true);
                }
            }
            requestLayout();
        } else {
            invalidate();
        }
        updateHomeAccessibility(this.mUpGoerFive.isEnabled());
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
        if (icon != null && ((this.mDisplayOptions & 1) == 0 || this.mLogo == null)) {
            this.mHomeLayout.setIcon(icon);
        }
        if (this.mExpandedActionView != null) {
            this.mExpandedHomeLayout.setIcon(this.mIcon.getConstantState().newDrawable(getResources()));
        }
    }

    public void setIcon(int resId) {
        setIcon(resId != 0 ? this.mContext.getDrawable(resId) : null);
    }

    public boolean hasIcon() {
        return this.mIcon != null;
    }

    public void setLogo(Drawable logo) {
        this.mLogo = logo;
        if (logo != null && (this.mDisplayOptions & 1) != 0) {
            this.mHomeLayout.setIcon(logo);
        }
    }

    public void setLogo(int resId) {
        setLogo(resId != 0 ? this.mContext.getDrawable(resId) : null);
    }

    public boolean hasLogo() {
        return this.mLogo != null;
    }

    public void setNavigationMode(int mode) {
        int oldMode = this.mNavigationMode;
        if (mode != oldMode) {
            if (oldMode == 1) {
                LinearLayout linearLayout = this.mListNavLayout;
                if (linearLayout != null) {
                    removeView(linearLayout);
                }
            } else if (oldMode == 2) {
                ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
                if (scrollingTabContainerView != null && this.mIncludeTabs) {
                    removeView(scrollingTabContainerView);
                }
            }
            if (mode == 1) {
                if (this.mSpinner == null) {
                    this.mSpinner = new Spinner(this.mContext, null, 16843479);
                    this.mSpinner.setId(R.id.action_bar_spinner);
                    this.mListNavLayout = new LinearLayout(this.mContext, null, 16843508);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-2, -1);
                    params.gravity = 17;
                    this.mListNavLayout.addView((View) this.mSpinner, (ViewGroup.LayoutParams) params);
                }
                SpinnerAdapter adapter = this.mSpinner.getAdapter();
                SpinnerAdapter spinnerAdapter = this.mSpinnerAdapter;
                if (adapter != spinnerAdapter) {
                    this.mSpinner.setAdapter(spinnerAdapter);
                }
                this.mSpinner.setOnItemSelectedListener(this.mNavItemSelectedListener);
                addView(this.mListNavLayout);
            } else if (mode == 2) {
                ScrollingTabContainerView scrollingTabContainerView2 = this.mTabScrollView;
                if (scrollingTabContainerView2 != null && this.mIncludeTabs) {
                    addView(scrollingTabContainerView2);
                }
            }
            this.mNavigationMode = mode;
            requestLayout();
        }
    }

    public void setDropdownParams(SpinnerAdapter adapter, OnItemSelectedListener l) {
        this.mSpinnerAdapter = adapter;
        this.mNavItemSelectedListener = l;
        Spinner spinner = this.mSpinner;
        if (spinner != null) {
            spinner.setAdapter(adapter);
            this.mSpinner.setOnItemSelectedListener(l);
        }
    }

    public int getDropdownItemCount() {
        SpinnerAdapter spinnerAdapter = this.mSpinnerAdapter;
        return spinnerAdapter != null ? spinnerAdapter.getCount() : 0;
    }

    public void setDropdownSelectedPosition(int position) {
        this.mSpinner.setSelection(position);
    }

    public int getDropdownSelectedPosition() {
        return this.mSpinner.getSelectedItemPosition();
    }

    public View getCustomView() {
        return this.mCustomNavView;
    }

    public int getNavigationMode() {
        return this.mNavigationMode;
    }

    public int getDisplayOptions() {
        return this.mDisplayOptions;
    }

    public ViewGroup getViewGroup() {
        return this;
    }

    /* Access modifiers changed, original: protected */
    public ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new ActionBar.LayoutParams((int) DEFAULT_CUSTOM_GRAVITY);
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mUpGoerFive.addView(this.mHomeLayout, 0);
        addView(this.mUpGoerFive);
        ViewParent parent = this.mCustomNavView;
        if (parent != null && (this.mDisplayOptions & 16) != 0) {
            ActionBarView parent2 = parent.getParent();
            if (parent2 != this) {
                if (parent2 instanceof ViewGroup) {
                    parent2.removeView(this.mCustomNavView);
                }
                addView(this.mCustomNavView);
            }
        }
    }

    private void initTitle() {
        if (this.mTitleLayout == null) {
            this.mTitleLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate((int) R.layout.action_bar_title_item, (ViewGroup) this, false);
            this.mTitleView = (TextView) this.mTitleLayout.findViewById(R.id.action_bar_title);
            this.mSubtitleView = (TextView) this.mTitleLayout.findViewById(R.id.action_bar_subtitle);
            int i = this.mTitleStyleRes;
            if (i != 0) {
                this.mTitleView.setTextAppearance(i);
            }
            CharSequence charSequence = this.mTitle;
            if (charSequence != null) {
                this.mTitleView.setText(charSequence);
            }
            i = this.mSubtitleStyleRes;
            if (i != 0) {
                this.mSubtitleView.setTextAppearance(i);
            }
            charSequence = this.mSubtitle;
            if (charSequence != null) {
                this.mSubtitleView.setText(charSequence);
                this.mSubtitleView.setVisibility(0);
            }
        }
        this.mUpGoerFive.addView(this.mTitleLayout);
        if (this.mExpandedActionView != null || (TextUtils.isEmpty(this.mTitle) && TextUtils.isEmpty(this.mSubtitle))) {
            this.mTitleLayout.setVisibility(8);
        } else {
            this.mTitleLayout.setVisibility(0);
        }
    }

    public void setContextView(ActionBarContextView view) {
        this.mContextView = view;
    }

    public void setCollapsible(boolean collapsible) {
        this.mIsCollapsible = collapsible;
    }

    public boolean isTitleTruncated() {
        Layout titleLayout = this.mTitleView;
        if (titleLayout == null) {
            return false;
        }
        titleLayout = titleLayout.getLayout();
        if (titleLayout == null) {
            return false;
        }
        int lineCount = titleLayout.getLineCount();
        for (int i = 0; i < lineCount; i++) {
            if (titleLayout.getEllipsisCount(i) > 0) {
                return true;
            }
        }
        return false;
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x01fc  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x01f9  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x02b3  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x020a  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x02d0  */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x02f5  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x02de  */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x02fc  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x01f9  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x01fc  */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x020a  */
    /* JADX WARNING: Removed duplicated region for block: B:133:0x02b3  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x02d0  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x02de  */
    /* JADX WARNING: Removed duplicated region for block: B:146:0x02f5  */
    /* JADX WARNING: Removed duplicated region for block: B:149:0x02fc  */
    public void onMeasure(int r34, int r35) {
        /*
        r33 = this;
        r0 = r33;
        r1 = r33.getChildCount();
        r2 = r0.mIsCollapsible;
        r3 = 8;
        r4 = 0;
        if (r2 == 0) goto L_0x0050;
    L_0x000d:
        r2 = 0;
        r5 = 0;
    L_0x000f:
        if (r5 >= r1) goto L_0x0030;
    L_0x0011:
        r6 = r0.getChildAt(r5);
        r7 = r6.getVisibility();
        if (r7 == r3) goto L_0x002d;
    L_0x001b:
        r7 = r0.mMenuView;
        if (r6 != r7) goto L_0x0027;
    L_0x001f:
        r7 = r0.mMenuView;
        r7 = r7.getChildCount();
        if (r7 == 0) goto L_0x002d;
    L_0x0027:
        r7 = r0.mUpGoerFive;
        if (r6 == r7) goto L_0x002d;
    L_0x002b:
        r2 = r2 + 1;
    L_0x002d:
        r5 = r5 + 1;
        goto L_0x000f;
    L_0x0030:
        r5 = r0.mUpGoerFive;
        r5 = r5.getChildCount();
        r6 = 0;
    L_0x0037:
        if (r6 >= r5) goto L_0x004a;
    L_0x0039:
        r7 = r0.mUpGoerFive;
        r7 = r7.getChildAt(r6);
        r8 = r7.getVisibility();
        if (r8 == r3) goto L_0x0047;
    L_0x0045:
        r2 = r2 + 1;
    L_0x0047:
        r6 = r6 + 1;
        goto L_0x0037;
    L_0x004a:
        if (r2 != 0) goto L_0x0050;
    L_0x004c:
        r0.setMeasuredDimension(r4, r4);
        return;
    L_0x0050:
        r2 = android.view.View.MeasureSpec.getMode(r34);
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        if (r2 != r5) goto L_0x034c;
    L_0x0058:
        r6 = android.view.View.MeasureSpec.getMode(r35);
        r7 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        if (r6 != r7) goto L_0x032b;
    L_0x0060:
        r8 = android.view.View.MeasureSpec.getSize(r34);
        r9 = r0.mContentHeight;
        if (r9 < 0) goto L_0x006b;
    L_0x0068:
        r9 = r0.mContentHeight;
        goto L_0x006f;
    L_0x006b:
        r9 = android.view.View.MeasureSpec.getSize(r35);
    L_0x006f:
        r10 = r33.getPaddingTop();
        r11 = r33.getPaddingBottom();
        r10 = r10 + r11;
        r11 = r33.getPaddingLeft();
        r12 = r33.getPaddingRight();
        r13 = r9 - r10;
        r14 = android.view.View.MeasureSpec.makeMeasureSpec(r13, r7);
        r15 = android.view.View.MeasureSpec.makeMeasureSpec(r13, r5);
        r16 = r8 - r11;
        r4 = r16 - r12;
        r16 = r4 / 2;
        r17 = r16;
        r5 = r0.mTitleLayout;
        if (r5 == 0) goto L_0x00a3;
    L_0x0096:
        r5 = r5.getVisibility();
        if (r5 == r3) goto L_0x00a3;
    L_0x009c:
        r5 = r0.mDisplayOptions;
        r5 = r5 & r3;
        if (r5 == 0) goto L_0x00a3;
    L_0x00a1:
        r5 = 1;
        goto L_0x00a4;
    L_0x00a3:
        r5 = 0;
    L_0x00a4:
        r7 = r0.mExpandedActionView;
        if (r7 == 0) goto L_0x00ab;
    L_0x00a8:
        r7 = r0.mExpandedHomeLayout;
        goto L_0x00ad;
    L_0x00ab:
        r7 = r0.mHomeLayout;
    L_0x00ad:
        r3 = r7.getLayoutParams();
        r19 = r2;
        r2 = r3.width;
        if (r2 >= 0) goto L_0x00c2;
    L_0x00b7:
        r2 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r20 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r2);
        r2 = r20;
        r20 = r3;
        goto L_0x00cc;
    L_0x00c2:
        r2 = r3.width;
        r20 = r3;
        r3 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r2 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r3);
    L_0x00cc:
        r7.measure(r2, r15);
        r3 = 0;
        r21 = r2;
        r2 = r7.getVisibility();
        r22 = r3;
        r3 = 8;
        if (r2 == r3) goto L_0x00e4;
    L_0x00dc:
        r2 = r7.getParent();
        r3 = r0.mUpGoerFive;
        if (r2 == r3) goto L_0x00e6;
    L_0x00e4:
        if (r5 == 0) goto L_0x0103;
    L_0x00e6:
        r3 = r7.getMeasuredWidth();
        r2 = r7.getStartOffset();
        r2 = r2 + r3;
        r22 = r3;
        r3 = r4 - r2;
        r23 = r4;
        r4 = 0;
        r3 = java.lang.Math.max(r4, r3);
        r24 = r6;
        r6 = r3 - r2;
        r16 = java.lang.Math.max(r4, r6);
        goto L_0x0109;
    L_0x0103:
        r23 = r4;
        r24 = r6;
        r3 = r23;
    L_0x0109:
        r2 = r0.mMenuView;
        if (r2 == 0) goto L_0x0128;
    L_0x010d:
        r2 = r0.mMenuView;
        r2 = r2.getParent();
        if (r2 != r0) goto L_0x0128;
    L_0x0115:
        r2 = r0.mMenuView;
        r4 = 0;
        r3 = r0.measureChildView(r2, r3, r15, r4);
        r2 = r0.mMenuView;
        r2 = r2.getMeasuredWidth();
        r2 = r17 - r2;
        r17 = java.lang.Math.max(r4, r2);
    L_0x0128:
        r2 = r0.mIndeterminateProgressView;
        if (r2 == 0) goto L_0x014a;
    L_0x012c:
        r2 = r2.getVisibility();
        r4 = 8;
        if (r2 == r4) goto L_0x014a;
    L_0x0134:
        r2 = r0.mIndeterminateProgressView;
        r4 = 0;
        r3 = r0.measureChildView(r2, r3, r14, r4);
        r2 = r0.mIndeterminateProgressView;
        r2 = r2.getMeasuredWidth();
        r2 = r17 - r2;
        r17 = java.lang.Math.max(r4, r2);
        r2 = r17;
        goto L_0x014c;
    L_0x014a:
        r2 = r17;
    L_0x014c:
        r4 = r0.mExpandedActionView;
        r6 = 2;
        if (r4 != 0) goto L_0x01ec;
    L_0x0151:
        r4 = r0.mNavigationMode;
        r17 = r7;
        r7 = 1;
        if (r4 == r7) goto L_0x01a8;
    L_0x0158:
        if (r4 == r6) goto L_0x0160;
    L_0x015a:
        r26 = r11;
        r27 = r12;
        goto L_0x01f2;
    L_0x0160:
        r4 = r0.mTabScrollView;
        if (r4 == 0) goto L_0x01a3;
    L_0x0164:
        r4 = r0.mItemPadding;
        if (r5 == 0) goto L_0x0169;
    L_0x0168:
        r4 = r4 * r6;
    L_0x0169:
        r7 = r3 - r4;
        r6 = 0;
        r3 = java.lang.Math.max(r6, r7);
        r7 = r16 - r4;
        r7 = java.lang.Math.max(r6, r7);
        r6 = r0.mTabScrollView;
        r25 = r4;
        r26 = r11;
        r4 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r11 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r4);
        r27 = r12;
        r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r12 = android.view.View.MeasureSpec.makeMeasureSpec(r13, r4);
        r6.measure(r11, r12);
        r4 = r0.mTabScrollView;
        r4 = r4.getMeasuredWidth();
        r6 = r3 - r4;
        r11 = 0;
        r3 = java.lang.Math.max(r11, r6);
        r6 = r7 - r4;
        r16 = java.lang.Math.max(r11, r6);
        r4 = r16;
        goto L_0x01f4;
    L_0x01a3:
        r26 = r11;
        r27 = r12;
        goto L_0x01f2;
    L_0x01a8:
        r26 = r11;
        r27 = r12;
        r4 = r0.mListNavLayout;
        if (r4 == 0) goto L_0x01f2;
    L_0x01b0:
        r4 = r0.mItemPadding;
        if (r5 == 0) goto L_0x01b6;
    L_0x01b4:
        r6 = 2;
        r4 = r4 * r6;
    L_0x01b6:
        r6 = r3 - r4;
        r7 = 0;
        r3 = java.lang.Math.max(r7, r6);
        r6 = r16 - r4;
        r6 = java.lang.Math.max(r7, r6);
        r7 = r0.mListNavLayout;
        r11 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r12 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r11);
        r25 = r4;
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = android.view.View.MeasureSpec.makeMeasureSpec(r13, r11);
        r7.measure(r12, r4);
        r4 = r0.mListNavLayout;
        r4 = r4.getMeasuredWidth();
        r7 = r3 - r4;
        r11 = 0;
        r3 = java.lang.Math.max(r11, r7);
        r7 = r6 - r4;
        r16 = java.lang.Math.max(r11, r7);
        r4 = r16;
        goto L_0x01f4;
    L_0x01ec:
        r17 = r7;
        r26 = r11;
        r27 = r12;
    L_0x01f2:
        r4 = r16;
    L_0x01f4:
        r6 = 0;
        r7 = r0.mExpandedActionView;
        if (r7 == 0) goto L_0x01fc;
    L_0x01f9:
        r6 = r0.mExpandedActionView;
        goto L_0x0208;
    L_0x01fc:
        r7 = r0.mDisplayOptions;
        r7 = r7 & 16;
        if (r7 == 0) goto L_0x0208;
    L_0x0202:
        r7 = r0.mCustomNavView;
        if (r7 == 0) goto L_0x0208;
    L_0x0206:
        r6 = r0.mCustomNavView;
    L_0x0208:
        if (r6 == 0) goto L_0x02b3;
    L_0x020a:
        r7 = r6.getLayoutParams();
        r7 = r0.generateLayoutParams(r7);
        r11 = r7 instanceof android.app.ActionBar.LayoutParams;
        if (r11 == 0) goto L_0x021a;
    L_0x0216:
        r11 = r7;
        r11 = (android.app.ActionBar.LayoutParams) r11;
        goto L_0x021b;
    L_0x021a:
        r11 = 0;
    L_0x021b:
        r12 = 0;
        r16 = 0;
        if (r11 == 0) goto L_0x0232;
    L_0x0220:
        r25 = r5;
        r5 = r11.leftMargin;
        r28 = r12;
        r12 = r11.rightMargin;
        r12 = r12 + r5;
        r5 = r11.topMargin;
        r28 = r12;
        r12 = r11.bottomMargin;
        r16 = r5 + r12;
        goto L_0x0236;
    L_0x0232:
        r25 = r5;
        r28 = r12;
    L_0x0236:
        r5 = r0.mContentHeight;
        r12 = -2;
        if (r5 > 0) goto L_0x023e;
    L_0x023b:
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        goto L_0x0247;
    L_0x023e:
        r5 = r7.height;
        if (r5 == r12) goto L_0x0245;
    L_0x0242:
        r5 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        goto L_0x0247;
    L_0x0245:
        r5 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r12 = r7.height;
        if (r12 < 0) goto L_0x0253;
    L_0x024c:
        r12 = r7.height;
        r12 = java.lang.Math.min(r12, r13);
        goto L_0x0254;
    L_0x0253:
        r12 = r13;
    L_0x0254:
        r12 = r12 - r16;
        r30 = r13;
        r13 = 0;
        r12 = java.lang.Math.max(r13, r12);
        r13 = r7.width;
        r31 = r14;
        r14 = -2;
        if (r13 == r14) goto L_0x0267;
    L_0x0264:
        r13 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        goto L_0x0269;
    L_0x0267:
        r13 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r14 = r7.width;
        if (r14 < 0) goto L_0x0275;
    L_0x026e:
        r14 = r7.width;
        r14 = java.lang.Math.min(r14, r3);
        goto L_0x0276;
    L_0x0275:
        r14 = r3;
    L_0x0276:
        r14 = r14 - r28;
        r29 = r15;
        r15 = 0;
        r14 = java.lang.Math.max(r15, r14);
        if (r11 == 0) goto L_0x0284;
    L_0x0281:
        r15 = r11.gravity;
        goto L_0x0287;
    L_0x0284:
        r15 = 8388627; // 0x800013 float:1.175497E-38 double:4.1445324E-317;
    L_0x0287:
        r15 = r15 & 7;
        r32 = r11;
        r11 = 1;
        if (r15 != r11) goto L_0x029d;
    L_0x028e:
        r11 = r7.width;
        r18 = r7;
        r7 = -1;
        if (r11 != r7) goto L_0x029f;
    L_0x0295:
        r7 = java.lang.Math.min(r4, r2);
        r11 = 2;
        r14 = r7 * 2;
        goto L_0x029f;
    L_0x029d:
        r18 = r7;
        r7 = android.view.View.MeasureSpec.makeMeasureSpec(r14, r13);
        r11 = android.view.View.MeasureSpec.makeMeasureSpec(r12, r5);
        r6.measure(r7, r11);
        r7 = r6.getMeasuredWidth();
        r7 = r28 + r7;
        r3 = r3 - r7;
        goto L_0x02bb;
    L_0x02b3:
        r25 = r5;
        r30 = r13;
        r31 = r14;
        r29 = r15;
    L_0x02bb:
        r5 = r0.mUpGoerFive;
        r7 = r3 + r22;
        r11 = r0.mContentHeight;
        r12 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r11 = android.view.View.MeasureSpec.makeMeasureSpec(r11, r12);
        r12 = 0;
        r3 = r0.measureChildView(r5, r7, r11, r12);
        r5 = r0.mTitleLayout;
        if (r5 == 0) goto L_0x02da;
    L_0x02d0:
        r5 = r5.getMeasuredWidth();
        r5 = r4 - r5;
        r4 = java.lang.Math.max(r12, r5);
    L_0x02da:
        r5 = r0.mContentHeight;
        if (r5 > 0) goto L_0x02f5;
    L_0x02de:
        r5 = 0;
        r7 = 0;
    L_0x02e0:
        if (r7 >= r1) goto L_0x02f1;
    L_0x02e2:
        r11 = r0.getChildAt(r7);
        r12 = r11.getMeasuredHeight();
        r12 = r12 + r10;
        if (r12 <= r5) goto L_0x02ee;
    L_0x02ed:
        r5 = r12;
    L_0x02ee:
        r7 = r7 + 1;
        goto L_0x02e0;
    L_0x02f1:
        r0.setMeasuredDimension(r8, r5);
        goto L_0x02f8;
    L_0x02f5:
        r0.setMeasuredDimension(r8, r9);
    L_0x02f8:
        r5 = r0.mContextView;
        if (r5 == 0) goto L_0x0303;
    L_0x02fc:
        r7 = r33.getMeasuredHeight();
        r5.setContentHeight(r7);
    L_0x0303:
        r5 = r0.mProgressView;
        if (r5 == 0) goto L_0x032a;
    L_0x0307:
        r5 = r5.getVisibility();
        r7 = 8;
        if (r5 == r7) goto L_0x032a;
    L_0x030f:
        r5 = r0.mProgressView;
        r7 = r0.mProgressBarPadding;
        r11 = 2;
        r7 = r7 * r11;
        r7 = r8 - r7;
        r11 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r7 = android.view.View.MeasureSpec.makeMeasureSpec(r7, r11);
        r11 = r33.getMeasuredHeight();
        r12 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        r11 = android.view.View.MeasureSpec.makeMeasureSpec(r11, r12);
        r5.measure(r7, r11);
    L_0x032a:
        return;
    L_0x032b:
        r19 = r2;
        r2 = new java.lang.IllegalStateException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = r33.getClass();
        r4 = r4.getSimpleName();
        r3.append(r4);
        r4 = " can only be used with android:layout_height=\"wrap_content\"";
        r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
    L_0x034c:
        r19 = r2;
        r2 = new java.lang.IllegalStateException;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = r33.getClass();
        r4 = r4.getSimpleName();
        r3.append(r4);
        r4 = " can only be used with android:layout_width=\"match_parent\" (or fill_parent)";
        r3.append(r4);
        r3 = r3.toString();
        r2.<init>(r3);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.ActionBarView.onMeasure(int, int):void");
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x013d  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x013a  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x014a  */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x026e  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00f2  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x013a  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x013d  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x014a  */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x026e  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:55:0x00f2  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x013d  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x013a  */
    /* JADX WARNING: Removed duplicated region for block: B:73:0x014a  */
    /* JADX WARNING: Removed duplicated region for block: B:135:0x026e  */
    public void onLayout(boolean r28, int r29, int r30, int r31, int r32) {
        /*
        r27 = this;
        r6 = r27;
        r0 = r32 - r30;
        r1 = r27.getPaddingTop();
        r0 = r0 - r1;
        r1 = r27.getPaddingBottom();
        r7 = r0 - r1;
        if (r7 > 0) goto L_0x0012;
    L_0x0011:
        return;
    L_0x0012:
        r8 = r27.isLayoutRtl();
        r9 = 1;
        if (r8 == 0) goto L_0x001b;
    L_0x0019:
        r0 = r9;
        goto L_0x001c;
    L_0x001b:
        r0 = -1;
    L_0x001c:
        r10 = r0;
        if (r8 == 0) goto L_0x0024;
    L_0x001f:
        r0 = r27.getPaddingLeft();
        goto L_0x002b;
    L_0x0024:
        r0 = r31 - r29;
        r1 = r27.getPaddingRight();
        r0 = r0 - r1;
    L_0x002b:
        r11 = r0;
        if (r8 == 0) goto L_0x0036;
    L_0x002e:
        r0 = r31 - r29;
        r1 = r27.getPaddingRight();
        r0 = r0 - r1;
        goto L_0x003a;
    L_0x0036:
        r0 = r27.getPaddingLeft();
    L_0x003a:
        r12 = r0;
        r13 = r27.getPaddingTop();
        r0 = r6.mExpandedActionView;
        if (r0 == 0) goto L_0x0046;
    L_0x0043:
        r0 = r6.mExpandedHomeLayout;
        goto L_0x0048;
    L_0x0046:
        r0 = r6.mHomeLayout;
    L_0x0048:
        r14 = r0;
        r0 = r6.mTitleLayout;
        r15 = 8;
        if (r0 == 0) goto L_0x005c;
    L_0x004f:
        r0 = r0.getVisibility();
        if (r0 == r15) goto L_0x005c;
    L_0x0055:
        r0 = r6.mDisplayOptions;
        r0 = r0 & r15;
        if (r0 == 0) goto L_0x005c;
    L_0x005a:
        r0 = r9;
        goto L_0x005d;
    L_0x005c:
        r0 = 0;
    L_0x005d:
        r16 = r0;
        r0 = 0;
        r1 = r14.getParent();
        r2 = r6.mUpGoerFive;
        if (r1 != r2) goto L_0x007c;
    L_0x0068:
        r1 = r14.getVisibility();
        if (r1 == r15) goto L_0x0074;
    L_0x006e:
        r0 = r14.getStartOffset();
        r5 = r0;
        goto L_0x007d;
    L_0x0074:
        if (r16 == 0) goto L_0x007c;
    L_0x0076:
        r0 = r14.getUpWidth();
        r5 = r0;
        goto L_0x007d;
    L_0x007c:
        r5 = r0;
    L_0x007d:
        r1 = r6.mUpGoerFive;
        r2 = com.android.internal.widget.AbsActionBarView.next(r12, r5, r8);
        r0 = r27;
        r3 = r13;
        r4 = r7;
        r15 = r5;
        r5 = r8;
        r0 = r0.positionChild(r1, r2, r3, r4, r5);
        r12 = r12 + r0;
        r0 = com.android.internal.widget.AbsActionBarView.next(r12, r15, r8);
        r1 = r6.mExpandedActionView;
        r12 = 2;
        if (r1 != 0) goto L_0x00ed;
    L_0x0097:
        r1 = r6.mNavigationMode;
        if (r1 == 0) goto L_0x00ed;
    L_0x009b:
        if (r1 == r9) goto L_0x00c5;
    L_0x009d:
        if (r1 == r12) goto L_0x00a0;
    L_0x009f:
        goto L_0x00ed;
    L_0x00a0:
        r1 = r6.mTabScrollView;
        if (r1 == 0) goto L_0x00ed;
    L_0x00a4:
        if (r16 == 0) goto L_0x00ac;
    L_0x00a6:
        r1 = r6.mItemPadding;
        r0 = com.android.internal.widget.AbsActionBarView.next(r0, r1, r8);
    L_0x00ac:
        r18 = r0;
        r1 = r6.mTabScrollView;
        r0 = r27;
        r2 = r18;
        r3 = r13;
        r4 = r7;
        r5 = r8;
        r0 = r0.positionChild(r1, r2, r3, r4, r5);
        r0 = r18 + r0;
        r1 = r6.mItemPadding;
        r0 = com.android.internal.widget.AbsActionBarView.next(r0, r1, r8);
        r5 = r0;
        goto L_0x00ee;
    L_0x00c5:
        r1 = r6.mListNavLayout;
        if (r1 == 0) goto L_0x00ed;
    L_0x00c9:
        if (r16 == 0) goto L_0x00d4;
    L_0x00cb:
        r1 = r6.mItemPadding;
        r0 = com.android.internal.widget.AbsActionBarView.next(r0, r1, r8);
        r18 = r0;
        goto L_0x00d6;
    L_0x00d4:
        r18 = r0;
    L_0x00d6:
        r1 = r6.mListNavLayout;
        r0 = r27;
        r2 = r18;
        r3 = r13;
        r4 = r7;
        r5 = r8;
        r0 = r0.positionChild(r1, r2, r3, r4, r5);
        r0 = r18 + r0;
        r1 = r6.mItemPadding;
        r0 = com.android.internal.widget.AbsActionBarView.next(r0, r1, r8);
        r5 = r0;
        goto L_0x00ee;
    L_0x00ed:
        r5 = r0;
    L_0x00ee:
        r0 = r6.mMenuView;
        if (r0 == 0) goto L_0x0112;
    L_0x00f2:
        r0 = r6.mMenuView;
        r0 = r0.getParent();
        if (r0 != r6) goto L_0x0112;
    L_0x00fa:
        r1 = r6.mMenuView;
        r18 = r8 ^ 1;
        r0 = r27;
        r2 = r11;
        r3 = r13;
        r4 = r7;
        r12 = r5;
        r5 = r18;
        r0.positionChild(r1, r2, r3, r4, r5);
        r0 = r6.mMenuView;
        r0 = r0.getMeasuredWidth();
        r0 = r0 * r10;
        r11 = r11 + r0;
        goto L_0x0113;
    L_0x0112:
        r12 = r5;
    L_0x0113:
        r0 = r6.mIndeterminateProgressView;
        if (r0 == 0) goto L_0x0133;
    L_0x0117:
        r0 = r0.getVisibility();
        r1 = 8;
        if (r0 == r1) goto L_0x0133;
    L_0x011f:
        r1 = r6.mIndeterminateProgressView;
        r5 = r8 ^ 1;
        r0 = r27;
        r2 = r11;
        r3 = r13;
        r4 = r7;
        r0.positionChild(r1, r2, r3, r4, r5);
        r0 = r6.mIndeterminateProgressView;
        r0 = r0.getMeasuredWidth();
        r0 = r0 * r10;
        r11 = r11 + r0;
    L_0x0133:
        r0 = 0;
        r1 = r6.mExpandedActionView;
        r2 = 16;
        if (r1 == 0) goto L_0x013d;
    L_0x013a:
        r0 = r6.mExpandedActionView;
        goto L_0x0148;
    L_0x013d:
        r1 = r6.mDisplayOptions;
        r1 = r1 & r2;
        if (r1 == 0) goto L_0x0148;
    L_0x0142:
        r1 = r6.mCustomNavView;
        if (r1 == 0) goto L_0x0148;
    L_0x0146:
        r0 = r6.mCustomNavView;
    L_0x0148:
        if (r0 == 0) goto L_0x026a;
    L_0x014a:
        r1 = r27.getLayoutDirection();
        r3 = r0.getLayoutParams();
        r4 = r3 instanceof android.app.ActionBar.LayoutParams;
        if (r4 == 0) goto L_0x015a;
    L_0x0156:
        r4 = r3;
        r4 = (android.app.ActionBar.LayoutParams) r4;
        goto L_0x015b;
    L_0x015a:
        r4 = 0;
    L_0x015b:
        if (r4 == 0) goto L_0x0160;
    L_0x015d:
        r5 = r4.gravity;
        goto L_0x0163;
    L_0x0160:
        r5 = 8388627; // 0x800013 float:1.175497E-38 double:4.1445324E-317;
    L_0x0163:
        r17 = r0.getMeasuredWidth();
        r18 = 0;
        r20 = 0;
        if (r4 == 0) goto L_0x0184;
    L_0x016d:
        r2 = r4.getMarginStart();
        r2 = com.android.internal.widget.AbsActionBarView.next(r12, r2, r8);
        r12 = r4.getMarginEnd();
        r12 = r12 * r10;
        r11 = r11 + r12;
        r12 = r4.topMargin;
        r9 = r4.bottomMargin;
        r20 = r9;
        r18 = r12;
        r12 = r2;
    L_0x0184:
        r2 = 8388615; // 0x800007 float:1.1754953E-38 double:4.1445265E-317;
        r2 = r2 & r5;
        r9 = 1;
        if (r2 != r9) goto L_0x01c4;
    L_0x018b:
        r9 = r6.mRight;
        r23 = r2;
        r2 = r6.mLeft;
        r9 = r9 - r2;
        r9 = r9 - r17;
        r2 = 2;
        r9 = r9 / r2;
        if (r8 == 0) goto L_0x01ae;
    L_0x0198:
        r2 = r9 + r17;
        r24 = r9;
        if (r2 <= r12) goto L_0x01a1;
    L_0x019e:
        r23 = 5;
        goto L_0x01a9;
    L_0x01a1:
        r25 = r2;
        r2 = r24;
        if (r2 >= r11) goto L_0x01a9;
    L_0x01a7:
        r23 = 3;
    L_0x01a9:
        r24 = r3;
        r2 = r23;
        goto L_0x01c3;
    L_0x01ae:
        r2 = r9;
        r24 = r3;
        r3 = r9 + r17;
        if (r2 >= r12) goto L_0x01ba;
    L_0x01b5:
        r23 = 3;
        r2 = r23;
        goto L_0x01c3;
    L_0x01ba:
        if (r3 <= r11) goto L_0x01c1;
    L_0x01bc:
        r23 = 5;
        r2 = r23;
        goto L_0x01c3;
    L_0x01c1:
        r2 = r23;
    L_0x01c3:
        goto L_0x01d0;
    L_0x01c4:
        r23 = r2;
        r24 = r3;
        if (r5 != 0) goto L_0x01ce;
    L_0x01ca:
        r2 = 8388611; // 0x800003 float:1.1754948E-38 double:4.1445245E-317;
        goto L_0x01d0;
    L_0x01ce:
        r2 = r23;
    L_0x01d0:
        r3 = 0;
        r9 = android.view.Gravity.getAbsoluteGravity(r2, r1);
        r23 = r1;
        r1 = 1;
        if (r9 == r1) goto L_0x01f1;
    L_0x01da:
        r1 = 3;
        if (r9 == r1) goto L_0x01ea;
    L_0x01dd:
        r1 = 5;
        if (r9 == r1) goto L_0x01e1;
    L_0x01e0:
        goto L_0x01fc;
    L_0x01e1:
        if (r8 == 0) goto L_0x01e6;
    L_0x01e3:
        r1 = r12 - r17;
        goto L_0x01e8;
    L_0x01e6:
        r1 = r11 - r17;
    L_0x01e8:
        r3 = r1;
        goto L_0x01fc;
    L_0x01ea:
        if (r8 == 0) goto L_0x01ee;
    L_0x01ec:
        r1 = r11;
        goto L_0x01ef;
    L_0x01ee:
        r1 = r12;
    L_0x01ef:
        r3 = r1;
        goto L_0x01fc;
    L_0x01f1:
        r1 = r6.mRight;
        r9 = r6.mLeft;
        r1 = r1 - r9;
        r1 = r1 - r17;
        r9 = 2;
        r3 = r1 / 2;
    L_0x01fc:
        r1 = r5 & 112;
        if (r5 != 0) goto L_0x0202;
    L_0x0200:
        r1 = 16;
    L_0x0202:
        r9 = 0;
        r22 = r2;
        r2 = 16;
        if (r1 == r2) goto L_0x0238;
    L_0x0209:
        r2 = 48;
        if (r1 == r2) goto L_0x022d;
    L_0x020d:
        r2 = 80;
        if (r1 == r2) goto L_0x0216;
    L_0x0211:
        r21 = r1;
        r25 = r4;
        goto L_0x0257;
    L_0x0216:
        r2 = r27.getHeight();
        r21 = r27.getPaddingBottom();
        r2 = r2 - r21;
        r21 = r0.getMeasuredHeight();
        r2 = r2 - r21;
        r9 = r2 - r20;
        r21 = r1;
        r25 = r4;
        goto L_0x0257;
    L_0x022d:
        r2 = r27.getPaddingTop();
        r9 = r2 + r18;
        r21 = r1;
        r25 = r4;
        goto L_0x0257;
    L_0x0238:
        r2 = r27.getPaddingTop();
        r21 = r1;
        r1 = r6.mBottom;
        r25 = r4;
        r4 = r6.mTop;
        r1 = r1 - r4;
        r4 = r27.getPaddingBottom();
        r1 = r1 - r4;
        r4 = r1 - r2;
        r26 = r0.getMeasuredHeight();
        r4 = r4 - r26;
        r19 = 2;
        r9 = r4 / 2;
    L_0x0257:
        r1 = r0.getMeasuredWidth();
        r2 = r3 + r1;
        r4 = r0.getMeasuredHeight();
        r4 = r4 + r9;
        r0.layout(r3, r9, r2, r4);
        r2 = com.android.internal.widget.AbsActionBarView.next(r12, r1, r8);
        r12 = r2;
    L_0x026a:
        r1 = r6.mProgressView;
        if (r1 == 0) goto L_0x0286;
    L_0x026e:
        r1.bringToFront();
        r1 = r6.mProgressView;
        r1 = r1.getMeasuredHeight();
        r2 = 2;
        r1 = r1 / r2;
        r2 = r6.mProgressView;
        r3 = r6.mProgressBarPadding;
        r4 = -r1;
        r5 = r2.getMeasuredWidth();
        r5 = r5 + r3;
        r2.layout(r3, r4, r5, r1);
    L_0x0286:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.ActionBarView.onLayout(boolean, int, int, int, int):void");
    }

    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ActionBar.LayoutParams(getContext(), attrs);
    }

    public ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
        if (lp == null) {
            return generateDefaultLayoutParams();
        }
        return lp;
    }

    public Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (!(expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null)) {
            state.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        state.isOverflowOpen = isOverflowMenuShowing();
        return state;
    }

    public void onRestoreInstanceState(Parcelable p) {
        SavedState state = (SavedState) p;
        super.onRestoreInstanceState(state.getSuperState());
        if (!(state.expandedMenuItemId == 0 || this.mExpandedMenuPresenter == null)) {
            MenuItem item = this.mOptionsMenu;
            if (item != null) {
                item = item.findItem(state.expandedMenuItemId);
                if (item != null) {
                    item.expandActionView();
                }
            }
        }
        if (state.isOverflowOpen) {
            postShowOverflowMenu();
        }
    }

    public void setNavigationIcon(Drawable indicator) {
        this.mHomeLayout.setUpIndicator(indicator);
    }

    public void setDefaultNavigationIcon(Drawable icon) {
        this.mHomeLayout.setDefaultUpIndicator(icon);
    }

    public void setNavigationIcon(int resId) {
        this.mHomeLayout.setUpIndicator(resId);
    }

    public void setNavigationContentDescription(CharSequence description) {
        this.mHomeDescription = description;
        updateHomeAccessibility(this.mUpGoerFive.isEnabled());
    }

    public void setNavigationContentDescription(int resId) {
        this.mHomeDescriptionRes = resId;
        this.mHomeDescription = resId != 0 ? getResources().getText(resId) : null;
        updateHomeAccessibility(this.mUpGoerFive.isEnabled());
    }

    public void setDefaultNavigationContentDescription(int defaultNavigationContentDescription) {
        if (this.mDefaultUpDescription != defaultNavigationContentDescription) {
            this.mDefaultUpDescription = defaultNavigationContentDescription;
            updateHomeAccessibility(this.mUpGoerFive.isEnabled());
        }
    }

    public void setMenuCallbacks(MenuPresenter.Callback presenterCallback, MenuBuilder.Callback menuBuilderCallback) {
        if (this.mActionMenuPresenter != null) {
            this.mActionMenuPresenter.setCallback(presenterCallback);
        }
        MenuBuilder menuBuilder = this.mOptionsMenu;
        if (menuBuilder != null) {
            menuBuilder.setCallback(menuBuilderCallback);
        }
    }

    public Menu getMenu() {
        return this.mOptionsMenu;
    }
}
