package android.telephony;

import android.telephony.ims.ImsReasonInfo;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$eYTgM6ABgThWqEatVha4ZuIpI0A implements Runnable {
    private final /* synthetic */ PhoneStateListener f$0;
    private final /* synthetic */ ImsReasonInfo f$1;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$eYTgM6ABgThWqEatVha4ZuIpI0A(PhoneStateListener phoneStateListener, ImsReasonInfo imsReasonInfo) {
        this.f$0 = phoneStateListener;
        this.f$1 = imsReasonInfo;
    }

    public final void run() {
        this.f$0.onImsCallDisconnectCauseChanged(this.f$1);
    }
}
