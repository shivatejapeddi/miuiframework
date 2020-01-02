package com.miui.whetstone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPowerKeeperClient extends IInterface {

    public static class Default implements IPowerKeeperClient {
        public void notifyFrozenAppWakeUpByHighPriorityAlarm(int uid) throws RemoteException {
        }

        public void perfThermalBreakAcquire(int boostTimeOut) throws RemoteException {
        }

        public void enableATrace(boolean enable, String processName) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPowerKeeperClient {
        private static final String DESCRIPTOR = "com.miui.whetstone.IPowerKeeperClient";
        static final int TRANSACTION_enableATrace = 3;
        static final int TRANSACTION_notifyFrozenAppWakeUpByHighPriorityAlarm = 1;
        static final int TRANSACTION_perfThermalBreakAcquire = 2;

        private static class Proxy implements IPowerKeeperClient {
            public static IPowerKeeperClient sDefaultImpl;
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

            public void notifyFrozenAppWakeUpByHighPriorityAlarm(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().notifyFrozenAppWakeUpByHighPriorityAlarm(uid);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void perfThermalBreakAcquire(int boostTimeOut) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(boostTimeOut);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().perfThermalBreakAcquire(boostTimeOut);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void enableATrace(boolean enable, String processName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeString(processName);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().enableATrace(enable, processName);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPowerKeeperClient asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPowerKeeperClient)) {
                return new Proxy(obj);
            }
            return (IPowerKeeperClient) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "notifyFrozenAppWakeUpByHighPriorityAlarm";
            }
            if (transactionCode == 2) {
                return "perfThermalBreakAcquire";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "enableATrace";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                notifyFrozenAppWakeUpByHighPriorityAlarm(data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                perfThermalBreakAcquire(data.readInt());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                enableATrace(data.readInt() != 0, data.readString());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IPowerKeeperClient impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPowerKeeperClient getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void enableATrace(boolean z, String str) throws RemoteException;

    void notifyFrozenAppWakeUpByHighPriorityAlarm(int i) throws RemoteException;

    void perfThermalBreakAcquire(int i) throws RemoteException;
}
