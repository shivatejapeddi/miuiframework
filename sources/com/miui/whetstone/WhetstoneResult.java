package com.miui.whetstone;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WhetstoneResult implements Parcelable {
    public static final Creator<WhetstoneResult> CREATOR = new Creator<WhetstoneResult>() {
        public WhetstoneResult createFromParcel(Parcel source) {
            return new WhetstoneResult(source, null);
        }

        public WhetstoneResult[] newArray(int size) {
            return new WhetstoneResult[size];
        }
    };
    private int mCode;
    private String mResult;

    public WhetstoneResult(int code) {
        this(code, null);
    }

    public WhetstoneResult(int code, String result) {
        this.mCode = code;
        this.mResult = result;
    }

    public int getCode() {
        return this.mCode;
    }

    public String getResult() {
        return this.mResult;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mCode);
        dest.writeString(this.mResult);
    }

    public void readFromParcel(Parcel source) {
        this.mCode = source.readInt();
        this.mResult = source.readString();
    }

    private WhetstoneResult(Parcel source) {
        readFromParcel(source);
    }
}
