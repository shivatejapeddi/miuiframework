package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class ElasticEaseInOutInterpolater implements Interpolator {
    private float mAmplitude;
    private float mPriod;

    public ElasticEaseInOutInterpolater() {
        this.mPriod = 0.45000002f;
        this.mAmplitude = 0.0f;
    }

    public ElasticEaseInOutInterpolater(float priod, float amplitude) {
        this.mPriod = priod;
        this.mAmplitude = amplitude;
    }

    public float getInterpolation(float t) {
        float a = this.mAmplitude;
        if (t == 0.0f) {
            return 0.0f;
        }
        float f = t / 0.5f;
        t = f;
        if (f == 2.0f) {
            return 1.0f;
        }
        if (a < 1.0f) {
            a = 1.0f;
            f = this.mPriod / 4.0f;
        } else {
            f = (float) ((((double) this.mPriod) / 6.283185307179586d) * Math.asin((double) (1.0f / a)));
        }
        float f2;
        if (t < 1.0f) {
            f2 = t - 1.0f;
            return ((float) ((((double) a) * Math.pow(2.0d, (double) (f2 * 10.0f))) * Math.sin((((double) (f2 - f)) * 6.283185307179586d) / ((double) this.mPriod)))) * -0.5f;
        }
        f2 = t - 1.0f;
        return (float) ((((((double) a) * Math.pow(2.0d, (double) (f2 * -10.0f))) * Math.sin((((double) (f2 - f)) * 6.283185307179586d) / ((double) this.mPriod))) * 0.5d) + 1.0d);
    }
}
