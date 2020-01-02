package com.miui.whetstone;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IWhetstone extends IInterface {

    public static class Default implements IWhetstone {
        public int registerMiuiWhetstoneCloudSync(ComponentName name, CloudControlInfo info) throws RemoteException {
            return 0;
        }

        public int registerMiuiWhetstoneCloudSyncList(ComponentName name, List<CloudControlInfo> list) throws RemoteException {
            return 0;
        }

        public int unregisterMiuiWhetstoneCloudSync(ComponentName name) throws RemoteException {
            return 0;
        }

        public void log(int tag, byte[] data) throws RemoteException {
        }

        public void recordRTCWakeupInfo(int uid, PendingIntent operation, boolean status) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IWhetstone {
        private static final String DESCRIPTOR = "com.miui.whetstone.IWhetstone";
        static final int TRANSACTION_log = 4;
        static final int TRANSACTION_recordRTCWakeupInfo = 5;
        static final int TRANSACTION_registerMiuiWhetstoneCloudSync = 1;
        static final int TRANSACTION_registerMiuiWhetstoneCloudSyncList = 2;
        static final int TRANSACTION_unregisterMiuiWhetstoneCloudSync = 3;

        private static class Proxy implements IWhetstone {
            public static IWhetstone sDefaultImpl;
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

            public int registerMiuiWhetstoneCloudSync(ComponentName name, CloudControlInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().registerMiuiWhetstoneCloudSync(name, info);
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

            public int registerMiuiWhetstoneCloudSyncList(ComponentName name, List<CloudControlInfo> infos) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeTypedList(infos);
                    int i = this.mRemote;
                    if (!i.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().registerMiuiWhetstoneCloudSyncList(name, infos);
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

            public int unregisterMiuiWhetstoneCloudSync(ComponentName name) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (name != null) {
                        _data.writeInt(1);
                        name.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    int i = this.mRemote;
                    if (!i.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().unregisterMiuiWhetstoneCloudSync(name);
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

            public void log(int tag, byte[] data) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(tag);
                    _data.writeByteArray(data);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().log(tag, data);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void recordRTCWakeupInfo(int uid, PendingIntent operation, boolean status) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    int i = 0;
                    if (operation != null) {
                        _data.writeInt(1);
                        operation.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (status) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().recordRTCWakeupInfo(uid, operation, status);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IWhetstone asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IWhetstone)) {
                return new Proxy(obj);
            }
            return (IWhetstone) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "registerMiuiWhetstoneCloudSync";
            }
            if (transactionCode == 2) {
                return "registerMiuiWhetstoneCloudSyncList";
            }
            if (transactionCode == 3) {
                return "unregisterMiuiWhetstoneCloudSync";
            }
            if (transactionCode == 4) {
                return "log";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "recordRTCWakeupInfo";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            ComponentName _arg0;
            int _result;
            if (code == 1) {
                CloudControlInfo _arg1;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                if (data.readInt() != 0) {
                    _arg1 = (CloudControlInfo) CloudControlInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                _result = registerMiuiWhetstoneCloudSync(_arg0, _arg1);
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                _result = registerMiuiWhetstoneCloudSyncList(_arg0, data.createTypedArrayList(CloudControlInfo.CREATOR));
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                int _result2 = unregisterMiuiWhetstoneCloudSync(_arg0);
                reply.writeNoException();
                reply.writeInt(_result2);
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                log(data.readInt(), data.createByteArray());
                return true;
            } else if (code == 5) {
                PendingIntent _arg12;
                data.enforceInterface(descriptor);
                int _arg02 = data.readInt();
                if (data.readInt() != 0) {
                    _arg12 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                } else {
                    _arg12 = null;
                }
                recordRTCWakeupInfo(_arg02, _arg12, data.readInt() != 0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IWhetstone impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IWhetstone getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void log(int i, byte[] bArr) throws RemoteException;

    void recordRTCWakeupInfo(int i, PendingIntent pendingIntent, boolean z) throws RemoteException;

    int registerMiuiWhetstoneCloudSync(ComponentName componentName, CloudControlInfo cloudControlInfo) throws RemoteException;

    int registerMiuiWhetstoneCloudSyncList(ComponentName componentName, List<CloudControlInfo> list) throws RemoteException;

    int unregisterMiuiWhetstoneCloudSync(ComponentName componentName) throws RemoteException;
}
