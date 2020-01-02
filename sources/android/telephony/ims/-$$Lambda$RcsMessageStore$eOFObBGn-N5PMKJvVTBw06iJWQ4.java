package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessageStore$eOFObBGn-N5PMKJvVTBw06iJWQ4 implements RcsServiceCall {
    private final /* synthetic */ RcsParticipant f$0;

    public /* synthetic */ -$$Lambda$RcsMessageStore$eOFObBGn-N5PMKJvVTBw06iJWQ4(RcsParticipant rcsParticipant) {
        this.f$0 = rcsParticipant;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return Integer.valueOf(iRcs.createRcs1To1Thread(this.f$0.getId(), str));
    }
}
