package android.content.pm;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOtaDexopt extends IInterface {

    public static class Default implements IOtaDexopt {
        public void prepare() throws RemoteException {
        }

        public void cleanup() throws RemoteException {
        }

        public boolean isDone() throws RemoteException {
            return false;
        }

        public float getProgress() throws RemoteException {
            return 0.0f;
        }

        public void dexoptNextPackage() throws RemoteException {
        }

        public String nextDexoptCommand() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IOtaDexopt {
        private static final String DESCRIPTOR = "android.content.pm.IOtaDexopt";
        static final int TRANSACTION_cleanup = 2;
        static final int TRANSACTION_dexoptNextPackage = 5;
        static final int TRANSACTION_getProgress = 4;
        static final int TRANSACTION_isDone = 3;
        static final int TRANSACTION_nextDexoptCommand = 6;
        static final int TRANSACTION_prepare = 1;

        private static class Proxy implements IOtaDexopt {
            public static IOtaDexopt sDefaultImpl;
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

            public void prepare() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepare();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cleanup() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cleanup();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isDone() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isDone();
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

            public float getProgress() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    float f = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        f = Stub.getDefaultImpl();
                        if (f != 0) {
                            f = Stub.getDefaultImpl().getProgress();
                            return f;
                        }
                    }
                    _reply.readException();
                    f = _reply.readFloat();
                    int _result = f;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void dexoptNextPackage() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dexoptNextPackage();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String nextDexoptCommand() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().nextDexoptCommand();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOtaDexopt asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IOtaDexopt)) {
                return new Proxy(obj);
            }
            return (IOtaDexopt) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "prepare";
                case 2:
                    return "cleanup";
                case 3:
                    return "isDone";
                case 4:
                    return "getProgress";
                case 5:
                    return "dexoptNextPackage";
                case 6:
                    return "nextDexoptCommand";
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
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        prepare();
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        cleanup();
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        boolean _result = isDone();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        float _result2 = getProgress();
                        reply.writeNoException();
                        reply.writeFloat(_result2);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        dexoptNextPackage();
                        reply.writeNoException();
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        String _result3 = nextDexoptCommand();
                        reply.writeNoException();
                        reply.writeString(_result3);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IOtaDexopt impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IOtaDexopt getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void cleanup() throws RemoteException;

    void dexoptNextPackage() throws RemoteException;

    float getProgress() throws RemoteException;

    boolean isDone() throws RemoteException;

    String nextDexoptCommand() throws RemoteException;

    void prepare() throws RemoteException;
}
