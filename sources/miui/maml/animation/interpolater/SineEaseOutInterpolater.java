package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class SineEaseOutInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        return (float) Math.sin(((double) t) * 1.5707963267948966d);
    }
}
