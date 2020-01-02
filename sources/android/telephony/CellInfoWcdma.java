package android.telephony;

import android.hardware.radio.V1_0.CellInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class CellInfoWcdma extends CellInfo implements Parcelable {
    public static final Creator<CellInfoWcdma> CREATOR = new Creator<CellInfoWcdma>() {
        public CellInfoWcdma createFromParcel(Parcel in) {
            in.readInt();
            return CellInfoWcdma.createFromParcelBody(in);
        }

        public CellInfoWcdma[] newArray(int size) {
            return new CellInfoWcdma[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellInfoWcdma";
    private CellIdentityWcdma mCellIdentityWcdma;
    private CellSignalStrengthWcdma mCellSignalStrengthWcdma;

    public CellInfoWcdma() {
        this.mCellIdentityWcdma = new CellIdentityWcdma();
        this.mCellSignalStrengthWcdma = new CellSignalStrengthWcdma();
    }

    public CellInfoWcdma(CellInfoWcdma ci) {
        super((CellInfo) ci);
        this.mCellIdentityWcdma = ci.mCellIdentityWcdma.copy();
        this.mCellSignalStrengthWcdma = ci.mCellSignalStrengthWcdma.copy();
    }

    public CellInfoWcdma(CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_0.CellInfoWcdma ciw = (android.hardware.radio.V1_0.CellInfoWcdma) ci.wcdma.get(0);
        this.mCellIdentityWcdma = new CellIdentityWcdma(ciw.cellIdentityWcdma);
        this.mCellSignalStrengthWcdma = new CellSignalStrengthWcdma(ciw.signalStrengthWcdma);
    }

    public CellInfoWcdma(android.hardware.radio.V1_2.CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_2.CellInfoWcdma ciw = (android.hardware.radio.V1_2.CellInfoWcdma) ci.wcdma.get(0);
        this.mCellIdentityWcdma = new CellIdentityWcdma(ciw.cellIdentityWcdma);
        this.mCellSignalStrengthWcdma = new CellSignalStrengthWcdma(ciw.signalStrengthWcdma);
    }

    public CellInfoWcdma(android.hardware.radio.V1_4.CellInfo ci, long timeStamp) {
        super(ci, timeStamp);
        android.hardware.radio.V1_2.CellInfoWcdma ciw = ci.info.wcdma();
        this.mCellIdentityWcdma = new CellIdentityWcdma(ciw.cellIdentityWcdma);
        this.mCellSignalStrengthWcdma = new CellSignalStrengthWcdma(ciw.signalStrengthWcdma);
    }

    public CellIdentityWcdma getCellIdentity() {
        return this.mCellIdentityWcdma;
    }

    public void setCellIdentity(CellIdentityWcdma cid) {
        this.mCellIdentityWcdma = cid;
    }

    public CellSignalStrengthWcdma getCellSignalStrength() {
        return this.mCellSignalStrengthWcdma;
    }

    public CellInfo sanitizeLocationInfo() {
        CellInfoWcdma result = new CellInfoWcdma(this);
        result.mCellIdentityWcdma = this.mCellIdentityWcdma.sanitizeLocationInfo();
        return result;
    }

    public void setCellSignalStrength(CellSignalStrengthWcdma css) {
        this.mCellSignalStrengthWcdma = css;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), this.mCellIdentityWcdma, this.mCellSignalStrengthWcdma});
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!super.equals(other)) {
            return false;
        }
        try {
            CellInfoWcdma o = (CellInfoWcdma) other;
            if (this.mCellIdentityWcdma.equals(o.mCellIdentityWcdma) && this.mCellSignalStrengthWcdma.equals(o.mCellSignalStrengthWcdma)) {
                z = true;
            }
            return z;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CellInfoWcdma:{");
        sb.append(super.toString());
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        sb.append(str);
        sb.append(this.mCellIdentityWcdma);
        sb.append(str);
        sb.append(this.mCellSignalStrengthWcdma);
        sb.append("}");
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 4);
        this.mCellIdentityWcdma.writeToParcel(dest, flags);
        this.mCellSignalStrengthWcdma.writeToParcel(dest, flags);
    }

    private CellInfoWcdma(Parcel in) {
        super(in);
        this.mCellIdentityWcdma = (CellIdentityWcdma) CellIdentityWcdma.CREATOR.createFromParcel(in);
        this.mCellSignalStrengthWcdma = (CellSignalStrengthWcdma) CellSignalStrengthWcdma.CREATOR.createFromParcel(in);
    }

    protected static CellInfoWcdma createFromParcelBody(Parcel in) {
        return new CellInfoWcdma(in);
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
