package android.net;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@Deprecated
public class NetworkQuotaInfo implements Parcelable {
    @UnsupportedAppUsage
    public static final Creator<NetworkQuotaInfo> CREATOR = new Creator<NetworkQuotaInfo>() {
        public NetworkQuotaInfo createFromParcel(Parcel in) {
            return new NetworkQuotaInfo(in);
        }

        public NetworkQuotaInfo[] newArray(int size) {
            return new NetworkQuotaInfo[size];
        }
    };
    public static final long NO_LIMIT = -1;

    public NetworkQuotaInfo(Parcel in) {
    }

    @UnsupportedAppUsage
    public long getEstimatedBytes() {
        return 0;
    }

    @UnsupportedAppUsage
    public long getSoftLimitBytes() {
        return -1;
    }

    @UnsupportedAppUsage
    public long getHardLimitBytes() {
        return -1;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
    }
}
