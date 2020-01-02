package android.telephony;

import android.telephony.ims.ImsReasonInfo;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Bzok3Q_pjLC0O4ulkDfbWru0v6w implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ ImsReasonInfo f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Bzok3Q_pjLC0O4ulkDfbWru0v6w(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, ImsReasonInfo imsReasonInfo) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = imsReasonInfo;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onImsCallDisconnectCauseChanged$55$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
