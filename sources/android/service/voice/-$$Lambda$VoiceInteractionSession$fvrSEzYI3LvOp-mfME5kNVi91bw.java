package android.service.voice;

import java.util.List;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$VoiceInteractionSession$fvrSEzYI3LvOp-mfME5kNVi91bw implements Runnable {
    private final /* synthetic */ Consumer f$0;
    private final /* synthetic */ List f$1;

    public /* synthetic */ -$$Lambda$VoiceInteractionSession$fvrSEzYI3LvOp-mfME5kNVi91bw(Consumer consumer, List list) {
        this.f$0 = consumer;
        this.f$1 = list;
    }

    public final void run() {
        this.f$0.accept(this.f$1);
    }
}
