package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsThread$A9iPL3bU3iiRv1xCYNUNP76n6Vw implements RcsServiceCall {
    private final /* synthetic */ RcsMessageQueryParams f$0;

    public /* synthetic */ -$$Lambda$RcsThread$A9iPL3bU3iiRv1xCYNUNP76n6Vw(RcsMessageQueryParams rcsMessageQueryParams) {
        this.f$0 = rcsMessageQueryParams;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return iRcs.getMessages(this.f$0, str);
    }
}
