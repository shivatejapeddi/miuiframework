package com.android.server.backup;

import android.app.backup.BlobBackupHelper;

public class PreferredActivityBackupHelper extends BlobBackupHelper {
    private static final boolean DEBUG = false;
    private static final String KEY_DEFAULT_APPS = "default-apps";
    private static final String KEY_INTENT_VERIFICATION = "intent-verification";
    private static final String KEY_PREFERRED = "preferred-activity";
    private static final int STATE_VERSION = 3;
    private static final String TAG = "PreferredBackup";

    public PreferredActivityBackupHelper() {
        super(3, KEY_PREFERRED, KEY_DEFAULT_APPS, KEY_INTENT_VERIFICATION);
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0061 A:{Catch:{ Exception -> 0x0066 }} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e A:{Catch:{ Exception -> 0x0066 }} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e A:{Catch:{ Exception -> 0x0066 }} */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0061 A:{Catch:{ Exception -> 0x0066 }} */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0061 A:{Catch:{ Exception -> 0x0066 }} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e A:{Catch:{ Exception -> 0x0066 }} */
    public byte[] getBackupPayload(java.lang.String r9) {
        /*
        r8 = this;
        r0 = "PreferredBackup";
        r1 = android.app.AppGlobals.getPackageManager();
        r2 = -1;
        r3 = r9.hashCode();	 Catch:{ Exception -> 0x0066 }
        r4 = -696985986; // 0xffffffffd674d67e float:-6.7300519E13 double:NaN;
        r5 = 2;
        r6 = 1;
        r7 = 0;
        if (r3 == r4) goto L_0x0033;
    L_0x0013:
        r4 = -429170260; // 0xffffffffe66b61ac float:-2.7788946E23 double:NaN;
        if (r3 == r4) goto L_0x0029;
    L_0x0018:
        r4 = 1336142555; // 0x4fa3eadb float:5.5001554E9 double:6.601421344E-315;
        if (r3 == r4) goto L_0x001e;
    L_0x001d:
        goto L_0x003c;
    L_0x001e:
        r3 = "preferred-activity";
        r3 = r9.equals(r3);	 Catch:{ Exception -> 0x0066 }
        if (r3 == 0) goto L_0x001d;
    L_0x0027:
        r2 = r7;
        goto L_0x003c;
    L_0x0029:
        r3 = "intent-verification";
        r3 = r9.equals(r3);	 Catch:{ Exception -> 0x0066 }
        if (r3 == 0) goto L_0x001d;
    L_0x0031:
        r2 = r5;
        goto L_0x003c;
    L_0x0033:
        r3 = "default-apps";
        r3 = r9.equals(r3);	 Catch:{ Exception -> 0x0066 }
        if (r3 == 0) goto L_0x001d;
    L_0x003b:
        r2 = r6;
    L_0x003c:
        if (r2 == 0) goto L_0x0061;
    L_0x003e:
        if (r2 == r6) goto L_0x005c;
    L_0x0040:
        if (r2 == r5) goto L_0x0057;
    L_0x0042:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0066 }
        r2.<init>();	 Catch:{ Exception -> 0x0066 }
        r3 = "Unexpected backup key ";
        r2.append(r3);	 Catch:{ Exception -> 0x0066 }
        r2.append(r9);	 Catch:{ Exception -> 0x0066 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0066 }
        android.util.Slog.w(r0, r2);	 Catch:{ Exception -> 0x0066 }
        goto L_0x007b;
    L_0x0057:
        r0 = r1.getIntentFilterVerificationBackup(r7);	 Catch:{ Exception -> 0x0066 }
        return r0;
    L_0x005c:
        r0 = r1.getDefaultAppsBackup(r7);	 Catch:{ Exception -> 0x0066 }
        return r0;
    L_0x0061:
        r0 = r1.getPreferredActivityBackup(r7);	 Catch:{ Exception -> 0x0066 }
        return r0;
    L_0x0066:
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Unable to store payload ";
        r3.append(r4);
        r3.append(r9);
        r3 = r3.toString();
        android.util.Slog.e(r0, r3);
    L_0x007b:
        r0 = 0;
        return r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.PreferredActivityBackupHelper.getBackupPayload(java.lang.String):byte[]");
    }

    /* Access modifiers changed, original: protected */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x005f A:{Catch:{ Exception -> 0x0064 }} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e A:{Catch:{ Exception -> 0x0064 }} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e A:{Catch:{ Exception -> 0x0064 }} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x005f A:{Catch:{ Exception -> 0x0064 }} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x005f A:{Catch:{ Exception -> 0x0064 }} */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x003e A:{Catch:{ Exception -> 0x0064 }} */
    public void applyRestoredPayload(java.lang.String r9, byte[] r10) {
        /*
        r8 = this;
        r0 = "PreferredBackup";
        r1 = android.app.AppGlobals.getPackageManager();
        r2 = -1;
        r3 = r9.hashCode();	 Catch:{ Exception -> 0x0064 }
        r4 = -696985986; // 0xffffffffd674d67e float:-6.7300519E13 double:NaN;
        r5 = 2;
        r6 = 1;
        r7 = 0;
        if (r3 == r4) goto L_0x0033;
    L_0x0013:
        r4 = -429170260; // 0xffffffffe66b61ac float:-2.7788946E23 double:NaN;
        if (r3 == r4) goto L_0x0029;
    L_0x0018:
        r4 = 1336142555; // 0x4fa3eadb float:5.5001554E9 double:6.601421344E-315;
        if (r3 == r4) goto L_0x001e;
    L_0x001d:
        goto L_0x003c;
    L_0x001e:
        r3 = "preferred-activity";
        r3 = r9.equals(r3);	 Catch:{ Exception -> 0x0064 }
        if (r3 == 0) goto L_0x001d;
    L_0x0027:
        r2 = r7;
        goto L_0x003c;
    L_0x0029:
        r3 = "intent-verification";
        r3 = r9.equals(r3);	 Catch:{ Exception -> 0x0064 }
        if (r3 == 0) goto L_0x001d;
    L_0x0031:
        r2 = r5;
        goto L_0x003c;
    L_0x0033:
        r3 = "default-apps";
        r3 = r9.equals(r3);	 Catch:{ Exception -> 0x0064 }
        if (r3 == 0) goto L_0x001d;
    L_0x003b:
        r2 = r6;
    L_0x003c:
        if (r2 == 0) goto L_0x005f;
    L_0x003e:
        if (r2 == r6) goto L_0x005b;
    L_0x0040:
        if (r2 == r5) goto L_0x0057;
    L_0x0042:
        r2 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0064 }
        r2.<init>();	 Catch:{ Exception -> 0x0064 }
        r3 = "Unexpected restore key ";
        r2.append(r3);	 Catch:{ Exception -> 0x0064 }
        r2.append(r9);	 Catch:{ Exception -> 0x0064 }
        r2 = r2.toString();	 Catch:{ Exception -> 0x0064 }
        android.util.Slog.w(r0, r2);	 Catch:{ Exception -> 0x0064 }
        goto L_0x0063;
    L_0x0057:
        r1.restoreIntentFilterVerification(r10, r7);	 Catch:{ Exception -> 0x0064 }
        goto L_0x0063;
    L_0x005b:
        r1.restoreDefaultApps(r10, r7);	 Catch:{ Exception -> 0x0064 }
        goto L_0x0063;
    L_0x005f:
        r1.restorePreferredActivities(r10, r7);	 Catch:{ Exception -> 0x0064 }
    L_0x0063:
        goto L_0x0079;
    L_0x0064:
        r2 = move-exception;
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r4 = "Unable to restore key ";
        r3.append(r4);
        r3.append(r9);
        r3 = r3.toString();
        android.util.Slog.w(r0, r3);
    L_0x0079:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.server.backup.PreferredActivityBackupHelper.applyRestoredPayload(java.lang.String, byte[]):void");
    }
}
