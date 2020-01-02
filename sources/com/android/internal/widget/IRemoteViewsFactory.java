package com.android.internal.widget;

import android.annotation.UnsupportedAppUsage;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.widget.RemoteViews;

public interface IRemoteViewsFactory extends IInterface {

    public static abstract class Stub extends Binder implements IRemoteViewsFactory {
        private static final String DESCRIPTOR = "com.android.internal.widget.IRemoteViewsFactory";
        static final int TRANSACTION_getCount = 4;
        static final int TRANSACTION_getItemId = 8;
        static final int TRANSACTION_getLoadingView = 6;
        static final int TRANSACTION_getViewAt = 5;
        static final int TRANSACTION_getViewTypeCount = 7;
        static final int TRANSACTION_hasStableIds = 9;
        static final int TRANSACTION_isCreated = 10;
        static final int TRANSACTION_onDataSetChanged = 1;
        static final int TRANSACTION_onDataSetChangedAsync = 2;
        static final int TRANSACTION_onDestroy = 3;

        private static class Proxy implements IRemoteViewsFactory {
            public static IRemoteViewsFactory sDefaultImpl;
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

            public void onDataSetChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onDataSetChanged();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onDataSetChangedAsync() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDataSetChangedAsync();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onDestroy(Intent intent) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (intent != null) {
                        _data.writeInt(1);
                        intent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onDestroy(intent);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public int getCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getCount();
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

            public RemoteViews getViewAt(int position) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(position);
                    RemoteViews remoteViews = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        remoteViews = Stub.getDefaultImpl();
                        if (remoteViews != 0) {
                            remoteViews = Stub.getDefaultImpl().getViewAt(position);
                            return remoteViews;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        remoteViews = (RemoteViews) RemoteViews.CREATOR.createFromParcel(_reply);
                    } else {
                        remoteViews = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return remoteViews;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public RemoteViews getLoadingView() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    RemoteViews remoteViews = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        remoteViews = Stub.getDefaultImpl();
                        if (remoteViews != 0) {
                            remoteViews = Stub.getDefaultImpl().getLoadingView();
                            return remoteViews;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        remoteViews = (RemoteViews) RemoteViews.CREATOR.createFromParcel(_reply);
                    } else {
                        remoteViews = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return remoteViews;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getViewTypeCount() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getViewTypeCount();
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

            public long getItemId(int position) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(position);
                    long j = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getItemId(position);
                            return j;
                        }
                    }
                    _reply.readException();
                    j = _reply.readLong();
                    long _result = j;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasStableIds() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasStableIds();
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

            public boolean isCreated() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isCreated();
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IRemoteViewsFactory asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IRemoteViewsFactory)) {
                return new Proxy(obj);
            }
            return (IRemoteViewsFactory) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onDataSetChanged";
                case 2:
                    return "onDataSetChangedAsync";
                case 3:
                    return "onDestroy";
                case 4:
                    return "getCount";
                case 5:
                    return "getViewAt";
                case 6:
                    return "getLoadingView";
                case 7:
                    return "getViewTypeCount";
                case 8:
                    return "getItemId";
                case 9:
                    return "hasStableIds";
                case 10:
                    return "isCreated";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            String descriptor = DESCRIPTOR;
            if (code != IBinder.INTERFACE_TRANSACTION) {
                int _result;
                boolean _result2;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        onDataSetChanged();
                        reply.writeNoException();
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        onDataSetChangedAsync();
                        return true;
                    case 3:
                        Intent _arg0;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Intent) Intent.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onDestroy(_arg0);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result = getCount();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        RemoteViews _result3 = getViewAt(data.readInt());
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        RemoteViews _result4 = getLoadingView();
                        reply.writeNoException();
                        if (_result4 != null) {
                            reply.writeInt(1);
                            _result4.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        _result = getViewTypeCount();
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        long _result5 = getItemId(data.readInt());
                        reply.writeNoException();
                        reply.writeLong(_result5);
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result2 = hasStableIds();
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        _result2 = isCreated();
                        reply.writeNoException();
                        reply.writeInt(_result2);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IRemoteViewsFactory impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IRemoteViewsFactory getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    public static class Default implements IRemoteViewsFactory {
        public void onDataSetChanged() throws RemoteException {
        }

        public void onDataSetChangedAsync() throws RemoteException {
        }

        public void onDestroy(Intent intent) throws RemoteException {
        }

        public int getCount() throws RemoteException {
            return 0;
        }

        public RemoteViews getViewAt(int position) throws RemoteException {
            return null;
        }

        public RemoteViews getLoadingView() throws RemoteException {
            return null;
        }

        public int getViewTypeCount() throws RemoteException {
            return 0;
        }

        public long getItemId(int position) throws RemoteException {
            return 0;
        }

        public boolean hasStableIds() throws RemoteException {
            return false;
        }

        public boolean isCreated() throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    @UnsupportedAppUsage
    int getCount() throws RemoteException;

    @UnsupportedAppUsage
    long getItemId(int i) throws RemoteException;

    @UnsupportedAppUsage
    RemoteViews getLoadingView() throws RemoteException;

    @UnsupportedAppUsage
    RemoteViews getViewAt(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getViewTypeCount() throws RemoteException;

    @UnsupportedAppUsage
    boolean hasStableIds() throws RemoteException;

    @UnsupportedAppUsage
    boolean isCreated() throws RemoteException;

    @UnsupportedAppUsage
    void onDataSetChanged() throws RemoteException;

    void onDataSetChangedAsync() throws RemoteException;

    void onDestroy(Intent intent) throws RemoteException;
}
