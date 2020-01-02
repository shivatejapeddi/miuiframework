package android.net.wifi;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WifiManager$SoftApCallbackProxy$X5LJgdNUCXHctJ7m4-CGDjDEfkU implements Runnable {
    private final /* synthetic */ SoftApCallbackProxy f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$WifiManager$SoftApCallbackProxy$X5LJgdNUCXHctJ7m4-CGDjDEfkU(SoftApCallbackProxy softApCallbackProxy, String str, int i) {
        this.f$0 = softApCallbackProxy;
        this.f$1 = str;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$onStaDisconnected$3$WifiManager$SoftApCallbackProxy(this.f$1, this.f$2);
    }
}
