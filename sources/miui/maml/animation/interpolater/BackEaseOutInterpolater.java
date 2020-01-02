package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class BackEaseOutInterpolater implements Interpolator {
    private final float mFactor;

    public BackEaseOutInterpolater() {
        this.mFactor = 1.70158f;
    }

    public BackEaseOutInterpolater(float factor) {
        this.mFactor = factor;
    }

    public float getInterpolation(float t) {
        float f = t - 1.0f;
        t = f;
        f *= t;
        float f2 = this.mFactor;
        return (f * (((f2 + 1.0f) * t) + f2)) + 1.0f;
    }
}
