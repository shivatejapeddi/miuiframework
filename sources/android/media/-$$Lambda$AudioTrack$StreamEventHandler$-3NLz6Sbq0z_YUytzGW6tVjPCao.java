package android.media;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AudioTrack$StreamEventHandler$-3NLz6Sbq0z_YUytzGW6tVjPCao implements Runnable {
    private final /* synthetic */ StreamEventHandler f$0;
    private final /* synthetic */ StreamEventCbInfo f$1;

    public /* synthetic */ -$$Lambda$AudioTrack$StreamEventHandler$-3NLz6Sbq0z_YUytzGW6tVjPCao(StreamEventHandler streamEventHandler, StreamEventCbInfo streamEventCbInfo) {
        this.f$0 = streamEventHandler;
        this.f$1 = streamEventCbInfo;
    }

    public final void run() {
        this.f$0.lambda$handleMessage$2$AudioTrack$StreamEventHandler(this.f$1);
    }
}
