package android.app;

import android.annotation.UnsupportedAppUsage;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.FileUtils;
import android.os.Looper;
import android.system.ErrnoException;
import android.system.Os;
import android.system.StructStat;
import android.system.StructTimespec;
import android.util.Log;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.ExponentiallyBucketedHistogram;
import com.android.internal.util.XmlUtils;
import dalvik.system.BlockGuard;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.concurrent.CountDownLatch;
import org.xmlpull.v1.XmlPullParserException;

final class SharedPreferencesImpl implements SharedPreferences {
    private static final Object CONTENT = new Object();
    private static final boolean DEBUG = false;
    private static final long MAX_FSYNC_DURATION_MILLIS = 256;
    private static final String TAG = "SharedPreferencesImpl";
    private final File mBackupFile;
    @GuardedBy({"this"})
    private long mCurrentMemoryStateGeneration;
    @GuardedBy({"mWritingToDiskLock"})
    private long mDiskStateGeneration;
    @GuardedBy({"mLock"})
    private int mDiskWritesInFlight = 0;
    @UnsupportedAppUsage
    private final File mFile;
    @GuardedBy({"mLock"})
    private final WeakHashMap<OnSharedPreferenceChangeListener, Object> mListeners = new WeakHashMap();
    @GuardedBy({"mLock"})
    private boolean mLoaded = false;
    private final Object mLock = new Object();
    @GuardedBy({"mLock"})
    private Map<String, Object> mMap;
    private final int mMode;
    private int mNumSync = 0;
    @GuardedBy({"mLock"})
    private long mStatSize;
    @GuardedBy({"mLock"})
    private StructTimespec mStatTimestamp;
    @GuardedBy({"mWritingToDiskLock"})
    private final ExponentiallyBucketedHistogram mSyncTimes = new ExponentiallyBucketedHistogram(16);
    @GuardedBy({"mLock"})
    private Throwable mThrowable;
    private final Object mWritingToDiskLock = new Object();

    public final class EditorImpl implements Editor {
        @GuardedBy({"mEditorLock"})
        private boolean mClear = false;
        private final Object mEditorLock = new Object();
        @GuardedBy({"mEditorLock"})
        private final Map<String, Object> mModified = new HashMap();

        public Editor putString(String key, String value) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, value);
            }
            return this;
        }

        public Editor putStringSet(String key, Set<String> values) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, values == null ? null : new HashSet(values));
            }
            return this;
        }

        public Editor putInt(String key, int value) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, Integer.valueOf(value));
            }
            return this;
        }

        public Editor putLong(String key, long value) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, Long.valueOf(value));
            }
            return this;
        }

        public Editor putFloat(String key, float value) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, Float.valueOf(value));
            }
            return this;
        }

        public Editor putBoolean(String key, boolean value) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, Boolean.valueOf(value));
            }
            return this;
        }

        public Editor remove(String key) {
            synchronized (this.mEditorLock) {
                this.mModified.put(key, this);
            }
            return this;
        }

        public Editor clear() {
            synchronized (this.mEditorLock) {
                this.mClear = true;
            }
            return this;
        }

        public void apply() {
            final long startTime = System.currentTimeMillis();
            final MemoryCommitResult mcr = commitToMemory();
            final Runnable awaitCommit = new Runnable() {
                public void run() {
                    try {
                        mcr.writtenToDiskLatch.await();
                    } catch (InterruptedException e) {
                    }
                }
            };
            QueuedWork.addFinisher(awaitCommit);
            SharedPreferencesImpl.this.enqueueDiskWrite(mcr, new Runnable() {
                public void run() {
                    awaitCommit.run();
                    QueuedWork.removeFinisher(awaitCommit);
                }
            });
            notifyListeners(mcr);
        }

        /* JADX WARNING: Removed duplicated region for block: B:43:0x00b1  */
        private android.app.SharedPreferencesImpl.MemoryCommitResult commitToMemory() {
            /*
            r13 = this;
            r0 = 0;
            r1 = 0;
            r2 = android.app.SharedPreferencesImpl.this;
            r2 = r2.mLock;
            monitor-enter(r2);
            r3 = android.app.SharedPreferencesImpl.this;	 Catch:{ all -> 0x00d8 }
            r3 = r3.mDiskWritesInFlight;	 Catch:{ all -> 0x00d8 }
            if (r3 <= 0) goto L_0x0021;
        L_0x0011:
            r3 = android.app.SharedPreferencesImpl.this;	 Catch:{ all -> 0x00d8 }
            r4 = new java.util.HashMap;	 Catch:{ all -> 0x00d8 }
            r5 = android.app.SharedPreferencesImpl.this;	 Catch:{ all -> 0x00d8 }
            r5 = r5.mMap;	 Catch:{ all -> 0x00d8 }
            r4.<init>(r5);	 Catch:{ all -> 0x00d8 }
            r3.mMap = r4;	 Catch:{ all -> 0x00d8 }
        L_0x0021:
            r3 = android.app.SharedPreferencesImpl.this;	 Catch:{ all -> 0x00d8 }
            r3 = r3.mMap;	 Catch:{ all -> 0x00d8 }
            r4 = android.app.SharedPreferencesImpl.this;	 Catch:{ all -> 0x00d8 }
            r4.mDiskWritesInFlight = r4.mDiskWritesInFlight + 1;	 Catch:{ all -> 0x00d8 }
            r4 = android.app.SharedPreferencesImpl.this;	 Catch:{ all -> 0x00d8 }
            r4 = r4.mListeners;	 Catch:{ all -> 0x00d8 }
            r4 = r4.size();	 Catch:{ all -> 0x00d8 }
            r5 = 0;
            if (r4 <= 0) goto L_0x003b;
        L_0x0039:
            r4 = 1;
            goto L_0x003c;
        L_0x003b:
            r4 = r5;
        L_0x003c:
            if (r4 == 0) goto L_0x0054;
        L_0x003e:
            r6 = new java.util.ArrayList;	 Catch:{ all -> 0x00d8 }
            r6.<init>();	 Catch:{ all -> 0x00d8 }
            r0 = r6;
            r6 = new java.util.HashSet;	 Catch:{ all -> 0x00d8 }
            r7 = android.app.SharedPreferencesImpl.this;	 Catch:{ all -> 0x00d8 }
            r7 = r7.mListeners;	 Catch:{ all -> 0x00d8 }
            r7 = r7.keySet();	 Catch:{ all -> 0x00d8 }
            r6.<init>(r7);	 Catch:{ all -> 0x00d8 }
            r1 = r6;
        L_0x0054:
            r7 = r13.mEditorLock;	 Catch:{ all -> 0x00d8 }
            monitor-enter(r7);	 Catch:{ all -> 0x00d8 }
            r6 = 0;
            r8 = r13.mClear;	 Catch:{ all -> 0x00d5 }
            if (r8 == 0) goto L_0x0068;
        L_0x005c:
            r8 = r3.isEmpty();	 Catch:{ all -> 0x00d5 }
            if (r8 != 0) goto L_0x0066;
        L_0x0062:
            r6 = 1;
            r3.clear();	 Catch:{ all -> 0x00d5 }
        L_0x0066:
            r13.mClear = r5;	 Catch:{ all -> 0x00d5 }
        L_0x0068:
            r5 = r13.mModified;	 Catch:{ all -> 0x00d5 }
            r5 = r5.entrySet();	 Catch:{ all -> 0x00d5 }
            r5 = r5.iterator();	 Catch:{ all -> 0x00d5 }
        L_0x0072:
            r8 = r5.hasNext();	 Catch:{ all -> 0x00d5 }
            if (r8 == 0) goto L_0x00b5;
        L_0x0078:
            r8 = r5.next();	 Catch:{ all -> 0x00d5 }
            r8 = (java.util.Map.Entry) r8;	 Catch:{ all -> 0x00d5 }
            r9 = r8.getKey();	 Catch:{ all -> 0x00d5 }
            r9 = (java.lang.String) r9;	 Catch:{ all -> 0x00d5 }
            r10 = r8.getValue();	 Catch:{ all -> 0x00d5 }
            if (r10 == r13) goto L_0x00a4;
        L_0x008a:
            if (r10 != 0) goto L_0x008d;
        L_0x008c:
            goto L_0x00a4;
        L_0x008d:
            r11 = r3.containsKey(r9);	 Catch:{ all -> 0x00d5 }
            if (r11 == 0) goto L_0x00a0;
        L_0x0093:
            r11 = r3.get(r9);	 Catch:{ all -> 0x00d5 }
            if (r11 == 0) goto L_0x00a0;
        L_0x0099:
            r12 = r11.equals(r10);	 Catch:{ all -> 0x00d5 }
            if (r12 == 0) goto L_0x00a0;
        L_0x009f:
            goto L_0x0072;
        L_0x00a0:
            r3.put(r9, r10);	 Catch:{ all -> 0x00d5 }
            goto L_0x00ae;
        L_0x00a4:
            r11 = r3.containsKey(r9);	 Catch:{ all -> 0x00d5 }
            if (r11 != 0) goto L_0x00ab;
        L_0x00aa:
            goto L_0x0072;
        L_0x00ab:
            r3.remove(r9);	 Catch:{ all -> 0x00d5 }
        L_0x00ae:
            r6 = 1;
            if (r4 == 0) goto L_0x00b4;
        L_0x00b1:
            r0.add(r9);	 Catch:{ all -> 0x00d5 }
        L_0x00b4:
            goto L_0x0072;
        L_0x00b5:
            r5 = r13.mModified;	 Catch:{ all -> 0x00d5 }
            r5.clear();	 Catch:{ all -> 0x00d5 }
            if (r6 == 0) goto L_0x00c1;
        L_0x00bc:
            r5 = android.app.SharedPreferencesImpl.this;	 Catch:{ all -> 0x00d5 }
            r5.mCurrentMemoryStateGeneration = 1 + r5.mCurrentMemoryStateGeneration;	 Catch:{ all -> 0x00d5 }
        L_0x00c1:
            r5 = android.app.SharedPreferencesImpl.this;	 Catch:{ all -> 0x00d5 }
            r8 = r5.mCurrentMemoryStateGeneration;	 Catch:{ all -> 0x00d5 }
            r5 = r8;
            monitor-exit(r7);	 Catch:{ all -> 0x00d5 }
            monitor-exit(r2);	 Catch:{ all -> 0x00d8 }
            r2 = new android.app.SharedPreferencesImpl$MemoryCommitResult;
            r10 = 0;
            r4 = r2;
            r7 = r0;
            r8 = r1;
            r9 = r3;
            r4.<init>(r5, r7, r8, r9, r10);
            return r2;
        L_0x00d5:
            r5 = move-exception;
            monitor-exit(r7);	 Catch:{ all -> 0x00d5 }
            throw r5;	 Catch:{ all -> 0x00d8 }
        L_0x00d8:
            r3 = move-exception;
            monitor-exit(r2);	 Catch:{ all -> 0x00d8 }
            throw r3;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.app.SharedPreferencesImpl$EditorImpl.commitToMemory():android.app.SharedPreferencesImpl$MemoryCommitResult");
        }

        public boolean commit() {
            MemoryCommitResult mcr = commitToMemory();
            SharedPreferencesImpl.this.enqueueDiskWrite(mcr, null);
            try {
                mcr.writtenToDiskLatch.await();
                notifyListeners(mcr);
                return mcr.writeToDiskResult;
            } catch (InterruptedException e) {
                return false;
            }
        }

        private void notifyListeners(MemoryCommitResult mcr) {
            if (mcr.listeners != null && mcr.keysModified != null && mcr.keysModified.size() != 0) {
                if (Looper.myLooper() == Looper.getMainLooper()) {
                    for (int i = mcr.keysModified.size() - 1; i >= 0; i--) {
                        String key = (String) mcr.keysModified.get(i);
                        for (OnSharedPreferenceChangeListener listener : mcr.listeners) {
                            if (listener != null) {
                                listener.onSharedPreferenceChanged(SharedPreferencesImpl.this, key);
                            }
                        }
                    }
                } else {
                    ActivityThread.sMainThreadHandler.post(new -$$Lambda$SharedPreferencesImpl$EditorImpl$3CAjkhzA131V3V-sLfP2uy0FWZ0(this, mcr));
                }
            }
        }

        public /* synthetic */ void lambda$notifyListeners$0$SharedPreferencesImpl$EditorImpl(MemoryCommitResult mcr) {
            notifyListeners(mcr);
        }
    }

    private static class MemoryCommitResult {
        final List<String> keysModified;
        final Set<OnSharedPreferenceChangeListener> listeners;
        final Map<String, Object> mapToWriteToDisk;
        final long memoryStateGeneration;
        boolean wasWritten;
        @GuardedBy({"mWritingToDiskLock"})
        volatile boolean writeToDiskResult;
        final CountDownLatch writtenToDiskLatch;

        /* synthetic */ MemoryCommitResult(long x0, List x1, Set x2, Map x3, AnonymousClass1 x4) {
            this(x0, x1, x2, x3);
        }

        private MemoryCommitResult(long memoryStateGeneration, List<String> keysModified, Set<OnSharedPreferenceChangeListener> listeners, Map<String, Object> mapToWriteToDisk) {
            this.writtenToDiskLatch = new CountDownLatch(1);
            this.writeToDiskResult = false;
            this.wasWritten = false;
            this.memoryStateGeneration = memoryStateGeneration;
            this.keysModified = keysModified;
            this.listeners = listeners;
            this.mapToWriteToDisk = mapToWriteToDisk;
        }

        /* Access modifiers changed, original: 0000 */
        public void setDiskWriteResult(boolean wasWritten, boolean result) {
            this.wasWritten = wasWritten;
            this.writeToDiskResult = result;
            this.writtenToDiskLatch.countDown();
        }
    }

    @UnsupportedAppUsage
    SharedPreferencesImpl(File file, int mode) {
        this.mFile = file;
        this.mBackupFile = makeBackupFile(file);
        this.mMode = mode;
        this.mLoaded = false;
        this.mMap = null;
        this.mThrowable = null;
        startLoadFromDisk();
    }

    @UnsupportedAppUsage
    private void startLoadFromDisk() {
        synchronized (this.mLock) {
            this.mLoaded = false;
        }
        new Thread("SharedPreferencesImpl-load") {
            public void run() {
                SharedPreferencesImpl.this.loadFromDisk();
            }
        }.start();
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x00ad  */
    /* JADX WARNING: Missing block: B:12:0x0024, code skipped:
            if (r8.mFile.exists() == false) goto L_0x004b;
     */
    /* JADX WARNING: Missing block: B:14:0x002c, code skipped:
            if (r8.mFile.canRead() != false) goto L_0x004b;
     */
    /* JADX WARNING: Missing block: B:15:0x002e, code skipped:
            r0 = new java.lang.StringBuilder();
            r0.append("Attempt to read preferences file ");
            r0.append(r8.mFile);
            r0.append(" without permission");
            android.util.Log.w(TAG, r0.toString());
     */
    /* JADX WARNING: Missing block: B:16:0x004b, code skipped:
            r0 = null;
            r1 = null;
            r2 = null;
     */
    /* JADX WARNING: Missing block: B:18:?, code skipped:
            r1 = android.system.Os.stat(r8.mFile.getPath());
     */
    /* JADX WARNING: Missing block: B:19:0x005f, code skipped:
            if (r8.mFile.canRead() == false) goto L_0x00a8;
     */
    /* JADX WARNING: Missing block: B:20:0x0061, code skipped:
            r3 = null;
     */
    /* JADX WARNING: Missing block: B:22:?, code skipped:
            r3 = new java.io.BufferedInputStream(new java.io.FileInputStream(r8.mFile), 16384);
     */
    /* JADX WARNING: Missing block: B:23:0x0075, code skipped:
            r0 = com.android.internal.util.XmlUtils.readMapXml(r3);
     */
    /* JADX WARNING: Missing block: B:25:?, code skipped:
            libcore.io.IoUtils.closeQuietly(r3);
     */
    /* JADX WARNING: Missing block: B:27:0x007c, code skipped:
            r4 = move-exception;
     */
    /* JADX WARNING: Missing block: B:29:?, code skipped:
            r5 = TAG;
            r6 = new java.lang.StringBuilder();
            r6.append("Cannot read ");
            r6.append(r8.mFile.getAbsolutePath());
            android.util.Log.w(r5, r6.toString(), r4);
     */
    /* JADX WARNING: Missing block: B:31:?, code skipped:
            libcore.io.IoUtils.closeQuietly(r3);
     */
    /* JADX WARNING: Missing block: B:32:0x009e, code skipped:
            libcore.io.IoUtils.closeQuietly(r3);
     */
    /* JADX WARNING: Missing block: B:34:0x00a2, code skipped:
            r3 = move-exception;
     */
    /* JADX WARNING: Missing block: B:35:0x00a3, code skipped:
            r2 = r3;
            r3 = r1;
            r1 = r0;
     */
    /* JADX WARNING: Missing block: B:37:0x00a8, code skipped:
            r3 = r1;
            r1 = r0;
     */
    /* JADX WARNING: Missing block: B:39:0x00ac, code skipped:
            monitor-enter(r8.mLock);
     */
    /* JADX WARNING: Missing block: B:42:?, code skipped:
            r8.mLoaded = true;
            r8.mThrowable = r2;
     */
    /* JADX WARNING: Missing block: B:43:0x00b2, code skipped:
            if (r2 == null) goto L_0x00b4;
     */
    /* JADX WARNING: Missing block: B:44:0x00b4, code skipped:
            if (r1 != null) goto L_0x00b6;
     */
    /* JADX WARNING: Missing block: B:46:?, code skipped:
            r8.mMap = r1;
            r8.mStatTimestamp = r3.st_mtim;
            r8.mStatSize = r3.st_size;
     */
    /* JADX WARNING: Missing block: B:47:0x00c1, code skipped:
            r8.mMap = new java.util.HashMap();
     */
    /* JADX WARNING: Missing block: B:48:0x00c9, code skipped:
            r0 = move-exception;
     */
    /* JADX WARNING: Missing block: B:50:?, code skipped:
            r8.mThrowable = r0;
     */
    /* JADX WARNING: Missing block: B:52:?, code skipped:
            r0 = r8.mLock;
     */
    /* JADX WARNING: Missing block: B:54:0x00d0, code skipped:
            r8.mLock.notifyAll();
     */
    /* JADX WARNING: Missing block: B:56:0x00d6, code skipped:
            r0 = r8.mLock;
     */
    /* JADX WARNING: Missing block: B:57:0x00d8, code skipped:
            r0.notifyAll();
     */
    /* JADX WARNING: Missing block: B:59:0x00dd, code skipped:
            return;
     */
    private void loadFromDisk() {
        /*
        r8 = this;
        r0 = r8.mLock;
        monitor-enter(r0);
        r1 = r8.mLoaded;	 Catch:{ all -> 0x00e1 }
        if (r1 == 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x00e1 }
        return;
    L_0x0009:
        r1 = r8.mBackupFile;	 Catch:{ all -> 0x00e1 }
        r1 = r1.exists();	 Catch:{ all -> 0x00e1 }
        if (r1 == 0) goto L_0x001d;
    L_0x0011:
        r1 = r8.mFile;	 Catch:{ all -> 0x00e1 }
        r1.delete();	 Catch:{ all -> 0x00e1 }
        r1 = r8.mBackupFile;	 Catch:{ all -> 0x00e1 }
        r2 = r8.mFile;	 Catch:{ all -> 0x00e1 }
        r1.renameTo(r2);	 Catch:{ all -> 0x00e1 }
    L_0x001d:
        monitor-exit(r0);	 Catch:{ all -> 0x00e1 }
        r0 = r8.mFile;
        r0 = r0.exists();
        if (r0 == 0) goto L_0x004b;
    L_0x0026:
        r0 = r8.mFile;
        r0 = r0.canRead();
        if (r0 != 0) goto L_0x004b;
    L_0x002e:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Attempt to read preferences file ";
        r0.append(r1);
        r1 = r8.mFile;
        r0.append(r1);
        r1 = " without permission";
        r0.append(r1);
        r0 = r0.toString();
        r1 = "SharedPreferencesImpl";
        android.util.Log.w(r1, r0);
    L_0x004b:
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r3 = r8.mFile;	 Catch:{ ErrnoException -> 0x00a7, all -> 0x00a2 }
        r3 = r3.getPath();	 Catch:{ ErrnoException -> 0x00a7, all -> 0x00a2 }
        r3 = android.system.Os.stat(r3);	 Catch:{ ErrnoException -> 0x00a7, all -> 0x00a2 }
        r1 = r3;
        r3 = r8.mFile;	 Catch:{ ErrnoException -> 0x00a7, all -> 0x00a2 }
        r3 = r3.canRead();	 Catch:{ ErrnoException -> 0x00a7, all -> 0x00a2 }
        if (r3 == 0) goto L_0x00a8;
    L_0x0061:
        r3 = 0;
        r4 = new java.io.BufferedInputStream;	 Catch:{ Exception -> 0x007c }
        r5 = new java.io.FileInputStream;	 Catch:{ Exception -> 0x007c }
        r6 = r8.mFile;	 Catch:{ Exception -> 0x007c }
        r5.<init>(r6);	 Catch:{ Exception -> 0x007c }
        r6 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
        r4.<init>(r5, r6);	 Catch:{ Exception -> 0x007c }
        r3 = r4;
        r4 = com.android.internal.util.XmlUtils.readMapXml(r3);	 Catch:{ Exception -> 0x007c }
        r0 = r4;
        libcore.io.IoUtils.closeQuietly(r3);	 Catch:{ ErrnoException -> 0x00a7, all -> 0x00a2 }
    L_0x0079:
        goto L_0x00a8;
    L_0x007a:
        r4 = move-exception;
        goto L_0x009e;
    L_0x007c:
        r4 = move-exception;
        r5 = "SharedPreferencesImpl";
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x007a }
        r6.<init>();	 Catch:{ all -> 0x007a }
        r7 = "Cannot read ";
        r6.append(r7);	 Catch:{ all -> 0x007a }
        r7 = r8.mFile;	 Catch:{ all -> 0x007a }
        r7 = r7.getAbsolutePath();	 Catch:{ all -> 0x007a }
        r6.append(r7);	 Catch:{ all -> 0x007a }
        r6 = r6.toString();	 Catch:{ all -> 0x007a }
        android.util.Log.w(r5, r6, r4);	 Catch:{ all -> 0x007a }
        libcore.io.IoUtils.closeQuietly(r3);	 Catch:{ ErrnoException -> 0x00a7, all -> 0x00a2 }
        goto L_0x0079;
    L_0x009e:
        libcore.io.IoUtils.closeQuietly(r3);	 Catch:{ ErrnoException -> 0x00a7, all -> 0x00a2 }
        throw r4;	 Catch:{ ErrnoException -> 0x00a7, all -> 0x00a2 }
    L_0x00a2:
        r3 = move-exception;
        r2 = r3;
        r3 = r1;
        r1 = r0;
        goto L_0x00aa;
    L_0x00a7:
        r3 = move-exception;
    L_0x00a8:
        r3 = r1;
        r1 = r0;
    L_0x00aa:
        r4 = r8.mLock;
        monitor-enter(r4);
        r0 = 1;
        r8.mLoaded = r0;	 Catch:{ all -> 0x00de }
        r8.mThrowable = r2;	 Catch:{ all -> 0x00de }
        if (r2 != 0) goto L_0x00d6;
    L_0x00b4:
        if (r1 == 0) goto L_0x00c1;
    L_0x00b6:
        r8.mMap = r1;	 Catch:{ all -> 0x00c9 }
        r0 = r3.st_mtim;	 Catch:{ all -> 0x00c9 }
        r8.mStatTimestamp = r0;	 Catch:{ all -> 0x00c9 }
        r5 = r3.st_size;	 Catch:{ all -> 0x00c9 }
        r8.mStatSize = r5;	 Catch:{ all -> 0x00c9 }
        goto L_0x00d6;
    L_0x00c1:
        r0 = new java.util.HashMap;	 Catch:{ all -> 0x00c9 }
        r0.<init>();	 Catch:{ all -> 0x00c9 }
        r8.mMap = r0;	 Catch:{ all -> 0x00c9 }
        goto L_0x00d6;
    L_0x00c9:
        r0 = move-exception;
        r8.mThrowable = r0;	 Catch:{ all -> 0x00cf }
        r0 = r8.mLock;	 Catch:{ all -> 0x00de }
        goto L_0x00d8;
    L_0x00cf:
        r0 = move-exception;
        r5 = r8.mLock;	 Catch:{ all -> 0x00de }
        r5.notifyAll();	 Catch:{ all -> 0x00de }
        throw r0;	 Catch:{ all -> 0x00de }
    L_0x00d6:
        r0 = r8.mLock;	 Catch:{ all -> 0x00de }
    L_0x00d8:
        r0.notifyAll();	 Catch:{ all -> 0x00de }
        monitor-exit(r4);	 Catch:{ all -> 0x00de }
        return;
    L_0x00de:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x00de }
        throw r0;
    L_0x00e1:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x00e1 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.SharedPreferencesImpl.loadFromDisk():void");
    }

    static File makeBackupFile(File prefsFile) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prefsFile.getPath());
        stringBuilder.append(".bak");
        return new File(stringBuilder.toString());
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public void startReloadIfChangedUnexpectedly() {
        synchronized (this.mLock) {
            if (hasFileChangedUnexpectedly()) {
                startLoadFromDisk();
                return;
            }
        }
    }

    /* JADX WARNING: Missing block: B:8:0x000b, code skipped:
            r0 = true;
     */
    /* JADX WARNING: Missing block: B:10:?, code skipped:
            dalvik.system.BlockGuard.getThreadPolicy().onReadFromDisk();
            r1 = android.system.Os.stat(r8.mFile.getPath());
     */
    /* JADX WARNING: Missing block: B:11:0x001d, code skipped:
            r3 = r8.mLock;
     */
    /* JADX WARNING: Missing block: B:12:0x0020, code skipped:
            monitor-enter(r3);
     */
    /* JADX WARNING: Missing block: B:15:0x0029, code skipped:
            if (r1.st_mtim.equals(r8.mStatTimestamp) == false) goto L_0x0035;
     */
    /* JADX WARNING: Missing block: B:17:0x0031, code skipped:
            if (r8.mStatSize == r1.st_size) goto L_0x0034;
     */
    /* JADX WARNING: Missing block: B:19:0x0034, code skipped:
            r0 = false;
     */
    /* JADX WARNING: Missing block: B:20:0x0035, code skipped:
            monitor-exit(r3);
     */
    /* JADX WARNING: Missing block: B:21:0x0036, code skipped:
            return r0;
     */
    /* JADX WARNING: Missing block: B:26:0x003b, code skipped:
            return true;
     */
    private boolean hasFileChangedUnexpectedly() {
        /*
        r8 = this;
        r0 = r8.mLock;
        monitor-enter(r0);
        r1 = r8.mDiskWritesInFlight;	 Catch:{ all -> 0x003c }
        r2 = 0;
        if (r1 <= 0) goto L_0x000a;
    L_0x0008:
        monitor-exit(r0);	 Catch:{ all -> 0x003c }
        return r2;
    L_0x000a:
        monitor-exit(r0);	 Catch:{ all -> 0x003c }
        r0 = 1;
        r1 = dalvik.system.BlockGuard.getThreadPolicy();	 Catch:{ ErrnoException -> 0x003a }
        r1.onReadFromDisk();	 Catch:{ ErrnoException -> 0x003a }
        r1 = r8.mFile;	 Catch:{ ErrnoException -> 0x003a }
        r1 = r1.getPath();	 Catch:{ ErrnoException -> 0x003a }
        r1 = android.system.Os.stat(r1);	 Catch:{ ErrnoException -> 0x003a }
        r3 = r8.mLock;
        monitor-enter(r3);
        r4 = r1.st_mtim;	 Catch:{ all -> 0x0037 }
        r5 = r8.mStatTimestamp;	 Catch:{ all -> 0x0037 }
        r4 = r4.equals(r5);	 Catch:{ all -> 0x0037 }
        if (r4 == 0) goto L_0x0035;
    L_0x002b:
        r4 = r8.mStatSize;	 Catch:{ all -> 0x0037 }
        r6 = r1.st_size;	 Catch:{ all -> 0x0037 }
        r4 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1));
        if (r4 == 0) goto L_0x0034;
    L_0x0033:
        goto L_0x0035;
    L_0x0034:
        r0 = r2;
    L_0x0035:
        monitor-exit(r3);	 Catch:{ all -> 0x0037 }
        return r0;
    L_0x0037:
        r0 = move-exception;
        monitor-exit(r3);	 Catch:{ all -> 0x0037 }
        throw r0;
    L_0x003a:
        r1 = move-exception;
        return r0;
    L_0x003c:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x003c }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.app.SharedPreferencesImpl.hasFileChangedUnexpectedly():boolean");
    }

    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        synchronized (this.mLock) {
            this.mListeners.put(listener, CONTENT);
        }
    }

    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
        synchronized (this.mLock) {
            this.mListeners.remove(listener);
        }
    }

    @GuardedBy({"mLock"})
    private void awaitLoadedLocked() {
        if (!this.mLoaded) {
            BlockGuard.getThreadPolicy().onReadFromDisk();
        }
        while (!this.mLoaded) {
            try {
                this.mLock.wait();
            } catch (InterruptedException e) {
            }
        }
        Throwable th = this.mThrowable;
        if (th != null) {
            throw new IllegalStateException(th);
        }
    }

    public Map<String, ?> getAll() {
        HashMap hashMap;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            hashMap = new HashMap(this.mMap);
        }
        return hashMap;
    }

    public String getString(String key, String defValue) {
        String str;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            String v = (String) this.mMap.get(key);
            str = v != null ? v : defValue;
        }
        return str;
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        Set<String> set;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            Set<String> v = (Set) this.mMap.get(key);
            set = v != null ? v : defValues;
        }
        return set;
    }

    public int getInt(String key, int defValue) {
        int intValue;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            Integer v = (Integer) this.mMap.get(key);
            intValue = v != null ? v.intValue() : defValue;
        }
        return intValue;
    }

    public long getLong(String key, long defValue) {
        long longValue;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            Long v = (Long) this.mMap.get(key);
            longValue = v != null ? v.longValue() : defValue;
        }
        return longValue;
    }

    public float getFloat(String key, float defValue) {
        float floatValue;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            Float v = (Float) this.mMap.get(key);
            floatValue = v != null ? v.floatValue() : defValue;
        }
        return floatValue;
    }

    public boolean getBoolean(String key, boolean defValue) {
        boolean booleanValue;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            Boolean v = (Boolean) this.mMap.get(key);
            booleanValue = v != null ? v.booleanValue() : defValue;
        }
        return booleanValue;
    }

    public boolean contains(String key) {
        boolean containsKey;
        synchronized (this.mLock) {
            awaitLoadedLocked();
            containsKey = this.mMap.containsKey(key);
        }
        return containsKey;
    }

    public Editor edit() {
        synchronized (this.mLock) {
            awaitLoadedLocked();
        }
        return new EditorImpl();
    }

    private void enqueueDiskWrite(final MemoryCommitResult mcr, final Runnable postWriteRunnable) {
        boolean z = false;
        final boolean isFromSyncCommit = postWriteRunnable == null;
        Runnable writeToDiskRunnable = new Runnable() {
            public void run() {
                synchronized (SharedPreferencesImpl.this.mWritingToDiskLock) {
                    SharedPreferencesImpl.this.writeToFile(mcr, isFromSyncCommit);
                }
                synchronized (SharedPreferencesImpl.this.mLock) {
                    SharedPreferencesImpl.this.mDiskWritesInFlight = SharedPreferencesImpl.this.mDiskWritesInFlight - 1;
                }
                Runnable runnable = postWriteRunnable;
                if (runnable != null) {
                    runnable.run();
                }
            }
        };
        if (isFromSyncCommit) {
            boolean wasEmpty;
            synchronized (this.mLock) {
                wasEmpty = this.mDiskWritesInFlight == 1;
            }
            if (wasEmpty) {
                writeToDiskRunnable.run();
                return;
            }
        }
        if (!isFromSyncCommit) {
            z = true;
        }
        QueuedWork.queue(writeToDiskRunnable, z);
    }

    private static FileOutputStream createFileOutputStream(File file) {
        FileOutputStream str = null;
        try {
            str = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            File parent = file.getParentFile();
            boolean mkdir = parent.mkdir();
            String str2 = TAG;
            if (mkdir) {
                FileUtils.setPermissions(parent.getPath(), 505, -1, -1);
                try {
                    str = new FileOutputStream(file);
                } catch (FileNotFoundException e2) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Couldn't create SharedPreferences file ");
                    stringBuilder.append(file);
                    Log.e(str2, stringBuilder.toString(), e2);
                }
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Couldn't create directory for SharedPreferences file ");
                stringBuilder2.append(file);
                Log.e(str2, stringBuilder2.toString());
                return null;
            }
        }
        return str;
    }

    @GuardedBy({"mWritingToDiskLock"})
    private void writeToFile(MemoryCommitResult mcr, boolean isFromSyncCommit) {
        Throwable th;
        StringBuilder stringBuilder;
        MemoryCommitResult memoryCommitResult = mcr;
        long startTime = 0;
        long existsTime;
        long backupExistsTime;
        if (this.mFile.exists()) {
            boolean needsWrite = false;
            existsTime = 0;
            if (this.mDiskStateGeneration >= memoryCommitResult.memoryStateGeneration) {
                backupExistsTime = 0;
            } else if (isFromSyncCommit) {
                needsWrite = true;
                backupExistsTime = 0;
            } else {
                synchronized (this.mLock) {
                    try {
                        backupExistsTime = 0;
                        if (this.mCurrentMemoryStateGeneration == memoryCommitResult.memoryStateGeneration) {
                            needsWrite = true;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                }
            }
            if (!needsWrite) {
                memoryCommitResult.setDiskWriteResult(false, true);
                return;
            } else if (this.mBackupFile.exists()) {
                this.mFile.delete();
            } else if (!this.mFile.renameTo(this.mBackupFile)) {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append("Couldn't rename file ");
                stringBuilder2.append(this.mFile);
                stringBuilder2.append(" to backup file ");
                stringBuilder2.append(this.mBackupFile);
                Log.e(TAG, stringBuilder2.toString());
                memoryCommitResult.setDiskWriteResult(false, false);
                return;
            }
        }
        existsTime = 0;
        backupExistsTime = 0;
        try {
            FileOutputStream str = createFileOutputStream(this.mFile);
            if (str == null) {
                memoryCommitResult.setDiskWriteResult(false, false);
                return;
            }
            XmlUtils.writeMapXml(memoryCommitResult.mapToWriteToDisk, str);
            long writeTime = System.currentTimeMillis();
            FileUtils.sync(str);
            long fsyncTime = System.currentTimeMillis();
            str.close();
            ContextImpl.setFilePermissionsFromMode(this.mFile.getPath(), this.mMode, 0);
            try {
                StructStat stat = Os.stat(this.mFile.getPath());
                synchronized (this.mLock) {
                    this.mStatTimestamp = stat.st_mtim;
                    this.mStatSize = stat.st_size;
                }
            } catch (ErrnoException e) {
            }
            this.mBackupFile.delete();
            this.mDiskStateGeneration = memoryCommitResult.memoryStateGeneration;
            memoryCommitResult.setDiskWriteResult(true, true);
            long fsyncDuration = fsyncTime - writeTime;
            this.mSyncTimes.add((int) fsyncDuration);
            this.mNumSync++;
            if (this.mNumSync % 1024 == 0 || fsyncDuration > 256) {
                ExponentiallyBucketedHistogram exponentiallyBucketedHistogram = this.mSyncTimes;
                String str2 = TAG;
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Time required to fsync ");
                stringBuilder3.append(this.mFile);
                stringBuilder3.append(": ");
                exponentiallyBucketedHistogram.log(str2, stringBuilder3.toString());
            }
        } catch (XmlPullParserException e2) {
            Log.w(TAG, "writeToFile: Got exception:", e2);
            if (this.mFile.exists() && !this.mFile.delete()) {
                stringBuilder = new StringBuilder();
                stringBuilder.append("Couldn't clean up partially-written file ");
                stringBuilder.append(this.mFile);
                Log.e(TAG, stringBuilder.toString());
            }
            memoryCommitResult.setDiskWriteResult(false, false);
        } catch (IOException e3) {
            Log.w(TAG, "writeToFile: Got exception:", e3);
            stringBuilder = new StringBuilder();
            stringBuilder.append("Couldn't clean up partially-written file ");
            stringBuilder.append(this.mFile);
            Log.e(TAG, stringBuilder.toString());
            memoryCommitResult.setDiskWriteResult(false, false);
        }
    }
}
