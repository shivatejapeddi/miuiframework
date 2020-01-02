package miui.mqsas.sdk.event;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class ScreenOnEvent implements Parcelable {
    public static final String AVG_SCREEN_ON = "avg_screen_on";
    public static final Creator<ScreenOnEvent> CREATOR = new Creator<ScreenOnEvent>() {
        public ScreenOnEvent createFromParcel(Parcel in) {
            return new ScreenOnEvent(in, null);
        }

        public ScreenOnEvent[] newArray(int size) {
            return new ScreenOnEvent[size];
        }
    };
    public static final String LT_SCREEN_ON = "lt_screen_on";
    public static final String[] TYPE_SCREEN_ON = new String[]{AVG_SCREEN_ON, Context.POWER_SERVICE, "dp_center", "keyguard_screenon_notification", "keyguard_screenon_finger_pass", "lid"};
    private long mBlockScreenTime;
    private String mScreenOnType;
    private long mSetDisplayTime;
    private String mTimeOutDetail;
    private String mTimeStamp;
    private String mTimeoutSummary;
    private long mTotalTime;
    private String mWakeSource;

    /* synthetic */ ScreenOnEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public ScreenOnEvent() {
        String str = "";
        this.mTimeoutSummary = str;
        this.mTimeOutDetail = str;
        this.mWakeSource = str;
        this.mTimeStamp = str;
        this.mTotalTime = -1;
        this.mSetDisplayTime = -1;
        this.mBlockScreenTime = -1;
        this.mScreenOnType = str;
    }

    private ScreenOnEvent(Parcel in) {
        this.mTimeoutSummary = in.readString();
        this.mTimeOutDetail = in.readString();
        this.mWakeSource = in.readString();
        this.mTimeStamp = in.readString();
        this.mTotalTime = in.readLong();
        this.mSetDisplayTime = in.readLong();
        this.mBlockScreenTime = in.readLong();
        this.mScreenOnType = in.readString();
    }

    public String getTimeoutSummary() {
        return this.mTimeoutSummary;
    }

    public void setTimeoutSummary(String mTimeoutSummary) {
        this.mTimeoutSummary = mTimeoutSummary;
    }

    public String getmTimeOutDetail() {
        return this.mTimeOutDetail;
    }

    public void setmTimeOutDetail(String mTimeOutDetail) {
        this.mTimeOutDetail = mTimeOutDetail;
    }

    public String getWakeSource() {
        return this.mWakeSource;
    }

    public void setWakeSource(String mWakeSource) {
        this.mWakeSource = mWakeSource;
    }

    public String getTimeStamp() {
        return this.mTimeStamp;
    }

    public void setTimeStamp(String mTimeStamp) {
        this.mTimeStamp = mTimeStamp;
    }

    public long getTotalTime() {
        return this.mTotalTime;
    }

    public void setTotalTime(long mTotalTime) {
        this.mTotalTime = mTotalTime;
    }

    public long getSetDisplayTime() {
        return this.mSetDisplayTime;
    }

    public void setSetDisplayTime(long mSetDisplayTime) {
        this.mSetDisplayTime = mSetDisplayTime;
    }

    public long getBlockScreenTime() {
        return this.mBlockScreenTime;
    }

    public void setBlockScreenTime(long mBlockScreenTime) {
        this.mBlockScreenTime = mBlockScreenTime;
    }

    public String getScreenOnType() {
        return this.mScreenOnType;
    }

    public void setScreenOnType(String mScreenOnType) {
        this.mScreenOnType = mScreenOnType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ScreenOnEvent { mTimeoutSummary = ");
        stringBuilder.append(this.mTimeoutSummary);
        stringBuilder.append(", mTimeOutDetail = ");
        stringBuilder.append(this.mTimeOutDetail);
        stringBuilder.append(", mWakeSource = ");
        stringBuilder.append(this.mWakeSource);
        stringBuilder.append(", mTimeStamp = ");
        stringBuilder.append(this.mTimeStamp);
        stringBuilder.append(", mTotalTime = ");
        stringBuilder.append(this.mTotalTime);
        stringBuilder.append(", mSetDisplayTime = ");
        stringBuilder.append(this.mSetDisplayTime);
        stringBuilder.append(", mBlockScreenTime = ");
        stringBuilder.append(this.mBlockScreenTime);
        stringBuilder.append(", mScreenOnType = ");
        stringBuilder.append(this.mScreenOnType);
        stringBuilder.append(" }");
        sb.append(stringBuilder.toString());
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTimeoutSummary);
        dest.writeString(this.mTimeOutDetail);
        dest.writeString(this.mWakeSource);
        dest.writeString(this.mTimeStamp);
        dest.writeLong(this.mTotalTime);
        dest.writeLong(this.mSetDisplayTime);
        dest.writeLong(this.mBlockScreenTime);
        dest.writeString(this.mScreenOnType);
    }
}
