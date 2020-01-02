package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsOutgoingMessageDelivery$l9Yzsl9k4Z30dUsRJ0yJpKeg9jk implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsOutgoingMessageDelivery f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$RcsOutgoingMessageDelivery$l9Yzsl9k4Z30dUsRJ0yJpKeg9jk(RcsOutgoingMessageDelivery rcsOutgoingMessageDelivery, int i) {
        this.f$0 = rcsOutgoingMessageDelivery;
        this.f$1 = i;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setStatus$4$RcsOutgoingMessageDelivery(this.f$1, iRcs, str);
    }
}
