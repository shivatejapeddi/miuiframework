package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsFileTransferPart$_U_JpxTv_8vqlG8zHOxxNMMBqjQ implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsFileTransferPart f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ -$$Lambda$RcsFileTransferPart$_U_JpxTv_8vqlG8zHOxxNMMBqjQ(RcsFileTransferPart rcsFileTransferPart, String str) {
        this.f$0 = rcsFileTransferPart;
        this.f$1 = str;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setContentMimeType$4$RcsFileTransferPart(this.f$1, iRcs, str);
    }
}
