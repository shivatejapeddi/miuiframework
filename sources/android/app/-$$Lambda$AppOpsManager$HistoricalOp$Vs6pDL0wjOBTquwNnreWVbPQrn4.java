package android.app;

import android.app.AppOpsManager.HistoricalOp;
import java.util.function.Supplier;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AppOpsManager$HistoricalOp$Vs6pDL0wjOBTquwNnreWVbPQrn4 implements Supplier {
    private final /* synthetic */ HistoricalOp f$0;

    public /* synthetic */ -$$Lambda$AppOpsManager$HistoricalOp$Vs6pDL0wjOBTquwNnreWVbPQrn4(HistoricalOp historicalOp) {
        this.f$0 = historicalOp;
    }

    public final Object get() {
        return this.f$0.getOrCreateAccessDuration();
    }
}
