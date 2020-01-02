package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IBluetoothGattCallback extends IInterface {

    public static abstract class Stub extends Binder implements IBluetoothGattCallback {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothGattCallback";
        static final int TRANSACTION_onCharacteristicRead = 6;
        static final int TRANSACTION_onCharacteristicWrite = 7;
        static final int TRANSACTION_onClientConnectionState = 2;
        static final int TRANSACTION_onClientRegistered = 1;
        static final int TRANSACTION_onConfigureMTU = 13;
        static final int TRANSACTION_onConnectionUpdated = 14;
        static final int TRANSACTION_onDescriptorRead = 9;
        static final int TRANSACTION_onDescriptorWrite = 10;
        static final int TRANSACTION_onExecuteWrite = 8;
        static final int TRANSACTION_onNotify = 11;
        static final int TRANSACTION_onPhyRead = 4;
        static final int TRANSACTION_onPhyUpdate = 3;
        static final int TRANSACTION_onReadRemoteRssi = 12;
        static final int TRANSACTION_onSearchComplete = 5;

        private static class Proxy implements IBluetoothGattCallback {
            public static IBluetoothGattCallback sDefaultImpl;
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

            public void onClientRegistered(int status, int clientIf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(clientIf);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onClientRegistered(status, clientIf);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onClientConnectionState(int status, int clientIf, boolean connected, String address) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    _data.writeInt(clientIf);
                    _data.writeInt(connected ? 1 : 0);
                    _data.writeString(address);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onClientConnectionState(status, clientIf, connected, address);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPhyUpdate(String address, int txPhy, int rxPhy, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(txPhy);
                    _data.writeInt(rxPhy);
                    _data.writeInt(status);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPhyUpdate(address, txPhy, rxPhy, status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPhyRead(String address, int txPhy, int rxPhy, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(txPhy);
                    _data.writeInt(rxPhy);
                    _data.writeInt(status);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPhyRead(address, txPhy, rxPhy, status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSearchComplete(String address, List<BluetoothGattService> services, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeTypedList(services);
                    _data.writeInt(status);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSearchComplete(address, services, status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCharacteristicRead(String address, int status, int handle, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    _data.writeInt(handle);
                    _data.writeByteArray(value);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCharacteristicRead(address, status, handle, value);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onCharacteristicWrite(String address, int status, int handle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    _data.writeInt(handle);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onCharacteristicWrite(address, status, handle);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onExecuteWrite(String address, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onExecuteWrite(address, status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDescriptorRead(String address, int status, int handle, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    _data.writeInt(handle);
                    _data.writeByteArray(value);
                    if (this.mRemote.transact(9, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDescriptorRead(address, status, handle, value);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDescriptorWrite(String address, int status, int handle) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(status);
                    _data.writeInt(handle);
                    if (this.mRemote.transact(10, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDescriptorWrite(address, status, handle);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onNotify(String address, int handle, byte[] value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(handle);
                    _data.writeByteArray(value);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onNotify(address, handle, value);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onReadRemoteRssi(String address, int rssi, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(rssi);
                    _data.writeInt(status);
                    if (this.mRemote.transact(12, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onReadRemoteRssi(address, rssi, status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onConfigureMTU(String address, int mtu, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(mtu);
                    _data.writeInt(status);
                    if (this.mRemote.transact(13, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onConfigureMTU(address, mtu, status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onConnectionUpdated(String address, int interval, int latency, int timeout, int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(address);
                    _data.writeInt(interval);
                    _data.writeInt(latency);
                    _data.writeInt(timeout);
                    _data.writeInt(status);
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onConnectionUpdated(address, interval, latency, timeout, status);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothGattCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothGattCallback)) {
                return new Proxy(obj);
            }
            return (IBluetoothGattCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onClientRegistered";
                case 2:
                    return "onClientConnectionState";
                case 3:
                    return "onPhyUpdate";
                case 4:
                    return "onPhyRead";
                case 5:
                    return "onSearchComplete";
                case 6:
                    return "onCharacteristicRead";
                case 7:
                    return "onCharacteristicWrite";
                case 8:
                    return "onExecuteWrite";
                case 9:
                    return "onDescriptorRead";
                case 10:
                    return "onDescriptorWrite";
                case 11:
                    return "onNotify";
                case 12:
                    return "onReadRemoteRssi";
                case 13:
                    return "onConfigureMTU";
                case 14:
                    return "onConnectionUpdated";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        onClientRegistered(data.readInt(), data.readInt());
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        onClientConnectionState(data.readInt(), data.readInt(), data.readInt() != 0, data.readString());
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        onPhyUpdate(data.readString(), data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        onPhyRead(data.readString(), data.readInt(), data.readInt(), data.readInt());
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        onSearchComplete(data.readString(), parcel.createTypedArrayList(BluetoothGattService.CREATOR), data.readInt());
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        onCharacteristicRead(data.readString(), data.readInt(), data.readInt(), data.createByteArray());
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        onCharacteristicWrite(data.readString(), data.readInt(), data.readInt());
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        onExecuteWrite(data.readString(), data.readInt());
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        onDescriptorRead(data.readString(), data.readInt(), data.readInt(), data.createByteArray());
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        onDescriptorWrite(data.readString(), data.readInt(), data.readInt());
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        onNotify(data.readString(), data.readInt(), data.createByteArray());
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        onReadRemoteRssi(data.readString(), data.readInt(), data.readInt());
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        onConfigureMTU(data.readString(), data.readInt(), data.readInt());
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        onConnectionUpdated(data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBluetoothGattCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothGattCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IBluetoothGattCallback {
        public void onClientRegistered(int status, int clientIf) throws RemoteException {
        }

        public void onClientConnectionState(int status, int clientIf, boolean connected, String address) throws RemoteException {
        }

        public void onPhyUpdate(String address, int txPhy, int rxPhy, int status) throws RemoteException {
        }

        public void onPhyRead(String address, int txPhy, int rxPhy, int status) throws RemoteException {
        }

        public void onSearchComplete(String address, List<BluetoothGattService> list, int status) throws RemoteException {
        }

        public void onCharacteristicRead(String address, int status, int handle, byte[] value) throws RemoteException {
        }

        public void onCharacteristicWrite(String address, int status, int handle) throws RemoteException {
        }

        public void onExecuteWrite(String address, int status) throws RemoteException {
        }

        public void onDescriptorRead(String address, int status, int handle, byte[] value) throws RemoteException {
        }

        public void onDescriptorWrite(String address, int status, int handle) throws RemoteException {
        }

        public void onNotify(String address, int handle, byte[] value) throws RemoteException {
        }

        public void onReadRemoteRssi(String address, int rssi, int status) throws RemoteException {
        }

        public void onConfigureMTU(String address, int mtu, int status) throws RemoteException {
        }

        public void onConnectionUpdated(String address, int interval, int latency, int timeout, int status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onCharacteristicRead(String str, int i, int i2, byte[] bArr) throws RemoteException;

    void onCharacteristicWrite(String str, int i, int i2) throws RemoteException;

    void onClientConnectionState(int i, int i2, boolean z, String str) throws RemoteException;

    void onClientRegistered(int i, int i2) throws RemoteException;

    void onConfigureMTU(String str, int i, int i2) throws RemoteException;

    void onConnectionUpdated(String str, int i, int i2, int i3, int i4) throws RemoteException;

    void onDescriptorRead(String str, int i, int i2, byte[] bArr) throws RemoteException;

    void onDescriptorWrite(String str, int i, int i2) throws RemoteException;

    void onExecuteWrite(String str, int i) throws RemoteException;

    void onNotify(String str, int i, byte[] bArr) throws RemoteException;

    void onPhyRead(String str, int i, int i2, int i3) throws RemoteException;

    void onPhyUpdate(String str, int i, int i2, int i3) throws RemoteException;

    void onReadRemoteRssi(String str, int i, int i2) throws RemoteException;

    void onSearchComplete(String str, List<BluetoothGattService> list, int i) throws RemoteException;
}
