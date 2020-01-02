package android.hardware.usb;

import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.hardware.usb.IUsbSerialReader.Stub;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.RemoteException;
import com.android.internal.util.Preconditions;

public class UsbDevice implements Parcelable {
    public static final Creator<UsbDevice> CREATOR = new Creator<UsbDevice>() {
        public UsbDevice createFromParcel(Parcel in) {
            return new UsbDevice(in.readString(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readString(), in.readString(), in.readString(), (UsbConfiguration[]) in.readParcelableArray(UsbConfiguration.class.getClassLoader(), UsbConfiguration.class), Stub.asInterface(in.readStrongBinder()), null);
        }

        public UsbDevice[] newArray(int size) {
            return new UsbDevice[size];
        }
    };
    private static final boolean DEBUG = false;
    private static final String TAG = "UsbDevice";
    private final int mClass;
    private final UsbConfiguration[] mConfigurations;
    @UnsupportedAppUsage
    private UsbInterface[] mInterfaces;
    private final String mManufacturerName;
    private final String mName;
    private final int mProductId;
    private final String mProductName;
    private final int mProtocol;
    private final IUsbSerialReader mSerialNumberReader;
    private final int mSubclass;
    private final int mVendorId;
    private final String mVersion;

    public static class Builder {
        private final int mClass;
        private final UsbConfiguration[] mConfigurations;
        private final String mManufacturerName;
        private final String mName;
        private final int mProductId;
        private final String mProductName;
        private final int mProtocol;
        private final int mSubclass;
        private final int mVendorId;
        private final String mVersion;
        public final String serialNumber;

        public Builder(String name, int vendorId, int productId, int Class, int subClass, int protocol, String manufacturerName, String productName, String version, UsbConfiguration[] configurations, String serialNumber) {
            this.mName = (String) Preconditions.checkNotNull(name);
            this.mVendorId = vendorId;
            this.mProductId = productId;
            this.mClass = Class;
            this.mSubclass = subClass;
            this.mProtocol = protocol;
            this.mManufacturerName = manufacturerName;
            this.mProductName = productName;
            this.mVersion = (String) Preconditions.checkStringNotEmpty(version);
            this.mConfigurations = configurations;
            this.serialNumber = serialNumber;
        }

        public UsbDevice build(IUsbSerialReader serialReader) {
            return new UsbDevice(this.mName, this.mVendorId, this.mProductId, this.mClass, this.mSubclass, this.mProtocol, this.mManufacturerName, this.mProductName, this.mVersion, this.mConfigurations, serialReader, null);
        }
    }

    private static native int native_get_device_id(String str);

    private static native String native_get_device_name(int i);

    /* synthetic */ UsbDevice(String x0, int x1, int x2, int x3, int x4, int x5, String x6, String x7, String x8, UsbConfiguration[] x9, IUsbSerialReader x10, AnonymousClass1 x11) {
        this(x0, x1, x2, x3, x4, x5, x6, x7, x8, x9, x10);
    }

    private UsbDevice(String name, int vendorId, int productId, int Class, int subClass, int protocol, String manufacturerName, String productName, String version, UsbConfiguration[] configurations, IUsbSerialReader serialNumberReader) {
        this.mName = (String) Preconditions.checkNotNull(name);
        this.mVendorId = vendorId;
        this.mProductId = productId;
        this.mClass = Class;
        this.mSubclass = subClass;
        this.mProtocol = protocol;
        this.mManufacturerName = manufacturerName;
        this.mProductName = productName;
        this.mVersion = (String) Preconditions.checkStringNotEmpty(version);
        this.mConfigurations = (UsbConfiguration[]) Preconditions.checkArrayElementsNotNull(configurations, "configurations");
        this.mSerialNumberReader = (IUsbSerialReader) Preconditions.checkNotNull(serialNumberReader);
        if (ActivityThread.isSystem()) {
            Preconditions.checkArgument(this.mSerialNumberReader instanceof Stub);
        }
    }

    public String getDeviceName() {
        return this.mName;
    }

    public String getManufacturerName() {
        return this.mManufacturerName;
    }

    public String getProductName() {
        return this.mProductName;
    }

    public String getVersion() {
        return this.mVersion;
    }

    public String getSerialNumber() {
        try {
            return this.mSerialNumberReader.getSerial(ActivityThread.currentPackageName());
        } catch (RemoteException e) {
            e.rethrowFromSystemServer();
            return null;
        }
    }

    public int getDeviceId() {
        return getDeviceId(this.mName);
    }

    public int getVendorId() {
        return this.mVendorId;
    }

    public int getProductId() {
        return this.mProductId;
    }

    public int getDeviceClass() {
        return this.mClass;
    }

    public int getDeviceSubclass() {
        return this.mSubclass;
    }

    public int getDeviceProtocol() {
        return this.mProtocol;
    }

    public int getConfigurationCount() {
        return this.mConfigurations.length;
    }

    public UsbConfiguration getConfiguration(int index) {
        return this.mConfigurations[index];
    }

    private UsbInterface[] getInterfaceList() {
        if (this.mInterfaces == null) {
            int interfaceCount = 0;
            for (UsbConfiguration configuration : this.mConfigurations) {
                interfaceCount += configuration.getInterfaceCount();
            }
            this.mInterfaces = new UsbInterface[interfaceCount];
            int i = 0;
            for (UsbConfiguration configuration2 : this.mConfigurations) {
                interfaceCount = configuration2.getInterfaceCount();
                int j = 0;
                while (j < interfaceCount) {
                    int offset = i + 1;
                    this.mInterfaces[i] = configuration2.getInterface(j);
                    j++;
                    i = offset;
                }
            }
        }
        return this.mInterfaces;
    }

    public int getInterfaceCount() {
        return getInterfaceList().length;
    }

    public UsbInterface getInterface(int index) {
        return getInterfaceList()[index];
    }

    public boolean equals(Object o) {
        if (o instanceof UsbDevice) {
            return ((UsbDevice) o).mName.equals(this.mName);
        }
        if (o instanceof String) {
            return ((String) o).equals(this.mName);
        }
        return false;
    }

    public int hashCode() {
        return this.mName.hashCode();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UsbDevice[mName=");
        stringBuilder.append(this.mName);
        stringBuilder.append(",mVendorId=");
        stringBuilder.append(this.mVendorId);
        stringBuilder.append(",mProductId=");
        stringBuilder.append(this.mProductId);
        stringBuilder.append(",mClass=");
        stringBuilder.append(this.mClass);
        stringBuilder.append(",mSubclass=");
        stringBuilder.append(this.mSubclass);
        stringBuilder.append(",mProtocol=");
        stringBuilder.append(this.mProtocol);
        stringBuilder.append(",mManufacturerName=");
        stringBuilder.append(this.mManufacturerName);
        stringBuilder.append(",mProductName=");
        stringBuilder.append(this.mProductName);
        stringBuilder.append(",mVersion=");
        stringBuilder.append(this.mVersion);
        stringBuilder.append(",mSerialNumberReader=");
        stringBuilder.append(this.mSerialNumberReader);
        stringBuilder.append(",mConfigurations=[");
        StringBuilder builder = new StringBuilder(stringBuilder.toString());
        for (UsbConfiguration usbConfiguration : this.mConfigurations) {
            builder.append("\n");
            builder.append(usbConfiguration.toString());
        }
        builder.append("]");
        return builder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mName);
        parcel.writeInt(this.mVendorId);
        parcel.writeInt(this.mProductId);
        parcel.writeInt(this.mClass);
        parcel.writeInt(this.mSubclass);
        parcel.writeInt(this.mProtocol);
        parcel.writeString(this.mManufacturerName);
        parcel.writeString(this.mProductName);
        parcel.writeString(this.mVersion);
        parcel.writeStrongBinder(this.mSerialNumberReader.asBinder());
        parcel.writeParcelableArray(this.mConfigurations, 0);
    }

    public static int getDeviceId(String name) {
        return native_get_device_id(name);
    }

    public static String getDeviceName(int id) {
        return native_get_device_name(id);
    }
}
