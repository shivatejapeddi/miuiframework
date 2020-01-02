package android.util;

class TypedValueInjector {
    TypedValueInjector() {
    }

    static float miuiScale(float value, DisplayMetrics metrics) {
        return metrics.scaledDensity * value;
    }
}
