package android.content.pm;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.proto.ProtoOutputStream;

public class FeatureInfo implements Parcelable {
    public static final Creator<FeatureInfo> CREATOR = new Creator<FeatureInfo>() {
        public FeatureInfo createFromParcel(Parcel source) {
            return new FeatureInfo(source, null);
        }

        public FeatureInfo[] newArray(int size) {
            return new FeatureInfo[size];
        }
    };
    public static final int FLAG_REQUIRED = 1;
    public static final int GL_ES_VERSION_UNDEFINED = 0;
    public int flags;
    public String name;
    public int reqGlEsVersion;
    public int version;

    /* synthetic */ FeatureInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public FeatureInfo(FeatureInfo orig) {
        this.name = orig.name;
        this.version = orig.version;
        this.reqGlEsVersion = orig.reqGlEsVersion;
        this.flags = orig.flags;
    }

    public String toString() {
        String str = "}";
        String str2 = " fl=0x";
        String str3 = "FeatureInfo{";
        StringBuilder stringBuilder;
        if (this.name != null) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(str3);
            stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(this.name);
            stringBuilder.append(" v=");
            stringBuilder.append(this.version);
            stringBuilder.append(str2);
            stringBuilder.append(Integer.toHexString(this.flags));
            stringBuilder.append(str);
            return stringBuilder.toString();
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(str3);
        stringBuilder.append(Integer.toHexString(System.identityHashCode(this)));
        stringBuilder.append(" glEsVers=");
        stringBuilder.append(getGlEsVersion());
        stringBuilder.append(str2);
        stringBuilder.append(Integer.toHexString(this.flags));
        stringBuilder.append(str);
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int parcelableFlags) {
        dest.writeString(this.name);
        dest.writeInt(this.version);
        dest.writeInt(this.reqGlEsVersion);
        dest.writeInt(this.flags);
    }

    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        String str = this.name;
        if (str != null) {
            proto.write(1138166333441L, str);
        }
        proto.write(1120986464258L, this.version);
        proto.write(1138166333443L, getGlEsVersion());
        proto.write(1120986464260L, this.flags);
        proto.end(token);
    }

    private FeatureInfo(Parcel source) {
        this.name = source.readString();
        this.version = source.readInt();
        this.reqGlEsVersion = source.readInt();
        this.flags = source.readInt();
    }

    public String getGlEsVersion() {
        int minor = this.reqGlEsVersion;
        int major = (-65536 & minor) >> 16;
        minor &= 65535;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(major));
        stringBuilder.append(".");
        stringBuilder.append(String.valueOf(minor));
        return stringBuilder.toString();
    }
}
