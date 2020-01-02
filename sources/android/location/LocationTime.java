package android.location;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class LocationTime implements Parcelable {
    public static final Creator<LocationTime> CREATOR = new Creator<LocationTime>() {
        public LocationTime createFromParcel(Parcel in) {
            return new LocationTime(in.readLong(), in.readLong());
        }

        public LocationTime[] newArray(int size) {
            return new LocationTime[size];
        }
    };
    private final long mElapsedRealtimeNanos;
    private final long mTime;

    public LocationTime(long time, long elapsedRealtimeNanos) {
        this.mTime = time;
        this.mElapsedRealtimeNanos = elapsedRealtimeNanos;
    }

    public long getTime() {
        return this.mTime;
    }

    public long getElapsedRealtimeNanos() {
        return this.mElapsedRealtimeNanos;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeLong(this.mTime);
        out.writeLong(this.mElapsedRealtimeNanos);
    }

    public int describeContents() {
        return 0;
    }
}
