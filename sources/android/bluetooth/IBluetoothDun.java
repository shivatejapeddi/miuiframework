package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IBluetoothDun extends IInterface {

    public static class Default implements IBluetoothDun {
        public boolean disconnect(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public int getConnectionState(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public List<BluetoothDevice> getConnectedDevices() throws RemoteException {
            return null;
        }

        public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBluetoothDun {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothDun";
        static final int TRANSACTION_disconnect = 1;
        static final int TRANSACTION_getConnectedDevices = 3;
        static final int TRANSACTION_getConnectionState = 2;
        static final int TRANSACTION_getDevicesMatchingConnectionStates = 4;

        private static class Proxy implements IBluetoothDun {
            public static IBluetoothDun sDefaultImpl;
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

            public boolean disconnect(BluetoothDevice device) throws RemoteException {
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
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().disconnect(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getConnectionState(BluetoothDevice device) throws RemoteException {
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
                    int i = this.mRemote;
                    if (!i.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getConnectionState(device);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<BluetoothDevice> getConnectedDevices() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<BluetoothDevice> list = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getConnectedDevices();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(BluetoothDevice.CREATOR);
                    List<BluetoothDevice> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(states);
                    List<BluetoothDevice> list = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getDevicesMatchingConnectionStates(states);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(BluetoothDevice.CREATOR);
                    List<BluetoothDevice> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothDun asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothDun)) {
                return new Proxy(obj);
            }
            return (IBluetoothDun) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "disconnect";
            }
            if (transactionCode == 2) {
                return "getConnectionState";
            }
            if (transactionCode == 3) {
                return "getConnectedDevices";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "getDevicesMatchingConnectionStates";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            BluetoothDevice _arg0;
            if (code == 1) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                boolean _result = disconnect(_arg0);
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                int _result2 = getConnectionState(_arg0);
                reply.writeNoException();
                reply.writeInt(_result2);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                List<BluetoothDevice> _result3 = getConnectedDevices();
                reply.writeNoException();
                reply.writeTypedList(_result3);
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                List<BluetoothDevice> _result4 = getDevicesMatchingConnectionStates(data.createIntArray());
                reply.writeNoException();
                reply.writeTypedList(_result4);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IBluetoothDun impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothDun getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean disconnect(BluetoothDevice bluetoothDevice) throws RemoteException;

    List<BluetoothDevice> getConnectedDevices() throws RemoteException;

    int getConnectionState(BluetoothDevice bluetoothDevice) throws RemoteException;

    List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] iArr) throws RemoteException;
}
