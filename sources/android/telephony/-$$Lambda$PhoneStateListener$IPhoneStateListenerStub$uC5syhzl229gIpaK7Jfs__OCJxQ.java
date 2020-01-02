package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$uC5syhzl229gIpaK7Jfs__OCJxQ implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ ServiceState f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$uC5syhzl229gIpaK7Jfs__OCJxQ(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, ServiceState serviceState) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = serviceState;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onServiceStateChanged$1$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
