package com.android.internal.os;

import android.os.IBinder;
import android.os.IBinder.DeathRecipient;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.ArraySet;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.annotations.VisibleForTesting;
import java.io.PrintWriter;

public class BinderDeathDispatcher<T extends IInterface> {
    private static final String TAG = "BinderDeathDispatcher";
    private final Object mLock = new Object();
    @GuardedBy({"mLock"})
    private final ArrayMap<IBinder, RecipientsInfo> mTargets = new ArrayMap();

    @VisibleForTesting
    class RecipientsInfo implements DeathRecipient {
        @GuardedBy({"mLock"})
        ArraySet<DeathRecipient> mRecipients;
        final IBinder mTarget;

        private RecipientsInfo(IBinder target) {
            this.mRecipients = new ArraySet();
            this.mTarget = target;
        }

        public void binderDied() {
            ArraySet<DeathRecipient> copy;
            synchronized (BinderDeathDispatcher.this.mLock) {
                copy = this.mRecipients;
                this.mRecipients = null;
                BinderDeathDispatcher.this.mTargets.remove(this.mTarget);
            }
            if (copy != null) {
                int size = copy.size();
                for (int i = 0; i < size; i++) {
                    ((DeathRecipient) copy.valueAt(i)).binderDied();
                }
            }
        }
    }

    public int linkToDeath(T target, DeathRecipient recipient) {
        int size;
        IBinder ib = target.asBinder();
        synchronized (this.mLock) {
            RecipientsInfo info = (RecipientsInfo) this.mTargets.get(ib);
            if (info == null) {
                info = new RecipientsInfo(ib);
                try {
                    ib.linkToDeath(info, 0);
                    this.mTargets.put(ib, info);
                } catch (RemoteException e) {
                    return -1;
                }
            }
            info.mRecipients.add(recipient);
            size = info.mRecipients.size();
        }
        return size;
    }

    /* JADX WARNING: Missing block: B:13:0x0031, code skipped:
            return;
     */
    public void unlinkToDeath(T r6, android.os.IBinder.DeathRecipient r7) {
        /*
        r5 = this;
        r0 = r6.asBinder();
        r1 = r5.mLock;
        monitor-enter(r1);
        r2 = r5.mTargets;	 Catch:{ all -> 0x0032 }
        r2 = r2.get(r0);	 Catch:{ all -> 0x0032 }
        r2 = (com.android.internal.os.BinderDeathDispatcher.RecipientsInfo) r2;	 Catch:{ all -> 0x0032 }
        if (r2 != 0) goto L_0x0013;
    L_0x0011:
        monitor-exit(r1);	 Catch:{ all -> 0x0032 }
        return;
    L_0x0013:
        r3 = r2.mRecipients;	 Catch:{ all -> 0x0032 }
        r3 = r3.remove(r7);	 Catch:{ all -> 0x0032 }
        if (r3 == 0) goto L_0x0030;
    L_0x001b:
        r3 = r2.mRecipients;	 Catch:{ all -> 0x0032 }
        r3 = r3.size();	 Catch:{ all -> 0x0032 }
        if (r3 != 0) goto L_0x0030;
    L_0x0023:
        r3 = r2.mTarget;	 Catch:{ all -> 0x0032 }
        r4 = 0;
        r3.unlinkToDeath(r2, r4);	 Catch:{ all -> 0x0032 }
        r3 = r5.mTargets;	 Catch:{ all -> 0x0032 }
        r4 = r2.mTarget;	 Catch:{ all -> 0x0032 }
        r3.remove(r4);	 Catch:{ all -> 0x0032 }
    L_0x0030:
        monitor-exit(r1);	 Catch:{ all -> 0x0032 }
        return;
    L_0x0032:
        r2 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x0032 }
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.os.BinderDeathDispatcher.unlinkToDeath(android.os.IInterface, android.os.IBinder$DeathRecipient):void");
    }

    public void dump(PrintWriter pw, String indent) {
        synchronized (this.mLock) {
            pw.print(indent);
            pw.print("# of watched binders: ");
            pw.println(this.mTargets.size());
            pw.print(indent);
            pw.print("# of death recipients: ");
            int n = 0;
            for (RecipientsInfo info : this.mTargets.values()) {
                n += info.mRecipients.size();
            }
            pw.println(n);
        }
    }

    @VisibleForTesting
    public ArrayMap<IBinder, RecipientsInfo> getTargetsForTest() {
        return this.mTargets;
    }
}
