package com.miui.internal.dynamicanimation.animation;

public final class FlingAnimation extends DynamicAnimation<FlingAnimation> {
    private final DragForce mFlingForce = new DragForce();

    static final class DragForce implements Force {
        private static final float DEFAULT_FRICTION = -4.2f;
        private static final float VELOCITY_THRESHOLD_MULTIPLIER = 62.5f;
        private float mFriction = DEFAULT_FRICTION;
        private final MassState mMassState = new MassState();
        private float mVelocityThreshold;

        DragForce() {
        }

        /* Access modifiers changed, original: 0000 */
        public void setFrictionScalar(float frictionScalar) {
            this.mFriction = DEFAULT_FRICTION * frictionScalar;
        }

        /* Access modifiers changed, original: 0000 */
        public float getFrictionScalar() {
            return this.mFriction / DEFAULT_FRICTION;
        }

        /* Access modifiers changed, original: 0000 */
        public MassState updateValueAndVelocity(float value, float velocity, long deltaT) {
            this.mMassState.mVelocity = (float) (((double) velocity) * Math.exp((double) ((((float) deltaT) / 1000.0f) * this.mFriction)));
            MassState massState = this.mMassState;
            massState.mValue = (value - (velocity / this.mFriction)) + (massState.mVelocity / this.mFriction);
            if (isAtEquilibrium(this.mMassState.mValue, this.mMassState.mVelocity)) {
                this.mMassState.mVelocity = 0.0f;
            }
            return this.mMassState;
        }

        public float getAcceleration(float position, float velocity) {
            return this.mFriction * velocity;
        }

        public boolean isAtEquilibrium(float value, float velocity) {
            return Math.abs(velocity) < this.mVelocityThreshold;
        }

        /* Access modifiers changed, original: 0000 */
        public void setValueThreshold(float threshold) {
            this.mVelocityThreshold = VELOCITY_THRESHOLD_MULTIPLIER * threshold;
        }
    }

    public FlingAnimation(FloatValueHolder floatValueHolder) {
        super(floatValueHolder);
        this.mFlingForce.setValueThreshold(getValueThreshold());
    }

    public <K> FlingAnimation(K object, FloatPropertyCompat<K> property) {
        super(object, property);
        this.mFlingForce.setValueThreshold(getValueThreshold());
    }

    public FlingAnimation setFriction(float friction) {
        if (friction > 0.0f) {
            this.mFlingForce.setFrictionScalar(friction);
            return this;
        }
        throw new IllegalArgumentException("Friction must be positive");
    }

    public float getFriction() {
        return this.mFlingForce.getFrictionScalar();
    }

    public FlingAnimation setMinValue(float minValue) {
        super.setMinValue(minValue);
        return this;
    }

    public FlingAnimation setMaxValue(float maxValue) {
        super.setMaxValue(maxValue);
        return this;
    }

    public FlingAnimation setStartVelocity(float startVelocity) {
        super.setStartVelocity(startVelocity);
        return this;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean updateValueAndVelocity(long deltaT) {
        MassState state = this.mFlingForce.updateValueAndVelocity(this.mValue, this.mVelocity, deltaT);
        this.mValue = state.mValue;
        this.mVelocity = state.mVelocity;
        if (this.mValue < this.mMinValue) {
            this.mValue = this.mMinValue;
            return true;
        } else if (this.mValue > this.mMaxValue) {
            this.mValue = this.mMaxValue;
            return true;
        } else if (isAtEquilibrium(this.mValue, this.mVelocity)) {
            return true;
        } else {
            return false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public float getAcceleration(float value, float velocity) {
        return this.mFlingForce.getAcceleration(value, velocity);
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isAtEquilibrium(float value, float velocity) {
        return value >= this.mMaxValue || value <= this.mMinValue || this.mFlingForce.isAtEquilibrium(value, velocity);
    }

    /* Access modifiers changed, original: 0000 */
    public void setValueThreshold(float threshold) {
        this.mFlingForce.setValueThreshold(threshold);
    }

    public float predictNaturalDest() {
        return (this.mValue - (this.mVelocity / this.mFlingForce.mFriction)) + ((this.mFlingForce.mVelocityThreshold * Math.signum(this.mVelocity)) / this.mFlingForce.mFriction);
    }

    public float predictTimeTo(float someValue) {
        return predictTimeWithVelocity(((someValue - this.mValue) + (this.mVelocity / this.mFlingForce.mFriction)) * this.mFlingForce.mFriction);
    }

    public float predictDuration() {
        return predictTimeWithVelocity(Math.signum(this.mVelocity) * this.mFlingForce.mVelocityThreshold);
    }

    private float predictTimeWithVelocity(float v) {
        return (float) ((Math.log((double) (v / this.mVelocity)) * 1000.0d) / ((double) this.mFlingForce.mFriction));
    }
}
