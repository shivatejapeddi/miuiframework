package android.se.omapi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISecureElementReader extends IInterface {

    public static class Default implements ISecureElementReader {
        public boolean isSecureElementPresent() throws RemoteException {
            return false;
        }

        public ISecureElementSession openSession() throws RemoteException {
            return null;
        }

        public void closeSessions() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISecureElementReader {
        private static final String DESCRIPTOR = "android.se.omapi.ISecureElementReader";
        static final int TRANSACTION_closeSessions = 3;
        static final int TRANSACTION_isSecureElementPresent = 1;
        static final int TRANSACTION_openSession = 2;

        private static class Proxy implements ISecureElementReader {
            public static ISecureElementReader sDefaultImpl;
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

            public boolean isSecureElementPresent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
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
                    z = Stub.getDefaultImpl().isSecureElementPresent();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ISecureElementSession openSession() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ISecureElementSession iSecureElementSession = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        iSecureElementSession = Stub.getDefaultImpl();
                        if (iSecureElementSession != 0) {
                            iSecureElementSession = Stub.getDefaultImpl().openSession();
                            return iSecureElementSession;
                        }
                    }
                    _reply.readException();
                    iSecureElementSession = android.se.omapi.ISecureElementSession.Stub.asInterface(_reply.readStrongBinder());
                    ISecureElementSession _result = iSecureElementSession;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void closeSessions() throws RemoteException {
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
                    Stub.getDefaultImpl().closeSessions();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISecureElementReader asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISecureElementReader)) {
                return new Proxy(obj);
            }
            return (ISecureElementReader) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "isSecureElementPresent";
            }
            if (transactionCode == 2) {
                return "openSession";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "closeSessions";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                boolean _result = isSecureElementPresent();
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                ISecureElementSession _result2 = openSession();
                reply.writeNoException();
                reply.writeStrongBinder(_result2 != null ? _result2.asBinder() : null);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                closeSessions();
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISecureElementReader impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISecureElementReader getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void closeSessions() throws RemoteException;

    boolean isSecureElementPresent() throws RemoteException;

    ISecureElementSession openSession() throws RemoteException;
}
