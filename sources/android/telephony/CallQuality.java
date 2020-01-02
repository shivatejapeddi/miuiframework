package android.telephony;

import android.annotation.SystemApi;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Objects;

@SystemApi
public final class CallQuality implements Parcelable {
    public static final int CALL_QUALITY_BAD = 4;
    public static final int CALL_QUALITY_EXCELLENT = 0;
    public static final int CALL_QUALITY_FAIR = 2;
    public static final int CALL_QUALITY_GOOD = 1;
    public static final int CALL_QUALITY_NOT_AVAILABLE = 5;
    public static final int CALL_QUALITY_POOR = 3;
    public static final Creator<CallQuality> CREATOR = new Creator() {
        public CallQuality createFromParcel(Parcel in) {
            return new CallQuality(in);
        }

        public CallQuality[] newArray(int size) {
            return new CallQuality[size];
        }
    };
    private int mAverageRelativeJitter;
    private int mAverageRoundTripTime;
    private int mCallDuration;
    private int mCodecType;
    private int mDownlinkCallQualityLevel;
    private int mMaxRelativeJitter;
    private int mNumRtpPacketsNotReceived;
    private int mNumRtpPacketsReceived;
    private int mNumRtpPacketsTransmitted;
    private int mNumRtpPacketsTransmittedLost;
    private int mUplinkCallQualityLevel;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CallQualityLevel {
    }

    public CallQuality(Parcel in) {
        this.mDownlinkCallQualityLevel = in.readInt();
        this.mUplinkCallQualityLevel = in.readInt();
        this.mCallDuration = in.readInt();
        this.mNumRtpPacketsTransmitted = in.readInt();
        this.mNumRtpPacketsReceived = in.readInt();
        this.mNumRtpPacketsTransmittedLost = in.readInt();
        this.mNumRtpPacketsNotReceived = in.readInt();
        this.mAverageRelativeJitter = in.readInt();
        this.mMaxRelativeJitter = in.readInt();
        this.mAverageRoundTripTime = in.readInt();
        this.mCodecType = in.readInt();
    }

    public CallQuality(int downlinkCallQualityLevel, int uplinkCallQualityLevel, int callDuration, int numRtpPacketsTransmitted, int numRtpPacketsReceived, int numRtpPacketsTransmittedLost, int numRtpPacketsNotReceived, int averageRelativeJitter, int maxRelativeJitter, int averageRoundTripTime, int codecType) {
        this.mDownlinkCallQualityLevel = downlinkCallQualityLevel;
        this.mUplinkCallQualityLevel = uplinkCallQualityLevel;
        this.mCallDuration = callDuration;
        this.mNumRtpPacketsTransmitted = numRtpPacketsTransmitted;
        this.mNumRtpPacketsReceived = numRtpPacketsReceived;
        this.mNumRtpPacketsTransmittedLost = numRtpPacketsTransmittedLost;
        this.mNumRtpPacketsNotReceived = numRtpPacketsNotReceived;
        this.mAverageRelativeJitter = averageRelativeJitter;
        this.mMaxRelativeJitter = maxRelativeJitter;
        this.mAverageRoundTripTime = averageRoundTripTime;
        this.mCodecType = codecType;
    }

    public int getDownlinkCallQualityLevel() {
        return this.mDownlinkCallQualityLevel;
    }

    public int getUplinkCallQualityLevel() {
        return this.mUplinkCallQualityLevel;
    }

    public int getCallDuration() {
        return this.mCallDuration;
    }

    public int getNumRtpPacketsTransmitted() {
        return this.mNumRtpPacketsTransmitted;
    }

    public int getNumRtpPacketsReceived() {
        return this.mNumRtpPacketsReceived;
    }

    public int getNumRtpPacketsTransmittedLost() {
        return this.mNumRtpPacketsTransmittedLost;
    }

    public int getNumRtpPacketsNotReceived() {
        return this.mNumRtpPacketsNotReceived;
    }

    public int getAverageRelativeJitter() {
        return this.mAverageRelativeJitter;
    }

    public int getMaxRelativeJitter() {
        return this.mMaxRelativeJitter;
    }

    public int getAverageRoundTripTime() {
        return this.mAverageRoundTripTime;
    }

    public int getCodecType() {
        return this.mCodecType;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CallQuality: {downlinkCallQualityLevel=");
        stringBuilder.append(this.mDownlinkCallQualityLevel);
        stringBuilder.append(" uplinkCallQualityLevel=");
        stringBuilder.append(this.mUplinkCallQualityLevel);
        stringBuilder.append(" callDuration=");
        stringBuilder.append(this.mCallDuration);
        stringBuilder.append(" numRtpPacketsTransmitted=");
        stringBuilder.append(this.mNumRtpPacketsTransmitted);
        stringBuilder.append(" numRtpPacketsReceived=");
        stringBuilder.append(this.mNumRtpPacketsReceived);
        stringBuilder.append(" numRtpPacketsTransmittedLost=");
        stringBuilder.append(this.mNumRtpPacketsTransmittedLost);
        stringBuilder.append(" numRtpPacketsNotReceived=");
        stringBuilder.append(this.mNumRtpPacketsNotReceived);
        stringBuilder.append(" averageRelativeJitter=");
        stringBuilder.append(this.mAverageRelativeJitter);
        stringBuilder.append(" maxRelativeJitter=");
        stringBuilder.append(this.mMaxRelativeJitter);
        stringBuilder.append(" averageRoundTripTime=");
        stringBuilder.append(this.mAverageRoundTripTime);
        stringBuilder.append(" codecType=");
        stringBuilder.append(this.mCodecType);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(this.mDownlinkCallQualityLevel), Integer.valueOf(this.mUplinkCallQualityLevel), Integer.valueOf(this.mCallDuration), Integer.valueOf(this.mNumRtpPacketsTransmitted), Integer.valueOf(this.mNumRtpPacketsReceived), Integer.valueOf(this.mNumRtpPacketsTransmittedLost), Integer.valueOf(this.mNumRtpPacketsNotReceived), Integer.valueOf(this.mAverageRelativeJitter), Integer.valueOf(this.mMaxRelativeJitter), Integer.valueOf(this.mAverageRoundTripTime), Integer.valueOf(this.mCodecType)});
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (o == null || !(o instanceof CallQuality) || hashCode() != o.hashCode()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        CallQuality s = (CallQuality) o;
        if (this.mDownlinkCallQualityLevel == s.mDownlinkCallQualityLevel && this.mUplinkCallQualityLevel == s.mUplinkCallQualityLevel && this.mCallDuration == s.mCallDuration && this.mNumRtpPacketsTransmitted == s.mNumRtpPacketsTransmitted && this.mNumRtpPacketsReceived == s.mNumRtpPacketsReceived && this.mNumRtpPacketsTransmittedLost == s.mNumRtpPacketsTransmittedLost && this.mNumRtpPacketsNotReceived == s.mNumRtpPacketsNotReceived && this.mAverageRelativeJitter == s.mAverageRelativeJitter && this.mMaxRelativeJitter == s.mMaxRelativeJitter && this.mAverageRoundTripTime == s.mAverageRoundTripTime && this.mCodecType == s.mCodecType) {
            z = true;
        }
        return z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mDownlinkCallQualityLevel);
        dest.writeInt(this.mUplinkCallQualityLevel);
        dest.writeInt(this.mCallDuration);
        dest.writeInt(this.mNumRtpPacketsTransmitted);
        dest.writeInt(this.mNumRtpPacketsReceived);
        dest.writeInt(this.mNumRtpPacketsTransmittedLost);
        dest.writeInt(this.mNumRtpPacketsNotReceived);
        dest.writeInt(this.mAverageRelativeJitter);
        dest.writeInt(this.mMaxRelativeJitter);
        dest.writeInt(this.mAverageRoundTripTime);
        dest.writeInt(this.mCodecType);
    }
}
