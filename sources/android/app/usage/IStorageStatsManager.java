package android.app.usage;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface IStorageStatsManager extends IInterface {

    public static class Default implements IStorageStatsManager {
        public boolean isQuotaSupported(String volumeUuid, String callingPackage) throws RemoteException {
            return false;
        }

        public boolean isReservedSupported(String volumeUuid, String callingPackage) throws RemoteException {
            return false;
        }

        public long getTotalBytes(String volumeUuid, String callingPackage) throws RemoteException {
            return 0;
        }

        public long getFreeBytes(String volumeUuid, String callingPackage) throws RemoteException {
            return 0;
        }

        public long getCacheBytes(String volumeUuid, String callingPackage) throws RemoteException {
            return 0;
        }

        public long getCacheQuotaBytes(String volumeUuid, int uid, String callingPackage) throws RemoteException {
            return 0;
        }

        public StorageStats queryStatsForPackage(String volumeUuid, String packageName, int userId, String callingPackage) throws RemoteException {
            return null;
        }

        public StorageStats queryStatsForUid(String volumeUuid, int uid, String callingPackage) throws RemoteException {
            return null;
        }

        public StorageStats queryStatsForUser(String volumeUuid, int userId, String callingPackage) throws RemoteException {
            return null;
        }

        public ExternalStorageStats queryExternalStatsForUser(String volumeUuid, int userId, String callingPackage) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IStorageStatsManager {
        private static final String DESCRIPTOR = "android.app.usage.IStorageStatsManager";
        static final int TRANSACTION_getCacheBytes = 5;
        static final int TRANSACTION_getCacheQuotaBytes = 6;
        static final int TRANSACTION_getFreeBytes = 4;
        static final int TRANSACTION_getTotalBytes = 3;
        static final int TRANSACTION_isQuotaSupported = 1;
        static final int TRANSACTION_isReservedSupported = 2;
        static final int TRANSACTION_queryExternalStatsForUser = 10;
        static final int TRANSACTION_queryStatsForPackage = 7;
        static final int TRANSACTION_queryStatsForUid = 8;
        static final int TRANSACTION_queryStatsForUser = 9;

        private static class Proxy implements IStorageStatsManager {
            public static IStorageStatsManager sDefaultImpl;
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

            public boolean isQuotaSupported(String volumeUuid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeString(callingPackage);
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
                    z = Stub.getDefaultImpl().isQuotaSupported(volumeUuid, callingPackage);
                    return z;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isReservedSupported(String volumeUuid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeString(callingPackage);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(2, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isReservedSupported(volumeUuid, callingPackage);
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

            public long getTotalBytes(String volumeUuid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeString(callingPackage);
                    long j = 3;
                    if (!this.mRemote.transact(3, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getTotalBytes(volumeUuid, callingPackage);
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

            public long getFreeBytes(String volumeUuid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeString(callingPackage);
                    long j = 4;
                    if (!this.mRemote.transact(4, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getFreeBytes(volumeUuid, callingPackage);
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

            public long getCacheBytes(String volumeUuid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeString(callingPackage);
                    long j = 5;
                    if (!this.mRemote.transact(5, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getCacheBytes(volumeUuid, callingPackage);
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

            public long getCacheQuotaBytes(String volumeUuid, int uid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(uid);
                    _data.writeString(callingPackage);
                    long j = 6;
                    if (!this.mRemote.transact(6, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getCacheQuotaBytes(volumeUuid, uid, callingPackage);
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

            public StorageStats queryStatsForPackage(String volumeUuid, String packageName, int userId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeString(callingPackage);
                    StorageStats storageStats = 7;
                    if (!this.mRemote.transact(7, _data, _reply, 0)) {
                        storageStats = Stub.getDefaultImpl();
                        if (storageStats != 0) {
                            storageStats = Stub.getDefaultImpl().queryStatsForPackage(volumeUuid, packageName, userId, callingPackage);
                            return storageStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        storageStats = (StorageStats) StorageStats.CREATOR.createFromParcel(_reply);
                    } else {
                        storageStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return storageStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StorageStats queryStatsForUid(String volumeUuid, int uid, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(uid);
                    _data.writeString(callingPackage);
                    StorageStats storageStats = 8;
                    if (!this.mRemote.transact(8, _data, _reply, 0)) {
                        storageStats = Stub.getDefaultImpl();
                        if (storageStats != 0) {
                            storageStats = Stub.getDefaultImpl().queryStatsForUid(volumeUuid, uid, callingPackage);
                            return storageStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        storageStats = (StorageStats) StorageStats.CREATOR.createFromParcel(_reply);
                    } else {
                        storageStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return storageStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public StorageStats queryStatsForUser(String volumeUuid, int userId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(userId);
                    _data.writeString(callingPackage);
                    StorageStats storageStats = 9;
                    if (!this.mRemote.transact(9, _data, _reply, 0)) {
                        storageStats = Stub.getDefaultImpl();
                        if (storageStats != 0) {
                            storageStats = Stub.getDefaultImpl().queryStatsForUser(volumeUuid, userId, callingPackage);
                            return storageStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        storageStats = (StorageStats) StorageStats.CREATOR.createFromParcel(_reply);
                    } else {
                        storageStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return storageStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ExternalStorageStats queryExternalStatsForUser(String volumeUuid, int userId, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(userId);
                    _data.writeString(callingPackage);
                    ExternalStorageStats externalStorageStats = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        externalStorageStats = Stub.getDefaultImpl();
                        if (externalStorageStats != 0) {
                            externalStorageStats = Stub.getDefaultImpl().queryExternalStatsForUser(volumeUuid, userId, callingPackage);
                            return externalStorageStats;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        externalStorageStats = (ExternalStorageStats) ExternalStorageStats.CREATOR.createFromParcel(_reply);
                    } else {
                        externalStorageStats = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return externalStorageStats;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStorageStatsManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IStorageStatsManager)) {
                return new Proxy(obj);
            }
            return (IStorageStatsManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "isQuotaSupported";
                case 2:
                    return "isReservedSupported";
                case 3:
                    return "getTotalBytes";
                case 4:
                    return "getFreeBytes";
                case 5:
                    return "getCacheBytes";
                case 6:
                    return "getCacheQuotaBytes";
                case 7:
                    return "queryStatsForPackage";
                case 8:
                    return "queryStatsForUid";
                case 9:
                    return "queryStatsForUser";
                case 10:
                    return "queryExternalStatsForUser";
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
                boolean _result;
                long _result2;
                StorageStats _result3;
                switch (code) {
                    case 1:
                        data.enforceInterface(descriptor);
                        _result = isQuotaSupported(data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 2:
                        data.enforceInterface(descriptor);
                        _result = isReservedSupported(data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeInt(_result);
                        return true;
                    case 3:
                        data.enforceInterface(descriptor);
                        _result2 = getTotalBytes(data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeLong(_result2);
                        return true;
                    case 4:
                        data.enforceInterface(descriptor);
                        _result2 = getFreeBytes(data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeLong(_result2);
                        return true;
                    case 5:
                        data.enforceInterface(descriptor);
                        _result2 = getCacheBytes(data.readString(), data.readString());
                        reply.writeNoException();
                        reply.writeLong(_result2);
                        return true;
                    case 6:
                        data.enforceInterface(descriptor);
                        long _result4 = getCacheQuotaBytes(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        reply.writeLong(_result4);
                        return true;
                    case 7:
                        data.enforceInterface(descriptor);
                        StorageStats _result5 = queryStatsForPackage(data.readString(), data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result5 != null) {
                            reply.writeInt(1);
                            _result5.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 8:
                        data.enforceInterface(descriptor);
                        _result3 = queryStatsForUid(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 9:
                        data.enforceInterface(descriptor);
                        _result3 = queryStatsForUser(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result3 != null) {
                            reply.writeInt(1);
                            _result3.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    case 10:
                        data.enforceInterface(descriptor);
                        ExternalStorageStats _result6 = queryExternalStatsForUser(data.readString(), data.readInt(), data.readString());
                        reply.writeNoException();
                        if (_result6 != null) {
                            reply.writeInt(1);
                            _result6.writeToParcel(reply, 1);
                        } else {
                            reply.writeInt(0);
                        }
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            reply.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IStorageStatsManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IStorageStatsManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    long getCacheBytes(String str, String str2) throws RemoteException;

    long getCacheQuotaBytes(String str, int i, String str2) throws RemoteException;

    long getFreeBytes(String str, String str2) throws RemoteException;

    long getTotalBytes(String str, String str2) throws RemoteException;

    boolean isQuotaSupported(String str, String str2) throws RemoteException;

    boolean isReservedSupported(String str, String str2) throws RemoteException;

    ExternalStorageStats queryExternalStatsForUser(String str, int i, String str2) throws RemoteException;

    StorageStats queryStatsForPackage(String str, String str2, int i, String str3) throws RemoteException;

    StorageStats queryStatsForUid(String str, int i, String str2) throws RemoteException;

    StorageStats queryStatsForUser(String str, int i, String str2) throws RemoteException;
}
