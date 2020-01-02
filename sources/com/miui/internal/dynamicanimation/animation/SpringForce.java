package com.miui.internal.dynamicanimation.animation;

public final class SpringForce implements Force {
    public static final float DAMPING_RATIO_HIGH_BOUNCY = 0.2f;
    public static final float DAMPING_RATIO_LOW_BOUNCY = 0.75f;
    public static final float DAMPING_RATIO_MEDIUM_BOUNCY = 0.5f;
    public static final float DAMPING_RATIO_NO_BOUNCY = 1.0f;
    public static final float STIFFNESS_HIGH = 10000.0f;
    public static final float STIFFNESS_LOW = 200.0f;
    public static final float STIFFNESS_MEDIUM = 1500.0f;
    public static final float STIFFNESS_VERY_LOW = 50.0f;
    private static final double UNSET = Double.MAX_VALUE;
    private static final double VELOCITY_THRESHOLD_MULTIPLIER = 62.5d;
    private double mDampedFreq;
    double mDampingRatio = 0.5d;
    private double mFinalPosition = UNSET;
    private double mGammaMinus;
    private double mGammaPlus;
    private boolean mInitialized = false;
    private final MassState mMassState = new MassState();
    double mNaturalFreq = Math.sqrt(1500.0d);
    private double mValueThreshold;
    private double mVelocityThreshold;

    public SpringForce(float finalPosition) {
        this.mFinalPosition = (double) finalPosition;
    }

    public SpringForce setStiffness(float stiffness) {
        if (stiffness > 0.0f) {
            this.mNaturalFreq = Math.sqrt((double) stiffness);
            this.mInitialized = false;
            return this;
        }
        throw new IllegalArgumentException("Spring stiffness constant must be positive.");
    }

    public float getStiffness() {
        double d = this.mNaturalFreq;
        return (float) (d * d);
    }

    public SpringForce setDampingRatio(float dampingRatio) {
        if (dampingRatio >= 0.0f) {
            this.mDampingRatio = (double) dampingRatio;
            this.mInitialized = false;
            return this;
        }
        throw new IllegalArgumentException("Damping ratio must be non-negative");
    }

    public float getDampingRatio() {
        return (float) this.mDampingRatio;
    }

    public SpringForce setFinalPosition(float finalPosition) {
        this.mFinalPosition = (double) finalPosition;
        return this;
    }

    public float getFinalPosition() {
        return (float) this.mFinalPosition;
    }

    public float getAcceleration(float lastDisplacement, float lastVelocity) {
        lastDisplacement -= getFinalPosition();
        double d = this.mNaturalFreq;
        return (float) (((-(d * d)) * ((double) lastDisplacement)) - (((double) lastVelocity) * ((d * 2.0d) * this.mDampingRatio)));
    }

    public boolean isAtEquilibrium(float value, float velocity) {
        if (((double) Math.abs(velocity)) >= this.mVelocityThreshold || ((double) Math.abs(value - getFinalPosition())) >= this.mValueThreshold) {
            return false;
        }
        return true;
    }

    private void init() {
        if (!this.mInitialized) {
            if (this.mFinalPosition != UNSET) {
                double d = this.mDampingRatio;
                if (d > 1.0d) {
                    double d2 = -d;
                    double d3 = this.mNaturalFreq;
                    this.mGammaPlus = (d2 * d3) + (d3 * Math.sqrt((d * d) - 1.0d));
                    d = this.mDampingRatio;
                    d2 = -d;
                    d3 = this.mNaturalFreq;
                    this.mGammaMinus = (d2 * d3) - (d3 * Math.sqrt((d * d) - 1.0d));
                } else if (d >= 0.0d && d < 1.0d) {
                    this.mDampedFreq = this.mNaturalFreq * Math.sqrt(1.0d - (d * d));
                }
                this.mInitialized = true;
                return;
            }
            throw new IllegalStateException("Error: Final position of the spring must be set before the animation starts");
        }
    }

    /* Access modifiers changed, original: 0000 */
    public MassState updateValues(double lastDisplacement, double lastVelocity, long timeElapsed) {
        double d;
        double currentVelocity;
        init();
        double deltaT = ((double) timeElapsed) / 1000.0d;
        double lastDisplacement2 = lastDisplacement - this.mFinalPosition;
        double d2 = this.mDampingRatio;
        double d3;
        double coeffB;
        double d4;
        double d5;
        if (d2 > 1.0d) {
            d2 = this.mGammaMinus;
            d = (d2 * lastDisplacement2) - lastVelocity;
            d3 = this.mGammaPlus;
            d = lastDisplacement2 - (d / (d2 - d3));
            coeffB = ((d2 * lastDisplacement2) - lastVelocity) / (d2 - d3);
            d2 = (Math.pow(2.718281828459045d, d2 * deltaT) * d) + (Math.pow(2.718281828459045d, this.mGammaPlus * deltaT) * coeffB);
            d3 = this.mGammaMinus;
            double currentVelocity2 = (d * d3) * Math.pow(2.718281828459045d, d3 * deltaT);
            d3 = this.mGammaPlus;
            d4 = lastDisplacement2;
            d = d2;
            currentVelocity = currentVelocity2 + ((coeffB * d3) * Math.pow(2.718281828459045d, d3 * deltaT));
        } else if (d2 == 1.0d) {
            d2 = lastDisplacement2;
            d = this.mNaturalFreq;
            d3 = lastVelocity + (d * lastDisplacement2);
            d = Math.pow(2.718281828459045d, (-d) * deltaT) * (d2 + (d3 * deltaT));
            coeffB = (d2 + (d3 * deltaT)) * Math.pow(2.718281828459045d, (-this.mNaturalFreq) * deltaT);
            d5 = this.mNaturalFreq;
            d4 = lastDisplacement2;
            currentVelocity = (coeffB * (-d5)) + (Math.pow(2.718281828459045d, (-d5) * deltaT) * d3);
        } else {
            currentVelocity = lastDisplacement2;
            d = 1.0d / this.mDampedFreq;
            d5 = this.mNaturalFreq;
            d *= ((d2 * d5) * lastDisplacement2) + lastVelocity;
            d2 = Math.pow(2.718281828459045d, ((-d2) * d5) * deltaT) * ((Math.cos(this.mDampedFreq * deltaT) * currentVelocity) + (Math.sin(this.mDampedFreq * deltaT) * d));
            d5 = this.mNaturalFreq;
            double d6 = (-d5) * d2;
            lastDisplacement2 = this.mDampingRatio;
            d6 *= lastDisplacement2;
            lastDisplacement2 = Math.pow(2.718281828459045d, ((-lastDisplacement2) * d5) * deltaT);
            d5 = this.mDampedFreq;
            lastDisplacement = d2;
            d2 = ((-d5) * currentVelocity) * Math.sin(d5 * deltaT);
            d5 = this.mDampedFreq;
            d = lastDisplacement;
            currentVelocity = d6 + (lastDisplacement2 * (d2 + ((d5 * d) * Math.cos(d5 * deltaT))));
        }
        MassState massState = this.mMassState;
        massState.mValue = (float) (this.mFinalPosition + d);
        massState.mVelocity = (float) currentVelocity;
        return massState;
    }

    /* Access modifiers changed, original: 0000 */
    public void setValueThreshold(double threshold) {
        this.mValueThreshold = Math.abs(threshold);
        this.mVelocityThreshold = this.mValueThreshold * VELOCITY_THRESHOLD_MULTIPLIER;
    }
}
