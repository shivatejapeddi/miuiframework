package android.bluetooth;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IBluetoothManager extends IInterface {

    public static class Default implements IBluetoothManager {
        public IBluetooth registerAdapter(IBluetoothManagerCallback callback) throws RemoteException {
            return null;
        }

        public void unregisterAdapter(IBluetoothManagerCallback callback) throws RemoteException {
        }

        public void registerStateChangeCallback(IBluetoothStateChangeCallback callback) throws RemoteException {
        }

        public void unregisterStateChangeCallback(IBluetoothStateChangeCallback callback) throws RemoteException {
        }

        public boolean isEnabled() throws RemoteException {
            return false;
        }

        public boolean enable(String packageName) throws RemoteException {
            return false;
        }

        public boolean enableNoAutoConnect(String packageName) throws RemoteException {
            return false;
        }

        public boolean disable(String packageName, boolean persist) throws RemoteException {
            return false;
        }

        public int getState() throws RemoteException {
            return 0;
        }

        public IBluetoothGatt getBluetoothGatt() throws RemoteException {
            return null;
        }

        public boolean bindBluetoothProfileService(int profile, IBluetoothProfileServiceConnection proxy) throws RemoteException {
            return false;
        }

        public void unbindBluetoothProfileService(int profile, IBluetoothProfileServiceConnection proxy) throws RemoteException {
        }

        public String getAddress() throws RemoteException {
            return null;
        }

        public String getName() throws RemoteException {
            return null;
        }

        public boolean isBleScanAlwaysAvailable() throws RemoteException {
            return false;
        }

        public int updateBleAppCount(IBinder b, boolean enable, String packageName) throws RemoteException {
            return 0;
        }

        public boolean isBleAppPresent() throws RemoteException {
            return false;
        }

        public boolean factoryReset() throws RemoteException {
            return false;
        }

        public boolean isHearingAidProfileSupported() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IBluetoothManager {
        private static final String DESCRIPTOR = "android.bluetooth.IBluetoothManager";
        static final int TRANSACTION_bindBluetoothProfileService = 11;
        static final int TRANSACTION_disable = 8;
        static final int TRANSACTION_enable = 6;
        static final int TRANSACTION_enableNoAutoConnect = 7;
        static final int TRANSACTION_factoryReset = 18;
        static final int TRANSACTION_getAddress = 13;
        static final int TRANSACTION_getBluetoothGatt = 10;
        static final int TRANSACTION_getName = 14;
        static final int TRANSACTION_getState = 9;
        static final int TRANSACTION_isBleAppPresent = 17;
        static final int TRANSACTION_isBleScanAlwaysAvailable = 15;
        static final int TRANSACTION_isEnabled = 5;
        static final int TRANSACTION_isHearingAidProfileSupported = 19;
        static final int TRANSACTION_registerAdapter = 1;
        static final int TRANSACTION_registerStateChangeCallback = 3;
        static final int TRANSACTION_unbindBluetoothProfileService = 12;
        static final int TRANSACTION_unregisterAdapter = 2;
        static final int TRANSACTION_unregisterStateChangeCallback = 4;
        static final int TRANSACTION_updateBleAppCount = 16;

        private static class Proxy implements IBluetoothManager {
            public static IBluetoothManager sDefaultImpl;
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

            public IBluetooth registerAdapter(IBluetoothManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    IBluetooth iBluetooth = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        iBluetooth = Stub.getDefaultImpl();
                        if (iBluetooth != 0) {
                            iBluetooth = Stub.getDefaultImpl().registerAdapter(callback);
                            return iBluetooth;
                        }
                    }
                    _reply.readException();
                    iBluetooth = android.bluetooth.IBluetooth.Stub.asInterface(_reply.readStrongBinder());
                    IBluetooth _result = iBluetooth;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterAdapter(IBluetoothManagerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterAdapter(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerStateChangeCallback(IBluetoothStateChangeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerStateChangeCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterStateChangeCallback(IBluetoothStateChangeCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterStateChangeCallback(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isEnabled();
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

            public boolean enable(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().enable(packageName);
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

            public boolean enableNoAutoConnect(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().enableNoAutoConnect(packageName);
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

            public boolean disable(String packageName, boolean persist) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    _data.writeInt(persist ? 1 : 0);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().disable(packageName, persist);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
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

            public IBluetoothGatt getBluetoothGatt() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    IBluetoothGatt iBluetoothGatt = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        iBluetoothGatt = Stub.getDefaultImpl();
                        if (iBluetoothGatt != 0) {
                            iBluetoothGatt = Stub.getDefaultImpl().getBluetoothGatt();
                            return iBluetoothGatt;
                        }
                    }
                    _reply.readException();
                    iBluetoothGatt = android.bluetooth.IBluetoothGatt.Stub.asInterface(_reply.readStrongBinder());
                    IBluetoothGatt _result = iBluetoothGatt;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean bindBluetoothProfileService(int profile, IBluetoothProfileServiceConnection proxy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(profile);
                    _data.writeStrongBinder(proxy != null ? proxy.asBinder() : null);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().bindBluetoothProfileService(profile, proxy);
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

            public void unbindBluetoothProfileService(int profile, IBluetoothProfileServiceConnection proxy) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(profile);
                    _data.writeStrongBinder(proxy != null ? proxy.asBinder() : null);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unbindBluetoothProfileService(profile, proxy);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getAddress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getAddress();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 14;
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getName();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isBleScanAlwaysAvailable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isBleScanAlwaysAvailable();
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

            public int updateBleAppCount(IBinder b, boolean enable, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(b);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeString(packageName);
                    int i = this.mRemote;
                    if (!i.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().updateBleAppCount(b, enable, packageName);
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

            public boolean isBleAppPresent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isBleAppPresent();
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

            public boolean factoryReset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().factoryReset();
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

            public boolean isHearingAidProfileSupported() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isHearingAidProfileSupported();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IBluetoothManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IBluetoothManager)) {
                return new Proxy(obj);
            }
            return (IBluetoothManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "registerAdapter";
                case 2:
                    return "unregisterAdapter";
                case 3:
                    return "registerStateChangeCallback";
                case 4:
                    return "unregisterStateChangeCallback";
                case 5:
                    return "isEnabled";
                case 6:
                    return "enable";
                case 7:
                    return "enableNoAutoConnect";
                case 8:
                    return "disable";
                case 9:
                    return "getState";
                case 10:
                    return "getBluetoothGatt";
                case 11:
                    return "bindBluetoothProfileService";
                case 12:
                    return "unbindBluetoothProfileService";
                case 13:
                    return "getAddress";
                case 14:
                    return "getName";
                case 15:
                    return "isBleScanAlwaysAvailable";
                case 16:
                    return "updateBleAppCount";
                case 17:
                    return "isBleAppPresent";
                case 18:
                    return "factoryReset";
                case 19:
                    return "isHearingAidProfileSupported";
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
                IBinder iBinder = null;
                boolean _result;
                boolean _result2;
                String _result3;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        IBluetooth _result4 = registerAdapter(android.bluetooth.IBluetoothManagerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        if (_result4 != null) {
                            iBinder = _result4.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        unregisterAdapter(android.bluetooth.IBluetoothManagerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        registerStateChangeCallback(android.bluetooth.IBluetoothStateChangeCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        unregisterStateChangeCallback(android.bluetooth.IBluetoothStateChangeCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg1 = isEnabled();
                        reply.writeNoException();
                        reply.writeInt(_arg1);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _result = enable(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result = enableNoAutoConnect(data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        String _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        _result2 = disable(_arg0, _arg1);
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        int _result5 = getState();
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        IBluetoothGatt _result6 = getBluetoothGatt();
                        reply.writeNoException();
                        if (_result6 != null) {
                            iBinder = _result6.asBinder();
                        }
                        reply.writeStrongBinder(iBinder);
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _result2 = bindBluetoothProfileService(data.readInt(), android.bluetooth.IBluetoothProfileServiceConnection.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        unbindBluetoothProfileService(data.readInt(), android.bluetooth.IBluetoothProfileServiceConnection.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        _result3 = getAddress();
                        reply.writeNoException();
                        reply.writeString(_result3);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        _result3 = getName();
                        reply.writeNoException();
                        reply.writeString(_result3);
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        _arg1 = isBleScanAlwaysAvailable();
                        reply.writeNoException();
                        reply.writeInt(_arg1);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        iBinder = data.readStrongBinder();
                        if (data.readInt() != 0) {
                            _arg1 = true;
                        }
                        int _result7 = updateBleAppCount(iBinder, _arg1, data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result7);
                        return true;
                    case 17:
                        data.enforceInterface(descriptor);
                        _arg1 = isBleAppPresent();
                        reply.writeNoException();
                        reply.writeInt(_arg1);
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        _arg1 = factoryReset();
                        reply.writeNoException();
                        reply.writeInt(_arg1);
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        _arg1 = isHearingAidProfileSupported();
                        reply.writeNoException();
                        reply.writeInt(_arg1);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IBluetoothManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IBluetoothManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean bindBluetoothProfileService(int i, IBluetoothProfileServiceConnection iBluetoothProfileServiceConnection) throws RemoteException;

    boolean disable(String str, boolean z) throws RemoteException;

    boolean enable(String str) throws RemoteException;

    boolean enableNoAutoConnect(String str) throws RemoteException;

    boolean factoryReset() throws RemoteException;

    String getAddress() throws RemoteException;

    IBluetoothGatt getBluetoothGatt() throws RemoteException;

    String getName() throws RemoteException;

    int getState() throws RemoteException;

    boolean isBleAppPresent() throws RemoteException;

    boolean isBleScanAlwaysAvailable() throws RemoteException;

    boolean isEnabled() throws RemoteException;

    boolean isHearingAidProfileSupported() throws RemoteException;

    IBluetooth registerAdapter(IBluetoothManagerCallback iBluetoothManagerCallback) throws RemoteException;

    void registerStateChangeCallback(IBluetoothStateChangeCallback iBluetoothStateChangeCallback) throws RemoteException;

    void unbindBluetoothProfileService(int i, IBluetoothProfileServiceConnection iBluetoothProfileServiceConnection) throws RemoteException;

    void unregisterAdapter(IBluetoothManagerCallback iBluetoothManagerCallback) throws RemoteException;

    void unregisterStateChangeCallback(IBluetoothStateChangeCallback iBluetoothStateChangeCallback) throws RemoteException;

    int updateBleAppCount(IBinder iBinder, boolean z, String str) throws RemoteException;
}
