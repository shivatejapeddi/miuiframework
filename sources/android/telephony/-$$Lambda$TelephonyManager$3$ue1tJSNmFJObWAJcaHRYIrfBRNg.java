package android.telephony;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$TelephonyManager$3$ue1tJSNmFJObWAJcaHRYIrfBRNg implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ NumberVerificationCallback f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ -$$Lambda$TelephonyManager$3$ue1tJSNmFJObWAJcaHRYIrfBRNg(Executor executor, NumberVerificationCallback numberVerificationCallback, String str) {
        this.f$0 = executor;
        this.f$1 = numberVerificationCallback;
        this.f$2 = str;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$TelephonyManager$3$LPMNUsxM8QRYWmnzGtrEYPm5sAs(this.f$1, this.f$2));
    }
}
