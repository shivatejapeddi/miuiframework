package miui.mqsas.sdk.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class JavaExceptionEvent extends ExceptionEvent implements Parcelable {
    public static int CATEGROY_JE_OOM_NEED_DUMPHEAP = 1;
    public static final Creator<JavaExceptionEvent> CREATOR = new Creator<JavaExceptionEvent>() {
        public JavaExceptionEvent createFromParcel(Parcel in) {
            return new JavaExceptionEvent(in, null);
        }

        public JavaExceptionEvent[] newArray(int size) {
            return new JavaExceptionEvent[size];
        }
    };
    private String mExceptionClassName;
    private String mExceptionMessage;
    private int mJeCategroy;
    private String mPrefix;
    private String mStackTrace;
    private String mThreadName;

    /* synthetic */ JavaExceptionEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public JavaExceptionEvent() {
        String str = "";
        this.mThreadName = str;
        this.mPrefix = str;
        this.mExceptionClassName = str;
        this.mExceptionMessage = str;
        this.mStackTrace = str;
    }

    public void initType() {
        this.mType = 1;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeInt(this.mPid);
        dest.writeString(this.mProcessName);
        dest.writeString(this.mPackageName);
        dest.writeLong(this.mTimeStamp);
        dest.writeString(this.mSummary);
        dest.writeString(this.mDetails);
        dest.writeString(this.mDigest);
        dest.writeString(this.mLogName);
        dest.writeString(this.mKeyWord);
        dest.writeString(this.mThreadName);
        dest.writeString(this.mPrefix);
        dest.writeInt(this.mIsSystem);
        dest.writeString(this.mExceptionClassName);
        dest.writeString(this.mExceptionMessage);
        dest.writeString(this.mStackTrace);
        dest.writeInt(this.mIsUpload);
        dest.writeInt(this.mJeCategroy);
    }

    private JavaExceptionEvent(Parcel source) {
        this.mType = source.readInt();
        this.mPid = source.readInt();
        this.mProcessName = source.readString();
        this.mPackageName = source.readString();
        this.mTimeStamp = source.readLong();
        this.mSummary = source.readString();
        this.mDetails = source.readString();
        this.mDigest = source.readString();
        this.mLogName = source.readString();
        this.mKeyWord = source.readString();
        this.mThreadName = source.readString();
        this.mPrefix = source.readString();
        boolean z = false;
        this.mIsSystem = source.readInt() == 1;
        this.mExceptionClassName = source.readString();
        this.mExceptionMessage = source.readString();
        this.mStackTrace = source.readString();
        if (source.readInt() == 1) {
            z = true;
        }
        this.mIsUpload = z;
        this.mJeCategroy = source.readInt();
    }

    public String getThreadName() {
        return this.mThreadName;
    }

    public void setThreadName(String mThreadName) {
        this.mThreadName = mThreadName;
    }

    public String getPrefix() {
        return this.mPrefix;
    }

    public void setPrefix(String mPrefix) {
        this.mPrefix = mPrefix;
    }

    public String getExceptionClassName() {
        return this.mExceptionClassName;
    }

    public void setExceptionClassName(String mExceptionClassName) {
        this.mExceptionClassName = mExceptionClassName;
    }

    public String getExceptionMessage() {
        return this.mExceptionMessage;
    }

    public void setExceptionMessage(String mExceptionMessage) {
        this.mExceptionMessage = mExceptionMessage;
    }

    public String getStackTrace() {
        return this.mStackTrace;
    }

    public void setStackTrace(String mStackTrace) {
        this.mStackTrace = mStackTrace;
    }

    public int getJeCategroy() {
        return this.mJeCategroy;
    }

    public void setJeCategroy(int jeCategroy) {
        this.mJeCategroy = jeCategroy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("JavaException { mType=");
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
        stringBuilder.append(" mExceptionClassName=");
        stringBuilder.append(this.mExceptionClassName);
        stringBuilder.append(" mExceptionMessage=");
        stringBuilder.append(this.mExceptionMessage);
        stringBuilder.append(" mStackTrace=");
        stringBuilder.append(this.mStackTrace);
        stringBuilder.append(" mIsSystem=");
        stringBuilder.append(this.mIsSystem);
        stringBuilder.append(" mJeCategroy=");
        stringBuilder.append(this.mJeCategroy);
        sb.append(stringBuilder.toString());
        return sb.toString();
    }

    public String toShortString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("JavaException { mType=");
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
        stringBuilder.append(" mExceptionClassName=");
        stringBuilder.append(this.mExceptionClassName);
        stringBuilder.append(" mExceptionMessage=");
        stringBuilder.append(this.mExceptionMessage);
        sb.append(stringBuilder.toString());
        return sb.toString();
    }
}
