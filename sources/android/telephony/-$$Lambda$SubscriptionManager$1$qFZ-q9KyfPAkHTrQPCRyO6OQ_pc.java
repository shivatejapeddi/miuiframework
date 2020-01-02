package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SubscriptionManager$1$qFZ-q9KyfPAkHTrQPCRyO6OQ_pc implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ Consumer f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$SubscriptionManager$1$qFZ-q9KyfPAkHTrQPCRyO6OQ_pc(Executor executor, Consumer consumer, int i) {
        this.f$0 = executor;
        this.f$1 = consumer;
        this.f$2 = i;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$SubscriptionManager$1$oi86t06gqdSgTtWgRmCc5dJIfEs(this.f$1, this.f$2));
    }
}
