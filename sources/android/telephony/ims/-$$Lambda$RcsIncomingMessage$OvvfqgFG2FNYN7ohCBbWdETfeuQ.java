package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsIncomingMessage$OvvfqgFG2FNYN7ohCBbWdETfeuQ implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsIncomingMessage f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ -$$Lambda$RcsIncomingMessage$OvvfqgFG2FNYN7ohCBbWdETfeuQ(RcsIncomingMessage rcsIncomingMessage, long j) {
        this.f$0 = rcsIncomingMessage;
        this.f$1 = j;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setSeenTimestamp$2$RcsIncomingMessage(this.f$1, iRcs, str);
    }
}
