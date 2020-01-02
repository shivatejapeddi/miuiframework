package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$JalixlMNdjktPsNntP_JT9pymhs implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$JalixlMNdjktPsNntP_JT9pymhs(PhoneStateListener phoneStateListener, int i) {
        this.f$0 = phoneStateListener;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onDataActivity(this.f$1);
    }
}
