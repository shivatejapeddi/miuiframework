package android.bluetooth.le;

import android.os.Parcel;
import android.os.ParcelUuid;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.ArrayMap;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class AdvertiseData implements Parcelable {
    public static final Creator<AdvertiseData> CREATOR = new Creator<AdvertiseData>() {
        public AdvertiseData[] newArray(int size) {
            return new AdvertiseData[size];
        }

        public AdvertiseData createFromParcel(Parcel in) {
            int i;
            Builder builder = new Builder();
            Iterator it = in.createTypedArrayList(ParcelUuid.CREATOR).iterator();
            while (it.hasNext()) {
                builder.addServiceUuid((ParcelUuid) it.next());
            }
            int manufacturerSize = in.readInt();
            for (i = 0; i < manufacturerSize; i++) {
                builder.addManufacturerData(in.readInt(), in.createByteArray());
            }
            i = in.readInt();
            for (int i2 = 0; i2 < i; i2++) {
                builder.addServiceData((ParcelUuid) in.readTypedObject(ParcelUuid.CREATOR), in.createByteArray());
            }
            boolean z = false;
            builder.setIncludeTxPowerLevel(in.readByte() == (byte) 1);
            if (in.readByte() == (byte) 1) {
                z = true;
            }
            builder.setIncludeDeviceName(z);
            if (in.readInt() > 0) {
                builder.addTransportDiscoveryData(in.createByteArray());
            }
            return builder.build();
        }
    };
    private final boolean mIncludeDeviceName;
    private final boolean mIncludeTxPowerLevel;
    private final SparseArray<byte[]> mManufacturerSpecificData;
    private final Map<ParcelUuid, byte[]> mServiceData;
    private final List<ParcelUuid> mServiceUuids;
    private final byte[] mTransportDiscoveryData;

    public static final class Builder {
        private boolean mIncludeDeviceName;
        private boolean mIncludeTxPowerLevel;
        private SparseArray<byte[]> mManufacturerSpecificData = new SparseArray();
        private Map<ParcelUuid, byte[]> mServiceData = new ArrayMap();
        private List<ParcelUuid> mServiceUuids = new ArrayList();
        private byte[] mTransportDiscoveryData;

        public Builder addServiceUuid(ParcelUuid serviceUuid) {
            if (serviceUuid != null) {
                this.mServiceUuids.add(serviceUuid);
                return this;
            }
            throw new IllegalArgumentException("serivceUuids are null");
        }

        public Builder addServiceData(ParcelUuid serviceDataUuid, byte[] serviceData) {
            if (serviceDataUuid == null || serviceData == null) {
                throw new IllegalArgumentException("serviceDataUuid or serviceDataUuid is null");
            }
            this.mServiceData.put(serviceDataUuid, serviceData);
            return this;
        }

        public Builder addManufacturerData(int manufacturerId, byte[] manufacturerSpecificData) {
            if (manufacturerId < 0) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("invalid manufacturerId - ");
                stringBuilder.append(manufacturerId);
                throw new IllegalArgumentException(stringBuilder.toString());
            } else if (manufacturerSpecificData != null) {
                this.mManufacturerSpecificData.put(manufacturerId, manufacturerSpecificData);
                return this;
            } else {
                throw new IllegalArgumentException("manufacturerSpecificData is null");
            }
        }

        public Builder setIncludeTxPowerLevel(boolean includeTxPowerLevel) {
            this.mIncludeTxPowerLevel = includeTxPowerLevel;
            return this;
        }

        public Builder setIncludeDeviceName(boolean includeDeviceName) {
            this.mIncludeDeviceName = includeDeviceName;
            return this;
        }

        public Builder addTransportDiscoveryData(byte[] transportDiscoveryData) {
            if (transportDiscoveryData == null || transportDiscoveryData.length == 0) {
                throw new IllegalArgumentException("transportDiscoveryData is null");
            }
            this.mTransportDiscoveryData = transportDiscoveryData;
            return this;
        }

        public AdvertiseData build() {
            return new AdvertiseData(this.mServiceUuids, this.mManufacturerSpecificData, this.mServiceData, this.mIncludeTxPowerLevel, this.mIncludeDeviceName, this.mTransportDiscoveryData, null);
        }
    }

    /* synthetic */ AdvertiseData(List x0, SparseArray x1, Map x2, boolean x3, boolean x4, byte[] x5, AnonymousClass1 x6) {
        this(x0, x1, x2, x3, x4, x5);
    }

    private AdvertiseData(List<ParcelUuid> serviceUuids, SparseArray<byte[]> manufacturerData, Map<ParcelUuid, byte[]> serviceData, boolean includeTxPowerLevel, boolean includeDeviceName, byte[] transportDiscoveryData) {
        this.mServiceUuids = serviceUuids;
        this.mManufacturerSpecificData = manufacturerData;
        this.mServiceData = serviceData;
        this.mIncludeTxPowerLevel = includeTxPowerLevel;
        this.mIncludeDeviceName = includeDeviceName;
        this.mTransportDiscoveryData = transportDiscoveryData;
    }

    public List<ParcelUuid> getServiceUuids() {
        return this.mServiceUuids;
    }

    public SparseArray<byte[]> getManufacturerSpecificData() {
        return this.mManufacturerSpecificData;
    }

    public Map<ParcelUuid, byte[]> getServiceData() {
        return this.mServiceData;
    }

    public boolean getIncludeTxPowerLevel() {
        return this.mIncludeTxPowerLevel;
    }

    public boolean getIncludeDeviceName() {
        return this.mIncludeDeviceName;
    }

    public byte[] getTransportDiscoveryData() {
        return this.mTransportDiscoveryData;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.mServiceUuids, this.mManufacturerSpecificData, this.mServiceData, Boolean.valueOf(this.mIncludeDeviceName), Boolean.valueOf(this.mIncludeTxPowerLevel), this.mTransportDiscoveryData});
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AdvertiseData other = (AdvertiseData) obj;
        if (!(Objects.equals(this.mServiceUuids, other.mServiceUuids) && BluetoothLeUtils.equals(this.mManufacturerSpecificData, other.mManufacturerSpecificData) && BluetoothLeUtils.equals(this.mServiceData, other.mServiceData) && this.mIncludeDeviceName == other.mIncludeDeviceName && this.mIncludeTxPowerLevel == other.mIncludeTxPowerLevel && BluetoothLeUtils.equals(this.mTransportDiscoveryData, other.mTransportDiscoveryData))) {
            z = false;
        }
        return z;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("AdvertiseData [mServiceUuids=");
        stringBuilder.append(this.mServiceUuids);
        stringBuilder.append(", mManufacturerSpecificData=");
        stringBuilder.append(BluetoothLeUtils.toString(this.mManufacturerSpecificData));
        stringBuilder.append(", mServiceData=");
        stringBuilder.append(BluetoothLeUtils.toString(this.mServiceData));
        stringBuilder.append(", mIncludeTxPowerLevel=");
        stringBuilder.append(this.mIncludeTxPowerLevel);
        stringBuilder.append(", mIncludeDeviceName=");
        stringBuilder.append(this.mIncludeDeviceName);
        stringBuilder.append(", mTransportDiscoveryData=");
        stringBuilder.append(BluetoothLeUtils.toString(this.mTransportDiscoveryData));
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        List list = this.mServiceUuids;
        dest.writeTypedArray((ParcelUuid[]) list.toArray(new ParcelUuid[list.size()]), flags);
        dest.writeInt(this.mManufacturerSpecificData.size());
        for (int i = 0; i < this.mManufacturerSpecificData.size(); i++) {
            dest.writeInt(this.mManufacturerSpecificData.keyAt(i));
            dest.writeByteArray((byte[]) this.mManufacturerSpecificData.valueAt(i));
        }
        dest.writeInt(this.mServiceData.size());
        for (ParcelUuid uuid : this.mServiceData.keySet()) {
            dest.writeTypedObject(uuid, flags);
            dest.writeByteArray((byte[]) this.mServiceData.get(uuid));
        }
        dest.writeByte((byte) getIncludeTxPowerLevel());
        dest.writeByte((byte) getIncludeDeviceName());
        byte[] bArr = this.mTransportDiscoveryData;
        dest.writeInt(bArr != null ? bArr.length : 0);
        bArr = this.mTransportDiscoveryData;
        if (bArr != null) {
            dest.writeByteArray(bArr);
        }
    }
}
