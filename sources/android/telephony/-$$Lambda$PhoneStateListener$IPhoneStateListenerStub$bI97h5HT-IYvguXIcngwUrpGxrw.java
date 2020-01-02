package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$bI97h5HT-IYvguXIcngwUrpGxrw implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$bI97h5HT-IYvguXIcngwUrpGxrw(PhoneStateListener phoneStateListener, int i) {
        this.f$0 = phoneStateListener;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onRadioPowerStateChanged(this.f$1);
    }
}
