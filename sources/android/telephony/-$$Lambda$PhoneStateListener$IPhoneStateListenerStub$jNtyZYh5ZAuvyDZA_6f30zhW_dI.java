package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jNtyZYh5ZAuvyDZA_6f30zhW_dI implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ byte[] f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$jNtyZYh5ZAuvyDZA_6f30zhW_dI(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, byte[] bArr) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = bArr;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onOemHookRawEvent$39$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
