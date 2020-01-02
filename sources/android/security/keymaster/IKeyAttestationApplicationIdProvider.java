package android.security.keymaster;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IKeyAttestationApplicationIdProvider extends IInterface {

    public static class Default implements IKeyAttestationApplicationIdProvider {
        public KeyAttestationApplicationId getKeyAttestationApplicationId(int uid) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IKeyAttestationApplicationIdProvider {
        private static final String DESCRIPTOR = "android.security.keymaster.IKeyAttestationApplicationIdProvider";
        static final int TRANSACTION_getKeyAttestationApplicationId = 1;

        private static class Proxy implements IKeyAttestationApplicationIdProvider {
            public static IKeyAttestationApplicationIdProvider sDefaultImpl;
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

            public KeyAttestationApplicationId getKeyAttestationApplicationId(int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    KeyAttestationApplicationId keyAttestationApplicationId = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        keyAttestationApplicationId = Stub.getDefaultImpl();
                        if (keyAttestationApplicationId != 0) {
                            keyAttestationApplicationId = Stub.getDefaultImpl().getKeyAttestationApplicationId(uid);
                            return keyAttestationApplicationId;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        keyAttestationApplicationId = (KeyAttestationApplicationId) KeyAttestationApplicationId.CREATOR.createFromParcel(_reply);
                    } else {
                        keyAttestationApplicationId = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return keyAttestationApplicationId;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IKeyAttestationApplicationIdProvider asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IKeyAttestationApplicationIdProvider)) {
                return new Proxy(obj);
            }
            return (IKeyAttestationApplicationIdProvider) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode != 1) {
                return null;
            }
            return "getKeyAttestationApplicationId";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                KeyAttestationApplicationId _result = getKeyAttestationApplicationId(data.readInt());
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

        public static boolean setDefaultImpl(IKeyAttestationApplicationIdProvider impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IKeyAttestationApplicationIdProvider getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    KeyAttestationApplicationId getKeyAttestationApplicationId(int i) throws RemoteException;
}
