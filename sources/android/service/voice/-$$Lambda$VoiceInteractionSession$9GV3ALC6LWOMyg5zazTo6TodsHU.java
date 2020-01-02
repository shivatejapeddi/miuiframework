package android.service.voice;

import android.os.Bundle;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$VoiceInteractionSession$9GV3ALC6LWOMyg5zazTo6TodsHU implements Consumer {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ Consumer f$1;

    public /* synthetic */ -$$Lambda$VoiceInteractionSession$9GV3ALC6LWOMyg5zazTo6TodsHU(Executor executor, Consumer consumer) {
        this.f$0 = executor;
        this.f$1 = consumer;
    }

    public final void accept(Object obj) {
        VoiceInteractionSession.lambda$performDirectAction$6(this.f$0, this.f$1, (Bundle) obj);
    }
}
