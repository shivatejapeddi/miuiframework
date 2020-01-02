package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$0s34qsuHFsa43jUHrTkD62ni6Ds implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ SignalStrength f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$0s34qsuHFsa43jUHrTkD62ni6Ds(PhoneStateListener phoneStateListener, SignalStrength signalStrength) {
        this.f$0 = phoneStateListener;
        this.f$1 = signalStrength;
    }

    public final void run() {
        this.f$0.onSignalStrengthsChanged(this.f$1);
    }
}
