package android.widget;

import android.content.Context;
import android.view.animation.AnimationUtils;
import com.miui.internal.dynamicanimation.animation.DynamicAnimation;
import com.miui.internal.dynamicanimation.animation.DynamicAnimation.OnAnimationUpdateListener;
import com.miui.internal.dynamicanimation.animation.FlingAnimation;
import com.miui.internal.dynamicanimation.animation.FloatValueHolder;
import com.miui.internal.dynamicanimation.animation.SpringAnimation;
import com.miui.internal.dynamicanimation.animation.SpringForce;
import java.lang.reflect.Field;

class DynamicOverScroller extends OverScroller {
    private static final float FLING_FRICTION = 0.4761905f;
    private static final float FLING_VELOCITY_SCALE = 1.05f;
    private static final float MAX_SPRING_INITIAL_VELOCITY = 4000.0f;
    private static final float MINIMAL_VISIBLE_CHANGE = 0.5f;
    private static final float SPRING_DAMPING_RATIO = 0.99f;
    private static final float SPRING_STIFFNESS = 200.0f;
    private static final Field mScrollerY = reflectField("mScrollerY");

    static class DynamicScroller extends SplineOverScroller {
        private FlingAnimation mFlingAnimation;
        private OverScrollHandler mHandler;
        private SpringAnimation mSpringAnimation = new SpringAnimation(this.mValue);
        private FloatValueHolder mValue = new FloatValueHolder();

        DynamicScroller(Context context) {
            super(context);
            this.mSpringAnimation.setSpring(new SpringForce());
            this.mSpringAnimation.setMinimumVisibleChange(0.5f);
            this.mSpringAnimation.getSpring().setDampingRatio(DynamicOverScroller.SPRING_DAMPING_RATIO);
            this.mSpringAnimation.getSpring().setStiffness(200.0f);
            this.mFlingAnimation = new FlingAnimation(this.mValue);
            this.mFlingAnimation.setMinimumVisibleChange(0.5f);
            this.mFlingAnimation.setFriction(DynamicOverScroller.FLING_FRICTION);
        }

        /* Access modifiers changed, original: 0000 */
        public void setFriction(float friction) {
            this.mFlingAnimation.setFriction(friction);
        }

        /* Access modifiers changed, original: 0000 */
        public void finish() {
            OverScrollLogger.debug("finish scroller");
            setCurrentPosition(getFinal());
            setFinished(true);
            resetHandler();
        }

        /* Access modifiers changed, original: 0000 */
        public void setFinalPosition(int position) {
            super.setFinalPosition(position);
        }

        /* Access modifiers changed, original: 0000 */
        public void extendDuration(int extend) {
            super.extendDuration(extend);
        }

        /* Access modifiers changed, original: 0000 */
        public boolean springback(int start, int min, int max) {
            OverScrollLogger.debug("SPRING_BACK start(%d) boundary(%d, %d)", Integer.valueOf(start), Integer.valueOf(min), Integer.valueOf(max));
            if (this.mHandler != null) {
                resetHandler();
            }
            if (start < min) {
                doSpring(1, start, 0.0f, min, 0);
            } else if (start > max) {
                doSpring(1, start, 0.0f, max, 0);
            } else {
                setCurrentPosition(start);
                setStart(start);
                setFinal(start);
                setDuration(0);
                setFinished(true);
            }
            return isFinished() ^ 1;
        }

        private void doSpring(int state, int start, float initialVelocity, int edge, int over) {
            if (initialVelocity > DynamicOverScroller.MAX_SPRING_INITIAL_VELOCITY) {
                OverScrollLogger.debug("%f is too fast for spring, slow down", Float.valueOf(initialVelocity));
                initialVelocity = DynamicOverScroller.MAX_SPRING_INITIAL_VELOCITY;
            }
            setFinished(false);
            setCurrVelocity(initialVelocity);
            setStartTime(AnimationUtils.currentAnimationTimeMillis());
            setCurrentPosition(start);
            setStart(start);
            setDuration(Integer.MAX_VALUE);
            setFinal(edge);
            setState(state);
            this.mHandler = new OverScrollHandler(this.mSpringAnimation, start, initialVelocity);
            this.mSpringAnimation.getSpring().setFinalPosition((float) this.mHandler.getOffset(edge));
            if (over != 0) {
                if (initialVelocity < 0.0f) {
                    this.mHandler.setMinValue(edge - over);
                    this.mHandler.setMaxValue(Math.max(edge, start));
                } else {
                    this.mHandler.setMinValue(Math.min(edge, start));
                    this.mHandler.setMaxValue(edge + over);
                }
            }
            this.mHandler.start();
        }

        /* Access modifiers changed, original: 0000 */
        public void fling(int start, int velocity, int min, int max, int over) {
            velocity = (int) (((float) velocity) * DynamicOverScroller.FLING_VELOCITY_SCALE);
            OverScrollLogger.debug("FLING: start(%d) velocity(%d) boundary(%d, %d) over(%d)", Integer.valueOf(start), Integer.valueOf(velocity), Integer.valueOf(min), Integer.valueOf(max), Integer.valueOf(over));
            resetHandler();
            if (velocity == 0) {
                setCurrentPosition(start);
                setStart(start);
                setFinal(start);
                setDuration(0);
                setFinished(true);
            } else if (start > max || start < min) {
                startAfterEdge(start, min, max, velocity, over);
            } else {
                doFling(start, velocity, min, max, over);
            }
        }

        private void doFling(int start, int velocity, final int min, final int max, final int over) {
            int fixedFinal;
            int duration;
            this.mFlingAnimation.setStartValue(0.0f);
            this.mFlingAnimation.setStartVelocity((float) velocity);
            long unlimitedFinal = ((long) start) + ((long) this.mFlingAnimation.predictNaturalDest());
            if (unlimitedFinal > ((long) max)) {
                fixedFinal = max;
                duration = (int) this.mFlingAnimation.predictTimeTo((float) (max - start));
            } else if (unlimitedFinal < ((long) min)) {
                fixedFinal = min;
                duration = (int) this.mFlingAnimation.predictTimeTo((float) (min - start));
            } else {
                fixedFinal = (int) unlimitedFinal;
                duration = (int) this.mFlingAnimation.predictDuration();
            }
            setFinished(false);
            setCurrVelocity((float) velocity);
            setStartTime(AnimationUtils.currentAnimationTimeMillis());
            setCurrentPosition(start);
            setStart(start);
            setDuration(duration);
            setFinal(fixedFinal);
            setState(0);
            int animMin = Math.min(min, start);
            int animMax = Math.max(max, start);
            this.mHandler = new OverScrollHandler(this.mFlingAnimation, start, (float) velocity);
            this.mHandler.setOnFinishedListener(new OnFinishedListener() {
                public boolean whenFinished(float value, float velocity) {
                    OverScrollLogger.debug("fling finished: value(%f), velocity(%f), scroller boundary(%d, %d)", Float.valueOf(value), Float.valueOf(velocity), Integer.valueOf(min), Integer.valueOf(max));
                    DynamicScroller.this.mFlingAnimation.setStartValue((float) DynamicScroller.this.mHandler.mValue);
                    DynamicScroller.this.mFlingAnimation.setStartVelocity(DynamicScroller.this.mHandler.mVelocity);
                    float predictValue = DynamicScroller.this.mFlingAnimation.predictNaturalDest();
                    if (((int) value) == 0 || (predictValue <= ((float) max) && predictValue >= ((float) min))) {
                        OverScrollLogger.debug("fling finished, no more work.");
                        return false;
                    }
                    OverScrollLogger.debug("fling destination beyound boundary, start spring");
                    DynamicScroller.this.resetHandler();
                    DynamicScroller dynamicScroller = DynamicScroller.this;
                    dynamicScroller.doSpring(2, dynamicScroller.getCurrentPosition(), DynamicScroller.this.getCurrVelocity(), DynamicScroller.this.getFinal(), over);
                    return true;
                }
            });
            this.mHandler.setMinValue(animMin);
            this.mHandler.setMaxValue(animMax);
            this.mHandler.start();
        }

        private void resetHandler() {
            if (this.mHandler != null) {
                OverScrollLogger.debug("resetting current handler: state(%d), anim(%s), value(%d), velocity(%f)", Integer.valueOf(getState()), this.mHandler.getAnimation().getClass().getSimpleName(), Integer.valueOf(this.mHandler.mValue), Float.valueOf(this.mHandler.mVelocity));
                this.mHandler.cancel();
                this.mHandler = null;
            }
        }

        private void startAfterEdge(int start, int min, int max, int velocity, int over) {
            int i = start;
            int i2 = min;
            int i3 = max;
            int i4 = velocity;
            r0 = new Object[5];
            boolean z = false;
            r0[0] = Integer.valueOf(start);
            r0[1] = Integer.valueOf(velocity);
            r0[2] = Integer.valueOf(min);
            r0[3] = Integer.valueOf(max);
            r0[4] = Integer.valueOf(over);
            OverScrollLogger.debug("startAfterEdge: start(%d) velocity(%d) boundary(%d, %d) over(%d)", r0);
            if (i <= i2 || i >= i3) {
                boolean positive = i > i3;
                int edge = positive ? i3 : i2;
                int overDistance = i - edge;
                if (i4 != 0 && Integer.signum(overDistance) * i4 >= 0) {
                    z = true;
                }
                if (z) {
                    OverScrollLogger.debug("spring forward");
                    doSpring(2, start, (float) i4, edge, over);
                } else {
                    this.mFlingAnimation.setStartValue((float) i);
                    this.mFlingAnimation.setStartVelocity((float) i4);
                    float predictDest = this.mFlingAnimation.predictNaturalDest();
                    if ((!positive || predictDest >= ((float) i3)) && (positive || predictDest <= ((float) i2))) {
                        OverScrollLogger.debug("spring backward");
                        doSpring(1, start, (float) i4, edge, over);
                    } else {
                        OverScrollLogger.debug("fling to content");
                        doFling(start, velocity, min, max, over);
                    }
                }
                return;
            }
            setFinished(true);
        }

        /* Access modifiers changed, original: 0000 */
        public void notifyEdgeReached(int start, int end, int over) {
            if (getState() == 0) {
                if (this.mHandler != null) {
                    resetHandler();
                }
                startAfterEdge(start, end, end, (int) getCurrVelocity(), over);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public boolean continueWhenFinished() {
            OverScrollHandler overScrollHandler = this.mHandler;
            if (overScrollHandler == null || !overScrollHandler.continueWhenFinished()) {
                return false;
            }
            OverScrollLogger.debug("checking have more work when finish");
            update();
            return true;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean update() {
            boolean finished = this.mHandler;
            if (finished) {
                finished = finished.update();
                setCurrentPosition(this.mHandler.mValue);
                setCurrVelocity(this.mHandler.mVelocity);
                if (getState() == 2 && Math.signum((float) this.mHandler.mValue) * Math.signum(this.mHandler.mVelocity) < 0.0f) {
                    OverScrollLogger.debug("State Changed: BALLISTIC -> CUBIC");
                    setState(1);
                }
                return finished ^ 1;
            }
            OverScrollLogger.debug("no handler found, aborting");
            return false;
        }
    }

    private static class OverScrollHandler {
        private float mAnimMaxValue;
        private float mAnimMinValue;
        DynamicAnimation<?> mAnimation;
        private long mLastUpdateTime;
        private final int mMaxLegalValue;
        private final int mMinLegalValue;
        private Monitor mMonitor = new Monitor();
        private OnFinishedListener mOnFinishedListener;
        int mStartValue;
        int mValue;
        float mVelocity;

        interface OnFinishedListener {
            boolean whenFinished(float f, float f2);
        }

        private class Monitor implements OnAnimationUpdateListener {
            private Monitor() {
            }

            public void onAnimationUpdate(DynamicAnimation animation, float value, float velocity) {
                OverScrollHandler overScrollHandler = OverScrollHandler.this;
                overScrollHandler.mVelocity = velocity;
                overScrollHandler.mValue = overScrollHandler.mStartValue + ((int) value);
                OverScrollLogger.verbose("%s updating value(%f), velocity(%f), min(%f), max(%f)", animation.getClass().getSimpleName(), Float.valueOf(value), Float.valueOf(velocity), Float.valueOf(OverScrollHandler.this.mAnimMinValue), Float.valueOf(OverScrollHandler.this.mAnimMaxValue));
            }
        }

        OverScrollHandler(DynamicAnimation<?> animation, int startValue, float velocity) {
            int minLegalValue;
            int maxLegalValue;
            this.mAnimation = animation;
            this.mAnimation.setMinValue(-3.4028235E38f);
            this.mAnimation.setMaxValue(Float.MAX_VALUE);
            this.mStartValue = startValue;
            this.mVelocity = velocity;
            if (startValue > 0) {
                minLegalValue = Integer.MIN_VALUE + startValue;
                maxLegalValue = Integer.MAX_VALUE;
            } else if (startValue < 0) {
                minLegalValue = Integer.MIN_VALUE;
                maxLegalValue = Integer.MAX_VALUE + startValue;
            } else {
                minLegalValue = Integer.MIN_VALUE;
                maxLegalValue = Integer.MAX_VALUE;
            }
            this.mMinLegalValue = minLegalValue;
            this.mMaxLegalValue = maxLegalValue;
            this.mAnimation.setStartValue(0.0f);
            this.mAnimation.setStartVelocity(velocity);
        }

        /* Access modifiers changed, original: 0000 */
        public DynamicAnimation<?> getAnimation() {
            return this.mAnimation;
        }

        /* Access modifiers changed, original: 0000 */
        public int getOffset(int raw) {
            return raw - this.mStartValue;
        }

        /* Access modifiers changed, original: 0000 */
        public void setMinValue(int min) {
            if (min < this.mMinLegalValue) {
                min = this.mMinLegalValue;
            }
            float animMinValue = (float) (min - this.mStartValue);
            this.mAnimation.setMinValue(animMinValue);
            this.mAnimMinValue = animMinValue;
        }

        /* Access modifiers changed, original: 0000 */
        public void setMaxValue(int max) {
            if (max > this.mMaxLegalValue) {
                max = this.mMaxLegalValue;
            }
            float animMaxValue = (float) (max - this.mStartValue);
            this.mAnimation.setMaxValue(animMaxValue);
            this.mAnimMaxValue = animMaxValue;
        }

        /* Access modifiers changed, original: 0000 */
        public void setOnFinishedListener(OnFinishedListener handler) {
            this.mOnFinishedListener = handler;
        }

        /* Access modifiers changed, original: 0000 */
        public void start() {
            this.mAnimation.addUpdateListener(this.mMonitor);
            this.mAnimation.start(true);
            this.mLastUpdateTime = 0;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean update() {
            long lastUpdateTime = this.mLastUpdateTime;
            long currentFrameTime = AnimationUtils.currentAnimationTimeMillis();
            if (currentFrameTime == lastUpdateTime) {
                OverScrollLogger.verbose("update done in this frame, dropping current update request");
                return this.mAnimation.isRunning() ^ 1;
            }
            boolean finish = this.mAnimation.doAnimationFrame(currentFrameTime);
            if (finish) {
                OverScrollLogger.verbose("%s finishing value(%d) velocity(%f)", this.mAnimation.getClass().getSimpleName(), Integer.valueOf(this.mValue), Float.valueOf(this.mVelocity));
                this.mAnimation.removeUpdateListener(this.mMonitor);
                this.mLastUpdateTime = 0;
            }
            this.mLastUpdateTime = currentFrameTime;
            return finish;
        }

        /* Access modifiers changed, original: 0000 */
        public void cancel() {
            this.mLastUpdateTime = 0;
            this.mAnimation.cancel();
            this.mAnimation.removeUpdateListener(this.mMonitor);
        }

        /* Access modifiers changed, original: 0000 */
        public boolean continueWhenFinished() {
            OnFinishedListener onFinishedListener = this.mOnFinishedListener;
            if (onFinishedListener != null) {
                return onFinishedListener.whenFinished((float) this.mValue, this.mVelocity);
            }
            return false;
        }
    }

    DynamicOverScroller(Context context) {
        super(context);
        replaceScroller(mScrollerY, new DynamicScroller(context));
    }

    private void replaceScroller(Field field, SplineOverScroller scroller) {
        try {
            field.set(this, scroller);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Field reflectField(String fieldName) {
        try {
            Field field = OverScroller.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
