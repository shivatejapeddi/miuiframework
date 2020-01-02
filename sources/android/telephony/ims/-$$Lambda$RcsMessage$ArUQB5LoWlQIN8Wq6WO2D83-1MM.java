package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessage$ArUQB5LoWlQIN8Wq6WO2D83-1MM implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsFileTransferPart f$0;

    public /* synthetic */ -$$Lambda$RcsMessage$ArUQB5LoWlQIN8Wq6WO2D83-1MM(RcsFileTransferPart rcsFileTransferPart) {
        this.f$0 = rcsFileTransferPart;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        iRcs.deleteFileTransfer(this.f$0.getId(), str);
    }
}
