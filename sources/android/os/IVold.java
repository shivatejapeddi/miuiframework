package android.os;

import java.io.FileDescriptor;

public interface IVold extends IInterface {
    public static final int ENCRYPTION_FLAG_NO_UI = 4;
    public static final int ENCRYPTION_STATE_ERROR_CORRUPT = -4;
    public static final int ENCRYPTION_STATE_ERROR_INCOMPLETE = -2;
    public static final int ENCRYPTION_STATE_ERROR_INCONSISTENT = -3;
    public static final int ENCRYPTION_STATE_ERROR_UNKNOWN = -1;
    public static final int ENCRYPTION_STATE_NONE = 1;
    public static final int ENCRYPTION_STATE_OK = 0;
    public static final int FSTRIM_FLAG_DEEP_TRIM = 1;
    public static final int MOUNT_FLAG_PRIMARY = 1;
    public static final int MOUNT_FLAG_VISIBLE = 2;
    public static final int PARTITION_TYPE_MIXED = 2;
    public static final int PARTITION_TYPE_PRIVATE = 1;
    public static final int PARTITION_TYPE_PUBLIC = 0;
    public static final int PASSWORD_TYPE_DEFAULT = 1;
    public static final int PASSWORD_TYPE_PASSWORD = 0;
    public static final int PASSWORD_TYPE_PATTERN = 2;
    public static final int PASSWORD_TYPE_PIN = 3;
    public static final int REMOUNT_MODE_DEFAULT = 1;
    public static final int REMOUNT_MODE_FULL = 6;
    public static final int REMOUNT_MODE_INSTALLER = 5;
    public static final int REMOUNT_MODE_LEGACY = 4;
    public static final int REMOUNT_MODE_NONE = 0;
    public static final int REMOUNT_MODE_READ = 2;
    public static final int REMOUNT_MODE_WRITE = 3;
    public static final int STORAGE_FLAG_CE = 2;
    public static final int STORAGE_FLAG_DE = 1;
    public static final int VOLUME_STATE_BAD_REMOVAL = 8;
    public static final int VOLUME_STATE_CHECKING = 1;
    public static final int VOLUME_STATE_EJECTING = 5;
    public static final int VOLUME_STATE_FORMATTING = 4;
    public static final int VOLUME_STATE_MOUNTED = 2;
    public static final int VOLUME_STATE_MOUNTED_READ_ONLY = 3;
    public static final int VOLUME_STATE_REMOVED = 7;
    public static final int VOLUME_STATE_UNMOUNTABLE = 6;
    public static final int VOLUME_STATE_UNMOUNTED = 0;
    public static final int VOLUME_TYPE_ASEC = 3;
    public static final int VOLUME_TYPE_EMULATED = 2;
    public static final int VOLUME_TYPE_OBB = 4;
    public static final int VOLUME_TYPE_PRIVATE = 1;
    public static final int VOLUME_TYPE_PUBLIC = 0;
    public static final int VOLUME_TYPE_STUB = 5;

    public static class Default implements IVold {
        public void setListener(IVoldListener listener) throws RemoteException {
        }

        public void monitor() throws RemoteException {
        }

        public void reset() throws RemoteException {
        }

        public void shutdown() throws RemoteException {
        }

        public void onUserAdded(int userId, int userSerial) throws RemoteException {
        }

        public void onUserRemoved(int userId) throws RemoteException {
        }

        public void onUserStarted(int userId) throws RemoteException {
        }

        public void onUserStopped(int userId) throws RemoteException {
        }

        public void addAppIds(String[] packageNames, int[] appIds) throws RemoteException {
        }

        public void addSandboxIds(int[] appIds, String[] sandboxIds) throws RemoteException {
        }

        public void onSecureKeyguardStateChanged(boolean isShowing) throws RemoteException {
        }

        public void partition(String diskId, int partitionType, int ratio) throws RemoteException {
        }

        public void forgetPartition(String partGuid, String fsUuid) throws RemoteException {
        }

        public void mount(String volId, int mountFlags, int mountUserId) throws RemoteException {
        }

        public void unmount(String volId) throws RemoteException {
        }

        public void format(String volId, String fsType) throws RemoteException {
        }

        public void benchmark(String volId, IVoldTaskListener listener) throws RemoteException {
        }

        public void checkEncryption(String volId) throws RemoteException {
        }

        public void moveStorage(String fromVolId, String toVolId, IVoldTaskListener listener) throws RemoteException {
        }

        public void remountUid(int uid, int remountMode) throws RemoteException {
        }

        public void mkdirs(String path) throws RemoteException {
        }

        public String createObb(String sourcePath, String sourceKey, int ownerGid) throws RemoteException {
            return null;
        }

        public void destroyObb(String volId) throws RemoteException {
        }

        public void fstrim(int fstrimFlags, IVoldTaskListener listener) throws RemoteException {
        }

        public void runIdleMaint(IVoldTaskListener listener) throws RemoteException {
        }

        public void abortIdleMaint(IVoldTaskListener listener) throws RemoteException {
        }

        public FileDescriptor mountAppFuse(int uid, int mountId) throws RemoteException {
            return null;
        }

        public void unmountAppFuse(int uid, int mountId) throws RemoteException {
        }

        public void fdeCheckPassword(String password) throws RemoteException {
        }

        public void fdeRestart() throws RemoteException {
        }

        public int fdeComplete() throws RemoteException {
            return 0;
        }

        public void fdeEnable(int passwordType, String password, int encryptionFlags) throws RemoteException {
        }

        public void fdeChangePassword(int passwordType, String currentPassword, String password) throws RemoteException {
        }

        public void fdeVerifyPassword(String password) throws RemoteException {
        }

        public String fdeGetField(String key) throws RemoteException {
            return null;
        }

        public void fdeSetField(String key, String value) throws RemoteException {
        }

        public int fdeGetPasswordType() throws RemoteException {
            return 0;
        }

        public String fdeGetPassword() throws RemoteException {
            return null;
        }

        public void fdeClearPassword() throws RemoteException {
        }

        public void fbeEnable() throws RemoteException {
        }

        public void mountDefaultEncrypted() throws RemoteException {
        }

        public void initUser0() throws RemoteException {
        }

        public boolean isConvertibleToFbe() throws RemoteException {
            return false;
        }

        public void mountFstab(String blkDevice, String mountPoint) throws RemoteException {
        }

        public void encryptFstab(String blkDevice, String mountPoint) throws RemoteException {
        }

        public void createUserKey(int userId, int userSerial, boolean ephemeral) throws RemoteException {
        }

        public void destroyUserKey(int userId) throws RemoteException {
        }

        public void addUserKeyAuth(int userId, int userSerial, String token, String secret) throws RemoteException {
        }

        public void clearUserKeyAuth(int userId, int userSerial, String token, String secret) throws RemoteException {
        }

        public void fixateNewestUserKeyAuth(int userId) throws RemoteException {
        }

        public void unlockUserKey(int userId, int userSerial, String token, String secret) throws RemoteException {
        }

        public void lockUserKey(int userId) throws RemoteException {
        }

        public void prepareUserStorage(String uuid, int userId, int userSerial, int storageFlags) throws RemoteException {
        }

        public void destroyUserStorage(String uuid, int userId, int storageFlags) throws RemoteException {
        }

        public void prepareSandboxForApp(String packageName, int appId, String sandboxId, int userId) throws RemoteException {
        }

        public void destroySandboxForApp(String packageName, String sandboxId, int userId) throws RemoteException {
        }

        public void startCheckpoint(int retry) throws RemoteException {
        }

        public boolean needsCheckpoint() throws RemoteException {
            return false;
        }

        public boolean needsRollback() throws RemoteException {
            return false;
        }

        public void abortChanges(String device, boolean retry) throws RemoteException {
        }

        public void commitChanges() throws RemoteException {
        }

        public void prepareCheckpoint() throws RemoteException {
        }

        public void restoreCheckpoint(String device) throws RemoteException {
        }

        public void restoreCheckpointPart(String device, int count) throws RemoteException {
        }

        public void markBootAttempt() throws RemoteException {
        }

        public boolean supportsCheckpoint() throws RemoteException {
            return false;
        }

        public boolean supportsBlockCheckpoint() throws RemoteException {
            return false;
        }

        public boolean supportsFileCheckpoint() throws RemoteException {
            return false;
        }

        public String createStubVolume(String sourcePath, String mountPath, String fsType, String fsUuid, String fsLabel) throws RemoteException {
            return null;
        }

        public void destroyStubVolume(String volId) throws RemoteException {
        }

        public FileDescriptor openAppFuseFile(int uid, int mountId, int fileId, int flags) throws RemoteException {
            return null;
        }

        public IBinder asBinder() {
            return null;
        }
    }

    public static abstract class Stub extends Binder implements IVold {
        private static final String DESCRIPTOR = "android.os.IVold";
        static final int TRANSACTION_abortChanges = 60;
        static final int TRANSACTION_abortIdleMaint = 26;
        static final int TRANSACTION_addAppIds = 9;
        static final int TRANSACTION_addSandboxIds = 10;
        static final int TRANSACTION_addUserKeyAuth = 48;
        static final int TRANSACTION_benchmark = 17;
        static final int TRANSACTION_checkEncryption = 18;
        static final int TRANSACTION_clearUserKeyAuth = 49;
        static final int TRANSACTION_commitChanges = 61;
        static final int TRANSACTION_createObb = 22;
        static final int TRANSACTION_createStubVolume = 69;
        static final int TRANSACTION_createUserKey = 46;
        static final int TRANSACTION_destroyObb = 23;
        static final int TRANSACTION_destroySandboxForApp = 56;
        static final int TRANSACTION_destroyStubVolume = 70;
        static final int TRANSACTION_destroyUserKey = 47;
        static final int TRANSACTION_destroyUserStorage = 54;
        static final int TRANSACTION_encryptFstab = 45;
        static final int TRANSACTION_fbeEnable = 40;
        static final int TRANSACTION_fdeChangePassword = 33;
        static final int TRANSACTION_fdeCheckPassword = 29;
        static final int TRANSACTION_fdeClearPassword = 39;
        static final int TRANSACTION_fdeComplete = 31;
        static final int TRANSACTION_fdeEnable = 32;
        static final int TRANSACTION_fdeGetField = 35;
        static final int TRANSACTION_fdeGetPassword = 38;
        static final int TRANSACTION_fdeGetPasswordType = 37;
        static final int TRANSACTION_fdeRestart = 30;
        static final int TRANSACTION_fdeSetField = 36;
        static final int TRANSACTION_fdeVerifyPassword = 34;
        static final int TRANSACTION_fixateNewestUserKeyAuth = 50;
        static final int TRANSACTION_forgetPartition = 13;
        static final int TRANSACTION_format = 16;
        static final int TRANSACTION_fstrim = 24;
        static final int TRANSACTION_initUser0 = 42;
        static final int TRANSACTION_isConvertibleToFbe = 43;
        static final int TRANSACTION_lockUserKey = 52;
        static final int TRANSACTION_markBootAttempt = 65;
        static final int TRANSACTION_mkdirs = 21;
        static final int TRANSACTION_monitor = 2;
        static final int TRANSACTION_mount = 14;
        static final int TRANSACTION_mountAppFuse = 27;
        static final int TRANSACTION_mountDefaultEncrypted = 41;
        static final int TRANSACTION_mountFstab = 44;
        static final int TRANSACTION_moveStorage = 19;
        static final int TRANSACTION_needsCheckpoint = 58;
        static final int TRANSACTION_needsRollback = 59;
        static final int TRANSACTION_onSecureKeyguardStateChanged = 11;
        static final int TRANSACTION_onUserAdded = 5;
        static final int TRANSACTION_onUserRemoved = 6;
        static final int TRANSACTION_onUserStarted = 7;
        static final int TRANSACTION_onUserStopped = 8;
        static final int TRANSACTION_openAppFuseFile = 71;
        static final int TRANSACTION_partition = 12;
        static final int TRANSACTION_prepareCheckpoint = 62;
        static final int TRANSACTION_prepareSandboxForApp = 55;
        static final int TRANSACTION_prepareUserStorage = 53;
        static final int TRANSACTION_remountUid = 20;
        static final int TRANSACTION_reset = 3;
        static final int TRANSACTION_restoreCheckpoint = 63;
        static final int TRANSACTION_restoreCheckpointPart = 64;
        static final int TRANSACTION_runIdleMaint = 25;
        static final int TRANSACTION_setListener = 1;
        static final int TRANSACTION_shutdown = 4;
        static final int TRANSACTION_startCheckpoint = 57;
        static final int TRANSACTION_supportsBlockCheckpoint = 67;
        static final int TRANSACTION_supportsCheckpoint = 66;
        static final int TRANSACTION_supportsFileCheckpoint = 68;
        static final int TRANSACTION_unlockUserKey = 51;
        static final int TRANSACTION_unmount = 15;
        static final int TRANSACTION_unmountAppFuse = 28;

        private static class Proxy implements IVold {
            public static IVold sDefaultImpl;
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

            public void setListener(IVoldListener listener) throws RemoteException {
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
                    Stub.getDefaultImpl().setListener(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void monitor() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(2, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().monitor();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void reset() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(3, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().reset();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void shutdown() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(4, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().shutdown();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onUserAdded(int userId, int userSerial) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    if (this.mRemote.transact(5, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onUserAdded(userId, userSerial);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onUserRemoved(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(6, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onUserRemoved(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onUserStarted(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(7, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onUserStarted(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onUserStopped(int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(8, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onUserStopped(userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addAppIds(String[] packageNames, int[] appIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStringArray(packageNames);
                    _data.writeIntArray(appIds);
                    if (this.mRemote.transact(9, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addAppIds(packageNames, appIds);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void addSandboxIds(int[] appIds, String[] sandboxIds) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeIntArray(appIds);
                    _data.writeStringArray(sandboxIds);
                    if (this.mRemote.transact(10, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addSandboxIds(appIds, sandboxIds);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void onSecureKeyguardStateChanged(boolean isShowing) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(isShowing ? 1 : 0);
                    if (this.mRemote.transact(11, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().onSecureKeyguardStateChanged(isShowing);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void partition(String diskId, int partitionType, int ratio) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(diskId);
                    _data.writeInt(partitionType);
                    _data.writeInt(ratio);
                    if (this.mRemote.transact(12, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().partition(diskId, partitionType, ratio);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void forgetPartition(String partGuid, String fsUuid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(partGuid);
                    _data.writeString(fsUuid);
                    if (this.mRemote.transact(13, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().forgetPartition(partGuid, fsUuid);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void mount(String volId, int mountFlags, int mountUserId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeInt(mountFlags);
                    _data.writeInt(mountUserId);
                    if (this.mRemote.transact(14, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().mount(volId, mountFlags, mountUserId);
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
                    if (this.mRemote.transact(15, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void format(String volId, String fsType) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    _data.writeString(fsType);
                    if (this.mRemote.transact(16, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().format(volId, fsType);
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
                    if (this.mRemote.transact(17, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void checkEncryption(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    if (this.mRemote.transact(18, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().checkEncryption(volId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void moveStorage(String fromVolId, String toVolId, IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(fromVolId);
                    _data.writeString(toVolId);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(19, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().moveStorage(fromVolId, toVolId, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void remountUid(int uid, int remountMode) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(remountMode);
                    if (this.mRemote.transact(20, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().remountUid(uid, remountMode);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void mkdirs(String path) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(path);
                    if (this.mRemote.transact(21, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().mkdirs(path);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String createObb(String sourcePath, String sourceKey, int ownerGid) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sourcePath);
                    _data.writeString(sourceKey);
                    _data.writeInt(ownerGid);
                    String str = 22;
                    if (!this.mRemote.transact(22, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().createObb(sourcePath, sourceKey, ownerGid);
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

            public void destroyObb(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    if (this.mRemote.transact(23, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroyObb(volId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void fstrim(int fstrimFlags, IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(fstrimFlags);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(24, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fstrim(fstrimFlags, listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void runIdleMaint(IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(25, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().runIdleMaint(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void abortIdleMaint(IVoldTaskListener listener) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeStrongBinder(listener != null ? listener.asBinder() : null);
                    if (this.mRemote.transact(26, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().abortIdleMaint(listener);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public FileDescriptor mountAppFuse(int uid, int mountId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(mountId);
                    FileDescriptor fileDescriptor = 27;
                    if (!this.mRemote.transact(27, _data, _reply, 0)) {
                        fileDescriptor = Stub.getDefaultImpl();
                        if (fileDescriptor != 0) {
                            fileDescriptor = Stub.getDefaultImpl().mountAppFuse(uid, mountId);
                            return fileDescriptor;
                        }
                    }
                    _reply.readException();
                    fileDescriptor = _reply.readRawFileDescriptor();
                    FileDescriptor _result = fileDescriptor;
                    _reply.recycle();
                    _data.recycle();
                    return _result;
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void unmountAppFuse(int uid, int mountId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(mountId);
                    if (this.mRemote.transact(28, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unmountAppFuse(uid, mountId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void fdeCheckPassword(String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    if (this.mRemote.transact(29, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fdeCheckPassword(password);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void fdeRestart() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(30, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fdeRestart();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int fdeComplete() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 31;
                    if (!this.mRemote.transact(31, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().fdeComplete();
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

            public void fdeEnable(int passwordType, String password, int encryptionFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(passwordType);
                    _data.writeString(password);
                    _data.writeInt(encryptionFlags);
                    if (this.mRemote.transact(32, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fdeEnable(passwordType, password, encryptionFlags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void fdeChangePassword(int passwordType, String currentPassword, String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(passwordType);
                    _data.writeString(currentPassword);
                    _data.writeString(password);
                    if (this.mRemote.transact(33, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fdeChangePassword(passwordType, currentPassword, password);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void fdeVerifyPassword(String password) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(password);
                    if (this.mRemote.transact(34, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fdeVerifyPassword(password);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public String fdeGetField(String key) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    String str = 35;
                    if (!this.mRemote.transact(35, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().fdeGetField(key);
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

            public void fdeSetField(String key, String value) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(key);
                    _data.writeString(value);
                    if (this.mRemote.transact(36, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fdeSetField(key, value);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public int fdeGetPasswordType() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    int i = 37;
                    if (!this.mRemote.transact(37, _data, _reply, 0)) {
                        i = Stub.getDefaultImpl();
                        if (i != 0) {
                            i = Stub.getDefaultImpl().fdeGetPasswordType();
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

            public String fdeGetPassword() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    String str = 38;
                    if (!this.mRemote.transact(38, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().fdeGetPassword();
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

            public void fdeClearPassword() throws RemoteException {
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
                    Stub.getDefaultImpl().fdeClearPassword();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void fbeEnable() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(40, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().fbeEnable();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void mountDefaultEncrypted() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(41, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().mountDefaultEncrypted();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void initUser0() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(42, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().initUser0();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public boolean isConvertibleToFbe() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(43, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().isConvertibleToFbe();
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

            public void mountFstab(String blkDevice, String mountPoint) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(blkDevice);
                    _data.writeString(mountPoint);
                    if (this.mRemote.transact(44, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().mountFstab(blkDevice, mountPoint);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void encryptFstab(String blkDevice, String mountPoint) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(blkDevice);
                    _data.writeString(mountPoint);
                    if (this.mRemote.transact(45, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().encryptFstab(blkDevice, mountPoint);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void createUserKey(int userId, int userSerial, boolean ephemeral) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeInt(ephemeral ? 1 : 0);
                    if (this.mRemote.transact(46, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().createUserKey(userId, userSerial, ephemeral);
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
                    if (this.mRemote.transact(47, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void addUserKeyAuth(int userId, int userSerial, String token, String secret) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeString(token);
                    _data.writeString(secret);
                    if (this.mRemote.transact(48, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().addUserKeyAuth(userId, userSerial, token, secret);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void clearUserKeyAuth(int userId, int userSerial, String token, String secret) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeString(token);
                    _data.writeString(secret);
                    if (this.mRemote.transact(49, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().clearUserKeyAuth(userId, userSerial, token, secret);
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
                    if (this.mRemote.transact(50, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void unlockUserKey(int userId, int userSerial, String token, String secret) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeString(token);
                    _data.writeString(secret);
                    if (this.mRemote.transact(51, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().unlockUserKey(userId, userSerial, token, secret);
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
                    if (this.mRemote.transact(52, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void prepareUserStorage(String uuid, int userId, int userSerial, int storageFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(userSerial);
                    _data.writeInt(storageFlags);
                    if (this.mRemote.transact(53, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepareUserStorage(uuid, userId, userSerial, storageFlags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void destroyUserStorage(String uuid, int userId, int storageFlags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(uuid);
                    _data.writeInt(userId);
                    _data.writeInt(storageFlags);
                    if (this.mRemote.transact(54, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroyUserStorage(uuid, userId, storageFlags);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void prepareSandboxForApp(String packageName, int appId, String sandboxId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeInt(appId);
                    _data.writeString(sandboxId);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(55, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepareSandboxForApp(packageName, appId, sandboxId, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void destroySandboxForApp(String packageName, String sandboxId, int userId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(packageName);
                    _data.writeString(sandboxId);
                    _data.writeInt(userId);
                    if (this.mRemote.transact(56, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroySandboxForApp(packageName, sandboxId, userId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void startCheckpoint(int retry) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(retry);
                    if (this.mRemote.transact(57, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().startCheckpoint(retry);
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
                    if (!this.mRemote.transact(58, _data, _reply, 0)) {
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

            public boolean needsRollback() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(59, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().needsRollback();
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

            public void abortChanges(String device, boolean retry) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(device);
                    _data.writeInt(retry ? 1 : 0);
                    if (this.mRemote.transact(60, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().abortChanges(device, retry);
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
                    if (this.mRemote.transact(61, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
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

            public void prepareCheckpoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(62, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().prepareCheckpoint();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreCheckpoint(String device) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(device);
                    if (this.mRemote.transact(63, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restoreCheckpoint(device);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void restoreCheckpointPart(String device, int count) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(device);
                    _data.writeInt(count);
                    if (this.mRemote.transact(64, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().restoreCheckpointPart(device, count);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public void markBootAttempt() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (this.mRemote.transact(65, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().markBootAttempt();
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
                    if (!this.mRemote.transact(66, _data, _reply, 0)) {
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

            public boolean supportsBlockCheckpoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(67, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().supportsBlockCheckpoint();
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

            public boolean supportsFileCheckpoint() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    boolean z = true;
                    boolean z2 = false;
                    if (!this.mRemote.transact(68, _data, _reply, 0)) {
                        z = Stub.getDefaultImpl();
                        if (z) {
                            z = Stub.getDefaultImpl().supportsFileCheckpoint();
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

            public String createStubVolume(String sourcePath, String mountPath, String fsType, String fsUuid, String fsLabel) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(sourcePath);
                    _data.writeString(mountPath);
                    _data.writeString(fsType);
                    _data.writeString(fsUuid);
                    _data.writeString(fsLabel);
                    String str = 69;
                    if (!this.mRemote.transact(69, _data, _reply, 0)) {
                        str = Stub.getDefaultImpl();
                        if (str != 0) {
                            str = Stub.getDefaultImpl().createStubVolume(sourcePath, mountPath, fsType, fsUuid, fsLabel);
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

            public void destroyStubVolume(String volId) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeString(volId);
                    if (this.mRemote.transact(70, _data, _reply, 0) || Stub.getDefaultImpl() == null) {
                        _reply.readException();
                        _reply.recycle();
                        _data.recycle();
                        return;
                    }
                    Stub.getDefaultImpl().destroyStubVolume(volId);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            public FileDescriptor openAppFuseFile(int uid, int mountId, int fileId, int flags) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(Stub.DESCRIPTOR);
                    _data.writeInt(uid);
                    _data.writeInt(mountId);
                    _data.writeInt(fileId);
                    _data.writeInt(flags);
                    FileDescriptor fileDescriptor = 71;
                    if (!this.mRemote.transact(71, _data, _reply, 0)) {
                        fileDescriptor = Stub.getDefaultImpl();
                        if (fileDescriptor != 0) {
                            fileDescriptor = Stub.getDefaultImpl().openAppFuseFile(uid, mountId, fileId, flags);
                            return fileDescriptor;
                        }
                    }
                    _reply.readException();
                    fileDescriptor = _reply.readRawFileDescriptor();
                    FileDescriptor _result = fileDescriptor;
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

        public static IVold asInterface(IBinder obj) {
            if (obj == null) {
                return null;
            }
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin == null || !(iin instanceof IVold)) {
                return new Proxy(obj);
            }
            return (IVold) iin;
        }

        public IBinder asBinder() {
            return this;
        }

        public static String getDefaultTransactionName(int transactionCode) {
            switch (transactionCode) {
                case 1:
                    return "setListener";
                case 2:
                    return "monitor";
                case 3:
                    return "reset";
                case 4:
                    return "shutdown";
                case 5:
                    return "onUserAdded";
                case 6:
                    return "onUserRemoved";
                case 7:
                    return "onUserStarted";
                case 8:
                    return "onUserStopped";
                case 9:
                    return "addAppIds";
                case 10:
                    return "addSandboxIds";
                case 11:
                    return "onSecureKeyguardStateChanged";
                case 12:
                    return "partition";
                case 13:
                    return "forgetPartition";
                case 14:
                    return "mount";
                case 15:
                    return "unmount";
                case 16:
                    return "format";
                case 17:
                    return "benchmark";
                case 18:
                    return "checkEncryption";
                case 19:
                    return "moveStorage";
                case 20:
                    return "remountUid";
                case 21:
                    return "mkdirs";
                case 22:
                    return "createObb";
                case 23:
                    return "destroyObb";
                case 24:
                    return "fstrim";
                case 25:
                    return "runIdleMaint";
                case 26:
                    return "abortIdleMaint";
                case 27:
                    return "mountAppFuse";
                case 28:
                    return "unmountAppFuse";
                case 29:
                    return "fdeCheckPassword";
                case 30:
                    return "fdeRestart";
                case 31:
                    return "fdeComplete";
                case 32:
                    return "fdeEnable";
                case 33:
                    return "fdeChangePassword";
                case 34:
                    return "fdeVerifyPassword";
                case 35:
                    return "fdeGetField";
                case 36:
                    return "fdeSetField";
                case 37:
                    return "fdeGetPasswordType";
                case 38:
                    return "fdeGetPassword";
                case 39:
                    return "fdeClearPassword";
                case 40:
                    return "fbeEnable";
                case 41:
                    return "mountDefaultEncrypted";
                case 42:
                    return "initUser0";
                case 43:
                    return "isConvertibleToFbe";
                case 44:
                    return "mountFstab";
                case 45:
                    return "encryptFstab";
                case 46:
                    return "createUserKey";
                case 47:
                    return "destroyUserKey";
                case 48:
                    return "addUserKeyAuth";
                case 49:
                    return "clearUserKeyAuth";
                case 50:
                    return "fixateNewestUserKeyAuth";
                case 51:
                    return "unlockUserKey";
                case 52:
                    return "lockUserKey";
                case 53:
                    return "prepareUserStorage";
                case 54:
                    return "destroyUserStorage";
                case 55:
                    return "prepareSandboxForApp";
                case 56:
                    return "destroySandboxForApp";
                case 57:
                    return "startCheckpoint";
                case 58:
                    return "needsCheckpoint";
                case 59:
                    return "needsRollback";
                case 60:
                    return "abortChanges";
                case 61:
                    return "commitChanges";
                case 62:
                    return "prepareCheckpoint";
                case 63:
                    return "restoreCheckpoint";
                case 64:
                    return "restoreCheckpointPart";
                case 65:
                    return "markBootAttempt";
                case 66:
                    return "supportsCheckpoint";
                case 67:
                    return "supportsBlockCheckpoint";
                case 68:
                    return "supportsFileCheckpoint";
                case 69:
                    return "createStubVolume";
                case 70:
                    return "destroyStubVolume";
                case 71:
                    return "openAppFuseFile";
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
                int _result;
                String _result2;
                String _result3;
                switch (i) {
                    case 1:
                        parcel.enforceInterface(descriptor);
                        setListener(android.os.IVoldListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface(descriptor);
                        monitor();
                        reply.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface(descriptor);
                        reset();
                        reply.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface(descriptor);
                        shutdown();
                        reply.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface(descriptor);
                        onUserAdded(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface(descriptor);
                        onUserRemoved(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface(descriptor);
                        onUserStarted(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface(descriptor);
                        onUserStopped(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface(descriptor);
                        addAppIds(data.createStringArray(), data.createIntArray());
                        reply.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface(descriptor);
                        addSandboxIds(data.createIntArray(), data.createStringArray());
                        reply.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface(descriptor);
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        onSecureKeyguardStateChanged(_arg2);
                        reply.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface(descriptor);
                        partition(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface(descriptor);
                        forgetPartition(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface(descriptor);
                        mount(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface(descriptor);
                        unmount(data.readString());
                        reply.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface(descriptor);
                        format(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 17:
                        parcel.enforceInterface(descriptor);
                        benchmark(data.readString(), android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface(descriptor);
                        checkEncryption(data.readString());
                        reply.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface(descriptor);
                        moveStorage(data.readString(), data.readString(), android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface(descriptor);
                        remountUid(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface(descriptor);
                        mkdirs(data.readString());
                        reply.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface(descriptor);
                        String _result4 = createObb(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeString(_result4);
                        return true;
                    case 23:
                        parcel.enforceInterface(descriptor);
                        destroyObb(data.readString());
                        reply.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface(descriptor);
                        fstrim(data.readInt(), android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 25:
                        parcel.enforceInterface(descriptor);
                        runIdleMaint(android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface(descriptor);
                        abortIdleMaint(android.os.IVoldTaskListener.Stub.asInterface(data.readStrongBinder()));
                        reply.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface(descriptor);
                        FileDescriptor _result5 = mountAppFuse(data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeRawFileDescriptor(_result5);
                        return true;
                    case 28:
                        parcel.enforceInterface(descriptor);
                        unmountAppFuse(data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 29:
                        parcel.enforceInterface(descriptor);
                        fdeCheckPassword(data.readString());
                        reply.writeNoException();
                        return true;
                    case 30:
                        parcel.enforceInterface(descriptor);
                        fdeRestart();
                        reply.writeNoException();
                        return true;
                    case 31:
                        parcel.enforceInterface(descriptor);
                        _result = fdeComplete();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 32:
                        parcel.enforceInterface(descriptor);
                        fdeEnable(data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 33:
                        parcel.enforceInterface(descriptor);
                        fdeChangePassword(data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 34:
                        parcel.enforceInterface(descriptor);
                        fdeVerifyPassword(data.readString());
                        reply.writeNoException();
                        return true;
                    case 35:
                        parcel.enforceInterface(descriptor);
                        _result2 = fdeGetField(data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result2);
                        return true;
                    case 36:
                        parcel.enforceInterface(descriptor);
                        fdeSetField(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 37:
                        parcel.enforceInterface(descriptor);
                        _result = fdeGetPasswordType();
                        reply.writeNoException();
                        parcel2.writeInt(_result);
                        return true;
                    case 38:
                        parcel.enforceInterface(descriptor);
                        _result3 = fdeGetPassword();
                        reply.writeNoException();
                        parcel2.writeString(_result3);
                        return true;
                    case 39:
                        parcel.enforceInterface(descriptor);
                        fdeClearPassword();
                        reply.writeNoException();
                        return true;
                    case 40:
                        parcel.enforceInterface(descriptor);
                        fbeEnable();
                        reply.writeNoException();
                        return true;
                    case 41:
                        parcel.enforceInterface(descriptor);
                        mountDefaultEncrypted();
                        reply.writeNoException();
                        return true;
                    case 42:
                        parcel.enforceInterface(descriptor);
                        initUser0();
                        reply.writeNoException();
                        return true;
                    case 43:
                        parcel.enforceInterface(descriptor);
                        _arg2 = isConvertibleToFbe();
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 44:
                        parcel.enforceInterface(descriptor);
                        mountFstab(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 45:
                        parcel.enforceInterface(descriptor);
                        encryptFstab(data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 46:
                        parcel.enforceInterface(descriptor);
                        int _arg0 = data.readInt();
                        int _arg1 = data.readInt();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        createUserKey(_arg0, _arg1, _arg2);
                        reply.writeNoException();
                        return true;
                    case 47:
                        parcel.enforceInterface(descriptor);
                        destroyUserKey(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 48:
                        parcel.enforceInterface(descriptor);
                        addUserKeyAuth(data.readInt(), data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 49:
                        parcel.enforceInterface(descriptor);
                        clearUserKeyAuth(data.readInt(), data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 50:
                        parcel.enforceInterface(descriptor);
                        fixateNewestUserKeyAuth(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 51:
                        parcel.enforceInterface(descriptor);
                        unlockUserKey(data.readInt(), data.readInt(), data.readString(), data.readString());
                        reply.writeNoException();
                        return true;
                    case 52:
                        parcel.enforceInterface(descriptor);
                        lockUserKey(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 53:
                        parcel.enforceInterface(descriptor);
                        prepareUserStorage(data.readString(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 54:
                        parcel.enforceInterface(descriptor);
                        destroyUserStorage(data.readString(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 55:
                        parcel.enforceInterface(descriptor);
                        prepareSandboxForApp(data.readString(), data.readInt(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 56:
                        parcel.enforceInterface(descriptor);
                        destroySandboxForApp(data.readString(), data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 57:
                        parcel.enforceInterface(descriptor);
                        startCheckpoint(data.readInt());
                        reply.writeNoException();
                        return true;
                    case 58:
                        parcel.enforceInterface(descriptor);
                        _arg2 = needsCheckpoint();
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 59:
                        parcel.enforceInterface(descriptor);
                        _arg2 = needsRollback();
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 60:
                        parcel.enforceInterface(descriptor);
                        _result2 = data.readString();
                        if (data.readInt() != 0) {
                            _arg2 = true;
                        }
                        abortChanges(_result2, _arg2);
                        reply.writeNoException();
                        return true;
                    case 61:
                        parcel.enforceInterface(descriptor);
                        commitChanges();
                        reply.writeNoException();
                        return true;
                    case 62:
                        parcel.enforceInterface(descriptor);
                        prepareCheckpoint();
                        reply.writeNoException();
                        return true;
                    case 63:
                        parcel.enforceInterface(descriptor);
                        restoreCheckpoint(data.readString());
                        reply.writeNoException();
                        return true;
                    case 64:
                        parcel.enforceInterface(descriptor);
                        restoreCheckpointPart(data.readString(), data.readInt());
                        reply.writeNoException();
                        return true;
                    case 65:
                        parcel.enforceInterface(descriptor);
                        markBootAttempt();
                        reply.writeNoException();
                        return true;
                    case 66:
                        parcel.enforceInterface(descriptor);
                        _arg2 = supportsCheckpoint();
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 67:
                        parcel.enforceInterface(descriptor);
                        _arg2 = supportsBlockCheckpoint();
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 68:
                        parcel.enforceInterface(descriptor);
                        _arg2 = supportsFileCheckpoint();
                        reply.writeNoException();
                        parcel2.writeInt(_arg2);
                        return true;
                    case 69:
                        parcel.enforceInterface(descriptor);
                        _result3 = createStubVolume(data.readString(), data.readString(), data.readString(), data.readString(), data.readString());
                        reply.writeNoException();
                        parcel2.writeString(_result3);
                        return true;
                    case 70:
                        parcel.enforceInterface(descriptor);
                        destroyStubVolume(data.readString());
                        reply.writeNoException();
                        return true;
                    case 71:
                        parcel.enforceInterface(descriptor);
                        FileDescriptor _result6 = openAppFuseFile(data.readInt(), data.readInt(), data.readInt(), data.readInt());
                        reply.writeNoException();
                        parcel2.writeRawFileDescriptor(_result6);
                        return true;
                    default:
                        return super.onTransact(code, data, reply, flags);
                }
            }
            parcel2.writeString(descriptor);
            return true;
        }

        public static boolean setDefaultImpl(IVold impl) {
            if (Proxy.sDefaultImpl != null || impl == null) {
                return false;
            }
            Proxy.sDefaultImpl = impl;
            return true;
        }

        public static IVold getDefaultImpl() {
            return Proxy.sDefaultImpl;
        }
    }

    void abortChanges(String str, boolean z) throws RemoteException;

    void abortIdleMaint(IVoldTaskListener iVoldTaskListener) throws RemoteException;

    void addAppIds(String[] strArr, int[] iArr) throws RemoteException;

    void addSandboxIds(int[] iArr, String[] strArr) throws RemoteException;

    void addUserKeyAuth(int i, int i2, String str, String str2) throws RemoteException;

    void benchmark(String str, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    void checkEncryption(String str) throws RemoteException;

    void clearUserKeyAuth(int i, int i2, String str, String str2) throws RemoteException;

    void commitChanges() throws RemoteException;

    String createObb(String str, String str2, int i) throws RemoteException;

    String createStubVolume(String str, String str2, String str3, String str4, String str5) throws RemoteException;

    void createUserKey(int i, int i2, boolean z) throws RemoteException;

    void destroyObb(String str) throws RemoteException;

    void destroySandboxForApp(String str, String str2, int i) throws RemoteException;

    void destroyStubVolume(String str) throws RemoteException;

    void destroyUserKey(int i) throws RemoteException;

    void destroyUserStorage(String str, int i, int i2) throws RemoteException;

    void encryptFstab(String str, String str2) throws RemoteException;

    void fbeEnable() throws RemoteException;

    void fdeChangePassword(int i, String str, String str2) throws RemoteException;

    void fdeCheckPassword(String str) throws RemoteException;

    void fdeClearPassword() throws RemoteException;

    int fdeComplete() throws RemoteException;

    void fdeEnable(int i, String str, int i2) throws RemoteException;

    String fdeGetField(String str) throws RemoteException;

    String fdeGetPassword() throws RemoteException;

    int fdeGetPasswordType() throws RemoteException;

    void fdeRestart() throws RemoteException;

    void fdeSetField(String str, String str2) throws RemoteException;

    void fdeVerifyPassword(String str) throws RemoteException;

    void fixateNewestUserKeyAuth(int i) throws RemoteException;

    void forgetPartition(String str, String str2) throws RemoteException;

    void format(String str, String str2) throws RemoteException;

    void fstrim(int i, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    void initUser0() throws RemoteException;

    boolean isConvertibleToFbe() throws RemoteException;

    void lockUserKey(int i) throws RemoteException;

    void markBootAttempt() throws RemoteException;

    void mkdirs(String str) throws RemoteException;

    void monitor() throws RemoteException;

    void mount(String str, int i, int i2) throws RemoteException;

    FileDescriptor mountAppFuse(int i, int i2) throws RemoteException;

    void mountDefaultEncrypted() throws RemoteException;

    void mountFstab(String str, String str2) throws RemoteException;

    void moveStorage(String str, String str2, IVoldTaskListener iVoldTaskListener) throws RemoteException;

    boolean needsCheckpoint() throws RemoteException;

    boolean needsRollback() throws RemoteException;

    void onSecureKeyguardStateChanged(boolean z) throws RemoteException;

    void onUserAdded(int i, int i2) throws RemoteException;

    void onUserRemoved(int i) throws RemoteException;

    void onUserStarted(int i) throws RemoteException;

    void onUserStopped(int i) throws RemoteException;

    FileDescriptor openAppFuseFile(int i, int i2, int i3, int i4) throws RemoteException;

    void partition(String str, int i, int i2) throws RemoteException;

    void prepareCheckpoint() throws RemoteException;

    void prepareSandboxForApp(String str, int i, String str2, int i2) throws RemoteException;

    void prepareUserStorage(String str, int i, int i2, int i3) throws RemoteException;

    void remountUid(int i, int i2) throws RemoteException;

    void reset() throws RemoteException;

    void restoreCheckpoint(String str) throws RemoteException;

    void restoreCheckpointPart(String str, int i) throws RemoteException;

    void runIdleMaint(IVoldTaskListener iVoldTaskListener) throws RemoteException;

    void setListener(IVoldListener iVoldListener) throws RemoteException;

    void shutdown() throws RemoteException;

    void startCheckpoint(int i) throws RemoteException;

    boolean supportsBlockCheckpoint() throws RemoteException;

    boolean supportsCheckpoint() throws RemoteException;

    boolean supportsFileCheckpoint() throws RemoteException;

    void unlockUserKey(int i, int i2, String str, String str2) throws RemoteException;

    void unmount(String str) throws RemoteException;

    void unmountAppFuse(int i, int i2) throws RemoteException;
}
