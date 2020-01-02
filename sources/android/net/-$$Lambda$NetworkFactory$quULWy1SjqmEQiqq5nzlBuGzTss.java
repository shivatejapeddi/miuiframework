package android.net;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$NetworkFactory$quULWy1SjqmEQiqq5nzlBuGzTss implements Runnable {
    private final /* synthetic */ NetworkFactory f$0;
    private final /* synthetic */ NetworkRequest f$1;

    public /* synthetic */ -$$Lambda$NetworkFactory$quULWy1SjqmEQiqq5nzlBuGzTss(NetworkFactory networkFactory, NetworkRequest networkRequest) {
        this.f$0 = networkFactory;
        this.f$1 = networkRequest;
    }

    public final void run() {
        this.f$0.lambda$releaseRequestAsUnfulfillableByAnyFactory$1$NetworkFactory(this.f$1);
    }
}
