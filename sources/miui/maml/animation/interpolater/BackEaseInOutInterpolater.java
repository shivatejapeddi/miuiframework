package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class BackEaseInOutInterpolater implements Interpolator {
    private final float mFactor;

    public BackEaseInOutInterpolater() {
        this.mFactor = 1.70158f;
    }

    public BackEaseInOutInterpolater(float factor) {
        this.mFactor = factor;
    }

    public float getInterpolation(float t) {
        float s = this.mFactor;
        float f = t * 2.0f;
        t = f;
        if (f < 1.0f) {
            f = (float) (((double) s) * 1.525d);
            return ((t * t) * (((f + 1.0f) * t) - f)) * 0.5f;
        }
        f = t - 2.0f;
        t = f;
        float f2 = (float) (((double) s) * 1.525d);
        return (((f * t) * (((f2 + 1.0f) * t) + f2)) + 2.0f) * 0.5f;
    }
}
