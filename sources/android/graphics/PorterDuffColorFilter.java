package android.graphics;

import android.annotation.UnsupportedAppUsage;
import android.graphics.PorterDuff.Mode;

public class PorterDuffColorFilter extends ColorFilter {
    private int mColor;
    private Mode mMode;

    private static native long native_CreateBlendModeFilter(int i, int i2);

    public PorterDuffColorFilter(int color, Mode mode) {
        this.mColor = color;
        this.mMode = mode;
    }

    @UnsupportedAppUsage
    public int getColor() {
        return this.mColor;
    }

    @UnsupportedAppUsage
    public Mode getMode() {
        return this.mMode;
    }

    /* Access modifiers changed, original: 0000 */
    public long createNativeInstance() {
        return native_CreateBlendModeFilter(this.mColor, this.mMode.nativeInt);
    }

    public boolean equals(Object object) {
        boolean z = true;
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        PorterDuffColorFilter other = (PorterDuffColorFilter) object;
        if (!(this.mColor == other.mColor && this.mMode.nativeInt == other.mMode.nativeInt)) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (this.mMode.hashCode() * 31) + this.mColor;
    }
}
