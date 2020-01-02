package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$1M3m0i6211i2YjWyTDT7l0bJm3I implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ boolean f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$1M3m0i6211i2YjWyTDT7l0bJm3I(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, boolean z) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = z;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onCallForwardingIndicatorChanged$7$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
