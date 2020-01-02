package android.accessibilityservice;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Slog;
import com.android.internal.util.Preconditions;

public final class AccessibilityButtonController {
    private static final String LOG_TAG = "A11yButtonController";
    private ArrayMap<AccessibilityButtonCallback, Handler> mCallbacks;
    private final Object mLock = new Object();
    private final IAccessibilityServiceConnection mServiceConnection;

    public static abstract class AccessibilityButtonCallback {
        public void onClicked(AccessibilityButtonController controller) {
        }

        public void onAvailabilityChanged(AccessibilityButtonController controller, boolean available) {
        }
    }

    AccessibilityButtonController(IAccessibilityServiceConnection serviceConnection) {
        this.mServiceConnection = serviceConnection;
    }

    public boolean isAccessibilityButtonAvailable() {
        IAccessibilityServiceConnection iAccessibilityServiceConnection = this.mServiceConnection;
        if (iAccessibilityServiceConnection == null) {
            return false;
        }
        try {
            return iAccessibilityServiceConnection.isAccessibilityButtonAvailable();
        } catch (RemoteException re) {
            Slog.w(LOG_TAG, "Failed to get accessibility button availability.", re);
            re.rethrowFromSystemServer();
            return false;
        }
    }

    public void registerAccessibilityButtonCallback(AccessibilityButtonCallback callback) {
        registerAccessibilityButtonCallback(callback, new Handler(Looper.getMainLooper()));
    }

    public void registerAccessibilityButtonCallback(AccessibilityButtonCallback callback, Handler handler) {
        Preconditions.checkNotNull(callback);
        Preconditions.checkNotNull(handler);
        synchronized (this.mLock) {
            if (this.mCallbacks == null) {
                this.mCallbacks = new ArrayMap();
            }
            this.mCallbacks.put(callback, handler);
        }
    }

    /* JADX WARNING: Missing block: B:14:0x001f, code skipped:
            return;
     */
    public void unregisterAccessibilityButtonCallback(android.accessibilityservice.AccessibilityButtonController.AccessibilityButtonCallback r5) {
        /*
        r4 = this;
        com.android.internal.util.Preconditions.checkNotNull(r5);
        r0 = r4.mLock;
        monitor-enter(r0);
        r1 = r4.mCallbacks;	 Catch:{ all -> 0x0020 }
        if (r1 != 0) goto L_0x000c;
    L_0x000a:
        monitor-exit(r0);	 Catch:{ all -> 0x0020 }
        return;
    L_0x000c:
        r1 = r4.mCallbacks;	 Catch:{ all -> 0x0020 }
        r1 = r1.indexOfKey(r5);	 Catch:{ all -> 0x0020 }
        if (r1 < 0) goto L_0x0016;
    L_0x0014:
        r2 = 1;
        goto L_0x0017;
    L_0x0016:
        r2 = 0;
    L_0x0017:
        if (r2 == 0) goto L_0x001e;
    L_0x0019:
        r3 = r4.mCallbacks;	 Catch:{ all -> 0x0020 }
        r3.removeAt(r1);	 Catch:{ all -> 0x0020 }
    L_0x001e:
        monitor-exit(r0);	 Catch:{ all -> 0x0020 }
        return;
    L_0x0020:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0020 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.accessibilityservice.AccessibilityButtonController.unregisterAccessibilityButtonCallback(android.accessibilityservice.AccessibilityButtonController$AccessibilityButtonCallback):void");
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:10:0x0018, code skipped:
            r0 = 0;
            r2 = r1.size();
     */
    /* JADX WARNING: Missing block: B:11:0x001d, code skipped:
            if (r0 >= r2) goto L_0x0036;
     */
    /* JADX WARNING: Missing block: B:12:0x001f, code skipped:
            ((android.os.Handler) r1.valueAt(r0)).post(new android.accessibilityservice.-$$Lambda$AccessibilityButtonController$b_UAM9QJWcH4KQOC_odiN0t_boU(r6, (android.accessibilityservice.AccessibilityButtonController.AccessibilityButtonCallback) r1.keyAt(r0)));
            r0 = r0 + 1;
     */
    /* JADX WARNING: Missing block: B:13:0x0036, code skipped:
            return;
     */
    public void dispatchAccessibilityButtonClicked() {
        /*
        r6 = this;
        r0 = r6.mLock;
        monitor-enter(r0);
        r1 = r6.mCallbacks;	 Catch:{ all -> 0x0040 }
        if (r1 == 0) goto L_0x0037;
    L_0x0007:
        r1 = r6.mCallbacks;	 Catch:{ all -> 0x0040 }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x0040 }
        if (r1 == 0) goto L_0x0010;
    L_0x000f:
        goto L_0x0037;
    L_0x0010:
        r1 = new android.util.ArrayMap;	 Catch:{ all -> 0x0040 }
        r2 = r6.mCallbacks;	 Catch:{ all -> 0x0040 }
        r1.<init>(r2);	 Catch:{ all -> 0x0040 }
        monitor-exit(r0);	 Catch:{ all -> 0x0040 }
        r0 = 0;
        r2 = r1.size();
    L_0x001d:
        if (r0 >= r2) goto L_0x0036;
    L_0x001f:
        r3 = r1.keyAt(r0);
        r3 = (android.accessibilityservice.AccessibilityButtonController.AccessibilityButtonCallback) r3;
        r4 = r1.valueAt(r0);
        r4 = (android.os.Handler) r4;
        r5 = new android.accessibilityservice.-$$Lambda$AccessibilityButtonController$b_UAM9QJWcH4KQOC_odiN0t_boU;
        r5.<init>(r6, r3);
        r4.post(r5);
        r0 = r0 + 1;
        goto L_0x001d;
    L_0x0036:
        return;
    L_0x0037:
        r1 = "A11yButtonController";
        r2 = "Received accessibility button click with no callbacks!";
        android.util.Slog.w(r1, r2);	 Catch:{ all -> 0x0040 }
        monitor-exit(r0);	 Catch:{ all -> 0x0040 }
        return;
    L_0x0040:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0040 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.accessibilityservice.AccessibilityButtonController.dispatchAccessibilityButtonClicked():void");
    }

    public /* synthetic */ void lambda$dispatchAccessibilityButtonClicked$0$AccessibilityButtonController(AccessibilityButtonCallback callback) {
        callback.onClicked(this);
    }

    /* Access modifiers changed, original: 0000 */
    /* JADX WARNING: Missing block: B:10:0x0018, code skipped:
            r0 = 0;
            r2 = r1.size();
     */
    /* JADX WARNING: Missing block: B:11:0x001d, code skipped:
            if (r0 >= r2) goto L_0x0036;
     */
    /* JADX WARNING: Missing block: B:12:0x001f, code skipped:
            ((android.os.Handler) r1.valueAt(r0)).post(new android.accessibilityservice.-$$Lambda$AccessibilityButtonController$RskKrfcSyUz7I9Sqaziy1P990ZM(r6, (android.accessibilityservice.AccessibilityButtonController.AccessibilityButtonCallback) r1.keyAt(r0), r7));
            r0 = r0 + 1;
     */
    /* JADX WARNING: Missing block: B:13:0x0036, code skipped:
            return;
     */
    public void dispatchAccessibilityButtonAvailabilityChanged(boolean r7) {
        /*
        r6 = this;
        r0 = r6.mLock;
        monitor-enter(r0);
        r1 = r6.mCallbacks;	 Catch:{ all -> 0x0040 }
        if (r1 == 0) goto L_0x0037;
    L_0x0007:
        r1 = r6.mCallbacks;	 Catch:{ all -> 0x0040 }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x0040 }
        if (r1 == 0) goto L_0x0010;
    L_0x000f:
        goto L_0x0037;
    L_0x0010:
        r1 = new android.util.ArrayMap;	 Catch:{ all -> 0x0040 }
        r2 = r6.mCallbacks;	 Catch:{ all -> 0x0040 }
        r1.<init>(r2);	 Catch:{ all -> 0x0040 }
        monitor-exit(r0);	 Catch:{ all -> 0x0040 }
        r0 = 0;
        r2 = r1.size();
    L_0x001d:
        if (r0 >= r2) goto L_0x0036;
    L_0x001f:
        r3 = r1.keyAt(r0);
        r3 = (android.accessibilityservice.AccessibilityButtonController.AccessibilityButtonCallback) r3;
        r4 = r1.valueAt(r0);
        r4 = (android.os.Handler) r4;
        r5 = new android.accessibilityservice.-$$Lambda$AccessibilityButtonController$RskKrfcSyUz7I9Sqaziy1P990ZM;
        r5.<init>(r6, r3, r7);
        r4.post(r5);
        r0 = r0 + 1;
        goto L_0x001d;
    L_0x0036:
        return;
    L_0x0037:
        r1 = "A11yButtonController";
        r2 = "Received accessibility button availability change with no callbacks!";
        android.util.Slog.w(r1, r2);	 Catch:{ all -> 0x0040 }
        monitor-exit(r0);	 Catch:{ all -> 0x0040 }
        return;
    L_0x0040:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x0040 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.accessibilityservice.AccessibilityButtonController.dispatchAccessibilityButtonAvailabilityChanged(boolean):void");
    }

    public /* synthetic */ void lambda$dispatchAccessibilityButtonAvailabilityChanged$1$AccessibilityButtonController(AccessibilityButtonCallback callback, boolean available) {
        callback.onAvailabilityChanged(this, available);
    }
}
