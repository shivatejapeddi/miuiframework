package com.miui.enterprise;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IRestrictionsManager extends IInterface {

    public static class Default implements IRestrictionsManager {
        public void setControlStatus(String key, int value, int userId) throws RemoteException {
        }

        public void setRestriction(String key, boolean value, int userId) throws RemoteException {
        }

        public int getControlStatus(String key, int userId) throws RemoteException {
            return 0;
        }

        public boolean hasRestriction(String key, int userId) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRestrictionsManager {
        private static final String DESCRIPTOR = "com.miui.enterprise.IRestrictionsManager";
        static final int TRANSACTION_getControlStatus = 3;
        static final int TRANSACTION_hasRestriction = 4;
        static final int TRANSACTION_setControlStatus = 1;
        static final int TRANSACTION_setRestriction = 2;

        private static class Proxy implements IRestrictionsManager {
            public static IRestrictionsManager sDefaultImpl;
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

            public void setControlStatus(String key, int value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(value);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setControlStatus(key, value, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setRestriction(String key, boolean value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(value ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setRestriction(key, value, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getControlStatus(String key, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(userId);
                    int i = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getControlStatus(key, userId);
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

            public boolean hasRestriction(String key, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasRestriction(key, userId);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRestrictionsManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRestrictionsManager)) {
                return new Proxy(obj);
            }
            return (IRestrictionsManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "setControlStatus";
            }
            if (transactionCode == 2) {
                return "setRestriction";
            }
            if (transactionCode == 3) {
                return "getControlStatus";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "hasRestriction";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                setControlStatus(data.readString(), data.readInt(), data.readInt());
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                setRestriction(data.readString(), data.readInt() != 0, data.readInt());
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                int _result = getControlStatus(data.readString(), data.readInt());
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                boolean _result2 = hasRestriction(data.readString(), data.readInt());
                reply.writeNoException();
                reply.writeInt(_result2);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IRestrictionsManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRestrictionsManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    int getControlStatus(String str, int i) throws RemoteException;

    boolean hasRestriction(String str, int i) throws RemoteException;

    void setControlStatus(String str, int i, int i2) throws RemoteException;

    void setRestriction(String str, boolean z, int i) throws RemoteException;
}
