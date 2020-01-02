package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jclAV5yU3RtV94suRvvhafvGuhw implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ byte[] f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jclAV5yU3RtV94suRvvhafvGuhw(PhoneStateListener phoneStateListener, byte[] bArr) {
        this.f$0 = phoneStateListener;
        this.f$1 = bArr;
    }

    public final void run() {
        this.f$0.onOemHookRawEvent(this.f$1);
    }
}
