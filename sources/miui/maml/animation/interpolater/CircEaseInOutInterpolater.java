package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class CircEaseInOutInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        float f = t * 2.0f;
        t = f;
        if (f < 1.0f) {
            return ((float) (Math.sqrt((double) (1.0f - (t * t))) - 1.0d)) * -0.5f;
        }
        float f2 = t - 2.0f;
        return ((float) (Math.sqrt((double) (1.0f - (f2 * f2))) + 1.0d)) * 0.5f;
    }
}
