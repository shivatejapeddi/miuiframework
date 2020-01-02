package android.os;

import android.annotation.UnsupportedAppUsage;
import android.util.Printer;
import android.util.SparseArray;
import android.util.proto.ProtoOutputStream;
import java.io.FileDescriptor;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public final class MessageQueue {
    private static final boolean DEBUG = false;
    private static final String TAG = "MessageQueue";
    private boolean mBlocked;
    private SparseArray<FileDescriptorRecord> mFileDescriptorRecords;
    @UnsupportedAppUsage
    private final ArrayList<IdleHandler> mIdleHandlers = new ArrayList();
    @UnsupportedAppUsage
    Message mMessages;
    @UnsupportedAppUsage
    private int mNextBarrierToken;
    private IdleHandler[] mPendingIdleHandlers;
    @UnsupportedAppUsage
    private long mPtr;
    @UnsupportedAppUsage
    private final boolean mQuitAllowed;
    private boolean mQuitting;

    public interface OnFileDescriptorEventListener {
        public static final int EVENT_ERROR = 4;
        public static final int EVENT_INPUT = 1;
        public static final int EVENT_OUTPUT = 2;

        @Retention(RetentionPolicy.SOURCE)
        public @interface Events {
        }

        int onFileDescriptorEvents(FileDescriptor fileDescriptor, int i);
    }

    private static final class FileDescriptorRecord {
        public final FileDescriptor mDescriptor;
        public int mEvents;
        public OnFileDescriptorEventListener mListener;
        public int mSeq;

        public FileDescriptorRecord(FileDescriptor descriptor, int events, OnFileDescriptorEventListener listener) {
            this.mDescriptor = descriptor;
            this.mEvents = events;
            this.mListener = listener;
        }
    }

    public interface IdleHandler {
        boolean queueIdle();
    }

    private static native void nativeDestroy(long j);

    private static native long nativeInit();

    private static native boolean nativeIsPolling(long j);

    @UnsupportedAppUsage
    private native void nativePollOnce(long j, int i);

    private static native void nativeSetFileDescriptorEvents(long j, int i, int i2);

    private static native void nativeWake(long j);

    MessageQueue(boolean quitAllowed) {
        this.mQuitAllowed = quitAllowed;
        this.mPtr = nativeInit();
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        try {
            dispose();
        } finally {
            super.finalize();
        }
    }

    private void dispose() {
        long j = this.mPtr;
        if (j != 0) {
            nativeDestroy(j);
            this.mPtr = 0;
        }
    }

    public boolean isIdle() {
        boolean z;
        synchronized (this) {
            long now = SystemClock.uptimeMillis();
            if (this.mMessages != null) {
                if (now >= this.mMessages.when) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    public void addIdleHandler(IdleHandler handler) {
        if (handler != null) {
            synchronized (this) {
                this.mIdleHandlers.add(handler);
            }
            return;
        }
        throw new NullPointerException("Can't add a null IdleHandler");
    }

    public void removeIdleHandler(IdleHandler handler) {
        synchronized (this) {
            this.mIdleHandlers.remove(handler);
        }
    }

    public boolean isPolling() {
        boolean isPollingLocked;
        synchronized (this) {
            isPollingLocked = isPollingLocked();
        }
        return isPollingLocked;
    }

    private boolean isPollingLocked() {
        return !this.mQuitting && nativeIsPolling(this.mPtr);
    }

    public void addOnFileDescriptorEventListener(FileDescriptor fd, int events, OnFileDescriptorEventListener listener) {
        if (fd == null) {
            throw new IllegalArgumentException("fd must not be null");
        } else if (listener != null) {
            synchronized (this) {
                updateOnFileDescriptorEventListenerLocked(fd, events, listener);
            }
        } else {
            throw new IllegalArgumentException("listener must not be null");
        }
    }

    public void removeOnFileDescriptorEventListener(FileDescriptor fd) {
        if (fd != null) {
            synchronized (this) {
                updateOnFileDescriptorEventListenerLocked(fd, 0, null);
            }
            return;
        }
        throw new IllegalArgumentException("fd must not be null");
    }

    private void updateOnFileDescriptorEventListenerLocked(FileDescriptor fd, int events, OnFileDescriptorEventListener listener) {
        int fdNum = fd.getInt$();
        int index = -1;
        FileDescriptorRecord record = null;
        SparseArray sparseArray = this.mFileDescriptorRecords;
        if (sparseArray != null) {
            index = sparseArray.indexOfKey(fdNum);
            if (index >= 0) {
                record = (FileDescriptorRecord) this.mFileDescriptorRecords.valueAt(index);
                if (record != null && record.mEvents == events) {
                    return;
                }
            }
        }
        if (events != 0) {
            events |= 4;
            if (record == null) {
                if (this.mFileDescriptorRecords == null) {
                    this.mFileDescriptorRecords = new SparseArray();
                }
                this.mFileDescriptorRecords.put(fdNum, new FileDescriptorRecord(fd, events, listener));
            } else {
                record.mListener = listener;
                record.mEvents = events;
                record.mSeq++;
            }
            nativeSetFileDescriptorEvents(this.mPtr, fdNum, events);
        } else if (record != null) {
            record.mEvents = 0;
            this.mFileDescriptorRecords.removeAt(index);
            nativeSetFileDescriptorEvents(this.mPtr, fdNum, 0);
        }
    }

    /* JADX WARNING: Missing block: B:13:0x001a, code skipped:
            r4 = r2.onFileDescriptorEvents(r0.mDescriptor, r9);
     */
    /* JADX WARNING: Missing block: B:14:0x0020, code skipped:
            if (r4 == 0) goto L_0x0024;
     */
    /* JADX WARNING: Missing block: B:15:0x0022, code skipped:
            r4 = r4 | 4;
     */
    /* JADX WARNING: Missing block: B:16:0x0024, code skipped:
            if (r4 == r1) goto L_0x0049;
     */
    /* JADX WARNING: Missing block: B:17:0x0026, code skipped:
            monitor-enter(r7);
     */
    /* JADX WARNING: Missing block: B:19:?, code skipped:
            r5 = r7.mFileDescriptorRecords.indexOfKey(r8);
     */
    /* JADX WARNING: Missing block: B:20:0x002d, code skipped:
            if (r5 < 0) goto L_0x0044;
     */
    /* JADX WARNING: Missing block: B:22:0x0035, code skipped:
            if (r7.mFileDescriptorRecords.valueAt(r5) != r0) goto L_0x0044;
     */
    /* JADX WARNING: Missing block: B:24:0x0039, code skipped:
            if (r0.mSeq != r3) goto L_0x0044;
     */
    /* JADX WARNING: Missing block: B:25:0x003b, code skipped:
            r0.mEvents = r4;
     */
    /* JADX WARNING: Missing block: B:26:0x003d, code skipped:
            if (r4 != 0) goto L_0x0044;
     */
    /* JADX WARNING: Missing block: B:27:0x003f, code skipped:
            r7.mFileDescriptorRecords.removeAt(r5);
     */
    /* JADX WARNING: Missing block: B:28:0x0044, code skipped:
            monitor-exit(r7);
     */
    /* JADX WARNING: Missing block: B:33:0x0049, code skipped:
            return r4;
     */
    @android.annotation.UnsupportedAppUsage
    private int dispatchEvents(int r8, int r9) {
        /*
        r7 = this;
        monitor-enter(r7);
        r0 = r7.mFileDescriptorRecords;	 Catch:{ all -> 0x004a }
        r0 = r0.get(r8);	 Catch:{ all -> 0x004a }
        r0 = (android.os.MessageQueue.FileDescriptorRecord) r0;	 Catch:{ all -> 0x004a }
        if (r0 != 0) goto L_0x000e;
    L_0x000b:
        r1 = 0;
        monitor-exit(r7);	 Catch:{ all -> 0x004a }
        return r1;
    L_0x000e:
        r1 = r0.mEvents;	 Catch:{ all -> 0x004a }
        r9 = r9 & r1;
        if (r9 != 0) goto L_0x0015;
    L_0x0013:
        monitor-exit(r7);	 Catch:{ all -> 0x004a }
        return r1;
    L_0x0015:
        r2 = r0.mListener;	 Catch:{ all -> 0x004a }
        r3 = r0.mSeq;	 Catch:{ all -> 0x004a }
        monitor-exit(r7);	 Catch:{ all -> 0x004a }
        r4 = r0.mDescriptor;
        r4 = r2.onFileDescriptorEvents(r4, r9);
        if (r4 == 0) goto L_0x0024;
    L_0x0022:
        r4 = r4 | 4;
    L_0x0024:
        if (r4 == r1) goto L_0x0049;
    L_0x0026:
        monitor-enter(r7);
        r5 = r7.mFileDescriptorRecords;	 Catch:{ all -> 0x0046 }
        r5 = r5.indexOfKey(r8);	 Catch:{ all -> 0x0046 }
        if (r5 < 0) goto L_0x0044;
    L_0x002f:
        r6 = r7.mFileDescriptorRecords;	 Catch:{ all -> 0x0046 }
        r6 = r6.valueAt(r5);	 Catch:{ all -> 0x0046 }
        if (r6 != r0) goto L_0x0044;
    L_0x0037:
        r6 = r0.mSeq;	 Catch:{ all -> 0x0046 }
        if (r6 != r3) goto L_0x0044;
    L_0x003b:
        r0.mEvents = r4;	 Catch:{ all -> 0x0046 }
        if (r4 != 0) goto L_0x0044;
    L_0x003f:
        r6 = r7.mFileDescriptorRecords;	 Catch:{ all -> 0x0046 }
        r6.removeAt(r5);	 Catch:{ all -> 0x0046 }
    L_0x0044:
        monitor-exit(r7);	 Catch:{ all -> 0x0046 }
        goto L_0x0049;
    L_0x0046:
        r5 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x0046 }
        throw r5;
    L_0x0049:
        return r4;
    L_0x004a:
        r0 = move-exception;
        monitor-exit(r7);	 Catch:{ all -> 0x004a }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.MessageQueue.dispatchEvents(int, int):int");
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:49:0x0097, code skipped:
            r5 = 0;
     */
    /* JADX WARNING: Missing block: B:50:0x0098, code skipped:
            if (r5 >= r2) goto L_0x00bf;
     */
    /* JADX WARNING: Missing block: B:51:0x009a, code skipped:
            r6 = r13.mPendingIdleHandlers;
            r7 = r6[r5];
            r6[r5] = null;
            r6 = false;
     */
    /* JADX WARNING: Missing block: B:54:0x00a5, code skipped:
            r6 = r7.queueIdle();
     */
    /* JADX WARNING: Missing block: B:55:0x00a7, code skipped:
            r8 = move-exception;
     */
    /* JADX WARNING: Missing block: B:56:0x00a8, code skipped:
            android.util.Log.wtf(TAG, "IdleHandler threw exception", r8);
     */
    /* JADX WARNING: Missing block: B:67:0x00bf, code skipped:
            r2 = 0;
            r4 = 0;
     */
    @android.annotation.UnsupportedAppUsage
    public android.os.Message next() {
        /*
        r13 = this;
        r0 = r13.mPtr;
        r2 = 0;
        r2 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1));
        r3 = 0;
        if (r2 != 0) goto L_0x000a;
    L_0x0009:
        return r3;
    L_0x000a:
        r2 = -1;
        r4 = 0;
    L_0x000c:
        if (r4 == 0) goto L_0x0011;
    L_0x000e:
        android.os.Binder.flushPendingCommands();
    L_0x0011:
        r13.nativePollOnce(r0, r4);
        monitor-enter(r13);
        r5 = android.os.SystemClock.uptimeMillis();	 Catch:{ all -> 0x00c3 }
        r7 = 0;
        r8 = r13.mMessages;	 Catch:{ all -> 0x00c3 }
        if (r8 == 0) goto L_0x002e;
    L_0x001e:
        r9 = r8.target;	 Catch:{ all -> 0x00c3 }
        if (r9 != 0) goto L_0x002e;
    L_0x0022:
        r7 = r8;
        r9 = r8.next;	 Catch:{ all -> 0x00c3 }
        r8 = r9;
        if (r8 == 0) goto L_0x002e;
    L_0x0028:
        r9 = r8.isAsynchronous();	 Catch:{ all -> 0x00c3 }
        if (r9 == 0) goto L_0x0022;
    L_0x002e:
        if (r8 == 0) goto L_0x0057;
    L_0x0030:
        r9 = r8.when;	 Catch:{ all -> 0x00c3 }
        r9 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1));
        if (r9 >= 0) goto L_0x0042;
    L_0x0036:
        r9 = r8.when;	 Catch:{ all -> 0x00c3 }
        r9 = r9 - r5;
        r11 = 2147483647; // 0x7fffffff float:NaN double:1.060997895E-314;
        r9 = java.lang.Math.min(r9, r11);	 Catch:{ all -> 0x00c3 }
        r4 = (int) r9;	 Catch:{ all -> 0x00c3 }
        goto L_0x0058;
    L_0x0042:
        r9 = 0;
        r13.mBlocked = r9;	 Catch:{ all -> 0x00c3 }
        if (r7 == 0) goto L_0x004c;
    L_0x0047:
        r9 = r8.next;	 Catch:{ all -> 0x00c3 }
        r7.next = r9;	 Catch:{ all -> 0x00c3 }
        goto L_0x0050;
    L_0x004c:
        r9 = r8.next;	 Catch:{ all -> 0x00c3 }
        r13.mMessages = r9;	 Catch:{ all -> 0x00c3 }
    L_0x0050:
        r8.next = r3;	 Catch:{ all -> 0x00c3 }
        r8.markInUse();	 Catch:{ all -> 0x00c3 }
        monitor-exit(r13);	 Catch:{ all -> 0x00c3 }
        return r8;
    L_0x0057:
        r4 = -1;
    L_0x0058:
        r9 = r13.mQuitting;	 Catch:{ all -> 0x00c3 }
        if (r9 == 0) goto L_0x0061;
    L_0x005c:
        r13.dispose();	 Catch:{ all -> 0x00c3 }
        monitor-exit(r13);	 Catch:{ all -> 0x00c3 }
        return r3;
    L_0x0061:
        if (r2 >= 0) goto L_0x0076;
    L_0x0063:
        r9 = r13.mMessages;	 Catch:{ all -> 0x00c3 }
        if (r9 == 0) goto L_0x006f;
    L_0x0067:
        r9 = r13.mMessages;	 Catch:{ all -> 0x00c3 }
        r9 = r9.when;	 Catch:{ all -> 0x00c3 }
        r9 = (r5 > r9 ? 1 : (r5 == r9 ? 0 : -1));
        if (r9 >= 0) goto L_0x0076;
    L_0x006f:
        r9 = r13.mIdleHandlers;	 Catch:{ all -> 0x00c3 }
        r9 = r9.size();	 Catch:{ all -> 0x00c3 }
        r2 = r9;
    L_0x0076:
        if (r2 > 0) goto L_0x007d;
    L_0x0078:
        r9 = 1;
        r13.mBlocked = r9;	 Catch:{ all -> 0x00c3 }
        monitor-exit(r13);	 Catch:{ all -> 0x00c3 }
        goto L_0x000c;
    L_0x007d:
        r9 = r13.mPendingIdleHandlers;	 Catch:{ all -> 0x00c3 }
        if (r9 != 0) goto L_0x008a;
    L_0x0081:
        r9 = 4;
        r9 = java.lang.Math.max(r2, r9);	 Catch:{ all -> 0x00c3 }
        r9 = new android.os.MessageQueue.IdleHandler[r9];	 Catch:{ all -> 0x00c3 }
        r13.mPendingIdleHandlers = r9;	 Catch:{ all -> 0x00c3 }
    L_0x008a:
        r9 = r13.mIdleHandlers;	 Catch:{ all -> 0x00c3 }
        r10 = r13.mPendingIdleHandlers;	 Catch:{ all -> 0x00c3 }
        r9 = r9.toArray(r10);	 Catch:{ all -> 0x00c3 }
        r9 = (android.os.MessageQueue.IdleHandler[]) r9;	 Catch:{ all -> 0x00c3 }
        r13.mPendingIdleHandlers = r9;	 Catch:{ all -> 0x00c3 }
        monitor-exit(r13);	 Catch:{ all -> 0x00c3 }
        r5 = 0;
    L_0x0098:
        if (r5 >= r2) goto L_0x00bf;
    L_0x009a:
        r6 = r13.mPendingIdleHandlers;
        r7 = r6[r5];
        r6[r5] = r3;
        r6 = 0;
        r8 = r7.queueIdle();	 Catch:{ all -> 0x00a7 }
        r6 = r8;
        goto L_0x00af;
    L_0x00a7:
        r8 = move-exception;
        r9 = "MessageQueue";
        r10 = "IdleHandler threw exception";
        android.util.Log.wtf(r9, r10, r8);
    L_0x00af:
        if (r6 != 0) goto L_0x00bc;
    L_0x00b1:
        monitor-enter(r13);
        r8 = r13.mIdleHandlers;	 Catch:{ all -> 0x00b9 }
        r8.remove(r7);	 Catch:{ all -> 0x00b9 }
        monitor-exit(r13);	 Catch:{ all -> 0x00b9 }
        goto L_0x00bc;
    L_0x00b9:
        r3 = move-exception;
        monitor-exit(r13);	 Catch:{ all -> 0x00b9 }
        throw r3;
    L_0x00bc:
        r5 = r5 + 1;
        goto L_0x0098;
    L_0x00bf:
        r2 = 0;
        r4 = 0;
        goto L_0x000c;
    L_0x00c3:
        r3 = move-exception;
        monitor-exit(r13);	 Catch:{ all -> 0x00c3 }
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.MessageQueue.next():android.os.Message");
    }

    /* Access modifiers changed, original: 0000 */
    public void quit(boolean safe) {
        if (this.mQuitAllowed) {
            synchronized (this) {
                if (this.mQuitting) {
                    return;
                }
                this.mQuitting = true;
                if (safe) {
                    removeAllFutureMessagesLocked();
                } else {
                    removeAllMessagesLocked();
                }
                nativeWake(this.mPtr);
                return;
            }
        }
        throw new IllegalStateException("Main thread not allowed to quit.");
    }

    public int postSyncBarrier() {
        return postSyncBarrier(SystemClock.uptimeMillis());
    }

    private int postSyncBarrier(long when) {
        int token;
        synchronized (this) {
            token = this.mNextBarrierToken;
            this.mNextBarrierToken = token + 1;
            Message msg = Message.obtain();
            msg.markInUse();
            msg.when = when;
            msg.arg1 = token;
            Message prev = null;
            Message p = this.mMessages;
            if (when != 0) {
                while (p != null && p.when <= when) {
                    prev = p;
                    p = p.next;
                }
            }
            if (prev != null) {
                msg.next = p;
                prev.next = msg;
            } else {
                msg.next = p;
                this.mMessages = msg;
            }
        }
        return token;
    }

    public void removeSyncBarrier(int token) {
        synchronized (this) {
            Message prev = null;
            Message p = this.mMessages;
            while (p != null && (p.target != null || p.arg1 != token)) {
                prev = p;
                p = p.next;
            }
            if (p != null) {
                boolean needWake;
                if (prev != null) {
                    prev.next = p.next;
                    needWake = false;
                } else {
                    this.mMessages = p.next;
                    if (this.mMessages != null) {
                        if (this.mMessages.target == null) {
                            needWake = false;
                        }
                    }
                    needWake = true;
                }
                p.recycleUnchecked();
                if (needWake && !this.mQuitting) {
                    nativeWake(this.mPtr);
                }
            } else {
                throw new IllegalStateException("The specified message queue synchronization  barrier token has not been posted or has already been removed.");
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0085  */
    /* JADX WARNING: Missing block: B:40:0x008b, code skipped:
            return true;
     */
    public boolean enqueueMessage(android.os.Message r7, long r8) {
        /*
        r6 = this;
        r0 = r7.target;
        if (r0 == 0) goto L_0x00a6;
    L_0x0004:
        r0 = r7.isInUse();
        if (r0 != 0) goto L_0x008f;
    L_0x000a:
        r0 = r7.monitorInfo;
        r0.init(r7, r8);
        monitor-enter(r6);
        r0 = r6.mQuitting;	 Catch:{ all -> 0x008c }
        r1 = 0;
        if (r0 == 0) goto L_0x003b;
    L_0x0015:
        r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x008c }
        r2 = new java.lang.StringBuilder;	 Catch:{ all -> 0x008c }
        r2.<init>();	 Catch:{ all -> 0x008c }
        r3 = r7.target;	 Catch:{ all -> 0x008c }
        r2.append(r3);	 Catch:{ all -> 0x008c }
        r3 = " sending message to a Handler on a dead thread";
        r2.append(r3);	 Catch:{ all -> 0x008c }
        r2 = r2.toString();	 Catch:{ all -> 0x008c }
        r0.<init>(r2);	 Catch:{ all -> 0x008c }
        r2 = "MessageQueue";
        r3 = r0.getMessage();	 Catch:{ all -> 0x008c }
        android.util.Log.w(r2, r3, r0);	 Catch:{ all -> 0x008c }
        r7.recycle();	 Catch:{ all -> 0x008c }
        monitor-exit(r6);	 Catch:{ all -> 0x008c }
        return r1;
    L_0x003b:
        r7.markInUse();	 Catch:{ all -> 0x008c }
        r7.when = r8;	 Catch:{ all -> 0x008c }
        r0 = r6.mMessages;	 Catch:{ all -> 0x008c }
        r2 = 1;
        if (r0 == 0) goto L_0x007d;
    L_0x0045:
        r3 = 0;
        r3 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1));
        if (r3 == 0) goto L_0x007d;
    L_0x004b:
        r3 = r0.when;	 Catch:{ all -> 0x008c }
        r3 = (r8 > r3 ? 1 : (r8 == r3 ? 0 : -1));
        if (r3 >= 0) goto L_0x0052;
    L_0x0051:
        goto L_0x007d;
    L_0x0052:
        r3 = r6.mBlocked;	 Catch:{ all -> 0x008c }
        if (r3 == 0) goto L_0x0061;
    L_0x0056:
        r3 = r0.target;	 Catch:{ all -> 0x008c }
        if (r3 != 0) goto L_0x0061;
    L_0x005a:
        r3 = r7.isAsynchronous();	 Catch:{ all -> 0x008c }
        if (r3 == 0) goto L_0x0061;
    L_0x0060:
        r1 = r2;
    L_0x0061:
        r3 = r0;
        r4 = r0.next;	 Catch:{ all -> 0x008c }
        r0 = r4;
        if (r0 == 0) goto L_0x0078;
    L_0x0067:
        r4 = r0.when;	 Catch:{ all -> 0x008c }
        r4 = (r8 > r4 ? 1 : (r8 == r4 ? 0 : -1));
        if (r4 >= 0) goto L_0x006e;
    L_0x006d:
        goto L_0x0078;
    L_0x006e:
        if (r1 == 0) goto L_0x0061;
    L_0x0070:
        r4 = r0.isAsynchronous();	 Catch:{ all -> 0x008c }
        if (r4 == 0) goto L_0x0061;
    L_0x0076:
        r1 = 0;
        goto L_0x0061;
    L_0x0078:
        r7.next = r0;	 Catch:{ all -> 0x008c }
        r3.next = r7;	 Catch:{ all -> 0x008c }
        goto L_0x0083;
    L_0x007d:
        r7.next = r0;	 Catch:{ all -> 0x008c }
        r6.mMessages = r7;	 Catch:{ all -> 0x008c }
        r1 = r6.mBlocked;	 Catch:{ all -> 0x008c }
    L_0x0083:
        if (r1 == 0) goto L_0x008a;
    L_0x0085:
        r3 = r6.mPtr;	 Catch:{ all -> 0x008c }
        nativeWake(r3);	 Catch:{ all -> 0x008c }
    L_0x008a:
        monitor-exit(r6);	 Catch:{ all -> 0x008c }
        return r2;
    L_0x008c:
        r0 = move-exception;
        monitor-exit(r6);	 Catch:{ all -> 0x008c }
        throw r0;
    L_0x008f:
        r0 = new java.lang.IllegalStateException;
        r1 = new java.lang.StringBuilder;
        r1.<init>();
        r1.append(r7);
        r2 = " This message is already in use.";
        r1.append(r2);
        r1 = r1.toString();
        r0.<init>(r1);
        throw r0;
    L_0x00a6:
        r0 = new java.lang.IllegalArgumentException;
        r1 = "Message must have a target.";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.MessageQueue.enqueueMessage(android.os.Message, long):boolean");
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasMessages(Handler h, int what, Object object) {
        if (h == null) {
            return false;
        }
        synchronized (this) {
            Message p = this.mMessages;
            while (p != null) {
                if (p.target == h && p.what == what && (object == null || p.obj == object)) {
                    return true;
                }
                p = p.next;
            }
            return false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    @UnsupportedAppUsage
    public boolean hasMessages(Handler h, Runnable r, Object object) {
        if (h == null) {
            return false;
        }
        synchronized (this) {
            Message p = this.mMessages;
            while (p != null) {
                if (p.target == h && p.callback == r && (object == null || p.obj == object)) {
                    return true;
                }
                p = p.next;
            }
            return false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasMessages(Handler h) {
        if (h == null) {
            return false;
        }
        synchronized (this) {
            for (Message p = this.mMessages; p != null; p = p.next) {
                if (p.target == h) {
                    return true;
                }
            }
            return false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void removeMessages(Handler h, int what, Object object) {
        if (h != null) {
            synchronized (this) {
                Message n;
                Message p = this.mMessages;
                while (p != null && p.target == h && p.what == what && (object == null || p.obj == object)) {
                    n = p.next;
                    this.mMessages = n;
                    p.recycleUnchecked();
                    p = n;
                }
                while (p != null) {
                    n = p.next;
                    if (n != null && n.target == h && n.what == what && (object == null || n.obj == object)) {
                        Message nn = n.next;
                        n.recycleUnchecked();
                        p.next = nn;
                    } else {
                        p = n;
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void removeMessages(Handler h, Runnable r, Object object) {
        if (h != null && r != null) {
            synchronized (this) {
                Message n;
                Message p = this.mMessages;
                while (p != null && p.target == h && p.callback == r && (object == null || p.obj == object)) {
                    n = p.next;
                    this.mMessages = n;
                    p.recycleUnchecked();
                    p = n;
                }
                while (p != null) {
                    n = p.next;
                    if (n != null && n.target == h && n.callback == r && (object == null || n.obj == object)) {
                        Message nn = n.next;
                        n.recycleUnchecked();
                        p.next = nn;
                    } else {
                        p = n;
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void removeCallbacksAndMessages(Handler h, Object object) {
        if (h != null) {
            synchronized (this) {
                Message n;
                Message p = this.mMessages;
                while (p != null && p.target == h && (object == null || p.obj == object)) {
                    n = p.next;
                    this.mMessages = n;
                    p.recycleUnchecked();
                    p = n;
                }
                while (p != null) {
                    n = p.next;
                    if (n != null && n.target == h && (object == null || n.obj == object)) {
                        Message nn = n.next;
                        n.recycleUnchecked();
                        p.next = nn;
                    } else {
                        p = n;
                    }
                }
            }
        }
    }

    private void removeAllMessagesLocked() {
        Message p = this.mMessages;
        while (p != null) {
            Message n = p.next;
            p.recycleUnchecked();
            p = n;
        }
        this.mMessages = null;
    }

    private void removeAllFutureMessagesLocked() {
        long now = SystemClock.uptimeMillis();
        Message p = this.mMessages;
        if (p != null) {
            if (p.when > now) {
                removeAllMessagesLocked();
            } else {
                while (true) {
                    Message n = p.next;
                    if (n != null) {
                        if (n.when > now) {
                            p.next = null;
                            do {
                                p = n;
                                n = p.next;
                                p.recycleUnchecked();
                            } while (n != null);
                            break;
                        }
                        p = n;
                    } else {
                        return;
                    }
                }
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void dump(Printer pw, String prefix, Handler h) {
        synchronized (this) {
            long now = SystemClock.uptimeMillis();
            int n = 0;
            Message msg = this.mMessages;
            while (msg != null) {
                if (h == null || h == msg.target) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(prefix);
                    stringBuilder.append("Message ");
                    stringBuilder.append(n);
                    stringBuilder.append(": ");
                    stringBuilder.append(msg.toString(now));
                    pw.println(stringBuilder.toString());
                }
                n++;
                msg = msg.next;
            }
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append(prefix);
            stringBuilder2.append("(Total messages: ");
            stringBuilder2.append(n);
            stringBuilder2.append(", polling=");
            stringBuilder2.append(isPollingLocked());
            stringBuilder2.append(", quitting=");
            stringBuilder2.append(this.mQuitting);
            stringBuilder2.append(")");
            pw.println(stringBuilder2.toString());
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void writeToProto(ProtoOutputStream proto, long fieldId) {
        long messageQueueToken = proto.start(fieldId);
        synchronized (this) {
            for (Message msg = this.mMessages; msg != null; msg = msg.next) {
                msg.writeToProto(proto, 2246267895809L);
            }
            proto.write(1133871366146L, isPollingLocked());
            proto.write(1133871366147L, this.mQuitting);
        }
        proto.end(messageQueueToken);
    }
}
