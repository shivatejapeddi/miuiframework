package android.app.role;

import android.os.Bundle;
import android.os.RemoteCallback.OnResultListener;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$RoleManager$m9y_ZqrQy4gHK3mGDXvG129sdjU implements OnResultListener {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ Consumer f$1;

    public /* synthetic */ -$$Lambda$RoleManager$m9y_ZqrQy4gHK3mGDXvG129sdjU(Executor executor, Consumer consumer) {
        this.f$0 = executor;
        this.f$1 = consumer;
    }

    public final void onResult(Bundle bundle) {
        this.f$0.execute(new -$$Lambda$RoleManager$DrSVQgbDoLZaqkfPdGzAK3BvOGQ(bundle, this.f$1));
    }
}
