package android.service.notification;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IStatusBarNotificationHolder extends IInterface {

    public static class Default implements IStatusBarNotificationHolder {
        public StatusBarNotification get() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IStatusBarNotificationHolder {
        private static final String DESCRIPTOR = "android.service.notification.IStatusBarNotificationHolder";
        static final int TRANSACTION_get = 1;

        private static class Proxy implements IStatusBarNotificationHolder {
            public static IStatusBarNotificationHolder sDefaultImpl;
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

            public StatusBarNotification get() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    StatusBarNotification statusBarNotification = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        statusBarNotification = Stub.getDefaultImpl();
                        if (statusBarNotification != 0) {
                            statusBarNotification = Stub.getDefaultImpl().get();
                            return statusBarNotification;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        statusBarNotification = (StatusBarNotification) StatusBarNotification.CREATOR.createFromParcel(_reply);
                    } else {
                        statusBarNotification = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return statusBarNotification;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStatusBarNotificationHolder asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IStatusBarNotificationHolder)) {
                return new Proxy(obj);
            }
            return (IStatusBarNotificationHolder) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "get";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                StatusBarNotification _result = get();
                reply.writeNoException();
                if (_result != null) {
                    reply.writeInt(1);
                    _result.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IStatusBarNotificationHolder impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IStatusBarNotificationHolder getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    StatusBarNotification get() throws RemoteException;
}
