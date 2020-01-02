package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$OfwFKKtcQHRmtv70FCopw6FDAAU implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ DataConnectionRealTimeInfo f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$OfwFKKtcQHRmtv70FCopw6FDAAU(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, DataConnectionRealTimeInfo dataConnectionRealTimeInfo) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = dataConnectionRealTimeInfo;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onDataConnectionRealTimeInfoChanged$29$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
