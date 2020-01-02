package android.net;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISocketKeepaliveCallback extends IInterface {

    public static abstract class Stub extends Binder implements ISocketKeepaliveCallback {
        private static final String DESCRIPTOR = "android.net.ISocketKeepaliveCallback";
        static final int TRANSACTION_onDataReceived = 4;
        static final int TRANSACTION_onError = 3;
        static final int TRANSACTION_onStarted = 1;
        static final int TRANSACTION_onStopped = 2;

        private static class Proxy implements ISocketKeepaliveCallback {
            public static ISocketKeepaliveCallback sDefaultImpl;
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

            public void onStarted(int slot) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(slot);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStarted(slot);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onStopped() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStopped();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onError(int error) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(error);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onError(error);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDataReceived() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDataReceived();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISocketKeepaliveCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISocketKeepaliveCallback)) {
                return new Proxy(obj);
            }
            return (ISocketKeepaliveCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onStarted";
            }
            if (transactionCode == 2) {
                return "onStopped";
            }
            if (transactionCode == 3) {
                return "onError";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "onDataReceived";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onStarted(data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onStopped();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onError(data.readInt());
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                onDataReceived();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISocketKeepaliveCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISocketKeepaliveCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements ISocketKeepaliveCallback {
        public void onStarted(int slot) throws RemoteException {
        }

        public void onStopped() throws RemoteException {
        }

        public void onError(int error) throws RemoteException {
        }

        public void onDataReceived() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onDataReceived() throws RemoteException;

    void onError(int i) throws RemoteException;

    void onStarted(int i) throws RemoteException;

    void onStopped() throws RemoteException;
}
