package android.net.wifi;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WifiManager$SoftApCallbackProxy$vo4E4HQhX8ezRZP1e1kxdx6MvpE implements Runnable {
    private final /* synthetic */ SoftApCallbackProxy f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$WifiManager$SoftApCallbackProxy$vo4E4HQhX8ezRZP1e1kxdx6MvpE(SoftApCallbackProxy softApCallbackProxy, String str, int i) {
        this.f$0 = softApCallbackProxy;
        this.f$1 = str;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$onStaConnected$2$WifiManager$SoftApCallbackProxy(this.f$1, this.f$2);
    }
}
