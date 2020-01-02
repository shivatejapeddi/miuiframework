package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsThread$9gFw0KtL-BczxOxCksL2zOV2xHM implements RcsServiceCall {
    private final /* synthetic */ RcsThread f$0;
    private final /* synthetic */ RcsIncomingMessageCreationParams f$1;

    public /* synthetic */ -$$Lambda$RcsThread$9gFw0KtL-BczxOxCksL2zOV2xHM(RcsThread rcsThread, RcsIncomingMessageCreationParams rcsIncomingMessageCreationParams) {
        this.f$0 = rcsThread;
        this.f$1 = rcsIncomingMessageCreationParams;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return this.f$0.lambda$addIncomingMessage$1$RcsThread(this.f$1, iRcs, str);
    }
}
