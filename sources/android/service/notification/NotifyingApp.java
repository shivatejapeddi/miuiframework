package android.service.notification;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateFormat;
import java.util.Objects;

public final class NotifyingApp implements Parcelable, Comparable<NotifyingApp> {
    public static final Creator<NotifyingApp> CREATOR = new Creator<NotifyingApp>() {
        public NotifyingApp createFromParcel(Parcel in) {
            return new NotifyingApp(in);
        }

        public NotifyingApp[] newArray(int size) {
            return new NotifyingApp[size];
        }
    };
    private long mLastNotified;
    private String mPkg;
    private int mUserId;

    protected NotifyingApp(Parcel in) {
        this.mUserId = in.readInt();
        this.mPkg = in.readString();
        this.mLastNotified = in.readLong();
    }

    public int getUserId() {
        return this.mUserId;
    }

    public NotifyingApp setUserId(int mUserId) {
        this.mUserId = mUserId;
        return this;
    }

    public String getPackage() {
        return this.mPkg;
    }

    public NotifyingApp setPackage(String mPkg) {
        this.mPkg = mPkg;
        return this;
    }

    public long getLastNotified() {
        return this.mLastNotified;
    }

    public NotifyingApp setLastNotified(long mLastNotified) {
        this.mLastNotified = mLastNotified;
        return this;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mUserId);
        dest.writeString(this.mPkg);
        dest.writeLong(this.mLastNotified);
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NotifyingApp that = (NotifyingApp) o;
        if (!(getUserId() == that.getUserId() && getLastNotified() == that.getLastNotified() && Objects.equals(this.mPkg, that.mPkg))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Integer.valueOf(getUserId()), this.mPkg, Long.valueOf(getLastNotified())});
    }

    public int compareTo(NotifyingApp o) {
        if (getLastNotified() != o.getLastNotified()) {
            return -Long.compare(getLastNotified(), o.getLastNotified());
        }
        if (getUserId() == o.getUserId()) {
            return getPackage().compareTo(o.getPackage());
        }
        return Integer.compare(getUserId(), o.getUserId());
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("NotifyingApp{mUserId=");
        stringBuilder.append(this.mUserId);
        stringBuilder.append(", mPkg='");
        stringBuilder.append(this.mPkg);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mLastNotified=");
        stringBuilder.append(this.mLastNotified);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
