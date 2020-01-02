package android.net;

import android.net.SocketKeepalive.AnonymousClass1;
import android.net.SocketKeepalive.Callback;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SocketKeepalive$1$0jK7H49vYYFjBANIXTac00ocnSo implements ThrowingRunnable {
    private final /* synthetic */ AnonymousClass1 f$0;
    private final /* synthetic */ Executor f$1;
    private final /* synthetic */ Callback f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ -$$Lambda$SocketKeepalive$1$0jK7H49vYYFjBANIXTac00ocnSo(AnonymousClass1 anonymousClass1, Executor executor, Callback callback, int i) {
        this.f$0 = anonymousClass1;
        this.f$1 = executor;
        this.f$2 = callback;
        this.f$3 = i;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onError$5$SocketKeepalive$1(this.f$1, this.f$2, this.f$3);
    }
}
