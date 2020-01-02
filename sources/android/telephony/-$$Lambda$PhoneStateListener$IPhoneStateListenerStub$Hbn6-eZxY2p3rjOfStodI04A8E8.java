package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Hbn6-eZxY2p3rjOfStodI04A8E8 implements ThrowingRunnable {
    private final /* synthetic */ IPhoneStateListenerStub f$0;
    private final /* synthetic */ PhoneStateListener f$1;
    private final /* synthetic */ CellLocation f$2;

    public /* synthetic */ -$$Lambda$PhoneStateListener$IPhoneStateListenerStub$Hbn6-eZxY2p3rjOfStodI04A8E8(IPhoneStateListenerStub iPhoneStateListenerStub, PhoneStateListener phoneStateListener, CellLocation cellLocation) {
        this.f$0 = iPhoneStateListenerStub;
        this.f$1 = phoneStateListener;
        this.f$2 = cellLocation;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onCellLocationChanged$9$PhoneStateListener$IPhoneStateListenerStub(this.f$1, this.f$2);
    }
}
