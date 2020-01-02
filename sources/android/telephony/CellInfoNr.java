package android.telephony;

import android.net.wifi.WifiEnterpriseConfig;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.Objects;

public final class CellInfoNr extends CellInfo {
    public static final Creator<CellInfoNr> CREATOR = new Creator<CellInfoNr>() {
        public CellInfoNr createFromParcel(Parcel in) {
            in.readInt();
            return new CellInfoNr(in, null);
        }

        public CellInfoNr[] newArray(int size) {
            return new CellInfoNr[size];
        }
    };
    private static final String TAG = "CellInfoNr";
    private final CellIdentityNr mCellIdentity;
    private final CellSignalStrengthNr mCellSignalStrength;

    private CellInfoNr(Parcel in) {
        super(in);
        this.mCellIdentity = (CellIdentityNr) CellIdentityNr.CREATOR.createFromParcel(in);
        this.mCellSignalStrength = (CellSignalStrengthNr) CellSignalStrengthNr.CREATOR.createFromParcel(in);
    }

    private CellInfoNr(CellInfoNr other, boolean sanitizeLocationInfo) {
        CellIdentityNr sanitizeLocationInfo2;
        super((CellInfo) other);
        if (sanitizeLocationInfo) {
            sanitizeLocationInfo2 = other.mCellIdentity.sanitizeLocationInfo();
        } else {
            sanitizeLocationInfo2 = other.mCellIdentity;
        }
        this.mCellIdentity = sanitizeLocationInfo2;
        this.mCellSignalStrength = other.mCellSignalStrength;
    }

    public CellIdentity getCellIdentity() {
        return this.mCellIdentity;
    }

    public CellSignalStrength getCellSignalStrength() {
        return this.mCellSignalStrength;
    }

    public CellInfo sanitizeLocationInfo() {
        return new CellInfoNr(this, true);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), this.mCellIdentity, this.mCellSignalStrength});
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!(other instanceof CellInfoNr)) {
            return false;
        }
        CellInfoNr o = (CellInfoNr) other;
        if (super.equals(o) && this.mCellIdentity.equals(o.mCellIdentity) && this.mCellSignalStrength.equals(o.mCellSignalStrength)) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CellInfoNr:{");
        StringBuilder stringBuilder2 = new StringBuilder();
        String str = WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER;
        stringBuilder2.append(str);
        stringBuilder2.append(super.toString());
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(this.mCellIdentity);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(this.mCellSignalStrength);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags, 6);
        this.mCellIdentity.writeToParcel(dest, flags);
        this.mCellSignalStrength.writeToParcel(dest, flags);
    }

    protected static CellInfoNr createFromParcelBody(Parcel in) {
        return new CellInfoNr(in);
    }
}
