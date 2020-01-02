package android.security.keystore;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.security.keymaster.KeyCharacteristics;

public interface IKeystoreKeyCharacteristicsCallback extends IInterface {

    public static abstract class Stub extends Binder implements IKeystoreKeyCharacteristicsCallback {
        private static final String DESCRIPTOR = "android.security.keystore.IKeystoreKeyCharacteristicsCallback";
        static final int TRANSACTION_onFinished = 1;

        private static class Proxy implements IKeystoreKeyCharacteristicsCallback {
            public static IKeystoreKeyCharacteristicsCallback sDefaultImpl;
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

            public void onFinished(KeystoreResponse response, KeyCharacteristics charactersistics) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (charactersistics != null) {
                        _data.writeInt(1);
                        charactersistics.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onFinished(response, charactersistics);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IKeystoreKeyCharacteristicsCallback asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IKeystoreKeyCharacteristicsCallback)) {
                return new Proxy(obj);
            }
            return (IKeystoreKeyCharacteristicsCallback) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "onFinished";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                KeystoreResponse _arg0;
                KeyCharacteristics _arg1;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (KeystoreResponse) KeystoreResponse.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                if (data.readInt() != 0) {
                    _arg1 = (KeyCharacteristics) KeyCharacteristics.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                onFinished(_arg0, _arg1);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IKeystoreKeyCharacteristicsCallback impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IKeystoreKeyCharacteristicsCallback getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IKeystoreKeyCharacteristicsCallback {
        public void onFinished(KeystoreResponse response, KeyCharacteristics charactersistics) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    void onFinished(KeystoreResponse keystoreResponse, KeyCharacteristics keyCharacteristics) throws RemoteException;
}
