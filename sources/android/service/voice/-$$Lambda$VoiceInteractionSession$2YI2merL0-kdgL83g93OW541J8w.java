package android.service.voice;

import android.os.Bundle;
import android.os.CancellationSignal;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$VoiceInteractionSession$2YI2merL0-kdgL83g93OW541J8w implements Consumer {
    private final /* synthetic */ CancellationSignal f$0;

    public /* synthetic */ -$$Lambda$VoiceInteractionSession$2YI2merL0-kdgL83g93OW541J8w(CancellationSignal cancellationSignal) {
        this.f$0 = cancellationSignal;
    }

    public final void accept(Object obj) {
        VoiceInteractionSession.lambda$performDirectAction$3(this.f$0, (Bundle) obj);
    }
}
