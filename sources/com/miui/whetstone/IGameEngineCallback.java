package com.miui.whetstone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IGameEngineCallback extends IInterface {

    public static class Default implements IGameEngineCallback {
        public void onUpdateGameInfo(String data) throws RemoteException {
        }

        public void onApplyHardwareResource(String data) throws RemoteException {
        }

        public void onRestoreDefaultConfig() throws RemoteException {
        }

        public void onEventHandle(int event, String org1, String org2) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IGameEngineCallback {
        private static final String DESCRIPTOR = "com.miui.whetstone.IGameEngineCallback";
        static final int TRANSACTION_onApplyHardwareResource = 2;
        static final int TRANSACTION_onEventHandle = 4;
        static final int TRANSACTION_onRestoreDefaultConfig = 3;
        static final int TRANSACTION_onUpdateGameInfo = 1;

        private static class Proxy implements IGameEngineCallback {
            public static IGameEngineCallback sDefaultImpl;
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

            public void onUpdateGameInfo(String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(data);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onUpdateGameInfo(data);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onApplyHardwareResource(String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(data);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onApplyHardwareResource(data);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onRestoreDefaultConfig() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onRestoreDefaultConfig();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onEventHandle(int event, String org1, String org2) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(event);
                    _data.writeString(org1);
                    _data.writeString(org2);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onEventHandle(event, org1, org2);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IGameEngineCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IGameEngineCallback)) {
                return new Proxy(obj);
            }
            return (IGameEngineCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onUpdateGameInfo";
            }
            if (transactionCode == 2) {
                return "onApplyHardwareResource";
            }
            if (transactionCode == 3) {
                return "onRestoreDefaultConfig";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "onEventHandle";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onUpdateGameInfo(data.readString());
                reply.writeNoException();
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onApplyHardwareResource(data.readString());
                reply.writeNoException();
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onRestoreDefaultConfig();
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                onEventHandle(data.readInt(), data.readString(), data.readString());
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IGameEngineCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IGameEngineCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onApplyHardwareResource(String str) throws RemoteException;

    void onEventHandle(int i, String str, String str2) throws RemoteException;

    void onRestoreDefaultConfig() throws RemoteException;

    void onUpdateGameInfo(String str) throws RemoteException;
}
