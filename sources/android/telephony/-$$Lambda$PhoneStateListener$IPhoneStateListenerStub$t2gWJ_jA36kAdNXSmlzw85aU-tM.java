package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$t2gWJ_jA36kAdNXSmlzw85aU-tM implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$t2gWJ_jA36kAdNXSmlzw85aU-tM(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, int i) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = i;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onDataActivationStateChanged$35$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
