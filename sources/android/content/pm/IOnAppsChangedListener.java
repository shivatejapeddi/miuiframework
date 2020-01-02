package android.content.pm;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.os.UserHandle;

public interface IOnAppsChangedListener extends IInterface {

    public static class Default implements IOnAppsChangedListener {
        public void onPackageRemoved(UserHandle user, String packageName) throws RemoteException {
        }

        public void onPackageAdded(UserHandle user, String packageName) throws RemoteException {
        }

        public void onPackageChanged(UserHandle user, String packageName) throws RemoteException {
        }

        public void onPackagesAvailable(UserHandle user, String[] packageNames, boolean replacing) throws RemoteException {
        }

        public void onPackagesUnavailable(UserHandle user, String[] packageNames, boolean replacing) throws RemoteException {
        }

        public void onPackagesSuspended(UserHandle user, String[] packageNames, Bundle launcherExtras) throws RemoteException {
        }

        public void onPackagesUnsuspended(UserHandle user, String[] packageNames) throws RemoteException {
        }

        public void onShortcutChanged(UserHandle user, String packageName, ParceledListSlice shortcuts) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IOnAppsChangedListener {
        private static final String DESCRIPTOR = "android.content.pm.IOnAppsChangedListener";
        static final int TRANSACTION_onPackageAdded = 2;
        static final int TRANSACTION_onPackageChanged = 3;
        static final int TRANSACTION_onPackageRemoved = 1;
        static final int TRANSACTION_onPackagesAvailable = 4;
        static final int TRANSACTION_onPackagesSuspended = 6;
        static final int TRANSACTION_onPackagesUnavailable = 5;
        static final int TRANSACTION_onPackagesUnsuspended = 7;
        static final int TRANSACTION_onShortcutChanged = 8;

        private static class Proxy implements IOnAppsChangedListener {
            public static IOnAppsChangedListener sDefaultImpl;
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

            public void onPackageRemoved(UserHandle user, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(1, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPackageRemoved(user, packageName);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPackageAdded(UserHandle user, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(2, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPackageAdded(user, packageName);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPackageChanged(UserHandle user, String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (this.mRemote.transact(3, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPackageChanged(user, packageName);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPackagesAvailable(UserHandle user, String[] packageNames, boolean replacing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(packageNames);
                    if (replacing) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(4, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPackagesAvailable(user, packageNames, replacing);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPackagesUnavailable(UserHandle user, String[] packageNames, boolean replacing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 0;
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(packageNames);
                    if (replacing) {
                        i = 1;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(5, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPackagesUnavailable(user, packageNames, replacing);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPackagesSuspended(UserHandle user, String[] packageNames, Bundle launcherExtras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(packageNames);
                    if (launcherExtras != null) {
                        _data.writeInt(1);
                        launcherExtras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(6, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPackagesSuspended(user, packageNames, launcherExtras);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onPackagesUnsuspended(UserHandle user, String[] packageNames) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStringArray(packageNames);
                    if (this.mRemote.transact(7, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onPackagesUnsuspended(user, packageNames);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void onShortcutChanged(UserHandle user, String packageName, ParceledListSlice shortcuts) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(packageName);
                    if (shortcuts != null) {
                        _data.writeInt(1);
                        shortcuts.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().onShortcutChanged(user, packageName, shortcuts);
                    }
                } finally {
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IOnAppsChangedListener asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IOnAppsChangedListener)) {
                return new Proxy(obj);
            }
            return (IOnAppsChangedListener) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "onPackageRemoved";
                case 2:
                    return "onPackageAdded";
                case 3:
                    return "onPackageChanged";
                case 4:
                    return "onPackagesAvailable";
                case 5:
                    return "onPackagesUnavailable";
                case 6:
                    return "onPackagesSuspended";
                case 7:
                    return "onPackagesUnsuspended";
                case 8:
                    return "onShortcutChanged";
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
                boolean _arg2 = false;
                UserHandle _arg0;
                UserHandle _arg02;
                String[] _arg1;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (UserHandle) UserHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onPackageRemoved(_arg0, data.readString());
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (UserHandle) UserHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onPackageAdded(_arg0, data.readString());
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (UserHandle) UserHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onPackageChanged(_arg0, data.readString());
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (UserHandle) UserHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _arg1 = data.createStringArray();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        onPackagesAvailable(_arg02, _arg1, _arg2);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (UserHandle) UserHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _arg1 = data.createStringArray();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        onPackagesUnavailable(_arg02, _arg1, _arg2);
                        return true;
                    case 6:
                        Bundle _arg22;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (UserHandle) UserHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        String[] _arg12 = data.createStringArray();
                        if (data.readInt() != 0) {
                            _arg22 = (Bundle) Bundle.CREATOR.createFromParcel(data);
                        } else {
                            _arg22 = null;
                        }
                        onPackagesSuspended(_arg0, _arg12, _arg22);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (UserHandle) UserHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        onPackagesUnsuspended(_arg0, data.createStringArray());
                        return true;
                    case 8:
                        ParceledListSlice _arg23;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (UserHandle) UserHandle.CREATOR.createFromParcel(data);
                        } else {
                            _arg0 = null;
                        }
                        String _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(data);
                        } else {
                            _arg23 = null;
                        }
                        onShortcutChanged(_arg0, _arg13, _arg23);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IOnAppsChangedListener impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IOnAppsChangedListener getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void onPackageAdded(UserHandle userHandle, String str) throws RemoteException;

    void onPackageChanged(UserHandle userHandle, String str) throws RemoteException;

    void onPackageRemoved(UserHandle userHandle, String str) throws RemoteException;

    void onPackagesAvailable(UserHandle userHandle, String[] strArr, boolean z) throws RemoteException;

    void onPackagesSuspended(UserHandle userHandle, String[] strArr, Bundle bundle) throws RemoteException;

    void onPackagesUnavailable(UserHandle userHandle, String[] strArr, boolean z) throws RemoteException;

    void onPackagesUnsuspended(UserHandle userHandle, String[] strArr) throws RemoteException;

    void onShortcutChanged(UserHandle userHandle, String str, ParceledListSlice parceledListSlice) throws RemoteException;
}
