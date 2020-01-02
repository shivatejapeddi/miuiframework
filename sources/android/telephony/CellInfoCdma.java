package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.hardware.radio.V1_0.CellInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class CellInfoCdma extends CellInfo implements Parcelable {
    public static final Creator<CellInfoCdma> CREATOR = new Creator<CellInfoCdma>() {
        public CellInfoCdma createFromParcel(Parcel in) {
            in.readInt();
            return CellInfoCdma.createFromParcelBody(in);
        }

        public CellInfoCdma[] newArray(int size) {
            return new CellInfoCdma[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellInfoCdma";
    private CellIdentityCdma mCellIdentityCdma;
    private CellSignalStrengthCdma mCellSignalStrengthCdma;

    @UnsupportedAppUsage
    public CellInfoCdma() {
        this.mCellIdentityCdma = new CellIdentityCdma();
        this.mCellSignalStrengthCdma = new CellSignalStrengthCdma();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public CellInfoCdma(CellInfoCdma ci) {
        super((CellInfo) ci);
        this.mCellIdentityCdma = ci.mCellIdentityCdma.copy();
        this.mCellSignalStrengthCdma = ci.mCellSignalStrengthCdma.copy();
    }

    public CellInfoCdma(CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_0.CellInfoCdma cic = (android.hardware.radio.V1_0.CellInfoCdma) ci.cdma.get(0);
        this.mCellIdentityCdma = new CellIdentityCdma(cic.cellIdentityCdma);
        this.mCellSignalStrengthCdma = new CellSignalStrengthCdma(cic.signalStrengthCdma, cic.signalStrengthEvdo);
    }

    public CellInfoCdma(android.hardware.radio.V1_2.CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_2.CellInfoCdma cic = (android.hardware.radio.V1_2.CellInfoCdma) ci.cdma.get(0);
        this.mCellIdentityCdma = new CellIdentityCdma(cic.cellIdentityCdma);
        this.mCellSignalStrengthCdma = new CellSignalStrengthCdma(cic.signalStrengthCdma, cic.signalStrengthEvdo);
    }

    public CellInfoCdma(android.hardware.radio.V1_4.CellInfo ci, long timeStamp) {
        super(ci, timeStamp);
        android.hardware.radio.V1_2.CellInfoCdma cic = ci.info.cdma();
        this.mCellIdentityCdma = new CellIdentityCdma(cic.cellIdentityCdma);
        this.mCellSignalStrengthCdma = new CellSignalStrengthCdma(cic.signalStrengthCdma, cic.signalStrengthEvdo);
    }

    public CellIdentityCdma getCellIdentity() {
        return this.mCellIdentityCdma;
    }

    @UnsupportedAppUsage
    public void setCellIdentity(CellIdentityCdma cid) {
        this.mCellIdentityCdma = cid;
    }

    public CellSignalStrengthCdma getCellSignalStrength() {
        return this.mCellSignalStrengthCdma;
    }

    public CellInfo sanitizeLocationInfo() {
        CellInfoCdma result = new CellInfoCdma(this);
        result.mCellIdentityCdma = this.mCellIdentityCdma.sanitizeLocationInfo();
        return result;
    }

    public void setCellSignalStrength(CellSignalStrengthCdma css) {
        this.mCellSignalStrengthCdma = css;
    }

    public int hashCode() {
        return (super.hashCode() + this.mCellIdentityCdma.hashCode()) + this.mCellSignalStrengthCdma.hashCode();
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!super.equals(other)) {
            return false;
        }
        try {
            CellInfoCdma o = (CellInfoCdma) other;
            if (this.mCellIdentityCdma.equals(o.mCellIdentityCdma) && this.mCellSignalStrengthCdma.equals(o.mCellSignalStrengthCdma)) {
                z = true;
            }
            return z;
        } catch (ClassCastException e) {
            return false;
        }
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CellInfoCdma:{");
        sb.append(super.toString());
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        sb.append(str);
        sb.append(this.mCellIdentityCdma);
        sb.append(str);
        sb.append(this.mCellSignalStrengthCdma);
        sb.append("}");
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 2);
        this.mCellIdentityCdma.writeToParcel(dest, flags);
        this.mCellSignalStrengthCdma.writeToParcel(dest, flags);
    }

    private CellInfoCdma(Parcel in) {
        super(in);
        this.mCellIdentityCdma = (CellIdentityCdma) CellIdentityCdma.CREATOR.createFromParcel(in);
        this.mCellSignalStrengthCdma = (CellSignalStrengthCdma) CellSignalStrengthCdma.CREATOR.createFromParcel(in);
    }

    protected static CellInfoCdma createFromParcelBody(Parcel in) {
        return new CellInfoCdma(in);
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
