package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGestureStubListener extends IInterface {

    public static class Default implements IGestureStubListener {
        public void onGestureReady() throws RemoteException {
        }

        public void onGestureStart() throws RemoteException {
        }

        public void onGestureFinish(boolean immediate) throws RemoteException {
        }

        public void skipAppTransition() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IGestureStubListener {
        private static final String DESCRIPTOR = "android.view.IGestureStubListener";
        static final int TRANSACTION_onGestureFinish = 3;
        static final int TRANSACTION_onGestureReady = 1;
        static final int TRANSACTION_onGestureStart = 2;
        static final int TRANSACTION_skipAppTransition = 4;

        private static class Proxy implements IGestureStubListener {
            public static IGestureStubListener sDefaultImpl;
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

            public void onGestureReady() throws RemoteException {
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
                    Stub.getDefaultImpl().onGestureReady();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onGestureStart() throws RemoteException {
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
                    Stub.getDefaultImpl().onGestureStart();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onGestureFinish(boolean immediate) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(immediate ? 1 : 0);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onGestureFinish(immediate);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void skipAppTransition() throws RemoteException {
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
                    Stub.getDefaultImpl().skipAppTransition();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGestureStubListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGestureStubListener)) {
                return new Proxy(obj);
            }
            return (IGestureStubListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onGestureReady";
            }
            if (transactionCode == 2) {
                return "onGestureStart";
            }
            if (transactionCode == 3) {
                return "onGestureFinish";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "skipAppTransition";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onGestureReady();
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onGestureStart();
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onGestureFinish(data.readInt() != 0);
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                skipAppTransition();
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IGestureStubListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IGestureStubListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onGestureFinish(boolean z) throws RemoteException;

    void onGestureReady() throws RemoteException;

    void onGestureStart() throws RemoteException;

    void skipAppTransition() throws RemoteException;
}
