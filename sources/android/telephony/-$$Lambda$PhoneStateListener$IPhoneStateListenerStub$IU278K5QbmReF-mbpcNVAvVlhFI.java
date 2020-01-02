package android.telephony;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$IU278K5QbmReF-mbpcNVAvVlhFI implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ DataConnectionRealTimeInfo f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$IU278K5QbmReF-mbpcNVAvVlhFI(PhoneStateListener phoneStateListener, DataConnectionRealTimeInfo dataConnectionRealTimeInfo) {
        this.f$0 = phoneStateListener;
        this.f$1 = dataConnectionRealTimeInfo;
    }

    public final void run() {
        this.f$0.onDataConnectionRealTimeInfoChanged(this.f$1);
    }
}
