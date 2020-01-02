package android.animation;

import android.animation.Animator.AnimatorListener;
import android.annotation.UnsupportedAppUsage;
import android.os.Looper;
import android.os.Trace;
import android.util.AndroidRuntimeException;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ValueAnimator extends Animator implements AnimationFrameCallback {
    private static final boolean DEBUG = false;
    public static final int INFINITE = -1;
    public static final int RESTART = 1;
    public static final int REVERSE = 2;
    private static final String TAG = "ValueAnimator";
    private static final TimeInterpolator sDefaultInterpolator = new AccelerateDecelerateInterpolator();
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private static float sDurationScale = 1.0f;
    private boolean mAnimationEndRequested = false;
    private float mCurrentFraction = 0.0f;
    @UnsupportedAppUsage
    private long mDuration = 300;
    private float mDurationScale = -1.0f;
    private long mFirstFrameTime = -1;
    boolean mInitialized = false;
    private TimeInterpolator mInterpolator = sDefaultInterpolator;
    private long mLastFrameTime = -1;
    private float mOverallFraction = 0.0f;
    private long mPauseTime;
    private int mRepeatCount = 0;
    private int mRepeatMode = 1;
    private boolean mResumed = false;
    private boolean mReversing;
    private boolean mRunning = false;
    float mSeekFraction = -1.0f;
    private boolean mSelfPulse = true;
    private long mStartDelay = 0;
    private boolean mStartListenersCalled = false;
    long mStartTime = -1;
    boolean mStartTimeCommitted;
    private boolean mStarted = false;
    private boolean mSuppressSelfPulseRequested = false;
    ArrayList<AnimatorUpdateListener> mUpdateListeners = null;
    PropertyValuesHolder[] mValues;
    HashMap<String, PropertyValuesHolder> mValuesMap;

    public interface AnimatorUpdateListener {
        void onAnimationUpdate(ValueAnimator valueAnimator);
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RepeatMode {
    }

    public static void setDurationScale(float durationScale) {
        sDurationScale = durationScale;
    }

    public static float getDurationScale() {
        return sDurationScale;
    }

    public static boolean areAnimatorsEnabled() {
        return sDurationScale != 0.0f;
    }

    public static ValueAnimator ofInt(int... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(values);
        return anim;
    }

    public static ValueAnimator ofArgb(int... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(values);
        anim.setEvaluator(ArgbEvaluator.getInstance());
        return anim;
    }

    public static ValueAnimator ofFloat(float... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setFloatValues(values);
        return anim;
    }

    public static ValueAnimator ofPropertyValuesHolder(PropertyValuesHolder... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setValues(values);
        return anim;
    }

    public static ValueAnimator ofObject(TypeEvaluator evaluator, Object... values) {
        ValueAnimator anim = new ValueAnimator();
        anim.setObjectValues(values);
        anim.setEvaluator(evaluator);
        return anim;
    }

    public void setIntValues(int... values) {
        if (values != null && values.length != 0) {
            PropertyValuesHolder valuesHolder = this.mValues;
            if (valuesHolder == null || valuesHolder.length == 0) {
                setValues(PropertyValuesHolder.ofInt("", values));
            } else {
                valuesHolder[0].setIntValues(values);
            }
            this.mInitialized = false;
        }
    }

    public void setFloatValues(float... values) {
        if (values != null && values.length != 0) {
            PropertyValuesHolder valuesHolder = this.mValues;
            if (valuesHolder == null || valuesHolder.length == 0) {
                setValues(PropertyValuesHolder.ofFloat("", values));
            } else {
                valuesHolder[0].setFloatValues(values);
            }
            this.mInitialized = false;
        }
    }

    public void setObjectValues(Object... values) {
        if (values != null && values.length != 0) {
            PropertyValuesHolder valuesHolder = this.mValues;
            if (valuesHolder == null || valuesHolder.length == 0) {
                setValues(PropertyValuesHolder.ofObject("", null, values));
            } else {
                valuesHolder[0].setObjectValues(values);
            }
            this.mInitialized = false;
        }
    }

    public void setValues(PropertyValuesHolder... values) {
        this.mValues = values;
        this.mValuesMap = new HashMap(numValues);
        for (PropertyValuesHolder valuesHolder : values) {
            this.mValuesMap.put(valuesHolder.getPropertyName(), valuesHolder);
        }
        this.mInitialized = false;
    }

    public PropertyValuesHolder[] getValues() {
        return this.mValues;
    }

    /* Access modifiers changed, original: 0000 */
    public void initAnimation() {
        if (!this.mInitialized) {
            for (PropertyValuesHolder init : this.mValues) {
                init.init();
            }
            this.mInitialized = true;
        }
    }

    public ValueAnimator setDuration(long duration) {
        if (duration >= 0) {
            this.mDuration = duration;
            return this;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Animators cannot have negative duration: ");
        stringBuilder.append(duration);
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public void overrideDurationScale(float durationScale) {
        this.mDurationScale = durationScale;
    }

    private float resolveDurationScale() {
        float f = this.mDurationScale;
        return f >= 0.0f ? f : sDurationScale;
    }

    private long getScaledDuration() {
        return (long) (((float) this.mDuration) * resolveDurationScale());
    }

    public long getDuration() {
        return this.mDuration;
    }

    public long getTotalDuration() {
        int i = this.mRepeatCount;
        if (i == -1) {
            return -1;
        }
        return this.mStartDelay + (this.mDuration * ((long) (i + 1)));
    }

    public void setCurrentPlayTime(long playTime) {
        long j = this.mDuration;
        setCurrentFraction(j > 0 ? ((float) playTime) / ((float) j) : 1.0f);
    }

    public void setCurrentFraction(float fraction) {
        initAnimation();
        fraction = clampFraction(fraction);
        this.mStartTimeCommitted = true;
        if (isPulsingInternal()) {
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis() - ((long) (((float) getScaledDuration()) * fraction));
        } else {
            this.mSeekFraction = fraction;
        }
        this.mOverallFraction = fraction;
        animateValue(getCurrentIterationFraction(fraction, this.mReversing));
    }

    private int getCurrentIteration(float fraction) {
        fraction = clampFraction(fraction);
        double iteration = Math.floor((double) fraction);
        if (((double) fraction) == iteration && fraction > 0.0f) {
            iteration -= 1.0d;
        }
        return (int) iteration;
    }

    private float getCurrentIterationFraction(float fraction, boolean inReverse) {
        fraction = clampFraction(fraction);
        int iteration = getCurrentIteration(fraction);
        float currentFraction = fraction - ((float) iteration);
        return shouldPlayBackward(iteration, inReverse) ? 1.0f - currentFraction : currentFraction;
    }

    private float clampFraction(float fraction) {
        if (fraction < 0.0f) {
            return 0.0f;
        }
        int i = this.mRepeatCount;
        if (i != -1) {
            return Math.min(fraction, (float) (i + 1));
        }
        return fraction;
    }

    private boolean shouldPlayBackward(int iteration, boolean inReverse) {
        if (iteration > 0 && this.mRepeatMode == 2) {
            int i = this.mRepeatCount;
            if (iteration < i + 1 || i == -1) {
                boolean z = false;
                if (inReverse) {
                    if (iteration % 2 == 0) {
                        z = true;
                    }
                    return z;
                }
                if (iteration % 2 != 0) {
                    z = true;
                }
                return z;
            }
        }
        return inReverse;
    }

    public long getCurrentPlayTime() {
        if (!this.mInitialized || (!this.mStarted && this.mSeekFraction < 0.0f)) {
            return 0;
        }
        float f = this.mSeekFraction;
        if (f >= 0.0f) {
            return (long) (((float) this.mDuration) * f);
        }
        f = resolveDurationScale();
        if (f == 0.0f) {
            f = 1.0f;
        }
        return (long) (((float) (AnimationUtils.currentAnimationTimeMillis() - this.mStartTime)) / f);
    }

    public long getStartDelay() {
        return this.mStartDelay;
    }

    public void setStartDelay(long startDelay) {
        if (startDelay < 0) {
            Log.w(TAG, "Start delay should always be non-negative");
            startDelay = 0;
        }
        this.mStartDelay = startDelay;
    }

    public static long getFrameDelay() {
        AnimationHandler.getInstance();
        return AnimationHandler.getFrameDelay();
    }

    public static void setFrameDelay(long frameDelay) {
        AnimationHandler.getInstance();
        AnimationHandler.setFrameDelay(frameDelay);
    }

    public Object getAnimatedValue() {
        PropertyValuesHolder[] propertyValuesHolderArr = this.mValues;
        if (propertyValuesHolderArr == null || propertyValuesHolderArr.length <= 0) {
            return null;
        }
        return propertyValuesHolderArr[0].getAnimatedValue();
    }

    public Object getAnimatedValue(String propertyName) {
        PropertyValuesHolder valuesHolder = (PropertyValuesHolder) this.mValuesMap.get(propertyName);
        if (valuesHolder != null) {
            return valuesHolder.getAnimatedValue();
        }
        return null;
    }

    public void setRepeatCount(int value) {
        this.mRepeatCount = value;
    }

    public int getRepeatCount() {
        return this.mRepeatCount;
    }

    public void setRepeatMode(int value) {
        this.mRepeatMode = value;
    }

    public int getRepeatMode() {
        return this.mRepeatMode;
    }

    public void addUpdateListener(AnimatorUpdateListener listener) {
        if (this.mUpdateListeners == null) {
            this.mUpdateListeners = new ArrayList();
        }
        this.mUpdateListeners.add(listener);
    }

    public void removeAllUpdateListeners() {
        ArrayList arrayList = this.mUpdateListeners;
        if (arrayList != null) {
            arrayList.clear();
            this.mUpdateListeners = null;
        }
    }

    public void removeUpdateListener(AnimatorUpdateListener listener) {
        ArrayList arrayList = this.mUpdateListeners;
        if (arrayList != null) {
            arrayList.remove(listener);
            if (this.mUpdateListeners.size() == 0) {
                this.mUpdateListeners = null;
            }
        }
    }

    public void setInterpolator(TimeInterpolator value) {
        if (value != null) {
            this.mInterpolator = value;
        } else {
            this.mInterpolator = new LinearInterpolator();
        }
    }

    public TimeInterpolator getInterpolator() {
        return this.mInterpolator;
    }

    public void setEvaluator(TypeEvaluator value) {
        if (value != null) {
            PropertyValuesHolder[] propertyValuesHolderArr = this.mValues;
            if (propertyValuesHolderArr != null && propertyValuesHolderArr.length > 0) {
                propertyValuesHolderArr[0].setEvaluator(value);
            }
        }
    }

    private void notifyStartListeners() {
        if (!(this.mListeners == null || this.mStartListenersCalled)) {
            ArrayList<AnimatorListener> tmpListeners = (ArrayList) this.mListeners.clone();
            int numListeners = tmpListeners.size();
            for (int i = 0; i < numListeners; i++) {
                ((AnimatorListener) tmpListeners.get(i)).onAnimationStart(this, this.mReversing);
            }
        }
        this.mStartListenersCalled = true;
    }

    private void start(boolean playBackwards) {
        if (Looper.myLooper() != null) {
            this.mReversing = playBackwards;
            this.mSelfPulse = this.mSuppressSelfPulseRequested ^ 1;
            if (playBackwards) {
                float f = this.mSeekFraction;
                if (!(f == -1.0f || f == 0.0f)) {
                    int i = this.mRepeatCount;
                    if (i == -1) {
                        this.mSeekFraction = 1.0f - ((float) (((double) f) - Math.floor((double) f)));
                    } else {
                        this.mSeekFraction = ((float) (i + 1)) - f;
                    }
                }
            }
            this.mStarted = true;
            this.mPaused = false;
            this.mRunning = false;
            this.mAnimationEndRequested = false;
            this.mLastFrameTime = -1;
            this.mFirstFrameTime = -1;
            this.mStartTime = -1;
            addAnimationCallback(0);
            if (this.mStartDelay == 0 || this.mSeekFraction >= 0.0f || this.mReversing) {
                startAnimation();
                float f2 = this.mSeekFraction;
                if (f2 == -1.0f) {
                    setCurrentPlayTime(0);
                    return;
                } else {
                    setCurrentFraction(f2);
                    return;
                }
            }
            return;
        }
        throw new AndroidRuntimeException("Animators may only be run on Looper threads");
    }

    /* Access modifiers changed, original: 0000 */
    public void startWithoutPulsing(boolean inReverse) {
        this.mSuppressSelfPulseRequested = true;
        if (inReverse) {
            reverse();
        } else {
            start();
        }
        this.mSuppressSelfPulseRequested = false;
    }

    public void start() {
        start(false);
    }

    public void cancel() {
        if (Looper.myLooper() == null) {
            throw new AndroidRuntimeException("Animators may only be run on Looper threads");
        } else if (!this.mAnimationEndRequested) {
            if ((this.mStarted || this.mRunning) && this.mListeners != null) {
                if (!this.mRunning) {
                    notifyStartListeners();
                }
                Iterator it = ((ArrayList) this.mListeners.clone()).iterator();
                while (it.hasNext()) {
                    ((AnimatorListener) it.next()).onAnimationCancel(this);
                }
            }
            endAnimation();
        }
    }

    public void end() {
        if (Looper.myLooper() != null) {
            if (!this.mRunning) {
                startAnimation();
                this.mStarted = true;
            } else if (!this.mInitialized) {
                initAnimation();
            }
            animateValue(shouldPlayBackward(this.mRepeatCount, this.mReversing) ? 0.0f : 1.0f);
            endAnimation();
            return;
        }
        throw new AndroidRuntimeException("Animators may only be run on Looper threads");
    }

    public void resume() {
        if (Looper.myLooper() != null) {
            if (this.mPaused && !this.mResumed) {
                this.mResumed = true;
                if (this.mPauseTime > 0) {
                    addAnimationCallback(0);
                }
            }
            super.resume();
            return;
        }
        throw new AndroidRuntimeException("Animators may only be resumed from the same thread that the animator was started on");
    }

    public void pause() {
        boolean previouslyPaused = this.mPaused;
        super.pause();
        if (!previouslyPaused && this.mPaused) {
            this.mPauseTime = -1;
            this.mResumed = false;
        }
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    public boolean isStarted() {
        return this.mStarted;
    }

    public void reverse() {
        if (isPulsingInternal()) {
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStartTime = currentTime - (getScaledDuration() - (currentTime - this.mStartTime));
            this.mStartTimeCommitted = true;
            this.mReversing ^= 1;
        } else if (this.mStarted) {
            this.mReversing ^= 1;
            end();
        } else {
            start(true);
        }
    }

    public boolean canReverse() {
        return true;
    }

    private void endAnimation() {
        if (!this.mAnimationEndRequested) {
            removeAnimationCallback();
            boolean notify = true;
            this.mAnimationEndRequested = true;
            this.mPaused = false;
            if (!(this.mStarted || this.mRunning) || this.mListeners == null) {
                notify = false;
            }
            if (notify && !this.mRunning) {
                notifyStartListeners();
            }
            this.mRunning = false;
            this.mStarted = false;
            this.mStartListenersCalled = false;
            this.mLastFrameTime = -1;
            this.mFirstFrameTime = -1;
            this.mStartTime = -1;
            if (notify && this.mListeners != null) {
                ArrayList<AnimatorListener> tmpListeners = (ArrayList) this.mListeners.clone();
                int numListeners = tmpListeners.size();
                for (int i = 0; i < numListeners; i++) {
                    ((AnimatorListener) tmpListeners.get(i)).onAnimationEnd(this, this.mReversing);
                }
            }
            this.mReversing = false;
            if (Trace.isTagEnabled(8)) {
                Trace.asyncTraceEnd(8, getNameForTrace(), System.identityHashCode(this));
            }
        }
    }

    private void startAnimation() {
        if (Trace.isTagEnabled(8)) {
            Trace.asyncTraceBegin(8, getNameForTrace(), System.identityHashCode(this));
        }
        this.mAnimationEndRequested = false;
        initAnimation();
        this.mRunning = true;
        float f = this.mSeekFraction;
        if (f >= 0.0f) {
            this.mOverallFraction = f;
        } else {
            this.mOverallFraction = 0.0f;
        }
        if (this.mListeners != null) {
            notifyStartListeners();
        }
    }

    private boolean isPulsingInternal() {
        return this.mLastFrameTime >= 0;
    }

    /* Access modifiers changed, original: 0000 */
    public String getNameForTrace() {
        return "animator";
    }

    public void commitAnimationFrame(long frameTime) {
        if (!this.mStartTimeCommitted) {
            this.mStartTimeCommitted = true;
            long adjustment = frameTime - this.mLastFrameTime;
            if (adjustment > 0) {
                this.mStartTime += adjustment;
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean animateBasedOnTime(long currentTime) {
        boolean done = false;
        if (this.mRunning) {
            long scaledDuration = getScaledDuration();
            float fraction = scaledDuration > 0 ? ((float) (currentTime - this.mStartTime)) / ((float) scaledDuration) : 1.0f;
            boolean z = false;
            boolean newIteration = ((int) fraction) > ((int) this.mOverallFraction);
            int i = this.mRepeatCount;
            if (fraction >= ((float) (i + 1)) && i != -1) {
                z = true;
            }
            boolean lastIterationFinished = z;
            if (scaledDuration == 0) {
                done = true;
            } else if (!newIteration || lastIterationFinished) {
                if (lastIterationFinished) {
                    done = true;
                }
            } else if (this.mListeners != null) {
                int numListeners = this.mListeners.size();
                for (int i2 = 0; i2 < numListeners; i2++) {
                    ((AnimatorListener) this.mListeners.get(i2)).onAnimationRepeat(this);
                }
            }
            this.mOverallFraction = clampFraction(fraction);
            animateValue(getCurrentIterationFraction(this.mOverallFraction, this.mReversing));
        }
        return done;
    }

    /* Access modifiers changed, original: 0000 */
    public void animateBasedOnPlayTime(long currentPlayTime, long lastPlayTime, boolean inReverse) {
        if (currentPlayTime < 0 || lastPlayTime < 0) {
            throw new UnsupportedOperationException("Error: Play time should never be negative.");
        }
        initAnimation();
        int iteration = this.mRepeatCount;
        if (iteration > 0) {
            long j = this.mDuration;
            if (!(Math.min((int) (currentPlayTime / j), iteration) == Math.min((int) (lastPlayTime / j), this.mRepeatCount) || this.mListeners == null)) {
                int numListeners = this.mListeners.size();
                for (int i = 0; i < numListeners; i++) {
                    ((AnimatorListener) this.mListeners.get(i)).onAnimationRepeat(this);
                }
            }
        }
        iteration = this.mRepeatCount;
        if (iteration == -1 || currentPlayTime < ((long) (iteration + 1)) * this.mDuration) {
            animateValue(getCurrentIterationFraction(((float) currentPlayTime) / ((float) this.mDuration), inReverse));
        } else {
            skipToEndValue(inReverse);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void skipToEndValue(boolean inReverse) {
        initAnimation();
        float endFraction = inReverse ? 0.0f : 1.0f;
        if (this.mRepeatCount % 2 == 1 && this.mRepeatMode == 2) {
            endFraction = 0.0f;
        }
        animateValue(endFraction);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isInitialized() {
        return this.mInitialized;
    }

    public final boolean doAnimationFrame(long frameTime) {
        if (this.mStartTime < 0) {
            long j;
            if (this.mReversing) {
                j = frameTime;
            } else {
                j = ((long) (((float) this.mStartDelay) * resolveDurationScale())) + frameTime;
            }
            this.mStartTime = j;
        }
        if (this.mPaused) {
            this.mPauseTime = frameTime;
            removeAnimationCallback();
            return false;
        }
        if (this.mResumed) {
            this.mResumed = false;
            long j2 = this.mPauseTime;
            if (j2 > 0) {
                this.mStartTime += frameTime - j2;
            }
        }
        if (!this.mRunning) {
            if (this.mStartTime > frameTime && this.mSeekFraction == -1.0f) {
                return false;
            }
            this.mRunning = true;
            startAnimation();
        }
        if (this.mLastFrameTime < 0) {
            if (this.mSeekFraction >= 0.0f) {
                this.mStartTime = frameTime - ((long) (((float) getScaledDuration()) * this.mSeekFraction));
                this.mSeekFraction = -1.0f;
            }
            this.mStartTimeCommitted = false;
        }
        this.mLastFrameTime = frameTime;
        boolean finished = animateBasedOnTime(Math.max(frameTime, this.mStartTime));
        if (finished) {
            endAnimation();
        }
        return finished;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean pulseAnimationFrame(long frameTime) {
        if (this.mSelfPulse) {
            return false;
        }
        return doAnimationFrame(frameTime);
    }

    private void addOneShotCommitCallback() {
        if (this.mSelfPulse) {
            getAnimationHandler().addOneShotCommitCallback(this);
        }
    }

    private void removeAnimationCallback() {
        if (this.mSelfPulse) {
            getAnimationHandler().removeCallback(this);
        }
    }

    private void addAnimationCallback(long delay) {
        if (this.mSelfPulse) {
            getAnimationHandler().addAnimationFrameCallback(this, delay);
        }
    }

    public float getAnimatedFraction() {
        return this.mCurrentFraction;
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void animateValue(float fraction) {
        fraction = this.mInterpolator.getInterpolation(fraction);
        this.mCurrentFraction = fraction;
        for (PropertyValuesHolder calculateValue : this.mValues) {
            calculateValue.calculateValue(fraction);
        }
        int i = this.mUpdateListeners;
        if (i != 0) {
            i = i.size();
            for (int i2 = 0; i2 < i; i2++) {
                ((AnimatorUpdateListener) this.mUpdateListeners.get(i2)).onAnimationUpdate(this);
            }
        }
    }

    public ValueAnimator clone() {
        ValueAnimator anim = (ValueAnimator) super.clone();
        ArrayList arrayList = this.mUpdateListeners;
        if (arrayList != null) {
            anim.mUpdateListeners = new ArrayList(arrayList);
        }
        anim.mSeekFraction = -1.0f;
        anim.mReversing = false;
        anim.mInitialized = false;
        anim.mStarted = false;
        anim.mRunning = false;
        anim.mPaused = false;
        anim.mResumed = false;
        anim.mStartListenersCalled = false;
        anim.mStartTime = -1;
        anim.mStartTimeCommitted = false;
        anim.mAnimationEndRequested = false;
        anim.mPauseTime = -1;
        anim.mLastFrameTime = -1;
        anim.mFirstFrameTime = -1;
        anim.mOverallFraction = 0.0f;
        anim.mCurrentFraction = 0.0f;
        anim.mSelfPulse = true;
        anim.mSuppressSelfPulseRequested = false;
        PropertyValuesHolder[] oldValues = this.mValues;
        if (oldValues != null) {
            int numValues = oldValues.length;
            anim.mValues = new PropertyValuesHolder[numValues];
            anim.mValuesMap = new HashMap(numValues);
            for (int i = 0; i < numValues; i++) {
                PropertyValuesHolder newValuesHolder = oldValues[i].clone();
                anim.mValues[i] = newValuesHolder;
                anim.mValuesMap.put(newValuesHolder.getPropertyName(), newValuesHolder);
            }
        }
        return anim;
    }

    public static int getCurrentAnimationsCount() {
        return AnimationHandler.getAnimationCount();
    }

    public String toString() {
        String returnVal = new StringBuilder();
        returnVal.append("ValueAnimator@");
        returnVal.append(Integer.toHexString(hashCode()));
        returnVal = returnVal.toString();
        if (this.mValues != null) {
            for (PropertyValuesHolder propertyValuesHolder : this.mValues) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(returnVal);
                stringBuilder.append("\n    ");
                stringBuilder.append(propertyValuesHolder.toString());
                returnVal = stringBuilder.toString();
            }
        }
        return returnVal;
    }

    public void setAllowRunningAsynchronously(boolean mayRunAsync) {
    }

    public AnimationHandler getAnimationHandler() {
        return AnimationHandler.getInstance();
    }
}
