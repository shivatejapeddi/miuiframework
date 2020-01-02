package android.telephony.ims;

import android.net.Uri;
import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsMessageStore$g309WUVpYx8N7s-uWdUAGJXtJOs implements RcsServiceCall {
    private final /* synthetic */ int[] f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ Uri f$2;

    public /* synthetic */ -$$Lambda$RcsMessageStore$g309WUVpYx8N7s-uWdUAGJXtJOs(int[] iArr, String str, Uri uri) {
        this.f$0 = iArr;
        this.f$1 = str;
        this.f$2 = uri;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return Integer.valueOf(iRcs.createGroupThread(this.f$0, this.f$1, this.f$2, str));
    }
}
