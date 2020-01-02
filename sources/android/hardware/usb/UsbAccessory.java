package android.hardware.usb;

import android.app.ActivityThread;
import android.hardware.usb.IUsbSerialReader.Stub;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.util.Preconditions;

public class UsbAccessory implements Parcelable {
    public static final Creator<UsbAccessory> CREATOR = new Creator<UsbAccessory>() {
        public UsbAccessory createFromParcel(Parcel in) {
            return new UsbAccessory(in.readString(), in.readString(), in.readString(), in.readString(), in.readString(), Stub.asInterface(in.readStrongBinder()));
        }

        public UsbAccessory[] newArray(int size) {
            return new UsbAccessory[size];
        }
    };
    public static final int DESCRIPTION_STRING = 2;
    public static final int MANUFACTURER_STRING = 0;
    public static final int MODEL_STRING = 1;
    public static final int SERIAL_STRING = 5;
    private static final String TAG = "UsbAccessory";
    public static final int URI_STRING = 4;
    public static final int VERSION_STRING = 3;
    private final String mDescription;
    private final String mManufacturer;
    private final String mModel;
    private final IUsbSerialReader mSerialNumberReader;
    private final String mUri;
    private final String mVersion;

    public UsbAccessory(String manufacturer, String model, String description, String version, String uri, IUsbSerialReader serialNumberReader) {
        this.mManufacturer = (String) Preconditions.checkNotNull(manufacturer);
        this.mModel = (String) Preconditions.checkNotNull(model);
        this.mDescription = description;
        this.mVersion = version;
        this.mUri = uri;
        this.mSerialNumberReader = serialNumberReader;
        if (ActivityThread.isSystem()) {
            Preconditions.checkArgument(this.mSerialNumberReader instanceof Stub);
        }
    }

    @Deprecated
    public UsbAccessory(String manufacturer, String model, String description, String version, String uri, final String serialNumber) {
        this(manufacturer, model, description, version, uri, new Stub() {
            public String getSerial(String packageName) {
                return serialNumber;
            }
        });
    }

    public String getManufacturer() {
        return this.mManufacturer;
    }

    public String getModel() {
        return this.mModel;
    }

    public String getDescription() {
        return this.mDescription;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public String getUri() {
        return this.mUri;
    }

    public String getSerial() {
        try {
            return this.mSerialNumberReader.getSerial(ActivityThread.currentPackageName());
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    private static boolean compare(String s1, String s2) {
        if (s1 != null) {
            return s1.equals(s2);
        }
        return s2 == null;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (!(obj instanceof UsbAccessory)) {
            return false;
        }
        UsbAccessory accessory = (UsbAccessory) obj;
        if (compare(this.mManufacturer, accessory.getManufacturer()) && compare(this.mModel, accessory.getModel()) && compare(this.mDescription, accessory.getDescription()) && compare(this.mVersion, accessory.getVersion()) && compare(this.mUri, accessory.getUri()) && compare(getSerial(), accessory.getSerial())) {
            z = true;
        }
        return z;
    }

    public int hashCode() {
        int hashCode = this.mManufacturer.hashCode() ^ this.mModel.hashCode();
        String str = this.mDescription;
        int i = 0;
        hashCode ^= str == null ? 0 : str.hashCode();
        str = this.mVersion;
        hashCode ^= str == null ? 0 : str.hashCode();
        str = this.mUri;
        if (str != null) {
            i = str.hashCode();
        }
        return hashCode ^ i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UsbAccessory[mManufacturer=");
        stringBuilder.append(this.mManufacturer);
        stringBuilder.append(", mModel=");
        stringBuilder.append(this.mModel);
        stringBuilder.append(", mDescription=");
        stringBuilder.append(this.mDescription);
        stringBuilder.append(", mVersion=");
        stringBuilder.append(this.mVersion);
        stringBuilder.append(", mUri=");
        stringBuilder.append(this.mUri);
        stringBuilder.append(", mSerialNumberReader=");
        stringBuilder.append(this.mSerialNumberReader);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mManufacturer);
        parcel.writeString(this.mModel);
        parcel.writeString(this.mDescription);
        parcel.writeString(this.mVersion);
        parcel.writeString(this.mUri);
        parcel.writeStrongBinder(this.mSerialNumberReader.asBinder());
    }
}
