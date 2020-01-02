package android.os.image;

import android.gsi.GsiProgress;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDynamicSystemService extends IInterface {

    public static class Default implements IDynamicSystemService {
        public boolean startInstallation(long systemSize, long userdataSize) throws RemoteException {
            return false;
        }

        public GsiProgress getInstallationProgress() throws RemoteException {
            return null;
        }

        public boolean abort() throws RemoteException {
            return false;
        }

        public boolean isInUse() throws RemoteException {
            return false;
        }

        public boolean isInstalled() throws RemoteException {
            return false;
        }

        public boolean isEnabled() throws RemoteException {
            return false;
        }

        public boolean remove() throws RemoteException {
            return false;
        }

        public boolean setEnable(boolean enable) throws RemoteException {
            return false;
        }

        public boolean write(byte[] buf) throws RemoteException {
            return false;
        }

        public boolean commit() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDynamicSystemService {
        private static final String DESCRIPTOR = "android.os.image.IDynamicSystemService";
        static final int TRANSACTION_abort = 3;
        static final int TRANSACTION_commit = 10;
        static final int TRANSACTION_getInstallationProgress = 2;
        static final int TRANSACTION_isEnabled = 6;
        static final int TRANSACTION_isInUse = 4;
        static final int TRANSACTION_isInstalled = 5;
        static final int TRANSACTION_remove = 7;
        static final int TRANSACTION_setEnable = 8;
        static final int TRANSACTION_startInstallation = 1;
        static final int TRANSACTION_write = 9;

        private static class Proxy implements IDynamicSystemService {
            public static IDynamicSystemService sDefaultImpl;
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

            public boolean startInstallation(long systemSize, long userdataSize) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(systemSize);
                    _data.writeLong(userdataSize);
                    boolean z = false;
                    if (this.mRemote.transact(1, _data, _reply, z) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            z = true;
                        }
                        boolean _result = z;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    z = Stub.getDefaultImpl().startInstallation(systemSize, userdataSize);
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public GsiProgress getInstallationProgress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    GsiProgress gsiProgress = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        gsiProgress = Stub.getDefaultImpl();
                        if (gsiProgress != 0) {
                            gsiProgress = Stub.getDefaultImpl().getInstallationProgress();
                            return gsiProgress;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        gsiProgress = (GsiProgress) GsiProgress.CREATOR.createFromParcel(_reply);
                    } else {
                        gsiProgress = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return gsiProgress;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean abort() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().abort();
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

            public boolean isInUse() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInUse();
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

            public boolean isInstalled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isInstalled();
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

            public boolean isEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
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

            public boolean remove() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().remove();
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

            public boolean setEnable(boolean enable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    _data.writeInt(enable ? 1 : 0);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setEnable(enable);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean write(byte[] buf) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(buf);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().write(buf);
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

            public boolean commit() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().commit();
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

        public static IDynamicSystemService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDynamicSystemService)) {
                return new Proxy(obj);
            }
            return (IDynamicSystemService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "startInstallation";
                case 2:
                    return "getInstallationProgress";
                case 3:
                    return "abort";
                case 4:
                    return "isInUse";
                case 5:
                    return "isInstalled";
                case 6:
                    return "isEnabled";
                case 7:
                    return "remove";
                case 8:
                    return "setEnable";
                case 9:
                    return "write";
                case 10:
                    return "commit";
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
                boolean _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _arg0 = startInstallation(data.readLong(), data.readLong());
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        GsiProgress _result2 = getInstallationProgress();
                        reply.writeNoException();
                        if (_result2 != null) {
                            reply.writeInt(1);
                            _result2.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _arg0 = abort();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _arg0 = isInUse();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg0 = isInstalled();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        _arg0 = isEnabled();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _arg0 = remove();
                        reply.writeNoException();
                        reply.writeInt(_arg0);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        _result = setEnable(_arg0);
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result = write(data.createByteArray());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _arg0 = commit();
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

        public static boolean setDefaultImpl(IDynamicSystemService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDynamicSystemService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean abort() throws RemoteException;

    boolean commit() throws RemoteException;

    GsiProgress getInstallationProgress() throws RemoteException;

    boolean isEnabled() throws RemoteException;

    boolean isInUse() throws RemoteException;

    boolean isInstalled() throws RemoteException;

    boolean remove() throws RemoteException;

    boolean setEnable(boolean z) throws RemoteException;

    boolean startInstallation(long j, long j2) throws RemoteException;

    boolean write(byte[] bArr) throws RemoteException;
}
