package android.permission;

import android.os.Bundle;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest$LF2T0wqhyO211uMsePvWLLBRNHc implements Runnable {
    private final /* synthetic */ PendingGrantOrUpgradeDefaultRuntimePermissionsRequest f$0;
    private final /* synthetic */ Consumer f$1;
    private final /* synthetic */ Bundle f$2;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest$LF2T0wqhyO211uMsePvWLLBRNHc(PendingGrantOrUpgradeDefaultRuntimePermissionsRequest pendingGrantOrUpgradeDefaultRuntimePermissionsRequest, Consumer consumer, Bundle bundle) {
        this.f$0 = pendingGrantOrUpgradeDefaultRuntimePermissionsRequest;
        this.f$1 = consumer;
        this.f$2 = bundle;
    }

    public final void run() {
        this.f$0.lambda$new$0$PermissionControllerManager$PendingGrantOrUpgradeDefaultRuntimePermissionsRequest(this.f$1, this.f$2);
    }
}
