package android.hardware.camera2.params;

import android.hardware.camera2.utils.HashCodeHelpers;

public final class RecommendedStreamConfiguration extends StreamConfiguration {
    private final int mUsecaseBitmap;

    public RecommendedStreamConfiguration(int format, int width, int height, boolean input, int usecaseBitmap) {
        super(format, width, height, input);
        this.mUsecaseBitmap = usecaseBitmap;
    }

    public int getUsecaseBitmap() {
        return this.mUsecaseBitmap;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RecommendedStreamConfiguration)) {
            return false;
        }
        RecommendedStreamConfiguration other = (RecommendedStreamConfiguration) obj;
        if (this.mFormat == other.mFormat && this.mWidth == other.mWidth && this.mHeight == other.mHeight && this.mUsecaseBitmap == other.mUsecaseBitmap && this.mInput == other.mInput) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return HashCodeHelpers.hashCode(this.mFormat, this.mWidth, this.mHeight, this.mInput, this.mUsecaseBitmap);
    }
}
