package com.miui.whetstone;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AppInfo implements Parcelable {
    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in, null);
        }

        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };
    public String mPrePackageName;
    public int mPreUid;
    public String mScreenPackageName;
    public int mScreenUid;
    public String mTopMultiPackageName;
    public int mTopMultiUid;

    /* synthetic */ AppInfo(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public AppInfo(String sPackageName, int sUid, String pPackageName, int pUid, String mPackageName, int mUid) {
        this.mScreenPackageName = sPackageName;
        this.mScreenUid = sUid;
        this.mPrePackageName = pPackageName;
        this.mPreUid = pUid;
        this.mTopMultiPackageName = mPackageName;
        this.mTopMultiUid = mUid;
    }

    private AppInfo(Parcel in) {
        this.mScreenPackageName = in.readString();
        this.mScreenUid = in.readInt();
        this.mPrePackageName = in.readString();
        this.mPreUid = in.readInt();
        this.mTopMultiPackageName = in.readString();
        this.mTopMultiUid = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mScreenPackageName);
        dest.writeInt(this.mScreenUid);
        dest.writeString(this.mPrePackageName);
        dest.writeInt(this.mPreUid);
        dest.writeString(this.mTopMultiPackageName);
        dest.writeInt(this.mTopMultiUid);
    }
}
