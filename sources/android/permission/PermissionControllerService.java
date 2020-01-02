package android.permission;

import android.Manifest.permission;
import android.annotation.SystemApi;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteCallback;
import android.os.UserHandle;
import android.permission.IPermissionController.Stub;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.util.Preconditions;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@SystemApi
public abstract class PermissionControllerService extends Service {
    private static final String LOG_TAG = PermissionControllerService.class.getSimpleName();
    public static final String SERVICE_INTERFACE = "android.permission.PermissionControllerService";

    public abstract void onCountPermissionApps(List<String> list, int i, IntConsumer intConsumer);

    public abstract void onGetAppPermissions(String str, Consumer<List<RuntimePermissionPresentationInfo>> consumer);

    public abstract void onGetPermissionUsages(boolean z, long j, Consumer<List<RuntimePermissionUsageInfo>> consumer);

    public abstract void onGetRuntimePermissionsBackup(UserHandle userHandle, OutputStream outputStream, Runnable runnable);

    public abstract void onGrantOrUpgradeDefaultRuntimePermissions(Runnable runnable);

    public abstract void onRestoreDelayedRuntimePermissionsBackup(String str, UserHandle userHandle, Consumer<Boolean> consumer);

    public abstract void onRestoreRuntimePermissionsBackup(UserHandle userHandle, InputStream inputStream, Runnable runnable);

    public abstract void onRevokeRuntimePermission(String str, String str2, Runnable runnable);

    public abstract void onRevokeRuntimePermissions(Map<String, List<String>> map, boolean z, int i, String str, Consumer<Map<String, List<String>>> consumer);

    public abstract void onSetRuntimePermissionGrantStateByDeviceAdmin(String str, String str2, String str3, int i, Consumer<Boolean> consumer);

    public final IBinder onBind(Intent intent) {
        return new Stub() {
            public void revokeRuntimePermissions(Bundle bundleizedRequest, boolean doDryRun, int reason, String callerPackageName, RemoteCallback callback) {
                Preconditions.checkNotNull(bundleizedRequest, "bundleizedRequest");
                Preconditions.checkNotNull(callerPackageName);
                Preconditions.checkNotNull(callback);
                Map<String, List<String>> request = new ArrayMap();
                for (String packageName : bundleizedRequest.keySet()) {
                    Preconditions.checkNotNull(packageName);
                    ArrayList<String> permissions = bundleizedRequest.getStringArrayList(packageName);
                    Preconditions.checkCollectionElementsNotNull(permissions, "permissions");
                    request.put(packageName, permissions);
                }
                PermissionControllerService.this.enforceCallingPermission(permission.REVOKE_RUNTIME_PERMISSIONS, null);
                try {
                    boolean z = false;
                    if (Binder.getCallingUid() == PermissionControllerService.this.getPackageManager().getPackageInfo(callerPackageName, 0).applicationInfo.uid) {
                        z = true;
                    }
                    Preconditions.checkArgument(z);
                    PermissionControllerService.this.onRevokeRuntimePermissions(request, doDryRun, reason, callerPackageName, new -$$Lambda$PermissionControllerService$1$__ZsT0Jo3iLdGM0gy2UV6ea_oEw(callback));
                } catch (NameNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            static /* synthetic */ void lambda$revokeRuntimePermissions$0(RemoteCallback callback, Map revoked) {
                Preconditions.checkNotNull(revoked);
                Bundle bundledizedRevoked = new Bundle();
                for (Entry<String, List<String>> appRevocation : revoked.entrySet()) {
                    Preconditions.checkNotNull((String) appRevocation.getKey());
                    Preconditions.checkCollectionElementsNotNull((List) appRevocation.getValue(), "permissions");
                    bundledizedRevoked.putStringArrayList((String) appRevocation.getKey(), new ArrayList((Collection) appRevocation.getValue()));
                }
                Bundle result = new Bundle();
                result.putBundle(PermissionControllerManager.KEY_RESULT, bundledizedRevoked);
                callback.sendResult(result);
            }

            /* JADX WARNING: Missing block: B:12:?, code skipped:
            android.permission.PermissionControllerService.AnonymousClass1.$closeResource(r1, r0);
     */
            public void getRuntimePermissionBackup(android.os.UserHandle r6, android.os.ParcelFileDescriptor r7) {
                /*
                r5 = this;
                com.android.internal.util.Preconditions.checkNotNull(r6);
                com.android.internal.util.Preconditions.checkNotNull(r7);
                r0 = android.permission.PermissionControllerService.this;
                r1 = 0;
                r2 = "android.permission.GET_RUNTIME_PERMISSIONS";
                r0.enforceCallingPermission(r2, r1);
                r0 = new android.os.ParcelFileDescriptor$AutoCloseOutputStream;	 Catch:{ IOException -> 0x0040, InterruptedException -> 0x0034 }
                r0.<init>(r7);	 Catch:{ IOException -> 0x0040, InterruptedException -> 0x0034 }
                r2 = new java.util.concurrent.CountDownLatch;	 Catch:{ all -> 0x002d }
                r3 = 1;
                r2.<init>(r3);	 Catch:{ all -> 0x002d }
                r3 = android.permission.PermissionControllerService.this;	 Catch:{ all -> 0x002d }
                java.util.Objects.requireNonNull(r2);	 Catch:{ all -> 0x002d }
                r4 = new android.permission.-$$Lambda$5k6tNlswoNAjCdgttrkQIe8VHVs;	 Catch:{ all -> 0x002d }
                r4.<init>(r2);	 Catch:{ all -> 0x002d }
                r3.onGetRuntimePermissionsBackup(r6, r0, r4);	 Catch:{ all -> 0x002d }
                r2.await();	 Catch:{ all -> 0x002d }
                android.permission.PermissionControllerService.AnonymousClass1.$closeResource(r1, r0);	 Catch:{ IOException -> 0x0040, InterruptedException -> 0x0034 }
                goto L_0x004a;
            L_0x002d:
                r1 = move-exception;
                throw r1;	 Catch:{ all -> 0x002f }
            L_0x002f:
                r2 = move-exception;
                android.permission.PermissionControllerService.AnonymousClass1.$closeResource(r1, r0);	 Catch:{ IOException -> 0x0040, InterruptedException -> 0x0034 }
                throw r2;	 Catch:{ IOException -> 0x0040, InterruptedException -> 0x0034 }
            L_0x0034:
                r0 = move-exception;
                r1 = android.permission.PermissionControllerService.LOG_TAG;
                r2 = "getRuntimePermissionBackup timed out";
                android.util.Log.e(r1, r2, r0);
                goto L_0x004b;
            L_0x0040:
                r0 = move-exception;
                r1 = android.permission.PermissionControllerService.LOG_TAG;
                r2 = "Could not open pipe to write backup to";
                android.util.Log.e(r1, r2, r0);
            L_0x004b:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.permission.PermissionControllerService$AnonymousClass1.getRuntimePermissionBackup(android.os.UserHandle, android.os.ParcelFileDescriptor):void");
            }

            private static /* synthetic */ void $closeResource(Throwable x0, AutoCloseable x1) {
                if (x0 != null) {
                    try {
                        x1.close();
                        return;
                    } catch (Throwable th) {
                        x0.addSuppressed(th);
                        return;
                    }
                }
                x1.close();
            }

            /* JADX WARNING: Missing block: B:12:?, code skipped:
            android.permission.PermissionControllerService.AnonymousClass1.$closeResource(r1, r0);
     */
            public void restoreRuntimePermissionBackup(android.os.UserHandle r6, android.os.ParcelFileDescriptor r7) {
                /*
                r5 = this;
                com.android.internal.util.Preconditions.checkNotNull(r6);
                com.android.internal.util.Preconditions.checkNotNull(r7);
                r0 = android.permission.PermissionControllerService.this;
                r1 = 0;
                r2 = "android.permission.GRANT_RUNTIME_PERMISSIONS";
                r0.enforceCallingPermission(r2, r1);
                r0 = new android.os.ParcelFileDescriptor$AutoCloseInputStream;	 Catch:{ IOException -> 0x0040, InterruptedException -> 0x0034 }
                r0.<init>(r7);	 Catch:{ IOException -> 0x0040, InterruptedException -> 0x0034 }
                r2 = new java.util.concurrent.CountDownLatch;	 Catch:{ all -> 0x002d }
                r3 = 1;
                r2.<init>(r3);	 Catch:{ all -> 0x002d }
                r3 = android.permission.PermissionControllerService.this;	 Catch:{ all -> 0x002d }
                java.util.Objects.requireNonNull(r2);	 Catch:{ all -> 0x002d }
                r4 = new android.permission.-$$Lambda$5k6tNlswoNAjCdgttrkQIe8VHVs;	 Catch:{ all -> 0x002d }
                r4.<init>(r2);	 Catch:{ all -> 0x002d }
                r3.onRestoreRuntimePermissionsBackup(r6, r0, r4);	 Catch:{ all -> 0x002d }
                r2.await();	 Catch:{ all -> 0x002d }
                android.permission.PermissionControllerService.AnonymousClass1.$closeResource(r1, r0);	 Catch:{ IOException -> 0x0040, InterruptedException -> 0x0034 }
                goto L_0x004a;
            L_0x002d:
                r1 = move-exception;
                throw r1;	 Catch:{ all -> 0x002f }
            L_0x002f:
                r2 = move-exception;
                android.permission.PermissionControllerService.AnonymousClass1.$closeResource(r1, r0);	 Catch:{ IOException -> 0x0040, InterruptedException -> 0x0034 }
                throw r2;	 Catch:{ IOException -> 0x0040, InterruptedException -> 0x0034 }
            L_0x0034:
                r0 = move-exception;
                r1 = android.permission.PermissionControllerService.LOG_TAG;
                r2 = "restoreRuntimePermissionBackup timed out";
                android.util.Log.e(r1, r2, r0);
                goto L_0x004b;
            L_0x0040:
                r0 = move-exception;
                r1 = android.permission.PermissionControllerService.LOG_TAG;
                r2 = "Could not open pipe to read backup from";
                android.util.Log.e(r1, r2, r0);
            L_0x004b:
                return;
                */
                throw new UnsupportedOperationException("Method not decompiled: android.permission.PermissionControllerService$AnonymousClass1.restoreRuntimePermissionBackup(android.os.UserHandle, android.os.ParcelFileDescriptor):void");
            }

            public void restoreDelayedRuntimePermissionBackup(String packageName, UserHandle user, RemoteCallback callback) {
                Preconditions.checkNotNull(packageName);
                Preconditions.checkNotNull(user);
                Preconditions.checkNotNull(callback);
                PermissionControllerService.this.enforceCallingPermission(permission.GRANT_RUNTIME_PERMISSIONS, null);
                PermissionControllerService.this.onRestoreDelayedRuntimePermissionsBackup(packageName, user, new -$$Lambda$PermissionControllerService$1$byERALVqclrc25diZo2Ly0OtfwI(callback));
            }

            static /* synthetic */ void lambda$restoreDelayedRuntimePermissionBackup$1(RemoteCallback callback, Boolean hasMoreBackup) {
                Bundle result = new Bundle();
                result.putBoolean(PermissionControllerManager.KEY_RESULT, hasMoreBackup.booleanValue());
                callback.sendResult(result);
            }

            public void getAppPermissions(String packageName, RemoteCallback callback) {
                Preconditions.checkNotNull(packageName, "packageName");
                Preconditions.checkNotNull(callback, "callback");
                PermissionControllerService.this.enforceCallingPermission(permission.GET_RUNTIME_PERMISSIONS, null);
                PermissionControllerService.this.onGetAppPermissions(packageName, new -$$Lambda$PermissionControllerService$1$ROtJOrojS2cjqvX59tSprAvs-1o(callback));
            }

            static /* synthetic */ void lambda$getAppPermissions$2(RemoteCallback callback, List permissions) {
                if (permissions == null || permissions.isEmpty()) {
                    callback.sendResult(null);
                    return;
                }
                Bundle result = new Bundle();
                result.putParcelableList(PermissionControllerManager.KEY_RESULT, permissions);
                callback.sendResult(result);
            }

            public void revokeRuntimePermission(String packageName, String permissionName) {
                Preconditions.checkNotNull(packageName, "packageName");
                Preconditions.checkNotNull(permissionName, "permissionName");
                PermissionControllerService.this.enforceCallingPermission(permission.REVOKE_RUNTIME_PERMISSIONS, null);
                CountDownLatch latch = new CountDownLatch(1);
                PermissionControllerService permissionControllerService = PermissionControllerService.this;
                Objects.requireNonNull(latch);
                permissionControllerService.onRevokeRuntimePermission(packageName, permissionName, new -$$Lambda$5k6tNlswoNAjCdgttrkQIe8VHVs(latch));
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    Log.e(PermissionControllerService.LOG_TAG, "revokeRuntimePermission timed out", e);
                }
            }

            public void countPermissionApps(List<String> permissionNames, int flags, RemoteCallback callback) {
                Preconditions.checkCollectionElementsNotNull(permissionNames, "permissionNames");
                Preconditions.checkFlagsArgument(flags, 3);
                Preconditions.checkNotNull(callback, "callback");
                PermissionControllerService.this.enforceCallingPermission(permission.GET_RUNTIME_PERMISSIONS, null);
                PermissionControllerService.this.onCountPermissionApps(permissionNames, flags, new -$$Lambda$PermissionControllerService$1$i3vGLgbFSsM1LDWQDjRkXStMIUE(callback));
            }

            static /* synthetic */ void lambda$countPermissionApps$3(RemoteCallback callback, int numApps) {
                Bundle result = new Bundle();
                result.putInt(PermissionControllerManager.KEY_RESULT, numApps);
                callback.sendResult(result);
            }

            public void getPermissionUsages(boolean countSystem, long numMillis, RemoteCallback callback) {
                Preconditions.checkArgumentNonnegative(numMillis);
                Preconditions.checkNotNull(callback, "callback");
                PermissionControllerService.this.enforceCallingPermission(permission.GET_RUNTIME_PERMISSIONS, null);
                PermissionControllerService.this.onGetPermissionUsages(countSystem, numMillis, new -$$Lambda$PermissionControllerService$1$oEdK7RdXzZpRIDF40ujz7uvW1Ts(callback));
            }

            static /* synthetic */ void lambda$getPermissionUsages$4(RemoteCallback callback, List users) {
                if (users == null || users.isEmpty()) {
                    callback.sendResult(null);
                    return;
                }
                Bundle result = new Bundle();
                result.putParcelableList(PermissionControllerManager.KEY_RESULT, users);
                callback.sendResult(result);
            }

            public void setRuntimePermissionGrantStateByDeviceAdmin(String callerPackageName, String packageName, String permission, int grantState, RemoteCallback callback) {
                Preconditions.checkStringNotEmpty(callerPackageName);
                Preconditions.checkStringNotEmpty(packageName);
                Preconditions.checkStringNotEmpty(permission);
                boolean z = true;
                if (!(grantState == 1 || grantState == 2 || grantState == 0)) {
                    z = false;
                }
                Preconditions.checkArgument(z);
                Preconditions.checkNotNull(callback);
                if (grantState == 2) {
                    PermissionControllerService.this.enforceCallingPermission(permission.GRANT_RUNTIME_PERMISSIONS, null);
                }
                if (grantState == 2) {
                    PermissionControllerService.this.enforceCallingPermission(permission.REVOKE_RUNTIME_PERMISSIONS, null);
                }
                PermissionControllerService.this.enforceCallingPermission(permission.ADJUST_RUNTIME_PERMISSIONS_POLICY, null);
                PermissionControllerService.this.onSetRuntimePermissionGrantStateByDeviceAdmin(callerPackageName, packageName, permission, grantState, new -$$Lambda$PermissionControllerService$1$Sp35OTwahalQfZumoUDJ70lCKe0(callback));
            }

            static /* synthetic */ void lambda$setRuntimePermissionGrantStateByDeviceAdmin$5(RemoteCallback callback, Boolean wasSet) {
                Bundle result = new Bundle();
                result.putBoolean(PermissionControllerManager.KEY_RESULT, wasSet.booleanValue());
                callback.sendResult(result);
            }

            public void grantOrUpgradeDefaultRuntimePermissions(RemoteCallback callback) {
                Preconditions.checkNotNull(callback, "callback");
                PermissionControllerService.this.enforceCallingPermission(permission.ADJUST_RUNTIME_PERMISSIONS_POLICY, null);
                PermissionControllerService.this.onGrantOrUpgradeDefaultRuntimePermissions(new -$$Lambda$PermissionControllerService$1$aoBUJn0rgfJAYfvz7rYL8N9wr_Y(callback));
            }
        };
    }
}
