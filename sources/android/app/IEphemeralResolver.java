package android.app;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IRemoteCallback;
import android.os.Parcel;
import android.os.RemoteException;

public interface IEphemeralResolver extends IInterface {

    public static class Default implements IEphemeralResolver {
        public void getEphemeralResolveInfoList(IRemoteCallback callback, int[] digestPrefix, int sequence) throws RemoteException {
        }

        public void getEphemeralIntentFilterList(IRemoteCallback callback, String hostName, int sequence) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IEphemeralResolver {
        private static final String DESCRIPTOR = "android.app.IEphemeralResolver";
        static final int TRANSACTION_getEphemeralIntentFilterList = 2;
        static final int TRANSACTION_getEphemeralResolveInfoList = 1;

        private static class Proxy implements IEphemeralResolver {
            public static IEphemeralResolver sDefaultImpl;
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

            public void getEphemeralResolveInfoList(IRemoteCallback callback, int[] digestPrefix, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeIntArray(digestPrefix);
                    _data.writeInt(sequence);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getEphemeralResolveInfoList(callback, digestPrefix, sequence);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void getEphemeralIntentFilterList(IRemoteCallback callback, String hostName, int sequence) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    _data.writeString(hostName);
                    _data.writeInt(sequence);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().getEphemeralIntentFilterList(callback, hostName, sequence);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IEphemeralResolver asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IEphemeralResolver)) {
                return new Proxy(obj);
            }
            return (IEphemeralResolver) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getEphemeralResolveInfoList";
            }
            if (transactionCode != 2) {
                return null;
            }
            return "getEphemeralIntentFilterList";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                getEphemeralResolveInfoList(android.os.IRemoteCallback.Stub.asInterface(data.readStrongBinder()), data.createIntArray(), data.readInt());
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                getEphemeralIntentFilterList(android.os.IRemoteCallback.Stub.asInterface(data.readStrongBinder()), data.readString(), data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IEphemeralResolver impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IEphemeralResolver getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void getEphemeralIntentFilterList(IRemoteCallback iRemoteCallback, String str, int i) throws RemoteException;

    void getEphemeralResolveInfoList(IRemoteCallback iRemoteCallback, int[] iArr, int i) throws RemoteException;
}
