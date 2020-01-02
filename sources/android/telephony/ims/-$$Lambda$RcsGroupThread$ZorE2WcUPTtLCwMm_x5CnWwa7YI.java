package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsGroupThread$ZorE2WcUPTtLCwMm_x5CnWwa7YI implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsGroupThread f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ -$$Lambda$RcsGroupThread$ZorE2WcUPTtLCwMm_x5CnWwa7YI(RcsGroupThread rcsGroupThread, String str) {
        this.f$0 = rcsGroupThread;
        this.f$1 = str;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setGroupName$1$RcsGroupThread(this.f$1, iRcs, str);
    }
}
