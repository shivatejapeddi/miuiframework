package android.content.pm;

import android.app.IApplicationThread;
import android.content.ComponentName;
import android.content.IntentSender;
import android.content.pm.LauncherApps.AppUsageLimit;
import android.content.pm.PackageInstaller.SessionInfo;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.UserHandle;
import java.util.List;

public interface ILauncherApps extends IInterface {

    public static class Default implements ILauncherApps {
        public void addOnAppsChangedListener(String callingPackage, IOnAppsChangedListener listener) throws RemoteException {
        }

        public void removeOnAppsChangedListener(IOnAppsChangedListener listener) throws RemoteException {
        }

        public ParceledListSlice getLauncherActivities(String callingPackage, String packageName, UserHandle user) throws RemoteException {
            return null;
        }

        public ActivityInfo resolveActivity(String callingPackage, ComponentName component, UserHandle user) throws RemoteException {
            return null;
        }

        public void startSessionDetailsActivityAsUser(IApplicationThread caller, String callingPackage, SessionInfo sessionInfo, Rect sourceBounds, Bundle opts, UserHandle user) throws RemoteException {
        }

        public void startActivityAsUser(IApplicationThread caller, String callingPackage, ComponentName component, Rect sourceBounds, Bundle opts, UserHandle user) throws RemoteException {
        }

        public void showAppDetailsAsUser(IApplicationThread caller, String callingPackage, ComponentName component, Rect sourceBounds, Bundle opts, UserHandle user) throws RemoteException {
        }

        public boolean isPackageEnabled(String callingPackage, String packageName, UserHandle user) throws RemoteException {
            return false;
        }

        public Bundle getSuspendedPackageLauncherExtras(String packageName, UserHandle user) throws RemoteException {
            return null;
        }

        public boolean isActivityEnabled(String callingPackage, ComponentName component, UserHandle user) throws RemoteException {
            return false;
        }

        public ApplicationInfo getApplicationInfo(String callingPackage, String packageName, int flags, UserHandle user) throws RemoteException {
            return null;
        }

        public AppUsageLimit getAppUsageLimit(String callingPackage, String packageName, UserHandle user) throws RemoteException {
            return null;
        }

        public ParceledListSlice getShortcuts(String callingPackage, long changedSince, String packageName, List shortcutIds, ComponentName componentName, int flags, UserHandle user) throws RemoteException {
            return null;
        }

        public void pinShortcuts(String callingPackage, String packageName, List<String> list, UserHandle user) throws RemoteException {
        }

        public boolean startShortcut(String callingPackage, String packageName, String id, Rect sourceBounds, Bundle startActivityOptions, int userId) throws RemoteException {
            return false;
        }

        public int getShortcutIconResId(String callingPackage, String packageName, String id, int userId) throws RemoteException {
            return 0;
        }

        public ParcelFileDescriptor getShortcutIconFd(String callingPackage, String packageName, String id, int userId) throws RemoteException {
            return null;
        }

        public boolean hasShortcutHostPermission(String callingPackage) throws RemoteException {
            return false;
        }

        public boolean shouldHideFromSuggestions(String packageName, UserHandle user) throws RemoteException {
            return false;
        }

        public ParceledListSlice getShortcutConfigActivities(String callingPackage, String packageName, UserHandle user) throws RemoteException {
            return null;
        }

        public IntentSender getShortcutConfigActivityIntent(String callingPackage, ComponentName component, UserHandle user) throws RemoteException {
            return null;
        }

        public void registerPackageInstallerCallback(String callingPackage, IPackageInstallerCallback callback) throws RemoteException {
        }

        public ParceledListSlice getAllSessions(String callingPackage) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements ILauncherApps {
        private static final String DESCRIPTOR = "android.content.pm.ILauncherApps";
        static final int TRANSACTION_addOnAppsChangedListener = 1;
        static final int TRANSACTION_getAllSessions = 23;
        static final int TRANSACTION_getAppUsageLimit = 12;
        static final int TRANSACTION_getApplicationInfo = 11;
        static final int TRANSACTION_getLauncherActivities = 3;
        static final int TRANSACTION_getShortcutConfigActivities = 20;
        static final int TRANSACTION_getShortcutConfigActivityIntent = 21;
        static final int TRANSACTION_getShortcutIconFd = 17;
        static final int TRANSACTION_getShortcutIconResId = 16;
        static final int TRANSACTION_getShortcuts = 13;
        static final int TRANSACTION_getSuspendedPackageLauncherExtras = 9;
        static final int TRANSACTION_hasShortcutHostPermission = 18;
        static final int TRANSACTION_isActivityEnabled = 10;
        static final int TRANSACTION_isPackageEnabled = 8;
        static final int TRANSACTION_pinShortcuts = 14;
        static final int TRANSACTION_registerPackageInstallerCallback = 22;
        static final int TRANSACTION_removeOnAppsChangedListener = 2;
        static final int TRANSACTION_resolveActivity = 4;
        static final int TRANSACTION_shouldHideFromSuggestions = 19;
        static final int TRANSACTION_showAppDetailsAsUser = 7;
        static final int TRANSACTION_startActivityAsUser = 6;
        static final int TRANSACTION_startSessionDetailsActivityAsUser = 5;
        static final int TRANSACTION_startShortcut = 15;

        private static class Proxy implements ILauncherApps {
            public static ILauncherApps sDefaultImpl;
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

            public void addOnAppsChangedListener(String callingPackage, IOnAppsChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addOnAppsChangedListener(callingPackage, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeOnAppsChangedListener(IOnAppsChangedListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeOnAppsChangedListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getLauncherActivities(String callingPackage, String packageName, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(3, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().getLauncherActivities(callingPackage, packageName, user);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ActivityInfo resolveActivity(String callingPackage, ComponentName component, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    ActivityInfo activityInfo = 0;
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        activityInfo = Stub.getDefaultImpl();
                        if (activityInfo != 0) {
                            activityInfo = Stub.getDefaultImpl().resolveActivity(callingPackage, component, user);
                            return activityInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        activityInfo = (ActivityInfo) ActivityInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        activityInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return activityInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startSessionDetailsActivityAsUser(IApplicationThread caller, String callingPackage, SessionInfo sessionInfo, Rect sourceBounds, Bundle opts, UserHandle user) throws RemoteException {
                Throwable th;
                SessionInfo sessionInfo2 = sessionInfo;
                Rect rect = sourceBounds;
                Bundle bundle = opts;
                UserHandle userHandle = user;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callingPackage);
                        if (sessionInfo2 != null) {
                            _data.writeInt(1);
                            sessionInfo2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (rect != null) {
                            _data.writeInt(1);
                            rect.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (userHandle != null) {
                            _data.writeInt(1);
                            userHandle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().startSessionDetailsActivityAsUser(caller, callingPackage, sessionInfo, sourceBounds, opts, user);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void startActivityAsUser(IApplicationThread caller, String callingPackage, ComponentName component, Rect sourceBounds, Bundle opts, UserHandle user) throws RemoteException {
                Throwable th;
                ComponentName componentName = component;
                Rect rect = sourceBounds;
                Bundle bundle = opts;
                UserHandle userHandle = user;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callingPackage);
                        if (componentName != null) {
                            _data.writeInt(1);
                            componentName.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (rect != null) {
                            _data.writeInt(1);
                            rect.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (userHandle != null) {
                            _data.writeInt(1);
                            userHandle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().startActivityAsUser(caller, callingPackage, component, sourceBounds, opts, user);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void showAppDetailsAsUser(IApplicationThread caller, String callingPackage, ComponentName component, Rect sourceBounds, Bundle opts, UserHandle user) throws RemoteException {
                Throwable th;
                ComponentName componentName = component;
                Rect rect = sourceBounds;
                Bundle bundle = opts;
                UserHandle userHandle = user;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(caller != null ? caller.asBinder() : null);
                    try {
                        _data.writeString(callingPackage);
                        if (componentName != null) {
                            _data.writeInt(1);
                            componentName.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (rect != null) {
                            _data.writeInt(1);
                            rect.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (bundle != null) {
                            _data.writeInt(1);
                            bundle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (userHandle != null) {
                            _data.writeInt(1);
                            userHandle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().showAppDetailsAsUser(caller, callingPackage, component, sourceBounds, opts, user);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public boolean isPackageEnabled(String callingPackage, String packageName, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    boolean _result = true;
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isPackageEnabled(callingPackage, packageName, user);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getSuspendedPackageLauncherExtras(String packageName, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    Bundle bundle = this.mRemote;
                    if (!bundle.transact(9, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != null) {
                            bundle = Stub.getDefaultImpl().getSuspendedPackageLauncherExtras(packageName, user);
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

            public boolean isActivityEnabled(String callingPackage, ComponentName component, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean _result = true;
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isActivityEnabled(callingPackage, component, user);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ApplicationInfo getApplicationInfo(String callingPackage, String packageName, int flags, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    ApplicationInfo applicationInfo = this.mRemote;
                    if (!applicationInfo.transact(11, _data, _reply, 0)) {
                        applicationInfo = Stub.getDefaultImpl();
                        if (applicationInfo != null) {
                            applicationInfo = Stub.getDefaultImpl().getApplicationInfo(callingPackage, packageName, flags, user);
                            return applicationInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        applicationInfo = (ApplicationInfo) ApplicationInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        applicationInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return applicationInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public AppUsageLimit getAppUsageLimit(String callingPackage, String packageName, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    AppUsageLimit appUsageLimit = this.mRemote;
                    if (!appUsageLimit.transact(12, _data, _reply, 0)) {
                        appUsageLimit = Stub.getDefaultImpl();
                        if (appUsageLimit != null) {
                            appUsageLimit = Stub.getDefaultImpl().getAppUsageLimit(callingPackage, packageName, user);
                            return appUsageLimit;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        appUsageLimit = (AppUsageLimit) AppUsageLimit.CREATOR.createFromParcel(_reply);
                    } else {
                        appUsageLimit = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return appUsageLimit;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getShortcuts(String callingPackage, long changedSince, String packageName, List shortcutIds, ComponentName componentName, int flags, UserHandle user) throws RemoteException {
                Throwable th;
                String str;
                ComponentName componentName2 = componentName;
                UserHandle userHandle = user;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(callingPackage);
                        _data.writeLong(changedSince);
                    } catch (Throwable th2) {
                        th = th2;
                        str = packageName;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(packageName);
                        _data.writeList(shortcutIds);
                        if (componentName2 != null) {
                            _data.writeInt(1);
                            componentName2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeInt(flags);
                        if (userHandle != null) {
                            _data.writeInt(1);
                            userHandle.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        ParceledListSlice _result;
                        if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() != 0) {
                                _result = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                            } else {
                                _result = null;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().getShortcuts(callingPackage, changedSince, packageName, shortcutIds, componentName, flags, user);
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    } catch (Throwable th3) {
                        th = th3;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    String str2 = callingPackage;
                    str = packageName;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void pinShortcuts(String callingPackage, String packageName, List<String> shortcutIds, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    _data.writeStringList(shortcutIds);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().pinShortcuts(callingPackage, packageName, shortcutIds, user);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean startShortcut(String callingPackage, String packageName, String id, Rect sourceBounds, Bundle startActivityOptions, int userId) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                int i;
                Rect rect = sourceBounds;
                Bundle bundle = startActivityOptions;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(callingPackage);
                    } catch (Throwable th2) {
                        th = th2;
                        str = packageName;
                        str2 = id;
                        i = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        boolean _result;
                        _data.writeString(packageName);
                        try {
                            _data.writeString(id);
                            _result = true;
                            if (rect != null) {
                                _data.writeInt(1);
                                rect.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            if (bundle != null) {
                                _data.writeInt(1);
                                bundle.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th3) {
                            th = th3;
                            i = userId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(userId);
                            if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() == 0) {
                                    _result = false;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().startShortcut(callingPackage, packageName, id, sourceBounds, startActivityOptions, userId);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        str2 = id;
                        i = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str3 = callingPackage;
                    str = packageName;
                    str2 = id;
                    i = userId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public int getShortcutIconResId(String callingPackage, String packageName, String id, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    _data.writeString(id);
                    _data.writeInt(userId);
                    int i = 16;
                    if (!this.mRemote.transact(16, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getShortcutIconResId(callingPackage, packageName, id, userId);
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

            public ParcelFileDescriptor getShortcutIconFd(String callingPackage, String packageName, String id, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    _data.writeString(id);
                    _data.writeInt(userId);
                    ParcelFileDescriptor parcelFileDescriptor = 17;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().getShortcutIconFd(callingPackage, packageName, id, userId);
                            return parcelFileDescriptor;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parcelFileDescriptor = (ParcelFileDescriptor) ParcelFileDescriptor.CREATOR.createFromParcel(_reply);
                    } else {
                        parcelFileDescriptor = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parcelFileDescriptor;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean hasShortcutHostPermission(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().hasShortcutHostPermission(callingPackage);
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

            public boolean shouldHideFromSuggestions(String packageName, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    boolean _result = true;
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().shouldHideFromSuggestions(packageName, user);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getShortcutConfigActivities(String callingPackage, String packageName, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeString(packageName);
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    ParceledListSlice parceledListSlice = this.mRemote;
                    if (!parceledListSlice.transact(20, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != null) {
                            parceledListSlice = Stub.getDefaultImpl().getShortcutConfigActivities(callingPackage, packageName, user);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public IntentSender getShortcutConfigActivityIntent(String callingPackage, ComponentName component, UserHandle user) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    IntentSender intentSender = 0;
                    if (component != null) {
                        _data.writeInt(1);
                        component.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (user != null) {
                        _data.writeInt(1);
                        user.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(21, _data, _reply, 0)) {
                        intentSender = Stub.getDefaultImpl();
                        if (intentSender != 0) {
                            intentSender = Stub.getDefaultImpl().getShortcutConfigActivityIntent(callingPackage, component, user);
                            return intentSender;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        intentSender = (IntentSender) IntentSender.CREATOR.createFromParcel(_reply);
                    } else {
                        intentSender = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return intentSender;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerPackageInstallerCallback(String callingPackage, IPackageInstallerCallback callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerPackageInstallerCallback(callingPackage, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getAllSessions(String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    ParceledListSlice parceledListSlice = 23;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getAllSessions(callingPackage);
                            return parceledListSlice;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        parceledListSlice = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(_reply);
                    } else {
                        parceledListSlice = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return parceledListSlice;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static ILauncherApps asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof ILauncherApps)) {
                return new Proxy(obj);
            }
            return (ILauncherApps) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "addOnAppsChangedListener";
                case 2:
                    return "removeOnAppsChangedListener";
                case 3:
                    return "getLauncherActivities";
                case 4:
                    return "resolveActivity";
                case 5:
                    return "startSessionDetailsActivityAsUser";
                case 6:
                    return "startActivityAsUser";
                case 7:
                    return "showAppDetailsAsUser";
                case 8:
                    return "isPackageEnabled";
                case 9:
                    return "getSuspendedPackageLauncherExtras";
                case 10:
                    return "isActivityEnabled";
                case 11:
                    return "getApplicationInfo";
                case 12:
                    return "getAppUsageLimit";
                case 13:
                    return "getShortcuts";
                case 14:
                    return "pinShortcuts";
                case 15:
                    return "startShortcut";
                case 16:
                    return "getShortcutIconResId";
                case 17:
                    return "getShortcutIconFd";
                case 18:
                    return "hasShortcutHostPermission";
                case 19:
                    return "shouldHideFromSuggestions";
                case 20:
                    return "getShortcutConfigActivities";
                case 21:
                    return "getShortcutConfigActivityIntent";
                case 22:
                    return "registerPackageInstallerCallback";
                case 23:
                    return "getAllSessions";
                default:
                    return null;
            }
        }

        public String getTransactionName(int transactionCode) {
            return getDefaultTransactionName(transactionCode);
        }

        public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            int i = code;
            Parcel parcel = data;
            Parcel parcel2 = reply;
            String descriptor = DESCRIPTOR;
            if (i != 1598968902) {
                String _arg0;
                String _arg1;
                UserHandle _arg2;
                ParceledListSlice _result;
                ComponentName _arg12;
                IApplicationThread _arg02;
                String _arg13;
                Rect _arg3;
                Bundle _arg4;
                UserHandle _arg5;
                ComponentName _arg22;
                boolean _result2;
                UserHandle _arg14;
                UserHandle _arg32;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        addOnAppsChangedListener(data.readString(), android.content.pm.IOnAppsChangedListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        removeOnAppsChangedListener(android.content.pm.IOnAppsChangedListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result = getLauncherActivities(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        ActivityInfo _result3 = resolveActivity(_arg0, _arg12, _arg2);
                        reply.writeNoException();
                        if (_result3 != null) {
                            parcel2.writeInt(1);
                            _result3.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        SessionInfo _arg23;
                        parcel.enforceInterface(descriptor);
                        _arg02 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (SessionInfo) SessionInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg5 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        startSessionDetailsActivityAsUser(_arg02, _arg13, _arg23, _arg3, _arg4, _arg5);
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _arg02 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg5 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        startActivityAsUser(_arg02, _arg13, _arg22, _arg3, _arg4, _arg5);
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _arg02 = android.app.IApplicationThread.Stub.asInterface(data.readStrongBinder());
                        _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg3 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg5 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        showAppDetailsAsUser(_arg02, _arg13, _arg22, _arg3, _arg4, _arg5);
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result2 = isPackageEnabled(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg14 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        Bundle _result4 = getSuspendedPackageLauncherExtras(_arg0, _arg14);
                        reply.writeNoException();
                        if (_result4 != null) {
                            parcel2.writeInt(1);
                            _result4.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result2 = isActivityEnabled(_arg0, _arg12, _arg2);
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readString();
                        int _arg24 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg32 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        ApplicationInfo _result5 = getApplicationInfo(_arg0, _arg1, _arg24, _arg32);
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        AppUsageLimit _result6 = getAppUsageLimit(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        if (_result6 != null) {
                            parcel2.writeInt(1);
                            _result6.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 13:
                        ComponentName _arg42;
                        UserHandle _arg6;
                        parcel.enforceInterface(descriptor);
                        String _arg03 = data.readString();
                        long _arg15 = data.readLong();
                        String _arg25 = data.readString();
                        ClassLoader cl = getClass().getClassLoader();
                        List _arg33 = parcel.readArrayList(cl);
                        if (data.readInt() != 0) {
                            _arg42 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        int _arg52 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg6 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg6 = null;
                        }
                        ParceledListSlice _result7 = getShortcuts(_arg03, _arg15, _arg25, _arg33, _arg42, _arg52, _arg6);
                        reply.writeNoException();
                        if (_result7 != null) {
                            parcel2.writeInt(1);
                            _result7.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readString();
                        List<String> _arg26 = data.createStringArrayList();
                        if (data.readInt() != 0) {
                            _arg32 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg32 = null;
                        }
                        pinShortcuts(_arg0, _arg1, _arg26, _arg32);
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        String _arg04 = data.readString();
                        _arg13 = data.readString();
                        String _arg27 = data.readString();
                        if (data.readInt() != 0) {
                            _arg3 = (Rect) Rect.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg4 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        boolean _result8 = startShortcut(_arg04, _arg13, _arg27, _arg3, _arg4, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        int _result9 = getShortcutIconResId(data.readString(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result9);
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        ParcelFileDescriptor _result10 = getShortcutIconFd(data.readString(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result10 != null) {
                            parcel2.writeInt(1);
                            _result10.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        boolean _result11 = hasShortcutHostPermission(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result11);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg14 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        boolean _result12 = shouldHideFromSuggestions(_arg0, _arg14);
                        reply.writeNoException();
                        parcel2.writeInt(_result12);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        _result = getShortcutConfigActivities(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg12 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg12 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg2 = (UserHandle) UserHandle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg2 = null;
                        }
                        IntentSender _result13 = getShortcutConfigActivityIntent(_arg0, _arg12, _arg2);
                        reply.writeNoException();
                        if (_result13 != null) {
                            parcel2.writeInt(1);
                            _result13.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        registerPackageInstallerCallback(data.readString(), android.content.pm.IPackageInstallerCallback.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result14 = getAllSessions(data.readString());
                        reply.writeNoException();
                        if (_result14 != null) {
                            parcel2.writeInt(1);
                            _result14.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(ILauncherApps impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static ILauncherApps getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addOnAppsChangedListener(String str, IOnAppsChangedListener iOnAppsChangedListener) throws RemoteException;

    ParceledListSlice getAllSessions(String str) throws RemoteException;

    AppUsageLimit getAppUsageLimit(String str, String str2, UserHandle userHandle) throws RemoteException;

    ApplicationInfo getApplicationInfo(String str, String str2, int i, UserHandle userHandle) throws RemoteException;

    ParceledListSlice getLauncherActivities(String str, String str2, UserHandle userHandle) throws RemoteException;

    ParceledListSlice getShortcutConfigActivities(String str, String str2, UserHandle userHandle) throws RemoteException;

    IntentSender getShortcutConfigActivityIntent(String str, ComponentName componentName, UserHandle userHandle) throws RemoteException;

    ParcelFileDescriptor getShortcutIconFd(String str, String str2, String str3, int i) throws RemoteException;

    int getShortcutIconResId(String str, String str2, String str3, int i) throws RemoteException;

    ParceledListSlice getShortcuts(String str, long j, String str2, List list, ComponentName componentName, int i, UserHandle userHandle) throws RemoteException;

    Bundle getSuspendedPackageLauncherExtras(String str, UserHandle userHandle) throws RemoteException;

    boolean hasShortcutHostPermission(String str) throws RemoteException;

    boolean isActivityEnabled(String str, ComponentName componentName, UserHandle userHandle) throws RemoteException;

    boolean isPackageEnabled(String str, String str2, UserHandle userHandle) throws RemoteException;

    void pinShortcuts(String str, String str2, List<String> list, UserHandle userHandle) throws RemoteException;

    void registerPackageInstallerCallback(String str, IPackageInstallerCallback iPackageInstallerCallback) throws RemoteException;

    void removeOnAppsChangedListener(IOnAppsChangedListener iOnAppsChangedListener) throws RemoteException;

    ActivityInfo resolveActivity(String str, ComponentName componentName, UserHandle userHandle) throws RemoteException;

    boolean shouldHideFromSuggestions(String str, UserHandle userHandle) throws RemoteException;

    void showAppDetailsAsUser(IApplicationThread iApplicationThread, String str, ComponentName componentName, Rect rect, Bundle bundle, UserHandle userHandle) throws RemoteException;

    void startActivityAsUser(IApplicationThread iApplicationThread, String str, ComponentName componentName, Rect rect, Bundle bundle, UserHandle userHandle) throws RemoteException;

    void startSessionDetailsActivityAsUser(IApplicationThread iApplicationThread, String str, SessionInfo sessionInfo, Rect rect, Bundle bundle, UserHandle userHandle) throws RemoteException;

    boolean startShortcut(String str, String str2, String str3, Rect rect, Bundle bundle, int i) throws RemoteException;
}
