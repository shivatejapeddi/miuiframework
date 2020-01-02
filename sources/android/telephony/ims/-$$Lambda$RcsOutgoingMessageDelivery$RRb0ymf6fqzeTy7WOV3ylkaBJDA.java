package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsOutgoingMessageDelivery$RRb0ymf6fqzeTy7WOV3ylkaBJDA implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsOutgoingMessageDelivery f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ -$$Lambda$RcsOutgoingMessageDelivery$RRb0ymf6fqzeTy7WOV3ylkaBJDA(RcsOutgoingMessageDelivery rcsOutgoingMessageDelivery, long j) {
        this.f$0 = rcsOutgoingMessageDelivery;
        this.f$1 = j;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setDeliveredTimestamp$0$RcsOutgoingMessageDelivery(this.f$1, iRcs, str);
    }
}
