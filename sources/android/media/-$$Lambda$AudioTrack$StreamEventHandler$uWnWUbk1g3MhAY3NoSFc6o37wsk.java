package android.media;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AudioTrack$StreamEventHandler$uWnWUbk1g3MhAY3NoSFc6o37wsk implements Runnable {
    private final /* synthetic */ StreamEventHandler f$0;
    private final /* synthetic */ StreamEventCbInfo f$1;

    public /* synthetic */ -$$Lambda$AudioTrack$StreamEventHandler$uWnWUbk1g3MhAY3NoSFc6o37wsk(StreamEventHandler streamEventHandler, StreamEventCbInfo streamEventCbInfo) {
        this.f$0 = streamEventHandler;
        this.f$1 = streamEventCbInfo;
    }

    public final void run() {
        this.f$0.lambda$handleMessage$1$AudioTrack$StreamEventHandler(this.f$1);
    }
}
