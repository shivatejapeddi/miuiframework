package android.content.pm;

import android.appwidget.AppWidgetProviderInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IPinItemRequest extends IInterface {

    public static class Default implements IPinItemRequest {
        public boolean isValid() throws RemoteException {
            return false;
        }

        public boolean accept(Bundle options) throws RemoteException {
            return false;
        }

        public ShortcutInfo getShortcutInfo() throws RemoteException {
            return null;
        }

        public AppWidgetProviderInfo getAppWidgetProviderInfo() throws RemoteException {
            return null;
        }

        public Bundle getExtras() throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IPinItemRequest {
        private static final String DESCRIPTOR = "android.content.pm.IPinItemRequest";
        static final int TRANSACTION_accept = 2;
        static final int TRANSACTION_getAppWidgetProviderInfo = 4;
        static final int TRANSACTION_getExtras = 5;
        static final int TRANSACTION_getShortcutInfo = 3;
        static final int TRANSACTION_isValid = 1;

        private static class Proxy implements IPinItemRequest {
            public static IPinItemRequest sDefaultImpl;
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

            public boolean isValid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = false;
                    if (this.mRemote.transact(1, _data, _reply, z) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() != 0) {
                            z = true;
                        }
                        boolean _result = z;
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    z = Stub.getDefaultImpl().isValid();
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean accept(Bundle options) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (options != null) {
                        _data.writeInt(1);
                        options.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().accept(options);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ShortcutInfo getShortcutInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ShortcutInfo shortcutInfo = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        shortcutInfo = Stub.getDefaultImpl();
                        if (shortcutInfo != 0) {
                            shortcutInfo = Stub.getDefaultImpl().getShortcutInfo();
                            return shortcutInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        shortcutInfo = (ShortcutInfo) ShortcutInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        shortcutInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return shortcutInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public AppWidgetProviderInfo getAppWidgetProviderInfo() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    AppWidgetProviderInfo appWidgetProviderInfo = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        appWidgetProviderInfo = Stub.getDefaultImpl();
                        if (appWidgetProviderInfo != 0) {
                            appWidgetProviderInfo = Stub.getDefaultImpl().getAppWidgetProviderInfo();
                            return appWidgetProviderInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        appWidgetProviderInfo = (AppWidgetProviderInfo) AppWidgetProviderInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        appWidgetProviderInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return appWidgetProviderInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getExtras() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    Bundle bundle = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != 0) {
                            bundle = Stub.getDefaultImpl().getExtras();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IPinItemRequest asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IPinItemRequest)) {
                return new Proxy(obj);
            }
            return (IPinItemRequest) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "isValid";
            }
            if (transactionCode == 2) {
                return "accept";
            }
            if (transactionCode == 3) {
                return "getShortcutInfo";
            }
            if (transactionCode == 4) {
                return "getAppWidgetProviderInfo";
            }
            if (transactionCode != 5) {
                return null;
            }
            return "getExtras";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            Bundle _arg0;
            if (code == 1) {
                data.enforceInterface(descriptor);
                boolean _result = isValid();
                reply.writeNoException();
                reply.writeInt(_result);
                return true;
            } else if (code == 2) {
                data.enforceInterface(descriptor);
                if (data.readInt() != 0) {
                    _arg0 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                } else {
                    _arg0 = null;
                }
                boolean _result2 = accept(_arg0);
                reply.writeNoException();
                reply.writeInt(_result2);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                ShortcutInfo _result3 = getShortcutInfo();
                reply.writeNoException();
                if (_result3 != null) {
                    reply.writeInt(1);
                    _result3.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                AppWidgetProviderInfo _result4 = getAppWidgetProviderInfo();
                reply.writeNoException();
                if (_result4 != null) {
                    reply.writeInt(1);
                    _result4.writeToParcel(reply, 1);
                } else {
                    reply.writeInt(0);
                }
                return true;
            } else if (code == 5) {
                data.enforceInterface(descriptor);
                _arg0 = getExtras();
                reply.writeNoException();
                if (_arg0 != null) {
                    reply.writeInt(1);
                    _arg0.writeToParcel(reply, 1);
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

        public static boolean setDefaultImpl(IPinItemRequest impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IPinItemRequest getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    boolean accept(Bundle bundle) throws RemoteException;

    AppWidgetProviderInfo getAppWidgetProviderInfo() throws RemoteException;

    Bundle getExtras() throws RemoteException;

    ShortcutInfo getShortcutInfo() throws RemoteException;

    boolean isValid() throws RemoteException;
}
