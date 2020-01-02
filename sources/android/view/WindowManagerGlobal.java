package android.view;

import android.animation.ValueAnimator;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.HardwareRenderer;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.SystemProperties;
import android.util.ArraySet;
import android.util.Log;
import android.view.IWindowManager.Stub;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import com.android.internal.util.FastPrintWriter;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public final class WindowManagerGlobal {
    public static final int ADD_APP_EXITING = -4;
    public static final int ADD_BAD_APP_TOKEN = -1;
    public static final int ADD_BAD_SUBWINDOW_TOKEN = -2;
    public static final int ADD_DUPLICATE_ADD = -5;
    public static final int ADD_FLAG_ALWAYS_CONSUME_SYSTEM_BARS = 4;
    public static final int ADD_FLAG_APP_VISIBLE = 2;
    public static final int ADD_FLAG_IN_TOUCH_MODE = 1;
    public static final int ADD_INVALID_DISPLAY = -9;
    public static final int ADD_INVALID_TYPE = -10;
    public static final int ADD_MULTIPLE_SINGLETON = -7;
    public static final int ADD_NOT_APP_TOKEN = -3;
    public static final int ADD_OKAY = 0;
    public static final int ADD_PERMISSION_DENIED = -8;
    public static final int ADD_STARTING_NOT_NEEDED = -6;
    public static final int RELAYOUT_DEFER_SURFACE_DESTROY = 2;
    public static final int RELAYOUT_INSETS_PENDING = 1;
    public static final int RELAYOUT_RES_CONSUME_ALWAYS_SYSTEM_BARS = 64;
    public static final int RELAYOUT_RES_DRAG_RESIZING_DOCKED = 8;
    public static final int RELAYOUT_RES_DRAG_RESIZING_FREEFORM = 16;
    public static final int RELAYOUT_RES_FIRST_TIME = 2;
    public static final int RELAYOUT_RES_IN_TOUCH_MODE = 1;
    public static final int RELAYOUT_RES_SURFACE_CHANGED = 4;
    public static final int RELAYOUT_RES_SURFACE_RESIZED = 32;
    private static final String TAG = "WindowManager";
    @UnsupportedAppUsage
    private static WindowManagerGlobal sDefaultWindowManager;
    @UnsupportedAppUsage
    private static IWindowManager sWindowManagerService;
    @UnsupportedAppUsage
    private static IWindowSession sWindowSession;
    private final ArraySet<View> mDyingViews = new ArraySet();
    @UnsupportedAppUsage
    private final Object mLock = new Object();
    @UnsupportedAppUsage
    private final ArrayList<LayoutParams> mParams = new ArrayList();
    @UnsupportedAppUsage
    private final ArrayList<ViewRootImpl> mRoots = new ArrayList();
    private Runnable mSystemPropertyUpdater;
    @UnsupportedAppUsage
    private final ArrayList<View> mViews = new ArrayList();

    private WindowManagerGlobal() {
    }

    @UnsupportedAppUsage
    public static void initialize() {
        getWindowManagerService();
    }

    @UnsupportedAppUsage
    public static WindowManagerGlobal getInstance() {
        WindowManagerGlobal windowManagerGlobal;
        synchronized (WindowManagerGlobal.class) {
            if (sDefaultWindowManager == null) {
                sDefaultWindowManager = new WindowManagerGlobal();
            }
            windowManagerGlobal = sDefaultWindowManager;
        }
        return windowManagerGlobal;
    }

    @UnsupportedAppUsage
    public static IWindowManager getWindowManagerService() {
        IWindowManager iWindowManager;
        synchronized (WindowManagerGlobal.class) {
            if (sWindowManagerService == null) {
                sWindowManagerService = Stub.asInterface(ServiceManager.getService(Context.WINDOW_SERVICE));
                try {
                    if (sWindowManagerService != null) {
                        ValueAnimator.setDurationScale(sWindowManagerService.getCurrentAnimatorScale());
                    }
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            iWindowManager = sWindowManagerService;
        }
        return iWindowManager;
    }

    @UnsupportedAppUsage
    public static IWindowSession getWindowSession() {
        IWindowSession iWindowSession;
        synchronized (WindowManagerGlobal.class) {
            if (sWindowSession == null) {
                try {
                    InputMethodManager.ensureDefaultInstanceForDefaultDisplayIfNecessary();
                    sWindowSession = getWindowManagerService().openSession(new IWindowSessionCallback.Stub() {
                        public void onAnimatorScaleChanged(float scale) {
                            ValueAnimator.setDurationScale(scale);
                        }
                    });
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            iWindowSession = sWindowSession;
        }
        return iWindowSession;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static IWindowSession peekWindowSession() {
        IWindowSession iWindowSession;
        synchronized (WindowManagerGlobal.class) {
            iWindowSession = sWindowSession;
        }
        return iWindowSession;
    }

    @UnsupportedAppUsage
    public String[] getViewRootNames() {
        String[] mViewRoots;
        synchronized (this.mLock) {
            int numRoots = this.mRoots.size();
            mViewRoots = new String[numRoots];
            for (int i = 0; i < numRoots; i++) {
                mViewRoots[i] = getWindowName((ViewRootImpl) this.mRoots.get(i));
            }
        }
        return mViewRoots;
    }

    @UnsupportedAppUsage
    public ArrayList<ViewRootImpl> getRootViews(IBinder token) {
        ArrayList<ViewRootImpl> views = new ArrayList();
        synchronized (this.mLock) {
            int numRoots = this.mRoots.size();
            for (int i = 0; i < numRoots; i++) {
                LayoutParams params = (LayoutParams) this.mParams.get(i);
                if (params.token != null) {
                    if (params.token != token) {
                        boolean isChild = false;
                        if (params.type >= 1000 && params.type <= 1999) {
                            for (int j = 0; j < numRoots; j++) {
                                LayoutParams paramsj = (LayoutParams) this.mParams.get(j);
                                if (params.token == ((View) this.mViews.get(j)).getWindowToken() && paramsj.token == token) {
                                    isChild = true;
                                    break;
                                }
                            }
                        }
                        if (!isChild) {
                        }
                    }
                    views.add((ViewRootImpl) this.mRoots.get(i));
                }
            }
        }
        return views;
    }

    public ArrayList<View> getWindowViews() {
        ArrayList arrayList;
        synchronized (this.mLock) {
            arrayList = new ArrayList(this.mViews);
        }
        return arrayList;
    }

    public View getWindowView(IBinder windowToken) {
        synchronized (this.mLock) {
            int numViews = this.mViews.size();
            for (int i = 0; i < numViews; i++) {
                View view = (View) this.mViews.get(i);
                if (view.getWindowToken() == windowToken) {
                    return view;
                }
            }
            return null;
        }
    }

    @UnsupportedAppUsage
    public View getRootView(String name) {
        synchronized (this.mLock) {
            for (int i = this.mRoots.size() - 1; i >= 0; i--) {
                ViewRootImpl root = (ViewRootImpl) this.mRoots.get(i);
                if (name.equals(getWindowName(root))) {
                    View view = root.getView();
                    return view;
                }
            }
            return null;
        }
    }

    public void addView(View view, ViewGroup.LayoutParams params, Display display, Window parentWindow) {
        if (view == null) {
            throw new IllegalArgumentException("view must not be null");
        } else if (display == null) {
            throw new IllegalArgumentException("display must not be null");
        } else if (params instanceof LayoutParams) {
            LayoutParams wparams = (LayoutParams) params;
            if (parentWindow != null) {
                parentWindow.adjustLayoutParamsForSubWindow(wparams);
            } else {
                Context context = view.getContext();
                if (!(context == null || (context.getApplicationInfo().flags & 536870912) == 0)) {
                    wparams.flags |= 16777216;
                }
            }
            View panelParentView = null;
            synchronized (this.mLock) {
                if (this.mSystemPropertyUpdater == null) {
                    this.mSystemPropertyUpdater = new Runnable() {
                        public void run() {
                            synchronized (WindowManagerGlobal.this.mLock) {
                                for (int i = WindowManagerGlobal.this.mRoots.size() - 1; i >= 0; i--) {
                                    ((ViewRootImpl) WindowManagerGlobal.this.mRoots.get(i)).loadSystemProperties();
                                }
                            }
                        }
                    };
                    SystemProperties.addChangeCallback(this.mSystemPropertyUpdater);
                }
                int index = findViewLocked(view, false);
                if (index >= 0) {
                    if (this.mDyingViews.contains(view)) {
                        ((ViewRootImpl) this.mRoots.get(index)).doDie();
                    } else {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("View ");
                        stringBuilder.append(view);
                        stringBuilder.append(" has already been added to the window manager.");
                        throw new IllegalStateException(stringBuilder.toString());
                    }
                }
                if (wparams.type >= 1000 && wparams.type <= 1999) {
                    int count = this.mViews.size();
                    for (int i = 0; i < count; i++) {
                        if (((ViewRootImpl) this.mRoots.get(i)).mWindow.asBinder() == wparams.token) {
                            panelParentView = (View) this.mViews.get(i);
                        }
                    }
                }
                ViewRootImpl root = new ViewRootImpl(view.getContext(), display);
                view.setLayoutParams(wparams);
                this.mViews.add(view);
                this.mRoots.add(root);
                this.mParams.add(wparams);
                try {
                    root.setView(view, wparams, panelParentView);
                } catch (RuntimeException e) {
                    int index2 = findViewLocked(view, false);
                    if (index2 >= 0) {
                        Log.e(TAG, "BadTokenException or InvalidDisplayException, clean up.");
                        removeViewLocked(index2, true);
                    }
                    throw e;
                }
            }
        } else {
            throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
        }
    }

    private boolean updateBlurRatioOnly(View view, LayoutParams wparams) {
        if ((wparams.flags & 4) != 0) {
            ViewRootImpl viewRoot = view.getViewRootImpl();
            if (!(viewRoot == null || viewRoot.mWindowAttributes == null || viewRoot.mWindowAttributes.blurRatio == wparams.blurRatio)) {
                LayoutParams viewLayoutParams = new LayoutParams();
                viewLayoutParams.copyFrom(viewRoot.mWindowAttributes);
                int windowParamsChangesFlag = viewLayoutParams.copyFrom(wparams);
                int blurRatioChangedFlag = 1073741824 & windowParamsChangesFlag;
                int otherParameterChangedFlag = -1073741825 & windowParamsChangesFlag;
                if ((wparams.softInputMode & 240) == 0) {
                    otherParameterChangedFlag = windowParamsChangesFlag & -1073742337;
                }
                viewRoot.mWindowAttributes.blurRatio = wparams.blurRatio;
                viewRoot.updateBlurRatio(wparams.blurRatio);
                if (blurRatioChangedFlag != 0 && otherParameterChangedFlag == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public void updateViewLayout(View view, ViewGroup.LayoutParams params) {
        if (view == null) {
            throw new IllegalArgumentException("view must not be null");
        } else if (params instanceof LayoutParams) {
            LayoutParams wparams = (LayoutParams) params;
            if (!updateBlurRatioOnly(view, wparams)) {
                view.setLayoutParams(wparams);
                synchronized (this.mLock) {
                    int index = findViewLocked(view, 1);
                    ViewRootImpl root = (ViewRootImpl) this.mRoots.get(index);
                    this.mParams.remove(index);
                    this.mParams.add(index, wparams);
                    root.setLayoutParams(wparams, false);
                }
            }
        } else {
            throw new IllegalArgumentException("Params must be WindowManager.LayoutParams");
        }
    }

    @UnsupportedAppUsage
    public void removeView(View view, boolean immediate) {
        if (view != null) {
            synchronized (this.mLock) {
                int index = findViewLocked(view, 1);
                View curView = ((ViewRootImpl) this.mRoots.get(index)).getView();
                removeViewLocked(index, immediate);
                if (curView == view) {
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Calling with view ");
                    stringBuilder.append(view);
                    stringBuilder.append(" but the ViewAncestor is attached to ");
                    stringBuilder.append(curView);
                    throw new IllegalStateException(stringBuilder.toString());
                }
            }
            return;
        }
        throw new IllegalArgumentException("view must not be null");
    }

    public void closeAll(IBinder token, String who, String what) {
        closeAllExceptView(token, null, who, what);
    }

    public void closeAllExceptView(IBinder token, View view, String who, String what) {
        synchronized (this.mLock) {
            int count = this.mViews.size();
            int i = 0;
            while (i < count) {
                if ((view == null || this.mViews.get(i) != view) && (token == null || ((LayoutParams) this.mParams.get(i)).token == token)) {
                    ViewRootImpl root = (ViewRootImpl) this.mRoots.get(i);
                    if (who != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(what);
                        stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                        stringBuilder.append(who);
                        stringBuilder.append(" has leaked window ");
                        stringBuilder.append(root.getView());
                        stringBuilder.append(" that was originally added here");
                        WindowLeaked leak = new WindowLeaked(stringBuilder.toString());
                        leak.setStackTrace(root.getLocation().getStackTrace());
                        Log.e(TAG, "", leak);
                    }
                    removeViewLocked(i, false);
                }
                i++;
            }
        }
    }

    private void removeViewLocked(int index, boolean immediate) {
        ViewRootImpl root = (ViewRootImpl) this.mRoots.get(index);
        View view = root.getView();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(InputMethodManager.class);
            if (imm != null) {
                imm.windowDismissed(((View) this.mViews.get(index)).getWindowToken());
            }
        }
        boolean deferred = root.die(immediate);
        if (view != null) {
            view.assignParent(null);
            if (deferred) {
                this.mDyingViews.add(view);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doRemoveView(ViewRootImpl root) {
        synchronized (this.mLock) {
            int index = this.mRoots.indexOf(root);
            if (index >= 0) {
                this.mRoots.remove(index);
                this.mParams.remove(index);
                this.mDyingViews.remove((View) this.mViews.remove(index));
            }
        }
        if (ThreadedRenderer.sTrimForeground && ThreadedRenderer.isAvailable()) {
            doTrimForeground();
        }
    }

    private int findViewLocked(View view, boolean required) {
        int index = this.mViews.indexOf(view);
        if (!required || index >= 0) {
            return index;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("View=");
        stringBuilder.append(view);
        stringBuilder.append(" not attached to window manager");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public static boolean shouldDestroyEglContext(int trimLevel) {
        if (trimLevel >= 80) {
            return true;
        }
        if (trimLevel < 60 || ActivityManager.isHighEndGfx()) {
            return false;
        }
        return true;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public void trimMemory(int level) {
        if (ThreadedRenderer.isAvailable()) {
            if (shouldDestroyEglContext(level)) {
                synchronized (this.mLock) {
                    for (int i = this.mRoots.size() - 1; i >= 0; i--) {
                        ((ViewRootImpl) this.mRoots.get(i)).destroyHardwareResources();
                    }
                }
                level = 80;
            }
            HardwareRenderer.trimMemory(level);
            if (ThreadedRenderer.sTrimForeground) {
                doTrimForeground();
            }
        }
    }

    public static void trimForeground() {
        if (ThreadedRenderer.sTrimForeground && ThreadedRenderer.isAvailable()) {
            getInstance().doTrimForeground();
        }
    }

    private void doTrimForeground() {
        boolean hasVisibleWindows = false;
        synchronized (this.mLock) {
            for (int i = this.mRoots.size() - 1; i >= 0; i--) {
                ViewRootImpl root = (ViewRootImpl) this.mRoots.get(i);
                if (root.mView == null || root.getHostVisibility() != 0 || root.mAttachInfo.mThreadedRenderer == null) {
                    root.destroyHardwareResources();
                } else {
                    hasVisibleWindows = true;
                }
            }
        }
        if (!hasVisibleWindows) {
            HardwareRenderer.trimMemory(80);
        }
    }

    public void dumpGfxInfo(FileDescriptor fd, String[] args) {
        Throwable th;
        FileDescriptor fileDescriptor = fd;
        PrintWriter pw = new FastPrintWriter(new FileOutputStream(fileDescriptor));
        String[] strArr;
        try {
            synchronized (this.mLock) {
                try {
                    int i;
                    int count = this.mViews.size();
                    pw.println("Profile data in ms:");
                    int i2 = 0;
                    while (true) {
                        i = 0;
                        if (i2 >= count) {
                            break;
                        }
                        pw.printf("\n\t%s (visibility=%d)", new Object[]{getWindowName(root), Integer.valueOf(((ViewRootImpl) this.mRoots.get(i2)).getHostVisibility())});
                        ThreadedRenderer renderer = root.getView().mAttachInfo.mThreadedRenderer;
                        if (renderer != null) {
                            try {
                                renderer.dumpGfxInfo(pw, fileDescriptor, args);
                            } catch (Throwable th2) {
                                th = th2;
                                try {
                                    throw th;
                                } catch (Throwable th3) {
                                    th = th3;
                                }
                            }
                        } else {
                            strArr = args;
                        }
                        i2++;
                    }
                    strArr = args;
                    pw.println("\nView hierarchy:\n");
                    i2 = 0;
                    int displayListsSize = 0;
                    int[] info = new int[2];
                    int i3 = 0;
                    while (i3 < count) {
                        ((ViewRootImpl) this.mRoots.get(i3)).dumpGfxInfo(info);
                        pw.printf("  %s\n  %d views, %.2f kB of display lists", new Object[]{getWindowName(root), Integer.valueOf(info[i]), Float.valueOf(((float) info[1]) / 1024.0f)});
                        pw.printf("\n\n", new Object[0]);
                        i2 += info[0];
                        displayListsSize += info[1];
                        i3++;
                        int i4 = 2;
                        i = 0;
                    }
                    pw.printf("\nTotal ViewRootImpl: %d\n", new Object[]{Integer.valueOf(count)});
                    pw.printf("Total Views:        %d\n", new Object[]{Integer.valueOf(i2)});
                    pw.printf("Total DisplayList:  %.2f kB\n\n", new Object[]{Float.valueOf(((float) displayListsSize) / 1024.0f)});
                    pw.flush();
                } catch (Throwable th4) {
                    th = th4;
                    strArr = args;
                    throw th;
                }
            }
        } catch (Throwable th5) {
            th = th5;
            strArr = args;
            pw.flush();
            throw th;
        }
    }

    private static String getWindowName(ViewRootImpl root) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(root.mWindowAttributes.getTitle());
        stringBuilder.append("/");
        stringBuilder.append(root.getClass().getName());
        stringBuilder.append('@');
        stringBuilder.append(Integer.toHexString(root.hashCode()));
        return stringBuilder.toString();
    }

    public void setStoppedState(IBinder token, boolean stopped) {
        ArrayList<ViewRootImpl> nonCurrentThreadRoots = null;
        synchronized (this.mLock) {
            int i = this.mViews.size() - 1;
            while (i >= 0) {
                if (token == null || ((LayoutParams) this.mParams.get(i)).token == token) {
                    ViewRootImpl root = (ViewRootImpl) this.mRoots.get(i);
                    if (root.mThread == Thread.currentThread()) {
                        root.setWindowStopped(stopped);
                    } else {
                        if (nonCurrentThreadRoots == null) {
                            nonCurrentThreadRoots = new ArrayList();
                        }
                        nonCurrentThreadRoots.add(root);
                    }
                    setStoppedState(root.mAttachInfo.mWindowToken, stopped);
                }
                i--;
            }
        }
        if (nonCurrentThreadRoots != null) {
            for (int i2 = nonCurrentThreadRoots.size() - 1; i2 >= 0; i2--) {
                ViewRootImpl root2 = (ViewRootImpl) nonCurrentThreadRoots.get(i2);
                root2.mHandler.runWithScissors(new -$$Lambda$WindowManagerGlobal$2bR3FsEm4EdRwuXfttH0wA2xOW4(root2, stopped), 0);
            }
        }
    }

    public void reportNewConfiguration(Configuration config) {
        synchronized (this.mLock) {
            int count = this.mViews.size();
            config = new Configuration(config);
            for (int i = 0; i < count; i++) {
                ((ViewRootImpl) this.mRoots.get(i)).requestUpdateConfiguration(config);
            }
        }
    }

    public void changeCanvasOpacity(IBinder token, boolean opaque) {
        if (token != null) {
            synchronized (this.mLock) {
                for (int i = this.mParams.size() - 1; i >= 0; i--) {
                    if (((LayoutParams) this.mParams.get(i)).token == token) {
                        ((ViewRootImpl) this.mRoots.get(i)).changeCanvasOpacity(opaque);
                        return;
                    }
                }
            }
        }
    }
}
