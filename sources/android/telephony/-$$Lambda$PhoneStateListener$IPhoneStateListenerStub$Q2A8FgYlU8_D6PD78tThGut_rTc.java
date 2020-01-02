package android.telephony;

import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Q2A8FgYlU8_D6PD78tThGut_rTc implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ List f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Q2A8FgYlU8_D6PD78tThGut_rTc(PhoneStateListener phoneStateListener, List list) {
        this.f$0 = phoneStateListener;
        this.f$1 = list;
    }

    public final void run() {
        this.f$0.onCellInfoChanged(this.f$1);
    }
}
