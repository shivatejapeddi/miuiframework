package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5J-sdvem6pUpdVwRdm8IbDhvuv8 implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$5J-sdvem6pUpdVwRdm8IbDhvuv8(PhoneStateListener phoneStateListener, int i) {
        this.f$0 = phoneStateListener;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onSignalStrengthChanged(this.f$1);
    }
}
