package android.service.voice;

import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$VoiceInteractionService$1$WnZueQJxACwCZWfYsmNtGrcNbEc implements Consumer {
    public static final /* synthetic */ -$$Lambda$VoiceInteractionService$1$WnZueQJxACwCZWfYsmNtGrcNbEc INSTANCE = new -$$Lambda$VoiceInteractionService$1$WnZueQJxACwCZWfYsmNtGrcNbEc();

    private /* synthetic */ -$$Lambda$VoiceInteractionService$1$WnZueQJxACwCZWfYsmNtGrcNbEc() {
    }

    public final void accept(Object obj) {
        ((VoiceInteractionService) ((VoiceInteractionService) obj)).onSoundModelsChangedInternal();
    }
}
