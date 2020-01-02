package android.net.wifi;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IDppCallback extends IInterface {

    public static class Default implements IDppCallback {
        public void onSuccessConfigReceived(int newNetworkId) throws RemoteException {
        }

        public void onSuccess(int status) throws RemoteException {
        }

        public void onFailure(int status) throws RemoteException {
        }

        public void onProgress(int status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IDppCallback {
        private static final String DESCRIPTOR = "android.net.wifi.IDppCallback";
        static final int TRANSACTION_onFailure = 3;
        static final int TRANSACTION_onProgress = 4;
        static final int TRANSACTION_onSuccess = 2;
        static final int TRANSACTION_onSuccessConfigReceived = 1;

        private static class Proxy implements IDppCallback {
            public static IDppCallback sDefaultImpl;
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

            public void onSuccessConfigReceived(int newNetworkId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(newNetworkId);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSuccessConfigReceived(newNetworkId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onSuccess(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSuccess(status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFailure(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFailure(status);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onProgress(int status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(status);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onProgress(status);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IDppCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IDppCallback)) {
                return new Proxy(obj);
            }
            return (IDppCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onSuccessConfigReceived";
            }
            if (transactionCode == 2) {
                return "onSuccess";
            }
            if (transactionCode == 3) {
                return "onFailure";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "onProgress";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onSuccessConfigReceived(data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onSuccess(data.readInt());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onFailure(data.readInt());
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                onProgress(data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IDppCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IDppCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onFailure(int i) throws RemoteException;

    void onProgress(int i) throws RemoteException;

    void onSuccess(int i) throws RemoteException;

    void onSuccessConfigReceived(int i) throws RemoteException;
}
