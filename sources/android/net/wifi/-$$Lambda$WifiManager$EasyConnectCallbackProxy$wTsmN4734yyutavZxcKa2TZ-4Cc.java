package android.net.wifi;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WifiManager$EasyConnectCallbackProxy$wTsmN4734yyutavZxcKa2TZ-4Cc implements Runnable {
    private final /* synthetic */ EasyConnectCallbackProxy f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$WifiManager$EasyConnectCallbackProxy$wTsmN4734yyutavZxcKa2TZ-4Cc(EasyConnectCallbackProxy easyConnectCallbackProxy, int i) {
        this.f$0 = easyConnectCallbackProxy;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$onSuccess$1$WifiManager$EasyConnectCallbackProxy(this.f$1);
    }
}
