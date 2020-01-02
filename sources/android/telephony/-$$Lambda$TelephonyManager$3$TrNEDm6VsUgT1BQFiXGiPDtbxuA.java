package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$3$TrNEDm6VsUgT1BQFiXGiPDtbxuA implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ NumberVerificationCallback f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ -$$Lambda$TelephonyManager$3$TrNEDm6VsUgT1BQFiXGiPDtbxuA(Executor executor, NumberVerificationCallback numberVerificationCallback, int i) {
        this.f$0 = executor;
        this.f$1 = numberVerificationCallback;
        this.f$2 = i;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$TelephonyManager$3$VM3y0XwyxZN6vR6ERQTngCQIICc(this.f$1, this.f$2));
    }
}
