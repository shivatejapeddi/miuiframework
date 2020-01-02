package com.android.internal.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.AbsListView;

public abstract class AutoScrollHelper implements OnTouchListener {
    private static final int DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    private static final int DEFAULT_EDGE_TYPE = 1;
    private static final float DEFAULT_MAXIMUM_EDGE = Float.MAX_VALUE;
    private static final int DEFAULT_MAXIMUM_VELOCITY_DIPS = 1575;
    private static final int DEFAULT_MINIMUM_VELOCITY_DIPS = 315;
    private static final int DEFAULT_RAMP_DOWN_DURATION = 500;
    private static final int DEFAULT_RAMP_UP_DURATION = 500;
    private static final float DEFAULT_RELATIVE_EDGE = 0.2f;
    private static final float DEFAULT_RELATIVE_VELOCITY = 1.0f;
    public static final int EDGE_TYPE_INSIDE = 0;
    public static final int EDGE_TYPE_INSIDE_EXTEND = 1;
    public static final int EDGE_TYPE_OUTSIDE = 2;
    private static final int HORIZONTAL = 0;
    public static final float NO_MAX = Float.MAX_VALUE;
    public static final float NO_MIN = 0.0f;
    public static final float RELATIVE_UNSPECIFIED = 0.0f;
    private static final int VERTICAL = 1;
    private int mActivationDelay;
    private boolean mAlreadyDelayed;
    private boolean mAnimating;
    private final Interpolator mEdgeInterpolator = new AccelerateInterpolator();
    private int mEdgeType;
    private boolean mEnabled;
    private boolean mExclusive;
    private float[] mMaximumEdges = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
    private float[] mMaximumVelocity = new float[]{Float.MAX_VALUE, Float.MAX_VALUE};
    private float[] mMinimumVelocity = new float[]{0.0f, 0.0f};
    private boolean mNeedsCancel;
    private boolean mNeedsReset;
    private float[] mRelativeEdges = new float[]{0.0f, 0.0f};
    private float[] mRelativeVelocity = new float[]{0.0f, 0.0f};
    private Runnable mRunnable;
    private final ClampedScroller mScroller = new ClampedScroller();
    private final View mTarget;

    public static class AbsListViewAutoScroller extends AutoScrollHelper {
        private final AbsListView mTarget;

        public AbsListViewAutoScroller(AbsListView target) {
            super(target);
            this.mTarget = target;
        }

        public void scrollTargetBy(int deltaX, int deltaY) {
            this.mTarget.scrollListBy(deltaY);
        }

        public boolean canTargetScrollHorizontally(int direction) {
            return false;
        }

        public boolean canTargetScrollVertically(int direction) {
            AbsListView target = this.mTarget;
            int itemCount = target.getCount();
            if (itemCount == 0) {
                return false;
            }
            int childCount = target.getChildCount();
            int firstPosition = target.getFirstVisiblePosition();
            int lastPosition = firstPosition + childCount;
            if (direction > 0) {
                if (lastPosition >= itemCount && target.getChildAt(childCount - 1).getBottom() <= target.getHeight()) {
                    return false;
                }
            } else if (direction >= 0) {
                return false;
            } else {
                if (firstPosition <= 0 && target.getChildAt(0).getTop() >= 0) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class ClampedScroller {
        private long mDeltaTime = 0;
        private int mDeltaX = 0;
        private int mDeltaY = 0;
        private int mEffectiveRampDown;
        private int mRampDownDuration;
        private int mRampUpDuration;
        private long mStartTime = Long.MIN_VALUE;
        private long mStopTime = -1;
        private float mStopValue;
        private float mTargetVelocityX;
        private float mTargetVelocityY;

        public void setRampUpDuration(int durationMillis) {
            this.mRampUpDuration = durationMillis;
        }

        public void setRampDownDuration(int durationMillis) {
            this.mRampDownDuration = durationMillis;
        }

        public void start() {
            this.mStartTime = AnimationUtils.currentAnimationTimeMillis();
            this.mStopTime = -1;
            this.mDeltaTime = this.mStartTime;
            this.mStopValue = 0.5f;
            this.mDeltaX = 0;
            this.mDeltaY = 0;
        }

        public void requestStop() {
            long currentTime = AnimationUtils.currentAnimationTimeMillis();
            this.mEffectiveRampDown = AutoScrollHelper.constrain((int) (currentTime - this.mStartTime), 0, this.mRampDownDuration);
            this.mStopValue = getValueAt(currentTime);
            this.mStopTime = currentTime;
        }

        public boolean isFinished() {
            return this.mStopTime > 0 && AnimationUtils.currentAnimationTimeMillis() > this.mStopTime + ((long) this.mEffectiveRampDown);
        }

        private float getValueAt(long currentTime) {
            if (currentTime < this.mStartTime) {
                return 0.0f;
            }
            long elapsedSinceEnd = this.mStopTime;
            if (elapsedSinceEnd < 0 || currentTime < elapsedSinceEnd) {
                return AutoScrollHelper.constrain(((float) (currentTime - this.mStartTime)) / ((float) this.mRampUpDuration), 0.0f, 1.0f) * 0.5f;
            }
            elapsedSinceEnd = currentTime - elapsedSinceEnd;
            float f = this.mStopValue;
            return (1.0f - f) + (f * AutoScrollHelper.constrain(((float) elapsedSinceEnd) / ((float) this.mEffectiveRampDown), 0.0f, 1.0f));
        }

        private float interpolateValue(float value) {
            return ((-4.0f * value) * value) + (4.0f * value);
        }

        public void computeScrollDelta() {
            if (this.mDeltaTime != 0) {
                long currentTime = AnimationUtils.currentAnimationTimeMillis();
                float scale = interpolateValue(getValueAt(currentTime));
                long elapsedSinceDelta = currentTime - this.mDeltaTime;
                this.mDeltaTime = currentTime;
                this.mDeltaX = (int) ((((float) elapsedSinceDelta) * scale) * this.mTargetVelocityX);
                this.mDeltaY = (int) ((((float) elapsedSinceDelta) * scale) * this.mTargetVelocityY);
                return;
            }
            throw new RuntimeException("Cannot compute scroll delta before calling start()");
        }

        public void setTargetVelocity(float x, float y) {
            this.mTargetVelocityX = x;
            this.mTargetVelocityY = y;
        }

        public int getHorizontalDirection() {
            float f = this.mTargetVelocityX;
            return (int) (f / Math.abs(f));
        }

        public int getVerticalDirection() {
            float f = this.mTargetVelocityY;
            return (int) (f / Math.abs(f));
        }

        public int getDeltaX() {
            return this.mDeltaX;
        }

        public int getDeltaY() {
            return this.mDeltaY;
        }
    }

    private class ScrollAnimationRunnable implements Runnable {
        private ScrollAnimationRunnable() {
        }

        public void run() {
            if (AutoScrollHelper.this.mAnimating) {
                if (AutoScrollHelper.this.mNeedsReset) {
                    AutoScrollHelper.this.mNeedsReset = false;
                    AutoScrollHelper.this.mScroller.start();
                }
                ClampedScroller scroller = AutoScrollHelper.this.mScroller;
                if (scroller.isFinished() || !AutoScrollHelper.this.shouldAnimate()) {
                    AutoScrollHelper.this.mAnimating = false;
                    return;
                }
                if (AutoScrollHelper.this.mNeedsCancel) {
                    AutoScrollHelper.this.mNeedsCancel = false;
                    AutoScrollHelper.this.cancelTargetTouch();
                }
                scroller.computeScrollDelta();
                AutoScrollHelper.this.scrollTargetBy(scroller.getDeltaX(), scroller.getDeltaY());
                AutoScrollHelper.this.mTarget.postOnAnimation(this);
            }
        }
    }

    public abstract boolean canTargetScrollHorizontally(int i);

    public abstract boolean canTargetScrollVertically(int i);

    public abstract void scrollTargetBy(int i, int i2);

    public AutoScrollHelper(View target) {
        this.mTarget = target;
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int maxVelocity = (int) ((metrics.density * 1575.0f) + 1056964608);
        int minVelocity = (int) ((metrics.density * 315.0f) + 0.5f);
        setMaximumVelocity((float) maxVelocity, (float) maxVelocity);
        setMinimumVelocity((float) minVelocity, (float) minVelocity);
        setEdgeType(1);
        setMaximumEdges(Float.MAX_VALUE, Float.MAX_VALUE);
        setRelativeEdges(0.2f, 0.2f);
        setRelativeVelocity(1.0f, 1.0f);
        setActivationDelay(DEFAULT_ACTIVATION_DELAY);
        setRampUpDuration(500);
        setRampDownDuration(500);
    }

    public AutoScrollHelper setEnabled(boolean enabled) {
        if (this.mEnabled && !enabled) {
            requestStop();
        }
        this.mEnabled = enabled;
        return this;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    public AutoScrollHelper setExclusive(boolean exclusive) {
        this.mExclusive = exclusive;
        return this;
    }

    public boolean isExclusive() {
        return this.mExclusive;
    }

    public AutoScrollHelper setMaximumVelocity(float horizontalMax, float verticalMax) {
        float[] fArr = this.mMaximumVelocity;
        fArr[0] = horizontalMax / 1000.0f;
        fArr[1] = verticalMax / 1000.0f;
        return this;
    }

    public AutoScrollHelper setMinimumVelocity(float horizontalMin, float verticalMin) {
        float[] fArr = this.mMinimumVelocity;
        fArr[0] = horizontalMin / 1000.0f;
        fArr[1] = verticalMin / 1000.0f;
        return this;
    }

    public AutoScrollHelper setRelativeVelocity(float horizontal, float vertical) {
        float[] fArr = this.mRelativeVelocity;
        fArr[0] = horizontal / 1000.0f;
        fArr[1] = vertical / 1000.0f;
        return this;
    }

    public AutoScrollHelper setEdgeType(int type) {
        this.mEdgeType = type;
        return this;
    }

    public AutoScrollHelper setRelativeEdges(float horizontal, float vertical) {
        float[] fArr = this.mRelativeEdges;
        fArr[0] = horizontal;
        fArr[1] = vertical;
        return this;
    }

    public AutoScrollHelper setMaximumEdges(float horizontalMax, float verticalMax) {
        float[] fArr = this.mMaximumEdges;
        fArr[0] = horizontalMax;
        fArr[1] = verticalMax;
        return this;
    }

    public AutoScrollHelper setActivationDelay(int delayMillis) {
        this.mActivationDelay = delayMillis;
        return this;
    }

    public AutoScrollHelper setRampUpDuration(int durationMillis) {
        this.mScroller.setRampUpDuration(durationMillis);
        return this;
    }

    public AutoScrollHelper setRampDownDuration(int durationMillis) {
        this.mScroller.setRampDownDuration(durationMillis);
        return this;
    }

    /* JADX WARNING: Missing block: B:9:0x0013, code skipped:
            if (r0 != 3) goto L_0x005a;
     */
    public boolean onTouch(android.view.View r8, android.view.MotionEvent r9) {
        /*
        r7 = this;
        r0 = r7.mEnabled;
        r1 = 0;
        if (r0 != 0) goto L_0x0006;
    L_0x0005:
        return r1;
    L_0x0006:
        r0 = r9.getActionMasked();
        r2 = 1;
        if (r0 == 0) goto L_0x001a;
    L_0x000d:
        if (r0 == r2) goto L_0x0016;
    L_0x000f:
        r3 = 2;
        if (r0 == r3) goto L_0x001e;
    L_0x0012:
        r3 = 3;
        if (r0 == r3) goto L_0x0016;
    L_0x0015:
        goto L_0x005a;
    L_0x0016:
        r7.requestStop();
        goto L_0x005a;
    L_0x001a:
        r7.mNeedsCancel = r2;
        r7.mAlreadyDelayed = r1;
        r3 = r9.getX();
        r4 = r8.getWidth();
        r4 = (float) r4;
        r5 = r7.mTarget;
        r5 = r5.getWidth();
        r5 = (float) r5;
        r3 = r7.computeTargetVelocity(r1, r3, r4, r5);
        r4 = r9.getY();
        r5 = r8.getHeight();
        r5 = (float) r5;
        r6 = r7.mTarget;
        r6 = r6.getHeight();
        r6 = (float) r6;
        r4 = r7.computeTargetVelocity(r2, r4, r5, r6);
        r5 = r7.mScroller;
        r5.setTargetVelocity(r3, r4);
        r5 = r7.mAnimating;
        if (r5 != 0) goto L_0x005a;
    L_0x0051:
        r5 = r7.shouldAnimate();
        if (r5 == 0) goto L_0x005a;
    L_0x0057:
        r7.startAnimating();
    L_0x005a:
        r3 = r7.mExclusive;
        if (r3 == 0) goto L_0x0063;
    L_0x005e:
        r3 = r7.mAnimating;
        if (r3 == 0) goto L_0x0063;
    L_0x0062:
        r1 = r2;
    L_0x0063:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.widget.AutoScrollHelper.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    private boolean shouldAnimate() {
        ClampedScroller scroller = this.mScroller;
        int verticalDirection = scroller.getVerticalDirection();
        int horizontalDirection = scroller.getHorizontalDirection();
        return (verticalDirection != 0 && canTargetScrollVertically(verticalDirection)) || (horizontalDirection != 0 && canTargetScrollHorizontally(horizontalDirection));
    }

    private void startAnimating() {
        if (this.mRunnable == null) {
            this.mRunnable = new ScrollAnimationRunnable();
        }
        this.mAnimating = true;
        this.mNeedsReset = true;
        if (!this.mAlreadyDelayed) {
            int i = this.mActivationDelay;
            if (i > 0) {
                this.mTarget.postOnAnimationDelayed(this.mRunnable, (long) i);
                this.mAlreadyDelayed = true;
            }
        }
        this.mRunnable.run();
        this.mAlreadyDelayed = true;
    }

    private void requestStop() {
        if (this.mNeedsReset) {
            this.mAnimating = false;
        } else {
            this.mScroller.requestStop();
        }
    }

    private float computeTargetVelocity(int direction, float coordinate, float srcSize, float dstSize) {
        float value = getEdgeValue(this.mRelativeEdges[direction], srcSize, this.mMaximumEdges[direction], coordinate);
        if (value == 0.0f) {
            return 0.0f;
        }
        float relativeVelocity = this.mRelativeVelocity[direction];
        float minimumVelocity = this.mMinimumVelocity[direction];
        float maximumVelocity = this.mMaximumVelocity[direction];
        float targetVelocity = relativeVelocity * dstSize;
        if (value > 0.0f) {
            return constrain(value * targetVelocity, minimumVelocity, maximumVelocity);
        }
        return -constrain((-value) * targetVelocity, minimumVelocity, maximumVelocity);
    }

    private float getEdgeValue(float relativeValue, float size, float maxValue, float current) {
        float interpolated;
        float edgeSize = constrain(relativeValue * size, 0.0f, maxValue);
        float value = constrainEdgeValue(size - current, edgeSize) - constrainEdgeValue(current, edgeSize);
        if (value < 0.0f) {
            interpolated = -this.mEdgeInterpolator.getInterpolation(-value);
        } else if (value <= 0.0f) {
            return 0.0f;
        } else {
            interpolated = this.mEdgeInterpolator.getInterpolation(value);
        }
        return constrain(interpolated, -1.0f, 1.0f);
    }

    private float constrainEdgeValue(float current, float leading) {
        if (leading == 0.0f) {
            return 0.0f;
        }
        int i = this.mEdgeType;
        if (i == 0 || i == 1) {
            if (current < leading) {
                if (current >= 0.0f) {
                    return 1.0f - (current / leading);
                }
                if (this.mAnimating && this.mEdgeType == 1) {
                    return 1.0f;
                }
                return 0.0f;
            }
        } else if (i == 2 && current < 0.0f) {
            return current / (-leading);
        }
        return 0.0f;
    }

    private static int constrain(int value, int min, int max) {
        if (value > max) {
            return max;
        }
        if (value < min) {
            return min;
        }
        return value;
    }

    private static float constrain(float value, float min, float max) {
        if (value > max) {
            return max;
        }
        if (value < min) {
            return min;
        }
        return value;
    }

    private void cancelTargetTouch() {
        long eventTime = SystemClock.uptimeMillis();
        MotionEvent cancel = MotionEvent.obtain(eventTime, eventTime, 3, 0.0f, 0.0f, 0);
        this.mTarget.onTouchEvent(cancel);
        cancel.recycle();
    }
}
