package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import java.util.Objects;
import miui.telephony.PhoneDebug;

public final class CellIdentityGsm extends CellIdentity {
    public static final Creator<CellIdentityGsm> CREATOR = new Creator<CellIdentityGsm>() {
        public CellIdentityGsm createFromParcel(Parcel in) {
            in.readInt();
            return CellIdentityGsm.createFromParcelBody(in);
        }

        public CellIdentityGsm[] newArray(int size) {
            return new CellIdentityGsm[size];
        }
    };
    private static final boolean DBG = false;
    private static final int MAX_ARFCN = 65535;
    private static final int MAX_BSIC = 63;
    private static final int MAX_CID = 65535;
    private static final int MAX_LAC = 65535;
    private static final String TAG = CellIdentityGsm.class.getSimpleName();
    private final int mArfcn;
    private final int mBsic;
    private final int mCid;
    private final int mLac;

    @UnsupportedAppUsage
    public CellIdentityGsm() {
        super(TAG, 1, null, null, null, null);
        this.mLac = Integer.MAX_VALUE;
        this.mCid = Integer.MAX_VALUE;
        this.mArfcn = Integer.MAX_VALUE;
        this.mBsic = Integer.MAX_VALUE;
    }

    public CellIdentityGsm(int lac, int cid, int arfcn, int bsic, String mccStr, String mncStr, String alphal, String alphas) {
        super(TAG, 1, mccStr, mncStr, alphal, alphas);
        this.mLac = CellIdentity.inRangeOrUnavailable(lac, 0, 65535);
        this.mCid = CellIdentity.inRangeOrUnavailable(cid, 0, 65535);
        this.mArfcn = CellIdentity.inRangeOrUnavailable(arfcn, 0, 65535);
        this.mBsic = CellIdentity.inRangeOrUnavailable(bsic, 0, 63);
    }

    public CellIdentityGsm(android.hardware.radio.V1_0.CellIdentityGsm cid) {
        this(cid.lac, cid.cid, cid.arfcn, cid.bsic == (byte) -1 ? (byte) -1 : cid.bsic, cid.mcc, cid.mnc, "", "");
    }

    public CellIdentityGsm(android.hardware.radio.V1_2.CellIdentityGsm cid) {
        this(cid.base.lac, cid.base.cid, cid.base.arfcn, cid.base.bsic == (byte) -1 ? (byte) -1 : cid.base.bsic, cid.base.mcc, cid.base.mnc, cid.operatorNames.alphaLong, cid.operatorNames.alphaShort);
    }

    private CellIdentityGsm(CellIdentityGsm cid) {
        this(cid.mLac, cid.mCid, cid.mArfcn, cid.mBsic, cid.mMccStr, cid.mMncStr, cid.mAlphaLong, cid.mAlphaShort);
    }

    /* Access modifiers changed, original: 0000 */
    public CellIdentityGsm copy() {
        return new CellIdentityGsm(this);
    }

    public CellIdentityGsm sanitizeLocationInfo() {
        return new CellIdentityGsm(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, this.mMccStr, this.mMncStr, this.mAlphaLong, this.mAlphaShort);
    }

    @Deprecated
    public int getMcc() {
        return this.mMccStr != null ? Integer.valueOf(this.mMccStr).intValue() : Integer.MAX_VALUE;
    }

    @Deprecated
    public int getMnc() {
        return this.mMncStr != null ? Integer.valueOf(this.mMncStr).intValue() : Integer.MAX_VALUE;
    }

    public int getLac() {
        return this.mLac;
    }

    public int getCid() {
        return this.mCid;
    }

    public int getArfcn() {
        return this.mArfcn;
    }

    public int getBsic() {
        return this.mBsic;
    }

    public String getMobileNetworkOperator() {
        if (this.mMccStr == null || this.mMncStr == null) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mMccStr);
        stringBuilder.append(this.mMncStr);
        return stringBuilder.toString();
    }

    public String getMccString() {
        return this.mMccStr;
    }

    public String getMncString() {
        return this.mMncStr;
    }

    public int getChannelNumber() {
        return this.mArfcn;
    }

    @Deprecated
    public int getPsc() {
        return Integer.MAX_VALUE;
    }

    public GsmCellLocation asCellLocation() {
        GsmCellLocation cl = new GsmCellLocation();
        int lac = this.mLac;
        if (lac == Integer.MAX_VALUE) {
            lac = -1;
        }
        int i = this.mCid;
        if (i == Integer.MAX_VALUE) {
            i = -1;
        }
        cl.setLacAndCid(lac, i);
        cl.setPsc(-1);
        return cl;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mLac), Integer.valueOf(this.mCid), Integer.valueOf(super.hashCode())});
    }

    public boolean equals(Object other) {
        boolean z = true;
        if (this == other) {
            return true;
        }
        if (!(other instanceof CellIdentityGsm)) {
            return false;
        }
        CellIdentityGsm o = (CellIdentityGsm) other;
        if (!(this.mLac == o.mLac && this.mCid == o.mCid && this.mArfcn == o.mArfcn && this.mBsic == o.mBsic && TextUtils.equals(this.mMccStr, o.mMccStr) && TextUtils.equals(this.mMncStr, o.mMncStr) && super.equals(other))) {
            z = false;
        }
        return z;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(TAG);
        if (PhoneDebug.VDBG) {
            sb.append(" mLac=");
            sb.append(this.mLac);
            sb.append(" mCid=");
            sb.append(this.mCid);
            sb.append(" mArfcn=");
            sb.append(this.mArfcn);
            sb.append(" mBsic=");
            sb.append("0x");
            sb.append(Integer.toHexString(this.mBsic));
        }
        sb.append(" mMcc=");
        sb.append(this.mMccStr);
        sb.append(" mMnc=");
        sb.append(this.mMncStr);
        sb.append(" mAlphaLong=");
        sb.append(this.mAlphaLong);
        sb.append(" mAlphaShort=");
        sb.append(this.mAlphaShort);
        sb.append("}");
        return sb.toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, 1);
        dest.writeInt(this.mLac);
        dest.writeInt(this.mCid);
        dest.writeInt(this.mArfcn);
        dest.writeInt(this.mBsic);
    }

    private CellIdentityGsm(Parcel in) {
        super(TAG, 1, in);
        this.mLac = in.readInt();
        this.mCid = in.readInt();
        this.mArfcn = in.readInt();
        this.mBsic = in.readInt();
    }

    protected static CellIdentityGsm createFromParcelBody(Parcel in) {
        return new CellIdentityGsm(in);
    }
}
