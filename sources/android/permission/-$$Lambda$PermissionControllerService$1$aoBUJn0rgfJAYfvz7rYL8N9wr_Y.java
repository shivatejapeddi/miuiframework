package android.permission;

import android.os.Bundle;
import android.os.RemoteCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PermissionControllerService$1$aoBUJn0rgfJAYfvz7rYL8N9wr_Y implements Runnable {
    private final /* synthetic */ RemoteCallback f$0;

    public /* synthetic */ -$$Lambda$PermissionControllerService$1$aoBUJn0rgfJAYfvz7rYL8N9wr_Y(RemoteCallback remoteCallback) {
        this.f$0 = remoteCallback;
    }

    public final void run() {
        this.f$0.sendResult(Bundle.EMPTY);
    }
}
