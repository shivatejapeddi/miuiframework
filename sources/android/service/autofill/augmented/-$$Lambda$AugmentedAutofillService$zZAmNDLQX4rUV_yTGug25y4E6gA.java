package android.service.autofill.augmented;

import java.util.function.Consumer;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$AugmentedAutofillService$zZAmNDLQX4rUV_yTGug25y4E6gA implements Consumer {
    public static final /* synthetic */ -$$Lambda$AugmentedAutofillService$zZAmNDLQX4rUV_yTGug25y4E6gA INSTANCE = new -$$Lambda$AugmentedAutofillService$zZAmNDLQX4rUV_yTGug25y4E6gA();

    private /* synthetic */ -$$Lambda$AugmentedAutofillService$zZAmNDLQX4rUV_yTGug25y4E6gA() {
    }

    public final void accept(Object obj) {
        ((AugmentedAutofillService) obj).handleOnUnbind();
    }
}
