package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class QuadEaseInOutInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        float f = t * 2.0f;
        t = f;
        if (f < 1.0f) {
            return (0.5f * t) * t;
        }
        float f2 = t - 1.0f;
        return ((f2 * (f2 - 2.0f)) - 1.0f) * -0.5f;
    }
}
