package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$OIAjnTzp_YIf6Y7jPFABi9BXZvs implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ List f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$OIAjnTzp_YIf6Y7jPFABi9BXZvs(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, List list) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = list;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onPhysicalChannelConfigurationChanged$43$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
