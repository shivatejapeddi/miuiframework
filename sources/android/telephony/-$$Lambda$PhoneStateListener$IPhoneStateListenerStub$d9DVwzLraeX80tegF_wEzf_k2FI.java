package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.Map;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$d9DVwzLraeX80tegF_wEzf_k2FI implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ Map f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$d9DVwzLraeX80tegF_wEzf_k2FI(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, Map map) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = map;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onEmergencyNumberListChanged$45$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
