package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Q_Cpm8aB8qYt8lGxD5PXek_-4bA implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ CallAttributes f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Q_Cpm8aB8qYt8lGxD5PXek_-4bA(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, CallAttributes callAttributes) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = callAttributes;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onCallAttributesChanged$51$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
