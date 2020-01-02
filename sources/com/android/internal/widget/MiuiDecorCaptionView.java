package com.android.internal.widget;

import android.app.Activity;
import android.app.ActivityTaskManager;
import android.app.IActivityTaskManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.ContentObserver;
import android.graphics.Outline;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ResultReceiver;
import android.provider.BrowserContract;
import android.provider.MiuiSettings.XSpace;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.MiuiMultiWindowUtils;
import android.util.Slog;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.view.ViewOutlineProvider;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import com.android.internal.R;
import com.android.internal.policy.PhoneWindow;
import com.miui.freeform.IMiuiFreeformWindowService;
import com.miui.freeform.IMiuiFreeformWindowService.Stub;
import java.util.ArrayList;
import miui.os.DeviceFeature;
import miui.security.SecurityManager;

public class MiuiDecorCaptionView extends DecorCaptionView implements OnTouchListener, OnGestureListener {
    private static final int COLOR_DECOR_CAPTION_VIEW = -13027256;
    private static final int COLOR_DECOR_CAPTION_VIEW_TRANSLUCENT = 859059252;
    private static final ArrayList<String> LIST_ABOUT_NEED_MODIFY_CAPTION_COLOR_NO_TRANSLUCENT = new ArrayList();
    private static final ArrayList<String> LIST_ABOUT_NEED_OVERLAY_CAPTION_VIEW = new ArrayList();
    private static final String TAG = "MiuiDecorCaptionView";
    private static final String WEIXIN_LAUNCHERUI_NAME = "com.tencent.mm.ui.LauncherUI";
    private final float RADIUS = 11.0f;
    private View mCaption;
    private final Rect mCaptionRect = new Rect();
    private boolean mCheckForDragging;
    private View mClickTarget;
    private View mClose;
    private final Rect mCloseRect = new Rect();
    private View mContent;
    private int mDragSlop;
    private boolean mDragging = false;
    private ServiceConnection mFreeformConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            MiuiDecorCaptionView.this.mFreeformService = Stub.asInterface(service);
        }

        public void onServiceDisconnected(ComponentName name) {
            MiuiDecorCaptionView.this.mFreeformService = null;
        }
    };
    private IMiuiFreeformWindowService mFreeformService;
    private GestureDetector mGestureDetector;
    private View mMaximize;
    private final Rect mMaximizeRect = new Rect();
    private View mMove;
    private final Rect mMoveRect = new Rect();
    private boolean mOverlayWithAppContent = false;
    private PhoneWindow mOwner = null;
    private View mPin;
    private final Rect mPinRect = new Rect();
    private boolean mPinnedWindow;
    private ViewGroup mRoot;
    private SecurityManager mSecurityManager;
    private SettingsObserver mSettingsObserver;
    private boolean mShow = false;
    private ArrayList<View> mTouchDispatchList = new ArrayList(2);
    private int mTouchDownX;
    private int mTouchDownY;
    private ViewOutlineProvider mViewOutlineProvider = new ViewOutlineProvider() {
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), MiuiDecorCaptionView.this.dipToPx(11.0f));
            outline.setAlpha(0.0f);
        }
    };
    private boolean useTranslucentColor = false;

    private class SettingsObserver extends ContentObserver {
        SettingsObserver(Context context) {
            super(new Handler());
            context.getContentResolver().registerContentObserver(Secure.getUriFor("gamebox_stick"), false, this);
        }

        public void onChange(boolean selfChange, Uri uri, int userId) {
            super.onChange(selfChange, uri, userId);
            if (MiuiDecorCaptionView.this.mPin != null && uri.equals(Secure.getUriFor("gamebox_stick"))) {
                MiuiDecorCaptionView.this.updatePinButton();
            }
        }
    }

    static {
        LIST_ABOUT_NEED_OVERLAY_CAPTION_VIEW.add(XSpace.QQ_PACKAGE_NAME);
        LIST_ABOUT_NEED_MODIFY_CAPTION_COLOR_NO_TRANSLUCENT.add(BrowserContract.AUTHORITY);
    }

    public MiuiDecorCaptionView(Context context) {
        super(context);
        init(context);
    }

    public MiuiDecorCaptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MiuiDecorCaptionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mDragSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        this.mGestureDetector = new GestureDetector(context, (OnGestureListener) this);
        this.mSecurityManager = (SecurityManager) context.getSystemService(Context.SECURITY_SERVICE);
        Intent intent = new Intent();
        intent.setAction("miui.intent.action.FREEFORM_WINDOW");
        intent.setPackage("com.miui.freeform");
        context.bindService(intent, this.mFreeformConnection, 1);
        this.mSettingsObserver = new SettingsObserver(context);
        this.mPinnedWindow = false;
    }

    /* Access modifiers changed, original: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mCaption = getChildAt(0);
    }

    public void setPhoneWindow(PhoneWindow owner, boolean show) {
        this.mOwner = owner;
        this.mShow = show;
        owner.setOverlayWithDecorCaptionEnabled(true);
        this.mOverlayWithAppContent = owner.isOverlayWithDecorCaptionEnabled();
        this.mOwner.getDecorView().setOutlineProvider(ViewOutlineProvider.BOUNDS);
        this.mMove = findViewById(R.id.move_window);
        this.mPin = findViewById(R.id.pin_window);
        this.mMaximize = findViewById(R.id.maximize_window);
        this.mClose = findViewById(R.id.close_window);
        if (this.mContext.getUserId() == 999) {
            this.mMove.setVisibility(8);
            this.mPin.setVisibility(8);
        }
        if (!MiuiMultiWindowUtils.supportPinFreeFormApp()) {
            this.mPin.setVisibility(8);
        }
        updateOutLineProvider();
        updateCaptionVisibility();
        invalidate();
        invalidateOutline();
    }

    private void updateOutLineProvider() {
        boolean canClip = inFreeformWindowingMode();
        setClipToOutline(canClip);
        setOutlineProvider(this.mViewOutlineProvider);
        PhoneWindow phoneWindow = this.mOwner;
        if (phoneWindow != null) {
            phoneWindow.getDecorView().setClipToOutline(canClip);
            this.mOwner.getDecorView().setOutlineProvider(this.mViewOutlineProvider);
            if (!canClip) {
                resizeWindowDismiss();
            }
        }
    }

    private float dipToPx(float dip) {
        return TypedValue.applyDimension(1, dip, getResources().getDisplayMetrics());
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            if (this.mCaptionRect.contains(x, y)) {
                this.mClickTarget = this.mCaption;
            }
            if (this.mMoveRect.contains(x, y)) {
                this.mClickTarget = this.mMove;
            }
            if (this.mPinRect.contains(x, y)) {
                this.mClickTarget = this.mPin;
            }
            if (this.mMaximizeRect.contains(x, y)) {
                this.mClickTarget = this.mMaximize;
            }
            if (this.mCloseRect.contains(x, y)) {
                this.mClickTarget = this.mClose;
            }
        }
        return this.mClickTarget != null;
    }

    /* JADX WARNING: Missing block: B:21:0x004b, code skipped:
            if (r8 != 3) goto L_0x008a;
     */
    public boolean onTouchEvent(android.view.MotionEvent r11) {
        /*
        r10 = this;
        r0 = r10.mClickTarget;
        r1 = 0;
        if (r0 == 0) goto L_0x0094;
    L_0x0005:
        r0 = r10.mGestureDetector;
        r0.onTouchEvent(r11);
        r0 = r11.getAction();
        r2 = 3;
        r3 = 1;
        if (r0 == r3) goto L_0x0014;
    L_0x0012:
        if (r0 != r2) goto L_0x0017;
    L_0x0014:
        r4 = 0;
        r10.mClickTarget = r4;
    L_0x0017:
        r4 = r11.getX();
        r4 = (int) r4;
        r5 = r11.getY();
        r5 = (int) r5;
        r6 = r10.mCaptionRect;
        r6 = r6.contains(r4, r5);
        if (r6 == 0) goto L_0x008a;
    L_0x0029:
        r6 = r11.getActionIndex();
        r6 = r11.getToolType(r6);
        if (r6 != r2) goto L_0x0035;
    L_0x0033:
        r6 = r3;
        goto L_0x0036;
    L_0x0035:
        r6 = r1;
    L_0x0036:
        r7 = r11.getButtonState();
        r7 = r7 & r3;
        if (r7 == 0) goto L_0x003f;
    L_0x003d:
        r7 = r3;
        goto L_0x0040;
    L_0x003f:
        r7 = r1;
    L_0x0040:
        r8 = r11.getActionMasked();
        if (r8 == 0) goto L_0x0079;
    L_0x0046:
        if (r8 == r3) goto L_0x006e;
    L_0x0048:
        r9 = 2;
        if (r8 == r9) goto L_0x004e;
    L_0x004b:
        if (r8 == r2) goto L_0x006e;
    L_0x004d:
        goto L_0x008a;
    L_0x004e:
        r2 = r10.mDragging;
        if (r2 != 0) goto L_0x008a;
    L_0x0052:
        r2 = r10.mCheckForDragging;
        if (r2 == 0) goto L_0x008a;
    L_0x0056:
        if (r6 != 0) goto L_0x005e;
    L_0x0058:
        r2 = r10.passedSlop(r4, r5);
        if (r2 == 0) goto L_0x008a;
    L_0x005e:
        r10.mCheckForDragging = r1;
        r10.mDragging = r3;
        r2 = r11.getRawX();
        r8 = r11.getRawY();
        r10.startMovingTask(r2, r8);
        goto L_0x008a;
    L_0x006e:
        r2 = r10.mDragging;
        if (r2 != 0) goto L_0x0073;
    L_0x0072:
        goto L_0x008a;
    L_0x0073:
        r10.mDragging = r1;
        r1 = r10.mCheckForDragging;
        r1 = r1 ^ r3;
        return r1;
    L_0x0079:
        r10.mDragging = r1;
        r2 = r10.mShow;
        if (r2 != 0) goto L_0x0080;
    L_0x007f:
        return r1;
    L_0x0080:
        if (r6 == 0) goto L_0x0084;
    L_0x0082:
        if (r7 == 0) goto L_0x008a;
    L_0x0084:
        r10.mCheckForDragging = r3;
        r10.mTouchDownX = r4;
        r10.mTouchDownY = r5;
    L_0x008a:
        r2 = r10.mDragging;
        if (r2 != 0) goto L_0x0092;
    L_0x008e:
        r2 = r10.mCheckForDragging;
        if (r2 == 0) goto L_0x0093;
    L_0x0092:
        r1 = r3;
    L_0x0093:
        return r1;
    L_0x0094:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.MiuiDecorCaptionView.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public ArrayList<View> buildTouchDispatchChildList() {
        this.mTouchDispatchList.ensureCapacity(3);
        View view = this.mCaption;
        if (view != null) {
            this.mTouchDispatchList.add(view);
        }
        view = this.mContent;
        if (view != null) {
            this.mTouchDispatchList.add(view);
        }
        return this.mTouchDispatchList;
    }

    public boolean shouldDelayChildPressedState() {
        return false;
    }

    private boolean passedSlop(int x, int y) {
        return Math.abs(x - this.mTouchDownX) > this.mDragSlop || Math.abs(y - this.mTouchDownY) > this.mDragSlop;
    }

    public void onConfigurationChanged(boolean show) {
        this.mShow = show;
        updateCaptionVisibility();
        updateOutLineProvider();
        updataDecorCaptionView();
    }

    public void removeAllViews() {
        if (this.mContent != null) {
            if (BrowserContract.AUTHORITY.equals(getContext().getPackageName())) {
                ((ViewGroup) this.mContent).removeAllViews();
                return;
            }
        }
        super.removeAllViews();
    }

    public void addView(View child, int index, LayoutParams params) {
        if (this.mContext.getPackageName().equals("com.tencent.mm") && child.getParent() != null) {
            child = (ViewGroup) child.getParent();
            if (child.getParent() != null) {
                ((ViewGroup) child.getParent()).removeView(child);
            }
        }
        if (!(params instanceof MarginLayoutParams)) {
            if (BrowserContract.AUTHORITY.equals(getContext().getPackageName())) {
                ((ViewGroup) this.mContent).addView(child, index, params);
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("params ");
            stringBuilder.append(params);
            stringBuilder.append(" must subclass MarginLayoutParams");
            throw new IllegalArgumentException(stringBuilder.toString());
        } else if (index >= 2 || getChildCount() >= 2) {
            throw new IllegalStateException("MiuiDecorCaptionView can only handle 1 client view");
        } else {
            super.addView(child, 0, params);
            this.mContent = child;
            this.mRoot = (ViewGroup) getParent();
            if (this.mContext.getPackageName().equals(XSpace.QQ_PACKAGE_NAME)) {
                ViewGroup viewGroup = this.mRoot;
                if (viewGroup != null) {
                    viewGroup.setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
                        public void onChildViewAdded(View parent, View child) {
                            if (!(child instanceof ViewGroup) && MiuiDecorCaptionView.this.inFreeformWindowingMode()) {
                                child.setAlpha(0.0f);
                            }
                        }

                        public void onChildViewRemoved(View parent, View child) {
                        }
                    });
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int captionHeight;
        if (this.mCaption.getVisibility() != 8) {
            measureChildWithMargins(this.mCaption, widthMeasureSpec, 0, heightMeasureSpec, 0);
            captionHeight = this.mCaption.getMeasuredHeight();
        } else {
            captionHeight = 0;
        }
        if (this.mContent != null) {
            if (this.mOverlayWithAppContent && needOverlayCaptionView()) {
                measureChildWithMargins(this.mContent, widthMeasureSpec, 0, heightMeasureSpec, 0);
            } else {
                measureChildWithMargins(this.mContent, widthMeasureSpec, 0, heightMeasureSpec, captionHeight);
            }
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
    }

    /* Access modifiers changed, original: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int captionHeight;
        if (this.mCaption.getVisibility() != 8) {
            View view = this.mCaption;
            view.layout(0, 0, view.getMeasuredWidth(), this.mCaption.getMeasuredHeight());
            captionHeight = this.mCaption.getBottom() - this.mCaption.getTop();
            this.mCaption.getHitRect(this.mCaptionRect);
            this.mMove.getHitRect(this.mMoveRect);
            this.mPin.getHitRect(this.mPinRect);
            this.mMaximize.getHitRect(this.mMaximizeRect);
            this.mClose.getHitRect(this.mCloseRect);
            Rect rect = this.mCloseRect;
            rect.right += 30;
            rect = this.mCloseRect;
            rect.left -= 30;
        } else {
            captionHeight = 0;
            this.mCaptionRect.setEmpty();
            this.mMoveRect.setEmpty();
            this.mPinRect.setEmpty();
            this.mMaximizeRect.setEmpty();
            this.mCloseRect.setEmpty();
        }
        if (this.mContent != null) {
            View view2;
            if (this.mOverlayWithAppContent && needOverlayCaptionView()) {
                view2 = this.mContent;
                view2.layout(0, 0, view2.getMeasuredWidth(), this.mContent.getMeasuredHeight());
            } else {
                view2 = this.mContent;
                view2.layout(0, captionHeight, view2.getMeasuredWidth(), this.mContent.getMeasuredHeight() + captionHeight);
            }
        }
        this.mOwner.notifyRestrictedCaptionAreaCallback(this.mMaximize.getLeft(), this.mMaximize.getTop(), this.mClose.getRight(), this.mClose.getBottom());
        PhoneWindow phoneWindow = this.mOwner;
        phoneWindow.setElevation(phoneWindow.getElevation());
    }

    private boolean isFillingScreen() {
        return ((getWindowSystemUiVisibility() | getSystemUiVisibility()) & 2565) != 0;
    }

    private void updateCaptionVisibility() {
        boolean invisible = this.mShow ^ 1;
        this.mCaption.setVisibility(invisible ? 8 : 0);
        if (!invisible) {
            if (this.mCaption.getBackground() == null) {
                this.mCaption.setBackground(getContext().getDrawable(R.drawable.decor_caption_title));
            }
            if (this.mMaximize.getBackground() == null) {
                this.mMaximize.setBackground(getContext().getDrawable(R.drawable.decor_maximize_button_dark));
            }
            if (this.mClose.getBackground() == null) {
                this.mClose.setBackground(getContext().getDrawable(R.drawable.decor_close_button_dark));
            }
            if (this.mPin.getBackground() == null) {
                this.mPin.setBackground(getContext().getDrawable(this.mPinnedWindow ? R.drawable.pin_button : R.drawable.unpin_button));
            }
            if (this.mMove.getBackground() == null) {
                this.mMove.setBackground(getContext().getDrawable(R.drawable.move_button));
            }
        }
    }

    public boolean isCaptionShowing() {
        return this.mShow;
    }

    public int getCaptionHeight() {
        View view = this.mCaption;
        return view != null ? view.getHeight() : 0;
    }

    public void removeContentView() {
        View view = this.mContent;
        if (view != null) {
            removeView(view);
            this.mContent = null;
        }
    }

    public View getCaption() {
        return this.mCaption;
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(-1, -1);
    }

    /* Access modifiers changed, original: protected */
    public LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    /* Access modifiers changed, original: protected */
    public boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        if (this.mSecurityManager == null) {
            this.mSecurityManager = (SecurityManager) this.mContext.getSystemService(Context.SECURITY_SERVICE);
        }
        if (this.mSecurityManager == null) {
            return false;
        }
        IActivityTaskManager mInterface = ActivityTaskManager.getService();
        try {
            final int taskId = mInterface.getTaskForActivity(getRootView().getAttachedActivityInstance().getActivityToken(), false);
            View view = this.mClickTarget;
            View view2 = this.mMove;
            String str = Context.INPUT_METHOD_SERVICE;
            if (view == view2) {
                if (this.mFreeformService == null) {
                    Intent intent = new Intent();
                    intent.setAction("miui.intent.action.FREEFORM_WINDOW");
                    intent.setPackage("com.miui.freeform");
                    this.mContext.bindService(intent, this.mFreeformConnection, 1);
                }
                if (this.mFreeformService == null) {
                    return false;
                }
                InputMethodManager im = (InputMethodManager) getContext().getSystemService(str);
                ResultReceiver receiver = null;
                if (VERSION.SDK_INT >= 3) {
                    receiver = new ResultReceiver(null) {
                        /* Access modifiers changed, original: protected */
                        public void onReceiveResult(int resultCode, Bundle resultData) {
                            if (resultCode == 3) {
                                try {
                                    MiuiDecorCaptionView.this.mFreeformService.windowResize(taskId, MiuiDecorCaptionView.this.getContext().getPackageName());
                                } catch (RemoteException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    };
                }
                if (!im.hideSoftInputFromWindow(this.mOwner.getDecorView().getWindowToken(), 2, receiver)) {
                    this.mFreeformService.windowResize(taskId, getContext().getPackageName());
                }
            } else if (this.mClickTarget == this.mClose) {
                ((InputMethodManager) getContext().getSystemService(str)).hideSoftInputFromWindow(this.mOwner.getDecorView().getWindowToken(), 2, null);
                this.mOwner.dispatchOnWindowDismissed(true, false);
                sendWindowClosedIntent();
            } else if (this.mClickTarget == this.mMaximize) {
                this.mSecurityManager.moveTaskToStack(taskId, 1, true);
            } else if (this.mClickTarget == this.mPin) {
                pinWindow();
            }
        } catch (RemoteException e2) {
            try {
                mInterface.setTaskWindowingMode(mInterface.getTaskForActivity(getRootView().getAttachedActivityInstance().getActivityToken(), false), 1, false);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
        return true;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    public void onLongPress(MotionEvent e) {
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    /* Access modifiers changed, original: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        resizeWindowDismiss();
        try {
            if (this.mFreeformService != null) {
                this.mContext.unbindService(this.mFreeformConnection);
            }
            this.mContext.getContentResolver().unregisterContentObserver(this.mSettingsObserver);
        } catch (Exception e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("onDetachedFromWindow error happened:");
            stringBuilder.append(e.toString());
            Slog.d(TAG, stringBuilder.toString());
        }
    }

    /* Access modifiers changed, original: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        updateOutLineProvider();
        updateCaptionColor();
        registerSettingsObserver();
        updataDecorCaptionView();
        invalidate();
        invalidateOutline();
        updatePinButton();
    }

    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (!hasWindowFocus) {
            resizeWindowDismiss();
        }
    }

    private boolean supportsPinButton() {
        return "lotus".equals(Build.DEVICE) || DeviceFeature.hasMirihiSupport();
    }

    private void updateCaptionColor() {
        if (!this.useTranslucentColor || !this.mOverlayWithAppContent || needOverlayCaptionView() || LIST_ABOUT_NEED_MODIFY_CAPTION_COLOR_NO_TRANSLUCENT.contains(this.mContext.getPackageName())) {
            this.mCaption.setBackgroundColor(COLOR_DECOR_CAPTION_VIEW);
        } else {
            this.mCaption.setBackgroundColor(COLOR_DECOR_CAPTION_VIEW_TRANSLUCENT);
        }
    }

    private void updatePinButton() {
        try {
            boolean z = true;
            if (ActivityTaskManager.getService().handleFreeformModeRequst(getRootView().getAttachedActivityInstance().getActivityToken(), 3) != 1) {
                z = false;
            }
            this.mPinnedWindow = z;
            this.mPin.setBackground(getContext().getDrawable(this.mPinnedWindow ? R.drawable.pin_button : R.drawable.unpin_button));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void resizeWindowDismiss() {
        if (this.mFreeformService != null) {
            try {
                IActivityTaskManager mInterface = ActivityTaskManager.getService();
                Activity attachedActivity = getRootView().getAttachedActivityInstance();
                if (attachedActivity != null) {
                    this.mFreeformService.windowDismiss(mInterface.getTaskForActivity(attachedActivity.getActivityToken(), false));
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void pinWindow() {
        try {
            ActivityTaskManager.getService().handleFreeformModeRequst(getRootView().getAttachedActivityInstance().getActivityToken(), this.mPinnedWindow ? 2 : 1);
            updatePinButton();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendWindowClosedIntent() {
        Intent intent = new Intent("com.miui.FREEFORM_WINDOW_CLOSED");
        intent.putExtra("window_name", getContext().getPackageName());
        getContext().sendBroadcast(intent);
    }

    private boolean inFreeformWindowingMode() {
        return this.mContext.getResources().getConfiguration().windowConfiguration.getWindowingMode() == 5;
    }

    private boolean needOverlayCaptionView() {
        if (LIST_ABOUT_NEED_OVERLAY_CAPTION_VIEW.contains(this.mContext.getPackageName())) {
            return true;
        }
        return false;
    }

    private void registerSettingsObserver() {
        this.mContext.getContentResolver().registerContentObserver(System.getUriFor("HFreeForm_Position_Left"), false, new ContentObserver(new Handler()) {
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                MiuiDecorCaptionView.this.updataDecorCaptionView();
            }
        });
    }

    private void updataDecorCaptionView() {
        DisplayMetrics outMetrics = new DisplayMetrics();
        this.mContext.getDisplay().getRealMetrics(outMetrics);
        boolean isVertical = outMetrics.widthPixels < outMetrics.heightPixels;
        Rect freeformRect = new Rect();
        freeformRect.left = System.getInt(this.mContext.getContentResolver(), "HFreeForm_Position_Left", -1);
        freeformRect.right = System.getInt(this.mContext.getContentResolver(), "HFreeForm_Position_Right", -1);
        float position = (float) ((freeformRect.left + freeformRect.right) / 2);
        RelativeLayout.LayoutParams closLayoutParams = (RelativeLayout.LayoutParams) this.mClose.getLayoutParams();
        RelativeLayout.LayoutParams movelayoutParams = (RelativeLayout.LayoutParams) this.mMove.getLayoutParams();
        RelativeLayout.LayoutParams maximizelayoutParams = (RelativeLayout.LayoutParams) this.mMaximize.getLayoutParams();
        if (closLayoutParams != null && movelayoutParams != null && maximizelayoutParams != null) {
            if (isVertical || position > ((float) (outMetrics.widthPixels / 2))) {
                closLayoutParams.removeRule(20);
                closLayoutParams.addRule(21);
                this.mClose.setLayoutParams(closLayoutParams);
                movelayoutParams.removeRule(21);
                movelayoutParams.addRule(20);
                this.mMove.setLayoutParams(movelayoutParams);
                maximizelayoutParams.removeRule(16);
                maximizelayoutParams.addRule(17, R.id.move_window);
                this.mMaximize.setLayoutParams(maximizelayoutParams);
            } else {
                closLayoutParams.removeRule(21);
                closLayoutParams.addRule(20);
                this.mClose.setLayoutParams(closLayoutParams);
                movelayoutParams.removeRule(20);
                movelayoutParams.addRule(21);
                this.mMove.setLayoutParams(movelayoutParams);
                maximizelayoutParams.removeRule(17);
                maximizelayoutParams.addRule(16, R.id.move_window);
                this.mMaximize.setLayoutParams(maximizelayoutParams);
            }
        }
    }
}
