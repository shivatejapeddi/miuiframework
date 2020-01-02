package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessageStore$z090Zf4wxRrBwUxXanwm4N3vb7w implements RcsServiceCall {
    private final /* synthetic */ RcsThreadQueryParams f$0;

    public /* synthetic */ -$$Lambda$RcsMessageStore$z090Zf4wxRrBwUxXanwm4N3vb7w(RcsThreadQueryParams rcsThreadQueryParams) {
        this.f$0 = rcsThreadQueryParams;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return iRcs.getRcsThreads(this.f$0, str);
    }
}
