package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsFileTransferPart$NeUx42-gy02-DXOOj3iF2Y92GoU implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsFileTransferPart f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ -$$Lambda$RcsFileTransferPart$NeUx42-gy02-DXOOj3iF2Y92GoU(RcsFileTransferPart rcsFileTransferPart, long j) {
        this.f$0 = rcsFileTransferPart;
        this.f$1 = j;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setTransferOffset$8$RcsFileTransferPart(this.f$1, iRcs, str);
    }
}
