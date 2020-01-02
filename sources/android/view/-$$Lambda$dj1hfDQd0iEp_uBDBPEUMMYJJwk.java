package android.view;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$dj1hfDQd0iEp_uBDBPEUMMYJJwk implements Runnable {
    private final /* synthetic */ ViewRootImpl f$0;

    public /* synthetic */ -$$Lambda$dj1hfDQd0iEp_uBDBPEUMMYJJwk(ViewRootImpl viewRootImpl) {
        this.f$0 = viewRootImpl;
    }

    public final void run() {
        this.f$0.destroyHardwareResources();
    }
}
