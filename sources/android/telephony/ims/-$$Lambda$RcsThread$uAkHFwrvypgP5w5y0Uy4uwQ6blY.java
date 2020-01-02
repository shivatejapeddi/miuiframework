package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsThread$uAkHFwrvypgP5w5y0Uy4uwQ6blY implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsThread f$0;
    private final /* synthetic */ RcsMessage f$1;

    public /* synthetic */ -$$Lambda$RcsThread$uAkHFwrvypgP5w5y0Uy4uwQ6blY(RcsThread rcsThread, RcsMessage rcsMessage) {
        this.f$0 = rcsThread;
        this.f$1 = rcsMessage;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$deleteMessage$3$RcsThread(this.f$1, iRcs, str);
    }
}
