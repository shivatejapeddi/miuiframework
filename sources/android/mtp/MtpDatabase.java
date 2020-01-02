package android.mtp;

import android.content.BroadcastReceiver;
import android.content.ContentProviderClient;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.mtp.MtpStorageManager.MtpNotifier;
import android.mtp.MtpStorageManager.MtpObject;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Video;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.util.SparseArray;
import android.view.Display;
import android.view.WindowManager;
import com.android.internal.annotations.VisibleForNative;
import com.google.android.collect.Sets;
import dalvik.system.CloseGuard;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.IntStream;

public class MtpDatabase implements AutoCloseable {
    private static final int[] AUDIO_PROPERTIES = new int[]{MtpConstants.PROPERTY_ARTIST, MtpConstants.PROPERTY_ALBUM_NAME, MtpConstants.PROPERTY_ALBUM_ARTIST, MtpConstants.PROPERTY_TRACK, MtpConstants.PROPERTY_ORIGINAL_RELEASE_DATE, MtpConstants.PROPERTY_DURATION, MtpConstants.PROPERTY_COMPOSER, MtpConstants.PROPERTY_AUDIO_WAVE_CODEC, MtpConstants.PROPERTY_BITRATE_TYPE, MtpConstants.PROPERTY_AUDIO_BITRATE, MtpConstants.PROPERTY_NUMBER_OF_CHANNELS, MtpConstants.PROPERTY_SAMPLE_RATE};
    private static final int[] DEVICE_PROPERTIES = new int[]{MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER, MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME, MtpConstants.DEVICE_PROPERTY_IMAGE_SIZE, MtpConstants.DEVICE_PROPERTY_BATTERY_LEVEL, MtpConstants.DEVICE_PROPERTY_PERCEIVED_DEVICE_TYPE};
    private static final int[] FILE_PROPERTIES = new int[]{MtpConstants.PROPERTY_STORAGE_ID, MtpConstants.PROPERTY_OBJECT_FORMAT, MtpConstants.PROPERTY_PROTECTION_STATUS, MtpConstants.PROPERTY_OBJECT_SIZE, MtpConstants.PROPERTY_OBJECT_FILE_NAME, MtpConstants.PROPERTY_DATE_MODIFIED, MtpConstants.PROPERTY_PERSISTENT_UID, MtpConstants.PROPERTY_PARENT_OBJECT, MtpConstants.PROPERTY_NAME, MtpConstants.PROPERTY_DISPLAY_NAME, MtpConstants.PROPERTY_DATE_ADDED};
    private static final String[] ID_PROJECTION = new String[]{"_id"};
    private static final int[] IMAGE_PROPERTIES = new int[]{MtpConstants.PROPERTY_DESCRIPTION};
    private static final String NO_MEDIA = ".nomedia";
    private static final String[] PATH_PROJECTION = new String[]{"_data"};
    private static final String PATH_WHERE = "_data=?";
    private static final int[] PLAYBACK_FORMATS = new int[]{12288, 12289, 12292, 12293, 12296, 12297, 12299, MtpConstants.FORMAT_EXIF_JPEG, MtpConstants.FORMAT_TIFF_EP, MtpConstants.FORMAT_BMP, MtpConstants.FORMAT_GIF, MtpConstants.FORMAT_JFIF, MtpConstants.FORMAT_PNG, MtpConstants.FORMAT_TIFF, MtpConstants.FORMAT_WMA, MtpConstants.FORMAT_OGG, MtpConstants.FORMAT_AAC, MtpConstants.FORMAT_MP4_CONTAINER, MtpConstants.FORMAT_MP2, MtpConstants.FORMAT_3GP_CONTAINER, MtpConstants.FORMAT_ABSTRACT_AV_PLAYLIST, MtpConstants.FORMAT_WPL_PLAYLIST, MtpConstants.FORMAT_M3U_PLAYLIST, MtpConstants.FORMAT_PLS_PLAYLIST, MtpConstants.FORMAT_XML_DOCUMENT, MtpConstants.FORMAT_FLAC, MtpConstants.FORMAT_DNG, MtpConstants.FORMAT_HEIF};
    private static final String TAG = MtpDatabase.class.getSimpleName();
    private static final int[] VIDEO_PROPERTIES = new int[]{MtpConstants.PROPERTY_ARTIST, MtpConstants.PROPERTY_ALBUM_NAME, MtpConstants.PROPERTY_DURATION, MtpConstants.PROPERTY_DESCRIPTION};
    private int mBatteryLevel;
    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED)) {
                MtpDatabase.this.mBatteryScale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
                int newLevel = intent.getIntExtra("level", 0);
                if (newLevel != MtpDatabase.this.mBatteryLevel) {
                    MtpDatabase.this.mBatteryLevel = newLevel;
                    if (MtpDatabase.this.mServer != null) {
                        MtpDatabase.this.mServer.sendDevicePropertyChanged(MtpConstants.DEVICE_PROPERTY_BATTERY_LEVEL);
                    }
                }
            }
        }
    };
    private int mBatteryScale;
    private final CloseGuard mCloseGuard = CloseGuard.get();
    private final AtomicBoolean mClosed = new AtomicBoolean();
    private final Context mContext;
    private SharedPreferences mDeviceProperties;
    private int mDeviceType;
    private MtpStorageManager mManager;
    private final ContentProviderClient mMediaProvider;
    @VisibleForNative
    private long mNativeContext;
    private final SparseArray<MtpPropertyGroup> mPropertyGroupsByFormat = new SparseArray();
    private final SparseArray<MtpPropertyGroup> mPropertyGroupsByProperty = new SparseArray();
    private MtpServer mServer;
    private final HashMap<String, MtpStorage> mStorageMap = new HashMap();

    private final native void native_finalize();

    private final native void native_setup();

    static {
        System.loadLibrary("media_jni");
    }

    @VisibleForNative
    private int[] getSupportedObjectProperties(int format) {
        if (!(format == 12296 || format == 12297)) {
            if (format != 12299) {
                if (!(format == MtpConstants.FORMAT_EXIF_JPEG || format == MtpConstants.FORMAT_BMP || format == MtpConstants.FORMAT_GIF || format == MtpConstants.FORMAT_PNG)) {
                    if (!(format == MtpConstants.FORMAT_WMV || format == MtpConstants.FORMAT_3GP_CONTAINER)) {
                        if (!(format == MtpConstants.FORMAT_DNG || format == MtpConstants.FORMAT_HEIF)) {
                            switch (format) {
                                case MtpConstants.FORMAT_WMA /*47361*/:
                                case MtpConstants.FORMAT_OGG /*47362*/:
                                case MtpConstants.FORMAT_AAC /*47363*/:
                                    break;
                                default:
                                    return FILE_PROPERTIES;
                            }
                        }
                    }
                }
                return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(IMAGE_PROPERTIES)).toArray();
            }
            return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(VIDEO_PROPERTIES)).toArray();
        }
        return IntStream.concat(Arrays.stream(FILE_PROPERTIES), Arrays.stream(AUDIO_PROPERTIES)).toArray();
    }

    public static Uri getObjectPropertiesUri(int format, String volumeName) {
        if (!(format == 12296 || format == 12297)) {
            if (format != 12299) {
                if (!(format == MtpConstants.FORMAT_EXIF_JPEG || format == MtpConstants.FORMAT_BMP || format == MtpConstants.FORMAT_GIF || format == MtpConstants.FORMAT_PNG)) {
                    if (!(format == MtpConstants.FORMAT_WMV || format == MtpConstants.FORMAT_3GP_CONTAINER)) {
                        if (!(format == MtpConstants.FORMAT_DNG || format == MtpConstants.FORMAT_HEIF)) {
                            switch (format) {
                                case MtpConstants.FORMAT_WMA /*47361*/:
                                case MtpConstants.FORMAT_OGG /*47362*/:
                                case MtpConstants.FORMAT_AAC /*47363*/:
                                    break;
                                default:
                                    return Files.getContentUri(volumeName);
                            }
                        }
                    }
                }
                return Media.getContentUri(volumeName);
            }
            return Video.Media.getContentUri(volumeName);
        }
        return Audio.Media.getContentUri(volumeName);
    }

    @VisibleForNative
    private int[] getSupportedDeviceProperties() {
        return DEVICE_PROPERTIES;
    }

    @VisibleForNative
    private int[] getSupportedPlaybackFormats() {
        return PLAYBACK_FORMATS;
    }

    @VisibleForNative
    private int[] getSupportedCaptureFormats() {
        return null;
    }

    public MtpDatabase(Context context, String[] subDirectories) {
        native_setup();
        this.mContext = (Context) Objects.requireNonNull(context);
        this.mMediaProvider = context.getContentResolver().acquireContentProviderClient(MediaStore.AUTHORITY);
        this.mManager = new MtpStorageManager(new MtpNotifier() {
            public void sendObjectAdded(int id) {
                if (MtpDatabase.this.mServer != null) {
                    MtpDatabase.this.mServer.sendObjectAdded(id);
                }
            }

            public void sendObjectRemoved(int id) {
                if (MtpDatabase.this.mServer != null) {
                    MtpDatabase.this.mServer.sendObjectRemoved(id);
                }
            }

            public void sendObjectInfoChanged(int id) {
                if (MtpDatabase.this.mServer != null) {
                    MtpDatabase.this.mServer.sendObjectInfoChanged(id);
                }
            }
        }, subDirectories == null ? null : Sets.newHashSet(subDirectories));
        initDeviceProperties(context);
        this.mDeviceType = SystemProperties.getInt("sys.usb.mtp.device_type", 0);
        this.mCloseGuard.open("close");
    }

    public void setServer(MtpServer server) {
        this.mServer = server;
        try {
            this.mContext.unregisterReceiver(this.mBatteryReceiver);
        } catch (IllegalArgumentException e) {
        }
        if (server != null) {
            this.mContext.registerReceiver(this.mBatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
    }

    public Context getContext() {
        return this.mContext;
    }

    public void close() {
        this.mManager.close();
        this.mCloseGuard.close();
        if (this.mClosed.compareAndSet(false, true)) {
            ContentProviderClient contentProviderClient = this.mMediaProvider;
            if (contentProviderClient != null) {
                contentProviderClient.close();
            }
            native_finalize();
        }
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            if (this.mCloseGuard != null) {
                this.mCloseGuard.warnIfOpen();
            }
            close();
        } finally {
            super.finalize();
        }
    }

    public void addStorage(StorageVolume storage) {
        MtpStorage mtpStorage = this.mManager.addMtpStorage(storage);
        this.mStorageMap.put(storage.getPath(), mtpStorage);
        MtpServer mtpServer = this.mServer;
        if (mtpServer != null) {
            mtpServer.addStorage(mtpStorage);
        }
    }

    public void removeStorage(StorageVolume storage) {
        MtpStorage mtpStorage = (MtpStorage) this.mStorageMap.get(storage.getPath());
        if (mtpStorage != null) {
            MtpServer mtpServer = this.mServer;
            if (mtpServer != null) {
                mtpServer.removeStorage(mtpStorage);
            }
            this.mManager.removeMtpStorage(mtpStorage);
            this.mStorageMap.remove(storage.getPath());
        }
    }

    /* JADX WARNING: Missing block: B:15:0x0061, code skipped:
            if (r6 != null) goto L_0x0063;
     */
    /* JADX WARNING: Missing block: B:16:0x0063, code skipped:
            r6.close();
     */
    /* JADX WARNING: Missing block: B:23:0x0077, code skipped:
            if (r6 != null) goto L_0x0063;
     */
    /* JADX WARNING: Missing block: B:24:0x007a, code skipped:
            r2.deleteDatabase(r4);
     */
    /* JADX WARNING: Missing block: B:32:?, code skipped:
            return;
     */
    private void initDeviceProperties(android.content.Context r17) {
        /*
        r16 = this;
        r1 = r16;
        r2 = r17;
        r3 = "device-properties";
        r0 = 0;
        r4 = "device-properties";
        r5 = r2.getSharedPreferences(r4, r0);
        r1.mDeviceProperties = r5;
        r5 = r2.getDatabasePath(r4);
        r6 = r5.exists();
        if (r6 == 0) goto L_0x0089;
    L_0x0019:
        r6 = 0;
        r7 = 0;
        r8 = 0;
        r0 = r2.openOrCreateDatabase(r4, r0, r8);	 Catch:{ Exception -> 0x0069 }
        r6 = r0;
        if (r6 == 0) goto L_0x005c;
    L_0x0023:
        r9 = "properties";
        r0 = "_id";
        r8 = "code";
        r10 = "value";
        r10 = new java.lang.String[]{r0, r8, r10};	 Catch:{ Exception -> 0x0069 }
        r11 = 0;
        r12 = 0;
        r13 = 0;
        r14 = 0;
        r15 = 0;
        r8 = r6;
        r0 = r8.query(r9, r10, r11, r12, r13, r14, r15);	 Catch:{ Exception -> 0x0069 }
        r7 = r0;
        if (r7 == 0) goto L_0x005c;
    L_0x003e:
        r0 = r1.mDeviceProperties;	 Catch:{ Exception -> 0x0069 }
        r0 = r0.edit();	 Catch:{ Exception -> 0x0069 }
    L_0x0044:
        r8 = r7.moveToNext();	 Catch:{ Exception -> 0x0069 }
        if (r8 == 0) goto L_0x0059;
    L_0x004a:
        r8 = 1;
        r8 = r7.getString(r8);	 Catch:{ Exception -> 0x0069 }
        r9 = 2;
        r9 = r7.getString(r9);	 Catch:{ Exception -> 0x0069 }
        r0.putString(r8, r9);	 Catch:{ Exception -> 0x0069 }
        goto L_0x0044;
    L_0x0059:
        r0.commit();	 Catch:{ Exception -> 0x0069 }
    L_0x005c:
        if (r7 == 0) goto L_0x0061;
    L_0x005e:
        r7.close();
    L_0x0061:
        if (r6 == 0) goto L_0x007a;
    L_0x0063:
        r6.close();
        goto L_0x007a;
    L_0x0067:
        r0 = move-exception;
        goto L_0x007e;
    L_0x0069:
        r0 = move-exception;
        r8 = TAG;	 Catch:{ all -> 0x0067 }
        r9 = "failed to migrate device properties";
        android.util.Log.e(r8, r9, r0);	 Catch:{ all -> 0x0067 }
        if (r7 == 0) goto L_0x0077;
    L_0x0074:
        r7.close();
    L_0x0077:
        if (r6 == 0) goto L_0x007a;
    L_0x0079:
        goto L_0x0063;
    L_0x007a:
        r2.deleteDatabase(r4);
        goto L_0x0089;
    L_0x007e:
        if (r7 == 0) goto L_0x0083;
    L_0x0080:
        r7.close();
    L_0x0083:
        if (r6 == 0) goto L_0x0088;
    L_0x0085:
        r6.close();
    L_0x0088:
        throw r0;
    L_0x0089:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpDatabase.initDeviceProperties(android.content.Context):void");
    }

    @VisibleForNative
    private int beginSendObject(String path, int format, int parent, int storageId) {
        MtpStorageManager mtpStorageManager = this.mManager;
        MtpObject parentObj = parent == 0 ? mtpStorageManager.getStorageRoot(storageId) : mtpStorageManager.getObject(parent);
        if (parentObj == null) {
            return -1;
        }
        return this.mManager.beginSendObject(parentObj, Paths.get(path, new String[0]).getFileName().toString(), format);
    }

    @VisibleForNative
    private void endSendObject(int handle, boolean succeeded) {
        MtpObject obj = this.mManager.getObject(handle);
        if (obj == null || !this.mManager.endSendObject(obj, succeeded)) {
            Log.e(TAG, "Failed to successfully end send object");
            return;
        }
        if (succeeded) {
            MediaStore.scanFile(this.mContext, obj.getPath().toFile());
        }
    }

    @VisibleForNative
    private void rescanFile(String path, int handle, int format) {
        MediaStore.scanFile(this.mContext, new File(path));
    }

    @VisibleForNative
    private int[] getObjectList(int storageID, int format, int parent) {
        List<MtpObject> objs = this.mManager.getObjects(parent, format, storageID);
        if (objs == null) {
            return null;
        }
        int[] ret = new int[objs.size()];
        for (int i = 0; i < objs.size(); i++) {
            ret[i] = ((MtpObject) objs.get(i)).getId();
        }
        return ret;
    }

    @VisibleForNative
    private int getNumObjects(int storageID, int format, int parent) {
        List<MtpObject> objs = this.mManager.getObjects(parent, format, storageID);
        if (objs == null) {
            return -1;
        }
        return objs.size();
    }

    @VisibleForNative
    private MtpPropertyList getObjectPropertyList(int handle, int format, int property, int groupCode, int depth) {
        int handle2 = handle;
        int format2 = format;
        int i = property;
        if (i != 0) {
            int err = -1;
            int depth2 = depth;
            if (depth2 == -1 && (handle2 == 0 || handle2 == -1)) {
                handle2 = -1;
                depth2 = 0;
            }
            if (depth2 != 0 && depth2 != 1) {
                return new MtpPropertyList(MtpConstants.RESPONSE_SPECIFICATION_BY_DEPTH_UNSUPPORTED);
            }
            List<MtpObject> objs = null;
            MtpObject thisObj = null;
            if (handle2 == -1) {
                objs = this.mManager.getObjects(0, format2, -1);
                if (objs == null) {
                    return new MtpPropertyList(8201);
                }
            } else if (handle2 != 0) {
                MtpObject obj = this.mManager.getObject(handle2);
                if (obj == null) {
                    return new MtpPropertyList(8201);
                }
                if (obj.getFormat() == format2 || format2 == 0) {
                    thisObj = obj;
                }
            }
            if (handle2 == 0 || depth2 == 1) {
                if (handle2 == 0) {
                    handle2 = -1;
                }
                objs = this.mManager.getObjects(handle2, format2, -1);
                if (objs == null) {
                    return new MtpPropertyList(8201);
                }
            }
            if (objs == null) {
                objs = new ArrayList();
            }
            if (thisObj != null) {
                objs.add(thisObj);
            }
            MtpPropertyList ret = new MtpPropertyList(8193);
            for (MtpObject obj2 : objs) {
                MtpPropertyGroup propertyGroup;
                if (i == err) {
                    if (!(format2 != 0 || handle == 0 || handle == err)) {
                        format2 = obj2.getFormat();
                    }
                    propertyGroup = (MtpPropertyGroup) this.mPropertyGroupsByFormat.get(format2);
                    if (propertyGroup == null) {
                        propertyGroup = new MtpPropertyGroup(getSupportedObjectProperties(format2));
                        this.mPropertyGroupsByFormat.put(format2, propertyGroup);
                    }
                } else {
                    propertyGroup = (MtpPropertyGroup) this.mPropertyGroupsByProperty.get(i);
                    if (propertyGroup == null) {
                        propertyGroup = new MtpPropertyGroup(new int[]{i});
                        this.mPropertyGroupsByProperty.put(i, propertyGroup);
                    }
                }
                err = propertyGroup.getPropertyList(this.mMediaProvider, obj2.getVolumeName(), obj2, ret);
                if (err != 8193) {
                    return new MtpPropertyList(err);
                }
                err = -1;
            }
            return ret;
        } else if (groupCode == 0) {
            return new MtpPropertyList(8198);
        } else {
            return new MtpPropertyList(MtpConstants.RESPONSE_SPECIFICATION_BY_GROUP_UNSUPPORTED);
        }
    }

    private int renameFile(int handle, String newName) {
        MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return 8201;
        }
        Path oldPath = obj.getPath();
        if (!this.mManager.beginRenameObject(obj, newName)) {
            return 8194;
        }
        Path newPath = obj.getPath();
        boolean success = oldPath.toFile().renameTo(newPath.toFile());
        try {
            Os.access(oldPath.toString(), OsConstants.F_OK);
            Os.access(newPath.toString(), OsConstants.F_OK);
        } catch (ErrnoException e) {
        }
        if (!this.mManager.endRenameObject(obj, oldPath.getFileName().toString(), success)) {
            Log.e(TAG, "Failed to end rename object");
        }
        if (!success) {
            return 8194;
        }
        ContentValues values = new ContentValues();
        values.put("_data", newPath.toString());
        String[] whereArgs = new String[]{oldPath.toString()};
        try {
            this.mMediaProvider.update(Files.getMtpObjectsUri(obj.getVolumeName()), values, PATH_WHERE, whereArgs);
        } catch (RemoteException e2) {
            Log.e(TAG, "RemoteException in mMediaProvider.update", e2);
        }
        String str;
        if (obj.isDir()) {
            str = ".";
            if (oldPath.getFileName().startsWith(str) && !newPath.startsWith(str)) {
                MediaStore.scanFile(this.mContext, newPath.toFile());
            }
        } else {
            str = ".nomedia";
            if (oldPath.getFileName().toString().toLowerCase(Locale.US).equals(str) && !newPath.getFileName().toString().toLowerCase(Locale.US).equals(str)) {
                MediaStore.scanFile(this.mContext, newPath.getParent().toFile());
            }
        }
        return 8193;
    }

    @VisibleForNative
    private int beginMoveObject(int handle, int newParent, int newStorage) {
        MtpObject obj = this.mManager.getObject(handle);
        MtpObject parent = newParent == 0 ? this.mManager.getStorageRoot(newStorage) : this.mManager.getObject(newParent);
        if (obj == null || parent == null) {
            return 8201;
        }
        return this.mManager.beginMoveObject(obj, parent) ? 8193 : 8194;
    }

    @VisibleForNative
    private void endMoveObject(int oldParent, int newParent, int oldStorage, int newStorage, int objId, boolean success) {
        MtpObject storageRoot;
        Cursor cursor;
        int i = oldParent;
        int i2 = newParent;
        int i3 = objId;
        boolean z = success;
        if (i == 0) {
            storageRoot = this.mManager.getStorageRoot(oldStorage);
        } else {
            int i4 = oldStorage;
            storageRoot = this.mManager.getObject(i);
        }
        MtpObject oldParentObj = storageRoot;
        if (i2 == 0) {
            storageRoot = this.mManager.getStorageRoot(newStorage);
        } else {
            int i5 = newStorage;
            storageRoot = this.mManager.getObject(i2);
        }
        MtpObject newParentObj = storageRoot;
        String name = this.mManager.getObject(i3).getName();
        if (newParentObj == null || oldParentObj == null || !this.mManager.endMoveObject(oldParentObj, newParentObj, name, z)) {
            Log.e(TAG, "Failed to end move object");
            return;
        }
        MtpObject obj = this.mManager.getObject(i3);
        if (z && obj != null) {
            int parentId;
            ContentValues values = new ContentValues();
            Path path = newParentObj.getPath().resolve(name);
            Path oldPath = oldParentObj.getPath().resolve(name);
            values.put("_data", path.toString());
            String str = "parent";
            if (obj.getParent().isRoot()) {
                values.put(str, Integer.valueOf(0));
            } else {
                parentId = findInMedia(newParentObj, path.getParent());
                if (parentId != -1) {
                    values.put(str, Integer.valueOf(parentId));
                } else {
                    deleteFromMedia(obj, oldPath, obj.isDir());
                    return;
                }
            }
            String[] whereArgs = new String[]{oldPath.toString()};
            parentId = -1;
            try {
                int i6;
                if (oldParentObj.isRoot()) {
                    i6 = -1;
                } else {
                    i6 = -1;
                    try {
                        parentId = findInMedia(oldParentObj, oldPath.getParent());
                    } catch (RemoteException e) {
                        storageRoot = e;
                        cursor = null;
                    }
                }
                if (oldParentObj.isRoot()) {
                    cursor = null;
                } else {
                    cursor = null;
                    if (parentId != -1) {
                        int i7 = parentId;
                    } else {
                        try {
                            MediaStore.scanFile(this.mContext, path.toFile());
                        } catch (RemoteException e2) {
                            storageRoot = e2;
                            Log.e(TAG, "RemoteException in mMediaProvider.update", storageRoot);
                        }
                    }
                }
                this.mMediaProvider.update(Files.getMtpObjectsUri(obj.getVolumeName()), values, PATH_WHERE, whereArgs);
            } catch (RemoteException e3) {
                storageRoot = e3;
                cursor = null;
                Log.e(TAG, "RemoteException in mMediaProvider.update", storageRoot);
            }
        }
    }

    @VisibleForNative
    private int beginCopyObject(int handle, int newParent, int newStorage) {
        MtpObject obj = this.mManager.getObject(handle);
        MtpObject parent = newParent == 0 ? this.mManager.getStorageRoot(newStorage) : this.mManager.getObject(newParent);
        if (obj == null || parent == null) {
            return 8201;
        }
        return this.mManager.beginCopyObject(obj, parent);
    }

    @VisibleForNative
    private void endCopyObject(int handle, boolean success) {
        MtpObject obj = this.mManager.getObject(handle);
        if (obj == null || !this.mManager.endCopyObject(obj, success)) {
            Log.e(TAG, "Failed to end copy object");
        } else if (success) {
            MediaStore.scanFile(this.mContext, obj.getPath().toFile());
        }
    }

    @VisibleForNative
    private int setObjectProperty(int handle, int property, long intValue, String stringValue) {
        if (property != MtpConstants.PROPERTY_OBJECT_FILE_NAME) {
            return MtpConstants.RESPONSE_OBJECT_PROP_NOT_SUPPORTED;
        }
        return renameFile(handle, stringValue);
    }

    @VisibleForNative
    private int getDeviceProperty(int property, long[] outIntValue, char[] outStringValue) {
        int width;
        switch (property) {
            case MtpConstants.DEVICE_PROPERTY_BATTERY_LEVEL /*20481*/:
                outIntValue[0] = (long) this.mBatteryLevel;
                outIntValue[1] = (long) this.mBatteryScale;
                return 8193;
            case MtpConstants.DEVICE_PROPERTY_IMAGE_SIZE /*20483*/:
                Display display = ((WindowManager) this.mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
                width = display.getMaximumSizeDimension();
                int height = display.getMaximumSizeDimension();
                String imageSize = new StringBuilder();
                imageSize.append(Integer.toString(width));
                imageSize.append("x");
                imageSize.append(Integer.toString(height));
                imageSize = imageSize.toString();
                imageSize.getChars(0, imageSize.length(), outStringValue, 0);
                outStringValue[imageSize.length()] = 0;
                return 8193;
            case MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER /*54273*/:
            case MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME /*54274*/:
                String value = this.mDeviceProperties.getString(Integer.toString(property), "");
                width = value.length();
                if (width > 255) {
                    width = 255;
                }
                value.getChars(0, width, outStringValue, 0);
                outStringValue[width] = 0;
                return 8193;
            case MtpConstants.DEVICE_PROPERTY_PERCEIVED_DEVICE_TYPE /*54279*/:
                outIntValue[0] = (long) this.mDeviceType;
                return 8193;
            default:
                return 8202;
        }
    }

    @VisibleForNative
    private int setDeviceProperty(int property, long intValue, String stringValue) {
        switch (property) {
            case MtpConstants.DEVICE_PROPERTY_SYNCHRONIZATION_PARTNER /*54273*/:
            case MtpConstants.DEVICE_PROPERTY_DEVICE_FRIENDLY_NAME /*54274*/:
                int i;
                Editor e = this.mDeviceProperties.edit();
                e.putString(Integer.toString(property), stringValue);
                if (e.commit()) {
                    i = 8193;
                } else {
                    i = 8194;
                }
                return i;
            default:
                return 8202;
        }
    }

    @VisibleForNative
    private boolean getObjectInfo(int handle, int[] outStorageFormatParent, char[] outName, long[] outCreatedModified) {
        MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return false;
        }
        outStorageFormatParent[0] = obj.getStorageId();
        outStorageFormatParent[1] = obj.getFormat();
        outStorageFormatParent[2] = obj.getParent().isRoot() ? 0 : obj.getParent().getId();
        int nameLen = Integer.min(obj.getName().length(), 255);
        obj.getName().getChars(0, nameLen, outName, 0);
        outName[nameLen] = 0;
        outCreatedModified[0] = obj.getModifiedTime();
        outCreatedModified[1] = obj.getModifiedTime();
        return true;
    }

    @VisibleForNative
    private int getObjectFilePath(int handle, char[] outFilePath, long[] outFileLengthFormat) {
        MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return 8201;
        }
        String path = obj.getPath().toString();
        int pathLen = Integer.min(path.length(), 4096);
        path.getChars(0, pathLen, outFilePath, 0);
        outFilePath[pathLen] = 0;
        outFileLengthFormat[0] = obj.getSize();
        outFileLengthFormat[1] = (long) obj.getFormat();
        return 8193;
    }

    private int getObjectFormat(int handle) {
        MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return -1;
        }
        return obj.getFormat();
    }

    @VisibleForNative
    private int beginDeleteObject(int handle) {
        MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return 8201;
        }
        if (this.mManager.beginRemoveObject(obj)) {
            return 8193;
        }
        return 8194;
    }

    @VisibleForNative
    private void endDeleteObject(int handle, boolean success) {
        MtpObject obj = this.mManager.getObject(handle);
        if (obj != null) {
            if (!this.mManager.endRemoveObject(obj, success)) {
                Log.e(TAG, "Failed to end remove object");
            }
            if (success) {
                deleteFromMedia(obj, obj.getPath(), obj.isDir());
            }
        }
    }

    /* JADX WARNING: Missing block: B:8:0x002f, code skipped:
            if (r9 != null) goto L_0x0031;
     */
    /* JADX WARNING: Missing block: B:9:0x0031, code skipped:
            r9.close();
     */
    /* JADX WARNING: Missing block: B:14:0x0054, code skipped:
            if (r9 == null) goto L_0x0057;
     */
    /* JADX WARNING: Missing block: B:15:0x0057, code skipped:
            return r8;
     */
    private int findInMedia(android.mtp.MtpStorageManager.MtpObject r12, java.nio.file.Path r13) {
        /*
        r11 = this;
        r0 = r12.getVolumeName();
        r0 = android.provider.MediaStore.Files.getMtpObjectsUri(r0);
        r8 = -1;
        r9 = 0;
        r1 = r11.mMediaProvider;	 Catch:{ RemoteException -> 0x0037 }
        r3 = ID_PROJECTION;	 Catch:{ RemoteException -> 0x0037 }
        r4 = "_data=?";
        r2 = 1;
        r5 = new java.lang.String[r2];	 Catch:{ RemoteException -> 0x0037 }
        r2 = r13.toString();	 Catch:{ RemoteException -> 0x0037 }
        r10 = 0;
        r5[r10] = r2;	 Catch:{ RemoteException -> 0x0037 }
        r6 = 0;
        r7 = 0;
        r2 = r0;
        r1 = r1.query(r2, r3, r4, r5, r6, r7);	 Catch:{ RemoteException -> 0x0037 }
        r9 = r1;
        if (r9 == 0) goto L_0x002f;
    L_0x0024:
        r1 = r9.moveToNext();	 Catch:{ RemoteException -> 0x0037 }
        if (r1 == 0) goto L_0x002f;
    L_0x002a:
        r1 = r9.getInt(r10);	 Catch:{ RemoteException -> 0x0037 }
        r8 = r1;
    L_0x002f:
        if (r9 == 0) goto L_0x0057;
    L_0x0031:
        r9.close();
        goto L_0x0057;
    L_0x0035:
        r1 = move-exception;
        goto L_0x0058;
    L_0x0037:
        r1 = move-exception;
        r2 = TAG;	 Catch:{ all -> 0x0035 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0035 }
        r3.<init>();	 Catch:{ all -> 0x0035 }
        r4 = "Error finding ";
        r3.append(r4);	 Catch:{ all -> 0x0035 }
        r3.append(r13);	 Catch:{ all -> 0x0035 }
        r4 = " in MediaProvider";
        r3.append(r4);	 Catch:{ all -> 0x0035 }
        r3 = r3.toString();	 Catch:{ all -> 0x0035 }
        android.util.Log.e(r2, r3);	 Catch:{ all -> 0x0035 }
        if (r9 == 0) goto L_0x0057;
    L_0x0056:
        goto L_0x0031;
    L_0x0057:
        return r8;
    L_0x0058:
        if (r9 == 0) goto L_0x005d;
    L_0x005a:
        r9.close();
    L_0x005d:
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpDatabase.findInMedia(android.mtp.MtpStorageManager$MtpObject, java.nio.file.Path):int");
    }

    private void deleteFromMedia(MtpObject obj, Path path, boolean isDir) {
        String str;
        StringBuilder stringBuilder;
        Uri objectsUri = Files.getMtpObjectsUri(obj.getVolumeName());
        if (isDir) {
            try {
                String[] strArr = new String[3];
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(path);
                stringBuilder2.append("/%");
                strArr[0] = stringBuilder2.toString();
                strArr[1] = Integer.toString(path.toString().length() + 1);
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(path.toString());
                stringBuilder3.append("/");
                strArr[2] = stringBuilder3.toString();
                this.mMediaProvider.delete(objectsUri, "_data LIKE ?1 AND lower(substr(_data,1,?2))=lower(?3)", strArr);
            } catch (Exception e) {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Failed to delete ");
                stringBuilder.append(path);
                stringBuilder.append(" from MediaProvider");
                Log.d(str, stringBuilder.toString());
                return;
            }
        }
        if (this.mMediaProvider.delete(objectsUri, PATH_WHERE, new String[]{path.toString()}) <= 0) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append("Mediaprovider didn't delete ");
            stringBuilder.append(path);
            Log.i(str, stringBuilder.toString());
        } else if (!isDir && path.toString().toLowerCase(Locale.US).endsWith(".nomedia")) {
            MediaStore.scanFile(this.mContext, path.getParent().toFile());
        }
    }

    @VisibleForNative
    private int[] getObjectReferences(int handle) {
        MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return null;
        }
        handle = findInMedia(obj, obj.getPath());
        if (handle == -1) {
            return null;
        }
        Cursor c = null;
        try {
            c = this.mMediaProvider.query(Files.getMtpReferencesUri(obj.getVolumeName(), (long) handle), PATH_PROJECTION, null, null, null, null);
            if (c == null) {
                if (c != null) {
                    c.close();
                }
                return null;
            }
            ArrayList<Integer> result = new ArrayList();
            while (c.moveToNext()) {
                MtpObject refObj = this.mManager.getByPath(c.getString(null));
                if (refObj != null) {
                    result.add(Integer.valueOf(refObj.getId()));
                }
            }
            int[] toArray = result.stream().mapToInt(-$$Lambda$UV1wDVoVlbcxpr8zevj_aMFtUGw.INSTANCE).toArray();
            c.close();
            return toArray;
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in getObjectList", e);
            if (c != null) {
                c.close();
            }
            return null;
        } catch (Throwable th) {
            if (c != null) {
                c.close();
            }
            throw th;
        }
    }

    @VisibleForNative
    private int setObjectReferences(int handle, int[] references) {
        int[] iArr = references;
        MtpObject obj = this.mManager.getObject(handle);
        if (obj == null) {
            return 8201;
        }
        int handle2 = findInMedia(obj, obj.getPath());
        int i = -1;
        if (handle2 == -1) {
            return 8194;
        }
        Uri uri = Files.getMtpReferencesUri(obj.getVolumeName(), (long) handle2);
        ArrayList<ContentValues> valuesList = new ArrayList();
        int length = iArr.length;
        int i2 = 0;
        while (i2 < length) {
            MtpObject refObj = this.mManager.getObject(iArr[i2]);
            if (refObj != null) {
                int refHandle = findInMedia(refObj, refObj.getPath());
                if (refHandle != i) {
                    ContentValues values = new ContentValues();
                    values.put("_id", Integer.valueOf(refHandle));
                    valuesList.add(values);
                }
            }
            i2++;
            i = -1;
        }
        try {
            if (this.mMediaProvider.bulkInsert(uri, (ContentValues[]) valuesList.toArray(new ContentValues[0])) > 0) {
                return 8193;
            }
        } catch (RemoteException e) {
            Log.e(TAG, "RemoteException in setObjectReferences", e);
        }
        return 8194;
    }
}
