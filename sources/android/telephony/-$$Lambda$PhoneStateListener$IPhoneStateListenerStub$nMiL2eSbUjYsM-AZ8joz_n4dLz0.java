package android.telephony;

import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nMiL2eSbUjYsM-AZ8joz_n4dLz0 implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ List f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$nMiL2eSbUjYsM-AZ8joz_n4dLz0(PhoneStateListener phoneStateListener, List list) {
        this.f$0 = phoneStateListener;
        this.f$1 = list;
    }

    public final void run() {
        this.f$0.onPhysicalChannelConfigurationChanged(this.f$1);
    }
}
