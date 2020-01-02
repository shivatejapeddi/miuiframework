package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5t7yF_frkRH7MdItRlwmP00irsM implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ CallAttributes f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5t7yF_frkRH7MdItRlwmP00irsM(PhoneStateListener phoneStateListener, CallAttributes callAttributes) {
        this.f$0 = phoneStateListener;
        this.f$1 = callAttributes;
    }

    public final void run() {
        this.f$0.onCallAttributesChanged(this.f$1);
    }
}
