package android.service.autofill.augmented;

import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AugmentedAutofillService$1$LSvI4QN2NxJLegcZI0BFIvKwp6o implements Consumer {
    public static final /* synthetic */ -$$Lambda$AugmentedAutofillService$1$LSvI4QN2NxJLegcZI0BFIvKwp6o INSTANCE = new -$$Lambda$AugmentedAutofillService$1$LSvI4QN2NxJLegcZI0BFIvKwp6o();

    private /* synthetic */ -$$Lambda$AugmentedAutofillService$1$LSvI4QN2NxJLegcZI0BFIvKwp6o() {
    }

    public final void accept(Object obj) {
        ((AugmentedAutofillService) ((AugmentedAutofillService) obj)).handleOnDestroyAllFillWindowsRequest();
    }
}
