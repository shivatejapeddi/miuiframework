package android.permission;

import android.os.Bundle;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin$L3EtiNpasfEGf-E2sSUKhk-dYUg implements Runnable {
    private final /* synthetic */ PendingSetRuntimePermissionGrantStateByDeviceAdmin f$0;
    private final /* synthetic */ Consumer f$1;
    private final /* synthetic */ Bundle f$2;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin$L3EtiNpasfEGf-E2sSUKhk-dYUg(PendingSetRuntimePermissionGrantStateByDeviceAdmin pendingSetRuntimePermissionGrantStateByDeviceAdmin, Consumer consumer, Bundle bundle) {
        this.f$0 = pendingSetRuntimePermissionGrantStateByDeviceAdmin;
        this.f$1 = consumer;
        this.f$2 = bundle;
    }

    public final void run() {
        this.f$0.lambda$new$0$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin(this.f$1, this.f$2);
    }
}
