package android.service.dreams;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDreamService extends IInterface {

    public static abstract class Stub extends Binder implements IDreamService {
        private static final String DESCRIPTOR = "android.service.dreams.IDreamService";
        static final int TRANSACTION_attach = 1;
        static final int TRANSACTION_detach = 2;
        static final int TRANSACTION_wakeUp = 3;

        private static class Proxy implements IDreamService {
            public static IDreamService sDefaultImpl;
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

            public void attach(IBinder windowToken, boolean canDoze, IRemoteCallback started) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(windowToken);
                    _data.writeInt(canDoze ? 1 : 0);
                    _data.writeStrongBinder(started != null ? started.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().attach(windowToken, canDoze, started);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void detach() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().detach();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void wakeUp() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().wakeUp();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDreamService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDreamService)) {
                return new Proxy(obj);
            }
            return (IDreamService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "attach";
            }
            if (transactionCode == 2) {
                return "detach";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "wakeUp";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                attach(data.readStrongBinder(), data.readInt() != 0, android.os.IRemoteCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                detach();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                wakeUp();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IDreamService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDreamService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IDreamService {
        public void attach(IBinder windowToken, boolean canDoze, IRemoteCallback started) throws RemoteException {
        }

        public void detach() throws RemoteException {
        }

        public void wakeUp() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void attach(IBinder iBinder, boolean z, IRemoteCallback iRemoteCallback) throws RemoteException;

    void detach() throws RemoteException;

    void wakeUp() throws RemoteException;
}
