package android.service.watchdog;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ExplicitHealthCheckService$ulagkAZ2bM7-LW9T7PSTxSLQfBE implements Runnable {
    private final /* synthetic */ ExplicitHealthCheckService f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ -$$Lambda$ExplicitHealthCheckService$ulagkAZ2bM7-LW9T7PSTxSLQfBE(ExplicitHealthCheckService explicitHealthCheckService, String str) {
        this.f$0 = explicitHealthCheckService;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.lambda$notifyHealthCheckPassed$0$ExplicitHealthCheckService(this.f$1);
    }
}
