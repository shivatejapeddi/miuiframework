package android.permission;

import android.os.Bundle;
import android.os.RemoteCallback.OnResultListener;
import android.permission.PermissionControllerManager.OnPermissionUsageResultCallback;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingGetPermissionUsagesRequest$M0RAdfneqBIIFQEhfWzd068mi7g implements OnResultListener {
    private final /* synthetic */ PendingGetPermissionUsagesRequest f$0;
    private final /* synthetic */ Executor f$1;
    private final /* synthetic */ OnPermissionUsageResultCallback f$2;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingGetPermissionUsagesRequest$M0RAdfneqBIIFQEhfWzd068mi7g(PendingGetPermissionUsagesRequest pendingGetPermissionUsagesRequest, Executor executor, OnPermissionUsageResultCallback onPermissionUsageResultCallback) {
        this.f$0 = pendingGetPermissionUsagesRequest;
        this.f$1 = executor;
        this.f$2 = onPermissionUsageResultCallback;
    }

    public final void onResult(Bundle bundle) {
        this.f$0.lambda$new$1$PermissionControllerManager$PendingGetPermissionUsagesRequest(this.f$1, this.f$2, bundle);
    }
}
