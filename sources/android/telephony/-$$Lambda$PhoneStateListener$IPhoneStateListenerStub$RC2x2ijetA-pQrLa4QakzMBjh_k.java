package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$RC2x2ijetA-pQrLa4QakzMBjh_k implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ PreciseDataConnectionState f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$RC2x2ijetA-pQrLa4QakzMBjh_k(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, PreciseDataConnectionState preciseDataConnectionState) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = preciseDataConnectionState;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onPreciseDataConnectionStateChanged$27$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
