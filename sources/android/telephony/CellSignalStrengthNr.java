package android.telephony;

import android.hardware.radio.V1_4.NrSignalStrength;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import java.util.Objects;

public final class CellSignalStrengthNr extends CellSignalStrength implements Parcelable {
    public static final Creator<CellSignalStrengthNr> CREATOR = new Creator<CellSignalStrengthNr>() {
        public CellSignalStrengthNr createFromParcel(Parcel in) {
            return new CellSignalStrengthNr(in, null);
        }

        public CellSignalStrengthNr[] newArray(int size) {
            return new CellSignalStrengthNr[size];
        }
    };
    private static final int SIGNAL_GOOD_THRESHOLD = -105;
    private static final int SIGNAL_GREAT_THRESHOLD = -95;
    private static final int SIGNAL_MODERATE_THRESHOLD = -115;
    private static final String TAG = "CellSignalStrengthNr";
    public static final int UNKNOWN_ASU_LEVEL = 99;
    private static final CellSignalStrengthNr sInvalid = new CellSignalStrengthNr();
    private int mCsiRsrp;
    private int mCsiRsrq;
    private int mCsiSinr;
    private int mLevel;
    private int mSsRsrp;
    private int mSsRsrq;
    private int mSsSinr;

    /* synthetic */ CellSignalStrengthNr(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public CellSignalStrengthNr() {
        setDefaultValues();
    }

    public CellSignalStrengthNr(int csiRsrp, int csiRsrq, int csiSinr, int ssRsrp, int ssRsrq, int ssSinr) {
        this.mCsiRsrp = CellSignalStrength.inRangeOrUnavailable(csiRsrp, -140, -44);
        this.mCsiRsrq = CellSignalStrength.inRangeOrUnavailable(csiRsrq, -20, -3);
        this.mCsiSinr = CellSignalStrength.inRangeOrUnavailable(csiSinr, -23, 23);
        this.mSsRsrp = CellSignalStrength.inRangeOrUnavailable(ssRsrp, -140, -44);
        this.mSsRsrq = CellSignalStrength.inRangeOrUnavailable(ssRsrq, -20, -3);
        this.mSsSinr = CellSignalStrength.inRangeOrUnavailable(ssSinr, -23, 40);
        updateLevel(null, null);
    }

    public CellSignalStrengthNr(NrSignalStrength ss) {
        this(ss.csiRsrp, ss.csiRsrq, ss.csiSinr, ss.ssRsrp, ss.ssRsrq, ss.ssSinr);
    }

    public int getSsRsrp() {
        return this.mSsRsrp;
    }

    public int getSsRsrq() {
        return this.mSsRsrq;
    }

    public int getSsSinr() {
        return this.mSsSinr;
    }

    public int getCsiRsrp() {
        return this.mCsiRsrp;
    }

    public int getCsiRsrq() {
        return this.mCsiRsrq;
    }

    public int getCsiSinr() {
        return this.mCsiSinr;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mCsiRsrp);
        dest.writeInt(this.mCsiRsrq);
        dest.writeInt(this.mCsiSinr);
        dest.writeInt(this.mSsRsrp);
        dest.writeInt(this.mSsRsrq);
        dest.writeInt(this.mSsSinr);
        dest.writeInt(this.mLevel);
        dest.writeInt(this.mMiuiLevel);
    }

    private CellSignalStrengthNr(Parcel in) {
        this.mCsiRsrp = in.readInt();
        this.mCsiRsrq = in.readInt();
        this.mCsiSinr = in.readInt();
        this.mSsRsrp = in.readInt();
        this.mSsRsrq = in.readInt();
        this.mSsSinr = in.readInt();
        this.mLevel = in.readInt();
        this.mMiuiLevel = in.readInt();
    }

    public void setDefaultValues() {
        this.mCsiRsrp = Integer.MAX_VALUE;
        this.mCsiRsrq = Integer.MAX_VALUE;
        this.mCsiSinr = Integer.MAX_VALUE;
        this.mSsRsrp = Integer.MAX_VALUE;
        this.mSsRsrq = Integer.MAX_VALUE;
        this.mSsSinr = Integer.MAX_VALUE;
        this.mLevel = 0;
        this.mMiuiLevel = 0;
    }

    public int getLevel() {
        return this.mLevel;
    }

    public void updateLevel(PersistableBundle cc, ServiceState ss) {
        this.mMiuiLevel = MiuiCellSignalStrengthNr.updateLevel(cc, ss, this.mCsiRsrp);
        int i = this.mSsRsrp;
        if (i == Integer.MAX_VALUE) {
            this.mLevel = 0;
        } else if (i >= -95) {
            this.mLevel = 4;
        } else if (i >= -105) {
            this.mLevel = 3;
        } else if (i >= -115) {
            this.mLevel = 2;
        } else {
            this.mLevel = 1;
        }
    }

    public int getAsuLevel() {
        int nrDbm = getDbm();
        if (nrDbm == Integer.MAX_VALUE) {
            return 99;
        }
        if (nrDbm <= -140) {
            return 0;
        }
        if (nrDbm >= -43) {
            return 97;
        }
        return nrDbm + 140;
    }

    public int getDbm() {
        return this.mSsRsrp;
    }

    public CellSignalStrengthNr(CellSignalStrengthNr s) {
        this.mCsiRsrp = s.mCsiRsrp;
        this.mCsiRsrq = s.mCsiRsrq;
        this.mCsiSinr = s.mCsiSinr;
        this.mSsRsrp = s.mSsRsrp;
        this.mSsRsrq = s.mSsRsrq;
        this.mSsSinr = s.mSsSinr;
        this.mLevel = s.mLevel;
        this.mMiuiLevel = s.mMiuiLevel;
    }

    public CellSignalStrengthNr copy() {
        return new CellSignalStrengthNr(this);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mCsiRsrp), Integer.valueOf(this.mCsiRsrq), Integer.valueOf(this.mCsiSinr), Integer.valueOf(this.mSsRsrp), Integer.valueOf(this.mSsRsrq), Integer.valueOf(this.mSsSinr), Integer.valueOf(this.mLevel)});
    }

    public boolean isValid() {
        return equals(sInvalid) ^ 1;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof CellSignalStrengthNr)) {
            return false;
        }
        CellSignalStrengthNr o = (CellSignalStrengthNr) obj;
        if (this.mCsiRsrp == o.mCsiRsrp && this.mCsiRsrq == o.mCsiRsrq && this.mCsiSinr == o.mCsiSinr && this.mSsRsrp == o.mSsRsrp && this.mSsRsrq == o.mSsRsrq && this.mSsSinr == o.mSsSinr && this.mLevel == o.mLevel) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CellSignalStrengthNr:{");
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" csiRsrp = ");
        stringBuilder2.append(this.mCsiRsrp);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" csiRsrq = ");
        stringBuilder2.append(this.mCsiRsrq);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" csiSinr = ");
        stringBuilder2.append(this.mCsiSinr);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" ssRsrp = ");
        stringBuilder2.append(this.mSsRsrp);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" ssRsrq = ");
        stringBuilder2.append(this.mSsRsrq);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" ssSinr = ");
        stringBuilder2.append(this.mSsSinr);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" miuiLevel = ");
        stringBuilder2.append(this.mMiuiLevel);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder2 = new StringBuilder();
        stringBuilder2.append(" level = ");
        stringBuilder2.append(this.mLevel);
        stringBuilder.append(stringBuilder2.toString());
        stringBuilder.append(" }");
        return stringBuilder.toString();
    }
}
