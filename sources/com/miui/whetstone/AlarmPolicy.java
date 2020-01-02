package com.miui.whetstone;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class AlarmPolicy implements Parcelable {
    public static final Creator<AlarmPolicy> CREATOR = new Creator<AlarmPolicy>() {
        public AlarmPolicy createFromParcel(Parcel in) {
            return new AlarmPolicy(in, null);
        }

        public AlarmPolicy[] newArray(int size) {
            return new AlarmPolicy[size];
        }
    };
    public String[] mTags;
    public int mUid;

    public AlarmPolicy(int uid, String[] tags) {
        this.mUid = uid;
        this.mTags = tags;
    }

    private AlarmPolicy(Parcel in) {
        this.mUid = in.readInt();
        this.mTags = in.createStringArray();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mUid);
        dest.writeStringArray(this.mTags);
    }

    public String toString() {
        StringBuilder ret = new StringBuilder(256);
        ret.append('{');
        ret.append("uid=");
        ret = ret.append(this.mUid);
        String[] strArr = this.mTags;
        if (strArr == null) {
            ret.append(" mTags=null}");
        } else if (strArr.length == 0) {
            ret.append(" mTags=all}");
        } else {
            ret.append(" mTags=[");
            for (int i = 0; i < this.mTags.length; i++) {
                if (i != 0) {
                    ret.append(',');
                }
                ret.append(this.mTags[i]);
            }
            ret.append("]}");
        }
        return ret.toString();
    }
}
