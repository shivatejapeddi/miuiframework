package android.service.autofill.augmented;

import com.android.internal.util.function.TriConsumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AugmentedAutofillService$1$4dXh5Zwc8KxDD9bV1LFhgo3zrgk implements TriConsumer {
    public static final /* synthetic */ -$$Lambda$AugmentedAutofillService$1$4dXh5Zwc8KxDD9bV1LFhgo3zrgk INSTANCE = new -$$Lambda$AugmentedAutofillService$1$4dXh5Zwc8KxDD9bV1LFhgo3zrgk();

    private /* synthetic */ -$$Lambda$AugmentedAutofillService$1$4dXh5Zwc8KxDD9bV1LFhgo3zrgk() {
    }

    public final void accept(Object obj, Object obj2, Object obj3) {
        ((AugmentedAutofillService) ((AugmentedAutofillService) obj)).handleOnConnected(((Boolean) obj2).booleanValue(), ((Boolean) obj3).booleanValue());
    }
}
