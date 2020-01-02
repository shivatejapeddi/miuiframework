package com.android.internal.appwidget;

import android.appwidget.AppWidgetProviderInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.RemoteViews;

public interface IAppWidgetHost extends IInterface {

    public static class Default implements IAppWidgetHost {
        public void updateAppWidget(int appWidgetId, RemoteViews views) throws RemoteException {
        }

        public void providerChanged(int appWidgetId, AppWidgetProviderInfo info) throws RemoteException {
        }

        public void providersChanged() throws RemoteException {
        }

        public void viewDataChanged(int appWidgetId, int viewId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IAppWidgetHost {
        private static final String DESCRIPTOR = "com.android.internal.appwidget.IAppWidgetHost";
        static final int TRANSACTION_providerChanged = 2;
        static final int TRANSACTION_providersChanged = 3;
        static final int TRANSACTION_updateAppWidget = 1;
        static final int TRANSACTION_viewDataChanged = 4;

        private static class Proxy implements IAppWidgetHost {
            public static IAppWidgetHost sDefaultImpl;
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

            public void updateAppWidget(int appWidgetId, RemoteViews views) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appWidgetId);
                    if (views != null) {
                        _data.writeInt(1);
                        views.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().updateAppWidget(appWidgetId, views);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void providerChanged(int appWidgetId, AppWidgetProviderInfo info) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appWidgetId);
                    if (info != null) {
                        _data.writeInt(1);
                        info.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().providerChanged(appWidgetId, info);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void providersChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().providersChanged();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void viewDataChanged(int appWidgetId, int viewId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appWidgetId);
                    _data.writeInt(viewId);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().viewDataChanged(appWidgetId, viewId);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IAppWidgetHost asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IAppWidgetHost)) {
                return new Proxy(obj);
            }
            return (IAppWidgetHost) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "updateAppWidget";
            }
            if (transactionCode == 2) {
                return "providerChanged";
            }
            if (transactionCode == 3) {
                return "providersChanged";
            }
            if (transactionCode != 4) {
                return null;
            }
            return "viewDataChanged";
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            int _arg0;
            if (code == 1) {
                RemoteViews _arg1;
                data.enforceInterface(descriptor);
                _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg1 = (RemoteViews) RemoteViews.CREATOR.createFromParcel(data);
                } else {
                    _arg1 = null;
                }
                updateAppWidget(_arg0, _arg1);
                return true;
            } else if (code == 2) {
                AppWidgetProviderInfo _arg12;
                data.enforceInterface(descriptor);
                _arg0 = data.readInt();
                if (data.readInt() != 0) {
                    _arg12 = (AppWidgetProviderInfo) AppWidgetProviderInfo.CREATOR.createFromParcel(data);
                } else {
                    _arg12 = null;
                }
                providerChanged(_arg0, _arg12);
                return true;
            } else if (code == 3) {
                data.enforceInterface(descriptor);
                providersChanged();
                return true;
            } else if (code == 4) {
                data.enforceInterface(descriptor);
                viewDataChanged(data.readInt(), data.readInt());
                return true;
            } else if (code != IBinder.INTERFACE_TRANSACTION) {
                return super.onTransact(code, data, reply, flags);
            } else {
                reply.writeString(descriptor);
                return true;
            }
        }

        public static boolean setDefaultImpl(IAppWidgetHost impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IAppWidgetHost getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void providerChanged(int i, AppWidgetProviderInfo appWidgetProviderInfo) throws RemoteException;

    void providersChanged() throws RemoteException;

    void updateAppWidget(int i, RemoteViews remoteViews) throws RemoteException;

    void viewDataChanged(int i, int i2) throws RemoteException;
}
