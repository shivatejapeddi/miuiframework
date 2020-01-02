package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class QuadEaseInInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        return t * t;
    }
}
