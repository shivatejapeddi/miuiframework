package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IBluetoothA2dp extends IInterface {

    public static class Default implements IBluetoothA2dp {
        public boolean connect(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public boolean disconnect(BluetoothDevice device) throws RemoteException {
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

        public boolean setActiveDevice(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public BluetoothDevice getActiveDevice() throws RemoteException {
            return null;
        }

        public boolean setPriority(BluetoothDevice device, int priority) throws RemoteException {
            return false;
        }

        public int getPriority(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public boolean isAvrcpAbsoluteVolumeSupported() throws RemoteException {
            return false;
        }

        public void setAvrcpAbsoluteVolume(int volume) throws RemoteException {
        }

        public boolean isA2dpPlaying(BluetoothDevice device) throws RemoteException {
            return false;
        }

        public BluetoothCodecStatus getCodecStatus(BluetoothDevice device) throws RemoteException {
            return null;
        }

        public void setCodecConfigPreference(BluetoothDevice device, BluetoothCodecConfig codecConfig) throws RemoteException {
        }

        public void enableOptionalCodecs(BluetoothDevice device) throws RemoteException {
        }

        public void disableOptionalCodecs(BluetoothDevice device) throws RemoteException {
        }

        public int supportsOptionalCodecs(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public int getOptionalCodecsEnabled(BluetoothDevice device) throws RemoteException {
            return 0;
        }

        public void setOptionalCodecsEnabled(BluetoothDevice device, int value) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBluetoothA2dp {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothA2dp";
        static final int TRANSACTION_connect = 1;
        static final int TRANSACTION_disableOptionalCodecs = 16;
        static final int TRANSACTION_disconnect = 2;
        static final int TRANSACTION_enableOptionalCodecs = 15;
        static final int TRANSACTION_getActiveDevice = 7;
        static final int TRANSACTION_getCodecStatus = 13;
        static final int TRANSACTION_getConnectedDevices = 3;
        static final int TRANSACTION_getConnectionState = 5;
        static final int TRANSACTION_getDevicesMatchingConnectionStates = 4;
        static final int TRANSACTION_getOptionalCodecsEnabled = 18;
        static final int TRANSACTION_getPriority = 9;
        static final int TRANSACTION_isA2dpPlaying = 12;
        static final int TRANSACTION_isAvrcpAbsoluteVolumeSupported = 10;
        static final int TRANSACTION_setActiveDevice = 6;
        static final int TRANSACTION_setAvrcpAbsoluteVolume = 11;
        static final int TRANSACTION_setCodecConfigPreference = 14;
        static final int TRANSACTION_setOptionalCodecsEnabled = 19;
        static final int TRANSACTION_setPriority = 8;
        static final int TRANSACTION_supportsOptionalCodecs = 17;

        private static class Proxy implements IBluetoothA2dp {
            public static IBluetoothA2dp sDefaultImpl;
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
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    if (!i.transact(5, _data, _reply, 0)) {
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

            public boolean setActiveDevice(BluetoothDevice device) throws RemoteException {
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
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setActiveDevice(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public BluetoothDevice getActiveDevice() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    BluetoothDevice bluetoothDevice = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        bluetoothDevice = Stub.getDefaultImpl();
                        if (bluetoothDevice != 0) {
                            bluetoothDevice = Stub.getDefaultImpl().getActiveDevice();
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
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                    if (!i.transact(9, _data, _reply, 0)) {
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

            public boolean isAvrcpAbsoluteVolumeSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAvrcpAbsoluteVolumeSupported();
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

            public void setAvrcpAbsoluteVolume(int volume) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(volume);
                    if (this.mRemote.transact(11, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setAvrcpAbsoluteVolume(volume);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public boolean isA2dpPlaying(BluetoothDevice device) throws RemoteException {
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
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isA2dpPlaying(device);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public BluetoothCodecStatus getCodecStatus(BluetoothDevice device) throws RemoteException {
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
                    BluetoothCodecStatus bluetoothCodecStatus = this.mRemote;
                    if (!bluetoothCodecStatus.transact(13, _data, _reply, 0)) {
                        bluetoothCodecStatus = Stub.getDefaultImpl();
                        if (bluetoothCodecStatus != null) {
                            bluetoothCodecStatus = Stub.getDefaultImpl().getCodecStatus(device);
                            return bluetoothCodecStatus;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bluetoothCodecStatus = (BluetoothCodecStatus) BluetoothCodecStatus.CREATOR.createFromParcel(_reply);
                    } else {
                        bluetoothCodecStatus = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bluetoothCodecStatus;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setCodecConfigPreference(BluetoothDevice device, BluetoothCodecConfig codecConfig) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (codecConfig != null) {
                        _data.writeInt(1);
                        codecConfig.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setCodecConfigPreference(device, codecConfig);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void enableOptionalCodecs(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(15, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().enableOptionalCodecs(device);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void disableOptionalCodecs(BluetoothDevice device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(16, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().disableOptionalCodecs(device);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public int supportsOptionalCodecs(BluetoothDevice device) throws RemoteException {
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
                    if (!i.transact(17, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().supportsOptionalCodecs(device);
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

            public int getOptionalCodecsEnabled(BluetoothDevice device) throws RemoteException {
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
                    if (!i.transact(18, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getOptionalCodecsEnabled(device);
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

            public void setOptionalCodecsEnabled(BluetoothDevice device, int value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (device != null) {
                        _data.writeInt(1);
                        device.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(value);
                    if (this.mRemote.transact(19, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setOptionalCodecsEnabled(device, value);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothA2dp asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothA2dp)) {
                return new Proxy(obj);
            }
            return (IBluetoothA2dp) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "connect";
                case 2:
                    return "disconnect";
                case 3:
                    return "getConnectedDevices";
                case 4:
                    return "getDevicesMatchingConnectionStates";
                case 5:
                    return "getConnectionState";
                case 6:
                    return "setActiveDevice";
                case 7:
                    return "getActiveDevice";
                case 8:
                    return "setPriority";
                case 9:
                    return "getPriority";
                case 10:
                    return "isAvrcpAbsoluteVolumeSupported";
                case 11:
                    return "setAvrcpAbsoluteVolume";
                case 12:
                    return "isA2dpPlaying";
                case 13:
                    return "getCodecStatus";
                case 14:
                    return "setCodecConfigPreference";
                case 15:
                    return "enableOptionalCodecs";
                case 16:
                    return "disableOptionalCodecs";
                case 17:
                    return "supportsOptionalCodecs";
                case 18:
                    return "getOptionalCodecsEnabled";
                case 19:
                    return "setOptionalCodecsEnabled";
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
                BluetoothDevice _arg0;
                boolean _result;
                int _result2;
                BluetoothDevice _result3;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = connect(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = disconnect(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        List<BluetoothDevice> _result4 = getConnectedDevices();
                        reply.writeNoException();
                        reply.writeTypedList(_result4);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        List<BluetoothDevice> _result5 = getDevicesMatchingConnectionStates(data.createIntArray());
                        reply.writeNoException();
                        reply.writeTypedList(_result5);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = getConnectionState(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = setActiveDevice(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result3 = getActiveDevice();
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        boolean _result6 = setPriority(_arg0, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result6);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = getPriority(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        boolean _result7 = isAvrcpAbsoluteVolumeSupported();
                        reply.writeNoException();
                        reply.writeInt(_result7);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        setAvrcpAbsoluteVolume(data.readInt());
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result = isA2dpPlaying(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result3 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _result3 = null;
                        }
                        BluetoothCodecStatus _result8 = getCodecStatus(_result3);
                        reply.writeNoException();
                        if (_result8 != null) {
                            reply.writeInt(1);
                            _result8.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 14:
                        BluetoothCodecConfig _arg1;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg1 = (BluetoothCodecConfig) BluetoothCodecConfig.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        setCodecConfigPreference(_arg0, _arg1);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        enableOptionalCodecs(_arg0);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        disableOptionalCodecs(_arg0);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = supportsOptionalCodecs(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = getOptionalCodecsEnabled(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (BluetoothDevice) BluetoothDevice.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        setOptionalCodecsEnabled(_arg0, data.readInt());
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBluetoothA2dp impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothA2dp getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean connect(BluetoothDevice bluetoothDevice) throws RemoteException;

    void disableOptionalCodecs(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean disconnect(BluetoothDevice bluetoothDevice) throws RemoteException;

    void enableOptionalCodecs(BluetoothDevice bluetoothDevice) throws RemoteException;

    BluetoothDevice getActiveDevice() throws RemoteException;

    BluetoothCodecStatus getCodecStatus(BluetoothDevice bluetoothDevice) throws RemoteException;

    List<BluetoothDevice> getConnectedDevices() throws RemoteException;

    int getConnectionState(BluetoothDevice bluetoothDevice) throws RemoteException;

    List<BluetoothDevice> getDevicesMatchingConnectionStates(int[] iArr) throws RemoteException;

    int getOptionalCodecsEnabled(BluetoothDevice bluetoothDevice) throws RemoteException;

    int getPriority(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean isA2dpPlaying(BluetoothDevice bluetoothDevice) throws RemoteException;

    boolean isAvrcpAbsoluteVolumeSupported() throws RemoteException;

    boolean setActiveDevice(BluetoothDevice bluetoothDevice) throws RemoteException;

    void setAvrcpAbsoluteVolume(int i) throws RemoteException;

    void setCodecConfigPreference(BluetoothDevice bluetoothDevice, BluetoothCodecConfig bluetoothCodecConfig) throws RemoteException;

    void setOptionalCodecsEnabled(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    boolean setPriority(BluetoothDevice bluetoothDevice, int i) throws RemoteException;

    int supportsOptionalCodecs(BluetoothDevice bluetoothDevice) throws RemoteException;
}
