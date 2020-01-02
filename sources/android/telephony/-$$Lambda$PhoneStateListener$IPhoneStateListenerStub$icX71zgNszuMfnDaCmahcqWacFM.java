package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$icX71zgNszuMfnDaCmahcqWacFM implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$icX71zgNszuMfnDaCmahcqWacFM(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, int i, int i2) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = i;
        this.f$3 = i2;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onCallDisconnectCauseChanged$25$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2, this.f$3);
    }
}
