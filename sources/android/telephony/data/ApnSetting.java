package android.telephony.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Telephony.Carriers;
import android.telephony.ServiceState;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.telephony.PhoneConstants;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ApnSetting implements Parcelable {
    private static final Map<Integer, String> APN_TYPE_INT_MAP = new ArrayMap();
    private static final Map<String, Integer> APN_TYPE_STRING_MAP = new ArrayMap();
    public static final int AUTH_TYPE_CHAP = 2;
    public static final int AUTH_TYPE_NONE = 0;
    public static final int AUTH_TYPE_PAP = 1;
    public static final int AUTH_TYPE_PAP_OR_CHAP = 3;
    public static final Creator<ApnSetting> CREATOR = new Creator<ApnSetting>() {
        public ApnSetting createFromParcel(Parcel in) {
            return ApnSetting.readFromParcel(in);
        }

        public ApnSetting[] newArray(int size) {
            return new ApnSetting[size];
        }
    };
    private static final String LOG_TAG = "ApnSetting";
    public static final int MVNO_TYPE_GID = 2;
    public static final int MVNO_TYPE_ICCID = 3;
    public static final int MVNO_TYPE_IMSI = 1;
    private static final Map<Integer, String> MVNO_TYPE_INT_MAP = new ArrayMap();
    public static final int MVNO_TYPE_SPN = 0;
    private static final Map<String, Integer> MVNO_TYPE_STRING_MAP = new ArrayMap();
    private static final Map<Integer, String> PROTOCOL_INT_MAP = new ArrayMap();
    public static final int PROTOCOL_IP = 0;
    public static final int PROTOCOL_IPV4V6 = 2;
    public static final int PROTOCOL_IPV6 = 1;
    public static final int PROTOCOL_NON_IP = 4;
    public static final int PROTOCOL_PPP = 3;
    private static final Map<String, Integer> PROTOCOL_STRING_MAP = new ArrayMap();
    public static final int PROTOCOL_UNSTRUCTURED = 5;
    public static final int TYPE_ALL = 255;
    public static final int TYPE_CBS = 128;
    public static final int TYPE_DEFAULT = 17;
    public static final int TYPE_DUN = 8;
    public static final int TYPE_EMERGENCY = 512;
    public static final int TYPE_FOTA = 32;
    public static final int TYPE_HIPRI = 16;
    public static final int TYPE_IA = 256;
    public static final int TYPE_IMS = 64;
    public static final int TYPE_MCX = 1024;
    public static final int TYPE_MMS = 2;
    public static final int TYPE_NONE = 0;
    public static final int TYPE_SUPL = 4;
    public static final int UNSET_MTU = 0;
    private static final int UNSPECIFIED_INT = -1;
    private static final String UNSPECIFIED_STRING = "";
    private static final String V2_FORMAT_REGEX = "^\\[ApnSettingV2\\]\\s*";
    private static final String V3_FORMAT_REGEX = "^\\[ApnSettingV3\\]\\s*";
    private static final String V4_FORMAT_REGEX = "^\\[ApnSettingV4\\]\\s*";
    private static final String V5_FORMAT_REGEX = "^\\[ApnSettingV5\\]\\s*";
    private static final String V6_FORMAT_REGEX = "^\\[ApnSettingV6\\]\\s*";
    private static final String V7_FORMAT_REGEX = "^\\[ApnSettingV7\\]\\s*";
    private static final boolean VDBG = false;
    private final String mApnName;
    private final int mApnSetId;
    private final int mApnTypeBitmask;
    private final int mAuthType;
    private final boolean mCarrierEnabled;
    private final int mCarrierId;
    private final String mEntryName;
    private final int mId;
    private final int mMaxConns;
    private final int mMaxConnsTime;
    private final String mMmsProxyAddress;
    private final int mMmsProxyPort;
    private final Uri mMmsc;
    private final int mMtu;
    private final String mMvnoMatchData;
    private final int mMvnoType;
    private final int mNetworkTypeBitmask;
    private final String mOperatorNumeric;
    private final String mPassword;
    private boolean mPermanentFailed;
    private final boolean mPersistent;
    private final int mProfileId;
    private final int mProtocol;
    private final String mProxyAddress;
    private final int mProxyPort;
    private final int mRoamingProtocol;
    private final int mSkip464Xlat;
    private final String mUser;
    private final int mWaitTime;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ApnType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface AuthType {
    }

    public static class Builder {
        private String mApnName;
        private int mApnSetId;
        private int mApnTypeBitmask;
        private int mAuthType;
        private boolean mCarrierEnabled;
        private int mCarrierId = -1;
        private String mEntryName;
        private int mId;
        private int mMaxConns;
        private int mMaxConnsTime;
        private String mMmsProxyAddress;
        private int mMmsProxyPort = -1;
        private Uri mMmsc;
        private boolean mModemCognitive;
        private int mMtu;
        private String mMvnoMatchData;
        private int mMvnoType = -1;
        private int mNetworkTypeBitmask;
        private String mOperatorNumeric;
        private String mPassword;
        private int mProfileId;
        private int mProtocol = -1;
        private String mProxyAddress;
        private int mProxyPort = -1;
        private int mRoamingProtocol = -1;
        private int mSkip464Xlat = -1;
        private String mUser;
        private int mWaitTime;

        private Builder setId(int id) {
            this.mId = id;
            return this;
        }

        public Builder setMtu(int mtu) {
            this.mMtu = mtu;
            return this;
        }

        public Builder setProfileId(int profileId) {
            this.mProfileId = profileId;
            return this;
        }

        public Builder setModemCognitive(boolean modemCognitive) {
            this.mModemCognitive = modemCognitive;
            return this;
        }

        public Builder setMaxConns(int maxConns) {
            this.mMaxConns = maxConns;
            return this;
        }

        public Builder setWaitTime(int waitTime) {
            this.mWaitTime = waitTime;
            return this;
        }

        public Builder setMaxConnsTime(int maxConnsTime) {
            this.mMaxConnsTime = maxConnsTime;
            return this;
        }

        public Builder setMvnoMatchData(String mvnoMatchData) {
            this.mMvnoMatchData = mvnoMatchData;
            return this;
        }

        public Builder setApnSetId(int apnSetId) {
            this.mApnSetId = apnSetId;
            return this;
        }

        public Builder setEntryName(String entryName) {
            this.mEntryName = entryName;
            return this;
        }

        public Builder setApnName(String apnName) {
            this.mApnName = apnName;
            return this;
        }

        @Deprecated
        public Builder setProxyAddress(InetAddress proxy) {
            this.mProxyAddress = ApnSetting.inetAddressToString(proxy);
            return this;
        }

        public Builder setProxyAddress(String proxy) {
            this.mProxyAddress = proxy;
            return this;
        }

        public Builder setProxyPort(int port) {
            this.mProxyPort = port;
            return this;
        }

        public Builder setMmsc(Uri mmsc) {
            this.mMmsc = mmsc;
            return this;
        }

        @Deprecated
        public Builder setMmsProxyAddress(InetAddress mmsProxy) {
            this.mMmsProxyAddress = ApnSetting.inetAddressToString(mmsProxy);
            return this;
        }

        public Builder setMmsProxyAddress(String mmsProxy) {
            this.mMmsProxyAddress = mmsProxy;
            return this;
        }

        public Builder setMmsProxyPort(int mmsPort) {
            this.mMmsProxyPort = mmsPort;
            return this;
        }

        public Builder setUser(String user) {
            this.mUser = user;
            return this;
        }

        public Builder setPassword(String password) {
            this.mPassword = password;
            return this;
        }

        public Builder setAuthType(int authType) {
            this.mAuthType = authType;
            return this;
        }

        public Builder setApnTypeBitmask(int apnTypeBitmask) {
            this.mApnTypeBitmask = apnTypeBitmask;
            return this;
        }

        public Builder setOperatorNumeric(String operatorNumeric) {
            this.mOperatorNumeric = operatorNumeric;
            return this;
        }

        public Builder setProtocol(int protocol) {
            this.mProtocol = protocol;
            return this;
        }

        public Builder setRoamingProtocol(int roamingProtocol) {
            this.mRoamingProtocol = roamingProtocol;
            return this;
        }

        public Builder setCarrierEnabled(boolean carrierEnabled) {
            this.mCarrierEnabled = carrierEnabled;
            return this;
        }

        public Builder setNetworkTypeBitmask(int networkTypeBitmask) {
            this.mNetworkTypeBitmask = networkTypeBitmask;
            return this;
        }

        public Builder setMvnoType(int mvnoType) {
            this.mMvnoType = mvnoType;
            return this;
        }

        public Builder setCarrierId(int carrierId) {
            this.mCarrierId = carrierId;
            return this;
        }

        public Builder setSkip464Xlat(int skip464xlat) {
            this.mSkip464Xlat = skip464xlat;
            return this;
        }

        public ApnSetting build() {
            if ((this.mApnTypeBitmask & 255) == 0 || TextUtils.isEmpty(this.mApnName) || TextUtils.isEmpty(this.mEntryName)) {
                return null;
            }
            return new ApnSetting(this, null);
        }

        public ApnSetting buildWithoutCheck() {
            return new ApnSetting(this, null);
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface MvnoType {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface ProtocolType {
    }

    /* synthetic */ ApnSetting(Builder x0, AnonymousClass1 x1) {
        this(x0);
    }

    static {
        APN_TYPE_STRING_MAP.put("*", Integer.valueOf(255));
        Map map = APN_TYPE_STRING_MAP;
        Integer valueOf = Integer.valueOf(17);
        String str = "default";
        map.put(str, valueOf);
        map = APN_TYPE_STRING_MAP;
        Integer valueOf2 = Integer.valueOf(2);
        String str2 = PhoneConstants.APN_TYPE_MMS;
        map.put(str2, valueOf2);
        map = APN_TYPE_STRING_MAP;
        Integer valueOf3 = Integer.valueOf(4);
        String str3 = PhoneConstants.APN_TYPE_SUPL;
        map.put(str3, valueOf3);
        map = APN_TYPE_STRING_MAP;
        Integer valueOf4 = Integer.valueOf(8);
        String str4 = PhoneConstants.APN_TYPE_DUN;
        map.put(str4, valueOf4);
        map = APN_TYPE_STRING_MAP;
        Integer valueOf5 = Integer.valueOf(16);
        String str5 = PhoneConstants.APN_TYPE_HIPRI;
        map.put(str5, valueOf5);
        map = APN_TYPE_STRING_MAP;
        Integer valueOf6 = Integer.valueOf(32);
        String str6 = PhoneConstants.APN_TYPE_FOTA;
        map.put(str6, valueOf6);
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_IMS, Integer.valueOf(64));
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_CBS, Integer.valueOf(128));
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_IA, Integer.valueOf(256));
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_EMERGENCY, Integer.valueOf(512));
        APN_TYPE_STRING_MAP.put(PhoneConstants.APN_TYPE_MCX, Integer.valueOf(1024));
        APN_TYPE_INT_MAP.put(valueOf, str);
        APN_TYPE_INT_MAP.put(valueOf2, str2);
        APN_TYPE_INT_MAP.put(valueOf3, str3);
        APN_TYPE_INT_MAP.put(valueOf4, str4);
        APN_TYPE_INT_MAP.put(Integer.valueOf(16), str5);
        APN_TYPE_INT_MAP.put(Integer.valueOf(32), str6);
        APN_TYPE_INT_MAP.put(Integer.valueOf(64), PhoneConstants.APN_TYPE_IMS);
        APN_TYPE_INT_MAP.put(Integer.valueOf(128), PhoneConstants.APN_TYPE_CBS);
        APN_TYPE_INT_MAP.put(Integer.valueOf(256), PhoneConstants.APN_TYPE_IA);
        APN_TYPE_INT_MAP.put(Integer.valueOf(512), PhoneConstants.APN_TYPE_EMERGENCY);
        APN_TYPE_INT_MAP.put(Integer.valueOf(1024), PhoneConstants.APN_TYPE_MCX);
        map = PROTOCOL_STRING_MAP;
        valueOf = Integer.valueOf(0);
        map.put("IP", valueOf);
        map = PROTOCOL_STRING_MAP;
        Integer valueOf7 = Integer.valueOf(1);
        map.put("IPV6", valueOf7);
        PROTOCOL_STRING_MAP.put("IPV4V6", valueOf2);
        map = PROTOCOL_STRING_MAP;
        Integer valueOf8 = Integer.valueOf(3);
        map.put("PPP", valueOf8);
        PROTOCOL_STRING_MAP.put("NON-IP", valueOf3);
        PROTOCOL_STRING_MAP.put("UNSTRUCTURED", Integer.valueOf(5));
        PROTOCOL_INT_MAP.put(valueOf, "IP");
        PROTOCOL_INT_MAP.put(valueOf7, "IPV6");
        PROTOCOL_INT_MAP.put(valueOf2, "IPV4V6");
        PROTOCOL_INT_MAP.put(valueOf8, "PPP");
        PROTOCOL_INT_MAP.put(valueOf3, "NON-IP");
        PROTOCOL_INT_MAP.put(Integer.valueOf(5), "UNSTRUCTURED");
        MVNO_TYPE_STRING_MAP.put("spn", valueOf);
        MVNO_TYPE_STRING_MAP.put(SubscriptionManager.IMSI, valueOf7);
        MVNO_TYPE_STRING_MAP.put("gid", valueOf2);
        MVNO_TYPE_STRING_MAP.put("iccid", valueOf8);
        MVNO_TYPE_INT_MAP.put(valueOf, "spn");
        MVNO_TYPE_INT_MAP.put(valueOf7, SubscriptionManager.IMSI);
        MVNO_TYPE_INT_MAP.put(valueOf2, "gid");
        MVNO_TYPE_INT_MAP.put(valueOf8, "iccid");
    }

    public int getMtu() {
        return this.mMtu;
    }

    public int getProfileId() {
        return this.mProfileId;
    }

    public boolean isPersistent() {
        return this.mPersistent;
    }

    public int getMaxConns() {
        return this.mMaxConns;
    }

    public int getWaitTime() {
        return this.mWaitTime;
    }

    public int getMaxConnsTime() {
        return this.mMaxConnsTime;
    }

    public String getMvnoMatchData() {
        return this.mMvnoMatchData;
    }

    public int getApnSetId() {
        return this.mApnSetId;
    }

    public boolean getPermanentFailed() {
        return this.mPermanentFailed;
    }

    public void setPermanentFailed(boolean permanentFailed) {
        this.mPermanentFailed = permanentFailed;
    }

    public String getEntryName() {
        return this.mEntryName;
    }

    public String getApnName() {
        return this.mApnName;
    }

    @Deprecated
    public InetAddress getProxyAddress() {
        return inetAddressFromString(this.mProxyAddress);
    }

    public String getProxyAddressAsString() {
        return this.mProxyAddress;
    }

    public int getProxyPort() {
        return this.mProxyPort;
    }

    public Uri getMmsc() {
        return this.mMmsc;
    }

    @Deprecated
    public InetAddress getMmsProxyAddress() {
        return inetAddressFromString(this.mMmsProxyAddress);
    }

    public String getMmsProxyAddressAsString() {
        return this.mMmsProxyAddress;
    }

    public int getMmsProxyPort() {
        return this.mMmsProxyPort;
    }

    public String getUser() {
        return this.mUser;
    }

    public String getPassword() {
        return this.mPassword;
    }

    public int getAuthType() {
        return this.mAuthType;
    }

    public int getApnTypeBitmask() {
        return this.mApnTypeBitmask;
    }

    public int getId() {
        return this.mId;
    }

    public String getOperatorNumeric() {
        return this.mOperatorNumeric;
    }

    public int getProtocol() {
        return this.mProtocol;
    }

    public int getRoamingProtocol() {
        return this.mRoamingProtocol;
    }

    public boolean isEnabled() {
        return this.mCarrierEnabled;
    }

    public int getNetworkTypeBitmask() {
        return this.mNetworkTypeBitmask;
    }

    public int getMvnoType() {
        return this.mMvnoType;
    }

    public int getCarrierId() {
        return this.mCarrierId;
    }

    public int getSkip464Xlat() {
        return this.mSkip464Xlat;
    }

    private ApnSetting(Builder builder) {
        this.mPermanentFailed = false;
        this.mEntryName = builder.mEntryName;
        this.mApnName = builder.mApnName;
        this.mProxyAddress = builder.mProxyAddress;
        this.mProxyPort = builder.mProxyPort;
        this.mMmsc = builder.mMmsc;
        this.mMmsProxyAddress = builder.mMmsProxyAddress;
        this.mMmsProxyPort = builder.mMmsProxyPort;
        this.mUser = builder.mUser;
        this.mPassword = builder.mPassword;
        this.mAuthType = builder.mAuthType;
        this.mApnTypeBitmask = builder.mApnTypeBitmask;
        this.mId = builder.mId;
        this.mOperatorNumeric = builder.mOperatorNumeric;
        this.mProtocol = builder.mProtocol;
        this.mRoamingProtocol = builder.mRoamingProtocol;
        this.mMtu = builder.mMtu;
        this.mCarrierEnabled = builder.mCarrierEnabled;
        this.mNetworkTypeBitmask = builder.mNetworkTypeBitmask;
        this.mProfileId = builder.mProfileId;
        this.mPersistent = builder.mModemCognitive;
        this.mMaxConns = builder.mMaxConns;
        this.mWaitTime = builder.mWaitTime;
        this.mMaxConnsTime = builder.mMaxConnsTime;
        this.mMvnoType = builder.mMvnoType;
        this.mMvnoMatchData = builder.mMvnoMatchData;
        this.mApnSetId = builder.mApnSetId;
        this.mCarrierId = builder.mCarrierId;
        this.mSkip464Xlat = builder.mSkip464Xlat;
    }

    public static ApnSetting makeApnSetting(int id, String operatorNumeric, String entryName, String apnName, String proxyAddress, int proxyPort, Uri mmsc, String mmsProxyAddress, int mmsProxyPort, String user, String password, int authType, int mApnTypeBitmask, int protocol, int roamingProtocol, boolean carrierEnabled, int networkTypeBitmask, int profileId, boolean modemCognitive, int maxConns, int waitTime, int maxConnsTime, int mtu, int mvnoType, String mvnoMatchData, int apnSetId, int carrierId, int skip464xlat) {
        return new Builder().setId(id).setOperatorNumeric(operatorNumeric).setEntryName(entryName).setApnName(apnName).setProxyAddress(proxyAddress).setProxyPort(proxyPort).setMmsc(mmsc).setMmsProxyAddress(mmsProxyAddress).setMmsProxyPort(mmsProxyPort).setUser(user).setPassword(password).setAuthType(authType).setApnTypeBitmask(mApnTypeBitmask).setProtocol(protocol).setRoamingProtocol(roamingProtocol).setCarrierEnabled(carrierEnabled).setNetworkTypeBitmask(networkTypeBitmask).setProfileId(profileId).setModemCognitive(modemCognitive).setMaxConns(maxConns).setWaitTime(waitTime).setMaxConnsTime(maxConnsTime).setMtu(mtu).setMvnoType(mvnoType).setMvnoMatchData(mvnoMatchData).setApnSetId(apnSetId).setCarrierId(carrierId).setSkip464Xlat(skip464xlat).buildWithoutCheck();
    }

    public static ApnSetting makeApnSetting(int id, String operatorNumeric, String entryName, String apnName, String proxyAddress, int proxyPort, Uri mmsc, String mmsProxyAddress, int mmsProxyPort, String user, String password, int authType, int mApnTypeBitmask, int protocol, int roamingProtocol, boolean carrierEnabled, int networkTypeBitmask, int profileId, boolean modemCognitive, int maxConns, int waitTime, int maxConnsTime, int mtu, int mvnoType, String mvnoMatchData) {
        return makeApnSetting(id, operatorNumeric, entryName, apnName, proxyAddress, proxyPort, mmsc, mmsProxyAddress, mmsProxyPort, user, password, authType, mApnTypeBitmask, protocol, roamingProtocol, carrierEnabled, networkTypeBitmask, profileId, modemCognitive, maxConns, waitTime, maxConnsTime, mtu, mvnoType, mvnoMatchData, 0, -1, -1);
    }

    public static ApnSetting makeApnSetting(Cursor cursor) {
        int networkTypeBitmask;
        Cursor cursor2 = cursor;
        int apnTypesBitmask = getApnTypesBitmaskFromString(cursor2.getString(cursor2.getColumnIndexOrThrow("type")));
        int networkTypeBitmask2 = cursor2.getInt(cursor2.getColumnIndexOrThrow(Carriers.NETWORK_TYPE_BITMASK));
        if (networkTypeBitmask2 == 0) {
            networkTypeBitmask = ServiceState.convertBearerBitmaskToNetworkTypeBitmask(cursor2.getInt(cursor2.getColumnIndexOrThrow(Carriers.BEARER_BITMASK)));
        } else {
            networkTypeBitmask = networkTypeBitmask2;
        }
        return makeApnSetting(cursor2.getInt(cursor2.getColumnIndexOrThrow("_id")), cursor2.getString(cursor2.getColumnIndexOrThrow(Carriers.NUMERIC)), cursor2.getString(cursor2.getColumnIndexOrThrow("name")), cursor2.getString(cursor2.getColumnIndexOrThrow("apn")), cursor2.getString(cursor2.getColumnIndexOrThrow("proxy")), portFromString(cursor2.getString(cursor2.getColumnIndexOrThrow("port"))), UriFromString(cursor2.getString(cursor2.getColumnIndexOrThrow(Carriers.MMSC))), cursor2.getString(cursor2.getColumnIndexOrThrow(Carriers.MMSPROXY)), portFromString(cursor2.getString(cursor2.getColumnIndexOrThrow(Carriers.MMSPORT))), cursor2.getString(cursor2.getColumnIndexOrThrow("user")), cursor2.getString(cursor2.getColumnIndexOrThrow("password")), cursor2.getInt(cursor2.getColumnIndexOrThrow(Carriers.AUTH_TYPE)), apnTypesBitmask, getProtocolIntFromString(cursor2.getString(cursor2.getColumnIndexOrThrow("protocol"))), getProtocolIntFromString(cursor2.getString(cursor2.getColumnIndexOrThrow(Carriers.ROAMING_PROTOCOL))), cursor2.getInt(cursor2.getColumnIndexOrThrow(Carriers.CARRIER_ENABLED)) == 1, networkTypeBitmask, cursor2.getInt(cursor2.getColumnIndexOrThrow(Carriers.PROFILE_ID)), cursor2.getInt(cursor2.getColumnIndexOrThrow(Carriers.MODEM_PERSIST)) == 1, cursor2.getInt(cursor2.getColumnIndexOrThrow(Carriers.MAX_CONNECTIONS)), cursor2.getInt(cursor2.getColumnIndexOrThrow(Carriers.WAIT_TIME_RETRY)), cursor2.getInt(cursor2.getColumnIndexOrThrow(Carriers.TIME_LIMIT_FOR_MAX_CONNECTIONS)), cursor2.getInt(cursor2.getColumnIndexOrThrow("mtu")), getMvnoTypeIntFromString(cursor2.getString(cursor2.getColumnIndexOrThrow("mvno_type"))), cursor2.getString(cursor2.getColumnIndexOrThrow("mvno_match_data")), cursor2.getInt(cursor2.getColumnIndexOrThrow(Carriers.APN_SET_ID)), cursor2.getInt(cursor2.getColumnIndexOrThrow("carrier_id")), cursor2.getInt(cursor2.getColumnIndexOrThrow(Carriers.SKIP_464XLAT)));
    }

    public static ApnSetting makeApnSetting(ApnSetting apn) {
        ApnSetting apnSetting = apn;
        int i = apnSetting.mId;
        return makeApnSetting(i, apnSetting.mOperatorNumeric, apnSetting.mEntryName, apnSetting.mApnName, apnSetting.mProxyAddress, apnSetting.mProxyPort, apnSetting.mMmsc, apnSetting.mMmsProxyAddress, apnSetting.mMmsProxyPort, apnSetting.mUser, apnSetting.mPassword, apnSetting.mAuthType, apnSetting.mApnTypeBitmask, apnSetting.mProtocol, apnSetting.mRoamingProtocol, apnSetting.mCarrierEnabled, apnSetting.mNetworkTypeBitmask, apnSetting.mProfileId, apnSetting.mPersistent, apnSetting.mMaxConns, apnSetting.mWaitTime, apnSetting.mMaxConnsTime, apnSetting.mMtu, apnSetting.mMvnoType, apnSetting.mMvnoMatchData, apnSetting.mApnSetId, apnSetting.mCarrierId, apnSetting.mSkip464Xlat);
    }

    public static ApnSetting fromString(String data) {
        String str = data;
        if (str == null) {
            return null;
        }
        int version;
        String data2;
        String str2 = "";
        if (str.matches("^\\[ApnSettingV7\\]\\s*.*")) {
            str = str.replaceFirst(V7_FORMAT_REGEX, str2);
            version = 7;
            data2 = str;
        } else if (str.matches("^\\[ApnSettingV6\\]\\s*.*")) {
            str = str.replaceFirst(V6_FORMAT_REGEX, str2);
            version = 6;
            data2 = str;
        } else if (str.matches("^\\[ApnSettingV5\\]\\s*.*")) {
            str = str.replaceFirst(V5_FORMAT_REGEX, str2);
            version = 5;
            data2 = str;
        } else if (str.matches("^\\[ApnSettingV4\\]\\s*.*")) {
            str = str.replaceFirst(V4_FORMAT_REGEX, str2);
            version = 4;
            data2 = str;
        } else if (str.matches("^\\[ApnSettingV3\\]\\s*.*")) {
            str = str.replaceFirst(V3_FORMAT_REGEX, str2);
            version = 3;
            data2 = str;
        } else if (str.matches("^\\[ApnSettingV2\\]\\s*.*")) {
            str = str.replaceFirst(V2_FORMAT_REGEX, str2);
            version = 2;
            data2 = str;
        } else {
            version = 1;
            data2 = str;
        }
        String[] a = data2.split("\\s*,\\s*", -1);
        if (a.length < 14) {
            return null;
        }
        int authType;
        String[] typeArray;
        String roamingProtocol;
        boolean carrierEnabled;
        int profileId;
        boolean modemCognitive;
        int maxConns;
        int waitTime;
        int maxConnsTime;
        int mtu;
        String mvnoType;
        String mvnoMatchData;
        int apnSetId;
        int carrierId;
        int skip464xlat;
        String protocol;
        int bearerBitmask;
        String[] typeArray2;
        int networkTypeBitmask;
        int networkTypeBitmask2;
        try {
            authType = Integer.parseInt(a[12]);
        } catch (NumberFormatException e) {
            authType = null;
        }
        int profileId2 = 0;
        boolean modemCognitive2 = false;
        int maxConns2 = 0;
        int waitTime2 = 0;
        int maxConnsTime2 = 0;
        int mtu2 = 0;
        String mvnoType2 = "";
        String mvnoMatchData2 = "";
        int apnSetId2 = 0;
        int carrierId2 = -1;
        int networkTypeBitmask3 = 0;
        int bearerBitmask2;
        if (version == 1) {
            typeArray = new String[(a.length - 13)];
            bearerBitmask2 = 0;
            System.arraycopy(a, 13, typeArray, 0, a.length - 13);
            roamingProtocol = (String) PROTOCOL_INT_MAP.get(Integer.valueOf(0));
            carrierEnabled = true;
            profileId = 0;
            modemCognitive = false;
            maxConns = 0;
            waitTime = 0;
            maxConnsTime = 0;
            mtu = 0;
            mvnoType = mvnoType2;
            mvnoMatchData = mvnoMatchData2;
            apnSetId = 0;
            carrierId = -1;
            skip464xlat = -1;
            protocol = (String) PROTOCOL_INT_MAP.get(Integer.valueOf(0));
            bearerBitmask = bearerBitmask2;
            typeArray2 = typeArray;
            networkTypeBitmask = networkTypeBitmask3;
        } else {
            bearerBitmask2 = 0;
            if (a.length < 18) {
                return null;
            }
            String[] typeArray3 = a[13].split("\\s*\\|\\s*");
            String protocol2 = a[14];
            String roamingProtocol2 = a[15];
            boolean carrierEnabled2 = Boolean.parseBoolean(a[16]);
            bearerBitmask2 = ServiceState.getBitmaskFromString(a[17]);
            if (a.length > 22) {
                modemCognitive2 = Boolean.parseBoolean(a[19]);
                try {
                    profileId2 = Integer.parseInt(a[18]);
                    maxConns2 = Integer.parseInt(a[20]);
                    waitTime2 = Integer.parseInt(a[21]);
                    maxConnsTime2 = Integer.parseInt(a[22]);
                } catch (NumberFormatException e2) {
                }
            }
            if (a.length > 23) {
                try {
                    mtu2 = Integer.parseInt(a[23]);
                } catch (NumberFormatException e3) {
                }
            }
            if (a.length > 25) {
                str = a[24];
                mvnoMatchData2 = a[25];
                mvnoType2 = str;
            }
            if (a.length > 26) {
                networkTypeBitmask = ServiceState.getBitmaskFromString(a[26]);
            } else {
                networkTypeBitmask = networkTypeBitmask3;
            }
            if (a.length > 27) {
                apnSetId2 = Integer.parseInt(a[27]);
            }
            if (a.length > 28) {
                carrierId2 = Integer.parseInt(a[28]);
            }
            if (a.length > 29) {
                try {
                    skip464xlat = Integer.parseInt(a[29]);
                    profileId = profileId2;
                    modemCognitive = modemCognitive2;
                    maxConns = maxConns2;
                    maxConnsTime = maxConnsTime2;
                    mtu = mtu2;
                    mvnoType = mvnoType2;
                    mvnoMatchData = mvnoMatchData2;
                    apnSetId = apnSetId2;
                    carrierId = carrierId2;
                    protocol = protocol2;
                    roamingProtocol = roamingProtocol2;
                    bearerBitmask = bearerBitmask2;
                    carrierEnabled = carrierEnabled2;
                    typeArray2 = typeArray3;
                    waitTime = waitTime2;
                } catch (NumberFormatException e4) {
                }
            }
            profileId = profileId2;
            modemCognitive = modemCognitive2;
            maxConns = maxConns2;
            maxConnsTime = maxConnsTime2;
            mtu = mtu2;
            mvnoType = mvnoType2;
            mvnoMatchData = mvnoMatchData2;
            apnSetId = apnSetId2;
            carrierId = carrierId2;
            skip464xlat = -1;
            protocol = protocol2;
            roamingProtocol = roamingProtocol2;
            bearerBitmask = bearerBitmask2;
            carrierEnabled = carrierEnabled2;
            typeArray2 = typeArray3;
            waitTime = waitTime2;
        }
        if (networkTypeBitmask == 0) {
            networkTypeBitmask2 = ServiceState.convertBearerBitmaskToNetworkTypeBitmask(bearerBitmask);
        } else {
            networkTypeBitmask2 = networkTypeBitmask;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(a[10]);
        stringBuilder.append(a[11]);
        typeArray = typeArray2;
        return makeApnSetting(-1, stringBuilder.toString(), a[0], a[1], a[2], portFromString(a[3]), UriFromString(a[7]), a[8], portFromString(a[9]), a[4], a[5], authType, getApnTypesBitmaskFromString(TextUtils.join((CharSequence) ",", (Object[]) typeArray)), getProtocolIntFromString(protocol), getProtocolIntFromString(roamingProtocol), carrierEnabled, networkTypeBitmask2, profileId, modemCognitive, maxConns, waitTime, maxConnsTime, mtu, getMvnoTypeIntFromString(mvnoType), mvnoMatchData, apnSetId, carrierId, skip464xlat);
    }

    public static List<ApnSetting> arrayFromString(String data) {
        List<ApnSetting> retVal = new ArrayList();
        if (TextUtils.isEmpty(data)) {
            return retVal;
        }
        for (String apnString : data.split("\\s*;\\s*")) {
            ApnSetting apn = fromString(apnString);
            if (apn != null) {
                retVal.add(apn);
            }
        }
        return retVal;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[ApnSettingV7] ");
        sb.append(this.mEntryName);
        String str = ", ";
        sb.append(str);
        sb.append(this.mId);
        sb.append(str);
        sb.append(this.mOperatorNumeric);
        sb.append(str);
        sb.append(this.mApnName);
        sb.append(str);
        sb.append(this.mProxyAddress);
        sb.append(str);
        sb.append(UriToString(this.mMmsc));
        sb.append(str);
        sb.append(this.mMmsProxyAddress);
        sb.append(str);
        sb.append(portToString(this.mMmsProxyPort));
        sb.append(str);
        sb.append(portToString(this.mProxyPort));
        sb.append(str);
        sb.append(this.mAuthType);
        sb.append(str);
        sb.append(TextUtils.join((CharSequence) " | ", getApnTypesStringFromBitmask(this.mApnTypeBitmask).split(",")));
        sb.append(str);
        sb.append((String) PROTOCOL_INT_MAP.get(Integer.valueOf(this.mProtocol)));
        sb.append(str);
        sb.append((String) PROTOCOL_INT_MAP.get(Integer.valueOf(this.mRoamingProtocol)));
        sb.append(str);
        sb.append(this.mCarrierEnabled);
        sb.append(str);
        sb.append(this.mProfileId);
        sb.append(str);
        sb.append(this.mPersistent);
        sb.append(str);
        sb.append(this.mMaxConns);
        sb.append(str);
        sb.append(this.mWaitTime);
        sb.append(str);
        sb.append(this.mMaxConnsTime);
        sb.append(str);
        sb.append(this.mMtu);
        sb.append(str);
        sb.append((String) MVNO_TYPE_INT_MAP.get(Integer.valueOf(this.mMvnoType)));
        sb.append(str);
        sb.append(this.mMvnoMatchData);
        sb.append(str);
        sb.append(this.mPermanentFailed);
        sb.append(str);
        sb.append(this.mNetworkTypeBitmask);
        sb.append(str);
        sb.append(this.mApnSetId);
        sb.append(str);
        sb.append(this.mCarrierId);
        sb.append(str);
        sb.append(this.mSkip464Xlat);
        return sb.toString();
    }

    public boolean hasMvnoParams() {
        return (TextUtils.isEmpty(getMvnoTypeStringFromInt(this.mMvnoType)) || TextUtils.isEmpty(this.mMvnoMatchData)) ? false : true;
    }

    private boolean hasApnType(int type) {
        return (this.mApnTypeBitmask & type) == type;
    }

    public boolean canHandleType(int type) {
        if (this.mCarrierEnabled && hasApnType(type)) {
            return true;
        }
        return false;
    }

    private boolean typeSameAny(ApnSetting first, ApnSetting second) {
        if ((first.mApnTypeBitmask & second.mApnTypeBitmask) != 0) {
            return true;
        }
        return false;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof ApnSetting)) {
            return false;
        }
        ApnSetting other = (ApnSetting) o;
        if (this.mEntryName.equals(other.mEntryName) && Objects.equals(Integer.valueOf(this.mId), Integer.valueOf(other.mId)) && Objects.equals(this.mOperatorNumeric, other.mOperatorNumeric) && Objects.equals(this.mApnName, other.mApnName) && Objects.equals(this.mProxyAddress, other.mProxyAddress) && Objects.equals(this.mMmsc, other.mMmsc) && Objects.equals(this.mMmsProxyAddress, other.mMmsProxyAddress) && Objects.equals(Integer.valueOf(this.mMmsProxyPort), Integer.valueOf(other.mMmsProxyPort)) && Objects.equals(Integer.valueOf(this.mProxyPort), Integer.valueOf(other.mProxyPort)) && Objects.equals(this.mUser, other.mUser) && Objects.equals(this.mPassword, other.mPassword) && Objects.equals(Integer.valueOf(this.mAuthType), Integer.valueOf(other.mAuthType)) && Objects.equals(Integer.valueOf(this.mApnTypeBitmask), Integer.valueOf(other.mApnTypeBitmask)) && Objects.equals(Integer.valueOf(this.mProtocol), Integer.valueOf(other.mProtocol)) && Objects.equals(Integer.valueOf(this.mRoamingProtocol), Integer.valueOf(other.mRoamingProtocol)) && Objects.equals(Boolean.valueOf(this.mCarrierEnabled), Boolean.valueOf(other.mCarrierEnabled)) && Objects.equals(Integer.valueOf(this.mProfileId), Integer.valueOf(other.mProfileId)) && Objects.equals(Boolean.valueOf(this.mPersistent), Boolean.valueOf(other.mPersistent)) && Objects.equals(Integer.valueOf(this.mMaxConns), Integer.valueOf(other.mMaxConns)) && Objects.equals(Integer.valueOf(this.mWaitTime), Integer.valueOf(other.mWaitTime)) && Objects.equals(Integer.valueOf(this.mMaxConnsTime), Integer.valueOf(other.mMaxConnsTime)) && Objects.equals(Integer.valueOf(this.mMtu), Integer.valueOf(other.mMtu)) && Objects.equals(Integer.valueOf(this.mMvnoType), Integer.valueOf(other.mMvnoType)) && Objects.equals(this.mMvnoMatchData, other.mMvnoMatchData) && Objects.equals(Integer.valueOf(this.mNetworkTypeBitmask), Integer.valueOf(other.mNetworkTypeBitmask)) && Objects.equals(Integer.valueOf(this.mApnSetId), Integer.valueOf(other.mApnSetId)) && Objects.equals(Integer.valueOf(this.mCarrierId), Integer.valueOf(other.mCarrierId)) && Objects.equals(Integer.valueOf(this.mSkip464Xlat), Integer.valueOf(other.mSkip464Xlat))) {
            z = true;
        }
        return z;
    }

    public boolean equals(Object o, boolean isDataRoaming) {
        boolean z = false;
        if (!(o instanceof ApnSetting)) {
            return false;
        }
        ApnSetting other = (ApnSetting) o;
        if (this.mEntryName.equals(other.mEntryName) && Objects.equals(this.mOperatorNumeric, other.mOperatorNumeric) && Objects.equals(this.mApnName, other.mApnName) && Objects.equals(this.mProxyAddress, other.mProxyAddress) && Objects.equals(this.mMmsc, other.mMmsc) && Objects.equals(this.mMmsProxyAddress, other.mMmsProxyAddress) && Objects.equals(Integer.valueOf(this.mMmsProxyPort), Integer.valueOf(other.mMmsProxyPort)) && Objects.equals(Integer.valueOf(this.mProxyPort), Integer.valueOf(other.mProxyPort)) && Objects.equals(this.mUser, other.mUser) && Objects.equals(this.mPassword, other.mPassword) && Objects.equals(Integer.valueOf(this.mAuthType), Integer.valueOf(other.mAuthType)) && Objects.equals(Integer.valueOf(this.mApnTypeBitmask), Integer.valueOf(other.mApnTypeBitmask)) && ((isDataRoaming || Objects.equals(Integer.valueOf(this.mProtocol), Integer.valueOf(other.mProtocol))) && ((!isDataRoaming || Objects.equals(Integer.valueOf(this.mRoamingProtocol), Integer.valueOf(other.mRoamingProtocol))) && Objects.equals(Boolean.valueOf(this.mCarrierEnabled), Boolean.valueOf(other.mCarrierEnabled)) && Objects.equals(Integer.valueOf(this.mProfileId), Integer.valueOf(other.mProfileId)) && Objects.equals(Boolean.valueOf(this.mPersistent), Boolean.valueOf(other.mPersistent)) && Objects.equals(Integer.valueOf(this.mMaxConns), Integer.valueOf(other.mMaxConns)) && Objects.equals(Integer.valueOf(this.mWaitTime), Integer.valueOf(other.mWaitTime)) && Objects.equals(Integer.valueOf(this.mMaxConnsTime), Integer.valueOf(other.mMaxConnsTime)) && Objects.equals(Integer.valueOf(this.mMtu), Integer.valueOf(other.mMtu)) && Objects.equals(Integer.valueOf(this.mMvnoType), Integer.valueOf(other.mMvnoType)) && Objects.equals(this.mMvnoMatchData, other.mMvnoMatchData) && Objects.equals(Integer.valueOf(this.mApnSetId), Integer.valueOf(other.mApnSetId)) && Objects.equals(Integer.valueOf(this.mCarrierId), Integer.valueOf(other.mCarrierId)) && Objects.equals(Integer.valueOf(this.mSkip464Xlat), Integer.valueOf(other.mSkip464Xlat))))) {
            z = true;
        }
        return z;
    }

    public boolean similar(ApnSetting other) {
        return !canHandleType(8) && !other.canHandleType(8) && Objects.equals(this.mApnName, other.mApnName) && !typeSameAny(this, other) && xorEqualsString(this.mProxyAddress, other.mProxyAddress) && xorEqualsInt(this.mProxyPort, other.mProxyPort) && xorEqualsInt(this.mProtocol, other.mProtocol) && xorEqualsInt(this.mRoamingProtocol, other.mRoamingProtocol) && Objects.equals(Boolean.valueOf(this.mCarrierEnabled), Boolean.valueOf(other.mCarrierEnabled)) && Objects.equals(Integer.valueOf(this.mProfileId), Integer.valueOf(other.mProfileId)) && Objects.equals(Integer.valueOf(this.mMvnoType), Integer.valueOf(other.mMvnoType)) && Objects.equals(this.mMvnoMatchData, other.mMvnoMatchData) && xorEquals(this.mMmsc, other.mMmsc) && xorEqualsString(this.mMmsProxyAddress, other.mMmsProxyAddress) && xorEqualsInt(this.mMmsProxyPort, other.mMmsProxyPort) && Objects.equals(Integer.valueOf(this.mNetworkTypeBitmask), Integer.valueOf(other.mNetworkTypeBitmask)) && Objects.equals(Integer.valueOf(this.mApnSetId), Integer.valueOf(other.mApnSetId)) && Objects.equals(Integer.valueOf(this.mCarrierId), Integer.valueOf(other.mCarrierId)) && Objects.equals(Integer.valueOf(this.mSkip464Xlat), Integer.valueOf(other.mSkip464Xlat));
    }

    private boolean xorEquals(Object first, Object second) {
        return first == null || second == null || first.equals(second);
    }

    private boolean xorEqualsString(String first, String second) {
        return TextUtils.isEmpty(first) || TextUtils.isEmpty(second) || first.equals(second);
    }

    private boolean xorEqualsInt(int first, int second) {
        return first == -1 || second == -1 || Objects.equals(Integer.valueOf(first), Integer.valueOf(second));
    }

    private String nullToEmpty(String stringValue) {
        return stringValue == null ? "" : stringValue;
    }

    public ContentValues toContentValues() {
        ContentValues apnValue = new ContentValues();
        apnValue.put(Carriers.NUMERIC, nullToEmpty(this.mOperatorNumeric));
        apnValue.put("name", nullToEmpty(this.mEntryName));
        apnValue.put("apn", nullToEmpty(this.mApnName));
        apnValue.put("proxy", nullToEmpty(this.mProxyAddress));
        apnValue.put("port", nullToEmpty(portToString(this.mProxyPort)));
        apnValue.put(Carriers.MMSC, nullToEmpty(UriToString(this.mMmsc)));
        apnValue.put(Carriers.MMSPORT, nullToEmpty(portToString(this.mMmsProxyPort)));
        apnValue.put(Carriers.MMSPROXY, nullToEmpty(this.mMmsProxyAddress));
        apnValue.put("user", nullToEmpty(this.mUser));
        apnValue.put("password", nullToEmpty(this.mPassword));
        apnValue.put(Carriers.AUTH_TYPE, Integer.valueOf(this.mAuthType));
        apnValue.put("type", nullToEmpty(getApnTypesStringFromBitmask(this.mApnTypeBitmask)));
        apnValue.put("protocol", getProtocolStringFromInt(this.mProtocol));
        apnValue.put(Carriers.ROAMING_PROTOCOL, getProtocolStringFromInt(this.mRoamingProtocol));
        apnValue.put(Carriers.CARRIER_ENABLED, Boolean.valueOf(this.mCarrierEnabled));
        apnValue.put("mvno_type", getMvnoTypeStringFromInt(this.mMvnoType));
        apnValue.put(Carriers.NETWORK_TYPE_BITMASK, Integer.valueOf(this.mNetworkTypeBitmask));
        apnValue.put("carrier_id", Integer.valueOf(this.mCarrierId));
        apnValue.put(Carriers.SKIP_464XLAT, Integer.valueOf(this.mSkip464Xlat));
        return apnValue;
    }

    public List<Integer> getApnTypes() {
        List<Integer> types = new ArrayList();
        for (Integer type : APN_TYPE_INT_MAP.keySet()) {
            if ((this.mApnTypeBitmask & type.intValue()) == type.intValue()) {
                types.add(type);
            }
        }
        return types;
    }

    public static String getApnTypesStringFromBitmask(int apnTypeBitmask) {
        Iterable types = new ArrayList();
        for (Integer type : APN_TYPE_INT_MAP.keySet()) {
            if ((type.intValue() & apnTypeBitmask) == type.intValue()) {
                types.add((String) APN_TYPE_INT_MAP.get(type));
            }
        }
        return TextUtils.join((CharSequence) ",", types);
    }

    public static String getApnTypeString(int apnType) {
        if (apnType == 255) {
            return "*";
        }
        String apnTypeString = (String) APN_TYPE_INT_MAP.get(Integer.valueOf(apnType));
        return apnTypeString == null ? "Unknown" : apnTypeString;
    }

    public static int getApnTypesBitmaskFromString(String types) {
        if (TextUtils.isEmpty(types)) {
            return 255;
        }
        int result = 0;
        for (String str : types.split(",")) {
            Integer type = (Integer) APN_TYPE_STRING_MAP.get(str.toLowerCase());
            if (type != null) {
                result |= type.intValue();
            }
        }
        return result;
    }

    public static int getMvnoTypeIntFromString(String mvnoType) {
        Integer mvnoTypeInt = (Integer) MVNO_TYPE_STRING_MAP.get(TextUtils.isEmpty(mvnoType) ? mvnoType : mvnoType.toLowerCase());
        return mvnoTypeInt == null ? -1 : mvnoTypeInt.intValue();
    }

    public static String getMvnoTypeStringFromInt(int mvnoType) {
        String mvnoTypeString = (String) MVNO_TYPE_INT_MAP.get(Integer.valueOf(mvnoType));
        return mvnoTypeString == null ? "" : mvnoTypeString;
    }

    public static int getProtocolIntFromString(String protocol) {
        Integer protocolInt = (Integer) PROTOCOL_STRING_MAP.get(protocol);
        return protocolInt == null ? -1 : protocolInt.intValue();
    }

    public static String getProtocolStringFromInt(int protocol) {
        String protocolString = (String) PROTOCOL_INT_MAP.get(Integer.valueOf(protocol));
        return protocolString == null ? "" : protocolString;
    }

    private static Uri UriFromString(String uri) {
        return TextUtils.isEmpty(uri) ? null : Uri.parse(uri);
    }

    private static String UriToString(Uri uri) {
        return uri == null ? null : uri.toString();
    }

    public static InetAddress inetAddressFromString(String inetAddress) {
        if (TextUtils.isEmpty(inetAddress)) {
            return null;
        }
        try {
            return InetAddress.getByName(inetAddress);
        } catch (UnknownHostException e) {
            Log.e(LOG_TAG, "Can't parse InetAddress from string: unknown host.");
            return null;
        }
    }

    public static String inetAddressToString(InetAddress inetAddress) {
        if (inetAddress == null) {
            return null;
        }
        String inetAddressString = inetAddress.toString();
        if (TextUtils.isEmpty(inetAddressString)) {
            return null;
        }
        String str = "/";
        String hostName = inetAddressString.substring(null, inetAddressString.indexOf(str));
        str = inetAddressString.substring(inetAddressString.indexOf(str) + 1);
        if (TextUtils.isEmpty(hostName) && TextUtils.isEmpty(str)) {
            return null;
        }
        return TextUtils.isEmpty(hostName) ? str : hostName;
    }

    private static int portFromString(String strPort) {
        if (TextUtils.isEmpty(strPort)) {
            return -1;
        }
        try {
            return Integer.parseInt(strPort);
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Can't parse port from String");
            return -1;
        }
    }

    private static String portToString(int port) {
        return port == -1 ? null : Integer.toString(port);
    }

    public boolean canSupportNetworkType(int networkType) {
        if (networkType != 16 || (((long) this.mNetworkTypeBitmask) & 3) == 0) {
            return ServiceState.bitmaskHasTech(this.mNetworkTypeBitmask, networkType);
        }
        return true;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mOperatorNumeric);
        dest.writeString(this.mEntryName);
        dest.writeString(this.mApnName);
        dest.writeString(this.mProxyAddress);
        dest.writeInt(this.mProxyPort);
        dest.writeValue(this.mMmsc);
        dest.writeString(this.mMmsProxyAddress);
        dest.writeInt(this.mMmsProxyPort);
        dest.writeString(this.mUser);
        dest.writeString(this.mPassword);
        dest.writeInt(this.mAuthType);
        dest.writeInt(this.mApnTypeBitmask);
        dest.writeInt(this.mProtocol);
        dest.writeInt(this.mRoamingProtocol);
        dest.writeBoolean(this.mCarrierEnabled);
        dest.writeInt(this.mMvnoType);
        dest.writeInt(this.mNetworkTypeBitmask);
        dest.writeInt(this.mApnSetId);
        dest.writeInt(this.mCarrierId);
        dest.writeInt(this.mSkip464Xlat);
    }

    private static ApnSetting readFromParcel(Parcel in) {
        return makeApnSetting(in.readInt(), in.readString(), in.readString(), in.readString(), in.readString(), in.readInt(), (Uri) in.readValue(Uri.class.getClassLoader()), in.readString(), in.readInt(), in.readString(), in.readString(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readBoolean(), in.readInt(), 0, false, 0, 0, 0, 0, in.readInt(), null, in.readInt(), in.readInt(), in.readInt());
    }
}
