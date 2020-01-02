package android.os;

import android.annotation.UnsupportedAppUsage;
import android.app.backup.FullBackup;
import android.content.ContentResolver;
import android.miui.BiometricConnect;
import android.provider.DocumentsContract.Document;
import android.system.ErrnoException;
import android.system.Os;
import android.system.OsConstants;
import android.system.StructStat;
import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.Slog;
import android.webkit.MimeTypeMap;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.internal.util.SizedInputStream;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import libcore.io.IoUtils;
import libcore.util.EmptyArray;

public final class FileUtils {
    private static final long COPY_CHECKPOINT_BYTES = 524288;
    private static final long SLOW_LOG_TIMEOUT = 100;
    public static final int S_IRGRP = 32;
    public static final int S_IROTH = 4;
    public static final int S_IRUSR = 256;
    public static final int S_IRWXG = 56;
    public static final int S_IRWXO = 7;
    public static final int S_IRWXU = 448;
    public static final int S_IWGRP = 16;
    public static final int S_IWOTH = 2;
    public static final int S_IWUSR = 128;
    public static final int S_IXGRP = 8;
    public static final int S_IXOTH = 1;
    public static final int S_IXUSR = 64;
    private static final String TAG = "FileUtils";
    private static boolean sEnableCopyOptimizations = true;

    @VisibleForTesting
    public static class MemoryPipe extends Thread implements AutoCloseable {
        private final byte[] data;
        private final FileDescriptor[] pipe;
        private final boolean sink;

        private MemoryPipe(byte[] data, boolean sink) throws IOException {
            try {
                this.pipe = Os.pipe();
                this.data = data;
                this.sink = sink;
            } catch (ErrnoException e) {
                throw e.rethrowAsIOException();
            }
        }

        private MemoryPipe startInternal() {
            super.start();
            return this;
        }

        public static MemoryPipe createSource(byte[] data) throws IOException {
            return new MemoryPipe(data, false).startInternal();
        }

        public static MemoryPipe createSink(byte[] data) throws IOException {
            return new MemoryPipe(data, true).startInternal();
        }

        public FileDescriptor getFD() {
            return this.sink ? this.pipe[1] : this.pipe[0];
        }

        public FileDescriptor getInternalFD() {
            return this.sink ? this.pipe[0] : this.pipe[1];
        }

        /* JADX WARNING: Missing block: B:11:0x002a, code skipped:
            if (r6.sink != false) goto L_0x0044;
     */
        /* JADX WARNING: Missing block: B:20:0x0042, code skipped:
            if (r6.sink == false) goto L_0x004d;
     */
        /* JADX WARNING: Missing block: B:21:0x0044, code skipped:
            android.os.SystemClock.sleep(java.util.concurrent.TimeUnit.SECONDS.toMillis(1));
     */
        /* JADX WARNING: Missing block: B:22:0x004d, code skipped:
            libcore.io.IoUtils.closeQuietly(r0);
     */
        /* JADX WARNING: Missing block: B:23:0x0051, code skipped:
            return;
     */
        public void run() {
            /*
            r6 = this;
            r0 = r6.getInternalFD();
            r1 = 0;
        L_0x0005:
            r2 = 1;
            r4 = r6.data;	 Catch:{ ErrnoException | IOException -> 0x003f, ErrnoException | IOException -> 0x003f, all -> 0x002d }
            r4 = r4.length;	 Catch:{ ErrnoException | IOException -> 0x003f, ErrnoException | IOException -> 0x003f, all -> 0x002d }
            if (r1 >= r4) goto L_0x0028;
        L_0x000c:
            r4 = r6.sink;	 Catch:{ ErrnoException | IOException -> 0x003f, ErrnoException | IOException -> 0x003f, all -> 0x002d }
            if (r4 == 0) goto L_0x001c;
        L_0x0010:
            r4 = r6.data;	 Catch:{ ErrnoException | IOException -> 0x003f, ErrnoException | IOException -> 0x003f, all -> 0x002d }
            r5 = r6.data;	 Catch:{ ErrnoException | IOException -> 0x003f, ErrnoException | IOException -> 0x003f, all -> 0x002d }
            r5 = r5.length;	 Catch:{ ErrnoException | IOException -> 0x003f, ErrnoException | IOException -> 0x003f, all -> 0x002d }
            r5 = r5 - r1;
            r2 = android.system.Os.read(r0, r4, r1, r5);	 Catch:{ ErrnoException | IOException -> 0x003f, ErrnoException | IOException -> 0x003f, all -> 0x002d }
            r1 = r1 + r2;
            goto L_0x0005;
        L_0x001c:
            r4 = r6.data;	 Catch:{ ErrnoException | IOException -> 0x003f, ErrnoException | IOException -> 0x003f, all -> 0x002d }
            r5 = r6.data;	 Catch:{ ErrnoException | IOException -> 0x003f, ErrnoException | IOException -> 0x003f, all -> 0x002d }
            r5 = r5.length;	 Catch:{ ErrnoException | IOException -> 0x003f, ErrnoException | IOException -> 0x003f, all -> 0x002d }
            r5 = r5 - r1;
            r2 = android.system.Os.write(r0, r4, r1, r5);	 Catch:{ ErrnoException | IOException -> 0x003f, ErrnoException | IOException -> 0x003f, all -> 0x002d }
            r1 = r1 + r2;
            goto L_0x0005;
        L_0x0028:
            r1 = r6.sink;
            if (r1 == 0) goto L_0x004d;
        L_0x002c:
            goto L_0x0044;
        L_0x002d:
            r1 = move-exception;
            r4 = r6.sink;
            if (r4 == 0) goto L_0x003b;
        L_0x0032:
            r4 = java.util.concurrent.TimeUnit.SECONDS;
            r2 = r4.toMillis(r2);
            android.os.SystemClock.sleep(r2);
        L_0x003b:
            libcore.io.IoUtils.closeQuietly(r0);
            throw r1;
        L_0x003f:
            r1 = move-exception;
            r1 = r6.sink;
            if (r1 == 0) goto L_0x004d;
        L_0x0044:
            r1 = java.util.concurrent.TimeUnit.SECONDS;
            r1 = r1.toMillis(r2);
            android.os.SystemClock.sleep(r1);
        L_0x004d:
            libcore.io.IoUtils.closeQuietly(r0);
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.os.FileUtils$MemoryPipe.run():void");
        }

        public void close() throws Exception {
            IoUtils.closeQuietly(getFD());
        }
    }

    private static class NoImagePreloadHolder {
        public static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("[\\w%+,./=_-]+");

        private NoImagePreloadHolder() {
        }
    }

    public interface ProgressListener {
        void onProgress(long j);
    }

    @UnsupportedAppUsage
    private FileUtils() {
    }

    @UnsupportedAppUsage
    public static int setPermissions(File path, int mode, int uid, int gid) {
        return setPermissions(path.getAbsolutePath(), mode, uid, gid);
    }

    @UnsupportedAppUsage
    public static int setPermissions(String path, int mode, int uid, int gid) {
        StringBuilder stringBuilder;
        String str = "): ";
        String str2 = TAG;
        try {
            Os.chmod(path, mode);
            if (uid >= 0 || gid >= 0) {
                try {
                    Os.chown(path, uid, gid);
                } catch (ErrnoException e) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to chown(");
                    stringBuilder.append(path);
                    stringBuilder.append(str);
                    stringBuilder.append(e);
                    Slog.w(str2, stringBuilder.toString());
                    return e.errno;
                }
            }
            return 0;
        } catch (ErrnoException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to chmod(");
            stringBuilder.append(path);
            stringBuilder.append(str);
            stringBuilder.append(e2);
            Slog.w(str2, stringBuilder.toString());
            return e2.errno;
        }
    }

    @UnsupportedAppUsage
    public static int setPermissions(FileDescriptor fd, int mode, int uid, int gid) {
        StringBuilder stringBuilder;
        String str = TAG;
        try {
            Os.fchmod(fd, mode);
            if (uid >= 0 || gid >= 0) {
                try {
                    Os.fchown(fd, uid, gid);
                } catch (ErrnoException e) {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to fchown(): ");
                    stringBuilder.append(e);
                    Slog.w(str, stringBuilder.toString());
                    return e.errno;
                }
            }
            return 0;
        } catch (ErrnoException e2) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Failed to fchmod(): ");
            stringBuilder.append(e2);
            Slog.w(str, stringBuilder.toString());
            return e2.errno;
        }
    }

    public static void copyPermissions(File from, File to) throws IOException {
        try {
            StructStat stat = Os.stat(from.getAbsolutePath());
            Os.chmod(to.getAbsolutePath(), stat.st_mode);
            Os.chown(to.getAbsolutePath(), stat.st_uid, stat.st_gid);
        } catch (ErrnoException e) {
            throw e.rethrowAsIOException();
        }
    }

    @Deprecated
    public static int getUid(String path) {
        try {
            return Os.stat(path).st_uid;
        } catch (ErrnoException e) {
            return -1;
        }
    }

    @UnsupportedAppUsage
    public static boolean sync(FileOutputStream stream) {
        if (stream != null) {
            try {
                long startTime = SystemClock.uptimeMillis();
                stream.getFD().sync();
                long fsyncDuration = SystemClock.uptimeMillis() - startTime;
                if (fsyncDuration > SLOW_LOG_TIMEOUT) {
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Slow Operation: fsync took ");
                    stringBuilder.append(fsyncDuration);
                    stringBuilder.append("ms");
                    Slog.w(str, stringBuilder.toString());
                }
            } catch (IOException e) {
                return false;
            }
        }
        return true;
    }

    @Deprecated
    @UnsupportedAppUsage
    public static boolean copyFile(File srcFile, File destFile) {
        try {
            copyFileOrThrow(srcFile, destFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /* JADX WARNING: Missing block: B:9:0x0010, code skipped:
            $closeResource(r1, r0);
     */
    @java.lang.Deprecated
    public static void copyFileOrThrow(java.io.File r3, java.io.File r4) throws java.io.IOException {
        /*
        r0 = new java.io.FileInputStream;
        r0.<init>(r3);
        copyToFileOrThrow(r0, r4);	 Catch:{ all -> 0x000d }
        r1 = 0;
        $closeResource(r1, r0);
        return;
    L_0x000d:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x000f }
    L_0x000f:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.FileUtils.copyFileOrThrow(java.io.File, java.io.File):void");
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

    @Deprecated
    @UnsupportedAppUsage
    public static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            copyToFileOrThrow(inputStream, destFile);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /* JADX WARNING: Missing block: B:18:0x0027, code skipped:
            $closeResource(r1, r0);
     */
    @java.lang.Deprecated
    public static void copyToFileOrThrow(java.io.InputStream r3, java.io.File r4) throws java.io.IOException {
        /*
        r0 = r4.exists();
        if (r0 == 0) goto L_0x0009;
    L_0x0006:
        r4.delete();
    L_0x0009:
        r0 = new java.io.FileOutputStream;
        r0.<init>(r4);
        r1 = 0;
        copy(r3, r0);	 Catch:{ all -> 0x0024 }
        r2 = r0.getFD();	 Catch:{ ErrnoException -> 0x001e }
        android.system.Os.fsync(r2);	 Catch:{ ErrnoException -> 0x001e }
        $closeResource(r1, r0);
        return;
    L_0x001e:
        r1 = move-exception;
        r2 = r1.rethrowAsIOException();	 Catch:{ all -> 0x0024 }
        throw r2;	 Catch:{ all -> 0x0024 }
    L_0x0024:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0026 }
    L_0x0026:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.FileUtils.copyToFileOrThrow(java.io.InputStream, java.io.File):void");
    }

    public static long copy(File from, File to) throws IOException {
        return copy(from, to, null, null, null);
    }

    /* JADX WARNING: Missing block: B:15:?, code skipped:
            $closeResource(r2, r1);
     */
    /* JADX WARNING: Missing block: B:21:0x0021, code skipped:
            $closeResource(r1, r0);
     */
    public static long copy(java.io.File r5, java.io.File r6, android.os.CancellationSignal r7, java.util.concurrent.Executor r8, android.os.FileUtils.ProgressListener r9) throws java.io.IOException {
        /*
        r0 = new java.io.FileInputStream;
        r0.<init>(r5);
        r1 = new java.io.FileOutputStream;	 Catch:{ all -> 0x001e }
        r1.<init>(r6);	 Catch:{ all -> 0x001e }
        r2 = copy(r0, r1, r7, r8, r9);	 Catch:{ all -> 0x0017 }
        r4 = 0;
        $closeResource(r4, r1);	 Catch:{ all -> 0x001e }
        $closeResource(r4, r0);
        return r2;
    L_0x0017:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0019 }
    L_0x0019:
        r3 = move-exception;
        $closeResource(r2, r1);	 Catch:{ all -> 0x001e }
        throw r3;	 Catch:{ all -> 0x001e }
    L_0x001e:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0020 }
    L_0x0020:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.FileUtils.copy(java.io.File, java.io.File, android.os.CancellationSignal, java.util.concurrent.Executor, android.os.FileUtils$ProgressListener):long");
    }

    public static long copy(InputStream in, OutputStream out) throws IOException {
        return copy(in, out, null, null, null);
    }

    public static long copy(InputStream in, OutputStream out, CancellationSignal signal, Executor executor, ProgressListener listener) throws IOException {
        if (sEnableCopyOptimizations && (in instanceof FileInputStream) && (out instanceof FileOutputStream)) {
            return copy(((FileInputStream) in).getFD(), ((FileOutputStream) out).getFD(), signal, executor, listener);
        }
        return copyInternalUserspace(in, out, signal, executor, listener);
    }

    public static long copy(FileDescriptor in, FileDescriptor out) throws IOException {
        return copy(in, out, null, null, null);
    }

    public static long copy(FileDescriptor in, FileDescriptor out, CancellationSignal signal, Executor executor, ProgressListener listener) throws IOException {
        return copy(in, out, Long.MAX_VALUE, signal, executor, listener);
    }

    public static long copy(FileDescriptor in, FileDescriptor out, long count, CancellationSignal signal, Executor executor, ProgressListener listener) throws IOException {
        if (sEnableCopyOptimizations) {
            try {
                StructStat st_in = Os.fstat(in);
                StructStat st_out = Os.fstat(out);
                if (OsConstants.S_ISREG(st_in.st_mode) && OsConstants.S_ISREG(st_out.st_mode)) {
                    return copyInternalSendfile(in, out, count, signal, executor, listener);
                }
                if (!OsConstants.S_ISFIFO(st_in.st_mode)) {
                    if (OsConstants.S_ISFIFO(st_out.st_mode)) {
                    }
                }
                return copyInternalSplice(in, out, count, signal, executor, listener);
            } catch (ErrnoException e) {
                throw e.rethrowAsIOException();
            }
        }
        return copyInternalUserspace(in, out, count, signal, executor, listener);
    }

    @VisibleForTesting
    public static long copyInternalSplice(FileDescriptor in, FileDescriptor out, long count, CancellationSignal signal, Executor executor, ProgressListener listener) throws ErrnoException {
        Executor executor2 = executor;
        ProgressListener progressListener = listener;
        long checkpoint = 0;
        long progress = 0;
        long count2 = count;
        while (true) {
            long j = 524288;
            long splice = Os.splice(in, null, out, null, Math.min(count2, 524288), OsConstants.SPLICE_F_MOVE | OsConstants.SPLICE_F_MORE);
            long t = splice;
            if (splice == 0) {
                break;
            }
            progress += t;
            checkpoint += t;
            count2 -= t;
            if (checkpoint >= j) {
                if (signal != null) {
                    signal.throwIfCanceled();
                }
                if (!(executor2 == null || progressListener == null)) {
                    executor2.execute(new -$$Lambda$FileUtils$RlOy_0MlKMWkkCC1mk_jzWcLTKs(progressListener, progress));
                }
                checkpoint = 0;
            }
        }
        if (!(executor2 == null || progressListener == null)) {
            executor2.execute(new -$$Lambda$FileUtils$e0JoE-HjVf9vMX679eNxZixyUZ0(progressListener, progress));
        }
        return progress;
    }

    @VisibleForTesting
    public static long copyInternalSendfile(FileDescriptor in, FileDescriptor out, long count, CancellationSignal signal, Executor executor, ProgressListener listener) throws ErrnoException {
        Executor executor2 = executor;
        ProgressListener progressListener = listener;
        long checkpoint = 0;
        long progress = 0;
        long count2 = count;
        while (true) {
            FileDescriptor fileDescriptor = out;
            long sendfile = Os.sendfile(fileDescriptor, in, null, Math.min(count2, 524288));
            long t = sendfile;
            if (sendfile == 0) {
                break;
            }
            progress += t;
            checkpoint += t;
            count2 -= t;
            if (checkpoint >= 524288) {
                if (signal != null) {
                    signal.throwIfCanceled();
                }
                if (!(executor2 == null || progressListener == null)) {
                    executor2.execute(new -$$Lambda$FileUtils$QtbHtI8Y1rifwydngi6coGK5l2A(progressListener, progress));
                }
                checkpoint = 0;
            }
        }
        if (!(executor2 == null || progressListener == null)) {
            executor2.execute(new -$$Lambda$FileUtils$XQaJiyjsC2_MFNDbZFQcIhqPnNA(progressListener, progress));
        }
        return progress;
    }

    @VisibleForTesting
    @Deprecated
    public static long copyInternalUserspace(FileDescriptor in, FileDescriptor out, ProgressListener listener, CancellationSignal signal, long count) throws IOException {
        return copyInternalUserspace(in, out, count, signal, -$$Lambda$_14QHG018Z6p13d3hzJuGTWnNeo.INSTANCE, listener);
    }

    @VisibleForTesting
    public static long copyInternalUserspace(FileDescriptor in, FileDescriptor out, long count, CancellationSignal signal, Executor executor, ProgressListener listener) throws IOException {
        if (count != Long.MAX_VALUE) {
            return copyInternalUserspace(new SizedInputStream(new FileInputStream(in), count), new FileOutputStream(out), signal, executor, listener);
        }
        return copyInternalUserspace(new FileInputStream(in), new FileOutputStream(out), signal, executor, listener);
    }

    @VisibleForTesting
    public static long copyInternalUserspace(InputStream in, OutputStream out, CancellationSignal signal, Executor executor, ProgressListener listener) throws IOException {
        long progress = 0;
        long checkpoint = 0;
        byte[] buffer = new byte[8192];
        while (true) {
            int read = in.read(buffer);
            int t = read;
            if (read == -1) {
                break;
            }
            out.write(buffer, 0, t);
            progress += (long) t;
            checkpoint += (long) t;
            if (checkpoint >= 524288) {
                if (signal != null) {
                    signal.throwIfCanceled();
                }
                if (!(executor == null || listener == null)) {
                    executor.execute(new -$$Lambda$FileUtils$TJeD9NeX5giO-5vlBrurGI-g4IY(listener, progress));
                }
                checkpoint = 0;
            }
        }
        if (!(executor == null || listener == null)) {
            executor.execute(new -$$Lambda$FileUtils$0SBPRWOXcbR9EMG_p-55sUuxJ_0(listener, progress));
        }
        return progress;
    }

    @UnsupportedAppUsage
    public static boolean isFilenameSafe(File file) {
        return NoImagePreloadHolder.SAFE_FILENAME_PATTERN.matcher(file.getPath()).matches();
    }

    @UnsupportedAppUsage
    public static String readTextFile(File file, int max, String ellipsis) throws IOException {
        InputStream input = new FileInputStream(file);
        BufferedInputStream bis = new BufferedInputStream(input);
        try {
            long size = file.length();
            String str = "";
            byte[] data;
            int length;
            StringBuilder stringBuilder;
            if (max > 0 || (size > 0 && max == 0)) {
                if (size > 0 && (max == 0 || size < ((long) max))) {
                    max = (int) size;
                }
                data = new byte[(max + 1)];
                length = bis.read(data);
                if (length <= 0) {
                    bis.close();
                    input.close();
                    return str;
                } else if (length <= max) {
                    str = new String(data, 0, length);
                    bis.close();
                    input.close();
                    return str;
                } else if (ellipsis == null) {
                    str = new String(data, 0, max);
                    bis.close();
                    input.close();
                    return str;
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(new String(data, 0, max));
                    stringBuilder.append(ellipsis);
                    str = stringBuilder.toString();
                    bis.close();
                    input.close();
                    return str;
                }
            } else if (max < 0) {
                int len;
                boolean rolled = false;
                byte[] last = null;
                byte[] data2 = null;
                while (true) {
                    if (last != null) {
                        rolled = true;
                    }
                    byte[] tmp = last;
                    last = data2;
                    data2 = tmp;
                    if (data2 == null) {
                        data2 = new byte[(-max)];
                    }
                    len = bis.read(data2);
                    if (len != data2.length) {
                        break;
                    }
                }
                if (last == null && len <= 0) {
                    return str;
                }
                if (last == null) {
                    str = new String(data2, 0, len);
                    bis.close();
                    input.close();
                    return str;
                }
                if (len > 0) {
                    rolled = true;
                    System.arraycopy(last, len, last, 0, last.length - len);
                    str = last.length - len;
                    System.arraycopy(data2, 0, last, str, len);
                }
                if (ellipsis != null) {
                    if (rolled) {
                        stringBuilder = new StringBuilder();
                        stringBuilder.append(ellipsis);
                        stringBuilder.append(new String(last));
                        str = stringBuilder.toString();
                        bis.close();
                        input.close();
                        return str;
                    }
                }
                str = new String(last);
                bis.close();
                input.close();
                return str;
            } else {
                ByteArrayOutputStream contents = new ByteArrayOutputStream();
                data = new byte[1024];
                while (true) {
                    length = bis.read(data);
                    if (length > 0) {
                        contents.write(data, 0, length);
                    }
                    if (length != data.length) {
                        String byteArrayOutputStream = contents.toString();
                        bis.close();
                        input.close();
                        return byteArrayOutputStream;
                    }
                }
            }
        } finally {
            bis.close();
            input.close();
        }
    }

    @UnsupportedAppUsage
    public static void stringToFile(File file, String string) throws IOException {
        stringToFile(file.getAbsolutePath(), string);
    }

    /* JADX WARNING: Missing block: B:14:?, code skipped:
            $closeResource(r1, r2);
     */
    /* JADX WARNING: Missing block: B:26:0x0038, code skipped:
            $closeResource(r1, r0);
     */
    public static void bytesToFile(java.lang.String r4, byte[] r5) throws java.io.IOException {
        /*
        r0 = "/proc/";
        r0 = r4.startsWith(r0);
        r1 = 0;
        if (r0 == 0) goto L_0x0029;
    L_0x0009:
        r0 = android.os.StrictMode.allowThreadDiskWritesMask();
        r2 = new java.io.FileOutputStream;	 Catch:{ all -> 0x0024 }
        r2.<init>(r4);	 Catch:{ all -> 0x0024 }
        r2.write(r5);	 Catch:{ all -> 0x001d }
        $closeResource(r1, r2);	 Catch:{ all -> 0x0024 }
        android.os.StrictMode.setThreadPolicyMask(r0);
        goto L_0x0034;
    L_0x001d:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x001f }
    L_0x001f:
        r3 = move-exception;
        $closeResource(r1, r2);	 Catch:{ all -> 0x0024 }
        throw r3;	 Catch:{ all -> 0x0024 }
    L_0x0024:
        r1 = move-exception;
        android.os.StrictMode.setThreadPolicyMask(r0);
        throw r1;
    L_0x0029:
        r0 = new java.io.FileOutputStream;
        r0.<init>(r4);
        r0.write(r5);	 Catch:{ all -> 0x0035 }
        $closeResource(r1, r0);
    L_0x0034:
        return;
    L_0x0035:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0037 }
    L_0x0037:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.FileUtils.bytesToFile(java.lang.String, byte[]):void");
    }

    @UnsupportedAppUsage
    public static void stringToFile(String filename, String string) throws IOException {
        bytesToFile(filename, string.getBytes(StandardCharsets.UTF_8));
    }

    @Deprecated
    @UnsupportedAppUsage
    public static long checksumCrc32(File file) throws FileNotFoundException, IOException {
        CRC32 checkSummer = new CRC32();
        CheckedInputStream cis = null;
        try {
            cis = new CheckedInputStream(new FileInputStream(file), checkSummer);
            while (cis.read(new byte[128]) >= 0) {
            }
            long value = checkSummer.getValue();
            try {
                cis.close();
            } catch (IOException e) {
            }
            return value;
        } catch (Throwable th) {
            if (cis != null) {
                try {
                    cis.close();
                } catch (IOException e2) {
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:9:0x0011, code skipped:
            $closeResource(r1, r0);
     */
    public static byte[] digest(java.io.File r3, java.lang.String r4) throws java.io.IOException, java.security.NoSuchAlgorithmException {
        /*
        r0 = new java.io.FileInputStream;
        r0.<init>(r3);
        r1 = digest(r0, r4);	 Catch:{ all -> 0x000e }
        r2 = 0;
        $closeResource(r2, r0);
        return r1;
    L_0x000e:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0010 }
    L_0x0010:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.FileUtils.digest(java.io.File, java.lang.String):byte[]");
    }

    public static byte[] digest(InputStream in, String algorithm) throws IOException, NoSuchAlgorithmException {
        return digestInternalUserspace(in, algorithm);
    }

    public static byte[] digest(FileDescriptor fd, String algorithm) throws IOException, NoSuchAlgorithmException {
        return digestInternalUserspace(new FileInputStream(fd), algorithm);
    }

    /* JADX WARNING: Missing block: B:13:0x0021, code skipped:
            $closeResource(r2, r1);
     */
    private static byte[] digestInternalUserspace(java.io.InputStream r5, java.lang.String r6) throws java.io.IOException, java.security.NoSuchAlgorithmException {
        /*
        r0 = java.security.MessageDigest.getInstance(r6);
        r1 = new java.security.DigestInputStream;
        r1.<init>(r5, r0);
        r2 = 8192; // 0x2000 float:1.14794E-41 double:4.0474E-320;
        r2 = new byte[r2];	 Catch:{ all -> 0x001e }
    L_0x000d:
        r3 = r1.read(r2);	 Catch:{ all -> 0x001e }
        r4 = -1;
        if (r3 == r4) goto L_0x0015;
    L_0x0014:
        goto L_0x000d;
    L_0x0015:
        r2 = 0;
        $closeResource(r2, r1);
        r1 = r0.digest();
        return r1;
    L_0x001e:
        r2 = move-exception;
        throw r2;	 Catch:{ all -> 0x0020 }
    L_0x0020:
        r3 = move-exception;
        $closeResource(r2, r1);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.FileUtils.digestInternalUserspace(java.io.InputStream, java.lang.String):byte[]");
    }

    @UnsupportedAppUsage
    public static boolean deleteOlderFiles(File dir, int minCount, long minAgeMs) {
        if (minCount < 0 || minAgeMs < 0) {
            throw new IllegalArgumentException("Constraints must be positive or 0");
        }
        File[] files = dir.listFiles();
        if (files == null) {
            return false;
        }
        Arrays.sort(files, new Comparator<File>() {
            public int compare(File lhs, File rhs) {
                return Long.compare(rhs.lastModified(), lhs.lastModified());
            }
        });
        boolean deleted = false;
        for (int i = minCount; i < files.length; i++) {
            File file = files[i];
            if (System.currentTimeMillis() - file.lastModified() > minAgeMs && file.delete()) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Deleted old file ");
                stringBuilder.append(file);
                Log.d(TAG, stringBuilder.toString());
                deleted = true;
            }
        }
        return deleted;
    }

    public static boolean contains(File[] dirs, File file) {
        for (File dir : dirs) {
            if (contains(dir, file)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(Collection<File> dirs, File file) {
        for (File dir : dirs) {
            if (contains(dir, file)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(File dir, File file) {
        if (dir == null || file == null) {
            return false;
        }
        return contains(dir.getAbsolutePath(), file.getAbsolutePath());
    }

    public static boolean contains(String dirPath, String filePath) {
        if (dirPath.equals(filePath)) {
            return true;
        }
        String str = "/";
        if (!dirPath.endsWith(str)) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(dirPath);
            stringBuilder.append(str);
            dirPath = stringBuilder.toString();
        }
        return filePath.startsWith(dirPath);
    }

    public static boolean deleteContentsAndDir(File dir) {
        if (deleteContents(dir)) {
            return dir.delete();
        }
        return false;
    }

    @UnsupportedAppUsage
    public static boolean deleteContents(File dir) {
        File[] files = dir.listFiles();
        boolean success = true;
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    success &= deleteContents(file);
                }
                if (!file.delete()) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Failed to delete ");
                    stringBuilder.append(file);
                    Log.w(TAG, stringBuilder.toString());
                    success = false;
                }
            }
        }
        return success;
    }

    private static boolean isValidExtFilenameChar(char c) {
        if (c == 0 || c == '/') {
            return false;
        }
        return true;
    }

    public static boolean isValidExtFilename(String name) {
        return name != null && name.equals(buildValidExtFilename(name));
    }

    public static String buildValidExtFilename(String name) {
        if (TextUtils.isEmpty(name) || ".".equals(name) || "..".equals(name)) {
            return "(invalid)";
        }
        StringBuilder res = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (isValidExtFilenameChar(c)) {
                res.append(c);
            } else {
                res.append('_');
            }
        }
        trimFilename(res, 255);
        return res.toString();
    }

    private static boolean isValidFatFilenameChar(char c) {
        if ((c >= 0 && c <= 31) || c == '\"' || c == '*' || c == '/' || c == ':' || c == '<' || c == '\\' || c == '|' || c == 127 || c == '>' || c == '?') {
            return false;
        }
        return true;
    }

    public static boolean isValidFatFilename(String name) {
        return name != null && name.equals(buildValidFatFilename(name));
    }

    public static String buildValidFatFilename(String name) {
        if (TextUtils.isEmpty(name) || ".".equals(name) || "..".equals(name)) {
            return "(invalid)";
        }
        StringBuilder res = new StringBuilder(name.length());
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (isValidFatFilenameChar(c)) {
                res.append(c);
            } else {
                res.append('_');
            }
        }
        trimFilename(res, 255);
        return res.toString();
    }

    @VisibleForTesting
    public static String trimFilename(String str, int maxBytes) {
        StringBuilder res = new StringBuilder(str);
        trimFilename(res, maxBytes);
        return res.toString();
    }

    private static void trimFilename(StringBuilder res, int maxBytes) {
        byte[] raw = res.toString().getBytes(StandardCharsets.UTF_8);
        if (raw.length > maxBytes) {
            maxBytes -= 3;
            while (raw.length > maxBytes) {
                res.deleteCharAt(res.length() / 2);
                raw = res.toString().getBytes(StandardCharsets.UTF_8);
            }
            res.insert(res.length() / 2, Session.TRUNCATE_STRING);
        }
    }

    public static String rewriteAfterRename(File beforeDir, File afterDir, String path) {
        String str = null;
        if (path == null) {
            return null;
        }
        File result = rewriteAfterRename(beforeDir, afterDir, new File(path));
        if (result != null) {
            str = result.getAbsolutePath();
        }
        return str;
    }

    public static String[] rewriteAfterRename(File beforeDir, File afterDir, String[] paths) {
        if (paths == null) {
            return null;
        }
        String[] result = new String[paths.length];
        for (int i = 0; i < paths.length; i++) {
            result[i] = rewriteAfterRename(beforeDir, afterDir, paths[i]);
        }
        return result;
    }

    /* JADX WARNING: Missing block: B:9:0x0025, code skipped:
            return null;
     */
    public static java.io.File rewriteAfterRename(java.io.File r2, java.io.File r3, java.io.File r4) {
        /*
        r0 = 0;
        if (r4 == 0) goto L_0x0025;
    L_0x0003:
        if (r2 == 0) goto L_0x0025;
    L_0x0005:
        if (r3 != 0) goto L_0x0008;
    L_0x0007:
        goto L_0x0025;
    L_0x0008:
        r1 = contains(r2, r4);
        if (r1 == 0) goto L_0x0024;
    L_0x000e:
        r0 = r4.getAbsolutePath();
        r1 = r2.getAbsolutePath();
        r1 = r1.length();
        r0 = r0.substring(r1);
        r1 = new java.io.File;
        r1.<init>(r3, r0);
        return r1;
    L_0x0024:
        return r0;
    L_0x0025:
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.FileUtils.rewriteAfterRename(java.io.File, java.io.File, java.io.File):java.io.File");
    }

    private static File buildUniqueFileWithExtension(File parent, String name, String ext) throws FileNotFoundException {
        File file = buildFile(parent, name, ext);
        int n = 0;
        while (file.exists()) {
            int n2 = n + 1;
            if (n < 32) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(name);
                stringBuilder.append(" (");
                stringBuilder.append(n2);
                stringBuilder.append(")");
                file = buildFile(parent, stringBuilder.toString(), ext);
                n = n2;
            } else {
                throw new FileNotFoundException("Failed to create unique file");
            }
        }
        return file;
    }

    public static File buildUniqueFile(File parent, String mimeType, String displayName) throws FileNotFoundException {
        String[] parts = splitFileName(mimeType, displayName);
        return buildUniqueFileWithExtension(parent, parts[0], parts[1]);
    }

    public static File buildNonUniqueFile(File parent, String mimeType, String displayName) {
        String[] parts = splitFileName(mimeType, displayName);
        return buildFile(parent, parts[0], parts[1]);
    }

    public static File buildUniqueFile(File parent, String displayName) throws FileNotFoundException {
        String name;
        String ext;
        int lastDot = displayName.lastIndexOf(46);
        if (lastDot >= 0) {
            name = displayName.substring(null, lastDot);
            ext = displayName.substring(lastDot + 1);
        } else {
            name = displayName;
            ext = null;
        }
        return buildUniqueFileWithExtension(parent, name, ext);
    }

    public static String[] splitFileName(String mimeType, String displayName) {
        String ext;
        String name;
        if (Document.MIME_TYPE_DIR.equals(mimeType)) {
            ext = null;
            name = displayName;
        } else {
            String mimeTypeFromExt;
            String extFromMimeType;
            int lastDot = displayName.lastIndexOf(46);
            if (lastDot >= 0) {
                ext = displayName.substring(0, lastDot);
                name = displayName.substring(lastDot + 1);
                mimeTypeFromExt = MimeTypeMap.getSingleton().getMimeTypeFromExtension(name.toLowerCase());
                String str = name;
                name = ext;
                ext = str;
            } else {
                mimeTypeFromExt = null;
                name = displayName;
                ext = null;
            }
            if (mimeTypeFromExt == null) {
                mimeTypeFromExt = ContentResolver.MIME_TYPE_DEFAULT;
            }
            if (ContentResolver.MIME_TYPE_DEFAULT.equals(mimeType)) {
                extFromMimeType = null;
            } else {
                extFromMimeType = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
            }
            if (!(Objects.equals(mimeType, mimeTypeFromExt) || Objects.equals(ext, extFromMimeType))) {
                name = displayName;
                ext = extFromMimeType;
            }
        }
        if (ext == null) {
            ext = "";
        }
        return new String[]{name, ext};
    }

    private static File buildFile(File parent, String name, String ext) {
        if (TextUtils.isEmpty(ext)) {
            return new File(parent, name);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(name);
        stringBuilder.append(".");
        stringBuilder.append(ext);
        return new File(parent, stringBuilder.toString());
    }

    public static String[] listOrEmpty(File dir) {
        if (dir != null) {
            return ArrayUtils.defeatNullable(dir.list());
        }
        return EmptyArray.STRING;
    }

    public static File[] listFilesOrEmpty(File dir) {
        if (dir != null) {
            return ArrayUtils.defeatNullable(dir.listFiles());
        }
        return ArrayUtils.EMPTY_FILE;
    }

    public static File[] listFilesOrEmpty(File dir, FilenameFilter filter) {
        if (dir != null) {
            return ArrayUtils.defeatNullable(dir.listFiles(filter));
        }
        return ArrayUtils.EMPTY_FILE;
    }

    public static File newFileOrNull(String path) {
        return path != null ? new File(path) : null;
    }

    public static File createDir(File baseDir, String name) {
        File dir = new File(baseDir, name);
        return createDir(dir) ? dir : null;
    }

    public static boolean createDir(File dir) {
        if (dir.exists()) {
            return dir.isDirectory();
        }
        return dir.mkdir();
    }

    public static long roundStorageSize(long size) {
        long val = 1;
        long pow = 1;
        while (val * pow < size) {
            val <<= 1;
            if (val > 512) {
                val = 1;
                pow *= 1000;
            }
        }
        return val * pow;
    }

    public static void closeQuietly(AutoCloseable closeable) {
        IoUtils.closeQuietly(closeable);
    }

    public static void closeQuietly(FileDescriptor fd) {
        IoUtils.closeQuietly(fd);
    }

    public static int translateModeStringToPosix(String mode) {
        int i = 0;
        while (true) {
            String str = "Bad mode: ";
            StringBuilder stringBuilder;
            if (i < mode.length()) {
                char charAt = mode.charAt(i);
                if (charAt == DateFormat.AM_PM || charAt == 'r' || charAt == 't' || charAt == 'w') {
                    i++;
                } else {
                    stringBuilder = new StringBuilder();
                    stringBuilder.append(str);
                    stringBuilder.append(mode);
                    throw new IllegalArgumentException(stringBuilder.toString());
                }
            }
            if (mode.startsWith("rw")) {
                i = OsConstants.O_RDWR | OsConstants.O_CREAT;
            } else if (mode.startsWith(BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W)) {
                i = OsConstants.O_WRONLY | OsConstants.O_CREAT;
            } else if (mode.startsWith("r")) {
                i = OsConstants.O_RDONLY;
            } else {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(mode);
                throw new IllegalArgumentException(stringBuilder.toString());
            }
            if (mode.indexOf(116) != -1) {
                i |= OsConstants.O_TRUNC;
            }
            if (mode.indexOf(97) != -1) {
                return i | OsConstants.O_APPEND;
            }
            return i;
        }
    }

    public static String translateModePosixToString(int mode) {
        StringBuilder stringBuilder;
        String res = "";
        if ((OsConstants.O_ACCMODE & mode) == OsConstants.O_RDWR) {
            res = "rw";
        } else if ((OsConstants.O_ACCMODE & mode) == OsConstants.O_WRONLY) {
            res = BiometricConnect.MSG_CB_BUNDLE_DEPTHMAP_W;
        } else if ((OsConstants.O_ACCMODE & mode) == OsConstants.O_RDONLY) {
            res = "r";
        } else {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("Bad mode: ");
            stringBuilder2.append(mode);
            throw new IllegalArgumentException(stringBuilder2.toString());
        }
        if ((OsConstants.O_TRUNC & mode) == OsConstants.O_TRUNC) {
            stringBuilder = new StringBuilder();
            stringBuilder.append(res);
            stringBuilder.append(IncidentManager.URI_PARAM_TIMESTAMP);
            res = stringBuilder.toString();
        }
        if ((OsConstants.O_APPEND & mode) != OsConstants.O_APPEND) {
            return res;
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append(res);
        stringBuilder.append(FullBackup.APK_TREE_TOKEN);
        return stringBuilder.toString();
    }

    public static int translateModePosixToPfd(int mode) {
        int res;
        if ((OsConstants.O_ACCMODE & mode) == OsConstants.O_RDWR) {
            res = 805306368;
        } else if ((OsConstants.O_ACCMODE & mode) == OsConstants.O_WRONLY) {
            res = 536870912;
        } else if ((OsConstants.O_ACCMODE & mode) == OsConstants.O_RDONLY) {
            res = 268435456;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Bad mode: ");
            stringBuilder.append(mode);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        if ((OsConstants.O_CREAT & mode) == OsConstants.O_CREAT) {
            res |= 134217728;
        }
        if ((OsConstants.O_TRUNC & mode) == OsConstants.O_TRUNC) {
            res |= 67108864;
        }
        if ((OsConstants.O_APPEND & mode) == OsConstants.O_APPEND) {
            return res | 33554432;
        }
        return res;
    }

    public static int translateModePfdToPosix(int mode) {
        int res;
        if ((mode & 805306368) == 805306368) {
            res = OsConstants.O_RDWR;
        } else if ((mode & 536870912) == 536870912) {
            res = OsConstants.O_WRONLY;
        } else if ((mode & 268435456) == 268435456) {
            res = OsConstants.O_RDONLY;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Bad mode: ");
            stringBuilder.append(mode);
            throw new IllegalArgumentException(stringBuilder.toString());
        }
        if ((mode & 134217728) == 134217728) {
            res |= OsConstants.O_CREAT;
        }
        if ((mode & 67108864) == 67108864) {
            res |= OsConstants.O_TRUNC;
        }
        if ((mode & 33554432) == 33554432) {
            return res | OsConstants.O_APPEND;
        }
        return res;
    }

    public static int translateModeAccessToPosix(int mode) {
        if (mode == OsConstants.F_OK) {
            return OsConstants.O_RDONLY;
        }
        if (((OsConstants.R_OK | OsConstants.W_OK) & mode) == (OsConstants.R_OK | OsConstants.W_OK)) {
            return OsConstants.O_RDWR;
        }
        if ((OsConstants.R_OK & mode) == OsConstants.R_OK) {
            return OsConstants.O_RDONLY;
        }
        if ((OsConstants.W_OK & mode) == OsConstants.W_OK) {
            return OsConstants.O_WRONLY;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Bad mode: ");
        stringBuilder.append(mode);
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
