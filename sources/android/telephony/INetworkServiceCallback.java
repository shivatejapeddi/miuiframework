package android.telephony;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface INetworkServiceCallback extends IInterface {

    public static class Default implements INetworkServiceCallback {
        public void onRequestNetworkRegistrationInfoComplete(int result, NetworkRegistrationInfo state) throws RemoteException {
        }

        public void onNetworkStateChanged() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements INetworkServiceCallback {
        private static final String DESCRIPTOR = "android.telephony.INetworkServiceCallback";
        static final int TRANSACTION_onNetworkStateChanged = 2;
        static final int TRANSACTION_onRequestNetworkRegistrationInfoComplete = 1;

        private static class Proxy implements INetworkServiceCallback {
            public static INetworkServiceCallback sDefaultImpl;
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

            public void onRequestNetworkRegistrationInfoComplete(int result, NetworkRegistrationInfo state) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(result);
                    if (state != null) {
                        _data.writeInt(1);
                        state.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRequestNetworkRegistrationInfoComplete(result, state);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onNetworkStateChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onNetworkStateChanged();
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static INetworkServiceCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof INetworkServiceCallback)) {
                return new Proxy(obj);
            }
            return (INetworkServiceCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onRequestNetworkRegistrationInfoComplete";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onNetworkStateChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                NetworkRegistrationInfo _arg1;
                data.enforceInterface(descriptor);
                int _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (NetworkRegistrationInfo) NetworkRegistrationInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                onRequestNetworkRegistrationInfoComplete(_arg0, _arg1);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onNetworkStateChanged();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(INetworkServiceCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static INetworkServiceCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onNetworkStateChanged() throws RemoteException;

    void onRequestNetworkRegistrationInfoComplete(int i, NetworkRegistrationInfo networkRegistrationInfo) throws RemoteException;
}
