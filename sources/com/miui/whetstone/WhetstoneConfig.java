package com.miui.whetstone;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;
import java.util.List;

public class WhetstoneConfig implements Parcelable {
    public static final Creator<WhetstoneConfig> CREATOR = new Creator<WhetstoneConfig>() {
        public WhetstoneConfig createFromParcel(Parcel source) {
            return new WhetstoneConfig(source, null);
        }

        public WhetstoneConfig[] newArray(int size) {
            return new WhetstoneConfig[size];
        }
    };
    public static final int DEEP_CLEAN_TYPE_GAME_CLEAN = 10;
    public static final int DEEP_CLEAN_TYPE_LOCK_SCREEN = 4;
    public static final int DEEP_CLEAN_TYPE_NORMAL = 0;
    public static final int DEEP_CLEAN_TYPE_ONE_KEY = 2;
    public static final int DEEP_CLEAN_TYPE_STRONG_CLEAN = 8;
    public static final int DEEP_CLEAN_TYPE_UNKNOW = -1;
    int mType;
    List<String> mWhiteList;

    public WhetstoneConfig(List<String> whiteList, int type) {
        this.mWhiteList = whiteList;
        this.mType = type;
    }

    public void setWhiteList(List<String> whiteList) {
        this.mWhiteList = whiteList;
    }

    public List<String> getWhiteList() {
        return this.mWhiteList;
    }

    public int getDeepCleanType() {
        return this.mType;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        if (this.mWhiteList == null) {
            this.mWhiteList = new ArrayList();
        }
        dest.writeList(this.mWhiteList);
        dest.writeInt(this.mType);
    }

    public void readFromParcel(Parcel source) {
        this.mWhiteList = source.readArrayList(List.class.getClassLoader());
        this.mType = source.readInt();
    }

    private WhetstoneConfig(Parcel source) {
        readFromParcel(source);
    }
}
