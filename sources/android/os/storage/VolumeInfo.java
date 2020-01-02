package android.os.storage;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.os.UserHandle;
import android.provider.DocumentsContract;
import android.provider.DocumentsContract.Root;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.DebugUtils;
import android.util.SparseArray;
import android.util.SparseIntArray;
import com.android.internal.R;
import com.android.internal.util.IndentingPrintWriter;
import com.android.internal.util.Preconditions;
import java.io.CharArrayWriter;
import java.io.File;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

public class VolumeInfo implements Parcelable {
    public static final String ACTION_VOLUME_STATE_CHANGED = "android.os.storage.action.VOLUME_STATE_CHANGED";
    @UnsupportedAppUsage
    public static final Creator<VolumeInfo> CREATOR = new Creator<VolumeInfo>() {
        public VolumeInfo createFromParcel(Parcel in) {
            return new VolumeInfo(in);
        }

        public VolumeInfo[] newArray(int size) {
            return new VolumeInfo[size];
        }
    };
    private static final String DOCUMENT_AUTHORITY = "com.android.externalstorage.documents";
    private static final String DOCUMENT_ROOT_PRIMARY_EMULATED = "primary";
    public static final String EXTRA_VOLUME_ID = "android.os.storage.extra.VOLUME_ID";
    public static final String EXTRA_VOLUME_STATE = "android.os.storage.extra.VOLUME_STATE";
    public static final String ID_EMULATED_INTERNAL = "emulated";
    public static final String ID_PRIVATE_INTERNAL = "private";
    public static final int MOUNT_FLAG_PRIMARY = 1;
    public static final int MOUNT_FLAG_VISIBLE = 2;
    public static final int STATE_BAD_REMOVAL = 8;
    public static final int STATE_CHECKING = 1;
    public static final int STATE_EJECTING = 5;
    public static final int STATE_FORMATTING = 4;
    public static final int STATE_MOUNTED = 2;
    public static final int STATE_MOUNTED_READ_ONLY = 3;
    public static final int STATE_REMOVED = 7;
    public static final int STATE_UNMOUNTABLE = 6;
    public static final int STATE_UNMOUNTED = 0;
    public static final int TYPE_ASEC = 3;
    @UnsupportedAppUsage
    public static final int TYPE_EMULATED = 2;
    public static final int TYPE_OBB = 4;
    public static final int TYPE_PRIVATE = 1;
    @UnsupportedAppUsage
    public static final int TYPE_PUBLIC = 0;
    public static final int TYPE_STUB = 5;
    private static final Comparator<VolumeInfo> sDescriptionComparator = new Comparator<VolumeInfo>() {
        public int compare(VolumeInfo lhs, VolumeInfo rhs) {
            if (VolumeInfo.ID_PRIVATE_INTERNAL.equals(lhs.getId())) {
                return -1;
            }
            if (lhs.getDescription() == null) {
                return 1;
            }
            if (rhs.getDescription() == null) {
                return -1;
            }
            return lhs.getDescription().compareTo(rhs.getDescription());
        }
    };
    private static ArrayMap<String, String> sEnvironmentToBroadcast = new ArrayMap();
    private static SparseIntArray sStateToDescrip = new SparseIntArray();
    private static SparseArray<String> sStateToEnvironment = new SparseArray();
    @UnsupportedAppUsage
    public final DiskInfo disk;
    @UnsupportedAppUsage
    public String fsLabel;
    public String fsType;
    @UnsupportedAppUsage
    public String fsUuid;
    public final String id;
    @UnsupportedAppUsage
    public String internalPath;
    public int mountFlags = 0;
    public int mountUserId = -10000;
    public final String partGuid;
    @UnsupportedAppUsage
    public String path;
    @UnsupportedAppUsage
    public int state = 0;
    @UnsupportedAppUsage
    public final int type;

    static {
        SparseArray sparseArray = sStateToEnvironment;
        String str = Environment.MEDIA_UNMOUNTED;
        sparseArray.put(0, str);
        sparseArray = sStateToEnvironment;
        String str2 = Environment.MEDIA_CHECKING;
        sparseArray.put(1, str2);
        sparseArray = sStateToEnvironment;
        String str3 = Environment.MEDIA_MOUNTED;
        sparseArray.put(2, str3);
        sparseArray = sStateToEnvironment;
        String str4 = Environment.MEDIA_MOUNTED_READ_ONLY;
        sparseArray.put(3, str4);
        sStateToEnvironment.put(4, str);
        sparseArray = sStateToEnvironment;
        String str5 = Environment.MEDIA_EJECTING;
        sparseArray.put(5, str5);
        sparseArray = sStateToEnvironment;
        String str6 = Environment.MEDIA_UNMOUNTABLE;
        sparseArray.put(6, str6);
        sparseArray = sStateToEnvironment;
        String str7 = Environment.MEDIA_REMOVED;
        sparseArray.put(7, str7);
        sStateToEnvironment.put(8, Environment.MEDIA_BAD_REMOVAL);
        sEnvironmentToBroadcast.put(str, Intent.ACTION_MEDIA_UNMOUNTED);
        sEnvironmentToBroadcast.put(str2, Intent.ACTION_MEDIA_CHECKING);
        sEnvironmentToBroadcast.put(str3, Intent.ACTION_MEDIA_MOUNTED);
        sEnvironmentToBroadcast.put(str4, Intent.ACTION_MEDIA_MOUNTED);
        sEnvironmentToBroadcast.put(str5, Intent.ACTION_MEDIA_EJECT);
        sEnvironmentToBroadcast.put(str6, Intent.ACTION_MEDIA_UNMOUNTABLE);
        sEnvironmentToBroadcast.put(str7, Intent.ACTION_MEDIA_REMOVED);
        sEnvironmentToBroadcast.put(Environment.MEDIA_BAD_REMOVAL, Intent.ACTION_MEDIA_BAD_REMOVAL);
        sStateToDescrip.put(0, R.string.ext_media_status_unmounted);
        sStateToDescrip.put(1, R.string.ext_media_status_checking);
        sStateToDescrip.put(2, R.string.ext_media_status_mounted);
        sStateToDescrip.put(3, R.string.ext_media_status_mounted_ro);
        sStateToDescrip.put(4, R.string.ext_media_status_formatting);
        sStateToDescrip.put(5, R.string.ext_media_status_ejecting);
        sStateToDescrip.put(6, R.string.ext_media_status_unmountable);
        sStateToDescrip.put(7, R.string.ext_media_status_removed);
        sStateToDescrip.put(8, R.string.ext_media_status_bad_removal);
    }

    public VolumeInfo(String id, int type, DiskInfo disk, String partGuid) {
        this.id = (String) Preconditions.checkNotNull(id);
        this.type = type;
        this.disk = disk;
        this.partGuid = partGuid;
    }

    @UnsupportedAppUsage
    public VolumeInfo(Parcel parcel) {
        this.id = parcel.readString();
        this.type = parcel.readInt();
        if (parcel.readInt() != 0) {
            this.disk = (DiskInfo) DiskInfo.CREATOR.createFromParcel(parcel);
        } else {
            this.disk = null;
        }
        this.partGuid = parcel.readString();
        this.mountFlags = parcel.readInt();
        this.mountUserId = parcel.readInt();
        this.state = parcel.readInt();
        this.fsType = parcel.readString();
        this.fsUuid = parcel.readString();
        this.fsLabel = parcel.readString();
        this.path = parcel.readString();
        this.internalPath = parcel.readString();
    }

    @UnsupportedAppUsage
    public static String getEnvironmentForState(int state) {
        String envState = (String) sStateToEnvironment.get(state);
        if (envState != null) {
            return envState;
        }
        return "unknown";
    }

    public static String getBroadcastForEnvironment(String envState) {
        return (String) sEnvironmentToBroadcast.get(envState);
    }

    public static String getBroadcastForState(int state) {
        return getBroadcastForEnvironment(getEnvironmentForState(state));
    }

    public static Comparator<VolumeInfo> getDescriptionComparator() {
        return sDescriptionComparator;
    }

    @UnsupportedAppUsage
    public String getId() {
        return this.id;
    }

    @UnsupportedAppUsage
    public DiskInfo getDisk() {
        return this.disk;
    }

    @UnsupportedAppUsage
    public String getDiskId() {
        DiskInfo diskInfo = this.disk;
        return diskInfo != null ? diskInfo.id : null;
    }

    @UnsupportedAppUsage
    public int getType() {
        return this.type;
    }

    @UnsupportedAppUsage
    public int getState() {
        return this.state;
    }

    public int getStateDescription() {
        return sStateToDescrip.get(this.state, 0);
    }

    @UnsupportedAppUsage
    public String getFsUuid() {
        return this.fsUuid;
    }

    public String getNormalizedFsUuid() {
        String str = this.fsUuid;
        return str != null ? str.toLowerCase(Locale.US) : null;
    }

    @UnsupportedAppUsage
    public int getMountUserId() {
        return this.mountUserId;
    }

    @UnsupportedAppUsage
    public String getDescription() {
        if (!ID_PRIVATE_INTERNAL.equals(this.id)) {
            if (!ID_EMULATED_INTERNAL.equals(this.id)) {
                if (TextUtils.isEmpty(this.fsLabel)) {
                    return null;
                }
                return this.fsLabel;
            }
        }
        return Resources.getSystem().getString(R.string.storage_internal);
    }

    @UnsupportedAppUsage
    public boolean isMountedReadable() {
        int i = this.state;
        return i == 2 || i == 3;
    }

    @UnsupportedAppUsage
    public boolean isMountedWritable() {
        return this.state == 2;
    }

    @UnsupportedAppUsage
    public boolean isPrimary() {
        return (this.mountFlags & 1) != 0;
    }

    @UnsupportedAppUsage
    public boolean isPrimaryPhysical() {
        return isPrimary() && getType() == 0;
    }

    @UnsupportedAppUsage
    public boolean isVisible() {
        return (this.mountFlags & 2) != 0;
    }

    public boolean isVisibleForUser(int userId) {
        int i = this.type;
        if ((i == 0 || i == 5) && this.mountUserId == userId) {
            return isVisible();
        }
        if (this.type == 2) {
            return isVisible();
        }
        return false;
    }

    public boolean isVisibleForRead(int userId) {
        return isVisibleForUser(userId);
    }

    @UnsupportedAppUsage
    public boolean isVisibleForWrite(int userId) {
        return isVisibleForUser(userId);
    }

    @UnsupportedAppUsage
    public File getPath() {
        String str = this.path;
        return str != null ? new File(str) : null;
    }

    @UnsupportedAppUsage
    public File getInternalPath() {
        String str = this.internalPath;
        return str != null ? new File(str) : null;
    }

    @UnsupportedAppUsage
    public File getPathForUser(int userId) {
        String str = this.path;
        if (str == null) {
            return null;
        }
        int i = this.type;
        if (i == 0 || i == 5) {
            return new File(this.path);
        }
        if (i == 2) {
            return new File(str, Integer.toString(userId));
        }
        return null;
    }

    @UnsupportedAppUsage
    public File getInternalPathForUser(int userId) {
        if (this.path == null) {
            return null;
        }
        int i = this.type;
        if (i == 0 || i == 5) {
            return new File(this.path.replace("/storage/", "/mnt/media_rw/"));
        }
        return getPathForUser(userId);
    }

    @UnsupportedAppUsage
    public StorageVolume buildStorageVolume(Context context, int userId, boolean reportUnmounted) {
        File internalPath;
        String derivedFsUuid;
        long maxFileSize;
        boolean emulated;
        boolean removable;
        String description;
        Context context2 = context;
        int i = userId;
        StorageManager storage = (StorageManager) context2.getSystemService(StorageManager.class);
        String envState = reportUnmounted ? Environment.MEDIA_UNMOUNTED : getEnvironmentForState(this.state);
        File userPath = getPathForUser(i);
        String str = "/dev/null";
        if (userPath == null) {
            userPath = new File(str);
        }
        File internalPath2 = getInternalPathForUser(i);
        if (internalPath2 == null) {
            internalPath = new File(str);
        } else {
            internalPath = internalPath2;
        }
        str = null;
        String derivedFsUuid2 = this.fsUuid;
        int i2 = this.type;
        if (i2 == 2) {
            boolean removable2;
            VolumeInfo privateVol = storage.findPrivateForEmulated(this);
            if (privateVol != null) {
                str = storage.getBestVolumeDescription(privateVol);
                derivedFsUuid2 = privateVol.fsUuid;
            }
            if (ID_EMULATED_INTERNAL.equals(this.id)) {
                removable2 = false;
            } else {
                removable2 = true;
            }
            derivedFsUuid = derivedFsUuid2;
            maxFileSize = 0;
            emulated = true;
            removable = removable2;
        } else if (i2 == 0 || i2 == 5) {
            str = storage.getBestVolumeDescription(this);
            if ("vfat".equals(this.fsType)) {
                derivedFsUuid = derivedFsUuid2;
                maxFileSize = 4294967295L;
                emulated = false;
                removable = true;
            } else {
                derivedFsUuid = derivedFsUuid2;
                maxFileSize = 0;
                emulated = false;
                removable = true;
            }
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Unexpected volume type ");
            stringBuilder.append(this.type);
            throw new IllegalStateException(stringBuilder.toString());
        }
        if (str == null) {
            description = context2.getString(17039374);
        } else {
            description = str;
        }
        return new StorageVolume(this.id, userPath, internalPath, description, isPrimary(), removable, emulated, false, maxFileSize, new UserHandle(i), derivedFsUuid, envState);
    }

    @UnsupportedAppUsage
    public static int buildStableMtpStorageId(String fsUuid) {
        if (TextUtils.isEmpty(fsUuid)) {
            return 0;
        }
        int hash = 0;
        for (int i = 0; i < fsUuid.length(); i++) {
            hash = (hash * 31) + fsUuid.charAt(i);
        }
        hash = ((hash << 16) ^ hash) & -65536;
        if (hash == 0) {
            hash = 131072;
        }
        if (hash == 65536) {
            hash = 131072;
        }
        if (hash == -65536) {
            hash = -131072;
        }
        return hash | 1;
    }

    @UnsupportedAppUsage
    public Intent buildBrowseIntent() {
        return buildBrowseIntentForUser(UserHandle.myUserId());
    }

    public Intent buildBrowseIntentForUser(int userId) {
        Uri uri;
        int i = this.type;
        String str = "com.android.externalstorage.documents";
        if ((i == 0 || i == 5) && this.mountUserId == userId) {
            uri = DocumentsContract.buildRootUri(str, this.fsUuid);
        } else if (this.type != 2 || !isPrimary()) {
            return null;
        } else {
            uri = DocumentsContract.buildRootUri(str, "primary");
        }
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setDataAndType(uri, Root.MIME_TYPE_ITEM);
        intent.putExtra(DocumentsContract.EXTRA_SHOW_ADVANCED, isPrimary());
        return intent;
    }

    public String toString() {
        CharArrayWriter writer = new CharArrayWriter();
        dump(new IndentingPrintWriter(writer, "    ", 80));
        return writer.toString();
    }

    public void dump(IndentingPrintWriter pw) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("VolumeInfo{");
        stringBuilder.append(this.id);
        stringBuilder.append("}:");
        pw.println(stringBuilder.toString());
        pw.increaseIndent();
        pw.printPair("type", DebugUtils.valueToString(getClass(), "TYPE_", this.type));
        pw.printPair("diskId", getDiskId());
        pw.printPair("partGuid", this.partGuid);
        pw.printPair("mountFlags", DebugUtils.flagsToString(getClass(), "MOUNT_FLAG_", this.mountFlags));
        pw.printPair("mountUserId", Integer.valueOf(this.mountUserId));
        pw.printPair("state", DebugUtils.valueToString(getClass(), "STATE_", this.state));
        pw.println();
        pw.printPair("fsType", this.fsType);
        pw.printPair("fsUuid", this.fsUuid);
        pw.printPair("fsLabel", this.fsLabel);
        pw.println();
        pw.printPair("path", this.path);
        pw.printPair("internalPath", this.internalPath);
        pw.decreaseIndent();
        pw.println();
    }

    public VolumeInfo clone() {
        Parcel temp = Parcel.obtain();
        try {
            writeToParcel(temp, 0);
            temp.setDataPosition(0);
            VolumeInfo volumeInfo = (VolumeInfo) CREATOR.createFromParcel(temp);
            return volumeInfo;
        } finally {
            temp.recycle();
        }
    }

    public boolean equals(Object o) {
        if (o instanceof VolumeInfo) {
            return Objects.equals(this.id, ((VolumeInfo) o).id);
        }
        return false;
    }

    public int hashCode() {
        return this.id.hashCode();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.id);
        parcel.writeInt(this.type);
        if (this.disk != null) {
            parcel.writeInt(1);
            this.disk.writeToParcel(parcel, flags);
        } else {
            parcel.writeInt(0);
        }
        parcel.writeString(this.partGuid);
        parcel.writeInt(this.mountFlags);
        parcel.writeInt(this.mountUserId);
        parcel.writeInt(this.state);
        parcel.writeString(this.fsType);
        parcel.writeString(this.fsUuid);
        parcel.writeString(this.fsLabel);
        parcel.writeString(this.path);
        parcel.writeString(this.internalPath);
    }
}
