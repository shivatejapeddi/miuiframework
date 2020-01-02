package android.media;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MediaRouter$Static$Client$xd63sG7fVJYYu49J1xqN8JYQvo4 implements Runnable {
    private final /* synthetic */ Client f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ -$$Lambda$MediaRouter$Static$Client$xd63sG7fVJYYu49J1xqN8JYQvo4(Client client, String str) {
        this.f$0 = client;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.lambda$onSelectedRouteChanged$0$MediaRouter$Static$Client(this.f$1);
    }
}
