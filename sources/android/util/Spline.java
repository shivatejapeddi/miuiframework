package android.util;

public abstract class Spline {

    public static class LinearSpline extends Spline {
        private final float[] mM;
        private final float[] mX;
        private final float[] mY;

        public LinearSpline(float[] x, float[] y) {
            if (x == null || y == null || x.length != y.length || x.length < 2) {
                throw new IllegalArgumentException("There must be at least two control points and the arrays must be of equal length.");
            }
            int N = x.length;
            this.mM = new float[(N - 1)];
            for (int i = 0; i < N - 1; i++) {
                this.mM[i] = (y[i + 1] - y[i]) / (x[i + 1] - x[i]);
            }
            this.mX = x;
            this.mY = y;
        }

        public float interpolate(float x) {
            int n = this.mX.length;
            if (Float.isNaN(x)) {
                return x;
            }
            float[] fArr = this.mX;
            if (x <= fArr[0]) {
                return this.mY[0];
            }
            if (x >= fArr[n - 1]) {
                return this.mY[n - 1];
            }
            int i = 0;
            float[] fArr2;
            do {
                fArr2 = this.mX;
                if (x < fArr2[i + 1]) {
                    return this.mY[i] + (this.mM[i] * (x - fArr2[i]));
                }
                i++;
            } while (x != fArr2[i]);
            return this.mY[i];
        }

        public String toString() {
            StringBuilder str = new StringBuilder();
            int n = this.mX.length;
            str.append("LinearSpline{[");
            for (int i = 0; i < n; i++) {
                String str2 = ", ";
                if (i != 0) {
                    str.append(str2);
                }
                str.append("(");
                str.append(this.mX[i]);
                str.append(str2);
                str.append(this.mY[i]);
                if (i < n - 1) {
                    str.append(": ");
                    str.append(this.mM[i]);
                }
                str.append(")");
            }
            str.append("]}");
            return str.toString();
        }
    }

    public static class MonotoneCubicSpline extends Spline {
        private float[] mM;
        private float[] mX;
        private float[] mY;

        public MonotoneCubicSpline(float[] x, float[] y) {
            if (x == null || y == null || x.length != y.length || x.length < 2) {
                throw new IllegalArgumentException("There must be at least two control points and the arrays must be of equal length.");
            }
            float h;
            int n = x.length;
            float[] d = new float[(n - 1)];
            float[] m = new float[n];
            int i = 0;
            while (i < n - 1) {
                h = x[i + 1] - x[i];
                if (h > 0.0f) {
                    d[i] = (y[i + 1] - y[i]) / h;
                    i++;
                } else {
                    throw new IllegalArgumentException("The control points must all have strictly increasing X values.");
                }
            }
            m[0] = d[0];
            for (i = 1; i < n - 1; i++) {
                m[i] = (d[i - 1] + d[i]) * 0.5f;
            }
            m[n - 1] = d[n - 2];
            for (i = 0; i < n - 1; i++) {
                if (d[i] == 0.0f) {
                    m[i] = 0.0f;
                    m[i + 1] = 0.0f;
                } else {
                    h = m[i] / d[i];
                    float b = m[i + 1] / d[i];
                    if (h < 0.0f || b < 0.0f) {
                        throw new IllegalArgumentException("The control points must have monotonic Y values.");
                    }
                    float h2 = (float) Math.hypot((double) h, (double) b);
                    if (h2 > 3.0f) {
                        float t = 3.0f / h2;
                        m[i] = m[i] * t;
                        int i2 = i + 1;
                        m[i2] = m[i2] * t;
                    }
                }
            }
            this.mX = x;
            this.mY = y;
            this.mM = m;
        }

        public float interpolate(float x) {
            int n = this.mX.length;
            if (Float.isNaN(x)) {
                return x;
            }
            float[] fArr = this.mX;
            if (x <= fArr[0]) {
                return this.mY[0];
            }
            if (x >= fArr[n - 1]) {
                return this.mY[n - 1];
            }
            int i = 0;
            float[] fArr2;
            do {
                fArr2 = this.mX;
                if (x >= fArr2[i + 1]) {
                    i++;
                } else {
                    float h = fArr2[i + 1] - fArr2[i];
                    float t = (x - fArr2[i]) / h;
                    float[] fArr3 = this.mY;
                    float f = fArr3[i] * ((t * 2.0f) + 1.0f);
                    float[] fArr4 = this.mM;
                    return (((f + ((fArr4[i] * h) * t)) * (1.0f - t)) * (1.0f - t)) + ((((fArr3[i + 1] * (3.0f - (2.0f * t))) + ((fArr4[i + 1] * h) * (t - 1.0f))) * t) * t);
                }
            } while (x != fArr2[i]);
            return this.mY[i];
        }

        public String toString() {
            StringBuilder str = new StringBuilder();
            int n = this.mX.length;
            str.append("MonotoneCubicSpline{[");
            for (int i = 0; i < n; i++) {
                String str2 = ", ";
                if (i != 0) {
                    str.append(str2);
                }
                str.append("(");
                str.append(this.mX[i]);
                str.append(str2);
                str.append(this.mY[i]);
                str.append(": ");
                str.append(this.mM[i]);
                str.append(")");
            }
            str.append("]}");
            return str.toString();
        }
    }

    public abstract float interpolate(float f);

    public static Spline createSpline(float[] x, float[] y) {
        if (!isStrictlyIncreasing(x)) {
            throw new IllegalArgumentException("The control points must all have strictly increasing X values.");
        } else if (isMonotonic(y)) {
            return createMonotoneCubicSpline(x, y);
        } else {
            return createLinearSpline(x, y);
        }
    }

    public static Spline createMonotoneCubicSpline(float[] x, float[] y) {
        return new MonotoneCubicSpline(x, y);
    }

    public static Spline createLinearSpline(float[] x, float[] y) {
        return new LinearSpline(x, y);
    }

    private static boolean isStrictlyIncreasing(float[] x) {
        if (x == null || x.length < 2) {
            throw new IllegalArgumentException("There must be at least two control points.");
        }
        float prev = x[0];
        for (int i = 1; i < x.length; i++) {
            float curr = x[i];
            if (curr <= prev) {
                return false;
            }
            prev = curr;
        }
        return true;
    }

    private static boolean isMonotonic(float[] x) {
        if (x == null || x.length < 2) {
            throw new IllegalArgumentException("There must be at least two control points.");
        }
        float prev = x[0];
        for (int i = 1; i < x.length; i++) {
            float curr = x[i];
            if (curr < prev) {
                return false;
            }
            prev = curr;
        }
        return true;
    }
}
