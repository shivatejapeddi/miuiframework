package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nrGqSRBJrc3_EwotCDNwfKeizIo implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ ServiceState f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nrGqSRBJrc3_EwotCDNwfKeizIo(PhoneStateListener phoneStateListener, ServiceState serviceState) {
        this.f$0 = phoneStateListener;
        this.f$1 = serviceState;
    }

    public final void run() {
        this.f$0.onServiceStateChanged(this.f$1);
    }
}
