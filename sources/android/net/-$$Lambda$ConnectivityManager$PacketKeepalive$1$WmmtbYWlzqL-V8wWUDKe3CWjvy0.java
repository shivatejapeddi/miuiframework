package android.net;

import android.net.ConnectivityManager.PacketKeepalive.AnonymousClass1;
import android.net.ConnectivityManager.PacketKeepaliveCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ConnectivityManager$PacketKeepalive$1$WmmtbYWlzqL-V8wWUDKe3CWjvy0 implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ PacketKeepaliveCallback f$1;

    public /* synthetic */ -$$Lambda$ConnectivityManager$PacketKeepalive$1$WmmtbYWlzqL-V8wWUDKe3CWjvy0(AnonymousClass1 anonymousClass1, PacketKeepaliveCallback packetKeepaliveCallback) {
        this.f$0 = anonymousClass1;
        this.f$1 = packetKeepaliveCallback;
    }

    public final void run() {
        this.f$0.lambda$onStopped$2$ConnectivityManager$PacketKeepalive$1(this.f$1);
    }
}
