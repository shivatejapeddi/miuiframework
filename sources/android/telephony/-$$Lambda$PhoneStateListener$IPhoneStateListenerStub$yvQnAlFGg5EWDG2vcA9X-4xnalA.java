package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$yvQnAlFGg5EWDG2vcA9X-4xnalA implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ List f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$yvQnAlFGg5EWDG2vcA9X-4xnalA(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, List list) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = list;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onCellInfoChanged$21$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
