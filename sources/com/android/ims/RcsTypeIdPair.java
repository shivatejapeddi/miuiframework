package com.android.ims;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class RcsTypeIdPair implements Parcelable {
    public static final Creator<RcsTypeIdPair> CREATOR = new Creator<RcsTypeIdPair>() {
        public RcsTypeIdPair createFromParcel(Parcel in) {
            return new RcsTypeIdPair(in);
        }

        public RcsTypeIdPair[] newArray(int size) {
            return new RcsTypeIdPair[size];
        }
    };
    private int mId;
    private int mType;

    public RcsTypeIdPair(int type, int id) {
        this.mType = type;
        this.mId = id;
    }

    public int getType() {
        return this.mType;
    }

    public void setType(int type) {
        this.mType = type;
    }

    public int getId() {
        return this.mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public RcsTypeIdPair(Parcel in) {
        this.mType = in.readInt();
        this.mId = in.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mType);
        dest.writeInt(this.mId);
    }
}
