package android.hardware.usb;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import java.util.List;

public interface IUsbManager extends IInterface {

    public static class Default implements IUsbManager {
        public void getDeviceList(Bundle devices) throws RemoteException {
        }

        public ParcelFileDescriptor openDevice(String deviceName, String packageName) throws RemoteException {
            return null;
        }

        public UsbAccessory getCurrentAccessory() throws RemoteException {
            return null;
        }

        public ParcelFileDescriptor openAccessory(UsbAccessory accessory) throws RemoteException {
            return null;
        }

        public void setDevicePackage(UsbDevice device, String packageName, int userId) throws RemoteException {
        }

        public void setAccessoryPackage(UsbAccessory accessory, String packageName, int userId) throws RemoteException {
        }

        public boolean hasDevicePermission(UsbDevice device, String packageName) throws RemoteException {
            return false;
        }

        public boolean hasAccessoryPermission(UsbAccessory accessory) throws RemoteException {
            return false;
        }

        public void requestDevicePermission(UsbDevice device, String packageName, PendingIntent pi) throws RemoteException {
        }

        public void requestAccessoryPermission(UsbAccessory accessory, String packageName, PendingIntent pi) throws RemoteException {
        }

        public void grantDevicePermission(UsbDevice device, int uid) throws RemoteException {
        }

        public void grantAccessoryPermission(UsbAccessory accessory, int uid) throws RemoteException {
        }

        public boolean hasDefaults(String packageName, int userId) throws RemoteException {
            return false;
        }

        public void clearDefaults(String packageName, int userId) throws RemoteException {
        }

        public boolean isFunctionEnabled(String function) throws RemoteException {
            return false;
        }

        public void setCurrentFunctions(long functions) throws RemoteException {
        }

        public void setCurrentFunction(String function, boolean usbDataUnlocked) throws RemoteException {
        }

        public long getCurrentFunctions() throws RemoteException {
            return 0;
        }

        public void setScreenUnlockedFunctions(long functions) throws RemoteException {
        }

        public long getScreenUnlockedFunctions() throws RemoteException {
            return 0;
        }

        public ParcelFileDescriptor getControlFd(long function) throws RemoteException {
            return null;
        }

        public List<ParcelableUsbPort> getPorts() throws RemoteException {
            return null;
        }

        public UsbPortStatus getPortStatus(String portId) throws RemoteException {
            return null;
        }

        public void setPortRoles(String portId, int powerRole, int dataRole) throws RemoteException {
        }

        public void enableContaminantDetection(String portId, boolean enable) throws RemoteException {
        }

        public void setUsbDeviceConnectionHandler(ComponentName usbDeviceConnectionHandler) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IUsbManager {
        private static final String DESCRIPTOR = "android.hardware.usb.IUsbManager";
        static final int TRANSACTION_clearDefaults = 14;
        static final int TRANSACTION_enableContaminantDetection = 25;
        static final int TRANSACTION_getControlFd = 21;
        static final int TRANSACTION_getCurrentAccessory = 3;
        static final int TRANSACTION_getCurrentFunctions = 18;
        static final int TRANSACTION_getDeviceList = 1;
        static final int TRANSACTION_getPortStatus = 23;
        static final int TRANSACTION_getPorts = 22;
        static final int TRANSACTION_getScreenUnlockedFunctions = 20;
        static final int TRANSACTION_grantAccessoryPermission = 12;
        static final int TRANSACTION_grantDevicePermission = 11;
        static final int TRANSACTION_hasAccessoryPermission = 8;
        static final int TRANSACTION_hasDefaults = 13;
        static final int TRANSACTION_hasDevicePermission = 7;
        static final int TRANSACTION_isFunctionEnabled = 15;
        static final int TRANSACTION_openAccessory = 4;
        static final int TRANSACTION_openDevice = 2;
        static final int TRANSACTION_requestAccessoryPermission = 10;
        static final int TRANSACTION_requestDevicePermission = 9;
        static final int TRANSACTION_setAccessoryPackage = 6;
        static final int TRANSACTION_setCurrentFunction = 17;
        static final int TRANSACTION_setCurrentFunctions = 16;
        static final int TRANSACTION_setDevicePackage = 5;
        static final int TRANSACTION_setPortRoles = 24;
        static final int TRANSACTION_setScreenUnlockedFunctions = 19;
        static final int TRANSACTION_setUsbDeviceConnectionHandler = 26;

        private static class Proxy implements IUsbManager {
            public static IUsbManager sDefaultImpl;
            private IBinder mRemote;

            Proxy(IBinder remote) {
                this.mRemote = remote;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void getDeviceList(Bundle devices) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            devices.readFromParcel(_reply);
                        }
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().getDeviceList(devices);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor openDevice(String deviceName, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(deviceName);
                    _data.writeString(packageName);
                    ParcelFileDescriptor parcelFileDescriptor = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().openDevice(deviceName, packageName);
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UsbAccessory getCurrentAccessory() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    UsbAccessory usbAccessory = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        usbAccessory = Stub.getDefaultImpl();
                        if (usbAccessory != 0) {
                            usbAccessory = Stub.getDefaultImpl().getCurrentAccessory();
                            return usbAccessory;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        usbAccessory = (UsbAccessory) UsbAccessory.CREATOR.createFromParcel(_reply);
                    } else {
                        usbAccessory = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return usbAccessory;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor openAccessory(UsbAccessory accessory) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accessory != null) {
                        _data.writeInt(1);
                        accessory.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    ParcelFileDescriptor parcelFileDescriptor = this.mRemote;
                    if (!parcelFileDescriptor.transact(4, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != null) {
                            parcelFileDescriptor = Stub.getDefaultImpl().openAccessory(accessory);
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDevicePackage(UsbDevice device, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDevicePackage(device, packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAccessoryPackage(UsbAccessory accessory, String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accessory != null) {
                        _data.writeInt(1);
                        accessory.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAccessoryPackage(accessory, packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasDevicePermission(UsbDevice device, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().hasDevicePermission(device, packageName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasAccessoryPermission(UsbAccessory accessory) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (accessory != null) {
                        _data.writeInt(1);
                        accessory.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().hasAccessoryPermission(accessory);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestDevicePermission(UsbDevice device, String packageName, PendingIntent pi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (pi != null) {
                        _data.writeInt(1);
                        pi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestDevicePermission(device, packageName, pi);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestAccessoryPermission(UsbAccessory accessory, String packageName, PendingIntent pi) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accessory != null) {
                        _data.writeInt(1);
                        accessory.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (pi != null) {
                        _data.writeInt(1);
                        pi.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestAccessoryPermission(accessory, packageName, pi);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantDevicePermission(UsbDevice device, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantDevicePermission(device, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantAccessoryPermission(UsbAccessory accessory, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (accessory != null) {
                        _data.writeInt(1);
                        accessory.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantAccessoryPermission(accessory, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasDefaults(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasDefaults(packageName, userId);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearDefaults(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearDefaults(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isFunctionEnabled(String function) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(function);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isFunctionEnabled(function);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCurrentFunctions(long functions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(functions);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCurrentFunctions(functions);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCurrentFunction(String function, boolean usbDataUnlocked) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(function);
                    _data.writeInt(usbDataUnlocked ? 1 : 0);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setCurrentFunction(function, usbDataUnlocked);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getCurrentFunctions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 18;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getCurrentFunctions();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setScreenUnlockedFunctions(long functions) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(functions);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setScreenUnlockedFunctions(functions);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getScreenUnlockedFunctions() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 20;
                    if (!this.mRemote.transact(20, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getScreenUnlockedFunctions();
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor getControlFd(long function) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(function);
                    ParcelFileDescriptor parcelFileDescriptor = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().getControlFd(function);
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ParcelableUsbPort> getPorts() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<ParcelableUsbPort> list = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPorts();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ParcelableUsbPort.CREATOR);
                    List<ParcelableUsbPort> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UsbPortStatus getPortStatus(String portId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(portId);
                    UsbPortStatus usbPortStatus = 23;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        usbPortStatus = Stub.getDefaultImpl();
                        if (usbPortStatus != 0) {
                            usbPortStatus = Stub.getDefaultImpl().getPortStatus(portId);
                            return usbPortStatus;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        usbPortStatus = (UsbPortStatus) UsbPortStatus.CREATOR.createFromParcel(_reply);
                    } else {
                        usbPortStatus = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return usbPortStatus;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPortRoles(String portId, int powerRole, int dataRole) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(portId);
                    _data.writeInt(powerRole);
                    _data.writeInt(dataRole);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPortRoles(portId, powerRole, dataRole);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableContaminantDetection(String portId, boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(portId);
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableContaminantDetection(portId, enable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setUsbDeviceConnectionHandler(ComponentName usbDeviceConnectionHandler) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (usbDeviceConnectionHandler != null) {
                        _data.writeInt(1);
                        usbDeviceConnectionHandler.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setUsbDeviceConnectionHandler(usbDeviceConnectionHandler);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IUsbManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IUsbManager)) {
                return new Proxy(obj);
            }
            return (IUsbManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getDeviceList";
                case 2:
                    return "openDevice";
                case 3:
                    return "getCurrentAccessory";
                case 4:
                    return "openAccessory";
                case 5:
                    return "setDevicePackage";
                case 6:
                    return "setAccessoryPackage";
                case 7:
                    return "hasDevicePermission";
                case 8:
                    return "hasAccessoryPermission";
                case 9:
                    return "requestDevicePermission";
                case 10:
                    return "requestAccessoryPermission";
                case 11:
                    return "grantDevicePermission";
                case 12:
                    return "grantAccessoryPermission";
                case 13:
                    return "hasDefaults";
                case 14:
                    return "clearDefaults";
                case 15:
                    return "isFunctionEnabled";
                case 16:
                    return "setCurrentFunctions";
                case 17:
                    return "setCurrentFunction";
                case 18:
                    return "getCurrentFunctions";
                case 19:
                    return "setScreenUnlockedFunctions";
                case 20:
                    return "getScreenUnlockedFunctions";
                case 21:
                    return "getControlFd";
                case 22:
                    return "getPorts";
                case 23:
                    return "getPortStatus";
                case 24:
                    return "setPortRoles";
                case 25:
                    return "enableContaminantDetection";
                case 26:
                    return "setUsbDeviceConnectionHandler";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                boolean _arg1 = false;
                ParcelFileDescriptor _result;
                UsbAccessory _result2;
                UsbDevice _arg0;
                UsbAccessory _arg02;
                boolean _result3;
                boolean _result4;
                String _arg12;
                PendingIntent _arg2;
                long _result5;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        Bundle _arg03 = new Bundle();
                        getDeviceList(_arg03);
                        reply.writeNoException();
                        reply.writeInt(1);
                        _arg03.writeToParcel(reply, 1);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = openDevice(data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result2 = getCurrentAccessory();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result2 = (UsbAccessory) UsbAccessory.CREATOR.createFromParcel(data);
                        } else {
                            _result2 = null;
                        }
                        ParcelFileDescriptor _result6 = openAccessory(_result2);
                        reply.writeNoException();
                        if (_result6 != null) {
                            reply.writeInt(1);
                            _result6.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (UsbDevice) UsbDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        setDevicePackage(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (UsbAccessory) UsbAccessory.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        setAccessoryPackage(_arg02, data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (UsbDevice) UsbDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result3 = hasDevicePermission(_arg0, data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (UsbAccessory) UsbAccessory.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result4 = hasAccessoryPermission(_arg02);
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (UsbDevice) UsbDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        requestDevicePermission(_arg0, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (UsbAccessory) UsbAccessory.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        requestAccessoryPermission(_arg02, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (UsbDevice) UsbDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        grantDevicePermission(_arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (UsbAccessory) UsbAccessory.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        grantAccessoryPermission(_arg02, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _result3 = hasDefaults(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        clearDefaults(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        _result4 = isFunctionEnabled(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        setCurrentFunctions(data.readLong());
                        reply.writeNoException();
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        setCurrentFunction(_arg12, _arg1);
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        _result5 = getCurrentFunctions();
                        reply.writeNoException();
                        reply.writeLong(_result5);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        setScreenUnlockedFunctions(data.readLong());
                        reply.writeNoException();
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        _result5 = getScreenUnlockedFunctions();
                        reply.writeNoException();
                        reply.writeLong(_result5);
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        _result = getControlFd(data.readLong());
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        List<ParcelableUsbPort> _result7 = getPorts();
                        reply.writeNoException();
                        reply.writeTypedList(_result7);
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        UsbPortStatus _result8 = getPortStatus(data.readString());
                        reply.writeNoException();
                        if (_result8 != null) {
                            reply.writeInt(1);
                            _result8.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        setPortRoles(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        enableContaminantDetection(_arg12, _arg1);
                        reply.writeNoException();
                        return true;
                    case 26:
                        ComponentName _arg04;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg04 = null;
                        }
                        setUsbDeviceConnectionHandler(_arg04);
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IUsbManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IUsbManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void clearDefaults(String str, int i) throws RemoteException;

    void enableContaminantDetection(String str, boolean z) throws RemoteException;

    ParcelFileDescriptor getControlFd(long j) throws RemoteException;

    UsbAccessory getCurrentAccessory() throws RemoteException;

    long getCurrentFunctions() throws RemoteException;

    void getDeviceList(Bundle bundle) throws RemoteException;

    UsbPortStatus getPortStatus(String str) throws RemoteException;

    List<ParcelableUsbPort> getPorts() throws RemoteException;

    long getScreenUnlockedFunctions() throws RemoteException;

    void grantAccessoryPermission(UsbAccessory usbAccessory, int i) throws RemoteException;

    void grantDevicePermission(UsbDevice usbDevice, int i) throws RemoteException;

    boolean hasAccessoryPermission(UsbAccessory usbAccessory) throws RemoteException;

    boolean hasDefaults(String str, int i) throws RemoteException;

    boolean hasDevicePermission(UsbDevice usbDevice, String str) throws RemoteException;

    boolean isFunctionEnabled(String str) throws RemoteException;

    ParcelFileDescriptor openAccessory(UsbAccessory usbAccessory) throws RemoteException;

    ParcelFileDescriptor openDevice(String str, String str2) throws RemoteException;

    void requestAccessoryPermission(UsbAccessory usbAccessory, String str, PendingIntent pendingIntent) throws RemoteException;

    void requestDevicePermission(UsbDevice usbDevice, String str, PendingIntent pendingIntent) throws RemoteException;

    void setAccessoryPackage(UsbAccessory usbAccessory, String str, int i) throws RemoteException;

    void setCurrentFunction(String str, boolean z) throws RemoteException;

    void setCurrentFunctions(long j) throws RemoteException;

    void setDevicePackage(UsbDevice usbDevice, String str, int i) throws RemoteException;

    void setPortRoles(String str, int i, int i2) throws RemoteException;

    void setScreenUnlockedFunctions(long j) throws RemoteException;

    void setUsbDeviceConnectionHandler(ComponentName componentName) throws RemoteException;
}
