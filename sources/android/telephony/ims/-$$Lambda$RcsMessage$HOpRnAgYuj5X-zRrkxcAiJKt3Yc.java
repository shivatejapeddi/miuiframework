package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessage$HOpRnAgYuj5X-zRrkxcAiJKt3Yc implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsMessage f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$RcsMessage$HOpRnAgYuj5X-zRrkxcAiJKt3Yc(RcsMessage rcsMessage, int i) {
        this.f$0 = rcsMessage;
        this.f$1 = i;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setStatus$2$RcsMessage(this.f$1, iRcs, str);
    }
}
