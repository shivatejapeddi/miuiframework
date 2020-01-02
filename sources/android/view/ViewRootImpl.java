package android.view;

import android.Manifest.permission;
import android.animation.LayoutTransition;
import android.annotation.UnsupportedAppUsage;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ResourcesManager;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.res.CompatibilityInfo;
import android.content.res.CompatibilityInfo.Translator;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.HardwareRenderer;
import android.graphics.HardwareRenderer.FrameDrawingCallback;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RecordingCanvas;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.RenderNode;
import android.graphics.drawable.Drawable;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.net.TrafficStats;
import android.os.Binder;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.ParcelFileDescriptor.AutoCloseOutputStream;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.sysprop.DisplayProperties;
import android.util.AndroidRuntimeException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LongArray;
import android.util.MergedConfiguration;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Choreographer.FrameCallback;
import android.view.DisplayCutout.ParcelableWrapper;
import android.view.InputDevice.MotionRange;
import android.view.InputQueue.Callback;
import android.view.KeyCharacterMap.FallbackAction;
import android.view.Surface.OutOfResourcesException;
import android.view.SurfaceControl.Builder;
import android.view.SurfaceControl.Transaction;
import android.view.SurfaceHolder.Callback2;
import android.view.View.MeasureSpec;
import android.view.ViewTreeObserver.InternalInsetsInfo;
import android.view.WindowManager.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener;
import android.view.accessibility.AccessibilityManager.HighTextContrastChangeListener;
import android.view.accessibility.AccessibilityNodeIdManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction;
import android.view.accessibility.AccessibilityNodeProvider;
import android.view.accessibility.IAccessibilityInteractionConnection.Stub;
import android.view.accessibility.IAccessibilityInteractionConnectionCallback;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.autofill.AutofillManager;
import android.view.inputmethod.InputMethodManager;
import android.view.inputmethod.InputMethodManager.FinishedInputEventCallback;
import android.widget.Scroller;
import com.android.internal.R;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.os.IResultReceiver;
import com.android.internal.os.SomeArgs;
import com.android.internal.policy.DecorView;
import com.android.internal.policy.PhoneFallbackEventHandler;
import com.android.internal.util.Preconditions;
import com.android.internal.view.BaseSurfaceHolder;
import com.android.internal.view.RootViewSurfaceTaker;
import com.android.internal.view.SurfaceCallbackHelper;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public final class ViewRootImpl implements ViewParent, Callbacks, DrawCallbacks {
    private static final boolean DBG = false;
    private static final boolean DEBUG_CONFIGURATION = false;
    private static final boolean DEBUG_CONTENT_CAPTURE = false;
    private static final boolean DEBUG_DIALOG = false;
    private static final boolean DEBUG_DRAW = false;
    private static final boolean DEBUG_FPS = false;
    private static final boolean DEBUG_IMF = false;
    private static final boolean DEBUG_INPUT_RESIZE = false;
    private static final boolean DEBUG_INPUT_STAGES = false;
    private static final boolean DEBUG_KEEP_SCREEN_ON = false;
    private static final boolean DEBUG_LAYOUT = false;
    private static final boolean DEBUG_ORIENTATION = false;
    private static final boolean DEBUG_TRACKBALL = false;
    private static final boolean LOCAL_LOGV = false;
    private static final int MAX_QUEUED_INPUT_EVENT_POOL_SIZE = 10;
    static final int MAX_TRACKBALL_DELAY = 250;
    private static final int MSG_CHECK_FOCUS = 13;
    private static final int MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST = 21;
    private static final int MSG_CLOSE_SYSTEM_DIALOGS = 14;
    private static final int MSG_DIE = 3;
    private static final int MSG_DISPATCH_APP_VISIBILITY = 8;
    private static final int MSG_DISPATCH_DRAG_EVENT = 15;
    private static final int MSG_DISPATCH_DRAG_LOCATION_EVENT = 16;
    private static final int MSG_DISPATCH_GET_NEW_SURFACE = 9;
    private static final int MSG_DISPATCH_INPUT_EVENT = 7;
    private static final int MSG_DISPATCH_KEY_FROM_AUTOFILL = 12;
    private static final int MSG_DISPATCH_KEY_FROM_IME = 11;
    private static final int MSG_DISPATCH_SYSTEM_UI_VISIBILITY = 17;
    private static final int MSG_DISPATCH_WINDOW_SHOWN = 25;
    private static final int MSG_DRAW_FINISHED = 29;
    private static final int MSG_INSETS_CHANGED = 30;
    private static final int MSG_INSETS_CONTROL_CHANGED = 31;
    private static final int MSG_INVALIDATE = 1;
    private static final int MSG_INVALIDATE_RECT = 2;
    private static final int MSG_INVALIDATE_WORLD = 22;
    private static final int MSG_POINTER_CAPTURE_CHANGED = 28;
    private static final int MSG_PROCESS_INPUT_EVENTS = 19;
    private static final int MSG_REQUEST_KEYBOARD_SHORTCUTS = 26;
    private static final int MSG_RESIZED = 4;
    private static final int MSG_RESIZED_REPORT = 5;
    private static final int MSG_SYNTHESIZE_INPUT_EVENT = 24;
    private static final int MSG_SYSTEM_GESTURE_EXCLUSION_CHANGED = 32;
    private static final int MSG_UPDATE_CONFIGURATION = 18;
    private static final int MSG_UPDATE_POINTER_ICON = 27;
    private static final int MSG_WINDOW_FOCUS_CHANGED = 6;
    private static final int MSG_WINDOW_MOVED = 23;
    private static final boolean MT_RENDERER_AVAILABLE = true;
    public static final int NEW_INSETS_MODE_FULL = 2;
    public static final int NEW_INSETS_MODE_IME = 1;
    public static final int NEW_INSETS_MODE_NONE = 0;
    public static final String PROPERTY_EMULATOR_WIN_OUTSET_BOTTOM_PX = "ro.emu.win_outset_bottom_px";
    private static final String PROPERTY_PROFILE_RENDERING = "viewroot.profile_rendering";
    private static final String TAG = "ViewRootImpl";
    private static final String USE_NEW_INSETS_PROPERTY = "persist.wm.new_insets";
    static final Interpolator mResizeInterpolator = new AccelerateDecelerateInterpolator();
    private static boolean sAlwaysAssignFocus;
    private static boolean sCompatibilityDone = false;
    private static final ArrayList<ConfigChangedCallback> sConfigCallbacks = new ArrayList();
    static boolean sFirstDrawComplete = false;
    static final ArrayList<Runnable> sFirstDrawHandlers = new ArrayList();
    public static int sNewInsetsMode = SystemProperties.getInt(USE_NEW_INSETS_PROPERTY, 0);
    @UnsupportedAppUsage
    static final ThreadLocal<HandlerActionQueue> sRunQueues = new ThreadLocal();
    SurfaceControl BlurSurfaceControl;
    SurfaceSession BlurSurfaceSession;
    View mAccessibilityFocusedHost;
    AccessibilityNodeInfo mAccessibilityFocusedVirtualView;
    final AccessibilityInteractionConnectionManager mAccessibilityInteractionConnectionManager;
    AccessibilityInteractionController mAccessibilityInteractionController;
    final AccessibilityManager mAccessibilityManager;
    private ActivityConfigCallback mActivityConfigCallback;
    private boolean mActivityRelaunched;
    @UnsupportedAppUsage
    boolean mAdded;
    boolean mAddedTouchMode;
    private boolean mAppVisibilityChanged;
    boolean mAppVisible = true;
    boolean mApplyInsetsRequested;
    @UnsupportedAppUsage
    final AttachInfo mAttachInfo;
    AudioManager mAudioManager;
    final String mBasePackageName;
    public final Surface mBoundsSurface;
    private SurfaceControl mBoundsSurfaceControl;
    private int mCanvasOffsetX;
    private int mCanvasOffsetY;
    Choreographer mChoreographer;
    int mClientWindowLayoutFlags;
    final ConsumeBatchedInputImmediatelyRunnable mConsumeBatchedInputImmediatelyRunnable;
    boolean mConsumeBatchedInputImmediatelyScheduled;
    boolean mConsumeBatchedInputScheduled;
    final ConsumeBatchedInputRunnable mConsumedBatchedInputRunnable;
    @UnsupportedAppUsage
    public final Context mContext;
    int mCurScrollY;
    View mCurrentDragView;
    private PointerIcon mCustomPointerIcon;
    private final int mDensity;
    @UnsupportedAppUsage
    Rect mDirty;
    final Rect mDispatchContentInsets;
    DisplayCutout mDispatchDisplayCutout;
    final Rect mDispatchStableInsets;
    Display mDisplay;
    private final DisplayListener mDisplayListener;
    final DisplayManager mDisplayManager;
    ClipDescription mDragDescription;
    final PointF mDragPoint;
    private boolean mDragResizing;
    boolean mDrawingAllowed;
    int mDrawsNeededToReport;
    @UnsupportedAppUsage
    FallbackEventHandler mFallbackEventHandler;
    boolean mFirst;
    InputStage mFirstInputStage;
    InputStage mFirstPostImeInputStage;
    private boolean mForceDecorViewVisibility;
    private boolean mForceNextConfigUpdate;
    boolean mForceNextWindowRelayout;
    private int mFpsNumFrames;
    private long mFpsPrevTime;
    private long mFpsStartTime;
    boolean mFullRedrawNeeded;
    private final GestureExclusionTracker mGestureExclusionTracker;
    boolean mHadWindowFocus;
    final ViewRootHandler mHandler;
    boolean mHandlingLayoutInLayoutRequest;
    int mHardwareXOffset;
    int mHardwareYOffset;
    boolean mHasHadWindowFocus;
    boolean mHaveMoveEvent;
    @UnsupportedAppUsage
    int mHeight;
    final HighContrastTextManager mHighContrastTextManager;
    private boolean mInLayout;
    InputChannel mInputChannel;
    private final InputEventCompatProcessor mInputCompatProcessor;
    protected final InputEventConsistencyVerifier mInputEventConsistencyVerifier;
    WindowInputEventReceiver mInputEventReceiver;
    InputQueue mInputQueue;
    Callback mInputQueueCallback;
    private final InsetsController mInsetsController;
    final InvalidateOnAnimationRunnable mInvalidateOnAnimationRunnable;
    private boolean mInvalidateRootRequested;
    boolean mIsAmbientMode;
    public boolean mIsAnimating;
    boolean mIsCreating;
    boolean mIsDrawing;
    boolean mIsInTraversal;
    private final Configuration mLastConfigurationFromResources;
    final InternalInsetsInfo mLastGivenInsets;
    boolean mLastInCompatMode;
    boolean mLastOverscanRequested;
    private final MergedConfiguration mLastReportedMergedConfiguration;
    @UnsupportedAppUsage
    WeakReference<View> mLastScrolledFocus;
    int mLastSystemUiVisibility;
    final PointF mLastTouchPoint;
    int mLastTouchSource;
    boolean mLastWasImTarget;
    private WindowInsets mLastWindowInsets;
    boolean mLayoutRequested;
    ArrayList<View> mLayoutRequesters;
    volatile Object mLocalDragState;
    final WindowLeaked mLocation;
    boolean mLostWindowFocus;
    private boolean mNeedsRendererSetup;
    boolean mNewSurfaceNeeded;
    private final int mNoncompatDensity;
    int mOrigWindowType;
    boolean mPausedForTransition;
    boolean mPendingAlwaysConsumeSystemBars;
    final Rect mPendingBackDropFrame;
    final Rect mPendingContentInsets;
    final ParcelableWrapper mPendingDisplayCutout;
    int mPendingInputEventCount;
    QueuedInputEvent mPendingInputEventHead;
    String mPendingInputEventQueueLengthCounterName;
    QueuedInputEvent mPendingInputEventTail;
    private final MergedConfiguration mPendingMergedConfiguration;
    final Rect mPendingOutsets;
    final Rect mPendingOverscanInsets;
    final Rect mPendingStableInsets;
    private ArrayList<LayoutTransition> mPendingTransitions;
    final Rect mPendingVisibleInsets;
    boolean mPointerCapture;
    private int mPointerIconType;
    final Region mPreviousTransparentRegion;
    boolean mProcessInputEventsScheduled;
    private boolean mProfile;
    private boolean mProfileRendering;
    private QueuedInputEvent mQueuedInputEventPool;
    private int mQueuedInputEventPoolSize;
    private boolean mRemoved;
    private FrameCallback mRenderProfiler;
    private boolean mRenderProfilingEnabled;
    boolean mReportNextDraw;
    private int mResizeMode;
    boolean mScrollMayChange;
    int mScrollY;
    Scroller mScroller;
    SendWindowContentChangedAccessibilityEvent mSendWindowContentChangedAccessibilityEvent;
    int mSeq;
    int mSoftInputMode;
    @UnsupportedAppUsage
    boolean mStopped;
    @UnsupportedAppUsage
    public final Surface mSurface;
    private final SurfaceControl mSurfaceControl;
    BaseSurfaceHolder mSurfaceHolder;
    Callback2 mSurfaceHolderCallback;
    private SurfaceSession mSurfaceSession;
    InputStage mSyntheticInputStage;
    private String mTag;
    final int mTargetSdkVersion;
    private final Rect mTempBoundsRect;
    HashSet<View> mTempHashSet;
    private InsetsState mTempInsets;
    final Rect mTempRect;
    final Thread mThread;
    final Rect mTmpFrame;
    final int[] mTmpLocation = new int[2];
    final TypedValue mTmpValue = new TypedValue();
    private final Transaction mTransaction;
    Translator mTranslator;
    final Region mTransparentRegion;
    int mTraversalBarrier;
    final TraversalRunnable mTraversalRunnable;
    public boolean mTraversalScheduled;
    boolean mUnbufferedInputDispatch;
    private final UnhandledKeyManager mUnhandledKeyManager;
    @GuardedBy({"this"})
    boolean mUpcomingInTouchMode;
    @GuardedBy({"this"})
    boolean mUpcomingWindowFocus;
    private boolean mUseMTRenderer;
    @UnsupportedAppUsage
    View mView;
    final ViewConfiguration mViewConfiguration;
    private int mViewLayoutDirectionInitial;
    int mViewVisibility;
    final Rect mVisRect;
    @UnsupportedAppUsage
    int mWidth;
    boolean mWillDrawSoon;
    final Rect mWinFrame;
    final W mWindow;
    public final LayoutParams mWindowAttributes = new LayoutParams();
    boolean mWindowAttributesChanged;
    int mWindowAttributesChangesFlag;
    @GuardedBy({"mWindowCallbacks"})
    final ArrayList<WindowCallbacks> mWindowCallbacks = new ArrayList();
    CountDownLatch mWindowDrawCountDown;
    @GuardedBy({"this"})
    boolean mWindowFocusChanged;
    @UnsupportedAppUsage
    final IWindowSession mWindowSession;
    private final ArrayList<WindowStoppedCallback> mWindowStoppedCallbacks;

    public interface ConfigChangedCallback {
        void onConfigurationChanged(Configuration configuration);
    }

    interface WindowStoppedCallback {
        void windowStopped(boolean z);
    }

    static final class AccessibilityInteractionConnection extends Stub {
        private final WeakReference<ViewRootImpl> mViewRootImpl;

        AccessibilityInteractionConnection(ViewRootImpl viewRootImpl) {
            this.mViewRootImpl = new WeakReference(viewRootImpl);
        }

        public void findAccessibilityNodeInfoByAccessibilityId(long accessibilityNodeId, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec, Bundle args) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setFindAccessibilityNodeInfosResult(null, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfoByAccessibilityIdClientThread(accessibilityNodeId, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec, args);
            int i = interactionId;
            IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback = callback;
        }

        public void performAccessibilityAction(long accessibilityNodeId, int action, Bundle arguments, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setPerformAccessibilityActionResult(false, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().performAccessibilityActionClientThread(accessibilityNodeId, action, arguments, interactionId, callback, flags, interrogatingPid, interrogatingTid);
            int i = interactionId;
            IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback = callback;
        }

        public void findAccessibilityNodeInfosByViewId(long accessibilityNodeId, String viewId, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setFindAccessibilityNodeInfoResult(null, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfosByViewIdClientThread(accessibilityNodeId, viewId, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
            int i = interactionId;
            IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback = callback;
        }

        public void findAccessibilityNodeInfosByText(long accessibilityNodeId, String text, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setFindAccessibilityNodeInfosResult(null, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().findAccessibilityNodeInfosByTextClientThread(accessibilityNodeId, text, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
            int i = interactionId;
            IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback = callback;
        }

        public void findFocus(long accessibilityNodeId, int focusType, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setFindAccessibilityNodeInfoResult(null, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().findFocusClientThread(accessibilityNodeId, focusType, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
            int i = interactionId;
            IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback = callback;
        }

        public void focusSearch(long accessibilityNodeId, int direction, Region interactiveRegion, int interactionId, IAccessibilityInteractionConnectionCallback callback, int flags, int interrogatingPid, long interrogatingTid, MagnificationSpec spec) {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl == null || viewRootImpl.mView == null) {
                try {
                    callback.setFindAccessibilityNodeInfoResult(null, interactionId);
                    return;
                } catch (RemoteException e) {
                    return;
                }
            }
            viewRootImpl.getAccessibilityInteractionController().focusSearchClientThread(accessibilityNodeId, direction, interactiveRegion, interactionId, callback, flags, interrogatingPid, interrogatingTid, spec);
            int i = interactionId;
            IAccessibilityInteractionConnectionCallback iAccessibilityInteractionConnectionCallback = callback;
        }

        public void clearAccessibilityFocus() {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl != null && viewRootImpl.mView != null) {
                viewRootImpl.getAccessibilityInteractionController().clearAccessibilityFocusClientThread();
            }
        }

        public void notifyOutsideTouch() {
            ViewRootImpl viewRootImpl = (ViewRootImpl) this.mViewRootImpl.get();
            if (viewRootImpl != null && viewRootImpl.mView != null) {
                viewRootImpl.getAccessibilityInteractionController().notifyOutsideTouchClientThread();
            }
        }
    }

    final class AccessibilityInteractionConnectionManager implements AccessibilityStateChangeListener {
        AccessibilityInteractionConnectionManager() {
        }

        public void onAccessibilityStateChanged(boolean enabled) {
            if (enabled) {
                ensureConnection();
                if (ViewRootImpl.this.mAttachInfo.mHasWindowFocus && ViewRootImpl.this.mView != null) {
                    ViewRootImpl.this.mView.sendAccessibilityEvent(32);
                    View focusedView = ViewRootImpl.this.mView.findFocus();
                    if (focusedView != null && focusedView != ViewRootImpl.this.mView) {
                        focusedView.sendAccessibilityEvent(8);
                        return;
                    }
                    return;
                }
                return;
            }
            ensureNoConnection();
            ViewRootImpl.this.mHandler.obtainMessage(21).sendToTarget();
        }

        public void ensureConnection() {
            if (!(ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId != -1)) {
                ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId = ViewRootImpl.this.mAccessibilityManager.addAccessibilityInteractionConnection(ViewRootImpl.this.mWindow, ViewRootImpl.this.mContext.getPackageName(), new AccessibilityInteractionConnection(ViewRootImpl.this));
            }
        }

        public void ensureNoConnection() {
            if (ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId != -1) {
                ViewRootImpl.this.mAttachInfo.mAccessibilityWindowId = -1;
                ViewRootImpl.this.mAccessibilityManager.removeAccessibilityInteractionConnection(ViewRootImpl.this.mWindow);
            }
        }
    }

    public interface ActivityConfigCallback {
        void onConfigurationChanged(Configuration configuration, int i);
    }

    abstract class InputStage {
        protected static final int FINISH_HANDLED = 1;
        protected static final int FINISH_NOT_HANDLED = 2;
        protected static final int FORWARD = 0;
        private final InputStage mNext;

        public InputStage(InputStage next) {
            this.mNext = next;
        }

        public final void deliver(QueuedInputEvent q) {
            if ((q.mFlags & 4) != 0) {
                forward(q);
            } else if (shouldDropInputEvent(q)) {
                finish(q, false);
            } else {
                apply(q, onProcess(q));
            }
        }

        /* Access modifiers changed, original: protected */
        public void finish(QueuedInputEvent q, boolean handled) {
            q.mFlags |= 4;
            if (handled) {
                q.mFlags |= 8;
            }
            forward(q);
        }

        /* Access modifiers changed, original: protected */
        public void forward(QueuedInputEvent q) {
            onDeliverToNext(q);
        }

        /* Access modifiers changed, original: protected */
        public void apply(QueuedInputEvent q, int result) {
            if (result == 0) {
                forward(q);
            } else if (result == 1) {
                finish(q, true);
            } else if (result == 2) {
                finish(q, false);
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Invalid result: ");
                stringBuilder.append(result);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        }

        /* Access modifiers changed, original: protected */
        public int onProcess(QueuedInputEvent q) {
            return 0;
        }

        /* Access modifiers changed, original: protected */
        public void onDeliverToNext(QueuedInputEvent q) {
            InputStage inputStage = this.mNext;
            if (inputStage != null) {
                inputStage.deliver(q);
            } else {
                ViewRootImpl.this.finishInputEvent(q);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onWindowFocusChanged(boolean hasWindowFocus) {
            InputStage inputStage = this.mNext;
            if (inputStage != null) {
                inputStage.onWindowFocusChanged(hasWindowFocus);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onDetachedFromWindow() {
            InputStage inputStage = this.mNext;
            if (inputStage != null) {
                inputStage.onDetachedFromWindow();
            }
        }

        /* Access modifiers changed, original: protected */
        public boolean shouldDropInputEvent(QueuedInputEvent q) {
            String access$1700;
            StringBuilder stringBuilder;
            if (ViewRootImpl.this.mView == null || !ViewRootImpl.this.mAdded) {
                access$1700 = ViewRootImpl.this.mTag;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Dropping event due to root view being removed: ");
                stringBuilder.append(q.mEvent);
                Slog.w(access$1700, stringBuilder.toString());
                return true;
            } else if ((ViewRootImpl.this.mAttachInfo.mHasWindowFocus || q.mEvent.isFromSource(2) || ViewRootImpl.this.isAutofillUiShowing()) && !ViewRootImpl.this.mStopped && ((!ViewRootImpl.this.mIsAmbientMode || q.mEvent.isFromSource(1)) && (!ViewRootImpl.this.mPausedForTransition || isBack(q.mEvent)))) {
                return false;
            } else {
                if (ViewRootImpl.isTerminalInputEvent(q.mEvent)) {
                    q.mEvent.cancel();
                    access$1700 = ViewRootImpl.this.mTag;
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Cancelling event due to no window focus: ");
                    stringBuilder2.append(q.mEvent);
                    Slog.w(access$1700, stringBuilder2.toString());
                    return false;
                }
                access$1700 = ViewRootImpl.this.mTag;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Dropping event due to no window focus: ");
                stringBuilder.append(q.mEvent);
                Slog.w(access$1700, stringBuilder.toString());
                return true;
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void dump(String prefix, PrintWriter writer) {
            InputStage inputStage = this.mNext;
            if (inputStage != null) {
                inputStage.dump(prefix, writer);
            }
        }

        private boolean isBack(InputEvent event) {
            boolean z = false;
            if (!(event instanceof KeyEvent)) {
                return false;
            }
            if (((KeyEvent) event).getKeyCode() == 4) {
                z = true;
            }
            return z;
        }
    }

    abstract class AsyncInputStage extends InputStage {
        protected static final int DEFER = 3;
        private QueuedInputEvent mQueueHead;
        private int mQueueLength;
        private QueuedInputEvent mQueueTail;
        private final String mTraceCounter;

        public AsyncInputStage(InputStage next, String traceCounter) {
            super(next);
            this.mTraceCounter = traceCounter;
        }

        /* Access modifiers changed, original: protected */
        public void defer(QueuedInputEvent q) {
            q.mFlags |= 2;
            enqueue(q);
        }

        /* Access modifiers changed, original: protected */
        public void forward(QueuedInputEvent q) {
            q.mFlags &= -3;
            QueuedInputEvent curr = this.mQueueHead;
            if (curr == null) {
                super.forward(q);
                return;
            }
            int deviceId = q.mEvent.getDeviceId();
            QueuedInputEvent prev = null;
            boolean blocked = false;
            while (curr != null && curr != q) {
                if (!blocked && deviceId == curr.mEvent.getDeviceId()) {
                    blocked = true;
                }
                prev = curr;
                curr = curr.mNext;
            }
            if (blocked) {
                if (curr == null) {
                    enqueue(q);
                }
                return;
            }
            if (curr != null) {
                curr = curr.mNext;
                dequeue(q, prev);
            }
            super.forward(q);
            while (curr != null) {
                if (deviceId != curr.mEvent.getDeviceId()) {
                    prev = curr;
                    curr = curr.mNext;
                } else if ((curr.mFlags & 2) != 0) {
                    break;
                } else {
                    QueuedInputEvent next = curr.mNext;
                    dequeue(curr, prev);
                    super.forward(curr);
                    curr = next;
                }
            }
        }

        /* Access modifiers changed, original: protected */
        public void apply(QueuedInputEvent q, int result) {
            if (result == 3) {
                defer(q);
            } else {
                super.apply(q, result);
            }
        }

        private void enqueue(QueuedInputEvent q) {
            QueuedInputEvent queuedInputEvent = this.mQueueTail;
            if (queuedInputEvent == null) {
                this.mQueueHead = q;
                this.mQueueTail = q;
            } else {
                queuedInputEvent.mNext = q;
                this.mQueueTail = q;
            }
            this.mQueueLength++;
            Trace.traceCounter(4, this.mTraceCounter, this.mQueueLength);
        }

        private void dequeue(QueuedInputEvent q, QueuedInputEvent prev) {
            if (prev == null) {
                this.mQueueHead = q.mNext;
            } else {
                prev.mNext = q.mNext;
            }
            if (this.mQueueTail == q) {
                this.mQueueTail = prev;
            }
            q.mNext = null;
            this.mQueueLength--;
            Trace.traceCounter(4, this.mTraceCounter, this.mQueueLength);
        }

        /* Access modifiers changed, original: 0000 */
        public void dump(String prefix, PrintWriter writer) {
            writer.print(prefix);
            writer.print(getClass().getName());
            writer.print(": mQueueLength=");
            writer.println(this.mQueueLength);
            super.dump(prefix, writer);
        }
    }

    public static final class CalledFromWrongThreadException extends AndroidRuntimeException {
        @UnsupportedAppUsage
        public CalledFromWrongThreadException(String msg) {
            super(msg);
        }
    }

    final class ConsumeBatchedInputImmediatelyRunnable implements Runnable {
        ConsumeBatchedInputImmediatelyRunnable() {
        }

        public void run() {
            ViewRootImpl.this.doConsumeBatchedInput(-1);
        }
    }

    final class ConsumeBatchedInputRunnable implements Runnable {
        ConsumeBatchedInputRunnable() {
        }

        public void run() {
            ViewRootImpl viewRootImpl = ViewRootImpl.this;
            viewRootImpl.doConsumeBatchedInput(viewRootImpl.mChoreographer.getFrameTimeNanos());
        }
    }

    final class EarlyPostImeInputStage extends InputStage {
        public EarlyPostImeInputStage(InputStage next) {
            super(next);
        }

        /* Access modifiers changed, original: protected */
        public int onProcess(QueuedInputEvent q) {
            if (q.mEvent instanceof KeyEvent) {
                return processKeyEvent(q);
            }
            if (q.mEvent instanceof MotionEvent) {
                return processMotionEvent(q);
            }
            return 0;
        }

        private int processKeyEvent(QueuedInputEvent q) {
            KeyEvent event = q.mEvent;
            if (ViewRootImpl.this.mAttachInfo.mTooltipHost != null) {
                ViewRootImpl.this.mAttachInfo.mTooltipHost.handleTooltipKey(event);
            }
            if (ViewRootImpl.this.checkForLeavingTouchModeAndConsume(event)) {
                return 1;
            }
            ViewRootImpl.this.mFallbackEventHandler.preDispatchKeyEvent(event);
            return 0;
        }

        private int processMotionEvent(QueuedInputEvent q) {
            MotionEvent event = q.mEvent;
            if (event.isFromSource(2)) {
                return processPointerEvent(q);
            }
            int action = event.getActionMasked();
            if ((action == 0 || action == 8) && event.isFromSource(8)) {
                ViewRootImpl.this.ensureTouchMode(false);
            }
            return 0;
        }

        private int processPointerEvent(QueuedInputEvent q) {
            MotionEvent event = q.mEvent;
            if (ViewRootImpl.this.mTranslator != null) {
                ViewRootImpl.this.mTranslator.translateEventInScreenToAppWindow(event);
            }
            int action = event.getAction();
            if (action == 0 || action == 8) {
                ViewRootImpl.this.ensureTouchMode(event.isFromSource(4098));
            }
            if (action == 0) {
                AutofillManager afm = ViewRootImpl.this.getAutofillManager();
                if (afm != null) {
                    afm.requestHideFillUi();
                }
            }
            if (action == 0 && ViewRootImpl.this.mAttachInfo.mTooltipHost != null) {
                ViewRootImpl.this.mAttachInfo.mTooltipHost.hideTooltip();
            }
            if (ViewRootImpl.this.mCurScrollY != 0) {
                event.offsetLocation(0.0f, (float) ViewRootImpl.this.mCurScrollY);
            }
            if (event.isTouchEvent()) {
                ViewRootImpl.this.mLastTouchPoint.x = event.getRawX();
                ViewRootImpl.this.mLastTouchPoint.y = event.getRawY();
                ViewRootImpl.this.mLastTouchSource = event.getSource();
            }
            return 0;
        }
    }

    final class HighContrastTextManager implements HighTextContrastChangeListener {
        HighContrastTextManager() {
            HardwareRenderer.setHighContrastText(ViewRootImpl.this.mAccessibilityManager.isHighTextContrastEnabled());
        }

        public void onHighTextContrastStateChanged(boolean enabled) {
            HardwareRenderer.setHighContrastText(enabled);
            ViewRootImpl.this.destroyHardwareResources();
            ViewRootImpl.this.invalidate();
        }
    }

    final class ImeInputStage extends AsyncInputStage implements FinishedInputEventCallback {
        public ImeInputStage(InputStage next, String traceCounter) {
            super(next, traceCounter);
        }

        /* Access modifiers changed, original: protected */
        public int onProcess(QueuedInputEvent q) {
            if (ViewRootImpl.this.mLastWasImTarget && !ViewRootImpl.this.isInLocalFocusMode()) {
                InputMethodManager imm = (InputMethodManager) ViewRootImpl.this.mContext.getSystemService(InputMethodManager.class);
                if (imm != null) {
                    int result = imm.dispatchInputEvent(q.mEvent, q, this, ViewRootImpl.this.mHandler);
                    if (result == 1) {
                        return 1;
                    }
                    if (result == 0) {
                        return 0;
                    }
                    return 3;
                }
            }
            return 0;
        }

        public void onFinishedInputEvent(Object token, boolean handled) {
            QueuedInputEvent q = (QueuedInputEvent) token;
            if (handled) {
                finish(q, true);
            } else {
                forward(q);
            }
        }
    }

    final class InvalidateOnAnimationRunnable implements Runnable {
        private boolean mPosted;
        private InvalidateInfo[] mTempViewRects;
        private View[] mTempViews;
        private final ArrayList<InvalidateInfo> mViewRects = new ArrayList();
        private final ArrayList<View> mViews = new ArrayList();

        InvalidateOnAnimationRunnable() {
        }

        public void addView(View view) {
            synchronized (this) {
                this.mViews.add(view);
                postIfNeededLocked();
            }
        }

        public void addViewRect(InvalidateInfo info) {
            synchronized (this) {
                this.mViewRects.add(info);
                postIfNeededLocked();
            }
        }

        public void removeView(View view) {
            synchronized (this) {
                this.mViews.remove(view);
                int i = this.mViewRects.size();
                while (true) {
                    int i2 = i - 1;
                    if (i <= 0) {
                        break;
                    }
                    InvalidateInfo info = (InvalidateInfo) this.mViewRects.get(i2);
                    if (info.target == view) {
                        this.mViewRects.remove(i2);
                        info.recycle();
                    }
                    i = i2;
                }
                if (this.mPosted && this.mViews.isEmpty() && this.mViewRects.isEmpty()) {
                    ViewRootImpl.this.mChoreographer.removeCallbacks(1, this, null);
                    this.mPosted = false;
                }
            }
        }

        public void run() {
            int viewCount;
            int viewRectCount;
            int i;
            synchronized (this) {
                this.mPosted = false;
                viewCount = this.mViews.size();
                if (viewCount != 0) {
                    this.mTempViews = (View[]) this.mViews.toArray(this.mTempViews != null ? this.mTempViews : new View[viewCount]);
                    this.mViews.clear();
                }
                viewRectCount = this.mViewRects.size();
                if (viewRectCount != 0) {
                    this.mTempViewRects = (InvalidateInfo[]) this.mViewRects.toArray(this.mTempViewRects != null ? this.mTempViewRects : new InvalidateInfo[viewRectCount]);
                    this.mViewRects.clear();
                }
            }
            for (i = 0; i < viewCount; i++) {
                this.mTempViews[i].invalidate();
                this.mTempViews[i] = null;
            }
            for (i = 0; i < viewRectCount; i++) {
                InvalidateInfo info = this.mTempViewRects[i];
                info.target.invalidate(info.left, info.top, info.right, info.bottom);
                info.recycle();
            }
        }

        private void postIfNeededLocked() {
            if (!this.mPosted) {
                ViewRootImpl.this.mChoreographer.postCallback(1, this, null);
                this.mPosted = true;
            }
        }
    }

    final class NativePostImeInputStage extends AsyncInputStage implements InputQueue.FinishedInputEventCallback {
        public NativePostImeInputStage(InputStage next, String traceCounter) {
            super(next, traceCounter);
        }

        /* Access modifiers changed, original: protected */
        public int onProcess(QueuedInputEvent q) {
            if (ViewRootImpl.this.mInputQueue == null) {
                return 0;
            }
            ViewRootImpl.this.mInputQueue.sendInputEvent(q.mEvent, q, false, this);
            return 3;
        }

        public void onFinishedInputEvent(Object token, boolean handled) {
            QueuedInputEvent q = (QueuedInputEvent) token;
            if (handled) {
                finish(q, true);
            } else {
                forward(q);
            }
        }
    }

    final class NativePreImeInputStage extends AsyncInputStage implements InputQueue.FinishedInputEventCallback {
        public NativePreImeInputStage(InputStage next, String traceCounter) {
            super(next, traceCounter);
        }

        /* Access modifiers changed, original: protected */
        public int onProcess(QueuedInputEvent q) {
            if (ViewRootImpl.this.mInputQueue == null || !(q.mEvent instanceof KeyEvent)) {
                return 0;
            }
            ViewRootImpl.this.mInputQueue.sendInputEvent(q.mEvent, q, true, this);
            return 3;
        }

        public void onFinishedInputEvent(Object token, boolean handled) {
            QueuedInputEvent q = (QueuedInputEvent) token;
            if (handled) {
                finish(q, true);
            } else {
                forward(q);
            }
        }
    }

    private static final class QueuedInputEvent {
        public static final int FLAG_DEFERRED = 2;
        public static final int FLAG_DELIVER_POST_IME = 1;
        public static final int FLAG_FINISHED = 4;
        public static final int FLAG_FINISHED_HANDLED = 8;
        public static final int FLAG_MODIFIED_FOR_COMPATIBILITY = 64;
        public static final int FLAG_RESYNTHESIZED = 16;
        public static final int FLAG_UNHANDLED = 32;
        public InputEvent mEvent;
        public int mFlags;
        public QueuedInputEvent mNext;
        public InputEventReceiver mReceiver;

        private QueuedInputEvent() {
        }

        /* synthetic */ QueuedInputEvent(AnonymousClass1 x0) {
            this();
        }

        public boolean shouldSkipIme() {
            boolean z = true;
            if ((this.mFlags & 1) != 0) {
                return true;
            }
            InputEvent inputEvent = this.mEvent;
            if (!((inputEvent instanceof MotionEvent) && (inputEvent.isFromSource(2) || this.mEvent.isFromSource(4194304)))) {
                z = false;
            }
            return z;
        }

        public boolean shouldSendToSynthesizer() {
            if ((this.mFlags & 32) != 0) {
                return true;
            }
            return false;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder("QueuedInputEvent{flags=");
            if (!flagToString("UNHANDLED", 32, flagToString("RESYNTHESIZED", 16, flagToString("FINISHED_HANDLED", 8, flagToString("FINISHED", 4, flagToString("DEFERRED", 2, flagToString("DELIVER_POST_IME", 1, false, sb), sb), sb), sb), sb), sb)) {
                sb.append("0");
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(", hasNextQueuedEvent=");
            String str = "true";
            String str2 = "false";
            stringBuilder.append(this.mEvent != null ? str : str2);
            sb.append(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(", hasInputEventReceiver=");
            if (this.mReceiver == null) {
                str = str2;
            }
            stringBuilder.append(str);
            sb.append(stringBuilder.toString());
            stringBuilder = new StringBuilder();
            stringBuilder.append(", mEvent=");
            stringBuilder.append(this.mEvent);
            stringBuilder.append("}");
            sb.append(stringBuilder.toString());
            return sb.toString();
        }

        private boolean flagToString(String name, int flag, boolean hasPrevious, StringBuilder sb) {
            if ((this.mFlags & flag) == 0) {
                return hasPrevious;
            }
            if (hasPrevious) {
                sb.append("|");
            }
            sb.append(name);
            return true;
        }
    }

    private class SendWindowContentChangedAccessibilityEvent implements Runnable {
        private int mChangeTypes;
        public long mLastEventTimeMillis;
        public StackTraceElement[] mOrigin;
        public View mSource;

        private SendWindowContentChangedAccessibilityEvent() {
            this.mChangeTypes = 0;
        }

        /* synthetic */ SendWindowContentChangedAccessibilityEvent(ViewRootImpl x0, AnonymousClass1 x1) {
            this();
        }

        public void run() {
            View source = this.mSource;
            this.mSource = null;
            if (source == null) {
                Log.e(ViewRootImpl.TAG, "Accessibility content change has no source");
                return;
            }
            if (AccessibilityManager.getInstance(ViewRootImpl.this.mContext).isEnabled()) {
                this.mLastEventTimeMillis = SystemClock.uptimeMillis();
                AccessibilityEvent event = AccessibilityEvent.obtain();
                event.setEventType(2048);
                event.setContentChangeTypes(this.mChangeTypes);
                source.sendAccessibilityEventUnchecked(event);
            } else {
                this.mLastEventTimeMillis = 0;
            }
            source.resetSubtreeAccessibilityStateChanged();
            this.mChangeTypes = 0;
        }

        public void runOrPost(View source, int changeType) {
            if (ViewRootImpl.this.mHandler.getLooper() != Looper.myLooper()) {
                Log.e(ViewRootImpl.TAG, "Accessibility content change on non-UI thread. Future Android versions will throw an exception.", new CalledFromWrongThreadException("Only the original thread that created a view hierarchy can touch its views."));
                ViewRootImpl.this.mHandler.removeCallbacks(this);
                if (this.mSource != null) {
                    run();
                }
            }
            View predecessor = this.mSource;
            if (predecessor != null) {
                predecessor = ViewRootImpl.this.getCommonPredecessor(predecessor, source);
                if (predecessor != null) {
                    predecessor = predecessor.getSelfOrParentImportantForA11y();
                }
                this.mSource = predecessor != null ? predecessor : source;
                this.mChangeTypes |= changeType;
                return;
            }
            this.mSource = source;
            this.mChangeTypes = changeType;
            long timeSinceLastMillis = SystemClock.uptimeMillis() - this.mLastEventTimeMillis;
            long minEventIntevalMillis = ViewConfiguration.getSendRecurringAccessibilityEventsInterval();
            if (timeSinceLastMillis >= minEventIntevalMillis) {
                removeCallbacksAndRun();
            } else {
                ViewRootImpl.this.mHandler.postDelayed(this, minEventIntevalMillis - timeSinceLastMillis);
            }
        }

        public void removeCallbacksAndRun() {
            ViewRootImpl.this.mHandler.removeCallbacks(this);
            run();
        }
    }

    final class SyntheticInputStage extends InputStage {
        private final SyntheticJoystickHandler mJoystick = new SyntheticJoystickHandler();
        private final SyntheticKeyboardHandler mKeyboard = new SyntheticKeyboardHandler();
        private final SyntheticTouchNavigationHandler mTouchNavigation = new SyntheticTouchNavigationHandler();
        private final SyntheticTrackballHandler mTrackball = new SyntheticTrackballHandler();

        public SyntheticInputStage() {
            super(null);
        }

        /* Access modifiers changed, original: protected */
        public int onProcess(QueuedInputEvent q) {
            q.mFlags |= 16;
            if (q.mEvent instanceof MotionEvent) {
                MotionEvent event = q.mEvent;
                int source = event.getSource();
                if ((source & 4) != 0) {
                    this.mTrackball.process(event);
                    return 1;
                } else if ((source & 16) != 0) {
                    this.mJoystick.process(event);
                    return 1;
                } else if ((source & 2097152) == 2097152) {
                    this.mTouchNavigation.process(event);
                    return 1;
                }
            } else if ((q.mFlags & 32) != 0) {
                this.mKeyboard.process((KeyEvent) q.mEvent);
                return 1;
            }
            return 0;
        }

        /* Access modifiers changed, original: protected */
        public void onDeliverToNext(QueuedInputEvent q) {
            if ((q.mFlags & 16) == 0 && (q.mEvent instanceof MotionEvent)) {
                MotionEvent event = q.mEvent;
                int source = event.getSource();
                if ((source & 4) != 0) {
                    this.mTrackball.cancel();
                } else if ((source & 16) != 0) {
                    this.mJoystick.cancel();
                } else if ((source & 2097152) == 2097152) {
                    this.mTouchNavigation.cancel(event);
                }
            }
            super.onDeliverToNext(q);
        }

        /* Access modifiers changed, original: protected */
        public void onWindowFocusChanged(boolean hasWindowFocus) {
            if (!hasWindowFocus) {
                this.mJoystick.cancel();
            }
        }

        /* Access modifiers changed, original: protected */
        public void onDetachedFromWindow() {
            this.mJoystick.cancel();
        }
    }

    final class SyntheticJoystickHandler extends Handler {
        private static final int MSG_ENQUEUE_X_AXIS_KEY_REPEAT = 1;
        private static final int MSG_ENQUEUE_Y_AXIS_KEY_REPEAT = 2;
        private final SparseArray<KeyEvent> mDeviceKeyEvents = new SparseArray();
        private final JoystickAxesState mJoystickAxesState = new JoystickAxesState();

        final class JoystickAxesState {
            private static final int STATE_DOWN_OR_RIGHT = 1;
            private static final int STATE_NEUTRAL = 0;
            private static final int STATE_UP_OR_LEFT = -1;
            final int[] mAxisStatesHat = new int[]{0, 0};
            final int[] mAxisStatesStick = new int[]{0, 0};

            JoystickAxesState() {
            }

            /* Access modifiers changed, original: 0000 */
            public void resetState() {
                int[] iArr = this.mAxisStatesHat;
                iArr[0] = 0;
                iArr[1] = 0;
                iArr = this.mAxisStatesStick;
                iArr[0] = 0;
                iArr[1] = 0;
            }

            /* Access modifiers changed, original: 0000 */
            public void updateStateForAxis(MotionEvent event, long time, int axis, float value) {
                int axisStateIndex;
                int repeatMessage;
                int currentState;
                int i = axis;
                if (isXAxis(i)) {
                    axisStateIndex = 0;
                    repeatMessage = 1;
                } else if (isYAxis(i)) {
                    axisStateIndex = 1;
                    repeatMessage = 2;
                } else {
                    float f = value;
                    String access$1700 = ViewRootImpl.this.mTag;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unexpected axis ");
                    stringBuilder.append(i);
                    stringBuilder.append(" in updateStateForAxis!");
                    Log.e(access$1700, stringBuilder.toString());
                    return;
                }
                int newState = joystickAxisValueToState(value);
                if (i == 0 || i == 1) {
                    currentState = this.mAxisStatesStick[axisStateIndex];
                } else {
                    currentState = this.mAxisStatesHat[axisStateIndex];
                }
                if (currentState != newState) {
                    int deviceId;
                    int metaState = event.getMetaState();
                    int deviceId2 = event.getDeviceId();
                    int source = event.getSource();
                    if (currentState == 1 || currentState == -1) {
                        int keyCode = joystickAxisAndStateToKeycode(i, currentState);
                        if (keyCode != 0) {
                            InputEvent inputEvent = r8;
                            ViewRootImpl viewRootImpl = ViewRootImpl.this;
                            deviceId = deviceId2;
                            InputEvent keyEvent = new KeyEvent(time, time, 1, keyCode, 0, metaState, deviceId, 0, 1024, source);
                            viewRootImpl.enqueueInputEvent(inputEvent);
                            deviceId2 = deviceId;
                            SyntheticJoystickHandler.this.mDeviceKeyEvents.put(deviceId2, null);
                        }
                        SyntheticJoystickHandler.this.removeMessages(repeatMessage);
                    }
                    if (newState == 1 || newState == -1) {
                        int keyCode2 = joystickAxisAndStateToKeycode(i, newState);
                        if (keyCode2 != 0) {
                            deviceId = deviceId2;
                            int i2 = source;
                            deviceId2 = new KeyEvent(time, time, 0, keyCode2, 0, metaState, deviceId, 0, 1024, i2);
                            ViewRootImpl.this.enqueueInputEvent(deviceId2);
                            Message m = SyntheticJoystickHandler.this.obtainMessage(repeatMessage, deviceId2);
                            m.setAsynchronous(true);
                            SyntheticJoystickHandler.this.sendMessageDelayed(m, (long) ViewConfiguration.getKeyRepeatTimeout());
                            KeyEvent keyEvent2 = r8;
                            SparseArray access$2700 = SyntheticJoystickHandler.this.mDeviceKeyEvents;
                            KeyEvent keyEvent3 = deviceId2;
                            KeyEvent keyEvent4 = new KeyEvent(time, time, 1, keyCode2, 0, metaState, deviceId, 0, 1056, i2);
                            access$2700.put(deviceId, keyEvent2);
                        }
                    } else {
                        int i3 = deviceId2;
                    }
                    if (i == 0 || i == 1) {
                        this.mAxisStatesStick[axisStateIndex] = newState;
                    } else {
                        this.mAxisStatesHat[axisStateIndex] = newState;
                    }
                }
            }

            private boolean isXAxis(int axis) {
                return axis == 0 || axis == 15;
            }

            private boolean isYAxis(int axis) {
                return axis == 1 || axis == 16;
            }

            private int joystickAxisAndStateToKeycode(int axis, int state) {
                if (isXAxis(axis) && state == -1) {
                    return 21;
                }
                if (isXAxis(axis) && state == 1) {
                    return 22;
                }
                if (isYAxis(axis) && state == -1) {
                    return 19;
                }
                if (isYAxis(axis) && state == 1) {
                    return 20;
                }
                String access$1700 = ViewRootImpl.this.mTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unknown axis ");
                stringBuilder.append(axis);
                stringBuilder.append(" or direction ");
                stringBuilder.append(state);
                Log.e(access$1700, stringBuilder.toString());
                return 0;
            }

            private int joystickAxisValueToState(float value) {
                if (value >= 0.5f) {
                    return 1;
                }
                if (value <= -0.5f) {
                    return -1;
                }
                return 0;
            }
        }

        public SyntheticJoystickHandler() {
            super(true);
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            if ((i == 1 || i == 2) && ViewRootImpl.this.mAttachInfo.mHasWindowFocus) {
                KeyEvent oldEvent = msg.obj;
                KeyEvent e = KeyEvent.changeTimeRepeat(oldEvent, SystemClock.uptimeMillis(), oldEvent.getRepeatCount() + 1);
                ViewRootImpl.this.enqueueInputEvent(e);
                Message m = obtainMessage(msg.what, e);
                m.setAsynchronous(true);
                sendMessageDelayed(m, (long) ViewConfiguration.getKeyRepeatDelay());
            }
        }

        public void process(MotionEvent event) {
            int actionMasked = event.getActionMasked();
            if (actionMasked == 2) {
                update(event);
            } else if (actionMasked != 3) {
                String access$1700 = ViewRootImpl.this.mTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected action: ");
                stringBuilder.append(event.getActionMasked());
                Log.w(access$1700, stringBuilder.toString());
            } else {
                cancel();
            }
        }

        private void cancel() {
            removeMessages(1);
            removeMessages(2);
            for (int i = 0; i < this.mDeviceKeyEvents.size(); i++) {
                KeyEvent keyEvent = (KeyEvent) this.mDeviceKeyEvents.valueAt(i);
                if (keyEvent != null) {
                    ViewRootImpl.this.enqueueInputEvent(KeyEvent.changeTimeRepeat(keyEvent, SystemClock.uptimeMillis(), 0));
                }
            }
            this.mDeviceKeyEvents.clear();
            this.mJoystickAxesState.resetState();
        }

        private void update(MotionEvent event) {
            int historySize = event.getHistorySize();
            for (int h = 0; h < historySize; h++) {
                MotionEvent motionEvent = event;
                long historicalEventTime = event.getHistoricalEventTime(h);
                this.mJoystickAxesState.updateStateForAxis(motionEvent, historicalEventTime, 0, event.getHistoricalAxisValue(0, 0, h));
                this.mJoystickAxesState.updateStateForAxis(motionEvent, historicalEventTime, 1, event.getHistoricalAxisValue(1, 0, h));
                this.mJoystickAxesState.updateStateForAxis(motionEvent, historicalEventTime, 15, event.getHistoricalAxisValue(15, 0, h));
                this.mJoystickAxesState.updateStateForAxis(motionEvent, historicalEventTime, 16, event.getHistoricalAxisValue(16, 0, h));
            }
            long time = event.getEventTime();
            this.mJoystickAxesState.updateStateForAxis(event, time, 0, event.getAxisValue(0));
            this.mJoystickAxesState.updateStateForAxis(event, time, 1, event.getAxisValue(1));
            this.mJoystickAxesState.updateStateForAxis(event, time, 15, event.getAxisValue(15));
            this.mJoystickAxesState.updateStateForAxis(event, time, 16, event.getAxisValue(16));
        }
    }

    final class SyntheticKeyboardHandler {
        SyntheticKeyboardHandler() {
        }

        public void process(KeyEvent event) {
            if ((event.getFlags() & 1024) == 0) {
                FallbackAction fallbackAction = event.getKeyCharacterMap().getFallbackAction(event.getKeyCode(), event.getMetaState());
                if (fallbackAction != null) {
                    KeyEvent fallbackEvent = KeyEvent.obtain(event.getDownTime(), event.getEventTime(), event.getAction(), fallbackAction.keyCode, event.getRepeatCount(), fallbackAction.metaState, event.getDeviceId(), event.getScanCode(), event.getFlags() | 1024, event.getSource(), null);
                    fallbackAction.recycle();
                    ViewRootImpl.this.enqueueInputEvent(fallbackEvent);
                }
            }
        }
    }

    final class SyntheticTouchNavigationHandler extends Handler {
        private static final float DEFAULT_HEIGHT_MILLIMETERS = 48.0f;
        private static final float DEFAULT_WIDTH_MILLIMETERS = 48.0f;
        private static final float FLING_TICK_DECAY = 0.8f;
        private static final boolean LOCAL_DEBUG = false;
        private static final String LOCAL_TAG = "SyntheticTouchNavigationHandler";
        private static final float MAX_FLING_VELOCITY_TICKS_PER_SECOND = 20.0f;
        private static final float MIN_FLING_VELOCITY_TICKS_PER_SECOND = 6.0f;
        private static final int TICK_DISTANCE_MILLIMETERS = 12;
        private float mAccumulatedX;
        private float mAccumulatedY;
        private int mActivePointerId = -1;
        private float mConfigMaxFlingVelocity;
        private float mConfigMinFlingVelocity;
        private float mConfigTickDistance;
        private boolean mConsumedMovement;
        private int mCurrentDeviceId = -1;
        private boolean mCurrentDeviceSupported;
        private int mCurrentSource;
        private final Runnable mFlingRunnable = new Runnable() {
            public void run() {
                long time = SystemClock.uptimeMillis();
                SyntheticTouchNavigationHandler syntheticTouchNavigationHandler = SyntheticTouchNavigationHandler.this;
                syntheticTouchNavigationHandler.sendKeyDownOrRepeat(time, syntheticTouchNavigationHandler.mPendingKeyCode, SyntheticTouchNavigationHandler.this.mPendingKeyMetaState);
                SyntheticTouchNavigationHandler.access$3132(SyntheticTouchNavigationHandler.this, SyntheticTouchNavigationHandler.FLING_TICK_DECAY);
                if (!SyntheticTouchNavigationHandler.this.postFling(time)) {
                    SyntheticTouchNavigationHandler.this.mFlinging = false;
                    SyntheticTouchNavigationHandler.this.finishKeys(time);
                }
            }
        };
        private float mFlingVelocity;
        private boolean mFlinging;
        private float mLastX;
        private float mLastY;
        private int mPendingKeyCode = 0;
        private long mPendingKeyDownTime;
        private int mPendingKeyMetaState;
        private int mPendingKeyRepeatCount;
        private float mStartX;
        private float mStartY;
        private VelocityTracker mVelocityTracker;

        static /* synthetic */ float access$3132(SyntheticTouchNavigationHandler x0, float x1) {
            float f = x0.mFlingVelocity * x1;
            x0.mFlingVelocity = f;
            return f;
        }

        public SyntheticTouchNavigationHandler() {
            super(true);
        }

        public void process(MotionEvent event) {
            float f;
            MotionEvent motionEvent = event;
            long time = event.getEventTime();
            int deviceId = event.getDeviceId();
            int source = event.getSource();
            if (!(this.mCurrentDeviceId == deviceId && this.mCurrentSource == source)) {
                finishKeys(time);
                finishTracking(time);
                this.mCurrentDeviceId = deviceId;
                this.mCurrentSource = source;
                this.mCurrentDeviceSupported = false;
                InputDevice device = event.getDevice();
                if (device != null) {
                    MotionRange xRange = device.getMotionRange(0);
                    MotionRange yRange = device.getMotionRange(1);
                    if (!(xRange == null || yRange == null)) {
                        this.mCurrentDeviceSupported = true;
                        float xRes = xRange.getResolution();
                        if (xRes <= 0.0f) {
                            xRes = xRange.getRange() / 48.0f;
                        }
                        float yRes = yRange.getResolution();
                        if (yRes <= 0.0f) {
                            yRes = yRange.getRange() / 48.0f;
                        }
                        this.mConfigTickDistance = 12.0f * ((xRes + yRes) * 0.5f);
                        f = this.mConfigTickDistance;
                        this.mConfigMinFlingVelocity = MIN_FLING_VELOCITY_TICKS_PER_SECOND * f;
                        this.mConfigMaxFlingVelocity = f * MAX_FLING_VELOCITY_TICKS_PER_SECOND;
                    }
                }
            }
            if (this.mCurrentDeviceSupported) {
                int action = event.getActionMasked();
                if (action == 0) {
                    boolean caughtFling = this.mFlinging;
                    finishKeys(time);
                    finishTracking(time);
                    this.mActivePointerId = motionEvent.getPointerId(0);
                    this.mVelocityTracker = VelocityTracker.obtain();
                    this.mVelocityTracker.addMovement(motionEvent);
                    this.mStartX = event.getX();
                    this.mStartY = event.getY();
                    this.mLastX = this.mStartX;
                    this.mLastY = this.mStartY;
                    this.mAccumulatedX = 0.0f;
                    this.mAccumulatedY = 0.0f;
                    this.mConsumedMovement = caughtFling;
                } else if (action == 1 || action == 2) {
                    int index = this.mActivePointerId;
                    if (index >= 0) {
                        index = motionEvent.findPointerIndex(index);
                        if (index < 0) {
                            finishKeys(time);
                            finishTracking(time);
                        } else {
                            this.mVelocityTracker.addMovement(motionEvent);
                            f = motionEvent.getX(index);
                            float y = motionEvent.getY(index);
                            this.mAccumulatedX += f - this.mLastX;
                            this.mAccumulatedY += y - this.mLastY;
                            this.mLastX = f;
                            this.mLastY = y;
                            consumeAccumulatedMovement(time, event.getMetaState());
                            if (action == 1) {
                                if (this.mConsumedMovement && this.mPendingKeyCode != 0) {
                                    this.mVelocityTracker.computeCurrentVelocity(1000, this.mConfigMaxFlingVelocity);
                                    if (!startFling(time, this.mVelocityTracker.getXVelocity(this.mActivePointerId), this.mVelocityTracker.getYVelocity(this.mActivePointerId))) {
                                        finishKeys(time);
                                    }
                                }
                                finishTracking(time);
                            }
                        }
                    }
                } else if (action == 3) {
                    finishKeys(time);
                    finishTracking(time);
                }
            }
        }

        public void cancel(MotionEvent event) {
            if (this.mCurrentDeviceId == event.getDeviceId() && this.mCurrentSource == event.getSource()) {
                long time = event.getEventTime();
                finishKeys(time);
                finishTracking(time);
            }
        }

        private void finishKeys(long time) {
            cancelFling();
            sendKeyUp(time);
        }

        private void finishTracking(long time) {
            if (this.mActivePointerId >= 0) {
                this.mActivePointerId = -1;
                this.mVelocityTracker.recycle();
                this.mVelocityTracker = null;
            }
        }

        private void consumeAccumulatedMovement(long time, int metaState) {
            float absX = Math.abs(this.mAccumulatedX);
            float absY = Math.abs(this.mAccumulatedY);
            if (absX >= absY) {
                if (absX >= this.mConfigTickDistance) {
                    this.mAccumulatedX = consumeAccumulatedMovement(time, metaState, this.mAccumulatedX, 21, 22);
                    this.mAccumulatedY = 0.0f;
                    this.mConsumedMovement = true;
                }
            } else if (absY >= this.mConfigTickDistance) {
                this.mAccumulatedY = consumeAccumulatedMovement(time, metaState, this.mAccumulatedY, 19, 20);
                this.mAccumulatedX = 0.0f;
                this.mConsumedMovement = true;
            }
        }

        private float consumeAccumulatedMovement(long time, int metaState, float accumulator, int negativeKeyCode, int positiveKeyCode) {
            while (accumulator <= (-this.mConfigTickDistance)) {
                sendKeyDownOrRepeat(time, negativeKeyCode, metaState);
                accumulator += this.mConfigTickDistance;
            }
            while (accumulator >= this.mConfigTickDistance) {
                sendKeyDownOrRepeat(time, positiveKeyCode, metaState);
                accumulator -= this.mConfigTickDistance;
            }
            return accumulator;
        }

        private void sendKeyDownOrRepeat(long time, int keyCode, int metaState) {
            int i = keyCode;
            if (this.mPendingKeyCode != i) {
                sendKeyUp(time);
                this.mPendingKeyDownTime = time;
                this.mPendingKeyCode = i;
                this.mPendingKeyRepeatCount = 0;
            } else {
                long j = time;
                this.mPendingKeyRepeatCount++;
            }
            this.mPendingKeyMetaState = metaState;
            ViewRootImpl viewRootImpl = ViewRootImpl.this;
            InputEvent inputEvent = r3;
            ViewRootImpl viewRootImpl2 = viewRootImpl;
            InputEvent keyEvent = new KeyEvent(this.mPendingKeyDownTime, time, 0, this.mPendingKeyCode, this.mPendingKeyRepeatCount, this.mPendingKeyMetaState, this.mCurrentDeviceId, 1024, this.mCurrentSource);
            viewRootImpl2.enqueueInputEvent(inputEvent);
        }

        private void sendKeyUp(long time) {
            int i = this.mPendingKeyCode;
            if (i != 0) {
                ViewRootImpl.this.enqueueInputEvent(new KeyEvent(this.mPendingKeyDownTime, time, 1, i, 0, this.mPendingKeyMetaState, this.mCurrentDeviceId, 0, 1024, this.mCurrentSource));
                this.mPendingKeyCode = 0;
            }
        }

        private boolean startFling(long time, float vx, float vy) {
            switch (this.mPendingKeyCode) {
                case 19:
                    if ((-vy) >= this.mConfigMinFlingVelocity && Math.abs(vx) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = -vy;
                        break;
                    }
                    return false;
                case 20:
                    if (vy >= this.mConfigMinFlingVelocity && Math.abs(vx) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = vy;
                        break;
                    }
                    return false;
                case 21:
                    if ((-vx) >= this.mConfigMinFlingVelocity && Math.abs(vy) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = -vx;
                        break;
                    }
                    return false;
                    break;
                case 22:
                    if (vx >= this.mConfigMinFlingVelocity && Math.abs(vy) < this.mConfigMinFlingVelocity) {
                        this.mFlingVelocity = vx;
                        break;
                    }
                    return false;
            }
            this.mFlinging = postFling(time);
            return this.mFlinging;
        }

        private boolean postFling(long time) {
            float f = this.mFlingVelocity;
            if (f < this.mConfigMinFlingVelocity) {
                return false;
            }
            postAtTime(this.mFlingRunnable, time + ((long) ((this.mConfigTickDistance / f) * 1000.0f)));
            return true;
        }

        private void cancelFling() {
            if (this.mFlinging) {
                removeCallbacks(this.mFlingRunnable);
                this.mFlinging = false;
            }
        }
    }

    final class SyntheticTrackballHandler {
        private long mLastTime;
        private final TrackballAxis mX = new TrackballAxis();
        private final TrackballAxis mY = new TrackballAxis();

        SyntheticTrackballHandler() {
        }

        public void process(MotionEvent event) {
            InputEvent keyEvent;
            long curTime;
            int i;
            float accel;
            long curTime2 = SystemClock.uptimeMillis();
            if (this.mLastTime + 250 < curTime2) {
                this.mX.reset(0);
                this.mY.reset(0);
                this.mLastTime = curTime2;
            }
            int action = event.getAction();
            int metaState = event.getMetaState();
            if (action == 0) {
                this.mX.reset(2);
                this.mY.reset(2);
                InputEvent inputEvent = keyEvent;
                ViewRootImpl viewRootImpl = ViewRootImpl.this;
                curTime = curTime2;
                i = 2;
                keyEvent = new KeyEvent(curTime2, curTime2, 0, 23, 0, metaState, -1, 0, 1024, 257);
                viewRootImpl.enqueueInputEvent(inputEvent);
            } else if (action != 1) {
                int i2 = action;
                curTime = curTime2;
                i = 2;
            } else {
                this.mX.reset(2);
                this.mY.reset(2);
                InputEvent inputEvent2 = keyEvent;
                ViewRootImpl viewRootImpl2 = ViewRootImpl.this;
                keyEvent = new KeyEvent(curTime2, curTime2, 1, 23, 0, metaState, -1, 0, 1024, 257);
                viewRootImpl2.enqueueInputEvent(inputEvent2);
                curTime = curTime2;
                i = 2;
            }
            float xOff = this.mX.collect(event.getX(), event.getEventTime(), "X");
            float yOff = this.mY.collect(event.getY(), event.getEventTime(), "Y");
            int movement = 0;
            int i3;
            int keycode;
            float accel2;
            if (xOff > yOff) {
                movement = this.mX.generate();
                if (movement != 0) {
                    if (movement > 0) {
                        i3 = 22;
                    } else {
                        i3 = 21;
                    }
                    keycode = i3;
                    accel2 = this.mX.acceleration;
                    this.mY.reset(i);
                    i = keycode;
                    accel = accel2;
                } else {
                    i = 0;
                    accel = 1.0f;
                }
            } else if (yOff > 0.0f) {
                movement = this.mY.generate();
                if (movement != 0) {
                    if (movement > 0) {
                        i3 = 20;
                    } else {
                        i3 = 19;
                    }
                    keycode = i3;
                    accel2 = this.mY.acceleration;
                    this.mX.reset(i);
                    i = keycode;
                    accel = accel2;
                } else {
                    i = 0;
                    accel = 1.0f;
                }
            } else {
                i = 0;
                accel = 1.0f;
            }
            if (i != 0) {
                long curTime3;
                if (movement < 0) {
                    movement = -movement;
                }
                action = (int) (((float) movement) * accel);
                if (action > movement) {
                    int movement2 = movement - 1;
                    int repeatCount = action - movement2;
                    InputEvent inputEvent3 = keyEvent;
                    ViewRootImpl viewRootImpl3 = ViewRootImpl.this;
                    keyEvent = new KeyEvent(curTime, curTime, 2, i, repeatCount, metaState, -1, 0, 1024, 257);
                    viewRootImpl3.enqueueInputEvent(inputEvent3);
                    curTime3 = curTime;
                    movement = movement2;
                } else {
                    curTime3 = curTime;
                }
                while (movement > 0) {
                    movement--;
                    curTime3 = SystemClock.uptimeMillis();
                    long j = curTime3;
                    long j2 = curTime3;
                    int i4 = i;
                    int i5 = metaState;
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(j, j2, 0, i4, 0, i5, -1, 0, 1024, 257));
                    ViewRootImpl.this.enqueueInputEvent(new KeyEvent(j, j2, 1, i4, 0, i5, -1, 0, 1024, 257));
                }
                this.mLastTime = curTime3;
                curTime = curTime3;
            }
        }

        public void cancel() {
            this.mLastTime = -2147483648L;
            if (ViewRootImpl.this.mView != null && ViewRootImpl.this.mAdded) {
                ViewRootImpl.this.ensureTouchMode(false);
            }
        }
    }

    static final class SystemUiVisibilityInfo {
        int globalVisibility;
        int localChanges;
        int localValue;
        int seq;

        SystemUiVisibilityInfo() {
        }
    }

    class TakenSurfaceHolder extends BaseSurfaceHolder {
        TakenSurfaceHolder() {
        }

        public boolean onAllowLockCanvas() {
            return ViewRootImpl.this.mDrawingAllowed;
        }

        public void onRelayoutContainer() {
        }

        public void setFormat(int format) {
            ((RootViewSurfaceTaker) ViewRootImpl.this.mView).setSurfaceFormat(format);
        }

        public void setType(int type) {
            ((RootViewSurfaceTaker) ViewRootImpl.this.mView).setSurfaceType(type);
        }

        public void onUpdateSurface() {
            throw new IllegalStateException("Shouldn't be here");
        }

        public boolean isCreating() {
            return ViewRootImpl.this.mIsCreating;
        }

        public void setFixedSize(int width, int height) {
            throw new UnsupportedOperationException("Currently only support sizing from layout");
        }

        public void setKeepScreenOn(boolean screenOn) {
            ((RootViewSurfaceTaker) ViewRootImpl.this.mView).setSurfaceKeepScreenOn(screenOn);
        }
    }

    static final class TrackballAxis {
        static final float ACCEL_MOVE_SCALING_FACTOR = 0.025f;
        static final long FAST_MOVE_TIME = 150;
        static final float FIRST_MOVEMENT_THRESHOLD = 0.5f;
        static final float MAX_ACCELERATION = 20.0f;
        static final float SECOND_CUMULATIVE_MOVEMENT_THRESHOLD = 2.0f;
        static final float SUBSEQUENT_INCREMENTAL_MOVEMENT_THRESHOLD = 1.0f;
        float acceleration = 1.0f;
        int dir;
        long lastMoveTime = 0;
        int nonAccelMovement;
        float position;
        int step;

        TrackballAxis() {
        }

        /* Access modifiers changed, original: 0000 */
        public void reset(int _step) {
            this.position = 0.0f;
            this.acceleration = 1.0f;
            this.lastMoveTime = 0;
            this.step = _step;
            this.dir = 0;
        }

        /* Access modifiers changed, original: 0000 */
        public float collect(float off, long time, String axis) {
            long normTime;
            float f = 1.0f;
            if (off > 0.0f) {
                normTime = (long) (150.0f * off);
                if (this.dir < 0) {
                    this.position = 0.0f;
                    this.step = 0;
                    this.acceleration = 1.0f;
                    this.lastMoveTime = 0;
                }
                this.dir = 1;
            } else if (off < 0.0f) {
                normTime = (long) ((-off) * 150.0f);
                if (this.dir > 0) {
                    this.position = 0.0f;
                    this.step = 0;
                    this.acceleration = 1.0f;
                    this.lastMoveTime = 0;
                }
                this.dir = -1;
            } else {
                normTime = 0;
            }
            if (normTime > 0) {
                long delta = time - this.lastMoveTime;
                this.lastMoveTime = time;
                float acc = this.acceleration;
                float scale;
                if (delta < normTime) {
                    scale = ((float) (normTime - delta)) * ACCEL_MOVE_SCALING_FACTOR;
                    if (scale > 1.0f) {
                        acc *= scale;
                    }
                    float f2 = MAX_ACCELERATION;
                    if (acc < MAX_ACCELERATION) {
                        f2 = acc;
                    }
                    this.acceleration = f2;
                } else {
                    scale = ((float) (delta - normTime)) * ACCEL_MOVE_SCALING_FACTOR;
                    if (scale > 1.0f) {
                        acc /= scale;
                    }
                    if (acc > 1.0f) {
                        f = acc;
                    }
                    this.acceleration = f;
                }
            }
            this.position += off;
            return Math.abs(this.position);
        }

        /* Access modifiers changed, original: 0000 */
        public int generate() {
            int movement = 0;
            this.nonAccelMovement = 0;
            while (true) {
                int dir = this.position >= 0.0f ? 1 : -1;
                int i = this.step;
                if (i != 0) {
                    if (i != 1) {
                        if (Math.abs(this.position) < 1.0f) {
                            return movement;
                        }
                        movement += dir;
                        this.position -= ((float) dir) * 1.0f;
                        float acc = this.acceleration * 1.1f;
                        this.acceleration = acc < MAX_ACCELERATION ? acc : this.acceleration;
                    } else if (Math.abs(this.position) < SECOND_CUMULATIVE_MOVEMENT_THRESHOLD) {
                        return movement;
                    } else {
                        movement += dir;
                        this.nonAccelMovement += dir;
                        this.position -= ((float) dir) * SECOND_CUMULATIVE_MOVEMENT_THRESHOLD;
                        this.step = 2;
                    }
                } else if (Math.abs(this.position) < 0.5f) {
                    return movement;
                } else {
                    movement += dir;
                    this.nonAccelMovement += dir;
                    this.step = 1;
                }
            }
        }
    }

    final class TraversalRunnable implements Runnable {
        TraversalRunnable() {
        }

        public void run() {
            ViewRootImpl.this.doTraversal();
            ViewRootImpl.this.notifyContentChangeToContentCatcher();
        }
    }

    private static class UnhandledKeyManager {
        private final SparseArray<WeakReference<View>> mCapturedKeys;
        private WeakReference<View> mCurrentReceiver;
        private boolean mDispatched;

        private UnhandledKeyManager() {
            this.mDispatched = true;
            this.mCapturedKeys = new SparseArray();
            this.mCurrentReceiver = null;
        }

        /* synthetic */ UnhandledKeyManager(AnonymousClass1 x0) {
            this();
        }

        /* Access modifiers changed, original: 0000 */
        public boolean dispatch(View root, KeyEvent event) {
            if (this.mDispatched) {
                return false;
            }
            try {
                Trace.traceBegin(8, "UnhandledKeyEvent dispatch");
                boolean z = true;
                this.mDispatched = true;
                View consumer = root.dispatchUnhandledKeyEvent(event);
                if (event.getAction() == 0) {
                    int keycode = event.getKeyCode();
                    if (!(consumer == null || KeyEvent.isModifierKey(keycode))) {
                        this.mCapturedKeys.put(keycode, new WeakReference(consumer));
                    }
                }
                Trace.traceEnd(8);
                if (consumer == null) {
                    z = false;
                }
                return z;
            } catch (Throwable th) {
                Trace.traceEnd(8);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void preDispatch(KeyEvent event) {
            this.mCurrentReceiver = null;
            if (event.getAction() == 1) {
                int idx = this.mCapturedKeys.indexOfKey(event.getKeyCode());
                if (idx >= 0) {
                    this.mCurrentReceiver = (WeakReference) this.mCapturedKeys.valueAt(idx);
                    this.mCapturedKeys.removeAt(idx);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean preViewDispatch(KeyEvent event) {
            this.mDispatched = false;
            if (this.mCurrentReceiver == null) {
                this.mCurrentReceiver = (WeakReference) this.mCapturedKeys.get(event.getKeyCode());
            }
            WeakReference weakReference = this.mCurrentReceiver;
            if (weakReference == null) {
                return false;
            }
            View target = (View) weakReference.get();
            if (event.getAction() == 1) {
                this.mCurrentReceiver = null;
            }
            if (target != null && target.isAttachedToWindow()) {
                target.onUnhandledKeyEvent(event);
            }
            return true;
        }
    }

    final class ViewPostImeInputStage extends InputStage {
        public ViewPostImeInputStage(InputStage next) {
            super(next);
        }

        /* Access modifiers changed, original: protected */
        public int onProcess(QueuedInputEvent q) {
            if (q.mEvent instanceof KeyEvent) {
                return processKeyEvent(q);
            }
            int source = q.mEvent.getSource();
            if ((source & 2) != 0) {
                return processPointerEvent(q);
            }
            if ((source & 4) != 0) {
                return processTrackballEvent(q);
            }
            return processGenericMotionEvent(q);
        }

        /* Access modifiers changed, original: protected */
        public void onDeliverToNext(QueuedInputEvent q) {
            if (ViewRootImpl.this.mUnbufferedInputDispatch && (q.mEvent instanceof MotionEvent) && ((MotionEvent) q.mEvent).isTouchEvent() && ViewRootImpl.isTerminalInputEvent(q.mEvent)) {
                ViewRootImpl viewRootImpl = ViewRootImpl.this;
                viewRootImpl.mUnbufferedInputDispatch = false;
                viewRootImpl.scheduleConsumeBatchedInput();
            }
            super.onDeliverToNext(q);
        }

        private boolean performFocusNavigation(KeyEvent event) {
            int direction = 0;
            int keyCode = event.getKeyCode();
            if (keyCode != 61) {
                switch (keyCode) {
                    case 19:
                        if (event.hasNoModifiers()) {
                            direction = 33;
                            break;
                        }
                        break;
                    case 20:
                        if (event.hasNoModifiers()) {
                            direction = 130;
                            break;
                        }
                        break;
                    case 21:
                        if (event.hasNoModifiers()) {
                            direction = 17;
                            break;
                        }
                        break;
                    case 22:
                        if (event.hasNoModifiers()) {
                            direction = 66;
                            break;
                        }
                        break;
                }
            } else if (event.hasNoModifiers()) {
                direction = 2;
            } else if (event.hasModifiers(1)) {
                direction = 1;
            }
            if (direction != 0) {
                View focused = ViewRootImpl.this.mView.findFocus();
                if (focused != null) {
                    View v = focused.focusSearch(direction);
                    if (!(v == null || v == focused)) {
                        focused.getFocusedRect(ViewRootImpl.this.mTempRect);
                        if (ViewRootImpl.this.mView instanceof ViewGroup) {
                            ((ViewGroup) ViewRootImpl.this.mView).offsetDescendantRectToMyCoords(focused, ViewRootImpl.this.mTempRect);
                            ((ViewGroup) ViewRootImpl.this.mView).offsetRectIntoDescendantCoords(v, ViewRootImpl.this.mTempRect);
                        }
                        if (v.requestFocus(direction, ViewRootImpl.this.mTempRect)) {
                            ViewRootImpl.this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
                            return true;
                        }
                    }
                    if (ViewRootImpl.this.mView.dispatchUnhandledMove(focused, direction)) {
                        return true;
                    }
                } else if (ViewRootImpl.this.mView.restoreDefaultFocus()) {
                    return true;
                }
            }
            return false;
        }

        private boolean performKeyboardGroupNavigation(int direction) {
            View focused = ViewRootImpl.this.mView.findFocus();
            if (focused == null && ViewRootImpl.this.mView.restoreDefaultFocus()) {
                return true;
            }
            View cluster;
            if (focused == null) {
                cluster = ViewRootImpl.this.keyboardNavigationClusterSearch(null, direction);
            } else {
                cluster = focused.keyboardNavigationClusterSearch(null, direction);
            }
            int realDirection = direction;
            if (direction == 2 || direction == 1) {
                realDirection = 130;
            }
            if (cluster != null && cluster.isRootNamespace()) {
                if (cluster.restoreFocusNotInCluster()) {
                    ViewRootImpl.this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
                    return true;
                }
                cluster = ViewRootImpl.this.keyboardNavigationClusterSearch(null, direction);
            }
            if (cluster == null || !cluster.restoreFocusInCluster(realDirection)) {
                return false;
            }
            ViewRootImpl.this.playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
            return true;
        }

        private int processKeyEvent(QueuedInputEvent q) {
            KeyEvent event = q.mEvent;
            if (ViewRootImpl.this.mUnhandledKeyManager.preViewDispatch(event) || ViewRootImpl.this.mView.dispatchKeyEvent(event)) {
                return 1;
            }
            if (shouldDropInputEvent(q)) {
                return 2;
            }
            if (ViewRootImpl.this.mUnhandledKeyManager.dispatch(ViewRootImpl.this.mView, event)) {
                return 1;
            }
            int groupNavigationDirection = 0;
            if (event.getAction() == 0 && event.getKeyCode() == 61) {
                if (KeyEvent.metaStateHasModifiers(event.getMetaState(), 65536)) {
                    groupNavigationDirection = 2;
                } else if (KeyEvent.metaStateHasModifiers(event.getMetaState(), 65537)) {
                    groupNavigationDirection = 1;
                }
            }
            if (event.getAction() == 0 && !KeyEvent.metaStateHasNoModifiers(event.getMetaState()) && event.getRepeatCount() == 0 && !KeyEvent.isModifierKey(event.getKeyCode()) && groupNavigationDirection == 0) {
                if (ViewRootImpl.this.mView.dispatchKeyShortcutEvent(event)) {
                    return 1;
                }
                if (shouldDropInputEvent(q)) {
                    return 2;
                }
            }
            if (ViewRootImpl.this.mFallbackEventHandler.dispatchKeyEvent(event)) {
                return 1;
            }
            if (shouldDropInputEvent(q)) {
                return 2;
            }
            if (event.getAction() == 0) {
                if (groupNavigationDirection != 0) {
                    if (performKeyboardGroupNavigation(groupNavigationDirection)) {
                        return 1;
                    }
                } else if (performFocusNavigation(event)) {
                    return 1;
                }
            }
            return 0;
        }

        private int processPointerEvent(QueuedInputEvent q) {
            MotionEvent event = q.mEvent;
            ViewRootImplInjector.checkForThreeGesture(event);
            ViewRootImpl.this.mAttachInfo.mUnbufferedDispatchRequested = false;
            ViewRootImpl.this.mAttachInfo.mHandlingPointerEvent = true;
            boolean handled = ViewRootImpl.this.mView.dispatchPointerEvent(event);
            int action = event.getActionMasked();
            if (action == 2) {
                ViewRootImpl.this.mHaveMoveEvent = true;
            } else if (action == 1) {
                ViewRootImpl.this.mHaveMoveEvent = false;
            }
            maybeUpdatePointerIcon(event);
            ViewRootImpl.this.maybeUpdateTooltip(event);
            ViewRootImpl.this.mAttachInfo.mHandlingPointerEvent = false;
            if (ViewRootImpl.this.mAttachInfo.mUnbufferedDispatchRequested && !ViewRootImpl.this.mUnbufferedInputDispatch) {
                ViewRootImpl viewRootImpl = ViewRootImpl.this;
                viewRootImpl.mUnbufferedInputDispatch = true;
                if (viewRootImpl.mConsumeBatchedInputScheduled) {
                    ViewRootImpl.this.scheduleConsumeBatchedInputImmediately();
                }
            }
            return handled;
        }

        private void maybeUpdatePointerIcon(MotionEvent event) {
            if (event.getPointerCount() == 1 && event.isFromSource(8194)) {
                if (event.getActionMasked() == 9 || event.getActionMasked() == 10) {
                    ViewRootImpl.this.mPointerIconType = 1;
                }
                if (event.getActionMasked() != 10 && !ViewRootImpl.this.updatePointerIcon(event) && event.getActionMasked() == 7) {
                    ViewRootImpl.this.mPointerIconType = 1;
                }
            }
        }

        /* JADX WARNING: Missing block: B:6:0x0020, code skipped:
            return 1;
     */
        private int processTrackballEvent(android.view.ViewRootImpl.QueuedInputEvent r4) {
            /*
            r3 = this;
            r0 = r4.mEvent;
            r0 = (android.view.MotionEvent) r0;
            r1 = 131076; // 0x20004 float:1.83677E-40 double:6.476E-319;
            r1 = r0.isFromSource(r1);
            r2 = 1;
            if (r1 == 0) goto L_0x0021;
        L_0x000e:
            r1 = android.view.ViewRootImpl.this;
            r1 = r1.hasPointerCapture();
            if (r1 == 0) goto L_0x0020;
        L_0x0016:
            r1 = android.view.ViewRootImpl.this;
            r1 = r1.mView;
            r1 = r1.dispatchCapturedPointerEvent(r0);
            if (r1 == 0) goto L_0x0021;
        L_0x0020:
            return r2;
        L_0x0021:
            r1 = android.view.ViewRootImpl.this;
            r1 = r1.mView;
            r1 = r1.dispatchTrackballEvent(r0);
            if (r1 == 0) goto L_0x002c;
        L_0x002b:
            return r2;
        L_0x002c:
            r1 = 0;
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.view.ViewRootImpl$ViewPostImeInputStage.processTrackballEvent(android.view.ViewRootImpl$QueuedInputEvent):int");
        }

        private int processGenericMotionEvent(QueuedInputEvent q) {
            MotionEvent event = q.mEvent;
            if ((event.isFromSource(InputDevice.SOURCE_TOUCHPAD) && ViewRootImpl.this.hasPointerCapture() && ViewRootImpl.this.mView.dispatchCapturedPointerEvent(event)) || ViewRootImpl.this.mView.dispatchGenericMotionEvent(event)) {
                return 1;
            }
            return 0;
        }
    }

    final class ViewPreImeInputStage extends InputStage {
        public ViewPreImeInputStage(InputStage next) {
            super(next);
        }

        /* Access modifiers changed, original: protected */
        public int onProcess(QueuedInputEvent q) {
            if (q.mEvent instanceof KeyEvent) {
                return processKeyEvent(q);
            }
            return 0;
        }

        private int processKeyEvent(QueuedInputEvent q) {
            KeyEvent event = q.mEvent;
            ViewRootImpl.this.dispatchKeyEventToContentCatcher(event);
            if (ViewRootImpl.this.mView.dispatchKeyEventPreIme(event)) {
                return 1;
            }
            return 0;
        }
    }

    final class ViewRootHandler extends Handler {
        ViewRootHandler() {
        }

        public String getMessageName(Message message) {
            switch (message.what) {
                case 1:
                    return "MSG_INVALIDATE";
                case 2:
                    return "MSG_INVALIDATE_RECT";
                case 3:
                    return "MSG_DIE";
                case 4:
                    return "MSG_RESIZED";
                case 5:
                    return "MSG_RESIZED_REPORT";
                case 6:
                    return "MSG_WINDOW_FOCUS_CHANGED";
                case 7:
                    return "MSG_DISPATCH_INPUT_EVENT";
                case 8:
                    return "MSG_DISPATCH_APP_VISIBILITY";
                case 9:
                    return "MSG_DISPATCH_GET_NEW_SURFACE";
                case 11:
                    return "MSG_DISPATCH_KEY_FROM_IME";
                case 12:
                    return "MSG_DISPATCH_KEY_FROM_AUTOFILL";
                case 13:
                    return "MSG_CHECK_FOCUS";
                case 14:
                    return "MSG_CLOSE_SYSTEM_DIALOGS";
                case 15:
                    return "MSG_DISPATCH_DRAG_EVENT";
                case 16:
                    return "MSG_DISPATCH_DRAG_LOCATION_EVENT";
                case 17:
                    return "MSG_DISPATCH_SYSTEM_UI_VISIBILITY";
                case 18:
                    return "MSG_UPDATE_CONFIGURATION";
                case 19:
                    return "MSG_PROCESS_INPUT_EVENTS";
                case 21:
                    return "MSG_CLEAR_ACCESSIBILITY_FOCUS_HOST";
                case 23:
                    return "MSG_WINDOW_MOVED";
                case 24:
                    return "MSG_SYNTHESIZE_INPUT_EVENT";
                case 25:
                    return "MSG_DISPATCH_WINDOW_SHOWN";
                case 27:
                    return "MSG_UPDATE_POINTER_ICON";
                case 28:
                    return "MSG_POINTER_CAPTURE_CHANGED";
                case 29:
                    return "MSG_DRAW_FINISHED";
                case 30:
                    return "MSG_INSETS_CHANGED";
                case 31:
                    return "MSG_INSETS_CONTROL_CHANGED";
                case 32:
                    return "MSG_SYSTEM_GESTURE_EXCLUSION_CHANGED";
                default:
                    return super.getMessageName(message);
            }
        }

        public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
            if (msg.what != 26 || msg.obj != null) {
                return super.sendMessageAtTime(msg, uptimeMillis);
            }
            throw new NullPointerException("Attempted to call MSG_REQUEST_KEYBOARD_SHORTCUTS with null receiver:");
        }

        public void handleMessage(Message msg) {
            SomeArgs args;
            int l;
            int i = -1;
            boolean z = true;
            ViewRootImpl viewRootImpl;
            switch (msg.what) {
                case 1:
                    ((View) msg.obj).invalidate();
                    return;
                case 2:
                    InvalidateInfo info = msg.obj;
                    info.target.invalidate(info.left, info.top, info.right, info.bottom);
                    info.recycle();
                    return;
                case 3:
                    ViewRootImpl.this.doDie();
                    return;
                case 4:
                    args = (SomeArgs) msg.obj;
                    if (ViewRootImpl.this.mWinFrame.equals(args.arg1) && ViewRootImpl.this.mPendingOverscanInsets.equals(args.arg5) && ViewRootImpl.this.mPendingContentInsets.equals(args.arg2) && ViewRootImpl.this.mPendingStableInsets.equals(args.arg6) && ViewRootImpl.this.mPendingDisplayCutout.get().equals(args.arg9) && ViewRootImpl.this.mPendingVisibleInsets.equals(args.arg3) && ViewRootImpl.this.mPendingOutsets.equals(args.arg7) && ViewRootImpl.this.mPendingBackDropFrame.equals(args.arg8) && args.arg4 == null && args.argi1 == 0 && ViewRootImpl.this.mDisplay.getDisplayId() == args.argi3) {
                        return;
                    }
                case 5:
                    break;
                case 6:
                    ViewRootImpl.this.handleWindowFocusChanged();
                    return;
                case 7:
                    args = msg.obj;
                    ViewRootImpl.this.enqueueInputEvent(args.arg1, args.arg2, 0, true);
                    args.recycle();
                    return;
                case 8:
                    viewRootImpl = ViewRootImpl.this;
                    if (msg.arg1 == 0) {
                        z = false;
                    }
                    viewRootImpl.handleAppVisibility(z);
                    return;
                case 9:
                    ViewRootImpl.this.handleGetNewSurface();
                    return;
                case 11:
                    KeyEvent event = (KeyEvent) msg.obj;
                    if ((event.getFlags() & 8) != 0) {
                        event = KeyEvent.changeFlags(event, event.getFlags() & -9);
                    }
                    ViewRootImpl.this.enqueueInputEvent(event, null, 1, true);
                    return;
                case 12:
                    ViewRootImpl.this.enqueueInputEvent(msg.obj, null, 0, true);
                    return;
                case 13:
                    InputMethodManager imm = (InputMethodManager) ViewRootImpl.this.mContext.getSystemService(InputMethodManager.class);
                    if (imm != null) {
                        imm.checkFocus();
                        return;
                    }
                    return;
                case 14:
                    if (ViewRootImpl.this.mView != null) {
                        ViewRootImpl.this.mView.onCloseSystemDialogs((String) msg.obj);
                        return;
                    }
                    return;
                case 15:
                case 16:
                    DragEvent event2 = msg.obj;
                    event2.mLocalState = ViewRootImpl.this.mLocalDragState;
                    ViewRootImpl.this.handleDragEvent(event2);
                    return;
                case 17:
                    ViewRootImpl.this.handleDispatchSystemUiVisibilityChanged((SystemUiVisibilityInfo) msg.obj);
                    return;
                case 18:
                    Configuration config = msg.obj;
                    if (config.isOtherSeqNewer(ViewRootImpl.this.mLastReportedMergedConfiguration.getMergedConfiguration())) {
                        config = ViewRootImpl.this.mLastReportedMergedConfiguration.getGlobalConfiguration();
                    }
                    ViewRootImpl.this.mPendingMergedConfiguration.setConfiguration(config, ViewRootImpl.this.mLastReportedMergedConfiguration.getOverrideConfiguration());
                    ViewRootImpl viewRootImpl2 = ViewRootImpl.this;
                    viewRootImpl2.performConfigurationChange(viewRootImpl2.mPendingMergedConfiguration, false, -1);
                    return;
                case 19:
                    viewRootImpl = ViewRootImpl.this;
                    viewRootImpl.mProcessInputEventsScheduled = false;
                    viewRootImpl.doProcessInputEvents();
                    return;
                case 21:
                    ViewRootImpl.this.setAccessibilityFocus(null, null);
                    return;
                case 22:
                    if (ViewRootImpl.this.mView != null) {
                        viewRootImpl = ViewRootImpl.this;
                        viewRootImpl.invalidateWorld(viewRootImpl.mView);
                        return;
                    }
                    return;
                case 23:
                    if (ViewRootImpl.this.mAdded) {
                        int w = ViewRootImpl.this.mWinFrame.width();
                        i = ViewRootImpl.this.mWinFrame.height();
                        l = msg.arg1;
                        int t = msg.arg2;
                        ViewRootImpl.this.mTmpFrame.left = l;
                        ViewRootImpl.this.mTmpFrame.right = l + w;
                        ViewRootImpl.this.mTmpFrame.top = t;
                        ViewRootImpl.this.mTmpFrame.bottom = t + i;
                        ViewRootImpl viewRootImpl3 = ViewRootImpl.this;
                        viewRootImpl3.setFrame(viewRootImpl3.mTmpFrame);
                        ViewRootImpl.this.mPendingBackDropFrame.set(ViewRootImpl.this.mWinFrame);
                        viewRootImpl3 = ViewRootImpl.this;
                        viewRootImpl3.maybeHandleWindowMove(viewRootImpl3.mWinFrame);
                        return;
                    }
                    return;
                case 24:
                    ViewRootImpl.this.enqueueInputEvent(msg.obj, null, 32, true);
                    return;
                case 25:
                    ViewRootImpl.this.handleDispatchWindowShown();
                    return;
                case 26:
                    ViewRootImpl.this.handleRequestKeyboardShortcuts(msg.obj, msg.arg1);
                    return;
                case 27:
                    ViewRootImpl.this.resetPointerIcon(msg.obj);
                    return;
                case 28:
                    if (msg.arg1 == 0) {
                        z = false;
                    }
                    ViewRootImpl.this.handlePointerCaptureChanged(z);
                    return;
                case 29:
                    ViewRootImpl.this.pendingDrawFinished();
                    return;
                case 30:
                    ViewRootImpl.this.mInsetsController.onStateChanged((InsetsState) msg.obj);
                    return;
                case 31:
                    args = msg.obj;
                    ViewRootImpl.this.mInsetsController.onControlsChanged((InsetsSourceControl[]) args.arg2);
                    ViewRootImpl.this.mInsetsController.onStateChanged((InsetsState) args.arg1);
                    return;
                case 32:
                    ViewRootImpl.this.systemGestureExclusionChanged();
                    return;
                default:
                    return;
            }
            if (ViewRootImpl.this.mAdded) {
                ViewRootImpl viewRootImpl4;
                args = (SomeArgs) msg.obj;
                l = args.argi3;
                MergedConfiguration mergedConfiguration = args.arg4;
                boolean displayChanged = ViewRootImpl.this.mDisplay.getDisplayId() != l;
                boolean configChanged = false;
                if (!ViewRootImpl.this.mLastReportedMergedConfiguration.equals(mergedConfiguration)) {
                    viewRootImpl4 = ViewRootImpl.this;
                    if (displayChanged) {
                        i = l;
                    }
                    viewRootImpl4.performConfigurationChange(mergedConfiguration, false, i);
                    configChanged = true;
                } else if (displayChanged) {
                    ViewRootImpl viewRootImpl5 = ViewRootImpl.this;
                    viewRootImpl5.onMovedToDisplay(l, viewRootImpl5.mLastConfigurationFromResources);
                }
                boolean framesChanged = (ViewRootImpl.this.mWinFrame.equals(args.arg1) && ViewRootImpl.this.mPendingOverscanInsets.equals(args.arg5) && ViewRootImpl.this.mPendingContentInsets.equals(args.arg2) && ViewRootImpl.this.mPendingStableInsets.equals(args.arg6) && ViewRootImpl.this.mPendingDisplayCutout.get().equals(args.arg9) && ViewRootImpl.this.mPendingVisibleInsets.equals(args.arg3) && ViewRootImpl.this.mPendingOutsets.equals(args.arg7)) ? false : true;
                ViewRootImpl.this.setFrame((Rect) args.arg1);
                ViewRootImpl.this.mPendingOverscanInsets.set((Rect) args.arg5);
                ViewRootImpl.this.mPendingContentInsets.set((Rect) args.arg2);
                ViewRootImpl.this.mPendingStableInsets.set((Rect) args.arg6);
                ViewRootImpl.this.mPendingDisplayCutout.set((DisplayCutout) args.arg9);
                ViewRootImpl.this.mPendingVisibleInsets.set((Rect) args.arg3);
                ViewRootImpl.this.mPendingOutsets.set((Rect) args.arg7);
                ViewRootImpl.this.mPendingBackDropFrame.set((Rect) args.arg8);
                ViewRootImpl.this.mForceNextWindowRelayout = args.argi1 != 0;
                viewRootImpl4 = ViewRootImpl.this;
                if (args.argi2 == 0) {
                    z = false;
                }
                viewRootImpl4.mPendingAlwaysConsumeSystemBars = z;
                args.recycle();
                if (msg.what == 5) {
                    ViewRootImpl.this.reportNextDraw();
                }
                if (ViewRootImpl.this.mView != null && (framesChanged || configChanged)) {
                    ViewRootImpl.forceLayout(ViewRootImpl.this.mView);
                }
                ViewRootImpl.this.requestLayout();
            }
        }
    }

    static class W extends IWindow.Stub {
        private final WeakReference<ViewRootImpl> mViewAncestor;
        private final IWindowSession mWindowSession;

        W(ViewRootImpl viewAncestor) {
            this.mViewAncestor = new WeakReference(viewAncestor);
            this.mWindowSession = viewAncestor.mWindowSession;
        }

        public void resized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, MergedConfiguration mergedConfiguration, Rect backDropFrame, boolean forceLayout, boolean alwaysConsumeSystemBars, int displayId, ParcelableWrapper displayCutout) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchResized(frame, overscanInsets, contentInsets, visibleInsets, stableInsets, outsets, reportDraw, mergedConfiguration, backDropFrame, forceLayout, alwaysConsumeSystemBars, displayId, displayCutout);
            }
        }

        public void insetsChanged(InsetsState insetsState) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchInsetsChanged(insetsState);
            }
        }

        public void insetsControlChanged(InsetsState insetsState, InsetsSourceControl[] activeControls) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchInsetsControlChanged(insetsState, activeControls);
            }
        }

        public void moved(int newX, int newY) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchMoved(newX, newY);
            }
        }

        public void dispatchAppVisibility(boolean visible) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchAppVisibility(visible);
            }
        }

        public void dispatchGetNewSurface() {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchGetNewSurface();
            }
        }

        public void windowFocusChanged(boolean hasFocus, boolean inTouchMode) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.windowFocusChanged(hasFocus, inTouchMode);
            }
        }

        private static int checkCallingPermission(String permission) {
            try {
                return ActivityManager.getService().checkPermission(permission, Binder.getCallingPid(), Binder.getCallingUid());
            } catch (RemoteException e) {
                return -1;
            }
        }

        public void executeCommand(String command, String parameters, ParcelFileDescriptor out) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                View view = viewAncestor.mView;
                if (view == null) {
                    return;
                }
                if (checkCallingPermission(permission.DUMP) == 0) {
                    OutputStream clientStream = null;
                    try {
                        clientStream = new AutoCloseOutputStream(out);
                        ViewDebug.dispatchCommand(view, command, parameters, clientStream);
                        try {
                            clientStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e2) {
                        e2.printStackTrace();
                        if (clientStream != null) {
                            clientStream.close();
                        }
                    } catch (Throwable th) {
                        if (clientStream != null) {
                            try {
                                clientStream.close();
                            } catch (IOException e3) {
                                e3.printStackTrace();
                            }
                        }
                    }
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Insufficient permissions to invoke executeCommand() from pid=");
                    stringBuilder.append(Binder.getCallingPid());
                    stringBuilder.append(", uid=");
                    stringBuilder.append(Binder.getCallingUid());
                    throw new SecurityException(stringBuilder.toString());
                }
            }
        }

        public void closeSystemDialogs(String reason) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchCloseSystemDialogs(reason);
            }
        }

        public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) {
            if (sync) {
                try {
                    this.mWindowSession.wallpaperOffsetsComplete(asBinder());
                } catch (RemoteException e) {
                }
            }
        }

        public void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras, boolean sync) {
            if (sync) {
                try {
                    this.mWindowSession.wallpaperCommandComplete(asBinder(), null);
                } catch (RemoteException e) {
                }
            }
        }

        public void dispatchDragEvent(DragEvent event) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchDragEvent(event);
            }
        }

        public void updatePointerIcon(float x, float y) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.updatePointerIcon(x, y);
            }
        }

        public void dispatchSystemUiVisibilityChanged(int seq, int globalVisibility, int localValue, int localChanges) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchSystemUiVisibilityChanged(seq, globalVisibility, localValue, localChanges);
            }
        }

        public void dispatchWindowShown() {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchWindowShown();
            }
        }

        public void requestAppKeyboardShortcuts(IResultReceiver receiver, int deviceId) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchRequestKeyboardShortcuts(receiver, deviceId);
            }
        }

        public void dispatchPointerCaptureChanged(boolean hasCapture) {
            ViewRootImpl viewAncestor = (ViewRootImpl) this.mViewAncestor.get();
            if (viewAncestor != null) {
                viewAncestor.dispatchPointerCaptureChanged(hasCapture);
            }
        }
    }

    final class WindowInputEventReceiver extends InputEventReceiver {
        public WindowInputEventReceiver(InputChannel inputChannel, Looper looper) {
            super(inputChannel, looper);
        }

        public void onInputEvent(InputEvent event) {
            ViewRootImplInjector.logOnInputEvent(event);
            Trace.traceBegin(8, "processInputEventForCompatibility");
            try {
                List<InputEvent> processedEvents = ViewRootImpl.this.mInputCompatProcessor.processInputEventForCompatibility(event);
                if (processedEvents == null) {
                    ViewRootImpl.this.enqueueInputEvent(event, this, 0, true);
                } else if (processedEvents.isEmpty()) {
                    finishInputEvent(event, true);
                } else {
                    for (int i = 0; i < processedEvents.size(); i++) {
                        ViewRootImpl.this.enqueueInputEvent((InputEvent) processedEvents.get(i), this, 64, true);
                    }
                }
            } finally {
                Trace.traceEnd(8);
            }
        }

        public void onBatchedInputEventPending() {
            if (ViewRootImpl.this.mUnbufferedInputDispatch) {
                super.onBatchedInputEventPending();
            } else {
                ViewRootImpl.this.scheduleConsumeBatchedInput();
            }
        }

        public void dispose() {
            ViewRootImpl.this.unscheduleConsumeBatchedInput();
            super.dispose();
        }
    }

    public ViewRootImpl(Context context, Display display) {
        boolean z = false;
        this.mForceDecorViewVisibility = false;
        this.mOrigWindowType = -1;
        this.mStopped = false;
        this.mIsAmbientMode = false;
        this.mPausedForTransition = false;
        this.mLastInCompatMode = false;
        this.mTempBoundsRect = new Rect();
        this.mPendingInputEventQueueLengthCounterName = "pq";
        this.mUnhandledKeyManager = new UnhandledKeyManager();
        this.mWindowAttributesChanged = false;
        this.mWindowAttributesChangesFlag = 0;
        this.mSurface = new Surface();
        this.mSurfaceControl = new SurfaceControl();
        this.mBoundsSurface = new Surface();
        this.mTransaction = new Transaction();
        this.mTmpFrame = new Rect();
        this.mPendingOverscanInsets = new Rect();
        this.mPendingVisibleInsets = new Rect();
        this.mPendingStableInsets = new Rect();
        this.mPendingContentInsets = new Rect();
        this.mPendingOutsets = new Rect();
        this.mPendingBackDropFrame = new Rect();
        this.mPendingDisplayCutout = new ParcelableWrapper(DisplayCutout.NO_CUTOUT);
        this.mTempInsets = new InsetsState();
        this.mLastGivenInsets = new InternalInsetsInfo();
        this.mDispatchContentInsets = new Rect();
        this.mDispatchStableInsets = new Rect();
        this.mDispatchDisplayCutout = DisplayCutout.NO_CUTOUT;
        this.mLastConfigurationFromResources = new Configuration();
        this.mLastReportedMergedConfiguration = new MergedConfiguration();
        this.mPendingMergedConfiguration = new MergedConfiguration();
        this.mDragPoint = new PointF();
        this.mLastTouchPoint = new PointF();
        this.mFpsStartTime = -1;
        this.mFpsPrevTime = -1;
        this.mPointerIconType = 1;
        this.mCustomPointerIcon = null;
        this.mAccessibilityInteractionConnectionManager = new AccessibilityInteractionConnectionManager();
        this.mInLayout = false;
        this.mLayoutRequesters = new ArrayList();
        this.mHandlingLayoutInLayoutRequest = false;
        this.mInputEventConsistencyVerifier = InputEventConsistencyVerifier.isInstrumentationEnabled() ? new InputEventConsistencyVerifier(this, 0) : null;
        this.mInsetsController = new InsetsController(this);
        this.mGestureExclusionTracker = new GestureExclusionTracker();
        String str = TAG;
        this.mTag = str;
        this.mHaveMoveEvent = false;
        this.mProfile = false;
        this.mDisplayListener = new DisplayListener() {
            public void onDisplayChanged(int displayId) {
                if (ViewRootImpl.this.mView != null && ViewRootImpl.this.mDisplay.getDisplayId() == displayId) {
                    int oldDisplayState = ViewRootImpl.this.mAttachInfo.mDisplayState;
                    int newDisplayState = ViewRootImpl.this.mDisplay.getState();
                    if (oldDisplayState != newDisplayState) {
                        ViewRootImpl.this.mAttachInfo.mDisplayState = newDisplayState;
                        ViewRootImpl.this.pokeDrawLockIfNeeded();
                        if (oldDisplayState != 0) {
                            int oldScreenState = toViewScreenState(oldDisplayState);
                            int newScreenState = toViewScreenState(newDisplayState);
                            if (oldScreenState != newScreenState) {
                                ViewRootImpl.this.mView.dispatchScreenStateChanged(newScreenState);
                            }
                            if (oldDisplayState == 1) {
                                ViewRootImpl viewRootImpl = ViewRootImpl.this;
                                viewRootImpl.mFullRedrawNeeded = true;
                                viewRootImpl.scheduleTraversals();
                            }
                        }
                    }
                }
            }

            public void onDisplayRemoved(int displayId) {
            }

            public void onDisplayAdded(int displayId) {
            }

            private int toViewScreenState(int displayState) {
                if (displayState == 1) {
                    return 0;
                }
                return 1;
            }
        };
        this.mWindowStoppedCallbacks = new ArrayList();
        this.mDrawsNeededToReport = 0;
        this.mHandler = new ViewRootHandler();
        this.mTraversalRunnable = new TraversalRunnable();
        this.mConsumedBatchedInputRunnable = new ConsumeBatchedInputRunnable();
        this.mConsumeBatchedInputImmediatelyRunnable = new ConsumeBatchedInputImmediatelyRunnable();
        this.mInvalidateOnAnimationRunnable = new InvalidateOnAnimationRunnable();
        this.mContext = context;
        this.mWindowSession = WindowManagerGlobal.getWindowSession();
        this.mDisplay = display;
        this.mBasePackageName = context.getBasePackageName();
        this.mThread = Thread.currentThread();
        this.mLocation = new WindowLeaked(null);
        this.mLocation.fillInStackTrace();
        this.mWidth = -1;
        this.mHeight = -1;
        this.mDirty = new Rect();
        this.mTempRect = new Rect();
        this.mVisRect = new Rect();
        this.mWinFrame = new Rect();
        this.mWindow = new W(this);
        this.mTargetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        this.mViewVisibility = 8;
        this.mTransparentRegion = new Region();
        this.mPreviousTransparentRegion = new Region();
        this.mFirst = true;
        this.mAdded = false;
        this.mAttachInfo = new AttachInfo(this.mWindowSession, this.mWindow, display, this, this.mHandler, this, context);
        this.mAccessibilityManager = AccessibilityManager.getInstance(context);
        this.mAccessibilityManager.addAccessibilityStateChangeListener(this.mAccessibilityInteractionConnectionManager, this.mHandler);
        this.mHighContrastTextManager = new HighContrastTextManager();
        this.mAccessibilityManager.addHighTextContrastStateChangeListener(this.mHighContrastTextManager, this.mHandler);
        this.mViewConfiguration = ViewConfiguration.get(context);
        this.mDensity = context.getResources().getDisplayMetrics().densityDpi;
        this.mNoncompatDensity = context.getResources().getDisplayMetrics().noncompatDensityDpi;
        this.mFallbackEventHandler = new PhoneFallbackEventHandler(context);
        this.mChoreographer = Choreographer.getInstance();
        this.mDisplayManager = (DisplayManager) context.getSystemService(Context.DISPLAY_SERVICE);
        String processorOverrideName = context.getResources().getString(R.string.config_inputEventCompatProcessorOverrideClassName);
        if (processorOverrideName.isEmpty()) {
            this.mInputCompatProcessor = new InputEventCompatProcessor(context);
        } else {
            try {
                InputEventCompatProcessor inputEventCompatProcessor = (InputEventCompatProcessor) Class.forName(processorOverrideName).getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
            } catch (Exception e) {
                Log.e(str, "Unable to create the InputEventCompatProcessor. ", e);
            } finally {
                this.mInputCompatProcessor = null;
            }
        }
        if (!sCompatibilityDone) {
            if (this.mTargetSdkVersion < 28) {
                z = true;
            }
            sAlwaysAssignFocus = z;
            sCompatibilityDone = true;
        }
        loadSystemProperties();
    }

    public static void addFirstDrawHandler(Runnable callback) {
        synchronized (sFirstDrawHandlers) {
            if (!sFirstDrawComplete) {
                sFirstDrawHandlers.add(callback);
            }
        }
    }

    @UnsupportedAppUsage
    public static void addConfigCallback(ConfigChangedCallback callback) {
        synchronized (sConfigCallbacks) {
            sConfigCallbacks.add(callback);
        }
    }

    public void setActivityConfigCallback(ActivityConfigCallback callback) {
        this.mActivityConfigCallback = callback;
    }

    public void addWindowCallbacks(WindowCallbacks callback) {
        synchronized (this.mWindowCallbacks) {
            this.mWindowCallbacks.add(callback);
        }
    }

    public void removeWindowCallbacks(WindowCallbacks callback) {
        synchronized (this.mWindowCallbacks) {
            this.mWindowCallbacks.remove(callback);
        }
    }

    public void reportDrawFinish() {
        CountDownLatch countDownLatch = this.mWindowDrawCountDown;
        if (countDownLatch != null) {
            countDownLatch.countDown();
        }
    }

    public void profile() {
        this.mProfile = true;
    }

    static boolean isInTouchMode() {
        IWindowSession windowSession = WindowManagerGlobal.peekWindowSession();
        if (windowSession != null) {
            try {
                return windowSession.getInTouchMode();
            } catch (RemoteException e) {
            }
        }
        return false;
    }

    public void notifyChildRebuilt() {
        if (this.mView instanceof RootViewSurfaceTaker) {
            Callback2 callback2 = this.mSurfaceHolderCallback;
            if (callback2 != null) {
                this.mSurfaceHolder.removeCallback(callback2);
            }
            this.mSurfaceHolderCallback = ((RootViewSurfaceTaker) this.mView).willYouTakeTheSurface();
            if (this.mSurfaceHolderCallback != null) {
                this.mSurfaceHolder = new TakenSurfaceHolder();
                this.mSurfaceHolder.setFormat(0);
                this.mSurfaceHolder.addCallback(this.mSurfaceHolderCallback);
            } else {
                this.mSurfaceHolder = null;
            }
            this.mInputQueueCallback = ((RootViewSurfaceTaker) this.mView).willYouTakeTheInputQueue();
            Callback callback = this.mInputQueueCallback;
            if (callback != null) {
                callback.onInputQueueCreated(this.mInputQueue);
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:147:0x041b A:{SYNTHETIC, Splitter:B:147:0x041b} */
    /* JADX WARNING: Removed duplicated region for block: B:147:0x041b A:{SYNTHETIC, Splitter:B:147:0x041b} */
    /* JADX WARNING: Missing block: B:155:0x0425, code skipped:
            return;
     */
    public void setView(android.view.View r24, android.view.WindowManager.LayoutParams r25, android.view.View r26) {
        /*
        r23 = this;
        r1 = r23;
        r2 = r24;
        monitor-enter(r23);
        r0 = r1.mView;	 Catch:{ all -> 0x0426 }
        if (r0 != 0) goto L_0x0422;
    L_0x0009:
        r1.mView = r2;	 Catch:{ all -> 0x0426 }
        r0 = r1.mAttachInfo;	 Catch:{ all -> 0x0426 }
        r3 = r1.mDisplay;	 Catch:{ all -> 0x0426 }
        r3 = r3.getState();	 Catch:{ all -> 0x0426 }
        r0.mDisplayState = r3;	 Catch:{ all -> 0x0426 }
        r0 = r1.mDisplayManager;	 Catch:{ all -> 0x0426 }
        r3 = r1.mDisplayListener;	 Catch:{ all -> 0x0426 }
        r4 = r1.mHandler;	 Catch:{ all -> 0x0426 }
        r0.registerDisplayListener(r3, r4);	 Catch:{ all -> 0x0426 }
        r0 = r1.mView;	 Catch:{ all -> 0x0426 }
        r0 = r0.getRawLayoutDirection();	 Catch:{ all -> 0x0426 }
        r1.mViewLayoutDirectionInitial = r0;	 Catch:{ all -> 0x0426 }
        r0 = r1.mFallbackEventHandler;	 Catch:{ all -> 0x0426 }
        r0.setView(r2);	 Catch:{ all -> 0x0426 }
        r0 = r1.mWindowAttributes;	 Catch:{ all -> 0x0426 }
        r3 = r25;
        r0.copyFrom(r3);	 Catch:{ all -> 0x0420 }
        r0 = r1.mWindowAttributes;	 Catch:{ all -> 0x0420 }
        r0 = r0.packageName;	 Catch:{ all -> 0x0420 }
        if (r0 != 0) goto L_0x003e;
    L_0x0038:
        r0 = r1.mWindowAttributes;	 Catch:{ all -> 0x0420 }
        r4 = r1.mBasePackageName;	 Catch:{ all -> 0x0420 }
        r0.packageName = r4;	 Catch:{ all -> 0x0420 }
    L_0x003e:
        r0 = r1.mWindowAttributes;	 Catch:{ all -> 0x0420 }
        r3 = r0;
        r23.setTag();	 Catch:{ all -> 0x042b }
        r0 = r3.flags;	 Catch:{ all -> 0x042b }
        r1.mClientWindowLayoutFlags = r0;	 Catch:{ all -> 0x042b }
        r4 = 0;
        r1.setAccessibilityFocus(r4, r4);	 Catch:{ all -> 0x042b }
        r0 = r2 instanceof com.android.internal.view.RootViewSurfaceTaker;	 Catch:{ all -> 0x042b }
        r5 = 0;
        if (r0 == 0) goto L_0x0071;
    L_0x0051:
        r0 = r2;
        r0 = (com.android.internal.view.RootViewSurfaceTaker) r0;	 Catch:{ all -> 0x042b }
        r0 = r0.willYouTakeTheSurface();	 Catch:{ all -> 0x042b }
        r1.mSurfaceHolderCallback = r0;	 Catch:{ all -> 0x042b }
        r0 = r1.mSurfaceHolderCallback;	 Catch:{ all -> 0x042b }
        if (r0 == 0) goto L_0x0071;
    L_0x005e:
        r0 = new android.view.ViewRootImpl$TakenSurfaceHolder;	 Catch:{ all -> 0x042b }
        r0.<init>();	 Catch:{ all -> 0x042b }
        r1.mSurfaceHolder = r0;	 Catch:{ all -> 0x042b }
        r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x042b }
        r0.setFormat(r5);	 Catch:{ all -> 0x042b }
        r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x042b }
        r6 = r1.mSurfaceHolderCallback;	 Catch:{ all -> 0x042b }
        r0.addCallback(r6);	 Catch:{ all -> 0x042b }
    L_0x0071:
        r0 = r3.hasManualSurfaceInsets;	 Catch:{ all -> 0x042b }
        r6 = 1;
        if (r0 != 0) goto L_0x0079;
    L_0x0076:
        r3.setSurfaceInsets(r2, r5, r6);	 Catch:{ all -> 0x042b }
    L_0x0079:
        r0 = r1.mDisplay;	 Catch:{ all -> 0x042b }
        r0 = r0.getDisplayAdjustments();	 Catch:{ all -> 0x042b }
        r0 = r0.getCompatibilityInfo();	 Catch:{ all -> 0x042b }
        r7 = r0;
        r0 = r7.getTranslator();	 Catch:{ all -> 0x042b }
        r1.mTranslator = r0;	 Catch:{ all -> 0x042b }
        r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x042b }
        if (r0 != 0) goto L_0x00a3;
    L_0x008e:
        r1.enableHardwareAcceleration(r3);	 Catch:{ all -> 0x042b }
        r0 = r1.mAttachInfo;	 Catch:{ all -> 0x042b }
        r0 = r0.mThreadedRenderer;	 Catch:{ all -> 0x042b }
        if (r0 == 0) goto L_0x0099;
    L_0x0097:
        r0 = r6;
        goto L_0x009a;
    L_0x0099:
        r0 = r5;
    L_0x009a:
        r8 = r1.mUseMTRenderer;	 Catch:{ all -> 0x042b }
        if (r8 == r0) goto L_0x00a3;
    L_0x009e:
        r23.endDragResizing();	 Catch:{ all -> 0x042b }
        r1.mUseMTRenderer = r0;	 Catch:{ all -> 0x042b }
    L_0x00a3:
        r0 = 0;
        r8 = r1.mTranslator;	 Catch:{ all -> 0x042b }
        if (r8 == 0) goto L_0x00ba;
    L_0x00a8:
        r8 = r1.mSurface;	 Catch:{ all -> 0x042b }
        r9 = r1.mTranslator;	 Catch:{ all -> 0x042b }
        r8.setCompatibilityTranslator(r9);	 Catch:{ all -> 0x042b }
        r0 = 1;
        r3.backup();	 Catch:{ all -> 0x042b }
        r8 = r1.mTranslator;	 Catch:{ all -> 0x042b }
        r8.translateWindowLayout(r3);	 Catch:{ all -> 0x042b }
        r8 = r0;
        goto L_0x00bb;
    L_0x00ba:
        r8 = r0;
    L_0x00bb:
        r0 = r7.supportsScreen();	 Catch:{ all -> 0x042b }
        if (r0 != 0) goto L_0x00c9;
    L_0x00c1:
        r0 = r3.privateFlags;	 Catch:{ all -> 0x042b }
        r0 = r0 | 128;
        r3.privateFlags = r0;	 Catch:{ all -> 0x042b }
        r1.mLastInCompatMode = r6;	 Catch:{ all -> 0x042b }
    L_0x00c9:
        r0 = r3.softInputMode;	 Catch:{ all -> 0x042b }
        r1.mSoftInputMode = r0;	 Catch:{ all -> 0x042b }
        r1.mWindowAttributesChanged = r6;	 Catch:{ all -> 0x042b }
        r0 = -1;
        r1.mWindowAttributesChangesFlag = r0;	 Catch:{ all -> 0x042b }
        r0 = r1.mAttachInfo;	 Catch:{ all -> 0x042b }
        r0.mRootView = r2;	 Catch:{ all -> 0x042b }
        r0 = r1.mAttachInfo;	 Catch:{ all -> 0x042b }
        r9 = r1.mTranslator;	 Catch:{ all -> 0x042b }
        if (r9 == 0) goto L_0x00de;
    L_0x00dc:
        r9 = r6;
        goto L_0x00df;
    L_0x00de:
        r9 = r5;
    L_0x00df:
        r0.mScalingRequired = r9;	 Catch:{ all -> 0x042b }
        r0 = r1.mAttachInfo;	 Catch:{ all -> 0x042b }
        r9 = r1.mTranslator;	 Catch:{ all -> 0x042b }
        if (r9 != 0) goto L_0x00ea;
    L_0x00e7:
        r9 = 1065353216; // 0x3f800000 float:1.0 double:5.263544247E-315;
        goto L_0x00ee;
    L_0x00ea:
        r9 = r1.mTranslator;	 Catch:{ all -> 0x042b }
        r9 = r9.applicationScale;	 Catch:{ all -> 0x042b }
    L_0x00ee:
        r0.mApplicationScale = r9;	 Catch:{ all -> 0x042b }
        if (r26 == 0) goto L_0x00fa;
    L_0x00f2:
        r0 = r1.mAttachInfo;	 Catch:{ all -> 0x042b }
        r9 = r26.getApplicationWindowToken();	 Catch:{ all -> 0x042b }
        r0.mPanelParentWindowToken = r9;	 Catch:{ all -> 0x042b }
    L_0x00fa:
        r1.mAdded = r6;	 Catch:{ all -> 0x042b }
        r23.requestLayout();	 Catch:{ all -> 0x042b }
        r0 = r1.mWindowAttributes;	 Catch:{ all -> 0x042b }
        r0 = r0.inputFeatures;	 Catch:{ all -> 0x042b }
        r0 = r0 & 2;
        if (r0 != 0) goto L_0x010e;
    L_0x0107:
        r0 = new android.view.InputChannel;	 Catch:{ all -> 0x042b }
        r0.<init>();	 Catch:{ all -> 0x042b }
        r1.mInputChannel = r0;	 Catch:{ all -> 0x042b }
    L_0x010e:
        r0 = r1.mWindowAttributes;	 Catch:{ all -> 0x042b }
        r0 = r0.privateFlags;	 Catch:{ all -> 0x042b }
        r0 = r0 & 16384;
        if (r0 == 0) goto L_0x0118;
    L_0x0116:
        r0 = r6;
        goto L_0x0119;
    L_0x0118:
        r0 = r5;
    L_0x0119:
        r1.mForceDecorViewVisibility = r0;	 Catch:{ all -> 0x042b }
        r0 = r1.mInputChannel;	 Catch:{ all -> 0x042b }
        if (r0 != 0) goto L_0x0126;
    L_0x011f:
        r0 = new android.view.InputChannel;	 Catch:{ all -> 0x042b }
        r0.<init>();	 Catch:{ all -> 0x042b }
        r1.mInputChannel = r0;	 Catch:{ all -> 0x042b }
    L_0x0126:
        r0 = r1.mWindowAttributes;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r0 = r0.type;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r1.mOrigWindowType = r0;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r0 = r1.mAttachInfo;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r0.mRecomputeGlobalAttributes = r6;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r23.collectViewAttributes();	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r9 = r1.mWindowSession;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r10 = r1.mWindow;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r11 = r1.mSeq;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r12 = r1.mWindowAttributes;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r13 = r23.getHostVisibility();	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r0 = r1.mDisplay;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r14 = r0.getDisplayId();	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r15 = r1.mTmpFrame;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r0 = r1.mAttachInfo;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r0 = r0.mContentInsets;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r6 = r1.mAttachInfo;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r6 = r6.mStableInsets;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r4 = r1.mAttachInfo;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r4 = r4.mOutsets;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r5 = r1.mAttachInfo;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r5 = r5.mDisplayCutout;	 Catch:{ RemoteException -> 0x03f6, all -> 0x03f2 }
        r22 = r7;
        r7 = r1.mInputChannel;	 Catch:{ RemoteException -> 0x03f0 }
        r2 = r1.mTempInsets;	 Catch:{ RemoteException -> 0x03ec, all -> 0x03e8 }
        r16 = r0;
        r17 = r6;
        r18 = r4;
        r19 = r5;
        r20 = r7;
        r21 = r2;
        r0 = r9.addToDisplay(r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21);	 Catch:{ RemoteException -> 0x03ec, all -> 0x03e8 }
        r2 = r1.mTmpFrame;	 Catch:{ RemoteException -> 0x03ec, all -> 0x03e8 }
        r1.setFrame(r2);	 Catch:{ RemoteException -> 0x03ec, all -> 0x03e8 }
        if (r8 == 0) goto L_0x0177;
    L_0x0174:
        r3.restore();	 Catch:{ all -> 0x03e4 }
    L_0x0177:
        r2 = r1.mTranslator;	 Catch:{ all -> 0x03e4 }
        if (r2 == 0) goto L_0x0184;
    L_0x017b:
        r2 = r1.mTranslator;	 Catch:{ all -> 0x03e4 }
        r4 = r1.mAttachInfo;	 Catch:{ all -> 0x03e4 }
        r4 = r4.mContentInsets;	 Catch:{ all -> 0x03e4 }
        r2.translateRectInScreenToAppWindow(r4);	 Catch:{ all -> 0x03e4 }
    L_0x0184:
        r2 = r1.mPendingOverscanInsets;	 Catch:{ all -> 0x03e4 }
        r4 = 0;
        r2.set(r4, r4, r4, r4);	 Catch:{ all -> 0x03e4 }
        r2 = r1.mPendingContentInsets;	 Catch:{ all -> 0x03e4 }
        r4 = r1.mAttachInfo;	 Catch:{ all -> 0x03e4 }
        r4 = r4.mContentInsets;	 Catch:{ all -> 0x03e4 }
        r2.set(r4);	 Catch:{ all -> 0x03e4 }
        r2 = r1.mPendingStableInsets;	 Catch:{ all -> 0x03e4 }
        r4 = r1.mAttachInfo;	 Catch:{ all -> 0x03e4 }
        r4 = r4.mStableInsets;	 Catch:{ all -> 0x03e4 }
        r2.set(r4);	 Catch:{ all -> 0x03e4 }
        r2 = r1.mPendingDisplayCutout;	 Catch:{ all -> 0x03e4 }
        r4 = r1.mAttachInfo;	 Catch:{ all -> 0x03e4 }
        r4 = r4.mDisplayCutout;	 Catch:{ all -> 0x03e4 }
        r2.set(r4);	 Catch:{ all -> 0x03e4 }
        r2 = r1.mPendingVisibleInsets;	 Catch:{ all -> 0x03e4 }
        r4 = 0;
        r2.set(r4, r4, r4, r4);	 Catch:{ all -> 0x03e4 }
        r2 = r1.mAttachInfo;	 Catch:{ all -> 0x03e4 }
        r4 = r0 & 4;
        if (r4 == 0) goto L_0x01b3;
    L_0x01b1:
        r4 = 1;
        goto L_0x01b4;
    L_0x01b3:
        r4 = 0;
    L_0x01b4:
        r2.mAlwaysConsumeSystemBars = r4;	 Catch:{ all -> 0x03e4 }
        r2 = r1.mAttachInfo;	 Catch:{ all -> 0x03e4 }
        r2 = r2.mAlwaysConsumeSystemBars;	 Catch:{ all -> 0x03e4 }
        r1.mPendingAlwaysConsumeSystemBars = r2;	 Catch:{ all -> 0x03e4 }
        r2 = r1.mInsetsController;	 Catch:{ all -> 0x03e4 }
        r4 = r1.mTempInsets;	 Catch:{ all -> 0x03e4 }
        r2.onStateChanged(r4);	 Catch:{ all -> 0x03e4 }
        if (r0 >= 0) goto L_0x0305;
    L_0x01c5:
        r2 = r1.mAttachInfo;	 Catch:{ all -> 0x03e4 }
        r4 = 0;
        r2.mRootView = r4;	 Catch:{ all -> 0x03e4 }
        r2 = 0;
        r1.mAdded = r2;	 Catch:{ all -> 0x03e4 }
        r2 = r1.mFallbackEventHandler;	 Catch:{ all -> 0x03e4 }
        r2.setView(r4);	 Catch:{ all -> 0x03e4 }
        r23.unscheduleTraversals();	 Catch:{ all -> 0x03e4 }
        r1.setAccessibilityFocus(r4, r4);	 Catch:{ all -> 0x03e4 }
        switch(r0) {
            case -10: goto L_0x02c6;
            case -9: goto L_0x02a8;
            case -8: goto L_0x0283;
            case -7: goto L_0x0259;
            case -6: goto L_0x0257;
            case -5: goto L_0x0239;
            case -4: goto L_0x021b;
            case -3: goto L_0x01fd;
            case -2: goto L_0x01df;
            case -1: goto L_0x01df;
            default: goto L_0x01db;
        };	 Catch:{ all -> 0x03e4 }
    L_0x01db:
        r2 = new java.lang.RuntimeException;	 Catch:{ all -> 0x03e4 }
        goto L_0x02f0;
    L_0x01df:
        r2 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x03e4 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03e4 }
        r4.<init>();	 Catch:{ all -> 0x03e4 }
        r5 = "Unable to add window -- token ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = r3.token;	 Catch:{ all -> 0x03e4 }
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = " is not valid; is your activity running?";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r4 = r4.toString();	 Catch:{ all -> 0x03e4 }
        r2.<init>(r4);	 Catch:{ all -> 0x03e4 }
        throw r2;	 Catch:{ all -> 0x03e4 }
    L_0x01fd:
        r2 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x03e4 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03e4 }
        r4.<init>();	 Catch:{ all -> 0x03e4 }
        r5 = "Unable to add window -- token ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = r3.token;	 Catch:{ all -> 0x03e4 }
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = " is not for an application";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r4 = r4.toString();	 Catch:{ all -> 0x03e4 }
        r2.<init>(r4);	 Catch:{ all -> 0x03e4 }
        throw r2;	 Catch:{ all -> 0x03e4 }
    L_0x021b:
        r2 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x03e4 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03e4 }
        r4.<init>();	 Catch:{ all -> 0x03e4 }
        r5 = "Unable to add window -- app for token ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = r3.token;	 Catch:{ all -> 0x03e4 }
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = " is exiting";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r4 = r4.toString();	 Catch:{ all -> 0x03e4 }
        r2.<init>(r4);	 Catch:{ all -> 0x03e4 }
        throw r2;	 Catch:{ all -> 0x03e4 }
    L_0x0239:
        r2 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x03e4 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03e4 }
        r4.<init>();	 Catch:{ all -> 0x03e4 }
        r5 = "Unable to add window -- window ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = r1.mWindow;	 Catch:{ all -> 0x03e4 }
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = " has already been added";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r4 = r4.toString();	 Catch:{ all -> 0x03e4 }
        r2.<init>(r4);	 Catch:{ all -> 0x03e4 }
        throw r2;	 Catch:{ all -> 0x03e4 }
    L_0x0257:
        monitor-exit(r23);	 Catch:{ all -> 0x03e4 }
        return;
    L_0x0259:
        r2 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x03e4 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03e4 }
        r4.<init>();	 Catch:{ all -> 0x03e4 }
        r5 = "Unable to add window ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = r1.mWindow;	 Catch:{ all -> 0x03e4 }
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = " -- another window of type ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = r1.mWindowAttributes;	 Catch:{ all -> 0x03e4 }
        r5 = r5.type;	 Catch:{ all -> 0x03e4 }
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = " already exists";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r4 = r4.toString();	 Catch:{ all -> 0x03e4 }
        r2.<init>(r4);	 Catch:{ all -> 0x03e4 }
        throw r2;	 Catch:{ all -> 0x03e4 }
    L_0x0283:
        r2 = new android.view.WindowManager$BadTokenException;	 Catch:{ all -> 0x03e4 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03e4 }
        r4.<init>();	 Catch:{ all -> 0x03e4 }
        r5 = "Unable to add window ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = r1.mWindow;	 Catch:{ all -> 0x03e4 }
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = " -- permission denied for window type ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = r1.mWindowAttributes;	 Catch:{ all -> 0x03e4 }
        r5 = r5.type;	 Catch:{ all -> 0x03e4 }
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r4 = r4.toString();	 Catch:{ all -> 0x03e4 }
        r2.<init>(r4);	 Catch:{ all -> 0x03e4 }
        throw r2;	 Catch:{ all -> 0x03e4 }
    L_0x02a8:
        r2 = new android.view.WindowManager$InvalidDisplayException;	 Catch:{ all -> 0x03e4 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03e4 }
        r4.<init>();	 Catch:{ all -> 0x03e4 }
        r5 = "Unable to add window ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = r1.mWindow;	 Catch:{ all -> 0x03e4 }
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = " -- the specified display can not be found";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r4 = r4.toString();	 Catch:{ all -> 0x03e4 }
        r2.<init>(r4);	 Catch:{ all -> 0x03e4 }
        throw r2;	 Catch:{ all -> 0x03e4 }
    L_0x02c6:
        r2 = new android.view.WindowManager$InvalidDisplayException;	 Catch:{ all -> 0x03e4 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03e4 }
        r4.<init>();	 Catch:{ all -> 0x03e4 }
        r5 = "Unable to add window ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = r1.mWindow;	 Catch:{ all -> 0x03e4 }
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = " -- the specified window type ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = r1.mWindowAttributes;	 Catch:{ all -> 0x03e4 }
        r5 = r5.type;	 Catch:{ all -> 0x03e4 }
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r5 = " is not valid";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r4 = r4.toString();	 Catch:{ all -> 0x03e4 }
        r2.<init>(r4);	 Catch:{ all -> 0x03e4 }
        throw r2;	 Catch:{ all -> 0x03e4 }
    L_0x02f0:
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x03e4 }
        r4.<init>();	 Catch:{ all -> 0x03e4 }
        r5 = "Unable to add window -- unknown error code ";
        r4.append(r5);	 Catch:{ all -> 0x03e4 }
        r4.append(r0);	 Catch:{ all -> 0x03e4 }
        r4 = r4.toString();	 Catch:{ all -> 0x03e4 }
        r2.<init>(r4);	 Catch:{ all -> 0x03e4 }
        throw r2;	 Catch:{ all -> 0x03e4 }
    L_0x0305:
        r2 = r24;
        r4 = r2 instanceof com.android.internal.view.RootViewSurfaceTaker;	 Catch:{ all -> 0x042b }
        if (r4 == 0) goto L_0x0314;
    L_0x030b:
        r4 = r2;
        r4 = (com.android.internal.view.RootViewSurfaceTaker) r4;	 Catch:{ all -> 0x042b }
        r4 = r4.willYouTakeTheInputQueue();	 Catch:{ all -> 0x042b }
        r1.mInputQueueCallback = r4;	 Catch:{ all -> 0x042b }
    L_0x0314:
        r4 = r1.mWindowAttributes;	 Catch:{ all -> 0x042b }
        r4 = r4.inputFeatures;	 Catch:{ all -> 0x042b }
        r4 = r4 & 2;
        if (r4 == 0) goto L_0x031f;
    L_0x031c:
        r4 = 0;
        r1.mInputChannel = r4;	 Catch:{ all -> 0x042b }
    L_0x031f:
        r4 = r1.mInputChannel;	 Catch:{ all -> 0x042b }
        if (r4 == 0) goto L_0x0342;
    L_0x0323:
        r4 = r1.mInputQueueCallback;	 Catch:{ all -> 0x042b }
        if (r4 == 0) goto L_0x0335;
    L_0x0327:
        r4 = new android.view.InputQueue;	 Catch:{ all -> 0x042b }
        r4.<init>();	 Catch:{ all -> 0x042b }
        r1.mInputQueue = r4;	 Catch:{ all -> 0x042b }
        r4 = r1.mInputQueueCallback;	 Catch:{ all -> 0x042b }
        r5 = r1.mInputQueue;	 Catch:{ all -> 0x042b }
        r4.onInputQueueCreated(r5);	 Catch:{ all -> 0x042b }
    L_0x0335:
        r4 = new android.view.ViewRootImpl$WindowInputEventReceiver;	 Catch:{ all -> 0x042b }
        r5 = r1.mInputChannel;	 Catch:{ all -> 0x042b }
        r6 = android.os.Looper.myLooper();	 Catch:{ all -> 0x042b }
        r4.<init>(r5, r6);	 Catch:{ all -> 0x042b }
        r1.mInputEventReceiver = r4;	 Catch:{ all -> 0x042b }
    L_0x0342:
        r2.assignParent(r1);	 Catch:{ all -> 0x042b }
        r4 = r0 & 1;
        if (r4 == 0) goto L_0x034b;
    L_0x0349:
        r4 = 1;
        goto L_0x034c;
    L_0x034b:
        r4 = 0;
    L_0x034c:
        r1.mAddedTouchMode = r4;	 Catch:{ all -> 0x042b }
        r4 = r0 & 2;
        if (r4 == 0) goto L_0x0354;
    L_0x0352:
        r4 = 1;
        goto L_0x0355;
    L_0x0354:
        r4 = 0;
    L_0x0355:
        r1.mAppVisible = r4;	 Catch:{ all -> 0x042b }
        r4 = r1.mAccessibilityManager;	 Catch:{ all -> 0x042b }
        r4 = r4.isEnabled();	 Catch:{ all -> 0x042b }
        if (r4 == 0) goto L_0x0364;
    L_0x035f:
        r4 = r1.mAccessibilityInteractionConnectionManager;	 Catch:{ all -> 0x042b }
        r4.ensureConnection();	 Catch:{ all -> 0x042b }
    L_0x0364:
        r4 = r24.getImportantForAccessibility();	 Catch:{ all -> 0x042b }
        if (r4 != 0) goto L_0x036e;
    L_0x036a:
        r4 = 1;
        r2.setImportantForAccessibility(r4);	 Catch:{ all -> 0x042b }
    L_0x036e:
        r4 = r3.getTitle();	 Catch:{ all -> 0x042b }
        r5 = new android.view.ViewRootImpl$SyntheticInputStage;	 Catch:{ all -> 0x042b }
        r5.<init>();	 Catch:{ all -> 0x042b }
        r1.mSyntheticInputStage = r5;	 Catch:{ all -> 0x042b }
        r5 = new android.view.ViewRootImpl$ViewPostImeInputStage;	 Catch:{ all -> 0x042b }
        r6 = r1.mSyntheticInputStage;	 Catch:{ all -> 0x042b }
        r5.<init>(r6);	 Catch:{ all -> 0x042b }
        r6 = new android.view.ViewRootImpl$NativePostImeInputStage;	 Catch:{ all -> 0x042b }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x042b }
        r7.<init>();	 Catch:{ all -> 0x042b }
        r9 = "aq:native-post-ime:";
        r7.append(r9);	 Catch:{ all -> 0x042b }
        r7.append(r4);	 Catch:{ all -> 0x042b }
        r7 = r7.toString();	 Catch:{ all -> 0x042b }
        r6.<init>(r5, r7);	 Catch:{ all -> 0x042b }
        r7 = new android.view.ViewRootImpl$EarlyPostImeInputStage;	 Catch:{ all -> 0x042b }
        r7.<init>(r6);	 Catch:{ all -> 0x042b }
        r9 = new android.view.ViewRootImpl$ImeInputStage;	 Catch:{ all -> 0x042b }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x042b }
        r10.<init>();	 Catch:{ all -> 0x042b }
        r11 = "aq:ime:";
        r10.append(r11);	 Catch:{ all -> 0x042b }
        r10.append(r4);	 Catch:{ all -> 0x042b }
        r10 = r10.toString();	 Catch:{ all -> 0x042b }
        r9.<init>(r7, r10);	 Catch:{ all -> 0x042b }
        r10 = new android.view.ViewRootImpl$ViewPreImeInputStage;	 Catch:{ all -> 0x042b }
        r10.<init>(r9);	 Catch:{ all -> 0x042b }
        r11 = new android.view.ViewRootImpl$NativePreImeInputStage;	 Catch:{ all -> 0x042b }
        r12 = new java.lang.StringBuilder;	 Catch:{ all -> 0x042b }
        r12.<init>();	 Catch:{ all -> 0x042b }
        r13 = "aq:native-pre-ime:";
        r12.append(r13);	 Catch:{ all -> 0x042b }
        r12.append(r4);	 Catch:{ all -> 0x042b }
        r12 = r12.toString();	 Catch:{ all -> 0x042b }
        r11.<init>(r10, r12);	 Catch:{ all -> 0x042b }
        r1.mFirstInputStage = r11;	 Catch:{ all -> 0x042b }
        r1.mFirstPostImeInputStage = r7;	 Catch:{ all -> 0x042b }
        r12 = new java.lang.StringBuilder;	 Catch:{ all -> 0x042b }
        r12.<init>();	 Catch:{ all -> 0x042b }
        r13 = "aq:pending:";
        r12.append(r13);	 Catch:{ all -> 0x042b }
        r12.append(r4);	 Catch:{ all -> 0x042b }
        r12 = r12.toString();	 Catch:{ all -> 0x042b }
        r1.mPendingInputEventQueueLengthCounterName = r12;	 Catch:{ all -> 0x042b }
        goto L_0x0424;
    L_0x03e4:
        r0 = move-exception;
        r2 = r24;
        goto L_0x0429;
    L_0x03e8:
        r0 = move-exception;
        r2 = r24;
        goto L_0x0419;
    L_0x03ec:
        r0 = move-exception;
        r2 = r24;
        goto L_0x03f9;
    L_0x03f0:
        r0 = move-exception;
        goto L_0x03f9;
    L_0x03f2:
        r0 = move-exception;
        r22 = r7;
        goto L_0x0419;
    L_0x03f6:
        r0 = move-exception;
        r22 = r7;
    L_0x03f9:
        r4 = 0;
        r1.mAdded = r4;	 Catch:{ all -> 0x0418 }
        r4 = 0;
        r1.mView = r4;	 Catch:{ all -> 0x0418 }
        r5 = r1.mAttachInfo;	 Catch:{ all -> 0x0418 }
        r5.mRootView = r4;	 Catch:{ all -> 0x0418 }
        r1.mInputChannel = r4;	 Catch:{ all -> 0x0418 }
        r5 = r1.mFallbackEventHandler;	 Catch:{ all -> 0x0418 }
        r5.setView(r4);	 Catch:{ all -> 0x0418 }
        r23.unscheduleTraversals();	 Catch:{ all -> 0x0418 }
        r1.setAccessibilityFocus(r4, r4);	 Catch:{ all -> 0x0418 }
        r4 = new java.lang.RuntimeException;	 Catch:{ all -> 0x0418 }
        r5 = "Adding window failed";
        r4.<init>(r5, r0);	 Catch:{ all -> 0x0418 }
        throw r4;	 Catch:{ all -> 0x0418 }
    L_0x0418:
        r0 = move-exception;
    L_0x0419:
        if (r8 == 0) goto L_0x041e;
    L_0x041b:
        r3.restore();	 Catch:{ all -> 0x042b }
        throw r0;	 Catch:{ all -> 0x042b }
    L_0x0420:
        r0 = move-exception;
        goto L_0x0429;
    L_0x0422:
        r3 = r25;
    L_0x0424:
        monitor-exit(r23);	 Catch:{ all -> 0x042b }
        return;
    L_0x0426:
        r0 = move-exception;
        r3 = r25;
    L_0x0429:
        monitor-exit(r23);	 Catch:{ all -> 0x042b }
        throw r0;
    L_0x042b:
        r0 = move-exception;
        goto L_0x0429;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewRootImpl.setView(android.view.View, android.view.WindowManager$LayoutParams, android.view.View):void");
    }

    private void setTag() {
        String[] split = this.mWindowAttributes.getTitle().toString().split("\\.");
        if (split.length > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("ViewRootImpl[");
            stringBuilder.append(split[split.length - 1]);
            stringBuilder.append("]");
            this.mTag = stringBuilder.toString();
        }
    }

    private boolean isInLocalFocusMode() {
        return (this.mWindowAttributes.flags & 268435456) != 0;
    }

    @UnsupportedAppUsage
    public int getWindowFlags() {
        return this.mWindowAttributes.flags;
    }

    public int getDisplayId() {
        return this.mDisplay.getDisplayId();
    }

    public CharSequence getTitle() {
        return this.mWindowAttributes.getTitle();
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    /* Access modifiers changed, original: 0000 */
    public void destroyHardwareResources() {
        ThreadedRenderer renderer = this.mAttachInfo.mThreadedRenderer;
        if (renderer != null) {
            if (Looper.myLooper() != this.mAttachInfo.mHandler.getLooper()) {
                this.mAttachInfo.mHandler.postAtFrontOfQueue(new -$$Lambda$dj1hfDQd0iEp_uBDBPEUMMYJJwk(this));
                return;
            }
            destroyBlurControlLayer();
            renderer.destroyHardwareResources(this.mView);
            renderer.destroy();
        }
    }

    @UnsupportedAppUsage
    public void detachFunctor(long functor) {
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.stopDrawing();
        }
    }

    @UnsupportedAppUsage
    public static void invokeFunctor(long functor, boolean waitForCompletion) {
        HardwareRenderer.invokeFunctor(functor, waitForCompletion);
    }

    public void registerAnimatingRenderNode(RenderNode animator) {
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.registerAnimatingRenderNode(animator);
            return;
        }
        if (this.mAttachInfo.mPendingAnimatingRenderNodes == null) {
            this.mAttachInfo.mPendingAnimatingRenderNodes = new ArrayList();
        }
        this.mAttachInfo.mPendingAnimatingRenderNodes.add(animator);
    }

    public void registerVectorDrawableAnimator(NativeVectorDrawableAnimator animator) {
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.registerVectorDrawableAnimator(animator);
        }
    }

    public void registerRtFrameCallback(FrameDrawingCallback callback) {
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.registerRtFrameCallback(new -$$Lambda$ViewRootImpl$IReiNMSbDakZSGbIZuL_ifaFWn8(callback));
        }
    }

    static /* synthetic */ void lambda$registerRtFrameCallback$0(FrameDrawingCallback callback, long frame) {
        try {
            callback.onFrameDraw(frame);
        } catch (Exception e) {
            Log.e(TAG, "Exception while executing onFrameDraw", e);
        }
    }

    @UnsupportedAppUsage
    private void enableHardwareAcceleration(LayoutParams attrs) {
        AttachInfo attachInfo = this.mAttachInfo;
        boolean wideGamut = false;
        attachInfo.mHardwareAccelerated = false;
        attachInfo.mHardwareAccelerationRequested = false;
        if (this.mTranslator == null) {
            if (((attrs.flags & 16777216) != 0) && ThreadedRenderer.isAvailable()) {
                boolean fakeHwAccelerated = (attrs.privateFlags & 1) != 0;
                boolean forceHwAccelerated = (attrs.privateFlags & 2) != 0;
                if (fakeHwAccelerated) {
                    this.mAttachInfo.mHardwareAccelerationRequested = true;
                } else if (!ThreadedRenderer.sRendererDisabled || (ThreadedRenderer.sSystemRendererDisabled && forceHwAccelerated)) {
                    if (this.mAttachInfo.mThreadedRenderer != null) {
                        this.mAttachInfo.mThreadedRenderer.destroy();
                    }
                    Rect insets = attrs.surfaceInsets;
                    boolean hasSurfaceInsets = (insets.left == 0 && insets.right == 0 && insets.top == 0 && insets.bottom == 0) ? false : true;
                    boolean translucent = attrs.format != -1 || hasSurfaceInsets;
                    if (this.mContext.getResources().getConfiguration().isScreenWideColorGamut() && attrs.getColorMode() == 1) {
                        wideGamut = true;
                    }
                    this.mAttachInfo.mThreadedRenderer = ThreadedRenderer.create(this.mContext, translucent, attrs.getTitle().toString());
                    this.mAttachInfo.mThreadedRenderer.setWideGamut(wideGamut);
                    updateForceDarkMode();
                    if (this.mAttachInfo.mThreadedRenderer != null) {
                        AttachInfo attachInfo2 = this.mAttachInfo;
                        attachInfo2.mHardwareAccelerationRequested = true;
                        attachInfo2.mHardwareAccelerated = true;
                    }
                }
            }
        }
    }

    private int getNightMode() {
        return this.mContext.getResources().getConfiguration().uiMode & 48;
    }

    private void updateForceDarkMode() {
        if (this.mAttachInfo.mThreadedRenderer != null) {
            boolean z = true;
            boolean useAutoDark = getNightMode() == 32;
            if (useAutoDark) {
                boolean forceDarkAllowedDefault = SystemProperties.getBoolean(ThreadedRenderer.DEBUG_FORCE_DARK, false);
                TypedArray a = this.mContext.obtainStyledAttributes(R.styleable.Theme);
                if (!(a.getBoolean(279, true) && a.getBoolean(278, forceDarkAllowedDefault))) {
                    z = false;
                }
                useAutoDark = z;
                a.recycle();
            }
            if (this.mAttachInfo.mThreadedRenderer.setForceDark(useAutoDark)) {
                invalidateWorld(this.mView);
            }
        }
    }

    @UnsupportedAppUsage
    public View getView() {
        return this.mView;
    }

    /* Access modifiers changed, original: final */
    public final WindowLeaked getLocation() {
        return this.mLocation;
    }

    /* Access modifiers changed, original: 0000 */
    public void setLayoutParams(LayoutParams attrs, boolean newView) {
        synchronized (this) {
            int oldInsetLeft = this.mWindowAttributes.surfaceInsets.left;
            int oldInsetTop = this.mWindowAttributes.surfaceInsets.top;
            int oldInsetRight = this.mWindowAttributes.surfaceInsets.right;
            int oldInsetBottom = this.mWindowAttributes.surfaceInsets.bottom;
            int oldSoftInputMode = this.mWindowAttributes.softInputMode;
            boolean oldHasManualSurfaceInsets = this.mWindowAttributes.hasManualSurfaceInsets;
            this.mClientWindowLayoutFlags = attrs.flags;
            int compatibleWindowFlag = this.mWindowAttributes.privateFlags & 128;
            attrs.systemUiVisibility = this.mWindowAttributes.systemUiVisibility;
            attrs.subtreeSystemUiVisibility = this.mWindowAttributes.subtreeSystemUiVisibility;
            this.mWindowAttributesChangesFlag = this.mWindowAttributes.copyFrom(attrs);
            if ((this.mWindowAttributesChangesFlag & 524288) != 0) {
                this.mAttachInfo.mRecomputeGlobalAttributes = true;
            }
            if ((this.mWindowAttributesChangesFlag & 1) != 0) {
                this.mAttachInfo.mNeedsUpdateLightCenter = true;
            }
            if (this.mWindowAttributes.packageName == null) {
                this.mWindowAttributes.packageName = this.mBasePackageName;
            }
            LayoutParams layoutParams = this.mWindowAttributes;
            layoutParams.privateFlags |= compatibleWindowFlag;
            if (this.mWindowAttributes.preservePreviousSurfaceInsets) {
                this.mWindowAttributes.surfaceInsets.set(oldInsetLeft, oldInsetTop, oldInsetRight, oldInsetBottom);
                this.mWindowAttributes.hasManualSurfaceInsets = oldHasManualSurfaceInsets;
            } else if (!(this.mWindowAttributes.surfaceInsets.left == oldInsetLeft && this.mWindowAttributes.surfaceInsets.top == oldInsetTop && this.mWindowAttributes.surfaceInsets.right == oldInsetRight && this.mWindowAttributes.surfaceInsets.bottom == oldInsetBottom)) {
                this.mNeedsRendererSetup = true;
            }
            applyKeepScreenOnFlag(this.mWindowAttributes);
            if (newView) {
                this.mSoftInputMode = attrs.softInputMode;
                requestLayout();
            }
            if ((attrs.softInputMode & 240) == 0) {
                this.mWindowAttributes.softInputMode = (this.mWindowAttributes.softInputMode & TrafficStats.TAG_SYSTEM_IMPERSONATION_RANGE_END) | (oldSoftInputMode & 240);
            }
            this.mWindowAttributesChanged = true;
            scheduleTraversals();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void handleAppVisibility(boolean visible) {
        if (this.mAppVisible != visible) {
            this.mAppVisible = visible;
            this.mAppVisibilityChanged = true;
            scheduleTraversals();
            if (!this.mAppVisible) {
                WindowManagerGlobal.trimForeground();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void handleGetNewSurface() {
        this.mNewSurfaceNeeded = true;
        this.mFullRedrawNeeded = true;
        scheduleTraversals();
    }

    public void onMovedToDisplay(int displayId, Configuration config) {
        if (this.mDisplay.getDisplayId() != displayId) {
            updateInternalDisplay(displayId, this.mView.getResources());
            this.mAttachInfo.mDisplayState = this.mDisplay.getState();
            this.mView.dispatchMovedToDisplay(this.mDisplay, config);
        }
    }

    private void updateInternalDisplay(int displayId, Resources resources) {
        Display preferredDisplay = ResourcesManager.getInstance().getAdjustedDisplay(displayId, resources);
        if (preferredDisplay == null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot get desired display with Id: ");
            stringBuilder.append(displayId);
            Slog.w(TAG, stringBuilder.toString());
            this.mDisplay = ResourcesManager.getInstance().getAdjustedDisplay(0, resources);
        } else {
            this.mDisplay = preferredDisplay;
        }
        this.mContext.updateDisplay(this.mDisplay.getDisplayId());
    }

    /* Access modifiers changed, original: 0000 */
    public void pokeDrawLockIfNeeded() {
        int displayState = this.mAttachInfo.mDisplayState;
        if (this.mView == null || !this.mAdded || !this.mTraversalScheduled) {
            return;
        }
        if (displayState == 3 || displayState == 4) {
            try {
                this.mWindowSession.pokeDrawLock(this.mWindow);
            } catch (RemoteException e) {
            }
        }
    }

    public void requestFitSystemWindows() {
        checkThread();
        this.mApplyInsetsRequested = true;
        scheduleTraversals();
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyInsetsChanged() {
        if (sNewInsetsMode != 0) {
            this.mApplyInsetsRequested = true;
            if (!this.mIsInTraversal) {
                scheduleTraversals();
            }
        }
    }

    public void requestLayout() {
        if (!this.mHandlingLayoutInLayoutRequest) {
            checkThread();
            this.mLayoutRequested = true;
            scheduleTraversals();
        }
    }

    public boolean isLayoutRequested() {
        return this.mLayoutRequested;
    }

    public void onDescendantInvalidated(View child, View descendant) {
        if ((descendant.mPrivateFlags & 64) != 0) {
            this.mIsAnimating = true;
        }
        invalidate();
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void invalidate() {
        this.mDirty.set(0, 0, this.mWidth, this.mHeight);
        if (!this.mWillDrawSoon) {
            scheduleTraversals();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void invalidateWorld(View view) {
        view.invalidate();
        if (view instanceof ViewGroup) {
            ViewGroup parent = (ViewGroup) view;
            for (int i = 0; i < parent.getChildCount(); i++) {
                invalidateWorld(parent.getChildAt(i));
            }
        }
    }

    public void invalidateChild(View child, Rect dirty) {
        invalidateChildInParent(null, dirty);
    }

    public ViewParent invalidateChildInParent(int[] location, Rect dirty) {
        checkThread();
        if (dirty == null) {
            invalidate();
            return null;
        } else if (dirty.isEmpty() && !this.mIsAnimating) {
            return null;
        } else {
            if (!(this.mCurScrollY == 0 && this.mTranslator == null)) {
                this.mTempRect.set(dirty);
                dirty = this.mTempRect;
                int i = this.mCurScrollY;
                if (i != 0) {
                    dirty.offset(0, -i);
                }
                Translator translator = this.mTranslator;
                if (translator != null) {
                    translator.translateRectInAppWindowToScreen(dirty);
                }
                if (this.mAttachInfo.mScalingRequired) {
                    dirty.inset(-1, -1);
                }
            }
            invalidateRectOnScreen(dirty);
            return null;
        }
    }

    private void invalidateRectOnScreen(Rect dirty) {
        Rect localDirty = this.mDirty;
        localDirty.union(dirty.left, dirty.top, dirty.right, dirty.bottom);
        float appScale = this.mAttachInfo.mApplicationScale;
        boolean intersected = localDirty.intersect(0, 0, (int) ((((float) this.mWidth) * appScale) + 0.5f), (int) ((((float) this.mHeight) * appScale) + 0.5f));
        if (!intersected) {
            localDirty.setEmpty();
        }
        if (!this.mWillDrawSoon) {
            if (intersected || this.mIsAnimating) {
                scheduleTraversals();
            }
        }
    }

    public void setIsAmbientMode(boolean ambient) {
        this.mIsAmbientMode = ambient;
    }

    /* Access modifiers changed, original: 0000 */
    public void addWindowStoppedCallback(WindowStoppedCallback c) {
        this.mWindowStoppedCallbacks.add(c);
    }

    /* Access modifiers changed, original: 0000 */
    public void removeWindowStoppedCallback(WindowStoppedCallback c) {
        this.mWindowStoppedCallbacks.remove(c);
    }

    /* Access modifiers changed, original: 0000 */
    public void setWindowStopped(boolean stopped) {
        checkThread();
        if (this.mStopped != stopped) {
            this.mStopped = stopped;
            ThreadedRenderer renderer = this.mAttachInfo.mThreadedRenderer;
            if (renderer != null) {
                renderer.setStopped(this.mStopped);
            }
            if (!this.mStopped) {
                this.mNewSurfaceNeeded = true;
                scheduleTraversals();
            } else if (renderer != null) {
                renderer.destroyHardwareResources(this.mView);
            }
            for (int i = 0; i < this.mWindowStoppedCallbacks.size(); i++) {
                ((WindowStoppedCallback) this.mWindowStoppedCallbacks.get(i)).windowStopped(stopped);
            }
            if (this.mStopped) {
                if (this.mSurfaceHolder != null && this.mSurface.isValid()) {
                    notifySurfaceDestroyed();
                }
                destroySurface();
            }
        }
    }

    public void createBoundsSurface(int zOrderLayer) {
        if (this.mSurfaceSession == null) {
            this.mSurfaceSession = new SurfaceSession();
        }
        if (this.mBoundsSurfaceControl == null || !this.mBoundsSurface.isValid()) {
            Builder builder = new Builder(this.mSurfaceSession);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Bounds for - ");
            stringBuilder.append(getTitle().toString());
            this.mBoundsSurfaceControl = builder.setName(stringBuilder.toString()).setParent(this.mSurfaceControl).build();
            setBoundsSurfaceCrop();
            this.mTransaction.setLayer(this.mBoundsSurfaceControl, zOrderLayer).show(this.mBoundsSurfaceControl).apply();
            this.mBoundsSurface.copyFrom(this.mBoundsSurfaceControl);
        }
    }

    private void setBoundsSurfaceCrop() {
        this.mTempBoundsRect.set(this.mWinFrame);
        this.mTempBoundsRect.offsetTo(this.mWindowAttributes.surfaceInsets.left, this.mWindowAttributes.surfaceInsets.top);
        this.mTransaction.setWindowCrop(this.mBoundsSurfaceControl, this.mTempBoundsRect);
    }

    private void updateBoundsSurface() {
        if (this.mBoundsSurfaceControl != null && this.mSurface.isValid()) {
            setBoundsSurfaceCrop();
            Transaction transaction = this.mTransaction;
            SurfaceControl surfaceControl = this.mBoundsSurfaceControl;
            Surface surface = this.mSurface;
            transaction.deferTransactionUntilSurface(surfaceControl, surface, surface.getNextFrameNumber()).apply();
        }
    }

    private void destroySurface() {
        this.mSurface.release();
        this.mSurfaceControl.release();
        this.mSurfaceSession = null;
        SurfaceControl surfaceControl = this.mBoundsSurfaceControl;
        if (surfaceControl != null) {
            surfaceControl.remove();
            this.mBoundsSurface.release();
            this.mBoundsSurfaceControl = null;
        }
    }

    public void setPausedForTransition(boolean paused) {
        this.mPausedForTransition = paused;
    }

    public ViewParent getParent() {
        return null;
    }

    public boolean getChildVisibleRect(View child, Rect r, Point offset) {
        if (child == this.mView) {
            return r.intersect(0, 0, this.mWidth, this.mHeight);
        }
        throw new RuntimeException("child is not mine, honest!");
    }

    public void bringChildToFront(View child) {
    }

    /* Access modifiers changed, original: 0000 */
    public int getHostVisibility() {
        return (this.mAppVisible || this.mForceDecorViewVisibility) ? this.mView.getVisibility() : 8;
    }

    public void requestTransitionStart(LayoutTransition transition) {
        ArrayList arrayList = this.mPendingTransitions;
        if (arrayList == null || !arrayList.contains(transition)) {
            if (this.mPendingTransitions == null) {
                this.mPendingTransitions = new ArrayList();
            }
            this.mPendingTransitions.add(transition);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void notifyRendererOfFramePending() {
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.notifyFramePending();
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void scheduleTraversals() {
        if (!this.mTraversalScheduled) {
            this.mTraversalScheduled = true;
            this.mTraversalBarrier = this.mHandler.getLooper().getQueue().postSyncBarrier();
            this.mChoreographer.postCallback(3, this.mTraversalRunnable, null);
            if (!this.mUnbufferedInputDispatch) {
                scheduleConsumeBatchedInput();
            }
            notifyRendererOfFramePending();
            pokeDrawLockIfNeeded();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void unscheduleTraversals() {
        if (this.mTraversalScheduled) {
            this.mTraversalScheduled = false;
            this.mHandler.getLooper().getQueue().removeSyncBarrier(this.mTraversalBarrier);
            this.mChoreographer.removeCallbacks(3, this.mTraversalRunnable, null);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doTraversal() {
        if (this.mTraversalScheduled) {
            this.mTraversalScheduled = false;
            this.mHandler.getLooper().getQueue().removeSyncBarrier(this.mTraversalBarrier);
            if (this.mProfile) {
                Debug.startMethodTracing("ViewAncestor");
            }
            performTraversals();
            if (this.mProfile) {
                Debug.stopMethodTracing();
                this.mProfile = false;
            }
        }
    }

    private void applyKeepScreenOnFlag(LayoutParams params) {
        if (this.mAttachInfo.mKeepScreenOn) {
            params.flags |= 128;
        } else {
            params.flags = (params.flags & -129) | (this.mClientWindowLayoutFlags & 128);
        }
    }

    private boolean collectViewAttributes() {
        if (this.mAttachInfo.mRecomputeGlobalAttributes) {
            boolean oldScreenOn = this.mAttachInfo;
            oldScreenOn.mRecomputeGlobalAttributes = false;
            oldScreenOn = oldScreenOn.mKeepScreenOn;
            AttachInfo attachInfo = this.mAttachInfo;
            attachInfo.mKeepScreenOn = false;
            attachInfo.mSystemUiVisibility = 0;
            attachInfo.mHasSystemUiListeners = false;
            this.mView.dispatchCollectViewAttributes(attachInfo, 0);
            attachInfo = this.mAttachInfo;
            attachInfo.mSystemUiVisibility &= ~this.mAttachInfo.mDisabledSystemUiVisibility;
            LayoutParams params = this.mWindowAttributes;
            AttachInfo attachInfo2 = this.mAttachInfo;
            attachInfo2.mSystemUiVisibility |= getImpliedSystemUiVisibility(params);
            if (!(this.mAttachInfo.mKeepScreenOn == oldScreenOn && this.mAttachInfo.mSystemUiVisibility == params.subtreeSystemUiVisibility && this.mAttachInfo.mHasSystemUiListeners == params.hasSystemUiListeners)) {
                applyKeepScreenOnFlag(params);
                params.subtreeSystemUiVisibility = this.mAttachInfo.mSystemUiVisibility;
                params.hasSystemUiListeners = this.mAttachInfo.mHasSystemUiListeners;
                this.mView.dispatchWindowSystemUiVisiblityChanged(this.mAttachInfo.mSystemUiVisibility);
                return true;
            }
        }
        return false;
    }

    private int getImpliedSystemUiVisibility(LayoutParams params) {
        int vis = 0;
        if ((params.flags & 67108864) != 0) {
            vis = 0 | 1280;
        }
        if ((params.flags & 134217728) != 0) {
            return vis | 768;
        }
        return vis;
    }

    private boolean measureHierarchy(View host, LayoutParams lp, Resources res, int desiredWindowWidth, int desiredWindowHeight) {
        boolean goodMeasure = false;
        if (lp.width == -2) {
            DisplayMetrics packageMetrics = res.getDisplayMetrics();
            res.getValue((int) R.dimen.config_prefDialogWidth, this.mTmpValue, true);
            int baseSize = 0;
            if (this.mTmpValue.type == 5) {
                baseSize = (int) this.mTmpValue.getDimension(packageMetrics);
            }
            if (baseSize != 0 && desiredWindowWidth > baseSize) {
                int childWidthMeasureSpec = getRootMeasureSpec(baseSize, lp.width);
                int childHeightMeasureSpec = getRootMeasureSpec(desiredWindowHeight, lp.height);
                performMeasure(childWidthMeasureSpec, childHeightMeasureSpec);
                if ((host.getMeasuredWidthAndState() & 16777216) == 0) {
                    goodMeasure = true;
                } else {
                    performMeasure(getRootMeasureSpec((baseSize + desiredWindowWidth) / 2, lp.width), childHeightMeasureSpec);
                    if ((host.getMeasuredWidthAndState() & 16777216) == 0) {
                        goodMeasure = true;
                    }
                }
            }
        }
        if (goodMeasure) {
            return false;
        }
        performMeasure(getRootMeasureSpec(desiredWindowWidth, lp.width), getRootMeasureSpec(desiredWindowHeight, lp.height));
        if (this.mWidth == host.getMeasuredWidth() && this.mHeight == host.getMeasuredHeight()) {
            return false;
        }
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void transformMatrixToGlobal(Matrix m) {
        m.preTranslate((float) this.mAttachInfo.mWindowLeft, (float) this.mAttachInfo.mWindowTop);
    }

    /* Access modifiers changed, original: 0000 */
    public void transformMatrixToLocal(Matrix m) {
        m.postTranslate((float) (-this.mAttachInfo.mWindowLeft), (float) (-this.mAttachInfo.mWindowTop));
    }

    /* Access modifiers changed, original: 0000 */
    public WindowInsets getWindowInsets(boolean forceConstruct) {
        if (this.mLastWindowInsets == null || forceConstruct) {
            DisplayCutout displayCutout;
            this.mDispatchContentInsets.set(this.mAttachInfo.mContentInsets);
            this.mDispatchStableInsets.set(this.mAttachInfo.mStableInsets);
            this.mDispatchDisplayCutout = this.mAttachInfo.mDisplayCutout.get();
            Rect contentInsets = this.mDispatchContentInsets;
            Rect stableInsets = this.mDispatchStableInsets;
            DisplayCutout displayCutout2 = this.mDispatchDisplayCutout;
            if (forceConstruct || (this.mPendingContentInsets.equals(contentInsets) && this.mPendingStableInsets.equals(stableInsets) && this.mPendingDisplayCutout.get().equals(displayCutout2))) {
                displayCutout = displayCutout2;
            } else {
                contentInsets = this.mPendingContentInsets;
                stableInsets = this.mPendingStableInsets;
                displayCutout = this.mPendingDisplayCutout.get();
            }
            Rect outsets = this.mAttachInfo.mOutsets;
            if (outsets.left > 0 || outsets.top > 0 || outsets.right > 0 || outsets.bottom > 0) {
                contentInsets = new Rect(contentInsets.left + outsets.left, contentInsets.top + outsets.top, contentInsets.right + outsets.right, contentInsets.bottom + outsets.bottom);
            }
            contentInsets = ensureInsetsNonNegative(contentInsets, "content");
            stableInsets = ensureInsetsNonNegative(stableInsets, "stable");
            this.mLastWindowInsets = this.mInsetsController.calculateInsets(this.mContext.getResources().getConfiguration().isScreenRound(), this.mAttachInfo.mAlwaysConsumeSystemBars, displayCutout, contentInsets, stableInsets, this.mWindowAttributes.softInputMode);
        }
        return this.mLastWindowInsets;
    }

    private Rect ensureInsetsNonNegative(Rect insets, String kind) {
        if (insets.left >= 0 && insets.top >= 0 && insets.right >= 0 && insets.bottom >= 0) {
            return insets;
        }
        String str = this.mTag;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Negative ");
        stringBuilder.append(kind);
        stringBuilder.append("Insets: ");
        stringBuilder.append(insets);
        stringBuilder.append(", mFirst=");
        stringBuilder.append(this.mFirst);
        Log.wtf(str, stringBuilder.toString());
        return new Rect(Math.max(0, insets.left), Math.max(0, insets.top), Math.max(0, insets.right), Math.max(0, insets.bottom));
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchApplyInsets(View host) {
        Trace.traceBegin(8, "dispatchApplyInsets");
        boolean dispatchCutout = true;
        WindowInsets insets = getWindowInsets(true);
        if (this.mWindowAttributes.layoutInDisplayCutoutMode != 1) {
            dispatchCutout = false;
        }
        if (!dispatchCutout) {
            insets = insets.consumeDisplayCutout();
        }
        host.dispatchApplyWindowInsets(insets);
        Trace.traceEnd(8);
    }

    /* Access modifiers changed, original: 0000 */
    public InsetsController getInsetsController() {
        return this.mInsetsController;
    }

    private static boolean shouldUseDisplaySize(LayoutParams lp) {
        return lp.type == 2014 || lp.type == 2011 || lp.type == 2020;
    }

    private int dipToPx(int dip) {
        return (int) ((this.mContext.getResources().getDisplayMetrics().density * ((float) dip)) + 0.5f);
    }

    /* JADX WARNING: Removed duplicated region for block: B:420:0x0686 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:419:0x0683 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x02d8  */
    /* JADX WARNING: Removed duplicated region for block: B:195:0x0349  */
    /* JADX WARNING: Removed duplicated region for block: B:194:0x0347  */
    /* JADX WARNING: Removed duplicated region for block: B:218:0x039d  */
    /* JADX WARNING: Removed duplicated region for block: B:211:0x0390  */
    /* JADX WARNING: Removed duplicated region for block: B:222:0x03ae  */
    /* JADX WARNING: Removed duplicated region for block: B:221:0x03a3  */
    /* JADX WARNING: Removed duplicated region for block: B:250:0x0426  */
    /* JADX WARNING: Removed duplicated region for block: B:227:0x03bf A:{SYNTHETIC, Splitter:B:227:0x03bf} */
    /* JADX WARNING: Removed duplicated region for block: B:257:0x043b A:{SYNTHETIC, Splitter:B:257:0x043b} */
    /* JADX WARNING: Removed duplicated region for block: B:270:0x0466 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:269:0x0464 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:274:0x0477 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:273:0x0475 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:278:0x0488 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:277:0x0486 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:282:0x0499 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:281:0x0497 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:286:0x04aa A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:285:0x04a8 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:290:0x04c1 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:289:0x04bf A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:294:0x04d0 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:293:0x04ce A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:298:0x04df A:{SYNTHETIC, Splitter:B:298:0x04df} */
    /* JADX WARNING: Removed duplicated region for block: B:301:0x04ea A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:303:0x04f8 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:305:0x0506 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:307:0x0514 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:320:0x0555 A:{SYNTHETIC, Splitter:B:320:0x0555} */
    /* JADX WARNING: Removed duplicated region for block: B:328:0x0573 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:327:0x0571 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:352:0x05c8 A:{SYNTHETIC, Splitter:B:352:0x05c8} */
    /* JADX WARNING: Removed duplicated region for block: B:331:0x0579 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:391:0x063e  */
    /* JADX WARNING: Removed duplicated region for block: B:390:0x063c  */
    /* JADX WARNING: Removed duplicated region for block: B:395:0x0645  */
    /* JADX WARNING: Removed duplicated region for block: B:394:0x0643  */
    /* JADX WARNING: Removed duplicated region for block: B:436:0x06cd A:{Catch:{ RemoteException -> 0x06f9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:405:0x0655 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:439:0x06de A:{Catch:{ RemoteException -> 0x06f9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0282  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x02d2  */
    /* JADX WARNING: Removed duplicated region for block: B:152:0x02a5  */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x02d8  */
    /* JADX WARNING: Removed duplicated region for block: B:161:0x02dd A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x031a  */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x0330  */
    /* JADX WARNING: Removed duplicated region for block: B:194:0x0347  */
    /* JADX WARNING: Removed duplicated region for block: B:195:0x0349  */
    /* JADX WARNING: Removed duplicated region for block: B:198:0x0353 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:211:0x0390  */
    /* JADX WARNING: Removed duplicated region for block: B:218:0x039d  */
    /* JADX WARNING: Removed duplicated region for block: B:221:0x03a3  */
    /* JADX WARNING: Removed duplicated region for block: B:222:0x03ae  */
    /* JADX WARNING: Removed duplicated region for block: B:227:0x03bf A:{SYNTHETIC, Splitter:B:227:0x03bf} */
    /* JADX WARNING: Removed duplicated region for block: B:250:0x0426  */
    /* JADX WARNING: Removed duplicated region for block: B:257:0x043b A:{SYNTHETIC, Splitter:B:257:0x043b} */
    /* JADX WARNING: Removed duplicated region for block: B:269:0x0464 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:270:0x0466 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:273:0x0475 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:274:0x0477 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:277:0x0486 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:278:0x0488 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:281:0x0497 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:282:0x0499 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:285:0x04a8 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:286:0x04aa A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:289:0x04bf A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:290:0x04c1 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:293:0x04ce A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:294:0x04d0 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:298:0x04df A:{SYNTHETIC, Splitter:B:298:0x04df} */
    /* JADX WARNING: Removed duplicated region for block: B:301:0x04ea A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:303:0x04f8 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:305:0x0506 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:307:0x0514 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:320:0x0555 A:{SYNTHETIC, Splitter:B:320:0x0555} */
    /* JADX WARNING: Removed duplicated region for block: B:327:0x0571 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:328:0x0573 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:331:0x0579 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:352:0x05c8 A:{SYNTHETIC, Splitter:B:352:0x05c8} */
    /* JADX WARNING: Removed duplicated region for block: B:390:0x063c  */
    /* JADX WARNING: Removed duplicated region for block: B:391:0x063e  */
    /* JADX WARNING: Removed duplicated region for block: B:394:0x0643  */
    /* JADX WARNING: Removed duplicated region for block: B:395:0x0645  */
    /* JADX WARNING: Removed duplicated region for block: B:398:0x064a A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:405:0x0655 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:436:0x06cd A:{Catch:{ RemoteException -> 0x06f9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:439:0x06de A:{Catch:{ RemoteException -> 0x06f9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0064  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00e7  */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0089  */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x00fe  */
    /* JADX WARNING: Removed duplicated region for block: B:69:0x0127  */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x0217  */
    /* JADX WARNING: Removed duplicated region for block: B:80:0x014a  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x0226  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x022e  */
    /* JADX WARNING: Removed duplicated region for block: B:127:0x024a  */
    /* JADX WARNING: Removed duplicated region for block: B:140:0x0282  */
    /* JADX WARNING: Removed duplicated region for block: B:152:0x02a5  */
    /* JADX WARNING: Removed duplicated region for block: B:156:0x02d2  */
    /* JADX WARNING: Removed duplicated region for block: B:159:0x02d8  */
    /* JADX WARNING: Removed duplicated region for block: B:161:0x02dd A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:182:0x031a  */
    /* JADX WARNING: Removed duplicated region for block: B:188:0x0330  */
    /* JADX WARNING: Removed duplicated region for block: B:195:0x0349  */
    /* JADX WARNING: Removed duplicated region for block: B:194:0x0347  */
    /* JADX WARNING: Removed duplicated region for block: B:198:0x0353 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:218:0x039d  */
    /* JADX WARNING: Removed duplicated region for block: B:211:0x0390  */
    /* JADX WARNING: Removed duplicated region for block: B:222:0x03ae  */
    /* JADX WARNING: Removed duplicated region for block: B:221:0x03a3  */
    /* JADX WARNING: Removed duplicated region for block: B:250:0x0426  */
    /* JADX WARNING: Removed duplicated region for block: B:227:0x03bf A:{SYNTHETIC, Splitter:B:227:0x03bf} */
    /* JADX WARNING: Removed duplicated region for block: B:257:0x043b A:{SYNTHETIC, Splitter:B:257:0x043b} */
    /* JADX WARNING: Removed duplicated region for block: B:270:0x0466 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:269:0x0464 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:274:0x0477 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:273:0x0475 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:278:0x0488 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:277:0x0486 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:282:0x0499 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:281:0x0497 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:286:0x04aa A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:285:0x04a8 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:290:0x04c1 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:289:0x04bf A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:294:0x04d0 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:293:0x04ce A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:298:0x04df A:{SYNTHETIC, Splitter:B:298:0x04df} */
    /* JADX WARNING: Removed duplicated region for block: B:301:0x04ea A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:303:0x04f8 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:305:0x0506 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:307:0x0514 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:320:0x0555 A:{SYNTHETIC, Splitter:B:320:0x0555} */
    /* JADX WARNING: Removed duplicated region for block: B:328:0x0573 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:327:0x0571 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:352:0x05c8 A:{SYNTHETIC, Splitter:B:352:0x05c8} */
    /* JADX WARNING: Removed duplicated region for block: B:331:0x0579 A:{Catch:{ RemoteException -> 0x044c }} */
    /* JADX WARNING: Removed duplicated region for block: B:391:0x063e  */
    /* JADX WARNING: Removed duplicated region for block: B:390:0x063c  */
    /* JADX WARNING: Removed duplicated region for block: B:395:0x0645  */
    /* JADX WARNING: Removed duplicated region for block: B:394:0x0643  */
    /* JADX WARNING: Removed duplicated region for block: B:398:0x064a A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:436:0x06cd A:{Catch:{ RemoteException -> 0x06f9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:405:0x0655 A:{Catch:{ RemoteException -> 0x06fb }} */
    /* JADX WARNING: Removed duplicated region for block: B:439:0x06de A:{Catch:{ RemoteException -> 0x06f9 }} */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:491:0x081a  */
    /* JADX WARNING: Removed duplicated region for block: B:458:0x0753  */
    /* JADX WARNING: Removed duplicated region for block: B:511:0x085e  */
    /* JADX WARNING: Removed duplicated region for block: B:510:0x085c  */
    /* JADX WARNING: Removed duplicated region for block: B:523:0x08a6  */
    /* JADX WARNING: Removed duplicated region for block: B:526:0x08bd  */
    /* JADX WARNING: Removed duplicated region for block: B:528:0x08d0  */
    /* JADX WARNING: Removed duplicated region for block: B:532:0x08de  */
    /* JADX WARNING: Removed duplicated region for block: B:534:0x08e3  */
    /* JADX WARNING: Removed duplicated region for block: B:542:0x08f1  */
    /* JADX WARNING: Removed duplicated region for block: B:560:0x096b  */
    /* JADX WARNING: Removed duplicated region for block: B:548:0x08fe  */
    /* JADX WARNING: Removed duplicated region for block: B:562:0x0973  */
    /* JADX WARNING: Removed duplicated region for block: B:581:0x09ef  */
    /* JADX WARNING: Removed duplicated region for block: B:564:0x097f  */
    /* JADX WARNING: Removed duplicated region for block: B:584:0x09f5  */
    /* JADX WARNING: Removed duplicated region for block: B:599:0x0a2a  */
    /* JADX WARNING: Removed duplicated region for block: B:606:0x0a39 A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:610:0x0a40  */
    /* JADX WARNING: Removed duplicated region for block: B:616:0x0a4d A:{SKIP} */
    /* JADX WARNING: Removed duplicated region for block: B:615:0x0a49  */
    /* JADX WARNING: Removed duplicated region for block: B:625:0x0a60  */
    /* JADX WARNING: Removed duplicated region for block: B:624:0x0a5e  */
    /* JADX WARNING: Removed duplicated region for block: B:630:0x0a6c  */
    /* JADX WARNING: Removed duplicated region for block: B:633:0x0a80  */
    /* JADX WARNING: Removed duplicated region for block: B:646:0x0adb  */
    /* JADX WARNING: Removed duplicated region for block: B:663:0x0b1b  */
    /* JADX WARNING: Removed duplicated region for block: B:653:0x0af1  */
    private void performTraversals() {
        /*
        r47 = this;
        r7 = r47;
        r8 = r7.mView;
        if (r8 == 0) goto L_0x0b4b;
    L_0x0006:
        r0 = r7.mAdded;
        if (r0 != 0) goto L_0x000c;
    L_0x000a:
        goto L_0x0b4b;
    L_0x000c:
        r9 = 1;
        r7.mIsInTraversal = r9;
        r7.mWillDrawSoon = r9;
        r0 = 0;
        r1 = 0;
        r10 = r7.mWindowAttributes;
        r11 = r47.getHostVisibility();
        r2 = r7.mFirst;
        r12 = 0;
        if (r2 != 0) goto L_0x002c;
    L_0x001e:
        r2 = r7.mViewVisibility;
        if (r2 != r11) goto L_0x002a;
    L_0x0022:
        r2 = r7.mNewSurfaceNeeded;
        if (r2 != 0) goto L_0x002a;
    L_0x0026:
        r2 = r7.mAppVisibilityChanged;
        if (r2 == 0) goto L_0x002c;
    L_0x002a:
        r2 = r9;
        goto L_0x002d;
    L_0x002c:
        r2 = r12;
    L_0x002d:
        r13 = r2;
        r7.mAppVisibilityChanged = r12;
        r2 = r7.mFirst;
        if (r2 != 0) goto L_0x0044;
    L_0x0034:
        r2 = r7.mViewVisibility;
        if (r2 != 0) goto L_0x003a;
    L_0x0038:
        r2 = r9;
        goto L_0x003b;
    L_0x003a:
        r2 = r12;
    L_0x003b:
        if (r11 != 0) goto L_0x003f;
    L_0x003d:
        r3 = r9;
        goto L_0x0040;
    L_0x003f:
        r3 = r12;
    L_0x0040:
        if (r2 == r3) goto L_0x0044;
    L_0x0042:
        r2 = r9;
        goto L_0x0045;
    L_0x0044:
        r2 = r12;
    L_0x0045:
        r14 = r2;
        r2 = 0;
        r3 = r7.mWindowAttributesChanged;
        if (r3 == 0) goto L_0x0051;
    L_0x004b:
        r7.mWindowAttributesChanged = r12;
        r1 = 1;
        r2 = r10;
        r15 = r1;
        goto L_0x0052;
    L_0x0051:
        r15 = r1;
    L_0x0052:
        r1 = r7.mDisplay;
        r1 = r1.getDisplayAdjustments();
        r16 = r1.getCompatibilityInfo();
        r1 = r16.supportsScreen();
        r3 = r7.mLastInCompatMode;
        if (r1 != r3) goto L_0x007f;
    L_0x0064:
        r2 = r10;
        r7.mFullRedrawNeeded = r9;
        r7.mLayoutRequested = r9;
        if (r3 == 0) goto L_0x0074;
    L_0x006b:
        r1 = r2.privateFlags;
        r1 = r1 & -129;
        r2.privateFlags = r1;
        r7.mLastInCompatMode = r12;
        goto L_0x007c;
    L_0x0074:
        r1 = r2.privateFlags;
        r1 = r1 | 128;
        r2.privateFlags = r1;
        r7.mLastInCompatMode = r9;
    L_0x007c:
        r17 = r2;
        goto L_0x0081;
    L_0x007f:
        r17 = r2;
    L_0x0081:
        r7.mWindowAttributesChangesFlag = r12;
        r6 = r7.mWinFrame;
        r1 = r7.mFirst;
        if (r1 == 0) goto L_0x00e7;
    L_0x0089:
        r7.mFullRedrawNeeded = r9;
        r7.mLayoutRequested = r9;
        r1 = r7.mContext;
        r1 = r1.getResources();
        r1 = r1.getConfiguration();
        r2 = shouldUseDisplaySize(r10);
        if (r2 == 0) goto L_0x00ac;
    L_0x009d:
        r2 = new android.graphics.Point;
        r2.<init>();
        r3 = r7.mDisplay;
        r3.getRealSize(r2);
        r3 = r2.x;
        r2 = r2.y;
        goto L_0x00b8;
    L_0x00ac:
        r2 = r7.mWinFrame;
        r3 = r2.width();
        r2 = r7.mWinFrame;
        r2 = r2.height();
    L_0x00b8:
        r4 = r7.mAttachInfo;
        r4.mUse32BitDrawingCache = r9;
        r4.mWindowVisibility = r11;
        r4.mRecomputeGlobalAttributes = r12;
        r4 = r7.mLastConfigurationFromResources;
        r4.setTo(r1);
        r4 = r7.mAttachInfo;
        r4 = r4.mSystemUiVisibility;
        r7.mLastSystemUiVisibility = r4;
        r4 = r7.mViewLayoutDirectionInitial;
        r5 = 2;
        if (r4 != r5) goto L_0x00d7;
    L_0x00d0:
        r4 = r1.getLayoutDirection();
        r8.setLayoutDirection(r4);
    L_0x00d7:
        r4 = r7.mAttachInfo;
        r8.dispatchAttachedToWindow(r4, r12);
        r4 = r7.mAttachInfo;
        r4 = r4.mTreeObserver;
        r4.dispatchOnWindowAttachedChange(r9);
        r7.dispatchApplyInsets(r8);
        goto L_0x00fc;
    L_0x00e7:
        r3 = r6.width();
        r2 = r6.height();
        r1 = r7.mWidth;
        if (r3 != r1) goto L_0x00f7;
    L_0x00f3:
        r1 = r7.mHeight;
        if (r2 == r1) goto L_0x00fc;
    L_0x00f7:
        r7.mFullRedrawNeeded = r9;
        r7.mLayoutRequested = r9;
        r0 = 1;
    L_0x00fc:
        if (r13 == 0) goto L_0x0121;
    L_0x00fe:
        r1 = r7.mAttachInfo;
        r1.mWindowVisibility = r11;
        r8.dispatchWindowVisibilityChanged(r11);
        if (r14 == 0) goto L_0x010f;
    L_0x0107:
        if (r11 != 0) goto L_0x010b;
    L_0x0109:
        r1 = r9;
        goto L_0x010c;
    L_0x010b:
        r1 = r12;
    L_0x010c:
        r8.dispatchVisibilityAggregated(r1);
    L_0x010f:
        if (r11 != 0) goto L_0x0115;
    L_0x0111:
        r1 = r7.mNewSurfaceNeeded;
        if (r1 == 0) goto L_0x011b;
    L_0x0115:
        r47.endDragResizing();
        r47.destroyHardwareResources();
    L_0x011b:
        r1 = 8;
        if (r11 != r1) goto L_0x0121;
    L_0x011f:
        r7.mHasHadWindowFocus = r12;
    L_0x0121:
        r1 = r7.mAttachInfo;
        r1 = r1.mWindowVisibility;
        if (r1 == 0) goto L_0x012a;
    L_0x0127:
        r8.clearAccessibilityFocus();
    L_0x012a:
        r1 = getRunQueue();
        r4 = r7.mAttachInfo;
        r4 = r4.mHandler;
        r1.executeActions(r4);
        r1 = 0;
        r4 = r7.mLayoutRequested;
        if (r4 == 0) goto L_0x0144;
    L_0x013a:
        r4 = r7.mStopped;
        if (r4 == 0) goto L_0x0142;
    L_0x013e:
        r4 = r7.mReportNextDraw;
        if (r4 == 0) goto L_0x0144;
    L_0x0142:
        r4 = r9;
        goto L_0x0145;
    L_0x0144:
        r4 = r12;
    L_0x0145:
        r18 = r4;
        r5 = -2;
        if (r18 == 0) goto L_0x0217;
    L_0x014a:
        r4 = r7.mView;
        r4 = r4.getContext();
        r19 = r4.getResources();
        r4 = r7.mFirst;
        if (r4 == 0) goto L_0x0169;
    L_0x0158:
        r4 = r7.mAttachInfo;
        r9 = r7.mAddedTouchMode;
        r12 = r9 ^ 1;
        r4.mInTouchMode = r12;
        r7.ensureTouchModeLocally(r9);
        r21 = r1;
        r12 = r2;
        r9 = r3;
        goto L_0x0204;
    L_0x0169:
        r4 = r7.mPendingOverscanInsets;
        r9 = r7.mAttachInfo;
        r9 = r9.mOverscanInsets;
        r4 = r4.equals(r9);
        if (r4 != 0) goto L_0x0176;
    L_0x0175:
        r1 = 1;
    L_0x0176:
        r4 = r7.mPendingContentInsets;
        r9 = r7.mAttachInfo;
        r9 = r9.mContentInsets;
        r4 = r4.equals(r9);
        if (r4 != 0) goto L_0x0183;
    L_0x0182:
        r1 = 1;
    L_0x0183:
        r4 = r7.mPendingStableInsets;
        r9 = r7.mAttachInfo;
        r9 = r9.mStableInsets;
        r4 = r4.equals(r9);
        if (r4 != 0) goto L_0x0190;
    L_0x018f:
        r1 = 1;
    L_0x0190:
        r4 = r7.mPendingDisplayCutout;
        r9 = r7.mAttachInfo;
        r9 = r9.mDisplayCutout;
        r4 = r4.equals(r9);
        if (r4 != 0) goto L_0x019d;
    L_0x019c:
        r1 = 1;
    L_0x019d:
        r4 = r7.mPendingVisibleInsets;
        r9 = r7.mAttachInfo;
        r9 = r9.mVisibleInsets;
        r4 = r4.equals(r9);
        if (r4 != 0) goto L_0x01b2;
    L_0x01a9:
        r4 = r7.mAttachInfo;
        r4 = r4.mVisibleInsets;
        r9 = r7.mPendingVisibleInsets;
        r4.set(r9);
    L_0x01b2:
        r4 = r7.mPendingOutsets;
        r9 = r7.mAttachInfo;
        r9 = r9.mOutsets;
        r4 = r4.equals(r9);
        if (r4 != 0) goto L_0x01bf;
    L_0x01be:
        r1 = 1;
    L_0x01bf:
        r4 = r7.mPendingAlwaysConsumeSystemBars;
        r9 = r7.mAttachInfo;
        r9 = r9.mAlwaysConsumeSystemBars;
        if (r4 == r9) goto L_0x01c8;
    L_0x01c7:
        r1 = 1;
    L_0x01c8:
        r4 = r10.width;
        if (r4 == r5) goto L_0x01d6;
    L_0x01cc:
        r4 = r10.height;
        if (r4 != r5) goto L_0x01d1;
    L_0x01d0:
        goto L_0x01d6;
    L_0x01d1:
        r21 = r1;
        r12 = r2;
        r9 = r3;
        goto L_0x0204;
    L_0x01d6:
        r0 = 1;
        r4 = shouldUseDisplaySize(r10);
        if (r4 == 0) goto L_0x01f0;
    L_0x01dd:
        r4 = new android.graphics.Point;
        r4.<init>();
        r9 = r7.mDisplay;
        r9.getRealSize(r4);
        r3 = r4.x;
        r2 = r4.y;
        r21 = r1;
        r12 = r2;
        r9 = r3;
        goto L_0x0204;
    L_0x01f0:
        r4 = r19.getConfiguration();
        r9 = r4.screenWidthDp;
        r3 = r7.dipToPx(r9);
        r9 = r4.screenHeightDp;
        r2 = r7.dipToPx(r9);
        r21 = r1;
        r12 = r2;
        r9 = r3;
    L_0x0204:
        r1 = r47;
        r2 = r8;
        r3 = r10;
        r4 = r19;
        r22 = r14;
        r14 = r5;
        r5 = r9;
        r23 = r6;
        r6 = r12;
        r1 = r1.measureHierarchy(r2, r3, r4, r5, r6);
        r0 = r0 | r1;
        goto L_0x0220;
    L_0x0217:
        r23 = r6;
        r22 = r14;
        r14 = r5;
        r21 = r1;
        r12 = r2;
        r9 = r3;
    L_0x0220:
        r1 = r47.collectViewAttributes();
        if (r1 == 0) goto L_0x0228;
    L_0x0226:
        r17 = r10;
    L_0x0228:
        r1 = r7.mAttachInfo;
        r1 = r1.mForceReportNewAttributes;
        if (r1 == 0) goto L_0x0235;
    L_0x022e:
        r1 = r7.mAttachInfo;
        r2 = 0;
        r1.mForceReportNewAttributes = r2;
        r17 = r10;
    L_0x0235:
        r1 = r7.mFirst;
        if (r1 != 0) goto L_0x023f;
    L_0x0239:
        r1 = r7.mAttachInfo;
        r1 = r1.mViewVisibilityChanged;
        if (r1 == 0) goto L_0x027e;
    L_0x023f:
        r1 = r7.mAttachInfo;
        r2 = 0;
        r1.mViewVisibilityChanged = r2;
        r2 = r7.mSoftInputMode;
        r2 = r2 & 240;
        if (r2 != 0) goto L_0x027e;
    L_0x024a:
        r1 = r1.mScrollContainers;
        r1 = r1.size();
        r3 = 0;
    L_0x0251:
        if (r3 >= r1) goto L_0x0268;
    L_0x0253:
        r4 = r7.mAttachInfo;
        r4 = r4.mScrollContainers;
        r4 = r4.get(r3);
        r4 = (android.view.View) r4;
        r4 = r4.isShown();
        if (r4 == 0) goto L_0x0265;
    L_0x0263:
        r2 = 16;
    L_0x0265:
        r3 = r3 + 1;
        goto L_0x0251;
    L_0x0268:
        if (r2 != 0) goto L_0x026c;
    L_0x026a:
        r2 = 32;
    L_0x026c:
        r3 = r10.softInputMode;
        r3 = r3 & 240;
        if (r3 == r2) goto L_0x027e;
    L_0x0272:
        r3 = r10.softInputMode;
        r3 = r3 & -241;
        r3 = r3 | r2;
        r10.softInputMode = r3;
        r17 = r10;
        r6 = r17;
        goto L_0x0280;
    L_0x027e:
        r6 = r17;
    L_0x0280:
        if (r6 == 0) goto L_0x02a1;
    L_0x0282:
        r1 = r8.mPrivateFlags;
        r1 = r1 & 512;
        if (r1 == 0) goto L_0x0293;
    L_0x0288:
        r1 = r6.format;
        r1 = android.graphics.PixelFormat.formatHasAlpha(r1);
        if (r1 != 0) goto L_0x0293;
    L_0x0290:
        r1 = -3;
        r6.format = r1;
    L_0x0293:
        r1 = r7.mAttachInfo;
        r2 = r6.flags;
        r3 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;
        r2 = r2 & r3;
        if (r2 == 0) goto L_0x029e;
    L_0x029c:
        r2 = 1;
        goto L_0x029f;
    L_0x029e:
        r2 = 0;
    L_0x029f:
        r1.mOverscanRequested = r2;
    L_0x02a1:
        r1 = r7.mApplyInsetsRequested;
        if (r1 == 0) goto L_0x02d2;
    L_0x02a5:
        r1 = 0;
        r7.mApplyInsetsRequested = r1;
        r1 = r7.mAttachInfo;
        r1 = r1.mOverscanRequested;
        r7.mLastOverscanRequested = r1;
        r7.dispatchApplyInsets(r8);
        r1 = r7.mLayoutRequested;
        if (r1 == 0) goto L_0x02cf;
    L_0x02b5:
        r1 = r7.mView;
        r1 = r1.getContext();
        r4 = r1.getResources();
        r1 = r47;
        r2 = r8;
        r3 = r10;
        r5 = r9;
        r24 = r6;
        r6 = r12;
        r1 = r1.measureHierarchy(r2, r3, r4, r5, r6);
        r0 = r0 | r1;
        r17 = r0;
        goto L_0x02d6;
    L_0x02cf:
        r24 = r6;
        goto L_0x02d4;
    L_0x02d2:
        r24 = r6;
    L_0x02d4:
        r17 = r0;
    L_0x02d6:
        if (r18 == 0) goto L_0x02db;
    L_0x02d8:
        r1 = 0;
        r7.mLayoutRequested = r1;
    L_0x02db:
        if (r18 == 0) goto L_0x0315;
    L_0x02dd:
        if (r17 == 0) goto L_0x0315;
    L_0x02df:
        r0 = r7.mWidth;
        r1 = r8.getMeasuredWidth();
        if (r0 != r1) goto L_0x0313;
    L_0x02e7:
        r0 = r7.mHeight;
        r1 = r8.getMeasuredHeight();
        if (r0 != r1) goto L_0x0313;
    L_0x02ef:
        r0 = r10.width;
        if (r0 != r14) goto L_0x0301;
    L_0x02f3:
        r0 = r23.width();
        if (r0 >= r9) goto L_0x0301;
    L_0x02f9:
        r0 = r23.width();
        r1 = r7.mWidth;
        if (r0 != r1) goto L_0x0313;
    L_0x0301:
        r0 = r10.height;
        if (r0 != r14) goto L_0x0315;
    L_0x0305:
        r0 = r23.height();
        if (r0 >= r12) goto L_0x0315;
    L_0x030b:
        r0 = r23.height();
        r1 = r7.mHeight;
        if (r0 == r1) goto L_0x0315;
    L_0x0313:
        r0 = 1;
        goto L_0x0316;
    L_0x0315:
        r0 = 0;
    L_0x0316:
        r1 = r7.mDragResizing;
        if (r1 == 0) goto L_0x0320;
    L_0x031a:
        r1 = r7.mResizeMode;
        if (r1 != 0) goto L_0x0320;
    L_0x031e:
        r1 = 1;
        goto L_0x0321;
    L_0x0320:
        r1 = 0;
    L_0x0321:
        r0 = r0 | r1;
        r1 = r7.mActivityRelaunched;
        r14 = r0 | r1;
        r0 = r7.mAttachInfo;
        r0 = r0.mTreeObserver;
        r0 = r0.hasComputeInternalInsetsListeners();
        if (r0 != 0) goto L_0x0339;
    L_0x0330:
        r0 = r7.mAttachInfo;
        r0 = r0.mHasNonEmptyGivenInternalInsets;
        if (r0 == 0) goto L_0x0337;
    L_0x0336:
        goto L_0x0339;
    L_0x0337:
        r0 = 0;
        goto L_0x033a;
    L_0x0339:
        r0 = 1;
    L_0x033a:
        r19 = r0;
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r3 = r7.mSurface;
        r6 = r3.getGenerationId();
        if (r11 != 0) goto L_0x0349;
    L_0x0347:
        r3 = 1;
        goto L_0x034a;
    L_0x0349:
        r3 = 0;
    L_0x034a:
        r25 = r3;
        r5 = r7.mForceNextWindowRelayout;
        r3 = 0;
        r4 = r7.mFirst;
        if (r4 != 0) goto L_0x0381;
    L_0x0353:
        if (r14 != 0) goto L_0x0381;
    L_0x0355:
        if (r21 != 0) goto L_0x0381;
    L_0x0357:
        if (r13 != 0) goto L_0x0381;
    L_0x0359:
        r4 = r24;
        if (r4 != 0) goto L_0x037a;
    L_0x035d:
        r24 = r0;
        r0 = r7.mForceNextWindowRelayout;
        if (r0 == 0) goto L_0x0368;
    L_0x0363:
        r26 = r9;
        r9 = r23;
        goto L_0x0389;
    L_0x0368:
        r26 = r9;
        r9 = r23;
        r7.maybeHandleWindowMove(r9);
        r40 = r4;
        r42 = r5;
        r32 = r9;
        r31 = r12;
        r12 = r6;
        goto L_0x08dc;
    L_0x037a:
        r24 = r0;
        r26 = r9;
        r9 = r23;
        goto L_0x0389;
    L_0x0381:
        r26 = r9;
        r9 = r23;
        r4 = r24;
        r24 = r0;
    L_0x0389:
        r23 = r1;
        r1 = 0;
        r7.mForceNextWindowRelayout = r1;
        if (r25 == 0) goto L_0x039d;
    L_0x0390:
        if (r19 == 0) goto L_0x039a;
    L_0x0392:
        r0 = r7.mFirst;
        if (r0 != 0) goto L_0x0398;
    L_0x0396:
        if (r13 == 0) goto L_0x039a;
    L_0x0398:
        r0 = 1;
        goto L_0x039b;
    L_0x039a:
        r0 = 0;
    L_0x039b:
        r1 = r0;
        goto L_0x039f;
    L_0x039d:
        r1 = r24;
    L_0x039f:
        r0 = r7.mSurfaceHolder;
        if (r0 == 0) goto L_0x03ae;
    L_0x03a3:
        r0 = r0.mSurfaceLock;
        r0.lock();
        r24 = r2;
        r2 = 1;
        r7.mDrawingAllowed = r2;
        goto L_0x03b0;
    L_0x03ae:
        r24 = r2;
    L_0x03b0:
        r2 = 0;
        r27 = 0;
        r0 = r7.mSurface;
        r28 = r0.isValid();
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x0711 }
        r0 = r0.mThreadedRenderer;	 Catch:{ RemoteException -> 0x0711 }
        if (r0 == 0) goto L_0x0426;
    L_0x03bf:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x0414 }
        r0 = r0.mThreadedRenderer;	 Catch:{ RemoteException -> 0x0414 }
        r0 = r0.pause();	 Catch:{ RemoteException -> 0x0414 }
        if (r0 == 0) goto L_0x03f8;
    L_0x03c9:
        r0 = r7.mDirty;	 Catch:{ RemoteException -> 0x0414 }
        r29 = r2;
        r2 = r7.mWidth;	 Catch:{ RemoteException -> 0x03e8 }
        r30 = r3;
        r3 = r7.mHeight;	 Catch:{ RemoteException -> 0x03da }
        r31 = r12;
        r12 = 0;
        r0.set(r12, r12, r2, r3);	 Catch:{ RemoteException -> 0x0408 }
        goto L_0x03fe;
    L_0x03da:
        r0 = move-exception;
        r31 = r12;
        r44 = r1;
        r40 = r4;
        r42 = r5;
        r12 = r6;
        r46 = r23;
        goto L_0x0721;
    L_0x03e8:
        r0 = move-exception;
        r30 = r3;
        r31 = r12;
        r44 = r1;
        r40 = r4;
        r42 = r5;
        r12 = r6;
        r46 = r23;
        goto L_0x0721;
    L_0x03f8:
        r29 = r2;
        r30 = r3;
        r31 = r12;
    L_0x03fe:
        r0 = r7.mChoreographer;	 Catch:{ RemoteException -> 0x0408 }
        r0 = r0.mFrameInfo;	 Catch:{ RemoteException -> 0x0408 }
        r2 = 1;
        r0.addFlags(r2);	 Catch:{ RemoteException -> 0x0408 }
        goto L_0x042c;
    L_0x0408:
        r0 = move-exception;
        r44 = r1;
        r40 = r4;
        r42 = r5;
        r12 = r6;
        r46 = r23;
        goto L_0x0721;
    L_0x0414:
        r0 = move-exception;
        r29 = r2;
        r30 = r3;
        r31 = r12;
        r44 = r1;
        r40 = r4;
        r42 = r5;
        r12 = r6;
        r46 = r23;
        goto L_0x0721;
    L_0x0426:
        r29 = r2;
        r30 = r3;
        r31 = r12;
    L_0x042c:
        r0 = r7.relayoutWindow(r4, r11, r1);	 Catch:{ RemoteException -> 0x0706 }
        r12 = r0;
        r0 = r7.mPendingMergedConfiguration;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r7.mLastReportedMergedConfiguration;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.equals(r2);	 Catch:{ RemoteException -> 0x06fb }
        if (r0 != 0) goto L_0x0458;
    L_0x043b:
        r0 = r7.mPendingMergedConfiguration;	 Catch:{ RemoteException -> 0x044c }
        r2 = r7.mFirst;	 Catch:{ RemoteException -> 0x044c }
        if (r2 != 0) goto L_0x0443;
    L_0x0441:
        r2 = 1;
        goto L_0x0444;
    L_0x0443:
        r2 = 0;
    L_0x0444:
        r3 = -1;
        r7.performConfigurationChange(r0, r2, r3);	 Catch:{ RemoteException -> 0x044c }
        r2 = 1;
        r24 = r2;
        goto L_0x0458;
    L_0x044c:
        r0 = move-exception;
        r44 = r1;
        r40 = r4;
        r42 = r5;
        r46 = r12;
        r12 = r6;
        goto L_0x0721;
    L_0x0458:
        r0 = r7.mPendingOverscanInsets;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r2.mOverscanInsets;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.equals(r2);	 Catch:{ RemoteException -> 0x06fb }
        if (r0 != 0) goto L_0x0466;
    L_0x0464:
        r0 = 1;
        goto L_0x0467;
    L_0x0466:
        r0 = 0;
    L_0x0467:
        r23 = r0;
        r0 = r7.mPendingContentInsets;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r2.mContentInsets;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.equals(r2);	 Catch:{ RemoteException -> 0x06fb }
        if (r0 != 0) goto L_0x0477;
    L_0x0475:
        r0 = 1;
        goto L_0x0478;
    L_0x0477:
        r0 = 0;
    L_0x0478:
        r27 = r0;
        r0 = r7.mPendingVisibleInsets;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r2.mVisibleInsets;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.equals(r2);	 Catch:{ RemoteException -> 0x06fb }
        if (r0 != 0) goto L_0x0488;
    L_0x0486:
        r0 = 1;
        goto L_0x0489;
    L_0x0488:
        r0 = 0;
    L_0x0489:
        r32 = r0;
        r0 = r7.mPendingStableInsets;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r2.mStableInsets;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.equals(r2);	 Catch:{ RemoteException -> 0x06fb }
        if (r0 != 0) goto L_0x0499;
    L_0x0497:
        r0 = 1;
        goto L_0x049a;
    L_0x0499:
        r0 = 0;
    L_0x049a:
        r33 = r0;
        r0 = r7.mPendingDisplayCutout;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r2.mDisplayCutout;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.equals(r2);	 Catch:{ RemoteException -> 0x06fb }
        if (r0 != 0) goto L_0x04aa;
    L_0x04a8:
        r0 = 1;
        goto L_0x04ab;
    L_0x04aa:
        r0 = 0;
    L_0x04ab:
        r34 = r0;
        r0 = r7.mPendingOutsets;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r2.mOutsets;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.equals(r2);	 Catch:{ RemoteException -> 0x06fb }
        r2 = 1;
        r0 = r0 ^ r2;
        r35 = r0;
        r0 = r12 & 32;
        if (r0 == 0) goto L_0x04c1;
    L_0x04bf:
        r0 = 1;
        goto L_0x04c2;
    L_0x04c1:
        r0 = 0;
    L_0x04c2:
        r30 = r0;
        r15 = r15 | r30;
        r0 = r7.mPendingAlwaysConsumeSystemBars;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r2.mAlwaysConsumeSystemBars;	 Catch:{ RemoteException -> 0x06fb }
        if (r0 == r2) goto L_0x04d0;
    L_0x04ce:
        r0 = 1;
        goto L_0x04d1;
    L_0x04d0:
        r0 = 0;
    L_0x04d1:
        r36 = r0;
        r0 = r10.getColorMode();	 Catch:{ RemoteException -> 0x06fb }
        r0 = r7.hasColorModeChanged(r0);	 Catch:{ RemoteException -> 0x06fb }
        r37 = r0;
        if (r27 == 0) goto L_0x04e8;
    L_0x04df:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mContentInsets;	 Catch:{ RemoteException -> 0x044c }
        r2 = r7.mPendingContentInsets;	 Catch:{ RemoteException -> 0x044c }
        r0.set(r2);	 Catch:{ RemoteException -> 0x044c }
    L_0x04e8:
        if (r23 == 0) goto L_0x04f6;
    L_0x04ea:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mOverscanInsets;	 Catch:{ RemoteException -> 0x044c }
        r2 = r7.mPendingOverscanInsets;	 Catch:{ RemoteException -> 0x044c }
        r0.set(r2);	 Catch:{ RemoteException -> 0x044c }
        r0 = 1;
        r27 = r0;
    L_0x04f6:
        if (r33 == 0) goto L_0x0504;
    L_0x04f8:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mStableInsets;	 Catch:{ RemoteException -> 0x044c }
        r2 = r7.mPendingStableInsets;	 Catch:{ RemoteException -> 0x044c }
        r0.set(r2);	 Catch:{ RemoteException -> 0x044c }
        r0 = 1;
        r27 = r0;
    L_0x0504:
        if (r34 == 0) goto L_0x0512;
    L_0x0506:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mDisplayCutout;	 Catch:{ RemoteException -> 0x044c }
        r2 = r7.mPendingDisplayCutout;	 Catch:{ RemoteException -> 0x044c }
        r0.set(r2);	 Catch:{ RemoteException -> 0x044c }
        r0 = 1;
        r27 = r0;
    L_0x0512:
        if (r36 == 0) goto L_0x051d;
    L_0x0514:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r2 = r7.mPendingAlwaysConsumeSystemBars;	 Catch:{ RemoteException -> 0x044c }
        r0.mAlwaysConsumeSystemBars = r2;	 Catch:{ RemoteException -> 0x044c }
        r0 = 1;
        r27 = r0;
    L_0x051d:
        if (r27 != 0) goto L_0x0535;
    L_0x051f:
        r0 = r7.mLastSystemUiVisibility;	 Catch:{ RemoteException -> 0x044c }
        r2 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r2 = r2.mSystemUiVisibility;	 Catch:{ RemoteException -> 0x044c }
        if (r0 != r2) goto L_0x0535;
    L_0x0527:
        r0 = r7.mApplyInsetsRequested;	 Catch:{ RemoteException -> 0x044c }
        if (r0 != 0) goto L_0x0535;
    L_0x052b:
        r0 = r7.mLastOverscanRequested;	 Catch:{ RemoteException -> 0x044c }
        r2 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r2 = r2.mOverscanRequested;	 Catch:{ RemoteException -> 0x044c }
        if (r0 != r2) goto L_0x0535;
    L_0x0533:
        if (r35 == 0) goto L_0x0553;
    L_0x0535:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.mSystemUiVisibility;	 Catch:{ RemoteException -> 0x06fb }
        r7.mLastSystemUiVisibility = r0;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.mOverscanRequested;	 Catch:{ RemoteException -> 0x06fb }
        r7.mLastOverscanRequested = r0;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.mOutsets;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r7.mPendingOutsets;	 Catch:{ RemoteException -> 0x06fb }
        r0.set(r2);	 Catch:{ RemoteException -> 0x06fb }
        r2 = 0;
        r7.mApplyInsetsRequested = r2;	 Catch:{ RemoteException -> 0x06fb }
        r7.dispatchApplyInsets(r8);	 Catch:{ RemoteException -> 0x06fb }
        r0 = 1;
        r27 = r0;
    L_0x0553:
        if (r32 == 0) goto L_0x055e;
    L_0x0555:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mVisibleInsets;	 Catch:{ RemoteException -> 0x044c }
        r2 = r7.mPendingVisibleInsets;	 Catch:{ RemoteException -> 0x044c }
        r0.set(r2);	 Catch:{ RemoteException -> 0x044c }
    L_0x055e:
        if (r37 == 0) goto L_0x0577;
    L_0x0560:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mThreadedRenderer;	 Catch:{ RemoteException -> 0x044c }
        if (r0 == 0) goto L_0x0577;
    L_0x0566:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mThreadedRenderer;	 Catch:{ RemoteException -> 0x044c }
        r2 = r10.getColorMode();	 Catch:{ RemoteException -> 0x044c }
        r3 = 1;
        if (r2 != r3) goto L_0x0573;
    L_0x0571:
        r2 = 1;
        goto L_0x0574;
    L_0x0573:
        r2 = 0;
    L_0x0574:
        r0.setWideGamut(r2);	 Catch:{ RemoteException -> 0x044c }
    L_0x0577:
        if (r28 != 0) goto L_0x05c8;
    L_0x0579:
        r0 = r7.mSurface;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.isValid();	 Catch:{ RemoteException -> 0x044c }
        if (r0 == 0) goto L_0x0638;
    L_0x0581:
        r2 = 1;
        r7.mFullRedrawNeeded = r2;	 Catch:{ RemoteException -> 0x044c }
        r0 = r7.mPreviousTransparentRegion;	 Catch:{ RemoteException -> 0x044c }
        r0.setEmpty();	 Catch:{ RemoteException -> 0x044c }
        android.view.Choreographer.setNextFrameAtFront(r2);	 Catch:{ RemoteException -> 0x044c }
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mThreadedRenderer;	 Catch:{ RemoteException -> 0x044c }
        if (r0 == 0) goto L_0x0638;
    L_0x0592:
        r0 = r7.mAttachInfo;	 Catch:{ OutOfResourcesException -> 0x05b3 }
        r0 = r0.mThreadedRenderer;	 Catch:{ OutOfResourcesException -> 0x05b3 }
        r2 = r7.mSurface;	 Catch:{ OutOfResourcesException -> 0x05b3 }
        r0 = r0.initialize(r2);	 Catch:{ OutOfResourcesException -> 0x05b3 }
        r2 = r0;
        if (r2 == 0) goto L_0x05af;
    L_0x059f:
        r0 = r8.mPrivateFlags;	 Catch:{ OutOfResourcesException -> 0x05ad }
        r0 = r0 & 512;
        if (r0 != 0) goto L_0x05af;
    L_0x05a5:
        r0 = r7.mAttachInfo;	 Catch:{ OutOfResourcesException -> 0x05ad }
        r0 = r0.mThreadedRenderer;	 Catch:{ OutOfResourcesException -> 0x05ad }
        r0.allocateBuffers();	 Catch:{ OutOfResourcesException -> 0x05ad }
        goto L_0x05af;
    L_0x05ad:
        r0 = move-exception;
        goto L_0x05b6;
    L_0x05af:
        r29 = r2;
        goto L_0x0638;
    L_0x05b3:
        r0 = move-exception;
        r2 = r29;
    L_0x05b6:
        r7.handleOutOfResourcesException(r0);	 Catch:{ RemoteException -> 0x05ba }
        return;
    L_0x05ba:
        r0 = move-exception;
        r44 = r1;
        r29 = r2;
        r40 = r4;
        r42 = r5;
        r46 = r12;
        r12 = r6;
        goto L_0x0721;
    L_0x05c8:
        r0 = r7.mSurface;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.isValid();	 Catch:{ RemoteException -> 0x06fb }
        if (r0 != 0) goto L_0x060e;
    L_0x05d0:
        r0 = r7.mLastScrolledFocus;	 Catch:{ RemoteException -> 0x044c }
        if (r0 == 0) goto L_0x05d9;
    L_0x05d4:
        r0 = r7.mLastScrolledFocus;	 Catch:{ RemoteException -> 0x044c }
        r0.clear();	 Catch:{ RemoteException -> 0x044c }
    L_0x05d9:
        r2 = 0;
        r7.mCurScrollY = r2;	 Catch:{ RemoteException -> 0x044c }
        r7.mScrollY = r2;	 Catch:{ RemoteException -> 0x044c }
        r0 = r7.mView;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0 instanceof com.android.internal.view.RootViewSurfaceTaker;	 Catch:{ RemoteException -> 0x044c }
        if (r0 == 0) goto L_0x05ed;
    L_0x05e4:
        r0 = r7.mView;	 Catch:{ RemoteException -> 0x044c }
        r0 = (com.android.internal.view.RootViewSurfaceTaker) r0;	 Catch:{ RemoteException -> 0x044c }
        r2 = r7.mCurScrollY;	 Catch:{ RemoteException -> 0x044c }
        r0.onRootViewScrollYChanged(r2);	 Catch:{ RemoteException -> 0x044c }
    L_0x05ed:
        r0 = r7.mScroller;	 Catch:{ RemoteException -> 0x044c }
        if (r0 == 0) goto L_0x05f6;
    L_0x05f1:
        r0 = r7.mScroller;	 Catch:{ RemoteException -> 0x044c }
        r0.abortAnimation();	 Catch:{ RemoteException -> 0x044c }
    L_0x05f6:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mThreadedRenderer;	 Catch:{ RemoteException -> 0x044c }
        if (r0 == 0) goto L_0x0638;
    L_0x05fc:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mThreadedRenderer;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.isEnabled();	 Catch:{ RemoteException -> 0x044c }
        if (r0 == 0) goto L_0x0638;
    L_0x0606:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mThreadedRenderer;	 Catch:{ RemoteException -> 0x044c }
        r0.destroy();	 Catch:{ RemoteException -> 0x044c }
        goto L_0x0638;
    L_0x060e:
        r0 = r7.mSurface;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.getGenerationId();	 Catch:{ RemoteException -> 0x06fb }
        if (r6 != r0) goto L_0x061c;
    L_0x0616:
        if (r30 != 0) goto L_0x061c;
    L_0x0618:
        if (r5 != 0) goto L_0x061c;
    L_0x061a:
        if (r37 == 0) goto L_0x0638;
    L_0x061c:
        r0 = r7.mSurfaceHolder;	 Catch:{ RemoteException -> 0x06fb }
        if (r0 != 0) goto L_0x0638;
    L_0x0620:
        r0 = r7.mAttachInfo;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.mThreadedRenderer;	 Catch:{ RemoteException -> 0x044c }
        if (r0 == 0) goto L_0x0638;
    L_0x0626:
        r2 = 1;
        r7.mFullRedrawNeeded = r2;	 Catch:{ RemoteException -> 0x044c }
        r0 = r7.mAttachInfo;	 Catch:{ OutOfResourcesException -> 0x0633 }
        r0 = r0.mThreadedRenderer;	 Catch:{ OutOfResourcesException -> 0x0633 }
        r2 = r7.mSurface;	 Catch:{ OutOfResourcesException -> 0x0633 }
        r0.updateSurface(r2);	 Catch:{ OutOfResourcesException -> 0x0633 }
        goto L_0x0638;
    L_0x0633:
        r0 = move-exception;
        r7.handleOutOfResourcesException(r0);	 Catch:{ RemoteException -> 0x044c }
        return;
    L_0x0638:
        r0 = r12 & 16;
        if (r0 == 0) goto L_0x063e;
    L_0x063c:
        r0 = 1;
        goto L_0x063f;
    L_0x063e:
        r0 = 0;
    L_0x063f:
        r2 = r12 & 8;
        if (r2 == 0) goto L_0x0645;
    L_0x0643:
        r2 = 1;
        goto L_0x0646;
    L_0x0645:
        r2 = 0;
    L_0x0646:
        r38 = r2;
        if (r0 != 0) goto L_0x064f;
    L_0x064a:
        if (r38 == 0) goto L_0x064d;
    L_0x064c:
        goto L_0x064f;
    L_0x064d:
        r2 = 0;
        goto L_0x0650;
    L_0x064f:
        r2 = 1;
    L_0x0650:
        r3 = r2;
        r2 = r7.mDragResizing;	 Catch:{ RemoteException -> 0x06fb }
        if (r2 == r3) goto L_0x06cd;
    L_0x0655:
        if (r3 == 0) goto L_0x06bc;
    L_0x0657:
        if (r0 == 0) goto L_0x065b;
    L_0x0659:
        r2 = 0;
        goto L_0x065c;
    L_0x065b:
        r2 = 1;
    L_0x065c:
        r7.mResizeMode = r2;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r7.mWinFrame;	 Catch:{ RemoteException -> 0x06fb }
        r2 = r2.width();	 Catch:{ RemoteException -> 0x06fb }
        r39 = r0;
        r0 = r7.mPendingBackDropFrame;	 Catch:{ RemoteException -> 0x06fb }
        r0 = r0.width();	 Catch:{ RemoteException -> 0x06fb }
        if (r2 != r0) goto L_0x067e;
    L_0x066e:
        r0 = r7.mWinFrame;	 Catch:{ RemoteException -> 0x044c }
        r0 = r0.height();	 Catch:{ RemoteException -> 0x044c }
        r2 = r7.mPendingBackDropFrame;	 Catch:{ RemoteException -> 0x044c }
        r2 = r2.height();	 Catch:{ RemoteException -> 0x044c }
        if (r0 != r2) goto L_0x067e;
    L_0x067c:
        r0 = 1;
        goto L_0x067f;
    L_0x067e:
        r0 = 0;
    L_0x067f:
        r2 = r7.mPendingBackDropFrame;	 Catch:{ RemoteException -> 0x06fb }
        if (r0 != 0) goto L_0x0686;
    L_0x0683:
        r40 = 1;
        goto L_0x0688;
    L_0x0686:
        r40 = 0;
    L_0x0688:
        r41 = r0;
        r0 = r7.mPendingVisibleInsets;	 Catch:{ RemoteException -> 0x06fb }
        r42 = r5;
        r5 = r7.mPendingStableInsets;	 Catch:{ RemoteException -> 0x06b2 }
        r43 = r6;
        r6 = r7.mResizeMode;	 Catch:{ RemoteException -> 0x06a7 }
        r44 = r1;
        r1 = r47;
        r45 = r3;
        r3 = r40;
        r40 = r4;
        r4 = r0;
        r46 = r12;
        r12 = r43;
        r1.startDragResizing(r2, r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x06f9 }
        goto L_0x06da;
    L_0x06a7:
        r0 = move-exception;
        r44 = r1;
        r40 = r4;
        r46 = r12;
        r12 = r43;
        goto L_0x0721;
    L_0x06b2:
        r0 = move-exception;
        r44 = r1;
        r40 = r4;
        r46 = r12;
        r12 = r6;
        goto L_0x0721;
    L_0x06bc:
        r39 = r0;
        r44 = r1;
        r45 = r3;
        r40 = r4;
        r42 = r5;
        r46 = r12;
        r12 = r6;
        r47.endDragResizing();	 Catch:{ RemoteException -> 0x06f9 }
        goto L_0x06da;
    L_0x06cd:
        r39 = r0;
        r44 = r1;
        r45 = r3;
        r40 = r4;
        r42 = r5;
        r46 = r12;
        r12 = r6;
    L_0x06da:
        r0 = r7.mUseMTRenderer;	 Catch:{ RemoteException -> 0x06f9 }
        if (r0 != 0) goto L_0x06f2;
    L_0x06de:
        if (r45 == 0) goto L_0x06ed;
    L_0x06e0:
        r0 = r7.mWinFrame;	 Catch:{ RemoteException -> 0x06f9 }
        r0 = r0.left;	 Catch:{ RemoteException -> 0x06f9 }
        r7.mCanvasOffsetX = r0;	 Catch:{ RemoteException -> 0x06f9 }
        r0 = r7.mWinFrame;	 Catch:{ RemoteException -> 0x06f9 }
        r0 = r0.top;	 Catch:{ RemoteException -> 0x06f9 }
        r7.mCanvasOffsetY = r0;	 Catch:{ RemoteException -> 0x06f9 }
        goto L_0x06f2;
    L_0x06ed:
        r1 = 0;
        r7.mCanvasOffsetY = r1;	 Catch:{ RemoteException -> 0x06f9 }
        r7.mCanvasOffsetX = r1;	 Catch:{ RemoteException -> 0x06f9 }
    L_0x06f2:
        r2 = r24;
        r3 = r30;
        r1 = r46;
        goto L_0x0727;
    L_0x06f9:
        r0 = move-exception;
        goto L_0x0721;
    L_0x06fb:
        r0 = move-exception;
        r44 = r1;
        r40 = r4;
        r42 = r5;
        r46 = r12;
        r12 = r6;
        goto L_0x0721;
    L_0x0706:
        r0 = move-exception;
        r44 = r1;
        r40 = r4;
        r42 = r5;
        r12 = r6;
        r46 = r23;
        goto L_0x0721;
    L_0x0711:
        r0 = move-exception;
        r44 = r1;
        r29 = r2;
        r30 = r3;
        r40 = r4;
        r42 = r5;
        r31 = r12;
        r12 = r6;
        r46 = r23;
    L_0x0721:
        r2 = r24;
        r3 = r30;
        r1 = r46;
    L_0x0727:
        r0 = r7.mAttachInfo;
        r4 = r9.left;
        r0.mWindowLeft = r4;
        r0 = r7.mAttachInfo;
        r4 = r9.top;
        r0.mWindowTop = r4;
        r0 = r7.mWidth;
        r4 = r9.width();
        if (r0 != r4) goto L_0x0743;
    L_0x073b:
        r0 = r7.mHeight;
        r4 = r9.height();
        if (r0 == r4) goto L_0x074f;
    L_0x0743:
        r0 = r9.width();
        r7.mWidth = r0;
        r0 = r9.height();
        r7.mHeight = r0;
    L_0x074f:
        r0 = r7.mSurfaceHolder;
        if (r0 == 0) goto L_0x081a;
    L_0x0753:
        r0 = r7.mSurface;
        r0 = r0.isValid();
        if (r0 == 0) goto L_0x0761;
    L_0x075b:
        r0 = r7.mSurfaceHolder;
        r4 = r7.mSurface;
        r0.mSurface = r4;
    L_0x0761:
        r0 = r7.mSurfaceHolder;
        r4 = r7.mWidth;
        r5 = r7.mHeight;
        r0.setSurfaceFrameSize(r4, r5);
        r0 = r7.mSurfaceHolder;
        r0 = r0.mSurfaceLock;
        r0.unlock();
        r0 = r7.mSurface;
        r0 = r0.isValid();
        if (r0 == 0) goto L_0x07f0;
    L_0x0779:
        if (r28 != 0) goto L_0x07a3;
    L_0x077b:
        r0 = r7.mSurfaceHolder;
        r0.ungetCallbacks();
        r4 = 1;
        r7.mIsCreating = r4;
        r0 = r7.mSurfaceHolder;
        r0 = r0.getCallbacks();
        if (r0 == 0) goto L_0x07a0;
    L_0x078b:
        r4 = r0.length;
        r5 = 0;
    L_0x078d:
        if (r5 >= r4) goto L_0x079d;
    L_0x078f:
        r6 = r0[r5];
        r23 = r0;
        r0 = r7.mSurfaceHolder;
        r6.surfaceCreated(r0);
        r5 = r5 + 1;
        r0 = r23;
        goto L_0x078d;
    L_0x079d:
        r23 = r0;
        goto L_0x07a2;
    L_0x07a0:
        r23 = r0;
    L_0x07a2:
        r15 = 1;
    L_0x07a3:
        if (r15 != 0) goto L_0x07b3;
    L_0x07a5:
        r0 = r7.mSurface;
        r0 = r0.getGenerationId();
        if (r12 == r0) goto L_0x07ae;
    L_0x07ad:
        goto L_0x07b3;
    L_0x07ae:
        r24 = r3;
        r32 = r9;
        goto L_0x07ec;
    L_0x07b3:
        r0 = r7.mSurfaceHolder;
        r0 = r0.getCallbacks();
        if (r0 == 0) goto L_0x07e6;
    L_0x07bb:
        r4 = r0.length;
        r5 = 0;
    L_0x07bd:
        if (r5 >= r4) goto L_0x07df;
    L_0x07bf:
        r6 = r0[r5];
        r23 = r0;
        r0 = r7.mSurfaceHolder;
        r24 = r3;
        r3 = r10.format;
        r30 = r4;
        r4 = r7.mWidth;
        r32 = r9;
        r9 = r7.mHeight;
        r6.surfaceChanged(r0, r3, r4, r9);
        r5 = r5 + 1;
        r0 = r23;
        r3 = r24;
        r4 = r30;
        r9 = r32;
        goto L_0x07bd;
    L_0x07df:
        r23 = r0;
        r24 = r3;
        r32 = r9;
        goto L_0x07ec;
    L_0x07e6:
        r23 = r0;
        r24 = r3;
        r32 = r9;
    L_0x07ec:
        r3 = 0;
        r7.mIsCreating = r3;
        goto L_0x081e;
    L_0x07f0:
        r24 = r3;
        r32 = r9;
        if (r28 == 0) goto L_0x081e;
    L_0x07f6:
        r47.notifySurfaceDestroyed();
        r0 = r7.mSurfaceHolder;
        r0 = r0.mSurfaceLock;
        r0.lock();
        r0 = r7.mSurfaceHolder;	 Catch:{ all -> 0x0811 }
        r3 = new android.view.Surface;	 Catch:{ all -> 0x0811 }
        r3.<init>();	 Catch:{ all -> 0x0811 }
        r0.mSurface = r3;	 Catch:{ all -> 0x0811 }
        r0 = r7.mSurfaceHolder;
        r0 = r0.mSurfaceLock;
        r0.unlock();
        goto L_0x081e;
    L_0x0811:
        r0 = move-exception;
        r3 = r7.mSurfaceHolder;
        r3 = r3.mSurfaceLock;
        r3.unlock();
        throw r0;
    L_0x081a:
        r24 = r3;
        r32 = r9;
    L_0x081e:
        r0 = r7.mAttachInfo;
        r0 = r0.mThreadedRenderer;
        if (r0 == 0) goto L_0x0850;
    L_0x0824:
        r3 = r0.isEnabled();
        if (r3 == 0) goto L_0x0850;
    L_0x082a:
        if (r29 != 0) goto L_0x0840;
    L_0x082c:
        r3 = r7.mWidth;
        r4 = r0.getWidth();
        if (r3 != r4) goto L_0x0840;
    L_0x0834:
        r3 = r7.mHeight;
        r4 = r0.getHeight();
        if (r3 != r4) goto L_0x0840;
    L_0x083c:
        r3 = r7.mNeedsRendererSetup;
        if (r3 == 0) goto L_0x0850;
    L_0x0840:
        r3 = r7.mWidth;
        r4 = r7.mHeight;
        r5 = r7.mAttachInfo;
        r6 = r7.mWindowAttributes;
        r6 = r6.surfaceInsets;
        r0.setup(r3, r4, r5, r6);
        r3 = 0;
        r7.mNeedsRendererSetup = r3;
    L_0x0850:
        r3 = r7.mStopped;
        if (r3 == 0) goto L_0x0858;
    L_0x0854:
        r3 = r7.mReportNextDraw;
        if (r3 == 0) goto L_0x087a;
    L_0x0858:
        r3 = r1 & 1;
        if (r3 == 0) goto L_0x085e;
    L_0x085c:
        r3 = 1;
        goto L_0x085f;
    L_0x085e:
        r3 = 0;
    L_0x085f:
        r3 = r7.ensureTouchModeLocally(r3);
        if (r3 != 0) goto L_0x087d;
    L_0x0865:
        r4 = r7.mWidth;
        r5 = r8.getMeasuredWidth();
        if (r4 != r5) goto L_0x087d;
    L_0x086d:
        r4 = r7.mHeight;
        r5 = r8.getMeasuredHeight();
        if (r4 != r5) goto L_0x087d;
    L_0x0875:
        if (r27 != 0) goto L_0x087d;
    L_0x0877:
        if (r2 == 0) goto L_0x087a;
    L_0x0879:
        goto L_0x087d;
    L_0x087a:
        r34 = r1;
        goto L_0x08d6;
    L_0x087d:
        r4 = r7.mWidth;
        r5 = r10.width;
        r4 = getRootMeasureSpec(r4, r5);
        r5 = r7.mHeight;
        r6 = r10.height;
        r5 = getRootMeasureSpec(r5, r6);
        r7.performMeasure(r4, r5);
        r6 = r8.getMeasuredWidth();
        r9 = r8.getMeasuredHeight();
        r23 = 0;
        r30 = r0;
        r0 = r10.horizontalWeight;
        r33 = 0;
        r0 = (r0 > r33 ? 1 : (r0 == r33 ? 0 : -1));
        r34 = r1;
        if (r0 <= 0) goto L_0x08b7;
    L_0x08a6:
        r0 = r7.mWidth;
        r0 = r0 - r6;
        r0 = (float) r0;
        r1 = r10.horizontalWeight;
        r0 = r0 * r1;
        r0 = (int) r0;
        r6 = r6 + r0;
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r4 = android.view.View.MeasureSpec.makeMeasureSpec(r6, r0);
        r23 = 1;
    L_0x08b7:
        r0 = r10.verticalWeight;
        r0 = (r0 > r33 ? 1 : (r0 == r33 ? 0 : -1));
        if (r0 <= 0) goto L_0x08ce;
    L_0x08bd:
        r0 = r7.mHeight;
        r0 = r0 - r9;
        r0 = (float) r0;
        r1 = r10.verticalWeight;
        r0 = r0 * r1;
        r0 = (int) r0;
        r9 = r9 + r0;
        r0 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
        r5 = android.view.View.MeasureSpec.makeMeasureSpec(r9, r0);
        r23 = 1;
    L_0x08ce:
        if (r23 == 0) goto L_0x08d3;
    L_0x08d0:
        r7.performMeasure(r4, r5);
    L_0x08d3:
        r0 = 1;
        r18 = r0;
    L_0x08d6:
        r3 = r24;
        r1 = r34;
        r24 = r44;
    L_0x08dc:
        if (r3 == 0) goto L_0x08e1;
    L_0x08de:
        r47.updateBoundsSurface();
    L_0x08e1:
        if (r18 == 0) goto L_0x08ed;
    L_0x08e3:
        r0 = r7.mStopped;
        if (r0 == 0) goto L_0x08eb;
    L_0x08e7:
        r0 = r7.mReportNextDraw;
        if (r0 == 0) goto L_0x08ed;
    L_0x08eb:
        r0 = 1;
        goto L_0x08ee;
    L_0x08ed:
        r0 = 0;
    L_0x08ee:
        r4 = r0;
        if (r4 != 0) goto L_0x08fa;
    L_0x08f1:
        r0 = r7.mAttachInfo;
        r0 = r0.mRecomputeGlobalAttributes;
        if (r0 == 0) goto L_0x08f8;
    L_0x08f7:
        goto L_0x08fa;
    L_0x08f8:
        r0 = 0;
        goto L_0x08fb;
    L_0x08fa:
        r0 = 1;
    L_0x08fb:
        r5 = r0;
        if (r4 == 0) goto L_0x096b;
    L_0x08fe:
        r0 = r7.mWidth;
        r6 = r7.mHeight;
        r7.performLayout(r10, r0, r6);
        r0 = r8.mPrivateFlags;
        r0 = r0 & 512;
        if (r0 == 0) goto L_0x0964;
    L_0x090b:
        r0 = r7.mTmpLocation;
        r8.getLocationInWindow(r0);
        r0 = r7.mTransparentRegion;
        r6 = r7.mTmpLocation;
        r23 = r2;
        r9 = 0;
        r2 = r6[r9];
        r27 = r3;
        r20 = 1;
        r3 = r6[r20];
        r6 = r6[r9];
        r9 = r8.mRight;
        r6 = r6 + r9;
        r9 = r8.mLeft;
        r6 = r6 - r9;
        r9 = r7.mTmpLocation;
        r9 = r9[r20];
        r28 = r4;
        r4 = r8.mBottom;
        r9 = r9 + r4;
        r4 = r8.mTop;
        r9 = r9 - r4;
        r0.set(r2, r3, r6, r9);
        r0 = r7.mTransparentRegion;
        r8.gatherTransparentRegion(r0);
        r0 = r7.mTranslator;
        if (r0 == 0) goto L_0x0944;
    L_0x093f:
        r2 = r7.mTransparentRegion;
        r0.translateRegionInWindowToScreen(r2);
    L_0x0944:
        r0 = r7.mTransparentRegion;
        r2 = r7.mPreviousTransparentRegion;
        r0 = r0.equals(r2);
        if (r0 != 0) goto L_0x0971;
    L_0x094e:
        r0 = r7.mPreviousTransparentRegion;
        r2 = r7.mTransparentRegion;
        r0.set(r2);
        r2 = 1;
        r7.mFullRedrawNeeded = r2;
        r0 = r7.mWindowSession;	 Catch:{ RemoteException -> 0x0962 }
        r2 = r7.mWindow;	 Catch:{ RemoteException -> 0x0962 }
        r3 = r7.mTransparentRegion;	 Catch:{ RemoteException -> 0x0962 }
        r0.setTransparentRegion(r2, r3);	 Catch:{ RemoteException -> 0x0962 }
        goto L_0x0971;
    L_0x0962:
        r0 = move-exception;
        goto L_0x0971;
    L_0x0964:
        r23 = r2;
        r27 = r3;
        r28 = r4;
        goto L_0x0971;
    L_0x096b:
        r23 = r2;
        r27 = r3;
        r28 = r4;
    L_0x0971:
        if (r5 == 0) goto L_0x097d;
    L_0x0973:
        r0 = r7.mAttachInfo;
        r2 = 0;
        r0.mRecomputeGlobalAttributes = r2;
        r0 = r0.mTreeObserver;
        r0.dispatchOnGlobalLayout();
    L_0x097d:
        if (r19 == 0) goto L_0x09ef;
    L_0x097f:
        r0 = r7.mAttachInfo;
        r2 = r0.mGivenInternalInsets;
        r2.reset();
        r0 = r7.mAttachInfo;
        r0 = r0.mTreeObserver;
        r0.dispatchOnComputeInternalInsets(r2);
        r0 = r7.mAttachInfo;
        r3 = r2.isEmpty();
        r4 = 1;
        r3 = r3 ^ r4;
        r0.mHasNonEmptyGivenInternalInsets = r3;
        if (r24 != 0) goto L_0x09a5;
    L_0x0999:
        r0 = r7.mLastGivenInsets;
        r0 = r0.equals(r2);
        if (r0 != 0) goto L_0x09a2;
    L_0x09a1:
        goto L_0x09a5;
    L_0x09a2:
        r29 = r5;
        goto L_0x09f1;
    L_0x09a5:
        r0 = r7.mLastGivenInsets;
        r0.set(r2);
        r0 = r7.mTranslator;
        if (r0 == 0) goto L_0x09c8;
    L_0x09ae:
        r3 = r2.contentInsets;
        r0 = r0.getTranslatedContentInsets(r3);
        r3 = r7.mTranslator;
        r4 = r2.visibleInsets;
        r3 = r3.getTranslatedVisibleInsets(r4);
        r4 = r7.mTranslator;
        r6 = r2.touchableRegion;
        r4 = r4.getTranslatedTouchableArea(r6);
        r6 = r4;
        r4 = r3;
        r3 = r0;
        goto L_0x09d1;
    L_0x09c8:
        r0 = r2.contentInsets;
        r3 = r2.visibleInsets;
        r4 = r2.touchableRegion;
        r6 = r4;
        r4 = r3;
        r3 = r0;
    L_0x09d1:
        r0 = r7.mWindowSession;	 Catch:{ RemoteException -> 0x09eb }
        r9 = r7.mWindow;	 Catch:{ RemoteException -> 0x09eb }
        r29 = r5;
        r5 = r2.mTouchableInsets;	 Catch:{ RemoteException -> 0x09e9 }
        r33 = r0;
        r34 = r9;
        r35 = r5;
        r36 = r3;
        r37 = r4;
        r38 = r6;
        r33.setInsets(r34, r35, r36, r37, r38);	 Catch:{ RemoteException -> 0x09e9 }
        goto L_0x09f1;
    L_0x09e9:
        r0 = move-exception;
        goto L_0x09f1;
    L_0x09eb:
        r0 = move-exception;
        r29 = r5;
        goto L_0x09f1;
    L_0x09ef:
        r29 = r5;
    L_0x09f1:
        r0 = r7.mFirst;
        if (r0 == 0) goto L_0x0a28;
    L_0x09f5:
        r0 = sAlwaysAssignFocus;
        if (r0 != 0) goto L_0x0a19;
    L_0x09f9:
        r0 = isInTouchMode();
        if (r0 != 0) goto L_0x0a00;
    L_0x09ff:
        goto L_0x0a19;
    L_0x0a00:
        r0 = r7.mView;
        r0 = r0.findFocus();
        r2 = r0 instanceof android.view.ViewGroup;
        if (r2 == 0) goto L_0x0a28;
    L_0x0a0a:
        r2 = r0;
        r2 = (android.view.ViewGroup) r2;
        r2 = r2.getDescendantFocusability();
        r3 = 262144; // 0x40000 float:3.67342E-40 double:1.295163E-318;
        if (r2 != r3) goto L_0x0a28;
    L_0x0a15:
        r0.restoreDefaultFocus();
        goto L_0x0a28;
    L_0x0a19:
        r0 = r7.mView;
        if (r0 == 0) goto L_0x0a28;
    L_0x0a1d:
        r0 = r0.hasFocus();
        if (r0 != 0) goto L_0x0a28;
    L_0x0a23:
        r0 = r7.mView;
        r0.restoreDefaultFocus();
    L_0x0a28:
        if (r13 != 0) goto L_0x0a2e;
    L_0x0a2a:
        r0 = r7.mFirst;
        if (r0 == 0) goto L_0x0a32;
    L_0x0a2e:
        if (r25 == 0) goto L_0x0a32;
    L_0x0a30:
        r0 = 1;
        goto L_0x0a33;
    L_0x0a32:
        r0 = 0;
    L_0x0a33:
        r2 = r7.mAttachInfo;
        r2 = r2.mHasWindowFocus;
        if (r2 == 0) goto L_0x0a3d;
    L_0x0a39:
        if (r25 == 0) goto L_0x0a3d;
    L_0x0a3b:
        r2 = 1;
        goto L_0x0a3e;
    L_0x0a3d:
        r2 = 0;
    L_0x0a3e:
        if (r2 == 0) goto L_0x0a46;
    L_0x0a40:
        r3 = r7.mLostWindowFocus;
        if (r3 == 0) goto L_0x0a46;
    L_0x0a44:
        r3 = 1;
        goto L_0x0a47;
    L_0x0a46:
        r3 = 0;
    L_0x0a47:
        if (r3 == 0) goto L_0x0a4d;
    L_0x0a49:
        r4 = 0;
        r7.mLostWindowFocus = r4;
        goto L_0x0a56;
    L_0x0a4d:
        if (r2 != 0) goto L_0x0a56;
    L_0x0a4f:
        r4 = r7.mHadWindowFocus;
        if (r4 == 0) goto L_0x0a56;
    L_0x0a53:
        r4 = 1;
        r7.mLostWindowFocus = r4;
    L_0x0a56:
        if (r0 != 0) goto L_0x0a5a;
    L_0x0a58:
        if (r3 == 0) goto L_0x0a71;
    L_0x0a5a:
        r4 = r7.mWindowAttributes;
        if (r4 != 0) goto L_0x0a60;
    L_0x0a5e:
        r4 = 0;
        goto L_0x0a69;
    L_0x0a60:
        r4 = r4.type;
        r5 = 2005; // 0x7d5 float:2.81E-42 double:9.906E-321;
        if (r4 != r5) goto L_0x0a68;
    L_0x0a66:
        r4 = 1;
        goto L_0x0a69;
    L_0x0a68:
        r4 = 0;
        if (r4 != 0) goto L_0x0a71;
    L_0x0a6c:
        r5 = 32;
        r8.sendAccessibilityEvent(r5);
    L_0x0a71:
        r4 = 0;
        r7.mFirst = r4;
        r7.mWillDrawSoon = r4;
        r7.mNewSurfaceNeeded = r4;
        r7.mActivityRelaunched = r4;
        r7.mViewVisibility = r11;
        r7.mHadWindowFocus = r2;
        if (r2 == 0) goto L_0x0ad3;
    L_0x0a80:
        r4 = r47.isInLocalFocusMode();
        if (r4 != 0) goto L_0x0ad3;
    L_0x0a86:
        r4 = r7.mWindowAttributes;
        r4 = r4.flags;
        r4 = android.view.WindowManager.LayoutParams.mayUseInputMethod(r4);
        r5 = r7.mLastWasImTarget;
        if (r4 == r5) goto L_0x0ace;
    L_0x0a92:
        r7.mLastWasImTarget = r4;
        r5 = r7.mContext;
        r6 = android.view.inputmethod.InputMethodManager.class;
        r5 = r5.getSystemService(r6);
        r5 = (android.view.inputmethod.InputMethodManager) r5;
        if (r5 == 0) goto L_0x0ac9;
    L_0x0aa0:
        if (r4 == 0) goto L_0x0ac9;
    L_0x0aa2:
        r6 = r7.mView;
        r5.onPreWindowFocus(r6, r2);
        r6 = r7.mView;
        r35 = r6.findFocus();
        r9 = r7.mWindowAttributes;
        r9 = r9.softInputMode;
        r30 = r0;
        r0 = r7.mHasHadWindowFocus;
        r20 = 1;
        r37 = r0 ^ 1;
        r0 = r7.mWindowAttributes;
        r0 = r0.flags;
        r33 = r5;
        r34 = r6;
        r36 = r9;
        r38 = r0;
        r33.onPostWindowFocus(r34, r35, r36, r37, r38);
        goto L_0x0ad7;
    L_0x0ac9:
        r30 = r0;
        r20 = 1;
        goto L_0x0ad7;
    L_0x0ace:
        r30 = r0;
        r20 = 1;
        goto L_0x0ad7;
    L_0x0ad3:
        r30 = r0;
        r20 = 1;
    L_0x0ad7:
        r0 = r1 & 2;
        if (r0 == 0) goto L_0x0ade;
    L_0x0adb:
        r47.reportNextDraw();
    L_0x0ade:
        r0 = r7.mAttachInfo;
        r0 = r0.mTreeObserver;
        r0 = r0.dispatchOnPreDraw();
        if (r0 != 0) goto L_0x0aed;
    L_0x0ae8:
        if (r25 != 0) goto L_0x0aeb;
    L_0x0aea:
        goto L_0x0aed;
    L_0x0aeb:
        r20 = 0;
    L_0x0aed:
        r0 = r20;
        if (r0 != 0) goto L_0x0b1b;
    L_0x0af1:
        r4 = r7.mPendingTransitions;
        if (r4 == 0) goto L_0x0b17;
    L_0x0af5:
        r4 = r4.size();
        if (r4 <= 0) goto L_0x0b17;
    L_0x0afb:
        r4 = 0;
    L_0x0afc:
        r5 = r7.mPendingTransitions;
        r5 = r5.size();
        if (r4 >= r5) goto L_0x0b12;
    L_0x0b04:
        r5 = r7.mPendingTransitions;
        r5 = r5.get(r4);
        r5 = (android.animation.LayoutTransition) r5;
        r5.startChangingAnimations();
        r4 = r4 + 1;
        goto L_0x0afc;
    L_0x0b12:
        r4 = r7.mPendingTransitions;
        r4.clear();
    L_0x0b17:
        r47.performDraw();
        goto L_0x0b47;
    L_0x0b1b:
        if (r25 == 0) goto L_0x0b21;
    L_0x0b1d:
        r47.scheduleTraversals();
        goto L_0x0b47;
    L_0x0b21:
        r4 = r7.mPendingTransitions;
        if (r4 == 0) goto L_0x0b47;
    L_0x0b25:
        r4 = r4.size();
        if (r4 <= 0) goto L_0x0b47;
    L_0x0b2b:
        r4 = 0;
    L_0x0b2c:
        r5 = r7.mPendingTransitions;
        r5 = r5.size();
        if (r4 >= r5) goto L_0x0b42;
    L_0x0b34:
        r5 = r7.mPendingTransitions;
        r5 = r5.get(r4);
        r5 = (android.animation.LayoutTransition) r5;
        r5.endChangingAnimations();
        r4 = r4 + 1;
        goto L_0x0b2c;
    L_0x0b42:
        r4 = r7.mPendingTransitions;
        r4.clear();
    L_0x0b47:
        r4 = 0;
        r7.mIsInTraversal = r4;
        return;
    L_0x0b4b:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewRootImpl.performTraversals():void");
    }

    private void notifySurfaceDestroyed() {
        this.mSurfaceHolder.ungetCallbacks();
        SurfaceHolder.Callback[] callbacks = this.mSurfaceHolder.getCallbacks();
        if (callbacks != null) {
            for (SurfaceHolder.Callback c : callbacks) {
                c.surfaceDestroyed(this.mSurfaceHolder);
            }
        }
    }

    private void maybeHandleWindowMove(Rect frame) {
        boolean windowMoved = (this.mAttachInfo.mWindowLeft == frame.left && this.mAttachInfo.mWindowTop == frame.top) ? false : true;
        if (windowMoved) {
            Translator translator = this.mTranslator;
            if (translator != null) {
                translator.translateRectInScreenToAppWinFrame(frame);
            }
            this.mAttachInfo.mWindowLeft = frame.left;
            this.mAttachInfo.mWindowTop = frame.top;
        }
        if (windowMoved || this.mAttachInfo.mNeedsUpdateLightCenter) {
            if (this.mAttachInfo.mThreadedRenderer != null) {
                this.mAttachInfo.mThreadedRenderer.setLightCenter(this.mAttachInfo);
            }
            this.mAttachInfo.mNeedsUpdateLightCenter = false;
        }
    }

    /* JADX WARNING: Missing block: B:9:0x0011, code skipped:
            if (sNewInsetsMode == 0) goto L_0x0020;
     */
    /* JADX WARNING: Missing block: B:10:0x0013, code skipped:
            if (r1 == false) goto L_0x001b;
     */
    /* JADX WARNING: Missing block: B:11:0x0015, code skipped:
            r12.mInsetsController.onWindowFocusGained();
     */
    /* JADX WARNING: Missing block: B:12:0x001b, code skipped:
            r12.mInsetsController.onWindowFocusLost();
     */
    /* JADX WARNING: Missing block: B:14:0x0022, code skipped:
            if (r12.mAdded == false) goto L_0x011a;
     */
    /* JADX WARNING: Missing block: B:15:0x0024, code skipped:
            profileRendering(r1);
     */
    /* JADX WARNING: Missing block: B:16:0x0028, code skipped:
            if (r1 == false) goto L_0x0086;
     */
    /* JADX WARNING: Missing block: B:17:0x002a, code skipped:
            ensureTouchModeLocally(r2);
     */
    /* JADX WARNING: Missing block: B:18:0x0031, code skipped:
            if (r12.mAttachInfo.mThreadedRenderer == null) goto L_0x0086;
     */
    /* JADX WARNING: Missing block: B:20:0x0039, code skipped:
            if (r12.mSurface.isValid() == false) goto L_0x0086;
     */
    /* JADX WARNING: Missing block: B:21:0x003b, code skipped:
            r12.mFullRedrawNeeded = true;
     */
    /* JADX WARNING: Missing block: B:23:?, code skipped:
            r4 = r12.mWindowAttributes;
     */
    /* JADX WARNING: Missing block: B:24:0x003f, code skipped:
            if (r4 == null) goto L_0x0044;
     */
    /* JADX WARNING: Missing block: B:25:0x0041, code skipped:
            r5 = r4.surfaceInsets;
     */
    /* JADX WARNING: Missing block: B:26:0x0044, code skipped:
            r5 = null;
     */
    /* JADX WARNING: Missing block: B:27:0x0045, code skipped:
            r12.mAttachInfo.mThreadedRenderer.initializeIfNeeded(r12.mWidth, r12.mHeight, r12.mAttachInfo, r12.mSurface, r5);
     */
    /* JADX WARNING: Missing block: B:28:0x0057, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:29:0x0058, code skipped:
            android.util.Log.e(r12.mTag, "OutOfResourcesException locking surface", r0);
     */
    /* JADX WARNING: Missing block: B:32:0x0067, code skipped:
            if (r12.mWindowSession.outOfMemory(r12.mWindow) == false) goto L_0x0069;
     */
    /* JADX WARNING: Missing block: B:33:0x0069, code skipped:
            android.util.Slog.w(r12.mTag, "No processes killed for memory; killing self");
            android.os.Process.killProcess(android.os.Process.myPid());
     */
    /* JADX WARNING: Missing block: B:35:0x0079, code skipped:
            r3 = r12.mHandler;
            r3.sendMessageDelayed(r3.obtainMessage(6), 500);
     */
    /* JADX WARNING: Missing block: B:36:0x0085, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:37:0x0086, code skipped:
            r12.mAttachInfo.mHasWindowFocus = r1;
            r12.mLastWasImTarget = android.view.WindowManager.LayoutParams.mayUseInputMethod(r12.mWindowAttributes.flags);
            r4 = (android.view.inputmethod.InputMethodManager) r12.mContext.getSystemService(android.view.inputmethod.InputMethodManager.class);
     */
    /* JADX WARNING: Missing block: B:38:0x009e, code skipped:
            if (r4 == null) goto L_0x00af;
     */
    /* JADX WARNING: Missing block: B:40:0x00a2, code skipped:
            if (r12.mLastWasImTarget == false) goto L_0x00af;
     */
    /* JADX WARNING: Missing block: B:42:0x00a8, code skipped:
            if (isInLocalFocusMode() != false) goto L_0x00af;
     */
    /* JADX WARNING: Missing block: B:43:0x00aa, code skipped:
            r4.onPreWindowFocus(r12.mView, r1);
     */
    /* JADX WARNING: Missing block: B:45:0x00b1, code skipped:
            if (r12.mView == null) goto L_0x00d3;
     */
    /* JADX WARNING: Missing block: B:46:0x00b3, code skipped:
            r12.mAttachInfo.mKeyDispatchState.reset();
            r12.mView.dispatchWindowFocusChanged(r1);
            r12.mAttachInfo.mTreeObserver.dispatchOnWindowFocusChange(r1);
     */
    /* JADX WARNING: Missing block: B:47:0x00ca, code skipped:
            if (r12.mAttachInfo.mTooltipHost == null) goto L_0x00d3;
     */
    /* JADX WARNING: Missing block: B:48:0x00cc, code skipped:
            r12.mAttachInfo.mTooltipHost.hideTooltip();
     */
    /* JADX WARNING: Missing block: B:49:0x00d3, code skipped:
            if (r1 == false) goto L_0x0113;
     */
    /* JADX WARNING: Missing block: B:50:0x00d5, code skipped:
            if (r4 == null) goto L_0x00f7;
     */
    /* JADX WARNING: Missing block: B:52:0x00d9, code skipped:
            if (r12.mLastWasImTarget == false) goto L_0x00f7;
     */
    /* JADX WARNING: Missing block: B:54:0x00df, code skipped:
            if (isInLocalFocusMode() != false) goto L_0x00f7;
     */
    /* JADX WARNING: Missing block: B:55:0x00e1, code skipped:
            r6 = r12.mView;
            r4.onPostWindowFocus(r6, r6.findFocus(), r12.mWindowAttributes.softInputMode, r12.mHasHadWindowFocus ^ 1, r12.mWindowAttributes.flags);
     */
    /* JADX WARNING: Missing block: B:56:0x00f7, code skipped:
            r0 = r12.mWindowAttributes;
            r0.softInputMode &= android.net.TrafficStats.TAG_NETWORK_STACK_RANGE_END;
            r0 = (android.view.WindowManager.LayoutParams) r12.mView.getLayoutParams();
            r0.softInputMode &= android.net.TrafficStats.TAG_NETWORK_STACK_RANGE_END;
            r12.mHasHadWindowFocus = true;
            fireAccessibilityFocusEventIfHasFocusedNode();
     */
    /* JADX WARNING: Missing block: B:58:0x0115, code skipped:
            if (r12.mPointerCapture == false) goto L_0x011a;
     */
    /* JADX WARNING: Missing block: B:59:0x0117, code skipped:
            handlePointerCaptureChanged(false);
     */
    /* JADX WARNING: Missing block: B:60:0x011a, code skipped:
            r12.mFirstInputStage.onWindowFocusChanged(r1);
     */
    /* JADX WARNING: Missing block: B:61:0x011f, code skipped:
            return;
     */
    private void handleWindowFocusChanged() {
        /*
        r12 = this;
        monitor-enter(r12);
        r0 = r12.mWindowFocusChanged;	 Catch:{ all -> 0x0120 }
        if (r0 != 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r12);	 Catch:{ all -> 0x0120 }
        return;
    L_0x0007:
        r0 = 0;
        r12.mWindowFocusChanged = r0;	 Catch:{ all -> 0x0120 }
        r1 = r12.mUpcomingWindowFocus;	 Catch:{ all -> 0x0120 }
        r2 = r12.mUpcomingInTouchMode;	 Catch:{ all -> 0x0120 }
        monitor-exit(r12);	 Catch:{ all -> 0x0120 }
        r3 = sNewInsetsMode;
        if (r3 == 0) goto L_0x0020;
    L_0x0013:
        if (r1 == 0) goto L_0x001b;
    L_0x0015:
        r3 = r12.mInsetsController;
        r3.onWindowFocusGained();
        goto L_0x0020;
    L_0x001b:
        r3 = r12.mInsetsController;
        r3.onWindowFocusLost();
    L_0x0020:
        r3 = r12.mAdded;
        if (r3 == 0) goto L_0x011a;
    L_0x0024:
        r12.profileRendering(r1);
        r3 = 1;
        if (r1 == 0) goto L_0x0086;
    L_0x002a:
        r12.ensureTouchModeLocally(r2);
        r4 = r12.mAttachInfo;
        r4 = r4.mThreadedRenderer;
        if (r4 == 0) goto L_0x0086;
    L_0x0033:
        r4 = r12.mSurface;
        r4 = r4.isValid();
        if (r4 == 0) goto L_0x0086;
    L_0x003b:
        r12.mFullRedrawNeeded = r3;
        r4 = r12.mWindowAttributes;	 Catch:{ OutOfResourcesException -> 0x0057 }
        if (r4 == 0) goto L_0x0044;
    L_0x0041:
        r5 = r4.surfaceInsets;	 Catch:{ OutOfResourcesException -> 0x0057 }
        goto L_0x0045;
    L_0x0044:
        r5 = 0;
    L_0x0045:
        r11 = r5;
        r5 = r12.mAttachInfo;	 Catch:{ OutOfResourcesException -> 0x0057 }
        r6 = r5.mThreadedRenderer;	 Catch:{ OutOfResourcesException -> 0x0057 }
        r7 = r12.mWidth;	 Catch:{ OutOfResourcesException -> 0x0057 }
        r8 = r12.mHeight;	 Catch:{ OutOfResourcesException -> 0x0057 }
        r9 = r12.mAttachInfo;	 Catch:{ OutOfResourcesException -> 0x0057 }
        r10 = r12.mSurface;	 Catch:{ OutOfResourcesException -> 0x0057 }
        r6.initializeIfNeeded(r7, r8, r9, r10, r11);	 Catch:{ OutOfResourcesException -> 0x0057 }
        goto L_0x0086;
    L_0x0057:
        r0 = move-exception;
        r3 = r12.mTag;
        r4 = "OutOfResourcesException locking surface";
        android.util.Log.e(r3, r4, r0);
        r3 = r12.mWindowSession;	 Catch:{ RemoteException -> 0x0078 }
        r4 = r12.mWindow;	 Catch:{ RemoteException -> 0x0078 }
        r3 = r3.outOfMemory(r4);	 Catch:{ RemoteException -> 0x0078 }
        if (r3 != 0) goto L_0x0077;
    L_0x0069:
        r3 = r12.mTag;	 Catch:{ RemoteException -> 0x0078 }
        r4 = "No processes killed for memory; killing self";
        android.util.Slog.w(r3, r4);	 Catch:{ RemoteException -> 0x0078 }
        r3 = android.os.Process.myPid();	 Catch:{ RemoteException -> 0x0078 }
        android.os.Process.killProcess(r3);	 Catch:{ RemoteException -> 0x0078 }
    L_0x0077:
        goto L_0x0079;
    L_0x0078:
        r3 = move-exception;
    L_0x0079:
        r3 = r12.mHandler;
        r4 = 6;
        r4 = r3.obtainMessage(r4);
        r5 = 500; // 0x1f4 float:7.0E-43 double:2.47E-321;
        r3.sendMessageDelayed(r4, r5);
        return;
    L_0x0086:
        r4 = r12.mAttachInfo;
        r4.mHasWindowFocus = r1;
        r4 = r12.mWindowAttributes;
        r4 = r4.flags;
        r4 = android.view.WindowManager.LayoutParams.mayUseInputMethod(r4);
        r12.mLastWasImTarget = r4;
        r4 = r12.mContext;
        r5 = android.view.inputmethod.InputMethodManager.class;
        r4 = r4.getSystemService(r5);
        r4 = (android.view.inputmethod.InputMethodManager) r4;
        if (r4 == 0) goto L_0x00af;
    L_0x00a0:
        r5 = r12.mLastWasImTarget;
        if (r5 == 0) goto L_0x00af;
    L_0x00a4:
        r5 = r12.isInLocalFocusMode();
        if (r5 != 0) goto L_0x00af;
    L_0x00aa:
        r5 = r12.mView;
        r4.onPreWindowFocus(r5, r1);
    L_0x00af:
        r5 = r12.mView;
        if (r5 == 0) goto L_0x00d3;
    L_0x00b3:
        r5 = r12.mAttachInfo;
        r5 = r5.mKeyDispatchState;
        r5.reset();
        r5 = r12.mView;
        r5.dispatchWindowFocusChanged(r1);
        r5 = r12.mAttachInfo;
        r5 = r5.mTreeObserver;
        r5.dispatchOnWindowFocusChange(r1);
        r5 = r12.mAttachInfo;
        r5 = r5.mTooltipHost;
        if (r5 == 0) goto L_0x00d3;
    L_0x00cc:
        r5 = r12.mAttachInfo;
        r5 = r5.mTooltipHost;
        r5.hideTooltip();
    L_0x00d3:
        if (r1 == 0) goto L_0x0113;
    L_0x00d5:
        if (r4 == 0) goto L_0x00f7;
    L_0x00d7:
        r0 = r12.mLastWasImTarget;
        if (r0 == 0) goto L_0x00f7;
    L_0x00db:
        r0 = r12.isInLocalFocusMode();
        if (r0 != 0) goto L_0x00f7;
    L_0x00e1:
        r6 = r12.mView;
        r7 = r6.findFocus();
        r0 = r12.mWindowAttributes;
        r8 = r0.softInputMode;
        r0 = r12.mHasHadWindowFocus;
        r9 = r0 ^ 1;
        r0 = r12.mWindowAttributes;
        r10 = r0.flags;
        r5 = r4;
        r5.onPostWindowFocus(r6, r7, r8, r9, r10);
    L_0x00f7:
        r0 = r12.mWindowAttributes;
        r5 = r0.softInputMode;
        r5 = r5 & -257;
        r0.softInputMode = r5;
        r0 = r12.mView;
        r0 = r0.getLayoutParams();
        r0 = (android.view.WindowManager.LayoutParams) r0;
        r5 = r0.softInputMode;
        r5 = r5 & -257;
        r0.softInputMode = r5;
        r12.mHasHadWindowFocus = r3;
        r12.fireAccessibilityFocusEventIfHasFocusedNode();
        goto L_0x011a;
    L_0x0113:
        r3 = r12.mPointerCapture;
        if (r3 == 0) goto L_0x011a;
    L_0x0117:
        r12.handlePointerCaptureChanged(r0);
    L_0x011a:
        r0 = r12.mFirstInputStage;
        r0.onWindowFocusChanged(r1);
        return;
    L_0x0120:
        r0 = move-exception;
        monitor-exit(r12);	 Catch:{ all -> 0x0120 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewRootImpl.handleWindowFocusChanged():void");
    }

    private void fireAccessibilityFocusEventIfHasFocusedNode() {
        if (AccessibilityManager.getInstance(this.mContext).isEnabled()) {
            View focusedView = this.mView.findFocus();
            if (focusedView != null) {
                AccessibilityNodeProvider provider = focusedView.getAccessibilityNodeProvider();
                if (provider == null) {
                    focusedView.sendAccessibilityEvent(8);
                } else {
                    AccessibilityNodeInfo focusedNode = findFocusedVirtualNode(provider);
                    if (focusedNode != null) {
                        int virtualId = AccessibilityNodeInfo.getVirtualDescendantId(focusedNode.getSourceNodeId());
                        AccessibilityEvent event = AccessibilityEvent.obtain(8);
                        event.setSource(focusedView, virtualId);
                        event.setPackageName(focusedNode.getPackageName());
                        event.setChecked(focusedNode.isChecked());
                        event.setContentDescription(focusedNode.getContentDescription());
                        event.setPassword(focusedNode.isPassword());
                        event.getText().add(focusedNode.getText());
                        event.setEnabled(focusedNode.isEnabled());
                        focusedView.getParent().requestSendAccessibilityEvent(focusedView, event);
                        focusedNode.recycle();
                    }
                }
            }
        }
    }

    private AccessibilityNodeInfo findFocusedVirtualNode(AccessibilityNodeProvider provider) {
        AccessibilityNodeInfo focusedNode = provider.findFocus(1);
        if (focusedNode != null) {
            return focusedNode;
        }
        if (!this.mContext.isAutofillCompatibilityEnabled()) {
            return null;
        }
        AccessibilityNodeInfo current = provider.createAccessibilityNodeInfo(-1);
        if (current.isFocused()) {
            return current;
        }
        Queue<AccessibilityNodeInfo> fringe = new LinkedList();
        fringe.offer(current);
        while (!fringe.isEmpty()) {
            current = (AccessibilityNodeInfo) fringe.poll();
            LongArray childNodeIds = current.getChildNodeIds();
            if (childNodeIds != null) {
                if (childNodeIds.size() > 0) {
                    int childCount = childNodeIds.size();
                    for (int i = 0; i < childCount; i++) {
                        AccessibilityNodeInfo child = provider.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(childNodeIds.get(i)));
                        if (child != null) {
                            if (child.isFocused()) {
                                return child;
                            }
                            fringe.offer(child);
                        }
                    }
                    current.recycle();
                }
            }
        }
        return null;
    }

    private void handleOutOfResourcesException(OutOfResourcesException e) {
        Log.e(this.mTag, "OutOfResourcesException initializing HW surface", e);
        try {
            if (!(this.mWindowSession.outOfMemory(this.mWindow) || Process.myUid() == 1000)) {
                Slog.w(this.mTag, "No processes killed for memory; killing self");
                Process.killProcess(Process.myPid());
            }
        } catch (RemoteException e2) {
        }
        this.mLayoutRequested = true;
    }

    private void performMeasure(int childWidthMeasureSpec, int childHeightMeasureSpec) {
        if (this.mView != null) {
            Trace.traceBegin(8, "measure");
            try {
                this.mView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
            } finally {
                Trace.traceEnd(8);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isInLayout() {
        return this.mInLayout;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean requestLayoutDuringLayout(View view) {
        if (view.mParent == null || view.mAttachInfo == null) {
            return true;
        }
        if (!this.mLayoutRequesters.contains(view)) {
            this.mLayoutRequesters.add(view);
        }
        if (this.mHandlingLayoutInLayoutRequest) {
            return false;
        }
        return true;
    }

    private void performLayout(LayoutParams lp, int desiredWindowWidth, int desiredWindowHeight) {
        this.mLayoutRequested = false;
        this.mScrollMayChange = true;
        this.mInLayout = true;
        View host = this.mView;
        if (host != null) {
            Trace.traceBegin(8, TtmlUtils.TAG_LAYOUT);
            try {
                host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
                this.mInLayout = false;
                if (this.mLayoutRequesters.size() > 0) {
                    ArrayList<View> validLayoutRequesters = getValidLayoutRequesters(this.mLayoutRequesters, false);
                    if (validLayoutRequesters != null) {
                        this.mHandlingLayoutInLayoutRequest = true;
                        int numValidRequests = validLayoutRequesters.size();
                        for (int i = 0; i < numValidRequests; i++) {
                            View view = (View) validLayoutRequesters.get(i);
                            StringBuilder stringBuilder = new StringBuilder();
                            stringBuilder.append("requestLayout() improperly called by ");
                            stringBuilder.append(view);
                            stringBuilder.append(" during layout: running second layout pass");
                            Log.w("View", stringBuilder.toString());
                            view.requestLayout();
                        }
                        measureHierarchy(host, lp, this.mView.getContext().getResources(), desiredWindowWidth, desiredWindowHeight);
                        this.mInLayout = true;
                        host.layout(0, 0, host.getMeasuredWidth(), host.getMeasuredHeight());
                        this.mHandlingLayoutInLayoutRequest = false;
                        ArrayList<View> validLayoutRequesters2 = getValidLayoutRequesters(this.mLayoutRequesters, true);
                        if (validLayoutRequesters2 != null) {
                            final ArrayList<View> finalRequesters = validLayoutRequesters2;
                            getRunQueue().post(new Runnable() {
                                public void run() {
                                    int numValidRequests = finalRequesters.size();
                                    for (int i = 0; i < numValidRequests; i++) {
                                        View view = (View) finalRequesters.get(i);
                                        StringBuilder stringBuilder = new StringBuilder();
                                        stringBuilder.append("requestLayout() improperly called by ");
                                        stringBuilder.append(view);
                                        stringBuilder.append(" during second layout pass: posting in next frame");
                                        Log.w("View", stringBuilder.toString());
                                        view.requestLayout();
                                    }
                                }
                            });
                        }
                    }
                }
                Trace.traceEnd(8);
                this.mInLayout = false;
            } catch (Throwable th) {
                Trace.traceEnd(8);
            }
        }
    }

    private ArrayList<View> getValidLayoutRequesters(ArrayList<View> layoutRequesters, boolean secondLayoutRequests) {
        int i;
        View view;
        int numViewsRequestingLayout = layoutRequesters.size();
        ArrayList<View> validLayoutRequesters = null;
        for (i = 0; i < numViewsRequestingLayout; i++) {
            view = (View) layoutRequesters.get(i);
            if (!(view == null || view.mAttachInfo == null || view.mParent == null || (!secondLayoutRequests && (view.mPrivateFlags & 4096) != 4096))) {
                boolean gone = false;
                View parent = view;
                while (parent != null) {
                    if ((parent.mViewFlags & 12) == 8) {
                        gone = true;
                        break;
                    } else if (parent.mParent instanceof View) {
                        parent = (View) parent.mParent;
                    } else {
                        parent = null;
                    }
                }
                if (!gone) {
                    if (validLayoutRequesters == null) {
                        validLayoutRequesters = new ArrayList();
                    }
                    validLayoutRequesters.add(view);
                }
            }
        }
        if (!secondLayoutRequests) {
            for (i = 0; i < numViewsRequestingLayout; i++) {
                view = (View) layoutRequesters.get(i);
                while (view != null && (view.mPrivateFlags & 4096) != 0) {
                    view.mPrivateFlags &= -4097;
                    if (view.mParent instanceof View) {
                        view = (View) view.mParent;
                    } else {
                        view = null;
                    }
                }
            }
        }
        layoutRequesters.clear();
        return validLayoutRequesters;
    }

    public void requestTransparentRegion(View child) {
        checkThread();
        View view = this.mView;
        if (view == child) {
            view.mPrivateFlags |= 512;
            this.mWindowAttributesChanged = true;
            this.mWindowAttributesChangesFlag = 0;
            requestLayout();
        }
    }

    private static int getRootMeasureSpec(int windowSize, int rootDimension) {
        if (rootDimension == -2) {
            return MeasureSpec.makeMeasureSpec(windowSize, Integer.MIN_VALUE);
        }
        if (rootDimension != -1) {
            return MeasureSpec.makeMeasureSpec(rootDimension, 1073741824);
        }
        return MeasureSpec.makeMeasureSpec(windowSize, 1073741824);
    }

    public void onPreDraw(RecordingCanvas canvas) {
        if (!(this.mCurScrollY == 0 || this.mHardwareYOffset == 0 || !this.mAttachInfo.mThreadedRenderer.isOpaque())) {
            canvas.drawColor(-16777216);
        }
        canvas.translate((float) (-this.mHardwareXOffset), (float) (-this.mHardwareYOffset));
    }

    public void onPostDraw(RecordingCanvas canvas) {
        drawAccessibilityFocusedDrawableIfNeeded(canvas);
        if (this.mUseMTRenderer) {
            for (int i = this.mWindowCallbacks.size() - 1; i >= 0; i--) {
                ((WindowCallbacks) this.mWindowCallbacks.get(i)).onPostDraw(canvas);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void outputDisplayList(View view) {
        view.mRenderNode.output();
    }

    private void profileRendering(boolean enabled) {
        if (this.mProfileRendering) {
            this.mRenderProfilingEnabled = enabled;
            FrameCallback frameCallback = this.mRenderProfiler;
            if (frameCallback != null) {
                this.mChoreographer.removeFrameCallback(frameCallback);
            }
            if (this.mRenderProfilingEnabled) {
                if (this.mRenderProfiler == null) {
                    this.mRenderProfiler = new FrameCallback() {
                        public void doFrame(long frameTimeNanos) {
                            ViewRootImpl.this.mDirty.set(0, 0, ViewRootImpl.this.mWidth, ViewRootImpl.this.mHeight);
                            ViewRootImpl.this.scheduleTraversals();
                            if (ViewRootImpl.this.mRenderProfilingEnabled) {
                                ViewRootImpl.this.mChoreographer.postFrameCallback(ViewRootImpl.this.mRenderProfiler);
                            }
                        }
                    };
                }
                this.mChoreographer.postFrameCallback(this.mRenderProfiler);
                return;
            }
            this.mRenderProfiler = null;
        }
    }

    private void trackFPS() {
        long nowTime = System.currentTimeMillis();
        if (this.mFpsStartTime < 0) {
            this.mFpsPrevTime = nowTime;
            this.mFpsStartTime = nowTime;
            this.mFpsNumFrames = 0;
            return;
        }
        this.mFpsNumFrames++;
        String thisHash = Integer.toHexString(System.identityHashCode(this));
        long frameTime = nowTime - this.mFpsPrevTime;
        long totalTime = nowTime - this.mFpsStartTime;
        String str = this.mTag;
        StringBuilder stringBuilder = new StringBuilder();
        String str2 = "0x";
        stringBuilder.append(str2);
        stringBuilder.append(thisHash);
        stringBuilder.append("\tFrame time:\t");
        stringBuilder.append(frameTime);
        Log.v(str, stringBuilder.toString());
        this.mFpsPrevTime = nowTime;
        if (totalTime > 1000) {
            float fps = (((float) this.mFpsNumFrames) * 1000.0f) / ((float) totalTime);
            String str3 = this.mTag;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(str2);
            stringBuilder2.append(thisHash);
            stringBuilder2.append("\tFPS:\t");
            stringBuilder2.append(fps);
            Log.v(str3, stringBuilder2.toString());
            this.mFpsStartTime = nowTime;
            this.mFpsNumFrames = 0;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void drawPending() {
        this.mDrawsNeededToReport++;
    }

    /* Access modifiers changed, original: 0000 */
    public void pendingDrawFinished() {
        int i = this.mDrawsNeededToReport;
        if (i != 0) {
            this.mDrawsNeededToReport = i - 1;
            if (this.mDrawsNeededToReport == 0) {
                reportDrawFinished();
                return;
            }
            return;
        }
        throw new RuntimeException("Unbalanced drawPending/pendingDrawFinished calls");
    }

    private void postDrawFinished() {
        this.mHandler.sendEmptyMessage(29);
    }

    private void reportDrawFinished() {
        try {
            this.mDrawsNeededToReport = 0;
            this.mWindowSession.finishDrawing(this.mWindow);
        } catch (RemoteException e) {
        }
    }

    private void performDraw() {
        if ((this.mAttachInfo.mDisplayState != 1 || this.mReportNextDraw) && this.mView != null) {
            boolean fullRedrawNeeded = this.mFullRedrawNeeded || this.mReportNextDraw;
            this.mFullRedrawNeeded = false;
            this.mIsDrawing = true;
            Trace.traceBegin(8, "draw");
            boolean usingAsyncReport = false;
            if (this.mAttachInfo.mThreadedRenderer != null && this.mAttachInfo.mThreadedRenderer.isEnabled()) {
                ArrayList<Runnable> commitCallbacks = this.mAttachInfo.mTreeObserver.captureFrameCommitCallbacks();
                if (this.mReportNextDraw) {
                    usingAsyncReport = true;
                    this.mAttachInfo.mThreadedRenderer.setFrameCompleteCallback(new -$$Lambda$ViewRootImpl$YBiqAhbCbXVPSKdbE3K4rH2gpxI(this, this.mAttachInfo.mHandler, commitCallbacks));
                } else if (commitCallbacks != null && commitCallbacks.size() > 0) {
                    this.mAttachInfo.mThreadedRenderer.setFrameCompleteCallback(new -$$Lambda$ViewRootImpl$zlBUjCwDtoAWMNaHI62DIq-eKFY(this.mAttachInfo.mHandler, commitCallbacks));
                }
            }
            try {
                boolean canUseAsync = draw(fullRedrawNeeded);
                if (usingAsyncReport && !canUseAsync) {
                    this.mAttachInfo.mThreadedRenderer.setFrameCompleteCallback(null);
                    usingAsyncReport = false;
                }
                this.mIsDrawing = false;
                Trace.traceEnd(8);
                if (this.mAttachInfo.mPendingAnimatingRenderNodes != null) {
                    int count = this.mAttachInfo.mPendingAnimatingRenderNodes.size();
                    for (int i = 0; i < count; i++) {
                        ((RenderNode) this.mAttachInfo.mPendingAnimatingRenderNodes.get(i)).endAllAnimators();
                    }
                    this.mAttachInfo.mPendingAnimatingRenderNodes.clear();
                }
                if (this.mReportNextDraw) {
                    this.mReportNextDraw = false;
                    CountDownLatch countDownLatch = this.mWindowDrawCountDown;
                    if (countDownLatch != null) {
                        try {
                            countDownLatch.await();
                        } catch (InterruptedException e) {
                            Log.e(this.mTag, "Window redraw count down interrupted!");
                        }
                        this.mWindowDrawCountDown = null;
                    }
                    if (this.mAttachInfo.mThreadedRenderer != null) {
                        this.mAttachInfo.mThreadedRenderer.setStopped(this.mStopped);
                    }
                    if (this.mSurfaceHolder != null && this.mSurface.isValid()) {
                        new SurfaceCallbackHelper(new -$$Lambda$ViewRootImpl$dznxCZGM2R1fsBljsJKomLjBRoM(this)).dispatchSurfaceRedrawNeededAsync(this.mSurfaceHolder, this.mSurfaceHolder.getCallbacks());
                    } else if (!usingAsyncReport) {
                        if (this.mAttachInfo.mThreadedRenderer != null) {
                            this.mAttachInfo.mThreadedRenderer.fence();
                        }
                        pendingDrawFinished();
                    }
                }
            } catch (Throwable th) {
                this.mIsDrawing = false;
                Trace.traceEnd(8);
            }
        }
    }

    public /* synthetic */ void lambda$performDraw$2$ViewRootImpl(Handler handler, ArrayList commitCallbacks, long frameNr) {
        handler.postAtFrontOfQueue(new -$$Lambda$ViewRootImpl$7A_3tkr_Kw4TZAeIUGVlOoTcZhg(this, commitCallbacks));
    }

    public /* synthetic */ void lambda$performDraw$1$ViewRootImpl(ArrayList commitCallbacks) {
        pendingDrawFinished();
        if (commitCallbacks != null) {
            for (int i = 0; i < commitCallbacks.size(); i++) {
                ((Runnable) commitCallbacks.get(i)).run();
            }
        }
    }

    static /* synthetic */ void lambda$performDraw$3(ArrayList commitCallbacks) {
        for (int i = 0; i < commitCallbacks.size(); i++) {
            ((Runnable) commitCallbacks.get(i)).run();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:121:0x025a  */
    /* JADX WARNING: Removed duplicated region for block: B:124:0x025e  */
    private boolean draw(boolean r26) {
        /*
        r25 = this;
        r9 = r25;
        r10 = r9.mSurface;
        r0 = r10.isValid();
        r11 = 0;
        if (r0 != 0) goto L_0x000c;
    L_0x000b:
        return r11;
    L_0x000c:
        r0 = sFirstDrawComplete;
        r12 = 1;
        if (r0 != 0) goto L_0x0034;
    L_0x0011:
        r1 = sFirstDrawHandlers;
        monitor-enter(r1);
        sFirstDrawComplete = r12;	 Catch:{ all -> 0x0031 }
        r0 = sFirstDrawHandlers;	 Catch:{ all -> 0x0031 }
        r0 = r0.size();	 Catch:{ all -> 0x0031 }
        r2 = 0;
    L_0x001d:
        if (r2 >= r0) goto L_0x002f;
    L_0x001f:
        r3 = r9.mHandler;	 Catch:{ all -> 0x0031 }
        r4 = sFirstDrawHandlers;	 Catch:{ all -> 0x0031 }
        r4 = r4.get(r2);	 Catch:{ all -> 0x0031 }
        r4 = (java.lang.Runnable) r4;	 Catch:{ all -> 0x0031 }
        r3.post(r4);	 Catch:{ all -> 0x0031 }
        r2 = r2 + 1;
        goto L_0x001d;
    L_0x002f:
        monitor-exit(r1);	 Catch:{ all -> 0x0031 }
        goto L_0x0034;
    L_0x0031:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0031 }
        throw r0;
    L_0x0034:
        r0 = 0;
        r9.scrollToRectOrFocus(r0, r11);
        r1 = r9.mAttachInfo;
        r1 = r1.mViewScrollChanged;
        if (r1 == 0) goto L_0x0047;
    L_0x003e:
        r1 = r9.mAttachInfo;
        r1.mViewScrollChanged = r11;
        r1 = r1.mTreeObserver;
        r1.dispatchOnScrollChanged();
    L_0x0047:
        r1 = r9.mScroller;
        if (r1 == 0) goto L_0x0053;
    L_0x004b:
        r1 = r1.computeScrollOffset();
        if (r1 == 0) goto L_0x0053;
    L_0x0051:
        r1 = r12;
        goto L_0x0054;
    L_0x0053:
        r1 = r11;
    L_0x0054:
        r13 = r1;
        if (r13 == 0) goto L_0x005f;
    L_0x0057:
        r1 = r9.mScroller;
        r1 = r1.getCurrY();
        r14 = r1;
        goto L_0x0062;
    L_0x005f:
        r1 = r9.mScrollY;
        r14 = r1;
    L_0x0062:
        r1 = r9.mCurScrollY;
        if (r1 == r14) goto L_0x0078;
    L_0x0066:
        r9.mCurScrollY = r14;
        r1 = 1;
        r2 = r9.mView;
        r3 = r2 instanceof com.android.internal.view.RootViewSurfaceTaker;
        if (r3 == 0) goto L_0x0076;
    L_0x006f:
        r2 = (com.android.internal.view.RootViewSurfaceTaker) r2;
        r3 = r9.mCurScrollY;
        r2.onRootViewScrollYChanged(r3);
    L_0x0076:
        r15 = r1;
        goto L_0x007a;
    L_0x0078:
        r15 = r26;
    L_0x007a:
        r1 = r9.mAttachInfo;
        r8 = r1.mApplicationScale;
        r1 = r9.mAttachInfo;
        r7 = r1.mScalingRequired;
        r6 = r9.mDirty;
        r1 = r9.mSurfaceHolder;
        if (r1 == 0) goto L_0x0095;
    L_0x0088:
        r6.setEmpty();
        if (r13 == 0) goto L_0x0094;
    L_0x008d:
        r0 = r9.mScroller;
        if (r0 == 0) goto L_0x0094;
    L_0x0091:
        r0.abortAnimation();
    L_0x0094:
        return r11;
    L_0x0095:
        if (r15 == 0) goto L_0x00a8;
    L_0x0097:
        r1 = r9.mWidth;
        r1 = (float) r1;
        r1 = r1 * r8;
        r2 = 1056964608; // 0x3f000000 float:0.5 double:5.222099017E-315;
        r1 = r1 + r2;
        r1 = (int) r1;
        r3 = r9.mHeight;
        r3 = (float) r3;
        r3 = r3 * r8;
        r3 = r3 + r2;
        r2 = (int) r3;
        r6.set(r11, r11, r1, r2);
    L_0x00a8:
        r1 = r9.mAttachInfo;
        r1 = r1.mTreeObserver;
        r1.dispatchOnDraw();
        r1 = r9.mCanvasOffsetX;
        r1 = -r1;
        r2 = r9.mCanvasOffsetY;
        r2 = -r2;
        r2 = r2 + r14;
        r5 = r9.mWindowAttributes;
        if (r5 == 0) goto L_0x00bc;
    L_0x00ba:
        r0 = r5.surfaceInsets;
    L_0x00bc:
        r4 = r0;
        if (r4 == 0) goto L_0x00cf;
    L_0x00bf:
        r0 = r4.left;
        r1 = r1 - r0;
        r0 = r4.top;
        r2 = r2 - r0;
        r0 = r4.left;
        r3 = r4.right;
        r6.offset(r0, r3);
        r3 = r2;
        r2 = r1;
        goto L_0x00d1;
    L_0x00cf:
        r3 = r2;
        r2 = r1;
    L_0x00d1:
        r0 = 0;
        r1 = r9.mAttachInfo;
        r1 = r1.mAccessibilityFocusDrawable;
        if (r1 == 0) goto L_0x00f2;
    L_0x00d8:
        r12 = r9.mAttachInfo;
        r12 = r12.mTmpInvalRect;
        r16 = r9.getAccessibilityFocusedRect(r12);
        if (r16 != 0) goto L_0x00e5;
    L_0x00e2:
        r12.setEmpty();
    L_0x00e5:
        r11 = r1.getBounds();
        r11 = r12.equals(r11);
        if (r11 != 0) goto L_0x00f2;
    L_0x00ef:
        r0 = 1;
        r11 = r0;
        goto L_0x00f3;
    L_0x00f2:
        r11 = r0;
    L_0x00f3:
        r0 = r9.mAttachInfo;
        r12 = r9.mChoreographer;
        r16 = r12.getFrameTimeNanos();
        r18 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r12 = r14;
        r26 = r15;
        r14 = r16 / r18;
        r0.mDrawingTime = r14;
        r14 = 0;
        r0 = r6.isEmpty();
        if (r0 == 0) goto L_0x0124;
    L_0x010c:
        r0 = r9.mIsAnimating;
        if (r0 != 0) goto L_0x0124;
    L_0x0110:
        if (r11 == 0) goto L_0x0113;
    L_0x0112:
        goto L_0x0124;
    L_0x0113:
        r15 = r1;
        r16 = r2;
        r17 = r3;
        r18 = r4;
        r19 = r5;
        r20 = r6;
        r21 = r7;
        r22 = r8;
        goto L_0x025c;
    L_0x0124:
        r0 = r9.mAttachInfo;
        r0 = r0.mThreadedRenderer;
        if (r0 == 0) goto L_0x01e4;
    L_0x012a:
        r0 = r9.mAttachInfo;
        r0 = r0.mThreadedRenderer;
        r0 = r0.isEnabled();
        if (r0 == 0) goto L_0x01e4;
    L_0x0134:
        if (r11 != 0) goto L_0x013d;
    L_0x0136:
        r0 = r9.mInvalidateRootRequested;
        if (r0 == 0) goto L_0x013b;
    L_0x013a:
        goto L_0x013d;
    L_0x013b:
        r0 = 0;
        goto L_0x013e;
    L_0x013d:
        r0 = 1;
    L_0x013e:
        r15 = 0;
        r9.mInvalidateRootRequested = r15;
        r9.mIsAnimating = r15;
        r15 = r9.mHardwareYOffset;
        if (r15 != r3) goto L_0x014b;
    L_0x0147:
        r15 = r9.mHardwareXOffset;
        if (r15 == r2) goto L_0x0150;
    L_0x014b:
        r9.mHardwareYOffset = r3;
        r9.mHardwareXOffset = r2;
        r0 = 1;
    L_0x0150:
        if (r0 == 0) goto L_0x0159;
    L_0x0152:
        r15 = r9.mAttachInfo;
        r15 = r15.mThreadedRenderer;
        r15.invalidateRoot();
    L_0x0159:
        r6.setEmpty();
        r15 = r25.updateContentDrawBounds();
        r16 = r0;
        r0 = r9.mReportNextDraw;
        if (r0 == 0) goto L_0x0171;
    L_0x0166:
        r0 = r9.mAttachInfo;
        r0 = r0.mThreadedRenderer;
        r22 = r1;
        r1 = 0;
        r0.setStopped(r1);
        goto L_0x0173;
    L_0x0171:
        r22 = r1;
    L_0x0173:
        if (r15 == 0) goto L_0x0178;
    L_0x0175:
        r25.requestDrawWindow();
    L_0x0178:
        r14 = 1;
        if (r4 == 0) goto L_0x01c3;
    L_0x017b:
        r0 = r4.top;
        r1 = r9.mAttachInfo;
        r1 = r1.mThreadedRenderer;
        r1 = r1.getInsetTop();
        if (r0 != r1) goto L_0x01b1;
    L_0x0187:
        r0 = r4.left;
        r1 = r9.mAttachInfo;
        r1 = r1.mThreadedRenderer;
        r1 = r1.getInsetLeft();
        if (r0 != r1) goto L_0x01b1;
    L_0x0193:
        r0 = r9.mWidth;
        r1 = r9.mAttachInfo;
        r1 = r1.mThreadedRenderer;
        r1 = r1.getWidth();
        if (r0 != r1) goto L_0x01b1;
    L_0x019f:
        r0 = r9.mHeight;
        r1 = r9.mAttachInfo;
        r1 = r1.mThreadedRenderer;
        r1 = r1.getHeight();
        if (r0 == r1) goto L_0x01ac;
    L_0x01ab:
        goto L_0x01b1;
    L_0x01ac:
        r23 = r2;
        r24 = r3;
        goto L_0x01c7;
    L_0x01b1:
        r0 = r9.mAttachInfo;
        r0 = r0.mThreadedRenderer;
        r1 = r9.mWidth;
        r23 = r2;
        r2 = r9.mHeight;
        r24 = r3;
        r3 = r9.mAttachInfo;
        r0.setup(r1, r2, r3, r4);
        goto L_0x01c7;
    L_0x01c3:
        r23 = r2;
        r24 = r3;
    L_0x01c7:
        r0 = r9.mAttachInfo;
        r0 = r0.mThreadedRenderer;
        r1 = r9.mView;
        r2 = r9.mAttachInfo;
        r0.draw(r1, r2, r9);
        r18 = r4;
        r19 = r5;
        r20 = r6;
        r21 = r7;
        r15 = r22;
        r16 = r23;
        r17 = r24;
        r22 = r8;
        goto L_0x025c;
    L_0x01e4:
        r22 = r1;
        r23 = r2;
        r24 = r3;
        r0 = r9.mAttachInfo;
        r0 = r0.mThreadedRenderer;
        if (r0 == 0) goto L_0x0236;
    L_0x01f0:
        r0 = r9.mAttachInfo;
        r0 = r0.mThreadedRenderer;
        r0 = r0.isEnabled();
        if (r0 != 0) goto L_0x0236;
    L_0x01fa:
        r0 = r9.mAttachInfo;
        r0 = r0.mThreadedRenderer;
        r0 = r0.isRequested();
        if (r0 == 0) goto L_0x0236;
    L_0x0204:
        r0 = r9.mSurface;
        r0 = r0.isValid();
        if (r0 == 0) goto L_0x0236;
    L_0x020c:
        r0 = r9.mAttachInfo;	 Catch:{ OutOfResourcesException -> 0x0230 }
        r0 = r0.mThreadedRenderer;	 Catch:{ OutOfResourcesException -> 0x0230 }
        r1 = r9.mWidth;	 Catch:{ OutOfResourcesException -> 0x0230 }
        r2 = r9.mHeight;	 Catch:{ OutOfResourcesException -> 0x0230 }
        r3 = r9.mAttachInfo;	 Catch:{ OutOfResourcesException -> 0x0230 }
        r15 = r9.mSurface;	 Catch:{ OutOfResourcesException -> 0x0230 }
        r16 = r0;
        r17 = r1;
        r18 = r2;
        r19 = r3;
        r20 = r15;
        r21 = r4;
        r16.initializeIfNeeded(r17, r18, r19, r20, r21);	 Catch:{ OutOfResourcesException -> 0x0230 }
        r0 = 1;
        r9.mFullRedrawNeeded = r0;
        r25.scheduleTraversals();
        r1 = 0;
        return r1;
    L_0x0230:
        r0 = move-exception;
        r9.handleOutOfResourcesException(r0);
        r1 = 0;
        return r1;
    L_0x0236:
        r3 = r9.mAttachInfo;
        r15 = r22;
        r1 = r25;
        r16 = r23;
        r2 = r10;
        r17 = r24;
        r18 = r4;
        r4 = r16;
        r19 = r5;
        r5 = r17;
        r20 = r6;
        r6 = r7;
        r21 = r7;
        r7 = r20;
        r22 = r8;
        r8 = r18;
        r0 = r1.drawSoftware(r2, r3, r4, r5, r6, r7, r8);
        if (r0 != 0) goto L_0x025c;
    L_0x025a:
        r1 = 0;
        return r1;
    L_0x025c:
        if (r13 == 0) goto L_0x0264;
    L_0x025e:
        r0 = 1;
        r9.mFullRedrawNeeded = r0;
        r25.scheduleTraversals();
    L_0x0264:
        return r14;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewRootImpl.draw(boolean):boolean");
    }

    /* JADX WARNING: Missing block: B:37:0x00b8, code skipped:
            r5.offset(r8, r9);
     */
    private boolean drawSoftware(android.view.Surface r17, android.view.View.AttachInfo r18, int r19, int r20, boolean r21, android.graphics.Rect r22, android.graphics.Rect r23) {
        /*
        r16 = this;
        r1 = r16;
        r2 = r17;
        r3 = r19;
        r4 = r20;
        r5 = r22;
        r6 = r23;
        r7 = "Could not unlock surface";
        r0 = r19;
        r8 = r20;
        if (r6 == 0) goto L_0x001d;
    L_0x0014:
        r9 = r6.left;
        r0 = r0 + r9;
        r9 = r6.top;
        r8 = r8 + r9;
        r9 = r8;
        r8 = r0;
        goto L_0x001f;
    L_0x001d:
        r9 = r8;
        r8 = r0;
    L_0x001f:
        r0 = -r8;
        r10 = -r9;
        r12 = 0;
        r5.offset(r0, r10);	 Catch:{ OutOfResourcesException -> 0x00af, IllegalArgumentException -> 0x009f }
        r0 = r5.left;	 Catch:{ OutOfResourcesException -> 0x00af, IllegalArgumentException -> 0x009f }
        r10 = r5.top;	 Catch:{ OutOfResourcesException -> 0x00af, IllegalArgumentException -> 0x009f }
        r13 = r5.right;	 Catch:{ OutOfResourcesException -> 0x00af, IllegalArgumentException -> 0x009f }
        r14 = r5.bottom;	 Catch:{ OutOfResourcesException -> 0x00af, IllegalArgumentException -> 0x009f }
        r15 = r1.mSurface;	 Catch:{ OutOfResourcesException -> 0x00af, IllegalArgumentException -> 0x009f }
        r15 = r15.lockCanvas(r5);	 Catch:{ OutOfResourcesException -> 0x00af, IllegalArgumentException -> 0x009f }
        r11 = r1.mDensity;	 Catch:{ OutOfResourcesException -> 0x00af, IllegalArgumentException -> 0x009f }
        r15.setDensity(r11);	 Catch:{ OutOfResourcesException -> 0x00af, IllegalArgumentException -> 0x009f }
        r5.offset(r8, r9);
        r0 = r15.isOpaque();	 Catch:{ all -> 0x008b }
        if (r0 == 0) goto L_0x0046;
    L_0x0042:
        if (r4 != 0) goto L_0x0046;
    L_0x0044:
        if (r3 == 0) goto L_0x004b;
    L_0x0046:
        r0 = android.graphics.PorterDuff.Mode.CLEAR;	 Catch:{ all -> 0x008b }
        r15.drawColor(r12, r0);	 Catch:{ all -> 0x008b }
    L_0x004b:
        r22.setEmpty();	 Catch:{ all -> 0x008b }
        r1.mIsAnimating = r12;	 Catch:{ all -> 0x008b }
        r0 = r1.mView;	 Catch:{ all -> 0x008b }
        r10 = r0.mPrivateFlags;	 Catch:{ all -> 0x008b }
        r10 = r10 | 32;
        r0.mPrivateFlags = r10;	 Catch:{ all -> 0x008b }
        r0 = -r3;
        r0 = (float) r0;	 Catch:{ all -> 0x008b }
        r10 = -r4;
        r10 = (float) r10;	 Catch:{ all -> 0x008b }
        r15.translate(r0, r10);	 Catch:{ all -> 0x008b }
        r0 = r1.mTranslator;	 Catch:{ all -> 0x008b }
        if (r0 == 0) goto L_0x0068;
    L_0x0063:
        r0 = r1.mTranslator;	 Catch:{ all -> 0x008b }
        r0.translateCanvas(r15);	 Catch:{ all -> 0x008b }
    L_0x0068:
        if (r21 == 0) goto L_0x006d;
    L_0x006a:
        r0 = r1.mNoncompatDensity;	 Catch:{ all -> 0x008b }
        goto L_0x006e;
    L_0x006d:
        r0 = r12;
    L_0x006e:
        r15.setScreenDensity(r0);	 Catch:{ all -> 0x008b }
        r0 = r1.mView;	 Catch:{ all -> 0x008b }
        r0.draw(r15);	 Catch:{ all -> 0x008b }
        r1.drawAccessibilityFocusedDrawableIfNeeded(r15);	 Catch:{ all -> 0x008b }
        r2.unlockCanvasAndPost(r15);	 Catch:{ IllegalArgumentException -> 0x007f }
        r10 = 1;
        return r10;
    L_0x007f:
        r0 = move-exception;
        r10 = 1;
        r11 = r0;
        r0 = r11;
        r11 = r1.mTag;
        android.util.Log.e(r11, r7, r0);
        r1.mLayoutRequested = r10;
    L_0x008a:
        return r12;
    L_0x008b:
        r0 = move-exception;
        r2.unlockCanvasAndPost(r15);	 Catch:{ IllegalArgumentException -> 0x0091 }
        throw r0;
    L_0x0091:
        r0 = move-exception;
        r10 = r0;
        r0 = r10;
        r10 = r1.mTag;
        android.util.Log.e(r10, r7, r0);
        r7 = 1;
        r1.mLayoutRequested = r7;
        goto L_0x008a;
    L_0x009d:
        r0 = move-exception;
        goto L_0x00b8;
    L_0x009f:
        r0 = move-exception;
        r7 = r1.mTag;	 Catch:{ all -> 0x009d }
        r10 = "Could not lock surface";
        android.util.Log.e(r7, r10, r0);	 Catch:{ all -> 0x009d }
        r7 = 1;
        r1.mLayoutRequested = r7;	 Catch:{ all -> 0x009d }
        r5.offset(r8, r9);
        return r12;
    L_0x00af:
        r0 = move-exception;
        r1.handleOutOfResourcesException(r0);	 Catch:{ all -> 0x009d }
        r5.offset(r8, r9);
        return r12;
    L_0x00b8:
        r5.offset(r8, r9);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.ViewRootImpl.drawSoftware(android.view.Surface, android.view.View$AttachInfo, int, int, boolean, android.graphics.Rect, android.graphics.Rect):boolean");
    }

    private void drawAccessibilityFocusedDrawableIfNeeded(Canvas canvas) {
        Rect bounds = this.mAttachInfo.mTmpInvalRect;
        if (getAccessibilityFocusedRect(bounds)) {
            Drawable drawable = getAccessibilityFocusedDrawable();
            if (drawable != null) {
                drawable.setBounds(bounds);
                drawable.draw(canvas);
            }
        } else if (this.mAttachInfo.mAccessibilityFocusDrawable != null) {
            this.mAttachInfo.mAccessibilityFocusDrawable.setBounds(0, 0, 0, 0);
        }
    }

    private boolean getAccessibilityFocusedRect(Rect bounds) {
        AccessibilityManager manager = AccessibilityManager.getInstance(this.mView.mContext);
        if (!manager.isEnabled() || !manager.isTouchExplorationEnabled()) {
            return false;
        }
        View host = this.mAccessibilityFocusedHost;
        if (host == null || host.mAttachInfo == null) {
            return false;
        }
        if (host.getAccessibilityNodeProvider() == null) {
            host.getBoundsOnScreen(bounds, true);
        } else {
            AccessibilityNodeInfo accessibilityNodeInfo = this.mAccessibilityFocusedVirtualView;
            if (accessibilityNodeInfo == null) {
                return false;
            }
            accessibilityNodeInfo.getBoundsInScreen(bounds);
        }
        AttachInfo attachInfo = this.mAttachInfo;
        bounds.offset(0, attachInfo.mViewRootImpl.mScrollY);
        bounds.offset(-attachInfo.mWindowLeft, -attachInfo.mWindowTop);
        if (!bounds.intersect(0, 0, attachInfo.mViewRootImpl.mWidth, attachInfo.mViewRootImpl.mHeight)) {
            bounds.setEmpty();
        }
        return bounds.isEmpty() ^ 1;
    }

    private Drawable getAccessibilityFocusedDrawable() {
        if (this.mAttachInfo.mAccessibilityFocusDrawable == null) {
            TypedValue value = new TypedValue();
            if (this.mView.mContext.getTheme().resolveAttribute(R.attr.accessibilityFocusedDrawable, value, true)) {
                this.mAttachInfo.mAccessibilityFocusDrawable = this.mView.mContext.getDrawable(value.resourceId);
            }
        }
        return this.mAttachInfo.mAccessibilityFocusDrawable;
    }

    /* Access modifiers changed, original: 0000 */
    public void updateSystemGestureExclusionRectsForView(View view) {
        this.mGestureExclusionTracker.updateRectsForView(view);
        this.mHandler.sendEmptyMessage(32);
    }

    /* Access modifiers changed, original: 0000 */
    public void systemGestureExclusionChanged() {
        List<Rect> rectsForWindowManager = this.mGestureExclusionTracker.computeChangedRects();
        if (rectsForWindowManager != null && this.mView != null) {
            try {
                this.mWindowSession.reportSystemGestureExclusionChanged(this.mWindow, rectsForWindowManager);
                this.mAttachInfo.mTreeObserver.dispatchOnSystemGestureExclusionRectsChanged(rectsForWindowManager);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    public void setRootSystemGestureExclusionRects(List<Rect> rects) {
        this.mGestureExclusionTracker.setRootSystemGestureExclusionRects(rects);
        this.mHandler.sendEmptyMessage(32);
    }

    public List<Rect> getRootSystemGestureExclusionRects() {
        return this.mGestureExclusionTracker.getRootSystemGestureExclusionRects();
    }

    public void requestInvalidateRootRenderNode() {
        this.mInvalidateRootRequested = true;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean scrollToRectOrFocus(Rect rectangle, boolean immediate) {
        Rect ci = this.mAttachInfo.mContentInsets;
        Rect vi = this.mAttachInfo.mVisibleInsets;
        int scrollY = 0;
        boolean handled = false;
        if (vi.left > ci.left || vi.top > ci.top || vi.right > ci.right || vi.bottom > ci.bottom) {
            scrollY = this.mScrollY;
            View focus = this.mView.findFocus();
            if (focus == null) {
                return false;
            }
            WeakReference weakReference = this.mLastScrolledFocus;
            View lastScrolledFocus = weakReference != null ? (View) weakReference.get() : null;
            if (focus != lastScrolledFocus) {
                rectangle = null;
            }
            if (!(focus == lastScrolledFocus && !this.mScrollMayChange && rectangle == null)) {
                this.mLastScrolledFocus = new WeakReference(focus);
                this.mScrollMayChange = false;
                if (focus.getGlobalVisibleRect(this.mVisRect, null)) {
                    if (rectangle == null) {
                        focus.getFocusedRect(this.mTempRect);
                        View view = this.mView;
                        if (view instanceof ViewGroup) {
                            ((ViewGroup) view).offsetDescendantRectToMyCoords(focus, this.mTempRect);
                        }
                    } else {
                        this.mTempRect.set(rectangle);
                    }
                    if (this.mTempRect.intersect(this.mVisRect)) {
                        if (this.mTempRect.height() <= (this.mView.getHeight() - vi.top) - vi.bottom) {
                            if (this.mTempRect.top < vi.top) {
                                scrollY = this.mTempRect.top - vi.top;
                            } else if (this.mTempRect.bottom > this.mView.getHeight() - vi.bottom) {
                                scrollY = this.mTempRect.bottom - (this.mView.getHeight() - vi.bottom);
                            } else {
                                scrollY = 0;
                            }
                        }
                        handled = true;
                    }
                }
            }
        }
        if (scrollY != this.mScrollY) {
            Scroller scroller;
            if (immediate) {
                scroller = this.mScroller;
                if (scroller != null) {
                    scroller.abortAnimation();
                }
            } else {
                if (this.mScroller == null) {
                    this.mScroller = new Scroller(this.mView.getContext());
                }
                scroller = this.mScroller;
                int i = this.mScrollY;
                scroller.startScroll(0, i, 0, scrollY - i);
            }
            this.mScrollY = scrollY;
        }
        return handled;
    }

    @UnsupportedAppUsage
    public View getAccessibilityFocusedHost() {
        return this.mAccessibilityFocusedHost;
    }

    @UnsupportedAppUsage
    public AccessibilityNodeInfo getAccessibilityFocusedVirtualView() {
        return this.mAccessibilityFocusedVirtualView;
    }

    /* Access modifiers changed, original: 0000 */
    public void setAccessibilityFocus(View view, AccessibilityNodeInfo node) {
        if (this.mAccessibilityFocusedVirtualView != null) {
            AccessibilityNodeInfo focusNode = this.mAccessibilityFocusedVirtualView;
            View focusHost = this.mAccessibilityFocusedHost;
            this.mAccessibilityFocusedHost = null;
            this.mAccessibilityFocusedVirtualView = null;
            focusHost.clearAccessibilityFocusNoCallbacks(64);
            AccessibilityNodeProvider provider = focusHost.getAccessibilityNodeProvider();
            if (provider != null) {
                focusNode.getBoundsInParent(this.mTempRect);
                focusHost.invalidate(this.mTempRect);
                provider.performAction(AccessibilityNodeInfo.getVirtualDescendantId(focusNode.getSourceNodeId()), 128, null);
            }
            focusNode.recycle();
        }
        View view2 = this.mAccessibilityFocusedHost;
        if (!(view2 == null || view2 == view)) {
            view2.clearAccessibilityFocusNoCallbacks(64);
        }
        this.mAccessibilityFocusedHost = view;
        this.mAccessibilityFocusedVirtualView = node;
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.invalidateRoot();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasPointerCapture() {
        return this.mPointerCapture;
    }

    /* Access modifiers changed, original: 0000 */
    public void requestPointerCapture(boolean enabled) {
        if (this.mPointerCapture != enabled) {
            InputManager.getInstance().requestPointerCapture(this.mAttachInfo.mWindowToken, enabled);
        }
    }

    private void handlePointerCaptureChanged(boolean hasCapture) {
        if (this.mPointerCapture != hasCapture) {
            this.mPointerCapture = hasCapture;
            View view = this.mView;
            if (view != null) {
                view.dispatchPointerCaptureChanged(hasCapture);
            }
        }
    }

    private boolean hasColorModeChanged(int colorMode) {
        if (this.mAttachInfo.mThreadedRenderer == null) {
            return false;
        }
        boolean isWideGamut = colorMode == 1;
        if (this.mAttachInfo.mThreadedRenderer.isWideGamut() == isWideGamut) {
            return false;
        }
        if (!isWideGamut || this.mContext.getResources().getConfiguration().isScreenWideColorGamut()) {
            return true;
        }
        return false;
    }

    public void requestChildFocus(View child, View focused) {
        checkThread();
        scheduleTraversals();
    }

    public void clearChildFocus(View child) {
        checkThread();
        scheduleTraversals();
    }

    public ViewParent getParentForAccessibility() {
        return null;
    }

    public void focusableViewAvailable(View v) {
        checkThread();
        View view = this.mView;
        if (view == null) {
            return;
        }
        if (view.hasFocus()) {
            view = this.mView.findFocus();
            if ((view instanceof ViewGroup) && ((ViewGroup) view).getDescendantFocusability() == 262144 && isViewDescendantOf(v, view)) {
                v.requestFocus();
            }
        } else if (sAlwaysAssignFocus || !this.mAttachInfo.mInTouchMode) {
            v.requestFocus();
        }
    }

    public void recomputeViewAttributes(View child) {
        checkThread();
        if (this.mView == child) {
            this.mAttachInfo.mRecomputeGlobalAttributes = true;
            if (!this.mWillDrawSoon) {
                scheduleTraversals();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dispatchDetachedFromWindow() {
        InputStage inputStage = this.mFirstInputStage;
        if (inputStage != null) {
            inputStage.onDetachedFromWindow();
        }
        View view = this.mView;
        if (!(view == null || view.mAttachInfo == null)) {
            this.mAttachInfo.mTreeObserver.dispatchOnWindowAttachedChange(false);
            this.mView.dispatchDetachedFromWindow();
        }
        this.mAccessibilityInteractionConnectionManager.ensureNoConnection();
        this.mAccessibilityManager.removeAccessibilityStateChangeListener(this.mAccessibilityInteractionConnectionManager);
        this.mAccessibilityManager.removeHighTextContrastStateChangeListener(this.mHighContrastTextManager);
        removeSendWindowContentChangedCallback();
        destroyHardwareRenderer();
        setAccessibilityFocus(null, null);
        this.mView.assignParent(null);
        this.mView = null;
        this.mAttachInfo.mRootView = null;
        destroySurface();
        Callback callback = this.mInputQueueCallback;
        if (callback != null) {
            InputQueue inputQueue = this.mInputQueue;
            if (inputQueue != null) {
                callback.onInputQueueDestroyed(inputQueue);
                this.mInputQueue.dispose();
                this.mInputQueueCallback = null;
                this.mInputQueue = null;
            }
        }
        WindowInputEventReceiver windowInputEventReceiver = this.mInputEventReceiver;
        if (windowInputEventReceiver != null) {
            windowInputEventReceiver.dispose();
            this.mInputEventReceiver = null;
        }
        try {
            this.mWindowSession.remove(this.mWindow);
        } catch (RemoteException e) {
        }
        InputChannel inputChannel = this.mInputChannel;
        if (inputChannel != null) {
            inputChannel.dispose();
            this.mInputChannel = null;
        }
        this.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
        unscheduleTraversals();
    }

    private void performConfigurationChange(MergedConfiguration mergedConfiguration, boolean force, int newDisplayId) {
        if (mergedConfiguration != null) {
            Configuration globalConfig = mergedConfiguration.getGlobalConfiguration();
            Configuration overrideConfig = mergedConfiguration.getOverrideConfiguration();
            CompatibilityInfo ci = this.mDisplay.getDisplayAdjustments().getCompatibilityInfo();
            if (!ci.equals(CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO)) {
                globalConfig = new Configuration(globalConfig);
                ci.applyToConfiguration(this.mNoncompatDensity, globalConfig);
            }
            synchronized (sConfigCallbacks) {
                for (int i = sConfigCallbacks.size() - 1; i >= 0; i--) {
                    ((ConfigChangedCallback) sConfigCallbacks.get(i)).onConfigurationChanged(globalConfig);
                }
            }
            this.mLastReportedMergedConfiguration.setConfiguration(globalConfig, overrideConfig);
            this.mForceNextConfigUpdate = force;
            ActivityConfigCallback activityConfigCallback = this.mActivityConfigCallback;
            if (activityConfigCallback != null) {
                activityConfigCallback.onConfigurationChanged(overrideConfig, newDisplayId);
            } else {
                updateConfiguration(newDisplayId);
            }
            this.mForceNextConfigUpdate = false;
            return;
        }
        throw new IllegalArgumentException("No merged config provided.");
    }

    public void updateConfiguration(int newDisplayId) {
        Resources localResources = this.mView;
        if (localResources != null) {
            localResources = localResources.getResources();
            Configuration config = localResources.getConfiguration();
            if (newDisplayId != -1) {
                onMovedToDisplay(newDisplayId, config);
            }
            if (this.mForceNextConfigUpdate || this.mLastConfigurationFromResources.diff(config) != 0) {
                updateInternalDisplay(this.mDisplay.getDisplayId(), localResources);
                int lastLayoutDirection = this.mLastConfigurationFromResources.getLayoutDirection();
                int currentLayoutDirection = config.getLayoutDirection();
                this.mLastConfigurationFromResources.setTo(config);
                if (lastLayoutDirection != currentLayoutDirection && this.mViewLayoutDirectionInitial == 2) {
                    this.mView.setLayoutDirection(currentLayoutDirection);
                }
                this.mView.dispatchConfigurationChanged(config);
                this.mForceNextWindowRelayout = true;
                requestLayout();
            }
            updateForceDarkMode();
        }
    }

    public static boolean isViewDescendantOf(View child, View parent) {
        boolean z = true;
        if (child == parent) {
            return true;
        }
        ViewParent theParent = child.getParent();
        if (!((theParent instanceof ViewGroup) && isViewDescendantOf((View) theParent, parent))) {
            z = false;
        }
        return z;
    }

    private static void forceLayout(View view) {
        view.forceLayout();
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                forceLayout(group.getChildAt(i));
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean ensureTouchMode(boolean inTouchMode) {
        if (this.mAttachInfo.mInTouchMode == inTouchMode) {
            return false;
        }
        try {
            this.mWindowSession.setInTouchMode(inTouchMode);
            return ensureTouchModeLocally(inTouchMode);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean ensureTouchModeLocally(boolean inTouchMode) {
        if (this.mAttachInfo.mInTouchMode == inTouchMode) {
            return false;
        }
        AttachInfo attachInfo = this.mAttachInfo;
        attachInfo.mInTouchMode = inTouchMode;
        attachInfo.mTreeObserver.dispatchOnTouchModeChanged(inTouchMode);
        return inTouchMode ? enterTouchMode() : leaveTouchMode();
    }

    private boolean enterTouchMode() {
        View view = this.mView;
        if (view != null && view.hasFocus()) {
            view = this.mView.findFocus();
            if (!(view == null || view.isFocusableInTouchMode())) {
                ViewGroup ancestorToTakeFocus = findAncestorToTakeFocusInTouchMode(view);
                if (ancestorToTakeFocus != null) {
                    return ancestorToTakeFocus.requestFocus();
                }
                view.clearFocusInternal(null, true, false);
                return true;
            }
        }
        return false;
    }

    private static ViewGroup findAncestorToTakeFocusInTouchMode(View focused) {
        ViewParent parent = focused.getParent();
        while (parent instanceof ViewGroup) {
            ViewGroup vgParent = (ViewGroup) parent;
            if (vgParent.getDescendantFocusability() == 262144 && vgParent.isFocusableInTouchMode()) {
                return vgParent;
            }
            if (vgParent.isRootNamespace()) {
                return null;
            }
            parent = vgParent.getParent();
        }
        return null;
    }

    private boolean leaveTouchMode() {
        View view = this.mView;
        if (view == null) {
            return false;
        }
        if (view.hasFocus()) {
            view = this.mView.findFocus();
            if (!((view instanceof ViewGroup) && ((ViewGroup) view).getDescendantFocusability() == 262144)) {
                return false;
            }
        }
        return this.mView.restoreDefaultFocus();
    }

    private void resetPointerIcon(MotionEvent event) {
        this.mPointerIconType = 1;
        updatePointerIcon(event);
    }

    private boolean updatePointerIcon(MotionEvent event) {
        float x = event.getX(0);
        float y = event.getY(0);
        View view = this.mView;
        if (view == null) {
            Slog.d(this.mTag, "updatePointerIcon called after view was removed");
            return false;
        } else if (x < 0.0f || x >= ((float) view.getWidth()) || y < 0.0f || y >= ((float) this.mView.getHeight())) {
            Slog.d(this.mTag, "updatePointerIcon called with position out of bounds");
            return false;
        } else {
            PointerIcon pointerIcon = this.mView.onResolvePointerIcon(event, 0);
            int pointerType = pointerIcon != null ? pointerIcon.getType() : 1000;
            if (this.mPointerIconType != pointerType) {
                this.mPointerIconType = pointerType;
                this.mCustomPointerIcon = null;
                if (this.mPointerIconType != -1) {
                    InputManager.getInstance().setPointerIconType(pointerType);
                    return true;
                }
            }
            if (this.mPointerIconType == -1 && !pointerIcon.equals(this.mCustomPointerIcon)) {
                this.mCustomPointerIcon = pointerIcon;
                InputManager.getInstance().setCustomPointerIcon(this.mCustomPointerIcon);
            }
            return true;
        }
    }

    private void maybeUpdateTooltip(MotionEvent event) {
        if (event.getPointerCount() == 1) {
            int action = event.getActionMasked();
            if (action == 9 || action == 7 || action == 10) {
                AccessibilityManager manager = AccessibilityManager.getInstance(this.mContext);
                if (!manager.isEnabled() || !manager.isTouchExplorationEnabled()) {
                    View view = this.mView;
                    if (view == null) {
                        Slog.d(this.mTag, "maybeUpdateTooltip called after view was removed");
                    } else {
                        view.dispatchTooltipHoverEvent(event);
                    }
                }
            }
        }
    }

    private static boolean isNavigationKey(KeyEvent keyEvent) {
        int keyCode = keyEvent.getKeyCode();
        if (!(keyCode == 61 || keyCode == 62 || keyCode == 66 || keyCode == 92 || keyCode == 93 || keyCode == 122 || keyCode == 123)) {
            switch (keyCode) {
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    private static boolean isTypingKey(KeyEvent keyEvent) {
        return keyEvent.getUnicodeChar() > 0;
    }

    private boolean checkForLeavingTouchModeAndConsume(KeyEvent event) {
        if (!this.mAttachInfo.mInTouchMode) {
            return false;
        }
        int action = event.getAction();
        if ((action != 0 && action != 2) || (event.getFlags() & 4) != 0) {
            return false;
        }
        if (isNavigationKey(event)) {
            return ensureTouchMode(false);
        }
        if (!isTypingKey(event)) {
            return false;
        }
        ensureTouchMode(false);
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void setLocalDragState(Object obj) {
        this.mLocalDragState = obj;
    }

    private void handleDragEvent(DragEvent event) {
        if (this.mView != null && this.mAdded) {
            int what = event.mAction;
            if (what == 1) {
                this.mCurrentDragView = null;
                this.mDragDescription = event.mClipDescription;
            } else {
                if (what == 4) {
                    this.mDragDescription = null;
                }
                event.mClipDescription = this.mDragDescription;
            }
            if (what == 6) {
                if (View.sCascadedDragDrop) {
                    this.mView.dispatchDragEnterExitInPreN(event);
                }
                setDragFocus(null, event);
            } else {
                if (what == 2 || what == 3) {
                    this.mDragPoint.set(event.mX, event.mY);
                    Translator translator = this.mTranslator;
                    if (translator != null) {
                        translator.translatePointInScreenToAppWindow(this.mDragPoint);
                    }
                    int i = this.mCurScrollY;
                    if (i != 0) {
                        this.mDragPoint.offset(0.0f, (float) i);
                    }
                    event.mX = this.mDragPoint.x;
                    event.mY = this.mDragPoint.y;
                }
                View prevDragView = this.mCurrentDragView;
                if (what == 3 && event.mClipData != null) {
                    event.mClipData.prepareToEnterProcess();
                }
                boolean result = this.mView.dispatchDragEvent(event);
                if (what == 2 && !event.mEventHandlerWasCalled) {
                    setDragFocus(null, event);
                }
                if (prevDragView != this.mCurrentDragView) {
                    if (prevDragView != null) {
                        try {
                            this.mWindowSession.dragRecipientExited(this.mWindow);
                        } catch (RemoteException e) {
                            Slog.e(this.mTag, "Unable to note drag target change");
                        }
                    }
                    if (this.mCurrentDragView != null) {
                        this.mWindowSession.dragRecipientEntered(this.mWindow);
                    }
                }
                if (what == 3) {
                    try {
                        String str = this.mTag;
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append("Reporting drop result: ");
                        stringBuilder.append(result);
                        Log.i(str, stringBuilder.toString());
                        this.mWindowSession.reportDropResult(this.mWindow, result);
                    } catch (RemoteException e2) {
                        Log.e(this.mTag, "Unable to report drop result");
                    }
                }
                if (what == 4) {
                    this.mCurrentDragView = null;
                    setLocalDragState(null);
                    AttachInfo attachInfo = this.mAttachInfo;
                    attachInfo.mDragToken = null;
                    if (attachInfo.mDragSurface != null) {
                        this.mAttachInfo.mDragSurface.release();
                        this.mAttachInfo.mDragSurface = null;
                    }
                }
            }
        }
        event.recycle();
    }

    public void handleDispatchSystemUiVisibilityChanged(SystemUiVisibilityInfo args) {
        if (this.mSeq != args.seq) {
            this.mSeq = args.seq;
            this.mAttachInfo.mForceReportNewAttributes = true;
            scheduleTraversals();
        }
        if (this.mView != null) {
            if (args.localChanges != 0) {
                this.mView.updateLocalSystemUiVisibility(args.localValue, args.localChanges);
            }
            int visibility = args.globalVisibility & 7;
            if (visibility != this.mAttachInfo.mGlobalSystemUiVisibility) {
                this.mAttachInfo.mGlobalSystemUiVisibility = visibility;
                this.mView.dispatchSystemUiVisibilityChanged(visibility);
            }
        }
    }

    public void onWindowTitleChanged() {
        this.mAttachInfo.mForceReportNewAttributes = true;
    }

    public void handleDispatchWindowShown() {
        this.mAttachInfo.mTreeObserver.dispatchOnWindowShown();
    }

    public void handleRequestKeyboardShortcuts(IResultReceiver receiver, int deviceId) {
        Bundle data = new Bundle();
        ArrayList<KeyboardShortcutGroup> list = new ArrayList();
        View view = this.mView;
        if (view != null) {
            view.requestKeyboardShortcuts(list, deviceId);
        }
        data.putParcelableArrayList(WindowManager.PARCEL_KEY_SHORTCUTS_ARRAY, list);
        try {
            receiver.send(0, data);
        } catch (RemoteException e) {
        }
    }

    @UnsupportedAppUsage
    public void getLastTouchPoint(Point outLocation) {
        outLocation.x = (int) this.mLastTouchPoint.x;
        outLocation.y = (int) this.mLastTouchPoint.y;
    }

    public int getLastTouchSource() {
        return this.mLastTouchSource;
    }

    public void setDragFocus(View newDragTarget, DragEvent event) {
        if (!(this.mCurrentDragView == newDragTarget || View.sCascadedDragDrop)) {
            float tx = event.mX;
            float ty = event.mY;
            int action = event.mAction;
            ClipData td = event.mClipData;
            event.mX = 0.0f;
            event.mY = 0.0f;
            event.mClipData = null;
            View view = this.mCurrentDragView;
            if (view != null) {
                event.mAction = 6;
                view.callDragEventHandler(event);
            }
            if (newDragTarget != null) {
                event.mAction = 5;
                newDragTarget.callDragEventHandler(event);
            }
            event.mAction = action;
            event.mX = tx;
            event.mY = ty;
            event.mClipData = td;
        }
        this.mCurrentDragView = newDragTarget;
    }

    private AudioManager getAudioManager() {
        View view = this.mView;
        if (view != null) {
            if (this.mAudioManager == null) {
                this.mAudioManager = (AudioManager) view.getContext().getSystemService("audio");
            }
            return this.mAudioManager;
        }
        throw new IllegalStateException("getAudioManager called when there is no mView");
    }

    private AutofillManager getAutofillManager() {
        ViewGroup decorView = this.mView;
        if (decorView instanceof ViewGroup) {
            decorView = decorView;
            if (decorView.getChildCount() > 0) {
                return (AutofillManager) decorView.getChildAt(0).getContext().getSystemService(AutofillManager.class);
            }
        }
        return null;
    }

    private boolean isAutofillUiShowing() {
        AutofillManager afm = getAutofillManager();
        if (afm == null) {
            return false;
        }
        return afm.isAutofillUiShowing();
    }

    public AccessibilityInteractionController getAccessibilityInteractionController() {
        if (this.mView != null) {
            if (this.mAccessibilityInteractionController == null) {
                this.mAccessibilityInteractionController = new AccessibilityInteractionController(this);
            }
            return this.mAccessibilityInteractionController;
        }
        throw new IllegalStateException("getAccessibilityInteractionController called when there is no mView");
    }

    private int relayoutWindow(LayoutParams params, int viewVisibility, boolean insetsPending) throws RemoteException {
        boolean restore;
        long frameNumber;
        LayoutParams layoutParams = params;
        float appScale = this.mAttachInfo.mApplicationScale;
        if (layoutParams == null || this.mTranslator == null) {
            restore = false;
        } else {
            params.backup();
            this.mTranslator.translateWindowLayout(layoutParams);
            restore = true;
        }
        if (!(layoutParams == null || this.mOrigWindowType == layoutParams.type || this.mTargetSdkVersion >= 14)) {
            String str = this.mTag;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Window type can not be changed after the window is added; ignoring change of ");
            stringBuilder.append(this.mView);
            Slog.w(str, stringBuilder.toString());
            layoutParams.type = this.mOrigWindowType;
        }
        if (this.mSurface.isValid()) {
            frameNumber = this.mSurface.getNextFrameNumber();
        } else {
            frameNumber = -1;
        }
        int relayoutResult = this.mWindowSession.relayout(this.mWindow, this.mSeq, params, (int) ((((float) this.mView.getMeasuredWidth()) * appScale) + 0.5f), (int) ((((float) this.mView.getMeasuredHeight()) * appScale) + 0.5f), viewVisibility, insetsPending, frameNumber, this.mTmpFrame, this.mPendingOverscanInsets, this.mPendingContentInsets, this.mPendingVisibleInsets, this.mPendingStableInsets, this.mPendingOutsets, this.mPendingBackDropFrame, this.mPendingDisplayCutout, this.mPendingMergedConfiguration, this.mSurfaceControl, this.mTempInsets);
        if (this.mSurfaceControl.isValid()) {
            this.mSurface.copyFrom(this.mSurfaceControl);
        } else {
            destroySurface();
        }
        this.mPendingAlwaysConsumeSystemBars = (relayoutResult & 64) != 0;
        if (restore) {
            params.restore();
        }
        Translator translator = this.mTranslator;
        if (translator != null) {
            translator.translateRectInScreenToAppWinFrame(this.mTmpFrame);
            this.mTranslator.translateRectInScreenToAppWindow(this.mPendingOverscanInsets);
            this.mTranslator.translateRectInScreenToAppWindow(this.mPendingContentInsets);
            this.mTranslator.translateRectInScreenToAppWindow(this.mPendingVisibleInsets);
            this.mTranslator.translateRectInScreenToAppWindow(this.mPendingStableInsets);
        }
        setFrame(this.mTmpFrame);
        this.mInsetsController.onStateChanged(this.mTempInsets);
        return relayoutResult;
    }

    private void setFrame(Rect frame) {
        this.mWinFrame.set(frame);
        this.mInsetsController.onFrameChanged(frame);
    }

    public void playSoundEffect(int effectId) {
        checkThread();
        StringBuilder stringBuilder;
        try {
            AudioManager audioManager = getAudioManager();
            if (effectId == 0) {
                audioManager.playSoundEffect(0);
            } else if (effectId == 1) {
                audioManager.playSoundEffect(3);
            } else if (effectId == 2) {
                audioManager.playSoundEffect(1);
            } else if (effectId == 3) {
                audioManager.playSoundEffect(4);
            } else if (effectId == 4) {
                audioManager.playSoundEffect(2);
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("unknown effect id ");
                stringBuilder.append(effectId);
                stringBuilder.append(" not defined in ");
                stringBuilder.append(SoundEffectConstants.class.getCanonicalName());
                throw new IllegalArgumentException(stringBuilder.toString());
            }
        } catch (IllegalStateException e) {
            String str = this.mTag;
            stringBuilder = new StringBuilder();
            stringBuilder.append("FATAL EXCEPTION when attempting to play sound effect: ");
            stringBuilder.append(e);
            Log.e(str, stringBuilder.toString());
            e.printStackTrace();
        }
    }

    public boolean performHapticFeedback(int effectId, boolean always) {
        try {
            return this.mWindowSession.performHapticFeedback(effectId, always);
        } catch (RemoteException e) {
            return false;
        }
    }

    public View focusSearch(View focused, int direction) {
        checkThread();
        if (this.mView instanceof ViewGroup) {
            return FocusFinder.getInstance().findNextFocus((ViewGroup) this.mView, focused, direction);
        }
        return null;
    }

    public View keyboardNavigationClusterSearch(View currentCluster, int direction) {
        checkThread();
        return FocusFinder.getInstance().findNextKeyboardNavigationCluster(this.mView, currentCluster, direction);
    }

    public void debug() {
        this.mView.debug();
    }

    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        String innerPrefix = new StringBuilder();
        innerPrefix.append(prefix);
        innerPrefix.append("  ");
        innerPrefix = innerPrefix.toString();
        writer.print(prefix);
        writer.println("ViewRoot:");
        writer.print(innerPrefix);
        writer.print("mAdded=");
        writer.print(this.mAdded);
        writer.print(" mRemoved=");
        writer.println(this.mRemoved);
        writer.print(innerPrefix);
        writer.print("mConsumeBatchedInputScheduled=");
        writer.println(this.mConsumeBatchedInputScheduled);
        writer.print(innerPrefix);
        writer.print("mConsumeBatchedInputImmediatelyScheduled=");
        writer.println(this.mConsumeBatchedInputImmediatelyScheduled);
        writer.print(innerPrefix);
        writer.print("mPendingInputEventCount=");
        writer.println(this.mPendingInputEventCount);
        writer.print(innerPrefix);
        writer.print("mProcessInputEventsScheduled=");
        writer.println(this.mProcessInputEventsScheduled);
        writer.print(innerPrefix);
        writer.print("mTraversalScheduled=");
        writer.print(this.mTraversalScheduled);
        writer.print(innerPrefix);
        writer.print("mIsAmbientMode=");
        writer.print(this.mIsAmbientMode);
        if (this.mTraversalScheduled) {
            writer.print(" (barrier=");
            writer.print(this.mTraversalBarrier);
            writer.println(")");
        } else {
            writer.println();
        }
        this.mFirstInputStage.dump(innerPrefix, writer);
        this.mChoreographer.dump(prefix, writer);
        this.mInsetsController.dump(prefix, writer);
        writer.print(prefix);
        writer.println("View Hierarchy:");
        dumpViewHierarchy(innerPrefix, writer, this.mView);
    }

    private void dumpViewHierarchy(String prefix, PrintWriter writer, View view) {
        writer.print(prefix);
        if (view == null) {
            writer.println("null");
            return;
        }
        writer.println(view.toString());
        if (view instanceof ViewGroup) {
            ViewGroup grp = (ViewGroup) view;
            int N = grp.getChildCount();
            if (N > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(prefix);
                stringBuilder.append("  ");
                prefix = stringBuilder.toString();
                for (int i = 0; i < N; i++) {
                    dumpViewHierarchy(prefix, writer, grp.getChildAt(i));
                }
            }
        }
    }

    public void dumpGfxInfo(int[] info) {
        info[1] = 0;
        info[0] = 0;
        View view = this.mView;
        if (view != null) {
            getGfxInfo(view, info);
        }
    }

    private static void getGfxInfo(View view, int[] info) {
        RenderNode renderNode = view.mRenderNode;
        info[0] = info[0] + 1;
        if (renderNode != null) {
            info[1] = info[1] + ((int) renderNode.computeApproximateMemoryUsage());
        }
        if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                getGfxInfo(group.getChildAt(i), info);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void updateBlurRatio(float blurRatio) {
        if (this.mSurface.isValid()) {
            if (this.BlurSurfaceSession == null || this.BlurSurfaceControl == null) {
                this.BlurSurfaceSession = new SurfaceSession();
                Builder builder = new Builder(this.BlurSurfaceSession);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("BlurControl - ");
                stringBuilder.append(getTitle());
                this.BlurSurfaceControl = builder.setName(stringBuilder.toString()).setBufferSize(1, 1).setFormat(1).setFlags(4).build(this.mSurface);
            }
            if (this.BlurSurfaceControl != null) {
                SurfaceControl.openTransaction();
                try {
                    this.BlurSurfaceControl.setBlurRatio(blurRatio);
                } finally {
                    SurfaceControl.closeTransaction();
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void destroyBlurControlLayer() {
        try {
            if (this.BlurSurfaceControl != null) {
                this.BlurSurfaceControl.remove();
            }
        } catch (RuntimeException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Error destroying blur SurfaceControl in: ");
            stringBuilder.append(getTitle());
            Slog.w(str, stringBuilder.toString(), e);
        } catch (Throwable th) {
            this.BlurSurfaceControl = null;
            this.BlurSurfaceSession = null;
        }
        this.BlurSurfaceControl = null;
        this.BlurSurfaceSession = null;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean die(boolean immediate) {
        if (!immediate || this.mIsInTraversal) {
            if (this.mIsDrawing) {
                String str = this.mTag;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Attempting to destroy the window while drawing!\n  window=");
                stringBuilder.append(this);
                stringBuilder.append(", title=");
                stringBuilder.append(this.mWindowAttributes.getTitle());
                Log.e(str, stringBuilder.toString());
            } else {
                destroyHardwareRenderer();
            }
            this.mHandler.sendEmptyMessage(3);
            return true;
        }
        doDie();
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void doDie() {
        checkThread();
        synchronized (this) {
            if (this.mRemoved) {
                return;
            }
            boolean viewVisibilityChanged = true;
            this.mRemoved = true;
            if (this.mAdded) {
                dispatchDetachedFromWindow();
            }
            if (this.mAdded && !this.mFirst) {
                destroyHardwareRenderer();
                if (this.mView != null) {
                    int viewVisibility = this.mView.getVisibility();
                    if (this.mViewVisibility == viewVisibility) {
                        viewVisibilityChanged = false;
                    }
                    if (this.mWindowAttributesChanged || viewVisibilityChanged) {
                        try {
                            if ((relayoutWindow(this.mWindowAttributes, viewVisibility, false) & 2) != 0) {
                                this.mWindowSession.finishDrawing(this.mWindow);
                            }
                        } catch (RemoteException e) {
                        }
                    }
                    destroySurface();
                }
            }
            this.mAdded = false;
            WindowManagerGlobal.getInstance().doRemoveView(this);
        }
    }

    public void requestUpdateConfiguration(Configuration config) {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(18, config));
    }

    public void loadSystemProperties() {
        this.mHandler.post(new Runnable() {
            public void run() {
                ViewRootImpl.this.mProfileRendering = SystemProperties.getBoolean(ViewRootImpl.PROPERTY_PROFILE_RENDERING, false);
                ViewRootImpl viewRootImpl = ViewRootImpl.this;
                viewRootImpl.profileRendering(viewRootImpl.mAttachInfo.mHasWindowFocus);
                if (ViewRootImpl.this.mAttachInfo.mThreadedRenderer != null && ViewRootImpl.this.mAttachInfo.mThreadedRenderer.loadSystemProperties()) {
                    ViewRootImpl.this.invalidate();
                }
                boolean layout = ((Boolean) DisplayProperties.debug_layout().orElse(Boolean.valueOf(false))).booleanValue();
                if (layout != ViewRootImpl.this.mAttachInfo.mDebugLayout) {
                    ViewRootImpl.this.mAttachInfo.mDebugLayout = layout;
                    if (!ViewRootImpl.this.mHandler.hasMessages(22)) {
                        ViewRootImpl.this.mHandler.sendEmptyMessageDelayed(22, 200);
                    }
                }
            }
        });
    }

    private void destroyHardwareRenderer() {
        ThreadedRenderer hardwareRenderer = this.mAttachInfo.mThreadedRenderer;
        if (hardwareRenderer != null) {
            View view = this.mView;
            if (view != null) {
                hardwareRenderer.destroyHardwareResources(view);
            }
            hardwareRenderer.destroy();
            hardwareRenderer.setRequested(false);
            AttachInfo attachInfo = this.mAttachInfo;
            attachInfo.mThreadedRenderer = null;
            attachInfo.mHardwareAccelerated = false;
        }
    }

    @UnsupportedAppUsage
    private void dispatchResized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, MergedConfiguration mergedConfiguration, Rect backDropFrame, boolean forceLayout, boolean alwaysConsumeSystemBars, int displayId, ParcelableWrapper displayCutout) {
        Rect rect = frame;
        Rect rect2 = overscanInsets;
        Rect rect3 = contentInsets;
        Rect rect4 = visibleInsets;
        Rect rect5 = stableInsets;
        MergedConfiguration mergedConfiguration2 = mergedConfiguration;
        Rect rect6 = backDropFrame;
        boolean sameProcessCall = true;
        if (this.mDragResizing && this.mUseMTRenderer) {
            boolean fullscreen = rect.equals(rect6);
            synchronized (this.mWindowCallbacks) {
                for (int i = this.mWindowCallbacks.size() - 1; i >= 0; i--) {
                    ((WindowCallbacks) this.mWindowCallbacks.get(i)).onWindowSizeIsChanging(rect6, fullscreen, rect4, rect5);
                }
            }
        }
        Message msg = this.mHandler.obtainMessage(reportDraw ? 5 : 4);
        Translator translator = this.mTranslator;
        if (translator != null) {
            translator.translateRectInScreenToAppWindow(rect);
            this.mTranslator.translateRectInScreenToAppWindow(rect2);
            this.mTranslator.translateRectInScreenToAppWindow(rect3);
            this.mTranslator.translateRectInScreenToAppWindow(rect4);
        }
        SomeArgs args = SomeArgs.obtain();
        if (Binder.getCallingPid() != Process.myPid()) {
            sameProcessCall = false;
        }
        args.arg1 = sameProcessCall ? new Rect(rect) : rect;
        args.arg2 = sameProcessCall ? new Rect(rect3) : rect3;
        args.arg3 = sameProcessCall ? new Rect(rect4) : rect4;
        Object mergedConfiguration3 = (!sameProcessCall || mergedConfiguration2 == null) ? mergedConfiguration2 : new MergedConfiguration(mergedConfiguration2);
        args.arg4 = mergedConfiguration3;
        args.arg5 = sameProcessCall ? new Rect(rect2) : rect2;
        args.arg6 = sameProcessCall ? new Rect(rect5) : rect5;
        args.arg7 = sameProcessCall ? new Rect(outsets) : outsets;
        args.arg8 = sameProcessCall ? new Rect(rect6) : rect6;
        args.arg9 = displayCutout.get();
        args.argi1 = forceLayout;
        args.argi2 = alwaysConsumeSystemBars;
        args.argi3 = displayId;
        msg.obj = args;
        this.mHandler.sendMessage(msg);
    }

    private void dispatchInsetsChanged(InsetsState insetsState) {
        this.mHandler.obtainMessage(30, insetsState).sendToTarget();
    }

    private void dispatchInsetsControlChanged(InsetsState insetsState, InsetsSourceControl[] activeControls) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = insetsState;
        args.arg2 = activeControls;
        this.mHandler.obtainMessage(31, args).sendToTarget();
    }

    public void dispatchMoved(int newX, int newY) {
        if (this.mTranslator != null) {
            PointF point = new PointF((float) newX, (float) newY);
            this.mTranslator.translatePointInScreenToAppWindow(point);
            newX = (int) (((double) point.x) + 0.5d);
            newY = (int) (((double) point.y) + 0.5d);
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(23, newX, newY));
    }

    private QueuedInputEvent obtainQueuedInputEvent(InputEvent event, InputEventReceiver receiver, int flags) {
        QueuedInputEvent q = this.mQueuedInputEventPool;
        if (q != null) {
            this.mQueuedInputEventPoolSize--;
            this.mQueuedInputEventPool = q.mNext;
            q.mNext = null;
        } else {
            q = new QueuedInputEvent();
        }
        q.mEvent = event;
        q.mReceiver = receiver;
        q.mFlags = flags;
        return q;
    }

    private void recycleQueuedInputEvent(QueuedInputEvent q) {
        q.mEvent = null;
        q.mReceiver = null;
        int i = this.mQueuedInputEventPoolSize;
        if (i < 10) {
            this.mQueuedInputEventPoolSize = i + 1;
            q.mNext = this.mQueuedInputEventPool;
            this.mQueuedInputEventPool = q;
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void enqueueInputEvent(InputEvent event) {
        enqueueInputEvent(event, null, 0, false);
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void enqueueInputEvent(InputEvent event, InputEventReceiver receiver, int flags, boolean processImmediately) {
        QueuedInputEvent q = obtainQueuedInputEvent(event, receiver, flags);
        QueuedInputEvent last = this.mPendingInputEventTail;
        if (last == null) {
            this.mPendingInputEventHead = q;
            this.mPendingInputEventTail = q;
        } else {
            last.mNext = q;
            this.mPendingInputEventTail = q;
        }
        this.mPendingInputEventCount++;
        Trace.traceCounter(4, this.mPendingInputEventQueueLengthCounterName, this.mPendingInputEventCount);
        if (processImmediately) {
            doProcessInputEvents();
        } else {
            scheduleProcessInputEvents();
        }
    }

    private void scheduleProcessInputEvents() {
        if (!this.mProcessInputEventsScheduled) {
            this.mProcessInputEventsScheduled = true;
            Message msg = this.mHandler.obtainMessage(19);
            msg.setAsynchronous(true);
            this.mHandler.sendMessage(msg);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doProcessInputEvents() {
        while (this.mPendingInputEventHead != null) {
            QueuedInputEvent q = this.mPendingInputEventHead;
            this.mPendingInputEventHead = q.mNext;
            if (this.mPendingInputEventHead == null) {
                this.mPendingInputEventTail = null;
            }
            q.mNext = null;
            this.mPendingInputEventCount--;
            Trace.traceCounter(4, this.mPendingInputEventQueueLengthCounterName, this.mPendingInputEventCount);
            long eventTime = q.mEvent.getEventTimeNano();
            long oldestEventTime = eventTime;
            if (q.mEvent instanceof MotionEvent) {
                MotionEvent me = q.mEvent;
                if (me.getHistorySize() > 0) {
                    oldestEventTime = me.getHistoricalEventTimeNano(0);
                }
            }
            this.mChoreographer.mFrameInfo.updateInputEventTime(eventTime, oldestEventTime);
            deliverInputEvent(q);
        }
        if (this.mProcessInputEventsScheduled) {
            this.mProcessInputEventsScheduled = false;
            this.mHandler.removeMessages(19);
        }
    }

    private void deliverInputEvent(QueuedInputEvent q) {
        Trace.asyncTraceBegin(8, "deliverInputEvent", q.mEvent.getSequenceNumber());
        InputEventConsistencyVerifier inputEventConsistencyVerifier = this.mInputEventConsistencyVerifier;
        if (inputEventConsistencyVerifier != null) {
            inputEventConsistencyVerifier.onInputEvent(q.mEvent, 0);
        }
        InputStage stage = q.shouldSendToSynthesizer() ? this.mSyntheticInputStage : q.shouldSkipIme() ? this.mFirstPostImeInputStage : this.mFirstInputStage;
        if (q.mEvent instanceof KeyEvent) {
            this.mUnhandledKeyManager.preDispatch((KeyEvent) q.mEvent);
        }
        if (stage != null) {
            handleWindowFocusChanged();
            stage.deliver(q);
            return;
        }
        finishInputEvent(q);
    }

    private void finishInputEvent(QueuedInputEvent q) {
        Trace.asyncTraceEnd(8, "deliverInputEvent", q.mEvent.getSequenceNumber());
        if (q.mReceiver != null) {
            boolean modified = true;
            boolean handled = (q.mFlags & 8) != 0;
            if ((q.mFlags & 64) == 0) {
                modified = false;
            }
            if (modified) {
                Trace.traceBegin(8, "processInputEventBeforeFinish");
                try {
                    InputEvent processedEvent = this.mInputCompatProcessor.processInputEventBeforeFinish(q.mEvent);
                    if (processedEvent != null) {
                        q.mReceiver.finishInputEvent(processedEvent, handled);
                    }
                } finally {
                    Trace.traceEnd(8);
                }
            } else {
                q.mReceiver.finishInputEvent(q.mEvent, handled);
            }
        } else {
            q.mEvent.recycleIfNeededAfterDispatch();
        }
        recycleQueuedInputEvent(q);
    }

    static boolean isTerminalInputEvent(InputEvent event) {
        boolean z = false;
        if (event instanceof KeyEvent) {
            if (((KeyEvent) event).getAction() == 1) {
                z = true;
            }
            return z;
        }
        int action = ((MotionEvent) event).getAction();
        if (action == 1 || action == 3 || action == 10) {
            z = true;
        }
        return z;
    }

    /* Access modifiers changed, original: 0000 */
    public void scheduleConsumeBatchedInput() {
        if (!this.mConsumeBatchedInputScheduled) {
            this.mConsumeBatchedInputScheduled = true;
            this.mChoreographer.postCallback(0, this.mConsumedBatchedInputRunnable, null);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void unscheduleConsumeBatchedInput() {
        if (this.mConsumeBatchedInputScheduled) {
            this.mConsumeBatchedInputScheduled = false;
            this.mChoreographer.removeCallbacks(0, this.mConsumedBatchedInputRunnable, null);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void scheduleConsumeBatchedInputImmediately() {
        if (!this.mConsumeBatchedInputImmediatelyScheduled) {
            unscheduleConsumeBatchedInput();
            this.mConsumeBatchedInputImmediatelyScheduled = true;
            this.mHandler.post(this.mConsumeBatchedInputImmediatelyRunnable);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doConsumeBatchedInput(long frameTimeNanos) {
        if (this.mConsumeBatchedInputScheduled) {
            this.mConsumeBatchedInputScheduled = false;
            WindowInputEventReceiver windowInputEventReceiver = this.mInputEventReceiver;
            if (!(windowInputEventReceiver == null || !windowInputEventReceiver.consumeBatchedInputEvents(frameTimeNanos) || frameTimeNanos == -1)) {
                scheduleConsumeBatchedInput();
            }
            doProcessInputEvents();
        }
    }

    public void dispatchInvalidateDelayed(View view, long delayMilliseconds) {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(1, view), delayMilliseconds);
    }

    public void dispatchInvalidateRectDelayed(InvalidateInfo info, long delayMilliseconds) {
        this.mHandler.sendMessageDelayed(this.mHandler.obtainMessage(2, info), delayMilliseconds);
    }

    public void dispatchInvalidateOnAnimation(View view) {
        this.mInvalidateOnAnimationRunnable.addView(view);
    }

    public void dispatchInvalidateRectOnAnimation(InvalidateInfo info) {
        this.mInvalidateOnAnimationRunnable.addViewRect(info);
    }

    @UnsupportedAppUsage
    public void cancelInvalidate(View view) {
        this.mHandler.removeMessages(1, view);
        this.mHandler.removeMessages(2, view);
        this.mInvalidateOnAnimationRunnable.removeView(view);
    }

    @UnsupportedAppUsage
    public void dispatchInputEvent(InputEvent event) {
        dispatchInputEvent(event, null);
    }

    @UnsupportedAppUsage
    public void dispatchInputEvent(InputEvent event, InputEventReceiver receiver) {
        SomeArgs args = SomeArgs.obtain();
        args.arg1 = event;
        args.arg2 = receiver;
        Message msg = this.mHandler.obtainMessage(7, args);
        msg.setAsynchronous(true);
        this.mHandler.sendMessage(msg);
    }

    public void synthesizeInputEvent(InputEvent event) {
        Message msg = this.mHandler.obtainMessage(24, event);
        msg.setAsynchronous(true);
        this.mHandler.sendMessage(msg);
    }

    @UnsupportedAppUsage
    public void dispatchKeyFromIme(KeyEvent event) {
        Message msg = this.mHandler.obtainMessage(11, event);
        msg.setAsynchronous(true);
        this.mHandler.sendMessage(msg);
    }

    public void dispatchKeyFromAutofill(KeyEvent event) {
        Message msg = this.mHandler.obtainMessage(12, event);
        msg.setAsynchronous(true);
        this.mHandler.sendMessage(msg);
    }

    @UnsupportedAppUsage
    public void dispatchUnhandledInputEvent(InputEvent event) {
        if (event instanceof MotionEvent) {
            event = MotionEvent.obtain((MotionEvent) event);
        }
        synthesizeInputEvent(event);
    }

    public void dispatchAppVisibility(boolean visible) {
        Message msg = this.mHandler.obtainMessage(8);
        msg.arg1 = visible;
        this.mHandler.sendMessage(msg);
    }

    public void dispatchGetNewSurface() {
        this.mHandler.sendMessage(this.mHandler.obtainMessage(9));
    }

    public void windowFocusChanged(boolean hasFocus, boolean inTouchMode) {
        synchronized (this) {
            this.mWindowFocusChanged = true;
            this.mUpcomingWindowFocus = hasFocus;
            this.mUpcomingInTouchMode = inTouchMode;
        }
        Message msg = Message.obtain();
        msg.what = 6;
        this.mHandler.sendMessage(msg);
    }

    public void dispatchWindowShown() {
        this.mHandler.sendEmptyMessage(25);
    }

    public void dispatchCloseSystemDialogs(String reason) {
        Message msg = Message.obtain();
        msg.what = 14;
        msg.obj = reason;
        this.mHandler.sendMessage(msg);
    }

    public void dispatchDragEvent(DragEvent event) {
        int what;
        if (event.getAction() == 2) {
            what = 16;
            this.mHandler.removeMessages(16);
        } else {
            what = 15;
        }
        this.mHandler.sendMessage(this.mHandler.obtainMessage(what, event));
    }

    public void updatePointerIcon(float x, float y) {
        this.mHandler.removeMessages(27);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(27, MotionEvent.obtain(0, SystemClock.uptimeMillis(), 7, x, y, 0)));
    }

    public void dispatchSystemUiVisibilityChanged(int seq, int globalVisibility, int localValue, int localChanges) {
        SystemUiVisibilityInfo args = new SystemUiVisibilityInfo();
        args.seq = seq;
        args.globalVisibility = globalVisibility;
        args.localValue = localValue;
        args.localChanges = localChanges;
        ViewRootHandler viewRootHandler = this.mHandler;
        viewRootHandler.sendMessage(viewRootHandler.obtainMessage(17, args));
    }

    public void dispatchCheckFocus() {
        if (!this.mHandler.hasMessages(13)) {
            this.mHandler.sendEmptyMessage(13);
        }
    }

    public void dispatchRequestKeyboardShortcuts(IResultReceiver receiver, int deviceId) {
        this.mHandler.obtainMessage(26, deviceId, 0, receiver).sendToTarget();
    }

    public void dispatchPointerCaptureChanged(boolean on) {
        this.mHandler.removeMessages(28);
        Message msg = this.mHandler.obtainMessage(28);
        msg.arg1 = on;
        this.mHandler.sendMessage(msg);
    }

    private void postSendWindowContentChangedCallback(View source, int changeType) {
        if (this.mSendWindowContentChangedAccessibilityEvent == null) {
            this.mSendWindowContentChangedAccessibilityEvent = new SendWindowContentChangedAccessibilityEvent(this, null);
        }
        this.mSendWindowContentChangedAccessibilityEvent.runOrPost(source, changeType);
    }

    private void removeSendWindowContentChangedCallback() {
        SendWindowContentChangedAccessibilityEvent sendWindowContentChangedAccessibilityEvent = this.mSendWindowContentChangedAccessibilityEvent;
        if (sendWindowContentChangedAccessibilityEvent != null) {
            this.mHandler.removeCallbacks(sendWindowContentChangedAccessibilityEvent);
        }
    }

    public boolean showContextMenuForChild(View originalView) {
        return false;
    }

    public boolean showContextMenuForChild(View originalView, float x, float y) {
        return false;
    }

    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback) {
        return null;
    }

    public ActionMode startActionModeForChild(View originalView, ActionMode.Callback callback, int type) {
        return null;
    }

    public void createContextMenu(ContextMenu menu) {
    }

    public void childDrawableStateChanged(View child) {
    }

    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        if (this.mView == null || this.mStopped || this.mPausedForTransition) {
            return false;
        }
        if (event.getEventType() != 2048) {
            SendWindowContentChangedAccessibilityEvent sendWindowContentChangedAccessibilityEvent = this.mSendWindowContentChangedAccessibilityEvent;
            if (!(sendWindowContentChangedAccessibilityEvent == null || sendWindowContentChangedAccessibilityEvent.mSource == null)) {
                this.mSendWindowContentChangedAccessibilityEvent.removeCallbacksAndRun();
            }
        }
        int eventType = event.getEventType();
        View source = getSourceForAccessibilityEvent(event);
        if (eventType == 2048) {
            handleWindowContentChangedEvent(event);
        } else if (eventType != 32768) {
            if (!(eventType != 65536 || source == null || source.getAccessibilityNodeProvider() == null)) {
                setAccessibilityFocus(null, null);
            }
        } else if (source != null) {
            AccessibilityNodeProvider provider = source.getAccessibilityNodeProvider();
            if (provider != null) {
                setAccessibilityFocus(source, provider.createAccessibilityNodeInfo(AccessibilityNodeInfo.getVirtualDescendantId(event.getSourceNodeId())));
            }
        }
        this.mAccessibilityManager.sendAccessibilityEvent(event);
        return true;
    }

    private View getSourceForAccessibilityEvent(AccessibilityEvent event) {
        return AccessibilityNodeIdManager.getInstance().findView(AccessibilityNodeInfo.getAccessibilityViewId(event.getSourceNodeId()));
    }

    private void handleWindowContentChangedEvent(AccessibilityEvent event) {
        View focusedHost = this.mAccessibilityFocusedHost;
        if (focusedHost != null && this.mAccessibilityFocusedVirtualView != null) {
            AccessibilityNodeProvider provider = focusedHost.getAccessibilityNodeProvider();
            if (provider == null) {
                this.mAccessibilityFocusedHost = null;
                this.mAccessibilityFocusedVirtualView = null;
                focusedHost.clearAccessibilityFocusNoCallbacks(0);
                return;
            }
            int changes = event.getContentChangeTypes();
            if ((changes & 1) != 0 || changes == 0) {
                int changedViewId = AccessibilityNodeInfo.getAccessibilityViewId(event.getSourceNodeId());
                boolean hostInSubtree = false;
                View root = this.mAccessibilityFocusedHost;
                while (root != null && !hostInSubtree) {
                    if (changedViewId == root.getAccessibilityViewId()) {
                        hostInSubtree = true;
                    } else {
                        ViewParent parent = root.getParent();
                        if (parent instanceof View) {
                            root = (View) parent;
                        } else {
                            root = null;
                        }
                    }
                }
                if (hostInSubtree) {
                    int focusedChildId = AccessibilityNodeInfo.getVirtualDescendantId(this.mAccessibilityFocusedVirtualView.getSourceNodeId());
                    Rect oldBounds = this.mTempRect;
                    this.mAccessibilityFocusedVirtualView.getBoundsInScreen(oldBounds);
                    this.mAccessibilityFocusedVirtualView = provider.createAccessibilityNodeInfo(focusedChildId);
                    AccessibilityNodeInfo accessibilityNodeInfo = this.mAccessibilityFocusedVirtualView;
                    if (accessibilityNodeInfo == null) {
                        this.mAccessibilityFocusedHost = null;
                        focusedHost.clearAccessibilityFocusNoCallbacks(0);
                        provider.performAction(focusedChildId, AccessibilityAction.ACTION_CLEAR_ACCESSIBILITY_FOCUS.getId(), null);
                        invalidateRectOnScreen(oldBounds);
                    } else {
                        Rect newBounds = accessibilityNodeInfo.getBoundsInScreen();
                        if (!oldBounds.equals(newBounds)) {
                            oldBounds.union(newBounds);
                            invalidateRectOnScreen(oldBounds);
                        }
                    }
                }
            }
        }
    }

    public void notifySubtreeAccessibilityStateChanged(View child, View source, int changeType) {
        postSendWindowContentChangedCallback((View) Preconditions.checkNotNull(source), changeType);
    }

    public boolean canResolveLayoutDirection() {
        return true;
    }

    public boolean isLayoutDirectionResolved() {
        return true;
    }

    public int getLayoutDirection() {
        return 0;
    }

    public boolean canResolveTextDirection() {
        return true;
    }

    public boolean isTextDirectionResolved() {
        return true;
    }

    public int getTextDirection() {
        return 1;
    }

    public boolean canResolveTextAlignment() {
        return true;
    }

    public boolean isTextAlignmentResolved() {
        return true;
    }

    public int getTextAlignment() {
        return 1;
    }

    private View getCommonPredecessor(View first, View second) {
        if (this.mTempHashSet == null) {
            this.mTempHashSet = new HashSet();
        }
        HashSet<View> seen = this.mTempHashSet;
        seen.clear();
        View firstCurrent = first;
        while (firstCurrent != null) {
            seen.add(firstCurrent);
            ViewParent firstCurrentParent = firstCurrent.mParent;
            if (firstCurrentParent instanceof View) {
                firstCurrent = (View) firstCurrentParent;
            } else {
                firstCurrent = null;
            }
        }
        View secondCurrent = second;
        while (secondCurrent != null) {
            if (seen.contains(secondCurrent)) {
                seen.clear();
                return secondCurrent;
            }
            ViewParent secondCurrentParent = secondCurrent.mParent;
            if (secondCurrentParent instanceof View) {
                secondCurrent = (View) secondCurrentParent;
            } else {
                secondCurrent = null;
            }
        }
        seen.clear();
        return null;
    }

    /* Access modifiers changed, original: 0000 */
    public void checkThread() {
        if (this.mThread != Thread.currentThread()) {
            throw new CalledFromWrongThreadException("Only the original thread that created a view hierarchy can touch its views.");
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
    }

    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        if (rectangle == null) {
            return scrollToRectOrFocus(null, immediate);
        }
        rectangle.offset(child.getLeft() - child.getScrollX(), child.getTop() - child.getScrollY());
        boolean scrolled = scrollToRectOrFocus(rectangle, immediate);
        this.mTempRect.set(rectangle);
        this.mTempRect.offset(0, -this.mCurScrollY);
        this.mTempRect.offset(this.mAttachInfo.mWindowLeft, this.mAttachInfo.mWindowTop);
        try {
            this.mWindowSession.onRectangleOnScreenRequested(this.mWindow, this.mTempRect);
        } catch (RemoteException e) {
        }
        return scrolled;
    }

    public void childHasTransientStateChanged(View child, boolean hasTransientState) {
    }

    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return false;
    }

    public void onStopNestedScroll(View target) {
    }

    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
    }

    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
    }

    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
    }

    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return false;
    }

    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return false;
    }

    public boolean onNestedPrePerformAccessibilityAction(View target, int action, Bundle args) {
        return false;
    }

    private void reportNextDraw() {
        if (!this.mReportNextDraw) {
            drawPending();
        }
        this.mReportNextDraw = true;
    }

    public void setReportNextDraw() {
        reportNextDraw();
        invalidate();
    }

    /* Access modifiers changed, original: 0000 */
    public void changeCanvasOpacity(boolean opaque) {
        String str = this.mTag;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("changeCanvasOpacity: opaque=");
        stringBuilder.append(opaque);
        Log.d(str, stringBuilder.toString());
        int i = opaque & ((this.mView.mPrivateFlags & 512) == 0 ? 1 : 0);
        if (this.mAttachInfo.mThreadedRenderer != null) {
            this.mAttachInfo.mThreadedRenderer.setOpaque(i);
        }
    }

    public boolean dispatchUnhandledKeyEvent(KeyEvent event) {
        return this.mUnhandledKeyManager.dispatch(this.mView, event);
    }

    static HandlerActionQueue getRunQueue() {
        HandlerActionQueue rq = (HandlerActionQueue) sRunQueues.get();
        if (rq != null) {
            return rq;
        }
        rq = new HandlerActionQueue();
        sRunQueues.set(rq);
        return rq;
    }

    private void startDragResizing(Rect initialBounds, boolean fullscreen, Rect systemInsets, Rect stableInsets, int resizeMode) {
        if (!this.mDragResizing) {
            this.mDragResizing = true;
            if (this.mUseMTRenderer) {
                for (int i = this.mWindowCallbacks.size() - 1; i >= 0; i--) {
                    ((WindowCallbacks) this.mWindowCallbacks.get(i)).onWindowDragResizeStart(initialBounds, fullscreen, systemInsets, stableInsets, resizeMode);
                }
            }
            this.mFullRedrawNeeded = true;
        }
    }

    private void endDragResizing() {
        if (this.mDragResizing) {
            this.mDragResizing = false;
            if (this.mUseMTRenderer) {
                for (int i = this.mWindowCallbacks.size() - 1; i >= 0; i--) {
                    ((WindowCallbacks) this.mWindowCallbacks.get(i)).onWindowDragResizeEnd();
                }
            }
            this.mFullRedrawNeeded = true;
        }
    }

    private boolean updateContentDrawBounds() {
        boolean updated = false;
        int i = 1;
        if (this.mUseMTRenderer) {
            for (int i2 = this.mWindowCallbacks.size() - 1; i2 >= 0; i2--) {
                updated |= ((WindowCallbacks) this.mWindowCallbacks.get(i2)).onContentDrawn(this.mWindowAttributes.surfaceInsets.left, this.mWindowAttributes.surfaceInsets.top, this.mWidth, this.mHeight);
            }
        }
        if (!(this.mDragResizing && this.mReportNextDraw)) {
            i = 0;
        }
        return updated | i;
    }

    private void requestDrawWindow() {
        if (this.mUseMTRenderer) {
            this.mWindowDrawCountDown = new CountDownLatch(this.mWindowCallbacks.size());
            for (int i = this.mWindowCallbacks.size() - 1; i >= 0; i--) {
                ((WindowCallbacks) this.mWindowCallbacks.get(i)).onRequestDraw(this.mReportNextDraw);
            }
        }
    }

    public void reportActivityRelaunched() {
        this.mActivityRelaunched = true;
    }

    public SurfaceControl getSurfaceControl() {
        return this.mSurfaceControl;
    }

    public void notifyContentChangeToContentCatcher() {
        View view = this.mView;
        if (view != null && (view instanceof DecorView)) {
            Context context = ((DecorView) view).getWindowContext();
            if (context != null && (context instanceof Activity)) {
                Activity a = (Activity) context;
                if (a.getInterceptor() != null) {
                    a.getInterceptor().notifyContentChange();
                }
            }
        }
    }

    public final void dispatchKeyEventToContentCatcher(KeyEvent event) {
        Activity attachedActivity = this.mView.getAttachedActivityInstance();
        if (attachedActivity != null && attachedActivity.getInterceptor() != null) {
            attachedActivity.getInterceptor().dispatchKeyEvent(event, this.mView, attachedActivity);
        }
    }
}
