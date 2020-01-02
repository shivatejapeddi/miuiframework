package android.net.wifi;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WifiDppConfig implements Parcelable {
    public static final Creator<WifiDppConfig> CREATOR = new Creator<WifiDppConfig>() {
        public WifiDppConfig createFromParcel(Parcel in) {
            WifiDppConfig config = new WifiDppConfig();
            config.peer_bootstrap_id = in.readInt();
            config.own_bootstrap_id = in.readInt();
            config.dpp_role = in.readInt();
            config.ssid = in.readString();
            config.passphrase = in.readString();
            config.isAp = in.readInt();
            config.isDpp = in.readInt();
            config.conf_id = in.readInt();
            config.bootstrap_type = in.readInt();
            config.chan_list = in.readString();
            config.mac_addr = in.readString();
            config.info = in.readString();
            config.curve = in.readString();
            config.expiry = in.readInt();
            config.key = in.readString();
            config.mEventResult.readFromParcel(in);
            return config;
        }

        public WifiDppConfig[] newArray(int size) {
            return new WifiDppConfig[size];
        }
    };
    public static final int DPP_INVALID_CONFIG_ID = -1;
    public static final int DPP_ROLE_CONFIGURATOR = 0;
    public static final int DPP_ROLE_ENROLLEE = 1;
    public static final int DPP_TYPE_NAN_BOOTSTRAP = 1;
    public static final int DPP_TYPE_QR_CODE = 0;
    private static final String TAG = "WifiDppConfig";
    public int bootstrap_type = -1;
    public String chan_list = null;
    public int conf_id = -1;
    public String curve = null;
    public int dpp_role = -1;
    public int expiry = 0;
    public String info = null;
    public int isAp = -1;
    public int isDpp = -1;
    public String key = null;
    private DppResult mEventResult = new DppResult();
    public String mac_addr = null;
    public int own_bootstrap_id = -1;
    public String passphrase = null;
    public int peer_bootstrap_id = -1;
    public String ssid = null;

    public static class DppResult {
        public static final int DPP_CONF_EVENT_TYPE_FAILED = 0;
        public static final int DPP_CONF_EVENT_TYPE_RECEIVED = 2;
        public static final int DPP_CONF_EVENT_TYPE_SENT = 1;
        public static final int DPP_EVENT_AUTH_SUCCESS = 0;
        public static final int DPP_EVENT_CONF = 4;
        public static final int DPP_EVENT_MISSING_AUTH = 5;
        public static final int DPP_EVENT_NETWORK_ID = 6;
        public static final int DPP_EVENT_NOT_COMPATIBLE = 1;
        public static final int DPP_EVENT_RESPONSE_PENDING = 2;
        public static final int DPP_EVENT_SCAN_PEER_QRCODE = 3;
        public byte authMissingParam = (byte) 0;
        public String cSignKey = null;
        public byte capab = (byte) 0;
        public byte configEventType = (byte) 0;
        public String connector = null;
        public String iBootstrapData = null;
        public boolean initiator = false;
        public String netAccessKey = null;
        public int netAccessKeyExpiry = 0;
        public int netID = -1;
        public String passphrase = null;
        public String psk = null;
        public String ssid = null;

        public void writeToParcel(Parcel dest) {
            dest.writeInt(this.initiator);
            dest.writeInt(this.netID);
            dest.writeByte(this.capab);
            dest.writeByte(this.authMissingParam);
            dest.writeByte(this.configEventType);
            dest.writeString(this.iBootstrapData);
            dest.writeString(this.ssid);
            dest.writeString(this.connector);
            dest.writeString(this.cSignKey);
            dest.writeString(this.netAccessKey);
            dest.writeInt(this.netAccessKeyExpiry);
            dest.writeString(this.passphrase);
            dest.writeString(this.psk);
        }

        public void readFromParcel(Parcel in) {
            this.initiator = in.readInt() > 0;
            this.netID = in.readInt();
            this.capab = in.readByte();
            this.authMissingParam = in.readByte();
            this.configEventType = in.readByte();
            this.iBootstrapData = in.readString();
            this.ssid = in.readString();
            this.connector = in.readString();
            this.cSignKey = in.readString();
            this.netAccessKey = in.readString();
            this.netAccessKeyExpiry = in.readInt();
            this.passphrase = in.readString();
            this.psk = in.readString();
        }
    }

    public DppResult getDppResult() {
        return this.mEventResult;
    }

    public void setDppResult(DppResult result) {
        this.mEventResult = result;
    }

    public void writeToParcel(Parcel dest) {
    }

    public void readFromParcel(Parcel in) {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.peer_bootstrap_id);
        dest.writeInt(this.own_bootstrap_id);
        dest.writeInt(this.dpp_role);
        dest.writeString(this.ssid);
        dest.writeString(this.passphrase);
        dest.writeInt(this.isAp);
        dest.writeInt(this.isDpp);
        dest.writeInt(this.conf_id);
        dest.writeInt(this.bootstrap_type);
        dest.writeString(this.chan_list);
        dest.writeString(this.mac_addr);
        dest.writeString(this.info);
        dest.writeString(this.curve);
        dest.writeInt(this.expiry);
        dest.writeString(this.key);
        this.mEventResult.writeToParcel(dest);
    }
}
