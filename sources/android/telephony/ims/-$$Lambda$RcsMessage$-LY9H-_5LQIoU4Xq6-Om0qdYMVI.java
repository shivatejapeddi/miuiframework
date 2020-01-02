package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessage$-LY9H-_5LQIoU4Xq6-Om0qdYMVI implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsMessage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$RcsMessage$-LY9H-_5LQIoU4Xq6-Om0qdYMVI(RcsMessage rcsMessage, int i) {
        this.f$0 = rcsMessage;
        this.f$1 = i;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setSubscriptionId$1$RcsMessage(this.f$1, iRcs, str);
    }
}
