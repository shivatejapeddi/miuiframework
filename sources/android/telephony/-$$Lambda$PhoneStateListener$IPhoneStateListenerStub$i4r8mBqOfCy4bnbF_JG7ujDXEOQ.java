package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$i4r8mBqOfCy4bnbF_JG7ujDXEOQ implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$i4r8mBqOfCy4bnbF_JG7ujDXEOQ(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, int i) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = i;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onOtaspChanged$19$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
