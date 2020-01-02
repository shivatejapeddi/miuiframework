package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$HEcWn-J1WRb0wLERu2qoMIZDfjY implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ PreciseDataConnectionState f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$HEcWn-J1WRb0wLERu2qoMIZDfjY(PhoneStateListener phoneStateListener, PreciseDataConnectionState preciseDataConnectionState) {
        this.f$0 = phoneStateListener;
        this.f$1 = preciseDataConnectionState;
    }

    public final void run() {
        this.f$0.onPreciseDataConnectionStateChanged(this.f$1);
    }
}
