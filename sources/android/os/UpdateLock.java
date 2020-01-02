package android.os;

import android.annotation.UnsupportedAppUsage;
import android.content.Context;
import android.os.IUpdateLock.Stub;
import android.util.Log;

public class UpdateLock {
    private static final boolean DEBUG = false;
    @UnsupportedAppUsage
    public static final String NOW_IS_CONVENIENT = "nowisconvenient";
    private static final String TAG = "UpdateLock";
    @UnsupportedAppUsage
    public static final String TIMESTAMP = "timestamp";
    @UnsupportedAppUsage
    public static final String UPDATE_LOCK_CHANGED = "android.os.UpdateLock.UPDATE_LOCK_CHANGED";
    private static IUpdateLock sService;
    int mCount = 0;
    boolean mHeld = false;
    boolean mRefCounted = true;
    final String mTag;
    IBinder mToken;

    private static void checkService() {
        if (sService == null) {
            sService = Stub.asInterface(ServiceManager.getService(Context.UPDATE_LOCK_SERVICE));
        }
    }

    public UpdateLock(String tag) {
        this.mTag = tag;
        this.mToken = new Binder();
    }

    public void setReferenceCounted(boolean isRefCounted) {
        this.mRefCounted = isRefCounted;
    }

    @UnsupportedAppUsage
    public boolean isHeld() {
        boolean z;
        synchronized (this.mToken) {
            z = this.mHeld;
        }
        return z;
    }

    @UnsupportedAppUsage
    public void acquire() {
        checkService();
        synchronized (this.mToken) {
            acquireLocked();
        }
    }

    private void acquireLocked() {
        if (this.mRefCounted) {
            int i = this.mCount;
            this.mCount = i + 1;
            if (i != 0) {
                return;
            }
        }
        IUpdateLock iUpdateLock = sService;
        if (iUpdateLock != null) {
            try {
                iUpdateLock.acquireUpdateLock(this.mToken, this.mTag);
            } catch (RemoteException e) {
                Log.e(TAG, "Unable to contact service to acquire");
            }
        }
        this.mHeld = true;
    }

    @UnsupportedAppUsage
    public void release() {
        checkService();
        synchronized (this.mToken) {
            releaseLocked();
        }
    }

    /* JADX WARNING: Missing block: B:3:0x000a, code skipped:
            if (r0 == 0) goto L_0x000c;
     */
    private void releaseLocked() {
        /*
        r3 = this;
        r0 = r3.mRefCounted;
        if (r0 == 0) goto L_0x000c;
    L_0x0004:
        r0 = r3.mCount;
        r0 = r0 + -1;
        r3.mCount = r0;
        if (r0 != 0) goto L_0x0021;
    L_0x000c:
        r0 = sService;
        if (r0 == 0) goto L_0x001e;
    L_0x0010:
        r1 = r3.mToken;	 Catch:{ RemoteException -> 0x0016 }
        r0.releaseUpdateLock(r1);	 Catch:{ RemoteException -> 0x0016 }
        goto L_0x001e;
    L_0x0016:
        r0 = move-exception;
        r1 = "UpdateLock";
        r2 = "Unable to contact service to release";
        android.util.Log.e(r1, r2);
    L_0x001e:
        r0 = 0;
        r3.mHeld = r0;
    L_0x0021:
        r0 = r3.mCount;
        if (r0 < 0) goto L_0x0026;
    L_0x0025:
        return;
    L_0x0026:
        r0 = new java.lang.RuntimeException;
        r1 = "UpdateLock under-locked";
        r0.<init>(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.os.UpdateLock.releaseLocked():void");
    }

    /* Access modifiers changed, original: protected */
    public void finalize() throws Throwable {
        synchronized (this.mToken) {
            if (this.mHeld) {
                Log.wtf(TAG, "UpdateLock finalized while still held");
                try {
                    sService.releaseUpdateLock(this.mToken);
                } catch (RemoteException e) {
                    Log.e(TAG, "Unable to contact service to release");
                }
            }
        }
    }
}
