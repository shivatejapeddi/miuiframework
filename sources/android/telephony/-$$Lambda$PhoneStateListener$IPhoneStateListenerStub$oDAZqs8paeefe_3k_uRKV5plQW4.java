package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$oDAZqs8paeefe_3k_uRKV5plQW4 implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ String f$3;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$oDAZqs8paeefe_3k_uRKV5plQW4(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, int i, String str) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = i;
        this.f$3 = str;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onCallStateChanged$11$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2, this.f$3);
    }
}
