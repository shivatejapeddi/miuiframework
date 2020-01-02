package android.telephony;

import java.util.Map;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jGj-qFMdpjbsKaUErqJEeOALEGo implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ Map f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jGj-qFMdpjbsKaUErqJEeOALEGo(PhoneStateListener phoneStateListener, Map map) {
        this.f$0 = phoneStateListener;
        this.f$1 = map;
    }

    public final void run() {
        this.f$0.onEmergencyNumberListChanged(this.f$1);
    }
}
