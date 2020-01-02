package miui.security;

import android.app.Activity;
import android.app.AppGlobals;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ParceledListSlice;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.provider.MiuiSettings;
import android.provider.Settings.Secure;
import android.util.Log;
import android.util.Slog;
import com.android.internal.app.IWakePathCallback;
import com.miui.internal.search.SettingsTree;
import java.lang.reflect.Method;
import java.util.List;
import miui.securityspace.XSpaceUserHandle;
import miui.util.FeatureParser;

public class SecurityManager {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int FLAG_AC_ENABLED = 1;
    public static final int FLAG_AC_PACKAGE_CANCELED = 8;
    public static final int FLAG_AC_PACKAGE_ENABLED = 2;
    public static final int FLAG_AC_PACKAGE_PASSED = 4;
    public static final int MODE_EACH = 0;
    public static final int MODE_LOCK_SCREEN = 1;
    public static final int MODE_TIME_OUT = 2;
    private static final String PACKAGE_SECURITYCENTER = "com.miui.securitycenter";
    public static final String SKIP_INTERCEPT = "skip_interception";
    public static final String SKIP_INTERCEPT_ACTIVITY_GALLERY_EDIT = "com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity";
    public static final String SKIP_INTERCEPT_ACTIVITY_GALLERY_EXTRA = "com.miui.gallery.activity.ExternalPhotoPageActivity";
    public static final String SKIP_INTERCEPT_PACKAGE = "com.miui.gallery";
    private static final String START_ACTIVITY_CALLEE_PKGNAME = "CalleePkgName";
    private static final String START_ACTIVITY_CALLER_PKGNAME = "CallerPkgName";
    private static final String START_ACTIVITY_USERID = "UserId";
    private static final String TAG = "SecurityManager";
    private static Method sActivityFinishMethod;
    private final ISecurityManager mService;

    private static native void hook();

    static {
        if (VERSION.SDK_INT > 19) {
            try {
                String str = "finish";
                if (VERSION.SDK_INT >= 24) {
                    sActivityFinishMethod = Activity.class.getDeclaredMethod(str, new Class[]{Integer.TYPE});
                } else {
                    sActivityFinishMethod = Activity.class.getDeclaredMethod(str, new Class[]{Boolean.TYPE});
                }
                sActivityFinishMethod.setAccessible(true);
            } catch (Exception e) {
                Log.e(TAG, " SecurityManager static init error", e);
            }
        }
        if (VERSION.SDK_INT == 19) {
            System.loadLibrary("sechook");
        }
    }

    public SecurityManager(ISecurityManager service) {
        this.mService = service;
    }

    public static boolean checkCallingPackage(Context context, String[] whiteList) {
        String[] pkgs = context.getPackageManager().getPackagesForUid(Binder.getCallingUid());
        if (pkgs == null) {
            return false;
        }
        for (String pkg : pkgs) {
            for (String validPkg : whiteList) {
                if (pkg.equals(validPkg)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void killNativePackageProcesses(int uid, String pkgName) {
        try {
            this.mService.killNativePackageProcesses(uid, pkgName);
        } catch (RemoteException ex) {
            ex.printStackTrace();
        }
    }

    public String getPackageNameByPid(int pid) {
        try {
            return this.mService.getPackageNameByPid(pid);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public boolean checkSmsBlocked(Intent intent) {
        try {
            return this.mService.checkSmsBlocked(intent);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean startInterceptSmsBySender(Context context, String sender, int count) {
        try {
            return this.mService.startInterceptSmsBySender(context.getPackageName(), sender, count);
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean stopInterceptSmsBySender() {
        try {
            return this.mService.stopInterceptSmsBySender();
        } catch (RemoteException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public void addAccessControlPass(String packageName) {
        try {
            this.mService.addAccessControlPass(packageName);
        } catch (RemoteException e) {
        }
    }

    public void addAccessControlPassForUser(String packageName, int userId) {
        try {
            this.mService.addAccessControlPassForUser(packageName, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("addAccessControlPassForUser exception", e);
        }
    }

    public void removeAccessControlPass(String packageName) {
        try {
            this.mService.removeAccessControlPass(packageName);
        } catch (RemoteException e) {
        }
    }

    public void removeAccessControlPassAsUser(String packageName, int userId) {
        try {
            this.mService.removeAccessControlPassAsUser(packageName, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void setGameBoosterIBinder(IBinder gameBooster, int userId, boolean isGameMode) {
        try {
            this.mService.setGameBoosterIBinder(gameBooster, userId, isGameMode);
        } catch (RemoteException e) {
            throw new RuntimeException("setGameBoosterIBinder exception", e);
        }
    }

    public boolean checkAccessControlPass(String packageName) {
        return checkAccessControlPass(packageName, null);
    }

    public boolean checkAccessControlPass(String packageName, Intent intent) {
        try {
            return this.mService.checkAccessControlPass(packageName, intent);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean checkAccessControlPassAsUser(String packageName, int userId) {
        return checkAccessControlPassAsUser(packageName, null, userId);
    }

    public boolean checkAccessControlPassAsUser(String packageName, Intent intent, int userId) {
        try {
            return this.mService.checkAccessControlPassAsUser(packageName, intent, userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean checkGameBoosterAntimsgPassAsUser(String packageName, Intent intent, int userId) {
        try {
            return this.mService.checkGameBoosterAntimsgPassAsUser(packageName, intent, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("checkGameBoosterAntimsgPassAsUser exception", e);
        }
    }

    public boolean getApplicationAccessControlEnabled(String packageName) {
        try {
            return this.mService.getApplicationAccessControlEnabled(packageName);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean getApplicationAccessControlEnabledAsUser(String packageName, int userId) {
        try {
            return this.mService.getApplicationAccessControlEnabledAsUser(packageName, userId);
        } catch (RemoteException e) {
            return false;
        }
    }

    public boolean getApplicationMaskNotificationEnabledAsUser(String packageName, int userId) {
        try {
            return this.mService.getApplicationMaskNotificationEnabledAsUser(packageName, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("getApplicationMaskNotificationEnabledAsUser exception", e);
        }
    }

    public void setApplicationAccessControlEnabled(String packageName, boolean enabled) {
        try {
            this.mService.setApplicationAccessControlEnabled(packageName, enabled);
        } catch (RemoteException e) {
        }
    }

    public void setApplicationAccessControlEnabledForUser(String packageName, boolean enabled, int userId) {
        try {
            this.mService.setApplicationAccessControlEnabledForUser(packageName, enabled, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("setApplicationAccessControlEnabledForUser exception", e);
        }
    }

    public void setApplicationMaskNotificationEnabledForUser(String packageName, boolean enabled, int userId) {
        try {
            this.mService.setApplicationMaskNotificationEnabledForUser(packageName, enabled, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("setApplicationMaskNotificationEnabledForUser exception", e);
        }
    }

    public void saveIcon(String fileName, Bitmap icon) {
        try {
            this.mService.saveIcon(fileName, icon);
        } catch (RemoteException e) {
            throw new RuntimeException("saveIcon exception", e);
        }
    }

    public void setAccessControlPassword(String passwordType, String password) {
        try {
            this.mService.setAccessControlPassword(passwordType, password, UserHandle.myUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean checkAccessControlPassword(String passwordType, String password) {
        try {
            return this.mService.checkAccessControlPassword(passwordType, password, UserHandle.myUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean haveAccessControlPassword() {
        try {
            return this.mService.haveAccessControlPassword(UserHandle.myUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public String getAccessControlPasswordType() {
        try {
            return this.mService.getAccessControlPasswordType(UserHandle.myUserId());
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean getApplicationChildrenControlEnabled(String packageName) {
        try {
            return this.mService.getApplicationChildrenControlEnabled(packageName);
        } catch (RemoteException e) {
            return false;
        }
    }

    public void setApplicationChildrenControlEnabled(String packageName, boolean enabled) {
        try {
            this.mService.setApplicationChildrenControlEnabled(packageName, enabled);
        } catch (RemoteException e) {
        }
    }

    public boolean checkAllowStartActivity(String callerPkgName, String calleePkgName, Intent intent, int userId) {
        long startTime = SystemClock.elapsedRealtime();
        boolean retval = false;
        try {
            retval = this.mService.checkAllowStartActivity(callerPkgName, calleePkgName, intent, userId);
        } catch (RemoteException e) {
        }
        checkTime(startTime, "checkAllowStartActivity");
        return retval;
    }

    public Intent getCheckIntent(Context context, ApplicationInfo callerAppInfo, String callingPackage, String packageName, Intent intent, boolean fromActivity, int requestCode, boolean calleeAlreadyStarted, int userId, int callingUid, Bundle bOptions) {
        Context context2 = context;
        Intent ret = AppRunningControlManager.getBlockActivityIntent(context, packageName, intent, fromActivity, requestCode);
        if (ret != null) {
            return ret;
        }
        Intent ret2 = getCheckStartActivityIntent(context, callerAppInfo, callingPackage, packageName, intent, fromActivity, requestCode, calleeAlreadyStarted, userId, callingUid);
        if (ret2 != null) {
            return ret2;
        }
        ret2 = getCheckGameBoosterAntimsgIntent(context, callingPackage, packageName, intent, fromActivity, requestCode, userId);
        if (ret2 != null) {
            return ret2;
        }
        return getCheckAccessControlIntent(context, packageName, intent, fromActivity, requestCode, userId, bOptions);
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0049  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0048 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0048 A:{RETURN} */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x0049  */
    public android.content.Intent getCheckStartActivityIntent(android.content.Context r15, android.content.pm.ApplicationInfo r16, java.lang.String r17, java.lang.String r18, android.content.Intent r19, boolean r20, int r21, boolean r22, int r23, int r24) {
        /*
        r14 = this;
        r1 = r18;
        r2 = r19;
        r3 = r23;
        r4 = android.os.SystemClock.elapsedRealtime();
        r6 = 0;
        if (r2 == 0) goto L_0x00eb;
    L_0x000d:
        if (r22 != 0) goto L_0x00eb;
    L_0x000f:
        r0 = android.miui.AppOpsUtils.isXOptMode();
        if (r0 != 0) goto L_0x00eb;
    L_0x0015:
        r0 = miui.os.Build.IS_INTERNATIONAL_BUILD;
        if (r0 == 0) goto L_0x001e;
    L_0x0019:
        r7 = r14;
        r11 = r17;
        goto L_0x00ee;
    L_0x001e:
        r7 = "SecurityManager";
        r8 = 0;
        r9 = "getCheckStartActivityIntent";
        if (r16 != 0) goto L_0x004b;
    L_0x0025:
        r0 = android.text.TextUtils.isEmpty(r17);
        if (r0 == 0) goto L_0x002c;
    L_0x002b:
        return r6;
    L_0x002c:
        r0 = android.os.UserHandle.getUserId(r24);	 Catch:{ Exception -> 0x003e }
        r10 = android.app.AppGlobals.getPackageManager();	 Catch:{ Exception -> 0x003e }
        r11 = r17;
        r10 = r10.getApplicationInfo(r11, r8, r0);	 Catch:{ Exception -> 0x003c }
        r0 = r10;
        goto L_0x0046;
    L_0x003c:
        r0 = move-exception;
        goto L_0x0041;
    L_0x003e:
        r0 = move-exception;
        r11 = r17;
    L_0x0041:
        android.util.Log.e(r7, r9, r0);
        r0 = r16;
    L_0x0046:
        if (r0 != 0) goto L_0x0049;
    L_0x0048:
        return r6;
    L_0x0049:
        r10 = r0;
        goto L_0x004f;
    L_0x004b:
        r11 = r17;
        r10 = r16;
    L_0x004f:
        r0 = r10.flags;
        r0 = r0 & 129;
        if (r0 != 0) goto L_0x00e9;
    L_0x0055:
        r0 = r10.uid;
        r12 = 10000; // 0x2710 float:1.4013E-41 double:4.9407E-320;
        if (r0 >= r12) goto L_0x005e;
    L_0x005b:
        r7 = r14;
        goto L_0x00ea;
    L_0x005e:
        r13 = 0;
        r0 = android.app.AppGlobals.getPackageManager();	 Catch:{ Exception -> 0x0069 }
        r0 = r0.getApplicationInfo(r1, r8, r3);	 Catch:{ Exception -> 0x0069 }
        r13 = r0;
        goto L_0x006d;
    L_0x0069:
        r0 = move-exception;
        android.util.Log.e(r7, r9, r0);
    L_0x006d:
        if (r13 != 0) goto L_0x0070;
    L_0x006f:
        return r6;
    L_0x0070:
        r0 = r13.flags;
        r0 = r0 & 129;
        if (r0 != 0) goto L_0x00e7;
    L_0x0076:
        r0 = r13.uid;
        if (r0 >= r12) goto L_0x007c;
    L_0x007a:
        r7 = r14;
        goto L_0x00e8;
    L_0x007c:
        r0 = r10.packageName;
        r7 = r13.packageName;
        r0 = android.text.TextUtils.equals(r0, r7);
        if (r0 != 0) goto L_0x00e5;
    L_0x0086:
        r0 = r10.packageName;
        r7 = r14;
        r0 = r14.checkAllowStartActivity(r0, r1, r2, r3);
        if (r0 == 0) goto L_0x0090;
    L_0x008f:
        goto L_0x00e6;
    L_0x0090:
        r0 = "android.app.action.CHECK_ALLOW_START_ACTIVITY";
        r12 = "is_pad";
        r8 = miui.util.FeatureParser.getBoolean(r12, r8);
        if (r8 == 0) goto L_0x009b;
    L_0x009a:
        return r6;
    L_0x009b:
        r6 = new android.content.Intent;
        r6.<init>(r0);
        r8 = r10.packageName;
        r12 = "CallerPkgName";
        r6.putExtra(r12, r8);
        r8 = r13.packageName;
        r12 = "CalleePkgName";
        r6.putExtra(r12, r8);
        r8 = "UserId";
        r6.putExtra(r8, r3);
        r8 = 8388608; // 0x800000 float:1.17549435E-38 double:4.144523E-317;
        r6.addFlags(r8);
        r8 = "com.miui.securitycenter";
        r6.setPackage(r8);
        r8 = r19.getFlags();
        r12 = 33554432; // 0x2000000 float:9.403955E-38 double:1.6578092E-316;
        r8 = r8 & r12;
        if (r8 == 0) goto L_0x00ca;
    L_0x00c7:
        r6.addFlags(r12);
    L_0x00ca:
        r8 = 16777216; // 0x1000000 float:2.3509887E-38 double:8.289046E-317;
        r2.addFlags(r8);
        if (r20 == 0) goto L_0x00d7;
    L_0x00d1:
        if (r21 < 0) goto L_0x00dc;
    L_0x00d3:
        r2.addFlags(r12);
        goto L_0x00dc;
    L_0x00d7:
        r8 = 268435456; // 0x10000000 float:2.5243549E-29 double:1.32624737E-315;
        r2.addFlags(r8);
    L_0x00dc:
        r8 = "android.intent.extra.INTENT";
        r6.putExtra(r8, r2);
        checkTime(r4, r9);
        return r6;
    L_0x00e5:
        r7 = r14;
    L_0x00e6:
        return r6;
    L_0x00e7:
        r7 = r14;
    L_0x00e8:
        return r6;
    L_0x00e9:
        r7 = r14;
    L_0x00ea:
        return r6;
    L_0x00eb:
        r7 = r14;
        r11 = r17;
    L_0x00ee:
        return r6;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.security.SecurityManager.getCheckStartActivityIntent(android.content.Context, android.content.pm.ApplicationInfo, java.lang.String, java.lang.String, android.content.Intent, boolean, int, boolean, int, int):android.content.Intent");
    }

    public Intent getCheckAccessControlIntent(Context context, String packageName, Intent intent, int userId) {
        boolean z = context != null && (context instanceof Activity);
        return getCheckAccessControlIntent(context, packageName, intent, z, -1, userId, null);
    }

    public Intent getCheckAccessControlIntent(Context context, String packageName, Intent intent, boolean fromActivity, int requestCode, int userId, Bundle bOptions) {
        String str = packageName;
        Intent intent2 = intent;
        int i = userId;
        if (context == null || !isAccessControlActived(context, i)) {
            return null;
        }
        ApplicationInfo info;
        try {
            info = AppGlobals.getPackageManager().getApplicationInfo(packageName, 0, i);
        } catch (Exception e) {
            info = null;
        }
        if (info == null) {
            return null;
        }
        if ((intent2 == null || info.uid != Binder.getCallingUid()) && !checkAccessControlPassAsUser(packageName, intent2, i) && (intent2 == null || (intent.getFlags() & 1048576) == 0)) {
            return getCheckAccessIntent(true, packageName, intent, requestCode, fromActivity, userId, bOptions);
        }
        return null;
    }

    public static Intent getCheckAccessIntent(boolean isAccessControl, String packageName, Intent original, int requestCode, boolean fromActivity, int userId, Bundle bOptions) {
        String action = "miui.intent.action.CHECK_ACCESS_CONTROL";
        if (!isAccessControl) {
            action = "com.miui.gamebooster.action.ACCESS_WINDOWCALLACTIVITY";
        }
        String targetPkg = "com.miui.securitycenter";
        if (FeatureParser.getBoolean("is_pad", false)) {
            action = "android.app.action.CHECK_ACCESS_CONTROL_PAD";
            targetPkg = SettingsTree.SETTINGS_PACKAGE;
        }
        Intent result = new Intent(action);
        result.putExtra(Intent.EXTRA_SHORTCUT_NAME, packageName);
        result.addFlags(8388608);
        result.setPackage(targetPkg);
        if (original != null) {
            if ((original.getFlags() & 33554432) != 0) {
                result.addFlags(33554432);
            }
            original.addFlags(16777216);
            if (fromActivity) {
                if (requestCode >= 0) {
                    original.addFlags(33554432);
                }
                if ((original.getFlags() & 268435456) == 0) {
                    result.addFlags(536870912);
                } else {
                    result.addFlags(268435456);
                    result.addFlags(134217728);
                }
            } else {
                original.addFlags(268435456);
                result.addFlags(134217728);
            }
            result.putExtra(Intent.EXTRA_INTENT, (Parcelable) original);
        } else {
            result.addFlags(536870912);
        }
        if (userId == 999) {
            result.putExtra("originating_uid", userId);
        }
        if (bOptions != null) {
            result.putExtras(bOptions);
        }
        return result;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x005d A:{Catch:{ all -> 0x0065 }} */
    public void checkAccessControl(android.app.Activity r13, int r14) {
        /*
        r12 = this;
        r0 = "skip_interception";
        if (r13 == 0) goto L_0x00aa;
    L_0x0005:
        r1 = r13.getParent();
        if (r1 != 0) goto L_0x00aa;
    L_0x000b:
        r1 = r13.getPackageName();
        r2 = new android.content.Intent;
        r2.<init>();
        r9 = r2;
        r2 = new android.content.ComponentName;
        r3 = r13.getClass();
        r3 = r3.getName();
        r2.<init>(r1, r3);
        r9.setComponent(r2);
        r2 = r13.getClass();
        r2 = r2.getName();
        r3 = "com.miui.gallery.activity.ExternalPhotoPageActivity";
        r2 = r3.equals(r2);
        r3 = 0;
        if (r2 != 0) goto L_0x0049;
    L_0x0036:
        r2 = r13.getClass();
        r2 = r2.getName();
        r4 = "com.miui.gallery.editor.photo.screen.home.ScreenEditorActivity";
        r2 = r4.equals(r2);
        if (r2 == 0) goto L_0x0047;
    L_0x0046:
        goto L_0x0049;
    L_0x0047:
        r2 = r3;
        goto L_0x004a;
    L_0x0049:
        r2 = 1;
    L_0x004a:
        r10 = r2;
        r2 = "com.miui.gallery";
        r2 = r2.equals(r1);
        r11 = "SecurityManager";
        if (r2 == 0) goto L_0x006b;
    L_0x0055:
        if (r10 == 0) goto L_0x006b;
    L_0x0057:
        r2 = r13.getIntent();	 Catch:{ all -> 0x0065 }
        if (r2 == 0) goto L_0x0064;
    L_0x005d:
        r4 = r2.getBooleanExtra(r0, r3);	 Catch:{ all -> 0x0065 }
        r9.putExtra(r0, r4);	 Catch:{ all -> 0x0065 }
    L_0x0064:
        goto L_0x006b;
    L_0x0065:
        r0 = move-exception;
        r2 = "checkAccessControl sourceIntent";
        android.util.Log.e(r11, r2, r0);
    L_0x006b:
        r0 = r12.activityResume(r9);
        r2 = r0 & 1;
        if (r2 == 0) goto L_0x00a9;
    L_0x0073:
        r2 = r0 & 2;
        if (r2 != 0) goto L_0x0078;
    L_0x0077:
        goto L_0x00a9;
    L_0x0078:
        r2 = r0 & 8;
        if (r2 == 0) goto L_0x0083;
    L_0x007c:
        r13.setResult(r3);
        r12.activityFinish(r13);
        return;
    L_0x0083:
        r2 = r0 & 4;
        if (r2 == 0) goto L_0x0088;
    L_0x0087:
        return;
    L_0x0088:
        r2 = 1;
        r4 = 0;
        r5 = -1;
        r6 = 1;
        r8 = 0;
        r3 = r1;
        r7 = r14;
        r2 = getCheckAccessIntent(r2, r3, r4, r5, r6, r7, r8);
        r3 = r13.getActivityToken();
        r4 = "android.app.extra.PROTECTED_APP_TOKEN";
        r2.putExtra(r4, r3);
        r3 = -1;
        r4 = 0;
        r13.startActivityForResult(r2, r3, r4);	 Catch:{ ActivityNotFoundException -> 0x00a2 }
        goto L_0x00aa;
    L_0x00a2:
        r3 = move-exception;
        r4 = "checkAccessControl";
        android.util.Log.e(r11, r4, r3);
        goto L_0x00aa;
    L_0x00a9:
        return;
    L_0x00aa:
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.security.SecurityManager.checkAccessControl(android.app.Activity, int):void");
    }

    public Intent getCheckGameBoosterAntimsgIntent(Context context, String callPackageName, String packageName, Intent intent, boolean fromActivity, int requestCode, int userId) {
        Intent intent2;
        String str = packageName;
        int i = userId;
        String str2;
        if (context != null) {
            str2 = callPackageName;
            if (!"com.miui.securitycenter".equals(callPackageName)) {
                if (isGameBoosterActived(i)) {
                    ApplicationInfo info;
                    try {
                        info = AppGlobals.getPackageManager().getApplicationInfo(str, 0, i);
                    } catch (Exception e) {
                        info = null;
                    }
                    return (info == null || checkGameBoosterAntimsgPassAsUser(str, intent, i)) ? null : getCheckAccessIntent(false, packageName, intent, requestCode, fromActivity, userId, null);
                } else {
                    intent2 = intent;
                    return null;
                }
            }
        }
        str2 = callPackageName;
        intent2 = intent;
        return null;
    }

    private void activityFinish(Activity activity) {
        activity.finish();
        if (!activity.isFinishing()) {
            try {
                if (sActivityFinishMethod != null) {
                    if (VERSION.SDK_INT >= 24) {
                        sActivityFinishMethod.invoke(activity, new Object[]{Integer.valueOf(0)});
                    } else {
                        sActivityFinishMethod.invoke(activity, new Object[]{Boolean.valueOf(false)});
                    }
                } else if (activity.getParent() == null) {
                    activity.finishAffinity();
                }
            } catch (Exception e) {
                Log.e(TAG, " FinishMethod.invoke error ", e);
            }
        }
    }

    public boolean isAccessControlActived(Context context) {
        return isAccessControlActived(context, UserHandle.getCallingUserId());
    }

    public boolean isAccessControlActived(Context context, int userHandle) {
        return 1 == Secure.getIntForUser(context.getContentResolver(), MiuiSettings.Secure.ACCESS_CONTROL_LOCK_ENABLED, 0, getUserHandle(userHandle));
    }

    public boolean isGameBoosterActived(int userHandle) {
        try {
            return this.mService.getGameMode(userHandle);
        } catch (RemoteException e) {
            throw new RuntimeException("isGameBoosterActived exception", e);
        }
    }

    public static int getUserHandle(int original) {
        if (XSpaceUserHandle.isXSpaceUserId(original)) {
            return 0;
        }
        return original;
    }

    public static void init() {
        if (VERSION.SDK_INT == 19) {
            hook();
        }
    }

    public void setWakeUpTime(String componentName, long timeInSeconds) {
        try {
            this.mService.setWakeUpTime(componentName, timeInSeconds);
        } catch (RemoteException e) {
        }
    }

    public long getWakeUpTime(String componentName) {
        try {
            return this.mService.getWakeUpTime(componentName);
        } catch (RemoteException e) {
            return 0;
        }
    }

    public boolean putSystemDataStringFile(String path, String value) {
        try {
            return this.mService.putSystemDataStringFile(path, value);
        } catch (RemoteException e) {
            return false;
        }
    }

    public String readSystemDataStringFile(String path) {
        try {
            return this.mService.readSystemDataStringFile(path);
        } catch (RemoteException e) {
            return null;
        }
    }

    public void pushWakePathData(int wakeType, ParceledListSlice wakePathRuleInfos, int userId) {
        try {
            this.mService.pushWakePathData(wakeType, wakePathRuleInfos, userId);
        } catch (RemoteException e) {
        }
    }

    public void pushWakePathWhiteList(List<String> wakePathWhiteList, int userId) {
        try {
            this.mService.pushWakePathWhiteList(wakePathWhiteList, userId);
        } catch (RemoteException e) {
        }
    }

    public void pushWakePathConfirmDialogWhiteList(int type, List<String> whiteList) {
        try {
            this.mService.pushWakePathConfirmDialogWhiteList(type, whiteList);
        } catch (RemoteException e) {
            Log.e(TAG, "pushWakePathConfirmDialogWhiteList", e);
        }
    }

    public void removeWakePathData(int userId) {
        try {
            this.mService.removeWakePathData(userId);
        } catch (RemoteException e) {
        }
    }

    public void setTrackWakePathCallListLogEnabled(boolean enabled) {
        try {
            this.mService.setTrackWakePathCallListLogEnabled(enabled);
        } catch (RemoteException e) {
        }
    }

    public ParceledListSlice getWakePathCallListLog() {
        try {
            return this.mService.getWakePathCallListLog();
        } catch (Exception e) {
            return null;
        }
    }

    public void registerWakePathCallback(IWakePathCallback callback) {
        try {
            this.mService.registerWakePathCallback(callback);
        } catch (Exception e) {
        }
    }

    public boolean getAppPermissionControlOpen(int userId) {
        boolean z = false;
        try {
            if (this.mService.getAppPermissionControlOpen(userId) != 0) {
                z = true;
            }
            return z;
        } catch (Exception e) {
            return false;
        }
    }

    public void setAppPermissionControlOpen(int status) {
        try {
            this.mService.setAppPermissionControlOpen(status);
        } catch (Exception e) {
        }
    }

    public boolean needFinishAccessControl(IBinder token) {
        try {
            return this.mService.needFinishAccessControl(token);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void finishAccessControl(String packageName) {
        try {
            this.mService.finishAccessControl(packageName, UserHandle.myUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishAccessControl(String packageName, int userId) {
        try {
            this.mService.finishAccessControl(packageName, userId);
        } catch (Exception e) {
            throw new RuntimeException("finishAccessControl has failed", e);
        }
    }

    public int activityResume(Intent intent) {
        try {
            return this.mService.activityResume(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void setCoreRuntimePermissionEnabled(boolean grant, int flags) {
        try {
            this.mService.setCoreRuntimePermissionEnabled(grant, flags);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void grantRuntimePermission(String packageName) {
        try {
            this.mService.grantRuntimePermission(packageName);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    private static void checkTime(long startTime, String where) {
        long now = SystemClock.elapsedRealtime();
        if (now - startTime > 100) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("MIUILOG-checkTime:Slow operation: ");
            stringBuilder.append(now - startTime);
            stringBuilder.append("ms so far, now at ");
            stringBuilder.append(where);
            Slog.w(TAG, stringBuilder.toString());
        }
    }

    public int getSysAppCracked() {
        try {
            return this.mService.getSysAppCracked();
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void grantInstallPermission(String packageName, String name) {
        try {
            this.mService.grantInstallPermission(packageName, name);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean isRestrictedAppNet(Context context) {
        try {
            return this.mService.isRestrictedAppNet(context.getPackageName());
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean writeAppHideConfig(boolean hide) {
        try {
            return this.mService.writeAppHideConfig(hide);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean addMiuiFirewallSharedUid(int uid) {
        try {
            return this.mService.addMiuiFirewallSharedUid(uid);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean setMiuiFirewallRule(String packageName, int uid, int rule, int type) {
        try {
            return this.mService.setMiuiFirewallRule(packageName, uid, rule, type);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean setCurrentNetworkState(int state) {
        try {
            return this.mService.setCurrentNetworkState(state);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void setIncompatibleAppList(List<String> list) {
        try {
            this.mService.setIncompatibleAppList(list);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public List<String> getIncompatibleAppList() {
        try {
            return this.mService.getIncompatibleAppList();
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public List<WakePathComponent> getWakePathComponents(String packageName) {
        try {
            ParceledListSlice<WakePathComponent> slice = this.mService.getWakePathComponents(packageName);
            if (slice == null) {
                return null;
            }
            return slice.getList();
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died.", e);
        }
    }

    public boolean areNotificationsEnabledForPackage(String packageName, int uid) {
        try {
            return this.mService.areNotificationsEnabledForPackage(packageName, uid);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void setNotificationsEnabledForPackage(String packageName, int uid, boolean enabled) {
        try {
            this.mService.setNotificationsEnabledForPackage(packageName, uid, enabled);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean isAppHide() {
        try {
            return this.mService.isAppHide();
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean isFunctionOpen() {
        try {
            return this.mService.isFunctionOpen();
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean setAppHide(boolean hide) {
        try {
            return this.mService.setAppHide(hide);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean isValidDevice() {
        try {
            return this.mService.isValidDevice();
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void setAppPrivacyStatus(String packageName, boolean isOpen) {
        try {
            this.mService.setAppPrivacyStatus(packageName, isOpen);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean isAppPrivacyEnabled(String packageName) {
        try {
            return this.mService.isAppPrivacyEnabled(packageName);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean isAllowStartService(Intent service, int userId) {
        try {
            return this.mService.isAllowStartService(service, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public IBinder getTopActivity() {
        try {
            return this.mService.getTopActivity();
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void watchGreenGuardProcess() {
        try {
            this.mService.watchGreenGuardProcess();
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public int moveTaskToStack(int taskId, int stackId, boolean toTop) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("moveTaskToStack ");
        stringBuilder.append(taskId);
        stringBuilder.append(" to ");
        stringBuilder.append(stackId);
        stringBuilder.append(", isOnTop:");
        stringBuilder.append(toTop);
        Log.i(TAG, stringBuilder.toString());
        try {
            return this.mService.moveTaskToStack(taskId, stackId, toTop);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void setStickWindowName(String component) {
        try {
            this.mService.setStickWindowName(component);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean getStickWindowName(String component) {
        try {
            return this.mService.getStickWindowName(component);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public int resizeTask(int taskId, Rect bounds, int resizeMode) {
        Log.i(TAG, "resizeTask");
        try {
            return this.mService.resizeTask(taskId, bounds, resizeMode);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void pushUpdatePkgsData(List<String> updatePkgsList, boolean enable) {
        try {
            this.mService.pushUpdatePkgsData(updatePkgsList, enable);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void setPrivacyApp(String packageName, int userId, boolean isPrivacy) {
        try {
            this.mService.setPrivacyApp(packageName, userId, isPrivacy);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public boolean isPrivacyApp(String packageName, int userId) {
        try {
            return this.mService.isPrivacyApp(packageName, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public List<String> getAllPrivacyApps(int userId) {
        try {
            return this.mService.getAllPrivacyApps(userId);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void updateLauncherPackageNames() {
        try {
            this.mService.updateLauncherPackageNames();
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void grantRuntimePermissionAsUser(String packageName, String permName, int userId) {
        try {
            this.mService.grantRuntimePermissionAsUser(packageName, permName, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void revokeRuntimePermissionAsUser(String packageName, String permName, int userId) {
        try {
            this.mService.revokeRuntimePermissionAsUser(packageName, permName, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void revokeRuntimePermissionAsUserNotKill(String packageName, String permName, int userId) {
        try {
            this.mService.revokeRuntimePermissionAsUserNotKill(packageName, permName, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public int getPermissionFlagsAsUser(String permName, String packageName, int userId) {
        try {
            return this.mService.getPermissionFlagsAsUser(permName, packageName, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }

    public void updatePermissionFlagsAsUser(String permissionName, String packageName, int flagMask, int flagValues, int userId) {
        try {
            this.mService.updatePermissionFlagsAsUser(permissionName, packageName, flagMask, flagValues, userId);
        } catch (RemoteException e) {
            throw new RuntimeException("security manager has died", e);
        }
    }
}
