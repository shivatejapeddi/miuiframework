package com.miui.whetstone;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class BroadcastPolicy implements Parcelable {
    public static final Creator<BroadcastPolicy> CREATOR = new Creator<BroadcastPolicy>() {
        public BroadcastPolicy createFromParcel(Parcel in) {
            return new BroadcastPolicy(in, null);
        }

        public BroadcastPolicy[] newArray(int size) {
            return new BroadcastPolicy[size];
        }
    };
    public String[] mRestrictTypes;
    public int mUid;

    public BroadcastPolicy(int uid, String[] restrictTypes) {
        this.mUid = uid;
        this.mRestrictTypes = restrictTypes;
    }

    private BroadcastPolicy(Parcel in) {
        this.mUid = in.readInt();
        this.mRestrictTypes = in.createStringArray();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mUid);
        dest.writeStringArray(this.mRestrictTypes);
    }
}
