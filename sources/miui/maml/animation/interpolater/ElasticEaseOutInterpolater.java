package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class ElasticEaseOutInterpolater implements Interpolator {
    private float mAmplitude;
    private float mPriod;

    public ElasticEaseOutInterpolater() {
        this.mPriod = 0.3f;
        this.mAmplitude = 0.0f;
    }

    public ElasticEaseOutInterpolater(float priod, float amplitude) {
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
        return (float) (((((double) a) * Math.pow(2.0d, (double) (-10.0f * t))) * Math.sin((((double) (t - s)) * 6.283185307179586d) / ((double) this.mPriod))) + 1.0d);
    }
}
