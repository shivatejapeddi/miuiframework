package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessageStore$nbXWLR_ux8VCEHNEyE7JO0J05YI implements RcsServiceCall {
    private final /* synthetic */ RcsThread f$0;

    public /* synthetic */ -$$Lambda$RcsMessageStore$nbXWLR_ux8VCEHNEyE7JO0J05YI(RcsThread rcsThread) {
        this.f$0 = rcsThread;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return Boolean.valueOf(iRcs.deleteThread(this.f$0.getThreadId(), this.f$0.getThreadType(), str));
    }
}
