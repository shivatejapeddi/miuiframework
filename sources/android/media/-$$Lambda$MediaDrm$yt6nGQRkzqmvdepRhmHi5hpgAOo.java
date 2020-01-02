package android.media;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$MediaDrm$yt6nGQRkzqmvdepRhmHi5hpgAOo implements Runnable {
    private final /* synthetic */ MediaDrm f$0;
    private final /* synthetic */ Object f$1;
    private final /* synthetic */ ListenerWithExecutor f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ int f$4;

    public /* synthetic */ -$$Lambda$MediaDrm$yt6nGQRkzqmvdepRhmHi5hpgAOo(MediaDrm mediaDrm, Object obj, ListenerWithExecutor listenerWithExecutor, int i, int i2) {
        this.f$0 = mediaDrm;
        this.f$1 = obj;
        this.f$2 = listenerWithExecutor;
        this.f$3 = i;
        this.f$4 = i2;
    }

    public final void run() {
        MediaDrm.lambda$postEventFromNative$4(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
