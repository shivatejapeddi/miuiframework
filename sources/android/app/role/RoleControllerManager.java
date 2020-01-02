package android.app.role;

import android.app.ActivityThread;
import android.app.role.IRoleController.Stub;
import android.app.role.RoleManager.ManageHoldersFlags;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallback;
import android.os.RemoteException;
import android.util.Log;
import android.util.SparseArray;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.infra.AbstractMultiplePendingRequestsRemoteService;
import com.android.internal.infra.AbstractRemoteService.AsyncRequest;
import com.android.internal.infra.AbstractRemoteService.BasePendingRequest;
import com.android.internal.infra.AbstractRemoteService.PendingRequest;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class RoleControllerManager {
    private static final String LOG_TAG = RoleControllerManager.class.getSimpleName();
    private static volatile ComponentName sRemoteServiceComponentName;
    @GuardedBy({"sRemoteServicesLock"})
    private static final SparseArray<RemoteService> sRemoteServices = new SparseArray();
    private static final Object sRemoteServicesLock = new Object();
    private final RemoteService mRemoteService;

    private static final class GrantDefaultRolesRequest extends PendingRequest<RemoteService, IRoleController> {
        private final Consumer<Boolean> mCallback;
        private final Executor mExecutor;
        private final RemoteCallback mRemoteCallback;

        private GrantDefaultRolesRequest(RemoteService service, Executor executor, Consumer<Boolean> callback) {
            super(service);
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$RoleControllerManager$GrantDefaultRolesRequest$uMND2yv3BzXWyrtureF8K8b0f0A(this));
        }

        public /* synthetic */ void lambda$new$1$RoleControllerManager$GrantDefaultRolesRequest(Bundle result) {
            this.mExecutor.execute(new -$$Lambda$RoleControllerManager$GrantDefaultRolesRequest$Qrnu382yknLH4_TvruMvYuK_N8M(this, result));
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$GrantDefaultRolesRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mCallback.accept(Boolean.valueOf(result != null));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$RoleControllerManager$GrantDefaultRolesRequest() {
            this.mCallback.accept(Boolean.valueOf(false));
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            this.mExecutor.execute(new -$$Lambda$RoleControllerManager$GrantDefaultRolesRequest$0iOorSSTMKMxorImfJcxQ8hscBs(this));
        }

        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).grantDefaultRoles(this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling grantDefaultRoles()", e);
            }
        }

        /* Access modifiers changed, original: protected */
        public void onFailed() {
            this.mRemoteCallback.sendResult(null);
        }
    }

    private static final class IsApplicationQualifiedForRoleRequest extends PendingRequest<RemoteService, IRoleController> {
        private final Consumer<Boolean> mCallback;
        private final Executor mExecutor;
        private final String mPackageName;
        private final RemoteCallback mRemoteCallback;
        private final String mRoleName;

        private IsApplicationQualifiedForRoleRequest(RemoteService service, String roleName, String packageName, Executor executor, Consumer<Boolean> callback) {
            super(service);
            this.mRoleName = roleName;
            this.mPackageName = packageName;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$RoleControllerManager$IsApplicationQualifiedForRoleRequest$YqB5KyJlcDUM5urf3ImMD1odxhI(this));
        }

        public /* synthetic */ void lambda$new$1$RoleControllerManager$IsApplicationQualifiedForRoleRequest(Bundle result) {
            this.mExecutor.execute(new -$$Lambda$RoleControllerManager$IsApplicationQualifiedForRoleRequest$pbhRqekkSEnYlxVcT_rMcU6hV-E(this, result));
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$IsApplicationQualifiedForRoleRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mCallback.accept(Boolean.valueOf(result != null));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$RoleControllerManager$IsApplicationQualifiedForRoleRequest() {
            this.mCallback.accept(Boolean.valueOf(false));
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            this.mExecutor.execute(new -$$Lambda$RoleControllerManager$IsApplicationQualifiedForRoleRequest$9YPce2vGDOZP97XHsgR7kBf64jQ(this));
        }

        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).isApplicationQualifiedForRole(this.mRoleName, this.mPackageName, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling isApplicationQualifiedForRole()", e);
            }
        }
    }

    private static final class IsRoleVisibleRequest extends PendingRequest<RemoteService, IRoleController> {
        private final Consumer<Boolean> mCallback;
        private final Executor mExecutor;
        private final RemoteCallback mRemoteCallback;
        private final String mRoleName;

        private IsRoleVisibleRequest(RemoteService service, String roleName, Executor executor, Consumer<Boolean> callback) {
            super(service);
            this.mRoleName = roleName;
            this.mExecutor = executor;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$RoleControllerManager$IsRoleVisibleRequest$oEPzdmOwBqsdvIknZm3f9_oOiE8(this));
        }

        public /* synthetic */ void lambda$new$1$RoleControllerManager$IsRoleVisibleRequest(Bundle result) {
            this.mExecutor.execute(new -$$Lambda$RoleControllerManager$IsRoleVisibleRequest$i7aWmxVK8GGR464ms-cqfIN7ou8(this, result));
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$IsRoleVisibleRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mCallback.accept(Boolean.valueOf(result != null));
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        public /* synthetic */ void lambda$onTimeout$2$RoleControllerManager$IsRoleVisibleRequest() {
            this.mCallback.accept(Boolean.valueOf(false));
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            this.mExecutor.execute(new -$$Lambda$RoleControllerManager$IsRoleVisibleRequest$mPvdI6Jc9sQbLKyjDLv3TR6mmlM(this));
        }

        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).isRoleVisible(this.mRoleName, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling isRoleVisible()", e);
            }
        }
    }

    private static final class OnAddRoleHolderRequest extends PendingRequest<RemoteService, IRoleController> {
        private final RemoteCallback mCallback;
        @ManageHoldersFlags
        private final int mFlags;
        private final String mPackageName;
        private final RemoteCallback mRemoteCallback;
        private final String mRoleName;

        private OnAddRoleHolderRequest(RemoteService service, String roleName, String packageName, @ManageHoldersFlags int flags, RemoteCallback callback) {
            super(service);
            this.mRoleName = roleName;
            this.mPackageName = packageName;
            this.mFlags = flags;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$RoleControllerManager$OnAddRoleHolderRequest$JT1k7eyE31b1Ili2aD3HPTU4d_Y(this));
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$OnAddRoleHolderRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mCallback.sendResult(result);
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.sendResult(null);
        }

        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).onAddRoleHolder(this.mRoleName, this.mPackageName, this.mFlags, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling onAddRoleHolder()", e);
            }
        }
    }

    private static final class OnClearRoleHoldersRequest extends PendingRequest<RemoteService, IRoleController> {
        private final RemoteCallback mCallback;
        @ManageHoldersFlags
        private final int mFlags;
        private final RemoteCallback mRemoteCallback;
        private final String mRoleName;

        private OnClearRoleHoldersRequest(RemoteService service, String roleName, @ManageHoldersFlags int flags, RemoteCallback callback) {
            super(service);
            this.mRoleName = roleName;
            this.mFlags = flags;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$RoleControllerManager$OnClearRoleHoldersRequest$WFtkA3AVOOzGz5tXwMpks5Iic-o(this));
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$OnClearRoleHoldersRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mCallback.sendResult(result);
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.sendResult(null);
        }

        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).onClearRoleHolders(this.mRoleName, this.mFlags, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling onClearRoleHolders()", e);
            }
        }
    }

    private static final class OnRemoveRoleHolderRequest extends PendingRequest<RemoteService, IRoleController> {
        private final RemoteCallback mCallback;
        @ManageHoldersFlags
        private final int mFlags;
        private final String mPackageName;
        private final RemoteCallback mRemoteCallback;
        private final String mRoleName;

        private OnRemoveRoleHolderRequest(RemoteService service, String roleName, String packageName, @ManageHoldersFlags int flags, RemoteCallback callback) {
            super(service);
            this.mRoleName = roleName;
            this.mPackageName = packageName;
            this.mFlags = flags;
            this.mCallback = callback;
            this.mRemoteCallback = new RemoteCallback(new -$$Lambda$RoleControllerManager$OnRemoveRoleHolderRequest$LtJIC2bE0p8jKF_FXl69Scqp5HE(this));
        }

        public /* synthetic */ void lambda$new$0$RoleControllerManager$OnRemoveRoleHolderRequest(Bundle result) {
            long token = Binder.clearCallingIdentity();
            try {
                this.mCallback.sendResult(result);
            } finally {
                Binder.restoreCallingIdentity(token);
                finish();
            }
        }

        /* Access modifiers changed, original: protected */
        public void onTimeout(RemoteService remoteService) {
            this.mCallback.sendResult(null);
        }

        public void run() {
            try {
                ((IRoleController) ((RemoteService) getService()).getServiceInterface()).onRemoveRoleHolder(this.mRoleName, this.mPackageName, this.mFlags, this.mRemoteCallback);
            } catch (RemoteException e) {
                Log.e(RoleControllerManager.LOG_TAG, "Error calling onRemoveRoleHolder()", e);
            }
        }
    }

    private static final class RemoteService extends AbstractMultiplePendingRequestsRemoteService<RemoteService, IRoleController> {
        private static final long REQUEST_TIMEOUT_MILLIS = 15000;
        private static final long UNBIND_DELAY_MILLIS = 15000;

        RemoteService(Context context, ComponentName componentName, Handler handler, int userId) {
            Context context2 = context;
            super(context2, RoleControllerService.SERVICE_INTERFACE, componentName, userId, -$$Lambda$RoleControllerManager$RemoteService$45dMO3SdHJhfBB_YKrC44Sznmoo.INSTANCE, handler, 0, false, 1);
        }

        static /* synthetic */ void lambda$new$0(RemoteService service) {
            String access$600 = RoleControllerManager.LOG_TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("RemoteService ");
            stringBuilder.append(service);
            stringBuilder.append(" died");
            Log.e(access$600, stringBuilder.toString());
        }

        public Handler getHandler() {
            return this.mHandler;
        }

        /* Access modifiers changed, original: protected */
        public IRoleController getServiceInterface(IBinder binder) {
            return Stub.asInterface(binder);
        }

        /* Access modifiers changed, original: protected */
        public long getTimeoutIdleBindMillis() {
            return 15000;
        }

        /* Access modifiers changed, original: protected */
        public long getRemoteRequestMillis() {
            return 15000;
        }

        public void scheduleRequest(BasePendingRequest<RemoteService, IRoleController> pendingRequest) {
            super.scheduleRequest(pendingRequest);
        }

        public void scheduleAsyncRequest(AsyncRequest<IRoleController> request) {
            super.scheduleAsyncRequest(request);
        }
    }

    public static void initializeRemoteServiceComponentName(Context context) {
        sRemoteServiceComponentName = getRemoteServiceComponentName(context);
    }

    public static RoleControllerManager createWithInitializedRemoteServiceComponentName(Handler handler, Context context) {
        return new RoleControllerManager(sRemoteServiceComponentName, handler, context);
    }

    private RoleControllerManager(ComponentName remoteServiceComponentName, Handler handler, Context context) {
        synchronized (sRemoteServicesLock) {
            int userId = context.getUserId();
            RemoteService remoteService = (RemoteService) sRemoteServices.get(userId);
            if (remoteService == null) {
                remoteService = new RemoteService(ActivityThread.currentApplication(), remoteServiceComponentName, handler, userId);
                sRemoteServices.put(userId, remoteService);
            }
            this.mRemoteService = remoteService;
        }
    }

    public RoleControllerManager(Context context) {
        this(getRemoteServiceComponentName(context), context.getMainThreadHandler(), context);
    }

    private static ComponentName getRemoteServiceComponentName(Context context) {
        Intent intent = new Intent(RoleControllerService.SERVICE_INTERFACE);
        PackageManager packageManager = context.getPackageManager();
        intent.setPackage(packageManager.getPermissionControllerPackageName());
        return packageManager.resolveService(intent, null).getComponentInfo().getComponentName();
    }

    public void grantDefaultRoles(Executor executor, Consumer<Boolean> callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new GrantDefaultRolesRequest(remoteService, executor, callback));
    }

    public void onAddRoleHolder(String roleName, String packageName, @ManageHoldersFlags int flags, RemoteCallback callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new OnAddRoleHolderRequest(remoteService, roleName, packageName, flags, callback));
    }

    public void onRemoveRoleHolder(String roleName, String packageName, @ManageHoldersFlags int flags, RemoteCallback callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new OnRemoveRoleHolderRequest(remoteService, roleName, packageName, flags, callback));
    }

    public void onClearRoleHolders(String roleName, @ManageHoldersFlags int flags, RemoteCallback callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new OnClearRoleHoldersRequest(remoteService, roleName, flags, callback));
    }

    public void isApplicationQualifiedForRole(String roleName, String packageName, Executor executor, Consumer<Boolean> callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new IsApplicationQualifiedForRoleRequest(remoteService, roleName, packageName, executor, callback));
    }

    public void isRoleVisible(String roleName, Executor executor, Consumer<Boolean> callback) {
        RemoteService remoteService = this.mRemoteService;
        remoteService.scheduleRequest(new IsRoleVisibleRequest(remoteService, roleName, executor, callback));
    }
}
