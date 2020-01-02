package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsFileTransferPart$1I5TANd1JGzUvxVPbWbmYgYHgZg implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsFileTransferPart f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$RcsFileTransferPart$1I5TANd1JGzUvxVPbWbmYgYHgZg(RcsFileTransferPart rcsFileTransferPart, int i) {
        this.f$0 = rcsFileTransferPart;
        this.f$1 = i;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setFileTransferStatus$10$RcsFileTransferPart(this.f$1, iRcs, str);
    }
}
