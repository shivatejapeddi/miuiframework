package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$YY3srkIkMm8vTSFJZHoiKzUUrGs implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$YY3srkIkMm8vTSFJZHoiKzUUrGs(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, boolean z) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = z;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onCarrierNetworkChange$41$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
