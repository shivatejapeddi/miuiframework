package android.view;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IAppTransitionAnimationSpecsFuture extends IInterface {

    public static class Default implements IAppTransitionAnimationSpecsFuture {
        public AppTransitionAnimationSpec[] get() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAppTransitionAnimationSpecsFuture {
        private static final String DESCRIPTOR = "android.view.IAppTransitionAnimationSpecsFuture";
        static final int TRANSACTION_get = 1;

        private static class Proxy implements IAppTransitionAnimationSpecsFuture {
            public static IAppTransitionAnimationSpecsFuture sDefaultImpl;
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

            public AppTransitionAnimationSpec[] get() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    AppTransitionAnimationSpec[] appTransitionAnimationSpecArr = 1;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        appTransitionAnimationSpecArr = Stub.getDefaultImpl();
                        if (appTransitionAnimationSpecArr != 0) {
                            appTransitionAnimationSpecArr = Stub.getDefaultImpl().get();
                            return appTransitionAnimationSpecArr;
                        }
                    }
                    _reply.readException();
                    AppTransitionAnimationSpec[] _result = (AppTransitionAnimationSpec[]) _reply.createTypedArray(AppTransitionAnimationSpec.CREATOR);
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

        public static IAppTransitionAnimationSpecsFuture asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAppTransitionAnimationSpecsFuture)) {
                return new Proxy(obj);
            }
            return (IAppTransitionAnimationSpecsFuture) iin;
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
                AppTransitionAnimationSpec[] _result = get();
                reply.writeNoException();
                reply.writeTypedArray(_result, 1);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IAppTransitionAnimationSpecsFuture impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAppTransitionAnimationSpecsFuture getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    AppTransitionAnimationSpec[] get() throws RemoteException;
}
