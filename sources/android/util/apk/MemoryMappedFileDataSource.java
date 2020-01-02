package android.util.apk;

import android.system.Os;
import android.system.OsConstants;
import java.io.FileDescriptor;

class MemoryMappedFileDataSource implements DataSource {
    private static final long MEMORY_PAGE_SIZE_BYTES = Os.sysconf(OsConstants._SC_PAGESIZE);
    private final FileDescriptor mFd;
    private final long mFilePosition;
    private final long mSize;

    MemoryMappedFileDataSource(FileDescriptor fd, long position, long size) {
        this.mFd = fd;
        this.mFilePosition = position;
        this.mSize = size;
    }

    public long size() {
        return this.mSize;
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x00a7 A:{SYNTHETIC, Splitter:B:42:0x00a7} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00a7 A:{SYNTHETIC, Splitter:B:42:0x00a7} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00a7 A:{SYNTHETIC, Splitter:B:42:0x00a7} */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00a7 A:{SYNTHETIC, Splitter:B:42:0x00a7} */
    public void feedIntoDataDigester(android.util.apk.DataDigester r24, long r25, int r27) throws java.io.IOException, java.security.DigestException {
        /*
        r23 = this;
        r1 = r23;
        r2 = r1.mFilePosition;
        r2 = r2 + r25;
        r4 = MEMORY_PAGE_SIZE_BYTES;
        r6 = r2 / r4;
        r4 = r4 * r6;
        r6 = r2 - r4;
        r6 = (int) r6;
        r0 = r27 + r6;
        r14 = (long) r0;
        r17 = 0;
        r8 = 0;
        r19 = 0;
        r12 = android.system.OsConstants.PROT_READ;	 Catch:{ ErrnoException -> 0x007f, all -> 0x0077 }
        r0 = android.system.OsConstants.MAP_SHARED;	 Catch:{ ErrnoException -> 0x007f, all -> 0x0077 }
        r7 = android.system.OsConstants.MAP_POPULATE;	 Catch:{ ErrnoException -> 0x007f, all -> 0x0077 }
        r13 = r0 | r7;
        r0 = r1.mFd;	 Catch:{ ErrnoException -> 0x007f, all -> 0x0077 }
        r10 = r14;
        r21 = r14;
        r14 = r0;
        r15 = r4;
        r7 = android.system.Os.mmap(r8, r10, r12, r13, r14, r15);	 Catch:{ ErrnoException -> 0x0071, all -> 0x0068 }
        r14 = r7;
        r0 = new java.nio.DirectByteBuffer;	 Catch:{ ErrnoException -> 0x0060, all -> 0x0059 }
        r7 = (long) r6;	 Catch:{ ErrnoException -> 0x0060, all -> 0x0059 }
        r9 = r14 + r7;
        r11 = r1.mFd;	 Catch:{ ErrnoException -> 0x0060, all -> 0x0059 }
        r12 = 0;
        r13 = 1;
        r7 = r0;
        r8 = r27;
        r7.<init>(r8, r9, r11, r12, r13);	 Catch:{ ErrnoException -> 0x0060, all -> 0x0059 }
        r7 = r24;
        r7.consume(r0);	 Catch:{ ErrnoException -> 0x0053, all -> 0x004e }
        r0 = (r14 > r19 ? 1 : (r14 == r19 ? 0 : -1));
        if (r0 == 0) goto L_0x004b;
    L_0x0043:
        r8 = r21;
        android.system.Os.munmap(r14, r8);	 Catch:{ ErrnoException -> 0x0049 }
    L_0x0048:
        goto L_0x004d;
    L_0x0049:
        r0 = move-exception;
        goto L_0x0048;
    L_0x004b:
        r8 = r21;
    L_0x004d:
        return;
    L_0x004e:
        r0 = move-exception;
        r8 = r21;
        r10 = r0;
        goto L_0x00a3;
    L_0x0053:
        r0 = move-exception;
        r8 = r21;
        r17 = r14;
        goto L_0x0083;
    L_0x0059:
        r0 = move-exception;
        r7 = r24;
        r8 = r21;
        r10 = r0;
        goto L_0x00a3;
    L_0x0060:
        r0 = move-exception;
        r7 = r24;
        r8 = r21;
        r17 = r14;
        goto L_0x0083;
    L_0x0068:
        r0 = move-exception;
        r7 = r24;
        r8 = r21;
        r10 = r0;
        r14 = r17;
        goto L_0x00a3;
    L_0x0071:
        r0 = move-exception;
        r7 = r24;
        r8 = r21;
        goto L_0x0083;
    L_0x0077:
        r0 = move-exception;
        r7 = r24;
        r8 = r14;
        r10 = r0;
        r14 = r17;
        goto L_0x00a3;
    L_0x007f:
        r0 = move-exception;
        r7 = r24;
        r8 = r14;
    L_0x0083:
        r10 = new java.io.IOException;	 Catch:{ all -> 0x009f }
        r11 = new java.lang.StringBuilder;	 Catch:{ all -> 0x009f }
        r11.<init>();	 Catch:{ all -> 0x009f }
        r12 = "Failed to mmap ";
        r11.append(r12);	 Catch:{ all -> 0x009f }
        r11.append(r8);	 Catch:{ all -> 0x009f }
        r12 = " bytes";
        r11.append(r12);	 Catch:{ all -> 0x009f }
        r11 = r11.toString();	 Catch:{ all -> 0x009f }
        r10.<init>(r11, r0);	 Catch:{ all -> 0x009f }
        throw r10;	 Catch:{ all -> 0x009f }
    L_0x009f:
        r0 = move-exception;
        r10 = r0;
        r14 = r17;
    L_0x00a3:
        r0 = (r14 > r19 ? 1 : (r14 == r19 ? 0 : -1));
        if (r0 == 0) goto L_0x00ad;
    L_0x00a7:
        android.system.Os.munmap(r14, r8);	 Catch:{ ErrnoException -> 0x00ab }
    L_0x00aa:
        goto L_0x00ad;
    L_0x00ab:
        r0 = move-exception;
        goto L_0x00aa;
    L_0x00ad:
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.apk.MemoryMappedFileDataSource.feedIntoDataDigester(android.util.apk.DataDigester, long, int):void");
    }
}
