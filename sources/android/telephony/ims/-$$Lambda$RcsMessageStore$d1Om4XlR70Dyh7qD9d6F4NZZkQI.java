package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessageStore$d1Om4XlR70Dyh7qD9d6F4NZZkQI implements RcsServiceCall {
    private final /* synthetic */ String f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ -$$Lambda$RcsMessageStore$d1Om4XlR70Dyh7qD9d6F4NZZkQI(String str, String str2) {
        this.f$0 = str;
        this.f$1 = str2;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return Integer.valueOf(iRcs.createRcsParticipant(this.f$0, this.f$1, str));
    }
}
