package com.android.internal.policy;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.UnsupportedAppUsage;
import android.app.Activity;
import android.app.AppGlobals;
import android.app.WindowConfiguration;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.LinearGradient;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.util.MiuiMultiWindowUtils;
import android.util.Pair;
import android.util.Slog;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ActionMode.Callback2;
import android.view.ContextThemeWrapper;
import android.view.InputQueue;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.ThreadedRenderer;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewOutlineProvider;
import android.view.ViewStub;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.Window.WindowControllerCallback;
import android.view.WindowCallbacks;
import android.view.WindowInsets;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import com.android.internal.R;
import com.android.internal.policy.PhoneWindow.PhoneWindowMenuCallback;
import com.android.internal.view.FloatingActionMode;
import com.android.internal.view.RootViewSurfaceTaker;
import com.android.internal.view.StandaloneActionMode;
import com.android.internal.view.menu.ContextMenuBuilder;
import com.android.internal.view.menu.MenuHelper;
import com.android.internal.widget.ActionBarContextView;
import com.android.internal.widget.BackgroundFallback;
import com.android.internal.widget.DecorCaptionView;
import com.android.internal.widget.FloatingToolbar;
import java.util.List;
import miui.os.DeviceFeature;
import miui.slide.AppSlideConfig;

public class DecorView extends FrameLayout implements RootViewSurfaceTaker, WindowCallbacks {
    private static final boolean DEBUG_MEASURE = false;
    private static final int DECOR_SHADOW_FOCUSED_HEIGHT_IN_DIP = 20;
    private static final int DECOR_SHADOW_UNFOCUSED_HEIGHT_IN_DIP = 5;
    public static final ColorViewAttributes NAVIGATION_BAR_COLOR_VIEW_ATTRIBUTES = new ColorViewAttributes(2, 134217728, 80, 5, 3, Window.NAVIGATION_BAR_BACKGROUND_TRANSITION_NAME, 16908336, 0, null);
    private static final ViewOutlineProvider PIP_OUTLINE_PROVIDER = new ViewOutlineProvider() {
        public void getOutline(View view, Outline outline) {
            outline.setRect(0, 0, view.getWidth(), view.getHeight());
            outline.setAlpha(1.0f);
        }
    };
    private static final int SCRIM_LIGHT = -419430401;
    public static final ColorViewAttributes STATUS_BAR_COLOR_VIEW_ATTRIBUTES = new ColorViewAttributes(4, 67108864, 48, 3, 5, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME, 16908335, 1024, null);
    private static final boolean SWEEP_OPEN_MENU = false;
    private static final String TAG = "DecorView";
    private boolean mAllowUpdateElevation = false;
    private boolean mApplyFloatingHorizontalInsets = false;
    private boolean mApplyFloatingVerticalInsets = false;
    private float mAvailableWidth;
    private BackdropFrameRenderer mBackdropFrameRenderer = null;
    private final BackgroundFallback mBackgroundFallback = new BackgroundFallback();
    private Insets mBackgroundInsets = Insets.NONE;
    private final Rect mBackgroundPadding = new Rect();
    private final int mBarEnterExitDuration;
    private Drawable mCaptionBackgroundDrawable;
    private boolean mChanging;
    ViewGroup mContentRoot;
    private DecorCaptionView mDecorCaptionView;
    int mDefaultOpacity = -1;
    private int mDownY;
    private boolean mDrawLegacyNavigationBarBackground;
    private final Rect mDrawingBounds = new Rect();
    private boolean mElevationAdjustedForStack = false;
    private ObjectAnimator mFadeAnim;
    private final int mFeatureId;
    private ActionMode mFloatingActionMode;
    private View mFloatingActionModeOriginatingView;
    private final Rect mFloatingInsets = new Rect();
    private FloatingToolbar mFloatingToolbar;
    private OnPreDrawListener mFloatingToolbarPreDrawListener;
    final boolean mForceWindowDrawsBarBackgrounds;
    private final Rect mFrameOffsets = new Rect();
    private final Rect mFramePadding = new Rect();
    private boolean mHasCaption = false;
    private final Interpolator mHideInterpolator;
    private final Paint mHorizontalResizeShadowPaint = new Paint();
    private boolean mIsInPictureInPictureMode;
    private Callback mLastBackgroundDrawableCb = null;
    private Insets mLastBackgroundInsets = Insets.NONE;
    @UnsupportedAppUsage
    private int mLastBottomInset = 0;
    private boolean mLastHasBottomStableInset = false;
    private boolean mLastHasLeftStableInset = false;
    private boolean mLastHasRightStableInset = false;
    private boolean mLastHasTopStableInset = false;
    @UnsupportedAppUsage
    private int mLastLeftInset = 0;
    private Drawable mLastOriginalBackgroundDrawable;
    private ViewOutlineProvider mLastOutlineProvider;
    @UnsupportedAppUsage
    private int mLastRightInset = 0;
    private boolean mLastShouldAlwaysConsumeSystemBars = false;
    private int mLastTopInset = 0;
    private int mLastWindowFlags = 0;
    private final Paint mLegacyNavigationBarBackgroundPaint = new Paint();
    String mLogTag = TAG;
    private Drawable mMenuBackground;
    private final ColorViewState mNavigationColorViewState = new ColorViewState(NAVIGATION_BAR_COLOR_VIEW_ATTRIBUTES);
    private Drawable mOriginalBackgroundDrawable;
    private Rect mOutsets = new Rect();
    ActionMode mPrimaryActionMode;
    private PopupWindow mPrimaryActionModePopup;
    private ActionBarContextView mPrimaryActionModeView;
    private int mResizeMode = -1;
    private final int mResizeShadowSize;
    private Drawable mResizingBackgroundDrawable;
    private int mRootScrollY = 0;
    private final int mSemiTransparentBarColor;
    private final Interpolator mShowInterpolator;
    private Runnable mShowPrimaryActionModePopup;
    private final ColorViewState mStatusColorViewState = new ColorViewState(STATUS_BAR_COLOR_VIEW_ATTRIBUTES);
    private View mStatusGuard;
    private Rect mTempRect;
    private Drawable mUserCaptionBackgroundDrawable;
    private final Paint mVerticalResizeShadowPaint = new Paint();
    private boolean mWatchingForMenu;
    @UnsupportedAppUsage
    private PhoneWindow mWindow;
    private boolean mWindowResizeCallbacksAdded = false;

    private class ActionModeCallback2Wrapper extends Callback2 {
        private final ActionMode.Callback mWrapped;

        public ActionModeCallback2Wrapper(ActionMode.Callback wrapped) {
            this.mWrapped = wrapped;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            return this.mWrapped.onCreateActionMode(mode, menu);
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            DecorView.this.requestFitSystemWindows();
            return this.mWrapped.onPrepareActionMode(mode, menu);
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return this.mWrapped.onActionItemClicked(mode, item);
        }

        public void onDestroyActionMode(ActionMode mode) {
            boolean isPrimary;
            this.mWrapped.onDestroyActionMode(mode);
            boolean isFloating = false;
            if (DecorView.this.mContext.getApplicationInfo().targetSdkVersion >= 23) {
                isPrimary = mode == DecorView.this.mPrimaryActionMode;
                if (mode == DecorView.this.mFloatingActionMode) {
                    isFloating = true;
                }
                if (!isPrimary && mode.getType() == 0) {
                    String str = DecorView.this.mLogTag;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Destroying unexpected ActionMode instance of TYPE_PRIMARY; ");
                    stringBuilder.append(mode);
                    stringBuilder.append(" was not the current primary action mode! Expected ");
                    stringBuilder.append(DecorView.this.mPrimaryActionMode);
                    Log.e(str, stringBuilder.toString());
                }
                if (!isFloating && mode.getType() == 1) {
                    String str2 = DecorView.this.mLogTag;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Destroying unexpected ActionMode instance of TYPE_FLOATING; ");
                    stringBuilder2.append(mode);
                    stringBuilder2.append(" was not the current floating action mode! Expected ");
                    stringBuilder2.append(DecorView.this.mFloatingActionMode);
                    Log.e(str2, stringBuilder2.toString());
                }
            } else {
                isPrimary = mode.getType() == 0;
                if (mode.getType() == 1) {
                    isFloating = true;
                }
            }
            if (isPrimary) {
                if (DecorView.this.mPrimaryActionModePopup != null) {
                    DecorView decorView = DecorView.this;
                    decorView.removeCallbacks(decorView.mShowPrimaryActionModePopup);
                }
                if (DecorView.this.mPrimaryActionModeView != null) {
                    DecorView.this.endOnGoingFadeAnimation();
                    final ActionBarContextView lastActionModeView = DecorView.this.mPrimaryActionModeView;
                    DecorView decorView2 = DecorView.this;
                    decorView2.mFadeAnim = ObjectAnimator.ofFloat(decorView2.mPrimaryActionModeView, View.ALPHA, 1.0f, 0.0f);
                    DecorView.this.mFadeAnim.addListener(new AnimatorListener() {
                        public void onAnimationStart(Animator animation) {
                        }

                        public void onAnimationEnd(Animator animation) {
                            if (lastActionModeView == DecorView.this.mPrimaryActionModeView) {
                                lastActionModeView.setVisibility(8);
                                if (DecorView.this.mPrimaryActionModePopup != null) {
                                    DecorView.this.mPrimaryActionModePopup.dismiss();
                                }
                                lastActionModeView.killMode();
                                DecorView.this.mFadeAnim = null;
                                DecorView.this.requestApplyInsets();
                            }
                        }

                        public void onAnimationCancel(Animator animation) {
                        }

                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    DecorView.this.mFadeAnim.start();
                }
                DecorView.this.mPrimaryActionMode = null;
            } else if (isFloating) {
                DecorView.this.cleanupFloatingActionModeViews();
                DecorView.this.mFloatingActionMode = null;
            }
            if (!(DecorView.this.mWindow.getCallback() == null || DecorView.this.mWindow.isDestroyed())) {
                try {
                    DecorView.this.mWindow.getCallback().onActionModeFinished(mode);
                } catch (AbstractMethodError e) {
                }
            }
            DecorView.this.requestFitSystemWindows();
        }

        public void onGetContentRect(ActionMode mode, View view, Rect outRect) {
            ActionMode.Callback callback = this.mWrapped;
            if (callback instanceof Callback2) {
                ((Callback2) callback).onGetContentRect(mode, view, outRect);
            } else {
                super.onGetContentRect(mode, view, outRect);
            }
        }
    }

    public static class ColorViewAttributes {
        final int hideWindowFlag;
        final int horizontalGravity;
        final int id;
        final int seascapeGravity;
        final int systemUiHideFlag;
        final String transitionName;
        final int translucentFlag;
        final int verticalGravity;

        /* synthetic */ ColorViewAttributes(int x0, int x1, int x2, int x3, int x4, String x5, int x6, int x7, AnonymousClass1 x8) {
            this(x0, x1, x2, x3, x4, x5, x6, x7);
        }

        private ColorViewAttributes(int systemUiHideFlag, int translucentFlag, int verticalGravity, int horizontalGravity, int seascapeGravity, String transitionName, int id, int hideWindowFlag) {
            this.id = id;
            this.systemUiHideFlag = systemUiHideFlag;
            this.translucentFlag = translucentFlag;
            this.verticalGravity = verticalGravity;
            this.horizontalGravity = horizontalGravity;
            this.seascapeGravity = seascapeGravity;
            this.transitionName = transitionName;
            this.hideWindowFlag = hideWindowFlag;
        }

        public boolean isPresent(int sysUiVis, int windowFlags, boolean force) {
            return (this.systemUiHideFlag & sysUiVis) == 0 && (this.hideWindowFlag & windowFlags) == 0 && ((Integer.MIN_VALUE & windowFlags) != 0 || force);
        }

        public boolean isVisible(boolean present, int color, int windowFlags, boolean force) {
            return present && (-16777216 & color) != 0 && ((this.translucentFlag & windowFlags) == 0 || force);
        }

        public boolean isVisible(int sysUiVis, int color, int windowFlags, boolean force) {
            return isVisible(isPresent(sysUiVis, windowFlags, force), color, windowFlags, force);
        }
    }

    private static class ColorViewState {
        final ColorViewAttributes attributes;
        int color;
        boolean present = false;
        int targetVisibility = 4;
        View view = null;
        boolean visible;

        ColorViewState(ColorViewAttributes attributes) {
            this.attributes = attributes;
        }
    }

    DecorView(Context context, int featureId, PhoneWindow window, LayoutParams params) {
        super(context);
        boolean z = false;
        this.mFeatureId = featureId;
        this.mShowInterpolator = AnimationUtils.loadInterpolator(context, 17563662);
        this.mHideInterpolator = AnimationUtils.loadInterpolator(context, 17563663);
        this.mBarEnterExitDuration = context.getResources().getInteger(R.integer.dock_enter_exit_duration);
        if (context.getResources().getBoolean(R.bool.config_forceWindowDrawsStatusBarBackground) && context.getApplicationInfo().targetSdkVersion >= 24) {
            z = true;
        }
        this.mForceWindowDrawsBarBackgrounds = z;
        this.mSemiTransparentBarColor = context.getResources().getColor(R.color.system_bar_background_semi_transparent, null);
        updateAvailableWidth();
        setWindow(window);
        updateLogTag(params);
        this.mResizeShadowSize = context.getResources().getDimensionPixelSize(R.dimen.resize_shadow_size);
        initResizingPaints();
        this.mLegacyNavigationBarBackgroundPaint.setColor(-16777216);
    }

    /* Access modifiers changed, original: 0000 */
    public void setBackgroundFallback(Drawable fallbackDrawable) {
        this.mBackgroundFallback.setDrawable(fallbackDrawable);
        boolean z = getBackground() == null && !this.mBackgroundFallback.hasFallback();
        setWillNotDraw(z);
    }

    public Drawable getBackgroundFallback() {
        return this.mBackgroundFallback.getDrawable();
    }

    public boolean gatherTransparentRegion(Region region) {
        return gatherTransparentRegion(this.mStatusColorViewState, region) || gatherTransparentRegion(this.mNavigationColorViewState, region) || super.gatherTransparentRegion(region);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean gatherTransparentRegion(ColorViewState colorViewState, Region region) {
        if (colorViewState.view != null && colorViewState.visible && isResizing()) {
            return colorViewState.view.gatherTransparentRegion(region);
        }
        return false;
    }

    public void onDraw(Canvas c) {
        super.onDraw(c);
        this.mBackgroundFallback.draw(this, this.mContentRoot, c, this.mWindow.mContentParent, this.mStatusColorViewState.view, this.mNavigationColorViewState.view);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean onKeyDown;
        int keyCode = event.getKeyCode();
        boolean isDown = event.getAction() == 0;
        if (isDown && event.getRepeatCount() == 0) {
            if (this.mWindow.mPanelChordingKey > 0 && this.mWindow.mPanelChordingKey != keyCode && dispatchKeyShortcutEvent(event)) {
                return true;
            }
            if (this.mWindow.mPreparedPanel != null && this.mWindow.mPreparedPanel.isOpen) {
                PhoneWindow phoneWindow = this.mWindow;
                if (phoneWindow.performPanelShortcut(phoneWindow.mPreparedPanel, keyCode, event, 0)) {
                    return true;
                }
            }
        }
        if (!this.mWindow.isDestroyed()) {
            boolean handled;
            Window.Callback cb = this.mWindow.getCallback();
            if (cb == null || this.mFeatureId >= 0) {
                handled = super.dispatchKeyEvent(event);
            } else {
                handled = cb.dispatchKeyEvent(event);
            }
            if (handled) {
                return true;
            }
            try {
                if (DeviceFeature.hasMirihiSupport() && event.getAction() == 0) {
                    AppGlobals.mAppSlideConfig.tryGotoTarget(keyCode, getAttachedActivity(), this);
                }
            } catch (Exception e) {
                Slog.d(AppSlideConfig.TAG, e.toString());
            }
        }
        if (isDown) {
            onKeyDown = this.mWindow.onKeyDown(this.mFeatureId, event.getKeyCode(), event);
        } else {
            onKeyDown = this.mWindow.onKeyUp(this.mFeatureId, event.getKeyCode(), event);
        }
        return onKeyDown;
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent ev) {
        if (this.mWindow.mPreparedPanel != null) {
            boolean handled = this.mWindow;
            if (handled.performPanelShortcut(handled.mPreparedPanel, ev.getKeyCode(), ev, 1)) {
                if (this.mWindow.mPreparedPanel != null) {
                    this.mWindow.mPreparedPanel.isHandled = true;
                }
                return true;
            }
        }
        Window.Callback cb = this.mWindow.getCallback();
        boolean handled2 = (cb == null || this.mWindow.isDestroyed() || this.mFeatureId >= 0) ? super.dispatchKeyShortcutEvent(ev) : cb.dispatchKeyShortcutEvent(ev);
        if (handled2) {
            return true;
        }
        PanelFeatureState st = this.mWindow.getPanelState(0, false);
        if (st != null && this.mWindow.mPreparedPanel == null) {
            this.mWindow.preparePanel(st, ev);
            handled2 = this.mWindow.performPanelShortcut(st, ev.getKeyCode(), ev, 1);
            st.isPrepared = false;
            if (handled2) {
                return true;
            }
        }
        return false;
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        Window.Callback cb = this.mWindow.getCallback();
        return (cb == null || this.mWindow.isDestroyed() || this.mFeatureId >= 0) ? super.dispatchTouchEvent(ev) : cb.dispatchTouchEvent(ev);
    }

    public boolean dispatchTrackballEvent(MotionEvent ev) {
        Window.Callback cb = this.mWindow.getCallback();
        return (cb == null || this.mWindow.isDestroyed() || this.mFeatureId >= 0) ? super.dispatchTrackballEvent(ev) : cb.dispatchTrackballEvent(ev);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        Window.Callback cb = this.mWindow.getCallback();
        return (cb == null || this.mWindow.isDestroyed() || this.mFeatureId >= 0) ? super.dispatchGenericMotionEvent(ev) : cb.dispatchGenericMotionEvent(ev);
    }

    public boolean superDispatchKeyEvent(KeyEvent event) {
        boolean z = true;
        if (event.getKeyCode() == 4) {
            int action = event.getAction();
            ActionMode actionMode = this.mPrimaryActionMode;
            if (actionMode != null) {
                if (action == 1) {
                    actionMode.finish();
                }
                return true;
            }
        }
        if (super.dispatchKeyEvent(event)) {
            return true;
        }
        if (getViewRootImpl() == null || !getViewRootImpl().dispatchUnhandledKeyEvent(event)) {
            z = false;
        }
        return z;
    }

    public boolean superDispatchKeyShortcutEvent(KeyEvent event) {
        return super.dispatchKeyShortcutEvent(event);
    }

    public boolean superDispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    public boolean superDispatchTrackballEvent(MotionEvent event) {
        return super.dispatchTrackballEvent(event);
    }

    public boolean superDispatchGenericMotionEvent(MotionEvent event) {
        return super.dispatchGenericMotionEvent(event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return onInterceptTouchEvent(event);
    }

    private boolean isOutOfInnerBounds(int x, int y) {
        return x < 0 || y < 0 || x > getWidth() || y > getHeight();
    }

    private boolean isOutOfBounds(int x, int y) {
        return x < -5 || y < -5 || x > getWidth() + 5 || y > getHeight() + 5;
    }

    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (this.mHasCaption && isShowingCaption() && action == 0 && isOutOfInnerBounds((int) event.getX(), (int) event.getY())) {
            return true;
        }
        if (this.mFeatureId < 0 || action != 0 || !isOutOfBounds((int) event.getX(), (int) event.getY())) {
            return false;
        }
        this.mWindow.closePanel(this.mFeatureId);
        return true;
    }

    public void sendAccessibilityEvent(int eventType) {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            int i = this.mFeatureId;
            if ((i == 0 || i == 6 || i == 2 || i == 5) && getChildCount() == 1) {
                getChildAt(0).sendAccessibilityEvent(eventType);
            } else {
                super.sendAccessibilityEvent(eventType);
            }
        }
    }

    public boolean dispatchPopulateAccessibilityEventInternal(AccessibilityEvent event) {
        Window.Callback cb = this.mWindow.getCallback();
        if (cb == null || this.mWindow.isDestroyed() || !cb.dispatchPopulateAccessibilityEvent(event)) {
            return super.dispatchPopulateAccessibilityEventInternal(event);
        }
        return true;
    }

    /* Access modifiers changed, original: protected */
    public boolean setFrame(int l, int t, int r, int b) {
        boolean changed = super.setFrame(l, t, r, b);
        if (changed) {
            Rect drawingBounds = this.mDrawingBounds;
            getDrawingRect(drawingBounds);
            Drawable fg = getForeground();
            if (fg != null) {
                Rect frameOffsets = this.mFrameOffsets;
                drawingBounds.left += frameOffsets.left;
                drawingBounds.top += frameOffsets.top;
                drawingBounds.right -= frameOffsets.right;
                drawingBounds.bottom -= frameOffsets.bottom;
                fg.setBounds(drawingBounds);
                Rect framePadding = this.mFramePadding;
                drawingBounds.left += framePadding.left - frameOffsets.left;
                drawingBounds.top += framePadding.top - frameOffsets.top;
                drawingBounds.right -= framePadding.right - frameOffsets.right;
                drawingBounds.bottom -= framePadding.bottom - frameOffsets.bottom;
            }
            Drawable bg = super.getBackground();
            if (bg != null) {
                bg.setBounds(drawingBounds);
            }
        }
        return changed;
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00fe  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0122  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x014d  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x014a  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0153  */
    /* JADX WARNING: Removed duplicated region for block: B:82:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0175  */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0087  */
    /* JADX WARNING: Removed duplicated region for block: B:53:0x00fe  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0122  */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x014a  */
    /* JADX WARNING: Removed duplicated region for block: B:67:0x014d  */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0153  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x0175  */
    /* JADX WARNING: Removed duplicated region for block: B:82:? A:{SYNTHETIC, RETURN} */
    public void onMeasure(int r17, int r18) {
        /*
        r16 = this;
        r0 = r16;
        r1 = r16.getContext();
        r1 = r1.getResources();
        r1 = r1.getDisplayMetrics();
        r2 = r16.getResources();
        r2 = r2.getConfiguration();
        r2 = r2.orientation;
        r3 = 0;
        r4 = 1;
        if (r2 != r4) goto L_0x001f;
    L_0x001d:
        r2 = r4;
        goto L_0x0020;
    L_0x001f:
        r2 = r3;
    L_0x0020:
        r5 = android.view.View.MeasureSpec.getMode(r17);
        r6 = android.view.View.MeasureSpec.getMode(r18);
        r7 = 0;
        r0.mApplyFloatingHorizontalInsets = r3;
        r8 = 6;
        r9 = 5;
        r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r11 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        if (r5 != r11) goto L_0x0081;
    L_0x0033:
        r12 = r0.mWindow;
        if (r2 == 0) goto L_0x003a;
    L_0x0037:
        r12 = r12.mFixedWidthMinor;
        goto L_0x003c;
    L_0x003a:
        r12 = r12.mFixedWidthMajor;
    L_0x003c:
        if (r12 == 0) goto L_0x0081;
    L_0x003e:
        r13 = r12.type;
        if (r13 == 0) goto L_0x0081;
    L_0x0042:
        r13 = r12.type;
        if (r13 != r9) goto L_0x004c;
    L_0x0046:
        r13 = r12.getDimension(r1);
        r13 = (int) r13;
        goto L_0x005d;
    L_0x004c:
        r13 = r12.type;
        if (r13 != r8) goto L_0x005c;
    L_0x0050:
        r13 = r1.widthPixels;
        r13 = (float) r13;
        r14 = r1.widthPixels;
        r14 = (float) r14;
        r13 = r12.getFraction(r13, r14);
        r13 = (int) r13;
        goto L_0x005d;
    L_0x005c:
        r13 = 0;
    L_0x005d:
        r14 = android.view.View.MeasureSpec.getSize(r17);
        if (r13 <= 0) goto L_0x006e;
        r15 = java.lang.Math.min(r13, r14);
        r15 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r10);
        r7 = 1;
        goto L_0x0083;
    L_0x006e:
        r15 = r0.mFloatingInsets;
        r15 = r15.left;
        r15 = r14 - r15;
        r10 = r0.mFloatingInsets;
        r10 = r10.right;
        r15 = r15 - r10;
        r10 = android.view.View.MeasureSpec.makeMeasureSpec(r15, r11);
        r0.mApplyFloatingHorizontalInsets = r4;
        r15 = r10;
        goto L_0x0083;
    L_0x0081:
        r15 = r17;
    L_0x0083:
        r0.mApplyFloatingVerticalInsets = r3;
        if (r6 != r11) goto L_0x00e5;
    L_0x0087:
        if (r2 == 0) goto L_0x008e;
    L_0x0089:
        r3 = r0.mWindow;
        r3 = r3.mFixedHeightMajor;
        goto L_0x0092;
    L_0x008e:
        r3 = r0.mWindow;
        r3 = r3.mFixedHeightMinor;
        if (r3 == 0) goto L_0x00e5;
    L_0x0095:
        r10 = r3.type;
        if (r10 == 0) goto L_0x00e5;
    L_0x0099:
        r10 = r3.type;
        if (r10 != r9) goto L_0x00a3;
    L_0x009d:
        r10 = r3.getDimension(r1);
        r10 = (int) r10;
        goto L_0x00b4;
    L_0x00a3:
        r10 = r3.type;
        if (r10 != r8) goto L_0x00b3;
    L_0x00a7:
        r10 = r1.heightPixels;
        r10 = (float) r10;
        r12 = r1.heightPixels;
        r12 = (float) r12;
        r10 = r3.getFraction(r10, r12);
        r10 = (int) r10;
        goto L_0x00b4;
    L_0x00b3:
        r10 = 0;
    L_0x00b4:
        r12 = android.view.View.MeasureSpec.getSize(r18);
        if (r10 <= 0) goto L_0x00c7;
        r4 = java.lang.Math.min(r10, r12);
        r13 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r13);
        r13 = r4;
        goto L_0x00e7;
    L_0x00c7:
        r13 = r0.mWindow;
        r13 = r13.getAttributes();
        r13 = r13.flags;
        r13 = r13 & 256;
        if (r13 != 0) goto L_0x00e5;
    L_0x00d3:
        r13 = r0.mFloatingInsets;
        r13 = r13.top;
        r13 = r12 - r13;
        r14 = r0.mFloatingInsets;
        r14 = r14.bottom;
        r13 = r13 - r14;
        r13 = android.view.View.MeasureSpec.makeMeasureSpec(r13, r11);
        r0.mApplyFloatingVerticalInsets = r4;
        goto L_0x00e7;
    L_0x00e5:
        r13 = r18;
    L_0x00e7:
        r3 = r0.mOutsets;
        r0.getOutsets(r3);
        r3 = r0.mOutsets;
        r3 = r3.top;
        if (r3 > 0) goto L_0x00f8;
    L_0x00f2:
        r3 = r0.mOutsets;
        r3 = r3.bottom;
        if (r3 <= 0) goto L_0x0110;
    L_0x00f8:
        r3 = android.view.View.MeasureSpec.getMode(r13);
        if (r3 == 0) goto L_0x0110;
    L_0x00fe:
        r4 = android.view.View.MeasureSpec.getSize(r13);
        r10 = r0.mOutsets;
        r10 = r10.top;
        r10 = r10 + r4;
        r12 = r0.mOutsets;
        r12 = r12.bottom;
        r10 = r10 + r12;
        r13 = android.view.View.MeasureSpec.makeMeasureSpec(r10, r3);
    L_0x0110:
        r3 = r0.mOutsets;
        r3 = r3.left;
        if (r3 > 0) goto L_0x011c;
    L_0x0116:
        r3 = r0.mOutsets;
        r3 = r3.right;
        if (r3 <= 0) goto L_0x0134;
    L_0x011c:
        r3 = android.view.View.MeasureSpec.getMode(r15);
        if (r3 == 0) goto L_0x0134;
    L_0x0122:
        r4 = android.view.View.MeasureSpec.getSize(r15);
        r10 = r0.mOutsets;
        r10 = r10.left;
        r10 = r10 + r4;
        r12 = r0.mOutsets;
        r12 = r12.right;
        r10 = r10 + r12;
        r15 = android.view.View.MeasureSpec.makeMeasureSpec(r10, r3);
    L_0x0134:
        super.onMeasure(r15, r13);
        r3 = r16.getMeasuredWidth();
        r4 = 0;
        r10 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r12 = android.view.View.MeasureSpec.makeMeasureSpec(r3, r10);
        if (r7 != 0) goto L_0x0173;
    L_0x0144:
        if (r5 != r11) goto L_0x0173;
    L_0x0146:
        r10 = r0.mWindow;
        if (r2 == 0) goto L_0x014d;
    L_0x014a:
        r10 = r10.mMinWidthMinor;
        goto L_0x014f;
    L_0x014d:
        r10 = r10.mMinWidthMajor;
    L_0x014f:
        r11 = r10.type;
        if (r11 == 0) goto L_0x0173;
    L_0x0153:
        r11 = r10.type;
        if (r11 != r9) goto L_0x015d;
    L_0x0157:
        r8 = r10.getDimension(r1);
        r8 = (int) r8;
        goto L_0x016a;
    L_0x015d:
        r9 = r10.type;
        if (r9 != r8) goto L_0x0169;
    L_0x0161:
        r8 = r0.mAvailableWidth;
        r8 = r10.getFraction(r8, r8);
        r8 = (int) r8;
        goto L_0x016a;
    L_0x0169:
        r8 = 0;
    L_0x016a:
        if (r3 >= r8) goto L_0x0173;
    L_0x016c:
        r9 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r12 = android.view.View.MeasureSpec.makeMeasureSpec(r8, r9);
        r4 = 1;
    L_0x0173:
        if (r4 == 0) goto L_0x0178;
    L_0x0175:
        super.onMeasure(r12, r13);
    L_0x0178:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.policy.DecorView.onMeasure(int, int):void");
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        getOutsets(this.mOutsets);
        if (this.mOutsets.left > 0) {
            offsetLeftAndRight(-this.mOutsets.left);
        }
        if (this.mOutsets.top > 0) {
            offsetTopAndBottom(-this.mOutsets.top);
        }
        if (this.mApplyFloatingVerticalInsets) {
            offsetTopAndBottom(this.mFloatingInsets.top);
        }
        if (this.mApplyFloatingHorizontalInsets) {
            offsetLeftAndRight(this.mFloatingInsets.left);
        }
        updateElevation();
        this.mAllowUpdateElevation = true;
        if (changed && this.mResizeMode == 1) {
            getViewRootImpl().requestInvalidateRootRenderNode();
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        Drawable drawable = this.mMenuBackground;
        if (drawable != null) {
            drawable.draw(canvas);
        }
    }

    public boolean showContextMenuForChild(View originalView) {
        return showContextMenuForChildInternal(originalView, Float.NaN, Float.NaN);
    }

    public boolean showContextMenuForChild(View originalView, float x, float y) {
        return showContextMenuForChildInternal(originalView, x, y);
    }

    private boolean showContextMenuForChildInternal(View originalView, float x, float y) {
        MenuHelper helper;
        if (this.mWindow.mContextMenuHelper != null) {
            this.mWindow.mContextMenuHelper.dismiss();
            this.mWindow.mContextMenuHelper = null;
        }
        PhoneWindowMenuCallback callback = this.mWindow.mContextMenuCallback;
        if (this.mWindow.mContextMenu == null) {
            this.mWindow.mContextMenu = new ContextMenuBuilder(getContext());
            this.mWindow.mContextMenu.setCallback(callback);
        } else {
            this.mWindow.mContextMenu.clearAll();
        }
        boolean isPopup = (Float.isNaN(x) || Float.isNaN(y)) ? false : true;
        if (isPopup) {
            helper = this.mWindow.mContextMenu.showPopup(getContext(), originalView, x, y);
        } else {
            helper = this.mWindow.mContextMenu.showDialog(originalView, originalView.getWindowToken());
        }
        if (helper != null) {
            callback.setShowDialogForSubmenu(!isPopup);
            helper.setPresenterCallback(callback);
        }
        this.mWindow.mContextMenuHelper = helper;
        if (helper != null) {
            return true;
        }
        return false;
    }

    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback) {
        return startActionModeForChild(originalView, callback, 0);
    }

    public ActionMode startActionModeForChild(View child, ActionMode.Callback callback, int type) {
        return startActionMode(child, callback, type);
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        return startActionMode(callback, 0);
    }

    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        return startActionMode(this, callback, type);
    }

    private ActionMode startActionMode(View originatingView, ActionMode.Callback callback, int type) {
        Callback2 wrappedCallback = new ActionModeCallback2Wrapper(callback);
        ActionMode mode = null;
        if (!(this.mWindow.getCallback() == null || this.mWindow.isDestroyed())) {
            try {
                mode = this.mWindow.getCallback().onWindowStartingActionMode(wrappedCallback, type);
            } catch (AbstractMethodError e) {
                if (type == 0) {
                    try {
                        mode = this.mWindow.getCallback().onWindowStartingActionMode(wrappedCallback);
                    } catch (AbstractMethodError e2) {
                    }
                }
            }
        }
        if (mode == null) {
            mode = createActionMode(type, wrappedCallback, originatingView);
            if (mode == null || !wrappedCallback.onCreateActionMode(mode, mode.getMenu())) {
                mode = null;
            } else {
                setHandledActionMode(mode);
            }
        } else if (mode.getType() == 0) {
            cleanupPrimaryActionMode();
            this.mPrimaryActionMode = mode;
        } else if (mode.getType() == 1) {
            ActionMode actionMode = this.mFloatingActionMode;
            if (actionMode != null) {
                actionMode.finish();
            }
            this.mFloatingActionMode = mode;
        }
        if (!(mode == null || this.mWindow.getCallback() == null || this.mWindow.isDestroyed())) {
            try {
                this.mWindow.getCallback().onActionModeStarted(mode);
            } catch (AbstractMethodError e3) {
            }
        }
        return mode;
    }

    private void cleanupPrimaryActionMode() {
        ActionMode actionMode = this.mPrimaryActionMode;
        if (actionMode != null) {
            actionMode.finish();
            this.mPrimaryActionMode = null;
        }
        ActionBarContextView actionBarContextView = this.mPrimaryActionModeView;
        if (actionBarContextView != null) {
            actionBarContextView.killMode();
        }
    }

    private void cleanupFloatingActionModeViews() {
        FloatingToolbar floatingToolbar = this.mFloatingToolbar;
        if (floatingToolbar != null) {
            floatingToolbar.dismiss();
            this.mFloatingToolbar = null;
        }
        View view = this.mFloatingActionModeOriginatingView;
        if (view != null) {
            if (this.mFloatingToolbarPreDrawListener != null) {
                view.getViewTreeObserver().removeOnPreDrawListener(this.mFloatingToolbarPreDrawListener);
                this.mFloatingToolbarPreDrawListener = null;
            }
            this.mFloatingActionModeOriginatingView = null;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void startChanging() {
        this.mChanging = true;
    }

    /* Access modifiers changed, original: 0000 */
    public void finishChanging() {
        this.mChanging = false;
        drawableChanged();
    }

    public void setWindowBackground(Drawable drawable) {
        if (this.mOriginalBackgroundDrawable != drawable) {
            this.mOriginalBackgroundDrawable = drawable;
            updateBackgroundDrawable();
            boolean z = false;
            if (drawable != null) {
                if (this.mWindow.isTranslucent() || this.mWindow.isShowingWallpaper()) {
                    z = true;
                }
                this.mResizingBackgroundDrawable = enforceNonTranslucentBackground(drawable, z);
            } else {
                Drawable drawable2 = this.mWindow.mBackgroundDrawable;
                Drawable drawable3 = this.mWindow.mBackgroundFallbackDrawable;
                if (this.mWindow.isTranslucent() || this.mWindow.isShowingWallpaper()) {
                    z = true;
                }
                this.mResizingBackgroundDrawable = getResizingBackgroundDrawable(drawable2, drawable3, z);
            }
            Drawable drawable4 = this.mResizingBackgroundDrawable;
            if (drawable4 != null) {
                drawable4.getPadding(this.mBackgroundPadding);
            } else {
                this.mBackgroundPadding.setEmpty();
            }
            drawableChanged();
        }
    }

    public void setBackgroundDrawable(Drawable background) {
        if (this.mOriginalBackgroundDrawable != background) {
            this.mOriginalBackgroundDrawable = background;
            updateBackgroundDrawable();
            if (!View.sBrokenWindowBackground) {
                drawableChanged();
            }
        }
    }

    public void setWindowFrame(Drawable drawable) {
        if (getForeground() != drawable) {
            setForeground(drawable);
            if (drawable != null) {
                drawable.getPadding(this.mFramePadding);
            } else {
                this.mFramePadding.setEmpty();
            }
            drawableChanged();
        }
    }

    public void onWindowSystemUiVisibilityChanged(int visible) {
        updateColorViews(null, true);
        updateDecorCaptionStatus(getResources().getConfiguration());
        View view = this.mStatusGuard;
        if (view != null && view.getVisibility() == 0) {
            updateStatusGuardColor();
        }
    }

    public WindowInsets onApplyWindowInsets(WindowInsets insets) {
        LayoutParams attrs = this.mWindow.getAttributes();
        this.mFloatingInsets.setEmpty();
        if ((attrs.flags & 256) == 0) {
            if (attrs.height == -2) {
                this.mFloatingInsets.top = insets.getSystemWindowInsetTop();
                this.mFloatingInsets.bottom = insets.getSystemWindowInsetBottom();
                insets = insets.inset(0, insets.getSystemWindowInsetTop(), 0, insets.getSystemWindowInsetBottom());
            }
            if (this.mWindow.getAttributes().width == -2) {
                this.mFloatingInsets.left = insets.getSystemWindowInsetTop();
                this.mFloatingInsets.right = insets.getSystemWindowInsetBottom();
                insets = insets.inset(insets.getSystemWindowInsetLeft(), 0, insets.getSystemWindowInsetRight(), 0);
            }
        }
        this.mFrameOffsets.set(insets.getSystemWindowInsetsAsRect());
        insets = updateStatusGuard(updateColorViews(insets, true));
        if (getForeground() != null) {
            drawableChanged();
        }
        return insets;
    }

    public boolean isTransitionGroup() {
        return false;
    }

    public static int getColorViewTopInset(int stableTop, int systemTop) {
        return Math.min(stableTop, systemTop);
    }

    public static int getColorViewBottomInset(int stableBottom, int systemBottom) {
        return Math.min(stableBottom, systemBottom);
    }

    public static int getColorViewRightInset(int stableRight, int systemRight) {
        return Math.min(stableRight, systemRight);
    }

    public static int getColorViewLeftInset(int stableLeft, int systemLeft) {
        return Math.min(stableLeft, systemLeft);
    }

    public static boolean isNavBarToRightEdge(int bottomInset, int rightInset) {
        return bottomInset == 0 && rightInset > 0;
    }

    public static boolean isNavBarToLeftEdge(int bottomInset, int leftInset) {
        return bottomInset == 0 && leftInset > 0;
    }

    public static int getNavBarSize(int bottomInset, int rightInset, int leftInset) {
        if (isNavBarToRightEdge(bottomInset, rightInset)) {
            return rightInset;
        }
        return isNavBarToLeftEdge(bottomInset, leftInset) ? leftInset : bottomInset;
    }

    public static void getNavigationBarRect(int canvasWidth, int canvasHeight, Rect stableInsets, Rect contentInsets, Rect outRect, float scale) {
        int bottomInset = (int) (((float) getColorViewBottomInset(stableInsets.bottom, contentInsets.bottom)) * scale);
        int leftInset = (int) (((float) getColorViewLeftInset(stableInsets.left, contentInsets.left)) * scale);
        int rightInset = (int) (((float) getColorViewLeftInset(stableInsets.right, contentInsets.right)) * scale);
        int size = getNavBarSize(bottomInset, rightInset, leftInset);
        if (isNavBarToRightEdge(bottomInset, rightInset)) {
            outRect.set(canvasWidth - size, 0, canvasWidth, canvasHeight);
        } else if (isNavBarToLeftEdge(bottomInset, leftInset)) {
            outRect.set(0, 0, size, canvasHeight);
        } else {
            outRect.set(0, canvasHeight - size, canvasWidth, canvasHeight);
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:173:0x025e  */
    /* JADX WARNING: Removed duplicated region for block: B:172:0x024f  */
    /* JADX WARNING: Removed duplicated region for block: B:178:? A:{SYNTHETIC, RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:176:0x0269  */
    public android.view.WindowInsets updateColorViews(android.view.WindowInsets r28, boolean r29) {
        /*
        r27 = this;
        r11 = r27;
        r12 = r28;
        r0 = r11.mWindow;
        r13 = r0.getAttributes();
        r0 = r13.systemUiVisibility;
        r1 = r27.getWindowSystemUiVisibility();
        r14 = r0 | r1;
        r0 = r11.mWindow;
        r0 = r0.getAttributes();
        r0 = r0.type;
        r15 = 1;
        r10 = 0;
        r1 = 2011; // 0x7db float:2.818E-42 double:9.936E-321;
        if (r0 != r1) goto L_0x0022;
    L_0x0020:
        r0 = r15;
        goto L_0x0023;
    L_0x0022:
        r0 = r10;
    L_0x0023:
        r16 = r0;
        r0 = r11.mWindow;
        r0 = r0.mIsFloating;
        r17 = -2147483648; // 0xffffffff80000000 float:-0.0 double:NaN;
        if (r0 == 0) goto L_0x0033;
    L_0x002d:
        if (r16 == 0) goto L_0x0030;
    L_0x002f:
        goto L_0x0033;
    L_0x0030:
        r15 = r10;
        goto L_0x018e;
    L_0x0033:
        r0 = r27.isLaidOut();
        r0 = r0 ^ r15;
        r1 = r11.mLastWindowFlags;
        r2 = r13.flags;
        r1 = r1 ^ r2;
        r1 = r1 & r17;
        if (r1 == 0) goto L_0x0043;
    L_0x0041:
        r1 = r15;
        goto L_0x0044;
    L_0x0043:
        r1 = r10;
    L_0x0044:
        r0 = r0 | r1;
        r1 = r13.flags;
        r11.mLastWindowFlags = r1;
        if (r12 == 0) goto L_0x00d8;
    L_0x004b:
        r1 = r28.getStableInsetTop();
        r2 = r28.getSystemWindowInsetTop();
        r1 = getColorViewTopInset(r1, r2);
        r11.mLastTopInset = r1;
        r1 = r28.getStableInsetBottom();
        r2 = r28.getSystemWindowInsetBottom();
        r1 = getColorViewBottomInset(r1, r2);
        r11.mLastBottomInset = r1;
        r1 = r28.getStableInsetRight();
        r2 = r28.getSystemWindowInsetRight();
        r1 = getColorViewRightInset(r1, r2);
        r11.mLastRightInset = r1;
        r1 = r28.getStableInsetLeft();
        r2 = r28.getSystemWindowInsetLeft();
        r1 = getColorViewRightInset(r1, r2);
        r11.mLastLeftInset = r1;
        r1 = r28.getStableInsetTop();
        if (r1 == 0) goto L_0x008b;
    L_0x0089:
        r1 = r15;
        goto L_0x008c;
    L_0x008b:
        r1 = r10;
    L_0x008c:
        r2 = r11.mLastHasTopStableInset;
        if (r1 == r2) goto L_0x0092;
    L_0x0090:
        r2 = r15;
        goto L_0x0093;
    L_0x0092:
        r2 = r10;
    L_0x0093:
        r0 = r0 | r2;
        r11.mLastHasTopStableInset = r1;
        r2 = r28.getStableInsetBottom();
        if (r2 == 0) goto L_0x009e;
    L_0x009c:
        r2 = r15;
        goto L_0x009f;
    L_0x009e:
        r2 = r10;
    L_0x009f:
        r3 = r11.mLastHasBottomStableInset;
        if (r2 == r3) goto L_0x00a5;
    L_0x00a3:
        r3 = r15;
        goto L_0x00a6;
    L_0x00a5:
        r3 = r10;
    L_0x00a6:
        r0 = r0 | r3;
        r11.mLastHasBottomStableInset = r2;
        r3 = r28.getStableInsetRight();
        if (r3 == 0) goto L_0x00b1;
    L_0x00af:
        r3 = r15;
        goto L_0x00b2;
    L_0x00b1:
        r3 = r10;
    L_0x00b2:
        r4 = r11.mLastHasRightStableInset;
        if (r3 == r4) goto L_0x00b8;
    L_0x00b6:
        r4 = r15;
        goto L_0x00b9;
    L_0x00b8:
        r4 = r10;
    L_0x00b9:
        r0 = r0 | r4;
        r11.mLastHasRightStableInset = r3;
        r4 = r28.getStableInsetLeft();
        if (r4 == 0) goto L_0x00c4;
    L_0x00c2:
        r4 = r15;
        goto L_0x00c5;
    L_0x00c4:
        r4 = r10;
    L_0x00c5:
        r5 = r11.mLastHasLeftStableInset;
        if (r4 == r5) goto L_0x00cb;
    L_0x00c9:
        r5 = r15;
        goto L_0x00cc;
    L_0x00cb:
        r5 = r10;
    L_0x00cc:
        r0 = r0 | r5;
        r11.mLastHasLeftStableInset = r4;
        r5 = r28.shouldAlwaysConsumeSystemBars();
        r11.mLastShouldAlwaysConsumeSystemBars = r5;
        r18 = r0;
        goto L_0x00da;
    L_0x00d8:
        r18 = r0;
    L_0x00da:
        r0 = r11.mLastBottomInset;
        r1 = r11.mLastRightInset;
        r19 = isNavBarToRightEdge(r0, r1);
        r0 = r11.mLastBottomInset;
        r1 = r11.mLastLeftInset;
        r20 = isNavBarToLeftEdge(r0, r1);
        r0 = r11.mLastBottomInset;
        r1 = r11.mLastRightInset;
        r2 = r11.mLastLeftInset;
        r21 = getNavBarSize(r0, r1, r2);
        r1 = r11.mNavigationColorViewState;
        r3 = r27.calculateNavigationBarColor();
        r0 = r11.mWindow;
        r4 = r0.mNavigationBarDividerColor;
        if (r19 != 0) goto L_0x0105;
    L_0x0100:
        if (r20 == 0) goto L_0x0103;
    L_0x0102:
        goto L_0x0105;
    L_0x0103:
        r6 = r10;
        goto L_0x0106;
    L_0x0105:
        r6 = r15;
    L_0x0106:
        r8 = 0;
        if (r29 == 0) goto L_0x010d;
    L_0x0109:
        if (r18 != 0) goto L_0x010d;
    L_0x010b:
        r9 = r15;
        goto L_0x010e;
    L_0x010d:
        r9 = r10;
    L_0x010e:
        r7 = r11.mForceWindowDrawsBarBackgrounds;
        r0 = r27;
        r2 = r14;
        r5 = r21;
        r22 = r7;
        r7 = r20;
        r15 = r10;
        r10 = r22;
        r0.updateColorViewInt(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
        r10 = r11.mDrawLegacyNavigationBarBackground;
        r0 = r11.mNavigationColorViewState;
        r0 = r0.visible;
        if (r0 == 0) goto L_0x0135;
    L_0x0127:
        r0 = r11.mWindow;
        r0 = r0.getAttributes();
        r0 = r0.flags;
        r0 = r0 & r17;
        if (r0 != 0) goto L_0x0135;
    L_0x0133:
        r0 = 1;
        goto L_0x0136;
    L_0x0135:
        r0 = r15;
    L_0x0136:
        r11.mDrawLegacyNavigationBarBackground = r0;
        r0 = r11.mDrawLegacyNavigationBarBackground;
        if (r10 == r0) goto L_0x0145;
    L_0x013c:
        r0 = r27.getViewRootImpl();
        if (r0 == 0) goto L_0x0145;
    L_0x0142:
        r0.requestInvalidateRootRenderNode();
    L_0x0145:
        if (r19 == 0) goto L_0x014f;
    L_0x0147:
        r0 = r11.mNavigationColorViewState;
        r0 = r0.present;
        if (r0 == 0) goto L_0x014f;
    L_0x014d:
        r0 = 1;
        goto L_0x0150;
    L_0x014f:
        r0 = r15;
    L_0x0150:
        r22 = r0;
        if (r20 == 0) goto L_0x015c;
    L_0x0154:
        r0 = r11.mNavigationColorViewState;
        r0 = r0.present;
        if (r0 == 0) goto L_0x015c;
    L_0x015a:
        r0 = 1;
        goto L_0x015d;
    L_0x015c:
        r0 = r15;
    L_0x015d:
        r24 = r0;
        if (r22 == 0) goto L_0x0165;
    L_0x0161:
        r0 = r11.mLastRightInset;
        r8 = r0;
        goto L_0x016c;
    L_0x0165:
        if (r24 == 0) goto L_0x016b;
    L_0x0167:
        r0 = r11.mLastLeftInset;
        r8 = r0;
        goto L_0x016c;
    L_0x016b:
        r8 = r15;
        r1 = r11.mStatusColorViewState;
        r3 = r27.calculateStatusBarColor();
        r4 = 0;
        r5 = r11.mLastTopInset;
        r6 = 0;
        if (r29 == 0) goto L_0x017d;
    L_0x0179:
        if (r18 != 0) goto L_0x017d;
    L_0x017b:
        r9 = 1;
        goto L_0x017e;
    L_0x017d:
        r9 = r15;
    L_0x017e:
        r7 = r11.mForceWindowDrawsBarBackgrounds;
        r0 = r27;
        r2 = r14;
        r25 = r7;
        r7 = r24;
        r26 = r10;
        r10 = r25;
        r0.updateColorViewInt(r1, r2, r3, r4, r5, r6, r7, r8, r9, r10);
    L_0x018e:
        r0 = r14 & 2;
        if (r0 == 0) goto L_0x0194;
    L_0x0192:
        r0 = 1;
        goto L_0x0195;
    L_0x0194:
        r0 = r15;
    L_0x0195:
        r1 = r11.mForceWindowDrawsBarBackgrounds;
        if (r1 == 0) goto L_0x01a5;
    L_0x0199:
        r1 = r13.flags;
        r1 = r1 & r17;
        if (r1 != 0) goto L_0x01a5;
    L_0x019f:
        r1 = r14 & 512;
        if (r1 != 0) goto L_0x01a5;
    L_0x01a3:
        if (r0 == 0) goto L_0x01ab;
    L_0x01a5:
        r1 = r11.mLastShouldAlwaysConsumeSystemBars;
        if (r1 == 0) goto L_0x01ad;
    L_0x01a9:
        if (r0 == 0) goto L_0x01ad;
    L_0x01ab:
        r1 = 1;
        goto L_0x01ae;
    L_0x01ad:
        r1 = r15;
    L_0x01ae:
        r2 = r13.flags;
        r2 = r2 & r17;
        if (r2 == 0) goto L_0x01ba;
    L_0x01b4:
        r2 = r14 & 512;
        if (r2 != 0) goto L_0x01ba;
    L_0x01b8:
        if (r0 == 0) goto L_0x01bc;
    L_0x01ba:
        if (r1 == 0) goto L_0x01be;
    L_0x01bc:
        r2 = 1;
        goto L_0x01bf;
    L_0x01be:
        r2 = r15;
    L_0x01bf:
        r3 = r14 & 4;
        if (r3 != 0) goto L_0x01cc;
    L_0x01c3:
        r3 = r13.flags;
        r3 = r3 & 1024;
        if (r3 == 0) goto L_0x01ca;
    L_0x01c9:
        goto L_0x01cc;
    L_0x01ca:
        r3 = r15;
        goto L_0x01cd;
    L_0x01cc:
        r3 = 1;
    L_0x01cd:
        r4 = r14 & 1024;
        if (r4 != 0) goto L_0x01e6;
    L_0x01d1:
        r4 = r13.flags;
        r4 = r4 & 256;
        if (r4 != 0) goto L_0x01e6;
    L_0x01d7:
        r4 = r13.flags;
        r5 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
        r4 = r4 & r5;
        if (r4 != 0) goto L_0x01e6;
    L_0x01de:
        r4 = r11.mForceWindowDrawsBarBackgrounds;
        if (r4 == 0) goto L_0x01e6;
    L_0x01e2:
        r4 = r11.mLastTopInset;
        if (r4 != 0) goto L_0x01ec;
    L_0x01e6:
        r4 = r11.mLastShouldAlwaysConsumeSystemBars;
        if (r4 == 0) goto L_0x01ef;
    L_0x01ea:
        if (r3 == 0) goto L_0x01ef;
    L_0x01ec:
        r23 = 1;
        goto L_0x01f1;
    L_0x01ef:
        r23 = r15;
    L_0x01f1:
        r4 = r23;
        if (r4 == 0) goto L_0x01f8;
    L_0x01f5:
        r10 = r11.mLastTopInset;
        goto L_0x01f9;
    L_0x01f8:
        r10 = r15;
    L_0x01f9:
        r5 = r10;
        if (r2 == 0) goto L_0x01ff;
    L_0x01fc:
        r10 = r11.mLastRightInset;
        goto L_0x0200;
    L_0x01ff:
        r10 = r15;
    L_0x0200:
        r6 = r10;
        if (r2 == 0) goto L_0x0206;
    L_0x0203:
        r10 = r11.mLastBottomInset;
        goto L_0x0207;
    L_0x0206:
        r10 = r15;
    L_0x0207:
        r7 = r10;
        if (r2 == 0) goto L_0x020d;
    L_0x020a:
        r10 = r11.mLastLeftInset;
        goto L_0x020e;
    L_0x020d:
        r10 = r15;
    L_0x020e:
        r8 = r10;
        r9 = r11.mContentRoot;
        if (r9 == 0) goto L_0x024c;
    L_0x0213:
        r9 = r9.getLayoutParams();
        r9 = r9 instanceof android.view.ViewGroup.MarginLayoutParams;
        if (r9 == 0) goto L_0x024c;
    L_0x021b:
        r9 = r11.mContentRoot;
        r9 = r9.getLayoutParams();
        r9 = (android.view.ViewGroup.MarginLayoutParams) r9;
        r10 = r9.topMargin;
        if (r10 != r5) goto L_0x0233;
    L_0x0227:
        r10 = r9.rightMargin;
        if (r10 != r6) goto L_0x0233;
    L_0x022b:
        r10 = r9.bottomMargin;
        if (r10 != r7) goto L_0x0233;
    L_0x022f:
        r10 = r9.leftMargin;
        if (r10 == r8) goto L_0x0245;
    L_0x0233:
        r9.topMargin = r5;
        r9.rightMargin = r6;
        r9.bottomMargin = r7;
        r9.leftMargin = r8;
        r10 = r11.mContentRoot;
        r10.setLayoutParams(r9);
        if (r12 != 0) goto L_0x0245;
    L_0x0242:
        r27.requestApplyInsets();
    L_0x0245:
        if (r12 == 0) goto L_0x024c;
    L_0x0247:
        r10 = r12.inset(r8, r5, r6, r7);
        goto L_0x024d;
    L_0x024c:
        r10 = r12;
    L_0x024d:
        if (r1 == 0) goto L_0x025e;
    L_0x024f:
        r9 = r11.mLastLeftInset;
        r12 = r11.mLastRightInset;
        r17 = r0;
        r0 = r11.mLastBottomInset;
        r0 = android.graphics.Insets.of(r9, r15, r12, r0);
        r11.mBackgroundInsets = r0;
        goto L_0x0264;
    L_0x025e:
        r17 = r0;
        r0 = android.graphics.Insets.NONE;
        r11.mBackgroundInsets = r0;
    L_0x0264:
        r27.updateBackgroundDrawable();
        if (r10 == 0) goto L_0x026d;
    L_0x0269:
        r10 = r10.consumeStableInsets();
    L_0x026d:
        return r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.policy.DecorView.updateColorViews(android.view.WindowInsets, boolean):android.view.WindowInsets");
    }

    private void updateBackgroundDrawable() {
        if (this.mBackgroundInsets == null) {
            this.mBackgroundInsets = Insets.NONE;
        }
        if (!this.mBackgroundInsets.equals(this.mLastBackgroundInsets) || this.mLastOriginalBackgroundDrawable != this.mOriginalBackgroundDrawable) {
            if (this.mOriginalBackgroundDrawable == null || this.mBackgroundInsets.equals(Insets.NONE)) {
                super.setBackgroundDrawable(this.mOriginalBackgroundDrawable);
            } else {
                super.setBackgroundDrawable(new InsetDrawable(this.mOriginalBackgroundDrawable, this.mBackgroundInsets.left, this.mBackgroundInsets.top, this.mBackgroundInsets.right, this.mBackgroundInsets.bottom) {
                    public boolean getPadding(Rect padding) {
                        return getDrawable().getPadding(padding);
                    }
                });
            }
            this.mLastBackgroundInsets = this.mBackgroundInsets;
            this.mLastOriginalBackgroundDrawable = this.mOriginalBackgroundDrawable;
        }
    }

    public Drawable getBackground() {
        return this.mOriginalBackgroundDrawable;
    }

    private int calculateStatusBarColor() {
        return calculateBarColor(this.mWindow.getAttributes().flags, 67108864, this.mSemiTransparentBarColor, this.mWindow.mStatusBarColor, getWindowSystemUiVisibility(), 8192, this.mWindow.mEnsureStatusBarContrastWhenTransparent);
    }

    private int calculateNavigationBarColor() {
        int i = this.mWindow.getAttributes().flags;
        int i2 = this.mSemiTransparentBarColor;
        int i3 = this.mWindow.mNavigationBarColor;
        int windowSystemUiVisibility = getWindowSystemUiVisibility();
        boolean z = this.mWindow.mEnsureNavigationBarContrastWhenTransparent && getContext().getResources().getBoolean(R.bool.config_navBarNeedsScrim);
        return calculateBarColor(i, 134217728, i2, i3, windowSystemUiVisibility, 16, z);
    }

    public static int calculateBarColor(int flags, int translucentFlag, int semiTransparentBarColor, int barColor, int sysuiVis, int lightSysuiFlag, boolean scrimTransparent) {
        if ((flags & translucentFlag) != 0) {
            return semiTransparentBarColor;
        }
        if ((Integer.MIN_VALUE & flags) == 0) {
            return -16777216;
        }
        if (!scrimTransparent || Color.alpha(barColor) != 0) {
            return barColor;
        }
        return (sysuiVis & lightSysuiFlag) != 0 ? SCRIM_LIGHT : semiTransparentBarColor;
    }

    private int getCurrentColor(ColorViewState state) {
        if (state.visible) {
            return state.color;
        }
        return 0;
    }

    /* JADX WARNING: Missing block: B:53:0x00e8, code skipped:
            if (r6.leftMargin != r13) goto L_0x00f8;
     */
    private void updateColorViewInt(com.android.internal.policy.DecorView.ColorViewState r20, int r21, int r22, int r23, int r24, boolean r25, boolean r26, int r27, boolean r28, boolean r29) {
        /*
        r19 = this;
        r0 = r19;
        r1 = r20;
        r2 = r22;
        r3 = r23;
        r4 = r25;
        r5 = r26;
        r6 = r27;
        r7 = r29;
        r8 = r1.attributes;
        r9 = r0.mWindow;
        r9 = r9.getAttributes();
        r9 = r9.flags;
        r10 = r21;
        r8 = r8.isPresent(r10, r9, r7);
        r1.present = r8;
        r8 = r1.attributes;
        r9 = r1.present;
        r11 = r0.mWindow;
        r11 = r11.getAttributes();
        r11 = r11.flags;
        r8 = r8.isVisible(r9, r2, r11, r7);
        if (r8 == 0) goto L_0x003e;
    L_0x0034:
        r12 = r19.isResizing();
        if (r12 != 0) goto L_0x003e;
    L_0x003a:
        if (r24 <= 0) goto L_0x003e;
    L_0x003c:
        r12 = 1;
        goto L_0x003f;
    L_0x003e:
        r12 = 0;
    L_0x003f:
        r13 = 0;
        r14 = r1.view;
        r15 = -1;
        if (r4 == 0) goto L_0x0048;
    L_0x0045:
        r16 = r15;
        goto L_0x004a;
    L_0x0048:
        r16 = r24;
    L_0x004a:
        r17 = r16;
        if (r4 == 0) goto L_0x0050;
    L_0x004e:
        r15 = r24;
    L_0x0050:
        if (r4 == 0) goto L_0x005c;
    L_0x0052:
        r9 = r1.attributes;
        if (r5 == 0) goto L_0x0059;
    L_0x0056:
        r9 = r9.seascapeGravity;
        goto L_0x0060;
    L_0x0059:
        r9 = r9.horizontalGravity;
        goto L_0x0060;
    L_0x005c:
        r9 = r1.attributes;
        r9 = r9.verticalGravity;
        if (r14 != 0) goto L_0x00a2;
    L_0x0063:
        if (r12 == 0) goto L_0x009e;
    L_0x0065:
        r11 = new android.view.View;
        r7 = r0.mContext;
        r11.<init>(r7);
        r14 = r11;
        r1.view = r11;
        setColor(r14, r2, r3, r4, r5);
        r7 = r1.attributes;
        r7 = r7.transitionName;
        r14.setTransitionName(r7);
        r7 = r1.attributes;
        r7 = r7.id;
        r14.setId(r7);
        r13 = 1;
        r7 = 4;
        r14.setVisibility(r7);
        r11 = 0;
        r1.targetVisibility = r11;
        r11 = new android.widget.FrameLayout$LayoutParams;
        r7 = r17;
        r11.<init>(r15, r7, r9);
        if (r5 == 0) goto L_0x0094;
    L_0x0091:
        r11.leftMargin = r6;
        goto L_0x0096;
    L_0x0094:
        r11.rightMargin = r6;
    L_0x0096:
        r0.addView(r14, r11);
        r19.updateColorViewTranslations();
        goto L_0x010c;
    L_0x009e:
        r7 = r17;
        goto L_0x010c;
    L_0x00a2:
        r7 = r17;
        if (r12 == 0) goto L_0x00a8;
    L_0x00a6:
        r11 = 0;
        goto L_0x00a9;
    L_0x00a8:
        r11 = 4;
    L_0x00a9:
        r6 = r1.targetVisibility;
        if (r6 == r11) goto L_0x00b0;
    L_0x00ad:
        r16 = 1;
        goto L_0x00b2;
    L_0x00b0:
        r16 = 0;
    L_0x00b2:
        r13 = r16;
        r1.targetVisibility = r11;
        r6 = r14.getLayoutParams();
        r6 = (android.widget.FrameLayout.LayoutParams) r6;
        if (r5 == 0) goto L_0x00c1;
    L_0x00be:
        r16 = 0;
        goto L_0x00c3;
    L_0x00c1:
        r16 = r27;
    L_0x00c3:
        r17 = r16;
        if (r5 == 0) goto L_0x00ca;
    L_0x00c7:
        r16 = r27;
        goto L_0x00cc;
    L_0x00ca:
        r16 = 0;
    L_0x00cc:
        r18 = r16;
        r10 = r6.height;
        if (r10 != r7) goto L_0x00f0;
    L_0x00d2:
        r10 = r6.width;
        if (r10 != r15) goto L_0x00f0;
    L_0x00d6:
        r10 = r6.gravity;
        if (r10 != r9) goto L_0x00f0;
    L_0x00da:
        r10 = r6.rightMargin;
        r16 = r11;
        r11 = r17;
        if (r10 != r11) goto L_0x00eb;
    L_0x00e2:
        r10 = r6.leftMargin;
        r17 = r13;
        r13 = r18;
        if (r10 == r13) goto L_0x0105;
    L_0x00ea:
        goto L_0x00f8;
    L_0x00eb:
        r17 = r13;
        r13 = r18;
        goto L_0x00f8;
    L_0x00f0:
        r16 = r11;
        r11 = r17;
        r17 = r13;
        r13 = r18;
    L_0x00f8:
        r6.height = r7;
        r6.width = r15;
        r6.gravity = r9;
        r6.rightMargin = r11;
        r6.leftMargin = r13;
        r14.setLayoutParams(r6);
    L_0x0105:
        if (r12 == 0) goto L_0x010a;
    L_0x0107:
        setColor(r14, r2, r3, r4, r5);
    L_0x010a:
        r13 = r17;
    L_0x010c:
        if (r13 == 0) goto L_0x016d;
    L_0x010e:
        r6 = r14.animate();
        r6.cancel();
        r6 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        if (r28 == 0) goto L_0x0162;
    L_0x0119:
        r10 = r19.isResizing();
        if (r10 != 0) goto L_0x0162;
    L_0x011f:
        r10 = 0;
        if (r12 == 0) goto L_0x0144;
    L_0x0122:
        r11 = r14.getVisibility();
        if (r11 == 0) goto L_0x012f;
    L_0x0128:
        r11 = 0;
        r14.setVisibility(r11);
        r14.setAlpha(r10);
    L_0x012f:
        r10 = r14.animate();
        r6 = r10.alpha(r6);
        r10 = r0.mShowInterpolator;
        r6 = r6.setInterpolator(r10);
        r10 = r0.mBarEnterExitDuration;
        r10 = (long) r10;
        r6.setDuration(r10);
        goto L_0x016d;
    L_0x0144:
        r6 = r14.animate();
        r6 = r6.alpha(r10);
        r10 = r0.mHideInterpolator;
        r6 = r6.setInterpolator(r10);
        r10 = r0.mBarEnterExitDuration;
        r10 = (long) r10;
        r6 = r6.setDuration(r10);
        r10 = new com.android.internal.policy.DecorView$3;
        r10.<init>(r1);
        r6.withEndAction(r10);
        goto L_0x016d;
    L_0x0162:
        r11 = 0;
        r14.setAlpha(r6);
        if (r12 == 0) goto L_0x0169;
    L_0x0168:
        goto L_0x016a;
    L_0x0169:
        r11 = 4;
    L_0x016a:
        r14.setVisibility(r11);
    L_0x016d:
        r1.visible = r8;
        r1.color = r2;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.policy.DecorView.updateColorViewInt(com.android.internal.policy.DecorView$ColorViewState, int, int, int, int, boolean, boolean, int, boolean, boolean):void");
    }

    private static void setColor(View v, int color, int dividerColor, boolean verticalBar, boolean seascape) {
        if (dividerColor != 0) {
            Pair<Boolean, Boolean> dir = (Pair) v.getTag();
            if (dir != null && ((Boolean) dir.first).booleanValue() == verticalBar && ((Boolean) dir.second).booleanValue() == seascape) {
                LayerDrawable d = (LayerDrawable) v.getBackground();
                ((ColorDrawable) ((InsetDrawable) d.getDrawable(1)).getDrawable()).setColor(color);
                ((ColorDrawable) d.getDrawable(0)).setColor(dividerColor);
                return;
            }
            int size = Math.round(TypedValue.applyDimension(1, 1.0f, v.getContext().getResources().getDisplayMetrics()));
            Drawable colorDrawable = new ColorDrawable(color);
            int i = (!verticalBar || seascape) ? 0 : size;
            int i2 = !verticalBar ? size : 0;
            int i3 = (verticalBar && seascape) ? size : 0;
            InsetDrawable d2 = new InsetDrawable(colorDrawable, i, i2, i3, 0);
            v.setBackground(new LayerDrawable(new Drawable[]{new ColorDrawable(dividerColor), d2}));
            v.setTag(new Pair(Boolean.valueOf(verticalBar), Boolean.valueOf(seascape)));
            return;
        }
        v.setTag(null);
        v.setBackgroundColor(color);
    }

    private void updateColorViewTranslations() {
        int rootScrollY = this.mRootScrollY;
        float f = 0.0f;
        if (this.mStatusColorViewState.view != null) {
            this.mStatusColorViewState.view.setTranslationY(rootScrollY > 0 ? (float) rootScrollY : 0.0f);
        }
        if (this.mNavigationColorViewState.view != null) {
            View view = this.mNavigationColorViewState.view;
            if (rootScrollY < 0) {
                f = (float) rootScrollY;
            }
            view.setTranslationY(f);
        }
    }

    private WindowInsets updateStatusGuard(WindowInsets insets) {
        boolean showStatusGuard;
        int showStatusGuard2;
        WindowInsets insets2 = insets;
        ActionBarContextView actionBarContextView = this.mPrimaryActionModeView;
        if (actionBarContextView == null) {
            showStatusGuard = false;
            showStatusGuard2 = 0;
        } else if (actionBarContextView.getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams mlp = (MarginLayoutParams) this.mPrimaryActionModeView.getLayoutParams();
            boolean mlpChanged = false;
            if (this.mPrimaryActionModeView.isShown()) {
                boolean showStatusGuard3;
                if (this.mTempRect == null) {
                    this.mTempRect = new Rect();
                }
                WindowInsets innerInsets = this.mWindow.mContentParent.computeSystemWindowInsets(insets2, this.mTempRect);
                int newTopMargin = innerInsets.getSystemWindowInsetTop();
                int newLeftMargin = innerInsets.getSystemWindowInsetLeft();
                int newRightMargin = innerInsets.getSystemWindowInsetRight();
                WindowInsets rootInsets = getRootWindowInsets();
                int newGuardLeftMargin = rootInsets.getSystemWindowInsetLeft();
                int newGuardRightMargin = rootInsets.getSystemWindowInsetRight();
                if (!(mlp.topMargin == newTopMargin && mlp.leftMargin == newLeftMargin && mlp.rightMargin == newRightMargin)) {
                    mlpChanged = true;
                    mlp.topMargin = newTopMargin;
                    mlp.leftMargin = newLeftMargin;
                    mlp.rightMargin = newRightMargin;
                }
                if (newTopMargin <= 0 || this.mStatusGuard != null) {
                    showStatusGuard = false;
                    View view = this.mStatusGuard;
                    if (view != null) {
                        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) view.getLayoutParams();
                        if (!(lp.height == mlp.topMargin && lp.leftMargin == newGuardLeftMargin && lp.rightMargin == newGuardRightMargin)) {
                            lp.height = mlp.topMargin;
                            lp.leftMargin = newGuardLeftMargin;
                            lp.rightMargin = newGuardRightMargin;
                            this.mStatusGuard.setLayoutParams(lp);
                        }
                    }
                } else {
                    this.mStatusGuard = new View(this.mContext);
                    this.mStatusGuard.setVisibility(8);
                    showStatusGuard = false;
                    showStatusGuard3 = new FrameLayout.LayoutParams(-1, mlp.topMargin, true);
                    showStatusGuard3.leftMargin = newGuardLeftMargin;
                    showStatusGuard3.rightMargin = newGuardRightMargin;
                    addView(this.mStatusGuard, indexOfChild(this.mStatusColorViewState.view), (ViewGroup.LayoutParams) showStatusGuard3);
                }
                boolean z = true;
                showStatusGuard3 = this.mStatusGuard != null;
                if (showStatusGuard3 && this.mStatusGuard.getVisibility() != 0) {
                    updateStatusGuardColor();
                }
                if ((this.mWindow.getLocalFeaturesPrivate() & 1024) != 0) {
                    z = false;
                }
                if (z && showStatusGuard3) {
                    insets2 = insets2.inset(0, insets.getSystemWindowInsetTop(), 0, 0);
                }
                showStatusGuard = showStatusGuard3;
                showStatusGuard2 = 0;
            } else {
                showStatusGuard = false;
                if (mlp.topMargin == 0 && mlp.leftMargin == 0 && mlp.rightMargin == 0) {
                    showStatusGuard2 = 0;
                } else {
                    mlpChanged = true;
                    showStatusGuard2 = 0;
                    mlp.topMargin = 0;
                }
            }
            if (mlpChanged) {
                this.mPrimaryActionModeView.setLayoutParams(mlp);
            }
        } else {
            showStatusGuard = false;
            showStatusGuard2 = 0;
        }
        View view2 = this.mStatusGuard;
        if (view2 != null) {
            if (!showStatusGuard) {
                showStatusGuard2 = 8;
            }
            view2.setVisibility(showStatusGuard2);
        }
        return insets2;
    }

    private void updateStatusGuardColor() {
        int color;
        boolean lightStatusBar = (getWindowSystemUiVisibility() & 8192) != 0;
        View view = this.mStatusGuard;
        if (lightStatusBar) {
            color = this.mContext.getColor(R.color.decor_view_status_guard_light);
        } else {
            color = this.mContext.getColor(R.color.decor_view_status_guard);
        }
        view.setBackgroundColor(color);
    }

    public void updatePictureInPictureOutlineProvider(boolean isInPictureInPictureMode) {
        if (this.mIsInPictureInPictureMode != isInPictureInPictureMode) {
            if (isInPictureInPictureMode) {
                WindowControllerCallback callback = this.mWindow.getWindowControllerCallback();
                if (callback != null && callback.isTaskRoot()) {
                    super.setOutlineProvider(PIP_OUTLINE_PROVIDER);
                }
            } else {
                ViewOutlineProvider outlineProvider = getOutlineProvider();
                ViewOutlineProvider viewOutlineProvider = this.mLastOutlineProvider;
                if (outlineProvider != viewOutlineProvider) {
                    setOutlineProvider(viewOutlineProvider);
                }
            }
            this.mIsInPictureInPictureMode = isInPictureInPictureMode;
        }
    }

    public void setOutlineProvider(ViewOutlineProvider provider) {
        super.setOutlineProvider(provider);
        this.mLastOutlineProvider = provider;
    }

    private void drawableChanged() {
        if (!this.mChanging) {
            Rect framePadding = this.mFramePadding;
            if (framePadding == null) {
                framePadding = new Rect();
            }
            Rect backgroundPadding = this.mBackgroundPadding;
            if (backgroundPadding == null) {
                backgroundPadding = new Rect();
            }
            setPadding(framePadding.left + backgroundPadding.left, framePadding.top + backgroundPadding.top, framePadding.right + backgroundPadding.right, framePadding.bottom + backgroundPadding.bottom);
            requestLayout();
            invalidate();
            int opacity = -1;
            if (getResources().getConfiguration().windowConfiguration.hasWindowShadow()) {
                opacity = -3;
            } else {
                Drawable bg = getBackground();
                Drawable fg = getForeground();
                if (bg != null) {
                    if (fg == null) {
                        opacity = bg.getOpacity();
                    } else if (framePadding.left > 0 || framePadding.top > 0 || framePadding.right > 0 || framePadding.bottom > 0) {
                        opacity = -3;
                    } else {
                        int fop = fg.getOpacity();
                        int bop = bg.getOpacity();
                        if (fop == -1 || bop == -1) {
                            opacity = -1;
                        } else if (fop == 0) {
                            opacity = bop;
                        } else if (bop == 0) {
                            opacity = fop;
                        } else {
                            opacity = Drawable.resolveOpacity(fop, bop);
                        }
                    }
                }
            }
            this.mDefaultOpacity = opacity;
            if (this.mFeatureId < 0) {
                this.mWindow.setDefaultWindowFormat(opacity);
            }
        }
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!(!this.mWindow.hasFeature(0) || hasWindowFocus || this.mWindow.mPanelChordingKey == 0)) {
            this.mWindow.closePanel(0);
        }
        Window.Callback cb = this.mWindow.getCallback();
        if (!(cb == null || this.mWindow.isDestroyed() || this.mFeatureId >= 0)) {
            cb.onWindowFocusChanged(hasWindowFocus);
        }
        ActionMode actionMode = this.mPrimaryActionMode;
        if (actionMode != null) {
            actionMode.onWindowFocusChanged(hasWindowFocus);
        }
        actionMode = this.mFloatingActionMode;
        if (actionMode != null) {
            actionMode.onWindowFocusChanged(hasWindowFocus);
        }
        updateElevation();
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window.Callback cb = this.mWindow.getCallback();
        if (!(cb == null || this.mWindow.isDestroyed() || this.mFeatureId >= 0)) {
            cb.onAttachedToWindow();
        }
        if (this.mFeatureId == -1) {
            this.mWindow.openPanelsAfterRestore();
        }
        if (this.mWindowResizeCallbacksAdded) {
            BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
            if (backdropFrameRenderer != null) {
                backdropFrameRenderer.onConfigurationChange();
            }
        } else {
            getViewRootImpl().addWindowCallbacks(this);
            this.mWindowResizeCallbacksAdded = true;
        }
        this.mWindow.onViewRootImplSet(getViewRootImpl());
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Window.Callback cb = this.mWindow.getCallback();
        if (cb != null && this.mFeatureId < 0) {
            cb.onDetachedFromWindow();
        }
        if (this.mWindow.mDecorContentParent != null) {
            this.mWindow.mDecorContentParent.dismissPopups();
        }
        if (this.mPrimaryActionModePopup != null) {
            removeCallbacks(this.mShowPrimaryActionModePopup);
            if (this.mPrimaryActionModePopup.isShowing()) {
                this.mPrimaryActionModePopup.dismiss();
            }
            this.mPrimaryActionModePopup = null;
        }
        FloatingToolbar floatingToolbar = this.mFloatingToolbar;
        if (floatingToolbar != null) {
            floatingToolbar.dismiss();
            this.mFloatingToolbar = null;
        }
        PanelFeatureState st = this.mWindow.getPanelState(0, false);
        if (!(st == null || st.menu == null || this.mFeatureId >= 0)) {
            st.menu.close();
        }
        releaseThreadedRenderer();
        if (this.mWindowResizeCallbacksAdded) {
            getViewRootImpl().removeWindowCallbacks(this);
            this.mWindowResizeCallbacksAdded = false;
        }
    }

    public void onCloseSystemDialogs(String reason) {
        if (this.mFeatureId >= 0) {
            this.mWindow.closeAllPanels();
        }
    }

    public SurfaceHolder.Callback2 willYouTakeTheSurface() {
        return this.mFeatureId < 0 ? this.mWindow.mTakeSurfaceCallback : null;
    }

    public InputQueue.Callback willYouTakeTheInputQueue() {
        return this.mFeatureId < 0 ? this.mWindow.mTakeInputQueueCallback : null;
    }

    public void setSurfaceType(int type) {
        this.mWindow.setType(type);
    }

    public void setSurfaceFormat(int format) {
        this.mWindow.setFormat(format);
    }

    public void setSurfaceKeepScreenOn(boolean keepOn) {
        if (keepOn) {
            this.mWindow.addFlags(128);
        } else {
            this.mWindow.clearFlags(128);
        }
    }

    public void onRootViewScrollYChanged(int rootScrollY) {
        this.mRootScrollY = rootScrollY;
        DecorCaptionView decorCaptionView = this.mDecorCaptionView;
        if (decorCaptionView != null) {
            decorCaptionView.onRootViewScrollYChanged(rootScrollY);
        }
        updateColorViewTranslations();
    }

    private ActionMode createActionMode(int type, Callback2 callback, View originatingView) {
        if (type != 1) {
            return createStandaloneActionMode(callback);
        }
        return createFloatingActionMode(originatingView, callback);
    }

    private void setHandledActionMode(ActionMode mode) {
        if (mode.getType() == 0) {
            setHandledPrimaryActionMode(mode);
        } else if (mode.getType() == 1) {
            setHandledFloatingActionMode(mode);
        }
    }

    private ActionMode createStandaloneActionMode(ActionMode.Callback callback) {
        endOnGoingFadeAnimation();
        cleanupPrimaryActionMode();
        ActionBarContextView actionBarContextView = this.mPrimaryActionModeView;
        boolean z = false;
        if (actionBarContextView == null || !actionBarContextView.isAttachedToWindow()) {
            if (this.mWindow.isFloating()) {
                Context actionBarContext;
                TypedValue outValue = new TypedValue();
                Theme baseTheme = this.mContext.getTheme();
                baseTheme.resolveAttribute(16843825, outValue, true);
                if (outValue.resourceId != 0) {
                    Theme actionBarTheme = this.mContext.getResources().newTheme();
                    actionBarTheme.setTo(baseTheme);
                    actionBarTheme.applyStyle(outValue.resourceId, true);
                    actionBarContext = new ContextThemeWrapper(this.mContext, 0);
                    actionBarContext.getTheme().setTo(actionBarTheme);
                } else {
                    actionBarContext = this.mContext;
                }
                this.mPrimaryActionModeView = new ActionBarContextView(actionBarContext);
                this.mPrimaryActionModePopup = new PopupWindow(actionBarContext, null, (int) R.attr.actionModePopupWindowStyle);
                this.mPrimaryActionModePopup.setWindowLayoutType(2);
                this.mPrimaryActionModePopup.setContentView(this.mPrimaryActionModeView);
                this.mPrimaryActionModePopup.setWidth(-1);
                actionBarContext.getTheme().resolveAttribute(16843499, outValue, true);
                this.mPrimaryActionModeView.setContentHeight(TypedValue.complexToDimensionPixelSize(outValue.data, actionBarContext.getResources().getDisplayMetrics()));
                this.mPrimaryActionModePopup.setHeight(-2);
                this.mShowPrimaryActionModePopup = new Runnable() {
                    public void run() {
                        DecorView.this.mPrimaryActionModePopup.showAtLocation(DecorView.this.mPrimaryActionModeView.getApplicationWindowToken(), 55, 0, 0);
                        DecorView.this.endOnGoingFadeAnimation();
                        if (DecorView.this.shouldAnimatePrimaryActionModeView()) {
                            DecorView decorView = DecorView.this;
                            decorView.mFadeAnim = ObjectAnimator.ofFloat(decorView.mPrimaryActionModeView, View.ALPHA, 0.0f, 1.0f);
                            DecorView.this.mFadeAnim.addListener(new AnimatorListenerAdapter() {
                                public void onAnimationStart(Animator animation) {
                                    DecorView.this.mPrimaryActionModeView.setVisibility(0);
                                }

                                public void onAnimationEnd(Animator animation) {
                                    DecorView.this.mPrimaryActionModeView.setAlpha(1.0f);
                                    DecorView.this.mFadeAnim = null;
                                }
                            });
                            DecorView.this.mFadeAnim.start();
                            return;
                        }
                        DecorView.this.mPrimaryActionModeView.setAlpha(1.0f);
                        DecorView.this.mPrimaryActionModeView.setVisibility(0);
                    }
                };
            } else {
                ViewStub stub = (ViewStub) findViewById(R.id.action_mode_bar_stub);
                if (stub != null) {
                    this.mPrimaryActionModeView = (ActionBarContextView) stub.inflate();
                    this.mPrimaryActionModePopup = null;
                }
            }
        }
        actionBarContextView = this.mPrimaryActionModeView;
        if (actionBarContextView == null) {
            return null;
        }
        actionBarContextView.killMode();
        Context context = this.mPrimaryActionModeView.getContext();
        ActionBarContextView actionBarContextView2 = this.mPrimaryActionModeView;
        if (this.mPrimaryActionModePopup == null) {
            z = true;
        }
        return new StandaloneActionMode(context, actionBarContextView2, callback, z);
    }

    private void endOnGoingFadeAnimation() {
        ObjectAnimator objectAnimator = this.mFadeAnim;
        if (objectAnimator != null) {
            objectAnimator.end();
        }
    }

    private void setHandledPrimaryActionMode(ActionMode mode) {
        endOnGoingFadeAnimation();
        this.mPrimaryActionMode = mode;
        this.mPrimaryActionMode.invalidate();
        this.mPrimaryActionModeView.initForMode(this.mPrimaryActionMode);
        if (this.mPrimaryActionModePopup != null) {
            post(this.mShowPrimaryActionModePopup);
        } else if (shouldAnimatePrimaryActionModeView()) {
            this.mFadeAnim = ObjectAnimator.ofFloat(this.mPrimaryActionModeView, View.ALPHA, 0.0f, 1.0f);
            this.mFadeAnim.addListener(new AnimatorListenerAdapter() {
                public void onAnimationStart(Animator animation) {
                    DecorView.this.mPrimaryActionModeView.setVisibility(0);
                }

                public void onAnimationEnd(Animator animation) {
                    DecorView.this.mPrimaryActionModeView.setAlpha(1.0f);
                    DecorView.this.mFadeAnim = null;
                }
            });
            this.mFadeAnim.start();
        } else {
            this.mPrimaryActionModeView.setAlpha(1.0f);
            this.mPrimaryActionModeView.setVisibility(0);
        }
        this.mPrimaryActionModeView.sendAccessibilityEvent(32);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean shouldAnimatePrimaryActionModeView() {
        return isLaidOut();
    }

    private ActionMode createFloatingActionMode(View originatingView, Callback2 callback) {
        ActionMode actionMode = this.mFloatingActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        cleanupFloatingActionModeViews();
        this.mFloatingToolbar = new FloatingToolbar(this.mWindow);
        final FloatingActionMode mode = new FloatingActionMode(this.mContext, callback, originatingView, this.mFloatingToolbar);
        this.mFloatingActionModeOriginatingView = originatingView;
        this.mFloatingToolbarPreDrawListener = new OnPreDrawListener() {
            public boolean onPreDraw() {
                mode.updateViewLocationInWindow();
                return true;
            }
        };
        return mode;
    }

    private void setHandledFloatingActionMode(ActionMode mode) {
        this.mFloatingActionMode = mode;
        this.mFloatingActionMode.invalidate();
        this.mFloatingActionModeOriginatingView.getViewTreeObserver().addOnPreDrawListener(this.mFloatingToolbarPreDrawListener);
    }

    /* Access modifiers changed, original: 0000 */
    public void enableCaption(boolean attachedAndVisible) {
        if (this.mHasCaption != attachedAndVisible) {
            this.mHasCaption = attachedAndVisible;
            if (getForeground() != null) {
                drawableChanged();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setWindow(PhoneWindow phoneWindow) {
        this.mWindow = phoneWindow;
        Context context = getContext();
        if (context instanceof DecorContext) {
            ((DecorContext) context).setPhoneWindow(this.mWindow);
        }
        this.mFirst = true;
    }

    public Resources getResources() {
        Resources resources = getContext().getResources();
        MiuiMultiWindowUtils.updatewindowConfiguration(this, resources);
        return resources;
    }

    /* Access modifiers changed, original: protected */
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateDecorCaptionStatus(newConfig);
        updateAvailableWidth();
        initializeElevation();
    }

    public void onMovedToDisplay(int displayId, Configuration config) {
        super.onMovedToDisplay(displayId, config);
        getContext().updateDisplay(displayId);
    }

    private boolean isFillingScreen(Configuration config) {
        if (!(config.windowConfiguration.getWindowingMode() == 1) || ((getWindowSystemUiVisibility() | getSystemUiVisibility()) & 4) == 0) {
            return false;
        }
        return true;
    }

    private void updateDecorCaptionStatus(Configuration config) {
        boolean displayWindowDecor = config.windowConfiguration.hasWindowDecorCaption() && !isFillingScreen(config);
        if (this.mDecorCaptionView == null && displayWindowDecor) {
            this.mDecorCaptionView = createDecorCaptionView(this.mWindow.getLayoutInflater());
            DecorCaptionView decorCaptionView = this.mDecorCaptionView;
            if (decorCaptionView != null) {
                if (decorCaptionView.getParent() == null) {
                    addView((View) this.mDecorCaptionView, 0, new ViewGroup.LayoutParams(-1, -1));
                }
                while (this.mContentRoot.getParent() != null && (this.mContentRoot.getParent() instanceof ViewGroup) && !(this.mContentRoot.getParent() instanceof DecorView)) {
                    this.mContentRoot = (ViewGroup) this.mContentRoot.getParent();
                }
                this.mDecorCaptionView.setZ(999.0f);
                removeView(this.mContentRoot);
                this.mDecorCaptionView.addView((View) this.mContentRoot, (ViewGroup.LayoutParams) new MarginLayoutParams(-1, -1));
                return;
            }
            return;
        }
        DecorCaptionView decorCaptionView2 = this.mDecorCaptionView;
        if (decorCaptionView2 != null) {
            decorCaptionView2.onConfigurationChanged(displayWindowDecor);
            enableCaption(displayWindowDecor);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void onResourcesLoaded(LayoutInflater inflater, int layoutResource) {
        if (this.mBackdropFrameRenderer != null) {
            loadBackgroundDrawablesIfNeeded();
            this.mBackdropFrameRenderer.onResourcesLoaded(this, this.mResizingBackgroundDrawable, this.mCaptionBackgroundDrawable, this.mUserCaptionBackgroundDrawable, getCurrentColor(this.mStatusColorViewState), getCurrentColor(this.mNavigationColorViewState));
        }
        this.mDecorCaptionView = createDecorCaptionView(inflater);
        View root = inflater.inflate(layoutResource, (ViewGroup) null);
        DecorCaptionView decorCaptionView = this.mDecorCaptionView;
        if (decorCaptionView != null) {
            if (decorCaptionView.getParent() == null) {
                addView((View) this.mDecorCaptionView, new ViewGroup.LayoutParams(-1, -1));
                this.mDecorCaptionView.setZ(999.0f);
            }
            this.mDecorCaptionView.addView(root, (ViewGroup.LayoutParams) new MarginLayoutParams(-1, -1));
        } else {
            addView(root, 0, new ViewGroup.LayoutParams(-1, -1));
        }
        this.mContentRoot = (ViewGroup) root;
        initializeElevation();
    }

    private void loadBackgroundDrawablesIfNeeded() {
        Drawable drawable;
        if (this.mResizingBackgroundDrawable == null) {
            drawable = this.mWindow.mBackgroundDrawable;
            Drawable drawable2 = this.mWindow.mBackgroundFallbackDrawable;
            boolean z = this.mWindow.isTranslucent() || this.mWindow.isShowingWallpaper();
            this.mResizingBackgroundDrawable = getResizingBackgroundDrawable(drawable, drawable2, z);
            if (this.mResizingBackgroundDrawable == null) {
                String str = this.mLogTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to find background drawable for PhoneWindow=");
                stringBuilder.append(this.mWindow);
                Log.w(str, stringBuilder.toString());
            }
        }
        if (this.mCaptionBackgroundDrawable == null) {
            this.mCaptionBackgroundDrawable = getContext().getDrawable(R.drawable.decor_caption_title_focused);
        }
        drawable = this.mResizingBackgroundDrawable;
        if (drawable != null) {
            this.mLastBackgroundDrawableCb = drawable.getCallback();
            this.mResizingBackgroundDrawable.setCallback(null);
        }
    }

    private DecorCaptionView createDecorCaptionView(LayoutInflater inflater) {
        DecorCaptionView decorCaptionView = null;
        boolean z = true;
        for (int i = getChildCount() - 1; i >= 0 && decorCaptionView == null; i--) {
            View view = getChildAt(i);
            if (view instanceof DecorCaptionView) {
                decorCaptionView = (DecorCaptionView) view;
                removeViewAt(i);
            }
        }
        LayoutParams attrs = this.mWindow.getAttributes();
        boolean isApplication = attrs.type == 1 || attrs.type == 2 || attrs.type == 4;
        WindowConfiguration winConfig = getResources().getConfiguration().windowConfiguration;
        if (!this.mWindow.isFloating() && isApplication && winConfig.hasWindowDecorCaption()) {
            if (decorCaptionView == null) {
                decorCaptionView = inflateDecorCaptionView(inflater);
            }
            decorCaptionView.setPhoneWindow(this.mWindow, true);
        } else {
            decorCaptionView = null;
        }
        if (decorCaptionView == null) {
            z = false;
        }
        enableCaption(z);
        return decorCaptionView;
    }

    private DecorCaptionView inflateDecorCaptionView(LayoutInflater inflater) {
        Context context = getContext();
        DecorCaptionView view = (DecorCaptionView) LayoutInflater.from(context).inflate((int) R.layout.decor_caption, null);
        setDecorCaptionShade(context, view);
        return view;
    }

    private void setDecorCaptionShade(Context context, DecorCaptionView view) {
        int shade = this.mWindow.getDecorCaptionShade();
        if (shade == 1) {
            setLightDecorCaptionShade(view);
        } else if (shade != 2) {
            TypedValue value = new TypedValue();
            context.getTheme().resolveAttribute(16843827, value, true);
            if (((double) Color.luminance(value.data)) < 0.5d) {
                setLightDecorCaptionShade(view);
            } else {
                setDarkDecorCaptionShade(view);
            }
        } else {
            setDarkDecorCaptionShade(view);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void updateDecorCaptionShade() {
        if (this.mDecorCaptionView != null) {
            setDecorCaptionShade(getContext(), this.mDecorCaptionView);
        }
    }

    private void setLightDecorCaptionShade(DecorCaptionView view) {
        view.findViewById(R.id.maximize_window).setBackgroundResource(R.drawable.decor_maximize_button_light);
        view.findViewById(R.id.close_window).setBackgroundResource(R.drawable.decor_close_button_light);
    }

    private void setDarkDecorCaptionShade(DecorCaptionView view) {
        view.findViewById(R.id.maximize_window).setBackgroundResource(R.drawable.decor_maximize_button_dark);
        view.findViewById(R.id.close_window).setBackgroundResource(R.drawable.decor_close_button_dark);
    }

    public static Drawable getResizingBackgroundDrawable(Drawable backgroundDrawable, Drawable fallbackDrawable, boolean windowTranslucent) {
        if (backgroundDrawable != null) {
            return enforceNonTranslucentBackground(backgroundDrawable, windowTranslucent);
        }
        if (fallbackDrawable != null) {
            return enforceNonTranslucentBackground(fallbackDrawable, windowTranslucent);
        }
        return new ColorDrawable(-16777216);
    }

    private static Drawable enforceNonTranslucentBackground(Drawable drawable, boolean windowTranslucent) {
        if (!windowTranslucent && (drawable instanceof ColorDrawable)) {
            ColorDrawable colorDrawable = (ColorDrawable) drawable;
            int color = colorDrawable.getColor();
            if (Color.alpha(color) != 255) {
                ColorDrawable copy = (ColorDrawable) colorDrawable.getConstantState().newDrawable().mutate();
                copy.setColor(Color.argb(255, Color.red(color), Color.green(color), Color.blue(color)));
                return copy;
            }
        }
        return drawable;
    }

    /* Access modifiers changed, original: 0000 */
    public void clearContentView() {
        DecorCaptionView decorCaptionView = this.mDecorCaptionView;
        if (decorCaptionView != null) {
            decorCaptionView.removeContentView();
            return;
        }
        for (int i = getChildCount() - 1; i >= 0; i--) {
            View v = getChildAt(i);
            if (!(v == this.mStatusColorViewState.view || v == this.mNavigationColorViewState.view || v == this.mStatusGuard)) {
                removeViewAt(i);
            }
        }
    }

    public void onWindowSizeIsChanging(Rect newBounds, boolean fullscreen, Rect systemInsets, Rect stableInsets) {
        BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
        if (backdropFrameRenderer != null) {
            backdropFrameRenderer.setTargetRect(newBounds, fullscreen, systemInsets, stableInsets);
        }
    }

    public void onWindowDragResizeStart(Rect initialBounds, boolean fullscreen, Rect systemInsets, Rect stableInsets, int resizeMode) {
        if (this.mWindow.isDestroyed()) {
            releaseThreadedRenderer();
        } else if (this.mBackdropFrameRenderer == null) {
            ThreadedRenderer renderer = getThreadedRenderer();
            if (renderer != null) {
                loadBackgroundDrawablesIfNeeded();
                this.mBackdropFrameRenderer = new BackdropFrameRenderer(this, renderer, initialBounds, this.mResizingBackgroundDrawable, this.mCaptionBackgroundDrawable, this.mUserCaptionBackgroundDrawable, getCurrentColor(this.mStatusColorViewState), getCurrentColor(this.mNavigationColorViewState), fullscreen, systemInsets, stableInsets);
                updateElevation();
                updateColorViews(null, false);
            }
            this.mResizeMode = resizeMode;
            getViewRootImpl().requestInvalidateRootRenderNode();
        }
    }

    public void onWindowDragResizeEnd() {
        releaseThreadedRenderer();
        updateColorViews(null, false);
        this.mResizeMode = -1;
        getViewRootImpl().requestInvalidateRootRenderNode();
    }

    public boolean onContentDrawn(int offsetX, int offsetY, int sizeX, int sizeY) {
        BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
        if (backdropFrameRenderer == null) {
            return false;
        }
        return backdropFrameRenderer.onContentDrawn(offsetX, offsetY, sizeX, sizeY);
    }

    public void onRequestDraw(boolean reportNextDraw) {
        BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
        if (backdropFrameRenderer != null) {
            backdropFrameRenderer.onRequestDraw(reportNextDraw);
        } else if (reportNextDraw && isAttachedToWindow()) {
            getViewRootImpl().reportDrawFinish();
        }
    }

    public void onPostDraw(RecordingCanvas canvas) {
        drawResizingShadowIfNeeded(canvas);
        drawLegacyNavigationBarBackground(canvas);
    }

    private void initResizingPaints() {
        int middleColor = (this.mContext.getResources().getColor(R.color.resize_shadow_start_color, null) + this.mContext.getResources().getColor(R.color.resize_shadow_end_color, null)) / 2;
        this.mHorizontalResizeShadowPaint.setShader(new LinearGradient(0.0f, 0.0f, 0.0f, (float) this.mResizeShadowSize, new int[]{startColor, middleColor, endColor}, new float[]{0.0f, 0.3f, 1.0f}, TileMode.CLAMP));
        this.mVerticalResizeShadowPaint.setShader(new LinearGradient(0.0f, 0.0f, (float) this.mResizeShadowSize, 0.0f, new int[]{startColor, middleColor, endColor}, new float[]{0.0f, 0.3f, 1.0f}, TileMode.CLAMP));
    }

    private void drawResizingShadowIfNeeded(RecordingCanvas canvas) {
        if (this.mResizeMode == 1 && !this.mWindow.mIsFloating && !this.mWindow.isTranslucent() && !this.mWindow.isShowingWallpaper()) {
            canvas.save();
            canvas.translate(0.0f, (float) (getHeight() - this.mFrameOffsets.bottom));
            canvas.drawRect(0.0f, 0.0f, (float) getWidth(), (float) this.mResizeShadowSize, this.mHorizontalResizeShadowPaint);
            canvas.restore();
            canvas.save();
            canvas.translate((float) (getWidth() - this.mFrameOffsets.right), 0.0f);
            canvas.drawRect(0.0f, 0.0f, (float) this.mResizeShadowSize, (float) getHeight(), this.mVerticalResizeShadowPaint);
            canvas.restore();
        }
    }

    private void drawLegacyNavigationBarBackground(RecordingCanvas canvas) {
        if (this.mDrawLegacyNavigationBarBackground) {
            View v = this.mNavigationColorViewState.view;
            if (v != null) {
                canvas.drawRect((float) v.getLeft(), (float) v.getTop(), (float) v.getRight(), (float) v.getBottom(), this.mLegacyNavigationBarBackgroundPaint);
            }
        }
    }

    private void releaseThreadedRenderer() {
        Drawable drawable = this.mResizingBackgroundDrawable;
        if (drawable != null) {
            Callback callback = this.mLastBackgroundDrawableCb;
            if (callback != null) {
                drawable.setCallback(callback);
                this.mLastBackgroundDrawableCb = null;
            }
        }
        BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
        if (backdropFrameRenderer != null) {
            backdropFrameRenderer.releaseRenderer();
            this.mBackdropFrameRenderer = null;
            updateElevation();
        }
    }

    private boolean isResizing() {
        return this.mBackdropFrameRenderer != null;
    }

    private void initializeElevation() {
        this.mAllowUpdateElevation = false;
        updateElevation();
    }

    private void updateElevation() {
        float elevation = 0.0f;
        boolean wasAdjustedForStack = this.mElevationAdjustedForStack;
        int windowingMode = getResources().getConfiguration().windowConfiguration.getWindowingMode();
        float f = 5.0f;
        if (windowingMode == 5 && !isResizing()) {
            if (hasWindowFocus()) {
                f = 20.0f;
            }
            elevation = f;
            if (!this.mAllowUpdateElevation) {
                elevation = 20.0f;
            }
            elevation = dipToPx(elevation);
            this.mElevationAdjustedForStack = true;
        } else if (windowingMode == 2) {
            elevation = dipToPx(5.0f);
            this.mElevationAdjustedForStack = true;
        } else {
            this.mElevationAdjustedForStack = false;
        }
        if ((!wasAdjustedForStack && !this.mElevationAdjustedForStack) || getElevation() == elevation) {
            return;
        }
        if (isResizing()) {
            setElevation(elevation);
        } else {
            this.mWindow.setElevation(elevation);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isShowingCaption() {
        DecorCaptionView decorCaptionView = this.mDecorCaptionView;
        return decorCaptionView != null && decorCaptionView.isCaptionShowing();
    }

    /* Access modifiers changed, original: 0000 */
    public int getCaptionHeight() {
        return isShowingCaption() ? this.mDecorCaptionView.getCaptionHeight() : 0;
    }

    private float dipToPx(float dip) {
        return TypedValue.applyDimension(1, dip, getResources().getDisplayMetrics());
    }

    /* Access modifiers changed, original: 0000 */
    public void setUserCaptionBackgroundDrawable(Drawable drawable) {
        this.mUserCaptionBackgroundDrawable = drawable;
        BackdropFrameRenderer backdropFrameRenderer = this.mBackdropFrameRenderer;
        if (backdropFrameRenderer != null) {
            backdropFrameRenderer.setUserCaptionBackgroundDrawable(drawable);
        }
    }

    private static String getTitleSuffix(LayoutParams params) {
        String str = "";
        if (params == null) {
            return str;
        }
        String[] split = params.getTitle().toString().split("\\.");
        if (split.length > 0) {
            return split[split.length - 1];
        }
        return str;
    }

    /* Access modifiers changed, original: 0000 */
    public void updateLogTag(LayoutParams params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DecorView[");
        stringBuilder.append(getTitleSuffix(params));
        stringBuilder.append("]");
        this.mLogTag = stringBuilder.toString();
    }

    private void updateAvailableWidth() {
        Resources res = getResources();
        this.mAvailableWidth = TypedValue.applyDimension(1, (float) res.getConfiguration().screenWidthDp, res.getDisplayMetrics());
    }

    public void requestKeyboardShortcuts(List<KeyboardShortcutGroup> list, int deviceId) {
        PanelFeatureState st = this.mWindow.getPanelState(0, false);
        Menu menu = st != null ? st.menu : null;
        if (!this.mWindow.isDestroyed() && this.mWindow.getCallback() != null) {
            this.mWindow.getCallback().onProvideKeyboardShortcuts(list, menu, deviceId);
        }
    }

    public void dispatchPointerCaptureChanged(boolean hasCapture) {
        super.dispatchPointerCaptureChanged(hasCapture);
        if (!this.mWindow.isDestroyed() && this.mWindow.getCallback() != null) {
            this.mWindow.getCallback().onPointerCaptureChanged(hasCapture);
        }
    }

    public int getAccessibilityViewId() {
        return 2147483646;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("DecorView@");
        stringBuilder.append(Integer.toHexString(hashCode()));
        stringBuilder.append("[");
        stringBuilder.append(getTitleSuffix(this.mWindow.getAttributes()));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public Context getWindowContext() {
        PhoneWindow phoneWindow = this.mWindow;
        return phoneWindow != null ? phoneWindow.getContext() : null;
    }

    public Activity getAttachedActivity() {
        if (getContext() instanceof DecorContext) {
            Context context = getWindowContext();
            if (context != null && (context instanceof Activity)) {
                return (Activity) context;
            }
        }
        return super.getAttachedActivity();
    }
}
