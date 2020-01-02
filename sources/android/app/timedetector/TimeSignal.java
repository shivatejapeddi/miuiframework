package android.app.timedetector;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.format.DateFormat;
import android.util.TimestampedValue;
import java.util.Objects;

public final class TimeSignal implements Parcelable {
    public static final Creator<TimeSignal> CREATOR = new Creator<TimeSignal>() {
        public TimeSignal createFromParcel(Parcel in) {
            return TimeSignal.createFromParcel(in);
        }

        public TimeSignal[] newArray(int size) {
            return new TimeSignal[size];
        }
    };
    public static final String SOURCE_ID_NITZ = "nitz";
    private final String mSourceId;
    private final TimestampedValue<Long> mUtcTime;

    public TimeSignal(String sourceId, TimestampedValue<Long> utcTime) {
        this.mSourceId = (String) Objects.requireNonNull(sourceId);
        this.mUtcTime = (TimestampedValue) Objects.requireNonNull(utcTime);
    }

    private static TimeSignal createFromParcel(Parcel in) {
        return new TimeSignal(in.readString(), TimestampedValue.readFromParcel(in, null, Long.class));
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSourceId);
        TimestampedValue.writeToParcel(dest, this.mUtcTime);
    }

    public String getSourceId() {
        return this.mSourceId;
    }

    public TimestampedValue<Long> getUtcTime() {
        return this.mUtcTime;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimeSignal that = (TimeSignal) o;
        if (!(Objects.equals(this.mSourceId, that.mSourceId) && Objects.equals(this.mUtcTime, that.mUtcTime))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mSourceId, this.mUtcTime});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TimeSignal{mSourceId='");
        stringBuilder.append(this.mSourceId);
        stringBuilder.append(DateFormat.QUOTE);
        stringBuilder.append(", mUtcTime=");
        stringBuilder.append(this.mUtcTime);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }
}
