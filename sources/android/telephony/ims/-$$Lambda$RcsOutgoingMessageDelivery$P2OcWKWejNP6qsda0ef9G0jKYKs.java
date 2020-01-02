package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsOutgoingMessageDelivery$P2OcWKWejNP6qsda0ef9G0jKYKs implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsOutgoingMessageDelivery f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ -$$Lambda$RcsOutgoingMessageDelivery$P2OcWKWejNP6qsda0ef9G0jKYKs(RcsOutgoingMessageDelivery rcsOutgoingMessageDelivery, long j) {
        this.f$0 = rcsOutgoingMessageDelivery;
        this.f$1 = j;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setSeenTimestamp$2$RcsOutgoingMessageDelivery(this.f$1, iRcs, str);
    }
}
