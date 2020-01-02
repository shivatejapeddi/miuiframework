package android.media.tv;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ITvRemoteProvider extends IInterface {

    public static class Default implements ITvRemoteProvider {
        public void setRemoteServiceInputSink(ITvRemoteServiceInput tvServiceInput) throws RemoteException {
        }

        public void onInputBridgeConnected(IBinder token) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ITvRemoteProvider {
        private static final String DESCRIPTOR = "android.media.tv.ITvRemoteProvider";
        static final int TRANSACTION_onInputBridgeConnected = 2;
        static final int TRANSACTION_setRemoteServiceInputSink = 1;

        private static class Proxy implements ITvRemoteProvider {
            public static ITvRemoteProvider sDefaultImpl;
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

            public void setRemoteServiceInputSink(ITvRemoteServiceInput tvServiceInput) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(tvServiceInput != null ? tvServiceInput.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setRemoteServiceInputSink(tvServiceInput);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onInputBridgeConnected(IBinder token) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(token);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onInputBridgeConnected(token);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ITvRemoteProvider asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ITvRemoteProvider)) {
                return new Proxy(obj);
            }
            return (ITvRemoteProvider) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "setRemoteServiceInputSink";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onInputBridgeConnected";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                setRemoteServiceInputSink(android.media.tv.ITvRemoteServiceInput.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onInputBridgeConnected(data.readStrongBinder());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ITvRemoteProvider impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ITvRemoteProvider getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onInputBridgeConnected(IBinder iBinder) throws RemoteException;

    void setRemoteServiceInputSink(ITvRemoteServiceInput iTvRemoteServiceInput) throws RemoteException;
}
