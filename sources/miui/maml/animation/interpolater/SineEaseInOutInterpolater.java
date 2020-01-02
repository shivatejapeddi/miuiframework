package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class SineEaseInOutInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        return ((float) (Math.cos(((double) t) * 3.141592653589793d) - 1.0d)) * -0.5f;
    }
}
