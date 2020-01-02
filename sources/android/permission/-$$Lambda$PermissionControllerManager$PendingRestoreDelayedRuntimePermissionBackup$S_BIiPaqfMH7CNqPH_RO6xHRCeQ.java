package android.permission;

import android.os.Bundle;
import android.os.RemoteCallback.OnResultListener;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$S_BIiPaqfMH7CNqPH_RO6xHRCeQ implements OnResultListener {
    private final /* synthetic */ PendingRestoreDelayedRuntimePermissionBackup f$0;
    private final /* synthetic */ Executor f$1;
    private final /* synthetic */ Consumer f$2;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$S_BIiPaqfMH7CNqPH_RO6xHRCeQ(PendingRestoreDelayedRuntimePermissionBackup pendingRestoreDelayedRuntimePermissionBackup, Executor executor, Consumer consumer) {
        this.f$0 = pendingRestoreDelayedRuntimePermissionBackup;
        this.f$1 = executor;
        this.f$2 = consumer;
    }

    public final void onResult(Bundle bundle) {
        this.f$0.lambda$new$1$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup(this.f$1, this.f$2, bundle);
    }
}
