package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$-CiOzgf6ys4EwlCYOVUsuz9YQ5c implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ PhoneCapability f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$-CiOzgf6ys4EwlCYOVUsuz9YQ5c(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, PhoneCapability phoneCapability) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = phoneCapability;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onPhoneCapabilityChanged$47$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
