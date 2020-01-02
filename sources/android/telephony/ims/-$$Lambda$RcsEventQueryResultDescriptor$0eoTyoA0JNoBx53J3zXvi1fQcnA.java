package android.telephony.ims;

import java.util.function.Function;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RcsEventQueryResultDescriptor$0eoTyoA0JNoBx53J3zXvi1fQcnA implements Function {
    private final /* synthetic */ RcsControllerCall f$0;

    public /* synthetic */ -$$Lambda$RcsEventQueryResultDescriptor$0eoTyoA0JNoBx53J3zXvi1fQcnA(RcsControllerCall rcsControllerCall) {
        this.f$0 = rcsControllerCall;
    }

    public final Object apply(Object obj) {
        return ((RcsEventDescriptor) obj).createRcsEvent(this.f$0);
    }
}
