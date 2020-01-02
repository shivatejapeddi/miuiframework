package android.app.timezone;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;

public interface IRulesManager extends IInterface {

    public static class Default implements IRulesManager {
        public RulesState getRulesState() throws RemoteException {
            return null;
        }

        public int requestInstall(ParcelFileDescriptor distroFileDescriptor, byte[] checkToken, ICallback callback) throws RemoteException {
            return 0;
        }

        public int requestUninstall(byte[] checkToken, ICallback callback) throws RemoteException {
            return 0;
        }

        public void requestNothing(byte[] token, boolean success) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRulesManager {
        private static final String DESCRIPTOR = "android.app.timezone.IRulesManager";
        static final int TRANSACTION_getRulesState = 1;
        static final int TRANSACTION_requestInstall = 2;
        static final int TRANSACTION_requestNothing = 4;
        static final int TRANSACTION_requestUninstall = 3;

        private static class Proxy implements IRulesManager {
            public static IRulesManager sDefaultImpl;
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

            public RulesState getRulesState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    RulesState rulesState = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        rulesState = Stub.getDefaultImpl();
                        if (rulesState != 0) {
                            rulesState = Stub.getDefaultImpl().getRulesState();
                            return rulesState;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        rulesState = (RulesState) RulesState.CREATOR.createFromParcel(_reply);
                    } else {
                        rulesState = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return rulesState;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestInstall(ParcelFileDescriptor distroFileDescriptor, byte[] checkToken, ICallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (distroFileDescriptor != null) {
                        _data.writeInt(1);
                        distroFileDescriptor.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeByteArray(checkToken);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    int i = this.mRemote;
                    if (!i.transact(2, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().requestInstall(distroFileDescriptor, checkToken, callback);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int requestUninstall(byte[] checkToken, ICallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(checkToken);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    int i = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().requestUninstall(checkToken, callback);
                            return i;
                        }
                    }
                    _reply.readException();
                    i = _reply.readInt();
                    int _result = i;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestNothing(byte[] token, boolean success) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeByteArray(token);
                    _data.writeInt(success ? 1 : 0);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestNothing(token, success);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRulesManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRulesManager)) {
                return new Proxy(obj);
            }
            return (IRulesManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getRulesState";
            }
            if (transactionCode == 2) {
                return "requestInstall";
            }
            if (transactionCode == 3) {
                return "requestUninstall";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "requestNothing";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            boolean _arg1 = false;
            if (code == 1) {
                data.enforceInterface(descriptor);
                RulesState _result = getRulesState();
                reply.writeNoException();
                if (_result != null) {
                    reply.writeInt(1);
                    _result.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 2) {
                ParcelFileDescriptor _arg0;
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                int _result2 = requestInstall(_arg0, data.createByteArray(), android.app.timezone.ICallback.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeInt(_result2);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                int _result3 = requestUninstall(data.createByteArray(), android.app.timezone.ICallback.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                reply.writeInt(_result3);
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                byte[] _arg02 = data.createByteArray();
                if (data.readInt() != 0) {
                    _arg1 = true;
                }
                requestNothing(_arg02, _arg1);
                reply.writeNoException();
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IRulesManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRulesManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    RulesState getRulesState() throws RemoteException;

    int requestInstall(ParcelFileDescriptor parcelFileDescriptor, byte[] bArr, ICallback iCallback) throws RemoteException;

    void requestNothing(byte[] bArr, boolean z) throws RemoteException;

    int requestUninstall(byte[] bArr, ICallback iCallback) throws RemoteException;
}
