package android.net;

import android.net.ConnectivityManager.OnTetheringEntitlementResultListener;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ConnectivityManager$4$GbcJVaUJX-pIrYQi94EYHYBwTJI implements Runnable {
    private final /* synthetic */ OnTetheringEntitlementResultListener f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$ConnectivityManager$4$GbcJVaUJX-pIrYQi94EYHYBwTJI(OnTetheringEntitlementResultListener onTetheringEntitlementResultListener, int i) {
        this.f$0 = onTetheringEntitlementResultListener;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.onTetheringEntitlementResult(this.f$1);
    }
}
