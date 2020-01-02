package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsFileTransferPart$iFRtCc6m4Iup_st7fFqTiBlhq4o implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsFileTransferPart f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ -$$Lambda$RcsFileTransferPart$iFRtCc6m4Iup_st7fFqTiBlhq4o(RcsFileTransferPart rcsFileTransferPart, long j) {
        this.f$0 = rcsFileTransferPart;
        this.f$1 = j;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setFileSize$6$RcsFileTransferPart(this.f$1, iRcs, str);
    }
}
