package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$dUc3j82sK9P9Zpaq-91n9bk_Rpc implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$dUc3j82sK9P9Zpaq-91n9bk_Rpc(PhoneStateListener phoneStateListener, int i, int i2) {
        this.f$0 = phoneStateListener;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run() {
        IPhoneStateListenerStub.lambda$onDataConnectionStateChanged$12(this.f$0, this.f$1, this.f$2);
    }
}
