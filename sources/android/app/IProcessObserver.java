package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IProcessObserver extends IInterface {

    public static abstract class Stub extends Binder implements IProcessObserver {
        private static final String DESCRIPTOR = "android.app.IProcessObserver";
        static final int TRANSACTION_onForegroundActivitiesChanged = 1;
        static final int TRANSACTION_onForegroundServicesChanged = 2;
        static final int TRANSACTION_onProcessDied = 3;

        private static class Proxy implements IProcessObserver {
            public static IProcessObserver sDefaultImpl;
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

            public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeInt(foregroundActivities ? 1 : 0);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onForegroundActivitiesChanged(pid, uid, foregroundActivities);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    _data.writeInt(serviceTypes);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onForegroundServicesChanged(pid, uid, serviceTypes);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onProcessDied(int pid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(pid);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onProcessDied(pid, uid);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IProcessObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IProcessObserver)) {
                return new Proxy(obj);
            }
            return (IProcessObserver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onForegroundActivitiesChanged";
            }
            if (transactionCode == 2) {
                return "onForegroundServicesChanged";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "onProcessDied";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onForegroundActivitiesChanged(data.readInt(), data.readInt(), data.readInt() != 0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onForegroundServicesChanged(data.readInt(), data.readInt(), data.readInt());
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                onProcessDied(data.readInt(), data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IProcessObserver impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IProcessObserver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IProcessObserver {
        public void onForegroundActivitiesChanged(int pid, int uid, boolean foregroundActivities) throws RemoteException {
        }

        public void onForegroundServicesChanged(int pid, int uid, int serviceTypes) throws RemoteException {
        }

        public void onProcessDied(int pid, int uid) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onForegroundActivitiesChanged(int i, int i2, boolean z) throws RemoteException;

    void onForegroundServicesChanged(int i, int i2, int i3) throws RemoteException;

    void onProcessDied(int i, int i2) throws RemoteException;
}
