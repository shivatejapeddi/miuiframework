package android.permission;

import android.os.Bundle;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$ZGmiW-2RcTI6YZLE1JgWr0ufJGk implements Runnable {
    private final /* synthetic */ PendingRestoreDelayedRuntimePermissionBackup f$0;
    private final /* synthetic */ Consumer f$1;
    private final /* synthetic */ Bundle f$2;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$ZGmiW-2RcTI6YZLE1JgWr0ufJGk(PendingRestoreDelayedRuntimePermissionBackup pendingRestoreDelayedRuntimePermissionBackup, Consumer consumer, Bundle bundle) {
        this.f$0 = pendingRestoreDelayedRuntimePermissionBackup;
        this.f$1 = consumer;
        this.f$2 = bundle;
    }

    public final void run() {
        this.f$0.lambda$new$0$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup(this.f$1, this.f$2);
    }
}
