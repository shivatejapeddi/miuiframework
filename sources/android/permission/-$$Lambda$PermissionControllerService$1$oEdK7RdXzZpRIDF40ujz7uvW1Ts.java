package android.permission;

import android.os.RemoteCallback;
import android.permission.PermissionControllerService.AnonymousClass1;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerService$1$oEdK7RdXzZpRIDF40ujz7uvW1Ts implements Consumer {
    private final /* synthetic */ RemoteCallback f$0;

    public /* synthetic */ -$$Lambda$PermissionControllerService$1$oEdK7RdXzZpRIDF40ujz7uvW1Ts(RemoteCallback remoteCallback) {
        this.f$0 = remoteCallback;
    }

    public final void accept(Object obj) {
        AnonymousClass1.lambda$getPermissionUsages$4(this.f$0, (List) obj);
    }
}
