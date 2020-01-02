package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessage$tq1Iu9i2c3B7IAVANp7f9nz6BQI implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsMessage f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ -$$Lambda$RcsMessage$tq1Iu9i2c3B7IAVANp7f9nz6BQI(RcsMessage rcsMessage, long j) {
        this.f$0 = rcsMessage;
        this.f$1 = j;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setOriginationTimestamp$4$RcsMessage(this.f$1, iRcs, str);
    }
}
