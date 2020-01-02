package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsGroupThread$X2eY_CkF7PfEGF8QwmaD6Cv0PhI implements RcsServiceCall {
    private final /* synthetic */ RcsParticipantQueryParams f$0;

    public /* synthetic */ -$$Lambda$RcsGroupThread$X2eY_CkF7PfEGF8QwmaD6Cv0PhI(RcsParticipantQueryParams rcsParticipantQueryParams) {
        this.f$0 = rcsParticipantQueryParams;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return iRcs.getParticipants(this.f$0, str);
    }
}
