package android.net.wifi;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.PreciseDisconnectCause;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ScanResult implements Parcelable {
    public static final int CHANNEL_WIDTH_160MHZ = 3;
    public static final int CHANNEL_WIDTH_20MHZ = 0;
    public static final int CHANNEL_WIDTH_40MHZ = 1;
    public static final int CHANNEL_WIDTH_80MHZ = 2;
    public static final int CHANNEL_WIDTH_80MHZ_PLUS_MHZ = 4;
    public static final int CIPHER_CCMP = 3;
    public static final int CIPHER_GCMP_256 = 4;
    public static final int CIPHER_NONE = 0;
    public static final int CIPHER_NO_GROUP_ADDRESSED = 1;
    public static final int CIPHER_SMS4 = 5;
    public static final int CIPHER_TKIP = 2;
    @UnsupportedAppUsage
    public static final Creator<ScanResult> CREATOR = new Creator<ScanResult>() {
        public ScanResult createFromParcel(Parcel in) {
            int i;
            Parcel parcel = in;
            WifiSsid wifiSsid = null;
            boolean z = true;
            if (in.readInt() == 1) {
                wifiSsid = (WifiSsid) WifiSsid.CREATOR.createFromParcel(parcel);
            }
            ScanResult scanResult = new ScanResult(wifiSsid, in.readString(), in.readString(), in.readLong(), in.readInt(), in.readString(), in.readInt(), in.readInt(), in.readLong(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), false);
            scanResult.seen = in.readLong();
            scanResult.untrusted = in.readInt() != 0;
            scanResult.numUsage = in.readInt();
            scanResult.venueName = in.readString();
            scanResult.operatorFriendlyName = in.readString();
            scanResult.flags = in.readLong();
            int n = in.readInt();
            if (n != 0) {
                scanResult.informationElements = new InformationElement[n];
                for (i = 0; i < n; i++) {
                    scanResult.informationElements[i] = new InformationElement();
                    scanResult.informationElements[i].id = in.readInt();
                    scanResult.informationElements[i].bytes = new byte[in.readInt()];
                    parcel.readByteArray(scanResult.informationElements[i].bytes);
                }
            }
            n = in.readInt();
            if (n != 0) {
                scanResult.anqpLines = new ArrayList();
                for (i = 0; i < n; i++) {
                    scanResult.anqpLines.add(in.readString());
                }
            }
            n = in.readInt();
            if (n != 0) {
                scanResult.anqpElements = new AnqpInformationElement[n];
                for (i = 0; i < n; i++) {
                    int vendorId = in.readInt();
                    int elementId = in.readInt();
                    byte[] payload = new byte[in.readInt()];
                    parcel.readByteArray(payload);
                    scanResult.anqpElements[i] = new AnqpInformationElement(vendorId, elementId, payload);
                }
            }
            if (in.readInt() == 0) {
                z = false;
            }
            scanResult.isCarrierAp = z;
            scanResult.carrierApEapType = in.readInt();
            scanResult.carrierName = in.readString();
            int n2 = in.readInt();
            if (n2 != 0) {
                scanResult.radioChainInfos = new RadioChainInfo[n2];
                for (n = 0; n < n2; n++) {
                    scanResult.radioChainInfos[n] = new RadioChainInfo();
                    scanResult.radioChainInfos[n].id = in.readInt();
                    scanResult.radioChainInfos[n].level = in.readInt();
                }
            }
            return scanResult;
        }

        public ScanResult[] newArray(int size) {
            return new ScanResult[size];
        }
    };
    public static final long FLAG_80211mc_RESPONDER = 2;
    public static final long FLAG_PASSPOINT_NETWORK = 1;
    public static final int KEY_MGMT_DPP = 13;
    public static final int KEY_MGMT_EAP = 2;
    public static final int KEY_MGMT_EAP_SHA256 = 6;
    public static final int KEY_MGMT_EAP_SUITE_B_192 = 10;
    public static final int KEY_MGMT_FILS_SHA256 = 14;
    public static final int KEY_MGMT_FILS_SHA384 = 15;
    public static final int KEY_MGMT_FT_EAP = 4;
    public static final int KEY_MGMT_FT_PSK = 3;
    public static final int KEY_MGMT_FT_SAE = 11;
    public static final int KEY_MGMT_NONE = 0;
    public static final int KEY_MGMT_OSEN = 7;
    public static final int KEY_MGMT_OWE = 9;
    public static final int KEY_MGMT_OWE_TRANSITION = 12;
    public static final int KEY_MGMT_PSK = 1;
    public static final int KEY_MGMT_PSK_SHA256 = 5;
    public static final int KEY_MGMT_SAE = 8;
    public static final int KEY_MGMT_WAPI_CERT = 17;
    public static final int KEY_MGMT_WAPI_PSK = 16;
    public static final int PROTOCOL_NONE = 0;
    public static final int PROTOCOL_OSEN = 3;
    public static final int PROTOCOL_RSN = 2;
    public static final int PROTOCOL_WAPI = 4;
    public static final int PROTOCOL_WPA = 1;
    public static final int UNSPECIFIED = -1;
    public String BSSID;
    public String SSID;
    @UnsupportedAppUsage
    public int anqpDomainId;
    public AnqpInformationElement[] anqpElements;
    @UnsupportedAppUsage
    public List<String> anqpLines;
    public String capabilities;
    public int carrierApEapType;
    public String carrierName;
    public int centerFreq0;
    public int centerFreq1;
    public int channelWidth;
    @UnsupportedAppUsage
    public int distanceCm;
    @UnsupportedAppUsage
    public int distanceSdCm;
    @UnsupportedAppUsage
    public long flags;
    public int frequency;
    @UnsupportedAppUsage
    public long hessid;
    @UnsupportedAppUsage
    public InformationElement[] informationElements;
    @UnsupportedAppUsage
    public boolean is80211McRTTResponder;
    public boolean isCarrierAp;
    public int level;
    @UnsupportedAppUsage
    public int numUsage;
    public CharSequence operatorFriendlyName;
    public RadioChainInfo[] radioChainInfos;
    @UnsupportedAppUsage
    public long seen;
    public long timestamp;
    @SystemApi
    public boolean untrusted;
    public CharSequence venueName;
    @UnsupportedAppUsage
    public WifiSsid wifiSsid;

    public static class InformationElement {
        @UnsupportedAppUsage
        public static final int EID_BSS_LOAD = 11;
        @UnsupportedAppUsage
        public static final int EID_ERP = 42;
        @UnsupportedAppUsage
        public static final int EID_EXTENDED_CAPS = 127;
        @UnsupportedAppUsage
        public static final int EID_EXTENDED_SUPPORTED_RATES = 50;
        public static final int EID_EXTENSION = 255;
        public static final int EID_EXT_HE_CAPABILITIES = 35;
        public static final int EID_HT_CAPABILITIES = 45;
        @UnsupportedAppUsage
        public static final int EID_HT_OPERATION = 61;
        @UnsupportedAppUsage
        public static final int EID_INTERWORKING = 107;
        @UnsupportedAppUsage
        public static final int EID_ROAMING_CONSORTIUM = 111;
        @UnsupportedAppUsage
        public static final int EID_RSN = 48;
        @UnsupportedAppUsage
        public static final int EID_SSID = 0;
        @UnsupportedAppUsage
        public static final int EID_SUPPORTED_RATES = 1;
        @UnsupportedAppUsage
        public static final int EID_TIM = 5;
        public static final int EID_VHT_CAPABILITIES = 191;
        @UnsupportedAppUsage
        public static final int EID_VHT_OPERATION = 192;
        @UnsupportedAppUsage
        public static final int EID_VSA = 221;
        public static final int EID_WAPI = 68;
        @UnsupportedAppUsage
        public byte[] bytes;
        @UnsupportedAppUsage
        public int id;

        public InformationElement(InformationElement rhs) {
            this.id = rhs.id;
            this.bytes = (byte[]) rhs.bytes.clone();
        }
    }

    public static class RadioChainInfo {
        public int id;
        public int level;

        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RadioChainInfo: id=");
            stringBuilder.append(this.id);
            stringBuilder.append(", level=");
            stringBuilder.append(this.level);
            return stringBuilder.toString();
        }

        public boolean equals(Object otherObj) {
            boolean z = true;
            if (this == otherObj) {
                return true;
            }
            if (!(otherObj instanceof RadioChainInfo)) {
                return false;
            }
            RadioChainInfo other = (RadioChainInfo) otherObj;
            if (!(this.id == other.id && this.level == other.level)) {
                z = false;
            }
            return z;
        }

        public int hashCode() {
            return Objects.hash(new Object[]{Integer.valueOf(this.id), Integer.valueOf(this.level)});
        }
    }

    public void setFlag(long flag) {
        this.flags |= flag;
    }

    public void clearFlag(long flag) {
        this.flags &= ~flag;
    }

    public boolean is80211mcResponder() {
        return (this.flags & 2) != 0;
    }

    public boolean isPasspointNetwork() {
        return (this.flags & 1) != 0;
    }

    public boolean is24GHz() {
        return is24GHz(this.frequency);
    }

    public static boolean is24GHz(int freq) {
        return freq > PreciseDisconnectCause.IWLAN_DPD_FAILURE && freq < PreciseDisconnectCause.EPDG_TUNNEL_ESTABLISH_FAILURE;
    }

    public boolean is5GHz() {
        return is5GHz(this.frequency);
    }

    public static boolean is5GHz(int freq) {
        return freq > 4900 && freq < 5900;
    }

    public ScanResult(WifiSsid wifiSsid, String BSSID, long hessid, int anqpDomainId, byte[] osuProviders, String caps, int level, int frequency, long tsf) {
        WifiSsid wifiSsid2 = wifiSsid;
        byte[] bArr = osuProviders;
        this.wifiSsid = wifiSsid2;
        this.SSID = wifiSsid2 != null ? wifiSsid.toString() : WifiSsid.NONE;
        this.BSSID = BSSID;
        this.hessid = hessid;
        this.anqpDomainId = anqpDomainId;
        if (bArr != null) {
            this.anqpElements = new AnqpInformationElement[1];
            this.anqpElements[0] = new AnqpInformationElement(AnqpInformationElement.HOTSPOT20_VENDOR_ID, 8, bArr);
        }
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = -1;
        this.distanceSdCm = -1;
        this.channelWidth = -1;
        this.centerFreq0 = -1;
        this.centerFreq1 = -1;
        this.flags = 0;
        this.isCarrierAp = false;
        this.carrierApEapType = -1;
        this.carrierName = null;
        this.radioChainInfos = null;
    }

    public ScanResult(WifiSsid wifiSsid, String BSSID, String caps, int level, int frequency, long tsf, int distCm, int distSdCm) {
        this.wifiSsid = wifiSsid;
        this.SSID = wifiSsid != null ? wifiSsid.toString() : WifiSsid.NONE;
        this.BSSID = BSSID;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = distCm;
        this.distanceSdCm = distSdCm;
        this.channelWidth = -1;
        this.centerFreq0 = -1;
        this.centerFreq1 = -1;
        this.flags = 0;
        this.isCarrierAp = false;
        this.carrierApEapType = -1;
        this.carrierName = null;
        this.radioChainInfos = null;
    }

    public ScanResult(String Ssid, String BSSID, long hessid, int anqpDomainId, String caps, int level, int frequency, long tsf, int distCm, int distSdCm, int channelWidth, int centerFreq0, int centerFreq1, boolean is80211McRTTResponder) {
        this.SSID = Ssid;
        this.BSSID = BSSID;
        this.hessid = hessid;
        this.anqpDomainId = anqpDomainId;
        this.capabilities = caps;
        this.level = level;
        this.frequency = frequency;
        this.timestamp = tsf;
        this.distanceCm = distCm;
        this.distanceSdCm = distSdCm;
        this.channelWidth = channelWidth;
        this.centerFreq0 = centerFreq0;
        this.centerFreq1 = centerFreq1;
        if (is80211McRTTResponder) {
            this.flags = 2;
        } else {
            this.flags = 0;
        }
        this.isCarrierAp = false;
        this.carrierApEapType = -1;
        this.carrierName = null;
        this.radioChainInfos = null;
    }

    public ScanResult(WifiSsid wifiSsid, String Ssid, String BSSID, long hessid, int anqpDomainId, String caps, int level, int frequency, long tsf, int distCm, int distSdCm, int channelWidth, int centerFreq0, int centerFreq1, boolean is80211McRTTResponder) {
        this(Ssid, BSSID, hessid, anqpDomainId, caps, level, frequency, tsf, distCm, distSdCm, channelWidth, centerFreq0, centerFreq1, is80211McRTTResponder);
        this.wifiSsid = wifiSsid;
    }

    public ScanResult(ScanResult source) {
        if (source != null) {
            this.wifiSsid = source.wifiSsid;
            this.SSID = source.SSID;
            this.BSSID = source.BSSID;
            this.hessid = source.hessid;
            this.anqpDomainId = source.anqpDomainId;
            this.informationElements = source.informationElements;
            this.anqpElements = source.anqpElements;
            this.capabilities = source.capabilities;
            this.level = source.level;
            this.frequency = source.frequency;
            this.channelWidth = source.channelWidth;
            this.centerFreq0 = source.centerFreq0;
            this.centerFreq1 = source.centerFreq1;
            this.timestamp = source.timestamp;
            this.distanceCm = source.distanceCm;
            this.distanceSdCm = source.distanceSdCm;
            this.seen = source.seen;
            this.untrusted = source.untrusted;
            this.numUsage = source.numUsage;
            this.venueName = source.venueName;
            this.operatorFriendlyName = source.operatorFriendlyName;
            this.flags = source.flags;
            this.isCarrierAp = source.isCarrierAp;
            this.carrierApEapType = source.carrierApEapType;
            this.carrierName = source.carrierName;
            this.radioChainInfos = source.radioChainInfos;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        String none = "<none>";
        sb.append("SSID: ");
        Object obj = this.wifiSsid;
        if (obj == null) {
            obj = WifiSsid.NONE;
        }
        sb.append(obj);
        sb.append(", BSSID: ");
        String str = this.BSSID;
        if (str == null) {
            str = none;
        }
        sb.append(str);
        sb.append(", capabilities: ");
        str = this.capabilities;
        if (str == null) {
            str = none;
        }
        sb.append(str);
        sb.append(", level: ");
        sb.append(this.level);
        sb.append(", frequency: ");
        sb.append(this.frequency);
        sb.append(", timestamp: ");
        sb.append(this.timestamp);
        sb.append(", distance: ");
        int i = this.distanceCm;
        Object obj2 = "?";
        sb.append(i != -1 ? Integer.valueOf(i) : obj2);
        str = "(cm)";
        sb.append(str);
        sb.append(", distanceSd: ");
        int i2 = this.distanceSdCm;
        if (i2 != -1) {
            obj2 = Integer.valueOf(i2);
        }
        sb.append(obj2);
        sb.append(str);
        sb.append(", passpoint: ");
        String str2 = "yes";
        String str3 = "no";
        sb.append((this.flags & 1) != 0 ? str2 : str3);
        sb.append(", ChannelBandwidth: ");
        sb.append(this.channelWidth);
        sb.append(", centerFreq0: ");
        sb.append(this.centerFreq0);
        sb.append(", centerFreq1: ");
        sb.append(this.centerFreq1);
        sb.append(", 80211mcResponder: ");
        sb.append((this.flags & 2) != 0 ? "is supported" : "is not supported");
        sb.append(", Carrier AP: ");
        if (!this.isCarrierAp) {
            str2 = str3;
        }
        sb.append(str2);
        sb.append(", Carrier AP EAP Type: ");
        sb.append(this.carrierApEapType);
        sb.append(", Carrier name: ");
        sb.append(this.carrierName);
        sb.append(", Radio Chain Infos: ");
        sb.append(Arrays.toString(this.radioChainInfos));
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        int i;
        if (this.wifiSsid != null) {
            dest.writeInt(1);
            this.wifiSsid.writeToParcel(dest, flags);
        } else {
            dest.writeInt(0);
        }
        dest.writeString(this.SSID);
        dest.writeString(this.BSSID);
        dest.writeLong(this.hessid);
        dest.writeInt(this.anqpDomainId);
        dest.writeString(this.capabilities);
        dest.writeInt(this.level);
        dest.writeInt(this.frequency);
        dest.writeLong(this.timestamp);
        dest.writeInt(this.distanceCm);
        dest.writeInt(this.distanceSdCm);
        dest.writeInt(this.channelWidth);
        dest.writeInt(this.centerFreq0);
        dest.writeInt(this.centerFreq1);
        dest.writeLong(this.seen);
        dest.writeInt(this.untrusted);
        dest.writeInt(this.numUsage);
        CharSequence charSequence = this.venueName;
        String str = "";
        dest.writeString(charSequence != null ? charSequence.toString() : str);
        charSequence = this.operatorFriendlyName;
        if (charSequence != null) {
            str = charSequence.toString();
        }
        dest.writeString(str);
        dest.writeLong(this.flags);
        InformationElement[] informationElementArr = this.informationElements;
        if (informationElementArr != null) {
            dest.writeInt(informationElementArr.length);
            i = 0;
            while (true) {
                InformationElement[] informationElementArr2 = this.informationElements;
                if (i >= informationElementArr2.length) {
                    break;
                }
                dest.writeInt(informationElementArr2[i].id);
                dest.writeInt(this.informationElements[i].bytes.length);
                dest.writeByteArray(this.informationElements[i].bytes);
                i++;
            }
        } else {
            dest.writeInt(0);
        }
        List list = this.anqpLines;
        if (list != null) {
            dest.writeInt(list.size());
            for (i = 0; i < this.anqpLines.size(); i++) {
                dest.writeString((String) this.anqpLines.get(i));
            }
        } else {
            dest.writeInt(0);
        }
        AnqpInformationElement[] anqpInformationElementArr = this.anqpElements;
        if (anqpInformationElementArr != null) {
            dest.writeInt(anqpInformationElementArr.length);
            for (AnqpInformationElement element : this.anqpElements) {
                dest.writeInt(element.getVendorId());
                dest.writeInt(element.getElementId());
                dest.writeInt(element.getPayload().length);
                dest.writeByteArray(element.getPayload());
            }
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(this.isCarrierAp);
        dest.writeInt(this.carrierApEapType);
        dest.writeString(this.carrierName);
        RadioChainInfo[] radioChainInfoArr = this.radioChainInfos;
        if (radioChainInfoArr != null) {
            dest.writeInt(radioChainInfoArr.length);
            i = 0;
            while (true) {
                RadioChainInfo[] radioChainInfoArr2 = this.radioChainInfos;
                if (i < radioChainInfoArr2.length) {
                    dest.writeInt(radioChainInfoArr2[i].id);
                    dest.writeInt(this.radioChainInfos[i].level);
                    i++;
                } else {
                    return;
                }
            }
        }
        dest.writeInt(0);
    }
}
