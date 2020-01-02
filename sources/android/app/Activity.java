package android.app;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager.TaskDescription;
import android.app.Application.ActivityLifecycleCallbacks;
import android.app.IRequestFinishCallback.Stub;
import android.app.Instrumentation.ActivityResult;
import android.app.PictureInPictureParams.Builder;
import android.app.VoiceInteractor.Request;
import android.app.assist.AssistContent;
import android.content.ComponentCallbacks2;
import android.content.ComponentName;
import android.content.Context;
import android.content.IIntentSender;
import android.content.Intent;
import android.content.IntentSender;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.session.MediaController;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.BadParcelableException;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.GraphicsEnvironment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.ServiceManager.ServiceNotFoundException;
import android.os.StrictMode;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.os.UserHandle;
import android.text.Selection;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.TextKeyListener;
import android.transition.Scene;
import android.transition.TransitionManager;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.util.EventLog;
import android.util.Log;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SuperNotCalledException;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ContextThemeWrapper;
import android.view.DragAndDropPermissions;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.KeyboardShortcutInfo;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory2;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.RemoteAnimationDefinition;
import android.view.SearchEvent;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewRootImpl;
import android.view.ViewRootImpl.ActivityConfigCallback;
import android.view.Window;
import android.view.Window.Callback;
import android.view.Window.OnWindowDismissedCallback;
import android.view.Window.WindowControllerCallback;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityEvent;
import android.view.autofill.AutofillId;
import android.view.autofill.AutofillManager;
import android.view.autofill.AutofillManager.AutofillClient;
import android.view.autofill.AutofillPopupWindow;
import android.view.autofill.Helper;
import android.view.autofill.IAutofillWindowPresenter;
import android.view.contentcapture.ContentCaptureManager;
import android.view.contentcapture.ContentCaptureManager.ContentCaptureClient;
import android.webkit.WebView;
import android.widget.Toast;
import android.widget.Toolbar;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.IVoiceInteractor;
import com.android.internal.app.ToolbarActionBar;
import com.android.internal.app.WindowDecorActionBar;
import com.android.internal.policy.MiuiPhoneWindow;
import com.miui.internal.contentcatcher.IInterceptor;
import com.miui.internal.search.Function;
import dalvik.system.VMRuntime;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import miui.contentcatcher.InterceptorProxy;
import org.apache.miui.commons.lang3.ClassUtils;

public class Activity extends ContextThemeWrapper implements Factory2, Callback, KeyEvent.Callback, OnCreateContextMenuListener, ComponentCallbacks2, OnWindowDismissedCallback, WindowControllerCallback, AutofillClient, ContentCaptureClient {
    private static final String AUTOFILL_RESET_NEEDED = "@android:autofillResetNeeded";
    private static final String AUTO_FILL_AUTH_WHO_PREFIX = "@android:autoFillAuth:";
    private static final int CONTENT_CAPTURE_PAUSE = 3;
    private static final int CONTENT_CAPTURE_RESUME = 2;
    private static final int CONTENT_CAPTURE_START = 1;
    private static final int CONTENT_CAPTURE_STOP = 4;
    private static final boolean DEBUG_LIFECYCLE = false;
    public static final int DEFAULT_KEYS_DIALER = 1;
    public static final int DEFAULT_KEYS_DISABLE = 0;
    public static final int DEFAULT_KEYS_SEARCH_GLOBAL = 4;
    public static final int DEFAULT_KEYS_SEARCH_LOCAL = 3;
    public static final int DEFAULT_KEYS_SHORTCUT = 2;
    public static final int DONT_FINISH_TASK_WITH_ACTIVITY = 0;
    public static final int FINISH_TASK_WITH_ACTIVITY = 2;
    public static final int FINISH_TASK_WITH_ROOT_ACTIVITY = 1;
    protected static final int[] FOCUSED_STATE_SET = new int[]{16842908};
    @UnsupportedAppUsage
    static final String FRAGMENTS_TAG = "android:fragments";
    private static final String HAS_CURENT_PERMISSIONS_REQUEST_KEY = "android:hasCurrentPermissionsRequest";
    private static final String KEYBOARD_SHORTCUTS_RECEIVER_PKG_NAME = "com.android.systemui";
    private static final String LAST_AUTOFILL_ID = "android:lastAutofillId";
    private static final int LOG_AM_ON_ACTIVITY_RESULT_CALLED = 30062;
    private static final int LOG_AM_ON_CREATE_CALLED = 30057;
    private static final int LOG_AM_ON_DESTROY_CALLED = 30060;
    private static final int LOG_AM_ON_PAUSE_CALLED = 30021;
    private static final int LOG_AM_ON_RESTART_CALLED = 30058;
    private static final int LOG_AM_ON_RESUME_CALLED = 30022;
    private static final int LOG_AM_ON_START_CALLED = 30059;
    private static final int LOG_AM_ON_STOP_CALLED = 30049;
    private static final int LOG_AM_ON_TOP_RESUMED_GAINED_CALLED = 30064;
    private static final int LOG_AM_ON_TOP_RESUMED_LOST_CALLED = 30065;
    private static final String REQUEST_PERMISSIONS_WHO_PREFIX = "@android:requestPermissions:";
    public static final int RESULT_CANCELED = 0;
    public static final int RESULT_FIRST_USER = 1;
    public static final int RESULT_OK = -1;
    private static final String SAVED_DIALOGS_TAG = "android:savedDialogs";
    private static final String SAVED_DIALOG_ARGS_KEY_PREFIX = "android:dialog_args_";
    private static final String SAVED_DIALOG_IDS_KEY = "android:savedDialogIds";
    private static final String SAVED_DIALOG_KEY_PREFIX = "android:dialog_";
    private static final long SLOW_ON_ACTIVITY_RESULT_THRESHOLD_MS = 100;
    private static final long SLOW_ON_CREATE_THRESHOLD_MS = 500;
    private static final long SLOW_ON_DESTROY_THRESHOLD_MS = 100;
    private static final long SLOW_ON_PAUSE_THRESHOLD_MS = 50;
    private static final long SLOW_ON_RESTART_THRESHOLD_MS = 100;
    private static final long SLOW_ON_RESUME_THRESHOLD_MS = 100;
    private static final long SLOW_ON_START_THRESHOLD_MS = 100;
    private static final long SLOW_ON_STOP_THRESHOLD_MS = 100;
    private static final String TAG = "Activity";
    private static final String WINDOW_HIERARCHY_TAG = "android:viewHierarchyState";
    ActionBar mActionBar = null;
    private int mActionModeTypeStarting = 0;
    @UnsupportedAppUsage
    ActivityInfo mActivityInfo;
    private final ArrayList<ActivityLifecycleCallbacks> mActivityLifecycleCallbacks = new ArrayList();
    @UnsupportedAppUsage
    ActivityTransitionState mActivityTransitionState = new ActivityTransitionState();
    @UnsupportedAppUsage
    private Application mApplication;
    private IBinder mAssistToken;
    private boolean mAutoFillIgnoreFirstResumePause;
    private boolean mAutoFillResetNeeded;
    private AutofillManager mAutofillManager;
    private AutofillPopupWindow mAutofillPopupWindow;
    @UnsupportedAppUsage
    boolean mCalled;
    private boolean mCanEnterPictureInPicture = false;
    private boolean mChangeCanvasToTranslucent;
    boolean mChangingConfigurations = false;
    @UnsupportedAppUsage
    private ComponentName mComponent;
    @UnsupportedAppUsage
    int mConfigChangeFlags;
    private ContentCaptureManager mContentCaptureManager;
    @UnsupportedAppUsage
    Configuration mCurrentConfig;
    View mDecor = null;
    private int mDefaultKeyMode = 0;
    private SpannableStringBuilder mDefaultKeySsb = null;
    @UnsupportedAppUsage
    private boolean mDestroyed;
    private boolean mDoReportFullyDrawn = true;
    @UnsupportedAppUsage
    String mEmbeddedID;
    private boolean mEnableDefaultActionBarUp;
    boolean mEnterAnimationComplete;
    SharedElementCallback mEnterTransitionListener = SharedElementCallback.NULL_CALLBACK;
    SharedElementCallback mExitTransitionListener = SharedElementCallback.NULL_CALLBACK;
    @UnsupportedAppUsage
    boolean mFinished;
    @UnsupportedAppUsage
    final FragmentController mFragments = FragmentController.createController(new HostCallbacks());
    @UnsupportedAppUsage
    final Handler mHandler = new Handler();
    private boolean mHasCurrentPermissionsRequest;
    @UnsupportedAppUsage
    private int mIdent;
    private final Object mInstanceTracker = StrictMode.trackActivity(this);
    @UnsupportedAppUsage
    private Instrumentation mInstrumentation;
    @UnsupportedAppUsage
    Intent mIntent;
    private IInterceptor mInterceptor = null;
    private int mLastAutofillId = View.LAST_APP_AUTOFILL_ID;
    @UnsupportedAppUsage
    NonConfigurationInstances mLastNonConfigurationInstances;
    @UnsupportedAppUsage
    ActivityThread mMainThread;
    @GuardedBy({"mManagedCursors"})
    private final ArrayList<ManagedCursor> mManagedCursors = new ArrayList();
    private SparseArray<ManagedDialog> mManagedDialogs;
    private MenuInflater mMenuInflater;
    @UnsupportedAppUsage
    Activity mParent;
    @UnsupportedAppUsage
    String mReferrer;
    private boolean mRestoredFromBundle;
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    int mResultCode = 0;
    @GuardedBy({"this"})
    @UnsupportedAppUsage
    Intent mResultData = null;
    @UnsupportedAppUsage
    boolean mResumed;
    private SearchEvent mSearchEvent;
    private SearchManager mSearchManager;
    boolean mStartedActivity;
    @UnsupportedAppUsage
    boolean mStopped;
    private TaskDescription mTaskDescription = new TaskDescription();
    @UnsupportedAppUsage
    private CharSequence mTitle;
    private int mTitleColor = 0;
    private boolean mTitleReady = false;
    @UnsupportedAppUsage
    private IBinder mToken;
    private TranslucentConversionListener mTranslucentCallback;
    private Thread mUiThread;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    boolean mVisibleFromClient = true;
    boolean mVisibleFromServer = false;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    VoiceInteractor mVoiceInteractor;
    @UnsupportedAppUsage
    private Window mWindow;
    @UnsupportedAppUsage
    boolean mWindowAdded = false;
    @UnsupportedAppUsage
    private WindowManager mWindowManager;

    @Retention(RetentionPolicy.SOURCE)
    @interface ContentCaptureNotificationType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @interface DefaultKeyMode {
    }

    class HostCallbacks extends FragmentHostCallback<Activity> {
        public HostCallbacks() {
            super(Activity.this);
        }

        public void onDump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
            Activity.this.dump(prefix, fd, writer, args);
        }

        public boolean onShouldSaveFragmentState(Fragment fragment) {
            return Activity.this.isFinishing() ^ 1;
        }

        public LayoutInflater onGetLayoutInflater() {
            LayoutInflater result = Activity.this.getLayoutInflater();
            if (onUseFragmentManagerInflaterFactory()) {
                return result.cloneInContext(Activity.this);
            }
            return result;
        }

        public boolean onUseFragmentManagerInflaterFactory() {
            return Activity.this.getApplicationInfo().targetSdkVersion >= 21;
        }

        public Activity onGetHost() {
            return Activity.this;
        }

        public void onInvalidateOptionsMenu() {
            Activity.this.invalidateOptionsMenu();
        }

        public void onStartActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
            Activity.this.startActivityFromFragment(fragment, intent, requestCode, options);
        }

        public void onStartActivityAsUserFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options, UserHandle user) {
            Activity.this.startActivityAsUserFromFragment(fragment, intent, requestCode, options, user);
        }

        public void onStartIntentSenderFromFragment(Fragment fragment, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
            if (Activity.this.mParent == null) {
                Activity.this.startIntentSenderForResultInner(intent, fragment.mWho, requestCode, fillInIntent, flagsMask, flagsValues, options);
                return;
            }
            Fragment fragment2 = fragment;
            if (options != null) {
                Activity.this.mParent.startIntentSenderFromChildFragment(fragment, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
            }
        }

        public void onRequestPermissionsFromFragment(Fragment fragment, String[] permissions, int requestCode) {
            String who = new StringBuilder();
            who.append(Activity.REQUEST_PERMISSIONS_WHO_PREFIX);
            who.append(fragment.mWho);
            Activity.this.startActivityForResult(who.toString(), Activity.this.getPackageManager().buildRequestPermissionsIntent(permissions), requestCode, null);
        }

        public boolean onHasWindowAnimations() {
            return Activity.this.getWindow() != null;
        }

        public int onGetWindowAnimations() {
            Window w = Activity.this.getWindow();
            return w == null ? 0 : w.getAttributes().windowAnimations;
        }

        public void onAttachFragment(Fragment fragment) {
            Activity.this.onAttachFragment(fragment);
        }

        public <T extends View> T onFindViewById(int id) {
            return Activity.this.findViewById(id);
        }

        public boolean onHasView() {
            Window w = Activity.this.getWindow();
            return (w == null || w.peekDecorView() == null) ? false : true;
        }
    }

    private static final class ManagedCursor {
        private final Cursor mCursor;
        private boolean mReleased = false;
        private boolean mUpdated = false;

        ManagedCursor(Cursor cursor) {
            this.mCursor = cursor;
        }
    }

    private static class ManagedDialog {
        Bundle mArgs;
        Dialog mDialog;

        private ManagedDialog() {
        }

        /* synthetic */ ManagedDialog(AnonymousClass1 x0) {
            this();
        }
    }

    static final class NonConfigurationInstances {
        Object activity;
        HashMap<String, Object> children;
        FragmentManagerNonConfig fragments;
        ArrayMap<String, LoaderManager> loaders;
        VoiceInteractor voiceInteractor;

        NonConfigurationInstances() {
        }
    }

    @SystemApi
    public interface TranslucentConversionListener {
        void onTranslucentConversionComplete(boolean z);
    }

    private static native String getDlWarning();

    public IInterceptor getInterceptor() {
        return this.mInterceptor;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public void setIntent(Intent newIntent) {
        this.mIntent = newIntent;
    }

    public final Application getApplication() {
        return this.mApplication;
    }

    public final boolean isChild() {
        return this.mParent != null;
    }

    public final Activity getParent() {
        return this.mParent;
    }

    public WindowManager getWindowManager() {
        return this.mWindowManager;
    }

    public Window getWindow() {
        return this.mWindow;
    }

    @Deprecated
    public LoaderManager getLoaderManager() {
        return this.mFragments.getLoaderManager();
    }

    public View getCurrentFocus() {
        Window window = this.mWindow;
        return window != null ? window.getCurrentFocus() : null;
    }

    private AutofillManager getAutofillManager() {
        if (this.mAutofillManager == null) {
            this.mAutofillManager = (AutofillManager) getSystemService(AutofillManager.class);
        }
        return this.mAutofillManager;
    }

    private ContentCaptureManager getContentCaptureManager() {
        if (!UserHandle.isApp(Process.myUid())) {
            return null;
        }
        if (this.mContentCaptureManager == null) {
            this.mContentCaptureManager = (ContentCaptureManager) getSystemService(ContentCaptureManager.class);
        }
        return this.mContentCaptureManager;
    }

    private String getContentCaptureTypeAsString(int type) {
        if (type == 1) {
            return "START";
        }
        if (type == 2) {
            return "RESUME";
        }
        if (type == 3) {
            return "PAUSE";
        }
        if (type == 4) {
            return "STOP";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UNKNOW-");
        stringBuilder.append(type);
        return stringBuilder.toString();
    }

    private void notifyContentCaptureManagerIfNeeded(int type) {
        if (Trace.isTagEnabled(64)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("notifyContentCapture(");
            stringBuilder.append(getContentCaptureTypeAsString(type));
            stringBuilder.append(") for ");
            stringBuilder.append(this.mComponent.toShortString());
            Trace.traceBegin(64, stringBuilder.toString());
        }
        try {
            ContentCaptureManager cm = getContentCaptureManager();
            if (cm != null) {
                if (type == 1) {
                    Window window = getWindow();
                    if (window != null) {
                        cm.updateWindowAttributes(window.getAttributes());
                    }
                    cm.onActivityCreated(this.mToken, getComponentName());
                } else if (type == 2) {
                    cm.onActivityResumed();
                } else if (type == 3) {
                    cm.onActivityPaused();
                } else if (type != 4) {
                    String str = TAG;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Invalid @ContentCaptureNotificationType: ");
                    stringBuilder2.append(type);
                    Log.wtf(str, stringBuilder2.toString());
                } else {
                    cm.onActivityDestroyed();
                }
                Trace.traceEnd(64);
            }
        } finally {
            Trace.traceEnd(64);
        }
    }

    /* Access modifiers changed, original: protected */
    public void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        if (newBase != null) {
            newBase.setAutofillClient(this);
            newBase.setContentCaptureOptions(getContentCaptureOptions());
        }
    }

    public final AutofillClient getAutofillClient() {
        return this;
    }

    public final ContentCaptureClient getContentCaptureClient() {
        return this;
    }

    public void registerActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.add(callback);
        }
    }

    public void unregisterActivityLifecycleCallbacks(ActivityLifecycleCallbacks callback) {
        synchronized (this.mActivityLifecycleCallbacks) {
            this.mActivityLifecycleCallbacks.remove(callback);
        }
    }

    private void dispatchActivityPreCreated(Bundle savedInstanceState) {
        getApplication().dispatchActivityPreCreated(this, savedInstanceState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPreCreated(this, savedInstanceState);
            }
        }
    }

    private void dispatchActivityCreated(Bundle savedInstanceState) {
        getApplication().dispatchActivityCreated(this, savedInstanceState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityCreated(this, savedInstanceState);
            }
        }
    }

    private void dispatchActivityPostCreated(Bundle savedInstanceState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPostCreated(this, savedInstanceState);
            }
        }
        getApplication().dispatchActivityPostCreated(this, savedInstanceState);
    }

    private void dispatchActivityPreStarted() {
        getApplication().dispatchActivityPreStarted(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPreStarted(this);
            }
        }
    }

    private void dispatchActivityStarted() {
        getApplication().dispatchActivityStarted(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityStarted(this);
            }
        }
    }

    private void dispatchActivityPostStarted() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPostStarted(this);
            }
        }
        getApplication().dispatchActivityPostStarted(this);
    }

    private void dispatchActivityPreResumed() {
        getApplication().dispatchActivityPreResumed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPreResumed(this);
            }
        }
    }

    private void dispatchActivityResumed() {
        getApplication().dispatchActivityResumed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityResumed(this);
            }
        }
    }

    private void dispatchActivityPostResumed() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (Object obj : callbacks) {
                ((ActivityLifecycleCallbacks) obj).onActivityPostResumed(this);
            }
        }
        getApplication().dispatchActivityPostResumed(this);
    }

    private void dispatchActivityPrePaused() {
        getApplication().dispatchActivityPrePaused(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityPrePaused(this);
            }
        }
    }

    private void dispatchActivityPaused() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityPaused(this);
            }
        }
        getApplication().dispatchActivityPaused(this);
    }

    private void dispatchActivityPostPaused() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityPostPaused(this);
            }
        }
        getApplication().dispatchActivityPostPaused(this);
    }

    private void dispatchActivityPreStopped() {
        getApplication().dispatchActivityPreStopped(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityPreStopped(this);
            }
        }
    }

    private void dispatchActivityStopped() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityStopped(this);
            }
        }
        getApplication().dispatchActivityStopped(this);
    }

    private void dispatchActivityPostStopped() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityPostStopped(this);
            }
        }
        getApplication().dispatchActivityPostStopped(this);
    }

    private void dispatchActivityPreSaveInstanceState(Bundle outState) {
        getApplication().dispatchActivityPreSaveInstanceState(this, outState);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityPreSaveInstanceState(this, outState);
            }
        }
    }

    private void dispatchActivitySaveInstanceState(Bundle outState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivitySaveInstanceState(this, outState);
            }
        }
        getApplication().dispatchActivitySaveInstanceState(this, outState);
    }

    private void dispatchActivityPostSaveInstanceState(Bundle outState) {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityPostSaveInstanceState(this, outState);
            }
        }
        getApplication().dispatchActivityPostSaveInstanceState(this, outState);
    }

    private void dispatchActivityPreDestroyed() {
        getApplication().dispatchActivityPreDestroyed(this);
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityPreDestroyed(this);
            }
        }
    }

    private void dispatchActivityDestroyed() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityDestroyed(this);
            }
        }
        getApplication().dispatchActivityDestroyed(this);
    }

    private void dispatchActivityPostDestroyed() {
        Object[] callbacks = collectActivityLifecycleCallbacks();
        if (callbacks != null) {
            for (int i = callbacks.length - 1; i >= 0; i--) {
                ((ActivityLifecycleCallbacks) callbacks[i]).onActivityPostDestroyed(this);
            }
        }
        getApplication().dispatchActivityPostDestroyed(this);
    }

    private Object[] collectActivityLifecycleCallbacks() {
        Object[] callbacks = null;
        synchronized (this.mActivityLifecycleCallbacks) {
            if (this.mActivityLifecycleCallbacks.size() > 0) {
                callbacks = this.mActivityLifecycleCallbacks.toArray();
            }
        }
        return callbacks;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        NonConfigurationInstances nonConfigurationInstances = this.mLastNonConfigurationInstances;
        if (nonConfigurationInstances != null) {
            this.mFragments.restoreLoaderNonConfig(nonConfigurationInstances.loaders);
        }
        if (this.mActivityInfo.parentActivityName != null) {
            ActionBar actionBar = this.mActionBar;
            if (actionBar == null) {
                this.mEnableDefaultActionBarUp = true;
            } else {
                actionBar.setDefaultDisplayHomeAsUpEnabled(true);
            }
        }
        boolean z = false;
        if (savedInstanceState != null) {
            this.mAutoFillResetNeeded = savedInstanceState.getBoolean(AUTOFILL_RESET_NEEDED, false);
            this.mLastAutofillId = savedInstanceState.getInt(LAST_AUTOFILL_ID, View.LAST_APP_AUTOFILL_ID);
            if (this.mAutoFillResetNeeded) {
                getAutofillManager().onCreate(savedInstanceState);
            }
            Parcelable p = savedInstanceState.getParcelable(FRAGMENTS_TAG);
            FragmentController fragmentController = this.mFragments;
            NonConfigurationInstances nonConfigurationInstances2 = this.mLastNonConfigurationInstances;
            fragmentController.restoreAllState(p, nonConfigurationInstances2 != null ? nonConfigurationInstances2.fragments : null);
        }
        this.mFragments.dispatchCreate();
        dispatchActivityCreated(savedInstanceState);
        VoiceInteractor voiceInteractor = this.mVoiceInteractor;
        if (voiceInteractor != null) {
            voiceInteractor.attachActivity(this);
        }
        if (savedInstanceState != null) {
            z = true;
        }
        this.mRestoredFromBundle = z;
        this.mCalled = true;
        if (this.mInterceptor == null) {
            this.mInterceptor = InterceptorProxy.create(this);
        }
        IInterceptor iInterceptor = this.mInterceptor;
        if (iInterceptor != null) {
            iInterceptor.notifyActivityCreate();
        }
    }

    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        onCreate(savedInstanceState);
    }

    /* Access modifiers changed, original: final */
    public final void performRestoreInstanceState(Bundle savedInstanceState) {
        onRestoreInstanceState(savedInstanceState);
        restoreManagedDialogs(savedInstanceState);
    }

    /* Access modifiers changed, original: final */
    public final void performRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        onRestoreInstanceState(savedInstanceState, persistentState);
        if (savedInstanceState != null) {
            restoreManagedDialogs(savedInstanceState);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (this.mWindow != null) {
            Bundle windowState = savedInstanceState.getBundle(WINDOW_HIERARCHY_TAG);
            if (windowState != null) {
                this.mWindow.restoreHierarchyState(windowState);
            }
        }
    }

    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }
    }

    private void restoreManagedDialogs(Bundle savedInstanceState) {
        Bundle b = savedInstanceState.getBundle(SAVED_DIALOGS_TAG);
        if (b != null) {
            this.mManagedDialogs = new SparseArray(numDialogs);
            for (Integer dialogId : b.getIntArray(SAVED_DIALOG_IDS_KEY)) {
                Integer dialogId2 = Integer.valueOf(dialogId2);
                Bundle dialogState = b.getBundle(savedDialogKeyFor(dialogId2.intValue()));
                if (dialogState != null) {
                    ManagedDialog md = new ManagedDialog();
                    md.mArgs = b.getBundle(savedDialogArgsKeyFor(dialogId2.intValue()));
                    md.mDialog = createDialog(dialogId2, dialogState, md.mArgs);
                    if (md.mDialog != null) {
                        this.mManagedDialogs.put(dialogId2.intValue(), md);
                        onPrepareDialog(dialogId2.intValue(), md.mDialog, md.mArgs);
                        md.mDialog.onRestoreInstanceState(dialogState);
                    }
                }
            }
        }
    }

    private Dialog createDialog(Integer dialogId, Bundle state, Bundle args) {
        Dialog dialog = onCreateDialog(dialogId.intValue(), args);
        if (dialog == null) {
            return null;
        }
        dialog.dispatchOnCreate(state);
        return dialog;
    }

    private static String savedDialogKeyFor(int key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SAVED_DIALOG_KEY_PREFIX);
        stringBuilder.append(key);
        return stringBuilder.toString();
    }

    private static String savedDialogArgsKeyFor(int key) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(SAVED_DIALOG_ARGS_KEY_PREFIX);
        stringBuilder.append(key);
        return stringBuilder.toString();
    }

    /* Access modifiers changed, original: protected */
    public void onPostCreate(Bundle savedInstanceState) {
        if (!isChild()) {
            this.mTitleReady = true;
            onTitleChanged(getTitle(), getTitleColor());
        }
        this.mCalled = true;
        notifyContentCaptureManagerIfNeeded(1);
    }

    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        onPostCreate(savedInstanceState);
    }

    /* Access modifiers changed, original: protected */
    public void onStart() {
        this.mCalled = true;
        this.mFragments.doLoaderStart();
        dispatchActivityStarted();
        if (this.mAutoFillResetNeeded) {
            getAutofillManager().onVisibleForAutofill();
        }
        IInterceptor iInterceptor = this.mInterceptor;
        if (iInterceptor != null) {
            iInterceptor.notifyActivityStart();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onRestart() {
        this.mCalled = true;
    }

    @Deprecated
    public void onStateNotSaved() {
    }

    /* Access modifiers changed, original: protected */
    public void onResume() {
        dispatchActivityResumed();
        this.mActivityTransitionState.onResume(this);
        enableAutofillCompatibilityIfNeeded();
        if (this.mAutoFillResetNeeded && !this.mAutoFillIgnoreFirstResumePause) {
            View focus = getCurrentFocus();
            if (focus != null && focus.canNotifyAutofillEnterExitEvent()) {
                getAutofillManager().notifyViewEntered(focus);
            }
        }
        notifyContentCaptureManagerIfNeeded(2);
        this.mCalled = true;
        IInterceptor iInterceptor = this.mInterceptor;
        if (iInterceptor != null) {
            iInterceptor.notifyActivityResume();
        }
        ActivityInjector.checkAccessControl(this);
    }

    /* Access modifiers changed, original: protected */
    public void onPostResume() {
        Window win = getWindow();
        if (win != null) {
            win.makeActive();
        }
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(true);
        }
        this.mCalled = true;
    }

    public void onTopResumedActivityChanged(boolean isTopResumedActivity) {
    }

    /* Access modifiers changed, original: final */
    public final void performTopResumedActivityChanged(boolean isTopResumedActivity, String reason) {
        onTopResumedActivityChanged(isTopResumedActivity);
        writeEventLog(isTopResumedActivity ? LOG_AM_ON_TOP_RESUMED_GAINED_CALLED : LOG_AM_ON_TOP_RESUMED_LOST_CALLED, reason);
    }

    /* Access modifiers changed, original: 0000 */
    public void setVoiceInteractor(IVoiceInteractor voiceInteractor) {
        Request[] requests = this.mVoiceInteractor;
        if (!(requests == null || requests.getActiveRequests() == null)) {
            for (Request activeRequest : this.mVoiceInteractor.getActiveRequests()) {
                activeRequest.cancel();
                activeRequest.clear();
            }
        }
        if (voiceInteractor == null) {
            this.mVoiceInteractor = null;
        } else {
            this.mVoiceInteractor = new VoiceInteractor(voiceInteractor, this, this, Looper.myLooper());
        }
    }

    public int getNextAutofillId() {
        if (this.mLastAutofillId == 2147483646) {
            this.mLastAutofillId = View.LAST_APP_AUTOFILL_ID;
        }
        this.mLastAutofillId++;
        return this.mLastAutofillId;
    }

    public AutofillId autofillClientGetNextAutofillId() {
        return new AutofillId(getNextAutofillId());
    }

    public boolean isVoiceInteraction() {
        return this.mVoiceInteractor != null;
    }

    public boolean isVoiceInteractionRoot() {
        boolean z = false;
        try {
            if (this.mVoiceInteractor != null && ActivityTaskManager.getService().isRootVoiceInteraction(this.mToken)) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            return false;
        }
    }

    public VoiceInteractor getVoiceInteractor() {
        return this.mVoiceInteractor;
    }

    public boolean isLocalVoiceInteractionSupported() {
        try {
            return ActivityTaskManager.getService().supportsLocalVoiceInteraction();
        } catch (RemoteException e) {
            return false;
        }
    }

    public void startLocalVoiceInteraction(Bundle privateOptions) {
        try {
            ActivityTaskManager.getService().startLocalVoiceInteraction(this.mToken, privateOptions);
        } catch (RemoteException e) {
        }
    }

    public void onLocalVoiceInteractionStarted() {
    }

    public void onLocalVoiceInteractionStopped() {
    }

    public void stopLocalVoiceInteraction() {
        try {
            ActivityTaskManager.getService().stopLocalVoiceInteraction(this.mToken);
        } catch (RemoteException e) {
        }
    }

    /* Access modifiers changed, original: protected */
    public void onNewIntent(Intent intent) {
    }

    /* Access modifiers changed, original: final */
    public final void performSaveInstanceState(Bundle outState) {
        dispatchActivityPreSaveInstanceState(outState);
        onSaveInstanceState(outState);
        saveManagedDialogs(outState);
        this.mActivityTransitionState.saveState(outState);
        storeHasCurrentPermissionRequest(outState);
        dispatchActivityPostSaveInstanceState(outState);
    }

    /* Access modifiers changed, original: final */
    public final void performSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        dispatchActivityPreSaveInstanceState(outState);
        onSaveInstanceState(outState, outPersistentState);
        saveManagedDialogs(outState);
        storeHasCurrentPermissionRequest(outState);
        dispatchActivityPostSaveInstanceState(outState);
    }

    /* Access modifiers changed, original: protected */
    public void onSaveInstanceState(Bundle outState) {
        outState.putBundle(WINDOW_HIERARCHY_TAG, this.mWindow.saveHierarchyState());
        outState.putInt(LAST_AUTOFILL_ID, this.mLastAutofillId);
        Parcelable p = this.mFragments.saveAllState();
        if (p != null) {
            outState.putParcelable(FRAGMENTS_TAG, p);
        }
        if (this.mAutoFillResetNeeded) {
            outState.putBoolean(AUTOFILL_RESET_NEEDED, true);
            getAutofillManager().onSaveInstanceState(outState);
        }
        dispatchActivitySaveInstanceState(outState);
    }

    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        onSaveInstanceState(outState);
    }

    @UnsupportedAppUsage
    private void saveManagedDialogs(Bundle outState) {
        int numDialogs = this.mManagedDialogs;
        if (numDialogs != 0) {
            numDialogs = numDialogs.size();
            if (numDialogs != 0) {
                Bundle dialogState = new Bundle();
                int[] ids = new int[this.mManagedDialogs.size()];
                for (int i = 0; i < numDialogs; i++) {
                    int key = this.mManagedDialogs.keyAt(i);
                    ids[i] = key;
                    ManagedDialog md = (ManagedDialog) this.mManagedDialogs.valueAt(i);
                    dialogState.putBundle(savedDialogKeyFor(key), md.mDialog.onSaveInstanceState());
                    if (md.mArgs != null) {
                        dialogState.putBundle(savedDialogArgsKeyFor(key), md.mArgs);
                    }
                }
                dialogState.putIntArray(SAVED_DIALOG_IDS_KEY, ids);
                outState.putBundle(SAVED_DIALOGS_TAG, dialogState);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPause() {
        dispatchActivityPaused();
        if (this.mAutoFillResetNeeded) {
            if (this.mAutoFillIgnoreFirstResumePause) {
                this.mAutoFillIgnoreFirstResumePause = false;
            } else {
                View focus = getCurrentFocus();
                if (focus != null && focus.canNotifyAutofillEnterExitEvent()) {
                    getAutofillManager().notifyViewExited(focus);
                }
            }
        }
        notifyContentCaptureManagerIfNeeded(3);
        this.mCalled = true;
        IInterceptor iInterceptor = this.mInterceptor;
        if (iInterceptor != null) {
            iInterceptor.notifyActivityPause();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onUserLeaveHint() {
    }

    @Deprecated
    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
        return false;
    }

    public CharSequence onCreateDescription() {
        return null;
    }

    public void onProvideAssistData(Bundle data) {
    }

    public void onProvideAssistContent(AssistContent outContent) {
    }

    public void onGetDirectActions(CancellationSignal cancellationSignal, Consumer<List<DirectAction>> callback) {
        callback.accept(Collections.emptyList());
    }

    public void onPerformDirectAction(String actionId, Bundle arguments, CancellationSignal cancellationSignal, Consumer<Bundle> consumer) {
    }

    public final void requestShowKeyboardShortcuts() {
        Intent intent = new Intent(Intent.ACTION_SHOW_KEYBOARD_SHORTCUTS);
        intent.setPackage(KEYBOARD_SHORTCUTS_RECEIVER_PKG_NAME);
        sendBroadcastAsUser(intent, Process.myUserHandle());
    }

    public final void dismissKeyboardShortcutsHelper() {
        Intent intent = new Intent(Intent.ACTION_DISMISS_KEYBOARD_SHORTCUTS);
        intent.setPackage(KEYBOARD_SHORTCUTS_RECEIVER_PKG_NAME);
        sendBroadcastAsUser(intent, Process.myUserHandle());
    }

    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> data, Menu menu, int deviceId) {
        if (menu != null) {
            KeyboardShortcutGroup group = null;
            int menuSize = menu.size();
            for (int i = 0; i < menuSize; i++) {
                MenuItem item = menu.getItem(i);
                CharSequence title = item.getTitle();
                char alphaShortcut = item.getAlphabeticShortcut();
                int alphaModifiers = item.getAlphabeticModifiers();
                if (!(title == null || alphaShortcut == 0)) {
                    if (group == null) {
                        int resource = this.mApplication.getApplicationInfo().labelRes;
                        group = new KeyboardShortcutGroup(resource != 0 ? getString(resource) : null);
                    }
                    group.addItem(new KeyboardShortcutInfo(title, alphaShortcut, alphaModifiers));
                }
            }
            if (group != null) {
                data.add(group);
            }
        }
    }

    public boolean showAssist(Bundle args) {
        try {
            return ActivityTaskManager.getService().showAssistFromActivity(this.mToken, args);
        } catch (RemoteException e) {
            return false;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onStop() {
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.setShowHideAnimationEnabled(false);
        }
        this.mActivityTransitionState.onStop();
        dispatchActivityStopped();
        this.mTranslucentCallback = null;
        this.mCalled = true;
        if (this.mAutoFillResetNeeded) {
            getAutofillManager().onInvisibleForAutofill();
        }
        if (isFinishing()) {
            if (this.mAutoFillResetNeeded) {
                getAutofillManager().onActivityFinishing();
            } else {
                Intent intent = this.mIntent;
                if (intent != null) {
                    String str = AutofillManager.EXTRA_RESTORE_SESSION_TOKEN;
                    if (intent.hasExtra(str)) {
                        getAutofillManager().onPendingSaveUi(1, this.mIntent.getIBinderExtra(str));
                    }
                }
            }
        }
        this.mEnterAnimationComplete = false;
        IInterceptor iInterceptor = this.mInterceptor;
        if (iInterceptor != null) {
            iInterceptor.notifyActivityStop();
        }
    }

    /* Access modifiers changed, original: protected */
    public void onDestroy() {
        int i;
        this.mCalled = true;
        int numDialogs = this.mManagedDialogs;
        if (numDialogs != 0) {
            numDialogs = numDialogs.size();
            for (i = 0; i < numDialogs; i++) {
                ManagedDialog md = (ManagedDialog) this.mManagedDialogs.valueAt(i);
                if (md.mDialog.isShowing()) {
                    md.mDialog.dismiss();
                }
            }
            this.mManagedDialogs = null;
        }
        synchronized (this.mManagedCursors) {
            i = this.mManagedCursors.size();
            for (int i2 = 0; i2 < i; i2++) {
                ManagedCursor c = (ManagedCursor) this.mManagedCursors.get(i2);
                if (c != null) {
                    c.mCursor.close();
                }
            }
            this.mManagedCursors.clear();
        }
        SearchManager searchManager = this.mSearchManager;
        if (searchManager != null) {
            searchManager.stopSearch();
        }
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.onDestroy();
        }
        dispatchActivityDestroyed();
        notifyContentCaptureManagerIfNeeded(4);
        IInterceptor iInterceptor = this.mInterceptor;
        if (iInterceptor != null) {
            iInterceptor.notifyActivityDestroy();
        }
    }

    public void reportFullyDrawn() {
        if (this.mDoReportFullyDrawn) {
            this.mDoReportFullyDrawn = false;
            try {
                ActivityTaskManager.getService().reportActivityFullyDrawn(this.mToken, this.mRestoredFromBundle);
                VMRuntime.getRuntime().notifyStartupCompleted();
            } catch (RemoteException e) {
            }
        }
    }

    public void onMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        onMultiWindowModeChanged(isInMultiWindowMode);
    }

    @Deprecated
    public void onMultiWindowModeChanged(boolean isInMultiWindowMode) {
    }

    public boolean isInMultiWindowMode() {
        try {
            return ActivityTaskManager.getService().isInMultiWindowMode(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        onPictureInPictureModeChanged(isInPictureInPictureMode);
    }

    @Deprecated
    public void onPictureInPictureModeChanged(boolean isInPictureInPictureMode) {
    }

    public boolean isInPictureInPictureMode() {
        try {
            return ActivityTaskManager.getService().isInPictureInPictureMode(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    @Deprecated
    public void enterPictureInPictureMode() {
        enterPictureInPictureMode(new Builder().build());
    }

    @Deprecated
    public boolean enterPictureInPictureMode(PictureInPictureArgs args) {
        return enterPictureInPictureMode(PictureInPictureArgs.convert(args));
    }

    public boolean enterPictureInPictureMode(PictureInPictureParams params) {
        try {
            if (!deviceSupportsPictureInPictureMode()) {
                return false;
            }
            if (params == null) {
                throw new IllegalArgumentException("Expected non-null picture-in-picture params");
            } else if (this.mCanEnterPictureInPicture) {
                return ActivityTaskManager.getService().enterPictureInPictureMode(this.mToken, params);
            } else {
                throw new IllegalStateException("Activity must be resumed to enter picture-in-picture");
            }
        } catch (RemoteException e) {
            return false;
        }
    }

    @Deprecated
    public void setPictureInPictureArgs(PictureInPictureArgs args) {
        setPictureInPictureParams(PictureInPictureArgs.convert(args));
    }

    public void setPictureInPictureParams(PictureInPictureParams params) {
        try {
            if (!deviceSupportsPictureInPictureMode()) {
                return;
            }
            if (params != null) {
                ActivityTaskManager.getService().setPictureInPictureParams(this.mToken, params);
                return;
            }
            throw new IllegalArgumentException("Expected non-null picture-in-picture params");
        } catch (RemoteException e) {
        }
    }

    public int getMaxNumPictureInPictureActions() {
        try {
            return ActivityTaskManager.getService().getMaxNumPictureInPictureActions(this.mToken);
        } catch (RemoteException e) {
            return 0;
        }
    }

    private boolean deviceSupportsPictureInPictureMode() {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_PICTURE_IN_PICTURE);
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchMovedToDisplay(int displayId, Configuration config) {
        updateDisplay(displayId);
        onMovedToDisplay(displayId, config);
    }

    public void onMovedToDisplay(int displayId, Configuration config) {
    }

    public void onConfigurationChanged(Configuration newConfig) {
        this.mCalled = true;
        this.mFragments.dispatchConfigurationChanged(newConfig);
        Window window = this.mWindow;
        if (window != null) {
            window.onConfigurationChanged(newConfig);
        }
        ActionBar actionBar = this.mActionBar;
        if (actionBar != null) {
            actionBar.onConfigurationChanged(newConfig);
        }
    }

    public int getChangingConfigurations() {
        return this.mConfigChangeFlags;
    }

    public Object getLastNonConfigurationInstance() {
        NonConfigurationInstances nonConfigurationInstances = this.mLastNonConfigurationInstances;
        return nonConfigurationInstances != null ? nonConfigurationInstances.activity : null;
    }

    public Object onRetainNonConfigurationInstance() {
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public HashMap<String, Object> getLastNonConfigurationChildInstances() {
        NonConfigurationInstances nonConfigurationInstances = this.mLastNonConfigurationInstances;
        return nonConfigurationInstances != null ? nonConfigurationInstances.children : null;
    }

    /* Access modifiers changed, original: 0000 */
    public HashMap<String, Object> onRetainNonConfigurationChildInstances() {
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public NonConfigurationInstances retainNonConfigurationInstances() {
        Object activity = onRetainNonConfigurationInstance();
        HashMap<String, Object> children = onRetainNonConfigurationChildInstances();
        FragmentManagerNonConfig fragments = this.mFragments.retainNestedNonConfig();
        this.mFragments.doLoaderStart();
        this.mFragments.doLoaderStop(true);
        ArrayMap<String, LoaderManager> loaders = this.mFragments.retainLoaderNonConfig();
        if (activity == null && children == null && fragments == null && loaders == null && this.mVoiceInteractor == null) {
            return null;
        }
        NonConfigurationInstances nci = new NonConfigurationInstances();
        nci.activity = activity;
        nci.children = children;
        nci.fragments = fragments;
        nci.loaders = loaders;
        VoiceInteractor voiceInteractor = this.mVoiceInteractor;
        if (voiceInteractor != null) {
            voiceInteractor.retainInstance();
            nci.voiceInteractor = this.mVoiceInteractor;
        }
        return nci;
    }

    public void onLowMemory() {
        this.mCalled = true;
        this.mFragments.dispatchLowMemory();
    }

    public void onTrimMemory(int level) {
        this.mCalled = true;
        this.mFragments.dispatchTrimMemory(level);
    }

    @Deprecated
    public FragmentManager getFragmentManager() {
        return this.mFragments.getFragmentManager();
    }

    @Deprecated
    public void onAttachFragment(Fragment fragment) {
    }

    @Deprecated
    @UnsupportedAppUsage
    public final Cursor managedQuery(Uri uri, String[] projection, String selection, String sortOrder) {
        Cursor c = getContentResolver().query(uri, projection, selection, null, sortOrder);
        if (c != null) {
            startManagingCursor(c);
        }
        return c;
    }

    @Deprecated
    public final Cursor managedQuery(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor c = getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
        if (c != null) {
            startManagingCursor(c);
        }
        return c;
    }

    @Deprecated
    public void startManagingCursor(Cursor c) {
        synchronized (this.mManagedCursors) {
            this.mManagedCursors.add(new ManagedCursor(c));
        }
    }

    @Deprecated
    public void stopManagingCursor(Cursor c) {
        synchronized (this.mManagedCursors) {
            int N = this.mManagedCursors.size();
            for (int i = 0; i < N; i++) {
                if (((ManagedCursor) this.mManagedCursors.get(i)).mCursor == c) {
                    this.mManagedCursors.remove(i);
                    break;
                }
            }
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public void setPersistent(boolean isPersistent) {
    }

    public <T extends View> T findViewById(int id) {
        return getWindow().findViewById(id);
    }

    public final <T extends View> T requireViewById(int id) {
        T view = findViewById(id);
        if (view != null) {
            return view;
        }
        throw new IllegalArgumentException("ID does not reference a View inside this Activity");
    }

    public ActionBar getActionBar() {
        initWindowDecorActionBar();
        return this.mActionBar;
    }

    public void setActionBar(Toolbar toolbar) {
        ActionBar ab = getActionBar();
        if (ab instanceof WindowDecorActionBar) {
            throw new IllegalStateException("This Activity already has an action bar supplied by the window decor. Do not request Window.FEATURE_ACTION_BAR and set android:windowActionBar to false in your theme to use a Toolbar instead.");
        }
        this.mMenuInflater = null;
        if (ab != null) {
            ab.onDestroy();
        }
        if (toolbar != null) {
            ToolbarActionBar tbab = new ToolbarActionBar(toolbar, getTitle(), this);
            this.mActionBar = tbab;
            this.mWindow.setCallback(tbab.getWrappedWindowCallback());
        } else {
            this.mActionBar = null;
            this.mWindow.setCallback(this);
        }
        invalidateOptionsMenu();
    }

    private void initWindowDecorActionBar() {
        Window window = getWindow();
        window.getDecorView();
        if (!isChild() && window.hasFeature(8) && this.mActionBar == null) {
            this.mActionBar = new WindowDecorActionBar(this);
            this.mActionBar.setDefaultDisplayHomeAsUpEnabled(this.mEnableDefaultActionBarUp);
            this.mWindow.setDefaultIcon(this.mActivityInfo.getIconResource());
            this.mWindow.setDefaultLogo(this.mActivityInfo.getLogoResource());
        }
    }

    public void setContentView(int layoutResID) {
        getWindow().setContentView(layoutResID);
        initWindowDecorActionBar();
    }

    public void setContentView(View view) {
        getWindow().setContentView(view);
        initWindowDecorActionBar();
    }

    public void setContentView(View view, LayoutParams params) {
        getWindow().setContentView(view, params);
        initWindowDecorActionBar();
    }

    public void addContentView(View view, LayoutParams params) {
        getWindow().addContentView(view, params);
        initWindowDecorActionBar();
    }

    public TransitionManager getContentTransitionManager() {
        return getWindow().getTransitionManager();
    }

    public void setContentTransitionManager(TransitionManager tm) {
        getWindow().setTransitionManager(tm);
    }

    public Scene getContentScene() {
        return getWindow().getContentScene();
    }

    public void setFinishOnTouchOutside(boolean finish) {
        this.mWindow.setCloseOnTouchOutside(finish);
    }

    public final void setDefaultKeyMode(int mode) {
        this.mDefaultKeyMode = mode;
        if (mode != 0) {
            if (mode != 1) {
                if (mode != 2) {
                    if (!(mode == 3 || mode == 4)) {
                        throw new IllegalArgumentException();
                    }
                }
            }
            this.mDefaultKeySsb = new SpannableStringBuilder();
            Selection.setSelection(this.mDefaultKeySsb, 0);
            return;
        }
        this.mDefaultKeySsb = null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            if (getApplicationInfo().targetSdkVersion >= 5) {
                event.startTracking();
            } else {
                onBackPressed();
            }
            return true;
        }
        int i = this.mDefaultKeyMode;
        if (i == 0) {
            return false;
        }
        if (i == 2) {
            Window w = getWindow();
            if (w.hasFeature(0) && w.performPanelShortcut(0, keyCode, event, 2)) {
                return true;
            }
            return false;
        } else if (keyCode == 61) {
            return false;
        } else {
            boolean handled;
            boolean clearSpannable = false;
            if (event.getRepeatCount() != 0 || event.isSystem()) {
                clearSpannable = true;
                handled = false;
            } else {
                handled = TextKeyListener.getInstance().onKeyDown(null, this.mDefaultKeySsb, keyCode, event);
                if (handled && this.mDefaultKeySsb.length() > 0) {
                    String str = this.mDefaultKeySsb.toString();
                    clearSpannable = true;
                    int i2 = this.mDefaultKeyMode;
                    if (i2 == 1) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(WebView.SCHEME_TEL);
                        stringBuilder.append(str);
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(stringBuilder.toString()));
                        intent.addFlags(268435456);
                        startActivity(intent);
                    } else if (i2 == 3) {
                        startSearch(str, false, null, false);
                    } else if (i2 == 4) {
                        startSearch(str, false, null, true);
                    }
                }
            }
            if (clearSpannable) {
                this.mDefaultKeySsb.clear();
                this.mDefaultKeySsb.clearSpans();
                Selection.setSelection(this.mDefaultKeySsb, 0);
            }
            return handled;
        }
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (getApplicationInfo().targetSdkVersion < 5 || keyCode != 4 || !event.isTracking() || event.isCanceled()) {
            return false;
        }
        onBackPressed();
        return true;
    }

    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        return false;
    }

    public void onBackPressed() {
        ActionBar actionBar = this.mActionBar;
        if (actionBar == null || !actionBar.collapseActionView()) {
            FragmentManager fragmentManager = this.mFragments.getFragmentManager();
            if (!fragmentManager.isStateSaved() && fragmentManager.popBackStackImmediate()) {
                return;
            }
            if (isTaskRoot()) {
                try {
                    ActivityTaskManager.getService().onBackPressedOnTaskRoot(this.mToken, new Stub() {
                        public /* synthetic */ void lambda$requestFinish$0$Activity$1() {
                            Activity.this.finishAfterTransition();
                        }

                        public void requestFinish() {
                            Activity.this.mHandler.post(new -$$Lambda$Activity$1$pR5b3qDyhldlD2RtkXoHHxgyGPU(this));
                        }
                    });
                } catch (RemoteException e) {
                    finishAfterTransition();
                }
                return;
            }
            finishAfterTransition();
        }
    }

    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        ActionBar actionBar = getActionBar();
        return actionBar != null && actionBar.onKeyShortcut(keyCode, event);
    }

    public boolean onTouchEvent(MotionEvent event) {
        if (!this.mWindow.shouldCloseOnTouch(this, event)) {
            return false;
        }
        finish();
        return true;
    }

    public boolean onTrackballEvent(MotionEvent event) {
        return false;
    }

    public boolean onGenericMotionEvent(MotionEvent event) {
        return false;
    }

    public void onUserInteraction() {
    }

    public void onWindowAttributesChanged(WindowManager.LayoutParams params) {
        if (this.mParent == null) {
            View decor = this.mDecor;
            if (decor != null && decor.getParent() != null) {
                getWindowManager().updateViewLayout(decor, params);
                ContentCaptureManager contentCaptureManager = this.mContentCaptureManager;
                if (contentCaptureManager != null) {
                    contentCaptureManager.updateWindowAttributes(params);
                }
            }
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

    public boolean hasWindowFocus() {
        Window w = getWindow();
        if (w != null) {
            View d = w.getDecorView();
            if (d != null) {
                return d.hasWindowFocus();
            }
        }
        return false;
    }

    public void onWindowDismissed(boolean finishTask, boolean suppressWindowTransition) {
        finish(finishTask ? 2 : 0);
        if (suppressWindowTransition) {
            overridePendingTransition(0, 0);
        }
    }

    public void toggleFreeformWindowingMode() throws RemoteException {
        ActivityTaskManager.getService().toggleFreeformWindowingMode(this.mToken);
    }

    public void enterPictureInPictureModeIfPossible() {
        if (this.mActivityInfo.supportsPictureInPicture()) {
            enterPictureInPictureMode();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        onUserInteraction();
        if (event.getKeyCode() == 82) {
            ActionBar actionBar = this.mActionBar;
            if (actionBar != null && actionBar.onMenuKeyEvent(event)) {
                return true;
            }
        }
        Window win = getWindow();
        if (win.superDispatchKeyEvent(event)) {
            return true;
        }
        View decor = this.mDecor;
        if (decor == null) {
            decor = win.getDecorView();
        }
        return event.dispatch(this, decor != null ? decor.getKeyDispatcherState() : null, this);
    }

    public boolean dispatchKeyShortcutEvent(KeyEvent event) {
        onUserInteraction();
        if (getWindow().superDispatchKeyShortcutEvent(event)) {
            return true;
        }
        return onKeyShortcut(event.getKeyCode(), event);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == 0) {
            onUserInteraction();
        }
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean dispatchTrackballEvent(MotionEvent ev) {
        onUserInteraction();
        if (getWindow().superDispatchTrackballEvent(ev)) {
            return true;
        }
        return onTrackballEvent(ev);
    }

    public boolean dispatchGenericMotionEvent(MotionEvent ev) {
        onUserInteraction();
        if (getWindow().superDispatchGenericMotionEvent(ev)) {
            return true;
        }
        return onGenericMotionEvent(ev);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        event.setClassName(getClass().getName());
        event.setPackageName(getPackageName());
        LayoutParams params = getWindow().getAttributes();
        boolean isFullScreen = params.width == -1 && params.height == -1;
        event.setFullScreen(isFullScreen);
        CharSequence title = getTitle();
        if (!TextUtils.isEmpty(title)) {
            event.getText().add(title);
        }
        return true;
    }

    public View onCreatePanelView(int featureId) {
        return null;
    }

    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        if (featureId == 0) {
            return onCreateOptionsMenu(menu) | this.mFragments.dispatchCreateOptionsMenu(menu, getMenuInflater());
        }
        return false;
    }

    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        if (featureId == 0) {
            return onPrepareOptionsMenu(menu) | this.mFragments.dispatchPrepareOptionsMenu(menu);
        }
        return true;
    }

    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == 8) {
            initWindowDecorActionBar();
            ActionBar actionBar = this.mActionBar;
            if (actionBar != null) {
                actionBar.dispatchMenuVisibilityChanged(true);
            } else {
                Log.e(TAG, "Tried to open action bar menu with no action bar");
            }
        }
        return true;
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        CharSequence titleCondensed = item.getTitleCondensed();
        if (featureId == 0) {
            if (titleCondensed != null) {
                EventLog.writeEvent(50000, Integer.valueOf(0), titleCondensed.toString());
            }
            if (onOptionsItemSelected(item) || this.mFragments.dispatchOptionsItemSelected(item)) {
                return true;
            }
            if (item.getItemId() == 16908332) {
                ActionBar actionBar = this.mActionBar;
                if (!(actionBar == null || (actionBar.getDisplayOptions() & 4) == 0)) {
                    Activity activity = this.mParent;
                    if (activity == null) {
                        return onNavigateUp();
                    }
                    return activity.onNavigateUpFromChild(this);
                }
            }
            return false;
        } else if (featureId != 6) {
            return false;
        } else {
            if (titleCondensed != null) {
                EventLog.writeEvent(50000, Integer.valueOf(1), titleCondensed.toString());
            }
            if (onContextItemSelected(item)) {
                return true;
            }
            return this.mFragments.dispatchContextItemSelected(item);
        }
    }

    public void onPanelClosed(int featureId, Menu menu) {
        if (featureId == 0) {
            this.mFragments.dispatchOptionsMenuClosed(menu);
            onOptionsMenuClosed(menu);
        } else if (featureId == 6) {
            onContextMenuClosed(menu);
        } else if (featureId == 8) {
            initWindowDecorActionBar();
            this.mActionBar.dispatchMenuVisibilityChanged(false);
        }
    }

    public void invalidateOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            ActionBar actionBar = this.mActionBar;
            if (actionBar == null || !actionBar.invalidateOptionsMenu()) {
                this.mWindow.invalidatePanelMenu(0);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Activity activity = this.mParent;
        if (activity != null) {
            return activity.onCreateOptionsMenu(menu);
        }
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        Activity activity = this.mParent;
        if (activity != null) {
            return activity.onPrepareOptionsMenu(menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Activity activity = this.mParent;
        if (activity != null) {
            return activity.onOptionsItemSelected(item);
        }
        return false;
    }

    public boolean onNavigateUp() {
        Intent upIntent = getParentActivityIntent();
        if (upIntent == null) {
            return false;
        }
        if (this.mActivityInfo.taskAffinity == null) {
            finish();
        } else if (shouldUpRecreateTask(upIntent)) {
            TaskStackBuilder b = TaskStackBuilder.create(this);
            onCreateNavigateUpTaskStack(b);
            onPrepareNavigateUpTaskStack(b);
            b.startActivities();
            if (this.mResultCode == 0 && this.mResultData == null) {
                finishAffinity();
            } else {
                Log.i(TAG, "onNavigateUp only finishing topmost activity to return a result");
                finish();
            }
        } else {
            navigateUpTo(upIntent);
        }
        return true;
    }

    public boolean onNavigateUpFromChild(Activity child) {
        return onNavigateUp();
    }

    public void onCreateNavigateUpTaskStack(TaskStackBuilder builder) {
        builder.addParentStack(this);
    }

    public void onPrepareNavigateUpTaskStack(TaskStackBuilder builder) {
    }

    public void onOptionsMenuClosed(Menu menu) {
        Activity activity = this.mParent;
        if (activity != null) {
            activity.onOptionsMenuClosed(menu);
        }
    }

    public void openOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            ActionBar actionBar = this.mActionBar;
            if (actionBar == null || !actionBar.openOptionsMenu()) {
                this.mWindow.openPanel(0, null);
            }
        }
    }

    public void closeOptionsMenu() {
        if (this.mWindow.hasFeature(0)) {
            ActionBar actionBar = this.mActionBar;
            if (actionBar == null || !actionBar.closeOptionsMenu()) {
                this.mWindow.closePanel(0);
            }
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

    public void closeContextMenu() {
        if (this.mWindow.hasFeature(6)) {
            this.mWindow.closePanel(6);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        Activity activity = this.mParent;
        if (activity != null) {
            return activity.onContextItemSelected(item);
        }
        return false;
    }

    public void onContextMenuClosed(Menu menu) {
        Activity activity = this.mParent;
        if (activity != null) {
            activity.onContextMenuClosed(menu);
        }
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public Dialog onCreateDialog(int id) {
        return null;
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public Dialog onCreateDialog(int id, Bundle args) {
        return onCreateDialog(id);
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public void onPrepareDialog(int id, Dialog dialog) {
        dialog.setOwnerActivity(this);
    }

    /* Access modifiers changed, original: protected */
    @Deprecated
    public void onPrepareDialog(int id, Dialog dialog, Bundle args) {
        onPrepareDialog(id, dialog);
    }

    @Deprecated
    public final void showDialog(int id) {
        showDialog(id, null);
    }

    @Deprecated
    public final boolean showDialog(int id, Bundle args) {
        if (this.mManagedDialogs == null) {
            this.mManagedDialogs = new SparseArray();
        }
        ManagedDialog md = (ManagedDialog) this.mManagedDialogs.get(id);
        if (md == null) {
            md = new ManagedDialog();
            md.mDialog = createDialog(Integer.valueOf(id), null, args);
            if (md.mDialog == null) {
                return false;
            }
            this.mManagedDialogs.put(id, md);
        }
        md.mArgs = args;
        onPrepareDialog(id, md.mDialog, args);
        md.mDialog.show();
        return true;
    }

    @Deprecated
    public final void dismissDialog(int id) {
        SparseArray sparseArray = this.mManagedDialogs;
        if (sparseArray != null) {
            ManagedDialog md = (ManagedDialog) sparseArray.get(id);
            if (md != null) {
                md.mDialog.dismiss();
                return;
            }
            throw missingDialog(id);
        }
        throw missingDialog(id);
    }

    private IllegalArgumentException missingDialog(int id) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("no dialog with id ");
        stringBuilder.append(id);
        stringBuilder.append(" was ever shown via Activity#showDialog");
        return new IllegalArgumentException(stringBuilder.toString());
    }

    @Deprecated
    public final void removeDialog(int id) {
        SparseArray sparseArray = this.mManagedDialogs;
        if (sparseArray != null) {
            ManagedDialog md = (ManagedDialog) sparseArray.get(id);
            if (md != null) {
                md.mDialog.dismiss();
                this.mManagedDialogs.remove(id);
            }
        }
    }

    public boolean onSearchRequested(SearchEvent searchEvent) {
        this.mSearchEvent = searchEvent;
        boolean result = onSearchRequested();
        this.mSearchEvent = null;
        return result;
    }

    public boolean onSearchRequested() {
        int uiMode = getResources().getConfiguration().uiMode & 15;
        if (uiMode == 4 || uiMode == 6) {
            return false;
        }
        startSearch(null, false, null, false);
        return true;
    }

    public final SearchEvent getSearchEvent() {
        return this.mSearchEvent;
    }

    public void startSearch(String initialQuery, boolean selectInitialQuery, Bundle appSearchData, boolean globalSearch) {
        ensureSearchManager();
        this.mSearchManager.startSearch(initialQuery, selectInitialQuery, getComponentName(), appSearchData, globalSearch);
    }

    public void triggerSearch(String query, Bundle appSearchData) {
        ensureSearchManager();
        this.mSearchManager.triggerSearch(query, getComponentName(), appSearchData);
    }

    public void takeKeyEvents(boolean get) {
        getWindow().takeKeyEvents(get);
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

    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            initWindowDecorActionBar();
            ActionBar actionBar = this.mActionBar;
            if (actionBar != null) {
                this.mMenuInflater = new MenuInflater(actionBar.getThemedContext(), this);
            } else {
                this.mMenuInflater = new MenuInflater(this);
            }
        }
        return this.mMenuInflater;
    }

    public void setTheme(int resid) {
        super.setTheme(resid);
        this.mWindow.setTheme(resid);
    }

    /* Access modifiers changed, original: protected */
    public void onApplyThemeResource(Theme theme, int resid, boolean first) {
        int colorPrimary;
        Activity activity = this.mParent;
        if (activity == null) {
            super.onApplyThemeResource(theme, resid, first);
        } else {
            try {
                theme.setTo(activity.getTheme());
            } catch (Exception e) {
            }
            theme.applyStyle(resid, false);
        }
        TypedArray a = theme.obtainStyledAttributes(R.styleable.ActivityTaskDescription);
        if (this.mTaskDescription.getPrimaryColor() == 0) {
            colorPrimary = a.getColor(1, 0);
            if (colorPrimary != 0 && Color.alpha(colorPrimary) == 255) {
                this.mTaskDescription.setPrimaryColor(colorPrimary);
            }
        }
        colorPrimary = a.getColor(0, 0);
        if (colorPrimary != 0 && Color.alpha(colorPrimary) == 255) {
            this.mTaskDescription.setBackgroundColor(colorPrimary);
        }
        int statusBarColor = a.getColor(2, 0);
        if (statusBarColor != 0) {
            this.mTaskDescription.setStatusBarColor(statusBarColor);
        }
        int navigationBarColor = a.getColor(3, 0);
        if (navigationBarColor != 0) {
            this.mTaskDescription.setNavigationBarColor(navigationBarColor);
        }
        if (!(getApplicationInfo().targetSdkVersion < 29)) {
            this.mTaskDescription.setEnsureStatusBarContrastWhenTransparent(a.getBoolean(4, false));
            this.mTaskDescription.setEnsureNavigationBarContrastWhenTransparent(a.getBoolean(5, true));
        }
        a.recycle();
        setTaskDescription(this.mTaskDescription);
    }

    public final void requestPermissions(String[] permissions, int requestCode) {
        if (requestCode < 0) {
            throw new IllegalArgumentException("requestCode should be >= 0");
        } else if (this.mHasCurrentPermissionsRequest) {
            Log.w(TAG, "Can request only one set of permissions at a time");
            onRequestPermissionsResult(requestCode, new String[0], new int[0]);
        } else {
            startActivityForResult(REQUEST_PERMISSIONS_WHO_PREFIX, getPackageManager().buildRequestPermissionsIntent(permissions), requestCode, null);
            this.mHasCurrentPermissionsRequest = true;
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    }

    public boolean shouldShowRequestPermissionRationale(String permission) {
        return getPackageManager().shouldShowRequestPermissionRationale(permission);
    }

    public void startActivityForResult(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode, null);
    }

    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        Activity activity = this.mParent;
        if (activity == null) {
            options = transferSpringboardActivityOptions(options);
            ActivityResult ar = this.mInstrumentation.execStartActivity((Context) this, this.mMainThread.getApplicationThread(), this.mToken, this, intent, requestCode, options);
            if (ar != null) {
                this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, requestCode, ar.getResultCode(), ar.getResultData());
            }
            if (requestCode >= 0) {
                this.mStartedActivity = true;
            }
            cancelInputsAndStartExitTransition(options);
        } else if (options != null) {
            activity.startActivityFromChild(this, intent, requestCode, options);
        } else {
            activity.startActivityFromChild(this, intent, requestCode);
        }
    }

    private void cancelInputsAndStartExitTransition(Bundle options) {
        Window window = this.mWindow;
        View decor = window != null ? window.peekDecorView() : null;
        if (decor != null) {
            decor.cancelPendingInputEvents();
        }
        if (options != null) {
            this.mActivityTransitionState.startExitOutTransition(this, options);
        }
    }

    public boolean isActivityTransitionRunning() {
        return this.mActivityTransitionState.isTransitionRunning();
    }

    private Bundle transferSpringboardActivityOptions(Bundle options) {
        if (options == null) {
            Window window = this.mWindow;
            if (!(window == null || window.isActive())) {
                ActivityOptions activityOptions = getActivityOptions();
                if (activityOptions != null && activityOptions.getAnimationType() == 5) {
                    return activityOptions.toBundle();
                }
            }
        }
        return options;
    }

    @UnsupportedAppUsage
    public void startActivityForResultAsUser(Intent intent, int requestCode, UserHandle user) {
        startActivityForResultAsUser(intent, requestCode, null, user);
    }

    public void startActivityForResultAsUser(Intent intent, int requestCode, Bundle options, UserHandle user) {
        startActivityForResultAsUser(intent, this.mEmbeddedID, requestCode, options, user);
    }

    public void startActivityForResultAsUser(Intent intent, String resultWho, int requestCode, Bundle options, UserHandle user) {
        if (this.mParent == null) {
            options = transferSpringboardActivityOptions(options);
            ActivityResult ar = this.mInstrumentation.execStartActivity(this, this.mMainThread.getApplicationThread(), this.mToken, resultWho, intent, requestCode, options, user);
            if (ar != null) {
                this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, requestCode, ar.getResultCode(), ar.getResultData());
            }
            if (requestCode >= 0) {
                this.mStartedActivity = true;
            }
            cancelInputsAndStartExitTransition(options);
            return;
        }
        throw new RuntimeException("Can't be called from a child");
    }

    public void startActivityAsUser(Intent intent, UserHandle user) {
        startActivityAsUser(intent, null, user);
    }

    public void startActivityAsUser(Intent intent, Bundle options, UserHandle user) {
        if (this.mParent == null) {
            options = transferSpringboardActivityOptions(options);
            ActivityResult ar = this.mInstrumentation.execStartActivity(this, this.mMainThread.getApplicationThread(), this.mToken, this.mEmbeddedID, intent, -1, options, user);
            if (ar != null) {
                this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, -1, ar.getResultCode(), ar.getResultData());
            }
            cancelInputsAndStartExitTransition(options);
            return;
        }
        throw new RuntimeException("Can't be called from a child");
    }

    public void startActivityAsCaller(Intent intent, Bundle options, IBinder permissionToken, boolean ignoreTargetSecurity, int userId) {
        Bundle bundle;
        if (this.mParent == null) {
            bundle = options;
            Bundle options2 = transferSpringboardActivityOptions(options);
            ActivityResult ar = this.mInstrumentation.execStartActivityAsCaller(this, this.mMainThread.getApplicationThread(), this.mToken, this, intent, -1, options2, permissionToken, ignoreTargetSecurity, userId);
            if (ar != null) {
                this.mMainThread.sendActivityResult(this.mToken, this.mEmbeddedID, -1, ar.getResultCode(), ar.getResultData());
            }
            cancelInputsAndStartExitTransition(options2);
            return;
        }
        bundle = options;
        throw new RuntimeException("Can't be called from a child");
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws SendIntentException {
        startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, null);
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
        Activity activity = this.mParent;
        if (activity == null) {
            startIntentSenderForResultInner(intent, this.mEmbeddedID, requestCode, fillInIntent, flagsMask, flagsValues, options);
        } else if (options != null) {
            activity.startIntentSenderFromChild(this, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } else {
            activity.startIntentSenderFromChild(this, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags);
        }
    }

    private void startIntentSenderForResultInner(IntentSender intent, String who, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, Bundle options) throws SendIntentException {
        Intent intent2 = fillInIntent;
        Bundle bundle = options;
        Bundle options2;
        try {
            options2 = transferSpringboardActivityOptions(bundle);
            String resolvedType = null;
            if (intent2 != null) {
                try {
                    fillInIntent.migrateExtraStreamToClipData();
                    intent2.prepareToLeaveProcess(this);
                    resolvedType = intent2.resolveTypeIfNeeded(getContentResolver());
                } catch (RemoteException e) {
                }
            }
            String str = resolvedType;
            String options3 = resolvedType;
            Object obj = null;
            int result = ActivityTaskManager.getService().startActivityIntentSender(this.mMainThread.getApplicationThread(), intent != null ? intent.getTarget() : null, intent != null ? intent.getWhitelistToken() : null, fillInIntent, str, this.mToken, who, requestCode, flagsMask, flagsValues, options2);
            if (result != -96) {
                Instrumentation.checkStartActivityResult(result, obj);
                if (options2 != null) {
                    cancelInputsAndStartExitTransition(options2);
                }
                if (requestCode >= 0) {
                    this.mStartedActivity = true;
                    return;
                }
                return;
            }
            throw new SendIntentException();
        } catch (RemoteException e2) {
            options2 = bundle;
        }
    }

    public void startActivity(Intent intent) {
        startActivity(intent, null);
    }

    public void startActivity(Intent intent, Bundle options) {
        if (options != null) {
            startActivityForResult(intent, -1, options);
        } else {
            startActivityForResult(intent, -1);
        }
    }

    public void startActivities(Intent[] intents) {
        startActivities(intents, null);
    }

    public void startActivities(Intent[] intents, Bundle options) {
        this.mInstrumentation.execStartActivities(this, this.mMainThread.getApplicationThread(), this.mToken, this, intents, options);
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws SendIntentException {
        startIntentSender(intent, fillInIntent, flagsMask, flagsValues, extraFlags, null);
    }

    public void startIntentSender(IntentSender intent, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
        if (options != null) {
            startIntentSenderForResult(intent, -1, fillInIntent, flagsMask, flagsValues, extraFlags, options);
        } else {
            startIntentSenderForResult(intent, -1, fillInIntent, flagsMask, flagsValues, extraFlags);
        }
    }

    public boolean startActivityIfNeeded(Intent intent, int requestCode) {
        return startActivityIfNeeded(intent, requestCode, null);
    }

    public boolean startActivityIfNeeded(Intent intent, int requestCode, Bundle options) {
        Intent intent2 = intent;
        if (this.mParent == null) {
            int result = 1;
            try {
                Parcelable referrer = onProvideReferrer();
                if (referrer != null) {
                    intent2.putExtra(Intent.EXTRA_REFERRER, referrer);
                }
                intent.migrateExtraStreamToClipData();
                intent2.prepareToLeaveProcess((Context) this);
                result = ActivityTaskManager.getService().startActivity(this.mMainThread.getApplicationThread(), getBasePackageName(), intent, intent2.resolveTypeIfNeeded(getContentResolver()), this.mToken, this.mEmbeddedID, requestCode, 1, null, options);
            } catch (RemoteException e) {
            }
            Instrumentation.checkStartActivityResult(result, intent2);
            if (requestCode >= 0) {
                this.mStartedActivity = true;
            }
            if (result != 1) {
                return true;
            }
            return false;
        }
        throw new UnsupportedOperationException("startActivityIfNeeded can only be called from a top-level activity");
    }

    public boolean startNextMatchingActivity(Intent intent) {
        return startNextMatchingActivity(intent, null);
    }

    public boolean startNextMatchingActivity(Intent intent, Bundle options) {
        if (this.mParent == null) {
            try {
                intent.migrateExtraStreamToClipData();
                intent.prepareToLeaveProcess((Context) this);
                return ActivityTaskManager.getService().startNextMatchingActivity(this.mToken, intent, options);
            } catch (RemoteException e) {
                return false;
            }
        }
        throw new UnsupportedOperationException("startNextMatchingActivity can only be called from a top-level activity");
    }

    public void startActivityFromChild(Activity child, Intent intent, int requestCode) {
        startActivityFromChild(child, intent, requestCode, null);
    }

    public void startActivityFromChild(Activity child, Intent intent, int requestCode, Bundle options) {
        options = transferSpringboardActivityOptions(options);
        ActivityResult ar = this.mInstrumentation.execStartActivity((Context) this, this.mMainThread.getApplicationThread(), this.mToken, child, intent, requestCode, options);
        if (ar != null) {
            this.mMainThread.sendActivityResult(this.mToken, child.mEmbeddedID, requestCode, ar.getResultCode(), ar.getResultData());
        }
        cancelInputsAndStartExitTransition(options);
    }

    @Deprecated
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode) {
        startActivityFromFragment(fragment, intent, requestCode, null);
    }

    @Deprecated
    public void startActivityFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options) {
        startActivityForResult(fragment.mWho, intent, requestCode, options);
    }

    public void startActivityAsUserFromFragment(Fragment fragment, Intent intent, int requestCode, Bundle options, UserHandle user) {
        startActivityForResultAsUser(intent, fragment.mWho, requestCode, options, user);
    }

    @UnsupportedAppUsage
    public void startActivityForResult(String who, Intent intent, int requestCode, Bundle options) {
        Parcelable referrer = onProvideReferrer();
        if (referrer != null) {
            intent.putExtra(Intent.EXTRA_REFERRER, referrer);
        }
        options = transferSpringboardActivityOptions(options);
        ActivityResult ar = this.mInstrumentation.execStartActivity((Context) this, this.mMainThread.getApplicationThread(), this.mToken, who, intent, requestCode, options);
        if (ar != null) {
            this.mMainThread.sendActivityResult(this.mToken, who, requestCode, ar.getResultCode(), ar.getResultData());
        }
        cancelInputsAndStartExitTransition(options);
    }

    public boolean canStartActivityForResult() {
        return true;
    }

    public void startIntentSenderFromChild(Activity child, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws SendIntentException {
        startIntentSenderFromChild(child, intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, null);
    }

    public void startIntentSenderFromChild(Activity child, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
        startIntentSenderForResultInner(intent, child.mEmbeddedID, requestCode, fillInIntent, flagsMask, flagsValues, options);
    }

    public void startIntentSenderFromChildFragment(Fragment child, IntentSender intent, int requestCode, Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws SendIntentException {
        startIntentSenderForResultInner(intent, child.mWho, requestCode, fillInIntent, flagsMask, flagsValues, options);
    }

    public void overridePendingTransition(int enterAnim, int exitAnim) {
        try {
            ActivityTaskManager.getService().overridePendingTransition(this.mToken, getPackageName(), enterAnim, exitAnim);
        } catch (RemoteException e) {
        }
    }

    public final void setResult(int resultCode) {
        synchronized (this) {
            this.mResultCode = resultCode;
            this.mResultData = null;
        }
    }

    public final void setResult(int resultCode, Intent data) {
        synchronized (this) {
            this.mResultCode = resultCode;
            this.mResultData = data;
        }
    }

    public Uri getReferrer() {
        Intent intent = getIntent();
        try {
            Uri referrer = (Uri) intent.getParcelableExtra(Intent.EXTRA_REFERRER);
            if (referrer != null) {
                return referrer;
            }
            String referrerName = intent.getStringExtra(Intent.EXTRA_REFERRER_NAME);
            if (referrerName != null) {
                return Uri.parse(referrerName);
            }
            if (this.mReferrer != null) {
                return new Uri.Builder().scheme("android-app").authority(this.mReferrer).build();
            }
            return null;
        } catch (BadParcelableException e) {
            Log.w(TAG, "Cannot read referrer from intent; intent extras contain unknown custom Parcelable objects");
        }
    }

    public Uri onProvideReferrer() {
        return null;
    }

    public String getCallingPackage() {
        try {
            return ActivityTaskManager.getService().getCallingPackage(this.mToken);
        } catch (RemoteException e) {
            return null;
        }
    }

    public ComponentName getCallingActivity() {
        try {
            return ActivityTaskManager.getService().getCallingActivity(this.mToken);
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setVisible(boolean visible) {
        if (this.mVisibleFromClient != visible) {
            this.mVisibleFromClient = visible;
            if (!this.mVisibleFromServer) {
                return;
            }
            if (visible) {
                makeVisible();
            } else {
                this.mDecor.setVisibility(4);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void makeVisible() {
        if (!this.mWindowAdded) {
            getWindowManager().addView(this.mDecor, getWindow().getAttributes());
            this.mWindowAdded = true;
        }
        this.mDecor.setVisibility(0);
    }

    public boolean isFinishing() {
        return this.mFinished;
    }

    public boolean isDestroyed() {
        return this.mDestroyed;
    }

    public boolean isChangingConfigurations() {
        return this.mChangingConfigurations;
    }

    public void recreate() {
        if (this.mParent != null) {
            throw new IllegalStateException("Can only be called on top-level activity");
        } else if (Looper.myLooper() == this.mMainThread.getLooper()) {
            this.mMainThread.scheduleRelaunchActivity(this.mToken);
        } else {
            throw new IllegalStateException("Must be called from main thread");
        }
    }

    @UnsupportedAppUsage
    private void finish(int finishTask) {
        Activity activity = this.mParent;
        if (activity == null) {
            int resultCode;
            Intent resultData;
            synchronized (this) {
                resultCode = this.mResultCode;
                resultData = this.mResultData;
            }
            if (resultData != null) {
                try {
                    resultData.prepareToLeaveProcess((Context) this);
                } catch (RemoteException e) {
                }
            }
            if (ActivityTaskManager.getService().finishActivity(this.mToken, resultCode, resultData, finishTask)) {
                this.mFinished = true;
            }
        } else {
            activity.finishFromChild(this);
        }
        Intent intent = this.mIntent;
        if (intent != null && intent.hasExtra(AutofillManager.EXTRA_RESTORE_SESSION_TOKEN)) {
            getAutofillManager().onPendingSaveUi(2, this.mIntent.getIBinderExtra(AutofillManager.EXTRA_RESTORE_SESSION_TOKEN));
        }
    }

    public void finish() {
        finish(0);
    }

    public void finishAffinity() {
        if (this.mParent != null) {
            throw new IllegalStateException("Can not be called from an embedded activity");
        } else if (this.mResultCode == 0 && this.mResultData == null) {
            try {
                if (ActivityTaskManager.getService().finishActivityAffinity(this.mToken)) {
                    this.mFinished = true;
                }
            } catch (RemoteException e) {
            }
        } else {
            throw new IllegalStateException("Can not be called to deliver a result");
        }
    }

    public void finishFromChild(Activity child) {
        finish();
    }

    public void finishAfterTransition() {
        if (!this.mActivityTransitionState.startExitBackTransition(this)) {
            finish();
        }
    }

    public void finishActivity(int requestCode) {
        Activity activity = this.mParent;
        if (activity == null) {
            try {
                ActivityTaskManager.getService().finishSubActivity(this.mToken, this.mEmbeddedID, requestCode);
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        activity.finishActivityFromChild(this, requestCode);
    }

    public void finishActivityFromChild(Activity child, int requestCode) {
        try {
            ActivityTaskManager.getService().finishSubActivity(this.mToken, child.mEmbeddedID, requestCode);
        } catch (RemoteException e) {
        }
    }

    public void finishAndRemoveTask() {
        finish(1);
    }

    public boolean releaseInstance() {
        try {
            return ActivityTaskManager.getService().releaseActivityInstance(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    public void onActivityReenter(int resultCode, Intent data) {
    }

    public PendingIntent createPendingResult(int requestCode, Intent data, int flags) {
        Intent intent = data;
        String packageName = getPackageName();
        PendingIntent pendingIntent = null;
        try {
            intent.prepareToLeaveProcess(this);
            IIntentSender target = ActivityManager.getService().getIntentSender(3, packageName, this.mParent == null ? this.mToken : this.mParent.mToken, this.mEmbeddedID, requestCode, new Intent[]{intent}, null, flags, null, getUserId());
            if (target != null) {
                pendingIntent = new PendingIntent(target);
            }
            return pendingIntent;
        } catch (RemoteException e) {
            return null;
        }
    }

    public void setRequestedOrientation(int requestedOrientation) {
        Activity activity = this.mParent;
        if (activity == null) {
            try {
                ActivityTaskManager.getService().setRequestedOrientation(this.mToken, requestedOrientation);
                return;
            } catch (RemoteException e) {
                return;
            }
        }
        activity.setRequestedOrientation(requestedOrientation);
    }

    public int getRequestedOrientation() {
        Activity activity = this.mParent;
        if (activity != null) {
            return activity.getRequestedOrientation();
        }
        try {
            return ActivityTaskManager.getService().getRequestedOrientation(this.mToken);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public int getTaskId() {
        try {
            return ActivityTaskManager.getService().getTaskForActivity(this.mToken, false);
        } catch (RemoteException e) {
            return -1;
        }
    }

    public boolean isTaskRoot() {
        boolean z = false;
        try {
            if (ActivityTaskManager.getService().getTaskForActivity(this.mToken, true) >= 0) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean moveTaskToBack(boolean nonRoot) {
        try {
            return ActivityTaskManager.getService().moveActivityTaskToBack(this.mToken, nonRoot);
        } catch (RemoteException e) {
            return false;
        }
    }

    public String getLocalClassName() {
        String pkg = getPackageName();
        String cls = this.mComponent.getClassName();
        int packageLen = pkg.length();
        if (cls.startsWith(pkg) && cls.length() > packageLen && cls.charAt(packageLen) == ClassUtils.PACKAGE_SEPARATOR_CHAR) {
            return cls.substring(packageLen + 1);
        }
        return cls;
    }

    public ComponentName getComponentName() {
        return this.mComponent;
    }

    public final ComponentName autofillClientGetComponentName() {
        return getComponentName();
    }

    public final ComponentName contentCaptureClientGetComponentName() {
        return getComponentName();
    }

    public SharedPreferences getPreferences(int mode) {
        return getSharedPreferences(getLocalClassName(), mode);
    }

    private void ensureSearchManager() {
        if (this.mSearchManager == null) {
            try {
                this.mSearchManager = new SearchManager(this, null);
            } catch (ServiceNotFoundException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public Object getSystemService(String name) {
        if (getBaseContext() == null) {
            throw new IllegalStateException("System services not available to Activities before onCreate()");
        } else if (Context.WINDOW_SERVICE.equals(name)) {
            return this.mWindowManager;
        } else {
            if (!"search".equals(name)) {
                return super.getSystemService(name);
            }
            ensureSearchManager();
            return this.mSearchManager;
        }
    }

    public void setTitle(CharSequence title) {
        this.mTitle = title;
        onTitleChanged(title, this.mTitleColor);
        Activity activity = this.mParent;
        if (activity != null) {
            activity.onChildTitleChanged(this, title);
        }
    }

    public void setTitle(int titleId) {
        setTitle(getText(titleId));
    }

    @Deprecated
    public void setTitleColor(int textColor) {
        this.mTitleColor = textColor;
        onTitleChanged(this.mTitle, textColor);
    }

    public final CharSequence getTitle() {
        return this.mTitle;
    }

    public final int getTitleColor() {
        return this.mTitleColor;
    }

    /* Access modifiers changed, original: protected */
    public void onTitleChanged(CharSequence title, int color) {
        if (this.mTitleReady) {
            Window win = getWindow();
            if (win != null) {
                win.setTitle(title);
                if (color != 0) {
                    win.setTitleColor(color);
                }
            }
            ActionBar actionBar = this.mActionBar;
            if (actionBar != null) {
                actionBar.setWindowTitle(title);
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onChildTitleChanged(Activity childActivity, CharSequence title) {
    }

    public void setTaskDescription(TaskDescription taskDescription) {
        TaskDescription taskDescription2 = this.mTaskDescription;
        if (taskDescription2 != taskDescription) {
            taskDescription2.copyFromPreserveHiddenFields(taskDescription);
            if (taskDescription.getIconFilename() == null && taskDescription.getIcon() != null) {
                int size = ActivityManager.getLauncherLargeIconSizeInner(this);
                this.mTaskDescription.setIcon(Bitmap.createScaledBitmap(taskDescription.getIcon(), size, size, true));
            }
        }
        try {
            ActivityTaskManager.getService().setTaskDescription(this.mToken, this.mTaskDescription);
        } catch (RemoteException e) {
        }
    }

    @Deprecated
    public final void setProgressBarVisibility(boolean visible) {
        int i;
        Window window = getWindow();
        if (visible) {
            i = -1;
        } else {
            i = -2;
        }
        window.setFeatureInt(2, i);
    }

    @Deprecated
    public final void setProgressBarIndeterminateVisibility(boolean visible) {
        getWindow().setFeatureInt(5, visible ? -1 : -2);
    }

    @Deprecated
    public final void setProgressBarIndeterminate(boolean indeterminate) {
        int i;
        Window window = getWindow();
        if (indeterminate) {
            i = -3;
        } else {
            i = -4;
        }
        window.setFeatureInt(2, i);
    }

    @Deprecated
    public final void setProgress(int progress) {
        getWindow().setFeatureInt(2, progress + 0);
    }

    @Deprecated
    public final void setSecondaryProgress(int secondaryProgress) {
        getWindow().setFeatureInt(2, secondaryProgress + 20000);
    }

    public final void setVolumeControlStream(int streamType) {
        getWindow().setVolumeControlStream(streamType);
    }

    public final int getVolumeControlStream() {
        return getWindow().getVolumeControlStream();
    }

    public final void setMediaController(MediaController controller) {
        getWindow().setMediaController(controller);
    }

    public final MediaController getMediaController() {
        return getWindow().getMediaController();
    }

    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != this.mUiThread) {
            this.mHandler.post(action);
        } else {
            action.run();
        }
    }

    public final void autofillClientRunOnUiThread(Runnable action) {
        runOnUiThread(action);
    }

    public View onCreateView(String name, Context context, AttributeSet attrs) {
        return null;
    }

    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        if (Function.FRAGMENT.equals(name)) {
            return this.mFragments.onCreateView(parent, name, context, attrs);
        }
        return onCreateView(name, context, attrs);
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        dumpInner(prefix, fd, writer, args);
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0036  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x002f  */
    /* JADX WARNING: Missing block: B:8:0x001f, code skipped:
            if (r1.equals("--autofill") == false) goto L_0x002c;
     */
    public void dumpInner(java.lang.String r7, java.io.FileDescriptor r8, java.io.PrintWriter r9, java.lang.String[] r10) {
        /*
        r6 = this;
        if (r10 == 0) goto L_0x003a;
    L_0x0002:
        r0 = r10.length;
        if (r0 <= 0) goto L_0x003a;
    L_0x0005:
        r0 = 0;
        r1 = r10[r0];
        r2 = -1;
        r3 = r1.hashCode();
        r4 = 1159329357; // 0x4519f64d float:2463.3938 double:5.727848075E-315;
        r5 = 1;
        if (r3 == r4) goto L_0x0022;
    L_0x0013:
        r4 = 1455016274; // 0x56b9c952 float:1.02137158E14 double:7.18873555E-315;
        if (r3 == r4) goto L_0x0019;
    L_0x0018:
        goto L_0x002c;
    L_0x0019:
        r3 = "--autofill";
        r1 = r1.equals(r3);
        if (r1 == 0) goto L_0x0018;
    L_0x0021:
        goto L_0x002d;
    L_0x0022:
        r0 = "--contentcapture";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0018;
    L_0x002a:
        r0 = r5;
        goto L_0x002d;
    L_0x002c:
        r0 = r2;
    L_0x002d:
        if (r0 == 0) goto L_0x0036;
    L_0x002f:
        if (r0 == r5) goto L_0x0032;
    L_0x0031:
        goto L_0x003a;
    L_0x0032:
        r6.dumpContentCaptureManager(r7, r9);
        return;
    L_0x0036:
        r6.dumpAutofillManager(r7, r9);
        return;
    L_0x003a:
        r9.print(r7);
        r0 = "Local Activity ";
        r9.print(r0);
        r0 = java.lang.System.identityHashCode(r6);
        r0 = java.lang.Integer.toHexString(r0);
        r9.print(r0);
        r0 = " State:";
        r9.println(r0);
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r0.append(r7);
        r1 = "  ";
        r0.append(r1);
        r0 = r0.toString();
        r9.print(r0);
        r1 = "mResumed=";
        r9.print(r1);
        r1 = r6.mResumed;
        r9.print(r1);
        r1 = " mStopped=";
        r9.print(r1);
        r1 = r6.mStopped;
        r9.print(r1);
        r1 = " mFinished=";
        r9.print(r1);
        r1 = r6.mFinished;
        r9.println(r1);
        r9.print(r0);
        r1 = "mChangingConfigurations=";
        r9.print(r1);
        r1 = r6.mChangingConfigurations;
        r9.println(r1);
        r9.print(r0);
        r1 = "mCurrentConfig=";
        r9.print(r1);
        r1 = r6.mCurrentConfig;
        r9.println(r1);
        r1 = r6.mFragments;
        r1.dumpLoaders(r0, r8, r9, r10);
        r1 = r6.mFragments;
        r1 = r1.getFragmentManager();
        r1.dump(r0, r8, r9, r10);
        r1 = r6.mVoiceInteractor;
        if (r1 == 0) goto L_0x00b3;
    L_0x00b0:
        r1.dump(r0, r8, r9, r10);
    L_0x00b3:
        r1 = r6.getWindow();
        if (r1 == 0) goto L_0x00e0;
    L_0x00b9:
        r1 = r6.getWindow();
        r1 = r1.peekDecorView();
        if (r1 == 0) goto L_0x00e0;
    L_0x00c3:
        r1 = r6.getWindow();
        r1 = r1.peekDecorView();
        r1 = r1.getViewRootImpl();
        if (r1 == 0) goto L_0x00e0;
    L_0x00d1:
        r1 = r6.getWindow();
        r1 = r1.peekDecorView();
        r1 = r1.getViewRootImpl();
        r1.dump(r7, r8, r9, r10);
    L_0x00e0:
        r1 = r6.mHandler;
        r1 = r1.getLooper();
        r2 = new android.util.PrintWriterPrinter;
        r2.<init>(r9);
        r1.dump(r2, r7);
        r6.dumpAutofillManager(r7, r9);
        r6.dumpContentCaptureManager(r7, r9);
        r1 = android.app.ResourcesManager.getInstance();
        r1.dump(r7, r9);
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.Activity.dumpInner(java.lang.String, java.io.FileDescriptor, java.io.PrintWriter, java.lang.String[]):void");
    }

    /* Access modifiers changed, original: 0000 */
    public void dumpAutofillManager(String prefix, PrintWriter writer) {
        AutofillManager afm = getAutofillManager();
        if (afm != null) {
            afm.dump(prefix, writer);
            writer.print(prefix);
            writer.print("Autofill Compat Mode: ");
            writer.println(isAutofillCompatibilityEnabled());
            return;
        }
        writer.print(prefix);
        writer.println("No AutofillManager");
    }

    /* Access modifiers changed, original: 0000 */
    public void dumpContentCaptureManager(String prefix, PrintWriter writer) {
        ContentCaptureManager cm = getContentCaptureManager();
        if (cm != null) {
            cm.dump(prefix, writer);
            return;
        }
        writer.print(prefix);
        writer.println("No ContentCaptureManager");
    }

    public boolean isImmersive() {
        try {
            return ActivityTaskManager.getService().isImmersive(this.mToken);
        } catch (RemoteException e) {
            return false;
        }
    }

    /* Access modifiers changed, original: final */
    public final boolean isTopOfTask() {
        if (this.mToken == null || this.mWindow == null) {
            return false;
        }
        try {
            return ActivityTaskManager.getService().isTopOfTask(getActivityToken());
        } catch (RemoteException e) {
            return false;
        }
    }

    @SystemApi
    public void convertFromTranslucent() {
        try {
            this.mTranslucentCallback = null;
            if (ActivityTaskManager.getService().convertFromTranslucent(this.mToken)) {
                WindowManagerGlobal.getInstance().changeCanvasOpacity(this.mToken, true);
            }
        } catch (RemoteException e) {
        }
    }

    @SystemApi
    public boolean convertToTranslucent(TranslucentConversionListener callback, ActivityOptions options) {
        boolean drawComplete;
        try {
            this.mTranslucentCallback = callback;
            this.mChangeCanvasToTranslucent = ActivityTaskManager.getService().convertToTranslucent(this.mToken, options == null ? null : options.toBundle());
            WindowManagerGlobal.getInstance().changeCanvasOpacity(this.mToken, false);
            drawComplete = true;
        } catch (RemoteException e) {
            this.mChangeCanvasToTranslucent = false;
            drawComplete = false;
        }
        if (!this.mChangeCanvasToTranslucent) {
            TranslucentConversionListener translucentConversionListener = this.mTranslucentCallback;
            if (translucentConversionListener != null) {
                translucentConversionListener.onTranslucentConversionComplete(drawComplete);
            }
        }
        return this.mChangeCanvasToTranslucent;
    }

    /* Access modifiers changed, original: 0000 */
    public void onTranslucentConversionComplete(boolean drawComplete) {
        TranslucentConversionListener translucentConversionListener = this.mTranslucentCallback;
        if (translucentConversionListener != null) {
            translucentConversionListener.onTranslucentConversionComplete(drawComplete);
            this.mTranslucentCallback = null;
        }
        if (this.mChangeCanvasToTranslucent) {
            WindowManagerGlobal.getInstance().changeCanvasOpacity(this.mToken, false);
        }
    }

    public void setDummyTranslucent(boolean translucent) {
        try {
            ActivityManager.getService().setDummyTranslucent(this.mToken, translucent);
        } catch (RemoteException e) {
        }
    }

    public int getWindowingMode() {
        try {
            return ActivityTaskManager.getService().handleFreeformModeRequst(this.mToken, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void onNewActivityOptions(ActivityOptions options) {
        this.mActivityTransitionState.setEnterActivityOptions(this, options);
        if (!this.mStopped) {
            this.mActivityTransitionState.enterReady(this);
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public ActivityOptions getActivityOptions() {
        try {
            return ActivityOptions.fromBundle(ActivityTaskManager.getService().getActivityOptions(this.mToken));
        } catch (RemoteException e) {
            return null;
        }
    }

    @Deprecated
    public boolean requestVisibleBehind(boolean visible) {
        return false;
    }

    @Deprecated
    public void onVisibleBehindCanceled() {
        this.mCalled = true;
    }

    @SystemApi
    @Deprecated
    public boolean isBackgroundVisibleBehind() {
        return false;
    }

    @SystemApi
    @Deprecated
    public void onBackgroundVisibleBehindChanged(boolean visible) {
    }

    public void onEnterAnimationComplete() {
    }

    public void dispatchEnterAnimationComplete() {
        this.mEnterAnimationComplete = true;
        this.mInstrumentation.onEnterAnimationComplete();
        onEnterAnimationComplete();
        if (getWindow() != null && getWindow().getDecorView() != null) {
            getWindow().getDecorView().getViewTreeObserver().dispatchOnEnterAnimationComplete();
        }
    }

    public void setImmersive(boolean i) {
        try {
            ActivityTaskManager.getService().setImmersive(this.mToken, i);
        } catch (RemoteException e) {
        }
    }

    public void setVrModeEnabled(boolean enabled, ComponentName requestedComponent) throws NameNotFoundException {
        try {
            if (ActivityTaskManager.getService().setVrMode(this.mToken, enabled, requestedComponent) != 0) {
                throw new NameNotFoundException(requestedComponent.flattenToString());
            }
        } catch (RemoteException e) {
        }
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        return this.mWindow.getDecorView().startActionMode(callback);
    }

    public ActionMode startActionMode(ActionMode.Callback callback, int type) {
        return this.mWindow.getDecorView().startActionMode(callback, type);
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        if (this.mActionModeTypeStarting == 0) {
            initWindowDecorActionBar();
            ActionBar actionBar = this.mActionBar;
            if (actionBar != null) {
                return actionBar.startActionMode(callback);
            }
        }
        return null;
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
    }

    public void onActionModeFinished(ActionMode mode) {
    }

    public boolean shouldUpRecreateTask(Intent targetIntent) {
        try {
            PackageManager pm = getPackageManager();
            ComponentName cn = targetIntent.getComponent();
            if (cn == null) {
                cn = targetIntent.resolveActivity(pm);
            }
            ActivityInfo info = pm.getActivityInfo(cn, 0);
            if (info.taskAffinity == null) {
                return false;
            }
            return ActivityTaskManager.getService().shouldUpRecreateTask(this.mToken, info.taskAffinity);
        } catch (RemoteException e) {
            return false;
        } catch (NameNotFoundException e2) {
            return false;
        }
    }

    public boolean navigateUpTo(Intent upIntent) {
        Activity activity = this.mParent;
        if (activity != null) {
            return activity.navigateUpToFromChild(this, upIntent);
        }
        Intent upIntent2;
        int resultCode;
        Intent resultData;
        ComponentName destInfo = upIntent.getComponent();
        boolean z = false;
        if (destInfo == null) {
            destInfo = upIntent.resolveActivity(getPackageManager());
            if (destInfo == null) {
                return false;
            }
            upIntent2 = new Intent(upIntent);
            upIntent2.setComponent(destInfo);
            ComponentName componentName = destInfo;
        } else {
            upIntent2 = upIntent;
        }
        synchronized (this) {
            resultCode = this.mResultCode;
            resultData = this.mResultData;
        }
        if (resultData != null) {
            resultData.prepareToLeaveProcess((Context) this);
        }
        try {
            upIntent2.prepareToLeaveProcess((Context) this);
            z = ActivityTaskManager.getService().navigateUpTo(this.mToken, upIntent2, resultCode, resultData);
            return z;
        } catch (RemoteException e) {
            return z;
        }
    }

    public boolean navigateUpToFromChild(Activity child, Intent upIntent) {
        return navigateUpTo(upIntent);
    }

    public Intent getParentActivityIntent() {
        String parentName = this.mActivityInfo.parentActivityName;
        if (TextUtils.isEmpty(parentName)) {
            return null;
        }
        ComponentName target = new ComponentName((Context) this, parentName);
        try {
            Intent parentIntent;
            if (getPackageManager().getActivityInfo(target, 0).parentActivityName == null) {
                parentIntent = Intent.makeMainActivity(target);
            } else {
                parentIntent = new Intent().setComponent(target);
            }
            return parentIntent;
        } catch (NameNotFoundException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getParentActivityIntent: bad parentActivityName '");
            stringBuilder.append(parentName);
            stringBuilder.append("' in manifest");
            Log.e(TAG, stringBuilder.toString());
            return null;
        }
    }

    public void setEnterSharedElementCallback(SharedElementCallback callback) {
        if (callback == null) {
            callback = SharedElementCallback.NULL_CALLBACK;
        }
        this.mEnterTransitionListener = callback;
    }

    public void setExitSharedElementCallback(SharedElementCallback callback) {
        if (callback == null) {
            callback = SharedElementCallback.NULL_CALLBACK;
        }
        this.mExitTransitionListener = callback;
    }

    public void postponeEnterTransition() {
        this.mActivityTransitionState.postponeEnterTransition();
    }

    public void startPostponedEnterTransition() {
        this.mActivityTransitionState.startPostponedEnterTransition();
    }

    public DragAndDropPermissions requestDragAndDropPermissions(DragEvent event) {
        DragAndDropPermissions dragAndDropPermissions = DragAndDropPermissions.obtain(event);
        if (dragAndDropPermissions == null || !dragAndDropPermissions.take(getActivityToken())) {
            return null;
        }
        return dragAndDropPermissions;
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public final void setParent(Activity parent) {
        this.mParent = parent;
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final void attach(Context context, ActivityThread aThread, Instrumentation instr, IBinder token, int ident, Application application, Intent intent, ActivityInfo info, CharSequence title, Activity parent, String id, NonConfigurationInstances lastNonConfigurationInstances, Configuration config, String referrer, IVoiceInteractor voiceInteractor, Window window, ActivityConfigCallback activityConfigCallback, IBinder assistToken) {
        ActivityInfo activityInfo = info;
        NonConfigurationInstances nonConfigurationInstances = lastNonConfigurationInstances;
        IVoiceInteractor iVoiceInteractor = voiceInteractor;
        attachBaseContext(context);
        this.mFragments.attachHost(null);
        this.mWindow = new MiuiPhoneWindow(this, window, activityConfigCallback);
        this.mWindow.setWindowControllerCallback(this);
        this.mWindow.setCallback(this);
        this.mWindow.setOnWindowDismissedCallback(this);
        this.mWindow.getLayoutInflater().setPrivateFactory(this);
        if (activityInfo.softInputMode != 0) {
            this.mWindow.setSoftInputMode(activityInfo.softInputMode);
        }
        if (activityInfo.uiOptions != 0) {
            this.mWindow.setUiOptions(activityInfo.uiOptions);
        }
        this.mUiThread = Thread.currentThread();
        this.mMainThread = aThread;
        this.mInstrumentation = instr;
        this.mToken = token;
        this.mAssistToken = assistToken;
        this.mIdent = ident;
        this.mApplication = application;
        this.mIntent = intent;
        this.mReferrer = referrer;
        this.mComponent = intent.getComponent();
        this.mActivityInfo = activityInfo;
        this.mTitle = title;
        this.mParent = parent;
        this.mEmbeddedID = id;
        this.mLastNonConfigurationInstances = nonConfigurationInstances;
        if (iVoiceInteractor != null) {
            if (nonConfigurationInstances != null) {
                this.mVoiceInteractor = nonConfigurationInstances.voiceInteractor;
            } else {
                this.mVoiceInteractor = new VoiceInteractor(iVoiceInteractor, this, this, Looper.myLooper());
            }
        }
        this.mWindow.setWindowManager((WindowManager) context.getSystemService(Context.WINDOW_SERVICE), this.mToken, this.mComponent.flattenToString(), (activityInfo.flags & 512) != 0);
        Activity activity = this.mParent;
        if (activity != null) {
            this.mWindow.setContainer(activity.getWindow());
        }
        this.mWindowManager = this.mWindow.getWindowManager();
        this.mCurrentConfig = config;
        this.mWindow.setColorMode(activityInfo.colorMode);
        setAutofillOptions(application.getAutofillOptions());
        setContentCaptureOptions(application.getContentCaptureOptions());
    }

    private void enableAutofillCompatibilityIfNeeded() {
        if (isAutofillCompatibilityEnabled()) {
            AutofillManager afm = (AutofillManager) getSystemService(AutofillManager.class);
            if (afm != null) {
                afm.enableCompatibilityMode();
            }
        }
    }

    @UnsupportedAppUsage
    public final IBinder getActivityToken() {
        Activity activity = this.mParent;
        return activity != null ? activity.getActivityToken() : this.mToken;
    }

    public final IBinder getAssistToken() {
        Activity activity = this.mParent;
        return activity != null ? activity.getAssistToken() : this.mAssistToken;
    }

    @VisibleForTesting
    public final ActivityThread getActivityThread() {
        return this.mMainThread;
    }

    /* Access modifiers changed, original: final */
    public final void performCreate(Bundle icicle) {
        performCreate(icicle, null);
    }

    /* Access modifiers changed, original: final */
    @UnsupportedAppUsage
    public final void performCreate(Bundle icicle, PersistableBundle persistentState) {
        dispatchActivityPreCreated(icicle);
        this.mCanEnterPictureInPicture = true;
        restoreHasCurrentPermissionRequest(icicle);
        long startTime = SystemClock.uptimeMillis();
        if (persistentState != null) {
            onCreate(icicle, persistentState);
        } else {
            onCreate(icicle);
        }
        long took = SystemClock.uptimeMillis() - startTime;
        writeEventLog(LOG_AM_ON_CREATE_CALLED, "performCreate", took);
        checkLifecycleTime(took, 500, "onCreate");
        this.mActivityTransitionState.readState(icicle);
        this.mVisibleFromClient = 1 ^ this.mWindow.getWindowStyle().getBoolean(10, false);
        this.mFragments.dispatchActivityCreated();
        this.mActivityTransitionState.setEnterActivityOptions(this, getActivityOptions());
        dispatchActivityPostCreated(icicle);
    }

    /* Access modifiers changed, original: final */
    public final void performNewIntent(Intent intent) {
        this.mCanEnterPictureInPicture = true;
        onNewIntent(intent);
    }

    /* Access modifiers changed, original: final */
    public final void performStart(String reason) {
        dispatchActivityPreStarted();
        this.mActivityTransitionState.setEnterActivityOptions(this, getActivityOptions());
        this.mFragments.noteStateNotSaved();
        this.mCalled = false;
        this.mFragments.execPendingActions();
        long startTime = SystemClock.uptimeMillis();
        this.mInstrumentation.callActivityOnStart(this);
        long took = SystemClock.uptimeMillis() - startTime;
        writeEventLog(LOG_AM_ON_START_CALLED, reason, took);
        checkLifecycleTime(took, 100, "onStart");
        if (this.mCalled) {
            this.mFragments.dispatchStart();
            this.mFragments.reportLoaderStart();
            boolean isAppDebuggable = (this.mApplication.getApplicationInfo().flags & 2) != 0;
            boolean isDlwarningEnabled = SystemProperties.getInt("ro.bionic.ld.warning", 0) == 1;
            if (isAppDebuggable || isDlwarningEnabled) {
                String dlwarning = getDlWarning();
                if (dlwarning != null) {
                    CharSequence appName = getApplicationInfo().loadLabel(getPackageManager()).toString();
                    String warning = new StringBuilder();
                    warning.append("Detected problems with app native libraries\n(please consult log for detail):\n");
                    warning.append(dlwarning);
                    CharSequence warning2 = warning.toString();
                    if (isAppDebuggable) {
                        new AlertDialog.Builder(this).setTitle(appName).setMessage(warning2).setPositiveButton(17039370, null).setCancelable(false).show();
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(appName);
                        stringBuilder.append("\n");
                        stringBuilder.append(warning2);
                        Toast.makeText((Context) this, stringBuilder.toString(), 1).show();
                    }
                }
            }
            GraphicsEnvironment.getInstance().showAngleInUseDialogBox(this);
            this.mActivityTransitionState.enterReady(this);
            dispatchActivityPostStarted();
            return;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append("Activity ");
        stringBuilder2.append(this.mComponent.toShortString());
        stringBuilder2.append(" did not call through to super.onStart()");
        throw new SuperNotCalledException(stringBuilder2.toString());
    }

    /* Access modifiers changed, original: final */
    public final void performRestart(boolean start, String reason) {
        this.mCanEnterPictureInPicture = true;
        this.mFragments.noteStateNotSaved();
        if (this.mToken != null && this.mParent == null) {
            WindowManagerGlobal.getInstance().setStoppedState(this.mToken, false);
        }
        if (this.mStopped) {
            StringBuilder stringBuilder;
            this.mStopped = false;
            synchronized (this.mManagedCursors) {
                int N = this.mManagedCursors.size();
                for (int i = 0; i < N; i++) {
                    ManagedCursor mc = (ManagedCursor) this.mManagedCursors.get(i);
                    if (mc.mReleased || mc.mUpdated) {
                        if (!mc.mCursor.requery()) {
                            if (getApplicationInfo().targetSdkVersion >= 14) {
                                stringBuilder = new StringBuilder();
                                stringBuilder.append("trying to requery an already closed cursor  ");
                                stringBuilder.append(mc.mCursor);
                                throw new IllegalStateException(stringBuilder.toString());
                            }
                        }
                        mc.mReleased = false;
                        mc.mUpdated = false;
                    }
                }
            }
            this.mCalled = false;
            long startTime = SystemClock.uptimeMillis();
            this.mInstrumentation.callActivityOnRestart(this);
            long took = SystemClock.uptimeMillis() - startTime;
            writeEventLog(LOG_AM_ON_RESTART_CALLED, reason, took);
            checkLifecycleTime(took, 100, "onRestart");
            if (!this.mCalled) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Activity ");
                stringBuilder.append(this.mComponent.toShortString());
                stringBuilder.append(" did not call through to super.onRestart()");
                throw new SuperNotCalledException(stringBuilder.toString());
            } else if (start) {
                performStart(reason);
            }
        }
    }

    /* Access modifiers changed, original: final */
    public final void performResume(boolean followedByPause, String reason) {
        dispatchActivityPreResumed();
        performRestart(true, reason);
        this.mFragments.execPendingActions();
        this.mLastNonConfigurationInstances = null;
        if (this.mAutoFillResetNeeded) {
            this.mAutoFillIgnoreFirstResumePause = followedByPause;
            boolean z = this.mAutoFillIgnoreFirstResumePause;
        }
        this.mCalled = false;
        long startTime = SystemClock.uptimeMillis();
        this.mInstrumentation.callActivityOnResume(this);
        long took = SystemClock.uptimeMillis() - startTime;
        writeEventLog(30022, reason, took);
        checkLifecycleTime(took, 100, "onResume");
        String str = "Activity ";
        StringBuilder stringBuilder;
        if (this.mCalled) {
            if (!(this.mVisibleFromClient || this.mFinished)) {
                Log.w(TAG, "An activity without a UI must call finish() before onResume() completes");
                if (getApplicationInfo().targetSdkVersion > 22) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(this.mComponent.toShortString());
                    stringBuilder.append(" did not call finish() prior to onResume() completing");
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
            this.mCalled = false;
            this.mFragments.dispatchResume();
            this.mFragments.execPendingActions();
            onPostResume();
            if (this.mCalled) {
                dispatchActivityPostResumed();
                return;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(str);
            stringBuilder.append(this.mComponent.toShortString());
            stringBuilder.append(" did not call through to super.onPostResume()");
            throw new SuperNotCalledException(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(str);
        stringBuilder.append(this.mComponent.toShortString());
        stringBuilder.append(" did not call through to super.onResume()");
        throw new SuperNotCalledException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: final */
    public final void performPause() {
        dispatchActivityPrePaused();
        this.mDoReportFullyDrawn = false;
        this.mFragments.dispatchPause();
        this.mCalled = false;
        long startTime = SystemClock.uptimeMillis();
        onPause();
        long took = SystemClock.uptimeMillis() - startTime;
        writeEventLog(30021, "performPause", took);
        checkLifecycleTime(took, SLOW_ON_PAUSE_THRESHOLD_MS, "onPause");
        this.mResumed = false;
        if (this.mCalled || getApplicationInfo().targetSdkVersion < 9) {
            dispatchActivityPostPaused();
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Activity ");
        stringBuilder.append(this.mComponent.toShortString());
        stringBuilder.append(" did not call through to super.onPause()");
        throw new SuperNotCalledException(stringBuilder.toString());
    }

    /* Access modifiers changed, original: final */
    public final void performUserLeaving() {
        onUserInteraction();
        onUserLeaveHint();
    }

    /* Access modifiers changed, original: final */
    public final void performStop(boolean preserveWindow, String reason) {
        this.mDoReportFullyDrawn = false;
        this.mFragments.doLoaderStop(this.mChangingConfigurations);
        this.mCanEnterPictureInPicture = false;
        if (!this.mStopped) {
            dispatchActivityPreStopped();
            Window window = this.mWindow;
            if (window != null) {
                window.closeAllPanels();
            }
            if (!(preserveWindow || this.mToken == null || this.mParent != null)) {
                WindowManagerGlobal.getInstance().setStoppedState(this.mToken, true);
            }
            this.mFragments.dispatchStop();
            this.mCalled = false;
            long startTime = SystemClock.uptimeMillis();
            this.mInstrumentation.callActivityOnStop(this);
            long took = SystemClock.uptimeMillis() - startTime;
            writeEventLog(LOG_AM_ON_STOP_CALLED, reason, took);
            checkLifecycleTime(took, 100, "onStop");
            if (this.mCalled) {
                synchronized (this.mManagedCursors) {
                    int N = this.mManagedCursors.size();
                    for (int i = 0; i < N; i++) {
                        ManagedCursor mc = (ManagedCursor) this.mManagedCursors.get(i);
                        if (!mc.mReleased) {
                            mc.mCursor.deactivate();
                            mc.mReleased = true;
                        }
                    }
                }
                this.mStopped = true;
                dispatchActivityPostStopped();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Activity ");
                stringBuilder.append(this.mComponent.toShortString());
                stringBuilder.append(" did not call through to super.onStop()");
                throw new SuperNotCalledException(stringBuilder.toString());
            }
        }
        this.mResumed = false;
    }

    /* Access modifiers changed, original: final */
    public final void performDestroy() {
        dispatchActivityPreDestroyed();
        this.mDestroyed = true;
        this.mWindow.destroy();
        this.mFragments.dispatchDestroy();
        long startTime = SystemClock.uptimeMillis();
        onDestroy();
        long took = SystemClock.uptimeMillis() - startTime;
        writeEventLog(LOG_AM_ON_DESTROY_CALLED, "performDestroy", took);
        checkLifecycleTime(took, 100, "onDestroy");
        this.mFragments.doLoaderDestroy();
        VoiceInteractor voiceInteractor = this.mVoiceInteractor;
        if (voiceInteractor != null) {
            voiceInteractor.detachActivity();
        }
        dispatchActivityPostDestroyed();
    }

    /* Access modifiers changed, original: final */
    public final void dispatchMultiWindowModeChanged(boolean isInMultiWindowMode, Configuration newConfig) {
        this.mFragments.dispatchMultiWindowModeChanged(isInMultiWindowMode, newConfig);
        Window window = this.mWindow;
        if (window != null) {
            window.onMultiWindowModeChanged();
        }
        onMultiWindowModeChanged(isInMultiWindowMode, newConfig);
    }

    /* Access modifiers changed, original: final */
    public final void dispatchPictureInPictureModeChanged(boolean isInPictureInPictureMode, Configuration newConfig) {
        this.mFragments.dispatchPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
        Window window = this.mWindow;
        if (window != null) {
            window.onPictureInPictureModeChanged(isInPictureInPictureMode);
        }
        onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig);
    }

    @UnsupportedAppUsage
    public final boolean isResumed() {
        return this.mResumed;
    }

    private void storeHasCurrentPermissionRequest(Bundle bundle) {
        if (bundle != null && this.mHasCurrentPermissionsRequest) {
            bundle.putBoolean(HAS_CURENT_PERMISSIONS_REQUEST_KEY, true);
        }
    }

    private void restoreHasCurrentPermissionRequest(Bundle bundle) {
        if (bundle != null) {
            this.mHasCurrentPermissionsRequest = bundle.getBoolean(HAS_CURENT_PERMISSIONS_REQUEST_KEY, false);
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void dispatchActivityResult(String who, int requestCode, int resultCode, Intent data, String reason) {
        this.mFragments.noteStateNotSaved();
        long startTime = SystemClock.uptimeMillis();
        if (who == null) {
            onActivityResult(requestCode, resultCode, data);
        } else {
            String str = REQUEST_PERMISSIONS_WHO_PREFIX;
            Fragment frag;
            if (who.startsWith(str)) {
                who = who.substring(str.length());
                if (TextUtils.isEmpty(who)) {
                    dispatchRequestPermissionsResult(requestCode, data);
                } else {
                    frag = this.mFragments.findFragmentByWho(who);
                    if (frag != null) {
                        dispatchRequestPermissionsResultToFragment(requestCode, data, frag);
                    }
                }
            } else if (who.startsWith("@android:view:")) {
                Iterator it = WindowManagerGlobal.getInstance().getRootViews(getActivityToken()).iterator();
                while (it.hasNext()) {
                    ViewRootImpl viewRoot = (ViewRootImpl) it.next();
                    if (viewRoot.getView() != null && viewRoot.getView().dispatchActivityResult(who, requestCode, resultCode, data)) {
                        return;
                    }
                }
            } else if (who.startsWith(AUTO_FILL_AUTH_WHO_PREFIX)) {
                getAutofillManager().onAuthenticationResult(requestCode, resultCode == -1 ? data : null, getCurrentFocus());
            } else {
                frag = this.mFragments.findFragmentByWho(who);
                if (frag != null) {
                    frag.onActivityResult(requestCode, resultCode, data);
                }
            }
        }
        long took = SystemClock.uptimeMillis() - startTime;
        writeEventLog(LOG_AM_ON_ACTIVITY_RESULT_CALLED, reason, took);
        checkLifecycleTime(took, 100, "onActivityResult");
    }

    public void startLockTask() {
        try {
            ActivityTaskManager.getService().startLockTaskModeByToken(this.mToken);
        } catch (RemoteException e) {
        }
    }

    public void stopLockTask() {
        try {
            ActivityTaskManager.getService().stopLockTaskModeByToken(this.mToken);
        } catch (RemoteException e) {
        }
    }

    public void showLockTaskEscapeMessage() {
        try {
            ActivityTaskManager.getService().showLockTaskEscapeMessage(this.mToken);
        } catch (RemoteException e) {
        }
    }

    public boolean isOverlayWithDecorCaptionEnabled() {
        return this.mWindow.isOverlayWithDecorCaptionEnabled();
    }

    public void setOverlayWithDecorCaptionEnabled(boolean enabled) {
        this.mWindow.setOverlayWithDecorCaptionEnabled(enabled);
    }

    private void dispatchRequestPermissionsResult(int requestCode, Intent data) {
        String[] permissions;
        int[] grantResults;
        this.mHasCurrentPermissionsRequest = false;
        if (data != null) {
            permissions = data.getStringArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_NAMES);
        } else {
            permissions = new String[0];
        }
        if (data != null) {
            grantResults = data.getIntArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_RESULTS);
        } else {
            grantResults = new int[0];
        }
        onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void dispatchRequestPermissionsResultToFragment(int requestCode, Intent data, Fragment fragment) {
        String[] permissions;
        int[] grantResults;
        if (data != null) {
            permissions = data.getStringArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_NAMES);
        } else {
            permissions = new String[0];
        }
        if (data != null) {
            grantResults = data.getIntArrayExtra(PackageManager.EXTRA_REQUEST_PERMISSIONS_RESULTS);
        } else {
            grantResults = new int[0];
        }
        fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public final void autofillClientAuthenticate(int authenticationId, IntentSender intent, Intent fillInIntent) {
        try {
            startIntentSenderForResultInner(intent, AUTO_FILL_AUTH_WHO_PREFIX, authenticationId, fillInIntent, 0, 0, null);
        } catch (SendIntentException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("authenticate() failed for intent:");
            stringBuilder.append(intent);
            Log.e(TAG, stringBuilder.toString(), e);
        }
    }

    public final void autofillClientResetableStateAvailable() {
        this.mAutoFillResetNeeded = true;
    }

    public final boolean autofillClientRequestShowFillUi(View anchor, int width, int height, Rect anchorBounds, IAutofillWindowPresenter presenter) {
        boolean wasShowing = this.mAutofillPopupWindow;
        if (wasShowing) {
            wasShowing = wasShowing.isShowing();
        } else {
            wasShowing = false;
            this.mAutofillPopupWindow = new AutofillPopupWindow(presenter);
        }
        this.mAutofillPopupWindow.update(anchor, 0, 0, width, height, anchorBounds);
        return !wasShowing && this.mAutofillPopupWindow.isShowing();
    }

    public final void autofillClientDispatchUnhandledKey(View anchor, KeyEvent keyEvent) {
        ViewRootImpl rootImpl = anchor.getViewRootImpl();
        if (rootImpl != null) {
            rootImpl.dispatchKeyFromAutofill(keyEvent);
        }
    }

    public final boolean autofillClientRequestHideFillUi() {
        AutofillPopupWindow autofillPopupWindow = this.mAutofillPopupWindow;
        if (autofillPopupWindow == null) {
            return false;
        }
        autofillPopupWindow.dismiss();
        this.mAutofillPopupWindow = null;
        return true;
    }

    public final boolean autofillClientIsFillUiShowing() {
        AutofillPopupWindow autofillPopupWindow = this.mAutofillPopupWindow;
        return autofillPopupWindow != null && autofillPopupWindow.isShowing();
    }

    public final View[] autofillClientFindViewsByAutofillIdTraversal(AutofillId[] autofillId) {
        View[] views = new View[autofillId.length];
        ArrayList<ViewRootImpl> roots = WindowManagerGlobal.getInstance().getRootViews(getActivityToken());
        for (int rootNum = 0; rootNum < roots.size(); rootNum++) {
            View rootView = ((ViewRootImpl) roots.get(rootNum)).getView();
            if (rootView != null) {
                int viewCount = autofillId.length;
                for (int viewNum = 0; viewNum < viewCount; viewNum++) {
                    if (views[viewNum] == null) {
                        views[viewNum] = rootView.findViewByAutofillIdTraversal(autofillId[viewNum].getViewId());
                    }
                }
            }
        }
        return views;
    }

    public final View autofillClientFindViewByAutofillIdTraversal(AutofillId autofillId) {
        ArrayList<ViewRootImpl> roots = WindowManagerGlobal.getInstance().getRootViews(getActivityToken());
        for (int rootNum = 0; rootNum < roots.size(); rootNum++) {
            View rootView = ((ViewRootImpl) roots.get(rootNum)).getView();
            if (rootView != null) {
                View view = rootView.findViewByAutofillIdTraversal(autofillId.getViewId());
                if (view != null) {
                    return view;
                }
            }
        }
        return null;
    }

    public final boolean[] autofillClientGetViewVisibility(AutofillId[] autofillIds) {
        int autofillIdCount = autofillIds.length;
        boolean[] visible = new boolean[autofillIdCount];
        for (int i = 0; i < autofillIdCount; i++) {
            AutofillId autofillId = autofillIds[i];
            View view = autofillClientFindViewByAutofillIdTraversal(autofillId);
            if (view != null) {
                if (autofillId.isVirtualInt()) {
                    visible[i] = view.isVisibleToUserForAutofill(autofillId.getVirtualChildIntId());
                } else {
                    visible[i] = view.isVisibleToUser();
                }
            }
        }
        if (Helper.sVerbose) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("autofillClientGetViewVisibility(): ");
            stringBuilder.append(Arrays.toString(visible));
            Log.v(TAG, stringBuilder.toString());
        }
        return visible;
    }

    public final View autofillClientFindViewByAccessibilityIdTraversal(int viewId, int windowId) {
        ArrayList<ViewRootImpl> roots = WindowManagerGlobal.getInstance().getRootViews(getActivityToken());
        for (int rootNum = 0; rootNum < roots.size(); rootNum++) {
            View rootView = ((ViewRootImpl) roots.get(rootNum)).getView();
            if (rootView != null && rootView.getAccessibilityWindowId() == windowId) {
                View view = rootView.findViewByAccessibilityIdTraversal(viewId);
                if (view != null) {
                    return view;
                }
            }
        }
        return null;
    }

    public final IBinder autofillClientGetActivityToken() {
        return getActivityToken();
    }

    public final boolean autofillClientIsVisibleForAutofill() {
        return this.mStopped ^ 1;
    }

    public final boolean autofillClientIsCompatibilityModeEnabled() {
        return isAutofillCompatibilityEnabled();
    }

    public final boolean isDisablingEnterExitEventForAutofill() {
        return this.mAutoFillIgnoreFirstResumePause || !this.mResumed;
    }

    @UnsupportedAppUsage
    public void setDisablePreviewScreenshots(boolean disable) {
        try {
            ActivityTaskManager.getService().setDisablePreviewScreenshots(this.mToken, disable);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setShowWhenLocked(boolean showWhenLocked) {
        try {
            ActivityTaskManager.getService().setShowWhenLocked(this.mToken, showWhenLocked);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setInheritShowWhenLocked(boolean inheritShowWhenLocked) {
        try {
            ActivityTaskManager.getService().setInheritShowWhenLocked(this.mToken, inheritShowWhenLocked);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setTurnScreenOn(boolean turnScreenOn) {
        try {
            ActivityTaskManager.getService().setTurnScreenOn(this.mToken, turnScreenOn);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public void registerRemoteAnimations(RemoteAnimationDefinition definition) {
        try {
            ActivityTaskManager.getService().registerRemoteAnimations(this.mToken, definition);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    private void writeEventLog(int event, String reason) {
        EventLog.writeEvent(event, Integer.valueOf(UserHandle.myUserId()), getComponentName().getClassName(), reason);
    }

    private void writeEventLog(int event, String reason, long duration) {
        EventLog.writeEvent(event, Integer.valueOf(UserHandle.myUserId()), getComponentName().getClassName(), reason, Long.valueOf(duration));
    }

    private void checkLifecycleTime(long took, long threshold, String callbackName) {
        if (took > threshold) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Slow Operation: Activity ");
            stringBuilder.append(getComponentName().getPackageName());
            stringBuilder.append("/");
            stringBuilder.append(getComponentName().getShortClassName());
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(callbackName);
            stringBuilder.append(" took ");
            stringBuilder.append(took);
            stringBuilder.append("ms");
            Slog.w(TAG, stringBuilder.toString());
        }
    }
}
