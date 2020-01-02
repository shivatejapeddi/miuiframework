package android.permission;

import android.os.Bundle;
import android.os.RemoteCallback.OnResultListener;
import android.permission.PermissionControllerManager.OnCountPermissionAppsResultCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingCountPermissionAppsRequest$5yk4p2I96nUHJ1QRErjoF1iiLLY implements OnResultListener {
    private final /* synthetic */ PendingCountPermissionAppsRequest f$0;
    private final /* synthetic */ OnCountPermissionAppsResultCallback f$1;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingCountPermissionAppsRequest$5yk4p2I96nUHJ1QRErjoF1iiLLY(PendingCountPermissionAppsRequest pendingCountPermissionAppsRequest, OnCountPermissionAppsResultCallback onCountPermissionAppsResultCallback) {
        this.f$0 = pendingCountPermissionAppsRequest;
        this.f$1 = onCountPermissionAppsResultCallback;
    }

    public final void onResult(Bundle bundle) {
        this.f$0.lambda$new$0$PermissionControllerManager$PendingCountPermissionAppsRequest(this.f$1, bundle);
    }
}
