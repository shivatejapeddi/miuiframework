package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsParticipant$HgHlMU15W2RReyvhk-UQ-432pfA implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsParticipant f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ -$$Lambda$RcsParticipant$HgHlMU15W2RReyvhk-UQ-432pfA(RcsParticipant rcsParticipant, String str) {
        this.f$0 = rcsParticipant;
        this.f$1 = str;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setContactId$4$RcsParticipant(this.f$1, iRcs, str);
    }
}
