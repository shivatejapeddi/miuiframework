package android.service.voice;

import android.service.voice.VoiceInteractionSession.ActivityId;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$lR4OeV3qsxUC-rL-7Xl2vrhTvEo implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$lR4OeV3qsxUC-rL-7Xl2vrhTvEo INSTANCE = new -$$Lambda$lR4OeV3qsxUC-rL-7Xl2vrhTvEo();

    private /* synthetic */ -$$Lambda$lR4OeV3qsxUC-rL-7Xl2vrhTvEo() {
    }

    public final void accept(Object obj, Object obj2) {
        ((VoiceInteractionSession) obj).onDirectActionsInvalidated((ActivityId) obj2);
    }
}
