package android.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.IBinder;
import android.transition.Transition;
import android.transition.Transition.EpicenterCallback;
import android.transition.Transition.TransitionListener;
import android.transition.TransitionInflater;
import android.transition.TransitionListenerAdapter;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.KeyEvent.DispatcherState;
import android.view.KeyboardShortcutGroup;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnLayoutChangeListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerGlobal;
import com.android.internal.R;
import com.miui.internal.variable.api.v29.Android_Widget_PopupWindow.Extension;
import com.miui.internal.variable.api.v29.Android_Widget_PopupWindow.Interface;
import java.lang.ref.WeakReference;
import java.util.List;

public class PopupWindow {
    private static final int[] ABOVE_ANCHOR_STATE_SET = new int[]{16842922};
    private static final int ANIMATION_STYLE_DEFAULT = -1;
    private static final int DEFAULT_ANCHORED_GRAVITY = 8388659;
    public static final int INPUT_METHOD_FROM_FOCUSABLE = 0;
    public static final int INPUT_METHOD_NEEDED = 1;
    public static final int INPUT_METHOD_NOT_NEEDED = 2;
    @UnsupportedAppUsage
    private boolean mAboveAnchor;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Drawable mAboveAnchorBackgroundDrawable;
    private boolean mAllowScrollingAnchorParent;
    @UnsupportedAppUsage
    private WeakReference<View> mAnchor;
    private WeakReference<View> mAnchorRoot;
    private int mAnchorXoff;
    private int mAnchorYoff;
    private int mAnchoredGravity;
    @UnsupportedAppUsage
    private int mAnimationStyle;
    private boolean mAttachedInDecor;
    private boolean mAttachedInDecorSet;
    private Drawable mBackground;
    @UnsupportedAppUsage
    private View mBackgroundView;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private Drawable mBelowAnchorBackgroundDrawable;
    private boolean mClipToScreen;
    private boolean mClippingEnabled;
    @UnsupportedAppUsage
    private View mContentView;
    @UnsupportedAppUsage
    private Context mContext;
    @UnsupportedAppUsage
    private PopupDecorView mDecorView;
    private float mElevation;
    private Transition mEnterTransition;
    private Rect mEpicenterBounds;
    private Transition mExitTransition;
    private boolean mFocusable;
    private int mGravity;
    private int mHeight;
    @UnsupportedAppUsage
    private int mHeightMode;
    private boolean mIgnoreCheekPress;
    private int mInputMethodMode;
    private boolean mIsAnchorRootAttached;
    @UnsupportedAppUsage
    private boolean mIsDropdown;
    @UnsupportedAppUsage
    private boolean mIsShowing;
    private boolean mIsTransitioningToDismiss;
    @UnsupportedAppUsage
    private int mLastHeight;
    @UnsupportedAppUsage
    private int mLastWidth;
    @UnsupportedAppUsage
    private boolean mLayoutInScreen;
    private boolean mLayoutInsetDecor;
    @UnsupportedAppUsage
    private boolean mNotTouchModal;
    private final OnAttachStateChangeListener mOnAnchorDetachedListener;
    private final OnAttachStateChangeListener mOnAnchorRootDetachedListener;
    @UnsupportedAppUsage
    private OnDismissListener mOnDismissListener;
    private final OnLayoutChangeListener mOnLayoutChangeListener;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private final OnScrollChangedListener mOnScrollChangedListener;
    private boolean mOutsideTouchable;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private boolean mOverlapAnchor;
    private WeakReference<View> mParentRootView;
    private boolean mPopupViewInitialLayoutDirectionInherited;
    private int mSoftInputMode;
    private int mSplitTouchEnabled;
    private final Rect mTempRect;
    private final int[] mTmpAppLocation;
    private final int[] mTmpDrawingLocation;
    private final int[] mTmpScreenLocation;
    @UnsupportedAppUsage
    private OnTouchListener mTouchInterceptor;
    private boolean mTouchable;
    private int mWidth;
    @UnsupportedAppUsage
    private int mWidthMode;
    @UnsupportedAppUsage
    private int mWindowLayoutType;
    @UnsupportedAppUsage
    private WindowManager mWindowManager;

    public interface OnDismissListener {
        void onDismiss();
    }

    private class PopupBackgroundView extends FrameLayout {
        public PopupBackgroundView(Context context) {
            super(context);
        }

        /* Access modifiers changed, original: protected */
        public int[] onCreateDrawableState(int extraSpace) {
            if (!PopupWindow.this.mAboveAnchor) {
                return super.onCreateDrawableState(extraSpace);
            }
            int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
            View.mergeDrawableStates(drawableState, PopupWindow.ABOVE_ANCHOR_STATE_SET);
            return drawableState;
        }
    }

    private class PopupDecorView extends FrameLayout {
        private Runnable mCleanupAfterExit;
        private final OnAttachStateChangeListener mOnAnchorRootDetachedListener = new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View v) {
            }

            public void onViewDetachedFromWindow(View v) {
                v.removeOnAttachStateChangeListener(this);
                if (PopupDecorView.this.isAttachedToWindow()) {
                    TransitionManager.endTransitions(PopupDecorView.this);
                }
            }
        };

        public PopupDecorView(Context context) {
            super(context);
        }

        public boolean dispatchKeyEvent(KeyEvent event) {
            if (event.getKeyCode() != 4) {
                return super.dispatchKeyEvent(event);
            }
            if (getKeyDispatcherState() == null) {
                return super.dispatchKeyEvent(event);
            }
            DispatcherState state;
            if (event.getAction() == 0 && event.getRepeatCount() == 0) {
                state = getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            }
            if (event.getAction() == 1) {
                state = getKeyDispatcherState();
                if (!(state == null || !state.isTracking(event) || event.isCanceled())) {
                    PopupWindow.this.dismiss();
                    return true;
                }
            }
            return super.dispatchKeyEvent(event);
        }

        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (PopupWindow.this.mTouchInterceptor == null || !PopupWindow.this.mTouchInterceptor.onTouch(this, ev)) {
                return super.dispatchTouchEvent(ev);
            }
            return true;
        }

        public boolean onTouchEvent(MotionEvent event) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (event.getAction() == 0 && (x < 0 || x >= getWidth() || y < 0 || y >= getHeight())) {
                PopupWindow.this.dismiss();
                return true;
            } else if (event.getAction() != 4) {
                return super.onTouchEvent(event);
            } else {
                PopupWindow.this.dismiss();
                return true;
            }
        }

        public void requestEnterTransition(Transition transition) {
            ViewTreeObserver observer = getViewTreeObserver();
            if (observer != null && transition != null) {
                final Transition enterTransition = transition.clone();
                observer.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        ViewTreeObserver observer = PopupDecorView.this.getViewTreeObserver();
                        if (observer != null) {
                            observer.removeOnGlobalLayoutListener(this);
                        }
                        final Rect epicenter = PopupWindow.this.getTransitionEpicenter();
                        enterTransition.setEpicenterCallback(new EpicenterCallback() {
                            public Rect onGetEpicenter(Transition transition) {
                                return epicenter;
                            }
                        });
                        PopupDecorView.this.startEnterTransition(enterTransition);
                    }
                });
            }
        }

        private void startEnterTransition(Transition enterTransition) {
            int i;
            int count = getChildCount();
            for (i = 0; i < count; i++) {
                View child = getChildAt(i);
                enterTransition.addTarget(child);
                child.setTransitionVisibility(4);
            }
            TransitionManager.beginDelayedTransition(this, enterTransition);
            for (i = 0; i < count; i++) {
                getChildAt(i).setTransitionVisibility(0);
            }
        }

        public void startExitTransition(Transition transition, View anchorRoot, final Rect epicenter, TransitionListener listener) {
            if (transition != null) {
                int i;
                if (anchorRoot != null) {
                    anchorRoot.addOnAttachStateChangeListener(this.mOnAnchorRootDetachedListener);
                }
                this.mCleanupAfterExit = new -$$Lambda$PopupWindow$PopupDecorView$T99WKEnQefOCXbbKvW95WY38p_I(this, listener, transition, anchorRoot);
                Transition exitTransition = transition.clone();
                exitTransition.addListener(new TransitionListenerAdapter() {
                    public void onTransitionEnd(Transition t) {
                        t.removeListener(this);
                        if (PopupDecorView.this.mCleanupAfterExit != null) {
                            PopupDecorView.this.mCleanupAfterExit.run();
                        }
                    }
                });
                exitTransition.setEpicenterCallback(new EpicenterCallback() {
                    public Rect onGetEpicenter(Transition transition) {
                        return epicenter;
                    }
                });
                int count = getChildCount();
                for (i = 0; i < count; i++) {
                    exitTransition.addTarget(getChildAt(i));
                }
                TransitionManager.beginDelayedTransition(this, exitTransition);
                for (i = 0; i < count; i++) {
                    getChildAt(i).setVisibility(4);
                }
            }
        }

        public /* synthetic */ void lambda$startExitTransition$0$PopupWindow$PopupDecorView(TransitionListener listener, Transition transition, View anchorRoot) {
            listener.onTransitionEnd(transition);
            if (anchorRoot != null) {
                anchorRoot.removeOnAttachStateChangeListener(this.mOnAnchorRootDetachedListener);
            }
            this.mCleanupAfterExit = null;
        }

        public void cancelTransitions() {
            TransitionManager.endTransitions(this);
            Runnable runnable = this.mCleanupAfterExit;
            if (runnable != null) {
                runnable.run();
            }
        }

        public void requestKeyboardShortcuts(List<KeyboardShortcutGroup> list, int deviceId) {
            if (PopupWindow.this.mParentRootView != null) {
                View parentRoot = (View) PopupWindow.this.mParentRootView.get();
                if (parentRoot != null) {
                    parentRoot.requestKeyboardShortcuts(list, deviceId);
                }
            }
        }
    }

    static {
        Extension.get().bindOriginal(new Interface() {
            public void invokePopup(PopupWindow popupWindow, LayoutParams layoutParams) {
                popupWindow.originalInvokePopup(layoutParams);
            }
        });
    }

    public /* synthetic */ void lambda$new$0$PopupWindow(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        alignToAnchor();
    }

    public PopupWindow(Context context) {
        this(context, null);
    }

    public PopupWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 16842870);
    }

    public PopupWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public PopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        this.mTmpDrawingLocation = new int[2];
        this.mTmpScreenLocation = new int[2];
        this.mTmpAppLocation = new int[2];
        this.mTempRect = new Rect();
        this.mInputMethodMode = 0;
        this.mSoftInputMode = 1;
        this.mTouchable = true;
        this.mOutsideTouchable = false;
        this.mClippingEnabled = true;
        this.mSplitTouchEnabled = -1;
        this.mAllowScrollingAnchorParent = true;
        this.mLayoutInsetDecor = false;
        this.mAttachedInDecor = true;
        this.mAttachedInDecorSet = false;
        this.mWidth = -2;
        this.mHeight = -2;
        this.mWindowLayoutType = 1000;
        this.mIgnoreCheekPress = false;
        this.mAnimationStyle = -1;
        this.mGravity = 0;
        this.mOnAnchorDetachedListener = new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View v) {
                PopupWindow.this.alignToAnchor();
            }

            public void onViewDetachedFromWindow(View v) {
            }
        };
        this.mOnAnchorRootDetachedListener = new OnAttachStateChangeListener() {
            public void onViewAttachedToWindow(View v) {
            }

            public void onViewDetachedFromWindow(View v) {
                PopupWindow.this.mIsAnchorRootAttached = false;
            }
        };
        this.mOnScrollChangedListener = new -$$Lambda$PopupWindow$nV1HS3Nc6Ck5JRIbIHe3mkyHWzc(this);
        this.mOnLayoutChangeListener = new -$$Lambda$PopupWindow$8Gc2stI5cSJZbuKX7X4Qr_vU2nI(this);
        this.mContext = context;
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PopupWindow, defStyleAttr, defStyleRes);
        Drawable bg = a.getDrawable(0);
        this.mElevation = a.getDimension(3, 0.0f);
        this.mOverlapAnchor = a.getBoolean(2, false);
        if (a.hasValueOrEmpty(1)) {
            int animStyle = a.getResourceId(1, 0);
            if (animStyle == R.style.Animation_PopupWindow) {
                this.mAnimationStyle = -1;
            } else {
                this.mAnimationStyle = animStyle;
            }
        } else {
            this.mAnimationStyle = -1;
        }
        Transition enterTransition = getTransition(a.getResourceId(4, 0));
        Transition exitTransition = a.hasValueOrEmpty(5) ? getTransition(a.getResourceId(5, 0)) : enterTransition == null ? null : enterTransition.clone();
        a.recycle();
        setEnterTransition(enterTransition);
        setExitTransition(exitTransition);
        setBackgroundDrawable(bg);
    }

    public PopupWindow() {
        this(null, 0, 0);
    }

    public PopupWindow(View contentView) {
        this(contentView, 0, 0);
    }

    public PopupWindow(int width, int height) {
        this(null, width, height);
    }

    public PopupWindow(View contentView, int width, int height) {
        this(contentView, width, height, false);
    }

    public PopupWindow(View contentView, int width, int height, boolean focusable) {
        this.mTmpDrawingLocation = new int[2];
        this.mTmpScreenLocation = new int[2];
        this.mTmpAppLocation = new int[2];
        this.mTempRect = new Rect();
        this.mInputMethodMode = 0;
        this.mSoftInputMode = 1;
        this.mTouchable = true;
        this.mOutsideTouchable = false;
        this.mClippingEnabled = true;
        this.mSplitTouchEnabled = -1;
        this.mAllowScrollingAnchorParent = true;
        this.mLayoutInsetDecor = false;
        this.mAttachedInDecor = true;
        this.mAttachedInDecorSet = false;
        this.mWidth = -2;
        this.mHeight = -2;
        this.mWindowLayoutType = 1000;
        this.mIgnoreCheekPress = false;
        this.mAnimationStyle = -1;
        this.mGravity = 0;
        this.mOnAnchorDetachedListener = /* anonymous class already generated */;
        this.mOnAnchorRootDetachedListener = /* anonymous class already generated */;
        this.mOnScrollChangedListener = new -$$Lambda$PopupWindow$nV1HS3Nc6Ck5JRIbIHe3mkyHWzc(this);
        this.mOnLayoutChangeListener = new -$$Lambda$PopupWindow$8Gc2stI5cSJZbuKX7X4Qr_vU2nI(this);
        if (contentView != null) {
            this.mContext = contentView.getContext();
            this.mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
        }
        setContentView(contentView);
        setWidth(width);
        setHeight(height);
        setFocusable(focusable);
    }

    public void setEnterTransition(Transition enterTransition) {
        this.mEnterTransition = enterTransition;
    }

    public Transition getEnterTransition() {
        return this.mEnterTransition;
    }

    public void setExitTransition(Transition exitTransition) {
        this.mExitTransition = exitTransition;
    }

    public Transition getExitTransition() {
        return this.mExitTransition;
    }

    public Rect getEpicenterBounds() {
        Rect rect = this.mEpicenterBounds;
        return rect != null ? new Rect(rect) : null;
    }

    public void setEpicenterBounds(Rect bounds) {
        this.mEpicenterBounds = bounds != null ? new Rect(bounds) : null;
    }

    private Transition getTransition(int resId) {
        if (!(resId == 0 || resId == 17760256)) {
            Transition transition = TransitionInflater.from(this.mContext).inflateTransition(resId);
            if (transition != null) {
                boolean isEmpty = (transition instanceof TransitionSet) && ((TransitionSet) transition).getTransitionCount() == 0;
                if (!isEmpty) {
                    return transition;
                }
            }
        }
        return null;
    }

    public Drawable getBackground() {
        return this.mBackground;
    }

    public void setBackgroundDrawable(Drawable background) {
        this.mBackground = background;
        StateListDrawable stateList = this.mBackground;
        if (stateList instanceof StateListDrawable) {
            stateList = stateList;
            int aboveAnchorStateIndex = stateList.findStateDrawableIndex(ABOVE_ANCHOR_STATE_SET);
            int count = stateList.getStateCount();
            int belowAnchorStateIndex = -1;
            for (int i = 0; i < count; i++) {
                if (i != aboveAnchorStateIndex) {
                    belowAnchorStateIndex = i;
                    break;
                }
            }
            if (aboveAnchorStateIndex == -1 || belowAnchorStateIndex == -1) {
                this.mBelowAnchorBackgroundDrawable = null;
                this.mAboveAnchorBackgroundDrawable = null;
                return;
            }
            this.mAboveAnchorBackgroundDrawable = stateList.getStateDrawable(aboveAnchorStateIndex);
            this.mBelowAnchorBackgroundDrawable = stateList.getStateDrawable(belowAnchorStateIndex);
        }
    }

    public float getElevation() {
        return this.mElevation;
    }

    public void setElevation(float elevation) {
        this.mElevation = elevation;
    }

    public int getAnimationStyle() {
        return this.mAnimationStyle;
    }

    public void setIgnoreCheekPress() {
        this.mIgnoreCheekPress = true;
    }

    public void setAnimationStyle(int animationStyle) {
        this.mAnimationStyle = animationStyle;
    }

    public View getContentView() {
        return this.mContentView;
    }

    public void setContentView(View contentView) {
        if (!isShowing()) {
            this.mContentView = contentView;
            if (this.mContext == null) {
                View view = this.mContentView;
                if (view != null) {
                    this.mContext = view.getContext();
                }
            }
            if (this.mWindowManager == null && this.mContentView != null) {
                this.mWindowManager = (WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE);
            }
            Context context = this.mContext;
            if (!(context == null || this.mAttachedInDecorSet)) {
                setAttachedInDecor(context.getApplicationInfo().targetSdkVersion >= 22);
            }
        }
    }

    public void setTouchInterceptor(OnTouchListener l) {
        this.mTouchInterceptor = l;
    }

    public boolean isFocusable() {
        return this.mFocusable;
    }

    public void setFocusable(boolean focusable) {
        this.mFocusable = focusable;
    }

    public int getInputMethodMode() {
        return this.mInputMethodMode;
    }

    public void setInputMethodMode(int mode) {
        this.mInputMethodMode = mode;
    }

    public void setSoftInputMode(int mode) {
        this.mSoftInputMode = mode;
    }

    public int getSoftInputMode() {
        return this.mSoftInputMode;
    }

    public boolean isTouchable() {
        return this.mTouchable;
    }

    public void setTouchable(boolean touchable) {
        this.mTouchable = touchable;
    }

    public boolean isOutsideTouchable() {
        return this.mOutsideTouchable;
    }

    public void setOutsideTouchable(boolean touchable) {
        this.mOutsideTouchable = touchable;
    }

    public boolean isClippingEnabled() {
        return this.mClippingEnabled;
    }

    public void setClippingEnabled(boolean enabled) {
        this.mClippingEnabled = enabled;
    }

    @Deprecated
    public boolean isClipToScreenEnabled() {
        return this.mClipToScreen;
    }

    @Deprecated
    public void setClipToScreenEnabled(boolean enabled) {
        this.mClipToScreen = enabled;
    }

    public boolean isClippedToScreen() {
        return this.mClipToScreen;
    }

    public void setIsClippedToScreen(boolean enabled) {
        this.mClipToScreen = enabled;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void setAllowScrollingAnchorParent(boolean enabled) {
        this.mAllowScrollingAnchorParent = enabled;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean getAllowScrollingAnchorParent() {
        return this.mAllowScrollingAnchorParent;
    }

    public boolean isSplitTouchEnabled() {
        boolean z = false;
        if (this.mSplitTouchEnabled < 0) {
            Context context = this.mContext;
            if (context != null) {
                if (context.getApplicationInfo().targetSdkVersion >= 11) {
                    z = true;
                }
                return z;
            }
        }
        if (this.mSplitTouchEnabled == 1) {
            z = true;
        }
        return z;
    }

    public void setSplitTouchEnabled(boolean enabled) {
        this.mSplitTouchEnabled = enabled;
    }

    @Deprecated
    public boolean isLayoutInScreenEnabled() {
        return this.mLayoutInScreen;
    }

    @Deprecated
    public void setLayoutInScreenEnabled(boolean enabled) {
        this.mLayoutInScreen = enabled;
    }

    public boolean isLaidOutInScreen() {
        return this.mLayoutInScreen;
    }

    public void setIsLaidOutInScreen(boolean enabled) {
        this.mLayoutInScreen = enabled;
    }

    public boolean isAttachedInDecor() {
        return this.mAttachedInDecor;
    }

    public void setAttachedInDecor(boolean enabled) {
        this.mAttachedInDecor = enabled;
        this.mAttachedInDecorSet = true;
    }

    @UnsupportedAppUsage
    public void setLayoutInsetDecor(boolean enabled) {
        this.mLayoutInsetDecor = enabled;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isLayoutInsetDecor() {
        return this.mLayoutInsetDecor;
    }

    public void setWindowLayoutType(int layoutType) {
        this.mWindowLayoutType = layoutType;
    }

    public int getWindowLayoutType() {
        return this.mWindowLayoutType;
    }

    public boolean isTouchModal() {
        return this.mNotTouchModal ^ 1;
    }

    public void setTouchModal(boolean touchModal) {
        this.mNotTouchModal = touchModal ^ 1;
    }

    @Deprecated
    public void setWindowLayoutMode(int widthSpec, int heightSpec) {
        this.mWidthMode = widthSpec;
        this.mHeightMode = heightSpec;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public void setWidth(int width) {
        this.mWidth = width;
    }

    public void setOverlapAnchor(boolean overlapAnchor) {
        this.mOverlapAnchor = overlapAnchor;
    }

    public boolean getOverlapAnchor() {
        return this.mOverlapAnchor;
    }

    public boolean isShowing() {
        return this.mIsShowing;
    }

    /* Access modifiers changed, original: protected|final */
    public final void setShowing(boolean isShowing) {
        this.mIsShowing = isShowing;
    }

    /* Access modifiers changed, original: protected|final */
    public final void setDropDown(boolean isDropDown) {
        this.mIsDropdown = isDropDown;
    }

    /* Access modifiers changed, original: protected|final */
    public final void setTransitioningToDismiss(boolean transitioningToDismiss) {
        this.mIsTransitioningToDismiss = transitioningToDismiss;
    }

    /* Access modifiers changed, original: protected|final */
    public final boolean isTransitioningToDismiss() {
        return this.mIsTransitioningToDismiss;
    }

    public void showAtLocation(View parent, int gravity, int x, int y) {
        this.mParentRootView = new WeakReference(parent.getRootView());
        showAtLocation(parent.getWindowToken(), gravity, x, y);
    }

    @UnsupportedAppUsage
    public void showAtLocation(IBinder token, int gravity, int x, int y) {
        if (!isShowing() && this.mContentView != null) {
            TransitionManager.endTransitions(this.mDecorView);
            detachFromAnchor();
            this.mIsShowing = true;
            this.mIsDropdown = false;
            this.mGravity = gravity;
            LayoutParams p = createPopupLayoutParams(token);
            preparePopup(p);
            p.x = x;
            p.y = y;
            invokePopup(p);
        }
    }

    public void showAsDropDown(View anchor) {
        showAsDropDown(anchor, 0, 0);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        showAsDropDown(anchor, xoff, yoff, DEFAULT_ANCHORED_GRAVITY);
    }

    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        if (!isShowing() && hasContentView()) {
            TransitionManager.endTransitions(this.mDecorView);
            attachToAnchor(anchor, xoff, yoff, gravity);
            this.mIsShowing = true;
            this.mIsDropdown = true;
            LayoutParams p = createPopupLayoutParams(anchor.getApplicationWindowToken());
            preparePopup(p);
            updateAboveAnchor(findDropDownPosition(anchor, p, xoff, yoff, p.width, p.height, gravity, this.mAllowScrollingAnchorParent));
            p.accessibilityIdOfAnchor = (long) anchor.getAccessibilityViewId();
            invokePopup(p);
        }
    }

    /* Access modifiers changed, original: protected|final */
    @UnsupportedAppUsage
    public final void updateAboveAnchor(boolean aboveAnchor) {
        if (aboveAnchor != this.mAboveAnchor) {
            this.mAboveAnchor = aboveAnchor;
            if (this.mBackground != null) {
                View view = this.mBackgroundView;
                if (view != null) {
                    Drawable drawable = this.mAboveAnchorBackgroundDrawable;
                    if (drawable == null) {
                        view.refreshDrawableState();
                    } else if (this.mAboveAnchor) {
                        view.setBackground(drawable);
                    } else {
                        view.setBackground(this.mBelowAnchorBackgroundDrawable);
                    }
                }
            }
        }
    }

    public boolean isAboveAnchor() {
        return this.mAboveAnchor;
    }

    @UnsupportedAppUsage
    private void preparePopup(LayoutParams p) {
        if (this.mContentView == null || this.mContext == null || this.mWindowManager == null) {
            throw new IllegalStateException("You must specify a valid content view by calling setContentView() before attempting to show the popup.");
        }
        if (p.accessibilityTitle == null) {
            p.accessibilityTitle = this.mContext.getString(R.string.popup_window_default_title);
        }
        PopupDecorView popupDecorView = this.mDecorView;
        if (popupDecorView != null) {
            popupDecorView.cancelTransitions();
        }
        if (this.mBackground != null) {
            this.mBackgroundView = createBackgroundView(this.mContentView);
            this.mBackgroundView.setBackground(this.mBackground);
        } else {
            this.mBackgroundView = this.mContentView;
        }
        this.mDecorView = createDecorView(this.mBackgroundView);
        boolean z = true;
        this.mDecorView.setIsRootNamespace(true);
        this.mBackgroundView.setElevation(this.mElevation);
        p.setSurfaceInsets(this.mBackgroundView, true, true);
        if (this.mContentView.getRawLayoutDirection() != 2) {
            z = false;
        }
        this.mPopupViewInitialLayoutDirectionInherited = z;
    }

    private PopupBackgroundView createBackgroundView(View contentView) {
        int height;
        ViewGroup.LayoutParams layoutParams = this.mContentView.getLayoutParams();
        if (layoutParams == null || layoutParams.height != -2) {
            height = -1;
        } else {
            height = -2;
        }
        PopupBackgroundView backgroundView = new PopupBackgroundView(this.mContext);
        backgroundView.addView(contentView, (ViewGroup.LayoutParams) new FrameLayout.LayoutParams(-1, height));
        return backgroundView;
    }

    private PopupDecorView createDecorView(View contentView) {
        int height;
        ViewGroup.LayoutParams layoutParams = this.mContentView.getLayoutParams();
        if (layoutParams == null || layoutParams.height != -2) {
            height = -1;
        } else {
            height = -2;
        }
        PopupDecorView decorView = new PopupDecorView(this.mContext);
        decorView.addView(contentView, -1, height);
        decorView.setClipChildren(false);
        decorView.setClipToPadding(false);
        return decorView;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    private void invokePopup(LayoutParams p) {
        if (Extension.get().getExtension() != null) {
            ((Interface) Extension.get().getExtension().asInterface()).invokePopup(this, p);
        } else {
            originalInvokePopup(p);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void originalInvokePopup(LayoutParams p) {
        Context context = this.mContext;
        if (context != null) {
            p.packageName = context.getPackageName();
        }
        PopupDecorView decorView = this.mDecorView;
        decorView.setFitsSystemWindows(this.mLayoutInsetDecor);
        setLayoutDirectionFromAnchor();
        this.mWindowManager.addView(decorView, p);
        Transition transition = this.mEnterTransition;
        if (transition != null) {
            decorView.requestEnterTransition(transition);
        }
    }

    private void setLayoutDirectionFromAnchor() {
        WeakReference weakReference = this.mAnchor;
        if (weakReference != null) {
            View anchor = (View) weakReference.get();
            if (anchor != null && this.mPopupViewInitialLayoutDirectionInherited) {
                this.mDecorView.setLayoutDirection(anchor.getLayoutDirection());
            }
        }
    }

    private int computeGravity() {
        int gravity = this.mGravity;
        if (gravity == 0) {
            gravity = DEFAULT_ANCHORED_GRAVITY;
        }
        if (!this.mIsDropdown) {
            return gravity;
        }
        if (this.mClipToScreen || this.mClippingEnabled) {
            return gravity | 268435456;
        }
        return gravity;
    }

    /* Access modifiers changed, original: protected|final */
    @UnsupportedAppUsage
    public final LayoutParams createPopupLayoutParams(IBinder token) {
        LayoutParams p = new LayoutParams();
        p.gravity = computeGravity();
        p.flags = computeFlags(p.flags);
        p.type = this.mWindowLayoutType;
        p.token = token;
        p.softInputMode = this.mSoftInputMode;
        p.windowAnimations = computeAnimationResource();
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            p.format = drawable.getOpacity();
        } else {
            p.format = -3;
        }
        int i = this.mHeightMode;
        if (i < 0) {
            this.mLastHeight = i;
            p.height = i;
        } else {
            i = this.mHeight;
            this.mLastHeight = i;
            p.height = i;
        }
        i = this.mWidthMode;
        if (i < 0) {
            this.mLastWidth = i;
            p.width = i;
        } else {
            i = this.mWidth;
            this.mLastWidth = i;
            p.width = i;
        }
        p.privateFlags = 98304;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PopupWindow:");
        stringBuilder.append(Integer.toHexString(hashCode()));
        p.setTitle(stringBuilder.toString());
        return p;
    }

    private int computeFlags(int curFlags) {
        curFlags &= -8815129;
        if (this.mIgnoreCheekPress) {
            curFlags |= 32768;
        }
        if (!this.mFocusable) {
            curFlags |= 8;
            if (this.mInputMethodMode == 1) {
                curFlags |= 131072;
            }
        } else if (this.mInputMethodMode == 2) {
            curFlags |= 131072;
        }
        if (!this.mTouchable) {
            curFlags |= 16;
        }
        if (this.mOutsideTouchable) {
            curFlags |= 262144;
        }
        if (!this.mClippingEnabled || this.mClipToScreen) {
            curFlags |= 512;
        }
        if (isSplitTouchEnabled()) {
            curFlags |= 8388608;
        }
        if (this.mLayoutInScreen) {
            curFlags |= 256;
        }
        if (this.mLayoutInsetDecor) {
            curFlags |= 65536;
        }
        if (this.mNotTouchModal) {
            curFlags |= 32;
        }
        if (this.mAttachedInDecor) {
            return curFlags | 1073741824;
        }
        return curFlags;
    }

    @UnsupportedAppUsage
    private int computeAnimationResource() {
        int i = this.mAnimationStyle;
        if (i != -1) {
            return i;
        }
        if (!this.mIsDropdown) {
            return 0;
        }
        if (this.mAboveAnchor) {
            i = R.style.Animation_DropDownUp;
        } else {
            i = R.style.Animation_DropDownDown;
        }
        return i;
    }

    /* Access modifiers changed, original: protected */
    public boolean findDropDownPosition(View anchor, LayoutParams outParams, int xOffset, int yOffset, int width, int height, int gravity, boolean allowScroll) {
        int yOffset2;
        int width2;
        int height2;
        boolean z;
        LayoutParams layoutParams;
        View view = anchor;
        LayoutParams layoutParams2 = outParams;
        int anchorHeight = anchor.getHeight();
        int anchorWidth = anchor.getWidth();
        if (this.mOverlapAnchor) {
            yOffset2 = yOffset - anchorHeight;
        } else {
            yOffset2 = yOffset;
        }
        int[] appScreenLocation = this.mTmpAppLocation;
        View appRootView = getAppRootView(anchor);
        appRootView.getLocationOnScreen(appScreenLocation);
        int[] screenLocation = this.mTmpScreenLocation;
        view.getLocationOnScreen(screenLocation);
        int[] drawingLocation = this.mTmpDrawingLocation;
        drawingLocation[0] = screenLocation[0] - appScreenLocation[0];
        drawingLocation[1] = screenLocation[1] - appScreenLocation[1];
        layoutParams2.x = drawingLocation[0] + xOffset;
        layoutParams2.y = (drawingLocation[1] + anchorHeight) + yOffset2;
        Rect displayFrame = new Rect();
        appRootView.getWindowVisibleDisplayFrame(displayFrame);
        int width3 = width;
        if (width3 == -1) {
            width2 = displayFrame.right - displayFrame.left;
        } else {
            width2 = width3;
        }
        width3 = height;
        if (width3 == -1) {
            height2 = displayFrame.bottom - displayFrame.top;
        } else {
            height2 = width3;
        }
        layoutParams2.gravity = computeGravity();
        layoutParams2.width = width2;
        layoutParams2.height = height2;
        int hgrav = Gravity.getAbsoluteGravity(gravity, anchor.getLayoutDirection()) & 7;
        int[] appScreenLocation2 = appScreenLocation;
        if (hgrav == 5) {
            layoutParams2.x -= width2 - anchorWidth;
        }
        int i = drawingLocation[1];
        int i2 = screenLocation[1];
        int[] screenLocation2 = screenLocation;
        View appRootView2 = appRootView;
        int hgrav2 = hgrav;
        LayoutParams layoutParams3 = outParams;
        int height3 = height2;
        int width4 = width2;
        height = hgrav2;
        Rect displayFrame2 = displayFrame;
        int i3 = 1;
        int i4 = i;
        int[] drawingLocation2 = drawingLocation;
        int i5 = i2;
        appScreenLocation = screenLocation2;
        screenLocation2 = appScreenLocation2;
        int[] screenLocation3 = appScreenLocation;
        boolean fitsVertical = tryFitVertical(layoutParams3, yOffset2, height3, anchorHeight, i4, i5, displayFrame.top, displayFrame.bottom, false);
        boolean fitsHorizontal = tryFitHorizontal(layoutParams3, xOffset, width4, anchorWidth, drawingLocation2[0], screenLocation3[0], displayFrame2.left, displayFrame2.right, false);
        if (fitsVertical && fitsHorizontal) {
            z = true;
            int i6 = height;
            int i7 = anchorHeight;
            layoutParams = outParams;
        } else {
            LayoutParams layoutParams4;
            i = anchor.getScrollX();
            int scrollY = anchor.getScrollY();
            Rect r = new Rect(i, scrollY, (i + width4) + xOffset, ((scrollY + height3) + anchorHeight) + yOffset2);
            if (!allowScroll) {
                layoutParams4 = outParams;
                hgrav = height;
                z = true;
            } else if (view.requestRectangleOnScreen(r, true)) {
                view.getLocationOnScreen(screenLocation3);
                drawingLocation2[0] = screenLocation3[0] - screenLocation2[0];
                drawingLocation2[1] = screenLocation3[1] - screenLocation2[1];
                layoutParams4 = outParams;
                z = true;
                layoutParams4.x = drawingLocation2[0] + xOffset;
                layoutParams4.y = (drawingLocation2[z] + anchorHeight) + yOffset2;
                hgrav = height;
                if (hgrav == 5) {
                    layoutParams4.x -= width4 - anchorWidth;
                }
            } else {
                layoutParams4 = outParams;
                z = true;
                hgrav = height;
            }
            i4 = drawingLocation2[z];
            int i8 = screenLocation3[z];
            int i9 = displayFrame2.top;
            width2 = displayFrame2.bottom;
            layoutParams3 = outParams;
            int i10 = i9;
            i9 = anchorHeight;
            layoutParams = layoutParams4;
            i5 = i8;
            int i11 = i10;
            scrollY = width2;
            tryFitVertical(layoutParams3, yOffset2, height3, i9, i4, i5, i11, scrollY, this.mClipToScreen);
            tryFitHorizontal(layoutParams3, xOffset, width4, anchorWidth, drawingLocation2[0], screenLocation3[0], displayFrame2.left, displayFrame2.right, this.mClipToScreen);
        }
        if (layoutParams.y < drawingLocation2[z]) {
            return z;
        }
        return false;
    }

    private boolean tryFitVertical(LayoutParams outParams, int yOffset, int height, int anchorHeight, int drawingLocationY, int screenLocationY, int displayFrameTop, int displayFrameBottom, boolean allowResize) {
        LayoutParams layoutParams = outParams;
        int i = height;
        int anchorTopInScreen = layoutParams.y + (screenLocationY - drawingLocationY);
        int spaceBelow = displayFrameBottom - anchorTopInScreen;
        if (anchorTopInScreen >= 0 && i <= spaceBelow) {
            return true;
        }
        if (i <= (anchorTopInScreen - anchorHeight) - displayFrameTop) {
            int yOffset2;
            if (this.mOverlapAnchor) {
                yOffset2 = yOffset + anchorHeight;
            } else {
                yOffset2 = yOffset;
            }
            layoutParams.y = (drawingLocationY - i) + yOffset2;
            return true;
        }
        if (positionInDisplayVertical(outParams, height, drawingLocationY, screenLocationY, displayFrameTop, displayFrameBottom, allowResize)) {
            return true;
        }
        return false;
    }

    private boolean positionInDisplayVertical(LayoutParams outParams, int height, int drawingLocationY, int screenLocationY, int displayFrameTop, int displayFrameBottom, boolean canResize) {
        boolean fitsInDisplay = true;
        int winOffsetY = screenLocationY - drawingLocationY;
        outParams.y += winOffsetY;
        outParams.height = height;
        int bottom = outParams.y + height;
        if (bottom > displayFrameBottom) {
            outParams.y -= bottom - displayFrameBottom;
        }
        if (outParams.y < displayFrameTop) {
            outParams.y = displayFrameTop;
            int displayFrameHeight = displayFrameBottom - displayFrameTop;
            if (!canResize || height <= displayFrameHeight) {
                fitsInDisplay = false;
            } else {
                outParams.height = displayFrameHeight;
            }
        }
        outParams.y -= winOffsetY;
        return fitsInDisplay;
    }

    private boolean tryFitHorizontal(LayoutParams outParams, int xOffset, int width, int anchorWidth, int drawingLocationX, int screenLocationX, int displayFrameLeft, int displayFrameRight, boolean allowResize) {
        int anchorLeftInScreen = outParams.x + (screenLocationX - drawingLocationX);
        int spaceRight = displayFrameRight - anchorLeftInScreen;
        if (anchorLeftInScreen < 0) {
            int i = width;
        } else if (width <= spaceRight) {
            return true;
        }
        if (positionInDisplayHorizontal(outParams, width, drawingLocationX, screenLocationX, displayFrameLeft, displayFrameRight, allowResize)) {
            return true;
        }
        return false;
    }

    private boolean positionInDisplayHorizontal(LayoutParams outParams, int width, int drawingLocationX, int screenLocationX, int displayFrameLeft, int displayFrameRight, boolean canResize) {
        boolean fitsInDisplay = true;
        int winOffsetX = screenLocationX - drawingLocationX;
        outParams.x += winOffsetX;
        int right = outParams.x + width;
        if (right > displayFrameRight) {
            outParams.x -= right - displayFrameRight;
        }
        if (outParams.x < displayFrameLeft) {
            outParams.x = displayFrameLeft;
            int displayFrameWidth = displayFrameRight - displayFrameLeft;
            if (!canResize || width <= displayFrameWidth) {
                fitsInDisplay = false;
            } else {
                outParams.width = displayFrameWidth;
            }
        }
        outParams.x -= winOffsetX;
        return fitsInDisplay;
    }

    public int getMaxAvailableHeight(View anchor) {
        return getMaxAvailableHeight(anchor, 0);
    }

    public int getMaxAvailableHeight(View anchor, int yOffset) {
        return getMaxAvailableHeight(anchor, yOffset, false);
    }

    public int getMaxAvailableHeight(View anchor, int yOffset, boolean ignoreBottomDecorations) {
        Rect displayFrame;
        int distanceToBottom;
        Rect visibleDisplayFrame = new Rect();
        getAppRootView(anchor).getWindowVisibleDisplayFrame(visibleDisplayFrame);
        if (ignoreBottomDecorations) {
            displayFrame = new Rect();
            anchor.getWindowDisplayFrame(displayFrame);
            displayFrame.top = visibleDisplayFrame.top;
            displayFrame.right = visibleDisplayFrame.right;
            displayFrame.left = visibleDisplayFrame.left;
        } else {
            displayFrame = visibleDisplayFrame;
        }
        int[] anchorPos = this.mTmpDrawingLocation;
        anchor.getLocationOnScreen(anchorPos);
        int bottomEdge = displayFrame.bottom;
        if (this.mOverlapAnchor) {
            distanceToBottom = (bottomEdge - anchorPos[1]) - yOffset;
        } else {
            distanceToBottom = (bottomEdge - (anchorPos[1] + anchor.getHeight())) - yOffset;
        }
        int returnedHeight = Math.max(distanceToBottom, (anchorPos[1] - displayFrame.top) + yOffset);
        Drawable drawable = this.mBackground;
        if (drawable == null) {
            return returnedHeight;
        }
        drawable.getPadding(this.mTempRect);
        return returnedHeight - (this.mTempRect.top + this.mTempRect.bottom);
    }

    public void dismiss() {
        if (isShowing() && !isTransitioningToDismiss()) {
            ViewGroup contentHolder;
            final PopupDecorView decorView = this.mDecorView;
            final View contentView = this.mContentView;
            ViewParent contentParent = contentView.getParent();
            if (contentParent instanceof ViewGroup) {
                contentHolder = (ViewGroup) contentParent;
            } else {
                contentHolder = null;
            }
            decorView.cancelTransitions();
            this.mIsShowing = false;
            this.mIsTransitioningToDismiss = true;
            Transition exitTransition = this.mExitTransition;
            if (exitTransition != null && decorView.isLaidOut() && (this.mIsAnchorRootAttached || this.mAnchorRoot == null)) {
                LayoutParams p = (LayoutParams) decorView.getLayoutParams();
                p.flags |= 16;
                p.flags |= 8;
                p.flags &= -131073;
                this.mWindowManager.updateViewLayout(decorView, p);
                WeakReference weakReference = this.mAnchorRoot;
                decorView.startExitTransition(exitTransition, weakReference != null ? (View) weakReference.get() : null, getTransitionEpicenter(), new TransitionListenerAdapter() {
                    public void onTransitionEnd(Transition transition) {
                        PopupWindow.this.dismissImmediate(decorView, contentHolder, contentView);
                    }
                });
            } else {
                dismissImmediate(decorView, contentHolder, contentView);
            }
            detachFromAnchor();
            OnDismissListener onDismissListener = this.mOnDismissListener;
            if (onDismissListener != null) {
                onDismissListener.onDismiss();
            }
        }
    }

    /* Access modifiers changed, original: protected|final */
    public final Rect getTransitionEpicenter() {
        WeakReference weakReference = this.mAnchor;
        View anchor = weakReference != null ? (View) weakReference.get() : null;
        View decor = this.mDecorView;
        if (anchor == null || decor == null) {
            return null;
        }
        int[] anchorLocation = anchor.getLocationOnScreen();
        int[] popupLocation = this.mDecorView.getLocationOnScreen();
        Rect bounds = new Rect(0, 0, anchor.getWidth(), anchor.getHeight());
        bounds.offset(anchorLocation[0] - popupLocation[0], anchorLocation[1] - popupLocation[1]);
        if (this.mEpicenterBounds != null) {
            int offsetX = bounds.left;
            int offsetY = bounds.top;
            bounds.set(this.mEpicenterBounds);
            bounds.offset(offsetX, offsetY);
        }
        return bounds;
    }

    private void dismissImmediate(View decorView, ViewGroup contentHolder, View contentView) {
        if (decorView.getParent() != null) {
            this.mWindowManager.removeViewImmediate(decorView);
        }
        if (contentHolder != null) {
            contentHolder.removeView(contentView);
        }
        this.mDecorView = null;
        this.mBackgroundView = null;
        this.mIsTransitioningToDismiss = false;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        this.mOnDismissListener = onDismissListener;
    }

    /* Access modifiers changed, original: protected|final */
    public final OnDismissListener getOnDismissListener() {
        return this.mOnDismissListener;
    }

    public void update() {
        if (isShowing() && hasContentView()) {
            LayoutParams p = getDecorViewLayoutParams();
            boolean update = false;
            int newAnim = computeAnimationResource();
            if (newAnim != p.windowAnimations) {
                p.windowAnimations = newAnim;
                update = true;
            }
            int newFlags = computeFlags(p.flags);
            if (newFlags != p.flags) {
                p.flags = newFlags;
                update = true;
            }
            int newGravity = computeGravity();
            if (newGravity != p.gravity) {
                p.gravity = newGravity;
                update = true;
            }
            if (update) {
                WeakReference weakReference = this.mAnchor;
                update(weakReference != null ? (View) weakReference.get() : null, p);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void update(View anchor, LayoutParams params) {
        setLayoutDirectionFromAnchor();
        this.mWindowManager.updateViewLayout(this.mDecorView, params);
    }

    public void update(int width, int height) {
        LayoutParams p = getDecorViewLayoutParams();
        update(p.x, p.y, width, height, false);
    }

    public void update(int x, int y, int width, int height) {
        update(x, y, width, height, false);
    }

    public void update(int x, int y, int width, int height, boolean force) {
        int i = x;
        int i2 = y;
        int i3 = width;
        int i4 = height;
        if (i3 >= 0) {
            this.mLastWidth = i3;
            setWidth(i3);
        }
        if (i4 >= 0) {
            this.mLastHeight = i4;
            setHeight(i4);
        }
        if (isShowing() && hasContentView()) {
            LayoutParams p = getDecorViewLayoutParams();
            boolean update = force;
            int finalWidth = this.mWidthMode;
            if (finalWidth >= 0) {
                finalWidth = this.mLastWidth;
            }
            if (!(i3 == -1 || p.width == finalWidth)) {
                this.mLastWidth = finalWidth;
                p.width = finalWidth;
                update = true;
            }
            int finalHeight = this.mHeightMode;
            if (finalHeight >= 0) {
                finalHeight = this.mLastHeight;
            }
            if (!(i4 == -1 || p.height == finalHeight)) {
                this.mLastHeight = finalHeight;
                p.height = finalHeight;
                update = true;
            }
            if (p.x != i) {
                p.x = i;
                update = true;
            }
            if (p.y != i2) {
                p.y = i2;
                update = true;
            }
            int newAnim = computeAnimationResource();
            if (newAnim != p.windowAnimations) {
                p.windowAnimations = newAnim;
                update = true;
            }
            int newFlags = computeFlags(p.flags);
            if (newFlags != p.flags) {
                p.flags = newFlags;
                update = true;
            }
            int newGravity = computeGravity();
            if (newGravity != p.gravity) {
                p.gravity = newGravity;
                update = true;
            }
            View anchor = null;
            int newAccessibilityIdOfAnchor = -1;
            WeakReference weakReference = this.mAnchor;
            if (!(weakReference == null || weakReference.get() == null)) {
                anchor = (View) this.mAnchor.get();
                newAccessibilityIdOfAnchor = anchor.getAccessibilityViewId();
            }
            if (((long) newAccessibilityIdOfAnchor) != p.accessibilityIdOfAnchor) {
                p.accessibilityIdOfAnchor = (long) newAccessibilityIdOfAnchor;
                update = true;
            }
            if (update) {
                update(anchor, p);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean hasContentView() {
        return this.mContentView != null;
    }

    /* Access modifiers changed, original: protected */
    public boolean hasDecorView() {
        return this.mDecorView != null;
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams getDecorViewLayoutParams() {
        return (LayoutParams) this.mDecorView.getLayoutParams();
    }

    public void update(View anchor, int width, int height) {
        update(anchor, false, 0, 0, width, height);
    }

    public void update(View anchor, int xoff, int yoff, int width, int height) {
        update(anchor, true, xoff, yoff, width, height);
    }

    private void update(View anchor, boolean updateLocation, int xoff, int yoff, int width, int height) {
        View view = anchor;
        int i = xoff;
        int i2 = yoff;
        if (isShowing() && hasContentView()) {
            int width2;
            boolean height2;
            WeakReference<View> oldAnchor = this.mAnchor;
            int gravity = this.mAnchoredGravity;
            boolean z = updateLocation && !(this.mAnchorXoff == i && this.mAnchorYoff == i2);
            boolean needsUpdate = z;
            if (oldAnchor == null || oldAnchor.get() != view || (needsUpdate && !this.mIsDropdown)) {
                attachToAnchor(view, i, i2, gravity);
            } else if (needsUpdate) {
                this.mAnchorXoff = i;
                this.mAnchorYoff = i2;
            }
            LayoutParams p = getDecorViewLayoutParams();
            int oldGravity = p.gravity;
            int oldWidth = p.width;
            int oldHeight = p.height;
            int oldX = p.x;
            int oldY = p.y;
            if (width < 0) {
                width2 = this.mWidth;
            } else {
                width2 = width;
            }
            if (height < 0) {
                height2 = this.mHeight;
            } else {
                height2 = height;
            }
            int oldY2 = oldY;
            int oldX2 = oldX;
            i = oldHeight;
            i2 = oldWidth;
            int oldGravity2 = oldGravity;
            oldGravity = gravity;
            LayoutParams p2 = p;
            updateAboveAnchor(findDropDownPosition(anchor, p, this.mAnchorXoff, this.mAnchorYoff, width2, height2, oldGravity, this.mAllowScrollingAnchorParent));
            boolean paramsChanged = (oldGravity2 == p2.gravity && oldX2 == p2.x && oldY2 == p2.y && i2 == p2.width && i == p2.height) ? false : true;
            update(p2.x, p2.y, width2 < 0 ? width2 : p2.width, height2 >= false ? height2 : p2.height, paramsChanged);
        }
    }

    /* Access modifiers changed, original: protected */
    public void detachFromAnchor() {
        View anchor = getAnchor();
        if (anchor != null) {
            anchor.getViewTreeObserver().removeOnScrollChangedListener(this.mOnScrollChangedListener);
            anchor.removeOnAttachStateChangeListener(this.mOnAnchorDetachedListener);
        }
        WeakReference weakReference = this.mAnchorRoot;
        View anchorRoot = weakReference != null ? (View) weakReference.get() : null;
        if (anchorRoot != null) {
            anchorRoot.removeOnAttachStateChangeListener(this.mOnAnchorRootDetachedListener);
            anchorRoot.removeOnLayoutChangeListener(this.mOnLayoutChangeListener);
        }
        this.mAnchor = null;
        this.mAnchorRoot = null;
        this.mIsAnchorRootAttached = false;
    }

    /* Access modifiers changed, original: protected */
    public void attachToAnchor(View anchor, int xoff, int yoff, int gravity) {
        detachFromAnchor();
        ViewTreeObserver vto = anchor.getViewTreeObserver();
        if (vto != null) {
            vto.addOnScrollChangedListener(this.mOnScrollChangedListener);
        }
        anchor.addOnAttachStateChangeListener(this.mOnAnchorDetachedListener);
        View anchorRoot = anchor.getRootView();
        anchorRoot.addOnAttachStateChangeListener(this.mOnAnchorRootDetachedListener);
        anchorRoot.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
        this.mAnchor = new WeakReference(anchor);
        this.mAnchorRoot = new WeakReference(anchorRoot);
        this.mIsAnchorRootAttached = anchorRoot.isAttachedToWindow();
        this.mParentRootView = this.mAnchorRoot;
        this.mAnchorXoff = xoff;
        this.mAnchorYoff = yoff;
        this.mAnchoredGravity = gravity;
    }

    /* Access modifiers changed, original: protected */
    public View getAnchor() {
        WeakReference weakReference = this.mAnchor;
        return weakReference != null ? (View) weakReference.get() : null;
    }

    private void alignToAnchor() {
        WeakReference weakReference = this.mAnchor;
        View anchor = weakReference != null ? (View) weakReference.get() : null;
        if (anchor != null && anchor.isAttachedToWindow() && hasDecorView()) {
            LayoutParams p = getDecorViewLayoutParams();
            updateAboveAnchor(findDropDownPosition(anchor, p, this.mAnchorXoff, this.mAnchorYoff, p.width, p.height, this.mAnchoredGravity, false));
            update(p.x, p.y, -1, -1, true);
        }
    }

    private View getAppRootView(View anchor) {
        View appWindowView = WindowManagerGlobal.getInstance().getWindowView(anchor.getApplicationWindowToken());
        if (appWindowView != null) {
            return appWindowView;
        }
        return anchor.getRootView();
    }
}
