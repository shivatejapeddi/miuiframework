package android.permission;

import android.os.RemoteCallback;
import android.permission.PermissionControllerService.AnonymousClass1;
import java.util.function.IntConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerService$1$i3vGLgbFSsM1LDWQDjRkXStMIUE implements IntConsumer {
    private final /* synthetic */ RemoteCallback f$0;

    public /* synthetic */ -$$Lambda$PermissionControllerService$1$i3vGLgbFSsM1LDWQDjRkXStMIUE(RemoteCallback remoteCallback) {
        this.f$0 = remoteCallback;
    }

    public final void accept(int i) {
        AnonymousClass1.lambda$countPermissionApps$3(this.f$0, i);
    }
}
