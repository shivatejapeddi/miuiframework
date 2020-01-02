package android.net;

import android.net.ConnectivityManager.PacketKeepalive.AnonymousClass1;
import android.net.ConnectivityManager.PacketKeepaliveCallback;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ConnectivityManager$PacketKeepalive$1$-H5tzn67t3ydWL8tXpl9UyOmDcc implements ThrowingRunnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ PacketKeepaliveCallback f$1;

    public /* synthetic */ -$$Lambda$ConnectivityManager$PacketKeepalive$1$-H5tzn67t3ydWL8tXpl9UyOmDcc(AnonymousClass1 anonymousClass1, PacketKeepaliveCallback packetKeepaliveCallback) {
        this.f$0 = anonymousClass1;
        this.f$1 = packetKeepaliveCallback;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onStopped$3$ConnectivityManager$PacketKeepalive$1(this.f$1);
    }
}
