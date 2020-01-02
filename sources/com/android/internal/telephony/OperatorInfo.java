package com.android.internal.telephony;

import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Telephony.Carriers;

public class OperatorInfo implements Parcelable {
    @UnsupportedAppUsage
    public static final Creator<OperatorInfo> CREATOR = new Creator<OperatorInfo>() {
        public OperatorInfo createFromParcel(Parcel in) {
            return new OperatorInfo(in.readString(), in.readString(), in.readString(), (State) in.readSerializable());
        }

        public OperatorInfo[] newArray(int size) {
            return new OperatorInfo[size];
        }
    };
    @UnsupportedAppUsage
    private String mOperatorAlphaLong;
    @UnsupportedAppUsage
    private String mOperatorAlphaShort;
    @UnsupportedAppUsage
    private String mOperatorNumeric;
    private String mRadioTech;
    @UnsupportedAppUsage
    private State mState;

    public enum State {
        UNKNOWN,
        AVAILABLE,
        CURRENT,
        FORBIDDEN
    }

    @UnsupportedAppUsage
    public String getOperatorAlphaLong() {
        return this.mOperatorAlphaLong;
    }

    @UnsupportedAppUsage
    public String getOperatorAlphaShort() {
        return this.mOperatorAlphaShort;
    }

    @UnsupportedAppUsage
    public String getOperatorNumeric() {
        return this.mOperatorNumeric;
    }

    @UnsupportedAppUsage
    public State getState() {
        return this.mState;
    }

    public String getRadioTech() {
        return this.mRadioTech;
    }

    @UnsupportedAppUsage
    OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, State state) {
        this.mState = State.UNKNOWN;
        this.mOperatorAlphaLong = operatorAlphaLong;
        this.mOperatorAlphaShort = operatorAlphaShort;
        this.mOperatorNumeric = operatorNumeric;
        this.mRadioTech = "";
        if (operatorNumeric != null) {
            String[] values = operatorNumeric.split("\\+");
            if (values.length > 0) {
                this.mOperatorNumeric = values[0];
                if (values.length > 1) {
                    this.mRadioTech = values[1];
                }
            }
        }
        this.mState = state;
    }

    @UnsupportedAppUsage
    public OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric, String stateString) {
        this(operatorAlphaLong, operatorAlphaShort, operatorNumeric, rilStateToState(stateString));
    }

    @UnsupportedAppUsage(maxTargetSdk = 28, trackingBug = 115609023)
    public OperatorInfo(String operatorAlphaLong, String operatorAlphaShort, String operatorNumeric) {
        this(operatorAlphaLong, operatorAlphaShort, operatorNumeric, State.UNKNOWN);
    }

    @UnsupportedAppUsage
    private static State rilStateToState(String s) {
        if (s.equals("unknown")) {
            return State.UNKNOWN;
        }
        if (s.equals("available")) {
            return State.AVAILABLE;
        }
        if (s.equals(Carriers.CURRENT)) {
            return State.CURRENT;
        }
        if (s.equals("forbidden")) {
            return State.FORBIDDEN;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RIL impl error: Invalid network state '");
        stringBuilder.append(s);
        stringBuilder.append("'");
        throw new RuntimeException(stringBuilder.toString());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("OperatorInfo ");
        stringBuilder.append(this.mOperatorAlphaLong);
        String str = "/";
        stringBuilder.append(str);
        stringBuilder.append(this.mOperatorAlphaShort);
        stringBuilder.append(str);
        stringBuilder.append(this.mOperatorNumeric);
        stringBuilder.append(str);
        stringBuilder.append(this.mRadioTech);
        stringBuilder.append(str);
        stringBuilder.append(this.mState);
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mOperatorAlphaLong);
        dest.writeString(this.mOperatorAlphaShort);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.mOperatorNumeric);
        stringBuilder.append("+");
        stringBuilder.append(this.mRadioTech);
        dest.writeString(stringBuilder.toString());
        dest.writeSerializable(this.mState);
    }
}
