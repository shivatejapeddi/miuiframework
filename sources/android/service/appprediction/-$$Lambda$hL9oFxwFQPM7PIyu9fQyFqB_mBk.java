package android.service.appprediction;

import android.app.prediction.AppPredictionSessionId;
import android.os.CancellationSignal;
import com.android.internal.util.function.QuintConsumer;
import java.util.List;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$hL9oFxwFQPM7PIyu9fQyFqB_mBk implements QuintConsumer {
    public static final /* synthetic */ -$$Lambda$hL9oFxwFQPM7PIyu9fQyFqB_mBk INSTANCE = new -$$Lambda$hL9oFxwFQPM7PIyu9fQyFqB_mBk();

    private /* synthetic */ -$$Lambda$hL9oFxwFQPM7PIyu9fQyFqB_mBk() {
    }

    public final void accept(Object obj, Object obj2, Object obj3, Object obj4, Object obj5) {
        ((AppPredictionService) obj).onSortAppTargets((AppPredictionSessionId) obj2, (List) obj3, (CancellationSignal) obj4, (CallbackWrapper) obj5);
    }
}
