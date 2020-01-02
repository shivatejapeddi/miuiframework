package android.telephony.ims;

import android.net.Uri;
import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsFileTransferPart$4bTF8UNuphmPWGI1zJtDN0vEMKQ implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsFileTransferPart f$0;
    private final /* synthetic */ Uri f$1;

    public /* synthetic */ -$$Lambda$RcsFileTransferPart$4bTF8UNuphmPWGI1zJtDN0vEMKQ(RcsFileTransferPart rcsFileTransferPart, Uri uri) {
        this.f$0 = rcsFileTransferPart;
        this.f$1 = uri;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setPreviewUri$19$RcsFileTransferPart(this.f$1, iRcs, str);
    }
}
