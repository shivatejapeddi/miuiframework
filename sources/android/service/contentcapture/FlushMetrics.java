package android.service.contentcapture;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class FlushMetrics implements Parcelable {
    public static final Creator<FlushMetrics> CREATOR = new Creator<FlushMetrics>() {
        public FlushMetrics createFromParcel(Parcel in) {
            FlushMetrics flushMetrics = new FlushMetrics();
            flushMetrics.sessionStarted = in.readInt();
            flushMetrics.sessionFinished = in.readInt();
            flushMetrics.viewAppearedCount = in.readInt();
            flushMetrics.viewDisappearedCount = in.readInt();
            flushMetrics.viewTextChangedCount = in.readInt();
            return flushMetrics;
        }

        public FlushMetrics[] newArray(int size) {
            return new FlushMetrics[size];
        }
    };
    public int sessionFinished;
    public int sessionStarted;
    public int viewAppearedCount;
    public int viewDisappearedCount;
    public int viewTextChangedCount;

    public void reset() {
        this.viewAppearedCount = 0;
        this.viewDisappearedCount = 0;
        this.viewTextChangedCount = 0;
        this.sessionStarted = 0;
        this.sessionFinished = 0;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.sessionStarted);
        out.writeInt(this.sessionFinished);
        out.writeInt(this.viewAppearedCount);
        out.writeInt(this.viewDisappearedCount);
        out.writeInt(this.viewTextChangedCount);
    }
}
