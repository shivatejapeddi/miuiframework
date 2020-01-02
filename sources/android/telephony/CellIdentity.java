package android.telephony;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import java.util.Objects;
import java.util.UUID;

public abstract class CellIdentity implements Parcelable {
    public static final Creator<CellIdentity> CREATOR = new Creator<CellIdentity>() {
        public CellIdentity createFromParcel(Parcel in) {
            switch (in.readInt()) {
                case 1:
                    return CellIdentityGsm.createFromParcelBody(in);
                case 2:
                    return CellIdentityCdma.createFromParcelBody(in);
                case 3:
                    return CellIdentityLte.createFromParcelBody(in);
                case 4:
                    return CellIdentityWcdma.createFromParcelBody(in);
                case 5:
                    return CellIdentityTdscdma.createFromParcelBody(in);
                case 6:
                    return CellIdentityNr.createFromParcelBody(in);
                default:
                    throw new IllegalArgumentException("Bad Cell identity Parcel");
            }
        }

        public CellIdentity[] newArray(int size) {
            return new CellIdentity[size];
        }
    };
    public static final int INVALID_CHANNEL_NUMBER = -1;
    protected String mAlphaLong;
    protected String mAlphaShort;
    protected final String mMccStr;
    protected final String mMncStr;
    protected final String mTag;
    protected final int mType;

    public abstract CellLocation asCellLocation();

    protected CellIdentity(String tag, int type, String mcc, String mnc, String alphal, String alphas) {
        this.mTag = tag;
        this.mType = type;
        if (mcc == null || mcc.matches("^[0-9]{3}$")) {
            this.mMccStr = mcc;
        } else if (mcc.isEmpty() || mcc.equals(String.valueOf(Integer.MAX_VALUE))) {
            this.mMccStr = null;
        } else {
            this.mMccStr = null;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("invalid MCC format: ");
            stringBuilder.append(mcc);
            log(stringBuilder.toString());
        }
        if (mnc == null || mnc.matches("^[0-9]{2,3}$")) {
            this.mMncStr = mnc;
        } else if (mnc.isEmpty() || mnc.equals(String.valueOf(Integer.MAX_VALUE))) {
            this.mMncStr = null;
        } else {
            this.mMncStr = null;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("invalid MNC format: ");
            stringBuilder2.append(mnc);
            log(stringBuilder2.toString());
        }
        if ((this.mMccStr != null && this.mMncStr == null) || (this.mMccStr == null && this.mMncStr != null)) {
            AnomalyReporter.reportAnomaly(UUID.fromString("a3ab0b9d-f2aa-4baf-911d-7096c0d4645a"), "CellIdentity Missing Half of PLMN ID");
        }
        this.mAlphaLong = alphal;
        this.mAlphaShort = alphas;
    }

    public int describeContents() {
        return 0;
    }

    public int getType() {
        return this.mType;
    }

    public String getMccString() {
        return this.mMccStr;
    }

    public String getMncString() {
        return this.mMncStr;
    }

    public int getChannelNumber() {
        return -1;
    }

    public CharSequence getOperatorAlphaLong() {
        return this.mAlphaLong;
    }

    public void setOperatorAlphaLong(String alphaLong) {
        this.mAlphaLong = alphaLong;
    }

    public CharSequence getOperatorAlphaShort() {
        return this.mAlphaShort;
    }

    public void setOperatorAlphaShort(String alphaShort) {
        this.mAlphaShort = alphaShort;
    }

    public boolean equals(Object other) {
        boolean z = false;
        if (!(other instanceof CellIdentity)) {
            return false;
        }
        CellIdentity o = (CellIdentity) other;
        if (this.mType == o.mType && TextUtils.equals(this.mMccStr, o.mMccStr) && TextUtils.equals(this.mMncStr, o.mMncStr) && TextUtils.equals(this.mAlphaLong, o.mAlphaLong) && TextUtils.equals(this.mAlphaShort, o.mAlphaShort)) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mAlphaLong, this.mAlphaShort, this.mMccStr, this.mMncStr, Integer.valueOf(this.mType)});
    }

    public void writeToParcel(Parcel dest, int type) {
        dest.writeInt(type);
        dest.writeString(this.mMccStr);
        dest.writeString(this.mMncStr);
        dest.writeString(this.mAlphaLong);
        dest.writeString(this.mAlphaShort);
    }

    protected CellIdentity(String tag, int type, Parcel source) {
        this(tag, type, source.readString(), source.readString(), source.readString(), source.readString());
    }

    /* Access modifiers changed, original: protected */
    public void log(String s) {
        Rlog.w(this.mTag, s);
    }

    protected static final int inRangeOrUnavailable(int value, int rangeMin, int rangeMax) {
        if (value < rangeMin || value > rangeMax) {
            return Integer.MAX_VALUE;
        }
        return value;
    }

    protected static final long inRangeOrUnavailable(long value, long rangeMin, long rangeMax) {
        if (value < rangeMin || value > rangeMax) {
            return Long.MAX_VALUE;
        }
        return value;
    }

    protected static final int inRangeOrUnavailable(int value, int rangeMin, int rangeMax, int special) {
        if ((value < rangeMin || value > rangeMax) && value != special) {
            return Integer.MAX_VALUE;
        }
        return value;
    }
}
