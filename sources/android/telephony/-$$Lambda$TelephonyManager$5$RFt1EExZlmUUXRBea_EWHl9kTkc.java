package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$5$RFt1EExZlmUUXRBea_EWHl9kTkc implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ Consumer f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$TelephonyManager$5$RFt1EExZlmUUXRBea_EWHl9kTkc(Executor executor, Consumer consumer, int i) {
        this.f$0 = executor;
        this.f$1 = consumer;
        this.f$2 = i;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$TelephonyManager$5$dLg4hbo46SmKP0wtKbXAlS8hCpg(this.f$1, this.f$2));
    }
}
