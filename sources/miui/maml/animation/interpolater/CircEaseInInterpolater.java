package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class CircEaseInInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        return -((float) (Math.sqrt((double) (1.0f - (t * t))) - 1.0d));
    }
}
