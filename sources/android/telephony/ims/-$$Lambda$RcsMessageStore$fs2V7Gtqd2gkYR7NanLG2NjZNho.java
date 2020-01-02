package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessageStore$fs2V7Gtqd2gkYR7NanLG2NjZNho implements RcsServiceCall {
    private final /* synthetic */ RcsQueryContinuationToken f$0;

    public /* synthetic */ -$$Lambda$RcsMessageStore$fs2V7Gtqd2gkYR7NanLG2NjZNho(RcsQueryContinuationToken rcsQueryContinuationToken) {
        this.f$0 = rcsQueryContinuationToken;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return iRcs.getMessagesWithToken(this.f$0, str);
    }
}
