package android.net;

import android.net.DnsResolver.Callback;
import android.os.CancellationSignal;
import android.os.MessageQueue;
import android.os.MessageQueue.OnFileDescriptorEventListener;
import java.io.FileDescriptor;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DnsResolver$kxKi6qjPYeR_SIipxW4tYpxyM50 implements OnFileDescriptorEventListener {
    private final /* synthetic */ MessageQueue f$0;
    private final /* synthetic */ Executor f$1;
    private final /* synthetic */ Object f$2;
    private final /* synthetic */ CancellationSignal f$3;
    private final /* synthetic */ Callback f$4;

    public /* synthetic */ -$$Lambda$DnsResolver$kxKi6qjPYeR_SIipxW4tYpxyM50(MessageQueue messageQueue, Executor executor, Object obj, CancellationSignal cancellationSignal, Callback callback) {
        this.f$0 = messageQueue;
        this.f$1 = executor;
        this.f$2 = obj;
        this.f$3 = cancellationSignal;
        this.f$4 = callback;
    }

    public final int onFileDescriptorEvents(FileDescriptor fileDescriptor, int i) {
        return DnsResolver.lambda$registerFDListener$9(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, fileDescriptor, i);
    }
}
