package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPinnedStackController extends IInterface {

    public static class Default implements IPinnedStackController {
        public void setIsMinimized(boolean isMinimized) throws RemoteException {
        }

        public void setMinEdgeSize(int minEdgeSize) throws RemoteException {
        }

        public int getDisplayRotation() throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPinnedStackController {
        private static final String DESCRIPTOR = "android.view.IPinnedStackController";
        static final int TRANSACTION_getDisplayRotation = 3;
        static final int TRANSACTION_setIsMinimized = 1;
        static final int TRANSACTION_setMinEdgeSize = 2;

        private static class Proxy implements IPinnedStackController {
            public static IPinnedStackController sDefaultImpl;
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

            public void setIsMinimized(boolean isMinimized) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isMinimized ? 1 : 0);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setIsMinimized(isMinimized);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setMinEdgeSize(int minEdgeSize) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(minEdgeSize);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setMinEdgeSize(minEdgeSize);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public int getDisplayRotation() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDisplayRotation();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPinnedStackController asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPinnedStackController)) {
                return new Proxy(obj);
            }
            return (IPinnedStackController) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "setIsMinimized";
            }
            if (transactionCode == 2) {
                return "setMinEdgeSize";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "getDisplayRotation";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                setIsMinimized(data.readInt() != 0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                setMinEdgeSize(data.readInt());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                int _result = getDisplayRotation();
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IPinnedStackController impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPinnedStackController getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int getDisplayRotation() throws RemoteException;

    void setIsMinimized(boolean z) throws RemoteException;

    void setMinEdgeSize(int i) throws RemoteException;
}
