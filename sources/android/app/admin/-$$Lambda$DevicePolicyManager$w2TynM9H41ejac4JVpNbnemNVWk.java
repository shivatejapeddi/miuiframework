package android.app.admin;

import android.os.Bundle;
import android.os.RemoteCallback.OnResultListener;
import java.util.concurrent.CompletableFuture;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DevicePolicyManager$w2TynM9H41ejac4JVpNbnemNVWk implements OnResultListener {
    private final /* synthetic */ CompletableFuture f$0;

    public /* synthetic */ -$$Lambda$DevicePolicyManager$w2TynM9H41ejac4JVpNbnemNVWk(CompletableFuture completableFuture) {
        this.f$0 = completableFuture;
    }

    public final void onResult(Bundle bundle) {
        DevicePolicyManager.lambda$setPermissionGrantState$0(this.f$0, bundle);
    }
}
