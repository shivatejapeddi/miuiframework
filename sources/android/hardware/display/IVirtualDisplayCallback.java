package android.hardware.display;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IVirtualDisplayCallback extends IInterface {

    public static abstract class Stub extends Binder implements IVirtualDisplayCallback {
        private static final String DESCRIPTOR = "android.hardware.display.IVirtualDisplayCallback";
        static final int TRANSACTION_onPaused = 1;
        static final int TRANSACTION_onResumed = 2;
        static final int TRANSACTION_onStopped = 3;

        private static class Proxy implements IVirtualDisplayCallback {
            public static IVirtualDisplayCallback sDefaultImpl;
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

            public void onPaused() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPaused();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onResumed() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onResumed();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onStopped() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onStopped();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IVirtualDisplayCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IVirtualDisplayCallback)) {
                return new Proxy(obj);
            }
            return (IVirtualDisplayCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onPaused";
            }
            if (transactionCode == 2) {
                return "onResumed";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onStopped";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onPaused();
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onResumed();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onStopped();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IVirtualDisplayCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IVirtualDisplayCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IVirtualDisplayCallback {
        public void onPaused() throws RemoteException {
        }

        public void onResumed() throws RemoteException {
        }

        public void onStopped() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onPaused() throws RemoteException;

    void onResumed() throws RemoteException;

    void onStopped() throws RemoteException;
}
