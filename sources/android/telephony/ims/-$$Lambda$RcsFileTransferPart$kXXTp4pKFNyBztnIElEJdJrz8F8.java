package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsFileTransferPart$kXXTp4pKFNyBztnIElEJdJrz8F8 implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsFileTransferPart f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ -$$Lambda$RcsFileTransferPart$kXXTp4pKFNyBztnIElEJdJrz8F8(RcsFileTransferPart rcsFileTransferPart, long j) {
        this.f$0 = rcsFileTransferPart;
        this.f$1 = j;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setLength$17$RcsFileTransferPart(this.f$1, iRcs, str);
    }
}
