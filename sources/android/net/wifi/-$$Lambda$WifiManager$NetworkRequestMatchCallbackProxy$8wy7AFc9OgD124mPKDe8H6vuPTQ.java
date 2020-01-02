package android.net.wifi;

import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$8wy7AFc9OgD124mPKDe8H6vuPTQ implements Runnable {
    private final /* synthetic */ NetworkRequestMatchCallbackProxy f$0;
    private final /* synthetic */ List f$1;

    public /* synthetic */ -$$Lambda$WifiManager$NetworkRequestMatchCallbackProxy$8wy7AFc9OgD124mPKDe8H6vuPTQ(NetworkRequestMatchCallbackProxy networkRequestMatchCallbackProxy, List list) {
        this.f$0 = networkRequestMatchCallbackProxy;
        this.f$1 = list;
    }

    public final void run() {
        this.f$0.lambda$onMatch$2$WifiManager$NetworkRequestMatchCallbackProxy(this.f$1);
    }
}
