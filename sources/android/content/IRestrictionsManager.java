package android.content;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.PersistableBundle;
import android.os.RemoteException;

public interface IRestrictionsManager extends IInterface {

    public static class Default implements IRestrictionsManager {
        public Bundle getApplicationRestrictions(String packageName) throws RemoteException {
            return null;
        }

        public boolean hasRestrictionsProvider() throws RemoteException {
            return false;
        }

        public void requestPermission(String packageName, String requestType, String requestId, PersistableBundle requestData) throws RemoteException {
        }

        public void notifyPermissionResponse(String packageName, PersistableBundle response) throws RemoteException {
        }

        public Intent createLocalApprovalIntent() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IRestrictionsManager {
        private static final String DESCRIPTOR = "android.content.IRestrictionsManager";
        static final int TRANSACTION_createLocalApprovalIntent = 5;
        static final int TRANSACTION_getApplicationRestrictions = 1;
        static final int TRANSACTION_hasRestrictionsProvider = 2;
        static final int TRANSACTION_notifyPermissionResponse = 4;
        static final int TRANSACTION_requestPermission = 3;

        private static class Proxy implements IRestrictionsManager {
            public static IRestrictionsManager sDefaultImpl;
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

            public Bundle getApplicationRestrictions(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    Bundle bundle = true;
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getApplicationRestrictions(packageName);
                            return bundle;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        bundle = (Bundle) Bundle.CREATOR.createFromParcel(_reply);
                    } else {
                        bundle = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return bundle;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasRestrictionsProvider() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasRestrictionsProvider();
                            return z;
                        }
                    }
                    _reply.readException();
                    z = _reply.readInt();
                    if (z) {
                        z2 = true;
                    }
                    boolean _result = z2;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void requestPermission(String packageName, String requestType, String requestId, PersistableBundle requestData) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(requestType);
                    _data.writeString(requestId);
                    if (requestData != null) {
                        _data.writeInt(1);
                        requestData.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestPermission(packageName, requestType, requestId, requestData);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyPermissionResponse(String packageName, PersistableBundle response) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (response != null) {
                        _data.writeInt(1);
                        response.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().notifyPermissionResponse(packageName, response);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Intent createLocalApprovalIntent() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Intent intent = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        intent = Stub.getDefaultImpl();
                        if (intent != 0) {
                            intent = Stub.getDefaultImpl().createLocalApprovalIntent();
                            return intent;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        intent = (Intent) Intent.CREATOR.createFromParcel(_reply);
                    } else {
                        intent = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return intent;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRestrictionsManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRestrictionsManager)) {
                return new Proxy(obj);
            }
            return (IRestrictionsManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "getApplicationRestrictions";
            }
            if (transactionCode == 2) {
                return "hasRestrictionsProvider";
            }
            if (transactionCode == 3) {
                return "requestPermission";
            }
            if (transactionCode == 4) {
                return "notifyPermissionResponse";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "createLocalApprovalIntent";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            String _arg0;
            if (code == 1) {
                data.enforceInterface(descriptor);
                Bundle _result = getApplicationRestrictions(data.readString());
                reply.writeNoException();
                if (_result != null) {
                    reply.writeInt(1);
                    _result.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                boolean _result2 = hasRestrictionsProvider();
                reply.writeNoException();
                reply.writeInt(_result2);
                return true;
            } else if (code == 3) {
                PersistableBundle _arg3;
                data.enforceInterface(descriptor);
                _arg0 = data.readString();
                String _arg1 = data.readString();
                String _arg2 = data.readString();
                if (data.readInt() != 0) {
                    _arg3 = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(data);
                } else {
                    _arg3 = null;
                }
                requestPermission(_arg0, _arg1, _arg2, _arg3);
                reply.writeNoException();
                return true;
            } else if (code == 4) {
                PersistableBundle _arg12;
                data.enforceInterface(descriptor);
                _arg0 = data.readString();
                if (data.readInt() != 0) {
                    _arg12 = (PersistableBundle) PersistableBundle.CREATOR.createFromParcel(data);
                } else {
                    _arg12 = null;
                }
                notifyPermissionResponse(_arg0, _arg12);
                reply.writeNoException();
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                Intent _result3 = createLocalApprovalIntent();
                reply.writeNoException();
                if (_result3 != null) {
                    reply.writeInt(1);
                    _result3.writeToParcel(reply, 1);
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

        public static boolean setDefaultImpl(IRestrictionsManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRestrictionsManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    Intent createLocalApprovalIntent() throws RemoteException;

    Bundle getApplicationRestrictions(String str) throws RemoteException;

    boolean hasRestrictionsProvider() throws RemoteException;

    void notifyPermissionResponse(String str, PersistableBundle persistableBundle) throws RemoteException;

    void requestPermission(String str, String str2, String str3, PersistableBundle persistableBundle) throws RemoteException;
}
