package com.miui.freeform;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiuiFreeformWindowService extends IInterface {

    public static class Default implements IMiuiFreeformWindowService {
        public void windowResize(int taskId, String caller) throws RemoteException {
        }

        public void windowDismiss(int taskId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiuiFreeformWindowService {
        private static final String DESCRIPTOR = "com.miui.freeform.IMiuiFreeformWindowService";
        static final int TRANSACTION_windowDismiss = 2;
        static final int TRANSACTION_windowResize = 1;

        private static class Proxy implements IMiuiFreeformWindowService {
            public static IMiuiFreeformWindowService sDefaultImpl;
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

            public void windowResize(int taskId, String caller) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    _data.writeString(caller);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().windowResize(taskId, caller);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void windowDismiss(int taskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(taskId);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().windowDismiss(taskId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiuiFreeformWindowService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiuiFreeformWindowService)) {
                return new Proxy(obj);
            }
            return (IMiuiFreeformWindowService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "windowResize";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "windowDismiss";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                windowResize(data.readInt(), data.readString());
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                windowDismiss(data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMiuiFreeformWindowService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiuiFreeformWindowService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void windowDismiss(int i) throws RemoteException;

    void windowResize(int i, String str) throws RemoteException;
}
