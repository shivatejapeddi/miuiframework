package android.telephony.ims;

import android.net.Uri;
import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsGroupThread$LhWdWS6noezEn0xijClZdbKHOas implements RcsServiceCallWithNoReturn {
    private final /* synthetic */ RcsGroupThread f$0;
    private final /* synthetic */ Uri f$1;

    public /* synthetic */ -$$Lambda$RcsGroupThread$LhWdWS6noezEn0xijClZdbKHOas(RcsGroupThread rcsGroupThread, Uri uri) {
        this.f$0 = rcsGroupThread;
        this.f$1 = uri;
    }

    public final void methodOnIRcs(IRcs iRcs, String str) {
        this.f$0.lambda$setConferenceUri$10$RcsGroupThread(this.f$1, iRcs, str);
    }
}
