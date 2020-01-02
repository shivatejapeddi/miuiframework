package android.content.pm.dex;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

public interface ISnapshotRuntimeProfileCallback extends IInterface {

    public static abstract class Stub extends Binder implements ISnapshotRuntimeProfileCallback {
        private static final String DESCRIPTOR = "android.content.pm.dex.ISnapshotRuntimeProfileCallback";
        static final int TRANSACTION_onError = 2;
        static final int TRANSACTION_onSuccess = 1;

        private static class Proxy implements ISnapshotRuntimeProfileCallback {
            public static ISnapshotRuntimeProfileCallback sDefaultImpl;
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

            public void onSuccess(ParcelFileDescriptor profileReadFd) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (profileReadFd != null) {
                        _data.writeInt(1);
                        profileReadFd.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onSuccess(profileReadFd);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onError(int errCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(errCode);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onError(errCode);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISnapshotRuntimeProfileCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISnapshotRuntimeProfileCallback)) {
                return new Proxy(obj);
            }
            return (ISnapshotRuntimeProfileCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onSuccess";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onError";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                ParcelFileDescriptor _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                onSuccess(_arg0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onError(data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISnapshotRuntimeProfileCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISnapshotRuntimeProfileCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements ISnapshotRuntimeProfileCallback {
        public void onSuccess(ParcelFileDescriptor profileReadFd) throws RemoteException {
        }

        public void onError(int errCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onError(int i) throws RemoteException;

    void onSuccess(ParcelFileDescriptor parcelFileDescriptor) throws RemoteException;
}
