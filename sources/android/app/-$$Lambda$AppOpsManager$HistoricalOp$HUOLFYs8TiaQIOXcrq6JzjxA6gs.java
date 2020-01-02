package android.app;

import android.app.AppOpsManager.HistoricalOp;
import java.util.function.Supplier;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AppOpsManager$HistoricalOp$HUOLFYs8TiaQIOXcrq6JzjxA6gs implements Supplier {
    private final /* synthetic */ HistoricalOp f$0;

    public /* synthetic */ -$$Lambda$AppOpsManager$HistoricalOp$HUOLFYs8TiaQIOXcrq6JzjxA6gs(HistoricalOp historicalOp) {
        this.f$0 = historicalOp;
    }

    public final Object get() {
        return this.f$0.getOrCreateAccessCount();
    }
}
