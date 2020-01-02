package android.service.appprediction;

import android.app.prediction.AppPredictionSessionId;
import java.util.function.BiConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AppPredictionService$1$oZsrXgV2j_8Zo7GiDdpYvbTz4h8 implements BiConsumer {
    public static final /* synthetic */ -$$Lambda$AppPredictionService$1$oZsrXgV2j_8Zo7GiDdpYvbTz4h8 INSTANCE = new -$$Lambda$AppPredictionService$1$oZsrXgV2j_8Zo7GiDdpYvbTz4h8();

    private /* synthetic */ -$$Lambda$AppPredictionService$1$oZsrXgV2j_8Zo7GiDdpYvbTz4h8() {
    }

    public final void accept(Object obj, Object obj2) {
        ((AppPredictionService) ((AppPredictionService) obj)).doDestroyPredictionSession((AppPredictionSessionId) obj2);
    }
}
