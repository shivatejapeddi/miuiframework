package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class BounceEaseInInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        return getInterpolationImp(t);
    }

    public static float getInterpolationImp(float t) {
        return 1.0f - BounceEaseOutInterpolater.getInterpolationImp(1.0f - t);
    }
}
