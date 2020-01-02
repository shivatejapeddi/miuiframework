package android.service.oemlock;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOemLockService extends IInterface {

    public static class Default implements IOemLockService {
        public String getLockName() throws RemoteException {
            return null;
        }

        public void setOemUnlockAllowedByCarrier(boolean allowed, byte[] signature) throws RemoteException {
        }

        public boolean isOemUnlockAllowedByCarrier() throws RemoteException {
            return false;
        }

        public void setOemUnlockAllowedByUser(boolean allowed) throws RemoteException {
        }

        public boolean isOemUnlockAllowedByUser() throws RemoteException {
            return false;
        }

        public boolean isOemUnlockAllowed() throws RemoteException {
            return false;
        }

        public boolean isDeviceOemUnlocked() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IOemLockService {
        private static final String DESCRIPTOR = "android.service.oemlock.IOemLockService";
        static final int TRANSACTION_getLockName = 1;
        static final int TRANSACTION_isDeviceOemUnlocked = 7;
        static final int TRANSACTION_isOemUnlockAllowed = 6;
        static final int TRANSACTION_isOemUnlockAllowedByCarrier = 3;
        static final int TRANSACTION_isOemUnlockAllowedByUser = 5;
        static final int TRANSACTION_setOemUnlockAllowedByCarrier = 2;
        static final int TRANSACTION_setOemUnlockAllowedByUser = 4;

        private static class Proxy implements IOemLockService {
            public static IOemLockService sDefaultImpl;
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

            public String getLockName() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getLockName();
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

            public void setOemUnlockAllowedByCarrier(boolean allowed, byte[] signature) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(allowed ? 1 : 0);
                    _data.writeByteArray(signature);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOemUnlockAllowedByCarrier(allowed, signature);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isOemUnlockAllowedByCarrier() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isOemUnlockAllowedByCarrier();
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

            public void setOemUnlockAllowedByUser(boolean allowed) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(allowed ? 1 : 0);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOemUnlockAllowedByUser(allowed);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isOemUnlockAllowedByUser() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isOemUnlockAllowedByUser();
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

            public boolean isOemUnlockAllowed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isOemUnlockAllowed();
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

            public boolean isDeviceOemUnlocked() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDeviceOemUnlocked();
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

        public static IOemLockService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IOemLockService)) {
                return new Proxy(obj);
            }
            return (IOemLockService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getLockName";
                case 2:
                    return "setOemUnlockAllowedByCarrier";
                case 3:
                    return "isOemUnlockAllowedByCarrier";
                case 4:
                    return "setOemUnlockAllowedByUser";
                case 5:
                    return "isOemUnlockAllowedByUser";
                case 6:
                    return "isOemUnlockAllowed";
                case 7:
                    return "isDeviceOemUnlocked";
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
                boolean _arg0 = false;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        String _result = getLockName();
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setOemUnlockAllowedByCarrier(_arg0, data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _arg0 = isOemUnlockAllowedByCarrier();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setOemUnlockAllowedByUser(_arg0);
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg0 = isOemUnlockAllowedByUser();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _arg0 = isOemUnlockAllowed();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _arg0 = isDeviceOemUnlocked();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IOemLockService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IOemLockService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    String getLockName() throws RemoteException;

    boolean isDeviceOemUnlocked() throws RemoteException;

    boolean isOemUnlockAllowed() throws RemoteException;

    boolean isOemUnlockAllowedByCarrier() throws RemoteException;

    boolean isOemUnlockAllowedByUser() throws RemoteException;

    void setOemUnlockAllowedByCarrier(boolean z, byte[] bArr) throws RemoteException;

    void setOemUnlockAllowedByUser(boolean z) throws RemoteException;
}
