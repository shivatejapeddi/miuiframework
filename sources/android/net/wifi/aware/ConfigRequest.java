package android.net.wifi.aware;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public final class ConfigRequest implements Parcelable {
    public static final int CLUSTER_ID_MAX = 65535;
    public static final int CLUSTER_ID_MIN = 0;
    public static final Creator<ConfigRequest> CREATOR = new Creator<ConfigRequest>() {
        public ConfigRequest[] newArray(int size) {
            return new ConfigRequest[size];
        }

        public ConfigRequest createFromParcel(Parcel in) {
            return new ConfigRequest(in.readInt() != 0, in.readInt(), in.readInt(), in.readInt(), in.createIntArray(), null);
        }
    };
    public static final int DW_DISABLE = 0;
    public static final int DW_INTERVAL_NOT_INIT = -1;
    public static final int NAN_BAND_24GHZ = 0;
    public static final int NAN_BAND_5GHZ = 1;
    public final int mClusterHigh;
    public final int mClusterLow;
    public final int[] mDiscoveryWindowInterval;
    public final int mMasterPreference;
    public final boolean mSupport5gBand;

    public static final class Builder {
        private int mClusterHigh = 65535;
        private int mClusterLow = 0;
        private int[] mDiscoveryWindowInterval = new int[]{-1, -1};
        private int mMasterPreference = 0;
        private boolean mSupport5gBand = true;

        public Builder setSupport5gBand(boolean support5gBand) {
            this.mSupport5gBand = support5gBand;
            return this;
        }

        public Builder setMasterPreference(int masterPreference) {
            if (masterPreference < 0) {
                throw new IllegalArgumentException("Master Preference specification must be non-negative");
            } else if (masterPreference == 1 || masterPreference == 255 || masterPreference > 255) {
                throw new IllegalArgumentException("Master Preference specification must not exceed 255 or use 1 or 255 (reserved values)");
            } else {
                this.mMasterPreference = masterPreference;
                return this;
            }
        }

        public Builder setClusterLow(int clusterLow) {
            if (clusterLow < 0) {
                throw new IllegalArgumentException("Cluster specification must be non-negative");
            } else if (clusterLow <= 65535) {
                this.mClusterLow = clusterLow;
                return this;
            } else {
                throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
            }
        }

        public Builder setClusterHigh(int clusterHigh) {
            if (clusterHigh < 0) {
                throw new IllegalArgumentException("Cluster specification must be non-negative");
            } else if (clusterHigh <= 65535) {
                this.mClusterHigh = clusterHigh;
                return this;
            } else {
                throw new IllegalArgumentException("Cluster specification must not exceed 0xFFFF");
            }
        }

        public Builder setDiscoveryWindowInterval(int band, int interval) {
            if (band != 0 && band != 1) {
                throw new IllegalArgumentException("Invalid band value");
            } else if ((band != 0 || (interval >= 1 && interval <= 5)) && (band != 1 || (interval >= 0 && interval <= 5))) {
                this.mDiscoveryWindowInterval[band] = interval;
                return this;
            } else {
                throw new IllegalArgumentException("Invalid interval value: 2.4 GHz [1,5] or 5GHz [0,5]");
            }
        }

        public ConfigRequest build() {
            int i = this.mClusterLow;
            int i2 = this.mClusterHigh;
            if (i <= i2) {
                return new ConfigRequest(this.mSupport5gBand, this.mMasterPreference, i, i2, this.mDiscoveryWindowInterval, null);
            }
            throw new IllegalArgumentException("Invalid argument combination - must have Cluster Low <= Cluster High");
        }
    }

    /* synthetic */ ConfigRequest(boolean x0, int x1, int x2, int x3, int[] x4, AnonymousClass1 x5) {
        this(x0, x1, x2, x3, x4);
    }

    private ConfigRequest(boolean support5gBand, int masterPreference, int clusterLow, int clusterHigh, int[] discoveryWindowInterval) {
        this.mSupport5gBand = support5gBand;
        this.mMasterPreference = masterPreference;
        this.mClusterLow = clusterLow;
        this.mClusterHigh = clusterHigh;
        this.mDiscoveryWindowInterval = discoveryWindowInterval;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ConfigRequest [mSupport5gBand=");
        stringBuilder.append(this.mSupport5gBand);
        stringBuilder.append(", mMasterPreference=");
        stringBuilder.append(this.mMasterPreference);
        stringBuilder.append(", mClusterLow=");
        stringBuilder.append(this.mClusterLow);
        stringBuilder.append(", mClusterHigh=");
        stringBuilder.append(this.mClusterHigh);
        stringBuilder.append(", mDiscoveryWindowInterval=");
        stringBuilder.append(Arrays.toString(this.mDiscoveryWindowInterval));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mSupport5gBand);
        dest.writeInt(this.mMasterPreference);
        dest.writeInt(this.mClusterLow);
        dest.writeInt(this.mClusterHigh);
        dest.writeIntArray(this.mDiscoveryWindowInterval);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigRequest)) {
            return false;
        }
        ConfigRequest lhs = (ConfigRequest) o;
        if (!(this.mSupport5gBand == lhs.mSupport5gBand && this.mMasterPreference == lhs.mMasterPreference && this.mClusterLow == lhs.mClusterLow && this.mClusterHigh == lhs.mClusterHigh && Arrays.equals(this.mDiscoveryWindowInterval, lhs.mDiscoveryWindowInterval))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return (((((((((17 * 31) + this.mSupport5gBand) * 31) + this.mMasterPreference) * 31) + this.mClusterLow) * 31) + this.mClusterHigh) * 31) + Arrays.hashCode(this.mDiscoveryWindowInterval);
    }

    public void validate() throws IllegalArgumentException {
        int i = this.mMasterPreference;
        if (i < 0) {
            throw new IllegalArgumentException("Master Preference specification must be non-negative");
        } else if (i == 1 || i == 255 || i > 255) {
            throw new IllegalArgumentException("Master Preference specification must not exceed 255 or use 1 or 255 (reserved values)");
        } else {
            i = this.mClusterLow;
            String str = "Cluster specification must be non-negative";
            if (i >= 0) {
                String str2 = "Cluster specification must not exceed 0xFFFF";
                if (i <= 65535) {
                    int i2 = this.mClusterHigh;
                    if (i2 < 0) {
                        throw new IllegalArgumentException(str);
                    } else if (i2 > 65535) {
                        throw new IllegalArgumentException(str2);
                    } else if (i <= i2) {
                        int[] iArr = this.mDiscoveryWindowInterval;
                        if (iArr.length != 2) {
                            throw new IllegalArgumentException("Invalid discovery window interval: must have 2 elements (2.4 & 5");
                        } else if (iArr[0] == -1 || (iArr[0] >= 1 && iArr[0] <= 5)) {
                            iArr = this.mDiscoveryWindowInterval;
                            if (iArr[1] == -1) {
                                return;
                            }
                            if (iArr[1] < 0 || iArr[1] > 5) {
                                throw new IllegalArgumentException("Invalid discovery window interval for 5GHz: valid is UNSET or [0,5]");
                            }
                            return;
                        } else {
                            throw new IllegalArgumentException("Invalid discovery window interval for 2.4GHz: valid is UNSET or [1,5]");
                        }
                    } else {
                        throw new IllegalArgumentException("Invalid argument combination - must have Cluster Low <= Cluster High");
                    }
                }
                throw new IllegalArgumentException(str2);
            }
            throw new IllegalArgumentException(str);
        }
    }
}
