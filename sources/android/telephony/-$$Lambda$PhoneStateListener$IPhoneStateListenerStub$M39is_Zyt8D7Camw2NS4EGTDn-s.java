package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$M39is_Zyt8D7Camw2NS4EGTDn-s implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$M39is_Zyt8D7Camw2NS4EGTDn-s(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, int i) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = i;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onSignalStrengthChanged$3$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
