package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$2cMrwdqnKBpixpApeIX38rmRLak implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ CellLocation f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$2cMrwdqnKBpixpApeIX38rmRLak(PhoneStateListener phoneStateListener, CellLocation cellLocation) {
        this.f$0 = phoneStateListener;
        this.f$1 = cellLocation;
    }

    public final void run() {
        this.f$0.onCellLocationChanged(this.f$1);
    }
}
