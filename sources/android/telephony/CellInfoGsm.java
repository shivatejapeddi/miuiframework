package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.hardware.radio.V1_0.CellInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class CellInfoGsm extends CellInfo implements Parcelable {
    public static final Creator<CellInfoGsm> CREATOR = new Creator<CellInfoGsm>() {
        public CellInfoGsm createFromParcel(Parcel in) {
            in.readInt();
            return CellInfoGsm.createFromParcelBody(in);
        }

        public CellInfoGsm[] newArray(int size) {
            return new CellInfoGsm[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellInfoGsm";
    private CellIdentityGsm mCellIdentityGsm;
    private CellSignalStrengthGsm mCellSignalStrengthGsm;

    @UnsupportedAppUsage
    public CellInfoGsm() {
        this.mCellIdentityGsm = new CellIdentityGsm();
        this.mCellSignalStrengthGsm = new CellSignalStrengthGsm();
    }

    public CellInfoGsm(CellInfoGsm ci) {
        super((CellInfo) ci);
        this.mCellIdentityGsm = ci.mCellIdentityGsm.copy();
        this.mCellSignalStrengthGsm = ci.mCellSignalStrengthGsm.copy();
    }

    public CellInfoGsm(CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_0.CellInfoGsm cig = (android.hardware.radio.V1_0.CellInfoGsm) ci.gsm.get(0);
        this.mCellIdentityGsm = new CellIdentityGsm(cig.cellIdentityGsm);
        this.mCellSignalStrengthGsm = new CellSignalStrengthGsm(cig.signalStrengthGsm);
    }

    public CellInfoGsm(android.hardware.radio.V1_2.CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_2.CellInfoGsm cig = (android.hardware.radio.V1_2.CellInfoGsm) ci.gsm.get(0);
        this.mCellIdentityGsm = new CellIdentityGsm(cig.cellIdentityGsm);
        this.mCellSignalStrengthGsm = new CellSignalStrengthGsm(cig.signalStrengthGsm);
    }

    public CellInfoGsm(android.hardware.radio.V1_4.CellInfo ci, long timeStamp) {
        super(ci, timeStamp);
        android.hardware.radio.V1_2.CellInfoGsm cig = ci.info.gsm();
        this.mCellIdentityGsm = new CellIdentityGsm(cig.cellIdentityGsm);
        this.mCellSignalStrengthGsm = new CellSignalStrengthGsm(cig.signalStrengthGsm);
    }

    public CellIdentityGsm getCellIdentity() {
        return this.mCellIdentityGsm;
    }

    public void setCellIdentity(CellIdentityGsm cid) {
        this.mCellIdentityGsm = cid;
    }

    public CellSignalStrengthGsm getCellSignalStrength() {
        return this.mCellSignalStrengthGsm;
    }

    public CellInfo sanitizeLocationInfo() {
        CellInfoGsm result = new CellInfoGsm(this);
        result.mCellIdentityGsm = this.mCellIdentityGsm.sanitizeLocationInfo();
        return result;
    }

    public void setCellSignalStrength(CellSignalStrengthGsm css) {
        this.mCellSignalStrengthGsm = css;
    }

    public int hashCode() {
        return (super.hashCode() + this.mCellIdentityGsm.hashCode()) + this.mCellSignalStrengthGsm.hashCode();
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!super.equals(other)) {
            return false;
        }
        try {
            CellInfoGsm o = (CellInfoGsm) other;
            if (this.mCellIdentityGsm.equals(o.mCellIdentityGsm) && this.mCellSignalStrengthGsm.equals(o.mCellSignalStrengthGsm)) {
                z = true;
            }
            return z;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CellInfoGsm:{");
        sb.append(super.toString());
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        sb.append(str);
        sb.append(this.mCellIdentityGsm);
        sb.append(str);
        sb.append(this.mCellSignalStrengthGsm);
        sb.append("}");
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 1);
        this.mCellIdentityGsm.writeToParcel(dest, flags);
        this.mCellSignalStrengthGsm.writeToParcel(dest, flags);
    }

    private CellInfoGsm(Parcel in) {
        super(in);
        this.mCellIdentityGsm = (CellIdentityGsm) CellIdentityGsm.CREATOR.createFromParcel(in);
        this.mCellSignalStrengthGsm = (CellSignalStrengthGsm) CellSignalStrengthGsm.CREATOR.createFromParcel(in);
    }

    protected static CellInfoGsm createFromParcelBody(Parcel in) {
        return new CellInfoGsm(in);
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
