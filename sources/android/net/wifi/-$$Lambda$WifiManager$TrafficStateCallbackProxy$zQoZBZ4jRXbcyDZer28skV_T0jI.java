package android.net.wifi;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WifiManager$TrafficStateCallbackProxy$zQoZBZ4jRXbcyDZer28skV_T0jI implements Runnable {
    private final /* synthetic */ TrafficStateCallbackProxy f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$WifiManager$TrafficStateCallbackProxy$zQoZBZ4jRXbcyDZer28skV_T0jI(TrafficStateCallbackProxy trafficStateCallbackProxy, int i) {
        this.f$0 = trafficStateCallbackProxy;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$onStateChanged$0$WifiManager$TrafficStateCallbackProxy(this.f$1);
    }
}
