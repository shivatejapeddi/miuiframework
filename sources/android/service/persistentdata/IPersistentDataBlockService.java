package android.service.persistentdata;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPersistentDataBlockService extends IInterface {

    public static class Default implements IPersistentDataBlockService {
        public int write(byte[] data) throws RemoteException {
            return 0;
        }

        public byte[] read() throws RemoteException {
            return null;
        }

        public void wipe() throws RemoteException {
        }

        public int getDataBlockSize() throws RemoteException {
            return 0;
        }

        public long getMaximumDataBlockSize() throws RemoteException {
            return 0;
        }

        public void setOemUnlockEnabled(boolean enabled) throws RemoteException {
        }

        public boolean getOemUnlockEnabled() throws RemoteException {
            return false;
        }

        public int getFlashLockState() throws RemoteException {
            return 0;
        }

        public boolean hasFrpCredentialHandle() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPersistentDataBlockService {
        private static final String DESCRIPTOR = "android.service.persistentdata.IPersistentDataBlockService";
        static final int TRANSACTION_getDataBlockSize = 4;
        static final int TRANSACTION_getFlashLockState = 8;
        static final int TRANSACTION_getMaximumDataBlockSize = 5;
        static final int TRANSACTION_getOemUnlockEnabled = 7;
        static final int TRANSACTION_hasFrpCredentialHandle = 9;
        static final int TRANSACTION_read = 2;
        static final int TRANSACTION_setOemUnlockEnabled = 6;
        static final int TRANSACTION_wipe = 3;
        static final int TRANSACTION_write = 1;

        private static class Proxy implements IPersistentDataBlockService {
            public static IPersistentDataBlockService sDefaultImpl;
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

            public int write(byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(data);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().write(data);
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

            public byte[] read() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    byte[] bArr = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().read();
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void wipe() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().wipe();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getDataBlockSize() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDataBlockSize();
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

            public long getMaximumDataBlockSize() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getMaximumDataBlockSize();
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

            public void setOemUnlockEnabled(boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setOemUnlockEnabled(enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getOemUnlockEnabled() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getOemUnlockEnabled();
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

            public int getFlashLockState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getFlashLockState();
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

            public boolean hasFrpCredentialHandle() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasFrpCredentialHandle();
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

        public static IPersistentDataBlockService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPersistentDataBlockService)) {
                return new Proxy(obj);
            }
            return (IPersistentDataBlockService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "write";
                case 2:
                    return "read";
                case 3:
                    return "wipe";
                case 4:
                    return "getDataBlockSize";
                case 5:
                    return "getMaximumDataBlockSize";
                case 6:
                    return "setOemUnlockEnabled";
                case 7:
                    return "getOemUnlockEnabled";
                case 8:
                    return "getFlashLockState";
                case 9:
                    return "hasFrpCredentialHandle";
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
                int _result;
                boolean _result2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        int _result3 = write(data.createByteArray());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        byte[] _result4 = read();
                        reply.writeNoException();
                        reply.writeByteArray(_result4);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        wipe();
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result = getDataBlockSize();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        long _result5 = getMaximumDataBlockSize();
                        reply.writeNoException();
                        reply.writeLong(_result5);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        setOemUnlockEnabled(data.readInt() != 0);
                        reply.writeNoException();
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result2 = getOemUnlockEnabled();
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result = getFlashLockState();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result2 = hasFrpCredentialHandle();
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IPersistentDataBlockService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPersistentDataBlockService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int getDataBlockSize() throws RemoteException;

    int getFlashLockState() throws RemoteException;

    long getMaximumDataBlockSize() throws RemoteException;

    boolean getOemUnlockEnabled() throws RemoteException;

    boolean hasFrpCredentialHandle() throws RemoteException;

    byte[] read() throws RemoteException;

    void setOemUnlockEnabled(boolean z) throws RemoteException;

    void wipe() throws RemoteException;

    int write(byte[] bArr) throws RemoteException;
}
