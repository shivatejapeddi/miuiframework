package com.android.internal.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class LaunchTimeRecord implements Parcelable, Serializable {
    public static final Creator<LaunchTimeRecord> CREATOR = new Creator<LaunchTimeRecord>() {
        public LaunchTimeRecord createFromParcel(Parcel source) {
            return new LaunchTimeRecord(source, null);
        }

        public LaunchTimeRecord[] newArray(int size) {
            return new LaunchTimeRecord[size];
        }
    };
    private static final long serialVersionUID = 1;
    String activity;
    boolean isColdStart;
    long launchEndTime;
    long launchStartTime;
    long launchTime;
    String packageName;
    int type;

    /* synthetic */ LaunchTimeRecord(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public LaunchTimeRecord() {
        this.packageName = null;
        this.activity = null;
    }

    public LaunchTimeRecord(String packageName, String activity, long launchStartTime, long launchEndTime, boolean isColdStart) {
        this.packageName = null;
        this.activity = null;
        this.packageName = packageName;
        this.activity = activity;
        this.launchStartTime = launchStartTime;
        this.launchEndTime = launchEndTime;
        this.launchTime = launchEndTime - launchStartTime;
        this.isColdStart = isColdStart;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getActivity() {
        return this.activity;
    }

    public void setLaunchStartTime(long launchStartTime) {
        this.launchStartTime = launchStartTime;
    }

    public long getLaunchStartTime() {
        return this.launchStartTime;
    }

    public void setLaunchEndTime(long launchEndTime) {
        this.launchEndTime = launchEndTime;
    }

    public long getLaunchEndTime() {
        return this.launchEndTime;
    }

    public long getLaunchTime() {
        return this.launchTime;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }

    public boolean isColdStart() {
        return this.isColdStart;
    }

    public void setIsColdStart(boolean isColdStart) {
        this.isColdStart = isColdStart;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.activity);
        dest.writeLong(this.launchStartTime);
        dest.writeLong(this.launchEndTime);
        dest.writeLong(this.launchTime);
        dest.writeInt(this.type);
        dest.writeInt(this.isColdStart);
    }

    public void readFromParcel(Parcel source) {
        this.activity = source.readString();
        this.launchStartTime = source.readLong();
        this.launchEndTime = source.readLong();
        this.launchTime = source.readLong();
        this.type = source.readInt();
        boolean z = true;
        if (source.readInt() != 1) {
            z = false;
        }
        this.isColdStart = z;
    }

    private LaunchTimeRecord(Parcel source) {
        this.packageName = null;
        this.activity = null;
        readFromParcel(source);
    }
}
