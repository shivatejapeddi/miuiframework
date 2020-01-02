package android.service.watchdog;

import android.os.RemoteCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper$5Rv9E4-Jc0y0GMGqI_g-82qtYpg implements Runnable {
    private final /* synthetic */ ExplicitHealthCheckServiceWrapper f$0;
    private final /* synthetic */ RemoteCallback f$1;

    public /* synthetic */ -$$Lambda$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper$5Rv9E4-Jc0y0GMGqI_g-82qtYpg(ExplicitHealthCheckServiceWrapper explicitHealthCheckServiceWrapper, RemoteCallback remoteCallback) {
        this.f$0 = explicitHealthCheckServiceWrapper;
        this.f$1 = remoteCallback;
    }

    public final void run() {
        this.f$0.lambda$getSupportedPackages$3$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper(this.f$1);
    }
}
