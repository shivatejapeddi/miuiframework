package android.permission;

import android.os.RemoteCallback;
import android.permission.PermissionControllerService.AnonymousClass1;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerService$1$Sp35OTwahalQfZumoUDJ70lCKe0 implements Consumer {
    private final /* synthetic */ RemoteCallback f$0;

    public /* synthetic */ -$$Lambda$PermissionControllerService$1$Sp35OTwahalQfZumoUDJ70lCKe0(RemoteCallback remoteCallback) {
        this.f$0 = remoteCallback;
    }

    public final void accept(Object obj) {
        AnonymousClass1.lambda$setRuntimePermissionGrantStateByDeviceAdmin$5(this.f$0, (Boolean) obj);
    }
}
