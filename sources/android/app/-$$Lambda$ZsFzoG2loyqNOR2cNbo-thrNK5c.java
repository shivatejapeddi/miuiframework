package android.app;

import android.os.Bundle;
import android.os.RemoteCallback;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ZsFzoG2loyqNOR2cNbo-thrNK5c implements Consumer {
    private final /* synthetic */ RemoteCallback f$0;

    public /* synthetic */ -$$Lambda$ZsFzoG2loyqNOR2cNbo-thrNK5c(RemoteCallback remoteCallback) {
        this.f$0 = remoteCallback;
    }

    public final void accept(Object obj) {
        this.f$0.sendResult((Bundle) obj);
    }
}
