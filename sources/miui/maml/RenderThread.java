package miui.maml;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import java.util.ArrayList;

public class RenderThread extends Thread {
    private static final String LOG_TAG = "RenderThread";
    private static RenderThread sGlobalThread;
    private static Object sGlobalThreadLock = new Object();
    private CommandThreadHandler mCmdHanlder;
    private HandlerThread mCmdThread;
    private boolean mPaused = true;
    private ArrayList<RendererController> mRendererControllerList = new ArrayList();
    private Object mResumeSignal = new Object();
    private boolean mSignaled;
    private Object mSleepSignal = new Object();
    private boolean mStarted;
    private boolean mStop;

    private class CommandThreadHandler extends Handler {
        private static final int MSG_PAUSE = 0;
        private static final int MSG_RESUME = 1;

        public CommandThreadHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            int i = msg.what;
            if (i == 0) {
                RenderThread.this.setPausedImpl(true);
            } else if (i == 1) {
                RenderThread.this.setPausedImpl(false);
            }
        }

        public void setPause(boolean pause) {
            Message msg = new Message();
            msg.what = pause ^ 1;
            sendMessage(msg);
        }
    }

    public RenderThread(RendererController c) {
        super("MAML RenderThread");
        addRendererController(c);
        initCmdThread();
    }

    public RenderThread() {
        super("MAML RenderThread");
        initCmdThread();
    }

    private void initCmdThread() {
        this.mCmdThread = new HandlerThread("cmd");
        this.mCmdThread.start();
        this.mCmdHanlder = new CommandThreadHandler(this.mCmdThread.getLooper());
    }

    public static void globalThreadStop() {
        synchronized (sGlobalThreadLock) {
            if (sGlobalThread != null) {
                sGlobalThread.setStop();
                sGlobalThread = null;
            }
        }
    }

    public static RenderThread globalThread() {
        return globalThread(false);
    }

    public static RenderThread globalThread(boolean ensureStart) {
        RenderThread renderThread;
        synchronized (sGlobalThreadLock) {
            if (sGlobalThread == null) {
                sGlobalThread = new RenderThread();
            }
            if (ensureStart && !sGlobalThread.isStarted()) {
                try {
                    sGlobalThread.start();
                } catch (IllegalThreadStateException e) {
                }
            }
            renderThread = sGlobalThread;
        }
        return renderThread;
    }

    public void addRendererController(RendererController c) {
        synchronized (this.mRendererControllerList) {
            if (this.mRendererControllerList.contains(c)) {
                Log.w(LOG_TAG, "addRendererController: RendererController already exists");
                return;
            }
            c.setRenderThread(this);
            this.mRendererControllerList.add(c);
            setPaused(false);
        }
    }

    public void removeRendererController(RendererController c) {
        synchronized (this.mRendererControllerList) {
            this.mRendererControllerList.remove(c);
            c.setRenderThread(null);
        }
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public void setPaused(boolean pause) {
        this.mCmdHanlder.setPause(pause);
    }

    private void setPausedImpl(boolean pause) {
        if (this.mStop) {
            signal();
        }
        if (this.mPaused != pause) {
            synchronized (this.mResumeSignal) {
                this.mPaused = pause;
                if (!pause) {
                    this.mResumeSignal.notify();
                }
            }
            signal();
        }
    }

    public void setStop() {
        this.mStop = true;
        setPaused(false);
    }

    public void run() {
        Log.i(LOG_TAG, "RenderThread started");
        try {
            doInit();
            this.mStarted = true;
            while (!this.mStop) {
                if (this.mPaused) {
                    synchronized (this.mResumeSignal) {
                        if (this.mPaused) {
                            doPause();
                            Log.i(LOG_TAG, "RenderThread paused, waiting for signal");
                            waiteForResume();
                            Log.i(LOG_TAG, "RenderThread resumed");
                            doResume();
                        }
                    }
                }
                if (this.mStop) {
                    break;
                }
                long currentTime = SystemClock.elapsedRealtime();
                long nextUpdateInterval = Long.MAX_VALUE;
                synchronized (this.mRendererControllerList) {
                    int N = this.mRendererControllerList.size();
                    boolean allPause = true;
                    for (int i = 0; i < N; i++) {
                        if (this.mPaused) {
                            break;
                        }
                        RendererController c = (RendererController) this.mRendererControllerList.get(i);
                        if (!c.isSelfPaused() || c.hasRunnable()) {
                            allPause = false;
                            if (!c.hasInited()) {
                                c.init();
                            }
                            long l = c.updateIfNeeded(currentTime);
                            if (l < nextUpdateInterval) {
                                nextUpdateInterval = l;
                            }
                        }
                    }
                    if (N != 0) {
                        if (!allPause) {
                            waitSleep(nextUpdateInterval);
                            this.mSignaled = false;
                        }
                    }
                    this.mPaused = true;
                    Log.i(LOG_TAG, "All controllers paused.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(LOG_TAG, e.toString());
        } catch (OutOfMemoryError e2) {
            e2.printStackTrace();
            Log.e(LOG_TAG, e2.toString());
        }
        doFinish();
        this.mCmdThread.quit();
        Log.i(LOG_TAG, "RenderThread stopped");
    }

    public void signal() {
        if (!this.mSignaled) {
            synchronized (this.mSleepSignal) {
                this.mSignaled = true;
                this.mSleepSignal.notify();
            }
        }
    }

    private final void waitSleep(long t) {
        if (!this.mSignaled && t > 0) {
            synchronized (this.mSleepSignal) {
                if (!this.mSignaled) {
                    try {
                        this.mSleepSignal.wait(t);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void doInit() {
        synchronized (this.mRendererControllerList) {
            if (this.mRendererControllerList.size() == 0) {
                return;
            }
            int N = this.mRendererControllerList.size();
            for (int i = 0; i < N; i++) {
                RendererController c = (RendererController) this.mRendererControllerList.get(i);
                c.init();
                c.requestUpdate();
            }
        }
    }

    private void doPause() {
        synchronized (this.mRendererControllerList) {
            if (this.mRendererControllerList.size() == 0) {
                return;
            }
            int N = this.mRendererControllerList.size();
            for (int i = 0; i < N; i++) {
                ((RendererController) this.mRendererControllerList.get(i)).pause();
            }
        }
    }

    private void doResume() {
        synchronized (this.mRendererControllerList) {
            if (this.mRendererControllerList.size() == 0) {
                return;
            }
            int N = this.mRendererControllerList.size();
            for (int i = 0; i < N; i++) {
                ((RendererController) this.mRendererControllerList.get(i)).resume();
            }
        }
    }

    private void doFinish() {
        synchronized (this.mRendererControllerList) {
            if (this.mRendererControllerList.size() == 0) {
                return;
            }
            int N = this.mRendererControllerList.size();
            for (int i = 0; i < N; i++) {
                ((RendererController) this.mRendererControllerList.get(i)).finish();
            }
        }
    }

    private void waiteForResume() {
        try {
            this.mResumeSignal.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
