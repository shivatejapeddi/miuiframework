package android.service.voice;

import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$VoiceInteractionService$1$ILMD_OnlN3EpU4AqKW9H-tgCoMg implements Consumer {
    public static final /* synthetic */ -$$Lambda$VoiceInteractionService$1$ILMD_OnlN3EpU4AqKW9H-tgCoMg INSTANCE = new -$$Lambda$VoiceInteractionService$1$ILMD_OnlN3EpU4AqKW9H-tgCoMg();

    private /* synthetic */ -$$Lambda$VoiceInteractionService$1$ILMD_OnlN3EpU4AqKW9H-tgCoMg() {
    }

    public final void accept(Object obj) {
        ((VoiceInteractionService) ((VoiceInteractionService) obj)).onShutdownInternal();
    }
}
