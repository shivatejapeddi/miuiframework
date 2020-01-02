package android.app.prediction;

import android.annotation.SystemApi;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@SystemApi
public final class AppPredictionContext implements Parcelable {
    public static final Creator<AppPredictionContext> CREATOR = new Creator<AppPredictionContext>() {
        public AppPredictionContext createFromParcel(Parcel parcel) {
            return new AppPredictionContext(parcel, null);
        }

        public AppPredictionContext[] newArray(int size) {
            return new AppPredictionContext[size];
        }
    };
    private final Bundle mExtras;
    private final String mPackageName;
    private final int mPredictedTargetCount;
    private final String mUiSurface;

    @SystemApi
    public static final class Builder {
        private Bundle mExtras;
        private final String mPackageName;
        private int mPredictedTargetCount;
        private String mUiSurface;

        @SystemApi
        public Builder(Context context) {
            this.mPackageName = context.getPackageName();
        }

        public Builder setPredictedTargetCount(int predictedTargetCount) {
            this.mPredictedTargetCount = predictedTargetCount;
            return this;
        }

        public Builder setUiSurface(String uiSurface) {
            this.mUiSurface = uiSurface;
            return this;
        }

        public Builder setExtras(Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        public AppPredictionContext build() {
            return new AppPredictionContext(this.mUiSurface, this.mPredictedTargetCount, this.mPackageName, this.mExtras, null);
        }
    }

    /* synthetic */ AppPredictionContext(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    /* synthetic */ AppPredictionContext(String x0, int x1, String x2, Bundle x3, AnonymousClass1 x4) {
        this(x0, x1, x2, x3);
    }

    private AppPredictionContext(String uiSurface, int numPredictedTargets, String packageName, Bundle extras) {
        this.mUiSurface = uiSurface;
        this.mPredictedTargetCount = numPredictedTargets;
        this.mPackageName = packageName;
        this.mExtras = extras;
    }

    private AppPredictionContext(Parcel parcel) {
        this.mUiSurface = parcel.readString();
        this.mPredictedTargetCount = parcel.readInt();
        this.mPackageName = parcel.readString();
        this.mExtras = parcel.readBundle();
    }

    public String getUiSurface() {
        return this.mUiSurface;
    }

    public int getPredictedTargetCount() {
        return this.mPredictedTargetCount;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public Bundle getExtras() {
        return this.mExtras;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (o == this) {
            return true;
        }
        if (!getClass().equals(o != null ? o.getClass() : null)) {
            return false;
        }
        AppPredictionContext other = (AppPredictionContext) o;
        if (!(this.mPredictedTargetCount == other.mPredictedTargetCount && this.mUiSurface.equals(other.mUiSurface) && this.mPackageName.equals(other.mPackageName))) {
            z = false;
        }
        return z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUiSurface);
        dest.writeInt(this.mPredictedTargetCount);
        dest.writeString(this.mPackageName);
        dest.writeBundle(this.mExtras);
    }
}
