package android.service.watchdog;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper$pC_jIpmynGf4FhRLSRCGbJwUkGE implements Runnable {
    private final /* synthetic */ ExplicitHealthCheckServiceWrapper f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ -$$Lambda$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper$pC_jIpmynGf4FhRLSRCGbJwUkGE(ExplicitHealthCheckServiceWrapper explicitHealthCheckServiceWrapper, String str) {
        this.f$0 = explicitHealthCheckServiceWrapper;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.lambda$cancel$2$ExplicitHealthCheckService$ExplicitHealthCheckServiceWrapper(this.f$1);
    }
}
