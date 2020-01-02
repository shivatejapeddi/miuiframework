package android.media;

import android.os.Message;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AudioTrack$StreamEventHandler$IUDediua4qA5AgKwU3zNCXA7jQo implements Runnable {
    private final /* synthetic */ StreamEventHandler f$0;
    private final /* synthetic */ StreamEventCbInfo f$1;
    private final /* synthetic */ Message f$2;

    public /* synthetic */ -$$Lambda$AudioTrack$StreamEventHandler$IUDediua4qA5AgKwU3zNCXA7jQo(StreamEventHandler streamEventHandler, StreamEventCbInfo streamEventCbInfo, Message message) {
        this.f$0 = streamEventHandler;
        this.f$1 = streamEventCbInfo;
        this.f$2 = message;
    }

    public final void run() {
        this.f$0.lambda$handleMessage$0$AudioTrack$StreamEventHandler(this.f$1, this.f$2);
    }
}
