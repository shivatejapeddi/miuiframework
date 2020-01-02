package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$aysbwPqxcLV_5w6LP0TzZu2D-ew implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ SignalStrength f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$aysbwPqxcLV_5w6LP0TzZu2D-ew(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, SignalStrength signalStrength) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = signalStrength;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onSignalStrengthsChanged$17$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
