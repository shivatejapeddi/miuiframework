package android.service.wallpaper;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.Service;
import android.app.WallpaperColors;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.service.wallpaper.IWallpaperEngine.Stub;
import android.util.Log;
import android.util.MergedConfiguration;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.DisplayCutout.ParcelableWrapper;
import android.view.IWindowSession;
import android.view.InputChannel;
import android.view.InputEvent;
import android.view.InputEventReceiver;
import android.view.InsetsState;
import android.view.MotionEvent;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.WindowInsets;
import android.view.WindowManager.LayoutParams;
import android.view.WindowManagerGlobal;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.os.HandlerCaller;
import com.android.internal.view.BaseIWindow;
import com.android.internal.view.BaseSurfaceHolder;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public abstract class WallpaperService extends Service {
    static final boolean DEBUG = false;
    private static final int DO_ATTACH = 10;
    private static final int DO_DETACH = 20;
    private static final int DO_IN_AMBIENT_MODE = 50;
    private static final int DO_SET_DESIRED_SIZE = 30;
    private static final int DO_SET_DISPLAY_PADDING = 40;
    private static final int MSG_REQUEST_WALLPAPER_COLORS = 10050;
    private static final int MSG_TOUCH_EVENT = 10040;
    private static final int MSG_UPDATE_SURFACE = 10000;
    private static final int MSG_VISIBILITY_CHANGED = 10010;
    private static final int MSG_WALLPAPER_COMMAND = 10025;
    private static final int MSG_WALLPAPER_OFFSETS = 10020;
    private static final int MSG_WINDOW_MOVED = 10035;
    @UnsupportedAppUsage
    private static final int MSG_WINDOW_RESIZED = 10030;
    private static final int NOTIFY_COLORS_RATE_LIMIT_MS = 1000;
    public static final String SERVICE_INTERFACE = "android.service.wallpaper.WallpaperService";
    public static final String SERVICE_META_DATA = "android.service.wallpaper";
    static final String TAG = "WallpaperService";
    private final ArrayList<Engine> mActiveEngines = new ArrayList();

    public class Engine {
        final Rect mBackdropFrame;
        HandlerCaller mCaller;
        private final Supplier<Long> mClockFunction;
        IWallpaperConnection mConnection;
        final Rect mContentInsets;
        boolean mCreated;
        int mCurHeight;
        int mCurWidth;
        int mCurWindowFlags;
        int mCurWindowPrivateFlags;
        boolean mDestroyed;
        final Rect mDispatchedContentInsets;
        DisplayCutout mDispatchedDisplayCutout;
        final Rect mDispatchedOutsets;
        final Rect mDispatchedOverscanInsets;
        final Rect mDispatchedStableInsets;
        private Display mDisplay;
        private Context mDisplayContext;
        final ParcelableWrapper mDisplayCutout;
        private final DisplayListener mDisplayListener;
        private int mDisplayState;
        boolean mDrawingAllowed;
        final Rect mFinalStableInsets;
        final Rect mFinalSystemInsets;
        boolean mFixedSizeAllowed;
        int mFormat;
        private final Handler mHandler;
        int mHeight;
        IWallpaperEngineWrapper mIWallpaperEngine;
        boolean mInitializing;
        InputChannel mInputChannel;
        WallpaperInputEventReceiver mInputEventReceiver;
        final InsetsState mInsetsState;
        boolean mIsCreating;
        boolean mIsInAmbientMode;
        private long mLastColorInvalidation;
        final LayoutParams mLayout;
        final Object mLock;
        final MergedConfiguration mMergedConfiguration;
        private final Runnable mNotifyColorsChanged;
        boolean mOffsetMessageEnqueued;
        boolean mOffsetsChanged;
        final Rect mOutsets;
        final Rect mOverscanInsets;
        MotionEvent mPendingMove;
        boolean mPendingSync;
        @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
        float mPendingXOffset;
        float mPendingXOffsetStep;
        float mPendingYOffset;
        float mPendingYOffsetStep;
        boolean mReportedVisible;
        IWindowSession mSession;
        final Rect mStableInsets;
        SurfaceControl mSurfaceControl;
        boolean mSurfaceCreated;
        final BaseSurfaceHolder mSurfaceHolder;
        int mType;
        boolean mVisible;
        final Rect mVisibleInsets;
        int mWidth;
        final Rect mWinFrame;
        final BaseIWindow mWindow;
        int mWindowFlags;
        int mWindowPrivateFlags;
        IBinder mWindowToken;

        final class WallpaperInputEventReceiver extends InputEventReceiver {
            public WallpaperInputEventReceiver(InputChannel inputChannel, Looper looper) {
                super(inputChannel, looper);
            }

            public void onInputEvent(InputEvent event) {
                boolean handled = false;
                try {
                    if ((event instanceof MotionEvent) && (event.getSource() & 2) != 0) {
                        Engine.this.dispatchPointer(MotionEvent.obtainNoHistory((MotionEvent) event));
                        handled = true;
                    }
                    finishInputEvent(event, handled);
                } catch (Throwable th) {
                    finishInputEvent(event, false);
                }
            }
        }

        public Engine(WallpaperService this$0) {
            this(-$$Lambda$87Do-TfJA3qVM7QF6F_6BpQlQTA.INSTANCE, Handler.getMain());
        }

        @VisibleForTesting
        public Engine(Supplier<Long> clockFunction, Handler handler) {
            this.mInitializing = true;
            this.mWindowFlags = 16;
            this.mWindowPrivateFlags = 4;
            this.mCurWindowFlags = this.mWindowFlags;
            this.mCurWindowPrivateFlags = this.mWindowPrivateFlags;
            this.mVisibleInsets = new Rect();
            this.mWinFrame = new Rect();
            this.mOverscanInsets = new Rect();
            this.mContentInsets = new Rect();
            this.mStableInsets = new Rect();
            this.mOutsets = new Rect();
            this.mDispatchedOverscanInsets = new Rect();
            this.mDispatchedContentInsets = new Rect();
            this.mDispatchedStableInsets = new Rect();
            this.mDispatchedOutsets = new Rect();
            this.mFinalSystemInsets = new Rect();
            this.mFinalStableInsets = new Rect();
            this.mBackdropFrame = new Rect();
            this.mDisplayCutout = new ParcelableWrapper();
            this.mDispatchedDisplayCutout = DisplayCutout.NO_CUTOUT;
            this.mInsetsState = new InsetsState();
            this.mMergedConfiguration = new MergedConfiguration();
            this.mLayout = new LayoutParams();
            this.mLock = new Object();
            this.mNotifyColorsChanged = new -$$Lambda$vsWBQpiXExY07tlrSzTqh4pNQAQ(this);
            this.mSurfaceControl = new SurfaceControl();
            this.mSurfaceHolder = new BaseSurfaceHolder() {
                {
                    this.mRequestedFormat = 2;
                }

                public boolean onAllowLockCanvas() {
                    return Engine.this.mDrawingAllowed;
                }

                public void onRelayoutContainer() {
                    Engine.this.mCaller.sendMessage(Engine.this.mCaller.obtainMessage(10000));
                }

                public void onUpdateSurface() {
                    Engine.this.mCaller.sendMessage(Engine.this.mCaller.obtainMessage(10000));
                }

                public boolean isCreating() {
                    return Engine.this.mIsCreating;
                }

                public void setFixedSize(int width, int height) {
                    if (Engine.this.mFixedSizeAllowed) {
                        super.setFixedSize(width, height);
                        return;
                    }
                    throw new UnsupportedOperationException("Wallpapers currently only support sizing from layout");
                }

                public void setKeepScreenOn(boolean screenOn) {
                    throw new UnsupportedOperationException("Wallpapers do not support keep screen on");
                }

                private void prepareToDraw() {
                    if (Engine.this.mDisplayState == 3 || Engine.this.mDisplayState == 4) {
                        try {
                            Engine.this.mSession.pokeDrawLock(Engine.this.mWindow);
                        } catch (RemoteException e) {
                        }
                    }
                }

                public Canvas lockCanvas() {
                    prepareToDraw();
                    return super.lockCanvas();
                }

                public Canvas lockCanvas(Rect dirty) {
                    prepareToDraw();
                    return super.lockCanvas(dirty);
                }

                public Canvas lockHardwareCanvas() {
                    prepareToDraw();
                    return super.lockHardwareCanvas();
                }
            };
            this.mWindow = new BaseIWindow() {
                public void resized(Rect frame, Rect overscanInsets, Rect contentInsets, Rect visibleInsets, Rect stableInsets, Rect outsets, boolean reportDraw, MergedConfiguration mergedConfiguration, Rect backDropRect, boolean forceLayout, boolean alwaysConsumeSystemBars, int displayId, ParcelableWrapper displayCutout) {
                    Engine.this.mCaller.sendMessage(Engine.this.mCaller.obtainMessageIO(10030, reportDraw, outsets));
                }

                public void moved(int newX, int newY) {
                    Engine.this.mCaller.sendMessage(Engine.this.mCaller.obtainMessageII(10035, newX, newY));
                }

                public void dispatchAppVisibility(boolean visible) {
                    if (!Engine.this.mIWallpaperEngine.mIsPreview) {
                        Engine.this.mCaller.sendMessage(Engine.this.mCaller.obtainMessageI(10010, visible));
                    }
                }

                public void dispatchWallpaperOffsets(float x, float y, float xStep, float yStep, boolean sync) {
                    synchronized (Engine.this.mLock) {
                        Engine.this.mPendingXOffset = x;
                        Engine.this.mPendingYOffset = y;
                        Engine.this.mPendingXOffsetStep = xStep;
                        Engine.this.mPendingYOffsetStep = yStep;
                        if (sync) {
                            Engine.this.mPendingSync = true;
                        }
                        if (!Engine.this.mOffsetMessageEnqueued) {
                            Engine.this.mOffsetMessageEnqueued = true;
                            Engine.this.mCaller.sendMessage(Engine.this.mCaller.obtainMessage(10020));
                        }
                    }
                }

                public void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras, boolean sync) {
                    synchronized (Engine.this.mLock) {
                        WallpaperCommand cmd = new WallpaperCommand();
                        cmd.action = action;
                        cmd.x = x;
                        cmd.y = y;
                        cmd.z = z;
                        cmd.extras = extras;
                        cmd.sync = sync;
                        Message msg = Engine.this.mCaller.obtainMessage(10025);
                        msg.obj = cmd;
                        Engine.this.mCaller.sendMessage(msg);
                    }
                }
            };
            this.mDisplayListener = new DisplayListener() {
                public void onDisplayChanged(int displayId) {
                    if (Engine.this.mDisplay.getDisplayId() == displayId) {
                        Engine.this.reportVisibility();
                    }
                }

                public void onDisplayRemoved(int displayId) {
                }

                public void onDisplayAdded(int displayId) {
                }
            };
            this.mClockFunction = clockFunction;
            this.mHandler = handler;
        }

        public SurfaceHolder getSurfaceHolder() {
            return this.mSurfaceHolder;
        }

        public int getDesiredMinimumWidth() {
            return this.mIWallpaperEngine.mReqWidth;
        }

        public int getDesiredMinimumHeight() {
            return this.mIWallpaperEngine.mReqHeight;
        }

        public boolean isVisible() {
            return this.mReportedVisible;
        }

        public boolean isPreview() {
            return this.mIWallpaperEngine.mIsPreview;
        }

        @SystemApi
        public boolean isInAmbientMode() {
            return this.mIsInAmbientMode;
        }

        public void setTouchEventsEnabled(boolean enabled) {
            int i;
            if (enabled) {
                i = this.mWindowFlags & -17;
            } else {
                i = this.mWindowFlags | 16;
            }
            this.mWindowFlags = i;
            if (this.mCreated) {
                updateSurface(false, false, false);
            }
        }

        public void setOffsetNotificationsEnabled(boolean enabled) {
            int i;
            if (enabled) {
                i = this.mWindowPrivateFlags | 4;
            } else {
                i = this.mWindowPrivateFlags & -5;
            }
            this.mWindowPrivateFlags = i;
            if (this.mCreated) {
                updateSurface(false, false, false);
            }
        }

        @UnsupportedAppUsage
        public void setFixedSizeAllowed(boolean allowed) {
            this.mFixedSizeAllowed = allowed;
        }

        public void onCreate(SurfaceHolder surfaceHolder) {
        }

        public void onDestroy() {
        }

        public void onVisibilityChanged(boolean visible) {
        }

        public void onApplyWindowInsets(WindowInsets insets) {
        }

        public void onTouchEvent(MotionEvent event) {
        }

        public void onOffsetsChanged(float xOffset, float yOffset, float xOffsetStep, float yOffsetStep, int xPixelOffset, int yPixelOffset) {
        }

        public Bundle onCommand(String action, int x, int y, int z, Bundle extras, boolean resultRequested) {
            return null;
        }

        @SystemApi
        public void onAmbientModeChanged(boolean inAmbientMode, long animationDuration) {
        }

        public void onDesiredSizeChanged(int desiredWidth, int desiredHeight) {
        }

        public void onSurfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        public void onSurfaceRedrawNeeded(SurfaceHolder holder) {
        }

        public void onSurfaceCreated(SurfaceHolder holder) {
        }

        public void onSurfaceDestroyed(SurfaceHolder holder) {
        }

        public void notifyColorsChanged() {
            long now = ((Long) this.mClockFunction.get()).longValue();
            int i = ((now - this.mLastColorInvalidation) > 1000 ? 1 : ((now - this.mLastColorInvalidation) == 1000 ? 0 : -1));
            String str = WallpaperService.TAG;
            if (i < 0) {
                Log.w(str, "This call has been deferred. You should only call notifyColorsChanged() once every 1.0 seconds.");
                if (!this.mHandler.hasCallbacks(this.mNotifyColorsChanged)) {
                    this.mHandler.postDelayed(this.mNotifyColorsChanged, 1000);
                }
                return;
            }
            this.mLastColorInvalidation = now;
            this.mHandler.removeCallbacks(this.mNotifyColorsChanged);
            try {
                WallpaperColors newColors = onComputeColors();
                if (this.mConnection != null) {
                    this.mConnection.onWallpaperColorsChanged(newColors, this.mDisplay.getDisplayId());
                } else {
                    Log.w(str, "Can't notify system because wallpaper connection was not established.");
                }
            } catch (RemoteException e) {
                Log.w(str, "Can't notify system because wallpaper connection was lost.", e);
            }
        }

        public WallpaperColors onComputeColors() {
            return null;
        }

        @VisibleForTesting
        public void setCreated(boolean created) {
            this.mCreated = created;
        }

        /* Access modifiers changed, original: protected */
        public void dump(String prefix, FileDescriptor fd, PrintWriter out, String[] args) {
            out.print(prefix);
            out.print("mInitializing=");
            out.print(this.mInitializing);
            out.print(" mDestroyed=");
            out.println(this.mDestroyed);
            out.print(prefix);
            out.print("mVisible=");
            out.print(this.mVisible);
            out.print(" mReportedVisible=");
            out.println(this.mReportedVisible);
            out.print(prefix);
            out.print("mDisplay=");
            out.println(this.mDisplay);
            out.print(prefix);
            out.print("mCreated=");
            out.print(this.mCreated);
            out.print(" mSurfaceCreated=");
            out.print(this.mSurfaceCreated);
            out.print(" mIsCreating=");
            out.print(this.mIsCreating);
            out.print(" mDrawingAllowed=");
            out.println(this.mDrawingAllowed);
            out.print(prefix);
            out.print("mWidth=");
            out.print(this.mWidth);
            out.print(" mCurWidth=");
            out.print(this.mCurWidth);
            out.print(" mHeight=");
            out.print(this.mHeight);
            out.print(" mCurHeight=");
            out.println(this.mCurHeight);
            out.print(prefix);
            out.print("mType=");
            out.print(this.mType);
            out.print(" mWindowFlags=");
            out.print(this.mWindowFlags);
            out.print(" mCurWindowFlags=");
            out.println(this.mCurWindowFlags);
            out.print(prefix);
            out.print("mWindowPrivateFlags=");
            out.print(this.mWindowPrivateFlags);
            out.print(" mCurWindowPrivateFlags=");
            out.println(this.mCurWindowPrivateFlags);
            out.print(prefix);
            out.print("mVisibleInsets=");
            out.print(this.mVisibleInsets.toShortString());
            out.print(" mWinFrame=");
            out.print(this.mWinFrame.toShortString());
            out.print(" mContentInsets=");
            out.println(this.mContentInsets.toShortString());
            out.print(prefix);
            out.print("mConfiguration=");
            out.println(this.mMergedConfiguration.getMergedConfiguration());
            out.print(prefix);
            out.print("mLayout=");
            out.println(this.mLayout);
            synchronized (this.mLock) {
                out.print(prefix);
                out.print("mPendingXOffset=");
                out.print(this.mPendingXOffset);
                out.print(" mPendingXOffset=");
                out.println(this.mPendingXOffset);
                out.print(prefix);
                out.print("mPendingXOffsetStep=");
                out.print(this.mPendingXOffsetStep);
                out.print(" mPendingXOffsetStep=");
                out.println(this.mPendingXOffsetStep);
                out.print(prefix);
                out.print("mOffsetMessageEnqueued=");
                out.print(this.mOffsetMessageEnqueued);
                out.print(" mPendingSync=");
                out.println(this.mPendingSync);
                if (this.mPendingMove != null) {
                    out.print(prefix);
                    out.print("mPendingMove=");
                    out.println(this.mPendingMove);
                }
            }
        }

        private void dispatchPointer(MotionEvent event) {
            if (event.isTouchEvent()) {
                synchronized (this.mLock) {
                    if (event.getAction() == 2) {
                        this.mPendingMove = event;
                    } else {
                        this.mPendingMove = null;
                    }
                }
                this.mCaller.sendMessage(this.mCaller.obtainMessageO(10040, event));
                return;
            }
            event.recycle();
        }

        /* Access modifiers changed, original: 0000 */
        /* JADX WARNING: Removed duplicated region for block: B:226:0x05d3 A:{Catch:{ RemoteException -> 0x05e0 }} */
        public void updateSurface(boolean r59, boolean r60, boolean r61) {
            /*
            r58 = this;
            r1 = r58;
            r0 = r1.mDestroyed;
            r2 = "WallpaperService";
            if (r0 == 0) goto L_0x000d;
        L_0x0008:
            r0 = "Ignoring updateSurface: destroyed";
            android.util.Log.w(r2, r0);
        L_0x000d:
            r0 = 0;
            r3 = r1.mSurfaceHolder;
            r3 = r3.getRequestedWidth();
            if (r3 > 0) goto L_0x0018;
        L_0x0016:
            r3 = -1;
            goto L_0x0019;
        L_0x0018:
            r0 = 1;
        L_0x0019:
            r4 = r1.mSurfaceHolder;
            r4 = r4.getRequestedHeight();
            if (r4 > 0) goto L_0x0024;
        L_0x0021:
            r4 = -1;
            r5 = r0;
            goto L_0x0026;
        L_0x0024:
            r0 = 1;
            r5 = r0;
        L_0x0026:
            r0 = r1.mCreated;
            r6 = 1;
            r0 = r0 ^ r6;
            r7 = r0;
            r0 = r1.mSurfaceCreated;
            r0 = r0 ^ r6;
            r8 = r0;
            r0 = r1.mFormat;
            r9 = r1.mSurfaceHolder;
            r9 = r9.getRequestedFormat();
            r10 = 0;
            if (r0 == r9) goto L_0x003c;
        L_0x003a:
            r0 = r6;
            goto L_0x003d;
        L_0x003c:
            r0 = r10;
        L_0x003d:
            r9 = r0;
            r0 = r1.mWidth;
            if (r0 != r3) goto L_0x0049;
        L_0x0042:
            r0 = r1.mHeight;
            if (r0 == r4) goto L_0x0047;
        L_0x0046:
            goto L_0x0049;
        L_0x0047:
            r0 = r10;
            goto L_0x004a;
        L_0x0049:
            r0 = r6;
        L_0x004a:
            r11 = r0;
            r0 = r1.mCreated;
            r0 = r0 ^ r6;
            r12 = r0;
            r0 = r1.mType;
            r13 = r1.mSurfaceHolder;
            r13 = r13.getRequestedType();
            if (r0 == r13) goto L_0x005b;
        L_0x0059:
            r0 = r6;
            goto L_0x005c;
        L_0x005b:
            r0 = r10;
        L_0x005c:
            r13 = r0;
            r0 = r1.mCurWindowFlags;
            r14 = r1.mWindowFlags;
            if (r0 != r14) goto L_0x006c;
        L_0x0063:
            r0 = r1.mCurWindowPrivateFlags;
            r14 = r1.mWindowPrivateFlags;
            if (r0 == r14) goto L_0x006a;
        L_0x0069:
            goto L_0x006c;
        L_0x006a:
            r0 = r10;
            goto L_0x006d;
        L_0x006c:
            r0 = r6;
        L_0x006d:
            r14 = r0;
            if (r59 != 0) goto L_0x0099;
        L_0x0070:
            if (r7 != 0) goto L_0x0099;
        L_0x0072:
            if (r8 != 0) goto L_0x0099;
        L_0x0074:
            if (r9 != 0) goto L_0x0099;
        L_0x0076:
            if (r11 != 0) goto L_0x0099;
        L_0x0078:
            if (r13 != 0) goto L_0x0099;
        L_0x007a:
            if (r14 != 0) goto L_0x0099;
        L_0x007c:
            if (r61 != 0) goto L_0x0099;
        L_0x007e:
            r0 = r1.mIWallpaperEngine;
            r0 = r0.mShownReported;
            if (r0 != 0) goto L_0x0085;
        L_0x0084:
            goto L_0x0099;
        L_0x0085:
            r6 = r61;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            r34 = r7;
            r35 = r8;
            r33 = r9;
            r31 = r13;
            r32 = r14;
            goto L_0x062e;
        L_0x0099:
            r1.mWidth = r3;	 Catch:{ RemoteException -> 0x0617 }
            r1.mHeight = r4;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r1.mSurfaceHolder;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r0.getRequestedFormat();	 Catch:{ RemoteException -> 0x0617 }
            r1.mFormat = r0;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r1.mSurfaceHolder;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r0.getRequestedType();	 Catch:{ RemoteException -> 0x0617 }
            r1.mType = r0;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x0617 }
            r0.x = r10;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x0617 }
            r0.y = r10;	 Catch:{ RemoteException -> 0x0617 }
            if (r5 != 0) goto L_0x00d5;
        L_0x00b7:
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x00c0 }
            r0.width = r3;	 Catch:{ RemoteException -> 0x00c0 }
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x00c0 }
            r0.height = r4;	 Catch:{ RemoteException -> 0x00c0 }
            goto L_0x0101;
        L_0x00c0:
            r0 = move-exception;
            r6 = r61;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            r34 = r7;
            r35 = r8;
            r33 = r9;
            r31 = r13;
            r32 = r14;
            goto L_0x062e;
        L_0x00d5:
            r0 = new android.view.DisplayInfo;	 Catch:{ RemoteException -> 0x0617 }
            r0.<init>();	 Catch:{ RemoteException -> 0x0617 }
            r15 = r1.mDisplay;	 Catch:{ RemoteException -> 0x0617 }
            r15.getDisplayInfo(r0);	 Catch:{ RemoteException -> 0x0617 }
            r15 = r0.logicalHeight;	 Catch:{ RemoteException -> 0x0617 }
            r15 = (float) r15;	 Catch:{ RemoteException -> 0x0617 }
            r10 = (float) r4;	 Catch:{ RemoteException -> 0x0617 }
            r15 = r15 / r10;
            r10 = r0.logicalWidth;	 Catch:{ RemoteException -> 0x0617 }
            r10 = (float) r10;	 Catch:{ RemoteException -> 0x0617 }
            r6 = (float) r3;	 Catch:{ RemoteException -> 0x0617 }
            r10 = r10 / r6;
            r6 = java.lang.Math.max(r15, r10);	 Catch:{ RemoteException -> 0x0617 }
            r10 = r1.mLayout;	 Catch:{ RemoteException -> 0x0617 }
            r15 = (float) r4;	 Catch:{ RemoteException -> 0x0617 }
            r15 = r15 * r6;
            r15 = (int) r15;	 Catch:{ RemoteException -> 0x0617 }
            r10.height = r15;	 Catch:{ RemoteException -> 0x0617 }
            r10 = r1.mLayout;	 Catch:{ RemoteException -> 0x0617 }
            r15 = (float) r3;	 Catch:{ RemoteException -> 0x0617 }
            r15 = r15 * r6;
            r15 = (int) r15;	 Catch:{ RemoteException -> 0x0617 }
            r10.width = r15;	 Catch:{ RemoteException -> 0x0617 }
            r10 = r1.mWindowFlags;	 Catch:{ RemoteException -> 0x0617 }
            r10 = r10 | 16384;
            r1.mWindowFlags = r10;	 Catch:{ RemoteException -> 0x0617 }
        L_0x0101:
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x0617 }
            r6 = r1.mFormat;	 Catch:{ RemoteException -> 0x0617 }
            r0.format = r6;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r1.mWindowFlags;	 Catch:{ RemoteException -> 0x0617 }
            r1.mCurWindowFlags = r0;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x0617 }
            r6 = r1.mWindowFlags;	 Catch:{ RemoteException -> 0x0617 }
            r6 = r6 | 512;
            r10 = 65536; // 0x10000 float:9.18355E-41 double:3.2379E-319;
            r6 = r6 | r10;
            r6 = r6 | 256;
            r6 = r6 | 8;
            r0.flags = r6;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r1.mWindowPrivateFlags;	 Catch:{ RemoteException -> 0x0617 }
            r1.mCurWindowPrivateFlags = r0;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x0617 }
            r6 = r1.mWindowPrivateFlags;	 Catch:{ RemoteException -> 0x0617 }
            r0.privateFlags = r6;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x0617 }
            r6 = r1.mType;	 Catch:{ RemoteException -> 0x0617 }
            r0.memoryType = r6;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x0617 }
            r6 = r1.mWindowToken;	 Catch:{ RemoteException -> 0x0617 }
            r0.token = r6;	 Catch:{ RemoteException -> 0x0617 }
            r0 = r1.mCreated;	 Catch:{ RemoteException -> 0x0617 }
            if (r0 != 0) goto L_0x0260;
        L_0x0134:
            r0 = android.service.wallpaper.WallpaperService.this;	 Catch:{ RemoteException -> 0x0247 }
            r6 = com.android.internal.R.styleable.Window;	 Catch:{ RemoteException -> 0x0247 }
            r0 = r0.obtainStyledAttributes(r6);	 Catch:{ RemoteException -> 0x0247 }
            r0.recycle();	 Catch:{ RemoteException -> 0x0247 }
            r6 = r1.mLayout;	 Catch:{ RemoteException -> 0x0247 }
            r10 = r1.mIWallpaperEngine;	 Catch:{ RemoteException -> 0x0247 }
            r10 = r10.mWindowType;	 Catch:{ RemoteException -> 0x0247 }
            r6.type = r10;	 Catch:{ RemoteException -> 0x0247 }
            r6 = r1.mLayout;	 Catch:{ RemoteException -> 0x0247 }
            r10 = 8388659; // 0x800033 float:1.1755015E-38 double:4.144548E-317;
            r6.gravity = r10;	 Catch:{ RemoteException -> 0x0247 }
            r6 = r1.mLayout;	 Catch:{ RemoteException -> 0x0247 }
            r10 = android.service.wallpaper.WallpaperService.this;	 Catch:{ RemoteException -> 0x0247 }
            r10 = r10.getClass();	 Catch:{ RemoteException -> 0x0247 }
            r10 = r10.getName();	 Catch:{ RemoteException -> 0x0247 }
            r6.setTitle(r10);	 Catch:{ RemoteException -> 0x0247 }
            r6 = r1.mLayout;	 Catch:{ RemoteException -> 0x0247 }
            r10 = 16974606; // 0x103030e float:2.4063092E-38 double:8.3865697E-317;
            r6.windowAnimations = r10;	 Catch:{ RemoteException -> 0x0247 }
            r6 = new android.view.InputChannel;	 Catch:{ RemoteException -> 0x0247 }
            r6.<init>();	 Catch:{ RemoteException -> 0x0247 }
            r1.mInputChannel = r6;	 Catch:{ RemoteException -> 0x0247 }
            r6 = r1.mSession;	 Catch:{ RemoteException -> 0x0247 }
            r10 = r1.mWindow;	 Catch:{ RemoteException -> 0x0247 }
            r15 = r1.mWindow;	 Catch:{ RemoteException -> 0x0247 }
            r15 = r15.mSeq;	 Catch:{ RemoteException -> 0x0247 }
            r29 = r0;
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x0247 }
            r20 = 0;
            r30 = r11;
            r11 = r1.mDisplay;	 Catch:{ RemoteException -> 0x022e }
            r21 = r11.getDisplayId();	 Catch:{ RemoteException -> 0x022e }
            r11 = r1.mWinFrame;	 Catch:{ RemoteException -> 0x022e }
            r31 = r13;
            r13 = r1.mContentInsets;	 Catch:{ RemoteException -> 0x0217 }
            r32 = r14;
            r14 = r1.mStableInsets;	 Catch:{ RemoteException -> 0x0202 }
            r33 = r9;
            r9 = r1.mOutsets;	 Catch:{ RemoteException -> 0x01ef }
            r34 = r7;
            r7 = r1.mDisplayCutout;	 Catch:{ RemoteException -> 0x01de }
            r35 = r8;
            r8 = r1.mInputChannel;	 Catch:{ RemoteException -> 0x01cf }
            r36 = r12;
            r12 = r1.mInsetsState;	 Catch:{ RemoteException -> 0x02ba }
            r16 = r6;
            r17 = r10;
            r18 = r15;
            r19 = r0;
            r22 = r11;
            r23 = r13;
            r24 = r14;
            r25 = r9;
            r26 = r7;
            r27 = r8;
            r28 = r12;
            r0 = r16.addToDisplay(r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28);	 Catch:{ RemoteException -> 0x02ba }
            if (r0 >= 0) goto L_0x01bd;
        L_0x01b7:
            r0 = "Failed to add window while updating wallpaper surface.";
            android.util.Log.w(r2, r0);	 Catch:{ RemoteException -> 0x02ba }
            return;
        L_0x01bd:
            r2 = 1;
            r1.mCreated = r2;	 Catch:{ RemoteException -> 0x02ba }
            r0 = new android.service.wallpaper.WallpaperService$Engine$WallpaperInputEventReceiver;	 Catch:{ RemoteException -> 0x02ba }
            r2 = r1.mInputChannel;	 Catch:{ RemoteException -> 0x02ba }
            r6 = android.os.Looper.myLooper();	 Catch:{ RemoteException -> 0x02ba }
            r0.<init>(r2, r6);	 Catch:{ RemoteException -> 0x02ba }
            r1.mInputEventReceiver = r0;	 Catch:{ RemoteException -> 0x02ba }
            goto L_0x026e;
        L_0x01cf:
            r0 = move-exception;
            r36 = r12;
            r6 = r61;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            r11 = r30;
            goto L_0x062e;
        L_0x01de:
            r0 = move-exception;
            r35 = r8;
            r36 = r12;
            r6 = r61;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            r11 = r30;
            goto L_0x062e;
        L_0x01ef:
            r0 = move-exception;
            r34 = r7;
            r35 = r8;
            r36 = r12;
            r6 = r61;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            r11 = r30;
            goto L_0x062e;
        L_0x0202:
            r0 = move-exception;
            r34 = r7;
            r35 = r8;
            r33 = r9;
            r36 = r12;
            r6 = r61;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            r11 = r30;
            goto L_0x062e;
        L_0x0217:
            r0 = move-exception;
            r34 = r7;
            r35 = r8;
            r33 = r9;
            r36 = r12;
            r32 = r14;
            r6 = r61;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            r11 = r30;
            goto L_0x062e;
        L_0x022e:
            r0 = move-exception;
            r34 = r7;
            r35 = r8;
            r33 = r9;
            r36 = r12;
            r31 = r13;
            r32 = r14;
            r6 = r61;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            r11 = r30;
            goto L_0x062e;
        L_0x0247:
            r0 = move-exception;
            r34 = r7;
            r35 = r8;
            r33 = r9;
            r30 = r11;
            r36 = r12;
            r31 = r13;
            r32 = r14;
            r6 = r61;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            goto L_0x062e;
        L_0x0260:
            r34 = r7;
            r35 = r8;
            r33 = r9;
            r30 = r11;
            r36 = r12;
            r31 = r13;
            r32 = r14;
        L_0x026e:
            r0 = r1.mSurfaceHolder;	 Catch:{ RemoteException -> 0x0609 }
            r0 = r0.mSurfaceLock;	 Catch:{ RemoteException -> 0x0609 }
            r0.lock();	 Catch:{ RemoteException -> 0x0609 }
            r2 = 1;
            r1.mDrawingAllowed = r2;	 Catch:{ RemoteException -> 0x0609 }
            if (r5 != 0) goto L_0x02c9;
        L_0x027a:
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x02ba }
            r0 = r0.surfaceInsets;	 Catch:{ RemoteException -> 0x02ba }
            r2 = r1.mIWallpaperEngine;	 Catch:{ RemoteException -> 0x02ba }
            r2 = r2.mDisplayPadding;	 Catch:{ RemoteException -> 0x02ba }
            r0.set(r2);	 Catch:{ RemoteException -> 0x02ba }
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x02ba }
            r0 = r0.surfaceInsets;	 Catch:{ RemoteException -> 0x02ba }
            r2 = r0.left;	 Catch:{ RemoteException -> 0x02ba }
            r6 = r1.mOutsets;	 Catch:{ RemoteException -> 0x02ba }
            r6 = r6.left;	 Catch:{ RemoteException -> 0x02ba }
            r2 = r2 + r6;
            r0.left = r2;	 Catch:{ RemoteException -> 0x02ba }
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x02ba }
            r0 = r0.surfaceInsets;	 Catch:{ RemoteException -> 0x02ba }
            r2 = r0.top;	 Catch:{ RemoteException -> 0x02ba }
            r6 = r1.mOutsets;	 Catch:{ RemoteException -> 0x02ba }
            r6 = r6.top;	 Catch:{ RemoteException -> 0x02ba }
            r2 = r2 + r6;
            r0.top = r2;	 Catch:{ RemoteException -> 0x02ba }
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x02ba }
            r0 = r0.surfaceInsets;	 Catch:{ RemoteException -> 0x02ba }
            r2 = r0.right;	 Catch:{ RemoteException -> 0x02ba }
            r6 = r1.mOutsets;	 Catch:{ RemoteException -> 0x02ba }
            r6 = r6.right;	 Catch:{ RemoteException -> 0x02ba }
            r2 = r2 + r6;
            r0.right = r2;	 Catch:{ RemoteException -> 0x02ba }
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x02ba }
            r0 = r0.surfaceInsets;	 Catch:{ RemoteException -> 0x02ba }
            r2 = r0.bottom;	 Catch:{ RemoteException -> 0x02ba }
            r6 = r1.mOutsets;	 Catch:{ RemoteException -> 0x02ba }
            r6 = r6.bottom;	 Catch:{ RemoteException -> 0x02ba }
            r2 = r2 + r6;
            r0.bottom = r2;	 Catch:{ RemoteException -> 0x02ba }
            goto L_0x02d1;
        L_0x02ba:
            r0 = move-exception;
            r6 = r61;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            r11 = r30;
            r12 = r36;
            goto L_0x062e;
        L_0x02c9:
            r0 = r1.mLayout;	 Catch:{ RemoteException -> 0x0609 }
            r0 = r0.surfaceInsets;	 Catch:{ RemoteException -> 0x0609 }
            r2 = 0;
            r0.set(r2, r2, r2, r2);	 Catch:{ RemoteException -> 0x0609 }
        L_0x02d1:
            r0 = r1.mSession;	 Catch:{ RemoteException -> 0x0609 }
            r2 = r1.mWindow;	 Catch:{ RemoteException -> 0x0609 }
            r6 = r1.mWindow;	 Catch:{ RemoteException -> 0x0609 }
            r6 = r6.mSeq;	 Catch:{ RemoteException -> 0x0609 }
            r7 = r1.mLayout;	 Catch:{ RemoteException -> 0x0609 }
            r8 = r1.mWidth;	 Catch:{ RemoteException -> 0x0609 }
            r9 = r1.mHeight;	 Catch:{ RemoteException -> 0x0609 }
            r43 = 0;
            r44 = 0;
            r45 = -1;
            r10 = r1.mWinFrame;	 Catch:{ RemoteException -> 0x0609 }
            r11 = r1.mOverscanInsets;	 Catch:{ RemoteException -> 0x0609 }
            r12 = r1.mContentInsets;	 Catch:{ RemoteException -> 0x0609 }
            r13 = r1.mVisibleInsets;	 Catch:{ RemoteException -> 0x0609 }
            r14 = r1.mStableInsets;	 Catch:{ RemoteException -> 0x0609 }
            r15 = r1.mOutsets;	 Catch:{ RemoteException -> 0x0609 }
            r16 = r4;
            r4 = r1.mBackdropFrame;	 Catch:{ RemoteException -> 0x05fd }
            r17 = r3;
            r3 = r1.mDisplayCutout;	 Catch:{ RemoteException -> 0x05f3 }
            r18 = r5;
            r5 = r1.mMergedConfiguration;	 Catch:{ RemoteException -> 0x05eb }
            r19 = r5;
            r5 = r1.mSurfaceControl;	 Catch:{ RemoteException -> 0x05eb }
            r20 = r5;
            r5 = r1.mInsetsState;	 Catch:{ RemoteException -> 0x05eb }
            r37 = r0;
            r38 = r2;
            r39 = r6;
            r40 = r7;
            r41 = r8;
            r42 = r9;
            r47 = r10;
            r48 = r11;
            r49 = r12;
            r50 = r13;
            r51 = r14;
            r52 = r15;
            r53 = r4;
            r54 = r3;
            r55 = r19;
            r56 = r20;
            r57 = r5;
            r0 = r37.relayout(r38, r39, r40, r41, r42, r43, r44, r45, r47, r48, r49, r50, r51, r52, r53, r54, r55, r56, r57);	 Catch:{ RemoteException -> 0x05eb }
            r2 = r0;
            r0 = r1.mSurfaceControl;	 Catch:{ RemoteException -> 0x05eb }
            r0 = r0.isValid();	 Catch:{ RemoteException -> 0x05eb }
            if (r0 == 0) goto L_0x0342;
        L_0x0334:
            r0 = r1.mSurfaceHolder;	 Catch:{ RemoteException -> 0x05eb }
            r0 = r0.mSurface;	 Catch:{ RemoteException -> 0x05eb }
            r3 = r1.mSurfaceControl;	 Catch:{ RemoteException -> 0x05eb }
            r0.copyFrom(r3);	 Catch:{ RemoteException -> 0x05eb }
            r0 = r1.mSurfaceControl;	 Catch:{ RemoteException -> 0x05eb }
            r0.release();	 Catch:{ RemoteException -> 0x05eb }
        L_0x0342:
            r0 = r1.mWinFrame;	 Catch:{ RemoteException -> 0x05eb }
            r0 = r0.width();	 Catch:{ RemoteException -> 0x05eb }
            r3 = r1.mWinFrame;	 Catch:{ RemoteException -> 0x05eb }
            r3 = r3.height();	 Catch:{ RemoteException -> 0x05eb }
            if (r18 != 0) goto L_0x03fe;
        L_0x0350:
            r4 = r1.mIWallpaperEngine;	 Catch:{ RemoteException -> 0x05eb }
            r4 = r4.mDisplayPadding;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r4.left;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r4.right;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r5 + r6;
            r6 = r1.mOutsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6.left;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r5 + r6;
            r6 = r1.mOutsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6.right;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r5 + r6;
            r0 = r0 + r5;
            r5 = r4.top;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r4.bottom;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r5 + r6;
            r6 = r1.mOutsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6.top;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r5 + r6;
            r6 = r1.mOutsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6.bottom;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r5 + r6;
            r3 = r3 + r5;
            r5 = r1.mOverscanInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.left;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.left;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.left = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mOverscanInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.top;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.top;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.top = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mOverscanInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.right;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.right;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.right = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mOverscanInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.bottom;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.bottom;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.bottom = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mContentInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.left;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.left;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.left = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mContentInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.top;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.top;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.top = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mContentInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.right;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.right;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.right = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mContentInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.bottom;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.bottom;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.bottom = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mStableInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.left;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.left;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.left = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mStableInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.top;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.top;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.top = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mStableInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.right;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.right;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.right = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mStableInsets;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r5.bottom;	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.bottom;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6 + r7;
            r5.bottom = r6;	 Catch:{ RemoteException -> 0x05eb }
            r5 = r1.mDisplayCutout;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r1.mDisplayCutout;	 Catch:{ RemoteException -> 0x05eb }
            r6 = r6.get();	 Catch:{ RemoteException -> 0x05eb }
            r7 = r4.left;	 Catch:{ RemoteException -> 0x05eb }
            r7 = -r7;
            r8 = r4.top;	 Catch:{ RemoteException -> 0x05eb }
            r8 = -r8;
            r9 = r4.right;	 Catch:{ RemoteException -> 0x05eb }
            r9 = -r9;
            r10 = r4.bottom;	 Catch:{ RemoteException -> 0x05eb }
            r10 = -r10;
            r6 = r6.inset(r7, r8, r9, r10);	 Catch:{ RemoteException -> 0x05eb }
            r5.set(r6);	 Catch:{ RemoteException -> 0x05eb }
            r4 = r3;
            r3 = r0;
            goto L_0x0404;
        L_0x03fe:
            r0 = r17;
            r3 = r16;
            r4 = r3;
            r3 = r0;
        L_0x0404:
            r0 = r1.mCurWidth;	 Catch:{ RemoteException -> 0x05eb }
            if (r0 == r3) goto L_0x0415;
        L_0x0408:
            r11 = 1;
            r1.mCurWidth = r3;	 Catch:{ RemoteException -> 0x040e }
            r30 = r11;
            goto L_0x0415;
        L_0x040e:
            r0 = move-exception;
            r6 = r61;
            r12 = r36;
            goto L_0x062e;
        L_0x0415:
            r0 = r1.mCurHeight;	 Catch:{ RemoteException -> 0x05eb }
            if (r0 == r4) goto L_0x0426;
        L_0x0419:
            r5 = 1;
            r1.mCurHeight = r4;	 Catch:{ RemoteException -> 0x041e }
            r11 = r5;
            goto L_0x0428;
        L_0x041e:
            r0 = move-exception;
            r6 = r61;
            r11 = r5;
            r12 = r36;
            goto L_0x062e;
        L_0x0426:
            r11 = r30;
        L_0x0428:
            r0 = r1.mDispatchedOverscanInsets;	 Catch:{ RemoteException -> 0x040e }
            r5 = r1.mOverscanInsets;	 Catch:{ RemoteException -> 0x040e }
            r0 = r0.equals(r5);	 Catch:{ RemoteException -> 0x040e }
            if (r0 != 0) goto L_0x0434;
        L_0x0432:
            r0 = 1;
            goto L_0x0435;
        L_0x0434:
            r0 = 0;
        L_0x0435:
            r5 = r36 | r0;
            r0 = r1.mDispatchedContentInsets;	 Catch:{ RemoteException -> 0x05e6 }
            r6 = r1.mContentInsets;	 Catch:{ RemoteException -> 0x05e6 }
            r0 = r0.equals(r6);	 Catch:{ RemoteException -> 0x05e6 }
            if (r0 != 0) goto L_0x0443;
        L_0x0441:
            r0 = 1;
            goto L_0x0444;
        L_0x0443:
            r0 = 0;
        L_0x0444:
            r5 = r5 | r0;
            r0 = r1.mDispatchedStableInsets;	 Catch:{ RemoteException -> 0x05e6 }
            r6 = r1.mStableInsets;	 Catch:{ RemoteException -> 0x05e6 }
            r0 = r0.equals(r6);	 Catch:{ RemoteException -> 0x05e6 }
            if (r0 != 0) goto L_0x0451;
        L_0x044f:
            r0 = 1;
            goto L_0x0452;
        L_0x0451:
            r0 = 0;
        L_0x0452:
            r5 = r5 | r0;
            r0 = r1.mDispatchedOutsets;	 Catch:{ RemoteException -> 0x05e6 }
            r6 = r1.mOutsets;	 Catch:{ RemoteException -> 0x05e6 }
            r0 = r0.equals(r6);	 Catch:{ RemoteException -> 0x05e6 }
            if (r0 != 0) goto L_0x045f;
        L_0x045d:
            r0 = 1;
            goto L_0x0460;
        L_0x045f:
            r0 = 0;
        L_0x0460:
            r5 = r5 | r0;
            r0 = r1.mDispatchedDisplayCutout;	 Catch:{ RemoteException -> 0x05e6 }
            r6 = r1.mDisplayCutout;	 Catch:{ RemoteException -> 0x05e6 }
            r6 = r6.get();	 Catch:{ RemoteException -> 0x05e6 }
            r0 = r0.equals(r6);	 Catch:{ RemoteException -> 0x05e6 }
            if (r0 != 0) goto L_0x0471;
        L_0x046f:
            r0 = 1;
            goto L_0x0472;
        L_0x0471:
            r0 = 0;
        L_0x0472:
            r12 = r5 | r0;
            r0 = r1.mSurfaceHolder;	 Catch:{ RemoteException -> 0x05e2 }
            r0.setSurfaceFrameSize(r3, r4);	 Catch:{ RemoteException -> 0x05e2 }
            r0 = r1.mSurfaceHolder;	 Catch:{ RemoteException -> 0x05e2 }
            r0 = r0.mSurfaceLock;	 Catch:{ RemoteException -> 0x05e2 }
            r0.unlock();	 Catch:{ RemoteException -> 0x05e2 }
            r0 = r1.mSurfaceHolder;	 Catch:{ RemoteException -> 0x05e2 }
            r0 = r0.mSurface;	 Catch:{ RemoteException -> 0x05e2 }
            r0 = r0.isValid();	 Catch:{ RemoteException -> 0x05e2 }
            if (r0 != 0) goto L_0x048e;
        L_0x048a:
            r58.reportSurfaceDestroyed();	 Catch:{ RemoteException -> 0x05e2 }
            return;
        L_0x048e:
            r5 = 0;
            r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x05c8 }
            r0.ungetCallbacks();	 Catch:{ all -> 0x05c8 }
            if (r35 == 0) goto L_0x04b5;
        L_0x0496:
            r6 = 1;
            r1.mIsCreating = r6;	 Catch:{ all -> 0x05c8 }
            r5 = 1;
            r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x05c8 }
            r1.onSurfaceCreated(r0);	 Catch:{ all -> 0x05c8 }
            r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x05c8 }
            r0 = r0.getCallbacks();	 Catch:{ all -> 0x05c8 }
            if (r0 == 0) goto L_0x04b5;
        L_0x04a7:
            r6 = r0.length;	 Catch:{ all -> 0x05c8 }
            r7 = 0;
        L_0x04a9:
            if (r7 >= r6) goto L_0x04b5;
        L_0x04ab:
            r8 = r0[r7];	 Catch:{ all -> 0x05c8 }
            r9 = r1.mSurfaceHolder;	 Catch:{ all -> 0x05c8 }
            r8.surfaceCreated(r9);	 Catch:{ all -> 0x05c8 }
            r7 = r7 + 1;
            goto L_0x04a9;
        L_0x04b5:
            if (r34 != 0) goto L_0x04be;
        L_0x04b7:
            r0 = r2 & 2;
            if (r0 == 0) goto L_0x04bc;
        L_0x04bb:
            goto L_0x04be;
        L_0x04bc:
            r0 = 0;
            goto L_0x04bf;
        L_0x04be:
            r0 = 1;
        L_0x04bf:
            r6 = r61 | r0;
            if (r60 != 0) goto L_0x04cb;
        L_0x04c3:
            if (r34 != 0) goto L_0x04cb;
        L_0x04c5:
            if (r35 != 0) goto L_0x04cb;
        L_0x04c7:
            if (r33 != 0) goto L_0x04cb;
        L_0x04c9:
            if (r11 == 0) goto L_0x04f3;
        L_0x04cb:
            r5 = 1;
            r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x05c6 }
            r7 = r1.mFormat;	 Catch:{ all -> 0x05c6 }
            r8 = r1.mCurWidth;	 Catch:{ all -> 0x05c6 }
            r9 = r1.mCurHeight;	 Catch:{ all -> 0x05c6 }
            r1.onSurfaceChanged(r0, r7, r8, r9);	 Catch:{ all -> 0x05c6 }
            r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x05c6 }
            r0 = r0.getCallbacks();	 Catch:{ all -> 0x05c6 }
            if (r0 == 0) goto L_0x04f3;
        L_0x04df:
            r7 = r0.length;	 Catch:{ all -> 0x05c6 }
            r8 = 0;
        L_0x04e1:
            if (r8 >= r7) goto L_0x04f3;
        L_0x04e3:
            r9 = r0[r8];	 Catch:{ all -> 0x05c6 }
            r10 = r1.mSurfaceHolder;	 Catch:{ all -> 0x05c6 }
            r13 = r1.mFormat;	 Catch:{ all -> 0x05c6 }
            r14 = r1.mCurWidth;	 Catch:{ all -> 0x05c6 }
            r15 = r1.mCurHeight;	 Catch:{ all -> 0x05c6 }
            r9.surfaceChanged(r10, r13, r14, r15);	 Catch:{ all -> 0x05c6 }
            r8 = r8 + 1;
            goto L_0x04e1;
        L_0x04f3:
            if (r12 == 0) goto L_0x0579;
        L_0x04f5:
            r0 = r1.mDispatchedOverscanInsets;	 Catch:{ all -> 0x05c6 }
            r7 = r1.mOverscanInsets;	 Catch:{ all -> 0x05c6 }
            r0.set(r7);	 Catch:{ all -> 0x05c6 }
            r0 = r1.mDispatchedOverscanInsets;	 Catch:{ all -> 0x05c6 }
            r7 = r0.left;	 Catch:{ all -> 0x05c6 }
            r8 = r1.mOutsets;	 Catch:{ all -> 0x05c6 }
            r8 = r8.left;	 Catch:{ all -> 0x05c6 }
            r7 = r7 + r8;
            r0.left = r7;	 Catch:{ all -> 0x05c6 }
            r0 = r1.mDispatchedOverscanInsets;	 Catch:{ all -> 0x05c6 }
            r7 = r0.top;	 Catch:{ all -> 0x05c6 }
            r8 = r1.mOutsets;	 Catch:{ all -> 0x05c6 }
            r8 = r8.top;	 Catch:{ all -> 0x05c6 }
            r7 = r7 + r8;
            r0.top = r7;	 Catch:{ all -> 0x05c6 }
            r0 = r1.mDispatchedOverscanInsets;	 Catch:{ all -> 0x05c6 }
            r7 = r0.right;	 Catch:{ all -> 0x05c6 }
            r8 = r1.mOutsets;	 Catch:{ all -> 0x05c6 }
            r8 = r8.right;	 Catch:{ all -> 0x05c6 }
            r7 = r7 + r8;
            r0.right = r7;	 Catch:{ all -> 0x05c6 }
            r0 = r1.mDispatchedOverscanInsets;	 Catch:{ all -> 0x05c6 }
            r7 = r0.bottom;	 Catch:{ all -> 0x05c6 }
            r8 = r1.mOutsets;	 Catch:{ all -> 0x05c6 }
            r8 = r8.bottom;	 Catch:{ all -> 0x05c6 }
            r7 = r7 + r8;
            r0.bottom = r7;	 Catch:{ all -> 0x05c6 }
            r0 = r1.mDispatchedContentInsets;	 Catch:{ all -> 0x05c6 }
            r7 = r1.mContentInsets;	 Catch:{ all -> 0x05c6 }
            r0.set(r7);	 Catch:{ all -> 0x05c6 }
            r0 = r1.mDispatchedStableInsets;	 Catch:{ all -> 0x05c6 }
            r7 = r1.mStableInsets;	 Catch:{ all -> 0x05c6 }
            r0.set(r7);	 Catch:{ all -> 0x05c6 }
            r0 = r1.mDispatchedOutsets;	 Catch:{ all -> 0x05c6 }
            r7 = r1.mOutsets;	 Catch:{ all -> 0x05c6 }
            r0.set(r7);	 Catch:{ all -> 0x05c6 }
            r0 = r1.mDisplayCutout;	 Catch:{ all -> 0x05c6 }
            r0 = r0.get();	 Catch:{ all -> 0x05c6 }
            r1.mDispatchedDisplayCutout = r0;	 Catch:{ all -> 0x05c6 }
            r0 = r1.mFinalSystemInsets;	 Catch:{ all -> 0x05c6 }
            r7 = r1.mDispatchedOverscanInsets;	 Catch:{ all -> 0x05c6 }
            r0.set(r7);	 Catch:{ all -> 0x05c6 }
            r0 = r1.mFinalStableInsets;	 Catch:{ all -> 0x05c6 }
            r7 = r1.mDispatchedStableInsets;	 Catch:{ all -> 0x05c6 }
            r0.set(r7);	 Catch:{ all -> 0x05c6 }
            r0 = new android.view.WindowInsets;	 Catch:{ all -> 0x05c6 }
            r7 = r1.mFinalSystemInsets;	 Catch:{ all -> 0x05c6 }
            r8 = r1.mFinalStableInsets;	 Catch:{ all -> 0x05c6 }
            r9 = android.service.wallpaper.WallpaperService.this;	 Catch:{ all -> 0x05c6 }
            r9 = r9.getResources();	 Catch:{ all -> 0x05c6 }
            r9 = r9.getConfiguration();	 Catch:{ all -> 0x05c6 }
            r22 = r9.isScreenRound();	 Catch:{ all -> 0x05c6 }
            r23 = 0;
            r9 = r1.mDispatchedDisplayCutout;	 Catch:{ all -> 0x05c6 }
            r19 = r0;
            r20 = r7;
            r21 = r8;
            r24 = r9;
            r19.<init>(r20, r21, r22, r23, r24);	 Catch:{ all -> 0x05c6 }
            r1.onApplyWindowInsets(r0);	 Catch:{ all -> 0x05c6 }
        L_0x0579:
            if (r6 == 0) goto L_0x059d;
        L_0x057b:
            r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x05c6 }
            r1.onSurfaceRedrawNeeded(r0);	 Catch:{ all -> 0x05c6 }
            r0 = r1.mSurfaceHolder;	 Catch:{ all -> 0x05c6 }
            r0 = r0.getCallbacks();	 Catch:{ all -> 0x05c6 }
            if (r0 == 0) goto L_0x059d;
        L_0x0588:
            r7 = r0.length;	 Catch:{ all -> 0x05c6 }
            r8 = 0;
        L_0x058a:
            if (r8 >= r7) goto L_0x059d;
        L_0x058c:
            r9 = r0[r8];	 Catch:{ all -> 0x05c6 }
            r10 = r9 instanceof android.view.SurfaceHolder.Callback2;	 Catch:{ all -> 0x05c6 }
            if (r10 == 0) goto L_0x059a;
        L_0x0592:
            r10 = r9;
            r10 = (android.view.SurfaceHolder.Callback2) r10;	 Catch:{ all -> 0x05c6 }
            r13 = r1.mSurfaceHolder;	 Catch:{ all -> 0x05c6 }
            r10.surfaceRedrawNeeded(r13);	 Catch:{ all -> 0x05c6 }
        L_0x059a:
            r8 = r8 + 1;
            goto L_0x058a;
        L_0x059d:
            if (r5 == 0) goto L_0x05af;
        L_0x059f:
            r0 = r1.mReportedVisible;	 Catch:{ all -> 0x05c6 }
            if (r0 != 0) goto L_0x05af;
        L_0x05a3:
            r0 = r1.mIsCreating;	 Catch:{ all -> 0x05c6 }
            if (r0 == 0) goto L_0x05ab;
        L_0x05a7:
            r7 = 1;
            r1.onVisibilityChanged(r7);	 Catch:{ all -> 0x05c6 }
        L_0x05ab:
            r7 = 0;
            r1.onVisibilityChanged(r7);	 Catch:{ all -> 0x05c6 }
        L_0x05af:
            r7 = 0;
            r1.mIsCreating = r7;	 Catch:{ RemoteException -> 0x05e0 }
            r7 = 1;
            r1.mSurfaceCreated = r7;	 Catch:{ RemoteException -> 0x05e0 }
            if (r6 == 0) goto L_0x05be;
        L_0x05b7:
            r0 = r1.mSession;	 Catch:{ RemoteException -> 0x05e0 }
            r7 = r1.mWindow;	 Catch:{ RemoteException -> 0x05e0 }
            r0.finishDrawing(r7);	 Catch:{ RemoteException -> 0x05e0 }
        L_0x05be:
            r0 = r1.mIWallpaperEngine;	 Catch:{ RemoteException -> 0x05e0 }
            r0.reportShown();	 Catch:{ RemoteException -> 0x05e0 }
            goto L_0x062e;
        L_0x05c6:
            r0 = move-exception;
            goto L_0x05cb;
        L_0x05c8:
            r0 = move-exception;
            r6 = r61;
        L_0x05cb:
            r7 = 0;
            r1.mIsCreating = r7;	 Catch:{ RemoteException -> 0x05e0 }
            r7 = 1;
            r1.mSurfaceCreated = r7;	 Catch:{ RemoteException -> 0x05e0 }
            if (r6 == 0) goto L_0x05da;
        L_0x05d3:
            r7 = r1.mSession;	 Catch:{ RemoteException -> 0x05e0 }
            r8 = r1.mWindow;	 Catch:{ RemoteException -> 0x05e0 }
            r7.finishDrawing(r8);	 Catch:{ RemoteException -> 0x05e0 }
        L_0x05da:
            r7 = r1.mIWallpaperEngine;	 Catch:{ RemoteException -> 0x05e0 }
            r7.reportShown();	 Catch:{ RemoteException -> 0x05e0 }
            throw r0;	 Catch:{ RemoteException -> 0x05e0 }
        L_0x05e0:
            r0 = move-exception;
            goto L_0x062e;
        L_0x05e2:
            r0 = move-exception;
            r6 = r61;
            goto L_0x062e;
        L_0x05e6:
            r0 = move-exception;
            r6 = r61;
            r12 = r5;
            goto L_0x062e;
        L_0x05eb:
            r0 = move-exception;
            r6 = r61;
            r11 = r30;
            r12 = r36;
            goto L_0x062e;
        L_0x05f3:
            r0 = move-exception;
            r18 = r5;
            r6 = r61;
            r11 = r30;
            r12 = r36;
            goto L_0x062e;
        L_0x05fd:
            r0 = move-exception;
            r17 = r3;
            r18 = r5;
            r6 = r61;
            r11 = r30;
            r12 = r36;
            goto L_0x062e;
        L_0x0609:
            r0 = move-exception;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            r6 = r61;
            r11 = r30;
            r12 = r36;
            goto L_0x062e;
        L_0x0617:
            r0 = move-exception;
            r17 = r3;
            r16 = r4;
            r18 = r5;
            r34 = r7;
            r35 = r8;
            r33 = r9;
            r30 = r11;
            r36 = r12;
            r31 = r13;
            r32 = r14;
            r6 = r61;
        L_0x062e:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.service.wallpaper.WallpaperService$Engine.updateSurface(boolean, boolean, boolean):void");
        }

        /* Access modifiers changed, original: 0000 */
        public void attach(IWallpaperEngineWrapper wrapper) {
            if (!this.mDestroyed) {
                this.mIWallpaperEngine = wrapper;
                this.mCaller = wrapper.mCaller;
                this.mConnection = wrapper.mConnection;
                this.mWindowToken = wrapper.mWindowToken;
                this.mSurfaceHolder.setSizeFromLayout();
                this.mInitializing = true;
                this.mSession = WindowManagerGlobal.getWindowSession();
                this.mWindow.setSession(this.mSession);
                this.mLayout.packageName = WallpaperService.this.getPackageName();
                this.mIWallpaperEngine.mDisplayManager.registerDisplayListener(this.mDisplayListener, this.mCaller.getHandler());
                this.mDisplay = this.mIWallpaperEngine.mDisplay;
                this.mDisplayContext = WallpaperService.this.createDisplayContext(this.mDisplay);
                this.mDisplayState = this.mDisplay.getState();
                onCreate(this.mSurfaceHolder);
                this.mInitializing = false;
                this.mReportedVisible = false;
                updateSurface(false, false, false);
            }
        }

        public Context getDisplayContext() {
            return this.mDisplayContext;
        }

        @VisibleForTesting
        public void doAmbientModeChanged(boolean inAmbientMode, long animationDuration) {
            if (!this.mDestroyed) {
                this.mIsInAmbientMode = inAmbientMode;
                if (this.mCreated) {
                    onAmbientModeChanged(inAmbientMode, animationDuration);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void doDesiredSizeChanged(int desiredWidth, int desiredHeight) {
            if (!this.mDestroyed) {
                IWallpaperEngineWrapper iWallpaperEngineWrapper = this.mIWallpaperEngine;
                iWallpaperEngineWrapper.mReqWidth = desiredWidth;
                iWallpaperEngineWrapper.mReqHeight = desiredHeight;
                onDesiredSizeChanged(desiredWidth, desiredHeight);
                doOffsetsChanged(true);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void doDisplayPaddingChanged(Rect padding) {
            if (!this.mDestroyed && !this.mIWallpaperEngine.mDisplayPadding.equals(padding)) {
                this.mIWallpaperEngine.mDisplayPadding.set(padding);
                updateSurface(true, false, false);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void doVisibilityChanged(boolean visible) {
            if (!this.mDestroyed) {
                this.mVisible = visible;
                reportVisibility();
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void reportVisibility() {
            if (!this.mDestroyed) {
                Display display = this.mDisplay;
                this.mDisplayState = display == null ? 0 : display.getState();
                boolean z = true;
                if (!this.mVisible || this.mDisplayState == 1) {
                    z = false;
                }
                boolean visible = z;
                if (this.mReportedVisible != visible) {
                    this.mReportedVisible = visible;
                    if (visible) {
                        doOffsetsChanged(false);
                        updateSurface(false, false, false);
                    }
                    onVisibilityChanged(visible);
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void doOffsetsChanged(boolean always) {
            if (!this.mDestroyed) {
                if (always || this.mOffsetsChanged) {
                    float xOffset;
                    float yOffset;
                    float xOffsetStep;
                    float yOffsetStep;
                    boolean sync;
                    boolean z;
                    synchronized (this.mLock) {
                        xOffset = this.mPendingXOffset;
                        yOffset = this.mPendingYOffset;
                        xOffsetStep = this.mPendingXOffsetStep;
                        yOffsetStep = this.mPendingYOffsetStep;
                        sync = this.mPendingSync;
                        z = false;
                        this.mPendingSync = false;
                        this.mOffsetMessageEnqueued = false;
                    }
                    if (this.mSurfaceCreated) {
                        if (this.mReportedVisible) {
                            int availw = this.mIWallpaperEngine.mReqWidth - this.mCurWidth;
                            int xPixels = availw > 0 ? -((int) ((((float) availw) * xOffset) + 0.5f)) : 0;
                            int availh = this.mIWallpaperEngine.mReqHeight - this.mCurHeight;
                            if (availh > 0) {
                                z = -((int) ((((float) availh) * yOffset) + 0.5f));
                            }
                            onOffsetsChanged(xOffset, yOffset, xOffsetStep, yOffsetStep, xPixels, z);
                        } else {
                            this.mOffsetsChanged = true;
                        }
                    }
                    if (sync) {
                        try {
                            this.mSession.wallpaperOffsetsComplete(this.mWindow.asBinder());
                        } catch (RemoteException e) {
                        }
                    }
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void doCommand(WallpaperCommand cmd) {
            Bundle result;
            if (this.mDestroyed) {
                result = null;
            } else {
                result = onCommand(cmd.action, cmd.x, cmd.y, cmd.z, cmd.extras, cmd.sync);
            }
            if (cmd.sync) {
                try {
                    this.mSession.wallpaperCommandComplete(this.mWindow.asBinder(), result);
                } catch (RemoteException e) {
                }
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void reportSurfaceDestroyed() {
            if (this.mSurfaceCreated) {
                int i = 0;
                this.mSurfaceCreated = false;
                this.mSurfaceHolder.ungetCallbacks();
                Callback[] callbacks = this.mSurfaceHolder.getCallbacks();
                if (callbacks != null) {
                    int length = callbacks.length;
                    while (i < length) {
                        callbacks[i].surfaceDestroyed(this.mSurfaceHolder);
                        i++;
                    }
                }
                onSurfaceDestroyed(this.mSurfaceHolder);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void detach() {
            if (!this.mDestroyed) {
                this.mDestroyed = true;
                if (this.mIWallpaperEngine.mDisplayManager != null) {
                    this.mIWallpaperEngine.mDisplayManager.unregisterDisplayListener(this.mDisplayListener);
                }
                if (this.mVisible) {
                    this.mVisible = false;
                    onVisibilityChanged(false);
                }
                reportSurfaceDestroyed();
                onDestroy();
                if (this.mCreated) {
                    try {
                        if (this.mInputEventReceiver != null) {
                            this.mInputEventReceiver.dispose();
                            this.mInputEventReceiver = null;
                        }
                        this.mSession.remove(this.mWindow);
                    } catch (RemoteException e) {
                    }
                    this.mSurfaceHolder.mSurface.release();
                    this.mCreated = false;
                    InputChannel inputChannel = this.mInputChannel;
                    if (inputChannel != null) {
                        inputChannel.dispose();
                        this.mInputChannel = null;
                    }
                }
            }
        }
    }

    class IWallpaperEngineWrapper extends Stub implements HandlerCaller.Callback {
        private final HandlerCaller mCaller;
        final IWallpaperConnection mConnection;
        private final AtomicBoolean mDetached = new AtomicBoolean();
        final Display mDisplay;
        final int mDisplayId;
        final DisplayManager mDisplayManager;
        final Rect mDisplayPadding = new Rect();
        Engine mEngine;
        final boolean mIsPreview;
        int mReqHeight;
        int mReqWidth;
        boolean mShownReported;
        final IBinder mWindowToken;
        final int mWindowType;

        IWallpaperEngineWrapper(WallpaperService context, IWallpaperConnection conn, IBinder windowToken, int windowType, boolean isPreview, int reqWidth, int reqHeight, Rect padding, int displayId) {
            this.mCaller = new HandlerCaller(context, context.getMainLooper(), this, true);
            this.mConnection = conn;
            this.mWindowToken = windowToken;
            this.mWindowType = windowType;
            this.mIsPreview = isPreview;
            this.mReqWidth = reqWidth;
            this.mReqHeight = reqHeight;
            this.mDisplayPadding.set(padding);
            this.mDisplayId = displayId;
            this.mDisplayManager = (DisplayManager) WallpaperService.this.getSystemService(DisplayManager.class);
            this.mDisplay = this.mDisplayManager.getDisplay(this.mDisplayId);
            if (this.mDisplay != null) {
                this.mCaller.sendMessage(this.mCaller.obtainMessage(10));
                return;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Cannot find display with id");
            stringBuilder.append(this.mDisplayId);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public void setDesiredSize(int width, int height) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageII(30, width, height));
        }

        public void setDisplayPadding(Rect padding) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageO(40, padding));
        }

        public void setVisibility(boolean visible) {
            this.mCaller.sendMessage(this.mCaller.obtainMessageI(10010, visible));
        }

        public void setInAmbientMode(boolean inAmbientDisplay, long animationDuration) throws RemoteException {
            this.mCaller.sendMessage(this.mCaller.obtainMessageIO(50, inAmbientDisplay, Long.valueOf(animationDuration)));
        }

        public void dispatchPointer(MotionEvent event) {
            Engine engine = this.mEngine;
            if (engine != null) {
                engine.dispatchPointer(event);
            } else {
                event.recycle();
            }
        }

        public void dispatchWallpaperCommand(String action, int x, int y, int z, Bundle extras) {
            Engine engine = this.mEngine;
            if (engine != null) {
                engine.mWindow.dispatchWallpaperCommand(action, x, y, z, extras, false);
            }
        }

        public void reportShown() {
            if (!this.mShownReported) {
                this.mShownReported = true;
                try {
                    this.mConnection.engineShown(this);
                } catch (RemoteException e) {
                    Log.w(WallpaperService.TAG, "Wallpaper host disappeared", e);
                }
            }
        }

        public void requestWallpaperColors() {
            this.mCaller.sendMessage(this.mCaller.obtainMessage(10050));
        }

        public void destroy() {
            this.mCaller.sendMessage(this.mCaller.obtainMessage(20));
        }

        public void detach() {
            this.mDetached.set(true);
        }

        private void doDetachEngine() {
            WallpaperService.this.mActiveEngines.remove(this.mEngine);
            this.mEngine.detach();
        }

        public void executeMessage(Message message) {
            if (this.mDetached.get()) {
                if (WallpaperService.this.mActiveEngines.contains(this.mEngine)) {
                    doDetachEngine();
                }
                return;
            }
            boolean z = false;
            Engine engine;
            boolean reportDraw;
            switch (message.what) {
                case 10:
                    try {
                        this.mConnection.attachEngine(this, this.mDisplayId);
                        engine = WallpaperService.this.onCreateEngine();
                        this.mEngine = engine;
                        WallpaperService.this.mActiveEngines.add(engine);
                        engine.attach(this);
                        return;
                    } catch (RemoteException e) {
                        Log.w(WallpaperService.TAG, "Wallpaper host disappeared", e);
                        return;
                    }
                case 20:
                    doDetachEngine();
                    return;
                case 30:
                    this.mEngine.doDesiredSizeChanged(message.arg1, message.arg2);
                    return;
                case 40:
                    this.mEngine.doDisplayPaddingChanged((Rect) message.obj);
                    return;
                case 50:
                    engine = this.mEngine;
                    if (message.arg1 != 0) {
                        z = true;
                    }
                    engine.doAmbientModeChanged(z, ((Long) message.obj).longValue());
                    return;
                case 10000:
                    this.mEngine.updateSurface(true, false, true);
                    break;
                case 10010:
                    engine = this.mEngine;
                    if (message.arg1 != 0) {
                        z = true;
                    }
                    engine.doVisibilityChanged(z);
                    break;
                case 10020:
                    this.mEngine.doOffsetsChanged(true);
                    break;
                case 10025:
                    this.mEngine.doCommand(message.obj);
                    break;
                case 10030:
                    reportDraw = message.arg1 != 0;
                    this.mEngine.mOutsets.set((Rect) message.obj);
                    this.mEngine.updateSurface(true, false, reportDraw);
                    this.mEngine.doOffsetsChanged(true);
                    break;
                case 10035:
                    break;
                case 10040:
                    reportDraw = false;
                    MotionEvent ev = message.obj;
                    if (ev.getAction() == 2) {
                        synchronized (this.mEngine.mLock) {
                            if (this.mEngine.mPendingMove == ev) {
                                this.mEngine.mPendingMove = null;
                            } else {
                                reportDraw = true;
                            }
                        }
                    }
                    if (!reportDraw) {
                        this.mEngine.onTouchEvent(ev);
                    }
                    ev.recycle();
                    break;
                case 10050:
                    IWallpaperConnection iWallpaperConnection = this.mConnection;
                    if (iWallpaperConnection != null) {
                        try {
                            iWallpaperConnection.onWallpaperColorsChanged(this.mEngine.onComputeColors(), this.mDisplayId);
                            break;
                        } catch (RemoteException e2) {
                            break;
                        }
                    }
                    break;
                default:
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Unknown message type ");
                    stringBuilder.append(message.what);
                    Log.w(WallpaperService.TAG, stringBuilder.toString());
                    break;
            }
        }
    }

    class IWallpaperServiceWrapper extends IWallpaperService.Stub {
        private IWallpaperEngineWrapper mEngineWrapper;
        private final WallpaperService mTarget;

        public IWallpaperServiceWrapper(WallpaperService context) {
            this.mTarget = context;
        }

        public void attach(IWallpaperConnection conn, IBinder windowToken, int windowType, boolean isPreview, int reqWidth, int reqHeight, Rect padding, int displayId) {
            this.mEngineWrapper = new IWallpaperEngineWrapper(this.mTarget, conn, windowToken, windowType, isPreview, reqWidth, reqHeight, padding, displayId);
        }

        public void detach() {
            this.mEngineWrapper.detach();
        }
    }

    static final class WallpaperCommand {
        String action;
        Bundle extras;
        boolean sync;
        int x;
        int y;
        int z;

        WallpaperCommand() {
        }
    }

    public abstract Engine onCreateEngine();

    public void onCreate() {
        super.onCreate();
    }

    public void onDestroy() {
        super.onDestroy();
        for (int i = 0; i < this.mActiveEngines.size(); i++) {
            ((Engine) this.mActiveEngines.get(i)).detach();
        }
        this.mActiveEngines.clear();
    }

    public final IBinder onBind(Intent intent) {
        return new IWallpaperServiceWrapper(this);
    }

    /* Access modifiers changed, original: protected */
    public void dump(FileDescriptor fd, PrintWriter out, String[] args) {
        out.print("State of wallpaper ");
        out.print(this);
        String str = ":";
        out.println(str);
        for (int i = 0; i < this.mActiveEngines.size(); i++) {
            Engine engine = (Engine) this.mActiveEngines.get(i);
            out.print("  Engine ");
            out.print(engine);
            out.println(str);
            engine.dump("    ", fd, out, args);
        }
    }
}
