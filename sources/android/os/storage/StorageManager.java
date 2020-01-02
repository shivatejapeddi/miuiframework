package android.os.storage;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.app.ActivityThread;
import android.app.AppGlobals;
import android.app.AppOpsManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.IPackageMoveObserver;
import android.content.res.ObbInfo;
import android.content.res.ObbScanner;
import android.net.Uri;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.Binder;
import android.os.Environment;
import android.os.FileUtils;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IVoldTaskListener;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.ParcelableException;
import android.os.PersistableBundle;
import android.os.Process;
import android.os.ProxyFileDescriptorCallback;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.ServiceManager.ServiceNotFoundException;
import android.os.SystemProperties;
import android.os.storage.IObbActionListener.Stub;
import android.provider.MediaStore;
import android.provider.Settings.Global;
import android.sysprop.VoldProperties;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.text.TextUtils;
import android.util.DataUnit;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.TimeUtils;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.os.AppFuseMount;
import com.android.internal.os.FuseAppLoop;
import com.android.internal.os.FuseUnavailableMountException;
import com.android.internal.os.RoSystemProperties;
import com.android.internal.os.SomeArgs;
import com.android.internal.util.Preconditions;
import dalvik.system.BlockGuard;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.ref.WeakReference;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class StorageManager {
    public static final String ACTION_MANAGE_STORAGE = "android.os.storage.action.MANAGE_STORAGE";
    @UnsupportedAppUsage
    public static final int CRYPT_TYPE_DEFAULT = 1;
    @UnsupportedAppUsage
    public static final int CRYPT_TYPE_PASSWORD = 0;
    public static final int CRYPT_TYPE_PATTERN = 2;
    public static final int CRYPT_TYPE_PIN = 3;
    public static final int DEBUG_ADOPTABLE_FORCE_OFF = 2;
    public static final int DEBUG_ADOPTABLE_FORCE_ON = 1;
    public static final int DEBUG_EMULATE_FBE = 4;
    public static final int DEBUG_ISOLATED_STORAGE_FORCE_OFF = 128;
    public static final int DEBUG_ISOLATED_STORAGE_FORCE_ON = 64;
    public static final int DEBUG_SDCARDFS_FORCE_OFF = 16;
    public static final int DEBUG_SDCARDFS_FORCE_ON = 8;
    public static final int DEBUG_VIRTUAL_DISK = 32;
    private static final long DEFAULT_CACHE_MAX_BYTES = DataUnit.GIBIBYTES.toBytes(5);
    private static final int DEFAULT_CACHE_PERCENTAGE = 10;
    private static final long DEFAULT_FULL_THRESHOLD_BYTES = DataUnit.MEBIBYTES.toBytes(1);
    private static final long DEFAULT_THRESHOLD_MAX_BYTES = DataUnit.MEBIBYTES.toBytes(500);
    private static final int DEFAULT_THRESHOLD_PERCENTAGE = 5;
    public static final int ENCRYPTION_STATE_ERROR_CORRUPT = -4;
    public static final int ENCRYPTION_STATE_ERROR_INCOMPLETE = -2;
    public static final int ENCRYPTION_STATE_ERROR_INCONSISTENT = -3;
    public static final int ENCRYPTION_STATE_ERROR_UNKNOWN = -1;
    @UnsupportedAppUsage
    public static final int ENCRYPTION_STATE_NONE = 1;
    public static final int ENCRYPTION_STATE_OK = 0;
    public static final String EXTRA_REQUESTED_BYTES = "android.os.storage.extra.REQUESTED_BYTES";
    public static final String EXTRA_UUID = "android.os.storage.extra.UUID";
    @SystemApi
    public static final int FLAG_ALLOCATE_AGGRESSIVE = 1;
    public static final int FLAG_ALLOCATE_DEFY_ALL_RESERVED = 2;
    public static final int FLAG_ALLOCATE_DEFY_HALF_RESERVED = 4;
    public static final int FLAG_FOR_WRITE = 256;
    public static final int FLAG_INCLUDE_INVISIBLE = 1024;
    public static final int FLAG_REAL_STATE = 512;
    public static final int FLAG_STORAGE_CE = 2;
    public static final int FLAG_STORAGE_DE = 1;
    public static final int FLAG_STORAGE_EXTERNAL = 4;
    public static final int FSTRIM_FLAG_DEEP = 1;
    private static final boolean LOCAL_LOGV = Log.isLoggable(TAG, 2);
    public static final String OWNER_INFO_KEY = "OwnerInfo";
    public static final String PASSWORD_VISIBLE_KEY = "PasswordVisible";
    public static final String PATTERN_VISIBLE_KEY = "PatternVisible";
    public static final String PROP_ADOPTABLE = "persist.sys.adoptable";
    public static final String PROP_EMULATE_FBE = "persist.sys.emulate_fbe";
    public static final String PROP_HAS_ADOPTABLE = "vold.has_adoptable";
    public static final String PROP_HAS_RESERVED = "vold.has_reserved";
    public static final String PROP_ISOLATED_STORAGE = "persist.sys.isolated_storage";
    public static final String PROP_ISOLATED_STORAGE_SNAPSHOT = "sys.isolated_storage_snapshot";
    public static final String PROP_PRIMARY_PHYSICAL = "ro.vold.primary_physical";
    public static final String PROP_SDCARDFS = "persist.sys.sdcardfs";
    public static final String PROP_VIRTUAL_DISK = "persist.sys.virtual_disk";
    public static final String SYSTEM_LOCALE_KEY = "SystemLocale";
    private static final String TAG = "StorageManager";
    public static final UUID UUID_DEFAULT = UUID.fromString("41217664-9172-527a-b3d5-edabb50a7d69");
    public static final String UUID_PRIMARY_PHYSICAL = "primary_physical";
    public static final UUID UUID_PRIMARY_PHYSICAL_ = UUID.fromString("0f95a519-dae7-5abf-9519-fbd6209e05fd");
    public static final String UUID_PRIVATE_INTERNAL = null;
    public static final String UUID_SYSTEM = "system";
    public static final UUID UUID_SYSTEM_ = UUID.fromString("5d258386-e60d-59e3-826d-0089cdd42cc0");
    private static final String XATTR_CACHE_GROUP = "user.cache_group";
    private static final String XATTR_CACHE_TOMBSTONE = "user.cache_tombstone";
    private static volatile IStorageManager sStorageManager = null;
    private final AppOpsManager mAppOps;
    private final Context mContext;
    private final ArrayList<StorageEventListenerDelegate> mDelegates = new ArrayList();
    @GuardedBy({"mFuseAppLoopLock"})
    private FuseAppLoop mFuseAppLoop = null;
    private final Object mFuseAppLoopLock = new Object();
    private final Looper mLooper;
    private final AtomicInteger mNextNonce = new AtomicInteger(0);
    private final ObbActionListener mObbActionListener = new ObbActionListener(this, null);
    private final ContentResolver mResolver;
    private final IStorageManager mStorageManager;

    @Retention(RetentionPolicy.SOURCE)
    public @interface AllocateFlags {
    }

    private class ObbActionListener extends Stub {
        private SparseArray<ObbListenerDelegate> mListeners;

        private ObbActionListener() {
            this.mListeners = new SparseArray();
        }

        /* synthetic */ ObbActionListener(StorageManager x0, AnonymousClass1 x1) {
            this();
        }

        public void onObbResult(String filename, int nonce, int status) {
            ObbListenerDelegate delegate;
            synchronized (this.mListeners) {
                delegate = (ObbListenerDelegate) this.mListeners.get(nonce);
                if (delegate != null) {
                    this.mListeners.remove(nonce);
                }
            }
            if (delegate != null) {
                delegate.sendObbStateChanged(filename, status);
            }
        }

        public int addListener(OnObbStateChangeListener listener) {
            ObbListenerDelegate delegate = new ObbListenerDelegate(listener);
            synchronized (this.mListeners) {
                this.mListeners.put(delegate.nonce, delegate);
            }
            return delegate.nonce;
        }
    }

    private class ObbListenerDelegate {
        private final Handler mHandler;
        private final WeakReference<OnObbStateChangeListener> mObbEventListenerRef;
        private final int nonce;

        ObbListenerDelegate(OnObbStateChangeListener listener) {
            this.nonce = StorageManager.this.getNextNonce();
            this.mObbEventListenerRef = new WeakReference(listener);
            this.mHandler = new Handler(StorageManager.this.mLooper, StorageManager.this) {
                public void handleMessage(Message msg) {
                    OnObbStateChangeListener changeListener = ObbListenerDelegate.this.getListener();
                    if (changeListener != null) {
                        changeListener.onObbStateChange((String) msg.obj, msg.arg1);
                    }
                }
            };
        }

        /* Access modifiers changed, original: 0000 */
        public OnObbStateChangeListener getListener() {
            WeakReference weakReference = this.mObbEventListenerRef;
            if (weakReference == null) {
                return null;
            }
            return (OnObbStateChangeListener) weakReference.get();
        }

        /* Access modifiers changed, original: 0000 */
        public void sendObbStateChanged(String path, int state) {
            this.mHandler.obtainMessage(0, state, 0, path).sendToTarget();
        }
    }

    private static class StorageEventListenerDelegate extends IStorageEventListener.Stub implements Callback {
        private static final int MSG_DISK_DESTROYED = 6;
        private static final int MSG_DISK_SCANNED = 5;
        private static final int MSG_STORAGE_STATE_CHANGED = 1;
        private static final int MSG_VOLUME_FORGOTTEN = 4;
        private static final int MSG_VOLUME_RECORD_CHANGED = 3;
        private static final int MSG_VOLUME_STATE_CHANGED = 2;
        final StorageEventListener mCallback;
        final Handler mHandler;

        public StorageEventListenerDelegate(StorageEventListener callback, Looper looper) {
            this.mCallback = callback;
            this.mHandler = new Handler(looper, (Callback) this);
        }

        public boolean handleMessage(Message msg) {
            SomeArgs args = msg.obj;
            switch (msg.what) {
                case 1:
                    this.mCallback.onStorageStateChanged((String) args.arg1, (String) args.arg2, (String) args.arg3);
                    args.recycle();
                    return true;
                case 2:
                    this.mCallback.onVolumeStateChanged((VolumeInfo) args.arg1, args.argi2, args.argi3);
                    args.recycle();
                    return true;
                case 3:
                    this.mCallback.onVolumeRecordChanged((VolumeRecord) args.arg1);
                    args.recycle();
                    return true;
                case 4:
                    this.mCallback.onVolumeForgotten((String) args.arg1);
                    args.recycle();
                    return true;
                case 5:
                    this.mCallback.onDiskScanned((DiskInfo) args.arg1, args.argi2);
                    args.recycle();
                    return true;
                case 6:
                    this.mCallback.onDiskDestroyed((DiskInfo) args.arg1);
                    args.recycle();
                    return true;
                default:
                    args.recycle();
                    return false;
            }
        }

        public void onUsbMassStorageConnectionChanged(boolean connected) throws RemoteException {
        }

        public void onStorageStateChanged(String path, String oldState, String newState) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = path;
            args.arg2 = oldState;
            args.arg3 = newState;
            this.mHandler.obtainMessage(1, args).sendToTarget();
        }

        public void onVolumeStateChanged(VolumeInfo vol, int oldState, int newState) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = vol;
            args.argi2 = oldState;
            args.argi3 = newState;
            this.mHandler.obtainMessage(2, args).sendToTarget();
        }

        public void onVolumeRecordChanged(VolumeRecord rec) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = rec;
            this.mHandler.obtainMessage(3, args).sendToTarget();
        }

        public void onVolumeForgotten(String fsUuid) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = fsUuid;
            this.mHandler.obtainMessage(4, args).sendToTarget();
        }

        public void onDiskScanned(DiskInfo disk, int volumeCount) {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = disk;
            args.argi2 = volumeCount;
            this.mHandler.obtainMessage(5, args).sendToTarget();
        }

        public void onDiskDestroyed(DiskInfo disk) throws RemoteException {
            SomeArgs args = SomeArgs.obtain();
            args.arg1 = disk;
            this.mHandler.obtainMessage(6, args).sendToTarget();
        }
    }

    private int getNextNonce() {
        return this.mNextNonce.getAndIncrement();
    }

    @Deprecated
    @UnsupportedAppUsage
    public static StorageManager from(Context context) {
        return (StorageManager) context.getSystemService(StorageManager.class);
    }

    @UnsupportedAppUsage
    public StorageManager(Context context, Looper looper) throws ServiceNotFoundException {
        this.mContext = context;
        this.mResolver = context.getContentResolver();
        this.mLooper = looper;
        this.mStorageManager = IStorageManager.Stub.asInterface(ServiceManager.getServiceOrThrow("mount"));
        this.mAppOps = (AppOpsManager) this.mContext.getSystemService(AppOpsManager.class);
    }

    @UnsupportedAppUsage
    public void registerListener(StorageEventListener listener) {
        synchronized (this.mDelegates) {
            StorageEventListenerDelegate delegate = new StorageEventListenerDelegate(listener, this.mLooper);
            try {
                this.mStorageManager.registerListener(delegate);
                this.mDelegates.add(delegate);
            } catch (RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    @UnsupportedAppUsage
    public void unregisterListener(StorageEventListener listener) {
        synchronized (this.mDelegates) {
            Iterator<StorageEventListenerDelegate> i = this.mDelegates.iterator();
            while (i.hasNext()) {
                StorageEventListenerDelegate delegate = (StorageEventListenerDelegate) i.next();
                if (delegate.mCallback == listener) {
                    try {
                        this.mStorageManager.unregisterListener(delegate);
                        i.remove();
                    } catch (RemoteException e) {
                        throw e.rethrowFromSystemServer();
                    }
                }
            }
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public void enableUsbMassStorage() {
    }

    @Deprecated
    @UnsupportedAppUsage
    public void disableUsbMassStorage() {
    }

    @Deprecated
    @UnsupportedAppUsage
    public boolean isUsbMassStorageConnected() {
        return false;
    }

    @Deprecated
    @UnsupportedAppUsage
    public boolean isUsbMassStorageEnabled() {
        return false;
    }

    public boolean mountObb(String rawPath, String key, OnObbStateChangeListener listener) {
        Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        Preconditions.checkNotNull(listener, "listener cannot be null");
        try {
            String canonicalPath = new File(rawPath).getCanonicalPath();
            String str = rawPath;
            String str2 = canonicalPath;
            String str3 = key;
            this.mStorageManager.mountObb(str, str2, str3, this.mObbActionListener, this.mObbActionListener.addListener(listener), getObbInfo(canonicalPath));
            return true;
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to resolve path: ");
            stringBuilder.append(rawPath);
            throw new IllegalArgumentException(stringBuilder.toString(), e);
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        }
    }

    private ObbInfo getObbInfo(String canonicalPath) {
        try {
            return ObbScanner.getObbInfo(canonicalPath);
        } catch (IOException e) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't get OBB info for ");
            stringBuilder.append(canonicalPath);
            throw new IllegalArgumentException(stringBuilder.toString(), e);
        }
    }

    public boolean unmountObb(String rawPath, boolean force, OnObbStateChangeListener listener) {
        Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        Preconditions.checkNotNull(listener, "listener cannot be null");
        try {
            this.mStorageManager.unmountObb(rawPath, force, this.mObbActionListener, this.mObbActionListener.addListener(listener));
            return true;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public boolean isObbMounted(String rawPath) {
        Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        try {
            return this.mStorageManager.isObbMounted(rawPath);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getMountedObbPath(String rawPath) {
        Preconditions.checkNotNull(rawPath, "rawPath cannot be null");
        try {
            return this.mStorageManager.getMountedObbPath(rawPath);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public List<DiskInfo> getDisks() {
        try {
            return Arrays.asList(this.mStorageManager.getDisks());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public DiskInfo findDiskById(String id) {
        Preconditions.checkNotNull(id);
        for (DiskInfo disk : getDisks()) {
            if (Objects.equals(disk.id, id)) {
                return disk;
            }
        }
        return null;
    }

    @UnsupportedAppUsage
    public VolumeInfo findVolumeById(String id) {
        Preconditions.checkNotNull(id);
        for (VolumeInfo vol : getVolumes()) {
            if (Objects.equals(vol.id, id)) {
                return vol;
            }
        }
        return null;
    }

    @UnsupportedAppUsage
    public VolumeInfo findVolumeByUuid(String fsUuid) {
        Preconditions.checkNotNull(fsUuid);
        for (VolumeInfo vol : getVolumes()) {
            if (Objects.equals(vol.fsUuid, fsUuid)) {
                return vol;
            }
        }
        return null;
    }

    public VolumeRecord findRecordByUuid(String fsUuid) {
        Preconditions.checkNotNull(fsUuid);
        for (VolumeRecord rec : getVolumeRecords()) {
            if (Objects.equals(rec.fsUuid, fsUuid)) {
                return rec;
            }
        }
        return null;
    }

    public VolumeInfo findPrivateForEmulated(VolumeInfo emulatedVol) {
        if (emulatedVol != null) {
            return findVolumeById(emulatedVol.getId().replace(VolumeInfo.ID_EMULATED_INTERNAL, VolumeInfo.ID_PRIVATE_INTERNAL));
        }
        return null;
    }

    @UnsupportedAppUsage
    public VolumeInfo findEmulatedForPrivate(VolumeInfo privateVol) {
        if (privateVol != null) {
            return findVolumeById(privateVol.getId().replace(VolumeInfo.ID_PRIVATE_INTERNAL, VolumeInfo.ID_EMULATED_INTERNAL));
        }
        return null;
    }

    public VolumeInfo findVolumeByQualifiedUuid(String volumeUuid) {
        if (Objects.equals(UUID_PRIVATE_INTERNAL, volumeUuid)) {
            return findVolumeById(VolumeInfo.ID_PRIVATE_INTERNAL);
        }
        if (Objects.equals(UUID_PRIMARY_PHYSICAL, volumeUuid)) {
            return getPrimaryPhysicalVolume();
        }
        return findVolumeByUuid(volumeUuid);
    }

    public UUID getUuidForPath(File path) throws IOException {
        Preconditions.checkNotNull(path);
        String pathString = path.getCanonicalPath();
        if (FileUtils.contains(Environment.getDataDirectory().getAbsolutePath(), pathString)) {
            return UUID_DEFAULT;
        }
        try {
            int i = 0;
            VolumeInfo[] volumes = this.mStorageManager.getVolumes(0);
            int length = volumes.length;
            while (i < length) {
                VolumeInfo vol = volumes[i];
                if (!(vol.path == null || !FileUtils.contains(vol.path, pathString) || vol.type == 0 || vol.type == 5)) {
                    try {
                        volumes = convert(vol.fsUuid);
                        return volumes;
                    } catch (IllegalArgumentException e) {
                    }
                }
                i++;
            }
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to find a storage device for ");
            stringBuilder.append(path);
            throw new FileNotFoundException(stringBuilder.toString());
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        }
    }

    public File findPathForUuid(String volumeUuid) throws FileNotFoundException {
        VolumeInfo vol = findVolumeByQualifiedUuid(volumeUuid);
        if (vol != null) {
            return vol.getPath();
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Failed to find a storage device for ");
        stringBuilder.append(volumeUuid);
        throw new FileNotFoundException(stringBuilder.toString());
    }

    public boolean isAllocationSupported(FileDescriptor fd) {
        try {
            getUuidForPath(ParcelFileDescriptor.getFile(fd));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @UnsupportedAppUsage
    public List<VolumeInfo> getVolumes() {
        try {
            return Arrays.asList(this.mStorageManager.getVolumes(0));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<VolumeInfo> getWritablePrivateVolumes() {
        try {
            ArrayList<VolumeInfo> res = new ArrayList();
            int i = 0;
            VolumeInfo[] volumes = this.mStorageManager.getVolumes(0);
            int length = volumes.length;
            while (i < length) {
                VolumeInfo vol = volumes[i];
                if (vol.getType() == 1 && vol.isMountedWritable()) {
                    res.add(vol);
                }
                i++;
            }
            return res;
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public List<VolumeRecord> getVolumeRecords() {
        try {
            return Arrays.asList(this.mStorageManager.getVolumeRecords(0));
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public String getBestVolumeDescription(VolumeInfo vol) {
        if (vol == null) {
            return null;
        }
        if (!TextUtils.isEmpty(vol.fsUuid)) {
            VolumeRecord rec = findRecordByUuid(vol.fsUuid);
            if (!(rec == null || TextUtils.isEmpty(rec.nickname))) {
                return rec.nickname;
            }
        }
        if (!TextUtils.isEmpty(vol.getDescription())) {
            return vol.getDescription();
        }
        if (vol.disk != null) {
            return vol.disk.getDescription();
        }
        return null;
    }

    @UnsupportedAppUsage
    public VolumeInfo getPrimaryPhysicalVolume() {
        for (VolumeInfo vol : getVolumes()) {
            if (vol.isPrimaryPhysical()) {
                return vol;
            }
        }
        return null;
    }

    public void mount(String volId) {
        try {
            this.mStorageManager.mount(volId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public void unmount(String volId) {
        try {
            this.mStorageManager.unmount(volId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public void format(String volId) {
        try {
            this.mStorageManager.format(volId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    public long benchmark(String volId) {
        final CompletableFuture<PersistableBundle> result = new CompletableFuture();
        benchmark(volId, new IVoldTaskListener.Stub() {
            public void onStatus(int status, PersistableBundle extras) {
            }

            public void onFinished(int status, PersistableBundle extras) {
                result.complete(extras);
            }
        });
        try {
            return ((PersistableBundle) result.get(3, TimeUnit.MINUTES)).getLong("run", Long.MAX_VALUE) * TimeUtils.NANOS_PER_MS;
        } catch (Exception e) {
            return Long.MAX_VALUE;
        }
    }

    public void benchmark(String volId, IVoldTaskListener listener) {
        try {
            this.mStorageManager.benchmark(volId, listener);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @UnsupportedAppUsage
    public void partitionPublic(String diskId) {
        try {
            this.mStorageManager.partitionPublic(diskId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void partitionPrivate(String diskId) {
        try {
            this.mStorageManager.partitionPrivate(diskId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void partitionMixed(String diskId, int ratio) {
        try {
            this.mStorageManager.partitionMixed(diskId, ratio);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void wipeAdoptableDisks() {
        for (DiskInfo disk : getDisks()) {
            String diskId = disk.getId();
            boolean isAdoptable = disk.isAdoptable();
            String str = TAG;
            StringBuilder stringBuilder;
            if (isAdoptable) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Found adoptable ");
                stringBuilder.append(diskId);
                stringBuilder.append("; wiping");
                Slog.d(str, stringBuilder.toString());
                try {
                    this.mStorageManager.partitionPublic(diskId);
                } catch (Exception e) {
                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append("Failed to wipe ");
                    stringBuilder2.append(diskId);
                    stringBuilder2.append(", but soldiering onward");
                    Slog.w(str, stringBuilder2.toString(), e);
                }
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Ignorning non-adoptable disk ");
                stringBuilder.append(disk.getId());
                Slog.d(str, stringBuilder.toString());
            }
        }
    }

    public void setVolumeNickname(String fsUuid, String nickname) {
        try {
            this.mStorageManager.setVolumeNickname(fsUuid, nickname);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setVolumeInited(String fsUuid, boolean inited) {
        try {
            this.mStorageManager.setVolumeUserFlags(fsUuid, inited ? 1 : 0, 1);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setVolumeSnoozed(String fsUuid, boolean snoozed) {
        try {
            this.mStorageManager.setVolumeUserFlags(fsUuid, snoozed ? 2 : 0, 2);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void forgetVolume(String fsUuid) {
        try {
            this.mStorageManager.forgetVolume(fsUuid);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public String getPrimaryStorageUuid() {
        try {
            return this.mStorageManager.getPrimaryStorageUuid();
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void setPrimaryStorageUuid(String volumeUuid, IPackageMoveObserver callback) {
        try {
            this.mStorageManager.setPrimaryStorageUuid(volumeUuid, callback);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public StorageVolume getStorageVolume(File file) {
        return getStorageVolume(getVolumeList(), file);
    }

    public StorageVolume getStorageVolume(Uri uri) {
        String volumeName = MediaStore.getVolumeName(uri);
        Object obj = (volumeName.hashCode() == -1921573490 && volumeName.equals(MediaStore.VOLUME_EXTERNAL_PRIMARY)) ? null : -1;
        if (obj == null) {
            return getPrimaryStorageVolume();
        }
        for (StorageVolume vol : getStorageVolumes()) {
            if (Objects.equals(vol.getNormalizedUuid(), volumeName)) {
                return vol;
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Unknown volume for ");
        stringBuilder.append(uri);
        throw new IllegalStateException(stringBuilder.toString());
    }

    public static StorageVolume getStorageVolume(File file, int userId) {
        return getStorageVolume(getVolumeList(userId, 0), file);
    }

    @UnsupportedAppUsage
    private static StorageVolume getStorageVolume(StorageVolume[] volumes, File file) {
        if (file == null) {
            return null;
        }
        String path = file.getAbsolutePath();
        if (path.startsWith(ContentResolver.DEPRECATE_DATA_PREFIX)) {
            return ((StorageManager) AppGlobals.getInitialApplication().getSystemService(StorageManager.class)).getStorageVolume(ContentResolver.translateDeprecatedDataPath(path));
        }
        try {
            file = file.getCanonicalFile();
            int length = volumes.length;
            int i = 0;
            while (i < length) {
                StorageVolume volume = volumes[i];
                try {
                    if (FileUtils.contains(volume.getPathFile().getCanonicalFile(), file)) {
                        return volume;
                    }
                    i++;
                } catch (IOException e) {
                }
            }
            return null;
        } catch (IOException e2) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Could not get canonical path for ");
            stringBuilder.append(file);
            Slog.d(TAG, stringBuilder.toString());
            return null;
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public String getVolumeState(String mountPoint) {
        StorageVolume vol = getStorageVolume(new File(mountPoint));
        if (vol != null) {
            return vol.getState();
        }
        return "unknown";
    }

    public List<StorageVolume> getStorageVolumes() {
        ArrayList<StorageVolume> res = new ArrayList();
        Collections.addAll(res, getVolumeList(this.mContext.getUserId(), 1536));
        return res;
    }

    public StorageVolume getPrimaryStorageVolume() {
        return getVolumeList(this.mContext.getUserId(), 1536)[0];
    }

    public static Pair<String, Long> getPrimaryStoragePathAndSize() {
        return Pair.create(null, Long.valueOf(FileUtils.roundStorageSize(Environment.getDataDirectory().getTotalSpace() + Environment.getRootDirectory().getTotalSpace())));
    }

    public long getPrimaryStorageSize() {
        return FileUtils.roundStorageSize(Environment.getDataDirectory().getTotalSpace() + Environment.getRootDirectory().getTotalSpace());
    }

    public void mkdirs(File file) {
        BlockGuard.getVmPolicy().onPathAccess(file.getAbsolutePath());
        try {
            this.mStorageManager.mkdirs(this.mContext.getOpPackageName(), file.getAbsolutePath());
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public StorageVolume[] getVolumeList() {
        return getVolumeList(this.mContext.getUserId(), 0);
    }

    @UnsupportedAppUsage
    public static StorageVolume[] getVolumeList(int userId, int flags) {
        IStorageManager storageManager = IStorageManager.Stub.asInterface(ServiceManager.getService("mount"));
        try {
            String packageName = ActivityThread.currentOpPackageName();
            if (packageName == null) {
                String[] packageNames = ActivityThread.getPackageManager().getPackagesForUid(Process.myUid());
                if (packageNames != null) {
                    if (packageNames.length > 0) {
                        packageName = packageNames[0];
                    }
                }
                return new StorageVolume[0];
            }
            int uid = ActivityThread.getPackageManager().getPackageUid(packageName, 268435456, userId);
            if (uid <= 0) {
                return new StorageVolume[0];
            }
            return storageManager.getVolumeList(uid, packageName, flags);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    @Deprecated
    @UnsupportedAppUsage
    public String[] getVolumePaths() {
        StorageVolume[] volumes = getVolumeList();
        int count = volumes.length;
        String[] paths = new String[count];
        for (int i = 0; i < count; i++) {
            paths[i] = volumes[i].getPath();
        }
        return paths;
    }

    public StorageVolume getPrimaryVolume() {
        return getPrimaryVolume(getVolumeList());
    }

    public static StorageVolume getPrimaryVolume(StorageVolume[] volumes) {
        for (StorageVolume volume : volumes) {
            if (volume.isPrimary()) {
                return volume;
            }
        }
        throw new IllegalStateException("Missing primary storage");
    }

    @UnsupportedAppUsage
    public long getStorageBytesUntilLow(File path) {
        return path.getUsableSpace() - getStorageFullBytes(path);
    }

    @UnsupportedAppUsage
    public long getStorageLowBytes(File path) {
        return Math.min((path.getTotalSpace() * ((long) Global.getInt(this.mResolver, Global.SYS_STORAGE_THRESHOLD_PERCENTAGE, 5))) / 100, Global.getLong(this.mResolver, Global.SYS_STORAGE_THRESHOLD_MAX_BYTES, DEFAULT_THRESHOLD_MAX_BYTES));
    }

    public long getStorageCacheBytes(File path, int flags) {
        long result = Math.min((path.getTotalSpace() * ((long) Global.getInt(this.mResolver, Global.SYS_STORAGE_CACHE_PERCENTAGE, 10))) / 100, Global.getLong(this.mResolver, Global.SYS_STORAGE_CACHE_MAX_BYTES, DEFAULT_CACHE_MAX_BYTES));
        if ((flags & 1) != 0 || (flags & 2) != 0) {
            return 0;
        }
        if ((flags & 4) != 0) {
            return result / 2;
        }
        return result;
    }

    @UnsupportedAppUsage
    public long getStorageFullBytes(File path) {
        return Global.getLong(this.mResolver, Global.SYS_STORAGE_FULL_THRESHOLD_BYTES, DEFAULT_FULL_THRESHOLD_BYTES);
    }

    public void createUserKey(int userId, int serialNumber, boolean ephemeral) {
        try {
            this.mStorageManager.createUserKey(userId, serialNumber, ephemeral);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void destroyUserKey(int userId) {
        try {
            this.mStorageManager.destroyUserKey(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void unlockUserKey(int userId, int serialNumber, byte[] token, byte[] secret) {
        try {
            this.mStorageManager.unlockUserKey(userId, serialNumber, token, secret);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void lockUserKey(int userId) {
        try {
            this.mStorageManager.lockUserKey(userId);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void prepareUserStorage(String volumeUuid, int userId, int serialNumber, int flags) {
        try {
            this.mStorageManager.prepareUserStorage(volumeUuid, userId, serialNumber, flags);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void destroyUserStorage(String volumeUuid, int userId, int flags) {
        try {
            this.mStorageManager.destroyUserStorage(volumeUuid, userId, flags);
        } catch (RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public static boolean isUserKeyUnlocked(int userId) {
        if (sStorageManager == null) {
            sStorageManager = IStorageManager.Stub.asInterface(ServiceManager.getService("mount"));
        }
        if (sStorageManager == null) {
            Slog.w(TAG, "Early during boot, assuming locked");
            return false;
        }
        long token = Binder.clearCallingIdentity();
        try {
            boolean isUserKeyUnlocked = sStorageManager.isUserKeyUnlocked(userId);
            Binder.restoreCallingIdentity(token);
            return isUserKeyUnlocked;
        } catch (RemoteException e) {
            throw e.rethrowAsRuntimeException();
        } catch (Throwable th) {
            Binder.restoreCallingIdentity(token);
        }
    }

    public boolean isEncrypted(File file) {
        if (FileUtils.contains(Environment.getDataDirectory(), file)) {
            return isEncrypted();
        }
        if (FileUtils.contains(Environment.getExpandDirectory(), file)) {
            return true;
        }
        return false;
    }

    public static boolean isEncryptable() {
        return RoSystemProperties.CRYPTO_ENCRYPTABLE;
    }

    public static boolean isEncrypted() {
        return RoSystemProperties.CRYPTO_ENCRYPTED;
    }

    @UnsupportedAppUsage
    public static boolean isFileEncryptedNativeOnly() {
        if (isEncrypted()) {
            return RoSystemProperties.CRYPTO_FILE_ENCRYPTED;
        }
        return false;
    }

    public static boolean isBlockEncrypted() {
        if (isEncrypted()) {
            return RoSystemProperties.CRYPTO_BLOCK_ENCRYPTED;
        }
        return false;
    }

    public static boolean isNonDefaultBlockEncrypted() {
        boolean z = false;
        if (!isBlockEncrypted()) {
            return false;
        }
        try {
            if (IStorageManager.Stub.asInterface(ServiceManager.getService("mount")).getPasswordType() != 1) {
                z = true;
            }
            return z;
        } catch (RemoteException e) {
            Log.e(TAG, "Error getting encryption type");
            return false;
        }
    }

    public static boolean isBlockEncrypting() {
        String str = "";
        return str.equalsIgnoreCase((String) VoldProperties.encrypt_progress().orElse(str)) ^ 1;
    }

    public static boolean inCryptKeeperBounce() {
        return "trigger_restart_min_framework".equals((String) VoldProperties.decrypt().orElse(""));
    }

    public static boolean isFileEncryptedEmulatedOnly() {
        return SystemProperties.getBoolean(PROP_EMULATE_FBE, false);
    }

    public static boolean isFileEncryptedNativeOrEmulated() {
        return isFileEncryptedNativeOnly() || isFileEncryptedEmulatedOnly();
    }

    public static boolean hasAdoptable() {
        return SystemProperties.getBoolean(PROP_HAS_ADOPTABLE, false);
    }

    @SystemApi
    public static boolean hasIsolatedStorage() {
        return SystemProperties.getBoolean(PROP_ISOLATED_STORAGE_SNAPSHOT, SystemProperties.getBoolean(PROP_ISOLATED_STORAGE, true));
    }

    @Deprecated
    public static File maybeTranslateEmulatedPathToInternal(File path) {
        return path;
    }

    public File translateAppToSystem(File file, int pid, int uid) {
        return file;
    }

    public File translateSystemToApp(File file, int pid, int uid) {
        return file;
    }

    public static boolean checkPermissionAndAppOp(Context context, boolean enforce, int pid, int uid, String packageName, String permission, int op) {
        return checkPermissionAndAppOp(context, enforce, pid, uid, packageName, permission, op, true);
    }

    public static boolean checkPermissionAndCheckOp(Context context, boolean enforce, int pid, int uid, String packageName, String permission, int op) {
        return checkPermissionAndAppOp(context, enforce, pid, uid, packageName, permission, op, false);
    }

    private static boolean checkPermissionAndAppOp(Context context, boolean enforce, int pid, int uid, String packageName, String permission, int op, boolean note) {
        if (context.checkPermission(permission, pid, uid) == 0) {
            int mode;
            AppOpsManager appOps = (AppOpsManager) context.getSystemService(AppOpsManager.class);
            if (note) {
                mode = appOps.noteOpNoThrow(op, uid, packageName);
            } else {
                try {
                    appOps.checkPackage(uid, packageName);
                    mode = appOps.checkOpNoThrow(op, uid, packageName);
                } catch (SecurityException e) {
                    if (!enforce) {
                        return false;
                    }
                    throw e;
                }
            }
            if (mode == 0) {
                return true;
            }
            StringBuilder stringBuilder;
            if (mode != 1 && mode != 2 && mode != 3 && mode != 5) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(AppOpsManager.opToName(op));
                stringBuilder.append(" has unknown mode ");
                stringBuilder.append(AppOpsManager.modeToName(mode));
                throw new IllegalStateException(stringBuilder.toString());
            } else if (!enforce) {
                return false;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Op ");
                stringBuilder.append(AppOpsManager.opToName(op));
                stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
                stringBuilder.append(AppOpsManager.modeToName(mode));
                stringBuilder.append(" for package ");
                stringBuilder.append(packageName);
                throw new SecurityException(stringBuilder.toString());
            }
        } else if (!enforce) {
            return false;
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Permission ");
            stringBuilder2.append(permission);
            stringBuilder2.append(" denied for package ");
            stringBuilder2.append(packageName);
            throw new SecurityException(stringBuilder2.toString());
        }
    }

    private boolean checkPermissionAndAppOp(boolean enforce, int pid, int uid, String packageName, String permission, int op) {
        return checkPermissionAndAppOp(this.mContext, enforce, pid, uid, packageName, permission, op);
    }

    private boolean noteAppOpAllowingLegacy(boolean enforce, int pid, int uid, String packageName, int op) {
        int mode = this.mAppOps.noteOpNoThrow(op, uid, packageName);
        if (mode == 0) {
            return true;
        }
        StringBuilder stringBuilder;
        if (mode != 1 && mode != 2 && mode != 3) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(AppOpsManager.opToName(op));
            stringBuilder.append(" has unknown mode ");
            stringBuilder.append(AppOpsManager.modeToName(mode));
            throw new IllegalStateException(stringBuilder.toString());
        } else if (this.mAppOps.checkOpNoThrow(87, uid, packageName) == 0) {
            return true;
        } else {
            if (!enforce) {
                return false;
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Op ");
            stringBuilder.append(AppOpsManager.opToName(op));
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(AppOpsManager.modeToName(mode));
            stringBuilder.append(" for package ");
            stringBuilder.append(packageName);
            throw new SecurityException(stringBuilder.toString());
        }
    }

    public boolean checkPermissionReadAudio(boolean enforce, int pid, int uid, String packageName) {
        if (checkPermissionAndAppOp(enforce, pid, uid, packageName, permission.READ_EXTERNAL_STORAGE, 59)) {
            return noteAppOpAllowingLegacy(enforce, pid, uid, packageName, 81);
        }
        return false;
    }

    public boolean checkPermissionWriteAudio(boolean enforce, int pid, int uid, String packageName) {
        if (checkPermissionAndAppOp(enforce, pid, uid, packageName, permission.WRITE_EXTERNAL_STORAGE, 60)) {
            return noteAppOpAllowingLegacy(enforce, pid, uid, packageName, 82);
        }
        return false;
    }

    public boolean checkPermissionReadVideo(boolean enforce, int pid, int uid, String packageName) {
        if (checkPermissionAndAppOp(enforce, pid, uid, packageName, permission.READ_EXTERNAL_STORAGE, 59)) {
            return noteAppOpAllowingLegacy(enforce, pid, uid, packageName, 83);
        }
        return false;
    }

    public boolean checkPermissionWriteVideo(boolean enforce, int pid, int uid, String packageName) {
        if (checkPermissionAndAppOp(enforce, pid, uid, packageName, permission.WRITE_EXTERNAL_STORAGE, 60)) {
            return noteAppOpAllowingLegacy(enforce, pid, uid, packageName, 84);
        }
        return false;
    }

    public boolean checkPermissionReadImages(boolean enforce, int pid, int uid, String packageName) {
        if (checkPermissionAndAppOp(enforce, pid, uid, packageName, permission.READ_EXTERNAL_STORAGE, 59)) {
            return noteAppOpAllowingLegacy(enforce, pid, uid, packageName, 85);
        }
        return false;
    }

    public boolean checkPermissionWriteImages(boolean enforce, int pid, int uid, String packageName) {
        if (checkPermissionAndAppOp(enforce, pid, uid, packageName, permission.WRITE_EXTERNAL_STORAGE, 60)) {
            return noteAppOpAllowingLegacy(enforce, pid, uid, packageName, 86);
        }
        return false;
    }

    @VisibleForTesting
    public ParcelFileDescriptor openProxyFileDescriptor(int mode, ProxyFileDescriptorCallback callback, Handler handler, ThreadFactory factory) throws IOException {
        Preconditions.checkNotNull(callback);
        MetricsLogger.count(this.mContext, "storage_open_proxy_file_descriptor", 1);
        while (true) {
            try {
                ParcelFileDescriptor pfd;
                synchronized (this.mFuseAppLoopLock) {
                    boolean newlyCreated = false;
                    if (this.mFuseAppLoop == null) {
                        AppFuseMount mount = this.mStorageManager.mountProxyFileDescriptorBridge();
                        if (mount != null) {
                            this.mFuseAppLoop = new FuseAppLoop(mount.mountPointId, mount.fd, factory);
                            newlyCreated = true;
                        } else {
                            throw new IOException("Failed to mount proxy bridge");
                        }
                    }
                    if (handler == null) {
                        handler = new Handler(Looper.getMainLooper());
                    }
                    try {
                        int fileId = this.mFuseAppLoop.registerCallback(callback, handler);
                        pfd = this.mStorageManager.openProxyFileDescriptor(this.mFuseAppLoop.getMountPointId(), fileId, mode);
                        if (pfd != null) {
                        } else {
                            this.mFuseAppLoop.unregisterCallback(fileId);
                            throw new FuseUnavailableMountException(this.mFuseAppLoop.getMountPointId());
                        }
                    } catch (FuseUnavailableMountException exception) {
                        if (newlyCreated) {
                            throw new IOException(exception);
                        }
                        this.mFuseAppLoop = null;
                    }
                }
                return pfd;
            } catch (RemoteException e) {
                throw new IOException(e);
            }
        }
    }

    public ParcelFileDescriptor openProxyFileDescriptor(int mode, ProxyFileDescriptorCallback callback) throws IOException {
        return openProxyFileDescriptor(mode, callback, null, null);
    }

    public ParcelFileDescriptor openProxyFileDescriptor(int mode, ProxyFileDescriptorCallback callback, Handler handler) throws IOException {
        Preconditions.checkNotNull(handler);
        return openProxyFileDescriptor(mode, callback, handler, null);
    }

    @VisibleForTesting
    public int getProxyFileDescriptorMountPointId() {
        int mountPointId;
        synchronized (this.mFuseAppLoopLock) {
            mountPointId = this.mFuseAppLoop != null ? this.mFuseAppLoop.getMountPointId() : -1;
        }
        return mountPointId;
    }

    public long getCacheQuotaBytes(UUID storageUuid) throws IOException {
        try {
            return this.mStorageManager.getCacheQuotaBytes(convert(storageUuid), this.mContext.getApplicationInfo().uid);
        } catch (ParcelableException e) {
            e.maybeRethrow(IOException.class);
            throw new RuntimeException(e);
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        }
    }

    public long getCacheSizeBytes(UUID storageUuid) throws IOException {
        try {
            return this.mStorageManager.getCacheSizeBytes(convert(storageUuid), this.mContext.getApplicationInfo().uid);
        } catch (ParcelableException e) {
            e.maybeRethrow(IOException.class);
            throw new RuntimeException(e);
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        }
    }

    public long getAllocatableBytes(UUID storageUuid) throws IOException {
        return getAllocatableBytes(storageUuid, 0);
    }

    @SuppressLint({"Doclava125"})
    @SystemApi
    public long getAllocatableBytes(UUID storageUuid, int flags) throws IOException {
        try {
            return this.mStorageManager.getAllocatableBytes(convert(storageUuid), flags, this.mContext.getOpPackageName());
        } catch (ParcelableException e) {
            e.maybeRethrow(IOException.class);
            throw new RuntimeException(e);
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        }
    }

    public void allocateBytes(UUID storageUuid, long bytes) throws IOException {
        allocateBytes(storageUuid, bytes, 0);
    }

    @SuppressLint({"Doclava125"})
    @SystemApi
    public void allocateBytes(UUID storageUuid, long bytes, int flags) throws IOException {
        try {
            this.mStorageManager.allocateBytes(convert(storageUuid), bytes, flags, this.mContext.getOpPackageName());
        } catch (ParcelableException e) {
            e.maybeRethrow(IOException.class);
        } catch (RemoteException e2) {
            throw e2.rethrowFromSystemServer();
        }
    }

    public void allocateBytes(FileDescriptor fd, long bytes) throws IOException {
        allocateBytes(fd, bytes, 0);
    }

    @SuppressLint({"Doclava125"})
    @SystemApi
    public void allocateBytes(FileDescriptor fd, long bytes, int flags) throws IOException {
        String str = TAG;
        File file = ParcelFileDescriptor.getFile(fd);
        UUID uuid = getUuidForPath(file);
        int i = 0;
        while (i < 3) {
            try {
                long needBytes = bytes - (Os.fstat(fd).st_blocks * 512);
                if (needBytes > 0) {
                    allocateBytes(uuid, needBytes, flags);
                }
                Os.posix_fallocate(fd, 0, bytes);
                return;
            } catch (ErrnoException e) {
                if (e.errno != OsConstants.ENOSYS) {
                    if (e.errno != OsConstants.ENOTSUP) {
                        throw e;
                    }
                }
                Log.w(str, "fallocate() not supported; falling back to ftruncate()");
                Os.ftruncate(fd, bytes);
                return;
            } catch (ErrnoException e2) {
                if (e2.errno == OsConstants.ENOSPC) {
                    Log.w(str, "Odd, not enough space; let's try again?");
                    i++;
                } else {
                    throw e2.rethrowAsIOException();
                }
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Well this is embarassing; we can't allocate ");
        stringBuilder.append(bytes);
        stringBuilder.append(" for ");
        stringBuilder.append(file);
        throw new IOException(stringBuilder.toString());
    }

    private static void setCacheBehavior(File path, String name, boolean enabled) throws IOException {
        if (!path.isDirectory()) {
            throw new IOException("Cache behavior can only be set on directories");
        } else if (enabled) {
            try {
                Os.setxattr(path.getAbsolutePath(), name, "1".getBytes(StandardCharsets.UTF_8), 0);
            } catch (ErrnoException e) {
                throw e.rethrowAsIOException();
            }
        } else {
            try {
                Os.removexattr(path.getAbsolutePath(), name);
            } catch (ErrnoException e2) {
                if (e2.errno != OsConstants.ENODATA) {
                    throw e2.rethrowAsIOException();
                }
            }
        }
    }

    private static boolean isCacheBehavior(File path, String name) throws IOException {
        try {
            Os.getxattr(path.getAbsolutePath(), name);
            return true;
        } catch (ErrnoException e) {
            if (e.errno == OsConstants.ENODATA) {
                return false;
            }
            throw e.rethrowAsIOException();
        }
    }

    public void setCacheBehaviorGroup(File path, boolean group) throws IOException {
        setCacheBehavior(path, XATTR_CACHE_GROUP, group);
    }

    public boolean isCacheBehaviorGroup(File path) throws IOException {
        return isCacheBehavior(path, XATTR_CACHE_GROUP);
    }

    public void setCacheBehaviorTombstone(File path, boolean tombstone) throws IOException {
        setCacheBehavior(path, XATTR_CACHE_TOMBSTONE, tombstone);
    }

    public boolean isCacheBehaviorTombstone(File path) throws IOException {
        return isCacheBehavior(path, XATTR_CACHE_TOMBSTONE);
    }

    public static UUID convert(String uuid) {
        if (Objects.equals(uuid, UUID_PRIVATE_INTERNAL)) {
            return UUID_DEFAULT;
        }
        if (Objects.equals(uuid, UUID_PRIMARY_PHYSICAL)) {
            return UUID_PRIMARY_PHYSICAL_;
        }
        if (Objects.equals(uuid, "system")) {
            return UUID_SYSTEM_;
        }
        return UUID.fromString(uuid);
    }

    public static String convert(UUID storageUuid) {
        if (UUID_DEFAULT.equals(storageUuid)) {
            return UUID_PRIVATE_INTERNAL;
        }
        if (UUID_PRIMARY_PHYSICAL_.equals(storageUuid)) {
            return UUID_PRIMARY_PHYSICAL;
        }
        if (UUID_SYSTEM_.equals(storageUuid)) {
            return "system";
        }
        return storageUuid.toString();
    }
}
