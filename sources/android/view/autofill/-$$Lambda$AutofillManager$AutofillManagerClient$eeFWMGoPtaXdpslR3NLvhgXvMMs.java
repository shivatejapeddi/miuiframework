package android.view.autofill;

import com.android.internal.os.IResultReceiver;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AutofillManager$AutofillManagerClient$eeFWMGoPtaXdpslR3NLvhgXvMMs implements Runnable {
    private final /* synthetic */ AutofillManager f$0;
    private final /* synthetic */ IResultReceiver f$1;

    public /* synthetic */ -$$Lambda$AutofillManager$AutofillManagerClient$eeFWMGoPtaXdpslR3NLvhgXvMMs(AutofillManager autofillManager, IResultReceiver iResultReceiver) {
        this.f$0 = autofillManager;
        this.f$1 = iResultReceiver;
    }

    public final void run() {
        this.f$0.getAugmentedAutofillClient(this.f$1);
    }
}
