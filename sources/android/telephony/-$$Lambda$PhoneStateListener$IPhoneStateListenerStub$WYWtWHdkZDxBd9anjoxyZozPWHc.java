package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$WYWtWHdkZDxBd9anjoxyZozPWHc implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$WYWtWHdkZDxBd9anjoxyZozPWHc(PhoneStateListener phoneStateListener, boolean z) {
        this.f$0 = phoneStateListener;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.onCallForwardingIndicatorChanged(this.f$1);
    }
}
