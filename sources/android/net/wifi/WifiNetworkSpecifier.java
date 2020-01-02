package android.net.wifi;

import android.app.ActivityThread;
import android.net.MacAddress;
import android.net.MatchAllNetworkSpecifier;
import android.net.NetworkSpecifier;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PatternMatcher;
import android.os.Process;
import android.text.TextUtils;
import android.util.Pair;
import com.android.internal.util.Preconditions;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public final class WifiNetworkSpecifier extends NetworkSpecifier implements Parcelable {
    public static final Creator<WifiNetworkSpecifier> CREATOR = new Creator<WifiNetworkSpecifier>() {
        public WifiNetworkSpecifier createFromParcel(Parcel in) {
            return new WifiNetworkSpecifier((PatternMatcher) in.readParcelable(null), Pair.create((MacAddress) in.readParcelable(null), (MacAddress) in.readParcelable(null)), (WifiConfiguration) in.readParcelable(null), in.readInt(), in.readString());
        }

        public WifiNetworkSpecifier[] newArray(int size) {
            return new WifiNetworkSpecifier[size];
        }
    };
    public final Pair<MacAddress, MacAddress> bssidPatternMatcher;
    public final String requestorPackageName;
    public final int requestorUid;
    public final PatternMatcher ssidPatternMatcher;
    public final WifiConfiguration wifiConfiguration;

    public static final class Builder {
        private static final Pair<MacAddress, MacAddress> MATCH_ALL_BSSID_PATTERN = new Pair(MacAddress.ALL_ZEROS_ADDRESS, MacAddress.ALL_ZEROS_ADDRESS);
        private static final String MATCH_ALL_SSID_PATTERN_PATH = ".*";
        private static final String MATCH_EMPTY_SSID_PATTERN_PATH = "";
        private static final MacAddress MATCH_EXACT_BSSID_PATTERN_MASK = MacAddress.BROADCAST_ADDRESS;
        private static final Pair<MacAddress, MacAddress> MATCH_NO_BSSID_PATTERN1 = new Pair(MacAddress.BROADCAST_ADDRESS, MacAddress.BROADCAST_ADDRESS);
        private static final Pair<MacAddress, MacAddress> MATCH_NO_BSSID_PATTERN2 = new Pair(MacAddress.ALL_ZEROS_ADDRESS, MacAddress.BROADCAST_ADDRESS);
        private Pair<MacAddress, MacAddress> mBssidPatternMatcher = null;
        private boolean mIsEnhancedOpen = false;
        private boolean mIsHiddenSSID = false;
        private PatternMatcher mSsidPatternMatcher = null;
        private WifiEnterpriseConfig mWpa2EnterpriseConfig = null;
        private String mWpa2PskPassphrase = null;
        private WifiEnterpriseConfig mWpa3EnterpriseConfig = null;
        private String mWpa3SaePassphrase = null;

        public Builder setSsidPattern(PatternMatcher ssidPattern) {
            Preconditions.checkNotNull(ssidPattern);
            this.mSsidPatternMatcher = ssidPattern;
            return this;
        }

        public Builder setSsid(String ssid) {
            Preconditions.checkNotNull(ssid);
            if (StandardCharsets.UTF_8.newEncoder().canEncode(ssid)) {
                this.mSsidPatternMatcher = new PatternMatcher(ssid, 0);
                return this;
            }
            throw new IllegalArgumentException("SSID is not a valid unicode string");
        }

        public Builder setBssidPattern(MacAddress baseAddress, MacAddress mask) {
            Preconditions.checkNotNull(baseAddress, mask);
            this.mBssidPatternMatcher = Pair.create(baseAddress, mask);
            return this;
        }

        public Builder setBssid(MacAddress bssid) {
            Preconditions.checkNotNull(bssid);
            this.mBssidPatternMatcher = Pair.create(bssid, MATCH_EXACT_BSSID_PATTERN_MASK);
            return this;
        }

        public Builder setIsEnhancedOpen(boolean isEnhancedOpen) {
            this.mIsEnhancedOpen = isEnhancedOpen;
            return this;
        }

        public Builder setWpa2Passphrase(String passphrase) {
            Preconditions.checkNotNull(passphrase);
            if (StandardCharsets.US_ASCII.newEncoder().canEncode(passphrase)) {
                this.mWpa2PskPassphrase = passphrase;
                return this;
            }
            throw new IllegalArgumentException("passphrase not ASCII encodable");
        }

        public Builder setWpa3Passphrase(String passphrase) {
            Preconditions.checkNotNull(passphrase);
            if (StandardCharsets.US_ASCII.newEncoder().canEncode(passphrase)) {
                this.mWpa3SaePassphrase = passphrase;
                return this;
            }
            throw new IllegalArgumentException("passphrase not ASCII encodable");
        }

        public Builder setWpa2EnterpriseConfig(WifiEnterpriseConfig enterpriseConfig) {
            Preconditions.checkNotNull(enterpriseConfig);
            this.mWpa2EnterpriseConfig = new WifiEnterpriseConfig(enterpriseConfig);
            return this;
        }

        public Builder setWpa3EnterpriseConfig(WifiEnterpriseConfig enterpriseConfig) {
            Preconditions.checkNotNull(enterpriseConfig);
            this.mWpa3EnterpriseConfig = new WifiEnterpriseConfig(enterpriseConfig);
            return this;
        }

        public Builder setIsHiddenSsid(boolean isHiddenSsid) {
            this.mIsHiddenSSID = isHiddenSsid;
            return this;
        }

        private void setSecurityParamsInWifiConfiguration(WifiConfiguration configuration) {
            String str = "\"";
            StringBuilder stringBuilder;
            if (!TextUtils.isEmpty(this.mWpa2PskPassphrase)) {
                configuration.setSecurityParams(2);
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(this.mWpa2PskPassphrase);
                stringBuilder.append(str);
                configuration.preSharedKey = stringBuilder.toString();
            } else if (!TextUtils.isEmpty(this.mWpa3SaePassphrase)) {
                configuration.setSecurityParams(4);
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(this.mWpa3SaePassphrase);
                stringBuilder.append(str);
                configuration.preSharedKey = stringBuilder.toString();
            } else if (this.mWpa2EnterpriseConfig != null) {
                configuration.setSecurityParams(3);
                configuration.enterpriseConfig = this.mWpa2EnterpriseConfig;
            } else if (this.mWpa3EnterpriseConfig != null) {
                configuration.setSecurityParams(5);
                configuration.enterpriseConfig = this.mWpa3EnterpriseConfig;
            } else if (this.mIsEnhancedOpen) {
                configuration.setSecurityParams(6);
            } else {
                configuration.setSecurityParams(0);
            }
        }

        private WifiConfiguration buildWifiConfiguration() {
            WifiConfiguration wifiConfiguration = new WifiConfiguration();
            if (this.mSsidPatternMatcher.getType() == 0) {
                StringBuilder stringBuilder = new StringBuilder();
                String str = "\"";
                stringBuilder.append(str);
                stringBuilder.append(this.mSsidPatternMatcher.getPath());
                stringBuilder.append(str);
                wifiConfiguration.SSID = stringBuilder.toString();
            }
            if (this.mBssidPatternMatcher.second == MATCH_EXACT_BSSID_PATTERN_MASK) {
                wifiConfiguration.BSSID = ((MacAddress) this.mBssidPatternMatcher.first).toString();
            }
            setSecurityParamsInWifiConfiguration(wifiConfiguration);
            wifiConfiguration.hiddenSSID = this.mIsHiddenSSID;
            return wifiConfiguration;
        }

        private boolean hasSetAnyPattern() {
            return (this.mSsidPatternMatcher == null && this.mBssidPatternMatcher == null) ? false : true;
        }

        private void setMatchAnyPatternIfUnset() {
            if (this.mSsidPatternMatcher == null) {
                this.mSsidPatternMatcher = new PatternMatcher(MATCH_ALL_SSID_PATTERN_PATH, 2);
            }
            if (this.mBssidPatternMatcher == null) {
                this.mBssidPatternMatcher = MATCH_ALL_BSSID_PATTERN;
            }
        }

        private boolean hasSetMatchNonePattern() {
            if ((this.mSsidPatternMatcher.getType() != 1 && this.mSsidPatternMatcher.getPath().equals("")) || this.mBssidPatternMatcher.equals(MATCH_NO_BSSID_PATTERN1) || this.mBssidPatternMatcher.equals(MATCH_NO_BSSID_PATTERN2)) {
                return true;
            }
            return false;
        }

        private boolean hasSetMatchAllPattern() {
            if (this.mSsidPatternMatcher.match("") && this.mBssidPatternMatcher.equals(MATCH_ALL_BSSID_PATTERN)) {
                return true;
            }
            return false;
        }

        private void validateSecurityParams() {
            int i = 0;
            int numSecurityTypes = (((0 + this.mIsEnhancedOpen) + (TextUtils.isEmpty(this.mWpa2PskPassphrase) ^ 1)) + (TextUtils.isEmpty(this.mWpa3SaePassphrase) ^ 1)) + (this.mWpa2EnterpriseConfig != null ? 1 : 0);
            if (this.mWpa3EnterpriseConfig != null) {
                i = 1;
            }
            if (numSecurityTypes + i > 1) {
                throw new IllegalStateException("only one of setIsEnhancedOpen, setWpa2Passphrase,setWpa3Passphrase, setWpa2EnterpriseConfig or setWpa3EnterpriseConfig can be invoked for network specifier");
            }
        }

        public WifiNetworkSpecifier build() {
            if (hasSetAnyPattern()) {
                setMatchAnyPatternIfUnset();
                if (hasSetMatchNonePattern()) {
                    throw new IllegalStateException("cannot set match-none pattern for specifier");
                } else if (hasSetMatchAllPattern()) {
                    throw new IllegalStateException("cannot set match-all pattern for specifier");
                } else if (!this.mIsHiddenSSID || this.mSsidPatternMatcher.getType() == 0) {
                    validateSecurityParams();
                    return new WifiNetworkSpecifier(this.mSsidPatternMatcher, this.mBssidPatternMatcher, buildWifiConfiguration(), Process.myUid(), ActivityThread.currentApplication().getApplicationContext().getOpPackageName());
                } else {
                    throw new IllegalStateException("setSsid should also be invoked when setIsHiddenSsid is invoked for network specifier");
                }
            }
            throw new IllegalStateException("one of setSsidPattern/setSsid/setBssidPattern/setBssid should be invoked for specifier");
        }
    }

    public WifiNetworkSpecifier() throws IllegalAccessException {
        throw new IllegalAccessException("Use the builder to create an instance");
    }

    public WifiNetworkSpecifier(PatternMatcher ssidPatternMatcher, Pair<MacAddress, MacAddress> bssidPatternMatcher, WifiConfiguration wifiConfiguration, int requestorUid, String requestorPackageName) {
        Preconditions.checkNotNull(ssidPatternMatcher);
        Preconditions.checkNotNull(bssidPatternMatcher);
        Preconditions.checkNotNull(wifiConfiguration);
        Preconditions.checkNotNull(requestorPackageName);
        this.ssidPatternMatcher = ssidPatternMatcher;
        this.bssidPatternMatcher = bssidPatternMatcher;
        this.wifiConfiguration = wifiConfiguration;
        this.requestorUid = requestorUid;
        this.requestorPackageName = requestorPackageName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.ssidPatternMatcher, flags);
        dest.writeParcelable((Parcelable) this.bssidPatternMatcher.first, flags);
        dest.writeParcelable((Parcelable) this.bssidPatternMatcher.second, flags);
        dest.writeParcelable(this.wifiConfiguration, flags);
        dest.writeInt(this.requestorUid);
        dest.writeString(this.requestorPackageName);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.ssidPatternMatcher.getPath(), Integer.valueOf(this.ssidPatternMatcher.getType()), this.bssidPatternMatcher, this.wifiConfiguration.allowedKeyManagement, Integer.valueOf(this.requestorUid), this.requestorPackageName});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof WifiNetworkSpecifier)) {
            return false;
        }
        WifiNetworkSpecifier lhs = (WifiNetworkSpecifier) obj;
        if (!(Objects.equals(this.ssidPatternMatcher.getPath(), lhs.ssidPatternMatcher.getPath()) && Objects.equals(Integer.valueOf(this.ssidPatternMatcher.getType()), Integer.valueOf(lhs.ssidPatternMatcher.getType())) && Objects.equals(this.bssidPatternMatcher, lhs.bssidPatternMatcher) && Objects.equals(this.wifiConfiguration.allowedKeyManagement, lhs.wifiConfiguration.allowedKeyManagement) && this.requestorUid == lhs.requestorUid && TextUtils.equals(this.requestorPackageName, lhs.requestorPackageName))) {
            z = false;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("WifiNetworkSpecifier [");
        stringBuilder.append(", SSID Match pattern=");
        stringBuilder.append(this.ssidPatternMatcher);
        stringBuilder.append(", BSSID Match pattern=");
        stringBuilder.append(this.bssidPatternMatcher);
        stringBuilder.append(", SSID=");
        stringBuilder.append(this.wifiConfiguration.SSID);
        stringBuilder.append(", BSSID=");
        stringBuilder.append(this.wifiConfiguration.BSSID);
        stringBuilder.append(", requestorUid=");
        stringBuilder.append(this.requestorUid);
        stringBuilder.append(", requestorPackageName=");
        stringBuilder.append(this.requestorPackageName);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public boolean satisfiedBy(NetworkSpecifier other) {
        if (this == other || other == null || (other instanceof MatchAllNetworkSpecifier)) {
            return true;
        }
        if (other instanceof WifiNetworkAgentSpecifier) {
            return ((WifiNetworkAgentSpecifier) other).satisfiesNetworkSpecifier(this);
        }
        return equals(other);
    }

    public void assertValidFromUid(int requestorUid) {
        if (this.requestorUid != requestorUid) {
            throw new SecurityException("mismatched UIDs");
        }
    }
}
