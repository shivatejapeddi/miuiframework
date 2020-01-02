package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$ygzOWFRiY4sZQ4WYUPIefqgiGvM implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$ygzOWFRiY4sZQ4WYUPIefqgiGvM(PhoneStateListener phoneStateListener, int i) {
        this.f$0 = phoneStateListener;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onSrvccStateChanged(this.f$1);
    }
}
