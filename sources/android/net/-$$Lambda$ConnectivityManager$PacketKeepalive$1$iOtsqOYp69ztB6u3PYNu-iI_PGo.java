package android.net;

import android.net.ConnectivityManager.PacketKeepalive.AnonymousClass1;
import android.net.ConnectivityManager.PacketKeepaliveCallback;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ConnectivityManager$PacketKeepalive$1$iOtsqOYp69ztB6u3PYNu-iI_PGo implements ThrowingRunnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ PacketKeepaliveCallback f$2;

    public /* synthetic */ -$$Lambda$ConnectivityManager$PacketKeepalive$1$iOtsqOYp69ztB6u3PYNu-iI_PGo(AnonymousClass1 anonymousClass1, int i, PacketKeepaliveCallback packetKeepaliveCallback) {
        this.f$0 = anonymousClass1;
        this.f$1 = i;
        this.f$2 = packetKeepaliveCallback;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onStarted$1$ConnectivityManager$PacketKeepalive$1(this.f$1, this.f$2);
    }
}
