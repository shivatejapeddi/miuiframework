package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class ExpoEaseInInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        return t == 0.0f ? 0.0f : (float) Math.pow(2.0d, (double) ((t - 1.0f) * 10.0f));
    }
}
