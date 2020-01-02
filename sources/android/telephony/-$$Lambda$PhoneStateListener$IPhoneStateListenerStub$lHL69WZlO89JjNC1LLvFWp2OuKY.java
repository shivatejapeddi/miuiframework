package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$lHL69WZlO89JjNC1LLvFWp2OuKY implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ PhoneCapability f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$lHL69WZlO89JjNC1LLvFWp2OuKY(PhoneStateListener phoneStateListener, PhoneCapability phoneCapability) {
        this.f$0 = phoneStateListener;
        this.f$1 = phoneCapability;
    }

    public final void run() {
        this.f$0.onPhoneCapabilityChanged(this.f$1);
    }
}
