package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IBluetoothSap extends IInterface {

    public static class Default implements IBluetoothSap {
        public int getState() throws RemoteException {
            return 0;
        }

        public BluetoothDevice getClient() throws RemoteException {
            return null;
        }

        public boolean connect(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean disconnect(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean isConnected(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public List<BluetoothDevice> getConnectedDevices() throws RemoteException {
            return null;
        }

        public List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] states) throws RemoteException {
            return null;
        }

        public int getConnectionState(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public boolean setPriority(BluetoothDevice device, int priority) throws RemoteException {
            return false;
        }

        public int getPriority(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBluetoothSap {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothSap";
        static final int TRANSACTION_connect = 3;
        static final int TRANSACTION_disconnect = 4;
        static final int TRANSACTION_getClient = 2;
        static final int TRANSACTION_getConnectedDevices = 6;
        static final int TRANSACTION_getConnectionState = 8;
        static final int TRANSACTION_getDevicesMatchingConnectionStates = 7;
        static final int TRANSACTION_getPriority = 10;
        static final int TRANSACTION_getState = 1;
        static final int TRANSACTION_isConnected = 5;
        static final int TRANSACTION_setPriority = 9;

        private static class Proxy implements IBluetoothSap {
            public static IBluetoothSap sDefaultImpl;
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

            public int getState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getState();
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

            public BluetoothDevice getClient() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    BluetoothDevice bluetoothDevice = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        bluetoothDevice = Stub.getDefaultImpl();
                        if (bluetoothDevice != 0) {
                            bluetoothDevice = Stub.getDefaultImpl().getClient();
                            return bluetoothDevice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bluetoothDevice = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(_reply);
                    } else {
                        bluetoothDevice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bluetoothDevice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean connect(BluetoothDevice device) throws RemoteException {
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
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().connect(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
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
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public boolean isConnected(BluetoothDevice device) throws RemoteException {
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
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isConnected(device);
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
                    List<BluetoothDevice> list = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
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
                    List<BluetoothDevice> list = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
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
                    if (!i.transact(8, _data, _reply, 0)) {
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

            public boolean setPriority(BluetoothDevice device, int priority) throws RemoteException {
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
                    _data.writeInt(priority);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setPriority(device, priority);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPriority(BluetoothDevice device) throws RemoteException {
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
                    if (!i.transact(10, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getPriority(device);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothSap asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothSap)) {
                return new Proxy(obj);
            }
            return (IBluetoothSap) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getState";
                case 2:
                    return "getClient";
                case 3:
                    return "connect";
                case 4:
                    return "disconnect";
                case 5:
                    return "isConnected";
                case 6:
                    return "getConnectedDevices";
                case 7:
                    return "getDevicesMatchingConnectionStates";
                case 8:
                    return "getConnectionState";
                case 9:
                    return "setPriority";
                case 10:
                    return "getPriority";
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
                BluetoothDevice _result;
                boolean _result2;
                int _result3;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        int _result4 = getState();
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = getClient();
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
                        if (data.readInt() != 0) {
                            _result = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _result = null;
                        }
                        _result2 = connect(_result);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _result = null;
                        }
                        _result2 = disconnect(_result);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _result = null;
                        }
                        _result2 = isConnected(_result);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        List<BluetoothDevice> _result5 = getConnectedDevices();
                        reply.writeNoException();
                        reply.writeTypedList(_result5);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        List<BluetoothDevice> _result6 = getDevicesMatchingConnectionStates(data.createIntArray());
                        reply.writeNoException();
                        reply.writeTypedList(_result6);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _result = null;
                        }
                        _result3 = getConnectionState(_result);
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _result = null;
                        }
                        boolean _result7 = setPriority(_result, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result7);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _result = null;
                        }
                        _result3 = getPriority(_result);
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBluetoothSap impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothSap getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean connect(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean disconnect(BluetoothDevice bluetoothDevice) throws RemoteException;

    BluetoothDevice getClient() throws RemoteException;

    List<BluetoothDevice> getConnectedDevices() throws RemoteException;

    int getConnectionState(BluetoothDevice bluetoothDevice) throws RemoteException;

    List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] iArr) throws RemoteException;

    int getPriority(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getState() throws RemoteException;

    boolean isConnected(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean setPriority(BluetoothDevice bluetoothDevice, int i) throws RemoteException;
}
