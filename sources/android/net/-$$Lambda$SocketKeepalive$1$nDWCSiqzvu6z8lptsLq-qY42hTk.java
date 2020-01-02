package android.net;

import android.net.SocketKeepalive.AnonymousClass1;
import android.net.SocketKeepalive.Callback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SocketKeepalive$1$nDWCSiqzvu6z8lptsLq-qY42hTk implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ Callback f$2;

    public /* synthetic */ -$$Lambda$SocketKeepalive$1$nDWCSiqzvu6z8lptsLq-qY42hTk(AnonymousClass1 anonymousClass1, int i, Callback callback) {
        this.f$0 = anonymousClass1;
        this.f$1 = i;
        this.f$2 = callback;
    }

    public final void run() {
        this.f$0.lambda$onStarted$0$SocketKeepalive$1(this.f$1, this.f$2);
    }
}
