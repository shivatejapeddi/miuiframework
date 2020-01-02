package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$Rcs1To1Thread$vx_evSYitgJIMB6l-hANvSJpdBE implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ Rcs1To1Thread f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ -$$Lambda$Rcs1To1Thread$vx_evSYitgJIMB6l-hANvSJpdBE(Rcs1To1Thread rcs1To1Thread, long j) {
        this.f$0 = rcs1To1Thread;
        this.f$1 = j;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setFallbackThreadId$1$Rcs1To1Thread(this.f$1, iRcs, str);
    }
}
