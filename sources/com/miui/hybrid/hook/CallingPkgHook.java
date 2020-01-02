package com.miui.hybrid.hook;

import android.telecom.Logging.Session;
import android.text.TextUtils;
import android.util.Slog;
import java.util.HashMap;
import java.util.Map;

public class CallingPkgHook {
    public static final String TAG = "CallingPkgHook";
    private Map<String, Map<String, String>> mHookData = new HashMap();

    private static class Holder {
        static CallingPkgHook INSTANCE = new CallingPkgHook();

        private Holder() {
        }
    }

    public static CallingPkgHook getInstance() {
        return Holder.INSTANCE;
    }

    public String getHookCallingPkg(String hostApp, String originCallingPkg) {
        if (hostApp != null) {
            synchronized (this.mHookData) {
                Map<String, String> pkgNameMap = (Map) this.mHookData.get(hostApp);
                if (pkgNameMap == null || originCallingPkg == null || !pkgNameMap.containsKey(originCallingPkg)) {
                } else {
                    String hookCallingPkg = (String) pkgNameMap.get(originCallingPkg);
                    String str = TAG;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("MIUILOG-HybridPlatform hook getCallingPkg, ");
                    stringBuilder.append(originCallingPkg);
                    stringBuilder.append(Session.SUBSESSION_SEPARATION_CHAR);
                    stringBuilder.append(hookCallingPkg);
                    stringBuilder.append(", hostApp:");
                    stringBuilder.append(hostApp);
                    Slog.v(str, stringBuilder.toString());
                    return hookCallingPkg;
                }
            }
        }
        return originCallingPkg;
    }

    public boolean add(String hostApp, String originCallingPkg, String hookCallingPkg) {
        StringBuilder stringBuilder;
        if (TextUtils.isEmpty(hostApp)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Fail to add rule, invalid hostApp:");
            stringBuilder.append(hostApp);
            Slog.e(TAG, stringBuilder.toString());
            return false;
        } else if (TextUtils.isEmpty(originCallingPkg)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Fail to add rule, invalid originCallingPkg:");
            stringBuilder.append(originCallingPkg);
            Slog.e(TAG, stringBuilder.toString());
            return false;
        } else if (TextUtils.isEmpty(hookCallingPkg)) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Fail to add rule, invalid hookCallingPkg:");
            stringBuilder.append(hookCallingPkg);
            Slog.e(TAG, stringBuilder.toString());
            return false;
        } else {
            synchronized (this.mHookData) {
                Map<String, String> pkgNameMap = (Map) this.mHookData.get(hostApp);
                if (pkgNameMap == null) {
                    pkgNameMap = new HashMap();
                    this.mHookData.put(hostApp, pkgNameMap);
                }
                pkgNameMap.put(originCallingPkg, hookCallingPkg);
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append("Add hook callingPkg success, hostApp:");
            stringBuilder.append(hostApp);
            stringBuilder.append(" originCallingPkg:");
            stringBuilder.append(originCallingPkg);
            stringBuilder.append(" hookCallingPkg:");
            stringBuilder.append(hookCallingPkg);
            Slog.v(TAG, stringBuilder.toString());
            return true;
        }
    }

    /* JADX WARNING: Missing block: B:20:0x008d, code skipped:
            if (r3 != null) goto L_0x00ae;
     */
    /* JADX WARNING: Missing block: B:21:0x008f, code skipped:
            r0 = new java.lang.StringBuilder();
            r0.append("Fail to remove rule, hookCallingPkg not found by originCallingPkg:");
            r0.append(r9);
            r0.append(", hostApp:");
            r0.append(r8);
            android.util.Slog.v(TAG, r0.toString());
     */
    /* JADX WARNING: Missing block: B:22:0x00ad, code skipped:
            return false;
     */
    /* JADX WARNING: Missing block: B:23:0x00ae, code skipped:
            r0 = new java.lang.StringBuilder();
            r0.append("Remove rule success, hostApp:");
            r0.append(r8);
            r0.append(", originCallingPkg:");
            r0.append(r9);
            r0.append(", hookCallingPkg:");
            r0.append(r3);
            android.util.Slog.v(TAG, r0.toString());
     */
    /* JADX WARNING: Missing block: B:24:0x00d5, code skipped:
            return true;
     */
    public boolean remove(java.lang.String r8, java.lang.String r9) {
        /*
        r7 = this;
        r0 = android.text.TextUtils.isEmpty(r8);
        r1 = 0;
        if (r0 == 0) goto L_0x001e;
    L_0x0007:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r2 = "Fail to remove rule, invalid hostApp:";
        r0.append(r2);
        r0.append(r8);
        r0 = r0.toString();
        r2 = "CallingPkgHook";
        android.util.Slog.e(r2, r0);
        return r1;
    L_0x001e:
        r0 = android.text.TextUtils.isEmpty(r9);
        if (r0 == 0) goto L_0x003b;
    L_0x0024:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r2 = "Fail to remove rule, invalid originCallingPkg:";
        r0.append(r2);
        r0.append(r9);
        r0 = r0.toString();
        r2 = "CallingPkgHook";
        android.util.Slog.e(r2, r0);
        return r1;
    L_0x003b:
        r0 = r7.mHookData;
        monitor-enter(r0);
        r2 = r7.mHookData;	 Catch:{ all -> 0x00d6 }
        r2 = r2.get(r8);	 Catch:{ all -> 0x00d6 }
        r2 = (java.util.Map) r2;	 Catch:{ all -> 0x00d6 }
        if (r2 != 0) goto L_0x0060;
    L_0x0048:
        r3 = "CallingPkgHook";
        r4 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d6 }
        r4.<init>();	 Catch:{ all -> 0x00d6 }
        r5 = "Fail to remove rule, pkgNameMap not found by hostApp:";
        r4.append(r5);	 Catch:{ all -> 0x00d6 }
        r4.append(r8);	 Catch:{ all -> 0x00d6 }
        r4 = r4.toString();	 Catch:{ all -> 0x00d6 }
        android.util.Slog.v(r3, r4);	 Catch:{ all -> 0x00d6 }
        monitor-exit(r0);	 Catch:{ all -> 0x00d6 }
        return r1;
    L_0x0060:
        r3 = r2.remove(r9);	 Catch:{ all -> 0x00d6 }
        r3 = (java.lang.String) r3;	 Catch:{ all -> 0x00d6 }
        r4 = r2.isEmpty();	 Catch:{ all -> 0x00d6 }
        if (r4 == 0) goto L_0x008c;
    L_0x006c:
        r4 = "CallingPkgHook";
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00d6 }
        r5.<init>();	 Catch:{ all -> 0x00d6 }
        r6 = "All rule for hostApp:";
        r5.append(r6);	 Catch:{ all -> 0x00d6 }
        r5.append(r8);	 Catch:{ all -> 0x00d6 }
        r6 = " removed.";
        r5.append(r6);	 Catch:{ all -> 0x00d6 }
        r5 = r5.toString();	 Catch:{ all -> 0x00d6 }
        android.util.Slog.v(r4, r5);	 Catch:{ all -> 0x00d6 }
        r4 = r7.mHookData;	 Catch:{ all -> 0x00d6 }
        r4.remove(r8);	 Catch:{ all -> 0x00d6 }
    L_0x008c:
        monitor-exit(r0);	 Catch:{ all -> 0x00d6 }
        if (r3 != 0) goto L_0x00ae;
    L_0x008f:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r2 = "Fail to remove rule, hookCallingPkg not found by originCallingPkg:";
        r0.append(r2);
        r0.append(r9);
        r2 = ", hostApp:";
        r0.append(r2);
        r0.append(r8);
        r0 = r0.toString();
        r2 = "CallingPkgHook";
        android.util.Slog.v(r2, r0);
        return r1;
    L_0x00ae:
        r0 = new java.lang.StringBuilder;
        r0.<init>();
        r1 = "Remove rule success, hostApp:";
        r0.append(r1);
        r0.append(r8);
        r1 = ", originCallingPkg:";
        r0.append(r1);
        r0.append(r9);
        r1 = ", hookCallingPkg:";
        r0.append(r1);
        r0.append(r3);
        r0 = r0.toString();
        r1 = "CallingPkgHook";
        android.util.Slog.v(r1, r0);
        r0 = 1;
        return r0;
    L_0x00d6:
        r1 = move-exception;
        monitor-exit(r0);	 Catch:{ all -> 0x00d6 }
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.hybrid.hook.CallingPkgHook.remove(java.lang.String, java.lang.String):boolean");
    }
}
