package android.util;

import android.os.Parcel;
import java.util.Objects;

public final class TimestampedValue<T> {
    private final long mReferenceTimeMillis;
    private final T mValue;

    public TimestampedValue(long referenceTimeMillis, T value) {
        this.mReferenceTimeMillis = referenceTimeMillis;
        this.mValue = value;
    }

    public long getReferenceTimeMillis() {
        return this.mReferenceTimeMillis;
    }

    public T getValue() {
        return this.mValue;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TimestampedValue<?> that = (TimestampedValue) o;
        if (!(this.mReferenceTimeMillis == that.mReferenceTimeMillis && Objects.equals(this.mValue, that.mValue))) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{Long.valueOf(this.mReferenceTimeMillis), this.mValue});
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("TimestampedValue{mReferenceTimeMillis=");
        stringBuilder.append(this.mReferenceTimeMillis);
        stringBuilder.append(", mValue=");
        stringBuilder.append(this.mValue);
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public static <T> TimestampedValue<T> readFromParcel(Parcel in, ClassLoader classLoader, Class<? extends T> valueClass) {
        long referenceTimeMillis = in.readLong();
        T value = in.readValue(classLoader);
        if (value == null || valueClass.isAssignableFrom(value.getClass())) {
            return new TimestampedValue(referenceTimeMillis, value);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Value was of type ");
        stringBuilder.append(value.getClass());
        stringBuilder.append(" is not assignable to ");
        stringBuilder.append(valueClass);
        throw new RuntimeException(stringBuilder.toString());
    }

    public static void writeToParcel(Parcel dest, TimestampedValue<?> timestampedValue) {
        dest.writeLong(timestampedValue.mReferenceTimeMillis);
        dest.writeValue(timestampedValue.mValue);
    }

    public static long referenceTimeDifference(TimestampedValue<?> one, TimestampedValue<?> two) {
        return one.mReferenceTimeMillis - two.mReferenceTimeMillis;
    }
}
