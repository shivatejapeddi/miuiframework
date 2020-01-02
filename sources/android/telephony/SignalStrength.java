package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SignalStrength implements Parcelable {
    @UnsupportedAppUsage
    public static final Creator<SignalStrength> CREATOR = new Creator() {
        public SignalStrength createFromParcel(Parcel in) {
            return new SignalStrength(in);
        }

        public SignalStrength[] newArray(int size) {
            return new SignalStrength[size];
        }
    };
    private static final boolean DBG = false;
    public static final int INVALID = Integer.MAX_VALUE;
    private static final String LOG_TAG = "SignalStrength";
    private static final int LTE_RSRP_THRESHOLDS_NUM = 4;
    private static final String MEASUREMENT_TYPE_RSCP = "rscp";
    @UnsupportedAppUsage
    public static final int NUM_SIGNAL_STRENGTH_BINS = 5;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final int SIGNAL_STRENGTH_GOOD = 3;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final int SIGNAL_STRENGTH_GREAT = 4;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final int SIGNAL_STRENGTH_MODERATE = 2;
    public static final String[] SIGNAL_STRENGTH_NAMES = new String[]{"none", "poor", "moderate", "good", "great"};
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final int SIGNAL_STRENGTH_POOR = 1;
    private static final int WCDMA_RSCP_THRESHOLDS_NUM = 4;
    CellSignalStrengthCdma mCdma;
    CellSignalStrengthGsm mGsm;
    CellSignalStrengthLte mLte;
    CellSignalStrengthNr mNr;
    CellSignalStrengthTdscdma mTdscdma;
    CellSignalStrengthWcdma mWcdma;

    @UnsupportedAppUsage
    public static SignalStrength newFromBundle(Bundle m) {
        SignalStrength ret = new SignalStrength();
        ret.setFromNotifierBundle(m);
        return ret;
    }

    @UnsupportedAppUsage
    public SignalStrength() {
        this(new CellSignalStrengthCdma(), new CellSignalStrengthGsm(), new CellSignalStrengthWcdma(), new CellSignalStrengthTdscdma(), new CellSignalStrengthLte(), new CellSignalStrengthNr());
    }

    public SignalStrength(CellSignalStrengthCdma cdma, CellSignalStrengthGsm gsm, CellSignalStrengthWcdma wcdma, CellSignalStrengthTdscdma tdscdma, CellSignalStrengthLte lte, CellSignalStrengthNr nr) {
        this.mCdma = cdma;
        this.mGsm = gsm;
        this.mWcdma = wcdma;
        this.mTdscdma = tdscdma;
        this.mLte = lte;
        this.mNr = nr;
    }

    public SignalStrength(android.hardware.radio.V1_0.SignalStrength signalStrength) {
        this(new CellSignalStrengthCdma(signalStrength.cdma, signalStrength.evdo), new CellSignalStrengthGsm(signalStrength.gw), new CellSignalStrengthWcdma(), new CellSignalStrengthTdscdma(signalStrength.tdScdma), new CellSignalStrengthLte(signalStrength.lte), new CellSignalStrengthNr());
    }

    public SignalStrength(android.hardware.radio.V1_2.SignalStrength signalStrength) {
        this(new CellSignalStrengthCdma(signalStrength.cdma, signalStrength.evdo), new CellSignalStrengthGsm(signalStrength.gsm), new CellSignalStrengthWcdma(signalStrength.wcdma), new CellSignalStrengthTdscdma(signalStrength.tdScdma), new CellSignalStrengthLte(signalStrength.lte), new CellSignalStrengthNr());
    }

    public SignalStrength(android.hardware.radio.V1_4.SignalStrength signalStrength) {
        this(new CellSignalStrengthCdma(signalStrength.cdma, signalStrength.evdo), new CellSignalStrengthGsm(signalStrength.gsm), new CellSignalStrengthWcdma(signalStrength.wcdma), new CellSignalStrengthTdscdma(signalStrength.tdscdma), new CellSignalStrengthLte(signalStrength.lte), new CellSignalStrengthNr(signalStrength.nr));
    }

    private CellSignalStrength getPrimary() {
        if (this.mLte.isValid()) {
            return this.mLte;
        }
        if (this.mCdma.isValid()) {
            return this.mCdma;
        }
        if (this.mTdscdma.isValid()) {
            return this.mTdscdma;
        }
        if (this.mWcdma.isValid()) {
            return this.mWcdma;
        }
        if (this.mGsm.isValid()) {
            return this.mGsm;
        }
        if (this.mNr.isValid()) {
            return this.mNr;
        }
        return this.mLte;
    }

    public List<CellSignalStrength> getCellSignalStrengths() {
        return getCellSignalStrengths(CellSignalStrength.class);
    }

    public <T extends CellSignalStrength> List<T> getCellSignalStrengths(Class<T> clazz) {
        List<T> cssList = new ArrayList(2);
        if (this.mLte.isValid() && clazz.isAssignableFrom(CellSignalStrengthLte.class)) {
            cssList.add(this.mLte);
        }
        if (this.mCdma.isValid() && clazz.isAssignableFrom(CellSignalStrengthCdma.class)) {
            cssList.add(this.mCdma);
        }
        if (this.mTdscdma.isValid() && clazz.isAssignableFrom(CellSignalStrengthTdscdma.class)) {
            cssList.add(this.mTdscdma);
        }
        if (this.mWcdma.isValid() && clazz.isAssignableFrom(CellSignalStrengthWcdma.class)) {
            cssList.add(this.mWcdma);
        }
        if (this.mGsm.isValid() && clazz.isAssignableFrom(CellSignalStrengthGsm.class)) {
            cssList.add(this.mGsm);
        }
        if (this.mNr.isValid() && clazz.isAssignableFrom(CellSignalStrengthNr.class)) {
            cssList.add(this.mNr);
        }
        return cssList;
    }

    public void updateLevel(PersistableBundle cc, ServiceState ss) {
        this.mCdma.updateLevel(cc, ss);
        this.mGsm.updateLevel(cc, ss);
        this.mWcdma.updateLevel(cc, ss);
        this.mTdscdma.updateLevel(cc, ss);
        this.mLte.updateLevel(cc, ss);
        this.mNr.updateLevel(cc, ss);
    }

    @UnsupportedAppUsage
    public SignalStrength(SignalStrength s) {
        copyFrom(s);
    }

    /* Access modifiers changed, original: protected */
    @UnsupportedAppUsage
    public void copyFrom(SignalStrength s) {
        this.mCdma = new CellSignalStrengthCdma(s.mCdma);
        this.mGsm = new CellSignalStrengthGsm(s.mGsm);
        this.mWcdma = new CellSignalStrengthWcdma(s.mWcdma);
        this.mTdscdma = new CellSignalStrengthTdscdma(s.mTdscdma);
        this.mLte = new CellSignalStrengthLte(s.mLte);
        this.mNr = new CellSignalStrengthNr(s.mNr);
    }

    @UnsupportedAppUsage
    public SignalStrength(Parcel in) {
        this.mCdma = (CellSignalStrengthCdma) in.readParcelable(CellSignalStrengthCdma.class.getClassLoader());
        this.mGsm = (CellSignalStrengthGsm) in.readParcelable(CellSignalStrengthGsm.class.getClassLoader());
        this.mWcdma = (CellSignalStrengthWcdma) in.readParcelable(CellSignalStrengthWcdma.class.getClassLoader());
        this.mTdscdma = (CellSignalStrengthTdscdma) in.readParcelable(CellSignalStrengthTdscdma.class.getClassLoader());
        this.mLte = (CellSignalStrengthLte) in.readParcelable(CellSignalStrengthLte.class.getClassLoader());
        this.mNr = (CellSignalStrengthNr) in.readParcelable(CellSignalStrengthLte.class.getClassLoader());
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeParcelable(this.mCdma, flags);
        out.writeParcelable(this.mGsm, flags);
        out.writeParcelable(this.mWcdma, flags);
        out.writeParcelable(this.mTdscdma, flags);
        out.writeParcelable(this.mLte, flags);
        out.writeParcelable(this.mNr, flags);
    }

    public int describeContents() {
        return 0;
    }

    @Deprecated
    public int getGsmSignalStrength() {
        return this.mGsm.getAsuLevel();
    }

    @Deprecated
    public int getGsmBitErrorRate() {
        return this.mGsm.getBitErrorRate();
    }

    @Deprecated
    public int getCdmaDbm() {
        return this.mCdma.getCdmaDbm();
    }

    @Deprecated
    public int getCdmaEcio() {
        return this.mCdma.getCdmaEcio();
    }

    @Deprecated
    public int getEvdoDbm() {
        return this.mCdma.getEvdoDbm();
    }

    @Deprecated
    public int getEvdoEcio() {
        return this.mCdma.getEvdoEcio();
    }

    @Deprecated
    public int getEvdoSnr() {
        return this.mCdma.getEvdoSnr();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getLteSignalStrength() {
        return this.mLte.getRssi();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getLteRsrp() {
        return this.mLte.getRsrp();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getLteRsrq() {
        return this.mLte.getRsrq();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getLteRssnr() {
        return this.mLte.getRssnr();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getLteCqi() {
        return this.mLte.getCqi();
    }

    public int getLevel() {
        int level = getPrimary().getLevel();
        if (level >= 0 && level <= 4) {
            return getPrimary().getLevel();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid Level ");
        stringBuilder.append(level);
        stringBuilder.append(", this=");
        stringBuilder.append(this);
        loge(stringBuilder.toString());
        return 0;
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getAsuLevel() {
        return getPrimary().getAsuLevel();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getDbm() {
        return getPrimary().getDbm();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getGsmDbm() {
        return this.mGsm.getDbm();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getGsmLevel() {
        return this.mGsm.getLevel();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getGsmAsuLevel() {
        return this.mGsm.getAsuLevel();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getCdmaLevel() {
        return this.mCdma.getLevel();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getCdmaAsuLevel() {
        return this.mCdma.getAsuLevel();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getEvdoLevel() {
        return this.mCdma.getEvdoLevel();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getEvdoAsuLevel() {
        return this.mCdma.getEvdoAsuLevel();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getLteDbm() {
        return this.mLte.getRsrp();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getLteLevel() {
        return this.mLte.getLevel();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getLteAsuLevel() {
        return this.mLte.getAsuLevel();
    }

    @Deprecated
    public boolean isGsm() {
        return !(getPrimary() instanceof CellSignalStrengthCdma);
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getTdScdmaDbm() {
        return this.mTdscdma.getRscp();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getTdScdmaLevel() {
        return this.mTdscdma.getLevel();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getTdScdmaAsuLevel() {
        return this.mTdscdma.getAsuLevel();
    }

    @Deprecated
    public int getWcdmaRscp() {
        return this.mWcdma.getRscp();
    }

    @Deprecated
    public int getWcdmaAsuLevel() {
        return this.mWcdma.getAsuLevel();
    }

    @Deprecated
    public int getWcdmaDbm() {
        return this.mWcdma.getDbm();
    }

    @Deprecated
    public int getWcdmaLevel() {
        return this.mWcdma.getLevel();
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mCdma, this.mGsm, this.mWcdma, this.mTdscdma, this.mLte, this.mNr});
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof SignalStrength)) {
            return false;
        }
        SignalStrength s = (SignalStrength) o;
        if (this.mCdma.equals(s.mCdma) && this.mGsm.equals(s.mGsm) && this.mWcdma.equals(s.mWcdma) && this.mTdscdma.equals(s.mTdscdma) && this.mLte.equals(s.mLte) && this.mNr.equals(s.mNr)) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SignalStrength:{");
        stringBuilder.append("mCdma=");
        stringBuilder.append(this.mCdma);
        stringBuilder.append(",mGsm=");
        stringBuilder.append(this.mGsm);
        stringBuilder.append(",mWcdma=");
        stringBuilder.append(this.mWcdma);
        stringBuilder.append(",mTdscdma=");
        stringBuilder.append(this.mTdscdma);
        stringBuilder.append(",mLte=");
        stringBuilder.append(this.mLte);
        stringBuilder.append(",mNr=");
        stringBuilder.append(this.mNr);
        stringBuilder.append(",primary=");
        stringBuilder.append(getPrimary().getClass().getSimpleName());
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private void setFromNotifierBundle(Bundle m) {
        this.mCdma = (CellSignalStrengthCdma) m.getParcelable("Cdma");
        this.mGsm = (CellSignalStrengthGsm) m.getParcelable("Gsm");
        this.mWcdma = (CellSignalStrengthWcdma) m.getParcelable("Wcdma");
        this.mTdscdma = (CellSignalStrengthTdscdma) m.getParcelable("Tdscdma");
        this.mLte = (CellSignalStrengthLte) m.getParcelable("Lte");
        this.mNr = (CellSignalStrengthNr) m.getParcelable("Nr");
    }

    @Deprecated
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public void fillInNotifierBundle(Bundle m) {
        m.putParcelable("Cdma", this.mCdma);
        m.putParcelable("Gsm", this.mGsm);
        m.putParcelable("Wcdma", this.mWcdma);
        m.putParcelable("Tdscdma", this.mTdscdma);
        m.putParcelable("Lte", this.mLte);
        m.putParcelable("Nr", this.mNr);
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }

    private static void loge(String s) {
        Rlog.e(LOG_TAG, s);
    }

    public int getMiuiLevel() {
        int level = getPrimary().getMiuiLevel();
        if (level >= 0 && level <= 5) {
            return level;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Invalid Miui Level ");
        stringBuilder.append(level);
        stringBuilder.append(", this=");
        stringBuilder.append(this);
        loge(stringBuilder.toString());
        return 0;
    }
}
