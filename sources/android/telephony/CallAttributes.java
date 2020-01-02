package android.telephony;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

@SystemApi
public final class CallAttributes implements Parcelable {
    public static final Creator<CallAttributes> CREATOR = new Creator() {
        public CallAttributes createFromParcel(Parcel in) {
            return new CallAttributes(in, null);
        }

        public CallAttributes[] newArray(int size) {
            return new CallAttributes[size];
        }
    };
    private CallQuality mCallQuality;
    private int mNetworkType;
    private PreciseCallState mPreciseCallState;

    /* synthetic */ CallAttributes(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public CallAttributes(PreciseCallState state, int networkType, CallQuality callQuality) {
        this.mPreciseCallState = state;
        this.mNetworkType = networkType;
        this.mCallQuality = callQuality;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mPreciseCallState=");
        stringBuilder.append(this.mPreciseCallState);
        stringBuilder.append(" mNetworkType=");
        stringBuilder.append(this.mNetworkType);
        stringBuilder.append(" mCallQuality=");
        stringBuilder.append(this.mCallQuality);
        return stringBuilder.toString();
    }

    private CallAttributes(Parcel in) {
        this.mPreciseCallState = (PreciseCallState) in.readParcelable(PreciseCallState.class.getClassLoader());
        this.mNetworkType = in.readInt();
        this.mCallQuality = (CallQuality) in.readParcelable(CallQuality.class.getClassLoader());
    }

    public PreciseCallState getPreciseCallState() {
        return this.mPreciseCallState;
    }

    public int getNetworkType() {
        return this.mNetworkType;
    }

    public CallQuality getCallQuality() {
        return this.mCallQuality;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mPreciseCallState, Integer.valueOf(this.mNetworkType), this.mCallQuality});
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (o == null || !(o instanceof CallAttributes) || hashCode() != o.hashCode()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        CallAttributes s = (CallAttributes) o;
        if (Objects.equals(this.mPreciseCallState, s.mPreciseCallState) && this.mNetworkType == s.mNetworkType && Objects.equals(this.mCallQuality, s.mCallQuality)) {
            z = true;
        }
        return z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mPreciseCallState, flags);
        dest.writeInt(this.mNetworkType);
        dest.writeParcelable(this.mCallQuality, flags);
    }
}
