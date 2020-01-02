package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.hardware.radio.V1_0.CellInfo;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class CellInfoLte extends CellInfo implements Parcelable {
    public static final Creator<CellInfoLte> CREATOR = new Creator<CellInfoLte>() {
        public CellInfoLte createFromParcel(Parcel in) {
            in.readInt();
            return CellInfoLte.createFromParcelBody(in);
        }

        public CellInfoLte[] newArray(int size) {
            return new CellInfoLte[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellInfoLte";
    private CellConfigLte mCellConfig;
    private CellIdentityLte mCellIdentityLte;
    private CellSignalStrengthLte mCellSignalStrengthLte;

    @UnsupportedAppUsage
    public CellInfoLte() {
        this.mCellIdentityLte = new CellIdentityLte();
        this.mCellSignalStrengthLte = new CellSignalStrengthLte();
        this.mCellConfig = new CellConfigLte();
    }

    public CellInfoLte(CellInfoLte ci) {
        super((CellInfo) ci);
        this.mCellIdentityLte = ci.mCellIdentityLte.copy();
        this.mCellSignalStrengthLte = ci.mCellSignalStrengthLte.copy();
        this.mCellConfig = new CellConfigLte(ci.mCellConfig);
    }

    public CellInfoLte(CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_0.CellInfoLte cil = (android.hardware.radio.V1_0.CellInfoLte) ci.lte.get(0);
        this.mCellIdentityLte = new CellIdentityLte(cil.cellIdentityLte);
        this.mCellSignalStrengthLte = new CellSignalStrengthLte(cil.signalStrengthLte);
        this.mCellConfig = new CellConfigLte();
    }

    public CellInfoLte(android.hardware.radio.V1_2.CellInfo ci) {
        super(ci);
        android.hardware.radio.V1_2.CellInfoLte cil = (android.hardware.radio.V1_2.CellInfoLte) ci.lte.get(0);
        this.mCellIdentityLte = new CellIdentityLte(cil.cellIdentityLte);
        this.mCellSignalStrengthLte = new CellSignalStrengthLte(cil.signalStrengthLte);
        this.mCellConfig = new CellConfigLte();
    }

    public CellInfoLte(android.hardware.radio.V1_4.CellInfo ci, long timeStamp) {
        super(ci, timeStamp);
        android.hardware.radio.V1_4.CellInfoLte cil = ci.info.lte();
        this.mCellIdentityLte = new CellIdentityLte(cil.base.cellIdentityLte);
        this.mCellSignalStrengthLte = new CellSignalStrengthLte(cil.base.signalStrengthLte);
        this.mCellConfig = new CellConfigLte(cil.cellConfig);
    }

    public CellIdentityLte getCellIdentity() {
        return this.mCellIdentityLte;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setCellIdentity(CellIdentityLte cid) {
        this.mCellIdentityLte = cid;
    }

    public CellSignalStrengthLte getCellSignalStrength() {
        return this.mCellSignalStrengthLte;
    }

    public CellInfo sanitizeLocationInfo() {
        CellInfoLte result = new CellInfoLte(this);
        result.mCellIdentityLte = this.mCellIdentityLte.sanitizeLocationInfo();
        return result;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setCellSignalStrength(CellSignalStrengthLte css) {
        this.mCellSignalStrengthLte = css;
    }

    public void setCellConfig(CellConfigLte cellConfig) {
        this.mCellConfig = cellConfig;
    }

    public CellConfigLte getCellConfig() {
        return this.mCellConfig;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), Integer.valueOf(this.mCellIdentityLte.hashCode()), Integer.valueOf(this.mCellSignalStrengthLte.hashCode()), Integer.valueOf(this.mCellConfig.hashCode())});
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!(other instanceof CellInfoLte)) {
            return false;
        }
        CellInfoLte o = (CellInfoLte) other;
        if (super.equals(o) && this.mCellIdentityLte.equals(o.mCellIdentityLte) && this.mCellSignalStrengthLte.equals(o.mCellSignalStrengthLte) && this.mCellConfig.equals(o.mCellConfig)) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("CellInfoLte:{");
        sb.append(super.toString());
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        sb.append(str);
        sb.append(this.mCellIdentityLte);
        sb.append(str);
        sb.append(this.mCellSignalStrengthLte);
        sb.append(str);
        sb.append(this.mCellConfig);
        sb.append("}");
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 3);
        this.mCellIdentityLte.writeToParcel(dest, flags);
        this.mCellSignalStrengthLte.writeToParcel(dest, flags);
        this.mCellConfig.writeToParcel(dest, flags);
    }

    private CellInfoLte(Parcel in) {
        super(in);
        this.mCellIdentityLte = (CellIdentityLte) CellIdentityLte.CREATOR.createFromParcel(in);
        this.mCellSignalStrengthLte = (CellSignalStrengthLte) CellSignalStrengthLte.CREATOR.createFromParcel(in);
        this.mCellConfig = (CellConfigLte) CellConfigLte.CREATOR.createFromParcel(in);
    }

    protected static CellInfoLte createFromParcelBody(Parcel in) {
        return new CellInfoLte(in);
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
