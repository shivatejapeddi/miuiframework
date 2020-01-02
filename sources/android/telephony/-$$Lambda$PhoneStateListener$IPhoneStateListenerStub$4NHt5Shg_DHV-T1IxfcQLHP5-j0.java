package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$4NHt5Shg_DHV-T1IxfcQLHP5-j0 implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ PreciseCallState f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$4NHt5Shg_DHV-T1IxfcQLHP5-j0(PhoneStateListener phoneStateListener, PreciseCallState preciseCallState) {
        this.f$0 = phoneStateListener;
        this.f$1 = preciseCallState;
    }

    public final void run() {
        this.f$0.onPreciseCallStateChanged(this.f$1);
    }
}
