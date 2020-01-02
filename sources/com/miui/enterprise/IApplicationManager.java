package com.miui.enterprise;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver2;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IApplicationManager extends IInterface {

    public static class Default implements IApplicationManager {
        public void installPackage(String path, int flag, IPackageInstallObserver2 observer, int userId) throws RemoteException {
        }

        public void installPackageWithPendingIntent(String path, PendingIntent pendingIntent, int userId) throws RemoteException {
        }

        public void deletePackage(String packageName, int flag, IPackageDeleteObserver observer, int userId) throws RemoteException {
        }

        public void setApplicationSettings(String packageName, int flags, int userId) throws RemoteException {
        }

        public int getApplicationSettings(String packageName, int userId) throws RemoteException {
            return 0;
        }

        public boolean setDeviceAdmin(ComponentName component, int userId) throws RemoteException {
            return false;
        }

        public boolean removeDeviceAdmin(ComponentName component, int userId) throws RemoteException {
            return false;
        }

        public void setApplicationBlackList(List<String> list, int userId) throws RemoteException {
        }

        public List<String> getApplicationBlackList(int userId) throws RemoteException {
            return null;
        }

        public void setApplicationWhiteList(List<String> list, int userId) throws RemoteException {
        }

        public List<String> getApplicationWhiteList(int userId) throws RemoteException {
            return null;
        }

        public void setApplicationRestriction(int mode, int userId) throws RemoteException {
        }

        public int getApplicationRestriction(int userId) throws RemoteException {
            return 0;
        }

        public void setDisallowedRunningAppList(List<String> list, int userId) throws RemoteException {
        }

        public List<String> getDisallowedRunningAppList(int userId) throws RemoteException {
            return null;
        }

        public void killProcess(String packageName, int userId) throws RemoteException {
        }

        public void enableAccessibilityService(ComponentName componentName, boolean enabled) throws RemoteException {
        }

        public void clearApplicationUserData(String packageName, int userId) throws RemoteException {
        }

        public void clearApplicationCache(String packageName, int userId) throws RemoteException {
        }

        public void addTrustedAppStore(List<String> list, int userId) throws RemoteException {
        }

        public List<String> getTrustedAppStore(int userId) throws RemoteException {
            return null;
        }

        public void enableTrustedAppStore(boolean enabld, int userId) throws RemoteException {
        }

        public boolean isTrustedAppStoreEnabled(int userId) throws RemoteException {
            return false;
        }

        public void setApplicationEnabled(String packageName, boolean enable, int userId) throws RemoteException {
        }

        public void enableNotifications(String pkg, boolean enabled) throws RemoteException {
        }

        public void setNotificaitonFilter(String pkg, String channelId, String type, boolean allow) throws RemoteException {
        }

        public void setXSpaceBlack(List<String> list) throws RemoteException {
        }

        public List<String> getXSpaceBlack() throws RemoteException {
            return null;
        }

        public void grantRuntimePermission(String packageName, String permission, int userId) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IApplicationManager {
        private static final String DESCRIPTOR = "com.miui.enterprise.IApplicationManager";
        static final int TRANSACTION_addTrustedAppStore = 20;
        static final int TRANSACTION_clearApplicationCache = 19;
        static final int TRANSACTION_clearApplicationUserData = 18;
        static final int TRANSACTION_deletePackage = 3;
        static final int TRANSACTION_enableAccessibilityService = 17;
        static final int TRANSACTION_enableNotifications = 25;
        static final int TRANSACTION_enableTrustedAppStore = 22;
        static final int TRANSACTION_getApplicationBlackList = 9;
        static final int TRANSACTION_getApplicationRestriction = 13;
        static final int TRANSACTION_getApplicationSettings = 5;
        static final int TRANSACTION_getApplicationWhiteList = 11;
        static final int TRANSACTION_getDisallowedRunningAppList = 15;
        static final int TRANSACTION_getTrustedAppStore = 21;
        static final int TRANSACTION_getXSpaceBlack = 28;
        static final int TRANSACTION_grantRuntimePermission = 29;
        static final int TRANSACTION_installPackage = 1;
        static final int TRANSACTION_installPackageWithPendingIntent = 2;
        static final int TRANSACTION_isTrustedAppStoreEnabled = 23;
        static final int TRANSACTION_killProcess = 16;
        static final int TRANSACTION_removeDeviceAdmin = 7;
        static final int TRANSACTION_setApplicationBlackList = 8;
        static final int TRANSACTION_setApplicationEnabled = 24;
        static final int TRANSACTION_setApplicationRestriction = 12;
        static final int TRANSACTION_setApplicationSettings = 4;
        static final int TRANSACTION_setApplicationWhiteList = 10;
        static final int TRANSACTION_setDeviceAdmin = 6;
        static final int TRANSACTION_setDisallowedRunningAppList = 14;
        static final int TRANSACTION_setNotificaitonFilter = 26;
        static final int TRANSACTION_setXSpaceBlack = 27;

        private static class Proxy implements IApplicationManager {
            public static IApplicationManager sDefaultImpl;
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

            public void installPackage(String path, int flag, IPackageInstallObserver2 observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    _data.writeInt(flag);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().installPackage(path, flag, observer, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void installPackageWithPendingIntent(String path, PendingIntent pendingIntent, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    if (pendingIntent != null) {
                        _data.writeInt(1);
                        pendingIntent.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().installPackageWithPendingIntent(path, pendingIntent, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deletePackage(String packageName, int flag, IPackageDeleteObserver observer, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flag);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deletePackage(packageName, flag, observer, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setApplicationSettings(String packageName, int flags, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationSettings(packageName, flags, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getApplicationSettings(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    int i = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getApplicationSettings(packageName, userId);
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

            public boolean setDeviceAdmin(ComponentName component, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().setDeviceAdmin(component, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean removeDeviceAdmin(ComponentName component, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().removeDeviceAdmin(component, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setApplicationBlackList(List<String> packages, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packages);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationBlackList(packages, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getApplicationBlackList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getApplicationBlackList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setApplicationWhiteList(List<String> packages, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packages);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationWhiteList(packages, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getApplicationWhiteList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getApplicationWhiteList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setApplicationRestriction(int mode, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mode);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationRestriction(mode, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getApplicationRestriction(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    int i = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getApplicationRestriction(userId);
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

            public void setDisallowedRunningAppList(List<String> packages, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packages);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDisallowedRunningAppList(packages, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getDisallowedRunningAppList(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getDisallowedRunningAppList(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void killProcess(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().killProcess(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableAccessibilityService(ComponentName componentName, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (componentName != null) {
                        _data.writeInt(1);
                        componentName.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!enabled) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableAccessibilityService(componentName, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearApplicationUserData(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearApplicationUserData(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearApplicationCache(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearApplicationCache(packageName, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addTrustedAppStore(List<String> packages, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packages);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addTrustedAppStore(packages, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getTrustedAppStore(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<String> list = 21;
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getTrustedAppStore(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableTrustedAppStore(boolean enabld, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(enabld ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableTrustedAppStore(enabld, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isTrustedAppStoreEnabled(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isTrustedAppStoreEnabled(userId);
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

            public void setApplicationEnabled(String packageName, boolean enable, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(enable ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setApplicationEnabled(packageName, enable, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void enableNotifications(String pkg, boolean enabled) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeInt(enabled ? 1 : 0);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().enableNotifications(pkg, enabled);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setNotificaitonFilter(String pkg, String channelId, String type, boolean allow) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(pkg);
                    _data.writeString(channelId);
                    _data.writeString(type);
                    _data.writeInt(allow ? 1 : 0);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setNotificaitonFilter(pkg, channelId, type, allow);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setXSpaceBlack(List<String> packages) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringList(packages);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setXSpaceBlack(packages);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<String> getXSpaceBlack() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<String> list = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getXSpaceBlack();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createStringArrayList();
                    List<String> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void grantRuntimePermission(String packageName, String permission, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(permission);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().grantRuntimePermission(packageName, permission, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IApplicationManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IApplicationManager)) {
                return new Proxy(obj);
            }
            return (IApplicationManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "installPackage";
                case 2:
                    return "installPackageWithPendingIntent";
                case 3:
                    return "deletePackage";
                case 4:
                    return "setApplicationSettings";
                case 5:
                    return "getApplicationSettings";
                case 6:
                    return "setDeviceAdmin";
                case 7:
                    return "removeDeviceAdmin";
                case 8:
                    return "setApplicationBlackList";
                case 9:
                    return "getApplicationBlackList";
                case 10:
                    return "setApplicationWhiteList";
                case 11:
                    return "getApplicationWhiteList";
                case 12:
                    return "setApplicationRestriction";
                case 13:
                    return "getApplicationRestriction";
                case 14:
                    return "setDisallowedRunningAppList";
                case 15:
                    return "getDisallowedRunningAppList";
                case 16:
                    return "killProcess";
                case 17:
                    return "enableAccessibilityService";
                case 18:
                    return "clearApplicationUserData";
                case 19:
                    return "clearApplicationCache";
                case 20:
                    return "addTrustedAppStore";
                case 21:
                    return "getTrustedAppStore";
                case 22:
                    return "enableTrustedAppStore";
                case 23:
                    return "isTrustedAppStoreEnabled";
                case 24:
                    return "setApplicationEnabled";
                case 25:
                    return "enableNotifications";
                case 26:
                    return "setNotificaitonFilter";
                case 27:
                    return "setXSpaceBlack";
                case 28:
                    return "getXSpaceBlack";
                case 29:
                    return "grantRuntimePermission";
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
                boolean _arg0 = false;
                ComponentName _arg02;
                boolean _result;
                List<String> _result2;
                String _arg03;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        installPackage(data.readString(), data.readInt(), android.content.pm.IPackageInstallObserver2.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        PendingIntent _arg1;
                        data.enforceInterface(descriptor);
                        String _arg04 = data.readString();
                        if (data.readInt() != 0) {
                            _arg1 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(data);
                        } else {
                            _arg1 = null;
                        }
                        installPackageWithPendingIntent(_arg04, _arg1, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        deletePackage(data.readString(), data.readInt(), android.content.pm.IPackageDeleteObserver.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        setApplicationSettings(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        int _result3 = getApplicationSettings(data.readString(), data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result3);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result = setDeviceAdmin(_arg02, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg02 = null;
                        }
                        _result = removeDeviceAdmin(_arg02, data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        setApplicationBlackList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result2 = getApplicationBlackList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result2);
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        setApplicationWhiteList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 11:
                        data.enforceInterface(descriptor);
                        _result2 = getApplicationWhiteList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result2);
                        return true;
                    case 12:
                        data.enforceInterface(descriptor);
                        setApplicationRestriction(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        int _result4 = getApplicationRestriction(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result4);
                        return true;
                    case 14:
                        data.enforceInterface(descriptor);
                        setDisallowedRunningAppList(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 15:
                        data.enforceInterface(descriptor);
                        _result2 = getDisallowedRunningAppList(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result2);
                        return true;
                    case 16:
                        data.enforceInterface(descriptor);
                        killProcess(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 17:
                        ComponentName _arg05;
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (ComponentName) ComponentName.CREATOR.createFromParcel(data);
                        } else {
                            _arg05 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        enableAccessibilityService(_arg05, _arg0);
                        reply.writeNoException();
                        return true;
                    case 18:
                        data.enforceInterface(descriptor);
                        clearApplicationUserData(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 19:
                        data.enforceInterface(descriptor);
                        clearApplicationCache(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 20:
                        data.enforceInterface(descriptor);
                        addTrustedAppStore(data.createStringArrayList(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 21:
                        data.enforceInterface(descriptor);
                        _result2 = getTrustedAppStore(data.readInt());
                        reply.writeNoException();
                        reply.writeStringList(_result2);
                        return true;
                    case 22:
                        data.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        enableTrustedAppStore(_arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        data.enforceInterface(descriptor);
                        boolean _result5 = isTrustedAppStoreEnabled(data.readInt());
                        reply.writeNoException();
                        reply.writeInt(_result5);
                        return true;
                    case 24:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setApplicationEnabled(_arg03, _arg0, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 25:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        enableNotifications(_arg03, _arg0);
                        reply.writeNoException();
                        return true;
                    case 26:
                        data.enforceInterface(descriptor);
                        _arg03 = data.readString();
                        String _arg12 = data.readString();
                        String _arg2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg0 = true;
                        }
                        setNotificaitonFilter(_arg03, _arg12, _arg2, _arg0);
                        reply.writeNoException();
                        return true;
                    case 27:
                        data.enforceInterface(descriptor);
                        setXSpaceBlack(data.createStringArrayList());
                        reply.writeNoException();
                        return true;
                    case 28:
                        data.enforceInterface(descriptor);
                        List<String> _result6 = getXSpaceBlack();
                        reply.writeNoException();
                        reply.writeStringList(_result6);
                        return true;
                    case 29:
                        data.enforceInterface(descriptor);
                        grantRuntimePermission(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IApplicationManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IApplicationManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addTrustedAppStore(List<String> list, int i) throws RemoteException;

    void clearApplicationCache(String str, int i) throws RemoteException;

    void clearApplicationUserData(String str, int i) throws RemoteException;

    void deletePackage(String str, int i, IPackageDeleteObserver iPackageDeleteObserver, int i2) throws RemoteException;

    void enableAccessibilityService(ComponentName componentName, boolean z) throws RemoteException;

    void enableNotifications(String str, boolean z) throws RemoteException;

    void enableTrustedAppStore(boolean z, int i) throws RemoteException;

    List<String> getApplicationBlackList(int i) throws RemoteException;

    int getApplicationRestriction(int i) throws RemoteException;

    int getApplicationSettings(String str, int i) throws RemoteException;

    List<String> getApplicationWhiteList(int i) throws RemoteException;

    List<String> getDisallowedRunningAppList(int i) throws RemoteException;

    List<String> getTrustedAppStore(int i) throws RemoteException;

    List<String> getXSpaceBlack() throws RemoteException;

    void grantRuntimePermission(String str, String str2, int i) throws RemoteException;

    void installPackage(String str, int i, IPackageInstallObserver2 iPackageInstallObserver2, int i2) throws RemoteException;

    void installPackageWithPendingIntent(String str, PendingIntent pendingIntent, int i) throws RemoteException;

    boolean isTrustedAppStoreEnabled(int i) throws RemoteException;

    void killProcess(String str, int i) throws RemoteException;

    boolean removeDeviceAdmin(ComponentName componentName, int i) throws RemoteException;

    void setApplicationBlackList(List<String> list, int i) throws RemoteException;

    void setApplicationEnabled(String str, boolean z, int i) throws RemoteException;

    void setApplicationRestriction(int i, int i2) throws RemoteException;

    void setApplicationSettings(String str, int i, int i2) throws RemoteException;

    void setApplicationWhiteList(List<String> list, int i) throws RemoteException;

    boolean setDeviceAdmin(ComponentName componentName, int i) throws RemoteException;

    void setDisallowedRunningAppList(List<String> list, int i) throws RemoteException;

    void setNotificaitonFilter(String str, String str2, String str3, boolean z) throws RemoteException;

    void setXSpaceBlack(List<String> list) throws RemoteException;
}
