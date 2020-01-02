package android.service.voice;

import android.os.Bundle;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$VoiceInteractionSession$bujvs7MJfXO9xSx9M8NS3hINZ_k implements Runnable {
    private final /* synthetic */ Consumer f$0;

    public /* synthetic */ -$$Lambda$VoiceInteractionSession$bujvs7MJfXO9xSx9M8NS3hINZ_k(Consumer consumer) {
        this.f$0 = consumer;
    }

    public final void run() {
        this.f$0.accept(Bundle.EMPTY);
    }
}
