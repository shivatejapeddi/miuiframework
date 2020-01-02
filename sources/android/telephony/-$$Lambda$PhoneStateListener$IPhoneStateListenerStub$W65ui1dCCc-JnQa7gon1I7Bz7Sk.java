package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$W65ui1dCCc-JnQa7gon1I7Bz7Sk implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$W65ui1dCCc-JnQa7gon1I7Bz7Sk(PhoneStateListener phoneStateListener, int i) {
        this.f$0 = phoneStateListener;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onDataActivationStateChanged(this.f$1);
    }
}
