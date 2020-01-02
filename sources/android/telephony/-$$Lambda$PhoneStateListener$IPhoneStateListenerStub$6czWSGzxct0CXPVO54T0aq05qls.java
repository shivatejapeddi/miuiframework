package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$6czWSGzxct0CXPVO54T0aq05qls implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$6czWSGzxct0CXPVO54T0aq05qls(PhoneStateListener phoneStateListener, int i, String str) {
        this.f$0 = phoneStateListener;
        this.f$1 = i;
        this.f$2 = str;
    }

    public final void run() {
        this.f$0.onCallStateChanged(this.f$1, this.f$2);
    }
}
