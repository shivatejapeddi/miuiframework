package android.permission;

import android.os.RemoteCallback;
import android.permission.PermissionControllerService.AnonymousClass1;
import java.util.Map;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerService$1$__ZsT0Jo3iLdGM0gy2UV6ea_oEw implements Consumer {
    private final /* synthetic */ RemoteCallback f$0;

    public /* synthetic */ -$$Lambda$PermissionControllerService$1$__ZsT0Jo3iLdGM0gy2UV6ea_oEw(RemoteCallback remoteCallback) {
        this.f$0 = remoteCallback;
    }

    public final void accept(Object obj) {
        AnonymousClass1.lambda$revokeRuntimePermissions$0(this.f$0, (Map) obj);
    }
}
