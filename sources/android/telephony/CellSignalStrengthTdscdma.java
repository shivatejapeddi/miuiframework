package android.telephony;

import android.hardware.radio.V1_0.TdScdmaSignalStrength;
import android.hardware.radio.V1_2.TdscdmaSignalStrength;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import java.util.Objects;

public final class CellSignalStrengthTdscdma extends CellSignalStrength implements Parcelable {
    public static final Creator<CellSignalStrengthTdscdma> CREATOR = new Creator<CellSignalStrengthTdscdma>() {
        public CellSignalStrengthTdscdma createFromParcel(Parcel in) {
            return new CellSignalStrengthTdscdma(in, null);
        }

        public CellSignalStrengthTdscdma[] newArray(int size) {
            return new CellSignalStrengthTdscdma[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellSignalStrengthTdscdma";
    private static final int TDSCDMA_RSCP_GOOD = -73;
    private static final int TDSCDMA_RSCP_GREAT = -49;
    private static final int TDSCDMA_RSCP_MAX = -24;
    private static final int TDSCDMA_RSCP_MIN = -120;
    private static final int TDSCDMA_RSCP_MODERATE = -97;
    private static final int TDSCDMA_RSCP_POOR = -110;
    private static final CellSignalStrengthTdscdma sInvalid = new CellSignalStrengthTdscdma();
    private int mBitErrorRate;
    private int mLevel;
    private int mRscp;
    private int mRssi;

    /* synthetic */ CellSignalStrengthTdscdma(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public CellSignalStrengthTdscdma() {
        setDefaultValues();
    }

    public CellSignalStrengthTdscdma(int rssi, int ber, int rscp) {
        this.mRssi = CellSignalStrength.inRangeOrUnavailable(rssi, -113, -51);
        this.mBitErrorRate = CellSignalStrength.inRangeOrUnavailable(ber, 0, 7, 99);
        this.mRscp = CellSignalStrength.inRangeOrUnavailable(rscp, -120, -24);
        updateLevel(null, null);
    }

    public CellSignalStrengthTdscdma(TdScdmaSignalStrength tdscdma) {
        this(Integer.MAX_VALUE, Integer.MAX_VALUE, tdscdma.rscp != Integer.MAX_VALUE ? -tdscdma.rscp : tdscdma.rscp);
        if (this.mRssi == Integer.MAX_VALUE && this.mRscp == Integer.MAX_VALUE) {
            setDefaultValues();
        }
    }

    public CellSignalStrengthTdscdma(TdscdmaSignalStrength tdscdma) {
        this(CellSignalStrength.getRssiDbmFromAsu(tdscdma.signalStrength), tdscdma.bitErrorRate, CellSignalStrength.getRscpDbmFromAsu(tdscdma.rscp));
        if (this.mRssi == Integer.MAX_VALUE && this.mRscp == Integer.MAX_VALUE) {
            setDefaultValues();
        }
    }

    public CellSignalStrengthTdscdma(CellSignalStrengthTdscdma s) {
        copyFrom(s);
    }

    /* Access modifiers changed, original: protected */
    public void copyFrom(CellSignalStrengthTdscdma s) {
        this.mRssi = s.mRssi;
        this.mBitErrorRate = s.mBitErrorRate;
        this.mRscp = s.mRscp;
        this.mLevel = s.mLevel;
        this.mMiuiLevel = s.mMiuiLevel;
    }

    public CellSignalStrengthTdscdma copy() {
        return new CellSignalStrengthTdscdma(this);
    }

    public void setDefaultValues() {
        this.mRssi = Integer.MAX_VALUE;
        this.mBitErrorRate = Integer.MAX_VALUE;
        this.mRscp = Integer.MAX_VALUE;
        this.mLevel = 0;
        this.mMiuiLevel = 0;
    }

    public int getLevel() {
        return this.mLevel;
    }

    public void updateLevel(PersistableBundle cc, ServiceState ss) {
        this.mMiuiLevel = MiuiCellSignalStrengthTdscdma.updateLevel(cc, ss, this.mRssi, this.mRscp);
        int i = this.mRscp;
        if (i > -24) {
            this.mLevel = 0;
        } else if (i >= -49) {
            this.mLevel = 4;
        } else if (i >= TDSCDMA_RSCP_GOOD) {
            this.mLevel = 3;
        } else if (i >= -97) {
            this.mLevel = 2;
        } else if (i >= -110) {
            this.mLevel = 1;
        } else {
            this.mLevel = 0;
        }
    }

    public int getDbm() {
        return this.mRscp;
    }

    public int getRscp() {
        return this.mRscp;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public int getBitErrorRate() {
        return this.mBitErrorRate;
    }

    public int getAsuLevel() {
        int i = this.mRscp;
        if (i != Integer.MAX_VALUE) {
            return CellSignalStrength.getAsuFromRscpDbm(i);
        }
        i = this.mRssi;
        if (i != Integer.MAX_VALUE) {
            return CellSignalStrength.getAsuFromRssiDbm(i);
        }
        return CellSignalStrength.getAsuFromRscpDbm(Integer.MAX_VALUE);
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mRssi), Integer.valueOf(this.mBitErrorRate), Integer.valueOf(this.mRscp), Integer.valueOf(this.mLevel)});
    }

    public boolean isValid() {
        return equals(sInvalid) ^ 1;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof CellSignalStrengthTdscdma)) {
            return false;
        }
        CellSignalStrengthTdscdma s = (CellSignalStrengthTdscdma) o;
        if (this.mRssi == s.mRssi && this.mBitErrorRate == s.mBitErrorRate && this.mRscp == s.mRscp && this.mLevel == s.mLevel) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CellSignalStrengthTdscdma: rssi=");
        stringBuilder.append(this.mRssi);
        stringBuilder.append(" ber=");
        stringBuilder.append(this.mBitErrorRate);
        stringBuilder.append(" rscp=");
        stringBuilder.append(this.mRscp);
        stringBuilder.append(" miuiLevel=");
        stringBuilder.append(this.mMiuiLevel);
        stringBuilder.append(" level=");
        stringBuilder.append(this.mLevel);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRssi);
        dest.writeInt(this.mBitErrorRate);
        dest.writeInt(this.mRscp);
        dest.writeInt(this.mLevel);
        dest.writeInt(this.mMiuiLevel);
    }

    private CellSignalStrengthTdscdma(Parcel in) {
        this.mRssi = in.readInt();
        this.mBitErrorRate = in.readInt();
        this.mRscp = in.readInt();
        this.mLevel = in.readInt();
        this.mMiuiLevel = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }
}
