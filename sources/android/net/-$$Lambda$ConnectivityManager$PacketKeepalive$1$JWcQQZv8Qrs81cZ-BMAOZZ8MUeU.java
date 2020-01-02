package android.net;

import android.net.ConnectivityManager.PacketKeepalive.AnonymousClass1;
import android.net.ConnectivityManager.PacketKeepaliveCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ConnectivityManager$PacketKeepalive$1$JWcQQZv8Qrs81cZ-BMAOZZ8MUeU implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ PacketKeepaliveCallback f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$ConnectivityManager$PacketKeepalive$1$JWcQQZv8Qrs81cZ-BMAOZZ8MUeU(AnonymousClass1 anonymousClass1, PacketKeepaliveCallback packetKeepaliveCallback, int i) {
        this.f$0 = anonymousClass1;
        this.f$1 = packetKeepaliveCallback;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$onError$4$ConnectivityManager$PacketKeepalive$1(this.f$1, this.f$2);
    }
}
