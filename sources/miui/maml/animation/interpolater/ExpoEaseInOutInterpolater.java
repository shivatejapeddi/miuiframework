package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class ExpoEaseInOutInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        if (t == 0.0f) {
            return 0.0f;
        }
        if (t == 1.0f) {
            return 1.0f;
        }
        float f = 2.0f * t;
        t = f;
        if (f < 1.0f) {
            return ((float) Math.pow(2.0d, (double) ((t - 1.0f) * 10.0f))) * 0.5f;
        }
        float f2 = t - 1.0f;
        t = f2;
        return ((float) ((-Math.pow(2.0d, (double) (f2 * -10.0f))) + 2.0d)) * 0.5f;
    }
}
