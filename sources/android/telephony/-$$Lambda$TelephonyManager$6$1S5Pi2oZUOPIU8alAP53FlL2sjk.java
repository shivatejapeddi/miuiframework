package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$6$1S5Pi2oZUOPIU8alAP53FlL2sjk implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ Consumer f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$TelephonyManager$6$1S5Pi2oZUOPIU8alAP53FlL2sjk(Executor executor, Consumer consumer, int i) {
        this.f$0 = executor;
        this.f$1 = consumer;
        this.f$2 = i;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$TelephonyManager$6$AFjFk42NCFYCMG8wA5-6SCfk7No(this.f$1, this.f$2));
    }
}
