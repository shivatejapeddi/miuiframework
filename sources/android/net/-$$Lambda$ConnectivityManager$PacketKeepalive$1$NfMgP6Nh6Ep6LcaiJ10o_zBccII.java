package android.net;

import android.net.ConnectivityManager.PacketKeepalive.AnonymousClass1;
import android.net.ConnectivityManager.PacketKeepaliveCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ConnectivityManager$PacketKeepalive$1$NfMgP6Nh6Ep6LcaiJ10o_zBccII implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ PacketKeepaliveCallback f$2;

    public /* synthetic */ -$$Lambda$ConnectivityManager$PacketKeepalive$1$NfMgP6Nh6Ep6LcaiJ10o_zBccII(AnonymousClass1 anonymousClass1, int i, PacketKeepaliveCallback packetKeepaliveCallback) {
        this.f$0 = anonymousClass1;
        this.f$1 = i;
        this.f$2 = packetKeepaliveCallback;
    }

    public final void run() {
        this.f$0.lambda$onStarted$0$ConnectivityManager$PacketKeepalive$1(this.f$1, this.f$2);
    }
}
