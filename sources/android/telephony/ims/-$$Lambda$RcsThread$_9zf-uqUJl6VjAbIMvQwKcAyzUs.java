package android.telephony.ims;

import android.telephony.ims.aidl.IRcs;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsThread$_9zf-uqUJl6VjAbIMvQwKcAyzUs implements RcsServiceCall {
    private final /* synthetic */ RcsThread f$0;
    private final /* synthetic */ RcsOutgoingMessageCreationParams f$1;

    public /* synthetic */ -$$Lambda$RcsThread$_9zf-uqUJl6VjAbIMvQwKcAyzUs(RcsThread rcsThread, RcsOutgoingMessageCreationParams rcsOutgoingMessageCreationParams) {
        this.f$0 = rcsThread;
        this.f$1 = rcsOutgoingMessageCreationParams;
    }

    public final Object methodOnIRcs(IRcs iRcs, String str) {
        return this.f$0.lambda$addOutgoingMessage$2$RcsThread(this.f$1, iRcs, str);
    }
}
