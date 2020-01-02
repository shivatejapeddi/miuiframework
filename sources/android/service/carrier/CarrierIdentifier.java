package android.service.carrier;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.telephony.Rlog;
import com.android.internal.telephony.uicc.IccUtils;
import java.util.Objects;

public class CarrierIdentifier implements Parcelable {
    public static final Creator<CarrierIdentifier> CREATOR = new Creator<CarrierIdentifier>() {
        public CarrierIdentifier createFromParcel(Parcel parcel) {
            return new CarrierIdentifier(parcel);
        }

        public CarrierIdentifier[] newArray(int i) {
            return new CarrierIdentifier[i];
        }
    };
    private int mCarrierId;
    private String mGid1;
    private String mGid2;
    private String mIccid;
    private String mImsi;
    private String mMcc;
    private String mMnc;
    private int mSpecificCarrierId;
    private String mSpn;

    public interface MatchType {
        public static final int ALL = 0;
        public static final int GID1 = 3;
        public static final int GID2 = 4;
        public static final int ICCID = 5;
        public static final int IMSI_PREFIX = 2;
        public static final int SPN = 1;
    }

    public CarrierIdentifier(String mcc, String mnc, String spn, String imsi, String gid1, String gid2) {
        this(mcc, mnc, spn, imsi, gid1, gid2, null, -1, -1);
    }

    public CarrierIdentifier(String mcc, String mnc, String spn, String imsi, String gid1, String gid2, int carrierid, int specificCarrierId) {
        this(mcc, mnc, spn, imsi, gid1, gid2, null, carrierid, specificCarrierId);
    }

    public CarrierIdentifier(String mcc, String mnc, String spn, String imsi, String gid1, String gid2, String iccid, int carrierid, int specificCarrierId) {
        this.mCarrierId = -1;
        this.mSpecificCarrierId = -1;
        this.mMcc = mcc;
        this.mMnc = mnc;
        this.mSpn = spn;
        this.mImsi = imsi;
        this.mGid1 = gid1;
        this.mGid2 = gid2;
        this.mIccid = iccid;
        this.mCarrierId = carrierid;
        this.mSpecificCarrierId = specificCarrierId;
    }

    public CarrierIdentifier(String mcc, String mnc, String spn, String imsi, String gid1, String gid2I, String iccid) {
        this(mcc, mnc, spn, imsi, gid1, gid2I);
        this.mIccid = iccid;
    }

    public CarrierIdentifier(byte[] mccMnc, String gid1, String gid2) {
        this.mCarrierId = -1;
        this.mSpecificCarrierId = -1;
        if (mccMnc.length == 3) {
            this.mMcc = new String(new char[]{hex.charAt(1), hex.charAt(0), IccUtils.bytesToHexString(mccMnc).charAt(3)});
            if (IccUtils.bytesToHexString(mccMnc).charAt(2) == 'F') {
                this.mMnc = new String(new char[]{hex.charAt(5), hex.charAt(4)});
            } else {
                this.mMnc = new String(new char[]{hex.charAt(5), hex.charAt(4), hex.charAt(2)});
            }
            this.mGid1 = gid1;
            this.mGid2 = gid2;
            this.mSpn = null;
            this.mImsi = null;
            this.mIccid = null;
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("MCC & MNC must be set by a 3-byte array: byte[");
        stringBuilder.append(mccMnc.length);
        stringBuilder.append("]");
        throw new IllegalArgumentException(stringBuilder.toString());
    }

    public CarrierIdentifier(Parcel parcel) {
        this.mCarrierId = -1;
        this.mSpecificCarrierId = -1;
        readFromParcel(parcel);
    }

    public String getMcc() {
        return this.mMcc;
    }

    public String getMnc() {
        return this.mMnc;
    }

    public String getSpn() {
        return this.mSpn;
    }

    public String getImsi() {
        return this.mImsi;
    }

    public String getGid1() {
        return this.mGid1;
    }

    public String getGid2() {
        return this.mGid2;
    }

    public String getIccid() {
        return this.mIccid;
    }

    public int getCarrierId() {
        return this.mCarrierId;
    }

    public int getSpecificCarrierId() {
        return this.mSpecificCarrierId;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CarrierIdentifier that = (CarrierIdentifier) obj;
        if (!(Objects.equals(this.mMcc, that.mMcc) && Objects.equals(this.mMnc, that.mMnc) && Objects.equals(this.mSpn, that.mSpn) && Objects.equals(this.mImsi, that.mImsi) && Objects.equals(this.mGid1, that.mGid1) && Objects.equals(this.mGid2, that.mGid2) && Objects.equals(this.mIccid, that.mIccid) && Objects.equals(Integer.valueOf(this.mCarrierId), Integer.valueOf(that.mCarrierId)) && Objects.equals(Integer.valueOf(this.mSpecificCarrierId), Integer.valueOf(that.mSpecificCarrierId)))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mMcc, this.mMnc, this.mSpn, this.mImsi, this.mGid1, this.mGid2, this.mIccid, Integer.valueOf(this.mCarrierId), Integer.valueOf(this.mSpecificCarrierId)});
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.mMcc);
        out.writeString(this.mMnc);
        out.writeString(this.mSpn);
        out.writeString(this.mImsi);
        out.writeString(this.mGid1);
        out.writeString(this.mGid2);
        out.writeString(this.mIccid);
        out.writeInt(this.mCarrierId);
        out.writeInt(this.mSpecificCarrierId);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CarrierIdentifier{mcc=");
        stringBuilder.append(this.mMcc);
        stringBuilder.append(",mnc=");
        stringBuilder.append(this.mMnc);
        stringBuilder.append(",spn=");
        stringBuilder.append(this.mSpn);
        stringBuilder.append(",imsi=");
        stringBuilder.append(Rlog.pii(false, this.mImsi));
        stringBuilder.append(",gid1=");
        stringBuilder.append(this.mGid1);
        stringBuilder.append(",gid2=");
        stringBuilder.append(this.mGid2);
        stringBuilder.append(",iccid=");
        stringBuilder.append(Rlog.pii(false, this.mIccid));
        stringBuilder.append(",carrierid=");
        stringBuilder.append(this.mCarrierId);
        stringBuilder.append(",specificCarrierId=");
        stringBuilder.append(this.mSpecificCarrierId);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public void readFromParcel(Parcel in) {
        this.mMcc = in.readString();
        this.mMnc = in.readString();
        this.mSpn = in.readString();
        this.mImsi = in.readString();
        this.mGid1 = in.readString();
        this.mGid2 = in.readString();
        this.mIccid = in.readString();
        this.mCarrierId = in.readInt();
        this.mSpecificCarrierId = in.readInt();
    }
}
