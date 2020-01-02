package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$hxq77a5O_MUfoptHg15ipzFvMkI implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$hxq77a5O_MUfoptHg15ipzFvMkI(PhoneStateListener phoneStateListener, int i, int i2) {
        this.f$0 = phoneStateListener;
        this.f$1 = i;
        this.f$2 = i2;
    }

    public final void run() {
        this.f$0.onCallDisconnectCauseChanged(this.f$1, this.f$2);
    }
}
