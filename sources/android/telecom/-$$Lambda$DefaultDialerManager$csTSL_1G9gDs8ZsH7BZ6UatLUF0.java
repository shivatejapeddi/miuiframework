package android.telecom;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$DefaultDialerManager$csTSL_1G9gDs8ZsH7BZ6UatLUF0 implements Consumer {
    private final /* synthetic */ CompletableFuture f$0;

    public /* synthetic */ -$$Lambda$DefaultDialerManager$csTSL_1G9gDs8ZsH7BZ6UatLUF0(CompletableFuture completableFuture) {
        this.f$0 = completableFuture;
    }

    public final void accept(Object obj) {
        DefaultDialerManager.lambda$setDefaultDialerApplication$0(this.f$0, (Boolean) obj);
    }
}
