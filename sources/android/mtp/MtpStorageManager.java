package android.mtp;

import android.media.MediaFile;
import android.net.wifi.WifiEnterpriseConfig;
import android.os.FileObserver;
import android.os.storage.StorageVolume;
import android.provider.Telephony.BaseMmsColumns;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class MtpStorageManager {
    private static final int IN_IGNORED = 32768;
    private static final int IN_ISDIR = 1073741824;
    private static final int IN_ONLYDIR = 16777216;
    private static final int IN_Q_OVERFLOW = 16384;
    private static final String TAG = MtpStorageManager.class.getSimpleName();
    public static boolean sDebug = false;
    private volatile boolean mCheckConsistency = false;
    private Thread mConsistencyThread = new Thread(new -$$Lambda$MtpStorageManager$HocvlaKIXOtuA3p8uOP9PA7UJtw(this));
    private MtpNotifier mMtpNotifier;
    private int mNextObjectId = 1;
    private int mNextStorageId = 1;
    private HashMap<Integer, MtpObject> mObjects = new HashMap();
    private HashMap<Integer, MtpObject> mRoots = new HashMap();
    private Set<String> mSubdirectories;

    public static abstract class MtpNotifier {
        public abstract void sendObjectAdded(int i);

        public abstract void sendObjectInfoChanged(int i);

        public abstract void sendObjectRemoved(int i);
    }

    /* renamed from: android.mtp.MtpStorageManager$1 */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$android$mtp$MtpStorageManager$MtpObjectState = new int[MtpObjectState.values().length];

        static {
            try {
                $SwitchMap$android$mtp$MtpStorageManager$MtpObjectState[MtpObjectState.FROZEN.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$android$mtp$MtpStorageManager$MtpObjectState[MtpObjectState.FROZEN_REMOVED.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$android$mtp$MtpStorageManager$MtpObjectState[MtpObjectState.FROZEN_ONESHOT_ADD.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$android$mtp$MtpStorageManager$MtpObjectState[MtpObjectState.NORMAL.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$android$mtp$MtpStorageManager$MtpObjectState[MtpObjectState.FROZEN_ADDED.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$android$mtp$MtpStorageManager$MtpObjectState[MtpObjectState.FROZEN_ONESHOT_DEL.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    public static class MtpObject {
        private HashMap<String, MtpObject> mChildren;
        private int mId;
        private boolean mIsDir;
        private String mName;
        private FileObserver mObserver = null;
        private MtpOperation mOp;
        private MtpObject mParent;
        private MtpObjectState mState = MtpObjectState.NORMAL;
        private MtpStorage mStorage;
        private boolean mVisited = false;

        MtpObject(String name, int id, MtpStorage storage, MtpObject parent, boolean isDir) {
            this.mId = id;
            this.mName = name;
            this.mStorage = (MtpStorage) Preconditions.checkNotNull(storage);
            this.mParent = parent;
            HashMap hashMap = null;
            this.mIsDir = isDir;
            this.mOp = MtpOperation.NONE;
            if (this.mIsDir) {
                hashMap = new HashMap();
            }
            this.mChildren = hashMap;
        }

        public String getName() {
            return this.mName;
        }

        public int getId() {
            return this.mId;
        }

        public boolean isDir() {
            return this.mIsDir;
        }

        public int getFormat() {
            return this.mIsDir ? 12289 : MediaFile.getFormatCode(this.mName, null);
        }

        public int getStorageId() {
            return getRoot().getId();
        }

        public long getModifiedTime() {
            return getPath().toFile().lastModified() / 1000;
        }

        public MtpObject getParent() {
            return this.mParent;
        }

        public MtpObject getRoot() {
            return isRoot() ? this : this.mParent.getRoot();
        }

        public long getSize() {
            return this.mIsDir ? 0 : getPath().toFile().length();
        }

        public Path getPath() {
            return isRoot() ? Paths.get(this.mName, new String[0]) : this.mParent.getPath().resolve(this.mName);
        }

        public boolean isRoot() {
            return this.mParent == null;
        }

        public String getVolumeName() {
            return this.mStorage.getVolumeName();
        }

        private void setName(String name) {
            this.mName = name;
        }

        private void setId(int id) {
            this.mId = id;
        }

        private boolean isVisited() {
            return this.mVisited;
        }

        private void setParent(MtpObject parent) {
            this.mParent = parent;
        }

        private void setDir(boolean dir) {
            if (dir != this.mIsDir) {
                this.mIsDir = dir;
                this.mChildren = this.mIsDir ? new HashMap() : null;
            }
        }

        private void setVisited(boolean visited) {
            this.mVisited = visited;
        }

        private MtpObjectState getState() {
            return this.mState;
        }

        private void setState(MtpObjectState state) {
            this.mState = state;
            if (this.mState == MtpObjectState.NORMAL) {
                this.mOp = MtpOperation.NONE;
            }
        }

        private MtpOperation getOperation() {
            return this.mOp;
        }

        private void setOperation(MtpOperation op) {
            this.mOp = op;
        }

        private FileObserver getObserver() {
            return this.mObserver;
        }

        private void setObserver(FileObserver observer) {
            this.mObserver = observer;
        }

        private void addChild(MtpObject child) {
            this.mChildren.put(child.getName(), child);
        }

        private MtpObject getChild(String name) {
            return (MtpObject) this.mChildren.get(name);
        }

        private Collection<MtpObject> getChildren() {
            return this.mChildren.values();
        }

        private boolean exists() {
            return getPath().toFile().exists();
        }

        private MtpObject copy(boolean recursive) {
            MtpObject copy = new MtpObject(this.mName, this.mId, this.mStorage, this.mParent, this.mIsDir);
            copy.mIsDir = this.mIsDir;
            copy.mVisited = this.mVisited;
            copy.mState = this.mState;
            copy.mChildren = this.mIsDir ? new HashMap() : null;
            if (recursive && this.mIsDir) {
                for (MtpObject child : this.mChildren.values()) {
                    MtpObject childCopy = child.copy(true);
                    childCopy.setParent(copy);
                    copy.addChild(childCopy);
                }
            }
            return copy;
        }
    }

    private class MtpObjectObserver extends FileObserver {
        MtpObject mObject;

        MtpObjectObserver(MtpObject object) {
            super(object.getPath().toString(), 16778184);
            this.mObject = object;
        }

        /* JADX WARNING: Missing block: B:49:0x0145, code skipped:
            return;
     */
        public void onEvent(int r6, java.lang.String r7) {
            /*
            r5 = this;
            r0 = android.mtp.MtpStorageManager.this;
            monitor-enter(r0);
            r1 = r6 & 16384;
            if (r1 == 0) goto L_0x0010;
        L_0x0007:
            r1 = android.mtp.MtpStorageManager.TAG;	 Catch:{ all -> 0x0146 }
            r2 = "Received Inotify overflow event!";
            android.util.Log.e(r1, r2);	 Catch:{ all -> 0x0146 }
        L_0x0010:
            r1 = r5.mObject;	 Catch:{ all -> 0x0146 }
            r1 = r1.getChild(r7);	 Catch:{ all -> 0x0146 }
            r2 = r6 & 128;
            if (r2 != 0) goto L_0x0111;
        L_0x001a:
            r2 = r6 & 256;
            if (r2 == 0) goto L_0x0020;
        L_0x001e:
            goto L_0x0111;
        L_0x0020:
            r2 = r6 & 64;
            if (r2 != 0) goto L_0x00cb;
        L_0x0024:
            r2 = r6 & 512;
            if (r2 == 0) goto L_0x002a;
        L_0x0028:
            goto L_0x00cb;
        L_0x002a:
            r2 = 32768; // 0x8000 float:4.5918E-41 double:1.61895E-319;
            r2 = r2 & r6;
            if (r2 == 0) goto L_0x0071;
        L_0x0030:
            r2 = android.mtp.MtpStorageManager.sDebug;	 Catch:{ all -> 0x0146 }
            if (r2 == 0) goto L_0x0058;
        L_0x0034:
            r2 = android.mtp.MtpStorageManager.TAG;	 Catch:{ all -> 0x0146 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0146 }
            r3.<init>();	 Catch:{ all -> 0x0146 }
            r4 = "inotify for ";
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r4 = r5.mObject;	 Catch:{ all -> 0x0146 }
            r4 = r4.getPath();	 Catch:{ all -> 0x0146 }
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r4 = " deleted";
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r3 = r3.toString();	 Catch:{ all -> 0x0146 }
            android.util.Log.i(r2, r3);	 Catch:{ all -> 0x0146 }
        L_0x0058:
            r2 = r5.mObject;	 Catch:{ all -> 0x0146 }
            r2 = r2.mObserver;	 Catch:{ all -> 0x0146 }
            if (r2 == 0) goto L_0x0069;
        L_0x0060:
            r2 = r5.mObject;	 Catch:{ all -> 0x0146 }
            r2 = r2.mObserver;	 Catch:{ all -> 0x0146 }
            r2.stopWatching();	 Catch:{ all -> 0x0146 }
        L_0x0069:
            r2 = r5.mObject;	 Catch:{ all -> 0x0146 }
            r3 = 0;
            r2.mObserver = r3;	 Catch:{ all -> 0x0146 }
            goto L_0x0144;
        L_0x0071:
            r2 = r6 & 8;
            if (r2 == 0) goto L_0x00a9;
        L_0x0075:
            r2 = android.mtp.MtpStorageManager.sDebug;	 Catch:{ all -> 0x0146 }
            if (r2 == 0) goto L_0x00a0;
        L_0x0079:
            r2 = android.mtp.MtpStorageManager.TAG;	 Catch:{ all -> 0x0146 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0146 }
            r3.<init>();	 Catch:{ all -> 0x0146 }
            r4 = "inotify for ";
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r4 = r5.mObject;	 Catch:{ all -> 0x0146 }
            r4 = r4.getPath();	 Catch:{ all -> 0x0146 }
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r4 = " CLOSE_WRITE: ";
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r3.append(r7);	 Catch:{ all -> 0x0146 }
            r3 = r3.toString();	 Catch:{ all -> 0x0146 }
            android.util.Log.i(r2, r3);	 Catch:{ all -> 0x0146 }
        L_0x00a0:
            r2 = android.mtp.MtpStorageManager.this;	 Catch:{ all -> 0x0146 }
            r3 = r5.mObject;	 Catch:{ all -> 0x0146 }
            r2.handleChangedObject(r3, r7);	 Catch:{ all -> 0x0146 }
            goto L_0x0144;
        L_0x00a9:
            r2 = android.mtp.MtpStorageManager.TAG;	 Catch:{ all -> 0x0146 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0146 }
            r3.<init>();	 Catch:{ all -> 0x0146 }
            r4 = "Got unrecognized event ";
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r3.append(r7);	 Catch:{ all -> 0x0146 }
            r4 = " ";
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r3.append(r6);	 Catch:{ all -> 0x0146 }
            r3 = r3.toString();	 Catch:{ all -> 0x0146 }
            android.util.Log.w(r2, r3);	 Catch:{ all -> 0x0146 }
            goto L_0x0144;
        L_0x00cb:
            if (r1 != 0) goto L_0x00e7;
        L_0x00cd:
            r2 = android.mtp.MtpStorageManager.TAG;	 Catch:{ all -> 0x0146 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0146 }
            r3.<init>();	 Catch:{ all -> 0x0146 }
            r4 = "Object was null in event ";
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r3.append(r7);	 Catch:{ all -> 0x0146 }
            r3 = r3.toString();	 Catch:{ all -> 0x0146 }
            android.util.Log.w(r2, r3);	 Catch:{ all -> 0x0146 }
            monitor-exit(r0);	 Catch:{ all -> 0x0146 }
            return;
        L_0x00e7:
            r2 = android.mtp.MtpStorageManager.sDebug;	 Catch:{ all -> 0x0146 }
            if (r2 == 0) goto L_0x010b;
        L_0x00eb:
            r2 = android.mtp.MtpStorageManager.TAG;	 Catch:{ all -> 0x0146 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0146 }
            r3.<init>();	 Catch:{ all -> 0x0146 }
            r4 = "Got inotify removed event for ";
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r3.append(r7);	 Catch:{ all -> 0x0146 }
            r4 = " ";
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r3.append(r6);	 Catch:{ all -> 0x0146 }
            r3 = r3.toString();	 Catch:{ all -> 0x0146 }
            android.util.Log.i(r2, r3);	 Catch:{ all -> 0x0146 }
        L_0x010b:
            r2 = android.mtp.MtpStorageManager.this;	 Catch:{ all -> 0x0146 }
            r2.handleRemovedObject(r1);	 Catch:{ all -> 0x0146 }
            goto L_0x0144;
        L_0x0111:
            r2 = android.mtp.MtpStorageManager.sDebug;	 Catch:{ all -> 0x0146 }
            if (r2 == 0) goto L_0x0135;
        L_0x0115:
            r2 = android.mtp.MtpStorageManager.TAG;	 Catch:{ all -> 0x0146 }
            r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0146 }
            r3.<init>();	 Catch:{ all -> 0x0146 }
            r4 = "Got inotify added event for ";
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r3.append(r7);	 Catch:{ all -> 0x0146 }
            r4 = " ";
            r3.append(r4);	 Catch:{ all -> 0x0146 }
            r3.append(r6);	 Catch:{ all -> 0x0146 }
            r3 = r3.toString();	 Catch:{ all -> 0x0146 }
            android.util.Log.i(r2, r3);	 Catch:{ all -> 0x0146 }
        L_0x0135:
            r2 = android.mtp.MtpStorageManager.this;	 Catch:{ all -> 0x0146 }
            r3 = r5.mObject;	 Catch:{ all -> 0x0146 }
            r4 = 1073741824; // 0x40000000 float:2.0 double:5.304989477E-315;
            r4 = r4 & r6;
            if (r4 == 0) goto L_0x0140;
        L_0x013e:
            r4 = 1;
            goto L_0x0141;
        L_0x0140:
            r4 = 0;
        L_0x0141:
            r2.handleAddedObject(r3, r7, r4);	 Catch:{ all -> 0x0146 }
        L_0x0144:
            monitor-exit(r0);	 Catch:{ all -> 0x0146 }
            return;
        L_0x0146:
            r1 = move-exception;
            monitor-exit(r0);	 Catch:{ all -> 0x0146 }
            throw r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager$MtpObjectObserver.onEvent(int, java.lang.String):void");
        }

        public void finalize() {
        }
    }

    private enum MtpObjectState {
        NORMAL,
        FROZEN,
        FROZEN_ADDED,
        FROZEN_REMOVED,
        FROZEN_ONESHOT_ADD,
        FROZEN_ONESHOT_DEL
    }

    private enum MtpOperation {
        NONE,
        ADD,
        RENAME,
        COPY,
        DELETE
    }

    public MtpStorageManager(MtpNotifier notifier, Set<String> subdirectories) {
        this.mMtpNotifier = notifier;
        this.mSubdirectories = subdirectories;
        if (this.mCheckConsistency) {
            this.mConsistencyThread.start();
        }
    }

    public /* synthetic */ void lambda$new$0$MtpStorageManager() {
        while (this.mCheckConsistency) {
            try {
                Thread.sleep(15000);
                if (checkConsistency()) {
                    Log.v(TAG, "Cache is consistent");
                } else {
                    Log.w(TAG, "Cache is not consistent");
                }
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    public synchronized void close() {
        for (MtpObject obj : this.mObjects.values()) {
            if (obj.getObserver() != null) {
                obj.getObserver().stopWatching();
                obj.setObserver(null);
            }
        }
        for (MtpObject obj2 : this.mRoots.values()) {
            if (obj2.getObserver() != null) {
                obj2.getObserver().stopWatching();
                obj2.setObserver(null);
            }
        }
        if (this.mCheckConsistency) {
            this.mCheckConsistency = false;
            this.mConsistencyThread.interrupt();
            try {
                this.mConsistencyThread.join();
            } catch (InterruptedException e) {
            }
        }
    }

    public synchronized void setSubdirectories(Set<String> subDirs) {
        this.mSubdirectories = subDirs;
    }

    public synchronized MtpStorage addMtpStorage(StorageVolume volume) {
        MtpStorage storage;
        int storageId = ((getNextStorageId() & 65535) << 16) + 1;
        storage = new MtpStorage(volume, storageId);
        this.mRoots.put(Integer.valueOf(storageId), new MtpObject(storage.getPath(), storageId, storage, null, true));
        return storage;
    }

    public synchronized void removeMtpStorage(MtpStorage storage) {
        removeObjectFromCache(getStorageRoot(storage.getStorageId()), true, true);
    }

    private synchronized boolean isSpecialSubDir(MtpObject obj) {
        boolean z;
        z = (!obj.getParent().isRoot() || this.mSubdirectories == null || this.mSubdirectories.contains(obj.getName())) ? false : true;
        return z;
    }

    public synchronized MtpObject getByPath(String path) {
        MtpObject obj = null;
        for (MtpObject root : this.mRoots.values()) {
            if (path.startsWith(root.getName())) {
                obj = root;
                path = path.substring(root.getName().length());
            }
        }
        String[] split = path.split("/");
        int length = split.length;
        int i = 0;
        while (i < length) {
            String name = split[i];
            if (obj != null) {
                if (obj.isDir()) {
                    if (!"".equals(name)) {
                        if (!obj.isVisited()) {
                            getChildren(obj);
                        }
                        obj = obj.getChild(name);
                    }
                    i++;
                }
            }
            return null;
        }
        return obj;
    }

    public synchronized MtpObject getObject(int id) {
        if (id == 0 || id == -1) {
            Log.w(TAG, "Can't get root storages with getObject()");
            return null;
        } else if (this.mObjects.containsKey(Integer.valueOf(id))) {
            return (MtpObject) this.mObjects.get(Integer.valueOf(id));
        } else {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Id ");
            stringBuilder.append(id);
            stringBuilder.append(" doesn't exist");
            Log.w(str, stringBuilder.toString());
            return null;
        }
    }

    public MtpObject getStorageRoot(int id) {
        if (this.mRoots.containsKey(Integer.valueOf(id))) {
            return (MtpObject) this.mRoots.get(Integer.valueOf(id));
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("StorageId ");
        stringBuilder.append(id);
        stringBuilder.append(" doesn't exist");
        Log.w(str, stringBuilder.toString());
        return null;
    }

    private int getNextObjectId() {
        int ret = this.mNextObjectId;
        this.mNextObjectId = (int) (((long) this.mNextObjectId) + 1);
        return ret;
    }

    private int getNextStorageId() {
        int i = this.mNextStorageId;
        this.mNextStorageId = i + 1;
        return i;
    }

    /* JADX WARNING: Missing block: B:19:0x0035, code skipped:
            return r4;
     */
    /* JADX WARNING: Missing block: B:33:0x004e, code skipped:
            return r4;
     */
    public synchronized java.util.List<android.mtp.MtpStorageManager.MtpObject> getObjects(int r8, int r9, int r10) {
        /*
        r7 = this;
        monitor-enter(r7);
        if (r8 != 0) goto L_0x0005;
    L_0x0003:
        r0 = 1;
        goto L_0x0006;
    L_0x0005:
        r0 = 0;
    L_0x0006:
        r1 = new java.util.ArrayList;	 Catch:{ all -> 0x004f }
        r1.<init>();	 Catch:{ all -> 0x004f }
        r2 = 1;
        r3 = -1;
        if (r8 != r3) goto L_0x0010;
    L_0x000f:
        r8 = 0;
    L_0x0010:
        r4 = 0;
        if (r10 != r3) goto L_0x0036;
    L_0x0013:
        if (r8 != 0) goto L_0x0036;
    L_0x0015:
        r3 = r7.mRoots;	 Catch:{ all -> 0x004f }
        r3 = r3.values();	 Catch:{ all -> 0x004f }
        r3 = r3.iterator();	 Catch:{ all -> 0x004f }
    L_0x001f:
        r5 = r3.hasNext();	 Catch:{ all -> 0x004f }
        if (r5 == 0) goto L_0x0031;
    L_0x0025:
        r5 = r3.next();	 Catch:{ all -> 0x004f }
        r5 = (android.mtp.MtpStorageManager.MtpObject) r5;	 Catch:{ all -> 0x004f }
        r6 = r7.getObjects(r1, r5, r9, r0);	 Catch:{ all -> 0x004f }
        r2 = r2 & r6;
        goto L_0x001f;
    L_0x0031:
        if (r2 == 0) goto L_0x0034;
    L_0x0033:
        r4 = r1;
    L_0x0034:
        monitor-exit(r7);
        return r4;
    L_0x0036:
        if (r8 != 0) goto L_0x003d;
    L_0x0038:
        r3 = r7.getStorageRoot(r10);	 Catch:{ all -> 0x004f }
        goto L_0x0041;
    L_0x003d:
        r3 = r7.getObject(r8);	 Catch:{ all -> 0x004f }
    L_0x0041:
        if (r3 != 0) goto L_0x0045;
    L_0x0043:
        monitor-exit(r7);
        return r4;
    L_0x0045:
        r5 = r7.getObjects(r1, r3, r9, r0);	 Catch:{ all -> 0x004f }
        r2 = r5;
        if (r2 == 0) goto L_0x004d;
    L_0x004c:
        r4 = r1;
    L_0x004d:
        monitor-exit(r7);
        return r4;
    L_0x004f:
        r8 = move-exception;
        monitor-exit(r7);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.getObjects(int, int, int):java.util.List");
    }

    /* JADX WARNING: Missing block: B:28:0x0047, code skipped:
            return r1;
     */
    private synchronized boolean getObjects(java.util.List<android.mtp.MtpStorageManager.MtpObject> r6, android.mtp.MtpStorageManager.MtpObject r7, int r8, boolean r9) {
        /*
        r5 = this;
        monitor-enter(r5);
        r0 = r5.getChildren(r7);	 Catch:{ all -> 0x0048 }
        if (r0 != 0) goto L_0x000a;
    L_0x0007:
        r1 = 0;
        monitor-exit(r5);
        return r1;
    L_0x000a:
        r1 = r0.iterator();	 Catch:{ all -> 0x0048 }
    L_0x000e:
        r2 = r1.hasNext();	 Catch:{ all -> 0x0048 }
        if (r2 == 0) goto L_0x0026;
    L_0x0014:
        r2 = r1.next();	 Catch:{ all -> 0x0048 }
        r2 = (android.mtp.MtpStorageManager.MtpObject) r2;	 Catch:{ all -> 0x0048 }
        if (r8 == 0) goto L_0x0022;
    L_0x001c:
        r3 = r2.getFormat();	 Catch:{ all -> 0x0048 }
        if (r3 != r8) goto L_0x0025;
    L_0x0022:
        r6.add(r2);	 Catch:{ all -> 0x0048 }
    L_0x0025:
        goto L_0x000e;
    L_0x0026:
        r1 = 1;
        if (r9 == 0) goto L_0x0046;
    L_0x0029:
        r2 = r0.iterator();	 Catch:{ all -> 0x0048 }
    L_0x002d:
        r3 = r2.hasNext();	 Catch:{ all -> 0x0048 }
        if (r3 == 0) goto L_0x0046;
    L_0x0033:
        r3 = r2.next();	 Catch:{ all -> 0x0048 }
        r3 = (android.mtp.MtpStorageManager.MtpObject) r3;	 Catch:{ all -> 0x0048 }
        r4 = r3.isDir();	 Catch:{ all -> 0x0048 }
        if (r4 == 0) goto L_0x0045;
    L_0x003f:
        r4 = 1;
        r4 = r5.getObjects(r6, r3, r8, r4);	 Catch:{ all -> 0x0048 }
        r1 = r1 & r4;
    L_0x0045:
        goto L_0x002d;
    L_0x0046:
        monitor-exit(r5);
        return r1;
    L_0x0048:
        r6 = move-exception;
        monitor-exit(r5);
        throw r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.getObjects(java.util.List, android.mtp.MtpStorageManager$MtpObject, int, boolean):boolean");
    }

    /* JADX WARNING: Missing block: B:30:0x0067, code skipped:
            if (r2 != null) goto L_0x0069;
     */
    /* JADX WARNING: Missing block: B:32:?, code skipped:
            $closeResource(r3, r2);
     */
    private synchronized java.util.Collection<android.mtp.MtpStorageManager.MtpObject> getChildren(android.mtp.MtpStorageManager.MtpObject r8) {
        /*
        r7 = this;
        monitor-enter(r7);
        r0 = 0;
        if (r8 == 0) goto L_0x0089;
    L_0x0004:
        r1 = r8.isDir();	 Catch:{ all -> 0x00af }
        if (r1 != 0) goto L_0x000c;
    L_0x000a:
        goto L_0x0089;
    L_0x000c:
        r1 = r8.isVisited();	 Catch:{ all -> 0x00af }
        if (r1 != 0) goto L_0x0083;
    L_0x0012:
        r1 = r8.getPath();	 Catch:{ all -> 0x00af }
        r2 = r8.getObserver();	 Catch:{ all -> 0x00af }
        if (r2 == 0) goto L_0x0023;
    L_0x001c:
        r2 = TAG;	 Catch:{ all -> 0x00af }
        r3 = "Observer is not null!";
        android.util.Log.e(r2, r3);	 Catch:{ all -> 0x00af }
    L_0x0023:
        r2 = new android.mtp.MtpStorageManager$MtpObjectObserver;	 Catch:{ all -> 0x00af }
        r2.<init>(r8);	 Catch:{ all -> 0x00af }
        r8.setObserver(r2);	 Catch:{ all -> 0x00af }
        r2 = r8.getObserver();	 Catch:{ all -> 0x00af }
        r2.startWatching();	 Catch:{ all -> 0x00af }
        r2 = java.nio.file.Files.newDirectoryStream(r1);	 Catch:{ IOException | DirectoryIteratorException -> 0x006d, IOException | DirectoryIteratorException -> 0x006d }
        r3 = r2.iterator();	 Catch:{ all -> 0x0064 }
    L_0x003a:
        r4 = r3.hasNext();	 Catch:{ all -> 0x0064 }
        if (r4 == 0) goto L_0x005b;
    L_0x0040:
        r4 = r3.next();	 Catch:{ all -> 0x0064 }
        r4 = (java.nio.file.Path) r4;	 Catch:{ all -> 0x0064 }
        r5 = r4.getFileName();	 Catch:{ all -> 0x0064 }
        r5 = r5.toString();	 Catch:{ all -> 0x0064 }
        r6 = r4.toFile();	 Catch:{ all -> 0x0064 }
        r6 = r6.isDirectory();	 Catch:{ all -> 0x0064 }
        r7.addObjectToCache(r8, r5, r6);	 Catch:{ all -> 0x0064 }
        goto L_0x003a;
    L_0x005b:
        $closeResource(r0, r2);	 Catch:{ IOException | DirectoryIteratorException -> 0x006d, IOException | DirectoryIteratorException -> 0x006d }
        r0 = 1;
        r8.setVisited(r0);	 Catch:{ all -> 0x00af }
        goto L_0x0083;
    L_0x0064:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0066 }
    L_0x0066:
        r4 = move-exception;
        if (r2 == 0) goto L_0x006c;
    L_0x0069:
        $closeResource(r3, r2);	 Catch:{ IOException | DirectoryIteratorException -> 0x006d, IOException | DirectoryIteratorException -> 0x006d }
    L_0x006c:
        throw r4;	 Catch:{ IOException | DirectoryIteratorException -> 0x006d, IOException | DirectoryIteratorException -> 0x006d }
    L_0x006d:
        r2 = move-exception;
        r3 = TAG;	 Catch:{ all -> 0x00af }
        r4 = r2.toString();	 Catch:{ all -> 0x00af }
        android.util.Log.e(r3, r4);	 Catch:{ all -> 0x00af }
        r3 = r8.getObserver();	 Catch:{ all -> 0x00af }
        r3.stopWatching();	 Catch:{ all -> 0x00af }
        r8.setObserver(r0);	 Catch:{ all -> 0x00af }
        monitor-exit(r7);
        return r0;
    L_0x0083:
        r0 = r8.getChildren();	 Catch:{ all -> 0x00af }
        monitor-exit(r7);
        return r0;
    L_0x0089:
        r1 = TAG;	 Catch:{ all -> 0x00af }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00af }
        r2.<init>();	 Catch:{ all -> 0x00af }
        r3 = "Can't find children of ";
        r2.append(r3);	 Catch:{ all -> 0x00af }
        if (r8 != 0) goto L_0x009b;
    L_0x0097:
        r3 = "null";
        goto L_0x00a3;
    L_0x009b:
        r3 = r8.getId();	 Catch:{ all -> 0x00af }
        r3 = java.lang.Integer.valueOf(r3);	 Catch:{ all -> 0x00af }
    L_0x00a3:
        r2.append(r3);	 Catch:{ all -> 0x00af }
        r2 = r2.toString();	 Catch:{ all -> 0x00af }
        android.util.Log.w(r1, r2);	 Catch:{ all -> 0x00af }
        monitor-exit(r7);
        return r0;
    L_0x00af:
        r8 = move-exception;
        monitor-exit(r7);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.getChildren(android.mtp.MtpStorageManager$MtpObject):java.util.Collection");
    }

    private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
        if (x0 != null) {
            try {
                x1.close();
                return;
            } catch (Throwable th) {
                x0.addSuppressed(th);
                return;
            }
        }
        x1.close();
    }

    private synchronized MtpObject addObjectToCache(MtpObject parent, String newName, boolean isDir) {
        if (!parent.isRoot() && getObject(parent.getId()) != parent) {
            return null;
        }
        if (parent.getChild(newName) != null) {
            return null;
        }
        if (this.mSubdirectories != null && parent.isRoot() && !this.mSubdirectories.contains(newName)) {
            return null;
        }
        MtpObject obj = new MtpObject(newName, getNextObjectId(), parent.mStorage, parent, isDir);
        this.mObjects.put(Integer.valueOf(obj.getId()), obj);
        parent.addChild(obj);
        return obj;
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x005c  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0045  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x009a  */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x00c0  */
    private synchronized boolean removeObjectFromCache(android.mtp.MtpStorageManager.MtpObject r8, boolean r9, boolean r10) {
        /*
        r7 = this;
        monitor-enter(r7);
        r0 = r8.isRoot();	 Catch:{ all -> 0x00d5 }
        r1 = 0;
        r2 = 1;
        if (r0 != 0) goto L_0x001e;
    L_0x0009:
        r0 = r8.getParent();	 Catch:{ all -> 0x00d5 }
        r0 = r0.mChildren;	 Catch:{ all -> 0x00d5 }
        r3 = r8.getName();	 Catch:{ all -> 0x00d5 }
        r0 = r0.remove(r3, r8);	 Catch:{ all -> 0x00d5 }
        if (r0 == 0) goto L_0x001c;
    L_0x001b:
        goto L_0x001e;
    L_0x001c:
        r0 = r1;
        goto L_0x001f;
    L_0x001e:
        r0 = r2;
    L_0x001f:
        if (r0 != 0) goto L_0x003f;
    L_0x0021:
        r3 = sDebug;	 Catch:{ all -> 0x00d5 }
        if (r3 == 0) goto L_0x003f;
    L_0x0025:
        r3 = TAG;	 Catch:{ all -> 0x00d5 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d5 }
        r4.<init>();	 Catch:{ all -> 0x00d5 }
        r5 = "Failed to remove from parent ";
        r4.append(r5);	 Catch:{ all -> 0x00d5 }
        r5 = r8.getPath();	 Catch:{ all -> 0x00d5 }
        r4.append(r5);	 Catch:{ all -> 0x00d5 }
        r4 = r4.toString();	 Catch:{ all -> 0x00d5 }
        android.util.Log.w(r3, r4);	 Catch:{ all -> 0x00d5 }
    L_0x003f:
        r3 = r8.isRoot();	 Catch:{ all -> 0x00d5 }
        if (r3 == 0) goto L_0x005c;
    L_0x0045:
        r3 = r7.mRoots;	 Catch:{ all -> 0x00d5 }
        r4 = r8.getId();	 Catch:{ all -> 0x00d5 }
        r4 = java.lang.Integer.valueOf(r4);	 Catch:{ all -> 0x00d5 }
        r3 = r3.remove(r4, r8);	 Catch:{ all -> 0x00d5 }
        if (r3 == 0) goto L_0x0059;
    L_0x0055:
        if (r0 == 0) goto L_0x0059;
    L_0x0057:
        r3 = r2;
        goto L_0x005a;
    L_0x0059:
        r3 = r1;
    L_0x005a:
        r0 = r3;
        goto L_0x0074;
    L_0x005c:
        if (r9 == 0) goto L_0x0074;
    L_0x005e:
        r3 = r7.mObjects;	 Catch:{ all -> 0x00d5 }
        r4 = r8.getId();	 Catch:{ all -> 0x00d5 }
        r4 = java.lang.Integer.valueOf(r4);	 Catch:{ all -> 0x00d5 }
        r3 = r3.remove(r4, r8);	 Catch:{ all -> 0x00d5 }
        if (r3 == 0) goto L_0x0072;
    L_0x006e:
        if (r0 == 0) goto L_0x0072;
    L_0x0070:
        r3 = r2;
        goto L_0x0073;
    L_0x0072:
        r3 = r1;
    L_0x0073:
        r0 = r3;
    L_0x0074:
        if (r0 != 0) goto L_0x0094;
    L_0x0076:
        r3 = sDebug;	 Catch:{ all -> 0x00d5 }
        if (r3 == 0) goto L_0x0094;
    L_0x007a:
        r3 = TAG;	 Catch:{ all -> 0x00d5 }
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d5 }
        r4.<init>();	 Catch:{ all -> 0x00d5 }
        r5 = "Failed to remove from global cache ";
        r4.append(r5);	 Catch:{ all -> 0x00d5 }
        r5 = r8.getPath();	 Catch:{ all -> 0x00d5 }
        r4.append(r5);	 Catch:{ all -> 0x00d5 }
        r4 = r4.toString();	 Catch:{ all -> 0x00d5 }
        android.util.Log.w(r3, r4);	 Catch:{ all -> 0x00d5 }
    L_0x0094:
        r3 = r8.getObserver();	 Catch:{ all -> 0x00d5 }
        if (r3 == 0) goto L_0x00a5;
    L_0x009a:
        r3 = r8.getObserver();	 Catch:{ all -> 0x00d5 }
        r3.stopWatching();	 Catch:{ all -> 0x00d5 }
        r3 = 0;
        r8.setObserver(r3);	 Catch:{ all -> 0x00d5 }
    L_0x00a5:
        r3 = r8.isDir();	 Catch:{ all -> 0x00d5 }
        if (r3 == 0) goto L_0x00d3;
    L_0x00ab:
        if (r10 == 0) goto L_0x00d3;
    L_0x00ad:
        r3 = new java.util.ArrayList;	 Catch:{ all -> 0x00d5 }
        r4 = r8.getChildren();	 Catch:{ all -> 0x00d5 }
        r3.<init>(r4);	 Catch:{ all -> 0x00d5 }
        r4 = r3.iterator();	 Catch:{ all -> 0x00d5 }
    L_0x00ba:
        r5 = r4.hasNext();	 Catch:{ all -> 0x00d5 }
        if (r5 == 0) goto L_0x00d3;
    L_0x00c0:
        r5 = r4.next();	 Catch:{ all -> 0x00d5 }
        r5 = (android.mtp.MtpStorageManager.MtpObject) r5;	 Catch:{ all -> 0x00d5 }
        r6 = r7.removeObjectFromCache(r5, r9, r2);	 Catch:{ all -> 0x00d5 }
        if (r6 == 0) goto L_0x00d0;
    L_0x00cc:
        if (r0 == 0) goto L_0x00d0;
    L_0x00ce:
        r6 = r2;
        goto L_0x00d1;
    L_0x00d0:
        r6 = r1;
    L_0x00d1:
        r0 = r6;
        goto L_0x00ba;
    L_0x00d3:
        monitor-exit(r7);
        return r0;
    L_0x00d5:
        r8 = move-exception;
        monitor-exit(r7);
        throw r8;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.removeObjectFromCache(android.mtp.MtpStorageManager$MtpObject, boolean, boolean):boolean");
    }

    /* JADX WARNING: Missing block: B:21:0x0070, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:70:0x0142, code skipped:
            if (r3 != null) goto L_0x0144;
     */
    /* JADX WARNING: Missing block: B:72:?, code skipped:
            $closeResource(r4, r3);
     */
    /* JADX WARNING: Missing block: B:84:0x017f, code skipped:
            return;
     */
    private synchronized void handleAddedObject(android.mtp.MtpStorageManager.MtpObject r10, java.lang.String r11, boolean r12) {
        /*
        r9 = this;
        monitor-enter(r9);
        r0 = android.mtp.MtpStorageManager.MtpOperation.NONE;	 Catch:{ all -> 0x0180 }
        r1 = r10.getChild(r11);	 Catch:{ all -> 0x0180 }
        r2 = 1;
        if (r1 == 0) goto L_0x00a7;
    L_0x000a:
        r3 = r1.getState();	 Catch:{ all -> 0x0180 }
        r4 = r1.getOperation();	 Catch:{ all -> 0x0180 }
        r0 = r4;
        r4 = r1.isDir();	 Catch:{ all -> 0x0180 }
        if (r4 == r12) goto L_0x0037;
    L_0x0019:
        r4 = android.mtp.MtpStorageManager.MtpObjectState.FROZEN_REMOVED;	 Catch:{ all -> 0x0180 }
        if (r3 == r4) goto L_0x0037;
    L_0x001d:
        r4 = TAG;	 Catch:{ all -> 0x0180 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0180 }
        r5.<init>();	 Catch:{ all -> 0x0180 }
        r6 = "Inconsistent directory info! ";
        r5.append(r6);	 Catch:{ all -> 0x0180 }
        r6 = r1.getPath();	 Catch:{ all -> 0x0180 }
        r5.append(r6);	 Catch:{ all -> 0x0180 }
        r5 = r5.toString();	 Catch:{ all -> 0x0180 }
        android.util.Log.d(r4, r5);	 Catch:{ all -> 0x0180 }
    L_0x0037:
        r1.setDir(r12);	 Catch:{ all -> 0x0180 }
        r4 = android.mtp.MtpStorageManager.AnonymousClass1.$SwitchMap$android$mtp$MtpStorageManager$MtpObjectState;	 Catch:{ all -> 0x0180 }
        r5 = r3.ordinal();	 Catch:{ all -> 0x0180 }
        r4 = r4[r5];	 Catch:{ all -> 0x0180 }
        if (r4 == r2) goto L_0x0077;
    L_0x0044:
        r5 = 2;
        if (r4 == r5) goto L_0x0077;
    L_0x0047:
        r5 = 3;
        if (r4 == r5) goto L_0x0071;
    L_0x004a:
        r5 = 4;
        if (r4 == r5) goto L_0x006f;
    L_0x004d:
        r5 = 5;
        if (r4 == r5) goto L_0x006f;
    L_0x0050:
        r4 = TAG;	 Catch:{ all -> 0x0180 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0180 }
        r5.<init>();	 Catch:{ all -> 0x0180 }
        r6 = "Unexpected state in add ";
        r5.append(r6);	 Catch:{ all -> 0x0180 }
        r5.append(r11);	 Catch:{ all -> 0x0180 }
        r6 = " ";
        r5.append(r6);	 Catch:{ all -> 0x0180 }
        r5.append(r3);	 Catch:{ all -> 0x0180 }
        r5 = r5.toString();	 Catch:{ all -> 0x0180 }
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x0180 }
        goto L_0x007d;
    L_0x006f:
        monitor-exit(r9);
        return;
    L_0x0071:
        r4 = android.mtp.MtpStorageManager.MtpObjectState.NORMAL;	 Catch:{ all -> 0x0180 }
        r1.setState(r4);	 Catch:{ all -> 0x0180 }
        goto L_0x007d;
    L_0x0077:
        r4 = android.mtp.MtpStorageManager.MtpObjectState.FROZEN_ADDED;	 Catch:{ all -> 0x0180 }
        r1.setState(r4);	 Catch:{ all -> 0x0180 }
    L_0x007d:
        r4 = sDebug;	 Catch:{ all -> 0x0180 }
        if (r4 == 0) goto L_0x00a6;
    L_0x0081:
        r4 = TAG;	 Catch:{ all -> 0x0180 }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0180 }
        r5.<init>();	 Catch:{ all -> 0x0180 }
        r5.append(r3);	 Catch:{ all -> 0x0180 }
        r6 = " transitioned to ";
        r5.append(r6);	 Catch:{ all -> 0x0180 }
        r6 = r1.getState();	 Catch:{ all -> 0x0180 }
        r5.append(r6);	 Catch:{ all -> 0x0180 }
        r6 = " in op ";
        r5.append(r6);	 Catch:{ all -> 0x0180 }
        r5.append(r0);	 Catch:{ all -> 0x0180 }
        r5 = r5.toString();	 Catch:{ all -> 0x0180 }
        android.util.Log.i(r4, r5);	 Catch:{ all -> 0x0180 }
    L_0x00a6:
        goto L_0x00b7;
    L_0x00a7:
        r3 = r9.addObjectToCache(r10, r11, r12);	 Catch:{ all -> 0x0180 }
        r1 = r3;
        if (r1 == 0) goto L_0x015e;
    L_0x00ae:
        r3 = r9.mMtpNotifier;	 Catch:{ all -> 0x0180 }
        r4 = r1.getId();	 Catch:{ all -> 0x0180 }
        r3.sendObjectAdded(r4);	 Catch:{ all -> 0x0180 }
    L_0x00b7:
        if (r12 == 0) goto L_0x015c;
    L_0x00b9:
        r3 = android.mtp.MtpStorageManager.MtpOperation.RENAME;	 Catch:{ all -> 0x0180 }
        if (r0 != r3) goto L_0x00bf;
    L_0x00bd:
        monitor-exit(r9);
        return;
    L_0x00bf:
        r3 = android.mtp.MtpStorageManager.MtpOperation.COPY;	 Catch:{ all -> 0x0180 }
        if (r0 != r3) goto L_0x00cb;
    L_0x00c3:
        r3 = r1.isVisited();	 Catch:{ all -> 0x0180 }
        if (r3 != 0) goto L_0x00cb;
    L_0x00c9:
        monitor-exit(r9);
        return;
    L_0x00cb:
        r3 = r1.getObserver();	 Catch:{ all -> 0x0180 }
        if (r3 == 0) goto L_0x00da;
    L_0x00d1:
        r2 = TAG;	 Catch:{ all -> 0x0180 }
        r3 = "Observer is not null!";
        android.util.Log.e(r2, r3);	 Catch:{ all -> 0x0180 }
        monitor-exit(r9);
        return;
    L_0x00da:
        r3 = new android.mtp.MtpStorageManager$MtpObjectObserver;	 Catch:{ all -> 0x0180 }
        r3.<init>(r1);	 Catch:{ all -> 0x0180 }
        r1.setObserver(r3);	 Catch:{ all -> 0x0180 }
        r3 = r1.getObserver();	 Catch:{ all -> 0x0180 }
        r3.startWatching();	 Catch:{ all -> 0x0180 }
        r1.setVisited(r2);	 Catch:{ all -> 0x0180 }
        r2 = 0;
        r3 = r1.getPath();	 Catch:{ IOException | DirectoryIteratorException -> 0x0148, IOException | DirectoryIteratorException -> 0x0148 }
        r3 = java.nio.file.Files.newDirectoryStream(r3);	 Catch:{ IOException | DirectoryIteratorException -> 0x0148, IOException | DirectoryIteratorException -> 0x0148 }
        r4 = r3.iterator();	 Catch:{ all -> 0x013f }
    L_0x00f9:
        r5 = r4.hasNext();	 Catch:{ all -> 0x013f }
        if (r5 == 0) goto L_0x013b;
    L_0x00ff:
        r5 = r4.next();	 Catch:{ all -> 0x013f }
        r5 = (java.nio.file.Path) r5;	 Catch:{ all -> 0x013f }
        r6 = sDebug;	 Catch:{ all -> 0x013f }
        if (r6 == 0) goto L_0x0127;
    L_0x0109:
        r6 = TAG;	 Catch:{ all -> 0x013f }
        r7 = new java.lang.StringBuilder;	 Catch:{ all -> 0x013f }
        r7.<init>();	 Catch:{ all -> 0x013f }
        r8 = "Manually handling event for ";
        r7.append(r8);	 Catch:{ all -> 0x013f }
        r8 = r5.getFileName();	 Catch:{ all -> 0x013f }
        r8 = r8.toString();	 Catch:{ all -> 0x013f }
        r7.append(r8);	 Catch:{ all -> 0x013f }
        r7 = r7.toString();	 Catch:{ all -> 0x013f }
        android.util.Log.i(r6, r7);	 Catch:{ all -> 0x013f }
    L_0x0127:
        r6 = r5.getFileName();	 Catch:{ all -> 0x013f }
        r6 = r6.toString();	 Catch:{ all -> 0x013f }
        r7 = r5.toFile();	 Catch:{ all -> 0x013f }
        r7 = r7.isDirectory();	 Catch:{ all -> 0x013f }
        r9.handleAddedObject(r1, r6, r7);	 Catch:{ all -> 0x013f }
        goto L_0x00f9;
    L_0x013b:
        $closeResource(r2, r3);	 Catch:{ IOException | DirectoryIteratorException -> 0x0148, IOException | DirectoryIteratorException -> 0x0148 }
        goto L_0x015c;
    L_0x013f:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x0141 }
    L_0x0141:
        r5 = move-exception;
        if (r3 == 0) goto L_0x0147;
    L_0x0144:
        $closeResource(r4, r3);	 Catch:{ IOException | DirectoryIteratorException -> 0x0148, IOException | DirectoryIteratorException -> 0x0148 }
    L_0x0147:
        throw r5;	 Catch:{ IOException | DirectoryIteratorException -> 0x0148, IOException | DirectoryIteratorException -> 0x0148 }
    L_0x0148:
        r3 = move-exception;
        r4 = TAG;	 Catch:{ all -> 0x0180 }
        r5 = r3.toString();	 Catch:{ all -> 0x0180 }
        android.util.Log.e(r4, r5);	 Catch:{ all -> 0x0180 }
        r4 = r1.getObserver();	 Catch:{ all -> 0x0180 }
        r4.stopWatching();	 Catch:{ all -> 0x0180 }
        r1.setObserver(r2);	 Catch:{ all -> 0x0180 }
    L_0x015c:
        monitor-exit(r9);
        return;
    L_0x015e:
        r2 = sDebug;	 Catch:{ all -> 0x0180 }
        if (r2 == 0) goto L_0x017e;
    L_0x0162:
        r2 = TAG;	 Catch:{ all -> 0x0180 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0180 }
        r3.<init>();	 Catch:{ all -> 0x0180 }
        r4 = "object ";
        r3.append(r4);	 Catch:{ all -> 0x0180 }
        r3.append(r11);	 Catch:{ all -> 0x0180 }
        r4 = " already exists";
        r3.append(r4);	 Catch:{ all -> 0x0180 }
        r3 = r3.toString();	 Catch:{ all -> 0x0180 }
        android.util.Log.w(r2, r3);	 Catch:{ all -> 0x0180 }
    L_0x017e:
        monitor-exit(r9);
        return;
    L_0x0180:
        r10 = move-exception;
        monitor-exit(r9);
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.handleAddedObject(android.mtp.MtpStorageManager$MtpObject, java.lang.String, boolean):void");
    }

    private synchronized void handleRemovedObject(MtpObject obj) {
        String str;
        StringBuilder stringBuilder;
        MtpObjectState state = obj.getState();
        MtpOperation op = obj.getOperation();
        int i = AnonymousClass1.$SwitchMap$android$mtp$MtpStorageManager$MtpObjectState[state.ordinal()];
        boolean z = true;
        if (i == 1) {
            obj.setState(MtpObjectState.FROZEN_REMOVED);
        } else if (i != 4) {
            if (i == 5) {
                obj.setState(MtpObjectState.FROZEN_REMOVED);
            } else if (i != 6) {
                str = TAG;
                stringBuilder = new StringBuilder();
                stringBuilder.append("Got unexpected object remove for ");
                stringBuilder.append(obj.getName());
                Log.e(str, stringBuilder.toString());
            } else {
                if (op == MtpOperation.RENAME) {
                    z = false;
                }
                removeObjectFromCache(obj, z, false);
            }
        } else if (removeObjectFromCache(obj, true, true)) {
            this.mMtpNotifier.sendObjectRemoved(obj.getId());
        }
        if (sDebug) {
            str = TAG;
            stringBuilder = new StringBuilder();
            stringBuilder.append(state);
            stringBuilder.append(" transitioned to ");
            stringBuilder.append(obj.getState());
            stringBuilder.append(" in op ");
            stringBuilder.append(op);
            Log.i(str, stringBuilder.toString());
        }
    }

    private synchronized void handleChangedObject(MtpObject parent, String path) {
        MtpOperation op = MtpOperation.NONE;
        MtpObject obj = parent.getChild(path);
        if (obj != null) {
            if (!obj.isDir() && obj.getSize() > 0) {
                MtpObjectState state = obj.getState();
                op = obj.getOperation();
                this.mMtpNotifier.sendObjectInfoChanged(obj.getId());
                if (sDebug) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("sendObjectInfoChanged: id=");
                    stringBuilder.append(obj.getId());
                    stringBuilder.append(",size=");
                    stringBuilder.append(obj.getSize());
                    Log.d(str, stringBuilder.toString());
                }
            }
        } else if (sDebug) {
            String str2 = TAG;
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("object ");
            stringBuilder2.append(path);
            stringBuilder2.append(" null");
            Log.w(str2, stringBuilder2.toString());
        }
    }

    public void flushEvents() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
        }
    }

    public synchronized void dump() {
        for (Integer key : this.mObjects.keySet()) {
            int key2 = key.intValue();
            MtpObject obj = (MtpObject) this.mObjects.get(Integer.valueOf(key2));
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(key2);
            stringBuilder.append(" | ");
            stringBuilder.append(obj.getParent() == null ? Integer.valueOf(obj.getParent().getId()) : "null");
            stringBuilder.append(" | ");
            stringBuilder.append(obj.getName());
            stringBuilder.append(" | ");
            stringBuilder.append(obj.isDir() ? "dir" : "obj");
            stringBuilder.append(" | ");
            stringBuilder.append(obj.isVisited() ? BaseMmsColumns.MMS_VERSION : "nv");
            stringBuilder.append(" | ");
            stringBuilder.append(obj.getState());
            Log.i(str, stringBuilder.toString());
        }
    }

    /* JADX WARNING: Missing block: B:88:0x02e5, code skipped:
            if (r4 != null) goto L_0x02e7;
     */
    /* JADX WARNING: Missing block: B:90:?, code skipped:
            $closeResource(r5, r4);
     */
    public synchronized boolean checkConsistency() {
        /*
        r12 = this;
        monitor-enter(r12);
        r0 = new java.util.ArrayList;	 Catch:{ all -> 0x02fa }
        r0.<init>();	 Catch:{ all -> 0x02fa }
        r1 = r12.mRoots;	 Catch:{ all -> 0x02fa }
        r1 = r1.values();	 Catch:{ all -> 0x02fa }
        r0.addAll(r1);	 Catch:{ all -> 0x02fa }
        r1 = r12.mObjects;	 Catch:{ all -> 0x02fa }
        r1 = r1.values();	 Catch:{ all -> 0x02fa }
        r0.addAll(r1);	 Catch:{ all -> 0x02fa }
        r1 = 1;
        r2 = r0.iterator();	 Catch:{ all -> 0x02fa }
    L_0x001d:
        r3 = r2.hasNext();	 Catch:{ all -> 0x02fa }
        if (r3 == 0) goto L_0x02f8;
    L_0x0023:
        r3 = r2.next();	 Catch:{ all -> 0x02fa }
        r3 = (android.mtp.MtpStorageManager.MtpObject) r3;	 Catch:{ all -> 0x02fa }
        r4 = r3.exists();	 Catch:{ all -> 0x02fa }
        if (r4 != 0) goto L_0x0056;
    L_0x002f:
        r4 = TAG;	 Catch:{ all -> 0x02fa }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02fa }
        r5.<init>();	 Catch:{ all -> 0x02fa }
        r6 = "Object doesn't exist ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.getPath();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = " ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.getId();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r5 = r5.toString();	 Catch:{ all -> 0x02fa }
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x02fa }
        r1 = 0;
    L_0x0056:
        r4 = r3.getState();	 Catch:{ all -> 0x02fa }
        r5 = android.mtp.MtpStorageManager.MtpObjectState.NORMAL;	 Catch:{ all -> 0x02fa }
        if (r4 == r5) goto L_0x0085;
    L_0x005e:
        r4 = TAG;	 Catch:{ all -> 0x02fa }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02fa }
        r5.<init>();	 Catch:{ all -> 0x02fa }
        r6 = "Object ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.getPath();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = " in state ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.getState();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r5 = r5.toString();	 Catch:{ all -> 0x02fa }
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x02fa }
        r1 = 0;
    L_0x0085:
        r4 = r3.getOperation();	 Catch:{ all -> 0x02fa }
        r5 = android.mtp.MtpStorageManager.MtpOperation.NONE;	 Catch:{ all -> 0x02fa }
        if (r4 == r5) goto L_0x00b4;
    L_0x008d:
        r4 = TAG;	 Catch:{ all -> 0x02fa }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02fa }
        r5.<init>();	 Catch:{ all -> 0x02fa }
        r6 = "Object ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.getPath();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = " in operation ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.getOperation();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r5 = r5.toString();	 Catch:{ all -> 0x02fa }
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x02fa }
        r1 = 0;
    L_0x00b4:
        r4 = r3.isRoot();	 Catch:{ all -> 0x02fa }
        if (r4 != 0) goto L_0x00ea;
    L_0x00ba:
        r4 = r12.mObjects;	 Catch:{ all -> 0x02fa }
        r5 = r3.getId();	 Catch:{ all -> 0x02fa }
        r5 = java.lang.Integer.valueOf(r5);	 Catch:{ all -> 0x02fa }
        r4 = r4.get(r5);	 Catch:{ all -> 0x02fa }
        if (r4 == r3) goto L_0x00ea;
    L_0x00ca:
        r4 = TAG;	 Catch:{ all -> 0x02fa }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02fa }
        r5.<init>();	 Catch:{ all -> 0x02fa }
        r6 = "Object ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.getPath();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = " is not in map correctly";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r5 = r5.toString();	 Catch:{ all -> 0x02fa }
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x02fa }
        r1 = 0;
    L_0x00ea:
        r4 = r3.getParent();	 Catch:{ all -> 0x02fa }
        if (r4 == 0) goto L_0x0193;
    L_0x00f0:
        r4 = r3.getParent();	 Catch:{ all -> 0x02fa }
        r4 = r4.isRoot();	 Catch:{ all -> 0x02fa }
        if (r4 == 0) goto L_0x012d;
    L_0x00fa:
        r4 = r3.getParent();	 Catch:{ all -> 0x02fa }
        r5 = r12.mRoots;	 Catch:{ all -> 0x02fa }
        r6 = r3.getParent();	 Catch:{ all -> 0x02fa }
        r6 = r6.getId();	 Catch:{ all -> 0x02fa }
        r6 = java.lang.Integer.valueOf(r6);	 Catch:{ all -> 0x02fa }
        r5 = r5.get(r6);	 Catch:{ all -> 0x02fa }
        if (r4 == r5) goto L_0x012d;
    L_0x0112:
        r4 = TAG;	 Catch:{ all -> 0x02fa }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02fa }
        r5.<init>();	 Catch:{ all -> 0x02fa }
        r6 = "Root parent is not in root mapping ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.getPath();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r5 = r5.toString();	 Catch:{ all -> 0x02fa }
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x02fa }
        r1 = 0;
    L_0x012d:
        r4 = r3.getParent();	 Catch:{ all -> 0x02fa }
        r4 = r4.isRoot();	 Catch:{ all -> 0x02fa }
        if (r4 != 0) goto L_0x016a;
    L_0x0137:
        r4 = r3.getParent();	 Catch:{ all -> 0x02fa }
        r5 = r12.mObjects;	 Catch:{ all -> 0x02fa }
        r6 = r3.getParent();	 Catch:{ all -> 0x02fa }
        r6 = r6.getId();	 Catch:{ all -> 0x02fa }
        r6 = java.lang.Integer.valueOf(r6);	 Catch:{ all -> 0x02fa }
        r5 = r5.get(r6);	 Catch:{ all -> 0x02fa }
        if (r4 == r5) goto L_0x016a;
    L_0x014f:
        r4 = TAG;	 Catch:{ all -> 0x02fa }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02fa }
        r5.<init>();	 Catch:{ all -> 0x02fa }
        r6 = "Parent is not in object mapping ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.getPath();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r5 = r5.toString();	 Catch:{ all -> 0x02fa }
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x02fa }
        r1 = 0;
    L_0x016a:
        r4 = r3.getParent();	 Catch:{ all -> 0x02fa }
        r5 = r3.getName();	 Catch:{ all -> 0x02fa }
        r4 = r4.getChild(r5);	 Catch:{ all -> 0x02fa }
        if (r4 == r3) goto L_0x0193;
    L_0x0178:
        r4 = TAG;	 Catch:{ all -> 0x02fa }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02fa }
        r5.<init>();	 Catch:{ all -> 0x02fa }
        r6 = "Child does not exist in parent ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.getPath();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r5 = r5.toString();	 Catch:{ all -> 0x02fa }
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x02fa }
        r1 = 0;
    L_0x0193:
        r4 = r3.isDir();	 Catch:{ all -> 0x02fa }
        if (r4 == 0) goto L_0x02f6;
    L_0x0199:
        r4 = r3.isVisited();	 Catch:{ all -> 0x02fa }
        r5 = r3.getObserver();	 Catch:{ all -> 0x02fa }
        if (r5 != 0) goto L_0x01a5;
    L_0x01a3:
        r5 = 1;
        goto L_0x01a6;
    L_0x01a5:
        r5 = 0;
    L_0x01a6:
        if (r4 != r5) goto L_0x01de;
    L_0x01a8:
        r4 = TAG;	 Catch:{ all -> 0x02fa }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02fa }
        r5.<init>();	 Catch:{ all -> 0x02fa }
        r6 = r3.getPath();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = " is ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.isVisited();	 Catch:{ all -> 0x02fa }
        if (r6 == 0) goto L_0x01c4;
    L_0x01c1:
        r6 = "";
        goto L_0x01c7;
    L_0x01c4:
        r6 = "not ";
    L_0x01c7:
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = " visited but observer is ";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = r3.getObserver();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r5 = r5.toString();	 Catch:{ all -> 0x02fa }
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x02fa }
        r1 = 0;
    L_0x01de:
        r4 = r3.isVisited();	 Catch:{ all -> 0x02fa }
        if (r4 != 0) goto L_0x0209;
    L_0x01e4:
        r4 = r3.getChildren();	 Catch:{ all -> 0x02fa }
        r4 = r4.size();	 Catch:{ all -> 0x02fa }
        if (r4 <= 0) goto L_0x0209;
    L_0x01ee:
        r4 = TAG;	 Catch:{ all -> 0x02fa }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02fa }
        r5.<init>();	 Catch:{ all -> 0x02fa }
        r6 = r3.getPath();	 Catch:{ all -> 0x02fa }
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r6 = " is not visited but has children";
        r5.append(r6);	 Catch:{ all -> 0x02fa }
        r5 = r5.toString();	 Catch:{ all -> 0x02fa }
        android.util.Log.w(r4, r5);	 Catch:{ all -> 0x02fa }
        r1 = 0;
    L_0x0209:
        r4 = r3.getPath();	 Catch:{ IOException | DirectoryIteratorException -> 0x02eb, IOException | DirectoryIteratorException -> 0x02eb }
        r4 = java.nio.file.Files.newDirectoryStream(r4);	 Catch:{ IOException | DirectoryIteratorException -> 0x02eb, IOException | DirectoryIteratorException -> 0x02eb }
        r5 = 0;
        r6 = new java.util.HashSet;	 Catch:{ all -> 0x02e2 }
        r6.<init>();	 Catch:{ all -> 0x02e2 }
        r7 = r4.iterator();	 Catch:{ all -> 0x02e2 }
    L_0x021b:
        r8 = r7.hasNext();	 Catch:{ all -> 0x02e2 }
        if (r8 == 0) goto L_0x0275;
    L_0x0221:
        r8 = r7.next();	 Catch:{ all -> 0x02e2 }
        r8 = (java.nio.file.Path) r8;	 Catch:{ all -> 0x02e2 }
        r9 = r3.isVisited();	 Catch:{ all -> 0x02e2 }
        if (r9 == 0) goto L_0x026c;
    L_0x022d:
        r9 = r8.getFileName();	 Catch:{ all -> 0x02e2 }
        r9 = r9.toString();	 Catch:{ all -> 0x02e2 }
        r9 = r3.getChild(r9);	 Catch:{ all -> 0x02e2 }
        if (r9 != 0) goto L_0x026c;
    L_0x023b:
        r9 = r12.mSubdirectories;	 Catch:{ all -> 0x02e2 }
        if (r9 == 0) goto L_0x0255;
    L_0x023f:
        r9 = r3.isRoot();	 Catch:{ all -> 0x02e2 }
        if (r9 == 0) goto L_0x0255;
    L_0x0245:
        r9 = r12.mSubdirectories;	 Catch:{ all -> 0x02e2 }
        r10 = r8.getFileName();	 Catch:{ all -> 0x02e2 }
        r10 = r10.toString();	 Catch:{ all -> 0x02e2 }
        r9 = r9.contains(r10);	 Catch:{ all -> 0x02e2 }
        if (r9 == 0) goto L_0x026c;
    L_0x0255:
        r9 = TAG;	 Catch:{ all -> 0x02e2 }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02e2 }
        r10.<init>();	 Catch:{ all -> 0x02e2 }
        r11 = "File exists in fs but not in children ";
        r10.append(r11);	 Catch:{ all -> 0x02e2 }
        r10.append(r8);	 Catch:{ all -> 0x02e2 }
        r10 = r10.toString();	 Catch:{ all -> 0x02e2 }
        android.util.Log.w(r9, r10);	 Catch:{ all -> 0x02e2 }
        r1 = 0;
    L_0x026c:
        r9 = r8.toString();	 Catch:{ all -> 0x02e2 }
        r6.add(r9);	 Catch:{ all -> 0x02e2 }
        goto L_0x021b;
    L_0x0275:
        r7 = r3.getChildren();	 Catch:{ all -> 0x02e2 }
        r7 = r7.iterator();	 Catch:{ all -> 0x02e2 }
    L_0x027d:
        r8 = r7.hasNext();	 Catch:{ all -> 0x02e2 }
        if (r8 == 0) goto L_0x02de;
    L_0x0283:
        r8 = r7.next();	 Catch:{ all -> 0x02e2 }
        r8 = (android.mtp.MtpStorageManager.MtpObject) r8;	 Catch:{ all -> 0x02e2 }
        r9 = r8.getPath();	 Catch:{ all -> 0x02e2 }
        r9 = r9.toString();	 Catch:{ all -> 0x02e2 }
        r9 = r6.contains(r9);	 Catch:{ all -> 0x02e2 }
        if (r9 != 0) goto L_0x02b2;
    L_0x0297:
        r9 = TAG;	 Catch:{ all -> 0x02e2 }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02e2 }
        r10.<init>();	 Catch:{ all -> 0x02e2 }
        r11 = "File in children doesn't exist in fs ";
        r10.append(r11);	 Catch:{ all -> 0x02e2 }
        r11 = r8.getPath();	 Catch:{ all -> 0x02e2 }
        r10.append(r11);	 Catch:{ all -> 0x02e2 }
        r10 = r10.toString();	 Catch:{ all -> 0x02e2 }
        android.util.Log.w(r9, r10);	 Catch:{ all -> 0x02e2 }
        r1 = 0;
    L_0x02b2:
        r9 = r12.mObjects;	 Catch:{ all -> 0x02e2 }
        r10 = r8.getId();	 Catch:{ all -> 0x02e2 }
        r10 = java.lang.Integer.valueOf(r10);	 Catch:{ all -> 0x02e2 }
        r9 = r9.get(r10);	 Catch:{ all -> 0x02e2 }
        if (r8 == r9) goto L_0x02dd;
    L_0x02c2:
        r9 = TAG;	 Catch:{ all -> 0x02e2 }
        r10 = new java.lang.StringBuilder;	 Catch:{ all -> 0x02e2 }
        r10.<init>();	 Catch:{ all -> 0x02e2 }
        r11 = "Child is not in object map ";
        r10.append(r11);	 Catch:{ all -> 0x02e2 }
        r11 = r8.getPath();	 Catch:{ all -> 0x02e2 }
        r10.append(r11);	 Catch:{ all -> 0x02e2 }
        r10 = r10.toString();	 Catch:{ all -> 0x02e2 }
        android.util.Log.w(r9, r10);	 Catch:{ all -> 0x02e2 }
        r1 = 0;
    L_0x02dd:
        goto L_0x027d;
    L_0x02de:
        $closeResource(r5, r4);	 Catch:{ IOException | DirectoryIteratorException -> 0x02eb, IOException | DirectoryIteratorException -> 0x02eb }
        goto L_0x02f6;
    L_0x02e2:
        r5 = move-exception;
        throw r5;	 Catch:{ all -> 0x02e4 }
    L_0x02e4:
        r6 = move-exception;
        if (r4 == 0) goto L_0x02ea;
    L_0x02e7:
        $closeResource(r5, r4);	 Catch:{ IOException | DirectoryIteratorException -> 0x02eb, IOException | DirectoryIteratorException -> 0x02eb }
    L_0x02ea:
        throw r6;	 Catch:{ IOException | DirectoryIteratorException -> 0x02eb, IOException | DirectoryIteratorException -> 0x02eb }
    L_0x02eb:
        r4 = move-exception;
        r5 = TAG;	 Catch:{ all -> 0x02fa }
        r6 = r4.toString();	 Catch:{ all -> 0x02fa }
        android.util.Log.w(r5, r6);	 Catch:{ all -> 0x02fa }
        r1 = 0;
    L_0x02f6:
        goto L_0x001d;
    L_0x02f8:
        monitor-exit(r12);
        return r1;
    L_0x02fa:
        r0 = move-exception;
        monitor-exit(r12);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.checkConsistency():boolean");
    }

    public synchronized int beginSendObject(MtpObject parent, String name, int format) {
        if (sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("beginSendObject ");
            stringBuilder.append(name);
            Log.v(str, stringBuilder.toString());
        }
        if (!parent.isDir()) {
            return -1;
        }
        if (parent.isRoot() && this.mSubdirectories != null && !this.mSubdirectories.contains(name)) {
            return -1;
        }
        getChildren(parent);
        MtpObject obj = addObjectToCache(parent, name, format == 12289 ? true : null);
        if (obj == null) {
            return -1;
        }
        obj.setState(MtpObjectState.FROZEN);
        obj.setOperation(MtpOperation.ADD);
        return obj.getId();
    }

    public synchronized boolean endSendObject(MtpObject obj, boolean succeeded) {
        if (sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("endSendObject ");
            stringBuilder.append(succeeded);
            Log.v(str, stringBuilder.toString());
        }
        return generalEndAddObject(obj, succeeded, true);
    }

    public synchronized boolean beginRenameObject(MtpObject obj, String newName) {
        if (sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("beginRenameObject ");
            stringBuilder.append(obj.getName());
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(newName);
            Log.v(str, stringBuilder.toString());
        }
        if (obj.isRoot()) {
            return false;
        }
        if (isSpecialSubDir(obj)) {
            return false;
        }
        if (obj.getParent().getChild(newName) != null) {
            return false;
        }
        MtpObject oldObj = obj.copy(false);
        obj.setName(newName);
        obj.getParent().addChild(obj);
        oldObj.getParent().addChild(oldObj);
        return generalBeginRenameObject(oldObj, obj);
    }

    public synchronized boolean endRenameObject(MtpObject obj, String oldName, boolean success) {
        MtpObject oldObj;
        if (sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("endRenameObject ");
            stringBuilder.append(success);
            Log.v(str, stringBuilder.toString());
        }
        MtpObject parent = obj.getParent();
        oldObj = parent.getChild(oldName);
        if (!success) {
            MtpObject temp = oldObj;
            MtpObjectState oldState = oldObj.getState();
            temp.setName(obj.getName());
            temp.setState(obj.getState());
            oldObj = obj;
            oldObj.setName(oldName);
            oldObj.setState(oldState);
            obj = temp;
            parent.addChild(obj);
            parent.addChild(oldObj);
        }
        return generalEndRenameObject(oldObj, obj, success);
    }

    public synchronized boolean beginRemoveObject(MtpObject obj) {
        boolean z;
        if (sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("beginRemoveObject ");
            stringBuilder.append(obj.getName());
            Log.v(str, stringBuilder.toString());
        }
        z = (obj.isRoot() || isSpecialSubDir(obj) || !generalBeginRemoveObject(obj, MtpOperation.DELETE)) ? false : true;
        return z;
    }

    public synchronized boolean endRemoveObject(MtpObject obj, boolean success) {
        boolean z;
        if (sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("endRemoveObject ");
            stringBuilder.append(success);
            Log.v(str, stringBuilder.toString());
        }
        boolean ret = true;
        z = false;
        if (obj.isDir()) {
            Iterator it = new ArrayList(obj.getChildren()).iterator();
            while (it.hasNext()) {
                MtpObject child = (MtpObject) it.next();
                if (child.getOperation() == MtpOperation.DELETE) {
                    boolean z2 = endRemoveObject(child, success) && ret;
                    ret = z2;
                }
            }
        }
        if (generalEndRemoveObject(obj, success, true) && ret) {
            z = true;
        }
        return z;
    }

    /* JADX WARNING: Missing block: B:29:0x0065, code skipped:
            return r0;
     */
    public synchronized boolean beginMoveObject(android.mtp.MtpStorageManager.MtpObject r5, android.mtp.MtpStorageManager.MtpObject r6) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = sDebug;	 Catch:{ all -> 0x0081 }
        if (r0 == 0) goto L_0x001f;
    L_0x0005:
        r0 = TAG;	 Catch:{ all -> 0x0081 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0081 }
        r1.<init>();	 Catch:{ all -> 0x0081 }
        r2 = "beginMoveObject ";
        r1.append(r2);	 Catch:{ all -> 0x0081 }
        r2 = r6.getPath();	 Catch:{ all -> 0x0081 }
        r1.append(r2);	 Catch:{ all -> 0x0081 }
        r1 = r1.toString();	 Catch:{ all -> 0x0081 }
        android.util.Log.v(r0, r1);	 Catch:{ all -> 0x0081 }
    L_0x001f:
        r0 = r5.isRoot();	 Catch:{ all -> 0x0081 }
        r1 = 0;
        if (r0 == 0) goto L_0x0028;
    L_0x0026:
        monitor-exit(r4);
        return r1;
    L_0x0028:
        r0 = r4.isSpecialSubDir(r5);	 Catch:{ all -> 0x0081 }
        if (r0 == 0) goto L_0x0030;
    L_0x002e:
        monitor-exit(r4);
        return r1;
    L_0x0030:
        r4.getChildren(r6);	 Catch:{ all -> 0x0081 }
        r0 = r5.getName();	 Catch:{ all -> 0x0081 }
        r0 = r6.getChild(r0);	 Catch:{ all -> 0x0081 }
        if (r0 == 0) goto L_0x003f;
    L_0x003d:
        monitor-exit(r4);
        return r1;
    L_0x003f:
        r0 = r5.getStorageId();	 Catch:{ all -> 0x0081 }
        r2 = r6.getStorageId();	 Catch:{ all -> 0x0081 }
        if (r0 == r2) goto L_0x0066;
    L_0x0049:
        r0 = 1;
        r2 = r5.copy(r0);	 Catch:{ all -> 0x0081 }
        r2.setParent(r6);	 Catch:{ all -> 0x0081 }
        r6.addChild(r2);	 Catch:{ all -> 0x0081 }
        r3 = android.mtp.MtpStorageManager.MtpOperation.RENAME;	 Catch:{ all -> 0x0081 }
        r3 = r4.generalBeginRemoveObject(r5, r3);	 Catch:{ all -> 0x0081 }
        if (r3 == 0) goto L_0x0063;
    L_0x005c:
        r3 = r4.generalBeginCopyObject(r2, r1);	 Catch:{ all -> 0x0081 }
        if (r3 == 0) goto L_0x0063;
    L_0x0062:
        goto L_0x0064;
    L_0x0063:
        r0 = r1;
    L_0x0064:
        monitor-exit(r4);
        return r0;
    L_0x0066:
        r0 = r5.copy(r1);	 Catch:{ all -> 0x0081 }
        r5.setParent(r6);	 Catch:{ all -> 0x0081 }
        r1 = r0.getParent();	 Catch:{ all -> 0x0081 }
        r1.addChild(r0);	 Catch:{ all -> 0x0081 }
        r1 = r5.getParent();	 Catch:{ all -> 0x0081 }
        r1.addChild(r5);	 Catch:{ all -> 0x0081 }
        r1 = r4.generalBeginRenameObject(r0, r5);	 Catch:{ all -> 0x0081 }
        monitor-exit(r4);
        return r1;
    L_0x0081:
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.beginMoveObject(android.mtp.MtpStorageManager$MtpObject, android.mtp.MtpStorageManager$MtpObject):boolean");
    }

    /* JADX WARNING: Missing block: B:16:0x0042, code skipped:
            return r2;
     */
    /* JADX WARNING: Missing block: B:25:0x0071, code skipped:
            return false;
     */
    public synchronized boolean endMoveObject(android.mtp.MtpStorageManager.MtpObject r7, android.mtp.MtpStorageManager.MtpObject r8, java.lang.String r9, boolean r10) {
        /*
        r6 = this;
        monitor-enter(r6);
        r0 = sDebug;	 Catch:{ all -> 0x0072 }
        if (r0 == 0) goto L_0x001b;
    L_0x0005:
        r0 = TAG;	 Catch:{ all -> 0x0072 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0072 }
        r1.<init>();	 Catch:{ all -> 0x0072 }
        r2 = "endMoveObject ";
        r1.append(r2);	 Catch:{ all -> 0x0072 }
        r1.append(r10);	 Catch:{ all -> 0x0072 }
        r1 = r1.toString();	 Catch:{ all -> 0x0072 }
        android.util.Log.v(r0, r1);	 Catch:{ all -> 0x0072 }
    L_0x001b:
        r0 = r7.getChild(r9);	 Catch:{ all -> 0x0072 }
        r1 = r8.getChild(r9);	 Catch:{ all -> 0x0072 }
        r2 = 0;
        if (r0 == 0) goto L_0x0070;
    L_0x0026:
        if (r1 != 0) goto L_0x0029;
    L_0x0028:
        goto L_0x0070;
    L_0x0029:
        r3 = r7.getStorageId();	 Catch:{ all -> 0x0072 }
        r4 = r1.getStorageId();	 Catch:{ all -> 0x0072 }
        if (r3 == r4) goto L_0x0043;
    L_0x0033:
        r3 = r6.endRemoveObject(r0, r10);	 Catch:{ all -> 0x0072 }
        r4 = 1;
        r5 = r6.generalEndCopyObject(r1, r10, r4);	 Catch:{ all -> 0x0072 }
        if (r5 == 0) goto L_0x0041;
    L_0x003e:
        if (r3 == 0) goto L_0x0041;
    L_0x0040:
        r2 = r4;
    L_0x0041:
        monitor-exit(r6);
        return r2;
    L_0x0043:
        if (r10 != 0) goto L_0x006a;
    L_0x0045:
        r2 = r0;
        r3 = r0.getState();	 Catch:{ all -> 0x0072 }
        r4 = r1.getParent();	 Catch:{ all -> 0x0072 }
        r2.setParent(r4);	 Catch:{ all -> 0x0072 }
        r4 = r1.getState();	 Catch:{ all -> 0x0072 }
        r2.setState(r4);	 Catch:{ all -> 0x0072 }
        r0 = r1;
        r0.setParent(r7);	 Catch:{ all -> 0x0072 }
        r0.setState(r3);	 Catch:{ all -> 0x0072 }
        r1 = r2;
        r4 = r1.getParent();	 Catch:{ all -> 0x0072 }
        r4.addChild(r1);	 Catch:{ all -> 0x0072 }
        r7.addChild(r0);	 Catch:{ all -> 0x0072 }
    L_0x006a:
        r2 = r6.generalEndRenameObject(r0, r1, r10);	 Catch:{ all -> 0x0072 }
        monitor-exit(r6);
        return r2;
    L_0x0070:
        monitor-exit(r6);
        return r2;
    L_0x0072:
        r7 = move-exception;
        monitor-exit(r6);
        throw r7;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.endMoveObject(android.mtp.MtpStorageManager$MtpObject, android.mtp.MtpStorageManager$MtpObject, java.lang.String, boolean):boolean");
    }

    public synchronized int beginCopyObject(MtpObject object, MtpObject newParent) {
        String str;
        if (sDebug) {
            str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("beginCopyObject ");
            stringBuilder.append(object.getName());
            stringBuilder.append(" to ");
            stringBuilder.append(newParent.getPath());
            Log.v(str, stringBuilder.toString());
        }
        str = object.getName();
        if (!newParent.isDir()) {
            return -1;
        }
        if (newParent.isRoot() && this.mSubdirectories != null && !this.mSubdirectories.contains(str)) {
            return -1;
        }
        getChildren(newParent);
        if (newParent.getChild(str) != null) {
            return -1;
        }
        MtpObject newObj = object.copy(object.isDir());
        newParent.addChild(newObj);
        newObj.setParent(newParent);
        if (!generalBeginCopyObject(newObj, true)) {
            return -1;
        }
        return newObj.getId();
    }

    public synchronized boolean endCopyObject(MtpObject object, boolean success) {
        if (sDebug) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("endCopyObject ");
            stringBuilder.append(object.getName());
            stringBuilder.append(WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER);
            stringBuilder.append(success);
            Log.v(str, stringBuilder.toString());
        }
        return generalEndCopyObject(object, success, false);
    }

    /* JADX WARNING: Missing block: B:34:0x005d, code skipped:
            return true;
     */
    private synchronized boolean generalEndAddObject(android.mtp.MtpStorageManager.MtpObject r5, boolean r6, boolean r7) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = android.mtp.MtpStorageManager.AnonymousClass1.$SwitchMap$android$mtp$MtpStorageManager$MtpObjectState;	 Catch:{ all -> 0x005e }
        r1 = r5.getState();	 Catch:{ all -> 0x005e }
        r1 = r1.ordinal();	 Catch:{ all -> 0x005e }
        r0 = r0[r1];	 Catch:{ all -> 0x005e }
        r1 = 1;
        r2 = 0;
        if (r0 == r1) goto L_0x004c;
    L_0x0011:
        r3 = 2;
        if (r0 == r3) goto L_0x0038;
    L_0x0014:
        r3 = 5;
        if (r0 == r3) goto L_0x0019;
    L_0x0017:
        monitor-exit(r4);
        return r2;
    L_0x0019:
        r0 = android.mtp.MtpStorageManager.MtpObjectState.NORMAL;	 Catch:{ all -> 0x005e }
        r5.setState(r0);	 Catch:{ all -> 0x005e }
        if (r6 != 0) goto L_0x005c;
    L_0x0020:
        r0 = r5.getParent();	 Catch:{ all -> 0x005e }
        r3 = r4.removeObjectFromCache(r5, r7, r2);	 Catch:{ all -> 0x005e }
        if (r3 != 0) goto L_0x002c;
    L_0x002a:
        monitor-exit(r4);
        return r2;
    L_0x002c:
        r2 = r5.getName();	 Catch:{ all -> 0x005e }
        r3 = r5.isDir();	 Catch:{ all -> 0x005e }
        r4.handleAddedObject(r0, r2, r3);	 Catch:{ all -> 0x005e }
        goto L_0x005c;
    L_0x0038:
        r0 = r4.removeObjectFromCache(r5, r7, r2);	 Catch:{ all -> 0x005e }
        if (r0 != 0) goto L_0x0040;
    L_0x003e:
        monitor-exit(r4);
        return r2;
    L_0x0040:
        if (r6 == 0) goto L_0x005c;
    L_0x0042:
        r0 = r4.mMtpNotifier;	 Catch:{ all -> 0x005e }
        r2 = r5.getId();	 Catch:{ all -> 0x005e }
        r0.sendObjectRemoved(r2);	 Catch:{ all -> 0x005e }
        goto L_0x005c;
    L_0x004c:
        if (r6 == 0) goto L_0x0054;
    L_0x004e:
        r0 = android.mtp.MtpStorageManager.MtpObjectState.FROZEN_ONESHOT_ADD;	 Catch:{ all -> 0x005e }
        r5.setState(r0);	 Catch:{ all -> 0x005e }
        goto L_0x005c;
    L_0x0054:
        r0 = r4.removeObjectFromCache(r5, r7, r2);	 Catch:{ all -> 0x005e }
        if (r0 != 0) goto L_0x005c;
    L_0x005a:
        monitor-exit(r4);
        return r2;
    L_0x005c:
        monitor-exit(r4);
        return r1;
    L_0x005e:
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.generalEndAddObject(android.mtp.MtpStorageManager$MtpObject, boolean, boolean):boolean");
    }

    /* JADX WARNING: Missing block: B:31:0x005b, code skipped:
            return true;
     */
    private synchronized boolean generalEndRemoveObject(android.mtp.MtpStorageManager.MtpObject r5, boolean r6, boolean r7) {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = android.mtp.MtpStorageManager.AnonymousClass1.$SwitchMap$android$mtp$MtpStorageManager$MtpObjectState;	 Catch:{ all -> 0x005c }
        r1 = r5.getState();	 Catch:{ all -> 0x005c }
        r1 = r1.ordinal();	 Catch:{ all -> 0x005c }
        r0 = r0[r1];	 Catch:{ all -> 0x005c }
        r1 = 1;
        if (r0 == r1) goto L_0x004c;
    L_0x0010:
        r2 = 2;
        r3 = 0;
        if (r0 == r2) goto L_0x0038;
    L_0x0014:
        r2 = 5;
        if (r0 == r2) goto L_0x0019;
    L_0x0017:
        monitor-exit(r4);
        return r3;
    L_0x0019:
        r0 = android.mtp.MtpStorageManager.MtpObjectState.NORMAL;	 Catch:{ all -> 0x005c }
        r5.setState(r0);	 Catch:{ all -> 0x005c }
        if (r6 == 0) goto L_0x005a;
    L_0x0020:
        r0 = r5.getParent();	 Catch:{ all -> 0x005c }
        r2 = r4.removeObjectFromCache(r5, r7, r3);	 Catch:{ all -> 0x005c }
        if (r2 != 0) goto L_0x002c;
    L_0x002a:
        monitor-exit(r4);
        return r3;
    L_0x002c:
        r2 = r5.getName();	 Catch:{ all -> 0x005c }
        r3 = r5.isDir();	 Catch:{ all -> 0x005c }
        r4.handleAddedObject(r0, r2, r3);	 Catch:{ all -> 0x005c }
        goto L_0x005a;
    L_0x0038:
        r0 = r4.removeObjectFromCache(r5, r7, r3);	 Catch:{ all -> 0x005c }
        if (r0 != 0) goto L_0x0040;
    L_0x003e:
        monitor-exit(r4);
        return r3;
    L_0x0040:
        if (r6 != 0) goto L_0x005a;
    L_0x0042:
        r0 = r4.mMtpNotifier;	 Catch:{ all -> 0x005c }
        r2 = r5.getId();	 Catch:{ all -> 0x005c }
        r0.sendObjectRemoved(r2);	 Catch:{ all -> 0x005c }
        goto L_0x005a;
    L_0x004c:
        if (r6 == 0) goto L_0x0054;
    L_0x004e:
        r0 = android.mtp.MtpStorageManager.MtpObjectState.FROZEN_ONESHOT_DEL;	 Catch:{ all -> 0x005c }
        r5.setState(r0);	 Catch:{ all -> 0x005c }
        goto L_0x005a;
    L_0x0054:
        r0 = android.mtp.MtpStorageManager.MtpObjectState.NORMAL;	 Catch:{ all -> 0x005c }
        r5.setState(r0);	 Catch:{ all -> 0x005c }
    L_0x005a:
        monitor-exit(r4);
        return r1;
    L_0x005c:
        r5 = move-exception;
        monitor-exit(r4);
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.mtp.MtpStorageManager.generalEndRemoveObject(android.mtp.MtpStorageManager$MtpObject, boolean, boolean):boolean");
    }

    private synchronized boolean generalBeginRenameObject(MtpObject fromObj, MtpObject toObj) {
        fromObj.setState(MtpObjectState.FROZEN);
        toObj.setState(MtpObjectState.FROZEN);
        fromObj.setOperation(MtpOperation.RENAME);
        toObj.setOperation(MtpOperation.RENAME);
        return true;
    }

    private synchronized boolean generalEndRenameObject(MtpObject fromObj, MtpObject toObj, boolean success) {
        boolean z;
        z = true;
        boolean ret = generalEndRemoveObject(fromObj, success, !success);
        if (!(generalEndAddObject(toObj, success, success) && ret)) {
            z = false;
        }
        return z;
    }

    private synchronized boolean generalBeginRemoveObject(MtpObject obj, MtpOperation op) {
        obj.setState(MtpObjectState.FROZEN);
        obj.setOperation(op);
        if (obj.isDir()) {
            for (MtpObject child : obj.getChildren()) {
                generalBeginRemoveObject(child, op);
            }
        }
        return true;
    }

    private synchronized boolean generalBeginCopyObject(MtpObject obj, boolean newId) {
        obj.setState(MtpObjectState.FROZEN);
        obj.setOperation(MtpOperation.COPY);
        if (newId) {
            obj.setId(getNextObjectId());
            this.mObjects.put(Integer.valueOf(obj.getId()), obj);
        }
        if (obj.isDir()) {
            for (MtpObject child : obj.getChildren()) {
                if (!generalBeginCopyObject(child, newId)) {
                    return false;
                }
            }
        }
        return true;
    }

    private synchronized boolean generalEndCopyObject(MtpObject obj, boolean success, boolean addGlobal) {
        boolean z;
        boolean z2;
        if (success && addGlobal) {
            this.mObjects.put(Integer.valueOf(obj.getId()), obj);
        }
        boolean ret = true;
        z = false;
        if (obj.isDir()) {
            Iterator it = new ArrayList(obj.getChildren()).iterator();
            while (it.hasNext()) {
                MtpObject child = (MtpObject) it.next();
                if (child.getOperation() == MtpOperation.COPY) {
                    boolean z3 = generalEndCopyObject(child, success, addGlobal) && ret;
                    ret = z3;
                }
            }
        }
        if (!success) {
            if (addGlobal) {
                z2 = false;
                if (generalEndAddObject(obj, success, z2) && ret) {
                    z = true;
                }
            }
        }
        z2 = true;
        z = true;
        return z;
    }
}
