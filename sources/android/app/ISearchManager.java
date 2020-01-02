package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.ComponentName;
import android.content.pm.ResolveInfo;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface ISearchManager extends IInterface {

    public static class Default implements ISearchManager {
        public SearchableInfo getSearchableInfo(ComponentName launchActivity) throws RemoteException {
            return null;
        }

        public List<SearchableInfo> getSearchablesInGlobalSearch() throws RemoteException {
            return null;
        }

        public List<ResolveInfo> getGlobalSearchActivities() throws RemoteException {
            return null;
        }

        public ComponentName getGlobalSearchActivity() throws RemoteException {
            return null;
        }

        public ComponentName getWebSearchActivity() throws RemoteException {
            return null;
        }

        public void launchAssist(Bundle args) throws RemoteException {
        }

        public boolean launchLegacyAssist(String hint, int userHandle, Bundle args) throws RemoteException {
            return false;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ISearchManager {
        private static final String DESCRIPTOR = "android.app.ISearchManager";
        static final int TRANSACTION_getGlobalSearchActivities = 3;
        static final int TRANSACTION_getGlobalSearchActivity = 4;
        static final int TRANSACTION_getSearchableInfo = 1;
        static final int TRANSACTION_getSearchablesInGlobalSearch = 2;
        static final int TRANSACTION_getWebSearchActivity = 5;
        static final int TRANSACTION_launchAssist = 6;
        static final int TRANSACTION_launchLegacyAssist = 7;

        private static class Proxy implements ISearchManager {
            public static ISearchManager sDefaultImpl;
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

            public SearchableInfo getSearchableInfo(ComponentName launchActivity) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    SearchableInfo searchableInfo = 0;
                    if (launchActivity != null) {
                        _data.writeInt(1);
                        launchActivity.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(1, _data, _reply, 0)) {
                        searchableInfo = Stub.getDefaultImpl();
                        if (searchableInfo != 0) {
                            searchableInfo = Stub.getDefaultImpl().getSearchableInfo(launchActivity);
                            return searchableInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        searchableInfo = (SearchableInfo) SearchableInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        searchableInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return searchableInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<SearchableInfo> getSearchablesInGlobalSearch() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<SearchableInfo> list = 2;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getSearchablesInGlobalSearch();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(SearchableInfo.CREATOR);
                    List<SearchableInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<ResolveInfo> getGlobalSearchActivities() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<ResolveInfo> list = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getGlobalSearchActivities();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(ResolveInfo.CREATOR);
                    List<ResolveInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getGlobalSearchActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName componentName = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getGlobalSearchActivity();
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ComponentName getWebSearchActivity() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    ComponentName componentName = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        componentName = Stub.getDefaultImpl();
                        if (componentName != 0) {
                            componentName = Stub.getDefaultImpl().getWebSearchActivity();
                            return componentName;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        componentName = (ComponentName) ComponentName.CREATOR.createFromParcel(_reply);
                    } else {
                        componentName = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return componentName;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void launchAssist(Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().launchAssist(args);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean launchLegacyAssist(String hint, int userHandle, Bundle args) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(hint);
                    _data.writeInt(userHandle);
                    boolean _result = true;
                    if (args != null) {
                        _data.writeInt(1);
                        args.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().launchLegacyAssist(hint, userHandle, args);
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

        public static ISearchManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ISearchManager)) {
                return new Proxy(obj);
            }
            return (ISearchManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "getSearchableInfo";
                case 2:
                    return "getSearchablesInGlobalSearch";
                case 3:
                    return "getGlobalSearchActivities";
                case 4:
                    return "getGlobalSearchActivity";
                case 5:
                    return "getWebSearchActivity";
                case 6:
                    return "launchAssist";
                case 7:
                    return "launchLegacyAssist";
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
                ComponentName _arg0;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        SearchableInfo _result = getSearchableInfo(_arg0);
                        reply.writeNoException();
                        if (_result != null) {
                            reply.writeInt(1);
                            _result.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        List<SearchableInfo> _result2 = getSearchablesInGlobalSearch();
                        reply.writeNoException();
                        reply.writeTypedList(_result2);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        List<ResolveInfo> _result3 = getGlobalSearchActivities();
                        reply.writeNoException();
                        reply.writeTypedList(_result3);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _arg0 = getGlobalSearchActivity();
                        reply.writeNoException();
                        if (_arg0 != null) {
                            reply.writeInt(1);
                            _arg0.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _arg0 = getWebSearchActivity();
                        reply.writeNoException();
                        if (_arg0 != null) {
                            reply.writeInt(1);
                            _arg0.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 6:
                        Bundle _arg02;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        launchAssist(_arg02);
                        reply.writeNoException();
                        return true;
                    case 7:
                        Bundle _arg2;
                        data.enforceInterface(descriptor);
                        String _arg03 = data.readString();
                        int _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg2 = null;
                        }
                        boolean _result4 = launchLegacyAssist(_arg03, _arg1, _arg2);
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ISearchManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ISearchManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    List<ResolveInfo> getGlobalSearchActivities() throws RemoteException;

    @UnsupportedAppUsage
    ComponentName getGlobalSearchActivity() throws RemoteException;

    SearchableInfo getSearchableInfo(ComponentName componentName) throws RemoteException;

    List<SearchableInfo> getSearchablesInGlobalSearch() throws RemoteException;

    ComponentName getWebSearchActivity() throws RemoteException;

    void launchAssist(Bundle bundle) throws RemoteException;

    boolean launchLegacyAssist(String str, int i, Bundle bundle) throws RemoteException;
}
