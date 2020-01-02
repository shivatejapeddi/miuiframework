package android.hardware.biometrics;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public enum BiometricSourceType implements Parcelable {
    FINGERPRINT,
    FACE,
    IRIS;
    
    public static final Creator<BiometricSourceType> CREATOR = null;

    static {
        CREATOR = new Creator<BiometricSourceType>() {
            public BiometricSourceType createFromParcel(Parcel source) {
                return BiometricSourceType.valueOf(source.readString());
            }

            public BiometricSourceType[] newArray(int size) {
                return new BiometricSourceType[size];
            }
        };
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name());
    }
}
