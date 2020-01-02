package android.telephony;

import android.database.CursorWindow;
import android.telephony.SmsManager.FinancialSmsCallback;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$SmsManager$9$Ma-xGOTcrGGV8QvZI0NSA6WUbKA implements Runnable {
    private final /* synthetic */ FinancialSmsCallback f$0;
    private final /* synthetic */ CursorWindow f$1;

    public /* synthetic */ -$$Lambda$SmsManager$9$Ma-xGOTcrGGV8QvZI0NSA6WUbKA(FinancialSmsCallback financialSmsCallback, CursorWindow cursorWindow) {
        this.f$0 = financialSmsCallback;
        this.f$1 = cursorWindow;
    }

    public final void run() {
        this.f$0.onFinancialSmsMessages(this.f$1);
    }
}
