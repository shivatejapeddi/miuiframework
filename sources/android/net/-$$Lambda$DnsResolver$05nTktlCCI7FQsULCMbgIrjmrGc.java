package android.net;

import android.os.CancellationSignal.OnCancelListener;
import java.io.FileDescriptor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DnsResolver$05nTktlCCI7FQsULCMbgIrjmrGc implements OnCancelListener {
    private final /* synthetic */ DnsResolver f$0;
    private final /* synthetic */ Object f$1;
    private final /* synthetic */ FileDescriptor f$2;

    public /* synthetic */ -$$Lambda$DnsResolver$05nTktlCCI7FQsULCMbgIrjmrGc(DnsResolver dnsResolver, Object obj, FileDescriptor fileDescriptor) {
        this.f$0 = dnsResolver;
        this.f$1 = obj;
        this.f$2 = fileDescriptor;
    }

    public final void onCancel() {
        this.f$0.lambda$addCancellationSignal$10$DnsResolver(this.f$1, this.f$2);
    }
}
