package android.telephony.ims;

import com.android.internal.util.FunctionalUtils.ThrowingRunnable;

/* compiled from: lambda */
public final /* synthetic */ class -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$F58PRHsH__07pmyvC0NTRprfEPU implements ThrowingRunnable {
    private final /* synthetic */ RegistrationBinder f$0;
    private final /* synthetic */ ImsReasonInfo f$1;

    public /* synthetic */ -$$Lambda$ImsMmTelManager$RegistrationCallback$RegistrationBinder$F58PRHsH__07pmyvC0NTRprfEPU(RegistrationBinder registrationBinder, ImsReasonInfo imsReasonInfo) {
        this.f$0 = registrationBinder;
        this.f$1 = imsReasonInfo;
    }

    public final void runOrThrow() {
        this.f$0.lambda$onDeregistered$5$ImsMmTelManager$RegistrationCallback$RegistrationBinder(this.f$1);
    }
}
