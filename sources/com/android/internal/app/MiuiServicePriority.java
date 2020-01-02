package com.android.internal.app;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class MiuiServicePriority implements Parcelable, Serializable {
    public static final Creator<MiuiServicePriority> CREATOR = new Creator<MiuiServicePriority>() {
        public MiuiServicePriority createFromParcel(Parcel source) {
            return new MiuiServicePriority(source, null);
        }

        public MiuiServicePriority[] newArray(int size) {
            return new MiuiServicePriority[size];
        }
    };
    public static final int HIGH_PRIORITY = 10;
    public static final int LOW_PRIORITY = -10;
    public static final int NORMAL_PRIORITY = 0;
    private static final long serialVersionUID = 1;
    public boolean checkPriority;
    public long delayTime;
    public boolean inBlacklist;
    public String packageName;
    public int priority;
    public String serviceName;

    /* synthetic */ MiuiServicePriority(Parcel x0, AnonymousClass1 x1) {
        this(x0);
    }

    public MiuiServicePriority(String packageName, String serviceName, int priority, boolean checkPriority, boolean inBlacklist, long delayTime) {
        this.packageName = packageName;
        this.serviceName = serviceName;
        this.priority = priority;
        this.checkPriority = checkPriority;
        this.inBlacklist = inBlacklist;
        this.delayTime = delayTime;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.packageName);
        dest.writeString(this.serviceName);
        dest.writeInt(this.priority);
        dest.writeInt(this.checkPriority);
        dest.writeInt(this.inBlacklist);
        dest.writeLong(this.delayTime);
    }

    public void readFromParcel(Parcel source) {
        this.packageName = source.readString();
        this.serviceName = source.readString();
        this.priority = source.readInt();
        boolean z = false;
        this.checkPriority = source.readInt() == 1;
        if (source.readInt() == 1) {
            z = true;
        }
        this.inBlacklist = z;
        this.delayTime = source.readLong();
    }

    private MiuiServicePriority(Parcel source) {
        readFromParcel(source);
    }
}
