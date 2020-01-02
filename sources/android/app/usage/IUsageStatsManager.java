package android.app.usage;

import android.annotation.UnsupportedAppUsage;
import android.app.PendingIntent;
import android.content.pm.ParceledListSlice;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IUsageStatsManager extends IInterface {

    public static class Default implements IUsageStatsManager {
        public ParceledListSlice queryUsageStats(int bucketType, long beginTime, long endTime, String callingPackage) throws RemoteException {
            return null;
        }

        public ParceledListSlice queryConfigurationStats(int bucketType, long beginTime, long endTime, String callingPackage) throws RemoteException {
            return null;
        }

        public ParceledListSlice queryEventStats(int bucketType, long beginTime, long endTime, String callingPackage) throws RemoteException {
            return null;
        }

        public UsageEvents queryEvents(long beginTime, long endTime, String callingPackage) throws RemoteException {
            return null;
        }

        public UsageEvents queryEventsForPackage(long beginTime, long endTime, String callingPackage) throws RemoteException {
            return null;
        }

        public UsageEvents queryEventsForUser(long beginTime, long endTime, int userId, String callingPackage) throws RemoteException {
            return null;
        }

        public UsageEvents queryEventsForPackageForUser(long beginTime, long endTime, int userId, String pkg, String callingPackage) throws RemoteException {
            return null;
        }

        public void setAppInactive(String packageName, boolean inactive, int userId) throws RemoteException {
        }

        public boolean isAppInactive(String packageName, int userId) throws RemoteException {
            return false;
        }

        public void whitelistAppTemporarily(String packageName, long duration, int userId) throws RemoteException {
        }

        public void onCarrierPrivilegedAppsChanged() throws RemoteException {
        }

        public void reportChooserSelection(String packageName, int userId, String contentType, String[] annotations, String action) throws RemoteException {
        }

        public int getAppStandbyBucket(String packageName, String callingPackage, int userId) throws RemoteException {
            return 0;
        }

        public void setAppStandbyBucket(String packageName, int bucket, int userId) throws RemoteException {
        }

        public ParceledListSlice getAppStandbyBuckets(String callingPackage, int userId) throws RemoteException {
            return null;
        }

        public void setAppStandbyBuckets(ParceledListSlice appBuckets, int userId) throws RemoteException {
        }

        public void registerAppUsageObserver(int observerId, String[] packages, long timeLimitMs, PendingIntent callback, String callingPackage) throws RemoteException {
        }

        public void unregisterAppUsageObserver(int observerId, String callingPackage) throws RemoteException {
        }

        public void registerUsageSessionObserver(int sessionObserverId, String[] observed, long timeLimitMs, long sessionThresholdTimeMs, PendingIntent limitReachedCallbackIntent, PendingIntent sessionEndCallbackIntent, String callingPackage) throws RemoteException {
        }

        public void unregisterUsageSessionObserver(int sessionObserverId, String callingPackage) throws RemoteException {
        }

        public void registerAppUsageLimitObserver(int observerId, String[] packages, long timeLimitMs, long timeUsedMs, PendingIntent callback, String callingPackage) throws RemoteException {
        }

        public void unregisterAppUsageLimitObserver(int observerId, String callingPackage) throws RemoteException {
        }

        public void reportUsageStart(IBinder activity, String token, String callingPackage) throws RemoteException {
        }

        public void reportPastUsageStart(IBinder activity, String token, long timeAgoMs, String callingPackage) throws RemoteException {
        }

        public void reportUsageStop(IBinder activity, String token, String callingPackage) throws RemoteException {
        }

        public int getUsageSource() throws RemoteException {
            return 0;
        }

        public void forceUsageSourceSettingRead() throws RemoteException {
        }

        public ParceledListSlice queryUsageStatsAsUser(int bucketType, long beginTime, long endTime, String callingPackage, int userId) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IUsageStatsManager {
        private static final String DESCRIPTOR = "android.app.usage.IUsageStatsManager";
        static final int TRANSACTION_forceUsageSourceSettingRead = 27;
        static final int TRANSACTION_getAppStandbyBucket = 13;
        static final int TRANSACTION_getAppStandbyBuckets = 15;
        static final int TRANSACTION_getUsageSource = 26;
        static final int TRANSACTION_isAppInactive = 9;
        static final int TRANSACTION_onCarrierPrivilegedAppsChanged = 11;
        static final int TRANSACTION_queryConfigurationStats = 2;
        static final int TRANSACTION_queryEventStats = 3;
        static final int TRANSACTION_queryEvents = 4;
        static final int TRANSACTION_queryEventsForPackage = 5;
        static final int TRANSACTION_queryEventsForPackageForUser = 7;
        static final int TRANSACTION_queryEventsForUser = 6;
        static final int TRANSACTION_queryUsageStats = 1;
        static final int TRANSACTION_queryUsageStatsAsUser = 28;
        static final int TRANSACTION_registerAppUsageLimitObserver = 21;
        static final int TRANSACTION_registerAppUsageObserver = 17;
        static final int TRANSACTION_registerUsageSessionObserver = 19;
        static final int TRANSACTION_reportChooserSelection = 12;
        static final int TRANSACTION_reportPastUsageStart = 24;
        static final int TRANSACTION_reportUsageStart = 23;
        static final int TRANSACTION_reportUsageStop = 25;
        static final int TRANSACTION_setAppInactive = 8;
        static final int TRANSACTION_setAppStandbyBucket = 14;
        static final int TRANSACTION_setAppStandbyBuckets = 16;
        static final int TRANSACTION_unregisterAppUsageLimitObserver = 22;
        static final int TRANSACTION_unregisterAppUsageObserver = 18;
        static final int TRANSACTION_unregisterUsageSessionObserver = 20;
        static final int TRANSACTION_whitelistAppTemporarily = 10;

        private static class Proxy implements IUsageStatsManager {
            public static IUsageStatsManager sDefaultImpl;
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

            public ParceledListSlice queryUsageStats(int bucketType, long beginTime, long endTime, String callingPackage) throws RemoteException {
                Throwable th;
                long j;
                long j2;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(bucketType);
                    } catch (Throwable th2) {
                        th = th2;
                        j = beginTime;
                        j2 = endTime;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(beginTime);
                    } catch (Throwable th3) {
                        th = th3;
                        j2 = endTime;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(endTime);
                        try {
                            _data.writeString(callingPackage);
                            ParceledListSlice _result;
                            if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                            _result = Stub.getDefaultImpl().queryUsageStats(bucketType, beginTime, endTime, callingPackage);
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
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i = bucketType;
                    j = beginTime;
                    j2 = endTime;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public ParceledListSlice queryConfigurationStats(int bucketType, long beginTime, long endTime, String callingPackage) throws RemoteException {
                Throwable th;
                long j;
                long j2;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(bucketType);
                    } catch (Throwable th2) {
                        th = th2;
                        j = beginTime;
                        j2 = endTime;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(beginTime);
                    } catch (Throwable th3) {
                        th = th3;
                        j2 = endTime;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(endTime);
                        try {
                            _data.writeString(callingPackage);
                            ParceledListSlice _result;
                            if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                            _result = Stub.getDefaultImpl().queryConfigurationStats(bucketType, beginTime, endTime, callingPackage);
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
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i = bucketType;
                    j = beginTime;
                    j2 = endTime;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public ParceledListSlice queryEventStats(int bucketType, long beginTime, long endTime, String callingPackage) throws RemoteException {
                Throwable th;
                long j;
                long j2;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(bucketType);
                    } catch (Throwable th2) {
                        th = th2;
                        j = beginTime;
                        j2 = endTime;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(beginTime);
                    } catch (Throwable th3) {
                        th = th3;
                        j2 = endTime;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(endTime);
                        try {
                            _data.writeString(callingPackage);
                            ParceledListSlice _result;
                            if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                            _result = Stub.getDefaultImpl().queryEventStats(bucketType, beginTime, endTime, callingPackage);
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
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i = bucketType;
                    j = beginTime;
                    j2 = endTime;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public UsageEvents queryEvents(long beginTime, long endTime, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(beginTime);
                    _data.writeLong(endTime);
                    _data.writeString(callingPackage);
                    UsageEvents usageEvents = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        usageEvents = Stub.getDefaultImpl();
                        if (usageEvents != 0) {
                            usageEvents = Stub.getDefaultImpl().queryEvents(beginTime, endTime, callingPackage);
                            return usageEvents;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        usageEvents = (UsageEvents) UsageEvents.CREATOR.createFromParcel(_reply);
                    } else {
                        usageEvents = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return usageEvents;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UsageEvents queryEventsForPackage(long beginTime, long endTime, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeLong(beginTime);
                    _data.writeLong(endTime);
                    _data.writeString(callingPackage);
                    UsageEvents usageEvents = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        usageEvents = Stub.getDefaultImpl();
                        if (usageEvents != 0) {
                            usageEvents = Stub.getDefaultImpl().queryEventsForPackage(beginTime, endTime, callingPackage);
                            return usageEvents;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        usageEvents = (UsageEvents) UsageEvents.CREATOR.createFromParcel(_reply);
                    } else {
                        usageEvents = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return usageEvents;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public UsageEvents queryEventsForUser(long beginTime, long endTime, int userId, String callingPackage) throws RemoteException {
                Throwable th;
                long j;
                int i;
                String str;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(beginTime);
                    } catch (Throwable th2) {
                        th = th2;
                        j = endTime;
                        i = userId;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(endTime);
                    } catch (Throwable th3) {
                        th = th3;
                        i = userId;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        try {
                            _data.writeString(callingPackage);
                            UsageEvents _result;
                            if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    _result = (UsageEvents) UsageEvents.CREATOR.createFromParcel(_reply);
                                } else {
                                    _result = null;
                                }
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().queryEventsForUser(beginTime, endTime, userId, callingPackage);
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
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    long j2 = beginTime;
                    j = endTime;
                    i = userId;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public UsageEvents queryEventsForPackageForUser(long beginTime, long endTime, int userId, String pkg, String callingPackage) throws RemoteException {
                Throwable th;
                long j;
                int i;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeLong(beginTime);
                    } catch (Throwable th2) {
                        th = th2;
                        j = endTime;
                        i = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(endTime);
                    } catch (Throwable th3) {
                        th = th3;
                        i = userId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        _data.writeString(pkg);
                        _data.writeString(callingPackage);
                        UsageEvents _result;
                        if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            if (_reply.readInt() != 0) {
                                _result = (UsageEvents) UsageEvents.CREATOR.createFromParcel(_reply);
                            } else {
                                _result = null;
                            }
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        }
                        _result = Stub.getDefaultImpl().queryEventsForPackageForUser(beginTime, endTime, userId, pkg, callingPackage);
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
                    long j2 = beginTime;
                    j = endTime;
                    i = userId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void setAppInactive(String packageName, boolean inactive, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(inactive ? 1 : 0);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppInactive(packageName, inactive, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isAppInactive(String packageName, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isAppInactive(packageName, userId);
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

            public void whitelistAppTemporarily(String packageName, long duration, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeLong(duration);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().whitelistAppTemporarily(packageName, duration, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onCarrierPrivilegedAppsChanged() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onCarrierPrivilegedAppsChanged();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportChooserSelection(String packageName, int userId, String contentType, String[] annotations, String action) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(contentType);
                    _data.writeStringArray(annotations);
                    _data.writeString(action);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportChooserSelection(packageName, userId, contentType, annotations, action);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getAppStandbyBucket(String packageName, String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    int i = 13;
                    if (!this.mRemote.transact(13, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getAppStandbyBucket(packageName, callingPackage, userId);
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

            public void setAppStandbyBucket(String packageName, int bucket, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(bucket);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppStandbyBucket(packageName, bucket, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice getAppStandbyBuckets(String callingPackage, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPackage);
                    _data.writeInt(userId);
                    ParceledListSlice parceledListSlice = 15;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        parceledListSlice = Stub.getDefaultImpl();
                        if (parceledListSlice != 0) {
                            parceledListSlice = Stub.getDefaultImpl().getAppStandbyBuckets(callingPackage, userId);
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

            public void setAppStandbyBuckets(ParceledListSlice appBuckets, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (appBuckets != null) {
                        _data.writeInt(1);
                        appBuckets.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    _data.writeInt(userId);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppStandbyBuckets(appBuckets, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerAppUsageObserver(int observerId, String[] packages, long timeLimitMs, PendingIntent callback, String callingPackage) throws RemoteException {
                Throwable th;
                long j;
                String str;
                String[] strArr;
                PendingIntent pendingIntent = callback;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(observerId);
                        try {
                            _data.writeStringArray(packages);
                        } catch (Throwable th2) {
                            th = th2;
                            j = timeLimitMs;
                            str = callingPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeLong(timeLimitMs);
                            if (pendingIntent != null) {
                                _data.writeInt(1);
                                pendingIntent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                _data.writeString(callingPackage);
                                if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    _reply.recycle();
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().registerAppUsageObserver(observerId, packages, timeLimitMs, callback, callingPackage);
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
                            str = callingPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        strArr = packages;
                        j = timeLimitMs;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    int i = observerId;
                    strArr = packages;
                    j = timeLimitMs;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void unregisterAppUsageObserver(int observerId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(observerId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterAppUsageObserver(observerId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerUsageSessionObserver(int sessionObserverId, String[] observed, long timeLimitMs, long sessionThresholdTimeMs, PendingIntent limitReachedCallbackIntent, PendingIntent sessionEndCallbackIntent, String callingPackage) throws RemoteException {
                Throwable th;
                PendingIntent pendingIntent = limitReachedCallbackIntent;
                PendingIntent pendingIntent2 = sessionEndCallbackIntent;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(sessionObserverId);
                        _data.writeStringArray(observed);
                        _data.writeLong(timeLimitMs);
                        _data.writeLong(sessionThresholdTimeMs);
                        if (pendingIntent != null) {
                            _data.writeInt(1);
                            pendingIntent.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        if (pendingIntent2 != null) {
                            _data.writeInt(1);
                            pendingIntent2.writeToParcel(_data, 0);
                        } else {
                            _data.writeInt(0);
                        }
                        _data.writeString(callingPackage);
                        if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                            _reply.readException();
                            _reply.recycle();
                            _data.recycle();
                            return;
                        }
                        Stub.getDefaultImpl().registerUsageSessionObserver(sessionObserverId, observed, timeLimitMs, sessionThresholdTimeMs, limitReachedCallbackIntent, sessionEndCallbackIntent, callingPackage);
                        _reply.recycle();
                        _data.recycle();
                    } catch (Throwable th2) {
                        th = th2;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    int i = sessionObserverId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void unregisterUsageSessionObserver(int sessionObserverId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(sessionObserverId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterUsageSessionObserver(sessionObserverId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void registerAppUsageLimitObserver(int observerId, String[] packages, long timeLimitMs, long timeUsedMs, PendingIntent callback, String callingPackage) throws RemoteException {
                Throwable th;
                String str;
                String[] strArr;
                PendingIntent pendingIntent = callback;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(observerId);
                        try {
                            _data.writeStringArray(packages);
                            _data.writeLong(timeLimitMs);
                            _data.writeLong(timeUsedMs);
                            if (pendingIntent != null) {
                                _data.writeInt(1);
                                pendingIntent.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            str = callingPackage;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(callingPackage);
                            if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().registerAppUsageLimitObserver(observerId, packages, timeLimitMs, timeUsedMs, callback, callingPackage);
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
                        strArr = packages;
                        str = callingPackage;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th5) {
                    th = th5;
                    int i = observerId;
                    strArr = packages;
                    str = callingPackage;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void unregisterAppUsageLimitObserver(int observerId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(observerId);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unregisterAppUsageLimitObserver(observerId, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportUsageStart(IBinder activity, String token, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activity);
                    _data.writeString(token);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportUsageStart(activity, token, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportPastUsageStart(IBinder activity, String token, long timeAgoMs, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activity);
                    _data.writeString(token);
                    _data.writeLong(timeAgoMs);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportPastUsageStart(activity, token, timeAgoMs, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reportUsageStop(IBinder activity, String token, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(activity);
                    _data.writeString(token);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reportUsageStop(activity, token, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getUsageSource() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 26;
                    if (!this.mRemote.transact(26, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getUsageSource();
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

            public void forceUsageSourceSettingRead() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forceUsageSourceSettingRead();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParceledListSlice queryUsageStatsAsUser(int bucketType, long beginTime, long endTime, String callingPackage, int userId) throws RemoteException {
                Throwable th;
                long j;
                long j2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeInt(bucketType);
                    } catch (Throwable th2) {
                        th = th2;
                        j = beginTime;
                        j2 = endTime;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(beginTime);
                    } catch (Throwable th3) {
                        th = th3;
                        j2 = endTime;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(endTime);
                        _data.writeString(callingPackage);
                        _data.writeInt(userId);
                        ParceledListSlice _result;
                        if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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
                        _result = Stub.getDefaultImpl().queryUsageStatsAsUser(bucketType, beginTime, endTime, callingPackage, userId);
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
                    int i = bucketType;
                    j = beginTime;
                    j2 = endTime;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IUsageStatsManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IUsageStatsManager)) {
                return new Proxy(obj);
            }
            return (IUsageStatsManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "queryUsageStats";
                case 2:
                    return "queryConfigurationStats";
                case 3:
                    return "queryEventStats";
                case 4:
                    return "queryEvents";
                case 5:
                    return "queryEventsForPackage";
                case 6:
                    return "queryEventsForUser";
                case 7:
                    return "queryEventsForPackageForUser";
                case 8:
                    return "setAppInactive";
                case 9:
                    return "isAppInactive";
                case 10:
                    return "whitelistAppTemporarily";
                case 11:
                    return "onCarrierPrivilegedAppsChanged";
                case 12:
                    return "reportChooserSelection";
                case 13:
                    return "getAppStandbyBucket";
                case 14:
                    return "setAppStandbyBucket";
                case 15:
                    return "getAppStandbyBuckets";
                case 16:
                    return "setAppStandbyBuckets";
                case 17:
                    return "registerAppUsageObserver";
                case 18:
                    return "unregisterAppUsageObserver";
                case 19:
                    return "registerUsageSessionObserver";
                case 20:
                    return "unregisterUsageSessionObserver";
                case 21:
                    return "registerAppUsageLimitObserver";
                case 22:
                    return "unregisterAppUsageLimitObserver";
                case 23:
                    return "reportUsageStart";
                case 24:
                    return "reportPastUsageStart";
                case 25:
                    return "reportUsageStop";
                case 26:
                    return "getUsageSource";
                case 27:
                    return "forceUsageSourceSettingRead";
                case 28:
                    return "queryUsageStatsAsUser";
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
                boolean z = false;
                ParceledListSlice _result;
                UsageEvents _result2;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        _result = queryUsageStats(data.readInt(), data.readLong(), data.readLong(), data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        _result = queryConfigurationStats(data.readInt(), data.readLong(), data.readLong(), data.readString());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        _result = queryEventStats(data.readInt(), data.readLong(), data.readLong(), data.readString());
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
                        _result2 = queryEvents(data.readLong(), data.readLong(), data.readString());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        _result2 = queryEventsForPackage(data.readLong(), data.readLong(), data.readString());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        _result2 = queryEventsForUser(data.readLong(), data.readLong(), data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        _result2 = queryEventsForPackageForUser(data.readLong(), data.readLong(), data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        if (_result2 != null) {
                            parcel2.writeInt(1);
                            _result2.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        String _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            z = true;
                        }
                        setAppInactive(_arg0, z, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        boolean _result3 = isAppInactive(data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        whitelistAppTemporarily(data.readString(), data.readLong(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        onCarrierPrivilegedAppsChanged();
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        reportChooserSelection(data.readString(), data.readInt(), data.readString(), data.createStringArray(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        int _result4 = getAppStandbyBucket(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        setAppStandbyBucket(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        ParceledListSlice _result5 = getAppStandbyBuckets(data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result5 != null) {
                            parcel2.writeInt(1);
                            _result5.writeToParcel(parcel2, 1);
                        } else {
                            parcel2.writeInt(0);
                        }
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _result = (ParceledListSlice) ParceledListSlice.CREATOR.createFromParcel(parcel);
                        } else {
                            _result = null;
                        }
                        setAppStandbyBuckets(_result, data.readInt());
                        reply.writeNoException();
                        return true;
                    case 17:
                        PendingIntent _arg3;
                        parcel.enforceInterface(descriptor);
                        int _arg02 = data.readInt();
                        String[] _arg1 = data.createStringArray();
                        long _arg2 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg3 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg3 = null;
                        }
                        registerAppUsageObserver(_arg02, _arg1, _arg2, _arg3, data.readString());
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        unregisterAppUsageObserver(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 19:
                        PendingIntent _arg4;
                        PendingIntent _arg5;
                        parcel.enforceInterface(descriptor);
                        int _arg03 = data.readInt();
                        String[] _arg12 = data.createStringArray();
                        long _arg22 = data.readLong();
                        long _arg32 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg4 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg4 = null;
                        }
                        if (data.readInt() != 0) {
                            _arg5 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        registerUsageSessionObserver(_arg03, _arg12, _arg22, _arg32, _arg4, _arg5, data.readString());
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        unregisterUsageSessionObserver(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 21:
                        PendingIntent _arg42;
                        parcel.enforceInterface(descriptor);
                        int _arg04 = data.readInt();
                        String[] _arg13 = data.createStringArray();
                        long _arg23 = data.readLong();
                        long _arg33 = data.readLong();
                        if (data.readInt() != 0) {
                            _arg42 = (PendingIntent) PendingIntent.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg42 = null;
                        }
                        registerAppUsageLimitObserver(_arg04, _arg13, _arg23, _arg33, _arg42, data.readString());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        unregisterAppUsageLimitObserver(data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        reportUsageStart(data.readStrongBinder(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        reportPastUsageStart(data.readStrongBinder(), data.readString(), data.readLong(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        reportUsageStop(data.readStrongBinder(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        int _result6 = getUsageSource();
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        forceUsageSourceSettingRead();
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        _result = queryUsageStatsAsUser(data.readInt(), data.readLong(), data.readLong(), data.readString(), data.readInt());
                        reply.writeNoException();
                        if (_result != null) {
                            parcel2.writeInt(1);
                            _result.writeToParcel(parcel2, 1);
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

        public static boolean setDefaultImpl(IUsageStatsManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IUsageStatsManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void forceUsageSourceSettingRead() throws RemoteException;

    int getAppStandbyBucket(String str, String str2, int i) throws RemoteException;

    ParceledListSlice getAppStandbyBuckets(String str, int i) throws RemoteException;

    int getUsageSource() throws RemoteException;

    @UnsupportedAppUsage
    boolean isAppInactive(String str, int i) throws RemoteException;

    void onCarrierPrivilegedAppsChanged() throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice queryConfigurationStats(int i, long j, long j2, String str) throws RemoteException;

    ParceledListSlice queryEventStats(int i, long j, long j2, String str) throws RemoteException;

    UsageEvents queryEvents(long j, long j2, String str) throws RemoteException;

    UsageEvents queryEventsForPackage(long j, long j2, String str) throws RemoteException;

    UsageEvents queryEventsForPackageForUser(long j, long j2, int i, String str, String str2) throws RemoteException;

    UsageEvents queryEventsForUser(long j, long j2, int i, String str) throws RemoteException;

    @UnsupportedAppUsage
    ParceledListSlice queryUsageStats(int i, long j, long j2, String str) throws RemoteException;

    ParceledListSlice queryUsageStatsAsUser(int i, long j, long j2, String str, int i2) throws RemoteException;

    void registerAppUsageLimitObserver(int i, String[] strArr, long j, long j2, PendingIntent pendingIntent, String str) throws RemoteException;

    void registerAppUsageObserver(int i, String[] strArr, long j, PendingIntent pendingIntent, String str) throws RemoteException;

    void registerUsageSessionObserver(int i, String[] strArr, long j, long j2, PendingIntent pendingIntent, PendingIntent pendingIntent2, String str) throws RemoteException;

    void reportChooserSelection(String str, int i, String str2, String[] strArr, String str3) throws RemoteException;

    void reportPastUsageStart(IBinder iBinder, String str, long j, String str2) throws RemoteException;

    void reportUsageStart(IBinder iBinder, String str, String str2) throws RemoteException;

    void reportUsageStop(IBinder iBinder, String str, String str2) throws RemoteException;

    @UnsupportedAppUsage
    void setAppInactive(String str, boolean z, int i) throws RemoteException;

    void setAppStandbyBucket(String str, int i, int i2) throws RemoteException;

    void setAppStandbyBuckets(ParceledListSlice parceledListSlice, int i) throws RemoteException;

    void unregisterAppUsageLimitObserver(int i, String str) throws RemoteException;

    void unregisterAppUsageObserver(int i, String str) throws RemoteException;

    void unregisterUsageSessionObserver(int i, String str) throws RemoteException;

    void whitelistAppTemporarily(String str, long j, int i) throws RemoteException;
}
