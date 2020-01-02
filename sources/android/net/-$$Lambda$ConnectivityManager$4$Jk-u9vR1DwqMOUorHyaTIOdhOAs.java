package android.net;

import android.net.ConnectivityManager.OnTetheringEntitlementResultListener;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ConnectivityManager$4$Jk-u9vR1DwqMOUorHyaTIOdhOAs implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ OnTetheringEntitlementResultListener f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$ConnectivityManager$4$Jk-u9vR1DwqMOUorHyaTIOdhOAs(Executor executor, OnTetheringEntitlementResultListener onTetheringEntitlementResultListener, int i) {
        this.f$0 = executor;
        this.f$1 = onTetheringEntitlementResultListener;
        this.f$2 = i;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$ConnectivityManager$4$GbcJVaUJX-pIrYQi94EYHYBwTJI(this.f$1, this.f$2));
    }
}
