package android.app;

import android.app.AppOpsManager.HistoricalOps;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AppOpsManager$VfNXTtJaOeEFrdIj0oDWr_N9nks implements Runnable {
    private final /* synthetic */ Consumer f$0;
    private final /* synthetic */ HistoricalOps f$1;

    public /* synthetic */ -$$Lambda$AppOpsManager$VfNXTtJaOeEFrdIj0oDWr_N9nks(Consumer consumer, HistoricalOps historicalOps) {
        this.f$0 = consumer;
        this.f$1 = historicalOps;
    }

    public final void run() {
        this.f$0.accept(this.f$1);
    }
}
