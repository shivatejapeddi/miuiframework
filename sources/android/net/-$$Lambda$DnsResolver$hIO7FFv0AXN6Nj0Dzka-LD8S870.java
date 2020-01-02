package android.net;

import android.net.DnsResolver.Callback;
import android.os.CancellationSignal;
import java.io.FileDescriptor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DnsResolver$hIO7FFv0AXN6Nj0Dzka-LD8S870 implements Runnable {
    private final /* synthetic */ Object f$0;
    private final /* synthetic */ CancellationSignal f$1;
    private final /* synthetic */ FileDescriptor f$2;
    private final /* synthetic */ Callback f$3;

    public /* synthetic */ -$$Lambda$DnsResolver$hIO7FFv0AXN6Nj0Dzka-LD8S870(Object obj, CancellationSignal cancellationSignal, FileDescriptor fileDescriptor, Callback callback) {
        this.f$0 = obj;
        this.f$1 = cancellationSignal;
        this.f$2 = fileDescriptor;
        this.f$3 = callback;
    }

    public final void run() {
        DnsResolver.lambda$registerFDListener$8(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
