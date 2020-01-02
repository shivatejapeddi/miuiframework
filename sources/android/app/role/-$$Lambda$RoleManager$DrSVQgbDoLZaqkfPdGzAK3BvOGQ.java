package android.app.role;

import android.os.Bundle;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RoleManager$DrSVQgbDoLZaqkfPdGzAK3BvOGQ implements Runnable {
    private final /* synthetic */ Bundle f$0;
    private final /* synthetic */ Consumer f$1;

    public /* synthetic */ -$$Lambda$RoleManager$DrSVQgbDoLZaqkfPdGzAK3BvOGQ(Bundle bundle, Consumer consumer) {
        this.f$0 = bundle;
        this.f$1 = consumer;
    }

    public final void run() {
        RoleManager.lambda$createRemoteCallback$0(this.f$0, this.f$1);
    }
}
