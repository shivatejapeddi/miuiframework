package com.miui.whetstone;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class WhetstonePackageState implements Parcelable {
    public static final int CLOUD_USERID = 0;
    public static final Creator<WhetstonePackageState> CREATOR = new Creator<WhetstonePackageState>() {
        public WhetstonePackageState createFromParcel(Parcel in) {
            return new WhetstonePackageState(in);
        }

        public WhetstonePackageState[] newArray(int size) {
            return new WhetstonePackageState[size];
        }
    };
    public static final boolean DEBUG = WhetstoneManager.DEBUG;
    public static final String SERVICE_RESTART = "Restart: AMS";
    public static final String TAG = "WhetstonePackageState";
    public static final int WPS_CHECK_FALSE = -1;
    public static final int WPS_CHECK_TRUE = 1;
    public static final int WPS_FEATURE_ACTIVITY = 1;
    public static final int WPS_FEATURE_ALL_OFF = 0;
    public static final int WPS_FEATURE_ALL_OPEN = 15;
    public static final int WPS_FEATURE_PROVIDER = 8;
    public static final int WPS_FEATURE_RECEIVER = 4;
    public static final int WPS_FEATURE_SERVICE = 2;
    public static final int WPS_START_BY_ACTIVITY = 1;
    public static final int WPS_START_BY_ALL = 15;
    public static final int WPS_START_BY_DEFAULT = 0;
    public static final int WPS_START_BY_PROVIDER = 8;
    public static final int WPS_START_BY_RECEIVER = 4;
    public static final int WPS_START_BY_SERVICE = 2;
    public static final int WPS_START_FORBIDDEN = 0;

    protected WhetstonePackageState(Parcel in) {
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
    }
}
