package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessageStore$RFZerRPNR1WyCuEIu6_yEveDhrk implements RcsServiceCall {
    private final /* synthetic */ RcsQueryContinuationToken f$0;

    public /* synthetic */ -$$Lambda$RcsMessageStore$RFZerRPNR1WyCuEIu6_yEveDhrk(RcsQueryContinuationToken rcsQueryContinuationToken) {
        this.f$0 = rcsQueryContinuationToken;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return iRcs.getEventsWithToken(this.f$0, str);
    }
}
