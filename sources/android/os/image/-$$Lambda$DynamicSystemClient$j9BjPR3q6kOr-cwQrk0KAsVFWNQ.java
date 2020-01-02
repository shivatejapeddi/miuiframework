package android.os.image;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DynamicSystemClient$j9BjPR3q6kOr-cwQrk0KAsVFWNQ implements Runnable {
    private final /* synthetic */ DynamicSystemClient f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ long f$3;
    private final /* synthetic */ Throwable f$4;

    public /* synthetic */ -$$Lambda$DynamicSystemClient$j9BjPR3q6kOr-cwQrk0KAsVFWNQ(DynamicSystemClient dynamicSystemClient, int i, int i2, long j, Throwable th) {
        this.f$0 = dynamicSystemClient;
        this.f$1 = i;
        this.f$2 = i2;
        this.f$3 = j;
        this.f$4 = th;
    }

    public final void run() {
        this.f$0.lambda$handleMessage$0$DynamicSystemClient(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
