package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class ExpoEaseOutInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        return t == 1.0f ? 1.0f : (float) ((-Math.pow(2.0d, (double) (-10.0f * t))) + 1.0d);
    }
}
