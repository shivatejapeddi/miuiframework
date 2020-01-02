package android.os.storage;

import android.content.pm.IPackageMoveObserver;
import android.content.res.ObbInfo;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.IVoldTaskListener;
import android.os.Parcel;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import com.android.internal.os.AppFuseMount;

public interface IStorageManager extends IInterface {

    public static class Default implements IStorageManager {
        public void registerListener(IStorageEventListener listener) throws RemoteException {
        }

        public void unregisterListener(IStorageEventListener listener) throws RemoteException {
        }

        public void shutdown(IStorageShutdownObserver observer) throws RemoteException {
        }

        public void mountObb(String rawPath, String canonicalPath, String key, IObbActionListener token, int nonce, ObbInfo obbInfo) throws RemoteException {
        }

        public void unmountObb(String rawPath, boolean force, IObbActionListener token, int nonce) throws RemoteException {
        }

        public boolean isObbMounted(String rawPath) throws RemoteException {
            return false;
        }

        public String getMountedObbPath(String rawPath) throws RemoteException {
            return null;
        }

        public int decryptStorage(String password) throws RemoteException {
            return 0;
        }

        public int encryptStorage(int type, String password) throws RemoteException {
            return 0;
        }

        public int changeEncryptionPassword(int type, String password) throws RemoteException {
            return 0;
        }

        public StorageVolume[] getVolumeList(int uid, String packageName, int flags) throws RemoteException {
            return null;
        }

        public int getEncryptionState() throws RemoteException {
            return 0;
        }

        public int verifyEncryptionPassword(String password) throws RemoteException {
            return 0;
        }

        public void mkdirs(String callingPkg, String path) throws RemoteException {
        }

        public int getPasswordType() throws RemoteException {
            return 0;
        }

        public String getPassword() throws RemoteException {
            return null;
        }

        public void clearPassword() throws RemoteException {
        }

        public void setField(String field, String contents) throws RemoteException {
        }

        public String getField(String field) throws RemoteException {
            return null;
        }

        public long lastMaintenance() throws RemoteException {
            return 0;
        }

        public void runMaintenance() throws RemoteException {
        }

        public DiskInfo[] getDisks() throws RemoteException {
            return null;
        }

        public VolumeInfo[] getVolumes(int flags) throws RemoteException {
            return null;
        }

        public VolumeRecord[] getVolumeRecords(int flags) throws RemoteException {
            return null;
        }

        public void mount(String volId) throws RemoteException {
        }

        public void unmount(String volId) throws RemoteException {
        }

        public void format(String volId) throws RemoteException {
        }

        public void partitionPublic(String diskId) throws RemoteException {
        }

        public void partitionPrivate(String diskId) throws RemoteException {
        }

        public void partitionMixed(String diskId, int ratio) throws RemoteException {
        }

        public void setVolumeNickname(String fsUuid, String nickname) throws RemoteException {
        }

        public void setVolumeUserFlags(String fsUuid, int flags, int mask) throws RemoteException {
        }

        public void forgetVolume(String fsUuid) throws RemoteException {
        }

        public void forgetAllVolumes() throws RemoteException {
        }

        public String getPrimaryStorageUuid() throws RemoteException {
            return null;
        }

        public void setPrimaryStorageUuid(String volumeUuid, IPackageMoveObserver callback) throws RemoteException {
        }

        public void benchmark(String volId, IVoldTaskListener listener) throws RemoteException {
        }

        public void setDebugFlags(int flags, int mask) throws RemoteException {
        }

        public void createUserKey(int userId, int serialNumber, boolean ephemeral) throws RemoteException {
        }

        public void destroyUserKey(int userId) throws RemoteException {
        }

        public void unlockUserKey(int userId, int serialNumber, byte[] token, byte[] secret) throws RemoteException {
        }

        public void lockUserKey(int userId) throws RemoteException {
        }

        public boolean isUserKeyUnlocked(int userId) throws RemoteException {
            return false;
        }

        public void prepareUserStorage(String volumeUuid, int userId, int serialNumber, int flags) throws RemoteException {
        }

        public void destroyUserStorage(String volumeUuid, int userId, int flags) throws RemoteException {
        }

        public boolean isConvertibleToFBE() throws RemoteException {
            return false;
        }

        public void addUserKeyAuth(int userId, int serialNumber, byte[] token, byte[] secret) throws RemoteException {
        }

        public void fixateNewestUserKeyAuth(int userId) throws RemoteException {
        }

        public void fstrim(int flags, IVoldTaskListener listener) throws RemoteException {
        }

        public AppFuseMount mountProxyFileDescriptorBridge() throws RemoteException {
            return null;
        }

        public ParcelFileDescriptor openProxyFileDescriptor(int mountPointId, int fileId, int mode) throws RemoteException {
            return null;
        }

        public long getCacheQuotaBytes(String volumeUuid, int uid) throws RemoteException {
            return 0;
        }

        public long getCacheSizeBytes(String volumeUuid, int uid) throws RemoteException {
            return 0;
        }

        public long getAllocatableBytes(String volumeUuid, int flags, String callingPackage) throws RemoteException {
            return 0;
        }

        public void allocateBytes(String volumeUuid, long bytes, int flags, String callingPackage) throws RemoteException {
        }

        public void runIdleMaintenance() throws RemoteException {
        }

        public void abortIdleMaintenance() throws RemoteException {
        }

        public void commitChanges() throws RemoteException {
        }

        public boolean supportsCheckpoint() throws RemoteException {
            return false;
        }

        public void startCheckpoint(int numTries) throws RemoteException {
        }

        public boolean needsCheckpoint() throws RemoteException {
            return false;
        }

        public void abortChanges(String message, boolean retry) throws RemoteException {
        }

        public void clearUserKeyAuth(int userId, int serialNumber, byte[] token, byte[] secret) throws RemoteException {
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IStorageManager {
        private static final String DESCRIPTOR = "android.os.storage.IStorageManager";
        static final int TRANSACTION_abortChanges = 88;
        static final int TRANSACTION_abortIdleMaintenance = 81;
        static final int TRANSACTION_addUserKeyAuth = 71;
        static final int TRANSACTION_allocateBytes = 79;
        static final int TRANSACTION_benchmark = 60;
        static final int TRANSACTION_changeEncryptionPassword = 29;
        static final int TRANSACTION_clearPassword = 38;
        static final int TRANSACTION_clearUserKeyAuth = 89;
        static final int TRANSACTION_commitChanges = 84;
        static final int TRANSACTION_createUserKey = 62;
        static final int TRANSACTION_decryptStorage = 27;
        static final int TRANSACTION_destroyUserKey = 63;
        static final int TRANSACTION_destroyUserStorage = 68;
        static final int TRANSACTION_encryptStorage = 28;
        static final int TRANSACTION_fixateNewestUserKeyAuth = 72;
        static final int TRANSACTION_forgetAllVolumes = 57;
        static final int TRANSACTION_forgetVolume = 56;
        static final int TRANSACTION_format = 50;
        static final int TRANSACTION_fstrim = 73;
        static final int TRANSACTION_getAllocatableBytes = 78;
        static final int TRANSACTION_getCacheQuotaBytes = 76;
        static final int TRANSACTION_getCacheSizeBytes = 77;
        static final int TRANSACTION_getDisks = 45;
        static final int TRANSACTION_getEncryptionState = 32;
        static final int TRANSACTION_getField = 40;
        static final int TRANSACTION_getMountedObbPath = 25;
        static final int TRANSACTION_getPassword = 37;
        static final int TRANSACTION_getPasswordType = 36;
        static final int TRANSACTION_getPrimaryStorageUuid = 58;
        static final int TRANSACTION_getVolumeList = 30;
        static final int TRANSACTION_getVolumeRecords = 47;
        static final int TRANSACTION_getVolumes = 46;
        static final int TRANSACTION_isConvertibleToFBE = 69;
        static final int TRANSACTION_isObbMounted = 24;
        static final int TRANSACTION_isUserKeyUnlocked = 66;
        static final int TRANSACTION_lastMaintenance = 42;
        static final int TRANSACTION_lockUserKey = 65;
        static final int TRANSACTION_mkdirs = 35;
        static final int TRANSACTION_mount = 48;
        static final int TRANSACTION_mountObb = 22;
        static final int TRANSACTION_mountProxyFileDescriptorBridge = 74;
        static final int TRANSACTION_needsCheckpoint = 87;
        static final int TRANSACTION_openProxyFileDescriptor = 75;
        static final int TRANSACTION_partitionMixed = 53;
        static final int TRANSACTION_partitionPrivate = 52;
        static final int TRANSACTION_partitionPublic = 51;
        static final int TRANSACTION_prepareUserStorage = 67;
        static final int TRANSACTION_registerListener = 1;
        static final int TRANSACTION_runIdleMaintenance = 80;
        static final int TRANSACTION_runMaintenance = 43;
        static final int TRANSACTION_setDebugFlags = 61;
        static final int TRANSACTION_setField = 39;
        static final int TRANSACTION_setPrimaryStorageUuid = 59;
        static final int TRANSACTION_setVolumeNickname = 54;
        static final int TRANSACTION_setVolumeUserFlags = 55;
        static final int TRANSACTION_shutdown = 20;
        static final int TRANSACTION_startCheckpoint = 86;
        static final int TRANSACTION_supportsCheckpoint = 85;
        static final int TRANSACTION_unlockUserKey = 64;
        static final int TRANSACTION_unmount = 49;
        static final int TRANSACTION_unmountObb = 23;
        static final int TRANSACTION_unregisterListener = 2;
        static final int TRANSACTION_verifyEncryptionPassword = 33;

        private static class Proxy implements IStorageManager {
            public static IStorageManager sDefaultImpl;
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

            public void registerListener(IStorageEventListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(1, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().registerListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unregisterListener(IStorageEventListener listener) throws RemoteException {
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
                    Stub.getDefaultImpl().unregisterListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void shutdown(IStorageShutdownObserver observer) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(observer != null ? observer.asBinder() : null);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().shutdown(observer);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void mountObb(String rawPath, String canonicalPath, String key, IObbActionListener token, int nonce, ObbInfo obbInfo) throws RemoteException {
                Throwable th;
                String str;
                int i;
                String str2;
                ObbInfo obbInfo2 = obbInfo;
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    try {
                        _data.writeString(rawPath);
                        try {
                            _data.writeString(canonicalPath);
                        } catch (Throwable th2) {
                            th = th2;
                            str = key;
                            i = nonce;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeString(key);
                            _data.writeStrongBinder(token != null ? token.asBinder() : null);
                        } catch (Throwable th3) {
                            th = th3;
                            i = nonce;
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                        try {
                            _data.writeInt(nonce);
                            if (obbInfo2 != null) {
                                _data.writeInt(1);
                                obbInfo2.writeToParcel(_data, 0);
                            } else {
                                _data.writeInt(0);
                            }
                            try {
                                if (this.mRemote.transact(22, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                                    _reply.readException();
                                    _reply.recycle();
                                    _data.recycle();
                                    return;
                                }
                                Stub.getDefaultImpl().mountObb(rawPath, canonicalPath, key, token, nonce, obbInfo);
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
                            _reply.recycle();
                            _data.recycle();
                            throw th;
                        }
                    } catch (Throwable th6) {
                        th = th6;
                        str2 = canonicalPath;
                        str = key;
                        i = nonce;
                        _reply.recycle();
                        _data.recycle();
                        throw th;
                    }
                } catch (Throwable th7) {
                    th = th7;
                    String str3 = rawPath;
                    str2 = canonicalPath;
                    str = key;
                    i = nonce;
                    _reply.recycle();
                    _data.recycle();
                    throw th;
                }
            }

            public void unmountObb(String rawPath, boolean force, IObbActionListener token, int nonce) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rawPath);
                    _data.writeInt(force ? 1 : 0);
                    _data.writeStrongBinder(token != null ? token.asBinder() : null);
                    _data.writeInt(nonce);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unmountObb(rawPath, force, token, nonce);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isObbMounted(String rawPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rawPath);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(24, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isObbMounted(rawPath);
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

            public String getMountedObbPath(String rawPath) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(rawPath);
                    String str = 25;
                    if (!this.mRemote.transact(25, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getMountedObbPath(rawPath);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int decryptStorage(String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    int i = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().decryptStorage(password);
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

            public int encryptStorage(int type, String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(password);
                    int i = 28;
                    if (!this.mRemote.transact(28, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().encryptStorage(type, password);
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

            public int changeEncryptionPassword(int type, String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(type);
                    _data.writeString(password);
                    int i = 29;
                    if (!this.mRemote.transact(29, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().changeEncryptionPassword(type, password);
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

            public StorageVolume[] getVolumeList(int uid, String packageName, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeString(packageName);
                    _data.writeInt(flags);
                    StorageVolume[] storageVolumeArr = 30;
                    if (!this.mRemote.transact(30, _data, _reply, 0)) {
                        storageVolumeArr = Stub.getDefaultImpl();
                        if (storageVolumeArr != 0) {
                            storageVolumeArr = Stub.getDefaultImpl().getVolumeList(uid, packageName, flags);
                            return storageVolumeArr;
                        }
                    }
                    _reply.readException();
                    StorageVolume[] _result = (StorageVolume[]) _reply.createTypedArray(StorageVolume.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getEncryptionState() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 32;
                    if (!this.mRemote.transact(32, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getEncryptionState();
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

            public int verifyEncryptionPassword(String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    int i = 33;
                    if (!this.mRemote.transact(33, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().verifyEncryptionPassword(password);
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

            public void mkdirs(String callingPkg, String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(callingPkg);
                    _data.writeString(path);
                    if (this.mRemote.transact(35, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().mkdirs(callingPkg, path);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int getPasswordType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 36;
                    if (!this.mRemote.transact(36, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().getPasswordType();
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

            public String getPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getPassword();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(38, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().clearPassword();
                    }
                } finally {
                    _data.recycle();
                }
            }

            public void setField(String field, String contents) throws RemoteException {
                Parcel _data = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(field);
                    _data.writeString(contents);
                    if (this.mRemote.transact(39, _data, null, 1) || Stub.getDefaultImpl() == null) {
                        _data.recycle();
                    } else {
                        Stub.getDefaultImpl().setField(field, contents);
                    }
                } finally {
                    _data.recycle();
                }
            }

            public String getField(String field) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(field);
                    String str = 40;
                    if (!this.mRemote.transact(40, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getField(field);
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public long lastMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    long j = 42;
                    if (!this.mRemote.transact(42, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().lastMaintenance();
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

            public void runMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(43, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().runMaintenance();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public DiskInfo[] getDisks() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    DiskInfo[] diskInfoArr = 45;
                    if (!this.mRemote.transact(45, _data, _reply, 0)) {
                        diskInfoArr = Stub.getDefaultImpl();
                        if (diskInfoArr != 0) {
                            diskInfoArr = Stub.getDefaultImpl().getDisks();
                            return diskInfoArr;
                        }
                    }
                    _reply.readException();
                    DiskInfo[] _result = (DiskInfo[]) _reply.createTypedArray(DiskInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public VolumeInfo[] getVolumes(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    VolumeInfo[] volumeInfoArr = 46;
                    if (!this.mRemote.transact(46, _data, _reply, 0)) {
                        volumeInfoArr = Stub.getDefaultImpl();
                        if (volumeInfoArr != 0) {
                            volumeInfoArr = Stub.getDefaultImpl().getVolumes(flags);
                            return volumeInfoArr;
                        }
                    }
                    _reply.readException();
                    VolumeInfo[] _result = (VolumeInfo[]) _reply.createTypedArray(VolumeInfo.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public VolumeRecord[] getVolumeRecords(int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    VolumeRecord[] volumeRecordArr = 47;
                    if (!this.mRemote.transact(47, _data, _reply, 0)) {
                        volumeRecordArr = Stub.getDefaultImpl();
                        if (volumeRecordArr != 0) {
                            volumeRecordArr = Stub.getDefaultImpl().getVolumeRecords(flags);
                            return volumeRecordArr;
                        }
                    }
                    _reply.readException();
                    VolumeRecord[] _result = (VolumeRecord[]) _reply.createTypedArray(VolumeRecord.CREATOR);
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void mount(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().mount(volId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unmount(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    if (this.mRemote.transact(49, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unmount(volId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void format(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    if (this.mRemote.transact(50, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().format(volId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void partitionPublic(String diskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().partitionPublic(diskId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void partitionPrivate(String diskId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    if (this.mRemote.transact(52, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().partitionPrivate(diskId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void partitionMixed(String diskId, int ratio) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    _data.writeInt(ratio);
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().partitionMixed(diskId, ratio);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVolumeNickname(String fsUuid, String nickname) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fsUuid);
                    _data.writeString(nickname);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVolumeNickname(fsUuid, nickname);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setVolumeUserFlags(String fsUuid, int flags, int mask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fsUuid);
                    _data.writeInt(flags);
                    _data.writeInt(mask);
                    if (this.mRemote.transact(55, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setVolumeUserFlags(fsUuid, flags, mask);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forgetVolume(String fsUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fsUuid);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forgetVolume(fsUuid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forgetAllVolumes() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forgetAllVolumes();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String getPrimaryStorageUuid() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 58;
                    if (!this.mRemote.transact(58, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().getPrimaryStorageUuid();
                            return str;
                        }
                    }
                    _reply.readException();
                    str = _reply.readString();
                    String _result = str;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setPrimaryStorageUuid(String volumeUuid, IPackageMoveObserver callback) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeStrongBinder(callback != null ? callback.asBinder() : null);
                    if (this.mRemote.transact(59, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setPrimaryStorageUuid(volumeUuid, callback);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void benchmark(String volId, IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().benchmark(volId, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void setDebugFlags(int flags, int mask) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeInt(mask);
                    if (this.mRemote.transact(61, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().setDebugFlags(flags, mask);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void createUserKey(int userId, int serialNumber, boolean ephemeral) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeInt(ephemeral ? 1 : 0);
                    if (this.mRemote.transact(62, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().createUserKey(userId, serialNumber, ephemeral);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void destroyUserKey(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(63, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroyUserKey(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unlockUserKey(int userId, int serialNumber, byte[] token, byte[] secret) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeByteArray(token);
                    _data.writeByteArray(secret);
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unlockUserKey(userId, serialNumber, token, secret);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void lockUserKey(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(65, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().lockUserKey(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isUserKeyUnlocked(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(66, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isUserKeyUnlocked(userId);
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

            public void prepareUserStorage(String volumeUuid, int userId, int serialNumber, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(67, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepareUserStorage(volumeUuid, userId, serialNumber, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void destroyUserStorage(String volumeUuid, int userId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(userId);
                    _data.writeInt(flags);
                    if (this.mRemote.transact(68, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroyUserStorage(volumeUuid, userId, flags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isConvertibleToFBE() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(69, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isConvertibleToFBE();
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

            public void addUserKeyAuth(int userId, int serialNumber, byte[] token, byte[] secret) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeByteArray(token);
                    _data.writeByteArray(secret);
                    if (this.mRemote.transact(71, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addUserKeyAuth(userId, serialNumber, token, secret);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void fixateNewestUserKeyAuth(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(72, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fixateNewestUserKeyAuth(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void fstrim(int flags, IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(flags);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(73, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fstrim(flags, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public AppFuseMount mountProxyFileDescriptorBridge() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    AppFuseMount appFuseMount = 74;
                    if (!this.mRemote.transact(74, _data, _reply, 0)) {
                        appFuseMount = Stub.getDefaultImpl();
                        if (appFuseMount != 0) {
                            appFuseMount = Stub.getDefaultImpl().mountProxyFileDescriptorBridge();
                            return appFuseMount;
                        }
                    }
                    _reply.readException();
                    if (_reply.readInt() != 0) {
                        appFuseMount = (AppFuseMount) AppFuseMount.CREATOR.createFromParcel(_reply);
                    } else {
                        appFuseMount = null;
                    }
                    _reply.recycle();
                    _data.recycle();
                    return appFuseMount;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public ParcelFileDescriptor openProxyFileDescriptor(int mountPointId, int fileId, int mode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(mountPointId);
                    _data.writeInt(fileId);
                    _data.writeInt(mode);
                    ParcelFileDescriptor parcelFileDescriptor = 75;
                    if (!this.mRemote.transact(75, _data, _reply, 0)) {
                        parcelFileDescriptor = Stub.getDefaultImpl();
                        if (parcelFileDescriptor != 0) {
                            parcelFileDescriptor = Stub.getDefaultImpl().openProxyFileDescriptor(mountPointId, fileId, mode);
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

            public long getCacheQuotaBytes(String volumeUuid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(uid);
                    long j = 76;
                    if (!this.mRemote.transact(76, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getCacheQuotaBytes(volumeUuid, uid);
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

            public long getCacheSizeBytes(String volumeUuid, int uid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(uid);
                    long j = 77;
                    if (!this.mRemote.transact(77, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getCacheSizeBytes(volumeUuid, uid);
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

            public long getAllocatableBytes(String volumeUuid, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    long j = 78;
                    if (!this.mRemote.transact(78, _data, _reply, 0)) {
                        j = Stub.getDefaultImpl();
                        if (j != 0) {
                            j = Stub.getDefaultImpl().getAllocatableBytes(volumeUuid, flags, callingPackage);
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

            public void allocateBytes(String volumeUuid, long bytes, int flags, String callingPackage) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volumeUuid);
                    _data.writeLong(bytes);
                    _data.writeInt(flags);
                    _data.writeString(callingPackage);
                    if (this.mRemote.transact(79, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().allocateBytes(volumeUuid, bytes, flags, callingPackage);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void runIdleMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(80, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().runIdleMaintenance();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void abortIdleMaintenance() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(81, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().abortIdleMaintenance();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void commitChanges() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(84, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().commitChanges();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean supportsCheckpoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(85, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().supportsCheckpoint();
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

            public void startCheckpoint(int numTries) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(numTries);
                    if (this.mRemote.transact(86, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startCheckpoint(numTries);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean needsCheckpoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(87, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().needsCheckpoint();
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

            public void abortChanges(String message, boolean retry) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(message);
                    _data.writeInt(retry ? 1 : 0);
                    if (this.mRemote.transact(88, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().abortChanges(message, retry);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearUserKeyAuth(int userId, int serialNumber, byte[] token, byte[] secret) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(serialNumber);
                    _data.writeByteArray(token);
                    _data.writeByteArray(secret);
                    if (this.mRemote.transact(89, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearUserKeyAuth(userId, serialNumber, token, secret);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static IStorageManager asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IStorageManager)) {
                return new Proxy(obj);
            }
            return (IStorageManager) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            if (transactionCode == 1) {
                return "registerListener";
            }
            if (transactionCode == 2) {
                return "unregisterListener";
            }
            if (transactionCode == 20) {
                return "shutdown";
            }
            if (transactionCode == 32) {
                return "getEncryptionState";
            }
            if (transactionCode == 33) {
                return "verifyEncryptionPassword";
            }
            if (transactionCode == 42) {
                return "lastMaintenance";
            }
            if (transactionCode == 43) {
                return "runMaintenance";
            }
            switch (transactionCode) {
                case 22:
                    return "mountObb";
                case 23:
                    return "unmountObb";
                case 24:
                    return "isObbMounted";
                case 25:
                    return "getMountedObbPath";
                default:
                    switch (transactionCode) {
                        case 27:
                            return "decryptStorage";
                        case 28:
                            return "encryptStorage";
                        case 29:
                            return "changeEncryptionPassword";
                        case 30:
                            return "getVolumeList";
                        default:
                            switch (transactionCode) {
                                case 35:
                                    return "mkdirs";
                                case 36:
                                    return "getPasswordType";
                                case 37:
                                    return "getPassword";
                                case 38:
                                    return "clearPassword";
                                case 39:
                                    return "setField";
                                case 40:
                                    return "getField";
                                default:
                                    switch (transactionCode) {
                                        case 45:
                                            return "getDisks";
                                        case 46:
                                            return "getVolumes";
                                        case 47:
                                            return "getVolumeRecords";
                                        case 48:
                                            return "mount";
                                        case 49:
                                            return "unmount";
                                        case 50:
                                            return "format";
                                        case 51:
                                            return "partitionPublic";
                                        case 52:
                                            return "partitionPrivate";
                                        case 53:
                                            return "partitionMixed";
                                        case 54:
                                            return "setVolumeNickname";
                                        case 55:
                                            return "setVolumeUserFlags";
                                        case 56:
                                            return "forgetVolume";
                                        case 57:
                                            return "forgetAllVolumes";
                                        case 58:
                                            return "getPrimaryStorageUuid";
                                        case 59:
                                            return "setPrimaryStorageUuid";
                                        case 60:
                                            return "benchmark";
                                        case 61:
                                            return "setDebugFlags";
                                        case 62:
                                            return "createUserKey";
                                        case 63:
                                            return "destroyUserKey";
                                        case 64:
                                            return "unlockUserKey";
                                        case 65:
                                            return "lockUserKey";
                                        case 66:
                                            return "isUserKeyUnlocked";
                                        case 67:
                                            return "prepareUserStorage";
                                        case 68:
                                            return "destroyUserStorage";
                                        case 69:
                                            return "isConvertibleToFBE";
                                        default:
                                            switch (transactionCode) {
                                                case 71:
                                                    return "addUserKeyAuth";
                                                case 72:
                                                    return "fixateNewestUserKeyAuth";
                                                case 73:
                                                    return "fstrim";
                                                case 74:
                                                    return "mountProxyFileDescriptorBridge";
                                                case 75:
                                                    return "openProxyFileDescriptor";
                                                case 76:
                                                    return "getCacheQuotaBytes";
                                                case 77:
                                                    return "getCacheSizeBytes";
                                                case 78:
                                                    return "getAllocatableBytes";
                                                case 79:
                                                    return "allocateBytes";
                                                case 80:
                                                    return "runIdleMaintenance";
                                                case 81:
                                                    return "abortIdleMaintenance";
                                                default:
                                                    switch (transactionCode) {
                                                        case 84:
                                                            return "commitChanges";
                                                        case 85:
                                                            return "supportsCheckpoint";
                                                        case 86:
                                                            return "startCheckpoint";
                                                        case 87:
                                                            return "needsCheckpoint";
                                                        case 88:
                                                            return "abortChanges";
                                                        case 89:
                                                            return "clearUserKeyAuth";
                                                        default:
                                                            return null;
                                                    }
                                            }
                                    }
                            }
                    }
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
            int _result;
            int _result2;
            if (i == 1) {
                parcel.enforceInterface(descriptor);
                registerListener(android.os.storage.IStorageEventListener.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (i == 2) {
                parcel.enforceInterface(descriptor);
                unregisterListener(android.os.storage.IStorageEventListener.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (i == 20) {
                parcel.enforceInterface(descriptor);
                shutdown(android.os.storage.IStorageShutdownObserver.Stub.asInterface(data.readStrongBinder()));
                reply.writeNoException();
                return true;
            } else if (i == 1598968902) {
                parcel2.writeString(descriptor);
                return true;
            } else if (i == 32) {
                parcel.enforceInterface(descriptor);
                _result = getEncryptionState();
                reply.writeNoException();
                parcel2.writeInt(_result);
                return true;
            } else if (i == 33) {
                parcel.enforceInterface(descriptor);
                _result2 = verifyEncryptionPassword(data.readString());
                reply.writeNoException();
                parcel2.writeInt(_result2);
                return true;
            } else if (i == 42) {
                parcel.enforceInterface(descriptor);
                long _result3 = lastMaintenance();
                reply.writeNoException();
                parcel2.writeLong(_result3);
                return true;
            } else if (i != 43) {
                boolean _arg2 = false;
                String _arg0;
                boolean _result4;
                switch (i) {
                    case 22:
                        ObbInfo _arg5;
                        parcel.enforceInterface(descriptor);
                        String _arg02 = data.readString();
                        String _arg1 = data.readString();
                        String _arg22 = data.readString();
                        IObbActionListener _arg3 = android.os.storage.IObbActionListener.Stub.asInterface(data.readStrongBinder());
                        int _arg4 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg5 = (ObbInfo) ObbInfo.CREATOR.createFromParcel(parcel);
                        } else {
                            _arg5 = null;
                        }
                        mountObb(_arg02, _arg1, _arg22, _arg3, _arg4, _arg5);
                        reply.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        _arg0 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        unmountObb(_arg0, _arg2, android.os.storage.IObbActionListener.Stub.asInterface(data.readStrongBinder()), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        _result4 = isObbMounted(data.readString());
                        reply.writeNoException();
                        parcel2.writeInt(_result4);
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        _arg0 = getMountedObbPath(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_arg0);
                        return true;
                    default:
                        int _result5;
                        switch (i) {
                            case 27:
                                parcel.enforceInterface(descriptor);
                                _result2 = decryptStorage(data.readString());
                                reply.writeNoException();
                                parcel2.writeInt(_result2);
                                return true;
                            case 28:
                                parcel.enforceInterface(descriptor);
                                _result5 = encryptStorage(data.readInt(), data.readString());
                                reply.writeNoException();
                                parcel2.writeInt(_result5);
                                return true;
                            case 29:
                                parcel.enforceInterface(descriptor);
                                _result5 = changeEncryptionPassword(data.readInt(), data.readString());
                                reply.writeNoException();
                                parcel2.writeInt(_result5);
                                return true;
                            case 30:
                                parcel.enforceInterface(descriptor);
                                StorageVolume[] _result6 = getVolumeList(data.readInt(), data.readString(), data.readInt());
                                reply.writeNoException();
                                parcel2.writeTypedArray(_result6, 1);
                                return true;
                            default:
                                String _result7;
                                switch (i) {
                                    case 35:
                                        parcel.enforceInterface(descriptor);
                                        mkdirs(data.readString(), data.readString());
                                        reply.writeNoException();
                                        return true;
                                    case 36:
                                        parcel.enforceInterface(descriptor);
                                        _result = getPasswordType();
                                        reply.writeNoException();
                                        parcel2.writeInt(_result);
                                        return true;
                                    case 37:
                                        parcel.enforceInterface(descriptor);
                                        _result7 = getPassword();
                                        reply.writeNoException();
                                        parcel2.writeString(_result7);
                                        return true;
                                    case 38:
                                        parcel.enforceInterface(descriptor);
                                        clearPassword();
                                        return true;
                                    case 39:
                                        parcel.enforceInterface(descriptor);
                                        setField(data.readString(), data.readString());
                                        return true;
                                    case 40:
                                        parcel.enforceInterface(descriptor);
                                        _arg0 = getField(data.readString());
                                        reply.writeNoException();
                                        parcel2.writeString(_arg0);
                                        return true;
                                    default:
                                        switch (i) {
                                            case 45:
                                                parcel.enforceInterface(descriptor);
                                                DiskInfo[] _result8 = getDisks();
                                                reply.writeNoException();
                                                parcel2.writeTypedArray(_result8, 1);
                                                return true;
                                            case 46:
                                                parcel.enforceInterface(descriptor);
                                                VolumeInfo[] _result9 = getVolumes(data.readInt());
                                                reply.writeNoException();
                                                parcel2.writeTypedArray(_result9, 1);
                                                return true;
                                            case 47:
                                                parcel.enforceInterface(descriptor);
                                                VolumeRecord[] _result10 = getVolumeRecords(data.readInt());
                                                reply.writeNoException();
                                                parcel2.writeTypedArray(_result10, 1);
                                                return true;
                                            case 48:
                                                parcel.enforceInterface(descriptor);
                                                mount(data.readString());
                                                reply.writeNoException();
                                                return true;
                                            case 49:
                                                parcel.enforceInterface(descriptor);
                                                unmount(data.readString());
                                                reply.writeNoException();
                                                return true;
                                            case 50:
                                                parcel.enforceInterface(descriptor);
                                                format(data.readString());
                                                reply.writeNoException();
                                                return true;
                                            case 51:
                                                parcel.enforceInterface(descriptor);
                                                partitionPublic(data.readString());
                                                reply.writeNoException();
                                                return true;
                                            case 52:
                                                parcel.enforceInterface(descriptor);
                                                partitionPrivate(data.readString());
                                                reply.writeNoException();
                                                return true;
                                            case 53:
                                                parcel.enforceInterface(descriptor);
                                                partitionMixed(data.readString(), data.readInt());
                                                reply.writeNoException();
                                                return true;
                                            case 54:
                                                parcel.enforceInterface(descriptor);
                                                setVolumeNickname(data.readString(), data.readString());
                                                reply.writeNoException();
                                                return true;
                                            case 55:
                                                parcel.enforceInterface(descriptor);
                                                setVolumeUserFlags(data.readString(), data.readInt(), data.readInt());
                                                reply.writeNoException();
                                                return true;
                                            case 56:
                                                parcel.enforceInterface(descriptor);
                                                forgetVolume(data.readString());
                                                reply.writeNoException();
                                                return true;
                                            case 57:
                                                parcel.enforceInterface(descriptor);
                                                forgetAllVolumes();
                                                reply.writeNoException();
                                                return true;
                                            case 58:
                                                parcel.enforceInterface(descriptor);
                                                _result7 = getPrimaryStorageUuid();
                                                reply.writeNoException();
                                                parcel2.writeString(_result7);
                                                return true;
                                            case 59:
                                                parcel.enforceInterface(descriptor);
                                                setPrimaryStorageUuid(data.readString(), android.content.pm.IPackageMoveObserver.Stub.asInterface(data.readStrongBinder()));
                                                reply.writeNoException();
                                                return true;
                                            case 60:
                                                parcel.enforceInterface(descriptor);
                                                benchmark(data.readString(), android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder()));
                                                reply.writeNoException();
                                                return true;
                                            case 61:
                                                parcel.enforceInterface(descriptor);
                                                setDebugFlags(data.readInt(), data.readInt());
                                                reply.writeNoException();
                                                return true;
                                            case 62:
                                                parcel.enforceInterface(descriptor);
                                                _result2 = data.readInt();
                                                _result5 = data.readInt();
                                                if (data.readInt() != 0) {
                                                    _arg2 = true;
                                                }
                                                createUserKey(_result2, _result5, _arg2);
                                                reply.writeNoException();
                                                return true;
                                            case 63:
                                                parcel.enforceInterface(descriptor);
                                                destroyUserKey(data.readInt());
                                                reply.writeNoException();
                                                return true;
                                            case 64:
                                                parcel.enforceInterface(descriptor);
                                                unlockUserKey(data.readInt(), data.readInt(), data.createByteArray(), data.createByteArray());
                                                reply.writeNoException();
                                                return true;
                                            case 65:
                                                parcel.enforceInterface(descriptor);
                                                lockUserKey(data.readInt());
                                                reply.writeNoException();
                                                return true;
                                            case 66:
                                                parcel.enforceInterface(descriptor);
                                                _result4 = isUserKeyUnlocked(data.readInt());
                                                reply.writeNoException();
                                                parcel2.writeInt(_result4);
                                                return true;
                                            case 67:
                                                parcel.enforceInterface(descriptor);
                                                prepareUserStorage(data.readString(), data.readInt(), data.readInt(), data.readInt());
                                                reply.writeNoException();
                                                return true;
                                            case 68:
                                                parcel.enforceInterface(descriptor);
                                                destroyUserStorage(data.readString(), data.readInt(), data.readInt());
                                                reply.writeNoException();
                                                return true;
                                            case 69:
                                                parcel.enforceInterface(descriptor);
                                                _arg2 = isConvertibleToFBE();
                                                reply.writeNoException();
                                                parcel2.writeInt(_arg2);
                                                return true;
                                            default:
                                                long _result11;
                                                switch (i) {
                                                    case 71:
                                                        parcel.enforceInterface(descriptor);
                                                        addUserKeyAuth(data.readInt(), data.readInt(), data.createByteArray(), data.createByteArray());
                                                        reply.writeNoException();
                                                        return true;
                                                    case 72:
                                                        parcel.enforceInterface(descriptor);
                                                        fixateNewestUserKeyAuth(data.readInt());
                                                        reply.writeNoException();
                                                        return true;
                                                    case 73:
                                                        parcel.enforceInterface(descriptor);
                                                        fstrim(data.readInt(), android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder()));
                                                        reply.writeNoException();
                                                        return true;
                                                    case 74:
                                                        parcel.enforceInterface(descriptor);
                                                        AppFuseMount _result12 = mountProxyFileDescriptorBridge();
                                                        reply.writeNoException();
                                                        if (_result12 != null) {
                                                            parcel2.writeInt(1);
                                                            _result12.writeToParcel(parcel2, 1);
                                                        } else {
                                                            parcel2.writeInt(0);
                                                        }
                                                        return true;
                                                    case 75:
                                                        parcel.enforceInterface(descriptor);
                                                        ParcelFileDescriptor _result13 = openProxyFileDescriptor(data.readInt(), data.readInt(), data.readInt());
                                                        reply.writeNoException();
                                                        if (_result13 != null) {
                                                            parcel2.writeInt(1);
                                                            _result13.writeToParcel(parcel2, 1);
                                                        } else {
                                                            parcel2.writeInt(0);
                                                        }
                                                        return true;
                                                    case 76:
                                                        parcel.enforceInterface(descriptor);
                                                        _result11 = getCacheQuotaBytes(data.readString(), data.readInt());
                                                        reply.writeNoException();
                                                        parcel2.writeLong(_result11);
                                                        return true;
                                                    case 77:
                                                        parcel.enforceInterface(descriptor);
                                                        _result11 = getCacheSizeBytes(data.readString(), data.readInt());
                                                        reply.writeNoException();
                                                        parcel2.writeLong(_result11);
                                                        return true;
                                                    case 78:
                                                        parcel.enforceInterface(descriptor);
                                                        long _result14 = getAllocatableBytes(data.readString(), data.readInt(), data.readString());
                                                        reply.writeNoException();
                                                        parcel2.writeLong(_result14);
                                                        return true;
                                                    case 79:
                                                        parcel.enforceInterface(descriptor);
                                                        allocateBytes(data.readString(), data.readLong(), data.readInt(), data.readString());
                                                        reply.writeNoException();
                                                        return true;
                                                    case 80:
                                                        parcel.enforceInterface(descriptor);
                                                        runIdleMaintenance();
                                                        reply.writeNoException();
                                                        return true;
                                                    case 81:
                                                        parcel.enforceInterface(descriptor);
                                                        abortIdleMaintenance();
                                                        reply.writeNoException();
                                                        return true;
                                                    default:
                                                        switch (i) {
                                                            case 84:
                                                                parcel.enforceInterface(descriptor);
                                                                commitChanges();
                                                                reply.writeNoException();
                                                                return true;
                                                            case 85:
                                                                parcel.enforceInterface(descriptor);
                                                                _arg2 = supportsCheckpoint();
                                                                reply.writeNoException();
                                                                parcel2.writeInt(_arg2);
                                                                return true;
                                                            case 86:
                                                                parcel.enforceInterface(descriptor);
                                                                startCheckpoint(data.readInt());
                                                                reply.writeNoException();
                                                                return true;
                                                            case 87:
                                                                parcel.enforceInterface(descriptor);
                                                                _arg2 = needsCheckpoint();
                                                                reply.writeNoException();
                                                                parcel2.writeInt(_arg2);
                                                                return true;
                                                            case 88:
                                                                parcel.enforceInterface(descriptor);
                                                                _arg0 = data.readString();
                                                                if (data.readInt() != 0) {
                                                                    _arg2 = true;
                                                                }
                                                                abortChanges(_arg0, _arg2);
                                                                reply.writeNoException();
                                                                return true;
                                                            case 89:
                                                                parcel.enforceInterface(descriptor);
                                                                clearUserKeyAuth(data.readInt(), data.readInt(), data.createByteArray(), data.createByteArray());
                                                                reply.writeNoException();
                                                                return true;
                                                            default:
                                                                return super.onTransact(code, data, reply, flags);
                                                        }
                                                }
                                        }
                                }
                        }
                }
            } else {
                parcel.enforceInterface(descriptor);
                runMaintenance();
                reply.writeNoException();
                return true;
            }
        }

        public static boolean setDefaultImpl(IStorageManager impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IStorageManager getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void abortChanges(String str, boolean z) throws RemoteException;

    void abortIdleMaintenance() throws RemoteException;

    void addUserKeyAuth(int i, int i2, byte[] bArr, byte[] bArr2) throws RemoteException;

    void allocateBytes(String str, long j, int i, String str2) throws RemoteException;

    void benchmark(String str, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    int changeEncryptionPassword(int i, String str) throws RemoteException;

    void clearPassword() throws RemoteException;

    void clearUserKeyAuth(int i, int i2, byte[] bArr, byte[] bArr2) throws RemoteException;

    void commitChanges() throws RemoteException;

    void createUserKey(int i, int i2, boolean z) throws RemoteException;

    int decryptStorage(String str) throws RemoteException;

    void destroyUserKey(int i) throws RemoteException;

    void destroyUserStorage(String str, int i, int i2) throws RemoteException;

    int encryptStorage(int i, String str) throws RemoteException;

    void fixateNewestUserKeyAuth(int i) throws RemoteException;

    void forgetAllVolumes() throws RemoteException;

    void forgetVolume(String str) throws RemoteException;

    void format(String str) throws RemoteException;

    void fstrim(int i, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    long getAllocatableBytes(String str, int i, String str2) throws RemoteException;

    long getCacheQuotaBytes(String str, int i) throws RemoteException;

    long getCacheSizeBytes(String str, int i) throws RemoteException;

    DiskInfo[] getDisks() throws RemoteException;

    int getEncryptionState() throws RemoteException;

    String getField(String str) throws RemoteException;

    String getMountedObbPath(String str) throws RemoteException;

    String getPassword() throws RemoteException;

    int getPasswordType() throws RemoteException;

    String getPrimaryStorageUuid() throws RemoteException;

    StorageVolume[] getVolumeList(int i, String str, int i2) throws RemoteException;

    VolumeRecord[] getVolumeRecords(int i) throws RemoteException;

    VolumeInfo[] getVolumes(int i) throws RemoteException;

    boolean isConvertibleToFBE() throws RemoteException;

    boolean isObbMounted(String str) throws RemoteException;

    boolean isUserKeyUnlocked(int i) throws RemoteException;

    long lastMaintenance() throws RemoteException;

    void lockUserKey(int i) throws RemoteException;

    void mkdirs(String str, String str2) throws RemoteException;

    void mount(String str) throws RemoteException;

    void mountObb(String str, String str2, String str3, IObbActionListener iObbActionListener, int i, ObbInfo obbInfo) throws RemoteException;

    AppFuseMount mountProxyFileDescriptorBridge() throws RemoteException;

    boolean needsCheckpoint() throws RemoteException;

    ParcelFileDescriptor openProxyFileDescriptor(int i, int i2, int i3) throws RemoteException;

    void partitionMixed(String str, int i) throws RemoteException;

    void partitionPrivate(String str) throws RemoteException;

    void partitionPublic(String str) throws RemoteException;

    void prepareUserStorage(String str, int i, int i2, int i3) throws RemoteException;

    void registerListener(IStorageEventListener iStorageEventListener) throws RemoteException;

    void runIdleMaintenance() throws RemoteException;

    void runMaintenance() throws RemoteException;

    void setDebugFlags(int i, int i2) throws RemoteException;

    void setField(String str, String str2) throws RemoteException;

    void setPrimaryStorageUuid(String str, IPackageMoveObserver iPackageMoveObserver) throws RemoteException;

    void setVolumeNickname(String str, String str2) throws RemoteException;

    void setVolumeUserFlags(String str, int i, int i2) throws RemoteException;

    void shutdown(IStorageShutdownObserver iStorageShutdownObserver) throws RemoteException;

    void startCheckpoint(int i) throws RemoteException;

    boolean supportsCheckpoint() throws RemoteException;

    void unlockUserKey(int i, int i2, byte[] bArr, byte[] bArr2) throws RemoteException;

    void unmount(String str) throws RemoteException;

    void unmountObb(String str, boolean z, IObbActionListener iObbActionListener, int i) throws RemoteException;

    void unregisterListener(IStorageEventListener iStorageEventListener) throws RemoteException;

    int verifyEncryptionPassword(String str) throws RemoteException;
}
