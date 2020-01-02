package android.service.appprediction;

import android.app.prediction.AppPredictionSessionId;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AppPredictionService$1$oaGU8LD9Stlihi_KoW_pb0jZjQk implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$AppPredictionService$1$oaGU8LD9Stlihi_KoW_pb0jZjQk INSTANCE = new -$$Lambda$AppPredictionService$1$oaGU8LD9Stlihi_KoW_pb0jZjQk();

    private /* synthetic */ -$$Lambda$AppPredictionService$1$oaGU8LD9Stlihi_KoW_pb0jZjQk() {
    }

    public final void accept(Object obj, Object obj2) {
        ((AppPredictionService) ((AppPredictionService) obj)).doRequestPredictionUpdate((AppPredictionSessionId) obj2);
    }
}
