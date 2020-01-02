package android.os;

import android.os.ICancellationSignal.Stub;

public final class CancellationSignal {
    private boolean mCancelInProgress;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;
    private ICancellationSignal mRemote;

    public interface OnCancelListener {
        void onCancel();
    }

    private static final class Transport extends Stub {
        final CancellationSignal mCancellationSignal;

        private Transport() {
            this.mCancellationSignal = new CancellationSignal();
        }

        public void cancel() throws RemoteException {
            this.mCancellationSignal.cancel();
        }
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this) {
            z = this.mIsCanceled;
        }
        return z;
    }

    public void throwIfCanceled() {
        if (isCanceled()) {
            throw new OperationCanceledException();
        }
    }

    /* JADX WARNING: Missing block: B:9:0x0012, code skipped:
            if (r0 == null) goto L_0x001a;
     */
    /* JADX WARNING: Missing block: B:11:?, code skipped:
            r0.onCancel();
     */
    /* JADX WARNING: Missing block: B:13:0x001a, code skipped:
            if (r1 == null) goto L_0x002c;
     */
    /* JADX WARNING: Missing block: B:15:?, code skipped:
            r1.cancel();
     */
    /* JADX WARNING: Missing block: B:16:0x0020, code skipped:
            monitor-enter(r4);
     */
    /* JADX WARNING: Missing block: B:18:?, code skipped:
            r4.mCancelInProgress = false;
            notifyAll();
     */
    public void cancel() {
        /*
        r4 = this;
        monitor-enter(r4);
        r0 = r4.mIsCanceled;	 Catch:{ all -> 0x0038 }
        if (r0 == 0) goto L_0x0007;
    L_0x0005:
        monitor-exit(r4);	 Catch:{ all -> 0x0038 }
        return;
    L_0x0007:
        r0 = 1;
        r4.mIsCanceled = r0;	 Catch:{ all -> 0x0038 }
        r4.mCancelInProgress = r0;	 Catch:{ all -> 0x0038 }
        r0 = r4.mOnCancelListener;	 Catch:{ all -> 0x0038 }
        r1 = r4.mRemote;	 Catch:{ all -> 0x0038 }
        monitor-exit(r4);	 Catch:{ all -> 0x0038 }
        r2 = 0;
        if (r0 == 0) goto L_0x001a;
    L_0x0014:
        r0.onCancel();	 Catch:{ all -> 0x0018 }
        goto L_0x001a;
    L_0x0018:
        r3 = move-exception;
        goto L_0x0020;
    L_0x001a:
        if (r1 == 0) goto L_0x002c;
    L_0x001c:
        r1.cancel();	 Catch:{ RemoteException -> 0x002b }
        goto L_0x002c;
    L_0x0020:
        monitor-enter(r4);
        r4.mCancelInProgress = r2;	 Catch:{ all -> 0x0028 }
        r4.notifyAll();	 Catch:{ all -> 0x0028 }
        monitor-exit(r4);	 Catch:{ all -> 0x0028 }
        throw r3;
    L_0x0028:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0028 }
        throw r2;
    L_0x002b:
        r3 = move-exception;
    L_0x002c:
        monitor-enter(r4);
        r4.mCancelInProgress = r2;	 Catch:{ all -> 0x0035 }
        r4.notifyAll();	 Catch:{ all -> 0x0035 }
        monitor-exit(r4);	 Catch:{ all -> 0x0035 }
        return;
    L_0x0035:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0035 }
        throw r2;
    L_0x0038:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0038 }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.CancellationSignal.cancel():void");
    }

    public void setOnCancelListener(OnCancelListener listener) {
        synchronized (this) {
            waitForCancelFinishedLocked();
            if (this.mOnCancelListener == listener) {
                return;
            }
            this.mOnCancelListener = listener;
            if (this.mIsCanceled) {
                if (listener != null) {
                    listener.onCancel();
                    return;
                }
            }
        }
    }

    /* JADX WARNING: Missing block: B:12:?, code skipped:
            r2.cancel();
     */
    public void setRemote(android.os.ICancellationSignal r2) {
        /*
        r1 = this;
        monitor-enter(r1);
        r1.waitForCancelFinishedLocked();	 Catch:{ all -> 0x001c }
        r0 = r1.mRemote;	 Catch:{ all -> 0x001c }
        if (r0 != r2) goto L_0x000a;
    L_0x0008:
        monitor-exit(r1);	 Catch:{ all -> 0x001c }
        return;
    L_0x000a:
        r1.mRemote = r2;	 Catch:{ all -> 0x001c }
        r0 = r1.mIsCanceled;	 Catch:{ all -> 0x001c }
        if (r0 == 0) goto L_0x001a;
    L_0x0010:
        if (r2 != 0) goto L_0x0013;
    L_0x0012:
        goto L_0x001a;
    L_0x0013:
        monitor-exit(r1);	 Catch:{ all -> 0x001c }
        r2.cancel();	 Catch:{ RemoteException -> 0x0018 }
        goto L_0x0019;
    L_0x0018:
        r0 = move-exception;
    L_0x0019:
        return;
    L_0x001a:
        monitor-exit(r1);	 Catch:{ all -> 0x001c }
        return;
    L_0x001c:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001c }
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.CancellationSignal.setRemote(android.os.ICancellationSignal):void");
    }

    private void waitForCancelFinishedLocked() {
        while (this.mCancelInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public static ICancellationSignal createTransport() {
        return new Transport();
    }

    public static CancellationSignal fromTransport(ICancellationSignal transport) {
        if (transport instanceof Transport) {
            return ((Transport) transport).mCancellationSignal;
        }
        return null;
    }
}
