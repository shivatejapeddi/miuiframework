package android.permission;

import android.os.RemoteCallback;
import android.permission.PermissionControllerService.AnonymousClass1;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerService$1$ROtJOrojS2cjqvX59tSprAvs-1o implements Consumer {
    private final /* synthetic */ RemoteCallback f$0;

    public /* synthetic */ -$$Lambda$PermissionControllerService$1$ROtJOrojS2cjqvX59tSprAvs-1o(RemoteCallback remoteCallback) {
        this.f$0 = remoteCallback;
    }

    public final void accept(Object obj) {
        AnonymousClass1.lambda$getAppPermissions$2(this.f$0, (List) obj);
    }
}
