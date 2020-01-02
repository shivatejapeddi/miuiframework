package android.app;

import android.app.ActivityThread.ActivityClientRecord;
import android.os.RemoteCallback;
import java.util.List;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ActivityThread$FmvGY8exyv0L0oqZrnunpl8OFI8 implements Consumer {
    private final /* synthetic */ ActivityClientRecord f$0;
    private final /* synthetic */ RemoteCallback f$1;

    public /* synthetic */ -$$Lambda$ActivityThread$FmvGY8exyv0L0oqZrnunpl8OFI8(ActivityClientRecord activityClientRecord, RemoteCallback remoteCallback) {
        this.f$0 = activityClientRecord;
        this.f$1 = remoteCallback;
    }

    public final void accept(Object obj) {
        ActivityThread.lambda$handleRequestDirectActions$0(this.f$0, this.f$1, (List) obj);
    }
}
