package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.content.pm.PackageManager;
import android.hardware.radio.V1_0.LteSignalStrength;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.PersistableBundle;
import java.util.Objects;

public final class CellSignalStrengthLte extends CellSignalStrength implements Parcelable {
    public static final Creator<CellSignalStrengthLte> CREATOR = new Creator<CellSignalStrengthLte>() {
        public CellSignalStrengthLte createFromParcel(Parcel in) {
            return new CellSignalStrengthLte(in, null);
        }

        public CellSignalStrengthLte[] newArray(int size) {
            return new CellSignalStrengthLte[size];
        }
    };
    private static final boolean DBG = false;
    private static final String LOG_TAG = "CellSignalStrengthLte";
    private static final int MAX_LTE_RSRP = -44;
    private static final int MIN_LTE_RSRP = -140;
    private static final int SIGNAL_STRENGTH_LTE_RSSI_ASU_UNKNOWN = 99;
    private static final int SIGNAL_STRENGTH_LTE_RSSI_VALID_ASU_MAX_VALUE = 31;
    private static final int SIGNAL_STRENGTH_LTE_RSSI_VALID_ASU_MIN_VALUE = 0;
    private static final CellSignalStrengthLte sInvalid = new CellSignalStrengthLte();
    private static final int sRsrpBoost = 0;
    private static final int[] sThresholds = new int[]{PackageManager.INSTALL_FAILED_ABORTED, PackageManager.INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING, -95, -85};
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mCqi;
    private int mLevel;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mRsrp;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mRsrq;
    private int mRssi;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mRssnr;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mSignalStrength;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mTimingAdvance;

    /* synthetic */ CellSignalStrengthLte(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    @UnsupportedAppUsage
    public CellSignalStrengthLte() {
        setDefaultValues();
    }

    public CellSignalStrengthLte(int rssi, int rsrp, int rsrq, int rssnr, int cqi, int timingAdvance) {
        this.mRssi = CellSignalStrength.inRangeOrUnavailable(rssi, -113, -51);
        this.mSignalStrength = this.mRssi;
        this.mRsrp = CellSignalStrength.inRangeOrUnavailable(rsrp, MIN_LTE_RSRP, -43);
        this.mRsrq = CellSignalStrength.inRangeOrUnavailable(rsrq, -20, -3);
        this.mRssnr = CellSignalStrength.inRangeOrUnavailable(rssnr, -200, 300);
        this.mCqi = CellSignalStrength.inRangeOrUnavailable(cqi, 0, 15);
        this.mTimingAdvance = CellSignalStrength.inRangeOrUnavailable(timingAdvance, 0, 1282);
        updateLevel(null, null);
    }

    public CellSignalStrengthLte(LteSignalStrength lte) {
        this(convertRssiAsuToDBm(lte.signalStrength), lte.rsrp != Integer.MAX_VALUE ? -lte.rsrp : lte.rsrp, lte.rsrq != Integer.MAX_VALUE ? -lte.rsrq : lte.rsrq, lte.rssnr, lte.cqi, lte.timingAdvance);
    }

    public CellSignalStrengthLte(CellSignalStrengthLte s) {
        copyFrom(s);
    }

    /* Access modifiers changed, original: protected */
    public void copyFrom(CellSignalStrengthLte s) {
        this.mSignalStrength = s.mSignalStrength;
        this.mRssi = s.mRssi;
        this.mRsrp = s.mRsrp;
        this.mRsrq = s.mRsrq;
        this.mRssnr = s.mRssnr;
        this.mCqi = s.mCqi;
        this.mTimingAdvance = s.mTimingAdvance;
        this.mLevel = s.mLevel;
        this.mMiuiLevel = s.mMiuiLevel;
    }

    public CellSignalStrengthLte copy() {
        return new CellSignalStrengthLte(this);
    }

    public void setDefaultValues() {
        this.mSignalStrength = Integer.MAX_VALUE;
        this.mRssi = Integer.MAX_VALUE;
        this.mRsrp = Integer.MAX_VALUE;
        this.mRsrq = Integer.MAX_VALUE;
        this.mRssnr = Integer.MAX_VALUE;
        this.mCqi = Integer.MAX_VALUE;
        this.mTimingAdvance = Integer.MAX_VALUE;
        this.mLevel = 0;
        this.mMiuiLevel = 0;
    }

    public int getLevel() {
        return this.mLevel;
    }

    public void updateLevel(PersistableBundle cc, ServiceState ss) {
        int[] thresholds;
        boolean rsrpOnly;
        int rsrpIconLevel;
        this.mMiuiLevel = MiuiCellSignalStrengthLte.updateMiuiLevel(cc, ss, this.mRsrp, this.mRssi);
        if (cc == null) {
            thresholds = sThresholds;
            rsrpOnly = false;
        } else {
            rsrpOnly = cc.getBoolean(CarrierConfigManager.KEY_USE_ONLY_RSRP_FOR_LTE_SIGNAL_BAR_BOOL, false);
            thresholds = cc.getIntArray(CarrierConfigManager.KEY_LTE_RSRP_THRESHOLDS_INT_ARRAY);
            if (thresholds == null) {
                thresholds = sThresholds;
            }
        }
        int rsrpBoost = 0;
        if (ss != null) {
            rsrpBoost = ss.getLteEarfcnRsrpBoost();
        }
        int snrIconLevel = -1;
        int rsrp = this.mRsrp + rsrpBoost;
        if (rsrp < MIN_LTE_RSRP || rsrp > -44) {
            rsrpIconLevel = -1;
        } else {
            rsrpIconLevel = thresholds.length;
            while (rsrpIconLevel > 0 && rsrp < thresholds[rsrpIconLevel - 1]) {
                rsrpIconLevel--;
            }
        }
        if (!rsrpOnly || rsrpIconLevel == -1) {
            int i = this.mRssnr;
            if (i > 300) {
                snrIconLevel = -1;
            } else if (i >= 130) {
                snrIconLevel = 4;
            } else if (i >= 45) {
                snrIconLevel = 3;
            } else if (i >= 10) {
                snrIconLevel = 2;
            } else if (i >= -30) {
                snrIconLevel = 1;
            } else if (i >= -200) {
                snrIconLevel = 0;
            }
            if (snrIconLevel != -1 && rsrpIconLevel != -1) {
                this.mLevel = rsrpIconLevel < snrIconLevel ? rsrpIconLevel : snrIconLevel;
                return;
            } else if (snrIconLevel != -1) {
                this.mLevel = snrIconLevel;
                return;
            } else if (rsrpIconLevel != -1) {
                this.mLevel = rsrpIconLevel;
                return;
            } else {
                int rssiIconLevel;
                int i2 = this.mRssi;
                if (i2 > -51) {
                    rssiIconLevel = 0;
                } else if (i2 >= -89) {
                    rssiIconLevel = 4;
                } else if (i2 >= -97) {
                    rssiIconLevel = 3;
                } else if (i2 >= PackageManager.INSTALL_PARSE_FAILED_NO_CERTIFICATES) {
                    rssiIconLevel = 2;
                } else if (i2 >= -113) {
                    rssiIconLevel = 1;
                } else {
                    rssiIconLevel = 0;
                }
                this.mLevel = rssiIconLevel;
                return;
            }
        }
        this.mLevel = rsrpIconLevel;
    }

    public int getRsrq() {
        return this.mRsrq;
    }

    public int getRssi() {
        return this.mRssi;
    }

    public int getRssnr() {
        return this.mRssnr;
    }

    public int getRsrp() {
        return this.mRsrp;
    }

    public int getCqi() {
        return this.mCqi;
    }

    public int getDbm() {
        return this.mRsrp;
    }

    public int getAsuLevel() {
        int lteDbm = this.mRsrp;
        if (lteDbm == Integer.MAX_VALUE) {
            return 99;
        }
        if (lteDbm <= MIN_LTE_RSRP) {
            return 0;
        }
        if (lteDbm >= -43) {
            return 97;
        }
        return lteDbm + 140;
    }

    public int getTimingAdvance() {
        return this.mTimingAdvance;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mRssi), Integer.valueOf(this.mRsrp), Integer.valueOf(this.mRsrq), Integer.valueOf(this.mRssnr), Integer.valueOf(this.mCqi), Integer.valueOf(this.mTimingAdvance), Integer.valueOf(this.mLevel)});
    }

    public boolean isValid() {
        return equals(sInvalid) ^ 1;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof CellSignalStrengthLte)) {
            return false;
        }
        CellSignalStrengthLte s = (CellSignalStrengthLte) o;
        if (this.mRssi == s.mRssi && this.mRsrp == s.mRsrp && this.mRsrq == s.mRsrq && this.mRssnr == s.mRssnr && this.mCqi == s.mCqi && this.mTimingAdvance == s.mTimingAdvance && this.mLevel == s.mLevel) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CellSignalStrengthLte: rssi=");
        stringBuilder.append(this.mRssi);
        stringBuilder.append(" rsrp=");
        stringBuilder.append(this.mRsrp);
        stringBuilder.append(" rsrq=");
        stringBuilder.append(this.mRsrq);
        stringBuilder.append(" rssnr=");
        stringBuilder.append(this.mRssnr);
        stringBuilder.append(" cqi=");
        stringBuilder.append(this.mCqi);
        stringBuilder.append(" ta=");
        stringBuilder.append(this.mTimingAdvance);
        stringBuilder.append(" miuiLevel=");
        stringBuilder.append(this.mMiuiLevel);
        stringBuilder.append(" level=");
        stringBuilder.append(this.mLevel);
        return stringBuilder.toString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mRssi);
        dest.writeInt(this.mRsrp);
        dest.writeInt(this.mRsrq);
        dest.writeInt(this.mRssnr);
        dest.writeInt(this.mCqi);
        dest.writeInt(this.mTimingAdvance);
        dest.writeInt(this.mLevel);
        dest.writeInt(this.mMiuiLevel);
    }

    private CellSignalStrengthLte(Parcel in) {
        this.mRssi = in.readInt();
        this.mSignalStrength = this.mRssi;
        this.mRsrp = in.readInt();
        this.mRsrq = in.readInt();
        this.mRssnr = in.readInt();
        this.mCqi = in.readInt();
        this.mTimingAdvance = in.readInt();
        this.mLevel = in.readInt();
        this.mMiuiLevel = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }

    private static int convertRssiAsuToDBm(int rssiAsu) {
        if (rssiAsu == 99) {
            return Integer.MAX_VALUE;
        }
        if (rssiAsu >= 0 && rssiAsu <= 31) {
            return (rssiAsu * 2) - 113;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("convertRssiAsuToDBm: invalid RSSI in ASU=");
        stringBuilder.append(rssiAsu);
        Rlog.e(LOG_TAG, stringBuilder.toString());
        return Integer.MAX_VALUE;
    }
}
