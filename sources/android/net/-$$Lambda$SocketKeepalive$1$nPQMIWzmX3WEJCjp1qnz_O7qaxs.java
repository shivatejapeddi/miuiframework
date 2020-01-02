package android.net;

import android.net.SocketKeepalive.AnonymousClass1;
import android.net.SocketKeepalive.Callback;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SocketKeepalive$1$nPQMIWzmX3WEJCjp1qnz_O7qaxs implements ThrowingRunnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ Executor f$1;
    private final /* synthetic */ Callback f$2;

    public /* synthetic */ -$$Lambda$SocketKeepalive$1$nPQMIWzmX3WEJCjp1qnz_O7qaxs(AnonymousClass1 anonymousClass1, Executor executor, Callback callback) {
        this.f$0 = anonymousClass1;
        this.f$1 = executor;
        this.f$2 = callback;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onDataReceived$7$SocketKeepalive$1(this.f$1, this.f$2);
    }
}
