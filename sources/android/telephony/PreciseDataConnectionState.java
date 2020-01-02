package android.telephony;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.net.LinkProperties;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.data.ApnSetting;
import java.util.Objects;

@SystemApi
public final class PreciseDataConnectionState implements Parcelable {
    public static final Creator<PreciseDataConnectionState> CREATOR = new Creator<PreciseDataConnectionState>() {
        public PreciseDataConnectionState createFromParcel(Parcel in) {
            return new PreciseDataConnectionState(in, null);
        }

        public PreciseDataConnectionState[] newArray(int size) {
            return new PreciseDataConnectionState[size];
        }
    };
    private String mAPN;
    private int mAPNTypes;
    private int mFailCause;
    private LinkProperties mLinkProperties;
    private int mNetworkType;
    private int mState;

    /* synthetic */ PreciseDataConnectionState(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    @UnsupportedAppUsage
    public PreciseDataConnectionState(int state, int networkType, int apnTypes, String apn, LinkProperties linkProperties, int failCause) {
        this.mState = -1;
        this.mNetworkType = 0;
        this.mFailCause = 0;
        this.mAPNTypes = 0;
        this.mAPN = "";
        this.mLinkProperties = null;
        this.mState = state;
        this.mNetworkType = networkType;
        this.mAPNTypes = apnTypes;
        this.mAPN = apn;
        this.mLinkProperties = linkProperties;
        this.mFailCause = failCause;
    }

    public PreciseDataConnectionState() {
        this.mState = -1;
        this.mNetworkType = 0;
        this.mFailCause = 0;
        this.mAPNTypes = 0;
        this.mAPN = "";
        this.mLinkProperties = null;
    }

    private PreciseDataConnectionState(Parcel in) {
        this.mState = -1;
        this.mNetworkType = 0;
        this.mFailCause = 0;
        this.mAPNTypes = 0;
        this.mAPN = "";
        this.mLinkProperties = null;
        this.mState = in.readInt();
        this.mNetworkType = in.readInt();
        this.mAPNTypes = in.readInt();
        this.mAPN = in.readString();
        this.mLinkProperties = (LinkProperties) in.readParcelable(null);
        this.mFailCause = in.readInt();
    }

    public int getDataConnectionState() {
        return this.mState;
    }

    public int getDataConnectionNetworkType() {
        return this.mNetworkType;
    }

    public int getDataConnectionApnTypeBitMask() {
        return this.mAPNTypes;
    }

    public String getDataConnectionApn() {
        return this.mAPN;
    }

    @UnsupportedAppUsage
    public LinkProperties getDataConnectionLinkProperties() {
        return this.mLinkProperties;
    }

    public int getDataConnectionFailCause() {
        return this.mFailCause;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mState);
        out.writeInt(this.mNetworkType);
        out.writeInt(this.mAPNTypes);
        out.writeString(this.mAPN);
        out.writeParcelable(this.mLinkProperties, flags);
        out.writeInt(this.mFailCause);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mState), Integer.valueOf(this.mNetworkType), Integer.valueOf(this.mAPNTypes), this.mAPN, this.mLinkProperties, Integer.valueOf(this.mFailCause)});
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof PreciseDataConnectionState)) {
            return false;
        }
        PreciseDataConnectionState other = (PreciseDataConnectionState) obj;
        if (Objects.equals(this.mAPN, other.mAPN) && this.mAPNTypes == other.mAPNTypes && this.mFailCause == other.mFailCause && Objects.equals(this.mLinkProperties, other.mLinkProperties) && this.mNetworkType == other.mNetworkType && this.mState == other.mState) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Data Connection state: ");
        stringBuilder.append(this.mState);
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(", Network type: ");
        stringBuilder.append(this.mNetworkType);
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(", APN types: ");
        stringBuilder.append(ApnSetting.getApnTypesStringFromBitmask(this.mAPNTypes));
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(", APN: ");
        stringBuilder.append(this.mAPN);
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(", Link properties: ");
        stringBuilder.append(this.mLinkProperties);
        sb.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(", Fail cause: ");
        stringBuilder.append(DataFailCause.toString(this.mFailCause));
        sb.append(stringBuilder.toString());
        return sb.toString();
    }
}
