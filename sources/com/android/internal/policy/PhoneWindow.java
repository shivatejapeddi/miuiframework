package com.android.internal.policy;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.provider.Settings.Global;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.IRotationWatcher.Stub;
import android.view.IWindowManager;
import android.view.InputDevice;
import android.view.InputEvent;
import android.view.InputQueue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.SurfaceHolder.Callback2;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewManager;
import android.view.ViewParent;
import android.view.ViewRootImpl;
import android.view.ViewRootImpl.ActivityConfigCallback;
import android.view.Window;
import android.view.WindowInsetsController;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.view.menu.ContextMenuBuilder;
import com.android.internal.view.menu.IconMenuPresenter;
import com.android.internal.view.menu.ListMenuPresenter;
import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuBuilder.Callback;
import com.android.internal.view.menu.MenuDialogHelper;
import com.android.internal.view.menu.MenuHelper;
import com.android.internal.view.menu.MenuPresenter;
import com.android.internal.view.menu.MenuView;
import com.android.internal.widget.DecorContentParent;
import com.android.internal.widget.SwipeDismissLayout;
import com.android.internal.widget.SwipeDismissLayout.OnDismissedListener;
import com.android.internal.widget.SwipeDismissLayout.OnSwipeProgressChangedListener;
import com.miui.internal.variable.api.v29.Internal_Policy_Impl_PhoneWindow.Extension;
import com.miui.internal.variable.api.v29.Internal_Policy_Impl_PhoneWindow.Interface;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import miui.os.MiuiInit;

public class PhoneWindow extends Window implements Callback {
    private static final String ACTION_BAR_TAG = "android:ActionBar";
    private static final int CUSTOM_TITLE_COMPATIBLE_FEATURES = 13505;
    private static final boolean DEBUG = false;
    private static final int DEFAULT_BACKGROUND_FADE_DURATION_MS = 300;
    static final int FLAG_RESOURCE_SET_ICON = 1;
    static final int FLAG_RESOURCE_SET_ICON_FALLBACK = 4;
    static final int FLAG_RESOURCE_SET_LOGO = 2;
    private static final String FOCUSED_ID_TAG = "android:focusedViewId";
    private static final String PANELS_TAG = "android:Panels";
    private static final String TAG = "PhoneWindow";
    private static final Transition USE_DEFAULT_TRANSITION = new TransitionSet();
    private static final String VIEWS_TAG = "android:views";
    static final RotationWatcher sRotationWatcher = new RotationWatcher();
    private ActionMenuPresenterCallback mActionMenuPresenterCallback;
    private ActivityConfigCallback mActivityConfigCallback;
    private Boolean mAllowEnterTransitionOverlap;
    private Boolean mAllowReturnTransitionOverlap;
    private boolean mAlwaysReadCloseOnTouchAttr;
    private AudioManager mAudioManager;
    Drawable mBackgroundDrawable;
    private long mBackgroundFadeDurationMillis;
    Drawable mBackgroundFallbackDrawable;
    private ProgressBar mCircularProgressBar;
    private boolean mClipToOutline;
    private boolean mClosingActionMenu;
    ViewGroup mContentParent;
    private boolean mContentParentExplicitlySet;
    private Scene mContentScene;
    ContextMenuBuilder mContextMenu;
    final PhoneWindowMenuCallback mContextMenuCallback;
    MenuHelper mContextMenuHelper;
    private DecorView mDecor;
    private int mDecorCaptionShade;
    DecorContentParent mDecorContentParent;
    private DrawableFeatureState[] mDrawables;
    private float mElevation;
    boolean mEnsureNavigationBarContrastWhenTransparent;
    boolean mEnsureStatusBarContrastWhenTransparent;
    private Transition mEnterTransition;
    private Transition mExitTransition;
    TypedValue mFixedHeightMajor;
    TypedValue mFixedHeightMinor;
    TypedValue mFixedWidthMajor;
    TypedValue mFixedWidthMinor;
    private boolean mForceDecorInstall;
    private boolean mForcedNavigationBarColor;
    private boolean mForcedStatusBarColor;
    private int mFrameResource;
    private ProgressBar mHorizontalProgressBar;
    int mIconRes;
    private int mInvalidatePanelMenuFeatures;
    private boolean mInvalidatePanelMenuPosted;
    private final Runnable mInvalidatePanelMenuRunnable;
    boolean mIsFloating;
    private boolean mIsStartingWindow;
    private boolean mIsTranslucent;
    private KeyguardManager mKeyguardManager;
    private LayoutInflater mLayoutInflater;
    private ImageView mLeftIconView;
    private boolean mLoadElevation;
    int mLogoRes;
    private MediaController mMediaController;
    private MediaSessionManager mMediaSessionManager;
    final TypedValue mMinWidthMajor;
    final TypedValue mMinWidthMinor;
    int mNavigationBarColor;
    int mNavigationBarDividerColor;
    int mPanelChordingKey;
    private PanelMenuPresenterCallback mPanelMenuPresenterCallback;
    private PanelFeatureState[] mPanels;
    PanelFeatureState mPreparedPanel;
    private Transition mReenterTransition;
    int mResourcesSetFlags;
    private Transition mReturnTransition;
    private ImageView mRightIconView;
    private Transition mSharedElementEnterTransition;
    private Transition mSharedElementExitTransition;
    private Transition mSharedElementReenterTransition;
    private Transition mSharedElementReturnTransition;
    private Boolean mSharedElementsUseOverlay;
    int mStatusBarColor;
    private boolean mSupportsPictureInPicture;
    InputQueue.Callback mTakeInputQueueCallback;
    Callback2 mTakeSurfaceCallback;
    private int mTextColor;
    private int mTheme;
    @UnsupportedAppUsage
    private CharSequence mTitle;
    private int mTitleColor;
    private TextView mTitleView;
    private TransitionManager mTransitionManager;
    private int mUiOptions;
    private boolean mUseDecorContext;
    private int mVolumeControlStreamType;

    private final class ActionMenuPresenterCallback implements MenuPresenter.Callback {
        private ActionMenuPresenterCallback() {
        }

        /* synthetic */ ActionMenuPresenterCallback(PhoneWindow x0, AnonymousClass1 x1) {
            this();
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            Window.Callback cb = PhoneWindow.this.getCallback();
            if (cb == null) {
                return false;
            }
            cb.onMenuOpened(8, subMenu);
            return true;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            PhoneWindow.this.checkCloseActionMenu(menu);
        }
    }

    private static final class DrawableFeatureState {
        int alpha = 255;
        Drawable child;
        Drawable cur;
        int curAlpha = 255;
        Drawable def;
        final int featureId;
        Drawable local;
        int resid;
        Uri uri;

        DrawableFeatureState(int _featureId) {
            this.featureId = _featureId;
        }
    }

    static final class PanelFeatureState {
        int background;
        View createdPanelView;
        DecorView decorView;
        int featureId;
        Bundle frozenActionViewState;
        Bundle frozenMenuState;
        int fullBackground;
        int gravity;
        IconMenuPresenter iconMenuPresenter;
        boolean isCompact;
        boolean isHandled;
        boolean isInExpandedMode;
        boolean isOpen;
        boolean isPrepared;
        ListMenuPresenter listMenuPresenter;
        int listPresenterTheme;
        MenuBuilder menu;
        public boolean qwertyMode;
        boolean refreshDecorView = false;
        boolean refreshMenuContent;
        View shownPanelView;
        boolean wasLastExpanded;
        boolean wasLastOpen;
        int windowAnimations;
        int x;
        int y;

        private static class SavedState implements Parcelable {
            public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
                public SavedState createFromParcel(Parcel in) {
                    return SavedState.readFromParcel(in);
                }

                public SavedState[] newArray(int size) {
                    return new SavedState[size];
                }
            };
            int featureId;
            boolean isInExpandedMode;
            boolean isOpen;
            Bundle menuState;

            private SavedState() {
            }

            /* synthetic */ SavedState(AnonymousClass1 x0) {
                this();
            }

            public int describeContents() {
                return 0;
            }

            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.featureId);
                dest.writeInt(this.isOpen);
                dest.writeInt(this.isInExpandedMode);
                if (this.isOpen) {
                    dest.writeBundle(this.menuState);
                }
            }

            private static SavedState readFromParcel(Parcel source) {
                SavedState savedState = new SavedState();
                savedState.featureId = source.readInt();
                boolean z = false;
                savedState.isOpen = source.readInt() == 1;
                if (source.readInt() == 1) {
                    z = true;
                }
                savedState.isInExpandedMode = z;
                if (savedState.isOpen) {
                    savedState.menuState = source.readBundle();
                }
                return savedState;
            }
        }

        PanelFeatureState(int featureId) {
            this.featureId = featureId;
        }

        public boolean isInListMode() {
            return this.isInExpandedMode || this.isCompact;
        }

        public boolean hasPanelItems() {
            View view = this.shownPanelView;
            boolean z = false;
            if (view == null) {
                return false;
            }
            if (this.createdPanelView != null) {
                return true;
            }
            if (this.isCompact || this.isInExpandedMode) {
                if (this.listMenuPresenter.getAdapter().getCount() > 0) {
                    z = true;
                }
                return z;
            }
            if (((ViewGroup) view).getChildCount() > 0) {
                z = true;
            }
            return z;
        }

        public void clearMenuPresenters() {
            MenuBuilder menuBuilder = this.menu;
            if (menuBuilder != null) {
                menuBuilder.removeMenuPresenter(this.iconMenuPresenter);
                this.menu.removeMenuPresenter(this.listMenuPresenter);
            }
            this.iconMenuPresenter = null;
            this.listMenuPresenter = null;
        }

        /* Access modifiers changed, original: 0000 */
        public void setStyle(Context context) {
            TypedArray a = context.obtainStyledAttributes(R.styleable.Theme);
            this.background = a.getResourceId(46, 0);
            this.fullBackground = a.getResourceId(47, 0);
            this.windowAnimations = a.getResourceId(93, 0);
            this.isCompact = a.getBoolean(314, false);
            this.listPresenterTheme = a.getResourceId(315, R.style.Theme_ExpandedMenu);
            a.recycle();
        }

        /* Access modifiers changed, original: 0000 */
        public void setMenu(MenuBuilder menu) {
            MenuBuilder menuBuilder = this.menu;
            if (menu != menuBuilder) {
                if (menuBuilder != null) {
                    menuBuilder.removeMenuPresenter(this.iconMenuPresenter);
                    this.menu.removeMenuPresenter(this.listMenuPresenter);
                }
                this.menu = menu;
                if (menu != null) {
                    IconMenuPresenter iconMenuPresenter = this.iconMenuPresenter;
                    if (iconMenuPresenter != null) {
                        menu.addMenuPresenter(iconMenuPresenter);
                    }
                    ListMenuPresenter listMenuPresenter = this.listMenuPresenter;
                    if (listMenuPresenter != null) {
                        menu.addMenuPresenter(listMenuPresenter);
                    }
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public MenuView getListMenuView(Context context, MenuPresenter.Callback cb) {
            if (this.menu == null) {
                return null;
            }
            if (!this.isCompact) {
                getIconMenuView(context, cb);
            }
            if (this.listMenuPresenter == null) {
                this.listMenuPresenter = new ListMenuPresenter((int) R.layout.list_menu_item_layout, this.listPresenterTheme);
                this.listMenuPresenter.setCallback(cb);
                this.listMenuPresenter.setId(R.id.list_menu_presenter);
                this.menu.addMenuPresenter(this.listMenuPresenter);
            }
            IconMenuPresenter iconMenuPresenter = this.iconMenuPresenter;
            if (iconMenuPresenter != null) {
                this.listMenuPresenter.setItemIndexOffset(iconMenuPresenter.getNumActualItemsShown());
            }
            return this.listMenuPresenter.getMenuView(this.decorView);
        }

        /* Access modifiers changed, original: 0000 */
        public MenuView getIconMenuView(Context context, MenuPresenter.Callback cb) {
            if (this.menu == null) {
                return null;
            }
            if (this.iconMenuPresenter == null) {
                this.iconMenuPresenter = new IconMenuPresenter(context);
                this.iconMenuPresenter.setCallback(cb);
                this.iconMenuPresenter.setId(R.id.icon_menu_presenter);
                this.menu.addMenuPresenter(this.iconMenuPresenter);
            }
            return this.iconMenuPresenter.getMenuView(this.decorView);
        }

        /* Access modifiers changed, original: 0000 */
        public Parcelable onSaveInstanceState() {
            SavedState savedState = new SavedState();
            savedState.featureId = this.featureId;
            savedState.isOpen = this.isOpen;
            savedState.isInExpandedMode = this.isInExpandedMode;
            if (this.menu != null) {
                savedState.menuState = new Bundle();
                this.menu.savePresenterStates(savedState.menuState);
            }
            return savedState;
        }

        /* Access modifiers changed, original: 0000 */
        public void onRestoreInstanceState(Parcelable state) {
            SavedState savedState = (SavedState) state;
            this.featureId = savedState.featureId;
            this.wasLastOpen = savedState.isOpen;
            this.wasLastExpanded = savedState.isInExpandedMode;
            this.frozenMenuState = savedState.menuState;
            this.createdPanelView = null;
            this.shownPanelView = null;
            this.decorView = null;
        }

        /* Access modifiers changed, original: 0000 */
        public void applyFrozenState() {
            MenuBuilder menuBuilder = this.menu;
            if (menuBuilder != null) {
                Bundle bundle = this.frozenMenuState;
                if (bundle != null) {
                    menuBuilder.restorePresenterStates(bundle);
                    this.frozenMenuState = null;
                }
            }
        }
    }

    private class PanelMenuPresenterCallback implements MenuPresenter.Callback {
        private PanelMenuPresenterCallback() {
        }

        /* synthetic */ PanelMenuPresenterCallback(PhoneWindow x0, AnonymousClass1 x1) {
            this();
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            Menu parentMenu = menu.getRootMenu();
            boolean isSubMenu = parentMenu != menu;
            PanelFeatureState panel = PhoneWindow.this.findMenuPanel(isSubMenu ? parentMenu : menu);
            if (panel == null) {
                return;
            }
            if (isSubMenu) {
                PhoneWindow.this.callOnPanelClosed(panel.featureId, panel, parentMenu);
                PhoneWindow.this.closePanel(panel, true);
                return;
            }
            PhoneWindow.this.closePanel(panel, allMenusAreClosing);
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            if (subMenu == null && PhoneWindow.this.hasFeature(8)) {
                Window.Callback cb = PhoneWindow.this.getCallback();
                if (!(cb == null || PhoneWindow.this.isDestroyed())) {
                    cb.onMenuOpened(8, subMenu);
                }
            }
            return true;
        }
    }

    public static final class PhoneWindowMenuCallback implements Callback, MenuPresenter.Callback {
        private static final int FEATURE_ID = 6;
        private boolean mShowDialogForSubmenu;
        private MenuDialogHelper mSubMenuHelper;
        private final PhoneWindow mWindow;

        public PhoneWindowMenuCallback(PhoneWindow window) {
            this.mWindow = window;
        }

        public void onCloseMenu(MenuBuilder menu, boolean allMenusAreClosing) {
            if (menu.getRootMenu() != menu) {
                onCloseSubMenu(menu);
            }
            if (allMenusAreClosing) {
                Window.Callback callback = this.mWindow.getCallback();
                if (!(callback == null || this.mWindow.isDestroyed())) {
                    callback.onPanelClosed(6, menu);
                }
                if (menu == this.mWindow.mContextMenu) {
                    this.mWindow.dismissContextMenu();
                }
                MenuDialogHelper menuDialogHelper = this.mSubMenuHelper;
                if (menuDialogHelper != null) {
                    menuDialogHelper.dismiss();
                    this.mSubMenuHelper = null;
                }
            }
        }

        private void onCloseSubMenu(MenuBuilder menu) {
            Window.Callback callback = this.mWindow.getCallback();
            if (callback != null && !this.mWindow.isDestroyed()) {
                callback.onPanelClosed(6, menu.getRootMenu());
            }
        }

        public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
            Window.Callback callback = this.mWindow.getCallback();
            return (callback == null || this.mWindow.isDestroyed() || !callback.onMenuItemSelected(6, item)) ? false : true;
        }

        public void onMenuModeChange(MenuBuilder menu) {
        }

        public boolean onOpenSubMenu(MenuBuilder subMenu) {
            if (subMenu == null) {
                return false;
            }
            subMenu.setCallback(this);
            if (!this.mShowDialogForSubmenu) {
                return false;
            }
            this.mSubMenuHelper = new MenuDialogHelper(subMenu);
            this.mSubMenuHelper.show(null);
            return true;
        }

        public void setShowDialogForSubmenu(boolean enabled) {
            this.mShowDialogForSubmenu = enabled;
        }
    }

    static class RotationWatcher extends Stub {
        private Handler mHandler;
        private boolean mIsWatching;
        private final Runnable mRotationChanged = new Runnable() {
            public void run() {
                RotationWatcher.this.dispatchRotationChanged();
            }
        };
        private final ArrayList<WeakReference<PhoneWindow>> mWindows = new ArrayList();

        RotationWatcher() {
        }

        public void onRotationChanged(int rotation) throws RemoteException {
            this.mHandler.post(this.mRotationChanged);
        }

        public void addWindow(PhoneWindow phoneWindow) {
            synchronized (this.mWindows) {
                if (!this.mIsWatching) {
                    try {
                        WindowManagerHolder.sWindowManager.watchRotation(this, phoneWindow.getContext().getDisplayId());
                        this.mHandler = new Handler();
                        this.mIsWatching = true;
                    } catch (RemoteException ex) {
                        Log.e(PhoneWindow.TAG, "Couldn't start watching for device rotation", ex);
                    }
                }
                this.mWindows.add(new WeakReference(phoneWindow));
            }
        }

        public void removeWindow(PhoneWindow phoneWindow) {
            synchronized (this.mWindows) {
                int i = 0;
                while (i < this.mWindows.size()) {
                    PhoneWindow win = (PhoneWindow) ((WeakReference) this.mWindows.get(i)).get();
                    if (win != null) {
                        if (win != phoneWindow) {
                            i++;
                        }
                    }
                    this.mWindows.remove(i);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void dispatchRotationChanged() {
            synchronized (this.mWindows) {
                int i = 0;
                while (i < this.mWindows.size()) {
                    PhoneWindow win = (PhoneWindow) ((WeakReference) this.mWindows.get(i)).get();
                    if (win != null) {
                        win.onOptionsPanelRotationChanged();
                        i++;
                    } else {
                        this.mWindows.remove(i);
                    }
                }
            }
        }
    }

    static class WindowManagerHolder {
        static final IWindowManager sWindowManager = IWindowManager.Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));

        WindowManagerHolder() {
        }
    }

    static {
        Extension.get().bindOriginal(new Interface() {
            public void installDecor(Object o) {
                ((PhoneWindow) o).originalInstallDecor();
            }
        });
    }

    @UnsupportedAppUsage
    public PhoneWindow(Context context) {
        super(context);
        this.mContextMenuCallback = new PhoneWindowMenuCallback(this);
        this.mMinWidthMajor = new TypedValue();
        this.mMinWidthMinor = new TypedValue();
        this.mForceDecorInstall = false;
        this.mContentParentExplicitlySet = false;
        this.mBackgroundDrawable = null;
        this.mBackgroundFallbackDrawable = null;
        this.mLoadElevation = true;
        this.mFrameResource = 0;
        this.mTextColor = 0;
        this.mStatusBarColor = 0;
        this.mNavigationBarColor = 0;
        this.mNavigationBarDividerColor = 0;
        this.mForcedStatusBarColor = false;
        this.mForcedNavigationBarColor = false;
        this.mTitle = null;
        this.mTitleColor = 0;
        this.mAlwaysReadCloseOnTouchAttr = false;
        this.mVolumeControlStreamType = Integer.MIN_VALUE;
        this.mUiOptions = 0;
        this.mInvalidatePanelMenuRunnable = new Runnable() {
            public void run() {
                for (int i = 0; i <= 13; i++) {
                    if ((PhoneWindow.this.mInvalidatePanelMenuFeatures & (1 << i)) != 0) {
                        PhoneWindow.this.doInvalidatePanelMenu(i);
                    }
                }
                PhoneWindow.this.mInvalidatePanelMenuPosted = false;
                PhoneWindow.this.mInvalidatePanelMenuFeatures = 0;
            }
        };
        this.mEnterTransition = null;
        Transition transition = USE_DEFAULT_TRANSITION;
        this.mReturnTransition = transition;
        this.mExitTransition = null;
        this.mReenterTransition = transition;
        this.mSharedElementEnterTransition = null;
        this.mSharedElementReturnTransition = transition;
        this.mSharedElementExitTransition = null;
        this.mSharedElementReenterTransition = transition;
        this.mBackgroundFadeDurationMillis = -1;
        this.mTheme = -1;
        this.mDecorCaptionShade = 0;
        this.mUseDecorContext = false;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    public PhoneWindow(Context context, Window preservedWindow, ActivityConfigCallback activityConfigCallback) {
        this(context);
        boolean z = true;
        this.mUseDecorContext = true;
        if (preservedWindow != null) {
            this.mDecor = (DecorView) preservedWindow.getDecorView();
            this.mElevation = preservedWindow.getElevation();
            this.mLoadElevation = false;
            this.mForceDecorInstall = true;
            getAttributes().token = preservedWindow.getAttributes().token;
        }
        if (!((Global.getInt(context.getContentResolver(), Global.DEVELOPMENT_FORCE_RESIZABLE_ACTIVITIES, 0) != 0) || context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE))) {
            z = false;
        }
        this.mSupportsPictureInPicture = z;
        this.mActivityConfigCallback = activityConfigCallback;
    }

    public final void setContainer(Window container) {
        super.setContainer(container);
    }

    public boolean requestFeature(int featureId) {
        if (this.mContentParentExplicitlySet) {
            throw new AndroidRuntimeException("requestFeature() must be called before adding content");
        }
        int features = getFeatures();
        int newFeatures = (1 << featureId) | features;
        if ((newFeatures & 128) != 0 && (newFeatures & -13506) != 0) {
            throw new AndroidRuntimeException("You cannot combine custom titles with other title features");
        } else if ((features & 2) != 0 && featureId == 8) {
            return false;
        } else {
            if ((features & 256) != 0 && featureId == 1) {
                removeFeature(8);
            }
            String str = "You cannot combine swipe dismissal and the action bar.";
            if ((features & 256) != 0 && featureId == 11) {
                throw new AndroidRuntimeException(str);
            } else if ((features & 2048) != 0 && featureId == 8) {
                throw new AndroidRuntimeException(str);
            } else if (featureId != 5 || !getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_WATCH)) {
                return super.requestFeature(featureId);
            } else {
                throw new AndroidRuntimeException("You cannot use indeterminate progress on a watch.");
            }
        }
    }

    public void setUiOptions(int uiOptions) {
        this.mUiOptions = uiOptions;
    }

    public void setUiOptions(int uiOptions, int mask) {
        this.mUiOptions = (this.mUiOptions & (~mask)) | (uiOptions & mask);
    }

    public TransitionManager getTransitionManager() {
        return this.mTransitionManager;
    }

    public void setTransitionManager(TransitionManager tm) {
        this.mTransitionManager = tm;
    }

    public Scene getContentScene() {
        return this.mContentScene;
    }

    public void setContentView(int layoutResID) {
        if (this.mContentParent == null) {
            installDecor();
        } else if (!hasFeature(12)) {
            this.mContentParent.removeAllViews();
        }
        if (hasFeature(12)) {
            transitionTo(Scene.getSceneForLayout(this.mContentParent, layoutResID, getContext()));
        } else {
            this.mLayoutInflater.inflate(layoutResID, this.mContentParent);
        }
        this.mContentParent.requestApplyInsets();
        Window.Callback cb = getCallback();
        if (!(cb == null || isDestroyed())) {
            cb.onContentChanged();
        }
        this.mContentParentExplicitlySet = true;
    }

    public void setContentView(View view) {
        setContentView(view, new LayoutParams(-1, -1));
    }

    public void setContentView(View view, LayoutParams params) {
        if (this.mContentParent == null) {
            installDecor();
        } else if (!hasFeature(12)) {
            this.mContentParent.removeAllViews();
        }
        if (hasFeature(12)) {
            view.setLayoutParams(params);
            transitionTo(new Scene(this.mContentParent, view));
        } else {
            this.mContentParent.addView(view, params);
        }
        this.mContentParent.requestApplyInsets();
        Window.Callback cb = getCallback();
        if (!(cb == null || isDestroyed())) {
            cb.onContentChanged();
        }
        this.mContentParentExplicitlySet = true;
    }

    public void addContentView(View view, LayoutParams params) {
        if (this.mContentParent == null) {
            installDecor();
        }
        if (hasFeature(12)) {
            Log.v(TAG, "addContentView does not support content transitions");
        }
        this.mContentParent.addView(view, params);
        this.mContentParent.requestApplyInsets();
        Window.Callback cb = getCallback();
        if (cb != null && !isDestroyed()) {
            cb.onContentChanged();
        }
    }

    public void clearContentView() {
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.clearContentView();
        }
    }

    private void transitionTo(Scene scene) {
        if (this.mContentScene == null) {
            scene.enter();
        } else {
            this.mTransitionManager.transitionTo(scene);
        }
        this.mContentScene = scene;
    }

    public View getCurrentFocus() {
        DecorView decorView = this.mDecor;
        return decorView != null ? decorView.findFocus() : null;
    }

    public void takeSurface(Callback2 callback) {
        this.mTakeSurfaceCallback = callback;
    }

    public void takeInputQueue(InputQueue.Callback callback) {
        this.mTakeInputQueueCallback = callback;
    }

    public boolean isFloating() {
        return this.mIsFloating;
    }

    public boolean isTranslucent() {
        return this.mIsTranslucent;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isShowingWallpaper() {
        return (getAttributes().flags & 1048576) != 0;
    }

    public LayoutInflater getLayoutInflater() {
        return this.mLayoutInflater;
    }

    public void setTitle(CharSequence title) {
        setTitle(title, true);
    }

    public void setTitle(CharSequence title, boolean updateAccessibilityTitle) {
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(title);
        } else {
            DecorContentParent decorContentParent = this.mDecorContentParent;
            if (decorContentParent != null) {
                decorContentParent.setWindowTitle(title);
            }
        }
        this.mTitle = title;
        if (updateAccessibilityTitle) {
            WindowManager.LayoutParams params = getAttributes();
            if (!TextUtils.equals(title, params.accessibilityTitle)) {
                params.accessibilityTitle = TextUtils.stringOrSpannedString(title);
                ViewRootImpl vr = this.mDecor;
                if (vr != null) {
                    vr = vr.getViewRootImpl();
                    if (vr != null) {
                        vr.onWindowTitleChanged();
                    }
                }
                dispatchWindowAttributesChanged(getAttributes());
            }
        }
    }

    @Deprecated
    public void setTitleColor(int textColor) {
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setTextColor(textColor);
        }
        this.mTitleColor = textColor;
    }

    public final boolean preparePanel(PanelFeatureState st, KeyEvent event) {
        if (isDestroyed()) {
            return false;
        }
        if (st.isPrepared) {
            return true;
        }
        PanelFeatureState panelFeatureState = this.mPreparedPanel;
        if (!(panelFeatureState == null || panelFeatureState == st)) {
            closePanel(panelFeatureState, false);
        }
        Window.Callback cb = getCallback();
        if (cb != null) {
            st.createdPanelView = cb.onCreatePanelView(st.featureId);
        }
        boolean isActionBarMenu = st.featureId == 0 || st.featureId == 8;
        if (isActionBarMenu) {
            DecorContentParent decorContentParent = this.mDecorContentParent;
            if (decorContentParent != null) {
                decorContentParent.setMenuPrepared();
            }
        }
        if (st.createdPanelView == null) {
            DecorContentParent decorContentParent2;
            if (st.menu == null || st.refreshMenuContent) {
                if (st.menu == null && (!initializePanelMenu(st) || st.menu == null)) {
                    return false;
                }
                if (isActionBarMenu && this.mDecorContentParent != null) {
                    if (this.mActionMenuPresenterCallback == null) {
                        this.mActionMenuPresenterCallback = new ActionMenuPresenterCallback(this, null);
                    }
                    this.mDecorContentParent.setMenu(st.menu, this.mActionMenuPresenterCallback);
                }
                st.menu.stopDispatchingItemsChanged();
                if (cb == null || !cb.onCreatePanelMenu(st.featureId, st.menu)) {
                    st.setMenu(null);
                    if (isActionBarMenu) {
                        decorContentParent2 = this.mDecorContentParent;
                        if (decorContentParent2 != null) {
                            decorContentParent2.setMenu(null, this.mActionMenuPresenterCallback);
                        }
                    }
                    return false;
                }
                st.refreshMenuContent = false;
            }
            st.menu.stopDispatchingItemsChanged();
            if (st.frozenActionViewState != null) {
                st.menu.restoreActionViewStates(st.frozenActionViewState);
                st.frozenActionViewState = null;
            }
            if (cb.onPreparePanel(st.featureId, st.createdPanelView, st.menu)) {
                st.qwertyMode = KeyCharacterMap.load(event != null ? event.getDeviceId() : -1).getKeyboardType() != 1;
                st.menu.setQwertyMode(st.qwertyMode);
                st.menu.startDispatchingItemsChanged();
            } else {
                if (isActionBarMenu) {
                    decorContentParent2 = this.mDecorContentParent;
                    if (decorContentParent2 != null) {
                        decorContentParent2.setMenu(null, this.mActionMenuPresenterCallback);
                    }
                }
                st.menu.startDispatchingItemsChanged();
                return false;
            }
        }
        st.isPrepared = true;
        st.isHandled = false;
        this.mPreparedPanel = st;
        return true;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.mDecorContentParent == null) {
            PanelFeatureState st = getPanelState(0, false);
            if (st != null && st.menu != null) {
                if (st.isOpen) {
                    Bundle state = new Bundle();
                    if (st.iconMenuPresenter != null) {
                        st.iconMenuPresenter.saveHierarchyState(state);
                    }
                    if (st.listMenuPresenter != null) {
                        st.listMenuPresenter.saveHierarchyState(state);
                    }
                    clearMenuViews(st);
                    reopenMenu(false);
                    if (st.iconMenuPresenter != null) {
                        st.iconMenuPresenter.restoreHierarchyState(state);
                    }
                    if (st.listMenuPresenter != null) {
                        st.listMenuPresenter.restoreHierarchyState(state);
                        return;
                    }
                    return;
                }
                clearMenuViews(st);
            }
        }
    }

    public void onMultiWindowModeChanged() {
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.onConfigurationChanged(getContext().getResources().getConfiguration());
        }
    }

    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.updatePictureInPictureOutlineProvider(isInPictureInPictureMode);
        }
    }

    public void reportActivityRelaunched() {
        DecorView decorView = this.mDecor;
        if (decorView != null && decorView.getViewRootImpl() != null) {
            this.mDecor.getViewRootImpl().reportActivityRelaunched();
        }
    }

    private static void clearMenuViews(PanelFeatureState st) {
        st.createdPanelView = null;
        st.refreshDecorView = true;
        st.clearMenuPresenters();
    }

    public final void openPanel(int featureId, KeyEvent event) {
        if (featureId == 0) {
            DecorContentParent decorContentParent = this.mDecorContentParent;
            if (!(decorContentParent == null || !decorContentParent.canShowOverflowMenu() || ViewConfiguration.get(getContext()).hasPermanentMenuKey())) {
                this.mDecorContentParent.showOverflowMenu();
                return;
            }
        }
        openPanel(getPanelState(featureId, true), event);
    }

    private void openPanel(PanelFeatureState st, KeyEvent event) {
        PanelFeatureState panelFeatureState = st;
        if (!panelFeatureState.isOpen && !isDestroyed()) {
            if (panelFeatureState.featureId == 0) {
                Context context = getContext();
                boolean isXLarge = (context.getResources().getConfiguration().screenLayout & 15) == 4;
                boolean isHoneycombApp = context.getApplicationInfo().targetSdkVersion >= 11;
                if (isXLarge && isHoneycombApp) {
                    return;
                }
            }
            Window.Callback cb = getCallback();
            if (cb == null || cb.onMenuOpened(panelFeatureState.featureId, panelFeatureState.menu)) {
                WindowManager wm = getWindowManager();
                if (wm != null && preparePanel(st, event)) {
                    int width = -2;
                    LayoutParams lp;
                    if (panelFeatureState.decorView == null || panelFeatureState.refreshDecorView) {
                        if (panelFeatureState.decorView == null) {
                            if (!initializePanelDecor(st) || panelFeatureState.decorView == null) {
                                return;
                            }
                        } else if (panelFeatureState.refreshDecorView && panelFeatureState.decorView.getChildCount() > 0) {
                            panelFeatureState.decorView.removeAllViews();
                        }
                        if (initializePanelContent(st) && st.hasPanelItems()) {
                            int backgroundResId;
                            lp = panelFeatureState.shownPanelView.getLayoutParams();
                            if (lp == null) {
                                lp = new LayoutParams(-2, -2);
                            }
                            if (lp.width == -1) {
                                backgroundResId = panelFeatureState.fullBackground;
                                width = -1;
                            } else {
                                backgroundResId = panelFeatureState.background;
                            }
                            panelFeatureState.decorView.setWindowBackground(getContext().getDrawable(backgroundResId));
                            ViewParent shownPanelParent = panelFeatureState.shownPanelView.getParent();
                            if (shownPanelParent != null && (shownPanelParent instanceof ViewGroup)) {
                                ((ViewGroup) shownPanelParent).removeView(panelFeatureState.shownPanelView);
                            }
                            panelFeatureState.decorView.addView(panelFeatureState.shownPanelView, lp);
                            if (!panelFeatureState.shownPanelView.hasFocus()) {
                                panelFeatureState.shownPanelView.requestFocus();
                            }
                        } else {
                            return;
                        }
                    } else if (!st.isInListMode()) {
                        width = -1;
                    } else if (panelFeatureState.createdPanelView != null) {
                        lp = panelFeatureState.createdPanelView.getLayoutParams();
                        if (lp != null && lp.width == -1) {
                            width = -1;
                        }
                    }
                    panelFeatureState.isHandled = false;
                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(width, -2, panelFeatureState.x, panelFeatureState.y, 1003, 8519680, panelFeatureState.decorView.mDefaultOpacity);
                    if (panelFeatureState.isCompact) {
                        layoutParams.gravity = getOptionsPanelGravity();
                        sRotationWatcher.addWindow(this);
                    } else {
                        layoutParams.gravity = panelFeatureState.gravity;
                    }
                    layoutParams.windowAnimations = panelFeatureState.windowAnimations;
                    wm.addView(panelFeatureState.decorView, layoutParams);
                    panelFeatureState.isOpen = true;
                    return;
                }
                return;
            }
            closePanel(panelFeatureState, true);
        }
    }

    public final void closePanel(int featureId) {
        if (featureId == 0) {
            DecorContentParent decorContentParent = this.mDecorContentParent;
            if (!(decorContentParent == null || !decorContentParent.canShowOverflowMenu() || ViewConfiguration.get(getContext()).hasPermanentMenuKey())) {
                this.mDecorContentParent.hideOverflowMenu();
                return;
            }
        }
        if (featureId == 6) {
            closeContextMenu();
        } else {
            closePanel(getPanelState(featureId, true), true);
        }
    }

    public final void closePanel(PanelFeatureState st, boolean doCallback) {
        if (doCallback && st.featureId == 0) {
            DecorContentParent decorContentParent = this.mDecorContentParent;
            if (decorContentParent != null && decorContentParent.isOverflowMenuShowing()) {
                checkCloseActionMenu(st.menu);
                return;
            }
        }
        ViewManager wm = getWindowManager();
        if (wm != null && st.isOpen) {
            if (st.decorView != null) {
                wm.removeView(st.decorView);
                if (st.isCompact) {
                    sRotationWatcher.removeWindow(this);
                }
            }
            if (doCallback) {
                callOnPanelClosed(st.featureId, st, null);
            }
        }
        st.isPrepared = false;
        st.isHandled = false;
        st.isOpen = false;
        st.shownPanelView = null;
        if (st.isInExpandedMode) {
            st.refreshDecorView = true;
            st.isInExpandedMode = false;
        }
        if (this.mPreparedPanel == st) {
            this.mPreparedPanel = null;
            this.mPanelChordingKey = 0;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void checkCloseActionMenu(Menu menu) {
        if (!this.mClosingActionMenu) {
            this.mClosingActionMenu = true;
            this.mDecorContentParent.dismissPopups();
            Window.Callback cb = getCallback();
            if (!(cb == null || isDestroyed())) {
                cb.onPanelClosed(8, menu);
            }
            this.mClosingActionMenu = false;
        }
    }

    public final void togglePanel(int featureId, KeyEvent event) {
        PanelFeatureState st = getPanelState(featureId, true);
        if (st.isOpen) {
            closePanel(st, true);
        } else {
            openPanel(st, event);
        }
    }

    public void invalidatePanelMenu(int featureId) {
        this.mInvalidatePanelMenuFeatures |= 1 << featureId;
        if (!this.mInvalidatePanelMenuPosted) {
            DecorView decorView = this.mDecor;
            if (decorView != null) {
                decorView.postOnAnimation(this.mInvalidatePanelMenuRunnable);
                this.mInvalidatePanelMenuPosted = true;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doPendingInvalidatePanelMenu() {
        if (this.mInvalidatePanelMenuPosted) {
            this.mDecor.removeCallbacks(this.mInvalidatePanelMenuRunnable);
            this.mInvalidatePanelMenuRunnable.run();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doInvalidatePanelMenu(int featureId) {
        PanelFeatureState st = getPanelState(featureId, false);
        if (st != null) {
            if (st.menu != null) {
                Bundle savedActionViewStates = new Bundle();
                st.menu.saveActionViewStates(savedActionViewStates);
                if (savedActionViewStates.size() > 0) {
                    st.frozenActionViewState = savedActionViewStates;
                }
                st.menu.stopDispatchingItemsChanged();
                st.menu.clear();
            }
            st.refreshMenuContent = true;
            st.refreshDecorView = true;
            if ((featureId == 8 || featureId == 0) && this.mDecorContentParent != null) {
                st = getPanelState(0, false);
                if (st != null) {
                    st.isPrepared = false;
                    preparePanel(st, null);
                }
            }
        }
    }

    public final boolean onKeyDownPanel(int featureId, KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0) {
            this.mPanelChordingKey = keyCode;
            PanelFeatureState st = getPanelState(featureId, false);
            if (!(st == null || st.isOpen)) {
                return preparePanel(st, event);
            }
        }
        return false;
    }

    /* JADX WARNING: Removed duplicated region for block: B:38:0x0087  */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0087  */
    public final void onKeyUpPanel(int r6, android.view.KeyEvent r7) {
        /*
        r5 = this;
        r0 = r5.mPanelChordingKey;
        if (r0 == 0) goto L_0x00a2;
    L_0x0004:
        r0 = 0;
        r5.mPanelChordingKey = r0;
        r1 = r5.getPanelState(r6, r0);
        r2 = r7.isCanceled();
        if (r2 != 0) goto L_0x00a1;
    L_0x0011:
        r2 = r5.mDecor;
        if (r2 == 0) goto L_0x0019;
    L_0x0015:
        r2 = r2.mPrimaryActionMode;
        if (r2 != 0) goto L_0x00a1;
    L_0x0019:
        if (r1 != 0) goto L_0x001d;
    L_0x001b:
        goto L_0x00a1;
    L_0x001d:
        r2 = 0;
        if (r6 != 0) goto L_0x005a;
    L_0x0020:
        r3 = r5.mDecorContentParent;
        if (r3 == 0) goto L_0x005a;
    L_0x0024:
        r3 = r3.canShowOverflowMenu();
        if (r3 == 0) goto L_0x005a;
    L_0x002a:
        r3 = r5.getContext();
        r3 = android.view.ViewConfiguration.get(r3);
        r3 = r3.hasPermanentMenuKey();
        if (r3 != 0) goto L_0x005a;
    L_0x0038:
        r3 = r5.mDecorContentParent;
        r3 = r3.isOverflowMenuShowing();
        if (r3 != 0) goto L_0x0053;
    L_0x0040:
        r3 = r5.isDestroyed();
        if (r3 != 0) goto L_0x0085;
    L_0x0046:
        r3 = r5.preparePanel(r1, r7);
        if (r3 == 0) goto L_0x0085;
    L_0x004c:
        r3 = r5.mDecorContentParent;
        r2 = r3.showOverflowMenu();
        goto L_0x0085;
    L_0x0053:
        r3 = r5.mDecorContentParent;
        r2 = r3.hideOverflowMenu();
        goto L_0x0085;
    L_0x005a:
        r3 = r1.isOpen;
        if (r3 != 0) goto L_0x007f;
    L_0x005e:
        r3 = r1.isHandled;
        if (r3 == 0) goto L_0x0063;
    L_0x0062:
        goto L_0x007f;
    L_0x0063:
        r3 = r1.isPrepared;
        if (r3 == 0) goto L_0x0085;
    L_0x0067:
        r3 = 1;
        r4 = r1.refreshMenuContent;
        if (r4 == 0) goto L_0x0072;
    L_0x006c:
        r1.isPrepared = r0;
        r3 = r5.preparePanel(r1, r7);
    L_0x0072:
        if (r3 == 0) goto L_0x0085;
    L_0x0074:
        r4 = 50001; // 0xc351 float:7.0066E-41 double:2.4704E-319;
        android.util.EventLog.writeEvent(r4, r0);
        r5.openPanel(r1, r7);
        r2 = 1;
        goto L_0x0085;
    L_0x007f:
        r2 = r1.isOpen;
        r3 = 1;
        r5.closePanel(r1, r3);
    L_0x0085:
        if (r2 == 0) goto L_0x00a2;
    L_0x0087:
        r3 = r5.getContext();
        r4 = "audio";
        r3 = r3.getSystemService(r4);
        r3 = (android.media.AudioManager) r3;
        if (r3 == 0) goto L_0x0099;
    L_0x0095:
        r3.playSoundEffect(r0);
        goto L_0x00a2;
    L_0x0099:
        r0 = "PhoneWindow";
        r4 = "Couldn't get audio manager";
        android.util.Log.w(r0, r4);
        goto L_0x00a2;
    L_0x00a1:
        return;
    L_0x00a2:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.policy.PhoneWindow.onKeyUpPanel(int, android.view.KeyEvent):void");
    }

    public final void closeAllPanels() {
        if (getWindowManager() != null) {
            PanelFeatureState[] panels = this.mPanels;
            int N = panels != null ? panels.length : 0;
            for (int i = 0; i < N; i++) {
                PanelFeatureState panel = panels[i];
                if (panel != null) {
                    closePanel(panel, true);
                }
            }
            closeContextMenu();
        }
    }

    private synchronized void closeContextMenu() {
        if (this.mContextMenu != null) {
            this.mContextMenu.close();
            dismissContextMenu();
        }
    }

    private synchronized void dismissContextMenu() {
        this.mContextMenu = null;
        if (this.mContextMenuHelper != null) {
            this.mContextMenuHelper.dismiss();
            this.mContextMenuHelper = null;
        }
    }

    public boolean performPanelShortcut(int featureId, int keyCode, KeyEvent event, int flags) {
        return performPanelShortcut(getPanelState(featureId, false), keyCode, event, flags);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean performPanelShortcut(PanelFeatureState st, int keyCode, KeyEvent event, int flags) {
        if (event.isSystem() || st == null) {
            return false;
        }
        boolean handled = false;
        if ((st.isPrepared || preparePanel(st, event)) && st.menu != null) {
            handled = st.menu.performShortcut(keyCode, event, flags);
        }
        if (handled) {
            st.isHandled = true;
            if ((flags & 1) == 0 && this.mDecorContentParent == null) {
                closePanel(st, true);
            }
        }
        return handled;
    }

    public boolean performPanelIdentifierAction(int featureId, int id, int flags) {
        PanelFeatureState st = getPanelState(featureId, true);
        if (!preparePanel(st, new KeyEvent(0, 82)) || st.menu == null) {
            return false;
        }
        boolean res = st.menu.performIdentifierAction(id, flags);
        if (this.mDecorContentParent == null) {
            closePanel(st, true);
        }
        return res;
    }

    public PanelFeatureState findMenuPanel(Menu menu) {
        PanelFeatureState[] panels = this.mPanels;
        int N = panels != null ? panels.length : 0;
        for (int i = 0; i < N; i++) {
            PanelFeatureState panel = panels[i];
            if (panel != null && panel.menu == menu) {
                return panel;
            }
        }
        return null;
    }

    public boolean onMenuItemSelected(MenuBuilder menu, MenuItem item) {
        Window.Callback cb = getCallback();
        if (!(cb == null || isDestroyed())) {
            PanelFeatureState panel = findMenuPanel(menu.getRootMenu());
            if (panel != null) {
                return cb.onMenuItemSelected(panel.featureId, item);
            }
        }
        return false;
    }

    public void onMenuModeChange(MenuBuilder menu) {
        reopenMenu(true);
    }

    private void reopenMenu(boolean toggleMenuMode) {
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent == null || !decorContentParent.canShowOverflowMenu() || (ViewConfiguration.get(getContext()).hasPermanentMenuKey() && !this.mDecorContentParent.isOverflowMenuShowPending())) {
            PanelFeatureState st = getPanelState(0, false);
            if (st != null) {
                boolean newExpandedMode = st.isInExpandedMode;
                if (toggleMenuMode) {
                    newExpandedMode = !newExpandedMode;
                }
                st.refreshDecorView = true;
                closePanel(st, false);
                st.isInExpandedMode = newExpandedMode;
                openPanel(st, null);
                return;
            }
            return;
        }
        Window.Callback cb = getCallback();
        PanelFeatureState st2;
        if (this.mDecorContentParent.isOverflowMenuShowing() && toggleMenuMode) {
            this.mDecorContentParent.hideOverflowMenu();
            st2 = getPanelState(0, false);
            if (!(st2 == null || cb == null || isDestroyed())) {
                cb.onPanelClosed(8, st2.menu);
            }
        } else if (!(cb == null || isDestroyed())) {
            if (this.mInvalidatePanelMenuPosted && (1 & this.mInvalidatePanelMenuFeatures) != 0) {
                this.mDecor.removeCallbacks(this.mInvalidatePanelMenuRunnable);
                this.mInvalidatePanelMenuRunnable.run();
            }
            st2 = getPanelState(0, false);
            if (!(st2 == null || st2.menu == null || st2.refreshMenuContent || !cb.onPreparePanel(0, st2.createdPanelView, st2.menu))) {
                cb.onMenuOpened(8, st2.menu);
                this.mDecorContentParent.showOverflowMenu();
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean initializePanelMenu(PanelFeatureState st) {
        Context context = getContext();
        if ((st.featureId == 0 || st.featureId == 8) && this.mDecorContentParent != null) {
            TypedValue outValue = new TypedValue();
            Theme baseTheme = context.getTheme();
            baseTheme.resolveAttribute(16843825, outValue, true);
            Theme widgetTheme = null;
            if (outValue.resourceId != 0) {
                widgetTheme = context.getResources().newTheme();
                widgetTheme.setTo(baseTheme);
                widgetTheme.applyStyle(outValue.resourceId, true);
                widgetTheme.resolveAttribute(16843671, outValue, true);
            } else {
                baseTheme.resolveAttribute(16843671, outValue, true);
            }
            if (outValue.resourceId != 0) {
                if (widgetTheme == null) {
                    widgetTheme = context.getResources().newTheme();
                    widgetTheme.setTo(baseTheme);
                }
                widgetTheme.applyStyle(outValue.resourceId, true);
            }
            if (widgetTheme != null) {
                context = new ContextThemeWrapper(context, 0);
                context.getTheme().setTo(widgetTheme);
            }
        }
        MenuBuilder menu = new MenuBuilder(context);
        menu.setCallback(this);
        st.setMenu(menu);
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean initializePanelDecor(PanelFeatureState st) {
        st.decorView = generateDecor(st.featureId);
        st.gravity = 81;
        st.setStyle(getContext());
        TypedArray a = getContext().obtainStyledAttributes(null, R.styleable.Window, 0, st.listPresenterTheme);
        float elevation = a.getDimension(5.3E-44f, 0.0f);
        if (elevation != 0.0f) {
            st.decorView.setElevation(elevation);
        }
        a.recycle();
        return true;
    }

    private int getOptionsPanelGravity() {
        try {
            return WindowManagerHolder.sWindowManager.getPreferredOptionsPanelGravity(getContext().getDisplayId());
        } catch (RemoteException ex) {
            Log.e(TAG, "Couldn't getOptionsPanelGravity; using default", ex);
            return 81;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onOptionsPanelRotationChanged() {
        PanelFeatureState st = getPanelState(0, false);
        if (st != null) {
            WindowManager.LayoutParams lp = st.decorView != null ? (WindowManager.LayoutParams) st.decorView.getLayoutParams() : null;
            if (lp != null) {
                lp.gravity = getOptionsPanelGravity();
                ViewManager wm = getWindowManager();
                if (wm != null) {
                    wm.updateViewLayout(st.decorView, lp);
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean initializePanelContent(PanelFeatureState st) {
        if (st.createdPanelView != null) {
            st.shownPanelView = st.createdPanelView;
            return true;
        } else if (st.menu == null) {
            return false;
        } else {
            MenuView menuView;
            if (this.mPanelMenuPresenterCallback == null) {
                this.mPanelMenuPresenterCallback = new PanelMenuPresenterCallback(this, null);
            }
            if (st.isInListMode()) {
                menuView = st.getListMenuView(getContext(), this.mPanelMenuPresenterCallback);
            } else {
                menuView = st.getIconMenuView(getContext(), this.mPanelMenuPresenterCallback);
            }
            st.shownPanelView = (View) menuView;
            if (st.shownPanelView == null) {
                return false;
            }
            int defaultAnimations = menuView.getWindowAnimations();
            if (defaultAnimations != 0) {
                st.windowAnimations = defaultAnimations;
            }
            return true;
        }
    }

    public boolean performContextMenuIdentifierAction(int id, int flags) {
        ContextMenuBuilder contextMenuBuilder = this.mContextMenu;
        return contextMenuBuilder != null ? contextMenuBuilder.performIdentifierAction(id, flags) : false;
    }

    public final void setElevation(float elevation) {
        this.mElevation = elevation;
        WindowManager.LayoutParams attrs = getAttributes();
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.setElevation(elevation);
            attrs.setSurfaceInsets(this.mDecor, true, false);
        }
        dispatchWindowAttributesChanged(attrs);
    }

    public float getElevation() {
        return this.mElevation;
    }

    public final void setClipToOutline(boolean clipToOutline) {
        this.mClipToOutline = clipToOutline;
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.setClipToOutline(clipToOutline);
        }
    }

    public final void setBackgroundDrawable(Drawable drawable) {
        if (drawable != this.mBackgroundDrawable) {
            this.mBackgroundDrawable = drawable;
            DecorView decorView = this.mDecor;
            if (decorView != null) {
                decorView.setWindowBackground(drawable);
                Drawable drawable2 = this.mBackgroundFallbackDrawable;
                if (drawable2 != null) {
                    DecorView decorView2 = this.mDecor;
                    if (drawable != null) {
                        drawable2 = null;
                    }
                    decorView2.setBackgroundFallback(drawable2);
                }
            }
        }
    }

    public final void setFeatureDrawableResource(int featureId, int resId) {
        if (resId != 0) {
            DrawableFeatureState st = getDrawableState(featureId, true);
            if (st.resid != resId) {
                st.resid = resId;
                st.uri = null;
                st.local = getContext().getDrawable(resId);
                updateDrawable(featureId, st, false);
                return;
            }
            return;
        }
        setFeatureDrawable(featureId, null);
    }

    public final void setFeatureDrawableUri(int featureId, Uri uri) {
        if (uri != null) {
            DrawableFeatureState st = getDrawableState(featureId, true);
            if (st.uri == null || !st.uri.equals(uri)) {
                st.resid = 0;
                st.uri = uri;
                st.local = loadImageURI(uri);
                updateDrawable(featureId, st, false);
                return;
            }
            return;
        }
        setFeatureDrawable(featureId, null);
    }

    public final void setFeatureDrawable(int featureId, Drawable drawable) {
        DrawableFeatureState st = getDrawableState(featureId, true);
        st.resid = 0;
        st.uri = null;
        if (st.local != drawable) {
            st.local = drawable;
            updateDrawable(featureId, st, false);
        }
    }

    public void setFeatureDrawableAlpha(int featureId, int alpha) {
        DrawableFeatureState st = getDrawableState(featureId, true);
        if (st.alpha != alpha) {
            st.alpha = alpha;
            updateDrawable(featureId, st, false);
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final void setFeatureDefaultDrawable(int featureId, Drawable drawable) {
        DrawableFeatureState st = getDrawableState(featureId, true);
        if (st.def != drawable) {
            st.def = drawable;
            updateDrawable(featureId, st, false);
        }
    }

    public final void setFeatureInt(int featureId, int value) {
        updateInt(featureId, value, false);
    }

    /* Access modifiers changed, original: protected|final */
    public final void updateDrawable(int featureId, boolean fromActive) {
        DrawableFeatureState st = getDrawableState(featureId, null);
        if (st != null) {
            updateDrawable(featureId, st, fromActive);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDrawableChanged(int featureId, Drawable drawable, int alpha) {
        ImageView view;
        if (featureId == 3) {
            view = getLeftIconView();
        } else if (featureId == 4) {
            view = getRightIconView();
        } else {
            return;
        }
        if (drawable != null) {
            drawable.setAlpha(alpha);
            view.setImageDrawable(drawable);
            view.setVisibility(0);
        } else {
            view.setVisibility(8);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onIntChanged(int featureId, int value) {
        if (featureId == 2 || featureId == 5) {
            updateProgressBars(value);
        } else if (featureId == 7) {
            ViewGroup titleContainer = (FrameLayout) findViewById(R.id.title_container);
            if (titleContainer != null) {
                this.mLayoutInflater.inflate(value, titleContainer);
            }
        }
    }

    private void updateProgressBars(int value) {
        ProgressBar circularProgressBar = getCircularProgressBar(true);
        ProgressBar horizontalProgressBar = getHorizontalProgressBar(true);
        int features = getLocalFeatures();
        String str = "Circular progress bar not located in current window decor";
        String str2 = "Horizontal progress bar not located in current window decor";
        String str3 = TAG;
        if (value == -1) {
            if ((features & 4) != 0) {
                if (horizontalProgressBar != null) {
                    int visibility = (horizontalProgressBar.isIndeterminate() || horizontalProgressBar.getProgress() < 10000) ? 0 : 4;
                    horizontalProgressBar.setVisibility(visibility);
                } else {
                    Log.e(str3, str2);
                }
            }
            if ((features & 32) == 0) {
                return;
            }
            if (circularProgressBar != null) {
                circularProgressBar.setVisibility(0);
            } else {
                Log.e(str3, str);
            }
        } else if (value == -2) {
            if ((features & 4) != 0) {
                if (horizontalProgressBar != null) {
                    horizontalProgressBar.setVisibility(8);
                } else {
                    Log.e(str3, str2);
                }
            }
            if ((features & 32) == 0) {
                return;
            }
            if (circularProgressBar != null) {
                circularProgressBar.setVisibility(8);
            } else {
                Log.e(str3, str);
            }
        } else if (value == -3) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setIndeterminate(true);
            } else {
                Log.e(str3, str2);
            }
        } else if (value == -4) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setIndeterminate(false);
            } else {
                Log.e(str3, str2);
            }
        } else if (value >= 0 && value <= 10000) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setProgress(value + 0);
            } else {
                Log.e(str3, str2);
            }
            if (value < 10000) {
                showProgressBars(horizontalProgressBar, circularProgressBar);
            } else {
                hideProgressBars(horizontalProgressBar, circularProgressBar);
            }
        } else if (20000 <= value && value <= 30000) {
            if (horizontalProgressBar != null) {
                horizontalProgressBar.setSecondaryProgress(value - 20000);
            } else {
                Log.e(str3, str2);
            }
            showProgressBars(horizontalProgressBar, circularProgressBar);
        }
    }

    private void showProgressBars(ProgressBar horizontalProgressBar, ProgressBar spinnyProgressBar) {
        int features = getLocalFeatures();
        if (!((features & 32) == 0 || spinnyProgressBar == null || spinnyProgressBar.getVisibility() != 4)) {
            spinnyProgressBar.setVisibility(0);
        }
        if ((features & 4) != 0 && horizontalProgressBar != null && horizontalProgressBar.getProgress() < 10000) {
            horizontalProgressBar.setVisibility(0);
        }
    }

    private void hideProgressBars(ProgressBar horizontalProgressBar, ProgressBar spinnyProgressBar) {
        int features = getLocalFeatures();
        Animation anim = AnimationUtils.loadAnimation(getContext(), 17432577);
        anim.setDuration(1000);
        if (!((features & 32) == 0 || spinnyProgressBar == null || spinnyProgressBar.getVisibility() != 0)) {
            spinnyProgressBar.startAnimation(anim);
            spinnyProgressBar.setVisibility(4);
        }
        if ((features & 4) != 0 && horizontalProgressBar != null && horizontalProgressBar.getVisibility() == 0) {
            horizontalProgressBar.startAnimation(anim);
            horizontalProgressBar.setVisibility(4);
        }
    }

    public void setIcon(int resId) {
        this.mIconRes = resId;
        this.mResourcesSetFlags |= 1;
        this.mResourcesSetFlags &= -5;
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
            decorContentParent.setIcon(resId);
        }
    }

    public void setDefaultIcon(int resId) {
        if ((this.mResourcesSetFlags & 1) == 0) {
            this.mIconRes = resId;
            DecorContentParent decorContentParent = this.mDecorContentParent;
            if (!(decorContentParent == null || (decorContentParent.hasIcon() && (this.mResourcesSetFlags & 4) == 0))) {
                if (resId != 0) {
                    this.mDecorContentParent.setIcon(resId);
                    this.mResourcesSetFlags &= -5;
                } else {
                    this.mDecorContentParent.setIcon(getContext().getPackageManager().getDefaultActivityIcon());
                    this.mResourcesSetFlags |= 4;
                }
            }
        }
    }

    public void setLogo(int resId) {
        this.mLogoRes = resId;
        this.mResourcesSetFlags |= 2;
        DecorContentParent decorContentParent = this.mDecorContentParent;
        if (decorContentParent != null) {
            decorContentParent.setLogo(resId);
        }
    }

    public void setDefaultLogo(int resId) {
        if ((this.mResourcesSetFlags & 2) == 0) {
            this.mLogoRes = resId;
            DecorContentParent decorContentParent = this.mDecorContentParent;
            if (!(decorContentParent == null || decorContentParent.hasLogo())) {
                this.mDecorContentParent.setLogo(resId);
            }
        }
    }

    public void setLocalFocus(boolean hasFocus, boolean inTouchMode) {
        getViewRootImpl().windowFocusChanged(hasFocus, inTouchMode);
    }

    public void injectInputEvent(InputEvent event) {
        getViewRootImpl().dispatchInputEvent(event);
    }

    private ViewRootImpl getViewRootImpl() {
        ViewRootImpl viewRootImpl = this.mDecor;
        if (viewRootImpl != null) {
            viewRootImpl = viewRootImpl.getViewRootImpl();
            if (viewRootImpl != null) {
                return viewRootImpl;
            }
        }
        throw new IllegalStateException("view not added");
    }

    public void takeKeyEvents(boolean get) {
        this.mDecor.setFocusable(get);
    }

    public boolean superDispatchKeyEvent(KeyEvent event) {
        return this.mDecor.superDispatchKeyEvent(event);
    }

    public boolean superDispatchKeyShortcutEvent(KeyEvent event) {
        return this.mDecor.superDispatchKeyShortcutEvent(event);
    }

    public boolean superDispatchTouchEvent(MotionEvent event) {
        return this.mDecor.superDispatchTouchEvent(event);
    }

    public boolean superDispatchTrackballEvent(MotionEvent event) {
        return this.mDecor.superDispatchTrackballEvent(event);
    }

    public boolean superDispatchGenericMotionEvent(MotionEvent event) {
        return this.mDecor.superDispatchGenericMotionEvent(event);
    }

    /* Access modifiers changed, original: protected */
    public boolean onKeyDown(int featureId, int keyCode, KeyEvent event) {
        DecorView decorView = this.mDecor;
        DispatcherState dispatcher = decorView != null ? decorView.getKeyDispatcherState() : null;
        int i = 0;
        if (keyCode != 4) {
            if (keyCode != 79) {
                if (keyCode != 82) {
                    if (keyCode != 130) {
                        if (keyCode != 164 && keyCode != 24 && keyCode != 25) {
                            if (!(keyCode == 126 || keyCode == 127)) {
                                switch (keyCode) {
                                    case 85:
                                    case 86:
                                    case 87:
                                    case 88:
                                    case 89:
                                    case 90:
                                    case 91:
                                        break;
                                }
                            }
                        }
                        if (this.mMediaController != null) {
                            getMediaSessionManager().dispatchVolumeKeyEventAsSystemService(this.mMediaController.getSessionToken(), event);
                        } else {
                            getMediaSessionManager().dispatchVolumeKeyEventAsSystemService(event, this.mVolumeControlStreamType);
                        }
                        return true;
                    }
                }
                if (featureId >= 0) {
                    i = featureId;
                }
                onKeyDownPanel(i, event);
                return true;
            }
            return this.mMediaController != null && getMediaSessionManager().dispatchMediaKeyEventAsSystemService(this.mMediaController.getSessionToken(), event);
        } else if (event.getRepeatCount() <= 0 && featureId >= 0) {
            if (dispatcher != null) {
                dispatcher.startTracking(event, this);
            }
            return true;
        }
        return false;
    }

    private KeyguardManager getKeyguardManager() {
        if (this.mKeyguardManager == null) {
            this.mKeyguardManager = (KeyguardManager) getContext().getSystemService(Context.KEYGUARD_SERVICE);
        }
        return this.mKeyguardManager;
    }

    /* Access modifiers changed, original: 0000 */
    public AudioManager getAudioManager() {
        if (this.mAudioManager == null) {
            this.mAudioManager = (AudioManager) getContext().getSystemService("audio");
        }
        return this.mAudioManager;
    }

    private MediaSessionManager getMediaSessionManager() {
        if (this.mMediaSessionManager == null) {
            this.mMediaSessionManager = (MediaSessionManager) getContext().getSystemService(Context.MEDIA_SESSION_SERVICE);
        }
        return this.mMediaSessionManager;
    }

    /* Access modifiers changed, original: protected */
    public boolean onKeyUp(int featureId, int keyCode, KeyEvent event) {
        DecorView decorView = this.mDecor;
        DispatcherState dispatcher = decorView != null ? decorView.getKeyDispatcherState() : null;
        if (dispatcher != null) {
            dispatcher.handleUpEvent(event);
        }
        int i = 0;
        if (keyCode != 4) {
            if (keyCode != 79) {
                if (keyCode == 82) {
                    if (featureId >= 0) {
                        i = featureId;
                    }
                    onKeyUpPanel(i, event);
                    return true;
                } else if (keyCode != 130) {
                    if (keyCode == 164) {
                        getMediaSessionManager().dispatchVolumeKeyEventAsSystemService(event, Integer.MIN_VALUE);
                        return true;
                    } else if (keyCode != 171) {
                        if (keyCode != 24 && keyCode != 25) {
                            if (!(keyCode == 126 || keyCode == 127)) {
                                switch (keyCode) {
                                    case 84:
                                        if (!(isNotInstantAppAndKeyguardRestricted() || (getContext().getResources().getConfiguration().uiMode & 15) == 6)) {
                                            if (event.isTracking() && !event.isCanceled()) {
                                                launchDefaultSearch(event);
                                            }
                                            return true;
                                        }
                                    case 85:
                                    case 86:
                                    case 87:
                                    case 88:
                                    case 89:
                                    case 90:
                                    case 91:
                                        break;
                                }
                            }
                        }
                        if (this.mMediaController != null) {
                            getMediaSessionManager().dispatchVolumeKeyEventAsSystemService(this.mMediaController.getSessionToken(), event);
                        } else {
                            getMediaSessionManager().dispatchVolumeKeyEventAsSystemService(event, this.mVolumeControlStreamType);
                        }
                        return true;
                    } else {
                        if (this.mSupportsPictureInPicture && !event.isCanceled()) {
                            getWindowControllerCallback().enterPictureInPictureModeIfPossible();
                        }
                        return true;
                    }
                }
            }
            return this.mMediaController != null && getMediaSessionManager().dispatchMediaKeyEventAsSystemService(this.mMediaController.getSessionToken(), event);
        } else if (featureId >= 0 && event.isTracking() && !event.isCanceled()) {
            if (featureId == 0) {
                PanelFeatureState st = getPanelState(featureId, false);
                if (st != null && st.isInExpandedMode) {
                    reopenMenu(true);
                    return true;
                }
            }
            closePanel(featureId);
            return true;
        }
        return false;
    }

    private boolean isNotInstantAppAndKeyguardRestricted() {
        return !getContext().getPackageManager().isInstantApp() && getKeyguardManager().inKeyguardRestrictedInputMode();
    }

    /* Access modifiers changed, original: protected */
    public void onActive() {
    }

    public final View getDecorView() {
        if (this.mDecor == null || this.mForceDecorInstall) {
            installDecor();
        }
        return this.mDecor;
    }

    public final View peekDecorView() {
        return this.mDecor;
    }

    /* Access modifiers changed, original: 0000 */
    public void onViewRootImplSet(ViewRootImpl viewRoot) {
        viewRoot.setActivityConfigCallback(this.mActivityConfigCallback);
    }

    public Bundle saveHierarchyState() {
        Bundle outState = new Bundle();
        if (this.mContentParent == null) {
            return outState;
        }
        SparseArray<Parcelable> states = new SparseArray();
        this.mContentParent.saveHierarchyState(states);
        outState.putSparseParcelableArray(VIEWS_TAG, states);
        View focusedView = this.mContentParent.findFocus();
        if (!(focusedView == null || focusedView.getId() == -1)) {
            outState.putInt(FOCUSED_ID_TAG, focusedView.getId());
        }
        SparseArray<Parcelable> panelStates = new SparseArray();
        savePanelState(panelStates);
        if (panelStates.size() > 0) {
            outState.putSparseParcelableArray(PANELS_TAG, panelStates);
        }
        if (this.mDecorContentParent != null) {
            SparseArray<Parcelable> actionBarStates = new SparseArray();
            this.mDecorContentParent.saveToolbarHierarchyState(actionBarStates);
            outState.putSparseParcelableArray(ACTION_BAR_TAG, actionBarStates);
        }
        return outState;
    }

    public void restoreHierarchyState(Bundle savedInstanceState) {
        if (this.mContentParent != null) {
            SparseArray<Parcelable> savedStates = savedInstanceState.getSparseParcelableArray(VIEWS_TAG);
            if (savedStates != null) {
                this.mContentParent.restoreHierarchyState(savedStates);
            }
            int focusedViewId = savedInstanceState.getInt(FOCUSED_ID_TAG, -1);
            String str = TAG;
            if (focusedViewId != -1) {
                View needsFocus = this.mContentParent.findViewById(focusedViewId);
                if (needsFocus != null) {
                    needsFocus.requestFocus();
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Previously focused view reported id ");
                    stringBuilder.append(focusedViewId);
                    stringBuilder.append(" during save, but can't be found during restore.");
                    Log.w(str, stringBuilder.toString());
                }
            }
            SparseArray<Parcelable> panelStates = savedInstanceState.getSparseParcelableArray(PANELS_TAG);
            if (panelStates != null) {
                restorePanelState(panelStates);
            }
            if (this.mDecorContentParent != null) {
                SparseArray<Parcelable> actionBarStates = savedInstanceState.getSparseParcelableArray(ACTION_BAR_TAG);
                if (actionBarStates != null) {
                    doPendingInvalidatePanelMenu();
                    this.mDecorContentParent.restoreToolbarHierarchyState(actionBarStates);
                } else {
                    Log.w(str, "Missing saved instance states for action bar views! State will not be restored.");
                }
            }
        }
    }

    private void savePanelState(SparseArray<Parcelable> icicles) {
        PanelFeatureState[] panels = this.mPanels;
        if (panels != null) {
            for (int curFeatureId = panels.length - 1; curFeatureId >= 0; curFeatureId--) {
                if (panels[curFeatureId] != null) {
                    icicles.put(curFeatureId, panels[curFeatureId].onSaveInstanceState());
                }
            }
        }
    }

    private void restorePanelState(SparseArray<Parcelable> icicles) {
        for (int i = icicles.size() - 1; i >= 0; i--) {
            int curFeatureId = icicles.keyAt(i);
            PanelFeatureState st = getPanelState(curFeatureId, null);
            if (st != null) {
                st.onRestoreInstanceState((Parcelable) icicles.get(curFeatureId));
                invalidatePanelMenu(curFeatureId);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void openPanelsAfterRestore() {
        PanelFeatureState[] panels = this.mPanels;
        if (panels != null) {
            for (int i = panels.length - 1; i >= 0; i--) {
                PanelFeatureState st = panels[i];
                if (st != null) {
                    st.applyFrozenState();
                    if (!st.isOpen && st.wasLastOpen) {
                        st.isInExpandedMode = st.wasLastExpanded;
                        openPanel(st, null);
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public DecorView generateDecor(int featureId) {
        Context context;
        if (this.mUseDecorContext) {
            Context applicationContext = getContext().getApplicationContext();
            if (applicationContext == null) {
                context = getContext();
            } else {
                context = new DecorContext(applicationContext, getContext());
                int i = this.mTheme;
                if (i != -1) {
                    context.setTheme(i);
                }
            }
        } else {
            context = getContext();
        }
        return new DecorView(context, featureId, this, getAttributes());
    }

    /* Access modifiers changed, original: protected */
    public ViewGroup generateLayout(DecorView decor) {
        int i;
        DecorView decorView = decor;
        TypedArray a = getWindowStyle();
        this.mIsFloating = a.getBoolean(4, false);
        int flagsToUpdate = (~getForcedWindowFlags()) & 65792;
        if (this.mIsFloating) {
            setLayout(-2, -2);
            setFlags(0, flagsToUpdate);
        } else {
            setFlags(65792, flagsToUpdate);
        }
        if (a.getBoolean(3, false)) {
            requestFeature(1);
        } else if (a.getBoolean(15, false)) {
            requestFeature(8);
        }
        if (a.getBoolean(17, false)) {
            requestFeature(9);
        }
        if (a.getBoolean(16, false)) {
            requestFeature(10);
        }
        if (a.getBoolean(25, false)) {
            requestFeature(11);
        }
        if (a.getBoolean(9, false)) {
            setFlags(1024, (~getForcedWindowFlags()) & 1024);
        }
        if (a.getBoolean(23, false)) {
            setFlags(67108864, (~getForcedWindowFlags()) & 67108864);
        }
        if (a.getBoolean(24, false)) {
            setFlags(134217728, (~getForcedWindowFlags()) & 134217728);
        }
        if (a.getBoolean(22, false)) {
            setFlags(33554432, 33554432 & (~getForcedWindowFlags()));
        }
        if (a.getBoolean(14, false)) {
            setFlags(1048576, 1048576 & (~getForcedWindowFlags()));
        }
        if (a.getBoolean(18, getContext().getApplicationInfo().targetSdkVersion >= 11)) {
            setFlags(8388608, 8388608 & (~getForcedWindowFlags()));
        }
        a.getValue(19, this.mMinWidthMajor);
        a.getValue(20, this.mMinWidthMinor);
        if (a.hasValue(57)) {
            if (this.mFixedWidthMajor == null) {
                this.mFixedWidthMajor = new TypedValue();
            }
            a.getValue(57, this.mFixedWidthMajor);
        }
        if (a.hasValue(58)) {
            if (this.mFixedWidthMinor == null) {
                this.mFixedWidthMinor = new TypedValue();
            }
            a.getValue(58, this.mFixedWidthMinor);
        }
        if (a.hasValue(55)) {
            if (this.mFixedHeightMajor == null) {
                this.mFixedHeightMajor = new TypedValue();
            }
            a.getValue(55, this.mFixedHeightMajor);
        }
        if (a.hasValue(56)) {
            if (this.mFixedHeightMinor == null) {
                this.mFixedHeightMinor = new TypedValue();
            }
            a.getValue(56, this.mFixedHeightMinor);
        }
        if (a.getBoolean(26, false)) {
            requestFeature(12);
        }
        if (a.getBoolean(45, false)) {
            requestFeature(13);
        }
        this.mIsTranslucent = a.getBoolean(5, false);
        Context context = getContext();
        int targetSdk = context.getApplicationInfo().targetSdkVersion;
        boolean targetPreHoneycomb = targetSdk < 11;
        boolean targetPreIcs = targetSdk < 14;
        boolean targetPreL = targetSdk < 21;
        boolean targetPreQ = targetSdk < 29;
        boolean targetHcNeedsOptions = context.getResources().getBoolean(true);
        boolean noActionBar = !hasFeature(8) || hasFeature(1);
        if (targetPreHoneycomb || (targetPreIcs && targetHcNeedsOptions && noActionBar)) {
            setNeedsMenuKey(1);
        } else {
            setNeedsMenuKey(2);
        }
        if (!this.mForcedStatusBarColor) {
            this.mStatusBarColor = a.getColor(35, -16777216);
        }
        if (!this.mForcedNavigationBarColor) {
            this.mNavigationBarColor = a.getColor(36, -16777216);
            this.mNavigationBarDividerColor = a.getColor(50, 0);
            PhoneWindowInjector.onNavigationBarColorChange(this);
        }
        if (!targetPreQ) {
            this.mEnsureStatusBarContrastWhenTransparent = a.getBoolean(52, false);
            this.mEnsureNavigationBarContrastWhenTransparent = a.getBoolean(53, true);
        }
        WindowManager.LayoutParams params = getAttributes();
        if (!this.mIsFloating) {
            if (!targetPreL && a.getBoolean(34, false)) {
                setFlags(Integer.MIN_VALUE, (~getForcedWindowFlags()) & Integer.MIN_VALUE);
            }
            if (this.mDecor.mForceWindowDrawsBarBackgrounds) {
                params.privateFlags |= 131072;
            }
        }
        if (a.getBoolean(46, false)) {
            decorView.setSystemUiVisibility(decor.getSystemUiVisibility() | 8192);
        }
        if (a.getBoolean(49, false)) {
            decorView.setSystemUiVisibility(decor.getSystemUiVisibility() | 16);
        }
        if (a.hasValue(51)) {
            int mode = a.getInt(51, -1);
            if (mode < 0 || mode > 2) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown windowLayoutInDisplayCutoutMode: ");
                stringBuilder.append(a.getString(51));
                throw new UnsupportedOperationException(stringBuilder.toString());
            }
            params.layoutInDisplayCutoutMode = mode;
        } else if (params.layoutInDisplayCutoutMode == 0) {
            params.layoutInDisplayCutoutMode = MiuiInit.getCutoutMode(getContext().getPackageName());
        }
        if ((this.mAlwaysReadCloseOnTouchAttr || getContext().getApplicationInfo().targetSdkVersion >= 11) && a.getBoolean(21, false)) {
            setCloseOnTouchOutsideIfNotSet(true);
        }
        if (!hasSoftInputMode()) {
            params.softInputMode = a.getInt(13, params.softInputMode);
        }
        if (a.getBoolean(11, this.mIsFloating)) {
            if ((getForcedWindowFlags() & 2) == 0) {
                params.flags |= 2;
            }
            if (haveDimAmount()) {
                i = 0;
            } else {
                i = 0;
                params.dimAmount = a.getFloat(0, 0.5f);
            }
        } else {
            i = 0;
        }
        if (params.windowAnimations == 0) {
            params.windowAnimations = a.getResourceId(8, i);
        }
        if (getContainer() == null) {
            if (this.mBackgroundDrawable == null) {
                if (this.mFrameResource == 0) {
                    this.mFrameResource = a.getResourceId(2, 0);
                }
                if (a.hasValue(1)) {
                    this.mBackgroundDrawable = a.getDrawable(1);
                }
            }
            if (a.hasValue(47)) {
                this.mBackgroundFallbackDrawable = a.getDrawable(47);
            }
            if (this.mLoadElevation) {
                this.mElevation = a.getDimension(38, 0.0f);
            }
            this.mClipToOutline = a.getBoolean(39, false);
            this.mTextColor = a.getColor(7, 0);
        }
        int features = getLocalFeatures();
        if ((features & 2048) != 0) {
            i = R.layout.screen_swipe_dismiss;
            setCloseOnSwipeEnabled(true);
            int i2 = flagsToUpdate;
        } else if ((features & 24) != 0) {
            if (this.mIsFloating) {
                i = new TypedValue();
                getContext().getTheme().resolveAttribute(R.attr.dialogTitleIconsDecorLayout, i, 1);
                i = i.resourceId;
            } else {
                i = R.layout.screen_title_icons;
            }
            removeFeature(8);
        } else {
            if ((features & 36) != 0 && (features & 256) == 0) {
                i = R.layout.screen_progress;
            } else if ((features & 128) != 0) {
                if (this.mIsFloating) {
                    i = new TypedValue();
                    getContext().getTheme().resolveAttribute(R.attr.dialogCustomTitleDecorLayout, i, true);
                    i = i.resourceId;
                } else {
                    i = R.layout.screen_custom_title;
                }
                removeFeature(8);
            } else if ((features & 2) == 0) {
                if (this.mIsFloating) {
                    i = new TypedValue();
                    getContext().getTheme().resolveAttribute(R.attr.dialogTitleDecorLayout, i, true);
                    i = i.resourceId;
                } else if ((features & 256) != 0) {
                    i = a.getResourceId(54, R.layout.screen_action_bar);
                } else {
                    i = R.layout.screen_title;
                }
            } else if ((features & 1024) != 0) {
                i = R.layout.screen_simple_overlay_action_mode;
            } else {
                i = R.layout.screen_simple;
            }
        }
        this.mDecor.startChanging();
        this.mDecor.onResourcesLoaded(this.mLayoutInflater, i);
        ViewGroup contentParent = (ViewGroup) findViewById(16908290);
        if (contentParent != null) {
            if ((features & 32) != 0) {
                ProgressBar progress = getCircularProgressBar(null);
                if (progress != null) {
                    progress.setIndeterminate(true);
                }
            }
            if ((features & 2048) != 0) {
                registerSwipeCallbacks(contentParent);
            }
            if (getContainer() == null) {
                Drawable frame;
                this.mDecor.setWindowBackground(this.mBackgroundDrawable);
                if (this.mFrameResource != 0) {
                    frame = getContext().getDrawable(this.mFrameResource);
                } else {
                    frame = null;
                }
                this.mDecor.setWindowFrame(frame);
                this.mDecor.setElevation(this.mElevation);
                this.mDecor.setClipToOutline(this.mClipToOutline);
                CharSequence charSequence = this.mTitle;
                if (charSequence != null) {
                    setTitle(charSequence);
                }
                if (this.mTitleColor == 0) {
                    this.mTitleColor = this.mTextColor;
                }
                setTitleColor(this.mTitleColor);
            }
            this.mDecor.finishChanging();
            return contentParent;
        }
        throw new RuntimeException("Window couldn't find content container view");
    }

    public void alwaysReadCloseOnTouchAttr() {
        this.mAlwaysReadCloseOnTouchAttr = true;
    }

    private void installDecor() {
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).installDecor(this);
        } else {
            originalInstallDecor();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void originalInstallDecor() {
        this.mForceDecorInstall = false;
        DecorView decorView = this.mDecor;
        if (decorView == null) {
            this.mDecor = generateDecor(-1);
            this.mDecor.setDescendantFocusability(262144);
            this.mDecor.setIsRootNamespace(true);
            if (!(this.mInvalidatePanelMenuPosted || this.mInvalidatePanelMenuFeatures == 0)) {
                this.mDecor.postOnAnimation(this.mInvalidatePanelMenuRunnable);
            }
        } else {
            decorView.setWindow(this);
        }
        if (this.mContentParent == null) {
            this.mContentParent = generateLayout(this.mDecor);
            this.mDecor.makeOptionalFitsSystemWindows();
            DecorContentParent decorContentParent = (DecorContentParent) this.mDecor.findViewById(R.id.decor_content_parent);
            if (decorContentParent != null) {
                this.mDecorContentParent = decorContentParent;
                this.mDecorContentParent.setWindowCallback(getCallback());
                if (this.mDecorContentParent.getTitle() == null) {
                    this.mDecorContentParent.setWindowTitle(this.mTitle);
                }
                int localFeatures = getLocalFeatures();
                for (int i = 0; i < 13; i++) {
                    if (((1 << i) & localFeatures) != 0) {
                        this.mDecorContentParent.initFeature(i);
                    }
                }
                this.mDecorContentParent.setUiOptions(this.mUiOptions);
                if ((this.mResourcesSetFlags & 1) != 0 || (this.mIconRes != 0 && !this.mDecorContentParent.hasIcon())) {
                    this.mDecorContentParent.setIcon(this.mIconRes);
                } else if ((this.mResourcesSetFlags & 1) == 0 && this.mIconRes == 0 && !this.mDecorContentParent.hasIcon()) {
                    this.mDecorContentParent.setIcon(getContext().getPackageManager().getDefaultActivityIcon());
                    this.mResourcesSetFlags |= 4;
                }
                if (!((this.mResourcesSetFlags & 2) == 0 && (this.mLogoRes == 0 || this.mDecorContentParent.hasLogo()))) {
                    this.mDecorContentParent.setLogo(this.mLogoRes);
                }
                PanelFeatureState st = getPanelState(0, false);
                if (!isDestroyed() && ((st == null || st.menu == null) && !this.mIsStartingWindow)) {
                    invalidatePanelMenu(8);
                }
            } else {
                this.mTitleView = (TextView) findViewById(16908310);
                if (this.mTitleView != null) {
                    if ((getLocalFeatures() & 2) != 0) {
                        View titleContainer = findViewById(R.id.title_container);
                        if (titleContainer != null) {
                            titleContainer.setVisibility(8);
                        } else {
                            this.mTitleView.setVisibility(8);
                        }
                        this.mContentParent.setForeground(null);
                    } else {
                        this.mTitleView.setText(this.mTitle);
                    }
                }
            }
            if (this.mDecor.getBackground() == null) {
                Drawable drawable = this.mBackgroundFallbackDrawable;
                if (drawable != null) {
                    this.mDecor.setBackgroundFallback(drawable);
                }
            }
            if (hasFeature(13)) {
                if (this.mTransitionManager == null) {
                    int transitionRes = getWindowStyle().getResourceId(27, 0);
                    if (transitionRes != 0) {
                        this.mTransitionManager = TransitionInflater.from(getContext()).inflateTransitionManager(transitionRes, this.mContentParent);
                    } else {
                        this.mTransitionManager = new TransitionManager();
                    }
                }
                this.mEnterTransition = getTransition(this.mEnterTransition, null, 28);
                this.mReturnTransition = getTransition(this.mReturnTransition, USE_DEFAULT_TRANSITION, 40);
                this.mExitTransition = getTransition(this.mExitTransition, null, 29);
                this.mReenterTransition = getTransition(this.mReenterTransition, USE_DEFAULT_TRANSITION, 41);
                this.mSharedElementEnterTransition = getTransition(this.mSharedElementEnterTransition, null, 30);
                this.mSharedElementReturnTransition = getTransition(this.mSharedElementReturnTransition, USE_DEFAULT_TRANSITION, 42);
                this.mSharedElementExitTransition = getTransition(this.mSharedElementExitTransition, null, 31);
                this.mSharedElementReenterTransition = getTransition(this.mSharedElementReenterTransition, USE_DEFAULT_TRANSITION, 43);
                if (this.mAllowEnterTransitionOverlap == null) {
                    this.mAllowEnterTransitionOverlap = Boolean.valueOf(getWindowStyle().getBoolean(33, true));
                }
                if (this.mAllowReturnTransitionOverlap == null) {
                    this.mAllowReturnTransitionOverlap = Boolean.valueOf(getWindowStyle().getBoolean(32, true));
                }
                if (this.mBackgroundFadeDurationMillis < 0) {
                    this.mBackgroundFadeDurationMillis = (long) getWindowStyle().getInteger(37, 300);
                }
                if (this.mSharedElementsUseOverlay == null) {
                    this.mSharedElementsUseOverlay = Boolean.valueOf(getWindowStyle().getBoolean(44, true));
                }
            }
        }
    }

    private Transition getTransition(Transition currentValue, Transition defaultValue, int id) {
        if (currentValue != defaultValue) {
            return currentValue;
        }
        int transitionId = getWindowStyle().getResourceId(id, -1);
        Transition transition = defaultValue;
        if (!(transitionId == -1 || transitionId == 17760256)) {
            transition = TransitionInflater.from(getContext()).inflateTransition(transitionId);
            if ((transition instanceof TransitionSet) && ((TransitionSet) transition).getTransitionCount() == 0) {
                transition = null;
            }
        }
        return transition;
    }

    private Drawable loadImageURI(Uri uri) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(getContext().getContentResolver().openInputStream(uri), null);
            return drawable;
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unable to open content: ");
            stringBuilder.append(uri);
            Log.w(TAG, stringBuilder.toString());
            return drawable;
        }
    }

    private DrawableFeatureState getDrawableState(int featureId, boolean required) {
        if ((getFeatures() & (1 << featureId)) != 0) {
            DrawableFeatureState[] drawableFeatureStateArr = this.mDrawables;
            DrawableFeatureState[] ar = drawableFeatureStateArr;
            if (drawableFeatureStateArr == null || ar.length <= featureId) {
                drawableFeatureStateArr = new DrawableFeatureState[(featureId + 1)];
                if (ar != null) {
                    System.arraycopy(ar, 0, drawableFeatureStateArr, 0, ar.length);
                }
                ar = drawableFeatureStateArr;
                this.mDrawables = drawableFeatureStateArr;
            }
            DrawableFeatureState st = ar[featureId];
            if (st == null) {
                DrawableFeatureState drawableFeatureState = new DrawableFeatureState(featureId);
                st = drawableFeatureState;
                ar[featureId] = drawableFeatureState;
            }
            return st;
        } else if (!required) {
            return null;
        } else {
            throw new RuntimeException("The feature has not been requested");
        }
    }

    /* Access modifiers changed, original: 0000 */
    public PanelFeatureState getPanelState(int featureId, boolean required) {
        return getPanelState(featureId, required, null);
    }

    private PanelFeatureState getPanelState(int featureId, boolean required, PanelFeatureState convertPanelState) {
        if ((getFeatures() & (1 << featureId)) != 0) {
            PanelFeatureState[] panelFeatureStateArr = this.mPanels;
            PanelFeatureState[] ar = panelFeatureStateArr;
            if (panelFeatureStateArr == null || ar.length <= featureId) {
                panelFeatureStateArr = new PanelFeatureState[(featureId + 1)];
                if (ar != null) {
                    System.arraycopy(ar, 0, panelFeatureStateArr, 0, ar.length);
                }
                ar = panelFeatureStateArr;
                this.mPanels = panelFeatureStateArr;
            }
            PanelFeatureState st = ar[featureId];
            if (st == null) {
                PanelFeatureState panelFeatureState;
                if (convertPanelState != null) {
                    panelFeatureState = convertPanelState;
                } else {
                    panelFeatureState = new PanelFeatureState(featureId);
                }
                st = panelFeatureState;
                ar[featureId] = panelFeatureState;
            }
            return st;
        } else if (!required) {
            return null;
        } else {
            throw new RuntimeException("The feature has not been requested");
        }
    }

    public final void setChildDrawable(int featureId, Drawable drawable) {
        DrawableFeatureState st = getDrawableState(featureId, true);
        st.child = drawable;
        updateDrawable(featureId, st, false);
    }

    public final void setChildInt(int featureId, int value) {
        updateInt(featureId, value, false);
    }

    public boolean isShortcutKey(int keyCode, KeyEvent event) {
        PanelFeatureState st = getPanelState(0, false);
        if (st == null || st.menu == null || !st.menu.isShortcutKey(keyCode, event)) {
            return false;
        }
        return true;
    }

    private void updateDrawable(int featureId, DrawableFeatureState st, boolean fromResume) {
        if (this.mContentParent != null) {
            int featureMask = 1 << featureId;
            if ((getFeatures() & featureMask) != 0 || fromResume) {
                Drawable drawable = null;
                if (st != null) {
                    drawable = st.child;
                    if (drawable == null) {
                        drawable = st.local;
                    }
                    if (drawable == null) {
                        drawable = st.def;
                    }
                }
                if ((getLocalFeatures() & featureMask) == 0) {
                    if (getContainer() != null && (isActive() || fromResume)) {
                        getContainer().setChildDrawable(featureId, drawable);
                    }
                } else if (!(st == null || (st.cur == drawable && st.curAlpha == st.alpha))) {
                    st.cur = drawable;
                    st.curAlpha = st.alpha;
                    onDrawableChanged(featureId, drawable, st.alpha);
                }
            }
        }
    }

    private void updateInt(int featureId, int value, boolean fromResume) {
        if (this.mContentParent != null) {
            int featureMask = 1 << featureId;
            if ((getFeatures() & featureMask) != 0 || fromResume) {
                if ((getLocalFeatures() & featureMask) != 0) {
                    onIntChanged(featureId, value);
                } else if (getContainer() != null) {
                    getContainer().setChildInt(featureId, value);
                }
            }
        }
    }

    private ImageView getLeftIconView() {
        ImageView imageView = this.mLeftIconView;
        if (imageView != null) {
            return imageView;
        }
        if (this.mContentParent == null) {
            installDecor();
        }
        imageView = (ImageView) findViewById(R.id.left_icon);
        this.mLeftIconView = imageView;
        return imageView;
    }

    /* Access modifiers changed, original: protected */
    public void dispatchWindowAttributesChanged(WindowManager.LayoutParams attrs) {
        super.dispatchWindowAttributesChanged(attrs);
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.updateColorViews(null, true);
        }
    }

    private ProgressBar getCircularProgressBar(boolean shouldInstallDecor) {
        ProgressBar progressBar = this.mCircularProgressBar;
        if (progressBar != null) {
            return progressBar;
        }
        if (this.mContentParent == null && shouldInstallDecor) {
            installDecor();
        }
        this.mCircularProgressBar = (ProgressBar) findViewById(R.id.progress_circular);
        progressBar = this.mCircularProgressBar;
        if (progressBar != null) {
            progressBar.setVisibility(4);
        }
        return this.mCircularProgressBar;
    }

    private ProgressBar getHorizontalProgressBar(boolean shouldInstallDecor) {
        ProgressBar progressBar = this.mHorizontalProgressBar;
        if (progressBar != null) {
            return progressBar;
        }
        if (this.mContentParent == null && shouldInstallDecor) {
            installDecor();
        }
        this.mHorizontalProgressBar = (ProgressBar) findViewById(R.id.progress_horizontal);
        progressBar = this.mHorizontalProgressBar;
        if (progressBar != null) {
            progressBar.setVisibility(4);
        }
        return this.mHorizontalProgressBar;
    }

    private ImageView getRightIconView() {
        ImageView imageView = this.mRightIconView;
        if (imageView != null) {
            return imageView;
        }
        if (this.mContentParent == null) {
            installDecor();
        }
        imageView = (ImageView) findViewById(R.id.right_icon);
        this.mRightIconView = imageView;
        return imageView;
    }

    private void registerSwipeCallbacks(ViewGroup contentParent) {
        if (contentParent instanceof SwipeDismissLayout) {
            SwipeDismissLayout swipeDismiss = (SwipeDismissLayout) contentParent;
            swipeDismiss.setOnDismissedListener(new OnDismissedListener() {
                public void onDismissed(SwipeDismissLayout layout) {
                    PhoneWindow.this.dispatchOnWindowSwipeDismissed();
                    PhoneWindow.this.dispatchOnWindowDismissed(false, true);
                }
            });
            swipeDismiss.setOnSwipeProgressChangedListener(new OnSwipeProgressChangedListener() {
                public void onSwipeProgressChanged(SwipeDismissLayout layout, float alpha, float translate) {
                    int flags;
                    WindowManager.LayoutParams newParams = PhoneWindow.this.getAttributes();
                    newParams.x = (int) translate;
                    newParams.alpha = alpha;
                    PhoneWindow.this.setAttributes(newParams);
                    if (newParams.x == 0) {
                        flags = 1024;
                    } else {
                        flags = 512;
                    }
                    PhoneWindow.this.setFlags(flags, 1536);
                }

                public void onSwipeCancelled(SwipeDismissLayout layout) {
                    WindowManager.LayoutParams newParams = PhoneWindow.this.getAttributes();
                    if (newParams.x != 0 || newParams.alpha != 1.0f) {
                        newParams.x = 0;
                        newParams.alpha = 1.0f;
                        PhoneWindow.this.setAttributes(newParams);
                        PhoneWindow.this.setFlags(1024, 1536);
                    }
                }
            });
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("contentParent is not a SwipeDismissLayout: ");
        stringBuilder.append(contentParent);
        Log.w(TAG, stringBuilder.toString());
    }

    public void setCloseOnSwipeEnabled(boolean closeOnSwipeEnabled) {
        if (hasFeature(11)) {
            ViewGroup viewGroup = this.mContentParent;
            if (viewGroup instanceof SwipeDismissLayout) {
                ((SwipeDismissLayout) viewGroup).setDismissable(closeOnSwipeEnabled);
            }
        }
        super.setCloseOnSwipeEnabled(closeOnSwipeEnabled);
    }

    private void callOnPanelClosed(int featureId, PanelFeatureState panel, Menu menu) {
        Window.Callback cb = getCallback();
        if (cb != null) {
            if (menu == null) {
                if (panel == null && featureId >= 0) {
                    PanelFeatureState[] panelFeatureStateArr = this.mPanels;
                    if (featureId < panelFeatureStateArr.length) {
                        panel = panelFeatureStateArr[featureId];
                    }
                }
                if (panel != null) {
                    menu = panel.menu;
                }
            }
            if ((panel == null || panel.isOpen) && !isDestroyed()) {
                cb.onPanelClosed(featureId, menu);
            }
        }
    }

    private boolean isTvUserSetupComplete() {
        int i = 0;
        boolean isTvSetupComplete = Secure.getInt(getContext().getContentResolver(), Secure.USER_SETUP_COMPLETE, 0) != 0;
        if (Secure.getInt(getContext().getContentResolver(), Secure.TV_USER_SETUP_COMPLETE, 0) != 0) {
            i = 1;
        }
        return isTvSetupComplete & i;
    }

    private boolean launchDefaultSearch(KeyEvent event) {
        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_LEANBACK) && !isTvUserSetupComplete()) {
            return false;
        }
        boolean result;
        Window.Callback cb = getCallback();
        String str = "search";
        if (cb == null || isDestroyed()) {
            result = false;
        } else {
            sendCloseSystemWindows(str);
            int deviceId = event.getDeviceId();
            SearchEvent searchEvent = null;
            if (deviceId != 0) {
                searchEvent = new SearchEvent(InputDevice.getDevice(deviceId));
            }
            try {
                result = cb.onSearchRequested(searchEvent);
            } catch (AbstractMethodError e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("WindowCallback ");
                stringBuilder.append(cb.getClass().getName());
                stringBuilder.append(" does not implement method onSearchRequested(SearchEvent); fa");
                Log.e(TAG, stringBuilder.toString(), e);
                result = cb.onSearchRequested();
            }
        }
        if (result || (getContext().getResources().getConfiguration().uiMode & 15) != 4) {
            return result;
        }
        Bundle args = new Bundle();
        args.putInt(Intent.EXTRA_ASSIST_INPUT_DEVICE_ID, event.getDeviceId());
        return ((SearchManager) getContext().getSystemService(str)).launchLegacyAssist(null, getContext().getUserId(), args);
    }

    public void setVolumeControlStream(int streamType) {
        this.mVolumeControlStreamType = streamType;
    }

    public int getVolumeControlStream() {
        return this.mVolumeControlStreamType;
    }

    public void setMediaController(MediaController controller) {
        this.mMediaController = controller;
    }

    public MediaController getMediaController() {
        return this.mMediaController;
    }

    public void setEnterTransition(Transition enterTransition) {
        this.mEnterTransition = enterTransition;
    }

    public void setReturnTransition(Transition transition) {
        this.mReturnTransition = transition;
    }

    public void setExitTransition(Transition exitTransition) {
        this.mExitTransition = exitTransition;
    }

    public void setReenterTransition(Transition transition) {
        this.mReenterTransition = transition;
    }

    public void setSharedElementEnterTransition(Transition sharedElementEnterTransition) {
        this.mSharedElementEnterTransition = sharedElementEnterTransition;
    }

    public void setSharedElementReturnTransition(Transition transition) {
        this.mSharedElementReturnTransition = transition;
    }

    public void setSharedElementExitTransition(Transition sharedElementExitTransition) {
        this.mSharedElementExitTransition = sharedElementExitTransition;
    }

    public void setSharedElementReenterTransition(Transition transition) {
        this.mSharedElementReenterTransition = transition;
    }

    public Transition getEnterTransition() {
        return this.mEnterTransition;
    }

    public Transition getReturnTransition() {
        Transition transition = this.mReturnTransition;
        return transition == USE_DEFAULT_TRANSITION ? getEnterTransition() : transition;
    }

    public Transition getExitTransition() {
        return this.mExitTransition;
    }

    public Transition getReenterTransition() {
        Transition transition = this.mReenterTransition;
        return transition == USE_DEFAULT_TRANSITION ? getExitTransition() : transition;
    }

    public Transition getSharedElementEnterTransition() {
        return this.mSharedElementEnterTransition;
    }

    public Transition getSharedElementReturnTransition() {
        Transition transition = this.mSharedElementReturnTransition;
        if (transition == USE_DEFAULT_TRANSITION) {
            return getSharedElementEnterTransition();
        }
        return transition;
    }

    public Transition getSharedElementExitTransition() {
        return this.mSharedElementExitTransition;
    }

    public Transition getSharedElementReenterTransition() {
        Transition transition = this.mSharedElementReenterTransition;
        if (transition == USE_DEFAULT_TRANSITION) {
            return getSharedElementExitTransition();
        }
        return transition;
    }

    public void setAllowEnterTransitionOverlap(boolean allow) {
        this.mAllowEnterTransitionOverlap = Boolean.valueOf(allow);
    }

    public boolean getAllowEnterTransitionOverlap() {
        Boolean bool = this.mAllowEnterTransitionOverlap;
        return bool == null ? true : bool.booleanValue();
    }

    public void setAllowReturnTransitionOverlap(boolean allowExitTransitionOverlap) {
        this.mAllowReturnTransitionOverlap = Boolean.valueOf(allowExitTransitionOverlap);
    }

    public boolean getAllowReturnTransitionOverlap() {
        Boolean bool = this.mAllowReturnTransitionOverlap;
        return bool == null ? true : bool.booleanValue();
    }

    public long getTransitionBackgroundFadeDuration() {
        long j = this.mBackgroundFadeDurationMillis;
        return j < 0 ? 300 : j;
    }

    public void setTransitionBackgroundFadeDuration(long fadeDurationMillis) {
        if (fadeDurationMillis >= 0) {
            this.mBackgroundFadeDurationMillis = fadeDurationMillis;
            return;
        }
        throw new IllegalArgumentException("negative durations are not allowed");
    }

    public void setSharedElementsUseOverlay(boolean sharedElementsUseOverlay) {
        this.mSharedElementsUseOverlay = Boolean.valueOf(sharedElementsUseOverlay);
    }

    public boolean getSharedElementsUseOverlay() {
        Boolean bool = this.mSharedElementsUseOverlay;
        return bool == null ? true : bool.booleanValue();
    }

    /* Access modifiers changed, original: 0000 */
    public int getLocalFeaturesPrivate() {
        return super.getLocalFeatures();
    }

    /* Access modifiers changed, original: protected */
    public void setDefaultWindowFormat(int format) {
        super.setDefaultWindowFormat(format);
    }

    /* Access modifiers changed, original: 0000 */
    public void sendCloseSystemWindows() {
        sendCloseSystemWindows(getContext(), null);
    }

    /* Access modifiers changed, original: 0000 */
    public void sendCloseSystemWindows(String reason) {
        sendCloseSystemWindows(getContext(), reason);
    }

    public static void sendCloseSystemWindows(Context context, String reason) {
        if (ActivityManager.isSystemReady()) {
            try {
                ActivityManager.getService().closeSystemDialogs(reason);
            } catch (RemoteException e) {
            }
        }
    }

    public int getStatusBarColor() {
        return this.mStatusBarColor;
    }

    public void setStatusBarColor(int color) {
        this.mStatusBarColor = color;
        this.mForcedStatusBarColor = true;
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.updateColorViews(null, false);
        }
    }

    public int getNavigationBarColor() {
        return this.mNavigationBarColor;
    }

    public void setNavigationBarColor(int color) {
        this.mNavigationBarColor = color;
        this.mForcedNavigationBarColor = true;
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.updateColorViews(null, false);
        }
        PhoneWindowInjector.onNavigationBarColorChange(this);
    }

    public void setNavigationBarDividerColor(int navigationBarDividerColor) {
        this.mNavigationBarDividerColor = navigationBarDividerColor;
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.updateColorViews(null, false);
        }
    }

    public int getNavigationBarDividerColor() {
        return this.mNavigationBarDividerColor;
    }

    public void setStatusBarContrastEnforced(boolean ensureContrast) {
        this.mEnsureStatusBarContrastWhenTransparent = ensureContrast;
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.updateColorViews(null, false);
        }
    }

    public boolean isStatusBarContrastEnforced() {
        return this.mEnsureStatusBarContrastWhenTransparent;
    }

    public void setNavigationBarContrastEnforced(boolean enforceContrast) {
        this.mEnsureNavigationBarContrastWhenTransparent = enforceContrast;
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.updateColorViews(null, false);
        }
    }

    public boolean isNavigationBarContrastEnforced() {
        return this.mEnsureNavigationBarContrastWhenTransparent;
    }

    public void setIsStartingWindow(boolean isStartingWindow) {
        this.mIsStartingWindow = isStartingWindow;
    }

    public void setTheme(int resid) {
        this.mTheme = resid;
        Context context = this.mDecor;
        if (context != null) {
            context = context.getContext();
            if (context instanceof DecorContext) {
                context.setTheme(resid);
            }
        }
    }

    public void setResizingCaptionDrawable(Drawable drawable) {
        this.mDecor.setUserCaptionBackgroundDrawable(drawable);
    }

    public void setDecorCaptionShade(int decorCaptionShade) {
        this.mDecorCaptionShade = decorCaptionShade;
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.updateDecorCaptionShade();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public int getDecorCaptionShade() {
        return this.mDecorCaptionShade;
    }

    public void setAttributes(WindowManager.LayoutParams params) {
        super.setAttributes(params);
        DecorView decorView = this.mDecor;
        if (decorView != null) {
            decorView.updateLogTag(params);
        }
    }

    public WindowInsetsController getInsetsController() {
        return this.mDecor.getWindowInsetsController();
    }

    public void setSystemGestureExclusionRects(List<Rect> rects) {
        getViewRootImpl().setRootSystemGestureExclusionRects(rects);
    }

    public List<Rect> getSystemGestureExclusionRects() {
        return getViewRootImpl().getRootSystemGestureExclusionRects();
    }
}
