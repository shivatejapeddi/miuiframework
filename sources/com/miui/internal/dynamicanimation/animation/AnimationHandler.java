package com.miui.internal.dynamicanimation.animation;

import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.ArrayMap;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import java.util.ArrayList;

class AnimationHandler {
    private static final long FRAME_DELAY_MS = 10;
    public static final ThreadLocal<AnimationHandler> sAnimatorHandler = new ThreadLocal();
    final ArrayList<AnimationFrameCallback> mAnimationCallbacks = new ArrayList();
    private final AnimationCallbackDispatcher mCallbackDispatcher = new AnimationCallbackDispatcher();
    long mCurrentFrameTime = 0;
    private final ArrayMap<AnimationFrameCallback, Long> mDelayedCallbackStartTime = new ArrayMap();
    private boolean mListDirty = false;
    private AnimationFrameCallbackProvider mProvider;

    class AnimationCallbackDispatcher {
        AnimationCallbackDispatcher() {
        }

        /* Access modifiers changed, original: 0000 */
        public void dispatchAnimationFrame() {
            AnimationHandler.this.mCurrentFrameTime = SystemClock.uptimeMillis();
            AnimationHandler animationHandler = AnimationHandler.this;
            animationHandler.doAnimationFrame(animationHandler.mCurrentFrameTime);
            if (AnimationHandler.this.mAnimationCallbacks.size() > 0) {
                AnimationHandler.this.getProvider().postFrameCallback();
            }
        }
    }

    interface AnimationFrameCallback {
        boolean doAnimationFrame(long j);
    }

    static abstract class AnimationFrameCallbackProvider {
        final AnimationCallbackDispatcher mDispatcher;

        public abstract void postFrameCallback();

        AnimationFrameCallbackProvider(AnimationCallbackDispatcher dispatcher) {
            this.mDispatcher = dispatcher;
        }
    }

    private static class FrameCallbackProvider14 extends AnimationFrameCallbackProvider {
        private final Handler mHandler = new Handler(Looper.myLooper());
        long mLastFrameTime = -1;
        private final Runnable mRunnable = new Runnable() {
            public void run() {
                FrameCallbackProvider14.this.mLastFrameTime = SystemClock.uptimeMillis();
                FrameCallbackProvider14.this.mDispatcher.dispatchAnimationFrame();
            }
        };

        FrameCallbackProvider14(AnimationCallbackDispatcher dispatcher) {
            super(dispatcher);
        }

        /* Access modifiers changed, original: 0000 */
        public void postFrameCallback() {
            this.mHandler.postDelayed(this.mRunnable, Math.max(AnimationHandler.FRAME_DELAY_MS - (SystemClock.uptimeMillis() - this.mLastFrameTime), 0));
        }
    }

    private static class FrameCallbackProvider16 extends AnimationFrameCallbackProvider {
        private final Choreographer mChoreographer = Choreographer.getInstance();
        private final FrameCallback mChoreographerCallback = new FrameCallback() {
            public void doFrame(long frameTimeNanos) {
                FrameCallbackProvider16.this.mDispatcher.dispatchAnimationFrame();
            }
        };

        FrameCallbackProvider16(AnimationCallbackDispatcher dispatcher) {
            super(dispatcher);
        }

        /* Access modifiers changed, original: 0000 */
        public void postFrameCallback() {
            this.mChoreographer.postFrameCallback(this.mChoreographerCallback);
        }
    }

    AnimationHandler() {
    }

    public static AnimationHandler getInstance() {
        if (sAnimatorHandler.get() == null) {
            sAnimatorHandler.set(new AnimationHandler());
        }
        return (AnimationHandler) sAnimatorHandler.get();
    }

    public static long getFrameTime() {
        if (sAnimatorHandler.get() == null) {
            return 0;
        }
        return ((AnimationHandler) sAnimatorHandler.get()).mCurrentFrameTime;
    }

    public void setProvider(AnimationFrameCallbackProvider provider) {
        this.mProvider = provider;
    }

    /* Access modifiers changed, original: 0000 */
    public AnimationFrameCallbackProvider getProvider() {
        if (this.mProvider == null) {
            if (VERSION.SDK_INT >= 16) {
                this.mProvider = new FrameCallbackProvider16(this.mCallbackDispatcher);
            } else {
                this.mProvider = new FrameCallbackProvider14(this.mCallbackDispatcher);
            }
        }
        return this.mProvider;
    }

    public void addAnimationFrameCallback(AnimationFrameCallback callback, long delay) {
        if (this.mAnimationCallbacks.size() == 0) {
            getProvider().postFrameCallback();
        }
        if (!this.mAnimationCallbacks.contains(callback)) {
            this.mAnimationCallbacks.add(callback);
        }
        if (delay > 0) {
            this.mDelayedCallbackStartTime.put(callback, Long.valueOf(SystemClock.uptimeMillis() + delay));
        }
    }

    public void removeCallback(AnimationFrameCallback callback) {
        this.mDelayedCallbackStartTime.remove(callback);
        int id = this.mAnimationCallbacks.indexOf(callback);
        if (id >= 0) {
            this.mAnimationCallbacks.set(id, null);
            this.mListDirty = true;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doAnimationFrame(long frameTime) {
        long currentTime = SystemClock.uptimeMillis();
        for (int i = 0; i < this.mAnimationCallbacks.size(); i++) {
            AnimationFrameCallback callback = (AnimationFrameCallback) this.mAnimationCallbacks.get(i);
            if (callback != null && isCallbackDue(callback, currentTime)) {
                callback.doAnimationFrame(frameTime);
            }
        }
        cleanUpList();
    }

    private boolean isCallbackDue(AnimationFrameCallback callback, long currentTime) {
        Long startTime = (Long) this.mDelayedCallbackStartTime.get(callback);
        if (startTime == null) {
            return true;
        }
        if (startTime.longValue() >= currentTime) {
            return false;
        }
        this.mDelayedCallbackStartTime.remove(callback);
        return true;
    }

    private void cleanUpList() {
        if (this.mListDirty) {
            for (int i = this.mAnimationCallbacks.size() - 1; i >= 0; i--) {
                if (this.mAnimationCallbacks.get(i) == null) {
                    this.mAnimationCallbacks.remove(i);
                }
            }
            this.mListDirty = false;
        }
    }
}
