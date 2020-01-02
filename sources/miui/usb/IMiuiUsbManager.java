package miui.usb;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiuiUsbManager extends IInterface {

    public static class Default implements IMiuiUsbManager {
        public void acceptMdbRestore() throws RemoteException {
        }

        public void cancelMdbRestore() throws RemoteException {
        }

        public void allowUsbDebugging(boolean alwaysAllow, String publicKey) throws RemoteException {
        }

        public void denyUsbDebugging() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiuiUsbManager {
        private static final String DESCRIPTOR = "miui.usb.IMiuiUsbManager";
        static final int TRANSACTION_acceptMdbRestore = 1;
        static final int TRANSACTION_allowUsbDebugging = 3;
        static final int TRANSACTION_cancelMdbRestore = 2;
        static final int TRANSACTION_denyUsbDebugging = 4;

        private static class Proxy implements IMiuiUsbManager {
            public static IMiuiUsbManager sDefaultImpl;
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

            public void acceptMdbRestore() throws RemoteException {
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
                    Stub.getDefaultImpl().acceptMdbRestore();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelMdbRestore() throws RemoteException {
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
                    Stub.getDefaultImpl().cancelMdbRestore();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void allowUsbDebugging(boolean alwaysAllow, String publicKey) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(alwaysAllow ? 1 : 0);
                    _data.writeString(publicKey);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().allowUsbDebugging(alwaysAllow, publicKey);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void denyUsbDebugging() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().denyUsbDebugging();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiuiUsbManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiuiUsbManager)) {
                return new Proxy(obj);
            }
            return (IMiuiUsbManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "acceptMdbRestore";
            }
            if (transactionCode == 2) {
                return "cancelMdbRestore";
            }
            if (transactionCode == 3) {
                return "allowUsbDebugging";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "denyUsbDebugging";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                acceptMdbRestore();
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                cancelMdbRestore();
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                allowUsbDebugging(data.readInt() != 0, data.readString());
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                denyUsbDebugging();
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMiuiUsbManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiuiUsbManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void acceptMdbRestore() throws RemoteException;

    void allowUsbDebugging(boolean z, String str) throws RemoteException;

    void cancelMdbRestore() throws RemoteException;

    void denyUsbDebugging() throws RemoteException;
}
