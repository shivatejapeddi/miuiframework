package android.net.wifi;

import android.net.wifi.WifiManager.OnWifiUsabilityStatsListener;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WifiManager$1$HHq94tH9ygKDknRiBOn9DYskiOc implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ OnWifiUsabilityStatsListener f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ boolean f$3;
    private final /* synthetic */ WifiUsabilityStatsEntry f$4;

    public /* synthetic */ -$$Lambda$WifiManager$1$HHq94tH9ygKDknRiBOn9DYskiOc(Executor executor, OnWifiUsabilityStatsListener onWifiUsabilityStatsListener, int i, boolean z, WifiUsabilityStatsEntry wifiUsabilityStatsEntry) {
        this.f$0 = executor;
        this.f$1 = onWifiUsabilityStatsListener;
        this.f$2 = i;
        this.f$3 = z;
        this.f$4 = wifiUsabilityStatsEntry;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$WifiManager$1$jN3hHFyvfp2UAuLO8N-VxYJuzY8(this.f$1, this.f$2, this.f$3, this.f$4));
    }
}
