package android.app.prediction;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public final class AppTargetEvent implements Parcelable {
    public static final int ACTION_DISMISS = 2;
    public static final int ACTION_LAUNCH = 1;
    public static final int ACTION_PIN = 3;
    public static final Creator<AppTargetEvent> CREATOR = new Creator<AppTargetEvent>() {
        public AppTargetEvent createFromParcel(Parcel parcel) {
            return new AppTargetEvent(parcel, null);
        }

        public AppTargetEvent[] newArray(int size) {
            return new AppTargetEvent[size];
        }
    };
    private final int mAction;
    private final String mLocation;
    private final AppTarget mTarget;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ActionType {
    }

    @SystemApi
    public static final class Builder {
        private int mAction;
        private String mLocation;
        private AppTarget mTarget;

        public Builder(AppTarget target, int actionType) {
            this.mTarget = target;
            this.mAction = actionType;
        }

        public Builder setLaunchLocation(String location) {
            this.mLocation = location;
            return this;
        }

        public AppTargetEvent build() {
            return new AppTargetEvent(this.mTarget, this.mLocation, this.mAction, null);
        }
    }

    /* synthetic */ AppTargetEvent(AppTarget x0, String x1, int x2, AnonymousClass1 x3) {
        this(x0, x1, x2);
    }

    /* synthetic */ AppTargetEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    private AppTargetEvent(AppTarget target, String location, int actionType) {
        this.mTarget = target;
        this.mLocation = location;
        this.mAction = actionType;
    }

    private AppTargetEvent(Parcel parcel) {
        this.mTarget = (AppTarget) parcel.readParcelable(null);
        this.mLocation = parcel.readString();
        this.mAction = parcel.readInt();
    }

    public AppTarget getTarget() {
        return this.mTarget;
    }

    public String getLaunchLocation() {
        return this.mLocation;
    }

    public int getAction() {
        return this.mAction;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!getClass().equals(o != null ? o.getClass() : null)) {
            return false;
        }
        AppTargetEvent other = (AppTargetEvent) o;
        if (this.mTarget.equals(other.mTarget) && this.mLocation.equals(other.mLocation) && this.mAction == other.mAction) {
            z = true;
        }
        return z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mTarget, 0);
        dest.writeString(this.mLocation);
        dest.writeInt(this.mAction);
    }
}
