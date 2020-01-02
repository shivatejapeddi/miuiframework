package miui.maml;

import android.util.Log;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import miui.maml.FramerateTokenList.FramerateChangeListener;
import miui.maml.FramerateTokenList.FramerateToken;
import miui.maml.elements.FramerateController;

public class RendererController implements FramerateChangeListener {
    private static final String LOG_TAG = "RendererController";
    private static final int MAX_MSG_COUNT = 3;
    private float mCurFramerate;
    private long mFrameTime;
    private ArrayList<FramerateController> mFramerateControllers;
    private FramerateTokenList mFramerateTokenList;
    private boolean mInited;
    private long mLastUpdateSystemTime;
    private Listener mListener;
    private byte[] mLock;
    private Object mMsgLock;
    private LinkedList<MotionEvent> mMsgQueue;
    private boolean mPaused;
    private boolean mPendingRender;
    private ArrayList<Runnable> mReadRunnableQueue;
    private RenderThread mRenderThread;
    private boolean mSelfPaused;
    private boolean mShouldUpdate;
    private float mTouchX;
    private float mTouchY;
    private ArrayList<Runnable> mWriteRunnableQueue;
    private Object mWriteRunnableQueueLock;

    public interface IRenderable {
        void doRender();
    }

    public interface ISelfUpdateRenderable extends IRenderable {
        void triggerUpdate();
    }

    public interface Listener extends ISelfUpdateRenderable {
        void finish();

        void init();

        void onHover(MotionEvent motionEvent);

        void onTouch(MotionEvent motionEvent);

        void pause();

        void resume();

        void tick(long j);
    }

    public static abstract class EmptyListener implements Listener {
        public void doRender() {
        }

        public void init() {
        }

        public void pause() {
        }

        public void resume() {
        }

        public void finish() {
        }

        public void tick(long currentTime) {
        }

        public void onTouch(MotionEvent event) {
        }

        public void onHover(MotionEvent event) {
        }
    }

    public RendererController() {
        this.mFramerateControllers = new ArrayList();
        this.mFramerateTokenList = new FramerateTokenList();
        this.mSelfPaused = true;
        this.mLock = new byte[0];
        this.mFrameTime = Long.MAX_VALUE;
        this.mMsgLock = new Object();
        this.mTouchX = -1.0f;
        this.mTouchY = -1.0f;
        this.mWriteRunnableQueue = new ArrayList();
        this.mReadRunnableQueue = new ArrayList();
        this.mWriteRunnableQueueLock = new Object();
        this.mFramerateTokenList = new FramerateTokenList(this);
    }

    public RendererController(Listener l) {
        this();
        setListener(l);
    }

    public void setListener(Listener l) {
        this.mListener = l;
    }

    public void setRenderThread(RenderThread renderThread) {
        this.mRenderThread = renderThread;
    }

    public final FramerateToken createToken(String name) {
        return this.mFramerateTokenList.createToken(name);
    }

    public final void requestUpdate() {
        this.mShouldUpdate = true;
        triggerUpdate();
    }

    public void finish() {
        synchronized (this.mLock) {
            if (this.mInited) {
                if (this.mListener != null) {
                    try {
                        this.mListener.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, e.toString());
                    }
                }
                synchronized (this.mMsgLock) {
                    if (this.mMsgQueue != null) {
                        while (this.mMsgQueue.size() > 0) {
                            ((MotionEvent) this.mMsgQueue.poll()).recycle();
                        }
                    }
                }
                synchronized (this.mWriteRunnableQueueLock) {
                    this.mWriteRunnableQueue.clear();
                }
                this.mInited = false;
                this.mFramerateTokenList.clear();
                return;
            }
        }
    }

    public void init() {
        synchronized (this.mLock) {
            if (this.mInited) {
                return;
            }
            if (this.mListener != null) {
                try {
                    this.mListener.init();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(LOG_TAG, e.toString());
                }
            }
            this.mInited = true;
        }
    }

    public void pause() {
        if (this.mInited) {
            synchronized (this.mLock) {
                this.mPaused = true;
                if (!(this.mSelfPaused || this.mListener == null)) {
                    this.mListener.pause();
                }
            }
            this.mPendingRender = false;
        }
    }

    public void resume() {
        if (this.mInited) {
            synchronized (this.mLock) {
                this.mPaused = false;
                if (!(this.mSelfPaused || this.mListener == null)) {
                    this.mListener.resume();
                }
            }
        }
    }

    public void addFramerateController(FramerateController framerateController) {
        if (!this.mFramerateControllers.contains(framerateController)) {
            this.mFramerateControllers.add(framerateController);
        }
    }

    public void removeFramerateController(FramerateController framerateController) {
        this.mFramerateControllers.remove(framerateController);
    }

    public final long updateFramerate(long time) {
        long l;
        long nextUpdateInterval = Long.MAX_VALUE;
        int N = this.mFramerateControllers.size();
        for (int i = 0; i < N; i++) {
            l = ((FramerateController) this.mFramerateControllers.get(i)).updateFramerate(time);
            if (l < nextUpdateInterval) {
                nextUpdateInterval = l;
            }
        }
        float framerate = this.mFramerateTokenList.getFramerate();
        float f = this.mCurFramerate;
        if (f != framerate) {
            if (f >= 1.0f && framerate < 1.0f) {
                requestUpdate();
            }
            this.mCurFramerate = framerate;
            this.mFrameTime = framerate != 0.0f ? (long) (1000.0f / framerate) : Long.MAX_VALUE;
        }
        l = this.mFrameTime;
        return l < nextUpdateInterval ? l : nextUpdateInterval;
    }

    public final void doRender() {
        Listener listener = this.mListener;
        if (listener != null) {
            this.mPendingRender = true;
            listener.doRender();
        }
    }

    public final boolean pendingRender() {
        return this.mPendingRender;
    }

    public void selfPause() {
        if (this.mInited) {
            synchronized (this.mLock) {
                if (!this.mSelfPaused) {
                    this.mSelfPaused = true;
                    if (!(this.mPaused || this.mListener == null)) {
                        this.mListener.pause();
                    }
                }
            }
            this.mPendingRender = false;
        }
    }

    public void selfResume() {
        if (this.mInited) {
            synchronized (this.mLock) {
                if (this.mSelfPaused) {
                    this.mSelfPaused = false;
                    if (!(this.mPaused || this.mListener == null)) {
                        this.mListener.resume();
                    }
                }
            }
            RenderThread renderThread = this.mRenderThread;
            if (renderThread != null) {
                renderThread.setPaused(false);
            }
        }
    }

    public final boolean isSelfPaused() {
        return this.mSelfPaused;
    }

    public final void doneRender() {
        this.mPendingRender = false;
        triggerUpdate();
    }

    public void triggerUpdate() {
        RenderThread renderThread = this.mRenderThread;
        if (renderThread != null) {
            renderThread.signal();
        }
        Listener listener = this.mListener;
        if (listener != null) {
            listener.triggerUpdate();
        }
    }

    public long updateIfNeeded(long currentTime) {
        long nextUpdateInterval = updateFramerate(currentTime);
        long j = this.mFrameTime;
        long j2 = Long.MAX_VALUE;
        if (j < Long.MAX_VALUE) {
            j2 = j - (currentTime - this.mLastUpdateSystemTime);
        }
        j = j2;
        boolean hasRunnable = hasRunnable();
        if (j <= 0 || this.mShouldUpdate || hasMessage() || hasRunnable) {
            if (this.mPendingRender && !hasRunnable) {
                return nextUpdateInterval;
            }
            runRunnables();
            MotionEvent event = getMessage();
            if (event != null) {
                if (event.isTouchEvent()) {
                    onTouch(event);
                } else {
                    onHover(event);
                }
            }
            tick(currentTime);
            doRender();
            if (this.mShouldUpdate || hasMessage()) {
                return 0;
            }
            return nextUpdateInterval;
        } else if (j < nextUpdateInterval) {
            return j;
        } else {
            return nextUpdateInterval;
        }
    }

    public long update(long currentTime) {
        long nextUpdateInterval = updateFramerate(currentTime);
        boolean hasRunnable = hasRunnable();
        if (this.mPendingRender && !hasRunnable) {
            return nextUpdateInterval;
        }
        runRunnables();
        MotionEvent event = getMessage();
        if (event != null) {
            if (event.isTouchEvent()) {
                onTouch(event);
            } else {
                onHover(event);
            }
        }
        tick(currentTime);
        doRender();
        if (this.mShouldUpdate || hasMessage()) {
            return 0;
        }
        return nextUpdateInterval;
    }

    public void tick(long currentTime) {
        this.mShouldUpdate = false;
        Listener listener = this.mListener;
        if (listener != null) {
            try {
                listener.tick(currentTime);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(LOG_TAG, e.toString());
            }
        }
        this.mLastUpdateSystemTime = currentTime;
    }

    public void onTouch(MotionEvent event) {
        String str = LOG_TAG;
        if (event != null) {
            Listener listener = this.mListener;
            if (listener != null) {
                try {
                    listener.onTouch(event);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(str, e.toString());
                } catch (OutOfMemoryError e2) {
                    e2.printStackTrace();
                    Log.e(str, e2.toString());
                }
            }
        }
    }

    public void onHover(MotionEvent event) {
        String str = LOG_TAG;
        if (event != null) {
            Listener listener = this.mListener;
            if (listener != null) {
                try {
                    listener.onHover(event);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(str, e.toString());
                } catch (OutOfMemoryError e2) {
                    e2.printStackTrace();
                    Log.e(str, e2.toString());
                }
            }
        }
    }

    public final boolean hasMessage() {
        boolean z = false;
        if (this.mMsgQueue == null) {
            return false;
        }
        synchronized (this.mMsgLock) {
            if (this.mMsgQueue != null && this.mMsgQueue.size() > 0) {
                z = true;
            }
        }
        return z;
    }

    public final MotionEvent getMessage() {
        MotionEvent motionEvent = null;
        if (this.mMsgQueue == null) {
            return null;
        }
        synchronized (this.mMsgLock) {
            if (this.mMsgQueue != null) {
                motionEvent = (MotionEvent) this.mMsgQueue.poll();
            }
        }
        return motionEvent;
    }

    public void postMessage(MotionEvent e) {
        synchronized (this.mMsgLock) {
            if (this.mMsgQueue == null) {
                this.mMsgQueue = new LinkedList();
            }
            if (!(e.getActionMasked() == 2 && e.getX() == this.mTouchX && e.getY() == this.mTouchY)) {
                this.mMsgQueue.add(e);
                this.mTouchX = e.getX();
                this.mTouchY = e.getY();
            }
            if (this.mMsgQueue.size() > 3) {
                MotionEvent dropEvent = null;
                Iterator it = this.mMsgQueue.iterator();
                while (it.hasNext()) {
                    MotionEvent me = (MotionEvent) it.next();
                    if (me.getActionMasked() == 2) {
                        dropEvent = me;
                        break;
                    }
                }
                if (dropEvent != null) {
                    this.mMsgQueue.remove(dropEvent);
                    dropEvent.recycle();
                }
            }
        }
        triggerUpdate();
    }

    public final boolean hasRunnable() {
        boolean ret;
        synchronized (this.mWriteRunnableQueueLock) {
            ret = !this.mWriteRunnableQueue.isEmpty();
        }
        return ret;
    }

    public void postRunnable(Runnable r) {
        if (r != null) {
            synchronized (this.mWriteRunnableQueueLock) {
                if (!this.mWriteRunnableQueue.contains(r)) {
                    this.mWriteRunnableQueue.add(r);
                }
            }
            requestUpdate();
            return;
        }
        throw new NullPointerException("postRunnable null");
    }

    public void runRunnables() {
        synchronized (this.mWriteRunnableQueueLock) {
            ArrayList<Runnable> temp = this.mWriteRunnableQueue;
            this.mWriteRunnableQueue = this.mReadRunnableQueue;
            this.mReadRunnableQueue = temp;
        }
        int N = this.mReadRunnableQueue.size();
        for (int i = 0; i < N; i++) {
            ((Runnable) this.mReadRunnableQueue.get(i)).run();
        }
        this.mReadRunnableQueue.clear();
    }

    public void onFrameRateChage(float old, float cur) {
        if (cur > 0.0f) {
            triggerUpdate();
        }
    }

    public final boolean hasInited() {
        return this.mInited;
    }
}
