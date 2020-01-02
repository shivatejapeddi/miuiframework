package com.android.internal.os;

import android.os.IBinder;
import android.os.IInterface;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.SystemClock;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TransferPipe implements Runnable, Closeable {
    static final boolean DEBUG = false;
    static final long DEFAULT_TIMEOUT = 5000;
    static final String TAG = "TransferPipe";
    String mBufferPrefix;
    boolean mComplete;
    long mEndTime;
    String mFailure;
    final ParcelFileDescriptor[] mFds;
    FileDescriptor mOutFd;
    final Thread mThread;

    interface Caller {
        void go(IInterface iInterface, FileDescriptor fileDescriptor, String str, String[] strArr) throws RemoteException;
    }

    public TransferPipe() throws IOException {
        this(null);
    }

    public TransferPipe(String bufferPrefix) throws IOException {
        this(bufferPrefix, TAG);
    }

    protected TransferPipe(String bufferPrefix, String threadName) throws IOException {
        this.mThread = new Thread(this, threadName);
        this.mFds = ParcelFileDescriptor.createPipe();
        this.mBufferPrefix = bufferPrefix;
    }

    /* Access modifiers changed, original: 0000 */
    public ParcelFileDescriptor getReadFd() {
        return this.mFds[0];
    }

    public ParcelFileDescriptor getWriteFd() {
        return this.mFds[1];
    }

    public void setBufferPrefix(String prefix) {
        this.mBufferPrefix = prefix;
    }

    public static void dumpAsync(IBinder binder, FileDescriptor out, String[] args) throws IOException, RemoteException {
        goDump(binder, out, args);
    }

    /* JADX WARNING: Missing block: B:10:?, code skipped:
            $closeResource(null, r6);
            r6 = r5.toByteArray();
     */
    /* JADX WARNING: Missing block: B:12:?, code skipped:
            $closeResource(null, r5);
     */
    /* JADX WARNING: Missing block: B:22:?, code skipped:
            $closeResource(r3, r6);
     */
    /* JADX WARNING: Missing block: B:26:0x0055, code skipped:
            r6 = th;
     */
    /* JADX WARNING: Missing block: B:28:?, code skipped:
            $closeResource(r3, r5);
     */
    public static byte[] dumpAsync(android.os.IBinder r9, java.lang.String... r10) throws java.io.IOException, android.os.RemoteException {
        /*
        r0 = android.os.ParcelFileDescriptor.createPipe();
        r1 = 0;
        r2 = 1;
        r3 = r0[r2];	 Catch:{ all -> 0x005a }
        r3 = r3.getFileDescriptor();	 Catch:{ all -> 0x005a }
        dumpAsync(r9, r3, r10);	 Catch:{ all -> 0x005a }
        r3 = r0[r2];	 Catch:{ all -> 0x005a }
        r3.close();	 Catch:{ all -> 0x005a }
        r3 = 0;
        r0[r2] = r3;	 Catch:{ all -> 0x005a }
        r4 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
        r4 = new byte[r4];	 Catch:{ all -> 0x005a }
        r5 = new java.io.ByteArrayOutputStream;	 Catch:{ all -> 0x005a }
        r5.<init>();	 Catch:{ all -> 0x005a }
        r6 = new java.io.FileInputStream;	 Catch:{ all -> 0x0053 }
        r7 = r0[r1];	 Catch:{ all -> 0x0053 }
        r7 = r7.getFileDescriptor();	 Catch:{ all -> 0x0053 }
        r6.<init>(r7);	 Catch:{ all -> 0x0053 }
    L_0x002b:
        r7 = r6.read(r4);	 Catch:{ all -> 0x004c }
        r8 = -1;
        if (r7 != r8) goto L_0x0048;
        $closeResource(r3, r6);	 Catch:{ all -> 0x0053 }
        r6 = r5.toByteArray();	 Catch:{ all -> 0x0053 }
        $closeResource(r3, r5);	 Catch:{ all -> 0x005a }
        r1 = r0[r1];
        r1.close();
        r1 = r0[r2];
        libcore.io.IoUtils.closeQuietly(r1);
        return r6;
    L_0x0048:
        r5.write(r4, r1, r7);	 Catch:{ all -> 0x004c }
        goto L_0x002b;
    L_0x004c:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x004e }
    L_0x004e:
        r7 = move-exception;
        $closeResource(r3, r6);	 Catch:{ all -> 0x0053 }
        throw r7;	 Catch:{ all -> 0x0053 }
    L_0x0053:
        r3 = move-exception;
        throw r3;	 Catch:{ all -> 0x0055 }
    L_0x0055:
        r6 = move-exception;
        $closeResource(r3, r5);	 Catch:{ all -> 0x005a }
        throw r6;	 Catch:{ all -> 0x005a }
    L_0x005a:
        r3 = move-exception;
        r1 = r0[r1];
        r1.close();
        r1 = r0[r2];
        libcore.io.IoUtils.closeQuietly(r1);
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.TransferPipe.dumpAsync(android.os.IBinder, java.lang.String[]):byte[]");
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

    static void go(Caller caller, IInterface iface, FileDescriptor out, String prefix, String[] args) throws IOException, RemoteException {
        go(caller, iface, out, prefix, args, 5000);
    }

    /* JADX WARNING: Missing block: B:15:0x0029, code skipped:
            $closeResource(r1, r0);
     */
    static void go(com.android.internal.os.TransferPipe.Caller r3, android.os.IInterface r4, java.io.FileDescriptor r5, java.lang.String r6, java.lang.String[] r7, long r8) throws java.io.IOException, android.os.RemoteException {
        /*
        r0 = r4.asBinder();
        r0 = r0 instanceof android.os.Binder;
        if (r0 == 0) goto L_0x000e;
    L_0x0008:
        r3.go(r4, r5, r6, r7);	 Catch:{ RemoteException -> 0x000c }
        goto L_0x000d;
    L_0x000c:
        r0 = move-exception;
    L_0x000d:
        return;
    L_0x000e:
        r0 = new com.android.internal.os.TransferPipe;
        r0.<init>();
        r1 = 0;
        r2 = r0.getWriteFd();	 Catch:{ all -> 0x0026 }
        r2 = r2.getFileDescriptor();	 Catch:{ all -> 0x0026 }
        r3.go(r4, r2, r6, r7);	 Catch:{ all -> 0x0026 }
        r0.go(r5, r8);	 Catch:{ all -> 0x0026 }
        $closeResource(r1, r0);
        return;
    L_0x0026:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0028 }
    L_0x0028:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.TransferPipe.go(com.android.internal.os.TransferPipe$Caller, android.os.IInterface, java.io.FileDescriptor, java.lang.String, java.lang.String[], long):void");
    }

    static void goDump(IBinder binder, FileDescriptor out, String[] args) throws IOException, RemoteException {
        goDump(binder, out, args, 5000);
    }

    /* JADX WARNING: Missing block: B:15:0x0025, code skipped:
            $closeResource(r1, r0);
     */
    static void goDump(android.os.IBinder r3, java.io.FileDescriptor r4, java.lang.String[] r5, long r6) throws java.io.IOException, android.os.RemoteException {
        /*
        r0 = r3 instanceof android.os.Binder;
        if (r0 == 0) goto L_0x000a;
    L_0x0004:
        r3.dump(r4, r5);	 Catch:{ RemoteException -> 0x0008 }
        goto L_0x0009;
    L_0x0008:
        r0 = move-exception;
    L_0x0009:
        return;
    L_0x000a:
        r0 = new com.android.internal.os.TransferPipe;
        r0.<init>();
        r1 = 0;
        r2 = r0.getWriteFd();	 Catch:{ all -> 0x0022 }
        r2 = r2.getFileDescriptor();	 Catch:{ all -> 0x0022 }
        r3.dumpAsync(r2, r5);	 Catch:{ all -> 0x0022 }
        r0.go(r4, r6);	 Catch:{ all -> 0x0022 }
        $closeResource(r1, r0);
        return;
    L_0x0022:
        r1 = move-exception;
        throw r1;	 Catch:{ all -> 0x0024 }
    L_0x0024:
        r2 = move-exception;
        $closeResource(r1, r0);
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.TransferPipe.goDump(android.os.IBinder, java.io.FileDescriptor, java.lang.String[], long):void");
    }

    public void go(FileDescriptor out) throws IOException {
        go(out, 5000);
    }

    public void go(FileDescriptor out, long timeout) throws IOException {
        try {
            synchronized (this) {
                this.mOutFd = out;
                this.mEndTime = SystemClock.uptimeMillis() + timeout;
                closeFd(1);
                this.mThread.start();
                while (this.mFailure == null && !this.mComplete) {
                    long waitTime = this.mEndTime - SystemClock.uptimeMillis();
                    if (waitTime > 0) {
                        try {
                            wait(waitTime);
                        } catch (InterruptedException e) {
                        }
                    } else {
                        this.mThread.interrupt();
                        throw new IOException("Timeout");
                    }
                }
                if (this.mFailure == null) {
                } else {
                    throw new IOException(this.mFailure);
                }
            }
            kill();
        } catch (Throwable th) {
            kill();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void closeFd(int num) {
        ParcelFileDescriptor[] parcelFileDescriptorArr = this.mFds;
        if (parcelFileDescriptorArr[num] != null) {
            try {
                parcelFileDescriptorArr[num].close();
            } catch (IOException e) {
            }
            this.mFds[num] = null;
        }
    }

    public void close() {
        kill();
    }

    public void kill() {
        synchronized (this) {
            closeFd(0);
            closeFd(1);
        }
    }

    /* Access modifiers changed, original: protected */
    public OutputStream getNewOutputStream() {
        return new FileOutputStream(this.mOutFd);
    }

    /* JADX WARNING: Missing block: B:10:0x0023, code skipped:
            r3 = null;
            r4 = true;
            r5 = r11.mBufferPrefix;
     */
    /* JADX WARNING: Missing block: B:11:0x0027, code skipped:
            if (r5 == null) goto L_0x002d;
     */
    /* JADX WARNING: Missing block: B:12:0x0029, code skipped:
            r3 = r5.getBytes();
     */
    /* JADX WARNING: Missing block: B:14:?, code skipped:
            r5 = r2.read(r0);
            r6 = r5;
     */
    /* JADX WARNING: Missing block: B:15:0x0033, code skipped:
            if (r5 <= 0) goto L_0x0068;
     */
    /* JADX WARNING: Missing block: B:16:0x0035, code skipped:
            if (r3 != null) goto L_0x003c;
     */
    /* JADX WARNING: Missing block: B:17:0x0037, code skipped:
            r1.write(r0, 0, r6);
     */
    /* JADX WARNING: Missing block: B:18:0x003c, code skipped:
            r5 = 0;
            r8 = 0;
     */
    /* JADX WARNING: Missing block: B:19:0x003e, code skipped:
            if (r8 >= r6) goto L_0x0060;
     */
    /* JADX WARNING: Missing block: B:21:0x0044, code skipped:
            if (r0[r8] == (byte) 10) goto L_0x005e;
     */
    /* JADX WARNING: Missing block: B:22:0x0046, code skipped:
            if (r8 <= r5) goto L_0x004d;
     */
    /* JADX WARNING: Missing block: B:23:0x0048, code skipped:
            r1.write(r0, r5, r8 - r5);
     */
    /* JADX WARNING: Missing block: B:24:0x004d, code skipped:
            r5 = r8;
     */
    /* JADX WARNING: Missing block: B:25:0x004e, code skipped:
            if (r4 == false) goto L_0x0054;
     */
    /* JADX WARNING: Missing block: B:26:0x0050, code skipped:
            r1.write(r3);
            r4 = false;
     */
    /* JADX WARNING: Missing block: B:27:0x0054, code skipped:
            r8 = r8 + 1;
     */
    /* JADX WARNING: Missing block: B:28:0x0055, code skipped:
            if (r8 >= r6) goto L_0x005b;
     */
    /* JADX WARNING: Missing block: B:30:0x0059, code skipped:
            if (r0[r8] != (byte) 10) goto L_0x0054;
     */
    /* JADX WARNING: Missing block: B:31:0x005b, code skipped:
            if (r8 >= r6) goto L_0x005e;
     */
    /* JADX WARNING: Missing block: B:32:0x005d, code skipped:
            r4 = true;
     */
    /* JADX WARNING: Missing block: B:33:0x005e, code skipped:
            r8 = r8 + 1;
     */
    /* JADX WARNING: Missing block: B:34:0x0060, code skipped:
            if (r6 <= r5) goto L_0x0067;
     */
    /* JADX WARNING: Missing block: B:35:0x0062, code skipped:
            r1.write(r0, r5, r6 - r5);
     */
    /* JADX WARNING: Missing block: B:37:0x0068, code skipped:
            r11.mThread.isInterrupted();
     */
    /* JADX WARNING: Missing block: B:38:0x006e, code skipped:
            monitor-enter(r11);
     */
    /* JADX WARNING: Missing block: B:40:?, code skipped:
            r11.mComplete = true;
            notifyAll();
     */
    /* JADX WARNING: Missing block: B:41:0x0074, code skipped:
            monitor-exit(r11);
     */
    /* JADX WARNING: Missing block: B:42:0x0075, code skipped:
            return;
     */
    /* JADX WARNING: Missing block: B:46:0x0079, code skipped:
            r5 = move-exception;
     */
    /* JADX WARNING: Missing block: B:47:0x007a, code skipped:
            monitor-enter(r11);
     */
    /* JADX WARNING: Missing block: B:49:?, code skipped:
            r11.mFailure = r5.toString();
            notifyAll();
     */
    /* JADX WARNING: Missing block: B:51:0x0085, code skipped:
            return;
     */
    public void run() {
        /*
        r11 = this;
        r0 = 1024; // 0x400 float:1.435E-42 double:5.06E-321;
        r0 = new byte[r0];
        monitor-enter(r11);
        r1 = r11.getReadFd();	 Catch:{ all -> 0x0089 }
        if (r1 != 0) goto L_0x0014;
    L_0x000b:
        r2 = "TransferPipe";
        r3 = "Pipe has been closed...";
        android.util.Slog.w(r2, r3);	 Catch:{ all -> 0x0089 }
        monitor-exit(r11);	 Catch:{ all -> 0x0089 }
        return;
    L_0x0014:
        r2 = new java.io.FileInputStream;	 Catch:{ all -> 0x0089 }
        r3 = r1.getFileDescriptor();	 Catch:{ all -> 0x0089 }
        r2.<init>(r3);	 Catch:{ all -> 0x0089 }
        r3 = r11.getNewOutputStream();	 Catch:{ all -> 0x0089 }
        r1 = r3;
        monitor-exit(r11);	 Catch:{ all -> 0x0089 }
        r3 = 0;
        r4 = 1;
        r5 = r11.mBufferPrefix;
        if (r5 == 0) goto L_0x002d;
    L_0x0029:
        r3 = r5.getBytes();
    L_0x002d:
        r5 = r2.read(r0);	 Catch:{ IOException -> 0x0079 }
        r6 = r5;
        r7 = 1;
        if (r5 <= 0) goto L_0x0068;
    L_0x0035:
        if (r3 != 0) goto L_0x003c;
    L_0x0037:
        r5 = 0;
        r1.write(r0, r5, r6);	 Catch:{ IOException -> 0x0079 }
        goto L_0x002d;
    L_0x003c:
        r5 = 0;
        r8 = 0;
    L_0x003e:
        if (r8 >= r6) goto L_0x0060;
    L_0x0040:
        r9 = r0[r8];	 Catch:{ IOException -> 0x0079 }
        r10 = 10;
        if (r9 == r10) goto L_0x005e;
    L_0x0046:
        if (r8 <= r5) goto L_0x004d;
    L_0x0048:
        r9 = r8 - r5;
        r1.write(r0, r5, r9);	 Catch:{ IOException -> 0x0079 }
    L_0x004d:
        r5 = r8;
        if (r4 == 0) goto L_0x0054;
    L_0x0050:
        r1.write(r3);	 Catch:{ IOException -> 0x0079 }
        r4 = 0;
    L_0x0054:
        r8 = r8 + r7;
        if (r8 >= r6) goto L_0x005b;
    L_0x0057:
        r9 = r0[r8];	 Catch:{ IOException -> 0x0079 }
        if (r9 != r10) goto L_0x0054;
    L_0x005b:
        if (r8 >= r6) goto L_0x005e;
    L_0x005d:
        r4 = 1;
    L_0x005e:
        r8 = r8 + r7;
        goto L_0x003e;
    L_0x0060:
        if (r6 <= r5) goto L_0x0067;
    L_0x0062:
        r7 = r6 - r5;
        r1.write(r0, r5, r7);	 Catch:{ IOException -> 0x0079 }
    L_0x0067:
        goto L_0x002d;
    L_0x0068:
        r5 = r11.mThread;	 Catch:{ IOException -> 0x0079 }
        r5.isInterrupted();	 Catch:{ IOException -> 0x0079 }
        monitor-enter(r11);
        r11.mComplete = r7;	 Catch:{ all -> 0x0076 }
        r11.notifyAll();	 Catch:{ all -> 0x0076 }
        monitor-exit(r11);	 Catch:{ all -> 0x0076 }
        return;
    L_0x0076:
        r5 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0076 }
        throw r5;
    L_0x0079:
        r5 = move-exception;
        monitor-enter(r11);
        r6 = r5.toString();	 Catch:{ all -> 0x0086 }
        r11.mFailure = r6;	 Catch:{ all -> 0x0086 }
        r11.notifyAll();	 Catch:{ all -> 0x0086 }
        monitor-exit(r11);	 Catch:{ all -> 0x0086 }
        return;
    L_0x0086:
        r6 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0086 }
        throw r6;
    L_0x0089:
        r1 = move-exception;
        monitor-exit(r11);	 Catch:{ all -> 0x0089 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.TransferPipe.run():void");
    }
}
