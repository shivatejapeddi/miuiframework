package android.permission;

import android.os.Bundle;
import android.permission.PermissionControllerManager.OnPermissionUsageResultCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingGetPermissionUsagesRequest$WBIc65bpG47GE1DYeIzY6NX7Oyw implements Runnable {
    private final /* synthetic */ PendingGetPermissionUsagesRequest f$0;
    private final /* synthetic */ Bundle f$1;
    private final /* synthetic */ OnPermissionUsageResultCallback f$2;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingGetPermissionUsagesRequest$WBIc65bpG47GE1DYeIzY6NX7Oyw(PendingGetPermissionUsagesRequest pendingGetPermissionUsagesRequest, Bundle bundle, OnPermissionUsageResultCallback onPermissionUsageResultCallback) {
        this.f$0 = pendingGetPermissionUsagesRequest;
        this.f$1 = bundle;
        this.f$2 = onPermissionUsageResultCallback;
    }

    public final void run() {
        this.f$0.lambda$new$0$PermissionControllerManager$PendingGetPermissionUsagesRequest(this.f$1, this.f$2);
    }
}
