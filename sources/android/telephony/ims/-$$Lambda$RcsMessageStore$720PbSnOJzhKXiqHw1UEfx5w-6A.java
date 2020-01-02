package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessageStore$720PbSnOJzhKXiqHw1UEfx5w-6A implements RcsServiceCall {
    private final /* synthetic */ RcsParticipantQueryParams f$0;

    public /* synthetic */ -$$Lambda$RcsMessageStore$720PbSnOJzhKXiqHw1UEfx5w-6A(RcsParticipantQueryParams rcsParticipantQueryParams) {
        this.f$0 = rcsParticipantQueryParams;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return iRcs.getParticipants(this.f$0, str);
    }
}
