package com.xiaomi.joyose;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IJoyoseInterface extends IInterface {

    public static class Default implements IJoyoseInterface {
        public void registerGameEngineListener(int mask, IGameEngineCallback callback) throws RemoteException {
        }

        public void unRegisterGameEngineListener(IGameEngineCallback callback) throws RemoteException {
        }

        public void handleGameBoosterForOneway(int cmd, String data) throws RemoteException {
        }

        public String handleGameBoosterForSync(int cmd, String data) throws RemoteException {
            return null;
        }

        public void predictAppBucketLevel(String packageName) throws RemoteException {
        }

        public void predictAppsBucketLevel(List<String> list, int id) throws RemoteException {
        }

        public void MiPlatformBoosterForOneway(int cmd, String data) throws RemoteException {
        }

        public String MiPlatformBoosterForSync(int cmd, String data) throws RemoteException {
            return null;
        }

        public List<String> getGameMotorAppList() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IJoyoseInterface {
        private static final String DESCRIPTOR = "com.xiaomi.joyose.IJoyoseInterface";
        static final int TRANSACTION_MiPlatformBoosterForOneway = 7;
        static final int TRANSACTION_MiPlatformBoosterForSync = 8;
        static final int TRANSACTION_getGameMotorAppList = 9;
        static final int TRANSACTION_handleGameBoosterForOneway = 3;
        static final int TRANSACTION_handleGameBoosterForSync = 4;
        static final int TRANSACTION_predictAppBucketLevel = 5;
        static final int TRANSACTION_predictAppsBucketLevel = 6;
        static final int TRANSACTION_registerGameEngineListener = 1;
        static final int TRANSACTION_unRegisterGameEngineListener = 2;

        private static class Proxy implements IJoyoseInterface {
            public static IJoyoseInterface sDefaultImpl;
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

            public void registerGameEngineListener(int mask, IGameEngineCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mask);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().registerGameEngineListener(mask, callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void unRegisterGameEngineListener(IGameEngineCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().unRegisterGameEngineListener(callback);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void handleGameBoosterForOneway(int cmd, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeString(data);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().handleGameBoosterForOneway(cmd, data);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public String handleGameBoosterForSync(int cmd, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeString(data);
                    String str = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().handleGameBoosterForSync(cmd, data);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void predictAppBucketLevel(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().predictAppBucketLevel(packageName);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void predictAppsBucketLevel(List<String> packageList, int id) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packageList);
                    _data.writeInt(id);
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().predictAppsBucketLevel(packageList, id);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void MiPlatformBoosterForOneway(int cmd, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeString(data);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().MiPlatformBoosterForOneway(cmd, data);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String MiPlatformBoosterForSync(int cmd, String data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(cmd);
                    _data.writeString(data);
                    String str = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().MiPlatformBoosterForSync(cmd, data);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getGameMotorAppList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<String> list = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getGameMotorAppList();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
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

        public static IJoyoseInterface asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IJoyoseInterface)) {
                return new Proxy(obj);
            }
            return (IJoyoseInterface) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "registerGameEngineListener";
                case 2:
                    return "unRegisterGameEngineListener";
                case 3:
                    return "handleGameBoosterForOneway";
                case 4:
                    return "handleGameBoosterForSync";
                case 5:
                    return "predictAppBucketLevel";
                case 6:
                    return "predictAppsBucketLevel";
                case 7:
                    return "MiPlatformBoosterForOneway";
                case 8:
                    return "MiPlatformBoosterForSync";
                case 9:
                    return "getGameMotorAppList";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                String _result;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        registerGameEngineListener(data.readInt(), com.xiaomi.joyose.IGameEngineCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        unRegisterGameEngineListener(com.xiaomi.joyose.IGameEngineCallback.Stub.asInterface(data.readStrongBinder()));
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        handleGameBoosterForOneway(data.readInt(), data.readString());
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result = handleGameBoosterForSync(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        predictAppBucketLevel(data.readString());
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        predictAppsBucketLevel(data.createStringArrayList(), data.readInt());
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        MiPlatformBoosterForOneway(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result = MiPlatformBoosterForSync(data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeString(_result);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        List<String> _result2 = getGameMotorAppList();
                        reply.writeNoException();
                        reply.writeStringList(_result2);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IJoyoseInterface impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IJoyoseInterface getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void MiPlatformBoosterForOneway(int i, String str) throws RemoteException;

    String MiPlatformBoosterForSync(int i, String str) throws RemoteException;

    List<String> getGameMotorAppList() throws RemoteException;

    void handleGameBoosterForOneway(int i, String str) throws RemoteException;

    String handleGameBoosterForSync(int i, String str) throws RemoteException;

    void predictAppBucketLevel(String str) throws RemoteException;

    void predictAppsBucketLevel(List<String> list, int i) throws RemoteException;

    void registerGameEngineListener(int i, IGameEngineCallback iGameEngineCallback) throws RemoteException;

    void unRegisterGameEngineListener(IGameEngineCallback iGameEngineCallback) throws RemoteException;
}
