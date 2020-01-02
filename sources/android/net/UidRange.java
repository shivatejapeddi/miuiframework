package android.net;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import java.util.Collection;

public final class UidRange implements Parcelable {
    public static final Creator<UidRange> CREATOR = new Creator<UidRange>() {
        public UidRange createFromParcel(Parcel in) {
            return new UidRange(in.readInt(), in.readInt());
        }

        public UidRange[] newArray(int size) {
            return new UidRange[size];
        }
    };
    public final int start;
    public final int stop;

    public UidRange(int startUid, int stopUid) {
        if (startUid < 0) {
            throw new IllegalArgumentException("Invalid start UID.");
        } else if (stopUid < 0) {
            throw new IllegalArgumentException("Invalid stop UID.");
        } else if (startUid <= stopUid) {
            this.start = startUid;
            this.stop = stopUid;
        } else {
            throw new IllegalArgumentException("Invalid UID range.");
        }
    }

    public static UidRange createForUser(int userId) {
        return new UidRange(userId * UserHandle.PER_USER_RANGE, ((userId + 1) * UserHandle.PER_USER_RANGE) - 1);
    }

    public int getStartUser() {
        return this.start / UserHandle.PER_USER_RANGE;
    }

    public int getEndUser() {
        return this.stop / UserHandle.PER_USER_RANGE;
    }

    public boolean contains(int uid) {
        return this.start <= uid && uid <= this.stop;
    }

    public int count() {
        return (this.stop + 1) - this.start;
    }

    public boolean containsRange(UidRange other) {
        return this.start <= other.start && other.stop <= this.stop;
    }

    public int hashCode() {
        return (((17 * 31) + this.start) * 31) + this.stop;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof UidRange)) {
            return false;
        }
        UidRange other = (UidRange) o;
        if (!(this.start == other.start && this.stop == other.stop)) {
            z = false;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.start);
        stringBuilder.append("-");
        stringBuilder.append(this.stop);
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.start);
        dest.writeInt(this.stop);
    }

    public static boolean containsUid(Collection<UidRange> ranges, int uid) {
        if (ranges == null) {
            return false;
        }
        for (UidRange range : ranges) {
            if (range.contains(uid)) {
                return true;
            }
        }
        return false;
    }
}
