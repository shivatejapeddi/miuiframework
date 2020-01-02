package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class BounceEaseInOutInterpolater implements Interpolator {
    public float getInterpolation(float t) {
        if (t < 0.5f) {
            return BounceEaseInInterpolater.getInterpolationImp(2.0f * t) * 0.5f;
        }
        return (BounceEaseOutInterpolater.getInterpolationImp((2.0f * t) - 1.0f) * 0.5f) + 0.5f;
    }
}
