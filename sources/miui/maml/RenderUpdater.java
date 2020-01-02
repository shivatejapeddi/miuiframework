package miui.maml;

import android.os.Handler;
import android.os.SystemClock;
import miui.maml.RendererController.ISelfUpdateRenderable;

public abstract class RenderUpdater implements ISelfUpdateRenderable {
    private boolean mAutoCleanup;
    private long mCreateTime;
    private long mDelay;
    private Handler mHandler;
    protected long mLastUpdateTime;
    protected long mNextUpdateInterval;
    private boolean mPaused;
    protected boolean mPendingRender;
    private ScreenElementRoot mRoot;
    private Runnable mRunUpdater;
    private boolean mSignaled;
    private boolean mStarted;
    private Runnable mUpdater;

    public abstract void doRenderImp();

    public RenderUpdater(ScreenElementRoot root, Handler h) {
        this(root, h, false);
    }

    public RenderUpdater(ScreenElementRoot root, Handler h, boolean autoCleanup) {
        this.mUpdater = new Runnable() {
            public void run() {
                RenderUpdater.this.mSignaled = false;
                long currentTime = SystemClock.elapsedRealtime();
                RenderUpdater renderUpdater = RenderUpdater.this;
                renderUpdater.mNextUpdateInterval = renderUpdater.mRoot.updateIfNeeded(currentTime);
                renderUpdater = RenderUpdater.this;
                renderUpdater.mLastUpdateTime = currentTime;
                if (!renderUpdater.mPendingRender && !RenderUpdater.this.mPaused && !RenderUpdater.this.mSignaled && RenderUpdater.this.mNextUpdateInterval < Long.MAX_VALUE) {
                    RenderUpdater.this.mHandler.postDelayed(RenderUpdater.this.mUpdater, RenderUpdater.this.mNextUpdateInterval);
                }
            }
        };
        this.mRunUpdater = new Runnable() {
            public void run() {
                RenderUpdater.this.doRunUpdater();
                RenderUpdater.this.mStarted = true;
            }
        };
        this.mRoot = root;
        this.mHandler = h;
        this.mAutoCleanup = autoCleanup;
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
        long delayToRun = this.mStarted ? 0 : checkDelay();
        if (delayToRun <= 0) {
            doRunUpdater();
            this.mStarted = true;
        } else if (!this.mHandler.hasCallbacks(this.mRunUpdater)) {
            this.mHandler.postDelayed(this.mRunUpdater, delayToRun);
        }
    }

    private void doRunUpdater() {
        if (!this.mSignaled) {
            this.mSignaled = true;
            this.mHandler.removeCallbacks(this.mUpdater);
            this.mHandler.post(this.mUpdater);
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
        if (!this.mPaused && !this.mSignaled) {
            long j = this.mNextUpdateInterval;
            if (j < Long.MAX_VALUE) {
                this.mHandler.postDelayed(this.mUpdater, j - (SystemClock.elapsedRealtime() - this.mLastUpdateTime));
            }
        }
    }

    public void cleanUp() {
        this.mHandler.removeCallbacks(this.mUpdater);
        this.mPaused = true;
        this.mRoot.selfFinish();
        this.mSignaled = false;
    }

    public void onPause() {
        this.mRoot.selfPause();
        this.mSignaled = false;
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
}
