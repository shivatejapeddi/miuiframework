package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$TqrkuLPlaG_ucU7VbLS4tnf8hG8 implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$TqrkuLPlaG_ucU7VbLS4tnf8hG8(PhoneStateListener phoneStateListener, boolean z) {
        this.f$0 = phoneStateListener;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.onMessageWaitingIndicatorChanged(this.f$1);
    }
}
