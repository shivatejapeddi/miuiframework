package com.miui.internal.dynamicanimation.animation;

import android.os.Looper;
import android.util.AndroidRuntimeException;

public final class SpringAnimation extends DynamicAnimation<SpringAnimation> {
    private static final float UNSET = Float.MAX_VALUE;
    private boolean mEndRequested = false;
    private float mPendingPosition = Float.MAX_VALUE;
    private SpringForce mSpring = null;

    public SpringAnimation(FloatValueHolder floatValueHolder) {
        super(floatValueHolder);
    }

    public <K> SpringAnimation(K object, FloatPropertyCompat<K> property) {
        super(object, property);
    }

    public <K> SpringAnimation(K object, FloatPropertyCompat<K> property, float finalPosition) {
        super(object, property);
        this.mSpring = new SpringForce(finalPosition);
    }

    public SpringForce getSpring() {
        return this.mSpring;
    }

    public SpringAnimation setSpring(SpringForce force) {
        this.mSpring = force;
        return this;
    }

    public void start(boolean manully) {
        sanityCheck();
        this.mSpring.setValueThreshold((double) getValueThreshold());
        super.start(manully);
    }

    public void animateToFinalPosition(float finalPosition) {
        if (isRunning()) {
            this.mPendingPosition = finalPosition;
            return;
        }
        if (this.mSpring == null) {
            this.mSpring = new SpringForce(finalPosition);
        }
        this.mSpring.setFinalPosition(finalPosition);
        start();
    }

    public void skipToEnd() {
        if (!canSkipToEnd()) {
            throw new UnsupportedOperationException("Spring animations can only come to an end when there is damping");
        } else if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be started on the main thread");
        } else if (this.mRunning) {
            this.mEndRequested = true;
        }
    }

    public boolean canSkipToEnd() {
        return this.mSpring.mDampingRatio > 0.0d;
    }

    private void sanityCheck() {
        SpringForce springForce = this.mSpring;
        if (springForce != null) {
            double finalPosition = (double) springForce.getFinalPosition();
            if (finalPosition > ((double) this.mMaxValue)) {
                throw new UnsupportedOperationException("Final position of the spring cannot be greater than the max value.");
            } else if (finalPosition < ((double) this.mMinValue)) {
                throw new UnsupportedOperationException("Final position of the spring cannot be less than the min value.");
            } else {
                return;
            }
        }
        throw new UnsupportedOperationException("Incomplete SpringAnimation: Either final position or a spring force needs to be set.");
    }

    /* Access modifiers changed, original: 0000 */
    public boolean updateValueAndVelocity(long deltaT) {
        if (this.mEndRequested) {
            float f = this.mPendingPosition;
            if (f != Float.MAX_VALUE) {
                this.mSpring.setFinalPosition(f);
                this.mPendingPosition = Float.MAX_VALUE;
            }
            this.mValue = this.mSpring.getFinalPosition();
            this.mVelocity = 0.0f;
            this.mEndRequested = false;
            return true;
        }
        MassState massState;
        if (this.mPendingPosition != Float.MAX_VALUE) {
            double lastPosition = (double) this.mSpring.getFinalPosition();
            massState = this.mSpring.updateValues((double) this.mValue, (double) this.mVelocity, deltaT / 2);
            this.mSpring.setFinalPosition(this.mPendingPosition);
            this.mPendingPosition = Float.MAX_VALUE;
            massState = this.mSpring.updateValues((double) massState.mValue, (double) massState.mVelocity, deltaT / 2);
            this.mValue = massState.mValue;
            this.mVelocity = massState.mVelocity;
        } else {
            massState = this.mSpring.updateValues((double) this.mValue, (double) this.mVelocity, deltaT);
            this.mValue = massState.mValue;
            this.mVelocity = massState.mVelocity;
        }
        this.mValue = Math.max(this.mValue, this.mMinValue);
        this.mValue = Math.min(this.mValue, this.mMaxValue);
        if (!isAtEquilibrium(this.mValue, this.mVelocity)) {
            return false;
        }
        this.mValue = this.mSpring.getFinalPosition();
        this.mVelocity = 0.0f;
        return true;
    }

    /* Access modifiers changed, original: 0000 */
    public float getAcceleration(float value, float velocity) {
        return this.mSpring.getAcceleration(value, velocity);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isAtEquilibrium(float value, float velocity) {
        return this.mSpring.isAtEquilibrium(value, velocity);
    }

    /* Access modifiers changed, original: 0000 */
    public void setValueThreshold(float threshold) {
    }
}
