package android.view.autofill;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AutofillManager$AugmentedAutofillManagerClient$tbNtqpHgXnRdc3JO5HaBlxclFg0 implements Runnable {
    private final /* synthetic */ AutofillManager f$0;
    private final /* synthetic */ AutofillId f$1;

    public /* synthetic */ -$$Lambda$AutofillManager$AugmentedAutofillManagerClient$tbNtqpHgXnRdc3JO5HaBlxclFg0(AutofillManager autofillManager, AutofillId autofillId) {
        this.f$0 = autofillManager;
        this.f$1 = autofillId;
    }

    public final void run() {
        this.f$0.requestHideFillUi(this.f$1, false);
    }
}
