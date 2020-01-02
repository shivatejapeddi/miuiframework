package android.net;

import android.net.SocketKeepalive.AnonymousClass1;
import android.net.SocketKeepalive.Callback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SocketKeepalive$1$xxwNi85oVXVQ_ILhrZNWwo4ppA8 implements Runnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ Callback f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$SocketKeepalive$1$xxwNi85oVXVQ_ILhrZNWwo4ppA8(AnonymousClass1 anonymousClass1, Callback callback, int i) {
        this.f$0 = anonymousClass1;
        this.f$1 = callback;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$onError$4$SocketKeepalive$1(this.f$1, this.f$2);
    }
}
