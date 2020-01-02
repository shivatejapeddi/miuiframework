package android.net.wifi;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$DYo-nMH0tB37PG_5OviApSTSGXg implements Runnable {
    private final /* synthetic */ NetworkRequestMatchCallbackProxy f$0;
    private final /* synthetic */ INetworkRequestUserSelectionCallback f$1;

    public /* synthetic */ -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$DYo-nMH0tB37PG_5OviApSTSGXg(NetworkRequestMatchCallbackProxy networkRequestMatchCallbackProxy, INetworkRequestUserSelectionCallback iNetworkRequestUserSelectionCallback) {
        this.f$0 = networkRequestMatchCallbackProxy;
        this.f$1 = iNetworkRequestUserSelectionCallback;
    }

    public final void run() {
        this.f$0.lambda$onUserSelectionCallbackRegistration$0$WifiManager$NetworkRequestMatchCallbackProxy(this.f$1);
    }
}
