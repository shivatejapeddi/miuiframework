package com.android.internal.os;

import android.os.SystemClock;
import java.nio.CharBuffer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

public class KernelCpuProcStringReader {
    private static final KernelCpuProcStringReader ACTIVE_TIME_READER = new KernelCpuProcStringReader(PROC_UID_ACTIVE_TIME);
    private static final KernelCpuProcStringReader CLUSTER_TIME_READER = new KernelCpuProcStringReader(PROC_UID_CLUSTER_TIME);
    private static final int ERROR_THRESHOLD = 5;
    private static final KernelCpuProcStringReader FREQ_TIME_READER = new KernelCpuProcStringReader(PROC_UID_FREQ_TIME);
    private static final long FRESHNESS = 500;
    private static final int MAX_BUFFER_SIZE = 1048576;
    private static final String PROC_UID_ACTIVE_TIME = "/proc/uid_concurrent_active_time";
    private static final String PROC_UID_CLUSTER_TIME = "/proc/uid_concurrent_policy_time";
    private static final String PROC_UID_FREQ_TIME = "/proc/uid_time_in_state";
    private static final String PROC_UID_USER_SYS_TIME = "/proc/uid_cputime/show_uid_stat";
    private static final String TAG = KernelCpuProcStringReader.class.getSimpleName();
    private static final KernelCpuProcStringReader USER_SYS_TIME_READER = new KernelCpuProcStringReader(PROC_UID_USER_SYS_TIME);
    private char[] mBuf;
    private int mErrors = 0;
    private final Path mFile;
    private long mLastReadTime = 0;
    private final ReentrantReadWriteLock mLock = new ReentrantReadWriteLock();
    private final ReadLock mReadLock = this.mLock.readLock();
    private int mSize;
    private final WriteLock mWriteLock = this.mLock.writeLock();

    public class ProcFileIterator implements AutoCloseable {
        private int mPos;
        private final int mSize;

        public ProcFileIterator(int size) {
            this.mSize = size;
        }

        public boolean hasNextLine() {
            return this.mPos < this.mSize;
        }

        public CharBuffer nextLine() {
            if (this.mPos >= this.mSize) {
                return null;
            }
            int i = this.mPos;
            while (i < this.mSize && KernelCpuProcStringReader.this.mBuf[i] != 10) {
                i++;
            }
            int start = this.mPos;
            this.mPos = i + 1;
            return CharBuffer.wrap(KernelCpuProcStringReader.this.mBuf, start, i - start);
        }

        public int size() {
            return this.mSize;
        }

        public void close() {
            KernelCpuProcStringReader.this.mReadLock.unlock();
        }
    }

    static KernelCpuProcStringReader getFreqTimeReaderInstance() {
        return FREQ_TIME_READER;
    }

    static KernelCpuProcStringReader getActiveTimeReaderInstance() {
        return ACTIVE_TIME_READER;
    }

    static KernelCpuProcStringReader getClusterTimeReaderInstance() {
        return CLUSTER_TIME_READER;
    }

    static KernelCpuProcStringReader getUserSysTimeReaderInstance() {
        return USER_SYS_TIME_READER;
    }

    public KernelCpuProcStringReader(String file) {
        this.mFile = Paths.get(file, new String[0]);
    }

    public ProcFileIterator open() {
        return open(false);
    }

    /* JADX WARNING: Missing block: B:42:0x00d4, code skipped:
            if (r3 != null) goto L_0x00d6;
     */
    /* JADX WARNING: Missing block: B:44:?, code skipped:
            r3.close();
     */
    public com.android.internal.os.KernelCpuProcStringReader.ProcFileIterator open(boolean r9) {
        /*
        r8 = this;
        r0 = r8.mErrors;
        r1 = 0;
        r2 = 5;
        if (r0 < r2) goto L_0x0007;
    L_0x0006:
        return r1;
    L_0x0007:
        if (r9 == 0) goto L_0x000f;
    L_0x0009:
        r0 = r8.mWriteLock;
        r0.lock();
        goto L_0x0044;
    L_0x000f:
        r0 = r8.mReadLock;
        r0.lock();
        r0 = r8.dataValid();
        if (r0 == 0) goto L_0x0022;
    L_0x001a:
        r0 = new com.android.internal.os.KernelCpuProcStringReader$ProcFileIterator;
        r1 = r8.mSize;
        r0.<init>(r1);
        return r0;
    L_0x0022:
        r0 = r8.mReadLock;
        r0.unlock();
        r0 = r8.mWriteLock;
        r0.lock();
        r0 = r8.dataValid();
        if (r0 == 0) goto L_0x0044;
    L_0x0032:
        r0 = r8.mReadLock;
        r0.lock();
        r0 = r8.mWriteLock;
        r0.unlock();
        r0 = new com.android.internal.os.KernelCpuProcStringReader$ProcFileIterator;
        r1 = r8.mSize;
        r0.<init>(r1);
        return r0;
    L_0x0044:
        r0 = 0;
        r2 = 0;
        r8.mSize = r2;
        r2 = android.os.StrictMode.allowThreadDiskReadsMask();
        r3 = r8.mFile;	 Catch:{ FileNotFoundException | NoSuchFileException -> 0x0101, FileNotFoundException | NoSuchFileException -> 0x0101, IOException -> 0x00e1 }
        r3 = java.nio.file.Files.newBufferedReader(r3);	 Catch:{ FileNotFoundException | NoSuchFileException -> 0x0101, FileNotFoundException | NoSuchFileException -> 0x0101, IOException -> 0x00e1 }
        r4 = r8.mBuf;	 Catch:{ all -> 0x00d1 }
        if (r4 != 0) goto L_0x005c;
    L_0x0056:
        r4 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r4 = new char[r4];	 Catch:{ all -> 0x00d1 }
        r8.mBuf = r4;	 Catch:{ all -> 0x00d1 }
    L_0x005c:
        r4 = r8.mBuf;	 Catch:{ all -> 0x00d1 }
        r5 = r8.mBuf;	 Catch:{ all -> 0x00d1 }
        r5 = r5.length;	 Catch:{ all -> 0x00d1 }
        r5 = r5 - r0;
        r4 = r3.read(r4, r0, r5);	 Catch:{ all -> 0x00d1 }
        r5 = r4;
        if (r4 < 0) goto L_0x00b3;
    L_0x0069:
        r0 = r0 + r5;
        r4 = r8.mBuf;	 Catch:{ all -> 0x00d1 }
        r4 = r4.length;	 Catch:{ all -> 0x00d1 }
        if (r0 != r4) goto L_0x005c;
    L_0x006f:
        r4 = r8.mBuf;	 Catch:{ all -> 0x00d1 }
        r4 = r4.length;	 Catch:{ all -> 0x00d1 }
        r6 = 1048576; // 0x100000 float:1.469368E-39 double:5.180654E-318;
        if (r4 != r6) goto L_0x00a1;
    L_0x0076:
        r4 = r8.mErrors;	 Catch:{ all -> 0x00d1 }
        r4 = r4 + 1;
        r8.mErrors = r4;	 Catch:{ all -> 0x00d1 }
        r4 = TAG;	 Catch:{ all -> 0x00d1 }
        r6 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d1 }
        r6.<init>();	 Catch:{ all -> 0x00d1 }
        r7 = "Proc file too large: ";
        r6.append(r7);	 Catch:{ all -> 0x00d1 }
        r7 = r8.mFile;	 Catch:{ all -> 0x00d1 }
        r6.append(r7);	 Catch:{ all -> 0x00d1 }
        r6 = r6.toString();	 Catch:{ all -> 0x00d1 }
        android.util.Slog.e(r4, r6);	 Catch:{ all -> 0x00d1 }
        r3.close();	 Catch:{ FileNotFoundException | NoSuchFileException -> 0x0101, FileNotFoundException | NoSuchFileException -> 0x0101, IOException -> 0x00e1 }
        android.os.StrictMode.setThreadPolicyMask(r2);
        r4 = r8.mWriteLock;
        r4.unlock();
        return r1;
    L_0x00a1:
        r4 = r8.mBuf;	 Catch:{ all -> 0x00d1 }
        r7 = r8.mBuf;	 Catch:{ all -> 0x00d1 }
        r7 = r7.length;	 Catch:{ all -> 0x00d1 }
        r7 = r7 << 1;
        r6 = java.lang.Math.min(r7, r6);	 Catch:{ all -> 0x00d1 }
        r4 = java.util.Arrays.copyOf(r4, r6);	 Catch:{ all -> 0x00d1 }
        r8.mBuf = r4;	 Catch:{ all -> 0x00d1 }
        goto L_0x005c;
    L_0x00b3:
        r8.mSize = r0;	 Catch:{ all -> 0x00d1 }
        r6 = android.os.SystemClock.elapsedRealtime();	 Catch:{ all -> 0x00d1 }
        r8.mLastReadTime = r6;	 Catch:{ all -> 0x00d1 }
        r4 = r8.mReadLock;	 Catch:{ all -> 0x00d1 }
        r4.lock();	 Catch:{ all -> 0x00d1 }
        r4 = new com.android.internal.os.KernelCpuProcStringReader$ProcFileIterator;	 Catch:{ all -> 0x00d1 }
        r4.<init>(r0);	 Catch:{ all -> 0x00d1 }
        r3.close();	 Catch:{ FileNotFoundException | NoSuchFileException -> 0x0101, FileNotFoundException | NoSuchFileException -> 0x0101, IOException -> 0x00e1 }
        android.os.StrictMode.setThreadPolicyMask(r2);
        r1 = r8.mWriteLock;
        r1.unlock();
        return r4;
    L_0x00d1:
        r4 = move-exception;
        throw r4;	 Catch:{ all -> 0x00d3 }
    L_0x00d3:
        r5 = move-exception;
        if (r3 == 0) goto L_0x00de;
    L_0x00d6:
        r3.close();	 Catch:{ all -> 0x00da }
        goto L_0x00de;
    L_0x00da:
        r6 = move-exception;
        r4.addSuppressed(r6);	 Catch:{ FileNotFoundException | NoSuchFileException -> 0x0101, FileNotFoundException | NoSuchFileException -> 0x0101, IOException -> 0x00e1 }
    L_0x00de:
        throw r5;	 Catch:{ FileNotFoundException | NoSuchFileException -> 0x0101, FileNotFoundException | NoSuchFileException -> 0x0101, IOException -> 0x00e1 }
    L_0x00df:
        r1 = move-exception;
        goto L_0x012b;
    L_0x00e1:
        r3 = move-exception;
        r4 = r8.mErrors;	 Catch:{ all -> 0x00df }
        r4 = r4 + 1;
        r8.mErrors = r4;	 Catch:{ all -> 0x00df }
        r4 = TAG;	 Catch:{ all -> 0x00df }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00df }
        r5.<init>();	 Catch:{ all -> 0x00df }
        r6 = "Error reading ";
        r5.append(r6);	 Catch:{ all -> 0x00df }
        r6 = r8.mFile;	 Catch:{ all -> 0x00df }
        r5.append(r6);	 Catch:{ all -> 0x00df }
        r5 = r5.toString();	 Catch:{ all -> 0x00df }
        android.util.Slog.e(r4, r5, r3);	 Catch:{ all -> 0x00df }
        goto L_0x0120;
    L_0x0101:
        r3 = move-exception;
        r4 = r8.mErrors;	 Catch:{ all -> 0x00df }
        r4 = r4 + 1;
        r8.mErrors = r4;	 Catch:{ all -> 0x00df }
        r4 = TAG;	 Catch:{ all -> 0x00df }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00df }
        r5.<init>();	 Catch:{ all -> 0x00df }
        r6 = "File not found. It's normal if not implemented: ";
        r5.append(r6);	 Catch:{ all -> 0x00df }
        r6 = r8.mFile;	 Catch:{ all -> 0x00df }
        r5.append(r6);	 Catch:{ all -> 0x00df }
        r5 = r5.toString();	 Catch:{ all -> 0x00df }
        android.util.Slog.w(r4, r5);	 Catch:{ all -> 0x00df }
        android.os.StrictMode.setThreadPolicyMask(r2);
        r3 = r8.mWriteLock;
        r3.unlock();
        return r1;
    L_0x012b:
        android.os.StrictMode.setThreadPolicyMask(r2);
        r3 = r8.mWriteLock;
        r3.unlock();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.KernelCpuProcStringReader.open(boolean):com.android.internal.os.KernelCpuProcStringReader$ProcFileIterator");
    }

    private boolean dataValid() {
        return this.mSize > 0 && SystemClock.elapsedRealtime() - this.mLastReadTime < 500;
    }

    public static int asLongs(CharBuffer buf, long[] array) {
        if (buf == null) {
            return -1;
        }
        int initialPos = buf.position();
        int count = 0;
        long num = -1;
        while (buf.remaining() > 0 && count < array.length) {
            char c = buf.get();
            if (!isNumber(c) && c != ' ' && c != ':') {
                buf.position(initialPos);
                return -2;
            } else if (num < 0) {
                if (isNumber(c)) {
                    num = (long) (c - 48);
                }
            } else if (isNumber(c)) {
                num = ((10 * num) + ((long) c)) - 48;
                if (num < 0) {
                    buf.position(initialPos);
                    return -3;
                }
            } else {
                int count2 = count + 1;
                array[count] = num;
                num = -1;
                count = count2;
            }
        }
        if (num >= 0) {
            int count3 = count + 1;
            array[count] = num;
            count = count3;
        }
        buf.position(initialPos);
        return count;
    }

    private static boolean isNumber(char c) {
        return c >= '0' && c <= '9';
    }
}
