package miui.process;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateFormat;

public class ForegroundInfo implements Parcelable {
    public static final Creator<ForegroundInfo> CREATOR = new Creator<ForegroundInfo>() {
        public ForegroundInfo createFromParcel(Parcel in) {
            return new ForegroundInfo(in, null);
        }

        public ForegroundInfo[] newArray(int size) {
            return new ForegroundInfo[size];
        }
    };
    public static final int FLAG_FOREGROUND_COLD_START = 1;
    public int mFlags;
    public String mForegroundPackageName;
    public int mForegroundPid;
    public int mForegroundUid;
    public String mLastForegroundPackageName;
    public int mLastForegroundPid;
    public int mLastForegroundUid;
    public String mMultiWindowForegroundPackageName;
    public int mMultiWindowForegroundUid;

    /* synthetic */ ForegroundInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public ForegroundInfo() {
        this.mForegroundUid = -1;
        this.mForegroundPid = -1;
        this.mLastForegroundUid = -1;
        this.mLastForegroundPid = -1;
        this.mMultiWindowForegroundUid = -1;
    }

    public ForegroundInfo(ForegroundInfo origin) {
        this.mForegroundUid = -1;
        this.mForegroundPid = -1;
        this.mLastForegroundUid = -1;
        this.mLastForegroundPid = -1;
        this.mMultiWindowForegroundUid = -1;
        this.mForegroundPackageName = origin.mForegroundPackageName;
        this.mForegroundUid = origin.mForegroundUid;
        this.mForegroundPid = origin.mForegroundPid;
        this.mLastForegroundPackageName = origin.mLastForegroundPackageName;
        this.mLastForegroundUid = origin.mLastForegroundUid;
        this.mLastForegroundPid = origin.mLastForegroundPid;
        this.mMultiWindowForegroundPackageName = origin.mMultiWindowForegroundPackageName;
        this.mMultiWindowForegroundUid = origin.mMultiWindowForegroundUid;
        this.mFlags = origin.mFlags;
    }

    private ForegroundInfo(Parcel in) {
        this.mForegroundUid = -1;
        this.mForegroundPid = -1;
        this.mLastForegroundUid = -1;
        this.mLastForegroundPid = -1;
        this.mMultiWindowForegroundUid = -1;
        this.mForegroundPackageName = in.readString();
        this.mForegroundUid = in.readInt();
        this.mForegroundPid = in.readInt();
        this.mLastForegroundPackageName = in.readString();
        this.mLastForegroundUid = in.readInt();
        this.mLastForegroundPid = in.readInt();
        this.mMultiWindowForegroundPackageName = in.readString();
        this.mMultiWindowForegroundUid = in.readInt();
        this.mFlags = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mForegroundPackageName);
        dest.writeInt(this.mForegroundUid);
        dest.writeInt(this.mForegroundPid);
        dest.writeString(this.mLastForegroundPackageName);
        dest.writeInt(this.mLastForegroundUid);
        dest.writeInt(this.mLastForegroundPid);
        dest.writeString(this.mMultiWindowForegroundPackageName);
        dest.writeInt(this.mMultiWindowForegroundUid);
        dest.writeInt(this.mFlags);
    }

    public boolean isColdStart() {
        return (this.mFlags & 1) != 0;
    }

    public void addFlags(int flags) {
        this.mFlags |= flags;
    }

    public void resetFlags() {
        this.mFlags = 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ForegroundInfo{mForegroundPackageName='");
        stringBuilder.append(this.mForegroundPackageName);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mForegroundUid=");
        stringBuilder.append(this.mForegroundUid);
        stringBuilder.append(", mForegroundPid=");
        stringBuilder.append(this.mForegroundPid);
        stringBuilder.append(", mLastForegroundPackageName='");
        stringBuilder.append(this.mLastForegroundPackageName);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mLastForegroundUid=");
        stringBuilder.append(this.mLastForegroundUid);
        stringBuilder.append(", mLastForegroundPid=");
        stringBuilder.append(this.mLastForegroundPid);
        stringBuilder.append(", mMultiWindowForegroundPackageName='");
        stringBuilder.append(this.mMultiWindowForegroundPackageName);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mMultiWindowForegroundUid=");
        stringBuilder.append(this.mMultiWindowForegroundUid);
        stringBuilder.append(", mFlags=");
        stringBuilder.append(Integer.toHexString(this.mFlags));
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
