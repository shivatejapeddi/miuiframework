package android.permission;

import android.os.Bundle;
import android.os.RemoteCallback.OnResultListener;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin$9CrKvc4Mj43M641VzAbk1z_vjck implements OnResultListener {
    private final /* synthetic */ PendingSetRuntimePermissionGrantStateByDeviceAdmin f$0;
    private final /* synthetic */ Executor f$1;
    private final /* synthetic */ Consumer f$2;

    public /* synthetic */ -$$Lambda$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin$9CrKvc4Mj43M641VzAbk1z_vjck(PendingSetRuntimePermissionGrantStateByDeviceAdmin pendingSetRuntimePermissionGrantStateByDeviceAdmin, Executor executor, Consumer consumer) {
        this.f$0 = pendingSetRuntimePermissionGrantStateByDeviceAdmin;
        this.f$1 = executor;
        this.f$2 = consumer;
    }

    public final void onResult(Bundle bundle) {
        this.f$0.lambda$new$1$PermissionControllerManager$PendingSetRuntimePermissionGrantStateByDeviceAdmin(this.f$1, this.f$2, bundle);
    }
}
