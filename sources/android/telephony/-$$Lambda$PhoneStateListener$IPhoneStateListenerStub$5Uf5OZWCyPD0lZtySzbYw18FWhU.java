package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5Uf5OZWCyPD0lZtySzbYw18FWhU implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5Uf5OZWCyPD0lZtySzbYw18FWhU(PhoneStateListener phoneStateListener, boolean z) {
        this.f$0 = phoneStateListener;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.onUserMobileDataStateChanged(this.f$1);
    }
}
