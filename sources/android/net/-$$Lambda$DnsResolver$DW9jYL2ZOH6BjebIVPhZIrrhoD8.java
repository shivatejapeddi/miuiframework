package android.net;

import android.os.CancellationSignal.OnCancelListener;
import java.io.FileDescriptor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DnsResolver$DW9jYL2ZOH6BjebIVPhZIrrhoD8 implements OnCancelListener {
    private final /* synthetic */ DnsResolver f$0;
    private final /* synthetic */ Object f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ FileDescriptor f$3;
    private final /* synthetic */ boolean f$4;
    private final /* synthetic */ FileDescriptor f$5;

    public /* synthetic */ -$$Lambda$DnsResolver$DW9jYL2ZOH6BjebIVPhZIrrhoD8(DnsResolver dnsResolver, Object obj, boolean z, FileDescriptor fileDescriptor, boolean z2, FileDescriptor fileDescriptor2) {
        this.f$0 = dnsResolver;
        this.f$1 = obj;
        this.f$2 = z;
        this.f$3 = fileDescriptor;
        this.f$4 = z2;
        this.f$5 = fileDescriptor2;
    }

    public final void onCancel() {
        this.f$0.lambda$query$6$DnsResolver(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
