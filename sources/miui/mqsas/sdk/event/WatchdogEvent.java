package miui.mqsas.sdk.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WatchdogEvent extends ExceptionEvent implements Parcelable {
    public static final Creator<WatchdogEvent> CREATOR = new Creator<WatchdogEvent>() {
        public WatchdogEvent createFromParcel(Parcel in) {
            return new WatchdogEvent(in, null);
        }

        public WatchdogEvent[] newArray(int size) {
            return new WatchdogEvent[size];
        }
    };
    private String[] mCheckers;
    private String[] mStackTraces;

    /* synthetic */ WatchdogEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public void initType() {
        this.mType = 2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeInt(this.mPid);
        dest.writeString(this.mProcessName);
        dest.writeString(this.mPackageName);
        dest.writeInt(this.mIsSystem);
        dest.writeLong(this.mTimeStamp);
        dest.writeString(this.mSummary);
        dest.writeString(this.mDetails);
        dest.writeString(this.mDigest);
        dest.writeString(this.mLogName);
        dest.writeString(this.mKeyWord);
        dest.writeStringArray(this.mCheckers);
        dest.writeStringArray(this.mStackTraces);
        dest.writeInt(this.mIsUpload);
    }

    private WatchdogEvent(Parcel source) {
        this.mType = source.readInt();
        this.mPid = source.readInt();
        this.mProcessName = source.readString();
        this.mPackageName = source.readString();
        boolean z = false;
        this.mIsSystem = source.readInt() == 1;
        this.mTimeStamp = source.readLong();
        this.mSummary = source.readString();
        this.mDetails = source.readString();
        this.mDigest = source.readString();
        this.mLogName = source.readString();
        this.mKeyWord = source.readString();
        this.mCheckers = source.readStringArray();
        this.mStackTraces = source.readStringArray();
        if (source.readInt() == 1) {
            z = true;
        }
        this.mIsUpload = z;
    }

    public String[] getCheckers() {
        return this.mCheckers;
    }

    public String[] getStackTraces() {
        return this.mStackTraces;
    }

    public void setCheckers(String[] mCheckers) {
        this.mCheckers = mCheckers;
    }

    public void setStackTraces(String[] mStackTraces) {
        this.mStackTraces = mStackTraces;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Watchdog { mType=");
        stringBuilder.append(this.mType);
        stringBuilder.append(" Pid=");
        stringBuilder.append(this.mPid);
        stringBuilder.append(" processName=");
        stringBuilder.append(this.mProcessName);
        stringBuilder.append(" mTimeStamp=");
        stringBuilder.append(this.mTimeStamp);
        stringBuilder.append(" mSummary=");
        stringBuilder.append(this.mSummary);
        stringBuilder.append(" mDetails=");
        stringBuilder.append(this.mDetails);
        stringBuilder.append(" mDigest=");
        stringBuilder.append(this.mDigest);
        stringBuilder.append(" mLogName=");
        stringBuilder.append(this.mLogName);
        stringBuilder.append(" mKeyWord=");
        stringBuilder.append(this.mKeyWord);
        sb.append(stringBuilder.toString());
        return sb.toString();
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Watchdog { mType=");
        stringBuilder.append(this.mType);
        stringBuilder.append(" Pid=");
        stringBuilder.append(this.mPid);
        stringBuilder.append(" processName=");
        stringBuilder.append(this.mProcessName);
        stringBuilder.append(" mTimeStamp=");
        stringBuilder.append(this.mTimeStamp);
        stringBuilder.append(" mDigest=");
        stringBuilder.append(this.mDigest);
        stringBuilder.append(" mSummary=");
        stringBuilder.append(this.mSummary);
        stringBuilder.append(" mLogName=");
        stringBuilder.append(this.mLogName);
        stringBuilder.append(" mKeyWord=");
        stringBuilder.append(this.mKeyWord);
        sb.append(stringBuilder.toString());
        return sb.toString();
    }
}
