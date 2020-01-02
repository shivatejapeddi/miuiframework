package android.permission;

import android.os.Bundle;
import android.os.RemoteCallback.OnResultListener;
import android.permission.PermissionControllerManager.OnRevokeRuntimePermissionsCallback;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingRevokeRuntimePermissionRequest$StUWUj0fmNRuCwuUzh3M5C7e_o0 implements OnResultListener {
    private final /* synthetic */ PendingRevokeRuntimePermissionRequest f$0;
    private final /* synthetic */ Executor f$1;
    private final /* synthetic */ OnRevokeRuntimePermissionsCallback f$2;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingRevokeRuntimePermissionRequest$StUWUj0fmNRuCwuUzh3M5C7e_o0(PendingRevokeRuntimePermissionRequest pendingRevokeRuntimePermissionRequest, Executor executor, OnRevokeRuntimePermissionsCallback onRevokeRuntimePermissionsCallback) {
        this.f$0 = pendingRevokeRuntimePermissionRequest;
        this.f$1 = executor;
        this.f$2 = onRevokeRuntimePermissionsCallback;
    }

    public final void onResult(Bundle bundle) {
        this.f$0.lambda$new$1$PermissionControllerManager$PendingRevokeRuntimePermissionRequest(this.f$1, this.f$2, bundle);
    }
}
