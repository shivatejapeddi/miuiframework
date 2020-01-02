package android.app;

import android.os.Bundle;
import android.os.RemoteCallback.OnResultListener;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AppOpsManager$4Zbi7CSLEt0nvOmfJBVYtJkauTQ implements OnResultListener {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ Consumer f$1;

    public /* synthetic */ -$$Lambda$AppOpsManager$4Zbi7CSLEt0nvOmfJBVYtJkauTQ(Executor executor, Consumer consumer) {
        this.f$0 = executor;
        this.f$1 = consumer;
    }

    public final void onResult(Bundle bundle) {
        AppOpsManager.lambda$getHistoricalOps$1(this.f$0, this.f$1, bundle);
    }
}
