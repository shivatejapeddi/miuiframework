package android.permission;

import android.os.Bundle;
import android.os.RemoteCallback.OnResultListener;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest$khE8_2qLkPzjjwzPXI9vCg1JiSo implements OnResultListener {
    private final /* synthetic */ PendingGrantOrUpgradeDefaultRuntimePermissionsRequest f$0;
    private final /* synthetic */ Executor f$1;
    private final /* synthetic */ Consumer f$2;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest$khE8_2qLkPzjjwzPXI9vCg1JiSo(PendingGrantOrUpgradeDefaultRuntimePermissionsRequest pendingGrantOrUpgradeDefaultRuntimePermissionsRequest, Executor executor, Consumer consumer) {
        this.f$0 = pendingGrantOrUpgradeDefaultRuntimePermissionsRequest;
        this.f$1 = executor;
        this.f$2 = consumer;
    }

    public final void onResult(Bundle bundle) {
        this.f$0.lambda$new$1$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(this.f$1, this.f$2, bundle);
    }
}
