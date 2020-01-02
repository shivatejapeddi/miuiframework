package android.os;

import android.os.Parcelable.Creator;
import com.android.internal.util.Preconditions;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class CoolingDevice implements Parcelable {
    public static final Creator<CoolingDevice> CREATOR = new Creator<CoolingDevice>() {
        public CoolingDevice createFromParcel(Parcel p) {
            return new CoolingDevice(p.readLong(), p.readInt(), p.readString());
        }

        public CoolingDevice[] newArray(int size) {
            return new CoolingDevice[size];
        }
    };
    public static final int TYPE_BATTERY = 1;
    public static final int TYPE_COMPONENT = 6;
    public static final int TYPE_CPU = 2;
    public static final int TYPE_FAN = 0;
    public static final int TYPE_GPU = 3;
    public static final int TYPE_MODEM = 4;
    public static final int TYPE_NPU = 5;
    private final String mName;
    private final int mType;
    private final long mValue;

    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
    }

    public static boolean isValidType(int type) {
        return type >= 0 && type <= 6;
    }

    public CoolingDevice(long value, int type, String name) {
        Preconditions.checkArgument(isValidType(type), "Invalid Type");
        this.mValue = value;
        this.mType = type;
        this.mName = (String) Preconditions.checkStringNotEmpty(name);
    }

    public long getValue() {
        return this.mValue;
    }

    public int getType() {
        return this.mType;
    }

    public String getName() {
        return this.mName;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CoolingDevice{mValue=");
        stringBuilder.append(this.mValue);
        stringBuilder.append(", mType=");
        stringBuilder.append(this.mType);
        stringBuilder.append(", mName=");
        stringBuilder.append(this.mName);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public int hashCode() {
        return (((this.mName.hashCode() * 31) + Long.hashCode(this.mValue)) * 31) + this.mType;
    }

    public boolean equals(Object o) {
        boolean z = false;
        if (!(o instanceof CoolingDevice)) {
            return false;
        }
        CoolingDevice other = (CoolingDevice) o;
        if (other.mValue == this.mValue && other.mType == this.mType && other.mName.equals(this.mName)) {
            z = true;
        }
        return z;
    }

    public void writeToParcel(Parcel p, int flags) {
        p.writeLong(this.mValue);
        p.writeInt(this.mType);
        p.writeString(this.mName);
    }

    public int describeContents() {
        return 0;
    }
}
