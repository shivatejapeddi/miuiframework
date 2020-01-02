package android.content;

import android.accounts.Account;
import android.annotation.UnsupportedAppUsage;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface ISyncAdapter extends IInterface {

    public static abstract class Stub extends Binder implements ISyncAdapter {
        private static final String DESCRIPTOR = "android.content.ISyncAdapter";
        static final int TRANSACTION_cancelSync = 3;
        static final int TRANSACTION_onUnsyncableAccount = 1;
        static final int TRANSACTION_startSync = 2;

        private static class Proxy implements ISyncAdapter {
            public static ISyncAdapter sDefaultImpl;
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

            public void onUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback cb) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(cb != null ? cb.asBinder() : null);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onUnsyncableAccount(cb);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void startSync(ISyncContext syncContext, String authority, Account account, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(syncContext != null ? syncContext.asBinder() : null);
                    _data.writeString(authority);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().startSync(syncContext, authority, account, extras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void cancelSync(ISyncContext syncContext) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(syncContext != null ? syncContext.asBinder() : null);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().cancelSync(syncContext);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ISyncAdapter asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISyncAdapter)) {
                return new Proxy(obj);
            }
            return (ISyncAdapter) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "onUnsyncableAccount";
            }
            if (transactionCode == 2) {
                return "startSync";
            }
            if (transactionCode != 3) {
                return null;
            }
            return "cancelSync";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code == 1) {
                data.enforceInterface(descriptor);
                onUnsyncableAccount(android.content.ISyncAdapterUnsyncableAccountCallback.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code == 2) {
                Account _arg2;
                Bundle _arg3;
                data.enforceInterface(descriptor);
                ISyncContext _arg0 = android.content.ISyncContext.Stub.asInterface(data.readStrongBinder());
                String _arg1 = data.readString();
                if (data.readInt() != 0) {
                    _arg2 = (Account) Account.CREATOR.createFromParcel(data);
                } else {
                    _arg2 = null;
                }
                if (data.readInt() != 0) {
                    _arg3 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg3 = null;
                }
                startSync(_arg0, _arg1, _arg2, _arg3);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                cancelSync(android.content.ISyncContext.Stub.asInterface(data.readStrongBinder()));
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(ISyncAdapter impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISyncAdapter getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements ISyncAdapter {
        public void onUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback cb) throws RemoteException {
        }

        public void startSync(ISyncContext syncContext, String authority, Account account, Bundle extras) throws RemoteException {
        }

        public void cancelSync(ISyncContext syncContext) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    @UnsupportedAppUsage
    void cancelSync(ISyncContext iSyncContext) throws RemoteException;

    @UnsupportedAppUsage
    void onUnsyncableAccount(ISyncAdapterUnsyncableAccountCallback iSyncAdapterUnsyncableAccountCallback) throws RemoteException;

    @UnsupportedAppUsage
    void startSync(ISyncContext iSyncContext, String str, Account account, Bundle bundle) throws RemoteException;
}
