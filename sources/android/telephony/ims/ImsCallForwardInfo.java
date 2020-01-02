package android.telephony.ims;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SystemApi
public final class ImsCallForwardInfo implements Parcelable {
    public static final int CDIV_CF_REASON_ALL = 4;
    public static final int CDIV_CF_REASON_ALL_CONDITIONAL = 5;
    public static final int CDIV_CF_REASON_BUSY = 1;
    public static final int CDIV_CF_REASON_NOT_LOGGED_IN = 6;
    public static final int CDIV_CF_REASON_NOT_REACHABLE = 3;
    public static final int CDIV_CF_REASON_NO_REPLY = 2;
    public static final int CDIV_CF_REASON_UNCONDITIONAL = 0;
    public static final Creator<ImsCallForwardInfo> CREATOR = new Creator<ImsCallForwardInfo>() {
        public ImsCallForwardInfo createFromParcel(Parcel in) {
            return new ImsCallForwardInfo(in);
        }

        public ImsCallForwardInfo[] newArray(int size) {
            return new ImsCallForwardInfo[size];
        }
    };
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_NOT_ACTIVE = 0;
    public static final int TYPE_OF_ADDRESS_INTERNATIONAL = 145;
    public static final int TYPE_OF_ADDRESS_UNKNOWN = 129;
    @UnsupportedAppUsage
    public int mCondition;
    @UnsupportedAppUsage
    public String mNumber;
    @UnsupportedAppUsage
    public int mServiceClass;
    @UnsupportedAppUsage
    public int mStatus;
    @UnsupportedAppUsage
    public int mTimeSeconds;
    @UnsupportedAppUsage
    public int mToA;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CallForwardReasons {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface CallForwardStatus {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface TypeOfAddress {
    }

    public ImsCallForwardInfo(int reason, int status, int toA, int serviceClass, String number, int replyTimerSec) {
        this.mCondition = reason;
        this.mStatus = status;
        this.mToA = toA;
        this.mServiceClass = serviceClass;
        this.mNumber = number;
        this.mTimeSeconds = replyTimerSec;
    }

    public ImsCallForwardInfo(Parcel in) {
        readFromParcel(in);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mCondition);
        out.writeInt(this.mStatus);
        out.writeInt(this.mToA);
        out.writeString(this.mNumber);
        out.writeInt(this.mTimeSeconds);
        out.writeInt(this.mServiceClass);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(super.toString());
        stringBuilder.append(", Condition: ");
        stringBuilder.append(this.mCondition);
        stringBuilder.append(", Status: ");
        stringBuilder.append(this.mStatus == 0 ? "disabled" : "enabled");
        stringBuilder.append(", ToA: ");
        stringBuilder.append(this.mToA);
        stringBuilder.append(", Service Class: ");
        stringBuilder.append(this.mServiceClass);
        stringBuilder.append(", Number=");
        stringBuilder.append(this.mNumber);
        stringBuilder.append(", Time (seconds): ");
        stringBuilder.append(this.mTimeSeconds);
        return stringBuilder.toString();
    }

    private void readFromParcel(Parcel in) {
        this.mCondition = in.readInt();
        this.mStatus = in.readInt();
        this.mToA = in.readInt();
        this.mNumber = in.readString();
        this.mTimeSeconds = in.readInt();
        this.mServiceClass = in.readInt();
    }

    public int getCondition() {
        return this.mCondition;
    }

    public int getStatus() {
        return this.mStatus;
    }

    public int getToA() {
        return this.mToA;
    }

    public int getServiceClass() {
        return this.mServiceClass;
    }

    public String getNumber() {
        return this.mNumber;
    }

    public int getTimeSeconds() {
        return this.mTimeSeconds;
    }
}
