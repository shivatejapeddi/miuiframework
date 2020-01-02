package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessageStore$IvBKppwBc6MDwzIkAi2XJcVB-iI implements RcsServiceCall {
    private final /* synthetic */ RcsEventQueryParams f$0;

    public /* synthetic */ -$$Lambda$RcsMessageStore$IvBKppwBc6MDwzIkAi2XJcVB-iI(RcsEventQueryParams rcsEventQueryParams) {
        this.f$0 = rcsEventQueryParams;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return iRcs.getEvents(this.f$0, str);
    }
}
