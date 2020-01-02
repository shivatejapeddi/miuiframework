package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnShowListener;
import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.Window.Callback;
import android.view.Window.OnWindowDismissedCallback;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import com.android.internal.app.WindowDecorActionBar;
import com.android.internal.policy.MiuiPhoneWindow;
import java.lang.ref.WeakReference;

public class Dialog implements DialogInterface, Callback, KeyEvent.Callback, OnCreateContextMenuListener, OnWindowDismissedCallback {
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static final int CANCEL = 68;
    private static final String DIALOG_HIERARCHY_TAG = "android:dialogHierarchy";
    private static final String DIALOG_SHOWING_TAG = "android:dialogShowing";
    private static final int DISMISS = 67;
    private static final int SHOW = 69;
    private static final String TAG = "Dialog";
    private ActionBar mActionBar;
    private ActionMode mActionMode;
    private int mActionModeTypeStarting;
    private String mCancelAndDismissTaken;
    @UnsupportedAppUsage
    private Message mCancelMessage;
    protected boolean mCancelable;
    private boolean mCanceled;
    @UnsupportedAppUsage
    final Context mContext;
    private boolean mCreated;
    View mDecor;
    private final Runnable mDismissAction;
    @UnsupportedAppUsage
    private Message mDismissMessage;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final Handler mHandler;
    @UnsupportedAppUsage
    private final Handler mListenersHandler;
    @UnsupportedAppUsage
    private OnKeyListener mOnKeyListener;
    @UnsupportedAppUsage
    private Activity mOwnerActivity;
    private SearchEvent mSearchEvent;
    @UnsupportedAppUsage
    private Message mShowMessage;
    @UnsupportedAppUsage
    private boolean mShowing;
    @UnsupportedAppUsage
    final Window mWindow;
    private final WindowManager mWindowManager;

    private static final class ListenersHandler extends Handler {
        private final WeakReference<DialogInterface> mDialog;

        public ListenersHandler(Dialog dialog) {
            this.mDialog = new WeakReference(dialog);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 67:
                    ((OnDismissListener) msg.obj).onDismiss((DialogInterface) this.mDialog.get());
                    return;
                case 68:
                    ((OnCancelListener) msg.obj).onCancel((DialogInterface) this.mDialog.get());
                    return;
                case 69:
                    ((OnShowListener) msg.obj).onShow((DialogInterface) this.mDialog.get());
                    return;
                default:
                    return;
            }
        }
    }

    public Dialog(Context context) {
        this(context, 0, true);
    }

    public Dialog(Context context, int themeResId) {
        this(context, themeResId, true);
    }

    Dialog(Context context, int themeResId, boolean createContextThemeWrapper) {
        this.mCancelable = true;
        this.mCreated = false;
        this.mShowing = false;
        this.mCanceled = false;
        this.mHandler = new Handler();
        this.mActionModeTypeStarting = 0;
        this.mDismissAction = new -$$Lambda$oslF4K8Uk6v-6nTRoaEpCmfAptE(this);
        if (createContextThemeWrapper) {
            if (themeResId == 0) {
                TypedValue outValue = new TypedValue();
                context.getTheme().resolveAttribute(16843528, outValue, true);
                themeResId = outValue.resourceId;
            }
            this.mContext = new ContextThemeWrapper(context, themeResId);
        } else {
            this.mContext = context;
        }
        this.mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Window w = new MiuiPhoneWindow(this.mContext);
        this.mWindow = w;
        w.setCallback(this);
        w.setOnWindowDismissedCallback(this);
        w.setOnWindowSwipeDismissedCallback(new -$$Lambda$Dialog$zXRzrq3I7H1_zmZ8d_W7t2CQN0I(this));
        w.setWindowManager(this.mWindowManager, null, null);
        w.setGravity(17);
        this.mListenersHandler = new ListenersHandler(this);
    }

    public /* synthetic */ void lambda$new$0$Dialog() {
        if (this.mCancelable) {
            cancel();
        }
    }

    @Deprecated
    protected Dialog(Context context, boolean cancelable, Message cancelCallback) {
        this(context);
        this.mCancelable = cancelable;
        updateWindowForCancelable();
        this.mCancelMessage = cancelCallback;
    }

    protected Dialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        this(context);
        this.mCancelable = cancelable;
        updateWindowForCancelable();
        setOnCancelListener(cancelListener);
    }

    public final Context getContext() {
        return this.mContext;
    }

    public ActionBar getActionBar() {
        return this.mActionBar;
    }

    public final void setOwnerActivity(Activity activity) {
        this.mOwnerActivity = activity;
        getWindow().setVolumeControlStream(this.mOwnerActivity.getVolumeControlStream());
    }

    public final Activity getOwnerActivity() {
        return this.mOwnerActivity;
    }

    public boolean isShowing() {
        View view = this.mDecor;
        return view != null && view.getVisibility() == 0;
    }

    public void create() {
        if (!this.mCreated) {
            dispatchOnCreate(null);
        }
    }

    public void show() {
        if (this.mShowing) {
            if (this.mDecor != null) {
                if (this.mWindow.hasFeature(8)) {
                    this.mWindow.invalidatePanelMenu(8);
                }
                this.mDecor.setVisibility(0);
            }
            return;
        }
        this.mCanceled = false;
        if (this.mCreated) {
            this.mWindow.getDecorView().dispatchConfigurationChanged(this.mContext.getResources().getConfiguration());
        } else {
            dispatchOnCreate(null);
        }
        onStart();
        this.mDecor = this.mWindow.getDecorView();
        if (this.mActionBar == null && this.mWindow.hasFeature(8)) {
            ApplicationInfo info = this.mContext.getApplicationInfo();
            this.mWindow.setDefaultIcon(info.icon);
            this.mWindow.setDefaultLogo(info.logo);
            this.mActionBar = new WindowDecorActionBar(this);
        }
        LayoutParams l = this.mWindow.getAttributes();
        boolean restoreSoftInputMode = false;
        if ((l.softInputMode & 256) == 0) {
            l.softInputMode |= 256;
            restoreSoftInputMode = true;
        }
        this.mWindowManager.addView(this.mDecor, l);
        if (restoreSoftInputMode) {
            l.softInputMode &= TrafficStats.TAG_NETWORK_STACK_RANGE_END;
        }
        this.mShowing = true;
        sendShowMessage();
    }

    public void hide() {
        View view = this.mDecor;
        if (view != null) {
            view.setVisibility(8);
        }
    }

    public void dismiss() {
        if (Looper.myLooper() == this.mHandler.getLooper()) {
            dismissDialog();
        } else {
            this.mHandler.post(this.mDismissAction);
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dismissDialog() {
        if (this.mDecor != null && this.mShowing) {
            if (this.mWindow.isDestroyed()) {
                Log.e(TAG, "Tried to dismissDialog() but the Dialog's window was already destroyed!");
                return;
            }
            try {
                this.mWindowManager.removeViewImmediate(this.mDecor);
            } finally {
                ActionMode actionMode = this.mActionMode;
                if (actionMode != null) {
                    actionMode.finish();
                }
                this.mDecor = null;
                this.mWindow.closeAllPanels();
                onStop();
                this.mShowing = false;
                sendDismissMessage();
            }
        }
    }

    private void sendDismissMessage() {
        Message message = this.mDismissMessage;
        if (message != null) {
            Message.obtain(message).sendToTarget();
        }
    }

    private void sendShowMessage() {
        Message message = this.mShowMessage;
        if (message != null) {
            Message.obtain(message).sendToTarget();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchOnCreate(Bundle savedInstanceState) {
        if (!this.mCreated) {
            onCreate(savedInstanceState);
            this.mCreated = true;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
    }

    /* Access modifiers changed, original: protected */
    public void onStart() {
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(true);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(false);
        }
    }

    public Bundle onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(DIALOG_SHOWING_TAG, this.mShowing);
        if (this.mCreated) {
            bundle.putBundle(DIALOG_HIERARCHY_TAG, this.mWindow.saveHierarchyState());
        }
        return bundle;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Bundle dialogHierarchyState = savedInstanceState.getBundle(DIALOG_HIERARCHY_TAG);
        if (dialogHierarchyState != null) {
            dispatchOnCreate(savedInstanceState);
            this.mWindow.restoreHierarchyState(dialogHierarchyState);
            if (savedInstanceState.getBoolean(DIALOG_SHOWING_TAG)) {
                show();
            }
        }
    }

    public Window getWindow() {
        return this.mWindow;
    }

    public View getCurrentFocus() {
        Window window = this.mWindow;
        return window != null ? window.getCurrentFocus() : null;
    }

    public <T extends View> T findViewById(int id) {
        return this.mWindow.findViewById(id);
    }

    public final <T extends View> T requireViewById(int id) {
        T view = findViewById(id);
        if (view != null) {
            return view;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this Dialog");
    }

    public void setContentView(int layoutResID) {
        this.mWindow.setContentView(layoutResID);
    }

    public void setContentView(View view) {
        this.mWindow.setContentView(view);
    }

    public void setContentView(View view, ViewGroup.LayoutParams params) {
        this.mWindow.setContentView(view, params);
    }

    public void addContentView(View view, ViewGroup.LayoutParams params) {
        this.mWindow.addContentView(view, params);
    }

    public void setTitle(CharSequence title) {
        this.mWindow.setTitle(title);
        this.mWindow.getAttributes().setTitle(title);
    }

    public void setTitle(int titleId) {
        setTitle(this.mContext.getText(titleId));
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode != 4 && keyCode != 111) {
            return false;
        }
        event.startTracking();
        return true;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode != 4 && keyCode != 111) || !event.isTracking() || event.isCanceled()) {
            return false;
        }
        onBackPressed();
        return true;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    public void onBackPressed() {
        if (this.mCancelable) {
            cancel();
        }
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mCancelable || !this.mShowing || !this.mWindow.shouldCloseOnTouch(this.mContext, event)) {
            return false;
        }
        cancel();
        return true;
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }

    public void onWindowAttributesChanged(LayoutParams params) {
        View view = this.mDecor;
        if (view != null) {
            this.mWindowManager.updateViewLayout(view, params);
        }
    }

    public void onContentChanged() {
    }

    public void onWindowFocusChanged(boolean hasFocus) {
    }

    public void onAttachedToWindow() {
    }

    public void onDetachedFromWindow() {
    }

    public void onWindowDismissed(boolean finishTask, boolean suppressWindowTransition) {
        dismiss();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        OnKeyListener onKeyListener = this.mOnKeyListener;
        if ((onKeyListener != null && onKeyListener.onKey(this, event.getKeyCode(), event)) || this.mWindow.superDispatchKeyEvent(event)) {
            return true;
        }
        View view = this.mDecor;
        return event.dispatch(this, view != null ? view.getKeyDispatcherState() : null, this);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        if (this.mWindow.superDispatchKeyShortcutEvent(event)) {
            return true;
        }
        return onKeyShortcut(event.getKeyCode(), event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.mWindow.superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean dispatchTrackballEvent(MotionEvent ev) {
        if (this.mWindow.superDispatchTrackballEvent(ev)) {
            return true;
        }
        return onTrackballEvent(ev);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        if (this.mWindow.superDispatchGenericMotionEvent(ev)) {
            return true;
        }
        return onGenericMotionEvent(ev);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        event.setClassName(getClass().getName());
        event.setPackageName(this.mContext.getPackageName());
        ViewGroup.LayoutParams params = getWindow().getAttributes();
        boolean isFullScreen = params.width == -1 && params.height == -1;
        event.setFullScreen(isFullScreen);
        return false;
    }

    public View onCreatePanelView(int featureId) {
        return null;
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == 0) {
            return onCreateOptionsMenu(menu);
        }
        return false;
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        boolean z = true;
        if (featureId != 0) {
            return true;
        }
        if (!(onPrepareOptionsMenu(menu) && menu.hasVisibleItems())) {
            z = false;
        }
        return z;
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == 8) {
            this.mActionBar.dispatchMenuVisibilityChanged(true);
        }
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        return false;
    }

    public void onPanelClosed(int featureId, Menu menu) {
        if (featureId == 8) {
            this.mActionBar.dispatchMenuVisibilityChanged(false);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    public void onOptionsMenuClosed(Menu menu) {
    }

    public void openOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            this.mWindow.openPanel(0, null);
        }
    }

    public void closeOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            this.mWindow.closePanel(0);
        }
    }

    public void invalidateOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            this.mWindow.invalidatePanelMenu(0);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
    }

    public void registerForContextMenu(View view) {
        view.setOnCreateContextMenuListener(this);
    }

    public void unregisterForContextMenu(View view) {
        view.setOnCreateContextMenuListener(null);
    }

    public void openContextMenu(View view) {
        view.showContextMenu();
    }

    public boolean onContextItemSelected(MenuItem item) {
        return false;
    }

    public void onContextMenuClosed(Menu menu) {
    }

    public boolean onSearchRequested(SearchEvent searchEvent) {
        this.mSearchEvent = searchEvent;
        return onSearchRequested();
    }

    public boolean onSearchRequested() {
        SearchManager searchManager = (SearchManager) this.mContext.getSystemService("search");
        ComponentName appName = getAssociatedActivity();
        if (appName == null || searchManager.getSearchableInfo(appName) == null) {
            return false;
        }
        searchManager.startSearch(null, false, appName, null, false);
        dismiss();
        return true;
    }

    public final SearchEvent getSearchEvent() {
        return this.mSearchEvent;
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        ActionBar actionBar = this.mActionBar;
        if (actionBar == null || this.mActionModeTypeStarting != 0) {
            return null;
        }
        return actionBar.startActionMode(callback);
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
        try {
            this.mActionModeTypeStarting = type;
            ActionMode onWindowStartingActionMode = onWindowStartingActionMode(callback);
            return onWindowStartingActionMode;
        } finally {
            this.mActionModeTypeStarting = 0;
        }
    }

    public void onActionModeStarted(ActionMode mode) {
        this.mActionMode = mode;
    }

    public void onActionModeFinished(ActionMode mode) {
        if (mode == this.mActionMode) {
            this.mActionMode = null;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:12:0x0025  */
    /* JADX WARNING: Removed duplicated region for block: B:19:? A:{SYNTHETIC, RETURN} */
    private android.content.ComponentName getAssociatedActivity() {
        /*
        r4 = this;
        r0 = r4.mOwnerActivity;
        r1 = r4.getContext();
    L_0x0006:
        r2 = 0;
        if (r0 != 0) goto L_0x0022;
    L_0x0009:
        if (r1 == 0) goto L_0x0022;
    L_0x000b:
        r3 = r1 instanceof android.app.Activity;
        if (r3 == 0) goto L_0x0013;
    L_0x000f:
        r0 = r1;
        r0 = (android.app.Activity) r0;
        goto L_0x0006;
    L_0x0013:
        r3 = r1 instanceof android.content.ContextWrapper;
        if (r3 == 0) goto L_0x001f;
    L_0x0017:
        r2 = r1;
        r2 = (android.content.ContextWrapper) r2;
        r2 = r2.getBaseContext();
        goto L_0x0020;
    L_0x0020:
        r1 = r2;
        goto L_0x0006;
    L_0x0022:
        if (r0 != 0) goto L_0x0025;
    L_0x0024:
        goto L_0x0029;
    L_0x0025:
        r2 = r0.getComponentName();
    L_0x0029:
        return r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Dialog.getAssociatedActivity():android.content.ComponentName");
    }

    public void takeKeyEvents(boolean get) {
        this.mWindow.takeKeyEvents(get);
    }

    public final boolean requestWindowFeature(int featureId) {
        return getWindow().requestFeature(featureId);
    }

    public final void setFeatureDrawableResource(int featureId, int resId) {
        getWindow().setFeatureDrawableResource(featureId, resId);
    }

    public final void setFeatureDrawableUri(int featureId, Uri uri) {
        getWindow().setFeatureDrawableUri(featureId, uri);
    }

    public final void setFeatureDrawable(int featureId, Drawable drawable) {
        getWindow().setFeatureDrawable(featureId, drawable);
    }

    public final void setFeatureDrawableAlpha(int featureId, int alpha) {
        getWindow().setFeatureDrawableAlpha(featureId, alpha);
    }

    public LayoutInflater getLayoutInflater() {
        return getWindow().getLayoutInflater();
    }

    public void setCancelable(boolean flag) {
        this.mCancelable = flag;
        updateWindowForCancelable();
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        if (cancel && !this.mCancelable) {
            this.mCancelable = true;
            updateWindowForCancelable();
        }
        this.mWindow.setCloseOnTouchOutside(cancel);
    }

    public void cancel() {
        if (!this.mCanceled) {
            Message message = this.mCancelMessage;
            if (message != null) {
                this.mCanceled = true;
                Message.obtain(message).sendToTarget();
            }
        }
        dismiss();
    }

    public void setOnCancelListener(OnCancelListener listener) {
        if (this.mCancelAndDismissTaken != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("OnCancelListener is already taken by ");
            stringBuilder.append(this.mCancelAndDismissTaken);
            stringBuilder.append(" and can not be replaced.");
            throw new IllegalStateException(stringBuilder.toString());
        } else if (listener != null) {
            this.mCancelMessage = this.mListenersHandler.obtainMessage(68, listener);
        } else {
            this.mCancelMessage = null;
        }
    }

    public void setCancelMessage(Message msg) {
        this.mCancelMessage = msg;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        if (this.mCancelAndDismissTaken != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("OnDismissListener is already taken by ");
            stringBuilder.append(this.mCancelAndDismissTaken);
            stringBuilder.append(" and can not be replaced.");
            throw new IllegalStateException(stringBuilder.toString());
        } else if (listener != null) {
            this.mDismissMessage = this.mListenersHandler.obtainMessage(67, listener);
        } else {
            this.mDismissMessage = null;
        }
    }

    public void setOnShowListener(OnShowListener listener) {
        if (listener != null) {
            this.mShowMessage = this.mListenersHandler.obtainMessage(69, listener);
        } else {
            this.mShowMessage = null;
        }
    }

    public void setDismissMessage(Message msg) {
        this.mDismissMessage = msg;
    }

    public boolean takeCancelAndDismissListeners(String msg, OnCancelListener cancel, OnDismissListener dismiss) {
        if (this.mCancelAndDismissTaken != null) {
            this.mCancelAndDismissTaken = null;
        } else if (!(this.mCancelMessage == null && this.mDismissMessage == null)) {
            return false;
        }
        setOnCancelListener(cancel);
        setOnDismissListener(dismiss);
        this.mCancelAndDismissTaken = msg;
        return true;
    }

    public final void setVolumeControlStream(int streamType) {
        getWindow().setVolumeControlStream(streamType);
    }

    public final int getVolumeControlStream() {
        return getWindow().getVolumeControlStream();
    }

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        this.mOnKeyListener = onKeyListener;
    }

    private void updateWindowForCancelable() {
        this.mWindow.setCloseOnSwipeEnabled(this.mCancelable);
    }
}
