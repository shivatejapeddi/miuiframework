package android.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.internal.telephony.IccCardConstants;
import java.util.Objects;

public class LocationPolicy implements Parcelable {
    public static final Creator<LocationPolicy> CREATOR = new Creator<LocationPolicy>() {
        public LocationPolicy createFromParcel(Parcel in) {
            return new LocationPolicy(in, null);
        }

        public LocationPolicy[] newArray(int size) {
            return new LocationPolicy[size];
        }
    };
    public static final int MATCH_FUSED_PROVIDER = 3;
    public static final int MATCH_GPS_PROVIDER = 2;
    public static final int MATCH_NETWORK_PROVIDER = 1;
    public static final int MATCH_PASSIVE_PROVIDER = 4;
    public final boolean mHighCost;
    private final int mMatchRule;
    public final int mMinIntervalMs;
    public final String mProvider;

    /* synthetic */ LocationPolicy(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public static LocationPolicy buildDefaultPolicyNetworkLocation() {
        return new LocationPolicy(1, LocationManager.NETWORK_PROVIDER, false);
    }

    public static LocationPolicy buildDefaultPolicyGPSLocation() {
        return new LocationPolicy(2, LocationManager.GPS_PROVIDER, true);
    }

    public static LocationPolicy buildDefaultPolicyFusedLocation() {
        return new LocationPolicy(3, LocationManager.FUSED_PROVIDER, true);
    }

    public static LocationPolicy buildDefaultPolicyPassiveLocation() {
        return new LocationPolicy(4, LocationManager.PASSIVE_PROVIDER, false);
    }

    public static LocationPolicy getLocationPolicy(String provider, int minIntervalMs) {
        String str = LocationManager.NETWORK_PROVIDER;
        if (provider.equals(str)) {
            return new LocationPolicy(1, str, false, minIntervalMs);
        }
        str = LocationManager.GPS_PROVIDER;
        if (provider.equals(str)) {
            return new LocationPolicy(2, str, true, minIntervalMs);
        }
        str = LocationManager.PASSIVE_PROVIDER;
        if (provider.equals(str)) {
            return new LocationPolicy(4, str, false, minIntervalMs);
        }
        str = LocationManager.FUSED_PROVIDER;
        if (provider.equals(str)) {
            return new LocationPolicy(3, str, true, minIntervalMs);
        }
        throw new IllegalArgumentException("unknown location provider");
    }

    public LocationPolicy(int matchRule, String provider, boolean highcost) {
        this.mMatchRule = matchRule;
        this.mProvider = provider;
        this.mHighCost = highcost;
        this.mMinIntervalMs = 0;
    }

    public LocationPolicy(int matchRule, String provider, boolean highcost, int minIntervalMs) {
        this.mMatchRule = matchRule;
        this.mProvider = provider;
        this.mHighCost = highcost;
        this.mMinIntervalMs = minIntervalMs;
    }

    private LocationPolicy(Parcel in) {
        this.mMatchRule = in.readInt();
        this.mProvider = in.readString();
        this.mHighCost = in.readInt() != 0;
        this.mMinIntervalMs = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mMatchRule);
        dest.writeString(this.mProvider);
        dest.writeInt(this.mHighCost);
        dest.writeInt(this.mMinIntervalMs);
    }

    public int describeContents() {
        return 0;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("LocationPolicy: ");
        builder.append("matchRule=");
        builder.append(getMatchRuleName(this.mMatchRule));
        builder.append(", provider=");
        builder.append(this.mProvider);
        builder.append(", highCost=");
        builder.append(this.mHighCost);
        builder.append(", minInterval=");
        builder.append(this.mMinIntervalMs);
        return builder.toString();
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mMatchRule), Boolean.valueOf(this.mHighCost), Integer.valueOf(this.mMinIntervalMs)});
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof LocationPolicy)) {
            return false;
        }
        LocationPolicy other = (LocationPolicy) obj;
        if (this.mMatchRule == other.mMatchRule && this.mProvider == other.mProvider && this.mHighCost == other.mHighCost && this.mMinIntervalMs == other.mMinIntervalMs) {
            z = true;
        }
        return z;
    }

    public int getMatchRule() {
        return this.mMatchRule;
    }

    public boolean matches(String locationProvider) {
        int i = this.mMatchRule;
        if (i == 1) {
            return locationProvider.equals(LocationManager.NETWORK_PROVIDER);
        }
        if (i == 2) {
            return locationProvider.equals(LocationManager.GPS_PROVIDER);
        }
        if (i == 3) {
            return locationProvider.equals(LocationManager.FUSED_PROVIDER);
        }
        if (i == 4) {
            return locationProvider.equals(LocationManager.PASSIVE_PROVIDER);
        }
        throw new IllegalArgumentException("unknown location provider");
    }

    private static String getMatchRuleName(int matchRule) {
        if (matchRule == 1) {
            return "NETWORK_PROVIDER";
        }
        if (matchRule == 2) {
            return "GPS_PROVIDER";
        }
        if (matchRule == 3) {
            return "FUSED_PROVIDER";
        }
        if (matchRule != 4) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        return "PASSIVE_PROVIDER";
    }
}
