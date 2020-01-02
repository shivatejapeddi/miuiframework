package android.telephony.ims;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$8xq93ap6i0L56Aegaj-ZEUt9ISc implements ThrowingRunnable {
    private final /* synthetic */ RegistrationBinder f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$8xq93ap6i0L56Aegaj-ZEUt9ISc(RegistrationBinder registrationBinder, int i) {
        this.f$0 = registrationBinder;
        this.f$1 = i;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onRegistered$1$ImsMmTelManager$RegistrationCallback$RegistrationBinder(this.f$1);
    }
}
