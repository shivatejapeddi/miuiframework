package android.net;

import android.net.ConnectivityManager.OnTetheringEventCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ConnectivityManager$3$Hh_etCA-vVs2IV58umWLOd1O4yk implements Runnable {
    private final /* synthetic */ OnTetheringEventCallback f$0;
    private final /* synthetic */ Network f$1;

    public /* synthetic */ -$$Lambda$ConnectivityManager$3$Hh_etCA-vVs2IV58umWLOd1O4yk(OnTetheringEventCallback onTetheringEventCallback, Network network) {
        this.f$0 = onTetheringEventCallback;
        this.f$1 = network;
    }

    public final void run() {
        this.f$0.onUpstreamChanged(this.f$1);
    }
}
