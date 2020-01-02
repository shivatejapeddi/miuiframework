package android.telephony;

import android.content.pm.PackageManager;
import android.hardware.radio.V1_0.CdmaSignalStrength;
import android.hardware.radio.V1_0.EvdoSignalStrength;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import java.util.Objects;

public final class CellSignalStrengthCdma extends CellSignalStrength implements Parcelable {
    public static final Creator<CellSignalStrengthCdma> CREATOR = new Creator<CellSignalStrengthCdma>() {
        public CellSignalStrengthCdma createFromParcel(Parcel in) {
            return new CellSignalStrengthCdma(in, null);
        }

        public CellSignalStrengthCdma[] newArray(int size) {
            return new CellSignalStrengthCdma[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellSignalStrengthCdma";
    private static final CellSignalStrengthCdma sInvalid = new CellSignalStrengthCdma();
    private int mCdmaDbm;
    private int mCdmaEcio;
    private int mEvdoDbm;
    private int mEvdoEcio;
    private int mEvdoSnr;
    private int mLevel;

    public CellSignalStrengthCdma() {
        setDefaultValues();
    }

    public CellSignalStrengthCdma(int cdmaDbm, int cdmaEcio, int evdoDbm, int evdoEcio, int evdoSnr) {
        this.mCdmaDbm = CellSignalStrength.inRangeOrUnavailable(cdmaDbm, PackageManager.INSTALL_FAILED_MULTIPACKAGE_INCONSISTENCY, 0);
        this.mCdmaEcio = CellSignalStrength.inRangeOrUnavailable(cdmaEcio, -160, 0);
        this.mEvdoDbm = CellSignalStrength.inRangeOrUnavailable(evdoDbm, PackageManager.INSTALL_FAILED_MULTIPACKAGE_INCONSISTENCY, 0);
        this.mEvdoEcio = CellSignalStrength.inRangeOrUnavailable(evdoEcio, -160, 0);
        this.mEvdoSnr = CellSignalStrength.inRangeOrUnavailable(evdoSnr, 0, 8);
        updateLevel(null, null);
    }

    public CellSignalStrengthCdma(CdmaSignalStrength cdma, EvdoSignalStrength evdo) {
        this(-cdma.dbm, -cdma.ecio, -evdo.dbm, -evdo.ecio, evdo.signalNoiseRatio);
    }

    public CellSignalStrengthCdma(CellSignalStrengthCdma s) {
        copyFrom(s);
    }

    /* Access modifiers changed, original: protected */
    public void copyFrom(CellSignalStrengthCdma s) {
        this.mCdmaDbm = s.mCdmaDbm;
        this.mCdmaEcio = s.mCdmaEcio;
        this.mEvdoDbm = s.mEvdoDbm;
        this.mEvdoEcio = s.mEvdoEcio;
        this.mEvdoSnr = s.mEvdoSnr;
        this.mLevel = s.mLevel;
        this.mMiuiLevel = s.mMiuiLevel;
    }

    public CellSignalStrengthCdma copy() {
        return new CellSignalStrengthCdma(this);
    }

    public void setDefaultValues() {
        this.mCdmaDbm = Integer.MAX_VALUE;
        this.mCdmaEcio = Integer.MAX_VALUE;
        this.mEvdoDbm = Integer.MAX_VALUE;
        this.mEvdoEcio = Integer.MAX_VALUE;
        this.mEvdoSnr = Integer.MAX_VALUE;
        this.mLevel = 0;
        this.mMiuiLevel = 0;
    }

    public int getLevel() {
        return this.mLevel;
    }

    public void updateLevel(PersistableBundle cc, ServiceState ss) {
        this.mMiuiLevel = MiuiCellSignalStrengthCdma.updateLevel(cc, ss, this.mCdmaDbm, this.mCdmaEcio, this.mEvdoDbm, this.mEvdoEcio, this.mEvdoSnr);
        int cdmaLevel = getCdmaLevel();
        int evdoLevel = getEvdoLevel();
        if (evdoLevel == 0) {
            this.mLevel = getCdmaLevel();
        } else if (cdmaLevel == 0) {
            this.mLevel = getEvdoLevel();
        } else {
            this.mLevel = cdmaLevel < evdoLevel ? cdmaLevel : evdoLevel;
        }
    }

    public int getAsuLevel() {
        int cdmaAsuLevel;
        int ecioAsuLevel;
        int cdmaDbm = getCdmaDbm();
        int cdmaEcio = getCdmaEcio();
        if (cdmaDbm == Integer.MAX_VALUE) {
            cdmaAsuLevel = 99;
        } else if (cdmaDbm >= -75) {
            cdmaAsuLevel = 16;
        } else if (cdmaDbm >= -82) {
            cdmaAsuLevel = 8;
        } else if (cdmaDbm >= -90) {
            cdmaAsuLevel = 4;
        } else if (cdmaDbm >= -95) {
            cdmaAsuLevel = 2;
        } else if (cdmaDbm >= -100) {
            cdmaAsuLevel = 1;
        } else {
            cdmaAsuLevel = 99;
        }
        if (cdmaEcio == Integer.MAX_VALUE) {
            ecioAsuLevel = 99;
        } else if (cdmaEcio >= -90) {
            ecioAsuLevel = 16;
        } else if (cdmaEcio >= -100) {
            ecioAsuLevel = 8;
        } else if (cdmaEcio >= PackageManager.INSTALL_FAILED_ABORTED) {
            ecioAsuLevel = 4;
        } else if (cdmaEcio >= -130) {
            ecioAsuLevel = 2;
        } else if (cdmaEcio >= -150) {
            ecioAsuLevel = 1;
        } else {
            ecioAsuLevel = 99;
        }
        int i;
        if (cdmaAsuLevel < ecioAsuLevel) {
            i = cdmaAsuLevel;
        } else {
            i = ecioAsuLevel;
        }
        return cdmaAsuLevel;
    }

    public int getCdmaLevel() {
        int levelDbm;
        int levelEcio;
        int cdmaDbm = getCdmaDbm();
        int cdmaEcio = getCdmaEcio();
        if (cdmaDbm == Integer.MAX_VALUE) {
            levelDbm = 0;
        } else if (cdmaDbm >= -75) {
            levelDbm = 4;
        } else if (cdmaDbm >= -85) {
            levelDbm = 3;
        } else if (cdmaDbm >= -95) {
            levelDbm = 2;
        } else if (cdmaDbm >= -100) {
            levelDbm = 1;
        } else {
            levelDbm = 0;
        }
        if (cdmaEcio == Integer.MAX_VALUE) {
            levelEcio = 0;
        } else if (cdmaEcio >= -90) {
            levelEcio = 4;
        } else if (cdmaEcio >= -110) {
            levelEcio = 3;
        } else if (cdmaEcio >= -130) {
            levelEcio = 2;
        } else if (cdmaEcio >= -150) {
            levelEcio = 1;
        } else {
            levelEcio = 0;
        }
        return levelDbm < levelEcio ? levelDbm : levelEcio;
    }

    public int getEvdoLevel() {
        int levelEvdoDbm;
        int levelEvdoSnr;
        int evdoDbm = getEvdoDbm();
        int evdoSnr = getEvdoSnr();
        if (evdoDbm == Integer.MAX_VALUE) {
            levelEvdoDbm = 0;
        } else if (evdoDbm >= -65) {
            levelEvdoDbm = 4;
        } else if (evdoDbm >= -75) {
            levelEvdoDbm = 3;
        } else if (evdoDbm >= -90) {
            levelEvdoDbm = 2;
        } else if (evdoDbm >= PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING) {
            levelEvdoDbm = 1;
        } else {
            levelEvdoDbm = 0;
        }
        if (evdoSnr == Integer.MAX_VALUE) {
            levelEvdoSnr = 0;
        } else if (evdoSnr >= 7) {
            levelEvdoSnr = 4;
        } else if (evdoSnr >= 5) {
            levelEvdoSnr = 3;
        } else if (evdoSnr >= 3) {
            levelEvdoSnr = 2;
        } else if (evdoSnr >= 1) {
            levelEvdoSnr = 1;
        } else {
            levelEvdoSnr = 0;
        }
        return levelEvdoDbm < levelEvdoSnr ? levelEvdoDbm : levelEvdoSnr;
    }

    public int getEvdoAsuLevel() {
        int levelEvdoDbm;
        int levelEvdoSnr;
        int evdoDbm = getEvdoDbm();
        int evdoSnr = getEvdoSnr();
        if (evdoDbm >= -65) {
            levelEvdoDbm = 16;
        } else if (evdoDbm >= -75) {
            levelEvdoDbm = 8;
        } else if (evdoDbm >= -85) {
            levelEvdoDbm = 4;
        } else if (evdoDbm >= -95) {
            levelEvdoDbm = 2;
        } else if (evdoDbm >= PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING) {
            levelEvdoDbm = 1;
        } else {
            levelEvdoDbm = 99;
        }
        if (evdoSnr >= 7) {
            levelEvdoSnr = 16;
        } else if (evdoSnr >= 6) {
            levelEvdoSnr = 8;
        } else if (evdoSnr >= 5) {
            levelEvdoSnr = 4;
        } else if (evdoSnr >= 3) {
            levelEvdoSnr = 2;
        } else if (evdoSnr >= 1) {
            levelEvdoSnr = 1;
        } else {
            levelEvdoSnr = 99;
        }
        int i;
        if (levelEvdoDbm < levelEvdoSnr) {
            i = levelEvdoDbm;
        } else {
            i = levelEvdoSnr;
        }
        return levelEvdoDbm;
    }

    public int getDbm() {
        int cdmaDbm = getCdmaDbm();
        int evdoDbm = getEvdoDbm();
        return cdmaDbm < evdoDbm ? cdmaDbm : evdoDbm;
    }

    public int getCdmaDbm() {
        return this.mCdmaDbm;
    }

    public void setCdmaDbm(int cdmaDbm) {
        this.mCdmaDbm = cdmaDbm;
    }

    public int getCdmaEcio() {
        return this.mCdmaEcio;
    }

    public void setCdmaEcio(int cdmaEcio) {
        this.mCdmaEcio = cdmaEcio;
    }

    public int getEvdoDbm() {
        return this.mEvdoDbm;
    }

    public void setEvdoDbm(int evdoDbm) {
        this.mEvdoDbm = evdoDbm;
    }

    public int getEvdoEcio() {
        return this.mEvdoEcio;
    }

    public void setEvdoEcio(int evdoEcio) {
        this.mEvdoEcio = evdoEcio;
    }

    public int getEvdoSnr() {
        return this.mEvdoSnr;
    }

    public void setEvdoSnr(int evdoSnr) {
        this.mEvdoSnr = evdoSnr;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mCdmaDbm), Integer.valueOf(this.mCdmaEcio), Integer.valueOf(this.mEvdoDbm), Integer.valueOf(this.mEvdoEcio), Integer.valueOf(this.mEvdoSnr), Integer.valueOf(this.mLevel)});
    }

    public boolean isValid() {
        return equals(sInvalid) ^ 1;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof CellSignalStrengthCdma)) {
            return false;
        }
        CellSignalStrengthCdma s = (CellSignalStrengthCdma) o;
        if (this.mCdmaDbm == s.mCdmaDbm && this.mCdmaEcio == s.mCdmaEcio && this.mEvdoDbm == s.mEvdoDbm && this.mEvdoEcio == s.mEvdoEcio && this.mEvdoSnr == s.mEvdoSnr && this.mLevel == s.mLevel) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CellSignalStrengthCdma: cdmaDbm=");
        stringBuilder.append(this.mCdmaDbm);
        stringBuilder.append(" cdmaEcio=");
        stringBuilder.append(this.mCdmaEcio);
        stringBuilder.append(" evdoDbm=");
        stringBuilder.append(this.mEvdoDbm);
        stringBuilder.append(" evdoEcio=");
        stringBuilder.append(this.mEvdoEcio);
        stringBuilder.append(" evdoSnr=");
        stringBuilder.append(this.mEvdoSnr);
        stringBuilder.append(" miuiLevel=");
        stringBuilder.append(this.mMiuiLevel);
        stringBuilder.append(" level=");
        stringBuilder.append(this.mLevel);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mCdmaDbm);
        dest.writeInt(this.mCdmaEcio);
        dest.writeInt(this.mEvdoDbm);
        dest.writeInt(this.mEvdoEcio);
        dest.writeInt(this.mEvdoSnr);
        dest.writeInt(this.mLevel);
        dest.writeInt(this.mMiuiLevel);
    }

    private CellSignalStrengthCdma(Parcel in) {
        this.mCdmaDbm = in.readInt();
        this.mCdmaEcio = in.readInt();
        this.mEvdoDbm = in.readInt();
        this.mEvdoEcio = in.readInt();
        this.mEvdoSnr = in.readInt();
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
