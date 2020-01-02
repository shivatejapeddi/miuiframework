package android.net.wifi;

import android.net.wifi.WifiManager.OnWifiUsabilityStatsListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$WifiManager$1$jN3hHFyvfp2UAuLO8N-VxYJuzY8 implements Runnable {
    private final /* synthetic */ OnWifiUsabilityStatsListener f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ WifiUsabilityStatsEntry f$3;

    public /* synthetic */ -$$Lambda$WifiManager$1$jN3hHFyvfp2UAuLO8N-VxYJuzY8(OnWifiUsabilityStatsListener onWifiUsabilityStatsListener, int i, boolean z, WifiUsabilityStatsEntry wifiUsabilityStatsEntry) {
        this.f$0 = onWifiUsabilityStatsListener;
        this.f$1 = i;
        this.f$2 = z;
        this.f$3 = wifiUsabilityStatsEntry;
    }

    public final void run() {
        this.f$0.onWifiUsabilityStats(this.f$1, this.f$2, this.f$3);
    }
}
