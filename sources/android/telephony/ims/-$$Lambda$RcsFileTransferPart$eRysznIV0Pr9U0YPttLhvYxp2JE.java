package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsFileTransferPart$eRysznIV0Pr9U0YPttLhvYxp2JE implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsFileTransferPart f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ -$$Lambda$RcsFileTransferPart$eRysznIV0Pr9U0YPttLhvYxp2JE(RcsFileTransferPart rcsFileTransferPart, String str) {
        this.f$0 = rcsFileTransferPart;
        this.f$1 = str;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setFileTransferSessionId$0$RcsFileTransferPart(this.f$1, iRcs, str);
    }
}
