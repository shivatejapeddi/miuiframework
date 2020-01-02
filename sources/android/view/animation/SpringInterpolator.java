package android.view.animation;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import com.android.internal.R;

public class SpringInterpolator extends BaseInterpolator implements TimeInterpolator {
    private static final float DEFAULT_DAMPING = 0.95f;
    private static final float DEFAULT_RESPONSE = 0.6f;
    private float c;
    private float c1;
    private float c2;
    private float damping;
    private float initial;
    private float k;
    private float m;
    private float r;
    private float response;
    private float w;

    public SpringInterpolator() {
        this.damping = DEFAULT_DAMPING;
        this.response = 0.6f;
        this.initial = -1.0f;
        this.m = 1.0f;
        double pow = Math.pow(6.283185307179586d / ((double) this.response), 2.0d);
        float f = this.m;
        this.k = (float) (pow * ((double) f));
        this.c = (float) (((((double) this.damping) * 12.566370614359172d) * ((double) f)) / ((double) this.response));
        f = (f * 4.0f) * this.k;
        float f2 = this.c;
        f2 = (float) Math.sqrt((double) (f - (f2 * f2)));
        float f3 = this.m;
        this.w = f2 / (f3 * 2.0f);
        this.r = -((this.c / 2.0f) * f3);
        f2 = this.initial;
        this.c1 = f2;
        this.c2 = (0.0f - (this.r * f2)) / this.w;
        this.damping = DEFAULT_DAMPING;
        this.response = 0.6f;
        refreshParams();
    }

    public SpringInterpolator(float damping, float response) {
        this.damping = DEFAULT_DAMPING;
        this.response = 0.6f;
        this.initial = -1.0f;
        this.m = 1.0f;
        double pow = Math.pow(6.283185307179586d / ((double) this.response), 2.0d);
        float f = this.m;
        this.k = (float) (pow * ((double) f));
        this.c = (float) (((((double) this.damping) * 12.566370614359172d) * ((double) f)) / ((double) this.response));
        f = (f * 4.0f) * this.k;
        float f2 = this.c;
        f2 = (float) Math.sqrt((double) (f - (f2 * f2)));
        float f3 = this.m;
        this.w = f2 / (f3 * 2.0f);
        this.r = -((this.c / 2.0f) * f3);
        f2 = this.initial;
        this.c1 = f2;
        this.c2 = (0.0f - (this.r * f2)) / this.w;
        this.damping = damping;
        this.response = response;
        refreshParams();
    }

    public SpringInterpolator(Context context, AttributeSet attrs) {
        this(context.getResources(), context.getTheme(), attrs);
    }

    public SpringInterpolator(Resources res, Theme theme, AttributeSet attrs) {
        TypedArray a;
        this.damping = DEFAULT_DAMPING;
        this.response = 0.6f;
        this.initial = -1.0f;
        this.m = 1.0f;
        double pow = Math.pow(6.283185307179586d / ((double) this.response), 2.0d);
        float f = this.m;
        this.k = (float) (pow * ((double) f));
        this.c = (float) (((((double) this.damping) * 12.566370614359172d) * ((double) f)) / ((double) this.response));
        f = (f * 4.0f) * this.k;
        float f2 = this.c;
        f2 = (float) Math.sqrt((double) (f - (f2 * f2)));
        float f3 = this.m;
        this.w = f2 / (f3 * 2.0f);
        this.r = -((this.c / 2.0f) * f3);
        f2 = this.initial;
        this.c1 = f2;
        this.c2 = (0.0f - (this.r * f2)) / this.w;
        if (theme != null) {
            a = theme.obtainStyledAttributes(attrs, R.styleable.SpringInterpolator, 0, 0);
        } else {
            a = res.obtainAttributes(attrs, R.styleable.SpringInterpolator);
        }
        this.damping = a.getFloat(0, DEFAULT_DAMPING);
        this.response = a.getFloat(1, DEFAULT_DAMPING);
        a.recycle();
        refreshParams();
    }

    public float getInterpolation(float input) {
        return (float) ((Math.pow(2.718281828459045d, (double) (this.r * input)) * ((((double) this.c1) * Math.cos((double) (this.w * input))) + (((double) this.c2) * Math.sin((double) (this.w * input))))) + 1.0d);
    }

    public float getDamping() {
        return this.damping;
    }

    public float getResponse() {
        return this.response;
    }

    public SpringInterpolator setDamping(float damping) {
        this.damping = damping;
        refreshParams();
        return this;
    }

    public SpringInterpolator setResponse(float response) {
        this.response = response;
        refreshParams();
        return this;
    }

    private void refreshParams() {
        double pow = Math.pow(6.283185307179586d / ((double) this.response), 2.0d);
        float f = this.m;
        this.k = (float) (pow * ((double) f));
        this.c = (float) (((((double) this.damping) * 12.566370614359172d) * ((double) f)) / ((double) this.response));
        f = (f * 4.0f) * this.k;
        float f2 = this.c;
        f2 = (float) Math.sqrt((double) (f - (f2 * f2)));
        float f3 = this.m;
        this.w = f2 / (f3 * 2.0f);
        this.r = -((this.c / 2.0f) * f3);
        this.c2 = (0.0f - (this.r * this.initial)) / this.w;
    }
}
