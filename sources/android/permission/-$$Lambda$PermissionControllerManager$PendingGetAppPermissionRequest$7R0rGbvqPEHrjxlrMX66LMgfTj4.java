package android.permission;

import android.os.Bundle;
import android.os.RemoteCallback.OnResultListener;
import android.permission.PermissionControllerManager.OnGetAppPermissionResultCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingGetAppPermissionRequest$7R0rGbvqPEHrjxlrMX66LMgfTj4 implements OnResultListener {
    private final /* synthetic */ PendingGetAppPermissionRequest f$0;
    private final /* synthetic */ OnGetAppPermissionResultCallback f$1;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingGetAppPermissionRequest$7R0rGbvqPEHrjxlrMX66LMgfTj4(PendingGetAppPermissionRequest pendingGetAppPermissionRequest, OnGetAppPermissionResultCallback onGetAppPermissionResultCallback) {
        this.f$0 = pendingGetAppPermissionRequest;
        this.f$1 = onGetAppPermissionResultCallback;
    }

    public final void onResult(Bundle bundle) {
        this.f$0.lambda$new$0$PermissionControllerManager$PendingGetAppPermissionRequest(this.f$1, bundle);
    }
}
