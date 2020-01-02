package android.app;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IMiuiActivityObserver extends IInterface {

    public static class Default implements IMiuiActivityObserver {
        public void activityIdle(Intent intent) throws RemoteException {
        }

        public void activityResumed(Intent intent) throws RemoteException {
        }

        public void activityPaused(Intent intent) throws RemoteException {
        }

        public void activityStopped(Intent intent) throws RemoteException {
        }

        public void activityDestroyed(Intent intent) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IMiuiActivityObserver {
        private static final String DESCRIPTOR = "android.app.IMiuiActivityObserver";
        static final int TRANSACTION_activityDestroyed = 5;
        static final int TRANSACTION_activityIdle = 1;
        static final int TRANSACTION_activityPaused = 3;
        static final int TRANSACTION_activityResumed = 2;
        static final int TRANSACTION_activityStopped = 4;

        private static class Proxy implements IMiuiActivityObserver {
            public static IMiuiActivityObserver sDefaultImpl;
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

            public void activityIdle(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().activityIdle(intent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void activityResumed(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().activityResumed(intent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void activityPaused(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().activityPaused(intent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void activityStopped(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().activityStopped(intent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void activityDestroyed(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().activityDestroyed(intent);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IMiuiActivityObserver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IMiuiActivityObserver)) {
                return new Proxy(obj);
            }
            return (IMiuiActivityObserver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "activityIdle";
            }
            if (transactionCode == 2) {
                return "activityResumed";
            }
            if (transactionCode == 3) {
                return "activityPaused";
            }
            if (transactionCode == 4) {
                return "activityStopped";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "activityDestroyed";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            Intent _arg0;
            if (code == 1) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Intent) Intent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                activityIdle(_arg0);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Intent) Intent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                activityResumed(_arg0);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Intent) Intent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                activityPaused(_arg0);
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Intent) Intent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                activityStopped(_arg0);
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Intent) Intent.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                activityDestroyed(_arg0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IMiuiActivityObserver impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IMiuiActivityObserver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void activityDestroyed(Intent intent) throws RemoteException;

    void activityIdle(Intent intent) throws RemoteException;

    void activityPaused(Intent intent) throws RemoteException;

    void activityResumed(Intent intent) throws RemoteException;

    void activityStopped(Intent intent) throws RemoteException;
}
