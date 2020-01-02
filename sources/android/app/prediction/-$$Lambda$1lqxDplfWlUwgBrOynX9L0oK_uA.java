package android.app.prediction;

import android.app.prediction.AppPredictor.Callback;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$1lqxDplfWlUwgBrOynX9L0oK_uA implements Consumer {
    private final /* synthetic */ Callback f$0;

    public /* synthetic */ -$$Lambda$1lqxDplfWlUwgBrOynX9L0oK_uA(Callback callback) {
        this.f$0 = callback;
    }

    public final void accept(Object obj) {
        this.f$0.onTargetsAvailable((List) obj);
    }
}
