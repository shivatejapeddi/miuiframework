package android.bluetooth;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

@Deprecated
public final class BluetoothHealthAppConfiguration implements Parcelable {
    @Deprecated
    public static final Creator<BluetoothHealthAppConfiguration> CREATOR = new Creator<BluetoothHealthAppConfiguration>() {
        public BluetoothHealthAppConfiguration createFromParcel(Parcel in) {
            return new BluetoothHealthAppConfiguration();
        }

        public BluetoothHealthAppConfiguration[] newArray(int size) {
            return new BluetoothHealthAppConfiguration[size];
        }
    };

    BluetoothHealthAppConfiguration() {
    }

    public int describeContents() {
        return 0;
    }

    @Deprecated
    public int getDataType() {
        return 0;
    }

    @Deprecated
    public String getName() {
        return null;
    }

    @Deprecated
    public int getRole() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
    }
}
