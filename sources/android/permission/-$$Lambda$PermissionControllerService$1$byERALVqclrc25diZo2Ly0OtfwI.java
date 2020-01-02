package android.permission;

import android.os.RemoteCallback;
import android.permission.PermissionControllerService.AnonymousClass1;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerService$1$byERALVqclrc25diZo2Ly0OtfwI implements Consumer {
    private final /* synthetic */ RemoteCallback f$0;

    public /* synthetic */ -$$Lambda$PermissionControllerService$1$byERALVqclrc25diZo2Ly0OtfwI(RemoteCallback remoteCallback) {
        this.f$0 = remoteCallback;
    }

    public final void accept(Object obj) {
        AnonymousClass1.lambda$restoreDelayedRuntimePermissionBackup$1(this.f$0, (Boolean) obj);
    }
}
