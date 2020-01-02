package android.app;

import android.app.ActivityManager.RunningTaskInfo;
import android.app.ActivityManager.StackInfo;
import android.app.PendingIntent.CanceledException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Insets;
import android.graphics.Matrix;
import android.graphics.Region;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.hardware.input.InputManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.SurfaceControl;
import android.view.SurfaceControl.Builder;
import android.view.SurfaceControl.Transaction;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceSession;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.inputmethod.InputMethodManager;
import dalvik.system.CloseGuard;
import java.util.List;

public class ActivityView extends ViewGroup {
    private static final String DISPLAY_NAME = "ActivityViewVirtualDisplay";
    private static final String TAG = "ActivityView";
    private IActivityTaskManager mActivityTaskManager;
    private StateCallback mActivityViewCallback;
    private Insets mForwardedInsets;
    private final CloseGuard mGuard;
    private final int[] mLocationInWindow;
    private boolean mOpened;
    private SurfaceControl mRootSurfaceControl;
    private final boolean mSingleTaskInstance;
    private final SurfaceCallback mSurfaceCallback;
    private final SurfaceView mSurfaceView;
    private final Region mTapExcludeRegion;
    private TaskStackListener mTaskStackListener;
    private final Transaction mTmpTransaction;
    private VirtualDisplay mVirtualDisplay;

    public static abstract class StateCallback {
        public abstract void onActivityViewDestroyed(ActivityView activityView);

        public abstract void onActivityViewReady(ActivityView activityView);

        public void onTaskCreated(int taskId, ComponentName componentName) {
        }

        public void onTaskMovedToFront(int taskId) {
        }

        public void onTaskRemovalStarted(int taskId) {
        }
    }

    private class SurfaceCallback implements Callback {
        private SurfaceCallback() {
        }

        public void surfaceCreated(SurfaceHolder surfaceHolder) {
            if (ActivityView.this.mVirtualDisplay == null) {
                ActivityView.this.initVirtualDisplay(new SurfaceSession());
                if (!(ActivityView.this.mVirtualDisplay == null || ActivityView.this.mActivityViewCallback == null)) {
                    ActivityView.this.mActivityViewCallback.onActivityViewReady(ActivityView.this);
                }
            } else {
                ActivityView.this.mTmpTransaction.reparent(ActivityView.this.mRootSurfaceControl, ActivityView.this.mSurfaceView.getSurfaceControl()).apply();
            }
            if (ActivityView.this.mVirtualDisplay != null) {
                ActivityView.this.mVirtualDisplay.setDisplayState(true);
            }
            ActivityView.this.updateLocationAndTapExcludeRegion();
        }

        public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
            if (ActivityView.this.mVirtualDisplay != null) {
                ActivityView.this.mVirtualDisplay.resize(width, height, ActivityView.this.getBaseDisplayDensity());
            }
            ActivityView.this.updateLocationAndTapExcludeRegion();
        }

        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            if (ActivityView.this.mVirtualDisplay != null) {
                ActivityView.this.mVirtualDisplay.setDisplayState(false);
            }
            ActivityView.this.clearActivityViewGeometryForIme();
            ActivityView.this.cleanTapExcludeRegion();
        }
    }

    private class TaskStackListenerImpl extends TaskStackListener {
        private TaskStackListenerImpl() {
        }

        public void onTaskDescriptionChanged(RunningTaskInfo taskInfo) throws RemoteException {
            if (ActivityView.this.mVirtualDisplay != null && taskInfo.displayId == ActivityView.this.mVirtualDisplay.getDisplay().getDisplayId()) {
                StackInfo stackInfo = getTopMostStackInfo();
                if (stackInfo != null && taskInfo.taskId == stackInfo.taskIds[stackInfo.taskIds.length - 1]) {
                    ActivityView.this.mSurfaceView.setResizeBackgroundColor(taskInfo.taskDescription.getBackgroundColor());
                }
            }
        }

        public void onTaskMovedToFront(RunningTaskInfo taskInfo) throws RemoteException {
            if (ActivityView.this.mActivityViewCallback != null && ActivityView.this.mVirtualDisplay != null && taskInfo.displayId == ActivityView.this.mVirtualDisplay.getDisplay().getDisplayId()) {
                StackInfo stackInfo = getTopMostStackInfo();
                if (stackInfo != null && taskInfo.taskId == stackInfo.taskIds[stackInfo.taskIds.length - 1]) {
                    ActivityView.this.mActivityViewCallback.onTaskMovedToFront(taskInfo.taskId);
                }
            }
        }

        public void onTaskCreated(int taskId, ComponentName componentName) throws RemoteException {
            if (ActivityView.this.mActivityViewCallback != null && ActivityView.this.mVirtualDisplay != null) {
                StackInfo stackInfo = getTopMostStackInfo();
                if (stackInfo != null && taskId == stackInfo.taskIds[stackInfo.taskIds.length - 1]) {
                    ActivityView.this.mActivityViewCallback.onTaskCreated(taskId, componentName);
                }
            }
        }

        public void onTaskRemovalStarted(RunningTaskInfo taskInfo) throws RemoteException {
            if (ActivityView.this.mActivityViewCallback != null && ActivityView.this.mVirtualDisplay != null && taskInfo.displayId == ActivityView.this.mVirtualDisplay.getDisplay().getDisplayId()) {
                ActivityView.this.mActivityViewCallback.onTaskRemovalStarted(taskInfo.taskId);
            }
        }

        private StackInfo getTopMostStackInfo() throws RemoteException {
            int displayId = ActivityView.this.mVirtualDisplay.getDisplay().getDisplayId();
            List<StackInfo> stackInfoList = ActivityView.this.mActivityTaskManager.getAllStackInfos();
            int stackCount = stackInfoList.size();
            for (int i = 0; i < stackCount; i++) {
                StackInfo stackInfo = (StackInfo) stackInfoList.get(i);
                if (stackInfo.displayId == displayId) {
                    return stackInfo;
                }
            }
            return null;
        }
    }

    public ActivityView(Context context) {
        this(context, null);
    }

    public ActivityView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActivityView(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle, false);
    }

    public ActivityView(Context context, AttributeSet attrs, int defStyle, boolean singleTaskInstance) {
        super(context, attrs, defStyle);
        this.mLocationInWindow = new int[2];
        this.mTapExcludeRegion = new Region();
        this.mGuard = CloseGuard.get();
        this.mTmpTransaction = new Transaction();
        this.mSingleTaskInstance = singleTaskInstance;
        this.mActivityTaskManager = ActivityTaskManager.getService();
        this.mSurfaceView = new SurfaceView(context);
        this.mSurfaceCallback = new SurfaceCallback();
        this.mSurfaceView.getHolder().addCallback(this.mSurfaceCallback);
        addView(this.mSurfaceView);
        this.mOpened = true;
        this.mGuard.open("release");
    }

    public void setCallback(StateCallback callback) {
        this.mActivityViewCallback = callback;
        if (this.mVirtualDisplay != null) {
            StateCallback stateCallback = this.mActivityViewCallback;
            if (stateCallback != null) {
                stateCallback.onActivityViewReady(this);
            }
        }
    }

    public void setCornerRadius(float cornerRadius) {
        this.mSurfaceView.setCornerRadius(cornerRadius);
    }

    public void startActivity(Intent intent) {
        getContext().startActivity(intent, prepareActivityOptions().toBundle());
    }

    public void startActivity(Intent intent, UserHandle user) {
        getContext().startActivityAsUser(intent, prepareActivityOptions().toBundle(), user);
    }

    public void startActivity(PendingIntent pendingIntent) {
        try {
            pendingIntent.send(null, 0, null, null, null, null, prepareActivityOptions().toBundle());
        } catch (CanceledException e) {
            throw new RuntimeException(e);
        }
    }

    public void startActivity(PendingIntent pendingIntent, ActivityOptions options) {
        options.setLaunchDisplayId(this.mVirtualDisplay.getDisplay().getDisplayId());
        try {
            pendingIntent.send(null, 0, null, null, null, null, options.toBundle());
        } catch (CanceledException e) {
            throw new RuntimeException(e);
        }
    }

    private ActivityOptions prepareActivityOptions() {
        if (this.mVirtualDisplay != null) {
            ActivityOptions options = ActivityOptions.makeBasic();
            options.setLaunchDisplayId(this.mVirtualDisplay.getDisplay().getDisplayId());
            return options;
        }
        throw new IllegalStateException("Trying to start activity before ActivityView is ready.");
    }

    public void release() {
        if (this.mVirtualDisplay != null) {
            performRelease();
            return;
        }
        throw new IllegalStateException("Trying to release container that is not initialized.");
    }

    public void onLocationChanged() {
        updateLocationAndTapExcludeRegion();
    }

    private void clearActivityViewGeometryForIme() {
        VirtualDisplay virtualDisplay = this.mVirtualDisplay;
        if (virtualDisplay != null) {
            ((InputMethodManager) this.mContext.getSystemService(InputMethodManager.class)).reportActivityView(virtualDisplay.getDisplay().getDisplayId(), null);
        }
    }

    public void onLayout(boolean changed, int l, int t, int r, int b) {
        this.mSurfaceView.layout(0, 0, r - l, b - t);
    }

    public boolean gatherTransparentRegion(Region region) {
        updateLocationAndTapExcludeRegion();
        return super.gatherTransparentRegion(region);
    }

    private void updateLocationAndTapExcludeRegion() {
        if (this.mVirtualDisplay != null && isAttachedToWindow()) {
            try {
                int x = this.mLocationInWindow[0];
                int y = this.mLocationInWindow[1];
                getLocationInWindow(this.mLocationInWindow);
                if (!(x == this.mLocationInWindow[0] && y == this.mLocationInWindow[1])) {
                    x = this.mLocationInWindow[0];
                    y = this.mLocationInWindow[1];
                    int displayId = this.mVirtualDisplay.getDisplay().getDisplayId();
                    WindowManagerGlobal.getWindowSession().updateDisplayContentLocation(getWindow(), x, y, displayId);
                    Matrix matrix = new Matrix();
                    matrix.set(getMatrix());
                    matrix.postTranslate((float) x, (float) y);
                    ((InputMethodManager) this.mContext.getSystemService(InputMethodManager.class)).reportActivityView(displayId, matrix);
                }
                updateTapExcludeRegion(x, y);
            } catch (RemoteException e) {
                e.rethrowAsRuntimeException();
            }
        }
    }

    private void updateTapExcludeRegion(int x, int y) throws RemoteException {
        if (canReceivePointerEvents()) {
            this.mTapExcludeRegion.set(x, y, getWidth() + x, getHeight() + y);
            ViewParent parent = getParent();
            if (parent != null) {
                parent.subtractObscuredTouchableRegion(this.mTapExcludeRegion, this);
            }
            WindowManagerGlobal.getWindowSession().updateTapExcludeRegion(getWindow(), hashCode(), this.mTapExcludeRegion);
            return;
        }
        cleanTapExcludeRegion();
    }

    /* Access modifiers changed, original: protected */
    public void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        this.mSurfaceView.setVisibility(visibility);
    }

    public int getVirtualDisplayId() {
        VirtualDisplay virtualDisplay = this.mVirtualDisplay;
        if (virtualDisplay != null) {
            return virtualDisplay.getDisplay().getDisplayId();
        }
        return -1;
    }

    public void performBackPress() {
        VirtualDisplay virtualDisplay = this.mVirtualDisplay;
        if (virtualDisplay != null) {
            int displayId = virtualDisplay.getDisplay().getDisplayId();
            InputManager im = InputManager.getInstance();
            im.injectInputEvent(createKeyEvent(0, 4, displayId), 0);
            im.injectInputEvent(createKeyEvent(1, 4, displayId), 0);
        }
    }

    private static KeyEvent createKeyEvent(int action, int code, int displayId) {
        long when = SystemClock.uptimeMillis();
        KeyEvent ev = new KeyEvent(when, when, action, code, 0, 0, -1, 0, 72, 257);
        ev.setDisplayId(displayId);
        return ev;
    }

    private void initVirtualDisplay(SurfaceSession surfaceSession) {
        if (this.mVirtualDisplay == null) {
            int width = this.mSurfaceView.getWidth();
            int height = this.mSurfaceView.getHeight();
            DisplayManager displayManager = (DisplayManager) this.mContext.getSystemService(DisplayManager.class);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ActivityViewVirtualDisplay@");
            stringBuilder.append(System.identityHashCode(this));
            this.mVirtualDisplay = displayManager.createVirtualDisplay(stringBuilder.toString(), width, height, getBaseDisplayDensity(), null, 265);
            VirtualDisplay virtualDisplay = this.mVirtualDisplay;
            String str = TAG;
            if (virtualDisplay == null) {
                Log.e(str, "Failed to initialize ActivityView");
                return;
            }
            int displayId = virtualDisplay.getDisplay().getDisplayId();
            IWindowManager wm = WindowManagerGlobal.getWindowManagerService();
            this.mRootSurfaceControl = new Builder(surfaceSession).setContainerLayer().setParent(this.mSurfaceView.getSurfaceControl()).setName(DISPLAY_NAME).build();
            try {
                WindowManagerGlobal.getWindowSession().reparentDisplayContent(getWindow(), this.mRootSurfaceControl, displayId);
                wm.dontOverrideDisplayInfo(displayId);
                if (this.mSingleTaskInstance) {
                    this.mActivityTaskManager.setDisplayToSingleTaskInstance(displayId);
                }
                wm.setForwardedInsets(displayId, this.mForwardedInsets);
            } catch (RemoteException e) {
                e.rethrowAsRuntimeException();
            }
            this.mTmpTransaction.show(this.mRootSurfaceControl).apply();
            this.mTaskStackListener = new TaskStackListenerImpl();
            try {
                this.mActivityTaskManager.registerTaskStackListener(this.mTaskStackListener);
            } catch (RemoteException e2) {
                Log.e(str, "Failed to register task stack listener", e2);
            }
            return;
        }
        throw new IllegalStateException("Trying to initialize for the second time.");
    }

    private void performRelease() {
        if (this.mOpened) {
            boolean displayReleased;
            this.mSurfaceView.getHolder().removeCallback(this.mSurfaceCallback);
            cleanTapExcludeRegion();
            TaskStackListener taskStackListener = this.mTaskStackListener;
            if (taskStackListener != null) {
                try {
                    this.mActivityTaskManager.unregisterTaskStackListener(taskStackListener);
                } catch (RemoteException e) {
                    Log.e(TAG, "Failed to unregister task stack listener", e);
                }
                this.mTaskStackListener = null;
            }
            VirtualDisplay virtualDisplay = this.mVirtualDisplay;
            if (virtualDisplay != null) {
                virtualDisplay.release();
                this.mVirtualDisplay = null;
                displayReleased = true;
            } else {
                displayReleased = false;
            }
            if (displayReleased) {
                StateCallback stateCallback = this.mActivityViewCallback;
                if (stateCallback != null) {
                    stateCallback.onActivityViewDestroyed(this);
                }
            }
            this.mGuard.close();
            this.mOpened = false;
        }
    }

    private void cleanTapExcludeRegion() {
        if (isAttachedToWindow() && !this.mTapExcludeRegion.isEmpty()) {
            try {
                WindowManagerGlobal.getWindowSession().updateTapExcludeRegion(getWindow(), hashCode(), null);
                this.mTapExcludeRegion.setEmpty();
            } catch (RemoteException e) {
                e.rethrowAsRuntimeException();
            }
        }
    }

    private int getBaseDisplayDensity() {
        WindowManager wm = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.densityDpi;
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mGuard != null) {
                this.mGuard.warnIfOpen();
                performRelease();
            }
            super.finalize();
        } catch (Throwable th) {
            super.finalize();
        }
    }

    public void setForwardedInsets(Insets insets) {
        this.mForwardedInsets = insets;
        if (this.mVirtualDisplay != null) {
            try {
                WindowManagerGlobal.getWindowManagerService().setForwardedInsets(this.mVirtualDisplay.getDisplay().getDisplayId(), this.mForwardedInsets);
            } catch (RemoteException e) {
                e.rethrowAsRuntimeException();
            }
        }
    }
}
