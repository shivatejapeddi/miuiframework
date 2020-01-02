package android.permission;

import android.os.Bundle;
import android.permission.PermissionControllerManager.OnRevokeRuntimePermissionsCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingRevokeRuntimePermissionRequest$RY69_9rYfdoaXdLj_Ux-62tZUXg implements Runnable {
    private final /* synthetic */ PendingRevokeRuntimePermissionRequest f$0;
    private final /* synthetic */ Bundle f$1;
    private final /* synthetic */ OnRevokeRuntimePermissionsCallback f$2;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingRevokeRuntimePermissionRequest$RY69_9rYfdoaXdLj_Ux-62tZUXg(PendingRevokeRuntimePermissionRequest pendingRevokeRuntimePermissionRequest, Bundle bundle, OnRevokeRuntimePermissionsCallback onRevokeRuntimePermissionsCallback) {
        this.f$0 = pendingRevokeRuntimePermissionRequest;
        this.f$1 = bundle;
        this.f$2 = onRevokeRuntimePermissionsCallback;
    }

    public final void run() {
        this.f$0.lambda$new$0$PermissionControllerManager$PendingRevokeRuntimePermissionRequest(this.f$1, this.f$2);
    }
}
