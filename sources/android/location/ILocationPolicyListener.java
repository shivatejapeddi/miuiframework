package android.location;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ILocationPolicyListener extends IInterface {

    public static class Default implements ILocationPolicyListener {
        public void onUidRulesChanged(int uid, int uidRules) throws RemoteException {
        }

        public void onRestrictBackgroundChanged(boolean restrictBackground) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ILocationPolicyListener {
        private static final String DESCRIPTOR = "android.location.ILocationPolicyListener";
        static final int TRANSACTION_onRestrictBackgroundChanged = 2;
        static final int TRANSACTION_onUidRulesChanged = 1;

        private static class Proxy implements ILocationPolicyListener {
            public static ILocationPolicyListener sDefaultImpl;
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

            public void onUidRulesChanged(int uid, int uidRules) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(uidRules);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onUidRulesChanged(uid, uidRules);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onRestrictBackgroundChanged(boolean restrictBackground) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(restrictBackground ? 1 : 0);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onRestrictBackgroundChanged(restrictBackground);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILocationPolicyListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ILocationPolicyListener)) {
                return new Proxy(obj);
            }
            return (ILocationPolicyListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onUidRulesChanged";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "onRestrictBackgroundChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onUidRulesChanged(data.readInt(), data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                onRestrictBackgroundChanged(data.readInt() != 0);
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ILocationPolicyListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ILocationPolicyListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onRestrictBackgroundChanged(boolean z) throws RemoteException;

    void onUidRulesChanged(int i, int i2) throws RemoteException;
}
