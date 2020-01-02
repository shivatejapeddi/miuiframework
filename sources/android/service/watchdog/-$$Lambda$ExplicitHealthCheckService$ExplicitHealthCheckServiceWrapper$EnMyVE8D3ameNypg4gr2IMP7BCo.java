package android.service.watchdog;

import android.os.RemoteCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper$EnMyVE8D3ameNypg4gr2IMP7BCo implements Runnable {
    private final /* synthetic */ ExplicitHealthCheckServiceWrapper f$0;
    private final /* synthetic */ RemoteCallback f$1;

    public /* synthetic */ -$$Lambda$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper$EnMyVE8D3ameNypg4gr2IMP7BCo(ExplicitHealthCheckServiceWrapper explicitHealthCheckServiceWrapper, RemoteCallback remoteCallback) {
        this.f$0 = explicitHealthCheckServiceWrapper;
        this.f$1 = remoteCallback;
    }

    public final void run() {
        this.f$0.lambda$setCallback$0$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper(this.f$1);
    }
}
