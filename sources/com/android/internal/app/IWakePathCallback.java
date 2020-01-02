package com.android.internal.app;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IWakePathCallback extends IInterface {

    public static class Default implements IWakePathCallback {
        public void onRejectCall(String caller, String callee, int wakePathType, int userId) throws RemoteException {
        }

        public void onAllowCall(String caller, String callee, int wakePathType, int userId) throws RemoteException {
        }

        public void onUpdateCall(int type, Intent intent, String pkgname) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWakePathCallback {
        private static final String DESCRIPTOR = "com.android.internal.app.IWakePathCallback";
        static final int TRANSACTION_onAllowCall = 2;
        static final int TRANSACTION_onRejectCall = 1;
        static final int TRANSACTION_onUpdateCall = 3;

        private static class Proxy implements IWakePathCallback {
            public static IWakePathCallback sDefaultImpl;
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

            public void onRejectCall(String caller, String callee, int wakePathType, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(caller);
                    _data.writeString(callee);
                    _data.writeInt(wakePathType);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRejectCall(caller, callee, wakePathType, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onAllowCall(String caller, String callee, int wakePathType, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(caller);
                    _data.writeString(callee);
                    _data.writeInt(wakePathType);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onAllowCall(caller, callee, wakePathType, userId);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onUpdateCall(int type, Intent intent, String pkgname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(pkgname);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onUpdateCall(type, intent, pkgname);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWakePathCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWakePathCallback)) {
                return new Proxy(obj);
            }
            return (IWakePathCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onRejectCall";
            }
            if (transactionCode == 2) {
                return "onAllowCall";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onUpdateCall";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onRejectCall(data.readString(), data.readString(), data.readInt(), data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onAllowCall(data.readString(), data.readString(), data.readInt(), data.readInt());
                return true;
            } else if (code == 3) {
                Intent _arg1;
                data.enforceInterface(descriptor);
                int _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (Intent) Intent.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                onUpdateCall(_arg0, _arg1, data.readString());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IWakePathCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWakePathCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onAllowCall(String str, String str2, int i, int i2) throws RemoteException;

    void onRejectCall(String str, String str2, int i, int i2) throws RemoteException;

    void onUpdateCall(int i, Intent intent, String str) throws RemoteException;
}
