package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsGroupThread$HaJSnZuef49b66N8v9ayzVaOQxQ implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsGroupThread f$0;
    private final /* synthetic */ RcsParticipant f$1;

    public /* synthetic */ -$$Lambda$RcsGroupThread$HaJSnZuef49b66N8v9ayzVaOQxQ(RcsGroupThread rcsGroupThread, RcsParticipant rcsParticipant) {
        this.f$0 = rcsGroupThread;
        this.f$1 = rcsParticipant;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$addParticipant$6$RcsGroupThread(this.f$1, iRcs, str);
    }
}
