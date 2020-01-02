package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$eMNW6lCcxHLvIrcBQvhUXUKuLFU implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ Consumer f$1;

    public /* synthetic */ -$$Lambda$TelephonyManager$eMNW6lCcxHLvIrcBQvhUXUKuLFU(Executor executor, Consumer consumer) {
        this.f$0 = executor;
        this.f$1 = consumer;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$TelephonyManager$qjhLNTc5_Bq4btM7q4y_F5cdAK0(this.f$1));
    }
}
