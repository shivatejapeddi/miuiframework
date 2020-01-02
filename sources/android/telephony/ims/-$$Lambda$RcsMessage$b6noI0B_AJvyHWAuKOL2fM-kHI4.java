package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessage$b6noI0B_AJvyHWAuKOL2fM-kHI4 implements RcsServiceCall {
    private final /* synthetic */ RcsMessage f$0;
    private final /* synthetic */ RcsFileTransferCreationParams f$1;

    public /* synthetic */ -$$Lambda$RcsMessage$b6noI0B_AJvyHWAuKOL2fM-kHI4(RcsMessage rcsMessage, RcsFileTransferCreationParams rcsFileTransferCreationParams) {
        this.f$0 = rcsMessage;
        this.f$1 = rcsFileTransferCreationParams;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return this.f$0.lambda$insertFileTransfer$14$RcsMessage(this.f$1, iRcs, str);
    }
}
