package android.net;

import android.net.SocketKeepalive.AnonymousClass1;
import android.net.SocketKeepalive.Callback;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SocketKeepalive$1$m-VPtyb2YaC8aWd5gXQYgFGhVbM implements ThrowingRunnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ Callback f$2;

    public /* synthetic */ -$$Lambda$SocketKeepalive$1$m-VPtyb2YaC8aWd5gXQYgFGhVbM(AnonymousClass1 anonymousClass1, int i, Callback callback) {
        this.f$0 = anonymousClass1;
        this.f$1 = i;
        this.f$2 = callback;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onStarted$1$SocketKeepalive$1(this.f$1, this.f$2);
    }
}
