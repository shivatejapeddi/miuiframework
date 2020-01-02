package com.android.internal.inputmethod;

import android.content.ContentResolver;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMultiClientInputMethod extends IInterface {

    public static class Default implements IMultiClientInputMethod {
        public void initialize(IMultiClientInputMethodPrivilegedOperations privOps) throws RemoteException {
        }

        public void addClient(int clientId, int uid, int pid, int selfReportedDisplayId) throws RemoteException {
        }

        public void removeClient(int clientId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMultiClientInputMethod {
        private static final String DESCRIPTOR = "com.android.internal.inputmethod.IMultiClientInputMethod";
        static final int TRANSACTION_addClient = 2;
        static final int TRANSACTION_initialize = 1;
        static final int TRANSACTION_removeClient = 3;

        private static class Proxy implements IMultiClientInputMethod {
            public static IMultiClientInputMethod sDefaultImpl;
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

            public void initialize(IMultiClientInputMethodPrivilegedOperations privOps) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(privOps != null ? privOps.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().initialize(privOps);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void addClient(int clientId, int uid, int pid, int selfReportedDisplayId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    _data.writeInt(uid);
                    _data.writeInt(pid);
                    _data.writeInt(selfReportedDisplayId);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().addClient(clientId, uid, pid, selfReportedDisplayId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void removeClient(int clientId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(clientId);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().removeClient(clientId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMultiClientInputMethod asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMultiClientInputMethod)) {
                return new Proxy(obj);
            }
            return (IMultiClientInputMethod) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return ContentResolver.SYNC_EXTRAS_INITIALIZE;
            }
            if (transactionCode == 2) {
                return "addClient";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "removeClient";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                initialize(com.android.internal.inputmethod.IMultiClientInputMethodPrivilegedOperations.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                addClient(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                removeClient(data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMultiClientInputMethod impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMultiClientInputMethod getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addClient(int i, int i2, int i3, int i4) throws RemoteException;

    void initialize(IMultiClientInputMethodPrivilegedOperations iMultiClientInputMethodPrivilegedOperations) throws RemoteException;

    void removeClient(int i) throws RemoteException;
}
