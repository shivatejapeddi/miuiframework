package android.telephony;

import android.telephony.TelephonyManager.CellInfoCallback;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.List;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$2$hWPf2raNadUBIhTQLEUpRhHWKoI implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ CellInfoCallback f$1;
    private final /* synthetic */ List f$2;

    public /* synthetic */ -$$Lambda$TelephonyManager$2$hWPf2raNadUBIhTQLEUpRhHWKoI(Executor executor, CellInfoCallback cellInfoCallback, List list) {
        this.f$0 = executor;
        this.f$1 = cellInfoCallback;
        this.f$2 = list;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$TelephonyManager$2$l6Pazxfi7QghMr2Z0MpduhNe6yc(this.f$1, this.f$2));
    }
}
