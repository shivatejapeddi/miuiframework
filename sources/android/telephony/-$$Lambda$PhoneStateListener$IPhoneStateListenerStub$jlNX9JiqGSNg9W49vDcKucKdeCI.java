package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jlNX9JiqGSNg9W49vDcKucKdeCI implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jlNX9JiqGSNg9W49vDcKucKdeCI(PhoneStateListener phoneStateListener, boolean z) {
        this.f$0 = phoneStateListener;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.onCarrierNetworkChange(this.f$1);
    }
}
