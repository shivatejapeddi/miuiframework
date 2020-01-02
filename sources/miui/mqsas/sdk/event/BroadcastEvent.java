package miui.mqsas.sdk.event;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BroadcastEvent extends ExceptionEvent implements Parcelable {
    public static final Creator<BroadcastEvent> CREATOR = new Creator<BroadcastEvent>() {
        public BroadcastEvent createFromParcel(Parcel in) {
            return new BroadcastEvent(in, null);
        }

        public BroadcastEvent[] newArray(int size) {
            return new BroadcastEvent[size];
        }
    };
    private String mAction;
    private String mBroadcastReceiver;
    private String mCallerPackage;
    private int mCount;
    private long mDispatchClockTime;
    private long mEnqueueClockTime;
    private long mFinishClockTime;
    private boolean mIsQueuedWorked;
    private int mNumReceivers;
    private String mReason;
    private long mTotalTime;

    /* synthetic */ BroadcastEvent(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public BroadcastEvent() {
        initBroadcastEvent();
    }

    private void initBroadcastEvent() {
        this.mType = -1;
        String str = "";
        this.mAction = str;
        this.mCallerPackage = str;
        this.mReason = str;
        this.mEnqueueClockTime = -1;
        this.mDispatchClockTime = -1;
        this.mFinishClockTime = -1;
        this.mBroadcastReceiver = str;
        this.mIsQueuedWorked = false;
        this.mTotalTime = -1;
        this.mCount = -1;
        this.mNumReceivers = -1;
    }

    public void initType() {
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
        dest.writeString(this.mAction);
        dest.writeString(this.mCallerPackage);
        dest.writeInt(this.mIsSystem);
        dest.writeString(this.mReason);
        dest.writeLong(this.mEnqueueClockTime);
        dest.writeLong(this.mDispatchClockTime);
        dest.writeLong(this.mFinishClockTime);
        dest.writeString(this.mBroadcastReceiver);
        dest.writeInt(this.mIsQueuedWorked);
        dest.writeLong(this.mTotalTime);
        dest.writeInt(this.mCount);
        dest.writeInt(this.mNumReceivers);
        dest.writeInt(this.mIsUpload);
    }

    private BroadcastEvent(Parcel source) {
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
        this.mAction = source.readString();
        this.mCallerPackage = source.readString();
        boolean z = false;
        this.mIsSystem = source.readInt() == 1;
        this.mReason = source.readString();
        this.mEnqueueClockTime = source.readLong();
        this.mDispatchClockTime = source.readLong();
        this.mFinishClockTime = source.readLong();
        this.mBroadcastReceiver = source.readString();
        this.mIsQueuedWorked = source.readInt() == 1;
        this.mTotalTime = source.readLong();
        this.mCount = source.readInt();
        this.mNumReceivers = source.readInt();
        if (source.readInt() == 1) {
            z = true;
        }
        this.mIsUpload = z;
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public String getAction() {
        return this.mAction;
    }

    public void setAction(String action) {
        this.mAction = action;
    }

    public String getCallerPackage() {
        return this.mCallerPackage;
    }

    public void setCallerPackage(String callerPackage) {
        this.mCallerPackage = callerPackage;
    }

    public String getReason() {
        return this.mReason;
    }

    public void setReason(String reason) {
        this.mReason = reason;
    }

    public long getEnTime() {
        return this.mEnqueueClockTime;
    }

    public void setEnTime(long EnqueueClockTime) {
        this.mEnqueueClockTime = EnqueueClockTime;
    }

    public long getDisTime() {
        return this.mDispatchClockTime;
    }

    public void setDisTime(long DispatchClockTime) {
        this.mDispatchClockTime = DispatchClockTime;
    }

    public long getFinTime() {
        return this.mFinishClockTime;
    }

    public void setFinTime(long FinishClockTime) {
        this.mFinishClockTime = FinishClockTime;
    }

    public String getBrReceiver() {
        return this.mBroadcastReceiver;
    }

    public void setBrReceiver(String BroadcastReceiver) {
        this.mBroadcastReceiver = BroadcastReceiver;
    }

    public boolean isQuWorked() {
        return this.mIsQueuedWorked;
    }

    public void setQuWorked(boolean isQueuedWorked) {
        this.mIsQueuedWorked = isQueuedWorked;
    }

    public long getTotalTime() {
        return this.mTotalTime;
    }

    public void setTotalTime(long TotalTime) {
        this.mTotalTime = TotalTime;
    }

    public long getCount() {
        return (long) this.mCount;
    }

    public void setCount(int count) {
        this.mCount = count;
    }

    public int getNumReceivers() {
        return this.mNumReceivers;
    }

    public void setNumReceivers(int numReceivers) {
        this.mNumReceivers = numReceivers;
    }

    public void addCount() {
        this.mCount++;
    }

    public void addTotalTime(long totalTime) {
        this.mTotalTime += totalTime;
    }

    private String formatTime(long timestamp) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(timestamp));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BroadcastEvent { mType=");
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
        stringBuilder.append(" mAction= ");
        stringBuilder.append(this.mAction);
        stringBuilder.append(" mPackageName=");
        stringBuilder.append(this.mPackageName);
        stringBuilder.append(" mReason= ");
        stringBuilder.append(this.mReason);
        stringBuilder.append(" mEnqueueClockTime= ");
        stringBuilder.append(formatTime(this.mEnqueueClockTime));
        stringBuilder.append(" mDispatchClockTime= ");
        stringBuilder.append(formatTime(this.mDispatchClockTime));
        stringBuilder.append(" mFinishClockTime= ");
        stringBuilder.append(formatTime(this.mFinishClockTime));
        stringBuilder.append(" mBroadcastReceiver=");
        stringBuilder.append(this.mBroadcastReceiver);
        stringBuilder.append(" isQueueWorked=");
        stringBuilder.append(this.mIsQueuedWorked);
        stringBuilder.append(" mCallerPackage=");
        stringBuilder.append(this.mCallerPackage);
        stringBuilder.append(" mCount=");
        stringBuilder.append(this.mCount);
        stringBuilder.append(", mTotalTime=");
        stringBuilder.append(this.mTotalTime);
        stringBuilder.append(" ms");
        sb.append(stringBuilder.toString());
        return sb.toString();
    }
}
