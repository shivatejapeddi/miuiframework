package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class LinearInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        return t;
    }
}
