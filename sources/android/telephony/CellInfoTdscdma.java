package android.telephony;

import android.hardware.radio.V1_0.CellInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class CellInfoTdscdma extends CellInfo implements Parcelable {
    public static final Creator<CellInfoTdscdma> CREATOR = new Creator<CellInfoTdscdma>() {
        public CellInfoTdscdma createFromParcel(Parcel in) {
            in.readInt();
            return CellInfoTdscdma.createFromParcelBody(in);
        }

        public CellInfoTdscdma[] newArray(int size) {
            return new CellInfoTdscdma[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellInfoTdscdma";
    private CellIdentityTdscdma mCellIdentityTdscdma;
    private CellSignalStrengthTdscdma mCellSignalStrengthTdscdma;

    public CellInfoTdscdma() {
        this.mCellIdentityTdscdma = new CellIdentityTdscdma();
        this.mCellSignalStrengthTdscdma = new CellSignalStrengthTdscdma();
    }

    public CellInfoTdscdma(CellInfoTdscdma ci) {
        super((CellInfo) ci);
        this.mCellIdentityTdscdma = ci.mCellIdentityTdscdma.copy();
        this.mCellSignalStrengthTdscdma = ci.mCellSignalStrengthTdscdma.copy();
    }

    public CellInfoTdscdma(CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_0.CellInfoTdscdma cit = (android.hardware.radio.V1_0.CellInfoTdscdma) ci.tdscdma.get(0);
        this.mCellIdentityTdscdma = new CellIdentityTdscdma(cit.cellIdentityTdscdma);
        this.mCellSignalStrengthTdscdma = new CellSignalStrengthTdscdma(cit.signalStrengthTdscdma);
    }

    public CellInfoTdscdma(android.hardware.radio.V1_2.CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_2.CellInfoTdscdma cit = (android.hardware.radio.V1_2.CellInfoTdscdma) ci.tdscdma.get(0);
        this.mCellIdentityTdscdma = new CellIdentityTdscdma(cit.cellIdentityTdscdma);
        this.mCellSignalStrengthTdscdma = new CellSignalStrengthTdscdma(cit.signalStrengthTdscdma);
    }

    public CellInfoTdscdma(android.hardware.radio.V1_4.CellInfo ci, long timeStamp) {
        super(ci, timeStamp);
        android.hardware.radio.V1_2.CellInfoTdscdma cit = ci.info.tdscdma();
        this.mCellIdentityTdscdma = new CellIdentityTdscdma(cit.cellIdentityTdscdma);
        this.mCellSignalStrengthTdscdma = new CellSignalStrengthTdscdma(cit.signalStrengthTdscdma);
    }

    public CellIdentityTdscdma getCellIdentity() {
        return this.mCellIdentityTdscdma;
    }

    public void setCellIdentity(CellIdentityTdscdma cid) {
        this.mCellIdentityTdscdma = cid;
    }

    public CellSignalStrengthTdscdma getCellSignalStrength() {
        return this.mCellSignalStrengthTdscdma;
    }

    public CellInfo sanitizeLocationInfo() {
        CellInfoTdscdma result = new CellInfoTdscdma(this);
        result.mCellIdentityTdscdma = this.mCellIdentityTdscdma.sanitizeLocationInfo();
        return result;
    }

    public void setCellSignalStrength(CellSignalStrengthTdscdma css) {
        this.mCellSignalStrengthTdscdma = css;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), this.mCellIdentityTdscdma, this.mCellSignalStrengthTdscdma});
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!super.equals(other)) {
            return false;
        }
        try {
            CellInfoTdscdma o = (CellInfoTdscdma) other;
            if (this.mCellIdentityTdscdma.equals(o.mCellIdentityTdscdma) && this.mCellSignalStrengthTdscdma.equals(o.mCellSignalStrengthTdscdma)) {
                z = true;
            }
            return z;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CellInfoTdscdma:{");
        sb.append(super.toString());
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        sb.append(str);
        sb.append(this.mCellIdentityTdscdma);
        sb.append(str);
        sb.append(this.mCellSignalStrengthTdscdma);
        sb.append("}");
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 5);
        this.mCellIdentityTdscdma.writeToParcel(dest, flags);
        this.mCellSignalStrengthTdscdma.writeToParcel(dest, flags);
    }

    private CellInfoTdscdma(Parcel in) {
        super(in);
        this.mCellIdentityTdscdma = (CellIdentityTdscdma) CellIdentityTdscdma.CREATOR.createFromParcel(in);
        this.mCellSignalStrengthTdscdma = (CellSignalStrengthTdscdma) CellSignalStrengthTdscdma.CREATOR.createFromParcel(in);
    }

    protected static CellInfoTdscdma createFromParcelBody(Parcel in) {
        return new CellInfoTdscdma(in);
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
