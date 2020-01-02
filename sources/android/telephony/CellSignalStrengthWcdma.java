package android.telephony;

import android.annotation.UnsupportedAppUsage;
import android.hardware.radio.V1_0.WcdmaSignalStrength;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

public final class CellSignalStrengthWcdma extends CellSignalStrength implements Parcelable {
    public static final Creator<CellSignalStrengthWcdma> CREATOR = new Creator<CellSignalStrengthWcdma>() {
        public CellSignalStrengthWcdma createFromParcel(Parcel in) {
            return new CellSignalStrengthWcdma(in, null);
        }

        public CellSignalStrengthWcdma[] newArray(int size) {
            return new CellSignalStrengthWcdma[size];
        }
    };
    private static final boolean DBG = false;
    private static final String DEFAULT_LEVEL_CALCULATION_METHOD = "rssi";
    public static final String LEVEL_CALCULATION_METHOD_RSCP = "rscp";
    public static final String LEVEL_CALCULATION_METHOD_RSSI = "rssi";
    private static final String LOG_TAG = "CellSignalStrengthWcdma";
    private static final int WCDMA_RSCP_GOOD = -95;
    private static final int WCDMA_RSCP_GREAT = -85;
    private static final int WCDMA_RSCP_MAX = -24;
    private static final int WCDMA_RSCP_MIN = -120;
    private static final int WCDMA_RSCP_MODERATE = -105;
    private static final int WCDMA_RSCP_POOR = -115;
    private static final int WCDMA_RSSI_GOOD = -87;
    private static final int WCDMA_RSSI_GREAT = -77;
    private static final int WCDMA_RSSI_MAX = -51;
    private static final int WCDMA_RSSI_MIN = -113;
    private static final int WCDMA_RSSI_MODERATE = -97;
    private static final int WCDMA_RSSI_POOR = -107;
    private static final CellSignalStrengthWcdma sInvalid = new CellSignalStrengthWcdma();
    private static final int[] sRscpThresholds = new int[]{-115, -105, -95, WCDMA_RSCP_GREAT};
    private static final int[] sRssiThresholds = new int[]{-107, -97, WCDMA_RSSI_GOOD, WCDMA_RSSI_GREAT};
    @UnsupportedAppUsage
    private int mBitErrorRate;
    private int mEcNo;
    private int mLevel;
    private int mRscp;
    private int mRssi;

    @Retention(RetentionPolicy.SOURCE)
    public @interface LevelCalculationMethod {
    }

    /* synthetic */ CellSignalStrengthWcdma(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public CellSignalStrengthWcdma() {
        setDefaultValues();
    }

    public CellSignalStrengthWcdma(int rssi, int ber, int rscp, int ecno) {
        this.mRssi = CellSignalStrength.inRangeOrUnavailable(rssi, -113, -51);
        this.mBitErrorRate = CellSignalStrength.inRangeOrUnavailable(ber, 0, 7, 99);
        this.mRscp = CellSignalStrength.inRangeOrUnavailable(rscp, -120, -24);
        this.mEcNo = CellSignalStrength.inRangeOrUnavailable(ecno, -24, 1);
        updateLevel(null, null);
    }

    public CellSignalStrengthWcdma(WcdmaSignalStrength wcdma) {
        this(CellSignalStrength.getRssiDbmFromAsu(wcdma.signalStrength), wcdma.bitErrorRate, Integer.MAX_VALUE, Integer.MAX_VALUE);
        if (this.mRssi == Integer.MAX_VALUE && this.mRscp == Integer.MAX_VALUE) {
            setDefaultValues();
        }
    }

    public CellSignalStrengthWcdma(android.hardware.radio.V1_2.WcdmaSignalStrength wcdma) {
        this(CellSignalStrength.getRssiDbmFromAsu(wcdma.base.signalStrength), wcdma.base.bitErrorRate, CellSignalStrength.getRscpDbmFromAsu(wcdma.rscp), CellSignalStrength.getEcNoDbFromAsu(wcdma.ecno));
        if (this.mRssi == Integer.MAX_VALUE && this.mRscp == Integer.MAX_VALUE) {
            setDefaultValues();
        }
    }

    public CellSignalStrengthWcdma(CellSignalStrengthWcdma s) {
        copyFrom(s);
    }

    /* Access modifiers changed, original: protected */
    public void copyFrom(CellSignalStrengthWcdma s) {
        this.mRssi = s.mRssi;
        this.mBitErrorRate = s.mBitErrorRate;
        this.mRscp = s.mRscp;
        this.mEcNo = s.mEcNo;
        this.mLevel = s.mLevel;
        this.mMiuiLevel = s.mMiuiLevel;
    }

    public CellSignalStrengthWcdma copy() {
        return new CellSignalStrengthWcdma(this);
    }

    public void setDefaultValues() {
        this.mRssi = Integer.MAX_VALUE;
        this.mBitErrorRate = Integer.MAX_VALUE;
        this.mRscp = Integer.MAX_VALUE;
        this.mEcNo = Integer.MAX_VALUE;
        this.mLevel = 0;
        this.mMiuiLevel = 0;
    }

    public int getLevel() {
        return this.mLevel;
    }

    /* JADX WARNING: Removed duplicated region for block: B:37:0x0090  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x005a  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0090  */
    public void updateLevel(android.os.PersistableBundle r9, android.telephony.ServiceState r10) {
        /*
        r8 = this;
        r0 = r8.mRssi;
        r0 = android.telephony.MiuiCellSignalStrengthWcdma.updateLevel(r9, r10, r0);
        r8.mMiuiLevel = r0;
        r0 = "rssi";
        if (r9 != 0) goto L_0x0013;
    L_0x000d:
        r1 = "rssi";
        r2 = sRscpThresholds;
        goto L_0x0032;
    L_0x0013:
        r1 = "wcdma_default_signal_strength_measurement_string";
        r1 = r9.getString(r1, r0);
        r2 = android.text.TextUtils.isEmpty(r1);
        if (r2 == 0) goto L_0x0023;
    L_0x0020:
        r1 = "rssi";
    L_0x0023:
        r2 = "wcdma_rscp_thresholds_int_array";
        r2 = r9.getIntArray(r2);
        if (r2 == 0) goto L_0x0030;
    L_0x002c:
        r3 = r2.length;
        r4 = 4;
        if (r3 == r4) goto L_0x0032;
    L_0x0030:
        r2 = sRscpThresholds;
    L_0x0032:
        r3 = 4;
        r4 = r1.hashCode();
        r5 = 3509870; // 0x358e6e float:4.918375E-39 double:1.734106E-317;
        r6 = 2;
        r7 = 0;
        if (r4 == r5) goto L_0x004c;
    L_0x003e:
        r5 = 3510359; // 0x359057 float:4.91906E-39 double:1.734348E-317;
        if (r4 == r5) goto L_0x0044;
    L_0x0043:
        goto L_0x0057;
    L_0x0044:
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0043;
    L_0x004a:
        r0 = r6;
        goto L_0x0058;
    L_0x004c:
        r0 = "rscp";
        r0 = r1.equals(r0);
        if (r0 == 0) goto L_0x0043;
    L_0x0055:
        r0 = r7;
        goto L_0x0058;
    L_0x0057:
        r0 = -1;
    L_0x0058:
        if (r0 == 0) goto L_0x0090;
    L_0x005a:
        if (r0 == r6) goto L_0x0070;
    L_0x005c:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r4 = "Invalid Level Calculation Method for CellSignalStrengthWcdma = ";
        r0.append(r4);
        r0.append(r1);
        r0 = r0.toString();
        loge(r0);
    L_0x0070:
        r0 = r8.mRssi;
        r4 = -113; // 0xffffffffffffff8f float:NaN double:NaN;
        if (r0 < r4) goto L_0x008d;
    L_0x0076:
        r4 = -51;
        if (r0 <= r4) goto L_0x007b;
    L_0x007a:
        goto L_0x008d;
    L_0x007b:
        if (r3 <= 0) goto L_0x008a;
    L_0x007d:
        r0 = r8.mRssi;
        r4 = sRssiThresholds;
        r5 = r3 + -1;
        r4 = r4[r5];
        if (r0 >= r4) goto L_0x008a;
    L_0x0087:
        r3 = r3 + -1;
        goto L_0x007b;
    L_0x008a:
        r8.mLevel = r3;
        return;
    L_0x008d:
        r8.mLevel = r7;
        return;
    L_0x0090:
        r0 = r8.mRscp;
        r4 = -120; // 0xffffffffffffff88 float:NaN double:NaN;
        if (r0 < r4) goto L_0x00ab;
    L_0x0096:
        r4 = -24;
        if (r0 <= r4) goto L_0x009b;
    L_0x009a:
        goto L_0x00ab;
    L_0x009b:
        if (r3 <= 0) goto L_0x00a8;
    L_0x009d:
        r0 = r8.mRscp;
        r4 = r3 + -1;
        r4 = r2[r4];
        if (r0 >= r4) goto L_0x00a8;
    L_0x00a5:
        r3 = r3 + -1;
        goto L_0x009b;
    L_0x00a8:
        r8.mLevel = r3;
        return;
    L_0x00ab:
        r8.mLevel = r7;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.telephony.CellSignalStrengthWcdma.updateLevel(android.os.PersistableBundle, android.telephony.ServiceState):void");
    }

    public int getDbm() {
        int i = this.mRscp;
        if (i != Integer.MAX_VALUE) {
            return i;
        }
        return this.mRssi;
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

    public int getRssi() {
        return this.mRssi;
    }

    public int getRscp() {
        return this.mRscp;
    }

    public int getEcNo() {
        return this.mEcNo;
    }

    public int getBitErrorRate() {
        return this.mBitErrorRate;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mRssi), Integer.valueOf(this.mBitErrorRate), Integer.valueOf(this.mRscp), Integer.valueOf(this.mEcNo), Integer.valueOf(this.mLevel)});
    }

    public boolean isValid() {
        return equals(sInvalid) ^ 1;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof CellSignalStrengthWcdma)) {
            return false;
        }
        CellSignalStrengthWcdma s = (CellSignalStrengthWcdma) o;
        if (this.mRssi == s.mRssi && this.mBitErrorRate == s.mBitErrorRate && this.mRscp == s.mRscp && this.mEcNo == s.mEcNo && this.mLevel == s.mLevel) {
            z = true;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CellSignalStrengthWcdma: ss=");
        stringBuilder.append(this.mRssi);
        stringBuilder.append(" ber=");
        stringBuilder.append(this.mBitErrorRate);
        stringBuilder.append(" rscp=");
        stringBuilder.append(this.mRscp);
        stringBuilder.append(" ecno=");
        stringBuilder.append(this.mEcNo);
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
        dest.writeInt(this.mEcNo);
        dest.writeInt(this.mLevel);
        dest.writeInt(this.mMiuiLevel);
    }

    private CellSignalStrengthWcdma(Parcel in) {
        this.mRssi = in.readInt();
        this.mBitErrorRate = in.readInt();
        this.mRscp = in.readInt();
        this.mEcNo = in.readInt();
        this.mLevel = in.readInt();
        this.mMiuiLevel = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    private static void log(String s) {
        Rlog.w(LOG_TAG, s);
    }

    private static void loge(String s) {
        Rlog.e(LOG_TAG, s);
    }
}
