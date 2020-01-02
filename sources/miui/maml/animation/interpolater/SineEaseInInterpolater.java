package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class SineEaseInInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        return (-((float) Math.cos(((double) t) * 1.5707963267948966d))) + 1.0f;
    }
}
