package miui.os;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiuiDexoptObserver extends IInterface {

    public static class Default implements IMiuiDexoptObserver {
        public void onStart(String packageName) throws RemoteException {
        }

        public void onFinished(String packageName, int returnCode) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiuiDexoptObserver {
        private static final String DESCRIPTOR = "miui.os.IMiuiDexoptObserver";
        static final int TRANSACTION_onFinished = 2;
        static final int TRANSACTION_onStart = 1;

        private static class Proxy implements IMiuiDexoptObserver {
            public static IMiuiDexoptObserver sDefaultImpl;
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

            public void onStart(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStart(packageName);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onFinished(String packageName, int returnCode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(returnCode);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFinished(packageName, returnCode);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiuiDexoptObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiuiDexoptObserver)) {
                return new Proxy(obj);
            }
            return (IMiuiDexoptObserver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onStart";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onFinished";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onStart(data.readString());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onFinished(data.readString(), data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMiuiDexoptObserver impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiuiDexoptObserver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onFinished(String str, int i) throws RemoteException;

    void onStart(String str) throws RemoteException;
}
