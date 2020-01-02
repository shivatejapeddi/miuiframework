package android.net;

import android.net.SocketKeepalive.AnonymousClass1;
import android.net.SocketKeepalive.Callback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SocketKeepalive$1$yVvEaumPDc_celEzvlSEH2FU0nc implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ Callback f$1;

    public /* synthetic */ -$$Lambda$SocketKeepalive$1$yVvEaumPDc_celEzvlSEH2FU0nc(AnonymousClass1 anonymousClass1, Callback callback) {
        this.f$0 = anonymousClass1;
        this.f$1 = callback;
    }

    public final void run() {
        this.f$0.lambda$onDataReceived$6$SocketKeepalive$1(this.f$1);
    }
}
