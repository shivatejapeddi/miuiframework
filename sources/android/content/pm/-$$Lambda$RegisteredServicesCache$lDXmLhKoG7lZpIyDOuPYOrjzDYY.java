package android.content.pm;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RegisteredServicesCache$lDXmLhKoG7lZpIyDOuPYOrjzDYY implements Runnable {
    private final /* synthetic */ RegisteredServicesCacheListener f$0;
    private final /* synthetic */ Object f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ boolean f$3;

    public /* synthetic */ -$$Lambda$RegisteredServicesCache$lDXmLhKoG7lZpIyDOuPYOrjzDYY(RegisteredServicesCacheListener registeredServicesCacheListener, Object obj, int i, boolean z) {
        this.f$0 = registeredServicesCacheListener;
        this.f$1 = obj;
        this.f$2 = i;
        this.f$3 = z;
    }

    public final void run() {
        RegisteredServicesCache.lambda$notifyListener$0(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
