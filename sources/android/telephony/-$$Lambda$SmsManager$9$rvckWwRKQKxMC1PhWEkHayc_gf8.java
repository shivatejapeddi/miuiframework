package android.telephony;

import android.database.CursorWindow;
import android.telephony.SmsManager.FinancialSmsCallback;
import com.android.internal.util.FunctionalUtils.ThrowingRunnable;
import java.util.concurrent.Executor;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SmsManager$9$rvckWwRKQKxMC1PhWEkHayc_gf8 implements ThrowingRunnable {
    private final /* synthetic */ Executor f$0;
    private final /* synthetic */ FinancialSmsCallback f$1;
    private final /* synthetic */ CursorWindow f$2;

    public /* synthetic */ -$$Lambda$SmsManager$9$rvckWwRKQKxMC1PhWEkHayc_gf8(Executor executor, FinancialSmsCallback financialSmsCallback, CursorWindow cursorWindow) {
        this.f$0 = executor;
        this.f$1 = financialSmsCallback;
        this.f$2 = cursorWindow;
    }

    public final void runOrThrow() {
        this.f$0.execute(new -$$Lambda$SmsManager$9$Ma-xGOTcrGGV8QvZI0NSA6WUbKA(this.f$1, this.f$2));
    }
}
