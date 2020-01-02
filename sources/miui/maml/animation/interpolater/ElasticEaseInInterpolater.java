package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class ElasticEaseInInterpolater implements Interpolator {
    private float mAmplitude;
    private float mPriod;

    public ElasticEaseInInterpolater() {
        this.mPriod = 0.3f;
        this.mAmplitude = 0.0f;
    }

    public ElasticEaseInInterpolater(float priod, float amplitude) {
        this.mPriod = priod;
        this.mAmplitude = amplitude;
    }

    public float getInterpolation(float t) {
        float a = this.mAmplitude;
        if (t == 0.0f) {
            return 0.0f;
        }
        if (t == 1.0f) {
            return 1.0f;
        }
        float s;
        if (a < 1.0f) {
            a = 1.0f;
            s = this.mPriod / 4.0f;
        } else {
            s = (float) ((((double) this.mPriod) / 6.283185307179586d) * Math.asin((double) (1.0f / a)));
        }
        float f = t - 1.0f;
        return -((float) ((((double) a) * Math.pow(2.0d, (double) (f * 10.0f))) * Math.sin((((double) (f - s)) * 6.283185307179586d) / ((double) this.mPriod))));
    }
}
