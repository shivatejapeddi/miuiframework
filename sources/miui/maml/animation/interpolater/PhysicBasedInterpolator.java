package miui.maml.animation.interpolater;

import android.view.animation.Interpolator;

public class PhysicBasedInterpolator implements Interpolator {
    private float c;
    private float c1;
    private float c2;
    private float k;
    private float m;
    private float mInitial;
    private float r;
    private float w;

    public PhysicBasedInterpolator() {
        this(0.9f, 0.3f);
    }

    public PhysicBasedInterpolator(float damping, float response) {
        this.mInitial = -1.0f;
        this.m = 1.0f;
        this.c1 = this.mInitial;
        double pow = Math.pow(6.283185307179586d / ((double) response), 2.0d);
        float f = this.m;
        this.k = (float) (pow * ((double) f));
        this.c = (float) (((((double) damping) * 12.566370614359172d) * ((double) f)) / ((double) response));
        f = (f * 4.0f) * this.k;
        float f2 = this.c;
        f2 = (float) Math.sqrt((double) (f - (f2 * f2)));
        float f3 = this.m;
        this.w = f2 / (f3 * 2.0f);
        this.r = -((this.c / 2.0f) * f3);
        this.c2 = (0.0f - (this.r * this.mInitial)) / this.w;
    }

    public float getInterpolation(float input) {
        return (float) ((Math.pow(2.718281828459045d, (double) (this.r * input)) * ((((double) this.c1) * Math.cos((double) (this.w * input))) + (((double) this.c2) * Math.sin((double) (this.w * input))))) + 1.0d);
    }
}
