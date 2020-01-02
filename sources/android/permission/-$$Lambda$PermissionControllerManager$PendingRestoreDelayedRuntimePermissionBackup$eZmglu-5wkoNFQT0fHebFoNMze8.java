package android.permission;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$eZmglu-5wkoNFQT0fHebFoNMze8 implements Runnable {
    private final /* synthetic */ PendingRestoreDelayedRuntimePermissionBackup f$0;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup$eZmglu-5wkoNFQT0fHebFoNMze8(PendingRestoreDelayedRuntimePermissionBackup pendingRestoreDelayedRuntimePermissionBackup) {
        this.f$0 = pendingRestoreDelayedRuntimePermissionBackup;
    }

    public final void run() {
        this.f$0.lambda$onTimeout$2$PermissionControllerManager$PendingRestoreDelayedRuntimePermissionBackup();
    }
}
