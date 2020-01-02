package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class CircEaseOutInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        float f = t - 1.0f;
        return (float) Math.sqrt((double) (1.0f - (f * f)));
    }
}
