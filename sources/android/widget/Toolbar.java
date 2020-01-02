package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.hardware.biometrics.BiometricPrompt;
import android.media.tv.TvContract.Channels.Logo;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.Layout;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.view.CollapsibleActionView;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.view.inspector.InspectionCompanion.UninitializedPropertyMapException;
import android.view.inspector.PropertyMapper;
import android.view.inspector.PropertyReader;
import com.android.internal.R;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuItemImpl;
import com.android.internal.view.menu.MenuPresenter;
import com.android.internal.view.menu.MenuPresenter.Callback;
import com.android.internal.view.menu.MenuView;
import com.android.internal.view.menu.SubMenuBuilder;
import com.android.internal.widget.DecorToolbar;
import com.android.internal.widget.ToolbarWidgetWrapper;
import java.util.ArrayList;
import java.util.List;

public class Toolbar extends ViewGroup {
    private static final String TAG = "Toolbar";
    private Callback mActionMenuPresenterCallback;
    private int mButtonGravity;
    private ImageButton mCollapseButtonView;
    private CharSequence mCollapseDescription;
    private Drawable mCollapseIcon;
    private boolean mCollapsible;
    private int mContentInsetEndWithActions;
    private int mContentInsetStartWithNavigation;
    private RtlSpacingHelper mContentInsets;
    private boolean mEatingTouch;
    View mExpandedActionView;
    private ExpandedActionViewMenuPresenter mExpandedMenuPresenter;
    private int mGravity;
    private final ArrayList<View> mHiddenViews;
    private ImageView mLogoView;
    private int mMaxButtonHeight;
    private MenuBuilder.Callback mMenuBuilderCallback;
    private ActionMenuView mMenuView;
    private final android.widget.ActionMenuView.OnMenuItemClickListener mMenuViewItemClickListener;
    private int mNavButtonStyle;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private ImageButton mNavButtonView;
    private OnMenuItemClickListener mOnMenuItemClickListener;
    private ActionMenuPresenter mOuterActionMenuPresenter;
    private Context mPopupContext;
    private int mPopupTheme;
    private final Runnable mShowOverflowMenuRunnable;
    private CharSequence mSubtitleText;
    private int mSubtitleTextAppearance;
    private int mSubtitleTextColor;
    private TextView mSubtitleTextView;
    private final int[] mTempMargins;
    private final ArrayList<View> mTempViews;
    @UnsupportedAppUsage
    private int mTitleMarginBottom;
    @UnsupportedAppUsage
    private int mTitleMarginEnd;
    @UnsupportedAppUsage
    private int mTitleMarginStart;
    @UnsupportedAppUsage
    private int mTitleMarginTop;
    private CharSequence mTitleText;
    private int mTitleTextAppearance;
    private int mTitleTextColor;
    @UnsupportedAppUsage
    private TextView mTitleTextView;
    private ToolbarWidgetWrapper mWrapper;

    private class ExpandedActionViewMenuPresenter implements MenuPresenter {
        MenuItemImpl mCurrentExpandedItem;
        MenuBuilder mMenu;

        private ExpandedActionViewMenuPresenter() {
        }

        /* synthetic */ ExpandedActionViewMenuPresenter(Toolbar x0, AnonymousClass1 x1) {
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

        public void setCallback(Callback cb) {
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
            Toolbar.this.ensureCollapseButtonView();
            ViewParent parent = Toolbar.this.mCollapseButtonView.getParent();
            ViewParent viewParent = Toolbar.this;
            if (parent != viewParent) {
                viewParent.addView(viewParent.mCollapseButtonView);
            }
            Toolbar.this.mExpandedActionView = item.getActionView();
            this.mCurrentExpandedItem = item;
            parent = Toolbar.this.mExpandedActionView.getParent();
            viewParent = Toolbar.this;
            if (parent != viewParent) {
                LayoutParams lp = viewParent.generateDefaultLayoutParams();
                lp.gravity = Gravity.START | (Toolbar.this.mButtonGravity & 112);
                lp.mViewType = 2;
                Toolbar.this.mExpandedActionView.setLayoutParams(lp);
                Toolbar toolbar = Toolbar.this;
                toolbar.addView(toolbar.mExpandedActionView);
            }
            Toolbar.this.removeChildrenForExpandedActionView();
            Toolbar.this.requestLayout();
            item.setActionViewExpanded(true);
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) Toolbar.this.mExpandedActionView).onActionViewExpanded();
            }
            return true;
        }

        public boolean collapseItemActionView(MenuBuilder menu, MenuItemImpl item) {
            if (Toolbar.this.mExpandedActionView instanceof CollapsibleActionView) {
                ((CollapsibleActionView) Toolbar.this.mExpandedActionView).onActionViewCollapsed();
            }
            Toolbar toolbar = Toolbar.this;
            toolbar.removeView(toolbar.mExpandedActionView);
            toolbar = Toolbar.this;
            toolbar.removeView(toolbar.mCollapseButtonView);
            toolbar = Toolbar.this;
            toolbar.mExpandedActionView = null;
            toolbar.addChildrenForExpandedActionView();
            this.mCurrentExpandedItem = null;
            Toolbar.this.requestLayout();
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

    public final class InspectionCompanion implements android.view.inspector.InspectionCompanion<Toolbar> {
        private int mCollapseContentDescriptionId;
        private int mCollapseIconId;
        private int mContentInsetEndId;
        private int mContentInsetEndWithActionsId;
        private int mContentInsetLeftId;
        private int mContentInsetRightId;
        private int mContentInsetStartId;
        private int mContentInsetStartWithNavigationId;
        private int mLogoDescriptionId;
        private int mLogoId;
        private int mNavigationContentDescriptionId;
        private int mNavigationIconId;
        private int mPopupThemeId;
        private boolean mPropertiesMapped = false;
        private int mSubtitleId;
        private int mTitleId;
        private int mTitleMarginBottomId;
        private int mTitleMarginEndId;
        private int mTitleMarginStartId;
        private int mTitleMarginTopId;

        public void mapProperties(PropertyMapper propertyMapper) {
            this.mCollapseContentDescriptionId = propertyMapper.mapObject("collapseContentDescription", 16843984);
            this.mCollapseIconId = propertyMapper.mapObject("collapseIcon", 16844031);
            this.mContentInsetEndId = propertyMapper.mapInt("contentInsetEnd", 16843860);
            this.mContentInsetEndWithActionsId = propertyMapper.mapInt("contentInsetEndWithActions", 16844067);
            this.mContentInsetLeftId = propertyMapper.mapInt("contentInsetLeft", 16843861);
            this.mContentInsetRightId = propertyMapper.mapInt("contentInsetRight", 16843862);
            this.mContentInsetStartId = propertyMapper.mapInt("contentInsetStart", 16843859);
            this.mContentInsetStartWithNavigationId = propertyMapper.mapInt("contentInsetStartWithNavigation", 16844066);
            this.mLogoId = propertyMapper.mapObject(Logo.CONTENT_DIRECTORY, 16843454);
            this.mLogoDescriptionId = propertyMapper.mapObject("logoDescription", 16844009);
            this.mNavigationContentDescriptionId = propertyMapper.mapObject("navigationContentDescription", 16843969);
            this.mNavigationIconId = propertyMapper.mapObject("navigationIcon", 16843968);
            this.mPopupThemeId = propertyMapper.mapInt("popupTheme", 16843945);
            this.mSubtitleId = propertyMapper.mapObject(BiometricPrompt.KEY_SUBTITLE, 16843473);
            this.mTitleId = propertyMapper.mapObject("title", 16843233);
            this.mTitleMarginBottomId = propertyMapper.mapInt("titleMarginBottom", 16844028);
            this.mTitleMarginEndId = propertyMapper.mapInt("titleMarginEnd", 16844026);
            this.mTitleMarginStartId = propertyMapper.mapInt("titleMarginStart", 16844025);
            this.mTitleMarginTopId = propertyMapper.mapInt("titleMarginTop", 16844027);
            this.mPropertiesMapped = true;
        }

        public void readProperties(Toolbar node, PropertyReader propertyReader) {
            if (this.mPropertiesMapped) {
                propertyReader.readObject(this.mCollapseContentDescriptionId, node.getCollapseContentDescription());
                propertyReader.readObject(this.mCollapseIconId, node.getCollapseIcon());
                propertyReader.readInt(this.mContentInsetEndId, node.getContentInsetEnd());
                propertyReader.readInt(this.mContentInsetEndWithActionsId, node.getContentInsetEndWithActions());
                propertyReader.readInt(this.mContentInsetLeftId, node.getContentInsetLeft());
                propertyReader.readInt(this.mContentInsetRightId, node.getContentInsetRight());
                propertyReader.readInt(this.mContentInsetStartId, node.getContentInsetStart());
                propertyReader.readInt(this.mContentInsetStartWithNavigationId, node.getContentInsetStartWithNavigation());
                propertyReader.readObject(this.mLogoId, node.getLogo());
                propertyReader.readObject(this.mLogoDescriptionId, node.getLogoDescription());
                propertyReader.readObject(this.mNavigationContentDescriptionId, node.getNavigationContentDescription());
                propertyReader.readObject(this.mNavigationIconId, node.getNavigationIcon());
                propertyReader.readInt(this.mPopupThemeId, node.getPopupTheme());
                propertyReader.readObject(this.mSubtitleId, node.getSubtitle());
                propertyReader.readObject(this.mTitleId, node.getTitle());
                propertyReader.readInt(this.mTitleMarginBottomId, node.getTitleMarginBottom());
                propertyReader.readInt(this.mTitleMarginEndId, node.getTitleMarginEnd());
                propertyReader.readInt(this.mTitleMarginStartId, node.getTitleMarginStart());
                propertyReader.readInt(this.mTitleMarginTopId, node.getTitleMarginTop());
                return;
            }
            throw new UninitializedPropertyMapException();
        }
    }

    public static class LayoutParams extends android.app.ActionBar.LayoutParams {
        static final int CUSTOM = 0;
        static final int EXPANDED = 2;
        static final int SYSTEM = 1;
        int mViewType;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            this.mViewType = 0;
        }

        public LayoutParams(int width, int height) {
            super(width, height);
            this.mViewType = 0;
            this.gravity = 8388627;
        }

        public LayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.mViewType = 0;
            this.gravity = gravity;
        }

        public LayoutParams(int gravity) {
            this(-2, -1, gravity);
        }

        public LayoutParams(LayoutParams source) {
            super((android.app.ActionBar.LayoutParams) source);
            this.mViewType = 0;
            this.mViewType = source.mViewType;
        }

        public LayoutParams(android.app.ActionBar.LayoutParams source) {
            super(source);
            this.mViewType = 0;
        }

        public LayoutParams(MarginLayoutParams source) {
            super((android.view.ViewGroup.LayoutParams) source);
            this.mViewType = 0;
            copyMarginsFrom(source);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
            this.mViewType = 0;
        }
    }

    public interface OnMenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        public int expandedMenuItemId;
        public boolean isOverflowOpen;

        public SavedState(Parcel source) {
            super(source);
            this.expandedMenuItemId = source.readInt();
            this.isOverflowOpen = source.readInt() != 0;
        }

        public SavedState(Parcelable superState) {
            super(superState);
        }

        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(this.expandedMenuItemId);
            out.writeInt(this.isOverflowOpen);
        }
    }

    public Toolbar(Context context) {
        this(context, null);
    }

    public Toolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 16843946);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Toolbar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mGravity = 8388627;
        this.mTempViews = new ArrayList();
        this.mHiddenViews = new ArrayList();
        this.mTempMargins = new int[2];
        this.mMenuViewItemClickListener = new android.widget.ActionMenuView.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                if (Toolbar.this.mOnMenuItemClickListener != null) {
                    return Toolbar.this.mOnMenuItemClickListener.onMenuItemClick(item);
                }
                return false;
            }
        };
        this.mShowOverflowMenuRunnable = new Runnable() {
            public void run() {
                Toolbar.this.showOverflowMenu();
            }
        };
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Toolbar, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.Toolbar, attrs, a, defStyleAttr, defStyleRes);
        this.mTitleTextAppearance = a.getResourceId(4, 0);
        this.mSubtitleTextAppearance = a.getResourceId(5, 0);
        this.mNavButtonStyle = a.getResourceId(27, 0);
        this.mGravity = a.getInteger(0, this.mGravity);
        this.mButtonGravity = a.getInteger(23, 48);
        int dimensionPixelOffset = a.getDimensionPixelOffset(17, 0);
        this.mTitleMarginBottom = dimensionPixelOffset;
        this.mTitleMarginTop = dimensionPixelOffset;
        this.mTitleMarginEnd = dimensionPixelOffset;
        this.mTitleMarginStart = dimensionPixelOffset;
        int marginStart = a.getDimensionPixelOffset(18, -1);
        if (marginStart >= 0) {
            this.mTitleMarginStart = marginStart;
        }
        int marginEnd = a.getDimensionPixelOffset(19, -1);
        if (marginEnd >= 0) {
            this.mTitleMarginEnd = marginEnd;
        }
        int marginTop = a.getDimensionPixelOffset(20, -1);
        if (marginTop >= 0) {
            this.mTitleMarginTop = marginTop;
        }
        int marginBottom = a.getDimensionPixelOffset(21, -1);
        if (marginBottom >= 0) {
            this.mTitleMarginBottom = marginBottom;
        }
        this.mMaxButtonHeight = a.getDimensionPixelSize(22, -1);
        int contentInsetStart = a.getDimensionPixelOffset(6, Integer.MIN_VALUE);
        int contentInsetEnd = a.getDimensionPixelOffset(7, Integer.MIN_VALUE);
        dimensionPixelOffset = a.getDimensionPixelSize(8, 0);
        int contentInsetRight = a.getDimensionPixelSize(9, 0);
        ensureContentInsets();
        this.mContentInsets.setAbsolute(dimensionPixelOffset, contentInsetRight);
        if (!(contentInsetStart == Integer.MIN_VALUE && contentInsetEnd == Integer.MIN_VALUE)) {
            this.mContentInsets.setRelative(contentInsetStart, contentInsetEnd);
        }
        this.mContentInsetStartWithNavigation = a.getDimensionPixelOffset(25, Integer.MIN_VALUE);
        this.mContentInsetEndWithActions = a.getDimensionPixelOffset(26, Integer.MIN_VALUE);
        this.mCollapseIcon = a.getDrawable(24);
        this.mCollapseDescription = a.getText(13);
        CharSequence title = a.getText(1);
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        }
        CharSequence subtitle = a.getText(3);
        if (!TextUtils.isEmpty(subtitle)) {
            setSubtitle(subtitle);
        }
        this.mPopupContext = this.mContext;
        setPopupTheme(a.getResourceId(10, 0));
        Drawable navIcon = a.getDrawable(11);
        if (navIcon != null) {
            setNavigationIcon(navIcon);
        }
        CharSequence navDesc = a.getText(12);
        if (!TextUtils.isEmpty(navDesc)) {
            setNavigationContentDescription(navDesc);
        }
        navIcon = a.getDrawable(2);
        if (navIcon != null) {
            setLogo(navIcon);
        }
        title = a.getText(16);
        if (!TextUtils.isEmpty(title)) {
            setLogoDescription(title);
        }
        if (a.hasValue(14)) {
            dimensionPixelOffset = -1;
            setTitleTextColor(a.getColor(14, -1));
        } else {
            dimensionPixelOffset = -1;
        }
        if (a.hasValue(15)) {
            setSubtitleTextColor(a.getColor(15, dimensionPixelOffset));
        }
        a.recycle();
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = getParent();
        while (parent != null && (parent instanceof ViewGroup)) {
            ViewGroup vgParent = (ViewGroup) parent;
            if (vgParent.isKeyboardNavigationCluster()) {
                setKeyboardNavigationCluster(false);
                if (vgParent.getTouchscreenBlocksFocus()) {
                    setTouchscreenBlocksFocus(false);
                    return;
                }
                return;
            }
            parent = vgParent.getParent();
        }
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

    public void setTitleMargin(int start, int top, int end, int bottom) {
        this.mTitleMarginStart = start;
        this.mTitleMarginTop = top;
        this.mTitleMarginEnd = end;
        this.mTitleMarginBottom = bottom;
        requestLayout();
    }

    public int getTitleMarginStart() {
        return this.mTitleMarginStart;
    }

    public void setTitleMarginStart(int margin) {
        this.mTitleMarginStart = margin;
        requestLayout();
    }

    public int getTitleMarginTop() {
        return this.mTitleMarginTop;
    }

    public void setTitleMarginTop(int margin) {
        this.mTitleMarginTop = margin;
        requestLayout();
    }

    public int getTitleMarginEnd() {
        return this.mTitleMarginEnd;
    }

    public void setTitleMarginEnd(int margin) {
        this.mTitleMarginEnd = margin;
        requestLayout();
    }

    public int getTitleMarginBottom() {
        return this.mTitleMarginBottom;
    }

    public void setTitleMarginBottom(int margin) {
        this.mTitleMarginBottom = margin;
        requestLayout();
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        ensureContentInsets();
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        boolean z = true;
        if (layoutDirection != 1) {
            z = false;
        }
        rtlSpacingHelper.setDirection(z);
    }

    public void setLogo(int resId) {
        setLogo(getContext().getDrawable(resId));
    }

    public boolean canShowOverflowMenu() {
        if (getVisibility() == 0) {
            ActionMenuView actionMenuView = this.mMenuView;
            if (actionMenuView != null && actionMenuView.isOverflowReserved()) {
                return true;
            }
        }
        return false;
    }

    public boolean isOverflowMenuShowing() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowing();
    }

    public boolean isOverflowMenuShowPending() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.isOverflowMenuShowPending();
    }

    public boolean showOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.showOverflowMenu();
    }

    public boolean hideOverflowMenu() {
        ActionMenuView actionMenuView = this.mMenuView;
        return actionMenuView != null && actionMenuView.hideOverflowMenu();
    }

    public void setMenu(MenuBuilder menu, ActionMenuPresenter outerPresenter) {
        if (menu != null || this.mMenuView != null) {
            ensureMenuView();
            MenuBuilder oldMenu = this.mMenuView.peekMenu();
            if (oldMenu != menu) {
                if (oldMenu != null) {
                    oldMenu.removeMenuPresenter(this.mOuterActionMenuPresenter);
                    oldMenu.removeMenuPresenter(this.mExpandedMenuPresenter);
                }
                if (this.mExpandedMenuPresenter == null) {
                    this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(this, null);
                }
                outerPresenter.setExpandedActionViewsExclusive(true);
                if (menu != null) {
                    menu.addMenuPresenter(outerPresenter, this.mPopupContext);
                    menu.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
                } else {
                    outerPresenter.initForMenu(this.mPopupContext, null);
                    this.mExpandedMenuPresenter.initForMenu(this.mPopupContext, null);
                    outerPresenter.updateMenuView(true);
                    this.mExpandedMenuPresenter.updateMenuView(true);
                }
                this.mMenuView.setPopupTheme(this.mPopupTheme);
                this.mMenuView.setPresenter(outerPresenter);
                this.mOuterActionMenuPresenter = outerPresenter;
            }
        }
    }

    public void dismissPopupMenus() {
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.dismissPopupMenus();
        }
    }

    public boolean isTitleTruncated() {
        Layout titleLayout = this.mTitleTextView;
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

    public void setLogo(Drawable drawable) {
        ImageView imageView;
        if (drawable != null) {
            ensureLogoView();
            if (!isChildOrHidden(this.mLogoView)) {
                addSystemView(this.mLogoView, true);
            }
        } else {
            imageView = this.mLogoView;
            if (imageView != null && isChildOrHidden(imageView)) {
                removeView(this.mLogoView);
                this.mHiddenViews.remove(this.mLogoView);
            }
        }
        imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setImageDrawable(drawable);
        }
    }

    public Drawable getLogo() {
        ImageView imageView = this.mLogoView;
        return imageView != null ? imageView.getDrawable() : null;
    }

    public void setLogoDescription(int resId) {
        setLogoDescription(getContext().getText(resId));
    }

    public void setLogoDescription(CharSequence description) {
        if (!TextUtils.isEmpty(description)) {
            ensureLogoView();
        }
        ImageView imageView = this.mLogoView;
        if (imageView != null) {
            imageView.setContentDescription(description);
        }
    }

    public CharSequence getLogoDescription() {
        ImageView imageView = this.mLogoView;
        return imageView != null ? imageView.getContentDescription() : null;
    }

    private void ensureLogoView() {
        if (this.mLogoView == null) {
            this.mLogoView = new ImageView(getContext());
        }
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

    public CharSequence getTitle() {
        return this.mTitleText;
    }

    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    public void setTitle(CharSequence title) {
        TextView textView;
        if (TextUtils.isEmpty(title)) {
            textView = this.mTitleTextView;
            if (textView != null && isChildOrHidden(textView)) {
                removeView(this.mTitleTextView);
                this.mHiddenViews.remove(this.mTitleTextView);
            }
        } else {
            if (this.mTitleTextView == null) {
                this.mTitleTextView = new TextView(getContext());
                this.mTitleTextView.setSingleLine();
                this.mTitleTextView.setEllipsize(TruncateAt.END);
                int i = this.mTitleTextAppearance;
                if (i != 0) {
                    this.mTitleTextView.setTextAppearance(i);
                }
                i = this.mTitleTextColor;
                if (i != 0) {
                    this.mTitleTextView.setTextColor(i);
                }
            }
            if (!isChildOrHidden(this.mTitleTextView)) {
                addSystemView(this.mTitleTextView, true);
            }
        }
        textView = this.mTitleTextView;
        if (textView != null) {
            textView.setText(title);
        }
        this.mTitleText = title;
    }

    public CharSequence getSubtitle() {
        return this.mSubtitleText;
    }

    public void setSubtitle(int resId) {
        setSubtitle(getContext().getText(resId));
    }

    public void setSubtitle(CharSequence subtitle) {
        TextView textView;
        if (TextUtils.isEmpty(subtitle)) {
            textView = this.mSubtitleTextView;
            if (textView != null && isChildOrHidden(textView)) {
                removeView(this.mSubtitleTextView);
                this.mHiddenViews.remove(this.mSubtitleTextView);
            }
        } else {
            if (this.mSubtitleTextView == null) {
                this.mSubtitleTextView = new TextView(getContext());
                this.mSubtitleTextView.setSingleLine();
                this.mSubtitleTextView.setEllipsize(TruncateAt.END);
                int i = this.mSubtitleTextAppearance;
                if (i != 0) {
                    this.mSubtitleTextView.setTextAppearance(i);
                }
                i = this.mSubtitleTextColor;
                if (i != 0) {
                    this.mSubtitleTextView.setTextColor(i);
                }
            }
            if (!isChildOrHidden(this.mSubtitleTextView)) {
                addSystemView(this.mSubtitleTextView, true);
            }
        }
        textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setText(subtitle);
        }
        this.mSubtitleText = subtitle;
    }

    public void setTitleTextAppearance(Context context, int resId) {
        this.mTitleTextAppearance = resId;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextAppearance(resId);
        }
    }

    public void setSubtitleTextAppearance(Context context, int resId) {
        this.mSubtitleTextAppearance = resId;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextAppearance(resId);
        }
    }

    public void setTitleTextColor(int color) {
        this.mTitleTextColor = color;
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public void setSubtitleTextColor(int color) {
        this.mSubtitleTextColor = color;
        TextView textView = this.mSubtitleTextView;
        if (textView != null) {
            textView.setTextColor(color);
        }
    }

    public CharSequence getNavigationContentDescription() {
        ImageButton imageButton = this.mNavButtonView;
        return imageButton != null ? imageButton.getContentDescription() : null;
    }

    public void setNavigationContentDescription(int resId) {
        setNavigationContentDescription(resId != 0 ? getContext().getText(resId) : null);
    }

    public void setNavigationContentDescription(CharSequence description) {
        if (!TextUtils.isEmpty(description)) {
            ensureNavButtonView();
        }
        ImageButton imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setContentDescription(description);
        }
    }

    public void setNavigationIcon(int resId) {
        setNavigationIcon(getContext().getDrawable(resId));
    }

    public void setNavigationIcon(Drawable icon) {
        ImageButton imageButton;
        if (icon != null) {
            ensureNavButtonView();
            if (!isChildOrHidden(this.mNavButtonView)) {
                addSystemView(this.mNavButtonView, true);
            }
        } else {
            imageButton = this.mNavButtonView;
            if (imageButton != null && isChildOrHidden(imageButton)) {
                removeView(this.mNavButtonView);
                this.mHiddenViews.remove(this.mNavButtonView);
            }
        }
        imageButton = this.mNavButtonView;
        if (imageButton != null) {
            imageButton.setImageDrawable(icon);
        }
    }

    public Drawable getNavigationIcon() {
        ImageButton imageButton = this.mNavButtonView;
        return imageButton != null ? imageButton.getDrawable() : null;
    }

    public void setNavigationOnClickListener(OnClickListener listener) {
        ensureNavButtonView();
        this.mNavButtonView.setOnClickListener(listener);
    }

    public View getNavigationView() {
        return this.mNavButtonView;
    }

    public CharSequence getCollapseContentDescription() {
        ImageButton imageButton = this.mCollapseButtonView;
        return imageButton != null ? imageButton.getContentDescription() : null;
    }

    public void setCollapseContentDescription(int resId) {
        setCollapseContentDescription(resId != 0 ? getContext().getText(resId) : null);
    }

    public void setCollapseContentDescription(CharSequence description) {
        if (!TextUtils.isEmpty(description)) {
            ensureCollapseButtonView();
        }
        ImageButton imageButton = this.mCollapseButtonView;
        if (imageButton != null) {
            imageButton.setContentDescription(description);
        }
    }

    public Drawable getCollapseIcon() {
        ImageButton imageButton = this.mCollapseButtonView;
        return imageButton != null ? imageButton.getDrawable() : null;
    }

    public void setCollapseIcon(int resId) {
        setCollapseIcon(getContext().getDrawable(resId));
    }

    public void setCollapseIcon(Drawable icon) {
        if (icon != null) {
            ensureCollapseButtonView();
            this.mCollapseButtonView.setImageDrawable(icon);
            return;
        }
        ImageButton imageButton = this.mCollapseButtonView;
        if (imageButton != null) {
            imageButton.setImageDrawable(this.mCollapseIcon);
        }
    }

    public Menu getMenu() {
        ensureMenu();
        return this.mMenuView.getMenu();
    }

    public void setOverflowIcon(Drawable icon) {
        ensureMenu();
        this.mMenuView.setOverflowIcon(icon);
    }

    public Drawable getOverflowIcon() {
        ensureMenu();
        return this.mMenuView.getOverflowIcon();
    }

    private void ensureMenu() {
        ensureMenuView();
        if (this.mMenuView.peekMenu() == null) {
            MenuBuilder menu = (MenuBuilder) this.mMenuView.getMenu();
            if (this.mExpandedMenuPresenter == null) {
                this.mExpandedMenuPresenter = new ExpandedActionViewMenuPresenter(this, null);
            }
            this.mMenuView.setExpandedActionViewsExclusive(true);
            menu.addMenuPresenter(this.mExpandedMenuPresenter, this.mPopupContext);
        }
    }

    private void ensureMenuView() {
        if (this.mMenuView == null) {
            this.mMenuView = new ActionMenuView(getContext());
            this.mMenuView.setPopupTheme(this.mPopupTheme);
            this.mMenuView.setOnMenuItemClickListener(this.mMenuViewItemClickListener);
            this.mMenuView.setMenuCallbacks(this.mActionMenuPresenterCallback, this.mMenuBuilderCallback);
            LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = Gravity.END | (this.mButtonGravity & 112);
            this.mMenuView.setLayoutParams(lp);
            addSystemView(this.mMenuView, false);
        }
    }

    private MenuInflater getMenuInflater() {
        return new MenuInflater(getContext());
    }

    public void inflateMenu(int resId) {
        getMenuInflater().inflate(resId, getMenu());
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener listener) {
        this.mOnMenuItemClickListener = listener;
    }

    public void setContentInsetsRelative(int contentInsetStart, int contentInsetEnd) {
        ensureContentInsets();
        this.mContentInsets.setRelative(contentInsetStart, contentInsetEnd);
    }

    public int getContentInsetStart() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getStart() : 0;
    }

    public int getContentInsetEnd() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getEnd() : 0;
    }

    public void setContentInsetsAbsolute(int contentInsetLeft, int contentInsetRight) {
        ensureContentInsets();
        this.mContentInsets.setAbsolute(contentInsetLeft, contentInsetRight);
    }

    public int getContentInsetLeft() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getLeft() : 0;
    }

    public int getContentInsetRight() {
        RtlSpacingHelper rtlSpacingHelper = this.mContentInsets;
        return rtlSpacingHelper != null ? rtlSpacingHelper.getRight() : 0;
    }

    public int getContentInsetStartWithNavigation() {
        int i = this.mContentInsetStartWithNavigation;
        if (i != Integer.MIN_VALUE) {
            return i;
        }
        return getContentInsetStart();
    }

    public void setContentInsetStartWithNavigation(int insetStartWithNavigation) {
        if (insetStartWithNavigation < 0) {
            insetStartWithNavigation = Integer.MIN_VALUE;
        }
        if (insetStartWithNavigation != this.mContentInsetStartWithNavigation) {
            this.mContentInsetStartWithNavigation = insetStartWithNavigation;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public int getContentInsetEndWithActions() {
        int i = this.mContentInsetEndWithActions;
        if (i != Integer.MIN_VALUE) {
            return i;
        }
        return getContentInsetEnd();
    }

    public void setContentInsetEndWithActions(int insetEndWithActions) {
        if (insetEndWithActions < 0) {
            insetEndWithActions = Integer.MIN_VALUE;
        }
        if (insetEndWithActions != this.mContentInsetEndWithActions) {
            this.mContentInsetEndWithActions = insetEndWithActions;
            if (getNavigationIcon() != null) {
                requestLayout();
            }
        }
    }

    public int getCurrentContentInsetStart() {
        if (getNavigationIcon() != null) {
            return Math.max(getContentInsetStart(), Math.max(this.mContentInsetStartWithNavigation, 0));
        }
        return getContentInsetStart();
    }

    public int getCurrentContentInsetEnd() {
        boolean hasActions = false;
        MenuBuilder mb = this.mMenuView;
        if (mb != null) {
            mb = mb.peekMenu();
            boolean z = mb != null && mb.hasVisibleItems();
            hasActions = z;
        }
        if (hasActions) {
            return Math.max(getContentInsetEnd(), Math.max(this.mContentInsetEndWithActions, 0));
        }
        return getContentInsetEnd();
    }

    public int getCurrentContentInsetLeft() {
        if (isLayoutRtl()) {
            return getCurrentContentInsetEnd();
        }
        return getCurrentContentInsetStart();
    }

    public int getCurrentContentInsetRight() {
        if (isLayoutRtl()) {
            return getCurrentContentInsetStart();
        }
        return getCurrentContentInsetEnd();
    }

    private void ensureNavButtonView() {
        if (this.mNavButtonView == null) {
            this.mNavButtonView = new ImageButton(getContext(), null, 0, this.mNavButtonStyle);
            LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = Gravity.START | (this.mButtonGravity & 112);
            this.mNavButtonView.setLayoutParams(lp);
        }
    }

    private void ensureCollapseButtonView() {
        if (this.mCollapseButtonView == null) {
            this.mCollapseButtonView = new ImageButton(getContext(), null, 0, this.mNavButtonStyle);
            this.mCollapseButtonView.setImageDrawable(this.mCollapseIcon);
            this.mCollapseButtonView.setContentDescription(this.mCollapseDescription);
            LayoutParams lp = generateDefaultLayoutParams();
            lp.gravity = Gravity.START | (this.mButtonGravity & 112);
            lp.mViewType = 2;
            this.mCollapseButtonView.setLayoutParams(lp);
            this.mCollapseButtonView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    Toolbar.this.collapseActionView();
                }
            });
        }
    }

    private void addSystemView(View v, boolean allowHide) {
        LayoutParams lp;
        android.view.ViewGroup.LayoutParams vlp = v.getLayoutParams();
        if (vlp == null) {
            lp = generateDefaultLayoutParams();
        } else if (checkLayoutParams(vlp)) {
            lp = (LayoutParams) vlp;
        } else {
            lp = generateLayoutParams(vlp);
        }
        lp.mViewType = 1;
        if (!allowHide || this.mExpandedActionView == null) {
            addView(v, (android.view.ViewGroup.LayoutParams) lp);
            return;
        }
        v.setLayoutParams(lp);
        this.mHiddenViews.add(v);
    }

    /* Access modifiers changed, original: protected */
    public Parcelable onSaveInstanceState() {
        SavedState state = new SavedState(super.onSaveInstanceState());
        ExpandedActionViewMenuPresenter expandedActionViewMenuPresenter = this.mExpandedMenuPresenter;
        if (!(expandedActionViewMenuPresenter == null || expandedActionViewMenuPresenter.mCurrentExpandedItem == null)) {
            state.expandedMenuItemId = this.mExpandedMenuPresenter.mCurrentExpandedItem.getItemId();
        }
        state.isOverflowOpen = isOverflowMenuShowing();
        return state;
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
        super.onRestoreInstanceState(ss.getSuperState());
        ActionMenuView actionMenuView = this.mMenuView;
        Menu menu = actionMenuView != null ? actionMenuView.peekMenu() : null;
        if (!(ss.expandedMenuItemId == 0 || this.mExpandedMenuPresenter == null || menu == null)) {
            MenuItem item = menu.findItem(ss.expandedMenuItemId);
            if (item != null) {
                item.expandActionView();
            }
        }
        if (ss.isOverflowOpen) {
            postShowOverflowMenu();
        }
    }

    private void postShowOverflowMenu() {
        removeCallbacks(this.mShowOverflowMenuRunnable);
        post(this.mShowOverflowMenuRunnable);
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeCallbacks(this.mShowOverflowMenuRunnable);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        if (action == 0) {
            this.mEatingTouch = false;
        }
        if (!this.mEatingTouch) {
            boolean handled = super.onTouchEvent(ev);
            if (action == 0 && !handled) {
                this.mEatingTouch = true;
            }
        }
        if (action == 1 || action == 3) {
            this.mEatingTouch = false;
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onSetLayoutParams(View child, android.view.ViewGroup.LayoutParams lp) {
        if (!checkLayoutParams(lp)) {
            child.setLayoutParams(generateLayoutParams(lp));
        }
    }

    private void measureChildConstrained(View child, int parentWidthSpec, int widthUsed, int parentHeightSpec, int heightUsed, int heightConstraint) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int childWidthSpec = ViewGroup.getChildMeasureSpec(parentWidthSpec, (((this.mPaddingLeft + this.mPaddingRight) + lp.leftMargin) + lp.rightMargin) + widthUsed, lp.width);
        int childHeightSpec = ViewGroup.getChildMeasureSpec(parentHeightSpec, (((this.mPaddingTop + this.mPaddingBottom) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height);
        int childHeightMode = MeasureSpec.getMode(childHeightSpec);
        if (childHeightMode != 1073741824 && heightConstraint >= 0) {
            int size;
            if (childHeightMode != 0) {
                size = Math.min(MeasureSpec.getSize(childHeightSpec), heightConstraint);
            } else {
                size = heightConstraint;
            }
            childHeightSpec = MeasureSpec.makeMeasureSpec(size, 1073741824);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private int measureChildCollapseMargins(View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed, int[] collapsingMargins) {
        MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
        int leftDiff = lp.leftMargin - collapsingMargins[0];
        int rightDiff = lp.rightMargin - collapsingMargins[1];
        int hMargins = Math.max(0, leftDiff) + Math.max(0, rightDiff);
        collapsingMargins[0] = Math.max(0, -leftDiff);
        collapsingMargins[1] = Math.max(0, -rightDiff);
        child.measure(ViewGroup.getChildMeasureSpec(parentWidthMeasureSpec, ((this.mPaddingLeft + this.mPaddingRight) + hMargins) + widthUsed, lp.width), ViewGroup.getChildMeasureSpec(parentHeightMeasureSpec, (((this.mPaddingTop + this.mPaddingBottom) + lp.topMargin) + lp.bottomMargin) + heightUsed, lp.height));
        return child.getMeasuredWidth() + hMargins;
    }

    private boolean shouldCollapse() {
        if (!this.mCollapsible) {
            return false;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (shouldLayout(child) && child.getMeasuredWidth() > 0 && child.getMeasuredHeight() > 0) {
                return false;
            }
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int marginStartIndex;
        int marginEndIndex;
        int childState;
        int menuWidth;
        int titleWidth;
        int height = 0;
        int childState2 = 0;
        int[] collapsingMargins = this.mTempMargins;
        if (isLayoutRtl()) {
            marginStartIndex = 1;
            marginEndIndex = 0;
        } else {
            marginStartIndex = 0;
            marginEndIndex = 1;
        }
        int navWidth = 0;
        if (shouldLayout(this.mNavButtonView)) {
            measureChildConstrained(this.mNavButtonView, widthMeasureSpec, 0, heightMeasureSpec, 0, this.mMaxButtonHeight);
            navWidth = this.mNavButtonView.getMeasuredWidth() + getHorizontalMargins(this.mNavButtonView);
            height = Math.max(0, this.mNavButtonView.getMeasuredHeight() + getVerticalMargins(this.mNavButtonView));
            childState2 = View.combineMeasuredStates(0, this.mNavButtonView.getMeasuredState());
        }
        if (shouldLayout(this.mCollapseButtonView)) {
            measureChildConstrained(this.mCollapseButtonView, widthMeasureSpec, 0, heightMeasureSpec, 0, this.mMaxButtonHeight);
            navWidth = this.mCollapseButtonView.getMeasuredWidth() + getHorizontalMargins(this.mCollapseButtonView);
            height = Math.max(height, this.mCollapseButtonView.getMeasuredHeight() + getVerticalMargins(this.mCollapseButtonView));
            childState2 = View.combineMeasuredStates(childState2, this.mCollapseButtonView.getMeasuredState());
        }
        int contentInsetStart = getCurrentContentInsetStart();
        int width = 0 + Math.max(contentInsetStart, navWidth);
        collapsingMargins[marginStartIndex] = Math.max(0, contentInsetStart - navWidth);
        if (shouldLayout(this.mMenuView)) {
            marginStartIndex = 0;
            measureChildConstrained(this.mMenuView, widthMeasureSpec, width, heightMeasureSpec, 0, this.mMaxButtonHeight);
            int menuWidth2 = this.mMenuView.getMeasuredWidth() + getHorizontalMargins(this.mMenuView);
            height = Math.max(height, this.mMenuView.getMeasuredHeight() + getVerticalMargins(this.mMenuView));
            childState = View.combineMeasuredStates(childState2, this.mMenuView.getMeasuredState());
            childState2 = height;
            height = menuWidth2;
        } else {
            marginStartIndex = 0;
            childState = childState2;
            childState2 = height;
            height = 0;
        }
        int contentInsetEnd = getCurrentContentInsetEnd();
        width += Math.max(contentInsetEnd, height);
        collapsingMargins[marginEndIndex] = Math.max(marginStartIndex, contentInsetEnd - height);
        if (shouldLayout(this.mExpandedActionView)) {
            marginStartIndex = childState;
            width += measureChildCollapseMargins(this.mExpandedActionView, widthMeasureSpec, width, heightMeasureSpec, 0, collapsingMargins);
            childState2 = Math.max(childState2, this.mExpandedActionView.getMeasuredHeight() + getVerticalMargins(this.mExpandedActionView));
            marginStartIndex = View.combineMeasuredStates(marginStartIndex, this.mExpandedActionView.getMeasuredState());
        } else {
            marginStartIndex = childState;
        }
        if (shouldLayout(this.mLogoView)) {
            width += measureChildCollapseMargins(this.mLogoView, widthMeasureSpec, width, heightMeasureSpec, 0, collapsingMargins);
            childState2 = Math.max(childState2, this.mLogoView.getMeasuredHeight() + getVerticalMargins(this.mLogoView));
            marginStartIndex = View.combineMeasuredStates(marginStartIndex, this.mLogoView.getMeasuredState());
        }
        childState = getChildCount();
        contentInsetEnd = childState2;
        childState2 = width;
        width = 0;
        while (width < childState) {
            int childCount;
            View child = getChildAt(width);
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            View view;
            if (lp.mViewType != 0) {
                view = child;
                childCount = childState;
                menuWidth = height;
                height = contentInsetEnd;
            } else if (shouldLayout(child)) {
                View child2 = child;
                menuWidth = height;
                childCount = childState;
                childState2 += measureChildCollapseMargins(child, widthMeasureSpec, childState2, heightMeasureSpec, 0, collapsingMargins);
                view = child2;
                contentInsetEnd = Math.max(contentInsetEnd, child2.getMeasuredHeight() + getVerticalMargins(view));
                marginStartIndex = View.combineMeasuredStates(marginStartIndex, view.getMeasuredState());
                width++;
                childState = childCount;
                height = menuWidth;
            } else {
                childCount = childState;
                menuWidth = height;
                height = contentInsetEnd;
            }
            contentInsetEnd = height;
            width++;
            childState = childCount;
            height = menuWidth;
        }
        menuWidth = height;
        height = contentInsetEnd;
        width = 0;
        int titleHeight = 0;
        int titleVertMargins = this.mTitleMarginTop + this.mTitleMarginBottom;
        int titleHorizMargins = this.mTitleMarginStart + this.mTitleMarginEnd;
        if (shouldLayout(this.mTitleTextView)) {
            titleWidth = measureChildCollapseMargins(this.mTitleTextView, widthMeasureSpec, childState2 + titleHorizMargins, heightMeasureSpec, titleVertMargins, collapsingMargins);
            width = this.mTitleTextView.getMeasuredWidth() + getHorizontalMargins(this.mTitleTextView);
            titleHeight = this.mTitleTextView.getMeasuredHeight() + getVerticalMargins(this.mTitleTextView);
            marginStartIndex = View.combineMeasuredStates(marginStartIndex, this.mTitleTextView.getMeasuredState());
        }
        if (shouldLayout(this.mSubtitleTextView)) {
            width = Math.max(width, measureChildCollapseMargins(this.mSubtitleTextView, widthMeasureSpec, childState2 + titleHorizMargins, heightMeasureSpec, titleHeight + titleVertMargins, collapsingMargins));
            titleHeight += this.mSubtitleTextView.getMeasuredHeight() + getVerticalMargins(this.mSubtitleTextView);
            marginStartIndex = View.combineMeasuredStates(marginStartIndex, this.mSubtitleTextView.getMeasuredState());
            titleWidth = titleHeight;
        } else {
            titleWidth = titleHeight;
        }
        setMeasuredDimension(View.resolveSizeAndState(Math.max((childState2 + width) + (getPaddingLeft() + getPaddingRight()), getSuggestedMinimumWidth()), widthMeasureSpec, -16777216 & marginStartIndex), shouldCollapse() ? 0 : View.resolveSizeAndState(Math.max(Math.max(height, titleWidth) + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight()), heightMeasureSpec, marginStartIndex << 16));
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x01c9  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x017c  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x025a  */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x01da  */
    /* JADX WARNING: Missing block: B:51:0x015e, code skipped:
            if (r0.mTitleTextView.getMeasuredWidth() <= 0) goto L_0x0163;
     */
    public void onLayout(boolean r34, int r35, int r36, int r37, int r38) {
        /*
        r33 = this;
        r0 = r33;
        r1 = r33.getLayoutDirection();
        r2 = 1;
        r3 = 0;
        if (r1 != r2) goto L_0x000c;
    L_0x000a:
        r1 = r2;
        goto L_0x000d;
    L_0x000c:
        r1 = r3;
    L_0x000d:
        r4 = r33.getWidth();
        r5 = r33.getHeight();
        r6 = r33.getPaddingLeft();
        r7 = r33.getPaddingRight();
        r8 = r33.getPaddingTop();
        r9 = r33.getPaddingBottom();
        r10 = r6;
        r11 = r4 - r7;
        r12 = r0.mTempMargins;
        r12[r2] = r3;
        r12[r3] = r3;
        r13 = r33.getMinimumHeight();
        if (r13 < 0) goto L_0x003b;
    L_0x0034:
        r14 = r38 - r36;
        r14 = java.lang.Math.min(r13, r14);
        goto L_0x003c;
    L_0x003b:
        r14 = r3;
    L_0x003c:
        r15 = r0.mNavButtonView;
        r15 = r0.shouldLayout(r15);
        if (r15 == 0) goto L_0x0053;
    L_0x0044:
        if (r1 == 0) goto L_0x004d;
    L_0x0046:
        r15 = r0.mNavButtonView;
        r11 = r0.layoutChildRight(r15, r11, r12, r14);
        goto L_0x0053;
    L_0x004d:
        r15 = r0.mNavButtonView;
        r10 = r0.layoutChildLeft(r15, r10, r12, r14);
    L_0x0053:
        r15 = r0.mCollapseButtonView;
        r15 = r0.shouldLayout(r15);
        if (r15 == 0) goto L_0x006a;
    L_0x005b:
        if (r1 == 0) goto L_0x0064;
    L_0x005d:
        r15 = r0.mCollapseButtonView;
        r11 = r0.layoutChildRight(r15, r11, r12, r14);
        goto L_0x006a;
    L_0x0064:
        r15 = r0.mCollapseButtonView;
        r10 = r0.layoutChildLeft(r15, r10, r12, r14);
    L_0x006a:
        r15 = r0.mMenuView;
        r15 = r0.shouldLayout(r15);
        if (r15 == 0) goto L_0x0081;
    L_0x0072:
        if (r1 == 0) goto L_0x007b;
    L_0x0074:
        r15 = r0.mMenuView;
        r10 = r0.layoutChildLeft(r15, r10, r12, r14);
        goto L_0x0081;
    L_0x007b:
        r15 = r0.mMenuView;
        r11 = r0.layoutChildRight(r15, r11, r12, r14);
    L_0x0081:
        r15 = r33.getCurrentContentInsetLeft();
        r16 = r33.getCurrentContentInsetRight();
        r2 = r15 - r10;
        r2 = java.lang.Math.max(r3, r2);
        r12[r3] = r2;
        r2 = r4 - r7;
        r2 = r2 - r11;
        r2 = r16 - r2;
        r2 = java.lang.Math.max(r3, r2);
        r17 = 1;
        r12[r17] = r2;
        r2 = java.lang.Math.max(r10, r15);
        r10 = r4 - r7;
        r10 = r10 - r16;
        r10 = java.lang.Math.min(r11, r10);
        r11 = r0.mExpandedActionView;
        r11 = r0.shouldLayout(r11);
        if (r11 == 0) goto L_0x00c1;
    L_0x00b2:
        if (r1 == 0) goto L_0x00bb;
    L_0x00b4:
        r11 = r0.mExpandedActionView;
        r10 = r0.layoutChildRight(r11, r10, r12, r14);
        goto L_0x00c1;
    L_0x00bb:
        r11 = r0.mExpandedActionView;
        r2 = r0.layoutChildLeft(r11, r2, r12, r14);
    L_0x00c1:
        r11 = r0.mLogoView;
        r11 = r0.shouldLayout(r11);
        if (r11 == 0) goto L_0x00d8;
    L_0x00c9:
        if (r1 == 0) goto L_0x00d2;
    L_0x00cb:
        r11 = r0.mLogoView;
        r10 = r0.layoutChildRight(r11, r10, r12, r14);
        goto L_0x00d8;
    L_0x00d2:
        r11 = r0.mLogoView;
        r2 = r0.layoutChildLeft(r11, r2, r12, r14);
    L_0x00d8:
        r11 = r0.mTitleTextView;
        r11 = r0.shouldLayout(r11);
        r3 = r0.mSubtitleTextView;
        r3 = r0.shouldLayout(r3);
        r19 = 0;
        if (r11 == 0) goto L_0x0105;
    L_0x00e8:
        r20 = r13;
        r13 = r0.mTitleTextView;
        r13 = r13.getLayoutParams();
        r13 = (android.widget.Toolbar.LayoutParams) r13;
        r21 = r15;
        r15 = r13.topMargin;
        r22 = r7;
        r7 = r0.mTitleTextView;
        r7 = r7.getMeasuredHeight();
        r15 = r15 + r7;
        r7 = r13.bottomMargin;
        r15 = r15 + r7;
        r19 = r19 + r15;
        goto L_0x010b;
    L_0x0105:
        r22 = r7;
        r20 = r13;
        r21 = r15;
    L_0x010b:
        if (r3 == 0) goto L_0x0123;
    L_0x010d:
        r7 = r0.mSubtitleTextView;
        r7 = r7.getLayoutParams();
        r7 = (android.widget.Toolbar.LayoutParams) r7;
        r13 = r7.topMargin;
        r15 = r0.mSubtitleTextView;
        r15 = r15.getMeasuredHeight();
        r13 = r13 + r15;
        r15 = r7.bottomMargin;
        r13 = r13 + r15;
        r19 = r19 + r13;
    L_0x0123:
        if (r11 != 0) goto L_0x0136;
    L_0x0125:
        if (r3 == 0) goto L_0x0128;
    L_0x0127:
        goto L_0x0136;
    L_0x0128:
        r27 = r1;
        r29 = r2;
        r25 = r4;
        r30 = r5;
        r26 = r6;
        r28 = r14;
        goto L_0x02d7;
    L_0x0136:
        if (r11 == 0) goto L_0x013b;
    L_0x0138:
        r7 = r0.mTitleTextView;
        goto L_0x013d;
    L_0x013b:
        r7 = r0.mSubtitleTextView;
    L_0x013d:
        if (r3 == 0) goto L_0x0142;
    L_0x013f:
        r13 = r0.mSubtitleTextView;
        goto L_0x0144;
    L_0x0142:
        r13 = r0.mTitleTextView;
    L_0x0144:
        r15 = r7.getLayoutParams();
        r15 = (android.widget.Toolbar.LayoutParams) r15;
        r23 = r13.getLayoutParams();
        r24 = r7;
        r7 = r23;
        r7 = (android.widget.Toolbar.LayoutParams) r7;
        if (r11 == 0) goto L_0x0161;
    L_0x0156:
        r23 = r13;
        r13 = r0.mTitleTextView;
        r13 = r13.getMeasuredWidth();
        if (r13 > 0) goto L_0x016d;
    L_0x0160:
        goto L_0x0163;
    L_0x0161:
        r23 = r13;
    L_0x0163:
        if (r3 == 0) goto L_0x016f;
    L_0x0165:
        r13 = r0.mSubtitleTextView;
        r13 = r13.getMeasuredWidth();
        if (r13 <= 0) goto L_0x016f;
    L_0x016d:
        r13 = 1;
        goto L_0x0170;
    L_0x016f:
        r13 = 0;
    L_0x0170:
        r25 = r4;
        r4 = r0.mGravity;
        r4 = r4 & 112;
        r26 = r6;
        r6 = 48;
        if (r4 == r6) goto L_0x01c9;
    L_0x017c:
        r6 = 80;
        if (r4 == r6) goto L_0x01ba;
    L_0x0180:
        r4 = r5 - r8;
        r4 = r4 - r9;
        r6 = r4 - r19;
        r6 = r6 / 2;
        r27 = r4;
        r4 = r15.topMargin;
        r28 = r14;
        r14 = r0.mTitleMarginTop;
        r4 = r4 + r14;
        if (r6 >= r4) goto L_0x019b;
    L_0x0192:
        r4 = r15.topMargin;
        r14 = r0.mTitleMarginTop;
        r6 = r4 + r14;
        r29 = r2;
        goto L_0x01b7;
    L_0x019b:
        r4 = r5 - r9;
        r4 = r4 - r19;
        r4 = r4 - r6;
        r4 = r4 - r8;
        r14 = r15.bottomMargin;
        r29 = r2;
        r2 = r0.mTitleMarginBottom;
        r14 = r14 + r2;
        if (r4 >= r14) goto L_0x01b7;
    L_0x01aa:
        r2 = r7.bottomMargin;
        r14 = r0.mTitleMarginBottom;
        r2 = r2 + r14;
        r2 = r2 - r4;
        r2 = r6 - r2;
        r14 = 0;
        r6 = java.lang.Math.max(r14, r2);
    L_0x01b7:
        r2 = r8 + r6;
        goto L_0x01d8;
    L_0x01ba:
        r29 = r2;
        r28 = r14;
        r2 = r5 - r9;
        r4 = r7.bottomMargin;
        r2 = r2 - r4;
        r4 = r0.mTitleMarginBottom;
        r2 = r2 - r4;
        r2 = r2 - r19;
        goto L_0x01d8;
    L_0x01c9:
        r29 = r2;
        r28 = r14;
        r2 = r33.getPaddingTop();
        r4 = r15.topMargin;
        r2 = r2 + r4;
        r4 = r0.mTitleMarginTop;
        r2 = r2 + r4;
    L_0x01d8:
        if (r1 == 0) goto L_0x025a;
    L_0x01da:
        if (r13 == 0) goto L_0x01df;
    L_0x01dc:
        r4 = r0.mTitleMarginStart;
        goto L_0x01e0;
    L_0x01df:
        r4 = 0;
    L_0x01e0:
        r6 = 1;
        r14 = r12[r6];
        r4 = r4 - r14;
        r14 = 0;
        r17 = java.lang.Math.max(r14, r4);
        r10 = r10 - r17;
        r27 = r1;
        r1 = -r4;
        r1 = java.lang.Math.max(r14, r1);
        r12[r6] = r1;
        r1 = r10;
        r6 = r10;
        if (r11 == 0) goto L_0x0223;
    L_0x01f8:
        r14 = r0.mTitleTextView;
        r14 = r14.getLayoutParams();
        r14 = (android.widget.Toolbar.LayoutParams) r14;
        r18 = r4;
        r4 = r0.mTitleTextView;
        r4 = r4.getMeasuredWidth();
        r4 = r1 - r4;
        r30 = r5;
        r5 = r0.mTitleTextView;
        r5 = r5.getMeasuredHeight();
        r5 = r5 + r2;
        r31 = r7;
        r7 = r0.mTitleTextView;
        r7.layout(r4, r2, r1, r5);
        r7 = r0.mTitleMarginEnd;
        r1 = r4 - r7;
        r7 = r14.bottomMargin;
        r2 = r5 + r7;
        goto L_0x0229;
    L_0x0223:
        r18 = r4;
        r30 = r5;
        r31 = r7;
    L_0x0229:
        if (r3 == 0) goto L_0x0251;
    L_0x022b:
        r4 = r0.mSubtitleTextView;
        r4 = r4.getLayoutParams();
        r4 = (android.widget.Toolbar.LayoutParams) r4;
        r5 = r4.topMargin;
        r2 = r2 + r5;
        r5 = r0.mSubtitleTextView;
        r5 = r5.getMeasuredWidth();
        r5 = r6 - r5;
        r7 = r0.mSubtitleTextView;
        r7 = r7.getMeasuredHeight();
        r7 = r7 + r2;
        r14 = r0.mSubtitleTextView;
        r14.layout(r5, r2, r6, r7);
        r14 = r0.mTitleMarginEnd;
        r6 = r6 - r14;
        r14 = r4.bottomMargin;
        r2 = r7 + r14;
    L_0x0251:
        if (r13 == 0) goto L_0x0258;
    L_0x0253:
        r4 = java.lang.Math.min(r1, r6);
        r10 = r4;
    L_0x0258:
        goto L_0x02d7;
    L_0x025a:
        r27 = r1;
        r30 = r5;
        r31 = r7;
        if (r13 == 0) goto L_0x0265;
    L_0x0262:
        r1 = r0.mTitleMarginStart;
        goto L_0x0266;
    L_0x0265:
        r1 = 0;
    L_0x0266:
        r4 = 0;
        r5 = r12[r4];
        r1 = r1 - r5;
        r5 = java.lang.Math.max(r4, r1);
        r5 = r29 + r5;
        r6 = -r1;
        r6 = java.lang.Math.max(r4, r6);
        r12[r4] = r6;
        r4 = r5;
        r6 = r5;
        if (r11 == 0) goto L_0x02a3;
    L_0x027b:
        r7 = r0.mTitleTextView;
        r7 = r7.getLayoutParams();
        r7 = (android.widget.Toolbar.LayoutParams) r7;
        r14 = r0.mTitleTextView;
        r14 = r14.getMeasuredWidth();
        r14 = r14 + r4;
        r18 = r1;
        r1 = r0.mTitleTextView;
        r1 = r1.getMeasuredHeight();
        r1 = r1 + r2;
        r29 = r5;
        r5 = r0.mTitleTextView;
        r5.layout(r4, r2, r14, r1);
        r5 = r0.mTitleMarginEnd;
        r4 = r14 + r5;
        r5 = r7.bottomMargin;
        r2 = r1 + r5;
        goto L_0x02a7;
    L_0x02a3:
        r18 = r1;
        r29 = r5;
    L_0x02a7:
        if (r3 == 0) goto L_0x02cf;
    L_0x02a9:
        r1 = r0.mSubtitleTextView;
        r1 = r1.getLayoutParams();
        r1 = (android.widget.Toolbar.LayoutParams) r1;
        r5 = r1.topMargin;
        r2 = r2 + r5;
        r5 = r0.mSubtitleTextView;
        r5 = r5.getMeasuredWidth();
        r5 = r5 + r6;
        r7 = r0.mSubtitleTextView;
        r7 = r7.getMeasuredHeight();
        r7 = r7 + r2;
        r14 = r0.mSubtitleTextView;
        r14.layout(r6, r2, r5, r7);
        r14 = r0.mTitleMarginEnd;
        r6 = r5 + r14;
        r14 = r1.bottomMargin;
        r2 = r7 + r14;
    L_0x02cf:
        if (r13 == 0) goto L_0x02d7;
    L_0x02d1:
        r1 = java.lang.Math.max(r4, r6);
        r29 = r1;
    L_0x02d7:
        r1 = r0.mTempViews;
        r2 = 3;
        r0.addCustomViewsWithGravity(r1, r2);
        r1 = r0.mTempViews;
        r1 = r1.size();
        r2 = 0;
        r4 = r29;
    L_0x02e6:
        if (r2 >= r1) goto L_0x02f9;
    L_0x02e8:
        r5 = r0.mTempViews;
        r5 = r5.get(r2);
        r5 = (android.view.View) r5;
        r6 = r28;
        r4 = r0.layoutChildLeft(r5, r4, r12, r6);
        r2 = r2 + 1;
        goto L_0x02e6;
    L_0x02f9:
        r6 = r28;
        r2 = r0.mTempViews;
        r5 = 5;
        r0.addCustomViewsWithGravity(r2, r5);
        r2 = r0.mTempViews;
        r2 = r2.size();
        r5 = 0;
    L_0x0308:
        if (r5 >= r2) goto L_0x0319;
    L_0x030a:
        r7 = r0.mTempViews;
        r7 = r7.get(r5);
        r7 = (android.view.View) r7;
        r10 = r0.layoutChildRight(r7, r10, r12, r6);
        r5 = r5 + 1;
        goto L_0x0308;
    L_0x0319:
        r5 = r0.mTempViews;
        r7 = 1;
        r0.addCustomViewsWithGravity(r5, r7);
        r5 = r0.mTempViews;
        r5 = r0.getViewListMeasuredWidth(r5, r12);
        r7 = r25 - r26;
        r7 = r7 - r22;
        r7 = r7 / 2;
        r7 = r26 + r7;
        r13 = r5 / 2;
        r14 = r7 - r13;
        r15 = r14 + r5;
        if (r14 >= r4) goto L_0x0337;
    L_0x0335:
        r14 = r4;
        goto L_0x033d;
    L_0x0337:
        if (r15 <= r10) goto L_0x033d;
    L_0x0339:
        r17 = r15 - r10;
        r14 = r14 - r17;
    L_0x033d:
        r17 = r1;
        r1 = r0.mTempViews;
        r1 = r1.size();
        r18 = 0;
        r32 = r18;
        r18 = r2;
        r2 = r14;
        r14 = r32;
    L_0x034e:
        if (r14 >= r1) goto L_0x0363;
    L_0x0350:
        r23 = r1;
        r1 = r0.mTempViews;
        r1 = r1.get(r14);
        r1 = (android.view.View) r1;
        r2 = r0.layoutChildLeft(r1, r2, r12, r6);
        r14 = r14 + 1;
        r1 = r23;
        goto L_0x034e;
    L_0x0363:
        r23 = r1;
        r1 = r0.mTempViews;
        r1.clear();
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.widget.Toolbar.onLayout(boolean, int, int, int, int):void");
    }

    private int getViewListMeasuredWidth(List<View> views, int[] collapsingMargins) {
        int collapseLeft = collapsingMargins[0];
        int collapseRight = collapsingMargins[1];
        int width = 0;
        int count = views.size();
        for (int i = 0; i < count; i++) {
            View v = (View) views.get(i);
            LayoutParams lp = (LayoutParams) v.getLayoutParams();
            int l = lp.leftMargin - collapseLeft;
            int r = lp.rightMargin - collapseRight;
            int leftMargin = Math.max(0, l);
            int rightMargin = Math.max(0, r);
            collapseLeft = Math.max(0, -l);
            collapseRight = Math.max(0, -r);
            width += (v.getMeasuredWidth() + leftMargin) + rightMargin;
        }
        return width;
    }

    private int layoutChildLeft(View child, int left, int[] collapsingMargins, int alignmentHeight) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int l = lp.leftMargin - collapsingMargins[0];
        left += Math.max(0, l);
        collapsingMargins[0] = Math.max(0, -l);
        int top = getChildTop(child, alignmentHeight);
        int childWidth = child.getMeasuredWidth();
        child.layout(left, top, left + childWidth, child.getMeasuredHeight() + top);
        return left + (lp.rightMargin + childWidth);
    }

    private int layoutChildRight(View child, int right, int[] collapsingMargins, int alignmentHeight) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int r = lp.rightMargin - collapsingMargins[1];
        right -= Math.max(0, r);
        collapsingMargins[1] = Math.max(0, -r);
        int top = getChildTop(child, alignmentHeight);
        int childWidth = child.getMeasuredWidth();
        child.layout(right - childWidth, top, right, child.getMeasuredHeight() + top);
        return right - (lp.leftMargin + childWidth);
    }

    private int getChildTop(View child, int alignmentHeight) {
        LayoutParams lp = (LayoutParams) child.getLayoutParams();
        int childHeight = child.getMeasuredHeight();
        int alignmentOffset = alignmentHeight > 0 ? (childHeight - alignmentHeight) / 2 : 0;
        int childVerticalGravity = getChildVerticalGravity(lp.gravity);
        if (childVerticalGravity == 48) {
            return getPaddingTop() - alignmentOffset;
        }
        if (childVerticalGravity == 80) {
            return (((getHeight() - getPaddingBottom()) - childHeight) - lp.bottomMargin) - alignmentOffset;
        }
        childVerticalGravity = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int height = getHeight();
        int spaceAbove = (((height - childVerticalGravity) - paddingBottom) - childHeight) / 2;
        if (spaceAbove < lp.topMargin) {
            spaceAbove = lp.topMargin;
        } else {
            int spaceBelow = (((height - paddingBottom) - childHeight) - spaceAbove) - childVerticalGravity;
            if (spaceBelow < lp.bottomMargin) {
                spaceAbove = Math.max(0, spaceAbove - (lp.bottomMargin - spaceBelow));
            }
        }
        return childVerticalGravity + spaceAbove;
    }

    private int getChildVerticalGravity(int gravity) {
        int vgrav = gravity & 112;
        if (vgrav == 16 || vgrav == 48 || vgrav == 80) {
            return vgrav;
        }
        return this.mGravity & 112;
    }

    private void addCustomViewsWithGravity(List<View> views, int gravity) {
        boolean z = true;
        if (getLayoutDirection() != 1) {
            z = false;
        }
        boolean isRtl = z;
        int childCount = getChildCount();
        int absGrav = Gravity.getAbsoluteGravity(gravity, getLayoutDirection());
        views.clear();
        int i;
        View child;
        LayoutParams lp;
        if (isRtl) {
            for (i = childCount - 1; i >= 0; i--) {
                child = getChildAt(i);
                lp = (LayoutParams) child.getLayoutParams();
                if (lp.mViewType == 0 && shouldLayout(child) && getChildHorizontalGravity(lp.gravity) == absGrav) {
                    views.add(child);
                }
            }
            return;
        }
        for (i = 0; i < childCount; i++) {
            child = getChildAt(i);
            lp = (LayoutParams) child.getLayoutParams();
            if (lp.mViewType == 0 && shouldLayout(child) && getChildHorizontalGravity(lp.gravity) == absGrav) {
                views.add(child);
            }
        }
    }

    private int getChildHorizontalGravity(int gravity) {
        int ld = getLayoutDirection();
        int hGrav = Gravity.getAbsoluteGravity(gravity, ld) & 7;
        if (hGrav != 1) {
            int i = 3;
            if (!(hGrav == 3 || hGrav == 5)) {
                if (ld == 1) {
                    i = 5;
                }
                return i;
            }
        }
        return hGrav;
    }

    private boolean shouldLayout(View view) {
        return (view == null || view.getParent() != this || view.getVisibility() == 8) ? false : true;
    }

    private int getHorizontalMargins(View v) {
        MarginLayoutParams mlp = (MarginLayoutParams) v.getLayoutParams();
        return mlp.getMarginStart() + mlp.getMarginEnd();
    }

    private int getVerticalMargins(View v) {
        MarginLayoutParams mlp = (MarginLayoutParams) v.getLayoutParams();
        return mlp.topMargin + mlp.bottomMargin;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p instanceof LayoutParams) {
            return new LayoutParams((LayoutParams) p);
        }
        if (p instanceof android.app.ActionBar.LayoutParams) {
            return new LayoutParams((android.app.ActionBar.LayoutParams) p);
        }
        if (p instanceof MarginLayoutParams) {
            return new LayoutParams((MarginLayoutParams) p);
        }
        return new LayoutParams(p);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(-2, -2);
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p) && (p instanceof LayoutParams);
    }

    private static boolean isCustomView(View child) {
        return ((LayoutParams) child.getLayoutParams()).mViewType == 0;
    }

    public DecorToolbar getWrapper() {
        if (this.mWrapper == null) {
            this.mWrapper = new ToolbarWidgetWrapper(this, true);
        }
        return this.mWrapper;
    }

    /* Access modifiers changed, original: 0000 */
    public void removeChildrenForExpandedActionView() {
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View child = getChildAt(i);
            if (!(((LayoutParams) child.getLayoutParams()).mViewType == 2 || child == this.mMenuView)) {
                removeViewAt(i);
                this.mHiddenViews.add(child);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void addChildrenForExpandedActionView() {
        for (int i = this.mHiddenViews.size() - 1; i >= 0; i--) {
            addView((View) this.mHiddenViews.get(i));
        }
        this.mHiddenViews.clear();
    }

    private boolean isChildOrHidden(View child) {
        return child.getParent() == this || this.mHiddenViews.contains(child);
    }

    public void setCollapsible(boolean collapsible) {
        this.mCollapsible = collapsible;
        requestLayout();
    }

    public void setMenuCallbacks(Callback pcb, MenuBuilder.Callback mcb) {
        this.mActionMenuPresenterCallback = pcb;
        this.mMenuBuilderCallback = mcb;
        ActionMenuView actionMenuView = this.mMenuView;
        if (actionMenuView != null) {
            actionMenuView.setMenuCallbacks(pcb, mcb);
        }
    }

    private void ensureContentInsets() {
        if (this.mContentInsets == null) {
            this.mContentInsets = new RtlSpacingHelper();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public ActionMenuPresenter getOuterActionMenuPresenter() {
        return this.mOuterActionMenuPresenter;
    }

    /* Access modifiers changed, original: 0000 */
    public Context getPopupContext() {
        return this.mPopupContext;
    }
}
