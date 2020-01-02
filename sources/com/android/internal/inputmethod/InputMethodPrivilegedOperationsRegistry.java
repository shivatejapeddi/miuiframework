package com.android.internal.inputmethod;

import android.os.IBinder;
import com.android.internal.annotations.GuardedBy;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public final class InputMethodPrivilegedOperationsRegistry {
    private static final Object sLock = new Object();
    private static InputMethodPrivilegedOperations sNop;
    @GuardedBy({"sLock"})
    private static WeakHashMap<IBinder, WeakReference<InputMethodPrivilegedOperations>> sRegistry;

    private InputMethodPrivilegedOperationsRegistry() {
    }

    private static InputMethodPrivilegedOperations getNopOps() {
        if (sNop == null) {
            sNop = new InputMethodPrivilegedOperations();
        }
        return sNop;
    }

    public static void put(IBinder token, InputMethodPrivilegedOperations ops) {
        synchronized (sLock) {
            if (sRegistry == null) {
                sRegistry = new WeakHashMap();
            }
            WeakReference<InputMethodPrivilegedOperations> previousOps = (WeakReference) sRegistry.put(token, new WeakReference(ops));
            if (previousOps == null) {
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(previousOps.get());
                stringBuilder.append(" is already registered for  this token=");
                stringBuilder.append(token);
                stringBuilder.append(" newOps=");
                stringBuilder.append(ops);
                throw new IllegalStateException(stringBuilder.toString());
            }
        }
    }

    public static InputMethodPrivilegedOperations get(IBinder token) {
        synchronized (sLock) {
            if (sRegistry == null) {
                InputMethodPrivilegedOperations nopOps = getNopOps();
                return nopOps;
            }
            WeakReference<InputMethodPrivilegedOperations> wrapperRef = (WeakReference) sRegistry.get(token);
            InputMethodPrivilegedOperations nopOps2;
            if (wrapperRef == null) {
                nopOps2 = getNopOps();
                return nopOps2;
            }
            nopOps2 = (InputMethodPrivilegedOperations) wrapperRef.get();
            if (nopOps2 == null) {
                InputMethodPrivilegedOperations nopOps3 = getNopOps();
                return nopOps3;
            }
            return nopOps2;
        }
    }

    /* JADX WARNING: Missing block: B:11:0x001a, code skipped:
            return;
     */
    public static void remove(android.os.IBinder r2) {
        /*
        r0 = sLock;
        monitor-enter(r0);
        r1 = sRegistry;	 Catch:{ all -> 0x001b }
        if (r1 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        return;
    L_0x0009:
        r1 = sRegistry;	 Catch:{ all -> 0x001b }
        r1.remove(r2);	 Catch:{ all -> 0x001b }
        r1 = sRegistry;	 Catch:{ all -> 0x001b }
        r1 = r1.isEmpty();	 Catch:{ all -> 0x001b }
        if (r1 == 0) goto L_0x0019;
    L_0x0016:
        r1 = 0;
        sRegistry = r1;	 Catch:{ all -> 0x001b }
    L_0x0019:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        return;
    L_0x001b:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.inputmethod.InputMethodPrivilegedOperationsRegistry.remove(android.os.IBinder):void");
    }

    public static boolean isRegistered(IBinder token) {
        synchronized (sLock) {
            if (sRegistry == null) {
                return false;
            }
            boolean containsKey = sRegistry.containsKey(token);
            return containsKey;
        }
    }
}
