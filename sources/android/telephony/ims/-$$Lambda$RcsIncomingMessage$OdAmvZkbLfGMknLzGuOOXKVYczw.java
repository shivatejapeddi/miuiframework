package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsIncomingMessage$OdAmvZkbLfGMknLzGuOOXKVYczw implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsIncomingMessage f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ -$$Lambda$RcsIncomingMessage$OdAmvZkbLfGMknLzGuOOXKVYczw(RcsIncomingMessage rcsIncomingMessage, long j) {
        this.f$0 = rcsIncomingMessage;
        this.f$1 = j;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setArrivalTimestamp$0$RcsIncomingMessage(this.f$1, iRcs, str);
    }
}
