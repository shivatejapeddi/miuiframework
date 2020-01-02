package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class CubicEaseInOutInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        float f = t * 2.0f;
        t = f;
        if (f < 1.0f) {
            return ((0.5f * t) * t) * t;
        }
        f = t - 2.0f;
        t = f;
        return (((f * t) * t) + 2.0f) * 0.5f;
    }
}
