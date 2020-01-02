package android.telephony;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.CalendarContract.CalendarCache;
import android.telephony.NetworkRegistrationInfo.Builder;
import android.text.TextUtils;
import com.android.internal.telephony.IccCardConstants;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import miui.telephony.PhoneDebug;

public class ServiceState implements Parcelable {
    public static final Creator<ServiceState> CREATOR = new Creator<ServiceState>() {
        public ServiceState createFromParcel(Parcel in) {
            return new ServiceState(in);
        }

        public ServiceState[] newArray(int size) {
            return new ServiceState[size];
        }
    };
    static final boolean DBG = false;
    public static final int DUPLEX_MODE_FDD = 1;
    public static final int DUPLEX_MODE_TDD = 2;
    public static final int DUPLEX_MODE_UNKNOWN = 0;
    public static final int FREQUENCY_RANGE_HIGH = 3;
    public static final int FREQUENCY_RANGE_LOW = 1;
    public static final int FREQUENCY_RANGE_MID = 2;
    public static final int FREQUENCY_RANGE_MMWAVE = 4;
    private static final List<Integer> FREQUENCY_RANGE_ORDER = Arrays.asList(new Integer[]{Integer.valueOf(-1), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4)});
    public static final int FREQUENCY_RANGE_UNKNOWN = -1;
    static final String LOG_TAG = "PHONE";
    private static final int NEXT_RIL_RADIO_TECHNOLOGY = 21;
    public static final int RIL_RADIO_CDMA_TECHNOLOGY_BITMASK = 6392;
    public static final int RIL_RADIO_TECHNOLOGY_1xRTT = 6;
    public static final int RIL_RADIO_TECHNOLOGY_EDGE = 2;
    public static final int RIL_RADIO_TECHNOLOGY_EHRPD = 13;
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_0 = 7;
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_A = 8;
    public static final int RIL_RADIO_TECHNOLOGY_EVDO_B = 12;
    public static final int RIL_RADIO_TECHNOLOGY_GPRS = 1;
    public static final int RIL_RADIO_TECHNOLOGY_GSM = 16;
    public static final int RIL_RADIO_TECHNOLOGY_HSDPA = 9;
    public static final int RIL_RADIO_TECHNOLOGY_HSPA = 11;
    public static final int RIL_RADIO_TECHNOLOGY_HSPAP = 15;
    public static final int RIL_RADIO_TECHNOLOGY_HSUPA = 10;
    public static final int RIL_RADIO_TECHNOLOGY_IS95A = 4;
    public static final int RIL_RADIO_TECHNOLOGY_IS95B = 5;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    public static final int RIL_RADIO_TECHNOLOGY_IWLAN = 18;
    public static final int RIL_RADIO_TECHNOLOGY_LTE = 14;
    public static final int RIL_RADIO_TECHNOLOGY_LTE_CA = 19;
    public static final int RIL_RADIO_TECHNOLOGY_NR = 20;
    public static final int RIL_RADIO_TECHNOLOGY_TD_SCDMA = 17;
    public static final int RIL_RADIO_TECHNOLOGY_UMTS = 3;
    public static final int RIL_RADIO_TECHNOLOGY_UNKNOWN = 0;
    @SystemApi
    public static final int ROAMING_TYPE_DOMESTIC = 2;
    @SystemApi
    public static final int ROAMING_TYPE_INTERNATIONAL = 3;
    @SystemApi
    public static final int ROAMING_TYPE_NOT_ROAMING = 0;
    @SystemApi
    public static final int ROAMING_TYPE_UNKNOWN = 1;
    public static final int STATE_EMERGENCY_ONLY = 2;
    public static final int STATE_IN_SERVICE = 0;
    public static final int STATE_OUT_OF_SERVICE = 1;
    public static final int STATE_POWER_OFF = 3;
    public static final int UNKNOWN_ID = -1;
    static final boolean VDBG = false;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mCdmaDefaultRoamingIndicator;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mCdmaEriIconIndex;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mCdmaEriIconMode;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private int mCdmaRoamingIndicator;
    private int[] mCellBandwidths;
    private int mChannelNumber;
    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private boolean mCssIndicator;
    private String mDataOperatorAlphaLong;
    private String mDataOperatorAlphaShort;
    private String mDataOperatorNumeric;
    private int mDataRegState;
    private boolean mIsEmergencyOnly;
    private boolean mIsIwlanPreferred;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private boolean mIsManualNetworkSelection;
    private int mLteEarfcnRsrpBoost;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mNetworkId;
    private final List<NetworkRegistrationInfo> mNetworkRegistrationInfos;
    private int mNrFrequencyRange;
    private String mOperatorAlphaLongRaw;
    private String mOperatorAlphaShortRaw;
    @UnsupportedAppUsage(maxTargetSdk = 28)
    private int mSystemId;
    private String mVoiceOperatorAlphaLong;
    private String mVoiceOperatorAlphaShort;
    private String mVoiceOperatorNumeric;
    private int mVoiceRegState;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DuplexMode {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface FrequencyRange {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RilRadioTechnology {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RoamingType {
    }

    public static final String getRoamingLogString(int roamingType) {
        if (roamingType == 0) {
            return CalendarCache.TIMEZONE_TYPE_HOME;
        }
        if (roamingType == 1) {
            return "roaming";
        }
        if (roamingType == 2) {
            return "Domestic Roaming";
        }
        if (roamingType != 3) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        return "International Roaming";
    }

    @UnsupportedAppUsage
    public static ServiceState newFromBundle(Bundle m) {
        ServiceState ret = new ServiceState();
        ret.setFromNotifierBundle(m);
        return ret;
    }

    public ServiceState() {
        this.mVoiceRegState = 1;
        this.mDataRegState = 1;
        this.mCellBandwidths = new int[0];
        this.mLteEarfcnRsrpBoost = 0;
        this.mNetworkRegistrationInfos = new ArrayList();
    }

    public ServiceState(ServiceState s) {
        this.mVoiceRegState = 1;
        this.mDataRegState = 1;
        this.mCellBandwidths = new int[0];
        this.mLteEarfcnRsrpBoost = 0;
        this.mNetworkRegistrationInfos = new ArrayList();
        copyFrom(s);
    }

    /* Access modifiers changed, original: protected */
    public void copyFrom(ServiceState s) {
        this.mVoiceRegState = s.mVoiceRegState;
        this.mDataRegState = s.mDataRegState;
        this.mVoiceOperatorAlphaLong = s.mVoiceOperatorAlphaLong;
        this.mVoiceOperatorAlphaShort = s.mVoiceOperatorAlphaShort;
        this.mVoiceOperatorNumeric = s.mVoiceOperatorNumeric;
        this.mDataOperatorAlphaLong = s.mDataOperatorAlphaLong;
        this.mDataOperatorAlphaShort = s.mDataOperatorAlphaShort;
        this.mDataOperatorNumeric = s.mDataOperatorNumeric;
        this.mIsManualNetworkSelection = s.mIsManualNetworkSelection;
        this.mCssIndicator = s.mCssIndicator;
        this.mNetworkId = s.mNetworkId;
        this.mSystemId = s.mSystemId;
        this.mCdmaRoamingIndicator = s.mCdmaRoamingIndicator;
        this.mCdmaDefaultRoamingIndicator = s.mCdmaDefaultRoamingIndicator;
        this.mCdmaEriIconIndex = s.mCdmaEriIconIndex;
        this.mCdmaEriIconMode = s.mCdmaEriIconMode;
        this.mIsEmergencyOnly = s.mIsEmergencyOnly;
        this.mChannelNumber = s.mChannelNumber;
        int[] iArr = s.mCellBandwidths;
        if (iArr == null) {
            iArr = null;
        } else {
            iArr = Arrays.copyOf(iArr, iArr.length);
        }
        this.mCellBandwidths = iArr;
        this.mLteEarfcnRsrpBoost = s.mLteEarfcnRsrpBoost;
        synchronized (this.mNetworkRegistrationInfos) {
            this.mNetworkRegistrationInfos.clear();
            this.mNetworkRegistrationInfos.addAll(s.getNetworkRegistrationInfoList());
        }
        this.mNrFrequencyRange = s.mNrFrequencyRange;
        this.mOperatorAlphaLongRaw = s.mOperatorAlphaLongRaw;
        this.mOperatorAlphaShortRaw = s.mOperatorAlphaShortRaw;
        this.mIsIwlanPreferred = s.mIsIwlanPreferred;
    }

    @Deprecated
    public ServiceState(Parcel in) {
        boolean z = true;
        this.mVoiceRegState = 1;
        this.mDataRegState = 1;
        this.mCellBandwidths = new int[0];
        this.mLteEarfcnRsrpBoost = 0;
        this.mNetworkRegistrationInfos = new ArrayList();
        this.mVoiceRegState = in.readInt();
        this.mDataRegState = in.readInt();
        this.mVoiceOperatorAlphaLong = in.readString();
        this.mVoiceOperatorAlphaShort = in.readString();
        this.mVoiceOperatorNumeric = in.readString();
        this.mDataOperatorAlphaLong = in.readString();
        this.mDataOperatorAlphaShort = in.readString();
        this.mDataOperatorNumeric = in.readString();
        this.mIsManualNetworkSelection = in.readInt() != 0;
        this.mCssIndicator = in.readInt() != 0;
        this.mNetworkId = in.readInt();
        this.mSystemId = in.readInt();
        this.mCdmaRoamingIndicator = in.readInt();
        this.mCdmaDefaultRoamingIndicator = in.readInt();
        this.mCdmaEriIconIndex = in.readInt();
        this.mCdmaEriIconMode = in.readInt();
        if (in.readInt() == 0) {
            z = false;
        }
        this.mIsEmergencyOnly = z;
        this.mLteEarfcnRsrpBoost = in.readInt();
        synchronized (this.mNetworkRegistrationInfos) {
            in.readList(this.mNetworkRegistrationInfos, NetworkRegistrationInfo.class.getClassLoader());
        }
        this.mChannelNumber = in.readInt();
        this.mCellBandwidths = in.createIntArray();
        this.mNrFrequencyRange = in.readInt();
        this.mOperatorAlphaLongRaw = in.readString();
        this.mOperatorAlphaShortRaw = in.readString();
        this.mIsIwlanPreferred = in.readBoolean();
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mVoiceRegState);
        out.writeInt(this.mDataRegState);
        out.writeString(this.mVoiceOperatorAlphaLong);
        out.writeString(this.mVoiceOperatorAlphaShort);
        out.writeString(this.mVoiceOperatorNumeric);
        out.writeString(this.mDataOperatorAlphaLong);
        out.writeString(this.mDataOperatorAlphaShort);
        out.writeString(this.mDataOperatorNumeric);
        out.writeInt(this.mIsManualNetworkSelection);
        out.writeInt(this.mCssIndicator);
        out.writeInt(this.mNetworkId);
        out.writeInt(this.mSystemId);
        out.writeInt(this.mCdmaRoamingIndicator);
        out.writeInt(this.mCdmaDefaultRoamingIndicator);
        out.writeInt(this.mCdmaEriIconIndex);
        out.writeInt(this.mCdmaEriIconMode);
        out.writeInt(this.mIsEmergencyOnly);
        out.writeInt(this.mLteEarfcnRsrpBoost);
        synchronized (this.mNetworkRegistrationInfos) {
            out.writeList(this.mNetworkRegistrationInfos);
        }
        out.writeInt(this.mChannelNumber);
        out.writeIntArray(this.mCellBandwidths);
        out.writeInt(this.mNrFrequencyRange);
        out.writeString(this.mOperatorAlphaLongRaw);
        out.writeString(this.mOperatorAlphaShortRaw);
        out.writeBoolean(this.mIsIwlanPreferred);
    }

    public int describeContents() {
        return 0;
    }

    public int getState() {
        return getVoiceRegState();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getVoiceRegState() {
        return this.mVoiceRegState;
    }

    @UnsupportedAppUsage
    public int getDataRegState() {
        return this.mDataRegState;
    }

    public int getDuplexMode() {
        if (isLte(getRilDataRadioTechnology())) {
            return AccessNetworkUtils.getDuplexModeForEutranBand(AccessNetworkUtils.getOperatingBandForEarfcn(this.mChannelNumber));
        }
        return 0;
    }

    public int getChannelNumber() {
        return this.mChannelNumber;
    }

    public int[] getCellBandwidths() {
        int[] iArr = this.mCellBandwidths;
        return iArr == null ? new int[0] : iArr;
    }

    public boolean getRoaming() {
        return getVoiceRoaming() || getDataRoaming();
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean getVoiceRoaming() {
        return getVoiceRoamingType() != 0;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getVoiceRoamingType() {
        NetworkRegistrationInfo regState = getNetworkRegistrationInfo(1, 1);
        if (regState != null) {
            return regState.getRoamingType();
        }
        return 0;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public boolean getDataRoaming() {
        return getDataRoamingType() != 0;
    }

    public boolean getDataRoamingFromRegistration() {
        boolean z = true;
        NetworkRegistrationInfo regState = getNetworkRegistrationInfo(2, 1);
        if (regState == null) {
            return false;
        }
        if (regState.getRegistrationState() != 5) {
            z = false;
        }
        return z;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getDataRoamingType() {
        NetworkRegistrationInfo regState = getNetworkRegistrationInfo(2, 1);
        if (regState != null) {
            return regState.getRoamingType();
        }
        return 0;
    }

    @UnsupportedAppUsage
    public boolean isEmergencyOnly() {
        return this.mIsEmergencyOnly;
    }

    @UnsupportedAppUsage
    public int getCdmaRoamingIndicator() {
        return this.mCdmaRoamingIndicator;
    }

    @UnsupportedAppUsage
    public int getCdmaDefaultRoamingIndicator() {
        return this.mCdmaDefaultRoamingIndicator;
    }

    @UnsupportedAppUsage
    public int getCdmaEriIconIndex() {
        return this.mCdmaEriIconIndex;
    }

    @UnsupportedAppUsage
    public int getCdmaEriIconMode() {
        return this.mCdmaEriIconMode;
    }

    public String getOperatorAlphaLong() {
        return this.mVoiceOperatorAlphaLong;
    }

    @UnsupportedAppUsage
    public String getVoiceOperatorAlphaLong() {
        return this.mVoiceOperatorAlphaLong;
    }

    public String getDataOperatorAlphaLong() {
        return this.mDataOperatorAlphaLong;
    }

    public String getOperatorAlphaShort() {
        return this.mVoiceOperatorAlphaShort;
    }

    @UnsupportedAppUsage
    public String getVoiceOperatorAlphaShort() {
        return this.mVoiceOperatorAlphaShort;
    }

    @UnsupportedAppUsage
    public String getDataOperatorAlphaShort() {
        return this.mDataOperatorAlphaShort;
    }

    public String getOperatorAlpha() {
        if (TextUtils.isEmpty(this.mVoiceOperatorAlphaLong)) {
            return this.mVoiceOperatorAlphaShort;
        }
        return this.mVoiceOperatorAlphaLong;
    }

    public String getOperatorNumeric() {
        return this.mVoiceOperatorNumeric;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public String getVoiceOperatorNumeric() {
        return this.mVoiceOperatorNumeric;
    }

    @UnsupportedAppUsage
    public String getDataOperatorNumeric() {
        return this.mDataOperatorNumeric;
    }

    public boolean getIsManualSelection() {
        return this.mIsManualNetworkSelection;
    }

    public int hashCode() {
        int hash;
        synchronized (this.mNetworkRegistrationInfos) {
            hash = Objects.hash(new Object[]{Integer.valueOf(this.mVoiceRegState), Integer.valueOf(this.mDataRegState), Integer.valueOf(this.mChannelNumber), Integer.valueOf(Arrays.hashCode(this.mCellBandwidths)), this.mVoiceOperatorAlphaLong, this.mVoiceOperatorAlphaShort, this.mVoiceOperatorNumeric, this.mDataOperatorAlphaLong, this.mDataOperatorAlphaShort, this.mDataOperatorNumeric, Boolean.valueOf(this.mIsManualNetworkSelection), Boolean.valueOf(this.mCssIndicator), Integer.valueOf(this.mNetworkId), Integer.valueOf(this.mSystemId), Integer.valueOf(this.mCdmaRoamingIndicator), Integer.valueOf(this.mCdmaDefaultRoamingIndicator), Integer.valueOf(this.mCdmaEriIconIndex), Integer.valueOf(this.mCdmaEriIconMode), Boolean.valueOf(this.mIsEmergencyOnly), Integer.valueOf(this.mLteEarfcnRsrpBoost), this.mNetworkRegistrationInfos, Integer.valueOf(this.mNrFrequencyRange), this.mOperatorAlphaLongRaw, this.mOperatorAlphaShortRaw, Boolean.valueOf(this.mIsIwlanPreferred)});
        }
        return hash;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof ServiceState)) {
            return false;
        }
        ServiceState s = (ServiceState) o;
        synchronized (this.mNetworkRegistrationInfos) {
            if (this.mVoiceRegState == s.mVoiceRegState && this.mDataRegState == s.mDataRegState && this.mIsManualNetworkSelection == s.mIsManualNetworkSelection && this.mChannelNumber == s.mChannelNumber && Arrays.equals(this.mCellBandwidths, s.mCellBandwidths) && equalsHandlesNulls(this.mVoiceOperatorAlphaLong, s.mVoiceOperatorAlphaLong) && equalsHandlesNulls(this.mVoiceOperatorAlphaShort, s.mVoiceOperatorAlphaShort) && equalsHandlesNulls(this.mVoiceOperatorNumeric, s.mVoiceOperatorNumeric) && equalsHandlesNulls(this.mDataOperatorAlphaLong, s.mDataOperatorAlphaLong) && equalsHandlesNulls(this.mDataOperatorAlphaShort, s.mDataOperatorAlphaShort) && equalsHandlesNulls(this.mDataOperatorNumeric, s.mDataOperatorNumeric) && equalsHandlesNulls(Boolean.valueOf(this.mCssIndicator), Boolean.valueOf(s.mCssIndicator)) && equalsHandlesNulls(Integer.valueOf(this.mNetworkId), Integer.valueOf(s.mNetworkId)) && equalsHandlesNulls(Integer.valueOf(this.mSystemId), Integer.valueOf(s.mSystemId)) && equalsHandlesNulls(Integer.valueOf(this.mCdmaRoamingIndicator), Integer.valueOf(s.mCdmaRoamingIndicator)) && equalsHandlesNulls(Integer.valueOf(this.mCdmaDefaultRoamingIndicator), Integer.valueOf(s.mCdmaDefaultRoamingIndicator)) && this.mIsEmergencyOnly == s.mIsEmergencyOnly && equalsHandlesNulls(this.mOperatorAlphaLongRaw, s.mOperatorAlphaLongRaw) && equalsHandlesNulls(this.mOperatorAlphaShortRaw, s.mOperatorAlphaShortRaw) && this.mNetworkRegistrationInfos.size() == s.mNetworkRegistrationInfos.size() && this.mNetworkRegistrationInfos.containsAll(s.mNetworkRegistrationInfos) && this.mNrFrequencyRange == s.mNrFrequencyRange && this.mIsIwlanPreferred == s.mIsIwlanPreferred) {
                z = true;
            }
        }
        return z;
    }

    public static String roamingTypeToString(int roamingType) {
        if (roamingType == 0) {
            return "NOT_ROAMING";
        }
        if (roamingType == 1) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        if (roamingType == 2) {
            return "DOMESTIC";
        }
        if (roamingType == 3) {
            return "INTERNATIONAL";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown roaming type ");
        stringBuilder.append(roamingType);
        return stringBuilder.toString();
    }

    @UnsupportedAppUsage
    public static String rilRadioTechnologyToString(int rt) {
        switch (rt) {
            case 0:
                return "Unknown";
            case 1:
                return "GPRS";
            case 2:
                return "EDGE";
            case 3:
                return "UMTS";
            case 4:
                return "CDMA-IS95A";
            case 5:
                return "CDMA-IS95B";
            case 6:
                return "1xRTT";
            case 7:
                return "EvDo-rev.0";
            case 8:
                return "EvDo-rev.A";
            case 9:
                return "HSDPA";
            case 10:
                return "HSUPA";
            case 11:
                return "HSPA";
            case 12:
                return "EvDo-rev.B";
            case 13:
                return "eHRPD";
            case 14:
                return "LTE";
            case 15:
                return "HSPAP";
            case 16:
                return "GSM";
            case 17:
                return "TD-SCDMA";
            case 18:
                return "IWLAN";
            case 19:
                return "LTE_CA";
            case 20:
                return "NR";
            default:
                String rtString = "Unexpected";
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unexpected radioTechnology=");
                stringBuilder.append(rt);
                Rlog.w(LOG_TAG, stringBuilder.toString());
                return rtString;
        }
    }

    public static String rilServiceStateToString(int serviceState) {
        if (serviceState == 0) {
            return "IN_SERVICE";
        }
        if (serviceState == 1) {
            return "OUT_OF_SERVICE";
        }
        if (serviceState == 2) {
            return "EMERGENCY_ONLY";
        }
        if (serviceState != 3) {
            return IccCardConstants.INTENT_VALUE_ICC_UNKNOWN;
        }
        return "POWER_OFF";
    }

    public String toString() {
        String stringBuilder;
        synchronized (this.mNetworkRegistrationInfos) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("{mVoiceRegState=");
            stringBuilder2.append(this.mVoiceRegState);
            StringBuilder stringBuilder3 = new StringBuilder();
            stringBuilder3.append("(");
            stringBuilder3.append(rilServiceStateToString(this.mVoiceRegState));
            stringBuilder3.append(")");
            stringBuilder2.append(stringBuilder3.toString());
            stringBuilder2.append(", mDataRegState=");
            stringBuilder2.append(this.mDataRegState);
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append("(");
            stringBuilder3.append(rilServiceStateToString(this.mDataRegState));
            stringBuilder3.append(")");
            stringBuilder2.append(stringBuilder3.toString());
            stringBuilder2.append(", mChannelNumber=");
            stringBuilder2.append(this.mChannelNumber);
            stringBuilder2.append(", duplexMode()=");
            stringBuilder2.append(getDuplexMode());
            stringBuilder2.append(", mCellBandwidths=");
            stringBuilder2.append(Arrays.toString(this.mCellBandwidths));
            stringBuilder2.append(", mVoiceOperatorAlphaLong=");
            stringBuilder2.append(this.mVoiceOperatorAlphaLong);
            stringBuilder2.append(", mVoiceOperatorAlphaShort=");
            stringBuilder2.append(this.mVoiceOperatorAlphaShort);
            stringBuilder2.append(", mDataOperatorAlphaLong=");
            stringBuilder2.append(this.mDataOperatorAlphaLong);
            stringBuilder2.append(", mDataOperatorAlphaShort=");
            stringBuilder2.append(this.mDataOperatorAlphaShort);
            stringBuilder2.append(", isManualNetworkSelection=");
            stringBuilder2.append(this.mIsManualNetworkSelection);
            stringBuilder2.append(this.mIsManualNetworkSelection ? "(manual)" : "(automatic)");
            stringBuilder2.append(", getRilVoiceRadioTechnology=");
            stringBuilder2.append(getRilVoiceRadioTechnology());
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append("(");
            stringBuilder3.append(rilRadioTechnologyToString(getRilVoiceRadioTechnology()));
            stringBuilder3.append(")");
            stringBuilder2.append(stringBuilder3.toString());
            stringBuilder2.append(", getRilDataRadioTechnology=");
            stringBuilder2.append(getRilDataRadioTechnology());
            stringBuilder3 = new StringBuilder();
            stringBuilder3.append("(");
            stringBuilder3.append(rilRadioTechnologyToString(getRilDataRadioTechnology()));
            stringBuilder3.append(")");
            stringBuilder2.append(stringBuilder3.toString());
            stringBuilder2.append(", mCssIndicator=");
            stringBuilder2.append(this.mCssIndicator ? "supported" : "unsupported");
            stringBuilder2.append(", mNetworkId=");
            int i = -1;
            stringBuilder2.append(PhoneDebug.VDBG ? this.mNetworkId : -1);
            stringBuilder2.append(", mSystemId=");
            if (PhoneDebug.VDBG) {
                i = this.mSystemId;
            }
            stringBuilder2.append(i);
            stringBuilder2.append(", mCdmaRoamingIndicator=");
            stringBuilder2.append(this.mCdmaRoamingIndicator);
            stringBuilder2.append(", mCdmaDefaultRoamingIndicator=");
            stringBuilder2.append(this.mCdmaDefaultRoamingIndicator);
            stringBuilder2.append(", mIsEmergencyOnly=");
            stringBuilder2.append(this.mIsEmergencyOnly);
            stringBuilder2.append(", isUsingCarrierAggregation=");
            stringBuilder2.append(isUsingCarrierAggregation());
            stringBuilder2.append(", mLteEarfcnRsrpBoost=");
            stringBuilder2.append(this.mLteEarfcnRsrpBoost);
            stringBuilder2.append(", mNetworkRegistrationInfos=");
            stringBuilder2.append(this.mNetworkRegistrationInfos);
            stringBuilder2.append(", mNrFrequencyRange=");
            stringBuilder2.append(this.mNrFrequencyRange);
            stringBuilder2.append(", mOperatorAlphaLongRaw=");
            stringBuilder2.append(this.mOperatorAlphaLongRaw);
            stringBuilder2.append(", mOperatorAlphaShortRaw=");
            stringBuilder2.append(this.mOperatorAlphaShortRaw);
            stringBuilder2.append(", mIsIwlanPreferred=");
            stringBuilder2.append(this.mIsIwlanPreferred);
            stringBuilder2.append("}");
            stringBuilder = stringBuilder2.toString();
        }
        return stringBuilder;
    }

    private void init() {
        this.mVoiceRegState = 1;
        this.mDataRegState = 1;
        this.mChannelNumber = -1;
        this.mCellBandwidths = new int[0];
        this.mVoiceOperatorAlphaLong = null;
        this.mVoiceOperatorAlphaShort = null;
        this.mVoiceOperatorNumeric = null;
        this.mDataOperatorAlphaLong = null;
        this.mDataOperatorAlphaShort = null;
        this.mDataOperatorNumeric = null;
        this.mIsManualNetworkSelection = false;
        this.mCssIndicator = false;
        this.mNetworkId = -1;
        this.mSystemId = -1;
        this.mCdmaRoamingIndicator = -1;
        this.mCdmaDefaultRoamingIndicator = -1;
        this.mCdmaEriIconIndex = -1;
        this.mCdmaEriIconMode = -1;
        this.mIsEmergencyOnly = false;
        this.mLteEarfcnRsrpBoost = 0;
        this.mNrFrequencyRange = -1;
        synchronized (this.mNetworkRegistrationInfos) {
            this.mNetworkRegistrationInfos.clear();
            addNetworkRegistrationInfo(new Builder().setDomain(1).setTransportType(1).setRegistrationState(4).build());
            addNetworkRegistrationInfo(new Builder().setDomain(2).setTransportType(1).setRegistrationState(4).build());
        }
        this.mOperatorAlphaLongRaw = null;
        this.mOperatorAlphaShortRaw = null;
        this.mIsIwlanPreferred = false;
    }

    public void setStateOutOfService() {
        init();
    }

    public void setStateOff() {
        init();
        this.mVoiceRegState = 3;
        this.mDataRegState = 3;
    }

    public void setState(int state) {
        setVoiceRegState(state);
    }

    @UnsupportedAppUsage
    public void setVoiceRegState(int state) {
        this.mVoiceRegState = state;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setDataRegState(int state) {
        this.mDataRegState = state;
    }

    public void setCellBandwidths(int[] bandwidths) {
        this.mCellBandwidths = bandwidths;
    }

    public void setChannelNumber(int channelNumber) {
        this.mChannelNumber = channelNumber;
    }

    public void setRoaming(boolean roaming) {
        setVoiceRoaming(roaming);
        setDataRoaming(roaming);
    }

    @UnsupportedAppUsage
    public void setVoiceRoaming(boolean roaming) {
        setVoiceRoamingType(roaming);
    }

    public void setVoiceRoamingType(int type) {
        NetworkRegistrationInfo regInfo = getNetworkRegistrationInfo(1, 1);
        if (regInfo == null) {
            regInfo = new Builder().setDomain(1).setTransportType(1).build();
        }
        regInfo.setRoamingType(type);
        addNetworkRegistrationInfo(regInfo);
    }

    @UnsupportedAppUsage
    public void setDataRoaming(boolean dataRoaming) {
        setDataRoamingType(dataRoaming);
    }

    public void setDataRoamingType(int type) {
        NetworkRegistrationInfo regInfo = getNetworkRegistrationInfo(2, 1);
        if (regInfo == null) {
            regInfo = new Builder().setDomain(2).setTransportType(1).build();
        }
        regInfo.setRoamingType(type);
        addNetworkRegistrationInfo(regInfo);
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setEmergencyOnly(boolean emergencyOnly) {
        this.mIsEmergencyOnly = emergencyOnly;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setCdmaRoamingIndicator(int roaming) {
        this.mCdmaRoamingIndicator = roaming;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setCdmaDefaultRoamingIndicator(int roaming) {
        this.mCdmaDefaultRoamingIndicator = roaming;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setCdmaEriIconIndex(int index) {
        this.mCdmaEriIconIndex = index;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setCdmaEriIconMode(int mode) {
        this.mCdmaEriIconMode = mode;
    }

    public void setOperatorName(String longName, String shortName, String numeric) {
        this.mVoiceOperatorAlphaLong = longName;
        this.mVoiceOperatorAlphaShort = shortName;
        this.mVoiceOperatorNumeric = numeric;
        this.mDataOperatorAlphaLong = longName;
        this.mDataOperatorAlphaShort = shortName;
        this.mDataOperatorNumeric = numeric;
    }

    public void setVoiceOperatorName(String longName, String shortName, String numeric) {
        this.mVoiceOperatorAlphaLong = longName;
        this.mVoiceOperatorAlphaShort = shortName;
        this.mVoiceOperatorNumeric = numeric;
    }

    public void setDataOperatorName(String longName, String shortName, String numeric) {
        this.mDataOperatorAlphaLong = longName;
        this.mDataOperatorAlphaShort = shortName;
        this.mDataOperatorNumeric = numeric;
    }

    @UnsupportedAppUsage
    public void setOperatorAlphaLong(String longName) {
        this.mVoiceOperatorAlphaLong = longName;
        this.mDataOperatorAlphaLong = longName;
    }

    public void setVoiceOperatorAlphaLong(String longName) {
        this.mVoiceOperatorAlphaLong = longName;
    }

    public void setDataOperatorAlphaLong(String longName) {
        this.mDataOperatorAlphaLong = longName;
    }

    public void setIsManualSelection(boolean isManual) {
        this.mIsManualNetworkSelection = isManual;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    private static boolean equalsHandlesNulls(Object a, Object b) {
        if (a == null) {
            return b == null;
        } else {
            return a.equals(b);
        }
    }

    @UnsupportedAppUsage
    private void setFromNotifierBundle(Bundle m) {
        ServiceState ssFromBundle = (ServiceState) m.getParcelable(Intent.EXTRA_SERVICE_STATE);
        if (ssFromBundle != null) {
            copyFrom(ssFromBundle);
        }
    }

    @UnsupportedAppUsage
    public void fillInNotifierBundle(Bundle m) {
        m.putParcelable(Intent.EXTRA_SERVICE_STATE, this);
        m.putInt(Intent.EXTRA_VOICE_REG_STATE, this.mVoiceRegState);
        m.putInt(Intent.EXTRA_DATA_REG_STATE, this.mDataRegState);
        m.putInt(Intent.EXTRA_DATA_ROAMING_TYPE, getDataRoamingType());
        m.putInt(Intent.EXTRA_VOICE_ROAMING_TYPE, getVoiceRoamingType());
        m.putString(Intent.EXTRA_OPERATOR_ALPHA_LONG, this.mVoiceOperatorAlphaLong);
        m.putString(Intent.EXTRA_OPERATOR_ALPHA_SHORT, this.mVoiceOperatorAlphaShort);
        m.putString(Intent.EXTRA_OPERATOR_NUMERIC, this.mVoiceOperatorNumeric);
        m.putString(Intent.EXTRA_DATA_OPERATOR_ALPHA_LONG, this.mDataOperatorAlphaLong);
        m.putString(Intent.EXTRA_DATA_OPERATOR_ALPHA_SHORT, this.mDataOperatorAlphaShort);
        m.putString(Intent.EXTRA_DATA_OPERATOR_NUMERIC, this.mDataOperatorNumeric);
        m.putBoolean("manual", this.mIsManualNetworkSelection);
        m.putInt(Intent.EXTRA_VOICE_RADIO_TECH, getRilVoiceRadioTechnology());
        m.putInt(Intent.EXTRA_DATA_RADIO_TECH, getRadioTechnology());
        m.putBoolean(Intent.EXTRA_CSS_INDICATOR, this.mCssIndicator);
        m.putInt(Intent.EXTRA_NETWORK_ID, this.mNetworkId);
        m.putInt(Intent.EXTRA_SYSTEM_ID, this.mSystemId);
        m.putInt(Intent.EXTRA_CDMA_ROAMING_INDICATOR, this.mCdmaRoamingIndicator);
        m.putInt(Intent.EXTRA_CDMA_DEFAULT_ROAMING_INDICATOR, this.mCdmaDefaultRoamingIndicator);
        m.putBoolean(Intent.EXTRA_EMERGENCY_ONLY, this.mIsEmergencyOnly);
        m.putBoolean(Intent.EXTRA_IS_DATA_ROAMING_FROM_REGISTRATION, getDataRoamingFromRegistration());
        m.putBoolean(Intent.EXTRA_IS_USING_CARRIER_AGGREGATION, isUsingCarrierAggregation());
        m.putInt(Intent.EXTRA_LTE_EARFCN_RSRP_BOOST, this.mLteEarfcnRsrpBoost);
        m.putInt("ChannelNumber", this.mChannelNumber);
        m.putIntArray("CellBandwidths", this.mCellBandwidths);
        m.putInt("mNrFrequencyRange", this.mNrFrequencyRange);
        m.putString("operator-alpha-long-raw", this.mOperatorAlphaLongRaw);
        m.putString("operator-alpha-short-raw", this.mOperatorAlphaShortRaw);
    }

    public void setRilVoiceRadioTechnology(int rt) {
        Rlog.e(LOG_TAG, "ServiceState.setRilVoiceRadioTechnology() called. It's encouraged to use addNetworkRegistrationInfo() instead *******");
        NetworkRegistrationInfo regInfo = getNetworkRegistrationInfo(1, 1);
        if (regInfo == null) {
            regInfo = new Builder().setDomain(1).setTransportType(1).build();
        }
        regInfo.setAccessNetworkTechnology(rilRadioTechnologyToNetworkType(rt));
        addNetworkRegistrationInfo(regInfo);
    }

    public void setRilDataRadioTechnology(int rt) {
        Rlog.e(LOG_TAG, "ServiceState.setRilDataRadioTechnology() called. It's encouraged to use addNetworkRegistrationInfo() instead *******");
        NetworkRegistrationInfo regInfo = getNetworkRegistrationInfo(2, 1);
        if (regInfo == null) {
            regInfo = new Builder().setDomain(2).setTransportType(1).build();
        }
        regInfo.setAccessNetworkTechnology(rilRadioTechnologyToNetworkType(rt));
        addNetworkRegistrationInfo(regInfo);
    }

    public boolean isUsingCarrierAggregation() {
        NetworkRegistrationInfo nri = getNetworkRegistrationInfo(2, 1);
        if (nri != null) {
            DataSpecificRegistrationInfo dsri = nri.getDataSpecificInfo();
            if (dsri != null) {
                return dsri.isUsingCarrierAggregation();
            }
        }
        return false;
    }

    public void setIsUsingCarrierAggregation(boolean ca) {
        NetworkRegistrationInfo nri = getNetworkRegistrationInfo(2, 1);
        if (nri != null) {
            DataSpecificRegistrationInfo dsri = nri.getDataSpecificInfo();
            if (dsri != null) {
                dsri.setIsUsingCarrierAggregation(ca);
            }
        }
    }

    public int getNrFrequencyRange() {
        return this.mNrFrequencyRange;
    }

    public int getNrState() {
        NetworkRegistrationInfo regInfo = getNetworkRegistrationInfo(2, 1);
        if (regInfo == null) {
            return -1;
        }
        return regInfo.getNrState();
    }

    public void setNrFrequencyRange(int nrFrequencyRange) {
        this.mNrFrequencyRange = nrFrequencyRange;
    }

    public int getLteEarfcnRsrpBoost() {
        return this.mLteEarfcnRsrpBoost;
    }

    public void setLteEarfcnRsrpBoost(int LteEarfcnRsrpBoost) {
        this.mLteEarfcnRsrpBoost = LteEarfcnRsrpBoost;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public void setCssIndicator(int css) {
        this.mCssIndicator = css != 0;
    }

    public void setCdmaSystemAndNetworkId(int systemId, int networkId) {
        this.mSystemId = systemId;
        this.mNetworkId = networkId;
    }

    @UnsupportedAppUsage
    public int getRilVoiceRadioTechnology() {
        NetworkRegistrationInfo wwanRegInfo = getNetworkRegistrationInfo(1, 1);
        if (wwanRegInfo != null) {
            return networkTypeToRilRadioTechnology(wwanRegInfo.getAccessNetworkTechnology());
        }
        return 0;
    }

    @UnsupportedAppUsage
    public int getRilDataRadioTechnology() {
        return networkTypeToRilRadioTechnology(getDataNetworkType());
    }

    @UnsupportedAppUsage
    public int getRadioTechnology() {
        Rlog.e(LOG_TAG, "ServiceState.getRadioTechnology() DEPRECATED will be removed *******");
        return getRilDataRadioTechnology();
    }

    public static int rilRadioTechnologyToNetworkType(int rat) {
        switch (rat) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
            case 5:
                return 4;
            case 6:
                return 7;
            case 7:
                return 5;
            case 8:
                return 6;
            case 9:
                return 8;
            case 10:
                return 9;
            case 11:
                return 10;
            case 12:
                return 12;
            case 13:
                return 14;
            case 14:
                return 13;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            default:
                return 0;
        }
    }

    public static int rilRadioTechnologyToAccessNetworkType(int rt) {
        switch (rt) {
            case 1:
            case 2:
            case 16:
                return 1;
            case 3:
            case 9:
            case 10:
            case 11:
            case 15:
            case 17:
                return 2;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 12:
            case 13:
                return 4;
            case 14:
            case 19:
            case 20:
                return 3;
            case 18:
                return 5;
            default:
                return 0;
        }
    }

    public static int networkTypeToRilRadioTechnology(int networkType) {
        switch (networkType) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 3;
            case 4:
                return 4;
            case 5:
                return 7;
            case 6:
                return 8;
            case 7:
                return 6;
            case 8:
                return 9;
            case 9:
                return 10;
            case 10:
                return 11;
            case 12:
                return 12;
            case 13:
                return 14;
            case 14:
                return 13;
            case 15:
                return 15;
            case 16:
                return 16;
            case 17:
                return 17;
            case 18:
                return 18;
            case 19:
                return 19;
            case 20:
                return 20;
            default:
                return 0;
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getDataNetworkType() {
        NetworkRegistrationInfo iwlanRegInfo = getNetworkRegistrationInfo(2, 2);
        NetworkRegistrationInfo wwanRegInfo = getNetworkRegistrationInfo(2, 1);
        if (iwlanRegInfo == null || !iwlanRegInfo.isInService()) {
            int accessNetworkTechnology;
            if (wwanRegInfo != null) {
                accessNetworkTechnology = wwanRegInfo.getAccessNetworkTechnology();
            } else {
                accessNetworkTechnology = 0;
            }
            return accessNetworkTechnology;
        } else if (!wwanRegInfo.isInService() || this.mIsIwlanPreferred) {
            return iwlanRegInfo.getAccessNetworkTechnology();
        } else {
            return wwanRegInfo.getAccessNetworkTechnology();
        }
    }

    @UnsupportedAppUsage(maxTargetSdk = 28)
    public int getVoiceNetworkType() {
        NetworkRegistrationInfo regState = getNetworkRegistrationInfo(1, 1);
        if (regState != null) {
            return regState.getAccessNetworkTechnology();
        }
        return 0;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public int getCssIndicator() {
        return this.mCssIndicator;
    }

    public int getCdmaNetworkId() {
        return this.mNetworkId;
    }

    public int getCdmaSystemId() {
        return this.mSystemId;
    }

    @UnsupportedAppUsage
    public static boolean isGsm(int radioTechnology) {
        return radioTechnology == 1 || radioTechnology == 2 || radioTechnology == 3 || radioTechnology == 9 || radioTechnology == 10 || radioTechnology == 11 || radioTechnology == 14 || radioTechnology == 15 || radioTechnology == 20 || radioTechnology == 16 || radioTechnology == 17 || radioTechnology == 18 || radioTechnology == 19 || radioTechnology == 20;
    }

    @UnsupportedAppUsage
    public static boolean isCdma(int radioTechnology) {
        return radioTechnology == 4 || radioTechnology == 5 || radioTechnology == 6 || radioTechnology == 7 || radioTechnology == 8 || radioTechnology == 12 || radioTechnology == 13;
    }

    public static boolean isLte(int radioTechnology) {
        return radioTechnology == 14 || radioTechnology == 19;
    }

    public static boolean isPsTech(int radioTechnology) {
        return radioTechnology == 14 || radioTechnology == 19 || radioTechnology == 20;
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public static boolean bearerBitmapHasCdma(int networkTypeBitmask) {
        return (convertNetworkTypeBitmaskToBearerBitmask(networkTypeBitmask) & RIL_RADIO_CDMA_TECHNOLOGY_BITMASK) != 0;
    }

    @UnsupportedAppUsage
    public static boolean bitmaskHasTech(int bearerBitmask, int radioTech) {
        boolean z = true;
        if (bearerBitmask == 0) {
            return true;
        }
        if (radioTech < 1) {
            return false;
        }
        if (((1 << (radioTech - 1)) & bearerBitmask) == 0) {
            z = false;
        }
        return z;
    }

    public static int getBitmaskForTech(int radioTech) {
        if (radioTech >= 1) {
            return 1 << (radioTech - 1);
        }
        return 0;
    }

    public static int getBitmaskFromString(String bearerList) {
        String[] bearers = bearerList.split("\\|");
        int length = bearers.length;
        int bearerBitmask = 0;
        int bearerBitmask2 = 0;
        while (bearerBitmask2 < length) {
            try {
                int bearerInt = Integer.parseInt(bearers[bearerBitmask2].trim());
                if (bearerInt == 0) {
                    return 0;
                }
                bearerBitmask |= getBitmaskForTech(bearerInt);
                bearerBitmask2++;
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return bearerBitmask;
    }

    public static int convertNetworkTypeBitmaskToBearerBitmask(int networkTypeBitmask) {
        if (networkTypeBitmask == 0) {
            return 0;
        }
        int bearerBitmask = 0;
        for (int bearerInt = 0; bearerInt < 21; bearerInt++) {
            if (bitmaskHasTech(networkTypeBitmask, rilRadioTechnologyToNetworkType(bearerInt))) {
                bearerBitmask |= getBitmaskForTech(bearerInt);
            }
        }
        return bearerBitmask;
    }

    public static int convertBearerBitmaskToNetworkTypeBitmask(int bearerBitmask) {
        if (bearerBitmask == 0) {
            return 0;
        }
        int networkTypeBitmask = 0;
        for (int bearerInt = 0; bearerInt < 21; bearerInt++) {
            if (bitmaskHasTech(bearerBitmask, bearerInt)) {
                networkTypeBitmask |= getBitmaskForTech(rilRadioTechnologyToNetworkType(bearerInt));
            }
        }
        return networkTypeBitmask;
    }

    @UnsupportedAppUsage
    public static ServiceState mergeServiceStates(ServiceState baseSs, ServiceState voiceSs) {
        if (voiceSs.mVoiceRegState != 0) {
            return baseSs;
        }
        ServiceState newSs = new ServiceState(baseSs);
        newSs.mVoiceRegState = voiceSs.mVoiceRegState;
        newSs.mIsEmergencyOnly = false;
        return newSs;
    }

    @SystemApi
    public List<NetworkRegistrationInfo> getNetworkRegistrationInfoList() {
        List<NetworkRegistrationInfo> newList;
        synchronized (this.mNetworkRegistrationInfos) {
            newList = new ArrayList();
            for (NetworkRegistrationInfo nri : this.mNetworkRegistrationInfos) {
                newList.add(new NetworkRegistrationInfo(nri));
            }
        }
        return newList;
    }

    @SystemApi
    public List<NetworkRegistrationInfo> getNetworkRegistrationInfoListForTransportType(int transportType) {
        List<NetworkRegistrationInfo> list = new ArrayList();
        synchronized (this.mNetworkRegistrationInfos) {
            for (NetworkRegistrationInfo networkRegistrationInfo : this.mNetworkRegistrationInfos) {
                if (networkRegistrationInfo.getTransportType() == transportType) {
                    list.add(new NetworkRegistrationInfo(networkRegistrationInfo));
                }
            }
        }
        return list;
    }

    @SystemApi
    public List<NetworkRegistrationInfo> getNetworkRegistrationInfoListForDomain(int domain) {
        List<NetworkRegistrationInfo> list = new ArrayList();
        synchronized (this.mNetworkRegistrationInfos) {
            for (NetworkRegistrationInfo networkRegistrationInfo : this.mNetworkRegistrationInfos) {
                if (networkRegistrationInfo.getDomain() == domain) {
                    list.add(new NetworkRegistrationInfo(networkRegistrationInfo));
                }
            }
        }
        return list;
    }

    @SystemApi
    public NetworkRegistrationInfo getNetworkRegistrationInfo(int domain, int transportType) {
        synchronized (this.mNetworkRegistrationInfos) {
            for (NetworkRegistrationInfo networkRegistrationInfo : this.mNetworkRegistrationInfos) {
                if (networkRegistrationInfo.getTransportType() == transportType && networkRegistrationInfo.getDomain() == domain) {
                    NetworkRegistrationInfo networkRegistrationInfo2 = new NetworkRegistrationInfo(networkRegistrationInfo);
                    return networkRegistrationInfo2;
                }
            }
            return null;
        }
    }

    public void addNetworkRegistrationInfo(NetworkRegistrationInfo nri) {
        if (nri != null) {
            synchronized (this.mNetworkRegistrationInfos) {
                for (int i = 0; i < this.mNetworkRegistrationInfos.size(); i++) {
                    NetworkRegistrationInfo curRegState = (NetworkRegistrationInfo) this.mNetworkRegistrationInfos.get(i);
                    if (curRegState.getTransportType() == nri.getTransportType() && curRegState.getDomain() == nri.getDomain()) {
                        this.mNetworkRegistrationInfos.remove(i);
                        break;
                    }
                }
                this.mNetworkRegistrationInfos.add(new NetworkRegistrationInfo(nri));
            }
        }
    }

    public static final int getBetterNRFrequencyRange(int range1, int range2) {
        if (FREQUENCY_RANGE_ORDER.indexOf(Integer.valueOf(range1)) > FREQUENCY_RANGE_ORDER.indexOf(Integer.valueOf(range2))) {
            return range1;
        }
        return range2;
    }

    public ServiceState sanitizeLocationInfo(boolean removeCoarseLocation) {
        ServiceState state = new ServiceState(this);
        synchronized (state.mNetworkRegistrationInfos) {
            List<NetworkRegistrationInfo> networkRegistrationInfos = (List) state.mNetworkRegistrationInfos.stream().map(-$$Lambda$MLKtmRGKP3e0WU7x_KyS5-Vg8q4.INSTANCE).collect(Collectors.toList());
            state.mNetworkRegistrationInfos.clear();
            state.mNetworkRegistrationInfos.addAll(networkRegistrationInfos);
        }
        if (!removeCoarseLocation) {
            return state;
        }
        state.mDataOperatorAlphaLong = null;
        state.mDataOperatorAlphaShort = null;
        state.mDataOperatorNumeric = null;
        state.mVoiceOperatorAlphaLong = null;
        state.mVoiceOperatorAlphaShort = null;
        state.mVoiceOperatorNumeric = null;
        return state;
    }

    public void setOperatorAlphaLongRaw(String operatorAlphaLong) {
        this.mOperatorAlphaLongRaw = operatorAlphaLong;
    }

    public String getOperatorAlphaLongRaw() {
        return this.mOperatorAlphaLongRaw;
    }

    public void setOperatorAlphaShortRaw(String operatorAlphaShort) {
        this.mOperatorAlphaShortRaw = operatorAlphaShort;
    }

    public String getOperatorAlphaShortRaw() {
        return this.mOperatorAlphaShortRaw;
    }

    public void setIwlanPreferred(boolean isIwlanPreferred) {
        this.mIsIwlanPreferred = isIwlanPreferred;
    }
}
