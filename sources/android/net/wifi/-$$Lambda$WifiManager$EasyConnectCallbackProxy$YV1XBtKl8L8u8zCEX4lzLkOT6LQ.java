package android.net.wifi;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WifiManager$EasyConnectCallbackProxy$YV1XBtKl8L8u8zCEX4lzLkOT6LQ implements Runnable {
    private final /* synthetic */ EasyConnectCallbackProxy f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$WifiManager$EasyConnectCallbackProxy$YV1XBtKl8L8u8zCEX4lzLkOT6LQ(EasyConnectCallbackProxy easyConnectCallbackProxy, int i) {
        this.f$0 = easyConnectCallbackProxy;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$onProgress$3$WifiManager$EasyConnectCallbackProxy(this.f$1);
    }
}
