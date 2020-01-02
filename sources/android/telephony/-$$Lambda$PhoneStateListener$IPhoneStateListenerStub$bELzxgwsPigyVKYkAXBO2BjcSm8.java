package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$bELzxgwsPigyVKYkAXBO2BjcSm8 implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ PreciseCallState f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$bELzxgwsPigyVKYkAXBO2BjcSm8(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, PreciseCallState preciseCallState) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = preciseCallState;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onPreciseCallStateChanged$23$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
