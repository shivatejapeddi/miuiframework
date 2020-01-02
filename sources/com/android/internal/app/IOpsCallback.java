package com.android.internal.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IOpsCallback extends IInterface {

    public static class Default implements IOpsCallback {
        public int askOperation(int uid, String packageName, int op) throws RemoteException {
            return 0;
        }

        public int getDefaultOp(int uid, String packageName, int op) throws RemoteException {
            return 0;
        }

        public boolean isAppPermissionControlOpen(int op) throws RemoteException {
            return false;
        }

        public void onAppApplyOperation(int uid, String packageName, int op, int mode, boolean isStartOperation) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IOpsCallback {
        private static final String DESCRIPTOR = "com.android.internal.app.IOpsCallback";
        static final int TRANSACTION_askOperation = 1;
        static final int TRANSACTION_getDefaultOp = 2;
        static final int TRANSACTION_isAppPermissionControlOpen = 3;
        static final int TRANSACTION_onAppApplyOperation = 4;

        private static class Proxy implements IOpsCallback {
            public static IOpsCallback sDefaultImpl;
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

            public int askOperation(int uid, String packageName, int op) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeInt(op);
                    int i = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().askOperation(uid, packageName, op);
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

            public int getDefaultOp(int uid, String packageName, int op) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeInt(op);
                    int i = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getDefaultOp(uid, packageName, op);
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

            public boolean isAppPermissionControlOpen(int op) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(op);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAppPermissionControlOpen(op);
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onAppApplyOperation(int uid, String packageName, int op, int mode, boolean isStartOperation) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeInt(op);
                    _data.writeInt(mode);
                    _data.writeInt(isStartOperation ? 1 : 0);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAppApplyOperation(uid, packageName, op, mode, isStartOperation);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOpsCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IOpsCallback)) {
                return new Proxy(obj);
            }
            return (IOpsCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "askOperation";
            }
            if (transactionCode == 2) {
                return "getDefaultOp";
            }
            if (transactionCode == 3) {
                return "isAppPermissionControlOpen";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "onAppApplyOperation";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            int _result;
            if (i == 1) {
                parcel.enforceInterface(descriptor);
                _result = askOperation(data.readInt(), data.readString(), data.readInt());
                reply.writeNoException();
                parcel2.writeInt(_result);
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(descriptor);
                _result = getDefaultOp(data.readInt(), data.readString(), data.readInt());
                reply.writeNoException();
                parcel2.writeInt(_result);
                return true;
            } else if (i == 3) {
                parcel.enforceInterface(descriptor);
                boolean _result2 = isAppPermissionControlOpen(data.readInt());
                reply.writeNoException();
                parcel2.writeInt(_result2);
                return true;
            } else if (i == 4) {
                parcel.enforceInterface(descriptor);
                onAppApplyOperation(data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt() != 0);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(code, data, reply, flags);
            } else {
                parcel2.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IOpsCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IOpsCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int askOperation(int i, String str, int i2) throws RemoteException;

    int getDefaultOp(int i, String str, int i2) throws RemoteException;

    boolean isAppPermissionControlOpen(int i) throws RemoteException;

    void onAppApplyOperation(int i, String str, int i2, int i3, boolean z) throws RemoteException;
}
