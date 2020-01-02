package android.permission;

import android.Manifest.permission;
import android.annotation.SystemApi;
import android.app.ActivityThread;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.os.UserHandle;
import android.permission.IPermissionController.Stub;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService;
import com.android.internal.infra.AbstractRemoteService.AsyncRequest;
import com.android.internal.infra.AbstractRemoteService.BasePendingRequest;
import com.android.internal.infra.AbstractRemoteService.PendingRequest;
import com.android.internal.util.Preconditions;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import libcore.io.IoUtils;

@SystemApi
public final class PermissionControllerManager {
    public static final int COUNT_ONLY_WHEN_GRANTED = 1;
    public static final int COUNT_WHEN_SYSTEM = 2;
    public static final String KEY_RESULT = "android.permission.PermissionControllerManager.key.result";
    public static final int REASON_INSTALLER_POLICY_VIOLATION = 2;
    public static final int REASON_MALWARE = 1;
    private static final String TAG = PermissionControllerManager.class.getSimpleName();
    private static final Object sLock = new Object();
    @GuardedBy({"sLock"})
    private static ArrayMap<Pair<Integer, Thread>, RemoteService> sRemoteServices = new ArrayMap(1);
    private final Context mContext;
    private final RemoteService mRemoteService;

    @Retention(RetentionPolicy.SOURCE)
    public @interface CountPermissionAppsFlag {
    }

    private static class FileReaderTask<Callback extends Consumer<byte[]>> extends AsyncTask<Void, Void, byte[]> {
        private final Callback mCallback;
        private ParcelFileDescriptor mLocalPipe;
        private ParcelFileDescriptor mRemotePipe;

        FileReaderTask(Callback callback) {
            this.mCallback = callback;
        }

        /* Access modifiers changed, original: protected */
        public void onPreExecute() {
            try {
                ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
                this.mLocalPipe = pipe[0];
                this.mRemotePipe = pipe[1];
            } catch (IOException e) {
                Log.e(PermissionControllerManager.TAG, "Could not create pipe needed to get runtime permission backup", e);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public ParcelFileDescriptor getRemotePipe() {
            return this.mRemotePipe;
        }

        /* Access modifiers changed, original: protected|varargs */
        /* JADX WARNING: Missing block: B:20:?, code skipped:
            r1.close();
     */
        public byte[] doInBackground(java.lang.Void... r6) {
            /*
            r5 = this;
            r0 = new java.io.ByteArrayOutputStream;
            r0.<init>();
            r1 = new android.os.ParcelFileDescriptor$AutoCloseInputStream;	 Catch:{ IOException | NullPointerException -> 0x0033, IOException | NullPointerException -> 0x0033 }
            r2 = r5.mLocalPipe;	 Catch:{ IOException | NullPointerException -> 0x0033, IOException | NullPointerException -> 0x0033 }
            r1.<init>(r2);	 Catch:{ IOException | NullPointerException -> 0x0033, IOException | NullPointerException -> 0x0033 }
            r2 = 16384; // 0x4000 float:2.2959E-41 double:8.0948E-320;
            r2 = new byte[r2];	 Catch:{ all -> 0x0027 }
        L_0x0010:
            r3 = r5.isCancelled();	 Catch:{ all -> 0x0027 }
            if (r3 != 0) goto L_0x0023;
        L_0x0016:
            r3 = r1.read(r2);	 Catch:{ all -> 0x0027 }
            r4 = -1;
            if (r3 != r4) goto L_0x001e;
        L_0x001d:
            goto L_0x0023;
        L_0x001e:
            r4 = 0;
            r0.write(r2, r4, r3);	 Catch:{ all -> 0x0027 }
            goto L_0x0010;
        L_0x0023:
            r1.close();	 Catch:{ IOException | NullPointerException -> 0x0033, IOException | NullPointerException -> 0x0033 }
            goto L_0x0040;
        L_0x0027:
            r2 = move-exception;
            throw r2;	 Catch:{ all -> 0x0029 }
        L_0x0029:
            r3 = move-exception;
            r1.close();	 Catch:{ all -> 0x002e }
            goto L_0x0032;
        L_0x002e:
            r4 = move-exception;
            r2.addSuppressed(r4);	 Catch:{ IOException | NullPointerException -> 0x0033, IOException | NullPointerException -> 0x0033 }
        L_0x0032:
            throw r3;	 Catch:{ IOException | NullPointerException -> 0x0033, IOException | NullPointerException -> 0x0033 }
        L_0x0033:
            r1 = move-exception;
            r2 = android.permission.PermissionControllerManager.TAG;
            r3 = "Error reading runtime permission backup";
            android.util.Log.e(r2, r3, r1);
            r0.reset();
        L_0x0040:
            r1 = r0.toByteArray();
            return r1;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.permission.PermissionControllerManager$FileReaderTask.doInBackground(java.lang.Void[]):byte[]");
        }

        /* Access modifiers changed, original: 0000 */
        public void interruptRead() {
            IoUtils.closeQuietly(this.mLocalPipe);
        }

        /* Access modifiers changed, original: protected */
        public void onCancelled() {
            onPostExecute(new byte[0]);
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(byte[] backup) {
            IoUtils.closeQuietly(this.mLocalPipe);
            this.mCallback.accept(backup);
        }
    }

    private static class FileWriterTask extends AsyncTask<byte[], Void, Void> {
        private static final int CHUNK_SIZE = 4096;
        private ParcelFileDescriptor mLocalPipe;
        private ParcelFileDescriptor mRemotePipe;

        private FileWriterTask() {
        }

        /* Access modifiers changed, original: protected */
        public void onPreExecute() {
            try {
                ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
                this.mRemotePipe = pipe[0];
                this.mLocalPipe = pipe[1];
            } catch (IOException e) {
                Log.e(PermissionControllerManager.TAG, "Could not create pipe needed to send runtime permission backup", e);
            }
        }

        /* Access modifiers changed, original: 0000 */
        public ParcelFileDescriptor getRemotePipe() {
            return this.mRemotePipe;
        }

        /* Access modifiers changed, original: protected|varargs */
        /* JADX WARNING: Missing block: B:15:?, code skipped:
            r2.close();
     */
        public java.lang.Void doInBackground(byte[]... r6) {
            /*
            r5 = this;
            r0 = 0;
            r1 = r6[r0];
            r2 = new android.os.ParcelFileDescriptor$AutoCloseOutputStream;	 Catch:{ IOException | NullPointerException -> 0x002c, IOException | NullPointerException -> 0x002c }
            r3 = r5.mLocalPipe;	 Catch:{ IOException | NullPointerException -> 0x002c, IOException | NullPointerException -> 0x002c }
            r2.<init>(r3);	 Catch:{ IOException | NullPointerException -> 0x002c, IOException | NullPointerException -> 0x002c }
        L_0x000b:
            r3 = r1.length;	 Catch:{ all -> 0x0020 }
            if (r0 >= r3) goto L_0x001c;
        L_0x000e:
            r3 = r1.length;	 Catch:{ all -> 0x0020 }
            r3 = r3 - r0;
            r4 = 4096; // 0x1000 float:5.74E-42 double:2.0237E-320;
            r3 = java.lang.Math.min(r4, r3);	 Catch:{ all -> 0x0020 }
            r2.write(r1, r0, r3);	 Catch:{ all -> 0x0020 }
            r0 = r0 + 4096;
            goto L_0x000b;
        L_0x001c:
            r2.close();	 Catch:{ IOException | NullPointerException -> 0x002c, IOException | NullPointerException -> 0x002c }
            goto L_0x0036;
        L_0x0020:
            r0 = move-exception;
            throw r0;	 Catch:{ all -> 0x0022 }
        L_0x0022:
            r3 = move-exception;
            r2.close();	 Catch:{ all -> 0x0027 }
            goto L_0x002b;
        L_0x0027:
            r4 = move-exception;
            r0.addSuppressed(r4);	 Catch:{ IOException | NullPointerException -> 0x002c, IOException | NullPointerException -> 0x002c }
        L_0x002b:
            throw r3;	 Catch:{ IOException | NullPointerException -> 0x002c, IOException | NullPointerException -> 0x002c }
        L_0x002c:
            r0 = move-exception;
            r2 = android.permission.PermissionControllerManager.TAG;
            r3 = "Error sending runtime permission backup";
            android.util.Log.e(r2, r3, r0);
        L_0x0036:
            r0 = 0;
            return r0;
            */
            throw new UnsupportedOperationException("Method not decompiled: android.permission.PermissionControllerManager$FileWriterTask.doInBackground(byte[][]):java.lang.Void");
        }

        /* Access modifiers changed, original: 0000 */
        public void interruptWrite() {
            IoUtils.closeQuietly(this.mLocalPipe);
        }

        /* Access modifiers changed, original: protected */
        public void onCancelled() {
            onPostExecute(null);
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Void ignored) {
            IoUtils.closeQuietly(this.mLocalPipe);
        }
    }

    public interface OnCountPermissionAppsResultCallback {
        void onCountPermissionApps(int i);
    }

    public interface OnGetAppPermissionResultCallback {
        void onGetAppPermissions(List<RuntimePermissionPresentationInfo> list);
    }

    public interface OnGetRuntimePermissionBackupCallback {
        void onGetRuntimePermissionsBackup(byte[] bArr);
    }

    public interface OnPermissionUsageResultCallback {
        void onPermissionUsageResult(List<RuntimePermissionUsageInfo> list);
    }

    public static abstract class OnRevokeRuntimePermissionsCallback {
        public abstract void onRevokeRuntimePermissions(Map<String, List<String>> map);
    }

    private static final class PendingCountPermissionAppsRequest extends PendingRequest<RemoteService, IPermissionController> {
        private final OnCountPermissionAppsResultCallback mCallback;
        private final int mFlags;
        private final List<String> mPermissionNames;
        private final RemoteCallback mRemoteCallback;

        private PendingCountPermissionAppsRequest(RemoteService service, List<String> permissionNames, int flags, OnCountPermissionAppsResultCallback callback, Handler handler) {
            super(service);
            this.mPermissionNames = permissionNames;
            this.mFlags = flags;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$PermissionControllerManager$PendingCountPermissionAppsRequest$5yk4p2I96nUHJ1QRErjoF1iiLLY(this, callback), handler);
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingCountPermissionAppsRequest(OnCountPermissionAppsResultCallback callback, Bundle result) {
            int numApps;
            if (result != null) {
                numApps = result.getInt(PermissionControllerManager.KEY_RESULT);
            } else {
                numApps = 0;
            }
            callback.onCountPermissionApps(numApps);
            finish();
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.onCountPermissionApps(0);
        }

        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).countPermissionApps(this.mPermissionNames, this.mFlags, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error counting permission apps", e);
            }
        }
    }

    private static final class PendingGetAppPermissionRequest extends PendingRequest<RemoteService, IPermissionController> {
        private final OnGetAppPermissionResultCallback mCallback;
        private final String mPackageName;
        private final RemoteCallback mRemoteCallback;

        private PendingGetAppPermissionRequest(RemoteService service, String packageName, OnGetAppPermissionResultCallback callback, Handler handler) {
            super(service);
            this.mPackageName = packageName;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$PermissionControllerManager$PendingGetAppPermissionRequest$7R0rGbvqPEHrjxlrMX66LMgfTj4(this, callback), handler);
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingGetAppPermissionRequest(OnGetAppPermissionResultCallback callback, Bundle result) {
            List<RuntimePermissionPresentationInfo> permissions = null;
            if (result != null) {
                permissions = result.getParcelableArrayList(PermissionControllerManager.KEY_RESULT);
            }
            if (permissions == null) {
                permissions = Collections.emptyList();
            }
            callback.onGetAppPermissions(permissions);
            finish();
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.onGetAppPermissions(Collections.emptyList());
        }

        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).getAppPermissions(this.mPackageName, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error getting app permission", e);
            }
        }
    }

    private static final class PendingGetPermissionUsagesRequest extends PendingRequest<RemoteService, IPermissionController> {
        private final OnPermissionUsageResultCallback mCallback;
        private final boolean mCountSystem;
        private final long mNumMillis;
        private final RemoteCallback mRemoteCallback;

        private PendingGetPermissionUsagesRequest(RemoteService service, boolean countSystem, long numMillis, Executor executor, OnPermissionUsageResultCallback callback) {
            super(service);
            this.mCountSystem = countSystem;
            this.mNumMillis = numMillis;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$PermissionControllerManager$PendingGetPermissionUsagesRequest$M0RAdfneqBIIFQEhfWzd068mi7g(this, executor, callback), null);
        }

        public /* synthetic */ void lambda$new$1$PermissionControllerManager$PendingGetPermissionUsagesRequest(Executor executor, OnPermissionUsageResultCallback callback, Bundle result) {
            executor.execute(new -$$Lambda$PermissionControllerManager$PendingGetPermissionUsagesRequest$WBIc65bpG47GE1DYeIzY6NX7Oyw(this, result, callback));
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingGetPermissionUsagesRequest(Bundle result, OnPermissionUsageResultCallback callback) {
            List<RuntimePermissionUsageInfo> users;
            long token = Binder.clearCallingIdentity();
            if (result != null) {
                try {
                    users = result.getParcelableArrayList(PermissionControllerManager.KEY_RESULT);
                } catch (Throwable th) {
                    Binder.restoreCallingIdentity(token);
                    finish();
                }
            } else {
                users = Collections.emptyList();
            }
            callback.onPermissionUsageResult(users);
            Binder.restoreCallingIdentity(token);
            finish();
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.onPermissionUsageResult(Collections.emptyList());
        }

        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).getPermissionUsages(this.mCountSystem, this.mNumMillis, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error counting permission users", e);
            }
        }
    }

    private static final class PendingGetRuntimePermissionBackup extends PendingRequest<RemoteService, IPermissionController> implements Consumer<byte[]> {
        private final FileReaderTask<PendingGetRuntimePermissionBackup> mBackupReader;
        private final OnGetRuntimePermissionBackupCallback mCallback;
        private final Executor mExecutor;
        private final UserHandle mUser;

        private PendingGetRuntimePermissionBackup(RemoteService service, UserHandle user, Executor executor, OnGetRuntimePermissionBackupCallback callback) {
            super(service);
            this.mUser = user;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mBackupReader = new FileReaderTask(this);
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            this.mBackupReader.cancel(true);
            this.mBackupReader.interruptRead();
        }

        public void run() {
            if (this.mBackupReader.getStatus() == Status.PENDING) {
                this.mBackupReader.execute((Object[]) new Void[0]);
                ParcelFileDescriptor remotePipe = this.mBackupReader.getRemotePipe();
                try {
                    ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).getRuntimePermissionBackup(this.mUser, remotePipe);
                } catch (RemoteException e) {
                    Log.e(PermissionControllerManager.TAG, "Error getting runtime permission backup", e);
                } catch (Throwable th) {
                    IoUtils.closeQuietly(remotePipe);
                }
                IoUtils.closeQuietly(remotePipe);
            }
        }

        public void accept(byte[] backup) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new -$$Lambda$PermissionControllerManager$PendingGetRuntimePermissionBackup$TnLX6gxZCMF3D0czwj_XwNhPIgE(this, backup));
                finish();
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        public /* synthetic */ void lambda$accept$0$PermissionControllerManager$PendingGetRuntimePermissionBackup(byte[] backup) {
            this.mCallback.onGetRuntimePermissionsBackup(backup);
        }
    }

    private static final class PendingGrantOrUpgradeDefaultRuntimePermissionsRequest extends PendingRequest<RemoteService, IPermissionController> {
        private final Consumer<Boolean> mCallback;
        private final RemoteCallback mRemoteCallback;

        private PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(RemoteService service, Executor executor, Consumer<Boolean> callback) {
            super(service);
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest$khE8_2qLkPzjjwzPXI9vCg1JiSo(this, executor, callback), null);
        }

        public /* synthetic */ void lambda$new$1$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(Executor executor, Consumer callback, Bundle result) {
            executor.execute(new -$$Lambda$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest$LF2T0wqhyO211uMsePvWLLBRNHc(this, callback, result));
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(Consumer callback, Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                callback.accept(Boolean.valueOf(result != null));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mCallback.accept(Boolean.valueOf(false));
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).grantOrUpgradeDefaultRuntimePermissions(this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error granting or upgrading runtime permissions", e);
            }
        }
    }

    private static final class PendingRestoreDelayedRuntimePermissionBackup extends PendingRequest<RemoteService, IPermissionController> {
        private final Consumer<Boolean> mCallback;
        private final Executor mExecutor;
        private final String mPackageName;
        private final RemoteCallback mRemoteCallback;
        private final UserHandle mUser;

        private PendingRestoreDelayedRuntimePermissionBackup(RemoteService service, String packageName, UserHandle user, Executor executor, Consumer<Boolean> callback) {
            super(service);
            this.mPackageName = packageName;
            this.mUser = user;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$S_BIiPaqfMH7CNqPH_RO6xHRCeQ(this, executor, callback), null);
        }

        public /* synthetic */ void lambda$new$1$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup(Executor executor, Consumer callback, Bundle result) {
            executor.execute(new -$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$ZGmiW-2RcTI6YZLE1JgWr0ufJGk(this, callback, result));
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup(Consumer callback, Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                callback.accept(Boolean.valueOf(result.getBoolean(PermissionControllerManager.KEY_RESULT, false)));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new -$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$eZmglu-5wkoNFQT0fHebFoNMze8(this));
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup() {
            this.mCallback.accept(Boolean.valueOf(true));
        }

        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).restoreDelayedRuntimePermissionBackup(this.mPackageName, this.mUser, this.mRemoteCallback);
            } catch (RemoteException e) {
                String access$1000 = PermissionControllerManager.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error restoring delayed permissions for ");
                stringBuilder.append(this.mPackageName);
                Log.e(access$1000, stringBuilder.toString(), e);
            }
        }
    }

    private static final class PendingRestoreRuntimePermissionBackup implements AsyncRequest<IPermissionController> {
        private final byte[] mBackup;
        private final FileWriterTask mBackupSender;
        private final UserHandle mUser;

        private PendingRestoreRuntimePermissionBackup(RemoteService service, byte[] backup, UserHandle user) {
            this.mBackup = backup;
            this.mUser = user;
            this.mBackupSender = new FileWriterTask();
        }

        public void run(IPermissionController service) {
            if (this.mBackupSender.getStatus() == Status.PENDING) {
                this.mBackupSender.execute((Object[]) new byte[][]{this.mBackup});
                ParcelFileDescriptor remotePipe = this.mBackupSender.getRemotePipe();
                try {
                    service.restoreRuntimePermissionBackup(this.mUser, remotePipe);
                } catch (RemoteException e) {
                    Log.e(PermissionControllerManager.TAG, "Error sending runtime permission backup", e);
                    this.mBackupSender.cancel(false);
                    this.mBackupSender.interruptWrite();
                } catch (Throwable th) {
                    IoUtils.closeQuietly(remotePipe);
                }
                IoUtils.closeQuietly(remotePipe);
            }
        }
    }

    private static final class PendingRevokeAppPermissionRequest implements AsyncRequest<IPermissionController> {
        private final String mPackageName;
        private final String mPermissionName;

        private PendingRevokeAppPermissionRequest(String packageName, String permissionName) {
            this.mPackageName = packageName;
            this.mPermissionName = permissionName;
        }

        public void run(IPermissionController remoteInterface) {
            try {
                remoteInterface.revokeRuntimePermission(this.mPackageName, this.mPermissionName);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error revoking app permission", e);
            }
        }
    }

    private static final class PendingRevokeRuntimePermissionRequest extends PendingRequest<RemoteService, IPermissionController> {
        private final OnRevokeRuntimePermissionsCallback mCallback;
        private final String mCallingPackage;
        private final boolean mDoDryRun;
        private final Executor mExecutor;
        private final int mReason;
        private final RemoteCallback mRemoteCallback;
        private final Map<String, List<String>> mRequest;

        private PendingRevokeRuntimePermissionRequest(RemoteService service, Map<String, List<String>> request, boolean doDryRun, int reason, String callingPackage, Executor executor, OnRevokeRuntimePermissionsCallback callback) {
            super(service);
            this.mRequest = request;
            this.mDoDryRun = doDryRun;
            this.mReason = reason;
            this.mCallingPackage = callingPackage;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$PermissionControllerManager$PendingRevokeRuntimePermissionRequest$StUWUj0fmNRuCwuUzh3M5C7e_o0(this, executor, callback), null);
        }

        public /* synthetic */ void lambda$new$1$PermissionControllerManager$PendingRevokeRuntimePermissionRequest(Executor executor, OnRevokeRuntimePermissionsCallback callback, Bundle result) {
            executor.execute(new -$$Lambda$PermissionControllerManager$PendingRevokeRuntimePermissionRequest$RY69_9rYfdoaXdLj_Ux-62tZUXg(this, result, callback));
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingRevokeRuntimePermissionRequest(Bundle result, OnRevokeRuntimePermissionsCallback callback) {
            Map<String, List<String>> revoked;
            long token = Binder.clearCallingIdentity();
            try {
                revoked = new ArrayMap();
                Bundle bundleizedRevoked = result.getBundle(PermissionControllerManager.KEY_RESULT);
                for (String packageName : bundleizedRevoked.keySet()) {
                    Preconditions.checkNotNull(packageName);
                    ArrayList<String> permissions = bundleizedRevoked.getStringArrayList(packageName);
                    Preconditions.checkCollectionElementsNotNull(permissions, "permissions");
                    revoked.put(packageName, permissions);
                }
            } catch (Exception e) {
                Log.e(PermissionControllerManager.TAG, "Could not read result when revoking runtime permissions", e);
            } catch (Throwable th) {
                Binder.restoreCallingIdentity(token);
                finish();
            }
            callback.onRevokeRuntimePermissions(revoked);
            Binder.restoreCallingIdentity(token);
            finish();
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new -$$Lambda$PermissionControllerManager$PendingRevokeRuntimePermissionRequest$HQXgA6xx0k7jv6y22RQn3Fx34QQ(this));
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$PermissionControllerManager$PendingRevokeRuntimePermissionRequest() {
            this.mCallback.onRevokeRuntimePermissions(Collections.emptyMap());
        }

        public void run() {
            Bundle bundledizedRequest = new Bundle();
            for (Entry<String, List<String>> appRequest : this.mRequest.entrySet()) {
                bundledizedRequest.putStringArrayList((String) appRequest.getKey(), new ArrayList((Collection) appRequest.getValue()));
            }
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).revokeRuntimePermissions(bundledizedRequest, this.mDoDryRun, this.mReason, this.mCallingPackage, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(PermissionControllerManager.TAG, "Error revoking runtime permission", e);
            }
        }
    }

    private static final class PendingSetRuntimePermissionGrantStateByDeviceAdmin extends PendingRequest<RemoteService, IPermissionController> {
        private final Consumer<Boolean> mCallback;
        private final String mCallerPackageName;
        private final Executor mExecutor;
        private final int mGrantState;
        private final String mPackageName;
        private final String mPermission;
        private final RemoteCallback mRemoteCallback;

        private PendingSetRuntimePermissionGrantStateByDeviceAdmin(RemoteService service, String callerPackageName, String packageName, String permission, int grantState, Executor executor, Consumer<Boolean> callback) {
            super(service);
            this.mCallerPackageName = callerPackageName;
            this.mPackageName = packageName;
            this.mPermission = permission;
            this.mGrantState = grantState;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin$9CrKvc4Mj43M641VzAbk1z_vjck(this, executor, callback), null);
        }

        public /* synthetic */ void lambda$new$1$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin(Executor executor, Consumer callback, Bundle result) {
            executor.execute(new -$$Lambda$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin$L3EtiNpasfEGf-E2sSUKhk-dYUg(this, callback, result));
        }

        public /* synthetic */ void lambda$new$0$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin(Consumer callback, Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                callback.accept(Boolean.valueOf(result.getBoolean(PermissionControllerManager.KEY_RESULT, false)));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mExecutor.execute(new -$$Lambda$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin$cgbsG1socgf6wsJmCUAPmh-jKmw(this));
            } finally {
                Binder.restoreCallingIdentity(token);
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin() {
            this.mCallback.accept(Boolean.valueOf(false));
        }

        public void run() {
            try {
                ((IPermissionController) ((RemoteService) getService()).getServiceInterface()).setRuntimePermissionGrantStateByDeviceAdmin(this.mCallerPackageName, this.mPackageName, this.mPermission, this.mGrantState, this.mRemoteCallback);
            } catch (RemoteException e) {
                String access$1000 = PermissionControllerManager.TAG;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("Error setting permissions state for device admin ");
                stringBuilder.append(this.mPackageName);
                Log.e(access$1000, stringBuilder.toString(), e);
            }
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Reason {
    }

    static final class RemoteService extends AbstractMultiplePendingRequestsRemoteService<RemoteService, IPermissionController> {
        private static final long MESSAGE_TIMEOUT_MILLIS = 30000;
        private static final long UNBIND_TIMEOUT_MILLIS = 10000;

        RemoteService(Context context, ComponentName componentName, Handler handler, UserHandle user) {
            Context context2 = context;
            super(context2, PermissionControllerService.SERVICE_INTERFACE, componentName, user.getIdentifier(), -$$Lambda$PermissionControllerManager$RemoteService$L8N-TbqIPWKu7tyiOxbu_00YKss.INSTANCE, handler, 0, false, 1);
        }

        static /* synthetic */ void lambda$new$0(RemoteService service) {
            String access$1000 = PermissionControllerManager.TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RemoteService ");
            stringBuilder.append(service);
            stringBuilder.append(" died");
            Log.e(access$1000, stringBuilder.toString());
        }

        /* Access modifiers changed, original: 0000 */
        public Handler getHandler() {
            return this.mHandler;
        }

        /* Access modifiers changed, original: protected */
        public IPermissionController getServiceInterface(IBinder binder) {
            return Stub.asInterface(binder);
        }

        /* Access modifiers changed, original: protected */
        public long getTimeoutIdleBindMillis() {
            return 10000;
        }

        /* Access modifiers changed, original: protected */
        public long getRemoteRequestMillis() {
            return 30000;
        }

        public void scheduleRequest(BasePendingRequest<RemoteService, IPermissionController> pendingRequest) {
            super.scheduleRequest(pendingRequest);
        }

        public void scheduleAsyncRequest(AsyncRequest<IPermissionController> request) {
            super.scheduleAsyncRequest(request);
        }
    }

    public PermissionControllerManager(Context context, Handler handler) {
        synchronized (sLock) {
            Pair<Integer, Thread> key = new Pair(Integer.valueOf(context.getUserId()), handler.getLooper().getThread());
            RemoteService remoteService = (RemoteService) sRemoteServices.get(key);
            if (remoteService == null) {
                Intent intent = new Intent(PermissionControllerService.SERVICE_INTERFACE);
                intent.setPackage(context.getPackageManager().getPermissionControllerPackageName());
                remoteService = new RemoteService(ActivityThread.currentApplication(), context.getPackageManager().resolveService(intent, 0).getComponentInfo().getComponentName(), handler, context.getUser());
                sRemoteServices.put(key, remoteService);
            }
            this.mRemoteService = remoteService;
        }
        this.mContext = context;
    }

    public void revokeRuntimePermissions(Map<String, List<String>> request, boolean doDryRun, int reason, Executor executor, OnRevokeRuntimePermissionsCallback callback) {
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(callback);
        Preconditions.checkNotNull(request);
        for (Entry<String, List<String>> appRequest : request.entrySet()) {
            Preconditions.checkNotNull((String) appRequest.getKey());
            Preconditions.checkCollectionElementsNotNull((List) appRequest.getValue(), "permissions");
        }
        if (this.mContext.checkSelfPermission(permission.REVOKE_RUNTIME_PERMISSIONS) == 0) {
            RemoteService remoteService = this.mRemoteService;
            remoteService.scheduleRequest(new PendingRevokeRuntimePermissionRequest(remoteService, request, doDryRun, reason, this.mContext.getPackageName(), executor, callback));
            return;
        }
        throw new SecurityException("android.permission.REVOKE_RUNTIME_PERMISSIONS required");
    }

    public void setRuntimePermissionGrantStateByDeviceAdmin(String callerPackageName, String packageName, String permission, int grantState, Executor executor, Consumer<Boolean> callback) {
        int i = grantState;
        Preconditions.checkStringNotEmpty(callerPackageName);
        Preconditions.checkStringNotEmpty(packageName);
        Preconditions.checkStringNotEmpty(permission);
        boolean z = true;
        if (!(i == 1 || i == 2 || i == 0)) {
            z = false;
        }
        Preconditions.checkArgument(z);
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingSetRuntimePermissionGrantStateByDeviceAdmin(remoteService, callerPackageName, packageName, permission, grantState, executor, callback));
    }

    public void getRuntimePermissionBackup(UserHandle user, Executor executor, OnGetRuntimePermissionBackupCallback callback) {
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingGetRuntimePermissionBackup(remoteService, user, executor, callback));
    }

    public void restoreRuntimePermissionBackup(byte[] backup, UserHandle user) {
        Preconditions.checkNotNull(backup);
        Preconditions.checkNotNull(user);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleAsyncRequest(new PendingRestoreRuntimePermissionBackup(remoteService, backup, user));
    }

    public void restoreDelayedRuntimePermissionBackup(String packageName, UserHandle user, Executor executor, Consumer<Boolean> callback) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(user);
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingRestoreDelayedRuntimePermissionBackup(remoteService, packageName, user, executor, callback));
    }

    public void getAppPermissions(String packageName, OnGetAppPermissionResultCallback callback, Handler handler) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingGetAppPermissionRequest(remoteService, packageName, callback, handler == null ? remoteService.getHandler() : handler));
    }

    public void revokeRuntimePermission(String packageName, String permissionName) {
        Preconditions.checkNotNull(packageName);
        Preconditions.checkNotNull(permissionName);
        this.mRemoteService.scheduleAsyncRequest(new PendingRevokeAppPermissionRequest(packageName, permissionName));
    }

    public void countPermissionApps(List<String> permissionNames, int flags, OnCountPermissionAppsResultCallback callback, Handler handler) {
        Preconditions.checkCollectionElementsNotNull(permissionNames, "permissionNames");
        Preconditions.checkFlagsArgument(flags, 3);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingCountPermissionAppsRequest(remoteService, permissionNames, flags, callback, handler == null ? remoteService.getHandler() : handler));
    }

    public void getPermissionUsages(boolean countSystem, long numMillis, Executor executor, OnPermissionUsageResultCallback callback) {
        Preconditions.checkArgumentNonnegative(numMillis);
        Preconditions.checkNotNull(executor);
        Preconditions.checkNotNull(callback);
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingGetPermissionUsagesRequest(remoteService, countSystem, numMillis, executor, callback));
    }

    public void grantOrUpgradeDefaultRuntimePermissions(Executor executor, Consumer<Boolean> callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(remoteService, executor, callback));
    }
}
