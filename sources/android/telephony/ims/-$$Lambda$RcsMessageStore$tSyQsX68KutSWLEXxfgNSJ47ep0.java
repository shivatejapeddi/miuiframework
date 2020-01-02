package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessageStore$tSyQsX68KutSWLEXxfgNSJ47ep0 implements RcsServiceCall {
    private final /* synthetic */ RcsQueryContinuationToken f$0;

    public /* synthetic */ -$$Lambda$RcsMessageStore$tSyQsX68KutSWLEXxfgNSJ47ep0(RcsQueryContinuationToken rcsQueryContinuationToken) {
        this.f$0 = rcsQueryContinuationToken;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return iRcs.getParticipantsWithToken(this.f$0, str);
    }
}
