package android.content;

import android.accounts.Account;
import android.annotation.UnsupportedAppUsage;
import android.database.IContentObserver;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import java.util.List;

public interface IContentService extends IInterface {

    public static class Default implements IContentService {
        public void unregisterContentObserver(IContentObserver observer) throws RemoteException {
        }

        public void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer, int userHandle, int targetSdkVersion) throws RemoteException {
        }

        public void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, int flags, int userHandle, int targetSdkVersion, String callingPackage) throws RemoteException {
        }

        public void requestSync(Account account, String authority, Bundle extras, String callingPackage) throws RemoteException {
        }

        public void sync(SyncRequest request, String callingPackage) throws RemoteException {
        }

        public void syncAsUser(SyncRequest request, int userId, String callingPackage) throws RemoteException {
        }

        public void cancelSync(Account account, String authority, ComponentName cname) throws RemoteException {
        }

        public void cancelSyncAsUser(Account account, String authority, ComponentName cname, int userId) throws RemoteException {
        }

        public void cancelRequest(SyncRequest request) throws RemoteException {
        }

        public boolean getSyncAutomatically(Account account, String providerName) throws RemoteException {
            return false;
        }

        public boolean getSyncAutomaticallyAsUser(Account account, String providerName, int userId) throws RemoteException {
            return false;
        }

        public void setSyncAutomatically(Account account, String providerName, boolean sync) throws RemoteException {
        }

        public void setSyncAutomaticallyAsUser(Account account, String providerName, boolean sync, int userId) throws RemoteException {
        }

        public List<PeriodicSync> getPeriodicSyncs(Account account, String providerName, ComponentName cname) throws RemoteException {
            return null;
        }

        public void addPeriodicSync(Account account, String providerName, Bundle extras, long pollFrequency) throws RemoteException {
        }

        public void removePeriodicSync(Account account, String providerName, Bundle extras) throws RemoteException {
        }

        public int getIsSyncable(Account account, String providerName) throws RemoteException {
            return 0;
        }

        public int getIsSyncableAsUser(Account account, String providerName, int userId) throws RemoteException {
            return 0;
        }

        public void setIsSyncable(Account account, String providerName, int syncable) throws RemoteException {
        }

        public void setIsSyncableAsUser(Account account, String providerName, int syncable, int userId) throws RemoteException {
        }

        public void setMasterSyncAutomatically(boolean flag) throws RemoteException {
        }

        public void setMasterSyncAutomaticallyAsUser(boolean flag, int userId) throws RemoteException {
        }

        public boolean getMasterSyncAutomatically() throws RemoteException {
            return false;
        }

        public boolean getMasterSyncAutomaticallyAsUser(int userId) throws RemoteException {
            return false;
        }

        public List<SyncInfo> getCurrentSyncs() throws RemoteException {
            return null;
        }

        public List<SyncInfo> getCurrentSyncsAsUser(int userId) throws RemoteException {
            return null;
        }

        public SyncAdapterType[] getSyncAdapterTypes() throws RemoteException {
            return null;
        }

        public SyncAdapterType[] getSyncAdapterTypesAsUser(int userId) throws RemoteException {
            return null;
        }

        public String[] getSyncAdapterPackagesForAuthorityAsUser(String authority, int userId) throws RemoteException {
            return null;
        }

        public boolean isSyncActive(Account account, String authority, ComponentName cname) throws RemoteException {
            return false;
        }

        public SyncStatusInfo getSyncStatus(Account account, String authority, ComponentName cname) throws RemoteException {
            return null;
        }

        public SyncStatusInfo getSyncStatusAsUser(Account account, String authority, ComponentName cname, int userId) throws RemoteException {
            return null;
        }

        public boolean isSyncPending(Account account, String authority, ComponentName cname) throws RemoteException {
            return false;
        }

        public boolean isSyncPendingAsUser(Account account, String authority, ComponentName cname, int userId) throws RemoteException {
            return false;
        }

        public void addStatusChangeListener(int mask, ISyncStatusObserver callback) throws RemoteException {
        }

        public void removeStatusChangeListener(ISyncStatusObserver callback) throws RemoteException {
        }

        public void putCache(String packageName, Uri key, Bundle value, int userId) throws RemoteException {
        }

        public Bundle getCache(String packageName, Uri key, int userId) throws RemoteException {
            return null;
        }

        public void resetTodayStats() throws RemoteException {
        }

        public void onDbCorruption(String tag, String message, String stacktrace) throws RemoteException {
        }

        public void setMiSyncPauseToTime(Account account, long pauseTimeMillis, int uid) throws RemoteException {
        }

        public long getMiSyncPauseToTime(Account account, int uid) throws RemoteException {
            return 0;
        }

        public void setMiSyncStrategy(Account account, int strategy, int uid) throws RemoteException {
        }

        public int getMiSyncStrategy(Account account, int uid) throws RemoteException {
            return 0;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IContentService {
        private static final String DESCRIPTOR = "android.content.IContentService";
        static final int TRANSACTION_addPeriodicSync = 15;
        static final int TRANSACTION_addStatusChangeListener = 35;
        static final int TRANSACTION_cancelRequest = 9;
        static final int TRANSACTION_cancelSync = 7;
        static final int TRANSACTION_cancelSyncAsUser = 8;
        static final int TRANSACTION_getCache = 38;
        static final int TRANSACTION_getCurrentSyncs = 25;
        static final int TRANSACTION_getCurrentSyncsAsUser = 26;
        static final int TRANSACTION_getIsSyncable = 17;
        static final int TRANSACTION_getIsSyncableAsUser = 18;
        static final int TRANSACTION_getMasterSyncAutomatically = 23;
        static final int TRANSACTION_getMasterSyncAutomaticallyAsUser = 24;
        static final int TRANSACTION_getMiSyncPauseToTime = 42;
        static final int TRANSACTION_getMiSyncStrategy = 44;
        static final int TRANSACTION_getPeriodicSyncs = 14;
        static final int TRANSACTION_getSyncAdapterPackagesForAuthorityAsUser = 29;
        static final int TRANSACTION_getSyncAdapterTypes = 27;
        static final int TRANSACTION_getSyncAdapterTypesAsUser = 28;
        static final int TRANSACTION_getSyncAutomatically = 10;
        static final int TRANSACTION_getSyncAutomaticallyAsUser = 11;
        static final int TRANSACTION_getSyncStatus = 31;
        static final int TRANSACTION_getSyncStatusAsUser = 32;
        static final int TRANSACTION_isSyncActive = 30;
        static final int TRANSACTION_isSyncPending = 33;
        static final int TRANSACTION_isSyncPendingAsUser = 34;
        static final int TRANSACTION_notifyChange = 3;
        static final int TRANSACTION_onDbCorruption = 40;
        static final int TRANSACTION_putCache = 37;
        static final int TRANSACTION_registerContentObserver = 2;
        static final int TRANSACTION_removePeriodicSync = 16;
        static final int TRANSACTION_removeStatusChangeListener = 36;
        static final int TRANSACTION_requestSync = 4;
        static final int TRANSACTION_resetTodayStats = 39;
        static final int TRANSACTION_setIsSyncable = 19;
        static final int TRANSACTION_setIsSyncableAsUser = 20;
        static final int TRANSACTION_setMasterSyncAutomatically = 21;
        static final int TRANSACTION_setMasterSyncAutomaticallyAsUser = 22;
        static final int TRANSACTION_setMiSyncPauseToTime = 41;
        static final int TRANSACTION_setMiSyncStrategy = 43;
        static final int TRANSACTION_setSyncAutomatically = 12;
        static final int TRANSACTION_setSyncAutomaticallyAsUser = 13;
        static final int TRANSACTION_sync = 5;
        static final int TRANSACTION_syncAsUser = 6;
        static final int TRANSACTION_unregisterContentObserver = 1;

        private static class Proxy implements IContentService {
            public static IContentService sDefaultImpl;
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

            public void unregisterContentObserver(IContentObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterContentObserver(observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerContentObserver(Uri uri, boolean notifyForDescendants, IContentObserver observer, int userHandle, int targetSdkVersion) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (uri != null) {
                        _data.writeInt(1);
                        uri.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!notifyForDescendants) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    _data.writeInt(userHandle);
                    _data.writeInt(targetSdkVersion);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerContentObserver(uri, notifyForDescendants, observer, userHandle, targetSdkVersion);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void notifyChange(Uri uri, IContentObserver observer, boolean observerWantsSelfNotifications, int flags, int userHandle, int targetSdkVersion, String callingPackage) throws RemoteException {
                Throwable th;
                int i;
                String str;
                int i2;
                Uri uri2 = uri;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i3 = 1;
                    if (uri2 != null) {
                        _data.writeInt(1);
                        uri2.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (!observerWantsSelfNotifications) {
                        i3 = 0;
                    }
                    _data.writeInt(i3);
                    try {
                        _data.writeInt(flags);
                        try {
                            _data.writeInt(userHandle);
                        } catch (Throwable th2) {
                            th = th2;
                            i = targetSdkVersion;
                            str = callingPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(targetSdkVersion);
                        } catch (Throwable th3) {
                            th = th3;
                            str = callingPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th4) {
                        th = th4;
                        i2 = userHandle;
                        i = targetSdkVersion;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(callingPackage);
                        if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().notifyChange(uri, observer, observerWantsSelfNotifications, flags, userHandle, targetSdkVersion, callingPackage);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th5) {
                        th = th5;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i4 = flags;
                    i2 = userHandle;
                    i = targetSdkVersion;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void requestSync(Account account, String authority, Bundle extras, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().requestSync(account, authority, extras, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void sync(SyncRequest request, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().sync(request, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void syncAsUser(SyncRequest request, int userId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().syncAsUser(request, userId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelSync(Account account, String authority, ComponentName cname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelSync(account, authority, cname);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelSyncAsUser(Account account, String authority, ComponentName cname, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelSyncAsUser(account, authority, cname, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void cancelRequest(SyncRequest request) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (request != null) {
                        _data.writeInt(1);
                        request.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().cancelRequest(request);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getSyncAutomatically(Account account, String providerName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().getSyncAutomatically(account, providerName);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getSyncAutomaticallyAsUser(Account account, String providerName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().getSyncAutomaticallyAsUser(account, providerName, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSyncAutomatically(Account account, String providerName, boolean sync) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    if (!sync) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSyncAutomatically(account, providerName, sync);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setSyncAutomaticallyAsUser(Account account, String providerName, boolean sync, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 1;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    if (!sync) {
                        i = 0;
                    }
                    _data.writeInt(i);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setSyncAutomaticallyAsUser(account, providerName, sync, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<PeriodicSync> getPeriodicSyncs(Account account, String providerName, ComponentName cname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<PeriodicSync> list = 0;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(14, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getPeriodicSyncs(account, providerName, cname);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(PeriodicSync.CREATOR);
                    List<PeriodicSync> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addPeriodicSync(Account account, String providerName, Bundle extras, long pollFrequency) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(pollFrequency);
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addPeriodicSync(account, providerName, extras, pollFrequency);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removePeriodicSync(Account account, String providerName, Bundle extras) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    if (extras != null) {
                        _data.writeInt(1);
                        extras.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removePeriodicSync(account, providerName, extras);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getIsSyncable(Account account, String providerName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    int i = this.mRemote;
                    if (!i.transact(17, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getIsSyncable(account, providerName);
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

            public int getIsSyncableAsUser(Account account, String providerName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    _data.writeInt(userId);
                    int i = this.mRemote;
                    if (!i.transact(18, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getIsSyncableAsUser(account, providerName, userId);
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

            public void setIsSyncable(Account account, String providerName, int syncable) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    _data.writeInt(syncable);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIsSyncable(account, providerName, syncable);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setIsSyncableAsUser(Account account, String providerName, int syncable, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(providerName);
                    _data.writeInt(syncable);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setIsSyncableAsUser(account, providerName, syncable, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMasterSyncAutomatically(boolean flag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flag ? 1 : 0);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMasterSyncAutomatically(flag);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMasterSyncAutomaticallyAsUser(boolean flag, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flag ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMasterSyncAutomaticallyAsUser(flag, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean getMasterSyncAutomatically() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(23, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getMasterSyncAutomatically();
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

            public boolean getMasterSyncAutomaticallyAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().getMasterSyncAutomaticallyAsUser(userId);
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

            public List<SyncInfo> getCurrentSyncs() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    List<SyncInfo> list = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getCurrentSyncs();
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(SyncInfo.CREATOR);
                    List<SyncInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public List<SyncInfo> getCurrentSyncsAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    List<SyncInfo> list = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        list = Stub.getDefaultImpl();
                        if (list != 0) {
                            list = Stub.getDefaultImpl().getCurrentSyncsAsUser(userId);
                            return list;
                        }
                    }
                    _reply.readException();
                    list = _reply.createTypedArrayList(SyncInfo.CREATOR);
                    List<SyncInfo> _result = list;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SyncAdapterType[] getSyncAdapterTypes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    SyncAdapterType[] syncAdapterTypeArr = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        syncAdapterTypeArr = Stub.getDefaultImpl();
                        if (syncAdapterTypeArr != 0) {
                            syncAdapterTypeArr = Stub.getDefaultImpl().getSyncAdapterTypes();
                            return syncAdapterTypeArr;
                        }
                    }
                    _reply.readException();
                    SyncAdapterType[] _result = (SyncAdapterType[]) _reply.createTypedArray(SyncAdapterType.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SyncAdapterType[] getSyncAdapterTypesAsUser(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    SyncAdapterType[] syncAdapterTypeArr = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        syncAdapterTypeArr = Stub.getDefaultImpl();
                        if (syncAdapterTypeArr != 0) {
                            syncAdapterTypeArr = Stub.getDefaultImpl().getSyncAdapterTypesAsUser(userId);
                            return syncAdapterTypeArr;
                        }
                    }
                    _reply.readException();
                    SyncAdapterType[] _result = (SyncAdapterType[]) _reply.createTypedArray(SyncAdapterType.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String[] getSyncAdapterPackagesForAuthorityAsUser(String authority, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(authority);
                    _data.writeInt(userId);
                    String[] strArr = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        strArr = Stub.getDefaultImpl();
                        if (strArr != 0) {
                            strArr = Stub.getDefaultImpl().getSyncAdapterPackagesForAuthorityAsUser(authority, userId);
                            return strArr;
                        }
                    }
                    _reply.readException();
                    strArr = _reply.createStringArray();
                    String[] _result = strArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSyncActive(Account account, String authority, ComponentName cname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isSyncActive(account, authority, cname);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SyncStatusInfo getSyncStatus(Account account, String authority, ComponentName cname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    SyncStatusInfo syncStatusInfo = 0;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        syncStatusInfo = Stub.getDefaultImpl();
                        if (syncStatusInfo != 0) {
                            syncStatusInfo = Stub.getDefaultImpl().getSyncStatus(account, authority, cname);
                            return syncStatusInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        syncStatusInfo = (SyncStatusInfo) SyncStatusInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        syncStatusInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return syncStatusInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public SyncStatusInfo getSyncStatusAsUser(Account account, String authority, ComponentName cname, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    SyncStatusInfo syncStatusInfo = 0;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        syncStatusInfo = Stub.getDefaultImpl();
                        if (syncStatusInfo != 0) {
                            syncStatusInfo = Stub.getDefaultImpl().getSyncStatusAsUser(account, authority, cname, userId);
                            return syncStatusInfo;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        syncStatusInfo = (SyncStatusInfo) SyncStatusInfo.CREATOR.createFromParcel(_reply);
                    } else {
                        syncStatusInfo = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return syncStatusInfo;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSyncPending(Account account, String authority, ComponentName cname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isSyncPending(account, authority, cname);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isSyncPendingAsUser(Account account, String authority, ComponentName cname, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean _result = true;
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeString(authority);
                    if (cname != null) {
                        _data.writeInt(1);
                        cname.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        if (_reply.readInt() == 0) {
                            _result = false;
                        }
                        _reply.recycle();
                        _data.recycle();
                        return _result;
                    }
                    _result = Stub.getDefaultImpl().isSyncPendingAsUser(account, authority, cname, userId);
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addStatusChangeListener(int mask, ISyncStatusObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mask);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addStatusChangeListener(mask, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeStatusChangeListener(ISyncStatusObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeStatusChangeListener(callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void putCache(String packageName, Uri key, Bundle value, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (key != null) {
                        _data.writeInt(1);
                        key.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    if (value != null) {
                        _data.writeInt(1);
                        value.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(37, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().putCache(packageName, key, value, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public Bundle getCache(String packageName, Uri key, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (key != null) {
                        _data.writeInt(1);
                        key.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    Bundle bundle = this.mRemote;
                    if (!bundle.transact(38, _data, _reply, 0)) {
                        bundle = Stub.getDefaultImpl();
                        if (bundle != null) {
                            bundle = Stub.getDefaultImpl().getCache(packageName, key, userId);
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

            public void resetTodayStats() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(39, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().resetTodayStats();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onDbCorruption(String tag, String message, String stacktrace) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(tag);
                    _data.writeString(message);
                    _data.writeString(stacktrace);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onDbCorruption(tag, message, stacktrace);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setMiSyncPauseToTime(Account account, long pauseTimeMillis, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeLong(pauseTimeMillis);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMiSyncPauseToTime(account, pauseTimeMillis, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long getMiSyncPauseToTime(Account account, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    long j = this.mRemote;
                    if (!j.transact(42, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != null) {
                            j = Stub.getDefaultImpl().getMiSyncPauseToTime(account, uid);
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

            public void setMiSyncStrategy(Account account, int strategy, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(strategy);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setMiSyncStrategy(account, strategy, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getMiSyncStrategy(Account account, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (account != null) {
                        _data.writeInt(1);
                        account.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(uid);
                    int i = this.mRemote;
                    if (!i.transact(44, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != null) {
                            i = Stub.getDefaultImpl().getMiSyncStrategy(account, uid);
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
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IContentService asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IContentService)) {
                return new Proxy(obj);
            }
            return (IContentService) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "unregisterContentObserver";
                case 2:
                    return "registerContentObserver";
                case 3:
                    return "notifyChange";
                case 4:
                    return "requestSync";
                case 5:
                    return "sync";
                case 6:
                    return "syncAsUser";
                case 7:
                    return "cancelSync";
                case 8:
                    return "cancelSyncAsUser";
                case 9:
                    return "cancelRequest";
                case 10:
                    return "getSyncAutomatically";
                case 11:
                    return "getSyncAutomaticallyAsUser";
                case 12:
                    return "setSyncAutomatically";
                case 13:
                    return "setSyncAutomaticallyAsUser";
                case 14:
                    return "getPeriodicSyncs";
                case 15:
                    return "addPeriodicSync";
                case 16:
                    return "removePeriodicSync";
                case 17:
                    return "getIsSyncable";
                case 18:
                    return "getIsSyncableAsUser";
                case 19:
                    return "setIsSyncable";
                case 20:
                    return "setIsSyncableAsUser";
                case 21:
                    return "setMasterSyncAutomatically";
                case 22:
                    return "setMasterSyncAutomaticallyAsUser";
                case 23:
                    return "getMasterSyncAutomatically";
                case 24:
                    return "getMasterSyncAutomaticallyAsUser";
                case 25:
                    return "getCurrentSyncs";
                case 26:
                    return "getCurrentSyncsAsUser";
                case 27:
                    return "getSyncAdapterTypes";
                case 28:
                    return "getSyncAdapterTypesAsUser";
                case 29:
                    return "getSyncAdapterPackagesForAuthorityAsUser";
                case 30:
                    return "isSyncActive";
                case 31:
                    return "getSyncStatus";
                case 32:
                    return "getSyncStatusAsUser";
                case 33:
                    return "isSyncPending";
                case 34:
                    return "isSyncPendingAsUser";
                case 35:
                    return "addStatusChangeListener";
                case 36:
                    return "removeStatusChangeListener";
                case 37:
                    return "putCache";
                case 38:
                    return "getCache";
                case 39:
                    return "resetTodayStats";
                case 40:
                    return "onDbCorruption";
                case 41:
                    return "setMiSyncPauseToTime";
                case 42:
                    return "getMiSyncPauseToTime";
                case 43:
                    return "setMiSyncStrategy";
                case 44:
                    return "getMiSyncStrategy";
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
                boolean _arg2 = false;
                Account _arg0;
                String _arg1;
                Bundle _arg22;
                SyncRequest _arg02;
                ComponentName _arg23;
                boolean _result;
                Account _arg03;
                String _arg12;
                int _result2;
                ComponentName _arg24;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        unregisterContentObserver(android.database.IContentObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 2:
                        Uri _arg04;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg04 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg04 = null;
                        }
                        registerContentObserver(_arg04, data.readInt() != 0, android.database.IContentObserver.Stub.asInterface(data.readStrongBinder()), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        Uri _arg05;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg05 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg05 = null;
                        }
                        notifyChange(_arg05, android.database.IContentObserver.Stub.asInterface(data.readStrongBinder()), data.readInt() != 0, data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        requestSync(_arg0, _arg1, _arg22, data.readString());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (SyncRequest) SyncRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        sync(_arg02, data.readString());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (SyncRequest) SyncRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        syncAsUser(_arg02, data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        cancelSync(_arg0, _arg1, _arg23);
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        cancelSyncAsUser(_arg0, _arg1, _arg23, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg02 = (SyncRequest) SyncRequest.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg02 = null;
                        }
                        cancelRequest(_arg02);
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        boolean _result3 = getSyncAutomatically(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result = getSyncAutomaticallyAsUser(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setSyncAutomatically(_arg03, _arg12, _arg2);
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setSyncAutomaticallyAsUser(_arg03, _arg12, _arg2, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        List<PeriodicSync> _result4 = getPeriodicSyncs(_arg0, _arg1, _arg23);
                        reply.writeNoException();
                        parcel2.writeTypedList(_result4);
                        return true;
                    case 15:
                        Account _arg06;
                        Bundle _arg25;
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg06 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg06 = null;
                        }
                        String _arg13 = data.readString();
                        if (data.readInt() != 0) {
                            _arg25 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg25 = null;
                        }
                        addPeriodicSync(_arg06, _arg13, _arg25, data.readLong());
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg22 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        removePeriodicSync(_arg0, _arg1, _arg22);
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = getIsSyncable(_arg0, data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        int _result5 = getIsSyncableAsUser(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result5);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setIsSyncable(_arg0, data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setIsSyncableAsUser(_arg0, data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setMasterSyncAutomatically(_arg2);
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        setMasterSyncAutomaticallyAsUser(_arg2, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _arg2 = getMasterSyncAutomatically();
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        boolean _result6 = getMasterSyncAutomaticallyAsUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        List<SyncInfo> _result7 = getCurrentSyncs();
                        reply.writeNoException();
                        parcel2.writeTypedList(_result7);
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        List<SyncInfo> _result8 = getCurrentSyncsAsUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedList(_result8);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        SyncAdapterType[] _result9 = getSyncAdapterTypes();
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result9, 1);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        SyncAdapterType[] _result10 = getSyncAdapterTypesAsUser(data.readInt());
                        reply.writeNoException();
                        parcel2.writeTypedArray(_result10, 1);
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        String[] _result11 = getSyncAdapterPackagesForAuthorityAsUser(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeStringArray(_result11);
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        _result = isSyncActive(_arg0, _arg1, _arg23);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg24 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        SyncStatusInfo _result12 = getSyncStatus(_arg03, _arg12, _arg24);
                        reply.writeNoException();
                        if (_result12 != null) {
                            parcel2.writeInt(1);
                            _result12.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg03 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg03 = null;
                        }
                        _arg12 = data.readString();
                        if (data.readInt() != 0) {
                            _arg24 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg24 = null;
                        }
                        SyncStatusInfo _result13 = getSyncStatusAsUser(_arg03, _arg12, _arg24, data.readInt());
                        reply.writeNoException();
                        if (_result13 != null) {
                            parcel2.writeInt(1);
                            _result13.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        _result = isSyncPending(_arg0, _arg1, _arg23);
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg23 = (ComponentName) ComponentName.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg23 = null;
                        }
                        boolean _result14 = isSyncPendingAsUser(_arg0, _arg1, _arg23, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result14);
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        addStatusChangeListener(data.readInt(), android.content.ISyncStatusObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        removeStatusChangeListener(android.content.ISyncStatusObserver.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 37:
                        Uri _arg14;
                        parcel.enforceInterface(descriptor);
                        String _arg07 = data.readString();
                        if (data.readInt() != 0) {
                            _arg14 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg14 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg22 = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg22 = null;
                        }
                        putCache(_arg07, _arg14, _arg22, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 38:
                        Uri _arg15;
                        parcel.enforceInterface(descriptor);
                        _arg1 = data.readString();
                        if (data.readInt() != 0) {
                            _arg15 = (Uri) Uri.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg15 = null;
                        }
                        Bundle _result15 = getCache(_arg1, _arg15, data.readInt());
                        reply.writeNoException();
                        if (_result15 != null) {
                            parcel2.writeInt(1);
                            _result15.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        resetTodayStats();
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        onDbCorruption(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setMiSyncPauseToTime(_arg0, data.readLong(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        long _result16 = getMiSyncPauseToTime(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeLong(_result16);
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        setMiSyncStrategy(_arg0, data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg0 = (Account) Account.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg0 = null;
                        }
                        _result2 = getMiSyncStrategy(_arg0, data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result2);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IContentService impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IContentService getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void addPeriodicSync(Account account, String str, Bundle bundle, long j) throws RemoteException;

    void addStatusChangeListener(int i, ISyncStatusObserver iSyncStatusObserver) throws RemoteException;

    void cancelRequest(SyncRequest syncRequest) throws RemoteException;

    @UnsupportedAppUsage
    void cancelSync(Account account, String str, ComponentName componentName) throws RemoteException;

    void cancelSyncAsUser(Account account, String str, ComponentName componentName, int i) throws RemoteException;

    Bundle getCache(String str, Uri uri, int i) throws RemoteException;

    List<SyncInfo> getCurrentSyncs() throws RemoteException;

    List<SyncInfo> getCurrentSyncsAsUser(int i) throws RemoteException;

    @UnsupportedAppUsage
    int getIsSyncable(Account account, String str) throws RemoteException;

    int getIsSyncableAsUser(Account account, String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean getMasterSyncAutomatically() throws RemoteException;

    boolean getMasterSyncAutomaticallyAsUser(int i) throws RemoteException;

    long getMiSyncPauseToTime(Account account, int i) throws RemoteException;

    int getMiSyncStrategy(Account account, int i) throws RemoteException;

    List<PeriodicSync> getPeriodicSyncs(Account account, String str, ComponentName componentName) throws RemoteException;

    String[] getSyncAdapterPackagesForAuthorityAsUser(String str, int i) throws RemoteException;

    @UnsupportedAppUsage
    SyncAdapterType[] getSyncAdapterTypes() throws RemoteException;

    SyncAdapterType[] getSyncAdapterTypesAsUser(int i) throws RemoteException;

    boolean getSyncAutomatically(Account account, String str) throws RemoteException;

    boolean getSyncAutomaticallyAsUser(Account account, String str, int i) throws RemoteException;

    SyncStatusInfo getSyncStatus(Account account, String str, ComponentName componentName) throws RemoteException;

    SyncStatusInfo getSyncStatusAsUser(Account account, String str, ComponentName componentName, int i) throws RemoteException;

    @UnsupportedAppUsage
    boolean isSyncActive(Account account, String str, ComponentName componentName) throws RemoteException;

    boolean isSyncPending(Account account, String str, ComponentName componentName) throws RemoteException;

    boolean isSyncPendingAsUser(Account account, String str, ComponentName componentName, int i) throws RemoteException;

    void notifyChange(Uri uri, IContentObserver iContentObserver, boolean z, int i, int i2, int i3, String str) throws RemoteException;

    void onDbCorruption(String str, String str2, String str3) throws RemoteException;

    void putCache(String str, Uri uri, Bundle bundle, int i) throws RemoteException;

    void registerContentObserver(Uri uri, boolean z, IContentObserver iContentObserver, int i, int i2) throws RemoteException;

    void removePeriodicSync(Account account, String str, Bundle bundle) throws RemoteException;

    void removeStatusChangeListener(ISyncStatusObserver iSyncStatusObserver) throws RemoteException;

    void requestSync(Account account, String str, Bundle bundle, String str2) throws RemoteException;

    void resetTodayStats() throws RemoteException;

    void setIsSyncable(Account account, String str, int i) throws RemoteException;

    void setIsSyncableAsUser(Account account, String str, int i, int i2) throws RemoteException;

    @UnsupportedAppUsage
    void setMasterSyncAutomatically(boolean z) throws RemoteException;

    void setMasterSyncAutomaticallyAsUser(boolean z, int i) throws RemoteException;

    void setMiSyncPauseToTime(Account account, long j, int i) throws RemoteException;

    void setMiSyncStrategy(Account account, int i, int i2) throws RemoteException;

    void setSyncAutomatically(Account account, String str, boolean z) throws RemoteException;

    void setSyncAutomaticallyAsUser(Account account, String str, boolean z, int i) throws RemoteException;

    void sync(SyncRequest syncRequest, String str) throws RemoteException;

    void syncAsUser(SyncRequest syncRequest, int i, String str) throws RemoteException;

    void unregisterContentObserver(IContentObserver iContentObserver) throws RemoteException;
}
