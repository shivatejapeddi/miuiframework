package android.os;

import android.content.Context;
import android.os.storage.StorageManager;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Slog;
import com.android.internal.annotations.VisibleForTesting;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.Arrays;
import libcore.io.IoUtils;
import libcore.util.EmptyArray;

public class RedactingFileDescriptor {
    private static final boolean DEBUG = true;
    private static final String TAG = "RedactingFileDescriptor";
    private final ProxyFileDescriptorCallback mCallback = new ProxyFileDescriptorCallback() {
        public long onGetSize() throws ErrnoException {
            return Os.fstat(RedactingFileDescriptor.this.mInner).st_size;
        }

        public int onRead(long offset, int size, byte[] data) throws ErrnoException {
            AnonymousClass1 anonymousClass1 = this;
            long j = offset;
            int i = size;
            int n = 0;
            while (n < i) {
                try {
                    byte[] bArr = data;
                    int i2 = n;
                    int res = Os.pread(RedactingFileDescriptor.this.mInner, bArr, i2, i - n, j + ((long) n));
                    if (res == 0) {
                        break;
                    }
                    n += res;
                } catch (InterruptedIOException e) {
                    n += e.bytesTransferred;
                }
            }
            long[] ranges = RedactingFileDescriptor.this.mRedactRanges;
            int i3 = 0;
            while (i3 < ranges.length) {
                long start = Math.max(j, ranges[i3]);
                long end = Math.min(((long) i) + j, ranges[i3 + 1]);
                for (long j2 = start; j2 < end; j2++) {
                    data[(int) (j2 - j)] = (byte) 0;
                }
                long[] access$200 = RedactingFileDescriptor.this.mFreeOffsets;
                int length = access$200.length;
                int i4 = 0;
                while (i4 < length) {
                    long freeEnd;
                    long freeOffset = access$200[i4];
                    long[] ranges2 = ranges;
                    ranges = freeOffset + 4;
                    long redactFreeStart = Math.max(freeOffset, start);
                    long redactFreeEnd = Math.min(ranges, end);
                    long j3 = redactFreeStart;
                    while (j3 < redactFreeEnd) {
                        freeEnd = ranges;
                        data[(int) (j3 - j)] = (byte) "free".charAt((int) (j3 - freeOffset));
                        j3 += 1;
                        j = offset;
                        ranges = freeEnd;
                    }
                    freeEnd = ranges;
                    i4++;
                    j = offset;
                    ranges = ranges2;
                }
                i3 += 2;
                anonymousClass1 = this;
                j = offset;
            }
            return n;
        }

        public int onWrite(long offset, int size, byte[] data) throws ErrnoException {
            int n = 0;
            while (n < size) {
                try {
                    byte[] bArr = data;
                    int i = n;
                    int res = Os.pwrite(RedactingFileDescriptor.this.mInner, bArr, i, size - n, offset + ((long) n));
                    if (res == 0) {
                        break;
                    }
                    n += res;
                } catch (InterruptedIOException e) {
                    n += e.bytesTransferred;
                }
            }
            RedactingFileDescriptor redactingFileDescriptor = RedactingFileDescriptor.this;
            redactingFileDescriptor.mRedactRanges = RedactingFileDescriptor.removeRange(redactingFileDescriptor.mRedactRanges, offset, ((long) n) + offset);
            return n;
        }

        public void onFsync() throws ErrnoException {
            Os.fsync(RedactingFileDescriptor.this.mInner);
        }

        public void onRelease() {
            Slog.v(RedactingFileDescriptor.TAG, "onRelease()");
            IoUtils.closeQuietly(RedactingFileDescriptor.this.mInner);
        }
    };
    private volatile long[] mFreeOffsets;
    private FileDescriptor mInner = null;
    private ParcelFileDescriptor mOuter = null;
    private volatile long[] mRedactRanges;

    private RedactingFileDescriptor(Context context, File file, int mode, long[] redactRanges, long[] freeOffsets) throws IOException {
        this.mRedactRanges = checkRangesArgument(redactRanges);
        this.mFreeOffsets = freeOffsets;
        try {
            this.mInner = Os.open(file.getAbsolutePath(), FileUtils.translateModePfdToPosix(mode), 0);
            this.mOuter = ((StorageManager) context.getSystemService(StorageManager.class)).openProxyFileDescriptor(mode, this.mCallback);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        } catch (IOException e2) {
            IoUtils.closeQuietly(this.mInner);
            IoUtils.closeQuietly(this.mOuter);
            throw e2;
        }
    }

    private static long[] checkRangesArgument(long[] ranges) {
        if (ranges.length % 2 == 0) {
            int i = 0;
            while (i < ranges.length - 1) {
                if (ranges[i] <= ranges[i + 1]) {
                    i += 2;
                } else {
                    throw new IllegalArgumentException();
                }
            }
            return ranges;
        }
        throw new IllegalArgumentException();
    }

    public static ParcelFileDescriptor open(Context context, File file, int mode, long[] redactRanges, long[] freePositions) throws IOException {
        return new RedactingFileDescriptor(context, file, mode, redactRanges, freePositions).mOuter;
    }

    @VisibleForTesting
    public static long[] removeRange(long[] ranges, long start, long end) {
        if (start == end) {
            return ranges;
        }
        if (start <= end) {
            long[] res = EmptyArray.LONG;
            int i = 0;
            while (i < ranges.length) {
                if (start > ranges[i] || end < ranges[i + 1]) {
                    if (start < ranges[i] || end > ranges[i + 1]) {
                        res = Arrays.copyOf(res, res.length + 2);
                        if (end < ranges[i] || end > ranges[i + 1]) {
                            res[res.length - 2] = ranges[i];
                        } else {
                            res[res.length - 2] = Math.max(ranges[i], end);
                        }
                        if (start < ranges[i] || start > ranges[i + 1]) {
                            res[res.length - 1] = ranges[i + 1];
                        } else {
                            res[res.length - 1] = Math.min(ranges[i + 1], start);
                        }
                    } else {
                        res = Arrays.copyOf(res, res.length + 4);
                        res[res.length - 4] = ranges[i];
                        res[res.length - 3] = start;
                        res[res.length - 2] = end;
                        res[res.length - 1] = ranges[i + 1];
                    }
                }
                i += 2;
            }
            return res;
        }
        throw new IllegalArgumentException();
    }
}
