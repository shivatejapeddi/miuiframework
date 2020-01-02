package miui.mqsas.sdk.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class FeatureEvent extends ExceptionEvent implements Parcelable {
    public static final Creator<FeatureEvent> CREATOR = new Creator<FeatureEvent>() {
        public FeatureEvent createFromParcel(Parcel in) {
            return new FeatureEvent(in, null);
        }

        public FeatureEvent[] newArray(int size) {
            return new FeatureEvent[size];
        }
    };
    private String subClass;

    /* synthetic */ FeatureEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public FeatureEvent(String processName, String packageName, String message, String details, String type) {
        this.mProcessName = processName;
        this.mPackageName = packageName;
        this.mSummary = message;
        this.mDetails = details;
        this.subClass = type;
    }

    public void initType() {
        setType(388);
    }

    public String getSubClass() {
        return this.subClass;
    }

    public void setSubClass(String subClass) {
        this.subClass = subClass;
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
        dest.writeString(this.subClass);
    }

    private FeatureEvent(Parcel source) {
        this.mType = source.readInt();
        this.mPid = source.readInt();
        this.mProcessName = source.readString();
        this.mPackageName = source.readString();
        boolean z = true;
        if (source.readInt() != 1) {
            z = false;
        }
        this.mIsSystem = z;
        this.mTimeStamp = source.readLong();
        this.mSummary = source.readString();
        this.mDetails = source.readString();
        this.mDigest = source.readString();
        this.mLogName = source.readString();
        this.mKeyWord = source.readString();
        this.subClass = source.readString();
    }
}
