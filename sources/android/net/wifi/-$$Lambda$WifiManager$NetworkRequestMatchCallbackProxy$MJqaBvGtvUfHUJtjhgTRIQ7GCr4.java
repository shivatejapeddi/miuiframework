package android.net.wifi;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$MJqaBvGtvUfHUJtjhgTRIQ7GCr4 implements Runnable {
    private final /* synthetic */ NetworkRequestMatchCallbackProxy f$0;
    private final /* synthetic */ WifiConfiguration f$1;

    public /* synthetic */ -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$MJqaBvGtvUfHUJtjhgTRIQ7GCr4(NetworkRequestMatchCallbackProxy networkRequestMatchCallbackProxy, WifiConfiguration wifiConfiguration) {
        this.f$0 = networkRequestMatchCallbackProxy;
        this.f$1 = wifiConfiguration;
    }

    public final void run() {
        this.f$0.lambda$onUserSelectionConnectFailure$4$WifiManager$NetworkRequestMatchCallbackProxy(this.f$1);
    }
}
