package android.service.sms;

import android.os.Bundle;
import android.os.RemoteCallback;
import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$FinancialSmsService$FinancialSmsServiceWrapper$XFtzKfY0m01I8W-d6pG7NlmdfiQ implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$FinancialSmsService$FinancialSmsServiceWrapper$XFtzKfY0m01I8W-d6pG7NlmdfiQ INSTANCE = new -$$Lambda$FinancialSmsService$FinancialSmsServiceWrapper$XFtzKfY0m01I8W-d6pG7NlmdfiQ();

    private /* synthetic */ -$$Lambda$FinancialSmsService$FinancialSmsServiceWrapper$XFtzKfY0m01I8W-d6pG7NlmdfiQ() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((FinancialSmsService) ((FinancialSmsService) obj)).getSmsMessages((RemoteCallback) obj2, (Bundle) obj3);
    }
}
