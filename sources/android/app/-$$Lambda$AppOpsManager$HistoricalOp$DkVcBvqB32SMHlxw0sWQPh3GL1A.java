package android.app;

import android.app.AppOpsManager.HistoricalOp;
import java.util.function.Supplier;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AppOpsManager$HistoricalOp$DkVcBvqB32SMHlxw0sWQPh3GL1A implements Supplier {
    private final /* synthetic */ HistoricalOp f$0;

    public /* synthetic */ -$$Lambda$AppOpsManager$HistoricalOp$DkVcBvqB32SMHlxw0sWQPh3GL1A(HistoricalOp historicalOp) {
        this.f$0 = historicalOp;
    }

    public final Object get() {
        return this.f$0.getOrCreateRejectCount();
    }
}
