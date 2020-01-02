package android.animation;

import android.annotation.UnsupportedAppUsage;
import android.content.res.ConstantState;
import java.util.ArrayList;

public abstract class Animator implements Cloneable {
    public static final long DURATION_INFINITE = -1;
    int mChangingConfigurations = 0;
    private AnimatorConstantState mConstantState;
    ArrayList<AnimatorListener> mListeners = null;
    ArrayList<AnimatorPauseListener> mPauseListeners = null;
    boolean mPaused = false;

    private static class AnimatorConstantState extends ConstantState<Animator> {
        final Animator mAnimator;
        int mChangingConf = this.mAnimator.getChangingConfigurations();

        public AnimatorConstantState(Animator animator) {
            this.mAnimator = animator;
            this.mAnimator.mConstantState = this;
        }

        public int getChangingConfigurations() {
            return this.mChangingConf;
        }

        public Animator newInstance() {
            Animator clone = this.mAnimator.clone();
            clone.mConstantState = this;
            return clone;
        }
    }

    public interface AnimatorListener {
        void onAnimationCancel(Animator animator);

        void onAnimationEnd(Animator animator);

        void onAnimationRepeat(Animator animator);

        void onAnimationStart(Animator animator);

        void onAnimationStart(Animator animation, boolean isReverse) {
            onAnimationStart(animation);
        }

        void onAnimationEnd(Animator animation, boolean isReverse) {
            onAnimationEnd(animation);
        }
    }

    public interface AnimatorPauseListener {
        void onAnimationPause(Animator animator);

        void onAnimationResume(Animator animator);
    }

    public abstract long getDuration();

    public abstract long getStartDelay();

    public abstract boolean isRunning();

    public abstract Animator setDuration(long j);

    public abstract void setInterpolator(TimeInterpolator timeInterpolator);

    public abstract void setStartDelay(long j);

    public void start() {
    }

    public void cancel() {
    }

    public void end() {
    }

    public void pause() {
        if (isStarted() && !this.mPaused) {
            this.mPaused = true;
            ArrayList arrayList = this.mPauseListeners;
            if (arrayList != null) {
                ArrayList<AnimatorPauseListener> tmpListeners = (ArrayList) arrayList.clone();
                int numListeners = tmpListeners.size();
                for (int i = 0; i < numListeners; i++) {
                    ((AnimatorPauseListener) tmpListeners.get(i)).onAnimationPause(this);
                }
            }
        }
    }

    public void resume() {
        if (this.mPaused) {
            this.mPaused = false;
            ArrayList arrayList = this.mPauseListeners;
            if (arrayList != null) {
                ArrayList<AnimatorPauseListener> tmpListeners = (ArrayList) arrayList.clone();
                int numListeners = tmpListeners.size();
                for (int i = 0; i < numListeners; i++) {
                    ((AnimatorPauseListener) tmpListeners.get(i)).onAnimationResume(this);
                }
            }
        }
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    public long getTotalDuration() {
        long duration = getDuration();
        if (duration == -1) {
            return -1;
        }
        return getStartDelay() + duration;
    }

    public TimeInterpolator getInterpolator() {
        return null;
    }

    public boolean isStarted() {
        return isRunning();
    }

    public void addListener(AnimatorListener listener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList();
        }
        this.mListeners.add(listener);
    }

    public void removeListener(AnimatorListener listener) {
        ArrayList arrayList = this.mListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
            if (this.mListeners.size() == 0) {
                this.mListeners = null;
            }
        }
    }

    public ArrayList<AnimatorListener> getListeners() {
        return this.mListeners;
    }

    public void addPauseListener(AnimatorPauseListener listener) {
        if (this.mPauseListeners == null) {
            this.mPauseListeners = new ArrayList();
        }
        this.mPauseListeners.add(listener);
    }

    public void removePauseListener(AnimatorPauseListener listener) {
        ArrayList arrayList = this.mPauseListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
            if (this.mPauseListeners.size() == 0) {
                this.mPauseListeners = null;
            }
        }
    }

    public void removeAllListeners() {
        ArrayList arrayList = this.mListeners;
        if (arrayList != null) {
            arrayList.clear();
            this.mListeners = null;
        }
        arrayList = this.mPauseListeners;
        if (arrayList != null) {
            arrayList.clear();
            this.mPauseListeners = null;
        }
    }

    public int getChangingConfigurations() {
        return this.mChangingConfigurations;
    }

    public void setChangingConfigurations(int configs) {
        this.mChangingConfigurations = configs;
    }

    public void appendChangingConfigurations(int configs) {
        this.mChangingConfigurations |= configs;
    }

    public ConstantState<Animator> createConstantState() {
        return new AnimatorConstantState(this);
    }

    public Animator clone() {
        try {
            Animator anim = (Animator) super.clone();
            if (this.mListeners != null) {
                anim.mListeners = new ArrayList(this.mListeners);
            }
            if (this.mPauseListeners != null) {
                anim.mPauseListeners = new ArrayList(this.mPauseListeners);
            }
            return anim;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void setupStartValues() {
    }

    public void setupEndValues() {
    }

    public void setTarget(Object target) {
    }

    public boolean canReverse() {
        return false;
    }

    @UnsupportedAppUsage
    public void reverse() {
        throw new IllegalStateException("Reverse is not supported");
    }

    /* Access modifiers changed, original: 0000 */
    public boolean pulseAnimationFrame(long frameTime) {
        return false;
    }

    /* Access modifiers changed, original: 0000 */
    public void startWithoutPulsing(boolean inReverse) {
        if (inReverse) {
            reverse();
        } else {
            start();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void skipToEndValue(boolean inReverse) {
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isInitialized() {
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public void animateBasedOnPlayTime(long currentPlayTime, long lastPlayTime, boolean inReverse) {
    }

    public void setAllowRunningAsynchronously(boolean mayRunAsync) {
    }
}
