package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.ParcelUuid;
import android.os.RemoteException;

public interface IBluetoothSocketManager extends IInterface {

    public static class Default implements IBluetoothSocketManager {
        public ParcelFileDescriptor connectSocket(BluetoothDevice device, int type, ParcelUuid uuid, int port, int flag) throws RemoteException {
            return null;
        }

        public ParcelFileDescriptor createSocketChannel(int type, String serviceName, ParcelUuid uuid, int port, int flag) throws RemoteException {
            return null;
        }

        public void requestMaximumTxDataLength(BluetoothDevice device) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBluetoothSocketManager {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothSocketManager";
        static final int TRANSACTION_connectSocket = 1;
        static final int TRANSACTION_createSocketChannel = 2;
        static final int TRANSACTION_requestMaximumTxDataLength = 3;

        private static class Proxy implements IBluetoothSocketManager {
            public static IBluetoothSocketManager sDefaultImpl;
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

            public ParcelFileDescriptor connectSocket(BluetoothDevice device, int type, ParcelUuid uuid, int port, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ParcelFileDescriptor parcelFileDescriptor = 0;
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(type);
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(port);
                    _data.writeInt(flag);
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().connectSocket(device, type, uuid, port, flag);
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

            public ParcelFileDescriptor createSocketChannel(int type, String serviceName, ParcelUuid uuid, int port, int flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(serviceName);
                    if (uuid != null) {
                        _data.writeInt(1);
                        uuid.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(port);
                    _data.writeInt(flag);
                    ParcelFileDescriptor parcelFileDescriptor = this.mRemote;
                    if (!parcelFileDescriptor.transact(2, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != null) {
                            parcelFileDescriptor = Stub.getDefaultImpl().createSocketChannel(type, serviceName, uuid, port, flag);
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

            public void requestMaximumTxDataLength(BluetoothDevice device) throws RemoteException {
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
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestMaximumTxDataLength(device);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothSocketManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothSocketManager)) {
                return new Proxy(obj);
            }
            return (IBluetoothSocketManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "connectSocket";
            }
            if (transactionCode == 2) {
                return "createSocketChannel";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "requestMaximumTxDataLength";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            ParcelUuid _arg2;
            ParcelFileDescriptor _result;
            if (i == 1) {
                BluetoothDevice _arg0;
                parcel.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                } else {
                    _arg0 = null;
                }
                int _arg1 = data.readInt();
                if (data.readInt() != 0) {
                    _arg2 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(parcel);
                } else {
                    _arg2 = null;
                }
                _result = connectSocket(_arg0, _arg1, _arg2, data.readInt(), data.readInt());
                reply.writeNoException();
                if (_result != null) {
                    parcel2.writeInt(1);
                    _result.writeToParcel(parcel2, 1);
                } else {
                    parcel2.writeInt(0);
                }
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(descriptor);
                int _arg02 = data.readInt();
                String _arg12 = data.readString();
                if (data.readInt() != 0) {
                    _arg2 = (ParcelUuid) ParcelUuid.CREATOR.createFromParcel(parcel);
                } else {
                    _arg2 = null;
                }
                _result = createSocketChannel(_arg02, _arg12, _arg2, data.readInt(), data.readInt());
                reply.writeNoException();
                if (_result != null) {
                    parcel2.writeInt(1);
                    _result.writeToParcel(parcel2, 1);
                } else {
                    parcel2.writeInt(0);
                }
                return true;
            } else if (i == 3) {
                BluetoothDevice _arg03;
                parcel.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg03 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(parcel);
                } else {
                    _arg03 = null;
                }
                requestMaximumTxDataLength(_arg03);
                reply.writeNoException();
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                parcel2.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IBluetoothSocketManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothSocketManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    ParcelFileDescriptor connectSocket(BluetoothDevice bluetoothDevice, int i, ParcelUuid parcelUuid, int i2, int i3) throws RemoteException;

    ParcelFileDescriptor createSocketChannel(int i, String str, ParcelUuid parcelUuid, int i2, int i3) throws RemoteException;

    void requestMaximumTxDataLength(BluetoothDevice bluetoothDevice) throws RemoteException;
}
