package com.android.internal.infra;

import android.content.ComponentName;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import java.io.PrintWriter;
import java.util.List;

public class GlobalWhitelistState {
    protected final Object mGlobalWhitelistStateLock = new Object();
    @GuardedBy({"mGlobalWhitelistStateLock"})
    protected SparseArray<WhitelistHelper> mWhitelisterHelpers;

    public void setWhitelist(int userId, List<String> packageNames, List<ComponentName> components) {
        synchronized (this.mGlobalWhitelistStateLock) {
            if (this.mWhitelisterHelpers == null) {
                this.mWhitelisterHelpers = new SparseArray(1);
            }
            WhitelistHelper helper = (WhitelistHelper) this.mWhitelisterHelpers.get(userId);
            if (helper == null) {
                helper = new WhitelistHelper();
                this.mWhitelisterHelpers.put(userId, helper);
            }
            helper.setWhitelist((List) packageNames, (List) components);
        }
    }

    /* JADX WARNING: Missing block: B:12:0x001a, code skipped:
            return r2;
     */
    public boolean isWhitelisted(int r4, java.lang.String r5) {
        /*
        r3 = this;
        r0 = r3.mGlobalWhitelistStateLock;
        monitor-enter(r0);
        r1 = r3.mWhitelisterHelpers;	 Catch:{ all -> 0x001b }
        r2 = 0;
        if (r1 != 0) goto L_0x000a;
    L_0x0008:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        return r2;
    L_0x000a:
        r1 = r3.mWhitelisterHelpers;	 Catch:{ all -> 0x001b }
        r1 = r1.get(r4);	 Catch:{ all -> 0x001b }
        r1 = (com.android.internal.infra.WhitelistHelper) r1;	 Catch:{ all -> 0x001b }
        if (r1 != 0) goto L_0x0015;
    L_0x0014:
        goto L_0x0019;
    L_0x0015:
        r2 = r1.isWhitelisted(r5);	 Catch:{ all -> 0x001b }
    L_0x0019:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        return r2;
    L_0x001b:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.infra.GlobalWhitelistState.isWhitelisted(int, java.lang.String):boolean");
    }

    /* JADX WARNING: Missing block: B:12:0x001a, code skipped:
            return r2;
     */
    public boolean isWhitelisted(int r4, android.content.ComponentName r5) {
        /*
        r3 = this;
        r0 = r3.mGlobalWhitelistStateLock;
        monitor-enter(r0);
        r1 = r3.mWhitelisterHelpers;	 Catch:{ all -> 0x001b }
        r2 = 0;
        if (r1 != 0) goto L_0x000a;
    L_0x0008:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        return r2;
    L_0x000a:
        r1 = r3.mWhitelisterHelpers;	 Catch:{ all -> 0x001b }
        r1 = r1.get(r4);	 Catch:{ all -> 0x001b }
        r1 = (com.android.internal.infra.WhitelistHelper) r1;	 Catch:{ all -> 0x001b }
        if (r1 != 0) goto L_0x0015;
    L_0x0014:
        goto L_0x0019;
    L_0x0015:
        r2 = r1.isWhitelisted(r5);	 Catch:{ all -> 0x001b }
    L_0x0019:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        return r2;
    L_0x001b:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.infra.GlobalWhitelistState.isWhitelisted(int, android.content.ComponentName):boolean");
    }

    /* JADX WARNING: Missing block: B:12:0x001a, code skipped:
            return r2;
     */
    public android.util.ArraySet<android.content.ComponentName> getWhitelistedComponents(int r4, java.lang.String r5) {
        /*
        r3 = this;
        r0 = r3.mGlobalWhitelistStateLock;
        monitor-enter(r0);
        r1 = r3.mWhitelisterHelpers;	 Catch:{ all -> 0x001b }
        r2 = 0;
        if (r1 != 0) goto L_0x000a;
    L_0x0008:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        return r2;
    L_0x000a:
        r1 = r3.mWhitelisterHelpers;	 Catch:{ all -> 0x001b }
        r1 = r1.get(r4);	 Catch:{ all -> 0x001b }
        r1 = (com.android.internal.infra.WhitelistHelper) r1;	 Catch:{ all -> 0x001b }
        if (r1 != 0) goto L_0x0015;
    L_0x0014:
        goto L_0x0019;
    L_0x0015:
        r2 = r1.getWhitelistedComponents(r5);	 Catch:{ all -> 0x001b }
    L_0x0019:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        return r2;
    L_0x001b:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.infra.GlobalWhitelistState.getWhitelistedComponents(int, java.lang.String):android.util.ArraySet");
    }

    /* JADX WARNING: Missing block: B:11:0x001a, code skipped:
            return;
     */
    public void resetWhitelist(int r3) {
        /*
        r2 = this;
        r0 = r2.mGlobalWhitelistStateLock;
        monitor-enter(r0);
        r1 = r2.mWhitelisterHelpers;	 Catch:{ all -> 0x001b }
        if (r1 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        return;
    L_0x0009:
        r1 = r2.mWhitelisterHelpers;	 Catch:{ all -> 0x001b }
        r1.remove(r3);	 Catch:{ all -> 0x001b }
        r1 = r2.mWhitelisterHelpers;	 Catch:{ all -> 0x001b }
        r1 = r1.size();	 Catch:{ all -> 0x001b }
        if (r1 != 0) goto L_0x0019;
    L_0x0016:
        r1 = 0;
        r2.mWhitelisterHelpers = r1;	 Catch:{ all -> 0x001b }
    L_0x0019:
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        return;
    L_0x001b:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x001b }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.infra.GlobalWhitelistState.resetWhitelist(int):void");
    }

    public void dump(String prefix, PrintWriter pw) {
        pw.print(prefix);
        pw.print("State: ");
        synchronized (this.mGlobalWhitelistStateLock) {
            if (this.mWhitelisterHelpers == null) {
                pw.println("empty");
                return;
            }
            pw.print(this.mWhitelisterHelpers.size());
            pw.println(" services");
            String prefix2 = new StringBuilder();
            prefix2.append(prefix);
            prefix2.append("  ");
            prefix2 = prefix2.toString();
            for (int i = 0; i < this.mWhitelisterHelpers.size(); i++) {
                int userId = this.mWhitelisterHelpers.keyAt(i);
                WhitelistHelper helper = (WhitelistHelper) this.mWhitelisterHelpers.valueAt(i);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Whitelist for userId ");
                stringBuilder.append(userId);
                helper.dump(prefix2, stringBuilder.toString(), pw);
            }
        }
    }
}
