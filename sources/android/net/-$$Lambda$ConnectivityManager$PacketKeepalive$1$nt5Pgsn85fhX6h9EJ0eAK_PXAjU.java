package android.net;

import android.net.ConnectivityManager.PacketKeepalive.AnonymousClass1;
import android.net.ConnectivityManager.PacketKeepaliveCallback;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ConnectivityManager$PacketKeepalive$1$nt5Pgsn85fhX6h9EJ0eAK_PXAjU implements ThrowingRunnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ PacketKeepaliveCallback f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$ConnectivityManager$PacketKeepalive$1$nt5Pgsn85fhX6h9EJ0eAK_PXAjU(AnonymousClass1 anonymousClass1, PacketKeepaliveCallback packetKeepaliveCallback, int i) {
        this.f$0 = anonymousClass1;
        this.f$1 = packetKeepaliveCallback;
        this.f$2 = i;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onError$5$ConnectivityManager$PacketKeepalive$1(this.f$1, this.f$2);
    }
}
