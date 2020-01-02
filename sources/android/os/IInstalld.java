package android.os;

import android.content.Context;
import java.io.FileDescriptor;

public interface IInstalld extends IInterface {
    public static final int FLAG_CLEAR_CACHE_ONLY = 16;
    public static final int FLAG_CLEAR_CODE_CACHE_ONLY = 32;
    public static final int FLAG_FORCE = 8192;
    public static final int FLAG_FREE_CACHE_NOOP = 1024;
    public static final int FLAG_FREE_CACHE_V2 = 256;
    public static final int FLAG_FREE_CACHE_V2_DEFY_QUOTA = 512;
    public static final int FLAG_STORAGE_CE = 2;
    public static final int FLAG_STORAGE_DE = 1;
    public static final int FLAG_STORAGE_EXTERNAL = 4;
    public static final int FLAG_USE_QUOTA = 4096;

    public static class Default implements IInstalld {
        public void createUserData(String uuid, int userId, int userSerial, int flags) throws RemoteException {
        }

        public void destroyUserData(String uuid, int userId, int flags) throws RemoteException {
        }

        public long createAppData(String uuid, String packageName, int userId, int flags, int appId, String seInfo, int targetSdkVersion) throws RemoteException {
            return 0;
        }

        public void restoreconAppData(String uuid, String packageName, int userId, int flags, int appId, String seInfo) throws RemoteException {
        }

        public void migrateAppData(String uuid, String packageName, int userId, int flags) throws RemoteException {
        }

        public void clearAppData(String uuid, String packageName, int userId, int flags, long ceDataInode) throws RemoteException {
        }

        public void destroyAppData(String uuid, String packageName, int userId, int flags, long ceDataInode) throws RemoteException {
        }

        public void fixupAppData(String uuid, int flags) throws RemoteException {
        }

        public long[] getAppSize(String uuid, String[] packageNames, int userId, int flags, int appId, long[] ceDataInodes, String[] codePaths) throws RemoteException {
            return null;
        }

        public long[] getUserSize(String uuid, int userId, int flags, int[] appIds) throws RemoteException {
            return null;
        }

        public long[] getExternalSize(String uuid, int userId, int flags, int[] appIds) throws RemoteException {
            return null;
        }

        public void setAppQuota(String uuid, int userId, int appId, long cacheQuota) throws RemoteException {
        }

        public void moveCompleteApp(String fromUuid, String toUuid, String packageName, String dataAppName, int appId, String seInfo, int targetSdkVersion) throws RemoteException {
        }

        public void dexopt(String apkPath, int uid, String packageName, String instructionSet, int dexoptNeeded, String outputPath, int dexFlags, String compilerFilter, String uuid, String sharedLibraries, String seInfo, boolean downgrade, int targetSdkVersion, String profileName, String dexMetadataPath, String compilationReason) throws RemoteException {
        }

        public boolean compileLayouts(String apkPath, String packageName, String outDexFile, int uid) throws RemoteException {
            return false;
        }

        public void rmdex(String codePath, String instructionSet) throws RemoteException {
        }

        public boolean mergeProfiles(int uid, String packageName, String profileName) throws RemoteException {
            return false;
        }

        public boolean dumpProfiles(int uid, String packageName, String profileName, String codePath) throws RemoteException {
            return false;
        }

        public boolean copySystemProfile(String systemProfile, int uid, String packageName, String profileName) throws RemoteException {
            return false;
        }

        public void clearAppProfiles(String packageName, String profileName) throws RemoteException {
        }

        public void destroyAppProfiles(String packageName) throws RemoteException {
        }

        public boolean createProfileSnapshot(int appId, String packageName, String profileName, String classpath) throws RemoteException {
            return false;
        }

        public void destroyProfileSnapshot(String packageName, String profileName) throws RemoteException {
        }

        public void idmap(String targetApkPath, String overlayApkPath, int uid) throws RemoteException {
        }

        public void removeIdmap(String overlayApkPath) throws RemoteException {
        }

        public void rmPackageDir(String packageDir) throws RemoteException {
        }

        public void markBootComplete(String instructionSet) throws RemoteException {
        }

        public void freeCache(String uuid, long targetFreeBytes, long cacheReservedBytes, int flags) throws RemoteException {
        }

        public void linkNativeLibraryDirectory(String uuid, String packageName, String nativeLibPath32, int userId) throws RemoteException {
        }

        public void createOatDir(String oatDir, String instructionSet) throws RemoteException {
        }

        public void linkFile(String relativePath, String fromBase, String toBase) throws RemoteException {
        }

        public void moveAb(String apkPath, String instructionSet, String outputPath) throws RemoteException {
        }

        public void deleteOdex(String apkPath, String instructionSet, String outputPath) throws RemoteException {
        }

        public void installApkVerity(String filePath, FileDescriptor verityInput, int contentSize) throws RemoteException {
        }

        public void assertFsverityRootHashMatches(String filePath, byte[] expectedHash) throws RemoteException {
        }

        public boolean reconcileSecondaryDexFile(String dexPath, String pkgName, int uid, String[] isas, String volume_uuid, int storage_flag) throws RemoteException {
            return false;
        }

        public byte[] hashSecondaryDexFile(String dexPath, String pkgName, int uid, String volumeUuid, int storageFlag) throws RemoteException {
            return null;
        }

        public void invalidateMounts() throws RemoteException {
        }

        public boolean isQuotaSupported(String uuid) throws RemoteException {
            return false;
        }

        public boolean prepareAppProfile(String packageName, int userId, int appId, String profileName, String codePath, String dexMetadata) throws RemoteException {
            return false;
        }

        public long snapshotAppData(String uuid, String packageName, int userId, int snapshotId, int storageFlags) throws RemoteException {
            return 0;
        }

        public void restoreAppDataSnapshot(String uuid, String packageName, int appId, String seInfo, int user, int snapshotId, int storageflags) throws RemoteException {
        }

        public void destroyAppDataSnapshot(String uuid, String packageName, int userId, long ceSnapshotInode, int snapshotId, int storageFlags) throws RemoteException {
        }

        public void migrateLegacyObbData() throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IInstalld {
        private static final String DESCRIPTOR = "android.os.IInstalld";
        static final int TRANSACTION_assertFsverityRootHashMatches = 35;
        static final int TRANSACTION_clearAppData = 6;
        static final int TRANSACTION_clearAppProfiles = 20;
        static final int TRANSACTION_compileLayouts = 15;
        static final int TRANSACTION_copySystemProfile = 19;
        static final int TRANSACTION_createAppData = 3;
        static final int TRANSACTION_createOatDir = 30;
        static final int TRANSACTION_createProfileSnapshot = 22;
        static final int TRANSACTION_createUserData = 1;
        static final int TRANSACTION_deleteOdex = 33;
        static final int TRANSACTION_destroyAppData = 7;
        static final int TRANSACTION_destroyAppDataSnapshot = 43;
        static final int TRANSACTION_destroyAppProfiles = 21;
        static final int TRANSACTION_destroyProfileSnapshot = 23;
        static final int TRANSACTION_destroyUserData = 2;
        static final int TRANSACTION_dexopt = 14;
        static final int TRANSACTION_dumpProfiles = 18;
        static final int TRANSACTION_fixupAppData = 8;
        static final int TRANSACTION_freeCache = 28;
        static final int TRANSACTION_getAppSize = 9;
        static final int TRANSACTION_getExternalSize = 11;
        static final int TRANSACTION_getUserSize = 10;
        static final int TRANSACTION_hashSecondaryDexFile = 37;
        static final int TRANSACTION_idmap = 24;
        static final int TRANSACTION_installApkVerity = 34;
        static final int TRANSACTION_invalidateMounts = 38;
        static final int TRANSACTION_isQuotaSupported = 39;
        static final int TRANSACTION_linkFile = 31;
        static final int TRANSACTION_linkNativeLibraryDirectory = 29;
        static final int TRANSACTION_markBootComplete = 27;
        static final int TRANSACTION_mergeProfiles = 17;
        static final int TRANSACTION_migrateAppData = 5;
        static final int TRANSACTION_migrateLegacyObbData = 44;
        static final int TRANSACTION_moveAb = 32;
        static final int TRANSACTION_moveCompleteApp = 13;
        static final int TRANSACTION_prepareAppProfile = 40;
        static final int TRANSACTION_reconcileSecondaryDexFile = 36;
        static final int TRANSACTION_removeIdmap = 25;
        static final int TRANSACTION_restoreAppDataSnapshot = 42;
        static final int TRANSACTION_restoreconAppData = 4;
        static final int TRANSACTION_rmPackageDir = 26;
        static final int TRANSACTION_rmdex = 16;
        static final int TRANSACTION_setAppQuota = 12;
        static final int TRANSACTION_snapshotAppData = 41;

        private static class Proxy implements IInstalld {
            public static IInstalld sDefaultImpl;
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

            public void createUserData(String uuid, int userId, int userSerial, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().createUserData(uuid, userId, userSerial, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void destroyUserData(String uuid, int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroyUserData(uuid, userId, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long createAppData(String uuid, String packageName, int userId, int flags, int appId, String seInfo, int targetSdkVersion) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                int i3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(uuid);
                    } catch (Throwable th2) {
                        th = th2;
                        str = packageName;
                        i = userId;
                        i2 = flags;
                        i3 = appId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th3) {
                        th = th3;
                        i = userId;
                        i2 = flags;
                        i3 = appId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        try {
                            _data.writeInt(flags);
                        } catch (Throwable th4) {
                            th = th4;
                            i3 = appId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(appId);
                            _data.writeString(seInfo);
                            _data.writeInt(targetSdkVersion);
                            long _result;
                            if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _result = _reply.readLong();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            _result = Stub.getDefaultImpl().createAppData(uuid, packageName, userId, flags, appId, seInfo, targetSdkVersion);
                            _reply.recycle();
                            _data.recycle();
                            return _result;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i2 = flags;
                        i3 = appId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = uuid;
                    str = packageName;
                    i = userId;
                    i2 = flags;
                    i3 = appId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void restoreconAppData(String uuid, String packageName, int userId, int flags, int appId, String seInfo) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                int i3;
                String str2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(uuid);
                    } catch (Throwable th2) {
                        th = th2;
                        str = packageName;
                        i = userId;
                        i2 = flags;
                        i3 = appId;
                        str2 = seInfo;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(packageName);
                        try {
                            _data.writeInt(userId);
                            try {
                                _data.writeInt(flags);
                            } catch (Throwable th3) {
                                th = th3;
                                i3 = appId;
                                str2 = seInfo;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            i2 = flags;
                            i3 = appId;
                            str2 = seInfo;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i = userId;
                        i2 = flags;
                        i3 = appId;
                        str2 = seInfo;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(appId);
                        try {
                            _data.writeString(seInfo);
                            if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().restoreconAppData(uuid, packageName, userId, flags, appId, seInfo);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th6) {
                            th = th6;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        str2 = seInfo;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    String str3 = uuid;
                    str = packageName;
                    i = userId;
                    i2 = flags;
                    i3 = appId;
                    str2 = seInfo;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void migrateAppData(String uuid, String packageName, int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().migrateAppData(uuid, packageName, userId, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearAppData(String uuid, String packageName, int userId, int flags, long ceDataInode) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                long j;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(uuid);
                    } catch (Throwable th2) {
                        th = th2;
                        str = packageName;
                        i = userId;
                        i2 = flags;
                        j = ceDataInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th3) {
                        th = th3;
                        i = userId;
                        i2 = flags;
                        j = ceDataInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        try {
                            _data.writeInt(flags);
                        } catch (Throwable th4) {
                            th = th4;
                            j = ceDataInode;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeLong(ceDataInode);
                            if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().clearAppData(uuid, packageName, userId, flags, ceDataInode);
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
                        i2 = flags;
                        j = ceDataInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = uuid;
                    str = packageName;
                    i = userId;
                    i2 = flags;
                    j = ceDataInode;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void destroyAppData(String uuid, String packageName, int userId, int flags, long ceDataInode) throws RemoteException {
                Throwable th;
                String str;
                int i;
                int i2;
                long j;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(uuid);
                    } catch (Throwable th2) {
                        th = th2;
                        str = packageName;
                        i = userId;
                        i2 = flags;
                        j = ceDataInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th3) {
                        th = th3;
                        i = userId;
                        i2 = flags;
                        j = ceDataInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        try {
                            _data.writeInt(flags);
                        } catch (Throwable th4) {
                            th = th4;
                            j = ceDataInode;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeLong(ceDataInode);
                            if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().destroyAppData(uuid, packageName, userId, flags, ceDataInode);
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
                        i2 = flags;
                        j = ceDataInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str2 = uuid;
                    str = packageName;
                    i = userId;
                    i2 = flags;
                    j = ceDataInode;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void fixupAppData(String uuid, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fixupAppData(uuid, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long[] getAppSize(String uuid, String[] packageNames, int userId, int flags, int appId, long[] ceDataInodes, String[] codePaths) throws RemoteException {
                Throwable th;
                String[] strArr;
                int i;
                int i2;
                int i3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(uuid);
                    } catch (Throwable th2) {
                        th = th2;
                        strArr = packageNames;
                        i = userId;
                        i2 = flags;
                        i3 = appId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeStringArray(packageNames);
                    } catch (Throwable th3) {
                        th = th3;
                        i = userId;
                        i2 = flags;
                        i3 = appId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        try {
                            _data.writeInt(flags);
                        } catch (Throwable th4) {
                            th = th4;
                            i3 = appId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(appId);
                            _data.writeLongArray(ceDataInodes);
                            _data.writeStringArray(codePaths);
                            if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                long[] _result = _reply.createLongArray();
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            long[] appSize = Stub.getDefaultImpl().getAppSize(uuid, packageNames, userId, flags, appId, ceDataInodes, codePaths);
                            _reply.recycle();
                            _data.recycle();
                            return appSize;
                        } catch (Throwable th5) {
                            th = th5;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        i2 = flags;
                        i3 = appId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str = uuid;
                    strArr = packageNames;
                    i = userId;
                    i2 = flags;
                    i3 = appId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public long[] getUserSize(String uuid, int userId, int flags, int[] appIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    _data.writeIntArray(appIds);
                    long[] jArr = 10;
                    if (!this.mRemote.transact(10, _data, _reply, 0)) {
                        jArr = Stub.getDefaultImpl();
                        if (jArr != 0) {
                            jArr = Stub.getDefaultImpl().getUserSize(uuid, userId, flags, appIds);
                            return jArr;
                        }
                    }
                    _reply.readException();
                    jArr = _reply.createLongArray();
                    long[] _result = jArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long[] getExternalSize(String uuid, int userId, int flags, int[] appIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    _data.writeIntArray(appIds);
                    long[] jArr = 11;
                    if (!this.mRemote.transact(11, _data, _reply, 0)) {
                        jArr = Stub.getDefaultImpl();
                        if (jArr != 0) {
                            jArr = Stub.getDefaultImpl().getExternalSize(uuid, userId, flags, appIds);
                            return jArr;
                        }
                    }
                    _reply.readException();
                    jArr = _reply.createLongArray();
                    long[] _result = jArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setAppQuota(String uuid, int userId, int appId, long cacheQuota) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(appId);
                    _data.writeLong(cacheQuota);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setAppQuota(uuid, userId, appId, cacheQuota);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void moveCompleteApp(String fromUuid, String toUuid, String packageName, String dataAppName, int appId, String seInfo, int targetSdkVersion) throws RemoteException {
                Throwable th;
                String str;
                String str2;
                String str3;
                int i;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(fromUuid);
                    } catch (Throwable th2) {
                        th = th2;
                        str = toUuid;
                        str2 = packageName;
                        str3 = dataAppName;
                        i = appId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(toUuid);
                    } catch (Throwable th3) {
                        th = th3;
                        str2 = packageName;
                        str3 = dataAppName;
                        i = appId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(packageName);
                        try {
                            _data.writeString(dataAppName);
                        } catch (Throwable th4) {
                            th = th4;
                            i = appId;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(appId);
                            _data.writeString(seInfo);
                            _data.writeInt(targetSdkVersion);
                            if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().moveCompleteApp(fromUuid, toUuid, packageName, dataAppName, appId, seInfo, targetSdkVersion);
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
                        str3 = dataAppName;
                        i = appId;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str4 = fromUuid;
                    str = toUuid;
                    str2 = packageName;
                    str3 = dataAppName;
                    i = appId;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void dexopt(String apkPath, int uid, String packageName, String instructionSet, int dexoptNeeded, String outputPath, int dexFlags, String compilerFilter, String uuid, String sharedLibraries, String seInfo, boolean downgrade, int targetSdkVersion, String profileName, String dexMetadataPath, String compilationReason) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apkPath);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeString(instructionSet);
                    _data.writeInt(dexoptNeeded);
                    _data.writeString(outputPath);
                    _data.writeInt(dexFlags);
                    _data.writeString(compilerFilter);
                    _data.writeString(uuid);
                    _data.writeString(sharedLibraries);
                    _data.writeString(seInfo);
                    _data.writeInt(downgrade ? 1 : 0);
                    _data.writeInt(targetSdkVersion);
                    _data.writeString(profileName);
                    _data.writeString(dexMetadataPath);
                    _data.writeString(compilationReason);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().dexopt(apkPath, uid, packageName, instructionSet, dexoptNeeded, outputPath, dexFlags, compilerFilter, uuid, sharedLibraries, seInfo, downgrade, targetSdkVersion, profileName, dexMetadataPath, compilationReason);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean compileLayouts(String apkPath, String packageName, String outDexFile, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apkPath);
                    _data.writeString(packageName);
                    _data.writeString(outDexFile);
                    _data.writeInt(uid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(15, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().compileLayouts(apkPath, packageName, outDexFile, uid);
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

            public void rmdex(String codePath, String instructionSet) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(codePath);
                    _data.writeString(instructionSet);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().rmdex(codePath, instructionSet);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean mergeProfiles(int uid, String packageName, String profileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(17, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().mergeProfiles(uid, packageName, profileName);
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

            public boolean dumpProfiles(int uid, String packageName, String profileName, String codePath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    _data.writeString(codePath);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(18, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().dumpProfiles(uid, packageName, profileName, codePath);
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

            public boolean copySystemProfile(String systemProfile, int uid, String packageName, String profileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(systemProfile);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(19, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().copySystemProfile(systemProfile, uid, packageName, profileName);
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

            public void clearAppProfiles(String packageName, String profileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearAppProfiles(packageName, profileName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void destroyAppProfiles(String packageName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroyAppProfiles(packageName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean createProfileSnapshot(int appId, String packageName, String profileName, String classpath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(appId);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    _data.writeString(classpath);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().createProfileSnapshot(appId, packageName, profileName, classpath);
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

            public void destroyProfileSnapshot(String packageName, String profileName) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(profileName);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroyProfileSnapshot(packageName, profileName);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void idmap(String targetApkPath, String overlayApkPath, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(targetApkPath);
                    _data.writeString(overlayApkPath);
                    _data.writeInt(uid);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().idmap(targetApkPath, overlayApkPath, uid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void removeIdmap(String overlayApkPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(overlayApkPath);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().removeIdmap(overlayApkPath);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void rmPackageDir(String packageDir) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageDir);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().rmPackageDir(packageDir);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void markBootComplete(String instructionSet) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(instructionSet);
                    if (this.mRemote.transact(27, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().markBootComplete(instructionSet);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void freeCache(String uuid, long targetFreeBytes, long cacheReservedBytes, int flags) throws RemoteException {
                Throwable th;
                long j;
                long j2;
                int i;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(uuid);
                    } catch (Throwable th2) {
                        th = th2;
                        j = targetFreeBytes;
                        j2 = cacheReservedBytes;
                        i = flags;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(targetFreeBytes);
                    } catch (Throwable th3) {
                        th = th3;
                        j2 = cacheReservedBytes;
                        i = flags;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeLong(cacheReservedBytes);
                        try {
                            _data.writeInt(flags);
                            if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().freeCache(uuid, targetFreeBytes, cacheReservedBytes, flags);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i = flags;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str = uuid;
                    j = targetFreeBytes;
                    j2 = cacheReservedBytes;
                    i = flags;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void linkNativeLibraryDirectory(String uuid, String packageName, String nativeLibPath32, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeString(packageName);
                    _data.writeString(nativeLibPath32);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().linkNativeLibraryDirectory(uuid, packageName, nativeLibPath32, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void createOatDir(String oatDir, String instructionSet) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(oatDir);
                    _data.writeString(instructionSet);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().createOatDir(oatDir, instructionSet);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void linkFile(String relativePath, String fromBase, String toBase) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(relativePath);
                    _data.writeString(fromBase);
                    _data.writeString(toBase);
                    if (this.mRemote.transact(31, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().linkFile(relativePath, fromBase, toBase);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void moveAb(String apkPath, String instructionSet, String outputPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apkPath);
                    _data.writeString(instructionSet);
                    _data.writeString(outputPath);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().moveAb(apkPath, instructionSet, outputPath);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void deleteOdex(String apkPath, String instructionSet, String outputPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(apkPath);
                    _data.writeString(instructionSet);
                    _data.writeString(outputPath);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().deleteOdex(apkPath, instructionSet, outputPath);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void installApkVerity(String filePath, FileDescriptor verityInput, int contentSize) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(filePath);
                    _data.writeRawFileDescriptor(verityInput);
                    _data.writeInt(contentSize);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().installApkVerity(filePath, verityInput, contentSize);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void assertFsverityRootHashMatches(String filePath, byte[] expectedHash) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(filePath);
                    _data.writeByteArray(expectedHash);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().assertFsverityRootHashMatches(filePath, expectedHash);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean reconcileSecondaryDexFile(String dexPath, String pkgName, int uid, String[] isas, String volume_uuid, int storage_flag) throws RemoteException {
                Throwable th;
                String str;
                int i;
                String[] strArr;
                String str2;
                int i2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(dexPath);
                    } catch (Throwable th2) {
                        th = th2;
                        str = pkgName;
                        i = uid;
                        strArr = isas;
                        str2 = volume_uuid;
                        i2 = storage_flag;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(pkgName);
                        try {
                            _data.writeInt(uid);
                            try {
                                _data.writeStringArray(isas);
                            } catch (Throwable th3) {
                                th = th3;
                                str2 = volume_uuid;
                                i2 = storage_flag;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            strArr = isas;
                            str2 = volume_uuid;
                            i2 = storage_flag;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i = uid;
                        strArr = isas;
                        str2 = volume_uuid;
                        i2 = storage_flag;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(volume_uuid);
                        try {
                            _data.writeInt(storage_flag);
                            boolean z = false;
                            if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    z = true;
                                }
                                boolean _result = z;
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            boolean reconcileSecondaryDexFile = Stub.getDefaultImpl().reconcileSecondaryDexFile(dexPath, pkgName, uid, isas, volume_uuid, storage_flag);
                            _reply.recycle();
                            _data.recycle();
                            return reconcileSecondaryDexFile;
                        } catch (Throwable th6) {
                            th = th6;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        i2 = storage_flag;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    String str3 = dexPath;
                    str = pkgName;
                    i = uid;
                    strArr = isas;
                    str2 = volume_uuid;
                    i2 = storage_flag;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public byte[] hashSecondaryDexFile(String dexPath, String pkgName, int uid, String volumeUuid, int storageFlag) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(dexPath);
                    _data.writeString(pkgName);
                    _data.writeInt(uid);
                    _data.writeString(volumeUuid);
                    _data.writeInt(storageFlag);
                    byte[] bArr = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        bArr = Stub.getDefaultImpl();
                        if (bArr != 0) {
                            bArr = Stub.getDefaultImpl().hashSecondaryDexFile(dexPath, pkgName, uid, volumeUuid, storageFlag);
                            return bArr;
                        }
                    }
                    _reply.readException();
                    bArr = _reply.createByteArray();
                    byte[] _result = bArr;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void invalidateMounts() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(38, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().invalidateMounts();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isQuotaSupported(String uuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(39, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isQuotaSupported(uuid);
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

            public boolean prepareAppProfile(String packageName, int userId, int appId, String profileName, String codePath, String dexMetadata) throws RemoteException {
                Throwable th;
                int i;
                int i2;
                String str;
                String str2;
                String str3;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th2) {
                        th = th2;
                        i = userId;
                        i2 = appId;
                        str = profileName;
                        str2 = codePath;
                        str3 = dexMetadata;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        try {
                            _data.writeInt(appId);
                            try {
                                _data.writeString(profileName);
                            } catch (Throwable th3) {
                                th = th3;
                                str2 = codePath;
                                str3 = dexMetadata;
                                _reply.recycle();
                                _data.recycle();
                                throw th;
                            }
                        } catch (Throwable th4) {
                            th = th4;
                            str = profileName;
                            str2 = codePath;
                            str3 = dexMetadata;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        i2 = appId;
                        str = profileName;
                        str2 = codePath;
                        str3 = dexMetadata;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(codePath);
                        try {
                            _data.writeString(dexMetadata);
                            boolean z = false;
                            if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                if (_reply.readInt() != 0) {
                                    z = true;
                                }
                                boolean _result = z;
                                _reply.recycle();
                                _data.recycle();
                                return _result;
                            }
                            boolean prepareAppProfile = Stub.getDefaultImpl().prepareAppProfile(packageName, userId, appId, profileName, codePath, dexMetadata);
                            _reply.recycle();
                            _data.recycle();
                            return prepareAppProfile;
                        } catch (Throwable th6) {
                            th = th6;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th7) {
                        th = th7;
                        str3 = dexMetadata;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th8) {
                    th = th8;
                    String str4 = packageName;
                    i = userId;
                    i2 = appId;
                    str = profileName;
                    str2 = codePath;
                    str3 = dexMetadata;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public long snapshotAppData(String uuid, String packageName, int userId, int snapshotId, int storageFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeString(packageName);
                    _data.writeInt(userId);
                    _data.writeInt(snapshotId);
                    _data.writeInt(storageFlags);
                    long j = 41;
                    if (!this.mRemote.transact(41, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().snapshotAppData(uuid, packageName, userId, snapshotId, storageFlags);
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

            public void restoreAppDataSnapshot(String uuid, String packageName, int appId, String seInfo, int user, int snapshotId, int storageflags) throws RemoteException {
                Throwable th;
                String str;
                int i;
                String str2;
                int i2;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(uuid);
                    } catch (Throwable th2) {
                        th = th2;
                        str = packageName;
                        i = appId;
                        str2 = seInfo;
                        i2 = user;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th3) {
                        th = th3;
                        i = appId;
                        str2 = seInfo;
                        i2 = user;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(appId);
                        try {
                            _data.writeString(seInfo);
                        } catch (Throwable th4) {
                            th = th4;
                            i2 = user;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(user);
                            _data.writeInt(snapshotId);
                            _data.writeInt(storageflags);
                            if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().restoreAppDataSnapshot(uuid, packageName, appId, seInfo, user, snapshotId, storageflags);
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
                        str2 = seInfo;
                        i2 = user;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str3 = uuid;
                    str = packageName;
                    i = appId;
                    str2 = seInfo;
                    i2 = user;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void destroyAppDataSnapshot(String uuid, String packageName, int userId, long ceSnapshotInode, int snapshotId, int storageFlags) throws RemoteException {
                Throwable th;
                String str;
                int i;
                long j;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(uuid);
                    } catch (Throwable th2) {
                        th = th2;
                        str = packageName;
                        i = userId;
                        j = ceSnapshotInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeString(packageName);
                    } catch (Throwable th3) {
                        th = th3;
                        i = userId;
                        j = ceSnapshotInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                    try {
                        _data.writeInt(userId);
                        try {
                            _data.writeLong(ceSnapshotInode);
                            _data.writeInt(snapshotId);
                            _data.writeInt(storageFlags);
                            if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                _reply.readException();
                                _reply.recycle();
                                _data.recycle();
                                return;
                            }
                            Stub.getDefaultImpl().destroyAppDataSnapshot(uuid, packageName, userId, ceSnapshotInode, snapshotId, storageFlags);
                            _reply.recycle();
                            _data.recycle();
                        } catch (Throwable th4) {
                            th = th4;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th5) {
                        th = th5;
                        j = ceSnapshotInode;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th6) {
                    th = th6;
                    String str2 = uuid;
                    str = packageName;
                    i = userId;
                    j = ceSnapshotInode;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void migrateLegacyObbData() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().migrateLegacyObbData();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IInstalld asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IInstalld)) {
                return new Proxy(obj);
            }
            return (IInstalld) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "createUserData";
                case 2:
                    return "destroyUserData";
                case 3:
                    return "createAppData";
                case 4:
                    return "restoreconAppData";
                case 5:
                    return "migrateAppData";
                case 6:
                    return "clearAppData";
                case 7:
                    return "destroyAppData";
                case 8:
                    return "fixupAppData";
                case 9:
                    return "getAppSize";
                case 10:
                    return "getUserSize";
                case 11:
                    return "getExternalSize";
                case 12:
                    return "setAppQuota";
                case 13:
                    return "moveCompleteApp";
                case 14:
                    return "dexopt";
                case 15:
                    return "compileLayouts";
                case 16:
                    return "rmdex";
                case 17:
                    return "mergeProfiles";
                case 18:
                    return "dumpProfiles";
                case 19:
                    return "copySystemProfile";
                case 20:
                    return "clearAppProfiles";
                case 21:
                    return "destroyAppProfiles";
                case 22:
                    return "createProfileSnapshot";
                case 23:
                    return "destroyProfileSnapshot";
                case 24:
                    return Context.IDMAP_SERVICE;
                case 25:
                    return "removeIdmap";
                case 26:
                    return "rmPackageDir";
                case 27:
                    return "markBootComplete";
                case 28:
                    return "freeCache";
                case 29:
                    return "linkNativeLibraryDirectory";
                case 30:
                    return "createOatDir";
                case 31:
                    return "linkFile";
                case 32:
                    return "moveAb";
                case 33:
                    return "deleteOdex";
                case 34:
                    return "installApkVerity";
                case 35:
                    return "assertFsverityRootHashMatches";
                case 36:
                    return "reconcileSecondaryDexFile";
                case 37:
                    return "hashSecondaryDexFile";
                case 38:
                    return "invalidateMounts";
                case 39:
                    return "isQuotaSupported";
                case 40:
                    return "prepareAppProfile";
                case 41:
                    return "snapshotAppData";
                case 42:
                    return "restoreAppDataSnapshot";
                case 43:
                    return "destroyAppDataSnapshot";
                case 44:
                    return "migrateLegacyObbData";
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
            Parcel parcel3;
            if (i != 1598968902) {
                long _result;
                long[] _result2;
                boolean _result3;
                boolean _result4;
                switch (i) {
                    case 1:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        createUserData(data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        destroyUserData(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        _result = createAppData(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel3.writeLong(_result);
                        return true;
                    case 4:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        restoreconAppData(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readInt(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        migrateAppData(data.readString(), data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        clearAppData(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        destroyAppData(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        fixupAppData(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        long[] _result5 = getAppSize(data.readString(), data.createStringArray(), data.readInt(), data.readInt(), data.readInt(), data.createLongArray(), data.createStringArray());
                        reply.writeNoException();
                        parcel3.writeLongArray(_result5);
                        return true;
                    case 10:
                        parcel3 = parcel2;
                        parcel.enforceInterface(descriptor);
                        _result2 = getUserSize(data.readString(), data.readInt(), data.readInt(), data.createIntArray());
                        reply.writeNoException();
                        parcel3.writeLongArray(_result2);
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        _result2 = getExternalSize(data.readString(), data.readInt(), data.readInt(), data.createIntArray());
                        reply.writeNoException();
                        reply.writeLongArray(_result2);
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        setAppQuota(data.readString(), data.readInt(), data.readInt(), data.readLong());
                        reply.writeNoException();
                        return true;
                    case 13:
                        data.enforceInterface(descriptor);
                        moveCompleteApp(data.readString(), data.readString(), data.readString(), data.readString(), data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        dexopt(data.readString(), data.readInt(), data.readString(), data.readString(), data.readInt(), data.readString(), data.readInt(), data.readString(), data.readString(), data.readString(), data.readString(), data.readInt() != 0, data.readInt(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        _result3 = compileLayouts(data.readString(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        rmdex(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        boolean _result6 = mergeProfiles(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result6);
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        _result3 = dumpProfiles(data.readInt(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        _result3 = copySystemProfile(data.readString(), data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        clearAppProfiles(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        destroyAppProfiles(data.readString());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        _result3 = createProfileSnapshot(data.readInt(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result3);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        destroyProfileSnapshot(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        idmap(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        removeIdmap(data.readString());
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        rmPackageDir(data.readString());
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        markBootComplete(data.readString());
                        reply.writeNoException();
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        freeCache(data.readString(), data.readLong(), data.readLong(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        linkNativeLibraryDirectory(data.readString(), data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        createOatDir(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        linkFile(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        moveAb(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        deleteOdex(data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        installApkVerity(data.readString(), data.readRawFileDescriptor(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        assertFsverityRootHashMatches(data.readString(), data.createByteArray());
                        reply.writeNoException();
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        _result4 = reconcileSecondaryDexFile(data.readString(), data.readString(), data.readInt(), data.createStringArray(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        byte[] _result7 = hashSecondaryDexFile(data.readString(), data.readString(), data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeByteArray(_result7);
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        invalidateMounts();
                        reply.writeNoException();
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        boolean _result8 = isQuotaSupported(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result8);
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        _result4 = prepareAppProfile(data.readString(), data.readInt(), data.readInt(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        _result = snapshotAppData(data.readString(), data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeLong(_result);
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        restoreAppDataSnapshot(data.readString(), data.readString(), data.readInt(), data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        destroyAppDataSnapshot(data.readString(), data.readString(), data.readInt(), data.readLong(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        migrateLegacyObbData();
                        reply.writeNoException();
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel3 = parcel2;
            parcel2 = parcel;
            parcel3.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IInstalld impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IInstalld getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void assertFsverityRootHashMatches(String str, byte[] bArr) throws RemoteException;

    void clearAppData(String str, String str2, int i, int i2, long j) throws RemoteException;

    void clearAppProfiles(String str, String str2) throws RemoteException;

    boolean compileLayouts(String str, String str2, String str3, int i) throws RemoteException;

    boolean copySystemProfile(String str, int i, String str2, String str3) throws RemoteException;

    long createAppData(String str, String str2, int i, int i2, int i3, String str3, int i4) throws RemoteException;

    void createOatDir(String str, String str2) throws RemoteException;

    boolean createProfileSnapshot(int i, String str, String str2, String str3) throws RemoteException;

    void createUserData(String str, int i, int i2, int i3) throws RemoteException;

    void deleteOdex(String str, String str2, String str3) throws RemoteException;

    void destroyAppData(String str, String str2, int i, int i2, long j) throws RemoteException;

    void destroyAppDataSnapshot(String str, String str2, int i, long j, int i2, int i3) throws RemoteException;

    void destroyAppProfiles(String str) throws RemoteException;

    void destroyProfileSnapshot(String str, String str2) throws RemoteException;

    void destroyUserData(String str, int i, int i2) throws RemoteException;

    void dexopt(String str, int i, String str2, String str3, int i2, String str4, int i3, String str5, String str6, String str7, String str8, boolean z, int i4, String str9, String str10, String str11) throws RemoteException;

    boolean dumpProfiles(int i, String str, String str2, String str3) throws RemoteException;

    void fixupAppData(String str, int i) throws RemoteException;

    void freeCache(String str, long j, long j2, int i) throws RemoteException;

    long[] getAppSize(String str, String[] strArr, int i, int i2, int i3, long[] jArr, String[] strArr2) throws RemoteException;

    long[] getExternalSize(String str, int i, int i2, int[] iArr) throws RemoteException;

    long[] getUserSize(String str, int i, int i2, int[] iArr) throws RemoteException;

    byte[] hashSecondaryDexFile(String str, String str2, int i, String str3, int i2) throws RemoteException;

    void idmap(String str, String str2, int i) throws RemoteException;

    void installApkVerity(String str, FileDescriptor fileDescriptor, int i) throws RemoteException;

    void invalidateMounts() throws RemoteException;

    boolean isQuotaSupported(String str) throws RemoteException;

    void linkFile(String str, String str2, String str3) throws RemoteException;

    void linkNativeLibraryDirectory(String str, String str2, String str3, int i) throws RemoteException;

    void markBootComplete(String str) throws RemoteException;

    boolean mergeProfiles(int i, String str, String str2) throws RemoteException;

    void migrateAppData(String str, String str2, int i, int i2) throws RemoteException;

    void migrateLegacyObbData() throws RemoteException;

    void moveAb(String str, String str2, String str3) throws RemoteException;

    void moveCompleteApp(String str, String str2, String str3, String str4, int i, String str5, int i2) throws RemoteException;

    boolean prepareAppProfile(String str, int i, int i2, String str2, String str3, String str4) throws RemoteException;

    boolean reconcileSecondaryDexFile(String str, String str2, int i, String[] strArr, String str3, int i2) throws RemoteException;

    void removeIdmap(String str) throws RemoteException;

    void restoreAppDataSnapshot(String str, String str2, int i, String str3, int i2, int i3, int i4) throws RemoteException;

    void restoreconAppData(String str, String str2, int i, int i2, int i3, String str3) throws RemoteException;

    void rmPackageDir(String str) throws RemoteException;

    void rmdex(String str, String str2) throws RemoteException;

    void setAppQuota(String str, int i, int i2, long j) throws RemoteException;

    long snapshotAppData(String str, String str2, int i, int i2, int i3) throws RemoteException;
}
