package android.service.voice;

import android.os.Bundle;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$VoiceInteractionSession$sg0qPgWHpOBD2lLJ7BGNEVSsBdo implements Runnable {
    private final /* synthetic */ Consumer f$0;
    private final /* synthetic */ Bundle f$1;

    public /* synthetic */ -$$Lambda$VoiceInteractionSession$sg0qPgWHpOBD2lLJ7BGNEVSsBdo(Consumer consumer, Bundle bundle) {
        this.f$0 = consumer;
        this.f$1 = bundle;
    }

    public final void run() {
        this.f$0.accept(this.f$1);
    }
}
