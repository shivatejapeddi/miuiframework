package com.android.internal.telephony;

import android.Manifest.permission;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.telephony.Rlog;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.util.StatsLogInternal;
import com.android.internal.annotations.VisibleForTesting;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class TelephonyPermissions {
    private static final boolean DBG = false;
    private static final String LOG_TAG = "TelephonyPermissions";
    private static final String PROPERTY_DEVICE_IDENTIFIER_ACCESS_RESTRICTIONS_DISABLED = "device_identifier_access_restrictions_disabled";
    private static final Supplier<ITelephony> TELEPHONY_SUPPLIER = -$$Lambda$TelephonyPermissions$LxEEC4irBSbjD1lSC4EeVLgFY9I.INSTANCE;
    private static final Map<String, Set<String>> sReportedDeviceIDPackages = new HashMap();

    private TelephonyPermissions() {
    }

    public static boolean checkCallingOrSelfReadPhoneState(Context context, int subId, String callingPackage, String message) {
        return checkReadPhoneState(context, subId, Binder.getCallingPid(), Binder.getCallingUid(), callingPackage, message);
    }

    public static boolean checkCallingOrSelfReadPhoneStateNoThrow(Context context, int subId, String callingPackage, String message) {
        try {
            return checkCallingOrSelfReadPhoneState(context, subId, callingPackage, message);
        } catch (SecurityException e) {
            return false;
        }
    }

    public static boolean checkReadPhoneState(Context context, int subId, int pid, int uid, String callingPackage, String message) {
        return checkReadPhoneState(context, TELEPHONY_SUPPLIER, subId, pid, uid, callingPackage, message);
    }

    public static boolean checkCarrierPrivilegeForSubId(int subId) {
        if (SubscriptionManager.isValidSubscriptionId(subId) && getCarrierPrivilegeStatus(TELEPHONY_SUPPLIER, subId, Binder.getCallingUid()) == 1) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public static boolean checkReadPhoneState(Context context, Supplier<ITelephony> telephonySupplier, int subId, int pid, int uid, String callingPackage, String message) {
        boolean z = true;
        try {
            context.enforcePermission(permission.READ_PRIVILEGED_PHONE_STATE, pid, uid, message);
            return true;
        } catch (SecurityException e) {
            try {
                context.enforcePermission(permission.READ_PHONE_STATE, pid, uid, message);
                if (((AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE)).noteOp(51, uid, callingPackage) != 0) {
                    z = false;
                }
                return z;
            } catch (SecurityException phoneStateException) {
                if (SubscriptionManager.isValidSubscriptionId(subId)) {
                    enforceCarrierPrivilege(telephonySupplier, subId, uid, message);
                    return true;
                }
                throw phoneStateException;
            }
        }
    }

    public static boolean checkReadPhoneStateOnAnyActiveSub(Context context, int pid, int uid, String callingPackage, String message) {
        return checkReadPhoneStateOnAnyActiveSub(context, TELEPHONY_SUPPLIER, pid, uid, callingPackage, message);
    }

    @VisibleForTesting
    public static boolean checkReadPhoneStateOnAnyActiveSub(Context context, Supplier<ITelephony> telephonySupplier, int pid, int uid, String callingPackage, String message) {
        boolean z = true;
        try {
            context.enforcePermission(permission.READ_PRIVILEGED_PHONE_STATE, pid, uid, message);
            return true;
        } catch (SecurityException e) {
            try {
                context.enforcePermission(permission.READ_PHONE_STATE, pid, uid, message);
                if (((AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE)).noteOp(51, uid, callingPackage) != 0) {
                    z = false;
                }
                return z;
            } catch (SecurityException e2) {
                return checkCarrierPrivilegeForAnySubId(context, telephonySupplier, uid);
            }
        }
    }

    public static boolean checkCallingOrSelfReadDeviceIdentifiers(Context context, String callingPackage, String message) {
        return checkCallingOrSelfReadDeviceIdentifiers(context, -1, callingPackage, message);
    }

    public static boolean checkCallingOrSelfReadDeviceIdentifiers(Context context, int subId, String callingPackage, String message) {
        return checkReadDeviceIdentifiers(context, TELEPHONY_SUPPLIER, subId, Binder.getCallingPid(), Binder.getCallingUid(), callingPackage, message);
    }

    public static boolean checkCallingOrSelfReadSubscriberIdentifiers(Context context, int subId, String callingPackage, String message) {
        return checkReadDeviceIdentifiers(context, TELEPHONY_SUPPLIER, subId, Binder.getCallingPid(), Binder.getCallingUid(), callingPackage, message);
    }

    /* JADX WARNING: Missing block: B:24:0x005d, code skipped:
            return true;
     */
    @com.android.internal.annotations.VisibleForTesting
    public static boolean checkReadDeviceIdentifiers(android.content.Context r7, java.util.function.Supplier<com.android.internal.telephony.ITelephony> r8, int r9, int r10, int r11, java.lang.String r12, java.lang.String r13) {
        /*
        r0 = android.os.UserHandle.getAppId(r11);
        r1 = 1;
        r2 = 1000; // 0x3e8 float:1.401E-42 double:4.94E-321;
        if (r0 == r2) goto L_0x005d;
    L_0x0009:
        if (r0 != 0) goto L_0x000c;
    L_0x000b:
        goto L_0x005d;
    L_0x000c:
        r2 = "android.permission.READ_PRIVILEGED_PHONE_STATE";
        r2 = r7.checkPermission(r2, r10, r11);
        if (r2 != 0) goto L_0x0015;
    L_0x0014:
        return r1;
    L_0x0015:
        r2 = checkCarrierPrivilegeForAnySubId(r7, r8, r11);
        if (r2 == 0) goto L_0x001c;
    L_0x001b:
        return r1;
    L_0x001c:
        if (r12 == 0) goto L_0x0052;
    L_0x001e:
        r2 = android.os.Binder.clearCallingIdentity();
        r4 = "appops";
        r4 = r7.getSystemService(r4);
        r4 = (android.app.AppOpsManager) r4;
        r5 = "android:read_device_identifiers";
        r5 = r4.noteOpNoThrow(r5, r11, r12);	 Catch:{ all -> 0x004d }
        if (r5 != 0) goto L_0x0037;
        android.os.Binder.restoreCallingIdentity(r2);
        return r1;
    L_0x0037:
        android.os.Binder.restoreCallingIdentity(r2);
        r5 = "device_policy";
        r5 = r7.getSystemService(r5);
        r5 = (android.app.admin.DevicePolicyManager) r5;
        if (r5 == 0) goto L_0x0052;
    L_0x0046:
        r6 = r5.checkDeviceIdentifierAccess(r12, r10, r11);
        if (r6 == 0) goto L_0x0052;
    L_0x004c:
        return r1;
    L_0x004d:
        r1 = move-exception;
        android.os.Binder.restoreCallingIdentity(r2);
        throw r1;
    L_0x0052:
        r1 = r7;
        r2 = r9;
        r3 = r10;
        r4 = r11;
        r5 = r12;
        r6 = r13;
        r1 = reportAccessDeniedToReadIdentifiers(r1, r2, r3, r4, r5, r6);
        return r1;
    L_0x005d:
        return r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.internal.telephony.TelephonyPermissions.checkReadDeviceIdentifiers(android.content.Context, java.util.function.Supplier, int, int, int, java.lang.String, java.lang.String):boolean");
    }

    private static boolean reportAccessDeniedToReadIdentifiers(Context context, int subId, int pid, int uid, String callingPackage, String message) {
        StringBuilder stringBuilder;
        String str = LOG_TAG;
        boolean isPreinstalled = false;
        boolean isPrivApp = false;
        ApplicationInfo callingPackageInfo = null;
        try {
            callingPackageInfo = context.getPackageManager().getApplicationInfoAsUser(callingPackage, 0, UserHandle.getUserId(uid));
            if (callingPackageInfo != null && callingPackageInfo.isSystemApp()) {
                isPreinstalled = true;
                if (callingPackageInfo.isPrivilegedApp()) {
                    isPrivApp = true;
                }
            }
        } catch (NameNotFoundException e) {
            stringBuilder = new StringBuilder();
            stringBuilder.append("Exception caught obtaining package info for package ");
            stringBuilder.append(callingPackage);
            Log.e(str, stringBuilder.toString(), e);
        }
        boolean packageReported = sReportedDeviceIDPackages.containsKey(callingPackage);
        if (!(packageReported && ((Set) sReportedDeviceIDPackages.get(callingPackage)).contains(message))) {
            Set invokedMethods;
            if (packageReported) {
                invokedMethods = (Set) sReportedDeviceIDPackages.get(callingPackage);
            } else {
                invokedMethods = new HashSet();
                sReportedDeviceIDPackages.put(callingPackage, invokedMethods);
            }
            invokedMethods.add(message);
            StatsLogInternal.write(172, callingPackage, message, isPreinstalled, isPrivApp);
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("reportAccessDeniedToReadIdentifiers:");
        stringBuilder.append(callingPackage);
        stringBuilder.append(":");
        stringBuilder.append(message);
        stringBuilder.append(":isPreinstalled=");
        stringBuilder.append(isPreinstalled);
        stringBuilder.append(":isPrivApp=");
        stringBuilder.append(isPrivApp);
        Log.w(str, stringBuilder.toString());
        if (callingPackageInfo != null && callingPackageInfo.targetSdkVersion < 29 && (context.checkPermission(permission.READ_PHONE_STATE, pid, uid) == 0 || checkCarrierPrivilegeForSubId(subId))) {
            return false;
        }
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(message);
        stringBuilder2.append(": The user ");
        stringBuilder2.append(uid);
        stringBuilder2.append(" does not meet the requirements to access device identifiers.");
        throw new SecurityException(stringBuilder2.toString());
    }

    public static boolean checkReadCallLog(Context context, int subId, int pid, int uid, String callingPackage) {
        return checkReadCallLog(context, TELEPHONY_SUPPLIER, subId, pid, uid, callingPackage);
    }

    @VisibleForTesting
    public static boolean checkReadCallLog(Context context, Supplier<ITelephony> telephonySupplier, int subId, int pid, int uid, String callingPackage) {
        boolean z = true;
        if (context.checkPermission(permission.READ_CALL_LOG, pid, uid) == 0) {
            if (((AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE)).noteOp(6, uid, callingPackage) != 0) {
                z = false;
            }
            return z;
        } else if (!SubscriptionManager.isValidSubscriptionId(subId)) {
            return false;
        } else {
            enforceCarrierPrivilege(telephonySupplier, subId, uid, "readCallLog");
            return true;
        }
    }

    public static boolean checkCallingOrSelfReadPhoneNumber(Context context, int subId, String callingPackage, String message) {
        return checkReadPhoneNumber(context, TELEPHONY_SUPPLIER, subId, Binder.getCallingPid(), Binder.getCallingUid(), callingPackage, message);
    }

    @VisibleForTesting
    public static boolean checkReadPhoneNumber(Context context, Supplier<ITelephony> telephonySupplier, int subId, int pid, int uid, String callingPackage, String message) {
        String str = permission.READ_PHONE_NUMBERS;
        String str2 = permission.READ_SMS;
        AppOpsManager appOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        if (appOps.noteOp(15, uid, callingPackage) == 0) {
            return true;
        }
        try {
            str = checkReadPhoneState(context, telephonySupplier, subId, pid, uid, callingPackage, message);
            return str;
        } catch (SecurityException e) {
            boolean z = false;
            int opCode;
            try {
                context.enforcePermission(str2, pid, uid, message);
                opCode = AppOpsManager.permissionToOpCode(str2);
                if (opCode == -1) {
                    return true;
                }
                if (appOps.noteOp(opCode, uid, callingPackage) == 0) {
                    z = true;
                }
                return z;
            } catch (SecurityException e2) {
                try {
                    context.enforcePermission(str, pid, uid, message);
                    opCode = AppOpsManager.permissionToOpCode(str);
                    if (opCode == -1) {
                        return true;
                    }
                    if (appOps.noteOp(opCode, uid, callingPackage) == 0) {
                        z = true;
                    }
                    return z;
                } catch (SecurityException e3) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(message);
                    stringBuilder.append(": Neither user ");
                    stringBuilder.append(uid);
                    stringBuilder.append(" nor current process has ");
                    stringBuilder.append(permission.READ_PHONE_STATE);
                    stringBuilder.append(", ");
                    stringBuilder.append(str2);
                    stringBuilder.append(", or ");
                    stringBuilder.append(str);
                    throw new SecurityException(stringBuilder.toString());
                }
            }
        }
    }

    public static void enforceCallingOrSelfModifyPermissionOrCarrierPrivilege(Context context, int subId, String message) {
        if (context.checkCallingOrSelfPermission(permission.MODIFY_PHONE_STATE) != 0) {
            enforceCallingOrSelfCarrierPrivilege(subId, message);
        }
    }

    public static void enforeceCallingOrSelfReadPhoneStatePermissionOrCarrierPrivilege(Context context, int subId, String message) {
        if (context.checkCallingOrSelfPermission(permission.READ_PHONE_STATE) != 0) {
            enforceCallingOrSelfCarrierPrivilege(subId, message);
        }
    }

    public static void enforeceCallingOrSelfReadPrivilegedPhoneStatePermissionOrCarrierPrivilege(Context context, int subId, String message) {
        if (context.checkCallingOrSelfPermission(permission.READ_PRIVILEGED_PHONE_STATE) != 0) {
            enforceCallingOrSelfCarrierPrivilege(subId, message);
        }
    }

    public static void enforceCallingOrSelfCarrierPrivilege(int subId, String message) {
        enforceCarrierPrivilege(subId, Binder.getCallingUid(), message);
    }

    private static void enforceCarrierPrivilege(int subId, int uid, String message) {
        enforceCarrierPrivilege(TELEPHONY_SUPPLIER, subId, uid, message);
    }

    private static void enforceCarrierPrivilege(Supplier<ITelephony> telephonySupplier, int subId, int uid, String message) {
        if (getCarrierPrivilegeStatus(telephonySupplier, subId, uid) != 1) {
            throw new SecurityException(message);
        }
    }

    private static boolean checkCarrierPrivilegeForAnySubId(Context context, Supplier<ITelephony> telephonySupplier, int uid) {
        int[] activeSubIds = ((SubscriptionManager) context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE)).getActiveSubscriptionIdList();
        if (activeSubIds != null) {
            for (int activeSubId : activeSubIds) {
                if (getCarrierPrivilegeStatus(telephonySupplier, activeSubId, uid) == 1) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int getCarrierPrivilegeStatus(Supplier<ITelephony> telephonySupplier, int subId, int uid) {
        ITelephony telephony = (ITelephony) telephonySupplier.get();
        if (telephony != null) {
            try {
                return telephony.getCarrierPrivilegeStatusForUid(subId, uid);
            } catch (RemoteException e) {
            }
        }
        Rlog.e(LOG_TAG, "Phone process is down, cannot check carrier privileges");
        return 0;
    }

    public static void enforceShellOnly(int callingUid, String message) {
        if (callingUid != 2000 && callingUid != 0) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(message);
            stringBuilder.append(": Only shell user can call it");
            throw new SecurityException(stringBuilder.toString());
        }
    }
}
