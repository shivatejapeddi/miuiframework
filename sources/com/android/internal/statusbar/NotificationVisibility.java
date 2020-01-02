package com.android.internal.statusbar;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class NotificationVisibility implements Parcelable {
    public static final Creator<NotificationVisibility> CREATOR = new Creator<NotificationVisibility>() {
        public NotificationVisibility createFromParcel(Parcel parcel) {
            return NotificationVisibility.obtain(parcel);
        }

        public NotificationVisibility[] newArray(int size) {
            return new NotificationVisibility[size];
        }
    };
    private static final int MAX_POOL_SIZE = 25;
    private static final String TAG = "NoViz";
    private static int sNexrId = 0;
    public int count;
    int id;
    public String key;
    public NotificationLocation location;
    public int rank;
    public boolean visible;

    public enum NotificationLocation {
        LOCATION_UNKNOWN(0),
        LOCATION_FIRST_HEADS_UP(1),
        LOCATION_HIDDEN_TOP(2),
        LOCATION_MAIN_AREA(3),
        LOCATION_BOTTOM_STACK_PEEKING(4),
        LOCATION_BOTTOM_STACK_HIDDEN(5),
        LOCATION_GONE(6);
        
        private final int mMetricsEventNotificationLocation;

        private NotificationLocation(int metricsEventNotificationLocation) {
            this.mMetricsEventNotificationLocation = metricsEventNotificationLocation;
        }

        public int toMetricsEventEnum() {
            return this.mMetricsEventNotificationLocation;
        }
    }

    private NotificationVisibility() {
        this.visible = true;
        int i = sNexrId;
        sNexrId = i + 1;
        this.id = i;
    }

    private NotificationVisibility(String key, int rank, int count, boolean visible, NotificationLocation location) {
        this();
        this.key = key;
        this.rank = rank;
        this.count = count;
        this.visible = visible;
        this.location = location;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NotificationVisibility(id=");
        stringBuilder.append(this.id);
        stringBuilder.append(" key=");
        stringBuilder.append(this.key);
        stringBuilder.append(" rank=");
        stringBuilder.append(this.rank);
        stringBuilder.append(" count=");
        stringBuilder.append(this.count);
        stringBuilder.append(this.visible ? " visible" : "");
        stringBuilder.append(" location=");
        stringBuilder.append(this.location.name());
        stringBuilder.append(" )");
        return stringBuilder.toString();
    }

    public NotificationVisibility clone() {
        return obtain(this.key, this.rank, this.count, this.visible, this.location);
    }

    public int hashCode() {
        String str = this.key;
        return str == null ? 0 : str.hashCode();
    }

    public boolean equals(Object that) {
        boolean z = false;
        if (!(that instanceof NotificationVisibility)) {
            return false;
        }
        NotificationVisibility thatViz = (NotificationVisibility) that;
        if ((this.key == null && thatViz.key == null) || this.key.equals(thatViz.key)) {
            z = true;
        }
        return z;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.key);
        out.writeInt(this.rank);
        out.writeInt(this.count);
        out.writeInt(this.visible);
        out.writeString(this.location.name());
    }

    private void readFromParcel(Parcel in) {
        this.key = in.readString();
        this.rank = in.readInt();
        this.count = in.readInt();
        this.visible = in.readInt() != 0;
        this.location = NotificationLocation.valueOf(in.readString());
    }

    public static NotificationVisibility obtain(String key, int rank, int count, boolean visible) {
        return obtain(key, rank, count, visible, NotificationLocation.LOCATION_UNKNOWN);
    }

    public static NotificationVisibility obtain(String key, int rank, int count, boolean visible, NotificationLocation location) {
        NotificationVisibility vo = obtain();
        vo.key = key;
        vo.rank = rank;
        vo.count = count;
        vo.visible = visible;
        vo.location = location;
        return vo;
    }

    private static NotificationVisibility obtain(Parcel in) {
        NotificationVisibility vo = obtain();
        vo.readFromParcel(in);
        return vo;
    }

    private static NotificationVisibility obtain() {
        return new NotificationVisibility();
    }

    public void recycle() {
    }
}
