package android.view;

import android.annotation.UnsupportedAppUsage;
import android.graphics.FrameInfo;
import android.graphics.HardwareRenderer;
import android.hardware.display.DisplayManagerGlobal;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.Trace;
import android.util.Log;
import android.util.TimeUtils;
import java.io.PrintWriter;

public final class Choreographer {
    public static final int CALLBACK_ANIMATION = 1;
    public static final int CALLBACK_COMMIT = 4;
    public static final int CALLBACK_INPUT = 0;
    public static final int CALLBACK_INSETS_ANIMATION = 2;
    private static final int CALLBACK_LAST = 4;
    private static final String[] CALLBACK_TRACE_TITLES = new String[]{"input", AnimationProperty.PROPERTY_NAME, "insets_animation", "traversal", "commit"};
    public static final int CALLBACK_TRAVERSAL = 3;
    private static final boolean DEBUG_FRAMES = false;
    private static final boolean DEBUG_JANK = false;
    private static final long DEFAULT_FRAME_DELAY = 10;
    private static final Object FRAME_CALLBACK_TOKEN = new Object() {
        public String toString() {
            return "FRAME_CALLBACK_TOKEN";
        }
    };
    private static final int MOTION_EVENT_ACTION_CANCEL = 3;
    private static final int MOTION_EVENT_ACTION_DOWN = 0;
    private static final int MOTION_EVENT_ACTION_MOVE = 2;
    private static final int MOTION_EVENT_ACTION_UP = 1;
    private static final int MSG_DO_FRAME = 0;
    private static final int MSG_DO_SCHEDULE_CALLBACK = 2;
    private static final int MSG_DO_SCHEDULE_VSYNC = 1;
    private static final boolean OPTS_INPUT = true;
    private static final int SKIPPED_FRAME_WARNING_LIMIT = SystemProperties.getInt("debug.choreographer.skipwarning", 30);
    private static final String TAG = "Choreographer";
    private static final boolean USE_FRAME_TIME = SystemProperties.getBoolean("debug.choreographer.frametime", true);
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 123769497)
    private static final boolean USE_VSYNC = SystemProperties.getBoolean("debug.choreographer.vsync", true);
    private static volatile Choreographer mMainInstance;
    private static volatile long sFrameDelay = DEFAULT_FRAME_DELAY;
    private static boolean sIsNextFrameAtFront = false;
    private static final ThreadLocal<Choreographer> sSfThreadInstance = new ThreadLocal<Choreographer>() {
        /* Access modifiers changed, original: protected */
        public Choreographer initialValue() {
            Looper looper = Looper.myLooper();
            if (looper != null) {
                return new Choreographer(looper, 1, null);
            }
            throw new IllegalStateException("The current thread must have a looper!");
        }
    };
    private static final ThreadLocal<Choreographer> sThreadInstance = new ThreadLocal<Choreographer>() {
        /* Access modifiers changed, original: protected */
        public Choreographer initialValue() {
            Looper looper = Looper.myLooper();
            if (looper != null) {
                Choreographer choreographer = new Choreographer(looper, 0, null);
                if (looper == Looper.getMainLooper()) {
                    Choreographer.mMainInstance = choreographer;
                }
                return choreographer;
            }
            throw new IllegalStateException("The current thread must have a looper!");
        }
    };
    private CallbackRecord mCallbackPool;
    @UnsupportedAppUsage
    private final CallbackQueue[] mCallbackQueues;
    private boolean mCallbacksRunning;
    private boolean mConsumedDown;
    private boolean mConsumedMove;
    private boolean mDebugPrintNextFrameTimeDelta;
    @UnsupportedAppUsage
    private final FrameDisplayEventReceiver mDisplayEventReceiver;
    private int mFPSDivisor;
    FrameInfo mFrameInfo;
    @UnsupportedAppUsage
    private long mFrameIntervalNanos;
    private boolean mFrameScheduled;
    private final FrameHandler mHandler;
    private boolean mIsVsyncScheduled;
    @UnsupportedAppUsage
    private long mLastFrameTimeNanos;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private final Object mLock;
    private final Looper mLooper;
    private int mMotionEventType;
    private int mTouchMoveNum;

    private final class CallbackQueue {
        private CallbackRecord mHead;

        private CallbackQueue() {
        }

        /* synthetic */ CallbackQueue(Choreographer x0, AnonymousClass1 x1) {
            this();
        }

        public boolean hasDueCallbacksLocked(long now) {
            CallbackRecord callbackRecord = this.mHead;
            return callbackRecord != null && callbackRecord.dueTime <= now;
        }

        public CallbackRecord extractDueCallbacksLocked(long now) {
            CallbackRecord callbacks = this.mHead;
            if (callbacks == null || callbacks.dueTime > now) {
                return null;
            }
            CallbackRecord last = callbacks;
            CallbackRecord next = last.next;
            while (next != null) {
                if (next.dueTime > now) {
                    last.next = null;
                    break;
                }
                last = next;
                next = next.next;
            }
            this.mHead = next;
            return callbacks;
        }

        @UnsupportedAppUsage
        public void addCallbackLocked(long dueTime, Object action, Object token) {
            CallbackRecord callback = Choreographer.this.obtainCallbackLocked(dueTime, action, token);
            CallbackRecord entry = this.mHead;
            if (entry == null) {
                this.mHead = callback;
            } else if (dueTime < entry.dueTime) {
                callback.next = entry;
                this.mHead = callback;
            } else {
                while (entry.next != null) {
                    if (dueTime < entry.next.dueTime) {
                        callback.next = entry.next;
                        break;
                    }
                    entry = entry.next;
                }
                entry.next = callback;
            }
        }

        public void removeCallbacksLocked(Object action, Object token) {
            CallbackRecord predecessor = null;
            CallbackRecord callback = this.mHead;
            while (callback != null) {
                CallbackRecord next = callback.next;
                if ((action == null || callback.action == action) && (token == null || callback.token == token)) {
                    if (predecessor != null) {
                        predecessor.next = next;
                    } else {
                        this.mHead = next;
                    }
                    Choreographer.this.recycleCallbackLocked(callback);
                } else {
                    predecessor = callback;
                }
                callback = next;
            }
        }
    }

    private static final class CallbackRecord {
        public Object action;
        public long dueTime;
        public CallbackRecord next;
        public Object token;

        private CallbackRecord() {
        }

        /* synthetic */ CallbackRecord(AnonymousClass1 x0) {
            this();
        }

        @UnsupportedAppUsage
        public void run(long frameTimeNanos) {
            if (this.token == Choreographer.FRAME_CALLBACK_TOKEN) {
                ((FrameCallback) this.action).doFrame(frameTimeNanos);
            } else {
                ((Runnable) this.action).run();
            }
        }
    }

    public interface FrameCallback {
        void doFrame(long j);
    }

    private final class FrameDisplayEventReceiver extends DisplayEventReceiver implements Runnable {
        private int mFrame;
        private boolean mHavePendingVsync;
        private long mTimestampNanos;

        public FrameDisplayEventReceiver(Looper looper, int vsyncSource) {
            super(looper, vsyncSource);
        }

        public void onVsync(long timestampNanos, long physicalDisplayId, int frame) {
            long now = System.nanoTime();
            int i = (timestampNanos > now ? 1 : (timestampNanos == now ? 0 : -1));
            String str = Choreographer.TAG;
            if (i > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Frame time is ");
                stringBuilder.append(((float) (timestampNanos - now)) * 1.0E-6f);
                stringBuilder.append(" ms in the future!  Check that graphics HAL is generating vsync timestamps using the correct timebase.");
                Log.w(str, stringBuilder.toString());
                timestampNanos = now;
            }
            if (this.mHavePendingVsync) {
                Log.w(str, "Already have a pending vsync event.  There should only be one at a time.");
            } else {
                this.mHavePendingVsync = true;
            }
            this.mTimestampNanos = timestampNanos;
            this.mFrame = frame;
            Message msg = Message.obtain(Choreographer.this.mHandler, (Runnable) this);
            msg.setAsynchronous(true);
            if (Choreographer.sIsNextFrameAtFront) {
                Choreographer.setNextFrameAtFront(false);
                Choreographer.this.mHandler.sendMessageAtFrontOfQueue(msg);
                return;
            }
            Choreographer.this.mHandler.sendMessageAtTime(msg, timestampNanos / TimeUtils.NANOS_PER_MS);
        }

        public void run() {
            this.mHavePendingVsync = false;
            Choreographer.this.doFrame(this.mTimestampNanos, this.mFrame);
        }
    }

    private final class FrameHandler extends Handler {
        public FrameHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 0) {
                Choreographer.this.doFrame(System.nanoTime(), 0);
            } else if (i == 1) {
                Choreographer.this.doScheduleVsync();
            } else if (i == 2) {
                Choreographer.this.doScheduleCallback(msg.arg1);
            }
        }
    }

    /* synthetic */ Choreographer(Looper x0, int x1, AnonymousClass1 x2) {
        this(x0, x1);
    }

    private Choreographer(Looper looper, int vsyncSource) {
        FrameDisplayEventReceiver frameDisplayEventReceiver;
        this.mLock = new Object();
        this.mFPSDivisor = 1;
        this.mTouchMoveNum = -1;
        this.mMotionEventType = -1;
        this.mConsumedMove = false;
        this.mConsumedDown = false;
        this.mIsVsyncScheduled = false;
        this.mFrameInfo = new FrameInfo();
        this.mLooper = looper;
        this.mHandler = new FrameHandler(looper);
        if (USE_VSYNC) {
            frameDisplayEventReceiver = new FrameDisplayEventReceiver(looper, vsyncSource);
        } else {
            frameDisplayEventReceiver = null;
        }
        this.mDisplayEventReceiver = frameDisplayEventReceiver;
        this.mLastFrameTimeNanos = Long.MIN_VALUE;
        this.mFrameIntervalNanos = (long) (1.0E9f / getRefreshRate());
        this.mCallbackQueues = new CallbackQueue[5];
        for (int i = 0; i <= 4; i++) {
            this.mCallbackQueues[i] = new CallbackQueue(this, null);
        }
        setFPSDivisor(SystemProperties.getInt(ThreadedRenderer.DEBUG_FPS_DIVISOR, 1));
    }

    private static float getRefreshRate() {
        return DisplayManagerGlobal.getInstance().getDisplayInfo(0).getMode().getRefreshRate();
    }

    public static Choreographer getInstance() {
        return (Choreographer) sThreadInstance.get();
    }

    @UnsupportedAppUsage
    public static Choreographer getSfInstance() {
        return (Choreographer) sSfThreadInstance.get();
    }

    public void setMotionEventInfo(int motionEventType, int touchMoveNum) {
        synchronized (this) {
            this.mTouchMoveNum = touchMoveNum;
            this.mMotionEventType = motionEventType;
        }
    }

    public static Choreographer getMainThreadInstance() {
        return mMainInstance;
    }

    public static void releaseInstance() {
        Choreographer old = (Choreographer) sThreadInstance.get();
        sThreadInstance.remove();
        old.dispose();
    }

    private void dispose() {
        this.mDisplayEventReceiver.dispose();
    }

    public static long getFrameDelay() {
        return sFrameDelay;
    }

    public static void setFrameDelay(long frameDelay) {
        sFrameDelay = frameDelay;
    }

    public static long subtractFrameDelay(long delayMillis) {
        long frameDelay = sFrameDelay;
        return delayMillis <= frameDelay ? 0 : delayMillis - frameDelay;
    }

    public long getFrameIntervalNanos() {
        return this.mFrameIntervalNanos;
    }

    /* Access modifiers changed, original: 0000 */
    public void dump(String prefix, PrintWriter writer) {
        String innerPrefix = new StringBuilder();
        innerPrefix.append(prefix);
        innerPrefix.append("  ");
        innerPrefix = innerPrefix.toString();
        writer.print(prefix);
        writer.println("Choreographer:");
        writer.print(innerPrefix);
        writer.print("mFrameScheduled=");
        writer.println(this.mFrameScheduled);
        writer.print(innerPrefix);
        writer.print("mLastFrameTime=");
        writer.println(TimeUtils.formatUptime(this.mLastFrameTimeNanos / TimeUtils.NANOS_PER_MS));
    }

    public void postCallback(int callbackType, Runnable action, Object token) {
        postCallbackDelayed(callbackType, action, token, 0);
    }

    public void postCallbackDelayed(int callbackType, Runnable action, Object token, long delayMillis) {
        if (action == null) {
            throw new IllegalArgumentException("action must not be null");
        } else if (callbackType < 0 || callbackType > 4) {
            throw new IllegalArgumentException("callbackType is invalid");
        } else {
            postCallbackDelayedInternal(callbackType, action, token, delayMillis);
        }
    }

    private void postCallbackDelayedInternal(int callbackType, Object action, Object token, long delayMillis) {
        synchronized (this.mLock) {
            long now = SystemClock.uptimeMillis();
            long dueTime = now + delayMillis;
            this.mCallbackQueues[callbackType].addCallbackLocked(dueTime, action, token);
            if (dueTime > now) {
                if (!sIsNextFrameAtFront) {
                    Message msg = this.mHandler.obtainMessage(2, action);
                    msg.arg1 = callbackType;
                    msg.setAsynchronous(true);
                    this.mHandler.sendMessageAtTime(msg, dueTime);
                }
            }
            scheduleFrameLocked(now);
        }
    }

    public void removeCallbacks(int callbackType, Runnable action, Object token) {
        if (callbackType < 0 || callbackType > 4) {
            throw new IllegalArgumentException("callbackType is invalid");
        }
        removeCallbacksInternal(callbackType, action, token);
    }

    private void removeCallbacksInternal(int callbackType, Object action, Object token) {
        synchronized (this.mLock) {
            this.mCallbackQueues[callbackType].removeCallbacksLocked(action, token);
            if (action != null && token == null) {
                this.mHandler.removeMessages(2, action);
            }
        }
    }

    public void postFrameCallback(FrameCallback callback) {
        postFrameCallbackDelayed(callback, 0);
    }

    public void postFrameCallbackDelayed(FrameCallback callback, long delayMillis) {
        if (callback != null) {
            postCallbackDelayedInternal(1, callback, FRAME_CALLBACK_TOKEN, delayMillis);
            return;
        }
        throw new IllegalArgumentException("callback must not be null");
    }

    public void removeFrameCallback(FrameCallback callback) {
        if (callback != null) {
            removeCallbacksInternal(1, callback, FRAME_CALLBACK_TOKEN);
            return;
        }
        throw new IllegalArgumentException("callback must not be null");
    }

    @UnsupportedAppUsage
    public long getFrameTime() {
        return getFrameTimeNanos() / TimeUtils.NANOS_PER_MS;
    }

    @UnsupportedAppUsage
    public long getFrameTimeNanos() {
        long nanoTime;
        synchronized (this.mLock) {
            if (this.mCallbacksRunning) {
                nanoTime = USE_FRAME_TIME ? this.mLastFrameTimeNanos : System.nanoTime();
            } else {
                throw new IllegalStateException("This method must only be called as part of a callback while a frame is in progress.");
            }
        }
        return nanoTime;
    }

    public long getLastFrameTimeNanos() {
        long nanoTime;
        synchronized (this.mLock) {
            nanoTime = USE_FRAME_TIME ? this.mLastFrameTimeNanos : System.nanoTime();
        }
        return nanoTime;
    }

    private void scheduleFrameLocked(long now) {
        if (!this.mFrameScheduled) {
            Message msg;
            this.mFrameScheduled = true;
            if (!this.mIsVsyncScheduled && System.nanoTime() - this.mLastFrameTimeNanos > this.mFrameIntervalNanos) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("scheduleFrameLocked-mMotionEventType:");
                stringBuilder.append(this.mMotionEventType);
                stringBuilder.append(" mTouchMoveNum:");
                stringBuilder.append(this.mTouchMoveNum);
                stringBuilder.append(" mConsumedDown:");
                stringBuilder.append(this.mConsumedDown);
                stringBuilder.append(" mConsumedMove:");
                stringBuilder.append(this.mConsumedMove);
                Trace.traceBegin(8, stringBuilder.toString());
                Trace.traceEnd(8);
                synchronized (this) {
                    int i = this.mMotionEventType;
                    if (i != 0) {
                        if (i != 1) {
                            if (i == 2) {
                                this.mConsumedDown = false;
                                if (this.mTouchMoveNum == 1 && !this.mConsumedMove) {
                                    msg = this.mHandler.obtainMessage(0);
                                    msg.setAsynchronous(true);
                                    this.mHandler.sendMessageAtFrontOfQueue(msg);
                                    this.mConsumedMove = true;
                                    return;
                                }
                            } else if (i != 3) {
                            }
                        }
                        this.mConsumedMove = false;
                        this.mConsumedDown = false;
                    } else {
                        this.mConsumedMove = false;
                        if (!this.mConsumedDown) {
                            msg = this.mHandler.obtainMessage(0);
                            msg.setAsynchronous(true);
                            this.mHandler.sendMessageAtFrontOfQueue(msg);
                            this.mConsumedDown = true;
                            return;
                        }
                    }
                }
            }
            if (!USE_VSYNC || sIsNextFrameAtFront) {
                long nextFrameTime = Math.max((this.mLastFrameTimeNanos / TimeUtils.NANOS_PER_MS) + sFrameDelay, now);
                msg = this.mHandler.obtainMessage(0);
                msg.setAsynchronous(true);
                if (sIsNextFrameAtFront) {
                    setNextFrameAtFront(false);
                    this.mHandler.sendMessageAtFrontOfQueue(msg);
                } else {
                    this.mHandler.sendMessageAtTime(msg, nextFrameTime);
                }
            } else if (isRunningOnLooperThreadLocked()) {
                scheduleVsyncLocked();
            } else {
                msg = this.mHandler.obtainMessage(1);
                msg.setAsynchronous(true);
                this.mHandler.sendMessageAtFrontOfQueue(msg);
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void setFPSDivisor(int divisor) {
        if (divisor <= 0) {
            divisor = 1;
        }
        this.mFPSDivisor = divisor;
        HardwareRenderer.setFPSDivisor(divisor);
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:41:?, code skipped:
            android.os.Trace.traceBegin(8, "Choreographer#doFrame");
            android.view.animation.AnimationUtils.lockAnimationClock(r13 / android.util.TimeUtils.NANOS_PER_MS);
            r1.mFrameInfo.markInputHandlingStart();
            doCallbacks(0, r13);
            r1.mFrameInfo.markAnimationsStart();
            doCallbacks(1, r13);
            doCallbacks(2, r13);
            r1.mFrameInfo.markPerformTraversalsStart();
            doCallbacks(3, r13);
            doCallbacks(4, r13);
     */
    /* JADX WARNING: Missing block: B:44:0x00c6, code skipped:
            android.view.animation.AnimationUtils.unlockAnimationClock();
            android.os.Trace.traceEnd(8);
     */
    @android.annotation.UnsupportedAppUsage
    public void doFrame(long r18, int r20) {
        /*
        r17 = this;
        r1 = r17;
        r2 = r1.mLock;
        monitor-enter(r2);
        r0 = 0;
        r1.mIsVsyncScheduled = r0;	 Catch:{ all -> 0x00cd }
        r3 = r1.mFrameScheduled;	 Catch:{ all -> 0x00cd }
        if (r3 != 0) goto L_0x000e;
    L_0x000c:
        monitor-exit(r2);	 Catch:{ all -> 0x00cd }
        return;
    L_0x000e:
        r3 = r18;
        r5 = java.lang.System.nanoTime();	 Catch:{ all -> 0x00cd }
        r7 = r5 - r18;
        r9 = r1.mFrameIntervalNanos;	 Catch:{ all -> 0x00cd }
        r9 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1));
        if (r9 < 0) goto L_0x004e;
    L_0x001c:
        r9 = r1.mFrameIntervalNanos;	 Catch:{ all -> 0x00cd }
        r9 = r7 / r9;
        r11 = SKIPPED_FRAME_WARNING_LIMIT;	 Catch:{ all -> 0x00cd }
        r11 = (long) r11;	 Catch:{ all -> 0x00cd }
        r11 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1));
        if (r11 < 0) goto L_0x0047;
    L_0x0027:
        r11 = "Choreographer";
        r12 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00cd }
        r12.<init>();	 Catch:{ all -> 0x00cd }
        r13 = "Skipped ";
        r12.append(r13);	 Catch:{ all -> 0x00cd }
        r12.append(r9);	 Catch:{ all -> 0x00cd }
        r13 = " frames!  The application may be doing too much work on its main thread.";
        r12.append(r13);	 Catch:{ all -> 0x00cd }
        r12 = r12.toString();	 Catch:{ all -> 0x00cd }
        android.util.Log.i(r11, r12);	 Catch:{ all -> 0x00cd }
        r11 = 30089; // 0x7589 float:4.2164E-41 double:1.4866E-319;
        android.util.EventLog.writeEvent(r11, r9);	 Catch:{ all -> 0x00cd }
    L_0x0047:
        r11 = r1.mFrameIntervalNanos;	 Catch:{ all -> 0x00cd }
        r11 = r7 % r11;
        r13 = r5 - r11;
        goto L_0x0050;
    L_0x004e:
        r13 = r18;
    L_0x0050:
        r9 = r1.mLastFrameTimeNanos;	 Catch:{ all -> 0x00d2 }
        r9 = (r13 > r9 ? 1 : (r13 == r9 ? 0 : -1));
        if (r9 >= 0) goto L_0x005b;
    L_0x0056:
        r17.scheduleVsyncLocked();	 Catch:{ all -> 0x00d2 }
        monitor-exit(r2);	 Catch:{ all -> 0x00d2 }
        return;
    L_0x005b:
        r9 = r1.mFPSDivisor;	 Catch:{ all -> 0x00d2 }
        r10 = 1;
        if (r9 <= r10) goto L_0x007e;
    L_0x0060:
        r11 = r1.mLastFrameTimeNanos;	 Catch:{ all -> 0x007a }
        r11 = r13 - r11;
        r15 = r11;
        r10 = r1.mFrameIntervalNanos;	 Catch:{ all -> 0x007a }
        r9 = r1.mFPSDivisor;	 Catch:{ all -> 0x007a }
        r0 = (long) r9;	 Catch:{ all -> 0x007a }
        r10 = r10 * r0;
        r0 = (r15 > r10 ? 1 : (r15 == r10 ? 0 : -1));
        if (r0 >= 0) goto L_0x007e;
    L_0x006f:
        r0 = 0;
        r0 = (r15 > r0 ? 1 : (r15 == r0 ? 0 : -1));
        if (r0 <= 0) goto L_0x007e;
    L_0x0075:
        r17.scheduleVsyncLocked();	 Catch:{ all -> 0x007a }
        monitor-exit(r2);	 Catch:{ all -> 0x007a }
        return;
    L_0x007a:
        r0 = move-exception;
        r1 = r17;
        goto L_0x00d0;
    L_0x007e:
        r1 = r17;
        r0 = r1.mFrameInfo;	 Catch:{ all -> 0x00d2 }
        r0.setVsync(r3, r13);	 Catch:{ all -> 0x00d2 }
        r0 = 0;
        r1.mFrameScheduled = r0;	 Catch:{ all -> 0x00d2 }
        r1.mLastFrameTimeNanos = r13;	 Catch:{ all -> 0x00d2 }
        monitor-exit(r2);	 Catch:{ all -> 0x00d2 }
        r2 = 8;
        r0 = "Choreographer#doFrame";
        android.os.Trace.traceBegin(r2, r0);	 Catch:{ all -> 0x00c5 }
        r7 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r7 = r13 / r7;
        android.view.animation.AnimationUtils.lockAnimationClock(r7);	 Catch:{ all -> 0x00c5 }
        r0 = r1.mFrameInfo;	 Catch:{ all -> 0x00c5 }
        r0.markInputHandlingStart();	 Catch:{ all -> 0x00c5 }
        r0 = 0;
        r1.doCallbacks(r0, r13);	 Catch:{ all -> 0x00c5 }
        r0 = r1.mFrameInfo;	 Catch:{ all -> 0x00c5 }
        r0.markAnimationsStart();	 Catch:{ all -> 0x00c5 }
        r0 = 1;
        r1.doCallbacks(r0, r13);	 Catch:{ all -> 0x00c5 }
        r0 = 2;
        r1.doCallbacks(r0, r13);	 Catch:{ all -> 0x00c5 }
        r0 = r1.mFrameInfo;	 Catch:{ all -> 0x00c5 }
        r0.markPerformTraversalsStart();	 Catch:{ all -> 0x00c5 }
        r0 = 3;
        r1.doCallbacks(r0, r13);	 Catch:{ all -> 0x00c5 }
        r0 = 4;
        r1.doCallbacks(r0, r13);	 Catch:{ all -> 0x00c5 }
        android.view.animation.AnimationUtils.unlockAnimationClock();
        android.os.Trace.traceEnd(r2);
        return;
    L_0x00c5:
        r0 = move-exception;
        android.view.animation.AnimationUtils.unlockAnimationClock();
        android.os.Trace.traceEnd(r2);
        throw r0;
    L_0x00cd:
        r0 = move-exception;
        r13 = r18;
    L_0x00d0:
        monitor-exit(r2);	 Catch:{ all -> 0x00d2 }
        throw r0;
    L_0x00d2:
        r0 = move-exception;
        goto L_0x00d0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.Choreographer.doFrame(long, int):void");
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:19:?, code skipped:
            android.os.Trace.traceBegin(8, CALLBACK_TRACE_TITLES[r2]);
            r0 = r6;
     */
    /* JADX WARNING: Missing block: B:20:0x004d, code skipped:
            if (r0 == null) goto L_0x0056;
     */
    /* JADX WARNING: Missing block: B:21:0x004f, code skipped:
            r0.run(r13);
     */
    /* JADX WARNING: Missing block: B:22:0x0054, code skipped:
            r0 = r0.next;
     */
    /* JADX WARNING: Missing block: B:23:0x0056, code skipped:
            r4 = r1.mLock;
     */
    /* JADX WARNING: Missing block: B:24:0x0058, code skipped:
            monitor-enter(r4);
     */
    /* JADX WARNING: Missing block: B:26:?, code skipped:
            r1.mCallbacksRunning = false;
     */
    /* JADX WARNING: Missing block: B:27:0x005b, code skipped:
            r0 = r6.next;
            recycleCallbackLocked(r6);
            r6 = r0;
     */
    /* JADX WARNING: Missing block: B:28:0x0061, code skipped:
            if (r6 != null) goto L_0x005b;
     */
    /* JADX WARNING: Missing block: B:29:0x0063, code skipped:
            monitor-exit(r4);
     */
    /* JADX WARNING: Missing block: B:30:0x0064, code skipped:
            android.os.Trace.traceEnd(8);
     */
    /* JADX WARNING: Missing block: B:31:0x0068, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:38:0x006f, code skipped:
            monitor-enter(r1.mLock);
     */
    /* JADX WARNING: Missing block: B:40:?, code skipped:
            r1.mCallbacksRunning = false;
     */
    /* JADX WARNING: Missing block: B:41:0x0072, code skipped:
            r3 = r6.next;
            recycleCallbackLocked(r6);
            r6 = r3;
     */
    /* JADX WARNING: Missing block: B:42:0x0078, code skipped:
            if (r6 != null) goto L_0x007a;
     */
    /* JADX WARNING: Missing block: B:45:0x007c, code skipped:
            android.os.Trace.traceEnd(8);
     */
    public void doCallbacks(int r16, long r17) {
        /*
        r15 = this;
        r1 = r15;
        r2 = r16;
        r3 = r1.mLock;
        monitor-enter(r3);
        r4 = java.lang.System.nanoTime();	 Catch:{ all -> 0x0083 }
        r0 = r1.mCallbackQueues;	 Catch:{ all -> 0x0083 }
        r0 = r0[r2];	 Catch:{ all -> 0x0083 }
        r6 = 1000000; // 0xf4240 float:1.401298E-39 double:4.940656E-318;
        r6 = r4 / r6;
        r0 = r0.extractDueCallbacksLocked(r6);	 Catch:{ all -> 0x0083 }
        r6 = r0;
        if (r6 != 0) goto L_0x001c;
    L_0x001a:
        monitor-exit(r3);	 Catch:{ all -> 0x0083 }
        return;
    L_0x001c:
        r0 = 1;
        r1.mCallbacksRunning = r0;	 Catch:{ all -> 0x0083 }
        r0 = 4;
        r7 = 8;
        if (r2 != r0) goto L_0x0041;
    L_0x0024:
        r9 = r4 - r17;
        r0 = "jitterNanos";
        r11 = (int) r9;	 Catch:{ all -> 0x0083 }
        android.os.Trace.traceCounter(r7, r0, r11);	 Catch:{ all -> 0x0083 }
        r11 = 2;
        r13 = r1.mFrameIntervalNanos;	 Catch:{ all -> 0x0083 }
        r13 = r13 * r11;
        r0 = (r9 > r13 ? 1 : (r9 == r13 ? 0 : -1));
        if (r0 < 0) goto L_0x0041;
    L_0x0035:
        r11 = r1.mFrameIntervalNanos;	 Catch:{ all -> 0x0083 }
        r11 = r9 % r11;
        r13 = r1.mFrameIntervalNanos;	 Catch:{ all -> 0x0083 }
        r11 = r11 + r13;
        r13 = r4 - r11;
        r1.mLastFrameTimeNanos = r13;	 Catch:{ all -> 0x0088 }
        goto L_0x0043;
    L_0x0041:
        r13 = r17;
    L_0x0043:
        monitor-exit(r3);	 Catch:{ all -> 0x0088 }
        r3 = 0;
        r0 = CALLBACK_TRACE_TITLES;	 Catch:{ all -> 0x006c }
        r0 = r0[r2];	 Catch:{ all -> 0x006c }
        android.os.Trace.traceBegin(r7, r0);	 Catch:{ all -> 0x006c }
        r0 = r6;
    L_0x004d:
        if (r0 == 0) goto L_0x0056;
    L_0x004f:
        r0.run(r13);	 Catch:{ all -> 0x006c }
        r4 = r0.next;	 Catch:{ all -> 0x006c }
        r0 = r4;
        goto L_0x004d;
    L_0x0056:
        r4 = r1.mLock;
        monitor-enter(r4);
        r1.mCallbacksRunning = r3;	 Catch:{ all -> 0x0069 }
    L_0x005b:
        r0 = r6.next;	 Catch:{ all -> 0x0069 }
        r15.recycleCallbackLocked(r6);	 Catch:{ all -> 0x0069 }
        r6 = r0;
        if (r6 != 0) goto L_0x005b;
    L_0x0063:
        monitor-exit(r4);	 Catch:{ all -> 0x0069 }
        android.os.Trace.traceEnd(r7);
        return;
    L_0x0069:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0069 }
        throw r0;
    L_0x006c:
        r0 = move-exception;
        r4 = r1.mLock;
        monitor-enter(r4);
        r1.mCallbacksRunning = r3;	 Catch:{ all -> 0x0080 }
    L_0x0072:
        r3 = r6.next;	 Catch:{ all -> 0x0080 }
        r15.recycleCallbackLocked(r6);	 Catch:{ all -> 0x0080 }
        r6 = r3;
        if (r6 == 0) goto L_0x007b;
    L_0x007a:
        goto L_0x0072;
    L_0x007b:
        monitor-exit(r4);	 Catch:{ all -> 0x0080 }
        android.os.Trace.traceEnd(r7);
        throw r0;
    L_0x0080:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0080 }
        throw r0;
    L_0x0083:
        r0 = move-exception;
        r13 = r17;
    L_0x0086:
        monitor-exit(r3);	 Catch:{ all -> 0x0088 }
        throw r0;
    L_0x0088:
        r0 = move-exception;
        goto L_0x0086;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.view.Choreographer.doCallbacks(int, long):void");
    }

    /* Access modifiers changed, original: 0000 */
    public void doScheduleVsync() {
        synchronized (this.mLock) {
            if (this.mFrameScheduled) {
                scheduleVsyncLocked();
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doScheduleCallback(int callbackType) {
        synchronized (this.mLock) {
            if (!this.mFrameScheduled) {
                long now = SystemClock.uptimeMillis();
                if (this.mCallbackQueues[callbackType].hasDueCallbacksLocked(now)) {
                    scheduleFrameLocked(now);
                }
            }
        }
    }

    @UnsupportedAppUsage
    private void scheduleVsyncLocked() {
        this.mDisplayEventReceiver.scheduleVsync();
        this.mIsVsyncScheduled = true;
    }

    private boolean isRunningOnLooperThreadLocked() {
        return Looper.myLooper() == this.mLooper;
    }

    private CallbackRecord obtainCallbackLocked(long dueTime, Object action, Object token) {
        CallbackRecord callback = this.mCallbackPool;
        if (callback == null) {
            callback = new CallbackRecord();
        } else {
            this.mCallbackPool = callback.next;
            callback.next = null;
        }
        callback.dueTime = dueTime;
        callback.action = action;
        callback.token = token;
        return callback;
    }

    private void recycleCallbackLocked(CallbackRecord callback) {
        callback.action = null;
        callback.token = null;
        callback.next = this.mCallbackPool;
        this.mCallbackPool = callback;
    }

    public static void setNextFrameAtFront(boolean isAtFront) {
        if (SystemProperties.getBoolean("persist.sys.miui_optimization", true)) {
            sIsNextFrameAtFront = isAtFront;
        }
    }
}
