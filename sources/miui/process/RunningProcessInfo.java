package miui.process;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.text.format.DateFormat;
import java.util.Arrays;

public class RunningProcessInfo implements Parcelable {
    public static final Creator<RunningProcessInfo> CREATOR = new Creator<RunningProcessInfo>() {
        public RunningProcessInfo createFromParcel(Parcel in) {
            return new RunningProcessInfo(in);
        }

        public RunningProcessInfo[] newArray(int size) {
            return new RunningProcessInfo[size];
        }
    };
    public int mAdj;
    public int mPid;
    public String[] mPkgList;
    public int mProcState;
    public String mProcessName;
    public int mUid;

    protected RunningProcessInfo(Parcel in) {
        readFromParcel(in);
    }

    private void readFromParcel(Parcel in) {
        this.mProcessName = in.readString();
        this.mPid = in.readInt();
        this.mUid = in.readInt();
        this.mAdj = in.readInt();
        this.mProcState = in.readInt();
        this.mPkgList = in.readStringArray();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mProcessName);
        dest.writeInt(this.mPid);
        dest.writeInt(this.mUid);
        dest.writeInt(this.mAdj);
        dest.writeInt(this.mProcState);
        dest.writeStringArray(this.mPkgList);
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof RunningProcessInfo)) {
            return false;
        }
        RunningProcessInfo otherInfo = (RunningProcessInfo) obj;
        if (TextUtils.equals(this.mProcessName, otherInfo.mProcessName) && this.mPid == otherInfo.mPid) {
            z = true;
        }
        return z;
    }

    public boolean isProcessForeground() {
        int i = this.mProcState;
        return i >= 0 && i <= 6;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("RunningProcessInfo{mProcessName='");
        stringBuilder.append(this.mProcessName);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mPid=");
        stringBuilder.append(this.mPid);
        stringBuilder.append(", mUid=");
        stringBuilder.append(this.mUid);
        stringBuilder.append(", mAdj=");
        stringBuilder.append(this.mAdj);
        stringBuilder.append(", mProcState=");
        stringBuilder.append(this.mProcState);
        stringBuilder.append(", mPkgList=");
        stringBuilder.append(Arrays.toString(this.mPkgList));
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
