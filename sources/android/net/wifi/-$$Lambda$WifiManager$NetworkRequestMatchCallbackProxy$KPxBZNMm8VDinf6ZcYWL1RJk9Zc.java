package android.net.wifi;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$KPxBZNMm8VDinf6ZcYWL1RJk9Zc implements Runnable {
    private final /* synthetic */ NetworkRequestMatchCallbackProxy f$0;
    private final /* synthetic */ WifiConfiguration f$1;

    public /* synthetic */ -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$KPxBZNMm8VDinf6ZcYWL1RJk9Zc(NetworkRequestMatchCallbackProxy networkRequestMatchCallbackProxy, WifiConfiguration wifiConfiguration) {
        this.f$0 = networkRequestMatchCallbackProxy;
        this.f$1 = wifiConfiguration;
    }

    public final void run() {
        this.f$0.lambda$onUserSelectionConnectSuccess$3$WifiManager$NetworkRequestMatchCallbackProxy(this.f$1);
    }
}
