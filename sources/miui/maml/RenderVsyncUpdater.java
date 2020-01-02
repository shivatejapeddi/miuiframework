package miui.maml;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.TimeUtils;
import miui.maml.RendererController.ISelfUpdateRenderable;

public abstract class RenderVsyncUpdater implements ISelfUpdateRenderable {
    private boolean mAutoCleanup;
    private long mCreateTime;
    private long mDelay;
    private FrameDisplayEventReceiver mDisplayEventReceiver;
    private Handler mHandler;
    protected long mLastUpdateTime;
    protected long mNextUpdateInterval;
    private boolean mPaused;
    protected boolean mPendingRender;
    private ScreenElementRoot mRoot;
    private Runnable mRunUpdater;
    private Runnable mScheduleFrame;
    private boolean mStarted;
    private int mVsyncLeft;

    private final class FrameDisplayEventReceiver extends MamlDisplayEventReceiver implements Runnable {
        public FrameDisplayEventReceiver(Looper looper) {
            super(looper);
        }

        public void onVsync(long timestampNanos, long builtInDisplayId, int frame) {
            RenderVsyncUpdater.access$320(RenderVsyncUpdater.this, 1);
            RenderVsyncUpdater.this.mHandler.sendMessageAtTime(Message.obtain(RenderVsyncUpdater.this.mHandler, (Runnable) this), timestampNanos / TimeUtils.NANOS_PER_MS);
        }

        public void run() {
            if (RenderVsyncUpdater.this.mVsyncLeft <= 0) {
                RenderVsyncUpdater.this.scheduleFrame();
            } else {
                scheduleVsync();
            }
        }
    }

    public abstract void doRenderImp();

    static /* synthetic */ int access$320(RenderVsyncUpdater x0, int x1) {
        int i = x0.mVsyncLeft - x1;
        x0.mVsyncLeft = i;
        return i;
    }

    public RenderVsyncUpdater(ScreenElementRoot root, Handler h) {
        this(root, h, false);
    }

    public RenderVsyncUpdater(ScreenElementRoot root, Handler h, boolean autoCleanup) {
        this.mRunUpdater = new Runnable() {
            public void run() {
                RenderVsyncUpdater.this.doRunUpdater();
                RenderVsyncUpdater.this.mStarted = true;
            }
        };
        this.mScheduleFrame = new Runnable() {
            public void run() {
                RenderVsyncUpdater.this.scheduleFrame();
            }
        };
        this.mRoot = root;
        this.mHandler = h;
        this.mAutoCleanup = autoCleanup;
        this.mDisplayEventReceiver = new FrameDisplayEventReceiver(h.getLooper());
    }

    public void setStartDelay(long create, long delay) {
        this.mCreateTime = create;
        this.mDelay = delay;
        if (this.mDelay <= 0) {
            this.mStarted = true;
        }
    }

    public void setAutoCleanup(boolean autoCleanup) {
        this.mAutoCleanup = autoCleanup;
    }

    public final void doRender() {
        this.mPendingRender = true;
        doRenderImp();
    }

    public void runUpdater() {
        long delayToRun = checkDelay();
        if (delayToRun <= 0) {
            doRunUpdater();
            this.mStarted = true;
        } else if (!this.mHandler.hasCallbacks(this.mRunUpdater)) {
            this.mHandler.postDelayed(this.mRunUpdater, delayToRun);
        }
    }

    private void doRunUpdater() {
        if (this.mVsyncLeft > 0) {
            this.mDisplayEventReceiver.scheduleVsync();
        } else if (!this.mHandler.hasCallbacks(this.mScheduleFrame)) {
            this.mHandler.post(this.mScheduleFrame);
        }
    }

    private long checkDelay() {
        long j = 0;
        if (this.mDelay <= 0) {
            return 0;
        }
        long elapsed = SystemClock.elapsedRealtime() - this.mCreateTime;
        long j2 = this.mDelay;
        if (elapsed < j2) {
            j = j2 - elapsed;
        }
        return j;
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public void triggerUpdate() {
        runUpdater();
    }

    public void doneRender() {
        this.mPendingRender = false;
    }

    public void cleanUp() {
        this.mPaused = true;
        this.mRoot.selfFinish();
    }

    public void onPause() {
        this.mRoot.selfPause();
        this.mPaused = true;
    }

    public void onResume() {
        this.mPaused = false;
        this.mRoot.selfResume();
        runUpdater();
    }

    public void init() {
        this.mPaused = false;
        this.mRoot.setRenderControllerRenderable(this);
        this.mRoot.selfInit();
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        if (this.mAutoCleanup) {
            cleanUp();
        }
        super.finalize();
    }

    private void scheduleFrame() {
        this.mNextUpdateInterval = this.mRoot.update(SystemClock.elapsedRealtime());
        this.mVsyncLeft = (int) (this.mNextUpdateInterval / 16);
        int i = this.mVsyncLeft;
        if (i > 0) {
            this.mVsyncLeft = i - 1;
        }
    }
}
