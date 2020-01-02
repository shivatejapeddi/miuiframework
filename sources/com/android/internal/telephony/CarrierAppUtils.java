package com.android.internal.telephony;

import android.content.ContentResolver;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.os.RemoteException;
import android.telephony.TelephonyManager;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.Slog;
import com.android.server.SystemConfig;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CarrierAppUtils {
    private static final boolean DEBUG = false;
    private static final String TAG = "CarrierAppUtils";

    private CarrierAppUtils() {
    }

    public static synchronized void disableCarrierAppsUntilPrivileged(String callingPackage, IPackageManager packageManager, TelephonyManager telephonyManager, ContentResolver contentResolver, int userId) {
        synchronized (CarrierAppUtils.class) {
            SystemConfig config = SystemConfig.getInstance();
            disableCarrierAppsUntilPrivileged(callingPackage, packageManager, telephonyManager, contentResolver, userId, config.getDisabledUntilUsedPreinstalledCarrierApps(), config.getDisabledUntilUsedPreinstalledCarrierAssociatedApps());
        }
    }

    public static synchronized void disableCarrierAppsUntilPrivileged(String callingPackage, IPackageManager packageManager, ContentResolver contentResolver, int userId) {
        synchronized (CarrierAppUtils.class) {
            SystemConfig config = SystemConfig.getInstance();
            disableCarrierAppsUntilPrivileged(callingPackage, packageManager, null, contentResolver, userId, config.getDisabledUntilUsedPreinstalledCarrierApps(), config.getDisabledUntilUsedPreinstalledCarrierAssociatedApps());
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:93:0x01d9 A:{Catch:{ RemoteException -> 0x0233 }} */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x007d A:{SYNTHETIC, Splitter:B:31:0x007d} */
    /* JADX WARNING: Removed duplicated region for block: B:78:0x018d A:{Catch:{ RemoteException -> 0x0233 }} */
    /* JADX WARNING: Removed duplicated region for block: B:39:0x00a7 A:{SYNTHETIC, Splitter:B:39:0x00a7} */
    @com.android.internal.annotations.VisibleForTesting
    public static void disableCarrierAppsUntilPrivileged(java.lang.String r29, android.content.pm.IPackageManager r30, android.telephony.TelephonyManager r31, android.content.ContentResolver r32, int r33, android.util.ArraySet<java.lang.String> r34, android.util.ArrayMap<java.lang.String, java.util.List<java.lang.String>> r35) {
        /*
        r7 = r30;
        r8 = r31;
        r9 = r32;
        r10 = r33;
        r11 = "CarrierAppUtils";
        r12 = r34;
        r13 = getDefaultCarrierAppCandidatesHelper(r7, r10, r12);
        if (r13 == 0) goto L_0x023f;
    L_0x0012:
        r0 = r13.isEmpty();
        if (r0 == 0) goto L_0x001c;
    L_0x0018:
        r21 = r13;
        goto L_0x0241;
    L_0x001c:
        r14 = r35;
        r15 = getDefaultCarrierAssociatedAppsHelper(r7, r10, r14);
        r0 = new java.util.ArrayList;
        r0.<init>();
        r6 = r0;
        r0 = "carrier_apps_handled";
        r5 = 0;
        r1 = android.provider.Settings.Secure.getIntForUser(r9, r0, r5, r10);
        r4 = 1;
        if (r1 != r4) goto L_0x0034;
    L_0x0032:
        r1 = r4;
        goto L_0x0035;
    L_0x0034:
        r1 = r5;
    L_0x0035:
        r16 = r1;
        r17 = r13.iterator();	 Catch:{ RemoteException -> 0x0235 }
    L_0x003b:
        r1 = r17.hasNext();	 Catch:{ RemoteException -> 0x0235 }
        if (r1 == 0) goto L_0x0217;
    L_0x0041:
        r1 = r17.next();	 Catch:{ RemoteException -> 0x0235 }
        r1 = (android.content.pm.ApplicationInfo) r1;	 Catch:{ RemoteException -> 0x0235 }
        r3 = r1;
        r1 = r3.packageName;	 Catch:{ RemoteException -> 0x0235 }
        r2 = r1;
        r1 = android.content.res.Resources.getSystem();	 Catch:{ RemoteException -> 0x0235 }
        r5 = 17236056; // 0x1070058 float:2.479583E-38 double:8.515743E-317;
        r1 = r1.getStringArray(r5);	 Catch:{ RemoteException -> 0x0235 }
        r5 = r1;
        if (r8 == 0) goto L_0x006d;
    L_0x0059:
        r1 = r8.checkCarrierPrivilegesForPackageAnyPhone(r2);	 Catch:{ RemoteException -> 0x0067 }
        if (r1 != r4) goto L_0x006d;
    L_0x005f:
        r1 = com.android.internal.util.ArrayUtils.contains(r5, r2);	 Catch:{ RemoteException -> 0x0067 }
        if (r1 != 0) goto L_0x006d;
    L_0x0065:
        r1 = r4;
        goto L_0x006e;
    L_0x0067:
        r0 = move-exception;
        r21 = r13;
        r13 = r6;
        goto L_0x0239;
    L_0x006d:
        r1 = 0;
    L_0x006e:
        r19 = r1;
        r7.setSystemAppHiddenUntilInstalled(r2, r4);	 Catch:{ RemoteException -> 0x0235 }
        r1 = r15.get(r2);	 Catch:{ RemoteException -> 0x0235 }
        r1 = (java.util.List) r1;	 Catch:{ RemoteException -> 0x0235 }
        r20 = r1;
        if (r20 == 0) goto L_0x009f;
    L_0x007d:
        r1 = r20.iterator();	 Catch:{ RemoteException -> 0x0067 }
    L_0x0081:
        r21 = r1.hasNext();	 Catch:{ RemoteException -> 0x0067 }
        if (r21 == 0) goto L_0x009f;
    L_0x0087:
        r21 = r1.next();	 Catch:{ RemoteException -> 0x0067 }
        r21 = (android.content.pm.ApplicationInfo) r21;	 Catch:{ RemoteException -> 0x0067 }
        r22 = r21;
        r4 = r22;
        r22 = r1;
        r1 = r4.packageName;	 Catch:{ RemoteException -> 0x0067 }
        r23 = r4;
        r4 = 1;
        r7.setSystemAppHiddenUntilInstalled(r1, r4);	 Catch:{ RemoteException -> 0x0067 }
        r1 = r22;
        r4 = 1;
        goto L_0x0081;
    L_0x009f:
        r4 = "Update associated state(";
        r1 = "Update state(";
        r22 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
        if (r19 == 0) goto L_0x018d;
    L_0x00a7:
        r23 = r3.isUpdatedSystemApp();	 Catch:{ RemoteException -> 0x0235 }
        r24 = r4;
        r4 = "): ENABLED for user ";
        if (r23 != 0) goto L_0x010b;
    L_0x00b1:
        r8 = r3.enabledSetting;	 Catch:{ RemoteException -> 0x0235 }
        if (r8 == 0) goto L_0x00d0;
    L_0x00b5:
        r8 = r3.enabledSetting;	 Catch:{ RemoteException -> 0x0067 }
        r25 = r5;
        r5 = 4;
        if (r8 == r5) goto L_0x00d2;
    L_0x00bc:
        r5 = r3.flags;	 Catch:{ RemoteException -> 0x0067 }
        r5 = r5 & r22;
        if (r5 != 0) goto L_0x00c3;
    L_0x00c2:
        goto L_0x00d2;
    L_0x00c3:
        r26 = r2;
        r12 = r3;
        r28 = r4;
        r21 = r13;
        r8 = r24;
        r18 = r25;
        r13 = r6;
        goto L_0x0117;
    L_0x00d0:
        r25 = r5;
    L_0x00d2:
        r5 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0235 }
        r5.<init>();	 Catch:{ RemoteException -> 0x0235 }
        r5.append(r1);	 Catch:{ RemoteException -> 0x0235 }
        r5.append(r2);	 Catch:{ RemoteException -> 0x0235 }
        r5.append(r4);	 Catch:{ RemoteException -> 0x0235 }
        r5.append(r10);	 Catch:{ RemoteException -> 0x0235 }
        r1 = r5.toString();	 Catch:{ RemoteException -> 0x0235 }
        android.util.Slog.i(r11, r1);	 Catch:{ RemoteException -> 0x0235 }
        r5 = 1;
        r7.setSystemAppInstallState(r2, r5, r10);	 Catch:{ RemoteException -> 0x0235 }
        r8 = 1;
        r21 = 1;
        r1 = r30;
        r26 = r2;
        r12 = r3;
        r3 = r8;
        r5 = r4;
        r8 = r24;
        r4 = r21;
        r28 = r5;
        r18 = r25;
        r5 = r33;
        r21 = r13;
        r13 = r6;
        r6 = r29;
        r1.setApplicationEnabledSetting(r2, r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x0233 }
        goto L_0x0117;
    L_0x010b:
        r26 = r2;
        r12 = r3;
        r28 = r4;
        r18 = r5;
        r21 = r13;
        r8 = r24;
        r13 = r6;
    L_0x0117:
        if (r20 == 0) goto L_0x0184;
    L_0x0119:
        r24 = r20.iterator();	 Catch:{ RemoteException -> 0x0233 }
    L_0x011d:
        r1 = r24.hasNext();	 Catch:{ RemoteException -> 0x0233 }
        if (r1 == 0) goto L_0x0182;
    L_0x0123:
        r1 = r24.next();	 Catch:{ RemoteException -> 0x0233 }
        r1 = (android.content.pm.ApplicationInfo) r1;	 Catch:{ RemoteException -> 0x0233 }
        r6 = r1;
        r1 = r6.enabledSetting;	 Catch:{ RemoteException -> 0x0233 }
        if (r1 == 0) goto L_0x0140;
    L_0x012e:
        r1 = r6.enabledSetting;	 Catch:{ RemoteException -> 0x0233 }
        r5 = 4;
        if (r1 == r5) goto L_0x0141;
    L_0x0133:
        r1 = r6.flags;	 Catch:{ RemoteException -> 0x0233 }
        r1 = r1 & r22;
        if (r1 != 0) goto L_0x013a;
    L_0x0139:
        goto L_0x0141;
    L_0x013a:
        r25 = r5;
        r23 = r28;
        r14 = 1;
        goto L_0x017d;
    L_0x0140:
        r5 = 4;
    L_0x0141:
        r1 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0233 }
        r1.<init>();	 Catch:{ RemoteException -> 0x0233 }
        r1.append(r8);	 Catch:{ RemoteException -> 0x0233 }
        r2 = r6.packageName;	 Catch:{ RemoteException -> 0x0233 }
        r1.append(r2);	 Catch:{ RemoteException -> 0x0233 }
        r4 = r28;
        r1.append(r4);	 Catch:{ RemoteException -> 0x0233 }
        r1.append(r10);	 Catch:{ RemoteException -> 0x0233 }
        r1 = r1.toString();	 Catch:{ RemoteException -> 0x0233 }
        android.util.Slog.i(r11, r1);	 Catch:{ RemoteException -> 0x0233 }
        r1 = r6.packageName;	 Catch:{ RemoteException -> 0x0233 }
        r3 = 1;
        r7.setSystemAppInstallState(r1, r3, r10);	 Catch:{ RemoteException -> 0x0233 }
        r2 = r6.packageName;	 Catch:{ RemoteException -> 0x0233 }
        r23 = 1;
        r25 = 1;
        r1 = r30;
        r14 = r3;
        r3 = r23;
        r23 = r4;
        r4 = r25;
        r25 = r5;
        r5 = r33;
        r27 = r6;
        r6 = r29;
        r1.setApplicationEnabledSetting(r2, r3, r4, r5, r6);	 Catch:{ RemoteException -> 0x0233 }
    L_0x017d:
        r14 = r35;
        r28 = r23;
        goto L_0x011d;
    L_0x0182:
        r14 = 1;
        goto L_0x0185;
    L_0x0184:
        r14 = 1;
    L_0x0185:
        r1 = r12.packageName;	 Catch:{ RemoteException -> 0x0233 }
        r13.add(r1);	 Catch:{ RemoteException -> 0x0233 }
        r2 = 0;
        goto L_0x020a;
    L_0x018d:
        r26 = r2;
        r12 = r3;
        r8 = r4;
        r18 = r5;
        r21 = r13;
        r14 = 1;
        r13 = r6;
        r2 = r12.isUpdatedSystemApp();	 Catch:{ RemoteException -> 0x0233 }
        r3 = "): DISABLED_UNTIL_USED for user ";
        if (r2 != 0) goto L_0x01c8;
    L_0x019f:
        r2 = r12.enabledSetting;	 Catch:{ RemoteException -> 0x0233 }
        if (r2 != 0) goto L_0x01c8;
    L_0x01a3:
        r2 = r12.flags;	 Catch:{ RemoteException -> 0x0233 }
        r2 = r2 & r22;
        if (r2 == 0) goto L_0x01c8;
    L_0x01a9:
        r2 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0233 }
        r2.<init>();	 Catch:{ RemoteException -> 0x0233 }
        r2.append(r1);	 Catch:{ RemoteException -> 0x0233 }
        r1 = r26;
        r2.append(r1);	 Catch:{ RemoteException -> 0x0233 }
        r2.append(r3);	 Catch:{ RemoteException -> 0x0233 }
        r2.append(r10);	 Catch:{ RemoteException -> 0x0233 }
        r2 = r2.toString();	 Catch:{ RemoteException -> 0x0233 }
        android.util.Slog.i(r11, r2);	 Catch:{ RemoteException -> 0x0233 }
        r2 = 0;
        r7.setSystemAppInstallState(r1, r2, r10);	 Catch:{ RemoteException -> 0x0233 }
        goto L_0x01cb;
    L_0x01c8:
        r1 = r26;
        r2 = 0;
    L_0x01cb:
        if (r16 != 0) goto L_0x020a;
    L_0x01cd:
        if (r20 == 0) goto L_0x020a;
    L_0x01cf:
        r4 = r20.iterator();	 Catch:{ RemoteException -> 0x0233 }
    L_0x01d3:
        r5 = r4.hasNext();	 Catch:{ RemoteException -> 0x0233 }
        if (r5 == 0) goto L_0x020a;
    L_0x01d9:
        r5 = r4.next();	 Catch:{ RemoteException -> 0x0233 }
        r5 = (android.content.pm.ApplicationInfo) r5;	 Catch:{ RemoteException -> 0x0233 }
        r6 = r5.enabledSetting;	 Catch:{ RemoteException -> 0x0233 }
        if (r6 != 0) goto L_0x0208;
    L_0x01e3:
        r6 = r5.flags;	 Catch:{ RemoteException -> 0x0233 }
        r6 = r6 & r22;
        if (r6 == 0) goto L_0x0208;
    L_0x01e9:
        r6 = new java.lang.StringBuilder;	 Catch:{ RemoteException -> 0x0233 }
        r6.<init>();	 Catch:{ RemoteException -> 0x0233 }
        r6.append(r8);	 Catch:{ RemoteException -> 0x0233 }
        r14 = r5.packageName;	 Catch:{ RemoteException -> 0x0233 }
        r6.append(r14);	 Catch:{ RemoteException -> 0x0233 }
        r6.append(r3);	 Catch:{ RemoteException -> 0x0233 }
        r6.append(r10);	 Catch:{ RemoteException -> 0x0233 }
        r6 = r6.toString();	 Catch:{ RemoteException -> 0x0233 }
        android.util.Slog.i(r11, r6);	 Catch:{ RemoteException -> 0x0233 }
        r6 = r5.packageName;	 Catch:{ RemoteException -> 0x0233 }
        r7.setSystemAppInstallState(r6, r2, r10);	 Catch:{ RemoteException -> 0x0233 }
    L_0x0208:
        r14 = 1;
        goto L_0x01d3;
    L_0x020a:
        r8 = r31;
        r12 = r34;
        r14 = r35;
        r5 = r2;
        r6 = r13;
        r13 = r21;
        r4 = 1;
        goto L_0x003b;
    L_0x0217:
        r21 = r13;
        r13 = r6;
        if (r16 != 0) goto L_0x0220;
    L_0x021c:
        r1 = 1;
        android.provider.Settings.Secure.putIntForUser(r9, r0, r1, r10);	 Catch:{ RemoteException -> 0x0233 }
    L_0x0220:
        r0 = r13.isEmpty();	 Catch:{ RemoteException -> 0x0233 }
        if (r0 != 0) goto L_0x0232;
    L_0x0226:
        r0 = r13.size();	 Catch:{ RemoteException -> 0x0233 }
        r0 = new java.lang.String[r0];	 Catch:{ RemoteException -> 0x0233 }
        r13.toArray(r0);	 Catch:{ RemoteException -> 0x0233 }
        r7.grantDefaultPermissionsToEnabledCarrierApps(r0, r10);	 Catch:{ RemoteException -> 0x0233 }
    L_0x0232:
        goto L_0x023e;
    L_0x0233:
        r0 = move-exception;
        goto L_0x0239;
    L_0x0235:
        r0 = move-exception;
        r21 = r13;
        r13 = r6;
    L_0x0239:
        r1 = "Could not reach PackageManager";
        android.util.Slog.w(r11, r1, r0);
    L_0x023e:
        return;
    L_0x023f:
        r21 = r13;
    L_0x0241:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.CarrierAppUtils.disableCarrierAppsUntilPrivileged(java.lang.String, android.content.pm.IPackageManager, android.telephony.TelephonyManager, android.content.ContentResolver, int, android.util.ArraySet, android.util.ArrayMap):void");
    }

    public static List<ApplicationInfo> getDefaultCarrierApps(IPackageManager packageManager, TelephonyManager telephonyManager, int userId) {
        List<ApplicationInfo> candidates = getDefaultCarrierAppCandidates(packageManager, userId);
        if (candidates == null || candidates.isEmpty()) {
            return null;
        }
        for (int i = candidates.size() - 1; i >= 0; i--) {
            if (!(telephonyManager.checkCarrierPrivilegesForPackageAnyPhone(((ApplicationInfo) candidates.get(i)).packageName) == 1)) {
                candidates.remove(i);
            }
        }
        return candidates;
    }

    public static List<ApplicationInfo> getDefaultCarrierAppCandidates(IPackageManager packageManager, int userId) {
        return getDefaultCarrierAppCandidatesHelper(packageManager, userId, SystemConfig.getInstance().getDisabledUntilUsedPreinstalledCarrierApps());
    }

    private static List<ApplicationInfo> getDefaultCarrierAppCandidatesHelper(IPackageManager packageManager, int userId, ArraySet<String> systemCarrierAppsDisabledUntilUsed) {
        if (systemCarrierAppsDisabledUntilUsed == null) {
            return null;
        }
        int size = systemCarrierAppsDisabledUntilUsed.size();
        if (size == 0) {
            return null;
        }
        List<ApplicationInfo> apps = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            ApplicationInfo ai = getApplicationInfoIfSystemApp(packageManager, userId, (String) systemCarrierAppsDisabledUntilUsed.valueAt(i));
            if (ai != null) {
                apps.add(ai);
            }
        }
        return apps;
    }

    private static Map<String, List<ApplicationInfo>> getDefaultCarrierAssociatedAppsHelper(IPackageManager packageManager, int userId, ArrayMap<String, List<String>> systemCarrierAssociatedAppsDisabledUntilUsed) {
        int size = systemCarrierAssociatedAppsDisabledUntilUsed.size();
        Map<String, List<ApplicationInfo>> associatedApps = new ArrayMap(size);
        for (int i = 0; i < size; i++) {
            String carrierAppPackage = (String) systemCarrierAssociatedAppsDisabledUntilUsed.keyAt(i);
            List<String> associatedAppPackages = (List) systemCarrierAssociatedAppsDisabledUntilUsed.valueAt(i);
            for (int j = 0; j < associatedAppPackages.size(); j++) {
                ApplicationInfo ai = getApplicationInfoIfSystemApp(packageManager, userId, (String) associatedAppPackages.get(j));
                if (!(ai == null || ai.isUpdatedSystemApp())) {
                    List<ApplicationInfo> appList = (List) associatedApps.get(carrierAppPackage);
                    if (appList == null) {
                        appList = new ArrayList();
                        associatedApps.put(carrierAppPackage, appList);
                    }
                    appList.add(ai);
                }
            }
        }
        return associatedApps;
    }

    private static ApplicationInfo getApplicationInfoIfSystemApp(IPackageManager packageManager, int userId, String packageName) {
        try {
            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 536903680, userId);
            if (ai != null && ai.isSystemApp()) {
                return ai;
            }
        } catch (RemoteException e) {
            Slog.w(TAG, "Could not reach PackageManager", e);
        }
        return null;
    }
}
