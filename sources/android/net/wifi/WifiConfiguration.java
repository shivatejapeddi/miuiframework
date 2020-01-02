package android.net.wifi;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.net.IpConfiguration;
import android.net.IpConfiguration.IpAssignment;
import android.net.IpConfiguration.ProxySettings;
import android.net.MacAddress;
import android.net.ProxyInfo;
import android.net.StaticIpConfiguration;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.Telephony.Carriers;
import android.security.keystore.KeyProperties;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.util.BackupUtils;
import android.util.BackupUtils.BadVersionException;
import android.util.Log;
import android.util.TimeUtils;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashMap;

@Deprecated
public class WifiConfiguration implements Parcelable {
    public static final int AP_BAND_2GHZ = 0;
    public static final int AP_BAND_5GHZ = 1;
    public static final int AP_BAND_ANY = -1;
    public static final int AP_BAND_DUAL = 2;
    private static final int BACKUP_VERSION = 3;
    @UnsupportedAppUsage
    public static final Creator<WifiConfiguration> CREATOR = new Creator<WifiConfiguration>() {
        public WifiConfiguration createFromParcel(Parcel in) {
            int i;
            WifiConfiguration config = new WifiConfiguration();
            config.networkId = in.readInt();
            config.status = in.readInt();
            config.mNetworkSelectionStatus.readFromParcel(in);
            config.SSID = in.readString();
            config.BSSID = in.readString();
            boolean z = false;
            config.shareThisAp = in.readInt() != 0;
            config.apBand = in.readInt();
            config.apChannel = in.readInt();
            config.FQDN = in.readString();
            config.providerFriendlyName = in.readString();
            config.isHomeProviderNetwork = in.readInt() != 0;
            int numRoamingConsortiumIds = in.readInt();
            config.roamingConsortiumIds = new long[numRoamingConsortiumIds];
            for (i = 0; i < numRoamingConsortiumIds; i++) {
                config.roamingConsortiumIds[i] = in.readLong();
            }
            config.preSharedKey = in.readString();
            for (i = 0; i < config.wepKeys.length; i++) {
                config.wepKeys[i] = in.readString();
            }
            config.wepTxKeyIndex = in.readInt();
            config.priority = in.readInt();
            config.hiddenSSID = in.readInt() != 0;
            config.requirePMF = in.readInt() != 0;
            config.updateIdentifier = in.readString();
            config.allowedKeyManagement = WifiConfiguration.readBitSet(in);
            config.allowedProtocols = WifiConfiguration.readBitSet(in);
            config.allowedAuthAlgorithms = WifiConfiguration.readBitSet(in);
            config.allowedPairwiseCiphers = WifiConfiguration.readBitSet(in);
            config.allowedGroupCiphers = WifiConfiguration.readBitSet(in);
            config.allowedGroupManagementCiphers = WifiConfiguration.readBitSet(in);
            config.allowedSuiteBCiphers = WifiConfiguration.readBitSet(in);
            if (config.allowedKeyManagement.get(190)) {
                config.wapiPskType = in.readInt();
                config.wapiPsk = in.readString();
            } else if (config.allowedKeyManagement.get(191)) {
                config.wapiCertSelMode = in.readInt();
                config.wapiCertSel = in.readString();
            }
            config.enterpriseConfig = (WifiEnterpriseConfig) in.readParcelable(null);
            config.setIpConfiguration((IpConfiguration) in.readParcelable(null));
            config.dhcpServer = in.readString();
            config.defaultGwMacAddress = in.readString();
            config.selfAdded = in.readInt() != 0;
            config.didSelfAdd = in.readInt() != 0;
            config.validatedInternetAccess = in.readInt() != 0;
            config.isLegacyPasspointConfig = in.readInt() != 0;
            config.ephemeral = in.readInt() != 0;
            config.trusted = in.readInt() != 0;
            config.fromWifiNetworkSuggestion = in.readInt() != 0;
            config.fromWifiNetworkSpecifier = in.readInt() != 0;
            config.meteredHint = in.readInt() != 0;
            config.meteredOverride = in.readInt();
            config.useExternalScores = in.readInt() != 0;
            config.creatorUid = in.readInt();
            config.lastConnectUid = in.readInt();
            config.lastUpdateUid = in.readInt();
            config.creatorName = in.readString();
            config.lastUpdateName = in.readString();
            config.numScorerOverride = in.readInt();
            config.numScorerOverrideAndSwitchedNetwork = in.readInt();
            config.numAssociation = in.readInt();
            config.userApproved = in.readInt();
            config.numNoInternetAccessReports = in.readInt();
            config.noInternetAccessExpected = in.readInt() != 0;
            config.shared = in.readInt() != 0;
            config.mPasspointManagementObjectTree = in.readString();
            config.recentFailure.setAssociationStatus(in.readInt());
            config.mRandomizedMacAddress = (MacAddress) in.readParcelable(null);
            config.dppConnector = in.readString();
            config.dppNetAccessKey = in.readString();
            config.dppNetAccessKeyExpiry = in.readInt();
            config.dppCsign = in.readString();
            config.macRandomizationSetting = in.readInt();
            if (in.readInt() != 0) {
                z = true;
            }
            config.osu = z;
            config.oweTransIfaceName = in.readString();
            return config;
        }

        public WifiConfiguration[] newArray(int size) {
            return new WifiConfiguration[size];
        }
    };
    public static final int HOME_NETWORK_RSSI_BOOST = 5;
    public static final int INVALID_NETWORK_ID = -1;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static int INVALID_RSSI = -127;
    public static final int LOCAL_ONLY_NETWORK_ID = -2;
    private static final int MAXIMUM_RANDOM_MAC_GENERATION_RETRY = 3;
    public static final int METERED_OVERRIDE_METERED = 1;
    public static final int METERED_OVERRIDE_NONE = 0;
    public static final int METERED_OVERRIDE_NOT_METERED = 2;
    public static final int RANDOMIZATION_NONE = 0;
    public static final int RANDOMIZATION_PERSISTENT = 1;
    public static final int SECURITY_TYPE_EAP = 3;
    public static final int SECURITY_TYPE_EAP_SUITE_B = 5;
    public static final int SECURITY_TYPE_FILS_SHA256 = 7;
    public static final int SECURITY_TYPE_FILS_SHA384 = 8;
    public static final int SECURITY_TYPE_OPEN = 0;
    public static final int SECURITY_TYPE_OWE = 6;
    public static final int SECURITY_TYPE_PSK = 2;
    public static final int SECURITY_TYPE_SAE = 4;
    public static final int SECURITY_TYPE_WAPI_CERT = 13;
    public static final int SECURITY_TYPE_WAPI_PSK = 12;
    public static final int SECURITY_TYPE_WEP = 1;
    public static final String SIMNumVarName = "sim_num";
    private static final String TAG = "WifiConfiguration";
    public static final int UNKNOWN_UID = -1;
    public static final int USER_APPROVED = 1;
    public static final int USER_BANNED = 2;
    public static final int USER_PENDING = 3;
    public static final int USER_UNSPECIFIED = 0;
    public static final int WAPI_PSK_TYPE_ASCII = 0;
    public static final int WAPI_PSK_TYPE_HEX = 1;
    public static final String bssidVarName = "bssid";
    public static final String hiddenSSIDVarName = "scan_ssid";
    public static final String pmfVarName = "ieee80211w";
    public static final String priorityVarName = "priority";
    public static final String pskVarName = "psk";
    public static final String shareThisApVarName = "share_this_ap";
    public static final String ssidVarName = "ssid";
    public static final String updateIdentiferVarName = "update_identifier";
    public static final String wapiCertSelModeVarName = "wapi_user_cert_mode";
    public static final String wapiCertSelVarName = "wapi_user_sel_cert";
    public static final String wapiPskTypeVarName = "psk_key_type";
    public static final String wapiPskVarName = "wapi_psk";
    @Deprecated
    @UnsupportedAppUsage
    public static final String[] wepKeyVarNames = new String[]{"wep_key0", "wep_key1", "wep_key2", "wep_key3"};
    @Deprecated
    public static final String wepTxKeyIdxVarName = "wep_tx_keyidx";
    public String BSSID;
    public String FQDN;
    public String SSID;
    public BitSet allowedAuthAlgorithms;
    public BitSet allowedGroupCiphers;
    public BitSet allowedGroupManagementCiphers;
    public BitSet allowedKeyManagement;
    public BitSet allowedPairwiseCiphers;
    public BitSet allowedProtocols;
    public BitSet allowedSuiteBCiphers;
    @UnsupportedAppUsage
    public int apBand;
    @UnsupportedAppUsage
    public int apChannel;
    public String creationTime;
    @SystemApi
    public String creatorName;
    @SystemApi
    public int creatorUid;
    @UnsupportedAppUsage
    public String defaultGwMacAddress;
    public String dhcpServer;
    public boolean didSelfAdd;
    public String dppConnector;
    public String dppCsign;
    public String dppNetAccessKey;
    public int dppNetAccessKeyExpiry;
    public int dtimInterval;
    public WifiEnterpriseConfig enterpriseConfig;
    public boolean ephemeral;
    public boolean fromWifiNetworkSpecifier;
    public boolean fromWifiNetworkSuggestion;
    public boolean hiddenSSID;
    public boolean isHomeProviderNetwork;
    public boolean isLegacyPasspointConfig;
    @UnsupportedAppUsage
    public int lastConnectUid;
    public long lastConnected;
    public long lastDisconnected;
    @SystemApi
    public String lastUpdateName;
    @SystemApi
    public int lastUpdateUid;
    public HashMap<String, Integer> linkedConfigurations;
    String mCachedConfigKey;
    @UnsupportedAppUsage
    private IpConfiguration mIpConfiguration;
    private NetworkSelectionStatus mNetworkSelectionStatus;
    private String mPasspointManagementObjectTree;
    private MacAddress mRandomizedMacAddress;
    public int macRandomizationSetting;
    @SystemApi
    public boolean meteredHint;
    public int meteredOverride;
    public int networkId;
    @UnsupportedAppUsage
    public boolean noInternetAccessExpected;
    @SystemApi
    public int numAssociation;
    @UnsupportedAppUsage
    public int numNoInternetAccessReports;
    @SystemApi
    public int numScorerOverride;
    @SystemApi
    public int numScorerOverrideAndSwitchedNetwork;
    public boolean osu;
    public String oweTransIfaceName;
    public String peerWifiConfiguration;
    public String preSharedKey;
    @Deprecated
    public int priority;
    public String providerFriendlyName;
    public final RecentFailure recentFailure;
    public boolean requirePMF;
    public long[] roamingConsortiumIds;
    @UnsupportedAppUsage
    public boolean selfAdded;
    public boolean shareThisAp;
    @UnsupportedAppUsage
    public boolean shared;
    public int status;
    public boolean trusted;
    public String updateIdentifier;
    public String updateTime;
    @SystemApi
    public boolean useExternalScores;
    public int userApproved;
    @UnsupportedAppUsage
    public boolean validatedInternetAccess;
    public String wapiCertSel;
    public int wapiCertSelMode;
    public String wapiPsk;
    public int wapiPskType;
    @Deprecated
    public String[] wepKeys;
    @Deprecated
    public int wepTxKeyIndex;

    public static class AuthAlgorithm {
        public static final int LEAP = 2;
        public static final int OPEN = 0;
        @Deprecated
        public static final int SHARED = 1;
        public static final String[] strings = new String[]{"OPEN", "SHARED", "LEAP"};
        public static final String varName = "auth_alg";

        private AuthAlgorithm() {
        }
    }

    public static class GroupCipher {
        public static final int CCMP = 3;
        public static final int GCMP_256 = 5;
        public static final int GTK_NOT_USED = 4;
        public static final int TKIP = 2;
        @Deprecated
        public static final int WEP104 = 1;
        @Deprecated
        public static final int WEP40 = 0;
        public static final String[] strings = new String[]{"WEP40", "WEP104", "TKIP", "CCMP", "GTK_NOT_USED", "GCMP_256"};
        public static final String varName = "group";

        private GroupCipher() {
        }
    }

    public static class GroupMgmtCipher {
        public static final int BIP_CMAC_256 = 0;
        public static final int BIP_GMAC_128 = 1;
        public static final int BIP_GMAC_256 = 2;
        private static final String[] strings = new String[]{"BIP_CMAC_256", "BIP_GMAC_128", "BIP_GMAC_256"};
        private static final String varName = "groupMgmt";

        private GroupMgmtCipher() {
        }
    }

    public static class KeyMgmt {
        public static final int DPP = 15;
        public static final int FILS_SHA256 = 13;
        public static final int FILS_SHA384 = 14;
        public static final int FT_EAP = 7;
        public static final int FT_PSK = 6;
        public static final int IEEE8021X = 3;
        public static final int NONE = 0;
        public static final int OSEN = 5;
        public static final int OWE = 9;
        public static final int SAE = 8;
        public static final int SUITE_B_192 = 10;
        public static final int WAPI_CERT = 191;
        public static final int WAPI_PSK = 190;
        @SystemApi
        public static final int WPA2_PSK = 4;
        public static final int WPA_EAP = 2;
        public static final int WPA_EAP_SHA256 = 12;
        public static final int WPA_PSK = 1;
        public static final int WPA_PSK_SHA256 = 11;
        public static final String[] strings = new String[]{KeyProperties.DIGEST_NONE, "WPA_PSK", "WPA_EAP", "IEEE8021X", "WPA2_PSK", "OSEN", "FT_PSK", "FT_EAP", "SAE", "OWE", "SUITE_B_192", "WPA_PSK_SHA256", "WPA_EAP_SHA256", "FILS_SHA256", "FILS_SHA384", "DPP"};
        public static final String varName = "key_mgmt";
        public static final String wapi_cert_string = "WAPI_CERT";
        public static final String wapi_psk_string = "WAPI_PSK";

        private KeyMgmt() {
        }
    }

    public static class NetworkSelectionStatus {
        private static final int CONNECT_CHOICE_EXISTS = 1;
        private static final int CONNECT_CHOICE_NOT_EXISTS = -1;
        public static final int DISABLED_ASSOCIATION_REJECTION = 2;
        public static final int DISABLED_AUTHENTICATION_FAILURE = 3;
        public static final int DISABLED_AUTHENTICATION_NO_CREDENTIALS = 9;
        public static final int DISABLED_AUTHENTICATION_NO_SUBSCRIPTION = 14;
        public static final int DISABLED_BAD_LINK = 1;
        public static final int DISABLED_BY_WIFI_MANAGER = 11;
        public static final int DISABLED_BY_WRONG_PASSWORD = 13;
        public static final int DISABLED_DHCP_FAILURE = 4;
        public static final int DISABLED_DNS_FAILURE = 5;
        public static final int DISABLED_DUE_TO_USER_SWITCH = 12;
        public static final int DISABLED_NO_INTERNET_PERMANENT = 10;
        public static final int DISABLED_NO_INTERNET_TEMPORARY = 6;
        public static final int DISABLED_TLS_VERSION_MISMATCH = 8;
        public static final int DISABLED_WPS_START = 7;
        public static final long INVALID_NETWORK_SELECTION_DISABLE_TIMESTAMP = -1;
        public static final int NETWORK_SELECTION_DISABLED_MAX = 15;
        public static final int NETWORK_SELECTION_DISABLED_STARTING_INDEX = 1;
        public static final int NETWORK_SELECTION_ENABLE = 0;
        public static final int NETWORK_SELECTION_ENABLED = 0;
        public static final int NETWORK_SELECTION_PERMANENTLY_DISABLED = 2;
        public static final int NETWORK_SELECTION_STATUS_MAX = 3;
        public static final int NETWORK_SELECTION_TEMPORARY_DISABLED = 1;
        public static final String[] QUALITY_NETWORK_SELECTION_DISABLE_REASON = new String[]{"NETWORK_SELECTION_ENABLE", "NETWORK_SELECTION_DISABLED_BAD_LINK", "NETWORK_SELECTION_DISABLED_ASSOCIATION_REJECTION ", "NETWORK_SELECTION_DISABLED_AUTHENTICATION_FAILURE", "NETWORK_SELECTION_DISABLED_DHCP_FAILURE", "NETWORK_SELECTION_DISABLED_DNS_FAILURE", "NETWORK_SELECTION_DISABLED_NO_INTERNET_TEMPORARY", "NETWORK_SELECTION_DISABLED_WPS_START", "NETWORK_SELECTION_DISABLED_TLS_VERSION", "NETWORK_SELECTION_DISABLED_AUTHENTICATION_NO_CREDENTIALS", "NETWORK_SELECTION_DISABLED_NO_INTERNET_PERMANENT", "NETWORK_SELECTION_DISABLED_BY_WIFI_MANAGER", "NETWORK_SELECTION_DISABLED_BY_USER_SWITCH", "NETWORK_SELECTION_DISABLED_BY_WRONG_PASSWORD", "NETWORK_SELECTION_DISABLED_AUTHENTICATION_NO_SUBSCRIPTION"};
        public static final String[] QUALITY_NETWORK_SELECTION_STATUS = new String[]{"NETWORK_SELECTION_ENABLED", "NETWORK_SELECTION_TEMPORARY_DISABLED", "NETWORK_SELECTION_PERMANENTLY_DISABLED"};
        private ScanResult mCandidate;
        private int mCandidateScore;
        private String mConnectChoice;
        private long mConnectChoiceTimestamp = -1;
        private boolean mHasEverConnected = false;
        private int[] mNetworkSeclectionDisableCounter = new int[15];
        private String mNetworkSelectionBSSID;
        private int mNetworkSelectionDisableReason;
        private boolean mNotRecommended;
        private boolean mSeenInLastQualifiedNetworkSelection;
        private int mStatus;
        private long mTemporarilyDisabledTimestamp = -1;

        public void setNotRecommended(boolean notRecommended) {
            this.mNotRecommended = notRecommended;
        }

        public boolean isNotRecommended() {
            return this.mNotRecommended;
        }

        public void setSeenInLastQualifiedNetworkSelection(boolean seen) {
            this.mSeenInLastQualifiedNetworkSelection = seen;
        }

        public boolean getSeenInLastQualifiedNetworkSelection() {
            return this.mSeenInLastQualifiedNetworkSelection;
        }

        public void setCandidate(ScanResult scanCandidate) {
            this.mCandidate = scanCandidate;
        }

        public ScanResult getCandidate() {
            return this.mCandidate;
        }

        public void setCandidateScore(int score) {
            this.mCandidateScore = score;
        }

        public int getCandidateScore() {
            return this.mCandidateScore;
        }

        public String getConnectChoice() {
            return this.mConnectChoice;
        }

        public void setConnectChoice(String newConnectChoice) {
            this.mConnectChoice = newConnectChoice;
        }

        public long getConnectChoiceTimestamp() {
            return this.mConnectChoiceTimestamp;
        }

        public void setConnectChoiceTimestamp(long timeStamp) {
            this.mConnectChoiceTimestamp = timeStamp;
        }

        public String getNetworkStatusString() {
            return QUALITY_NETWORK_SELECTION_STATUS[this.mStatus];
        }

        public void setHasEverConnected(boolean value) {
            this.mHasEverConnected = value;
        }

        public boolean getHasEverConnected() {
            return this.mHasEverConnected;
        }

        public static String getNetworkDisableReasonString(int reason) {
            if (reason < 0 || reason >= 15) {
                return null;
            }
            return QUALITY_NETWORK_SELECTION_DISABLE_REASON[reason];
        }

        public String getNetworkDisableReasonString() {
            return QUALITY_NETWORK_SELECTION_DISABLE_REASON[this.mNetworkSelectionDisableReason];
        }

        public int getNetworkSelectionStatus() {
            return this.mStatus;
        }

        public boolean isNetworkEnabled() {
            return this.mStatus == 0;
        }

        public boolean isNetworkTemporaryDisabled() {
            return this.mStatus == 1;
        }

        public boolean isNetworkPermanentlyDisabled() {
            return this.mStatus == 2;
        }

        public void setNetworkSelectionStatus(int status) {
            if (status >= 0 && status < 3) {
                this.mStatus = status;
            }
        }

        public int getNetworkSelectionDisableReason() {
            return this.mNetworkSelectionDisableReason;
        }

        public void setNetworkSelectionDisableReason(int reason) {
            if (reason < 0 || reason >= 15) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Illegal reason value: ");
                stringBuilder.append(reason);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            this.mNetworkSelectionDisableReason = reason;
        }

        public boolean isDisabledByReason(int reason) {
            return this.mNetworkSelectionDisableReason == reason;
        }

        public void setDisableTime(long timeStamp) {
            this.mTemporarilyDisabledTimestamp = timeStamp;
        }

        public long getDisableTime() {
            return this.mTemporarilyDisabledTimestamp;
        }

        public int getDisableReasonCounter(int reason) {
            if (reason >= 0 && reason < 15) {
                return this.mNetworkSeclectionDisableCounter[reason];
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Illegal reason value: ");
            stringBuilder.append(reason);
            throw new IllegalArgumentException(stringBuilder.toString());
        }

        public void setDisableReasonCounter(int reason, int value) {
            if (reason < 0 || reason >= 15) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Illegal reason value: ");
                stringBuilder.append(reason);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            this.mNetworkSeclectionDisableCounter[reason] = value;
        }

        public void incrementDisableReasonCounter(int reason) {
            if (reason < 0 || reason >= 15) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Illegal reason value: ");
                stringBuilder.append(reason);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            int[] iArr = this.mNetworkSeclectionDisableCounter;
            iArr[reason] = iArr[reason] + 1;
        }

        public void clearDisableReasonCounter(int reason) {
            if (reason < 0 || reason >= 15) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Illegal reason value: ");
                stringBuilder.append(reason);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            this.mNetworkSeclectionDisableCounter[reason] = 0;
        }

        public void clearDisableReasonCounter() {
            Arrays.fill(this.mNetworkSeclectionDisableCounter, 0);
        }

        public String getNetworkSelectionBSSID() {
            return this.mNetworkSelectionBSSID;
        }

        public void setNetworkSelectionBSSID(String bssid) {
            this.mNetworkSelectionBSSID = bssid;
        }

        public void copy(NetworkSelectionStatus source) {
            this.mStatus = source.mStatus;
            this.mNetworkSelectionDisableReason = source.mNetworkSelectionDisableReason;
            for (int index = 0; index < 15; index++) {
                this.mNetworkSeclectionDisableCounter[index] = source.mNetworkSeclectionDisableCounter[index];
            }
            this.mTemporarilyDisabledTimestamp = source.mTemporarilyDisabledTimestamp;
            this.mNetworkSelectionBSSID = source.mNetworkSelectionBSSID;
            setSeenInLastQualifiedNetworkSelection(source.getSeenInLastQualifiedNetworkSelection());
            setCandidate(source.getCandidate());
            setCandidateScore(source.getCandidateScore());
            setConnectChoice(source.getConnectChoice());
            setConnectChoiceTimestamp(source.getConnectChoiceTimestamp());
            setHasEverConnected(source.getHasEverConnected());
            setNotRecommended(source.isNotRecommended());
        }

        public void writeToParcel(Parcel dest) {
            dest.writeInt(getNetworkSelectionStatus());
            dest.writeInt(getNetworkSelectionDisableReason());
            for (int index = 0; index < 15; index++) {
                dest.writeInt(getDisableReasonCounter(index));
            }
            dest.writeLong(getDisableTime());
            dest.writeString(getNetworkSelectionBSSID());
            if (getConnectChoice() != null) {
                dest.writeInt(1);
                dest.writeString(getConnectChoice());
                dest.writeLong(getConnectChoiceTimestamp());
            } else {
                dest.writeInt(-1);
            }
            dest.writeInt(getHasEverConnected());
            dest.writeInt(isNotRecommended());
        }

        public void readFromParcel(Parcel in) {
            setNetworkSelectionStatus(in.readInt());
            setNetworkSelectionDisableReason(in.readInt());
            for (int index = 0; index < 15; index++) {
                setDisableReasonCounter(index, in.readInt());
            }
            setDisableTime(in.readLong());
            setNetworkSelectionBSSID(in.readString());
            boolean z = true;
            if (in.readInt() == 1) {
                setConnectChoice(in.readString());
                setConnectChoiceTimestamp(in.readLong());
            } else {
                setConnectChoice(null);
                setConnectChoiceTimestamp(-1);
            }
            setHasEverConnected(in.readInt() != 0);
            if (in.readInt() == 0) {
                z = false;
            }
            setNotRecommended(z);
        }
    }

    public static class PairwiseCipher {
        public static final int CCMP = 2;
        public static final int GCMP_256 = 3;
        public static final int NONE = 0;
        @Deprecated
        public static final int TKIP = 1;
        public static final String[] strings = new String[]{KeyProperties.DIGEST_NONE, "TKIP", "CCMP", "GCMP_256"};
        public static final String varName = "pairwise";

        private PairwiseCipher() {
        }
    }

    public static class Protocol {
        public static final int OSEN = 2;
        public static final int RSN = 1;
        public static final int WAPI = 3;
        @Deprecated
        public static final int WPA = 0;
        public static final String[] strings = new String[]{"WPA", "RSN", "OSEN", "WAPI"};
        public static final String varName = "proto";

        private Protocol() {
        }
    }

    public static class RecentFailure {
        public static final int NONE = 0;
        public static final int STATUS_AP_UNABLE_TO_HANDLE_NEW_STA = 17;
        private int mAssociationStatus = 0;

        public void setAssociationStatus(int status) {
            this.mAssociationStatus = status;
        }

        public void clear() {
            this.mAssociationStatus = 0;
        }

        public int getAssociationStatus() {
            return this.mAssociationStatus;
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface SecurityType {
    }

    public static class Status {
        public static final int CURRENT = 0;
        public static final int DISABLED = 1;
        public static final int ENABLED = 2;
        public static final String[] strings = new String[]{Carriers.CURRENT, "disabled", "enabled"};

        private Status() {
        }
    }

    public static class SuiteBCipher {
        public static final int ECDHE_ECDSA = 0;
        public static final int ECDHE_RSA = 1;
        private static final String[] strings = new String[]{"ECDHE_ECDSA", "ECDHE_RSA"};
        private static final String varName = "SuiteB";

        private SuiteBCipher() {
        }
    }

    public void setSecurityParams(int securityType) {
        this.allowedKeyManagement.clear();
        this.allowedProtocols.clear();
        this.allowedAuthAlgorithms.clear();
        this.allowedPairwiseCiphers.clear();
        this.allowedGroupCiphers.clear();
        this.allowedGroupManagementCiphers.clear();
        this.allowedSuiteBCiphers.clear();
        switch (securityType) {
            case 0:
                this.allowedKeyManagement.set(0);
                return;
            case 1:
                this.allowedKeyManagement.set(0);
                this.allowedAuthAlgorithms.set(0);
                this.allowedAuthAlgorithms.set(1);
                return;
            case 2:
                this.allowedKeyManagement.set(1);
                return;
            case 3:
                this.allowedKeyManagement.set(2);
                this.allowedKeyManagement.set(3);
                return;
            case 4:
                this.allowedKeyManagement.set(8);
                this.requirePMF = true;
                return;
            case 5:
                this.allowedKeyManagement.set(10);
                this.allowedGroupCiphers.set(5);
                this.allowedGroupManagementCiphers.set(2);
                this.requirePMF = true;
                return;
            case 6:
                this.allowedKeyManagement.set(9);
                this.requirePMF = true;
                return;
            case 7:
                this.allowedKeyManagement.set(13);
                return;
            case 8:
                this.allowedKeyManagement.set(14);
                return;
            default:
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("unknown security type ");
                stringBuilder.append(securityType);
                throw new IllegalArgumentException(stringBuilder.toString());
        }
    }

    @SystemApi
    public boolean hasNoInternetAccess() {
        return this.numNoInternetAccessReports > 0 && !this.validatedInternetAccess;
    }

    @SystemApi
    public boolean isNoInternetAccessExpected() {
        return this.noInternetAccessExpected;
    }

    @SystemApi
    public boolean isEphemeral() {
        return this.ephemeral;
    }

    public static boolean isMetered(WifiConfiguration config, WifiInfo info) {
        boolean metered = false;
        if (info != null && info.getMeteredHint()) {
            metered = true;
        }
        if (config != null && config.meteredHint) {
            metered = true;
        }
        if (config != null && config.meteredOverride == 1) {
            metered = true;
        }
        if (config == null || config.meteredOverride != 2) {
            return metered;
        }
        return false;
    }

    public boolean isOpenNetwork() {
        int cardinality = this.allowedKeyManagement.cardinality();
        boolean hasNoKeyMgmt = cardinality == 0 || (cardinality == 1 && (this.allowedKeyManagement.get(0) || this.allowedKeyManagement.get(9)));
        boolean hasNoWepKeys = true;
        if (this.wepKeys != null) {
            int i = 0;
            while (true) {
                String[] strArr = this.wepKeys;
                if (i >= strArr.length) {
                    break;
                } else if (strArr[i] != null) {
                    hasNoWepKeys = false;
                    break;
                } else {
                    i++;
                }
            }
        }
        if (hasNoKeyMgmt && hasNoWepKeys) {
            return true;
        }
        return false;
    }

    public static boolean isValidMacAddressForRandomization(MacAddress mac) {
        return (mac == null || mac.isMulticastAddress() || !mac.isLocallyAssigned() || MacAddress.fromString("02:00:00:00:00:00").equals(mac)) ? false : true;
    }

    public MacAddress getOrCreateRandomizedMacAddress() {
        int randomMacGenerationCount = 0;
        while (!isValidMacAddressForRandomization(this.mRandomizedMacAddress) && randomMacGenerationCount < 3) {
            this.mRandomizedMacAddress = MacAddress.createRandomUnicastAddress();
            randomMacGenerationCount++;
        }
        if (!isValidMacAddressForRandomization(this.mRandomizedMacAddress)) {
            this.mRandomizedMacAddress = MacAddress.fromString("02:00:00:00:00:00");
        }
        return this.mRandomizedMacAddress;
    }

    public MacAddress getRandomizedMacAddress() {
        return this.mRandomizedMacAddress;
    }

    public void setRandomizedMacAddress(MacAddress mac) {
        if (mac == null) {
            Log.e(TAG, "setRandomizedMacAddress received null MacAddress.");
        } else {
            this.mRandomizedMacAddress = mac;
        }
    }

    public NetworkSelectionStatus getNetworkSelectionStatus() {
        return this.mNetworkSelectionStatus;
    }

    public void setNetworkSelectionStatus(NetworkSelectionStatus status) {
        this.mNetworkSelectionStatus = status;
    }

    public WifiConfiguration() {
        this.apBand = 0;
        this.apChannel = 0;
        this.dtimInterval = 0;
        this.isLegacyPasspointConfig = false;
        this.userApproved = 0;
        this.meteredOverride = 0;
        this.macRandomizationSetting = 1;
        this.mNetworkSelectionStatus = new NetworkSelectionStatus();
        this.recentFailure = new RecentFailure();
        this.networkId = -1;
        this.SSID = null;
        this.BSSID = null;
        this.FQDN = null;
        this.roamingConsortiumIds = new long[0];
        this.priority = 0;
        this.hiddenSSID = false;
        this.shareThisAp = false;
        this.allowedKeyManagement = new BitSet();
        this.allowedProtocols = new BitSet();
        this.allowedAuthAlgorithms = new BitSet();
        this.allowedPairwiseCiphers = new BitSet();
        this.allowedGroupCiphers = new BitSet();
        this.allowedGroupManagementCiphers = new BitSet();
        this.allowedSuiteBCiphers = new BitSet();
        this.wepKeys = new String[4];
        int i = 0;
        while (true) {
            String[] strArr = this.wepKeys;
            if (i < strArr.length) {
                strArr[i] = null;
                i++;
            } else {
                this.enterpriseConfig = new WifiEnterpriseConfig();
                this.selfAdded = false;
                this.didSelfAdd = false;
                this.ephemeral = false;
                this.osu = false;
                this.trusted = true;
                this.fromWifiNetworkSuggestion = false;
                this.fromWifiNetworkSpecifier = false;
                this.meteredHint = false;
                this.meteredOverride = 0;
                this.useExternalScores = false;
                this.validatedInternetAccess = false;
                this.mIpConfiguration = new IpConfiguration();
                this.wapiPskType = -1;
                this.wapiPsk = null;
                this.wapiCertSelMode = -1;
                this.wapiCertSel = null;
                this.lastUpdateUid = -1;
                this.creatorUid = -1;
                this.shared = true;
                this.dtimInterval = 0;
                this.mRandomizedMacAddress = MacAddress.fromString("02:00:00:00:00:00");
                this.dppConnector = null;
                this.dppNetAccessKey = null;
                this.dppNetAccessKeyExpiry = -1;
                this.dppCsign = null;
                this.oweTransIfaceName = null;
                return;
            }
        }
    }

    public boolean isPasspoint() {
        if (!(TextUtils.isEmpty(this.FQDN) || TextUtils.isEmpty(this.providerFriendlyName))) {
            WifiEnterpriseConfig wifiEnterpriseConfig = this.enterpriseConfig;
            if (!(wifiEnterpriseConfig == null || wifiEnterpriseConfig.getEapMethod() == -1)) {
                return true;
            }
        }
        return false;
    }

    public boolean isLinked(WifiConfiguration config) {
        if (config != null) {
            HashMap hashMap = config.linkedConfigurations;
            if (!(hashMap == null || this.linkedConfigurations == null || hashMap.get(configKey()) == null || this.linkedConfigurations.get(config.configKey()) == null)) {
                return true;
            }
        }
        return false;
    }

    @UnsupportedAppUsage
    public boolean isEnterprise() {
        if (this.allowedKeyManagement.get(2) || this.allowedKeyManagement.get(3) || this.allowedKeyManagement.get(10)) {
            WifiEnterpriseConfig wifiEnterpriseConfig = this.enterpriseConfig;
            if (!(wifiEnterpriseConfig == null || wifiEnterpriseConfig.getEapMethod() == -1)) {
                return true;
            }
        }
        return false;
    }

    public String toString() {
        int index;
        String str;
        String str2;
        StringBuilder sbuf = new StringBuilder();
        int i = this.status;
        if (i == 0) {
            sbuf.append("* ");
        } else if (i == 1) {
            sbuf.append("- DSBLE ");
        }
        sbuf.append("ID: ");
        sbuf.append(this.networkId);
        sbuf.append(" SSID: ");
        sbuf.append(this.SSID);
        sbuf.append(" PROVIDER-NAME: ");
        sbuf.append(this.providerFriendlyName);
        sbuf.append(" BSSID: ");
        sbuf.append(this.BSSID);
        sbuf.append(" FQDN: ");
        sbuf.append(this.FQDN);
        sbuf.append(" PRIO: ");
        sbuf.append(this.priority);
        sbuf.append(" HIDDEN: ");
        sbuf.append(this.hiddenSSID);
        sbuf.append(" PMF: ");
        sbuf.append(this.requirePMF);
        sbuf.append(" OWE Transition mode Iface: ");
        sbuf.append(this.oweTransIfaceName);
        sbuf.append(10);
        sbuf.append(" NetworkSelectionStatus ");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mNetworkSelectionStatus.getNetworkStatusString());
        String str3 = "\n";
        stringBuilder.append(str3);
        sbuf.append(stringBuilder.toString());
        if (this.mNetworkSelectionStatus.getNetworkSelectionDisableReason() > 0) {
            sbuf.append(" mNetworkSelectionDisableReason ");
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.mNetworkSelectionStatus.getNetworkDisableReasonString());
            stringBuilder.append(str3);
            sbuf.append(stringBuilder.toString());
            NetworkSelectionStatus networkSelectionStatus = this.mNetworkSelectionStatus;
            index = 0;
            while (true) {
                NetworkSelectionStatus networkSelectionStatus2 = this.mNetworkSelectionStatus;
                if (index >= 15) {
                    break;
                }
                if (networkSelectionStatus2.getDisableReasonCounter(index) != 0) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(NetworkSelectionStatus.getNetworkDisableReasonString(index));
                    stringBuilder2.append(" counter:");
                    stringBuilder2.append(this.mNetworkSelectionStatus.getDisableReasonCounter(index));
                    stringBuilder2.append(str3);
                    sbuf.append(stringBuilder2.toString());
                }
                index++;
            }
        }
        if (this.mNetworkSelectionStatus.getConnectChoice() != null) {
            sbuf.append(" connect choice: ");
            sbuf.append(this.mNetworkSelectionStatus.getConnectChoice());
            sbuf.append(" connect choice set time: ");
            sbuf.append(TimeUtils.logTimeOfDay(this.mNetworkSelectionStatus.getConnectChoiceTimestamp()));
        }
        sbuf.append(" hasEverConnected: ");
        sbuf.append(this.mNetworkSelectionStatus.getHasEverConnected());
        sbuf.append(str3);
        if (this.numAssociation > 0) {
            sbuf.append(" numAssociation ");
            sbuf.append(this.numAssociation);
            sbuf.append(str3);
        }
        if (this.numNoInternetAccessReports > 0) {
            sbuf.append(" numNoInternetAccessReports ");
            sbuf.append(this.numNoInternetAccessReports);
            sbuf.append(str3);
        }
        if (this.updateTime != null) {
            sbuf.append(" update ");
            sbuf.append(this.updateTime);
            sbuf.append(str3);
        }
        if (this.creationTime != null) {
            sbuf.append(" creation ");
            sbuf.append(this.creationTime);
            sbuf.append(str3);
        }
        if (this.didSelfAdd) {
            sbuf.append(" didSelfAdd");
        }
        if (this.selfAdded) {
            sbuf.append(" selfAdded");
        }
        if (this.validatedInternetAccess) {
            sbuf.append(" validatedInternetAccess");
        }
        if (this.ephemeral) {
            sbuf.append(" ephemeral");
        }
        if (this.osu) {
            sbuf.append(" osu");
        }
        if (this.trusted) {
            sbuf.append(" trusted");
        }
        if (this.fromWifiNetworkSuggestion) {
            sbuf.append(" fromWifiNetworkSuggestion");
        }
        if (this.fromWifiNetworkSpecifier) {
            sbuf.append(" fromWifiNetworkSpecifier");
        }
        if (this.meteredHint) {
            sbuf.append(" meteredHint");
        }
        if (this.useExternalScores) {
            sbuf.append(" useExternalScores");
        }
        if (this.didSelfAdd || this.selfAdded || this.validatedInternetAccess || this.ephemeral || this.trusted || this.fromWifiNetworkSuggestion || this.fromWifiNetworkSpecifier || this.meteredHint || this.useExternalScores) {
            sbuf.append(str3);
        }
        if (this.meteredOverride != 0) {
            sbuf.append(" meteredOverride ");
            sbuf.append(this.meteredOverride);
            sbuf.append(str3);
        }
        sbuf.append(" macRandomizationSetting: ");
        sbuf.append(this.macRandomizationSetting);
        sbuf.append(str3);
        sbuf.append(" mRandomizedMacAddress: ");
        sbuf.append(this.mRandomizedMacAddress);
        sbuf.append(str3);
        sbuf.append(" KeyMgmt:");
        index = 0;
        while (true) {
            int size = this.allowedKeyManagement.size();
            str = "??";
            str2 = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
            if (index >= size) {
                break;
            }
            if (this.allowedKeyManagement.get(index)) {
                sbuf.append(str2);
                if (index < KeyMgmt.strings.length) {
                    sbuf.append(KeyMgmt.strings[index]);
                } else if (190 == index) {
                    sbuf.append(KeyMgmt.wapi_psk_string);
                } else if (191 == index) {
                    sbuf.append(KeyMgmt.wapi_cert_string);
                } else {
                    sbuf.append(str);
                }
            }
            index++;
        }
        sbuf.append(" Protocols:");
        for (index = 0; index < this.allowedProtocols.size(); index++) {
            if (this.allowedProtocols.get(index)) {
                sbuf.append(str2);
                if (index < Protocol.strings.length) {
                    sbuf.append(Protocol.strings[index]);
                } else {
                    sbuf.append(str);
                }
            }
        }
        sbuf.append(10);
        sbuf.append(" AuthAlgorithms:");
        for (index = 0; index < this.allowedAuthAlgorithms.size(); index++) {
            if (this.allowedAuthAlgorithms.get(index)) {
                sbuf.append(str2);
                if (index < AuthAlgorithm.strings.length) {
                    sbuf.append(AuthAlgorithm.strings[index]);
                } else {
                    sbuf.append(str);
                }
            }
        }
        sbuf.append(10);
        sbuf.append(" PairwiseCiphers:");
        for (index = 0; index < this.allowedPairwiseCiphers.size(); index++) {
            if (this.allowedPairwiseCiphers.get(index)) {
                sbuf.append(str2);
                if (index < PairwiseCipher.strings.length) {
                    sbuf.append(PairwiseCipher.strings[index]);
                } else {
                    sbuf.append(str);
                }
            }
        }
        sbuf.append(10);
        sbuf.append(" GroupCiphers:");
        for (index = 0; index < this.allowedGroupCiphers.size(); index++) {
            if (this.allowedGroupCiphers.get(index)) {
                sbuf.append(str2);
                if (index < GroupCipher.strings.length) {
                    sbuf.append(GroupCipher.strings[index]);
                } else {
                    sbuf.append(str);
                }
            }
        }
        sbuf.append(10);
        sbuf.append(" GroupMgmtCiphers:");
        for (index = 0; index < this.allowedGroupManagementCiphers.size(); index++) {
            if (this.allowedGroupManagementCiphers.get(index)) {
                sbuf.append(str2);
                if (index < GroupMgmtCipher.strings.length) {
                    sbuf.append(GroupMgmtCipher.strings[index]);
                } else {
                    sbuf.append(str);
                }
            }
        }
        sbuf.append(10);
        sbuf.append(" SuiteBCiphers:");
        for (index = 0; index < this.allowedSuiteBCiphers.size(); index++) {
            if (this.allowedSuiteBCiphers.get(index)) {
                sbuf.append(str2);
                if (index < SuiteBCipher.strings.length) {
                    sbuf.append(SuiteBCipher.strings[index]);
                } else {
                    sbuf.append(str);
                }
            }
        }
        sbuf.append(10);
        sbuf.append(" PSK/SAE: ");
        if (this.preSharedKey != null) {
            sbuf.append('*');
        }
        sbuf.append(10);
        if (this.wapiPskType != -1) {
            sbuf.append(" WapiPskType: ");
            sbuf.append(this.wapiPskType);
        }
        sbuf.append(10);
        if (this.wapiPsk != null) {
            sbuf.append(" WapiPsk: ");
            sbuf.append(this.wapiPsk);
        }
        sbuf.append(10);
        if (this.wapiCertSelMode != -1) {
            sbuf.append(" WapiCertSelMode: ");
            sbuf.append(this.wapiCertSelMode);
        }
        sbuf.append(10);
        if (this.wapiCertSel != null) {
            sbuf.append(" WapiCertSel: ");
            sbuf.append(this.wapiCertSel);
        }
        sbuf.append("\nEnterprise config:\n");
        sbuf.append(this.enterpriseConfig);
        sbuf.append("\nDPP config:\n");
        if (this.dppConnector != null) {
            sbuf.append(" Dpp Connector: *\n");
        }
        if (this.dppNetAccessKey != null) {
            sbuf.append(" Dpp NetAccessKey: *\n");
        }
        if (this.dppCsign != null) {
            sbuf.append(" Dpp Csign: *\n");
        }
        sbuf.append("IP config:\n");
        sbuf.append(this.mIpConfiguration.toString());
        if (this.mNetworkSelectionStatus.getNetworkSelectionBSSID() != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(" networkSelectionBSSID=");
            stringBuilder.append(this.mNetworkSelectionStatus.getNetworkSelectionBSSID());
            sbuf.append(stringBuilder.toString());
        }
        long now_ms = SystemClock.elapsedRealtime();
        if (this.mNetworkSelectionStatus.getDisableTime() != -1) {
            sbuf.append(10);
            long diff = now_ms - this.mNetworkSelectionStatus.getDisableTime();
            if (diff <= 0) {
                sbuf.append(" blackListed since <incorrect>");
            } else {
                sbuf.append(" blackListed: ");
                sbuf.append(Long.toString(diff / 1000));
                sbuf.append("sec ");
            }
        }
        if (this.creatorUid != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(" cuid=");
            stringBuilder.append(this.creatorUid);
            sbuf.append(stringBuilder.toString());
        }
        if (this.creatorName != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(" cname=");
            stringBuilder.append(this.creatorName);
            sbuf.append(stringBuilder.toString());
        }
        if (this.lastUpdateUid != 0) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(" luid=");
            stringBuilder.append(this.lastUpdateUid);
            sbuf.append(stringBuilder.toString());
        }
        if (this.lastUpdateName != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(" lname=");
            stringBuilder.append(this.lastUpdateName);
            sbuf.append(stringBuilder.toString());
        }
        if (this.updateIdentifier != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(" updateIdentifier=");
            stringBuilder.append(this.updateIdentifier);
            sbuf.append(stringBuilder.toString());
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(" lcuid=");
        stringBuilder.append(this.lastConnectUid);
        sbuf.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(" userApproved=");
        stringBuilder.append(userApprovedAsString(this.userApproved));
        sbuf.append(stringBuilder.toString());
        stringBuilder = new StringBuilder();
        stringBuilder.append(" noInternetAccessExpected=");
        stringBuilder.append(this.noInternetAccessExpected);
        sbuf.append(stringBuilder.toString());
        sbuf.append(str2);
        if (this.lastConnected != 0) {
            sbuf.append(10);
            sbuf.append("lastConnected: ");
            sbuf.append(TimeUtils.logTimeOfDay(this.lastConnected));
            sbuf.append(str2);
        }
        sbuf.append(10);
        HashMap hashMap = this.linkedConfigurations;
        if (hashMap != null) {
            for (String str22 : hashMap.keySet()) {
                sbuf.append(" linked: ");
                sbuf.append(str22);
                sbuf.append(10);
            }
        }
        sbuf.append("recentFailure: ");
        sbuf.append("Association Rejection code: ");
        sbuf.append(this.recentFailure.getAssociationStatus());
        sbuf.append(str3);
        sbuf.append("ShareThisAp: ");
        sbuf.append(this.shareThisAp);
        sbuf.append(10);
        return sbuf.toString();
    }

    @UnsupportedAppUsage
    public String getPrintableSsid() {
        int length = this.SSID;
        if (length == 0) {
            return "";
        }
        length = length.length();
        if (length > 2 && this.SSID.charAt(0) == '\"' && this.SSID.charAt(length - 1) == '\"') {
            return this.SSID.substring(1, length - 1);
        }
        if (length > 3 && this.SSID.charAt(0) == 'P' && this.SSID.charAt(1) == '\"' && this.SSID.charAt(length - 1) == '\"') {
            return WifiSsid.createFromAsciiEncoded(this.SSID.substring(2, length - 1)).toString();
        }
        return this.SSID;
    }

    public static String userApprovedAsString(int userApproved) {
        if (userApproved == 0) {
            return "USER_UNSPECIFIED";
        }
        if (userApproved == 1) {
            return "USER_APPROVED";
        }
        if (userApproved != 2) {
            return "INVALID";
        }
        return "USER_BANNED";
    }

    public String getKeyIdForCredentials(WifiConfiguration current) {
        String str = Session.SESSION_SEPARATION_CHAR_CHILD;
        String keyMgmt = "";
        try {
            StringBuilder stringBuilder;
            if (TextUtils.isEmpty(this.SSID)) {
                this.SSID = current.SSID;
            }
            if (this.allowedKeyManagement.cardinality() == 0) {
                this.allowedKeyManagement = current.allowedKeyManagement;
            }
            if (this.allowedKeyManagement.get(2)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(keyMgmt);
                stringBuilder.append(KeyMgmt.strings[2]);
                keyMgmt = stringBuilder.toString();
            }
            if (this.allowedKeyManagement.get(5)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(keyMgmt);
                stringBuilder.append(KeyMgmt.strings[5]);
                keyMgmt = stringBuilder.toString();
            }
            if (this.allowedKeyManagement.get(3)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(keyMgmt);
                stringBuilder.append(KeyMgmt.strings[3]);
                keyMgmt = stringBuilder.toString();
            }
            if (this.allowedKeyManagement.get(10)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(keyMgmt);
                stringBuilder.append(KeyMgmt.strings[10]);
                keyMgmt = stringBuilder.toString();
            }
            if (TextUtils.isEmpty(keyMgmt)) {
                throw new IllegalStateException("Not an EAP network");
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(trimStringForKeyId(this.SSID));
            stringBuilder.append(str);
            stringBuilder.append(keyMgmt);
            stringBuilder.append(str);
            stringBuilder.append(trimStringForKeyId(this.enterpriseConfig.getKeyId(current != null ? current.enterpriseConfig : null)));
            return stringBuilder.toString();
        } catch (NullPointerException e) {
            throw new IllegalStateException("Invalid config details");
        }
    }

    private String trimStringForKeyId(String string) {
        String str = "";
        return string.replace("\"", str).replace(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER, str);
    }

    private static BitSet readBitSet(Parcel src) {
        int cardinality = src.readInt();
        BitSet set = new BitSet();
        for (int i = 0; i < cardinality; i++) {
            set.set(src.readInt());
        }
        return set;
    }

    private static void writeBitSet(Parcel dest, BitSet set) {
        int nextSetBit = -1;
        dest.writeInt(set.cardinality());
        while (true) {
            int nextSetBit2 = set.nextSetBit(nextSetBit + 1);
            nextSetBit = nextSetBit2;
            if (nextSetBit2 != -1) {
                dest.writeInt(nextSetBit);
            } else {
                return;
            }
        }
    }

    @UnsupportedAppUsage
    public int getAuthType() {
        if (this.allowedKeyManagement.cardinality() > 1) {
            String str = "More than one auth type set";
            if (!this.allowedKeyManagement.get(190) || !this.allowedKeyManagement.get(191)) {
                throw new IllegalStateException(str);
            } else if (this.allowedKeyManagement.cardinality() != 4) {
                throw new IllegalStateException(str);
            }
        }
        if (this.allowedKeyManagement.get(1)) {
            return 1;
        }
        if (this.allowedKeyManagement.get(4)) {
            return 4;
        }
        if (this.allowedKeyManagement.get(2)) {
            return 2;
        }
        if (this.allowedKeyManagement.get(3)) {
            return 3;
        }
        if (this.allowedKeyManagement.get(15)) {
            return 15;
        }
        if (this.allowedKeyManagement.get(8)) {
            return 8;
        }
        if (this.allowedKeyManagement.get(9)) {
            return 9;
        }
        if (this.allowedKeyManagement.get(10)) {
            return 10;
        }
        if (this.allowedKeyManagement.get(190)) {
            return 190;
        }
        if (this.allowedKeyManagement.get(191)) {
            return 191;
        }
        return 0;
    }

    public String configKey(boolean allowCached) {
        if (allowCached && this.mCachedConfigKey != null) {
            return this.mCachedConfigKey;
        }
        String str = "-";
        String key;
        StringBuilder stringBuilder;
        if (this.providerFriendlyName != null) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(this.FQDN);
            stringBuilder2.append(KeyMgmt.strings[2]);
            key = stringBuilder2.toString();
            if (this.shared) {
                return key;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(key);
            stringBuilder.append(str);
            stringBuilder.append(Integer.toString(UserHandle.getUserId(this.creatorUid)));
            return stringBuilder.toString();
        }
        key = getSsidAndSecurityTypeString();
        if (!this.shared) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(key);
            stringBuilder.append(str);
            stringBuilder.append(Integer.toString(UserHandle.getUserId(this.creatorUid)));
            key = stringBuilder.toString();
        }
        this.mCachedConfigKey = key;
        return key;
    }

    public String getSsidAndSecurityTypeString() {
        String str = "-";
        StringBuilder stringBuilder;
        if (this.allowedKeyManagement.get(1)) {
            String key = new StringBuilder();
            key.append(this.SSID);
            key.append(str);
            key.append(KeyMgmt.strings[1]);
            return key.toString();
        } else if (this.allowedKeyManagement.get(2) || this.allowedKeyManagement.get(3)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(this.SSID);
            stringBuilder.append(str);
            stringBuilder.append(KeyMgmt.strings[2]);
            return stringBuilder.toString();
        } else {
            String[] strArr = this.wepKeys;
            if (strArr[0] != null || strArr[1] != null || strArr[2] != null || strArr[3] != null) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.SSID);
                stringBuilder.append("-WEP");
                return stringBuilder.toString();
            } else if (this.allowedKeyManagement.get(9)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.SSID);
                stringBuilder.append(KeyMgmt.strings[9]);
                return stringBuilder.toString();
            } else if (this.allowedKeyManagement.get(8)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.SSID);
                stringBuilder.append(KeyMgmt.strings[8]);
                return stringBuilder.toString();
            } else if (this.allowedKeyManagement.get(10)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.SSID);
                stringBuilder.append(KeyMgmt.strings[10]);
                return stringBuilder.toString();
            } else if (this.allowedKeyManagement.get(15)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.SSID);
                stringBuilder.append(KeyMgmt.strings[15]);
                return stringBuilder.toString();
            } else if (this.allowedKeyManagement.get(190)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.SSID);
                stringBuilder.append(str);
                stringBuilder.append(KeyMgmt.wapi_psk_string);
                return stringBuilder.toString();
            } else if (this.allowedKeyManagement.get(191)) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.SSID);
                stringBuilder.append(str);
                stringBuilder.append(KeyMgmt.wapi_cert_string);
                return stringBuilder.toString();
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(this.SSID);
                stringBuilder.append(KeyMgmt.strings[0]);
                return stringBuilder.toString();
            }
        }
    }

    public String configKey() {
        return configKey(false);
    }

    public static String configKey(ScanResult result) {
        String key = new StringBuilder();
        String str = "\"";
        key.append(str);
        key.append(result.SSID);
        key.append(str);
        return key.toString();
    }

    @UnsupportedAppUsage
    public IpConfiguration getIpConfiguration() {
        return this.mIpConfiguration;
    }

    @UnsupportedAppUsage
    public void setIpConfiguration(IpConfiguration ipConfiguration) {
        if (ipConfiguration == null) {
            ipConfiguration = new IpConfiguration();
        }
        this.mIpConfiguration = ipConfiguration;
    }

    @UnsupportedAppUsage
    public StaticIpConfiguration getStaticIpConfiguration() {
        return this.mIpConfiguration.getStaticIpConfiguration();
    }

    @UnsupportedAppUsage
    public void setStaticIpConfiguration(StaticIpConfiguration staticIpConfiguration) {
        this.mIpConfiguration.setStaticIpConfiguration(staticIpConfiguration);
    }

    @UnsupportedAppUsage
    public IpAssignment getIpAssignment() {
        return this.mIpConfiguration.ipAssignment;
    }

    @UnsupportedAppUsage
    public void setIpAssignment(IpAssignment ipAssignment) {
        this.mIpConfiguration.ipAssignment = ipAssignment;
    }

    @UnsupportedAppUsage
    public ProxySettings getProxySettings() {
        return this.mIpConfiguration.proxySettings;
    }

    @UnsupportedAppUsage
    public void setProxySettings(ProxySettings proxySettings) {
        this.mIpConfiguration.proxySettings = proxySettings;
    }

    public ProxyInfo getHttpProxy() {
        if (this.mIpConfiguration.proxySettings == ProxySettings.NONE) {
            return null;
        }
        return new ProxyInfo(this.mIpConfiguration.httpProxy);
    }

    public void setHttpProxy(ProxyInfo httpProxy) {
        if (httpProxy == null) {
            this.mIpConfiguration.setProxySettings(ProxySettings.NONE);
            this.mIpConfiguration.setHttpProxy(null);
            return;
        }
        ProxySettings proxySettingCopy;
        ProxyInfo httpProxyCopy;
        if (Uri.EMPTY.equals(httpProxy.getPacFileUrl())) {
            proxySettingCopy = ProxySettings.STATIC;
            httpProxyCopy = new ProxyInfo(httpProxy.getHost(), httpProxy.getPort(), httpProxy.getExclusionListAsString());
        } else {
            proxySettingCopy = ProxySettings.PAC;
            httpProxyCopy = new ProxyInfo(httpProxy.getPacFileUrl(), httpProxy.getPort());
        }
        if (httpProxyCopy.isValid()) {
            this.mIpConfiguration.setProxySettings(proxySettingCopy);
            this.mIpConfiguration.setHttpProxy(httpProxyCopy);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid ProxyInfo: ");
        stringBuilder.append(httpProxyCopy.toString());
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    @UnsupportedAppUsage
    public void setProxy(ProxySettings settings, ProxyInfo proxy) {
        IpConfiguration ipConfiguration = this.mIpConfiguration;
        ipConfiguration.proxySettings = settings;
        ipConfiguration.httpProxy = proxy;
    }

    public int describeContents() {
        return 0;
    }

    public void setPasspointManagementObjectTree(String passpointManagementObjectTree) {
        this.mPasspointManagementObjectTree = passpointManagementObjectTree;
    }

    public String getMoTree() {
        return this.mPasspointManagementObjectTree;
    }

    @UnsupportedAppUsage
    public WifiConfiguration(WifiConfiguration source) {
        this.apBand = 0;
        this.apChannel = 0;
        this.dtimInterval = 0;
        this.isLegacyPasspointConfig = false;
        this.userApproved = 0;
        this.meteredOverride = 0;
        this.macRandomizationSetting = 1;
        this.mNetworkSelectionStatus = new NetworkSelectionStatus();
        this.recentFailure = new RecentFailure();
        if (source != null) {
            this.networkId = source.networkId;
            this.status = source.status;
            this.SSID = source.SSID;
            this.BSSID = source.BSSID;
            this.FQDN = source.FQDN;
            this.shareThisAp = source.shareThisAp;
            this.roamingConsortiumIds = (long[]) source.roamingConsortiumIds.clone();
            this.providerFriendlyName = source.providerFriendlyName;
            this.isHomeProviderNetwork = source.isHomeProviderNetwork;
            this.preSharedKey = source.preSharedKey;
            this.wapiPskType = source.wapiPskType;
            this.wapiPsk = source.wapiPsk;
            this.wapiCertSelMode = source.wapiCertSelMode;
            this.wapiCertSel = source.wapiCertSel;
            this.mNetworkSelectionStatus.copy(source.getNetworkSelectionStatus());
            this.apBand = source.apBand;
            this.apChannel = source.apChannel;
            this.wepKeys = new String[4];
            int i = 0;
            while (true) {
                String[] strArr = this.wepKeys;
                if (i >= strArr.length) {
                    break;
                }
                strArr[i] = source.wepKeys[i];
                i++;
            }
            this.wepTxKeyIndex = source.wepTxKeyIndex;
            this.priority = source.priority;
            this.hiddenSSID = source.hiddenSSID;
            this.allowedKeyManagement = (BitSet) source.allowedKeyManagement.clone();
            this.allowedProtocols = (BitSet) source.allowedProtocols.clone();
            this.allowedAuthAlgorithms = (BitSet) source.allowedAuthAlgorithms.clone();
            this.allowedPairwiseCiphers = (BitSet) source.allowedPairwiseCiphers.clone();
            this.allowedGroupCiphers = (BitSet) source.allowedGroupCiphers.clone();
            this.allowedGroupManagementCiphers = (BitSet) source.allowedGroupManagementCiphers.clone();
            this.allowedSuiteBCiphers = (BitSet) source.allowedSuiteBCiphers.clone();
            this.enterpriseConfig = new WifiEnterpriseConfig(source.enterpriseConfig);
            this.defaultGwMacAddress = source.defaultGwMacAddress;
            this.mIpConfiguration = new IpConfiguration(source.mIpConfiguration);
            HashMap hashMap = source.linkedConfigurations;
            if (hashMap != null && hashMap.size() > 0) {
                this.linkedConfigurations = new HashMap();
                this.linkedConfigurations.putAll(source.linkedConfigurations);
            }
            this.mCachedConfigKey = null;
            this.selfAdded = source.selfAdded;
            this.validatedInternetAccess = source.validatedInternetAccess;
            this.isLegacyPasspointConfig = source.isLegacyPasspointConfig;
            this.ephemeral = source.ephemeral;
            this.osu = source.osu;
            this.trusted = source.trusted;
            this.fromWifiNetworkSuggestion = source.fromWifiNetworkSuggestion;
            this.fromWifiNetworkSpecifier = source.fromWifiNetworkSpecifier;
            this.meteredHint = source.meteredHint;
            this.meteredOverride = source.meteredOverride;
            this.useExternalScores = source.useExternalScores;
            this.didSelfAdd = source.didSelfAdd;
            this.lastConnectUid = source.lastConnectUid;
            this.lastUpdateUid = source.lastUpdateUid;
            this.creatorUid = source.creatorUid;
            this.creatorName = source.creatorName;
            this.lastUpdateName = source.lastUpdateName;
            this.peerWifiConfiguration = source.peerWifiConfiguration;
            this.lastConnected = source.lastConnected;
            this.lastDisconnected = source.lastDisconnected;
            this.numScorerOverride = source.numScorerOverride;
            this.numScorerOverrideAndSwitchedNetwork = source.numScorerOverrideAndSwitchedNetwork;
            this.numAssociation = source.numAssociation;
            this.userApproved = source.userApproved;
            this.numNoInternetAccessReports = source.numNoInternetAccessReports;
            this.noInternetAccessExpected = source.noInternetAccessExpected;
            this.creationTime = source.creationTime;
            this.updateTime = source.updateTime;
            this.shared = source.shared;
            this.recentFailure.setAssociationStatus(source.recentFailure.getAssociationStatus());
            this.mRandomizedMacAddress = source.mRandomizedMacAddress;
            this.dppConnector = source.dppConnector;
            this.dppNetAccessKey = source.dppNetAccessKey;
            this.dppNetAccessKeyExpiry = source.dppNetAccessKeyExpiry;
            this.dppCsign = source.dppCsign;
            this.macRandomizationSetting = source.macRandomizationSetting;
            this.requirePMF = source.requirePMF;
            this.updateIdentifier = source.updateIdentifier;
            this.oweTransIfaceName = source.oweTransIfaceName;
        }
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.networkId);
        dest.writeInt(this.status);
        this.mNetworkSelectionStatus.writeToParcel(dest);
        dest.writeString(this.SSID);
        dest.writeString(this.BSSID);
        dest.writeInt(this.shareThisAp);
        dest.writeInt(this.apBand);
        dest.writeInt(this.apChannel);
        dest.writeString(this.FQDN);
        dest.writeString(this.providerFriendlyName);
        dest.writeInt(this.isHomeProviderNetwork);
        dest.writeInt(this.roamingConsortiumIds.length);
        int i = 0;
        for (long roamingConsortiumId : this.roamingConsortiumIds) {
            dest.writeLong(roamingConsortiumId);
        }
        dest.writeString(this.preSharedKey);
        String[] strArr = this.wepKeys;
        int length = strArr.length;
        while (i < length) {
            dest.writeString(strArr[i]);
            i++;
        }
        dest.writeInt(this.wepTxKeyIndex);
        dest.writeInt(this.priority);
        dest.writeInt(this.hiddenSSID);
        dest.writeInt(this.requirePMF);
        dest.writeString(this.updateIdentifier);
        writeBitSet(dest, this.allowedKeyManagement);
        writeBitSet(dest, this.allowedProtocols);
        writeBitSet(dest, this.allowedAuthAlgorithms);
        writeBitSet(dest, this.allowedPairwiseCiphers);
        writeBitSet(dest, this.allowedGroupCiphers);
        writeBitSet(dest, this.allowedGroupManagementCiphers);
        writeBitSet(dest, this.allowedSuiteBCiphers);
        if (this.allowedKeyManagement.get(190)) {
            dest.writeInt(this.wapiPskType);
            dest.writeString(this.wapiPsk);
        } else if (this.allowedKeyManagement.get(191)) {
            dest.writeInt(this.wapiCertSelMode);
            dest.writeString(this.wapiCertSel);
        }
        dest.writeParcelable(this.enterpriseConfig, flags);
        dest.writeParcelable(this.mIpConfiguration, flags);
        dest.writeString(this.dhcpServer);
        dest.writeString(this.defaultGwMacAddress);
        dest.writeInt(this.selfAdded);
        dest.writeInt(this.didSelfAdd);
        dest.writeInt(this.validatedInternetAccess);
        dest.writeInt(this.isLegacyPasspointConfig);
        dest.writeInt(this.ephemeral);
        dest.writeInt(this.trusted);
        dest.writeInt(this.fromWifiNetworkSuggestion);
        dest.writeInt(this.fromWifiNetworkSpecifier);
        dest.writeInt(this.meteredHint);
        dest.writeInt(this.meteredOverride);
        dest.writeInt(this.useExternalScores);
        dest.writeInt(this.creatorUid);
        dest.writeInt(this.lastConnectUid);
        dest.writeInt(this.lastUpdateUid);
        dest.writeString(this.creatorName);
        dest.writeString(this.lastUpdateName);
        dest.writeInt(this.numScorerOverride);
        dest.writeInt(this.numScorerOverrideAndSwitchedNetwork);
        dest.writeInt(this.numAssociation);
        dest.writeInt(this.userApproved);
        dest.writeInt(this.numNoInternetAccessReports);
        dest.writeInt(this.noInternetAccessExpected);
        dest.writeInt(this.shared);
        dest.writeString(this.mPasspointManagementObjectTree);
        dest.writeInt(this.recentFailure.getAssociationStatus());
        dest.writeParcelable(this.mRandomizedMacAddress, flags);
        dest.writeString(this.dppConnector);
        dest.writeString(this.dppNetAccessKey);
        dest.writeInt(this.dppNetAccessKeyExpiry);
        dest.writeString(this.dppCsign);
        dest.writeInt(this.macRandomizationSetting);
        dest.writeInt(this.osu);
        dest.writeString(this.oweTransIfaceName);
    }

    public byte[] getBytesForBackup() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        out.writeInt(3);
        BackupUtils.writeString(out, this.SSID);
        out.writeInt(this.apBand);
        out.writeInt(this.apChannel);
        BackupUtils.writeString(out, this.preSharedKey);
        out.writeInt(getAuthType());
        out.writeBoolean(this.hiddenSSID);
        return baos.toByteArray();
    }

    public static WifiConfiguration getWifiConfigFromBackup(DataInputStream in) throws IOException, BadVersionException {
        WifiConfiguration config = new WifiConfiguration();
        int version = in.readInt();
        if (version < 1 || version > 3) {
            throw new BadVersionException("Unknown Backup Serialization Version");
        } else if (version == 1) {
            return null;
        } else {
            config.SSID = BackupUtils.readString(in);
            config.apBand = in.readInt();
            config.apChannel = in.readInt();
            config.preSharedKey = BackupUtils.readString(in);
            config.allowedKeyManagement.set(in.readInt());
            if (version >= 3) {
                config.hiddenSSID = in.readBoolean();
            }
            return config;
        }
    }
}
