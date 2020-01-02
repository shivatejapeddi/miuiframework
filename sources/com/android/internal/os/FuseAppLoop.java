package com.android.internal.os;

import android.annotation.UnsupportedAppUsage;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.os.ParcelFileDescriptor;
import android.os.ProxyFileDescriptorCallback;
import android.system.ErrnoException;
import android.system.OsConstants;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.Preconditions;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ThreadFactory;

public class FuseAppLoop implements Callback {
    private static final int ARGS_POOL_SIZE = 50;
    private static final boolean DEBUG = Log.isLoggable(TAG, 3);
    private static final int FUSE_FSYNC = 20;
    private static final int FUSE_GETATTR = 3;
    private static final int FUSE_LOOKUP = 1;
    private static final int FUSE_MAX_WRITE = 131072;
    private static final int FUSE_OK = 0;
    private static final int FUSE_OPEN = 14;
    private static final int FUSE_READ = 15;
    private static final int FUSE_RELEASE = 18;
    private static final int FUSE_WRITE = 16;
    private static final int MIN_INODE = 2;
    public static final int ROOT_INODE = 1;
    private static final String TAG = "FuseAppLoop";
    private static final ThreadFactory sDefaultThreadFactory = new ThreadFactory() {
        public Thread newThread(Runnable r) {
            return new Thread(r, FuseAppLoop.TAG);
        }
    };
    @GuardedBy({"mLock"})
    private final LinkedList<Args> mArgsPool = new LinkedList();
    @GuardedBy({"mLock"})
    private final BytesMap mBytesMap = new BytesMap();
    @GuardedBy({"mLock"})
    private final SparseArray<CallbackEntry> mCallbackMap = new SparseArray();
    @GuardedBy({"mLock"})
    private long mInstance;
    private final Object mLock = new Object();
    private final int mMountPointId;
    @GuardedBy({"mLock"})
    private int mNextInode = 2;
    private final Thread mThread;

    private static class Args {
        byte[] data;
        CallbackEntry entry;
        long inode;
        long offset;
        int size;
        long unique;

        private Args() {
        }

        /* synthetic */ Args(AnonymousClass1 x0) {
            this();
        }
    }

    private static class BytesMap {
        final Map<Long, BytesMapEntry> mEntries;

        private BytesMap() {
            this.mEntries = new HashMap();
        }

        /* synthetic */ BytesMap(AnonymousClass1 x0) {
            this();
        }

        /* Access modifiers changed, original: 0000 */
        public byte[] startUsing(long threadId) {
            BytesMapEntry entry = (BytesMapEntry) this.mEntries.get(Long.valueOf(threadId));
            if (entry == null) {
                entry = new BytesMapEntry();
                this.mEntries.put(Long.valueOf(threadId), entry);
            }
            entry.counter++;
            return entry.bytes;
        }

        /* Access modifiers changed, original: 0000 */
        public void stopUsing(long threadId) {
            BytesMapEntry entry = (BytesMapEntry) this.mEntries.get(Long.valueOf(threadId));
            Preconditions.checkNotNull(entry);
            entry.counter--;
            if (entry.counter <= 0) {
                this.mEntries.remove(Long.valueOf(threadId));
            }
        }

        /* Access modifiers changed, original: 0000 */
        public void clear() {
            this.mEntries.clear();
        }
    }

    private static class BytesMapEntry {
        byte[] bytes;
        int counter;

        private BytesMapEntry() {
            this.counter = 0;
            this.bytes = new byte[131072];
        }

        /* synthetic */ BytesMapEntry(AnonymousClass1 x0) {
            this();
        }
    }

    private static class CallbackEntry {
        final ProxyFileDescriptorCallback callback;
        final Handler handler;
        boolean opened;

        CallbackEntry(ProxyFileDescriptorCallback callback, Handler handler) {
            this.callback = (ProxyFileDescriptorCallback) Preconditions.checkNotNull(callback);
            this.handler = (Handler) Preconditions.checkNotNull(handler);
        }

        /* Access modifiers changed, original: 0000 */
        public long getThreadId() {
            return this.handler.getLooper().getThread().getId();
        }
    }

    public static class UnmountedException extends Exception {
    }

    public native void native_delete(long j);

    public native long native_new(int i);

    public native void native_replyGetAttr(long j, long j2, long j3, long j4);

    public native void native_replyLookup(long j, long j2, long j3, long j4);

    public native void native_replyOpen(long j, long j2, long j3);

    public native void native_replyRead(long j, long j2, int i, byte[] bArr);

    public native void native_replySimple(long j, long j2, int i);

    public native void native_replyWrite(long j, long j2, int i);

    public native void native_start(long j);

    public FuseAppLoop(int mountPointId, ParcelFileDescriptor fd, ThreadFactory factory) {
        this.mMountPointId = mountPointId;
        if (factory == null) {
            factory = sDefaultThreadFactory;
        }
        this.mInstance = native_new(fd.detachFd());
        this.mThread = factory.newThread(new -$$Lambda$FuseAppLoop$e9Yru2f_btesWlxIgerkPnHibpg(this));
        this.mThread.start();
    }

    public /* synthetic */ void lambda$new$0$FuseAppLoop() {
        native_start(this.mInstance);
        synchronized (this.mLock) {
            native_delete(this.mInstance);
            this.mInstance = 0;
            this.mBytesMap.clear();
        }
    }

    public int registerCallback(ProxyFileDescriptorCallback callback, Handler handler) throws FuseUnavailableMountException {
        int id;
        synchronized (this.mLock) {
            Preconditions.checkNotNull(callback);
            Preconditions.checkNotNull(handler);
            boolean z = false;
            Preconditions.checkState(this.mCallbackMap.size() < 2147483645, "Too many opened files.");
            if (Thread.currentThread().getId() != handler.getLooper().getThread().getId()) {
                z = true;
            }
            Preconditions.checkArgument(z, "Handler must be different from the current thread");
            if (this.mInstance != 0) {
                do {
                    id = this.mNextInode;
                    this.mNextInode++;
                    if (this.mNextInode < 0) {
                        this.mNextInode = 2;
                    }
                } while (this.mCallbackMap.get(id) != null);
                this.mCallbackMap.put(id, new CallbackEntry(callback, new Handler(handler.getLooper(), (Callback) this)));
            } else {
                throw new FuseUnavailableMountException(this.mMountPointId);
            }
        }
        return id;
    }

    public void unregisterCallback(int id) {
        synchronized (this.mLock) {
            this.mCallbackMap.remove(id);
        }
    }

    public int getMountPointId() {
        return this.mMountPointId;
    }

    /* JADX WARNING: Removed duplicated region for block: B:154:0x01f6 A:{SYNTHETIC, Splitter:B:154:0x01f6} */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x01f6 A:{SYNTHETIC, Splitter:B:154:0x01f6} */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x01f6 A:{SYNTHETIC, Splitter:B:154:0x01f6} */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x01f6 A:{SYNTHETIC, Splitter:B:154:0x01f6} */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x01f6 A:{SYNTHETIC, Splitter:B:154:0x01f6} */
    /* JADX WARNING: Removed duplicated region for block: B:154:0x01f6 A:{SYNTHETIC, Splitter:B:154:0x01f6} */
    public boolean handleMessage(android.os.Message r26) {
        /*
        r25 = this;
        r10 = r25;
        r11 = r26;
        r0 = r11.obj;
        r12 = r0;
        r12 = (com.android.internal.os.FuseAppLoop.Args) r12;
        r13 = r12.entry;
        r14 = r12.inode;
        r8 = r12.unique;
        r7 = r12.size;
        r4 = r12.offset;
        r2 = r12.data;
        r3 = 1;
        r0 = r11.what;	 Catch:{ Exception -> 0x01e6 }
        r16 = 0;
        if (r0 == r3) goto L_0x01ab;
    L_0x001c:
        r1 = 3;
        if (r0 == r1) goto L_0x0168;
    L_0x001f:
        r1 = 18;
        if (r0 == r1) goto L_0x012f;
    L_0x0023:
        r1 = 20;
        if (r0 == r1) goto L_0x0104;
    L_0x0027:
        r1 = 15;
        if (r0 == r1) goto L_0x00b1;
    L_0x002b:
        r1 = 16;
        if (r0 != r1) goto L_0x0089;
    L_0x002f:
        r0 = r13.callback;	 Catch:{ Exception -> 0x007b }
        r6 = r0.onWrite(r4, r7, r2);	 Catch:{ Exception -> 0x007b }
        r1 = r10.mLock;	 Catch:{ Exception -> 0x007b }
        monitor-enter(r1);	 Catch:{ Exception -> 0x007b }
        r18 = r4;
        r3 = r10.mInstance;	 Catch:{ all -> 0x006b }
        r0 = (r3 > r16 ? 1 : (r3 == r16 ? 0 : -1));
        if (r0 == 0) goto L_0x0056;
    L_0x0040:
        r3 = r10.mInstance;	 Catch:{ all -> 0x006b }
        r16 = r1;
        r1 = r25;
        r5 = r2;
        r20 = 1;
        r2 = r3;
        r21 = r14;
        r14 = r18;
        r18 = r7;
        r7 = r5;
        r4 = r8;
        r1.native_replyWrite(r2, r4, r6);	 Catch:{ all -> 0x0079 }
        goto L_0x0061;
    L_0x0056:
        r16 = r1;
        r21 = r14;
        r14 = r18;
        r20 = 1;
        r18 = r7;
        r7 = r2;
    L_0x0061:
        r10.recycleLocked(r12);	 Catch:{ all -> 0x0079 }
        monitor-exit(r16);	 Catch:{ all -> 0x0079 }
        r19 = r7;
        r23 = r8;
        goto L_0x01da;
    L_0x006b:
        r0 = move-exception;
        r16 = r1;
        r21 = r14;
        r14 = r18;
        r20 = 1;
        r18 = r7;
        r7 = r2;
    L_0x0077:
        monitor-exit(r16);	 Catch:{ all -> 0x0079 }
        throw r0;	 Catch:{ Exception -> 0x00aa }
    L_0x0079:
        r0 = move-exception;
        goto L_0x0077;
    L_0x007b:
        r0 = move-exception;
        r20 = r3;
        r18 = r7;
        r21 = r14;
        r14 = r4;
        r19 = r2;
        r23 = r8;
        goto L_0x01f2;
    L_0x0089:
        r20 = r3;
        r18 = r7;
        r21 = r14;
        r7 = r2;
        r14 = r4;
        r0 = new java.lang.IllegalArgumentException;	 Catch:{ Exception -> 0x00aa }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00aa }
        r1.<init>();	 Catch:{ Exception -> 0x00aa }
        r2 = "Unknown FUSE command: ";
        r1.append(r2);	 Catch:{ Exception -> 0x00aa }
        r2 = r11.what;	 Catch:{ Exception -> 0x00aa }
        r1.append(r2);	 Catch:{ Exception -> 0x00aa }
        r1 = r1.toString();	 Catch:{ Exception -> 0x00aa }
        r0.<init>(r1);	 Catch:{ Exception -> 0x00aa }
        throw r0;	 Catch:{ Exception -> 0x00aa }
    L_0x00aa:
        r0 = move-exception;
        r19 = r7;
        r23 = r8;
        goto L_0x01f2;
    L_0x00b1:
        r20 = r3;
        r18 = r7;
        r21 = r14;
        r7 = r2;
        r14 = r4;
        r0 = r13.callback;	 Catch:{ Exception -> 0x00fd }
        r4 = r18;
        r6 = r0.onRead(r14, r4, r7);	 Catch:{ Exception -> 0x00f4 }
        r5 = r10.mLock;	 Catch:{ Exception -> 0x00f4 }
        monitor-enter(r5);	 Catch:{ Exception -> 0x00f4 }
        r0 = r10.mInstance;	 Catch:{ all -> 0x00e9 }
        r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1));
        if (r0 == 0) goto L_0x00db;
    L_0x00ca:
        r2 = r10.mInstance;	 Catch:{ all -> 0x00e9 }
        r1 = r25;
        r16 = r4;
        r17 = r5;
        r4 = r8;
        r19 = r7;
        r18 = r16;
        r1.native_replyRead(r2, r4, r6, r7);	 Catch:{ all -> 0x00f2 }
        goto L_0x00e1;
    L_0x00db:
        r18 = r4;
        r17 = r5;
        r19 = r7;
    L_0x00e1:
        r10.recycleLocked(r12);	 Catch:{ all -> 0x00f2 }
        monitor-exit(r17);	 Catch:{ all -> 0x00f2 }
        r23 = r8;
        goto L_0x01da;
    L_0x00e9:
        r0 = move-exception;
        r18 = r4;
        r17 = r5;
        r19 = r7;
    L_0x00f0:
        monitor-exit(r17);	 Catch:{ all -> 0x00f2 }
        throw r0;	 Catch:{ Exception -> 0x0163 }
    L_0x00f2:
        r0 = move-exception;
        goto L_0x00f0;
    L_0x00f4:
        r0 = move-exception;
        r18 = r4;
        r19 = r7;
        r23 = r8;
        goto L_0x01f2;
    L_0x00fd:
        r0 = move-exception;
        r19 = r7;
        r23 = r8;
        goto L_0x01f2;
    L_0x0104:
        r19 = r2;
        r20 = r3;
        r18 = r7;
        r21 = r14;
        r14 = r4;
        r0 = r13.callback;	 Catch:{ Exception -> 0x0163 }
        r0.onFsync();	 Catch:{ Exception -> 0x0163 }
        r7 = r10.mLock;	 Catch:{ Exception -> 0x0163 }
        monitor-enter(r7);	 Catch:{ Exception -> 0x0163 }
        r0 = r10.mInstance;	 Catch:{ all -> 0x012c }
        r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1));
        if (r0 == 0) goto L_0x0124;
    L_0x011b:
        r2 = r10.mInstance;	 Catch:{ all -> 0x012c }
        r6 = 0;
        r1 = r25;
        r4 = r8;
        r1.native_replySimple(r2, r4, r6);	 Catch:{ all -> 0x012c }
    L_0x0124:
        r10.recycleLocked(r12);	 Catch:{ all -> 0x012c }
        monitor-exit(r7);	 Catch:{ all -> 0x012c }
        r23 = r8;
        goto L_0x01da;
    L_0x012c:
        r0 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x012c }
        throw r0;	 Catch:{ Exception -> 0x0163 }
    L_0x012f:
        r19 = r2;
        r20 = r3;
        r18 = r7;
        r21 = r14;
        r14 = r4;
        r0 = r13.callback;	 Catch:{ Exception -> 0x0163 }
        r0.onRelease();	 Catch:{ Exception -> 0x0163 }
        r7 = r10.mLock;	 Catch:{ Exception -> 0x0163 }
        monitor-enter(r7);	 Catch:{ Exception -> 0x0163 }
        r0 = r10.mInstance;	 Catch:{ all -> 0x0160 }
        r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1));
        if (r0 == 0) goto L_0x014f;
    L_0x0146:
        r2 = r10.mInstance;	 Catch:{ all -> 0x0160 }
        r6 = 0;
        r1 = r25;
        r4 = r8;
        r1.native_replySimple(r2, r4, r6);	 Catch:{ all -> 0x0160 }
    L_0x014f:
        r0 = r10.mBytesMap;	 Catch:{ all -> 0x0160 }
        r1 = r13.getThreadId();	 Catch:{ all -> 0x0160 }
        r0.stopUsing(r1);	 Catch:{ all -> 0x0160 }
        r10.recycleLocked(r12);	 Catch:{ all -> 0x0160 }
        monitor-exit(r7);	 Catch:{ all -> 0x0160 }
        r23 = r8;
        goto L_0x01da;
    L_0x0160:
        r0 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x0160 }
        throw r0;	 Catch:{ Exception -> 0x0163 }
    L_0x0163:
        r0 = move-exception;
        r23 = r8;
        goto L_0x01f2;
    L_0x0168:
        r19 = r2;
        r20 = r3;
        r18 = r7;
        r21 = r14;
        r14 = r4;
        r0 = r13.callback;	 Catch:{ Exception -> 0x01a7 }
        r0 = r0.onGetSize();	 Catch:{ Exception -> 0x01a7 }
        r6 = r8;
        r8 = r0;
        r4 = r10.mLock;	 Catch:{ Exception -> 0x01a3 }
        monitor-enter(r4);	 Catch:{ Exception -> 0x01a3 }
        r0 = r10.mInstance;	 Catch:{ all -> 0x019a }
        r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1));
        if (r0 == 0) goto L_0x0191;
    L_0x0182:
        r2 = r10.mInstance;	 Catch:{ all -> 0x019a }
        r1 = r25;
        r16 = r4;
        r4 = r6;
        r23 = r6;
        r6 = r21;
        r1.native_replyGetAttr(r2, r4, r6, r8);	 Catch:{ all -> 0x01a1 }
        goto L_0x0195;
    L_0x0191:
        r16 = r4;
        r23 = r6;
    L_0x0195:
        r10.recycleLocked(r12);	 Catch:{ all -> 0x01a1 }
        monitor-exit(r16);	 Catch:{ all -> 0x01a1 }
        goto L_0x01da;
    L_0x019a:
        r0 = move-exception;
        r16 = r4;
        r23 = r6;
    L_0x019f:
        monitor-exit(r16);	 Catch:{ all -> 0x01a1 }
        throw r0;	 Catch:{ Exception -> 0x01e4 }
    L_0x01a1:
        r0 = move-exception;
        goto L_0x019f;
    L_0x01a3:
        r0 = move-exception;
        r23 = r6;
        goto L_0x01f2;
    L_0x01a7:
        r0 = move-exception;
        r23 = r8;
        goto L_0x01f2;
    L_0x01ab:
        r19 = r2;
        r20 = r3;
        r18 = r7;
        r23 = r8;
        r21 = r14;
        r14 = r4;
        r0 = r13.callback;	 Catch:{ Exception -> 0x01e4 }
        r8 = r0.onGetSize();	 Catch:{ Exception -> 0x01e4 }
        r6 = r10.mLock;	 Catch:{ Exception -> 0x01e4 }
        monitor-enter(r6);	 Catch:{ Exception -> 0x01e4 }
        r0 = r10.mInstance;	 Catch:{ all -> 0x01dd }
        r0 = (r0 > r16 ? 1 : (r0 == r16 ? 0 : -1));
        if (r0 == 0) goto L_0x01d3;
    L_0x01c5:
        r2 = r10.mInstance;	 Catch:{ all -> 0x01dd }
        r1 = r25;
        r4 = r23;
        r16 = r6;
        r6 = r21;
        r1.native_replyLookup(r2, r4, r6, r8);	 Catch:{ all -> 0x01e2 }
        goto L_0x01d5;
    L_0x01d3:
        r16 = r6;
    L_0x01d5:
        r10.recycleLocked(r12);	 Catch:{ all -> 0x01e2 }
        monitor-exit(r16);	 Catch:{ all -> 0x01e2 }
    L_0x01da:
        r3 = r23;
        goto L_0x020a;
    L_0x01dd:
        r0 = move-exception;
        r16 = r6;
    L_0x01e0:
        monitor-exit(r16);	 Catch:{ all -> 0x01e2 }
        throw r0;	 Catch:{ Exception -> 0x01e4 }
    L_0x01e2:
        r0 = move-exception;
        goto L_0x01e0;
    L_0x01e4:
        r0 = move-exception;
        goto L_0x01f2;
    L_0x01e6:
        r0 = move-exception;
        r19 = r2;
        r20 = r3;
        r18 = r7;
        r23 = r8;
        r21 = r14;
        r14 = r4;
    L_0x01f2:
        r1 = r0;
        r2 = r10.mLock;
        monitor-enter(r2);
        r0 = "FuseAppLoop";
        r3 = "";
        android.util.Log.e(r0, r3, r1);	 Catch:{ all -> 0x020b }
        r0 = getError(r1);	 Catch:{ all -> 0x020b }
        r3 = r23;
        r10.replySimpleLocked(r3, r0);	 Catch:{ all -> 0x0210 }
        r10.recycleLocked(r12);	 Catch:{ all -> 0x0210 }
        monitor-exit(r2);	 Catch:{ all -> 0x0210 }
    L_0x020a:
        return r20;
    L_0x020b:
        r0 = move-exception;
        r3 = r23;
    L_0x020e:
        monitor-exit(r2);	 Catch:{ all -> 0x0210 }
        throw r0;
    L_0x0210:
        r0 = move-exception;
        goto L_0x020e;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.FuseAppLoop.handleMessage(android.os.Message):boolean");
    }

    @UnsupportedAppUsage
    private void onCommand(int command, long unique, long inode, long offset, int size, byte[] data) {
        synchronized (this.mLock) {
            try {
                Args args;
                if (this.mArgsPool.size() == 0) {
                    args = new Args();
                } else {
                    args = (Args) this.mArgsPool.pop();
                }
                args.unique = unique;
                args.inode = inode;
                args.offset = offset;
                args.size = size;
                args.data = data;
                args.entry = getCallbackEntryOrThrowLocked(inode);
                if (args.entry.handler.sendMessage(Message.obtain(args.entry.handler, command, 0, 0, args))) {
                } else {
                    throw new ErrnoException("onCommand", OsConstants.EBADF);
                }
            } catch (Exception error) {
                replySimpleLocked(unique, getError(error));
            }
        }
    }

    @UnsupportedAppUsage
    private byte[] onOpen(long unique, long inode) {
        synchronized (this.mLock) {
            try {
                CallbackEntry entry = getCallbackEntryOrThrowLocked(inode);
                if (entry.opened) {
                    throw new ErrnoException("onOpen", OsConstants.EMFILE);
                }
                if (this.mInstance != 0) {
                    native_replyOpen(this.mInstance, unique, inode);
                    entry.opened = true;
                    byte[] startUsing = this.mBytesMap.startUsing(entry.getThreadId());
                    return startUsing;
                }
                return null;
            } catch (ErrnoException error) {
                replySimpleLocked(unique, getError(error));
            }
        }
    }

    private static int getError(Exception error) {
        if (error instanceof ErrnoException) {
            int errno = ((ErrnoException) error).errno;
            if (errno != OsConstants.ENOSYS) {
                return -errno;
            }
        }
        return -OsConstants.EBADF;
    }

    @GuardedBy({"mLock"})
    private CallbackEntry getCallbackEntryOrThrowLocked(long inode) throws ErrnoException {
        CallbackEntry entry = (CallbackEntry) this.mCallbackMap.get(checkInode(inode));
        if (entry != null) {
            return entry;
        }
        throw new ErrnoException("getCallbackEntryOrThrowLocked", OsConstants.ENOENT);
    }

    @GuardedBy({"mLock"})
    private void recycleLocked(Args args) {
        if (this.mArgsPool.size() < 50) {
            this.mArgsPool.add(args);
        }
    }

    @GuardedBy({"mLock"})
    private void replySimpleLocked(long unique, int result) {
        long j = this.mInstance;
        if (j != 0) {
            native_replySimple(j, unique, result);
        }
    }

    private static int checkInode(long inode) {
        Preconditions.checkArgumentInRange(inode, 2, 2147483647L, "checkInode");
        return (int) inode;
    }
}
