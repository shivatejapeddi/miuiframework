package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessageStore$5QXAY7bGFdmsWgLF0pk1tyYYovg implements RcsServiceCall {
    private final /* synthetic */ RcsMessageQueryParams f$0;

    public /* synthetic */ -$$Lambda$RcsMessageStore$5QXAY7bGFdmsWgLF0pk1tyYYovg(RcsMessageQueryParams rcsMessageQueryParams) {
        this.f$0 = rcsMessageQueryParams;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return iRcs.getMessages(this.f$0, str);
    }
}
