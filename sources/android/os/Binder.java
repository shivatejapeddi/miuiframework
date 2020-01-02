package android.os;

import android.annotation.SystemApi;
import android.annotation.UnsupportedAppUsage;
import android.os.IBinder.DeathRecipient;
import android.util.ExceptionUtils;
import android.util.Log;
import com.android.internal.os.BinderInternal;
import com.android.internal.os.BinderInternal.Observer;
import com.android.internal.os.BinderInternal.WorkSourceProvider;
import com.android.internal.util.FastPrintWriter;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import com.android.internal.util.FunctionalUtils.ThrowingSupplier;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import libcore.io.IoUtils;
import libcore.util.NativeAllocationRegistry;

public class Binder implements IBinder {
    public static final boolean CHECK_PARCEL_SIZE = false;
    private static final boolean FIND_POTENTIAL_LEAKS = false;
    public static boolean LOG_RUNTIME_EXCEPTION = false;
    private static final int NATIVE_ALLOCATION_SIZE = 500;
    static final String TAG = "Binder";
    public static final int UNSET_WORKSOURCE = -1;
    private static volatile String sDumpDisabled = null;
    private static Observer sObserver = null;
    private static volatile boolean sTracingEnabled = false;
    private static volatile TransactionTracker sTransactionTracker = null;
    static volatile boolean sWarnOnBlocking = false;
    private static volatile WorkSourceProvider sWorkSourceProvider = -$$Lambda$Binder$IYUHVkWouPK_9CG2s8VwyWBt5_I.INSTANCE;
    private String mDescriptor;
    @UnsupportedAppUsage
    private final long mObject;
    private IInterface mOwner;

    private static class NoImagePreloadHolder {
        public static final NativeAllocationRegistry sRegistry = new NativeAllocationRegistry(Binder.class.getClassLoader(), Binder.getNativeFinalizer(), 500);

        private NoImagePreloadHolder() {
        }
    }

    @SystemApi
    public interface ProxyTransactListener {
        void onTransactEnded(Object obj);

        Object onTransactStarted(IBinder iBinder, int i);
    }

    public static class PropagateWorkSourceTransactListener implements ProxyTransactListener {
        public Object onTransactStarted(IBinder binder, int transactionCode) {
            int uid = ThreadLocalWorkSource.getUid();
            if (uid != -1) {
                return Long.valueOf(Binder.setCallingWorkSourceUid(uid));
            }
            return null;
        }

        public void onTransactEnded(Object session) {
            if (session != null) {
                Binder.restoreCallingWorkSource(((Long) session).longValue());
            }
        }
    }

    public static final native void blockUntilThreadAvailable();

    public static final native long clearCallingIdentity();

    public static final native long clearCallingWorkSource();

    public static final native void flushPendingCommands();

    public static final native int getCallingPid();

    public static final native int getCallingUid();

    public static final native int getCallingWorkSourceUid();

    private static native long getFinalizer();

    private static native long getNativeBBinderHolder();

    private static native long getNativeFinalizer();

    public static final native int getThreadStrictModePolicy();

    public static final native boolean isHandlingTransaction();

    public static final native void restoreCallingIdentity(long j);

    public static final native void restoreCallingWorkSource(long j);

    public static final native long setCallingWorkSourceUid(int i);

    public static final native void setThreadStrictModePolicy(int i);

    public static void enableTracing() {
        sTracingEnabled = true;
    }

    public static void disableTracing() {
        sTracingEnabled = false;
    }

    public static boolean isTracingEnabled() {
        return sTracingEnabled;
    }

    public static synchronized TransactionTracker getTransactionTracker() {
        TransactionTracker transactionTracker;
        synchronized (Binder.class) {
            if (sTransactionTracker == null) {
                sTransactionTracker = new TransactionTracker();
            }
            transactionTracker = sTransactionTracker;
        }
        return transactionTracker;
    }

    public static void setObserver(Observer observer) {
        sObserver = observer;
    }

    public static void setWarnOnBlocking(boolean warnOnBlocking) {
        sWarnOnBlocking = warnOnBlocking;
    }

    public static IBinder allowBlocking(IBinder binder) {
        try {
            if (binder instanceof BinderProxy) {
                ((BinderProxy) binder).mWarnOnBlocking = false;
            } else if (!(binder == null || binder.getInterfaceDescriptor() == null || binder.queryLocalInterface(binder.getInterfaceDescriptor()) != null)) {
                String str = TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Unable to allow blocking on interface ");
                stringBuilder.append(binder);
                Log.w(str, stringBuilder.toString());
            }
        } catch (RemoteException e) {
        }
        return binder;
    }

    public static IBinder defaultBlocking(IBinder binder) {
        if (binder instanceof BinderProxy) {
            ((BinderProxy) binder).mWarnOnBlocking = sWarnOnBlocking;
        }
        return binder;
    }

    public static void copyAllowBlocking(IBinder fromBinder, IBinder toBinder) {
        if ((fromBinder instanceof BinderProxy) && (toBinder instanceof BinderProxy)) {
            ((BinderProxy) toBinder).mWarnOnBlocking = ((BinderProxy) fromBinder).mWarnOnBlocking;
        }
    }

    public static final int getCallingUidOrThrow() {
        if (isHandlingTransaction()) {
            return getCallingUid();
        }
        throw new IllegalStateException("Thread is not in a binder transcation");
    }

    public static final UserHandle getCallingUserHandle() {
        return UserHandle.of(UserHandle.getUserId(getCallingUid()));
    }

    public static final void withCleanCallingIdentity(ThrowingRunnable action) {
        long callingIdentity = clearCallingIdentity();
        try {
            action.runOrThrow();
            restoreCallingIdentity(callingIdentity);
            if (null != null) {
                throw ExceptionUtils.propagate(null);
            }
        } catch (Throwable throwable) {
            Throwable throwableToPropagate = throwable;
            restoreCallingIdentity(callingIdentity);
            RuntimeException propagate = ExceptionUtils.propagate(throwableToPropagate);
        }
    }

    public static final <T> T withCleanCallingIdentity(ThrowingSupplier<T> action) {
        long callingIdentity = clearCallingIdentity();
        try {
            Object orThrow = action.getOrThrow();
            restoreCallingIdentity(callingIdentity);
            if (null == null) {
                return orThrow;
            }
            throw ExceptionUtils.propagate(null);
        } catch (Throwable throwable) {
            Throwable throwableToPropagate = throwable;
            restoreCallingIdentity(callingIdentity);
            RuntimeException propagate = ExceptionUtils.propagate(throwableToPropagate);
        }
    }

    public static final void joinThreadPool() {
        BinderInternal.joinThreadPool();
    }

    public static final boolean isProxy(IInterface iface) {
        return iface.asBinder() != iface;
    }

    public Binder() {
        this(null);
    }

    public Binder(String descriptor) {
        this.mObject = getNativeBBinderHolder();
        NoImagePreloadHolder.sRegistry.registerNativeAllocation(this, this.mObject);
        this.mDescriptor = descriptor;
    }

    public void attachInterface(IInterface owner, String descriptor) {
        this.mOwner = owner;
        this.mDescriptor = descriptor;
    }

    public String getInterfaceDescriptor() {
        return this.mDescriptor;
    }

    public boolean pingBinder() {
        return true;
    }

    public boolean isBinderAlive() {
        return true;
    }

    public IInterface queryLocalInterface(String descriptor) {
        String str = this.mDescriptor;
        if (str == null || !str.equals(descriptor)) {
            return null;
        }
        return this.mOwner;
    }

    public static void setDumpDisabled(String msg) {
        sDumpDisabled = msg;
    }

    @SystemApi
    public static void setProxyTransactListener(ProxyTransactListener listener) {
        BinderProxy.setTransactListener(listener);
    }

    /* Access modifiers changed, original: protected */
    public boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        Throwable th;
        int i = code;
        Parcel parcel = data;
        Parcel parcel2 = reply;
        ParcelFileDescriptor fd;
        if (i == 1598968902) {
            parcel2.writeString(getInterfaceDescriptor());
            return true;
        } else if (i == 1598311760) {
            fd = data.readFileDescriptor();
            String[] args = data.readStringArray();
            if (fd != null) {
                try {
                    try {
                        dump(fd.getFileDescriptor(), args);
                        IoUtils.closeQuietly(fd);
                    } catch (Throwable th2) {
                        th = th2;
                        IoUtils.closeQuietly(fd);
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    IoUtils.closeQuietly(fd);
                    throw th;
                }
            }
            if (parcel2 != null) {
                reply.writeNoException();
            } else {
                StrictMode.clearGatheredViolations();
            }
            return true;
        } else {
            if (i != 1598246212) {
                return false;
            }
            fd = data.readFileDescriptor();
            ParcelFileDescriptor out = data.readFileDescriptor();
            ParcelFileDescriptor err = data.readFileDescriptor();
            String[] args2 = data.readStringArray();
            ShellCallback shellCallback = (ShellCallback) ShellCallback.CREATOR.createFromParcel(parcel);
            ResultReceiver resultReceiver = (ResultReceiver) ResultReceiver.CREATOR.createFromParcel(parcel);
            if (out != null) {
                FileDescriptor fileDescriptor;
                if (fd != null) {
                    try {
                        fileDescriptor = fd.getFileDescriptor();
                    } catch (Throwable th4) {
                        IoUtils.closeQuietly(fd);
                        IoUtils.closeQuietly(out);
                        IoUtils.closeQuietly(err);
                        if (parcel2 != null) {
                            reply.writeNoException();
                        } else {
                            StrictMode.clearGatheredViolations();
                        }
                    }
                } else {
                    fileDescriptor = null;
                }
                shellCommand(fileDescriptor, out.getFileDescriptor(), err != null ? err.getFileDescriptor() : out.getFileDescriptor(), args2, shellCallback, resultReceiver);
            }
            IoUtils.closeQuietly(fd);
            IoUtils.closeQuietly(out);
            IoUtils.closeQuietly(err);
            if (parcel2 != null) {
                reply.writeNoException();
            } else {
                StrictMode.clearGatheredViolations();
            }
            return true;
        }
    }

    public String getTransactionName(int transactionCode) {
        return null;
    }

    public void dump(FileDescriptor fd, String[] args) {
        PrintWriter pw = new FastPrintWriter(new FileOutputStream(fd));
        try {
            doDump(fd, pw, args);
        } finally {
            pw.flush();
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void doDump(FileDescriptor fd, PrintWriter pw, String[] args) {
        if (sDumpDisabled == null) {
            try {
                dump(fd, pw, args);
                return;
            } catch (SecurityException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Security exception: ");
                stringBuilder.append(e.getMessage());
                pw.println(stringBuilder.toString());
                throw e;
            } catch (Throwable e2) {
                pw.println();
                pw.println("Exception occurred while dumping:");
                e2.printStackTrace(pw);
                return;
            }
        }
        pw.println(sDumpDisabled);
    }

    public void dumpAsync(FileDescriptor fd, String[] args) {
        final PrintWriter pw = new FastPrintWriter(new FileOutputStream(fd));
        final FileDescriptor fileDescriptor = fd;
        final String[] strArr = args;
        new Thread("Binder.dumpAsync") {
            public void run() {
                try {
                    Binder.this.dump(fileDescriptor, pw, strArr);
                } finally {
                    pw.flush();
                }
            }
        }.start();
    }

    /* Access modifiers changed, original: protected */
    public void dump(FileDescriptor fd, PrintWriter fout, String[] args) {
    }

    public void shellCommand(FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, ResultReceiver resultReceiver) throws RemoteException {
        onShellCommand(in, out, err, args, callback, resultReceiver);
    }

    public void onShellCommand(FileDescriptor in, FileDescriptor out, FileDescriptor err, String[] args, ShellCallback callback, ResultReceiver resultReceiver) throws RemoteException {
        PrintWriter pw = new FastPrintWriter(new FileOutputStream(err != null ? err : out));
        pw.println("No shell command implementation.");
        pw.flush();
        resultReceiver.send(0, null);
    }

    public final boolean transact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
        if (data != null) {
            data.setDataPosition(0);
        }
        boolean r = onTransact(code, data, reply, flags);
        if (reply != null) {
            reply.setDataPosition(0);
        }
        return r;
    }

    public void linkToDeath(DeathRecipient recipient, int flags) {
    }

    public boolean unlinkToDeath(DeathRecipient recipient, int flags) {
        return true;
    }

    static void checkParcel(IBinder obj, int code, Parcel parcel, String msg) {
    }

    public static void setWorkSourceProvider(WorkSourceProvider workSourceProvider) {
        if (workSourceProvider != null) {
            sWorkSourceProvider = workSourceProvider;
            return;
        }
        throw new IllegalArgumentException("workSourceProvider cannot be null");
    }

    @UnsupportedAppUsage
    private boolean execTransact(int code, long dataObj, long replyObj, int flags) {
        int callingUid = getCallingUid();
        long origWorkSource = ThreadLocalWorkSource.setUid(callingUid);
        try {
            boolean execTransactInternal = execTransactInternal(code, dataObj, replyObj, flags, callingUid);
            ThreadLocalWorkSource.restore(origWorkSource);
            return execTransactInternal;
        } catch (Throwable th) {
            Throwable th2 = th;
            ThreadLocalWorkSource.restore(origWorkSource);
        }
    }

    /* JADX WARNING: Missing block: B:15:0x0054, code skipped:
            if (r4 != null) goto L_0x0056;
     */
    /* JADX WARNING: Missing block: B:16:0x0056, code skipped:
            r4.callEnded(r5, r6.dataSize(), r7.dataSize(), sWorkSourceProvider.resolveWorkSourceUid(r6.readCallingWorkSourceUid()));
     */
    /* JADX WARNING: Missing block: B:37:0x00a1, code skipped:
            if (r4 != null) goto L_0x0056;
     */
    /* JADX WARNING: Missing block: B:38:0x00a4, code skipped:
            checkParcel(r14, r15, r7, "Unreasonably large binder reply buffer");
            r7.recycle();
            r6.recycle();
            android.os.StrictMode.clearGatheredViolations();
     */
    /* JADX WARNING: Missing block: B:39:0x00b2, code skipped:
            return r0;
     */
    private boolean execTransactInternal(int r15, long r16, long r18, int r20, int r21) {
        /*
        r14 = this;
        r1 = r14;
        r2 = r15;
        r3 = r20;
        r4 = sObserver;
        if (r4 == 0) goto L_0x000e;
    L_0x0008:
        r0 = -1;
        r0 = r4.callStarted(r14, r15, r0);
        goto L_0x000f;
    L_0x000e:
        r0 = 0;
    L_0x000f:
        r5 = r0;
        r6 = android.os.Parcel.obtain(r16);
        r7 = android.os.Parcel.obtain(r18);
        r8 = isTracingEnabled();
        r9 = 1;
        if (r8 == 0) goto L_0x004b;
    L_0x0020:
        r0 = r14.getTransactionName(r15);	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
        r11 = new java.lang.StringBuilder;	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
        r11.<init>();	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
        r12 = r14.getClass();	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
        r12 = r12.getName();	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
        r11.append(r12);	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
        r12 = ":";
        r11.append(r12);	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
        if (r0 == 0) goto L_0x003d;
    L_0x003b:
        r12 = r0;
        goto L_0x0041;
    L_0x003d:
        r12 = java.lang.Integer.valueOf(r15);	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
    L_0x0041:
        r11.append(r12);	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
        r11 = r11.toString();	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
        android.os.Trace.traceBegin(r9, r11);	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
    L_0x004b:
        r0 = r14.onTransact(r15, r6, r7, r3);	 Catch:{ RemoteException | RuntimeException -> 0x006e, RemoteException | RuntimeException -> 0x006e }
        if (r8 == 0) goto L_0x0054;
    L_0x0051:
        android.os.Trace.traceEnd(r9);
    L_0x0054:
        if (r4 == 0) goto L_0x00a4;
    L_0x0056:
        r9 = sWorkSourceProvider;
        r10 = r6.readCallingWorkSourceUid();
        r9 = r9.resolveWorkSourceUid(r10);
        r10 = r6.dataSize();
        r11 = r7.dataSize();
        r4.callEnded(r5, r10, r11, r9);
        goto L_0x00a4;
    L_0x006c:
        r0 = move-exception;
        goto L_0x00b3;
    L_0x006e:
        r0 = move-exception;
        if (r4 == 0) goto L_0x0074;
    L_0x0071:
        r4.callThrewException(r5, r0);	 Catch:{ all -> 0x006c }
    L_0x0074:
        r11 = LOG_RUNTIME_EXCEPTION;	 Catch:{ all -> 0x006c }
        r12 = "Caught a RuntimeException from the binder stub implementation.";
        r13 = "Binder";
        if (r11 == 0) goto L_0x007f;
    L_0x007c:
        android.util.Log.w(r13, r12, r0);	 Catch:{ all -> 0x006c }
    L_0x007f:
        r11 = r3 & 1;
        if (r11 == 0) goto L_0x0091;
    L_0x0083:
        r11 = r0 instanceof android.os.RemoteException;	 Catch:{ all -> 0x006c }
        if (r11 == 0) goto L_0x008d;
    L_0x0087:
        r11 = "Binder call failed.";
        android.util.Log.w(r13, r11, r0);	 Catch:{ all -> 0x006c }
        goto L_0x009b;
    L_0x008d:
        android.util.Log.w(r13, r12, r0);	 Catch:{ all -> 0x006c }
        goto L_0x009b;
    L_0x0091:
        r11 = 0;
        r7.setDataSize(r11);	 Catch:{ all -> 0x006c }
        r7.setDataPosition(r11);	 Catch:{ all -> 0x006c }
        r7.writeException(r0);	 Catch:{ all -> 0x006c }
    L_0x009b:
        r0 = 1;
        if (r8 == 0) goto L_0x00a1;
    L_0x009e:
        android.os.Trace.traceEnd(r9);
    L_0x00a1:
        if (r4 == 0) goto L_0x00a4;
    L_0x00a3:
        goto L_0x0056;
    L_0x00a4:
        r9 = "Unreasonably large binder reply buffer";
        checkParcel(r14, r15, r7, r9);
        r7.recycle();
        r6.recycle();
        android.os.StrictMode.clearGatheredViolations();
        return r0;
    L_0x00b3:
        if (r8 == 0) goto L_0x00b8;
    L_0x00b5:
        android.os.Trace.traceEnd(r9);
    L_0x00b8:
        if (r4 == 0) goto L_0x00cf;
    L_0x00ba:
        r9 = sWorkSourceProvider;
        r10 = r6.readCallingWorkSourceUid();
        r9 = r9.resolveWorkSourceUid(r10);
        r10 = r6.dataSize();
        r11 = r7.dataSize();
        r4.callEnded(r5, r10, r11, r9);
    L_0x00cf:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.Binder.execTransactInternal(int, long, long, int, int):boolean");
    }
}
