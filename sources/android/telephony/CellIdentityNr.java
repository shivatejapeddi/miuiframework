package android.telephony;

import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.telephony.gsm.GsmCellLocation;
import java.util.Objects;

public final class CellIdentityNr extends CellIdentity {
    public static final Creator<CellIdentityNr> CREATOR = new Creator<CellIdentityNr>() {
        public CellIdentityNr createFromParcel(Parcel in) {
            in.readInt();
            return CellIdentityNr.createFromParcelBody(in);
        }

        public CellIdentityNr[] newArray(int size) {
            return new CellIdentityNr[size];
        }
    };
    private static final long MAX_NCI = 68719476735L;
    private static final int MAX_NRARFCN = 3279165;
    private static final int MAX_PCI = 1007;
    private static final int MAX_TAC = 65535;
    private static final String TAG = "CellIdentityNr";
    private final long mNci;
    private final int mNrArfcn;
    private final int mPci;
    private final int mTac;

    public CellIdentityNr(int pci, int tac, int nrArfcn, String mccStr, String mncStr, long nci, String alphal, String alphas) {
        super(TAG, 6, mccStr, mncStr, alphal, alphas);
        int i = pci;
        this.mPci = CellIdentity.inRangeOrUnavailable(pci, 0, 1007);
        this.mTac = CellIdentity.inRangeOrUnavailable(tac, 0, 65535);
        this.mNrArfcn = CellIdentity.inRangeOrUnavailable(nrArfcn, 0, (int) MAX_NRARFCN);
        this.mNci = CellIdentity.inRangeOrUnavailable(nci, 0, (long) MAX_NCI);
    }

    public CellIdentityNr sanitizeLocationInfo() {
        return new CellIdentityNr(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, this.mMccStr, this.mMncStr, 2147483647L, this.mAlphaLong, this.mAlphaShort);
    }

    public CellLocation asCellLocation() {
        return new GsmCellLocation();
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), Integer.valueOf(this.mPci), Integer.valueOf(this.mTac), Integer.valueOf(this.mNrArfcn), Long.valueOf(this.mNci)});
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!(other instanceof CellIdentityNr)) {
            return false;
        }
        CellIdentityNr o = (CellIdentityNr) other;
        if (super.equals(o) && this.mPci == o.mPci && this.mTac == o.mTac && this.mNrArfcn == o.mNrArfcn && this.mNci == o.mNci) {
            z = true;
        }
        return z;
    }

    public long getNci() {
        return this.mNci;
    }

    public int getNrarfcn() {
        return this.mNrArfcn;
    }

    public int getPci() {
        return this.mPci;
    }

    public int getTac() {
        return this.mTac;
    }

    public String getMccString() {
        return this.mMccStr;
    }

    public String getMncString() {
        return this.mMncStr;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("CellIdentityNr:{");
        stringBuilder.append(" mPci = ");
        stringBuilder.append(this.mPci);
        stringBuilder.append(" mTac = ");
        stringBuilder.append(this.mTac);
        stringBuilder.append(" mNrArfcn = ");
        stringBuilder.append(this.mNrArfcn);
        stringBuilder.append(" mMcc = ");
        stringBuilder.append(this.mMccStr);
        stringBuilder.append(" mMnc = ");
        stringBuilder.append(this.mMncStr);
        stringBuilder.append(" mNci = ");
        stringBuilder.append(this.mNci);
        stringBuilder.append(" mAlphaLong = ");
        stringBuilder.append(this.mAlphaLong);
        stringBuilder.append(" mAlphaShort = ");
        stringBuilder.append(this.mAlphaShort);
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel dest, int type) {
        super.writeToParcel(dest, 6);
        dest.writeInt(this.mPci);
        dest.writeInt(this.mTac);
        dest.writeInt(this.mNrArfcn);
        dest.writeLong(this.mNci);
    }

    private CellIdentityNr(Parcel in) {
        super(TAG, 6, in);
        this.mPci = in.readInt();
        this.mTac = in.readInt();
        this.mNrArfcn = in.readInt();
        this.mNci = in.readLong();
    }

    protected static CellIdentityNr createFromParcelBody(Parcel in) {
        return new CellIdentityNr(in);
    }
}
